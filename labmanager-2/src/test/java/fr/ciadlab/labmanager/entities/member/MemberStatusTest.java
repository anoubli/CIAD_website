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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link MemberStatus}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class MemberStatusTest {

	private List<MemberStatus> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(MemberStatus.values()));
	}

	private MemberStatus cons(MemberStatus status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getHierachicalLevel() {
		assertEquals(0, cons(MemberStatus.FULL_PROFESSOR).getHierachicalLevel());
		assertEquals(0, cons(MemberStatus.RESEARCH_DIRECTOR).getHierachicalLevel());
		assertEquals(1, cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.ASSOCIATE_PROFESSOR).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.RESEARCHER_PHD).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.RESEARCHER).getHierachicalLevel());
		assertEquals(3, cons(MemberStatus.RESEARCH_ENGINEER_PHD).getHierachicalLevel());
		assertEquals(3, cons(MemberStatus.RESEARCH_ENGINEER).getHierachicalLevel());
		assertEquals(3, cons(MemberStatus.POSTDOC).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.PHD_STUDENT).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.ENGINEER_PHD).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.ENGINEER).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.ADMIN).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.TEACHER_PHD).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.TEACHER).getHierachicalLevel());
		assertEquals(5, cons(MemberStatus.MASTER_STUDENT).getHierachicalLevel());
		assertEquals(6, cons(MemberStatus.OTHER_STUDENT).getHierachicalLevel());
		assertEquals(7, cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getHierachicalLevel());
		assertEquals(7, cons(MemberStatus.ASSOCIATED_MEMBER).getHierachicalLevel());
		assertAllTreated();
	}

	@Test
	public void getUsualResearchFullTimeEquivalent() {
		assertEquals(0.5, cons(MemberStatus.FULL_PROFESSOR).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.RESEARCH_DIRECTOR).getUsualResearchFullTimeEquivalent());
		assertEquals(0.5, cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getUsualResearchFullTimeEquivalent());
		assertEquals(0.5, cons(MemberStatus.ASSOCIATE_PROFESSOR).getUsualResearchFullTimeEquivalent());
		assertEquals(0.5, cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(0.5, cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.RESEARCHER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.RESEARCHER).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.RESEARCH_ENGINEER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.RESEARCH_ENGINEER).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.POSTDOC).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.PHD_STUDENT).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.ENGINEER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.ENGINEER).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.ADMIN).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.TEACHER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.TEACHER).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.MASTER_STUDENT).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.OTHER_STUDENT).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.ASSOCIATED_MEMBER).getUsualResearchFullTimeEquivalent());
		assertAllTreated();
	}

	@Test
	public void isPermanentPositionAllowed() {
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.RESEARCH_DIRECTOR).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.RESEARCHER_PHD).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.RESEARCHER).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.POSTDOC).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.ENGINEER_PHD).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.ENGINEER).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.ADMIN).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.TEACHER_PHD).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.TEACHER).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isPermanentPositionAllowed());
		assertAllTreated();
	}

	@Test
	public void isResearcher() {
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isResearcher());
		assertTrue(cons(MemberStatus.RESEARCH_DIRECTOR).isResearcher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isResearcher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR).isResearcher());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isResearcher());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isResearcher());
		assertTrue(cons(MemberStatus.RESEARCHER_PHD).isResearcher());
		assertTrue(cons(MemberStatus.RESEARCHER).isResearcher());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isResearcher());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isResearcher());
		assertTrue(cons(MemberStatus.POSTDOC).isResearcher());
		assertTrue(cons(MemberStatus.PHD_STUDENT).isResearcher());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isResearcher());
		assertFalse(cons(MemberStatus.ENGINEER).isResearcher());
		assertFalse(cons(MemberStatus.ADMIN).isResearcher());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isResearcher());
		assertFalse(cons(MemberStatus.TEACHER).isResearcher());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isResearcher());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isResearcher());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isResearcher());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER).isResearcher());
		assertAllTreated();
	}

	@Test
	public void isTeacher() {
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCH_DIRECTOR).isTeacher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isTeacher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR).isTeacher());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isTeacher());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCHER).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isTeacher());
		assertFalse(cons(MemberStatus.POSTDOC).isTeacher());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isTeacher());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isTeacher());
		assertFalse(cons(MemberStatus.ENGINEER).isTeacher());
		assertFalse(cons(MemberStatus.ADMIN).isTeacher());
		assertTrue(cons(MemberStatus.TEACHER_PHD).isTeacher());
		assertTrue(cons(MemberStatus.TEACHER).isTeacher());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isTeacher());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isTeacher());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isTeacher());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isTeacher());
		assertAllTreated();
	}

	@Test
	public void isTechnicalStaff() {
		assertFalse(cons(MemberStatus.FULL_PROFESSOR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.RESEARCH_DIRECTOR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isTechnicalStaff());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isTechnicalStaff());
		assertFalse(cons(MemberStatus.RESEARCHER).isTechnicalStaff());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isTechnicalStaff());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.POSTDOC).isTechnicalStaff());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isTechnicalStaff());
		assertTrue(cons(MemberStatus.ENGINEER_PHD).isTechnicalStaff());
		assertTrue(cons(MemberStatus.ENGINEER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ADMIN).isTechnicalStaff());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isTechnicalStaff());
		assertFalse(cons(MemberStatus.TEACHER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isTechnicalStaff());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isTechnicalStaff());
		assertAllTreated();
	}

	@Test
	public void isAdministrativeStaff() {
		assertFalse(cons(MemberStatus.FULL_PROFESSOR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCH_DIRECTOR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCHER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.POSTDOC).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ENGINEER).isAdministrativeStaff());
		assertTrue(cons(MemberStatus.ADMIN).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.TEACHER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isAdministrativeStaff());
		assertAllTreated();
	}

	@Test
	public void isPhDOwner() {
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isPhDOwner());
		assertTrue(cons(MemberStatus.RESEARCH_DIRECTOR).isPhDOwner());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isPhDOwner());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR).isPhDOwner());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isPhDOwner());
		assertTrue(cons(MemberStatus.RESEARCHER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.RESEARCHER).isPhDOwner());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isPhDOwner());
		assertTrue(cons(MemberStatus.POSTDOC).isPhDOwner());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isPhDOwner());
		assertTrue(cons(MemberStatus.ENGINEER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.ENGINEER).isPhDOwner());
		assertFalse(cons(MemberStatus.ADMIN).isPhDOwner());
		assertTrue(cons(MemberStatus.TEACHER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.TEACHER).isPhDOwner());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isPhDOwner());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isPhDOwner());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isPhDOwner());
		assertAllTreated();
	}

	@Test
	public void isSupervisable() {
		assertFalse(cons(MemberStatus.FULL_PROFESSOR).isSupervisable());
		assertFalse(cons(MemberStatus.RESEARCH_DIRECTOR).isSupervisable());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isSupervisable());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isSupervisable());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isSupervisable());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.RESEARCHER).isSupervisable());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isSupervisable());
		assertTrue(cons(MemberStatus.POSTDOC).isSupervisable());
		assertTrue(cons(MemberStatus.PHD_STUDENT).isSupervisable());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.ENGINEER).isSupervisable());
		assertFalse(cons(MemberStatus.ADMIN).isSupervisable());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.TEACHER).isSupervisable());
		assertTrue(cons(MemberStatus.MASTER_STUDENT).isSupervisable());
		assertTrue(cons(MemberStatus.OTHER_STUDENT).isSupervisable());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isSupervisable());
		assertAllTreated();
	}

	@Test
	public void isSupervisor() {
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isSupervisor());
		assertTrue(cons(MemberStatus.RESEARCH_DIRECTOR).isSupervisor());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isSupervisor());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isSupervisable());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isSupervisor());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isSupervisor());
		assertTrue(cons(MemberStatus.RESEARCHER_PHD).isSupervisor());
		assertTrue(cons(MemberStatus.RESEARCHER).isSupervisor());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isSupervisor());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER).isSupervisor());
		assertTrue(cons(MemberStatus.POSTDOC).isSupervisor());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isSupervisor());
		assertTrue(cons(MemberStatus.ENGINEER_PHD).isSupervisor());
		assertFalse(cons(MemberStatus.ENGINEER).isSupervisor());
		assertFalse(cons(MemberStatus.ADMIN).isSupervisor());
		assertTrue(cons(MemberStatus.TEACHER_PHD).isSupervisor());
		assertFalse(cons(MemberStatus.TEACHER).isSupervisor());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isSupervisor());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isSupervisor());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isSupervisor());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER).isSupervisor());
		assertAllTreated();
	}

	@Test
	public void isExternalPosition() {
		assertFalse(cons(MemberStatus.FULL_PROFESSOR).isExternalPosition());
		assertFalse(cons(MemberStatus.RESEARCH_DIRECTOR).isExternalPosition());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isExternalPosition());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isExternalPosition());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isExternalPosition());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isExternalPosition());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isExternalPosition());
		assertFalse(cons(MemberStatus.RESEARCHER).isExternalPosition());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isExternalPosition());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isExternalPosition());
		assertFalse(cons(MemberStatus.POSTDOC).isExternalPosition());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isExternalPosition());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isExternalPosition());
		assertFalse(cons(MemberStatus.ENGINEER).isExternalPosition());
		assertFalse(cons(MemberStatus.ADMIN).isExternalPosition());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isExternalPosition());
		assertFalse(cons(MemberStatus.TEACHER).isExternalPosition());
		assertTrue(cons(MemberStatus.MASTER_STUDENT).isExternalPosition());
		assertTrue(cons(MemberStatus.OTHER_STUDENT).isExternalPosition());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isExternalPosition());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER).isExternalPosition());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel());
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel());
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel());
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel());
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel());
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel());
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel());
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel());
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel());
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel());
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel());
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel());
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel());
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel());
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel());
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel());
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel());
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel());
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel());
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel());
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_FR() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel());
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel());
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel());
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel());
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel());
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel());
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel());
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel());
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel());
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel());
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel());
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel());
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel());
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel());
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel());
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel());
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel());
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel());
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel());
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel());
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() {
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(java.util.Locale.US));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(java.util.Locale.US));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(java.util.Locale.US));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(java.util.Locale.US));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(java.util.Locale.US));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(java.util.Locale.US));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(java.util.Locale.US));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(java.util.Locale.US));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() {
		assertEquals("Professeur.e des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel(java.util.Locale.FRANCE));
		assertEquals("Directeur.trice de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(java.util.Locale.FRANCE));
		assertEquals("Maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(java.util.Locale.FRANCE));
		assertEquals("Maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(java.util.Locale.FRANCE));
		assertEquals("Enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(java.util.Locale.FRANCE));
		assertEquals("Enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(java.util.Locale.FRANCE));
		assertEquals("Chercheur.se", cons(MemberStatus.RESEARCHER_PHD).getLabel(java.util.Locale.FRANCE));
		assertEquals("Chercheur.se", cons(MemberStatus.RESEARCHER).getLabel(java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(java.util.Locale.FRANCE));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(java.util.Locale.FRANCE));
		assertEquals("Doctorant.e", cons(MemberStatus.PHD_STUDENT).getLabel(java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e", cons(MemberStatus.ENGINEER_PHD).getLabel(java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e", cons(MemberStatus.ENGINEER).getLabel(java.util.Locale.FRANCE));
		assertEquals("Personnel administratif", cons(MemberStatus.ADMIN).getLabel(java.util.Locale.FRANCE));
		assertEquals("Enseignant.e", cons(MemberStatus.TEACHER_PHD).getLabel(java.util.Locale.FRANCE));
		assertEquals("Enseignant.e", cons(MemberStatus.TEACHER).getLabel(java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiant.e master", cons(MemberStatus.MASTER_STUDENT).getLabel(java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiant.e", cons(MemberStatus.OTHER_STUDENT).getLabel(java.util.Locale.FRANCE));
		assertEquals("Membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(java.util.Locale.FRANCE));
		assertEquals("Membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(java.util.Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_US_FEMALE() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.FEMALE));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.FEMALE));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.FEMALE));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.FEMALE));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.FEMALE));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.FEMALE));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.FEMALE));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.FEMALE));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.FEMALE));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.FEMALE));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.FEMALE));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.FEMALE));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.FEMALE));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.FEMALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_FEMALE() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.FEMALE));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.FEMALE));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.FEMALE));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.FEMALE));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.FEMALE));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.FEMALE));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.FEMALE));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.FEMALE));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.FEMALE));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.FEMALE));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.FEMALE));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.FEMALE));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.FEMALE));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.FEMALE));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.FEMALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_FEMALE() {
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.FEMALE, java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_FEMALE() {
		assertEquals("Professeure des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Directrice de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Enseignante chercheuse contractuelle", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Enseignante chercheuse contractuelle", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Chercheuse", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Chercheuse", cons(MemberStatus.RESEARCHER).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Ingénieure de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Ingénieure de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Doctorante", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Ingénieure", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Ingénieure", cons(MemberStatus.ENGINEER).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Personnel administratif", cons(MemberStatus.ADMIN).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Enseignante", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Enseignante", cons(MemberStatus.TEACHER).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiante master", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiante", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Membre associée", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertEquals("Membre associée", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.FEMALE, java.util.Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_US_MALE() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.MALE));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.MALE));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.MALE));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.MALE));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.MALE));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.MALE));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.MALE));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.MALE));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.MALE));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.MALE));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.MALE));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.MALE));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.MALE));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.MALE));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.MALE));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.MALE));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.MALE));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.MALE));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.MALE));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.MALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_MALE() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.MALE));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.MALE));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.MALE));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.MALE));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.MALE));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.MALE));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.MALE));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.MALE));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.MALE));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.MALE));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.MALE));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.MALE));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.MALE));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.MALE));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.MALE));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.MALE));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.MALE));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.MALE));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.MALE));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.MALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_MALE() {
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.MALE, java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.MALE, java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_MALE() {
		assertEquals("Professeur des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Directeur de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Enseignant chercheur contractuel", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Enseignant chercheur contractuel", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Chercheur", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Chercheur", cons(MemberStatus.RESEARCHER).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Ingénieur de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Ingénieur de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Doctorant", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Ingénieur", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Ingénieur", cons(MemberStatus.ENGINEER).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Personnel administratif", cons(MemberStatus.ADMIN).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Enseignant", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Enseignant", cons(MemberStatus.TEACHER).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiant master", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiant", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Membre associé", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertEquals("Membre associé", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.MALE, java.util.Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_US_OTHER() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.OTHER));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.OTHER));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.OTHER));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.OTHER));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.OTHER));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.OTHER));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.OTHER));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.OTHER));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.OTHER));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.OTHER));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.OTHER));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.OTHER));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.OTHER));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.OTHER));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.OTHER));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.OTHER));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.OTHER));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.OTHER));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.OTHER));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.OTHER));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_OTHER() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.OTHER));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.OTHER));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.OTHER));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.OTHER));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.OTHER));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.OTHER));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.OTHER));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.OTHER));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.OTHER));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.OTHER));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.OTHER));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.OTHER));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.OTHER));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.OTHER));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.OTHER));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.OTHER));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.OTHER));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.OTHER));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.OTHER));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.OTHER));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_OTHER() {
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.OTHER, java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.OTHER, java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_OTHER() {
		assertEquals("Professeur.e des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Directeur.trice de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Chercheur.se", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Chercheur.se", cons(MemberStatus.RESEARCHER).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Doctorant.e", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e", cons(MemberStatus.ENGINEER).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Personnel administratif", cons(MemberStatus.ADMIN).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e", cons(MemberStatus.TEACHER).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiant.e master", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiant.e", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertEquals("Membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.OTHER, java.util.Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_US_NOTSPECIFIED() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.NOT_SPECIFIED));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_NOTSPECIFIED() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.NOT_SPECIFIED));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.NOT_SPECIFIED));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_NOTSPECIFIED() {
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_NOTSPECIFIED() {
		assertEquals("Professeur.e des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Directeur.trice de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Chercheur.se", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Chercheur.se", cons(MemberStatus.RESEARCHER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Doctorant.e", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e", cons(MemberStatus.ENGINEER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Personnel administratif", cons(MemberStatus.ADMIN).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e", cons(MemberStatus.TEACHER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiant.e master", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiant.e", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertEquals("Membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.NOT_SPECIFIED, java.util.Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_US_NULLGENDER() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel((Gender) null));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel((Gender) null));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel((Gender) null));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel((Gender) null));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel((Gender) null));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel((Gender) null));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel((Gender) null));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel((Gender) null));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel((Gender) null));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel((Gender) null));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel((Gender) null));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel((Gender) null));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel((Gender) null));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel((Gender) null));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel((Gender) null));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel((Gender) null));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel((Gender) null));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel((Gender) null));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel((Gender) null));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel((Gender) null));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel((Gender) null));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_NULLGENDER() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel((Gender) null));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel((Gender) null));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel((Gender) null));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel((Gender) null));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel((Gender) null));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel((Gender) null));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel((Gender) null));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel((Gender) null));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel((Gender) null));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel((Gender) null));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel((Gender) null));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel((Gender) null));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel((Gender) null));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel((Gender) null));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel((Gender) null));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel((Gender) null));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel((Gender) null));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel((Gender) null));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel((Gender) null));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel((Gender) null));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel((Gender) null));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_NULLGENDER() {
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel((Gender) null, java.util.Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel((Gender) null, java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_NULLGENDER() {
		assertEquals("Professeur.e des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Directeur.trice de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Chercheur.se", cons(MemberStatus.RESEARCHER_PHD).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Chercheur.se", cons(MemberStatus.RESEARCHER).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Doctorant.e", cons(MemberStatus.PHD_STUDENT).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e", cons(MemberStatus.ENGINEER_PHD).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Ingénieur.e", cons(MemberStatus.ENGINEER).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Personnel administratif", cons(MemberStatus.ADMIN).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e", cons(MemberStatus.TEACHER_PHD).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Enseignant.e", cons(MemberStatus.TEACHER).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiant.e master", cons(MemberStatus.MASTER_STUDENT).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("\u00C9tudiant.e", cons(MemberStatus.OTHER_STUDENT).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertEquals("Membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel((Gender) null, java.util.Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_US() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_FR() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_US() {
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(true, java.util.Locale.US));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(true, java.util.Locale.US));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(true, java.util.Locale.US));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(true, java.util.Locale.US));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(true, java.util.Locale.US));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(true, java.util.Locale.US));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(true, java.util.Locale.US));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(true, java.util.Locale.US));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(true, java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_FR() {
		assertEquals("Ancien.ne professeur.e des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne directeur.trice de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne chercheur.se", cons(MemberStatus.RESEARCHER_PHD).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne chercheur.se", cons(MemberStatus.RESEARCHER).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne postdoc", cons(MemberStatus.POSTDOC).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne doctorant.e", cons(MemberStatus.PHD_STUDENT).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e", cons(MemberStatus.ENGINEER_PHD).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e", cons(MemberStatus.ENGINEER).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien personnel administratif", cons(MemberStatus.ADMIN).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e", cons(MemberStatus.TEACHER_PHD).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e", cons(MemberStatus.TEACHER).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne étudiant.e master", cons(MemberStatus.MASTER_STUDENT).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne étudiant.e", cons(MemberStatus.OTHER_STUDENT).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(true, java.util.Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_US_FEMALE() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.FEMALE, true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE, true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.FEMALE, true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.FEMALE, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.FEMALE, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.FEMALE, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.FEMALE, true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.FEMALE, true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.FEMALE, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.FEMALE, true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.FEMALE, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.FEMALE, true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.FEMALE, true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.FEMALE, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.FEMALE, true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_FR_FEMALE() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.FEMALE, true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE, true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.FEMALE, true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.FEMALE, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.FEMALE, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.FEMALE, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.FEMALE, true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.FEMALE, true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.FEMALE, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.FEMALE, true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.FEMALE, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.FEMALE, true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.FEMALE, true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.FEMALE, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.FEMALE, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.FEMALE, true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_US_FEMALE() {
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.FEMALE, true, java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_FR_FEMALE() {
		assertEquals("Ancienne professeure des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne directrice de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne enseignante chercheuse contractuelle", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne enseignante chercheuse contractuelle", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne chercheuse", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne chercheuse", cons(MemberStatus.RESEARCHER).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne ingénieure de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne ingénieure de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne doctorante", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne ingénieure", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne ingénieure", cons(MemberStatus.ENGINEER).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien personnel administratif", cons(MemberStatus.ADMIN).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne enseignante", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne enseignante", cons(MemberStatus.TEACHER).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne étudiante master", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne étudiante", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne membre associée", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancienne membre associée", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.FEMALE, true, java.util.Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_US_MALE() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.MALE, true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.MALE, true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.MALE, true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.MALE, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.MALE, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.MALE, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.MALE, true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.MALE, true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.MALE, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.MALE, true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.MALE, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.MALE, true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.MALE, true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.MALE, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.MALE, true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_FR_MALE() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.MALE, true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.MALE, true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.MALE, true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.MALE, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.MALE, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.MALE, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.MALE, true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.MALE, true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.MALE, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.MALE, true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.MALE, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.MALE, true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.MALE, true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.MALE, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.MALE, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.MALE, true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_US_MALE() {
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.MALE, true, java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_FR_MALE() {
		assertEquals("Ancien professeur des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien directeur de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien enseignant chercheur contractuel", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien enseignant chercheur contractuel", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien chercheur", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien chercheur", cons(MemberStatus.RESEARCHER).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien ingénieur de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien ingénieur de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien doctorant", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien ingénieur", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien ingénieur", cons(MemberStatus.ENGINEER).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien personnel administratif", cons(MemberStatus.ADMIN).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien enseignant", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien enseignant", cons(MemberStatus.TEACHER).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien étudiant master", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien étudiant", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien membre associé", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertEquals("Ancien membre associé", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.MALE, true, java.util.Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_US_OTHER() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.OTHER, true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.OTHER, true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.OTHER, true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.OTHER, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.OTHER, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.OTHER, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.OTHER, true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.OTHER, true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.OTHER, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.OTHER, true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.OTHER, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.OTHER, true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.OTHER, true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.OTHER, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.OTHER, true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_FR_OTHER() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.OTHER, true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.OTHER, true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.OTHER, true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.OTHER, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.OTHER, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.OTHER, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.OTHER, true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.OTHER, true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.OTHER, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.OTHER, true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.OTHER, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.OTHER, true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.OTHER, true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.OTHER, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.OTHER, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.OTHER, true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_US_OTHER() {
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.OTHER, true, java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_FR_OTHER() {
		assertEquals("Ancien.ne professeur.e des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne directeur.trice de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne chercheur.se", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne chercheur.se", cons(MemberStatus.RESEARCHER).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne doctorant.e", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e", cons(MemberStatus.ENGINEER).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien personnel administratif", cons(MemberStatus.ADMIN).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e", cons(MemberStatus.TEACHER).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne étudiant.e master", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne étudiant.e", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.OTHER, true, java.util.Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_US_NOTSPECIFIED() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.NOT_SPECIFIED, true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_FR_NOTSPECIFIED() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.NOT_SPECIFIED, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.NOT_SPECIFIED, true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_US_NOTSPECIFIED() {
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_FR_NOTSPECIFIED() {
		assertEquals("Ancien.ne professeur.e des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne directeur.trice de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne chercheur.se", cons(MemberStatus.RESEARCHER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne chercheur.se", cons(MemberStatus.RESEARCHER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne postdoc", cons(MemberStatus.POSTDOC).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne doctorant.e", cons(MemberStatus.PHD_STUDENT).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e", cons(MemberStatus.ENGINEER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e", cons(MemberStatus.ENGINEER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien personnel administratif", cons(MemberStatus.ADMIN).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e", cons(MemberStatus.TEACHER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e", cons(MemberStatus.TEACHER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne étudiant.e master", cons(MemberStatus.MASTER_STUDENT).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne étudiant.e", cons(MemberStatus.OTHER_STUDENT).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(Gender.NOT_SPECIFIED, true, java.util.Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_US_NULLGENDER() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(null, true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(null, true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(null, true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(null, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(null, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(null, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(null, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(null, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(null, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(null, true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(null, true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(null, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(null, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(null, true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(null, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(null, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(null, true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(null, true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(null, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(null, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(null, true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_FR_NULLGENDER() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(null, true));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(null, true));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(null, true));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(null, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(null, true));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(null, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(null, true));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(null, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(null, true));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(null, true));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(null, true));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(null, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(null, true));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(null, true));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(null, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(null, true));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(null, true));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(null, true));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(null, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(null, true));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(null, true));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_US_NULLGENDER() {
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(null, true, java.util.Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(null, true, java.util.Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_FR_NULLGENDER() {
		assertEquals("Ancien.ne professeur.e des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne directeur.trice de recherche", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e chercheur.se contractuel.le", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne chercheur.se", cons(MemberStatus.RESEARCHER_PHD).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne chercheur.se", cons(MemberStatus.RESEARCHER).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne postdoc", cons(MemberStatus.POSTDOC).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne doctorant.e", cons(MemberStatus.PHD_STUDENT).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e", cons(MemberStatus.ENGINEER_PHD).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne ingénieur.e", cons(MemberStatus.ENGINEER).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien personnel administratif", cons(MemberStatus.ADMIN).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e", cons(MemberStatus.TEACHER_PHD).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne enseignant.e", cons(MemberStatus.TEACHER).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne étudiant.e master", cons(MemberStatus.MASTER_STUDENT).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne étudiant.e", cons(MemberStatus.OTHER_STUDENT).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(null, true, java.util.Locale.FRANCE));
		assertEquals("Ancien.ne membre associé.e", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(null, true, java.util.Locale.FRANCE));
		assertAllTreated();
	}

}
