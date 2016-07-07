package com.g2inc.scap.model.ocil;

public interface TestActionResult extends ModelBase {
	
	public ArtifactResults getArtifactResults();
	public void setArtifactResults(ArtifactResults results);
	
	public TestActionRef getTestActionRef();
	public void setTestActionRef(TestActionRef testActionRef);
	
	public ResultEnum getResult();
	public void setResult(ResultEnum resultType);
	public void addArtifactResult(ArtifactResult artifactResult);

}
