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

package fr.ciadlab.labmanager.service.indicator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.indicator.GlobalIndicators;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.indicators.Indicator;
import fr.ciadlab.labmanager.repository.indicator.GlobalIndicatorsRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service related to the global indicators.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@Service
public class GlobalIndicatorsService extends AbstractService {

	private static final int CACHE_AGE = 30;

	private final GlobalIndicatorsRepository indicatorRepository;

	private final List<? extends Indicator> allIndicators;

	private final Map<String, Indicator> allIndicatorsPerKey;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param indicatorRepository the global indicator repository.
	 * @param allIndicators the list of all the indicators that were install in the app.
	 */
	public GlobalIndicatorsService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired GlobalIndicatorsRepository indicatorRepository,
			@Autowired List<? extends Indicator> allIndicators) {
		super(messages, constants);
		this.indicatorRepository = indicatorRepository;
		this.allIndicators = allIndicators;
		this.allIndicatorsPerKey = this.allIndicators.stream().collect(
				Collectors.toMap(Indicator::getKey, Function.identity()));
	}

	/** Ensure that the global indicators' object is created.
	 * Refresh the values if they are too old.
	 *
	 * @param updateCache indicates if the cache must be loaded.
	 * @return the global indicators' object.
	 */
	public GlobalIndicators getGlobalIndicatorsNeverNull(boolean updateCache) {
		final Optional<GlobalIndicators> opt = this.indicatorRepository.findAll().stream().findFirst();
		GlobalIndicators gi;
		if (opt.isEmpty()) {
			gi = createGlobalIndicators();
		} else {
			gi = opt.get();
		}
		if (updateCache) {
			gi.updateCache();
		}
		return gi;
	}

	/** Ensure that the global indicators' object is created.
	 * Refresh the values if they are too old.
	 *
	 * @param organization the organization that must be used for updated the values.
	 * @param indicators the indicators to consider.
	 * @return the global indicators' object.
	 */
	protected GlobalIndicators ensureGlobalIndicators(ResearchOrganization organization, List<? extends Indicator> indicators) {
		final GlobalIndicators gi = getGlobalIndicatorsNeverNull(false);
		if (isOldGlobalIndicatorCache(gi)) {
			updateGlobalIndicatorValues(gi, organization, indicators);
		}
		return gi;
	}

	/** Refresh the values of the global indicators.
	 *
	 * @param globalIndicators the object for global indicators to be refreshed.
	 * @param organization the organization that must be used for updated the values.
	 * @param indicators the indicators to consider.
	 */
	protected void updateGlobalIndicatorValues(GlobalIndicators globalIndicators, ResearchOrganization organization,
			List<? extends Indicator> indicators) {
		globalIndicators.setValues(organization, indicators);
		this.indicatorRepository.save(globalIndicators);
	}

	/** Create the global indicators' object.
	 *
	 * @return a new global indicators' object.
	 */
	@SuppressWarnings("static-method")
	protected GlobalIndicators createGlobalIndicators() {
		return new GlobalIndicators();
	}

	/** Replies the map of all the indicator values.
	 *
	 * @param organization the organization that must be used for updated the values.
	 * @return the map.
	 */
	public Map<String, Number> getAllIndicatorValues(ResearchOrganization organization) {
		return getIndicatorValues(organization, this.allIndicators);
	}

	/** Replies the map of the indicator values for the given indicators.
	 *
	 * @param organization the organization that must be used for updated the values.
	 * @param indicators the list of indicators to consider.
	 * @return the map.
	 */
	public Map<String, Number> getIndicatorValues(ResearchOrganization organization, List<? extends Indicator> indicators) {
		final GlobalIndicators gi = ensureGlobalIndicators(organization, indicators);
		Map<String, Number> cache = gi.getIndicators();
		if (cache == null) {
			return Collections.emptyMap();
		}
		return cache;
	}

	/** Replies if the values of the global indicators are too old.
	 * 
	 * @parma globalIndicators the object to check.
	 * @return {@code true} if they are too old.
	 */
	protected static boolean isOldGlobalIndicatorCache(GlobalIndicators globalIndicators) {
		return globalIndicators != null
				&& (globalIndicators.getLastUpdate() == null
					|| globalIndicators.getIndicators() == null
					|| globalIndicators.getIndicators().isEmpty()
					|| LocalDateTime.now().isAfter(globalIndicators.getLastUpdate().plusDays(CACHE_AGE)));
	}

	/** Replies all the visible global indicators in the order that they should be displayed.
	 *
	 * @return the visible indicators.
	 */
	public List<? extends Indicator> getVisibleIndicators() {
		final List<String> indicatorKeys = getGlobalIndicatorsNeverNull(true).getVisibleIndicators();		
		return indicatorKeys.stream().map(it -> this.allIndicatorsPerKey.get(it)).filter(it -> it != null).collect(Collectors.toList());
	}

	/** Replies all the invisible global indicators.
	 *
	 * @return the invisible indicators.
	 */
	public List<? extends Indicator> getInvisibleIndicators() {
		final Set<String> visibles = new TreeSet<>(getGlobalIndicatorsNeverNull(true).getVisibleIndicators());
		if (visibles.isEmpty()) {
			return this.allIndicators;
		}
		return this.allIndicators.stream().filter(it -> {
			return !visibles.contains(it.getKey());
		}).collect(Collectors.toList());
	}

	/** Replies the global indicators.
	 *
	 * @return the indicators or {@code null} if there is no global indicators yet.
	 */
	public GlobalIndicators getGlobalIndicatorsOrNull() {
		final Optional<GlobalIndicators> opt = this.indicatorRepository.findAll().stream().findFirst();
		if (opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	/** Save or create the global indicators.
	 *
	 * @param visibleIndicators the list of th keys of the visible indicators.
	 */
	public void updateVisibleIndicators(List<String> visibleIndicators) {
		final GlobalIndicators gi = getGlobalIndicatorsNeverNull(false);
		gi.setVisibleIndicators(visibleIndicators);
		this.indicatorRepository.save(gi);
	}

	/** Reset the values of the global indicators to force there computation.
	 *
	 * @param username the name of the logged-in user.
	 */
	public void resetGlobalIndicatorValues() {
		final Optional<GlobalIndicators> opt = this.indicatorRepository.findAll().stream().findFirst();
		if (opt.isPresent()) {
			final GlobalIndicators gi = opt.get();
			gi.setValues(null);
			this.indicatorRepository.save(gi);
		}
	}

}
