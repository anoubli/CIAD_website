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

package fr.ciadlab.labmanager.repository.publication;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import fr.ciadlab.labmanager.entities.publication.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for a publication.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PublicationRepository extends JpaRepository<Publication, Integer> {

	/** Replies the list of publications for the person with the given identifier.
	 *
	 * @param personId the identifier of the person.
	 * @return the list of publications.
	 */
	List<Publication> findAllByAuthorshipsPersonId(int personId);

	/** Replies the list of publications for the person with the given webpage identifier.
	 *
	 * @param webpageId the identifier of the webpage of the person.
	 * @return the list of publications.
	 */
	List<Publication> findAllByAuthorshipsPersonWebPageId(String webpageId);

	/** Replies the list of publications for the persons with the given identifiers.
	 *
	 * @param personIds the list of identifiers of the authors.
	 * @return the list of publications.
	 */
	Set<Publication> findAllByAuthorshipsPersonIdIn(Set<Integer> personIds);
	
	/** Replies the list of publictions for the given identifiers.
	 *
	 * @param identifiers the identifiers to search for.
	 * @return the publications.
	 * @since 2.5
	 */
	Set<Publication> findAllByIdIn(Collection<Integer> identifiers);

	/** Replies the list of publications with the given title.
	 *
	 * @param title the title to search for, with case insensitive test.
	 * @return the set of publications with the given title.
	 */
	List<Publication> findAllByTitleIgnoreCase(String title);

}
