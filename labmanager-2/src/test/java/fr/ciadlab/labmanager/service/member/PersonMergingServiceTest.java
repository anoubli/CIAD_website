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

package fr.ciadlab.labmanager.service.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.jury.JuryMembership;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.service.invitation.PersonInvitationService;
import fr.ciadlab.labmanager.service.jury.JuryMembershipService;
import fr.ciadlab.labmanager.service.supervision.SupervisionService;
import fr.ciadlab.labmanager.utils.names.DefaultPersonNameParser;
import fr.ciadlab.labmanager.utils.names.PersonNameComparator;
import fr.ciadlab.labmanager.utils.names.SorensenDicePersonNameComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link PersonMergingService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class PersonMergingServiceTest {

	private MessageSourceAccessor messages;

	private PersonRepository personRepository;

	private PersonService personService;

	private MembershipService organizationMembershipService;

	private MembershipRepository organizationMembershipRepository;

	private JuryMembershipService juryMembershipService;

	private SupervisionService supervisionService;

	private PersonInvitationService invitationService;

	private PersonNameComparator nameComparator;
	
	private PersonMergingService test;
	
	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.personRepository = mock(PersonRepository.class);
		this.personService = mock(PersonService.class);
		this.organizationMembershipService = mock(MembershipService.class);
		this.organizationMembershipRepository = mock(MembershipRepository.class);
		this.juryMembershipService = mock(JuryMembershipService.class);
		this.supervisionService = mock(SupervisionService.class);
		this.invitationService = mock(PersonInvitationService.class);
		this.nameComparator = new SorensenDicePersonNameComparator(new DefaultPersonNameParser());

		this.test = new PersonMergingService(this.messages, new Constants(), this.personRepository, this.personService,
				this.organizationMembershipService, this.organizationMembershipRepository,
				this.juryMembershipService, this.supervisionService, this.invitationService, this.nameComparator);
	}

	@Test
	public void mergePersonsById_nullArguments() throws Exception {
		assertThrows(AssertionError.class, () -> {
			this.test.mergePersonsById(null, null);
		});
	}

	@Test
	public void mergePersonsById() throws Exception {
		Person pers0 = mock(Person.class, "pers0");
		when(pers0.getId()).thenReturn(12345);

		Set<Authorship> existingAuthorships0 = new HashSet<>();
		when(pers0.getAuthorships()).thenReturn(existingAuthorships0);
		
		Publication pub0 = mock(Publication.class);
		when(pub0.getAuthors()).thenReturn(Collections.singletonList(pers0));
		
		Authorship existingAuthorship0 = mock(Authorship.class);
		when(existingAuthorship0.getPublication()).thenReturn(pub0);
		existingAuthorships0.add(existingAuthorship0);

		Person pers3 = mock(Person.class, "pers3");
		when(pers3.getId()).thenReturn(34567548);

		Set<Authorship> existingAuthorships3 = new HashSet<>();
		when(pers3.getAuthorships()).thenReturn(existingAuthorships3);

		when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 12345:
				return Optional.of(pers0);
			case 34567548:
				return Optional.of(pers3);
			}
			return Optional.empty();
		});

		when(this.personRepository.findAllById(any(List.class))).thenAnswer(it -> {
			final List<Person> list = new ArrayList<>();
			for (final Integer id : (List<Integer>) it.getArgument(0)) {
				switch (id) {
				case 12345:
					list.add(pers0);
					break;
				}
			}
			return list;
		});
		
		//
		this.test.mergePersonsById(Arrays.asList(12345), 34567548);
		//

		verify(this.personService).removePerson(eq(12345));

		verify(this.personRepository).save(same(pers3));

		verify(existingAuthorship0).setPerson(same(pers3));
		
		assertTrue(existingAuthorships0.isEmpty());
		
		assertEquals(1, existingAuthorships3.size());
		assertSame(existingAuthorship0, existingAuthorships3.iterator().next());
	}

	@Test
	public void mergePersons_nullArguments() throws Exception {
		assertThrows(AssertionError.class, () -> {
			this.test.mergePersons(null, null);
		});
	}

	@Test
	public void mergePersons_nothingInSource() throws Exception {
		Person pers0 = mock(Person.class, "pers0");
		when(pers0.getId()).thenReturn(12345);

		Set<Authorship> existingAuthorships0 = new HashSet<>();
		when(pers0.getAuthorships()).thenReturn(existingAuthorships0);

		Person pers3 = mock(Person.class, "pers3");
		when(pers3.getId()).thenReturn(34567548);

		//
		this.test.mergePersons(Arrays.asList(pers0), pers3);
		//

		verify(this.personService).removePerson(eq(12345));

		verify(this.personRepository, never()).save(any());
	}

	@Test
	public void mergePersons_publications() throws Exception {
		Person pers0 = mock(Person.class, "pers0");
		when(pers0.getId()).thenReturn(12345);

		Set<Authorship> existingAuthorships0 = new HashSet<>();
		when(pers0.getAuthorships()).thenReturn(existingAuthorships0);
		
		Publication pub0 = mock(Publication.class);
		when(pub0.getAuthors()).thenReturn(Collections.singletonList(pers0));
		
		Authorship existingAuthorship0 = mock(Authorship.class);
		when(existingAuthorship0.getPublication()).thenReturn(pub0);
		existingAuthorships0.add(existingAuthorship0);
		
		Person pers3 = mock(Person.class, "pers3");
		when(pers3.getId()).thenReturn(34567548);

		Set<Authorship> existingAuthorships3 = new HashSet<>();
		when(pers3.getAuthorships()).thenReturn(existingAuthorships3);

		//
		this.test.mergePersons(Arrays.asList(pers0), pers3);
		//

		verify(this.personService).removePerson(eq(12345));

		verify(this.personRepository).save(same(pers3));

		verify(existingAuthorship0).setPerson(same(pers3));
		
		assertTrue(existingAuthorships0.isEmpty());
		
		assertEquals(1, existingAuthorships3.size());
		assertSame(existingAuthorship0, existingAuthorships3.iterator().next());
	}

	@Test
	public void mergePersons_organizationMemberships() throws Exception {
		Person pers0 = mock(Person.class, "pers0");
		when(pers0.getId()).thenReturn(12345);

		Membership mbr0 = mock(Membership.class);
		
		List<Membership> memberships = new ArrayList<>();
		memberships.add(mbr0);
		
		when(this.organizationMembershipService.getMembershipsForPerson(eq(12345))).thenReturn(memberships);
		
		Person pers3 = mock(Person.class, "pers3");
		when(pers3.getId()).thenReturn(34567548);

		//
		this.test.mergePersons(Arrays.asList(pers0), pers3);
		//

		verify(this.personService).removePerson(eq(12345));

		verify(this.personRepository).save(same(pers3));

		verify(mbr0).setPerson(same(pers3));
		verify(this.organizationMembershipRepository).save(same(mbr0));
	}

	@Test
	public void mergePersons_juryMemberships() throws Exception {
		Person pers0 = mock(Person.class, "pers0");
		when(pers0.getId()).thenReturn(12345);

		JuryMembership mbr0 = mock(JuryMembership.class);
		
		List<JuryMembership> memberships = new ArrayList<>();
		memberships.add(mbr0);
		
		when(this.juryMembershipService.getMembershipsForPerson(eq(12345))).thenReturn(memberships);
		
		Person pers3 = mock(Person.class, "pers3");
		when(pers3.getId()).thenReturn(34567548);

		//
		this.test.mergePersons(Arrays.asList(pers0), pers3);
		//

		verify(this.personService).removePerson(eq(12345));

		verify(this.personRepository).save(same(pers3));

		verify(mbr0).setPerson(same(pers3));
		verify(this.juryMembershipService).save(same(mbr0));
	}


	@Test
	public void getPersonDuplicate_noDuplicate() throws Exception {
		List<Set<Person>> duplicate = this.test.getPersonDuplicates(null, null);
		assertTrue(duplicate.isEmpty());
	}

	@Test
	public void getPersonDuplicates_oneDuplicate() throws Exception {
		Person pers0 = mock(Person.class, "pers0");
		lenient().when(pers0.getId()).thenReturn(123);
		lenient().when(pers0.getFirstName()).thenReturn("F1");
		lenient().when(pers0.getLastName()).thenReturn("L1");

		Person pers1 = mock(Person.class, "pers1");
		lenient().when(pers1.getId()).thenReturn(234);
		lenient().when(pers1.getFirstName()).thenReturn("F2");
		lenient().when(pers1.getLastName()).thenReturn("L2");

		Person pers0b = mock(Person.class, "pers0b");
		lenient().when(pers0b.getId()).thenReturn(456852);
		lenient().when(pers0b.getFirstName()).thenReturn("F1");
		lenient().when(pers0b.getLastName()).thenReturn("L1");

		Person pers2 = mock(Person.class, "pers2");
		lenient().when(pers2.getId()).thenReturn(345);
		lenient().when(pers2.getFirstName()).thenReturn("F3");
		lenient().when(pers2.getLastName()).thenReturn("L3");

		Person pers2b = mock(Person.class, "pers2b");
		lenient().when(pers2b.getId()).thenReturn(456853);
		lenient().when(pers2b.getFirstName()).thenReturn("F3");
		lenient().when(pers2b.getLastName()).thenReturn("L3");

		Person pers2c = mock(Person.class, "pers2c");
		lenient().when(pers2c.getId()).thenReturn(456854);
		lenient().when(pers2c.getFirstName()).thenReturn("F3");
		lenient().when(pers2c.getLastName()).thenReturn("L3");

		Person pers3 = mock(Person.class, "pers3");
		lenient().when(pers3.getId()).thenReturn(456);
		lenient().when(pers3.getFirstName()).thenReturn("F4");
		lenient().when(pers3.getLastName()).thenReturn("L4");

		when(this.personRepository.findAll()).thenReturn(
				Arrays.asList(pers0, pers2c, pers1, pers0b, pers2, pers3, pers2b));
		
		List<Set<Person>> allDuplicates = this.test.getPersonDuplicates(null, null);

		assertEquals(2, allDuplicates.size());

		Set<Person> set1 = allDuplicates.get(0);
		assertSet(set1, pers0, pers0b);

		Set<Person> set2 = allDuplicates.get(1);
		assertSet(set2, pers2, pers2b, pers2c);
	}

	private void assertSet(Set<Person> actual, Person... expected) {
		final List<Person> exp = new ArrayList<>(Arrays.asList(expected));
		for (final Person p : actual) {
			assertTrue(exp.removeIf(it -> it == p), "Unexpected element: " + p);
		}
		assertTrue(exp.isEmpty(), "Missed elements: " + exp);
	}

}
