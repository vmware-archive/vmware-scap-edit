package com.g2inc.scap.library.domain.xccdf;

import java.util.List;

import com.g2inc.scap.library.domain.SCAPElement;

public interface ComplexValueElement extends ValueElement {
	
	public List<String> getItemList();
	public void setItemList(List<String> list);

}
