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

package fr.ciadlab.labmanager.indicators.members.count;

import java.util.List;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.indicators.AbstractInstantIndicator;
import fr.ciadlab.labmanager.indicators.members.fte.PermanentResearcherFteIndicator;
import fr.ciadlab.labmanager.utils.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Count the current number of permanent researchers in a specific organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 * @see ResearcherCountIndicator
 * @see PermanentResearcherFteIndicator
 */
@Component
public class PermanentResearcherCountIndicator extends AbstractInstantIndicator {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public PermanentResearcherCountIndicator(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants) {
		super(messages, constants);
	}

	@Override
	public String getName() {
		return getMessage("permanentResearcherCountIndicator.name"); //$NON-NLS-1$
	}

	@Override
	public String getLabel(Unit unit) {
		return getLabelWithoutYears("permanentResearcherCountIndicator.label"); //$NON-NLS-1$
	}

	@Override
	protected Number computeValue(ResearchOrganization organization) {
		final List<Membership> researchers = organization.getMemberships()
				.parallelStream()
				.filter(PermanentResearcherCountIndicator::isPermanentResearcher)
				.collect(Collectors.toList());
		final long nb = researchers.size();
		setComputationDetails(researchers, it -> it.getPerson().getFullNameWithLastNameFirst());
		return Long.valueOf(nb);
	}

	/** Replies if the given membership is for a permanent researcher.
	 *
	 * @param membership the membership to test.
	 * @return {@code true} if the membership is for a permanent researcher.
	 */
	public static boolean isPermanentResearcher(Membership membership) {
		if (membership != null && membership.isActive() && membership.isPermanentPosition()) {
			final MemberStatus status = membership.getMemberStatus();
			return !status.isExternalPosition() && status != MemberStatus.PHD_STUDENT
					&& status.isResearcher() && status.isPermanentPositionAllowed();
		}
		return false;
	}

}
