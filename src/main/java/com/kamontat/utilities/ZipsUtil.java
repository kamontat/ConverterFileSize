package com.kamontat.utilities;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author kamontat
 * @version 1.0
 * @since Sun 19/Mar/2017 - 11:58 PM
 */
public class ZipsUtil {
	// 32 MB
	private static int SIZE = 32 * 1024;
	// read buffer
	private static byte[] buffer = new byte[SIZE];
	
	public static String unZip(String zipFile, String outputFolder) {
		return unZip(zipFile, outputFolder, pathname -> true);
	}
	
	public static String unZip(String zipPath, String outputFolder, FileFilter filter) {
		String rootFile = FilesUtil.removeExtension(FilesUtil.getFileName(zipPath));
		
		cleanBuffer();
		try {
			//create output directory is not exists
			FilesUtil.isExist(outputFolder);
			if (!FilesUtil.isEmptyDirectory(outputFolder)) {
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
	
	public void zip(String zipPath, String inputFolder) {
		cleanBuffer();
		try {
			if (!FilesUtil.isExistNotCreate(inputFolder) || FilesUtil.isEmptyDirectory(inputFolder)) {
				System.err.println("input folder must have input file to zip");
				return;
			}
			
			if (!FilesUtil.isExist(zipPath)) {
				System.err.println("zip path not exist and cannot created");
				return;
			}
			
			if (!FilesUtil.isEmptyDirectory(zipPath)) {
				zipPath = FilesUtil.createFolders(zipPath, FilesUtil.getFileName(inputFolder));
				if (zipPath.equals("")) return;
			}
			
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath));
			
			List<String> all = FilesUtil.getAllFileNames(inputFolder, true);
			for (String file : all) {
				System.out.println("File Added : " + file);
				zos.putNextEntry(new ZipEntry(file));
				
				FileInputStream in = new FileInputStream(inputFolder + File.separator + file);
				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				
				in.close();
			}
			zos.closeEntry();
			zos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		System.out.println("Output to Zip : " + zipPath);
	}
	
	private static void cleanBuffer() {
		buffer = new byte[SIZE];
	}
}
