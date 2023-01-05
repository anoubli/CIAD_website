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

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

/** Override of the MVC configuration for forcing the locale definition.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
public class DefaultWebMvcConfiguration extends DelegatingWebMvcConfiguration {

	/** Replies the locale resolver that must be used by this application.
	 *
	 * @return the locale resolver.
	 */
	@Bean
	@Override
	public LocaleResolver localeResolver() {
		//final SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		final FixedLocaleResolver localeResolver = new FixedLocaleResolver();
		localeResolver.setDefaultLocale(defaultLocale());
		return localeResolver;
	}

	/** Replies the default locale.
	 *
	 * @return the default locale.
	 */
	@SuppressWarnings("static-method")
	@Bean
	public Locale defaultLocale() {
		return Locale.ENGLISH;
	}

}
