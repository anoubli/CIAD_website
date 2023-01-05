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

package fr.ciadlab.labmanager.controller.view.indicator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.indicator.GlobalIndicators;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.indicators.Indicator;
import fr.ciadlab.labmanager.service.indicator.GlobalIndicatorsService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for viewing general indicators.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@RestController
@CrossOrigin
public class GlobalIndicatorViewController extends AbstractViewController {

	private String defaultOrganizationName;
	
	private ResearchOrganizationService organizationService;

	private GlobalIndicatorsService indicatorService;

	private Locale currentLocale;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param indicatorService the service for accessing the global indicators.
	 * @param organizationService the service related to the research organizations.
	 * @param defaultLocale the default locale.
	 * @param defaultOrganizationName name of the default organization.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public GlobalIndicatorViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired GlobalIndicatorsService indicatorService,
			@Autowired Locale defaultLocale,
			@Value("${labmanager.default-organization}") String defaultOrganizationName,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.defaultOrganizationName = defaultOrganizationName;
		this.organizationService = organizationService;
		this.indicatorService = indicatorService;
		this.currentLocale = defaultLocale;
	}

	/** Show the component with the global indicators.
	 *
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 * @param username the name of the logged-in user.
	 * @return the model-view.
	 */
	@GetMapping("/" + Constants.GLOBAL_INDICATORS_EDITING_ENDPOINT)
	public ModelAndView globalIndicatorsEditor(
			@RequestParam(required = false, defaultValue = "false") boolean embedded,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, Constants.GLOBAL_INDICATORS_EDITING_ENDPOINT);
		final ModelAndView modelAndView = new ModelAndView(Constants.GLOBAL_INDICATORS_EDITING_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, embedded);
		//
		final GlobalIndicators gi = this.indicatorService.getGlobalIndicatorsNeverNull(false);
		modelAndView.addObject("globalIndicators", gi); //$NON-NLS-1$
		final List<? extends Indicator> visibleIndicators = this.indicatorService.getVisibleIndicators();
		modelAndView.addObject("visibleIndicators", visibleIndicators); //$NON-NLS-1$
		final List<? extends Indicator> invisibleIndicators = this.indicatorService.getInvisibleIndicators();
		modelAndView.addObject("invisibleIndicators", invisibleIndicators); //$NON-NLS-1$
		//
		final Optional<ResearchOrganization> organizationOpt = this.organizationService.getResearchOrganizationByAcronymOrName(this.defaultOrganizationName);
		if (organizationOpt.isEmpty()) {
			throw new IllegalArgumentException("Organization not found with name: " + this.defaultOrganizationName); //$NON-NLS-1$
		}
		modelAndView.addObject("organization", organizationOpt.get()); //$NON-NLS-1$
		//
		modelAndView.addObject("formActionUrl", rooted(Constants.GLOBAL_INDICATORS_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.ADMIN_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("resetIndicatorUrl", endpoint(Constants.GLOBAL_INDICATOR_VALUES_RESET_ENDPOINT)); //$NON-NLS-1$
		return modelAndView;
	}

	/** Replies the model-view component for listing the global indicators.
	 *
	 * @param dbId the database identifier of the organization.
	 * @param name the name of acronym of the organization.
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 * @param username the name of the logged-in user.
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/showGlobalIndicators")
	public ModelAndView showGlobalIndicators(
			@RequestParam(required = false) Integer dbId,
			@RequestParam(required = false) String name,
			@RequestParam(required = false, defaultValue = "false") boolean embedded,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, "/showGlobalIndicators"); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("showGlobalIndicators"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, embedded);
		//
		modelAndView.addObject("backendLocale", this.currentLocale); //$NON-NLS-1$
		//
		final ResearchOrganization organization;
		if (dbId != null && dbId.intValue() != 0) {
			final Optional<ResearchOrganization> organizationOpt = this.organizationService.getResearchOrganizationById(dbId.intValue());
			if (organizationOpt.isEmpty()) {
				throw new IllegalArgumentException("Organization not found with id: " + dbId); //$NON-NLS-1$
			}
			organization = organizationOpt.get();
		} else {
			final String inOrganizationAcronym = inString(name);
			if (!Strings.isNullOrEmpty(inOrganizationAcronym)) {
				final Optional<ResearchOrganization> organizationOpt = this.organizationService.getResearchOrganizationByAcronymOrName(name);
				if (organizationOpt.isEmpty()) {
					throw new IllegalArgumentException("Organization not found with name: " + name); //$NON-NLS-1$
				}
				organization = organizationOpt.get();
			} else {
				throw new IllegalArgumentException("Organization not found"); //$NON-NLS-1$
			}
		}
		assert organization != null;
		modelAndView.addObject("organization", organization); //$NON-NLS-1$
		//
		modelAndView.addObject("globalIndicators", this.indicatorService.getGlobalIndicatorsNeverNull(false)); //$NON-NLS-1$
		final List<? extends Indicator> visibleIndicators = this.indicatorService.getVisibleIndicators();
		if (visibleIndicators == null || visibleIndicators.isEmpty()) {
			modelAndView.addObject("indicators", Collections.emptyList()); //$NON-NLS-1$
		} else {
			final Map<String, Number> values = this.indicatorService.getAllIndicatorValues(organization);
			final List<Pair<Indicator, Number>> synthesis = new LinkedList<>();
			for (final Indicator indicator : visibleIndicators) {
				final Number number = values.get(indicator.getKey());
				if (number != null) {
					synthesis.add(Pair.of(indicator, number));
				}
			}
			modelAndView.addObject("indicators", synthesis); //$NON-NLS-1$
		}
		//
		return modelAndView;
	}

}
