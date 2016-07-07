package com.g2inc.scap.library.domain.ocil;

import java.util.HashMap;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.g2inc.scap.model.ocil.Artifact;
import com.g2inc.scap.model.ocil.ArtifactResult;
import com.g2inc.scap.model.ocil.User;

public class ArtifactResultImpl extends ModelBaseImpl implements ArtifactResult {
	
	public final static HashMap<String, Integer> ARTIFACT_ORDER = new HashMap<String, Integer>();
	static {
		ARTIFACT_ORDER.put("title", 0);
		ARTIFACT_ORDER.put("description", 1);
	}
	
	@Override
	public String getValue() {
		return getChildStringValue("value");
	}
	
	@Override
	public void setValue(String value) {
		insertStringValueChild("value", value, getOrderMap());
	}	
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ARTIFACT_ORDER;
	}

	@Override
	public String getProvider() {
		return getChildStringValue("provider");
	}

	@Override
	public void setProvider(String provider) {
		insertStringValueChild("provider", provider, getOrderMap());
	}

	@Override
	public User getSubmitter() {
		return getApiElement("submitter", UserImpl.class);
	}

	@Override
	public void setSubmitter(User submitter) {
		setApiElement(submitter, getOrderMap(), "submitter");
		
	}

	@Override
	public Artifact getArtifact() {
		Artifact artifact = null;
		String artifactRef = element.getAttributeValue("artifact_ref");
		if (artifactRef != null) {
			artifact = ocilDocument.getArtifact(artifactRef);
		}
		return artifact;
	}	

	@Override
	public void setArtifact(Artifact artifact) {
		element.setAttribute("artifact_ref", artifact.getId());
	}	

	@Override
	public String getTimeStampString() {
		return this.getChildStringValue("timestamp");
	}
	
	@Override
	public XMLGregorianCalendar getTimeStamp() {
		XMLGregorianCalendar timestamp = null;
		String timestampString = getTimeStampString();
		if (timestampString != null) {
			try {
				DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
				timestamp = datatypeFactory.newXMLGregorianCalendar(timestampString);
			} catch (DatatypeConfigurationException e) {
				throw new IllegalStateException("Could not interpret timestamp string: " + timestampString, e);
			}			
		}
		return timestamp;
	}
	
	@Override
	public void setTimeStamp(XMLGregorianCalendar date) {
		String dateString = date.toString();
		setTimeStampString(dateString);
	}
	
	@Override
	public void setTimeStampString(String dateString) {
		setStringValueChild("timestamp", dateString, getOrderMap());
	}


}
