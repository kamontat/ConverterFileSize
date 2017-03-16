package com.kamontat.object;

import com.kamontat.constants.SizeUnit;
import com.kamontat.constants.SizeUnitType;

import java.math.BigDecimal;

/**
 * @author kamontat
 * @version 1.0
 * @since Thu 16/Mar/2017 - 12:22 AM
 */
public class Size {
	private BigDecimal size;
	private SizeUnit unit;
	private SizeUnitType type;
	
	public Size(BigDecimal size, SizeUnit unit, SizeUnitType type) {
		this.size = size;
		this.unit = unit;
		this.type = type;
	}
	
	public BigDecimal getSize() {
		return size;
	}
	
	public SizeUnit getUnit() {
		return unit;
	}
	
	public SizeUnitType getType() {
		return type;
	}
	
	public Size convertTo(SizeUnit unit) {
		BigDecimal size = this.unit.to(this.size, unit);
		return new Size(size, unit, type);
	}
}
