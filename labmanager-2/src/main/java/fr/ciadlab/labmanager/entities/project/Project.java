/**
 * 
 */
package fr.ciadlab.labmanager.entities.project;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;

import fr.ciadlab.labmanager.entities.organization.*;
import fr.ciadlab.labmanager.entities.member.Person;

/** Abstract representation of a research project
 * 
 * @author $Author: bperrat-dit-janton$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Projects")
@Inheritance(strategy = InheritanceType.JOINED)
@Polymorphism(type = PolymorphismType.IMPLICIT)
public class Project
		implements Serializable, JsonSerializable, Comparable<Project>, AttributeProvider, IdentifiableEntity {
	
	private static final long serialVersionUID = 2713078763828551628L;

	/**
	 * Identifier of the project
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;
	
	/** name of the project.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String name;
	
	/** Acronym of the project.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String acronym;
	
	/** funding scheme of the project.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private FundingSchemeType fundingScheme;
	
	/** short description of the project.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String description;
	
	/** global budget of the project.
	 */
	@Column
	private float globalBudget;
	
	/** budget only for the CIAD Lab of the project.
	 */
	@Column
	private float budgetCIADLabOnly;
	
	/** type of the project.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private ProjectType type;

	/** List of the reference Person of the project.
	 */
	@ManyToMany(mappedBy = "referenceProjects", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable( name = "PERSONS_PROJECTS", 
				joinColumns = @JoinColumn(name ="idProjects"),
				inverseJoinColumns = @JoinColumn(name = "idPersons"))
	private Set<Person> referencePersons;
	
	/**
	 * Organization who coordinate the project
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	private ResearchOrganization owningOrganization;
	
	/**
	 * List of institution involved in the project (Outside the CIAD)
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable( name = "RESEARCH_ORGS_PARTNER_PROJECTS",
				joinColumns = @JoinColumn(name = "idProjects"),
				inverseJoinColumns = @JoinColumn( name = "idResearchOrgs"))
	private Set<ResearchOrganization> partnerOrganizations;
	
	/**
	 * Organization with a partnership on the project
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	private ResearchOrganization managerOrganization;
	
	/** URL of a associated video if the project has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String pathImage;
	
	/** URL of a associated video if the project has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String videoUrl;
	
	/** Website URL of the project if it has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String websiteUrl;
	
	/** Name of the powerpoint of the project if it has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String pathToDownloadPowerpoint;
	
	/** expected TRL for the project.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private TRLGrade expectedTRL; 
	
	/** If the project is confidential or not.
	 */
	@Column
	private boolean confidential;

	
	public Project(int id, String name, String acronym, FundingSchemeType fundingScheme, String description,
			float globalBudget, float budgetCIADLabOnly, ProjectType type, Set<Person> referencePersons,
			ResearchOrganization owningOrganization, Set<ResearchOrganization> partnerOrganizations,
			ResearchOrganization managerOrganization, String pathImage, String videoUrl, String websiteUrl,
			String pathToDownloadPowerpoint, TRLGrade expectedTRL, boolean confidential) {
		super();
		this.id = id;
		this.name = name;
		this.acronym = acronym;
		this.fundingScheme = fundingScheme;
		this.description = description;
		this.globalBudget = globalBudget;
		this.budgetCIADLabOnly = budgetCIADLabOnly;
		this.type = type;
		this.referencePersons = referencePersons;
		this.owningOrganization = owningOrganization;
		this.partnerOrganizations = partnerOrganizations;
		this.managerOrganization = managerOrganization;
		this.pathImage = pathImage;
		this.videoUrl = videoUrl;
		this.websiteUrl = websiteUrl;
		this.pathToDownloadPowerpoint = pathToDownloadPowerpoint;
		this.expectedTRL = expectedTRL;
		this.confidential = confidential;
	}

	/**
	 * Create an empty project
	 */
	public Project() {
		//
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public FundingSchemeType getFundingScheme() {
		return fundingScheme;
	}

	public void setFundingScheme(FundingSchemeType fundingScheme) {
		this.fundingScheme = fundingScheme;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getGlobalBudget() {
		return globalBudget;
	}

	public void setGlobalBudget(float globalBudget) {
		this.globalBudget = globalBudget;
	}

	public float getBudgetCIADLabOnly() {
		return budgetCIADLabOnly;
	}

	public void setBudgetCIADLabOnly(float budgetCIADLabOnly) {
		this.budgetCIADLabOnly = budgetCIADLabOnly;
	}

	public ProjectType getType() {
		return type;
	}

	public void setType(ProjectType type) {
		this.type = type;
	}

	/**
	 * @return the referencePersons
	 */
	public Set<Person> getReferencePersons() {
		return referencePersons;
	}

	/**
	 * @param referencePersons the referencePersons to set
	 */
	public void setReferencePersons(Set<Person> referencePersons) {
		this.referencePersons = referencePersons;
	}

	public ResearchOrganization getOwningOrganization() {
		return owningOrganization;
	}

	public void setOwningOrganization(ResearchOrganization owningOrganization) {
		this.owningOrganization = owningOrganization;
	}

	public Set<ResearchOrganization> getPartnerOrganizations() {
		return partnerOrganizations;
	}

	public void setPartnerOrganizations(Set<ResearchOrganization> partnerOrganizations) {
		this.partnerOrganizations = partnerOrganizations;
	}

	public ResearchOrganization getManagerOrganization() {
		return managerOrganization;
	}

	public void setManagerOrganization(ResearchOrganization managerOrganization) {
		this.managerOrganization = managerOrganization;
	}

	public String getPathImage() {
		return pathImage;
	}

	public void setPathImage(String pathImage) {
		this.pathImage = pathImage;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getPathToDownloadPowerpoint() {
		return pathToDownloadPowerpoint;
	}

	public void setPathToDownloadPowerpoint(String pathToDownloadPowerpoint) {
		this.pathToDownloadPowerpoint = pathToDownloadPowerpoint;
	}

	public TRLGrade getExpectedTRL() {
		return expectedTRL;
	}

	public void setExpectedTRL(TRLGrade expectedTRL) {
		this.expectedTRL = expectedTRL;
	}

	public boolean isConfidential() {
		return confidential;
	}

	public void setConfidential(boolean confidential) {
		this.confidential = confidential;
	}

	@Override
	public int hashCode() {
		return Objects.hash(acronym, budgetCIADLabOnly, confidential, description, expectedTRL, fundingScheme,
				globalBudget, id, managerOrganization, name, owningOrganization, partnerOrganizations, pathImage,
				pathToDownloadPowerpoint, referencePersons, type, videoUrl, websiteUrl);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		return Objects.equals(acronym, other.acronym)
				&& Float.floatToIntBits(budgetCIADLabOnly) == Float.floatToIntBits(other.budgetCIADLabOnly)
				&& confidential == other.confidential && Objects.equals(description, other.description)
				&& expectedTRL == other.expectedTRL && fundingScheme == other.fundingScheme
				&& Float.floatToIntBits(globalBudget) == Float.floatToIntBits(other.globalBudget) && id == other.id
				&& Objects.equals(managerOrganization, other.managerOrganization) && Objects.equals(name, other.name)
				&& Objects.equals(owningOrganization, other.owningOrganization)
				&& Objects.equals(partnerOrganizations, other.partnerOrganizations)
				&& Objects.equals(pathImage, other.pathImage)
				&& Objects.equals(pathToDownloadPowerpoint, other.pathToDownloadPowerpoint)
				&& Objects.equals(referencePersons, other.referencePersons) && type == other.type
				&& Objects.equals(videoUrl, other.videoUrl) && Objects.equals(websiteUrl, other.websiteUrl);
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", acronym=" + acronym + ", fundingScheme=" + fundingScheme
				+ ", description=" + description + ", globalBudget=" + globalBudget + ", budgetCIADLabOnly="
				+ budgetCIADLabOnly + ", type=" + type + ", referencePersons=" + referencePersons
				+ ", owningOrganization=" + owningOrganization + ", partnerOrganizations=" + partnerOrganizations
				+ ", managerOrganization=" + managerOrganization + ", pathImage=" + pathImage + ", videoUrl=" + videoUrl
				+ ", websiteUrl=" + websiteUrl + ", pathToDownloadPowerpoint=" + pathToDownloadPowerpoint
				+ ", expectedTRL=" + expectedTRL + ", confidential=" + confidential + "]";
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(Project arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	
}
