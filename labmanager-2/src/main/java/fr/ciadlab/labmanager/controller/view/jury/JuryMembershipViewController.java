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

package fr.ciadlab.labmanager.controller.view.jury;

import java.util.List;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.jury.JuryMembership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.service.jury.JuryMembershipService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
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

/** REST Controller for jury membership views.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@RestController
@CrossOrigin
public class JuryMembershipViewController extends AbstractViewController {

	private PersonService personService;

	private PersonComparator personComparator;

	private PersonNameParser nameParser;

	private JuryMembershipService membershipService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param membershipService the service for managing the jury memberships.
	 * @param personService the service for managing the persons.
	 * @param personComparator the comparator of persons.
	 * @param nameParser the parser of person names.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public JuryMembershipViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JuryMembershipService membershipService,
			@Autowired PersonService personService,
			@Autowired PersonComparator personComparator,
			@Autowired PersonNameParser nameParser,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.membershipService = membershipService;
		this.personService = personService;
		this.personComparator = personComparator;
		this.nameParser = nameParser;
	}

	/** Replies the model-view component for showing the persons independently of the organization memberships.
	 *
	 * @param person the identifier of the person for who the jury memberships must be edited.
	 * @param gotoName the name of the anchor to go to in the view.
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/" + Constants.JURY_MEMBERSHIP_EDITING_ENDPOINT)
	public ModelAndView juryMembershipEditor(
			@RequestParam(required = true) int person,
			@RequestParam(required = false, name = "goto") String gotoName,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, Constants.JURY_MEMBERSHIP_EDITING_ENDPOINT);
		final ModelAndView modelAndView = new ModelAndView(Constants.JURY_MEMBERSHIP_EDITING_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Person personObj = this.personService.getPersonById(person);
		if (personObj == null) {
			throw new RuntimeException("Person not found: " + person); //$NON-NLS-1$
		}
		modelAndView.addObject("person", personObj); //$NON-NLS-1$
		modelAndView.addObject("allPersons", this.personService.getAllPersons().stream().sorted(this.personComparator).iterator()); //$NON-NLS-1$
		//
		final List<JuryMembership> memberships = this.membershipService.getMembershipsForPerson(person);
		final List<JuryMembership> sortedMemberships = memberships.stream().sorted(EntityUtils.getPreferredJuryMembershipComparator()).collect(Collectors.toList());
		modelAndView.addObject("sortedMemberships", sortedMemberships); //$NON-NLS-1$
		//
		modelAndView.addObject("countryLabels", CountryCodeUtils.getAllDisplayCountries()); //$NON-NLS-1$
		modelAndView.addObject("defaultCountry", CountryCodeUtils.DEFAULT); //$NON-NLS-1$
		//
		modelAndView.addObject("savingUrl", rooted(Constants.JURY_MEMBERSHIP_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("deletionUrl", rooted(Constants.JURY_MEMBERSHIP_DELETION_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("gotoName", inString(gotoName)); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the list of the jury memberships for the given person.
	 *
	 * @param dbId the database identifier of the person. If it is not provided, the webId should be provided.
	 * @param webId the web-page identifier of the person. If it is not provided, the dbId should be provided.
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 * @param username the name of the logged-in user.
	 * @return the model-view.
	 */
	@GetMapping("/showJuryMemberships")
	public ModelAndView showJuryMemberships(
			@RequestParam(required = false, name = Constants.DBID_ENDPOINT_PARAMETER) Integer dbId,
			@RequestParam(required = false, name = Constants.WEBID_ENDPOINT_PARAMETER) String webId,
			@RequestParam(required = false, defaultValue = "false") boolean embedded,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		final String inWebId = inString(webId);
		readCredentials(username, "showJuryMemberships", dbId, inWebId); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("showJuryMemberships"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, embedded);
		//
		final Person personObj = getPersonWith(dbId, inWebId, null, this.personService, this.nameParser);
		if (personObj == null) {
			throw new RuntimeException("Person not found"); //$NON-NLS-1$
		}
		final List<JuryMembership> memberships = this.membershipService.getMembershipsForPerson(personObj.getId());
		final List<JuryMembership> sortedMemberships = memberships.stream().sorted(EntityUtils.getPreferredJuryMembershipComparator()).collect(Collectors.toList()); 
		modelAndView.addObject("person", personObj); //$NON-NLS-1$
		modelAndView.addObject("memberships", sortedMemberships); //$NON-NLS-1$
		modelAndView.addObject("countryLabels", CountryCodeUtils.getAllDisplayCountries()); //$NON-NLS-1$
		modelAndView.addObject("typeLabelKeyOrdering", JuryMembership.getAllLongTypeLabelKeys(personObj.getGender())); //$NON-NLS-1$
		if (isLoggedIn()) {
			modelAndView.addObject("editionUrl", endpoint(Constants.JURY_MEMBERSHIP_EDITING_ENDPOINT, //$NON-NLS-1$
					Constants.PERSON_ENDPOINT_PARAMETER, Integer.valueOf(personObj.getId())));
		}
		return modelAndView;
	}

}
