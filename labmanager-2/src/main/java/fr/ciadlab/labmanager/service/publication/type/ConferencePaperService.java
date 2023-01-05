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
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.ConferencePaper;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.repository.publication.type.ConferencePaperRepository;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.publication.AbstractPublicationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for managing papers for conferences and workshops.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class ConferencePaperService extends AbstractPublicationTypeService {

	private ConferencePaperRepository repository;

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
	public ConferencePaperService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired DownloadableFileManager downloadableFileManager,
			@Autowired ConferencePaperRepository repository,
			@Autowired MembershipService membershipService) {
		super(messages, constants, downloadableFileManager);
		this.repository = repository;
		this.membershipService = membershipService;
	}

	/** Replies all the conference papers.
	 *
	 * @return the papers.
	 */
	public List<ConferencePaper> getAllConferencePapers() {
		return this.repository.findAll();
	}

	/** Replies the conference paper with the given identifier.
	 *
	 * @param identifier the identifier of the conference paper.
	 * @return the conference paper or {@code null}.
	 */
	public ConferencePaper getConferencePaper(int identifier) {
		final Optional<ConferencePaper> byId = this.repository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}


	/** Replies all the conference papers from the database that are attached to a person involved in the given organization.
	 *
	 * @param identifier the identifier of the organization.
	 * @param includeSubOrganizations indicates if the members of the suborganizations are considered.
	 * @return the publications.
	 */
	public Set<ConferencePaper> getConferencePapersByOrganizationId(int identifier, boolean includeSubOrganizations) {
		final Set<Person> members;
		if (includeSubOrganizations) {
			members = this.membershipService.getMembersOf(identifier);
		} else {
			members = this.membershipService.getDirectMembersOf(identifier);
		}
		final Set<Integer> identifiers = members.stream().map(it -> Integer.valueOf(it.getId())).collect(Collectors.toUnmodifiableSet());
		return this.repository.findAllByAuthorshipsPersonIdIn(identifiers);
	}

	/** Create a conference paper.
	 *
	 * @param publication the publication to copy.
	 * @param scientificEventName the name of the conference or the workshop.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param series the number or the name of the series for the conference proceedings.
	 * @param orga the name of the organization institution.
	 * @param address the geographical location of the event, usually a city and a country.
	 * @param publisher the name of the publisher of the proocedings.
	 * @return the created conference paper.
	 */
	public ConferencePaper createConferencePaper(Publication publication, String scientificEventName, String volume, String number,
			String pages, String editors, String series, String orga, String address, String publisher) {
		return createConferencePaper(publication, scientificEventName, volume, number, pages, editors, series,
				orga, address, publisher, true);
	}

	/** Create a conference paper.
	 *
	 * @param publication the publication to copy.
	 * @param scientificEventName the name of the conference or the workshop.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param series the number or the name of the series for the conference proceedings.
	 * @param orga the name of the organization institution.
	 * @param address the geographical location of the event, usually a city and a country.
	 * @param publisher the name of the publisher of the proocedings.
	 * @param saveInDb {@code true} for saving the publication in the database.
	 * @return the created conference paper.
	 */
	public ConferencePaper createConferencePaper(Publication publication, String scientificEventName, String volume, String number,
			String pages, String editors, String series, String orga, String address, String publisher, boolean saveInDb) {
		final ConferencePaper res = new ConferencePaper(publication, scientificEventName, volume, number, pages, editors,
				orga, address, series, publisher);
		if (saveInDb) {
			this.repository.save(res);
		}
		return res;
	}

	/** Update the conference paper with the given identifier.
	 *
	 * @param pubId identifier of the paper to change.
	 * @param title the new title of the publication, never {@code null} or empty.
	 * @param type the new type of publication, never {@code null}.
	 * @param date the new date of publication. It may be {@code null}. In this case only the year should be considered.
	 * @param year the new year of the publication. 
	 * @param abstractText the new text of the abstract.
	 * @param keywords the new list of keywords.
	 * @param doi the new DOI number.
	 * @param isbn the new ISBN number.
	 * @param issn the new ISSN number.
	 * @param dblpUrl the new URL to the DBLP page of the publication.
	 * @param extraUrl the new URL to the page of the publication.
	 * @param language the new major language of the publication.
	 * @param pdfContent the content of the publication PDF that is encoded in {@link Base64}. The content will be saved into
	 *     the dedicated folder for PDF files.
	 * @param awardContent the content of the publication award certificate that is encoded in {@link Base64}. The content will be saved into
	 *     the dedicated folder for PDF files.
	 * @param pathToVideo the path that allows to download the video of the publication.
	 * @param scientificEventName the name of the conference or the workshop.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param series the number or the name of the series for the conference proceedings.
	 * @param orga the name of the organization institution.
	 * @param publisher the publisher of the proceedings of the conference.
	 * @param address the geographical location of the event, usually a city and a country.
	 */
	public void updateConferencePaper(int pubId,
			String title, PublicationType type, LocalDate date, int year, String abstractText, String keywords,
			String doi, String isbn, String issn, String dblpUrl, String extraUrl,
			PublicationLanguage language, String pdfContent, String awardContent, String pathToVideo,
			String scientificEventName, String volume, String number,
			String pages, String editors, String series, String orga, String publisher, String address) {
		final Optional<ConferencePaper> res = this.repository.findById(Integer.valueOf(pubId));
		if (res.isPresent()) {
			final ConferencePaper paper = res.get();

			updatePublicationNoSave(paper, title, type, date, year,
					abstractText, keywords, doi, isbn, issn, dblpUrl,
					extraUrl, language, pdfContent, awardContent,
					pathToVideo);

			paper.setScientificEventName(Strings.emptyToNull(scientificEventName));
			paper.setVolume(Strings.emptyToNull(volume));
			paper.setNumber(Strings.emptyToNull(number));
			paper.setPages(Strings.emptyToNull(pages));
			paper.setEditors(Strings.emptyToNull(editors));
			paper.setSeries(Strings.emptyToNull(series));
			paper.setOrganization(Strings.emptyToNull(orga));
			paper.setPublisher(Strings.emptyToNull(publisher));
			paper.setAddress(Strings.emptyToNull(address));
			
			this.repository.save(res.get());
		}
	}

	/** Remove the conference paper from the database.
	 *
	 * @param identifier the identifier of the conference paper to be removed.
	 */
	public void removeConferencePaper(int identifier) {
		this.repository.deleteById(Integer.valueOf(identifier));
	}

}
