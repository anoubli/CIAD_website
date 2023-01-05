/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.io.wos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.progress.DefaultProgression;
import org.arakhne.afc.progress.Progression;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Accessor to the online Web-of-Science platform.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.5
 * @see "https://www.webofscience.com"
 */
@Component
@Primary
public class OnlineWebOfSciencePlatform implements WebOfSciencePlatform {

	/** Name of the column for the journal ISSN.
	 */
	protected static final String ISSN_COLUMN = "ISSN"; //$NON-NLS-1$

	/** Name of the column for the journal E-ISSN.
	 */
	protected static final String EISSN_COLUMN = "EISSN"; //$NON-NLS-1$

	/** Name of the column for the journal quartiles.
	 */
	protected static final String CATEGORY_COLUMN = "Category & Journal Quartiles"; //$NON-NLS-1$

	/** Prefix for the name of the column for the journal impact factor.
	 */
	protected static final String IMPACT_FACTOR_COLUMN_PREFIX = "IF"; //$NON-NLS-1$

	private final Map<Integer, Map<String, WebOfScienceJournal>> rankingCache = new ConcurrentHashMap<>();

	private static Progression ensureProgress(Progression progress) {
		return progress == null ? new DefaultProgression() : progress;
	}

	private static WebOfScienceJournal analyzeCsvRecord(Integer categoryColumn, Integer impactFactorColumn, String[] row) {
		final Map<String, QuartileRanking> quartiles = new TreeMap<>();
		if (categoryColumn != null) {
			final String rawCategories = row[categoryColumn.intValue()];
			if (!Strings.isNullOrEmpty(rawCategories)) {
				final String[] categories = rawCategories.split("\\s*[;]\\s*"); //$NON-NLS-1$
				final Pattern pattern = Pattern.compile("\\s*(.+?)(?:\\s*\\-.*)?\\s*\\(([^\\)]+)\\)\\s*"); //$NON-NLS-1$
				for (final String rawCategory : categories) {
					final Matcher matcher = pattern.matcher(rawCategory);
					if (matcher.matches()) {
						try {
							final QuartileRanking quartile = QuartileRanking.valueOfCaseInsensitive(matcher.group(2));
							final String name = matcher.group(1);
							if (!Strings.isNullOrEmpty(name)) {
								quartiles.put(name.toLowerCase(), quartile);
							}
						} catch (Throwable ex) {
							//
						}
					}
				}
			}
		}
		float impactFactor = 0f;
		if (impactFactorColumn != null) {
			final String rawIf = row[impactFactorColumn.intValue()];
			if (!Strings.isNullOrEmpty(rawIf)) {
				try {
					impactFactor = Float.parseFloat(rawIf);
				} catch (Throwable ex) {
					impactFactor = 0f;
				}
			}
		}
		if (!quartiles.isEmpty()) {
			return new WebOfScienceJournal(quartiles, impactFactor);
		}
		return null;
	}

	@SuppressWarnings("resource")
	private static void analyzeCsvRecords(InputStream csv, Progression progress, Consumer4 consumer) {
		progress.setProperties(0, 0, 100, false);
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(csv))) {
			final CSVParserBuilder parserBuilder = new CSVParserBuilder();
			parserBuilder.withSeparator(';');
			parserBuilder.withIgnoreLeadingWhiteSpace(true);
			parserBuilder.withQuoteChar('"');
			parserBuilder.withStrictQuotes(false);
			final CSVReaderBuilder csvBuilder = new CSVReaderBuilder(reader);
			csvBuilder.withCSVParser(parserBuilder.build());
			final CSVReader csvReader = csvBuilder.build();
			// Search for the column headers
			String[] row = csvReader.readNext();
			if (row == null) {
				throw new IOException("Unable to find the column \"" + ISSN_COLUMN + "\" in the WoS CSV data source"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			Integer categoryColumn = null;
			Integer issnColumn = null;
			Integer eissnColumn = null;
			Integer ifColumn = null;
			final String prefix = IMPACT_FACTOR_COLUMN_PREFIX + " "; //$NON-NLS-1$
			int i = 0;
			while (i < row.length && ((issnColumn == null && eissnColumn == null) || categoryColumn == null || ifColumn == null)) {
				final String name = row[i];
				if (issnColumn == null && ISSN_COLUMN.equalsIgnoreCase(name)) {
					issnColumn = Integer.valueOf(i);
				}
				if (eissnColumn == null && EISSN_COLUMN.equalsIgnoreCase(name)) {
					eissnColumn = Integer.valueOf(i);
				}
				if (categoryColumn == null && CATEGORY_COLUMN.equalsIgnoreCase(name)) {
					categoryColumn = Integer.valueOf(i);
				}
				if (ifColumn == null && (IMPACT_FACTOR_COLUMN_PREFIX.equalsIgnoreCase(name) || (name != null && name.startsWith(prefix)))) {
					ifColumn = Integer.valueOf(i);
				}
				++i;
			}
			if (issnColumn == null && eissnColumn == null) {
				throw new IOException("Unable to find the column \"" + ISSN_COLUMN + "\" in the WoS CSV data source"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (categoryColumn == null) {
				throw new IOException("No column for quartiles in the WoS CSV data source"); //$NON-NLS-1$
			}
			progress.increment();
			// Read records
			consumer.accept(csvReader, issnColumn, eissnColumn, categoryColumn, ifColumn, progress);
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			progress.end();
		}
	}
	
	private Map<String, WebOfScienceJournal> readJournalRanking(InputStream csv, Progression rootProgress) {
		final Map<String, WebOfScienceJournal> ranking = new TreeMap<>();
		analyzeCsvRecords(csv, rootProgress, (stream, issnColumn, eissnColumn, categoryColumn, ifColumn, progress) -> {
			String[] row = stream.readNext();
			final Progression rowProgress = progress.subTask(99, 0, row == null ? 0 : row.length);
			while (row != null) {
				final WebOfScienceJournal journalRanking = analyzeCsvRecord(categoryColumn, ifColumn, row);
				if (journalRanking != null) {
					// Put the ranking object two times in the map: one for the issn and one for the eissn 
					if (issnColumn != null) {
						final String journalId = normalizeIssn(row[issnColumn.intValue()]);
						if (!Strings.isNullOrEmpty(journalId)) {
							ranking.put(journalId, journalRanking);
						}
					}
					if (eissnColumn != null) {
						final String journalId = normalizeIssn(row[eissnColumn.intValue()]);
						if (!Strings.isNullOrEmpty(journalId)) {
							ranking.put(journalId, journalRanking);
						}
					}
				}
				rowProgress.increment();
				row = stream.readNext();
			}
			rowProgress.end();
		});
		return ranking;
	}

	@Override
	public Map<String, WebOfScienceJournal> getJournalRanking(int year, InputStream csv, Progression progress)
			throws Exception {
		return this.rankingCache.computeIfAbsent(Integer.valueOf(year), it -> readJournalRanking(csv, ensureProgress(progress)));
	}

	/** Call back for analyzing the journal CSV.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.5
	 * @see "https://www.webofscience.com"
	 */
	@FunctionalInterface
	private interface Consumer4 {

		/** Callback.
		 * 
		 * @param stream the stream of CSV records.
		 * @param issnColumn the column for the journal ISSN.
		 * @param eissnColumn the column for the journal E-ISSN.
		 * @param categoryColumn the column for the categories.
		 * @param impactFactorColumn the column for the impact factor.
		 * @param progress a progress monitor.
		 * @throws Exception if the CSV cannot be read.
		 */
		void accept(CSVReader stream, Integer issnColumn, Integer eissnColumn, Integer categoryColumn,
				Integer impactFactorColumn, Progression progress) throws Exception;

	}

}
