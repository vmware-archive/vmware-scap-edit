package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.Pattern;
import com.g2inc.scap.model.ocil.WhenPattern;

public class WhenPatternImpl extends ResultChoiceImpl implements WhenPattern {
	
	public final static HashMap<String, Integer> WHEN_PATTERN_ORDER = new HashMap<String, Integer>();
	static {
		WHEN_PATTERN_ORDER.put("result", 0);
		WHEN_PATTERN_ORDER.put("test_action_ref", 0);
		WHEN_PATTERN_ORDER.put("artifacts", 1);
		WHEN_PATTERN_ORDER.put("pattern", 2);
	}

	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return WHEN_PATTERN_ORDER;
	}

	@Override
	public List<Pattern> getPatternList() {
		return getApiElementList(new ArrayList<Pattern>(), "pattern", PatternImpl.class);
	}

	@Override
	public void setPatternList(List<Pattern> list) {
		replaceApiList(list, getOrderMap(), "pattern");
	}

	@Override
	public void addPattern(Pattern pattern) {
		addApiElement(pattern, getOrderMap());
	}

}
