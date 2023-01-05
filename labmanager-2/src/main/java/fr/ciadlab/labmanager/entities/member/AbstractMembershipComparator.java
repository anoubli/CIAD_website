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

package fr.ciadlab.labmanager.entities.member;

import java.time.LocalDate;
import java.util.Comparator;

import fr.ciadlab.labmanager.utils.bap.FrenchBap;
import fr.ciadlab.labmanager.utils.cnu.CnuSection;
import fr.ciadlab.labmanager.utils.conrs.ConrsSection;

/** Abstract comparator of memberships.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractMembershipComparator implements Comparator<Membership> {

	/** Null-safe comparison the two dates.
	 * 
	 * @param s0 the first value.
	 * @param s1 the second value.
	 * @return the result of the comparison.
	 */
	protected static int compareDate(LocalDate d0, LocalDate d1) {
		if (d0 == d1) {
			return 0;
		}
		if (d0 == null) {
			return Integer.MIN_VALUE;
		}
		if (d1 == null) {
			return Integer.MAX_VALUE;
		}
		return d1.compareTo(d0);
	}

	/** Null-safe comparison the two dates as they are start dates of a membership.
	 * 
	 * @param s0 the first value.
	 * @param s1 the second value.
	 * @return the result of the comparison.
	 */
	protected static int compareStartDate(LocalDate d0, LocalDate d1) {
		if (d0 == d1) {
			return 0;
		}
		if (d0 == null) {
			return Integer.MIN_VALUE;
		}
		if (d1 == null) {
			return Integer.MAX_VALUE;
		}
		return d1.compareTo(d0);
	}

	/** Null-safe comparison the two dates as they are end dates of a membership.
	 * 
	 * @param s0 the first value.
	 * @param s1 the second value.
	 * @return the result of the comparison.
	 */
	protected static int compareEndDate(LocalDate d0, LocalDate d1) {
		if (d0 == d1) {
			return 0;
		}
		if (d0 == null) {
			return Integer.MAX_VALUE;
		}
		if (d1 == null) {
			return Integer.MIN_VALUE;
		}
		return d1.compareTo(d0);
	}

	/** Null-safe comparison the two CNU sections.
	 * 
	 * @param s0 the first value.
	 * @param s1 the second value.
	 * @return the result of the comparison.
	 */
	protected static int compareCnuSection(CnuSection s0, CnuSection s1) {
		if (s0 == s1) {
			return 0;
		}
		if (s0 == null) {
			return Integer.MIN_VALUE;
		}
		if (s1 == null) {
			return Integer.MAX_VALUE;
		}
		return s0.compareTo(s1);
	}

	/** Null-safe comparison the two CoNRS sections.
	 * 
	 * @param s0 the first value.
	 * @param s1 the second value.
	 * @return the result of the comparison.
	 */
	protected static int compareConrsSection(ConrsSection s0, ConrsSection s1) {
		if (s0 == s1) {
			return 0;
		}
		if (s0 == null) {
			return Integer.MIN_VALUE;
		}
		if (s1 == null) {
			return Integer.MAX_VALUE;
		}
		return s0.compareTo(s1);
	}

	/** Null-safe comparison the two French professional categories.
	 * 
	 * @param s0 the first value.
	 * @param s1 the second value.
	 * @return the result of the comparison.
	 */
	protected static int compareFrenchBap(FrenchBap b0, FrenchBap b1) {
		if (b0 == b1) {
			return 0;
		}
		if (b0 == null) {
			return Integer.MIN_VALUE;
		}
		if (b1 == null) {
			return Integer.MAX_VALUE;
		}
		return b0.compareTo(b1);
	}

	/** Null-safe comparison the two responsabilities.
	 * 
	 * @param s0 the first value.
	 * @param s1 the second value.
	 * @return the result of the comparison.
	 */
	protected static int compareResponsabilities(Responsibility r0, Responsibility r1) {
		if (r0 == r1) {
			return 0;
		}
		if (r0 == null) {
			return Integer.MIN_VALUE;
		}
		if (r1 == null) {
			return Integer.MAX_VALUE;
		}
		return r0.compareTo(r1);
	}

}


