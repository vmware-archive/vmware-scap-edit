package com.g2inc.scap.library.domain.ocil;

import java.util.HashMap;

import com.g2inc.scap.model.ocil.Artifact;
import com.g2inc.scap.model.ocil.ArtifactDataType;
import com.g2inc.scap.model.ocil.TextType;

public class ArtifactImpl extends ModelBaseImpl implements Artifact {
	
	public final static HashMap<String, Integer> ARTIFACT_ORDER = new HashMap<String, Integer>();
	static {
		ARTIFACT_ORDER.put("title", 0);
		ARTIFACT_ORDER.put("description", 1);
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
		TextType titleTextType = ocilDocument.createDescription();
		titleTextType.setValue(desc);
		setDescription(titleTextType);
	}	
	
	@Override
	public Boolean isPersistent() {
		return getBoolean("persistent");
	}
	
	@Override
	public void setPersistent(Boolean persistent) {
		setBoolean("persistent", persistent);
	}
	
	@Override
	public Boolean isRequired() {
		return getBoolean("required");
	}
	
	@Override
	public void setRequired(Boolean required) {
		setBoolean("required", required);
	}	
	
	@Override
	public ArtifactDataType getDatatype() {
		ArtifactDataType type = ArtifactDataType.TEXT;
		String typeString = element.getAttributeValue("datatype");
		if (typeString != null) {
			type = ArtifactDataType.valueOf(typeString);
		}
		return type;
	}
	
	@Override
	public void setDatatype(ArtifactDataType type) {
		if (type != null) {
			element.setAttribute("datatype", type.name());
		}
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ARTIFACT_ORDER;
	}

//	@Override
	public void setId(int idNum) {
		setId("ocil:" + ocilDocument.getIdNamespace() + ":artifact:" + idNum);
	}

}
