package com.g2inc.scap.library.domain.ocil;

import com.g2inc.scap.model.ocil.Pattern;
import com.g2inc.scap.model.ocil.Variable;

public class PatternImpl extends ModelBaseImpl implements Pattern {
	
	@Override
	public Variable getVariable() {
		Variable var = null;
		String varRef = element.getAttributeValue("var_ref");
		if (varRef != null) {
			var = getOcilDocument().getVariable(varRef);
		}
		return var;
	}
	
	@Override
	public void setVariable(Variable var) {
		setOptionalAttribute("var_ref", var.getId());
	}
	
	@Override
	public String getVariableRef() {
		return element.getAttributeValue("var_ref");
	}
	
	@Override
	public void setVariableRef(String varRefId) {
		setOptionalAttribute("var_ref", varRefId);
	}

	@Override
	public String getValue() {
		return element.getText();
	}

	@Override
	public void setValue(String value) {
		element.setText(value);
	}	

}
