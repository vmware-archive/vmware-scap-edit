package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.model.ocil.VarSetWhenChoiceRef;
import com.g2inc.scap.model.ocil.VarSetWhenCondition;
import com.g2inc.scap.model.ocil.VarSetWhenPattern;
import com.g2inc.scap.model.ocil.VarSetWhenRange;
import com.g2inc.scap.model.ocil.VariableSet;

public class VariableSetImpl extends ModelBaseImpl implements VariableSet {
	
	public final static HashMap<String, Integer> ORDER_MAP = new HashMap<String, Integer>();
	static {
		ORDER_MAP.put("when_pattern", 0);
		ORDER_MAP.put("when_choice_ref", 0);
		ORDER_MAP.put("when_range", 0);
		ORDER_MAP.put("value", 1);
	}
	
	@Override
	public List<VarSetWhenCondition> getWhenConditions() {
		List<VarSetWhenCondition> children = new ArrayList<VarSetWhenCondition>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			if (elem.getName().equals("when_pattern")) {
				VarSetWhenPattern child = createApiElement(elem, VarSetWhenPatternImpl.class);
				children.add(child);
			} else if (elem.getName().equals("when_choice")) {
				VarSetWhenChoiceRef child = createApiElement(elem, VarSetWhenChoiceRefImpl.class);
				children.add(child);
			} else if (elem.getName().equals("when_range")) {
				VarSetWhenRange child = createApiElement(elem, VarSetWhenRangeImpl.class);
				children.add(child);
			}
		}
		return children;
	}

	@Override
	public void setWhenConditions(List<VarSetWhenCondition> list) {
		element.removeChildren("when_pattern", element.getNamespace());
		element.removeChildren("when_choice_ref", element.getNamespace());
		element.removeChildren("when_range", element.getNamespace());
		for (VarSetWhenCondition whenCond : list) {
			insertChild(((SCAPElementImpl) whenCond).getElement(), getOrderMap());
		}		
	}
	
	@Override
	public void addWhenCondition(VarSetWhenCondition cond) {
		insertChild(((SCAPElementImpl) cond).getElement(), getOrderMap());
	}

	@Override
	public String getValue() {
		return getChildStringValue("value");
	}

	@Override
	public void setValue(String value) {
		setStringValueChild("value", value, getOrderMap());
	}
		
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ORDER_MAP;
	}
	
}
