package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.model.ocil.Reference;
import com.g2inc.scap.model.ocil.References;

public class ReferencesImpl extends ModelBaseImpl implements References {
	
	public final static HashMap<String, Integer> REFERENCES_ORDER = new HashMap<String, Integer>();
	static {
		REFERENCES_ORDER.put("reference", 0);
	}
	
	@Override
	public List<Reference> getReferenceList() {
		return getApiElementList(new ArrayList<Reference>(), "reference", ReferenceImpl.class);
	}

	@Override
	public void setReferenceList(List<Reference> list) {
		replaceApiList(list, getOrderMap(), "reference");
	}
	
	@Override
	public void addReference(Reference reference) {
		addApiElement(reference, getOrderMap());
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return REFERENCES_ORDER;
	}
	
}
