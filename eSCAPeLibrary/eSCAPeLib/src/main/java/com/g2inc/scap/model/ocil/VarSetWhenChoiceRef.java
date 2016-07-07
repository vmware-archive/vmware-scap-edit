package com.g2inc.scap.model.ocil;

public interface VarSetWhenChoiceRef extends VarSetWhenCondition {
	
	public Choice getChoice();
	public void setChoice(Choice choice);
	
	public String getChoiceRefId();
	public void setChoiceRefId(String id);

}
