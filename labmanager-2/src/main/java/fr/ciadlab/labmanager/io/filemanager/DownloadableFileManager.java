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

import java.io.File;
import java.io.IOException;

import org.arakhne.afc.sizediterator.SizedIterator;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;
import org.springframework.web.multipart.MultipartFile;

/** Utilities for managing the downloadable files.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface DownloadableFileManager {

	/** Delete from the server the downloadable PDF file associated to the publication with given identifier.
	 *
	 * @param id the identifier of the publication.
	 * @throws Exception if the file cannot be deleted.
	 */
	void deleteDownloadablePublicationPdfFile(int id) throws Exception;

	/** Delete from the server the downloadable award's PDF file associated to the publication with given identifier.
	 *
	 * @param id the identifier of the publication.
	 * @throws Exception if the file cannot be deleted.
	 */
	void deleteDownloadableAwardPdfFile(int id) throws Exception;

	/** Delete from the server the address background image associated to the address with given identifier.
	 *
	 * @param id the identifier of the address.
	 * @param fileExtension the filename extension for the image ({@code .jpg}, {@code .gif}, {@code .png}).
	 * @throws Exception if the file cannot be deleted.
	 */
	void deleteAddressBackgroundImage(int id, String fileExtension);

	/** Replies the path to the a folder that could be used temporary.
	 *
	 * @return the path to the temporary folder.
	 */
	File getTemporaryRootFile();

	/** Replies the path to the root folder for pdf files.
	 *
	 * @return the path to the root folder.
	 */
	File getPdfRootFile();

	/** Replies the path to the root folder for award files.
	 *
	 * @return the path to the root folder.
	 */
	File getAwardRootFile();

	/** Replies the path to the root folder for address background images.
	 *
	 * @return the path to the root folder.
	 */
	File getAddressBackgroundRootFile();

	/** Replies the path to the root folder for saved files.
	 *
	 * @return the path to the root folder.
	 * @since 2.2
	 */
	File getSavingDataRootFile();

	/** Make the path to the PDF downloadable file for the publication with the given identifier.
	 *
	 * @param publicationId the identifier of the publication.
	 * @return the path to the downloadable file.
	 */
	File makePdfFilename(int publicationId);

	/** Make the path to the image that corresponds to the PDF downloadable file for the publication with the given identifier.
	 *
	 * @param publicationId the identifier of the publication.
	 * @return the path to the JPEG image associated to the downloadable file.
	 */
	File makePdfPictureFilename(int publicationId);

	/** Make the path to the award downloadable file for the publication with the given identifier.
	 *
	 * @param publicationId the identifier of the publication.
	 * @return the path to the downloadable file.
	 */
	File makeAwardFilename(int publicationId);

	/** Make the path to the image that corresponds to the award downloadable file for the publication with the given identifier.
	 *
	 * @param publicationId the identifier of the publication.
	 * @return the path to the JPEG image associated to the downloadable file.
	 */
	File makeAwardPictureFilename(int publicationId);

	/** Make the path to the image that corresponds to the address background file for the address with the given identifier.
	 *
	 * @param addressId the identifier of the address.
	 * @param fileExtension the filename extension for the image ({@code .jpg}, {@code .gif}, {@code .png}).
	 * @return the path to the image associated to the background image.
	 */
	File makeAddressBackgroundImage(int addressId, String fileExtension);

	/** Normalize a relative filename to be absolute for the server.
	 *
	 * @param file the relative filename.
	 * @return the absolute filename.
	 */
	File normalizeForServerSide(File file);

	/** Save the uploaded PDF file and its associated picture.
	 *
	 * @param pdfFilename the filename of the PDF file to upload.
	 * @param pictureFilename the filename of the JPEG file to create. 
	 * @param multipartPdfFile the content of the PDF file.
	 * @throws IOException if the file cannot be created.
	 */
	void saveFiles(File pdfFilename, File pictureFilename, MultipartFile multipartPdfFile) throws IOException;

	/** Save the uploaded address background image.
	 *
	 * @param filename the filename of the image to upload.
	 * @param backgroundImage the content of the image.
	 * @throws IOException if the file cannot be created.
	 */
	void saveAddressBackgroundImage(File filename, MultipartFile backgroundImage) throws IOException;

	/** Ensure that the picture file representing the PDF file is generated.
	 *
	 * @param pdfFilename the filename of the PDF file to upload.
	 * @param pictureFilename the filename of the JPEG file to create. 
	 * @throws IOException if the file cannot be created.
	 */
	void ensurePictureFile(File pdfFilename, File pictureFilename) throws IOException;

	/** Move the uploaded files from one publication to another publication.
	 * If the target files exist, they must not be replaced by the source files; but the source files
	 * must disappear from the file system.
	 *
	 * @param sourceId the identifier of the publication that is currently associated to the uploaded files. 
	 * @param targetId the identifier of the publication that should be associated to the uploaded files in place of the previous publication.
	 * @param callback lambda that is invoked each time a file has changed. THe arguments are the id of the file, the old name and the new name. 
	 * @throws IOException if the files cannot be moved.
	 */
	void moveFiles(int sourceId, int targetId, Procedure3<String, String, String> callback) throws IOException;

	/** Replies the list of all the uploaded PDF files.
	 *
	 * @return the list of uploaded PDF files
	 * @since 2.2
	 */
	SizedIterator<File> getUploadedPdfFiles();

	/** Replies the list of all the thumbnail files.
	 *
	 * @return the list of uploaded PDF files
	 * @since 2.2
	 */
	SizedIterator<File> getThumbailFiles();

	/** Regenerate all the thumbnail for the given files.
	 *
	 * @param file the PDF file.
	 * @throws IOException if the file cannot be created.
	 */
	void regenerateThumbnail(File file) throws IOException;

}
