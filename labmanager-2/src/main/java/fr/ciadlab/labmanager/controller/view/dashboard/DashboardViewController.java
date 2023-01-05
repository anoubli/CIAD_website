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

package fr.ciadlab.labmanager.controller.view.dashboard;

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
import fr.ciadlab.labmanager.service.conference.ConferenceService;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DashboardViewController extends AbstractViewController {


	ConferenceService conferenceService;
	JournalService journalService;
	MembershipService membershipService;
	ResearchOrganizationService researchOrganizationService;
	PublicationService publicationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param journalService the journal service.
	 */
	public DashboardViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JournalService journalService,
			@Autowired ConferenceService conferenceService,
			@Autowired MembershipService membershipService,
			@Autowired ResearchOrganizationService researchOrganizationService,
			@Autowired PublicationService publicationService) {
		super(messages, constants);
		this.conferenceService = conferenceService;
		this.journalService = journalService;
		this.membershipService = membershipService;
		this.researchOrganizationService = researchOrganizationService;
		this.publicationService = publicationService;
	}

	/** Replies the model-view component for managing the journals.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/dashboard")
	public ModelAndView showDashboard(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /" + "dashboard" + " by " + username); //$NON-NLS-1$ //$NON-NLS-2$
		readCredentials(username);
		final ModelAndView modelAndView = new ModelAndView("dashboard");
		initModelViewWithInternalProperties(modelAndView);
		modelAndView.addObject("organizationStat", this.publicationService.getPublicationPerOrganisation());
		modelAndView.addObject("memberPerOrganization", this.researchOrganizationService.getMembershipPerOrganization());
		modelAndView.addObject("projectPerOrganization", this.researchOrganizationService.getProjectByOrganization());
		modelAndView.addObject("organizations", this.journalService.getAllJournals());
		modelAndView.addObject("publications", this.publicationService.getNumberOfPublicationPerYear(this.publicationService.getAllPublications()));
		//initModelViewWithInternalProperties(modelAndView);
		return modelAndView;
	}


}
