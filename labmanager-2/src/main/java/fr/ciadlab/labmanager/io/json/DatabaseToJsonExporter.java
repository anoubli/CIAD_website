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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.repository.journal.JournalRepository;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Exporter of JSON data from the database.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
public class DatabaseToJsonExporter extends JsonTool {

	private ResearchOrganizationRepository organizationRepository;

	private PersonRepository personRepository;

	private MembershipRepository membershipRepository;

	private JournalRepository journalRepository;

	private PublicationRepository publicationRepository;

	/** Constructor.
	 * 
	 * @param organizationRepository the accessor to the organization repository.
	 * @param personRepository the accessor to the person repository.
	 * @param membershipRepository the accessor to the membership repository.
	 * @param journalRepository the accessor to the journal repository.
	 * @param publicationRepository the accessor to the repository of the publications.
	 */
	public DatabaseToJsonExporter(
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired PersonRepository personRepository,
			@Autowired MembershipRepository membershipRepository,
			@Autowired JournalRepository journalRepository,
			@Autowired PublicationRepository publicationRepository) {
		this.organizationRepository = organizationRepository;
		this.personRepository = personRepository;
		this.membershipRepository = membershipRepository;
		this.journalRepository = journalRepository;
		this.publicationRepository = publicationRepository;
	}

	/** Run the exporter.
	 *
	 * @return the JSON content or {@code null} if empty.
	 * @throws Exception if there is problem for exporting.
	 */
	public Map<String, Object> exportFromDatabase() throws Exception {
		return exportFromDatabase(null, null);
	}

	/** Run the exporter.
	 *
	 * @param similarPublicationProvider a provider of a publication that is similar to a given publication. 
	 *      If this argument is not {@code null} and if it replies a similar publication, the information in this
	 *      similar publication is used to complete the JSON file that is initially filled up with the source publication.
	 * @param extraPublicationProvider this provider gives publications that must be exported into the JSON that are
	 *      not directly extracted from the database. If this argument is {@code null}, no extra publication is exported. 
	 * @return the JSON content or {@code null} if empty.
	 * @throws Exception if there is problem for exporting.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> exportFromDatabase(SimilarPublicationProvider similarPublicationProvider,
			ExtraPublicationProvider extraPublicationProvider) throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		final JsonNode obj = exportFromDatabaseToJsonObject(mapper.getNodeFactory(), similarPublicationProvider, extraPublicationProvider);
		if (obj != null) {
			return mapper.treeToValue(obj, Map.class);
		}
		return null;
	}

	/** Run the exporter for creating JSON objects.
	 *
	 * @param factory the factory of nodes.
	 * @return the JSON content.
	 * @throws Exception if there is problem for exporting.
	 */
	public JsonNode exportFromDatabaseToJsonObject(JsonNodeCreator factory) throws Exception {
		return exportFromDatabaseToJsonObject(factory, null, null);
	}

	/** Run the exporter for creating JSON objects.
	 *
	 * @param factory the factory of nodes.
	 * @param similarPublicationProvider a provider of a publication that is similar to a given publication. 
	 *      If this argument is not {@code null} and if it replies a similar publication, the information in this
	 *      similar publication is used to complete the JSON file that is initially filled up with the source publication.
	 * @param extraPublicationProvider this provider gives publications that must be exported into the JSON that are
	 *      not directly extracted from the database. If this argument is {@code null}, no extra publication is exported. 
	 * @return the JSON content.
	 * @throws Exception if there is problem for exporting.
	 */
	public JsonNode exportFromDatabaseToJsonObject(JsonNodeCreator factory, SimilarPublicationProvider similarPublicationProvider,
			ExtraPublicationProvider extraPublicationProvider) throws Exception {
		final ObjectNode root = factory.objectNode();
		final Map<Object, String> repository = new HashMap<>();
		exportOrganizations(root, repository);
		exportPersons(root, repository);
		exportMemberships(root, repository);
		exportJournals(root, repository);
		exportPublications(root, repository, similarPublicationProvider, extraPublicationProvider);
		if (root.size() > 0) {
			root.set(LAST_CHANGE_FIELDNAME, factory.textNode(LocalDate.now().toString()));
			return root;
		}
		return null;
	}

	/** Export the given object to the receiver.
	 *
	 * @param receiver the receiver of JSON.
	 * @param id the identifier.
	 * @param object the object to export.
	 * @param factory the factory of nodes.
	 * @param complements the objects that are of the same type of {@code object} but that could be used for obtaining additional data
	 *     that is missed from the original object.
	 * @throws Exception if there is problem for exporting.
	 */
	@SuppressWarnings("static-method")
	protected void exportObject(JsonNode receiver, String id, Object object, JsonNodeCreator factory, List<?> complements) throws Exception {
		if (object != null) {
			final ObjectNode rec = (ObjectNode) receiver;
			if (!Strings.isNullOrEmpty(id)) {
				rec.set(ID_FIELDNAME, factory.textNode(id));
			}
			final Map<String, Method> meths = findGetterMethods(object.getClass());
			for (final Entry<String, Method> entry : meths.entrySet()) {
				final Method method = entry.getValue();
				Object objValue = convertValue(method.invoke(object));
				if (objValue == null && complements != null && !complements.isEmpty()) {
					final Iterator<?> iterator = complements.iterator();
					while (iterator.hasNext() && objValue == null) {
						final Object complement = iterator.next();
						objValue = convertValue(method.invoke(complement));
					}
				}
				if (objValue instanceof String) {
					rec.set(entry.getKey(), factory.textNode((String) objValue));
				} else if (objValue instanceof Byte) {
					rec.set(entry.getKey(), factory.numberNode((Byte) objValue));
				} else if (objValue instanceof Short) {
					rec.set(entry.getKey(), factory.numberNode((Short) objValue));
				} else if (objValue instanceof Integer) {
					rec.set(entry.getKey(), factory.numberNode((Integer) objValue));
				} else if (objValue instanceof Long) {
					rec.set(entry.getKey(), factory.numberNode((Long) objValue));
				} else if (objValue instanceof Float) {
					rec.set(entry.getKey(), factory.numberNode((Float) objValue));
				} else if (objValue instanceof BigDecimal) {
					rec.set(entry.getKey(), factory.numberNode((BigDecimal) objValue));
				} else if (objValue instanceof BigInteger) {
					rec.set(entry.getKey(), factory.numberNode((BigInteger) objValue));
				} else if (objValue instanceof Number) {
					rec.set(entry.getKey(), factory.numberNode(((Number) objValue).doubleValue()));
				} else if (objValue instanceof Boolean) {
					rec.set(entry.getKey(), factory.booleanNode(((Boolean) objValue).booleanValue()));
				} else if (objValue instanceof Character) {
					rec.set(entry.getKey(), factory.textNode(((Character) objValue).toString()));
				}
			}
		}
	}

	/** Export the research organizations to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportOrganizations(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<ResearchOrganization> organizations = this.organizationRepository.findAll();
		if (!organizations.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			final Map<Integer, ObjectNode> nodes = new TreeMap<>();
			for (final ResearchOrganization organization : organizations) {
				final ObjectNode jsonOrganization = array.objectNode();

				final String id = RESEARCHORGANIZATION_ID_PREFIX + i;
				exportObject(jsonOrganization, id, organization, jsonOrganization, null);

				if (jsonOrganization.size() > 0) {
					repository.put(organization, id);
					nodes.put(Integer.valueOf(organization.getId()), jsonOrganization);
					array.add(jsonOrganization);
					++i;
				}
			}
			// Export the super organization for enabling the building of the organization hierarchy
			// This loop is externalized to be sure all the organizations are in the repository.
			for (final ResearchOrganization organization : organizations) {
				if (organization.getSuperOrganization() != null) {
					final String id = repository.get(organization.getSuperOrganization());
					if (Strings.isNullOrEmpty(id)) {
						throw new IllegalStateException("Organization not found: " + organization.getAcronymOrName()); //$NON-NLS-1$
					}
					final ObjectNode objNode = nodes.get(Integer.valueOf(organization.getId()));
					if (objNode == null) {
						throw new IllegalStateException("No JSON node created for organization: " + organization.getAcronymOrName()); //$NON-NLS-1$
					}
					addReference(objNode, SUPERORGANIZATION_KEY, id);
				}
			}
			if (array.size() > 0) {
				root.set(RESEARCHORGANIZATIONS_SECTION, array);
			}
		}
	}


	/** Export the persons to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportPersons(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<Person> persons = this.personRepository.findAll();
		if (!persons.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final Person person : persons) {
				final ObjectNode jsonPerson = array.objectNode();

				final String id = PERSON_ID_PREFIX + i;
				exportObject(jsonPerson, id, person, jsonPerson, null);

				if (jsonPerson.size() > 0) {
					repository.put(person, id);
					array.add(jsonPerson);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(PERSONS_SECTION, array);
			}
		}
	}

	/** Export the memberships to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportMemberships(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<Membership> memberships = this.membershipRepository.findAll();
		if (!memberships.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final Membership membership : memberships) {
				final String personId = repository.get(membership.getPerson());
				final String organizationId = repository.get(membership.getResearchOrganization());
				if (!Strings.isNullOrEmpty(personId) && !Strings.isNullOrEmpty(organizationId)) {
					final ObjectNode jsonMembership = array.objectNode();

					final String id = MEMBERSHIP_ID_PREFIX + i;
					exportObject(jsonMembership, id, membership, jsonMembership, null);

					// Person and organization must be added explicitly because the "exportObject" function
					// ignore the getter functions for both.
					addReference(jsonMembership, PERSON_KEY, personId);
					addReference(jsonMembership, RESEARCHORGANIZATION_KEY, organizationId);

					if (jsonMembership.size() > 0) {
						array.add(jsonMembership);
						++i;
					}
				}
			}
			if (array.size() > 0) {
				root.set(MEMBERSHIPS_SECTION, array);
			}
		}
	}

	/** Export the journals to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportJournals(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<Journal> journals = this.journalRepository.findAll();
		if (!journals.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final Journal journal : journals) {
				final ObjectNode jsonJournal = array.objectNode();

				final String id = JOURNAL_ID_PREFIX + i;
				exportObject(jsonJournal, id, journal, jsonJournal, null);

				// Add the publication indicators by hand because they are not exported implicitly by
				// the "exportObject" function
				final ObjectNode indicatorMap = jsonJournal.objectNode();
				for (final JournalQualityAnnualIndicators indicators : journal.getQualityIndicators().values()) {
					final ObjectNode jsonIndicator = indicatorMap.objectNode();
					exportObject(jsonIndicator, null, indicators, jsonIndicator, null);
					// Remove the year because it is not necessary into the JSON map as value and the year is the key.
					jsonIndicator.remove(REFERENCEYEAR_KEY);
					if (jsonIndicator.size() > 0) {
						indicatorMap.set(Integer.toString(indicators.getReferenceYear()), jsonIndicator);
					}
				}
				if (indicatorMap.size() > 0) {
					jsonJournal.set(QUALITYINDICATORSHISTORY_KEY, indicatorMap);
				}

				if (jsonJournal.size() > 0) {
					repository.put(journal, id);
					array.add(jsonJournal);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(JOURNALS_SECTION, array);
			}
		}
	}

	/** Export the publications to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @param similarPublicationProvider a provider of a publication that is similar to a given publication. 
	 *      If this argument is not {@code null} and if it replies a similar publication, the information in this
	 *      similar publication is used to complete the JSON file that is initially filled up with the source publication.
	 * @param extraPublicationProvider this provider gives publications that must be exported into the JSON that are
	 *      not directly extracted from the database. If this argument is {@code null}, no extra publication is exported. 
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportPublications(ObjectNode root, Map<Object, String> repository, SimilarPublicationProvider similarPublicationProvider,
			ExtraPublicationProvider extraPublicationProvider) throws Exception {
		final List<Publication> publications = this.publicationRepository.findAll();
		final ArrayNode array = root.arrayNode();
		int i = 0;
		if (!publications.isEmpty()) {
			for (final Publication publication : publications) {
				final ObjectNode jsonPublication = array.objectNode();
				final String id = exportPublication(i, publication, root, jsonPublication, repository, similarPublicationProvider);
				if (jsonPublication.size() > 0) {
					jsonPublication.set(HIDDEN_INTERNAL_DATA_SOURCE_KEY, jsonPublication.textNode(HIDDEN_INTERNAL_DATABASE_SOURCE_VALUE));
					repository.put(publication, id);
					array.add(jsonPublication);
					++i;
				}
			}
		}
		getLogger().info("Exporting " + array.size() + " publications from the database."); //$NON-NLS-1$ //$NON-NLS-2$
		if (extraPublicationProvider != null) {
			for (final Publication publication : extraPublicationProvider.getPublications()) {
				final ObjectNode jsonPublication = array.objectNode();
				final String id = exportPublication(i, publication, root, jsonPublication, repository, null);
				if (jsonPublication.size() > 0) {
					jsonPublication.set(HIDDEN_INTERNAL_DATA_SOURCE_KEY, jsonPublication.textNode(HIDDEN_INTERNAL_EXTERNAL_SOURCE_VALUE));
					repository.put(publication, id);
					array.add(jsonPublication);
					++i;
				}
			}
			getLogger().info("Exporting " + extraPublicationProvider.getPublications().size() + " extra publications from the BibTeX."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (array.size() > 0) {
			root.set(PUBLICATIONS_SECTION, array);
		}
	}

	/** Export the publications to the given JSON root element.
	 *
	 * @param index the index of the publication in the list of publications.
	 * @param publication the publication to export.
	 * @param root the JSON root.
	 * @param jsonPublication the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @param similarPublicationProvider a provider of a publication that is similar to a given publication. 
	 *      If this argument is not {@code null} and if it replies a similar publication, the information in this
	 *      similar publication is used to complete the JSON file that is initially filled up with the source publication.
	 * @return the identifier of the exported publication.
	 * @throws Exception if there is problem for exporting.
	 */
	protected String exportPublication(int index, Publication publication, ObjectNode root, ObjectNode jsonPublication, 
			Map<Object, String> repository, SimilarPublicationProvider similarPublicationProvider) throws Exception {
		// Add missed information from any similar publication
		final List<Publication> similarPublications;
		if (similarPublicationProvider != null) {
			similarPublications = similarPublicationProvider.get(publication);
			if (similarPublications != null && !similarPublications.isEmpty()) {
				getLogger().info("Found similar publication(s) for: " + publication.getTitle()); //$NON-NLS-1$
			}
		} else {
			similarPublications = Collections.emptyList();
		}

		final String id = PUBLICATION_ID_PREFIX + index;
		exportObject(jsonPublication, id, publication, jsonPublication, similarPublications);

		// Add the database identifier for information
		if (publication.getId() > 0) {
			jsonPublication.set(DATABASE_ID_FIELDNAME, jsonPublication.numberNode(publication.getId()));
		}
		
		// Add the authors by hand because they are not exported implicitly by
		// the "exportObject" function.
		// It is due to the reference to person entities.
		final ArrayNode authorArray = jsonPublication.arrayNode();
		for (final Person author : publication.getAuthors()) {
			String authorId = repository.get(author);
			if (Strings.isNullOrEmpty(authorId)) {
				// Author not found in the repository. It is a behavior that may
				// occur when the authors are provided by a BibTeX source and the
				// person is not yet known.
				authorId = createPerson(root, repository, author);
				if (Strings.isNullOrEmpty(authorId)) {
					// Unexpected behavior. But add the name as text to have it inside the output.
					authorArray.add(author.getFullName());
				} else {
					authorArray.add(createReference(authorId, authorArray));
				}
			} else {
				authorArray.add(createReference(authorId, authorArray));
			}
		}
		if (authorArray.size() > 0) {
			jsonPublication.set(AUTHORS_KEY, authorArray);
		}

		// Add the journal by hand because they are not exported implicitly by
		// the "exportObject" function
		// It is due to the reference to journal entities.
		if (publication instanceof JournalBasedPublication) {
			final JournalBasedPublication jbp = (JournalBasedPublication) publication;
			final Journal journal = jbp.getJournal();
			if (journal != null) {
				final String journalId = repository.get(journal);
				if (Strings.isNullOrEmpty(journalId)) {
					// Journal not found in the repository. It is an unexpected behavior but
					// the name of the journal is output to JSON
					jsonPublication.set(JOURNAL_KEY, jsonPublication.textNode(journal.getJournalName()));
				} else {
					jsonPublication.set(JOURNAL_KEY, createReference(journalId, jsonPublication));
				}
			}
		}

		return id;
	}

	private String createPerson(ObjectNode root, Map<Object, String> repository, Person person) throws Exception {
		final ArrayNode personNode = (ArrayNode) root.get(PERSONS_SECTION);
		if (personNode != null) {
			final ObjectNode jsonPerson = personNode.objectNode();
			final String id = PERSON_ID_PREFIX + personNode.size();
			exportObject(jsonPerson, id, person, jsonPerson, null);
			repository.put(person, id);
			personNode.add(jsonPerson);
			return id;
		}
		return null;
	}

}
