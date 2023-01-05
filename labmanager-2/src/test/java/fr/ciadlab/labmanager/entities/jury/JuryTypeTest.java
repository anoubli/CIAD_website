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

/** Tests for {@link JuryType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@SuppressWarnings("all")
public class JuryTypeTest {

	private List<JuryType> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(JuryType.values()));
	}

	private JuryType cons(JuryType type) {
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
		assertEquals("HDR", cons(JuryType.HDR).getLabel());
		assertEquals("PhD", cons(JuryType.PHD).getLabel());
		assertEquals("Master", cons(JuryType.MASTER).getLabel());
		assertEquals("Baccalaureat", cons(JuryType.BAC).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_FR() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("HDR", cons(JuryType.HDR).getLabel());
		assertEquals("PhD", cons(JuryType.PHD).getLabel());
		assertEquals("Master", cons(JuryType.MASTER).getLabel());
		assertEquals("Baccalaureat", cons(JuryType.BAC).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() {
		assertEquals("HDR", cons(JuryType.HDR).getLabel(Locale.US));
		assertEquals("PhD", cons(JuryType.PHD).getLabel(Locale.US));
		assertEquals("Master", cons(JuryType.MASTER).getLabel(Locale.US));
		assertEquals("Baccalaureat", cons(JuryType.BAC).getLabel(Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() {
		assertEquals("HDR", cons(JuryType.HDR).getLabel(Locale.FRANCE));
		assertEquals("Doctorat", cons(JuryType.PHD).getLabel(Locale.FRANCE));
		assertEquals("Master", cons(JuryType.MASTER).getLabel(Locale.FRANCE));
		assertEquals("Baccalauréat", cons(JuryType.BAC).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

}
