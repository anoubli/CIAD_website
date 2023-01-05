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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.ciadlab.labmanager.entities.member.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link SupervisorType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */@SuppressWarnings("all")
 public class SupervisorTypeTest {

	 private List<SupervisorType> items;

	 @BeforeEach
	 public void setUp() {
		 this.items = new ArrayList<>();
		 this.items.addAll(Arrays.asList(SupervisorType.values()));
	 }

	 private SupervisorType cons(SupervisorType status) {
		 assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		 return status;
	 }

	 private void assertAllTreated() {
		 assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	 }

	 @Test
	 public void getLabel_FEMALE_FR() throws Exception {
		 java.util.Locale.setDefault(java.util.Locale.FRANCE);
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.FEMALE));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.FEMALE));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.FEMALE));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_MALE_FR() throws Exception {
		 java.util.Locale.setDefault(java.util.Locale.FRANCE);
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.MALE));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.MALE));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.MALE));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_OTHER_FR() throws Exception {
		 java.util.Locale.setDefault(java.util.Locale.FRANCE);
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.OTHER));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.OTHER));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.OTHER));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_NOTSPECIFIED_FR() throws Exception {
		 java.util.Locale.setDefault(java.util.Locale.FRANCE);
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.NOT_SPECIFIED));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.NOT_SPECIFIED));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.NOT_SPECIFIED));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_FEMALE_US() throws Exception {
		 java.util.Locale.setDefault(java.util.Locale.US);
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.FEMALE));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.FEMALE));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.FEMALE));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_MALE_US() throws Exception {
		 java.util.Locale.setDefault(java.util.Locale.US);
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.MALE));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.MALE));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.MALE));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_OTHER_US() throws Exception {
		 java.util.Locale.setDefault(java.util.Locale.US);
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.OTHER));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.OTHER));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.OTHER));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_NOTSPECIFIED_US() throws Exception {
		 java.util.Locale.setDefault(java.util.Locale.US);
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.NOT_SPECIFIED));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.NOT_SPECIFIED));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.NOT_SPECIFIED));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_Locale_FEMALE_FR() throws Exception {
		 assertEquals("Directrice", cons(SupervisorType.DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		 assertEquals("Encadrante", cons(SupervisorType.SUPERVISOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		 assertEquals("Membre du comité de suivi", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.FEMALE, Locale.FRANCE));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_Locale_MALE_FR() throws Exception {
		 assertEquals("Directeur", cons(SupervisorType.DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		 assertEquals("Encadrant", cons(SupervisorType.SUPERVISOR).getLabel(Gender.MALE, Locale.FRANCE));
		 assertEquals("Membre du comité de suivi", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.MALE, Locale.FRANCE));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_Locale_OTHER_FR() throws Exception {
		 assertEquals("Directeur.trice", cons(SupervisorType.DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		 assertEquals("Encadrant.e", cons(SupervisorType.SUPERVISOR).getLabel(Gender.OTHER, Locale.FRANCE));
		 assertEquals("Membre du comité de suivi", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.OTHER, Locale.FRANCE));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_Locale_NOTSPECIFIED_FR() throws Exception {
		 assertEquals("Directeur.trice", cons(SupervisorType.DIRECTOR).getLabel(Gender.NOT_SPECIFIED, Locale.FRANCE));
		 assertEquals("Encadrant.e", cons(SupervisorType.SUPERVISOR).getLabel(Gender.NOT_SPECIFIED, Locale.FRANCE));
		 assertEquals("Membre du comité de suivi", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.NOT_SPECIFIED, Locale.FRANCE));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_Locale_FEMALE_US() throws Exception {
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.FEMALE, Locale.US));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.FEMALE, Locale.US));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_Locale_MALE_US() throws Exception {
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.MALE, Locale.US));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.MALE, Locale.US));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.MALE, Locale.US));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_Locale_OTHER_US() throws Exception {
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.OTHER, Locale.US));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.OTHER, Locale.US));
		 assertAllTreated();
	 }

	 @Test
	 public void getLabel_Locale_NOTSPECIFIED_US() throws Exception {
		 assertEquals("Director", cons(SupervisorType.DIRECTOR).getLabel(Gender.NOT_SPECIFIED, Locale.US));
		 assertEquals("Supervisor", cons(SupervisorType.SUPERVISOR).getLabel(Gender.NOT_SPECIFIED, Locale.US));
		 assertEquals("Monitoring Committee Member", cons(SupervisorType.COMMITTEE_MEMBER).getLabel(Gender.NOT_SPECIFIED, Locale.US));
		 assertAllTreated();
	 }

 }