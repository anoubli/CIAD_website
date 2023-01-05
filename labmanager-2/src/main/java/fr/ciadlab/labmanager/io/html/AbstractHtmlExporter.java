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

package fr.ciadlab.labmanager.io.html;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Publication;
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
import fr.ciadlab.labmanager.io.AbstractPublicationExporter;
import fr.ciadlab.labmanager.io.ExportedAuthorStatus;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.utils.doi.DoiTools;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.context.support.MessageSourceAccessor;

/** Utilities for exporting publications to HTML.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public abstract class AbstractHtmlExporter extends AbstractPublicationExporter implements HtmlExporter {

	private static final String MESSAGES_PREFIX = "abstractHtmlExporter."; //$NON-NLS-1$

	/** Application constants.
	 */
	protected final Constants constants;

	/** Tools for managing DOI.
	 */
	protected final DoiTools doiTools;

	/** Provider of localized messages.
	 */
	protected final MessageSourceAccessor messages;

	/** Constructor.
	 *
	 * @param constants the accessor to the application constants.
	 * @param messages the accessor to the localized messages.
	 * @param doiTools the accessor to the DOI tools.
	 */
	public AbstractHtmlExporter(Constants constants, MessageSourceAccessor messages, DoiTools doiTools) {
		this.constants = constants;
		this.messages = messages;
		this.doiTools = doiTools;
	}

	/** Replies the string representation of left quotes.
	 *
	 * @return the left quotes.
	 */
	public static String getLeftQuotes() {
		return "&ldquo;"; //$NON-NLS-1$
	}

	/** Replies the string representation of right quotes.
	 *
	 * @return the left quotes.
	 */
	public static String getRightQuotes() {
		return "&rdquo;"; //$NON-NLS-1$
	}

	@Override
	public String separator() {
		return "&nbsp;"; //$NON-NLS-1$
	}

	@Override
	public String doubleSeparator() {
		return separator() + separator();
	}

	/** Replies the name of the person with a format compliant with the HTML output.
	 *
	 * @param person the person.
	 * @param year the year of the publication associated to the author.
	 * @param configurator the exporter configurator.
	 * @return the formatted person name.
	 */
	@SuppressWarnings("static-method")
	protected String formatAuthorName(Person person, int year, ExporterConfigurator configurator) {
		assert configurator != null;
		final ExportedAuthorStatus status = configurator.getExportedAuthorStatusFor(person, year);
		final StringBuilder innerName = new StringBuilder();
		innerName.append(toHtml(person.getFirstName()));
		innerName.append(" "); //$NON-NLS-1$
		innerName.append(toHtml(person.getLastName().toUpperCase()));

		final StringBuilder formattedName = new StringBuilder();
		switch (status) {
		case SELECTED_PERSON:
			if (configurator.isSelectedPersonNameFormatted()) {
				formattedName.append("<b><u>"); //$NON-NLS-1$
				formattedName.append(innerName);
				formattedName.append("</u></b>"); //$NON-NLS-1$
			} else {
				formattedName.append(innerName);
			}
			break;
		case RESEARCHER:
			if (configurator.isResearcherNameFormatted()) {
				formattedName.append("<b>"); //$NON-NLS-1$
				formattedName.append(innerName);
				formattedName.append("</b>"); //$NON-NLS-1$
			} else {
				formattedName.append(innerName);
			}
			break;
		case PHD_STUDENT:
			if (configurator.isPhDStudentNameFormatted()) {
				formattedName.append("<u>"); //$NON-NLS-1$
				formattedName.append(innerName);
				formattedName.append("</u>"); //$NON-NLS-1$
			} else {
				formattedName.append(innerName);
			}
			break;
		case POSTDOC_ENGINEER:
			if (configurator.isPostdocEngineerNameFormatted()) {
				formattedName.append("<i>"); //$NON-NLS-1$
				formattedName.append(innerName);
				formattedName.append("</i>"); //$NON-NLS-1$
			} else {
				formattedName.append(innerName);
			}
			break;
		case OTHER:
		default:
			formattedName.append(innerName);
			break;
		}
		return formattedName.toString();
	}

	/** Replies the title with a format compliant with the HTML output.
	 * 
	 * @param title the title.
	 * @param configurator the configurator of the exporter.
	 * @return the formatted title.
	 */
	protected abstract String formatTitle(String title, ExporterConfigurator configurator);

	/** Replies the string representation of the given number if it is strictly positive.
	 * 
	 * @param number the number.
	 * @return the formatted number or {@code null} if the number is negative or nul.
	 */
	protected static String formatNumberIfStrictlyPositive(float number) {
		if (number > 0f) {
			final NumberFormat format = new DecimalFormat("#0.000"); //$NON-NLS-1$
			return format.format(number);
		}
		return null;
	}

	/** Replies the HTML string representation of the given UTF8 string.
	 * 
	 * @param utfString the UTF8 string.
	 * @return the HTML string.
	 */
	protected static String toHtml(CharSequence utfString) {
		if (utfString == null || utfString.length() == 0) {
			return Strings.nullToEmpty(null);
		}
		int len = utfString.length();
		final StringBuffer buffer = new StringBuffer(len);
		// true if last char was blank
		boolean lastWasBlankChar = false;
		for (int i = 0; i < len; i++) {
			final char c = utfString.charAt(i);
			if (c == ' ') {
				// blank gets extra work,
				// this solves the problem you get if you replace all
				// blanks with &nbsp;, if you do that you loss 
				// word breaking
				if (lastWasBlankChar) {
					lastWasBlankChar = false;
					buffer.append("&nbsp;"); //$NON-NLS-1$
				} else {
					lastWasBlankChar = true;
					buffer.append(' ');
				}
			} else {
				lastWasBlankChar = false;
				// HTML Special Chars
				if (c == '"')
					buffer.append("&quot;"); //$NON-NLS-1$
				else if (c == '&')
					buffer.append("&amp;"); //$NON-NLS-1$
				else if (c == '<')
					buffer.append("&lt;"); //$NON-NLS-1$
				else if (c == '>')
					buffer.append("&gt;"); //$NON-NLS-1$
				else if (c == '\n')
					// Handle Newline
					buffer.append("<br/>"); //$NON-NLS-1$
				else {
					int ci = 0xffff & c;
					if (ci < 160)
						// nothing special only 7 Bit
						buffer.append(c);
					else {
						// Not 7 Bit use the unicode system
						buffer.append("&#"); //$NON-NLS-1$
						buffer.append(Integer.toString(ci));
						buffer.append(';');
					}
				}
			}
		}
		return buffer.toString();
	}

	/** Build the DOI link if the DOI is not empty.
	 *
	 * @param doi the DOI identifier.
	 * @return the link or {@code null}.
	 */
	protected String buildDoiLink(String doi) {
		if (!Strings.isNullOrEmpty(doi)) {
			return "<a href=\"" + this.doiTools.getDOIUrlFromDOINumber(doi).toExternalForm() + "\">" //$NON-NLS-1$ //$NON-NLS-2$
					+ toHtml(doi) + "</a>"; //$NON-NLS-1$
		}
		return null;
	}

	/** Append the given values, with the given separator to the receiver.
	 *
	 * @param receiver the receiver.
	 * @param separator the separator of values.
	 * @param values the values to add.
	 * @return {@code true} if a value was added.
	 */
	protected static boolean append(StringBuilder receiver, String separator, String... values) {
		assert receiver != null;
		assert separator != null;
		assert values != null;
		boolean first = true;
		boolean added = false;
		for (final String value : values) {
			if (!Strings.isNullOrEmpty(value)) {
				if (first) {
					first = false;
				} else {
					receiver.append(separator);
				}
				receiver.append(value);
				added = true;
			}
		}
		return added;
	}

	/** Append the given value if it is not empty.
	 *
	 * @param receiver the receiver.
	 * @param values the values to add.
	 * @return {@code true} if a value was added.
	 */
	protected static boolean append(StringBuilder receiver, String value) {
		return append(receiver, "", value); //$NON-NLS-1$
	}

	/** Append the given ranks to the receiver.
	 *
	 * @param receiver the receiver of the HTML.
	 * @param scimago the Scimago ranking.
	 * @param wos the Web-of-Science ranking.
	 * @param impactFactor the journal's impact factor.
	 * @return {@code true} if the receiver has changed.
	 */
	protected boolean appendRanks(StringBuilder receiver, Object scimago, Object wos, float impactFactor) {
		final String impactFactorStr = formatNumberIfStrictlyPositive(impactFactor);
		String rank = null;
		if (scimago != null && wos != null) {
			if (wos != scimago) {
				final String scimagoStr = toHtml(scimago.toString());
				final String wosStr = toHtml(wos.toString());
				if (append(receiver, ", ", //$NON-NLS-1$
						decorateBefore(scimagoStr, this.messages.getMessage(MESSAGES_PREFIX + "SCIMAGO_PREFIX")), //$NON-NLS-1$
						decorateBefore(wosStr, this.messages.getMessage(MESSAGES_PREFIX + "WOS_PREFIX")), //$NON-NLS-1$
						decorateBefore(impactFactorStr, this.messages.getMessage(MESSAGES_PREFIX + "IMPACTFACTOR_PREFIX")))) { //$NON-NLS-1$
					receiver.append(". "); //$NON-NLS-1$
					return true;
				}		
			} else {
				rank = toHtml(scimago.toString());
			}
		} else if (scimago != null) {
			rank = toHtml(scimago.toString());
		} else if (wos != null) {
			rank = toHtml(wos.toString());
		}
		if (append(receiver, ", ", //$NON-NLS-1$
				decorateBefore(rank, this.messages.getMessage(MESSAGES_PREFIX + "JOURNALRANK_PREFIX")), //$NON-NLS-1$
				decorateBefore(impactFactorStr, this.messages.getMessage(MESSAGES_PREFIX + "IMPACTFACTOR_PREFIX")))) { //$NON-NLS-1$
			receiver.append(". "); //$NON-NLS-1$
			return true;
		}		
		return false;
	}

	/** Replies the value preceded by its decoration if the value is not {@code null} or empty.
	 *
	 * @param value the value to decorate.
	 * @param decorator the text to append before the value.
	 * @return the decorated value.
	 */
	protected static String decorateBefore(String value, String decorator) {
		assert decorator != null;
		if (!Strings.isNullOrEmpty(value)) {
			return decorator + value;
		}
		return ""; //$NON-NLS-1$
	}

	/** Export in HTML the description of a single book.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(StringBuilder html, Book publication);

	/** Export in HTML the description of a single book chapter.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(StringBuilder html, BookChapter publication);

	/** Export in HTML the description of a single conference paper.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(StringBuilder html, ConferencePaper publication);

	/** Export in HTML the description of a single journal paper.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(StringBuilder html, JournalPaper publication);

	/** Export in HTML the description of a single journal edition.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(StringBuilder html, JournalEdition publication);

	/** Export in HTML the description of a single key-note.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(StringBuilder html, KeyNote publication);

	/** Export in HTML the description of a single report.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(StringBuilder html, Report publication);

	/** Export in HTML the description of a single thesis.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(StringBuilder html, Thesis publication);

	/** Export in HTML the description of a single patent.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(StringBuilder html, Patent publication);

	/** Export in HTML the description of a single document.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(StringBuilder html, MiscDocument publication);

	/** Export in HTML the description of a single publication.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator of the exporter.
	 */
	protected void exportDescription(StringBuilder html, Publication publication, ExporterConfigurator configurator) {
		html.append(formatTitle(publication.getTitle(), configurator));

		final Class<? extends Publication> publicationClass = publication.getType().getInstanceType();
		assert publicationClass != null;

		if (publicationClass.equals(Book.class)) {
			exportDescription(html, (Book) publication);
		} else if (publicationClass.equals(BookChapter.class)) {
			exportDescription(html, (BookChapter) publication);
		} else if (publicationClass.equals(ConferencePaper.class)) {
			exportDescription(html, (ConferencePaper) publication);
		} else if (publicationClass.equals(JournalEdition.class)) {
			exportDescription(html, (JournalEdition) publication);
		} else if (publicationClass.equals(JournalPaper.class)) {
			exportDescription(html, (JournalPaper) publication);
		} else if (publicationClass.equals(KeyNote.class)) {
			exportDescription(html, (KeyNote) publication);
		} else if (publicationClass.equals(Report.class)) {
			exportDescription(html, (Report) publication);
		} else if (publicationClass.equals(Thesis.class)) {
			exportDescription(html, (Thesis) publication);
		} else if (publicationClass.equals(Patent.class)) {
			exportDescription(html, (Patent) publication);
		} else if (publicationClass.equals(MiscDocument.class)) {
			exportDescription(html, (MiscDocument) publication);
		} else {
			throw new IllegalArgumentException("Unsupported publication type: " + publication.getType()); //$NON-NLS-1$
		}

		final LocalDate date = publication.getPublicationDate();
		if (date != null) {
			final Month month = date.getMonth();
			assert month != null;
			final String displayName = month.getDisplayName(TextStyle.FULL_STANDALONE, java.util.Locale.getDefault());
			html.append(toHtml(displayName));
			html.append(", "); //$NON-NLS-1$
		}
		final int year = publication.getPublicationYear();
		html.append(year);
		html.append("."); //$NON-NLS-1$
	}

}
