package com.g2inc.scap.model.ocil;

public interface BooleanQuestionTestAction extends QuestionTestAction {
	
	public ResultChoice getWhenTrue();
	public void setWhenTrue(ResultChoice type);

	public ResultChoice getWhenFalse();
	public void setWhenFalse(ResultChoice type);

}
