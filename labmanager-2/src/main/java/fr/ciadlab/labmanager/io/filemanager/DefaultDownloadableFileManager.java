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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.aspose.pdf.Document;
import com.aspose.pdf.Page;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/** Utilities for managing the downloadable files. This implementation is dedicated to the WordPress service
 * of the lab.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
@Primary
public class DefaultDownloadableFileManager implements DownloadableFileManager {

	private static final String DOWNLOADABLE_FOLDER_NAME = "Downloadables"; //$NON-NLS-1$

	private static final String PDF_FOLDER_NAME = "PDFs"; //$NON-NLS-1$

	private static final String PDF_FILE_PREFIX = "PDF"; //$NON-NLS-1$

	private static final String PDF_FILE_EXTENSION = ".pdf"; //$NON-NLS-1$

	private static final String JPEG_FILE_EXTENSION = ".jpg"; //$NON-NLS-1$

	private static final String AWARD_FOLDER_NAME = "Awards"; //$NON-NLS-1$

	private static final String AWARD_FILE_PREFIX = "Award"; //$NON-NLS-1$

	private final File uploadFolder;

	/** Constructor with the given stream factory.
	 *
	 * @param factory the factory.
	 * @param uploadFolder the path of the upload folder. It is defined by the property {@code labmanager.file.upload-directory}.
	 */
	public DefaultDownloadableFileManager(@Value("${labmanager.file.upload-directory}") String uploadFolder) {
		final String f = Strings.emptyToNull(uploadFolder);
		if (f == null) {
			this.uploadFolder = null;
		} else {
			this.uploadFolder = FileSystem.convertStringToFile(f).getAbsoluteFile();
		}
	}

	@Override
	public File getPdfRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), PDF_FOLDER_NAME);
	}

	@Override
	public File getAwardRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), AWARD_FOLDER_NAME);
	}

	@Override
	public File makePdfFilename(int publicationId) {
		return FileSystem.join(getPdfRootFile(), PDF_FILE_PREFIX + Integer.valueOf(publicationId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makePdfPictureFilename(int publicationId) {
		return FileSystem.join(getPdfRootFile(), PDF_FILE_PREFIX + Integer.valueOf(publicationId) + JPEG_FILE_EXTENSION);
	}

	@Override
	public File makeAwardFilename(int publicationId) {
		return FileSystem.join(getAwardRootFile(), AWARD_FILE_PREFIX + Integer.valueOf(publicationId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makeAwardPictureFilename(int publicationId) {
		return FileSystem.join(getAwardRootFile(), AWARD_FILE_PREFIX + Integer.valueOf(publicationId) + JPEG_FILE_EXTENSION);
	}

	@Override
	public File normalizeForServerSide(File file) {
		if (file == null) {
			return null;
		}
		if (file.isAbsolute()) {
			return file;
		}
		if (this.uploadFolder != null) {
			return FileSystem.join(this.uploadFolder, file);
		}
		return file.getAbsoluteFile();
	}

	@Override
	public void deleteDownloadablePublicationPdfFile(int id) throws Exception {
		File file = makePdfFilename(id);
		File absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
		file = makePdfPictureFilename(id);
		absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteDownloadableAwardPdfFile(int id) {
		File file = makeAwardFilename(id);
		File absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
		file = makeAwardPictureFilename(id);
		absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void saveFiles(File pdfFilename, File pictureFilename, MultipartFile multipartPdfFile) throws IOException {
		final File normalizedPdfFilename = normalizeForServerSide(pdfFilename);
		final File pdfUploadDir = normalizedPdfFilename.getParentFile();
		pdfUploadDir.mkdirs();
		try (final InputStream inputStream = multipartPdfFile.getInputStream()) {
			final Path filePath = normalizedPdfFilename.toPath();
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save PDF file: " + normalizedPdfFilename.getName(), ioe); //$NON-NLS-1$
		}
		//
		final File normalizedJpgFilename = normalizeForServerSide(pictureFilename);
		final File jpgUploadDir = normalizedJpgFilename.getParentFile();
		jpgUploadDir.mkdirs();
		try (final OutputStream outputStream = new FileOutputStream(normalizedJpgFilename)) {
			convertPdfToJpeg(normalizedPdfFilename, outputStream);
		} catch (IOException ioe) {
			throw new IOException("Could not save picture file: " + normalizedJpgFilename.getName(), ioe); //$NON-NLS-1$
		}
	}

	private static void convertPdfToJpeg(File pdfFile, OutputStream jpgStream) throws IOException {
		try (final InputStream pdfStream = new FileInputStream(pdfFile)) {
			try (final Document pdfDocument = new  Document(pdfStream)) {
				if (!pdfDocument.getPages().isEmpty()) {
					final Resolution resolution = new Resolution(300);
					// Create JpegDevice object where second argument indicates the quality of resultant image
					final JpegDevice jpegDevice = new JpegDevice(resolution, 100);
					// Convert a particular page and save the image to stream
					try (final Page page = pdfDocument.getPages().get_Item(1)) {
						jpegDevice.process(page, jpgStream);
					}
				}
			}
		}
	}

	@Override
	public void moveFiles(int sourceId, int targetId, Procedure3<String, String, String> callback) throws IOException {
		final File sourcePdfRel = makePdfFilename(sourceId);
		final File sourcePdfAbs = normalizeForServerSide(sourcePdfRel);
		if (sourcePdfAbs.exists()) {
			final File targetPdfRel = makePdfFilename(targetId);
			final File targetPdfAbs = normalizeForServerSide(targetPdfRel);
			if (targetPdfAbs.exists()) {
				Files.deleteIfExists(sourcePdfAbs.toPath());
				if (callback != null) {
					callback.apply("pdf", sourcePdfRel.toString(), null); //$NON-NLS-1$
				}
			} else {
				Files.move(sourcePdfAbs.toPath(), targetPdfAbs.toPath());
				if (callback != null) {
					callback.apply("pdf", sourcePdfRel.toString(), targetPdfRel.toString()); //$NON-NLS-1$
				}
			}
		}

		final File sourcePdfPictureRel = makePdfPictureFilename(sourceId);
		final File sourcePdfPictureAbs = normalizeForServerSide(sourcePdfPictureRel);
		if (sourcePdfPictureAbs.exists()) {
			final File targetPdfPictureRel = makePdfPictureFilename(targetId);
			final File targetPdfPictureAbs = normalizeForServerSide(targetPdfPictureRel);
			if (targetPdfPictureAbs.exists()) {
				Files.deleteIfExists(sourcePdfPictureAbs.toPath());
				if (callback != null) {
					callback.apply("pdf_picture", sourcePdfPictureRel.toString(), null); //$NON-NLS-1$
				}
			} else {
				Files.move(sourcePdfPictureAbs.toPath(), targetPdfPictureAbs.toPath());
				if (callback != null) {
					callback.apply("pdf_picture", sourcePdfPictureRel.toString(), targetPdfPictureRel.toString()); //$NON-NLS-1$
				}
			}
		}

		final File sourceAwardRel = makeAwardFilename(sourceId);
		final File sourceAwardAbs = normalizeForServerSide(sourceAwardRel);
		if (sourceAwardAbs.exists()) {
			final File targetAwardRel = makeAwardFilename(targetId);
			final File targetAwardAbs = normalizeForServerSide(targetAwardRel);
			if (targetAwardAbs.exists()) {
				Files.deleteIfExists(sourceAwardAbs.toPath());
				if (callback != null) {
					callback.apply("award", sourceAwardRel.toString(), null); //$NON-NLS-1$
				}
			} else {
				Files.move(sourceAwardAbs.toPath(), targetAwardAbs.toPath());
				if (callback != null) {
					callback.apply("award", sourceAwardRel.toString(), targetAwardRel.toString()); //$NON-NLS-1$
				}
			}
		}

		final File sourceAwardPictureRel = makeAwardPictureFilename(sourceId);
		final File sourceAwardPictureAbs = normalizeForServerSide(sourceAwardPictureRel);
		if (sourceAwardPictureAbs.exists()) {
			final File targetAwardPictureRel = makeAwardPictureFilename(targetId);
			final File targetAwardPictureAbs = normalizeForServerSide(targetAwardPictureRel);
			if (targetAwardPictureAbs.exists()) {
				Files.deleteIfExists(sourceAwardPictureAbs.toPath());
				if (callback != null) {
					callback.apply("award_picture", sourceAwardPictureRel.toString(), null); //$NON-NLS-1$
				}
			} else {
				Files.move(sourceAwardPictureAbs.toPath(), targetAwardPictureAbs.toPath());
				if (callback != null) {
					callback.apply("award_picture", sourceAwardPictureRel.toString(), targetAwardPictureRel.toString()); //$NON-NLS-1$
				}
			}
		}
	}

}
