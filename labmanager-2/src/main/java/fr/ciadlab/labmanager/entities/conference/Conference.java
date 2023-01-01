package fr.ciadlab.labmanager.entities.conference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import org.arakhne.afc.util.IntegerList;
import org.arakhne.afc.util.ListUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;

@Entity
@Table(name = "Conferences")
public class Conference implements Serializable, JsonSerializable, AttributeProvider, IdentifiableEntity{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4383839850815225465L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private int id;
	
	/** acronym.
	 */
	@Column
	private String acronym;

	
	/** Name of the conference.
	 */
	@Column
	private String conferenceName;
	
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String conferenceUrl; 
	
	@Column
	private String publisher;
	
	/** ISsN number if the conference has one.
	 */
	@Column
	private String issn;
	
	@Column
	private String coreIdentifier;
	
	// TODO create a class ConferenceQualityAnnualIndicators
	/** History of the quality indicators for this journal.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "conference_conference_annual_indicators_mapping", 
	joinColumns = {
			@JoinColumn(name = "conference_id", referencedColumnName = "id")
	},
	inverseJoinColumns = {
			@JoinColumn(name = "indicators_id", referencedColumnName = "id")
	})
	@MapKey(name = "referenceYear")
	private Map<Integer, ConferenceQualityAnnualIndicators> qualityIndicators;
	
	
	public Conference(int id, String acronym, String name, String conferenceUrl, String publisher, String issn,
			String coreIdentifier, Map<Integer, ConferenceQualityAnnualIndicators> qualityIndicators) {
		super();
		this.id = id;
		this.acronym = acronym;
		this.conferenceName = name;
		this.conferenceUrl = conferenceUrl;
		this.publisher = publisher;
		this.issn = issn;
		this.coreIdentifier = coreIdentifier;
		this.qualityIndicators = qualityIndicators;
	}
	
	public Conference(){
		
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	public String getAcronym() {
		return this.acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getName() {
		return conferenceName;
	}

	public void setName(String name) {
		this.conferenceName = name;
	}

	public String getConferenceUrl() {
		return conferenceUrl;
	}

	public void setConferenceUrl(String conferenceUrl) {
		this.conferenceUrl = conferenceUrl;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String getCoreIdentifier() {
		return coreIdentifier;
	}

	public void setCoreIdentifier(String coreIdentifier) {
		this.coreIdentifier = coreIdentifier;
	}

	public Map<Integer, ConferenceQualityAnnualIndicators> getQualityIndicators() {
		return qualityIndicators;
	}

	public void setQualityIndicators(Map<Integer, ConferenceQualityAnnualIndicators> qualityIndicators) {
		this.qualityIndicators = qualityIndicators;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	public final ConferenceQualityAnnualIndicators getQualityIndicatorsFor(int year, Predicate<ConferenceQualityAnnualIndicators> selector) {
		if (this.qualityIndicators != null) {
			final IntegerList ids = new IntegerList(this.qualityIndicators.keySet());
			final int start = ListUtil.floorIndex(ids, (a, b) -> Integer.compare(a.intValue(), b.intValue()), Integer.valueOf(year));
			for (int i = start; i >= 0; --i) {
				final ConferenceQualityAnnualIndicators indicators = this.qualityIndicators.get(ids.get(i));
				if (indicators != null && selector.test(indicators)) {
					return indicators;
				}
			}
		}
		return null;
	}

	public final ConferenceQualityAnnualIndicators getQualityIndicatorsForYear(int year) {
		if (this.qualityIndicators == null) {
			return null;
		}
		return this.qualityIndicators.get(Integer.valueOf(year));
	}

	public ConferenceQualityAnnualIndicators setQualityIndicatorsByYear(int year, CoreRanking ranking) {

		ConferenceQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			indicators.setRanking(ranking);
		} else {
			indicators = new ConferenceQualityAnnualIndicators(year, ranking);
			if (this.qualityIndicators == null) {
				this.qualityIndicators = new TreeMap<>();
			}
			this.qualityIndicators.put(Integer.valueOf(year), indicators);
		}
		return indicators;
	}
	
	

}
