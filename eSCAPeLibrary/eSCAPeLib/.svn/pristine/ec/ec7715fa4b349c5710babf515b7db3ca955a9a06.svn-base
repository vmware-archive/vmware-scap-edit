package com.g2inc.scap.library.domain.ocil;

import com.g2inc.scap.model.ocil.VarSetWhenBoolean;

public class VarSetWhenBooleanImpl extends VarSetWhenConditionImpl implements VarSetWhenBoolean {
	
	public Boolean getBooleanValue() {
		Boolean result = null;
		String valueString = element.getAttributeValue("value");
		if (valueString != null) {
			result = new Boolean(valueString);
		}
		return result;
	}
	public void setBooleanValue(Boolean booleanValue) {
		if (booleanValue != null) {
			element.setAttribute("value", booleanValue.toString());
		}
	}

}
