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

import java.net.URL;
import java.util.Collections;
import java.util.Map;

import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.arakhne.afc.progress.Progression;

/** Accessor to the online Scimago platform.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.5
 * @see "https://www.scimagojr.com"
 */
public interface ScimagoPlatform {

	/** Name that could be used for retrieving the best quartile for a journal.
	 *
	 * @see #getJournalRanking(int, String)
	 */
	String BEST = "~BEST"; //$NON-NLS-1$

	/** Replies the URL of the quartile picture for a journal on Scimago.
	 *
	 * @param journalId the identifier of the journal.
	 * @return the URL for the journal picture.
	 */
	URL getJournalPictureUrl(String journalId);

	/** Replies the URL of a journal on Scimago.
	 *
	 * @param journalId the identifier of the journal.
	 * @return the URL for the journal.
	 */
	URL getJournalUrl(String journalId);

	/** Replies the URL for obtaining the CSV data for the journals.
	 *
	 * @param year the year for which the journal data must be retrieved from the Scimago platform.
	 * @return the URL to access to the journal CSV.
	 */
	URL getJournalCsvUrl(int year);

	/** Replies the ranking descriptions for all the journals and for the given year.
	 * The ranking descriptions maps journal identifier to a single ranking description.
	 * Each ranking description provides the quartiles per scientific topics. The key
	 * {@link #BEST} represents the best quartile from Scimago database.
	 *
	 * @param year the reference year.
	 * @param csvUrl the URL of the CSV file.
	 * @param progress progress monitor.
	 * @return the ranking descriptions for all the journals.
	 * @throws Exception if rankings cannot be read.
	 */
	Map<String, Map<String, QuartileRanking>> getJournalRanking(int year, URL csvUrl, Progression progress) throws Exception;

	/** Replies the ranking descriptions for all the journals and for the given year.
	 * The ranking descriptions maps journal identifier to a single ranking description.
	 * Each ranking description provides the quartiles per scientific topics. The key
	 * {@link #BEST} represents the best quartile from Scimago database.
	 *
	 * @param year the reference year.
	 * @param progress progress monitor.
	 * @return the ranking descriptions for all the journals.
	 * @throws Exception if rankings cannot be read.
	 */
	default Map<String, Map<String, QuartileRanking>> getJournalRanking(int year, Progression progress) throws Exception {
		return getJournalRanking(year, getJournalCsvUrl(year), progress);
	}

	/** Replies the ranking description for the journal with the given identifier and for the given year.
	 * The ranking description provides the quartiles per scientific topics. The key
	 * {@link #BEST} represents the best quartile from Scimago database.
	 *
	 * @param year the reference year.
	 * @param csvUrl the URL of the CSV file.
	 * @param journalId the identifier of the journal on Scimago.
	 * @param progress progress monitor.
	 * @return the ranking description for the journal.
	 * @throws Exception if rankings cannot be read.
	 */
	default Map<String, QuartileRanking> getJournalRanking(int year, URL csvUrl, String journalId, Progression progress) throws Exception {
		final Map<String, Map<String, QuartileRanking>> rankings0 = getJournalRanking(year, csvUrl, progress);
		final Map<String, QuartileRanking> rankings1 = rankings0.get(journalId);
		if (rankings1 == null) {
			return Collections.emptyMap();
		}
		return Collections.unmodifiableMap(rankings1);
	}

	/** Replies the ranking description for the journal with the given identifier and for the given year.
	 * The ranking description provides the quartiles per scientific topics. The key
	 * {@link #BEST} represents the best quartile from Scimago database.
	 *
	 * @param year the reference year.
	 * @param journalId the identifier of the journal on Scimago.
	 * @param progress progress monitor.
	 * @return the ranking description for the journal.
	 * @throws Exception if rankings cannot be read.
	 */
	default Map<String, QuartileRanking> getJournalRanking(int year, String journalId, Progression progress) throws Exception {
		return getJournalRanking(year, getJournalCsvUrl(year), journalId, progress);
	}

}
