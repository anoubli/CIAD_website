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

package fr.ciadlab.labmanager.service.invitation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.invitation.PersonInvitation;
import fr.ciadlab.labmanager.entities.invitation.PersonInvitationType;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.repository.invitation.PersonInvitationRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.arakhne.afc.util.CountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the person invitations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@Service
public class PersonInvitationService extends AbstractService {

	private PersonInvitationRepository invitationRepository;

	private PersonService personService;

	private PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param invitationRepository the person invitation repository.
	 * @param personService the service for accessing the person.
	 * @param nameParser the parser of person names.
	 */
	public PersonInvitationService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonInvitationRepository invitationRepository,
			@Autowired PersonService personService,
			@Autowired PersonNameParser nameParser) {
		super(messages, constants);
		this.invitationRepository = invitationRepository;
		this.personService = personService;
		this.nameParser = nameParser;
	}

	/** Replies the invitations for the given person whatever if he/she is guest or inviter.
	 *
	 * @param personId the identifier of the person to search for.
	 * @return the invitations for the person.
	 */
	public List<PersonInvitation> getInvitationsForPerson(int personId) {
		return this.invitationRepository.findAllByGuestIdOrInviterId(personId, personId);
	}

	/** Add a person invitation.
	 *
	 * @param person the identifier of the person who is involved in invitation as guest or inviter.
	 * @param guest the name of the guest person.
	 * @param inviter the name of the inviting person.
	 * @param startDate the start date of the invitation. 
	 * @param endDate the end date of the invitation. 
	 * @param type the type of invitation.
	 * @param title the title of the works.
	 * @param university the name of the university of the partner person.
	 * @param country the country of the university.
	 * @return the created invitation.
	 */
	public PersonInvitation addPersonInvitation(int person,
			String guest, String inviter,
			LocalDate startDate, LocalDate endDate,
			PersonInvitationType type,
			String title, String university, CountryCode country) {
		final Person personObj = this.personService.getPersonById(person);
		if (personObj != null) {
			final PersonInvitation inv = new PersonInvitation();
			updatePersonInvitationWithoutSaving(inv, personObj, guest, inviter, startDate, endDate, type, title, university, country);
			return this.invitationRepository.save(inv);
		}
		throw new IllegalArgumentException("Person not found with id: " + person); //$NON-NLS-1$
	}

	/** Update a person invitation.
	 *
	 * @param invitation the identifier of the invitation to be updated.
	 * @param person the identifier of the person who is involved in invitation as guest or inviter.
	 * @param guest the name of the guest person.
	 * @param inviter the name of the inviting person.
	 * @param startDate the start date of the invitation. 
	 * @param endDate the end date of the invitation. 
	 * @param type the type of invitation.
	 * @param title the title of the works.
	 * @param university the name of the university of the partner person.
	 * @param country the country of the university.
	 * @return the created invitation.
	 */
	public PersonInvitation updatePersonInvitation(
			int invitation,
			int person,
			String guest, String inviter,
			LocalDate startDate, LocalDate endDate,
			PersonInvitationType type,
			String title, String university, CountryCode country) {
		final Optional<PersonInvitation> optInvitation = this.invitationRepository.findById(Integer.valueOf(invitation));
		if (optInvitation.isEmpty()) {
			throw new IllegalArgumentException("Invitation not found with id: " + invitation); //$NON-NLS-1$
		}
		final PersonInvitation invitationObj = optInvitation.get();
		//
		final Person personObj = this.personService.getPersonById(person);
		if (personObj == null) {
			throw new IllegalArgumentException("Person not found with id: " + person); //$NON-NLS-1$
		}
		//
		updatePersonInvitationWithoutSaving(invitationObj, personObj, guest, inviter, startDate, endDate, type, title, university, country);
		return this.invitationRepository.save(invitationObj);
	}

	/** Update a person invitation without saving the JPA.
	 *
	 * @param invitation the invitation to be updated.
	 * @param person the person who is involved in invitation as guest or inviter.
	 * @param guest the name of the guest person.
	 * @param inviter the name of the inviting person.
	 * @param startDate the start date of the invitation. 
	 * @param endDate the end date of the invitation. 
	 * @param type the type of invitation.
	 * @param title the title of the works.
	 * @param university the name of the university of the partner person.
	 * @param country the country of the university.
	 */
	public void updatePersonInvitationWithoutSaving(
			PersonInvitation invitation,
			Person person,
			String guest, String inviter,
			LocalDate startDate, LocalDate endDate,
			PersonInvitationType type,
			String title, String university, CountryCode country) {
		final Person guestObj = extractPerson(guest, true, this.personService, this.nameParser);
		final Person inviterObj = extractPerson(inviter, true, this.personService, this.nameParser);
		if (person.getId() != guestObj.getId() && person.getId() != inviterObj.getId()) {
			throw new IllegalArgumentException("Person with name '" + person.getFullName() + "' is neither guest nor inviter"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		//
		invitation.setType(type);
		invitation.setGuest(guestObj);
		invitation.setInviter(inviterObj);
		invitation.setStartDate(startDate);
		invitation.setEndDate(endDate);
		invitation.setTitle(title);
		invitation.setUniversity(university);
		invitation.setCountry(country);
	}

	/** Delete the person invitation with the given identifier.
	 *
	 * @param invitationId the identifier of the person invitation to be deleted.
	 * @throws Exception in case of error.
	 */
	@Transactional
	public void removePersonInvitation(int invitationId) {
		final Integer iid = Integer.valueOf(invitationId);
		final Optional<PersonInvitation> optInv = this.invitationRepository.findById(iid);
		if (optInv.isEmpty()) {
			throw new IllegalStateException("Person invitation not found with id: " + invitationId); //$NON-NLS-1$
		}
		this.invitationRepository.deleteById(iid);
	}

	/** Save the given invitation into the database.
	 *
	 * @param invitation the invitation to save.
	 */
	public void save(PersonInvitation invitation) {
		this.invitationRepository.save(invitation);
	}

}
