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

package fr.ciadlab.labmanager.configuration;

import java.util.EnumSet;
import java.util.Locale;

import javax.annotation.PostConstruct;

import fr.ciadlab.labmanager.controller.api.member.GeneralMemberType;
import fr.ciadlab.labmanager.entities.jury.JuryMembershipType;
import fr.ciadlab.labmanager.entities.jury.JuryType;
import fr.ciadlab.labmanager.entities.member.Gender;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Responsibility;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
import fr.ciadlab.labmanager.entities.publication.PublicationCategory;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.supervision.SupervisorType;
import fr.ciadlab.labmanager.utils.bap.FrenchBap;
import fr.ciadlab.labmanager.utils.cnu.CnuSection;
import fr.ciadlab.labmanager.utils.conrs.ConrsSection;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import fr.ciadlab.labmanager.utils.ranking.JournalRankingSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/** Base implementation of a message source accessor, outside Spring run.
 * This source is implemented for enabling the enumeration types to use
 * the localized messages.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Configuration
public class BaseMessageSource {

	private static MessageSource STATIC_SOURCE;

	private static MessageSourceAccessor STATIC_ACCESSOR;

	/** Get the message source that is lazily and statically loaded.
	 *
	 * @return the message source.
	 */
	public static MessageSource getStaticMessageSource() {
		if (STATIC_SOURCE == null) {
			STATIC_SOURCE = createMessageSource();
		}
		return STATIC_SOURCE;
	}

	/** Get the message source accessor that is lazily and statically loaded.
	 *
	 * @return the message source.
	 */
	public static MessageSourceAccessor getStaticMessageSourceAccessor() {
		if (STATIC_ACCESSOR == null) {
			STATIC_ACCESSOR = createMessageSourceAccessor(getStaticMessageSource());
		}
		return STATIC_ACCESSOR;
	}

	/** Create a well-configured message source.
	 *
	 * @return the message source.
	 */
	public static MessageSource createMessageSource() {
		final ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
		//res.setDefaultEncoding("UTF-8"); //$NON-NLS-1$
		// Force to use specified locale even if locale for current env is different
		res.setFallbackToSystemLocale(false);
		res.addBasenames("classpath:messages/messages"); //$NON-NLS-1$
		return res;
	}

	/** Create a well-configured message source accessor.
	 *
	 * @param source the message source.
	 * @return the message source accessor.
	 */
	public static MessageSourceAccessor createMessageSourceAccessor(MessageSource source) {
		return new MessageSourceAccessor(source, Locale.US);
	}

	/** Replies the source of the localized messages.
	 *
	 * @return the manager of the messages.
	 */
	@SuppressWarnings("static-method")
	@Bean(name = "messageSource")
	public MessageSource getMessageSource() {
		return createMessageSource();
	}

	/** Replies the tool for accessing to the localized source that is provided as argument.
	 *
	 * @param messageSource the source to embed.
	 * @return the accessor to the message source.
	 */
	@SuppressWarnings("static-method")
	@Bean
	public MessageSourceAccessor getMessageSourceAccessor(final MessageSource messageSource) {
		return createMessageSourceAccessor(messageSource);
	}

	/** Injectors of autowired elements in enumeration types.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@Component
	public static class EnumerationInjector {

		@Autowired
		private MessageSourceAccessor messages;

		/**
		 * Invoked by Spring engine after the class is created in memory.
		 */
		@PostConstruct
		public void postConstruct() {
			for (final ResearchOrganizationType item : EnumSet.allOf(ResearchOrganizationType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (MemberStatus item : EnumSet.allOf(MemberStatus.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (Responsibility item : EnumSet.allOf(Responsibility.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (PublicationCategory item : EnumSet.allOf(PublicationCategory.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (PublicationType item : EnumSet.allOf(PublicationType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (Gender item : EnumSet.allOf(Gender.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (CnuSection item : EnumSet.allOf(CnuSection.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (ConrsSection item : EnumSet.allOf(ConrsSection.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (FrenchBap item : EnumSet.allOf(FrenchBap.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (FundingScheme item : EnumSet.allOf(FundingScheme.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (GeneralMemberType item : EnumSet.allOf(GeneralMemberType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (JuryMembershipType item : EnumSet.allOf(JuryMembershipType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (JuryType item : EnumSet.allOf(JuryType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (SupervisorType item : EnumSet.allOf(SupervisorType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (JournalRankingSystem item : EnumSet.allOf(JournalRankingSystem.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
		}
	}

}
