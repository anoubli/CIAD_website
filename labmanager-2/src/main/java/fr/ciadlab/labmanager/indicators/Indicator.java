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

package fr.ciadlab.labmanager.indicators;

import java.time.LocalDate;

import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.utils.Unit;

/** A computed value that indicates a key element for an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public interface Indicator {
	
	/** Clear any buffered value.
	 */
	void clear();

	/** Replies the key that identify the the indicator.
	 *
	 * @return the name.
	 */
	String getKey();

	/** Replies the name of the indicator.
	 *
	 * @return the name.
	 */
	String getName();

	/** Replies the details of the computation of the indicator.
	 * The details provides information about how the computation was done for the indicator.
	 *
	 * @return the details.
	 * @since 2.4
	 */
	String getComputationDetails();

	/** Replies the label associated to the indicator.
	 * The label describes the meaning of the indicator.
	 * This label may be adapted to the associated value that is
	 * provided as argument.
	 *
	 * @param unit the unit to be used for displaying the value.
	 * @return the label.
	 */
	String getLabel(Unit unit);

	/** Replies the start of the reference period for this indicator.
	 *
	 * @return the start date.
	 */
	LocalDate getReferencePeriodStart();

	/** Replies the end of the reference period for this indicator.
	 *
	 * @return the end date.
	 */
	LocalDate getReferencePeriodEnd();

	/** Replies the value of the indicator in the form of a number.
	 *
	 * @param organization the organization for which the indicator should be computed.
	 * @return the number value, or {@code null} if the indicator does not compute a numeric value.
	 */
	Number getNumericValue(ResearchOrganization organization);

}
