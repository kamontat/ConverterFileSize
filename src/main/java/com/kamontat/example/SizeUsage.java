package com.kamontat.example;

import com.kamontat.constants.SizeUnitType;
import com.kamontat.object.Size;
import com.kamontat.utilities.SizeUtil;

import static com.kamontat.constants.SizeUnit.BYTE;
import static com.kamontat.constants.SizeUnit.EB;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 16/Mar/2017 - 4:04 PM
 */
public class SizeUsage {
	public static void main(String[] args) {
		Size DEFAULT = SizeUtil.getSize(10, EB, SizeUnitType.NON_SI);
		Size s = DEFAULT.convertTo(BYTE).convertTo(SizeUnitType.SI).convertTo(EB).convertTo(SizeUnitType.SI);
		System.out.println(s);
	}
}
