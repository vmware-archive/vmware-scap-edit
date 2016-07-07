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

import com.g2inc.scap.library.domain.xccdf.Group;
import com.g2inc.scap.library.domain.xccdf.Rule;
import com.g2inc.scap.library.domain.xccdf.SelectableItem;
import com.g2inc.scap.library.domain.xccdf.Value;

public class GroupImpl extends SelectableItemImpl implements Group {
	protected final static HashMap<String, Integer> GROUP_ORDER = new HashMap<String, Integer>();
	static {
		GROUP_ORDER.put("status", 0);
		GROUP_ORDER.put("version", 1);
		GROUP_ORDER.put("title", 2);
		GROUP_ORDER.put("description", 3);
		GROUP_ORDER.put("warning", 4);
		GROUP_ORDER.put("question", 5);
		GROUP_ORDER.put("reference", 6);
		// end of ITEM_ORDER fields
        GROUP_ORDER.put("rationale", 7);
		GROUP_ORDER.put("platform", 8);
		GROUP_ORDER.put("requires", 9);
		GROUP_ORDER.put("conflicts", 10);
		// end of SELECTABLEITEM_ORDER fields
		GROUP_ORDER.put("Value", 11);
		GROUP_ORDER.put("Group", 12);
		GROUP_ORDER.put("Rule", 12);
		GROUP_ORDER.put("signature", 14);
	}
	
	public List<Value> getValueList() {
		return  getSCAPElementIntList("Value", Value.class);
	}

	public void setValueList(List<Value> valueList) {
		replaceList(valueList, getOrderMap(), "Value");
	}

	public List<SelectableItem> getChildren() {
		List<SelectableItem> children = new ArrayList<SelectableItem>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			if (elem.getName().equals("Rule")) {
				RuleImpl rule = (RuleImpl) createSCAPElementInt(elem, Rule.class);
				rule.setRoot(root);
				children.add(rule);
			} else if (elem.getName().equals("Group")) {
				GroupImpl group = (GroupImpl) createSCAPElementInt(elem, Group.class);
				children.add(group);
			}
		}
		return children;
	}

	public void setChildren(List<SelectableItem> children) {
		this.element.removeChildren("Value", element.getNamespace());
		this.element.removeChildren("Rule", element.getNamespace());
		for (int i=0; i<children.size(); i++) {
			insertChild(children.get(i), getOrderMap(), -1);
		}
	}
	
	public List<Rule> getRuleList() {
		List<Rule> ruleList = new ArrayList<Rule>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			if (elem.getName().equals("Rule")) {
				Rule rule = (Rule) createSCAPElementInt(elem, Rule.class);
				rule.setElement(elem);
				ruleList.add(rule);
			} 
		}
		return ruleList;
	}
	public List<Group> getGroupList() {
		List<Group> groupList = new ArrayList<Group>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			if (elem.getName().equals("Group")) {
				Group group = (Group) createSCAPElementInt(elem, Group.class);
				group.setElement(elem);
				groupList.add(group);
			} 
		}
		return groupList;
	}		
	
	public void addToGroupList(List<Group> groupList) {
		groupList.add(this);
		List<Group> childList = getGroupList();
		for(int i = 0; i < childList.size();i++)	{
			Group child = childList.get(i);
			child.addToGroupList(groupList);
		}
	}
	
	public void addToRuleList(List<Rule> ruleList) {
		ruleList.addAll(getRuleList());
		List<Group> childList = getGroupList();
		for(int i = 0; i < childList.size();i++)	{
			Group child = childList.get(i);
			child.addToRuleList(ruleList);
		}
	}	
	
	public void addToValueList(List<Value> valueList) {
		valueList.addAll(getValueList());
		List<Group> childList = getGroupList();
		for(int i = 0; i < childList.size();i++)	{
			Group child = childList.get(i);
			child.addToValueList(valueList);
		}
	}	

	public void addValue(Value value) {
		insertChild(value, getOrderMap(), -1);
	}
	
	public void addRule(Rule rule) {
		insertChild(rule, getOrderMap(), -1);
	}
	
	public void addGroup(Group group) {
		insertChild(group, getOrderMap(), -1);
	}

    @Override
	public String toString()
    {
        List<Group> groupList = getGroupList();

        int size = 0;
        if(groupList != null)
        {
            size = groupList.size();
        }

        return "Group " + getId() + "(" + size + ")";
    }
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return GROUP_ORDER;
	}
}
