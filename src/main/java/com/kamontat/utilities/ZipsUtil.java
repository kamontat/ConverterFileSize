package com.kamontat.utilities;

import com.kamontat.constants.SizeUnit;
import com.kamontat.constants.SizeUnitType;
import com.kamontat.object.Size;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * zip and unzip utilities
 *
 * @author kamontat
 * @version 1.0
 * @since Sun 19/Mar/2017 - 11:58 PM
 */
public class ZipsUtil {
	/**
	 * default size is 32 MB
	 */
	private static int SIZE = 32 * 1024;
	// read buffer
	private static byte[] buffer = new byte[SIZE];
	
	/**
	 * change buffer size (the default is 32 MB)
	 *
	 * @param size
	 * 		new size (request {@link SizeUnitType#NON_SI} type)
	 */
	public static void setBufferSize(Size size) {
		SIZE = size.convertTo(SizeUnit.BYTE).convertTo(SizeUnitType.NON_SI).getSize().intValue();
	}
	
	/**
	 * unzip all file in zip file to dist folder (outputFolder)
	 *
	 * @param zipFile
	 * 		input zip file
	 * @param outputFolder
	 * 		output folder
	 * @return path of output folder
	 */
	public static String unZip(String zipFile, String outputFolder) {
		return unZip(zipFile, outputFolder, pathname -> true);
	}
	
	/**
	 * unzip only file that match with {@link FileFilter} to the dist folder
	 *
	 * @param zipPath
	 * 		input zip file
	 * @param outputFolder
	 * 		output folder
	 * @param filter
	 * 		filter file
	 * @return path of output file
	 */
	public static String unZip(String zipPath, String outputFolder, FileFilter filter) {
		String rootFile = FilesUtil.getNameWithoutExtension(FilesUtil.getFileName(zipPath));
		
		cleanBuffer();
		try {
			//create output directory is not exists
			FilesUtil.isDirectoryExist(outputFolder);
			if (!FilesUtil.isDirectoryEmpty(outputFolder)) {
				outputFolder = FilesUtil.createFolders(outputFolder, rootFile);
				if (outputFolder.equals("")) return "";
			}
			
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath));
			ZipEntry ze = zis.getNextEntry();
			
			while (ze != null) {
				String fileName = ze.getName();
				File file = new File(outputFolder + File.separator + fileName);
				if (ze.isDirectory()) {
					FilesUtil.createFolders(file.getAbsolutePath());
				} else if (filter.accept(file)) {
					StringBuilder sb = new StringBuilder();
					int len;
					while ((len = zis.read(buffer)) > 0) {
						sb.append(new String(buffer, 0, len));
					}
					System.out.println("unzip file: " + file.getAbsoluteFile());
					FilesUtil.newFile(file.getAbsolutePath(), sb.toString());
				}
				ze = zis.getNextEntry();
			}
			
			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return outputFolder;
	}
	
	/**
	 * zip all file in input-folder together and save as zipPath
	 *
	 * @param zipPath
	 * 		path to zip file (no need to create before)
	 * @param inputFolder
	 * 		input folder
	 * @return path that zip locate
	 */
	public static String zip(String zipPath, String inputFolder) {
		return zip(zipPath, inputFolder, pathname -> true);
	}
	
	/**
	 * zip some file that match filter in input-folder together and save as zipPath
	 *
	 * @param zipPath
	 * 		path to zip file (no need to create before)
	 * @param inputFolder
	 * 		input folder
	 * @param filter
	 * 		file filter
	 * @return path that zip locate
	 */
	public static String zip(String zipPath, String inputFolder, FileFilter filter) {
		cleanBuffer();
		try {
			if (!FilesUtil.isDirectoryExistNotCreate(inputFolder) || FilesUtil.isDirectoryEmpty(inputFolder)) {
				System.err.println("input folder must have input file to zip");
				return "";
			}
			if (!FilesUtil.getExtension(FilesUtil.getFileName(zipPath)).equals("zip"))
				zipPath = FilesUtil.getNameWithoutExtension(zipPath).concat(".zip");
			
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath));
			
			List<String> all = FilesUtil.getAllFileAbsPath(inputFolder, true);
			for (String file : all) {
				if (filter.accept(new File(file))) {
					System.out.println("File Added : " + file);
					zos.putNextEntry(new ZipEntry(FilesUtil.getFileName(inputFolder) + File.separator + file.replace(inputFolder, "")));
					
					FileInputStream in = new FileInputStream(file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
					in.close();
				}
			}
			zos.closeEntry();
			zos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return zipPath;
	}
	
	private static void cleanBuffer() {
		buffer = new byte[SIZE];
	}
}
