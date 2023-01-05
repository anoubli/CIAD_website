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

package fr.ciadlab.labmanager.io.od;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;

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
import org.apache.jena.ext.com.google.common.base.Strings;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.style.StyleTextPropertiesElement;
import org.odftoolkit.odfdom.dom.element.text.TextAElement;
import org.odftoolkit.odfdom.dom.element.text.TextHElement;
import org.odftoolkit.odfdom.dom.element.text.TextListElement;
import org.odftoolkit.odfdom.dom.element.text.TextListItemElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.odftoolkit.odfdom.type.Color;
import org.springframework.context.support.MessageSourceAccessor;

/** Exporter of publications to Open Document Text based on the ODF toolkit.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 * @see "https://odftoolkit.org"
 */
public abstract class AbstractOdfToolkitOpenDocumentTextExporter extends AbstractPublicationExporter implements OpenDocumentTextExporter {

	/** Green color for CIAD lab.
	 */
	public static final Color CIAD_GREEN = Color.valueOf("#95bc0f"); //$NON-NLS-1$

	/** Dark green color for CIAD lab.
	 */
	public static final Color CIAD_DARK_GREEN = Color.valueOf("#4b5e08"); //$NON-NLS-1$

	private static final String MESSAGES_PREFIX = "abstractOdfToolkitOpenDocumentTextExporter."; //$NON-NLS-1$

	/** Accessor to the localized messages.
	 */
	protected final MessageSourceAccessor messages;

	/** Constructor.
	 *
	 * @param messages the accessor to the localized message.
	 */
	public AbstractOdfToolkitOpenDocumentTextExporter(MessageSourceAccessor messages) {
		this.messages = messages;
	}

	/** Replies the string representation of left quotes.
	 *
	 * @return the left quotes.
	 */
	public String getLeftQuotes() {
		return this.messages.getMessage(MESSAGES_PREFIX + "LEFT_QUOTES"); //$NON-NLS-1$
	}

	/** Replies the string representation of right quotes.
	 *
	 * @return the left quotes.
	 */
	public String getRightQuotes() {
		return this.messages.getMessage(MESSAGES_PREFIX + "RIGHT_QUOTES"); //$NON-NLS-1$
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] exportPublications(Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws Exception {
		if (publications == null) {
			return null;
		}
		final OdfTextDocument odt = OdfTextDocument.newTextDocument();
		exportPublicationsWithGroupingCriteria(publications, configurator,
				it -> {
					try {
						final TextHElement element = odt.getContentRoot().newTextHElement(1);
						element.setTextContent(it);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				},
				it -> {
					try {
						final TextHElement element = odt.getContentRoot().newTextHElement(2);
						element.setTextContent(it);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				},
				it -> {
					try {
						exportFlatList(odt.getContentRoot().newTextListElement(), it, configurator);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}					
				});
		final byte[] content;
		try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			odt.save(output);
			output.flush();
			content = output.toByteArray();
		}
		return content;
	}

	/** Export the publications in a flat list.
	 *
	 * @param list the ODT list in which the publications must be exported.
	 * @param publications the publications to export.
	 * @param configurator the exporter configurator.
	 */
	protected void exportFlatList(TextListElement list, Iterable<? extends Publication> publications, ExporterConfigurator configurator) {
		for (final Publication publication : publications) {
			exportPublication(list, publication, configurator);
		}
	}

	/** Export in ODT a single publication.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator for the exporter.
	 */
	public void exportPublication(TextListElement odt, Publication publication, ExporterConfigurator configurator) {
		assert odt != null;
		assert publication != null;
		final TextListItemElement item = odt.newTextListItemElement();
		final TextPElement odtText = item.newTextPElement();
		final java.util.Locale loc = java.util.Locale.getDefault();
		try {
			java.util.Locale.setDefault(publication.getMajorLanguage().getLocale());
			exportAuthors(odtText, publication, configurator);
			exportDescription(odtText, publication, configurator);
		} finally {
			java.util.Locale.setDefault(loc);
		}
	}

	/** Replies the name of the person with a format compliant with the HTML output.
	 *
	 * @param odtText the receiver of the formatted name.
	 * @param person the person.
	 * @param year the year of the publication associated to the author.
	 * @param configurator the configurator for the exporter.
	 */
	protected static void formatAuthorName(TextPElement odtText, Person person, int year, ExporterConfigurator configurator) {
		assert configurator != null;
		final ExportedAuthorStatus status = configurator.getExportedAuthorStatusFor(person, year);
		final StringBuilder innerName = new StringBuilder();
		innerName.append(person.getFirstName());
		innerName.append(" "); //$NON-NLS-1$
		innerName.append(person.getLastName().toUpperCase());

		TextSpanElement span;
		switch (status) {
		case SELECTED_PERSON:
			span = newTextBoldUnderline(odtText);
			span.newTextNode(innerName.toString());
			break;
		case RESEARCHER:
			span = newTextBold(odtText);
			span.newTextNode(innerName.toString());
			break;
		case PHD_STUDENT:
			span = newTextUnderline(odtText);
			span.newTextNode(innerName.toString());
			break;
		case POSTDOC_ENGINEER:
			span = newTextItalic(odtText);
			span.newTextNode(innerName.toString());
			break;
		case OTHER:
		default:
			odtText.newTextNode(innerName.toString());
			break;
		}
	}

	/** Append the DOI link if the DOI is not empty.
	 *
	 * @param receiver the receiver of the ODT content.
	 * @param doi the DOI identifier.
	 * @param prefix the prefix to append to the DOI link.
	 * @return {@code true} if the receiver is changed.
	 */
	@SuppressWarnings("static-method")
	protected boolean appendDoiLink(TextPElement receiver, String doi, String prefix) {
		if (!Strings.isNullOrEmpty(doi)) {
			receiver.newTextNode(prefix);
			final TextAElement aElement = receiver.newTextAElement("https://doi.org/" + doi, null); //$NON-NLS-1$
			aElement.newTextNode(doi);
			return true;
		}
		return false;
	}

	/** Replies a text that is underlined.
	 * 
	 * @param odtText the container.
	 * @return the formating element.
	 */
	protected static TextSpanElement newTextUnderline(TextPElement odtText) {
		final TextSpanElement odtSpan = odtText.newTextSpanElement();
		odtSpan.setProperty(StyleTextPropertiesElement.TextUnderlineType, "single"); //$NON-NLS-1$
		odtSpan.setProperty(StyleTextPropertiesElement.TextUnderlineStyle, "solid"); //$NON-NLS-1$
		return odtSpan;
	}

	/** Replies a text that is italic.
	 * 
	 * @param odtText the container.
	 * @return the formating element.
	 */
	protected static TextSpanElement newTextItalic(TextPElement odtText) {
		final TextSpanElement odtSpan = odtText.newTextSpanElement();
		odtSpan.setProperty(StyleTextPropertiesElement.FontStyle, "italic"); //$NON-NLS-1$
		return odtSpan;
	}

	/** Replies a text that is bold.
	 * 
	 * @param odtText the container.
	 * @return the formating element.
	 */
	protected static TextSpanElement newTextBold(TextPElement odtText) {
		final TextSpanElement odtSpan = odtText.newTextSpanElement();
		odtSpan.setProperty(StyleTextPropertiesElement.FontWeight, "bold"); //$NON-NLS-1$
		return odtSpan;
	}

	/** Replies a text that is bold and underlined.
	 * 
	 * @param odtText the container.
	 * @return the formating element.
	 */
	protected static TextSpanElement newTextBoldUnderline(TextPElement odtText) {
		final TextSpanElement odtSpan = odtText.newTextSpanElement();
		odtSpan.setProperty(StyleTextPropertiesElement.FontWeight, "bold"); //$NON-NLS-1$
		odtSpan.setProperty(StyleTextPropertiesElement.TextUnderlineType, "single"); //$NON-NLS-1$
		odtSpan.setProperty(StyleTextPropertiesElement.TextUnderlineStyle, "solid"); //$NON-NLS-1$
		return odtSpan;
	}

	/** Replies the title with a format compliant with the HTML output.
	 * 
	 * @param odtText the receiver of the ODT content.
	 * @param title the title.
	 * @param configurator the configurator of the exporter.
	 */
	protected abstract void formatTitle(TextPElement odtText, String title, ExporterConfigurator configurator);

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

	/** Append the given values, with the given separator to the receiver.
	 *
	 * @param receiver the receiver.
	 * @param separator the separator of values.
	 * @param values the values to add.
	 * @return {@code true} if a value was added.
	 */
	protected static boolean append(TextPElement receiver, String separator, String... values) {
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
					receiver.newTextNode(separator);
				}
				receiver.newTextNode(value);
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
	protected static boolean append(TextPElement receiver, String value) {
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
	protected boolean appendRanks(TextPElement receiver, Object scimago, Object wos, float impactFactor) {
		final String impactFactorStr = formatNumberIfStrictlyPositive(impactFactor);
		String rank = null;
		if (scimago != null && wos != null) {
			if (wos != scimago) {
				final String scimagoStr = scimago.toString();
				final String wosStr = wos.toString();
				if (append(receiver, ", ", //$NON-NLS-1$
						decorateBefore(scimagoStr, this.messages.getMessage(MESSAGES_PREFIX + "SCIMAGO_PREFIX")), //$NON-NLS-1$
						decorateBefore(wosStr, this.messages.getMessage(MESSAGES_PREFIX + "WOS_PREFIX")), //$NON-NLS-1$
						decorateBefore(impactFactorStr, this.messages.getMessage(MESSAGES_PREFIX + "IMPACTFACTOR_PREFIX")))) { //$NON-NLS-1$
					receiver.newTextNode(". "); //$NON-NLS-1$
					return true;
				}		
			} else {
				rank = scimago.toString();
			}
		} else if (scimago != null) {
			rank = scimago.toString();
		} else if (wos != null) {
			rank = wos.toString();
		}
		if (append(receiver, ", ", //$NON-NLS-1$
				decorateBefore(rank, this.messages.getMessage(MESSAGES_PREFIX + "JOURNALRANK_PREFIX")), //$NON-NLS-1$
				decorateBefore(impactFactorStr, this.messages.getMessage(MESSAGES_PREFIX + "IMPACTFACTOR_PREFIX")))) { //$NON-NLS-1$
			receiver.newTextNode(". "); //$NON-NLS-1$
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

	/** Export in ODT the description of a single book.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, Book publication);

	/** Export in ODT the description of a single book chapter.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, BookChapter publication);

	/** Export in ODT the description of a single conference paper.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, ConferencePaper publication);

	/** Export in ODT the description of a single journal paper.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, JournalPaper publication);

	/** Export in ODT the description of a single journal edition.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, JournalEdition publication);

	/** Export in ODT the description of a single key-note.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, KeyNote publication);

	/** Export in ODT the description of a single report.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, Report publication);

	/** Export in ODT the description of a single thesis.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, Thesis publication);

	/** Export in ODT the description of a single patent.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, Patent publication);

	/** Export in ODT the description of a single document.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, MiscDocument publication);


	/** Export in ODT the description of a single publication.
	 *
	 * @param odtText the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator of the exporter.
	 */
	protected void exportDescription(TextPElement odtText, Publication publication, ExporterConfigurator configurator) {
		formatTitle(odtText, publication.getTitle(), configurator);

		final Class<? extends Publication> publicationClass = publication.getType().getInstanceType();
		assert publicationClass != null;

		if (publicationClass.equals(Book.class)) {
			exportDescription(odtText, (Book) publication);
		} else if (publicationClass.equals(BookChapter.class)) {
			exportDescription(odtText, (BookChapter) publication);
		} else if (publicationClass.equals(ConferencePaper.class)) {
			exportDescription(odtText, (ConferencePaper) publication);
		} else if (publicationClass.equals(JournalEdition.class)) {
			exportDescription(odtText, (JournalEdition) publication);
		} else if (publicationClass.equals(JournalPaper.class)) {
			exportDescription(odtText, (JournalPaper) publication);
		} else if (publicationClass.equals(KeyNote.class)) {
			exportDescription(odtText, (KeyNote) publication);
		} else if (publicationClass.equals(Report.class)) {
			exportDescription(odtText, (Report) publication);
		} else if (publicationClass.equals(Thesis.class)) {
			exportDescription(odtText, (Thesis) publication);
		} else if (publicationClass.equals(Patent.class)) {
			exportDescription(odtText, (Patent) publication);
		} else if (publicationClass.equals(MiscDocument.class)) {
			exportDescription(odtText, (MiscDocument) publication);
		} else {
			throw new IllegalArgumentException("Unsupported publication type: " + publication.getType()); //$NON-NLS-1$
		}

		final LocalDate date = publication.getPublicationDate();
		if (date != null) {
			final Month month = date.getMonth();
			assert month != null;
			final String displayName = month.getDisplayName(TextStyle.FULL_STANDALONE, java.util.Locale.getDefault());
			odtText.newTextNode(displayName);
			odtText.newTextNode(", "); //$NON-NLS-1$
		}
		final int year = publication.getPublicationYear();
		odtText.newTextNode(Integer.toString(year));
		odtText.newTextNode(". "); //$NON-NLS-1$
	}


	/** Export in ODT the authors of a single publication.
	 *
	 * @param odtText the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator for the exporter.
	 */
	@SuppressWarnings("static-method")
	protected void exportAuthors(TextPElement odtText, Publication publication, ExporterConfigurator configurator) {
		assert configurator != null;
		final int year = publication.getPublicationYear();
		boolean first = true;
		for (final Person person : publication.getAuthors()) {
			if (first) {
				first = false;
			} else {
				odtText.newTextNode(", "); //$NON-NLS-1$
			}
			formatAuthorName(odtText, person, year, configurator);
		}
		odtText.newTextNode(". "); //$NON-NLS-1$
	}

}
