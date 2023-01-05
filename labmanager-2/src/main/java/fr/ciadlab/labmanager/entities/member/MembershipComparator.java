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

import fr.ciadlab.labmanager.entities.organization.OrganizationAddressComparator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of memberships. First the organizations are considered in the
 * sort, Then, dates are sorted from the highest to the lowest.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Component
@Primary
public class MembershipComparator extends AbstractMembershipComparator {

	private PersonComparator personComparator;

	private ResearchOrganizationComparator organizationComparator;

	private OrganizationAddressComparator addressComparator;

	/** Constructor.
	 *
	 * @param personComparator the comparator of persons names.
	 * @param organizationComparator the comparator of research organizations.
	 * @param addressComparator the comparator of organization addresses.
	 */
	public MembershipComparator(@Autowired PersonComparator personComparator,
			@Autowired ResearchOrganizationComparator organizationComparator,
			@Autowired OrganizationAddressComparator addressComparator) {
		this.personComparator = personComparator;
		this.organizationComparator = organizationComparator;
		this.addressComparator = addressComparator;
	}

	@Override
	public int compare(Membership o1, Membership o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int n = this.organizationComparator.compare(o1.getResearchOrganization(), o2.getResearchOrganization());
		if (n != 0) {
			return n;
		}
		n = this.addressComparator.compare(o1.getOrganizationAddress(), o2.getOrganizationAddress());
		if (n != 0) {
			return n;
		}
		n = o1.getMemberStatus().compareTo(o2.getMemberStatus());
		if (n != 0) {
			return n;
		}
		n = compareDate(o1.getMemberSinceWhen(), o2.getMemberSinceWhen());
		if (n != 0) {
			return n;
		}
		n = compareDate(o1.getMemberToWhen(), o2.getMemberToWhen());
		if (n != 0) {
			return n;
		}
		n = this.personComparator.compare(o1.getPerson(), o2.getPerson());
		if (n != 0) {
			return n;
		}
		n = compareResponsabilities(o1.getResponsibility(), o2.getResponsibility());
		if (n != 0) {
			return n;
		}
		n = compareCnuSection(o1.getCnuSection(), o2.getCnuSection());
		if (n != 0) {
			return n;
		}
		n = compareConrsSection(o1.getConrsSection(), o2.getConrsSection());
		if (n != 0) {
			return n;
		}
		n = compareFrenchBap(o1.getFrenchBap(), o2.getFrenchBap());
		if (n != 0) {
			return n;
		}
		// Main position order is reversed to put the "true" before the "false"
		n = Boolean.compare(o2.isMainPosition(), o1.isMainPosition());
		if (n != 0) {
			return n;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

}


