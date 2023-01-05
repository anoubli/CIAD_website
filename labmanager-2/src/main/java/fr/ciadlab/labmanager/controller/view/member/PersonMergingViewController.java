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

package fr.ciadlab.labmanager.controller.view.member;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for merging persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RestController
@CrossOrigin
public class PersonMergingViewController extends AbstractViewController {

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public PersonMergingViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
	}

	/** Show the view that permits to analyze duplicate persons and merge them.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view that shows the duplicate persons.
	 */
	@GetMapping("/personDuplicateList")
	public ModelAndView personDuplicateList(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, "personDuplicateList"); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("personDuplicateList"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		modelAndView.addObject("batchUrl", endpoint(Constants.COMPUTE_PERSON_DUPLICATE_NAMES_ENDPOINT)); //$NON-NLS-1$
		//
		return modelAndView;
	}

}
