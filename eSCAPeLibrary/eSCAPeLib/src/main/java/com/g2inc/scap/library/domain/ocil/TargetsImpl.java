package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import com.g2inc.scap.model.ocil.Target;
import com.g2inc.scap.model.ocil.Targets;

public class TargetsImpl extends ModelBaseImpl implements Targets {
	
	public final static HashMap<String, Class<?>> CLASS_MAP = new HashMap<String, Class<?>>();
	public final static HashMap<String, Integer> QUESTIONS_ORDER = new HashMap<String, Integer>();
	static {
		CLASS_MAP.put("user", UserImpl.class);
		CLASS_MAP.put("system", SystemTargetImpl.class);
		
		for (String key : CLASS_MAP.keySet()) {
			QUESTIONS_ORDER.put(key, 0);
		}
	}

	@Override
	public List<Target> getTargetList() {
		List<Target> children = new ArrayList<Target>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			Class<?> clazz = CLASS_MAP.get(elem.getName());
			if (clazz != null) {
				Target target = (Target) createApiElement(elem, clazz);
				children.add(target);
			}
		}
		return children;
	}

	@Override
	public void setTargetList(List<Target> list) {
		for (String key : CLASS_MAP.keySet()) {
			this.element.removeChildren(key);
		}
		for (Target target : list) {
			insertApiChild(target, getOrderMap(), -1);
		}
	}
	
	@Override
	public void addTarget(Target target) {
		insertApiChild(target, getOrderMap(), -1);
	}

}
