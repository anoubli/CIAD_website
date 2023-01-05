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
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Responsibility}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ResponsibilityTest {

	private List<Responsibility> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(Responsibility.values()));
	}

	private Responsibility cons(Responsibility type) {
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
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(Gender.MALE));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.MALE));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.MALE));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_US_FEMALE() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(Gender.FEMALE));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.FEMALE));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.FEMALE));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_US_OTHER() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(Gender.OTHER));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.OTHER));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.OTHER));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_MALE() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(Gender.MALE));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.MALE));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.MALE));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_FEMALE() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(Gender.FEMALE));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.FEMALE));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.FEMALE));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_OTHER() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(Gender.OTHER));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.OTHER));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.OTHER));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_MALE() {
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(Gender.MALE, Locale.US));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.MALE, Locale.US));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_FEMALE() {
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_OTHER() {
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_MALE() {
		assertEquals("Vice-président du Conseil d'Administration", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Président du Conseil d'Administration en Formation Restreinte", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Membre du Conseil d'Administration", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable de la valorisation", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur de la communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable de la communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président à la communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Doyen", cons(Responsibility.DEAN).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur de département", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur adjoint", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Secrétaire de direction", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur", cons(Responsibility.DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur exécutif", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Doyen de Faculté", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur de Faculté", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable à l'égalité des genres", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable des ressources humaines", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable au numérique", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Membre du Conseil au numérique", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur au numérique", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président au numérique", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable des activités de Science Ouverte", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Membre du Conseil de la Pédagogie", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président du Conseil de la Pédagogie", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur de la pédagogie", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Président", cons(Responsibility.PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur à la recherche et à la valorisation", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président à la recherche et à la valorisation", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Membre du Conseil Scientifique", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président du Conseil Scientifique", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable des conférences et des séminaires", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable scientifique d'une équipe", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable d'un axe scientifique", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable d'un axe applicatif", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable d'une plateforme technologique", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable d'un axe transversal", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur de la formation en informatique d'ingénieur sous statut étudiant", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur de la formation en informatique d'ingénieur sous statut apprenti", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur du Pôle \u00C9nergie et Informatique", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur du Pôle Transport et Mobilité", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur aux relations internationales", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Membre du Conseil de Laboratoire", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_FEMALE() {
		assertEquals("Vice-présidente du Conseil d'Administration", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Présidente du Conseil d'Administration en Formation Restreinte", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Membre du Conseil d'Administration", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable de la valorisation", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice de la communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable de la communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente à la communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Doyenne", cons(Responsibility.DEAN).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice de département", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice adjointe", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Secrétaire de direction", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice", cons(Responsibility.DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice exécutive", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Doyenne de Faculté", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice de Faculté", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable à l'égalité des genres", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable des ressources humaines", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable au numérique", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Membre du Conseil au numérique", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice au numérique", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente au numérique", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable des activités de Science Ouverte", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Membre du Conseil de la Pédagogie", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente du Conseil de la Pédagogie", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice de la pédagogie", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Présidente", cons(Responsibility.PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice à la recherche et à la valorisation", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente à la recherche et à la valorisation", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Membre du Conseil Scientifique", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente du Conseil Scientifique", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable des conférences et des séminaires", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable scientifique d'une équipe", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable d'un axe scientifique", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable d'un axe applicatif", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable d'une plateforme technologique", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable d'un axe transversal", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice de la formation en informatique d'ingénieur sous statut étudiant", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice de la formation en informatique d'ingénieur sous statut apprenti", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice du Pôle \u00C9nergie et Informatique", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice du Pôle Transport et Mobilité", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice aux relations internationales", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Membre du Conseil de Laboratoire", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_OTHER() {
		assertEquals("Vice-président.e du Conseil d'Administration", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Président.e du Conseil d'Administration en Formation Restreinte", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Membre du Conseil d'Administration", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable de la valorisation", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice de la communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable de la communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e à la communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Doyen.ne", cons(Responsibility.DEAN).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice de département", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice adjoint.e", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Secrétaire de direction", cons(Responsibility.DIRECTION_SECRETARY).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice", cons(Responsibility.DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice exécutif.ve", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Doyen.ne de Faculté", cons(Responsibility.FACULTY_DEAN).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice de Faculté", cons(Responsibility.FACULTY_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable à l'égalité des genres", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable des ressources humaines", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable au numérique", cons(Responsibility.IT_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Membre du Conseil au numérique", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice au numérique", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e au numérique", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable des activités de Science Ouverte", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Membre du Conseil de la Pédagogie", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e du Conseil de la Pédagogie", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice de la pédagogie", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Président.e", cons(Responsibility.PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice à la recherche et à la valorisation", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e à la recherche et à la valorisation", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Membre du Conseil Scientifique", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e du Conseil Scientifique", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable des conférences et des séminaires", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e", cons(Responsibility.VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable scientifique d'une équipe", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable d'un axe scientifique", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable d'un axe applicatif", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable d'une plateforme technologique", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable d'un axe transversal", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice de la formation en informatique d'ingénieur sous statut étudiant", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice de la formation en informatique d'ingénieur sous statut apprenti", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice du Pôle \u00C9nergie et Informatique", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice du Pôle Transport et Mobilité", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice aux relations internationales", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Membre du Conseil de Laboratoire", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.FRANCE));
		assertAllTreated();
	}

}
