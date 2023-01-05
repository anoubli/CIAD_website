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

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of organization addresses.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Component
@Primary
public class OrganizationAddressComparator implements Comparator<OrganizationAddress> {

	@Override
	public int compare(OrganizationAddress o1, OrganizationAddress o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = StringUtils.compareIgnoreCase(o1.getName(), o2.getName());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compareIgnoreCase(o1.getComplement(), o2.getComplement());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compareIgnoreCase(o1.getStreet(), o2.getStreet());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compareIgnoreCase(o1.getZipCode(), o2.getZipCode());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compareIgnoreCase(o1.getCity(), o2.getCity());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getMapCoordinates(), o2.getMapCoordinates());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compare(o1.getGoogleMapLink(), o2.getGoogleMapLink());
		if (cmp != 0) {
			return cmp;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

}
