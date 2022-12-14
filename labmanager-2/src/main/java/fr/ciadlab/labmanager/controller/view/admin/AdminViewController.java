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

package fr.ciadlab.labmanager.controller.view.admin;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** This general controller shows up the administration tools and the list of all the controllers in the backend.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@RestController
@CrossOrigin
public class AdminViewController extends AbstractViewController {

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the live constants.
	 * @param organizationService the research organization service.
	 */
	public AdminViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationService organizationService) {
		super(messages, constants);
		this.organizationService = organizationService;
	}

	/** Shows up the main administration page.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view of the list of publications.
	 */
	@GetMapping(value = "/admin")
	public ModelAndView admin(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /admin by " + username); //$NON-NLS-1$
		readCredentials(username);
		final ModelAndView modelAndView = new ModelAndView("admin"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView);
		final List<ResearchOrganization> list = this.organizationService.getAllResearchOrganizations().stream()
				.sorted(EntityUtils.getPreferredResearchOrganizationComparator()).collect(Collectors.toList());
		modelAndView.addObject("organizations", list); //$NON-NLS-1$
		modelAndView.addObject("username", Strings.nullToEmpty(this.username)); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the view for merging the database JSON and a given BibTeX for generating a new JSON file to be download.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 * @throws IOException if there is some internal IO error when building the form's data.
	 */
	@GetMapping(value = "/mergeDatabaseBibTeXToJson")
	public ModelAndView mergeDatabaseBibTeXToJson(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws IOException {
		getLogger().info("Opening /mergeDatabaseBibTeXToJson by " + username); //$NON-NLS-1$
		ensureCredentials(username);
		final ModelAndView modelAndView = new ModelAndView("mergeDatabaseBibTeXToJson"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView);
		//
		modelAndView.addObject("formActionUrl", rooted(Constants.GET_JSON_FROM_DATABASE_AND_BIBTEX_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("URLS_edit", rooted(Constants.PUBLICATION_EDITING_ENDPOINT) + "?" //$NON-NLS-1$ //$NON-NLS-2$
				+ Constants.PUBLICATION_ENDPOINT_PARAMETER + "="); //$NON-NLS-1$
		//
		return modelAndView;
	}

}
