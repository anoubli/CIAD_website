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

package fr.ciadlab.labmanager.io.bibtex;

import fr.ciadlab.labmanager.entities.journal.Journal;

/** Fake of a journal.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.4
 */
public class JournalFake extends Journal {

	private static final long serialVersionUID = 8594111079867959152L;

	/** Constructor.
	 *
	 * @param name the name of the journal.
	 * @param publisher the name of the journal publisher.
	 * @param issn the ISSN number of the journal.
	 */
	public JournalFake(String name, String publisher, String issn) {
		setJournalName(name);
		setPublisher(publisher);
		setISSN(issn);
	}

	@Override
	public boolean isFakeEntity() {
		return true;
	}

}
