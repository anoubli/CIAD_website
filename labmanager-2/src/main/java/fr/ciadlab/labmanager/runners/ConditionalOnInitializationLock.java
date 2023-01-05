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

import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/** Condition for endpoint activitation based on the exisiting of the initialization lock file
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2.0
 * @see JsonDatabaseInitializer
 */
public class ConditionalOnInitializationLock implements Condition {

	private static final String LOCK_FILENAME = "data.lock"; //$NON-NLS-1$

	/** Replies the name of the endponit lock file.
	 * 
	 * @param dataSourceFolder the name of the folder that should contain the data source.
	 * @return the name of the lock file.
	 */
	public static File getLockFilename(String dataSourceFolder) {
		if (!Strings.isNullOrEmpty(dataSourceFolder)) {
			try {
				final File root = FileSystem.convertStringToFile(dataSourceFolder);
				return FileSystem.join(root, LOCK_FILENAME);
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		final String value = context.getEnvironment().getProperty("labmanager.init.data-source"); //$NON-NLS-1$
		if (!Strings.isNullOrEmpty(value)) {
			try {
				final File file = getLockFilename(value);
				if (file != null) {
					return file.exists();
				}
			} catch (Throwable ex) {
				//
			}
		}
		return false;
	}

}
