/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities.publication.type;

import java.util.Objects;
import java.util.function.BiConsumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import org.apache.jena.ext.com.google.common.base.Strings;

/** A thesis (HDR, PHD or Master).
 *
 * <p>This type is equivalent to the BibTeX types: {@code masterthesis}, {@code phdthesis}.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Theses")
@PrimaryKeyJoinColumn(name = "id")
public class Thesis extends Publication {

	private static final long serialVersionUID = 7051177025481855254L;

	/** Name of the university, school or institution in which the thesis was passed.
	 */
	@Column
	private String institution;

	/** Geographical address of the institution in which the thesis was passed. It is usually a city and a country.
	 */
	@Column
	private String address;

	/** Construct a thesis with the given values.
	 *
	 * @param publication the publication to copy.
	 * @param institution the name of the institution in which the thesis was passed.
	 * @param address the geographical address of the institution. Usually a city and a country.
	 */
	public Thesis(Publication publication, String institution, String address) {
		super(publication);
		this.institution = institution;
		this.address = address;
	}

	/** Construct an empty thesis.
	 */
	public Thesis() {
		//
	}

	@Override
	public int hashCode() {
		int h = super.hashCode();
		h = HashCodeUtils.add(h, this.institution);
		h = HashCodeUtils.add(h, this.address);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		final Thesis other = (Thesis) obj;
		if (!Objects.equals(this.institution, other.institution)) {
			return false;
		}
		if (!Objects.equals(this.address, other.address)) {
			return false;
		}
		return true;
	}

	@Override
	public void forEachAttribute(BiConsumer<String, Object> consumer) {
		super.forEachAttribute(consumer);
		if (!Strings.isNullOrEmpty(getInstitution())) {
			consumer.accept("institution", getInstitution()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			consumer.accept("address", getAddress()); //$NON-NLS-1$
		}
	}

	/** Replies the name of the institution in which the thesis was passed.
	 *
	 * @return the name of the institution.
	 */
	public String getInstitution() {
		return this.institution;
	}

	/** Chage the name of the institution in which the thesis was passed.
	 *
	 * @param name the name of the institution.
	 */
	public void setInstitution(String name) {
		this.institution = Strings.emptyToNull(name);
	}

	/** Replies the geographical address where the thesis was passed. It is usually a city and a country.
	 *
	 * @return the address.
	 */
	public String getAddress() {
		return this.address;
	}

	/** Change the geographical address where the thesis was passed. It is usually a city and a country.
	 *
	 * @param address the address.
	 */
	public void setAddress(String address) {
		this.address = Strings.emptyToNull(address);
	}

	@Override
	public boolean isRanked() {
		return false;
	}

}

