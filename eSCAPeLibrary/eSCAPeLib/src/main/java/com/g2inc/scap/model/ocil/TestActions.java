package com.g2inc.scap.model.ocil;

import java.util.List;

public interface TestActions extends ModelBase {
	
	public List<TestAction> getTestActionList();
	public void setTestActionList(List<TestAction> list);
	void addTestAction(TestAction testAction);

}
