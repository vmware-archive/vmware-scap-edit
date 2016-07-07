package com.g2inc.scap.model.ocil;

public interface Questionnaire extends CompoundTestAction, Identified {

	public Priority getPriority();
	public void setPriority(Priority priority);
	
	public boolean isChildOnly();
	public void setChildOnly(boolean isChildOnly);
	
	public Scope getScope();
	public void setScope(Scope scope);
	
}
