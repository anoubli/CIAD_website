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

package fr.ciadlab.labmanager.controller.api.supervision;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.service.supervision.SupervisionService;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for person supervisions.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */
@RestController
@CrossOrigin
public class SupervisionApiController extends AbstractApiController {

	private SupervisionService supervisionService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param supervisionService the service for managing the person supervisions.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public SupervisionApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired SupervisionService supervisionService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.supervisionService = supervisionService;
	}

	/** Saving information of a person supervision. 
	 *
	 * @param supervision the identifier of the supervision to be updated. If the identifier is not provided, this endpoint is supposed to create
	 *     a person supervision in the database.
	 * @param membership the identifier of the membership that describes the supervised person.
	 * @param supervisors the list of the supervisors. Each supervisor description is a map with the following keys:<ul>
	 *     <li>{@code supervisorId} the identifier of the supervisor.</li>
	 *     <li>{@code supervisorType} the type of the supervisor.</li>
	 *     <li>{@code supervisorPercent} the percentage of implication of the supervisor.</li>
	 *     </ul>
	 * @param abandonment indicates if the works were abandoned by the supervised person.
	 * @param title the title of the works done by the supervised person.
	 * @param fundingScheme the name of the scheme that is used for funding the supervised person.
	 * @param fundingDetails some explanation and details about the funding scheme.
	 * @param defenseDate the date (format {@code YYYY-MM-DD}) of the defense.
	 * @param positionAfterSupervision an description of the becoming of the supervised person.
	 * @param numberOfAterPositions the number of ATER positions that were given to the supervised person.
	 * @param jointPosition indicates if the position of the supervised person is in the context of a joint agreement between institutions (co-tutelle, etc.).
	 * @param entrepreneur indicates if the supervised person has also a position of entrepreneur in parallel. 
	 * @param username the name of the logged-in user.
	 * @throws Exception if it is impossible to save the supervision in the database.
	 */
	@SuppressWarnings("unchecked")
	@PutMapping(value = "/" + Constants.SUPERVISION_SAVING_ENDPOINT)
	public void saveSupervision(
			@RequestParam(required = false) Integer supervision,
			@RequestParam(required = true) int membership,
			@RequestParam(required = true) String supervisors,
			@RequestParam(required = false, defaultValue = "false") boolean abandonment,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String fundingScheme,
			@RequestParam(required = false) String fundingDetails,
			@RequestParam(required = false) String defenseDate,
			@RequestParam(required = false) String positionAfterSupervision,
			@RequestParam(required = false, defaultValue = "0") int numberOfAterPositions,
			@RequestParam(required = false, defaultValue = "false") boolean jointPosition,
			@RequestParam(required = false, defaultValue = "false") boolean entrepreneur,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.SUPERVISION_SAVING_ENDPOINT, Integer.valueOf(membership));
		try {
			final String inTitle = inString(title);
			final FundingScheme fundingSchemeObj = FundingScheme.valueOfCaseInsensitive(inString(fundingScheme));
			final String inFundingDetails = inString(fundingDetails);
			final String inPositionAfterSupervision = inString(positionAfterSupervision);
			final String inDefenseDate = inString(defenseDate);
			final LocalDate defenseDateObj;
			if (inDefenseDate != null) {
				defenseDateObj = LocalDate.parse(inDefenseDate);
			} else {
				defenseDateObj = null;
			}
			//
			final List<Map<String, String>> supervisorsObj;
			try (final ByteArrayInputStream bais = new ByteArrayInputStream(supervisors.getBytes())) {
				final ObjectMapper mapper = new ObjectMapper();
				supervisorsObj = mapper.readValue(bais, List.class);
			} catch (Throwable ex) {
				throw new RuntimeException("Supervisors are invalid"); //$NON-NLS-1$
			}
			if (supervisorsObj == null || supervisorsObj.isEmpty()) {
				throw new RuntimeException("Supervisors are missed"); //$NON-NLS-1$
			}
			//
			if (supervision == null || supervision.intValue() == 0) {
				// Create the supervision
				this.supervisionService.addSupervision(
						membership, supervisorsObj, abandonment, inTitle, fundingSchemeObj,
						inFundingDetails, defenseDateObj, inPositionAfterSupervision,
						numberOfAterPositions, jointPosition, entrepreneur);
			} else {
				// Update the supervision		
				this.supervisionService.updateSupervision(
						supervision.intValue(),
						membership, supervisorsObj, abandonment, inTitle, fundingSchemeObj,
						inFundingDetails, defenseDateObj, inPositionAfterSupervision,
						numberOfAterPositions, jointPosition, entrepreneur);
			}
		} catch (Throwable ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
	}


	/** Delete a supervision from the database.
	 *
	 * @param id the identifier of the supervision.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.SUPERVISION_DELETION_ENDPOINT)
	public void deleteJuryMembership(
			@RequestParam Integer id,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.SUPERVISION_DELETION_ENDPOINT, id);
		if (id == null || id.intValue() == 0) {
			throw new IllegalStateException("Missing the supervision id"); //$NON-NLS-1$
		}
		this.supervisionService.removeSupervision(id.intValue());
	}

}
