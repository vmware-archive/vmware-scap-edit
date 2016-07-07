package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.StringQuestionTestAction;
import com.g2inc.scap.model.ocil.WhenPattern;

public class StringQuestionTestActionImpl extends QuestionTestActionImpl implements StringQuestionTestAction {
	public final static HashMap<String, Integer> ORDER_MAP = new HashMap<String, Integer>();
	static {
		ORDER_MAP.put("notes", 0);
		ORDER_MAP.put("title", 1);
		ORDER_MAP.put("when_unknown", 2);
		ORDER_MAP.put("when_not_tested", 3);
		ORDER_MAP.put("when_not_applicable", 4);
		ORDER_MAP.put("when_error", 5);
		ORDER_MAP.put("when_pattern", 6);
	}

	@Override
	public List<WhenPattern> getWhenPatternList() {
		return getApiElementList(new ArrayList<WhenPattern>(), "when_pattern", WhenPatternImpl.class);
	}

	@Override
	public void setWhenPatternList(List<WhenPattern> list) {
		replaceApiList(list, getOrderMap(), "when_pattern");
	}
	
	@Override
	public void addWhenPattern(WhenPattern when) {
		addApiElement(when, getOrderMap());
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ORDER_MAP;
	}

}
