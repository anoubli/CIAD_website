/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities.organization;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
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
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.io.json.JsonUtils.CachedGenerator;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import org.arakhne.afc.util.CountryCode;

/** Research organization or group or researchers.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "ResearchOrgs")
public class ResearchOrganization implements Serializable, JsonSerializable, Comparable<ResearchOrganization>, AttributeProvider, IdentifiableEntity {

	/** Default country for research organizations.
	 */
	public static final CountryCode DEFAULT_COUNTRY = CountryCode.FRANCE;

	/** Default type for research organizations.
	 */
	public static final ResearchOrganizationType DEFAULT_TYPE = ResearchOrganizationType.LABORATORY;

	private static final long serialVersionUID = -450531251083286848L;

	/** Identifier of the organization.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Acronym of the research organization.
	 */
	@Column
	private String acronym;

	/** Name of the research organization.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String name;

	/** Textual description of the research organization.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	@Lob
	private String description;

	/** The country of the organization.
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CountryCode country = DEFAULT_COUNTRY;

	/** The type of organization.
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ResearchOrganizationType type = DEFAULT_TYPE;

	/** The URL of the organization.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String organizationUrl;

	/** Members of the organization.
	 */
	@OneToMany(mappedBy = "researchOrganization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Membership> memberships = new HashSet<>();

	/** Reference to the super organization.
	 */
	@ManyToOne
	private ResearchOrganization superOrganization;

	/** References to the sub organizations.
	 */
	@OneToMany(mappedBy = "superOrganization")
	private Set<ResearchOrganization> subOrganizations = new HashSet<>();
	
	/**
	 * Reference to the projects owned
	 */
	@OneToMany(mappedBy = "owningOrganization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Project> owningProjects;
	
	/**
	 * Reference to the partner projects 
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable( name = "RESEARCH_ORGS_PARTNER_PROJECTS",
				joinColumns = @JoinColumn(name = "idResearchOrgs"),
				inverseJoinColumns = @JoinColumn( name = "idProjects"))
	private Set<Project> partnerProjects;
	
	/**
	 * Reference to the projects managed
	 */
	@OneToMany(mappedBy = "managerOrganization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Project> manageProjects;

	/** Construct a research organization from the given values.
	 * 
	 * @param id the identifier of the organization.
	 * @param members the members of the organization.
	 * @param subOrgas the references to the suborganizations.
	 * @param superOrga the reference to the super organization.
	 * @param acronym the acronym of the organization.
	 * @param name the name of the organization.
	 * @param description the textual description of the organization.
	 */
	public ResearchOrganization(int id, Set<Membership> members, Set<ResearchOrganization> subOrgas,
			ResearchOrganization superOrga, String acronym, String name, String description) {
		this.id = id;
		if (members == null) {
			this.memberships = new HashSet<>();
		} else {
			this.memberships = members;
		}
		if (subOrgas == null) {
			this.subOrganizations = new HashSet<>();
		} else {
			this.subOrganizations = subOrgas;
		}
		this.superOrganization = superOrga;
		this.acronym = acronym;
		this.name = name;
		this.description = description;
	}

	/** Construct an empty research organization.
	 */
	public ResearchOrganization() {
		//
	}

	@Override
	public String toString() {
		return getAcronymOrName() + ":" + this.id; //$NON-NLS-1$
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.acronym);
		h = HashCodeUtils.add(h, this.country);
		h = HashCodeUtils.add(h, this.description);
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.name);
		h = HashCodeUtils.add(h, this.organizationUrl);
		h = HashCodeUtils.add(h, this.type);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final ResearchOrganization other = (ResearchOrganization) obj;
		if (this.id != other.id) {
			return false;
		}
		if (!Objects.equals(this.acronym, other.acronym)) {
			return false;
		}
		if (!Objects.equals(this.country, other.country)) {
			return false;
		}
		if (!Objects.equals(this.description, other.description)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.organizationUrl, other.organizationUrl)) {
			return false;
		}
		if (!Objects.equals(this.type, other.type)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(ResearchOrganization o) {
		return EntityUtils.getPreferredResearchOrganizationComparator().compare(this, o);
	}

	/** {@inheritDoc}
	 * <p>The attributes that are not considered by this function are:<ul>
	 * <li>{@code id}</li>
	 * <li>{@code superOrganization}</li>
	 * <li>{@code subOrganizations}</li>
	 * </ul>
	 */
	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		assert consumer != null : "How to consume an attribute if the consumer is null?"; //$NON-NLS-1$
		if (getId() != 0) {
			consumer.accept("id", Integer.valueOf(getId())); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAcronym())) {
			consumer.accept("acronym", getAcronym()); //$NON-NLS-1$
		}
		if (getCountry() != null) {
			consumer.accept("country", getCountry()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getDescription())) {
			consumer.accept("description", getDescription()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getName())) {
			consumer.accept("name", getName()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getOrganizationURL())) {
			consumer.accept("organizationURL", getOrganizationURL()); //$NON-NLS-1$
		}
		if (getType() != null) {
			consumer.accept("type", getType()); //$NON-NLS-1$
		}
	}

	@Override
	public void serialize(JsonGenerator generator, SerializerProvider serializers) throws IOException {
		generator.writeStartObject();
		forEachAttribute((attrName, attrValue) -> {
			JsonUtils.writeField(generator, attrName, attrValue);
		});
		//
		final CachedGenerator organizations = JsonUtils.cache(generator);
		final CachedGenerator persons = JsonUtils.cache(generator);
		//
		organizations.writeReferenceOrObjectField("superOrganization", getSuperOrganization(), () -> { //$NON-NLS-1$
			JsonUtils.writeObjectAndAttributes(generator, getSuperOrganization());
		});
		//
		generator.writeArrayFieldStart("subOrganizations"); //$NON-NLS-1$
		for (final ResearchOrganization suborga : getSubOrganizations()) {
			organizations.writeReferenceOrObject(suborga, () -> {
				JsonUtils.writeObjectAndAttributes(generator, suborga);
			});
		}
		generator.writeEndArray();
		//
		generator.writeArrayFieldStart("memberships"); //$NON-NLS-1$
		for (final Membership membership : getMemberships()) {
			generator.writeStartObject();
			membership.forEachAttribute((attrName, attrValue) -> {
				JsonUtils.writeField(generator, attrName, attrValue);
			});
			persons.writeReferenceOrObjectField("person", membership.getPerson(), () -> { //$NON-NLS-1$
				JsonUtils.writeObjectAndAttributes(generator, membership.getPerson());
			});
			generator.writeEndObject();
		}
		generator.writeEndArray();
		//
		generator.writeEndObject();
	}

	@Override
	public void serializeWithType(JsonGenerator generator, SerializerProvider serializers, TypeSerializer typeSer)
			throws IOException {
		serialize(generator, serializers);
	}

	@Override
	public int getId() {
		return this.id;
	}

	/** Change the identifier of the research organization.
	 *
	 * @param id the identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** Replies the members of the organization.
	 *
	 * @return the members.
	 */
	public Set<Membership> getMemberships() {
		return this.memberships;
	}

	/** Change the members of the organization.
	 *
	 * @param members the members.
	 */
	public void setMemberships(Set<Membership> members) {
		if (members == null) {
			this.memberships = new HashSet<>();
		} else {
			this.memberships = members;
		}
	}

	/** Replies the suborganizations.
	 *
	 * @return the set of suborganizations.
	 */
	public Set<ResearchOrganization> getSubOrganizations() {
		return this.subOrganizations;
	}

	/** Change the suborganizations.
	 *
	 * @param set the set of suborganizations.
	 */
	public void setSubOrganizations(Set<ResearchOrganization> set) {
		if (set == null) {
			this.subOrganizations = new HashSet<>();
		} else {
			this.subOrganizations = set;
		}
	}

	/** Replies the reference to the super organization.
	 *
	 * @return the super organization or {@code null}.
	 */
	public ResearchOrganization getSuperOrganization() {
		return this.superOrganization;
	}

	/** Change the reference to the super organization.
	 *
	 * @param orga the super organization or {@code null}.
	 */
	public void setSuperOrganization(ResearchOrganization orga) {
		// TODO: An organization may have multiple super organizations.
		// For examples, CIAD laboratory has UTBM, UB, UBFC as super-organizations.
		this.superOrganization = orga;
	}

	/** Replies the acronym or the name of the research organization.
	 *
	 * @return the acronym or name.
	 */
	public String getAcronymOrName() {
		return Strings.isNullOrEmpty(this.acronym) ? this.name : this.acronym;
	}

	/** Replies the acronym of the research organization.
	 *
	 * @return the acronym.
	 */
	public String getAcronym() {
		return this.acronym;
	}

	/** Change the acronym of the research organization.
	 *
	 * @param acronym the acronym.
	 */
	public void setAcronym(String acronym) {
		this.acronym = Strings.emptyToNull(acronym);
	}

	/** Replies the name of the research organization.
	 *
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}

	/** Change the name of the research organization.
	 *
	 * @param name the name.
	 */
	public void setName(String name) {
		this.name = Strings.emptyToNull(name);
	}

	/** Replies the textual description.
	 *
	 * @return the textual description.
	 */
	public String getDescription() {
		return this.description;
	}

	/** Change the textual description.
	 *
	 * @param description the textual description.
	 */
	public void setDescription(String description) {
		this.description = Strings.emptyToNull(description);
	}

	/** Replies the name of the country of the organization.
	 *
	 * @return the display name of the country.
	 */
	public String getCountryDisplayName() {
		return CountryCodeUtils.getDisplayCountry(this.country);
	}

	/** Replies the country of the organization.
	 *
	 * @return the country, never {@code null}.
	 */
	public CountryCode getCountry() {
		return this.country;
	}

	/** Change the country of the organization.
	 *
	 * @param country the country, or {@code null} if the country is the default country.
	 * @see #DEFAULT_COUNTRY
	 */
	public void setCountry(CountryCode country) {
		if (country == null) {
			this.country = DEFAULT_COUNTRY;
		} else {
			this.country = country;
		}
	}

	/** Change the country of the organization.
	 *
	 * @param country the country.
	 */
	public final void setCountry(String country) {
		if (Strings.isNullOrEmpty(country)) {
			setCountry((CountryCode) null);
		} else {
			setCountry(CountryCodeUtils.valueOfCaseInsensitive(country));
		}
	}

	/** Replies the URL of the research organization.
	 *
	 * @return the URL.
	 */
	public String getOrganizationURL() {
		return this.organizationUrl;
	}

	/** Replies the URL of the research organization.
	 *
	 * @return the URL.
	 */
	public final URL getOrganizationURLObject() {
		try {
			return new URL(getOrganizationURL());
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/** Change the URL of the research organization.
	 *
	 * @param url the URL.
	 */
	public void setOrganizationURL(String url) {
		this.organizationUrl = Strings.emptyToNull(url);
	}

	/** Change the URL of the research organization.
	 *
	 * @param url the URL.
	 */
	public final void setOrganizationURL(URL url) {
		final String value;
		if (url != null) {
			value = url.toExternalForm();
		} else {
			value = null;
		}
		setOrganizationURL(value);
	}

	/** Replies the type of organization.
	 *
	 * @return the type.
	 */
	public ResearchOrganizationType getType() {
		return this.type;
	}

	/** Change the type of organization.
	 *
	 * @param type the country, or {@code null} if the type is the default one.
	 * @see #DEFAULT_TYPE
	 */
	public void setType(ResearchOrganizationType type) {
		if (type == null) {
			this.type = DEFAULT_TYPE;
		} else {
			this.type = type;
		}
	}

	/** Change the type of the organization.
	 *
	 * @param type the type.
	 */
	public final void setType(String type) {
		if (Strings.isNullOrEmpty(type)) {
			setType((ResearchOrganizationType) null);
		} else {
			setType(ResearchOrganizationType.valueOfCaseInsensitive(type));
		}
	}

	/**
	 * @return the owningProjects
	 */
	public Set<Project> getOwningProjects() {
		return owningProjects;
	}

	/**
	 * @param owningProjects the owningProjects to set
	 */
	public void setOwningProjects(Set<Project> owningProjects) {
		this.owningProjects = owningProjects;
	}

	/**
	 * @return the partnerProjects
	 */
	public Set<Project> getPartnerProjects() {
		return partnerProjects;
	}

	/**
	 * @param partnerProjects the partnerProjects to set
	 */
	public void setPartnerProjects(Set<Project> partnerProjects) {
		this.partnerProjects = partnerProjects;
	}

	/**
	 * @return the manageProjects
	 */
	public Set<Project> getManageProjects() {
		return manageProjects;
	}

	/**
	 * @param manageProjects the manageProjects to set
	 */
	public void setManageProjects(Set<Project> manageProjects) {
		this.manageProjects = manageProjects;
	}
	

}
