package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import com.g2inc.scap.model.ocil.Choice;
import com.g2inc.scap.model.ocil.ChoiceRef;
import com.g2inc.scap.model.ocil.Range;
import com.g2inc.scap.model.ocil.WhenChoice;

public class WhenChoiceImpl extends ResultChoiceImpl implements WhenChoice {
	
	public final static HashMap<String, Integer> WHEN_CHOICE_ORDER = new HashMap<String, Integer>();
	static {
		WHEN_CHOICE_ORDER.put("result", 0);
		WHEN_CHOICE_ORDER.put("test_action_ref", 0);
		WHEN_CHOICE_ORDER.put("artifacts", 1);
		WHEN_CHOICE_ORDER.put("choice_ref", 1);
	}

	@Override
	public List<ChoiceRef> getChoiceRefList() {
		return getApiElementList(new ArrayList<ChoiceRef>(), "choice_ref", ChoiceRefImpl.class);
	}

	@Override
	public void setChoiceRefList(List<ChoiceRef> list) {
		replaceApiList(list, getOrderMap(), "choice_ref");		
	}
	
	@Override
	public void addChoiceRef(ChoiceRef choiceRef) {
		addApiElement(choiceRef, getOrderMap());
	}

	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return WHEN_CHOICE_ORDER;
	}

}
