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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link OrganizationAddress}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class OrganizationAddressTest {

	private OrganizationAddress test;

	@BeforeEach
	public void setUp() {
		this.test = new OrganizationAddress();
	}

	@Test
	public void getName() {
		assertNull(this.test.getName());
	}

	@Test
	public void setName() {
		assertNull(this.test.getName());

		this.test.setName("xyz");
		assertEquals("xyz", this.test.getName());

		this.test.setName(null);
		assertNull(this.test.getName());

		this.test.setName("abc");
		assertEquals("abc", this.test.getName());

		this.test.setName("");
		assertNull(this.test.getName());
	}

	@Test
	public void getComplement() {
		assertNull(this.test.getComplement());
	}

	@Test
	public void setComplement() {
		assertNull(this.test.getComplement());

		this.test.setComplement("xyz");
		assertEquals("xyz", this.test.getComplement());

		this.test.setComplement(null);
		assertNull(this.test.getComplement());

		this.test.setComplement("abc");
		assertEquals("abc", this.test.getComplement());

		this.test.setComplement("");
		assertNull(this.test.getComplement());
	}

	@Test
	public void getStreet() {
		assertNull(this.test.getStreet());
	}

	@Test
	public void setStreet() {
		assertNull(this.test.getStreet());

		this.test.setStreet("xyz");
		assertEquals("xyz", this.test.getStreet());

		this.test.setStreet(null);
		assertNull(this.test.getStreet());

		this.test.setStreet("abc");
		assertEquals("abc", this.test.getStreet());

		this.test.setStreet("");
		assertNull(this.test.getStreet());
	}

	@Test
	public void getZipCode() {
		assertNull(this.test.getZipCode());
	}

	@Test
	public void setZipCode() {
		assertNull(this.test.getZipCode());

		this.test.setZipCode("xyz");
		assertEquals("xyz", this.test.getZipCode());

		this.test.setZipCode(null);
		assertNull(this.test.getZipCode());

		this.test.setZipCode("abc");
		assertEquals("abc", this.test.getZipCode());

		this.test.setZipCode("");
		assertNull(this.test.getZipCode());
	}

	@Test
	public void getCity() {
		assertNull(this.test.getCity());
	}

	@Test
	public void setCity() {
		assertNull(this.test.getCity());

		this.test.setCity("xyz");
		assertEquals("xyz", this.test.getCity());

		this.test.setCity(null);
		assertNull(this.test.getCity());

		this.test.setCity("abc");
		assertEquals("abc", this.test.getCity());

		this.test.setCity("");
		assertNull(this.test.getCity());
	}

	@Test
	public void getMapCoordinates() {
		assertNull(this.test.getMapCoordinates());
	}

	@Test
	public void setMapCoordinates() {
		assertNull(this.test.getMapCoordinates());

		this.test.setMapCoordinates("xyz");
		assertEquals("xyz", this.test.getMapCoordinates());

		this.test.setMapCoordinates(null);
		assertNull(this.test.getMapCoordinates());

		this.test.setMapCoordinates("abc");
		assertEquals("abc", this.test.getMapCoordinates());

		this.test.setMapCoordinates("");
		assertNull(this.test.getMapCoordinates());
	}

	@Test
	public void getGoogleMapLink() {
		assertNull(this.test.getGoogleMapLink());
	}

	@Test
	public void setGoogleMapLink() {
		assertNull(this.test.getGoogleMapLink());

		this.test.setGoogleMapLink("xyz");
		assertEquals("xyz", this.test.getGoogleMapLink());

		this.test.setGoogleMapLink(null);
		assertNull(this.test.getGoogleMapLink());

		this.test.setGoogleMapLink("abc");
		assertEquals("abc", this.test.getGoogleMapLink());

		this.test.setGoogleMapLink("");
		assertNull(this.test.getMapCoordinates());
	}

	@Test
	public void getPathToBackgroundImage() {
		assertNull(this.test.getPathToBackgroundImage());
	}

	@Test
	public void setPathToBackgroundImage() {
		assertNull(this.test.getPathToBackgroundImage());

		this.test.setPathToBackgroundImage("xyz");
		assertEquals("xyz", this.test.getPathToBackgroundImage());

		this.test.setPathToBackgroundImage(null);
		assertNull(this.test.getPathToBackgroundImage());

		this.test.setPathToBackgroundImage("abc");
		assertEquals("abc", this.test.getPathToBackgroundImage());

		this.test.setPathToBackgroundImage("");
		assertNull(this.test.getPathToBackgroundImage());
	}

}
