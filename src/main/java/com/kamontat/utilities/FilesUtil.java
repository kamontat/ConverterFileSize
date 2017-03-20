package com.kamontat.utilities;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Files Utility
 *
 * @author robin
 * @author refactor by Sevan Joe
 * @author add more by kamontat chantrachirathumrong
 */
public class FilesUtil {
	public static String DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * get path start with <code>current project path</code>
	 *
	 * @param more
	 * 		addition string to be joined to start path
	 * @return {@link File}
	 * @see Paths#get(String, String...)
	 */
	public static File getFile(String... more) {
		return Paths.get(".", more).toAbsolutePath().normalize().toFile();
	}
	
	/**
	 * read all context in file {@link File}
	 *
	 * @param file
	 * 		reading file
	 * @return String text content
	 */
	public static String readAll(File file) {
		return readAll(file.getAbsolutePath(), DEFAULT_ENCODING);
	}
	
	/**
	 * read text file content, return string split by "\n"
	 *
	 * @param filePathAndName
	 * 		String file name with absolute path
	 * @return String text content
	 */
	public static String readAll(String filePathAndName) {
		return readAll(filePathAndName, DEFAULT_ENCODING);
	}
	
	/**
	 * read text file content, return string split by "\n"
	 *
	 * @param filePathAndName
	 * 		String file name with absolute path
	 * @param encoding
	 * 		String file encoding
	 * @return String text content
	 */
	public static String readAll(String filePathAndName, String encoding) {
		return readLine(filePathAndName, encoding).stream().reduce((s, s2) -> s.concat("\n").concat(s2)).orElse("");
	}
	
	/**
	 * read the line content of text file, in array <br>
	 * <b>PS.</b> size of element = number of line
	 *
	 * @param filePathAndName
	 * 		String file name with absolute path
	 * @param encoding
	 * 		String file encoding
	 * @return Array of string content separate by "\n" (new line)
	 */
	public static List<String> readLine(String filePathAndName, String encoding) {
		if (encoding == null) encoding = DEFAULT_ENCODING;
		String string = "";
		List<String> stringList = new ArrayList<>();
		long i = 0;
		
		BufferedReader reader = getBuffer(filePathAndName, encoding);
		if (reader == null) return stringList;
		
		try {
			String data;
			while ((data = reader.readLine()) != null) {
				stringList.add(data);
			}
			return stringList;
		} catch (Exception e) {
			return stringList;
		}
	}
	
	/**
	 * read the specified line content of text file
	 *
	 * @param filePathAndName
	 * 		String file name with absolute path
	 * @param rowIndex
	 * 		the row number that want to read
	 * @return String text content of the line
	 */
	public static String readLine(String filePathAndName, long rowIndex) {
		return readLine(filePathAndName, DEFAULT_ENCODING).get(Math.toIntExact(rowIndex));
	}
	
	/**
	 * create file
	 *
	 * @param filePathAndName
	 * 		String file path and name
	 * @param fileContent
	 * 		String file content
	 * @return boolean flag to indicate create success or not
	 */
	public static boolean newFile(String filePathAndName, String fileContent) {
		return newFile(filePathAndName, fileContent, false);
	}
	
	/**
	 * create file
	 *
	 * @param filePathAndName
	 * 		String file path and name
	 * @param fileContent
	 * 		String file content
	 * @param append
	 * 		true to append, false to create
	 * @return boolean append to indicate create success or not
	 */
	public static boolean newFile(String filePathAndName, String fileContent, boolean append) {
		try {
			File file = new File(filePathAndName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file, append);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(fileContent);
			
			printWriter.close();
			fileWriter.close();
			return true;
		} catch (Exception e) {
			System.out.println("create file failed");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * create file with specified encoding
	 *
	 * @param filePathAndName
	 * 		String file path and name
	 * @param fileContent
	 * 		String file content
	 * @param encoding
	 * 		the specified encoding, such as GBK or UTF-8
	 * @return boolean flag to indicate create success or not
	 */
	public static boolean newFile(String filePathAndName, String fileContent, String encoding) {
		try {
			File file = new File(filePathAndName);
			if (!file.exists()) {
				file.createNewFile();
			}
			PrintWriter printWriter = new PrintWriter(file, encoding);
			printWriter.println(fileContent);
			printWriter.close();
			return true;
		} catch (Exception e) {
			System.out.println("create file failed");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * delete file
	 *
	 * @param filePathAndName
	 * 		String file path and name
	 * @return true, if delete successfully
	 */
	public static boolean delFile(String filePathAndName) {
		try {
			File file = new File(filePathAndName);
			return file.delete();
		} catch (Exception e) {
			System.out.println("delete file failed");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * create folder
	 *
	 * @param folderPath
	 * 		String folder path
	 * @return true, if successful create new folder; otherwise will return false.
	 */
	public static boolean newFolder(String folderPath) {
		try {
			File myFilePath = new File(folderPath);
			if (!myFilePath.exists()) {
				return myFilePath.mkdir();
			}
			return false;
		} catch (Exception e) {
			System.out.println("create folder failed");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * delete folder
	 *
	 * @param folderPath
	 * 		String folder path
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // delete all files inside
			File file = new File(folderPath);
			file.delete(); // delete the empty folder
		} catch (Exception e) {
			System.out.println("delete folder failed");
			e.printStackTrace();
		}
	}
	
	/**
	 * delete all files inside folder
	 *
	 * @param path
	 * 		String folder path
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		if (file.getAbsolutePath().equalsIgnoreCase("/")) {
			System.out.println("this is a root directory, you cannot delete all files in it!");
			System.out.println("please change the path!");
			return;
		}
		if (file.getAbsolutePath().equalsIgnoreCase("/root")) {
			System.out.println("this is a root directory, you cannot delete all files in it!");
			System.out.println("please change the path!");
			return;
		}
		if (file.getAbsolutePath().equalsIgnoreCase("/usr") || file.getAbsolutePath().equalsIgnoreCase("/opt") || file.getAbsolutePath().equalsIgnoreCase("/bin") || file.getAbsolutePath().equalsIgnoreCase("/sbin") || file.getAbsolutePath().equalsIgnoreCase("/etc") || file.getAbsolutePath().equalsIgnoreCase("/selinux") || file.getAbsolutePath().equalsIgnoreCase("/sys") || file.getAbsolutePath().equalsIgnoreCase("/var") || file.getAbsolutePath().equalsIgnoreCase("/home") || file.getAbsolutePath().equalsIgnoreCase("/net")) {
			System.out.println("this is a root directory, you cannot delete all files in it!");
			System.out.println("please change the path!");
			return;
		}
		if (file.getAbsolutePath().equalsIgnoreCase("C://") || file.getAbsolutePath().equalsIgnoreCase("C:\\\\")) {
			System.out.println("this is a root directory, you cannot delete all files in it!");
			System.out.println("please change the path!");
			return;
		}
		String[] tempList = file.list();
		File temp;
		if (tempList == null) {
			return;
		}
		for (String aTempList : tempList) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + aTempList);
			} else {
				temp = new File(path + File.separator + aTempList);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + aTempList);// delete all files inside
				delFolder(path + "/" + aTempList);// delete the empty folder
			}
		}
	}
	
	/**
	 * copy a file
	 *
	 * @param srcPath
	 * 		String the source path (File)
	 * @param dstPath
	 * 		String the destination path (File)
	 * @param replace
	 * 		is dstPath exist, and this is true that file will be replaced with new content
	 * @return true, if copy successfully
	 */
	public static boolean copyFile(String srcPath, String dstPath, boolean replace) {
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			int byteRead;
			File srcFile = new File(srcPath);
			File dstFile = new File(dstPath);
			// have but no replace
			if (dstFile.exists() && !replace) {
				return false;
			}
			// don't have, create new one
			if (!dstFile.exists()) dstFile.createNewFile();
			
			if (srcFile.exists()) { // file exists
				inputStream = new FileInputStream(srcFile);
				fileOutputStream = new FileOutputStream(dstFile);
				byte[] buffer = new byte[1444];
				while ((byteRead = inputStream.read(buffer)) != -1) {
					fileOutputStream.write(buffer, 0, byteRead);
				}
				fileOutputStream.close();
				inputStream.close();
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println("copy file failed");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * copy a folder
	 *
	 * @param srcPath
	 * 		String the source path
	 * @param dstPath
	 * 		String the destination path
	 */
	public static void copyFolder(String srcPath, String dstPath) {
		try {
			(new File(dstPath)).mkdirs(); // if the folder does not exits, create it
			File file = new File(srcPath);
			String[] fileList = file.list();
			File tempFile;
			for (String fileName : fileList) {
				if (srcPath.endsWith(File.separator)) {
					tempFile = new File(srcPath + fileName);
				} else {
					tempFile = new File(srcPath + File.separator + fileName);
				}
				
				if (tempFile.isFile()) {
					FileInputStream fileInputStream = new FileInputStream(tempFile);
					FileOutputStream fileOutputStream = new FileOutputStream(dstPath + "/" + (tempFile.getName()));
					byte[] bytes = new byte[1024 * 5];
					int length;
					while ((length = fileInputStream.read(bytes)) != -1) {
						fileOutputStream.write(bytes, 0, length);
					}
					fileOutputStream.flush();
					fileOutputStream.close();
					fileInputStream.close();
				}
				if (tempFile.isDirectory()) { // it is a subdirectory
					copyFolder(srcPath + "/" + fileName, dstPath + "/" + fileName);
				}
			}
		} catch (Exception e) {
			System.out.println("copy folder failed");
			e.printStackTrace();
		}
	}
	
	/**
	 * move a file
	 *
	 * @param srcPath
	 * 		String the source path
	 * @param dstPath
	 * 		String the destination path
	 * @return true, if move sucessfully
	 */
	public static boolean moveFile(String srcPath, String dstPath) {
		boolean c = copyFile(srcPath, dstPath, true);
		boolean d = delFile(srcPath);
		return c && d;
	}
	
	/**
	 * move a folder
	 *
	 * @param srcPath
	 * 		String the source path
	 * @param dstPath
	 * 		String the destination path
	 */
	public static void moveFolder(String srcPath, String dstPath) {
		copyFolder(srcPath, dstPath);
		delFolder(srcPath);
	}
	
	/**
	 * create multi-level directory <br>
	 * Example use: createFolders("/", "Users", "Power", "Main");
	 *
	 * @param paths
	 * 		must be at least 1 element is root folder
	 * 		(Optional) the folder that want to created.
	 * @return String the created directory path or empty string if something error
	 */
	public static String createFolders(String... paths) {
		if (paths.length == 0) return "";
		String root = paths[0];
		String[] other = new String[paths.length - 1];
		System.arraycopy(paths, 1, other, 0, paths.length - 1);
		
		try {
			File file = new File(root);
			if (!file.exists()) file.mkdirs();
			if (!file.isDirectory()) {
				System.err.println("folderPath must be directory");
				return "";
			}
			
			String nextPath = root;
			for (String path : other) {
				if (nextPath.lastIndexOf(File.separator) != 0) {
					nextPath += File.separator;
				}
				newFolder(nextPath + path);
				nextPath = nextPath + path;
			}
			return nextPath;
		} catch (Exception e) {
			System.out.println("create multi-level directory failed");
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * check if the specified file exists
	 *
	 * @param fileName
	 * 		the name of the file to be checked
	 * @return boolean true if exits, false if not
	 */
	public static boolean isFileExist(String fileName) {
		return new File(fileName).isFile();
	}
	
	/**
	 * get all files in a folder (is isDepth is false will include folder too)
	 *
	 * @param path
	 * 		String folder path
	 * @param isDepth
	 * 		true is need to scan all subdirectories
	 * @return List of File that content all file in the folder
	 */
	public static List<File> getAllFiles(String path, boolean isDepth) {
		List<File> fileList = new ArrayList<>();
		File file = new File(path);
		File[] tempList = file.listFiles();
		
		if (tempList == null || !file.exists() || !file.isDirectory()) {
			return fileList;
		}
		
		for (File tempFile : tempList) {
			if (isDepth) {
				if (tempFile.isFile()) {
					fileList.add(tempFile);
				}
				if (tempFile.isDirectory()) {
					List<File> allFiles = getAllFiles(tempFile.getAbsolutePath(), true);
					fileList.addAll(allFiles);
				}
			} else {
				fileList.add(tempFile);
			}
		}
		return fileList;
	}
	
	/**
	 * add all files that matched with extension
	 *
	 * @param path
	 * 		folder path
	 * @param extension
	 * 		file extension
	 * @return all file matching extension
	 */
	public static List<File> getAllFiles(String path, String extension) {
		return getAllFiles(path, true).stream().filter(file -> extension.equals(getExtension(file.getName()))).collect(Collectors.toList());
	}
	
	/**
	 * get all names of file in the folder
	 *
	 * @param path
	 * 		String folder path
	 * @param isDepth
	 * 		boolean is need to scan all subdirectories
	 * @return {@link List} of the {@link String}
	 */
	public static List<String> getAllFileNames(String path, boolean isDepth) {
		List<File> fileList = getAllFiles(path, isDepth);
		return fileList.stream().map(File::getName).collect(Collectors.toList());
	}
	
	/**
	 * remove suffix of a file
	 *
	 * @param fileName
	 * 		file name
	 * @return String file name without suffix
	 */
	public static String getNameNoSuffix(String fileName) {
		return fileName.replace("." + getExtension(fileName), "");
	}
	
	/**
	 * get file extension/suffix (without dot) <br>
	 * Example: html, txt, pdf, etc.
	 *
	 * @param fileName
	 * 		file name
	 * @return extension/suffix of file
	 */
	public static String getExtension(String fileName) {
		String[] name = fileName.split("\\.");
		if (name.length < 2) return "";
		return name[name.length - 1];
	}
	
	/**
	 * get file name without extension <br>
	 * example: {@code test.txt} will return {@code test}
	 *
	 * @param fileName
	 * 		file name
	 * @return file name without extension
	 */
	public static String removeExtension(String fileName) {
		return fileName.replace("." + getExtension(fileName), "");
	}
	
	/**
	 * get file name from path
	 *
	 * @param path
	 * 		absolute file/folder path
	 * @return name of the file
	 */
	public static String getFileName(String path) {
		return new File(path).getName();
	}
	
	/**
	 * check if directory exists, if not exist, create it, return false if create failed
	 *
	 * @param path
	 * 		folder path
	 * @return boolean
	 */
	public static boolean isExist(String path) {
		if (!isExistNotCreate(path)) {
			String newPath = FilesUtil.createFolders(FilesUtil.separatePath(path));
			return !newPath.equals("");
		}
		return true;
	}
	
	/**
	 * check if directory exists
	 *
	 * @param path
	 * 		folder path
	 * @return boolean
	 */
	public static boolean isExistNotCreate(String path) {
		File f = new File(path);
		return (f.exists() && f.isDirectory());
	}
	
	/**
	 * check is path a directory
	 *
	 * @param path
	 * 		absolute folder path
	 * @return true if path is directory path
	 */
	public static boolean isDirectory(String path) {
		return new File(path).isDirectory();
	}
	
	/**
	 * copy a file from srcPath to dstPath
	 *
	 * @param fileName
	 * 		file name (File)
	 * @param srcPath
	 * 		source path (Folder)
	 * @param dstPath
	 * 		destination path (Folder)
	 * @return true, if copy successfully; otherwise return false
	 */
	public boolean copyTheFile(String fileName, String srcPath, String dstPath) {
		File dstFile = new File(dstPath);
		if (!dstFile.exists()) {
			createFolders(dstPath);
		}
		return copyFile(srcPath + File.separator + fileName, dstPath + File.separator + fileName, false);
	}
	
	/**
	 * check is that directory empty or not
	 *
	 * @param path
	 * 		absolute folder path
	 * @return true if the path is empty directory
	 */
	public static boolean isEmptyDirectory(String path) {
		if (!FilesUtil.isDirectory(path)) return false;
		
		List<String> list = FilesUtil.getAllFileNames(path, true);
		return list.size() == 0;
	}
	
	/**
	 * move a file
	 *
	 * @param fileName
	 * 		file name (File)
	 * @param srcPath
	 * 		source path (Folder)
	 * @param dstPath
	 * 		destination path (Folder)
	 * @return true, if copy successfully; otherwise return false
	 */
	public boolean moveTheFile(String fileName, String srcPath, String dstPath) {
		File dstFile = new File(dstPath);
		if (!dstFile.exists()) {
			createFolders(dstPath);
		}
		return moveFile(srcPath + File.separator + fileName, dstPath + File.separator + fileName);
	}
	
	/**
	 * separate the path to file name array <br>
	 * example: {@code /var/private/zz/yy} - [var, private, zz, yy]
	 *
	 * @param path
	 * 		file name with {@code absolute path}
	 * @return file or folder string in each element
	 */
	public static String[] separatePath(String path) {
		String[] ss = path.split(File.separator);
		return Arrays.stream(ss).filter(s -> !s.isEmpty()).toArray(String[]::new);
	}
	
	/**
	 * convert string of file path to bufferReader
	 *
	 * @param fileAndPath
	 * 		String file name with absolute path
	 * @param encoding
	 * 		(optional) can be null, Default -> {@link #DEFAULT_ENCODING}
	 * @return bufferReader of file
	 */
	private static BufferedReader getBuffer(String fileAndPath, String encoding) {
		if (encoding == null || encoding.equals("")) encoding = DEFAULT_ENCODING;
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(fileAndPath), encoding));
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			return null;
		}
	}
}