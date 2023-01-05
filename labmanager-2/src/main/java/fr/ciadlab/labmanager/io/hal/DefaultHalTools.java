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

import com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Default implementation for the utilities for HAL numbers.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
@Primary
public class DefaultHalTools implements HalTools {

	private static final URL HAL_BASE;

	static {
		try {
			HAL_BASE = new URL("https://hal.archives-ouvertes.fr"); //$NON-NLS-1$
		} catch (MalformedURLException ex) {
			throw new Error(ex);
		}
	}

	@Override
	public String getHALNumberFromHALUrl(URL url) {
		if (url != null) {
			final String path = url.getPath();
			if (!Strings.isNullOrEmpty(path)) {
				if (path.startsWith("/")) { //$NON-NLS-1$
					return path.substring(1);
				}
				return path;
			}
		}
		return null;
	}

	@Override
	public URL getHALUrlFromHALNumber(String number) {
		if (!Strings.isNullOrEmpty(number)) {
			return FileSystem.join(HAL_BASE, number);
		}
		return null;
	}

}
