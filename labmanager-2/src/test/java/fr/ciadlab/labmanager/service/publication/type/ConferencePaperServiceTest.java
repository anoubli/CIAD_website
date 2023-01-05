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

package fr.ciadlab.labmanager.service.publication.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.ConferencePaper;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.repository.publication.type.ConferencePaperRepository;
import fr.ciadlab.labmanager.service.member.MembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link ConferencePaperService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class ConferencePaperServiceTest {

	private ConferencePaper pub0;

	private ConferencePaper pub1;

	private ConferencePaper pub2;

	private Publication base;

	private MessageSourceAccessor messages;

	private DownloadableFileManager downloadableFileManager;

	private ConferencePaperRepository repository;

	private MembershipService membershipService;

	private ConferencePaperService test;

	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.downloadableFileManager = mock(DownloadableFileManager.class);
		this.repository = mock(ConferencePaperRepository.class);
		this.membershipService = mock(MembershipService.class);
		this.test = new ConferencePaperService(this.messages, new Constants(), this.downloadableFileManager, this.repository,
				this.membershipService);

		// Prepare some publications to be inside the repository
		// The lenient configuration is used to configure the mocks for all the tests
		// at the same code location for configuration simplicity
		this.pub0 = mock(ConferencePaper.class);
		lenient().when(this.pub0.getId()).thenReturn(123);
		this.pub1 = mock(ConferencePaper.class);
		lenient().when(this.pub1.getId()).thenReturn(234);
		this.pub2 = mock(ConferencePaper.class);
		lenient().when(this.pub2.getId()).thenReturn(345);

		lenient().when(this.repository.findAll()).thenReturn(
				Arrays.asList(this.pub0, this.pub1, this.pub2));
		lenient().when(this.repository.findById(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 123:
				return Optional.of(this.pub0);
			case 234:
				return Optional.of(this.pub1);
			case 345:
				return Optional.of(this.pub2);
			}
			return Optional.empty();
		});

		this.base = mock(Publication.class);
		lenient().when(this.base.getId()).thenReturn(4567);
	}

	@Test
	public void getAllConferencePapers() {
		final List<ConferencePaper> list = this.test.getAllConferencePapers();
		assertNotNull(list);
		assertEquals(3, list.size());
		assertSame(this.pub0, list.get(0));
		assertSame(this.pub1, list.get(1));
		assertSame(this.pub2, list.get(2));
	}

	@Test
	public void getConferencePaper() {
		assertNull(this.test.getConferencePaper(-4756));
		assertNull(this.test.getConferencePaper(0));
		assertSame(this.pub0, this.test.getConferencePaper(123));
		assertSame(this.pub1, this.test.getConferencePaper(234));
		assertSame(this.pub2, this.test.getConferencePaper(345));
		assertNull(this.test.getConferencePaper(7896));
	}

	@Test
	public void createConferencePaper() {
		final ConferencePaper actual = this.test.createConferencePaper(pub0,
				"event0", "volume0", "number0", "pages0", "editors0",
				"series0", "orga0", "address0", "publish0");

		assertEquals("event0", actual.getScientificEventName());
		assertEquals("volume0", actual.getVolume());
		assertEquals("number0", actual.getNumber());
		assertEquals("pages0", actual.getPages());
		assertEquals("editors0", actual.getEditors());
		assertEquals("address0", actual.getAddress());
		assertEquals("series0", actual.getSeries());
		assertEquals("orga0", actual.getOrganization());
		assertEquals("address0", actual.getAddress());
		assertEquals("publish0", actual.getPublisher());

		verify(this.repository).save(actual);
	}

	@Test
	public void updateConferencePaper() {
		this.test.updateConferencePaper(234,
				"title0", PublicationType.NATIONAL_CONFERENCE_PAPER, LocalDate.parse("2022-07-22"), 2022, "abstractText0",
				"keywords0", "doi0", "isbn0", "issn0", "dblpUrl0", "extraUrl0",
				PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0", "pathToVideo0",
				"event0", "volume0", "number0", "pages0", "editors0",
				"series0", "orga0", "publisher0", "address0");

		verifyNoInteractions(this.pub0);

		verify(this.pub1).setScientificEventName("event0");
		verify(this.pub1).setVolume("volume0");
		verify(this.pub1).setNumber("number0");
		verify(this.pub1).setPages("pages0");
		verify(this.pub1).setEditors("editors0");
		verify(this.pub1).setSeries("series0");
		verify(this.pub1).setOrganization("orga0");
		verify(this.pub1).setPublisher("publisher0");
		verify(this.pub1).setAddress("address0");

		verifyNoInteractions(this.pub2);
	}

	@Test
	public void removeConferencePaper() {
		this.test.removeConferencePaper(345);

		verify(this.repository).deleteById(345);
	}

}
