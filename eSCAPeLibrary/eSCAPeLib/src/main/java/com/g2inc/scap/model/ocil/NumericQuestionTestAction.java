package com.g2inc.scap.model.ocil;

import java.util.List;

public interface NumericQuestionTestAction extends QuestionTestAction {
	
	public List<WhenEquals> getWhenEqualsList();
	public void setWhenEqualsList(List<WhenEquals> list);
	public void addWhenEquals(WhenEquals whenEquals);

	public List<WhenRange> getWhenRangeList();
	public void setWhenRangeList(List<WhenRange> list);
	public void addWhenRange(WhenRange whenRange);

}
