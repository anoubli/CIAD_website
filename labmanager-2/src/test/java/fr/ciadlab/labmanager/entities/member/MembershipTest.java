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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.utils.bap.FrenchBap;
import fr.ciadlab.labmanager.utils.cnu.CnuSection;
import fr.ciadlab.labmanager.utils.conrs.ConrsSection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Membership}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class MembershipTest {

	private Membership test;

	private LocalDate now;

	private LocalDate past;

	private LocalDate future;

	private List<MemberStatus> membershipStatusItems;

	@BeforeEach
	public void setUp() {
		this.membershipStatusItems = new ArrayList<>();
		this.membershipStatusItems.addAll(Arrays.asList(MemberStatus.values()));
		//
		final LocalDate ld = LocalDate.now();
		this.now = LocalDate.of(ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());
		this.past = LocalDate.of(ld.getYear() - 1, ld.getMonthValue(), ld.getDayOfMonth());
		this.future = LocalDate.of(ld.getYear() + 1, ld.getMonthValue(), ld.getDayOfMonth());
		//
		this.test = new Membership();
	}

	private MemberStatus cons(MemberStatus status) {
		assertTrue(this.membershipStatusItems.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.membershipStatusItems.isEmpty(), "Missing enumeration items: " + this.membershipStatusItems.toString());
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		assertEquals(0, this.test.getId());

		this.test.setId(456);
		assertEquals(456, this.test.getId());

		this.test.setId(0);
		assertEquals(0, this.test.getId());
	}

	@Test
	public void getPerson() {
		assertNull(this.test.getPerson());
	}

	@Test
	public void setPerson() {
		assertNull(this.test.getPerson());
		final Person p0 = mock(Person.class);
		this.test.setPerson(p0);
		assertSame(p0, this.test.getPerson());
		this.test.setPerson(null);
		assertNull(this.test.getPerson());
	}

	@Test
	public void getResearchOrganization() {
		assertNull(this.test.getResearchOrganization());
	}

	@Test
	public void setResearchOrganization() {
		assertNull(this.test.getResearchOrganization());
		final ResearchOrganization r0 = mock(ResearchOrganization.class);
		this.test.setResearchOrganization(r0);
		assertSame(r0, this.test.getResearchOrganization());
		this.test.setResearchOrganization(null);
		assertNull(this.test.getResearchOrganization());
	}

	@Test
	public void getOrganizationAddress() {
		assertNull(this.test.getOrganizationAddress());
	}

	@Test
	public void setOrganizationAddress_notNull_valid() {
		OrganizationAddress adr = mock(OrganizationAddress.class);
		ResearchOrganization orga = mock(ResearchOrganization.class);
		when(orga.getAddresses()).thenReturn(Collections.singleton(adr));
		this.test.setResearchOrganization(orga);

		this.test.setOrganizationAddress(adr);

		assertSame(adr, this.test.getOrganizationAddress());
	}

	@Test
	public void setOrganizationAddress_notNull_invalid0() {
		OrganizationAddress adr = mock(OrganizationAddress.class);
		ResearchOrganization orga = mock(ResearchOrganization.class);
		when(orga.getAddresses()).thenReturn(Collections.emptySet());
		this.test.setResearchOrganization(orga);

		this.test.setOrganizationAddress(adr);

		assertNull(this.test.getOrganizationAddress());
	}

	@Test
	public void setOrganizationAddress_notNull_invalid1() {
		OrganizationAddress adr = mock(OrganizationAddress.class);
		OrganizationAddress adr1 = mock(OrganizationAddress.class);
		ResearchOrganization orga = mock(ResearchOrganization.class);
		when(orga.getAddresses()).thenReturn(Collections.singleton(adr1));
		this.test.setResearchOrganization(orga);

		this.test.setOrganizationAddress(adr);

		assertNull(this.test.getOrganizationAddress());
	}

	@Test
	public void setOrganizationAddress_null() {
		this.test.setOrganizationAddress(null);
		assertNull(this.test.getOrganizationAddress());
	}

	@Test
	public void getMemberSinceWhen() {
		assertNull(this.test.getMemberSinceWhen());
	}

	@Test
	public void setMemberSinceWhen_LocalDate() {
		assertNull(this.test.getMemberSinceWhen());
		final LocalDate d0 = LocalDate.parse("2022-07-22");
		this.test.setMemberSinceWhen(d0);
		assertSame(d0, this.test.getMemberSinceWhen());
		this.test.setMemberSinceWhen((LocalDate) null);
		assertNull(this.test.getMemberSinceWhen());
	}

	@Test
	public void setMemberSinceWhen_string() {
		assertNull(this.test.getMemberSinceWhen());
		final LocalDate d0 = LocalDate.parse("2022-07-22");
		this.test.setMemberSinceWhen(d0);
		assertSame(d0, this.test.getMemberSinceWhen());
		this.test.setMemberSinceWhen((String) null);
		assertNull(this.test.getMemberSinceWhen());
	}

	@Test
	public void getMemberToWhen() {
		assertNull(this.test.getMemberToWhen());
	}

	@Test
	public void setMemberToWhen_LocalDate() {
		assertNull(this.test.getMemberToWhen());
		final LocalDate d0 = LocalDate.parse("2022-07-22");
		this.test.setMemberToWhen(d0);
		assertSame(d0, this.test.getMemberToWhen());
		this.test.setMemberToWhen((LocalDate) null);
		assertNull(this.test.getMemberToWhen());
	}

	@Test
	public void setMemberToWhen_string() {
		assertNull(this.test.getMemberToWhen());
		final LocalDate d0 = LocalDate.parse("2022-07-22");
		this.test.setMemberToWhen(d0);
		assertSame(d0, this.test.getMemberToWhen());
		this.test.setMemberToWhen((String) null);
		assertNull(this.test.getMemberToWhen());
	}

	@Test
	public void getMemberStatus() {
		assertNull(this.test.getMemberStatus());
	}

	@Test
	public void setMemberStatus_MemberStatus() {
		assertNull(this.test.getMemberStatus());
		final MemberStatus s0 = MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER;
		this.test.setMemberStatus(s0);
		assertSame(s0, this.test.getMemberStatus());
		this.test.setMemberStatus((MemberStatus) null);
		assertNull(this.test.getMemberStatus());
	}

	@Test
	public void setMemberStatus_String() {
		assertNull(this.test.getMemberStatus());
		final MemberStatus s0 = MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER;
		this.test.setMemberStatus(s0.name().toLowerCase());
		assertSame(s0, this.test.getMemberStatus());
		this.test.setMemberStatus((String) null);
		assertNull(this.test.getMemberStatus());
	}

	private void doIsPermanentPositionTest(MemberStatus status) {
		this.test.setMemberStatus(cons(status));
		//
		this.test.setPermanentPosition(true);
		assertEquals(status.isPermanentPositionAllowed(), this.test.isPermanentPosition());
		//
		this.test.setPermanentPosition(false);
		assertFalse(this.test.isPermanentPosition());
	}
	
	@Test
	public void isPermanentPosition() {
		assertFalse(this.test.isPermanentPosition());
		doIsPermanentPositionTest(MemberStatus.ADMIN);
		doIsPermanentPositionTest(MemberStatus.ASSOCIATE_PROFESSOR);
		doIsPermanentPositionTest(MemberStatus.ASSOCIATE_PROFESSOR_HDR);
		doIsPermanentPositionTest(MemberStatus.ASSOCIATED_MEMBER);
		doIsPermanentPositionTest(MemberStatus.ASSOCIATED_MEMBER_PHD);
		doIsPermanentPositionTest(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER);
		doIsPermanentPositionTest(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD);
		doIsPermanentPositionTest(MemberStatus.ENGINEER);
		doIsPermanentPositionTest(MemberStatus.ENGINEER_PHD);
		doIsPermanentPositionTest(MemberStatus.FULL_PROFESSOR);
		doIsPermanentPositionTest(MemberStatus.MASTER_STUDENT);
		doIsPermanentPositionTest(MemberStatus.OTHER_STUDENT);
		doIsPermanentPositionTest(MemberStatus.PHD_STUDENT);
		doIsPermanentPositionTest(MemberStatus.POSTDOC);
		doIsPermanentPositionTest(MemberStatus.RESEARCH_DIRECTOR);
		doIsPermanentPositionTest(MemberStatus.RESEARCH_ENGINEER);
		doIsPermanentPositionTest(MemberStatus.RESEARCH_ENGINEER_PHD);
		doIsPermanentPositionTest(MemberStatus.RESEARCHER);
		doIsPermanentPositionTest(MemberStatus.RESEARCHER_PHD);
		doIsPermanentPositionTest(MemberStatus.TEACHER);
		doIsPermanentPositionTest(MemberStatus.TEACHER_PHD);
		assertAllTreated();
	}

	@Test
	public void setPermanentPosition() {
		assertFalse(this.test.isPermanentPosition());

	}

	@Test
	public void getCnuSection() {
		assertNull(this.test.getCnuSection());
	}

	@Test
	public void setCnuSection() {
		assertNull(this.test.getCnuSection());
		this.test.setCnuSection(61);
		assertSame(CnuSection.CNU_61, this.test.getCnuSection());
		this.test.setCnuSection(0);
		assertNull(this.test.getCnuSection());
		this.test.setCnuSection(-1);
		assertNull(this.test.getCnuSection());
	}

	@Test
	public void getConrsSection() {
		assertNull(this.test.getConrsSection());
	}

	@Test
	public void setConrsSection() {
		assertNull(this.test.getConrsSection());
		this.test.setConrsSection(31);
		assertSame(ConrsSection.CONRS_31, this.test.getConrsSection());
		this.test.setConrsSection(0);
		assertNull(this.test.getConrsSection());
		this.test.setConrsSection(-1);
		assertNull(this.test.getConrsSection());
	}

	@Test
	public void getFrenchBap() {
		assertNull(this.test.getFrenchBap());
	}

	@Test
	public void setFrenchBap() {
		assertNull(this.test.getFrenchBap());
		this.test.setFrenchBap("bap_b");
		assertSame(FrenchBap.BAP_B, this.test.getFrenchBap());
		this.test.setFrenchBap("c");
		assertSame(FrenchBap.BAP_C, this.test.getFrenchBap());
		this.test.setFrenchBap((FrenchBap) null);
		assertNull(this.test.getFrenchBap());
	}

	@Test
	public void isActive_noStart_noEnd() {
		assertTrue(this.test.isActive());
	}

	@Test
	public void isActive_noStart() {
		this.test.setMemberToWhen(this.past);
		assertFalse(this.test.isActive());
		//
		this.test.setMemberToWhen(this.now);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberToWhen(this.future);
		assertTrue(this.test.isActive());
	}

	@Test
	public void isActive_noEnd() {
		this.test.setMemberSinceWhen(this.past);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.now);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.future);
		assertFalse(this.test.isActive());
	}

	@Test
	public void isActive() {
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.past);
		assertFalse(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.now);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.future);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.now);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.future);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.future);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isActive());
	}

	@Test
	public void isFormer_noStart_noEnd() {
		assertFalse(this.test.isFormer());
	}

	@Test
	public void isFormer_noStart() {
		this.test.setMemberToWhen(this.past);
		assertTrue(this.test.isFormer());
		//
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFormer());
	}

	@Test
	public void isFormer_noEnd() {
		this.test.setMemberSinceWhen(this.past);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.now);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.future);
		assertFalse(this.test.isFormer());
	}

	@Test
	public void isFormer() {
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.past);
		assertTrue(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.future);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFormer());
	}

	@Test
	public void isFuture_noStart_noEnd() {
		assertFalse(this.test.isFuture());
	}

	@Test
	public void isFuture_noStart() {
		this.test.setMemberToWhen(this.past);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFuture());
	}

	@Test
	public void isFuture_noEnd() {
		this.test.setMemberSinceWhen(this.past);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.now);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.future);
		assertTrue(this.test.isFuture());
	}

	@Test
	public void isFuture() {
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.past);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.future);
		this.test.setMemberToWhen(this.future);
		assertTrue(this.test.isFuture());
	}

	@Test
	public void isActiveAt_noStart_noEnd() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		assertTrue(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt_noStart() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt_noEnd() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
	}


	@Test
	public void isActiveIn_noStart_noEnd() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	public void isActiveIn_noStart() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 1, 1));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 12, 31));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	public void isActiveIn_noEnd() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 12, 31));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 01, 01));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	public void isActiveIn() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
	}


	@Test
	public void isMainPosition() {
		assertTrue(this.test.isMainPosition());
	}

	@Test
	public void setMainPosition() {
		assertTrue(this.test.isMainPosition());
		this.test.setMainPosition(false);
		assertFalse(this.test.isMainPosition());
		this.test.setMainPosition(true);
		assertTrue(this.test.isMainPosition());
	}

	@Test
	public void setMainPosition_Boolean() {
		assertTrue(this.test.isMainPosition());
		this.test.setMainPosition(Boolean.FALSE);
		assertFalse(this.test.isMainPosition());
		this.test.setMainPosition(Boolean.TRUE);
		assertTrue(this.test.isMainPosition());
		this.test.setMainPosition(Boolean.FALSE);
		assertFalse(this.test.isMainPosition());
		this.test.setMainPosition((Boolean) null);
		assertTrue(this.test.isMainPosition());
	}

	@Test
	public void getResponsibility() {
		assertNull(this.test.getResponsibility());
	}

	@Test
	public void setResponsibility_Responsibility() {
		assertNull(this.test.getResponsibility());
		final Responsibility r0 = Responsibility.COMMUNICATION_VICE_PRESIDENT;
		this.test.setResponsibility(r0);
		assertSame(r0, this.test.getResponsibility());
		this.test.setResponsibility((Responsibility) null);
		assertNull(this.test.getResponsibility());
	}

	@Test
	public void setResponsibility_String() {
		assertNull(this.test.getResponsibility());
		final Responsibility r0 = Responsibility.BUSINESS_UNIT_RESPONSIBLE;
		this.test.setResponsibility(r0.name().toLowerCase());
		assertSame(r0, this.test.getResponsibility());
		this.test.setResponsibility((String) null);
		assertNull(this.test.getResponsibility());
	}

}
