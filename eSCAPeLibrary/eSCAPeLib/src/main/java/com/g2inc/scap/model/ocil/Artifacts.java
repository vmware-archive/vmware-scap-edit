package com.g2inc.scap.model.ocil;

import java.util.List;

public interface Artifacts extends ModelBase {

	public List<Artifact> getArtifactList();
	public void setArtifactList(List<Artifact> list);	
	public void addArtifact(Artifact artifact);
	
}
