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
import java.util.Map;

import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;

/** A computed value that indicates a key element per year for an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.3
 */
public interface AnnualIndicator extends Indicator {

	/** Replies the number of years to be supported by default.
	 * 
	 * @return the number of years to support.
	 */
	int getYearCount();
	
	/** Change the number of years to be supported by default.
	 * 
	 * @param years the number of years to support.
	 */
	void setYearCount(int years);

	/** Replies the start year of the reference period for this indicator.
	 *
	 * @return the start year.
	 */
	int getReferenceStartYear();

	/** Change the start year of the reference period for this indicator.
	 *
	 * @param year the start year.
	 */
	void setReferenceStartYear(int year);

	/** Replies the end year of the reference period for this indicator.
	 *
	 * @return the end year.
	 */
	default int getReferenceEndYear() {
		return getReferenceStartYear() + getYearCount() - 1;
	}

	@Override
	default LocalDate getReferencePeriodStart() {
		return LocalDate.of(getReferenceStartYear(), 1, 1);
	}

	@Override
	default LocalDate getReferencePeriodEnd() {
		return LocalDate.of(getReferenceEndYear(), 12, 31);
	}

	/** Replies the values for the given time window.
	 *
	 * @param organization the organization for which indicators should be computed.
	 * @param startYear the first year to consider.
	 * @param endYear the last year to consider.
	 * @return the values per year.
	 */
	Map<Integer, Number> getValuesPerYear(ResearchOrganization organization, int startYear, int endYear);

	/** Replies the values for the given time window.
	 *
	 * @param organization the organization for which indicators should be computed.
	 * @param startYear the first year to consider.
	 * @param endYear the last year to consider.
	 * @return the values per year.
	 */
	default Map<Integer, Number> getValuesPerYear(ResearchOrganization organization) {
		return getValuesPerYear(organization, getReferenceStartYear(), getReferenceEndYear());
	}

}
