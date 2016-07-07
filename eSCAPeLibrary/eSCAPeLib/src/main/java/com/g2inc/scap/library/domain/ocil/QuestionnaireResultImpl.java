package com.g2inc.scap.library.domain.ocil;

import java.util.HashMap;

import com.g2inc.scap.model.ocil.ArtifactResults;
import com.g2inc.scap.model.ocil.Questionnaire;
import com.g2inc.scap.model.ocil.QuestionnaireResult;
import com.g2inc.scap.model.ocil.ResultEnum;

public class QuestionnaireResultImpl extends ModelBaseImpl implements  QuestionnaireResult {
	
	public final static HashMap<String, Integer> QUESTIONNAIRE_RESULT_ORDER = new HashMap<String, Integer>();
	static {
		QUESTIONNAIRE_RESULT_ORDER.put("artifact_result", 0);
	}

	@Override
	public ArtifactResults getArtifactResults() {
		return getApiElement("artifact_results", ArtifactResultsImpl.class);
	}
	
	@Override
	public void setArtifactResults(ArtifactResults artifactResults) {
		setApiElement(artifactResults, getOrderMap(), "artifact_results");
	}
	
	@Override
	public Questionnaire getQuestionnaire() {
		return ocilDocument.getQuestionnaire(element.getAttributeValue("questionnaire_ref"));
	}
	
	@Override
	public void setQuestionnaire(Questionnaire questionnaire) {
		element.setAttribute("questionnaire_ref", questionnaire.getId());
	}

	@Override
	public ResultEnum getResult() {
		ResultEnum result = null;
		String resultString = getChildStringValue("result");
		if (resultString != null) {
			result = ResultEnum.valueOf(resultString);
		}
		return result;
	}
	
	@Override
	public void setResult(ResultEnum result) {
		if (result != null) {
			setStringValueChild("result", result.name(), getOrderMap());
		}
	}	
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return QUESTIONNAIRE_RESULT_ORDER;
	}

}
