package com.g2inc.scap.model.ocil;

import java.util.List;

public interface ArtifactResults extends ModelBase {

	public List<ArtifactResult> getArtifactResultList();
	public void setArtifactResultList(List<ArtifactResult> list);	
	public void addArtifactResult(ArtifactResult artifactResult);
	
}
