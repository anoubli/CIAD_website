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
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.RequiredFieldInForm;
import org.apache.jena.ext.com.google.common.base.Strings;

/** A patent.
 *
 * <p>This type has no equivalent in the BibTeX types. It is inspired from: {@code misc}, {@code techreport}.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Patents")
@PrimaryKeyJoinColumn(name = "id")
public class Patent extends Publication {

	private static final long serialVersionUID = -4184253717435746463L;

	/** Name of the institution that is supporting the patent.
	 */
	@Column
	private String institution;

	/** Geographical address of the institution that published the patent. It is usually a city and a country.
	 */
	@Column
	private String address;

	/** Type of patent.
	 */
	@Column
	private String patentType;

	/** Number that is assigned by the institution to the patent.
	 */
	@Column
	private String patentNumber;

	/** Construct a patent with the given values.
	 *
	 * @param publication the publication to copy.
	 * @param institution the name of the institution in which the patent was published.
	 * @param address the geographical address of the institution. Usually a city and a country.
	 * @param type the type of patent.
	 * @param number the number of the patent.
	 */
	public Patent(Publication publication, String institution, String address, String type, String number) {
		super(publication);
		this.institution = institution;
		this.address = address;
		this.patentType = type;
		this.patentNumber = number;
	}

	/** Construct an empty patent.
	 */
	public Patent() {
		//
	}

	@Override
	public int hashCode() {
		int h = super.hashCode();
		h = HashCodeUtils.add(h, this.institution);
		h = HashCodeUtils.add(h, this.address);
		h = HashCodeUtils.add(h, this.patentType);
		h = HashCodeUtils.add(h, this.patentNumber);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		final Patent other = (Patent) obj;
		if (!Objects.equals(this.institution, other.institution)) {
			return false;
		}
		if (!Objects.equals(this.address, other.address)) {
			return false;
		}
		if (!Objects.equals(this.patentType, other.patentType)) {
			return false;
		}
		if (!Objects.equals(this.patentNumber, other.patentNumber)) {
			return false;
		}
		return true;
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		super.forEachAttribute(consumer);
		if (!Strings.isNullOrEmpty(getInstitution())) {
			consumer.accept("institution", getInstitution()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			consumer.accept("address", getAddress()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPatentNumber())) {
			consumer.accept("patentNumber", getPatentNumber()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPatentType())) {
			consumer.accept("patentType", getPatentType()); //$NON-NLS-1$
		}
	}

	@Override
	@JsonIgnore
	public String getWherePublishedShortDescription() {
		final StringBuilder buf = new StringBuilder();
		buf.append(getInstitution());
		final boolean b0 = !Strings.isNullOrEmpty(getPatentNumber());
		final boolean b1 = !Strings.isNullOrEmpty(getPatentType());
		if (b0 && b1) {
			buf.append(", n. "); //$NON-NLS-1$
			buf.append(getPatentNumber());
			buf.append(" ("); //$NON-NLS-1$
			buf.append(getPatentType());
			buf.append(")"); //$NON-NLS-1$
		} else if (b0) {
			buf.append(", n. "); //$NON-NLS-1$
			buf.append(getPatentNumber());
		} else if (b1) {
			buf.append(getPatentType());
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			buf.append(", "); //$NON-NLS-1$
			buf.append(getAddress());
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
		buf.append(getInstitution());
		return buf.toString();
	}

	/** Replies the name of the institution in which the patent was published.
	 *
	 * @return the name of the institution.
	 */
	@RequiredFieldInForm
	public String getInstitution() {
		return this.institution;
	}

	/** Chage the name of the institution in which the patent was published.
	 *
	 * @param name the name of the institution.
	 */
	public void setInstitution(String name) {
		this.institution = Strings.emptyToNull(name);
	}

	/** Replies the geographical address where the patent was published. It is usually a city and a country.
	 *
	 * @return the address.
	 */
	public String getAddress() {
		return this.address;
	}

	/** Change the geographical address where the patent was published. It is usually a city and a country.
	 *
	 * @param address the address.
	 */
	public void setAddress(String address) {
		this.address = Strings.emptyToNull(address);
	}

	/** Replies the type of patent.
	 *
	 * @return the type description.
	 */
	public String getPatentType() {
		return this.patentType;
	}

	/** Change the type of patent.
	 *
	 * @param type the type description.
	 */
	public void setPatentType(String type) {
		this.patentType = Strings.emptyToNull(type);
	}

	/** Replies the number that was assigned by the institution to the patent.
	 *
	 * @return the report number.
	 */
	public String getPatentNumber() {
		return this.patentNumber;
	}

	/** Replies the number that was assigned by the institution to the patent.
	 *
	 * @param number the report number.
	 */
	public void setPatentNumber(String number) {
		this.patentNumber = Strings.emptyToNull(number);
	}

	@Override
	public boolean isRanked() {
		return false;
	}

}


