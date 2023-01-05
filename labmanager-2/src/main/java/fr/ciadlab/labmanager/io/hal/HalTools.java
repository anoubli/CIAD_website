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

package fr.ciadlab.labmanager.io.hal;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.jena.ext.com.google.common.base.Strings;

/** Utilities for HAL identifiers.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface HalTools {

	/** Replies the HAL number that is specified inside the given HAL URL.
	 * A HAL URL is based on the standard {@link "https://hal.archives-ouvertes.fr"} to which
	 * the HAL number is added.
	 *
	 * @param url the HAL URL.
	 * @return the HAL number.
	 */
	String getHALNumberFromHALUrl(URL url);

	/** Replies the HAL number that is specified inside the given HAL URL.
	 * A HAL URL is based on the standard {@link "https://hal.archives-ouvertes.fr"} to which
	 * the HAL number is added.
	 *
	 * @param url the HAL URL.
	 * @return the HAL number.
	 * @throws MalformedURLException if the given parameter is a malformed URL.
	 */
	default String getHALNumberFromHALUrl(String url) throws MalformedURLException {
		if (!Strings.isNullOrEmpty(url)) {
			return getHALNumberFromHALUrl(new URL(url));
		}
		return null;
	}

	/** Replies the HAL URL from the given HAL number.
	 * A HAL URL is based on the standard {@link "https://hal.archives-ouvertes.fr"} to which
	 * the HAL number is added.
	 *
	 * @param number the HAL number.
	 * @return the HAL URL.
	 */
	URL getHALUrlFromHALNumber(String number);

}
