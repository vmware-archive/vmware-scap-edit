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

import com.g2inc.scap.model.ocil.Instructions;
import com.g2inc.scap.model.ocil.Question;
import com.g2inc.scap.model.ocil.QuestionText;

public class QuestionImpl extends ItemBaseImpl implements Question {
	
	public final static HashMap<String, Integer> QUESTION_ORDER = new HashMap<String, Integer>();
	static {
		QUESTION_ORDER.put("notes", 0);
		QUESTION_ORDER.put("question_text", 1);
		QUESTION_ORDER.put("instructions", 2);
	}
	
	@Override
	public List<QuestionText> getQuestionTextList() {
		return getApiElementList(new ArrayList<QuestionText>(), "question_text", QuestionTextImpl.class);
	}
	
	@Override
	public void setQuestionTextList(List<QuestionText> list) {
		replaceApiList(list, getOrderMap(), "question_text");
	}
	
	@Override
	public Instructions getInstructions() {
		return getApiElement("instructions", InstructionsImpl.class);
	}
	
	@Override
	public void setInstructions(Instructions instructions) {
		setApiElement(instructions, getOrderMap(), "instructions");
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return QUESTION_ORDER;
	}
	
//	@Override
	public void setId(int idNum) {
		setId("ocil:" + ocilDocument.getIdNamespace() + ":question:" + idNum);
	}

	@Override
	public void addQuestionText(QuestionText text) {
		insertApiChild(text, getOrderMap(), -1);
	}

}
