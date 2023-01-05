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

package fr.ciadlab.labmanager.indicators.publication.count;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.indicators.AbstractAnnualIndicator;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.utils.ranking.JournalRankingSystem;
import org.springframework.context.support.MessageSourceAccessor;

/** Count the number of ranked journal papers for an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public abstract class AbstractRankedJournalPaperCountIndicator extends AbstractAnnualIndicator {

	private JournalPaperService journalPaperService;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param journalPaperService the service for accessing the journal papers.
	 */
	public AbstractRankedJournalPaperCountIndicator(
			MessageSourceAccessor messages,
			Constants constants,
			JournalPaperService journalPaperService) {
		this(messages, constants, AbstractAnnualIndicator::sum, journalPaperService);
	}

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param mergingFunction the function that should be used for merging the annual values.
	 *      If it is {@code null}, the {@link #sum(Map)} is used.
	 * @param journalPaperService the service for accessing the journal papers.
	 */
	public AbstractRankedJournalPaperCountIndicator(
			MessageSourceAccessor messages,
			Constants constants,
			Function<Map<Integer, Number>, Number> mergingFunction,
			JournalPaperService journalPaperService) {
		super(messages, constants, mergingFunction);
		this.journalPaperService = journalPaperService;
	}

	/** Replies the journal ranking system to be used.
	 *
	 * @return the journal ranking system to be used. 
	 */
	public abstract JournalRankingSystem getJournalRankingSystem();

	@Override
	public Map<Integer, Number> getValuesPerYear(ResearchOrganization organization, int startYear, int endYear) {
		final Set<JournalPaper> papers = this.journalPaperService.getJournalPapersByOrganizationId(organization.getId(), true, true);
		//
		Stream<JournalPaper> stream = filterByYearWindow(false, papers, it -> Integer.valueOf(it.getPublicationYear()));
		switch (getJournalRankingSystem()) {
		case SCIMAGO:
			stream = stream.filter(it -> it.getScimagoQIndex() != null);
			break;
		case WOS:
			stream = stream.filter(it -> it.getWosQIndex() != null);
			break;
		default:
			throw new IllegalArgumentException("Unsupported ranking system"); //$NON-NLS-1$
		}
		//
		final Map<Integer, Number> rankedPapers = stream.collect(Collectors.toConcurrentMap(
				it -> Integer.valueOf(it.getPublicationYear()),
				it -> Integer.valueOf(1),
				(a, b) -> Integer.valueOf(a.intValue() + b.intValue())));
		//
		setComputationDetails(rankedPapers);
		return rankedPapers;
	}

}
