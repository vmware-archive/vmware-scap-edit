package com.g2inc.scap.library.domain.ocil;

import com.g2inc.scap.model.ocil.Choice;
import com.g2inc.scap.model.ocil.ChoiceQuestionResult;

public class ChoiceQuestionResultImpl extends QuestionResultImpl implements ChoiceQuestionResult {

	@Override
	public Choice getAnswer() {
		Choice choice = null;
		String choiceRef = element.getAttributeValue("choice_ref");
		if (choiceRef != null) 
			choice = ocilDocument.getChoice(choiceRef);
		return choice;
	}

	@Override
	public void setAnswer(Choice answer) {
		String choiceRef = answer.getId();
		element.setAttribute("choice_ref", choiceRef);
	}

}
