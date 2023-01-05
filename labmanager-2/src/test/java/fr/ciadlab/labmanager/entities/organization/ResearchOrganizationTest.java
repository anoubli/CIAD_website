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

package fr.ciadlab.labmanager.entities.organization;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import fr.ciadlab.labmanager.entities.member.Membership;
import org.arakhne.afc.util.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ResearchOrganization}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ResearchOrganizationTest {

	private ResearchOrganization test;

	@BeforeEach
	public void setUp() {
		this.test = new ResearchOrganization();
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
	public void getMemberships() {
		assertTrue(this.test.getMemberships().isEmpty());
	}

	@Test
	public void setMemberships() {
		Set<Membership> members = new HashSet<>();
		members.add(mock(Membership.class));
		members.add(mock(Membership.class));
		members.add(mock(Membership.class));
		//
		this.test.setMemberships(members);
		assertSame(members, this.test.getMemberships());
		//
		this.test.setMemberships(null);
		assertTrue(this.test.getMemberships().isEmpty());
	}

	@Test
	public void getSubOrganizations() {
		assertTrue(this.test.getSubOrganizations().isEmpty());
	}

	@Test
	public void setSubOrganizations() {
		Set<ResearchOrganization> orgas = new HashSet<>();
		orgas.add(mock(ResearchOrganization.class));
		orgas.add(mock(ResearchOrganization.class));
		orgas.add(mock(ResearchOrganization.class));
		//
		this.test.setSubOrganizations(orgas);
		assertSame(orgas, this.test.getSubOrganizations());
		//
		this.test.setSubOrganizations(null);
		assertTrue(this.test.getSubOrganizations().isEmpty());
	}

	@Test
	public void getSuperOrganization() {
		assertNull(this.test.getSuperOrganization());
	}

	@Test
	public void setSuperOrganization() {
		ResearchOrganization org = mock(ResearchOrganization.class);
		//
		this.test.setSuperOrganization(org);
		assertSame(org, this.test.getSuperOrganization());
		//
		this.test.setSuperOrganization(null);
		assertNull(this.test.getSuperOrganization());
	}

	@Test
	public void getAcronym() {
		assertNull(this.test.getAcronym());
	}

	@Test
	public void setAcronym() {
		this.test.setAcronym("xyz");
		assertEquals("xyz", this.test.getAcronym());

		this.test.setAcronym("");
		assertNull(this.test.getAcronym());

		this.test.setAcronym(null);
		assertNull(this.test.getAcronym());
	}

	@Test
	public void getName() {
		assertNull(this.test.getName());
	}

	@Test
	public void setName() {
		this.test.setName("xyz");
		assertEquals("xyz", this.test.getName());

		this.test.setName("");
		assertNull(this.test.getName());

		this.test.setName("abc");
		assertEquals("abc", this.test.getName());

		this.test.setName(null);
		assertNull(this.test.getName());
	}

	@Test
	public void getNationalIdentifier() {
		assertNull(this.test.getNationalIdentifier());
	}

	@Test
	public void setNationalIdentifier() {
		this.test.setNationalIdentifier("xyz");
		assertEquals("xyz", this.test.getNationalIdentifier());

		this.test.setNationalIdentifier("");
		assertNull(this.test.getNationalIdentifier());

		this.test.setNationalIdentifier("abc");
		assertEquals("abc", this.test.getNationalIdentifier());

		this.test.setNationalIdentifier(null);
		assertNull(this.test.getNationalIdentifier());
	}

	@Test
	public void getDescription() {
		assertNull(this.test.getDescription());
	}

	@Test
	public void setDescription() {
		this.test.setDescription("xyz");
		assertEquals("xyz", this.test.getDescription());

		this.test.setDescription("");
		assertNull(this.test.getDescription());

		this.test.setDescription(null);
		assertNull(this.test.getDescription());
	}

	@Test
	public void isMajorOrganization() {
		assertFalse(this.test.isMajorOrganization());
	}

	@Test
	public void setMajorOrganization() {
		this.test.setMajorOrganization(true);
		assertTrue(this.test.isMajorOrganization());

		this.test.setMajorOrganization(false);
		assertFalse(this.test.isMajorOrganization());
	}

	@Test
	public void getRnsr() {
		assertNull(this.test.getRnsr());
	}

	@Test
	public void setRnsr() {
		this.test.setRnsr("xyz");
		assertEquals("xyz", this.test.getRnsr());

		this.test.setRnsr("");
		assertNull(this.test.getRnsr());

		this.test.setRnsr("abc");
		assertEquals("abc", this.test.getRnsr());

		this.test.setRnsr(null);
		assertNull(this.test.getRnsr());
	}

	@Test
	public void getRnsrUrl() {
		assertNull(this.test.getRnsrUrl());
	
		this.test.setRnsr("xyz");
		assertEquals("https://appliweb.dgri.education.fr/rnsr/PresenteStruct.jsp?PUBLIC=OK&numNatStruct=xyz", this.test.getRnsrUrl().toExternalForm());
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
	public void getOrganizationURL() {
		assertNull(this.test.getOrganizationURL());
	}

	@Test
	public void getOrganizationURLObject() {
		assertNull(this.test.getOrganizationURLObject());
	}

	@Test
	public void setOrganizationURL_string() throws Exception {
		this.test.setOrganizationURL("http://xyz.org");
		assertEquals("http://xyz.org", this.test.getOrganizationURL());
		assertEquals(new URL("http://xyz.org"), this.test.getOrganizationURLObject());

		this.test.setOrganizationURL("");
		assertNull(this.test.getOrganizationURL());
		assertNull(this.test.getOrganizationURLObject());

		this.test.setOrganizationURL((String) null);
		assertNull(this.test.getOrganizationURL());
		assertNull(this.test.getOrganizationURLObject());
	}

	@Test
	public void setOrganizationURL_url() throws Exception {
		this.test.setOrganizationURL(new URL("http://xyz.org"));
		assertEquals("http://xyz.org", this.test.getOrganizationURL());
		assertEquals(new URL("http://xyz.org"), this.test.getOrganizationURLObject());

		this.test.setOrganizationURL((URL) null);
		assertNull(this.test.getOrganizationURL());
		assertNull(this.test.getOrganizationURLObject());
	}

	@Test
	public void getType() {
		assertSame(ResearchOrganizationType.LABORATORY, this.test.getType());
	}

	@Test
	public void setType_type() {
		this.test.setType(ResearchOrganizationType.COMMUNITY);
		assertSame(ResearchOrganizationType.COMMUNITY, this.test.getType());

		this.test.setType((ResearchOrganizationType) null);
		assertSame(ResearchOrganizationType.LABORATORY, this.test.getType());
	}

	@Test
	public void setType_string() {
		this.test.setType("faCulty");
		assertSame(ResearchOrganizationType.FACULTY, this.test.getType());

		this.test.setType((String) null);
		assertSame(ResearchOrganizationType.LABORATORY, this.test.getType());
	}

	@Test
	public void getAddresses() {
		assertTrue(this.test.getAddresses().isEmpty());
	}

	@Test
	public void setAddresses() {
		OrganizationAddress a0 = mock(OrganizationAddress.class);
		OrganizationAddress a1 = mock(OrganizationAddress.class);
		Set<OrganizationAddress> addresses = new TreeSet<>();
		addresses.add(a0);
		addresses.add(a1);

		this.test.setAddresses(addresses);
		assertNotSame(addresses, this.test.getAddresses());
		assertEquals(2, this.test.getAddresses().size());
		assertTrue(this.test.getAddresses().contains(a0));
		assertTrue(this.test.getAddresses().contains(a1));
	}

	@Test
	public void setAddresses_null() {
		// Prepare the set of addresses to be not empty.
		OrganizationAddress a0 = mock(OrganizationAddress.class);
		OrganizationAddress a1 = mock(OrganizationAddress.class);
		Set<OrganizationAddress> addresses = new TreeSet<>();
		addresses.add(a0);
		addresses.add(a1);
		this.test.setAddresses(addresses);

		this.test.setAddresses(null);
		
		assertTrue(this.test.getAddresses().isEmpty());
	}

}
