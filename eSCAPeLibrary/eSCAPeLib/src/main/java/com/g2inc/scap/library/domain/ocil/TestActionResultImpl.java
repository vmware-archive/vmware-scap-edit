package com.g2inc.scap.library.domain.ocil;

import java.util.HashMap;

import com.g2inc.scap.model.ocil.ArtifactResult;
import com.g2inc.scap.model.ocil.ArtifactResults;
import com.g2inc.scap.model.ocil.ResultEnum;
import com.g2inc.scap.model.ocil.TestActionRef;
import com.g2inc.scap.model.ocil.TestActionResult;

public class TestActionResultImpl extends ModelBaseImpl implements TestActionResult {

	public final static HashMap<String, Integer> RESULT_CHOICE_ORDER = new HashMap<String, Integer>();
	static {
		RESULT_CHOICE_ORDER.put("artifact_results", 0);
	}
	
	@Override
	public ResultEnum getResult() {
		ResultEnum result = null;
		String resultString = element.getAttributeValue("result");
		if (resultString != null) {
			result = ResultEnum.valueOf(resultString);
		}
		return result;
	}
	
	@Override
	public void setResult(ResultEnum result) {
		if (result != null) {
			element.setAttribute("result", result.name());
		}
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
	public TestActionRef getTestActionRef() {
		return getApiElement("test_action_ref", TestActionRefImpl.class);
	}

	@Override
	public void setTestActionRef(TestActionRef ref) {
		setApiElement(ref, getOrderMap(), "test_action_ref");
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return RESULT_CHOICE_ORDER;
	}

	@Override
	public void addArtifactResult(ArtifactResult artifactResult) {
		ArtifactResults artifactResults = getArtifactResults();
		if (artifactResults == null) {
			artifactResults = ocilDocument.createArtifactResults();
			setArtifactResults(artifactResults);
		}
		artifactResults.addArtifactResult(artifactResult);
	}
}
