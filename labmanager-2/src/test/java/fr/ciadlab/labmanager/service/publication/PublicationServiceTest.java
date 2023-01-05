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

package fr.ciadlab.labmanager.service.publication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.bibtex.BibTeX;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.io.html.HtmlDocumentExporter;
import fr.ciadlab.labmanager.io.json.JsonExporter;
import fr.ciadlab.labmanager.io.od.OpenDocumentTextExporter;
import fr.ciadlab.labmanager.repository.journal.JournalRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.publication.AuthorshipRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.type.BookChapterService;
import fr.ciadlab.labmanager.service.publication.type.BookService;
import fr.ciadlab.labmanager.service.publication.type.ConferencePaperService;
import fr.ciadlab.labmanager.service.publication.type.JournalEditionService;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.service.publication.type.KeyNoteService;
import fr.ciadlab.labmanager.service.publication.type.MiscDocumentService;
import fr.ciadlab.labmanager.service.publication.type.PatentService;
import fr.ciadlab.labmanager.service.publication.type.ReportService;
import fr.ciadlab.labmanager.service.publication.type.ThesisService;
import fr.ciadlab.labmanager.utils.names.DefaultPersonNameParser;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link PublicationService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class PublicationServiceTest {

	private Publication pub0;

	private Publication pub1;

	private Publication pub2;

	private MessageSourceAccessor messages;

	private PublicationRepository publicationRepository;

	private PrePublicationFactory prePublicationFactory;

	private AuthorshipRepository authorshipRepository;

	private PersonService personService;

	private PersonRepository personRepository;

	private JournalRepository journalRepository;

	private PersonNameParser nameParser;

	private BibTeX bibtex;

	private HtmlDocumentExporter html;

	private OpenDocumentTextExporter odt;

	private JsonExporter json;
	
	private DownloadableFileManager fileManager;

	private PublicationService test;
	
	private MembershipService membershipService;

	private BookService bookService;

	private BookChapterService bookChapterService;

	private ConferencePaperService conferencePaperService;

	private JournalEditionService journalEditionService;

	private JournalPaperService journalPaperService;

	private KeyNoteService keyNoteService;

	private MiscDocumentService miscDocumentService;

	private PatentService patentService;

	private ReportService reportService;

	private ThesisService thesisService;

	@BeforeEach
	public void setUp() {
		this.fileManager = mock(DownloadableFileManager.class);
		this.messages = mock(MessageSourceAccessor.class);
		this.prePublicationFactory = mock(PrePublicationFactory.class);
		this.publicationRepository = mock(PublicationRepository.class);
		this.authorshipRepository = mock(AuthorshipRepository.class);
		this.personService = mock(PersonService.class);
		this.personRepository = mock(PersonRepository.class);
		this.journalRepository = mock(JournalRepository.class);
		this.nameParser = new DefaultPersonNameParser();
		this.bibtex = mock(BibTeX.class);
		this.html = mock(HtmlDocumentExporter.class);
		this.odt = mock(OpenDocumentTextExporter.class);
		this.json = mock(JsonExporter.class);
		this.membershipService = mock(MembershipService.class);
		this.bookService = mock(BookService.class);
		this.bookChapterService = mock(BookChapterService.class);
		this.conferencePaperService = mock(ConferencePaperService.class);
		this.journalEditionService = mock(JournalEditionService.class);
		this.journalPaperService = mock(JournalPaperService.class);
		this.keyNoteService = mock(KeyNoteService.class);
		this.miscDocumentService = mock(MiscDocumentService.class);
		this.patentService = mock(PatentService.class);
		this.reportService = mock(ReportService.class);
		this.thesisService = mock(ThesisService.class);
		this.test = new PublicationService(this.messages, new Constants(), this.publicationRepository, this.prePublicationFactory,
				this.authorshipRepository,
				this.personService, this.personRepository,
				this.journalRepository, this.nameParser, this.bibtex, this.html, this.odt, this.json, this.fileManager,
				this.membershipService,
				this.bookService, this.bookChapterService, this.conferencePaperService,
				this.journalEditionService, this.journalPaperService, this.keyNoteService,
				this.miscDocumentService, this.patentService, this.reportService,
				this.thesisService);

		// Prepare some publications to be inside the repository
		// The lenient configuration is used to configure the mocks for all the tests
		// at the same code location for configuration simplicity
		this.pub0 = mock(Publication.class, "pub0");
		lenient().when(this.pub0.getId()).thenReturn(123);
		this.pub1 = mock(Publication.class, "pub1");
		lenient().when(this.pub1.getId()).thenReturn(234);
		this.pub2 = mock(Publication.class, "pub2");
		lenient().when(this.pub2.getId()).thenReturn(345);

		lenient().when(this.publicationRepository.findAll()).thenReturn(
				Arrays.asList(this.pub0, this.pub1, this.pub2));
		lenient().when(this.publicationRepository.findById(anyInt())).then(it -> {
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
	}

	@Test
	public void getAllPublications() {
		final List<Publication> list = this.test.getAllPublications();
		assertNotNull(list);
		assertEquals(3, list.size());
		assertSame(this.pub0, list.get(0));
		assertSame(this.pub1, list.get(1));
		assertSame(this.pub2, list.get(2));
	}

	@Test
	public void getPublicationById() {
		assertNull(this.test.getPublicationById(-4756));
		assertNull(this.test.getPublicationById(0));
		assertSame(this.pub0, this.test.getPublicationById(123));
		assertSame(this.pub1, this.test.getPublicationById(234));
		assertSame(this.pub2, this.test.getPublicationById(345));
		assertNull(this.test.getPublicationById(7896));
	}

	@Test
	public void getPublicationsByTitle() {
		final List<Publication> expected = Arrays.asList(this.pub0, this.pub2);
		when(this.publicationRepository.findAllByTitleIgnoreCase(anyString())).then(it -> expected);

		List<Publication> set = this.test.getPublicationsByTitle("xyz");
		assertSame(expected, set);
	}

	@Test
	public void removePublication() {
		this.test.removePublication(234, false);

		final ArgumentCaptor<Integer> arg = ArgumentCaptor.forClass(Integer.class);

		verify(this.publicationRepository, atLeastOnce()).findById(eq(234));
		verify(this.publicationRepository, atLeastOnce()).deleteById(eq(234));
		
		verifyNoMoreInteractions(this.publicationRepository);
	}

	@Test
	public void save() {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(1234);
		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(2345);
		doReturn(Arrays.asList(pers0)).when(this.pub0).getTemporaryAuthors();
		doReturn(Arrays.asList(pers1)).when(this.pub1).getTemporaryAuthors();
		
		when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch ((Integer) it.getArgument(0)) {
			case 1234:
				return Optional.of(pers0);
			case 2345:
				return Optional.of(pers1);
			}
			return Optional.empty();
		});

		this.test.save(pub0, pub1);

		verify(pub0).setTemporaryAuthors(isNull());
		verify(pub1).setTemporaryAuthors(isNull());

		verify(this.publicationRepository, atLeastOnce()).save(same(pub0));
		verify(this.publicationRepository, atLeastOnce()).save(same(pub1));

		verify(this.personRepository, atLeastOnce()).save(same(pers0));
		verify(this.personRepository, atLeastOnce()).save(same(pers1));

		ArgumentCaptor<Authorship> arg0 = ArgumentCaptor.forClass(Authorship.class);
		verify(this.authorshipRepository, times(2)).save(arg0.capture());
		assertAuthorship(arg0, 1234, 123, 0);
		assertAuthorship(arg0, 2345, 234, 0);

		verifyNoInteractions(this.journalRepository);
	}

	private void assertAuthorship(ArgumentCaptor<Authorship> authorships, int personId, int publicationId, int rank) {
		for (final Authorship aut : authorships.getAllValues()) {
			int pid = aut.getPerson().getId();
			int pubid = aut.getPublication().getId();
			int r = aut.getAuthorRank();
			if (personId == pid && publicationId == pubid && rank == r) {
				return;
			}
		}
		fail("Authorship not found");
	}

	private void assertAuthorship(Authorship authorship, int personId, int publicationId, int rank) {
		assertEquals(personId, authorship.getPerson().getId(), "Invalid person");
		assertEquals(publicationId, authorship.getPublication().getId(), "Invalid publication");
		assertEquals(rank, authorship.getAuthorRank(), "Invalid rank");
	}

	@Test
	public void importPublications_null() throws Exception {
		List<Integer> ids = this.test.importPublications(null, null, false);
		assertNotNull(ids);
		assertTrue(ids.isEmpty());
	}

	@Test
	public void importPublications_empty() throws Exception {
		Reader rd = new StringReader("");
		List<Integer> ids = this.test.importPublications(rd, null, false);
		assertNotNull(ids);
		assertTrue(ids.isEmpty());
	}

	@Test
	public void importPublications_0() throws Exception {
		String bibtex = "--valid-bibtex--";
		Person a0 = mock(Person.class);
		when(a0.getId()).thenReturn(1234);
		when(a0.getFirstName()).thenReturn("Fa0");
		when(a0.getLastName()).thenReturn("La0");
		Person a1 = mock(Person.class);
		when(a1.getId()).thenReturn(2345);
		when(a1.getFirstName()).thenReturn("Fa1");
		when(a1.getLastName()).thenReturn("La1");
		Person a2 = mock(Person.class);
		when(a2.getId()).thenReturn(3456);
		when(a2.getFirstName()).thenReturn("Fa2");
		when(a2.getLastName()).thenReturn("La2");
		Publication p0 = mock(Publication.class);
		when(p0.getId()).thenReturn(987);
		when(p0.getAuthors()).thenReturn(Arrays.asList(a0, a1));
		Publication p1 = mock(Publication.class);
		when(p1.getId()).thenReturn(874);
		when(p1.getAuthors()).thenReturn(Arrays.asList(a1, a2));
		when(this.bibtex.extractPublications(any(Reader.class), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(Arrays.asList(p0, p1));
		when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch ((Integer) it.getArgument(0)) {
			case 1234:
				return Optional.of(a0);
			case 2345:
				return Optional.of(a1);
			case 3456:
				return Optional.of(a2);
			}
			return Optional.empty();
		});
		when(this.personRepository.findByAuthorshipsPublicationIdOrderByAuthorshipsAuthorRank(anyInt())).thenReturn(Collections.singletonList(null));

		lenient().when(this.publicationRepository.findById(anyInt())).then(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 123:
				return Optional.of(this.pub0);
			case 234:
				return Optional.of(this.pub1);
			case 345:
				return Optional.of(this.pub2);
			case 987:
				return Optional.of(p0);
			case 874:
				return Optional.of(p1);
			}
			return Optional.empty();
		});

		List<Integer> ids = this.test.importPublications(new StringReader(bibtex), null, false);

		assertNotNull(ids);
		assertEquals(2, ids.size());
		assertEquals(987, ids.get(0));
		assertEquals(874, ids.get(1));

		verify(this.publicationRepository, atLeastOnce()).save(same(p0));
		verify(this.publicationRepository, atLeastOnce()).save(same(p1));

		verify(this.personRepository, atLeastOnce()).save(same(a1));

		verify(p0, atLeastOnce()).setTemporaryAuthors(same(null));

		verify(p1, atLeastOnce()).setTemporaryAuthors(same(null));

		verify( this.personRepository, atLeastOnce()).findByAuthorshipsPublicationIdOrderByAuthorshipsAuthorRank(eq(874));
		
		ArgumentCaptor<Authorship> arg0 = ArgumentCaptor.forClass(Authorship.class);
		verify(this.authorshipRepository, atLeastOnce()).save(arg0.capture());
		assertAuthorship(arg0, 1234, 987, 0);
		assertAuthorship(arg0, 2345, 987, 1);
		assertAuthorship(arg0, 2345, 874, 0);
		assertAuthorship(arg0, 3456, 874, 1);
	}

	@Test
	public void exportBibTeX_Collection_null() {
		String bibtex = this.test.exportBibTeX((Collection<Publication>) null, new ExporterConfigurator(mock(JournalService.class)));
		assertNull(bibtex);
	}

	@Test
	public void exportBibTeX_Collection() {
		when(this.bibtex.exportPublications(any(Iterable.class), any())).thenReturn("abc");
		Collection<Publication> pubs = Arrays.asList(this.pub0, this.pub2);

		String bibtex = this.test.exportBibTeX(pubs, new ExporterConfigurator(mock(JournalService.class)));

		assertEquals("abc", bibtex);

		ArgumentCaptor<Iterable> arg = ArgumentCaptor.forClass(Iterable.class);
		verify(this.bibtex, only()).exportPublications(arg.capture(), any());
		Iterable<Publication> it = arg.getValue();
		assertNotNull(it);
		Iterator<Publication> iterator = it.iterator();
		assertSame(this.pub0, iterator.next());
		assertSame(this.pub2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void exportHtml_Collection_null() throws Exception {
		ExporterConfigurator configurator = new ExporterConfigurator(mock(JournalService.class));
		String html = this.test.exportHtml((Collection<Publication>) null, configurator);
		assertNull(html);
	}

	@Test
	public void exportHtml_Collection() throws Exception {
		ExporterConfigurator configurator = new ExporterConfigurator(mock(JournalService.class));
		when(this.html.exportPublications(any(Iterable.class), any())).thenReturn("abc");
		Collection<Publication> pubs = Arrays.asList(this.pub0, this.pub2);

		String html = this.test.exportHtml(pubs, configurator);

		assertEquals("abc", html);

		ArgumentCaptor<Iterable> arg0 = ArgumentCaptor.forClass(Iterable.class);
		ArgumentCaptor<ExporterConfigurator> arg1 = ArgumentCaptor.forClass(ExporterConfigurator.class);
		verify(this.html, only()).exportPublications(arg0.capture(), arg1.capture());
		Iterable<Publication> it = arg0.getValue();
		assertNotNull(it);
		Iterator<Publication> iterator = it.iterator();
		assertSame(this.pub0, iterator.next());
		assertSame(this.pub2, iterator.next());
		assertFalse(iterator.hasNext());
		assertNotNull(arg1.getValue());
	}

	@Test
	public void exportOdt_Collection_null() throws Exception {
		ExporterConfigurator configurator = new ExporterConfigurator(mock(JournalService.class));
		byte[] odt = this.test.exportOdt((Collection<Publication>) null, configurator);
		assertNull(odt);
	}

	@Test
	public void exportOdt_Collection() throws Exception {
		ExporterConfigurator configurator = new ExporterConfigurator(mock(JournalService.class));
		when(this.odt.exportPublications(any(Iterable.class), any())).thenReturn("abc".getBytes());
		Collection<Publication> pubs = Arrays.asList(this.pub0, this.pub2);

		byte[] odt = this.test.exportOdt(pubs, configurator);

		assertEquals("abc", new String(odt));

		ArgumentCaptor<Iterable> arg0 = ArgumentCaptor.forClass(Iterable.class);
		ArgumentCaptor<ExporterConfigurator> arg1 = ArgumentCaptor.forClass(ExporterConfigurator.class);
		verify(this.odt, only()).exportPublications(arg0.capture(), arg1.capture());
		Iterable<Publication> it = arg0.getValue();
		assertNotNull(it);
		Iterator<Publication> iterator = it.iterator();
		assertSame(this.pub0, iterator.next());
		assertSame(this.pub2, iterator.next());
		assertFalse(iterator.hasNext());
		assertNotNull(arg1.getValue());
	}

	@Test
	public void exportJson_Collection_null() throws Exception {
		ExporterConfigurator configurator = new ExporterConfigurator(mock(JournalService.class));
		String html = this.test.exportJson((Collection<Publication>) null, configurator);
		assertNull(html);
	}

	@Test
	public void exportJson_Collection() throws Exception {
		ExporterConfigurator configurator = new ExporterConfigurator(mock(JournalService.class));
		when(this.json.exportPublicationsWithRootKeys(any(Iterable.class), any(), any())).thenReturn("abc");
		Collection<Publication> pubs = Arrays.asList(this.pub0, this.pub2);

		String json = this.test.exportJson(pubs, configurator);

		assertEquals("abc", json);

		ArgumentCaptor<Iterable> arg0 = ArgumentCaptor.forClass(Iterable.class);
		ArgumentCaptor<ExporterConfigurator> arg1 = ArgumentCaptor.forClass(ExporterConfigurator.class);
		verify(this.json, only()).exportPublicationsWithRootKeys(arg0.capture(), arg1.capture(), any());
		Iterable<Publication> it = arg0.getValue();
		assertNotNull(it);
		Iterator<Publication> iterator = it.iterator();
		assertSame(this.pub0, iterator.next());
		assertSame(this.pub2, iterator.next());
		assertFalse(iterator.hasNext());
		assertNotNull(arg1.getValue());
	}

	@Test
	public void exportJsonAsTree_Collection_null() throws Exception {
		ExporterConfigurator configurator = new ExporterConfigurator(mock(JournalService.class));
		JsonNode json = this.test.exportJsonAsTree((Collection<Publication>) null, configurator, null);
		assertNull(json);
	}

	@Test
	public void exportJsonAsTree_Collection() throws Exception {
		ExporterConfigurator configurator = new ExporterConfigurator(mock(JournalService.class));
		JsonNode root = mock(JsonNode.class);
		when(this.json.exportPublicationsAsTreeWithRootKeys(any(Iterable.class), any(), any())).thenReturn(root);
		Collection<Publication> pubs = Arrays.asList(this.pub0, this.pub2);

		JsonNode json = this.test.exportJsonAsTree(pubs, configurator, null);

		assertSame(root, json);

		ArgumentCaptor<Iterable> arg0 = ArgumentCaptor.forClass(Iterable.class);
		ArgumentCaptor<ExporterConfigurator> arg1 = ArgumentCaptor.forClass(ExporterConfigurator.class);
		ArgumentCaptor<Procedure2> arg2 = ArgumentCaptor.forClass(Procedure2.class);
		verify(this.json, only()).exportPublicationsAsTreeWithRootKeys(arg0.capture(), arg1.capture(), arg2.capture(), any());
		Iterable<Publication> it = arg0.getValue();
		assertNotNull(it);
		Iterator<Publication> iterator = it.iterator();
		assertSame(this.pub0, iterator.next());
		assertSame(this.pub2, iterator.next());
		assertFalse(iterator.hasNext());
		assertNotNull(arg1.getValue());
		assertNull(arg2.getValue());
	}

	@Test
	public void getPublicationsByPersonId() {
		when(this.publicationRepository.findAllByAuthorshipsPersonId(anyInt())).thenReturn(
				Arrays.asList(this.pub0, this.pub1));

		final List<Publication> list = this.test.getPublicationsByPersonId(2345);
		assertNotNull(list);
		assertEquals(2, list.size());
		assertTrue(list.contains(this.pub0));
		assertTrue(list.contains(this.pub1));
	}

	@Test
	public void getPublicationsByOrganizationId() {
		when(this.publicationRepository.findAllByAuthorshipsPersonIdIn(any())).thenReturn(
				new HashSet<>(Arrays.asList(this.pub0, this.pub2)));

		final Set<Publication> list = this.test.getPublicationsByOrganizationId(2345, false, false);
		assertNotNull(list);
		assertEquals(2, list.size());
		assertTrue(list.contains(this.pub0));
		assertTrue(list.contains(this.pub2));
	}

	@Test
	public void getPublicationsByIds() {
		when(this.publicationRepository.findAllById(any())).thenReturn(
				Arrays.asList(this.pub0, this.pub2));

		final List<Publication> list = this.test.getPublicationsByIds(Arrays.asList(2345, 3456));
		assertNotNull(list);
		assertEquals(2, list.size());
		assertTrue(list.contains(this.pub0));
		assertTrue(list.contains(this.pub2));
	}

	@Test
	public void getAuthorsFor() {
		Person pers0 = mock(Person.class);
		Person pers1 = mock(Person.class);

		when(this.personRepository.findByAuthorshipsPublicationIdOrderByAuthorshipsAuthorRank(anyInt())).thenAnswer(it -> {
			switch ((Integer) it.getArgument(0)) {
			case 1234:
				return Arrays.asList(pers1, pers0);
			case 2345:
				return Collections.singletonList(pers1);
			}
			return Collections.emptyList();
		});

		final List<Person> list0 = this.test.getAuthorsFor(0);
		assertTrue(list0.isEmpty());

		final List<Person> list1 = this.test.getAuthorsFor(1234);
		assertEquals(2, list1.size());
		assertSame(pers1, list1.get(0));
		assertSame(pers0, list1.get(1));

		final List<Person> list2 = this.test.getAuthorsFor(2345);
		assertEquals(1, list2.size());
		assertTrue(list2.contains(pers1));
	}

	@Test
	public void addAuthorship_IntIntInt_noAuthorship() {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(1234);
		when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch ((Integer) it.getArgument(0)) {
			case 1234:
				return Optional.of(pers0);
			}
			return Optional.empty();
		});

		Set<Authorship> rawAuthorships = new HashSet<>();
		when(this.pub0.getAuthorshipsRaw()).thenReturn(rawAuthorships);

		final Authorship autship = this.test.addAuthorship(1234, 123, 1, true);

		assertNotNull(autship);
		assertAuthorship(autship, 1234, 123, 0);
		
		assertEquals(1, rawAuthorships.size());
		assertTrue(rawAuthorships.contains(autship));

		verify(this.authorshipRepository, times(1)).save(same(autship));
	}

	@Test
	public void addAuthorship_IntIntInt_alreadyInsideAuthorship() {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(1234);
		when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch ((Integer) it.getArgument(0)) {
			case 1234:
				return Optional.of(pers0);
			}
			return Optional.empty();
		});

		Set<Authorship> rawAuthorships = new HashSet<>();
		when(this.pub0.getAuthorshipsRaw()).thenReturn(rawAuthorships);

		Authorship existingAuthorship = mock(Authorship.class);
		when(existingAuthorship.getPerson()).thenReturn(pers0);
		rawAuthorships.add(existingAuthorship);

		final Authorship autship = this.test.addAuthorship(1234, 123, 1, true);

		assertNull(autship);
		
		assertEquals(1, rawAuthorships.size());

		verify(this.authorshipRepository, never()).save(any());
	}

	@Test
	public void addAuthorship_IntIntInt_newAuthorship_insertAsFirstAuthor() {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(1234);
		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(2345);
		when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch ((Integer) it.getArgument(0)) {
			case 1234:
				return Optional.of(pers0);
			case 2345:
				return Optional.of(pers1);
			}
			return Optional.empty();
		});
		
		Set<Authorship> rawAuthorships = new HashSet<>();
		when(this.pub1.getAuthorshipsRaw()).thenReturn(rawAuthorships);

		Authorship existingAuthorship = mock(Authorship.class);
		when(existingAuthorship.getPerson()).thenReturn(pers1);
		when(existingAuthorship.getAuthorRank()).thenReturn(0);
		rawAuthorships.add(existingAuthorship);

		final Authorship autship = this.test.addAuthorship(1234, 234, 0, true);
		assertNotNull(autship);
		assertAuthorship(autship, 1234, 234, 0);
		
		verify(existingAuthorship).setAuthorRank(eq(1));

		final ArgumentCaptor<Authorship> arg = ArgumentCaptor.forClass(Authorship.class);
		verify(this.authorshipRepository, atLeastOnce()).save(same(autship));
	}

	@Test
	public void addAuthorship_IntIntInt_newAuthorship_insertAsSecondAuthor() {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(1234);
		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(2345);
		when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch ((Integer) it.getArgument(0)) {
			case 1234:
				return Optional.of(pers0);
			case 2345:
				return Optional.of(pers1);
			}
			return Optional.empty();
		});
		
		Set<Authorship> rawAuthorships = new HashSet<>();
		when(this.pub1.getAuthorshipsRaw()).thenReturn(rawAuthorships);

		Authorship existingAuthorship = mock(Authorship.class);
		when(existingAuthorship.getPerson()).thenReturn(pers1);
		when(existingAuthorship.getAuthorRank()).thenReturn(0);
		rawAuthorships.add(existingAuthorship);
		
		final Authorship autship = this.test.addAuthorship(1234, 234, 1, true);
		assertNotNull(autship);
		assertAuthorship(autship, 1234, 234, 1);

		verify(this.authorshipRepository, atLeastOnce()).save(same(autship));
	}

	@Test
	public void addAuthorship_IntIntInt_newAuthorship_insertWithOutRangedRank() {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(1234);
		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(2345);
		when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch ((Integer) it.getArgument(0)) {
			case 1234:
				return Optional.of(pers0);
			case 2345:
				return Optional.of(pers1);
			}
			return Optional.empty();
		});
		
		Set<Authorship> rawAuthorships = new HashSet<>();
		when(this.pub1.getAuthorshipsRaw()).thenReturn(rawAuthorships);

		Authorship existingAuthorship = mock(Authorship.class);
		when(existingAuthorship.getPerson()).thenReturn(pers1);
		rawAuthorships.add(existingAuthorship);

		final Authorship autship = this.test.addAuthorship(1234, 234, 100, true);
		assertNotNull(autship);
		assertAuthorship(autship, 1234, 234, 1);

		verify(this.authorshipRepository, atLeastOnce()).save(same(autship));
	}

}
