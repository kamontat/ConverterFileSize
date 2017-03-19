package com.kamontat.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	
	/**
	 * Unzip it
	 *
	 * @param zipFile
	 * 		input zip file
	 * @param outputFolder
	 * 		zip file output folder
	 */
	public static void unZip(String zipFile, String outputFolder) {
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
				} else {
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
	
	
	
	
	private static void cleanBuffer() {
		buffer = new byte[SIZE];
	}
}
