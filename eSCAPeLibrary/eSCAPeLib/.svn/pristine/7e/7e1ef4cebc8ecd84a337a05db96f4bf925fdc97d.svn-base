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

import com.g2inc.scap.model.ocil.Reference;
import com.g2inc.scap.model.ocil.Step;
import com.g2inc.scap.model.ocil.TextType;

public class StepImpl extends ModelBaseImpl implements Step {
	public final static HashMap<String, Integer> STEP_ORDER = new HashMap<String, Integer>();
	static {
		STEP_ORDER.put("description", 0);
		STEP_ORDER.put("reference", 1);
		STEP_ORDER.put("step", 2);
	}
	
	@Override
	public Boolean isDone() {
		return getBoolean("is_done");
	}
	
	@Override
	public void setDone(Boolean value) {
		setBoolean("is_done" , value);
	}
	
	@Override
	public Boolean isRequired() {
		return getBoolean("is_required");
	}
	
	@Override
	public void setRequired(Boolean value) {
		setBoolean("is_required" , value);
	}
	
	@Override
	public TextType getDescription() {
		return getApiElement("description", TextTypeImpl.class);
	}
	
	@Override
	public void setDescription(TextType description) {
		setApiElement(description, getOrderMap(), "description");
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
	public List<Step> getStepList() {
		return getApiElementList(new ArrayList<Step>(), "step", StepImpl.class);
	}
	
	@Override
	public void setStepList(List<Step> list) {
		replaceApiList(list, getOrderMap(), "step");
	}
	
	@Override
	public void addStep(Step step) {
		addApiElement(step, getOrderMap());
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return STEP_ORDER;
	}
	
	@Override
	public void addReference(Reference ref) {
		addApiElement(ref, getOrderMap());
	}

}
