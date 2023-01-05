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

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.arakhne.afc.progress.Progression;

/** Accessor to the online Web-of-Science platform.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.5
 * @see "https://www.webofscience.com"
 */
public interface WebOfSciencePlatform {

	/** Replies the ranking descriptions for all the journals and for the given year.
	 * The ranking descriptions maps journal identifier to a single ranking description.
	 * Each ranking description provides the quartiles per scientific topics.
	 *
	 * @param year the reference year.
	 * @param csvUrl the URL of the CSV file.
	 * @param progress progress monitor.
	 * @return the ranking descriptions for all the journals.
	 * @throws Exception if rankings cannot be read.
	 */
	default Map<String, WebOfScienceJournal> getJournalRanking(int year, URL csvUrl, Progression progress) throws Exception {
		try (InputStream is = csvUrl.openStream()) {
			return getJournalRanking(year, is, progress);
		}
	}

	/** Replies the ranking description for the journal with the given identifier and for the given year.
	 * The ranking description provides the quartiles per scientific topics.
	 *
	 * @param year the reference year.
	 * @param csvUrl the URL of the CSV file.
	 * @param journalId the identifier of the journal on WoS.
	 * @param progress progress monitor.
	 * @return the ranking description for the journal.
	 * @throws Exception if rankings cannot be read.
	 */
	default WebOfScienceJournal getJournalRanking(int year, URL csvUrl, String journalId, Progression progress) throws Exception {
		try (InputStream is = csvUrl.openStream()) {
			return getJournalRanking(year, is, journalId, progress);
		}
	}

	/** Replies the ranking descriptions for all the journals and for the given year.
	 * The ranking descriptions maps journal identifier to a single ranking description.
	 * Each ranking description provides the quartiles per scientific topics.
	 *
	 * @param year the reference year.
	 * @param csv the stream of the CSV file.
	 * @param progress progress monitor.
	 * @return the ranking descriptions for all the journals.
	 * @throws Exception if rankings cannot be read.
	 */
	Map<String, WebOfScienceJournal> getJournalRanking(int year, InputStream csv, Progression progress) throws Exception;

	/** Replies the ranking description for the journal with the given identifier and for the given year.
	 * The ranking description provides the quartiles per scientific topics.
	 *
	 * @param year the reference year.
	 * @param csv the stream of the CSV file.
	 * @param journalId the identifier of the journal on WoS.
	 * @param progress progress monitor.
	 * @return the ranking description for the journal.
	 * @throws Exception if rankings cannot be read.
	 */
	default WebOfScienceJournal getJournalRanking(int year, InputStream csv, String journalId, Progression progress) throws Exception {
		final Map<String, WebOfScienceJournal> rankings0 = getJournalRanking(year, csv, progress);
		final String normalizedId = normalizeIssn(journalId);
		final WebOfScienceJournal rankings1 = rankings0.get(normalizedId);
		return rankings1;
	}

	/** Normalize the ISSN number from WoS in order to be used as an journal identifier in {@link #getJournalRanking(int, InputStream, Progression)}.
	 *
	 * @param wosIssn the ISSN number to normalize.
	 * @return the normalized identifier.
	 */
	default String normalizeIssn(String wosIssn) {
		if (Strings.isNullOrEmpty(wosIssn)) {
			return null;
		}
		return wosIssn.replaceAll("[^0-9a-zA-Z]+", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Accessor to the online Web-of-Science platform.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.5
	 * @see "https://www.webofscience.com"
	 */
	public class WebOfScienceJournal implements Serializable {

		private static final long serialVersionUID = -5636236034630289851L;

		/** The quartiles of the journal per category.
		 */
		public final Map<String, QuartileRanking> quartiles;

		/** The impact factor of the journal.
		 */
		public final float impactFactor;

		/** Constructor.
		 *
		 * @param rankings the rankings per category.
		 * @param impactFactor the impact factor.
		 */
		public WebOfScienceJournal(Map<String, QuartileRanking> rankings, float impactFactor) {
			this.quartiles = Collections.unmodifiableMap(rankings);
			this.impactFactor = impactFactor;
		}

	}

}
