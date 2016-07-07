package com.g2inc.scap.model.ocil;

public interface QuestionnaireResult extends ModelBase {
	
	public ArtifactResults getArtifactResults();
	public void setArtifactResults(ArtifactResults results);
	
	public Questionnaire getQuestionnaire();
	public void setQuestionnaire(Questionnaire questionnaire);
	
	public ResultEnum getResult();
	public void setResult(ResultEnum result);

}
