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

import com.g2inc.scap.library.domain.xccdf.Conflicts;
import com.g2inc.scap.library.domain.xccdf.Rationale;
import com.g2inc.scap.library.domain.xccdf.Requires;
import com.g2inc.scap.library.domain.xccdf.SelectableItem;

public abstract class SelectableItemImpl extends ItemImpl implements SelectableItem {
//	private Boolean selected = null;
//	private Double weight = null;
//
//	private List<String> rationaleList;
//	private List<Requires> requiresList;
//	private List<Conflicts> conflictsList;
	
	public final static HashMap<String, Integer> SELECTABLEITEM_ORDER = new HashMap<String, Integer>();
	static {
		SELECTABLEITEM_ORDER.put("status", 0);
		SELECTABLEITEM_ORDER.put("version", 1);
		SELECTABLEITEM_ORDER.put("title", 2);
		SELECTABLEITEM_ORDER.put("description", 3);
		SELECTABLEITEM_ORDER.put("warning", 4);
		SELECTABLEITEM_ORDER.put("question", 5);
		SELECTABLEITEM_ORDER.put("reference", 6);
		// end of ITEM_ORDER fields
        SELECTABLEITEM_ORDER.put("rationale", 7);
		SELECTABLEITEM_ORDER.put("platform", 8);
		SELECTABLEITEM_ORDER.put("requires", 9);
		SELECTABLEITEM_ORDER.put("conflicts", 10);
	}

	public boolean isSelected() {
		// default is true!
		boolean selected = true;
		String selectedString = this.element.getAttributeValue("selected");
		if (selectedString != null
				&& (selectedString.equalsIgnoreCase("false") || selectedString.equals("0"))) {
			selected = false;
		}
		return selected;
	}

	public void setSelected(boolean selected) {
		if (selected ^ isSelected()) {
			element.setAttribute("selected", Boolean.toString(selected));
		}
	}

	public Boolean getSelected() {
		// default is true!
		Boolean selected = Boolean.TRUE;
		String selectedString = this.element.getAttributeValue("selected");
		if (selectedString != null) {
			selectedString = selectedString.trim();
			if (selectedString.equalsIgnoreCase("false") || selectedString.equals("0")) {
				selected = Boolean.FALSE;
			} else {
				selected = Boolean.TRUE;
			}
		}
		return selected;
	}
	public void setSelected(Boolean selected) {
        this.element.setAttribute("selected", selected.toString());
	}
	public Double getWeight() {
		Double weight = null;
		String weightString = this.element.getAttributeValue("weight");
		if (weightString == null) {
			weight = new Double(1.0);
		} else {
			weight = Double.parseDouble(weightString);
		}
		return weight;
	}
	public String getWeightAsString() {
		return this.element.getAttributeValue("weight");
	}
	public void setWeight(Double weight) {
		String weightString = null;
		if (weight != null) {
			weightString = weight.toString();
		}
		this.element.setAttribute("weight", weightString);
	}
	public List<Rationale> getRationaleList() {
		return getSCAPElementIntList("rationale", Rationale.class);
	}
	public void setRationaleList(List<Rationale> rationaleList) {
		replaceList(rationaleList, getOrderMap(), "rationale");
	}
	public List<String> getPlatformList() {		
		List<String> platformList = new ArrayList<String>();
		List<?> childList = this.element.getChildren("platform", element.getNamespace());
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			String platform = elem.getAttributeValue("idref");
			platformList.add(platform);
		}
		return platformList;
	}
	public void setPlatformList(List<String> platformList) {
		element.removeChildren("platform", element.getNamespace());
		for (int i=0; i<platformList.size(); i++) {
			Element newElement = new Element("platform", element.getNamespace());
			newElement.setAttribute("idref", platformList.get(i));
			insertChild(newElement, getOrderMap(), -1);
		}
	}
	public List<Requires> getRequiresList() {
		return getSCAPElementIntList("requires", Requires.class);
	}
	public void setRequiresList(List<Requires> requiresList) {
		replaceList(requiresList, getOrderMap(), "requires");
	}
	public List<Conflicts> getConflictsList() {
		return getSCAPElementIntList("conflicts", Conflicts.class);
	}
	public void setConflictsList(List<Conflicts> conflictsList) {
		replaceList(conflictsList, getOrderMap(), "conflicts");
	}
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return SELECTABLEITEM_ORDER;
	}
}
