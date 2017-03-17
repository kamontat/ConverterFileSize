package com.kamontat.utilities;

import com.kamontat.constants.SizeUnit;
import com.kamontat.constants.SizeUnitType;
import com.kamontat.object.Size;

import java.math.BigDecimal;

import static com.kamontat.object.Size.DEFAULT_ROUNDING_MODE;
import static com.kamontat.object.Size.SIZE_SCALE;

/**
 * @author kamontat
 * @version 1.0
 * @since Wed 15/Mar/2017 - 5:41 PM
 */
public class SizeUtil {
	
	/**
	 * change byte to minimum size <br>
	 * from: <a href="http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java">stackoverflow</a>
	 *
	 * @param bytes
	 * 		number of bytes
	 * @param si
	 * 		is si unit
	 * @return string format %.1f
	 */
	public static String humanReadableByteCount(long bytes, boolean si) {
		if (bytes < 0) throw new IllegalArgumentException("input bytes must be positive number.");
		int unit = si ? 1000: 1024;
		if (bytes < unit) return bytes + " BYTE";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE": "KMGTPE").charAt(exp - 1) + (si ? "": "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	public static Size getSize(long size, SizeUnit unit, SizeUnitType type) {
		return new Size(BigDecimal.valueOf(size).setScale(SIZE_SCALE, DEFAULT_ROUNDING_MODE), unit, type);
	}
}
