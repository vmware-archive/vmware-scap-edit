package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.ChoiceQuestionTestAction;
import com.g2inc.scap.model.ocil.WhenChoice;

public class ChoiceQuestionTestActionImpl extends QuestionTestActionImpl implements ChoiceQuestionTestAction {
	
	public final static HashMap<String, Integer> ORDER_MAP = new HashMap<String, Integer>();
	static {
		ORDER_MAP.put("notes", 0);
		ORDER_MAP.put("title", 1);
		ORDER_MAP.put("when_unknown", 2);
		ORDER_MAP.put("when_not_tested", 3);
		ORDER_MAP.put("when_not_applicable", 4);
		ORDER_MAP.put("when_error", 5);
		ORDER_MAP.put("when_choice", 5);
	}

	@Override
	public List<WhenChoice> getWhenChoiceList() {
		return getApiElementList(new ArrayList<WhenChoice>(), "when_choice", WhenChoiceImpl.class);
	}

	@Override
	public void setWhenChoiceList(List<WhenChoice> list) {
		replaceApiList(list, getOrderMap(), "when_choice");
	}
	
	@Override
	public void addWhenChoice(WhenChoice whenChoice) {
		insertApiChild(whenChoice, getOrderMap(), -1);
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ORDER_MAP;
	}

}
