package com.g2inc.scap.library.domain.xccdf.impl;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.library.domain.xccdf.Choice;
import com.g2inc.scap.library.domain.xccdf.ChoiceAbstract;
import com.g2inc.scap.library.domain.xccdf.ChoicesElement;

public class ChoicesElementImpl extends SelValueElementImpl implements ChoicesElement {
	public final static HashMap<String, Integer> CHOICES_ORDER = new HashMap<String, Integer>();
	static {
		CHOICES_ORDER.put("choice", 0);
	}
	
	public List<ChoiceAbstract> getChoiceList() {
		List<Choice> choiceList =  getSCAPElementIntList("choice", Choice.class);
		List<ChoiceAbstract> choiceAbstractList = new ArrayList<ChoiceAbstract>();
		Collections.copy(choiceAbstractList, choiceList);
		return choiceAbstractList;
	}

	@Override
	public void setChoiceList(List<ChoiceAbstract> choiceList) {
		replaceList(choiceList, getOrderMap(), "choice");
	}
    
    @Override
 	public boolean isMustMatch() {
		boolean mustMatch = false;
		String selectedString = this.element.getAttributeValue("mustMatch");
		if (selectedString != null
				&& (selectedString.equalsIgnoreCase("true") || selectedString.equals("1"))) {
			mustMatch = true;
		}
		return mustMatch;
	}

    @Override
	public void setMustMatch(boolean mustMatch) {
		element.setAttribute("mustMatch", Boolean.toString(mustMatch));
	}

	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return CHOICES_ORDER;
	}

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String selector = getSelector();
        sb.append("Selector: " + (selector == null ? "none" : selector));
        if (isMustMatch() ) {
            sb.append("; MustMatch");
        }
        String conjunction = "";
        sb.append("; [");
        List<ChoiceAbstract> choiceList = getChoiceList();
        for (ChoiceAbstract choice : choiceList) {
            sb.append(conjunction);
            sb.append(choice.toString());
            conjunction = ";";
        }
        sb.append("]");
        return sb.toString();
    }
}
