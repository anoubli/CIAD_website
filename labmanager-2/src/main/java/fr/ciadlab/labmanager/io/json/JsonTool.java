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

package fr.ciadlab.labmanager.io.json;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.entities.publication.Publication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Definition of constants for the JSON tools.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public abstract class JsonTool {

	/** Prefix for hidden JSON fields.
	 */
	public static final String HIDDEN_FIELD_PREFIX = "_"; //$NON-NLS-1$

	/** Prefix for special JSON fields.
	 */
	public static final String SPECIAL_FIELD_PREFIX = "@"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to global indicators.
	 */
	public static final String GLOBALINDICATORS_SECTION = "globalIndicators"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to organization addresses.
	 */
	public static final String ORGANIZATIONADDRESSES_SECTION = "organizationAddresses"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to research organizations.
	 */
	public static final String RESEARCHORGANIZATIONS_SECTION = "researchOrganizations"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to persons.
	 */
	public static final String PERSONS_SECTION = "persons"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to memberships.
	 */
	public static final String ORGANIZATION_MEMBERSHIPS_SECTION = "memberships"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to journals.
	 */
	public static final String JOURNALS_SECTION = "journals"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to publications.
	 */
	public static final String PUBLICATIONS_SECTION = "publications"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to jury memberships.
	 */
	public static final String JURY_MEMBERSHIPS_SECTION = "juryMemberships"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to supervisions.
	 */
	public static final String SUPERVISIONS_SECTION = "supervisions"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to invitations.
	 */
	public static final String INVITATIONS_SECTION = "invitations"; //$NON-NLS-1$

	/** Name of the field that contains the source of the data. Value of this field may be
	 * {@code :db} or the name of the source.
	 */
	public static final String HIDDEN_INTERNAL_DATA_SOURCE_KEY = HIDDEN_FIELD_PREFIX + "internalDataSource"; //$NON-NLS-1$

	/** Value of the field {@link #HIDDEN_INTERNAL_DATA_SOURCE_KEY} that represents the database source.
	 */
	public static final String HIDDEN_INTERNAL_DATABASE_SOURCE_VALUE = ":db"; //$NON-NLS-1$

	/** Value of the field {@link #HIDDEN_INTERNAL_DATA_SOURCE_KEY} that represents an external source.
	 */
	public static final String HIDDEN_INTERNAL_EXTERNAL_SOURCE_VALUE = ":ext"; //$NON-NLS-1$

	/** Name of the field that contains the identifier of the data in the database.
	 */
	public static final String HIDDEN_INTERNAL_DATA_SOURCE_ID_KEY = HIDDEN_FIELD_PREFIX + "internalDataSourceId"; //$NON-NLS-1$

	/** Name of the field that indicates if an entity could be imported in the database.
	 * The reason why an entity cannot be imported is not explicitly explained in this field.
	 */
	public static final String HIDDEN_INTERNAL_IMPORTABLE_KEY = HIDDEN_FIELD_PREFIX + "internalImportable"; //$NON-NLS-1$

	/** Name of the field that indicates if a journal must be created for importing a publication in the database.
	 */
	public static final String HIDDEN_INTERNAL_NEW_JOURNAL_KEY = HIDDEN_FIELD_PREFIX + "internalNewJournal"; //$NON-NLS-1$

	/** Name of the field for the visible global indicators.
	 */
	public static final String VISIBLEGLOBALINDICATORS_KEY = "visibleGlobalIndicators"; //$NON-NLS-1$

	/** Name of the field for the year. This field is usually for {@link Publication}.
	 *
	 * @see #YEAR_KEY
	 * @see #REFERENCEYEAR_KEY
	 */
	public static final String PUBLICATIONYEAR_KEY = "publicationYear"; //$NON-NLS-1$

	/** Name of the field for the title. This field is usually for {@link Publication}.
	 */
	public static final String TITLE_KEY = "title"; //$NON-NLS-1$

	/** Name of the field for the DOI. This field is usually for {@link Publication}.
	 */
	public static final String DOI_KEY = "doi"; //$NON-NLS-1$

	/** Name of the field for the ISSN. This field is usually for {@link Publication}.
	 */
	public static final String ISSN_KEY = "issn"; //$NON-NLS-1$

	/** Name of the field for the Edition. This field is usually for {@link Publication}.
	 */
	public static final String EDITION_KEY = "edition"; //$NON-NLS-1$

	/** Name of the field for the publisher. This field is usually for {@link Publication}.
	 */
	public static final String PUBLISHER_KEY = "publisher"; //$NON-NLS-1$

	/** Name of the field for the book title. This field is usually for {@link Publication}.
	 */
	public static final String BOOKTITLE_KEY = "bookTitle"; //$NON-NLS-1$

	/** Name of the field for the event name. This field is usually for {@link Publication}.
	 */
	public static final String SCIENTIFICEVENTNAME_KEY = "scientificEventName"; //$NON-NLS-1$

	/** Name of the field for the "how published". This field is usually for {@link Publication}.
	 */
	public static final String HOWPUBLISHED_KEY = "howPublished"; //$NON-NLS-1$

	/** Name of the field for the institution. This field is usually for {@link Publication}.
	 */
	public static final String INSTITUTION_KEY = "institution"; //$NON-NLS-1$

	/** Name of the field for the journal of a journal paper.
	 */
	public static final String JOURNAL_KEY = "journal"; //$NON-NLS-1$

	/** Name of the field for indicating of a published was manually validated. This field is usually for {@link Publication}.
	 */
	public static final String MANUALVALIDATIONFORCED_KEY = "manualValidationForced"; //$NON-NLS-1$

	/** Prefix for organization address identifiers.
	 */
	protected  static final String ORGANIZATIONADDRESS_ID_PREFIX = "/adr#"; //$NON-NLS-1$

	/** Prefix for research organization identifiers.
	 */
	protected  static final String RESEARCHORGANIZATION_ID_PREFIX = "/ro#"; //$NON-NLS-1$

	/** Prefix for membership identifiers.
	 */
	protected  static final String MEMBERSHIP_ID_PREFIX = "/mbr#"; //$NON-NLS-1$

	/** Prefix for jury membership identifiers.
	 */
	protected  static final String JURY_MEMBERSHIP_ID_PREFIX = "/jurymbr#"; //$NON-NLS-1$

	/** Prefix for supervision identifiers.
	 */
	protected  static final String SUPERVISION_ID_PREFIX = "/supervis#"; //$NON-NLS-1$

	/** Prefix for invitation identifiers.
	 */
	protected  static final String INVITATION_ID_PREFIX = "/invit#"; //$NON-NLS-1$

	/** Prefix for person identifiers.
	 */
	protected  static final String PERSON_ID_PREFIX = "/pers#"; //$NON-NLS-1$

	/** Prefix for journal identifiers.
	 */
	protected  static final String JOURNAL_ID_PREFIX = "/jour#"; //$NON-NLS-1$

	/** Prefix for publication identifiers.
	 */
	protected  static final String PUBLICATION_ID_PREFIX = "/publi#"; //$NON-NLS-1$

	/** The name of the field that is an JSON identifier.
	 */
	protected static final String ID_FIELDNAME = SPECIAL_FIELD_PREFIX + "id"; //$NON-NLS-1$

	/** The name of the field that is the date of the last modification of the file.
	 */
	protected static final String DATABASE_ID_FIELDNAME = HIDDEN_FIELD_PREFIX + "database-id"; //$NON-NLS-1$

	/** The name of the field that is the date of the last modification of the file.
	 */
	protected static final String LAST_CHANGE_FIELDNAME = HIDDEN_FIELD_PREFIX + "last-update-date"; //$NON-NLS-1$

	/** Prefix for for the setter functions.
	 */
	protected static final String SETTER_FUNCTION_PREFIX = "set"; //$NON-NLS-1$

	/** Prefix for for the getter functions.
	 */
	protected static final String GETTER_FUNCTION_PREFIX = "get"; //$NON-NLS-1$

	/** Prefix for for the "is" (boolean) getter functions.
	 */
	protected static final String IS_GETTER_FUNCTION_PREFIX = "is"; //$NON-NLS-1$

	/** Name of the field for the addresses reference.
	 */
	protected static final String ADDRESSES_KEY = "addresses"; //$NON-NLS-1$

	/** Name of the field for the address reference.
	 */
	protected static final String ADDRESS_KEY = "address"; //$NON-NLS-1$

	/** Name of the field for the super organization reference.
	 */
	protected static final String SUPERORGANIZATION_KEY = "superOrganization"; //$NON-NLS-1$

	/** Name of the field for the person reference.
	 */
	protected static final String PERSON_KEY = "person"; //$NON-NLS-1$

	/** Name of the field for the guest person reference.
	 */
	protected static final String GUEST_KEY = "guest"; //$NON-NLS-1$

	/** Name of the field for the inviter person reference.
	 */
	protected static final String INVITER_KEY = "inviter"; //$NON-NLS-1$

	/** Name of the field for a percentage.
	 */
	protected static final String PERCENT_KEY = "percent"; //$NON-NLS-1$

	/** Name of the field for the candidate reference.
	 */
	protected static final String CANDIDATE_KEY = "candidate"; //$NON-NLS-1$

	/** Name of the field for the promoters' references.
	 */
	protected static final String PROMOTERS_KEY = "promoters"; //$NON-NLS-1$

	/** Name of the field for the supervisors' references.
	 */
	protected static final String SUPERVISORS_KEY = "supervisors"; //$NON-NLS-1$

	/** Name of the field for the abandonment.
	 */
	protected static final String ABANDONMENT_KEY = "abandonment"; //$NON-NLS-1$

	/** Name of the field for the defense date.
	 */
	protected static final String DEFENSE_DATE_KEY = "defenseDate"; //$NON-NLS-1$

	/** Name of the field for the number of ATER positions.
	 */
	protected static final String ATER_COUNT_KEY = "aterCount"; //$NON-NLS-1$

	/** Name of the field for the becoming of.
	 */
	protected static final String BECOMING_KEY = "becoming"; //$NON-NLS-1$

	/** Name of the field for the funding.
	 */
	protected static final String FUNDING_KEY = "funding"; //$NON-NLS-1$

	/** Name of the field for the funding details.
	 */
	protected static final String FUNDING_DETAILS_KEY = "fundingDetails"; //$NON-NLS-1$

	/** Name of the field for the research organization reference.
	 */
	protected static final String RESEARCHORGANIZATION_KEY = "researchOrganization"; //$NON-NLS-1$

	/** Name of the field for the definition of the journals' annual quality indicators.
	 */
	protected static final String QUALITYINDICATORSHISTORY_KEY = "qualityIndicatorsHistory"; //$NON-NLS-1$

	/** Name of the field for the Scimago Q-Index.
	 */
	protected static final String SCIMAGOQINDEX_KEY = "scimagoQIndex"; //$NON-NLS-1$

	/** Name of the field for the Web-of-Science Q-Index.
	 */
	protected static final String WOSQINDEX_KEY = "wosQIndex"; //$NON-NLS-1$

	/** Name of the field for the journal impact factor.
	 */
	protected static final String IMPACTFACTOR_KEY = "impactFactor"; //$NON-NLS-1$

	/** Name of the field for the types.
	 */
	protected static final String TYPE_KEY = "type"; //$NON-NLS-1$

	/** Name of the field for the authors of a publication.
	 */
	protected static final String AUTHORS_KEY = "authors"; //$NON-NLS-1$

	/** Name of the field for the year. This field is usually for {@link Publication}.
	 *
	 * @see #PUBLICATIONYEAR_KEY
	 * @see #REFERENCEYEAR_KEY
	 */
	protected static final String YEAR_KEY = "year"; //$NON-NLS-1$

	/** Name of the field for the year. This field is usually for {@link JournalQualityAnnualIndicators}.
	 *
	 * @see #YEAR_KEY
	 * @see #PUBLICATIONYEAR_KEY
	 */
	protected static final String REFERENCEYEAR_KEY = "referenceYear"; //$NON-NLS-1$

	/** Name of the field for the major language of a publication.
	 *
	 * @see #MAJORLANGUAGE_KEY
	 */
	protected static final String LANGUAGE_KEY = "language"; //$NON-NLS-1$

	/** Name of the field for the major language of a publication.
	 *
	 * @see #LANGUAGE_KEY
	 */
	protected static final String MAJORLANGUAGE_KEY = "majorLanguage"; //$NON-NLS-1$

	/** Name of the field for the month of a publication.
	 */
	protected static final String MONTH_KEY = "month"; //$NON-NLS-1$

	/** Name of the field for the URL of the publication.
	 *
	 * @see #EXTRAURL_KEY
	 */
	protected static final String URL_KEY = "url"; //$NON-NLS-1$

	/** Name of the field for the URL of the publication.
	 *
	 * @see #URL_KEY
	 */
	protected static final String EXTRAURL_KEY = "extraURL"; //$NON-NLS-1$

	/** Name of the field for the first name of a person.
	 */
	protected static final String FIRSTNAME_KEY = "firstName"; //$NON-NLS-1$

	/** Name of the field for the last name of a person.
	 */
	protected static final String LASTNAME_KEY = "lastName"; //$NON-NLS-1$

	/** Name of the field for the full name of a person.
	 */
	protected static final String FULLNAME_KEY = "fullName"; //$NON-NLS-1$

	/** Logger of the service. It is lazy loaded.
	 */
	private Logger logger;

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

	/** Change the logger of this service.
	 *
	 * @param logger the logger.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/** Factory method for creating the service logger.
	 *
	 * @return the logger.
	 */
	protected Logger createLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	/** Extract the raw value from the given node.
	 *
	 * @param node the JSON node.
	 * @return the raw value, or {@code null} if the value cannot be obtained.
	 */
	protected static Object getRawValue(JsonNode node) {
		if (node.isBoolean()) {
			return Boolean.valueOf(node.booleanValue());
		}
		if (node.isShort()) {
			return Short.valueOf(node.shortValue());
		}
		if (node.isInt()) {
			return Integer.valueOf(node.intValue());
		}
		if (node.isLong()) {
			return Long.valueOf(node.longValue());
		}
		if (node.isFloat()) {
			return Float.valueOf(node.floatValue());
		}
		if (node.isDouble()) {
			return Double.valueOf(node.doubleValue());
		}
		if (node.isBigInteger()) {
			return node.bigIntegerValue();
		}
		if (node.isBigDecimal()) {
			return node.decimalValue();
		}
		if (node.isTextual()) {
			return node.textValue();
		}
		return null;
	}

	/** Find the setter methods that matches the given names.
	 *
	 * @param type the type of the object on which the setter function must be invoked.
	 * @param names the set of lower-cased names of the setter functions that could serve as candidates for
	 *     finding the method.
	 * @param value the value to give to the setter function. The value is used for checking the formal parameter's type.
	 * @return the method that could be served as setter, or {@code null} if none was found.
	 */
	protected static Method findSetterMethod(Class<?> type, Set<String> names, Object value) {
		assert value != null;
		try {
			final Class<?> valueType = value.getClass();
			final Collection<Method> methods = Arrays.asList(type.getMethods()).parallelStream().filter(it -> {
				if (it.getParameterCount() == 1 && (names.contains(it.getName().toLowerCase()))) {
					final Class<?> receiver = it.getParameterTypes()[0];
					final Class<?> provider = valueType;
					return receiver.isAssignableFrom(provider);
				}
				return false;
			}) .collect(Collectors.toSet());
			final int size = methods.size();
			if (size > 1) {
				throw new IllegalArgumentException("Too many setter function candidates (case insensitive) in type '" //$NON-NLS-1$
						+ type.getName() + "' with names: " + names.toString()); //$NON-NLS-1$
			} else if (size == 1) {
				return methods.iterator().next();
			}
		} catch (Throwable ex) {
			//
		}
		return null;
	}

	/** Replies all the getter functions of the given type that have a matching setter function.
	 *
	 * @param source the source type.
	 * @return the map of the methods. Keys are the names of the attributes, and the values are the setter functions.
	 */
	protected static Map<String, Method> findGetterMethods(Class<?> source) {
		final Set<String> setters = new TreeSet<>();
		final Map<String, Method> getters = new HashMap<>();
		for (final Method meth : source.getMethods()) {
			final String name = meth.getName().toLowerCase();
			if (meth.getParameterCount() == 1
					&& (meth.getParameterTypes()[0].isPrimitive()
							|| Number.class.equals(meth.getParameterTypes()[0])
							|| String.class.equals(meth.getParameterTypes()[0])
							|| Boolean.class.equals(meth.getParameterTypes()[0]))
					&& name.startsWith(SETTER_FUNCTION_PREFIX)) {
				final String attrName = toLowerFirst(meth.getName().substring(SETTER_FUNCTION_PREFIX.length()));
				setters.add(attrName);
			} else 	if (meth.getParameterCount() == 0
					&& (meth.getReturnType().isPrimitive()
							|| meth.getReturnType().isEnum()
							|| Number.class.isAssignableFrom(meth.getReturnType())
							|| String.class.isAssignableFrom(meth.getReturnType())
							|| Boolean.class.isAssignableFrom(meth.getReturnType())
							|| LocalDate.class.isAssignableFrom(meth.getReturnType())
							|| Character.class.isAssignableFrom(meth.getReturnType()))
					&& (name.startsWith(GETTER_FUNCTION_PREFIX)
							|| name.startsWith(IS_GETTER_FUNCTION_PREFIX))) {
				if (name.startsWith(IS_GETTER_FUNCTION_PREFIX)) {
					final String attrName = toLowerFirst(meth.getName().substring(IS_GETTER_FUNCTION_PREFIX.length()));
					getters.put(attrName, meth);
				} else {
					final String attrName = toLowerFirst(meth.getName().substring(GETTER_FUNCTION_PREFIX.length()));
					getters.put(attrName, meth);
				}
			}
		}
		//
		getters.remove("id"); //$NON-NLS-1$
		//
		final Iterator<Entry<String, Method>> iterator = getters.entrySet().iterator();
		while (iterator.hasNext()) {
			final Entry<String, Method> entry = iterator.next();
			if (!setters.contains(entry.getKey())) {
				iterator.remove();
			}
		}
		//
		return getters;
	}

	private static String toLowerFirst(String name) {
		if (name.length() <= 0) {
			return name.toLowerCase();
		}
		final Pattern pattern = Pattern.compile("^[A-Z]+$"); //$NON-NLS-1$
		final Matcher matcher = pattern.matcher(name);
		if (matcher.matches()) {
			return name.toLowerCase();
		}
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	/** Convert the given value into an equivalent value that could be saved into a JSON.
	 *
	 * @param value the value to convert.
	 * @return the JSON compliant value, or {@code null} if the value cannot be saved into JSON.
	 */
	protected static Object convertValue(Object value) {
		if (value != null) {
			final Class<?> type = value.getClass();
			if (value instanceof CharSequence) {
				return Strings.emptyToNull(((CharSequence) value).toString());
			}
			if (type.isPrimitive() || value instanceof Number || value instanceof Boolean) {
				return value;
			}
			if (value instanceof Enum) {
				return ((Enum<?>) value).name().toLowerCase();
			}
			if (value instanceof LocalDate) {
				return ((LocalDate) value).toString();
			}
			if (value instanceof Character) {
				return ((Character) value).toString();
			}
		}
		return null;
	}

	/** Create a reference to another entity.
	 *
	 * @param id the identifier.
	 * @param factory the factory of JSON nodes.
	 */
	protected static JsonNode createReference(String id, JsonNodeCreator factory) {
		final ObjectNode ref = factory.objectNode();
		ref.set(ID_FIELDNAME, factory.textNode(id));
		return ref;
	}

	/** Add entity reference to the given receiver.
	 *
	 * @param receiver the receiver of JSON.
	 * @param key the key that should receive the reference.
	 * @param id the identifier.
	 */
	protected static void addReference(ObjectNode receiver, String key, String id) {
		final JsonNode ref = createReference(id, receiver);
		receiver.set(key, ref);
	}

}
