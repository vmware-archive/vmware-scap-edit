package com.g2inc.scap.library.domain.ocil;

import com.g2inc.scap.model.ocil.Choice;
import com.g2inc.scap.model.ocil.VarSetWhenChoiceRef;


public class VarSetWhenChoiceRefImpl extends VarSetWhenConditionImpl implements VarSetWhenChoiceRef {
	
	@Override
	public Choice getChoice() {
		return ocilDocument.getChoice(element.getAttributeValue("choice_ref"));
	}	
	
	@Override
	public void setChoice(Choice choice) {
		element.setAttribute("choice_ref", choice.getId());
	}
	
	@Override
	public String getChoiceRefId() {
		return element.getAttributeValue("choice_ref");
	}
	
	@Override
	public void setChoiceRefId(String id) {
		element.setAttribute("choice_ref", id);
	}

}
