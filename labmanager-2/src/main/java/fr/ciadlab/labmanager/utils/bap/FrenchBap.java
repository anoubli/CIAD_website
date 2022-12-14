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

package fr.ciadlab.labmanager.utils.bap;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import fr.ciadlab.labmanager.utils.cnu.CnuSection;
import fr.ciadlab.labmanager.utils.conrs.ConrsSection;
import org.springframework.context.support.MessageSourceAccessor;

/** The French BAP is a classification of the types of jobs that engineers, technicians and other
 * not researcher staff could do. BAP means "Branch d'activité professionnelle".
 * Usually, a staff meber who is not researcher in public institution is associated to BAP.
 * <p>Rsearcher classification are {@link CnuSection CNU section} and {@link ConrsSection CoNRS section}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see "https://www.galaxie.enseignementsup-recherche.gouv.fr/ensup/pdf/qualification/sections.pdf"
 * @see CnuSection
 * @see ConrsSection
 */
public enum FrenchBap {
	/** BAP A.
	 */
	BAP_A {
		@Override
		public String getShortName() {
			return "A"; //$NON-NLS-1$
		}
	},
	/** BAP B.
	 */
	BAP_B {
		@Override
		public String getShortName() {
			return "B"; //$NON-NLS-1$
		}
	},
	/** BAP C.
	 */
	BAP_C {
		@Override
		public String getShortName() {
			return "C"; //$NON-NLS-1$
		}
	},
	/** BAP D.
	 */
	BAP_D {
		@Override
		public String getShortName() {
			return "D"; //$NON-NLS-1$
		}
	},
	/** BAP E.
	 */
	BAP_E {
		@Override
		public String getShortName() {
			return "E"; //$NON-NLS-1$
		}
	},
	/** BAP F.
	 */
	BAP_F {
		@Override
		public String getShortName() {
			return "F"; //$NON-NLS-1$
		}
	},
	/** BAP G.
	 */
	BAP_G {
		@Override
		public String getShortName() {
			return "G"; //$NON-NLS-1$
		}
	},
	/** BAP J.
	 */
	BAP_J {
		@Override
		public String getShortName() {
			return "J"; //$NON-NLS-1$
		}
	};

	private static final String MESSAGE_PREFIX = "frenchBap."; //$NON-NLS-1$

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

	/** Replies the label of the French BAP in the current language.
	 *
	 * @return the label of the French BAP in the current language.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the French BAP in the current language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the French BAP in the current language.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the French BAP that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the French BAP, to search for.
	 * @return the French BAP.
	 * @throws IllegalArgumentException if the given name does not corresponds to a French BAP.
	 */
	public static FrenchBap valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final FrenchBap section : values()) {
				if (name.equalsIgnoreCase(section.name()) || name.equalsIgnoreCase(section.getShortName())) {
					return section;
				}
			}
		}
		throw new IllegalArgumentException("Invalid French BAP: " + name); //$NON-NLS-1$
	}

	/** Replies the short name of this French BAP. The sohrt name is usually a letter. 
	 *
	 * @return the short name.
	 */
	public abstract String getShortName();

}
