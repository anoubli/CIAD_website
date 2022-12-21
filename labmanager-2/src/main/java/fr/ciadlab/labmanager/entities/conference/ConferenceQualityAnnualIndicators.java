package fr.ciadlab.labmanager.entities.conference;

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

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;

@Entity
@Table(name = "ConferenceQualityAnnualIndicators")
public class ConferenceQualityAnnualIndicators implements Serializable, AttributeProvider{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6559412818508678891L;
	
	
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
	
	@Column
	@Enumerated(EnumType.STRING)
	private CoreRanking ranking;
	
	
	public ConferenceQualityAnnualIndicators() {
		this.referenceYear = 0;
		this.ranking = null;
	}
	
	public ConferenceQualityAnnualIndicators(int referenceYear, CoreRanking ranking) {
		this.referenceYear = referenceYear;
		this.ranking = ranking;
	}


	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		// TODO Auto-generated method stub
		
	}


	public int getReferenceYear() {
		return referenceYear;
	}


	public void setReferenceYear(int referenceYear) {
		this.referenceYear = referenceYear;
	}


	public CoreRanking getRanking() {
		return ranking;
	}


	public void setRanking(CoreRanking ranking) {
		this.ranking = ranking;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ranking, referenceYear);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConferenceQualityAnnualIndicators other = (ConferenceQualityAnnualIndicators) obj;
		return ranking == other.ranking && referenceYear == other.referenceYear;
	}
	
	@Override
	public String toString() {
		return ":" + this.id; //$NON-NLS-1$
	}
	
	
	
	
	
	
}
