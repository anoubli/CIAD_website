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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.apache.jena.ext.com.google.common.base.Strings;

/** Utilities for text.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public final class TextUtils {

	private final static Set<Character> APOSTROPHE = new TreeSet<>(Arrays.asList(
			Character.valueOf('a'), Character.valueOf('e'), Character.valueOf('i'),
			Character.valueOf('o'), Character.valueOf('u'), Character.valueOf('y'),
			Character.valueOf('h')));
	
	private TextUtils() {
		//
	}

	/** Replies if the given string should be prefixed by a string with the {@code '}
	 * character is some language.
	 *
	 * @param text the text to check.
	 * @return {@code true} if the given string should be prefixed by the {@code '} character.
	 */
	public static boolean isApostrophable(String text) {
		if (Strings.isNullOrEmpty(text)) {
			return false;
		}
		final char c = Character.toLowerCase(text.charAt(0));
		return APOSTROPHE.contains(Character.valueOf(c));
	}

	/** Replies the preferred string representation of the given number.
	 *
	 * @param number the number to format.
	 * @param unit the unit to user for formatting.
	 * @param locale the locale to use.
	 * @return the string representation of the number.
	 */
	public static String formatNumber(Number number, Unit unit, Locale locale) {
		if (number != null) {
			final Number cvalue = unit == null ? number : unit.convert(number);
			final DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale == null ? Locale.US : locale);
			final DecimalFormat format = new DecimalFormat("#0.#", symbols); //$NON-NLS-1$
			return format.format(cvalue);
		}
		return ""; //$NON-NLS-1$
	}

}
