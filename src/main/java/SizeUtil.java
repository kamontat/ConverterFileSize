import com.kamontat.constants.SizeUnit;
import com.kamontat.constants.SizeUnitType;
import com.kamontat.object.Size;

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
	 * @return string format %.1f %s
	 */
	public static String humanReadableByteCount(Size size, SizeUnitType toType, SizeUnit toUnit) {
		if (size.getSize() < 0) throw new IllegalArgumentException("input bytes must be positive number.");
		boolean isSI = toType == SizeUnitType.SI;
		int unit = isSI ? 1000: 1024;
		if (size.getSize() < unit) return size.getSize() + "";
		int exp = (int) (Math.log(size.getSize()) / Math.log(unit));
		return String.format("%.1f", size.getSize() / Math.pow(unit, exp));
	}
}
