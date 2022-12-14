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

package fr.ciadlab.labmanager.runners;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import fr.ciadlab.labmanager.io.json.JsonToDatabaseImporter;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/** This component fill up the database with JSON format that is independent of the database engine.
 * It is searching for a file with the name {@code data-<platform>.json} at the
 * root folder of the class-path.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
public class JsonDatabaseInitializer implements ApplicationRunner {

	private static final String DATA_FILENAME = "data.json"; //$NON-NLS-1$

	/** Logger of the service. It is lazy loaded.
	 */
	private Logger logger;

	private final JsonToDatabaseImporter importer;

	private final String dataSourceFolder;

	private final boolean enabled;

	/** Constructor.
	 * 
	 * @param importer the importer of JSON.
	 * @param dataSourceFolder the folder in the local file system in which the data source file could be located.
	 * @param enabled from configuration file, indicates if the data import is enabled or not.
	 */
	public JsonDatabaseInitializer(
			@Autowired JsonToDatabaseImporter importer,
			@Value("${labmanager.init.data-source}") String dataSourceFolder,
			@Value("${labmanager.init.enable}") boolean enabled) {
		this.importer = importer;
		this.dataSourceFolder = dataSourceFolder;
		this.enabled = enabled;
	}

	/** Replies the logger of this service.
	 *
	 * @return the logger.
	 */
	public Logger getLogger() {
		if (this.logger == null) {
			this.logger = createLogger();
		}
		return this.logger;
	}

	/** Change the logger of this service.
	 *
	 * @param logger the logger.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/** Factory method for creating the service logger.
	 *
	 * @return the logger.
	 */
	protected Logger createLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	/** Replies the URL to the data script to use in the current WAR.
	 *
	 * @return the URL or {@code null} if none.
	 */
	protected static URL getDataScriptURLInWar() {
		try {
			final URL url = Resources.getResource("/" + DATA_FILENAME); //$NON-NLS-1$
			if (url != null) {
				try {
					@SuppressWarnings("resource")
					final InputStream is = url.openStream();
					if (is != null) {
						is.close();
					}
					return url;
				} catch (Throwable ex) {
					//
				}
			}
		} catch (Throwable ex) {
			//
		}
		return null;
	}

	/** Replies the URL to the data script to use in the local file system.
	 *
	 * @return the URL or {@code null} if none.
	 */
	protected URL getDataScriptURLInLocalFileSystem() {
		if (!Strings.isNullOrEmpty(this.dataSourceFolder)) {
			try {
				final File root = FileSystem.convertStringToFile(this.dataSourceFolder);
				final File file = FileSystem.join(root, DATA_FILENAME);
				if (file != null && file.exists() && file.canRead()) {
					try {
						return FileSystem.convertFileToURL(file);
					} catch (Throwable ex) {
						//
					}
				}
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Replies the URL to the data script to use.
	 *
	 * @return the URL or {@code null} if none.
	 */
	protected URL getDataScriptURL() {
		final URL url = getDataScriptURLInWar();
		if (url != null) {
			return url;
		}
		return getDataScriptURLInLocalFileSystem();
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (this.enabled) {
			final URL url = getDataScriptURL();
			if (url != null) {
				getLogger().info("Database initialization with: " + url.toExternalForm()); //$NON-NLS-1$
				this.importer.importToDatabase(url);
			} else {
				getLogger().info("Database initialization is skipped because of lake of data source"); //$NON-NLS-1$
			}
		} else {
			getLogger().info("Database initialization is disabled"); //$NON-NLS-1$
		}
	}

}
