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

import com.g2inc.scap.library.domain.xccdf.DcStatus;
import com.g2inc.scap.library.domain.xccdf.Group;
import com.g2inc.scap.library.domain.xccdf.Metadata;

public class GroupImpl extends com.g2inc.scap.library.domain.xccdf.impl.GroupImpl implements Group {
	public final static HashMap<String, Integer> GROUP_ORDER_12 = new HashMap<String, Integer>();
	static {
		GROUP_ORDER_12.put("status", 0);
		GROUP_ORDER_12.put("dc-status", 1);
		GROUP_ORDER_12.put("version", 2);
		GROUP_ORDER_12.put("title", 3);
		GROUP_ORDER_12.put("description", 4);
		GROUP_ORDER_12.put("warning", 5);
		GROUP_ORDER_12.put("question", 6);
		GROUP_ORDER_12.put("reference", 7);
		GROUP_ORDER_12.put("metadata", 8);
		// end of Item fields
		// end of ITEM_ORDER fields
        GROUP_ORDER_12.put("rationale", 9);
		GROUP_ORDER_12.put("platform", 10);
		GROUP_ORDER_12.put("requires", 11);
		GROUP_ORDER_12.put("conflicts", 12);
		// end of SELECTABLEITEM_ORDER fields
		GROUP_ORDER_12.put("Value", 13);
		GROUP_ORDER_12.put("Group", 14);
		GROUP_ORDER_12.put("Rule", 15);
		GROUP_ORDER_12.put("signature", 16);
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
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return GROUP_ORDER_12;
	}
}
