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

package fr.ciadlab.labmanager.repository.member;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;

import org.springframework.data.jpa.repository.JpaRepository;

/** JPA Repository for the persons.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PersonRepository extends JpaRepository<Person, Integer> {

	/** Replies a person who has the given first and last names.
	 * 
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @return the person.
	 */
	Optional<Person> findDistinctByFirstNameAndLastName(String firstName, String lastName);

	/** Replies a person who has the given identifier for her/his webpage.
	 * 
	 * @param identifier the expected identifier of the webpage of the person.
	 * @return the person.
	 */
	Optional<Person> findDistinctByWebPageId(String identifier);

	/** Replies the persons who have the given first and last names.
	 * 
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @return the persons.
	 */
	Set<Person> findByFirstNameAndLastName(String firstName, String lastName);

	/** Replies the persons in the organization with the given name and who have the given status.
	 *
	 * @param organizationName the name of the organization.
	 * @param status the member status
	 * @return the persons.
	 */
	Set<Person> findDistinctByMembershipsResearchOrganizationNameAndMembershipsMemberStatus(
			String organizationName, MemberStatus status);

	/** Replies the persons in the organization with the given acronym and who have the given status.
	 *
	 * @param organizationAcronym the acronym of the organization.
	 * @param status the member status
	 * @return the persons.
	 */
	Set<Person> findDistinctByMembershipsResearchOrganizationAcronymAndMembershipsMemberStatus(
			String organizationAcronym, MemberStatus status);

	/** Replies the persons in the organization with the given identifier.
	 *
	 * @param id the identifier of the organization.
	 * @return the persons.
	 */
	Set<Person> findDistinctByMembershipsResearchOrganizationId(int id);

	/** Replies the persons who authored the publication with the given identifier.
	 *
	 * @param id the identifier of the publication.
	 * @return the persons.
	 */
	List<Person> findByAuthorshipsPublicationIdOrderByAuthorshipsAuthorRank(int id);
}
