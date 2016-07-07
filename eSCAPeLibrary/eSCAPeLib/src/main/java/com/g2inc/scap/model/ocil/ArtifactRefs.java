package com.g2inc.scap.model.ocil;

import java.util.List;

public interface ArtifactRefs extends ModelBase {

	public List<ArtifactRef> getArtifactRefList();
	public void setArtifactRefList(List<ArtifactRef> list);
	public void addArtifactRef(ArtifactRef ref);
	
}
