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

package fr.ciadlab.labmanager.service.organization;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
import fr.ciadlab.labmanager.repository.organization.OrganizationAddressRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import org.arakhne.afc.util.CountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for research organizations.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class ResearchOrganizationService extends AbstractService {

	private final OrganizationAddressRepository addressRepository;

	private final ResearchOrganizationRepository organizationRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param addressRepository the address repository.
	 * @param organizationRepository the organization repository.
	 */
	public ResearchOrganizationService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired OrganizationAddressRepository addressRepository,
			@Autowired ResearchOrganizationRepository organizationRepository) {
		super(messages, constants);
		this.addressRepository = addressRepository;
		this.organizationRepository = organizationRepository;
	}

	/** Replies all the research organizations.
	 *
	 * @return the research organizations.
	 */
	public List<ResearchOrganization> getAllResearchOrganizations() {
		return this.organizationRepository.findAll();
	}

	/** Replies the research organization with the given identifier.
	 *
	 * @param identifier the identifier to search for.
	 * @return the research organization.
	 */
	public Optional<ResearchOrganization> getResearchOrganizationById(int identifier) {
		return this.organizationRepository.findById(Integer.valueOf(identifier));
	}

	/** Replies the research organization with the given acronym.
	 *
	 * @param acronym the acronym to search for.
	 * @return the research organization.
	 */
	public Optional<ResearchOrganization> getResearchOrganizationByAcronym(String acronym) {
		return this.organizationRepository.findDistinctByAcronym(acronym);
	}

	/** Replies the research organization with the given name.
	 *
	 * @param name the name to search for.
	 * @return the research organization.
	 */
	public Optional<ResearchOrganization> getResearchOrganizationByName(String name) {
		return this.organizationRepository.findDistinctByName(name);
	}

	/** Replies the research organization with the given acronym or name.
	 *
	 * @param text the text to search for.
	 * @return the research organization.
	 */
	public Optional<ResearchOrganization> getResearchOrganizationByAcronymOrName(String text) {
		return this.organizationRepository.findDistinctByAcronymOrName(text, text);
	}

	/** Create a research organization.
	 *
	 * @param acronym the new acronym for the research organization.
	 * @param name the new name for the research organization.
	 * @param isMajor indicates if the organization is a major organization.
	 * @param rnsr the number of the organization in the RNSR.
	 * @param nationalIdentifier the number of the organization for the national minitry of research.
	 * @param description the new description for the research organization.
	 * @param type the type of the research organization.
	 * @param organizationURL the web-site URL of the research organization.
	 * @param country the country of the research organization.
	 * @param addresses the identifiers of the addresses of the organization.
	 * @param superOrganization the identifier of the super organization, or {@code null} or {@code 0} if none.
	 * @return the created organization in the database.
	 */
	public Optional<ResearchOrganization> createResearchOrganization(String acronym, String name,
			boolean isMajor,
			String rnsr, String nationalIdentifier, String description,
			ResearchOrganizationType type, String organizationURL, CountryCode country, List<Integer> addresses, Integer superOrganization) {
		final Optional<ResearchOrganization> sres;
		if (superOrganization != null && superOrganization.intValue() != 0) {
			sres = this.organizationRepository.findById(superOrganization);
			if (sres.isEmpty()) {
				throw new IllegalArgumentException("Research organization not found with id: " + superOrganization); //$NON-NLS-1$
			}
		} else {
			sres = Optional.empty();
		}
		//
		final Set<OrganizationAddress> adrs;
		if (addresses != null && !addresses.isEmpty()) {
			adrs = this.addressRepository.findAllByIdIn(addresses);
		} else {
			adrs = null;
		}
		//
		final ResearchOrganization res = new ResearchOrganization();
		res.setAcronym(Strings.emptyToNull(acronym));
		res.setName(Strings.emptyToNull(name));
		res.setMajorOrganization(isMajor);
		res.setRnsr(Strings.emptyToNull(rnsr));
		res.setNationalIdentifier(Strings.emptyToNull(nationalIdentifier));
		res.setDescription(Strings.emptyToNull(description));
		res.setType(type);
		res.setOrganizationURL(Strings.emptyToNull(organizationURL));
		res.setCountry(country);
		if (adrs != null && !adrs.isEmpty()) {
			res.setAddresses(adrs);
		}
		if (sres.isPresent()) {
			res.setSuperOrganization(sres.get());
		}
		this.organizationRepository.save(res);
		return Optional.of(res);
	}

	/** Remove a research organization from the database.
	 * An research organization cannot be removed if it contains a suborganization.
	 *
	 * @param identifier the identifier of the organization to remove.
	 * @throws AttachedSubOrganizationException when the organization contains suborganizations.
	 * @throws AttachedMemberException when the organization contains members.
	 */
	public void removeResearchOrganization(int identifier) throws AttachedSubOrganizationException, AttachedMemberException {
		final Integer id = Integer.valueOf(identifier);
		final Optional<ResearchOrganization> res = this.organizationRepository.findById(id);
		if (res.isPresent()) {
			final ResearchOrganization organization = res.get();
			if (!organization.getSubOrganizations().isEmpty()) {
				throw new AttachedSubOrganizationException();
			}
			if (!organization.getMemberships().isEmpty()) {
				throw new AttachedMemberException();
			}
			//
			this.organizationRepository.deleteById(id);
		}
	}

	/** Change the information associated to a research organization.
	 *
	 * @param identifier the identifier of the research organization to be updated.
	 * @param acronym the new acronym for the research organization.
	 * @param name the new name for the research organization.
	 * @param isMajor indicates if the organization is a major organization.
	 * @param rnsr the number of the organization in the RNSR.
	 * @param nationalIdentifier the identifier of the organization for the national ministry of research.
	 * @param description the new description for the research organization.
	 * @param type the type of the research organization.
	 * @param organizationURL the web-site URL of the research organization.
	 * @param country the country of the research organization.
	 * @param addresses the identifiers of the addresses of the organization.
	 * @param superOrganization the identifier of the super organization, or {@code null} or {@code 0} if none.
	 * @return the organization object that was updated.
	 */
	public Optional<ResearchOrganization> updateResearchOrganization(int identifier, String acronym, String name,
			boolean isMajor,
			String rnsr, String nationalIdentifier, String description,
			ResearchOrganizationType type, String organizationURL, CountryCode country, List<Integer> addresses, Integer superOrganization) {
		final Optional<ResearchOrganization> res = this.organizationRepository.findById(Integer.valueOf(identifier));
		if (res.isPresent()) {
			final Optional<ResearchOrganization> sres;
			if (superOrganization != null && superOrganization.intValue() != 0) {
				sres = this.organizationRepository.findById(superOrganization);
				if (sres.isEmpty()) {
					throw new IllegalArgumentException("Research organization not found with id: " + superOrganization); //$NON-NLS-1$
				}
			} else {
				sres = Optional.empty();
			}
			//
			final Set<OrganizationAddress> adrs;
			if (addresses != null && !addresses.isEmpty()) {
				adrs = this.addressRepository.findAllByIdIn(addresses);
			} else {
				adrs = null;
			}
			//
			final ResearchOrganization organization = res.get();
			if (!Strings.isNullOrEmpty(acronym)) {
				organization.setAcronym(acronym);
			}
			if (!Strings.isNullOrEmpty(name)) {
				organization.setName(name);
			}
			organization.setMajorOrganization(isMajor);
			organization.setRnsr(Strings.emptyToNull(rnsr));
			organization.setNationalIdentifier(Strings.emptyToNull(nationalIdentifier));
			organization.setDescription(Strings.emptyToNull(description));
			organization.setType(type);
			organization.setOrganizationURL(Strings.emptyToNull(organizationURL));
			organization.setCountry(country);
			organization.setAddresses(adrs);
			if (sres.isPresent()) {
				organization.setSuperOrganization(sres.get());
			}
			//
			this.organizationRepository.save(organization);
		}
		return res;
	}

	/** Link a suborganization to a super organization.
	 * 
	 * @param superIdentifier the identifier of the super organization.
	 * @param subIdentifier the identifier of the suborganization.
	 * @return {@code true} if the link is created; {@code false} if the link cannot be created.
	 */
	public boolean linkSubOrganization(int superIdentifier, int subIdentifier) {
		if (superIdentifier != subIdentifier) {
			final Optional<ResearchOrganization> superOrg = this.organizationRepository.findById(
					Integer.valueOf(superIdentifier));
			if (superOrg.isPresent()) {
				final Optional<ResearchOrganization> subOrg = this.organizationRepository.findById(
						Integer.valueOf(subIdentifier));
				if (subOrg.isPresent()) {
					final ResearchOrganization superOrganization = superOrg.get();
					final ResearchOrganization subOrganization = subOrg.get();
					if (superOrganization.getSubOrganizations().add(subOrganization)) {
						subOrganization.setSuperOrganization(superOrganization);
						this.organizationRepository.save(subOrganization);
						this.organizationRepository.save(superOrganization);
						return true;
					}
				}
			}
		}
		return false;
	}

	/** Unlink a suborganization from a super organization.
	 * 
	 * @param superIdentifier the identifier of the super organization.
	 * @param subIdentifier the identifier of the suborganization.
	 * @return {@code true} if the link is deleted; {@code false} if the link cannot be deleted.
	 */
	public boolean unlinkSubOrganization(int superIdentifier, int subIdentifier) {
		final Optional<ResearchOrganization> superOrg = this.organizationRepository.findById(
				Integer.valueOf(superIdentifier));
		if (superOrg.isPresent()) {
			final Optional<ResearchOrganization> subOrg = this.organizationRepository.findById(
					Integer.valueOf(subIdentifier));
			if (subOrg.isPresent()) {
				final ResearchOrganization superOrganization = superOrg.get();
				final ResearchOrganization subOrganization = subOrg.get();
				if (superOrganization.getSubOrganizations().remove(subOrganization)) {
					subOrganization.setSuperOrganization(null);
					this.organizationRepository.save(superOrganization);
					this.organizationRepository.save(subOrganization);
					return true;
				}
			}
		}
		return false;
	}

}
