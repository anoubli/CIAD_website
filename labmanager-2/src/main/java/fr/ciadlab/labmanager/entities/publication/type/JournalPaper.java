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
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.RequiredFieldInForm;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Paper in a journal.
 *
 * <p>This type is equivalent to the BibTeX types: {@code article}, {@code incollection}.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "JournalPapers")
@PrimaryKeyJoinColumn(name = "id")
public class JournalPaper extends Publication implements JournalBasedPublication {

	private static final long serialVersionUID = -3322028380433314352L;

	/** Number that represent the volume of the journal.
	 */
	@Column
	private String volume;

	/** Number of the journal.
	 */
	@Column
	private String number;

	/** Range of pages that corresponds to the paper in the journal.
	 */
	@Column
	private String pages;

	/** Name of series of the journal.
	 */
	@Column
	private String series;

	/** Reference to the journal.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Journal journal;

	/** Construct a journal paper with the given values.
	 *
	 * @param publication the publication to copy.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param series the series of the journal.
	 */
	public JournalPaper(Publication publication, String volume, String number, String pages, String series) {
		super(publication);
		this.volume = volume;
		this.number = number;
		this.pages = pages;
		this.series = series;
	}

	/** Construct an empty journal paper.
	 */
	public JournalPaper() {
		//
	}

	@Override
	public int hashCode() {
		int h = super.hashCode();
		h = HashCodeUtils.add(h, this.volume);
		h = HashCodeUtils.add(h, this.number);
		h = HashCodeUtils.add(h, this.pages);
		h = HashCodeUtils.add(h, this.series);
		h = HashCodeUtils.add(h, this.journal);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		final JournalPaper other = (JournalPaper) obj;
		if (!Objects.equals(this.volume, other.volume)) {
			return false;
		}
		if (!Objects.equals(this.number, other.number)) {
			return false;
		}
		if (!Objects.equals(this.pages, other.pages)) {
			return false;
		}
		if (!Objects.equals(this.series, other.series)) {
			return false;
		}
		if (!Objects.equals(this.journal, other.journal)) {
			return false;
		}
		return true;
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		super.forEachAttribute(consumer);
		if (!Strings.isNullOrEmpty(getVolume())) {
			consumer.accept("volume", getVolume()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getNumber())) {
			consumer.accept("number", getNumber()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPages())) {
			consumer.accept("pages", getPages()); //$NON-NLS-1$
		}		
		if (!Strings.isNullOrEmpty(getSeries())) {
			consumer.accept("series", getSeries()); //$NON-NLS-1$
		}
		if (isRanked()) {
			consumer.accept("scimagoQIndex", getScimagoQIndex()); //$NON-NLS-1$
			consumer.accept("wosQIndex", getWosQIndex()); //$NON-NLS-1$
			consumer.accept("impactFactor", Float.valueOf(getImpactFactor())); //$NON-NLS-1$
		}
	}

	@Override
	public String getWherePublishedShortDescription() {
		final StringBuilder buf = new StringBuilder();
		buf.append(getJournal().getJournalName());
		if (!Strings.isNullOrEmpty(getVolume())) {
			buf.append(", vol. "); //$NON-NLS-1$
			buf.append(getVolume());
		}
		if (!Strings.isNullOrEmpty(getNumber())) {
			buf.append(", n. "); //$NON-NLS-1$
			buf.append(getNumber());
		}
		if (!Strings.isNullOrEmpty(getPages())) {
			buf.append(", pp. "); //$NON-NLS-1$
			buf.append(getPages());
		}
		if (!Strings.isNullOrEmpty(getJournal().getPublisher())) {
			buf.append(", "); //$NON-NLS-1$
			buf.append(getJournal().getPublisher());
		}
		if (!Strings.isNullOrEmpty(getJournal().getISBN())) {
			buf.append(", ISBN "); //$NON-NLS-1$
			buf.append(getJournal().getISBN());
		}
		if (!Strings.isNullOrEmpty(getJournal().getISSN())) {
			buf.append(", ISSN "); //$NON-NLS-1$
			buf.append(getJournal().getISSN());
		}
		return buf.toString();
	}

	@Override
	public String getPublicationTarget() {
		final StringBuilder buf = new StringBuilder();
		final Journal journal = getJournal();
		if (journal != null) {
			buf.append(journal.getJournalName());
			if (!Strings.isNullOrEmpty(journal.getPublisher())) {
				buf.append(", "); //$NON-NLS-1$
				buf.append(journal.getPublisher());
			}
		}
		return buf.toString();
	}

	/** Replies the volume number of the journal in which the publication was published.
	 * 
	 * @return the volume number.
	 */
	public String getVolume() {
		return this.volume;
	}

	/** Change the volume number of the journal in which the publication was published.
	 * 
	 * @param volume the volume number.
	 */
	public void setVolume(String volume) {
		this.volume = Strings.emptyToNull(volume);
	}

	/** Replies the number of the journal in which the publication was published.
	 * 
	 * @return the number.
	 */
	public String getNumber() {
		return this.number;
	}

	/** Change the number of the journal in which the publication was published.
	 * 
	 * @param number the number.
	 */
	public void setNumber(String number) {
		this.number = Strings.emptyToNull(number);
	}

	/** Replies the page range in the journal in which the publication was published.
	 * 
	 * @return the number.
	 */
	public String getPages() {
		return this.pages;
	}

	/** Change the page range in the journal in which the publication was published.
	 * 
	 * @param range the page range.
	 */
	public void setPages(String range) {
		this.pages = Strings.emptyToNull(range);
	}

	/** Replies the series of the journal in which the publication was published.
	 * 
	 * @return the series.
	 */
	public String getSeries() {
		return this.series;
	}

	/** Change the series of the journal in which the publication was published.
	 * 
	 * @param series the series.
	 */
	public void setSeries(String series) {
		this.series = Strings.emptyToNull(series);
	}

	@Override
	@RequiredFieldInForm
	public Journal getJournal() {
		return this.journal;
	}

	@Override
	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	@Override
	public QuartileRanking getScimagoQIndex() {
		final Journal journal = getJournal();
		if (journal != null) {
			return journal.getScimagoQIndexByYear(getPublicationYear());
		}
		return QuartileRanking.NR;
	}

	@Override
	public QuartileRanking getWosQIndex() {
		final Journal journal = getJournal();
		if (journal != null) {
			return journal.getWosQIndexByYear(getPublicationYear());
		}
		return QuartileRanking.NR;
	}

	@Override
	public float getImpactFactor() {
		final Journal journal = getJournal();
		if (journal != null) {
			return journal.getImpactFactorByYear(getPublicationYear());
		}
		return 0f;
	}

	@Override
	public boolean isRanked() {
		final Journal journal = getJournal();
		if (journal != null) {
			return journal.getScimagoQIndexByYear(getPublicationYear()) != QuartileRanking.NR
					|| journal.getWosQIndexByYear(getPublicationYear()) != QuartileRanking.NR;
		}
		return false;
	}

	/** Replies the ISBN number that is associated to this publication.
	 * This functions delegates to the journal.
	 *
	 * @return the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 * @deprecated See {@link Journal#getISBN()}
	 */
	@Override
	@Deprecated(since = "2.0.0")
	public String getISBN() {
		if (this.journal != null) {
			return this.journal.getISBN();
		}
		return null;
	}

	/** Change the ISBN number that is associated to this publication.
	 * This functions delegates to the journal.
	 *
	 * @param isbn the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 * @deprecated See {@link Journal#setISBN(String)}
	 */
	@Override
	@Deprecated(since = "2.0.0")
	public void setISBN(String isbn) {
		if (this.journal != null) {
			this.journal.setISBN(isbn);
		}
	}

	/** Replies the ISSN number that is associated to this publication.
	 * This functions delegates to the journal.
	 *
	 * @return the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 * @deprecated See {@link Journal#getISSN()}
	 */
	@Override
	@Deprecated(since = "2.0.0")
	public String getISSN() {
		if (this.journal != null) {
			return this.journal.getISSN();
		}
		return null;
	}

	/** Change the ISSN number that is associated to this publication.
	 * This functions delegates to the journal.
	 *
	 * @param issn the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 * @deprecated See {@link Journal#setISSN(String)}
	 */
	@Override
	@Deprecated(since = "2.0.0")
	public final void setISSN(String issn) {
		if (this.journal != null) {
			this.journal.setISSN(issn);
		}
	}

	@Override
	public Boolean getOpenAccess() {
		final Journal journal = getJournal();
		if (journal != null) {
			return journal.getOpenAccess();
		}
		return null;
	}

}
