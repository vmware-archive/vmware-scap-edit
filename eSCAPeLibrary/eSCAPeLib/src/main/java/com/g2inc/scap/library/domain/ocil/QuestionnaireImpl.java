package com.g2inc.scap.library.domain.ocil;

import com.g2inc.scap.model.ocil.Priority;
import com.g2inc.scap.model.ocil.Questionnaire;
import com.g2inc.scap.model.ocil.Scope;

public class QuestionnaireImpl extends CompoundTestActionImpl implements Questionnaire {
	// no additional element content, so no need to override ORDER_MAP from superclass

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

	@Override
	public boolean isChildOnly() {
		return getBoolean("child_only", false);
	}

	@Override
	public void setChildOnly(boolean isChildOnly) {
		setBoolean("child_only", isChildOnly);
	}

	@Override
	public Scope getScope() {
		Scope scope = Scope.FULL;
		String valString = element.getAttributeValue("scope");
		if (valString != null) {
			scope = Scope.valueOf(valString);
		}
		return scope;
	}

	@Override
	public void setScope(Scope scope) {
		if (scope != null) {
			element.setAttribute("scope", scope.name());
		}	
	}
	
//	@Override
	public void setId(int idNum) {
		setId("ocil:" + ocilDocument.getIdNamespace() + ":questionnaire:" + idNum);
	}

}
