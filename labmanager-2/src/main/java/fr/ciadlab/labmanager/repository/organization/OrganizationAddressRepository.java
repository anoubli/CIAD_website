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

package fr.ciadlab.labmanager.repository.organization;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA Repository for the organization addresses.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see OrganizationAddress
 */
public interface OrganizationAddressRepository extends JpaRepository<OrganizationAddress, Integer> {

	/** Find an address from its symbolic name.
	 *
	 * @param name the symbolic name to search for.
	 * @return the result of the search.
	 */
	Optional<OrganizationAddress> findDistinctByName(String name);

	
	/** Replies the organization addresses with the given identifiers.
	 *
	 * @param addresses the identifiers to match.
	 * @return the list of addresses with the given identifiers.
	 */
	Set<OrganizationAddress> findAllByIdIn(List<Integer> addresses);

}
