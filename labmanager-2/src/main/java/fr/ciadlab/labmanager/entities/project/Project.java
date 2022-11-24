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

import fr.ciadlab.labmanager.entities.organization.*;
import fr.ciadlab.labmanager.entities.member.Person;

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
	 * Constants for Type of the project
	 */
	public final String THEORICAL_RESEARCH = "Recherche théorique";
	public final String APPLIED_RESEARCH = "Recherche appliqué";
	public final String EXPERIMENTAL_DEVELOPMENT = "Développement expérimental";
	//Todo enum ?
	
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
	//TODO faire une enum (class) avec une description de ce dernier
	
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
	private String type;
	//TODO maybe add a new class ProjectType and ProjectCategory

	/** List of the reference Person of the project.
	 */
	@Column
	private List<Person> referencePersons;
	
	/**
	 * Organization who coordinate the project
	 */
	@Column
	private ResearchOrganization OwningOrganization;
	//Todo link to ResearchOrganization
	
	/**
	 * List of institution involved in the project (Outside the CIAD)
	 */
	@Column
	private List<ResearchOrganization> participatingOrganizations;
	
	/**
	 * Organization with a partnership on the project
	 */
	@Column
	private ResearchOrganization partnerOrganization;
	//Todo link to ResearchOrganization
	
	
	//private List<Image> images;
	
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
	private String powerpoint;
	
	/** expected TRL for the project.
	 */
	@Column
	private int expectedTRL;
	
	/** If the project is confidential or not.
	 */
	@Column
	private boolean confidential;

}
