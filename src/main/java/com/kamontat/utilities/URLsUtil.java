package com.kamontat.utilities;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * URL Utility
 *
 * @author kamontat
 * @version 1.0
 * @since Wed 08/Mar/2017 - 10:30 PM
 */
public class URLsUtil {
	/**
	 * protocol for website link
	 */
	public enum Protocol {
		HTTP,
		HTTPS;
		
		/**
		 * get protocol from link
		 *
		 * @param url
		 * 		link
		 * @return protocol
		 */
		public static Protocol getProtocol(URL url) {
			if (url == null) return null;
			return url.getProtocol().contains(HTTPS.name().toLowerCase()) ? HTTPS: HTTP;
		}
		
		@Override
		public String toString() {
			return this.name().toLowerCase(Locale.ENGLISH);
		}
	}
	
	/**
	 * web protocol
	 */
	private Protocol protocol;
	/**
	 * web url/link
	 */
	private URL url;
	
	/**
	 * get url utility
	 *
	 * @param protocol
	 * 		web protocol
	 * @param link
	 * 		web link (without <b>http://</b> or <b>https://</b>)
	 * @return url utility
	 * @see URLsUtil
	 */
	public static URLsUtil getUrl(Protocol protocol, String link) {
		return new URLsUtil(protocol, link);
	}
	
	/**
	 * get url utility
	 *
	 * @param url
	 * 		the url link
	 * @return url utility
	 * @see URLsUtil
	 */
	public static URLsUtil getUrl(URL url) {
		return new URLsUtil(url);
	}
	
	/**
	 * get url utility
	 *
	 * @param link
	 * 		the link
	 * @return url utility
	 * @see URLsUtil
	 */
	public static URLsUtil getUrl(String link) {
		URL url = null;
		try {
			url = new URL(link);
		} catch (MalformedURLException ignore) {
		}
		return new URLsUtil(url);
	}
	
	/**
	 * Constructor Private
	 *
	 * @param protocol
	 * 		web protocol
	 * @param link
	 * 		web link
	 */
	private URLsUtil(Protocol protocol, String link) {
		this.protocol = protocol;
		// add protocol
		link = (protocol == Protocol.HTTP ? "http://": "https://") + link;
		
		try {
			url = new URL(link);
		} catch (MalformedURLException ignore) {
		}
	}
	
	/**
	 * Constructor Private
	 *
	 * @param url
	 * 		web url
	 */
	private URLsUtil(URL url) {
		this.protocol = Protocol.getProtocol(url);
		this.url = url;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public Protocol getProtocol() {
		return protocol;
	}
	
	/**
	 * get https connection (For <b>HTTPS</b> protocol ONLY)
	 *
	 * @return {@link HttpsURLConnection}
	 */
	public HttpsURLConnection getHttpsConnection() {
		if (checkNull()) return null;
		
		try {
			return (HttpsURLConnection) url.openConnection();
		} catch (ClassCastException | IOException e) {
			return null;
		}
	}
	
	/**
	 * get http connection (For <b>HTTP</b> protocol ONLY)
	 *
	 * @return {@link HttpURLConnection}
	 */
	public HttpURLConnection getHttpConnection() {
		if (checkNull()) return null;
		
		try {
			return (HttpURLConnection) url.openConnection();
		} catch (ClassCastException | IOException e) {
			return null;
		}
	}
	
	/**
	 * get connection
	 *
	 * @return url connection
	 * @see URL#openConnection()
	 */
	public URLConnection getConnection() {
		if (checkNull()) return null;
		try {
			return url.openConnection();
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * get all content inside url
	 *
	 * @return content
	 */
	public String getContent() {
		StringBuilder builder = new StringBuilder();
		if (checkNull()) return builder.toString();
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getConnection().getInputStream()));
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				builder.append(line).append("\n");
			}
		} catch (IOException ignored) {
		}
		
		return builder.toString();
	}
	
	/**
	 * get content length/size
	 *
	 * @return size of content
	 */
	public int getURLSize() {
		if (checkNull()) return 0;
		return getConnection().getContentLength();
	}
	
	/**
	 * get file name <br>
	 * Example: https://xyz.com/re/as/fe/test.txt <br>
	 * the return will be "test.txt"
	 *
	 * @return file name
	 */
	public String getURLFilename() {
		String filename = url.getPath();
		int k = filename.lastIndexOf("/");
		if (k == filename.length() - 1) return "";
		if (k >= 0) filename = filename.substring(k + 1);
		return filename;
	}
	
	/**
	 * print all header to (DEBUG TOOL)
	 */
	public void printHeader() {
		Map header = getConnection().getHeaderFields();
		for (Object key : header.keySet()) {
			System.out.printf("%s: %s\n", key, header.get(key));
		}
	}
	
	private boolean checkNull() {
		return url == null;
	}
}
