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

package fr.ciadlab.labmanager.entities.publication;

import fr.ciadlab.labmanager.entities.EntityUtils;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;

/** Abstract comparator of publications based on a normalized similarity.
 * For comparison, the order of the publication is based on the
 * type of publication, the year, the authors, the identifier.
 * For similarity, an algorithm is used that is defined in sub-classes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPublicationComparator implements PublicationComparator {

	private NormalizedStringSimilarity similarityComputer;

	private double similaritylevel;

	/** Constructor.
	 *
	 * @param defaultSimilarity the default similarity.
	 */
	public AbstractPublicationComparator(double defaultSimilarity) {
		this.similaritylevel = defaultSimilarity;
	}

	/** Replies the internal similarity computer.
	 *
	 * @return the internal similarity computer.
	 */
	protected NormalizedStringSimilarity getSimilarityComputer() {
		if (this.similarityComputer == null) {
			this.similarityComputer = createSimilarityComputer();
		}
		return this.similarityComputer;
	}
	
	/** Create an instance of the internal similarity computer.
	 *
	 * @return the internal similarity computer.
	 */
	protected abstract NormalizedStringSimilarity createSimilarityComputer();

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
	public int compare(Publication o1, Publication o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = o1.getType().compareTo(o2.getType());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Integer.compare(o1.getPublicationYear(), o2.getPublicationYear());
		if (cmp != 0) {
			return cmp;
		}
		cmp = EntityUtils.getPreferredPersonListComparator().compare(o1.getAuthors(), o2.getAuthors());
		if (cmp != 0) {
			return cmp;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

	private double similarity(String s1, String s2) {
		if (s1 == s2) {
			return 1.0;
		}
		if (s1 == null || s2 == null) {
			return 0.0;
		}
		return getSimilarityComputer().similarity(s1, s2);
	}

	private static String getPublicationDescription(Publication publication) {
		final StringBuilder buf = new StringBuilder();
		buf.append(publication.getPublicationYear());
		buf.append(',');
		if (publication.getType() != null) {
			buf.append(publication.getType().ordinal());
		} else {
			buf.append('0');
		}
		buf.append(',');
		buf.append(publication.getTitle());
		buf.append(',');
		buf.append(publication.getWherePublishedShortDescription());
		buf.append(',');
		if (publication.getDOI() != null) {
			buf.append(publication.getDOI());
		}
		buf.append(',');
		if (publication.getISBN() != null) {
			buf.append(publication.getISBN());
		}
		buf.append(',');
		if (publication.getISSN() != null) {
			buf.append(publication.getISSN());
		}
		return buf.toString();
	}

	@Override
	public double getSimilarity(Publication publication1, Publication publication2) {
		if (publication1 == publication2) {
			return 1.0;
		}
		if (publication1 == null || publication2 == null) {
			return 0.0;
		}
		final String desc1 = getPublicationDescription(publication1);
		final String desc2 = getPublicationDescription(publication2);
		return similarity(desc1, desc2);
	}

}
