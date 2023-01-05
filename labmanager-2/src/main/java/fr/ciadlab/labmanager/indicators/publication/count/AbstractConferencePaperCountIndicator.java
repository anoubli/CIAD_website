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
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.ConferencePaper;
import fr.ciadlab.labmanager.indicators.AbstractAnnualIndicator;
import fr.ciadlab.labmanager.service.publication.type.ConferencePaperService;
import org.springframework.context.support.MessageSourceAccessor;

/** Count the number of conference papers for an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public abstract class AbstractConferencePaperCountIndicator extends AbstractAnnualIndicator {

	private ConferencePaperService conferencePaperService;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param conferencePaperService the service for accessing the conference papers.
	 */
	public AbstractConferencePaperCountIndicator(
			MessageSourceAccessor messages,
			Constants constants,
			ConferencePaperService conferencePaperService) {
		this(messages, constants, AbstractAnnualIndicator::sum, conferencePaperService);
	}

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param mergingFunction the function that should be used for merging the annual values.
	 *      If it is {@code null}, the {@link #sum(Map)} is used.
	 * @param conferencePaperService the service for accessing the conference papers.
	 */
	public AbstractConferencePaperCountIndicator(
			MessageSourceAccessor messages,
			Constants constants,
			Function<Map<Integer, Number>, Number> mergingFunction,
			ConferencePaperService conferencePaperService) {
		super(messages, constants, mergingFunction);
		this.conferencePaperService = conferencePaperService;
	}

	@Override
	public Map<Integer, Number> getValuesPerYear(ResearchOrganization organization, int startYear, int endYear) {
		final Set<ConferencePaper> papers = this.conferencePaperService.getConferencePapersByOrganizationId(organization.getId(), true);
		//
		final Stream<ConferencePaper> stream = filterByYearWindow(true, papers, it -> Integer.valueOf(it.getPublicationYear()))
				.filter(it -> {
					final PublicationType type = it.getType();
					return type == PublicationType.INTERNATIONAL_CONFERENCE_PAPER || type == PublicationType.NATIONAL_CONFERENCE_PAPER; 
				});
		//
		final Map<Integer, Number> annualCounts = stream.collect(Collectors.toConcurrentMap(
				it -> Integer.valueOf(it.getPublicationYear()),
				it -> Integer.valueOf(1),
				(a, b) -> Integer.valueOf(a.intValue() + b.intValue())));
		//
		setComputationDetails(annualCounts);
		return annualCounts;
	}

}
