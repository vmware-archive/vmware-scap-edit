package com.g2inc.scap.library.domain.ocil;

import java.util.HashMap;

import com.g2inc.scap.model.ocil.VarSetWhenCondition;

public class VarSetWhenConditionImpl extends ModelBaseImpl implements VarSetWhenCondition {
	
	public final static HashMap<String, Integer> SET_WHEN_COND = new HashMap<String, Integer>();
	static {
		SET_WHEN_COND.put("value", 0);
	}

	@Override
	public String getValue() {
		return this.getChildStringValue("value");
	}

	@Override
	public void setValue(String value) {
		this.setStringValueChild("value", value, getOrderMap());
	}

}
