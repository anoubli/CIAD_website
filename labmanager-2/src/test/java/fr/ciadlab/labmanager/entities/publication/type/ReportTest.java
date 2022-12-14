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

package fr.ciadlab.labmanager.entities.publication.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Report}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ReportTest {

	private Report test;

	@BeforeEach
	public void setUp() {
		this.test = new Report();
	}

	@Test
	public void isRanked() {
		assertFalse(this.test.isRanked());
	}

	@Test
	public void getAddress() {
		assertNull(this.test.getAddress());
	}

	@Test
	public void setAddress() {
		this.test.setAddress("xyz");
		assertEquals("xyz", this.test.getAddress());

		this.test.setAddress("");
		assertNull(this.test.getAddress());

		this.test.setAddress(null);
		assertNull(this.test.getAddress());
	}

	@Test
	public void getInstitution() {
		assertNull(this.test.getInstitution());
	}

	@Test
	public void setInstitution() {
		this.test.setInstitution("xyz");
		assertEquals("xyz", this.test.getInstitution());

		this.test.setInstitution("");
		assertNull(this.test.getInstitution());

		this.test.setInstitution(null);
		assertNull(this.test.getInstitution());
	}

	@Test
	public void getReportType() {
		assertNull(this.test.getReportType());
	}

	@Test
	public void setReportType() {
		this.test.setReportType("xyz");
		assertEquals("xyz", this.test.getReportType());

		this.test.setReportType("");
		assertNull(this.test.getReportType());

		this.test.setReportType(null);
		assertNull(this.test.getReportType());
	}

	@Test
	public void getReportNumber() {
		assertNull(this.test.getReportNumber());
	}

	@Test
	public void setReportNumber() {
		this.test.setReportNumber("xyz");
		assertEquals("xyz", this.test.getReportNumber());

		this.test.setReportNumber("");
		assertNull(this.test.getReportNumber());

		this.test.setReportNumber(null);
		assertNull(this.test.getReportNumber());
	}

}
