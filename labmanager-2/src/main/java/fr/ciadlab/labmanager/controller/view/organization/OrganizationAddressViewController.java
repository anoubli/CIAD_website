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

package fr.ciadlab.labmanager.controller.view.organization;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.service.organization.OrganizationAddressService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for organization addresses' views.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ResearchOrganizationService
 * @since 2.0.0
 */
@RestController
@CrossOrigin
public class OrganizationAddressViewController extends AbstractViewController {

	private OrganizationAddressService addressService;

	private ResearchOrganizationService organizationService;

	private DownloadableFileManager fileManager;
	
	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the live constants.
	 * @param addressService the service for managing the addresses.
	 * @param organizationService the service related to the research organizations.
	 * @param fileManager the manager of local files.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public OrganizationAddressViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired OrganizationAddressService addressService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired DownloadableFileManager fileManager,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.addressService = addressService;
		this.organizationService = organizationService;
		this.fileManager = fileManager;
	}

	/** Replies the model-view component for listing the research organizations' addresses. It is the main endpoint for this controller.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/" + Constants.ORGANIZATION_ADDRESS_LIST_ENDPOINT)
	public ModelAndView showAddressList(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, Constants.ORGANIZATION_ADDRESS_LIST_ENDPOINT);
		final ModelAndView modelAndView = new ModelAndView(Constants.ORGANIZATION_ADDRESS_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		initAdminTableButtons(modelAndView, endpoint(Constants.ORGANIZATION_ADDRESS_EDITING_ENDPOINT, "address")); //$NON-NLS-1$
		modelAndView.addObject("addresses", this.addressService.getAllAddresses()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the editor for an organization address. This editor permits to create or to edit an address.
	 *
	 * @param address the identifier of the address to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of an address.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/" + Constants.ORGANIZATION_ADDRESS_EDITING_ENDPOINT)
	public ModelAndView showAddressEditor(
			@RequestParam(required = false) Integer address,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, Constants.ORGANIZATION_ADDRESS_EDITING_ENDPOINT, address);
		final ModelAndView modelAndView = new ModelAndView("addressEditor"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final OrganizationAddress addressObj;
		if (address != null && address.intValue() != 0) {
			addressObj = this.addressService.getAddressById(address.intValue());
			if (addressObj == null) {
				throw new IllegalArgumentException("Address not found: " + address); //$NON-NLS-1$
			}
		} else {
			addressObj = null;
		}
		//
		if (addressObj != null) {
			final String backgroundPath = addressObj.getPathToBackgroundImage();
			if (!Strings.isNullOrEmpty(backgroundPath)) {
				final String basename = FileSystem.largeBasename(backgroundPath);
				final String fileExtension = FileSystem.extension(backgroundPath);
				modelAndView.addObject("pathToBackgroundImage_basename", basename); //$NON-NLS-1$
				modelAndView.addObject("pathToBackgroundImage_picture", //$NON-NLS-1$
						rooted(this.fileManager.makeAddressBackgroundImage(addressObj.getId(), fileExtension)));
			}
		}
		//
		modelAndView.addObject("address", addressObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.ORGANIZATION_ADDRESS_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.ORGANIZATION_ADDRESS_LIST_ENDPOINT)); //$NON-NLS-1$
		return modelAndView;
	}


	/** Show the organization addresses into an HTML component.
	 *
	 * @param dbId the database identifier of the organization.
	 * @param name the name of acronym of the organization.
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/showOrganizationAddresses")
	public ModelAndView showOrganizationAddresses(
			@RequestParam(required = false) Integer dbId,
			@RequestParam(required = false) String name,
			@RequestParam(required = false, defaultValue = "false") boolean embedded,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, "/showOrganizationAddresses"); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("showOrganizationAddresses"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, embedded);
		//
		final ResearchOrganization organization;
		if (dbId != null && dbId.intValue() != 0) {
			final Optional<ResearchOrganization> organizationOpt = this.organizationService.getResearchOrganizationById(dbId.intValue());
			if (organizationOpt.isEmpty()) {
				throw new IllegalArgumentException("Organization not found with id: " + dbId); //$NON-NLS-1$
			}
			organization = organizationOpt.get();
		} else {
			final String inOrganizationAcronym = inString(name);
			if (!Strings.isNullOrEmpty(inOrganizationAcronym)) {
				final Optional<ResearchOrganization> organizationOpt = this.organizationService.getResearchOrganizationByAcronymOrName(name);
				if (organizationOpt.isEmpty()) {
					throw new IllegalArgumentException("Organization not found with name: " + name); //$NON-NLS-1$
				}
				organization = organizationOpt.get();
			} else {
				throw new IllegalArgumentException("Organization not found"); //$NON-NLS-1$
			}
		}
		assert organization != null;
		modelAndView.addObject("organization", organization); //$NON-NLS-1$
		//
		final List<OrganizationAddress> addresses = organization.getAddresses().stream().sorted(EntityUtils.getPreferredOrganizationAddressComparator()).collect(Collectors.toList());
		modelAndView.addObject("addresses", addresses); //$NON-NLS-1$
		//
		return modelAndView;
	}

}
