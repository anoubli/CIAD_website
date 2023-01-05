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

package fr.ciadlab.labmanager.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Unit}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class UnitTest {

	private List<Unit> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(Unit.values()));
	}

	private Unit cons(Unit unit) {
		assertTrue(this.items.remove(unit), "Expecting enumeration item: " + unit.toString());
		return unit;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void fromUnitLabel() {
		assertEquals(Unit.NONE, cons(Unit.fromUnitLabel("")));
		assertEquals(Unit.KILO, cons(Unit.fromUnitLabel("k")));
		assertEquals(Unit.MEGA, cons(Unit.fromUnitLabel("m")));
		assertEquals(Unit.GIGA, cons(Unit.fromUnitLabel("g")));
		assertEquals(Unit.TERA, cons(Unit.fromUnitLabel("t")));
		assertEquals(Unit.PETA, cons(Unit.fromUnitLabel("p")));
		assertEquals(Unit.EXA, cons(Unit.fromUnitLabel("e")));
		assertEquals(Unit.ZETTA, cons(Unit.fromUnitLabel("z")));
		assertEquals(Unit.YOTTA, cons(Unit.fromUnitLabel("y")));
		assertAllTreated();
	}

	@Test
	public void fromNumericValue_positiveValue() {
		assertEquals(Unit.NONE, cons(Unit.fromNumericValue(456.)));
		assertEquals(Unit.KILO, cons(Unit.fromNumericValue(7890.)));
		assertEquals(Unit.MEGA, cons(Unit.fromNumericValue(1407045.)));
		assertEquals(Unit.GIGA, cons(Unit.fromNumericValue(1407045756.)));
		assertEquals(Unit.TERA, cons(Unit.fromNumericValue(1407045756451.)));
		assertEquals(Unit.PETA, cons(Unit.fromNumericValue(new BigInteger("1407045756451000"))));
		assertEquals(Unit.EXA, cons(Unit.fromNumericValue(new BigInteger("1407045756451000111"))));
		assertEquals(Unit.ZETTA, cons(Unit.fromNumericValue(new BigInteger("1407045756451000111222"))));
		assertEquals(Unit.YOTTA, cons(Unit.fromNumericValue(new BigInteger("1407045756451000111222333"))));
		assertAllTreated();
	}

	@Test
	public void fromNumericValue_fpValue() {
		assertEquals(Unit.NONE, cons(Unit.fromNumericValue(456.4)));
		assertEquals(Unit.KILO, cons(Unit.fromNumericValue(7890.5)));
		assertEquals(Unit.MEGA, cons(Unit.fromNumericValue(1407045.6)));
		assertEquals(Unit.GIGA, cons(Unit.fromNumericValue(1407045756.7)));
		assertEquals(Unit.TERA, cons(Unit.fromNumericValue(1407045756451.8)));
		assertEquals(Unit.PETA, cons(Unit.fromNumericValue(new BigDecimal("1407045756451000.5"))));
		assertEquals(Unit.EXA, cons(Unit.fromNumericValue(new BigDecimal("1407045756451000111.6"))));
		assertEquals(Unit.ZETTA, cons(Unit.fromNumericValue(new BigDecimal("1407045756451000111222.7"))));
		assertEquals(Unit.YOTTA, cons(Unit.fromNumericValue(new BigDecimal("1407045756451000111222333.8"))));
		assertAllTreated();
	}

	@Test
	public void fromNumericValue_negativeValue() {
		assertEquals(Unit.NONE, cons(Unit.fromNumericValue(-456.)));
		assertEquals(Unit.KILO, cons(Unit.fromNumericValue(-7890.)));
		assertEquals(Unit.MEGA, cons(Unit.fromNumericValue(-1407045.)));
		assertEquals(Unit.GIGA, cons(Unit.fromNumericValue(-1407045756.)));
		assertEquals(Unit.TERA, cons(Unit.fromNumericValue(-1407045756451.)));
		assertEquals(Unit.PETA, cons(Unit.fromNumericValue(new BigInteger("-1407045756451000"))));
		assertEquals(Unit.EXA, cons(Unit.fromNumericValue(new BigInteger("-1407045756451000111"))));
		assertEquals(Unit.ZETTA, cons(Unit.fromNumericValue(new BigInteger("-1407045756451000111222"))));
		assertEquals(Unit.YOTTA, cons(Unit.fromNumericValue(new BigInteger("-1407045756451000111222333"))));
		assertAllTreated();
	}

	@Test
	public void fromNumericValue_borders() {
		assertEquals(Unit.NONE, Unit.fromNumericValue(0l));
		assertEquals(Unit.NONE, Unit.fromNumericValue(9l));
		assertEquals(Unit.NONE, Unit.fromNumericValue(10l));
		assertEquals(Unit.NONE, Unit.fromNumericValue(99l));
		assertEquals(Unit.NONE, Unit.fromNumericValue(100l));
		assertEquals(Unit.NONE, Unit.fromNumericValue(999l));
		assertEquals(Unit.KILO, Unit.fromNumericValue(1000l));
		assertEquals(Unit.KILO, Unit.fromNumericValue(9999l));
		assertEquals(Unit.KILO, Unit.fromNumericValue(10000l));
		assertEquals(Unit.KILO, Unit.fromNumericValue(99999l));
		assertEquals(Unit.KILO, Unit.fromNumericValue(100000l));
		assertEquals(Unit.KILO, Unit.fromNumericValue(999999l));
		assertEquals(Unit.MEGA, Unit.fromNumericValue(1000000l));
		assertEquals(Unit.MEGA, Unit.fromNumericValue(9999999l));
		assertEquals(Unit.MEGA, Unit.fromNumericValue(10000000l));
		assertEquals(Unit.MEGA, Unit.fromNumericValue(99999999l));
		assertEquals(Unit.MEGA, Unit.fromNumericValue(100000000l));
		assertEquals(Unit.MEGA, Unit.fromNumericValue(999999999l));
		assertEquals(Unit.GIGA, Unit.fromNumericValue(1000000000l));
		assertEquals(Unit.GIGA, Unit.fromNumericValue(9999999999l));
		assertEquals(Unit.GIGA, Unit.fromNumericValue(10000000000l));
		assertEquals(Unit.GIGA, Unit.fromNumericValue(99999999999l));
		assertEquals(Unit.GIGA, Unit.fromNumericValue(100000000000l));
		assertEquals(Unit.GIGA, Unit.fromNumericValue(999999999999l));
		assertEquals(Unit.TERA, Unit.fromNumericValue(1000000000000l));
		assertEquals(Unit.TERA, Unit.fromNumericValue(9999999999999l));
		assertEquals(Unit.TERA, Unit.fromNumericValue(10000000000000l));
		assertEquals(Unit.TERA, Unit.fromNumericValue(99999999999999l));
		assertEquals(Unit.TERA, Unit.fromNumericValue(100000000000000l));
		assertEquals(Unit.TERA, Unit.fromNumericValue(999999999999999l));
		assertEquals(Unit.PETA, Unit.fromNumericValue(new BigInteger("1000000000000000")));
		assertEquals(Unit.PETA, Unit.fromNumericValue(new BigInteger("9999999999999999")));
		assertEquals(Unit.PETA, Unit.fromNumericValue(new BigInteger("10000000000000000")));
		assertEquals(Unit.PETA, Unit.fromNumericValue(new BigInteger("99999999999999999")));
		assertEquals(Unit.PETA, Unit.fromNumericValue(new BigInteger("100000000000000000")));
		assertEquals(Unit.PETA, Unit.fromNumericValue(new BigInteger("999999999999999999")));
		assertEquals(Unit.EXA, Unit.fromNumericValue(new BigInteger("1000000000000000000")));
		assertEquals(Unit.EXA, Unit.fromNumericValue(new BigInteger("9999999999999999999")));
		assertEquals(Unit.EXA, Unit.fromNumericValue(new BigInteger("10000000000000000000")));
		assertEquals(Unit.EXA, Unit.fromNumericValue(new BigInteger("99999999999999999999")));
		assertEquals(Unit.EXA, Unit.fromNumericValue(new BigInteger("100000000000000000000")));
		assertEquals(Unit.EXA, Unit.fromNumericValue(new BigInteger("999999999999999999999")));
		assertEquals(Unit.ZETTA, Unit.fromNumericValue(new BigInteger("1000000000000000000000")));
		assertEquals(Unit.ZETTA, Unit.fromNumericValue(new BigInteger("9999999999999999999999")));
		assertEquals(Unit.ZETTA, Unit.fromNumericValue(new BigInteger("10000000000000000000000")));
		assertEquals(Unit.ZETTA, Unit.fromNumericValue(new BigInteger("99999999999999999999999")));
		assertEquals(Unit.ZETTA, Unit.fromNumericValue(new BigInteger("100000000000000000000000")));
		assertEquals(Unit.ZETTA, Unit.fromNumericValue(new BigInteger("999999999999999999999999")));
		assertEquals(Unit.YOTTA, Unit.fromNumericValue(new BigInteger("1000000000000000000000000")));
		assertEquals(Unit.YOTTA, Unit.fromNumericValue(new BigInteger("9999999999999999999999999")));
		assertEquals(Unit.YOTTA, Unit.fromNumericValue(new BigInteger("10000000000000000000000000")));
		assertEquals(Unit.YOTTA, Unit.fromNumericValue(new BigInteger("99999999999999999999999999")));
		assertEquals(Unit.YOTTA, Unit.fromNumericValue(new BigInteger("100000000000000000000000000")));
		assertEquals(Unit.YOTTA, Unit.fromNumericValue(new BigInteger("999999999999999999999999999")));
		assertEquals(Unit.YOTTA, Unit.fromNumericValue(new BigInteger("1000000000000000000000000000")));
	}

}
