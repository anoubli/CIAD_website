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
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.utils.HashCodeUtils;

/** Address of an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Entity
@Table(name = "OrgAddresses")
public class OrganizationAddress implements Serializable, JsonSerializable, Comparable<OrganizationAddress>, AttributeProvider, IdentifiableEntity {

	private static final long serialVersionUID = 6630654880175784574L;

	/** Identifier of the address.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Symbolic name of the address.
	 */
	@Column(nullable = false)
	private String name;

	/** Complementatry information that may appear before the rest.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String complement;

	/** Building number and street in the address.
	 */
	@Column(nullable = false, length = EntityUtils.LARGE_TEXT_SIZE)
	private String street;

	/** The ZIP code of the address.
	 */
	@Column
	private String zipCode;

	/** City.
	 */
	@Column(nullable = false)
	private String city;

	/** Coordinates of the address on a map tool such as Google Map..
	 */
	@Column
	private String mapCoordinates;	

	/** Link to the Google Map
	 */
	@Column
	private String googleMapLink;	

	/** URL of a associated background if the address has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String pathToBackgroundImage;

	/** Construct an organization address from the given values.
	 * 
	 * @param id the identifier of the organization.
	 * @param name the symbolic name of the address.
	 * @param complement the complementary information that may appear before the rest of the address information.
	 * @param street the building number and street name.
	 * @param zipCode the ZIP code.
	 * @param city the city.
	 * @param mapCoordinates the coordinates for a mapping tool.
	 */
	public OrganizationAddress(int id, String name, String complement, String street, String zipCode, String city, String mapCoordinates) {
		this.id = id;
		this.name = name;
		this.complement = complement;
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
		this.mapCoordinates = mapCoordinates;
	}

	/** Construct an empty organization address.
	 */
	public OrganizationAddress() {
		//
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.name);
		h = HashCodeUtils.add(h, this.complement);
		h = HashCodeUtils.add(h, this.street);
		h = HashCodeUtils.add(h, this.zipCode);
		h = HashCodeUtils.add(h, this.city);
		h = HashCodeUtils.add(h, this.mapCoordinates);
		h = HashCodeUtils.add(h, this.pathToBackgroundImage);
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
		final OrganizationAddress other = (OrganizationAddress) obj;
		if (this.id != other.id) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.complement, other.complement)) {
			return false;
		}
		if (!Objects.equals(this.street, other.street)) {
			return false;
		}
		if (!Objects.equals(this.zipCode, other.zipCode)) {
			return false;
		}
		if (!Objects.equals(this.city, other.city)) {
			return false;
		}
		if (!Objects.equals(this.mapCoordinates, other.mapCoordinates)) {
			return false;
		}
		if (!Objects.equals(this.pathToBackgroundImage, other.pathToBackgroundImage)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(OrganizationAddress o) {
		return EntityUtils.getPreferredOrganizationAddressComparator().compare(this, o);
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
		if (!Strings.isNullOrEmpty(getName())) {
			consumer.accept("name", getName()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getComplement())) {
			consumer.accept("complementary", getComplement()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getStreet())) {
			consumer.accept("street", getStreet()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getZipCode())) {
			consumer.accept("zipCode", getZipCode()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getCity())) {
			consumer.accept("city", getCity()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getMapCoordinates())) {
			consumer.accept("mapCoordinates", getMapCoordinates()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getMapCoordinates())) {
			consumer.accept("googleMapLink", getGoogleMapLink()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPathToBackgroundImage())) {
			consumer.accept("pathToBackgroundImage", getPathToBackgroundImage()); //$NON-NLS-1$
		}
	}

	@Override
	public void serialize(JsonGenerator generator, SerializerProvider serializers) throws IOException {
		generator.writeStartObject();
		forEachAttribute((attrName, attrValue) -> {
			JsonUtils.writeField(generator, attrName, attrValue);
		});
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

	/** Replies the complementary information that may appear before the other information.
	 *
	 * @return the complementary information.
	 */
	public String getComplement() {
		return this.complement;
	}

	/** Change the complementary information that may appear before the other information.
	 *
	 * @param complement the complementary information.
	 */
	public void setComplement(String complement) {
		this.complement = Strings.emptyToNull(complement);
	}

	/** Replies the building number and street of the address.
	 *
	 * @return the building number and street.
	 */
	public String getStreet() {
		return this.street;
	}

	/** Change the building number and street of the address.
	 *
	 * @param street the building number and street.
	 */
	public void setStreet(String street) {
		this.street = Strings.emptyToNull(street);
	}

	/** Replies the ZIP code of the address.
	 *
	 * @return the ZIP code.
	 */
	public String getZipCode() {
		return this.zipCode;
	}

	/** Change the ZIP code of the address.
	 *
	 * @param zipCode the ZIP code.
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = Strings.emptyToNull(zipCode);
	}

	/** Replies the city of the address.
	 *
	 * @return the city.
	 */
	public String getCity() {
		return this.city;
	}

	/** Change the city of the address.
	 *
	 * @param city the city.
	 */
	public void setCity(String city) {
		this.city = Strings.emptyToNull(city);
	}

	/** Replies the coordinates of the address for a mapping system. There coordinates are usually GPS based.
	 *
	 * @return the map coordinates.
	 * @see #getGoogleMapURL()
	 */
	public String getMapCoordinates() {
		return this.mapCoordinates;
	}

	/** Changes the coordinates of the address for a mapping system. There coordinates are usually GPS based.
	 *
	 * @param mapCoordinates the map coordinates.
	 */
	public void setMapCoordinates(String mapCoordinates) {
		this.mapCoordinates = Strings.emptyToNull(mapCoordinates);
	}

	/** Replies the Link to the Google Map that corresponds to this address.
	 *
	 * @return the link to the Google Map of the research organization, or {@code null}.
	 * @see #getMapCoordinates()
	 * @see #getGoogleMapURL()
	 */
	public String getGoogleMapLink() {
		return this.googleMapLink;
	}

	/** Change the Link to the Google Map that corresponds to this address.
	 *
	 * @param link the link to the Google Map of the research organization, or {@code null}.
	 */
	public void setGoogleMapLink(String link) {
		this.googleMapLink = Strings.emptyToNull(link);
	}

	/** Replies the URL to the Google Map that corresponds to this address.
	 *
	 * @return the URL to the Google Map of the research organization, or {@code null}.
	 * @see #getMapCoordinates()
	 * @throws MalformedURLException if the URL cannot be built.
	 */
	public URL getGoogleMapURL() throws MalformedURLException {
		final String link = getGoogleMapLink();
		if (!Strings.isNullOrEmpty(link)) {
			return new URL(link);
		}
		return null;
	}

	/** Replies the full address composed by the building number, the street, the ZIP code and the city.
	 *
	 * @return the full address.
	 */
	public String getFullAddress() {
		String separator = " "; //$NON-NLS-1$
		final StringBuilder builder = new StringBuilder();
		if (!Strings.isNullOrEmpty(getComplement())) {
			builder.append(getComplement());
			separator = ", "; //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getStreet())) {
			if (builder.length() > 0) {
				builder.append(separator);
				separator = " "; //$NON-NLS-1$
			}
			builder.append(getStreet());
		}
		if (!Strings.isNullOrEmpty(getZipCode())) {
			if (builder.length() > 0) {
				builder.append(separator);
				separator = " "; //$NON-NLS-1$
			}
			builder.append(getZipCode());
		}
		if (!Strings.isNullOrEmpty(getCity())) {
			if (builder.length() > 0) {
				builder.append(separator);
				separator = " "; //$NON-NLS-1$
			}
			builder.append(getCity());
		}
		return builder.toString();
	}

	/** Replies the background image for the address.
	 *
	 * @return the background image.
	 */
	public String getPathToBackgroundImage() {
		return this.pathToBackgroundImage;
	}

	/** Change the city of the address.
	 *
	 * @param path the local path to the background image.
	 */
	public void setPathToBackgroundImage(String path) {
		this.pathToBackgroundImage = Strings.emptyToNull(path);
	}

}
