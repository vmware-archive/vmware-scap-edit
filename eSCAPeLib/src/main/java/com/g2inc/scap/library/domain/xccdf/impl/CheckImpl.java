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

import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.library.domain.xccdf.Check;
import com.g2inc.scap.library.domain.xccdf.CheckContentRef;
import com.g2inc.scap.library.domain.xccdf.CheckExport;
import com.g2inc.scap.library.domain.xccdf.CheckImport;

public class CheckImpl extends CheckAbstractImpl implements Check {
	
	public final static HashMap<String, Integer> CHECK_ORDER = new HashMap<String, Integer>();
	static {
		CHECK_ORDER.put("check-import", 0);
		CHECK_ORDER.put("check-export", 1);
		CHECK_ORDER.put("check-content-ref", 2);
		CHECK_ORDER.put("check-content", 3);
	}

	public List<CheckImport> getImportList() {
		return getSCAPElementIntList("check-import", CheckImport.class);
	}
	public void setImportList(List<CheckImport> importList) {
		replaceList(importList, CHECK_ORDER, "check-import");
	}
	public List<CheckExport> getExportList() {
		return getSCAPElementIntList("check-export", CheckExport.class);
	}
	public void setExportList(List<CheckExport> exportList) {
		replaceList(exportList, CHECK_ORDER, "check-export");
	}
	public List<CheckContentRef> getCheckContentRefList() {
		return getSCAPElementIntList("check-content-ref", CheckContentRef.class);
	}
	public void setCheckContentRefList(List<CheckContentRef> checkContentRefList) {
		replaceList(checkContentRefList, CHECK_ORDER, "check-content-ref");
	}
	public String getSystem() {
		return this.element.getAttributeValue("system");
	}
	public void setSystem(String system) {
		this.element.setAttribute("system", system);
	}
	
	public void addCheckContentRef(CheckContentRef checkContentRef) {
		insertChild(checkContentRef, CHECK_ORDER, -1);
	}
	
	public void addCheckImport(CheckImport checkImport) {
		insertChild(checkImport, CHECK_ORDER, -1);
	}
	
	public void addCheckExport(CheckExport checkExport) {
		insertChild(checkExport, CHECK_ORDER, -1);
	}

	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return CHECK_ORDER;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( "Check: " );
		List<CheckContentRef> contentRefs = getCheckContentRefList();
		if (contentRefs.size() > 0) {
			CheckContentRef contentRef1 = contentRefs.get(0);
			sb.append("content-ref:" + contentRef1.toString());
		}
		if (contentRefs.size() > 1) {
			sb.append(" + " + (contentRefs.size() - 1) + " more content-refs");
		}
		return sb.toString();
	}
}
