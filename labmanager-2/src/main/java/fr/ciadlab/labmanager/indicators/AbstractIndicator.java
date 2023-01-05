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
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation of a computed value that indicates a key element for an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public abstract class AbstractIndicator extends AbstractComponent implements Indicator {

	private String key;
	
	private final Map<Integer, Number> values = new TreeMap<>();

	private String details;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public AbstractIndicator(MessageSourceAccessor messages, Constants constants) {
		super(messages, constants);
	}

	@Override
	public void clear() {
		this.values.clear();
	}

	@Override
	public String getKey() {
		if (this.key == null) {
			this.key = StringUtils.uncapitalize(getClass().getSimpleName().replace("Indicator$", "")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return this.key;
	}

	/** Replies the label with the year at the end.
	 *
	 * @param key the key for the message.
	 * @param arguments the arguments to put in the label.
	 * @return the label with the year.
	 */
	protected final String getLabelWithYears(String key, Object... arguments) {
		final StringBuilder text = new StringBuilder(getMessage(key, arguments));
		final LocalDate start = getReferencePeriodStart();
		final LocalDate end = getReferencePeriodEnd();
		if (start != null && end != null) {
			final int syear = start.getYear();
			final int eyear = end.getYear();
			if (syear != eyear) {
				text.append(" (").append(syear).append("-").append(eyear).append(")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				text.append(" (").append(syear).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else if (start != null) {
			final int year = start.getYear();
			text.append(" (").append(year).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (end != null) {
			final int year = end.getYear();
			text.append(" (").append(year).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return text.toString();
	}

	/** Replies the label with the year at the end.
	 *
	 * @param key the key for the message.
	 * @param arguments the arguments to put in the label.
	 * @return the label without the year.
	 */
	protected final String getLabelWithoutYears(String key, Object... arguments) {
		return getMessage(key, arguments);
	}

	@Override
	public String getComputationDetails() {
		return this.details;
	}

	/** Change the details of the computation.
	 *
	 * @param details the details of the computation.
	 * @since 2.4
	 */
	protected void setComputationDetails(String details) {
		this.details = Strings.emptyToNull(details);
	}

	/** Change the details of the computation.
	 *
	 * @param collection the elements that are inside the explanation.
	 * @param name the function that provides the name of each element
	 * @since 2.4
	 */
	protected <T> void setComputationDetails(Collection<T> collection, Function<T, String> name) {
		final StringBuffer bb = new StringBuffer();
		final AtomicInteger index = new AtomicInteger(0);
		collection.stream().map(it -> name.apply(it)).sorted().forEach(it -> {
			if (bb.length() > 0) {
				bb.append("\n"); //$NON-NLS-1$
			}
			if (!Strings.isNullOrEmpty(it)) {
				bb.append(index.incrementAndGet()).append(") ").append(it); //$NON-NLS-1$
			}
		});
		setComputationDetails(bb.toString());
	}

	@Override
	public Number getNumericValue(ResearchOrganization organization) {
		final Number value = this.values.computeIfAbsent(Integer.valueOf(organization.getId()), it -> {
			getLogger().info("Computing indicator value for " + getKey()); //$NON-NLS-1$
			final Number v = computeValue(organization);
			getLogger().info(getKey() + " = " + v); //$NON-NLS-1$
			return v;
		});
		return value;
	}

	/** Compute the numeric value of the indicator.
	 * 
	 * @param organization the organization for which the indicator should be computed.
	 * @return the numeric value.
	 */
	protected abstract Number computeValue(ResearchOrganization organization);

	/** Replies the start date of the reference period if the duration of this period corresponds to the argument.
	 * This function provides the January 1 of the X years before today.
	 *
	 * @param years number of years for the reference period.
	 * @return the start date.
	 */
	protected static LocalDate computeStartDate(int years) {
		final int ref = LocalDate.now().getYear() - 1;
		return LocalDate.of(ref - years + 1, 1, 1);
	}

	/** Replies the end date of the reference period if the duration of this period corresponds to the argument.
	 * This function provides the December 31 of the previous year than today.
	 *
	 * @param years number of years for the reference period.
	 * @return the end date.
	 */
	protected static LocalDate computeEndDate(int years) {
		final int ref = LocalDate.now().getYear() - 1;
		return LocalDate.of(ref - years, 12, 31);
	}

	/** Filter the given collection by years.
	 *
	 * @param <T> the type of elements in the collection.
	 * @param parallel indicates if the stream is parallel or not.
	 * @param collection the collection to filter.
	 * @param yearExtractor the extractor of year from the elements.
	 * @return the filtered stream.
	 */
	protected <T> Stream<T> filterByYearWindow(boolean parallel, Collection<T> collection, Function<T, Integer> yearExtractor) {
		final int start = getReferencePeriodStart().getYear();
		final int end = getReferencePeriodEnd().getYear();
		Stream<T> stream;
		if (parallel) {
			stream = collection.parallelStream();
		} else {
			stream = collection.stream();
		}
		return stream.filter(it -> {
			final Integer year = yearExtractor.apply(it);
			return year != null && year.intValue() >= start && year.intValue() <= end;
		});
	}

}
