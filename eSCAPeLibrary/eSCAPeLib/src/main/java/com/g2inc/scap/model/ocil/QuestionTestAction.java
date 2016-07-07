package com.g2inc.scap.model.ocil;

public interface QuestionTestAction extends ItemBase, TestAction {
	
	public Question getQuestion();
	public void setQuestion(Question question);
	
	public String getQuestionRef();
	public void setQuestionRef(String ref);
	
	public TextType getTitle();	
	public void setTitle(TextType title);
	
	public ResultChoice getWhenUnknown();
	public void setWhenUnknown(ResultChoice type);

	public ResultChoice getWhenNotTested();
	public void setWhenNotTested(ResultChoice type);
	
	public ResultChoice getWhenNotApplicable();
	public void setWhenNotApplicable(ResultChoice type);

	public ResultChoice getWhenError();
	public void setWhenError(ResultChoice type);
	
}
