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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Namespace;

import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.SCAPElement;
import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.xccdf.ChoicesElement;
import com.g2inc.scap.library.domain.xccdf.ComplexChoice;
import com.g2inc.scap.library.domain.xccdf.ComplexValueElement;
import com.g2inc.scap.library.domain.xccdf.DcStatus;
import com.g2inc.scap.library.domain.xccdf.Group;
import com.g2inc.scap.library.domain.xccdf.Item;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.Rule;
import com.g2inc.scap.library.domain.xccdf.SelComplexValueElement;
import com.g2inc.scap.library.domain.xccdf.Value;

public class XCCDFBenchmark extends com.g2inc.scap.library.domain.xccdf.impl.XCCDFBenchmark {
	
	private static final Logger LOG = Logger.getLogger(XCCDFBenchmark.class);
	
    public static final Map<Class<? extends SCAPElement>, Class<? extends SCAPElementImpl> > TYPE_MAP_V12 =
            new HashMap<Class<? extends SCAPElement>, Class<? extends SCAPElementImpl>>();
    static {   	
    	TYPE_MAP_V12.putAll(TYPE_MAP);
    	TYPE_MAP_V12.put(DcStatus.class, DcStatusImpl.class);
        TYPE_MAP_V12.put(Group.class, GroupImpl.class);
        TYPE_MAP_V12.put(Item.class, ItemImpl.class);
        TYPE_MAP_V12.put(Profile.class, ProfileImpl.class);
        TYPE_MAP_V12.put(Rule.class, RuleImpl.class);
        TYPE_MAP_V12.put(Value.class, ValueImpl.class);
        TYPE_MAP_V12.put(ComplexValueElement.class, ComplexValueElementImpl.class);
        TYPE_MAP_V12.put(SelComplexValueElement.class, SelComplexValueElementImpl.class);
        TYPE_MAP_V12.put(ChoicesElement.class, ChoicesElementImpl.class);
        TYPE_MAP_V12.put(ComplexChoice.class, ComplexChoiceImpl.class);
    }
    
	public final static HashMap<String, Integer> BENCHMARK_ORDER_12 = new HashMap<String, Integer>();
	static {
		BENCHMARK_ORDER_12.put("status", 0);
		BENCHMARK_ORDER_12.put("dc-status", 1);
		BENCHMARK_ORDER_12.put("title", 2);
		BENCHMARK_ORDER_12.put("description", 3);
		BENCHMARK_ORDER_12.put("notice", 4);
		BENCHMARK_ORDER_12.put("front-matter", 5);
		BENCHMARK_ORDER_12.put("rear-matter", 6);
		BENCHMARK_ORDER_12.put("reference", 7);
		BENCHMARK_ORDER_12.put("plain-text", 8);
		BENCHMARK_ORDER_12.put("platform-specification", 9);
		BENCHMARK_ORDER_12.put("platform", 10);
		BENCHMARK_ORDER_12.put("version", 11);
		BENCHMARK_ORDER_12.put("metadata", 12);
		BENCHMARK_ORDER_12.put("model", 13);
		BENCHMARK_ORDER_12.put("Profile", 14);
		BENCHMARK_ORDER_12.put("Value", 15);
		BENCHMARK_ORDER_12.put("Group", 16);
		BENCHMARK_ORDER_12.put("Rule", 16);
		// Note: TestResult complex type not modeled yet, it will go here
		BENCHMARK_ORDER_12.put("signature", 17);
	}
    
	public XCCDFBenchmark(Document d) {
		super(d);
        setDocumentType(SCAPDocumentTypeEnum.XCCDF_12);
	}
    
    @Override
    public Group createGroup() {
		return (Group) createSCAPElement("Group", GroupImpl.class);
    }
    
    @Override
    public Profile createProfile() {
    	return (Profile) createSCAPElement("Profile", ProfileImpl.class);	
    }	
    
    @Override
	public Rule createRule() {
		return (Rule) createSCAPElement("Rule", RuleImpl.class);
	}
    
    @Override
	public Value createValue() {
		return (Value) createSCAPElement("Value", ValueImpl.class);	
	}
    
    @Override
    public List<DcStatus> getDcStatusList() {
    	return getSCAPElementIntList("dc-status", DcStatus.class);
    }
    
    @Override
    public void setDcStatusList(List<DcStatus> dcStatusList) {
    	replaceList(dcStatusList, getOrderMap(), "dc-status");
    }
    
    @Override
    public ChoicesElement createChoicesElement() {
    	return (ChoicesElement) createSCAPElement("choices", ChoicesElementImpl.class);
    }
	
	@Override
	public String getSchemaVersion() {
		return "1.2";
	}
	
	@Override
	public Namespace getNamespace() {
		return XCCDF12_NAMESPACE;
	}
	
	/**
	 * Return the implementation class for a given interface; used to produce SCAPElements or 
	 * Lists of SCAPElements.
	 * 
	 * @param intClass Class of interface, eg, Group.class
	 * @return Class of corresponding implementation, eg, Group.class
	 */
	@Override
	public Class<? extends SCAPElementImpl> getImplClass(Class<? extends SCAPElement> intClass) {
		return TYPE_MAP_V12.get(intClass);
	}
        
    @Override
    public DcStatus createDcStatus() {
        return (DcStatus) createSCAPElement("dc-status", DcStatusImpl.class);
    }
    
    @Override
    public SelComplexValueElement createSelComplexValueElement(String elementName) {
    	return (SelComplexValueElement) createSCAPElement(elementName, SelComplexValueElementImpl.class);
    }
    
	@Override
	public ComplexChoice createComplexChoice() {
		return (ComplexChoice) createSCAPElement("complex-choice", ComplexChoiceImpl.class);
	}
    
    @Override
    public HashMap<String, Integer> getOrderMap() {
	  return BENCHMARK_ORDER_12;
	}


}
