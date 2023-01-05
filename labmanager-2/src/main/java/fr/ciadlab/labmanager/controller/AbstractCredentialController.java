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

package fr.ciadlab.labmanager.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.utils.HttpRequestUtils;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation of a JEE Controller that provides support for logged users.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCredentialController extends AbstractComponent {

	/** Current username provided by {@link #ensureLoggedUser(HttpServletRequest)}.
	 *
	 * @see #ensureLoggedUser(HttpServletRequest)
	 */
	private String username;

	/** Hash for encoding the username.
	 */
	private final byte[] usernameKey;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public AbstractCredentialController(MessageSourceAccessor messages, Constants constants, String usernameKey) {
		super(messages, constants);
		final String key = Strings.isNullOrEmpty(usernameKey) ? "the-key" : usernameKey; //$NON-NLS-1$
		this.usernameKey = key.getBytes(StandardCharsets.US_ASCII);
	}

	/** Replies the name of the logged-in user.
	 *
	 * @return the name of {@code null}.
	 */
	protected String getCurrentUsername() {
		return this.username;
	}

	/** Replies if a user is logged-in.
	 * <p>This function always validates if the backend is in debug mode.
	 *
	 * @return {@code true} if a user is logged in.
	 */
	protected boolean isLoggedIn() {
		return !Strings.isNullOrEmpty(this.username);
	}

	private void failOnInvalidClient(String serviceName) {
		getLogger().error("Refused connection to the service " + serviceName + " for user with id " + getCurrentUsername()); //$NON-NLS-1$ //$NON-NLS-2$
		getLogger().error("Agressor client IP: " + HttpRequestUtils.getClientIpAddressIfServletRequestExist()); //$NON-NLS-1$
		throw new IllegalAccessError(getMessage("all.notLogged")); //$NON-NLS-1$
	}

	/** Validate the credentials for the given username.
	 * <p>This function always validates if the backend is in debug mode.
	 *
	 * @param encryptedUsername the encrypted username of the logged-in user.
	 * @param serviceName the name of the service that is accessed.
	 * @param serviceParams the significant parameters that are passed to the service. They are used for building the log messages.
	 * @return the uncrypted username.
	 * @throws IllegalAccessError if there is no logged user.
	 */
	protected String ensureCredentials(byte[] encryptedUsername, String serviceName, Object... serviceParams) {
		final String uname = readCredentials(encryptedUsername, serviceName, serviceParams);
		if (!isLoggedIn()) {
			failOnInvalidClient(serviceName);
		}
		return uname;
	}

	/** Validate the credentials for the given username.
	 * <p>This function always validates if the backend is in debug mode.
	 *
	 * @param encryptedUsername the encrypted username of the logged-in user.
	 * @param serviceName the name of the service that is accessed.
	 * @param serviceParams the significant parameters that are passed to the service. They are used for building the log messages.
	 * @return the uncrypted username.
	 * @throws IllegalAccessError if there is no logged user.
	 */
	protected String ensureCredentials(String encryptedUsername, String serviceName, Object... serviceParams) {
		final String uname = readCredentials(encryptedUsername, serviceName, serviceParams);
		if (!isLoggedIn()) {
			failOnInvalidClient(serviceName);
		}
		return uname;
	}

	/** Read the credentials for the given username without validating them.
	 * <p>This function always validates if the backend is in debug mode.
	 *
	 * @param encryptedUsername the encrypted username of the logged-in user.
	 * @param serviceName the name of the service that is accessed.
	 * @param serviceParams the significant parameters that are passed to the service. They are used for building the log messages.
	 * @return the uncrypted username.
	 * @throws IllegalAccessError if there is no logged user.
	 */
	protected String readCredentials(byte[] encryptedUsername, String serviceName, Object... serviceParams) {
		getLogger().debug("Receiving credential: " + Arrays.toString(encryptedUsername)); //$NON-NLS-1$
		final String str = new String(encryptedUsername, StandardCharsets.US_ASCII);
		return readCredentials(str, serviceName, serviceParams);
	}

	/** Read the credentials for the given username without validating them.
	 * <p>This function always validates if the backend is in debug mode.
	 *
	 * @param encryptedUsername the encrypted username of the logged-in user.
	 * @param serviceName the name of the service that is accessed.
	 * @param serviceParams the significant parameters that are passed to the service. They are used for building the log messages.
	 * @return the uncrypted username.
	 * @throws IllegalAccessError if there is no logged user.
	 */
	protected String readCredentials(String encryptedUsername, String serviceName, Object... serviceParams) {
		checkMaintenance();
		if (this.debugVersion) {
			this.username = Constants.DEVELOPER;
		} else {
			String decodedUsername = null;
			if (!Strings.isNullOrEmpty(encryptedUsername)) {
				try {
					decodedUsername = decode(encryptedUsername);
					getLogger().debug("Decoding " + encryptedUsername + " to " + decodedUsername); //$NON-NLS-1$ //$NON-NLS-2$
				} catch (Throwable ex) {
					decodedUsername = null;
				}
			}
			if (!Strings.isNullOrEmpty(decodedUsername) && !Constants.ANONYMOUS.equalsIgnoreCase(decodedUsername)) {
				this.username = decodedUsername;
			} else {
				this.username = null;
			}
		}
		if (!Strings.isNullOrEmpty(this.username)) {
			getLogger().info("Opening /" + serviceName + " by " + this.username + " for parameters: " + Arrays.toString(serviceParams)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			getLogger().info("Opening /" + serviceName + " without login with parameters: " + Arrays.toString(serviceParams)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return this.username;
	}

	private String decode(String username) throws Exception {
		try {
			if (!Strings.isNullOrEmpty(username)) {
				final String[] parts = username.split(":"); //$NON-NLS-1$
				if (parts.length == 2) {
					final String data64 = parts[0];
					final String iv64 = parts[1];
					final IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(iv64));
					getLogger().debug("Using key: " + Arrays.toString(this.usernameKey)); //$NON-NLS-1$
					final SecretKeySpec skeySpec = new SecretKeySpec(this.usernameKey, "AES"); //$NON-NLS-1$
					final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); //$NON-NLS-1$
					cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
					final byte[] decodedEncryptedData = Base64.getDecoder().decode(data64);
					final byte[] original = cipher.doFinal(decodedEncryptedData);
					return new String(original);
				}
			}
			getLogger().error("Unable to decode: " + username); //$NON-NLS-1$
		} catch (Throwable ex) {
			getLogger().error("Unable to decode: " + username, ex); //$NON-NLS-1$
		}
		return null;
	}

}
