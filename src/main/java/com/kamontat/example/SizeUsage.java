package com.kamontat.example;

import com.kamontat.constants.SizeUnit;
import com.kamontat.object.Size;
import com.kamontat.utilities.SizeUtil;

import java.math.BigDecimal;

import static com.kamontat.constants.SizeUnit.B;
import static com.kamontat.constants.SizeUnit.EB;
import static com.kamontat.constants.SizeUnit.MB;
import static com.kamontat.constants.SizeUnitType.NON_SI;
import static com.kamontat.constants.SizeUnitType.SI;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 16/Mar/2017 - 4:04 PM
 */
public class SizeUsage {
	public static void main(String[] args) {
		// example 1 - convert the size
		Size DEFAULT = SizeUtil.getSize(10, EB);
		// EB -> B -> SI-type -> NonSI-type -> EB
		Size s = DEFAULT.convertTo(B).convertTo(SI).convertTo(NON_SI).convertTo(EB);
		System.out.println(s); // result should be `type=(NON_SI) 10.000000 Yobibyte`
		
		// example 2 - minimum the size
		String str = SizeUtil.toMinimumByte(2111231122339871324L, NON_SI);
		
		System.out.println(SizeUnit.valueOf(str, NON_SI)); // `EB`
		System.out.println(str); // `1.8 EiB`
		
		// test 1 - exceed integer value
		Size s1 = new Size(new BigDecimal("9123874691283476"), MB, NON_SI);
		System.out.println(s1.convertTo(B).getSizeInt());
	}
}
