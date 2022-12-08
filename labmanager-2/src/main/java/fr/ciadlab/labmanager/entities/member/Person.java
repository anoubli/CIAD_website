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

package fr.ciadlab.labmanager.entities.member;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.AuthorshipComparator;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.io.json.JsonUtils.CachedGenerator;
import fr.ciadlab.labmanager.utils.HashCodeUtils;

/** Represent a person.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Persons")
public class Person implements Serializable, JsonSerializable, AttributeProvider, Comparable<Person>, IdentifiableEntity {

	private static final long serialVersionUID = -1312811718336186349L;

	/** Base URL for gravatar pictures.
	 */
	public static final String GRAVATAR_URL = "https://www.gravatar.com/avatar/"; //$NON-NLS-1$

	/** HTML query parameter name for specifying the size of an avatar on Gravatar.
	 */
	public static final String GRAVATAR_SIZE_PARAM = "s"; //$NON-NLS-1$

	/** Base URL for github pictures.
	 */
	public static final String GITHUB_AVATAR_URL = "https://avatars.githubusercontent.com/"; //$NON-NLS-1$
	
	/** HTML query parameter name for specifying the size of an avatar on Github.
	 */
	public static final String GITHUB_AVATAR_SIZE_PARAM = "s"; //$NON-NLS-1$

	/** Base URL for Google scholar pictures.
	 */
	public static final String GOOGLE_SCHOLAR_AVATAR_URL = "https://scholar.googleusercontent.com/citations?view_op=view_photo&user="; //$NON-NLS-1$
	
	/** HTML query parameter name for specifying the size of an avatar on Google Scholar.
	 */
	public static final String GOOGLE_SCHOLAR_AVATAR_SIZE_PARAM = "s"; //$NON-NLS-1$

	private static final String ORCID_URL = "https://orcid.org/"; //$NON-NLS-1$

	private static final String FACEBOOK_URL = "https://www.facebook.com/"; //$NON-NLS-1$

	private static final String GITHUB_URL = "https://www.github.com/"; //$NON-NLS-1$
	
	private static final String LINKEDIN_URL = "http://linkedin.com/in/"; //$NON-NLS-1$

	/** Before 2022, ResearcherId was linked to Publons.
	 * Since 2022, Publons was integrated in Web-of-Science.
	 * The old URL of ResearcherId is {@code http://www.researcherid.com/rid/}.
	 */
	private static final String RESEARCHERID_URL = "https://www.webofscience.com/wos/author/rid/"; //$NON-NLS-1$

	private static final String GOOGLESCHOLAR_URL = "https://scholar.google.fr/citations?user="; //$NON-NLS-1$

	private static final String RESEARCHGATE_URL = "http://www.researchgate.net/profile/"; //$NON-NLS-1$

	/** Identifier of a person.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** First name of the person.
	 */
	@Column
	private String firstName;

	/** Last name of the person.
	 */
	@Column
	private String lastName;

	/** Gender of the person.
	 */
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	/** Email of the person.
	 */
	@Column
	private String email;

	/** Naming convention for the webpage of the person.
	 */
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private WebPageNaming webPageNaming;

	/** Identifier for the wb page of the person.
	 * This information is stored in the database for accelerating the JPA queries.
	 */
	@Column(nullable = true)
	private String webPageId;

	/** ORCID of the person.
	 */
	@Column
	private String orcid;

	/** URL of the person on {@code academia.edu}.
	 */
	@Column
	private String academiaURL;

	/** Office phone number, using the international prefix if possible.
	 */
	@Column(length = EntityUtils.VERY_SMALL_TEXT_SIZE)
	private String officePhone;

	/** Mobile phone number, using the international prefix if possible.
	 */
	@Column(length = EntityUtils.VERY_SMALL_TEXT_SIZE)
	private String mobilePhone;

	/** URL of the person on {@code cordis.europa.eu}.
	 */
	@Column
	private String cordisURL;

	/** URL of the person on DBLP.
	 */
	@Column
	private String dblpURL;

	/** Identifier of the person on Facebook.
	 */
	@Column
	private String facebookId;

	/** Identifier of the person on Github.
	 */
	@Column
	private String githubId;

	/** Identifier of the person on Linked-In.
	 */
	@Column
	private String linkedInId;

	/** Research Identifier of the person.
	 */
	@Column
	private String researcherId;

	/** Google Scholar Identifier of the person.
	 */
	@Column
	private String googleScholarId;

	/** Identifier of the person on Research Gate.
	 */
	@Column
	private String researchGateId;

	/** H-index of the person provided by Google Scholar.
	 */
	@Column
	private int googleScholarHindex;

	/** H-index of the person provided by Web-Of-Science.
	 */
	@Column
	private int wosHindex;

	/** Identifier of the person on Gravatar.
	 */
	@Column
	private String gravatarId;

	/** List of research organizations for the person.
	 */
	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Membership> memberships;

	/** List of publications of the person.
	 */
	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Authorship> authorships;

	/**
	 * Reference to the references projects 
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Project> referenceProjects;
	
	/** Construct a person with the given values.
	 *
	 * @param id the identifier of the person.
	 * @param publications the list of publications.
	 * @param orgas the memberships of the person.
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @param gender the gender of the person.
	 * @param email the email of the person.
	 * @param orcid the ORCID identifier of the person.
	 */
	public Person(int id, Set<Authorship> publications, Set<Membership> orgas, String firstName, String lastName,
			Gender gender, String email, String orcid) {
		this.id = id;
		this.authorships = publications;
		this.memberships = orgas;
		this.firstName = firstName;
		this.lastName = lastName;
		if (gender == Gender.NOT_SPECIFIED) {
			this.gender = null;
		}else {
			this.gender = gender;
		}
		this.email = email;
		this.orcid = orcid;
	}

	/** Construct an empty person.
	 */
	public Person() {
		//
	}

	@Override
	public String toString() {
		return getFullName() + ":" + this.id; //$NON-NLS-1$
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.academiaURL);
		h = HashCodeUtils.add(h, this.cordisURL);
		h = HashCodeUtils.add(h, this.dblpURL);
		h = HashCodeUtils.add(h, this.email);
		h = HashCodeUtils.add(h, this.facebookId);
		h = HashCodeUtils.add(h, this.firstName);
		h = HashCodeUtils.add(h, this.gender);
		h = HashCodeUtils.add(h, this.githubId);
		h = HashCodeUtils.add(h, this.googleScholarHindex);
		h = HashCodeUtils.add(h, this.gravatarId);
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.lastName);
		h = HashCodeUtils.add(h, this.linkedInId);
		h = HashCodeUtils.add(h, this.mobilePhone);
		h = HashCodeUtils.add(h, this.officePhone);
		h = HashCodeUtils.add(h, this.orcid);
		h = HashCodeUtils.add(h, this.googleScholarId);
		h = HashCodeUtils.add(h, this.researcherId);
		h = HashCodeUtils.add(h, this.researchGateId);
		h = HashCodeUtils.add(h, this.wosHindex);
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
		final Person other = (Person) obj;
		if (this.id != other.id) {
			return false;
		}
		if (!Objects.equals(this.academiaURL, other.academiaURL)) {
			return false;
		}
		if (!Objects.equals(this.cordisURL, other.cordisURL)) {
			return false;
		}
		if (!Objects.equals(this.dblpURL, other.dblpURL)) {
			return false;
		}
		if (!Objects.equals(this.email, other.email)) {
			return false;
		}
		if (!Objects.equals(this.facebookId, other.facebookId)) {
			return false;
		}
		if (!Objects.equals(this.firstName, other.firstName)) {
			return false;
		}
		if (!Objects.equals(this.gender, other.gender)) {
			return false;
		}
		if (!Objects.equals(this.githubId, other.githubId)) {
			return false;
		}
		if (this.googleScholarHindex != other.googleScholarHindex) {
			return false;
		}
		if (!Objects.equals(this.gravatarId, other.gravatarId)) {
			return false;
		}
		if (!Objects.equals(this.lastName, other.lastName)) {
			return false;
		}
		if (!Objects.equals(this.linkedInId, other.linkedInId)) {
			return false;
		}
		if (!Objects.equals(this.mobilePhone, other.mobilePhone)) {
			return false;
		}
		if (!Objects.equals(this.officePhone, other.officePhone)) {
			return false;
		}
		if (!Objects.equals(this.orcid, other.orcid)) {
			return false;
		}
		if (!Objects.equals(this.googleScholarId, other.googleScholarId)) {
			return false;
		}
		if (!Objects.equals(this.researcherId, other.researcherId)) {
			return false;
		}
		if (!Objects.equals(this.researchGateId, other.researchGateId)) {
			return false;
		}
		if (this.wosHindex != other.wosHindex) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Person o) {
		return EntityUtils.getPreferredPersonComparator().compare(this, o);
	}

	/** {@inheritDoc}
	 * <p>The attributes that are not considered by this function are:<ul>
	 * <li>{@code id}</li>
	 * <li>{@code publications}</li>
	 * <li>{@code researchOrganizations}</li>
	 * </ul>
	 */
	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (getId() != 0) {
			consumer.accept("id", Integer.valueOf(getId())); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAcademiaURL())) {
			consumer.accept("academiaURL", getAcademiaURL()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getCordisURL())) {
			consumer.accept("cordisURL", getCordisURL()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getDblpURL())) {
			consumer.accept("dblpURL", getDblpURL()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getEmail())) {
			consumer.accept("email", getEmail()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getFacebookId())) {
			consumer.accept("facebookId", getFacebookId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getFirstName())) {
			consumer.accept("firstName", getFirstName()); //$NON-NLS-1$
		}
		if (getGender() != null) {
			consumer.accept("gender", getGender()); //$NON-NLS-1$
		}
		if (getCivilTitle() != null) {
			consumer.accept("civilTitle", getCivilTitle()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getGithubId())) {
			consumer.accept("githubId", getGithubId()); //$NON-NLS-1$
		}
		if (getGoogleScholarHindex() > 0) {
			consumer.accept("googleScholarHindex", Integer.valueOf(getGoogleScholarHindex())); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getGravatarId())) {
			consumer.accept("gravatarId", getGravatarId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getLastName())) {
			consumer.accept("lastName", getLastName()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getFullName())) {
			consumer.accept("fullName", getFullName()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getFullNameWithLastNameFirst())) {
			consumer.accept("fullNameWithLastNameFirst", getFullNameWithLastNameFirst()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getLinkedInId())) {
			consumer.accept("linkedInId", getLinkedInId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getMobilePhone())) {
			consumer.accept("mobilePhone", getMobilePhone()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getOfficePhone())) {
			consumer.accept("officePhone", getOfficePhone()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getORCID())) {
			consumer.accept("orcid", getORCID()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getResearcherId())) {
			consumer.accept("researcherId", getResearcherId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getGoogleScholarId())) {
			consumer.accept("googleScholarId", getGoogleScholarId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getResearchGateId())) {
			consumer.accept("researchGateId", getResearchGateId()); //$NON-NLS-1$
		}
		if (getWosHindex() > 0) {
			consumer.accept("wosHindex", Integer.valueOf(getWosHindex())); //$NON-NLS-1$
		}
		if (getWebPageNaming() != null) {
			consumer.accept("webPageNaming", getWebPageNaming()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getWebPageId())) {
			consumer.accept("webPageId", getWebPageId()); //$NON-NLS-1$
		}
		if (getWebPageURI() != null) {
			consumer.accept("webPageURI", getWebPageURI()); //$NON-NLS-1$
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
		final CachedGenerator publications = JsonUtils.cache(generator);
		final CachedGenerator journals = JsonUtils.cache(generator);
		//
		generator.writeArrayFieldStart("memberships"); //$NON-NLS-1$
		for (final Membership membership : getMemberships()) {
			generator.writeStartObject();
			membership.forEachAttribute((attrName, attrValue) -> {
				JsonUtils.writeField(generator, attrName, attrValue);
			});
			organizations.writeReferenceOrObjectField("researchOrganization", membership.getResearchOrganization(), () -> { //$NON-NLS-1$
				JsonUtils.writeObjectAndAttributes(generator, membership.getResearchOrganization());
			});
			generator.writeEndObject();
		}
		generator.writeEndArray();
		//
		generator.writeArrayFieldStart("authorships"); //$NON-NLS-1$
		for (final Authorship authorship : getAuthorships()) {
			generator.writeStartObject();
			authorship.forEachAttribute((attrName, attrValue) -> {
				JsonUtils.writeField(generator, attrName, attrValue);
			});
			publications.writeReferenceOrObjectField("publication", authorship.getPublication(), () -> { //$NON-NLS-1$
				journals.writePublicationAndAttributes(authorship.getPublication(), journal -> {
					JsonUtils.writeObjectAndAttributes(generator, journal);
				});
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

	/** Change the identifier of the person.
	 *
	 * @param id the identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** Replies the first name of the person.
	 *
	 * @return the first name.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/** Change the first name of the person.
	 *
	 * @param name the first name.
	 */
	public void setFirstName(String name) {
		this.firstName = Strings.emptyToNull(name);
		resetWebPageId();
	}

	/** Replies the last name of the person.
	 *
	 * @return the last name.
	 */
	public String getLastName() {
		return this.lastName;
	}

	/** Change the last name of the person.
	 *
	 * @param name the last name.
	 */
	public void setLastName(String name) {
		this.lastName = Strings.emptyToNull(name);
		resetWebPageId();
	}

	/** Replies the sequence of first and last names of the person.
	 *
	 * @return the full name in the form {@code FIRST LAST}.
	 * @see #getFullNameWithLastNameFirst()
	 */
	public String getFullName() {
		return getFirstName() + " " + getLastName(); //$NON-NLS-1$
	}

	/** Replies the sequence of last and first names of the person.
	 *
	 * @return the full name in the form {@code LAST FIRST}.
	 * @see #getFullName()
	 */
	public String getFullNameWithLastNameFirst() {
		return getLastName() + " " + getFirstName(); //$NON-NLS-1$
	}

	/** Replies the email of the person.
	 *
	 * @return the email.
	 */
	public String getEmail() {
		return this.email;
	}

	/** Change the email of the person.
	 *
	 * @param email the email.
	 */
	public void setEmail(String email) {
		this.email = Strings.emptyToNull(email);
		resetWebPageId();
	}

	/** Replies the naming convention for the webpage of the person.
	 *
	 * @return the convention.
	 */
	public WebPageNaming getWebPageNaming() {
		if (this.webPageNaming == null) {
			return WebPageNaming.UNSPECIFIED;
		}
		return this.webPageNaming;
	}

	/** Recompute and reset the field for the webpage id.
	 */
	protected void resetWebPageId() {
		final WebPageNaming naming = getWebPageNaming();
		if (naming == null) {
			this.webPageId = null;
		} else {
			this.webPageId = naming.getWebpageIdFor(this);
		}
	}

	/** Change the naming convention for the webpage of the person.
	 *
	 * @param namingConvention the new naming convention.
	 */
	public void setWebPageNaming(WebPageNaming namingConvention) {
		this.webPageNaming = namingConvention;
		resetWebPageId();
	}

	/** Change the naming convention for the webpage of the person.
	 *
	 * @param namingConvention the new naming convention.
	 */
	public final void setWebPageNaming(String namingConvention) {
		if (Strings.isNullOrEmpty(namingConvention)) {
			setWebPageNaming((WebPageNaming) null);
		} else {
			setWebPageNaming(WebPageNaming.valueOfCaseInsensitive(namingConvention));
		}
	}

	/** Replies the URI of the webpage of the person.
	 *
	 * @return the URI or {@code null} if none.
	 */
	public URI getWebPageURI() {
		return getWebPageNaming().getWebpageURIFor(this);
	}

	/** Replies the identifier of the webpage of the person.
	 *
	 * @param id the identifier or {@code null}.
	 */
	public void setWebPageId(String id) {
		this.webPageId = Strings.emptyToNull(id);
	}

	/** Replies the identifier of the webpage of the person.
	 *
	 * @return the identifier or {@code null}.
	 */
	public String getWebPageId() {
		return this.webPageId;
	}

	/** Replies the ORCID of the person.
	 *
	 * @return the ORCID.
	 */
	public String getORCID() {
		return this.orcid;
	}

	/** Replies the URL to ORCID of the person.
	 *
	 * @return the ORCID URL.
	 */
	public final URL getOrcidURL() {
		if (!Strings.isNullOrEmpty(getORCID())) {
			try {
				return new URL(ORCID_URL + getORCID());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Change the ORCID of the person.
	 *
	 * @param orcid the orcid.
	 */
	public void setORCID(String orcid) {
		this.orcid = Strings.emptyToNull(orcid);
	}

	/** Replies the gender of the person.
	 *
	 * @return the gender.
	 */
	public Gender getGender() {
		if (this.gender == null) {
			return Gender.NOT_SPECIFIED;
		}
		return this.gender;
	}

	/** Change the gender of the person.
	 *
	 * @param gender the gender, or {@code null} to set to "unspecified".
	 */
	public void setGender(Gender gender) {
		if (gender == Gender.NOT_SPECIFIED) {
			this.gender = null;
		} else {
			this.gender = gender;
		}
	}

	/** Change the gender of the person.
	 *
	 * @param gender the gender.
	 */
	public final void setGender(String gender) {
		if (Strings.isNullOrEmpty(gender)) {
			setGender((Gender) null);
		} else {
			setGender(Gender.valueOfCaseInsensitive(gender));
		}
	}

	/** Replies the list of publications of the person.
	 *
	 * @return the list of publications.
	 */
	public Set<Authorship> getAuthorships() {
		if (this.authorships == null) {
			this.authorships = new TreeSet<>(AuthorshipComparator.DEFAULT);
		}
		return this.authorships;
	}

	/** Change the list of publications of the person.
	 *
	 * @param list the list of publications.
	 */
	public void setAuthorships(Set<Authorship> list) {
		this.authorships = list;
	}

	/** Replies the memberships of the person.
	 *
	 * @return the research organizations in which the person is involved.
	 */
	public Set<Membership> getMemberships() {
		if (this.memberships == null) {
			this.memberships = new TreeSet<>(EntityUtils.getPreferredMembershipComparator());
		}
		return this.memberships;
	}

	/** Replies the more recent membership per research organization.
	 *
	 * @return the recent memberships per organization.
	 */
	public Map<ResearchOrganization, Membership> getRecentMemberships() {
		return getRecentMemberships(null);
	}

	/** Replies the more recent membership per research organization.
	 *
	 * @param filteringCondition the condition to apply for filtering the memberships, never {@code null}.
	 * @return the recent membership for each organization.
	 */
	public Map<ResearchOrganization, Membership> getRecentMemberships(Predicate<? super Membership> filteringCondition) {
		Stream<Membership> stream = getMemberships().stream();
		if (filteringCondition != null) {
			stream = stream.filter(filteringCondition);
		}
		return stream.collect(Collectors.toMap(
				it -> it.getResearchOrganization(),
				it -> it,
				// Sort the memberships from the highest date to the lowest date
				BinaryOperator.minBy(EntityUtils.getPreferredMembershipComparator()),
				() -> new TreeMap<>(EntityUtils.getPreferredResearchOrganizationComparator())));
	}

	/** Replies the active membership per research organization.
	 * The current system date is used as the temporal reference.
	 *
	 * @return the active membership for each organization.
	 */
	public Map<ResearchOrganization, Membership> getActiveMemberships() {
		return getRecentMemberships(it -> it.isActive());
	}

	/** Replies the finished membership per research organization.
	 * The current system date is used as the temporal reference.
	 *
	 * @return the finished membership for each organization.
	 */
	public Map<ResearchOrganization, Membership> getFinishedMemberships() {
		return getRecentMemberships(it -> it.isFormer());
	}

	/** Replies the active or finished membership per research organization.
	 * The current system date is used as the temporal reference.
	 *
	 * @return the active or finished membership for each organization.
	 */
	public Map<ResearchOrganization, Membership> getActiveOrFinishedMemberships() {
		return getRecentMemberships(it -> !it.isFuture());
	}

	/** Replies the future membership per research organization.
	 * The current system date is used as the temporal reference.
	 *
	 * @return the future membership for each organization.
	 */
	public Map<ResearchOrganization, Membership> getFutureMemberships() {
		return getRecentMemberships(it -> it.isFuture());
	}

	/** Change the memberships of the person.
	 *
	 * @param orgas the research organizations in which the person is involved.
	 */
	public void setMemberships(Set<Membership> orgas) {
		this.memberships = orgas;
	}

	/** Delete a publication for the person.
	 *
	 * @param pub is the publication authorship.
	 */
	@Transactional
	public void deleteAuthorship(Authorship pub) {
		if (this.authorships != null) {
			this.authorships.remove(pub);
		}
	}

	/** Delete all the publications for the person.
	 */
	@Transactional
	public void deleteAllAuthorships() {
		if (this.authorships != null) {
			this.authorships.clear();
		}
	}

	/** Replies the URL of the person on {@code academia.edu}.
	 *
	 * @return the URL.
	 */
	public String getAcademiaURL() {
		return this.academiaURL;
	}

	/** Replies the URL of the person on {@code academia.edu}.
	 *
	 * @return the URL.
	 */
	public final URL getAcademiaURLObject() {
		if (!Strings.isNullOrEmpty(getAcademiaURL())) {
			try {
				return new URL(getAcademiaURL());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Change the URL of the person on {@code academia.edu}.
	 *
	 * @param url the URL.
	 */
	public void setAcademiaURL(String url) {
		this.academiaURL = Strings.emptyToNull(url);
	}

	/** Replies the URL of the person on {@code cordis.europa.eu}.
	 *
	 * @return the URL.
	 */
	public String getCordisURL() {
		return this.cordisURL;
	}

	/** Replies the URL of the person on {@code cordis.europa.eu}.
	 *
	 * @return the URL.
	 */
	public final URL getCordisURLObject() {
		if (!Strings.isNullOrEmpty(getAcademiaURL())) {
			try {
				return new URL(getCordisURL());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Change the URL of the person on {@code cordis.europa.eu}.
	 *
	 * @param url the URL.
	 */
	public void setCordisURL(String url) {
		this.cordisURL = Strings.emptyToNull(url);
	}

	/** Replies the URL of the person on DBLP.
	 *
	 * @return the URL.
	 */
	public String getDblpURL() {
		return this.dblpURL;
	}

	/** Replies the URL of the person on DBLP.
	 *
	 * @return the URL.
	 */
	public final URL getDblpURLObject() {
		if (!Strings.isNullOrEmpty(getDblpURL())) {
			try {
				return new URL(getDblpURL());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Change the URL of the person on DBLP.
	 *
	 * @param url the URL.
	 */
	public void setDblpURL(String url) {
		this.dblpURL = Strings.emptyToNull(url);
	}

	/** Replies the identifier of the person on Facebook.
	 *
	 * @return the identifier.
	 */
	public String getFacebookId() {
		return this.facebookId;
	}

	/** Replies the URL of the person on Facebook.
	 *
	 * @return the URL.
	 */
	public final URL getFacebookURL() {
		if (!Strings.isNullOrEmpty(getFacebookId())) {
			try {

				return new URL(FACEBOOK_URL + getFacebookId());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Change the identifier of the person on Facebook.
	 *
	 * @param id the identifier.
	 */
	public void setFacebookId(String id) {
		this.facebookId = Strings.emptyToNull(id);
	}

	/** Replies the identifier of the person on Github.
	 *
	 * @return the identifier.
	 */
	public String getGithubId() {
		return this.githubId;
	}

	/** Replies the URL of the person on Github.
	 *
	 * @return the URL.
	 * @see #getGithubAvatarURL()
	 */
	public final URL getGithubURL() {
		if (!Strings.isNullOrEmpty(getGithubId())) {
			try {

				return new URL(GITHUB_URL + getGithubId());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Replies the URL of the person's avatar on Github.
	 *
	 * @return the URL.
	 * @see #getGithubURL()
	 * @see #getPhotoURL()
	 */
	public final URL getGithubAvatarURL() {
		if (!Strings.isNullOrEmpty(getGithubId())) {
			try {

				return new URL(GITHUB_AVATAR_URL + getGithubId());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Replies the URL of the person's avatar on Github with a specific size.
	 *
	 * @param size the expecting size of the photo, in pixels.
	 * @return the URL.
	 * @see #getGithubURL()
	 * @see #getPhotoURL()
	 */
	public final URL getGithubAvatarURL(int size) {
		if (!Strings.isNullOrEmpty(getGithubId())) {
			try {

				return new URL(GITHUB_AVATAR_URL + getGithubId() + "?" + GITHUB_AVATAR_SIZE_PARAM + "=" + Integer.toString(size)); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Change the identifier of the person on Github.
	 *
	 * @param id the identifier.
	 */
	public void setGithubId(String id) {
		this.githubId = Strings.emptyToNull(id);
	}

	/** Replies the identifier of the person on LinkedIn.
	 *
	 * @return the identifier.
	 */
	public String getLinkedInId() {
		return this.linkedInId;
	}

	/** Replies the URL of the person on LinkedIn.
	 *
	 * @return the URL.
	 */
	public final URL getLinkedInURL() {
		if (!Strings.isNullOrEmpty(getLinkedInId())) {
			try {

				return new URL(LINKEDIN_URL + getLinkedInId());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Change the identifier of the person on LinkedIn.
	 *
	 * @param id the identifier.
	 */
	public void setLinkedInId(String id) {
		this.linkedInId = Strings.emptyToNull(id);
	}

	/** Replies the ResearcherID of the person.
	 *
	 * @return the identifier.
	 */
	public String getResearcherId() {
		return this.researcherId;
	}

	/** Replies the URL of the person on Plubon.
	 *
	 * @return the URL.
	 */
	public final URL getResearcherIdURL() {
		if (!Strings.isNullOrEmpty(getResearcherId())) {
			try {

				return new URL(RESEARCHERID_URL + getResearcherId());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Change the ResearcherID of the person.
	 *
	 * @param id the identifier.
	 */
	public void setResearcherId(String id) {
		this.researcherId = Strings.emptyToNull(id);
	}

	/** Replies the Google Scholar ID of the person.
	 *
	 * @return the identifier.
	 */
	public String getGoogleScholarId() {
		return this.googleScholarId;
	}

	/** Replies the URL of the person on Google Scholar.
	 *
	 * @return the URL.
	 */
	public final URL getGoogleScholarURL() {
		if (!Strings.isNullOrEmpty(getGoogleScholarId())) {
			try {

				return new URL(GOOGLESCHOLAR_URL + getGoogleScholarId());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Replies the URL of the person's avatar on Google scholar.
	 *
	 * @return the URL.
	 * @see #getGoogleScholarURL()
	 * @see #getPhotoURL()
	 */
	public final URL getGoogleScholarAvatarURL() {
		if (!Strings.isNullOrEmpty(getGoogleScholarId())) {
			try {
				return new URL(GOOGLE_SCHOLAR_AVATAR_URL + getGoogleScholarId());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Replies the URL of the person's avatar on Google Scholar with a specific size.
	 *
	 * @param size the expecting size of the photo, in pixels.
	 * @return the URL.
	 * @see #getGoogleScholarURL()
	 * @see #getPhotoURL()
	 */
	public final URL getGoogleScholarAvatarURL(int size) {
		if (!Strings.isNullOrEmpty(getGoogleScholarId())) {
			try {
				return new URL(GOOGLE_SCHOLAR_AVATAR_URL + getGoogleScholarId() + "&" + GOOGLE_SCHOLAR_AVATAR_SIZE_PARAM + "=" + Integer.toString(size)); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Change the Google Scholar Id of the person.
	 *
	 * @param id the identifier.
	 */
	public void setGoogleScholarId(String id) {
		this.googleScholarId = Strings.emptyToNull(id);
	}

	/** Replies the identifier of the person on ResearchGate.
	 *
	 * @return the identifier.
	 */
	public String getResearchGateId() {
		return this.researchGateId;
	}

	/** Replies the URL of the person on ResearchGate.
	 *
	 * @return the URL.
	 */
	public final URL getResearchGateURL() {
		if (!Strings.isNullOrEmpty(getResearchGateId())) {
			try {

				return new URL(RESEARCHGATE_URL + getResearchGateId());
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Change the identifier of the person on ResearchGate.
	 *
	 * @param id the identifier.
	 */
	public void setResearchGateId(String id) {
		this.researchGateId = Strings.emptyToNull(id);
	}

	/** Replies the H-index of the person provided by Google Scholar.
	 *
	 * @return the h-index.
	 */
	public int getGoogleScholarHindex() {
		return this.googleScholarHindex;
	}

	/** Change the H-index of the person provided by Google Scholar.
	 *
	 * @param hindex the H-index.
	 */
	public void setGoogleScholarHindex(int hindex) {
		if (hindex < 0) {
			this.googleScholarHindex = 0;
		} else {
			this.googleScholarHindex = hindex;
		}
	}

	/** Change the H-index of the person provided by Google Scholar.
	 *
	 * @param hindex the H-index.
	 */
	public final void setGoogleScholarHindex(Number hindex) {
		if (hindex == null) {
			setGoogleScholarHindex(0);
		} else {
			setGoogleScholarHindex(hindex.intValue());
		}
	}

	/** Replies the H-index of the person provided by Web-of-Science.
	 *
	 * @return the h-index.
	 */
	public int getWosHindex() {
		return this.wosHindex;
	}

	/** Change the H-index of the person provided by Web-of-Science.
	 *
	 * @param hindex the H-index.
	 */
	public void setWosHindex(int hindex) {
		if (hindex < 0) {
			this.wosHindex = 0;
		} else {
			this.wosHindex = hindex;
		}
	}

	/** Change the H-index of the person provided by Web-of-Science.
	 *
	 * @param hindex the H-index.
	 */
	public final void setWosHindex(Number hindex) {
		if (hindex == null) {
			setWosHindex(0);
		} else {
			setWosHindex(hindex.intValue());
		}
	}

	/** Replies the identifier of the person provided by Gravatar.
	 *
	 * @return the identifier.
	 */
	public String getGravatarId() {
		return this.gravatarId;
	}

	/** Replies the URL to the photo of the person that is provided by Gravatar.
	 *
	 * @return the URL, or {@code null} if none.
	 * @see #getPhotoURL()
	 */
	public final URL getGravatarURL() {
		final String id = getGravatarId();
		if (!Strings.isNullOrEmpty(id)) {
			try {
				return new URL(GRAVATAR_URL + id);
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Replies the URL to the photo of the person that is provided by Gravatar.
	 *
	 * @param size the expecting size of the photo, in pixels.
	 * @return the URL, or {@code null} if none.
	 * @see #getPhotoURL(int)
	 */
	public final URL getGravatarURL(int size) {
		final String id = getGravatarId();
		if (size > 0 && !Strings.isNullOrEmpty(id)) {
			try {
				return new URL(GRAVATAR_URL + id + "?" + GRAVATAR_SIZE_PARAM + "=" + Integer.toString(size)); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Change the identifier of the person provided by Gravatar.
	 *
	 * @param identifier the identifier.
	 */
	public void setGravatarId(String identifier) {
		this.gravatarId = Strings.emptyToNull(identifier);
	}

	/** Replies the office phone number of the person. This phone number is supposed to follows the international
	 * standards
	 *
	 * @return the number.
	 */
	public String getOfficePhone() {
		return this.officePhone;
	}

	/** Change the office phone number of the person. This phone number is supposed to follows the international
	 * standards
	 *
	 * @param number the number.
	 */
	public void setOfficePhone(String number) {
		this.officePhone = Strings.emptyToNull(number);
	}

	/** Replies the mobile phone number of the person. This phone number is supposed to follows the international
	 * standards
	 *
	 * @return the number.
	 */
	public String getMobilePhone() {
		return this.mobilePhone;
	}

	/** Change the mobile phone number of the person. This phone number is supposed to follows the international
	 * standards
	 *
	 * @param number the number.
	 */
	public void setMobilePhone(String number) {
		this.mobilePhone = Strings.emptyToNull(number);
	}

	/** Replies the preferred civil title for this person. This civil title is not stored and computed based
	 * on the values replied by {@link #getActiveOrFinishedMemberships()} and {@link #getGender()}.
	 * The language of the title depends on the current locale.
	 *
	 * @return the civil title, or {@code null} if none.
	 * @see #getCivilTitle(Locale)
	 * @see #getCivilTitle(boolean)
	 * @see #getCivilTitle(boolean, Locale)
	 */
	public final String getCivilTitle() {
		return getCivilTitle(true);
	}

	/** Replies the preferred civil title for this person. This civil title is not stored and computed based
	 * on the values replied by {@link #getActiveOrFinishedMemberships()} and {@link #getGender()}.
	 *
	 * @param locale the locale to use for generating the civil title.
	 * @return the civil title, or {@code null} if none.
	 * @see #getCivilTitle()
	 * @see #getCivilTitle(boolean)
	 * @see #getCivilTitle(boolean, Locale)
	 */
	public final String getCivilTitle(Locale locale) {
		return getCivilTitle(true);
	}

	/** Replies the preferred civil title for this person. This civil title is not stored and computed based
	 * on the values replied by {@link #getActiveOrFinishedMemberships()}, and, if {@code includeGenderTitle}
	 * is evaluated to {@code true}, the value replied by {@link #getGender()}.
	 * The language of the title depends on the current locale.
	 *
	 * @param includeGenderTitle indicates if the gender title should be consider and eventually replied.
	 * @return the civil title, or {@code null} if none.
	 * @see #getCivilTitle(Locale)
	 * @see #getCivilTitle()
	 * @see #getCivilTitle(boolean, Locale)
	 */
	public String getCivilTitle(boolean includeGenderTitle) {
		final MemberStatus status = findHigherMemberStatus();
		String title = null;
		if (status != null) {
			title = status.getCivilTitle();
		}
		if (includeGenderTitle && Strings.isNullOrEmpty(title)) {
			title = getGender().getCivilTitle();
		}
		return Strings.emptyToNull(title);
	}

	/** Replies the preferred civil title for this person. This civil title is not stored and computed based
	 * on the values replied by {@link #getActiveOrFinishedMemberships()}, and, if {@code includeGenderTitle}
	 * is evaluated to {@code true}, the value replied by {@link #getGender()}.
	 *
	 * @param includeGenderTitle indicates if the gender title should be consider and eventually replied.
	 * @param locale the locale to use for generating the civil title.
	 * @return the civil title, or {@code null} if none.
	 * @see #getCivilTitle()
	 * @see #getCivilTitle(boolean)
	 * @see #getCivilTitle(Locale)
	 */
	public String getCivilTitle(boolean includeGenderTitle, Locale locale) {
		final MemberStatus status = findHigherMemberStatus();
		String title = null;
		if (status != null) {
			title = status.getCivilTitle(locale);
		}
		if (includeGenderTitle && Strings.isNullOrEmpty(title)) {
			title = getGender().getCivilTitle(locale);
		}
		return Strings.emptyToNull(title);
	}

	private MemberStatus findHigherMemberStatus() {
		final Optional<MemberStatus> status = getActiveOrFinishedMemberships().values().stream().map(it -> it.getMemberStatus())
				.min((a, b) -> {
					final int cmp = Integer.compare(a.getHierachicalLevel(), b.getHierachicalLevel());
					if (cmp != 0) {
						return cmp;
					}
					return b.compareTo(a);
				});
		if (status.isEmpty()) {
			return null;
		}
		return status.get();
	}


	/** Replies the URL to the photo of the person that is provided by one of the sources (Gravatar, Github, ...).
	 *
	 * @return the URL, or {@code null} if none.
	 * @see #getGravatarURL()
	 * @see #getGithubAvatarURL()
	 * @see #getGoogleScholarAvatarURL()
	 */
	public final URL getPhotoURL() {
		URL url = getGravatarURL();
		if (url != null) {
			return url;
		}
		url = getGithubAvatarURL();
		if (url != null) {
			return url;
		}
		url = getGoogleScholarAvatarURL();
		if (url != null) {
			return url;
		}
		return null;
	}

	/** Replies the URL to the photo of the person that is provided by one of the sources (Gravatar, Github, ...).
	 *
	 * @param size the expecting size of the photo, in pixels.
	 * @return the URL, or {@code null} if none.
	 * @see #getGravatarURL(int)
	 * @see #getGithubAvatarURL(int)
	 * @see #getGoogleScholarAvatarURL(int)
	 */
	public final URL getPhotoURL(int size) {
		URL url = getGravatarURL(size);
		if (url != null) {
			return url;
		}
		url = getGithubAvatarURL(size);
		if (url != null) {
			return url;
		}
		url = getGoogleScholarAvatarURL(size);
		if (url != null) {
			return url;
		}
		return null;
	}

}
