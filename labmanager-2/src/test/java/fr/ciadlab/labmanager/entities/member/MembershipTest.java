/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
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

import java.sql.Date;
import java.time.LocalDate;

import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
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

	private Date now;

	private Date past;

	private Date future;

	private static Date date(int year, int month, int day) {
		final LocalDate ld = LocalDate.of(year, month, day);
		return Date.valueOf(ld.getYear() + "-" + ld.getMonthValue() + "-" + ld.getDayOfMonth());
	}

	@BeforeEach
	public void setUp() {
		final LocalDate ld = LocalDate.now();
		this.now = date(ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());
		this.past = date(ld.getYear() - 1, ld.getMonthValue(), ld.getDayOfMonth());
		this.future = date(ld.getYear() + 1, ld.getMonthValue(), ld.getDayOfMonth());
		//
		this.test = new Membership();
	}

	@Test
	public void getId() {
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
	public void getMemberSinceWhen() {
		assertNull(this.test.getMemberSinceWhen());
	}

	@Test
	public void setMemberSinceWhen() {
		assertNull(this.test.getMemberSinceWhen());
		final Date d0 = mock(Date.class);
		this.test.setMemberSinceWhen(d0);
		assertSame(d0, this.test.getMemberSinceWhen());
		this.test.setMemberSinceWhen(null);
		assertNull(this.test.getMemberSinceWhen());
	}

	@Test
	public void getMemberToWhen() {
		assertNull(this.test.getMemberToWhen());
	}

	@Test
	public void setMemberToWhen() {
		assertNull(this.test.getMemberToWhen());
		final Date d0 = mock(Date.class);
		this.test.setMemberToWhen(d0);
		assertSame(d0, this.test.getMemberToWhen());
		this.test.setMemberToWhen(null);
		assertNull(this.test.getMemberToWhen());
	}

	@Test
	public void getMemberStatus() {
		assertNull(this.test.getMemberStatus());
	}

	@Test
	public void setMemberStatus() {
		assertNull(this.test.getMemberStatus());
		final MemberStatus s0 = MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER;
		this.test.setMemberStatus(s0);
		assertSame(s0, this.test.getMemberStatus());
		this.test.setMemberStatus(null);
		assertNull(this.test.getMemberStatus());
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
		this.test.setMemberToWhen(date(2021, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
		//
		this.test.setMemberToWhen(date(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberToWhen(date(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt_noEnd() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setMemberSinceWhen(date(2021, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(date(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(date(2023, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setMemberSinceWhen(date(2021, 5, 5));
		this.test.setMemberToWhen(date(2021, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(date(2021, 5, 5));
		this.test.setMemberToWhen(date(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(date(2021, 5, 5));
		this.test.setMemberToWhen(date(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(date(2022, 5, 5));
		this.test.setMemberToWhen(date(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(date(2022, 5, 5));
		this.test.setMemberToWhen(date(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(date(2023, 5, 5));
		this.test.setMemberToWhen(date(2023, 5, 5));
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
		this.test.setMemberToWhen(date(2021, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(date(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(date(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(date(2022, 1, 1));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(date(2021, 12, 31));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	public void isActiveIn_noEnd() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setMemberSinceWhen(date(2021, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(date(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(date(2023, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(date(2022, 12, 31));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(date(2023, 01, 01));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	public void isActiveIn() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setMemberSinceWhen(date(2021, 5, 5));
		this.test.setMemberToWhen(date(2021, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(date(2021, 5, 5));
		this.test.setMemberToWhen(date(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(date(2021, 5, 5));
		this.test.setMemberToWhen(date(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(date(2022, 5, 5));
		this.test.setMemberToWhen(date(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(date(2022, 5, 5));
		this.test.setMemberToWhen(date(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(date(2023, 5, 5));
		this.test.setMemberToWhen(date(2023, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
	}

}