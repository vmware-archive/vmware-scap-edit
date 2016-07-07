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
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import com.g2inc.scap.library.domain.xccdf.ChoicesElement;
import com.g2inc.scap.library.domain.xccdf.DefaultElement;
import com.g2inc.scap.library.domain.xccdf.InterfaceHintEnum;
import com.g2inc.scap.library.domain.xccdf.LowerBoundElement;
import com.g2inc.scap.library.domain.xccdf.MatchElement;
import com.g2inc.scap.library.domain.xccdf.SelElement;
import com.g2inc.scap.library.domain.xccdf.SelValueElement;
import com.g2inc.scap.library.domain.xccdf.UpperBoundElement;
import com.g2inc.scap.library.domain.xccdf.Value;
import com.g2inc.scap.library.domain.xccdf.ValueElement;
import com.g2inc.scap.library.domain.xccdf.ValueOperatorEnum;
import com.g2inc.scap.library.domain.xccdf.ValueTypeEnum;

public class ValueImpl extends ItemImpl implements Value {
	
	XCCDFBenchmark benchmark = (XCCDFBenchmark) getSCAPDocument();
	
	public final static HashMap<String, Integer> VALUE_ORDER = new HashMap<String, Integer>();
	static {
		VALUE_ORDER.put("status", 0);
		VALUE_ORDER.put("version", 1);
		VALUE_ORDER.put("title", 2);
		VALUE_ORDER.put("description", 3);
		VALUE_ORDER.put("warning", 4);
		VALUE_ORDER.put("question", 5);
		VALUE_ORDER.put("reference", 6);
		VALUE_ORDER.put("value", 7);
        VALUE_ORDER.put("default", 8);
		VALUE_ORDER.put("match", 9);
		VALUE_ORDER.put("lower-bound", 10);
		VALUE_ORDER.put("upper-bound", 11);
		VALUE_ORDER.put("choices", 12);
		VALUE_ORDER.put("source", 13);
		VALUE_ORDER.put("signature", 14);
	}
		
	public ValueOperatorEnum getOperator() {
		String operString = element.getAttributeValue("operator");
		if(operString != null) {
			return ValueOperatorEnum.getValueOf(operString);
		} else {
			return ValueOperatorEnum.EQUALS;
		}
	}
	
	public void setOperator(ValueOperatorEnum operator) {
		if (operator != null) {
			element.setAttribute("operator", operator.toString());
		} else {
			element.removeAttribute("operator");
		}
	}

	public ValueTypeEnum getType() {
		String typeString = this.element.getAttributeValue("type");
		if(typeString != null) {
			return ValueTypeEnum.getValueOf(typeString);
		} else {
			return ValueTypeEnum.STRING;
		}
	}

	public void setType(ValueTypeEnum type) {
		if(type != null) {
			this.element.setAttribute("type", type.toString());
		} else {
			this.element.removeAttribute("type");
		}
	}

    @Override
	public List<SelValueElement> getValueElementList() {
		return getSCAPElementIntList("value", SelValueElement.class);
	}
    @Override
	public void setValueElementList(List<SelValueElement> selValueElementList) {
		replaceList(selValueElementList, getOrderMap(), "value");
	}
	public void addValueSelector(SelValueElement valueSelector) {
		insertChild(valueSelector, getOrderMap(), -1);
	}
	public List<SelValueElement> getDefaultElementList() {
        return getSCAPElementIntList("default", SelValueElement.class);
	}
	public void setDefaultElementList(List<SelValueElement> defaultList) {
		replaceList(defaultList, getOrderMap(), "default");
	}
	public List<SelValueElement> getMatchList() {
        return getSCAPElementIntList("match", SelValueElement.class);
	}
	public void setMatchList(List<SelValueElement> matchList) {
		replaceList(matchList, getOrderMap(), "match");
	}
	public List<SelValueElement> getLowerBoundList() {
        return getSCAPElementIntList("lower-bound", SelValueElement.class);
	}
	public void setLowerBoundList(List<SelValueElement> lowerBoundList) {
		replaceList(lowerBoundList, getOrderMap(), "lower-bound");
	}
	public List<SelValueElement> getUpperBoundList() {
        return getSCAPElementIntList("upper-bound", SelValueElement.class);
	}
	public void setUpperBoundList(List<SelValueElement> upperBoundList) {
		replaceList(upperBoundList, getOrderMap(), "upper-bound");
	}
	public List<ChoicesElement> getChoiceList() {
        return getSCAPElementIntList("choices", ChoicesElement.class);
	}
	public void setChoiceList(List<ChoicesElement> choiceList) {
		replaceList(choiceList, getOrderMap(), "choices");
	}
	public List<String> getSourceList() {
		List<String> sourceList = new ArrayList<String>();
		List<?> childList = this.element.getChildren("source", element.getNamespace());
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			String uri = elem.getAttributeValue("uri");
			sourceList.add(uri);
		}
		return sourceList;
	}
	public void setSourceList(List<String> sourceList) {
        element.removeChildren("source", element.getNamespace());
        for (String source : sourceList) {
          Element newElement = new Element("source", element.getNamespace());
          newElement.setAttribute("uri", source);
          insertChild(newElement, getOrderMap(), -1);
        }
		replaceStringList(sourceList, getOrderMap(), "source");
	}
    
    @Override
 	public Boolean isInteractive() {
        return getBoolean("interactive");
	}

    @Override
	public void setInteractive(Boolean interactive) {
        setBoolean("interactive", interactive);
	}
    
    @Override
    public InterfaceHintEnum getInterfaceHint() {
		String hintString = this.element.getAttributeValue("interfaceHint");
		if(hintString != null) {
			return InterfaceHintEnum.valueOf(hintString);
		} else {
			return null;
		}
	}

    @Override
	public void setInterfaceHint(InterfaceHintEnum hint) {
		if(hint != null) {
			this.element.setAttribute("interfaceHint", hint.toString());
		} else {
			this.element.removeAttribute("interfaceHint");
		}
	}

    public String toString() {
        return "Value id=" + getId() + ", oper=" + getOperator();
    }

	public HashMap<String, Integer> getOrderMap() {
	  return VALUE_ORDER;
	}
}
