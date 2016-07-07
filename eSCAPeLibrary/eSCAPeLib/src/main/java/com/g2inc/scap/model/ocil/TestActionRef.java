package com.g2inc.scap.model.ocil;

public interface TestActionRef extends ModelBase {
	
	public TestAction getTestAction();
	public void setTestAction(TestAction testAction);
	public String getTestActionRefId();
	public void setTestActionRefId(String id);
	
	public boolean isNegate();
	public void setNegate(boolean negate);
	
	public Priority getPriority();
	public void setPriority(Priority priority);
}
