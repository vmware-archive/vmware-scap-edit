package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.QuestionnaireResult;
import com.g2inc.scap.model.ocil.QuestionnaireResults;

public class QuestionnaireResultsImpl extends ModelBaseImpl implements QuestionnaireResults {
	
	public final static HashMap<String, Integer> QUESTIONNAIRE_RESULTS_ORDER = new HashMap<String, Integer>();
	static {
		QUESTIONNAIRE_RESULTS_ORDER.put("questionnaire_result", 0);
	}
	
	@Override
	public List<QuestionnaireResult> getQuestionnaireResultList() {
		return getApiElementList(new ArrayList<QuestionnaireResult>(), "questionnaire_result", QuestionnaireResultImpl.class);
	}

	@Override
	public void setQuestionnaireResultList(List<QuestionnaireResult> list) {
		replaceApiList(list, getOrderMap(), "questionnaire_result");
	}
	
	@Override
	public void addQuestionnaireResult(QuestionnaireResult artifact) {
		addApiElement(artifact, getOrderMap());
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return QUESTIONNAIRE_RESULTS_ORDER;
	}
	
}
