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

package fr.ciadlab.labmanager.entities.invitation;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/** Type of persin invitation.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public enum PersonInvitationType {

	/** Incoming guest professor.
	 */
	INCOMING_GUEST_PROFESSOR {
		@Override
		public boolean isOutgoing() {
			return false;
		}
	},

	/** Incoming guest student.
	 */
	INCOMING_GUEST_PHD_STUDENT {
		@Override
		public boolean isOutgoing() {
			return false;
		}
	},

	/** Incoming guest student.
	 */
	OUTGOING_GUEST {
		@Override
		public boolean isOutgoing() {
			return true;
		}
	};

	private static final String MESSAGE_PREFIX = "personInvitationType."; //$NON-NLS-1$

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

	/** Replies if this type is for outgoing invitations.
	 *
	 * @return {@code true} if the type of invitation is for outgoing invitations, {@code false} if it is for
	 *     incoming invitations.
	 */
	public abstract boolean isOutgoing();

	/** Replies the label of the type.
	 *
	 * @return the label of the type.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the type.
	 *
	 * @param locale the locale to use.
	 * @return the label of the type.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the jury membership that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the membership, to search for.
	 * @return the status.
	 * @throws IllegalArgumentException if the given name does not corresponds to a membership.
	 */
	public static PersonInvitationType valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final PersonInvitationType status : values()) {
				if (name.equalsIgnoreCase(status.name())) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("Invalid person invitation type: " + name); //$NON-NLS-1$
	}

}
