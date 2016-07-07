package com.g2inc.scap.library.domain.ocil;

import java.util.List;

import com.g2inc.scap.model.ocil.SystemTarget;
import com.g2inc.scap.model.ocil.TextType;

public class SystemTargetImpl extends NamedItemBaseImpl implements SystemTarget {

	@Override
	public String getOrganization() {
		// TODO Auto-generated method stub
		return getChildStringValue("organization");
	}

	@Override
	public void setOrganization(String org) {
		insertStringValueChild("organization", org, getOrderMap());
	}

	@Override
	public List<String> getIpAddressList() {
		return getStringList("ipaddress");
	}

	@Override
	public void setIpAddressList(List<String> list) {
		replaceStringList(list, getOrderMap(), "ipaddress");
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

}
