package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.NumericQuestionTestAction;
import com.g2inc.scap.model.ocil.WhenEquals;
import com.g2inc.scap.model.ocil.WhenRange;

public class NumericQuestionTestActionImpl extends QuestionTestActionImpl implements NumericQuestionTestAction {
	
	public final static HashMap<String, Integer> ORDER_MAP = new HashMap<String, Integer>();
	static {
		ORDER_MAP.put("notes", 0);
		ORDER_MAP.put("title", 1);
		ORDER_MAP.put("when_unknown", 2);
		ORDER_MAP.put("when_not_tested", 3);
		ORDER_MAP.put("when_not_applicable", 4);
		ORDER_MAP.put("when_error", 5);
		ORDER_MAP.put("when_equals", 6);
		ORDER_MAP.put("when_range", 7);
	}

	@Override
	public List<WhenEquals> getWhenEqualsList() {
		return getApiElementList(new ArrayList<WhenEquals>(), "when_equals", WhenEqualsImpl.class);
	}

	@Override
	public void setWhenEqualsList(List<WhenEquals> list) {
		replaceApiList(list, getOrderMap(), "when_equals");
	}

	@Override
	public List<WhenRange> getWhenRangeList() {
		return getApiElementList(new ArrayList<WhenRange>(), "when_range", WhenRangeImpl.class);
	}

	@Override
	public void setWhenRangeList(List<WhenRange> list) {
		replaceApiList(list, getOrderMap(), "when_range");		
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ORDER_MAP;
	}

	@Override
	public void addWhenEquals(WhenEquals whenEquals) {
		addApiElement(whenEquals, getOrderMap());
	}

	@Override
	public void addWhenRange(WhenRange whenRange) {
		addApiElement(whenRange, getOrderMap());
	}

}
