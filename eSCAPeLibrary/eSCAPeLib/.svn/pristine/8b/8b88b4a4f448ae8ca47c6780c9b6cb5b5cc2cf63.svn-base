package com.g2inc.scap.library.domain.ocil;

import java.math.BigDecimal;

import com.g2inc.scap.model.ocil.VarSetWhenRange;

public class VarSetWhenRangeImpl extends VarSetWhenConditionImpl implements VarSetWhenRange {
	
	public BigDecimal getMin() {
		BigDecimal result = null;
		String valueString = element.getAttributeValue("min");
		if (valueString != null) {
			result = new BigDecimal(valueString);
		}
		return result;
	}
	public void setMin(BigDecimal min) {
		if (min != null) {
			String valueString = min.toPlainString();
			element.setAttribute("min", valueString);
		}
	}
	
	public BigDecimal getMax() {
		BigDecimal result = null;
		String valueString = element.getAttributeValue("max");
		if (valueString != null) {
			result = new BigDecimal(valueString);
		}
		return result;
	}
	public void setMax(BigDecimal max) {
		if (max != null) {
			String valueString = max.toPlainString();
			element.setAttribute("max", valueString);
		}
		
	}

}
