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

package fr.ciadlab.labmanager.service.publication;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.service.AbstractService;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation of a service for managing the publications.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPublicationService extends AbstractService {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public AbstractPublicationService(MessageSourceAccessor messages, Constants constants) {
		super(messages, constants);
	}

	/** Filter the given publication for returning only those that have authors with an active membership.
	 *
	 * @param <P> the type of the publications to be fitlered.
	 * @param publications the publications to filter.
	 * @param organizationId the identifier of the organization for which the publications are accepted.
	 * @param includeSubOrganizations indicates if the members of the suborganizations are considered.
	 * @return the filtered publications.
	 */
	protected static <P extends Publication> Set<P> filterPublicationsWithMemberships(Set<P> publications,
			int organizationId, boolean includeSubOrganizations) {
		final Function<Person, Stream<Membership>> streamBuilder;
		if (includeSubOrganizations) {
			streamBuilder = it -> buildStream(it, organizationId);
		} else {
			streamBuilder = it -> buildStreamStrict(it, organizationId);
		}
		return publications.stream().filter(it -> hasActiveAuthor(it, streamBuilder))
					.collect(Collectors.toUnmodifiableSet());
	}

	private static boolean isOrganizationOf(Membership membership, int organizationId) {
		ResearchOrganization ro = membership.getResearchOrganization();
		while (ro != null) {
			if (ro.getId() == organizationId) {
				return true;
			}
			ro = ro.getSuperOrganization();
		}
		return false;
	}
	
	private static Stream<Membership> buildStream(Person author, int organizationId) {
		return author.getMemberships().stream().filter(it -> isOrganizationOf(it, organizationId));
	}
	
	private static Stream<Membership> buildStreamStrict(Person author, int organizationId) {
		return author.getMemberships().stream().filter(it -> it.getId() == organizationId);
	}

	private static boolean hasActiveAuthor(Publication publication, Function<Person, Stream<Membership>> streamBuilder) {
		final LocalDate pubDate = publication.getPublicationDate();
		if (pubDate != null) {
			for (final Person author : publication.getAuthors()) {
				final Stream<Membership> stream = streamBuilder.apply(author).filter(it -> it.isActiveAt(pubDate));
				if (stream.count() > 0) {
					return true;
				}
			}
		} else {
			final int year = publication.getPublicationYear();
			final LocalDate start = LocalDate.of(year, 1, 1);
			final LocalDate end = LocalDate.of(year, 12, 31);
			for (final Person author : publication.getAuthors()) {
				final Stream<Membership> stream = streamBuilder.apply(author).filter(it -> it.isActiveIn(start, end));
				if (stream.count() > 0) {
					return true;
				}
			}
		}
		return false;
	}

}
