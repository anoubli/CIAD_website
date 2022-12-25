package fr.ciadlab.labmanager.controller.view.conference;

import java.time.LocalDate;

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
	public ModelAndView showJournalList(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /" + Constants.CONFERENCE_LIST_ENDPOINT + " by " + username); //$NON-NLS-1$ //$NON-NLS-2$
		readCredentials(username);
		final ModelAndView modelAndView = new ModelAndView(Constants.CONFERENCE_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView);
		initAdminTableButtons(modelAndView, endpoint(Constants.CONFERENCE_EDITING_ENDPOINT, "conference")); //$NON-NLS-1$
		modelAndView.addObject("Conferences", this.conferenceService.getAllConferences()); //$NON-NLS-1$
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
//		modelAndView.addObject("scimagoQIndex_imageUrl", JournalService.SCIMAGO_URL_PREFIX + "{0}"); //$NON-NLS-1$ //$NON-NLS-2$
		//
		return modelAndView;
	}
	
	
}
