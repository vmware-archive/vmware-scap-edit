package com.g2inc.scap.library.domain.ocil;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.g2inc.scap.model.ocil.ArtifactResults;
import com.g2inc.scap.model.ocil.QuestionResults;
import com.g2inc.scap.model.ocil.QuestionnaireResults;
import com.g2inc.scap.model.ocil.Results;
import com.g2inc.scap.model.ocil.Targets;
import com.g2inc.scap.model.ocil.TestActionResults;
import com.g2inc.scap.model.ocil.TextType;

public class ResultsImpl extends ModelBaseImpl implements Results {

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
	public QuestionnaireResults getQuestionnaireResults() {
		return getApiElement("questionnaire_results", QuestionnaireResultsImpl.class);
	}

	@Override
	public void setQuestionnaireResults(QuestionnaireResults results) {
		setApiElement(results, getOrderMap(), "questionnaire_results");
	}

	@Override
	public TestActionResults getTestActionResults() {
		return getApiElement("test_action_results", TestActionResultsImpl.class);
	}

	@Override
	public void setTestActionResults(TestActionResults results) {
		setApiElement(results, getOrderMap(), "test_action_results");
	}

	@Override
	public QuestionResults getQuestionResults() {
		return getApiElement("question_results", QuestionResultsImpl.class);
	}

	@Override
	public void setQuestionResults(QuestionResults results) {
		setApiElement(results, getOrderMap(), "question_results");
	}

	@Override
	public ArtifactResults getArtifactResults() {
		return getApiElement("artifact_results", ArtifactResultsImpl.class);
	}

	@Override
	public void setArtifactResults(ArtifactResults results) {
		setApiElement(results, getOrderMap(), "artifact_results");
	}

	@Override
	public Targets getTargets() {
		return getApiElement("targets", TargetsImpl.class);
	}

	@Override
	public void setTargets(Targets targets) {
		setApiElement(targets, getOrderMap(), "targets");
	}
	
	@Override
	public String getStartTimeString() {
		return this.getChildStringValue("timestamp");
	}
	
	@Override
	public XMLGregorianCalendar getStartTime() {
		XMLGregorianCalendar timestamp = null;
		String timestampString = getStartTimeString();
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
	public void setStartTime(XMLGregorianCalendar date) {
		date.setTimezone(DatatypeConstants.FIELD_UNDEFINED); 
		date.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
		String dateString = date.toString();
		setStartTimeString(dateString);
	}
	
	@Override
	public void setStartTime() {
		try {
			XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
			setStartTime(date);
		} catch (DatatypeConfigurationException e) {
			throw new IllegalStateException("Can't configure DataType to set timestamp", e);
		}
	}
	
	@Override
	public void setStartTimeString(String dateString) {
		setStringValueChild("timestamp", dateString, getOrderMap());
	}
	
	@Override
	public String getEndTimeString() {
		return this.getChildStringValue("timestamp");
	}
	
	@Override
	public XMLGregorianCalendar getEndTime() {
		XMLGregorianCalendar timestamp = null;
		String timestampString = getEndTimeString();
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
	public void setEndTime(XMLGregorianCalendar date) {
		date.setTimezone(DatatypeConstants.FIELD_UNDEFINED); 
		date.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
		String dateString = date.toString();
		setEndTimeString(dateString);
	}
	
	@Override
	public void setEndTime() {
		try {
			XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
			setEndTime(date);
		} catch (DatatypeConfigurationException e) {
			throw new IllegalStateException("Can't configure DataType to set timestamp", e);
		}
	}
	
	@Override
	public void setEndTimeString(String dateString) {
		setStringValueChild("timestamp", dateString, getOrderMap());
	}	

}
