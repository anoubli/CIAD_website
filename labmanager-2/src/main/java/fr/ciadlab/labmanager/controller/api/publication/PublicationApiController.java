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

package fr.ciadlab.labmanager.controller.api.publication;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonString;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

/** REST Controller for publications.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RestController
@CrossOrigin
public class PublicationApiController extends AbstractApiController {

	private static final int THUMBNAIL_SERVICE_TIMEOUT = 1200000;

	private static final int THUMBNAIL_STEP_1 = 1;

	private static final int THUMBNAIL_STEP_2 = 2;

	private static final String PERCENT_100_STR = "100"; //$NON-NLS-1$

	private static final int PERCENT_100_NUM = 100;

	private PublicationService publicationService;

	private PersonService personService;

	private DownloadableFileManager fileManager;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param publicationService the publication service.
	 * @param personService the person service.
	 * @param fileManager the manager of local files.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public PublicationApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PublicationService publicationService,
			@Autowired PersonService personService,
			@Autowired DownloadableFileManager fileManager,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.publicationService = publicationService;
		this.personService = personService;
		this.fileManager = fileManager;
	}

	/** Replies data about a specific publication from the database.
	 * This endpoint accepts one of the two parameters: the title or the identifier of the publication.
	 * <p>This function and {@link #exportJson(HttpServletResponse, List, Integer, Integer, Integer)} differ
	 * from the structure of the JSON output. This function is a direct translation of the publication to
	 * JSON.
	 *
	 * @param title the title of the publication.
	 * @param id the identifier of the publication.
	 * @return the publication if a specific identifier is provided, or a list of publications that have the given title.
	 * @see #exportJson(HttpServletResponse, List, Integer, Integer, Integer)
	 */
	@GetMapping(value = "/getPublicationData", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Object getPublicationData(@RequestParam(required = false) String title, @RequestParam(required = false) Integer id) {
		final String inTitle = inString(title);
		if (id == null && inTitle == null) {
			throw new IllegalArgumentException("Title and identifier parameters are missed"); //$NON-NLS-1$
		}
		if (id != null) {
			return this.publicationService.getPublicationById(id.intValue());
		}
		return this.publicationService.getPublicationsByTitle(inTitle);
	}

	/** Saving information of a publication. 
	 *
	 * @param publication the identifier of the publication. If the identifier is not provided, this endpoint is supposed to create
	 *     a publication in the database.
	 * @param validated indicates if the publication is validated by a local authority.
	 * @param pathToDownloadablePDF the uploaded PDF file for the publication.
	 * @param pathToDownloadableAwardCertificate the uploaded Award certificate for the publication.
	 * @param authors the list of authors. It is a list of database identifiers (for known persons) and full name
	 *     (for unknown persons).
	 * @param allParameters the map of all the request string-based parameters.
	 * @param username the name of the logged-in user.
	 * @throws Exception if the publication cannot be saved.
	 */
	@PutMapping(value = "/" + Constants.PUBLICATION_SAVING_ENDPOINT)
	public void savePublication(
			@RequestParam(required = false) Integer publication,
			@RequestParam(required = false, defaultValue = "false") boolean validated,
			@RequestParam(required = false) List<String> authors,
			@RequestParam(required = false) MultipartFile pathToDownloadablePDF,
			@RequestParam(required = false) MultipartFile pathToDownloadableAwardCertificate,
			@RequestParam Map<String, String> allParameters,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.PUBLICATION_SAVING_ENDPOINT, publication);
		final List<String> inAuthors = authors.stream().map(it -> inString(it)).filter(it -> it != null).collect(Collectors.toList());
		// First check if the authors follows the constraints
		if (!this.personService.containsAMember(inAuthors, true)) {
			throw new IllegalArgumentException("The list of authors does not contains a member of one of the known research organizations."); //$NON-NLS-1$
		}

		int uploadedPdfFile = 0;
		int uploadedAwardFile = 0;
		Optional<Publication> optPublication = Optional.empty();
		try {
			// The "type" parameter format is more complex than a simple enumeration constant.
			// It it the PublicationType constant followed by the category label.
			// We must reformat the type value to have only a enumeration constant.
			String typeValue = ensureString(allParameters, "type"); //$NON-NLS-1$
			typeValue = inString(StringUtils.substringBefore(typeValue, "/")); //$NON-NLS-1$
			allParameters.put("type", typeValue); //$NON-NLS-1$
			//
			if (publication == null) {
				optPublication = this.publicationService.createPublicationFromMap(validated, allParameters,
						inAuthors, pathToDownloadablePDF, pathToDownloadableAwardCertificate);
			} else {
				optPublication = this.publicationService.updatePublicationFromMap(publication.intValue(), validated, allParameters,
						inAuthors, pathToDownloadablePDF, pathToDownloadableAwardCertificate);
			}
			if (optPublication.isEmpty()) {
				throw new IllegalStateException("Publication not found"); //$NON-NLS-1$
			}
		} catch (Throwable ex) {
			// Delete created publication
			if (optPublication.isPresent()) {
				try {
					this.publicationService.removePublication(optPublication.get().getId(), true);
				} catch (Throwable ex0) {
					// Silent
				}
			}
			// Delete any uploaded file
			if (uploadedPdfFile != 0) {
				try {
					this.fileManager.deleteDownloadablePublicationPdfFile(uploadedPdfFile);
				} catch (Throwable ex0) {
					// Silent
				}
			}
			if (uploadedAwardFile != 0) {
				try {
					this.fileManager.deleteDownloadableAwardPdfFile(uploadedAwardFile);
				} catch (Throwable ex0) {
					// Silent
				}
			}
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
	}

	/** Delete a publication from the database.
	 *
	 * @param publication the identifier of the publication.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/deletePublication")
	public void deletePublication(
			@RequestParam Integer publication,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, "deletePublication", publication); //$NON-NLS-1$
		if (publication == null || publication.intValue() == 0) {
			throw new IllegalStateException("Publication not found"); //$NON-NLS-1$
		}
		this.publicationService.removePublication(publication.intValue(), true);
	}

	/** Regenerate the thumbnail files asynchronously.
	 *
	 * @param username the name of the logged-in user.
	 * @return the asynchronous response.
	 */
	@GetMapping(value = "/" + Constants.REGENERATE_THUMBNAIL_ASYNC_ENDPOINT)
	public SseEmitter regenerateThumbnailAsync(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, Constants.REGENERATE_THUMBNAIL_ASYNC_ENDPOINT);
		final ExecutorService service = Executors.newSingleThreadExecutor();
		final SseEmitter emitter = new SseEmitter(Long.valueOf(THUMBNAIL_SERVICE_TIMEOUT));
		service.execute(() -> {
			try {
				final SizedIterator<File> thumbnail = this.fileManager.getThumbailFiles();
				final SizedIterator<File> uploadedFiles = this.fileManager.getUploadedPdfFiles();
				while (thumbnail.hasNext()) {
					final int percent = (thumbnail.index() * PERCENT_100_NUM) / thumbnail.totalSize();
					sendProgressStep(emitter, THUMBNAIL_STEP_1, percent, getMessage("publicationApiController.DeletingThumbnails", Integer.valueOf(percent))); //$NON-NLS-1$
					final File thumbnailFile = thumbnail.next();
					FileSystem.delete(thumbnailFile);
				}
				sendProgressStep(emitter, THUMBNAIL_STEP_1, PERCENT_100_NUM, getMessage("publicationApiController.DeletingThumbnails", PERCENT_100_STR)); //$NON-NLS-1$
				while (uploadedFiles.hasNext()) {
					final int percent = (uploadedFiles.index() * PERCENT_100_NUM) / uploadedFiles.totalSize();
					sendProgressStep(emitter, THUMBNAIL_STEP_2, percent, getMessage("publicationApiController.GeneratingThumbnails", Integer.valueOf(percent))); //$NON-NLS-1$
					final File uploadedFile = uploadedFiles.next();
					this.fileManager.regenerateThumbnail(uploadedFile);
				}
				sendProgressStep(emitter, THUMBNAIL_STEP_2, PERCENT_100_NUM, getMessage("publicationApiController.GeneratingThumbnails", PERCENT_100_STR)); //$NON-NLS-1$
				emitter.complete();
			} catch (ClientAbortException ex) {
				// Do not log a message because the connection was closed by the client.
				emitter.completeWithError(ex);
				return;
			} catch (Throwable ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
				emitter.completeWithError(ex);
				return;
			}
		});
		return emitter;
	}

	private static void sendProgressStep(SseEmitter emitter, int step, int percent, String message) throws IOException {
		final JsonString jstring = Json.createValue(Strings.nullToEmpty(message));
		final StringBuilder json = new StringBuilder();
		json.append("{\"step\":").append(step) //$NON-NLS-1$
		.append(",\"terminated\":").append(Boolean.toString(step == THUMBNAIL_STEP_2 && percent >= PERCENT_100_NUM)) //$NON-NLS-1$
		.append(",\"percent\":").append(percent) //$NON-NLS-1$
		.append(",\"message\":").append(jstring.toString()).append("}"); //$NON-NLS-1$ //$NON-NLS-2$
		final SseEventBuilder event = SseEmitter.event().data(json.toString());
		emitter.send(event);
	}

	/** Delete a collection of publications from the database.
	 *
	 * @param publications the identifiers of the publications to be deleted.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 * @since 2.4
	 */
	@DeleteMapping("/deletePublications")
	public void deletePublications(
			@RequestParam(name = "id") List<Integer> publications,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		if (publications == null) {
			throw new IllegalStateException("Publication not found"); //$NON-NLS-1$
		}
		ensureCredentials(username, "deletePublications", publications.toString()); //$NON-NLS-1$
		if (publications.isEmpty()) {
			throw new IllegalStateException("Publication not found"); //$NON-NLS-1$
		}
		this.publicationService.removePublications(publications, true);
	}

}
