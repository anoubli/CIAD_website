/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.service.publication.type;

import java.sql.Date;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.Thesis;
import fr.ciadlab.labmanager.repository.publication.type.ThesisRepository;
import fr.ciadlab.labmanager.service.publication.AbstractPublicationTypeService;
import fr.ciadlab.labmanager.utils.files.DownloadableFileManager;
import org.springframework.beans.factory.annotation.Autowired;

/** Service for managing theses.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ThesisService extends AbstractPublicationTypeService {

	private ThesisRepository repository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param downloadableFileManager downloadable file manager.
	 * @param repository the repository for this service.
	 */
	public ThesisService(@Autowired DownloadableFileManager downloadableFileManager,
			@Autowired ThesisRepository repository) {
		super(downloadableFileManager);
		this.repository = repository;
	}

	/** Replies all the theses.
	 *
	 * @return the theses.
	 */
	public List<Thesis> getAllTheses() {
		return this.repository.findAll();
	}

	/** Replies the thesis with the given identifier.
	 *
	 * @param identifier the identifier of the thesis.
	 * @return the thesis or {@code null}.
	 */
	public Thesis getThesis(int identifier) {
		final Optional<Thesis> byId = this.repository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}

	/** Create a thesis.
	 *
	 * @param publication the publication to copy.
	 * @param institution the name of the institution in which the report was published.
	 * @param address the geographical address of the institution. Usually a city and a country.
	 * @return the created thesis.
	 */
	public Thesis createThesis(Publication publication,
			String institution, String address) {
		return createThesis(publication, institution, address, true);
	}

	/** Create a thesis.
	 *
	 * @param publication the publication to copy.
	 * @param institution the name of the institution in which the report was published.
	 * @param address the geographical address of the institution. Usually a city and a country.
	 * @param saveInDb {@code true} for saving the publication in the database.
	 * @return the created thesis.
	 */
	public Thesis createThesis(Publication publication,
			String institution, String address, boolean saveInDb) {
		final Thesis res = new Thesis(publication, institution, address);
		if (saveInDb) {
			this.repository.save(res);
		}
		return res;
	}

	/** Update the thesis with the given identifier.
	 *
	 * @param pubId identifier of the thesis to change.
	 * @param title the new title of the publication, never {@code null} or empty.
	 * @param type the new type of publication, never {@code null}.
	 * @param date the new date of publication, never {@code null}.
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
	 * @param institution the name of the institution in which the thesis was published.
	 * @param address the geographical address of the institution. Usually a city and a country.
	 */
	public void updateThesis(int pubId,
			String title, PublicationType type, Date date, String abstractText, String keywords,
			String doi, String isbn, String issn, String dblpUrl, String extraUrl,
			PublicationLanguage language, String pdfContent, String awardContent, String pathToVideo,
			String institution, String address) {
		final Optional<Thesis> res = this.repository.findById(Integer.valueOf(pubId));
		if (res.isPresent()) {
			final Thesis thesis = res.get();

			updatePublicationNoSave(thesis, title, type, date,
					abstractText, keywords, doi, isbn, issn, dblpUrl,
					extraUrl, language, pdfContent, awardContent,
					pathToVideo);

			thesis.setInstitution(Strings.emptyToNull(institution));
			thesis.setAddress(Strings.emptyToNull(address));

			this.repository.save(res.get());
		}
	}

	/** Remove the thesis from the database.
	 *
	 * @param identifier the identifier of the thesis to be removed.
	 */
	public void removeThesis(int identifier) {
		this.repository.deleteById(Integer.valueOf(identifier));
	}

}