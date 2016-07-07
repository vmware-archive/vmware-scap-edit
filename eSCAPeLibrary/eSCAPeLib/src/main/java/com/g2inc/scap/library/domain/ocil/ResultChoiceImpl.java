package com.g2inc.scap.library.domain.ocil;

import java.util.HashMap;

import com.g2inc.scap.model.ocil.ArtifactRefs;
import com.g2inc.scap.model.ocil.ResultChoice;
import com.g2inc.scap.model.ocil.ResultEnum;
import com.g2inc.scap.model.ocil.TestActionRef;

public class ResultChoiceImpl extends ModelBaseImpl implements ResultChoice {

	public final static HashMap<String, Integer> RESULT_CHOICE_ORDER = new HashMap<String, Integer>();
	static {
		RESULT_CHOICE_ORDER.put("result", 0);
		RESULT_CHOICE_ORDER.put("test_action_ref", 0);
		RESULT_CHOICE_ORDER.put("artifacts", 1);
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
	public ArtifactRefs getArtifactRefs() {
		return getApiElement("artifact_refs", ArtifactRefsImpl.class);
	}
	
	@Override
	public void setArtifactRefs(ArtifactRefs artifacts) {
		setApiElement(artifacts, getOrderMap(), "artifacts");
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

//	@Override
//	public void addArtifact(Artifact artifact) {
//		Artifacts artifacts = getArtifacts();
//		if (artifacts == null) {
//			artifacts = ocilDocument.createArtifacts();
//			setArtifacts(artifacts);
//		}
//		artifacts.addArtifact(artifact);
//	}
}
