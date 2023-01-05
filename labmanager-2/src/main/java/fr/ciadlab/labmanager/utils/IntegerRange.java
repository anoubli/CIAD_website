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

package fr.ciadlab.labmanager.utils;

import java.io.Serializable;

/** Range of integer values.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public class IntegerRange implements Serializable {

	private static final long serialVersionUID = 5475666225792640252L;

	private final String separator;

	private final Integer min;

	private final Integer max;

	/** Constructor.
	 *
	 * @param separator the seperator for rendering the range.
	 * @param values values to put in the range.
	 */
	public IntegerRange(String separator, Iterable<Integer> values) {
		this.separator = separator;
		Integer mn = null;
		Integer mx = null;
		for (final Integer value : values) {
			if (value != null) {
				if (mn == null || value.intValue() < mn.intValue()) {
					mn = value;
				}
				if (mx == null || value.intValue() > mx.intValue()) {
					mx = value;
				}
			}
		}
		this.min = mn;
		this.max = mx;
	}

	/** Constructor.
	 *
	 * @param separator the seperator for rendering the range.
	 * @param values values to put in the range.
	 */
	public IntegerRange(String separator, int... values) {
		this.separator = separator;
		Integer mn = null;
		Integer mx = null;
		for (final int value : values) {
			if (mn == null || value < mn.intValue()) {
				mn = Integer.valueOf(value);
			}
			if (mx == null || value > mx.intValue()) {
				mx = Integer.valueOf(value);
			}
		}
		this.min = mn;
		this.max = mx;
	}

	/** Constructor.
	 *
	 * @param values values to put in the range.
	 */
	public IntegerRange(Iterable<Integer> values) {
		this("-", values); //$NON-NLS-1$
	}

	/** Constructor.
	 *
	 * @param values values to put in the range.
	 */
	public IntegerRange(int... values) {
		this("-", values); //$NON-NLS-1$
	}

	/** Replies the range start (min value).
	 *
	 * @return the min value or {@code null} if unknown.
	 */
	public Integer getMin() {
		return this.min;
	}

	/** Replies the range end (max value).
	 *
	 * @return the max value or {@code null} if unknown.
	 */
	public Integer getMax() {
		return this.max;
	}

	@Override
	public String toString() {
		final Integer mn = getMin();
		final Integer mx = getMax();
		if (mn == null) {
			if (mx == null) {
				return ""; //$NON-NLS-1$
			}
			return mx.toString();
		}
		if (mx == null || mn.intValue() == mx.intValue()) {
			return mn.toString();
		}
		final StringBuilder b = new StringBuilder();
		b.append(mn.toString());
		b.append(this.separator);
		b.append(mx.toString());
		return b.toString();
	}

}
