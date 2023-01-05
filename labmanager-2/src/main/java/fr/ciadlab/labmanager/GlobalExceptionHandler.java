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

package fr.ciadlab.labmanager;

import fr.ciadlab.labmanager.utils.ApiError;
import fr.ciadlab.labmanager.utils.MaintenanceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Global handlers of exceptions
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Indicates that the application is on maintenance.
	 *
	 * @return the description of the error.
	 */
	@SuppressWarnings("static-method")
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler(MaintenanceException.class)
	public ApiError maintenance() {
		return new ApiError(HttpStatus.SERVICE_UNAVAILABLE.value(), "Service currently in maintenance"); //$NON-NLS-1$
	}

}
