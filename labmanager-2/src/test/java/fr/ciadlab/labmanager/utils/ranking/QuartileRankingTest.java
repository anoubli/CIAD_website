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

package fr.ciadlab.labmanager.utils.ranking;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.junit.jupiter.api.Test;

/** Tests for {@link QuartileRanking}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class QuartileRankingTest {

	@Test
	public void toStringTest() {
		assertEquals("Q1", QuartileRanking.Q1.toString());
		assertEquals("Q2", QuartileRanking.Q2.toString());
		assertEquals("Q3", QuartileRanking.Q3.toString());
		assertEquals("Q4", QuartileRanking.Q4.toString());
		assertEquals("NR", QuartileRanking.NR.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> QuartileRanking.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> QuartileRanking.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(QuartileRanking.Q1, QuartileRanking.valueOfCaseInsensitive("Q1"));
		assertEquals(QuartileRanking.Q2, QuartileRanking.valueOfCaseInsensitive("q2"));
		assertEquals(QuartileRanking.Q3, QuartileRanking.valueOfCaseInsensitive("Q3"));
		assertEquals(QuartileRanking.Q4, QuartileRanking.valueOfCaseInsensitive("Q4"));
		assertEquals(QuartileRanking.NR, QuartileRanking.valueOfCaseInsensitive("NR"));
		assertThrows(IllegalArgumentException.class, () -> QuartileRanking.valueOfCaseInsensitive("E"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(QuartileRanking.Q1, QuartileRanking.valueOfCaseInsensitive("q1"));
		assertEquals(QuartileRanking.Q2, QuartileRanking.valueOfCaseInsensitive("q2"));
		assertEquals(QuartileRanking.Q3, QuartileRanking.valueOfCaseInsensitive("q3"));
		assertEquals(QuartileRanking.Q4, QuartileRanking.valueOfCaseInsensitive("q4"));
		assertEquals(QuartileRanking.NR, QuartileRanking.valueOfCaseInsensitive("nR"));
		assertThrows(IllegalArgumentException.class, () -> QuartileRanking.valueOfCaseInsensitive("e"));
	}

	@Test
	public void normalize_null() {
		assertEquals(QuartileRanking.NR, QuartileRanking.normalize(null));
	}

	@Test
	public void normalize() {
		assertEquals(QuartileRanking.Q1, QuartileRanking.normalize(QuartileRanking.Q1));
		assertEquals(QuartileRanking.Q2, QuartileRanking.normalize(QuartileRanking.Q2));
		assertEquals(QuartileRanking.Q3, QuartileRanking.normalize(QuartileRanking.Q3));
		assertEquals(QuartileRanking.Q4, QuartileRanking.normalize(QuartileRanking.Q4));
		assertEquals(QuartileRanking.NR, QuartileRanking.normalize(QuartileRanking.NR));
	}

}
