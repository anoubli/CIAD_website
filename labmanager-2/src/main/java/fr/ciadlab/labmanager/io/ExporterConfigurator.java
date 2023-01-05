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

package fr.ciadlab.labmanager.io;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.service.journal.JournalService;
import org.springframework.web.util.UriBuilder;

/** Configurator for an exporter.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public class ExporterConfigurator {

	private Predicate<Person> personSelector;

	private Predicate<ResearchOrganization> organizationSelector;

	private final Map<String, Object> queryParameters = new HashMap<>();

	private boolean enableSelectedPersonFormat = true;

	private boolean enableResearcherFormat = true;

	private boolean enablePhDStudentFormat = true;

	private boolean enablePostdocEngineerFormat = true;

	private boolean enableTitleColor = true;

	private boolean enableDownloadButtons = true;

	private boolean enableExportButtons = true;

	private boolean enableEditButtons = true;

	private boolean enableDeleteButtons = true;

	private boolean enableFormattedLinks = true;

	private boolean enableFormattedAuthorList = true;

	private boolean enableFormattedPublicationDetails = true;

	private boolean enableTypeAndCategoryLabels = true;

	private boolean enableGroupByCategory;

	private boolean enableGroupByYear;

	private final JournalService service;

	/** Constructor.
	 *
	 * @param service the journal service.
	 */
	public ExporterConfigurator(JournalService service) {
		this.service = service;
	}

	/** Replies the service dedicated to journals.
	 *
	 * @return the service.
	 */
	public JournalService getJournalService() {
		return this.service;
	}

	/** Replies if the publications must be grouped by year.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} if publications are grouped by year.
	 */
	public boolean isGroupedByYear() {
		return this.enableGroupByYear;
	}

	/** Enable the grouping of the publications by year.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator enableGroupByYear() {
		this.enableGroupByYear = true;
		return this;
	}

	/** Replies if the publications must be grouped by category.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} if publications are grouped by category.
	 */
	public boolean isGroupedByCategory() {
		return this.enableGroupByCategory;
	}

	/** Enable the grouping of the publications by category.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator enableGroupByCategory() {
		this.enableGroupByCategory = true;
		return this;
	}

	/** Replies if the labels of the publication type and category are included to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for including HTML labels.
	 */
	public boolean isTypeAndCategoryLabels() {
		return this.enableTypeAndCategoryLabels;
	}

	/** Disable the generation of the labels of the publication type and category are included to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableTypeAndCategoryLabels() {
		this.enableTypeAndCategoryLabels = false;
		return this;
	}

	/** Replies if the HTML links is included to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for including HTML publication details.
	 */
	public boolean isFormattedLinks() {
		return this.enableFormattedLinks;
	}

	/** Disable the generation of the HTML links to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableFormattedLinks() {
		this.enableFormattedLinks = false;
		return this;
	}

	/** Replies if the HTML publication details is included to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for including HTML publication details.
	 */
	public boolean isFormattedPublicationDetails() {
		return this.enableFormattedPublicationDetails;
	}

	/** Disable the generation of the HTML list of authors to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableFormattedPublicationDetails() {
		this.enableFormattedPublicationDetails = false;
		return this;
	}

	/** Replies if the HTML list of authors is included to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for including HTML author list .
	 */
	public boolean isFormattedAuthorList() {
		return this.enableFormattedAuthorList;
	}

	/** Disable the generation of the HTML list of authors to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableFormattedAuthorList() {
		this.enableFormattedAuthorList = false;
		return this;
	}

	/** Replies if the edit buttons are included to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for including edit buttons.
	 */
	public boolean isEditButtons() {
		return this.enableEditButtons;
	}

	/** Disable the inclusion of the edit buttons in the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableEditButtons() {
		this.enableEditButtons = false;
		return this;
	}

	/** Replies if the delete buttons are included to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for including delete buttons.
	 */
	public boolean isDeleteButtons() {
		return this.enableDeleteButtons;
	}

	/** Disable the inclusion of the delete buttons in the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableDeleteButtons() {
		this.enableDeleteButtons = false;
		return this;
	}

	/** Replies if the export buttons are included to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for including export buttons.
	 */
	public boolean isExportButtons() {
		return this.enableExportButtons;
	}

	/** Disable the inclusion of the export buttons in the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableExportButtons() {
		this.enableExportButtons = false;
		return this;
	}

	/** Replies if the download buttons are included to the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for including download buttons.
	 */
	public boolean isDownloadButtons() {
		return this.enableDownloadButtons;
	}

	/** Disable the inclusion of the download buttons in the exported data.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableDownloadButtons() {
		this.enableDownloadButtons = false;
		return this;
	}

	/** Replies if the titles could be drawn with color font.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for color font.
	 */
	public boolean isColoredTitle() {
		return this.enableTitleColor;
	}

	/** Disable the coloring of the titles.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableTitleColor() {
		this.enableTitleColor = false;
		return this;
	}

	/** Replies if the names of the organization Postdoc/engineer should be formated.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for formatting the names.
	 */
	public boolean isPostdocEngineerNameFormatted() {
		return this.enablePostdocEngineerFormat;
	}

	/** Disable the format of the names of the organization Postdoc/engineer.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disablePostdocEngineerFormat() {
		this.enablePostdocEngineerFormat = false;
		return this;
	}

	/** Replies if the names of the organization PhD students should be formated.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for formatting the names.
	 */
	public boolean isPhDStudentNameFormatted() {
		return this.enablePhDStudentFormat;
	}

	/** Disable the format of the names of the organization PhD students.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disablePhDStudentFormat() {
		this.enablePhDStudentFormat = false;
		return this;
	}

	/** Replies if the names of the organization researchers should be formated.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for formatting the names.
	 */
	public boolean isResearcherNameFormatted() {
		return this.enableResearcherFormat;
	}

	/** Disable the format of the names of the organization researchers.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableResearcherFormat() {
		this.enableResearcherFormat = false;
		return this;
	}

	/** Replies if the names of the selected authors should be formated.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return {@code true} for formatting the names.
	 */
	public boolean isSelectedPersonNameFormatted() {
		return this.enableSelectedPersonFormat;
	}

	/** Disable the format of the names of the selected authors.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @return this.
	 */
	public ExporterConfigurator disableSelectedPersonFormat() {
		this.enableSelectedPersonFormat = false;
		return this;
	}

	/** Change the selector for the persons.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @param selector the selector or {@code null}.
	 * @return this.
	 */
	public ExporterConfigurator selectPerson(Predicate<Person> selector) {
		this.personSelector = selector;
		return this;
	}

	/** Replies if a person selector is defined
	 *
	 * @return {@code true} if a person selector is defined.
	 */
	public boolean hasPersonSelector() {
		return this.personSelector != null;
	}

	/** Change the selector for the organization.
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @param selector the selector or {@code null}.
	 * @return this.
	 */
	public ExporterConfigurator selectOrganization(Predicate<ResearchOrganization> selector) {
		this.organizationSelector = selector;
		return this;
	}

	/** Replies if an organization selector is defined
	 *
	 * @return {@code true} if an organization selector is defined.
	 */
	public boolean hasOrganizationSelector() {
		return this.organizationSelector != null;
	}

	private static boolean isValidPeriod(Membership membership, int year) {
		final LocalDate start = LocalDate.of(year, 1, 1);
		final LocalDate end = LocalDate.of(year, 12, 31);
		return membership.isActiveIn(start, end);
	}

	private static boolean isResearcher(Membership membership) {
		final MemberStatus it = membership.getMemberStatus();
		return it.isResearcher() && it != MemberStatus.POSTDOC && it != MemberStatus.PHD_STUDENT
				&& it != MemberStatus.ASSOCIATED_MEMBER;
	}

	private static boolean isPhDStudent(Membership membership) {
		final MemberStatus it = membership.getMemberStatus();
		return it == MemberStatus.PHD_STUDENT;
	}

	private static boolean isPostdocEngineer(Membership membership) {
		final MemberStatus it = membership.getMemberStatus();
		return it.isTechnicalStaff() || it == MemberStatus.POSTDOC;
	}

	private static ExportedAuthorStatus max(ExportedAuthorStatus status0, ExportedAuthorStatus status1) {
		if (status0.ordinal() <= status1.ordinal()) {
			return status0;
		}
		return status1;
	}

	/** Replies the status of an exported author. If there is multiple memberships active at the same time,
	 * the highest membership position is considered. 
	 * <p>This feature may be ignored in the implementation of the exporter.
	 *
	 * @param person the person to test.
	 * @param year the current year.
	 * @return the status, never {@code null}.
	 */
	public ExportedAuthorStatus getExportedAuthorStatusFor(Person person, int year) {
		if (person != null) {
			if (this.personSelector != null && this.personSelector.test(person)) {
				return ExportedAuthorStatus.SELECTED_PERSON;
			}
			ExportedAuthorStatus status = ExportedAuthorStatus.OTHER;
			for (final Membership membership : person.getMemberships()) {
				if (isValidPeriod(membership, year)
						&& (this.organizationSelector == null || this.organizationSelector.test(membership.getResearchOrganization()))) {
					if (isResearcher(membership)) {
						status = max(status, ExportedAuthorStatus.RESEARCHER);
					} else if (isPhDStudent(membership)) {
						status = max(status, ExportedAuthorStatus.PHD_STUDENT);
					} else if (isPostdocEngineer(membership)) {
						status = max(status, ExportedAuthorStatus.POSTDOC_ENGINEER);
					}
				}
			}
			return status;
		}
		return ExportedAuthorStatus.OTHER;
	}

	/** Register a parameter that should be added to the URLs as query parameter.
	 *
	 * @param name the name of the parameter.
	 * @param value the value of the parameter.
	 */
	public void addUriQueryParam(String name, Integer value) {
		this.queryParameters.put(name, value);
	}

	/** Register a parameter that should be added to the URLs as query parameter.
	 *
	 * @param name the name of the parameter.
	 * @param value the value of the parameter.
	 */
	public void addUriQueryParam(String name, String value) {
		this.queryParameters.put(name, value);
	}

	/** Add the registered qyery parameters to the given builder.
	 *
	 * @param uriBuilder the builder to fill up.
	 * @return the {@code uriBuilder}.
	 */
	public UriBuilder applyQueryParams(UriBuilder uriBuilder) {
		UriBuilder b = uriBuilder;
		for (final Entry<String, Object> query : this.queryParameters.entrySet()) {
			b = b.queryParam(query.getKey(), query.getValue());			
		}
		return b;
	}

}
