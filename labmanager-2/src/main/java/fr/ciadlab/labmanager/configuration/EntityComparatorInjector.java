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

package fr.ciadlab.labmanager.configuration;

import javax.annotation.PostConstruct;

import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.invitation.PersonInvitationComparator;
import fr.ciadlab.labmanager.entities.journal.JournalComparator;
import fr.ciadlab.labmanager.entities.jury.JuryMembershipComparator;
import fr.ciadlab.labmanager.entities.member.MembershipComparator;
import fr.ciadlab.labmanager.entities.member.NameBasedMembershipComparator;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.member.PersonListComparator;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddressComparator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationComparator;
import fr.ciadlab.labmanager.entities.publication.PublicationComparator;
import fr.ciadlab.labmanager.entities.supervision.SupervisionComparator;
import fr.ciadlab.labmanager.entities.supervision.SupervisorComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Spring component that is injecting the comparators for the Spring entities.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
public class EntityComparatorInjector {

	@Autowired
	private PersonComparator personComparator;

	@Autowired
	private PersonListComparator personListComparator;

	@Autowired
	private MembershipComparator membershipComparator;

	@Autowired
	private NameBasedMembershipComparator nameMembershipComparator;

	@Autowired
	private ResearchOrganizationComparator organizationComparator;

	@Autowired
	private OrganizationAddressComparator organizationAddressComparator;

	@Autowired
	private PublicationComparator publicationComparator;

	@Autowired
	private JournalComparator journalComparator;

	@Autowired
	private JuryMembershipComparator juryMembershipComparator;

	@Autowired
	private PersonInvitationComparator personInvitationComparator;

	@Autowired
	private SupervisorComparator supervisorComparator;

	@Autowired
	private SupervisionComparator supervisionComparator;

	/** Invoked by the Spring engine is started and this injector is created in memory.
	 */
	@PostConstruct
	public void postConstruct() {
		EntityUtils.setPreferredResearchOrganizationComparator(this.organizationComparator);
		EntityUtils.setPreferredOrganizationAddressComparator(this.organizationAddressComparator);
		EntityUtils.setPreferredPersonComparator(this.personComparator);
		EntityUtils.setPreferredPersonListComparator(this.personListComparator);
		EntityUtils.setPreferredMembershipComparator(this.membershipComparator);
		EntityUtils.setPreferredPersonNameBasedMembershipComparator(this.nameMembershipComparator);
		EntityUtils.setPreferredPublicationComparator(this.publicationComparator);
		EntityUtils.setPreferredJournalComparator(this.journalComparator);
		EntityUtils.setPreferredJuryMembershipComparator(this.juryMembershipComparator);
		EntityUtils.setPreferredPersonInvitationComparator(this.personInvitationComparator);
		EntityUtils.setPreferredSupervisorComparator(this.supervisorComparator);
		EntityUtils.setPreferredSupervisionComparator(this.supervisionComparator);
	}

}
