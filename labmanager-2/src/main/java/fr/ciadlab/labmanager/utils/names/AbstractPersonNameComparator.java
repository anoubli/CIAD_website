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

package fr.ciadlab.labmanager.utils.names;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;

/** Abstract implementation of utilities for comparing person names.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public abstract class AbstractPersonNameComparator implements PersonNameComparator {

	private final PersonNameParser nameParser;

	private double similaritylevel;

	/** Constructor.
	 *
	 * @param nameParser the parser for persons' names.
	 */
	public AbstractPersonNameComparator(@Autowired PersonNameParser nameParser) {
		this.nameParser = nameParser;
	}

	@Override
	public double getSimilarityLevel() {
		return this.similaritylevel;
	}

	@Override
	public void setSimilarityLevel(double similarityLevel) {
		if (similarityLevel < 0.0) {
			this.similaritylevel = 0.0;
		} else if (similarityLevel > 1.0) {
			this.similaritylevel = 1.0;
		} else {
			this.similaritylevel = similarityLevel;
		}
	}

	@Override
	public double getSimilarity(String fullName1, String fullName2) {
		final String first1 = this.nameParser.parseFirstName(fullName1);
		final String last1 = this.nameParser.parseLastName(fullName1);
		final String first2 = this.nameParser.parseFirstName(fullName2);
		final String last2 = this.nameParser.parseLastName(fullName2);
		return getSimilarity(first1, last1, first2, last2);
	}

	@Override
	public double getSimilarity(String firstName1, String lastName1, String firstName2, String lastName2) {
		boolean enableShortNames = this.nameParser.isShortName(firstName1)
				|| this.nameParser.isShortName(lastName1)
				|| this.nameParser.isShortName(firstName2)
				|| this.nameParser.isShortName(lastName2);
		//
		final String first1 = this.nameParser.normalizeName(firstName1);
		final Set<String> firsts1 = this.nameParser.getNormalizedNamesFor(firstName1, enableShortNames);
		final String last1 = this.nameParser.normalizeName(lastName1);
		final Set<String> lasts1 = this.nameParser.getNormalizedNamesFor(lastName1, enableShortNames);
		//
		final String first2 = this.nameParser.normalizeName(firstName2);
		final Set<String> firsts2 = this.nameParser.getNormalizedNamesFor(firstName2, enableShortNames);
		final String last2 = this.nameParser.normalizeName(lastName2);
		final Set<String> lasts2 = this.nameParser.getNormalizedNamesFor(lastName2, enableShortNames);
		//
		return getSimilarity(
				first1, firsts1,
				last1, lasts1,
				first2, firsts2,
				last2, lasts2);
	}

	/** Create an instance of a string similarity computer.
	 * This is a factory method.
	 *
	 * @return the string similarity computer.
	 */
	protected abstract NormalizedStringSimilarity createStringSimilarityComputer();

	/** Replies the similarity of the two names.
	 * This function test if the two first names and last names are similar, or
	 * if the first first name is similar to the second last name, and the
	 * first last name is similar to the second first name.
	 *
	 * @param firstName1 the first name.
	 * @param firstNames1 the set of first names for the first name.
	 * @param lastName1 the last names for the first name.
	 * @param lastNames1 the set of last names for the last name.
	 * @param firstName2 the first names for the second name.
	 * @param firstNames2 the set of first names for the second name.
	 * @param lastName2 the last names for the second name.
	 * @param lastNames2 the set of last names for the second name.
	 * @return the level of similarity. {@code 0} means that the names are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	protected double getSimilarity(
			String firstName1, Set<String> firstNames1,
			String lastName1, Set<String> lastNames1,
			String firstName2, Set<String> firstNames2,
			String lastName2, Set<String> lastNames2) {
		final NormalizedStringSimilarity similarityComputer = createStringSimilarityComputer();
		
		// FL vs. FL
		double max = getSimilarity(similarityComputer, firstNames1, lastName1, firstNames2, lastName2);
		if (max >= 1.0) {
			return max;
		}
		
		// FL vs. LF
		double s = getSimilarity(similarityComputer, firstNames1, lastName1, lastNames2, firstName2);
		if (s >= 1.0) {
			return s;
		}
		if (s > max) {
			max = s;
		}

		// LF vs. LF
		s = getSimilarity(similarityComputer, lastNames1, firstName1, lastNames2, firstName2);
		if (s >= 1.0) {
			return s;
		}
		if (s > max) {
			max = s;
		}

		// LF vs. FL
		s = getSimilarity(similarityComputer, lastNames1, firstName1, firstNames2, lastName2);
		if (s >= 1.0) {
			return s;
		}
		if (s > max) {
			max = s;
		}

		return max;
	}

	protected double getSimilarity(NormalizedStringSimilarity similarityComputer, Set<String> first1, String last1, Set<String> first2, String last2) {
		final double s0 = getSimilarity(similarityComputer, first1, first2);
		final double s1 = getSimilarity(similarityComputer, last1, last2);
		return (s0 + s1) / 2.0;
	}

	/** Replies if the two sets of names are similar.
	 *
	 * @param matcher the string similarity computer to be used.
	 * @param name1 the first name to compare.
	 * @param name2 the second name to compare.
	 * @return the level of similarity. {@code 0} means that the names are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	@SuppressWarnings("static-method")
	protected double getSimilarity(NormalizedStringSimilarity matcher, Set<String> name1, Set<String> name2) {
		if (name1.isEmpty() || name2.isEmpty()) {
			return 1.0;
		}
		final Set<String> ens1;
		final Set<String> ens2;
		if (name1.size() <= name2.size()) {
			ens1 = name1;
			ens2 = new TreeSet<>(name2);
		} else {
			ens1 = name2;
			ens2 = new TreeSet<>(name1);
		}
		double mmax = 0.0;
		for (final String n1 : ens1) {
			final Iterator<String> iter2 = ens2.iterator();
			String candidate = null;
			double max = 0.0;
			while (iter2.hasNext()) {
				final String n2 = iter2.next();
				final double similarity = matcher.similarity(n1, n2);
				if (similarity > max) {
					max = similarity;
					candidate = n2;
				}
			}
			if (candidate != null) {
				ens2.remove(candidate);
				if (max > mmax) {
					mmax = max;
				}
			}
		}
		return mmax;
	}

	/** Replies the similarity of the two names.
	 *
	 * @param matcher the string similarity computer to be used.
	 * @param name1 the first name to compare.
	 * @param name2 the second name to compare.
	 * @return the level of similarity. {@code 0} means that the names are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	@SuppressWarnings("static-method")
	protected double getSimilarity(NormalizedStringSimilarity matcher, String name1, String name2) {
		if (Strings.isNullOrEmpty(name1) || Strings.isNullOrEmpty(name2)) {
			return 1.0;
		}
		return matcher.similarity(name1, name2);
	}

}