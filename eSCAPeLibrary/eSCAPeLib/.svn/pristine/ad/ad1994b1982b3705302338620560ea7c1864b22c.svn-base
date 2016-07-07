package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.Range;
import com.g2inc.scap.model.ocil.WhenRange;

public class WhenRangeImpl extends ResultChoiceImpl implements WhenRange {
	
	public final static HashMap<String, Integer> WHEN_RANGE_ORDER = new HashMap<String, Integer>();
	static {
		WHEN_RANGE_ORDER.put("result", 0);
		WHEN_RANGE_ORDER.put("test_action_ref", 0);
		WHEN_RANGE_ORDER.put("artifacts", 1);
		WHEN_RANGE_ORDER.put("range", 2);
	}

	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return WHEN_RANGE_ORDER;
	}

	@Override
	public List<Range> getRangeList() {
		return getApiElementList(new ArrayList<Range>(), "range", RangeImpl.class);
	}

	@Override
	public void setRangeList(List<Range> ranges) {
		replaceApiList(ranges, getOrderMap(), "range");		
	}

	@Override
	public void addRange(Range range) {
		addApiElement(range, getOrderMap());
	}

}
