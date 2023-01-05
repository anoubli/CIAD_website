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

package fr.ciadlab.labmanager.entities.journal;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.hibernate.annotations.ColumnDefault;

/** Histoty of the quality indicators for a journal.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "JournalAnnualIndicators")
public class JournalQualityAnnualIndicators implements Serializable, AttributeProvider {

	private static final long serialVersionUID = -3671513001937890573L;

	/** Identifier for the history entry.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@JsonIgnore
	private int id;

	/** Year for the entry.
	 */
	@Column
	@ColumnDefault("0")
	@JsonIgnore
	private int referenceYear;

	/** Scimargo Q-Index.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private QuartileRanking scimagoQIndex;

	/** JCR/Web-Of-Science Q-Index.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private QuartileRanking wosQIndex;

	/** Impact factor.
	 */
	@Column
	@ColumnDefault("0")
	private float impactFactor;

	/** Default constructor.
	 */
	public JournalQualityAnnualIndicators() {
		this.referenceYear = 0;
		this.scimagoQIndex = null;
		this.wosQIndex = null;
		this.impactFactor = 0;
	}

	/** Construct an history with the given values.
	 *
	 * @param year the year of the entry.
	 * @param scimagoQuartile the Scimargo Q-index.
	 * @param wosQuartile the JCR/Web-Of-Science Q-Index.
	 * @param impactFactor the journal impact factor.
	 */
	public JournalQualityAnnualIndicators(int year, QuartileRanking scimagoQuartile,
			QuartileRanking wosQuartile, float impactFactor) {
		this.referenceYear = year;
		this.scimagoQIndex = scimagoQuartile;
		this.wosQIndex = wosQuartile;
		this.impactFactor = impactFactor;
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.impactFactor);
		h = HashCodeUtils.add(h, this.referenceYear);
		h = HashCodeUtils.add(h, this.scimagoQIndex);
		h = HashCodeUtils.add(h, this.wosQIndex);
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
		final JournalQualityAnnualIndicators other = (JournalQualityAnnualIndicators) obj;
		if (this.id != other.id) {
			return false;
		}
		if (this.impactFactor != other.impactFactor) {
			return false;
		}
		if (this.referenceYear != other.referenceYear) {
			return false;
		}
		if (!Objects.equals(this.scimagoQIndex, other.scimagoQIndex)) {
			return false;
		}
		if (!Objects.equals(this.wosQIndex, other.wosQIndex)) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc}
	 * <p>The attributes that are not considered by this function are:<ul>
	 * </ul>
	 */
	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (getScimagoQIndex() != null) {
			consumer.accept("scimagoQIndex", getScimagoQIndex()); //$NON-NLS-1$
		}
		if (getWosQIndex() != null) {
			consumer.accept("wosQIndex", getWosQIndex()); //$NON-NLS-1$
		}
		if (getImpactFactor() > 0f) {
			consumer.accept("impactFactor", Float.valueOf(getImpactFactor())); //$NON-NLS-1$
		}
		if (getReferenceYear() > 0) {
			consumer.accept("referenceYear", Integer.valueOf(getReferenceYear())); //$NON-NLS-1$
		}
	}

	/** Replies the year for this history entry.
	 *
	 * @return the year.
	 */
	public int getReferenceYear() {
		return this.referenceYear;
	}

	/** Change the year for this history entry.
	 *
	 * @param year the year.
	 */
	public void setReferenceYear(int year) {
		this.referenceYear = year;
	}

	/** Change the year for this history entry.
	 *
	 * @param year the year.
	 */
	public final void setReferenceYear(Number year) {
		if (year == null) {
			setReferenceYear(0);
		} else {
			setReferenceYear(year.intValue());
		}
	}

	/** Replies the Q-Index of the journal from Scimago source.
	 * 
	 * @return the Q-Index, or {@code null} if not defined.
	 */
	public QuartileRanking getScimagoQIndex() {
		return this.scimagoQIndex;
	}

	/** Change the Q-Index of the journal from Scimago source.
	 *
	 * @param quartile the Q-Index.
	 */
	public void setScimagoQIndex(QuartileRanking  quartile) {
		this.scimagoQIndex = quartile;
	}

	/** Change the Q-Index of the journal from Scimago source.
	 *
	 * @param quartile the Q-Index.
	 */
	public final void setScimagoQIndex(String quartile) {
		if (Strings.isNullOrEmpty(quartile)) {
			setScimagoQIndex((QuartileRanking) null);
		} else {
			setScimagoQIndex(QuartileRanking.valueOfCaseInsensitive(quartile));
		}
	}

	/** Replies the Q-Index of the journal from JCR/Web-Of-Science source.
	 * 
	 * @return the Q-Index, or {@code null} if not defined.
	 */
	public QuartileRanking getWosQIndex() {
		return this.wosQIndex;
	}

	/** Change the Q-Index of the journal from JCR/Web-Of-Science source.
	 * 
	 * @param quartile the Q-Index.
	 */
	public void setWosQIndex(QuartileRanking quartile) {
		this.wosQIndex = quartile;
	}

	/** Change the Q-Index of the journal from JCR/Web-Of-Science source.
	 * 
	 * @param quartile the Q-Index.
	 */
	public final void setWosQIndex(String quartile) {
		if (Strings.isNullOrEmpty(quartile)) {
			setWosQIndex((QuartileRanking) null);
		} else {
			setWosQIndex(QuartileRanking.valueOfCaseInsensitive(quartile));
		}
	}

	/** Replies the impact factor of the journal.
	 * 
	 * @return the impact factor, or {@code 0} if not defined
	 */
	public float getImpactFactor() {
		return this.impactFactor;
	}

	/** Change the impact factor of the journal.
	 * 
	 * @param impactFactor the impact factor that is a positive number.
	 */
	public void setImpactFactor(float impactFactor) {
		if (impactFactor >= 0f) {
			this.impactFactor = impactFactor;
		} else {
			this.impactFactor = 0f;
		}
	}

	/** Change the impact factor of the journal.
	 * 
	 * @param impactFactor the impact factor that is a positive number.
	 */
	public final void setImpactFactor(Number impactFactor) {
		if (impactFactor == null) {
			setImpactFactor(0f);
		} else {
			setImpactFactor(impactFactor.floatValue());
		}
	}

}
