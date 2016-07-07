package com.g2inc.scap.library.domain.ocil;

import java.util.HashMap;

import com.g2inc.scap.model.ocil.Range;
import com.g2inc.scap.model.ocil.RangeValue;

public class RangeImpl extends ModelBaseImpl implements Range {
	
	public final static HashMap<String, Integer> RANGE_ORDER = new HashMap<String, Integer>();
	static {
		RANGE_ORDER.put("min", 0);
		RANGE_ORDER.put("max", 1);
	}
	
	@Override
	public RangeValue getMin() {
		return (RangeValue) getApiElement("min", RangeValueImpl.class);
	}
	
	@Override
	public void setMin(RangeValue min) {
		setApiElement(min, getOrderMap(), "min");
	}

	@Override
	public RangeValue getMax() {
		return (RangeValue) getApiElement("max", RangeValueImpl.class);
	}
	
	@Override
	public void setMax(RangeValue max) {
		setApiElement(max, getOrderMap(), "max");
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return RANGE_ORDER;
	}
}
