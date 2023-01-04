package fr.ciadlab.labmanager.service.project;

import org.springframework.stereotype.Service;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
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
	
	private final ResearchOrganizationRepository researchOrganizationRepository;
	
	private final PersonRepository personRepository;
	
	/**
	 * 
	 * @param messages
	 * @param constants
	 * @param projectRepository
	 */
	public ProjectService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectRepository projectRepository,
			@Autowired ResearchOrganizationRepository researchOrganizationRepository,
			@Autowired PersonRepository personRepository) {
		super(messages, constants);
		this.projectRepository = projectRepository;
		this.researchOrganizationRepository = researchOrganizationRepository;
		this.personRepository = personRepository;
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
	
	public Project getProjectById(int identifier) {
		final Optional<Project> byId = this.projectRepository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}
	
	public List<Project> getProjectByName(String name) {
		if (Strings.isNullOrEmpty(name)) {
			return Collections.emptyList();
		}
		return this.projectRepository.findAllByNameIgnoreCase(name);
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
			ResearchOrganization managerOrga = project.getManagerOrganization();
			ResearchOrganization owningOrga = project.getOwningOrganization();
			if (managerOrga != null) {
				managerOrga.getManageProjects().remove(project);
				project.setManagerOrganization(null);
				this.researchOrganizationRepository.save(managerOrga);
			}
			if (owningOrga != null) {
				owningOrga.getOwningProjects().remove(project);
				project.setOwningOrganization(null);
				this.researchOrganizationRepository.save(owningOrga);
			}
			for (Person referencePerson : project.getReferencePersons()) {
				referencePerson.getReferenceProjects().remove(project);
				project.getReferencePersons().remove(referencePerson);
				this.personRepository.save(referencePerson);
			}
			for (ResearchOrganization partnerOrganization : project.getPartnerOrganizations()) {
				partnerOrganization.getPartnerProjects().remove(project);
				project.getPartnerOrganizations().remove(partnerOrganization);
				this.researchOrganizationRepository.save(partnerOrganization);
			}
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
