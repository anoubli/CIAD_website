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

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.runners.ConditionalOnInitializationLock;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.utils.MaintenanceException;
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

	private static final String UNSET_STR = "-"; //$NON-NLS-1$
	
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

	@Value("${labmanager.init.data-source")
	private String dataSource;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public AbstractComponent(MessageSourceAccessor messages, Constants constants) {
		this.messages = messages;
		this.constants = constants;
	}

	/** Check if the server is on maintenance.
	 * If the service is on maintenance an {@link MaintenanceException exception} is thrown.
	 * This exception could be catch by the server for building a proper HTTP response.
	 */
	protected void checkMaintenance() {
		final File lockFile = ConditionalOnInitializationLock.getLockFilename(this.dataSource);
		if (lockFile != null && lockFile.exists()) {
			throw new MaintenanceException();
		}
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
	protected static String ensureString(Map<String, ?> attributes, String name) {
		final String param = inString(attributes.get(name));
		if (param == null) {
			throw new IllegalArgumentException("Missed string parameter: " + name); //$NON-NLS-1$
		}
		return param;
	}

	/** Get the value from the given map for an attribute with the given name.
	 * <p>This function does not generate an exception if the value is {@code null} or empty.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value
	 * @see #ensureString(Map, String)
	 */
	protected static String optionalString(Map<String, ?> attributes, String name) {
		final String param = inString(attributes.get(name));
		if (Strings.isNullOrEmpty(param)) {
			return null;
		}
		return param;
	}

	/** Get the value from the given map for an attribute with the given name.
	 * <p>This function does not generate an exception if the value is {@code null} or empty.
	 * <p>If the string value is equal to {@code -}, the function replies {@code null}
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value
	 * @see #optionalString(Map, String)
	 */
	protected static String optionalStringWithUnsetConstant(Map<String, ?> attributes, String name) {
		final String param = inString(attributes.get(name));
		if (Strings.isNullOrEmpty(param) || UNSET_STR.equals(param)) {
			return null;
		}
		return param;
	}

	/** Get the value from the given map for an attribute with the given name.
	 * <p>This function does not generate an exception if the value is {@code null} or empty.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value
	 * @see #ensureString(Map, String)
	 */
	protected static boolean optionalBoolean(Map<String, ?> attributes, String name) {
		final String param = inString(attributes.get(name));
		if (param == null) {
			return false;
		}
		try {
			return Boolean.parseBoolean(param);
		} catch (Throwable ex) {
			return false;
		}
	}

	/** Get the value from the given map for an attribute with the given name.
	 * <p>This function does not generate an exception if the value is {@code null} or empty.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value or {@link Float#NaN}.
	 * @see #ensureString(Map, String)
	 */
	protected static float optionalFloat(Map<String, ?> attributes, String name) {
		final String param = inString(attributes.get(name));
		if (Strings.isNullOrEmpty(param)) {
			return Float.NaN;
		}
		try {
			return Float.parseFloat(param);
		} catch (Throwable ex) {
			return Float.NaN;
		}
	}

	/** Get the value from the given map for an attribute with the given name.
	 * <p>This function does not generate an exception if the value is {@code null} or empty.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @param type the type of the enum.
	 * @return the value or {@link Float#NaN}.
	 * @see #ensureString(Map, String)
	 */
	protected static <E extends Enum<E>> E optionalEnum(Map<String, ?> attributes, String name, Class<E> type) {
		final String param = inString(attributes.get(name));
		if (!Strings.isNullOrEmpty(param)) {
			for (final E enumConstant : type.getEnumConstants()) {
				if (enumConstant.name().equalsIgnoreCase(param)) {
					return enumConstant;
				}
			}
		}
		return null;
	}

	/** Get the local date value from the given map for an attribute with the given name.
	 * The attribute must follow one of the formats: {@code YYYY-MM-DD}, {@code YYYY-MD}.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value
	 */
	protected static LocalDate optionalDate(Map<String, ?> attributes, String name) {
		final String dateStr = optionalString(attributes, name);
		LocalDate date = null;
		if (!Strings.isNullOrEmpty(dateStr)) {
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
	protected static int ensureYear(Map<String, ?> attributes, String name) {
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

	/** Get the integer value from the given map for an attribute with the given name.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value
	 */
	protected static int ensureInt(Map<String, ?> attributes, String name) {
		final String intStr = ensureString(attributes, name);
		try {
			return Integer.parseInt(intStr);
		} catch (Throwable ex) {
			throw new IllegalArgumentException(ex);
		}
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

	/** Clean and normalized a string that is provided as input to an endpoint.
	 *
	 * @param input the input string to the endpoint.
	 * @return the normalized string that is equivalent to the argument.
	 * @see #inStringCr(String)
	 */
	public static String inString(Object input) {
		final String strInput = input == null ? null : input.toString();
		String out = Strings.emptyToNull(strInput);
		if (out != null) {
			out = out.trim();
			out = Strings.emptyToNull(out);
		}
		return out;
	}

	/** Loop on the persons that are described in the provided list.
	 * This list may contain the identifiers of the persons or the full names of the persons.
	 *
	 * @param persons the list of identifiers or full names.
	 * @param createPerson indicates if any person that is not yet stored in the database must be created on the fly.
	 * @param personService the service for accessing the persons in the database.
	 * @param nameParser the parser of person names.
	 * @param consumer invoked for each person in the list.
	 */
	protected void forEarchPerson(List<String> persons, boolean createPerson,
			PersonService personService, PersonNameParser nameParser, Consumer<Person> consumer) {
		final Pattern idPattern = Pattern.compile("\\d+"); //$NON-NLS-1$
		for (final String personDesc : persons) {
			final Person person = extractPerson(personDesc, idPattern, createPerson, personService, nameParser);
			if (person != null) {
				consumer.accept(person);
			}
		}
	}

	/** Extract a person from the description.
	 * This description may contain the identifier of the person or the full name of the person.
	 *
	 * @param personDesc the identifier or full name.
	 * @param createPerson indicates if any person that is not yet stored in the database must be created on the fly.
	 * @param personService the service for accessing the persons in the database.
	 * @param nameParser the parser of person names.
	 */
	protected Person extractPerson(String personDesc, boolean createPerson,
			PersonService personService, PersonNameParser nameParser) {
		final Pattern idPattern = Pattern.compile("\\d+"); //$NON-NLS-1$
		return extractPerson(personDesc, idPattern, createPerson, personService, nameParser);
	}

	private Person extractPerson(String personDesc, Pattern idPattern, boolean createPerson,
			PersonService personService, PersonNameParser nameParser) {
		Person person = null;
		int personId = 0;
		if (idPattern.matcher(personDesc).matches()) {
			// Numeric value means that the person is known.
			try {
				personId = Integer.parseInt(personDesc);
			} catch (Throwable ex) {
				// Silent
			}
		}
		if (personId == 0) {
			// The person seems to be not in the database already. Check it based on the name.
			final String firstName = nameParser.parseFirstName(personDesc);
			final String lastName = nameParser.parseLastName(personDesc);
			personId = personService.getPersonIdByName(firstName, lastName);
			if (personId == 0) {
				// Now, it is sure that the person is unknown
				if (createPerson) {
					person = personService.createPerson(firstName, lastName);
					getLogger().info("New person \"" + personDesc + "\" created with id: " + person.getId()); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				person = personService.getPersonById(personId);
				if (person == null) {
					throw new IllegalArgumentException("Unknown person with id: " + personId); //$NON-NLS-1$
				}
			}
		} else {
			// Check if the given author identifier corresponds to a known person.
			person = personService.getPersonById(personId);
			if (person == null) {
				throw new IllegalArgumentException("Unknown person with id: " + personId); //$NON-NLS-1$
			}
		}
		return person;
	}

}
