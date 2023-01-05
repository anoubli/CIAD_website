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

package fr.ciadlab.labmanager.io.filemanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.arakhne.afc.vmutil.FileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link DefaultDownloadableFileManager}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class DefaultDownloadableFileManagerTest {

	private DefaultDownloadableFileManager test;

	private ByteArrayOutputStream stream;

	private File root;

	private File tmp;

	@BeforeEach
	public void setUp() throws IOException {
		this.stream = new ByteArrayOutputStream();
		this.root = new File(File.listRoots()[0], "rootuploads");
		this.tmp = new File(File.listRoots()[0], "rootuploads-tmp");
		this.test = new DefaultDownloadableFileManager(this.root.toString(), this.tmp.toString());
	}

	@Test
	public void normalizeForServerSide_null() {
		assertNull(this.test.normalizeForServerSide(null));
	}

	@Test
	public void normalizeForServerSide_empty() {
		final File file = FileSystem.join(new File("Downloadables"), "PDFs", "PDF1234.pdf");
		final File actual = this.test.normalizeForServerSide(file);
		assertNotNull(actual);
		final String expected = FileSystem.join(this.root, file).toString(); 
		assertEquals(expected, actual.getAbsolutePath());
	}

	@Test
	public void getPdfRootFile() {
		assertNotNull(this.test.getPdfRootFile());
	}

	@Test
	public void getAwardRootFile() {
		assertNotNull(this.test.getAwardRootFile());
	}

	@Test
	public void makePdfFilename() {
		assertNotNull(this.test.makePdfFilename(123));
	}

	@Test
	public void makeAwardFilename() {
		assertNotNull(this.test.makeAwardFilename(123));
	}

}
