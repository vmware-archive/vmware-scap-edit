package com.g2inc.scap.library.domain.ocil;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.g2inc.scap.model.ocil.QuestionText;

public class QuestionTextImpl extends ModelBaseImpl implements QuestionText {
	
	public String getText() throws IOException {
		if (getSubVarIds().size() == 0) {
			return element.getText();
		} else {
			XMLOutputter outputter = new XMLOutputter();
			StringWriter writer = new StringWriter();
			List<?> content = element.getContent();
			outputter.output(content, writer);
			return writer.toString();
		}
	}
	
	@Override
	public Set<String> getSubVarIds() {
		Set<String> set = new HashSet<String>();	
		List<?> content = element.getContent();
		for (Object listEntry : content) {
			if (listEntry instanceof Element) {
				Element listElement = (Element) listEntry;
				if (listElement.getName().equals("sub")) {
					String varId = listElement.getAttributeValue("var_ref");
					if (varId != null) {
						set.add(varId);
					}
				}
			}
		}
		return set;
	}
	
	@Override
	public void replaceVarIds(Map<String, String> varIdMap) {
		List<?> content = element.getContent();
		Set<String> oldVarIds = varIdMap.keySet();
		for (String oldVarId : oldVarIds) {
			replaceVarId(oldVarId, varIdMap.get(oldVarId), content);
		}
	}
	
	private void replaceVarId(String oldId, String newId, List<?> content) {
		for (Object listEntry : content) {
			if (listEntry instanceof Element) {
				Element listElement = (Element) listEntry;
				if (listElement.getName().equals("sub")) {
					String varId = listElement.getAttributeValue("var_ref");
					if (varId != null && varId.equals(oldId)) {
						listElement.setAttribute("var_ref", newId);
					}
				}
			}
		}
	}
}
