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

package fr.ciadlab.labmanager.indicators.publication.fte;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.indicators.AbstractAnnualIndicator;
import fr.ciadlab.labmanager.indicators.members.fte.PermanentResearcherFteIndicator;
import fr.ciadlab.labmanager.indicators.publication.count.AbstractRankedJournalPaperCountIndicator;
import org.springframework.context.support.MessageSourceAccessor;

/** Count the number of ranked journal papers per full-time equivalent per year.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public abstract class AbstractRankedJournalPaperFteRatioIndicator extends AbstractAnnualIndicator {

	private AbstractRankedJournalPaperCountIndicator paperCount;

	private PermanentResearcherFteIndicator fteIndicator;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param fteIndicator the indicator that counts the FTE.
	 * @param paperCount the indicator for counting the ranked papers.
	 */
	public AbstractRankedJournalPaperFteRatioIndicator(
			MessageSourceAccessor messages,
			Constants constants,
			PermanentResearcherFteIndicator fteIndicator,
			AbstractRankedJournalPaperCountIndicator paperCount) {
		this(messages, constants, AbstractAnnualIndicator::average, fteIndicator, paperCount);
	}

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param mergingFunction the function that should be used for merging the annual values.
	 *      If it is {@code null}, the {@link #sum(Map)} is used.
	 * @param fteIndicator the indicator that counts the FTE.
	 * @param paperCount the indicator for counting the ranked papers.
	 */
	public AbstractRankedJournalPaperFteRatioIndicator(
			MessageSourceAccessor messages,
			Constants constants,
			Function<Map<Integer, Number>, Number> mergingFunction,
			PermanentResearcherFteIndicator fteIndicator,
			AbstractRankedJournalPaperCountIndicator paperCount) {
		super(messages, constants, mergingFunction);
		this.fteIndicator = fteIndicator;
		this.paperCount = paperCount;
	}

	@Override
	public Map<Integer, Number> getValuesPerYear(ResearchOrganization organization, int startYear, int endYear) {
		final Map<Integer, Number> rankedPapers = this.paperCount.getValuesPerYear(organization, startYear, endYear);
		final Map<Integer, Number> ftes = this.fteIndicator.getValuesPerYear(organization, startYear, endYear);
		final Map<Integer, Number> ratios = rankedPapers.entrySet().parallelStream().collect(Collectors.toConcurrentMap(
			Map.Entry::getKey,
			it -> {
				final Number count = it.getValue();
				final Number fte = ftes.get(it.getKey());
				if (fte != null && count != null) {
					return Float.valueOf(count.floatValue() / fte.floatValue());
				}
				return Float.valueOf(0f);
			}));
		//
		setComputationDetails(ratios);
		return ratios;
	}

}
