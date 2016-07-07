package com.g2inc.scap.library.domain.ocil;

import java.util.HashMap;

import com.g2inc.scap.model.ocil.BooleanQuestionTestAction;
import com.g2inc.scap.model.ocil.ResultChoice;

public class BooleanQuestionTestActionImpl extends QuestionTestActionImpl implements BooleanQuestionTestAction {
	
	public final static HashMap<String, Integer> BOOLEAN_QUESTION_TEST_ACTION_ORDER = new HashMap<String, Integer>();
	static {
		BOOLEAN_QUESTION_TEST_ACTION_ORDER.put("notes", 0);
		BOOLEAN_QUESTION_TEST_ACTION_ORDER.put("title", 1);
		BOOLEAN_QUESTION_TEST_ACTION_ORDER.put("when_unknown", 2);
		BOOLEAN_QUESTION_TEST_ACTION_ORDER.put("when_not_tested", 3);
		BOOLEAN_QUESTION_TEST_ACTION_ORDER.put("when_not_applicable", 4);
		BOOLEAN_QUESTION_TEST_ACTION_ORDER.put("when_error", 5);
		BOOLEAN_QUESTION_TEST_ACTION_ORDER.put("when_true", 6);
		BOOLEAN_QUESTION_TEST_ACTION_ORDER.put("when_false", 7);
	}

	@Override
	public ResultChoice getWhenTrue() {
		return getApiElement("when_true", ResultChoiceImpl.class);
	}

	@Override
	public void setWhenTrue(ResultChoice type) {
		setApiElement(type, getOrderMap(), "when_true");
	}

	@Override
	public ResultChoice getWhenFalse() {
		return getApiElement("when_false", ResultChoiceImpl.class);
	}

	@Override
	public void setWhenFalse(ResultChoice type) {
		setApiElement(type, getOrderMap(), "when_false");
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return BOOLEAN_QUESTION_TEST_ACTION_ORDER;
	}

}
