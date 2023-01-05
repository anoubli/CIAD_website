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
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Component
@Primary
public class PersonComparator implements Comparator<Person> {

	@Override
	public int compare(Person o1, Person o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = StringUtils.compare(o1.getLastName(), o2.getLastName());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getFirstName(), o2.getFirstName());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Objects.compare(o1.getGender(), o2.getGender(), (a, b) -> a.compareTo(b));
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getOfficePhone(), o2.getOfficePhone());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getMobilePhone(), o2.getMobilePhone());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getOfficeRoom(), o2.getOfficeRoom());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getEmail(), o2.getEmail());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getORCID(), o2.getORCID());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getAcademiaURL(), o2.getAcademiaURL());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getCordisURL(), o2.getCordisURL());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getDblpURL(), o2.getDblpURL());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getFacebookId(), o2.getFacebookId());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getGithubId(), o2.getGithubId());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getLinkedInId(), o2.getLinkedInId());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getResearcherId(), o2.getResearcherId());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getGoogleScholarId(), o2.getGoogleScholarId());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getResearchGateId(), o2.getResearchGateId());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Integer.compare(o1.getGoogleScholarHindex(), o2.getGoogleScholarHindex());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Integer.compare(o1.getWosHindex(), o2.getWosHindex());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getGravatarId(), o2.getGravatarId());
		if (cmp != 0) {
			return cmp;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

}
