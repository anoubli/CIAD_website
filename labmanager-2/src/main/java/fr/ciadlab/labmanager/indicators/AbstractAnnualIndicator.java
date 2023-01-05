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

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import com.google.common.util.concurrent.AtomicDouble;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import org.joda.time.LocalDate;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation of a computed value that indicates a key element for an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.3
 */
public abstract class AbstractAnnualIndicator extends AbstractIndicator implements AnnualIndicator {

	/** Default number of years for the year window.
	 */
	public static final int DEFAULT_YEAR_WINDOW = 5;

	private int yearCount = DEFAULT_YEAR_WINDOW;
	
	private int startYear;

	private final Function<Map<Integer, Number>, Number> mergingFunction;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param mergingFunction the function that should be used for merging the annual values.
	 *      If it is {@code null}, the {@link #sum(Map)} is used.
	 */
	public AbstractAnnualIndicator(MessageSourceAccessor messages, Constants constants,
			Function<Map<Integer, Number>, Number> mergingFunction) {
		super(messages, constants);
		this.mergingFunction = mergingFunction;
		this.startYear = LocalDate.now().getYear() - getYearCount();
	}

	@Override
	public int getYearCount() {
		return this.yearCount;
	}

	@Override
	public void setYearCount(int year) {
		this.yearCount = year;
	}

	@Override
	public void setReferenceStartYear(int year) {
		this.startYear = year;
	}

	@Override
	public int getReferenceStartYear() {
		return this.startYear;
	}

	@Override
	protected Number computeValue(ResearchOrganization organization) {
		final Map<Integer, Number> values = getValuesPerYear(organization, getReferenceStartYear(), getReferenceEndYear());
		return mergeValues(values);
	}

	/** Sum the values for building a single global value.
	 *
	 * @param values the values per year.
	 * @return the sum value.
	 */
	protected static Number sum(Map<Integer, Number> values) {
		final AtomicDouble sum = new AtomicDouble();
		values.values().parallelStream().forEach(it -> {
			sum.addAndGet(it.doubleValue());
		});
		return Double.valueOf(sum.doubleValue());
	}

	/** Average the values for building a single global value.
	 *
	 * @param values the values per year.
	 * @return the average value.
	 */
	protected static Number average(Map<Integer, Number> values) {
		final AtomicDouble sum = new AtomicDouble();
		final AtomicInteger count = new AtomicInteger();
		values.values().parallelStream().forEach(it -> {
			sum.addAndGet(it.doubleValue());
			count.incrementAndGet();
		});
		final int n = count.get();
		final double s;
		if (n > 0) {
			s = sum.doubleValue() / n;
		} else {
			s = 0.;
		}
		return Double.valueOf(s);
	}

	/** Merge the values for building a single global value.
	 *
	 * @param values the values per year.
	 * @return the merged value.
	 */
	protected Number mergeValues(Map<Integer, Number> values) {
		if (this.mergingFunction == null) {
			return sum(values);
		}
		return this.mergingFunction.apply(values);
	}

	/** Change the details of the computation.
	 *
	 * @param collection the elements that are inside the explanation.
	 * @since 2.4
	 */
	protected <T> void setComputationDetails(Map<Integer, Number> collection) {
		final StringBuilder bb = new StringBuilder();
		for (final Entry<Integer, Number> entry : collection.entrySet()) {
			if (bb.length() > 0) {
				bb.append("\n"); //$NON-NLS-1$
			}
			bb.append(entry.getKey());
			bb.append(") "); //$NON-NLS-1$
			bb.append(entry.getValue());
		}
		setComputationDetails(bb.toString());
	}

}
