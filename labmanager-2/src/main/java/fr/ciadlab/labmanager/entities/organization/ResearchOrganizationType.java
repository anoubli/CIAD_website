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

package fr.ciadlab.labmanager.entities.organization;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/** Type of research organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum ResearchOrganizationType {
	/** Research team.
	 */
	RESEARCH_TEAM,
	/** Research department.
	 */
	LABORATORY_DEPARTMENT,
	/** Research laboratory or institute.
	 */
	LABORATORY,
	/** Faculty.
	 */
	FACULTY,
	/** University or college.
	 */
	UNIVERSITY,
	/** High school.
	 */
	HIGH_SCHOOL,
	/** University community or network.
	 */
	COMMUNITY,
	/** OTHER.
	 */
	OTHER;

	/** Default organization type.
	 */
	public static final ResearchOrganizationType DEFAULT = LABORATORY;

	private static final String MESSAGE_PREFIX = "researchOrganizationType."; //$NON-NLS-1$

	private MessageSourceAccessor messages;
	
	/** Replies the message accessor to be used.
	 *
	 * @return the accessor.
	 */
	public MessageSourceAccessor getMessageSourceAccessor() {
		if (this.messages == null) {
			this.messages = BaseMessageSource.getStaticMessageSourceAccessor();
		}
		return this.messages;
	}

	/** Change the message accessor to be used.
	 *
	 * @param messages the accessor.
	 */
	public void setMessageSourceAccessor(MessageSourceAccessor messages) {
		this.messages = messages;
	}

	/** Replies the label of the type of organization.
	 *
	 * @return the label of the type of organization.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the type of organization.
	 *
	 * @param locale the locale to use.
	 * @return the label of the type of organization.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the type of organization that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the type of organization, to search for.
	 * @return the status.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type.
	 */
	public static ResearchOrganizationType valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final ResearchOrganizationType status : values()) {
				if (name.equalsIgnoreCase(status.name())) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("Invalid type of organization: " + name); //$NON-NLS-1$
	}

}
