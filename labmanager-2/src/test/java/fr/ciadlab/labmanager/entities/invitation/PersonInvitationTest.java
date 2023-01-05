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

package fr.ciadlab.labmanager.entities.invitation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;

import fr.ciadlab.labmanager.entities.member.Person;
import org.arakhne.afc.util.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link PersonInvitation}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@SuppressWarnings("all")
public class PersonInvitationTest {

	private PersonInvitation test;

	@BeforeEach
	public void setUp() {
		this.test = new PersonInvitation();
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		assertEquals(0, this.test.getId());

		this.test.setId(-1234);
		assertEquals(-1234, this.test.getId());

		this.test.setId(0);
		assertEquals(0, this.test.getId());

		this.test.setId(4789);
		assertEquals(4789, this.test.getId());
	}

	@Test
	public void getType() {
		assertNull(this.test.getType());
	}

	@Test
	public void setType_PersonInvitationType() {
		assertNull(this.test.getType());

		this.test.setType(PersonInvitationType.OUTGOING_GUEST);
		assertSame(PersonInvitationType.OUTGOING_GUEST, this.test.getType());

		this.test.setType((PersonInvitationType) null);
		assertNull(this.test.getType());
	}

	@Test
	public void setType_String() {
		assertNull(this.test.getType());

		this.test.setType("incoming_guest_professor");
		assertSame(PersonInvitationType.INCOMING_GUEST_PROFESSOR, this.test.getType());

		this.test.setType((String) null);
		assertNull(this.test.getType());

		this.test.setType("outgoing_guest");
		assertSame(PersonInvitationType.OUTGOING_GUEST, this.test.getType());

		this.test.setType("");
		assertNull(this.test.getType());
	}

	@Test
	public void getStartDate() {
		assertNull(this.test.getStartDate());
	}

	@Test
	public void setStartDate() {
		assertNull(this.test.getStartDate());

		this.test.setStartDate(LocalDate.parse("2022-10-14"));
		assertEquals(LocalDate.parse("2022-10-14"), this.test.getStartDate());
	}

	@Test
	public void getEndDate() {
		assertNull(this.test.getEndDate());
	}

	@Test
	public void setEndDate() {
		assertNull(this.test.getEndDate());

		this.test.setEndDate(LocalDate.parse("2022-10-14"));
		assertEquals(LocalDate.parse("2022-10-14"), this.test.getEndDate());
	}

	@Test
	public void getUniversity() {
		assertNull(this.test.getUniversity());
	}

	@Test
	public void setUniversity() {
		assertNull(this.test.getUniversity());

		this.test.setUniversity("abc");
		assertEquals("abc", this.test.getUniversity());

		this.test.setUniversity(null);
		assertNull(this.test.getUniversity());

		this.test.setUniversity("xyz");
		assertEquals("xyz", this.test.getUniversity());

		this.test.setUniversity("");
		assertNull(this.test.getUniversity());
	}

	@Test
	public void getCountry() {
		assertSame(CountryCode.FRANCE, this.test.getCountry());
	}

	@Test
	public void setCountry_code() {
		this.test.setCountry(CountryCode.ALGERIA);
		assertSame(CountryCode.ALGERIA, this.test.getCountry());

		this.test.setCountry((CountryCode) null);
		assertSame(CountryCode.FRANCE, this.test.getCountry());
	}

	@Test
	public void setCountry_string() {
		this.test.setCountry("AlGERIA");
		assertSame(CountryCode.ALGERIA, this.test.getCountry());

		this.test.setCountry((String) null);
		assertSame(CountryCode.FRANCE, this.test.getCountry());
	}

	@Test
	public void getGuest() {
		assertNull(this.test.getGuest());
	}

	@Test
	public void setGuest() {
		assertNull(this.test.getGuest());

		Person p = mock(Person.class);
		this.test.setGuest(p);
		assertSame(p, this.test.getGuest());

		this.test.setGuest(null);
		assertNull(this.test.getGuest());
	}

	@Test
	public void getInviter() {
		assertNull(this.test.getInviter());
	}

	@Test
	public void setInviter() {
		assertNull(this.test.getInviter());

		Person p = mock(Person.class);
		this.test.setInviter(p);
		assertSame(p, this.test.getInviter());

		this.test.setInviter(null);
		assertNull(this.test.getInviter());
	}

	@Test
	public void getTitle() {
		assertNull(this.test.getTitle());
	}

	@Test
	public void setTitle() {
		assertNull(this.test.getTitle());

		this.test.setTitle("abc");
		assertEquals("abc", this.test.getTitle());

		this.test.setTitle(null);
		assertNull(this.test.getTitle());

		this.test.setTitle("xyz");
		assertEquals("xyz", this.test.getTitle());

		this.test.setTitle("");
		assertNull(this.test.getTitle());
	}

}
