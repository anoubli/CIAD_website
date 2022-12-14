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

package fr.ciadlab.labmanager.entities.publication;

import java.util.Locale;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import fr.ciadlab.labmanager.entities.publication.type.Book;
import fr.ciadlab.labmanager.entities.publication.type.BookChapter;
import fr.ciadlab.labmanager.entities.publication.type.ConferencePaper;
import fr.ciadlab.labmanager.entities.publication.type.JournalEdition;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.entities.publication.type.KeyNote;
import fr.ciadlab.labmanager.entities.publication.type.MiscDocument;
import fr.ciadlab.labmanager.entities.publication.type.Patent;
import fr.ciadlab.labmanager.entities.publication.type.Report;
import fr.ciadlab.labmanager.entities.publication.type.Thesis;
import org.springframework.context.support.MessageSourceAccessor;

/** Describe the type of a publication.
 * The type of publication is mostly inherited from the BibTeX standard,
 * the National French Research agencies, and adapted to the usage of the research laboratory.
 * The publication tpe is located into a {@link PublicationCategory publication category}.
 *
 * <p>The order of the items of this enumeration reflects the order of the types from the most
 * important (ordinal equals to 0} to the less important (highest ordinal value).
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see PublicationCategory
 * @see "https://en.wikipedia.org/wiki/BibTeX"
 */
public enum PublicationType {
	/** Articles in international journals with selection commitee.
	 * The ranking of the journal depends on  and ranked in international databases.
	 */
	INTERNATIONAL_JOURNAL_PAPER {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return JournalPaper.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.ACL, PublicationCategory.ACLN);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			if (isRankedPublication) {
				return PublicationCategory.ACL;
			}
			return PublicationCategory.ACLN;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** Articles in national journals with selection commitee and ranked in international databases.
	 */
	NATIONAL_JOURNAL_PAPER {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return JournalPaper.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.ACL, PublicationCategory.ACLN);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			if (isRankedPublication) {
				return PublicationCategory.ACL;
			}
			return PublicationCategory.ACLN;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Papers in the proceedings of an international conference.
	 */
	INTERNATIONAL_CONFERENCE_PAPER {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return ConferencePaper.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.C_ACTI);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.C_ACTI;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** Papers in the proceedings of a national conference.
	 */
	NATIONAL_CONFERENCE_PAPER {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return ConferencePaper.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.C_ACTN);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.C_ACTN;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Oral Communications without proceeding in international conference.
	 */
	INTERNATIONAL_ORAL_COMMUNICATION {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return ConferencePaper.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.C_COM);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.C_COM;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** Oral communications without proceeding in national conference.
	 */
	NATIONAL_ORAL_COMMUNICATION {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return ConferencePaper.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.C_COM);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.C_COM;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Posters in international conference.
	 */
	INTERNATIONAL_POSTER {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return ConferencePaper.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.C_AFF);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.C_AFF;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** Posters in national conference.
	 */
	NATIONAL_POSTER {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return ConferencePaper.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.C_AFF);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.C_AFF;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Editor of international journals.
	 */
	INTERNATIONAL_JOURNAL_EDITION {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return JournalEdition.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.DO);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.DO;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** Editor of national journals.
	 */
	NATIONAL_JOURNAL_EDITION {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return JournalEdition.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.DO);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.DO;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** International scientific books.
	 */
	INTERNATIONAL_BOOK {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Book.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.OS);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.OS;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** National scientific books.
	 */
	NATIONAL_BOOK {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Book.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.OS);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.OS;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Chapters in international scientific books.
	 */
	INTERNATIONAL_BOOK_CHAPTER {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return BookChapter.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.COS);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.COS;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** Chapters in national scientific books.
	 */
	NATIONAL_BOOK_CHAPTER {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return BookChapter.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.COS);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.COS;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Articles in international journals without selection committee.
	 */
	INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return JournalPaper.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.ASCL);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.ASCL;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** Articles in national journals without selection committee.
	 */
	NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return JournalPaper.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.ASCL);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.ASCL;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Keynotes in international conference.
	 */
	INTERNATIONAL_KEYNOTE {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return KeyNote.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.C_INV);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.C_INV;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** Keynotes in national conference.
	 */
	NATIONAL_KEYNOTE {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return KeyNote.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.C_INV);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.C_INV;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** HDR theses.
	 */
	HDR_THESIS {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Thesis.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.TH);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.TH;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** PhD theses.
	 */
	PHD_THESIS {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Thesis.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.TH);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.TH;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Master theses.
	 */
	MASTER_THESIS {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Thesis.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.TH);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.TH;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** International Patents.
	 */
	INTERNATIONAL_PATENT {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Patent.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.BRE);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.BRE;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** EU Patents.
	 */
	EUROPEAN_PATENT {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Patent.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.BRE);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.BRE;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** National Patents.
	 */
	NATIONAL_PATENT {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Patent.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.BRE);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.BRE;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Publications for research transfer.
	 */
	RESEARCH_TRANSFERT_REPORT {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Report.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.PT);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.PT;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Research tools (not software).
	 */
	RESEARCH_TOOL {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return MiscDocument.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.OR);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.OR;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Books for scientific culture dissemination.
	 */
	SCIENTIFIC_CULTURE_BOOK {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Book.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.OV);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.OV;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Seminar in scientific institution at international level.
	 */
	INTERNATIONAL_PRESENTATION {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return MiscDocument.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.AP);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.AP;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** Seminar in scientific institution at national level.
	 */
	NATIONAL_PRESENTATION {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return MiscDocument.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.AP);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.AP;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Chapters in a book for scientific culture dissemination.
	 */
	SCIENTIFIC_CULTURE_BOOK_CHAPTER {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return BookChapter.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.COV);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.COV;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Chapters in a book for scientific culture dissemination.
	 */
	SCIENTIFIC_CULTURE_PAPER {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return JournalPaper.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.PV);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.PV;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Fourm, public conference or seminar for scientific culture dissemination at international level.
	 */
	INTERNATIONAL_SCIENTIFIC_CULTURE_PRESENTATION {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return MiscDocument.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.AP);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.AP;
		}
		@Override
		public boolean isInternational() {
			return true;
		}
	},
	/** Public conference or seminar for scientific culture dissemination at national level.
	 */
	NATIONAL_SCIENTIFIC_CULTURE_PRESENTATION {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return MiscDocument.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.AP);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.AP;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Artistic research productions.
	 */
	ARTISTIC_PRODUCTION {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return MiscDocument.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.PAT);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.PAT;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Technical reports.
	 */
	TECHNICAL_REPORT {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Report.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.AP);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.AP;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Project reports.
	 */
	PROJECT_REPORT {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Report.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.AP);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.AP;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Teaching documents.
	 */
	TEACHING_DOCUMENT {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Report.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.AP);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.AP;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Tutorial or documentation.
	 */
	TUTORIAL_DOCUMENTATION {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return Report.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.AP);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.AP;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	},
	/** Other productions (registered software, reports...)
	 */
	OTHER {
		@Override
		public Class<? extends Publication> getInstanceType() {
			return MiscDocument.class;
		}
		@Override
		public Set<PublicationCategory> getCategories() {
			return Sets.newHashSet(PublicationCategory.AP);
		}
		@Override
		public PublicationCategory getCategory(boolean isRankedPublication) {
			return PublicationCategory.AP;
		}
		@Override
		public boolean isInternational() {
			return false;
		}
	};

	private static final String MESSAGE_PREFIX = "publicationType."; //$NON-NLS-1$

	private MessageSourceAccessor messages;

	/** Replies the message accessor to be used.
	 *
	 * @return the accessor.
	 */
	public MessageSourceAccessor getMessageSourceAccessor() {
		if (this.messages == null) {
			this.messages = BaseMessageSource.getStaticMessageSourceAccessor();
		}
		return this.messages;
	}

	/** Change the message accessor to be used.
	 *
	 * @param messages the accessor.
	 */
	public void setMessageSourceAccessor(MessageSourceAccessor messages) {
		this.messages = messages;
	}

	/** Replies the type of the publication instance that is implementing this type.
	 *
	 * @return the type, or {@code null} if there is no instance type for implementation.
	 */
	public abstract Class<? extends Publication> getInstanceType();

	/** Replies the publication categories associated to the publication type.
	 *
	 * @return the categories, never {@code null}.
	 */
	public abstract Set<PublicationCategory> getCategories();

	/** Replies the publication category associated to the publication type according to the
	 * given ranking status.
	 *
	 * @param isRankedPublication indicates the ranking status of the publication.
	 * @return the category, never {@code null}.
	 */
	public abstract PublicationCategory getCategory(boolean isRankedPublication);

	/** Replies if the paper is in an international support.
	 *
	 * @return {@code true} if the support of the publication is international.
	 */
	public abstract boolean isInternational();

	/** Replies the label of the status in the current language.
	 *
	 * @return the label of the status in the current language.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the publication type that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the type, to search for.
	 * @return the type.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type.
	 */
	public static PublicationType valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final PublicationType type : values()) {
				if (name.equalsIgnoreCase(type.name())) {
					return type;
				}
			}
		}
		throw new IllegalArgumentException("Invalid publication type: " + name); //$NON-NLS-1$
	}

	/** Replies if this publication type and the given publication type are considered as compatible.
	 * The compatibility test is based on the type of the JPA entity associated to the type.
	 *
	 * @param type the type to compare to the current type.
	 * @return {@code true} if both types are associated to the same type of JPA entity.
	 */
	public boolean isCompatibleWith(PublicationType type) {
		if (type == null) {
			return false;
		}
		final Class<? extends Publication> currentClass = getInstanceType();
		assert currentClass != null;
		final Class<? extends Publication> otherClass = type.getInstanceType();
		assert otherClass != null;
		return currentClass.equals(otherClass);
	}

}


