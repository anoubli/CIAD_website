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

package fr.ciadlab.labmanager.entities.supervision;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

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
import fr.ciadlab.labmanager.utils.HashCodeUtils;

/** Description of a supervisor.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */
@Entity
@Table(name = "Supervisors")
public class Supervisor implements Serializable, AttributeProvider, Comparable<Supervisor>, IdentifiableEntity {

	private static final long serialVersionUID = -2851789340464408145L;

	/** Identifier of the jury.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Promoter or director of the candidate.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Person supervisor;

	/** Percentage of supervision.
	 */
	@Column
	private int percentage;

	/** Type of supervisor.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private SupervisorType type = SupervisorType.SUPERVISOR;

	/** Construct an empty supervision.
	 */
	public Supervisor() {
		//
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		//h = HashCodeUtils.add(h, this.supervisor);
		h = HashCodeUtils.add(h, this.percentage);
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
		final Supervisor other = (Supervisor) obj;
//		if (!Objects.equals(this.supervisor, other.supervisor)) {
//			return false;
//		}
		if (this.percentage != other.percentage) {
			return false;
		}
		if (!Objects.equals(this.type, other.type)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Supervisor o) {
		return EntityUtils.getPreferredSupervisorComparator().compare(this, o);
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (getType() != null) {
			consumer.accept("type", getType()); //$NON-NLS-1$
		}
		if (getPercentage() > 0 && getPercentage() <= 100) {
			consumer.accept("percentage", Integer.valueOf(getPercentage())); //$NON-NLS-1$
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

	/** Replies the supervisor.
	 *
	 * @return the supervisor.
	 */
	public Person getSupervisor() {
		return this.supervisor;
	}

	/** Change the supervisor.
	 *
	 * @param supervisor the supervisor.
	 */
	public void setSupervisor(Person supervisor) {
		this.supervisor = supervisor;
	}

	/** Replies the percentage of supervision.
	 *
	 * @return the percentage of supervision.
	 */
	public int getPercentage() {
		return this.percentage;
	}

	/** Change the percentage of supervision.
	 *
	 * @param percentage the percentage of supervision between 0 and 100.
	 */
	public void setPercentage(int percentage) {
		final int p;
		if (percentage < 0) {
			p = 0;
		} else if (percentage > 100) {
			p = 100;
		} else {
			p = percentage;
		}
		this.percentage = p;
	}

	/** Change the percentage of supervision.
	 *
	 * @param percentage the percentage of supervision between 0 and 100.
	 */
	public final void setPercentage(Number percentage) {
		if (percentage == null) {
			setPercentage(0);
		} else {
			setPercentage(percentage.intValue());
		}
	}

	/** Replies the type of supervisor.
	 *
	 * @return the type.
	 */
	public SupervisorType getType() {
		return this.type;
	}

	/** Change the type of supervisor.
	 *
	 * @param type the type of supervisor.
	 */
	public void setType(SupervisorType type) {
		if (type == null) {
			this.type = SupervisorType.SUPERVISOR;
		} else {
			this.type = type;
		}
	}

	/** Change the type of supervisor.
	 *
	 * @param type the type of supervisor.
	 */
	public final void setType(String type) {
		if (Strings.isNullOrEmpty(type)) {
			setType((SupervisorType) null);
		} else {
			setType(SupervisorType.valueOfCaseInsensitive(type));
		}
	}

}
