package com.kamontat.example;

import com.kamontat.object.Size;
import com.kamontat.utilities.SizeUtil;

import static com.kamontat.constants.SizeUnit.BYTE;
import static com.kamontat.constants.SizeUnit.YB;
import static com.kamontat.constants.SizeUnitType.NON_SI;
import static com.kamontat.constants.SizeUnitType.SI;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 16/Mar/2017 - 4:04 PM
 */
public class SizeUsage {
	public static void main(String[] args) {
		Size DEFAULT = SizeUtil.getSize(10, YB, NON_SI);
		Size s = DEFAULT.convertTo(BYTE).convertTo(SI).convertTo(NON_SI).convertTo(YB);
		System.out.println(s);
	}
}
