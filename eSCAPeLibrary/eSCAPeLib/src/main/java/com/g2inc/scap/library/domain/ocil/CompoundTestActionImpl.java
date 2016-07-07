package com.g2inc.scap.library.domain.ocil;

import java.util.HashMap;

import com.g2inc.scap.model.ocil.Artifacts;
import com.g2inc.scap.model.ocil.CompoundTestAction;
import com.g2inc.scap.model.ocil.Operation;
import com.g2inc.scap.model.ocil.References;
import com.g2inc.scap.model.ocil.TestAction;
import com.g2inc.scap.model.ocil.TestActionRef;
import com.g2inc.scap.model.ocil.TextType;

public abstract class CompoundTestActionImpl extends ItemBaseImpl implements CompoundTestAction  {
	
	public final static HashMap<String, Integer> ORDER_MAP = new HashMap<String, Integer>();
	static {
		ORDER_MAP.put("notes", 0);
		ORDER_MAP.put("title", 1);
		ORDER_MAP.put("description", 2);
		ORDER_MAP.put("references", 3);
		ORDER_MAP.put("actions", 4);
		ORDER_MAP.put("artifacts", 5);
	}

	@Override
	public TextType getTitle() {
		return getApiElement("title", TextTypeImpl.class);
	}
	
	@Override
	public void setTitle(TextType title) {
		setApiElement(title, getOrderMap(), "title");
	}
	
	@Override
	public void setTitle(String title) {
		TextType titleTextType = ocilDocument.createTitle();
		titleTextType.setValue(title);
		setTitle(titleTextType);
	}

	@Override
	public TextType getDescription() {
		return getApiElement("description", TextTypeImpl.class);
	}
	
	@Override
	public void setDescription(TextType title) {
		setApiElement(title, getOrderMap(), "description");
	}
	
	@Override
	public void setDescription(String desc) {
		TextType descTextType = ocilDocument.createDescription();
		descTextType.setValue(desc);
		setDescription(descTextType);
	}	

	@Override
	public References getReferences() {
		return getApiElement("references", ReferencesImpl.class);
	}

	@Override
	public void setReferences(References refs) {
		setApiElement(refs, getOrderMap(), "references");
	}

	@Override
	public Operation getActions() {
		return getApiElement("actions", OperationImpl.class);
	}

	@Override
	public void setActions(Operation oper) {
		setApiElement(oper, getOrderMap(), "actions");	
	}
	
	@Override
	public void addAction(TestAction action) {
		Operation oper = getActions();
		if (oper == null) {
			oper = ocilDocument.createOperation();
			setActions(oper);
		}
		TestActionRef testActionRef = ocilDocument.createTestActionRef();
		testActionRef.setTestAction(action);
		oper.addTestActionRef(testActionRef);
	}
	
	@Override
	public Artifacts getArtifacts() {
		return getApiElement("artifacts", ArtifactsImpl.class);
	}

	@Override
	public void setArtifacts(Artifacts artifacts) {
		setApiElement(artifacts, getOrderMap(), "artifacts");		
	}
	
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ORDER_MAP;
	}	

}
