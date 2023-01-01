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

package fr.ciadlab.labmanager;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.service.conference.ConferenceService;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation of a JEE component.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractComponent {

	/** Constants of the application.
	 */
	protected Constants constants;

	private MessageSourceAccessor messages;

	private Logger logger;

	private final Random random = new Random();

	/** Indicates if the component is launched in debug mode.
	 */
	@Value("${labmanager.debug}")
	protected boolean debugVersion;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public AbstractComponent(MessageSourceAccessor messages, Constants constants) {
		this.messages = messages;
		this.constants = constants;
	}

	/** Replies the logger of this service.
	 *
	 * @return the logger.
	 */
	public Logger getLogger() {
		if (this.logger == null) {
			this.logger = createLogger();
		}
		return this.logger;
	}

	/** Change the logger of this controller.
	 *
	 * @param logger the logger.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/** Factory method for creating the controller logger.
	 *
	 * @return the logger.
	 */
	protected Logger createLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	/** Replies the message provider of this controller.
	 *
	 * @return the accessor.
	 */
	public MessageSourceAccessor getMessageSourceAccessor() {
		return this.messages;
	}

	/** Short-hand function for {@link #getMessageSourceAccessor()} and {@link MessageSourceAccessor#getMessage(String, Object[])}.
	 *
	 * @param key the message id.
	 * @param args the arguments to inject in the string.
	 * @return the string.
	 */
	public String getMessage(String key, Object... args) {
		return getMessageSourceAccessor().getMessage(key, args);
	}

	/** Change the message provider of this controller.
	 *
	 * @param accessor the accessor.
	 */
	public void setMessageSourceAccessor(MessageSourceAccessor accessor) {
		this.messages = accessor;
	}

	/** Generate an UUID.
	 *
	 * @return the UUID.
	 */
	protected Integer generateUUID() {
		return Integer.valueOf(Math.abs(this.random.nextInt()));
	}

	/** Get the value from the given map for an attribute with the given name.
	 * <p>This function generates an exception if the value is {@code null} or empty.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value
	 * @see #optionalString(Map, String)
	 */
	protected static String ensureString(Map<String, String> attributes, String name) {
		final Object param = attributes.get(name);
		if (param == null) {
			throw new IllegalArgumentException("Missed string parameter: " + name); //$NON-NLS-1$
		}
		final String str = param.toString();
		if (Strings.isNullOrEmpty(str)) {
			throw new IllegalArgumentException("Missed string parameter: " + name); //$NON-NLS-1$
		}
		return str;
	}

	/** Get the value from the given map for an attribute with the given name.
	 * <p>This function does not generate an exception if the value is {@code null} or empty.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value
	 * @see #ensureString(Map, String)
	 */
	protected static String optionalString(Map<String, String> attributes, String name) {
		final Object param = attributes.get(name);
		if (param == null) {
			return null;
		}
		final String str = param.toString();
		if (Strings.isNullOrEmpty(str)) {
			return null;
		}
		return str;
	}

	/** Get the value from the given map for an attribute with the given name.
	 * <p>This function does not generate an exception if the value is {@code null} or empty.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value
	 * @see #ensureString(Map, String)
	 */
	protected static boolean optionalBoolean(Map<String, String> attributes, String name) {
		final Object param = attributes.get(name);
		if (param == null) {
			return false;
		}
		final String str = param.toString();
		if (Strings.isNullOrEmpty(str)) {
			return false;
		}
		try {
			return Boolean.parseBoolean(str);
		} catch (Throwable ex) {
			return false;
		}
	}

	/** Get the local date value from the given map for an attribute with the given name.
	 * The attribute must follow one of the formats: {@code YYYY-MM-DD}, {@code YYYY-MD}.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value
	 */
	protected static LocalDate optionalDate(Map<String, String> attributes, String name) {
		final String dateStr = ensureString(attributes, name);
		LocalDate date;
		try {
			date = LocalDate.parse(dateStr);
		} catch (Throwable ex0) {
			// Test if the date is only "YYYY-MM"
			try {
				date = LocalDate.parse(dateStr + "-01"); //$NON-NLS-1$
			} catch (Throwable ex1) {
				date = null;
			}
		}
		return date;
	}

	/** Get the year value from the given map for an attribute with the given name.
	 * The attribute must follow one of the formats: {@code YYYY-MM-DD}, {@code YYYY-MD}, or {@code YYYY}.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value
	 */
	protected static int ensureYear(Map<String, String> attributes, String name) {
		final String dateStr = ensureString(attributes, name);
		LocalDate date;
		try {
			date = LocalDate.parse(dateStr);
		} catch (Throwable ex0) {
			// Test if the date is only "YYYY-MM"
			try {
				date = LocalDate.parse(dateStr + "-01"); //$NON-NLS-1$
			} catch (Throwable ex1) {
				// Test if the date is only "YYYY"
				try {
					date = LocalDate.parse(dateStr + "-01-01"); //$NON-NLS-1$
				} catch (Throwable ex2) {
					date = null;
				}
			}
		}
		if (date == null) {
			throw new IllegalArgumentException("Invalid date parameter: " + name); //$NON-NLS-1$
		}
		return date.getYear();
	}

	/** Replies the constants associated to this application.
	 *
	 * @return the constants.
	 */
	protected Constants getApplicationConstants() {
		if (this.constants == null) {
			this.constants = new Constants();
		}
		return this.constants;
	}

	/** Find the journal object that corresponds to the given identifier or name.
	 *
	 * @param journal the identifier or the name of the journal.
	 * @param jorunalService the service that must be used for accessing the journal's object.
	 * @return the journal or {@code null}.
	 */
	protected static Journal getJournalWith(String journal, JournalService journalService) {
		if (!Strings.isNullOrEmpty(journal)) {
			try {
				final int id = Integer.parseInt(journal);
				final Journal journalObj = journalService.getJournalById(id);
				if (journalObj != null) {
					return journalObj;
				}
			} catch (Throwable ex) {
				//
			}
			final Journal journalObj = journalService.getJournalByName(journal);
			return journalObj;
		}
		return null;
	}
	
	protected static Conference getConferenceWith(String conference, ConferenceService conferenceService) {
		if (!Strings.isNullOrEmpty(conference)) {
			try {
				final int id = Integer.parseInt(conference);
				final Conference conferenceObj = conferenceService.getConferenceById(id);
				if (conferenceObj != null) {
					return conferenceObj;
				}
			} catch (Throwable ex) {
				//
			}
			final Conference conferenceObj = conferenceService.getConferenceByName(conference);
			return conferenceObj;
		}
		return null;
	}

	/** Find the person object that corresponds to the given identifier or name.
	 *
	 * @param dbId the database identifier of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param webId the identifier of the webpage of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param name the name of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param personService the service that must be used for accessing the person's object.
	 * @param nameParser the parser of person names to be used for extracting first and last names.
	 * @return the person or {@code null}.
	 */
	protected static Person getPersonWith(
			Integer dbId, String webId, String name,
			PersonService personService, PersonNameParser nameParser) {
		if (dbId != null && dbId.intValue() != 0) {
			return personService.getPersonById(dbId.intValue());
		}
		if (!Strings.isNullOrEmpty(webId)) {
			return personService.getPersonByWebPageId(webId);
		}
		if (!Strings.isNullOrEmpty(name)) {
			final String firstName = nameParser.parseFirstName(name);
			final String lastName = nameParser.parseLastName(name);
			final Person personObj = personService.getPersonBySimilarName(firstName, lastName);
			return personObj;
		}
		return null;
	}

	/** Find the organization object that corresponds to the given identifier, acronym or name.
	 *
	 * @param organization the identifier, acronym or the name of the organization.
	 * @param organizationService the service that permits to access to the organization object.
	 * @return the organization or {@code null}.
	 */
	protected static ResearchOrganization getOrganizationWith(String organization, ResearchOrganizationService organizationService) {
		if (!Strings.isNullOrEmpty(organization)) {
			try {
				final int id = Integer.parseInt(organization);
				final Optional<ResearchOrganization> organizationObj = organizationService.getResearchOrganizationById(id);
				if (organizationObj.isPresent()) {
					return organizationObj.get();
				}
			} catch (Throwable ex) {
				//
			}
			final Optional<ResearchOrganization> organizationObj = organizationService.getResearchOrganizationByAcronymOrName(organization);
			if (organizationObj.isPresent()) {
				return organizationObj.get();
			}
		}
		return null;
	}

}
