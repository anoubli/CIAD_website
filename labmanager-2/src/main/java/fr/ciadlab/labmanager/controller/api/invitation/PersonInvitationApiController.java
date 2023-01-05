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

package fr.ciadlab.labmanager.controller.api.invitation;

import java.time.LocalDate;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.invitation.PersonInvitationType;
import fr.ciadlab.labmanager.service.invitation.PersonInvitationService;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import org.arakhne.afc.util.CountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for person invitation.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@RestController
@CrossOrigin
public class PersonInvitationApiController extends AbstractApiController {

	private PersonInvitationService invitationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param invitationService the service for managing the person invitations.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public PersonInvitationApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonInvitationService invitationService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.invitationService = invitationService;
	}

	/** Saving information of a person invitation. 
	 *
	 * @param person the identifier of the person for who the invitation must be saved. If the identifier is
	 *     not provided, this endpoint is supposed to create a membership in the database.
	 * @param invitation the identifier of the invitation. If the identifier is not provided, this endpoint
	 *     is supposed to create an invitation in the database.
	 * @param guest the name of the guest person.
	 * @param inviter the name of the inviter person.
	 * @param startDate the start date of the invitation.
	 * @param endDate the end date of the invitation.
	 * @param type the type of invitation.
	 * @param title the title of the evaluated works.
	 * @param university the name of the hosting university.
	 * @param country the country of the hosting university.
	 * @param username the name of the logged-in user.
	 * @throws Exception if it is impossible to save the invitation in the database.
	 */
	@PutMapping(value = "/" + Constants.PERSON_INVITATION_SAVING_ENDPOINT)
	public void saveJuryMembership(
			@RequestParam(required = true) int person,
			@RequestParam(required = false) Integer invitation,
			@RequestParam(required = false) String guest,
			@RequestParam(required = false) String inviter,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String university,
			@RequestParam(required = false) String country,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.PERSON_INVITATION_SAVING_ENDPOINT, Integer.valueOf(person));
		try {
			final String inStartDate = inString(startDate);
			final String inEndDate = inString(endDate);
			final String inType = inString(type);
			final String inTitle = inString(title);
			final String inGuest = inString(guest);
			final String inInviter = inString(inviter);
			final String inUniversity = inString(university);
			final String inCountry = inString(country);
			//
			if (inStartDate == null) {
				throw new RuntimeException("Start date is missed"); //$NON-NLS-1$
			}
			final LocalDate startDateObj = LocalDate.parse(inStartDate);
			if (inEndDate == null) {
				throw new RuntimeException("End date is missed"); //$NON-NLS-1$
			}
			final LocalDate endDateObj = LocalDate.parse(inEndDate);
			//
			if (inType == null) {
				throw new RuntimeException("Type of invitation is missed"); //$NON-NLS-1$
			}
			final PersonInvitationType typeObj = PersonInvitationType.valueOfCaseInsensitive(inType);
			//
			if (inCountry == null) {
				throw new RuntimeException("Country is missed"); //$NON-NLS-1$
			}
			final CountryCode countryObj = CountryCodeUtils.valueOfCaseInsensitive(inCountry);
			//
			if (inTitle == null) {
				throw new RuntimeException("Title is missed"); //$NON-NLS-1$
			}
			if (inUniversity == null) {
				throw new RuntimeException("University is missed"); //$NON-NLS-1$
			}
			//
			if (invitation == null || invitation.intValue() == 0) {
				// Create the membership
				this.invitationService.addPersonInvitation(
						person, inGuest, inInviter,
						startDateObj, endDateObj,
						typeObj, inTitle,
						inUniversity, countryObj);
			} else {
				// Update the membership				
				this.invitationService.updatePersonInvitation(
						invitation.intValue(),
						person, inGuest, inInviter,
						startDateObj, endDateObj,
						typeObj, inTitle,
						inUniversity, countryObj);
			}
		} catch (Throwable ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
	}

	/** Delete a person invitation from the database.
	 *
	 * @param id the identifier of the invitation.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.PERSON_INVITATION_DELETION_ENDPOINT)
	public void deleteJuryMembership(
			@RequestParam Integer id,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.PERSON_INVITATION_DELETION_ENDPOINT, id);
		if (id == null || id.intValue() == 0) {
			throw new IllegalStateException("Missing the person invitation id"); //$NON-NLS-1$
		}
		this.invitationService.removePersonInvitation(id.intValue());
	}

}
