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

import java.util.Comparator;

import fr.ciadlab.labmanager.entities.member.PersonComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of supervisors.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */
@Component
@Primary
public class SupervisorComparator implements Comparator<Supervisor> {

	private PersonComparator personComparator;

	/** Constructor.
	 *
	 * @param personComparator the comparator of persons names.
	 */
	public SupervisorComparator(@Autowired PersonComparator personComparator) {
		this.personComparator = personComparator;
	}

	@Override
	public int compare(Supervisor s1, Supervisor s2) {
		if (s1 == s2) {
			return 0;
		}
		if (s1 == null) {
			return Integer.MIN_VALUE;
		}
		if (s2 == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = compareSupervisorTypes(s1.getType(), s2.getType());
		if (cmp != 0) {
			return cmp;
		}
		// Higher percentage first
		cmp = Integer.compare(s2.getPercentage(), s1.getPercentage());
		if (cmp != 0) {
			return cmp;
		}
		cmp = this.personComparator.compare(s1.getSupervisor(), s2.getSupervisor());
		if (cmp != 0) {
			return cmp;
		}
		return Integer.compare(s1.getId(), s2.getId());
	}

	private static int compareSupervisorTypes(SupervisorType t1, SupervisorType t2) {
		if (t1 == t2) {
			return 0;
		}
		if (t1 == null) {
			return Integer.MIN_VALUE;
		}
		if (t2 == null) {
			return Integer.MAX_VALUE;
		}
		return t1.compareTo(t2);
	}

}
