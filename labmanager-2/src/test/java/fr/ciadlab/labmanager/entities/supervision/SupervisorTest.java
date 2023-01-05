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

package fr.ciadlab.labmanager.entities.supervision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import fr.ciadlab.labmanager.entities.member.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Supervisor}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */@SuppressWarnings("all")
 public class SupervisorTest {

	 private Supervisor test;

	 @BeforeEach
	 public void setUp() {
		 this.test = new Supervisor();
	 }

	 @Test
	 public void getId() {
		 assertEquals(0, this.test.getId());
	 }

	 @Test
	 public void setId() {
		 this.test.setId(123456);
		 assertEquals(123456, this.test.getId());
	 }

	 @Test
	 public void getPercentage() {
		 assertEquals(0, this.test.getPercentage());
	 }

	 @Test
	 public void setPercentage() {
		 this.test.setPercentage((byte) -1);
		 assertEquals(0, this.test.getPercentage());

		 this.test.setPercentage((byte) 0);
		 assertEquals(0, this.test.getPercentage());

		 this.test.setPercentage((byte) 105);
		 assertEquals(100, this.test.getPercentage());

		 this.test.setPercentage((byte) 64);
		 assertEquals(64, this.test.getPercentage());
	 }

	 @Test
	 public void getType() {
		 assertSame(SupervisorType.SUPERVISOR, this.test.getType());
	 }

	 @Test
	 public void setType_SupervisorType() {
		 this.test.setType(SupervisorType.DIRECTOR);
		 assertSame(SupervisorType.DIRECTOR, this.test.getType());

		 this.test.setType((SupervisorType) null);
		 assertSame(SupervisorType.SUPERVISOR, this.test.getType());
	 }

	 @Test
	 public void setType_String() {
		 this.test.setType("Director");
		 assertSame(SupervisorType.DIRECTOR, this.test.getType());

		 this.test.setType((String) null);
		 assertSame(SupervisorType.SUPERVISOR, this.test.getType());

		 this.test.setType("DireCtor");
		 assertSame(SupervisorType.DIRECTOR, this.test.getType());

		 this.test.setType("");
		 assertSame(SupervisorType.SUPERVISOR, this.test.getType());
	 }

	 @Test
	 public void getSupervisor() {
		 assertNull(this.test.getSupervisor());
	 }

	 @Test
	 public void setSupervisor() {
		 assertNull(this.test.getSupervisor());

		 Person s0 = mock(Person.class);
		 this.test.setSupervisor(s0);
		 assertSame(s0, this.test.getSupervisor());
	 }

 }