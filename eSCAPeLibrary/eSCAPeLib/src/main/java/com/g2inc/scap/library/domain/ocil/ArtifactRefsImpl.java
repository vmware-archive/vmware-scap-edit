package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.ArtifactRefs;
import com.g2inc.scap.model.ocil.ArtifactRef;

public class ArtifactRefsImpl extends ModelBaseImpl implements ArtifactRefs {
	
	public final static HashMap<String, Integer> ARTIFACTS_GROUP_ORDER = new HashMap<String, Integer>();
	static {
		ARTIFACTS_GROUP_ORDER.put("artifact_ref", 0);
	}
	
	@Override
	public List<ArtifactRef> getArtifactRefList() {
		return getApiElementList(new ArrayList<ArtifactRef>(), "artifact_ref", ArtifactRefImpl.class);
	}
	
	@Override
	public void setArtifactRefList(List<ArtifactRef> list) {
		replaceApiList(list, getOrderMap(), "artifact_ref");
	}
	
	@Override
	public void addArtifactRef(ArtifactRef choice) {
		insertApiChild(choice, getOrderMap(), -1);
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ARTIFACTS_GROUP_ORDER;
	}
	
}
