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

package fr.ciadlab.labmanager.utils.vcard;

import java.net.URI;
import java.net.URL;
import java.util.Locale;

import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

/** A builder of a Virtual Card File (VCF) based on the description on {@link "https://en.wikipedia.org/wiki/VCard"}.
 * <p>Virtual Card Format (VCF) or vCard is a digital file format for storing contact information.
 * The format is widely used for data interchange among popular information exchange applications.
 * A single VCF file can contain contact information for one or multiple contacts.
 * A VCF file usually contains information such as contact’s name, address, phone number, email,
 * birthday, photographs and audio in addition to a number of other fields.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 * @see "https://en.wikipedia.org/wiki/VCard"
 */
@Component
@Primary
public class DefaultVcardBuilder implements VcardBuilder {

	private static final String VCARD_START = "BEGIN:VCARD\nVERSION:4.0\n"; //$NON-NLS-1$
	
	private static final String EMAIL = "EMAIL:"; //$NON-NLS-1$

	private static final String URL_PREFIX = "URL:"; //$NON-NLS-1$

	private static final String OFFICE_PHONE = "office,voice"; //$NON-NLS-1$

	private static final String MOBILE_PHONE = "mobile,voice"; //$NON-NLS-1$

	private static final String OFFICE_ROOM = "office,room"; //$NON-NLS-1$

	private static final String NAME = "N:"; //$NON-NLS-1$

	private static final String FULLNAME = "FN:"; //$NON-NLS-1$

	private static final String ORGANIZATION = "ORG:"; //$NON-NLS-1$

	private static final String TITLE = "TITLE:"; //$NON-NLS-1$

	private static final String TEL_TYPE = "TEL;TYPE#"; //$NON-NLS-1$

	private static final String TEL_NUM = ";VALUE#uri:tel:"; //$NON-NLS-1$

	private static final String PHOTO_TYPE = "PHOTO;MEDIATYPE#image/jpeg:"; //$NON-NLS-1$

	private static final String ROLE = "ROLE:"; //$NON-NLS-1$

	private static final String VCARD_END = "END:VCARD\n"; //$NON-NLS-1$

	private static final String ADR_TYPE = "ADR:TYPE="; //$NON-NLS-1$

	private static final String ADR_VALUE = ":"; //$NON-NLS-1$
	
	private static void append(StringBuilder vcard, String property, String value) {
		if (!Strings.isNullOrEmpty(value)) {
			vcard.append(property).append(value).append("\n"); //$NON-NLS-1$
		}
	}
	
	private static void append(StringBuilder vcard, String property, String... value) {
		if (value != null && value.length > 0) {
			vcard.append(property);
			boolean first = true;
			for (final String val : value) {
				if (first) {
					first = false;
				} else {
					vcard.append(";"); //$NON-NLS-1$
				}
				if (!Strings.isNullOrEmpty(val)) {
					vcard.append(val);
				}
			}
			vcard.append("\n"); //$NON-NLS-1$
		}
	}

	private static void appendTo(StringBuilder buffer, String value) {
		if (!Strings.isNullOrEmpty(value)) {
			if (buffer.length() > 0) {
				buffer.append(";"); //$NON-NLS-1$
			}
			buffer.append(value);
		}
	}

	private static void appendAdr(StringBuilder vcard, String type, String postOfficeBox, String additionalNumber, String numberStreetAddress,
			String locality, String region, String zipCode, String country) {
		final StringBuilder value = new StringBuilder();
		appendTo(value, postOfficeBox);
		appendTo(value, additionalNumber);
		appendTo(value, numberStreetAddress);
		appendTo(value, locality);
		appendTo(value, region);
		appendTo(value, zipCode);
		appendTo(value, country);
		if (value.length() > 0) {
			vcard.append(ADR_TYPE).append(type).append(ADR_VALUE);
			vcard.append(value.toString()).append("\n"); //$NON-NLS-1$
		}
	}

	private static void appendTel(StringBuilder vcard, String type, String number) {
		if (!Strings.isNullOrEmpty(number)) {
			vcard.append(TEL_TYPE).append(type).append(TEL_NUM).append(number.replaceAll("\\s+", "-")).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	private static void appendPhoto(StringBuilder vcard, URL url) {
		if (url != null) {
			vcard.append(PHOTO_TYPE).append(url.toExternalForm()).append("\n"); //$NON-NLS-1$
		}
	}

	private static void append(StringBuilder vcard, String property, URL url) {
		if (url != null) {
			vcard.append(property).append(url.toExternalForm()).append("\n"); //$NON-NLS-1$
		}
	}

	private static void append(StringBuilder vcard, String property, URI uri) {
		if (uri != null) {
			final UriComponents currentUri =  ServletUriComponentsBuilder.fromCurrentContextPath().build();
			URI nuri;
			try {
				nuri = new URI(
						currentUri.getScheme(), null,
						currentUri.getHost(),
						currentUri.getPort(),
				        uri.getPath(),
				        uri.getQuery(),
				        uri.getFragment());
				vcard.append(property).append(nuri.toURL()).append("\n"); //$NON-NLS-1$
			} catch (Exception ex) {
				//
			}
		}
	}

	private static void append(StringBuilder vcard, Membership membership, Membership university) {
		if (membership != null) {
			append(vcard, TITLE, membership.getMemberStatus().getLabel(Locale.US));
			final StringBuilder org = new StringBuilder();
			ResearchOrganization ro = membership.getResearchOrganization();
			if (university == null) {
				do {
					if (org.length() > 0) {
						org.insert(0, ";"); //$NON-NLS-1$
					}
					org.insert(0, ro.getName());
					ro = ro.getSuperOrganization();
				} while (ro != null);
			} else {
				org.append(university.getResearchOrganization().getName()).append(";").append(ro.getName()); //$NON-NLS-1$
			}
			append(vcard, ORGANIZATION, org.toString());
			if (membership.getResponsibility() != null) {
				append(vcard, ROLE, membership.getResponsibility().getLabel(membership.getPerson().getGender(), Locale.US));
			}
		}
	}

	@Override
	public String build(Person person, ResearchOrganization organization) {
		final StringBuilder vcard = new StringBuilder();
		vcard.append(VCARD_START);
		append(vcard, NAME, person.getLastName(), person.getFirstName(), null, person.getCivilTitle(Locale.US));
		append(vcard, FULLNAME, person.getFullName());
		append(vcard, EMAIL, person.getEmail());
		append(vcard, URL_PREFIX, person.getWebPageURI());
		append(vcard, URL_PREFIX, person.getOrcidURL());
		append(vcard, URL_PREFIX, person.getGoogleScholarURL());
		append(vcard, URL_PREFIX, person.getResearcherIdURL());
		append(vcard, URL_PREFIX, person.getResearchGateURL());
		append(vcard, URL_PREFIX, person.getLinkedInURL());
		append(vcard, URL_PREFIX, person.getDblpURLObject());
		append(vcard, URL_PREFIX, person.getAcademiaURLObject());
		append(vcard, URL_PREFIX, person.getGithubURL());
		appendTel(vcard, OFFICE_PHONE, person.getOfficePhone());
		appendTel(vcard, MOBILE_PHONE, person.getMobilePhone());
		appendAdr(vcard, OFFICE_ROOM,
				null, // B.O.
				person.getOfficeRoom(),
				null, // Street
				null, // Locality
				null, // Region
				null, // Zip code
				organization == null ? null : organization.getCountryDisplayName());
		Membership universityMembership = null;
		Membership detailMembership = null;
		for (final Membership membership : person.getActiveMemberships().values()) {
			if (universityMembership == null
					|| membership.getResearchOrganization().getType().compareTo(ResearchOrganizationType.UNIVERSITY) >= 0) {
				universityMembership = membership;
			}
			if (organization != null) {
				if (detailMembership == null || organization.getId() == membership.getResearchOrganization().getId()) {
					detailMembership = membership;
				}
			} else if (detailMembership == null
					|| detailMembership.getResearchOrganization().getType().compareTo(membership.getResearchOrganization().getType()) > 0){
				detailMembership = membership;
			}
		}
		if (detailMembership != null) {
			append(vcard, detailMembership, universityMembership);
		}
		appendPhoto(vcard, person.getPhotoURL());
		vcard.append(VCARD_END);
		return vcard.toString();
	}

}
