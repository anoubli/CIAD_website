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

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Strings;
import org.arakhne.afc.util.CountryCode;
import org.springframework.context.i18n.LocaleContextHolder;

/** Utilities for country codes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public final class CountryCodeUtils {

	/** Default country.
	 */
	public static final CountryCode DEFAULT = CountryCode.FRANCE;

	private CountryCodeUtils() {
		//
	}

	/** Replies the country code that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the code, to search for.
	 * @return the code.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type.
	 */
	public static CountryCode valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final CountryCode code : CountryCode.values()) {
				if (name.equalsIgnoreCase(code.name())) {
					return code;
				}
			}
		}
		throw new IllegalArgumentException("Invalid country code: " + name); //$NON-NLS-1$
	}

	/** Replies the display name of the country.
	 * The target language is the one provided by {@link LocaleContextHolder}.
	 *
	 * @param code the country code to use.
	 * @return the display name of the country.
	 */
	public static String getDisplayCountry(CountryCode code) {
		final Locale targetLanguage = LocaleContextHolder.getLocale();
		final Locale countryLocale = new Locale(code.getCode(), code.getCode());
		if (targetLanguage == null) {
			countryLocale.getDisplayCountry();
		}
		return countryLocale.getDisplayCountry(targetLanguage);
	}

	/** Replies all the display names of the countries.
	 * The target language is the one provided by {@link LocaleContextHolder}.
	 *
	 * @return the display names of the countries.
	 */
	public static Map<CountryCode, String> getAllDisplayCountries() {
		final Map<CountryCode, String> labels = new TreeMap<>();
		for (final CountryCode code : CountryCode.values()) {
			labels.put(code, getDisplayCountry(code));
		}
		return labels;
	}

	/** Replies if the given code corresponds to France and one of its associated territories.
	 *
	 * @param code the code to test.
	 * @return {@code true} if the code corresponds to a French territory.
	 */
	public static boolean isFrance(CountryCode code) {
		if (code != null) {
			return code == CountryCode.FRANCE || code == CountryCode.FRENCH_GUIANA
					|| code == CountryCode.FRENCH_POLYNESIA || code == CountryCode.FRENCH_SOUTHERN_TERRITORIES;
		}
		return false;
	}

}
