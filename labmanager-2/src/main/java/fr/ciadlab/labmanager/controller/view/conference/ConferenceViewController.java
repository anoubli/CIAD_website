package fr.ciadlab.labmanager.controller.view.conference;

import java.time.LocalDate;
import java.util.*;

import fr.ciadlab.labmanager.entities.conference.ConferenceQualityAnnualIndicators;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.service.conference.ConferenceService;
import fr.ciadlab.labmanager.service.journal.JournalService;

@RestController
@CrossOrigin
public class ConferenceViewController extends AbstractViewController{
	
	private ConferenceService conferenceService;

	public ConferenceViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ConferenceService conferenceService) {
		super(messages, constants);
		this.conferenceService = conferenceService;
	}
	
	@GetMapping("/" + Constants.CONFERENCE_LIST_ENDPOINT)
	public ModelAndView showConferenceList(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /" + Constants.CONFERENCE_LIST_ENDPOINT + " by " + username); //$NON-NLS-1$ //$NON-NLS-2$
		readCredentials(username);
		final ModelAndView modelAndView = new ModelAndView(Constants.CONFERENCE_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView);
		initAdminTableButtons(modelAndView, endpoint(Constants.CONFERENCE_EDITING_ENDPOINT, "conference")); //$NON-NLS-1$
		modelAndView.addObject("conferences", this.conferenceService.getAllConferences()); //$NON-NLS-1$
		modelAndView.addObject("currentYear", Integer.valueOf(LocalDate.now().getYear())); //$NON-NLS-1$
		return modelAndView;
	}
	
	/** Show the editor for a conference. This editor permits to create or to edit a conference.
	 *
	 * @param conference the identifier of the journal to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of a conference.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/" + Constants.CONFERENCE_EDITING_ENDPOINT)
	public ModelAndView showConferenceEditor(
			@RequestParam(required = false) Integer conference,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /" + Constants.CONFERENCE_EDITING_ENDPOINT + " by " + username + " for conference " + conference); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ensureCredentials(username);
		final ModelAndView modelAndView = new ModelAndView("conferenceEditor"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView);
		//
		final Conference conferenceObj;
		if (conference != null && conference.intValue() != 0) {
			conferenceObj = this.conferenceService.getConferenceById(conference.intValue());
			if (conferenceObj == null) {
				throw new IllegalArgumentException("Conference not found: " + conference); //$NON-NLS-1$
			}
		} else {
			conferenceObj = null;
		}
		//
		modelAndView.addObject("conference", conferenceObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.CONFERENCE_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.CONFERENCE_LIST_ENDPOINT)); //$NON-NLS-1$
		return modelAndView;
	}

	@GetMapping(value = "/conferenceRankingEditor")
	public ModelAndView showConferenceEditor(
			@RequestParam(required = true) int id,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /conferenceRankingEditor by " + username + " for conference " + id); //$NON-NLS-1$ //$NON-NLS-2$
		ensureCredentials(username);
		final ModelAndView modelAndView = new ModelAndView("conferenceRankingEditor"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView);
		//
		final Conference conference = this.conferenceService.getConferenceById(id);
		if (conference == null) {
			throw new IllegalArgumentException("Conference not found: " + id); //$NON-NLS-1$
		}
		//
		modelAndView.addObject("savingUrl", endpoint(Constants.SAVE_CONFERENCE_RANKING_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("deletionUrl", endpoint(Constants.DELETE_CONFERENCE_RANKING_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("conference", conference); //$NON-NLS-1$
		final Map<Integer, ConferenceQualityAnnualIndicators> sortedMap = new TreeMap<>((a, b) -> {
			if (a == b) {
				return 0;
			}
			if (a == null) {
				return 1;
			}
			if (b == null) {
				return -1;
			}
			return Integer.compare(b.intValue(), a.intValue());
		});
		sortedMap.putAll(conference.getQualityIndicators());
		modelAndView.addObject("qualityIndicators", sortedMap); //$NON-NLS-1$
		//
		Integer year = null;
		CoreRanking ranking = null;
		final Iterator<ConferenceQualityAnnualIndicators> iterator = sortedMap.values().iterator();
		while (iterator.hasNext() && ( ranking == null)) {
			final ConferenceQualityAnnualIndicators indicators = iterator.next();
			if (year == null) {
				year = Integer.valueOf(indicators.getReferenceYear());
			}
			if (ranking == null && indicators.getRanking() != null) {
				ranking = indicators.getRanking();
			}
		}
		final int currentYear = LocalDate.now().getYear();
		modelAndView.addObject("currentYear", Integer.valueOf(currentYear)); //$NON-NLS-1$
		modelAndView.addObject("lastReferenceYear", year); //$NON-NLS-1$
		modelAndView.addObject("lastRanking", ranking); //$NON-NLS-1$
		final Set<Integer> years = new TreeSet<>((a, b) -> {
			if (a == b) {
				return 0;
			}
			if (a == null) {
				return 1;
			}
			if (b == null) {
				return -1;
			}
			return Integer.compare(b.intValue(), a.intValue());
		});
		for (int y = currentYear - 20; y <= currentYear; ++y) {
			if (!sortedMap.containsKey(Integer.valueOf(y))) {
				years.add(Integer.valueOf(y));
			}
		}
		modelAndView.addObject("years", years); //$NON-NLS-1$
		//
		return modelAndView;
	}
	
	
}
