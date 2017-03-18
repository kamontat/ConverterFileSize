package com.kamontat.utilities;

import com.kamontat.constants.SizeUnit;
import com.kamontat.constants.SizeUnitType;
import com.kamontat.object.Size;

import java.math.BigDecimal;
import java.util.*;

import static com.kamontat.object.Size.DEFAULT_ROUNDING_MODE;
import static com.kamontat.object.Size.SIZE_SCALE;

/**
 * easy convert file size to other.
 *
 * @author kamontat
 * @version 1.0
 * @since Wed 15/Mar/2017 - 5:41 PM
 */
public class SizeUtil {
	
	/**
	 * change byte to minimum size <br>
	 * From: <a href="http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java">stackoverflow</a>
	 * <p>
	 * <b>Beware</b>: this method call support until {@link SizeUnit#EB} only because of {@link Long#MAX_VALUE}
	 *
	 * @param bytes
	 * 		number of bytes
	 * @param type
	 * 		type of unit
	 * @return string format %.1f
	 */
	public static String toMinimumByte(long bytes, SizeUnitType type) {
		if (bytes < 0) throw new IllegalArgumentException("input bytes must be positive number.");
		if (bytes < type.getSize()) return bytes + " BYTE";
		int exp = (int) (Math.log(bytes) / Math.log(type.getSize()));
		String pre = (type == SizeUnitType.SI ? "kMGTPE": "KMGTPE").charAt(exp - 1) + (type == SizeUnitType.SI ? "": "i");
		return String.format("%.1f %sB", bytes / Math.pow(type.getSize(), exp), pre);
	}
	
	/**
	 * get file size
	 *
	 * @param size
	 * 		file size
	 * @param unit
	 * 		the size unit
	 * @param type
	 * 		the type of unit
	 * @return {@link Size}
	 * @throws NullPointerException
	 * 		if one of parameter will null
	 * @see SizeUnit
	 * @see SizeUnitType
	 * @see Size
	 */
	public static Size getSize(long size, SizeUnit unit, SizeUnitType type) {
		Objects.requireNonNull(size);
		Objects.requireNonNull(unit);
		Objects.requireNonNull(type);
		
		return new Size(BigDecimal.valueOf(size).setScale(SIZE_SCALE, DEFAULT_ROUNDING_MODE), unit, type);
	}
}
