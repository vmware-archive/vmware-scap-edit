package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.Operation;
import com.g2inc.scap.model.ocil.Operator;
import com.g2inc.scap.model.ocil.Priority;
import com.g2inc.scap.model.ocil.TestActionRef;

public class OperationImpl extends ModelBaseImpl implements Operation {
	public final static HashMap<String, Integer> ORDER_MAP = new HashMap<String, Integer>();
	static {
		ORDER_MAP.put("test_action_ref", 0);
	}

	@Override
	public List<TestActionRef> getTestActionRefList() {
		return getApiElementList(new ArrayList<TestActionRef>(), "test_action_ref", TestActionRefImpl.class);
	}

	@Override
	public void setTestActionRefList(List<TestActionRef> list) {
		replaceApiList(list, getOrderMap(), "test_action_ref");
	}
	
	@Override 
	public void addTestActionRef(TestActionRef ref) {
		addApiElement(ref, getOrderMap());
	}

	@Override
	public Operator getOperation() {
		Operator oper = Operator.AND;
		String priorityString = element.getAttributeValue("operation");
		if (priorityString != null) {
			oper = Operator.valueOf(priorityString);
		}
		return oper;
	}

	@Override
	public void setOperation(Operator oper) {
		if (oper != null) {
			element.setAttribute("operation", oper.name());
		}
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
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ORDER_MAP;
	}
	

}
