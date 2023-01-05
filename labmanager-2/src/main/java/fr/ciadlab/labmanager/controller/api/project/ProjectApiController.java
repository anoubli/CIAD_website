package fr.ciadlab.labmanager.controller.api.project;

import java.util.Optional;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.FundingSchemeType;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectType;
import fr.ciadlab.labmanager.entities.project.TRLGrade;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.project.ProjectService;

@RestController
@CrossOrigin
public class ProjectApiController extends AbstractApiController {
	
	private ProjectService projectService;
	
	/**Constructor for injector
	 * This constructor is defined for being invoked by the IOC injector
	 * 
	 * @param messages
	 * @param constants
	 * @param projectService
	 * @param personService
	 * @param researchOrganizationService
	 */
	public ProjectApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectService projectService,
			@Autowired ResearchOrganizationService researchOrganizationService,
			@Autowired PersonService personService) {
		super(messages, constants);
		this.projectService = projectService;
	}
	
	@GetMapping(value = "/getProjectData", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Object getProjectData(@RequestParam(required = false) String name, @RequestParam(required = false) Integer id) {
		if (id == null && Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Name and Identifier parameters are missed");
		}
		if (id != null) {
			return this.projectService.getProjectById(id.intValue());
		}
		return this.projectService.getProjectByName(name);
	}
	
	/** Saving information of a project
	 * 
	 * @param project
	 * @param name
	 * @param acronym
	 * @param fundingScheme
	 * @param description
	 * @param globalBudget
	 * @param budgetCIADLabOnly
	 * @param type
	 * @param pathImage
	 * @param videoUrl
	 * @param websiteUrl
	 * @param pathToDownloadPowerpoint
	 * @param expectedTRL
	 * @param confidential
	 * @param username
	 * @throws Exception
	 */
	@PostMapping(value = "/" + Constants.PROJECT_SAVING_ENDPOINT)
	public void saveProject(
			@RequestParam(required = false) Integer project,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String acronym,
			@RequestParam(required = false) FundingSchemeType fundingScheme,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) Float globalBudget,
			@RequestParam(required = false) Float budgetCIADLabOnly,
			@RequestParam(required = false) ProjectType type,
			@RequestParam(required = false) String videoUrl,
			@RequestParam(required = false) String websiteUrl,
			@RequestParam(required = false) String pathToDownloadPowerpoint,
			@RequestParam(required = false) TRLGrade expectedTRL,
			@RequestParam(required = false) Boolean confidential,
			@RequestParam(required = false) Integer owningOrga,
			@RequestParam(required = false) Integer managerOrga,
			@RequestParam(required = false) Integer partnerOrga,
			@RequestParam(required = false) Integer person,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /" + Constants.PROJECT_SAVING_ENDPOINT + " by " + username + " for project " + project);
		ensureCredentials(username);
		final Project optProject;
		if (project == null) {
			optProject = this.projectService.createProject(name, acronym, fundingScheme, description, globalBudget, budgetCIADLabOnly, type, videoUrl, websiteUrl, pathToDownloadPowerpoint, expectedTRL, confidential, owningOrga, managerOrga, partnerOrga, person);
		} else {
			optProject = this.projectService.updateProject(project.intValue(), name, acronym, fundingScheme, description, globalBudget, budgetCIADLabOnly, type, videoUrl, websiteUrl, pathToDownloadPowerpoint, expectedTRL, confidential, owningOrga, managerOrga, partnerOrga, person);
		}
		if(optProject == null) {
			throw new IllegalStateException("Project not found");
		}
	
	}
	
	@DeleteMapping("/deleteProject")
	public void deleteProject(
			@RequestParam Integer project,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username ) {
		getLogger().info("Opening /deletePublication by " + username + " for project " + project); //$NON-NLS-1$ //$NON-NLS-2$
		ensureCredentials(username);
		if (project == null || project.intValue() == 0) {
			throw new IllegalStateException("Project not found");
		}
		this.projectService.removeProject(project.intValue());
	}
}

