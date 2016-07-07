package com.g2inc.scap.library.domain.ocil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.Variable;
import com.g2inc.scap.model.ocil.WhenEquals;

public class WhenEqualsImpl extends ResultChoiceImpl implements WhenEquals {
	
	public final static HashMap<String, Integer> WHEN_RANGE_ORDER = new HashMap<String, Integer>();
	static {
		WHEN_RANGE_ORDER.put("result", 0);
		WHEN_RANGE_ORDER.put("test_action_ref", 0);
		WHEN_RANGE_ORDER.put("artifacts", 1);
		WHEN_RANGE_ORDER.put("value", 2);
	}

	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return WHEN_RANGE_ORDER;
	}

	@Override
	public List<BigDecimal> getValueList() {
		List<String> valueStrings = getStringList("value");
		List<BigDecimal> valueList = new ArrayList<BigDecimal>();
		for (String value : valueStrings) {
			valueList.add(getBigDecimal(value, "value"));
		}
		return valueList;
	}

	@Override
	public void setValueList(List<BigDecimal> list) {
		List<String> stringValueList = new ArrayList<String>();
		for (BigDecimal decimal : list) {
			stringValueList.add(decimal.toPlainString());
		}
		replaceStringList(stringValueList, getOrderMap(), "value");
	}
	
	@Override
	public Variable getVariable() {
		Variable var = null;
		String varRef = element.getAttributeValue("var_ref");
		if (varRef != null) {
			var = ((OcilDocumentImpl) getSCAPDocument()).getVariable(varRef);
		}
		return var;
	}
	
	@Override
	public void setVariable(Variable var) {
		setOptionalAttribute("var_ref", var.getId());
	}	

}
