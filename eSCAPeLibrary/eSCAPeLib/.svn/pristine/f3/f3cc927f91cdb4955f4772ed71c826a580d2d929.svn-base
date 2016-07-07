package com.g2inc.scap.library.domain.ocil;

import java.util.HashMap;

import com.g2inc.scap.model.ocil.Question;
import com.g2inc.scap.model.ocil.QuestionTestAction;
import com.g2inc.scap.model.ocil.ResultChoice;
import com.g2inc.scap.model.ocil.TextType;

public class QuestionTestActionImpl extends ItemBaseImpl implements QuestionTestAction {
	public final static HashMap<String, Integer> QUESTION_TEST_ACTION_ORDER = new HashMap<String, Integer>();
	static {
		QUESTION_TEST_ACTION_ORDER.put("notes", 0);
		QUESTION_TEST_ACTION_ORDER.put("title", 1);
		QUESTION_TEST_ACTION_ORDER.put("when_unknown", 2);
		QUESTION_TEST_ACTION_ORDER.put("when_not_tested", 3);
		QUESTION_TEST_ACTION_ORDER.put("when_not_applicable", 4);
		QUESTION_TEST_ACTION_ORDER.put("when_error", 5);
	}
	
	@Override
	public TextType getTitle() {
		return getApiElement("title", TextTypeImpl.class);
	}
	
	@Override
	public void setTitle(TextType title) {
		setApiElement(title, getOrderMap(), "title");
	}
	
	@Override
	public ResultChoice getWhenUnknown() {
		return getApiElement("when_unknown", ResultChoiceImpl.class);
	}
	
	@Override
	public void setWhenUnknown(ResultChoice resultChoice) {
		setApiElement((ResultChoiceImpl)resultChoice, getOrderMap(), "when_unknown");
	}
	
	@Override
	public void setWhenNotTested(ResultChoice resultChoice) {
		setApiElement((ResultChoiceImpl)resultChoice, getOrderMap(), "when_not_tested");
	}
	
	@Override
	public ResultChoice getWhenNotTested() {
		return getApiElement("when_not_tested", ResultChoiceImpl.class);
	}	
	
	@Override
	public void setWhenNotApplicable(ResultChoice resultChoice) {
		setApiElement(resultChoice, getOrderMap(), "when_not_applicable");
	}
	
	@Override
	public ResultChoice getWhenNotApplicable() {
		return getApiElement("when_not_applicable", ResultChoiceImpl.class);
	}
	
	@Override
	public void setWhenError(ResultChoice resultChoice) {
		setApiElement(resultChoice, getOrderMap(), "when_error");
	}
	
	@Override
	public ResultChoice getWhenError() {
		return getApiElement("when_error", ResultChoiceImpl.class);
	}	
	
	@Override
	public Question getQuestion() {
		return ocilDocument.getQuestion(element.getAttributeValue("question_ref"));
	}
	
	@Override
	public void setQuestion(Question question) {
		element.setAttribute("question_ref", question.getId());
	}

	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return QUESTION_TEST_ACTION_ORDER;
	}
	
	@Override
	public String getQuestionRef() {
		return element.getAttributeValue("question_ref");
	}
	
	@Override
	public void setQuestionRef(String ref) {
		element.setAttribute("question_ref", ref);
	}
	
//	@Override
	public void setId(int idNum) {
		setId("ocil:" + ocilDocument.getIdNamespace() + ":testaction:" + idNum);
	}

}
