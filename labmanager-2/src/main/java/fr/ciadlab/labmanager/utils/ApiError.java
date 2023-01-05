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

package fr.ciadlab.labmanager.utils;

import java.io.Serializable;

/** API error.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2.0
 */
public class ApiError implements Serializable {

	private static final long serialVersionUID = 3392972706564054457L;

	private final int code;

	private final String reason;

	/** Constructor.
	 *
	 * @param code the HTTP code of the error.
	 * @param reason the reseaon.
	 */
	public ApiError(int code, String reason) {
		this.code = code;
		this.reason = reason;
	}

	/** Replies the error code.
	 *
	 * @return the code.
	 */
	public int getCode() {
		return this.code;
	}

	/** Replies the reason of the error.
	 *
	 * @return the error message.
	 */
	public String getReason() {
		return this.reason;
	}

}
