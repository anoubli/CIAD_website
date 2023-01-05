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

package fr.ciadlab.labmanager.service.member;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.Responsibility;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.utils.bap.FrenchBap;
import fr.ciadlab.labmanager.utils.cnu.CnuSection;
import fr.ciadlab.labmanager.utils.conrs.ConrsSection;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the memberships to research organizations.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class MembershipService extends AbstractService {

	private ResearchOrganizationRepository organizationRepository;

	private MembershipRepository membershipRepository;

	private PersonRepository personRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param organizationRepository the organization repository.
	 * @param membershipRepository the membership repository.
	 * @param personRepository the person repository.
	 */
	public MembershipService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired MembershipRepository membershipRepository,
			@Autowired PersonRepository personRepository) {
		super(messages, constants);
		this.organizationRepository = organizationRepository;
		this.membershipRepository = membershipRepository;
		this.personRepository = personRepository;
	}

	/** Replies the members of the organization.
	 * 
	 * @param organization the organization to use.
	 * @param memberFilter describes how members are filtered.
	 * @param statusFilter indicates how to filter according to the member status.
	 * @return the list of the members.
	 */
	@SuppressWarnings("static-method")
	public List<Membership> getOrganizationMembers(ResearchOrganization organization, MemberFiltering memberFilter,
			Predicate<MemberStatus> statusFilter) {
		if (organization != null) {
			Stream<Membership> stream = organization.getMemberships().stream();
			if (memberFilter != null) {
				switch (memberFilter) {
				case ACTIVES:
					stream = stream.filter(it -> it.isActive());
					break;
				case FORMERS:
					stream = stream.filter(it -> it.isFormer());
					break;
				case FUTURES:
					stream = stream.filter(it -> it.isFuture());
					break;
				case ALL:
				default:
					//
				}
			}
			if (statusFilter != null) {
				stream = stream.filter(it -> statusFilter.test(it.getMemberStatus()));
			}
			return stream.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	/** Replies the members of the organization.
	 * 
	 * @param identifier the identifier of the organization to explore.
	 * @param memberFilter describes how members are filtered.
	 * @param statusFilter indicates how to filter according to the member status.
	 * @return the list of the members.
	 */
	public List<Membership> getOrganizationMembers(int identifier, MemberFiltering memberFilter,
			Predicate<MemberStatus> statusFilter) {
		final Optional<ResearchOrganization> optOrg = this.organizationRepository.findById(Integer.valueOf(identifier));
		if (optOrg.isPresent()) {
			return getOrganizationMembers(optOrg.get(), memberFilter, statusFilter);
		}
		return Collections.emptyList();
	}

	/** Replies the list of the organizations that are not those with the given name.
	 *
	 * @param members the members to find into the organizations.
	 * @param currentOrganizationName the current organization name in order to find *others* organizations.
	 * @return map with the member identifiers as keys and the list of the other organizations as values.
	 */
	@SuppressWarnings("static-method")
	public Map<Integer, List<ResearchOrganization>> getOtherOrganizationsForMembers(Collection<Membership> members,
			String currentOrganizationName) {
		if (currentOrganizationName != null) {
			return getOtherOrganizationsForMembers(members,
					it -> !Objects.equals(currentOrganizationName, it.getResearchOrganization().getName()));
		}
		return Collections.emptyMap();
	}

	/** Replies the list of the organizations that are not those with the given name.
	 *
	 * @param members the members to find into the organizations.
	 * @param currentOrganizationId the current organization name in order to find *others* organizations.
	 * @return map with the member identifiers as keys and the list of the other organizations as values.
	 */
	@SuppressWarnings("static-method")
	public Map<Integer, List<ResearchOrganization>> getOtherOrganizationsForMembers(Collection<Membership> members,
			int currentOrganizationId) {
		return getOtherOrganizationsForMembers(members,
				it -> currentOrganizationId != it.getResearchOrganization().getId());
	}

	private static Map<Integer, List<ResearchOrganization>> getOtherOrganizationsForMembers(Collection<Membership> members,
			Predicate<? super Membership> organizationSelector) {
		if (members != null) {
			// Get the memberships in other organizations for the given members
			final Stream<Membership> stream = members.stream().filter(organizationSelector);
			// Group by member id and extract only the research organizations
			final Map<Integer, List<ResearchOrganization>> map = stream.collect(Collectors.groupingBy(
					it -> Integer.valueOf(it.getPerson().getId()),
					TreeMap::new,
					Collectors.mapping(Membership::getResearchOrganization, Collectors.toList())));
			return map;
		}
		return Collections.emptyMap();
	}

	/** Replies the membership that corresponds to the given organization and member identifiers.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @param memberId the identifier of the member.
	 * @return the membership or {@code null}.
	 */
	public Membership getMembership(int organizationId, int memberId) {
		final Optional<Membership> res = this.membershipRepository.findDistinctByResearchOrganizationIdAndPersonId(
				organizationId, memberId);
		if (res.isPresent()) {
			return res.get();
		}
		return null;
	}

	/** Replies the membership of the given person.
	 *
	 * @param memberId the identifier of the person.
	 * @return the memberships of the person.
	 */
	public List<Membership> getMembershipsForPerson(int memberId) {
		return this.membershipRepository.findAllByPersonId(memberId);
	}

	/** Create the membership that corresponds to the given organization and member identifiers.
	 * This function may test if an active membership already exists in the organization for the given
	 * person. In this case, and if the argument {@code forceCreation} is set to {@code false},
	 * then the function does not create the membership and replies the existing membership.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @param organizationAddressId the identifier of the organization address, if known.
	 * @param personId the identifier of the member.
	 * @param startDate the beginning of the membership.
	 * @param endDate the end of the membership.
	 * @param memberStatus the status of the person in the membership.
	 * @param permanentPosition indicates if the position is permanent.
	 * @param responsibility the responsibility of the person during the membership period.
	 * @param cnuSection the section of the CNU to which this membership belongs to.
	 * @param conrsSection the section of the CoNRS to which this membership belongs to.
	 * @param frenchBap the type of job for a not-researcher staff.
	 * @param isMainPosition indicates if the membership is mark as a main position.
	 * @param forceCreation indicates if the membership must be created even if there is an active membership
	 *     in the same organization.
	 * @return a pair that contains the membership (created or not) and a boolean flag indicating
	 *     if the replied membership is a new membership or not.
	 * @throws Exception if the creation cannot be done.
	 */
	public Pair<Membership, Boolean> addMembership(int organizationId, Integer organizationAddressId, int personId,
			LocalDate startDate, LocalDate endDate,
			MemberStatus memberStatus, boolean permanentPosition,
			Responsibility responsibility, CnuSection cnuSection, ConrsSection conrsSection,
			FrenchBap frenchBap, boolean isMainPosition, boolean forceCreation) throws Exception {
		assert memberStatus != null;
		final Optional<ResearchOrganization> optOrg = this.organizationRepository.findById(Integer.valueOf(organizationId));
		if (optOrg.isPresent()) {
			final Optional<Person> optPerson = this.personRepository.findById(Integer.valueOf(personId));
			if (optPerson.isPresent()) {
				final Person person = optPerson.get();
				if (!forceCreation) {
					// We don't need to add the membership is the person is already involved in the organization
					final Optional<Membership> ro = person.getMemberships().stream().filter(
							it -> it.isActive() && it.getResearchOrganization().getId() == organizationId).findAny();
					if (ro.isPresent()) {
						final Membership activeMembership = ro.get();
						final LocalDate sd = activeMembership.getMemberSinceWhen();
						if (endDate == null || sd == null || !endDate.isBefore(sd)) {
							return Pair.of(ro.get(), Boolean.FALSE);
						}
					}
				}
				final ResearchOrganization organization = optOrg.get();
				OrganizationAddress address = null;
				if (organizationAddressId != null && organizationAddressId.intValue() != 0) {
					final Optional<OrganizationAddress> optAdr = organization.getAddresses().stream().filter(it -> organizationAddressId.intValue() == it.getId()).findAny();
					if (optAdr.isPresent()) {
						address = optAdr.get();
					}
				}
				
				final Membership mem = new Membership();
				mem.setPerson(person);
				mem.setResearchOrganization(organization);
				mem.setOrganizationAddress(address);
				mem.setMemberSinceWhen(startDate);
				mem.setMemberToWhen(endDate);
				mem.setMemberStatus(memberStatus);
				mem.setPermanentPosition(permanentPosition);
				mem.setResponsibility(responsibility);
				mem.setCnuSection(cnuSection);
				mem.setConrsSection(conrsSection);
				mem.setFrenchBap(frenchBap);
				mem.setMainPosition(isMainPosition);
				this.membershipRepository.save(mem);
				return Pair.of(mem, Boolean.TRUE);
			}
			throw new IllegalArgumentException("Person not found with id: " + personId); //$NON-NLS-1$
		}
		throw new IllegalArgumentException("Organization not found with id: " + organizationId); //$NON-NLS-1$
	}

	/** Update the membership that corresponds to the given membership identifier identifiers.
	 * 
	 * @param membershipId the identifier of the membership to update.
	 * @param organizationId the identifier of the organization. If it is {@code null}, the organization should not change.
	 * @param organizationAddressId the identifier of the organization address, if known.
	 * @param startDate the new beginning of the membership.
	 * @param endDate the new end of the membership.
	 * @param memberStatus the new status of the person in the membership.
	 * @param permanentPosition indicates if the position is permanent or not.
	 * @param responsibility the responsibility of the person during the membership period.
	 * @param cnuSection the new CNU section, or {@code null} if unknown.
	 * @param conrsSection the section of the CoNRS to which this membership belongs to.
	 * @param frenchBap the type of job for a not-researcher staff.
	 * @param isMainPosition indicates if the membership is mark as a main position.
	 * @return the updated membership.
	 * @throws Exception if the given identifiers cannot be resolved to JPA entities.
	 */
	public Membership updateMembershipById(int membershipId, Integer organizationId, Integer organizationAddressId,
			LocalDate startDate, LocalDate endDate,
			MemberStatus memberStatus, boolean permanentPosition, Responsibility responsibility,
			CnuSection cnuSection, ConrsSection conrsSection, FrenchBap frenchBap,
			boolean isMainPosition) throws Exception {
		final Optional<Membership> res = this.membershipRepository.findById(Integer.valueOf(membershipId));
		if (res.isPresent()) {
			final Membership membership = res.get();
			if (organizationId != null) {
				final Optional<ResearchOrganization> res0 = this.organizationRepository.findById(organizationId);
				if (res0.isEmpty()) {
					throw new IllegalArgumentException("Cannot find organization with id: " + organizationId); //$NON-NLS-1$
				}
				membership.setResearchOrganization(res0.get());
				OrganizationAddress address = null;
				if (organizationAddressId != null && organizationAddressId.intValue() != 0) {
					final Optional<OrganizationAddress> optAdr = res0.get().getAddresses().stream().filter(it -> organizationAddressId.intValue() == it.getId()).findAny();
					if (optAdr.isPresent()) {
						address = optAdr.get();
					}
				}
				membership.setOrganizationAddress(address);
			}
			membership.setMemberSinceWhen(startDate);
			membership.setMemberToWhen(endDate);
			if (memberStatus != null) {
				membership.setMemberStatus(memberStatus);
			}
			membership.setPermanentPosition(permanentPosition);
			membership.setResponsibility(responsibility);
			membership.setCnuSection(cnuSection);
			membership.setConrsSection(conrsSection);
			membership.setFrenchBap(frenchBap);
			membership.setMainPosition(isMainPosition);
			this.membershipRepository.save(membership);
			return membership;
		}
		throw new IllegalArgumentException("Cannot find membership with id: " + membershipId); //$NON-NLS-1$
	}

	/** Delete the membership with the given identifier.
	 *
	 * @param membershipId the identifier of the membership to be deleted.
	 * @throws Exception in case of error.
	 */
	@Transactional
	public void removeMembership(int membershipId) throws Exception {
		final Integer mid = Integer.valueOf(membershipId);
		final Optional<Membership> optMbr = this.membershipRepository.findById(mid);
		if (optMbr.isEmpty()) {
			throw new IllegalStateException("Membership not found with id: " + membershipId); //$NON-NLS-1$
		}
		final Membership mbr = optMbr.get();
		final Person person = mbr.getPerson();
		if (person != null) {
			person.getMemberships().remove(mbr);
			mbr.setPerson(null);
		}
		final ResearchOrganization organization = mbr.getResearchOrganization();
		if (organization != null) {
			organization.getMemberships().remove(mbr);
			mbr.setResearchOrganization(null);
		}
		this.membershipRepository.deleteById(mid);
	}

	/** Replies the persons in the organization of the given identifier.
	 * This function does not consider the suborganizations.
	 * The function {@link #getMembersOf(int)} provides the members for an organization and the associated
	 * suborganizations.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @return the persons.
	 * @see #getMembersOf(int)
	 */
	public Set<Person> getDirectMembersOf(int organizationId) {
		return this.personRepository.findDistinctByMembershipsResearchOrganizationId(organizationId);
	}

	/** Replies the persons in the organization of the given identifier and its suborganizations.
	 * The function {@link #getDirectMembersOf(int)} provides the members for an organization and not of the associated
	 * suborganizations.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @return the persons.
	 * @see #getDirectMembersOf(int)
	 */
	public Set<Person> getMembersOf(int organizationId) {
		final Optional<ResearchOrganization> optOrg = this.organizationRepository.findById(Integer.valueOf(organizationId));
		if (optOrg.isPresent()) {
			final ResearchOrganization organization = optOrg.get();

			final Set<Person> persons = new HashSet<>();

			final Deque<ResearchOrganization> organizationCandidates = new LinkedList<>();
			organizationCandidates.add(organization);

			do {
				final ResearchOrganization currentOrganization = organizationCandidates.removeFirst();
				persons.addAll(getDirectMembersOf(currentOrganization.getId()));
				organizationCandidates.addAll(currentOrganization.getSubOrganizations());
			} while (!organizationCandidates.isEmpty());

			return persons;
		}
		return Collections.emptySet();
	}

	/** Replies the persons in the organization of the given name, with the given status.
	 * 
	 * @param organizationName the name of the organization.
	 * @param status the person status.
	 * @return the persons
	 */
	public Set<Person> getPersonsByOrganizationNameStatus(String organizationName, MemberStatus status) {
		return this.personRepository.findDistinctByMembershipsResearchOrganizationNameAndMembershipsMemberStatus(
				organizationName, status);
	}

	/** Replies the persons in the organization of the given acronym, with the given status.
	 * 
	 * @param organizationAcronym the acronym of the organization.
	 * @param status the person status.
	 * @return the persons
	 */
	public Set<Person> getPersonsByOrganizationAcronymStatus(String organizationAcronym, MemberStatus status) {
		return this.personRepository.findDistinctByMembershipsResearchOrganizationAcronymAndMembershipsMemberStatus(
				organizationAcronym, status);
	}

}
