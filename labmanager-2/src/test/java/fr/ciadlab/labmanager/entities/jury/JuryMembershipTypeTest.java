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

/** Tests for {@link JuryMembershipType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@SuppressWarnings("all")
public class JuryMembershipTypeTest {

	private List<JuryMembershipType> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(JuryMembershipType.values()));
	}

	private JuryMembershipType cons(JuryMembershipType type) {
		assertTrue(this.items.remove(type), "Expecting enumeration item: " + type.toString());
		return type;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getLabel_US_MALE() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(Gender.MALE));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.MALE));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(Gender.MALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_US_FEMALE() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(Gender.FEMALE));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.FEMALE));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(Gender.FEMALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_US_OTHER() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(Gender.OTHER));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.OTHER));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(Gender.OTHER));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_MALE() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(Gender.MALE));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.MALE));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(Gender.MALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_FEMALE() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(Gender.FEMALE));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.FEMALE));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(Gender.FEMALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_OTHER() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(Gender.OTHER));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.OTHER));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(Gender.OTHER));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_MALE() {
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(Gender.MALE, Locale.US));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.MALE, Locale.US));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(Gender.MALE, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_FEMALE() {
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(Gender.FEMALE, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_OTHER() {
		assertEquals("Examiner", cons(JuryMembershipType.EXAMINER).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Invited member", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.OTHER, Locale.US));
		assertEquals("President", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Reviewer", cons(JuryMembershipType.REVIEWER).getLabel(Gender.OTHER, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_MALE() {
		assertEquals("Examinateur", cons(JuryMembershipType.EXAMINER).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Membre invité", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Président", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Rapporteur", cons(JuryMembershipType.REVIEWER).getLabel(Gender.MALE, Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_FEMALE() {
		assertEquals("Examinatrice", cons(JuryMembershipType.EXAMINER).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Membre invitée", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Présidente", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Rapportrice", cons(JuryMembershipType.REVIEWER).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_OTHER() {
		assertEquals("Examinateur.trice", cons(JuryMembershipType.EXAMINER).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Membre invité.e", cons(JuryMembershipType.INVITED_PERSON).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Président.e", cons(JuryMembershipType.PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Rapporteur.trice", cons(JuryMembershipType.REVIEWER).getLabel(Gender.OTHER, Locale.FRANCE));
		assertAllTreated();
	}

}
