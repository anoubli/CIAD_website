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

package fr.ciadlab.labmanager.entities.organization;

import java.util.Comparator;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.arakhne.afc.util.CountryCode;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of research organizations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Component
@Primary
public class ResearchOrganizationComparator implements Comparator<ResearchOrganization> {

	@Override
	public int compare(ResearchOrganization o1, ResearchOrganization o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = Objects.compare(o1.getType(), o2.getType(), (a, b) -> a.compareTo(b));
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getAcronym(), o2.getAcronym());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getName(), o2.getName());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getDescription(), o2.getDescription());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Boolean.compare(o1.isMajorOrganization(), o2.isMajorOrganization());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getRnsr(), o2.getRnsr());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getNationalIdentifier(), o2.getNationalIdentifier());
		if (cmp != 0) {
			return cmp;
		}
		cmp = compareCountry(o1.getCountry(), o2.getCountry());
		if (cmp != 0) {
			return cmp;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

	private static int compareCountry(CountryCode c0, CountryCode c1) {
		if (c0 == c1) {
			return 0;
		}
		if (c0 == null) {
			return Integer.MIN_VALUE;
		}
		if (c1 == null) {
			return Integer.MAX_VALUE;
		}
		return c0.compareTo(c1);
	}

}
