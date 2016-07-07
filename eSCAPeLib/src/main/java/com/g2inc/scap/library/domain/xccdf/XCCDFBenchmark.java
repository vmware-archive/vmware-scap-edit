package com.g2inc.scap.library.domain.xccdf;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jdom.Namespace;

import com.g2inc.scap.library.domain.SCAPDocumentType;
import com.g2inc.scap.library.domain.cpelang.PlatformSpecification;
import com.g2inc.scap.library.domain.oval.OvalDefinition;

/**
 * Represents an XCCDF benchmark xml document.
 */
public interface XCCDFBenchmark extends SCAPDocumentType, ItemBasicType
{

	public static final Pattern NCNAME_PATTERN = Pattern.compile("[_A-Za-z][-._A-Za-z0-9]*");
	/**
	 * Get the list of profiles in this benchmark.
	 * 
	 * @return List<Profile>
	 */
	public List<Profile> getProfileList();
	
	public void addStatus(Status status);
	public void setStatus(Status status);
	
    public String getDescriptionAsString();
    public void setDescriptionAsString(String description);
	public String getTitle();
	public void setTitle(String titleText);
	
	public List<Notice> getNoticeList();
	public void setNoticeList(List<Notice> list);

	public Notice getNotice();
	public void setNotice(Notice notice);
	
	public List<Model> getModelList();
	public void setModelList(List<Model> list);

	public List<Reference> getReferenceList();
	public void setReferenceList(List<Reference> referenceList);
	public List<PlainText> getPlainTextList();
	public void setPlainTextList(List<PlainText> list);
	public String getPlainText(String id);
	public List<String> getPlatformList();
	public void setPlatformList(List<String> platformList);
	public List<Value> getValueList();
	public void setValueList(List<Value> valueList);
	public void addValue(Value value);
	public List<SelectableItem> getSelectableItemList();
	public void setSelectablItemList(List<SelectableItem> selItemList);
	public List<Rule> getRuleList();
	public List<Group> getGroupList();
	public List<Group> getAllGroups();
	public List<Rule> getAllRules();
	public List<Value> getAllValues();
	
	
	public List<FrontMatter> getFrontMatterList();
	public void setFrontMatterList(List<FrontMatter> frontMatterList);
	
	public List<RearMatter> getRearMatterList();
	public void setRearMatterList(List<RearMatter> rearMatterList);
	
	public PlatformSpecification getPlatformSpecification();
	public void setPlatformSpecification(PlatformSpecification spec);
	public List<Metadata> getMetadataList();
        public void setMetadataList(List<Metadata> list);
	
    /**
     * Returns whether the given profile exists in this benchmark.
     * 
     * @param profName The name of a profile
     * @return boolean
     */
    public boolean containsProfile(String profName);
    /**
     * Returns whether the given rule exists in this benchmark.
     *
     * @param ruleName The name of a rule
     * @return boolean
     */
    public boolean containsRule(String ruleName);
    
    /**
     * Returns whether the given value exists in this benchmark.
     *
     * @param valueName The name of a value
     * @return boolean
     */
    public boolean containsValue(String valueName);
    /**
     * Call this method to make sure that an id you want to use doesn't already exist in the benchmark.
     * All ids must be unique.  Returns true if the id is being used as an identifier somewhere in the document
     * or false otherwise.
     * 
     * @param id Id you want to use
     * @return boolean
     */
    public boolean containsId(String id);
    /**
     * Returns whether the given group exists in this benchmark.
     *
     * @param groupName The name of a group
     * @return boolean
     */
    public boolean containsGroup(String groupName);

	public String getSchemaVersion();
	
	public void addProfile(Profile profile);
	
	public void addGroup(Group group);
	
	public void addRule(Rule rule);
	
	public Profile findProfile(String profileId);
	public Group findGroup(String groupId);
	public Rule findRule(String ruleId);
	public Value findValue(String valueId);

	public Rule createRule();
	public Check createCheck();
    public ComplexCheck createComplexCheck();
	public CheckContentRef createCheckContentRef();
	public CheckExport createCheckExport();
	public CheckImport createCheckImport();
	public ChoicesElement createChoicesElement();
	public Conflicts createConflicts();
	public SelValueElement createSelValueElement();
	public Value createValue();
    public Profile createProfile();
	public ProfileSelect createProfileSelect();
	public ProfileRefineRule createProfileRefineRule();
	public ProfileRefineValue createProfileRefineValue();
	public ProfileSetValue createProfileSetValue();
	public Requires createRequires();
    public Status createStatus();

    public Group createGroup();
    public Ident createIdent();
    public FixText createFixText();
    public Fix createFix();
    public FixInstance createFixInstance();
    public FrontMatter createFrontMatter();
    public RearMatter createRearMatter();
    public LowerBoundElement createLowerBoundElement();
    public UpperBoundElement createUpperBoundElement();
    public DefaultElement createDefaultElement();
    public ValueElement createValueElement();
	public Description createDescription();
	public Version createVersion();
	public Reference createReference();
	public PlatformSpecification createPlatformSpecification();
	public Title createTitle();
	public Question createQuestion();
	public Warning createWarning();
	public Rationale createRationale();
	public Notice createNotice();
	public Remark createRemark();
    public DcStatus createDcStatus();
    public SelComplexValueElement createSelComplexValueElement(String elementName);
    public Choice createChoice();
    public ComplexChoice createComplexChoice();


	
	/*
	 * Validate that check content for rules all point to existent files/definitions
	 */
	public String validateRuleContent();
	
	/**
	 * Return a set of the rule ids in this document.
	 * 
	 * @return HashSet<String>
	 * @see Rule
	 */
	public HashSet<String> getDocumentRuleIds();

	/**
	 * Return a set of the profileIds in this document.
	 * 
	 * @return HashSet<String>
	 * @see Profile
	 */
	public HashSet<String> getDocumentProfileIds();

	/**
	 * Return a set of the Value ids in this document.
	 * 
	 * @return HashSet<String>
	 * @see Value
	 */
	public HashSet<String> getDocumentValueIds();

	/**
	 * Return a set of the Group ids in this document.
	 * 
	 * @return HashSet<String>
	 * @see Group
	 */
	public HashSet<String> getDocumentGroupIds();
	/**
	 * Validate things about profiles.
	 * 
	 * @return String
	 */
	public String validateProfiles();
	
	/**
	 * Validate things about groups.
	 * 
	 * @return String
	 */
	public String validateGroups();

	/**
	 * Validate things about variables.
	 * 
	 * @return String
	 */
	public String validateVariables();

;
	public List<Title> getTitleList();

	public void setTitleList(List<Title> list);

	/**
	 * Tell whether this Benchmark is resolved.
	 *
	 * @return boolean
	 */
    public boolean isResolved();

    /**
     * Set whether this Benchmark is resolved.
     *
     * @param isResolved Resolved or not
     */
    public void setResolved(boolean isResolved);
	/**
	 * get style attribute for this Benchmark
	 * @return String style attribute
	 */
	public String getStyle();

	/**
	 * set style attribute for this Benchmark
	 * @param style String 
	 */
	public void setStyle(String style);

		/**
	 * get style-href attribute for this Benchmark
	 * @return String style attribute
	 */
	public String getStyleHref();

	/**
	 * set style-href attribute for this Benchmark
	 * 
	 * @param styleHref Href of style
	 */
	public void setStyleHref(String styleHref);
	
	public Set<String> getAllReferencedCPES();
	
	public void addOvalRuleToBenchmark(OvalDefinition ovalDef, String groupId, String ruleId, List<String> profileIds );
	
	public Namespace getNamespace();
	
    public List<DcStatus> getDcStatusList();
    public void setDcStatusList(List<DcStatus> dcStatusList);
    
    public DublinCoreElement createDublinCoreElement(String name);
    
    public Set<String> getAllCheckContentHrefs();
}