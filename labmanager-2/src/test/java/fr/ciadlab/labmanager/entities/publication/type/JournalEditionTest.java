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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link JournalEdition}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class JournalEditionTest {

	private JournalEdition test;

	@BeforeEach
	public void setUp() {
		this.test = new JournalEdition();
	}

	@Test
	public void isRanked_notRanked() {
		final Journal jour = mock(Journal.class);
		when(jour.getScimagoQIndexByYear(anyInt())).thenCallRealMethod();
		when(jour.getWosQIndexByYear(anyInt())).thenCallRealMethod();
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);
		assertFalse(this.test.isRanked());
	}

	@Test
	public void isRanked_scimagoRanked() {
		final Journal jour = mock(Journal.class);
		when(jour.getScimagoQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q1);
		when(jour.getWosQIndexByYear(anyInt())).thenCallRealMethod();
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);
		assertTrue(this.test.isRanked());
	}

	@Test
	public void isRanked_wosRanked() {
		final Journal jour = mock(Journal.class);
		when(jour.getScimagoQIndexByYear(anyInt())).thenCallRealMethod();
		when(jour.getWosQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q2);
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);
		assertTrue(this.test.isRanked());
	}

	@Test
	public void isRanked_bothRanked() {
		final Journal jour = mock(Journal.class);
		when(jour.getScimagoQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q3);
		when(jour.getWosQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q4);
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);
		assertTrue(this.test.isRanked());
	}

	@Test
	public void getVolume() {
		assertNull(this.test.getVolume());
	}

	@Test
	public void setVolume() {
		this.test.setVolume("xyz");
		assertEquals("xyz", this.test.getVolume());

		this.test.setVolume("");
		assertNull(this.test.getVolume());

		this.test.setVolume(null);
		assertNull(this.test.getVolume());
	}

	@Test
	public void getNumber() {
		assertNull(this.test.getNumber());
	}

	@Test
	public void setNumber() {
		this.test.setNumber("xyz");
		assertEquals("xyz", this.test.getNumber());

		this.test.setNumber("");
		assertNull(this.test.getNumber());

		this.test.setNumber(null);
		assertNull(this.test.getNumber());
	}

	@Test
	public void getPages() {
		assertNull(this.test.getPages());
	}

	@Test
	public void setPages() {
		this.test.setPages("xyz");
		assertEquals("xyz", this.test.getPages());

		this.test.setPages("");
		assertNull(this.test.getPages());

		this.test.setPages(null);
		assertNull(this.test.getPages());
	}

	@Test
	public void getJournal() {
		assertNull(this.test.getJournal());
	}

	@Test
	public void setJournal() {
		final Journal jour = mock(Journal.class);
		this.test.setJournal(jour);
		assertSame(jour, this.test.getJournal());

		this.test.setJournal(null);
		assertNull(this.test.getJournal());
	}

	@Test
	public void getScimagoQIndex() {
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndex());

		final Journal jour = mock(Journal.class);
		when(jour.getScimagoQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q3);
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);

		assertSame(QuartileRanking.Q3, this.test.getScimagoQIndex());
	}

	@Test
	public void getWosQIndex() {
		assertSame(QuartileRanking.NR, this.test.getWosQIndex());

		final Journal jour = mock(Journal.class);
		when(jour.getWosQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q2);
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);

		assertSame(QuartileRanking.Q2, this.test.getWosQIndex());
	}

	@Test
	public void getImpactFactor() {
		assertEquals(0f, this.test.getImpactFactor());

		final Journal jour = mock(Journal.class);
		when(jour.getImpactFactorByYear(anyInt())).thenReturn(426f);
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);

		assertEquals(426f, this.test.getImpactFactor());
	}

}
