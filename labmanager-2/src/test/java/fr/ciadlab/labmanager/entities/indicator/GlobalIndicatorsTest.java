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

package fr.ciadlab.labmanager.entities.indicator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link GlobalIndicators}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class GlobalIndicatorsTest {

	private GlobalIndicators test;

	@BeforeEach
	public void setUp() {
		this.test = new GlobalIndicators();
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
	public void getLastUpdate() {
		assertNull(this.test.getLastUpdate());
	}

	@Test
	public void setLastUpdate_LocalDate() {
		assertNull(this.test.getLastUpdate());

		this.test.setLastUpdate(LocalDateTime.of(2022, 10, 24, 15, 45, 23));
		assertEquals(LocalDateTime.of(2022, 10, 24, 15, 45, 23), this.test.getLastUpdate());

		this.test.setLastUpdate((LocalDateTime) null);
		assertNull(this.test.getLastUpdate());
	}

	@Test
	public void setLastUpdate_String() {
		assertNull(this.test.getLastUpdate());

		this.test.setLastUpdate("2022-10-24T10:36:22");
		assertEquals(LocalDateTime.of(2022, 10, 24, 10, 36, 22), this.test.getLastUpdate());

		this.test.setLastUpdate("");
		assertNull(this.test.getLastUpdate());

		this.test.setLastUpdate("2022-10-02T15:45:23");
		assertEquals(LocalDateTime.of(2022, 10, 2, 15, 45, 23), this.test.getLastUpdate());

		this.test.setLastUpdate((String) null);
		assertNull(this.test.getLastUpdate());
	}

	@Test
	public void getValues() {
		assertNull(this.test.getValues());
	}

	@Test
	public void setValues() {
		assertNull(this.test.getValues());
		assertNull(this.test.getIndicators());

		this.test.setValues("{\"abc\":4}");
		assertEquals("{\"abc\":4}", this.test.getValues());
		assertNull(this.test.getIndicators());

		this.test.setValues(null);
		assertNull(this.test.getValues());
		assertNull(this.test.getIndicators());

		this.test.setValues("{\"xyz\":8}");
		assertEquals("{\"xyz\":8}", this.test.getValues());
		assertNull(this.test.getIndicators());

		this.test.setValues("");
		assertNull(this.test.getValues());
		assertNull(this.test.getIndicators());
	}

	@Test
	public void updateCache_noValue() {
		this.test.updateCache();
		assertNull(this.test.getIndicators());
	}

	@Test
	public void updateCache_value() {
		this.test.setValues("{\"xyz\":18}");
		this.test.updateCache();
		Map<String, Number> cache = this.test.getIndicators();
		assertNotNull(cache);
		assertEquals(1, cache.size());
		assertTrue(cache.containsKey("xyz"));
		assertEquals(18, cache.get("xyz").intValue());
	}

	@Test
	public void getVisibleIndicatorKeys() {
		assertNull(this.test.getVisibleIndicatorKeys());
	}

	@Test
	public void setVisibleIndicatorKeys() {
		assertNull(this.test.getVisibleIndicatorKeys());

		this.test.setVisibleIndicatorKeys("abc,xyz");
		assertEquals("abc,xyz", this.test.getVisibleIndicatorKeys());

		this.test.setVisibleIndicatorKeys("");
		assertNull(this.test.getVisibleIndicatorKeys());

		this.test.setVisibleIndicatorKeys("uvw,nop");
		assertEquals("uvw,nop", this.test.getVisibleIndicatorKeys());

		this.test.setVisibleIndicatorKeys(null);
		assertNull(this.test.getVisibleIndicatorKeys());
	}

	@Test
	public void getVisibleIndicators() {
		assertTrue(this.test.getVisibleIndicators().isEmpty());
	}

	@Test
	public void setVisibleIndicators() {
		assertTrue(this.test.getVisibleIndicators().isEmpty());

		this.test.setVisibleIndicators(Arrays.asList("abc", "xyz"));
		List<String> keys0 = this.test.getVisibleIndicators();
		assertEquals(2, keys0.size());
		assertTrue(keys0.contains("xyz"));
		assertTrue(keys0.contains("abc"));

		this.test.setVisibleIndicators(Arrays.asList());
		assertTrue(this.test.getVisibleIndicators().isEmpty());

		this.test.setVisibleIndicators(Arrays.asList("uvw", "nop"));
		List<String> keys1 = this.test.getVisibleIndicators();
		assertEquals(2, keys1.size());
		assertTrue(keys1.contains("nop"));
		assertTrue(keys1.contains("uvw"));

		this.test.setVisibleIndicators(null);
		assertTrue(this.test.getVisibleIndicators().isEmpty());
	}

}
