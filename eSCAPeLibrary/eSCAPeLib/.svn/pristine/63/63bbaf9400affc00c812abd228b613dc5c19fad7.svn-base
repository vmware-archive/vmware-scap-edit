package com.g2inc.scap.library.domain.ocil;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import com.g2inc.scap.model.ocil.ChoiceGroup;
import com.g2inc.scap.model.ocil.Question;
import com.g2inc.scap.model.ocil.Questions;

public class QuestionsImpl extends ItemBaseImpl implements Questions {
	
	public final static HashMap<String, Class<?>> CLASS_MAP = new HashMap<String, Class<?>>();
	public final static HashMap<String, Integer> QUESTIONS_ORDER = new HashMap<String, Integer>();
	static {
		CLASS_MAP.put("boolean_question", BooleanQuestionImpl.class);
		CLASS_MAP.put("choice_question", ChoiceQuestionImpl.class);
		CLASS_MAP.put("numeric_question", NumericQuestionImpl.class);
		CLASS_MAP.put("string_question", StringQuestionImpl.class);
		
		QUESTIONS_ORDER.put("notes", 0);
		for (String key : CLASS_MAP.keySet()) {
			QUESTIONS_ORDER.put(key, 1);
		}
		QUESTIONS_ORDER.put("choice_group", 2);
	}
	
	@Override
	public List<Question> getQuestionList() {
		List<Question> children = new ArrayList<Question>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			Class<?> clazz = CLASS_MAP.get(elem.getName());
			if (clazz != null) {
				Question question = (Question) createApiElement(elem, clazz);
				children.add(question);
			}
		}
		return children;
	}
	
	@Override
	public void setQuestionList(List<Question> list) {
		for (String key : CLASS_MAP.keySet()) {
			this.element.removeChildren(key);
		}
		for (Question question : list) {
			insertApiChild(question, getOrderMap(), -1);
		}
	}
	
	@Override
	public void addQuestion(Question question) {
		insertApiChild(question, getOrderMap(), -1);
	}
	
	@Override
	public void setChoiceGroupList(List<ChoiceGroup> list) {
		replaceApiList(list, getOrderMap(), "choice_group");
	}
	
	@Override
	public void addChoiceGroup(ChoiceGroup choiceGroup) {
		insertApiChild(choiceGroup, getOrderMap(), -1);
	}
	
	@Override
	public List<ChoiceGroup> getChoiceGroupList() {
		return getApiElementList(new ArrayList<ChoiceGroup>(), "choice_group", ChoiceGroupImpl.class);
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return QUESTIONS_ORDER;
	}

}
