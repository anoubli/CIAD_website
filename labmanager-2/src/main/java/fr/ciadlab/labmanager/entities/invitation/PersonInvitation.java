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

package fr.ciadlab.labmanager.entities.invitation;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.arakhne.afc.util.CountryCode;

/** Description of an invitation for a person.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@Entity
@Table(name = "PersonInvitations")
public class PersonInvitation implements Serializable, AttributeProvider, Comparable<PersonInvitation>, IdentifiableEntity {

	private static final long serialVersionUID = -6587396903933519629L;

	/** Identifier of the jury.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Reference to the person who is invited.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Person guest;

	/** Reference to the person who invited.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Person inviter;

	/** Type of person invitation.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private PersonInvitationType type;

	/** Start date of the invitation.
	 */
	@Column
	private LocalDate startDate;

	/** End date of the invitation.
	 */
	@Column
	private LocalDate endDate;

	/** Name of the university of the person who is invited, or of the target university.
	 */
	@Column
	private String university;

	/** The country of the other university.
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CountryCode country = CountryCodeUtils.DEFAULT;

	/** Title of the works.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String title;

	/** Construct an invitation with the given values.
	 *
	 * @param invitee the person who is invited.
	 * @param inviter the person who invites.
	 * @param type the type of invitation.
	 * @param startDate the start date of the invitation.
	 * @param endDate the start date of the invitation.
	 * @param title the title of the evaluated works.
	 * @param university the name of the university of the candidate.
	 * @param country the country of the university.
	 */
	public PersonInvitation(Person invitee, Person inviter, PersonInvitationType type, LocalDate startDate, LocalDate endDate,
			String title, String university, CountryCode country) {
		this.guest = invitee;
		this.inviter = inviter;
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.title = title;
		this.university = university;
		this.country = country;
	}

	/** Construct an empty invitation.
	 */
	public PersonInvitation() {
		//
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.guest);
		h = HashCodeUtils.add(h, this.inviter);
		h = HashCodeUtils.add(h, this.type);
		h = HashCodeUtils.add(h, this.startDate);
		h = HashCodeUtils.add(h, this.endDate);
		h = HashCodeUtils.add(h, this.title);
		h = HashCodeUtils.add(h, this.university);
		h = HashCodeUtils.add(h, this.country);
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
		final PersonInvitation other = (PersonInvitation) obj;
		if (!Objects.equals(this.guest, other.guest)) {
			return false;
		}
		if (!Objects.equals(this.inviter, other.inviter)) {
			return false;
		}
		if (!Objects.equals(this.type, other.type)) {
			return false;
		}
		if (!Objects.equals(this.startDate, other.startDate)) {
			return false;
		}
		if (!Objects.equals(this.endDate, other.endDate)) {
			return false;
		}
		if (!Objects.equals(this.title, other.title)) {
			return false;
		}
		if (!Objects.equals(this.university, other.university)) {
			return false;
		}
		if (!Objects.equals(this.country, other.country)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(PersonInvitation o) {
		return EntityUtils.getPreferredPersonInvitationComparator().compare(this, o);
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (getGuest() != null) {
			consumer.accept("guest", getGuest()); //$NON-NLS-1$
		}
		if (getInviter() != null) {
			consumer.accept("inviter", getInviter()); //$NON-NLS-1$
		}
		if (getType() != null) {
			consumer.accept("type", getType()); //$NON-NLS-1$
		}
		if (getStartDate() != null) {
			consumer.accept("startDate", getStartDate()); //$NON-NLS-1$
		}
		if (getEndDate() != null) {
			consumer.accept("endDate", getEndDate()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getTitle())) {
			consumer.accept("title", getTitle()); //$NON-NLS-1$
		}
		if (getUniversity() != null) {
			consumer.accept("university", getUniversity()); //$NON-NLS-1$
		}
		if (getCountry() != null) {
			consumer.accept("country", getCountry()); //$NON-NLS-1$
			consumer.accept("countryLabel", getCountryDisplayName()); //$NON-NLS-1$
		}
	}

	@Override
	public int getId() {
		return this.id;
	}

	/** Change the membership identifier.
	 *
	 * @param id the identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** Replies the guest.
	 *
	 * @return the guest.
	 */
	public Person getGuest() {
		return this.guest;
	}

	/** Change the guest related to this jury.
	 *
	 * @param person the guest.
	 */
	public void setGuest(Person person) {
		this.guest = person;
	}

	/** Replies the inviter.
	 *
	 * @return the inviter.
	 */
	public Person getInviter() {
		return this.inviter;
	}

	/** Change the inviter related to this jury.
	 *
	 * @param person the inviter.
	 */
	public void setInviter(Person person) {
		this.inviter = person;
	}

	/** Replies the type of invitation.
	 *
	 * @return the type.
	 */
	public PersonInvitationType getType() {
		return this.type;
	}

	/** Change the type of invitation.
	 *
	 * @param type the type.
	 */
	public void setType(PersonInvitationType type) {
		this.type = type;
	}

	/** Change the type of membership in the jury.
	 *
	 * @param type the type.
	 */
	public final void setType(String type) {
		if (Strings.isNullOrEmpty(type)) {
			setType((PersonInvitationType) null);
		} else {
			setType(PersonInvitationType.valueOfCaseInsensitive(type));
		}
	}

	/** Replies the start date of the invitation.
	 *
	 * @return the start date of the invitation.
	 */
	public LocalDate getStartDate() {
		return this.startDate;
	}

	/** Change the start date of the invitation.
	 *
	 * @param date the start date of the invitation.
	 */
	public void setStartDate(LocalDate date) {
		this.startDate = date;
	}

	/** Change the start date of the invitation.
	 *
	 * @param date the start date of the invitation.
	 */
	public final void setStartDate(String date) {
		if (Strings.isNullOrEmpty(date)) {
			setStartDate((LocalDate) null);
		} else {
			setStartDate(LocalDate.parse(date));
		}
	}

	/** Replies the end date of the invitation.
	 *
	 * @return the end date of the invitation.
	 */
	public LocalDate getEndDate() {
		return this.endDate;
	}

	/** Change the end date of the invitation.
	 *
	 * @param date the end date of the invitation.
	 */
	public void setEndDate(LocalDate date) {
		this.endDate = date;
	}

	/** Change the end date of the invitation.
	 *
	 * @param date the end date of the invitation.
	 */
	public final void setEndDate(String date) {
		if (Strings.isNullOrEmpty(date)) {
			setEndDate((LocalDate) null);
		} else {
			setEndDate(LocalDate.parse(date));
		}
	}

	/** Replies the title of the evaluated works.
	 *
	 * @return the title of the evaluated works.
	 */
	public String getTitle() {
		return this.title;
	}

	/** Change the title of the evaluated works.
	 *
	 * @param title the title of the evaluated works.
	 */
	public void setTitle(String title) {
		this.title = Strings.emptyToNull(title);
	}

	/** Replies the university who passes the exam.
	 *
	 * @return the name of the candidate's university.
	 */
	public String getUniversity() {
		return this.university;
	}

	/** Change the name of the university of the candidate.
	 *
	 * @param university the name of the university.
	 */
	public void setUniversity(String university) {
		this.university = Strings.emptyToNull(university);
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
			this.country = CountryCodeUtils.DEFAULT;
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

	/** Replies the localization key for the long label that corresponds to the type of this person invitation.
	 *
	 * @return the key.
	 * @see #getAllLongTypeLabelKeys
	 */
	public String getLongTypeLabelKey() {
		return buildLongTypeLabelKey(getType(), CountryCodeUtils.isFrance(getCountry()));
	}

	private static String buildLongTypeLabelKey(PersonInvitationType type, boolean isFrance) {
		final StringBuilder key = new StringBuilder("personInvitation."); //$NON-NLS-1$
		key.append(type.name());
		if (isFrance) {
			key.append("_fr"); //$NON-NLS-1$
		} else {
			key.append("_other"); //$NON-NLS-1$
		}
		return key.toString();
	}

	/** Replies all the localization key for the long label that corresponds to the type of a person invitation.
	 * The keys are provided from the most important to the less important.
	 *
	 * @return the ordering index of each key.
	 * @see #getLongTypeLabelKey
	 */
	public static Map<String, Integer> getAllLongTypeLabelKeys() {
		final Map<String, Integer> keys = new TreeMap<>();
		final MutableInt index = new MutableInt();
		buildLongTypeLabelKeys(keys, index, false, false);
		buildLongTypeLabelKeys(keys, index, false, true);
		buildLongTypeLabelKeys(keys, index, true, false);
		buildLongTypeLabelKeys(keys, index, true, true);
		return keys;
	}

	private static void buildLongTypeLabelKeys(Map<String, Integer> keys, MutableInt index, boolean outgoing, boolean isFrance) {
		for (final PersonInvitationType type : PersonInvitationType.values()) {
			if (type.isOutgoing() == outgoing) {
				final String key = buildLongTypeLabelKey(type, isFrance);
				keys.put(key, index.getValue());
				index.increment();
			}
		}
	}

}
