package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.g2inc.scap.model.ocil.Choice;
import com.g2inc.scap.model.ocil.ChoiceAbstract;
import com.g2inc.scap.model.ocil.ChoiceGroupRef;
import com.g2inc.scap.model.ocil.ChoiceQuestion;

/* ESCAPE Software Copyright 2010 G2, Inc. - All rights reserved.
*
* ESCAPE is open source software distributed under GNU General Public License Version 3.  ESCAPE is not in the public domain 
* and G2, Inc. holds its copyright.  Redistribution and use in source and binary forms, with or without modification, are
* permitted provided that the following conditions are met:

* 1. Redistributions of ESCAPE source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
* 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the ESCAPE Software distribution. 
* 3. Neither the name of G2, Inc. nor the names of any contributors may be used to endorse or promote products derived from this software without specific prior written permission. 

* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES,
* INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
* IN NO EVENT SHALL G2, INC., THE AUTHORS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
* OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
* OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.

* You should have received a copy of the GNU General Public License Version 3 along with this program. 
* If not, see http://www.gnu.org/licenses/ for a copy.
*/

public class ChoiceQuestionImpl extends QuestionImpl implements ChoiceQuestion {
	private static Logger LOG = Logger.getLogger(ChoiceQuestionImpl.class);
	
	public final static HashMap<String, Integer> CHOICE_QUESTION_ORDER = new HashMap<String, Integer>();
	static {
		CHOICE_QUESTION_ORDER.put("notes", 0);
		CHOICE_QUESTION_ORDER.put("question_text", 1);
		CHOICE_QUESTION_ORDER.put("instructions", 2);
		CHOICE_QUESTION_ORDER.put("choice", 3);
		CHOICE_QUESTION_ORDER.put("choice_group_ref", 3);
	}
	
	@Override
	public Choice getDefaultAnswer() {
		Choice result = null;
		String defaultAnswerRef = element.getAttributeValue("default_answer_ref");
		if (defaultAnswerRef != null) {
			result = getChoice(defaultAnswerRef);
		}
		return result;
	}
	
	@Override
	public String getDefaultAnswerRef() {
		return element.getAttributeValue("default_answer_ref");
	}
	
	@Override
	public void setDefaultAnswerRef(String defaultAnswerId) {
		element.setAttribute("default_answer_ref", defaultAnswerId);
	}
	
	@Override
	public void setDefaultAnswer(Choice defaultChoice) {
		if (defaultChoice != null) {
			setOptionalAttribute("default_answer_ref", defaultChoice.getId());
		}
	}
	
	@Override
	public List<ChoiceAbstract> getChoiceAndChoiceGroupList() {
		List<ChoiceAbstract> children = new ArrayList<ChoiceAbstract>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			if (elem.getName().equals("choice")) {
				Choice child = (Choice) createApiElement(elem, ChoiceImpl.class);
				children.add(child);
			} else if (elem.getName().equals("choice_group_ref")) {
				ChoiceGroupRef child = (ChoiceGroupRef) createApiElement(elem, ChoiceGroupRefImpl.class);
				children.add(child);
			}
		}
		return children;
	}
	
	@Override
	public void addChoice(Choice choice) {
		addApiElement(choice, getOrderMap());
	}
	
	@Override
	public void addChoiceGroupRef(ChoiceGroupRef choiceGroupRef) {
//		ChoiceGroupRef newChoiceGroup = this.createApiElement("choice_group_ref", ChoiceGroupRefImpl.class);
//		newChoiceGroup.setRefId(choiceGroup.getId());
		addApiElement(choiceGroupRef, getOrderMap());
	}
	
	@Override
	public void setChoiceAbstractList(List<ChoiceAbstract> children) {
		this.element.removeChildren("choice");
		this.element.removeChildren("choice_group_ref");
		for (ChoiceAbstract child : children) {
			if (child instanceof ChoiceImpl) {
				addChoice((Choice) child);
			} else if (child instanceof ChoiceGroupRefImpl) {
				addChoiceGroupRef((ChoiceGroupRef) child);
			}			
		}
	}
	
	@Override
	public Choice getChoice(String id) {
		Choice foundChoice = null;
		List<Choice> allChoices = getAllChoices();
		for (Choice choice : allChoices) {
			if (choice.getId().equals(id)) {
				foundChoice = choice;
			}
		}
		return foundChoice;
	}
	
	@Override
	public List<Choice> getAllChoices() {
		List<Choice> children = new ArrayList<Choice>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			if (elem.getName().equals("choice")) {
				Choice child = createApiElement(elem, ChoiceImpl.class);
				children.add(child);
			} //else if (elem.getName().equals("choice_group_ref")) {
//				LOG.debug("getAllChoices processing " + elem.getName() + ", text=" + elem.getText());
//				ChoiceGroup child = ((OcilDocumentImpl) getSCAPDocument()).getChoiceGroup(elem.getText());
//				children.addAll(child.getChoices());
//			}
		}
		return children;
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return CHOICE_QUESTION_ORDER;
	}
	
}
