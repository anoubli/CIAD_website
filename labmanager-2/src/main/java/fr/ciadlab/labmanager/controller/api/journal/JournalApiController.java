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

package fr.ciadlab.labmanager.controller.api.journal;

import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** REST Controller for journals.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see JournalService
 */
@RestController
@CrossOrigin
public class JournalApiController extends AbstractApiController {

	private JournalService journalService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param journalService the journal service.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public JournalApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JournalService journalService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.journalService = journalService;
	}

	/** Replies data about a specific journal from the database.
	 * This endpoint accepts one of the two parameters: the name or the identifier of the journal.
	 *
	 * @param name the name of the journal.
	 * @param id the identifier of the journal.
	 * @return the journal.
	 */
	@GetMapping(value = "/getJournalData", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Journal getJournalData(@RequestParam(required = false) String name, @RequestParam(required = false) Integer id) {
		final String inName = inString(name);
		if (id == null && Strings.isNullOrEmpty(inName)) {
			throw new IllegalArgumentException("Name and identifier parameters are missed"); //$NON-NLS-1$
		}
		if (id != null) {
			return this.journalService.getJournalById(id.intValue());
		}
		return this.journalService.getJournalByName(inName);
	}

	/** Replies the quality indicators for a specific journal.
	 * This endpoint accepts one of the two parameters: the name or the identifier of the journal.
	 * If the years are provided, only the quality indicators for these years are replied. Otherwise,
	 * all the quality indicators are replied.
	 *
	 * @param journal the name or the identifier of the journal. If this argument is numeric, it is assumed to be the journal identifier.
	 *     otherwise it is the name of the journal.
	 * @param years the list of years for which the indicators must be replied.
	 * @return a map of quality indicators per year.
	 */
	@GetMapping(value = "/getJournalQualityIndicators")
	@ResponseBody
	public Map<Integer, JournalQualityAnnualIndicators> getJournalQualityIndicators(
			@RequestParam(required = true) String journal,
			@RequestParam(required = false, name = "year") List<Integer> years) {
		final String inJournal = inString(journal);
		final Journal journalObj = getJournalWith(inJournal, this.journalService);
		if (journalObj == null) {
			throw new IllegalArgumentException("Journal not found with: " + inJournal); //$NON-NLS-1$
		}
		if (years != null && !years.isEmpty()) {
			final Map<Integer, JournalQualityAnnualIndicators> indicators = new HashMap<>();
			for (final Integer year : years) {
				if (year != null) {
					indicators.computeIfAbsent(year, it -> {
						return journalObj.getQualityIndicatorsForYear(year.intValue());
					});
				}
			}
			return indicators;
		}
		return journalObj.getQualityIndicators();
	}

	/** Saving information of a journal. 
	 *
	 * @param journal the identifier of the journal. If the identifier is not provided, this endpoint is supposed to create
	 *     a journal in the database.
	 * @param name the name of the journal.
	 * @param address the address of the publisher of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param isbn the ISBN number for the journal.
	 * @param issn the ISSN number for the journal.
	 * @param openAccess indicates if the journal is open access or not.
	 * @param journalUrl the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier to the page of the journal on the Scimago website.
	 * @param wosId the identifier to the page of the journal on the Web-Of-Science website.
	 * @param validated indicates if the journal is validated by a local authority.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case the journal cannot be saved
	 */
	@PutMapping(value = "/" + Constants.JOURNAL_SAVING_ENDPOINT)
	public void saveJournal(
			@RequestParam(required = false) Integer journal,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String address,
			@RequestParam(required = false) String publisher,
			@RequestParam(required = false) String isbn,
			@RequestParam(required = false) String issn,
			@RequestParam(required = false) Boolean openAccess,
			@RequestParam(required = false) String journalUrl,
			@RequestParam(required = false) String scimagoId,
			@RequestParam(required = false) String wosId,
			@RequestParam(required = false, defaultValue = "false") boolean validated,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.JOURNAL_SAVING_ENDPOINT, journal);
		final Journal optJournal;
		//
		final String inName = inString(name);
		final String inAddress = inString(address);
		final String inPublisher = inString(publisher);
		final String inIsbn = inString(isbn);
		final String inIssn = inString(issn);
		final String inJournalUrl = inString(journalUrl);
		final String inScimagoId = inString(scimagoId);
		final String inWosId = inString(wosId);
		//
		if (journal == null) {
			optJournal = this.journalService.createJournal(
					validated, inName, inAddress, inPublisher, inIsbn, inIssn,
					openAccess, inJournalUrl, inScimagoId, inWosId);
		} else {
			optJournal = this.journalService.updateJournal(journal.intValue(),
					validated, inName, inAddress, inPublisher, inIsbn, inIssn,
					openAccess, inJournalUrl, inScimagoId, inWosId);
		}
		if (optJournal == null) {
			throw new IllegalStateException("Journal not found"); //$NON-NLS-1$
		}
	}

	/** Delete a journal from the database.
	 *
	 * @param journal the identifier of the journal.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/deleteJournal")
	public void deleteJournal(
			@RequestParam Integer journal,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, "deleteJournal", journal); //$NON-NLS-1$
		if (journal == null || journal.intValue() == 0) {
			throw new IllegalStateException("Journal not found"); //$NON-NLS-1$
		}
		this.journalService.removeJournal(journal.intValue());
	}

	/** Saving ranking of a journal. 
	 *
	 * @param journal the identifier of the journal.
	 * @param year the identifier of the journal.
	 * @param impactFactor the impact factor of the journal for the given year.
	 * @param scimagoQIndex the Scimago's Q-Index of the journal for the given year.
	 * @param wosQIndex the WoS's Q-Index of the journal for the given year.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case the journal ranking cannot be saved
	 */
	@PutMapping(value = "/" + Constants.SAVE_JOURNAL_RANKING_ENDPOINT)
	public void saveJournalRanking(
			@RequestParam(required = true) int journal,
			@RequestParam(required = true) int year,
			@RequestParam(required = false) Float impactFactor,
			@RequestParam(required = false) String scimagoQIndex,
			@RequestParam(required = false) String wosQIndex,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.SAVE_JOURNAL_RANKING_ENDPOINT, Integer.valueOf(journal));
		final Journal journalObj = this.journalService.getJournalById(journal);
		if (journalObj == null) {
			throw new IllegalArgumentException("Journal not found with: " + journal); //$NON-NLS-1$
		}
		final float realImpactFactor = impactFactor == null || impactFactor.floatValue() < 0f ? 0f : impactFactor.floatValue();
		final String inScimagoQIndex = inString(scimagoQIndex);
		final QuartileRanking scimago = inScimagoQIndex == null ? null : QuartileRanking.valueOf(inScimagoQIndex);
		final String inWosQIndex = inString(wosQIndex);
		final QuartileRanking wos = inWosQIndex == null ? null : QuartileRanking.valueOf(inWosQIndex);
		this.journalService.setQualityIndicators(journalObj, year, realImpactFactor, scimago, wos);
	}

	/** Delete a journal ranking from the database.
	 *
	 * @param journal the identifier of the journal.
	 * @param year the identifier of the journal.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.DELETE_JOURNAL_RANKING_ENDPOINT)
	public void deleteJournalRanking(
			@RequestParam(required = true) int journal,
			@RequestParam(required = true) int year,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.DELETE_JOURNAL_RANKING_ENDPOINT, Integer.valueOf(journal), Integer.valueOf(year));
		final Journal journalObj = this.journalService.getJournalById(journal);
		if (journalObj == null) {
			throw new IllegalArgumentException("Journal not found with: " + journal); //$NON-NLS-1$
		}
		this.journalService.deleteQualityIndicators(journalObj, year);
	}

	/** Replies Json that describes an update of the journal indicators for the given refence year.
	 *
	 * @param referenceYear the reference year.
	 * @param wosCsvFile the uploaded CSV file from web-of-science.
	 * @return the JSON.
	 * @throws Exception in case of error.
	 */
	@PostMapping(value = "/" + Constants.JOURNAL_INDICATOR_UPDATES_ENDPOINT)
	@ResponseBody
	public JsonNode getJournalUpdateJson(
			@RequestParam(required = true) int referenceYear,
			@RequestParam(required = false) MultipartFile wosCsvFile) throws Exception {
		if (wosCsvFile == null || wosCsvFile.isEmpty()) {
			return this.journalService.getJournalIndicatorUpdates(referenceYear, null, null);
		}
		try (InputStream is = wosCsvFile.getInputStream()) {
			return this.journalService.getJournalIndicatorUpdates(referenceYear, is, null);
		}
	}

	/** Save the updates of the journals' quality indicators.
	 *
	 * @param data the map of the changes. Expected keys are {@code referenceYear} for the reference year; and
	 *     {@code changes} for the changes to apply to the quality indicators.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/" + Constants.SAVE_JOURNAL_INDICATOR_UPDATES_ENDPOINT)
	public void saveJournalIndicatorUpdates(
			@RequestParam(required = true) Map<String, String> data,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.SAVE_JOURNAL_INDICATOR_UPDATES_ENDPOINT);
		final int referenceYear = ensureYear(data, "referenceYear"); //$NON-NLS-1$
		final String rawChanges = ensureString(data, "changes"); //$NON-NLS-1$
		if (Strings.isNullOrEmpty(rawChanges)) {
			throw new IllegalArgumentException("changes are expected"); //$NON-NLS-1$
		}
		final Map<String, Map<String, ?>> changes;
		try (StringReader sr = new StringReader(rawChanges)) {
			final ObjectMapper mapper = new ObjectMapper();
			try (JsonParser parser = mapper.createParser(sr)) {
				changes = parser.readValueAs(Map.class);
			}
		}
		this.journalService.updateJournalIndicators(referenceYear, changes);
	}

}
