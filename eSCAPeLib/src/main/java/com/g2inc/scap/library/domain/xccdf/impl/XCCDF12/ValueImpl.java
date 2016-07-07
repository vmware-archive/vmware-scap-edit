package com.g2inc.scap.library.domain.xccdf.impl.XCCDF12;
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

import com.g2inc.scap.library.domain.SCAPElementImpl;
import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.library.domain.xccdf.ChoicesElement;
import com.g2inc.scap.library.domain.xccdf.DcStatus;
import com.g2inc.scap.library.domain.xccdf.Metadata;
import com.g2inc.scap.library.domain.xccdf.SelElement;
import com.g2inc.scap.library.domain.xccdf.SelValueElement;
import com.g2inc.scap.library.domain.xccdf.Value;
import com.g2inc.scap.library.domain.xccdf.ValueElement;
import com.g2inc.scap.library.domain.xccdf.impl.SelValueElementImpl;
import com.g2inc.scap.library.domain.xccdf.impl.ValueElementImpl;
import java.util.ArrayList;
import org.jdom.Element;

public class ValueImpl extends com.g2inc.scap.library.domain.xccdf.impl.ValueImpl implements Value {
	
	XCCDFBenchmark benchmark = (XCCDFBenchmark) getSCAPDocument();
	
	public static final String[] VALUE_ELEMENT_NAMES = { "value", "complex-value" };
	public static final String[] DEFAULT_ELEMENT_NAMES = { "default", "complex-default" };
	
	public final static HashMap<String, Integer> VALUE_ORDER_12 = new HashMap<String, Integer>();
	static {
		VALUE_ORDER_12.put("status", 0);
		VALUE_ORDER_12.put("dc-status", 1);
		VALUE_ORDER_12.put("version", 2);
		VALUE_ORDER_12.put("title", 3);
		VALUE_ORDER_12.put("description", 4);
		VALUE_ORDER_12.put("warning", 5);
		VALUE_ORDER_12.put("question", 6);
		VALUE_ORDER_12.put("reference", 7);
		VALUE_ORDER_12.put("metadata", 8);
		// end of Item fields
		VALUE_ORDER_12.put("value", 9);
        VALUE_ORDER_12.put("complex-value", 9);
        VALUE_ORDER_12.put("default", 10);
        VALUE_ORDER_12.put("complex-default", 10);
		VALUE_ORDER_12.put("match", 11);
		VALUE_ORDER_12.put("lower-bound", 12);
		VALUE_ORDER_12.put("upper-bound", 13);
		VALUE_ORDER_12.put("choices", 14);
		VALUE_ORDER_12.put("source", 15);
		VALUE_ORDER_12.put("signature", 16);
	}
    
    @Override
    public List<SelValueElement> getValueElementList() {
        return getValueElementList(VALUE_ELEMENT_NAMES[0], VALUE_ELEMENT_NAMES[1]);
    }
	
    @Override
	public void setValueElementList(List<SelValueElement> valueElementList) {
		replaceList(valueElementList, getOrderMap(), VALUE_ELEMENT_NAMES);
	}
    
    @Override
    public List<SelValueElement> getDefaultElementList() {
    	return getValueElementList(DEFAULT_ELEMENT_NAMES[0], DEFAULT_ELEMENT_NAMES[1]);
    }
    @Override
	public void setDefaultElementList(List<SelValueElement> valueElementList) {
		replaceList(valueElementList, getOrderMap(), DEFAULT_ELEMENT_NAMES);
	}
	
    public List<DcStatus> getDcStatusList() {
    	return getSCAPElementIntList("dc-status", DcStatus.class);
    }
    public void setDcStatusList(List<DcStatus> dcStatusList) {
    	replaceList(dcStatusList, getOrderMap(), "dc-status");
    }
    
    public List<Metadata> getMetadataList() {
    	return getSCAPElementIntList("metadata", Metadata.class);
    }
    public void setMetadataList(List<Metadata> metadataList) {
    	replaceList(metadataList, getOrderMap(), "metadata");
    }
    
    private List<SelValueElement> getValueElementList(String simpleElementName, String complexElementName) {
        List<SelValueElement> valueElementList = new ArrayList<SelValueElement>();
        List<Element> childList = this.element.getChildren();
        for (Element child : childList) {
        	SelValueElement valueElement = null;
            if (child.getName().equals(simpleElementName)) {
                valueElement = new SelValueElementImpl();
            } else if (child.getName().equals(complexElementName)) {
                valueElement = new SelComplexValueElementImpl();
            } else {
                continue;
            }
            valueElement.setElement(child);
            valueElement.setRoot(root);
            valueElement.setDoc(doc);
            valueElement.setSCAPDocument(SCAPDocument);
            valueElementList.add(valueElement);
        }
        return valueElementList;
    }

	public HashMap<String, Integer> getOrderMap() {
	  return VALUE_ORDER_12;
	}
}
