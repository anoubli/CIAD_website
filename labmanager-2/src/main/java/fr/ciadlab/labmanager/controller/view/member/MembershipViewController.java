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

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.member.ChronoMembershipComparator;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.Responsibility;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationComparator;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.utils.bap.FrenchBap;
import fr.ciadlab.labmanager.utils.cnu.CnuSection;
import fr.ciadlab.labmanager.utils.conrs.ConrsSection;
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
import org.springframework.web.util.UriBuilder;

/** REST Controller for memberships views.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see AuthorController
 */
@RestController
@CrossOrigin
public class MembershipViewController extends AbstractViewController {

	private PersonNameParser nameParser;

	private PersonService personService;

	private ResearchOrganizationService organizationService;

	private ResearchOrganizationRepository organizationRepository;

	private ChronoMembershipComparator membershipComparator;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param nameParser the parser of person names.
	 * @param personService the service related to the persons.
	 * @param organizationService the service related to the research organizations.
	 * @param organizationRepository the repository of the research organizations.
	 * @param membershipComparator the comparator of memberships to use for building the views with
	 *     a chronological point of view.
	 * @param membershipService the service for managing the memberships.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public MembershipViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonNameParser nameParser,
			@Autowired PersonService personService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired ChronoMembershipComparator membershipComparator,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.nameParser = nameParser;
		this.personService = personService;
		this.organizationService = organizationService;
		this.organizationRepository = organizationRepository;
		this.membershipComparator = membershipComparator;
	}

	/** Show the view that permits to edit the memberships.
	 *
	 * @param personName the name of the person for who memberships must be edited. If this argument is not
	 *      provided, {@code personId} must be provided.
	 * @param personId the identifier of the person for who memberships must be edited. If this argument is not
	 *      provided, {@code personName} must be provided.
	 * @param gotoName the name of the anchor to go to in the view.
	 * @param username the name of the logged-in user.
	 * @param locale the locale to be used.
	 * @return the model-view that shows the duplicate persons.
	 */
	@GetMapping("/" + Constants.MEMBERSHIP_EDITING_ENDPOINT)
	public ModelAndView showMembershipEditor(
			@RequestParam(required = false) String personName,
			@RequestParam(required = false) Integer personId,
			@RequestParam(required = false, name = "goto") String gotoName,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username,
			Locale locale) {
		ensureCredentials(username, Constants.MEMBERSHIP_EDITING_ENDPOINT);
		//
		final String inPersonName = inString(personName);
		if (Strings.isNullOrEmpty(inPersonName) && personId == null) {
			throw new IllegalArgumentException("You must provide the name or the identifier of the person."); //$NON-NLS-1$
		}
		final Person person;
		if (personId == null) {
			final String firstName = this.nameParser.parseFirstName(inPersonName);
			final String lastName = this.nameParser.parseLastName(inPersonName);
			person = this.personService.getPersonBySimilarName(firstName, lastName);
		} else {
			person = this.personService.getPersonById(personId.intValue());
		}
		if (person == null) {
			throw new IllegalArgumentException("Person not found"); //$NON-NLS-1$
		}
		//
		final ModelAndView modelAndView = new ModelAndView(Constants.MEMBERSHIP_EDITING_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final List<Membership> memberships = person.getMemberships().stream().sorted(this.membershipComparator).collect(Collectors.toList());
		// Preferred values
		ResearchOrganization preferredOrganization = null;
		MemberStatus preferredStatus = null;
		Responsibility preferredResponsibility = null;
		CnuSection preferredCnuSection = null;
		ConrsSection preferredConrsSection = null;
		FrenchBap preferredFrenchBap = null;
		boolean foundActive = false;
		int maxId = 0;
		for (final Membership m : memberships) {
			if (m.getId() > maxId) {
				maxId = m.getId();
			}
			if (m.isActive() && !foundActive) {
				foundActive = true;
				preferredOrganization = m.getResearchOrganization();
				preferredStatus = m.getMemberStatus();
				preferredResponsibility = m.getResponsibility();
				preferredCnuSection = m.getCnuSection();
				preferredConrsSection = m.getConrsSection();
				preferredFrenchBap = m.getFrenchBap();
			}
			if (preferredOrganization == null) {
				preferredOrganization = m.getResearchOrganization();
			}
			if (preferredStatus == null) {
				preferredStatus = m.getMemberStatus();
			}
			if (preferredResponsibility == null) {
				preferredResponsibility = m.getResponsibility();
			}
			if (preferredCnuSection == null) {
				preferredCnuSection = m.getCnuSection();
			}
			if (preferredConrsSection == null) {
				preferredConrsSection = m.getConrsSection();
			}
			if (preferredFrenchBap == null) {
				preferredFrenchBap = m.getFrenchBap();
			}
		}
		modelAndView.addObject("preferredOrganization", preferredOrganization); //$NON-NLS-1$
		modelAndView.addObject("preferredStatus", preferredStatus); //$NON-NLS-1$
		modelAndView.addObject("preferredResponsibility", preferredResponsibility); //$NON-NLS-1$
		modelAndView.addObject("preferredCnuSection", preferredCnuSection); //$NON-NLS-1$
		modelAndView.addObject("preferredConrsSection", preferredConrsSection); //$NON-NLS-1$
		modelAndView.addObject("preferredFrenchBap", preferredFrenchBap); //$NON-NLS-1$
		modelAndView.addObject("preferredIsMainPosition", Boolean.TRUE); //$NON-NLS-1$
		modelAndView.addObject("minMembershipId", Integer.valueOf(maxId + 10)); //$NON-NLS-1$
		//
		final Map<String, Responsibility> allResponsabilities = new TreeMap<>();
		for (final Responsibility resp : Responsibility.values()) {
			allResponsabilities.put(resp.getLabel(person.getGender(), locale), resp);
		}
		modelAndView.addObject("allResponsabilities", allResponsabilities); //$NON-NLS-1$
		//
		final List<ResearchOrganization> sortedOrganizations = this.organizationRepository.findAll().stream()
				.sorted(new ResearchOrganizationComparator()).collect(Collectors.toList());
		//
		modelAndView.addObject("savingUrl", rooted(Constants.MEMBERSHIP_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("deletionUrl", rooted(Constants.MEMBERSHIP_DELETION_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("person", person); //$NON-NLS-1$
		modelAndView.addObject("sortedMemberships", memberships); //$NON-NLS-1$
		modelAndView.addObject("organizations", sortedOrganizations); //$NON-NLS-1$
		modelAndView.addObject("gotoName", inString(gotoName)); //$NON-NLS-1$
		return modelAndView;
	}

	/** Replies the list of members for the given organization.
	 * This function differs to {@link #showBackPersonList(Integer, String)} because it is dedicated to
	 * the public front-end of the research organization. The function {@link #showBackPersonList(Integer, String)}
	 * is more dedicated to the administration of the data-set.
	 *
	 * @param organization the identifier of the organization for which the publications must be exported.
	 * @param organizationAcronym the acronym of the organization for which the publications must be exported.
	 * @param includeSuborganizations indicates if the sub-organizations are included.
	 * @param enableFilters indicates if the "Filters" box should be visible.
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 * @param username the name of the logged-in user.
	 * @return the model-view of the list of publications.
	 * @see #showBackPersonList(Integer, String)
	 */
	@GetMapping("/showMembers")
	public ModelAndView showMembers(
			@RequestParam(required = false, name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@RequestParam(required = false) String organizationAcronym,
			@RequestParam(required = false, name = Constants.INCLUDESUBORGANIZATION_ENDPOINT_PARAMETER, defaultValue = "true") boolean includeSuborganizations,
			@RequestParam(required = false, defaultValue = "true") boolean enableFilters,
			@RequestParam(required = false, defaultValue = "false") boolean embedded,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, "showMembers"); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("showMembers"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, embedded);
		//
		final Integer organizationIdObj;
		if (organization != null && organization.intValue() != 0) {
			organizationIdObj = Integer.valueOf(organization.intValue());
		} else {
			final String inOrganizationAcronym = inString(organizationAcronym);
			if (!Strings.isNullOrEmpty(inOrganizationAcronym)) {
				final ResearchOrganization org = getOrganizationWith(inOrganizationAcronym, this.organizationService);
				if (org != null) {
					organizationIdObj = Integer.valueOf(org.getId());
				} else {
					organizationIdObj = null;
				}
			} else {
				organizationIdObj = null;
			}
		}
		//
		addUrlToMemberListEndPoint(modelAndView, organizationIdObj, includeSuborganizations);
		//
		modelAndView.addObject("enableFilters", Boolean.valueOf(enableFilters)); //$NON-NLS-1$
		return modelAndView;
	}

	/** Add the URL to model that permits to retrieve the member list.
	 *
	 * @param modelAndView the model-view to configure for redirection.
	 * @param organization the identifier of the organization for which the members must be exported.
	 * @param includeSuborganizations indicates if the sub-organizations are included.
	 */
	protected void addUrlToMemberListEndPoint(ModelAndView modelAndView, Integer organization, boolean includeSuborganizations) {
		final StringBuilder path = new StringBuilder();
		path.append("/").append(getApplicationConstants().getServerName()).append("/").append(Constants.EXPORT_MEMBERS_TO_JSON_ENDPOINT); //$NON-NLS-1$ //$NON-NLS-2$
		UriBuilder uriBuilder = this.uriBuilderFactory.builder();
		uriBuilder = uriBuilder.path(path.toString());
		uriBuilder = uriBuilder.queryParam(Constants.FORAJAX_ENDPOINT_PARAMETER, Boolean.TRUE);
		if (organization != null && organization.intValue() != 0) {
			uriBuilder = uriBuilder.queryParam(Constants.ORGANIZATION_ENDPOINT_PARAMETER, organization);
		}
		uriBuilder = uriBuilder.queryParam(Constants.INCLUDESUBORGANIZATION_ENDPOINT_PARAMETER, Boolean.valueOf(includeSuborganizations));
		final String url = uriBuilder.build().toString();
		modelAndView.addObject("url", url); //$NON-NLS-1$
	}

}
