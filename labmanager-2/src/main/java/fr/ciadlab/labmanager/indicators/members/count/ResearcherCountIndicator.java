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
import fr.ciadlab.labmanager.utils.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Count the current number of researchers in a specific organization.
 * Researchers may be permanent or not.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 * @see PermanentResearcherCountIndicator
 */
@Component
public class ResearcherCountIndicator extends AbstractInstantIndicator {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public ResearcherCountIndicator(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants) {
		super(messages, constants);
	}

	@Override
	public String getName() {
		return getMessage("researcherCountIndicator.name"); //$NON-NLS-1$
	}

	@Override
	public String getLabel(Unit unit) {
		return getLabelWithoutYears("researcherCountIndicator.label"); //$NON-NLS-1$
	}

	@Override
	protected Number computeValue(ResearchOrganization organization) {
		final List<Membership> researchers = organization.getMemberships().parallelStream().filter(
				it -> {
					if (it.isActive()) {
						final MemberStatus status = it.getMemberStatus();
						return !status.isExternalPosition() && status != MemberStatus.PHD_STUDENT && status.isResearcher();
					}
					return false;
				})
				.collect(Collectors.toList());
		final long nb = researchers.size();
		setComputationDetails(researchers, it -> it.getPerson().getFullNameWithLastNameFirst());
		return Long.valueOf(nb);
	}

}
