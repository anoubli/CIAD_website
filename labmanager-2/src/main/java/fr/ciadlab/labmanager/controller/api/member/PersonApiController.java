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

package fr.ciadlab.labmanager.controller.api.member;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.controller.api.publication.PublicationApiController;
import fr.ciadlab.labmanager.entities.member.Gender;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.WebPageNaming;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.utils.names.PersonNameComparator;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import fr.ciadlab.labmanager.utils.vcard.VcardBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for persons.
 * <p>
 * This controller does not manage the memberships' or authorships' databases themselves.
 * You must use {@link MembershipApiController} for managing the memberships, and
 * {@link PublicationApiController} for authorships.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see MembershipApiController
 * @see PublicationApiController
 */
@RestController
@CrossOrigin
public class PersonApiController extends AbstractApiController {

	private PersonService personService;

	private PersonNameParser nameParser;

	private ResearchOrganizationService organizationService;

	private VcardBuilder vcardBuilder;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param personService the person service.
	 * @param organizationService the organization service.
	 * @param nameParser the parser of person names.
	 * @param vcardBuilder the builder of Vcards.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public PersonApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonService personService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired PersonNameParser nameParser,
			@Autowired VcardBuilder vcardBuilder,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.personService = personService;
		this.organizationService = organizationService;
		this.nameParser = nameParser;
		this.vcardBuilder = vcardBuilder;
	}

	/** Replies the information about a person as a JSON stream.
	 * This endpoint accepts one of the two parameters: the name or the identifier of the person.
	 * The name test is based on
	 * {@link PersonNameComparator#isSimilar(String, String) name similarity}, and not on strict equality.
	 *
	 * @param dbId the database identifier of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param webId the identifier of the webpage of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param strictName indicates if the name test must be strict (equality test) or not (similarity test).
	 *     By default, this parameter has the value {@code false}.
	 * @param username the name of the logged-in user.
	 * @return the person, or {@code null} if the person with the given name was not found.
	 * @see PersonNameComparator
	 */
	@GetMapping(value = "/getPersonData", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Person getPersonData(
			@RequestParam(required = false) Integer dbId,
			@RequestParam(required = false) String webId,
			@RequestParam(defaultValue = "false", required = false) boolean strictName,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		final String inWebId = inString(webId);
		ensureCredentials(username, "getPersonData", dbId, inWebId); //$NON-NLS-1$
		final Person person = getPersonWith(dbId, inWebId, null, this.personService, this.nameParser);
		if (person == null) {
			throw new IllegalArgumentException("Person not found"); //$NON-NLS-1$
		}
		return person;
	}

	/** Saving information of a person. 
	 *
	 * @param person the identifier of the person. If the identifier is not provided, this endpoint is supposed to create
	 *     a person in the database.
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @param gender the gender.
	 * @param email the email of the person.
	 * @param officePhone the phone number at office.
	 * @param mobilePhone the mobile phone number.
	 * @param officeRoom the number of the office room.
	 * @param gravatarId the identifier for obtaining a photo on Gravatar.
	 * @param orcid the ORCID of the person.
	 * @param researcherId the identifier of the person on ResearchId/WOS/Publon.
	 * @param googleScholarId the identifier of the person on Google Scholar.
	 * @param linkedInId the identifier of the person on LinkedIn.
	 * @param githubId the identifier of the person on Github.
	 * @param researchGateId the identifier of the person on ResearchGate.
	 * @param facebookId the identifier of the person on Facebook.
	 * @param dblpURL the URL of the person's page on DBLP.
	 * @param academiaURL the URL of the person's page on Academia.edu.
	 * @param cordisURL the URL of the person's page on European Commission's Cordis.
	 * @param webPageNaming the type of naming for the person's webpage on the organization server.
	 * @param googleScholarHindex the Hindex of the person on Google Scholar.
	 * @param wosHindex the Hindex of the person on WOS.
	 * @param username the name of the logged-in user.
	 * @throws Exception if the person cannot be saved.
	 */
	@PutMapping(value = "/" + Constants.PERSON_SAVING_ENDPOINT)
	public void personSave(
			@RequestParam(required = false) Integer person,
			@RequestParam(required = true) String firstName,
			@RequestParam(required = true) String lastName,
			@RequestParam(required = false) String gender,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String officePhone,
			@RequestParam(required = false) String mobilePhone,
			@RequestParam(required = false) String officeRoom,
			@RequestParam(required = false) String gravatarId,
			@RequestParam(required = false) String orcid,
			@RequestParam(required = false) String researcherId,
			@RequestParam(required = false) String googleScholarId,
			@RequestParam(required = false) String linkedInId,
			@RequestParam(required = false) String githubId,
			@RequestParam(required = false) String researchGateId,
			@RequestParam(required = false) String facebookId,
			@RequestParam(required = false) String dblpURL,
			@RequestParam(required = false) String academiaURL,
			@RequestParam(required = false) String cordisURL,
			@RequestParam(required = false) String webPageNaming,
			@RequestParam(required = false) Integer googleScholarHindex,
			@RequestParam(required = false) Integer wosHindex,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.PERSON_SAVING_ENDPOINT, person);
		final String inFirstName = inString(firstName);
		final String inLastName = inString(lastName);
		final String inGender = inString(gender);
		final String inEmail = inString(email);
		final String inOfficePhone = inString(officePhone);
		final String inMobilePhone = inString(mobilePhone);
		final String inOfficeRoom = inString(officeRoom);
		final String inGravatarId = inString(gravatarId);
		final String inOrcid = inString(orcid);
		final String inResearcherId = inString(researcherId);
		final String inGoogleScholarId = inString(googleScholarId);
		final String inLinkedInId = inString(linkedInId);
		final String inGithubId = inString(githubId);
		final String inResearchGateId = inString(researchGateId);
		final String inFacebookId = inString(facebookId);
		final String inDblpURL = inString(dblpURL);
		final String inAcademiaURL = inString(academiaURL);
		final String inCordisURL = inString(cordisURL);
		final String inWebPageNaming = inString(webPageNaming);
		//
		final Gender genderObj = inGender == null ? Gender.NOT_SPECIFIED : Gender.valueOfCaseInsensitive(inGender);
		final WebPageNaming webPageNamingObj = inWebPageNaming == null ? WebPageNaming.UNSPECIFIED : WebPageNaming.valueOfCaseInsensitive(inWebPageNaming);
		final int shindex = googleScholarHindex == null ? 0 : googleScholarHindex.intValue();
		final int whindex = wosHindex == null ? 0 : wosHindex.intValue();
		//
		final Person optPerson;
		//
		if (person == null) {
			optPerson = this.personService.createPerson(
					inFirstName, inLastName, genderObj, inEmail, inOfficePhone, inMobilePhone, inOfficeRoom,
					inGravatarId, inOrcid, inResearcherId, inGoogleScholarId, inLinkedInId, inGithubId, inResearchGateId,
					inFacebookId, inDblpURL, inAcademiaURL, inCordisURL, webPageNamingObj,
					shindex, whindex);
		} else {
			optPerson = this.personService.updatePerson(person.intValue(),
					inFirstName, inLastName, genderObj, inEmail, inOfficePhone, inMobilePhone, inOfficeRoom,
					inGravatarId, inOrcid, inResearcherId, inGoogleScholarId, inLinkedInId, inGithubId, inResearchGateId,
					inFacebookId, inDblpURL, inAcademiaURL, inCordisURL, webPageNamingObj,
					shindex, whindex);
		}
		if (optPerson == null) {
			throw new IllegalStateException("Person not found"); //$NON-NLS-1$
		}
	}

	/** Delete a person from the database.
	 *
	 * @param person the identifier of the person.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/deletePerson")
	public void deletePerson(
			@RequestParam Integer person,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, "deletePerson", person); //$NON-NLS-1$
		if (person == null || person.intValue() == 0) {
			throw new IllegalStateException("Person not found"); //$NON-NLS-1$
		}
		this.personService.removePerson(person.intValue());
	}

	/** Replies the person's virtual card. This card is a description of the person that could 
	 * be used for building a Internet page for the person.
	 *
	 * @param dbId the database identifier of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param webId the identifier of the webpage of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param organization the identifier or the name of the organization to restrict the card to.
	 * @param inAttachment indicates if the JSON is provided as attached document or not. By default, the value is
	 *     {@code false}.
	 * @return the Vcard.
	 */
	@GetMapping(value = "/" + Constants.PERSON_VCARD_ENDPOINT)
	public ResponseEntity<String> personVcard(
			@RequestParam(required = false) Integer dbId,
			@RequestParam(required = false) String webId,
			@RequestParam(required = false) String organization,
			@RequestParam(required = false, defaultValue = "false", name = Constants.INATTACHMENT_ENDPOINT_PARAMETER) Boolean inAttachment) {
		final Person personObj = getPersonWith(dbId, inString(webId), null, this.personService, this.nameParser);
		if (personObj == null) {
			throw new IllegalArgumentException("Person not found"); //$NON-NLS-1$
		}
		//
		final ResearchOrganization organizationObj = getOrganizationWith(inString(organization), this.organizationService);
		//
		final String content = this.vcardBuilder.build(personObj, organizationObj);
		//
		BodyBuilder bb = ResponseEntity.ok().contentType(VcardBuilder.VCARD_MIME_TYPE);
		if (inAttachment != null && inAttachment.booleanValue()) {
			bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + personObj.getFullName() + ".vcf\""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return bb.body(content);
	}

}



