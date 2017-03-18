package com.kamontat.constants;

import com.kamontat.object.Size;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

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
	
	/**
	 * type, DEFAULT is NON-SI
	 */
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
	 * 		the new type, can't be {@code null}
	 * @return this
	 */
	public SizeUnit setType(SizeUnitType type) {
		if (type == null) return this;
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
	
	/**
	 * get string of specify input type
	 *
	 * @param type
	 * 		input size unit type
	 * @return string of unit
	 */
	public String getString(SizeUnitType type) {
		return type == SizeUnitType.SI ? si: nonSI;
	}
	
	/**
	 * get string that must contain unit of file size and convert it to {@link SizeUnit}
	 *
	 * @param s
	 * 		string that contains unit, like this {@link com.kamontat.utilities.SizeUtil#toMinimumByte(long, SizeUnitType)} method
	 * @param type
	 * 		the type (can be null if don't know)
	 * @return SizeUnit if present; otherwise, will return {@link SizeUnit#BYTE}
	 */
	public static SizeUnit valueOf(String s, SizeUnitType type) {
		return Arrays.stream(values()).filter(sizeUnit -> {
			String textLower = s.toLowerCase(Locale.ENGLISH);
			String letterLower = textLower.chars().filter(Character::isLowerCase).mapToObj(value -> String.valueOf((char) value)).collect(Collectors.joining());
			// is short form
			boolean isAbbreviation;
			// the short must be 2 or 1 only
			if (letterLower.length() == 2 || letterLower.length() == 1) {
				isAbbreviation = letterLower.contains(sizeUnit.name().toLowerCase(Locale.ENGLISH));
				// otherwise will get first char and last char to computer
			} else {
				isAbbreviation = (letterLower.charAt(0) + "" + letterLower.charAt(letterLower.length() - 1)).contains(sizeUnit.name().toLowerCase(Locale.ENGLISH));
			}
			boolean isSi = s.toLowerCase(Locale.ENGLISH).contains(sizeUnit.getString(SizeUnitType.SI).toLowerCase(Locale.ENGLISH));
			boolean isNonSi = s.toLowerCase(Locale.ENGLISH).contains(sizeUnit.getString(SizeUnitType.NON_SI).toLowerCase(Locale.ENGLISH));
			return isAbbreviation || isSi || isNonSi;
		}).findFirst().orElse(BYTE).setType(type);
	}
	
	/**
	 * convert this unit to Byte unit ({@link #BYTE}) <br>
	 * 1 byte = 1 byte <br>
	 * Minimum: 10^0 or 2^0
	 *
	 * @param number
	 * 		the number to conversion
	 * @param decimalPoint
	 * 		the decimal to conversion, if you want to use {@link Size#SIZE_SCALE} yo can call {@link #toB(BigDecimal)} method instantly
	 * @return the size in Byte unit
	 */
	public BigDecimal toB(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, BYTE);
	}
	
	/**
	 * convert this unit to KB unit ({@link #KB}) <br>
	 * 1 KB = 1000 byte or 1024 byte <br>
	 * Minimum: 10^3 or 2^10
	 *
	 * @param number
	 * 		the number to conversion
	 * @param decimalPoint
	 * 		the decimal to conversion, if you want to use {@link Size#SIZE_SCALE} yo can call {@link #toK(BigDecimal)} method instantly
	 * @return the size in KB unit
	 */
	public BigDecimal toK(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, KB);
	}
	
	/**
	 * convert this unit to MB unit ({@link #MB}) <br>
	 * 1 MB = 1,000,000 byte or 1,048,576 byte <br>
	 * Minimum: 10^6 or 2^20
	 *
	 * @param number
	 * 		the number to conversion
	 * @param decimalPoint
	 * 		the decimal to conversion, if you want to use {@link Size#SIZE_SCALE} yo can call {@link #toM(BigDecimal)} method instantly
	 * @return the size in MB unit
	 */
	public BigDecimal toM(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, MB);
	}
	
	/**
	 * convert this unit to GB unit ({@link #GB}) <br>
	 * 1 GB = 1,000,000,000 byte or 1,073,741,824 byte <br>
	 * Minimum: 10^9 or 2^30
	 *
	 * @param number
	 * 		the number to conversion
	 * @param decimalPoint
	 * 		the decimal to conversion, if you want to use {@link Size#SIZE_SCALE} yo can call {@link #toG(BigDecimal)} method instantly
	 * @return the size in GB unit
	 */
	public BigDecimal toG(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, GB);
	}
	
	/**
	 * convert this unit to TB unit ({@link #TB}) <br>
	 * 1 TB = 1,000,000,000,000 byte or 1,099,511,627,776 byte <br>
	 * Minimum: 10^12 or 2^40
	 *
	 * @param number
	 * 		the number to conversion
	 * @param decimalPoint
	 * 		the decimal to conversion, if you want to use {@link Size#SIZE_SCALE} yo can call {@link #toT(BigDecimal)} method instantly
	 * @return the size in TB unit
	 */
	public BigDecimal toT(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, TB);
	}
	
	/**
	 * convert this unit to PB unit ({@link #PB}) <br>
	 * 1 PB = 1,000,000,000,000,000 byte or 1,125,899,906,842,624 byte <br>
	 * Minimum: 10^15 or 2^50
	 *
	 * @param number
	 * 		the number to conversion
	 * @param decimalPoint
	 * 		the decimal to conversion, if you want to use {@link Size#SIZE_SCALE} yo can call {@link #toP(BigDecimal)} method instantly
	 * @return the size in PB unit
	 */
	public BigDecimal toP(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, PB);
	}
	
	/**
	 * convert this unit to EB unit ({@link #EB}) <br>
	 * 1 EB = 1,000,000,000,000,000,000 byte or 1,152,921,504,606,846,976 byte <br>
	 * Minimum: 10^18 or 2^60
	 *
	 * @param number
	 * 		the number to conversion
	 * @param decimalPoint
	 * 		the decimal to conversion, if you want to use {@link Size#SIZE_SCALE} yo can call {@link #toE(BigDecimal)} method instantly
	 * @return the size in EB unit
	 */
	public BigDecimal toE(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, EB);
	}
	
	/**
	 * convert this unit to ZB unit ({@link #ZB}) <br>
	 * 1 ZB = 1,000,000,000,000,000,000,000 byte or 1,180,591,620,717,411,303,424 byte <br>
	 * Minimum: 10^21 or 2^70
	 *
	 * @param number
	 * 		the number to conversion
	 * @param decimalPoint
	 * 		the decimal to conversion, if you want to use {@link Size#SIZE_SCALE} yo can call {@link #toZ(BigDecimal)} method instantly
	 * @return the size in ZB unit
	 */
	public BigDecimal toZ(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, ZB);
	}
	
	/**
	 * convert this unit to YB unit ({@link #YB}) <br>
	 * 1 YB = 1,000,000,000,000,000,000,000,000 byte or 1,208,925,819,614,629,174,706,176 byte <br>
	 * Minimum: 10^24 or 2^80
	 *
	 * @param number
	 * 		the number to conversion
	 * @param decimalPoint
	 * 		the decimal to conversion, if you want to use {@link Size#SIZE_SCALE} yo can call {@link #toY(BigDecimal)} method instantly
	 * @return the size in YB unit
	 */
	public BigDecimal toY(BigDecimal number, int decimalPoint) {
		return to(number, decimalPoint, YB);
	}
	
	/**
	 * {@link #toB(BigDecimal, int)} by place int to {@link Size#SIZE_SCALE}
	 *
	 * @param number
	 * 		the conversion number
	 * @return the size that already convert
	 */
	public BigDecimal toB(BigDecimal number) {
		return toB(number, Size.SIZE_SCALE);
	}
	
	/**
	 * {@link #toK(BigDecimal, int)} by place int to {@link Size#SIZE_SCALE}
	 *
	 * @param number
	 * 		the conversion number
	 * @return the size that already convert
	 */
	public BigDecimal toK(BigDecimal number) {
		return toK(number, Size.SIZE_SCALE);
	}
	
	/**
	 * {@link #toM(BigDecimal, int)} by place int to {@link Size#SIZE_SCALE}
	 *
	 * @param number
	 * 		the conversion number
	 * @return the size that already convert
	 */
	public BigDecimal toM(BigDecimal number) {
		return toM(number, Size.SIZE_SCALE);
	}
	
	/**
	 * {@link #toG(BigDecimal, int)} by place int to {@link Size#SIZE_SCALE}
	 *
	 * @param number
	 * 		the conversion number
	 * @return the size that already convert
	 */
	public BigDecimal toG(BigDecimal number) {
		return toG(number, Size.SIZE_SCALE);
	}
	
	/**
	 * {@link #toT(BigDecimal, int)} by place int to {@link Size#SIZE_SCALE}
	 *
	 * @param number
	 * 		the conversion number
	 * @return the size that already convert
	 */
	public BigDecimal toT(BigDecimal number) {
		return toT(number, Size.SIZE_SCALE);
	}
	
	/**
	 * {@link #toP(BigDecimal, int)} by place int to {@link Size#SIZE_SCALE}
	 *
	 * @param number
	 * 		the conversion number
	 * @return the size that already convert
	 */
	public BigDecimal toP(BigDecimal number) {
		return toP(number, Size.SIZE_SCALE);
	}
	
	/**
	 * {@link #toE(BigDecimal, int)} by place int to {@link Size#SIZE_SCALE}
	 *
	 * @param number
	 * 		the conversion number
	 * @return the size that already convert
	 */
	public BigDecimal toE(BigDecimal number) {
		return toE(number, Size.SIZE_SCALE);
	}
	
	/**
	 * {@link #toZ(BigDecimal, int)} by place int to {@link Size#SIZE_SCALE}
	 *
	 * @param number
	 * 		the conversion number
	 * @return the size that already convert
	 */
	public BigDecimal toZ(BigDecimal number) {
		return toZ(number, Size.SIZE_SCALE);
	}
	
	/**
	 * {@link #toY(BigDecimal, int)} by place int to {@link Size#SIZE_SCALE}
	 *
	 * @param number
	 * 		the conversion number
	 * @return the size that already convert
	 */
	public BigDecimal toY(BigDecimal number) {
		return toY(number, Size.SIZE_SCALE);
	}
	
	/**
	 * convert this unit to any unit that you pass from the parameter <br>
	 * and return the decimal number in decimal point number <br>
	 * Example: decimalPoint=2 <br>
	 * the return value will compute until 2 number after pass dot (##.XX)
	 *
	 * @param number
	 * 		the conversion number
	 * @param decimalPoint
	 * 		the number of number after dot (###.XX)
	 * @param unit
	 * 		the unit that want to convert to
	 * @return number that already converted
	 */
	public BigDecimal to(BigDecimal number, int decimalPoint, SizeUnit unit) {
		return choose(number, unit, decimalPoint);
	}
	
	/**
	 * same with {@link #to(BigDecimal, int, SizeUnit)} but pass int as {@link Size#SIZE_SCALE}
	 *
	 * @param number
	 * 		the conversion number
	 * @param unit
	 * 		the unit that want to convert to
	 * @return number that already converted
	 */
	public BigDecimal to(BigDecimal number, SizeUnit unit) {
		return choose(number, unit, Size.SIZE_SCALE);
	}
	
	/**
	 * compute method
	 *
	 * @param number
	 * 		the original number
	 * @param unit
	 * 		the unit that want to convert to
	 * @param decimal
	 * 		the decimal to compute
	 * @return the number that already computed
	 */
	private BigDecimal choose(BigDecimal number, SizeUnit unit, int decimal) {
		if (number.scale() == 0) number = number.setScale(Size.SIZE_SCALE, Size.DEFAULT_ROUNDING_MODE);
		int pow = getPow(unit);
		BigDecimal multiply = BigDecimal.valueOf(getType().getSize()).pow(Math.abs(pow));
		if (pow < 0) {
			return number.divide(multiply, decimal, RoundingMode.HALF_EVEN);
		}
		return number.multiply(multiply);
	}
	
	/**
	 * get power to compute by unit
	 *
	 * @param unit
	 * 		convert to unit
	 * @return number of power
	 */
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
