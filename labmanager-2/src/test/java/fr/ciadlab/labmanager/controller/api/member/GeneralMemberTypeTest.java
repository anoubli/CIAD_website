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

package fr.ciadlab.labmanager.controller.api.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Responsibility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link GeneralMemberType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class GeneralMemberTypeTest {

	private static final Random RANDOM = new Random();

	private List<GeneralMemberType> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(GeneralMemberType.values()));
	}

	private GeneralMemberType cons(GeneralMemberType type) {
		assertTrue(this.items.remove(type), "Expecting enumeration item: " + type.toString());
		return type;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getLabel_US() {
		// Force the default locale to be US
		Locale.setDefault(Locale.US);
		assertEquals("Associated external researchers", cons(GeneralMemberType.ASSOCIATED_MEMBERS).getLabel());
		assertEquals("Former members", cons(GeneralMemberType.FORMER_MEMBERS).getLabel());
		assertEquals("Master students", cons(GeneralMemberType.MASTER_STUDENTS).getLabel());
		assertEquals("Other members", cons(GeneralMemberType.OTHER_MEMBERS).getLabel());
		assertEquals("PhD students", cons(GeneralMemberType.PHDS).getLabel());
		assertEquals("Post-Docs", cons(GeneralMemberType.POSTDOCS).getLabel());
		assertEquals("Researchers", cons(GeneralMemberType.RESEARCHERS).getLabel());
		assertEquals("Direction", cons(GeneralMemberType.DIRECTION).getLabel());
		assertEquals("Engineers", cons(GeneralMemberType.ENGINEERS).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_FR() {
		// Force the default locale to be FR
		Locale.setDefault(Locale.FRANCE);
		assertEquals("Associated external researchers", cons(GeneralMemberType.ASSOCIATED_MEMBERS).getLabel());
		assertEquals("Former members", cons(GeneralMemberType.FORMER_MEMBERS).getLabel());
		assertEquals("Master students", cons(GeneralMemberType.MASTER_STUDENTS).getLabel());
		assertEquals("Other members", cons(GeneralMemberType.OTHER_MEMBERS).getLabel());
		assertEquals("PhD students", cons(GeneralMemberType.PHDS).getLabel());
		assertEquals("Post-Docs", cons(GeneralMemberType.POSTDOCS).getLabel());
		assertEquals("Researchers", cons(GeneralMemberType.RESEARCHERS).getLabel());
		assertEquals("Direction", cons(GeneralMemberType.DIRECTION).getLabel());
		assertEquals("Engineers", cons(GeneralMemberType.ENGINEERS).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() {
		assertEquals("Associated external researchers", cons(GeneralMemberType.ASSOCIATED_MEMBERS).getLabel(Locale.US));
		assertEquals("Former members", cons(GeneralMemberType.FORMER_MEMBERS).getLabel(Locale.US));
		assertEquals("Master students", cons(GeneralMemberType.MASTER_STUDENTS).getLabel(Locale.US));
		assertEquals("Other members", cons(GeneralMemberType.OTHER_MEMBERS).getLabel(Locale.US));
		assertEquals("PhD students", cons(GeneralMemberType.PHDS).getLabel(Locale.US));
		assertEquals("Post-Docs", cons(GeneralMemberType.POSTDOCS).getLabel(Locale.US));
		assertEquals("Researchers", cons(GeneralMemberType.RESEARCHERS).getLabel(Locale.US));
		assertEquals("Direction", cons(GeneralMemberType.DIRECTION).getLabel(Locale.US));
		assertEquals("Engineers", cons(GeneralMemberType.ENGINEERS).getLabel(Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() {
		assertEquals("Chercheurs externes associés", cons(GeneralMemberType.ASSOCIATED_MEMBERS).getLabel(Locale.FRANCE));
		assertEquals("Anciens membres", cons(GeneralMemberType.FORMER_MEMBERS).getLabel(Locale.FRANCE));
		assertEquals("\u00C9tudiants Master", cons(GeneralMemberType.MASTER_STUDENTS).getLabel(Locale.FRANCE));
		assertEquals("Autres membres", cons(GeneralMemberType.OTHER_MEMBERS).getLabel(Locale.FRANCE));
		assertEquals("Doctorants", cons(GeneralMemberType.PHDS).getLabel(Locale.FRANCE));
		assertEquals("Post-Docs", cons(GeneralMemberType.POSTDOCS).getLabel(Locale.FRANCE));
		assertEquals("Chercheurs", cons(GeneralMemberType.RESEARCHERS).getLabel(Locale.FRANCE));
		assertEquals("Direction", cons(GeneralMemberType.DIRECTION).getLabel(Locale.FRANCE));
		assertEquals("Ingénieurs", cons(GeneralMemberType.ENGINEERS).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void fromMembership_0() {
		Membership mbr = mock(Membership.class);
		when(mbr.isFormer()).thenReturn(Boolean.FALSE);
		when(mbr.isActive()).thenReturn(Boolean.TRUE);
		when(mbr.isFuture()).thenReturn(Boolean.FALSE);
		when(mbr.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATED_MEMBER);
		GeneralMemberType type = GeneralMemberType.fromMembership(mbr);
		assertSame(GeneralMemberType.ASSOCIATED_MEMBERS, type);
	}

	@Test
	public void fromMembership_1() {
		Membership mbr = mock(Membership.class);
		when(mbr.isFormer()).thenReturn(Boolean.TRUE);
		when(mbr.isActive()).thenReturn(Boolean.FALSE);
		when(mbr.isFuture()).thenReturn(Boolean.FALSE);
		when(mbr.getMemberStatus()).thenReturn(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER);
		GeneralMemberType type = GeneralMemberType.fromMembership(mbr);
		assertSame(GeneralMemberType.FORMER_MEMBERS, type);
	}

	@Test
	public void fromMembership_2() {
		Membership mbr = mock(Membership.class);
		when(mbr.isFormer()).thenReturn(Boolean.FALSE);
		when(mbr.isActive()).thenReturn(Boolean.TRUE);
		when(mbr.isFuture()).thenReturn(Boolean.FALSE);
		when(mbr.getMemberStatus()).thenReturn(MemberStatus.MASTER_STUDENT);
		GeneralMemberType type = GeneralMemberType.fromMembership(mbr);
		assertSame(GeneralMemberType.MASTER_STUDENTS, type);
	}

	@Test
	public void fromMembership_3() {
		Membership mbr = mock(Membership.class);
		when(mbr.isFormer()).thenReturn(Boolean.FALSE);
		when(mbr.isActive()).thenReturn(Boolean.TRUE);
		when(mbr.isFuture()).thenReturn(Boolean.FALSE);
		when(mbr.getMemberStatus()).thenReturn(MemberStatus.RESEARCH_ENGINEER);
		GeneralMemberType type = GeneralMemberType.fromMembership(mbr);
		assertSame(GeneralMemberType.ENGINEERS, type);
	}

	@Test
	public void fromMembership_4() {
		Membership mbr = mock(Membership.class);
		when(mbr.isFormer()).thenReturn(Boolean.FALSE);
		when(mbr.isActive()).thenReturn(Boolean.TRUE);
		when(mbr.isFuture()).thenReturn(Boolean.FALSE);
		when(mbr.getMemberStatus()).thenReturn(MemberStatus.PHD_STUDENT);
		GeneralMemberType type = GeneralMemberType.fromMembership(mbr);
		assertSame(GeneralMemberType.PHDS, type);
	}

	@Test
	public void fromMembership_5() {
		Membership mbr = mock(Membership.class);
		when(mbr.isFormer()).thenReturn(Boolean.FALSE);
		when(mbr.isActive()).thenReturn(Boolean.TRUE);
		when(mbr.isFuture()).thenReturn(Boolean.FALSE);
		when(mbr.getMemberStatus()).thenReturn(MemberStatus.POSTDOC);
		GeneralMemberType type = GeneralMemberType.fromMembership(mbr);
		assertSame(GeneralMemberType.POSTDOCS, type);
	}

	@Test
	public void fromMembership_6() {
		Membership mbr = mock(Membership.class);
		when(mbr.isFormer()).thenReturn(Boolean.FALSE);
		when(mbr.isActive()).thenReturn(Boolean.TRUE);
		when(mbr.isFuture()).thenReturn(Boolean.FALSE);
		when(mbr.getMemberStatus()).thenReturn(MemberStatus.FULL_PROFESSOR);
		GeneralMemberType type = GeneralMemberType.fromMembership(mbr);
		assertSame(GeneralMemberType.RESEARCHERS, type);
	}

	@Test
	public void fromMembership_7() {
		Membership mbr = mock(Membership.class);
		when(mbr.isFormer()).thenReturn(Boolean.FALSE);
		when(mbr.isActive()).thenReturn(Boolean.FALSE);
		when(mbr.isFuture()).thenReturn(Boolean.TRUE);
		when(mbr.getMemberStatus()).thenReturn(MemberStatus.FULL_PROFESSOR);
		GeneralMemberType type = GeneralMemberType.fromMembership(mbr);
		assertNull(type);
	}

}
