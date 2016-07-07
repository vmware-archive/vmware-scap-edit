package com.g2inc.scap.model.ocil;

import java.util.List;

public interface StringQuestionTestAction extends QuestionTestAction {
	
	public List<WhenPattern> getWhenPatternList();
	public void setWhenPatternList(List<WhenPattern> list);
	void addWhenPattern(WhenPattern when);

}
