package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.TestActionResult;
import com.g2inc.scap.model.ocil.TestActionResults;

public class TestActionResultsImpl extends ModelBaseImpl implements TestActionResults {
	
	public final static HashMap<String, Integer> TEST_ACTION_RESULTS_ORDER = new HashMap<String, Integer>();
	static {
		TEST_ACTION_RESULTS_ORDER.put("test_action_result", 0);
	}
	
	@Override
	public List<TestActionResult> getTestActionResultList() {
		return getApiElementList(new ArrayList<TestActionResult>(), "test_action_result", TestActionResultImpl.class);
	}

	@Override
	public void setTestActionResultList(List<TestActionResult> list) {
		replaceApiList(list, getOrderMap(), "test_action_result");
	}
	
	@Override
	public void addTestActionResult(TestActionResult testActionResult) {
		addApiElement(testActionResult, getOrderMap());
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return TEST_ACTION_RESULTS_ORDER;
	}
	
}
