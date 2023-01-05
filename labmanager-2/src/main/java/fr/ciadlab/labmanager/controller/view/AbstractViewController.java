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

package fr.ciadlab.labmanager.controller.view;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.AbstractCredentialController;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** Abstract implementation of a JEE Controller that provides views.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractViewController extends AbstractCredentialController {

	/** Factory of URI builder.
	 */
	protected final UriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();

	private static final String EMAIL_PATTERN_VALUE = "^([^@]+)@(.*?)\\.([^\\.]+)$"; //$NON-NLS-1$
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_PATTERN_VALUE);

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public AbstractViewController(MessageSourceAccessor messages, Constants constants, String usernameKey) {
		super(messages, constants, usernameKey);
	}

	/** Build the URL for accessing an endpoint with the given parameter name, but without setting the parameter value. 
	 *
	 * @param endpointName the name of the endpoint.
	 * @param parameterName the parameter name.
	 * @return the endpoint URL.
	 */
	protected String endpoint(String endpointName, String parameterName) {
		return endpoint(endpointName, parameterName, ""); //$NON-NLS-1$
	}

	/** Build the URL for accessing an endpoint. 
	 *
	 * @param endpointName the name of the endpoint.
	 * @return the endpoint URL.
	 */
	protected String endpoint(String endpointName) {
		final UriBuilder b = endpointUriBuilder(endpointName);
		return b.build().toASCIIString();
	}

	/** Build the URL for accessing an endpoint with the given parameter name, but without setting the parameter value. 
	 *
	 * @param endpointName the name of the endpoint.
	 * @param parameterName the parameter name.
	 * @param parameterValue the parameter value.
	 * @return the endpoint URL.
	 */
	protected String endpoint(String endpointName, String parameterName, Object value) {
		final UriBuilder b = endpointUriBuilder(endpointName);
		if (!Strings.isNullOrEmpty(parameterName)) {
			b.queryParam(parameterName, value);
		}
		return b.build().toASCIIString();
	}

	/** Create the URL builder for accessing an endpoint. 
	 *
	 * @param endpointName the name of the endpoint.
	 * @return the endpoint URL. builder
	 */
	protected UriBuilder endpointUriBuilder(String endpointName) {
		final UriBuilder b = this.uriBuilderFactory.builder();
		b.path("/" + this.constants.getServerName() + "/" + endpointName); //$NON-NLS-1$ //$NON-NLS-2$
		return b;
	}

	/** Build the URL from the root of the JPA server. 
	 *
	 * @param relativeFile the relative path to append to the server's root.
	 * @return the rooted URL.
	 */
	protected String rooted(File relativeFile) {
		final StringBuilder bb = new StringBuilder();
		File f = relativeFile;
		while (f != null) {
			bb.insert(0, f.getName()).insert(0, "/"); //$NON-NLS-1$
			f = f.getParentFile();
		}
		final UriBuilder b = this.uriBuilderFactory.builder();
		b.path("/" + this.constants.getServerName() + bb.toString()); //$NON-NLS-1$
		return b.build().toASCIIString();
	}

	/** Build the URL from the root of the JPA server. 
	 *
	 * @param relativeUrl the relative URL to append to the server's root.
	 * @return the rooted URL.
	 */
	protected String rooted(String relativeUrl) {
		final UriBuilder b = this.uriBuilderFactory.builder();
		b.path("/" + this.constants.getServerName() + "/" + relativeUrl); //$NON-NLS-1$ //$NON-NLS-2$
		return b.build().toASCIIString();
	}

	/** Fill the attributes of the given model-view with the standard properties.
	 * The added object in the model are: {@code uuid} and {@code changeEnabled}.
	 *
	 * @param modelAndView the model-view
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 */
	protected void initModelViewWithInternalProperties(ModelAndView modelAndView, boolean embedded) {
		modelAndView.addObject("uuid", generateUUID()); //$NON-NLS-1$
		modelAndView.addObject("changeEnabled", Boolean.valueOf(isLoggedIn())); //$NON-NLS-1$
		modelAndView.addObject("isEmbeddedPage", Boolean.valueOf(embedded)); //$NON-NLS-1$
	}

	/** Fill the attributes that are needed to build up the buttons in an admin table.
	 *
	 * @param modelAndView the model-view
	 * @param editUrl the URL for editing an entity. The id of the entity will be appended to the given URL.
	 */
	protected void initAdminTableButtons(ModelAndView modelAndView, String editUrl) {
		modelAndView.addObject("URLS_edit", editUrl); //$NON-NLS-1$
		modelAndView.addObject("URLS_exportBibTeX", endpoint(Constants.EXPORT_BIBTEX_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("URLS_exportOdt", endpoint(Constants.EXPORT_ODT_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("URLS_exportHtml", endpoint(Constants.EXPORT_HTML_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("URLS_exportJson", endpoint(Constants.EXPORT_JSON_ENDPOINT, null)); //$NON-NLS-1$
	}

	/** Fill the attributes that describe the components of an obfuscated email.
	 *
	 * @param values the attribute values to fill up.
	 * @param email the email to obfuscate.
	 * @param id the identifier to add to the attribute names for making them unique.
	 */
	@SuppressWarnings("static-method")
	protected void addObfuscatedEmailFields(Map<String, Object> values, String email, String id) {
		if (!Strings.isNullOrEmpty(email)) {
			final Matcher matcher = EMAIL_PATTERN.matcher(email);
			if (matcher.matches()) {
				final String name = matcher.group(1);
				final String name0;
				final String name1;
				if (name.length() >= 2) {
					final int nl = name.length() / 2;
					name0 = name.substring(0, nl);
					name1 = name.substring(nl);
				} else {
					name0 = name;
					name1 = ""; //$NON-NLS-1$
				}
				final String domain = matcher.group(2);
				final String tld = matcher.group(3);
				final String rid = Strings.isNullOrEmpty(id) ? "" : ("_" + id); //$NON-NLS-1$ //$NON-NLS-2$
				values.put("obfuscatedEmailName0" + rid, name0); //$NON-NLS-1$
				values.put("obfuscatedEmailName1" + rid, name1); //$NON-NLS-1$
				values.put("obfuscatedEmailDomain" + rid, domain); //$NON-NLS-1$
				values.put("obfuscatedEmailTld" + rid, tld); //$NON-NLS-1$
			}
		}
	}

	/** Fill the attributes that describe the components of an obfuscated phone number.
	 *
	 * @param values the attribute values to fill up.
	 * @param phone the phone to obfuscate.
	 * @param id the identifier to add to the attribute names for making them unique.
	 */
	@SuppressWarnings("static-method")
	protected void addObfuscatedPhoneFields(Map<String, Object> values, String phone, String id) {
		if (!Strings.isNullOrEmpty(phone)) {
			final String phone0;
			final String phone1;
			final String phone2;
			final String phone3;
			if (phone.length() >= 4) {
				final int nl = phone.length() / 4;
				int n = 0;
				phone0 = phone.substring(0, nl);
				n += nl;
				phone1 = phone.substring(n, n + nl);
				n += nl;
				phone2 = phone.substring(n, n + nl);
				n += nl;
				phone3 = phone.substring(n);
			} else if (phone.length() == 3) {
				phone0 = ""; //$NON-NLS-1$
				phone1 = phone.substring(0, 2);
				phone2 = ""; //$NON-NLS-1$
				phone3 = phone.substring(2, 3);
			} else {
				phone0 = ""; //$NON-NLS-1$
				phone1 = ""; //$NON-NLS-1$
				phone2 = phone;
				phone3 = ""; //$NON-NLS-1$
			}
			final String rid = Strings.isNullOrEmpty(id) ? "" : ("_" + id); //$NON-NLS-1$ //$NON-NLS-2$
			values.put("obfuscatedPhone0" + rid, phone0); //$NON-NLS-1$
			values.put("obfuscatedPhone1" + rid, phone1); //$NON-NLS-1$
			values.put("obfuscatedPhone2" + rid, phone2); //$NON-NLS-1$
			values.put("obfuscatedPhone3" + rid, phone3); //$NON-NLS-1$
		}
	}

	/** Fill the attributes that describe the components of an obfuscated email.
	 *
	 * @param modelAndView the model-view object to fill up.
	 * @param values the attribute values to fill up.
	 */
	@SuppressWarnings("static-method")
	protected void addObfuscatedValues(ModelAndView modelAndView, Map<String, Object> values) {
		modelAndView.addObject("obfuscatedValues", values); //$NON-NLS-1$
	}

}
