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

package fr.ciadlab.labmanager.controller.view.member;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.member.MembershipApiController;
import fr.ciadlab.labmanager.controller.api.publication.PublicationApiController;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.member.Gender;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.WebPageNaming;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;

/** REST Controller for persons views.
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
public class PersonViewController extends AbstractViewController {

	private PersonService personService;

	private MembershipService membershipService;

	private PersonNameParser nameParser;

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param personService the person service.
	 * @param membershipService the service for managing the memberships.
	 * @param organizationService the service for organizations.
	 * @param nameParser the parser for person names.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public PersonViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonService personService,
			@Autowired MembershipService membershipService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired PersonNameParser nameParser,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.personService = personService;
		this.membershipService = membershipService;
		this.organizationService = organizationService;
		this.nameParser = nameParser;
	}

	/** Replies the model-view component for showing the persons independently of the organization memberships.
	 *
	 * @param organization identifier of the organization for which the members must be displayed. If it is ot provided,
	 *      all the persons are considered, independently of the memberships. If this parameter is provided but it is equal
	 *      to {@code 0}, all the persons outside an organization will be considered.
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/" + Constants.PERSON_LIST_ENDPOINT)
	public ModelAndView showBackPersonList(
			@RequestParam(required = false) Integer organization,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, Constants.PERSON_LIST_ENDPOINT, organization);
		final ModelAndView modelAndView = new ModelAndView(Constants.PERSON_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		initAdminTableButtons(modelAndView, endpoint(Constants.PERSON_EDITING_ENDPOINT, "person")); //$NON-NLS-1$
		Collection<Person> persons = null;
		if (organization != null) {
			if (organization.intValue() == 0) {
				persons = this.personService.getAllPersons().stream().filter(it -> it.getMemberships().isEmpty()).collect(Collectors.toList());
			} else {
				persons = this.membershipService.getMembersOf(organization.intValue());
			}
		}
		if (persons == null) {
			persons = this.personService.getAllPersons();
		}
		modelAndView.addObject("persons", persons); //$NON-NLS-1$
		modelAndView.addObject("membershipdeletionUrl", rooted(Constants.MEMBERSHIP_DELETION_ENDPOINT)); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the editor for a person. This editor permits to create or to edit a person.
	 *
	 * @param person the identifier of the person to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of a person.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/" + Constants.PERSON_EDITING_ENDPOINT)
	public ModelAndView showPersonEditor(
			@RequestParam(required = false) Integer person,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, Constants.PERSON_EDITING_ENDPOINT, person);
		final ModelAndView modelAndView = new ModelAndView(Constants.PERSON_EDITING_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Person personObj;
		if (person != null && person.intValue() != 0) {
			personObj = this.personService.getPersonById(person.intValue());
			if (personObj == null) {
				throw new IllegalArgumentException("Person not found: " + person); //$NON-NLS-1$
			}
		} else {
			personObj = null;
		}
		//
		modelAndView.addObject("person", personObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.PERSON_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.PERSON_LIST_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("gravatarLink", Person.GRAVATAR_URL + "{0}?" + Person.GRAVATAR_SIZE_PARAM + "=50"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		modelAndView.addObject("googleScholarLink", Person.GOOGLE_SCHOLAR_AVATAR_URL + "{0}&" + Person.GOOGLE_SCHOLAR_AVATAR_SIZE_PARAM + "=50"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		modelAndView.addObject("githubLink", Person.GITHUB_AVATAR_URL + "{0}?" + Person.GITHUB_AVATAR_SIZE_PARAM + "=50"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		modelAndView.addObject("defaultGender", Gender.NOT_SPECIFIED); //$NON-NLS-1$
		modelAndView.addObject("defaultNaming", WebPageNaming.UNSPECIFIED); //$NON-NLS-1$
		//
		return modelAndView;
	}

	/** Show the person's virtual card. This card is a description of the person that could 
	 * be used for building a Internet page for the person.
	 *
	 * @param dbId the database identifier of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param webId the identifier of the webpage of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param organization the identifier or the name of the organization to restrict the card to.
	 * @param introText a small text that is output as a status/position.
	 * @param photo indicates if the photo should be shown on the card.
	 * @param qrcode indicates if the QR-code should be shown on the card.
	 * @param status indicates if the member status should be shown on the card.
	 * @param admin indicates if the administrative position should be shown on the card.
	 * @param email indicates if the email should be shown on the card.
	 * @param officePhone indicates if the office phone should be shown on the card.
	 * @param mobilePhone indicates if the mobile phone should be shown on the card.
	 * @param officeRoom indicates if the number of the office room should be shown on the card.
	 * @param postalAddress indicates if the postal address should be shown on the card.
	 * @param qindexes indicates if the Q-indexes should be shown on the card.
	 * @param links indicates if the links to external sites should be shown on the card.
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/showPersonCard")
	public ModelAndView showPersonCard(
			@RequestParam(required = false) Integer dbId,
			@RequestParam(required = false) String webId,
			@RequestParam(required = false) String organization,
			@RequestParam(required = false) String introText,
			@RequestParam(required = false, defaultValue="true") boolean photo,
			@RequestParam(required = false, defaultValue="true") boolean qrcode,
			@RequestParam(required = false, defaultValue="true") boolean status,
			@RequestParam(required = false, defaultValue="true") boolean admin,
			@RequestParam(required = false, defaultValue="true") boolean email,
			@RequestParam(required = false, defaultValue="true") boolean officePhone,
			@RequestParam(required = false, defaultValue="true") boolean mobilePhone,
			@RequestParam(required = false, defaultValue="true") boolean officeRoom,
			@RequestParam(required = false, defaultValue="true") boolean postalAddress,
			@RequestParam(required = false, defaultValue="true") boolean qindexes,
			@RequestParam(required = false, defaultValue="true") boolean links,
			@RequestParam(required = false, defaultValue="false") boolean embedded,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, "showPersonCard", dbId, webId, organization); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("showPersonCard"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, embedded);
		//
		final Person personObj = getPersonWith(dbId, inString(webId), null, this.personService, this.nameParser);
		if (personObj == null) {
			throw new IllegalArgumentException("Person not found"); //$NON-NLS-1$
		}
		//
		final ResearchOrganization organizationObj = getOrganizationWith(inString(organization), this.organizationService);
		//
		Stream<Membership> stream = personObj.getMemberships().stream().parallel();
		if (organizationObj == null) {
			stream = stream.filter(it -> !it.isFuture());
		} else {
			stream = stream.filter(it -> !it.isFuture() && it.getResearchOrganization().getId() == organizationObj.getId());
		}
		// Sort the memberships to push the active memberships before the former memberships.
		final Collection<Membership> activeMemberships = new ConcurrentLinkedQueue<>();
		final Collection<Membership> activeResponsibilities = new ConcurrentLinkedQueue<>();
		final AtomicReference<OrganizationAddress> activePostalAddressObj = new AtomicReference<>();
		final Collection<Membership> formerMemberships = new ConcurrentLinkedQueue<>();
		final Collection<Membership> formerResponsibilities = new ConcurrentLinkedQueue<>();
		final AtomicReference<OrganizationAddress> formerPostalAddressObj = new AtomicReference<>();
		final AtomicBoolean foundActive = new AtomicBoolean();
		stream.forEach(it -> {
			if (it.isActive()) {
				foundActive.set(true);
				if (it.getResponsibility() != null) {
					activeResponsibilities.add(it);
				}
				if (it.isMainPosition()) {
					if (activePostalAddressObj.get() == null && it.getOrganizationAddress() != null) {
						activePostalAddressObj.set(it.getOrganizationAddress());
					}
					activeMemberships.add(it);
				}
			} else {
				if (it.getResponsibility() != null) {
					formerResponsibilities.add(it);
				}
				if (it.isMainPosition()) {
					if (formerPostalAddressObj.get() == null && it.getOrganizationAddress() != null) {
						formerPostalAddressObj.set(it.getOrganizationAddress());
					}
					formerMemberships.add(it);
				}
			}
		});
		//
		final Collection<Membership> memberships;
		final Collection<Membership> responsibilities;
		final OrganizationAddress postalAddressObj;
		if (foundActive.get()) {
			modelAndView.addObject("isFormerMember", Boolean.FALSE); //$NON-NLS-1$
			memberships = activeMemberships;
			responsibilities = activeResponsibilities;
			postalAddressObj = activePostalAddressObj.get();
		} else {
			modelAndView.addObject("isFormerMember", Boolean.TRUE); //$NON-NLS-1$
			memberships = formerMemberships;
			responsibilities = formerResponsibilities;
			postalAddressObj = formerPostalAddressObj.get();
		}
		//
		modelAndView.addObject("person", personObj); //$NON-NLS-1$
		final Map<String, Object> obfuscatedValues = new HashMap<>();
		addObfuscatedEmailFields(obfuscatedValues, personObj.getEmail(), null);
		addObfuscatedPhoneFields(obfuscatedValues, personObj.getOfficePhone(), "o"); //$NON-NLS-1$
		addObfuscatedPhoneFields(obfuscatedValues, personObj.getMobilePhone(), "m"); //$NON-NLS-1$
		addObfuscatedValues(modelAndView, obfuscatedValues);
		modelAndView.addObject("introText", inString(introText)); //$NON-NLS-1$
		modelAndView.addObject("memberships", memberships); //$NON-NLS-1$
		if (postalAddressObj != null) {
			modelAndView.addObject("postalAddress", postalAddressObj); //$NON-NLS-1$
		}
		modelAndView.addObject("responsibilities", responsibilities); //$NON-NLS-1$
		if (qrcode) {
			final UriComponents currentUri =  ServletUriComponentsBuilder.fromCurrentContextPath().build();
			final UriBuilder uriBuilder = endpointUriBuilder(Constants.PERSON_VCARD_ENDPOINT);
			uriBuilder.scheme(currentUri.getScheme());
			uriBuilder.host(currentUri.getHost());
			uriBuilder.port(currentUri.getPort());
			uriBuilder.queryParam(Constants.DBID_ENDPOINT_PARAMETER, Integer.toString(personObj.getId()));
			uriBuilder.queryParam(Constants.INATTACHMENT_ENDPOINT_PARAMETER, Boolean.TRUE.toString());
			if (organizationObj != null) {
				uriBuilder.queryParam(Constants.ORGANIZATION_ENDPOINT_PARAMETER, Integer.toString(organizationObj.getId()));
			}
			modelAndView.addObject("vcardURL", uriBuilder.build().toASCIIString()); //$NON-NLS-1$
		}
		modelAndView.addObject("enablePhoto", Boolean.valueOf(photo)); //$NON-NLS-1$
		modelAndView.addObject("enableQrcode", Boolean.valueOf(qrcode)); //$NON-NLS-1$
		modelAndView.addObject("enableStatus", Boolean.valueOf(status)); //$NON-NLS-1$
		modelAndView.addObject("enableAdminPosition", Boolean.valueOf(admin)); //$NON-NLS-1$
		modelAndView.addObject("enableEmail", Boolean.valueOf(email)); //$NON-NLS-1$
		modelAndView.addObject("enableOfficePhone", Boolean.valueOf(officePhone)); //$NON-NLS-1$
		modelAndView.addObject("enableMobilePhone", Boolean.valueOf(mobilePhone)); //$NON-NLS-1$
		modelAndView.addObject("enableOfficeRoom", Boolean.valueOf(officeRoom)); //$NON-NLS-1$
		modelAndView.addObject("enablePostalAddress", Boolean.valueOf(postalAddress)); //$NON-NLS-1$
		modelAndView.addObject("enableQindexes", Boolean.valueOf(qindexes)); //$NON-NLS-1$
		modelAndView.addObject("enableLinks", Boolean.valueOf(links)); //$NON-NLS-1$
		//
		if (isLoggedIn()) {
			modelAndView.addObject("personEditionUrl", endpoint(Constants.PERSON_EDITING_ENDPOINT, //$NON-NLS-1$
					Constants.PERSON_ENDPOINT_PARAMETER, Integer.valueOf(personObj.getId())));
			modelAndView.addObject("membershipEditionUrl", endpoint(Constants.MEMBERSHIP_EDITING_ENDPOINT, //$NON-NLS-1$
					Constants.PERSONID_ENDPOINT_PARAMETER, Integer.valueOf(personObj.getId())));
		}
		return modelAndView;
	}

}
