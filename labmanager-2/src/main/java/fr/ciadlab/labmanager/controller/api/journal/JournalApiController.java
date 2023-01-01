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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;

import javax.enterprise.inject.Produces;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	 */
	public JournalApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JournalService journalService) {
		super(messages, constants);
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
		if (id == null && Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Name and identifier parameters are missed"); //$NON-NLS-1$
		}
		if (id != null) {
			return this.journalService.getJournalById(id.intValue());
		}
		return this.journalService.getJournalByName(name);
	}
		
	
	@GetMapping(value = "/hello", produces = "application/json; charset=UTF-8")
	@ResponseBody 
	public List<Journal> getAllJournal()
	{		
		return this.journalService.getAllJournals();
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
		final Journal journalObj = getJournalWith(journal, this.journalService);
		if (journalObj == null) {
			throw new IllegalArgumentException("Journal not found with: " + journal); //$NON-NLS-1$
		}
		if (years != null && !years.isEmpty()) {
			final Map<Integer, JournalQualityAnnualIndicators> indicators = new HashMap<>();
			for (final Integer year : years) {
				if (year != null) {
					indicators.computeIfAbsent(year, it -> {
						return journalObj.getQualityIndicatorsFor(year.intValue(), null);
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
	 * @param username the name of the logged-in user.
	 * @throws Exception in case the journal cannot be saved
	 */
	@PostMapping(value = "/" + Constants.JOURNAL_SAVING_ENDPOINT)
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
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /" + Constants.JOURNAL_SAVING_ENDPOINT + " by " + username + " for journal " + journal); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ensureCredentials(username);
		final Journal optJournal;
		//
		if (journal == null) {
			optJournal = this.journalService.createJournal(
					name, address, publisher, isbn, issn,
					openAccess, journalUrl, scimagoId, wosId);
		} else {
			optJournal = this.journalService.updateJournal(journal.intValue(),
					name, address, publisher, isbn, issn,
					openAccess, journalUrl, scimagoId, wosId);
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
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /deleteJournal by " + username + " for journal " + journal); //$NON-NLS-1$ //$NON-NLS-2$
		ensureCredentials(username);
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
	@PostMapping(value = "/" + Constants.SAVE_JOURNAL_RANKING_ENDPOINT)
	public void saveJournalRanking(
			@RequestParam(required = true) int journal,
			@RequestParam(required = true) int year,
			@RequestParam(required = false) Float impactFactor,
			@RequestParam(required = false) String scimagoQIndex,
			@RequestParam(required = false) String wosQIndex,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /" + Constants.SAVE_JOURNAL_RANKING_ENDPOINT + " by " + username + " for journal " + journal); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ensureCredentials(username);
		final Journal journalObj = this.journalService.getJournalById(journal);
		if (journalObj == null) {
			throw new IllegalArgumentException("Journal not found with: " + journal); //$NON-NLS-1$
		}
		final float realImpactFactor = impactFactor == null || impactFactor.floatValue() < 0f ? 0f : impactFactor.floatValue();
		final QuartileRanking scimago = Strings.isNullOrEmpty(scimagoQIndex) ? null : QuartileRanking.valueOf(scimagoQIndex);
		final QuartileRanking wos = Strings.isNullOrEmpty(wosQIndex) ? null : QuartileRanking.valueOf(wosQIndex);
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
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /" + Constants.DELETE_JOURNAL_RANKING_ENDPOINT + " by " + username + " for journal " + journal + " and year " + year); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ensureCredentials(username);
		final Journal journalObj = this.journalService.getJournalById(journal);
		if (journalObj == null) {
			throw new IllegalArgumentException("Journal not found with: " + journal); //$NON-NLS-1$
		}
		this.journalService.deleteQualityIndicators(journalObj, year);
	}

}
