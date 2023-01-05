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

package fr.ciadlab.labmanager.entities;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import fr.ciadlab.labmanager.entities.invitation.PersonInvitationComparator;
import fr.ciadlab.labmanager.entities.journal.JournalComparator;
import fr.ciadlab.labmanager.entities.jury.JuryMembershipComparator;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.MembershipComparator;
import fr.ciadlab.labmanager.entities.member.NameBasedMembershipComparator;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.member.PersonListComparator;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddressComparator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationComparator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationComparator;
import fr.ciadlab.labmanager.entities.publication.SorensenDicePublicationComparator;
import fr.ciadlab.labmanager.entities.supervision.Supervision;
import fr.ciadlab.labmanager.entities.supervision.SupervisionComparator;
import fr.ciadlab.labmanager.entities.supervision.SupervisorComparator;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Tools and configuration for the JPA entities.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class EntityUtils {

	/** Size of {@code VARCHAR} fields for large texts.
	 * By default, the length of columns is set to 255. This value
	 * permits to override this size for string-based columns.
	 * <p>This value is defined in order to be database independent for most of the {@code VARCHAR} specifications.
	 */
	public static final int LARGE_TEXT_SIZE = 32672;

	/** Size of {@code VARCHAR} fields for very small texts.
	 * By default, the length of columns is set to 32. This value
	 * permits to override this size for string-based columns.
	 * <p>This value is defined in order to be database independent for most of the {@code VARCHAR} specifications.
	 */
	public static final int VERY_SMALL_TEXT_SIZE = 32;

	private static final double SIMILARITY = 0.95;

	private static PersonComparator PERSON_COMPARATOR; 

	private static PersonListComparator PERSONLIST_COMPARATOR; 

	private static MembershipComparator MEMBERSHIP_COMPARATOR; 

	private static NameBasedMembershipComparator PERSON_NAME_MEMBERSHIP_COMPARATOR; 

	private static ResearchOrganizationComparator ORGANIZATION_COMPARATOR; 

	private static OrganizationAddressComparator ORGANIZATION_ADDRESS_COMPARATOR; 

	private static PublicationComparator PUBLICATION_COMPARATOR; 

	private static JournalComparator JOURNAL_COMPARATOR; 

	private static JuryMembershipComparator JURY_MEMBERSHIP_COMPARATOR; 

	private static PersonInvitationComparator PERSON_INVITATION_COMPARATOR; 

	private static SupervisorComparator SUPERVISOR_COMPARATOR; 

	private static SupervisionComparator SUPERVISION_COMPARATOR; 

	private static final NormalizedStringSimilarity SIMILARITY_COMPUTER = new SorensenDice();

	private static Comparator<? super Publication> DEFAULT_PUBLICATION_COMPARATOR = (a, b) -> {
		if (a == b) {
			return 0;
		}
		if (a == null) {
			return Integer.MIN_VALUE;
		}
		if (b == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = - Integer.compare(a.getPublicationYear(), b.getPublicationYear());
		if (cmp != 0) {
			return cmp;
		}
		cmp = EntityUtils.getPreferredPersonListComparator().compare(a.getAuthors(), b.getAuthors());
		if (cmp != 0) {
			return cmp;
		}
		cmp = a.getType().compareTo(b.getType());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compareIgnoreCase(a.getTitle(), b.getTitle(), true);
		if (cmp != 0) {
			return cmp;
		}
		return Integer.compare(a.getId(), b.getId());
	};

	private EntityUtils() {
		//
	}

	/** Replies the preferred comparator of persons.
	 *
	 * @return the comparator.
	 */
	public static PersonComparator getPreferredPersonComparator() {
		synchronized (EntityUtils.class) {
			if (PERSON_COMPARATOR == null) {
				PERSON_COMPARATOR = new PersonComparator();
			}
			return PERSON_COMPARATOR;
		}
	}

	/** Change the preferred comparator of persons.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredPersonComparator(PersonComparator comparator) {
		synchronized (EntityUtils.class) {
			PERSON_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of lists of persons.
	 *
	 * @return the comparator.
	 */
	public static PersonListComparator getPreferredPersonListComparator() {
		synchronized (EntityUtils.class) {
			if (PERSONLIST_COMPARATOR == null) {
				PERSONLIST_COMPARATOR = new PersonListComparator(getPreferredPersonComparator());
			}
			return PERSONLIST_COMPARATOR;
		}
	}

	/** Change the preferred comparator of lists of persons.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredPersonListComparator(PersonListComparator comparator) {
		synchronized (EntityUtils.class) {
			PERSONLIST_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of memberships.
	 *
	 * @return the comparator.
	 */
	public static MembershipComparator getPreferredMembershipComparator() {
		synchronized (EntityUtils.class) {
			if (MEMBERSHIP_COMPARATOR == null) {
				MEMBERSHIP_COMPARATOR = new MembershipComparator(
						getPreferredPersonComparator(), getPreferredResearchOrganizationComparator(),
						getPreferredOrganizationAddressComparator());
			}
			return MEMBERSHIP_COMPARATOR;
		}
	}

	/** Change the preferred comparator of memberships.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredMembershipComparator(MembershipComparator comparator) {
		synchronized (EntityUtils.class) {
			MEMBERSHIP_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of research organizations.
	 *
	 * @return the comparator.
	 */
	public static ResearchOrganizationComparator getPreferredResearchOrganizationComparator() {
		synchronized (EntityUtils.class) {
			if (ORGANIZATION_COMPARATOR == null) {
				ORGANIZATION_COMPARATOR = new ResearchOrganizationComparator();
			}
			return ORGANIZATION_COMPARATOR;
		}
	}

	/** Change the preferred comparator of research organizations.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredResearchOrganizationComparator(ResearchOrganizationComparator comparator) {
		synchronized (EntityUtils.class) {
			ORGANIZATION_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of organization addresses.
	 *
	 * @return the comparator.
	 */
	public static OrganizationAddressComparator getPreferredOrganizationAddressComparator() {
		synchronized (EntityUtils.class) {
			if (ORGANIZATION_ADDRESS_COMPARATOR == null) {
				ORGANIZATION_ADDRESS_COMPARATOR = new OrganizationAddressComparator();
			}
			return ORGANIZATION_ADDRESS_COMPARATOR;
		}
	}

	/** Change the preferred comparator of organization addresses.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredOrganizationAddressComparator(OrganizationAddressComparator comparator) {
		synchronized (EntityUtils.class) {
			ORGANIZATION_ADDRESS_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of publications.
	 *
	 * @return the comparator.
	 */
	public static PublicationComparator getPreferredPublicationComparator() {
		synchronized (EntityUtils.class) {
			if (PUBLICATION_COMPARATOR == null) {
				PUBLICATION_COMPARATOR = new SorensenDicePublicationComparator();
			}
			return PUBLICATION_COMPARATOR;
		}
	}

	/** Replies the preferred comparator of publications that is used for sorting the list of publications.
	 *
	 * @return the comparator.
	 */
	public static Comparator<? super Publication> getPreferredPublicationComparatorInLists() {
		synchronized (EntityUtils.class) {
			return DEFAULT_PUBLICATION_COMPARATOR;
		}
	}

	/** Change the preferred comparator of publications.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredPublicationComparator(PublicationComparator comparator) {
		synchronized (EntityUtils.class) {
			PUBLICATION_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of memberships based on the person's names.
	 *
	 * @return the comparator.
	 */
	public static NameBasedMembershipComparator getPreferredPersonNameBasedMembershipComparator() {
		synchronized (EntityUtils.class) {
			if (PERSON_NAME_MEMBERSHIP_COMPARATOR == null) {
				PERSON_NAME_MEMBERSHIP_COMPARATOR = new NameBasedMembershipComparator(
						getPreferredPersonComparator(), getPreferredResearchOrganizationComparator());
			}
			return PERSON_NAME_MEMBERSHIP_COMPARATOR;
		}
	}

	/** Change the preferred comparator of memberships based on the person's names.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredPersonNameBasedMembershipComparator(NameBasedMembershipComparator comparator) {
		synchronized (EntityUtils.class) {
			PERSON_NAME_MEMBERSHIP_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of journals.
	 *
	 * @return the comparator.
	 */
	public static JournalComparator getPreferredJournalComparator() {
		synchronized (EntityUtils.class) {
			if (JOURNAL_COMPARATOR == null) {
				JOURNAL_COMPARATOR = new JournalComparator();
			}
			return JOURNAL_COMPARATOR;
		}
	}

	/** Change the preferred comparator of journals.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredJournalComparator(JournalComparator comparator) {
		synchronized (EntityUtils.class) {
			JOURNAL_COMPARATOR = comparator;
		}
	}

	/** Check if the information from two publications correspond to similar publications, WITHOUT
	 * normalization of the strings. It is assumed that the given strings are already normalized.
	 * 
	 * @param year0 the publication year of the first publication.
	 * @param title0 the title of the first publication.
	 * @param doi0 the DOI of the first publication.
	 * @param issn0 the ISSN of the first publication.
	 * @param target0 the target of the first publication.
	 * @param year1 the publication year of the second publication.
	 * @param title1 the title of the second publication.
	 * @param doi1 the DOI of the second publication.
	 * @param issn1 the ISSN of the second publication.
	 * @param target1 the target of the second publication.
	 * @return {@code true} if the two publications are similar.
	 * @see #normalizeForSimularityTest(String)
	 */
	public static boolean isSimilarWithoutNormalization(
			int year0, String title0, String doi0, String issn0, String target0,
			int year1, String title1, String doi1, String issn1, String target1) {
		return isSimilar(year0, year1)
				&& isSimilarWithoutNormalization(title0, title1)
				&& isSimilarWithoutNormalization(doi0, doi1)
				&& isSimilarWithoutNormalization(issn0, issn1)
				&& isSimilarWithoutNormalization(target0, target1);
	}

	/** Normalize a string for enabling its similarity computation.
	 *
	 * @param a the string to normalize.
	 * @return the normalized string.
	 * @see #isSimilarWithoutNormalization(int, String, String, String, String, int, String, String, String, String)
	 * @see #isSimilarWithoutNormalization(String, String)
	 */
	public static String normalizeForSimularityTest(String a) {
		if (a == null) {
			return null;
		}
		String str = a.toLowerCase().trim();
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		return str.replaceAll("\\P{InBasic_Latin}", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static boolean isSimilar(int a, int b) {
		return Math.abs(a - b) <= 1;
	}

	/** Replies if the two given strings are considered as similar.
	 * The two given strings are supposed to be normalized with {@link #normalizeForSimularityTest(String)}.
	 *
	 * @param a the first string to test.
	 * @param b the second string to test.
	 * @return {@code true} if the two strings are similar.
	 * @see #normalizeForSimularityTest(String)
	 */
	public static boolean isSimilarWithoutNormalization(String a, String b) {
		if (a == null || b == null) {
			return true;
		}
		return SIMILARITY_COMPUTER.similarity(a, b) >= SIMILARITY;
	}

	/** Replies the preferred comparator of jury memberships.
	 *
	 * @return the comparator.
	 */
	public static JuryMembershipComparator getPreferredJuryMembershipComparator() {
		synchronized (EntityUtils.class) {
			if (JURY_MEMBERSHIP_COMPARATOR == null) {
				JURY_MEMBERSHIP_COMPARATOR = new JuryMembershipComparator(getPreferredPersonComparator());
			}
			return JURY_MEMBERSHIP_COMPARATOR;
		}
	}

	/** Change the preferred comparator of jury memberships.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredJuryMembershipComparator(JuryMembershipComparator comparator) {
		synchronized (EntityUtils.class) {
			JURY_MEMBERSHIP_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of person invitations.
	 *
	 * @return the comparator.
	 */
	public static PersonInvitationComparator getPreferredPersonInvitationComparator() {
		synchronized (EntityUtils.class) {
			if (PERSON_INVITATION_COMPARATOR == null) {
				PERSON_INVITATION_COMPARATOR = new PersonInvitationComparator(getPreferredPersonComparator());
			}
			return PERSON_INVITATION_COMPARATOR;
		}
	}

	/** Change the preferred comparator of person invitations.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredPersonInvitationComparator(PersonInvitationComparator comparator) {
		synchronized (EntityUtils.class) {
			PERSON_INVITATION_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of supervisors.
	 *
	 * @return the comparator.
	 */
	public static SupervisorComparator getPreferredSupervisorComparator() {
		synchronized (EntityUtils.class) {
			if (SUPERVISOR_COMPARATOR == null) {
				SUPERVISOR_COMPARATOR = new SupervisorComparator(getPreferredPersonComparator());
			}
			return SUPERVISOR_COMPARATOR;
		}
	}

	/** Change the preferred comparator of supervisors.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredSupervisorComparator(SupervisorComparator comparator) {
		synchronized (EntityUtils.class) {
			SUPERVISOR_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of supervisions.
	 *
	 * @return the comparator.
	 */
	public static SupervisionComparator getPreferredSupervisionComparator() {
		synchronized (EntityUtils.class) {
			if (SUPERVISION_COMPARATOR == null) {
				SUPERVISION_COMPARATOR = new SupervisionComparator(
						getPreferredMembershipComparator(),
						getPreferredSupervisorComparator());
			}
			return SUPERVISION_COMPARATOR;
		}
	}

	/** Change the preferred comparator of supervisions.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredSupervisionComparator(SupervisionComparator comparator) {
		synchronized (EntityUtils.class) {
			SUPERVISION_COMPARATOR = comparator;
		}
	}

	/** Replies the name of the research organization, and possibly of the enclosing organizations up to the university.
	 *
	 * @param supervision the source supervision.
	 * @param separator the text to put between the names of the organizations.
	 * @return the name.
	 * @since 2.2
	 */
	public static String getNameInUniversity(Supervision supervision, String separator) {
		final Membership membership = supervision.getSupervisedPerson();
		final ResearchOrganization organization = membership.getResearchOrganization();
		final StringBuilder outcome = new StringBuilder();
		outcome.append(organization.getNameOrAcronym());
		
		final StringBuilder univBuffer = new StringBuilder();
		final int univType = ResearchOrganizationType.UNIVERSITY.ordinal();
		boolean univFound = false;
		if (organization.getType().ordinal() <= univType) {
			ResearchOrganization org = organization.getSuperOrganization();
			while (org != null && org.getType().ordinal() <= univType) {
				univFound = org.getType().ordinal() == univType;
				univBuffer.append(separator);
				univBuffer.append(org.getNameOrAcronym());
				org = org.getSuperOrganization();
			}
		}

		if (!univFound) {
			final LocalDate timeStart = membership.getMemberSinceWhen() == null
					? LocalDate.of(supervision.getYear(), 1, 1) : membership.getMemberSinceWhen();
			final LocalDate timeEnd = membership.getMemberToWhen() == null
					? LocalDate.of(supervision.getYear(), 12, 31) : membership.getMemberToWhen();
			final Iterator<Membership> iterator = supervision.getSupervisedPerson().getPerson().getMemberships().stream()
					.filter(it -> it.getId() != membership.getId() && it.isActiveIn(timeStart, timeEnd)).iterator();
			while (iterator.hasNext()) {
				final Membership mbr = iterator.next();
				if (mbr.getResearchOrganization().getType() == ResearchOrganizationType.UNIVERSITY) {
					outcome.append(separator);
					outcome.append(mbr.getResearchOrganization().getNameOrAcronym());
					break;
				}
			}
		} else {
			outcome.append(univBuffer);
		}
		
		return outcome.toString();
	}

	/** Replies the university/school/company that corresponds to the given organization.
	 *
	 * @param organization the organization.
	 * @return the parent organization or the given organization if it is not an university/school/company.
	 * @since 2.2
	 */
	public static ResearchOrganization getUniversityOrSchoolOrCompany(ResearchOrganization organization) {
		ResearchOrganization current = organization;
		while (current != null && current.getType().ordinal() < ResearchOrganizationType.UNIVERSITY.ordinal()) {
			current = current.getSuperOrganization();
		}
		if (current != null && current.getType().ordinal() >= ResearchOrganizationType.UNIVERSITY.ordinal()) {
			return current;
		}
		return organization;
	}

	/** Replies the name of the university or company for the given person regarding the time windows of the given supervision.
	 *
	 * @param supervision the source supervision.
	 * @param person the person.
	 * @param prefix the text to put before the name of the university.
	 * @param postfix the text to put after the name of the university.
	 * @param acronym indicates if the acronyms of the organizations are preferred to the full names.
	 * @return the university name, or empty string
	 * @since 2.2
	 */
	public static String getUniversityOrSchoolOrCompany(Supervision supervision, Person person, String prefix, String postfix, boolean acronym) {
		final Membership membership = supervision.getSupervisedPerson();
		final LocalDate timeStart = membership.getMemberSinceWhen() == null
				? LocalDate.of(supervision.getYear(), 1, 1) : membership.getMemberSinceWhen();
		final LocalDate timeEnd = membership.getMemberToWhen() == null
				? LocalDate.of(supervision.getYear(), 12, 31) : membership.getMemberToWhen();
		final Iterator<Membership> iterator = person.getMemberships().stream()
				.filter(it -> it.isActiveIn(timeStart, timeEnd)).iterator();
		while (iterator.hasNext()) {
			final Membership mbr = iterator.next();
			final ResearchOrganization org = mbr.getResearchOrganization();
			final ResearchOrganizationType otype = org.getType();
			if (otype == ResearchOrganizationType.UNIVERSITY || otype == ResearchOrganizationType.HIGH_SCHOOL
					|| otype == ResearchOrganizationType.OTHER) {
				final StringBuilder b = new StringBuilder();
				if (prefix != null) {
					b.append(prefix);
				}
				if (acronym) {
					b.append(org.getAcronymOrName());
				} else {
					b.append(org.getNameOrAcronym());
				}
				b.append(", "); //$NON-NLS-1$
				b.append(CountryCodeUtils.getDisplayCountry(org.getCountry()));
				if (postfix != null) {
					b.append(postfix);
				}
				return b.toString();
			}
		}
		return ""; //$NON-NLS-1$
	}

	/** Fixing the carriage-return characters in the address complements.
	 * Replace the CR characters by the equivalent protected string.
	 *
	 * @param complement the complement to fix.
	 * @return the fixed complement.
	 * @see #fixAddressComplementForBackend(String)
	 * @see #getAddressComplementComponents(String)
	 * @since 2.2
	 */
	public static String fixAddressComplementForEditor(String complement) {
		if (complement == null) {
			return null;
		}
		return complement.replace("\n", "\\n"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Fixing the carriage-return characters in the address complements.
	 * Replace the protected CR characters by the equivalent unicode characters.
	 *
	 * @param complement the complement to fix.
	 * @return the fixed complement.
	 * @see #fixAddressComplementForEditor(String)
	 * @see #getAddressComplementComponents(String)
	 * @since 2.2
	 */
	public static String fixAddressComplementForBackend(String complement) {
		if (complement == null) {
			return null;
		}
		return complement.replace("\\n", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Extract the components of an address complement. The components are separated by carriage return characters.
	 *
	 * @param complement the complement to analyze.
	 * @return the components of the complement.
	 * @see #fixAddressComplementForEditor(String)
	 * @see #fixAddressComplementForBackend(String)
	 * @since 2.2
	 */
	public static List<String> getAddressComplementComponents(String complement) {
		if (Strings.isNullOrEmpty(complement)) {
			return Collections.emptyList();
		}
		final String[] components = complement.split("[\n]+");  //$NON-NLS-1$
		return Arrays.asList(components);
	}

}
