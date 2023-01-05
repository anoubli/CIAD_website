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

package fr.ciadlab.labmanager.io.scimago;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** Accessor to the online Scimago platform.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.5
 * @see "https://www.scimagojr.com"
 */
@Component
@Primary
public class OnlineScimagoPlatform implements ScimagoPlatform {

	/** Name of the column for the journal id.
	 */
	protected static final String SOURCE_ID_COLUMN = "Sourceid"; //$NON-NLS-1$

	/** Name of the column for the journal categories and their quartiles.
	 */
	protected static final String CATEGORY_COLUMN = "Categories"; //$NON-NLS-1$

	/** Name of the column for the journal best quartile.
	 */
	protected static final String BEST_QUARTILE_COLUMN = "SJR Best Quartile"; //$NON-NLS-1$

	private static final String SCHEME = "https"; //$NON-NLS-1$

	private static final String HOST = "www.scimagojr.com"; //$NON-NLS-1$

	private static final String RANK_PATH = "journalrank.php"; //$NON-NLS-1$

	private static final String JOURNAL_PATH = "journalsearch.php"; //$NON-NLS-1$

	private static final String JOURNAL_PICTURE_PATH = "journal_img.php"; //$NON-NLS-1$

	private static final String YEAR_PARAM = "year"; //$NON-NLS-1$

	private static final String OUT_PARAM = "out"; //$NON-NLS-1$

	private static final String CSV_TYPE = "xls"; //$NON-NLS-1$

	private static final String TIP_PARAM = "tip"; //$NON-NLS-1$

	private static final String SID_TYPE = "sid"; //$NON-NLS-1$

	private static final String QUERY_PARAM = "q"; //$NON-NLS-1$

	private static final String ID_PARAM = "id"; //$NON-NLS-1$

	/** Factory of URI builder.
	 */
	protected final UriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();

	private final Map<Integer, Map<String, Map<String, QuartileRanking>>> rankingCache = new ConcurrentHashMap<>();

	@Override
	public URL getJournalPictureUrl(String journalId) {
		if (!Strings.isNullOrEmpty(journalId)) {
			try {
				UriBuilder builder = this.uriBuilderFactory.builder();
				builder = builder.scheme(SCHEME);
				builder = builder.host(HOST);
				builder = builder.path(JOURNAL_PICTURE_PATH);
				builder = builder.queryParam(ID_PARAM, journalId);
				final URI uri = builder.build();
				return uri.toURL();
			} catch (Exception ex) {
				//
			}
		}
		return null;
	}

	@Override
	public URL getJournalUrl(String journalId) {
		if (!Strings.isNullOrEmpty(journalId)) {
			try {
				UriBuilder builder = this.uriBuilderFactory.builder();
				builder = builder.scheme(SCHEME);
				builder = builder.host(HOST);
				builder = builder.path(JOURNAL_PATH);
				builder = builder.queryParam(TIP_PARAM, SID_TYPE);
				builder = builder.queryParam(QUERY_PARAM, journalId);
				final URI uri = builder.build();
				return uri.toURL();
			} catch (Exception ex) {
				//
			}
		}
		return null;
	}

	@Override
	public URL getJournalCsvUrl(int year) {
		try {
			UriBuilder builder = this.uriBuilderFactory.builder();
			builder = builder.scheme(SCHEME);
			builder = builder.host(HOST);
			builder = builder.path(RANK_PATH);
			builder = builder.queryParam(OUT_PARAM, CSV_TYPE);
			builder = builder.queryParam(YEAR_PARAM, Integer.valueOf(year));
			final URI uri = builder.build();
			return uri.toURL();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static void analyzeCsvRecord(Integer categoryColumn, Integer bestQuartileColumn, String[] row,
			BiConsumer<String, QuartileRanking> callback) {
		if (categoryColumn != null) {
			final String rawCategories = row[categoryColumn.intValue()];
			if (!Strings.isNullOrEmpty(rawCategories)) {
				final String[] categories = rawCategories.split("\\s*[,;]\\s*"); //$NON-NLS-1$
				final Pattern pattern = Pattern.compile("\\s*(.+?)\\s*\\(([^\\)]+)\\)\\s*"); //$NON-NLS-1$
				for (final String rawCategory : categories) {
					final Matcher matcher = pattern.matcher(rawCategory);
					if (matcher.matches()) {
						try {
							final QuartileRanking quartile = QuartileRanking.valueOfCaseInsensitive(matcher.group(2));
							final String name = matcher.group(1);
							if (!Strings.isNullOrEmpty(name)) {
								callback.accept(name.toLowerCase(), quartile);
							}
						} catch (Throwable ex) {
							//
						}
					}
				}
			}
		}
		if (bestQuartileColumn != null) {
			try {
				final QuartileRanking quartile = QuartileRanking.valueOfCaseInsensitive(row[bestQuartileColumn.intValue()]);
				callback.accept(BEST, quartile);
			} catch (Throwable ex) {
				//
			}
		}
	}

	@SuppressWarnings("resource")
	private static void analyzeCsvRecords(URL csvUrl, Progression progress, Consumer4 consumer) {
		progress.setProperties(0, 0, 100, false);
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(csvUrl.openStream()))) {
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
				throw new IOException("Unable to find the column \"" + SOURCE_ID_COLUMN + "\" in the Scimago CSV data source"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			Integer bestQuartileColumn = null;
			Integer categoryColumn = null;
			Integer sourceIdColumn = null;
			int i = 0;
			while (i < row.length && (sourceIdColumn == null || categoryColumn == null || bestQuartileColumn == null)) {
				final String name = row[i];
				if (sourceIdColumn == null && SOURCE_ID_COLUMN.equalsIgnoreCase(name)) {
					sourceIdColumn = Integer.valueOf(i);
				}
				if (categoryColumn == null && CATEGORY_COLUMN.equalsIgnoreCase(name)) {
					categoryColumn = Integer.valueOf(i);
				}
				if (bestQuartileColumn == null && BEST_QUARTILE_COLUMN.equalsIgnoreCase(name)) {
					bestQuartileColumn = Integer.valueOf(i);
				}
				++i;
			}
			if (sourceIdColumn == null) {
				throw new IOException("Unable to find the column \"" + SOURCE_ID_COLUMN + "\" in the Scimago CSV data source"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (bestQuartileColumn == null && categoryColumn == null) {
				throw new IOException("No column for quartiles in the Scimago CSV data source"); //$NON-NLS-1$
			}
			progress.increment();
			// Read records
			consumer.accept(csvReader, sourceIdColumn, categoryColumn, bestQuartileColumn, progress);
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			progress.end();
		}
	}
	
	private static Map<String, Map<String, QuartileRanking>> readJournalRanking(URL csvUrl, Progression rootProgress) {
		final Map<String, Map<String, QuartileRanking>> ranking = new TreeMap<>();
		analyzeCsvRecords(csvUrl, rootProgress, (stream, sourceIdColumn, categoryColumn, bestQuartileColumn, progress) -> {
			String[] row = stream.readNext();
			final Progression rowProgress = progress.subTask(99, 0, row == null ? 0 : row.length);
			while (row != null) {
				final Map<String, QuartileRanking> journalRanking = new HashMap<>();
				analyzeCsvRecord(categoryColumn, bestQuartileColumn, row,
						(a, b) -> journalRanking.put(a, b));
				if (!journalRanking.isEmpty()) {
					final String journalId = row[sourceIdColumn.intValue()];
					ranking.put(journalId, journalRanking);
				}
				rowProgress.increment();
				row = stream.readNext();
			}
			rowProgress.end();
		});
		return ranking;
	}

	private static Progression ensureProgress(Progression progress) {
		return progress == null ? new DefaultProgression() : progress;
	}

	@Override
	public Map<String, Map<String, QuartileRanking>> getJournalRanking(int year, URL csvUrl, Progression progress) throws Exception {
		return this.rankingCache.computeIfAbsent(Integer.valueOf(year), it -> readJournalRanking(csvUrl, ensureProgress(progress)));
	}

	/** Call back for {@link OnlineScimagoPlatform#analyzeCsvRecords(int, Consumer)}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.5
	 * @see "https://www.scimagojr.com"
	 */
	@FunctionalInterface
	private interface Consumer4 {

		/** Callback.
		 * 
		 * @param stream the stream of CSV records.
		 * @param sourceIdColumn the column for the journal indentifier.
		 * @param categoryColumn the column for the categories.
		 * @param bestQuartileColumn the column for the best quartile.
		 * @param progress a progress monitor.
		 * @throws Exception if the CSV cannot be read.
		 */
		void accept(CSVReader stream, Integer sourceIdColumn, Integer categoryColumn, Integer bestQuartileColumn,
				Progression progress) throws Exception;

	}

}
