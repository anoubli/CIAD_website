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

package fr.ciadlab.labmanager.entities.invitation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.ciadlab.labmanager.entities.member.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link PersonInvitationType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@SuppressWarnings("all")
public class PersonInvitationTypeTest {

	private List<PersonInvitationType> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(PersonInvitationType.values()));
	}

	private PersonInvitationType cons(PersonInvitationType type) {
		assertTrue(this.items.remove(type), "Expecting enumeration item: " + type.toString());
		return type;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getLabel_US() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Incoming Guest Professors", cons(PersonInvitationType.INCOMING_GUEST_PROFESSOR).getLabel());
		assertEquals("Incoming Guest PhD Students", cons(PersonInvitationType.INCOMING_GUEST_PHD_STUDENT).getLabel());
		assertEquals("Outgoing Invitations", cons(PersonInvitationType.OUTGOING_GUEST).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_FR() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Incoming Guest Professors", cons(PersonInvitationType.INCOMING_GUEST_PROFESSOR).getLabel());
		assertEquals("Incoming Guest PhD Students", cons(PersonInvitationType.INCOMING_GUEST_PHD_STUDENT).getLabel());
		assertEquals("Outgoing Invitations", cons(PersonInvitationType.OUTGOING_GUEST).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() {
		assertEquals("Incoming Guest Professors", cons(PersonInvitationType.INCOMING_GUEST_PROFESSOR).getLabel(Locale.US));
		assertEquals("Incoming Guest PhD Students", cons(PersonInvitationType.INCOMING_GUEST_PHD_STUDENT).getLabel(Locale.US));
		assertEquals("Outgoing Invitations", cons(PersonInvitationType.OUTGOING_GUEST).getLabel(Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() {
		assertEquals("Professeurs invités", cons(PersonInvitationType.INCOMING_GUEST_PROFESSOR).getLabel(Locale.FRANCE));
		assertEquals("Doctorants invités", cons(PersonInvitationType.INCOMING_GUEST_PHD_STUDENT).getLabel(Locale.FRANCE));
		assertEquals("Invitations sortantes", cons(PersonInvitationType.OUTGOING_GUEST).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

}
