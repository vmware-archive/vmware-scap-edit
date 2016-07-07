package com.g2inc.scap.library.domain.ocil;

import com.g2inc.scap.model.ocil.Question;
import com.g2inc.scap.model.ocil.QuestionResult;
import com.g2inc.scap.model.ocil.UserResponse;

public class QuestionResultImpl extends ModelBaseImpl implements QuestionResult {

	@Override
	public Question getQuestion() {
		return ocilDocument.getQuestion(element.getAttributeValue("question_ref"));
	}
	
	@Override
	public void setQuestion(Question question) {
		element.setAttribute("question_ref", question.getId());
	}

	@Override
	public UserResponse getResponse() {
		UserResponse userResp = null;
		String response = element.getAttributeValue("response");
		if (response != null) {
			userResp = UserResponse.valueOf(response);
		}
		return userResp;
	}

	@Override
	public void setResponse(UserResponse resp) {
		if (resp != null) {
			element.setAttribute("response", resp.toString());
		}
	}

}
