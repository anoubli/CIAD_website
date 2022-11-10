/**
 * 
 */
package fr.ciadlab.labmanager.entities.project;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
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

/**
 * @author baptiste
 *
 */
@Entity
@Table(name = "Projects")
@Inheritance(strategy = InheritanceType.JOINED)
@Polymorphism(type = PolymorphismType.IMPLICIT)
public abstract class Project
		implements Serializable, JsonSerializable, Comparable<Project>, AttributeProvider, IdentifiableEntity {
	/**
	 * Constants for State of the project
	 */
	public final String UNDER_EVALUATION = "under evaluation";
	public final String ACCEPTED = "accepted";
	public final String STARTED = "started";
	public final String FINISHED = "finihed";
	public final String CANCELED = "canceled";

	
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
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String fundingScheme;
	
	/** short description of the project.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String description;
	
	/** starting date of the project.
	 */
	@Column
	private LocalDate startingDate;
	
	/** duration (in month) of the project.
	 */
	@Column
	private int duration;
	
	/** state of the project.
	 */
	@Column
	private String state;
	
	/** global budget of the project.
	 */
	@Column
	private int globalBudget;
	
	/** budget only for the CIAD Lab of the project.
	 */
	@Column
	private int budgetCIADLabOnly;
	
	/** type of the project.
	 */
	@Column
	private String type;
	//TODO maybe add a new class ProjectType and ProjectCategory
	
	//private <Person> managerCIAD;
	//private List<Person> participantsCIAD;
	
	/**
	 * List of institution involved in the project (Outside the CIAD)
	 */
	@Column
	private List<String> participatingInstutitions;
	
	/**
	 * Institution who coordinate the project
	 */
	@Column
	private String coordinatingInstitution;
	
	/** number of people involved in the project from CIAD.
	 */
	@Column
	private int numberPersonsByMonth;
	
	//One to Many
	//private List<Publication> publications;
	
	//private List<Image> images;
	//private List<Video> images;
	
	/** Website URL of the project if it has one.
	 */
	@Column
	private String websiteUrl;
	
	/** expected TRL for the project.
	 */
	@Column
	private int expectedTRL;
	
	//private List<String|ScientificDomain> scientificDomains;
	//private List<String|ApplicationDomain> applicationDomains;s
	
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

	public String getFundingScheme() {
		return fundingScheme;
	}

	public void setFundingScheme(String fundingScheme) {
		this.fundingScheme = fundingScheme;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(LocalDate startingDate) {
		this.startingDate = startingDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getGlobalBudget() {
		return globalBudget;
	}

	public void setGlobalBudget(int globalBudget) {
		this.globalBudget = globalBudget;
	}

	public int getBudgetCIADLabOnly() {
		return budgetCIADLabOnly;
	}

	public void setBudgetCIADLabOnly(int budgetCIADLabOnly) {
		this.budgetCIADLabOnly = budgetCIADLabOnly;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getParticipatingInstutitions() {
		return participatingInstutitions;
	}

	public void setParticipatingInstutitions(List<String> participatingInstutitions) {
		this.participatingInstutitions = participatingInstutitions;
	}

	public String getCoordinatingInstitution() {
		return coordinatingInstitution;
	}

	public void setCoordinatingInstitution(String coordinatingInstitution) {
		this.coordinatingInstitution = coordinatingInstitution;
	}

	public int getNumberPersonsByMonth() {
		return numberPersonsByMonth;
	}

	public void setNumberPersonsByMonth(int numberPersonsByMonth) {
		this.numberPersonsByMonth = numberPersonsByMonth;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public int getExpectedTRL() {
		return expectedTRL;
	}

	public void setExpectedTRL(int expectedTRL) {
		this.expectedTRL = expectedTRL;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		// TODO Auto-generated method stub

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
