package fr.ciadlab.labmanager.controller.api.conference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.*;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.conference.ConferenceQualityAnnualIndicators;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.service.conference.ConferenceService;

@RestController
@CrossOrigin
public class ConferenceApiController extends AbstractApiController {
	private ConferenceService conferenceService;

	public ConferenceApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ConferenceService conferenceService) {
		super(messages, constants);
		this.conferenceService = conferenceService;
	}
	
	@GetMapping(value = "/getConferenceData", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Conference getConferenceData(@RequestParam(required = false) String name, @RequestParam(required = false) Integer id) {
		if (id == null && Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Name and identifier parameters are missed"); //$NON-NLS-1$
		}
		if (id != null) {
			return this.conferenceService.getConferenceById(id.intValue());
		}
		return this.conferenceService.getConferenceByName(name);
	}
	
	@GetMapping(value = "/getConferenceQualityIndicators")
	@ResponseBody
	public Map<Integer, ConferenceQualityAnnualIndicators> getConferenceQualityIndicators(
			@RequestParam(required = true) String conference,
			@RequestParam(required = false, name = "year") List<Integer> years) {
		final Conference conferenceObj = getConferenceWith(conference, this.conferenceService);
		if (conferenceObj == null) {
			throw new IllegalArgumentException("Conference not found with: " + conference); //$NON-NLS-1$
		}
		if (years != null && !years.isEmpty()) {
			final Map<Integer, ConferenceQualityAnnualIndicators> indicators = new HashMap<>();
			for (final Integer year : years) {
				if (year != null) {
					indicators.computeIfAbsent(year, it -> {
						return conferenceObj.getQualityIndicatorsFor(year.intValue(), null);
					});
				}
			}
			return indicators;
		}
		return conferenceObj.getQualityIndicators();
	}
	
	
	@PostMapping(value = "/" + Constants.CONFERENCE_SAVING_ENDPOINT)
	public void saveConference(
			@RequestParam(required = false) Integer conference,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String acronym,
			@RequestParam(required = false) String conferenceUrl,
			@RequestParam(required = false) String publisher,
			@RequestParam(required = false) String issn,
			@RequestParam(required = false) String coreIdentifier,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /" + Constants.CONFERENCE_SAVING_ENDPOINT + " by " + username + " for journal " + conference); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ensureCredentials(username);
		final Conference optConference;
		//
		if (conference == null) {
			optConference = this.conferenceService.createConference(name,acronym,conferenceUrl,publisher,issn,coreIdentifier);
		} else {
			optConference = this.conferenceService.updateConference(conference.intValue(),name,acronym,conferenceUrl,publisher,issn,coreIdentifier);
		}
		if (optConference == null) {
			throw new IllegalStateException("Conference not found"); //$NON-NLS-1$
		}
	}
	
	@DeleteMapping("/deleteConference")
	public void deleteConference(
			@RequestParam Integer conference,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /deleteConference by " + username + " for conference " + conference); //$NON-NLS-1$ //$NON-NLS-2$
		ensureCredentials(username);
		if (conference == null || conference.intValue() == 0) {
			throw new IllegalStateException("Conference not found"); //$NON-NLS-1$
		}
		this.conferenceService.removeConference(conference.intValue());
	}
	
	@PostMapping(value = "/" + Constants.SAVE_CONFERENCE_RANKING_ENDPOINT)
	public void saveConferenceRanking(
			@RequestParam(required = true) int conference,
			@RequestParam(required = true) int year,
			@RequestParam(required = false) CoreRanking ranking,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /" + Constants.SAVE_CONFERENCE_RANKING_ENDPOINT  + " by " + username + " for conference " + conference); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ensureCredentials(username);
		final Conference conferenceObj = this.conferenceService.getConferenceById(conference);
		if (conferenceObj == null) {
			throw new IllegalArgumentException("Conference not found with: " + conference); //$NON-NLS-1$
		}
		this.conferenceService.setQualityIndicators(conferenceObj, year, ranking);
		
	}
	
	@DeleteMapping("/" + Constants.DELETE_CONFERENCE_RANKING_ENDPOINT)
	public void deleteJournalRanking(
			@RequestParam(required = true) int conference,
			@RequestParam(required = true) int year,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /" + Constants.DELETE_CONFERENCE_RANKING_ENDPOINT + " by " + username + " for conference " + conference + " and year " + year); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ensureCredentials(username);
		final Conference conferenceObj = this.conferenceService.getConferenceById(conference);
		if (conferenceObj == null) {
			throw new IllegalArgumentException("Journal not found with: " + conference); //$NON-NLS-1$
		}
		this.conferenceService.deleteQualityIndicators(conferenceObj, year);
	}
	
	
	
	
	
	
	

}
