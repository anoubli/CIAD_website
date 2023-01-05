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

package fr.ciadlab.labmanager.service.jury;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.jury.JuryMembership;
import fr.ciadlab.labmanager.entities.jury.JuryMembershipType;
import fr.ciadlab.labmanager.entities.jury.JuryType;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.repository.jury.JuryMembershipRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.arakhne.afc.util.CountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the jury memberships.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Service
public class JuryMembershipService extends AbstractService {

	private JuryMembershipRepository membershipRepository;

	private PersonService personService;

	private PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param membershipRepository the jury membership repository.
	 * @param personService the service for accessing the persons.
	 * @param nameParser the parser of person names.
	 */
	public JuryMembershipService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JuryMembershipRepository membershipRepository,
			@Autowired PersonService personService,
			@Autowired PersonNameParser nameParser) {
		super(messages, constants);
		this.membershipRepository = membershipRepository;
		this.personService = personService;
		this.nameParser = nameParser;
	}

	/** Replies the list of jury memberships for the person with the given identifier.
	 *
	 * @param personId the identifier of the person.
	 * @return the list of jury memberships.
	 */
	public List<JuryMembership> getMembershipsForPerson(int personId) {
		return this.membershipRepository.findAllByPersonId(personId);
	}

	/** Replies the list of jury memberships for the candidate with the given identifier.
	 *
	 * @param candidateId the identifier of the candidate.
	 * @return the list of jury memberships.
	 */
	public List<JuryMembership> getMembershipsForCandidate(int candidateId) {
		return this.membershipRepository.findAllByCandidateId(candidateId);
	}

	/** Replies the list of jury memberships for the promoter with the given identifier.
	 *
	 * @param promoterId the identifier of the promoter.
	 * @return the list of jury memberships.
	 */
	public List<JuryMembership> getMembershipsForPromoter(int promoterId) {
		return this.membershipRepository.findAllByPromotersId(promoterId);
	}

	/** Add a jury membership.
	 *
	 * @param person the identifier of the person who is involved in the jury.
	 * @param date the date of the defense. 
	 * @param membershipType the type of membership.
	 * @param defenseType the type of defense.
	 * @param title the title of the evaluated works.
	 * @param candidate the name of the candidate. It is a database identifier (for known person) or full name
	 *     (for unknown person).
	 * @param university the name of the university in which the defense was done.
	 * @param country the country of the university.
	 * @param promoters the list of promoters/directors. It is a list of database identifiers (for known persons) or full names
	 *     (for unknown persons).
	 * @return the created jury membership.
	 */
	public JuryMembership addJuryMembership(int person, LocalDate date, JuryMembershipType membershipType,
			JuryType defenseType, String title, String candidate, String university, CountryCode country,
			List<String> promoters) {
		final Person personObj = this.personService.getPersonById(person);
		if (personObj != null) {
			final JuryMembership mem = new JuryMembership();
			updateJuryMembershipWithoutSaving(mem, personObj, date, membershipType, defenseType, title, candidate, university, country, promoters);
			return this.membershipRepository.save(mem);
		}
		throw new IllegalArgumentException("Person not found with id: " + person); //$NON-NLS-1$
	}

	/** Update the jury membership with the given identifier.
	 *
	 * @param membershipId the identifier of the jury membership to be updated.
	 * @param person the identifier of the person who is involved in the jury.
	 * @param date the date of the defense. 
	 * @param membershipType the type of membership.
	 * @param defenseType the type of defense.
	 * @param title the title of the evaluated works.
	 * @param candidate the name of the candidate. It is a database identifier (for known person) or full name
	 *     (for unknown person).
	 * @param university the name of the university in which the defense was done.
	 * @param country the country of the university.
	 * @param promoters the list of promoters/directors. It is a list of database identifiers (for known persons) or full names
	 *     (for unknown persons).
	 * @return the updated jury membership.
	 */
	public JuryMembership updateJuryMembership(int membershipId, int person, LocalDate date, JuryMembershipType membershipType,
			JuryType defenseType, String title, String candidate, String university, CountryCode country,
			List<String> promoters) {
		final Optional<JuryMembership> optMembership = this.membershipRepository.findById(Integer.valueOf(membershipId));
		if (optMembership.isEmpty()) {
			throw new IllegalArgumentException("Jury membership not found with id: " + membershipId); //$NON-NLS-1$
		}
		final JuryMembership membership = optMembership.get();
		final Person personObj = this.personService.getPersonById(person);
		if (personObj == null) {
			throw new IllegalArgumentException("Person not found with id: " + person); //$NON-NLS-1$
		}
		updateJuryMembershipWithoutSaving(membership, personObj, date, membershipType, defenseType, title, candidate, university, country, promoters);
		return this.membershipRepository.save(membership);
	}

	/** Update the jury membership with the given identifier.
	 *
	 * @param membership the jury membership to be updated.
	 * @param person the person who is involved in the jury.
	 * @param date the date of the defense. 
	 * @param membershipType the type of membership.
	 * @param defenseType the type of defense.
	 * @param title the title of the evaluated works.
	 * @param candidate the name of the candidate. It is a database identifier (for known person) or full name
	 *     (for unknown person).
	 * @param university the name of the university in which the defense was done.
	 * @param country the country of the university.
	 * @param promoters the list of promoters/directors. It is a list of database identifiers (for known persons) or full names
	 *     (for unknown persons).
	 */
	public void updateJuryMembershipWithoutSaving(JuryMembership membership, Person person, LocalDate date, JuryMembershipType membershipType,
			JuryType defenseType, String title, String candidate, String university, CountryCode country,
			List<String> promoters) {
		if (defenseType == JuryType.BAC && membershipType != JuryMembershipType.PRESIDENT) {
			throw new IllegalStateException("Unsupported type of jury membership for " + defenseType.name() + " and " + membershipType.name()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		//
		membership.setPerson(person);
		membership.setDate(date);
		membership.setType(membershipType);
		membership.setDefenseType(defenseType);
		membership.setTitle(title);
		membership.setCandidate(extractPerson(candidate, true, this.personService, this.nameParser));
		membership.setUniversity(university);
		membership.setCountry(country);
		final List<Person> promoterArray = new ArrayList<>();
		forEarchPerson(promoters, true, this.personService, this.nameParser, it -> promoterArray.add(it));
		membership.setPromoters(promoterArray);
	}

	/** Delete the jury membership with the given identifier.
	 *
	 * @param membershipId the identifier of the jury membership to be deleted.
	 * @throws Exception in case of error.
	 */
	@Transactional
	public void removeJuryMembership(int membershipId) throws Exception {
		final Integer mid = Integer.valueOf(membershipId);
		final Optional<JuryMembership> optMbr = this.membershipRepository.findById(mid);
		if (optMbr.isEmpty()) {
			throw new IllegalStateException("Jury membership not found with id: " + membershipId); //$NON-NLS-1$
		}
		this.membershipRepository.deleteById(mid);
	}

	/** Save the given jury membership into the database.
	 *
	 * @param membership the jury membership to save.
	 */
	public void save(JuryMembership membership) {
		this.membershipRepository.save(membership);
	}

}
