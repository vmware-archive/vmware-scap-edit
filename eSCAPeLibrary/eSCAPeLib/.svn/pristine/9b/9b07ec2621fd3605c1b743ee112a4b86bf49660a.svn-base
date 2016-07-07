package com.g2inc.scap.library.domain.ocil;

import com.g2inc.scap.model.ocil.Priority;
import com.g2inc.scap.model.ocil.TestAction;
import com.g2inc.scap.model.ocil.TestActionRef;

public class TestActionRefImpl extends ModelBaseImpl implements TestActionRef {

	@Override
	public TestAction getTestAction() {
		return ((OcilDocumentImpl) SCAPDocument).getTestAction(element.getText());
	}

	@Override
	public void setTestAction(TestAction testAction) {
		element.setText(testAction.getId());
	}
	
	@Override
	public String getTestActionRefId() {
		return element.getText();
	}
	
	@Override
	public void setTestActionRefId(String id) {
		element.setText(id);
	}
	
	@Override
	public boolean isNegate() {
		return getBoolean("negate", false);
	}

	@Override
	public void setNegate(boolean negate) {
		setBoolean("negate", negate);
	}

	@Override
	public Priority getPriority() {
		Priority priority = Priority.LOW;
		String priorityString = element.getAttributeValue("priority");
		if (priorityString != null) {
			priority = Priority.valueOf(priorityString);
		}
		return priority;
	}

	@Override
	public void setPriority(Priority priority) {
		if (priority != null) {
			element.setAttribute("priority", priority.name());
		}
	}

}
