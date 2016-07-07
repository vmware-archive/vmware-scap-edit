package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import com.g2inc.scap.model.ocil.Variable;
import com.g2inc.scap.model.ocil.Variables;

public class VariablesImpl extends ModelBaseImpl implements Variables {
	
	public final static HashMap<String, Class<?>> CLASS_MAP = new HashMap<String, Class<?>>();
	public final static HashMap<String, Integer> VARIABLES_ORDER = new HashMap<String, Integer>();
	static {
		CLASS_MAP.put("external_variable", ExternalVariableImpl.class);
		CLASS_MAP.put("constant_variable", ConstantVariableImpl.class);
		CLASS_MAP.put("local_variable", LocalVariableImpl.class);
		
		for (String key : CLASS_MAP.keySet()) {
			VARIABLES_ORDER.put(key, 0);
		}
	}
	
	@Override
	public List<Variable> getVariableList() {
		List<Variable> children = new ArrayList<Variable>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			Class<?> clazz = CLASS_MAP.get(elem.getName());
			if (clazz != null) {
				Variable variable = (Variable) createApiElement(elem, clazz);
				children.add(variable);
			}
		}
		return children;
	}
	
	@Override
	public void setVariableList(List<Variable> list) {
		for (String key : CLASS_MAP.keySet()) {
			this.element.removeChildren(key);
		}
		for (Variable variable : list) {
			insertApiChild(variable, getOrderMap(), -1);
		}
	}
	
	@Override
	public void addVariable(Variable variable) {
		insertApiChild(variable, getOrderMap(), -1);
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return VARIABLES_ORDER;
	}
	
}
