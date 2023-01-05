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

package fr.ciadlab.labmanager.indicators;

import java.time.LocalDate;

import fr.ciadlab.labmanager.configuration.Constants;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation of an indicator for the "current time".
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.3
 */
public abstract class AbstractInstantIndicator extends AbstractIndicator {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public AbstractInstantIndicator(MessageSourceAccessor messages, Constants constants) {
		super(messages, constants);
	}

	@Override
	public LocalDate getReferencePeriodStart() {
		return LocalDate.now();
	}

	@Override
	public LocalDate getReferencePeriodEnd() {
		return LocalDate.now();
	}

}
