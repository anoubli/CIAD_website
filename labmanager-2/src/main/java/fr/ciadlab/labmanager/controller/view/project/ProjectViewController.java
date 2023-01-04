package fr.ciadlab.labmanager.controller.view.project;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.service.project.ProjectService;

public class ProjectViewController extends AbstractViewController {
	
	private ProjectService projectService;
	
	public ProjectViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectService projectService) {
		super(messages, constants);
		this.projectService = projectService;
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
		
		modelAndView.addObject("project", projectObj);
		modelAndView.addObject("formActionUrl", rooted(Constants.PROJECT_SAVING_ENDPOINT));
		modelAndView.addObject("formRedirectUrl", rooted(Constants.PROJECT_LIST_ENDPOINT));

		return modelAndView;
	}
}
