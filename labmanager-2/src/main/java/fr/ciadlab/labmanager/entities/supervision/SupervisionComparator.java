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

package fr.ciadlab.labmanager.entities.supervision;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import fr.ciadlab.labmanager.entities.member.MembershipComparator;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of supervisions.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */
@Component
@Primary
public class SupervisionComparator implements Comparator<Supervision> {

	private MembershipComparator membershipComparator;

	private SupervisorComparator supervisorComparator;

	/** Constructor.
	 *
	 * @param membershipComparator the comparator of memberships.
	 * @param supervisorComparator the comparator of supervisors.
	 */
	public SupervisionComparator(
			@Autowired MembershipComparator membershipComparator,
			@Autowired SupervisorComparator supervisorComparator) {
		this.membershipComparator = membershipComparator;
		this.supervisorComparator = supervisorComparator;
	}

	@Override
	public int compare(Supervision s1, Supervision s2) {
		if (s1 == s2) {
			return 0;
		}
		if (s1 == null) {
			return Integer.MIN_VALUE;
		}
		if (s2 == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = this.membershipComparator.compare(s1.getSupervisedPerson(), s2.getSupervisedPerson());
		if (cmp != 0) {
			return cmp;
		}
		cmp = compareSupervisors(s1.getSupervisors(), s2.getSupervisors());
		if (cmp != 0) {
			return cmp;
		}
		cmp = compareStrings(s1.getTitle(), s2.getTitle());
		if (cmp != 0) {
			return cmp;
		}
		cmp = compareFundingSchemes(s1.getFunding(), s2.getFunding());
		if (cmp != 0) {
			return cmp;
		}
		cmp = compareDates(s1.getDefenseDate(), s2.getDefenseDate());
		if (cmp != 0) {
			return cmp;
		}
		cmp = compareStrings(s1.getPositionAfterSupervision(), s2.getPositionAfterSupervision());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Integer.compare(s1.getNumberOfAterPositions(), s2.getNumberOfAterPositions());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Boolean.compare(s1.isJointPosition(), s2.isJointPosition());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Boolean.compare(s1.isEntrepreneur(), s2.isEntrepreneur());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Boolean.compare(s1.isAbandonment(), s2.isAbandonment());
		if (cmp != 0) {
			return cmp;
		}
		return Integer.compare(s1.getId(), s2.getId());
	}

	private static int compareStrings(String s1, String s2) {
		if (s1 == s2) {
			return 0;
		}
		if (s1 == null) {
			return Integer.MIN_VALUE;
		}
		if (s2 == null) {
			return Integer.MAX_VALUE;
		}
		return s1.compareTo(s2);
	}

	private static int compareFundingSchemes(FundingScheme s1, FundingScheme s2) {
		if (s1 == s2) {
			return 0;
		}
		if (s1 == null) {
			return Integer.MIN_VALUE;
		}
		if (s2 == null) {
			return Integer.MAX_VALUE;
		}
		return s1.compareTo(s2);
	}

	private static int compareDates(LocalDate s1, LocalDate s2) {
		if (s1 == s2) {
			return 0;
		}
		if (s1 == null) {
			return Integer.MIN_VALUE;
		}
		if (s2 == null) {
			return Integer.MAX_VALUE;
		}
		return s1.compareTo(s2);
	}

	private int compareSupervisors(List<Supervisor> s1, List<Supervisor> s2) {
		if (s1 == s2) {
			return 0;
		}
		if (s1 == null) {
			return Integer.MIN_VALUE;
		}
		if (s2 == null) {
			return Integer.MAX_VALUE;
		}
		final Iterator<Supervisor> it1 = s1.iterator();
		final Iterator<Supervisor> it2 = s2.iterator();
		while (it1.hasNext() && it2.hasNext()) {
			final Supervisor su1 = it1.next();
			final Supervisor su2 = it2.next();
			final int cmp = this.supervisorComparator.compare(su1, su2);
			if (cmp != 0) {
				return cmp;
			}
		}
		if (it1.hasNext()) {
			return Integer.MAX_VALUE;
		}
		if (it2.hasNext()) {
			return Integer.MIN_VALUE;
		}
		return 0;
	}

}
