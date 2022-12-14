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

package fr.ciadlab.labmanager.controller.view.organization;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for research organizations' views.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ResearchOrganizationService
 * @since 2.0.0
 */
@RestController
@CrossOrigin
public class ResearchOrganizationViewController extends AbstractViewController {

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the live constants.
	 * @param organizationService the research organization service.
	 */
	public ResearchOrganizationViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationService organizationService) {
		super(messages, constants);
		this.organizationService = organizationService;
	}

	/** Replies the model-view component for listing the research organizations. It is the main endpoint for this controller.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/" + Constants.ORGANIZATION_LIST_ENDPOINT)
	public ModelAndView showOrganizationList(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /" + Constants.ORGANIZATION_LIST_ENDPOINT + " by " + username); //$NON-NLS-1$ //$NON-NLS-2$
		readCredentials(username);
		final ModelAndView modelAndView = new ModelAndView(Constants.ORGANIZATION_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView);
		initAdminTableButtons(modelAndView, endpoint(Constants.ORGANIZATION_EDITING_ENDPOINT, "organization")); //$NON-NLS-1$
		modelAndView.addObject("organizations", this.organizationService.getAllResearchOrganizations()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the editor for an organization. This editor permits to create or to edit an organization.
	 *
	 * @param organization the identifier of the organization to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of an organization.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/" + Constants.ORGANIZATION_EDITING_ENDPOINT)
	public ModelAndView showOrganizationEditor(
			@RequestParam(required = false) Integer organization,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /" + Constants.ORGANIZATION_EDITING_ENDPOINT + " by " + username + " for organization " + organization); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ensureCredentials(username);
		final ModelAndView modelAndView = new ModelAndView("organizationEditor"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView);
		//
		final ResearchOrganization organizationObj;
		if (organization != null && organization.intValue() != 0) {
			Optional<ResearchOrganization> opt = this.organizationService.getResearchOrganizationById(organization.intValue());
			if (opt.isPresent()) {
				organizationObj = opt.get();
			} else {
				throw new IllegalArgumentException("Organization not found: " + organization); //$NON-NLS-1$
			}
		} else {
			organizationObj = null;
		}
		//
		List<ResearchOrganization> otherOrganizations = this.organizationService.getAllResearchOrganizations();
		if (organizationObj != null) {
			otherOrganizations = otherOrganizations.stream().filter(it -> it.getId() != organizationObj.getId()).collect(Collectors.toList());
		}
		//
		modelAndView.addObject("organization", organizationObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.ORGANIZATION_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.ORGANIZATION_LIST_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("defaultOrganizationType", ResearchOrganizationType.DEFAULT); //$NON-NLS-1$
		modelAndView.addObject("countryLabels", CountryCodeUtils.getAllDisplayCountries()); //$NON-NLS-1$
		modelAndView.addObject("defaultCountry", CountryCodeUtils.DEFAULT); //$NON-NLS-1$
		modelAndView.addObject("otherOrganizations", otherOrganizations); //$NON-NLS-1$
		return modelAndView;
	}

}
