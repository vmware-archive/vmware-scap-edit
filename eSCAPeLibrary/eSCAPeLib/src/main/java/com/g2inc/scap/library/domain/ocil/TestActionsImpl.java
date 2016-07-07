package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import com.g2inc.scap.model.ocil.TestAction;
import com.g2inc.scap.model.ocil.TestActions;

public class TestActionsImpl extends ModelBaseImpl implements TestActions {
	
	public final static HashMap<String, Class<?>> CLASS_MAP = new HashMap<String, Class<?>>();
	public final static HashMap<String, Integer> ORDER_MAP = new HashMap<String, Integer>();
	static {
		CLASS_MAP.put("boolean_question_test_action", BooleanQuestionTestActionImpl.class);
		CLASS_MAP.put("choice_question_test_action", ChoiceQuestionTestActionImpl.class);
		CLASS_MAP.put("numeric_question_test_action", NumericQuestionTestActionImpl.class);
		CLASS_MAP.put("string_question_test_action", StringQuestionTestActionImpl.class);
		CLASS_MAP.put("questionnaire", QuestionnaireImpl.class);
		
		for (String key : CLASS_MAP.keySet()) {
			ORDER_MAP.put(key, 0);
		}
	}
	
	@Override
	public List<TestAction> getTestActionList() {
		List<TestAction> children = new ArrayList<TestAction>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			Class<?> clazz = CLASS_MAP.get(elem.getName());
			if (clazz != null) {
				TestAction testAction = (TestAction) createApiElement(elem, clazz);
				children.add(testAction);
			}
		}
		return children;
	}
	
	@Override
	public void setTestActionList(List<TestAction> list) {
		for (String key : CLASS_MAP.keySet()) {
			this.element.removeChildren(key);
		}
		for (TestAction testAction : list) {
			insertApiChild(testAction, getOrderMap(), -1);
		}
	}
	
	
	@Override
	public void addTestAction(TestAction testAction) {
		addApiElement(testAction, getOrderMap());
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ORDER_MAP;
	}
	
}
