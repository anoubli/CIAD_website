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

package fr.ciadlab.labmanager.controller.view.publication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.utils.RequiredFieldInForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** REST Controller for publications' views.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RestController
@CrossOrigin
public class PublicationViewController extends AbstractViewController {

	private PublicationService publicationService;

	private PersonService personService;

	private ResearchOrganizationService organizationService;

	private PersonComparator personComparator;

	private DownloadableFileManager fileManager;

	private JournalService journalService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param publicationService the publication service.
	 * @param personService the person service.
	 * @param organizationService the research organization service.
	 * @param personComparator the comparator of persons.
	 * @param fileManager the manager of local files.
	 * @param journalService the tools for manipulating journals.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public PublicationViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PublicationService publicationService,
			@Autowired PersonService personService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired PersonComparator personComparator,
			@Autowired DownloadableFileManager fileManager,
			@Autowired JournalService journalService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.publicationService = publicationService;
		this.personService = personService;
		this.organizationService = organizationService;
		this.personComparator = personComparator;
		this.fileManager = fileManager;
		this.journalService = journalService;
	}

	/** Replies the model-view component for managing the publications.
	 * This endpoint is designed for the database management.
	 *
	 * @param journal the identifier of the journal for which the publications must be displayed.
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 * @see #showFrontPublicationList(Integer, Integer, Integer, Boolean)
	 */
	@GetMapping("/" + Constants.PUBLICATION_LIST_ENDPOINT)
	public ModelAndView showBackPublicationList(
			@RequestParam(required = false) Integer journal,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, Constants.PUBLICATION_LIST_ENDPOINT, journal);
		final ModelAndView modelAndView = new ModelAndView(Constants.PUBLICATION_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		initAdminTableButtons(modelAndView, endpoint(Constants.PUBLICATION_EDITING_ENDPOINT, "publication")); //$NON-NLS-1$
		Collection<? extends Publication> pubs = null;
		if (journal != null) {
			final Journal journalObj = this.journalService.getJournalById(journal.intValue());
			if (journalObj != null) {
				pubs = journalObj.getPublishedPapers();
			}
		}
		if (pubs == null) {
			pubs = this.publicationService.getAllPublications();
		}
		modelAndView.addObject("publications", pubs); //$NON-NLS-1$
		return modelAndView;
	}

	/** Replies the list of publications for the given author.
	 * This function differs to {@link #showBackPublicationList()} because it is dedicated to
	 * the public front-end of the research organization. The function {@link #showBackPublicationList()}
	 * is more dedicated to the administration of the data-set.
	 * <p> This function may provide to the front-end the map of the person identifiers to
	 * their full names. The type of the map is: {@code Map&lt;Integer, String&gt;}.
	 *
	 * @param dbId the database identifier of the author for who the publications must be exported.
	 * @param webId the webpage identifier of the author for who the publications must be exported.
	 * @param organization the identifier of the organization for which the publications must be exported.
	 * @param organizationAcronym the acronym of the organization for which the publications must be exported.
	 * @param journal the identifier of the journal for which the publications must be exported.
	 * @param enableExports indicates if the "exports" box should be visible.
	 * @param enableSearch indicates if the "Search" box should be visible.
	 * @param enableSortChanges indicates if the "Sort Control" box should be visible.
	 * @param enableFilters indicates if the "Filters" box should be visible.
	 * @param enableYearFilter indicates if the filter dedicated to years is enabled.
	 * @param enableTypeFilter indicates if the filter dedicated to types/categories is enabled.
	 * @param enableAuthorFilter indicates if the filter dedicated to authors is enabled.
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 * @param username the name of the logged-in user.
	 * @return the model-view of the list of publications.
	 * @see #showBackPublicationList()
	 * @see #exportJson(HttpServletResponse, List, Integer, Integer, Integer)
	 */
	@GetMapping("/showPublications")
	public ModelAndView showFrontPublicationList(
			@RequestParam(required = false, name = Constants.DBID_ENDPOINT_PARAMETER) Integer dbId,
			@RequestParam(required = false, name = Constants.WEBID_ENDPOINT_PARAMETER) String webId,
			@RequestParam(required = false, name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@RequestParam(required = false) String organizationAcronym,
			@RequestParam(required = false, name = Constants.JOURNAL_ENDPOINT_PARAMETER) Integer journal,
			@RequestParam(required = false, defaultValue = "true") boolean enableExports,
			@RequestParam(required = false, defaultValue = "true") boolean enableSearch,
			@RequestParam(required = false, defaultValue = "true") boolean enableSortChanges,
			@RequestParam(required = false, defaultValue = "true") boolean enableFilters,
			@RequestParam(required = false, defaultValue = "true") boolean enableYearFilter,
			@RequestParam(required = false, defaultValue = "true") boolean enableTypeFilter,
			@RequestParam(required = false, defaultValue = "true") boolean enableAuthorFilter,
			@RequestParam(required = false, defaultValue = "false") boolean embedded,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		final String inWebId = inString(webId);
		final String inOrganizationAcronym = inString(organizationAcronym);
		readCredentials(username, "showPublications", dbId, inWebId, organization, inOrganizationAcronym, journal); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("showPublications"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, embedded);
		//
		final Integer organizationIdObj;
		if (organization != null && organization.intValue() != 0) {
			organizationIdObj = Integer.valueOf(organization.intValue());
		} else if (!Strings.isNullOrEmpty(inOrganizationAcronym)) {
			final ResearchOrganization org = getOrganizationWith(inOrganizationAcronym, this.organizationService);
			if (org != null) {
				organizationIdObj = Integer.valueOf(org.getId());
			} else {
				organizationIdObj = null;
			}
		} else {
			organizationIdObj = null;
		}
		//
		addUrlToPublicationListEndPoint(modelAndView, dbId, inWebId, organizationIdObj, journal);
		//
		modelAndView.addObject("enableFilters", Boolean.valueOf(enableFilters)); //$NON-NLS-1$
		modelAndView.addObject("enableYearFilter", Boolean.valueOf(enableYearFilter)); //$NON-NLS-1$
		modelAndView.addObject("enableTypeFilter", Boolean.valueOf(enableTypeFilter)); //$NON-NLS-1$
		modelAndView.addObject("enableAuthorFilter", Boolean.valueOf(enableAuthorFilter)); //$NON-NLS-1$
		if (enableFilters && enableAuthorFilter) {
			final List<Person> persons = this.personService.getAllPersons();
			final Map<Integer, String> personMap = persons.stream()
					.collect(Collectors.toMap(
							it -> Integer.valueOf(it.getId()),
							it -> it.getFullNameWithLastNameFirst()));
			modelAndView.addObject("authorsMap", personMap); //$NON-NLS-1$
		}
		//
		modelAndView.addObject("enableExports", Boolean.valueOf(enableExports)); //$NON-NLS-1$
		if (enableExports) {
			final UriBuilderFactory factory = new DefaultUriBuilderFactory();
			modelAndView.addObject("endpoint_export_bibtex", //$NON-NLS-1$
					buildUri(factory, dbId, inWebId, organizationIdObj, journal, Constants.EXPORT_BIBTEX_ENDPOINT));
			modelAndView.addObject("endpoint_export_odt", //$NON-NLS-1$
					buildUri(factory, dbId, inWebId, organizationIdObj, journal, Constants.EXPORT_ODT_ENDPOINT));
			modelAndView.addObject("endpoint_export_html", //$NON-NLS-1$
					buildUri(factory, dbId, inWebId, organizationIdObj, journal, Constants.EXPORT_HTML_ENDPOINT));
		}
		//
		modelAndView.addObject("enableSearch", Boolean.valueOf(enableSearch)); //$NON-NLS-1$
		modelAndView.addObject("enableSortChanges", Boolean.valueOf(enableSortChanges)); //$NON-NLS-1$
		if (isLoggedIn()) {
			modelAndView.addObject("editionUrl", endpoint(Constants.PUBLICATION_EDITING_ENDPOINT, //$NON-NLS-1$
					Constants.PUBLICATION_ENDPOINT_PARAMETER));
			modelAndView.addObject("bibtexImportUrl", endpoint(Constants.IMPORT_BIBTEX_VIEW_ENDPOINT)); //$NON-NLS-1$
			modelAndView.addObject("additionUrl", endpoint(Constants.PUBLICATION_EDITING_ENDPOINT)); //$NON-NLS-1$
		}
		return modelAndView;
	}

	private String buildUri(UriBuilderFactory factory, Integer dbId, String webId, Integer organization,
			Integer journal, String endpoint) {
		UriBuilder uriBuilder = factory.builder();
		uriBuilder = uriBuilder.path(rooted(endpoint));
		if (organization != null) {
			uriBuilder = uriBuilder.queryParam(Constants.ORGANIZATION_ENDPOINT_PARAMETER, organization);
		}
		if (dbId != null && dbId.intValue() != 0) {
			uriBuilder = uriBuilder.queryParam(Constants.DBID_ENDPOINT_PARAMETER, dbId);
		} else if (!Strings.isNullOrEmpty(webId)) {
			uriBuilder = uriBuilder.queryParam(Constants.WEBID_ENDPOINT_PARAMETER, webId);
		}
		if (journal != null) {
			uriBuilder = uriBuilder.queryParam(Constants.JOURNAL_ENDPOINT_PARAMETER, journal);
		}
		return uriBuilder.build().toString();
	}

	/** Replies the statistics for the publications and for the author with the given identifier.
	 *
	 * @param dbId the database identifier of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param webId the identifier of the webpage of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param name the name of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param annual indicates if the stats for each year are provided. Default is {@code true}.
	 * @param global indicates if the global stats are provided. Default is {@code true}.
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 * @param username the name of the logged-in user.
	 * @return the model-view with the statistics.
	 */
	@GetMapping("/showPublicationStats")
	public ModelAndView showPublicationsStats(
			@RequestParam(required = false) Integer dbId,
			@RequestParam(required = false) String webId,
			@RequestParam(required = false, defaultValue = "true") boolean annual,
			@RequestParam(required = false, defaultValue = "true") boolean global,
			@RequestParam(required = false, defaultValue = "true") boolean embedded,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, "showPublicationStats", dbId, webId); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("showPublicationStats"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, embedded);

		final List<Publication> publications;
		if (dbId != null && dbId.intValue() != 0) {
			publications = this.publicationService.getPublicationsByPersonId(dbId.intValue());
		} else if (!Strings.isNullOrEmpty(webId)) {
			publications = this.publicationService.getPublicationsByPersonWebPageId(webId);
		} else {
			publications = this.publicationService.getAllPublications();
		}

		final Map<Integer, PublicationsStat> statsPerYear = new TreeMap<>();
		final PublicationsStat globalStats = new PublicationsStat(Integer.MIN_VALUE);

		for (final Publication p : publications) {
			final Integer y = Integer.valueOf(p.getPublicationYear());
			if (annual) {
				final PublicationsStat stats = statsPerYear.computeIfAbsent(y,
						it -> new PublicationsStat(it.intValue()));
				stats.increment(p.getType(), p.isRanked(), 1);
			}
			if (global) {
				globalStats.increment(p.getType(), p.isRanked(), 1);
			}
		}

		modelAndView.addObject("stats", statsPerYear); //$NON-NLS-1$
		if (global) {
			modelAndView.addObject("globalStats", globalStats); //$NON-NLS-1$
		}
		return modelAndView;
	}

	/** Show the editor for a publication. This editor permits to create or to edit apublication.
	 *
	 * @param publication the identifier of the publication to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of a publication.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 * @throws IOException if there is some internal IO error when building the form's data.
	 */
	@GetMapping(value = "/" + Constants.PUBLICATION_EDITING_ENDPOINT)
	public ModelAndView showPublicationEditor(
			@RequestParam(required = false, name = Constants.PUBLICATION_ENDPOINT_PARAMETER) Integer publication,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws IOException {
		ensureCredentials(username, Constants.PUBLICATION_EDITING_ENDPOINT, publication);
		final ModelAndView modelAndView = new ModelAndView("publicationEditor"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Publication publicationObj;
		if (publication != null && publication.intValue() != 0) {
			publicationObj = this.publicationService.getPublicationById(publication.intValue());
			if (publicationObj == null) {
				throw new IllegalArgumentException("Publication not found: " + publication); //$NON-NLS-1$
			}
		} else {
			publicationObj = null;
		}
		//
		if (publicationObj != null) {
			// Provide the attributes of the publication
			publicationObj.forEachAttribute((attrName, attrValue) -> {
				// Specific treatment of fields that are considered as shared among multiple publication types
				if ("reportNumber".equals(attrName) || "patentNumber".equals(attrName) || "documentNumber".equals(attrName)) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					modelAndView.addObject("shared_number", attrValue); //$NON-NLS-1$
				} else if ("reportType".equals(attrName) || "patentType".equals(attrName)) {  //$NON-NLS-1$//$NON-NLS-2$
					modelAndView.addObject("shared_documentType", attrValue); //$NON-NLS-1$
				} else {
					modelAndView.addObject("shared_" + attrName, attrValue); //$NON-NLS-1$
				}
			});
		}

		// Create the mapping from type name to input field
		final Map<String, String> requiredFields = new TreeMap<>();
		final Map<String, Set<String>> typeFieldMapping = new HashMap<>();
		for (final PublicationType ptype : PublicationType.values()) {
			final Class<?> ctype = ptype.getInstanceType();
			final String type = ctype.getSimpleName();
			typeFieldMapping.computeIfAbsent(type, it -> {
				return buildHtmlElementMapping(ctype, requiredFields);
			});
		}
		modelAndView.addObject("typeFieldMapping", typeFieldMapping); //$NON-NLS-1$
		modelAndView.addObject("requiredFields", requiredFields); //$NON-NLS-1$

		// Special injection of attributes
		if (publicationObj != null) {
			// Provide more information about uploaded files
			final Object pdfPath = modelAndView.getModel().get("shared_pathToDownloadablePDF"); //$NON-NLS-1$
			if (pdfPath != null && !Strings.isNullOrEmpty(pdfPath.toString())) {
				modelAndView.addObject("pathToDownloadablePDF_basename", FileSystem.largeBasename(pdfPath.toString())); //$NON-NLS-1$
				modelAndView.addObject("pathToDownloadablePDF_picture", //$NON-NLS-1$
						rooted(this.fileManager.makePdfPictureFilename(publicationObj.getId())));
			}

			final Object awardPath = modelAndView.getModel().get("shared_pathToDownloadableAwardCertificate"); //$NON-NLS-1$
			if (awardPath != null && !Strings.isNullOrEmpty(awardPath.toString())) {
				modelAndView.addObject("pathToDownloadableAwardCertificate_basename", FileSystem.largeBasename(awardPath.toString())); //$NON-NLS-1$
				modelAndView.addObject("pathToDownloadableAwardCertificate_picture", //$NON-NLS-1$
						rooted(this.fileManager.makeAwardPictureFilename(publicationObj.getId())));
			}

			// Provide a YEAR-MONTH publication date
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM"); //$NON-NLS-1$
			if (publicationObj.getPublicationDate() != null) {
				modelAndView.addObject("dateYearMonth_enableMonth", Boolean.TRUE); //$NON-NLS-1$
				modelAndView.addObject("dateYearMonth", publicationObj.getPublicationDate().format(formatter)); //$NON-NLS-1$
			} else {
				modelAndView.addObject("dateYearMonth_enableMonth", Boolean.FALSE); //$NON-NLS-1$
				modelAndView.addObject("dateYearMonth",  //$NON-NLS-1$
						Integer.toString(publicationObj.getPublicationYear())
						+ "-12"); //$NON-NLS-1$
			}
			final LocalDate maxDate = LocalDate.now().plus(1, ChronoUnit.YEARS);
			modelAndView.addObject("maxDateYearMonth", maxDate.format(formatter)); //$NON-NLS-1$
			if (publicationObj instanceof JournalBasedPublication) {
				final JournalBasedPublication jbp = (JournalBasedPublication) publicationObj;
				final Journal journal = jbp.getJournal();
				if (journal != null) {
					modelAndView.addObject("journalIdentifier", Integer.valueOf(journal.getId())); //$NON-NLS-1$
				}
			}
		}

		// List of all the authors
		modelAndView.addObject("allPersons", this.personService.getAllPersons().stream().sorted(this.personComparator).iterator()); //$NON-NLS-1$

		// Provide the list of journals
		modelAndView.addObject("publication", publicationObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.PUBLICATION_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.PUBLICATION_LIST_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("journals", this.journalService.getAllJournals()); //$NON-NLS-1$
		modelAndView.addObject("defaultPublicationType", PublicationType.INTERNATIONAL_JOURNAL_PAPER); //$NON-NLS-1$
		//
		return modelAndView;
	}

	private static Set<String> buildHtmlElementMapping(Class<?> jtype, Map<String, String> required) {
		final Set<String> elements = new TreeSet<>();
		final boolean isJournalType = JournalBasedPublication.class.isAssignableFrom(jtype);
		if (isJournalType) {
			elements.add("dynamic-form-group-journal"); //$NON-NLS-1$
		} else {
			elements.add("dynamic-form-group-isbn"); //$NON-NLS-1$
			elements.add("dynamic-form-group-issn"); //$NON-NLS-1$
		}
		for (final Method method : jtype.getDeclaredMethods()) {
			final String bname = method.getName();
			if (bname.startsWith("get") && method.getParameterCount() == 0 && String.class.equals(method.getReturnType())) { //$NON-NLS-1$
				try {
					String attrName = bname.substring(3);
					final String setterName = "set" + attrName; //$NON-NLS-1$
					jtype.getDeclaredMethod(setterName, String.class);
					final String fieldName = StringUtils.uncapitalize(attrName);
					if (fieldName.endsWith("Number") && !"chapterNumber".equals(fieldName)) { //$NON-NLS-1$ //$NON-NLS-2$
						attrName = "number"; //$NON-NLS-1$
					} else if (!fieldName.equals("type") && fieldName.endsWith("Type")) { //$NON-NLS-1$ //$NON-NLS-2$
						attrName = "documentType"; //$NON-NLS-1$
					} else if ("ISBN".equals(attrName) || "ISSN".equals(attrName)) { //$NON-NLS-1$ //$NON-NLS-2$
						attrName = null;
					} else if ("DOI".equals(attrName)) { //$NON-NLS-1$
						attrName = fieldName.toLowerCase();
					} else {
						attrName = fieldName;
					}
					if (attrName != null) {
						final String htmlElement = "dynamic-form-group-" + attrName; //$NON-NLS-1$
						elements.add(htmlElement);
						if (method.isAnnotationPresent(RequiredFieldInForm.class)) {
							required.put(htmlElement, fieldName);
						}
					}
				} catch (Throwable ex) {
					//
				}
			}
		}
		return elements;
	}

	/** Show the view for importing BibTeX files.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 * @throws IOException if there is some internal IO error when building the form's data.
	 */
	@GetMapping(value = "/" + Constants.IMPORT_BIBTEX_VIEW_ENDPOINT)
	public ModelAndView showBibTeXImporter(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws IOException {
		ensureCredentials(username, Constants.IMPORT_BIBTEX_VIEW_ENDPOINT);
		final ModelAndView modelAndView = new ModelAndView("importBibTeX"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		modelAndView.addObject("bibtexJsonActionUrl", endpoint(Constants.GET_JSON_FROM_BIBTEX_ENDPOINT, //$NON-NLS-1$
				Constants.CHECKINDB_ENDPOINT_PARAMETER, Boolean.TRUE));
		modelAndView.addObject("formActionUrl", rooted(Constants.SAVE_BIBTEX_ENDPOINT)); //$NON-NLS-1$
		//
		return modelAndView;
	}

	/** Add the URL to model that permits to retrieve the publication list.
	 *
	 * @param modelAndView the model-view to configure for redirection.
	 * @param dbId the database identifier of the author for who the publications must be exported.
	 * @param webId the webpage identifier of the author for who the publications must be exported.
	 * @param organization the identifier of the organization for which the publications must be exported.
	 * @param journal the identifier of the journal for which the publications must be exported.
	 */
	protected void addUrlToPublicationListEndPoint(ModelAndView modelAndView, Integer dbId, String webId,
			Integer organization, Integer journal) {
		final StringBuilder path = new StringBuilder();
		path.append("/").append(getApplicationConstants().getServerName()).append("/").append(Constants.EXPORT_JSON_ENDPOINT); //$NON-NLS-1$ //$NON-NLS-2$
		UriBuilder uriBuilder = this.uriBuilderFactory.builder();
		uriBuilder = uriBuilder.path(path.toString());
		uriBuilder = uriBuilder.queryParam(Constants.FORAJAX_ENDPOINT_PARAMETER, Boolean.TRUE);
		if (organization != null && organization.intValue() != 0) {
			uriBuilder = uriBuilder.queryParam(Constants.ORGANIZATION_ENDPOINT_PARAMETER, organization);
		}
		if (dbId != null && dbId.intValue() != 0) {
			uriBuilder = uriBuilder.queryParam(Constants.DBID_ENDPOINT_PARAMETER, dbId);
		} else if (!Strings.isNullOrEmpty(webId)) {
			uriBuilder = uriBuilder.queryParam(Constants.WEBID_ENDPOINT_PARAMETER, webId);
		}
		if (journal != null && journal.intValue() != 0) {
			uriBuilder = uriBuilder.queryParam(Constants.JOURNAL_ENDPOINT_PARAMETER, journal);
		}
		final String url = uriBuilder.build().toString();
		modelAndView.addObject("url", url); //$NON-NLS-1$
	}

	/** Regenerate the picture files and show the list on a view.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/regenerateThumbnail")
	public ModelAndView regenerateThumbnail(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, "regenerateThumbnail"); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("regenerateThumbnail"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		modelAndView.addObject("batchUrl", endpoint(Constants.REGENERATE_THUMBNAIL_ASYNC_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("terminationUrl", endpoint(Constants.ADMIN_ENDPOINT)); //$NON-NLS-1$
		//
		return modelAndView;
	}
}
