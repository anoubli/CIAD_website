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

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.repository.publication.type.JournalPaperRepository;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.publication.AbstractPublicationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for managing journal paper.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class JournalPaperService extends AbstractPublicationTypeService {

	private JournalPaperRepository repository;

	private MembershipService membershipService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param downloadableFileManager downloadable file manager.
	 * @param repository the repository for this service.
	 * @param membershipService the service for accessing the memberships.
	 */
	public JournalPaperService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired DownloadableFileManager downloadableFileManager,
			@Autowired JournalPaperRepository repository,
			@Autowired MembershipService membershipService) {
		super(messages, constants, downloadableFileManager);
		this.repository = repository;
		this.membershipService = membershipService;
	}

	/** Replies all the journal papers.
	 *
	 * @return the journal papers.
	 */
	public List<JournalPaper> getAllJournalPapers() {
		return this.repository.findAll();
	}

	/** Replies the journal paper with the given identifier.
	 *
	 * @param identifier the identifier of the journal paper.
	 * @return the journal paper or {@code null}.
	 */
	public JournalPaper getJournalPaper(int identifier) {
		final Optional<JournalPaper> byId = this.repository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}

	/** Replies the journal papers that are associated to the journal with the given identifier.
	 *
	 * @param journalId the identifier of the journal.
	 * @return the journal papers.
	 */
	public List<JournalPaper> getJournalPapersByJournalId(int journalId) {
		return this.repository.findAllByJournalId(journalId);
	}

	/** Replies all the journal paper from the database that are attached to a person involved in the given organization.
	 *
	 * @param identifier the identifier of the organization.
	 * @param includeSubOrganizations indicates if the members of the suborganizations are considered.
	 * @param filterAuthorshipsWithActiveMemberships indicates if the authorships must correspond to active memberships.
	 * @return the publications.
	 */
	public Set<JournalPaper> getJournalPapersByOrganizationId(int identifier, boolean includeSubOrganizations,
			boolean filterAuthorshipsWithActiveMemberships) {
		final Set<Person> members;
		if (includeSubOrganizations) {
			members = this.membershipService.getMembersOf(identifier);
		} else {
			members = this.membershipService.getDirectMembersOf(identifier);
		}
		final Set<Integer> identifiers = members.stream().map(it -> Integer.valueOf(it.getId())).collect(Collectors.toUnmodifiableSet());
		final Set<JournalPaper> publications = this.repository.findAllByAuthorshipsPersonIdIn(identifiers);
		if (filterAuthorshipsWithActiveMemberships) {
			return filterPublicationsWithMemberships(publications, identifier, includeSubOrganizations);
		}
		return publications;
	}

	/** Create a journal paper.
	 *
	 * @param publication the publication to copy.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param series the series of the journal.
	 * @param journal the associated journal.
	 * @return the created journal paper.
	 */
	public JournalPaper createJournalPaper(Publication publication, String volume, String number, String pages, String series, Journal journal) {
		return createJournalPaper(publication, volume, number, pages, series, journal, true);
	}

	/** Create a journal paper.
	 *
	 * @param publication the publication to copy.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param series the series of the journal.
	 * @param journal the associated journal.
	 * @param saveInDb {@code true} for saving the publication in the database.
	 * @return the created journal paper.
	 */
	public JournalPaper createJournalPaper(Publication publication, String volume, String number, String pages,
			String series, Journal journal, boolean saveInDb) {
		final JournalPaper res = new JournalPaper(publication, volume, number, pages, series);
		res.setJournal(journal);
		if (saveInDb) {
			this.repository.save(res);
		}
		return res;
	}

	/** Update the journal paper with the given identifier.
	 *
	 * @param pubId identifier of the paper to change.
	 * @param title the new title of the publication, never {@code null} or empty.
	 * @param type the new type of publication, never {@code null}.
	 * @param date the new date of publication. It may be {@code null}. In this case only the year should be considered.
	 * @param year the new year of the publication. 
	 * @param abstractText the new text of the abstract.
	 * @param keywords the new list of keywords.
	 * @param doi the new DOI number.
	 * @param dblpUrl the new URL to the DBLP page of the publication.
	 * @param extraUrl the new URL to the page of the publication.
	 * @param language the new major language of the publication.
	 * @param pdfContent the content of the publication PDF that is encoded in {@link Base64}. The content will be saved into
	 *     the dedicated folder for PDF files.
	 * @param awardContent the content of the publication award certificate that is encoded in {@link Base64}. The content will be saved into
	 *     the dedicated folder for PDF files.
	 * @param pathToVideo the path that allows to download the video of the publication.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param series the series of the journal.
	 * @param journal the journal.
	 */
	public void updateJournalPaper(int pubId,
			String title, PublicationType type, LocalDate date, int year, String abstractText, String keywords,
			String doi, String dblpUrl, String extraUrl,
			PublicationLanguage language, String pdfContent, String awardContent, String pathToVideo,
			String volume, String number, String pages, String series, Journal journal) {
		final Optional<JournalPaper> res = this.repository.findById(Integer.valueOf(pubId));
		if (res.isPresent()) {
			final JournalPaper paper = res.get();

			updatePublicationNoSave(paper, title, type, date, year,
					abstractText, keywords, doi, null, null, dblpUrl,
					extraUrl, language, pdfContent, awardContent,
					pathToVideo);

			paper.setVolume(Strings.emptyToNull(volume));
			paper.setNumber(Strings.emptyToNull(number));
			paper.setPages(Strings.emptyToNull(pages));
			paper.setSeries(Strings.emptyToNull(series));

			paper.setJournal(journal);

			this.repository.save(res.get());
		}
	}

	/** Remove the journal paper from the database.
	 *
	 * @param identifier the identifier of the journal paper to be removed.
	 */
	public void removeJournalPaper(int identifier) {
		this.repository.deleteById(Integer.valueOf(identifier));
	}

}
