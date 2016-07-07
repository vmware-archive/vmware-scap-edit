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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import com.g2inc.scap.library.domain.Anomaly;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentClassEnum;
import com.g2inc.scap.library.domain.SCAPElement;
import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.cpelang.FactRef;
import com.g2inc.scap.library.domain.cpelang.LogicalTest;
import com.g2inc.scap.library.domain.cpelang.Platform;
import com.g2inc.scap.library.domain.cpelang.PlatformSpecification;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.xccdf.*;


/**
 * Represents an XCCDF benchmark xml document.
 */
public abstract class XCCDFBenchmark extends SCAPDocument implements com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark
{
	
	private static final Logger LOG = Logger.getLogger(XCCDFBenchmark.class);
	
    public static final Map<Class<? extends SCAPElement>, Class<? extends SCAPElementImpl> > TYPE_MAP =
            new HashMap<Class<? extends SCAPElement>, Class<? extends SCAPElementImpl>>();
    static {
        TYPE_MAP.put(CheckContentRef.class, CheckContentRefImpl.class);
        TYPE_MAP.put(CheckExport.class, CheckExportImpl.class);
        TYPE_MAP.put(Check.class, CheckImpl.class);
        TYPE_MAP.put(CheckImport.class, CheckImportImpl.class);
        TYPE_MAP.put(ChoicesElement.class, ChoicesElementImpl.class);
        TYPE_MAP.put(ComplexCheck.class, ComplexCheckImpl.class);
        TYPE_MAP.put(Conflicts.class, ConflictsImpl.class);
        TYPE_MAP.put(DefaultElement.class, DefaultElementImpl.class);
        TYPE_MAP.put(Description.class, DescriptionImpl.class);
        TYPE_MAP.put(DublinCoreElement.class, DublinCoreElementImpl.class);
        TYPE_MAP.put(Fix.class, FixImpl.class);
        TYPE_MAP.put(FixInstance.class, FixInstanceImpl.class);
        TYPE_MAP.put(FixText.class, FixTextImpl.class);
        TYPE_MAP.put(FrontMatter.class, FrontMatterImpl.class);
        TYPE_MAP.put(Group.class, GroupImpl.class);
        TYPE_MAP.put(HtmlText.class, HtmlTextImpl.class);
        TYPE_MAP.put(HtmlWithSub.class, HtmlWithSubImpl.class);
        TYPE_MAP.put(Ident.class, IdentImpl.class);
        TYPE_MAP.put(IdRefElement.class, IdRefElementImpl.class);
        TYPE_MAP.put(Item.class, ItemImpl.class);
        TYPE_MAP.put(LowerBoundElement.class, LowerBoundElementImpl.class);
        TYPE_MAP.put(MatchElement.class, MatchElementImpl.class);
        TYPE_MAP.put(Metadata.class, MetadataImpl.class);
        TYPE_MAP.put(Model.class, ModelImpl.class);
        TYPE_MAP.put(Notice.class, NoticeImpl.class);
        TYPE_MAP.put(OverrideableSCAPElement.class, OverrideableSCAPElementImpl.class);
        TYPE_MAP.put(PlainText.class, PlainTextImpl.class);
        TYPE_MAP.put(ProfileChild.class, ProfileChildImpl.class);
        TYPE_MAP.put(Profile.class, ProfileImpl.class);
        TYPE_MAP.put(ProfileRefineRule.class, ProfileRefineRuleImpl.class);
        TYPE_MAP.put(ProfileRefineValue.class, ProfileRefineValueImpl.class);
        TYPE_MAP.put(ProfileSelect.class, ProfileSelectImpl.class);
        TYPE_MAP.put(ProfileSetValue.class, ProfileSetValueImpl.class);
        TYPE_MAP.put(Question.class, QuestionImpl.class);
        TYPE_MAP.put(Rationale.class, RationaleImpl.class);
        TYPE_MAP.put(RearMatter.class, RearMatterImpl.class);
        TYPE_MAP.put(Reference.class, ReferenceImpl.class);
        TYPE_MAP.put(Remark.class, RemarkImpl.class);
        TYPE_MAP.put(Requires.class, RequiresImpl.class);
        TYPE_MAP.put(Rule.class, RuleImpl.class);
        TYPE_MAP.put(SelectableItem.class, SelectableItemImpl.class);
        TYPE_MAP.put(SelValueElement.class, SelValueElementImpl.class);
        TYPE_MAP.put(Status.class, StatusImpl.class);
        TYPE_MAP.put(TextWithSub.class, TextWithSubImpl.class);
        TYPE_MAP.put(Title.class, TitleImpl.class);
        TYPE_MAP.put(UpperBoundElement.class, UpperBoundElementImpl.class);
        TYPE_MAP.put(ValueElement.class, ValueElementImpl.class);
        TYPE_MAP.put(Value.class, ValueImpl.class);
        TYPE_MAP.put(Version.class, VersionImpl.class);
        TYPE_MAP.put(Warning.class, WarningImpl.class);
    }
	
	private List<Profile> profileList;
	public final static HashMap<String, Integer> BENCHMARK_ORDER = new HashMap<String, Integer>();
	static {
		BENCHMARK_ORDER.put("status", 0);
		BENCHMARK_ORDER.put("title", 1);
		BENCHMARK_ORDER.put("description", 2);
		BENCHMARK_ORDER.put("notice", 3);
		BENCHMARK_ORDER.put("front-matter", 4);
		BENCHMARK_ORDER.put("rear-matter", 5);
		BENCHMARK_ORDER.put("reference", 6);
		BENCHMARK_ORDER.put("plain-text", 7);
		BENCHMARK_ORDER.put("cpe-list", 8);
		BENCHMARK_ORDER.put("platform-specification", 8);
		BENCHMARK_ORDER.put("platform", 9);
		BENCHMARK_ORDER.put("version", 10);
		BENCHMARK_ORDER.put("metadata", 11);
		BENCHMARK_ORDER.put("model", 12);
		BENCHMARK_ORDER.put("Profile", 13);
		BENCHMARK_ORDER.put("Value", 14);
		BENCHMARK_ORDER.put("Group", 15);
		BENCHMARK_ORDER.put("Rule", 16);
		// Note: TestResult complex type not modeled yet, it will go here
		BENCHMARK_ORDER.put("signature", 17);
	}

	/**
	 * Constructor using a JDOM document.
	 * 
	 * @param doc A JDOM document
	 */
	public XCCDFBenchmark(Document doc) {
		super(doc);
        setDocumentClass(SCAPDocumentClassEnum.XCCDF);

		// handle required (minOccurs="1") elements in Benchmark; if not present, make something up, so it will validate
		List<Status> statusList = getStatusList();
		if (statusList.size() == 0) {
			Status status = createStatus();
			status.setDate(new Date());
			status.setStatus(StatusEnum.draft);		
			statusList.add(status);
			setStatusList(statusList);
		}
		Version version = getVersion();
		if (version == null) {
			version = createVersion();
			version.setVersion("v0.0");
			setVersion(version);
		}
	}
	
	/**
	 * Get the list of profiles in this benchmark.
	 * 
	 * @return List<Profile>
	 */
	public List<Profile> getProfileList() {
		profileList = new ArrayList<Profile>();
		List<?> childList = this.element.getChildren("Profile", getNamespace());		
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			Profile profile = createProfile();
			profile.setElement(elem);
			profileList.add(profile);
		}
		return profileList;
	}
	
	/**
	 * Get this status list for this benchmark.
	 * 
	 * @return List<Status>
	 */
	public List<Status> getStatusList() {
		return getSCAPElementIntList("status", Status.class);
	}
	public void setStatusList(List<Status> statusList) {
		replaceList(statusList, getOrderMap(), "status");
	}
	public void addStatus(Status status) {
		insertChild(status, getOrderMap(), -1);
	}
	public void setStatus(Status status) {
		ArrayList<Status> list = new ArrayList<Status>();
		list.add(status);
		setStatusList(list);
	}
	public List<Description> getDescriptionList() {
		return getSCAPElementIntList("description", Description.class);
	}
	public void setDescriptionList(List<Description> descriptionList) {
		replaceList(descriptionList, getOrderMap(), "description");
	}
    public String getDescriptionAsString() {
        List<Description> descList = getDescriptionList();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<descList.size(); i++) {
            sb.append(descList.get(i).toStringWithHtml());
        }
        return sb.toString();
    }
    public void setDescriptionAsString(String description) {
        if (description != null) {
            List<Description> descriptionList = new ArrayList<Description>();
            Description desc = createDescription();
            desc.setElementFromStringWithHtml("description", description);
            descriptionList.add(desc);
            setDescriptionList(descriptionList);
        }
    }
	public String getTitle() {
		Element titleElement = this.element.getChild("title", getNamespace());
		return titleElement == null ? null : titleElement.getValue();
	}
	public void setTitle(String titleText) {
		Title title = createTitle();
		title.setText(titleText);
		List<Title> titleList = new ArrayList<Title>();
		titleList.add(title);
		setTitleList(titleList);
	}
	
	public List<Notice> getNoticeList() {
		return getSCAPElementIntList("notice", Notice.class);
	}
	public void setNoticeList(List<Notice> list) {
		replaceList(list, getOrderMap(), "notice");
	}

	public Notice getNotice() {
		Notice notice = null;
		List<Notice> list = getNoticeList();
		if (list != null && list.size() > 0) {
			notice = list.get(0);
		}
		return notice;
	}
	public void setNotice(Notice notice) {
		List<Notice> list = new ArrayList<Notice>();
		list.add(notice);
		setNoticeList(list);
	}
	
	public List<Model> getModelList() {
		return getSCAPElementIntList("model", Model.class);
	}
	public void setModelList(List<Model> list) {
		replaceList(list, getOrderMap(), "model");
	}

	public Version getVersion() {
		return getSCAPElementInt("version", Version.class);
	}
	
	public void setVersion(Version version) {
		Element versionElement = this.element.getChild("version", getNamespace());
		if (versionElement != null) {
			// maxOccurs for version element is 1, so remove old version first
			this.element.removeContent(versionElement);
		}
		insertChild(version, getOrderMap(), -1);
	}
	public List<Reference> getReferenceList() {
		return getSCAPElementIntList("reference", Reference.class);
	}
	public void setReferenceList(List<Reference> referenceList) {
		replaceList(referenceList, getOrderMap(), "reference");
	}
	public List<PlainText> getPlainTextList() {
		return getSCAPElementIntList("plain-text", PlainText.class);
	}
	public void setPlainTextList(List<PlainText> list) {
		replaceList(list, getOrderMap(), "plain-text");
	}
	public String getPlainText(String id) {
		String text = null;
		List<PlainText> list = getPlainTextList();
		for (int i=0; i<list.size(); i++) {
			PlainText plainText = list.get(i);
			if (plainText.getId().equals(id)) {
				text = plainText.getText();
				break;
			}
		}
		return text;
	}
	public List<String> getPlatformList() {		
		List<String> platformList = new ArrayList<String>();
		List<?> childList = this.element.getChildren("platform", getNamespace());
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			String platform = elem.getAttributeValue("idref");
			if (platform != null && platform.trim().length() > 0) {
				platformList.add(platform);
			}
		}
		return platformList;
	}
	public void setPlatformList(List<String> platformList) {
		element.removeChildren("platform", getNamespace());
		for (int i=0; i<platformList.size(); i++) {
			Element newElement = new Element("platform", getNamespace());
			newElement.setAttribute("idref", platformList.get(i));
			insertChild(newElement, getOrderMap(), -1);
		}
	}
	public List<Value> getValueList() {
		return getSCAPElementIntList("Value", Value.class);
	}
	public void setValueList(List<Value> valueList) {
		replaceList(valueList, getOrderMap(), "Value");
	}	
	public void addValue(Value value) {
		insertChild(value, getOrderMap(), -1);
	}	
	public List<SelectableItem> getSelectableItemList() {
		List<SelectableItem> selItemList = new ArrayList<SelectableItem>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			if (elem.getName().equals("Rule")) {
				Rule rule = createRule();
				rule.setElement(elem);
				selItemList.add(rule);
			} else if (elem.getName().equals("Group")) {
				Group group = createGroup();
				group.setElement(elem);
				selItemList.add(group);
			}
		}
		return selItemList;
	}
	public void setSelectablItemList(List<SelectableItem> selItemList) {
		element.removeChildren("Group", getNamespace());
		element.removeChildren("Rule", getNamespace());
		replaceList(selItemList, getOrderMap(), (String) null); 
	}
	public List<Rule> getRuleList() {
		List<Rule> ruleList = new ArrayList<Rule>();
		List<?> childList = this.element.getChildren();
		for(int i = 0; i < childList.size();i++)	{
			Element elem = (Element) childList.get(i);
			if (elem.getName().equals("Rule")) {
				Rule rule = createRule();
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
				Group group = createGroup();
				group.setElement(elem);
				groupList.add(group);
			} 
		}
		return groupList;
	}	
	public List<Group> getAllGroups() {
		List<Group> groupList = new ArrayList<Group>();
		List<Group> childList = getGroupList();
		for(int i = 0; i < childList.size();i++)	{
			Group group = childList.get(i);
			group.addToGroupList(groupList);
		}
		return groupList;
	}		
	public List<Rule> getAllRules() {
		List<Rule> ruleList = getRuleList();
		List<Group> groupList = getGroupList();
		for(int i = 0; i < groupList.size();i++) {
			groupList.get(i).addToRuleList(ruleList);
		}
		return ruleList;
	}
	public List<Value> getAllValues() {
		List<Value> valueList = getValueList();
		List<Group> groupList = getGroupList();
		for(int i = 0; i < groupList.size();i++) {
			groupList.get(i).addToValueList(valueList);
		}
		return valueList;
	}	
	
	
	public List<FrontMatter> getFrontMatterList() {
		return getSCAPElementIntList("front-matter", FrontMatter.class);
	} 
	public void setFrontMatterList(List<FrontMatter> frontMatterList) {
		replaceList(frontMatterList, getOrderMap(), "front-matter");
	}
	
	public List<RearMatter> getRearMatterList() {
		return getSCAPElementIntList("rear-matter", RearMatter.class);
	} 
	public void setRearMatterList(List<RearMatter> rearMatterList) {
		replaceList(rearMatterList, getOrderMap(), "rear-matter");
	}
	
	public PlatformSpecification getPlatformSpecification() {
		return getSCAPElementInt("platform-specification", PlatformSpecification.class, SCAPElementImpl.CPE_LANG_NAMESPACE);
	}
	public void setPlatformSpecification(PlatformSpecification spec) {
		element.removeChildren("platform-specification", SCAPElementImpl.CPE_LANG_NAMESPACE);
		insertChild(spec, getOrderMap(), -1);
	}
	public List<Metadata> getMetadataList() {
		return getSCAPElementIntList("metadata", Metadata.class);
	}
        public void setMetadataList(List<Metadata> list) {
            replaceList(list, getOrderMap(), "metadata");
        }
	
	@Override
	public boolean equals(Object o)
	{
		// TODO: Make this more robust by include things like version
		boolean equal = false;
		if(o == null)
		{
			return equal;
		}
		if(o instanceof XCCDFBenchmark)
		{
			XCCDFBenchmark other = (XCCDFBenchmark)o;
			if(getId() != null && other.getId() != null)
			{
				return getId().equals(other.getId());
			}
		}
		return equal;
	}

    /**
     * Returns whether the given profile exists in this benchmark.
     * 
     * @param profName The name of a profile
     * @return boolean
     */
    public boolean containsProfile(String profName)
    {
        boolean ret = false;

        if(profName == null || profName.length() == 0)
        {
            return ret;
        }
        
        List<Profile> profs = getProfileList();

        if(profs != null && profs.size() > 0)
        {
            for(int x = 0; x < profs.size(); x++)
            {
                Profile p = profs.get(x);

                if(p.getId() != null && p.getId().equals(profName))
                {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    /**
     * Returns whether the given rule exists in this benchmark.
     *
     * @param ruleName The name of a rule
     * @return boolean
     */
    public boolean containsRule(String ruleName)
    {
        boolean ret = false;

        if(ruleName == null || ruleName.length() == 0)
        {
            return ret;
        }

        List<Rule> rules = getAllRules();

        if(rules != null && rules.size() > 0)
        {
            for(int x = 0; x < rules.size(); x++)
            {
                Rule r = rules.get(x);

                if(r.getId() != null && r.getId().equals(ruleName))
                {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }
    
    /**
     * Returns whether the given value exists in this benchmark.
     *
     * @param valueName The name of a value
     * @return boolean
     */
    public boolean containsValue(String valueName)
    {
        boolean ret = false;

        if(valueName == null || valueName.length() == 0)
        {
            return ret;
        }

        List<Value> values = getAllValues();

        if(values != null && values.size() > 0)
        {
            for(int x = 0; x < values.size(); x++)
            {
                Value v = values.get(x);

                if(v.getId() != null && v.getId().equals(valueName))
                {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    /**
     * Call this method to make sure that an id you want to use doesn't already exist in the benchmark.
     * All ids must be unique.  Returns true if the id is being used as an identifier somewhere in the document
     * or false otherwise.
     * 
     * @param id Id you want to use
     * @return boolean
     */
    public boolean containsId(String id)
    {
        boolean ret = false;

        if(getId() != null && getId().equals(id))
        {
            return true;
        }

        if(containsProfile(id))
        {
            return true;
        }

        if(containsGroup(id))
        {
            return true;
        }

        if(containsRule(id))
        {
            return true;
        }

        if(containsValue(id))
        {
            return true;
        }

        return ret;
    }

    /**
     * Returns whether the given group exists in this benchmark.
     *
     * @param groupName The name of a group
     * @return boolean
     */
    public boolean containsGroup(String groupName)
    {
        boolean ret = false;

        if(groupName == null || groupName.length() == 0)
        {
            return ret;
        }

        List<Group> groups = getAllGroups();

        if(groups != null && groups.size() > 0)
        {
            for(int x = 0; x < groups.size(); x++)
            {
                Group r = groups.get(x);

                if(r.getId() != null && r.getId().equals(groupName))
                {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

	@Override
	public String toString() {
		return "Benchmark id=" + getId();
	}
	public String getSchemaVersion() {
		return null;
	}
	
	public void addProfile(Profile profile) {
		// assume at end of existing Profiles for now
		insertChild(profile, getOrderMap(), -1);
	}
	
	public void addGroup(Group group) {
		// assume at end of existing SelectableItems 
		insertChild(group, getOrderMap(), -1);
	}
	
	public void addRule(Rule rule) {
		// assume at end of existing SelectableItems 
		insertChild(rule, getOrderMap(), -1);
	}
	
	public Profile findProfile(String profileId) {
		Profile profile = null;
		getProfileList();
		for (int i=0; i<profileList.size(); i++) {
			if (profileId.equals(profileList.get(i))) {
				profile = profileList.get(i);
				break;
			}
		}
		return profile;
	}
	
	private Group findGroup(String groupId, List<SelectableItem> list) {
		Group group = null;
		for (int i=0; i<list.size(); i++) {
			SelectableItem item = list.get(i);
			if (item instanceof Group) {
				if (groupId.equals(item.getId())) {
					group = (Group) item;
					break;
				} else {
					List<SelectableItem> children = ((Group) item).getChildren();
					group = findGroup(groupId, children);
				}
			}
		}
		return group;
	}
	
	@Override
	public Group findGroup(String groupId) {
		return findGroup(groupId, getSelectableItemList());
	}
	
	@Override
	public Rule findRule(String ruleId) {
		return findRule(ruleId, getSelectableItemList());
	}
	
	private Rule findRule(String ruleId, List<SelectableItem> list) {
		Rule rule = null;
		for (int i=0; i<list.size(); i++) {
			SelectableItem item = list.get(i);
			if (item instanceof Rule) {
				if (ruleId.equals(item.getId())) {
					rule = (Rule) item;
					break;
				}
			} else if (item instanceof Group) {
				List<SelectableItem> children = ((Group) item).getChildren();
				rule = findRule(ruleId, children);
			}
		}
		return rule;
	}
	
	@Override
	public Value findValue(String valueId) {
		Value value = null;
		List<Value> valuesInRoot = getValueList();
		for (Value rootValue : valuesInRoot) {
			if (rootValue != null && rootValue.getId().equals(valueId)) {
				return rootValue;
			}
		}
		List<Group> groupsInRoot = getGroupList();
		for (Group group : groupsInRoot) {
			value = findValue(valueId, group);
			if (value != null) {
				break;
			}
		}
		return value;
	}
	
	private Value findValue(String valueId, Group group) {
		//LOG.debug("findValue called for valueId " + valueId + ", group id is " + group.getId());
		Value value = null;
		List<Value> valuesInGroup = group.getValueList();
		for (Value rootValue : valuesInGroup) {
			if (rootValue != null && rootValue.getId().equals(valueId)) {
				return rootValue;
			}
		}
		List<Group> children = group.getGroupList();
		for (Group childGroup : children) {
			value = findValue(valueId, (Group) childGroup);
			if (value != null) {
				return value;
			}
		}
		return null;
	}
	

	public Rule createRule() {
		return (Rule) createSCAPElement("Rule", RuleImpl.class);
	}
	public Check createCheck() {
		return (Check) createSCAPElement("check", CheckImpl.class);
	}
    public ComplexCheck createComplexCheck() {
        return (ComplexCheck) createSCAPElement("complex-check", ComplexCheckImpl.class);
    }	
	public CheckContentRef createCheckContentRef() {
		return (CheckContentRef) createSCAPElement("check-content-ref", CheckContentRefImpl.class);
	}
	public CheckExport createCheckExport() {
		return (CheckExport) createSCAPElement("check-export", CheckExportImpl.class);
	}
	public CheckImport createCheckImport() {
		return (CheckImport) createSCAPElement("check-import", CheckImportImpl.class);
	}
	@Override
	public ChoicesElement createChoicesElement() {
		return (ChoicesElement) createSCAPElement("choices", ChoicesElementImpl.class);
	}
	public Conflicts createConflicts() {
		return (Conflicts) createSCAPElement("conflicts", ConflictsImpl.class);
	}	
	public SelValueElement createSelValueElement() {
		return (SelValueElement) createSCAPElement("value", SelValueElementImpl.class);
	}
	public Value createValue() {
		return (Value) createSCAPElement("Value", ValueImpl.class);	
	}
    public Profile createProfile() {
    	return (Profile) createSCAPElement("Profile", ProfileImpl.class);	
    }	
	public ProfileSelect createProfileSelect() {
		return (ProfileSelect) createSCAPElement("select", ProfileSelectImpl.class);	
	}
	public ProfileRefineRule createProfileRefineRule() {
		return (ProfileRefineRule) createSCAPElement("refine-rule", ProfileRefineRuleImpl.class);	
	}
	public ProfileRefineValue createProfileRefineValue() {
		return (ProfileRefineValue) createSCAPElement("refine-value", ProfileRefineValueImpl.class);	
	}
	public ProfileSetValue createProfileSetValue() {
		return (ProfileSetValue) createSCAPElement("set-value", ProfileSetValueImpl.class);	
	}
	public Requires createRequires() {
		return (Requires) createSCAPElement("requires", RequiresImpl.class);
	}	
    public Status createStatus() {
    	return (Status) createSCAPElement("status", StatusImpl.class);
    }

    /**
     * Creates an XCCDF Group suitable for adding to a benchmark or another group.
     *
     * @return Group
     */
    public Group createGroup() {
		return (Group) createSCAPElement("Group", GroupImpl.class);
    }
    public Ident createIdent() {
    	return (Ident) createSCAPElement("ident", IdentImpl.class);
    }
    public FixText createFixText() {
    	return (FixText) createSCAPElement("fixtext", FixTextImpl.class);
    }
    public Fix createFix() {
    	return (Fix) createSCAPElement("fix", FixImpl.class);
    }    
    public FixInstance createFixInstance() {
    	return (FixInstance) createSCAPElement("instance", FixInstanceImpl.class);
    }
    public FrontMatter createFrontMatter() {
    	return (FrontMatter) createSCAPElement("front-matter", FrontMatterImpl.class);
    } 
    public RearMatter createRearMatter() {
    	return (RearMatter) createSCAPElement("front-matter", RearMatterImpl.class);
    }     
    public LowerBoundElement createLowerBoundElement() {
    	return (LowerBoundElement) createSCAPElement("lower-bound", LowerBoundElementImpl.class);
    } 
    public UpperBoundElement createUpperBoundElement() {
    	return (UpperBoundElement) createSCAPElement("upper-bound", UpperBoundElementImpl.class);
    }  
    public DefaultElement createDefaultElement() {
    	return (DefaultElement) createSCAPElement("default", DefaultElementImpl.class);
    } 
    public ValueElement createValueElement() {
    	return (ValueElement) createSCAPElement("value", ValueElementImpl.class);
    }
	public Description createDescription() {
    	return (Description) createSCAPElement("description", DescriptionImpl.class);
    }
	public Version createVersion() {
    	return (Version) createSCAPElement("version", VersionImpl.class);
    }
	public Reference createReference() {
    	return (Reference) createSCAPElement("reference", ReferenceImpl.class);
    }
	public PlatformSpecification createPlatformSpecification() {
		Element newElement = new Element("platform-specification", "cpelang", SCAPElementImpl.CPE_LANG_NAMESPACE.getURI());
		return  (PlatformSpecification) createSCAPElement(newElement, PlatformSpecification.class);
	}
	public Title createTitle() {
		return (Title) createSCAPElement("title", TitleImpl.class);
	}
	public Question createQuestion() {
		return (Question) createSCAPElement("question", QuestionImpl.class);
	}
	public Warning createWarning() {
		return (Warning) createSCAPElement("warning", WarningImpl.class);
	}
	public Rationale createRationale() {
		return (Rationale) createSCAPElement("rationale", RationaleImpl.class);
	}
	public Notice createNotice() {
		return (Notice) createSCAPElement("notice", NoticeImpl.class);
	}	
	public Remark createRemark() {
		return (Remark) createSCAPElement("remark", RemarkImpl.class);
	}
    public MatchElement createMatchElement() {
    	return (MatchElement) createSCAPElement("match", MatchElementImpl.class);
    } 
    public DublinCoreElement createDublinCoreElement(String name) {
    	Element elem = new Element(name, DUBLIN_CORE_NAMESPACE);
    	return (DublinCoreElement) createSCAPElement(elem, DublinCoreElementImpl.class);
    }
    
    public DcStatus createDcStatus() {
        return null;
    }
	@Override
	public SelComplexValueElement createSelComplexValueElement(String elementName) {
		return null;
	}
	@Override
	public ComplexChoice createComplexChoice() {
		return null;
	}
	@Override
	public Choice createChoice() {
		return (Choice) createSCAPElement("choice", ChoiceImpl.class);
	}
    
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return BENCHMARK_ORDER;
	}
	
	/*
	 * Validate that check content for rules all point to existent files/definitions
	 */
	public String validateRuleContent()
	{
		String ret = null;

		StringBuilder sb = new StringBuilder();
    	String newLine = System.getProperty("line.separator");
		
		List<Rule> allRules = getAllRules();
		if(allRules != null && allRules.size() > 0)
		{
			for(int ruleIdx = 0; ruleIdx < allRules.size(); ruleIdx++)
			{
				Rule rule = allRules.get(ruleIdx);
				
				List<Check> ruleChecks = rule.getCheckList();
				
				if(ruleChecks != null && ruleChecks.size() > 0)
				{
					for(int chkIdx = 0; chkIdx < ruleChecks.size(); chkIdx++)
					{
						Check check = ruleChecks.get(chkIdx);
						String chkSys = check.getSystem();
						
						if(chkSys == null || chkSys.indexOf("oval") == -1)
						{
							// for now we only care about oval checks
							continue;
						}
						
						List<CheckContentRef> contentRefList = check.getCheckContentRefList();
						
						if(contentRefList != null && contentRefList.size() > 0)
						{
							for(int refIdx = 0; refIdx < contentRefList.size(); refIdx++)
							{
								CheckContentRef ccRef = contentRefList.get(refIdx);
								
								String checkFile = ccRef.getHref();
								boolean problem = false;
								if(checkFile == null)
								{
									problem = true;									
									sb.append("Rule " + rule.getId() + " has a check-content-ref with a null href!" + newLine);
								}
								if(checkFile.startsWith("http"))
								{
									// this is a url, we can't check this right now
									continue;
								}
								
								String checkId = ccRef.getName();

//								if(checkId == null)
//								{
//									problem = true;									
//									sb.append("Rule " + rule.getId() + " has a check-content-ref with a null name!" + newLine);
//								}
								
								if(problem)
								{
									continue;
								}
								
								if(getBundle() != null)
								{									
									// check that the bundle has an oval
									// document with that name
									OvalDefinitionsDocument odd = getBundle().getOvalDocumentByName(checkFile);

									if (odd == null)
									{
										problem = true;
										sb.append("Rule "
														+ rule.getId()
														+ " has a check-content-ref with href=\""
														+ checkFile
														+ "\" and name=\""
														+ checkId
														+ "\" but "
														+ " the file referred to by href is not part of this bundle."
														+ newLine);
									}

									if (problem)
									{
										continue;
									}

									if (checkId != null
											&& !checkId.equals("null"))
									{
										if (!odd.containsDefinition(checkId))
										{
											problem = true;
											sb.append("Rule "
															+ rule.getId()
															+ " has a check-content-ref with href=\""
															+ checkFile
															+ "\" and name=\""
															+ checkId
															+ "\" but "
															+ " the file doesn't contain the name/id "
															+ checkId + newLine);
										}
									}
								}
							} // done looping through check content refs
						} // if there are check content refs
					} // done looping through rule checks
				} // if there are rule checks    						
			} // done looping through rules
		} // if there are rules
    	
		if(sb.length() > 0)
		{
			ret = sb.toString();
		}
		
		return ret;
	}
	
	/**
	 * Return a set of the rule ids in this document.
	 * 
	 * @return HashSet<String>
	 * @see Rule
	 */
	public HashSet<String> getDocumentRuleIds()
	{
		HashSet<String> ret = new HashSet<String>();
		
		List<Rule> rules = getAllRules();
		
		if(rules != null && rules.size() > 0)
		{
			for(int x = 0; x < rules.size() ; x++)
			{
				Rule r = rules.get(x);
				
				ret.add(r.getId());
			}
			rules.clear();
		}
		rules = null;
		
		return ret;
	}

	/**
	 * Return a set of the profileIds in this document.
	 * 
	 * @return HashSet<String>
	 * @see Profile
	 */
	public HashSet<String> getDocumentProfileIds()
	{
		HashSet<String> ret = new HashSet<String>();
		
		List<Profile> profiles = getProfileList();
		
		if(profiles != null && profiles.size() > 0)
		{
			for(int x = 0; x < profiles.size() ; x++)
			{
				Profile p = profiles.get(x);
				
				ret.add(p.getId());
			}
			profiles.clear();
		}
		profiles = null;
		
		return ret;
	}

	/**
	 * Return a set of the Value ids in this document.
	 * 
	 * @return HashSet<String>
	 * @see Value
	 */
	public HashSet<String> getDocumentValueIds()
	{
		HashSet<String> ret = new HashSet<String>();
		
		List<Value> values = getAllValues();
		
		if(values != null && values.size() > 0)
		{
			for(int x = 0; x < values.size() ; x++)
			{
				Value v = values.get(x);
				
				ret.add(v.getId());
			}
			values.clear();
		}
		values = null;
		
		return ret;
	}

	/**
	 * Return a set of the Group ids in this document.
	 * 
	 * @return HashSet<String>
	 * @see Group
	 */
	public HashSet<String> getDocumentGroupIds()
	{
		HashSet<String> ret = new HashSet<String>();
		
		List<Group> groups = getAllGroups();
		
		if(groups != null && groups.size() > 0)
		{
			for(int x = 0; x < groups.size() ; x++)
			{
				Group g = groups.get(x);
				
				ret.add(g.getId());
			}
			groups.clear();
		}
		groups = null;
		
		return ret;
	}
	/**
	 * Validate things about profiles.
	 * 
	 * @return String
	 */
	public String validateProfiles()
	{
		StringBuilder sb = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		
		HashSet<String> existingRuleIds = getDocumentRuleIds(); // will not return null
		HashSet<String> existingProfileIds = getDocumentProfileIds(); // will not return null
		HashSet<String> existingValueIds = getDocumentValueIds(); // will not return null
		HashSet<String> existingGroupIds = getDocumentGroupIds(); // will not return null
		
		List<Profile> profiles = getProfileList();
		
		if(profiles != null && profiles.size() > 0)
		{
			// loop through profiles and perform checks on them
			for(int profIndex = 0; profIndex < profiles.size();profIndex++)
			{
				Profile p = profiles.get(profIndex);
				
				// check if the profile extends another profile and make sure that 
				// exists.
				String extendsProf = p.getExtends();
				
				if(extendsProf != null)
				{
					if(!existingProfileIds.contains(extendsProf))
					{
						sb.append("Profile " + p.getId() + " extends a profile that doesn't exist: " + extendsProf + newLine);						
					}
				}
				
				// loop through and check refine-values, refine-rule, selects, etc
				List<ProfileChild> children = p.getChildren();
				if(children != null && children.size() > 0)
				{
					for(int childIndex = 0; childIndex < children.size(); childIndex++)
					{
						ProfileChild pc = children.get(childIndex);
						
						if(pc instanceof ProfileSelect)
						{
							ProfileSelect select = (ProfileSelect) pc;
							
							String idRef = select.getIdRef();
							
							if(idRef == null)
							{
								sb.append("Profile " + p.getId() + " has a select that doesn't refer to a group or rule id!" + newLine);
							}
							else
							{
								if(!existingRuleIds.contains(idRef) && !existingGroupIds.contains(idRef))
								{
									sb.append("Profile " + p.getId() + " has a select that refers to a non-existent group or rule: " + idRef + newLine);
								}
							}
						}
						else if(pc instanceof ProfileRefineRule)
						{
							ProfileRefineRule refineRule = (ProfileRefineRule) pc;
							
							String ruleRef = refineRule.getIdRef();
							
							if(ruleRef == null)
							{
								sb.append("Profile " + p.getId() + " has a refine rule that doesn't refer to a rule!" + newLine);								
							}
							else
							{
								if(!existingRuleIds.contains(ruleRef))
								{
									sb.append("Profile " + p.getId() + " has a refine rule that refers to a non-existent rule: " + ruleRef + newLine);									
								}
							}
						}
						else if (pc instanceof ProfileRefineValue)
						{
							ProfileRefineValue refineValue = (ProfileRefineValue) pc;
							
							String valRef = refineValue.getIdRef();
							
							if(valRef == null)
							{
								sb.append("Profile " + p.getId() + " has a refine value that doesn't refer to a value!" + newLine);								
							}
							else
							{
								if(!existingValueIds.contains(valRef))
								{
									sb.append("Profile " + p.getId() + " has a refine value that refers to a non-existent value: " + valRef + newLine);									
								}
							}
						}
						else if (pc instanceof ProfileSetValue)
						{
							ProfileSetValue setValue = (ProfileSetValue) pc;
							String valRef = setValue.getIdRef();
							
							if(valRef == null)
							{
								sb.append("Profile " + p.getId() + " has a set value that doesn't refer to a value!" + newLine);								
							}
							else
							{
								if(!existingValueIds.contains(valRef))
								{
									sb.append("Profile " + p.getId() + " has a set value that refers to a non-existent value: " + valRef + newLine);									
								}
							}							
						}
					}
				}
			}
		}
		
		if(sb.length() > 0)
		{
			return sb.toString();
		}
		
		return null;
	}
	
	/**
	 * Validate things about groups.
	 * 
	 * @return String
	 */
	public String validateGroups()
	{
		// TODO: implement this
		return null;
	}

	/**
	 * Validate things about variables.
	 * 
	 * @return String
	 */
	public String validateVariables()
	{
		// TODO: implement this
		return null;
	}

    @Override
    public String validateSymantically() throws Exception
    {
    	StringBuilder sb = new StringBuilder();

		// Rule content
    	String ruleResults = validateRuleContent();
    	if(ruleResults != null)
    	{
    		sb.append(ruleResults);
    	}
    
    	// profiles
    	String profileResults = validateProfiles();
    	if(profileResults != null)
    	{
    		sb.append(profileResults);
    	}

    	// groups
    	String groupResults = validateGroups();
    	if(groupResults != null)
    	{
    		sb.append(groupResults);
    	}

    	// variables
    	String variableResults = validateVariables();
    	if(variableResults != null)
    	{
    		sb.append(variableResults);
    	}

		if(sb.length() > 0)
		{
			return sb.toString();
		}
		else
		{
			return null;
		}
    }

	public List<Title> getTitleList() {
		return getSCAPElementIntList("title", Title.class);
	}

	public void setTitleList(List<Title> list) {
		replaceList(list, getOrderMap(), "title");
	}

	/**
	 * Tell whether this Benchmark is resolved.
	 *
	 * @return boolean
	 */
    public boolean isResolved() {
        boolean result = false;
        String string = this.element.getAttributeValue("resolved");
        if (string != null) {
            result = string.equalsIgnoreCase("true") || string.equals("1");
        }
        return result;
    }

    /**
     * Set whether this Benchmark is resolved.
     *
     * @param isResolved Resolved or not
     */
    public void setResolved(boolean isResolved)
    {
        this.element.setAttribute("resolved", Boolean.toString(isResolved));
    }

	/**
	 * get style attribute for this Benchmark
	 * @return String style attribute
	 */
	public String getStyle() {
		return element.getAttributeValue("style");
	}

	/**
	 * set style attribute for this Benchmark
	 * @param style String 
	 */
	public void setStyle(String style)
	{
		if(style == null || style.trim().equals(""))
		{
			// remove the attribute
			element.removeAttribute("style");
		}
		else
		{
			element.setAttribute("style", style);
		}
	}

		/**
	 * get style-href attribute for this Benchmark
	 * @return String style attribute
	 */
	public String getStyleHref() {
		return element.getAttributeValue("style-href");
	}

	/**
	 * set style-href attribute for this Benchmark
	 * 
	 * @param styleHref Href of style
	 */
	public void setStyleHref(String styleHref)
	{
		if(styleHref == null || styleHref.trim().equals(""))
		{
			element.removeAttribute("style-href");
		}
		else
		{
			element.setAttribute("style-href", styleHref);
		}
	}
	
	public void applyValidation(List<Anomaly> anomalyList) {
		
	}
	
	public Set<String> getAllReferencedCPES() {
		HashSet<String> resultSet = new HashSet<String>();
		
		resultSet.addAll(getPlatformList());
		
		addPlatformSpecCpeReferences(resultSet);
		
		addProfileCpeReferences(resultSet, getProfileList());
		
		addRuleCpeReferences(resultSet, getRuleList());
		
		addGroupCpeReferences(resultSet, getGroupList());
		
		return resultSet;
	}
	
	private void addPlatformSpecCpeReferences(Set<String> cpeSet) {
		PlatformSpecification platformSpec = getPlatformSpecification();
		if (platformSpec == null) {
			return;
		}
		List<Platform> platformList = platformSpec.getPlatformList();
		for (Platform platform : platformList) {
			addLogicalTestCpeReferences(cpeSet, platform.getLogicalTest());
		}
	}
	
	private void addLogicalTestCpeReferences(Set<String> cpeSet, LogicalTest logicalTest) {
		List<FactRef> factRefList = logicalTest.getFactRefList();
		for (FactRef factRef : factRefList) {
			cpeSet.add(factRef.getName());
		}
		List<LogicalTest> childLogicalTests = logicalTest.getLogicalTestList();
		for (LogicalTest childLogicalTest : childLogicalTests) {
			addLogicalTestCpeReferences(cpeSet, childLogicalTest);
		}
	}
	
	private void addRuleCpeReferences(Set<String> cpeSet, List<Rule> ruleList) {
		for (Rule rule : ruleList) {
			cpeSet.addAll(rule.getPlatformList());
		}
	}
	
	private void addGroupCpeReferences(Set<String> cpeSet, List<Group> groupList) {
		for (Group group : groupList) {
			cpeSet.addAll(group.getPlatformList());
			addRuleCpeReferences(cpeSet, group.getRuleList());
			addGroupCpeReferences(cpeSet, group.getGroupList());
		}
	}
	
	private void addProfileCpeReferences(Set<String> cpeSet, List<Profile> profileList) {
		for (Profile profile : profileList) {
			cpeSet.addAll(profile.getPlatformList());
		}
	}
		
    @Override
    public void close()
    {
    	if(profileList != null)
    	{
    		profileList.clear();
    	}
    	
    	setElement(null);
    	setDoc(null);
    }
    
    @Override
    public void setId(String id) 
    {
    	if (!NCNAME_PATTERN.matcher(id).matches()) 
    	{ 
    		throw new IllegalArgumentException("Illegal characters in XCCDF id:" + id);
    	}
    	super.setId(id);
    }
    
	public void addOvalRuleToBenchmark(OvalDefinition ovalDef, String groupId, String ruleId, List<String> profileIds ) {		
		// First, find or create Group to contain new Rule
		Group group = findGroup(groupId);
		if (group == null) {
			group = createGroup();
			group.setId(groupId);
			addGroup(group);
		}
		
		// Second, create a new Rule and add it to the selected Group
		Rule rule = createRule();
		rule.setId(ruleId);
		rule.setSelected(false);
		group.addRule(rule);
		
		// Third, create a new Check and add it to the new Rule
		Check check = createCheck();
		check.setSystem(OvalDefinition.OVAL_NAMESPACE);
		rule.addCheck(check);
		
		// Fourth, create a new CheckContentRef and add it to the new Check
		CheckContentRef checkContentRef = createCheckContentRef();
		// href field is just the name of the Oval xml file (just file name w/o path info)
		File ovalFile = new File(ovalDef.getParentDocument().getFilename());
		checkContentRef.setHref(ovalFile.getName());
		checkContentRef.setName(ovalDef.getId());
		check.addCheckContentRef(checkContentRef);
		
		// Fifth, create or find each Profile whose id is in profileIds, and add a select element to
		// each of these Profiles for the new Rule id;
		for (int i=0; i<profileIds.size(); i++) {
			String profileId = profileIds.get(i);
			Profile profile = findProfile(profileId);
			ProfileSelect select = null;
			if (profile != null) {
				select = profile.findProfileSelect(ruleId);
				if (select != null) {
					if (!select.isSelected()) {
						select.setSelected(true);
					}
				}
			} else {
				profile = createProfile();
				profile.setId(profileId);
				profile.setTitle("Title");
				addProfile(profile);
			}
			// Find or create a 'select' for the new Rule to be added to this Profile
			if (select == null) {
				select = createProfileSelect();
				select.setIdRef(ruleId);
				select.setSelected(true);
				profile.addProfileSelect(select);
			}
		}		
	}
	
	@Override
    public List<DcStatus> getDcStatusList() {
    	return null;
    }
	@Override
    public void setDcStatusList(List<DcStatus> dcStatusList) {
    	// default implementation does nothing; introduced in 1.2
    }
	
	public static Map<Class<? extends SCAPElement>, Class<? extends SCAPElementImpl> > getTypeMap() {
		return TYPE_MAP;
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
		return TYPE_MAP.get(intClass);
	}
	
	@Override
	public abstract Namespace getNamespace();
	
	public Set<String> getAllCheckContentHrefs() {
		Set<String> hrefSet = new HashSet<String>();
		List<Rule> allRules = getAllRules();
		for (Rule rule : allRules) {
			List<Check> checkList = rule.getCheckList();
			for (Check check : checkList) {
				List<CheckContentRef> checkContentRefList = check.getCheckContentRefList();
				for (CheckContentRef checkContentRef : checkContentRefList) {
					String href = checkContentRef.getHref();
					hrefSet.add(href);
				}
			}
		}
		return hrefSet;
	}
}