package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.Choice;
import com.g2inc.scap.model.ocil.ChoiceGroup;

public class ChoiceGroupImpl extends ModelBaseImpl implements ChoiceGroup {
	
	public final static HashMap<String, Integer> CHOICE_GROUP_ORDER = new HashMap<String, Integer>();
	static {
		CHOICE_GROUP_ORDER.put("choice", 0);
	}
	
	@Override
	public List<Choice> getChoices() {
		return getApiElementList(new ArrayList<Choice>(), "choice", ChoiceImpl.class);
	}
	
	@Override
	public void setChoices(List<Choice> list) {
		replaceApiList(list, getOrderMap(), "choice");
	}
	
	@Override
	public void addChoice(Choice choice) {
		insertApiChild(choice, getOrderMap(), -1);
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return CHOICE_GROUP_ORDER;
	}
	
//	@Override
	public void setId(int idNum) {
		setId("ocil:" + ocilDocument.getIdNamespace() + ":choicegroup:" + idNum);
	}
}
