package com.g2inc.scap.model.ocil;

import java.util.List;

public interface References extends ModelBase {

	public List<Reference> getReferenceList();
	public void setReferenceList(List<Reference> list);
	void addReference(Reference reference);
	
}
