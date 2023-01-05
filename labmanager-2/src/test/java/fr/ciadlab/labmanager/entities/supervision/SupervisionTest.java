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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Supervision}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */@SuppressWarnings("all")
 public class SupervisionTest {

	 private Supervision test;

	 @BeforeEach
	 public void setUp() {
		 this.test = new Supervision();
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
	 public void isAbandonment() {
		 assertFalse(this.test.isAbandonment());
	 }

	 @Test
	 public void setAbandonment() {
		 this.test.setAbandonment(true);
		 assertTrue(this.test.isAbandonment());
		 this.test.setAbandonment(false);
		 assertFalse(this.test.isAbandonment());
	 }

	 @Test
	 public void getFunding() {
		 assertSame(FundingScheme.NOT_FUNDED, this.test.getFunding());
	 }

	 @Test
	 public void setFunding_FundingScheme() {
		 this.test.setFunding(FundingScheme.CAMPUS_FRANCE);
		 assertSame(FundingScheme.CAMPUS_FRANCE, this.test.getFunding());

		 this.test.setFunding((FundingScheme) null);
		 assertSame(FundingScheme.NOT_FUNDED, this.test.getFunding());
	 }
	 
	 @Test
	 public void setFunding_String() {
		 this.test.setFunding("CAMPUS_FRANCE");
		 assertSame(FundingScheme.CAMPUS_FRANCE, this.test.getFunding());

		 this.test.setFunding((String) null);
		 assertSame(FundingScheme.NOT_FUNDED, this.test.getFunding());

		 this.test.setFunding("anr");
		 assertSame(FundingScheme.ANR, this.test.getFunding());

		 this.test.setFunding("");
		 assertSame(FundingScheme.NOT_FUNDED, this.test.getFunding());
	 }

	 @Test
	 public void getFundingDetails() {
		 assertNull(this.test.getFundingDetails());
	 }

	 @Test
	 public void setFundingDetails() {
		 this.test.setFundingDetails("CAMPUS_FRANCE");
		 assertEquals("CAMPUS_FRANCE", this.test.getFundingDetails());

		 this.test.setFundingDetails(null);
		 assertNull(this.test.getFundingDetails());

		 this.test.setFundingDetails("anr");
		 assertEquals("anr", this.test.getFundingDetails());

		 this.test.setFundingDetails("");
		 assertNull(this.test.getFundingDetails());
	 }

	 @Test
	 public void getNumberOfAterPositions() {
		 assertEquals(0, this.test.getNumberOfAterPositions());
	 }

	 @Test
	 public void setNumberOfAterPositions() {
		 this.test.setNumberOfAterPositions(-1);
		 assertEquals(0, this.test.getNumberOfAterPositions());

		 this.test.setNumberOfAterPositions(6);
		 assertEquals(6, this.test.getNumberOfAterPositions());
	 }
	 
	 @Test
	 public void getTitle() {
		 assertNull(this.test.getTitle());
	 }

	 @Test
	 public void setTitle() {
		 this.test.setTitle("xyz");
		 assertEquals("xyz", this.test.getTitle());

		 this.test.setTitle(null);
		 assertNull(this.test.getTitle());

		 this.test.setTitle("abc");
		 assertEquals("abc", this.test.getTitle());

		 this.test.setTitle("");
		 assertNull(this.test.getTitle());
	 }

	 @Test
	 public void isEntrepreneur() {
		 assertFalse(this.test.isEntrepreneur());
	 }

	 @Test
	 public void setEntrepreneur() {
		 this.test.setEntrepreneur(true);
		 assertTrue(this.test.isEntrepreneur());

		 this.test.setEntrepreneur(false);
		 assertFalse(this.test.isEntrepreneur());
	 }

	 @Test
	 public void isJointPosition() {
		 assertFalse(this.test.isJointPosition());
	 }

	 @Test
	 public void setJointPosition() {
		 this.test.setJointPosition(true);
		 assertTrue(this.test.isJointPosition());

		 this.test.setJointPosition(false);
		 assertFalse(this.test.isJointPosition());
	 }

	 @Test
	 public void getDefenseDate() {
		 assertNull(this.test.getDefenseDate());
	 }

	 @Test
	 public void setDefenseDate_LocalDate() {
		 this.test.setDefenseDate(LocalDate.parse("2020-10-16"));
		 assertEquals(LocalDate.parse("2020-10-16"), this.test.getDefenseDate());

		 this.test.setDefenseDate((LocalDate) null);
		 assertNull(this.test.getDefenseDate());
	 }

	 @Test
	 public void setDefenseDate_String() {
		 this.test.setDefenseDate("2020-10-16");
		 assertEquals(LocalDate.parse("2020-10-16"), this.test.getDefenseDate());

		 this.test.setDefenseDate((String) null);
		 assertNull(this.test.getDefenseDate());

		 this.test.setDefenseDate("2020-10-15");
		 assertEquals(LocalDate.parse("2020-10-15"), this.test.getDefenseDate());

		 this.test.setDefenseDate("");
		 assertNull(this.test.getDefenseDate());
	 }

	 @Test
	 public void getPositionAfterSupervision() {
		 assertNull(this.test.getPositionAfterSupervision());
	 }

	 @Test
	 public void setPositionAfterSupervision() {
		 this.test.setPositionAfterSupervision("xyz");
		 assertEquals("xyz", this.test.getPositionAfterSupervision());

		 this.test.setPositionAfterSupervision(null);
		 assertNull(this.test.getPositionAfterSupervision());

		 this.test.setPositionAfterSupervision("abc");
		 assertEquals("abc", this.test.getPositionAfterSupervision());

		 this.test.setPositionAfterSupervision("");
		 assertNull(this.test.getPositionAfterSupervision());
	 }

	 @Test
	 public void getSupervisedPerson() {
		 assertNull(this.test.getSupervisedPerson());
	 }

	 @Test
	 public void setSupervisedPerson() {
		 Membership mbr = mock(Membership.class);
		 this.test.setSupervisedPerson(mbr);
		 assertSame(mbr, this.test.getSupervisedPerson());
	 }

	 @Test
	 public void getSupervisors() {
		 assertTrue(this.test.getSupervisors().isEmpty());
	 }

	 @Test
	 public void setSupervisors() {
		 Supervisor s0 = mock(Supervisor.class);
		 Supervisor s1 = mock(Supervisor.class);
		 List<Supervisor> ss = Arrays.asList(s0, s1);
		 this.test.setSupervisors(ss);
		 assertFalse(this.test.getSupervisors().isEmpty());
		 assertEquals(2, this.test.getSupervisors().size());
		 assertTrue(this.test.getSupervisors().contains(s0));
		 assertTrue(this.test.getSupervisors().contains(s1));
	 }

 }