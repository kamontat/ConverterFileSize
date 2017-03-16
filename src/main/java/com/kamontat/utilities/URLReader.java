package com.kamontat.utilities;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

// This is a decorator (wrapper) for an InputStream that you can monitor read progress.
//import javax.swing.ProgressMonitorInputStream;

/**
 * Download a URL to a File.
 *
 * @author James Brucker
 */
public class URLReader implements Runnable {
	private URL url;
	private URLConnection connection;
	private InputStream inStream;
	private int bytesRead; // number of bytes read so far
	private static final int BUFFER_SIZE = 32 * 1024; // default buffer size for read
	/**
	 * length of URL stream, lazily determined.
	 */
	private int urlSize;
	/**
	 * The output file.
	 */
	private File outfile;
	/**
	 * output file to write to
	 */
	private FileOutputStream outStream;
	
	/**
	 * Initialize a new URL Reader.
	 *
	 * @param url
	 * 		is the URL to read from
	 * @param outfile
	 * 		is a File output to write output to. If it is a writable directory
	 * 		then a file is created in that directory with same name as the download resource.
	 * @throws IOException
	 * 		if URLConnection fails,
	 * @throws FileNotFoundException
	 * 		if outfile not found or cannot be written to
	 * @precondition url is a valid URL, outfile is a valid writable file or directory.
	 */
	public URLReader(URL url, File outfile) throws IOException {
		if (url == null) throw new IllegalArgumentException("url cannot be null");
		if (outfile == null) throw new IllegalArgumentException("output file cannot be null");
		if (!outfile.exists() && !outfile.createNewFile()) {
			throw new IllegalArgumentException("output file " + outfile.getName() + " is not found");
		}
		if (!outfile.canWrite())
			throw new IllegalArgumentException("output file " + outfile.getName() + " is not writable");
		
		this.url = url;
		this.bytesRead = 0;
		if (outfile.isDirectory()) {
			// create a file in directory having same name as the URL resource name
			String filename = url.getPath();
			int k = filename.lastIndexOf("/");
			if (k >= 0) {
				if (k == filename.length() - 1) filename = "noname";
				else filename = filename.substring(k + 1); // could fail
			}
			outfile = new File(outfile, filename);
		}
		this.outfile = outfile;
		// don't open connection yet -- the server might close it before we run()
		// create output writer
		outStream = new FileOutputStream(outfile); // "rwd" mode?
	}
	
	private InputStream getInputStream() throws IOException {
		return url.openStream();
	}
	
	/**
	 * Get the number of bytes downloaded so far.
	 *
	 * @return number of bytes downloaded from URL so far.
	 */
	public int getBytesRead() {
		return bytesRead;
	}
	
	/**
	 * Read the URL connection and save bytes to output file.
	 * This method will block until the entire URL content is read.
	 * While reading, the bytesRead object attribute is regularly updated.
	 *
	 * @return the number of bytes actually read
	 */
	public int readURL() {
		bytesRead = 0;
		byte[] buff = new byte[BUFFER_SIZE];
		try {
			inStream = getInputStream();
			do {
				int n = inStream.read(buff);
				if (n < 0) break; // read returns -1 at end of input
				outStream.write(buff, 0, n);
				bytesRead += n;
			} while (true);
		} catch (IOException e) {
			System.err.println("readURL: " + e);
		} finally {
			if (inStream != null) try {
				inStream.close();
			} catch (IOException e) { /* who cares? its not my data. */ }
			try {
				outStream.close();
			} catch (IOException e) { /* ignore it */ }
		}
		return bytesRead;
	}
	
	/**
	 * Get the size in bytes of the URL to download.
	 *
	 * @return the size in bytes of the URL to download, -1 if cannot determine.
	 */
	public int getSize() {
		if (urlSize > 0) return urlSize;
		if (url == null) return 0;
		try {
			URLConnection connection = url.openConnection();
			urlSize = connection.getContentLength();
		} catch (java.io.IOException e) {
			urlSize = -1;
		}
		return urlSize;
	}
	
	/**
	 * Get actual name of the output file.
	 *
	 * @return string name with path of the output file
	 */
	public String getOutputFile() {
		return outfile.getPath();
	}
	
	/**
	 * start the URL reader. On completion the output stream is closed.
	 */
	public void run() {
		readURL();
	}
}
