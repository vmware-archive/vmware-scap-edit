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

import java.math.BigDecimal;
import java.util.HashMap;

import com.g2inc.scap.model.ocil.TextType;
import com.g2inc.scap.model.ocil.Variable;
import com.g2inc.scap.model.ocil.VariableDataType;

public class VariableImpl extends ItemBaseImpl implements Variable {
	
	public final static HashMap<String, Integer> VARIABLE_ORDER = new HashMap<String, Integer>();
	static {
		VARIABLE_ORDER.put("notes", 0);
		VARIABLE_ORDER.put("description", 1);
	}
	
	@Override
	public VariableDataType getDatatype() {
		// datatype attribute is required, not checking for null
		return VariableDataType.valueOf(element.getAttributeValue("datatype"));
	}
	
	@Override
	public void setDatatype(VariableDataType datatype) {
		element.setAttribute("datatype", datatype.name());
	}	
	
	@Override
	public TextType getDescription() {
		return getApiElement("description", TextTypeImpl.class);
	}
	
	@Override
	public void setDescription(TextType description) {
		setApiElement((TextTypeImpl)description, getOrderMap(), "description");
	}
		
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return VARIABLE_ORDER;
	}
	
	protected Object getValue(String tagName) {
		Object value = null;
		String stringValue = getChildStringValue(tagName);
		if (this.getDatatype() != null && this.getDatatype() == VariableDataType.NUMERIC) {
			if (stringValue != null) {
				try {
					value = new BigDecimal(stringValue);
				} catch (NumberFormatException e) {
					System.out.println(element.getName() + " "
							+ (getId() == null ? "" : " " + getId()) 
							+ " field " + tagName 
							+ " should be numeric but is not in valid decimal format: " + stringValue);
					throw e;
				}
			}			
		} else {
			value = stringValue;
		}
		return value;
	}
	
	protected void setValue(String tagName, Object value) {
		element.removeChildren(tagName);
		if (value != null) {
			String stringValue = null;
			if (value instanceof BigDecimal) {
				stringValue = ((BigDecimal) value).toPlainString();
			} else {
				stringValue = value.toString();
			}
			setStringValueChild(tagName, stringValue, getOrderMap());
		}		
	}
	
//	@Override
	public void setId(int idNum) {
		setId("ocil:" + ocilDocument.getIdNamespace() + ":variable:" + idNum);
	}

}
