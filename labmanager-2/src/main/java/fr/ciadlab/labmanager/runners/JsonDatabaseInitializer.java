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
import fr.ciadlab.labmanager.io.json.ZipToDatabaseImporter;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.apache.jena.ext.com.google.common.io.Files;
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

	private static final String ARCHIVE_FILENAME = "data.zip"; //$NON-NLS-1$

	/** Logger of the service. It is lazy loaded.
	 */
	private Logger logger;

	private final JsonToDatabaseImporter jsonImporter;

	private final ZipToDatabaseImporter zipImporter;

	private final String dataSourceFolder;

	private final boolean enabled;

	/** Constructor.
	 * 
	 * @param jsonImporter the importer of JSON.
	 * @param zipImporter the importer of ZIP.
	 * @param dataSourceFolder the folder in the local file system in which the data source file could be located.
	 * @param enabled from configuration file, indicates if the data import is enabled or not.
	 */
	public JsonDatabaseInitializer(
			@Autowired JsonToDatabaseImporter jsonImporter,
			@Autowired ZipToDatabaseImporter zipImporter,
			@Value("${labmanager.init.data-source}") String dataSourceFolder,
			@Value("${labmanager.init.enable}") boolean enabled) {
		this.jsonImporter = jsonImporter;
		this.zipImporter = zipImporter;
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
	protected static URL getDataURLInWar() {
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
	protected URL getDataURLInLocalFileSystem() {
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
	protected URL getJsonDataURL() {
		final URL url = getDataURLInWar();
		if (url != null) {
			return url;
		}
		return getDataURLInLocalFileSystem();
	}

	/** Replies the URL to the data archive to use.
	 *
	 * @return the URL or {@code null} if none.
	 */
	protected URL getZipDataURL() {
		if (!Strings.isNullOrEmpty(this.dataSourceFolder)) {
			try {
				final File root = FileSystem.convertStringToFile(this.dataSourceFolder);
				final File file = FileSystem.join(root, ARCHIVE_FILENAME);
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
	
	/** Replies the URL of the data source (Zip or Json).
	 *
	 * @return the URL of the data source or {@code null} if none.
	 */
	protected URL detectDataUrl() {
		final URL zipUrl = getZipDataURL();
		if (zipUrl != null) {
			return zipUrl;
		}
		final URL jsonUrl = getJsonDataURL();
		if (jsonUrl != null) {
			return jsonUrl;
		}
		return null;
	}

	/** Replies the importer code to be used for the given URL of the data source.
	 *
	 * @return the URL of the data source or {@code null} if none.
	 */
	protected Importer detectImporter(URL url) {
		assert url != null;
		if (FileSystem.hasExtension(url, ".zip")) { //$NON-NLS-1$
			return it -> this.zipImporter.importArchiveFileToDatabase(it);
		}
		if (FileSystem.hasExtension(url, ".json")) { //$NON-NLS-1$
			return it -> this.jsonImporter.importDataFileToDatabase(it);
		}
		return null;
	}

	/** Import the given archive file into the database.
	 *
	 * @param url the URL of data to be imported.
	 * @param importer the callback function for doing the importation concretely.
	 * @throws Exception if import process cannot be done.
	 */
	protected void doImport(URL url, Importer importer) throws Exception {
		assert url != null;
		assert importer != null;
		getLogger().info("Database initialization with: " + url.toExternalForm()); //$NON-NLS-1$
		createLockFile();
		try {
			importer.importFrom(url);
		} finally {
			deleteLockFile();
		}
	}

	/** Replies the name of the endponit lock file.
	 * 
	 * @return the name of the lock file.
	 */
	protected File getLockFilename() {
		return ConditionalOnInitializationLock.getLockFilename(this.dataSourceFolder);
	}

	/** Create the lock file for the endpoints.
	 */
	protected void createLockFile() {
		try {
			final File lockFile = getLockFilename();
			if (lockFile != null) {
				Files.touch(lockFile);
			}
		} catch (Throwable ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
		}
	}

	/** Delete the lock file for the endpoints.
	 */
	protected void deleteLockFile() {
		try {
			final File lockFile = getLockFilename();
			if (lockFile != null) {
				lockFile.delete();
			}
		} catch (Throwable ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (this.enabled) {
			URL dataUrl = detectDataUrl();
			if (dataUrl != null) {
				final Importer importer = detectImporter(dataUrl);
				if (importer != null) {
					doImport(dataUrl, importer);
				} else {
					getLogger().warn("Database initialization is skipped because of lake of importer for " + dataUrl); //$NON-NLS-1$
				}
			} else {
				getLogger().info("Database initialization is skipped because of lake of data source"); //$NON-NLS-1$
			}
		} else {
			getLogger().info("Database initialization is disabled"); //$NON-NLS-1$
		}
	}

	/** Importer of data into the database.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.2
	 */
	@FunctionalInterface
	protected interface Importer {

		/** Do the import from the data source with the given URL.
		 *
		 * @param url the URL of the data source.
		 * @throws Exception any error during import process.
		 */
		void importFrom(URL url) throws Exception;

	}

}
