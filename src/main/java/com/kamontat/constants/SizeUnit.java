package com.kamontat.constants;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 16/Mar/2017 - 12:02 AM
 */
public enum SizeUnit {
	/**
	 * <b>Kilobyte</b> or <b>Kibibyte</b>
	 */
	K("Kilobyte", "Kibibyte"),
	/**
	 * <b>Megabyte</b> or <b>Mebibyte</b>
	 */
	M("Megabyte", "Mebibyte"),
	/**
	 * <b>Gigabyte</b> or <b>Gibibyte</b>
	 */
	G("Gigabyte", "Gibibyte"),
	/**
	 * <b>Terabyte</b> or <b>Tebibyte</b>
	 */
	T("Terabyte", "Tebibyte"),
	/**
	 * <b>Petabyte</b> or <b>Pebibyte</b>
	 */
	P("Petabyte", "Pebibyte"),
	/**
	 * <b>Exabyte</b> or <b>Exbibyte</b>
	 */
	E("Exabyte", "Exbibyte"),
	/**
	 * <b>Zettabyte</b> or <b>Zebibyte</b>
	 */
	Z("Zettabyte", "Zebibyte"),
	/**
	 * <b>Yottabyte</b> or <b>Yobibyte</b>
	 */
	Y("Yottabyte", "Yobibyte");
	
	private String nonSI;
	private String si;
	
	SizeUnit(String si, String non) {
		this.si = si;
		nonSI = non;
	}
	
	public String toString(SizeUnitType type) {
		if (type == SizeUnitType.SI) return si;
		return nonSI;
	}
}
