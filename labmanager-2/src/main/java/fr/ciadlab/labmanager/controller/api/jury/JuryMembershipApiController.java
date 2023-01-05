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

package fr.ciadlab.labmanager.controller.api.jury;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.jury.JuryMembershipType;
import fr.ciadlab.labmanager.entities.jury.JuryType;
import fr.ciadlab.labmanager.service.jury.JuryMembershipService;
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

/** REST Controller for jury memberships.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RestController
@CrossOrigin
public class JuryMembershipApiController extends AbstractApiController {

	private JuryMembershipService membershipService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param membershipService the service for managing the memberships.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public JuryMembershipApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JuryMembershipService membershipService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.membershipService = membershipService;
	}

	/** Saving information of a jury membership. 
	 *
	 * @param person the identifier of the person.
	 * @param membership the identifier of the membership. If the identifier is not provided, this endpoint is supposed to create
	 *     a membership in the database.
	 * @param date the date of the defense.
	 * @param type the type of membership in the jury.
	 * @param defenseType the type of the defense.
	 * @param title the title of the evaluated works.
	 * @param candidate the name of the candidate. It is a database identifier (for known person) or full name
	 *     (for unknown person).
	 * @param university the name of the hosting university.
	 * @param country the country of the hosting university.
	 * @param promoters the list of promoters/directors. It is a list of database identifiers (for known persons) or full names
	 *     (for unknown persons).
	 * @param username the name of the logged-in user.
	 * @throws Exception if it is impossible to save the membership in the database.
	 */
	@PutMapping(value = "/" + Constants.JURY_MEMBERSHIP_SAVING_ENDPOINT)
	public void saveJuryMembership(
			@RequestParam(required = true) int person,
			@RequestParam(required = false) Integer membership,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String defenseType,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String candidate,
			@RequestParam(required = false) String university,
			@RequestParam(required = false) String country,
			@RequestParam(required = false) List<String> promoters,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.JURY_MEMBERSHIP_SAVING_ENDPOINT, Integer.valueOf(person));
		try {
			final String inDate = inString(date);
			final String inType = inString(type);
			final String inDefenseType = inString(defenseType);
			final String inTitle = inString(title);
			final String inCandidate = inString(candidate);
			final String inUniversity = inString(university);
			final String inCountry = inString(country);
			final List<String> inPromoters = promoters.stream().map(it -> inString(it)).filter(it -> it != null).collect(Collectors.toList());
			//
			if (inDate == null) {
				throw new RuntimeException("Date is missed"); //$NON-NLS-1$
			}
			final LocalDate dateObj = LocalDate.parse(inDate);
			if (inType == null) {
				throw new RuntimeException("Type of membership is missed"); //$NON-NLS-1$
			}
			final JuryMembershipType typeObj = JuryMembershipType.valueOfCaseInsensitive(inType);
			if (inDefenseType == null) {
				throw new RuntimeException("Type of defense is missed"); //$NON-NLS-1$
			}
			final JuryType defenseTypeObj = JuryType.valueOfCaseInsensitive(inDefenseType);
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
			if (membership == null || membership.intValue() == 0) {
				// Create the membership
				this.membershipService.addJuryMembership(
						person,
						dateObj, typeObj, defenseTypeObj, inTitle, inCandidate,
						inUniversity, countryObj, inPromoters);
			} else {
				// Update the membership				
				this.membershipService.updateJuryMembership(
						membership.intValue(),
						person,
						dateObj, typeObj, defenseTypeObj, inTitle, inCandidate,
						inUniversity, countryObj, inPromoters);
			}
		} catch (Throwable ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
	}


	/** Delete a jury membership from the database.
	 *
	 * @param id the identifier of the membership.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.JURY_MEMBERSHIP_DELETION_ENDPOINT)
	public void deleteJuryMembership(
			@RequestParam Integer id,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.JURY_MEMBERSHIP_DELETION_ENDPOINT, id);
		if (id == null || id.intValue() == 0) {
			throw new IllegalStateException("Missing the jury membership id"); //$NON-NLS-1$
		}
		this.membershipService.removeJuryMembership(id.intValue());
	}

}
