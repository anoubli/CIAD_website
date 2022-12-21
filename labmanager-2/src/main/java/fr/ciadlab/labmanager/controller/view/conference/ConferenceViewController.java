package fr.ciadlab.labmanager.controller.view.conference;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
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
	
	
}
