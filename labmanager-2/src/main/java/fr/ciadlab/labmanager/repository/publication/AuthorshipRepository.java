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

import java.util.List;
import java.util.Optional;

import fr.ciadlab.labmanager.entities.publication.Authorship;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for the authorship relations.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface AuthorshipRepository extends JpaRepository<Authorship, Integer> {

	/** Find an authorship that corresponds tothe given person identifier and publication identifier.
	 *
	 * @param personId the identifier of the person.
	 * @param publicationId the publication of the publication.
	 * @return the authorship.
	 */
	Optional<Authorship> findByPersonIdAndPublicationId(int personId, int publicationId);

	/** Find the authorships that corresponds are linked to the publication with the given identifier.
	 *
	 * @param publicationId the publication of the publication.
	 * @return the authorships linked to the publication with the given id.
	 */
	List<Authorship> findByPublicationId(int publicationId);

	/** Count the number of authorships for the person with the given id.
	 *
	 * @param id the identifier of the person.
	 * @return the count of authorships.
	 */
	int countDistinctByPersonId(int id);

}
