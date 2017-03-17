package com.kamontat.object;

import com.kamontat.constants.SizeUnit;
import com.kamontat.constants.SizeUnitType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * size of file, supported both si and non-si format
 *
 * @author kamontat
 * @version 1.0
 * @since Thu 16/Mar/2017 - 12:22 AM
 */
public class Size {
	/**
	 * default scale of BigDecimal size
	 */
	public static int SIZE_SCALE = 99;
	/**
	 * default of rounding the number
	 */
	public static RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;
	
	/**
	 * size supported until 2^Integer.MAX_VALUE
	 */
	private BigDecimal size;
	/**
	 * size unit (as <b>Kilobyte</b>, <b>Megabyte</b>, etc.) <br>
	 * and inside unit already have type of it
	 *
	 * @see SizeUnit
	 */
	private SizeUnit unit;
	
	public Size(BigDecimal size, SizeUnit unit, SizeUnitType type) {
		if (size.scale() == 0) size = size.setScale(SIZE_SCALE, DEFAULT_ROUNDING_MODE);
		this.size = size;
		this.unit = unit.setType(type);
	}
	
	public BigDecimal getSize() {
		return size;
	}
	
	public SizeUnit getUnit() {
		return unit;
	}
	
	public SizeUnitType getType() {
		return unit.getType();
	}
	
	/**
	 * convert current unit to newUnit
	 *
	 * @param unit
	 * 		the new unit
	 * 		convert to unit {@link SizeUnit}
	 * @return {@link Size}
	 */
	public Size convertTo(SizeUnit unit) {
		this.size = this.unit.to(this.size, unit);
		this.unit = unit;
		return this;
	}
	
	/**
	 * convert current type to newType
	 *
	 * @param type
	 * 		the new type
	 * @return {@link Size}
	 */
	public Size convertTo(SizeUnitType type) {
		this.size = size.multiply(BigDecimal.valueOf(type.getSize())).divide(BigDecimal.valueOf(getType().getSize()), size.scale(), RoundingMode.HALF_EVEN);
		this.unit.setType(type);
		return this;
	}
	
	/**
	 * get size in string format
	 *
	 * @param decimalNumber
	 * 		number that come after dot(.)
	 * @return string of size (example: 45233.1234 ~~ 45,233.12 ,if parameter is 2)
	 */
	public String getSizeAsString(int decimalNumber) {
		String pattern = "#,##0.";
		for (int i = 0; i < decimalNumber; i++) {
			pattern += "0";
		}
		DecimalFormat df = new DecimalFormat(pattern);
		df.setRoundingMode(DEFAULT_ROUNDING_MODE);
		return df.format(size);
	}
	
	@Override
	public String toString() {
		return String.format("type=(%s) %.6f %s", getType(), size, unit.getString());
	}
}
