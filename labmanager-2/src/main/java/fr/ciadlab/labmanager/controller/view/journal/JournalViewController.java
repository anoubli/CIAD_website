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

package fr.ciadlab.labmanager.controller.view.journal;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for journals views.
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
public class JournalViewController extends AbstractViewController {

	private JournalService journalService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param journalService the journal service.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public JournalViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JournalService journalService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.journalService = journalService;
	}

	/** Replies the model-view component for managing the journals.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/" + Constants.JOURNAL_LIST_ENDPOINT)
	public ModelAndView journalList(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, Constants.JOURNAL_LIST_ENDPOINT);
		final ModelAndView modelAndView = new ModelAndView(Constants.JOURNAL_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		initAdminTableButtons(modelAndView, endpoint(Constants.JOURNAL_EDITING_ENDPOINT, "journal")); //$NON-NLS-1$
		modelAndView.addObject("journals", this.journalService.getAllJournals()); //$NON-NLS-1$
		modelAndView.addObject("currentYear", Integer.valueOf(LocalDate.now().getYear())); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the editor for a journal. This editor permits to create or to edit a journal.
	 *
	 * @param journal the identifier of the journal to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of a journal.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/" + Constants.JOURNAL_EDITING_ENDPOINT)
	public ModelAndView journalEditor(
			@RequestParam(required = false) Integer journal,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, Constants.JOURNAL_EDITING_ENDPOINT);
		final ModelAndView modelAndView = new ModelAndView("journalEditor"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Journal journalObj;
		if (journal != null && journal.intValue() != 0) {
			journalObj = this.journalService.getJournalById(journal.intValue());
			if (journalObj == null) {
				throw new IllegalArgumentException("Journal not found: " + journal); //$NON-NLS-1$
			}
		} else {
			journalObj = null;
		}
		//
		modelAndView.addObject("journal", journalObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.JOURNAL_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.JOURNAL_LIST_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("scimagoQIndex_imageUrl", this.journalService.getScimagoURLByJournalId("{0}")); //$NON-NLS-1$ //$NON-NLS-2$
		//
		return modelAndView;
	}

	/** Show the editor for a journal ranking.
	 *
	 * @param id the identifier of the journal to edit.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/journalRankingEditor")
	public ModelAndView journalRankingEditor(
			@RequestParam(required = true) int id,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, "journalRankingEditor", Integer.valueOf(id)); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("journalRankingEditor"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Journal journal = this.journalService.getJournalById(id);
		if (journal == null) {
			throw new IllegalArgumentException("Journal not found: " + id); //$NON-NLS-1$
		}
		//
		modelAndView.addObject("savingUrl", endpoint(Constants.SAVE_JOURNAL_RANKING_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("deletionUrl", endpoint(Constants.DELETE_JOURNAL_RANKING_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("journal", journal); //$NON-NLS-1$
		final Map<Integer, JournalQualityAnnualIndicators> sortedMap = new TreeMap<>((a, b) -> {
			if (a == b) {
				return 0;
			}
			if (a == null) {
				return 1;
			}
			if (b == null) {
				return -1;
			}
			return Integer.compare(b.intValue(), a.intValue());
		});
		sortedMap.putAll(journal.getQualityIndicators());
		modelAndView.addObject("qualityIndicators", sortedMap); //$NON-NLS-1$
		//
		Integer year = null;
		QuartileRanking scimago = null;
		QuartileRanking wos = null;
		float impactFactor = 0f;
		final Iterator<JournalQualityAnnualIndicators> iterator = sortedMap.values().iterator();
		while (iterator.hasNext() && (scimago == null || wos == null || impactFactor <= 0f)) {
			final JournalQualityAnnualIndicators indicators = iterator.next();
			if (year == null) {
				year = Integer.valueOf(indicators.getReferenceYear());
			}
			if (scimago == null && indicators.getScimagoQIndex() != null) {
				scimago = indicators.getScimagoQIndex();
			}
			if (wos == null && indicators.getWosQIndex() != null) {
				wos = indicators.getWosQIndex();
			}
			if (impactFactor <= 0f && indicators.getImpactFactor() > 0f) {
				impactFactor = indicators.getImpactFactor();
			}
		}
		if (scimago == QuartileRanking.NR) {
			scimago = null;
		}
		if (wos == QuartileRanking.NR) {
			wos = null;
		}
		final int currentYear = LocalDate.now().getYear();
		modelAndView.addObject("currentYear", Integer.valueOf(currentYear)); //$NON-NLS-1$
		modelAndView.addObject("lastReferenceYear", year); //$NON-NLS-1$
		modelAndView.addObject("lastScimagoQIndex", scimago); //$NON-NLS-1$
		modelAndView.addObject("lastWosQIndex", wos); //$NON-NLS-1$
		modelAndView.addObject("lastImpactFactor", Float.valueOf(impactFactor)); //$NON-NLS-1$
		final Set<Integer> years = new TreeSet<>((a, b) -> {
			if (a == b) {
				return 0;
			}
			if (a == null) {
				return 1;
			}
			if (b == null) {
				return -1;
			}
			return Integer.compare(b.intValue(), a.intValue());
		});
		for (int y = currentYear - 20; y <= currentYear; ++y) {
			if (!sortedMap.containsKey(Integer.valueOf(y))) {
				years.add(Integer.valueOf(y));
			}
		}
		modelAndView.addObject("years", years); //$NON-NLS-1$
		//
		return modelAndView;
	}

	/** Show the updater for a journal rankings.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/journalRankingUpdater")
	public ModelAndView journalRankingUpdater(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, "journalRankingUpdater"); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("journalRankingUpdater"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		modelAndView.addObject("getJournalUpdateJsonUrl", endpoint(Constants.JOURNAL_INDICATOR_UPDATES_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("referenceYear", Integer.valueOf(LocalDate.now().getYear() - 1)); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", endpoint(Constants.SAVE_JOURNAL_INDICATOR_UPDATES_ENDPOINT)); //$NON-NLS-1$
		//
		return modelAndView;
	}

}
