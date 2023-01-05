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

package fr.ciadlab.labmanager.repository.invitation;

import java.util.List;

import fr.ciadlab.labmanager.entities.invitation.PersonInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for person invitations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public interface PersonInvitationRepository extends JpaRepository<PersonInvitation, Integer> {

	/** Replies the invitations for the given person whatever if he/she is invitee or inviter.
	 *
	 * @param guestId the identifier of the guest.
	 * @param inviterId the identifier of the inviter.
	 * @return the invitations for the person.
	 */
	List<PersonInvitation> findAllByGuestIdOrInviterId(int guestId, int inviterId);

	/** Count the number of persons in the invitations whatever if he/she is invitee or inviter.
	 *
	 * @param guestId the identifier of the guest.
	 * @param inviterId the identifier of the inviter.
	 * @return the count of invitations
	 */
	int countDistinctByGuestIdOrInviterId(int guestId, int inviterId);

}
