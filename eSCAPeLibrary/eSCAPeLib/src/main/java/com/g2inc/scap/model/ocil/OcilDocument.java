package com.g2inc.scap.model.ocil;

public interface OcilDocument extends Ocil {
    
    public static final String OCIL_NAMESPACE = "http://scap.nist.gov/schema/ocil/2";
	
	public <T extends ModelBase> T createApiElement(String tag, Class<?> clazz);
	
	public String getIdNamespace();
	public void setIdNamespace(String value);

}
