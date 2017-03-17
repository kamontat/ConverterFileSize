package com.kamontat.constants;

import com.kamontat.object.Size;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * contains the unit of file size and type of that unit <br>
 * The type we have 2 type <br>
 * <ol>
 * <li>SI - {@link SizeUnitType#SI}</li>
 * <li>Non-SI - {@link SizeUnitType#NON_SI}</li>
 * </ol>
 *
 * @author kamontat
 * @version 1.0
 * @see SizeUnitType
 * @since Thu 16/Mar/2017 - 12:02 AM
 */
public enum SizeUnit {
	/**
	 * <b>Byte</b> or <b>Byte</b>
	 */
	BYTE("Byte", "Byte", 1),
	/**
	 * <b>Kilobyte</b> or <b>Kibibyte</b>
	 */
	KB("Kilobyte", "Kibibyte", 2),
	/**
	 * <b>Megabyte</b> or <b>Mebibyte</b>
	 */
	MB("Megabyte", "Mebibyte", 3),
	/**
	 * <b>Gigabyte</b> or <b>Gibibyte</b>
	 */
	GB("Gigabyte", "Gibibyte", 4),
	/**
	 * <b>Terabyte</b> or <b>Tebibyte</b>
	 */
	TB("Terabyte", "Tebibyte", 5),
	/**
	 * <b>Petabyte</b> or <b>Pebibyte</b>
	 */
	PB("Petabyte", "Pebibyte", 6),
	/**
	 * <b>Exabyte</b> or <b>Exbibyte</b>
	 */
	EB("Exabyte", "Exbibyte", 7),
	/**
	 * <b>Zettabyte</b> or <b>Zebibyte</b>
	 */
	ZB("Zettabyte", "Zebibyte", 8),
	/**
	 * <b>Yottabyte</b> or <b>Yobibyte</b>
	 */
	YB("Yottabyte", "Yobibyte", 9);
	
	private SizeUnitType type = SizeUnitType.NON_SI;
	private String nonSI;
	private String si;
	private int multiply;
	
	SizeUnit(String si, String non, int multiply) {
		this.si = si;
		nonSI = non;
		this.multiply = multiply;
	}
	
	/**
	 * set new type and return this
	 *
	 * @param type
	 * 		the new type
	 * @return this
	 */
	public SizeUnit setType(SizeUnitType type) {
		this.type = type;
		return this;
	}
	
	public SizeUnitType getType() {
		return type;
	}
	
	/**
	 * get unit, by type that insert
	 *
	 * @return string of unit
	 */
	public String getString() {
		return type == SizeUnitType.SI ? si: nonSI;
	}
	
	public BigDecimal toB(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, BYTE);
	}
	
	public BigDecimal toK(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, KB);
	}
	
	public BigDecimal toM(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, MB);
	}
	
	public BigDecimal toT(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, TB);
	}
	
	public BigDecimal toP(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, PB);
	}
	
	public BigDecimal toE(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, EB);
	}
	
	public BigDecimal toZ(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, ZB);
	}
	
	public BigDecimal toY(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, YB);
	}
	
	public BigDecimal toB(BigDecimal number) {
		return toB(number, Size.SIZE_SCALE);
	}
	
	public BigDecimal toK(BigDecimal number) {
		return toK(number, Size.SIZE_SCALE);
	}
	
	public BigDecimal toM(BigDecimal number) {
		return toM(number, Size.SIZE_SCALE);
	}
	
	public BigDecimal toT(BigDecimal number) {
		return toT(number, Size.SIZE_SCALE);
	}
	
	public BigDecimal toP(BigDecimal number) {
		return toP(number, Size.SIZE_SCALE);
	}
	
	public BigDecimal toE(BigDecimal number) {
		return toE(number, Size.SIZE_SCALE);
	}
	
	public BigDecimal toZ(BigDecimal number) {
		return toZ(number, Size.SIZE_SCALE);
	}
	
	public BigDecimal toY(BigDecimal number) {
		return toY(number, Size.SIZE_SCALE);
	}
	
	public BigDecimal to(BigDecimal number, int decimalPoint, SizeUnit unit) {
		int pow = getPow(unit);
		return choose(number, pow, decimalPoint);
	}
	
	public BigDecimal to(BigDecimal number, SizeUnit unit) {
		int pow = getPow(unit);
		return choose(number, pow, Size.SIZE_SCALE);
	}
	
	private BigDecimal choose(BigDecimal number, int pow, int decimal) {
		BigDecimal multiply = BigDecimal.valueOf(getType().getSize()).pow(Math.abs(pow));
		if (pow < 0) {
			return number.divide(multiply, decimal, RoundingMode.HALF_EVEN);
		}
		return number.multiply(multiply);
	}
	
	private int getPow(SizeUnit unit) {
		return multiply - unit.multiply;
	}
	
	/**
	 * Example usage directly <br>
	 * otherwise you can use this enum by {@link com.kamontat.utilities.SizeUtil#getSize(long, SizeUnit, SizeUnitType)}
	 *
	 * @param args
	 * 		no used
	 * @see com.kamontat.utilities.SizeUtil
	 * @see com.kamontat.object.Size
	 */
	public static void main(String[] args) {
		BigDecimal decimal = SizeUnit.PB.toE(new BigDecimal("1"));
		System.out.println(decimal);
	}
}
