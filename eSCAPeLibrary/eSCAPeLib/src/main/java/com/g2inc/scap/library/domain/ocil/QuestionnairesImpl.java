package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.Questionnaire;
import com.g2inc.scap.model.ocil.Questionnaires;

public class QuestionnairesImpl extends ModelBaseImpl implements Questionnaires {
	
	public final static HashMap<String, Integer> QUESTIONNAIRES_ORDER = new HashMap<String, Integer>();
	static {
		QUESTIONNAIRES_ORDER.put("questionnaire", 0);
	}
	
	@Override
	public List<Questionnaire> getQuestionnaireList() {
		return getApiElementList(new ArrayList<Questionnaire>(), "questionnaire", QuestionnaireImpl.class);
	}

	@Override
	public void setQuestionnaireList(List<Questionnaire> list) {
		replaceApiList(list, getOrderMap(), "questionnaire");
	}
	
	@Override
	public void addQuestionnaire(Questionnaire questionnaire) {
		addApiElement(questionnaire, getOrderMap());
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return QUESTIONNAIRES_ORDER;
	}
	
}
