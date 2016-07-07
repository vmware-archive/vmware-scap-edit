package com.g2inc.scap.model.ocil;

import java.util.List;

public interface TestActionResults extends ModelBase {
	
	public List<TestActionResult> getTestActionResultList();	
	public void setTestActionResultList(List<TestActionResult> list);
	public void addTestActionResult(TestActionResult result);

}
