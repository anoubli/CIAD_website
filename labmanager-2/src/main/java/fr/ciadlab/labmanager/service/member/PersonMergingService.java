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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.invitation.PersonInvitation;
import fr.ciadlab.labmanager.entities.jury.JuryMembership;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.supervision.Supervision;
import fr.ciadlab.labmanager.entities.supervision.Supervisor;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.service.invitation.PersonInvitationService;
import fr.ciadlab.labmanager.service.jury.JuryMembershipService;
import fr.ciadlab.labmanager.service.member.PersonService.PersonDuplicateCallback;
import fr.ciadlab.labmanager.service.supervision.SupervisionService;
import fr.ciadlab.labmanager.utils.names.PersonNameComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the merging persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class PersonMergingService extends AbstractService {

	private final PersonRepository personRepository;

	private final PersonService personService;

	private final MembershipService organizationMembershipService;

	private final MembershipRepository organizationMembershipRepository;

	private final JuryMembershipService juryMembershipService;

	private final SupervisionService supervisionService;

	private final PersonInvitationService invitationService;

	private PersonNameComparator nameComparator;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param personRepository the person repository.
	 * @param personService the person service.
	 * @param organizationMembershipService the service for managing the organization memberships.
	 * @param organizationMembershipRepository the repository for managing the organization memberships.
	 * @param juryMembershipService the service for managing the jury memberships.
	 * @param supervisionService the service for managing the supervisions.
	 * @param invitationService the service for managing the invitations.
	 * @param nameComparator the comparator of person names.
	 */
	public PersonMergingService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonRepository personRepository,
			@Autowired PersonService personService,
			@Autowired MembershipService organizationMembershipService,
			@Autowired MembershipRepository organizationMembershipRepository,
			@Autowired JuryMembershipService juryMembershipService,
			@Autowired SupervisionService supervisionService,
			@Autowired PersonInvitationService invitationService,
			@Autowired PersonNameComparator nameComparator) {
		super(messages, constants);
		this.personRepository = personRepository;
		this.personService = personService;
		this.organizationMembershipService = organizationMembershipService;
		this.organizationMembershipRepository = organizationMembershipRepository;
		this.juryMembershipService = juryMembershipService;
		this.supervisionService = supervisionService;
		this.invitationService = invitationService;
		this.nameComparator = nameComparator;
	}

	/** Replies the duplicate person names.
	 * The replied list contains groups of persons who have similar names.
	 *
	 * @param comparator comparator of persons that is used for sorting the groups of duplicates. If it is {@code null},
	 *      a {@link PersonComparator} is used.
	 * @param callback the callback invoked during the building.
	 * @return the duplicate persons that is finally computed.
	 * @throws Exception if a problem occurred during the building.
	 */
	public List<Set<Person>> getPersonDuplicates(Comparator<? super Person> comparator, PersonDuplicateCallback callback) throws Exception {
		// Each list represents a group of authors that could be duplicate
		final List<Set<Person>> matchingAuthors = new ArrayList<>();

		// Copy the list of authors into another list in order to enable its
		// modification during the function's process
		final List<Person> authorsList = new ArrayList<>(this.personRepository.findAll());

		final Comparator<? super Person> theComparator = comparator == null ? EntityUtils.getPreferredPersonComparator() : comparator;

		final int total = authorsList.size();
		// Notify the callback
		if (callback != null) {
			callback.onDuplicate(0, 0, total);
		}
		int duplicateCount = 0;
		
		for (int i = 0; i < authorsList.size() - 1; ++i) {
			final Person referencePerson = authorsList.get(i);

			final Set<Person> currentMatching = new TreeSet<>(theComparator);
			currentMatching.add(referencePerson);

			final ListIterator<Person> iterator2 = authorsList.listIterator(i + 1);
			while (iterator2.hasNext()) {
				final Person otherPerson = iterator2.next();
				if (this.nameComparator.isSimilar(
						referencePerson.getFirstName(), referencePerson.getLastName(),
						otherPerson.getFirstName(), otherPerson.getLastName())) {
					currentMatching.add(otherPerson);
					++duplicateCount;
					// Consume the other person to avoid to be treated twice times
					iterator2.remove();
				}
			}
			if (currentMatching.size() > 1) {
				matchingAuthors.add(currentMatching);
			}
			// Notify the callback
			if (callback != null) {
				callback.onDuplicate(i, duplicateCount, total);
			}
		}

		return matchingAuthors;
	}

	/** Merge the persons and authorships by replacing those with an old author name by those with the new author name.
	 * This function enables to group the publications that are attached to two different author names
	 * and select one of the name as the final author name.
	 *
	 * @param source the list of the identifiers of the persons to remove and replace by the target person.
	 * @param target the identifier of the target person who should replace the source persons.
	 * @throws Exception if the merging cannot be completed.
	 */
	public void mergePersonsById(Collection<Integer> source, Integer target) throws Exception {
		assert target != null;
		assert source != null;
		final Optional<Person> optTarget = this.personRepository.findById(target);
		if (optTarget.isPresent()) {
			final Person targetPerson = optTarget.get();
			final List<Person> sourcePersons = this.personRepository.findAllById(source);
			if (sourcePersons.size() != source.size()) {
				for (final Person sp : sourcePersons) {
					if (!source.contains(Integer.valueOf(sp.getId()))) {
						throw new IllegalArgumentException("Source person not found with identifier: " + sp.getId()); //$NON-NLS-1$
					}
				}
				throw new IllegalArgumentException("Source person not found"); //$NON-NLS-1$
			}
			mergePersons(sourcePersons, targetPerson);
		} else {
			throw new IllegalArgumentException("Target person not found with identifier: " + target); //$NON-NLS-1$
		}
	}

	/** Merge the persons and authorships by replacing those with an old author name by those with the new author name.
	 * This function enables to group the publications that are attached to two different author names
	 * and select one of the name as the final author name.
	 *
	 * @param sources the list of persons to remove and replace by the target person.
	 * @param target the target person who should replace the source persons.
	 * @throws Exception if the merging cannot be completed.
	 */
	public void mergePersons(Iterable<Person> sources, Person target) throws Exception {
		assert target != null;
		assert sources != null;
		boolean changed = false;
		for (final Person source : sources) {
			if (source.getId() != target.getId()) {
				getLogger().info("Reassign to " + target.getFullName() + " the elements of " + source.getFullName()); //$NON-NLS-1$ //$NON-NLS-2$
				boolean lchange = reassignPublications(source, target);
				lchange = reassignOrganizationMemberships(source, target) || lchange;
				lchange = reassignJuryMemberships(source, target) || lchange;
				lchange = reassignSupervisions(source, target) || lchange;
				lchange = reassignInvitations(source, target) || lchange;
				//
				this.personService.removePerson(source.getId());
				changed = changed || lchange;
			}
		}
		if (changed) {
			this.personRepository.save(target);
		}
	}

	/** Re-assign the publication attached to the source person to the target person.
	 * 
	 * @param sources the person to remove and replace by the target person.
	 * @param target the target person who should replace the source persons.
	 * @return {@code true} if publication has changed.
	 * @throws Exception if the change cannot be completed.
	 */
	@SuppressWarnings("static-method")
	protected boolean reassignPublications(Person source, Person target) throws Exception {
		final Set<Authorship> autPubs = source.getAuthorships();
		final Iterator<Authorship> iterator = autPubs.iterator();
		boolean changed = false;
		while (iterator.hasNext()) {
			final Authorship authorship = iterator.next();
			// Test if the target is co-author. If yes, don't do re-assignment to avoid
			// the same person multiple times as author.
			if (!authorship.getPublication().getAuthors().contains(target)) {
				authorship.setPerson(target);
				iterator.remove();
				target.getAuthorships().add(authorship);
				changed = true;
			}
		}
		return changed;
	}

	/** Re-assign the organization memberships attached to the source person to the target person.
	 * 
	 * @param sources the person to remove and replace by the target person.
	 * @param target the target person who should replace the source persons.
	 * @return {@code true} if organization membership has changed.
	 * @throws Exception if the change cannot be completed.
	 */
	protected boolean reassignOrganizationMemberships(Person source, Person target) throws Exception {
		boolean changed = false;
		for (final Membership membership : this.organizationMembershipService.getMembershipsForPerson(source.getId())) {
			source.getMemberships().remove(membership);
			membership.setPerson(target);
			target.getMemberships().add(membership);
			this.organizationMembershipRepository.save(membership);
			changed = true;
		}
		return changed;
	}

	/** Re-assign the jury memberships attached to the source person to the target person.
	 * 
	 * @param sources the person to remove and replace by the target person.
	 * @param target the target person who should replace the source persons.
	 * @return {@code true} if jury membership has changed.
	 * @throws Exception if the change cannot be completed.
	 */
	protected boolean reassignJuryMemberships(Person source, Person target) throws Exception {
		final Set<JuryMembership> changed = new TreeSet<>(EntityUtils.getPreferredJuryMembershipComparator());
		for (final JuryMembership jmembership : this.juryMembershipService.getMembershipsForPerson(source.getId())) {
			jmembership.setPerson(target);
			changed.add(jmembership);
		}
		for (final JuryMembership jmembership : this.juryMembershipService.getMembershipsForCandidate(source.getId())) {
			jmembership.setCandidate(target);
			changed.add(jmembership);
		}
		for (final JuryMembership jmembership : this.juryMembershipService.getMembershipsForPromoter(source.getId())) {
			final List<Person> list = new ArrayList<>(jmembership.getPromoters());
			list.remove(source);
			list.add(target);
			jmembership.setPromoters(list);
			changed.add(jmembership);
		}
		if (changed.isEmpty()) {
			return false;
		}
		for (final JuryMembership mbr : changed) {
			this.juryMembershipService.save(mbr);
		}
		return true;
	}

	/** Re-assign the supervisions attached to the source person to the target person.
	 * 
	 * @param sources the person to remove and replace by the target person.
	 * @param target the target person who should replace the source persons.
	 * @return {@code true} if supervision has changed.
	 * @throws Exception if the change cannot be completed.
	 */
	protected boolean reassignSupervisions(Person source, Person target) throws Exception {
		final Set<Supervisor> changed = new TreeSet<>(EntityUtils.getPreferredSupervisorComparator());
		// Do not need to change the supervised person's membership because it is done by reassignJuryMemberships()
		//
		for (final Supervision supervision : this.supervisionService.getSupervisionsForSupervisor(source.getId())) {
			for (final Supervisor supervisor : supervision.getSupervisors()) {
				if (supervisor.getSupervisor().getId() == source.getId()) {
					supervisor.setSupervisor(target);
					changed.add(supervisor);
				}
			}
		}
		//
		if (changed.isEmpty()) {
			return false;
		}
		for (final Supervisor sup : changed) {
			this.supervisionService.save(sup);
		}
		return true;
	}

	/** Re-assign the invitations attached to the source person to the target person.
	 * 
	 * @param sources the person to remove and replace by the target person.
	 * @param target the target person who should replace the source persons.
	 * @return {@code true} if invitation has changed.
	 * @throws Exception if the change cannot be completed.
	 */
	protected boolean reassignInvitations(Person source, Person target) throws Exception {
		final Set<PersonInvitation> changed = new TreeSet<>(EntityUtils.getPreferredPersonInvitationComparator());
		for (final PersonInvitation invitation : this.invitationService.getInvitationsForPerson(source.getId())) {
			if (invitation.getGuest().getId() == source.getId()) {
				invitation.setGuest(target);
				changed.add(invitation);
			}
			if (invitation.getInviter().getId() == source.getId()) {
				invitation.setInviter(target);
				changed.add(invitation);
			}
		}
		//
		if (changed.isEmpty()) {
			return false;
		}
		for (final PersonInvitation inv : changed) {
			this.invitationService.save(inv);
		}
		return true;
	}

}
