package com.g2inc.scap.model.ocil;

public interface CompoundTestAction extends ItemBase, TestAction {
	
	public TextType getTitle();
	public void setTitle(TextType title);
	public void setTitle(String title);
	
	public TextType getDescription();
	public void setDescription(TextType desc);
	public void setDescription(String desc);
	
	public References getReferences();
	public void setReferences(References refs);
	
	public Operation getActions();
	public void setActions(Operation oper);
	public void addAction(TestAction action);
	
	public Artifacts getArtifacts();
	public void setArtifacts(Artifacts artifacts);

}
