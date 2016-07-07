package com.g2inc.scap.library.domain.ocil;

import com.g2inc.scap.model.ocil.ChoiceRef;

public class ChoiceRefImpl extends ModelBaseImpl implements ChoiceRef {

	@Override
	public String getRefId() {
		return element.getText();
	}

	@Override
	public void setRefId(String refId) {
		element.setText(refId);
	}
	

}
