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

package fr.ciadlab.labmanager.entities.journal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Journal}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class JournalTest {

	private Journal test;

	@BeforeEach
	public void setUp() {
		this.test = new Journal();
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		assertEquals(0, this.test.getId());

		this.test.setId(1478);
		assertEquals(1478, this.test.getId());

		this.test.setId(0);
		assertEquals(0, this.test.getId());

		this.test.setId(-568);
		assertEquals(-568, this.test.getId());
	}

	@Test
	public void getJournalName() {
		assertNull(this.test.getJournalName());
	}

	@Test
	public void setJournalName() {
		assertNull(this.test.getJournalName());

		this.test.setJournalName("abc");
		assertEquals("abc", this.test.getJournalName());

		this.test.setJournalName("");
		assertNull(this.test.getJournalName());

		this.test.setJournalName("xyz");
		assertEquals("xyz", this.test.getJournalName());

		this.test.setJournalName(null);
		assertNull(this.test.getJournalName());
	}

	@Test
	public void getPublisher() {
		assertNull(this.test.getPublisher());
	}

	@Test
	public void setPublisher() {
		assertNull(this.test.getPublisher());

		this.test.setPublisher("abc");
		assertEquals("abc", this.test.getPublisher());

		this.test.setPublisher("");
		assertNull(this.test.getPublisher());

		this.test.setPublisher("xyz");
		assertEquals("xyz", this.test.getPublisher());

		this.test.setPublisher(null);
		assertNull(this.test.getPublisher());
	}

	@Test
	public void getAddress() {
		assertNull(this.test.getAddress());
	}

	@Test
	public void setAddress() {
		assertNull(this.test.getAddress());

		this.test.setAddress("abc");
		assertEquals("abc", this.test.getAddress());

		this.test.setAddress("");
		assertNull(this.test.getAddress());

		this.test.setAddress("xyz");
		assertEquals("xyz", this.test.getAddress());

		this.test.setAddress(null);
		assertNull(this.test.getAddress());
	}

	@Test
	public void getJournalURL() {
		assertNull(this.test.getJournalURL());
	}

	@Test
	public void setJournalURL_String() {
		assertNull(this.test.getJournalURL());

		this.test.setJournalURL("abc");
		assertEquals("abc", this.test.getJournalURL());

		this.test.setJournalURL("");
		assertNull(this.test.getJournalURL());

		this.test.setJournalURL("xyz");
		assertEquals("xyz", this.test.getJournalURL());

		this.test.setJournalURL((String) null);
		assertNull(this.test.getJournalURL());
	}

	@Test
	public void getJournalURLObject() {
		assertNull(this.test.getJournalURLObject());
	}

	@Test
	public void setJournalURL_URL() throws Exception {
		assertNull(this.test.getJournalURL());
		assertNull(this.test.getJournalURLObject());

		this.test.setJournalURL(new URL("http://abc.com"));
		assertEquals("http://abc.com", this.test.getJournalURL());
		assertEquals(new URL("http://abc.com"), this.test.getJournalURLObject());

		this.test.setJournalURL((URL) null);
		assertNull(this.test.getJournalURL());
		assertNull(this.test.getJournalURLObject());

		this.test.setJournalURL(new URL("http://xyz.org"));
		assertEquals("http://xyz.org", this.test.getJournalURL());
		assertEquals(new URL("http://xyz.org"), this.test.getJournalURLObject());
	}

	@Test
	public void getScimagoId() {
		assertNull(this.test.getScimagoId());
	}

	@Test
	public void setScimagoId() {
		assertNull(this.test.getScimagoId());

		this.test.setScimagoId("abc");
		assertEquals("abc", this.test.getScimagoId());

		this.test.setScimagoId("");
		assertNull(this.test.getScimagoId());

		this.test.setScimagoId("xyz");
		assertEquals("xyz", this.test.getScimagoId());

		this.test.setScimagoId(null);
		assertNull(this.test.getScimagoId());
	}

	@Test
	public void getScimagoCategory() {
		assertNull(this.test.getScimagoCategory());
	}

	@Test
	public void setScimagoCategory() {
		assertNull(this.test.getScimagoCategory());

		this.test.setScimagoCategory("abc");
		assertEquals("abc", this.test.getScimagoCategory());

		this.test.setScimagoCategory("");
		assertNull(this.test.getScimagoCategory());

		this.test.setScimagoCategory("xyz");
		assertEquals("xyz", this.test.getScimagoCategory());

		this.test.setScimagoCategory(null);
		assertNull(this.test.getScimagoCategory());
	}

	@Test
	public void getWosId() {
		assertNull(this.test.getWosId());
	}

	@Test
	public void setWosId() {
		assertNull(this.test.getWosId());

		this.test.setWosId("abc");
		assertEquals("abc", this.test.getWosId());

		this.test.setWosId("");
		assertNull(this.test.getWosId());

		this.test.setWosId("xyz");
		assertEquals("xyz", this.test.getWosId());

		this.test.setWosId(null);
		assertNull(this.test.getWosId());
	}

	@Test
	public void getWosCategory() {
		assertNull(this.test.getWosCategory());
	}

	@Test
	public void setWosCategory() {
		assertNull(this.test.getWosCategory());

		this.test.setWosCategory("abc");
		assertEquals("abc", this.test.getWosCategory());

		this.test.setWosCategory("");
		assertNull(this.test.getWosCategory());

		this.test.setWosCategory("xyz");
		assertEquals("xyz", this.test.getWosCategory());

		this.test.setWosCategory(null);
		assertNull(this.test.getWosCategory());
	}

	@Test
	public void getPublishedPapers() {
		assertTrue(this.test.getPublishedPapers().isEmpty());
	}

	@Test
	public void setPublishedPapers() {
		assertTrue(this.test.getPublishedPapers().isEmpty());

		final Set<JournalPaper> base0 = new TreeSet<>();
		this.test.setPublishedPapers(base0);
		assertNotNull(this.test.getPublishedPapers());
		assertEquals(base0, this.test.getPublishedPapers());

		final Set<JournalPaper> base1 = new TreeSet<>();
		base1.add(mock(JournalPaper.class));
		this.test.setPublishedPapers(base1);
		assertNotNull(this.test.getPublishedPapers());
		assertEquals(base1, this.test.getPublishedPapers());

		this.test.setPublishedPapers(null);
		assertTrue(this.test.getPublishedPapers().isEmpty());
	}

	@Test
	public void hasPublishedPaper() {
		assertFalse(this.test.hasPublishedPaper());

		final Set<JournalPaper> base0 = new TreeSet<>();
		this.test.setPublishedPapers(base0);
		assertFalse(this.test.hasPublishedPaper());

		final Set<JournalPaper> base1 = new TreeSet<>();
		base1.add(mock(JournalPaper.class));
		this.test.setPublishedPapers(base1);
		assertTrue(this.test.hasPublishedPaper());
	}

	@Test
	public void getQualityIndicators() {
		assertTrue(this.test.getQualityIndicators().isEmpty());
	}

	@Test
	public void getQualityIndicatorsForYear() {
		assertNull(this.test.getQualityIndicatorsForYear(-10));
		assertNull(this.test.getQualityIndicatorsForYear(0));
		assertNull(this.test.getQualityIndicatorsForYear(123));
		assertNull(this.test.getQualityIndicatorsForYear(2022));
	}

	@Test
	public void getQualityIndicatorsFor() {
		assertNull(this.test.getQualityIndicatorsFor(-10, it -> true));
		assertNull(this.test.getQualityIndicatorsFor(0, it -> true));
		assertNull(this.test.getQualityIndicatorsFor(123, it -> true));
		assertNull(this.test.getQualityIndicatorsFor(2022, it -> true));
	}

	@Test
	public void hasQualityIndicatorsForYear() {
		assertNull(this.test.getQualityIndicatorsForYear(-10));
		assertNull(this.test.getQualityIndicatorsForYear(0));
		assertNull(this.test.getQualityIndicatorsForYear(123));
		assertNull(this.test.getQualityIndicatorsForYear(2022));
	}

	@Test
	public void getScimagoQIndexByYear() {
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndexByYear(-1234));
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndexByYear(0));
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndexByYear(1234));
	}

	@Test
	public void setScimagoQIndexByYear() {
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndexByYear(512));
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndexByYear(1234));
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndexByYear(4567));

		assertNotNull(this.test.setScimagoQIndexByYear(1234, QuartileRanking.Q3));
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndexByYear(512));
		assertSame(QuartileRanking.Q3, this.test.getScimagoQIndexByYear(1234));
		assertSame(QuartileRanking.Q3, this.test.getScimagoQIndexByYear(4567));

		assertNotNull(this.test.setScimagoQIndexByYear(1234, null));
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndexByYear(512));
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndexByYear(1234));
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndexByYear(4567));

		assertNotNull(this.test.setScimagoQIndexByYear(1234, QuartileRanking.Q1));
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndexByYear(512));
		assertSame(QuartileRanking.Q1, this.test.getScimagoQIndexByYear(1234));
		assertSame(QuartileRanking.Q1, this.test.getScimagoQIndexByYear(4567));

		assertNotNull(this.test.setScimagoQIndexByYear(3, QuartileRanking.Q4));
		assertSame(QuartileRanking.Q4, this.test.getScimagoQIndexByYear(512));
		assertSame(QuartileRanking.Q1, this.test.getScimagoQIndexByYear(1234));
		assertSame(QuartileRanking.Q1, this.test.getScimagoQIndexByYear(4567));

		assertNotNull(this.test.setScimagoQIndexByYear(2500, QuartileRanking.Q3));
		assertSame(QuartileRanking.Q4, this.test.getScimagoQIndexByYear(512));
		assertSame(QuartileRanking.Q1, this.test.getScimagoQIndexByYear(1234));
		assertSame(QuartileRanking.Q3, this.test.getScimagoQIndexByYear(4567));
	}

	@Test
	public void hasScimagoQIndexForYear() {
		assertFalse(this.test.hasScimagoQIndexForYear(512));
		assertFalse(this.test.hasScimagoQIndexForYear(1234));
		assertFalse(this.test.hasScimagoQIndexForYear(4567));

		assertNotNull(this.test.setScimagoQIndexByYear(1234, QuartileRanking.Q3));
		assertFalse(this.test.hasScimagoQIndexForYear(512));
		assertTrue(this.test.hasScimagoQIndexForYear(1234));
		assertTrue(this.test.hasScimagoQIndexForYear(4567));

		assertNotNull(this.test.setScimagoQIndexByYear(1234, null));
		assertFalse(this.test.hasScimagoQIndexForYear(512));
		assertFalse(this.test.hasScimagoQIndexForYear(1234));
		assertFalse(this.test.hasScimagoQIndexForYear(4567));

		assertNotNull(this.test.setScimagoQIndexByYear(1234, QuartileRanking.Q1));
		assertFalse(this.test.hasScimagoQIndexForYear(512));
		assertTrue(this.test.hasScimagoQIndexForYear(1234));
		assertTrue(this.test.hasScimagoQIndexForYear(4567));

		assertNotNull(this.test.setScimagoQIndexByYear(3, QuartileRanking.Q4));
		assertTrue(this.test.hasScimagoQIndexForYear(512));
		assertTrue(this.test.hasScimagoQIndexForYear(1234));
		assertTrue(this.test.hasScimagoQIndexForYear(4567));
	}

	@Test
	public void getWosQIndexByYear() {
		assertSame(QuartileRanking.NR, this.test.getWosQIndexByYear(-1234));
		assertSame(QuartileRanking.NR, this.test.getWosQIndexByYear(0));
		assertSame(QuartileRanking.NR, this.test.getWosQIndexByYear(1234));
	}

	@Test
	public void setWosQIndexByYear() {
		assertSame(QuartileRanking.NR, this.test.getWosQIndexByYear(512));
		assertSame(QuartileRanking.NR, this.test.getWosQIndexByYear(1234));
		assertSame(QuartileRanking.NR, this.test.getWosQIndexByYear(4567));

		assertNotNull(this.test.setWosQIndexByYear(1234, QuartileRanking.Q3));
		assertSame(QuartileRanking.NR, this.test.getWosQIndexByYear(512));
		assertSame(QuartileRanking.Q3, this.test.getWosQIndexByYear(1234));
		assertSame(QuartileRanking.Q3, this.test.getWosQIndexByYear(4567));

		assertNotNull(this.test.setWosQIndexByYear(1234, null));
		assertSame(QuartileRanking.NR, this.test.getWosQIndexByYear(512));
		assertSame(QuartileRanking.NR, this.test.getWosQIndexByYear(1234));
		assertSame(QuartileRanking.NR, this.test.getWosQIndexByYear(4567));

		assertNotNull(this.test.setWosQIndexByYear(1234, QuartileRanking.Q1));
		assertSame(QuartileRanking.NR, this.test.getWosQIndexByYear(512));
		assertSame(QuartileRanking.Q1, this.test.getWosQIndexByYear(1234));
		assertSame(QuartileRanking.Q1, this.test.getWosQIndexByYear(4567));

		this.test.setWosQIndexByYear(3, QuartileRanking.Q4);
		assertSame(QuartileRanking.Q4, this.test.getWosQIndexByYear(512));
		assertSame(QuartileRanking.Q1, this.test.getWosQIndexByYear(1234));
		assertSame(QuartileRanking.Q1, this.test.getWosQIndexByYear(4567));
	}

	@Test
	public void hasWosQIndexForYear() {
		assertFalse(this.test.hasWosQIndexForYear(512));
		assertFalse(this.test.hasWosQIndexForYear(1234));
		assertFalse(this.test.hasWosQIndexForYear(4567));

		assertNotNull(this.test.setWosQIndexByYear(1234, QuartileRanking.Q3));
		assertFalse(this.test.hasWosQIndexForYear(512));
		assertTrue(this.test.hasWosQIndexForYear(1234));
		assertTrue(this.test.hasWosQIndexForYear(4567));

		assertNotNull(this.test.setWosQIndexByYear(1234, null));
		assertFalse(this.test.hasWosQIndexForYear(512));
		assertFalse(this.test.hasWosQIndexForYear(1234));
		assertFalse(this.test.hasWosQIndexForYear(4567));

		assertNotNull(this.test.setWosQIndexByYear(1234, QuartileRanking.Q1));
		assertFalse(this.test.hasWosQIndexForYear(512));
		assertTrue(this.test.hasWosQIndexForYear(1234));
		assertTrue(this.test.hasWosQIndexForYear(4567));

		assertNotNull(this.test.setWosQIndexByYear(3, QuartileRanking.Q4));
		assertTrue(this.test.hasWosQIndexForYear(512));
		assertTrue(this.test.hasWosQIndexForYear(1234));
		assertTrue(this.test.hasWosQIndexForYear(4567));
	}

	@Test
	public void getImpactFactorByYear() {
		assertEquals(0f, this.test.getImpactFactorByYear(-1234));
		assertEquals(0f, this.test.getImpactFactorByYear(0));
		assertEquals(0f, this.test.getImpactFactorByYear(1234));
	}

	@Test
	public void setImpactFactorByYear() {
		assertEquals(0f, this.test.getImpactFactorByYear(512));
		assertEquals(0f, this.test.getImpactFactorByYear(1234));
		assertEquals(0f, this.test.getImpactFactorByYear(4567));

		assertNotNull(this.test.setImpactFactorByYear(1234, 4.567f));
		assertEquals(0f, this.test.getImpactFactorByYear(512));
		assertEquals(4.567f, this.test.getImpactFactorByYear(1234));
		assertEquals(4.567f, this.test.getImpactFactorByYear(4567));

		assertNotNull(this.test.setImpactFactorByYear(1234, 0f));
		assertEquals(0f, this.test.getImpactFactorByYear(512));
		assertEquals(0f, this.test.getImpactFactorByYear(1234));
		assertEquals(0f, this.test.getImpactFactorByYear(4567));

		assertNotNull(this.test.setImpactFactorByYear(1234, 9.478f));
		assertEquals(0f, this.test.getImpactFactorByYear(512));
		assertEquals(9.478f, this.test.getImpactFactorByYear(1234));
		assertEquals(9.478f, this.test.getImpactFactorByYear(4567));

		this.test.setImpactFactorByYear(3, 0.412f);
		assertEquals(0.412f, this.test.getImpactFactorByYear(512));
		assertEquals(9.478f, this.test.getImpactFactorByYear(1234));
		assertEquals(9.478f, this.test.getImpactFactorByYear(4567));
	}

	@Test
	public void hasImpactFactorForYear() {
		assertFalse(this.test.hasImpactFactorForYear(512));
		assertFalse(this.test.hasImpactFactorForYear(1234));
		assertFalse(this.test.hasImpactFactorForYear(4567));

		assertNotNull(this.test.setImpactFactorByYear(1234, 123.456f));
		assertFalse(this.test.hasImpactFactorForYear(512));
		assertTrue(this.test.hasImpactFactorForYear(1234));
		assertTrue(this.test.hasImpactFactorForYear(4567));

		assertNotNull(this.test.setImpactFactorByYear(1234, 0f));
		assertFalse(this.test.hasImpactFactorForYear(512));
		assertFalse(this.test.hasImpactFactorForYear(1234));
		assertFalse(this.test.hasImpactFactorForYear(4567));

		assertNotNull(this.test.setImpactFactorByYear(1234, 9.589f));
		assertFalse(this.test.hasImpactFactorForYear(512));
		assertTrue(this.test.hasImpactFactorForYear(1234));
		assertTrue(this.test.hasImpactFactorForYear(4567));

		assertNotNull(this.test.setImpactFactorByYear(3, 5f));
		assertTrue(this.test.hasImpactFactorForYear(512));
		assertTrue(this.test.hasImpactFactorForYear(1234));
		assertTrue(this.test.hasImpactFactorForYear(4567));
	}

	@Test
	public void getOpenAcess() {
		assertNull(this.test.getOpenAccess());
	}

	@Test
	public void setOpenAccess() {
		assertNull(this.test.getOpenAccess());

		this.test.setOpenAccess(Boolean.TRUE);
		assertTrue(this.test.getOpenAccess());

		this.test.setOpenAccess(null);
		assertNull(this.test.getOpenAccess());

		this.test.setOpenAccess(Boolean.FALSE);
		assertFalse(this.test.getOpenAccess());
	}

	@Test
	public void getISBN() {
		assertNull(this.test.getISBN());
	}

	@Test
	public void setISBN() {
		this.test.setISBN("xyz");
		assertEquals("xyz", this.test.getISBN());

		this.test.setISBN("");
		assertNull(this.test.getISBN());

		this.test.setISBN(null);
		assertNull(this.test.getISBN());
	}

	@Test
	public void getISSN() {
		assertNull(this.test.getISSN());
	}

	@Test
	public void setISSN() {
		this.test.setISSN("xyz");
		assertEquals("xyz", this.test.getISSN());

		this.test.setISSN("");
		assertNull(this.test.getISSN());

		this.test.setISSN(null);
		assertNull(this.test.getISSN());
	}

}
