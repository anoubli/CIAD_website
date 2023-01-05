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

package fr.ciadlab.labmanager.entities;

/** Interface that represents an object that has an database identifier.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface IdentifiableEntity {

	/** Replies the database identifier for this entity.
	 *
	 * @return the identifier.
	 */
	int getId();

	/** Replies if the given entity is a fake entity or not.
	 * A fake entity is created for being provided by the JPA to the front-end.
	 * It is not supposed to be saved into the JPA database.
	 *
	 * @return {@code true} if the entity is a fake entity.
	 * @since 2.4
	 */
	default boolean isFakeEntity() {
		return false;
	}

}
