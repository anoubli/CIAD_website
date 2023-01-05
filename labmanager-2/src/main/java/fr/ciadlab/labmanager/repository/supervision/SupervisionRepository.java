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

package fr.ciadlab.labmanager.repository.supervision;

import java.util.List;

import fr.ciadlab.labmanager.entities.supervision.Supervision;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for surpervision declaration.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */
public interface SupervisionRepository extends JpaRepository<Supervision, Integer> {

	/** Replies all the supervisions associated to the person with the given identifier, when he/she is the supervised person.
	 *
	 * @param supervisedPersonId the identifier of the supervised person.
	 * @return the list of the supervisions for the supervised person.
	 */
	List<Supervision> findAllBySupervisedPersonPersonId(Integer supervisedPersonId);

	/** Replies all the supervisions associated to the person with the given identifier, when he/she is one of the supervisors.
	 *
	 * @param supervisorId the identifier of the supervisor.
	 * @return the list of the supervisions for the supervisor.
	 */
	List<Supervision> findAllDisctinctBySupervisorsSupervisorId(Integer supervisorId);

	/** Count the number of supervisions for the supervisor with the given id.
	 *
	 * @param id the identifier of the supervisor.
	 * @return the count of supervisions.
	 */
	int countDistinctBySupervisedPersonPersonId(int id);

}
