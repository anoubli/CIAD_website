/**
 * 
 */
package fr.ciadlab.labmanager.entities.project;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
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
import javax.persistence.Lob;
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

/** Abstract rerpesentation of a research project
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
public abstract class Project
		implements Serializable, JsonSerializable, Comparable<Project>, AttributeProvider, IdentifiableEntity {
	
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
	@OneToMany(mappedBy = "referenceProjects", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Person> referencePersons;
	
	/**
	 * Organization who coordinate the project
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	private ResearchOrganization owningOrganization;
	
	/**
	 * List of institution involved in the project (Outside the CIAD)
	 */
	@OneToMany(mappedBy = "partnerProjects", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

}
