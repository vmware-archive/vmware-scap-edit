package com.g2inc.scap.library.domain.xccdf.impl.XCCDF12;

import com.g2inc.scap.library.domain.xccdf.ComplexValueElement;
import com.g2inc.scap.library.domain.xccdf.impl.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jdom.Element;
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

public class ComplexValueElementImpl extends ValueElementImpl implements ComplexValueElement {
    
    public final static HashMap<String, Integer> COMPLEX_VALUE_ELEMENT_ORDER_12 = new HashMap<String, Integer>();
	static {
		COMPLEX_VALUE_ELEMENT_ORDER_12.put("item", 0);
	}

    @Override
    public List<String> getItemList() {
        List<String> itemList = new ArrayList<String>();
		List<?> childList = this.element.getChildren("item", element.getNamespace());
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			String valueString = elem.getTextTrim();
			itemList.add(valueString);
		}
		return itemList;
    }

    @Override
    public void setItemList(List<String> list) {
        replaceStringList(list, getOrderMap(), "item");
    }
    
    @Override
    public Object getValue() {
		return getItemList();
	}
    
    @Override
	public void setValue(Object value) {
		if (value instanceof List) {
            setItemList((List<String>) value);
        } else {
            throw new IllegalArgumentException("setValue called for ComplexValueElement must pass in List<String>, actual type is " + value.getClass().getName());
        }
	}
    
    @Override
    public HashMap<String, Integer> getOrderMap() {
        return COMPLEX_VALUE_ELEMENT_ORDER_12;
    }
    
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		String conjunction = "";
		for (String item : getItemList()) {
			sb.append(conjunction + item);
			conjunction = ", ";
		}
		sb.append("]");
		return sb.toString();
	}


}
