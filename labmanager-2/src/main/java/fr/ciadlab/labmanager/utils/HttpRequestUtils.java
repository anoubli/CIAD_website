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

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** Utilities for HTTP request.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public class HttpRequestUtils {

	private static final String[] IP_HEADER_CANDIDATES = {
			"X-Forwarded-For", //$NON-NLS-1$
			"Proxy-Client-IP", //$NON-NLS-1$
			"WL-Proxy-Client-IP", //$NON-NLS-1$
			"HTTP_X_FORWARDED_FOR", //$NON-NLS-1$
			"HTTP_X_FORWARDED", //$NON-NLS-1$
			"HTTP_X_CLUSTER_CLIENT_IP", //$NON-NLS-1$
			"HTTP_CLIENT_IP", //$NON-NLS-1$
			"HTTP_FORWARDED_FOR", //$NON-NLS-1$
			"HTTP_FORWARDED", //$NON-NLS-1$
			"HTTP_VIA", //$NON-NLS-1$
			"REMOTE_ADDR" //$NON-NLS-1$
	};

	private HttpRequestUtils() {
		//
	}

	/** Replie the Client IP of a Servlet request is ongoing.
	 *
	 * @return the client address.
	 */
	public static String getClientIpAddressIfServletRequestExist() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return "0.0.0.0"; //$NON-NLS-1$
		}
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		final JsonObjectBuilder bb = Json.createObjectBuilder();
		for (String header: IP_HEADER_CANDIDATES) {
			final String ipList = request.getHeader(header);
			if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) { //$NON-NLS-1$
				bb.add(header, ipList);
			}
		}
		bb.add("_REMOTE_ADDR", request.getRemoteAddr()); //$NON-NLS-1$
		return bb.build().toString();
	}
}