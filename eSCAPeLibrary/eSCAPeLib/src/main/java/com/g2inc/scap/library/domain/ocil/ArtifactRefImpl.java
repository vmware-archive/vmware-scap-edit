package com.g2inc.scap.library.domain.ocil;

import com.g2inc.scap.model.ocil.ArtifactRef;

public class ArtifactRefImpl extends ModelBaseImpl implements ArtifactRef {

	@Override
	public String getRefId() {
		return element.getAttributeValue("idref");
	}

	@Override
	public void setRefId(String refId) {
		element.setAttribute("idref", refId);
	}
	
	@Override
	public Boolean isRequired() {
		return getBoolean("required");
	}
	
	@Override
	public void setRequired(Boolean required) {
		setBoolean("required", required);
	}	
	

}
