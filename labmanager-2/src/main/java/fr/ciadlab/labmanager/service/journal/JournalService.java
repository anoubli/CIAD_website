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

package fr.ciadlab.labmanager.service.journal;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.repository.journal.JournalQualityAnnualIndicatorsRepository;
import fr.ciadlab.labmanager.repository.journal.JournalRepository;
import fr.ciadlab.labmanager.repository.publication.type.JournalPaperRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.utils.net.NetConnection;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service related to the journals.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class JournalService extends AbstractService {

	/** Base URL for obtaining the picture showing the quartile of a journal.
	 */
	public static final String SCIMAGO_URL_PREFIX = "https://www.scimagojr.com/journal_img.php?id="; //$NON-NLS-1$
	
	private final JournalRepository journalRepository;

	private final JournalQualityAnnualIndicatorsRepository indicatorRepository;

	private final JournalPaperRepository publicationRepository;

	private final NetConnection netConnection;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param journalRepository the journal repository.
	 * @param indicatorRepository the repository for journal indicators.
	 * @param publicationRepository the publication repository.
	 * @param netConnection the tools for accessing the network.
	 */
	public JournalService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JournalRepository journalRepository,
			@Autowired JournalQualityAnnualIndicatorsRepository indicatorRepository,
			@Autowired JournalPaperRepository publicationRepository,
			@Autowired NetConnection netConnection) {
		super(messages, constants);
		this.journalRepository = journalRepository;
		this.indicatorRepository = indicatorRepository;
		this.publicationRepository = publicationRepository;
		this.netConnection = netConnection;
	}

	/** Replies all the journals for the database.
	 *
	 * @return the list of journals.
	 */
	public List<Journal> getAllJournals() {		
	
		return this.journalRepository.findAll();		
	}

	/** Replies the journal with the given identifier.
	 *
	 * @param identifier the identifier to search for.
	 * @return the journal, or {@code null} if none is defined.
	 */
	public Journal getJournalById(int identifier) {
		final Optional<Journal> res = this.journalRepository.findById(Integer.valueOf(identifier));
		if (res.isPresent()) {
			return res.get();
		}
		return null;
	}

	/** Replies a journal with the given name. If multiple journals have the same name, one is replied (usually the first replied by the iterator).
	 *
	 * @param name the name to search for.
	 * @return the journal, or {@code null} if none is defined.
	 */
	public Journal getJournalByName(String name) {
		final Set<Journal> res = getJournalsByName(name);
		if (res.isEmpty()) {
			return null;
		}
		return res.iterator().next();
	}

	/** Replies the journals with the given name.
	 *
	 * @param name the name to search for.
	 * @return the journals.
	 */
	public Set<Journal> getJournalsByName(String name) {
		return this.journalRepository.findDistinctByJournalName(name);
	}

	/** Replies the identifier of a journal with the given name. If multiple journals have the same name, one is selected (usually the first replied by the iterator).
	 *
	 * @param name the name to search for.
	 * @return the journal identifier, or {@code 0} if none is defined.
	 */
	public int getJournalIdByName(String name) {
		final Set<Journal> res = this.journalRepository.findDistinctByJournalName(name);
		if (!res.isEmpty()) {
			final Journal journal = res.iterator().next();
			if (journal != null) {
				return journal.getId();
			}
		}
		return 0;
	}

	/** Create a journal into the database.
	 *
	 * @param name the name of the journal.
	 * @param address the address of the publisher of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param isbn the ISBN number for the journal.
	 * @param issn the ISSN number for the journal.
	 * @param openAccess indicates if the journal is open access or not.
	 * @param journalUrl the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier to the page of the journal on the Scimago website.
	 * @param wosId the identifier to the page of the journal on the Web-Of-Science website.
	 * @return the created journal.
	 */
	public Journal createJournal(String name, String address, String publisher, String isbn, String issn,
			Boolean openAccess, String journalUrl, String scimagoId, String wosId) {
		final Journal res = new Journal();
		res.setJournalName(name);
		res.setAddress(address);
		res.setPublisher(publisher);
		res.setISBN(isbn);
		res.setISSN(issn);
		res.setOpenAccess(openAccess);
		res.setJournalURL(journalUrl);
		res.setScimagoId(scimagoId);
		res.setWosId(wosId);
		this.journalRepository.save(res);
		return res;
	}

	/** Remove the journal with the given identifier.
	 * A journal cannot be removed if it has attached papers.
	 *
	 * @param identifier the identifier of the journal to remove.
	 * @throws AttachedJournalPaperException when the journal has attached papers.
	 */
	public void removeJournal(int identifier) throws AttachedJournalPaperException {
		final Integer id = Integer.valueOf(identifier);
		final Optional<Journal> journalRef = this.journalRepository.findById(id);
		if (journalRef.isPresent()) {
			final Journal journal = journalRef.get();
			if (journal.hasPublishedPaper()) {
				throw new AttachedJournalPaperException();
			}
			this.journalRepository.deleteById(id);
		}
	}

	/** Update the information of a journal.
	 *
	 * @param identifier the identifier of the journal for which information must be updated.
	 * @param name the name of the journal.
	 * @param address the address of the publisher of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param isbn the ISBN number for the journal.
	 * @param issn the ISSN number for the journal.
	 * @param openAccess indicates if the journal is open access or not.
	 * @param journalUrl the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier to the page of the journal on the Scimago website.
	 * @param wosId the identifier to the page of the journal on the Web-Of-Science website.
	 * @return the updated journal.
	 */
	public Journal updateJournal(int identifier, String name, String address, String publisher, String isbn, String issn,
			Boolean openAccess, String journalUrl, String scimagoId, String wosId) {
		final Optional<Journal> res = this.journalRepository.findById(Integer.valueOf(identifier));
		if (res.isPresent()) {
			final Journal journal = res.get();
			if (journal != null) {
				if (!Strings.isNullOrEmpty(name)) {
					journal.setJournalName(name);
				}
				journal.setJournalName(Strings.emptyToNull(name));
				journal.setAddress(Strings.emptyToNull(address));
				journal.setPublisher(Strings.emptyToNull(publisher));
				journal.setISBN(Strings.emptyToNull(isbn));
				journal.setISSN(Strings.emptyToNull(issn));
				journal.setOpenAccess(openAccess);
				journal.setJournalURL(Strings.emptyToNull(journalUrl));
				journal.setScimagoId(Strings.emptyToNull(scimagoId));
				journal.setWosId(Strings.emptyToNull(wosId));
				this.journalRepository.save(journal);
				return journal; 
			}
		}
		return null;
	}

	/** Create a link between a journal and a paper.
	 * If the paper is linked to another journal, this previous link is lost and the new link is set up.
	 *
	 * @param journalId the identifier of the journal.
	 * @param paperId the identifier of the paper.
	 * @return {@code true} if the link is created; {@code false} if the link cannot be created.
	 */
	public boolean linkPaper(int journalId, int paperId) {
		final Integer idPaper = Integer.valueOf(paperId);
		final Optional<JournalPaper> optPaper = this.publicationRepository.findById(idPaper);
		if (optPaper.isPresent()) {
			final Optional<Journal> optJournal = this.journalRepository.findById(Integer.valueOf(journalId));
			if (optJournal.isPresent()) {
				final JournalPaper paper = optPaper.get();
				final Journal journal = optJournal.get();
				unlinkPaper(paperId, false);
				journal.getPublishedPapers().add(paper);
				paper.setJournal(journal);
				this.journalRepository.save(journal);
				this.publicationRepository.save(paper);
				return true;
			}
		}
		return false;
	}

	/** Unlink the paper with the given identifier from its journal.
	 *
	 * @param paperId the identifier of the paper. 
	 * @param applySave indicates if the changes must be committed into the database by this function.
	 * @return {@code true} if the link is deleted; {@code false} if the link cannot be deleted.
	 */
	public boolean unlinkPaper(int paperId, boolean applySave) {
		final Optional<JournalPaper> optPaper = this.publicationRepository.findById(Integer.valueOf(paperId));
		if (optPaper.isPresent()) {
			final JournalPaper paper = optPaper.get();
			final Journal linkedJournal = paper.getJournal();
			if (linkedJournal != null) {
				if (linkedJournal.getPublishedPapers().remove(paper)) {
					paper.setJournal(null);
					if (applySave) {
						this.journalRepository.save(linkedJournal);
						this.publicationRepository.save(paper);
					}
					return true;
				}
			}
		}
		return false;
	}

	/** Unlink the paper with the given identifier from its journal.
	 *
	 * @param paperId the identifier of the paper. 
	 * @return {@code true} if the link is deleted; {@code false} if the link cannot be deleted.
	 */
	public boolean unlinkPaper(int paperId) {
		return unlinkPaper(paperId, true);
	}

	/** Download the quartile information from the Scimago website.
	 *
	 * @param journalId the identifier of the journal.
	 * @return the quartile or {@code null} if none cannot be found.
	 */
	public QuartileRanking downloadScimagoQuartileByJournalId(int journalId) {
		final Optional<Journal> res = this.journalRepository.findById(Integer.valueOf(journalId));
		if (res.isPresent()) {
			return downloadScimagoQuartileByJournal(res.get());
		}
		return null;
	}

	/** Replies the URL of the image that shows up the scimago quartile.
	 *
	 * @param journal the journal for which the quartile should be replied.
	 * @return the URL of the image.
	 */
	public URL getScimagoQuartileImageURLByJournal(Journal journal) {
		try {
			return new URL(SCIMAGO_URL_PREFIX + journal.getScimagoId());
		} catch (Throwable ex) {
			getLogger().warn(ex.getLocalizedMessage(), ex);
			return null;
		}
	}

	/** Download the quartile information from the Scimago website.
	 *
	 * @param journal the journal for which the quartile must be downloaded.
	 * @return the quartile or {@code null} if none cannot be found.
	 */
	public QuartileRanking downloadScimagoQuartileByJournal(Journal journal) {
		if (journal != null) {
			final String scimagoId = journal.getScimagoId();
			if (!Strings.isNullOrEmpty(scimagoId)) {
				try {
					final URL url = getScimagoQuartileImageURLByJournal(journal);
					if (url != null) {
						final BufferedImage image = this.netConnection.getImageFromURL(url);
						final int rgba = image.getRGB(5, 55);
						final int red = (rgba >> 16) & 0xff;
						switch (red) {
						case 164:
							return QuartileRanking.Q1;
						case 232:
							return QuartileRanking.Q2;
						case 251:
							return QuartileRanking.Q3;
						case 221:
							return QuartileRanking.Q4;
						default:
							//
						}
					}
				} catch (Throwable ex) {
					getLogger().warn(ex.getLocalizedMessage(), ex);
				}
			}
		}
		return null;
	}

	/** Save the given quality indicators for the journal.
	 *
	 * @param journal the journal.
	 * @param year the reference year of the quality indicators.
	 * @param impactFactor the impact factor.
	 * @param scimagoQIndex the Scimago Q-Index.
	 * @param wosQIndex  the WoS Q-Index.
	 * @return the indicators.
	 */
	public JournalQualityAnnualIndicators setQualityIndicators(Journal journal, int year, float impactFactor,
			QuartileRanking scimagoQIndex, QuartileRanking wosQIndex) {
		final JournalQualityAnnualIndicators indicators = journal.setImpactFactorByYear(year, impactFactor);
		indicators.setScimagoQIndex(scimagoQIndex);
		indicators.setWosQIndex(wosQIndex);
		this.indicatorRepository.save(indicators);
		this.journalRepository.save(journal);
		return indicators;
	}

	/** Delete the quality indicators for the given journal and year.
	 *
	 * @param journal the journal.
	 * @param year the reference year.
	 */
	public void deleteQualityIndicators(Journal journal, int year) {
		final JournalQualityAnnualIndicators indicators = journal.getQualityIndicators().remove(Integer.valueOf(year));
		if (indicators != null) {
			this.indicatorRepository.delete(indicators);
			this.journalRepository.save(journal);
		}
	}

}
