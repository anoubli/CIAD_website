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

package fr.ciadlab.labmanager.controller.view.supervision;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.supervision.Supervision;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.supervision.SupervisionService;
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

/** REST Controller for person supervision views.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */
@RestController
@CrossOrigin
public class SupervisionViewController extends AbstractViewController {

	private MembershipService membershipService;

	private SupervisionService supervisionService;

	private PersonService personService;

	private PersonComparator personComparator;

	private PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param supervisionService the service for accessing the supervisions.
	 * @param membershipService the service for managing the memberships.
	 * @param personService the service for managing the persons.
	 * @param personComparator the comparator of persons.
	 * @param nameParser the parser of person names.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public SupervisionViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired SupervisionService supervisionService,
			@Autowired MembershipService membershipService,
			@Autowired PersonService personService,
			@Autowired PersonComparator personComparator,
			@Autowired PersonNameParser nameParser,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.supervisionService = supervisionService;
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
	@GetMapping("/" + Constants.SUPERVISION_EDITING_ENDPOINT)
	public ModelAndView supervisionEditor(
			@RequestParam(required = true) int person,
			@RequestParam(required = false, name = "goto") String gotoName,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, Constants.SUPERVISION_EDITING_ENDPOINT);
		final ModelAndView modelAndView = new ModelAndView(Constants.SUPERVISION_EDITING_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Person personObj = this.personService.getPersonById(person);
		if (personObj == null) {
			throw new RuntimeException("Person not found: " + person); //$NON-NLS-1$
		}
		modelAndView.addObject("person", personObj); //$NON-NLS-1$
		//
		final List<Supervision> supervisions = this.supervisionService.getSupervisionsForSupervisedPerson(person).stream()
				.sorted(EntityUtils.getPreferredSupervisionComparator()).collect(Collectors.toList());
		modelAndView.addObject("supervisions", supervisions); //$NON-NLS-1$
		//
		final List<Membership> memberships = this.membershipService.getMembershipsForPerson(person).stream()
				.filter(it -> it.getMemberStatus().isSupervisable())
				.sorted(EntityUtils.getPreferredMembershipComparator()).collect(Collectors.toList());
		modelAndView.addObject("memberships", memberships); //$NON-NLS-1$
		//
		modelAndView.addObject("allPersons", this.personService.getAllPersons().stream() //$NON-NLS-1$
				.filter(it -> it.getId() != personObj.getId() && !it.getSupervisorMemberships().isEmpty())
				.sorted(this.personComparator).collect(Collectors.toList()));
		//
		modelAndView.addObject("savingUrl", rooted(Constants.SUPERVISION_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("deletionUrl", rooted(Constants.SUPERVISION_DELETION_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("gotoName", inString(gotoName)); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the list of the supervisions for the given person.
	 *
	 * @param dbId the database identifier of the person who is supervisor. If it is not provided, the webId should be provided.
	 * @param webId the web-page identifier of the person who is supervisor. If it is not provided, the dbId should be provided.
	 * @param abandonment indicates if the abandoned positions are displayed. By default, they are hidden.
	 * @param phds indicates if the Postdocs and PhDs are included.
	 * @param masters indicates if the masters are included.
	 * @param others indicates if the other types of formation (excl. PhDs and Masters) are included.
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 * @param username the name of the logged-in user.
	 * @return the model-view.
	 */
	@GetMapping("/showSupervisions")
	public ModelAndView showSupervisions(
			@RequestParam(required = false, name = Constants.DBID_ENDPOINT_PARAMETER) Integer dbId,
			@RequestParam(required = false, name = Constants.WEBID_ENDPOINT_PARAMETER) String webId,
			@RequestParam(required = false, defaultValue = "false") boolean abandonment,
			@RequestParam(required = false, defaultValue = "true") boolean phds,
			@RequestParam(required = false, defaultValue = "true") boolean masters,
			@RequestParam(required = false, defaultValue = "true") boolean others,
			@RequestParam(required = false, defaultValue = "false") boolean embedded,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		final String inWebId = inString(webId);
		readCredentials(username, "showSupervisions", dbId, inWebId); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("showSupervisions"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, embedded);
		//
		final Person personObj = getPersonWith(dbId, inWebId, null, this.personService, this.nameParser);
		if (personObj == null) {
			throw new RuntimeException("Person not found"); //$NON-NLS-1$
		}
		final List<Supervision> supervisions = this.supervisionService.getSupervisionsForSupervisor(personObj.getId());
		final List<Supervision> sortedSupervisions = supervisions.stream()
				.filter(it -> {
					if (abandonment || !it.isAbandonment()) {
						final MemberStatus status = it.getSupervisedPerson().getMemberStatus();
						assert status.isSupervisable();
						switch (status) {
						case POSTDOC:
						case PHD_STUDENT:
							return phds;
						case MASTER_STUDENT:
							return masters;
						case OTHER_STUDENT:
							return others;
						default:
						}
					}
					return false;
				})
				.sorted(EntityUtils.getPreferredSupervisionComparator())
				.collect(Collectors.toList()); 
		modelAndView.addObject("person", personObj); //$NON-NLS-1$
		modelAndView.addObject("supervisions", sortedSupervisions); //$NON-NLS-1$
		modelAndView.addObject("countryLabels", CountryCodeUtils.getAllDisplayCountries()); //$NON-NLS-1$
		modelAndView.addObject("typeLabelKeyOrdering", Supervision.getAllLongTypeLabelKeys(personObj.getGender())); //$NON-NLS-1$
		modelAndView.addObject("preferredSupervisorComparator", EntityUtils.getPreferredSupervisorComparator()); //$NON-NLS-1$
		if (isLoggedIn()) {
			modelAndView.addObject("editionUrl", endpoint(Constants.SUPERVISION_EDITING_ENDPOINT, //$NON-NLS-1$
					Constants.PERSON_ENDPOINT_PARAMETER));
			modelAndView.addObject("personAdditionUrl", endpoint(Constants.PERSON_EDITING_ENDPOINT)); //$NON-NLS-1$
			final Optional<Membership> organizationMbr = personObj.getSupervisorMemberships().stream()
					.filter(it -> !it.isFuture() && it.isMainPosition()).min(EntityUtils.getPreferredMembershipComparator());
			if (organizationMbr.isPresent()) {
				final ResearchOrganization org = organizationMbr.get().getResearchOrganization();
				modelAndView.addObject("organization", org); //$NON-NLS-1$
			}
		}
		return modelAndView;
	}

}
