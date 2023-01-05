package fr.ciadlab.labmanager.controller.view.project;

import java.util.List;

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
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.project.ProjectService;

@RestController
@CrossOrigin
public class ProjectViewController extends AbstractViewController {
	
	private ProjectService projectService;
	
	private PersonService personService;
	
	private ResearchOrganizationService researchOrganizationService;
	
	public ProjectViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectService projectService,
			@Autowired PersonService personService,
			@Autowired ResearchOrganizationService researchOrganizationService) {
		super(messages, constants);
		this.projectService = projectService;
		this.personService = personService;
		this.researchOrganizationService = researchOrganizationService;
	}
	
	@GetMapping("/" + Constants.PROJECT_LIST_ENDPOINT)
	public ModelAndView showBackProjectList(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /" + Constants.PROJECT_LIST_ENDPOINT + " by " + username); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		readCredentials(username);
		final ModelAndView modelAndView = new ModelAndView(Constants.PROJECT_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView);
		initAdminTableButtons(modelAndView, endpoint(Constants.PROJECT_EDITING_ENDPOINT, "project")); //$NON-NLS-1$
		modelAndView.addObject("projects", this.projectService.getAllProjects());
		return modelAndView;
	}
	
	@GetMapping(value = "/" + Constants.PROJECT_EDITING_ENDPOINT)
	public ModelAndView showProjectEditor(
			@RequestParam(required = false) Integer project,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /" + Constants.PROJECT_LIST_ENDPOINT + " by " + username); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		readCredentials(username);
		final ModelAndView modelAndView = new ModelAndView("projectEditor");
		initModelViewWithInternalProperties(modelAndView);
		
		final Project projectObj;
		if (project != null && project.intValue() != 0) {
			projectObj = this.projectService.getProjectById(project.intValue());
			if (projectObj == null) {
				throw new IllegalArgumentException("Project not found");
			}
		}
		else {
			projectObj = null;
		}
		List<ResearchOrganization> allOrgas = this.researchOrganizationService.getAllResearchOrganizations();
		
		modelAndView.addObject("project", projectObj);
		modelAndView.addObject("formActionUrl", rooted(Constants.PROJECT_SAVING_ENDPOINT));
		modelAndView.addObject("formRedirectUrl", rooted(Constants.PROJECT_LIST_ENDPOINT));
		modelAndView.addObject("allPersons", this.personService.getAllPersons()); //$NON-NLS-1$
		modelAndView.addObject("allOrgas", allOrgas); //$NON-NLS-1$
		
		return modelAndView;
	}
}
