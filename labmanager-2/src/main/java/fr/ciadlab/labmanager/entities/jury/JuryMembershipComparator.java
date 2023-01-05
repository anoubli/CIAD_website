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

package fr.ciadlab.labmanager.entities.jury;

import java.time.LocalDate;
import java.util.Comparator;

import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import org.arakhne.afc.util.CountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of jury memberships. First the years are considered in the
 * sort, Then, types and the persons are used from the highest to the lowest.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
@Primary
public class JuryMembershipComparator implements Comparator<JuryMembership> {

	private PersonComparator personComparator;

	/** Constructor.
	 *
	 * @param personComparator the comparator of persons names.
	 */
	public JuryMembershipComparator(@Autowired PersonComparator personComparator) {
		this.personComparator = personComparator;
	}

	@Override
	public int compare(JuryMembership o1, JuryMembership o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int n = compareDates(o1.getDate(), o2.getDate());
		if (n != 0) {
			return n;
		}
		n = compareMembershipTypes(o1.getType(), o2.getType());
		if (n != 0) {
			return n;
		}
		n = compareCountries(o1.getCountry(), o2.getCountry());
		if (n != 0) {
			return n;
		}
		n = compareDefenseTypes(o1.getDefenseType(), o2.getDefenseType());
		if (n != 0) {
			return n;
		}
		n = this.personComparator.compare(o1.getPerson(), o2.getPerson());
		if (n != 0) {
			return n;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

	private static int compareCountries(CountryCode c1, CountryCode c2) {
		if (c1 == c2) {
			return 0;
		}
		if (c1 == null) {
			return Integer.MIN_VALUE;
		}
		if (c2 == null) {
			return Integer.MAX_VALUE;
		}
		// France has lower priority
		final boolean f1 = CountryCodeUtils.isFrance(c1);
		final boolean f2 = CountryCodeUtils.isFrance(c2);
		if (f1 == f2) {
			return c1.compareTo(c2);
		}
		if (f1) {
			return 1;
		}
		return -1;
	}

	private static int compareDates(LocalDate d1, LocalDate d2) {
		if (d1 == d2) {
			return 0;
		}
		if (d1 == null) {
			return Integer.MIN_VALUE;
		}
		if (d2 == null) {
			return Integer.MAX_VALUE;
		}
		return - d1.compareTo(d2);
	}

	private static int compareDefenseTypes(JuryType t1, JuryType t2) {
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

	private static int compareMembershipTypes(JuryMembershipType t1, JuryMembershipType t2) {
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
