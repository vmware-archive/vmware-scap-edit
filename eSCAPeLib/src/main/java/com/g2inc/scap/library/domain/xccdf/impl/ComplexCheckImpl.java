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

import com.g2inc.scap.library.domain.xccdf.Check;
import com.g2inc.scap.library.domain.xccdf.CheckAbstract;
import com.g2inc.scap.library.domain.xccdf.ComplexCheck;
import com.g2inc.scap.library.domain.xccdf.ComplexCheckOperEnum;

public class ComplexCheckImpl extends CheckAbstractImpl implements ComplexCheck {
	
	public final static HashMap<String, Integer> COMPLEX_CHECK_ORDER = new HashMap<String, Integer>();
	static {
	    COMPLEX_CHECK_ORDER.put("check", 0);
	    COMPLEX_CHECK_ORDER.put("complex-check", 0);
	}

	public List<CheckAbstract> getChildren() {
        List<CheckAbstract> children = new ArrayList<CheckAbstract>();
        List<?> childList = this.element.getChildren();
        for(int i = 0; i < childList.size();i++)    {
            Element elem = (Element) childList.get(i);
            if (elem.getName().equals("check")) {
                Check check = (CheckImpl) createSCAPElement(elem, CheckImpl.class);
                children.add(check);
            } else if (elem.getName().equals("complex-check")) {
                ComplexCheck complexCheck = (ComplexCheck) createSCAPElement(elem, ComplexCheckImpl.class);
                children.add(complexCheck);
            }
        }
        return children;
	}
	
    public void setChildren(List<CheckAbstract> children) {
        this.element.removeChildren("check", element.getNamespace());
        this.element.removeChildren("complex-check", element.getNamespace());
        for (int i=0; i<children.size(); i++) {
            insertChild(children.get(i), COMPLEX_CHECK_ORDER, -1);
        }
    }	
    
	public ComplexCheckOperEnum getOperator() {
		return ComplexCheckOperEnum.valueOf(this.element.getAttributeValue("operator"));
	}
	
	public void setOperator(ComplexCheckOperEnum operator) {
	    this.element.setAttribute("operator", operator.toString());
	}
	
    public void addCheck(Check check) {
        insertChild(check, COMPLEX_CHECK_ORDER, -1);
    }	
    
    public void addComplexCheck(ComplexCheck complexCheck) {
        insertChild(complexCheck, COMPLEX_CHECK_ORDER, -1);
    }       

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( "Complex-Check: " );
		//TODO: fill this in
		return sb.toString();
	}
}
