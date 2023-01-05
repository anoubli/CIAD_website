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

package fr.ciadlab.labmanager.service.supervision;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.supervision.Supervision;
import fr.ciadlab.labmanager.entities.supervision.Supervisor;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.supervision.SupervisionRepository;
import fr.ciadlab.labmanager.repository.supervision.SupervisorRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the person supervisions.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Service
public class SupervisionService extends AbstractService {

	private SupervisionRepository supervisionRepository;

	private SupervisorRepository supervisorRepository;

	private MembershipRepository membershipRepository;

	private PersonService personService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param supervisionRepository the repository for person supervisions.
	 * @param supervisorRepository the repository for person supervisors.
	 * @param membershipRepository the repository for accessing the organization memberships.
	 * @param personService the service for accessing the persons.
	 */
	public SupervisionService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired SupervisionRepository supervisionRepository,
			@Autowired SupervisorRepository supervisorRepository,
			@Autowired MembershipRepository membershipRepository,
			@Autowired PersonService personService) {
		super(messages, constants);
		this.supervisionRepository = supervisionRepository;
		this.supervisorRepository = supervisorRepository;
		this.membershipRepository = membershipRepository;
		this.personService = personService;
	}

	/** Replies all the supervisions associated to the person with the given identifier, when he/she is one of the supervisors.
	 *
	 * @param supervisorId the identifier of the supervisor.
	 * @return the list of the supervisions for the supervised person.
	 */
	public List<Supervision> getSupervisionsForSupervisor(int supervisorId) {
		return this.supervisionRepository.findAllDisctinctBySupervisorsSupervisorId(Integer.valueOf(supervisorId));
	}

	/** Replies all the supervisions associated to the person with the given identifier, when he/she is the supervised person.
	 *
	 * @param supervisedPersonId the identifier of the supervised person.
	 * @return the list of the supervisions for the supervised person.
	 */
	public List<Supervision> getSupervisionsForSupervisedPerson(int supervisedPersonId) {
		return this.supervisionRepository.findAllBySupervisedPersonPersonId(Integer.valueOf(supervisedPersonId));
	}

	/** Create a supervision of a person.
	 *
	 * @param membership the identifier of the membership that describes the supervised person.
	 * @param supervisors the list of the supervisors. Each supervisor description is a map with the following keys:<ul>
	 *     <li>{@code supervisorId} the identifier of the supervisor.</li>
	 *     <li>{@code supervisorType} the type of the supervisor.</li>
	 *     <li>{@code supervisorPercent} the percentage of implication of the supervisor.</li>
	 *     </ul>
	 * @param abandonment indicates if the works were abandoned by the supervised person.
	 * @param title the title of the works done by the supervised person.
	 * @param fundingScheme the name of the scheme that is used for funding the supervised person.
	 * @param fundingDetails some explanation and details about the funding scheme.
	 * @param defenseDate the date of the defense.
	 * @param positionAfterSupervision an description of the becoming of the supervised person.
	 * @param numberOfAteRPositions the number of ATER positions that were given to the supervised person.
	 * @param jointPosition indicates if the position of the supervised person is in the context of a joint agreement between institutions (co-tutelle, etc.).
	 * @param entrepreneur indicates if the supervised person has also a position of entrepreneur in parallel. 
	 */
	public void addSupervision(int membership, List<Map<String, String>> supervisors, boolean abandonment,
			String title, FundingScheme fundingScheme, String fundingDetails, LocalDate defenseDate,
			String positionAfterSupervision, int numberOfAteRPositions, boolean jointPosition, boolean entrepreneur) {
		final Supervision supervision = new Supervision();
		updateSupervisionWithoutSavingAndSupervisors(supervision, membership, abandonment, title, fundingScheme,
				fundingDetails, defenseDate, positionAfterSupervision, numberOfAteRPositions, jointPosition, entrepreneur);
		this.supervisionRepository.save(supervision);
		supervision.setSupervisors(makeSupervisorList(supervisors));
		this.supervisionRepository.save(supervision);
	}

	/** Create a supervision of a person.
	 *
	 * @param supervision the identifier of the supervision to be updated.
	 * @param membership the identifier of the membership that describes the supervised person.
	 * @param supervisors the list of the supervisors. Each supervisor description is a map with the following keys:<ul>
	 *     <li>{@code supervisorId} the identifier of the supervisor.</li>
	 *     <li>{@code supervisorType} the type of the supervisor.</li>
	 *     <li>{@code supervisorPercent} the percentage of implication of the supervisor.</li>
	 *     </ul>
	 * @param abandonment indicates if the works were abandoned by the supervised person.
	 * @param title the title of the works done by the supervised person.
	 * @param fundingScheme the name of the scheme that is used for funding the supervised person.
	 * @param fundingDetails some explanation and details about the funding scheme.
	 * @param defenseDate the date of the defense.
	 * @param positionAfterSupervision an description of the becoming of the supervised person.
	 * @param numberOfAteRPositions the number of ATER positions that were given to the supervised person.
	 * @param jointPosition indicates if the position of the supervised person is in the context of a joint agreement between institutions (co-tutelle, etc.).
	 * @param entrepreneur indicates if the supervised person has also a position of entrepreneur in parallel. 
	 */
	public void updateSupervision(int supervision, int membership, List<Map<String, String>> supervisors, boolean abandonment,
			String title, FundingScheme fundingScheme, String fundingDetails, LocalDate defenseDate,
			String positionAfterSupervision, int numberOfAteRPositions, boolean jointPosition, boolean entrepreneur) {
		final Optional<Supervision> supervisionOpt = this.supervisionRepository.findById(Integer.valueOf(supervision));
		if (supervisionOpt.isPresent()) {
			final Supervision supervisionObj = supervisionOpt.get();
			updateSupervisionWithoutSavingAndSupervisors(supervisionObj, membership, abandonment, title, fundingScheme,
					fundingDetails, defenseDate, positionAfterSupervision, numberOfAteRPositions, jointPosition, entrepreneur);
			this.supervisionRepository.save(supervisionObj);
			supervisionObj.setSupervisors(makeSupervisorList(supervisors));
			this.supervisionRepository.save(supervisionObj);
		} else {
			throw new IllegalArgumentException("Supervision not found with id: " + supervision); //$NON-NLS-1$
		}
	}

	private void updateSupervisionWithoutSavingAndSupervisors(Supervision supervision, int membership, boolean abandonment,
			String title, FundingScheme fundingScheme, String fundingDetails, LocalDate defenseDate,
			String positionAfterSupervision, int numberOfAteRPositions, boolean jointPosition, boolean entrepreneur) {
		assert supervision != null;
		final Optional<Membership> membershipObj = this.membershipRepository.findById(Integer.valueOf(membership));
		if (membershipObj.isEmpty()) {
			throw new IllegalArgumentException("Membership cannot be found with with: " + membership); //$NON-NLS-1$
		}
		supervision.setSupervisedPerson(membershipObj.get());
		supervision.setTitle(title);
		supervision.setFunding(fundingScheme);
		supervision.setFundingDetails(fundingDetails);
		supervision.setAbandonment(abandonment);
		supervision.setDefenseDate(defenseDate);
		supervision.setPositionAfterSupervision(positionAfterSupervision);
		supervision.setNumberOfAterPositions(numberOfAteRPositions);
		supervision.setJointPosition(jointPosition);
		supervision.setEntrepreneur(entrepreneur);
	}

	private List<Supervisor> makeSupervisorList(List<Map<String, String>> supervisors) {
		final List<Supervisor> supervisorList = new ArrayList<>();
		for (final Map<String, String> supervisorDesc : supervisors) {
			final Supervisor sup = new Supervisor();
			int id;
			try {
				id = Integer.parseInt(supervisorDesc.get("supervisorId")); //$NON-NLS-1$
			} catch (Throwable ex) {
				id = 0;
			}
			if (id == 0) {
				throw new IllegalArgumentException("Person not found with id: " + supervisorDesc.get("supervisorId")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			final Person person = this.personService.getPersonById(id);
			if (person == null) {
				throw new IllegalArgumentException("Person not found with id: " + supervisorDesc.get("supervisorId")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			sup.setSupervisor(person);
			try {
				sup.setType(supervisorDesc.get("supervisorType")); //$NON-NLS-1$
			} catch (Throwable ex) {
				throw new IllegalArgumentException("Invalid type of supervisor: " + supervisorDesc.get("supervisorType")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			int percent;
			try {
				percent = Integer.parseInt(supervisorDesc.get("percent")); //$NON-NLS-1$
			} catch (Throwable ex) {
				percent = 0;
			}
			if (percent < 0 || percent > 100) {
				throw new IllegalArgumentException("Invalid percentage: " + supervisorDesc.get("percent")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			sup.setPercentage(percent);
			supervisorList.add(sup);
		}
		if (supervisorList.isEmpty()) {
			throw new IllegalArgumentException("Missing supervisor"); //$NON-NLS-1$
		}
		return supervisorList;
	}

	/** Remove the supervsion that has the given identifier.
	 *
	 * @param identifier the identifier of the supervision.
	 */
	@Transactional
	public void removeSupervision(int identifier) {
		final Integer mid = Integer.valueOf(identifier);
		final Optional<Supervision> optSupervision = this.supervisionRepository.findById(mid);
		if (optSupervision.isEmpty()) {
			throw new IllegalStateException("Supervision not found with id: " + identifier); //$NON-NLS-1$
		}
		this.supervisionRepository.deleteById(mid);
	}

	/** Save the given supervision into the database.
	 *
	 * @param supervision the supervision to save.
	 */
	public void save(Supervision supervision) {
		this.supervisionRepository.save(supervision);
	}

	/** Save the given supervisor into the database.
	 *
	 * @param supervisor the supervisor to save.
	 */
	public void save(Supervisor supervisor) {
		this.supervisorRepository.save(supervisor);
	}

}
