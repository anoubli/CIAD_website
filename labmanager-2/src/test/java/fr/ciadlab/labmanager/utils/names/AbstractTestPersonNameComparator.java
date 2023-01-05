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

package fr.ciadlab.labmanager.utils.names;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Abstract tests for comparators of person names 
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@SuppressWarnings("all")
public abstract class AbstractTestPersonNameComparator {

	protected PersonNameParser nameParser;

	protected PersonNameComparator test;

	public void setUp() {
		this.nameParser = new DefaultPersonNameParser();
	}

	@Test
	public void isSimilar_StringStringStringString_null_0() {
		assertTrue(this.test.isSimilar(null, "Galland", "Stephane", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_null_1() {
		assertTrue(this.test.isSimilar("Stephane", null, "Stephane", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_null_2() {
		assertTrue(this.test.isSimilar("Stephane", "Galland", null, "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_null_3() {
		assertTrue(this.test.isSimilar("Stephane", "Galland", "Stephane", null));
	}

	@Test
	public void isSimilar_StringStringStringString_null_4() {
		assertTrue(this.test.isSimilar("Stephane", null, "Stephane", null));
	}

	@Test
	public void isSimilar_StringStringStringString_null_5() {
		assertTrue(this.test.isSimilar(null, "Galland", null, "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_null_6() {
		assertTrue(this.test.isSimilar(null, null, null, null));
	}

	@Test
	public void isSimilar_StringStringStringString_empty_0() {
		assertTrue(this.test.isSimilar("", "Galland", "Stephane", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_empty_1() {
		assertTrue(this.test.isSimilar("Stephane", "", "Stephane", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_empty_2() {
		assertTrue(this.test.isSimilar("Stephane", "Galland", "", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_empty_3() {
		assertTrue(this.test.isSimilar("Stephane", "Galland", "Stephane", ""));
	}

	@Test
	public void isSimilar_StringStringStringString_empty_4() {
		assertTrue(this.test.isSimilar("Stephane", "", "Stephane", ""));
	}

	@Test
	public void isSimilar_StringStringStringString_empty_5() {
		assertTrue(this.test.isSimilar("", "Galland", "", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_empty_6() {
		assertTrue(this.test.isSimilar("", "", "", ""));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_0() {
		assertFalse(this.test.isSimilar("Stephane", "Galland", "Etienne", "Dupont"));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_1() {
		assertFalse(this.test.isSimilar("Stephane", "Galland", "Etienne", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_2() {
		assertFalse(this.test.isSimilar("Stephane", "Galland", "Stephane", "Dupont"));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_3() {
		assertTrue(this.test.isSimilar("Stephane", "Galland", "Stephane", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_4() {
		assertFalse(this.test.isSimilar("Stephane", "Galland", "E", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_5() {
		assertFalse(this.test.isSimilar("Stephane", "Galland", "E.", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_6() {
		assertFalse(this.test.isSimilar("Stephane", "Galland", "Galland", "E"));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_7() {
		assertFalse(this.test.isSimilar("Stephane", "Galland", "Galland", "E."));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_8() {
		assertTrue(this.test.isSimilar("Stephane", "Galland", "S", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_9() {
		assertTrue(this.test.isSimilar("Stephane", "Galland", "S.", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_10() {
		assertTrue(this.test.isSimilar("Stephane", "Galland", "Galland", "S"));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_11() {
		assertTrue(this.test.isSimilar("Stephane", "Galland", "Galland", "S."));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_12() {
		assertFalse(this.test.isSimilar("Stephane", "Galland", "Sarah", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_simpleName_13() {
		assertFalse(this.test.isSimilar("Stephane", "Galland", "Galland", "Sarah"));
	}

	@Test
	public void isSimilar_StringStringStringString_accent_0() {
		assertTrue(this.test.isSimilar("Stéphane", "Galland", "Stephane", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_accent_1() {
		assertTrue(this.test.isSimilar("Stéphane", "Galland", "S", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_accent_2() {
		assertTrue(this.test.isSimilar("Stéphane", "Galland", "S.", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_accent_3() {
		assertTrue(this.test.isSimilar("Stéphane", "Galland", "Galland", "Stéphane"));
	}

	@Test
	public void isSimilar_StringStringStringString_accent_4() {
		assertFalse(this.test.isSimilar("Stéphane", "Galland", "Galland", "étienne"));
	}

	@Test
	public void isSimilar_StringStringStringString_accent_5() {
		assertFalse(this.test.isSimilar("Stéphane", "Galland", "étienne", "Galland"));
	}

	@Test
	public void isSimilar_StringStringStringString_accent_6() {
		assertTrue(this.test.isSimilar("étienne", "dupont", "etienne", "dupont"));
	}

	@Test
	public void isSimilar_StringStringStringString_specific_0() {
		assertTrue(this.test.isSimilar("Meriem", "Fekih", "Meriam", "Fekih"));
	}

	@Test
	public void isSimilar_StringStringStringString_specific_1() {
		assertTrue(this.test.isSimilar("Fekih", "Meriem", "Meriam", "Fekih"));
	}

	@Test
	public void isSimilar_StringStringStringString_specific_2() {
		assertTrue(this.test.isSimilar("Meriem", "Fekih", "Meriam", "Fekih"));
	}

	@Test
	public void isSimilar_StringStringStringString_specific_3() {
		assertFalse(this.test.isSimilar("Fekih", "Mariem", "Meriam", "Fekih"));
	}

}
