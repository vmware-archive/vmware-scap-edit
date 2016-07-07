package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.Artifact;
import com.g2inc.scap.model.ocil.Artifacts;

public class ArtifactsImpl extends ModelBaseImpl implements Artifacts {
	
	public final static HashMap<String, Integer> ARTIFACTS_ORDER = new HashMap<String, Integer>();
	static {
		ARTIFACTS_ORDER.put("artifact", 0);
	}
	
	@Override
	public List<Artifact> getArtifactList() {
		return getApiElementList(new ArrayList<Artifact>(), "artifact", ArtifactImpl.class);
	}

	@Override
	public void setArtifactList(List<Artifact> list) {
		replaceApiList(list, getOrderMap(), "artifact");
	}
	
	@Override
	public void addArtifact(Artifact artifact) {
		addApiElement(artifact, getOrderMap());
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ARTIFACTS_ORDER;
	}
	
}
