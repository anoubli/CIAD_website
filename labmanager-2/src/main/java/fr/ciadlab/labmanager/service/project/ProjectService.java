package fr.ciadlab.labmanager.service.project;

import org.springframework.stereotype.Service;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.List;
import java.util.Set;
import java.util.Optional;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import fr.ciadlab.labmanager.repository.project.ProjectRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.FundingSchemeType;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectType;
import fr.ciadlab.labmanager.entities.project.TRLGrade;


@Service
public class ProjectService extends AbstractService {
	
	private final ProjectRepository projectRepository;
	
	/**
	 * 
	 * @param messages
	 * @param constants
	 * @param projectRepository
	 */
	public ProjectService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectRepository projectRepository) {
		super(messages, constants);
		this.projectRepository = projectRepository;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Project> getAllProjects() {
		return this.projectRepository.findAll();
	}
	
	/**
	 * 
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
	 * @return
	 */
	public Project createProject(String name, String acronym, FundingSchemeType fundingScheme, String description, 
			float globalBudget, float budgetCIADLabOnly, ProjectType type,String pathImage, String videoUrl, String websiteUrl,
			String pathToDownloadPowerpoint, TRLGrade expectedTRL, boolean confidential) {
		
		final Project res = new Project();
		res.setName(name);
		res.setAcronym(acronym);
		res.setFundingScheme(fundingScheme);
		res.setDescription(description);
		res.setGlobalBudget(globalBudget);
		res.setBudgetCIADLabOnly(budgetCIADLabOnly);
		res.setType(type);
		res.setPathImage(pathImage);
		res.setVideoUrl(videoUrl);
		res.setWebsiteUrl(websiteUrl);
		res.setPathToDownloadPowerpoint(pathToDownloadPowerpoint);
		res.setExpectedTRL(expectedTRL);
		res.setConfidential(confidential);
		
		this.projectRepository.save(res);
		return res;
	}
	
	/**
	 * 
	 * @param identifier
	 */
	public void removeProject(int identifier) {
		final Integer id = Integer.valueOf(identifier);
		final Optional<Project> projectRef = this.projectRepository.findById(id);
		if (projectRef.isPresent()) {
			final Project project = projectRef.get();
			//TODO
			// Delete all the relations : 
			// - managerOrganization - owningOrganisation - PartnerOrganization - referencePerson
			this.projectRepository.deleteById(id);
		}	
	}
	
	/**
	 * 
	 * @param identifier
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
	 * @return
	 */
	public Project updateProject(int identifier, String name, String acronym, FundingSchemeType fundingScheme, String description, 
			float globalBudget, float budgetCIADLabOnly, ProjectType type,String pathImage, String videoUrl, String websiteUrl,
			String pathToDownloadPowerpoint, TRLGrade expectedTRL, boolean confidential) {
		
		final Integer id = Integer.valueOf(identifier);
		final Optional<Project> res = this.projectRepository.findById(id);
		
		if (res.isPresent()) {
			final Project project = res.get();
			if (project != null) {
				if(!Strings.isNullOrEmpty(name)) {
					project.setName(name);
				}
				project.setName(Strings.emptyToNull(name));
				project.setAcronym(Strings.emptyToNull(acronym));
				project.setFundingScheme(fundingScheme);
				project.setDescription(Strings.emptyToNull(description));
				project.setGlobalBudget(globalBudget);
				project.setBudgetCIADLabOnly(budgetCIADLabOnly);
				project.setType(type);
				project.setPathImage(Strings.emptyToNull(pathImage));
				project.setVideoUrl(Strings.emptyToNull(videoUrl));
				project.setWebsiteUrl(Strings.emptyToNull(websiteUrl));
				project.setPathToDownloadPowerpoint(Strings.emptyToNull(pathToDownloadPowerpoint));
				project.setExpectedTRL(expectedTRL);
				project.setConfidential(confidential);
				this.projectRepository.save(project);
				return project;
			}
		}
		return null;
		
	}
}
