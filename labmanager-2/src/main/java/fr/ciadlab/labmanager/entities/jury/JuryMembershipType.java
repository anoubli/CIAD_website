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

package fr.ciadlab.labmanager.entities.jury;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import fr.ciadlab.labmanager.entities.member.Gender;
import org.springframework.context.support.MessageSourceAccessor;

/** Type of membership for a jury.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public enum JuryMembershipType {

	/** President of the jury.
	 */
	PRESIDENT,

	/** Reviewer.
	 */
	REVIEWER,

	/** Examiner.
	 */
	EXAMINER,

	/** Invited person.
	 */
	INVITED_PERSON;

	private static final String MESSAGE_PREFIX = "juryMembershipType."; //$NON-NLS-1$

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

	/** Replies the label of the status in the current language.
	 *
	 * @param gender the gender of the person who has the position of this type.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Gender gender) {
		Gender g = gender;
		if (g == null || g == Gender.NOT_SPECIFIED) {
			g = Gender.OTHER;
		}
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + "_" + g.name()); //$NON-NLS-1$
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param gender the gender of the person who has the position of this type.
	 * @param locale the locale to use.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Gender gender, Locale locale) {
		Gender g = gender;
		if (g == null || g == Gender.NOT_SPECIFIED) {
			g = Gender.OTHER;
		}
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + "_" + g.name(), locale); //$NON-NLS-1$
		return Strings.nullToEmpty(label);
	}

	/** Replies the jury membership that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the membership, to search for.
	 * @return the status.
	 * @throws IllegalArgumentException if the given name does not corresponds to a membership.
	 */
	public static JuryMembershipType valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final JuryMembershipType status : values()) {
				if (name.equalsIgnoreCase(status.name())) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("Invalid jury membership type: " + name); //$NON-NLS-1$
	}

}
