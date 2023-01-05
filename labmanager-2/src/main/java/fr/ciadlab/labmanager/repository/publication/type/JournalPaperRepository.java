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

package fr.ciadlab.labmanager.repository.publication.type;

import java.util.List;
import java.util.Set;

import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA Repository for journal papers.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface JournalPaperRepository extends JpaRepository<JournalPaper, Integer> {

	/** Replies the journal papers that are associated to the journal with the given identifier.
	 *
	 * @param journalId the identifier of the journal.
	 * @return the journal papers.
	 */
	List<JournalPaper> findAllByJournalId(int journalId);

	/** Replies the list of journal papers for the persons with the given identifiers.
	 *
	 * @param personIds the list of identifiers of the authors.
	 * @return the list of publications.
	 */
	Set<JournalPaper> findAllByAuthorshipsPersonIdIn(Set<Integer> personIds);

}

