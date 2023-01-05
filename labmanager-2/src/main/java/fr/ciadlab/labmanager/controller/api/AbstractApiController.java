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

package fr.ciadlab.labmanager.controller.api;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.AbstractCredentialController;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation of a JEE Controller that provides regular API.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractApiController extends AbstractCredentialController {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public AbstractApiController(MessageSourceAccessor messages, Constants constants, String usernameKey) {
		super(messages, constants, usernameKey);
	}

}
