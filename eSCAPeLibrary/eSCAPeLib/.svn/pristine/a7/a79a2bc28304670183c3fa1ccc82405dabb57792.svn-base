package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.ArtifactResult;
import com.g2inc.scap.model.ocil.ArtifactResults;

public class ArtifactResultsImpl extends ModelBaseImpl implements ArtifactResults {
	
	public final static HashMap<String, Integer> ARTIFACTS_ORDER = new HashMap<String, Integer>();
	static {
		ARTIFACTS_ORDER.put("artifact_result", 0);
	}
	
	@Override
	public List<ArtifactResult> getArtifactResultList() {
		return getApiElementList(new ArrayList<ArtifactResult>(), "artifact_result", ArtifactResultImpl.class);
	}

	@Override
	public void setArtifactResultList(List<ArtifactResult> list) {
		replaceApiList(list, getOrderMap(), "artifact_result");
	}
	
	@Override
	public void addArtifactResult(ArtifactResult artifact) {
		addApiElement(artifact, getOrderMap());
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ARTIFACTS_ORDER;
	}
	
}
