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

package fr.ciadlab.labmanager.entities.publication.type;

import java.io.IOException;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.RequiredFieldInForm;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Document that is of an unspecified type.
 *
 * <p>This type is equivalent to the BibTeX types: {@code misc}.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "MiscDocuments")
@PrimaryKeyJoinColumn(name = "id")
public class MiscDocument extends Publication {

	private static final long serialVersionUID = 2168691698481647036L;

	@Column
	private String organization;

	@Column
	private String address;

	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String howPublished;

	@Column
	private String publisher;

	@Column
	private String documentNumber;

	@Column
	private String documentType;

	/** Construct a misc document with the given values.
	 *
	 * @param publication the publication to copy.
	 * @param organization the name of the organization that has published the document.
	 * @param address the geographical location of the organization that has published the document. It is usually a city, country pair.
	 * @param howPublished a description of how the document is published. 
	 * @param publisher the name of the publisher if any.
	 * @param number the number that is attached to the document.
	 * @param type a description of the type of document.
	 */
	public MiscDocument(Publication publication, String organization, String address,
			String howPublished, String publisher, String number, String type) {
		super(publication);
		this.organization = organization;
		this.address = address;
		this.howPublished = howPublished;
		this.publisher = publisher;
		this.documentNumber = number;
		this.documentType = type;
	}

	/** Construct an empty misc document.
	 */
	public MiscDocument() {
		//
	}

	@Override
	public int hashCode() {
		int h = super.hashCode();
		h = HashCodeUtils.add(h, this.organization);
		h = HashCodeUtils.add(h, this.address);
		h = HashCodeUtils.add(h, this.howPublished);
		h = HashCodeUtils.add(h, this.publisher);
		h = HashCodeUtils.add(h, this.documentNumber);
		h = HashCodeUtils.add(h, this.documentType);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		final MiscDocument other = (MiscDocument) obj;
		if (!Objects.equals(this.organization, other.organization)) {
			return false;
		}
		if (!Objects.equals(this.address, other.address)) {
			return false;
		}
		if (!Objects.equals(this.howPublished, other.howPublished)) {
			return false;
		}
		if (!Objects.equals(this.publisher, other.publisher)) {
			return false;
		}
		if (!Objects.equals(this.documentNumber, other.documentNumber)) {
			return false;
		}
		if (!Objects.equals(this.documentType, other.documentType)) {
			return false;
		}
		return true;
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		super.forEachAttribute(consumer);
		if (!Strings.isNullOrEmpty(getOrganization())) {
			consumer.accept("organization", getOrganization()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			consumer.accept("address", getAddress()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getHowPublished())) {
			consumer.accept("howPublished", getHowPublished()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getDocumentNumber())) {
			consumer.accept("documentNumber", getDocumentNumber()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getDocumentType())) {
			consumer.accept("documentType", getDocumentType()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPublisher())) {
			consumer.accept("publisher", getPublisher()); //$NON-NLS-1$
		}
	}

	@Override
	@JsonIgnore
	public String getWherePublishedShortDescription() {
		final StringBuilder buf = new StringBuilder();
		buf.append(getHowPublished());
		final boolean b0 = !Strings.isNullOrEmpty(getDocumentNumber());
		final boolean b1 = !Strings.isNullOrEmpty(getDocumentType());
		if (b0 && b1) {
			buf.append(", n. "); //$NON-NLS-1$
			buf.append(getDocumentNumber());
			buf.append(" ("); //$NON-NLS-1$
			buf.append(getDocumentType());
			buf.append(")"); //$NON-NLS-1$
		} else if (b0) {
			buf.append(", n. "); //$NON-NLS-1$
			buf.append(getDocumentNumber());
		} else if (b1) {
			buf.append(" ("); //$NON-NLS-1$
			buf.append(getDocumentType());
			buf.append(")"); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getOrganization())) {
			buf.append(", "); //$NON-NLS-1$
			buf.append(getOrganization());
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			buf.append(", "); //$NON-NLS-1$
			buf.append(getAddress());
		}
		if (!Strings.isNullOrEmpty(getPublisher())) {
			buf.append(", "); //$NON-NLS-1$
			buf.append(getPublisher());
		}
		if (!Strings.isNullOrEmpty(getISBN())) {
			buf.append(", ISBN "); //$NON-NLS-1$
			buf.append(getISBN());
		}
		if (!Strings.isNullOrEmpty(getISSN())) {
			buf.append(", ISSN "); //$NON-NLS-1$
			buf.append(getISSN());
		}
		return buf.toString();
	}

	@Override
	public String getPublicationTarget() {
		final StringBuilder buf = new StringBuilder();
		buf.append(getHowPublished());
		if (!Strings.isNullOrEmpty(getPublisher())) {
			buf.append(", "); //$NON-NLS-1$
			buf.append(getPublisher());
		}
		return buf.toString();
	}

	/** Replies the name of the organization that has published the document.
	 *
	 * @return the name of the organization.
	 */
	public String getOrganization() {
		return this.organization;
	}

	/** Chage the name of the organization that has published the document.
	 *
	 * @param name the name of the organization.
	 */
	public void setOrganization(String name) {
		this.organization = Strings.emptyToNull(name);
	}

	/** Replies the geographical address where the document was published. It is usually a city and a country.
	 *
	 * @return the address.
	 */
	public String getAddress() {
		return this.address;
	}

	/** Change the geographical address where the document was published. It is usually a city and a country.
	 *
	 * @param address the address.
	 */
	public void setAddress(String address) {
		this.address = Strings.emptyToNull(address);
	}

	/** Replies the type of document.
	 *
	 * @return the type description.
	 */
	public String getDocumentType() {
		return this.documentType;
	}

	/** Change the type of document.
	 *
	 * @param type the type description.
	 */
	public void setDocumentType(String type) {
		this.documentType = Strings.emptyToNull(type);
	}

	/** Replies the number that was assigned by the organization to the document.
	 *
	 * @return the document number.
	 */
	public String getDocumentNumber() {
		return this.documentNumber;
	}

	/** Replies the number that was assigned by the organization to the document.
	 *
	 * @param number the document number.
	 */
	public void setDocumentNumber(String number) {
		this.documentNumber = Strings.emptyToNull(number);
	}

	/** Replies a description on how the document was published.
	 *
	 * @return the description of the publication means.
	 */
	@RequiredFieldInForm
	public String getHowPublished() {
		return this.howPublished;
	}

	/** Change the description on how the document was published.
	 *
	 * @param description the description of the publication means.
	 */
	public void setHowPublished(String description) {
		this.howPublished = Strings.emptyToNull(description);
	}

	/** Replies the name of the publisher of the document.
	 *
	 * @return the name of the publisher.
	 */
	public String getPublisher() {
		return this.publisher;
	}

	/** Change the name of the publisher of the document.
	 *
	 * @param name the name of the publisher.
	 */
	public void setPublisher(String name) {
		this.publisher = Strings.emptyToNull(name);
	}

	@Override
	public boolean isRanked() {
		return false;
	}

}


