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
	
	public static void unZip(String zipFile, String outputFolder) {
		unZip(zipFile, outputFolder, pathname -> true);
	}
	
	public static void unZip(String zipFile, String outputFolder, FileFilter filter) {
		cleanBuffer();
		
		try {
			//create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) folder.mkdirs();
			
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
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
	}
	
	// TODO 3/20/2017 AD 1:53 AM NO TEST YET
	public void zip(String zipFile, String inputFolder) {
		cleanBuffer();
		
		try {
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			System.out.println("Output to Zip : " + zipFile);
			
			List<String> all = FilesUtil.getAllFileNames(inputFolder, true);
			
			for (String file : all) {
				
				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);
				
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
	}
	
	private static void cleanBuffer() {
		buffer = new byte[SIZE];
	}
}
