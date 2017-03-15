package com.kamontat.object;

import com.kamontat.constants.SizeUnit;
import com.kamontat.constants.SizeUnitType;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 16/Mar/2017 - 12:22 AM
 */
public class Size {
	private long size;
	private SizeUnit unit;
	private SizeUnitType type;
	
	public Size(long size, SizeUnit unit, SizeUnitType type) {
		this.size = size;
		this.unit = unit;
		this.type = type;
	}
	
	public long getSize() {
		return size;
	}
	
	public SizeUnit getUnit() {
		return unit;
	}
	
	public SizeUnitType getType() {
		return type;
	}
}
