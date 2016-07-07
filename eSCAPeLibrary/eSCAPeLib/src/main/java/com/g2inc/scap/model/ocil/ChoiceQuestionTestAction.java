package com.g2inc.scap.model.ocil;

import java.util.List;

public interface ChoiceQuestionTestAction extends QuestionTestAction {
	
	public List<WhenChoice> getWhenChoiceList();
	public void setWhenChoiceList(List<WhenChoice> list);
	void addWhenChoice(WhenChoice whenChoice);

}
