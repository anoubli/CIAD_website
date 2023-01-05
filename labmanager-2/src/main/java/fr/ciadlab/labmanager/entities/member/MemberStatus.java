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

package fr.ciadlab.labmanager.entities.member;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/** Status of a person in a research organization.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum MemberStatus {
	/** Full professor (professeur des universites).
	 */
	FULL_PROFESSOR {
		@Override
		public int getHierachicalLevel() {
			return 0;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 0.5f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return true;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Research Director (Directeur de recherche).
	 */
	RESEARCH_DIRECTOR {
		@Override
		public int getHierachicalLevel() {
			return 0;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 1f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return true;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Associate professor (maitre de conferences) with HDR.
	 */
	ASSOCIATE_PROFESSOR_HDR {
		@Override
		public int getHierachicalLevel() {
			return 1;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 0.5f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return true;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Associate professor (maitre de conferences) without HDR.
	 */
	ASSOCIATE_PROFESSOR {
		@Override
		public int getHierachicalLevel() {
			return 2;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 0.5f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return true;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Contractual teacher-researcher (enseignant chercheur contractuel) with PhD.
	 */
	CONTRACTUAL_RESEARCHER_TEACHER_PHD {
		@Override
		public int getHierachicalLevel() {
			return 2;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 0.5f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return true;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Contractual teacher-researcher (enseignant chercheur contractuel) without PhD.
	 */
	CONTRACTUAL_RESEARCHER_TEACHER {
		@Override
		public int getHierachicalLevel() {
			return 2;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 0.5f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return false;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Researcher (Chargé de recherche, LRU, etc.) with PhD.
	 */
	RESEARCHER_PHD {
		@Override
		public int getHierachicalLevel() {
			return 2;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 1f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return true;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Researcher (Chargé de recherche, LRU, etc.) without PhD.
	 */
	RESEARCHER {
		@Override
		public int getHierachicalLevel() {
			return 2;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 1f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return false;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Postdoc.
	 */
	POSTDOC {
		@Override
		public int getHierachicalLevel() {
			return 3;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 1f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return false;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return true;
		}
		@Override
		public boolean isSupervisable() {
			return true;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Research engineer (IGR) with PhD.
	 */
	RESEARCH_ENGINEER_PHD {
		@Override
		public int getHierachicalLevel() {
			return 3;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 1f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return true;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return true;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Research engineer (IGR) without PhD.
	 */
	RESEARCH_ENGINEER {
		@Override
		public int getHierachicalLevel() {
			return 3;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 1f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return true;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return false;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** PHD student.
	 */
	PHD_STUDENT {
		@Override
		public int getHierachicalLevel() {
			return 4;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 1f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return false;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return false;
		}
		@Override
		public boolean isSupervisable() {
			return true;
		}
		@Override
		public boolean isSupervisor() {
			return false;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Engineer (IGE) with PhD.
	 */
	ENGINEER_PHD {
		@Override
		public int getHierachicalLevel() {
			return 4;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 1f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return true;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return true;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Engineer (IGE) without PhD.
	 */
	ENGINEER {
		@Override
		public int getHierachicalLevel() {
			return 4;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 1f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return true;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return false;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return false;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Administrative staff.
	 */
	ADMIN {
		@Override
		public int getHierachicalLevel() {
			return 4;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 0f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return true;
		}
		@Override
		public boolean isPhDOwner() {
			return false;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return false;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Teacher (PRAG, etc.) with PhD.
	 */
	TEACHER_PHD {
		@Override
		public int getHierachicalLevel() {
			return 4;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 0f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return true;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Teacher (PRAG, etc.) without PhD.
	 */
	TEACHER {
		@Override
		public int getHierachicalLevel() {
			return 4;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 0f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return true;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return false;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return false;
		}
		@Override
		public boolean isExternalPosition() {
			return false;
		}
	},
	/** Master student (internship).
	 */
	MASTER_STUDENT {
		@Override
		public int getHierachicalLevel() {
			return 5;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 1f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return false;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return false;
		}
		@Override
		public boolean isSupervisable() {
			return true;
		}
		@Override
		public boolean isSupervisor() {
			return false;
		}
		@Override
		public boolean isExternalPosition() {
			return true;
		}
	},
	/** Bachelor student (internship).
	 */
	OTHER_STUDENT {
		@Override
		public int getHierachicalLevel() {
			return 6;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 1f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return false;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return false;
		}
		@Override
		public boolean isSupervisable() {
			return true;
		}
		@Override
		public boolean isSupervisor() {
			return false;
		}
		@Override
		public boolean isExternalPosition() {
			return true;
		}
	},
	/** Associate member with PhD.
	 */
	ASSOCIATED_MEMBER_PHD {
		@Override
		public int getHierachicalLevel() {
			return 7;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 0f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return false;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return true;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return true;
		}
	},
	/** Associate member without PhD.
	 */
	ASSOCIATED_MEMBER {
		@Override
		public int getHierachicalLevel() {
			return 7;
		}
		@Override
		public float getUsualResearchFullTimeEquivalent() {
			return 0f;
		}
		@Override
		public boolean isPermanentPositionAllowed() {
			return false;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
		@Override
		public boolean isPhDOwner() {
			return false;
		}
		@Override
		public boolean isSupervisable() {
			return false;
		}
		@Override
		public boolean isSupervisor() {
			return true;
		}
		@Override
		public boolean isExternalPosition() {
			return true;
		}
	};

	private static final String MESSAGE_PREFIX = "memberStatus."; //$NON-NLS-1$

	private static final String TITLE_POSTFIX = "_title"; //$NON-NLS-1$

	private MessageSourceAccessor messages;

	/** Replies the message accessor to be used.
	 *
	 * @return the accessor.
	 */
	public MessageSourceAccessor getMessageSourceAccessor() {
		if (this.messages == null) {
			this.messages = BaseMessageSource.getStaticMessageSourceAccessor();
		}
		return this.messages;
	}

	/** Change the message accessor to be used.
	 *
	 * @param messages the accessor.
	 */
	public void setMessageSourceAccessor(MessageSourceAccessor messages) {
		this.messages = messages;
	}

	/** Replies if the status supposes that the member owns a PhD.
	 *
	 * @return {@code true} if the member is supposed to own a PhD.
	 */
	public abstract boolean isPhDOwner();

	/** Replies the hierarchical level.
	 *
	 * @return the level: {@code 0} for the higher level, higher is the value lower is the hierarchical position.
	 */
	public abstract int getHierachicalLevel();

	/** Replies the amount of full-time equivalent for this type of member status that is usually defined
	 * in the job status for the associated position.
	 *
	 * @return the amount of full-time equivalent. {@code 1} means a full-time, {@code 0.5} means 50% part-time.
	 * @since 2.2
	 */
	public abstract float getUsualResearchFullTimeEquivalent();

	/** Replies if this status allowed to be associated to a permanent position.
	 *
	 * @return {@code true} if the status could be associated to a permanent position.
	 * @since 2.2
	 */
	public abstract boolean isPermanentPositionAllowed();

	/** Replies if the status includes research activities.
	 *
	 * @return {@code true} if the status includes activities of research.
	 */
	public abstract boolean isResearcher();

	/** Replies if the status includes teaching activities.
	 *
	 * @return {@code true} if the status includes activities of teaching.
	 */
	public abstract boolean isTeacher();

	/** Replies if the status includes engineering or technical activities.
	 *
	 * @return {@code true} if the status includes engineering or technical activities.
	 */
	public abstract boolean isTechnicalStaff();

	/** Replies if the status includes administrative activities.
	 *
	 * @return {@code true} if the status includes administrative activities.
	 */
	public abstract boolean isAdministrativeStaff();

	/** Replies if the status enables to be supervised by another person.
	 *
	 * @return {@code true} if the status enables supervision by another person.
	 * @since 2.1
	 */
	public abstract boolean isSupervisable();

	/** Replies if the status enables to be supervisor of another person.
	 *
	 * @return {@code true} if the status enables supervisor of another person.
	 * @since 2.1
	 */
	public abstract boolean isSupervisor();

	/** Replies if this status corresponds to an external position outside the organization itself.
	 *
	 * @return {@code true} if the status could be associated to an external position.
	 * @since 2.3
	 */
	public abstract boolean isExternalPosition();

	/** Replies the label of the status in the current language.
	 *
	 * @return the label of the status in the current language.
	 */
	public String getLabel() {
		return getLabel(null, false, null);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param former is the status for a former member?
	 * @return the label of the status in the current language.
	 */
	public String getLabel(boolean former) {
		return getLabel(null, former, null);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Locale locale) {
		return getLabel(null, false, locale);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param former is the status for a former member?
	 * @param locale the locale to use.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(boolean former, Locale locale) {
		return getLabel(null, former, locale);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param gender the gender of the person concerned by the member status.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Gender gender) {
		return getLabel(gender, false, null);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param gender the gender of the person concerned by the member status.
	 * @param locale the locale to use.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Gender gender, Locale locale) {
		return getLabel(gender, false, locale);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param gender the gender of the person concerned by the member status.
	 * @param former is the status for a former member?
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Gender gender, boolean former) {
		return getLabel(gender, former, null);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param gender the gender of the person concerned by the member status.
	 * @param former is the status for a former member?
	 * @param locale the locale to use.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Gender gender, boolean former, Locale locale) {
		final Gender gndr = gender == null || gender == Gender.NOT_SPECIFIED ? Gender.OTHER : gender;
		final StringBuilder key = new StringBuilder();
		key.append(MESSAGE_PREFIX).append(name());
		if (former) {
			key.append("_former"); //$NON-NLS-1$
		}
		key.append("_").append(gndr.name()); //$NON-NLS-1$
		final String label;
		if (locale == null) {
			label = getMessageSourceAccessor().getMessage(key.toString());
		} else {
			label = getMessageSourceAccessor().getMessage(key.toString(), locale);
		}
		return Strings.nullToEmpty(label);
	}

	/** Replies the member status that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the status, to search for.
	 * @return the status.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type.
	 */
	public static MemberStatus valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final MemberStatus status : values()) {
				if (name.equalsIgnoreCase(status.name())) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("Invalid member status: " + name); //$NON-NLS-1$
	}

	/** Replies the civil title associated to this status, if it not a civil title based on gender.
	 *
	 * @return the civil title or {@code null} if none is defined.
	 * @see Gender
	 */
	public String getCivilTitle() {
		return Strings.emptyToNull(getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + TITLE_POSTFIX, (String) null));
	}

	/** Replies the civil title associated to this status, if it not a civil title based on gender.
	 *
	 * @param locale the locale that must be used for creating the localized civil title. 
	 * @return the civil title or {@code null} if none is defined.
	 * @see Gender
	 */
	public String getCivilTitle(Locale locale) {
		return Strings.emptyToNull(getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + TITLE_POSTFIX, (String) null, locale));
	}

}
