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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.member.ChronoMembershipComparator;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.Responsibility;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.utils.bap.FrenchBap;
import fr.ciadlab.labmanager.utils.cnu.CnuSection;
import fr.ciadlab.labmanager.utils.conrs.ConrsSection;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for memberships.
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
public class MembershipApiController extends AbstractApiController {

	private MembershipService membershipService;

	private ResearchOrganizationService organizationService;

	private ChronoMembershipComparator membershipComparator;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param membershipService the service for managing the memberships.
	 * @param organizationService the service for accessing the organizations.
	 * @param membershipComparator the comparator of member for determining the more recents.
	 */
	public MembershipApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired MembershipService membershipService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired ChronoMembershipComparator membershipComparator) {
		super(messages, constants);
		this.membershipService = membershipService;
		this.organizationService = organizationService;
		this.membershipComparator = membershipComparator;
	}

	/** Saving information of a membership. 
	 *
	 * @param person the identifier of the person. If the identifier is not provided, this endpoint is supposed to create
	 *     a person in the database.
	 * @param membership the identifier of the membership. If the identifier is not provided, this endpoint is supposed to create
	 *     a membership in the database.
	 * @param organization the identifier of the organization related to the membership.
	 * @param status the status of the person related to the membership. If {@code null} or empty, the status does not change in
	 *     the existing membership.
	 * @param responsibility the responsibility of the person during the period of the menmbership.
	 * @param memberSinceWhen the start date of the membership, or {@code null} to set it as unknown or "since always".
	 * @param memberToWhen the end date of the membership, or {@code null} to set it as unknown or "for the rest of the time".
	 * @param cnuSection the number of the CNU section to which this membership belongs to. If {@code null} or empty, the
	 *     CNU section does not change in the existing membership.
	 * @param conrsSection the number of the CoNRS section to which this membership belongs to. If {@code null} or empty, the
	 *     CoNRS section does not change in the existing membership.
	 * @param frenchBap the type of job for a not-researcher.
	 * @param isMainPosition indicates if the membership is marked as a main position.
	 * @param closeActive in the case that an active membership exists for the person and the organization, this flag
	 *     permits to control the behavior of the controller. If it is evaluated to {@code true}, the existing active
	 *     membership is closed to the date given by {@code memberSinceWhen} (if this date is not given, today is considered.
	 *     If argument {@code closeActive} is evaluated to {@code false}, an error is thrown. 
	 * @param username the name of the logged-in user.
	 * @throws Exception if it is impossible to save the membership in the database.
	 */
	@PostMapping(value = "/" + Constants.MEMBERSHIP_SAVING_ENDPOINT)
	public void saveMembership(
			@RequestParam(required = true) Integer person,
			@RequestParam(required = false) Integer membership,
			@RequestParam(required = false) Integer organization,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String responsibility,
			@RequestParam(required = false) String memberSinceWhen,
			@RequestParam(required = false) String memberToWhen,
			@RequestParam(required = false) Integer cnuSection,
			@RequestParam(required = false) Integer conrsSection,
			@RequestParam(required = false) String frenchBap,
			@RequestParam(required = false, defaultValue = "true") boolean isMainPosition,
			@RequestParam(required = false, defaultValue = "true") boolean closeActive,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /" + Constants.MEMBERSHIP_SAVING_ENDPOINT + " by " + username + " for person " + person); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ensureCredentials(username);
		try {
			// Parse values to Java objects
			LocalDate startDate = Strings.isNullOrEmpty(memberSinceWhen) ? null : LocalDate.parse(memberSinceWhen);
			final LocalDate endDate = Strings.isNullOrEmpty(memberToWhen) ? null : LocalDate.parse(memberToWhen);
			final MemberStatus statusObj = Strings.isNullOrEmpty(status) ? null : MemberStatus.valueOfCaseInsensitive(status);
			final Responsibility responsibilityObj = Strings.isNullOrEmpty(responsibility) ? null : Responsibility.valueOfCaseInsensitive(responsibility);
			final CnuSection cnuSectionObj = cnuSection == null || cnuSection.intValue() == 0 ? null : CnuSection.valueOf(cnuSection);
			final ConrsSection conrsSectionObj = conrsSection == null || conrsSection.intValue() == 0 ? null : ConrsSection.valueOf(conrsSection);
			final FrenchBap frenchBapObj = Strings.isNullOrEmpty(frenchBap) ? null : FrenchBap.valueOfCaseInsensitive(frenchBap);

			// Check validity of dates
			if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
				throw new IllegalArgumentException("The end date cannot be fore the start date"); //$NON-NLS-1$
			}

			if (membership == null || membership.intValue() == 0) {
				// Create the membership
				if (person == null || person.intValue() == 0) {
					throw new IllegalArgumentException("Person identifier is missed"); //$NON-NLS-1$
				}
				if (organization == null || organization.intValue() == 0) {
					throw new IllegalArgumentException("Organization identifier is missed"); //$NON-NLS-1$
				}
				if (statusObj == null) {
					throw new IllegalArgumentException("Member status is missed"); //$NON-NLS-1$
				}
				boolean continueCreation;
				do {
					continueCreation = false;
					final Pair<Membership, Boolean> result = this.membershipService.addMembership(
							organization.intValue(),
							person.intValue(),
							startDate, endDate, statusObj, responsibilityObj,
							cnuSectionObj, conrsSectionObj,
							frenchBapObj, isMainPosition, false);
					if (!result.getRight().booleanValue()) {
						// The membership already exists, start the dedicated behavior
						if (closeActive) {
							final Membership otherMembership = result.getLeft();
							if (startDate == null) {
								startDate = LocalDate.now();
							}
							final LocalDate pEnd = startDate.minus(1, ChronoUnit.DAYS);
							LocalDate pStart = otherMembership.getMemberSinceWhen();
							if (pStart != null && pEnd.isBefore(pStart)) {
								pStart = pEnd;
							}
							this.membershipService.updateMembershipById(
									otherMembership.getId(),
									Integer.valueOf(otherMembership.getResearchOrganization().getId()),
									pStart, pEnd,
									otherMembership.getMemberStatus(),
									otherMembership.getResponsibility(),
									otherMembership.getCnuSection(),
									otherMembership.getConrsSection(),
									otherMembership.getFrenchBap(),
									otherMembership.isMainPosition());
							continueCreation = true;
						} else {
							throw new IllegalStateException("A membership is already active. It is not allowed to create multiple active memberships for the same organization"); //$NON-NLS-1$
						}
					}
				} while (continueCreation);
			} else {
				// Update an existing membership.
				this.membershipService.updateMembershipById(
						membership.intValue(),
						organization, startDate, endDate, statusObj, responsibilityObj,
						cnuSectionObj, conrsSectionObj, frenchBapObj,
						isMainPosition);
			}
		} catch (Throwable ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
	}

	/** Delete a membership from the database.
	 *
	 * @param id the identifier of the membership.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.MEMBERSHIP_DELETION_ENDPOINT)
	public void deleteMembership(
			@RequestParam Integer id,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /" + Constants.MEMBERSHIP_DELETION_ENDPOINT + " by " + username + " for membership " + id); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ensureCredentials(username);
		if (id == null || id.intValue() == 0) {
			throw new IllegalStateException("Missing the membership id"); //$NON-NLS-1$
		}
		this.membershipService.removeMembership(id.intValue());
	}

	/** Replies the JSON representation of the members of the given organization.
	 *
	 * @param organization the identifier of the organization to export.
	 * @param otherOrganizationType the type of organization that must be considered as "other organization".
	 *     Default is {@link ResearchOrganizationType#UNIVERSITY}.
	 * @param includeSuborganizations indicates if the sub-organizations are included.
	 * @param forAjax indicates if the JSON is provided to AJAX. By default, the value is
	 *     {@code false}. If the JSON is provided to AJAX, the data is included into the root key {@code data} that is expected by AJAX.
	 *     If this parameter is evaluated to {@code true}, the parameter {@code inAttachment} is ignored.
	 * @param inAttachment indicates if the JSON is provided as attached document or not. By default, the value is
	 *     {@code false}.
	 *     If the parameter {@code forAjax} is evaluated to {@code true}, this parameter is ignored.
	 * @param username the name of the logged-in user.
	 * @return the JSON.
	 */
	@GetMapping(value = "/" + Constants.EXPORT_MEMBERS_TO_JSON_ENDPOINT)
	public ResponseEntity<Object> exportMembersToJson(
			@RequestParam(required = true) int organization,
			@RequestParam(required = false) String otherOrganizationType,
			@RequestParam(required = false, name = Constants.INCLUDESUBORGANIZATION_ENDPOINT_PARAMETER, defaultValue = "true") boolean includeSuborganizations,
			@RequestParam(required = false, defaultValue = "false", name = Constants.FORAJAX_ENDPOINT_PARAMETER) Boolean forAjax,
			@RequestParam(required = false, defaultValue = "false", name = Constants.INATTACHMENT_ENDPOINT_PARAMETER) Boolean inAttachment,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /" + Constants.EXPORT_MEMBERS_TO_JSON_ENDPOINT + " by " + username + " for organization " + organization); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		readCredentials(username);
		final boolean isAjax = forAjax != null && forAjax.booleanValue();
		final boolean isAttachment = !isAjax && inAttachment != null && inAttachment.booleanValue();
		final ResearchOrganizationType otherOrganizationTypeEnum =
				otherOrganizationType == null ? ResearchOrganizationType.UNIVERSITY
						: ResearchOrganizationType.valueOfCaseInsensitive(otherOrganizationType);
		//
		final Optional<ResearchOrganization> organizationOpt = this.organizationService.getResearchOrganizationById(organization);
		if (organizationOpt.isEmpty()) {
			throw new NoSuchElementException("Organization not found with id: " + organization); //$NON-NLS-1$
		}
		final ResearchOrganization rootOrganization = organizationOpt.get();
		//
		// List of memberships should be built specifically for the front ends
		final Map<Person, MutablePair<Membership, Set<ResearchOrganization>>> members = new TreeMap<>(EntityUtils.getPreferredPersonComparator());
		final LinkedList<ResearchOrganization> organizationStack = new LinkedList<>();
		organizationStack.push(rootOrganization);
		while (!organizationStack.isEmpty()) {
			final ResearchOrganization currentOrganization = organizationStack.pop();
			if (includeSuborganizations) {
				organizationStack.addAll(currentOrganization.getSubOrganizations());
			}
			for (final Membership membership : currentOrganization.getMemberships()) {
				if (!membership.isFuture()) {
					final Person person = membership.getPerson();
					final MutablePair<Membership, Set<ResearchOrganization>> pair = members.computeIfAbsent(person, it -> new MutablePair<>());
					final Membership previousMembership = pair.getKey();
					if (previousMembership == null) {
						pair.setLeft(membership);
					} else {
						final int cmp = this.membershipComparator.compare(membership, previousMembership);
						if (cmp < 0) {
							pair.setLeft(membership);
						}
					}
					if (otherOrganizationTypeEnum != null) {
						for (final Membership otherm : person.getMemberships()) {
							final ResearchOrganization otherro = otherm.getResearchOrganization();
							if (otherro.getId() != organization && otherOrganizationTypeEnum == otherro.getType()) {
								Set<ResearchOrganization> orgs = pair.getRight();
								if (orgs == null) {
									orgs = new TreeSet<>(EntityUtils.getPreferredResearchOrganizationComparator());
									pair.setRight(orgs);
								}
								orgs.add(otherro);
							}
						}
					}
				}
			}
		}
		//
		// Build the data structure to be serialized to JSON
		final List<Map<String, Object>> content = new ArrayList<>();
		for (final MutablePair<Membership, Set<ResearchOrganization>> entry : members.values()) {
			final Map<String, Object> data = buildMemberEntry(entry.getLeft());
			if (data != null) {
				Set<ResearchOrganization> oo = entry.getRight();
				if (oo == null) {
					oo = Collections.emptySet();
				}
				data.put("otherOrganizations", oo); //$NON-NLS-1$
				content.add(data);
			}
		}
		//
		final Object contentObj;
		if (isAjax) {
			contentObj = Collections.singletonMap("data", content); //$NON-NLS-1$
		} else {
			contentObj = content;
		}
		//
		BodyBuilder bb = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON);
		if (isAttachment) {
			bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_MEMBERS_ATTACHMENT_BASENAME + ".json\""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return bb.body(contentObj);
	}

	private static Map<String, Object> buildMemberEntry(Membership membership) {
		final GeneralMemberType type = GeneralMemberType.fromMembership(membership);
		if (type == null) {
			return null;
		}

		final Map<String, Object> entry = new HashMap<>();
		entry.put("person", membership.getPerson()); //$NON-NLS-1$
		entry.put("memberStatus", membership.getMemberStatus()); //$NON-NLS-1$
		entry.put("responsibility", membership.getResponsibility()); //$NON-NLS-1$
		entry.put("memberType", type); //$NON-NLS-1$
		return entry;
	}

}
