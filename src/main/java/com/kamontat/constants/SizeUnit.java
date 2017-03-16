package com.kamontat.constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 1DEFAULT_SIZE/Mar/2017 - 12:02 AM
 */
public enum SizeUnit {
	/**
	 * <b>Byte</b> or <b>Byte</b>
	 */
	B("Byte", "Byte", 1),
	/**
	 * <b>Kilobyte</b> or <b>Kibibyte</b>
	 */
	K("Kilobyte", "Kibibyte", 2),
	/**
	 * <b>Megabyte</b> or <b>Mebibyte</b>
	 */
	M("Megabyte", "Mebibyte", 3),
	/**
	 * <b>Gigabyte</b> or <b>Gibibyte</b>
	 */
	G("Gigabyte", "Gibibyte", 4),
	/**
	 * <b>Terabyte</b> or <b>Tebibyte</b>
	 */
	T("Terabyte", "Tebibyte", 5),
	/**
	 * <b>Petabyte</b> or <b>Pebibyte</b>
	 */
	P("Petabyte", "Pebibyte", 6),
	/**
	 * <b>Exabyte</b> or <b>Exbibyte</b>
	 */
	E("Exabyte", "Exbibyte", 7),
	/**
	 * <b>Zettabyte</b> or <b>Zebibyte</b>
	 */
	Z("Zettabyte", "Zebibyte", 8),
	/**
	 * <b>Yottabyte</b> or <b>Yobibyte</b>
	 */
	Y("Yottabyte", "Yobibyte", 9);
	
	private static final int DEFAULT_SIZE = 6;
	
	private SizeUnitType type = SizeUnitType.NON_SI;
	private String nonSI;
	private String si;
	private int multiply;
	
	SizeUnit(String si, String non, int multiply) {
		this.si = si;
		nonSI = non;
		this.multiply = multiply;
	}
	
	public SizeUnit type(SizeUnitType type) {
		this.type = type;
		return this;
	}
	
	protected SizeUnitType getType() {
		return type;
	}
	
	protected int getPow() {
		return multiply;
	}
	
	public BigDecimal toB(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, B);
	}
	
	public BigDecimal toK(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, K);
	}
	
	public BigDecimal toM(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, M);
	}
	
	public BigDecimal toT(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, T);
	}
	
	public BigDecimal toP(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, P);
	}
	
	public BigDecimal toE(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, E);
	}
	
	public BigDecimal toZ(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, Z);
	}
	
	public BigDecimal toY(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, Y);
	}
	
	public BigDecimal toB(BigDecimal number) {
		return toB(number, DEFAULT_SIZE);
	}
	
	public BigDecimal toK(BigDecimal number) {
		return toK(number, DEFAULT_SIZE);
	}
	
	public BigDecimal toM(BigDecimal number) {
		return toM(number, DEFAULT_SIZE);
	}
	
	public BigDecimal toT(BigDecimal number) {
		return toT(number, DEFAULT_SIZE);
	}
	
	public BigDecimal toP(BigDecimal number) {
		return toP(number, DEFAULT_SIZE);
	}
	
	public BigDecimal toE(BigDecimal number) {
		return toE(number, DEFAULT_SIZE);
	}
	
	public BigDecimal toZ(BigDecimal number) {
		return toZ(number, DEFAULT_SIZE);
	}
	
	public BigDecimal toY(BigDecimal number) {
		return toY(number, DEFAULT_SIZE);
	}
	
	public BigDecimal to(BigDecimal number, int decimalPoint, SizeUnit unit) {
		int pow = getPow(unit);
		return choose(number, pow, decimalPoint);
	}
	
	public BigDecimal to(BigDecimal number, SizeUnit unit) {
		int pow = getPow(unit);
		return choose(number, pow, DEFAULT_SIZE);
	}
	
	private BigDecimal choose(BigDecimal number, int pow, int decimal) {
		BigDecimal multiply = BigDecimal.valueOf(getType().getSize()).pow(Math.abs(pow));
		if (pow < 0) {
			return number.divide(multiply, decimal, RoundingMode.HALF_EVEN);
		}
		return number.multiply(multiply);
	}
	
	private int getPow(SizeUnit unit) {
		return this.getPow() - unit.getPow();
	}
	
	public static void main(String[] args) {
		BigDecimal decimal = SizeUnit.P.toE(new BigDecimal("1"));
		System.out.println(decimal);
	}
}
