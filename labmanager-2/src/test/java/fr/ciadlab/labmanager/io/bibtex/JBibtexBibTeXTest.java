/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Universite de Technologie
 * de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.io.bibtex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.Book;
import fr.ciadlab.labmanager.entities.publication.type.BookChapter;
import fr.ciadlab.labmanager.entities.publication.type.ConferencePaper;
import fr.ciadlab.labmanager.entities.publication.type.JournalEdition;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.entities.publication.type.KeyNote;
import fr.ciadlab.labmanager.entities.publication.type.MiscDocument;
import fr.ciadlab.labmanager.entities.publication.type.Patent;
import fr.ciadlab.labmanager.entities.publication.type.Report;
import fr.ciadlab.labmanager.entities.publication.type.Thesis;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.bibtex.bugfix.BugfixLaTeXPrinter;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.PrePublicationFactory;
import fr.ciadlab.labmanager.service.publication.type.BookChapterService;
import fr.ciadlab.labmanager.service.publication.type.BookService;
import fr.ciadlab.labmanager.service.publication.type.ConferencePaperService;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.service.publication.type.MiscDocumentService;
import fr.ciadlab.labmanager.service.publication.type.ReportService;
import fr.ciadlab.labmanager.service.publication.type.ThesisService;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.Resources;
import org.jbibtex.LaTeXObject;
import org.jbibtex.LaTeXParser;
import org.jbibtex.LaTeXPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link JBibtexBibTeX}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class JBibtexBibTeXTest {

	private MessageSourceAccessor messages;

	private PrePublicationFactory prePublicationFactory;

	private JournalService journalService;

	private PersonService personService;

	private BookService bookService;

	private BookChapterService bookChapterService;

	private ConferencePaperService conferencePaperService;

	private JournalPaperService journalPaperService;

	private MiscDocumentService miscDocumentService;

	private ReportService reportService;

	private ThesisService thesisService;

	private JBibtexBibTeX test;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getStaticMessageSourceAccessor();
		this.prePublicationFactory = mock(PrePublicationFactory.class);
		this.journalService = mock(JournalService.class);
		this.personService = mock(PersonService.class);
		this.bookService = mock(BookService.class);
		this.bookChapterService = mock(BookChapterService.class);
		this.conferencePaperService = mock(ConferencePaperService.class);
		this.journalPaperService = mock(JournalPaperService.class);
		this.miscDocumentService = mock(MiscDocumentService.class);
		this.reportService = mock(ReportService.class);
		this.thesisService = mock(ThesisService.class);
		this.test = new JBibtexBibTeX(
				this.messages,
				this.prePublicationFactory,
				this.journalService,
				this.personService,
				this.bookService,
				this.bookChapterService,
				this.conferencePaperService,
				this.journalPaperService,
				this.miscDocumentService,
				this.reportService,
				this.thesisService);
	}

	/** This test is defined for validating that a bug is still present in the JBibtex library.
	 * It enables to know if the class {@link BugfixLaTeXPrinter} should be removed or not.
	 * If this test fails, it means that the JBibtex library is fixed.
	 */
	@Test
	public void validateBugInJBibtexLibrary() throws Exception {
		final LaTeXParser latexParser = new LaTeXParser();
		List<LaTeXObject> latexObjects = latexParser.parse("\\^A");
		final LaTeXPrinter latexPrinter = new LaTeXPrinter();
		final String plainTextString = latexPrinter.print(latexObjects);
		assertEquals("A", plainTextString);
	}

	@Test
	public void parseTeXString_null() throws Exception {
		assertNull(this.test.parseTeXString(null));
	}

	@Test
	public void parseTeXString_empty() throws Exception {
		assertNull(this.test.parseTeXString(""));
	}

	@Test
	public void parseTeXString_acute() throws Exception {
		assertEquals("é", this.test.parseTeXString("\\'e"));
		assertEquals("é", this.test.parseTeXString("\\'{e}"));
		assertEquals("é", this.test.parseTeXString("{\\'e}"));
		assertEquals("é", this.test.parseTeXString("{\\'{e}}"));

		assertEquals("É", this.test.parseTeXString("\\'E"));
		assertEquals("É", this.test.parseTeXString("\\'{E}"));
		assertEquals("É", this.test.parseTeXString("{\\'E}"));
		assertEquals("É", this.test.parseTeXString("{\\'{E}}"));
	}

	@Test
	public void parseTeXString_grave() throws Exception {
		assertEquals("è", this.test.parseTeXString("\\`e"));
		assertEquals("è", this.test.parseTeXString("\\`{e}"));
		assertEquals("è", this.test.parseTeXString("{\\`e}"));
		assertEquals("è", this.test.parseTeXString("{\\`{e}}"));

		assertEquals("È", this.test.parseTeXString("\\`E"));
		assertEquals("È", this.test.parseTeXString("\\`{E}"));
		assertEquals("È", this.test.parseTeXString("{\\`E}"));
		assertEquals("È", this.test.parseTeXString("{\\`{E}}"));
	}

	@Test
	public void parseTeXString_circumflex() throws Exception {
		assertEquals("ê", this.test.parseTeXString("\\^e"));
		assertEquals("ê", this.test.parseTeXString("\\^{e}"));
		assertEquals("ê", this.test.parseTeXString("{\\^e}"));
		assertEquals("ê", this.test.parseTeXString("{\\^{e}}"));

		assertEquals("Ê", this.test.parseTeXString("\\^E"));
		assertEquals("Ê", this.test.parseTeXString("\\^{E}"));
		assertEquals("Ê", this.test.parseTeXString("{\\^E}"));
		assertEquals("Ê", this.test.parseTeXString("{\\^{E}}"));
	}

	@Test
	public void parseTeXString_diaeresis() throws Exception {
		assertEquals("ë", this.test.parseTeXString("\\\"e"));
		assertEquals("ë", this.test.parseTeXString("\\\"{e}"));
		assertEquals("ë", this.test.parseTeXString("{\\\"e}"));
		assertEquals("ë", this.test.parseTeXString("{\\\"{e}}"));

		assertEquals("Ë", this.test.parseTeXString("\\\"E"));
		assertEquals("Ë", this.test.parseTeXString("\\\"{E}"));
		assertEquals("Ë", this.test.parseTeXString("{\\\"E}"));
		assertEquals("Ë", this.test.parseTeXString("{\\\"{E}}"));
	}

	@Test
	public void parseTeXString_tilde() throws Exception {
		assertEquals("ã", this.test.parseTeXString("\\~a"));
		assertEquals("ã", this.test.parseTeXString("\\~{a}"));
		assertEquals("ã", this.test.parseTeXString("{\\~a}"));
		assertEquals("ã", this.test.parseTeXString("{\\~{a}}"));

		assertEquals("Ã", this.test.parseTeXString("\\~A"));
		assertEquals("Ã", this.test.parseTeXString("\\~{A}"));
		assertEquals("Ã", this.test.parseTeXString("{\\~A}"));
		assertEquals("Ã", this.test.parseTeXString("{\\~{A}}"));
	}

	@Test
	public void parseTeXString_cedil() throws Exception {
		assertEquals("ç", this.test.parseTeXString("\\c{c}"));
		assertEquals("ç", this.test.parseTeXString("{\\c{c}}"));

		assertEquals("Ç", this.test.parseTeXString("\\c{C}"));
		assertEquals("Ç", this.test.parseTeXString("{\\c{C}}"));
	}

	@Test
	public void parseTeXString_i_accent() throws Exception {
		assertEquals("î", this.test.parseTeXString("\\^i"));
		assertEquals("î", this.test.parseTeXString("\\^{i}"));
		assertEquals("î", this.test.parseTeXString("{\\^i}"));
		assertEquals("î", this.test.parseTeXString("{\\^{i}}"));
		assertEquals("î", this.test.parseTeXString("\\^{\\i}"));
		assertEquals("î", this.test.parseTeXString("{\\^{\\i}}"));

		assertEquals("Î", this.test.parseTeXString("\\^I"));
		assertEquals("Î", this.test.parseTeXString("\\^{I}"));
		assertEquals("Î", this.test.parseTeXString("{\\^I}"));
		assertEquals("Î", this.test.parseTeXString("{\\^{I}}"));
		assertEquals("Î", this.test.parseTeXString("\\^{\\I}"));
		assertEquals("Î", this.test.parseTeXString("{\\^{\\I}}"));
	}

	@Test
	public void parseTeXString_string_macro() throws Exception {
		assertEquals(".", this.test.parseTeXString("\\string."));
	}

	@Test
	public void parseTeXString_texBlocks() throws Exception {
		assertEquals("This IS a text", this.test.parseTeXString("This {IS a} text"));
	}

	@Test
	public void protectAcronymsInText_null() {
		assertNull(this.test.protectAcronymsInText(null));
	}

	@Test
	public void protectAcronymsInText_empty() {
		assertNull(this.test.protectAcronymsInText(""));
	}

	@Test
	public void protectAcronymsInText() {
		assertEquals("This {IS} a tEXt {ACRO}", this.test.protectAcronymsInText("This IS a tEXt ACRO"));
		assertEquals("This {ISs} a tEXt {ACRO}", this.test.protectAcronymsInText("This ISs a tEXt ACRO"));
	}

	private Stream<Publication> getPublicationStreamFromTest(String filename) throws Exception {
		URL url = Resources.getResource(JBibtexBibTeXTest.class.getPackageName().replaceAll("\\.", "/") + "/" + filename);
		try (Reader r = new InputStreamReader(url.openStream())) {
			return this.test.getPublicationStreamFrom(r, false, false, false, false);
		}
	}

	@Test
	public void getPublicationStreamFrom_0() throws Exception {
		Stream<Publication> pubs = getPublicationStreamFromTest("bibtex_0.bib");
		assertNotNull(pubs);
		List<Publication> list = pubs.collect(Collectors.toList());
		assertTrue(list.isEmpty());
	}

	@Test
	public void getPublicationStreamFrom_n() throws Exception {
		when(this.prePublicationFactory.createPrePublication(
				any(), any(), any(), any(), any(), anyInt(), any(), any(), any(),
				any(), any(), any(), any(), any(), any(), any())).thenAnswer(it -> {
					Publication fake = mock(Publication.class);
					return fake;		
				});

		JournalPaper jp = mock(JournalPaper.class);
		when(this.journalPaperService.createJournalPaper(
				any(), any(), any(), any(), any(), any(), anyBoolean())).thenReturn(jp);

		ConferencePaper cp = mock(ConferencePaper.class);
		when(this.conferencePaperService.createConferencePaper(
				any(), any(), any(), any(), any(), any(), any(),
				any(), any(), any(), anyBoolean())).thenReturn(cp);

		Journal journal = mock(Journal.class);
		when(this.journalService.getJournalsByName(any())).thenReturn(Collections.singleton(journal));

		Person p0 = mock(Person.class);
		Person p1 = mock(Person.class);
		when(this.personService.extractPersonsFrom(any(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(Arrays.asList(p0, p1));

		Stream<Publication> pubs = getPublicationStreamFromTest("bibtex_n.bib");
		assertNotNull(pubs);
		List<Publication> list = pubs.collect(Collectors.toList());
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());

		Publication p;

		p = list.get(0);
		assertTrue(p instanceof JournalPaper);
		verify(this.prePublicationFactory, atLeastOnce()).createPrePublication(
				eq(PublicationType.INTERNATIONAL_JOURNAL_PAPER),
				eq("On the Use of Clustering Algorithms in Medical Domain"),
				isNull(),
				eq("Clustering, nasopharyngeal cancer, medical data"),
				eq(LocalDate.parse("2019-10-01")),
				eq(2019),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				eq(PublicationLanguage.ENGLISH));
		verify(this.journalPaperService).createJournalPaper(
				any(),
				eq("17"),
				eq("2"),
				isNull(),
				isNull(),
				same(journal),
				eq(false));
		verify(p).setTemporaryAuthors(any());
		verify(this.personService).extractPersonsFrom(
				eq("Rehioui, Hajjar and Idrissi, Abdellah and Koukam, Abderrafiaa"),
				eq(true),
				eq(false),
				eq(false));

		p = list.get(1);
		assertTrue(p instanceof ConferencePaper);
		verify(this.prePublicationFactory).createPrePublication(
				eq(PublicationType.INTERNATIONAL_CONFERENCE_PAPER),
				eq("Deploiement d?un systeme de detection automatise des situations a risque de decompensation de comorbidites."),
				eq("Deploiement d?un systeme de detection automatise des situations a risque de decompensation de comorbidites."),
				eq("telemedecine, ontologies, insuffisants cardiaques, esante,"),
				eq(LocalDate.parse("2015-04-01")),
				eq(2015),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				eq(PublicationLanguage.ENGLISH));
		verify(this.conferencePaperService).createConferencePaper(
				any(),
				eq("Communication au Congres de la Societe Française d?Hematologie, Paris"),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				isNull(),
				anyBoolean());
		verify(p).setTemporaryAuthors(any());
		verify(this.personService).extractPersonsFrom(
				eq("Andres, Emmanuel and Talha, Samy"),
				eq(true),
				eq(false),
				eq(false));
	}

	private String lines(String... lines) {
		final StringBuilder b = new StringBuilder();
		for (final String line : lines) {
			if (b.length() > 0) {
				b.append("\n");
			}
			b.append(Strings.nullToEmpty(line));
		}
		return b.toString();
	}

	private void preparePublicationForExportTest(Publication pub, PublicationType type) {
		Person p0 = mock(Person.class);
		lenient().when(p0.getFirstName()).thenReturn("Firstname0");
		lenient().when(p0.getLastName()).thenReturn("Lastname0");

		Person p1 = mock(Person.class);
		lenient().when(p1.getFirstName()).thenReturn("Firstname1");
		lenient().when(p1.getLastName()).thenReturn("Lastname1");

		lenient().when(pub.getAbstractText()).thenReturn("Abs 1");
		lenient().when(pub.getAuthors()).thenReturn(Arrays.asList(p1, p0));
		lenient().when(pub.getCategory()).thenCallRealMethod();
		lenient().when(pub.getDblpURL()).thenReturn("DBLP/1");
		lenient().when(pub.getDOI()).thenReturn("doi/1");
		lenient().when(pub.getExtraURL()).thenReturn("url/1");
		lenient().when(pub.getISBN()).thenReturn("isbn/1");
		lenient().when(pub.getISSN()).thenReturn("issn/1");
		lenient().when(pub.getKeywords()).thenReturn("keyword 1, keyword 2");
		lenient().when(pub.getMajorLanguage()).thenReturn(PublicationLanguage.ENGLISH);
		lenient().when(pub.getPathToDownloadableAwardCertificate()).thenReturn("path/to/award");
		lenient().when(pub.getPathToDownloadablePDF()).thenReturn("path/to/pdf");
		lenient().when(pub.getPublicationDate()).thenReturn(LocalDate.parse("2022-07-24"));
		lenient().when(pub.getPublicationYear()).thenReturn(2022);
		lenient().when(pub.getTitle()).thenReturn("Title 1");
		lenient().when(pub.getType()).thenReturn(type);
		lenient().when(pub.getVideoURL()).thenReturn("video/1");
	}

	@Test
	public void exportPublications_journalPaper() {
		Journal journal = mock(Journal.class);
		when(journal.getJournalName()).thenReturn("journal name/1");
		when(journal.getPublisher()).thenReturn("publisher/1");
		JournalPaper pub = mock(JournalPaper.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_JOURNAL_PAPER);
		when(pub.getPreferredStringId()).thenReturn("JournalPaper_123");
		when(pub.getImpactFactor()).thenReturn(123.456f);
		when(pub.getJournal()).thenReturn(journal);
		when(pub.getVolume()).thenReturn("vol/1");
		when(pub.getNumber()).thenReturn("nb/1");
		when(pub.getPages()).thenReturn("pages/1");
		when(pub.getScimagoQIndex()).thenReturn(QuartileRanking.Q2);

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"@article{JournalPaper_123,",
				"	title = {Title 1},",
				"	author = {Lastname1, Firstname1 and Lastname0, Firstname0},",
				"	year = 2022,",
				"	month = jul,",
				"	doi = {doi/1},",
				"	isbn = {isbn/1},",
				"	issn = {issn/1},",
				"	url = {url/1},",
				"	dblp = {DBLP/1},",
				"	_video = {video/1},",
				"	abstract = {Abs 1},",
				"	keywords = {keyword 1, keyword 2},",
				"	_language = {ENGLISH},",
				"	_publication_type = {INTERNATIONAL_JOURNAL_PAPER},",
				"	_publication_type_name = {Articles in international journals with selection committee},",
				"	_publication_category = {ACLN},",
				"	_publication_category_name = {Articles in international or national journals with selection committee and not ranked in international databases},",
				"	journal = {journal name/1},",
				"	publisher = {publisher/1},",
				"	volume = {vol/1},",
				"	number = {nb/1},",
				"	pages = {pages/1},",
				"	_scimago_qindex = {Q2},",
				"	_impact_factor = {123.456},",
				"	note = {Scimago Q-Index: Q2, Impact factor: 123.456}",
				"}"), bibtex);
	}

	@Test
	public void exportPublications_book() {
		Book pub = mock(Book.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_BOOK);
		when(pub.getPreferredStringId()).thenReturn("Book_123");
		when(pub.getVolume()).thenReturn("vol/1");
		when(pub.getNumber()).thenReturn("nb/1");
		when(pub.getPages()).thenReturn("pages/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getEdition()).thenReturn("edition/1");
		when(pub.getEditors()).thenReturn("Editor1, Editor2 and Editor3, Editor4");
		when(pub.getPublisher()).thenReturn("publisher/1");
		when(pub.getSeries()).thenReturn("series/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"@book{Book_123,",
				"	title = {Title 1},",
				"	author = {Lastname1, Firstname1 and Lastname0, Firstname0},",
				"	year = 2022,",
				"	month = jul,",
				"	doi = {doi/1},",
				"	isbn = {isbn/1},",
				"	issn = {issn/1},",
				"	url = {url/1},",
				"	dblp = {DBLP/1},",
				"	_video = {video/1},",
				"	abstract = {Abs 1},",
				"	keywords = {keyword 1, keyword 2},",
				"	_language = {ENGLISH},",
				"	_publication_type = {INTERNATIONAL_BOOK},",
				"	_publication_type_name = {International scientific books},",
				"	_publication_category = {OS},",
				"	_publication_category_name = {Scientific books},",
				"	edition = {edition/1},",
				"	series = {series/1},",
				"	volume = {vol/1},",
				"	number = {nb/1},",
				"	pages = {pages/1},",
				"	editor = {Editor1, Editor2 and Editor3, Editor4},",
				"	publisher = {publisher/1},",
				"	address = {adr/1}",
				"}"), bibtex);
	}

	@Test
	public void exportPublications_bookChapter() {
		BookChapter pub = mock(BookChapter.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_BOOK_CHAPTER);
		when(pub.getPreferredStringId()).thenReturn("BookChapter_123");
		when(pub.getVolume()).thenReturn("vol/1");
		when(pub.getNumber()).thenReturn("nb/1");
		when(pub.getPages()).thenReturn("pages/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getEdition()).thenReturn("edition/1");
		when(pub.getEditors()).thenReturn("Editor1, Editor2 and Editor3, Editor4");
		when(pub.getPublisher()).thenReturn("publisher/1");
		when(pub.getSeries()).thenReturn("series/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"@incollection{BookChapter_123,",
				"	title = {Title 1},",
				"	author = {Lastname1, Firstname1 and Lastname0, Firstname0},",
				"	year = 2022,",
				"	month = jul,",
				"	doi = {doi/1},",
				"	isbn = {isbn/1},",
				"	issn = {issn/1},",
				"	url = {url/1},",
				"	dblp = {DBLP/1},",
				"	_video = {video/1},",
				"	abstract = {Abs 1},",
				"	keywords = {keyword 1, keyword 2},",
				"	_language = {ENGLISH},",
				"	_publication_type = {INTERNATIONAL_BOOK_CHAPTER},",
				"	_publication_type_name = {Chapters in international scientific books},",
				"	_publication_category = {COS},",
				"	_publication_category_name = {Chapters in scientific books},",
				"	edition = {edition/1},",
				"	series = {series/1},",
				"	volume = {vol/1},",
				"	number = {nb/1},",
				"	pages = {pages/1},",
				"	editor = {Editor1, Editor2 and Editor3, Editor4},",
				"	publisher = {publisher/1},",
				"	address = {adr/1}",
				"}"), bibtex);
	}

	@Test
	public void exportPublications_phdthesis() {
		Thesis pub = mock(Thesis.class);
		preparePublicationForExportTest(pub, PublicationType.PHD_THESIS);
		when(pub.getPreferredStringId()).thenReturn("Thesis_123");
		when(pub.getInstitution()).thenReturn("inst/1");
		when(pub.getAddress()).thenReturn("adr/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"@phdthesis{Thesis_123,",
				"	title = {Title 1},",
				"	author = {Lastname1, Firstname1 and Lastname0, Firstname0},",
				"	year = 2022,",
				"	month = jul,",
				"	doi = {doi/1},",
				"	isbn = {isbn/1},",
				"	issn = {issn/1},",
				"	url = {url/1},",
				"	dblp = {DBLP/1},",
				"	_video = {video/1},",
				"	abstract = {Abs 1},",
				"	keywords = {keyword 1, keyword 2},",
				"	_language = {ENGLISH},",
				"	_publication_type = {PHD_THESIS},",
				"	_publication_type_name = {PhD theses},",
				"	_publication_category = {TH},",
				"	_publication_category_name = {Theses (HDR, PHD, Master)},",
				"	school = {inst/1},",
				"	address = {adr/1},",
				"	type = {PhD theses}",
				"}"), bibtex);
	}

	@Test
	public void exportPublications_masterthesis() {
		Thesis pub = mock(Thesis.class);
		preparePublicationForExportTest(pub, PublicationType.MASTER_THESIS);
		when(pub.getPreferredStringId()).thenReturn("Thesis_123");
		when(pub.getInstitution()).thenReturn("inst/1");
		when(pub.getAddress()).thenReturn("adr/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"@mastersthesis{Thesis_123,",
				"	title = {Title 1},",
				"	author = {Lastname1, Firstname1 and Lastname0, Firstname0},",
				"	year = 2022,",
				"	month = jul,",
				"	doi = {doi/1},",
				"	isbn = {isbn/1},",
				"	issn = {issn/1},",
				"	url = {url/1},",
				"	dblp = {DBLP/1},",
				"	_video = {video/1},",
				"	abstract = {Abs 1},",
				"	keywords = {keyword 1, keyword 2},",
				"	_language = {ENGLISH},",
				"	_publication_type = {MASTER_THESIS},",
				"	_publication_type_name = {Master theses},",
				"	_publication_category = {TH},",
				"	_publication_category_name = {Theses (HDR, PHD, Master)},",
				"	school = {inst/1},",
				"	address = {adr/1},",
				"	type = {Master theses}",
				"}"), bibtex);
	}

	@Test
	public void exportPublications_journalEdition() {
		Journal journal = mock(Journal.class);
		when(journal.getJournalName()).thenReturn("journal name/1");
		when(journal.getPublisher()).thenReturn("publisher/1");
		JournalEdition pub = mock(JournalEdition.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_JOURNAL_EDITION);
		when(pub.getPreferredStringId()).thenReturn("JournalEdition_123");
		when(pub.getImpactFactor()).thenReturn(123.456f);
		when(pub.getJournal()).thenReturn(journal);
		when(pub.getVolume()).thenReturn("vol/1");
		when(pub.getNumber()).thenReturn("nb/1");
		when(pub.getPages()).thenReturn("pages/1");
		when(pub.getScimagoQIndex()).thenReturn(QuartileRanking.Q2);

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"@book{JournalEdition_123,",
				"	title = {Title 1},",
				"	editor = {Lastname1, Firstname1 and Lastname0, Firstname0},",
				"	year = 2022,",
				"	month = jul,",
				"	doi = {doi/1},",
				"	isbn = {isbn/1},",
				"	issn = {issn/1},",
				"	url = {url/1},",
				"	dblp = {DBLP/1},",
				"	_video = {video/1},",
				"	abstract = {Abs 1},",
				"	keywords = {keyword 1, keyword 2},",
				"	_language = {ENGLISH},",
				"	_publication_type = {INTERNATIONAL_JOURNAL_EDITION},",
				"	_publication_type_name = {Editor of international books or journals},",
				"	_publication_category = {DO},",
				"	_publication_category_name = {Editor of books or journals},",
				"	journal = {journal name/1},",
				"	publisher = {publisher/1},",
				"	volume = {vol/1},",
				"	number = {nb/1},",
				"	pages = {pages/1},",
				"	_scimago_qindex = {Q2},",
				"	_impact_factor = {123.456},",
				"	note = {Scimago Q-Index: Q2, Impact factor: 123.456}",
				"}"), bibtex);
	}

	@Test
	public void exportPublications_keynote() {
		KeyNote pub = mock(KeyNote.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_KEYNOTE);
		when(pub.getPreferredStringId()).thenReturn("Keynote_123");
		when(pub.getOrganization()).thenReturn("orga/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getEditors()).thenReturn("Editor1, Editor2 and Editor3, Editor4");
		when(pub.getScientificEventName()).thenReturn("event/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"@inproceedings{Keynote_123,",
				"	title = {Title 1},",
				"	author = {Lastname1, Firstname1 and Lastname0, Firstname0},",
				"	year = 2022,",
				"	month = jul,",
				"	doi = {doi/1},",
				"	isbn = {isbn/1},",
				"	issn = {issn/1},",
				"	url = {url/1},",
				"	dblp = {DBLP/1},",
				"	_video = {video/1},",
				"	abstract = {Abs 1},",
				"	keywords = {keyword 1, keyword 2},",
				"	_language = {ENGLISH},",
				"	_publication_type = {INTERNATIONAL_KEYNOTE},",
				"	_publication_type_name = {Keynotes in international conference},",
				"	_publication_category = {C_INV},",
				"	_publication_category_name = {Keynotes in international or national conference},",
				"	booktitle = {event/1},",
				"	editor = {Editor1, Editor2 and Editor3, Editor4},",
				"	organization = {orga/1},",
				"	address = {adr/1},",
				"	note = {Keynotes in international conference}",
				"}"), bibtex);
	}

	@Test
	public void exportPublications_techreport() {
		Report pub = mock(Report.class);
		preparePublicationForExportTest(pub, PublicationType.TECHNICAL_REPORT);
		when(pub.getPreferredStringId()).thenReturn("Report_123");
		when(pub.getInstitution()).thenReturn("inst/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getReportNumber()).thenReturn("num/1");
		when(pub.getReportType()).thenReturn("type/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"@techreport{Report_123,",
				"	title = {Title 1},",
				"	author = {Lastname1, Firstname1 and Lastname0, Firstname0},",
				"	year = 2022,",
				"	month = jul,",
				"	doi = {doi/1},",
				"	isbn = {isbn/1},",
				"	issn = {issn/1},",
				"	url = {url/1},",
				"	dblp = {DBLP/1},",
				"	_video = {video/1},",
				"	abstract = {Abs 1},",
				"	keywords = {keyword 1, keyword 2},",
				"	_language = {ENGLISH},",
				"	_publication_type = {TECHNICAL_REPORT},",
				"	_publication_type_name = {Technical reports},",
				"	_publication_category = {AP},",
				"	_publication_category_name = {Other productions},",
				"	number = {num/1},",
				"	institution = {inst/1},",
				"	address = {adr/1},",
				"	note = {type/1}",
				"}"), bibtex);
	}

	@Test
	public void exportPublications_manual() {
		Report pub = mock(Report.class);
		preparePublicationForExportTest(pub, PublicationType.TUTORIAL_DOCUMENTATION);
		when(pub.getPreferredStringId()).thenReturn("Doc_123");
		when(pub.getInstitution()).thenReturn("inst/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getReportNumber()).thenReturn("num/1");
		when(pub.getReportType()).thenReturn("type/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"@manual{Doc_123,",
				"	title = {Title 1},",
				"	author = {Lastname1, Firstname1 and Lastname0, Firstname0},",
				"	year = 2022,",
				"	month = jul,",
				"	doi = {doi/1},",
				"	isbn = {isbn/1},",
				"	issn = {issn/1},",
				"	url = {url/1},",
				"	dblp = {DBLP/1},",
				"	_video = {video/1},",
				"	abstract = {Abs 1},",
				"	keywords = {keyword 1, keyword 2},",
				"	_language = {ENGLISH},",
				"	_publication_type = {TUTORIAL_DOCUMENTATION},",
				"	_publication_type_name = {Tutorials or documentations},",
				"	_publication_category = {AP},",
				"	_publication_category_name = {Other productions},",
				"	edition = {num/1},",
				"	organization = {inst/1},",
				"	address = {adr/1},",
				"	note = {type/1}",
				"}"), bibtex);
	}

	@Test
	public void exportPublications_patent() {
		Patent pub = mock(Patent.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_PATENT);
		when(pub.getPreferredStringId()).thenReturn("Patent_123");
		when(pub.getInstitution()).thenReturn("inst/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getPatentNumber()).thenReturn("bre/1");
		when(pub.getPatentType()).thenReturn("int./1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"@misc{Patent_123,",
				"	title = {Title 1},",
				"	author = {Lastname1, Firstname1 and Lastname0, Firstname0},",
				"	year = 2022,",
				"	month = jul,",
				"	doi = {doi/1},",
				"	isbn = {isbn/1},",
				"	issn = {issn/1},",
				"	url = {url/1},",
				"	dblp = {DBLP/1},",
				"	_video = {video/1},",
				"	abstract = {Abs 1},",
				"	keywords = {keyword 1, keyword 2},",
				"	_language = {ENGLISH},",
				"	_publication_type = {INTERNATIONAL_PATENT},",
				"	_publication_type_name = {International patents},",
				"	_publication_category = {BRE},",
				"	_publication_category_name = {Patents},",
				"	address = {adr/1},",
				"	note = {int./1},",
				"	howpublished = {Patent bre/1 registered to: inst/1}",
				"}"), bibtex);
	}

	@Test
	public void exportPublications_misc() {
		MiscDocument pub = mock(MiscDocument.class);
		preparePublicationForExportTest(pub, PublicationType.ARTISTIC_PRODUCTION);
		when(pub.getPreferredStringId()).thenReturn("Art_123");
		when(pub.getDocumentNumber()).thenReturn("num/1");
		when(pub.getDocumentType()).thenReturn("type/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getHowPublished()).thenReturn("how/1");
		when(pub.getOrganization()).thenReturn("orga/1");
		when(pub.getPublisher()).thenReturn("publisher/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"@misc{Art_123,",
				"	title = {Title 1},",
				"	author = {Lastname1, Firstname1 and Lastname0, Firstname0},",
				"	year = 2022,",
				"	month = jul,",
				"	doi = {doi/1},",
				"	isbn = {isbn/1},",
				"	issn = {issn/1},",
				"	url = {url/1},",
				"	dblp = {DBLP/1},",
				"	_video = {video/1},",
				"	abstract = {Abs 1},",
				"	keywords = {keyword 1, keyword 2},",
				"	_language = {ENGLISH},",
				"	_publication_type = {ARTISTIC_PRODUCTION},",
				"	_publication_type_name = {Artistic research productions},",
				"	_publication_category = {PAT},",
				"	_publication_category_name = {Artistic research productions},",
				"	howpublished = {how/1},",
				"	type = {type/1},",
				"	number = {num/1},",
				"	organization = {orga/1},",
				"	publisher = {publisher/1},",
				"	address = {adr/1}",
				"}"), bibtex);
	}


	@Test
	public void exportPublications_perPublicationType() {
		for (final PublicationType type : PublicationType.values()) {
			Publication pub = mock(type.getInstanceType());
			preparePublicationForExportTest(pub, type);
			lenient().when(pub.getPreferredStringId()).thenReturn("Pub/" + type.name());
			lenient().when(pub.getAbstractText()).thenReturn("abs/" + type.name());
			lenient().when(pub.getDblpURL()).thenReturn("dblp/" + type.name());
			lenient().when(pub.getDOI()).thenReturn("doi/" + type.name());
			lenient().when(pub.getExtraURL()).thenReturn("extra/" + type.name());
			lenient().when(pub.getHalId()).thenReturn("hal/" + type.name());
			lenient().when(pub.getISBN()).thenReturn("isbn/" + type.name());
			lenient().when(pub.getISSN()).thenReturn("issn/" + type.name());
			lenient().when(pub.getPublicationYear()).thenReturn(2022);
			lenient().when(pub.getTitle()).thenReturn("title/" + type.name());

			final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

			assertFalse(Strings.isNullOrEmpty(bibtex));
		}
	}

	@Test
	public void exportSpecialUnicodeChars() {
		String input = "éàç(D-λLBP++HOG)";
		String output = this.test.toTeXString(input);
		assertEquals("{\\'{e}}{\\`{a}}{\\c{c}}(D-λLBP++HOG)", output);
	}

}