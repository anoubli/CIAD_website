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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.indicator.GlobalIndicators;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.indicators.Indicator;
import fr.ciadlab.labmanager.repository.indicator.GlobalIndicatorsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link GlobalIndicatorsService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class GlobalIndicatorServiceTest {

	private MessageSourceAccessor messages;

	private GlobalIndicatorsRepository indicatorRepository;

	private List<Indicator> allIndicators;

	private Indicator ind0;

	private Indicator ind1;

	private GlobalIndicatorsService test;

	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.indicatorRepository = mock(GlobalIndicatorsRepository.class);
		this.ind0 = mock(Indicator.class);
		lenient().when(this.ind0.getKey()).thenReturn("ind0");
		lenient().when(this.ind0.getNumericValue(any(ResearchOrganization.class))).thenReturn(123);
		this.ind1 = mock(Indicator.class);
		lenient().when(this.ind1.getKey()).thenReturn("ind1");
		lenient().when(this.ind1.getNumericValue(any(ResearchOrganization.class))).thenReturn(456);
		this.allIndicators = Arrays.asList(this.ind0, this.ind1);
		this.test = new GlobalIndicatorsService(this.messages, new Constants(), this.indicatorRepository, this.allIndicators);
	}

	@Test
	public void getAllIndicatorValues_withoutGlobalInDb() {
		ResearchOrganization org = mock(ResearchOrganization.class);
		when(this.indicatorRepository.findAll()).thenReturn(Collections.emptyList());

		Map<String, Number> cache = this.test.getAllIndicatorValues(org);
		assertNotNull(cache);
		assertEquals(2, cache.size());
		assertEquals(123, cache.get("ind0"));
		assertEquals(456, cache.get("ind1"));

		ArgumentCaptor<GlobalIndicators> arg0 = ArgumentCaptor.forClass(GlobalIndicators.class);
		verify(this.indicatorRepository).save(arg0.capture());
		assertNotNull(arg0.getValue());

		GlobalIndicators ind = arg0.getValue();
		assertEquals("{\"ind0\":123,\"ind1\":456}", ind.getValues());
		assertSame(cache, ind.getIndicators());
		assertNotNull(ind.getLastUpdate());
	}

	@Test
	public void getAllIndicatorValues_withGlobalInDb() {
		ResearchOrganization org = mock(ResearchOrganization.class);
		GlobalIndicators gi = mock(GlobalIndicators.class);
		Map<String, Number> expectedCache = mock(Map.class);
		when(gi.getIndicators()).thenReturn(expectedCache);
		when(this.indicatorRepository.findAll()).thenReturn(Arrays.asList(gi));

		Map<String, Number> cache = this.test.getAllIndicatorValues(org);
		assertSame(expectedCache, cache);

		verify(gi).setValues(same(org), same(this.allIndicators));

		verify(this.indicatorRepository).save(same(gi));
	}

}
