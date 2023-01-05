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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.arakhne.afc.util.IntegerList;
import org.arakhne.afc.util.ListUtil;

/** Scientific or scientific culture dissemination journal.
 * This class provides all the necessary information and tools for managing the journals and their quality indicators.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Journals")
public class Journal implements Serializable, JsonSerializable, AttributeProvider, IdentifiableEntity {

	private static final long serialVersionUID = -2046765660549008074L;

	/** Identifier of the journal in the database.
	 * 
	 * <p>Using this instead of {@link GenerationType#IDENTITY} allows for JOINED or TABLE_PER_CLASS inheritance types to work.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private int id;

	/** Name of the journal.
	 */
	@Column
	private String journalName;

	/** Name of the publisher of the journal.
	 */
	@Column
	private String publisher;

	/** Address of the publisher of the journal.
	 */
	@Column
	private String address;

	/** URL to the page of the journal on the publisher website.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String journalUrl;

	/** Identifier of the journal on the Scimago website.
	 */
	@Column
	private String scimagoId;

	/** Name of the Scimago category that should be used for retrieving the quartile.
	 */
	@Column
	private String scimagoCategory;

	/** Identifier of the journal on the Web-Of-Science website.
	 */
	@Column
	private String wosId;

	/** Name of the WoS category that should be used for retrieving the quartile.
	 */
	@Column
	private String wosCategory;

	/** ISBN number if the journal has one.
	 */
	@Column
	private String isbn;

	/** ISSN number if the journal has one.
	 */
	@Column
	private String issn;

	/** Indicates if the journal is an open-access journal. If this field
	 * is not set, we don't know if the journal is open access.
	 */
	@Column(nullable = true)
	private Boolean openAccess;

	/** Indicates if the journal was validated by an authority.
	 */
	@Column(nullable = false)
	private boolean validated;

	/** List of papers that are published in this journal.
	 */
	@OneToMany(mappedBy = "journal", fetch = FetchType.LAZY)
	private Set<JournalPaper> publishedPapers;

	/** History of the quality indicators for this journal.
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "journal_journal_annual_indicators_mapping", 
	joinColumns = {
			@JoinColumn(name = "journal_id", referencedColumnName = "id")
	},
	inverseJoinColumns = {
			@JoinColumn(name = "indicators_id", referencedColumnName = "id")
	})
	@MapKey(name = "referenceYear")
	private Map<Integer, JournalQualityAnnualIndicators> qualityIndicators;

	/** Construct an empty journal.
	 */
	public Journal() {
		//
	}

	/** Construct by copying the given journal (exception id, published papers and quality indicators).
	 *
	 * @param journal the journal to be copied.
	 */
	public Journal(Journal journal) {
		assert journal != null;
		this.journalName = journal.getJournalName();
		this.publisher = journal.getPublisher();
		this.address = journal.getAddress();
		this.journalUrl = journal.getJournalURL();
		this.scimagoId = journal.getScimagoId();
		this.scimagoCategory = journal.getScimagoCategory();
		this.wosId = journal.getWosId();
		this.wosCategory = journal.getWosCategory();
		this.isbn = journal.getISBN();
		this.issn = journal.getISSN();
		this.openAccess = journal.getOpenAccess();
		this.validated = journal.isValidated();
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.journalName);
		h = HashCodeUtils.add(h, this.publisher);
		h = HashCodeUtils.add(h, this.address);
		h = HashCodeUtils.add(h, this.journalUrl);
		h = HashCodeUtils.add(h, this.scimagoId);
		h = HashCodeUtils.add(h, this.scimagoCategory);
		h = HashCodeUtils.add(h, this.wosId);
		h = HashCodeUtils.add(h, this.wosCategory);
		h = HashCodeUtils.add(h, this.isbn);
		h = HashCodeUtils.add(h, this.issn);
		h = HashCodeUtils.add(h, this.openAccess);
		h = HashCodeUtils.add(h, this.validated);
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
		final Journal other = (Journal) obj;
		if (this.id != other.id) {
			return false;
		}
		if (!Objects.equals(this.journalName, other.journalName)) {
			return false;
		}
		if (!Objects.equals(this.publisher, other.publisher)) {
			return false;
		}
		if (!Objects.equals(this.address, other.address)) {
			return false;
		}
		if (!Objects.equals(this.journalUrl, other.journalUrl)) {
			return false;
		}
		if (!Objects.equals(this.scimagoId, other.scimagoId)) {
			return false;
		}
		if (!Objects.equals(this.scimagoCategory, other.scimagoCategory)) {
			return false;
		}
		if (!Objects.equals(this.wosId, other.wosId)) {
			return false;
		}
		if (!Objects.equals(this.wosCategory, other.wosCategory)) {
			return false;
		}
		if (!Objects.equals(this.isbn, other.isbn)) {
			return false;
		}
		if (!Objects.equals(this.issn, other.issn)) {
			return false;
		}
		if (!Objects.equals(this.openAccess, other.openAccess)) {
			return false;
		}
		if (this.validated != other.validated) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc}
	 * <p>The attributes that are not considered by this function are:<ul>
	 * <li>{@code id}</li>
	 * <li>{@code publishedPapers}</li>
	 * <li>{@code qualityIndicators}</li>
	 * </ul>
	 */
	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (getId() != 0) {
			consumer.accept("id", Integer.valueOf(getId())); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getJournalName())) {
			consumer.accept("journalName", getJournalName()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPublisher())) {
			consumer.accept("publisher", getPublisher()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			consumer.accept("address", getAddress()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getJournalURL())) {
			consumer.accept("journalURL", getJournalURL()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getScimagoId())) {
			consumer.accept("scimagoId", getScimagoId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getScimagoCategory())) {
			consumer.accept("scimagoCategory", getScimagoCategory()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getWosId())) {
			consumer.accept("wosId", getWosId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getWosCategory())) {
			consumer.accept("wosCategory", getWosCategory()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getISBN())) {
			consumer.accept("isbn", getISBN()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getISSN())) {
			consumer.accept("issn", getISSN()); //$NON-NLS-1$
		}
		if (getOpenAccess() != null) {
			consumer.accept("openAccess", getOpenAccess()); //$NON-NLS-1$
		}
		consumer.accept("validated", Boolean.valueOf(isValidated())); //$NON-NLS-1$
	}

	@Override
	public void serialize(JsonGenerator generator, SerializerProvider serializers) throws IOException {
		generator.writeStartObject();
		forEachAttribute((attrName, attrValue) -> {
			JsonUtils.writeField(generator, attrName, attrValue);
		});
		//
		generator.writeObjectFieldStart("qualityIndicators"); //$NON-NLS-1$
		for (final Entry<Integer, JournalQualityAnnualIndicators> indicators : getQualityIndicators().entrySet()) {
			generator.writeFieldId(indicators.getKey().intValue());
			generator.writeObject(indicators.getValue());
		}
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

	/** Change the identifier of the journal in the database.
	 *
	 * @param id the identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** Replies the name of the journal.
	 *
	 * @return the name.
	 */
	public String getJournalName() {
		return this.journalName;
	}

	/** Change the name of the journal.
	 *
	 * @param name the name.
	 */
	public void setJournalName(String name) {
		this.journalName = Strings.emptyToNull(name);
	}

	/** Replies the name of publisher of the journal.
	 *
	 * @return the name.
	 */
	public String getPublisher() {
		return this.publisher;
	}

	/** Change the name of publisher of the journal.
	 *
	 * @param name the name.
	 */
	public void setPublisher(String name) {
		this.publisher = Strings.emptyToNull(name);
	}

	/** Replies the address of publisher of the journal.
	 *
	 * @return the address.
	 */
	public String getAddress() {
		return this.address;
	}

	/** Change the address of publisher of the journal.
	 *
	 * @param address the address.
	 */
	public void setAddress(String address) {
		this.address = Strings.emptyToNull(address);
	}

	/** Replies the URL of the journal on the publisher website.
	 *
	 * @return the URL.
	 */
	public final URL getJournalURLObject() {
		try {
			return new URL(getJournalURL());
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/** Replies the URL of the journal on the publisher website.
	 *
	 * @return the URL.
	 */
	public String getJournalURL() {
		return this.journalUrl;
	}

	/** Change the URL of the journal on the publisher website.
	 *
	 * @param url the URL.
	 */
	public void setJournalURL(String url) {
		this.journalUrl = Strings.emptyToNull(url);
	}

	/** Change the URL of the journal on the publisher website.
	 *
	 * @param url the URL.
	 */
	public final void setJournalURL(URL url) {
		setJournalURL(url == null ? null : url.toExternalForm());
	}

	/** Replies the identifier of the journal on the Scimago website.
	 *
	 * @return the identifier.
	 */
	public String getScimagoId() {
		return this.scimagoId;
	}

	/** Change the identifier of the journal on the Scimago website.
	 *
	 * @param id the identifier.
	 */
	public void setScimagoId(String id) {
		this.scimagoId = Strings.emptyToNull(id);
	}

	/** Replies the name of the Scimago category that should be used for retrieving the quartile.
	 *
	 * @return the name.
	 * @since 2.5
	 */
	public String getScimagoCategory() {
		return this.scimagoCategory;
	}

	/** Change the name of the Scimago category that should be used for retrieving the quartile.
	 *
	 * @param category the category name.
	 * @since 2.5
	 */
	public void setScimagoCategory(String category) {
		this.scimagoCategory = Strings.emptyToNull(category);
	}

	/** Replies the identifier of the journal on the Web-Of-Science website.
	 *
	 * @return the identifier.
	 */
	public String getWosId() {
		return this.wosId;
	}

	/** Change the identifier of the journal on the Web-Of-Science website.
	 *
	 * @param id the identifier.
	 */
	public void setWosId(String id) {
		this.wosId = Strings.emptyToNull(id);
	}

	/** Replies the name of the WoS category that should be used for retrieving the quartile.
	 *
	 * @return the name.
	 * @since 2.5
	 */
	public String getWosCategory() {
		return this.wosCategory;
	}

	/** Change the name of the WoS category that should be used for retrieving the quartile.
	 *
	 * @param category the category name.
	 * @since 2.5
	 */
	public void setWosCategory(String category) {
		this.wosCategory = Strings.emptyToNull(category);
	}

	/** Replies if the journal is open access. If it is not known, this functio returns {@code null}.
	 *
	 * @return the open-access status of the journal, or {@code null} if it is not known.
	 */
	public Boolean getOpenAccess() {
		return this.openAccess;
	}

	/** Change the flag that indicates if the journal is open access. If it is not known, this functio returns {@code null}.
	 *
	 * @param openAccess the open-access status of the journal, or {@code null} if it is not known.
	 */
	public void setOpenAccess(Boolean openAccess) {
		this.openAccess = openAccess;
	}

	/** Replies the set of published papers in this journal.
	 *
	 * @return the published papers.
	 */
	public Set<JournalPaper> getPublishedPapers() {
		if (this.publishedPapers == null) {
			this.publishedPapers = new HashSet<>();
		}
		return this.publishedPapers;
	}

	/** Change the set of published papers in this journal.
	 *
	 * @param papers the published papers.
	 */
	public void setPublishedPapers(Set<JournalPaper> papers) {
		if (this.publishedPapers != null) {
			this.publishedPapers.clear();
			if (papers != null) {
				this.publishedPapers.addAll(papers);
			}
		} else if (papers == null) {
			this.publishedPapers = new HashSet<>();
		} else {
			this.publishedPapers = papers;
		}
	}

	/** Replies if the journal has at least one published paper attached to it.
	 *
	 * @return {@code true} if a paper is attached to this journal.
	 */
	public boolean hasPublishedPaper() {
		return this.publishedPapers != null && !this.publishedPapers.isEmpty();
	}

	/** Replies the quality indicators of the journal.
	 *
	 * @return the indicators.
	 * @since 2.0
	 */
	public Map<Integer, JournalQualityAnnualIndicators> getQualityIndicators() {
		if (this.qualityIndicators == null) {
			this.qualityIndicators = new TreeMap<>();
		}
		return this.qualityIndicators;
	}

	/** Replies the quality indicators of the journal for the given year.
	 *
	 * @param year the year to search for.
	 * @return the indicators or {@code null} if none were defined.
	 * @since 2.0
	 */
	public final JournalQualityAnnualIndicators getQualityIndicatorsForYear(int year) {
		if (this.qualityIndicators == null) {
			return null;
		}
		return this.qualityIndicators.get(Integer.valueOf(year));
	}

	/** Replies the quality indicators that is fitting the given predicates and with an year lower or equal
	 * to the given value.
	 *
	 * @param year the year to search for.
	 * @param selector the object that permits to select the best quality indicators.
	 * @return the indicators or {@code null} if none were defined.
	 * @since 2.0
	 */
	public final JournalQualityAnnualIndicators getQualityIndicatorsFor(int year, Predicate<JournalQualityAnnualIndicators> selector) {
		if (this.qualityIndicators != null) {
			final IntegerList ids = new IntegerList(this.qualityIndicators.keySet());
			final int start = ListUtil.floorIndex(ids, (a, b) -> Integer.compare(a.intValue(), b.intValue()), Integer.valueOf(year));
			for (int i = start; i >= 0; --i) {
				final JournalQualityAnnualIndicators indicators = this.qualityIndicators.get(ids.get(i));
				if (indicators != null && selector.test(indicators)) {
					return indicators;
				}
			}
		}
		return null;
	}

	/** Replies if the journal has quality indicators for the given year.
	 *
	 * @param year the year to search for.
	 * @return {@code true} if the journal has quality indicators.
	 */
	public boolean hasQualityIndicatorsForYear(int year) {
		return getQualityIndicatorsForYear(year) != null;
	}

	/** Replies the Q-Index of the journal from the Scimago source.
	 * If there is no Q-Index known for the given year, this function replies
	 * the Q-Index for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return the Q-Index of the journal for the given year, never {@code null}.
	 */
	public QuartileRanking getScimagoQIndexByYear(int year) {
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsFor(year, it -> it.getScimagoQIndex() != null);
		if (indicators != null) {
			final QuartileRanking ranking = indicators.getScimagoQIndex();
			if (ranking != null) {
				return ranking;
			}
		}
		return QuartileRanking.NR;
	}

	/** Change the Q-Index of the journal from the Scimago source.
	 *
	 * @param year the year to search for.
	 * @param quartile the Q-Index of the journal for the given year, or {@code null} if not defined.
	 * @return the impacted annual indicators object
	 */
	public JournalQualityAnnualIndicators setScimagoQIndexByYear(int year, QuartileRanking quartile) {
		JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			indicators.setScimagoQIndex(quartile);
		} else {
			indicators = new JournalQualityAnnualIndicators(year, quartile, null, 0f);
			if (this.qualityIndicators == null) {
				this.qualityIndicators = new TreeMap<>();
			}
			this.qualityIndicators.put(Integer.valueOf(year), indicators);
		}
		return indicators;
	}

	/** Replies if the journal has Scimago Q-Index for the given year.
	 * If there is no Q-Index known for the given year, this function replies
	 * the Q-Index for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return {@code true} if the journal has Q-Index.
	 */
	public boolean hasScimagoQIndexForYear(int year) {
		final QuartileRanking qindex = getScimagoQIndexByYear(year);
		return qindex != QuartileRanking.NR;
	}

	/** Replies the Q-Index of the journal from the JCR/WOS source.
	 * If there is no Q-Index known for the given year, this function replies
	 * the Q-Index for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return the Q-Index of the journal for the given year, never {@code null}.
	 */
	public QuartileRanking getWosQIndexByYear(int year) {
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsFor(year, it -> it.getWosQIndex() != null);
		if (indicators != null) {
			QuartileRanking ranking = indicators.getWosQIndex();
			if (ranking != null) {
				return ranking;
			}
		}
		return QuartileRanking.NR;
	}

	/** Change the Q-Index of the journal from the Web-Of-Science source.
	 *
	 * @param year the year to search for.
	 * @param quartile the Q-Index of the journal for the given year, or {@code null} if not defined.
	 * @return the impacted annual indicators object
	 */
	public JournalQualityAnnualIndicators setWosQIndexByYear(int year, QuartileRanking quartile) {
		JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			indicators.setWosQIndex(quartile);
		} else {
			indicators = new JournalQualityAnnualIndicators(year, null, quartile, 0f);
			if (this.qualityIndicators == null) {
				this.qualityIndicators = new TreeMap<>();
			}
			this.qualityIndicators.put(Integer.valueOf(year), indicators);
		}
		return indicators;
	}

	/** Replies if the journal has WOS Q-Index for the given year.
	 * If there is no Q-Index known for the given year, this function replies
	 * the Q-Index for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return {@code true} if the journal has Q-Index.
	 */
	public boolean hasWosQIndexForYear(int year) {
		return getWosQIndexByYear(year) != QuartileRanking.NR;
	}

	/** Replies the impact factor of the journal.
	 * If there is no impact factor known for the given year, this function replies
	 * the impact factor for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return the IF of the journal for the given year, or {@code 0} if not defined.
	 */
	public float getImpactFactorByYear(int year) {
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsFor(year, it -> it.getImpactFactor() > 0f);
		if (indicators != null) {
			return indicators.getImpactFactor();
		}
		return 0f;
	}

	/** Change the impact factor of the journal.
	 *
	 * @param year the year to search for.
	 * @param impactFactor the new impact factor of the journal for the given year, or {@code 0} if not defined.
	 * @return the impacted annual indicators object
	 */
	public JournalQualityAnnualIndicators setImpactFactorByYear(int year, float impactFactor) {
		assert impactFactor >= 0f;
		JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			indicators.setImpactFactor(impactFactor);
		} else {
			indicators = new JournalQualityAnnualIndicators(year, null, null, impactFactor);
			if (this.qualityIndicators == null) {
				this.qualityIndicators = new TreeMap<>();
			}
			this.qualityIndicators.put(Integer.valueOf(year), indicators);
		}
		return indicators;
	}

	/** Replies if the journal has impact factor for the given year.
	 * If there is no impact factor known for the given year, this function replies
	 * the impact factor for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return {@code true} if the journal has impact factor.
	 */
	public boolean hasImpactFactorForYear(int year) {
		return getImpactFactorByYear(year) > 0f;
	}

	/** Replies the ISBN number that is associated to this journal.
	 *
	 * @return the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 */
	public String getISBN() {
		return this.isbn;
	}

	/** Change the ISBN number that is associated to this journal.
	 *
	 * @param isbn the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 */
	public void setISBN(String isbn) {
		this.isbn = Strings.emptyToNull(isbn);
	}

	/** Replies the ISSN number that is associated to this journal.
	 *
	 * @return the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 */
	public String getISSN() {
		return this.issn;
	}

	/** Change the ISSN number that is associated to this journal.
	 *
	 * @param issn the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 */
	public void setISSN(String issn) {
		this.issn = Strings.emptyToNull(issn);
	}

	/** Replies if this journal was validated by an authority.
	 *
	 * @return {@code true} if the journal is validated.
	 */
	public boolean isValidated() {
		return this.validated;
	}

	/** Change the flag that indicates if this journal was validated by an authority.
	 *
	 * @param validated {@code true} if the journal is validated.
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	/** Change the flag that indicates if this journal was validated by an authority.
	 *
	 * @param validated {@code true} if the journal is validated.
	 */
	public final void setValidated(Boolean validated) {
		if (validated == null) {
			setValidated(false);
		} else {
			setValidated(validated.booleanValue());
		}
	}

}
