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

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/** Callbacks for MVC architecture.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Configuration
@EnableWebMvc
@Import(DefaultWebMvcConfiguration.class)
public class MvcConfig implements WebMvcConfigurer {

	@Autowired
	private Environment env;

	/** Bean for converting the HTTP message.
	 *
	 * @return the converter.
	 */
	@SuppressWarnings("static-method")
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		return new StringHttpMessageConverter(Charset.forName("UTF-8")); //$NON-NLS-1$
	}

	/** Provide a bean for resolving HTML templates.
	 *
	 * @return the resolver.
	 */
	@SuppressWarnings("static-method")
	@Bean
	ITemplateResolver templateResolver() {
		final ClassLoaderTemplateResolver htmlResolver = new ClassLoaderTemplateResolver();
		htmlResolver.setPrefix("/templates/"); //$NON-NLS-1$
		htmlResolver.setSuffix(".html"); //$NON-NLS-1$
		htmlResolver.setOrder(Integer.valueOf(0));
		htmlResolver.setTemplateMode(TemplateMode.HTML);
		htmlResolver.setCharacterEncoding("UTF-8"); //$NON-NLS-1$
		htmlResolver.setCacheable(true);
		return htmlResolver;
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {
		//
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer contentNegotiationConfigurer) {
		//
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer) {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("mvc-task-executor-"); //$NON-NLS-1$
        executor.initialize();
        asyncSupportConfigurer.setTaskExecutor(executor);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {
		//
	}

	@Override
	public void addFormatters(FormatterRegistry formatterRegistry) {
		//
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/") //$NON-NLS-1$//$NON-NLS-2$
		.setCacheControl(CacheControl.maxAge(15, TimeUnit.DAYS));
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/") //$NON-NLS-1$ //$NON-NLS-2$
		.setCacheControl(CacheControl.maxAge(15, TimeUnit.DAYS));
		if (this.env != null) {
			String resources = this.env.getProperty("labmanager.web.publish-resources"); //$NON-NLS-1$
			if (!Strings.isNullOrEmpty(resources)) {
				for (final String resource : resources.split("\\s*[,;:]+\\s*")) { //$NON-NLS-1$
					final String basename = FileSystem.basename(resource);
					final URL url = FileSystem.convertStringToURL("file:" + resource, false); //$NON-NLS-1$
					String resourcePath = url.toExternalForm();
					if (!resourcePath.endsWith(FileSystem.URL_PATH_SEPARATOR)) {
						resourcePath += FileSystem.URL_PATH_SEPARATOR;
					}
					registry.addResourceHandler("/" + basename + "/**") //$NON-NLS-1$//$NON-NLS-2$
					.addResourceLocations(resourcePath)
					.setCacheControl(CacheControl.maxAge(15, TimeUnit.DAYS));
				}
			}
		}
	}

	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {
		//
	}

	@Override
	public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
		//
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry viewResolverRegistry) {
		//
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {
		//
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {
		//
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> list) {
		//
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> list) {
		//
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
		//
	}

	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
		//
	}

	@Override
	public Validator getValidator() {
		return null;
	}

	@Override
	public MessageCodesResolver getMessageCodesResolver() {
		return null;
	}

}

