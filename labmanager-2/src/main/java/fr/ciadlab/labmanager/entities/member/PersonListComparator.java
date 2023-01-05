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

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of lists of persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Component
@Primary
public class PersonListComparator implements Comparator<List<Person>> {

	private PersonComparator personComparator;

	/** Constructor.
	 *
	 * @param personComparator the comparator of person names.
	 */
	public PersonListComparator(@Autowired PersonComparator personComparator) {
		this.personComparator = personComparator;
	}

	@Override
	public int compare(List<Person> o1, List<Person> o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		final Iterator<Person> it1 = o1.iterator();
		final Iterator<Person> it2 = o2.iterator();
		while (it1.hasNext() && it2.hasNext()) {
			final Person p1 = it1.next();
			final Person p2 = it2.next();
			final int cmp = this.personComparator.compare(p1, p2);
			if (cmp != 0) {
				return cmp;
			}
		}
		if (it1.hasNext()) {
			return 1;
		}
		if (it2.hasNext()) {
			return -1;
		}
		return 0;
	}

}
