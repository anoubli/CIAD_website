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

package fr.ciadlab.labmanager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.nio.charset.StandardCharsets;

import fr.ciadlab.labmanager.configuration.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link AbstractCredentialController}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class AbstractCredentialControllerTest {

	private static final String HASH1 = "76c3299759c8512c";

	private static final byte[] INPUT1 = new byte[] {
			0x6e, 0x65, 0x79, 0x64, 0x53, 0x4f, 0x70, 0x4a,
			0x4a, 0x59, 0x68, 0x52, 0x45, 0x33, 0x45, 0x62,
			0x46, 0x5a, 0x67, 0x57, 0x45, 0x77, 0x3d, 0x3d,
			0x3a, 0x4e, 0x47, 0x59, 0x33, 0x4e, 0x6a, 0x51,
			0x33, 0x4e, 0x6a, 0x59, 0x35, 0x4d, 0x6d, 0x4a,
			0x6c, 0x4e, 0x7a, 0x45, 0x33, 0x4d, 0x51, 0x3d,
			0x3d
	};

	private static final String OUTPUT1 = "mylogin";

	private static AbstractCredentialController create(String hash) {
		final MessageSourceAccessor messages = mock(MessageSourceAccessor.class);
		return new AbstractCredentialController(messages, new Constants(), hash) {
			@Override
			public String readCredentials(String username, String serviceName, Object... serviceParams) {
				return super.readCredentials(username, serviceName, serviceParams);
			}
		};
	}

	@Test
	public void readCredentials1_utf8() throws Exception {
		final String input1 = new String(INPUT1, StandardCharsets.UTF_8);
		final AbstractCredentialController test = create(HASH1);
		final String username = test.readCredentials(input1, "myserv");
		assertEquals("mylogin", username);
	}

	@Test
	public void readCredentials1_ascii() throws Exception {
		final String input1 = new String(INPUT1, StandardCharsets.US_ASCII);
		final AbstractCredentialController test = create(HASH1);
		final String username = test.readCredentials(input1, "myserv");
		assertEquals("mylogin", username);
	}

	@Test
	public void readCredentials1_iso88591() throws Exception {
		final String input1 = new String(INPUT1, StandardCharsets.ISO_8859_1);
		final AbstractCredentialController test = create(HASH1);
		final String username = test.readCredentials(input1, "myserv");
		assertEquals("mylogin", username);
	}

}
