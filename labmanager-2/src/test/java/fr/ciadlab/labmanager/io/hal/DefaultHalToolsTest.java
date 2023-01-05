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

package fr.ciadlab.labmanager.io.hal;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.MalformedURLException;
import java.net.URL;

import fr.ciadlab.labmanager.io.hal.DefaultHalTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link DefaultHalTools}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class DefaultHalToolsTest {

	private DefaultHalTools test;

	@BeforeEach
	public void setUp() {
		this.test = new DefaultHalTools();
	}

	@Test
	public void getHALNumberFromHALUrl_String_null() throws Exception {
		final String r = this.test.getHALNumberFromHALUrl((String) null);
		assertNull(r);
	}

	@Test
	public void getHALNumberFromHALUrl_String_empty() throws Exception {
		final String r = this.test.getHALNumberFromHALUrl("");
		assertNull(r);
	}

	@Test
	public void getHALNumberFromHALUrl_String_malformedUrl() throws Exception {
		final MalformedURLException ex = catchThrowableOfType(
				() -> this.test.getHALNumberFromHALUrl("xyz/abc"),
				MalformedURLException.class);
		assertNotNull(ex);
	}


	@Test
	public void getHALNumberFromHALUrl_String() throws Exception {
		final String r = this.test.getHALNumberFromHALUrl("https://hal.archives-ouvertes.fr/xyz/abc");
		assertNotNull(r);
		assertEquals("xyz/abc", r);
	}

	@Test
	public void getHALNumberFromHALUrl_URL_null() throws Exception {
		final String r = this.test.getHALNumberFromHALUrl((URL) null);
		assertNull(r);
	}

	@Test
	public void getHALNumberFromHALUrl_URL() throws Exception {
		final String r = this.test.getHALNumberFromHALUrl(new URL("https://hal.archives-ouvertes.fr/xyz/abc"));
		assertNotNull(r);
		assertEquals("xyz/abc", r);
	}

	@Test
	public void getHALUrlFromHALNumber_null() {
		final URL url = this.test.getHALUrlFromHALNumber(null);
		assertNull(url);
	}

	@Test
	public void getHALUrlFromHALNumber_empty() {
		final URL url = this.test.getHALUrlFromHALNumber("");
		assertNull(url);
	}

	@Test
	public void getHALUrlFromHALNumber_url0() {
		final URL url = this.test.getHALUrlFromHALNumber("a b c");
		assertNotNull(url);
		assertEquals("https://hal.archives-ouvertes.fr/a%20b%20c", url.toExternalForm());
	}

	@Test
	public void getHALUrlFromHALNumber_url1() {
		final URL url = this.test.getHALUrlFromHALNumber("xyz/abc");
		assertNotNull(url);
		assertEquals("https://hal.archives-ouvertes.fr/xyz/abc", url.toExternalForm());
	}

}