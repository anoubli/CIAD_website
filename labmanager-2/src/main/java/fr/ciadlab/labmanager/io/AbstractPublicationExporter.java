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

package fr.ciadlab.labmanager.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Consumer;

import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationCategory;
import org.arakhne.afc.util.ListUtil;

/** Provides tools for exporting publications.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public abstract class AbstractPublicationExporter {

	/** Replies the comparator of publications that is used for sorting the publications.
	 *
	 * @return the comparator of publications.
	 */
	@SuppressWarnings("static-method")
	public Comparator<? super Publication> getPublicationComparator() {
		return EntityUtils.getPreferredPublicationComparatorInLists();
	}

	/** Replies the preferred label for the given publication category.
	 *
	 * @param category the category.
	 * @return the label.
	 */
	@SuppressWarnings("static-method")
	protected String getCategoryLabel(PublicationCategory category) {
		if (category == null) {
			return ""; //$NON-NLS-1$
		}
		return category.getLabel() + " (" + category.name() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Replies the preferred label for the given publication year.
	 *
	 * @param year the publication year.
	 * @return the label.
	 */
	@SuppressWarnings("static-method")
	protected String getYearLabel(Integer year) {
		if (year == null) {
			return ""; //$NON-NLS-1$
		}
		return year.toString();
	}

	/** Generic implementation of the exporting logic depending on the grouping configuration.
	 *
	 * @param publications the list of publications.
	 * @param configurator the exporter configuration.
	 * @param section the lambda for generating a section. 
	 * @param subsection the lambda for generating a subsection. 
	 * @param list the lambda for generating a list of publications. 
	 * @throws Exception if the export cannot be done.
	 */
	protected void exportPublicationsWithGroupingCriteria(Iterable<? extends Publication> publications, ExporterConfigurator configurator,
			Consumer<String> section,
			Consumer<String> subsection,
			Consumer<List<Publication>> list) throws Exception {
		if (configurator.isGroupedByCategory() || configurator.isGroupedByYear()) {
			if (configurator.isGroupedByCategory() && configurator.isGroupedByYear()) {
				final Map<PublicationCategory, Map<Integer, List<Publication>>> groupedPublications = groupByCategoryAndYear(publications);
				for (final Entry<PublicationCategory, Map<Integer, List<Publication>>> entry0 : groupedPublications.entrySet()) {
					section.accept(getCategoryLabel(entry0.getKey()));
					for (final Entry<Integer, List<Publication>> entry1 : entry0.getValue().entrySet()) {
						subsection.accept(getYearLabel(entry1.getKey()));
						list.accept(entry1.getValue());
					}
				}
			} else if (configurator.isGroupedByCategory()) {
				final Map<PublicationCategory, List<Publication>> groupedPublications = groupByCategory(publications);
				for (final Entry<PublicationCategory, List<Publication>> entry : groupedPublications.entrySet()) {
					section.accept(getCategoryLabel(entry.getKey()));
					list.accept(entry.getValue());
				}
			} else {
				final Map<Integer, List<Publication>> groupedPublications = groupByYear(publications);
				for (final Entry<Integer, List<Publication>> entry : groupedPublications.entrySet()) {
					section.accept(getYearLabel(entry.getKey()));
					list.accept(entry.getValue());
				}
			}
		} else {
			final List<Publication> ungroupedPublications = new ArrayList<>();
			final Comparator<? super Publication> sorter = getPublicationComparator();
			for (final Publication publication : publications) {
				ListUtil.add(ungroupedPublications, sorter, publication, true, false);
			}
			list.accept(ungroupedPublications);
		}
	}

	/** Group the publications per category and year.
	 *
	 * @param html the receiver of the HTML code.
	 * @param publications the publications to export.
	 * @param configurator the exporter configurator.
	 */
	protected Map<PublicationCategory, Map<Integer, List<Publication>>> groupByCategoryAndYear(Iterable<? extends Publication> publications) {
		final Map<PublicationCategory, Map<Integer, List<Publication>>> content = new TreeMap<>();
		final Comparator<? super Publication> sorter = getPublicationComparator();
		for (final Publication publication : publications) {
			final PublicationCategory category = publication.getCategory();
			final Map<Integer, List<Publication>> years = content.computeIfAbsent(category, it -> new TreeMap<>(Collections.reverseOrder()));
			final Integer year = Integer.valueOf(publication.getPublicationYear());
			final List<Publication> list = years.computeIfAbsent(year, it -> new ArrayList<>());
			ListUtil.add(list, sorter, publication, true, false);
		}
		return content;
	}

	/** Group the publications per category only.
	 *
	 * @param html the receiver of the HTML code.
	 * @param publications the publications to export.
	 * @param configurator the exporter configurator.
	 */
	protected Map<PublicationCategory, List<Publication>> groupByCategory(Iterable<? extends Publication> publications) {
		final Map<PublicationCategory, List<Publication>> content = new TreeMap<>();
		final Comparator<? super Publication> sorter = getPublicationComparator();
		for (final Publication publication : publications) {
			final PublicationCategory category = publication.getCategory();
			final List<Publication> list = content.computeIfAbsent(category, it -> new ArrayList<>());
			ListUtil.add(list, sorter, publication, true, false);
		}
		return content;
	}

	/** Group the publications per year only.
	 *
	 * @param html the receiver of the HTML code.
	 * @param publications the publications to export.
	 * @param configurator the exporter configurator.
	 */
	protected Map<Integer, List<Publication>> groupByYear(Iterable<? extends Publication> publications) {
		final Map<Integer, List<Publication>> content = new TreeMap<>(Collections.reverseOrder());
		final Comparator<? super Publication> sorter = getPublicationComparator();
		for (final Publication publication : publications) {
			final Integer year = Integer.valueOf(publication.getPublicationYear());
			final List<Publication> list = content.computeIfAbsent(year, it -> new ArrayList<>());
			ListUtil.add(list, sorter, publication, true, false);
		}
		return content;
	}

}
