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

package fr.ciadlab.labmanager.repository.journal;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.ciadlab.labmanager.entities.journal.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for journals.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface JournalRepository extends JpaRepository<Journal, Integer> {

	/** Find the journals by their name.
	 *
	 * @param name the name of the journal to search for.
	 * @return the journals with the given name.
	 */
	Set<Journal> findDistinctByJournalName(String name);

	/** Find a journal by their name.
	 *
	 * @param name the name of the journal to search for.
	 * @return the journal with the given name.
	 */
	Optional<Journal> findByJournalName(String name);
	
}
