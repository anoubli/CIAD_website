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

package fr.ciadlab.labmanager.controller.api.indicator;

import java.util.List;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.service.indicator.GlobalIndicatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for manipulating general indicators.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@RestController
@CrossOrigin
public class GlobalIndicatorApiController extends AbstractApiController {

	private GlobalIndicatorsService indicatorService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param indicatorService the service for accessing the global indicators.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public GlobalIndicatorApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired GlobalIndicatorsService indicatorService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.indicatorService = indicatorService;
	}

	/** Save or create the global indicators.
	 *
	 * @param visibleIndicators the list of th keys of the visible indicators.
	 * @param username the name of the logged-in user.
	 */
	@PutMapping("/" + Constants.GLOBAL_INDICATORS_SAVING_ENDPOINT)
	public void saveGlobalIndicators(
			@RequestParam(required = false) List<String> visibleIndicators,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, Constants.GLOBAL_INDICATORS_SAVING_ENDPOINT);
		this.indicatorService.updateVisibleIndicators(visibleIndicators);
	}

	/** Reset the values of the global indicators to force there computation.
	 *
	 * @param username the name of the logged-in user.
	 */
	@PutMapping("/" + Constants.GLOBAL_INDICATOR_VALUES_RESET_ENDPOINT)
	public void resetGlobalIndicatorValues(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, Constants.GLOBAL_INDICATOR_VALUES_RESET_ENDPOINT);
		this.indicatorService.resetGlobalIndicatorValues();
	}

}
