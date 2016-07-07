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

import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.xccdf.DcStatus;
import com.g2inc.scap.library.domain.xccdf.Description;
import com.g2inc.scap.library.domain.xccdf.ItemBasicType;
import com.g2inc.scap.library.domain.xccdf.Metadata;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.ProfileChild;
import com.g2inc.scap.library.domain.xccdf.ProfileRefineRule;
import com.g2inc.scap.library.domain.xccdf.ProfileRefineValue;
import com.g2inc.scap.library.domain.xccdf.ProfileSelect;
import com.g2inc.scap.library.domain.xccdf.ProfileSetValue;
import com.g2inc.scap.library.domain.xccdf.Reference;
import com.g2inc.scap.library.domain.xccdf.Status;
import com.g2inc.scap.library.domain.xccdf.Title;
import com.g2inc.scap.library.domain.xccdf.Version;

/**
 * Represents a profile element in an XCCDFBenchmark.
 */
public class ProfileImpl extends SCAPElementImpl implements Profile, ItemBasicType
{
	private String title;
	
	public final static HashMap<String, Integer> PROFILE_ORDER = new HashMap<String, Integer>();
	static {
		PROFILE_ORDER.put("status", 0);
		PROFILE_ORDER.put("version", 1);
		PROFILE_ORDER.put("title", 2);              //required
		PROFILE_ORDER.put("description", 3);
		PROFILE_ORDER.put("reference", 4);
		PROFILE_ORDER.put("platform", 5);
		PROFILE_ORDER.put("select", 6);
		PROFILE_ORDER.put("set-value", 6);
		PROFILE_ORDER.put("refine-value", 6);
		PROFILE_ORDER.put("refine-rule", 6);
		PROFILE_ORDER.put("signature", 7);
	}

	/**
	 * Get the status list of this profile.
	 * 
	 * @return List<Status>
	 */
	public List<Status> getStatusList() {
		return getSCAPElementIntList("status", Status.class);
	}

	/**
	 * Set the status list of this profile.
	 * 
	 * @param statusList A list of Status objects
	 */
	public void setStatusList(List<Status> statusList) {
		replaceList(statusList, getOrderMap(), "status");
	}
	
	/**
	 * Set a single status for this profile.
	 * 
	 * @param status A status to set
	 */
	public void setStatus(Status status) {
		ArrayList<Status> list = new ArrayList<Status>();
		list.add(status);
		setStatusList(list);
	}

	/**
	 * Get the version of this profile.
	 * 
	 * @return Version
	 */
	public Version getVersion() {
		return getSCAPElementInt("version", Version.class);
	}

	/**
	 * Set the version of this profile.
	 * 
	 * @param version A version to set
	 */
	public void setVersion(Version version) {
		element.removeChildren("version", element.getNamespace());  //maxOccurs=1, so remove any existing Version
		insertChild(version, getOrderMap(), -1);
	}

	/**
	 * Get the title for this profile.
	 * 
	 * @return String
	 */
	public String getTitle() {
		Element titleElement = this.element.getChild("title", element.getNamespace());
		if (titleElement != null) {
			title = titleElement.getValue();
		}
		return title;
	}

	/**
	 * Set the title of this profile.
	 * 
	 * @param title A title to set
	 */
	public void setTitle(String title) {
		this.title = title;
		Element titleElement = this.element.getChild("title", element.getNamespace());
		if (titleElement == null) {
			titleElement = new Element("title", element.getNamespace());
			insertChild(titleElement, getOrderMap(), -1);
		}
		titleElement.setText(title);
	}

	/**
	 * Get the description of this profile.
	 * 
	 * @return String
	 */
	public String getDescription() {
		String description = this.element.getChildTextTrim("description", element.getNamespace());
		return description;
	}
	
	/**
	 * Get the list of descriptions for this profile.
	 * 
	 * @return List<String>
	 */
	public List<Description> getDescriptionList() {
		return getSCAPElementIntList("description", Description.class);
	}

	/**
	 * Set the list of descriptions for this profile.
	 * 
	 * @param list List of descriptions to set
	 */
	public void setDescriptionList(List<Description> list) {
		replaceList(list, getOrderMap(), "description");
	}

	/**
	 * Get the list of references for this profile.
	 * 
	 * @return List<Reference>
	 */
	public List<Reference> getReferenceList() {
		return getSCAPElementIntList("reference", Reference.class);
	}
	
	/**
	 * Set the list of references for this profile.
	 * 
	 * @param referenceList A list of references to set
	 */
	public void setReferenceList(List<Reference> referenceList) {
		replaceList(referenceList, getOrderMap(), "reference");
	}

	/**
	 * Get the platform list for this profile.
	 * 
	 * @return List<String>
	 */
	public List<String> getPlatformList() {		
		List<String> platformList = new ArrayList<String>();
		List<?> childList = this.element.getChildren("platform", element.getNamespace());
		for(int i = 0; i < childList.size();i++) {
			Element elem = (Element) childList.get(i);
			String platform = elem.getAttributeValue("idref");
			platformList.add(platform);
		}
		return platformList;
	}

	/**
	 * Set the platform list for this profile.
	 * 
	 * @param platformList A list of platforms to set
	 */
    public void setPlatformList(List<String> platformList) {
        element.removeChildren("platform", element.getNamespace());
        for (int i = 0; i < platformList.size(); i++) {
            Element newElement = new Element("platform", element.getNamespace());
            newElement.setAttribute("idref", platformList.get(i));
            insertChild(newElement, getOrderMap(), -1);
        }
        replaceStringList(platformList, getOrderMap(), "platform");
    }

	/**
	 * Get the select list for this profile.
	 * 
	 * @return List<ProfileSelect>
	 */
	public List<ProfileSelect> getSelectList() {
		return getSCAPElementIntList("select", ProfileSelect.class);
	}

	/**
	 * Set the select list for this profile.
	 * 
	 * @param selectList A list of ProfileSelect objects.
	 */
	public void setSelectList(List<ProfileSelect> selectList) {
		replaceList(selectList, getOrderMap(), "select");
	}
	
	/**
	 * Get the children of this profile.
	 * 
	 * @return List<ProfileChild>
	 */
	public List<ProfileChild> getChildren() {
		ArrayList<ProfileChild> profileChildren = new ArrayList<ProfileChild>();
		@SuppressWarnings("unchecked")
		List<Element> childElements = element.getChildren();
		for (int i=0; i<childElements.size(); i++) {
			Element child = childElements.get(i);
			ProfileChild profileChild = null;
			if (child.getName().equals("select")) {
				profileChild = (ProfileSelect) createSCAPElementInt(child, ProfileSelect.class);
			} else if (child.getName().equals("set-value")) {
				profileChild = (ProfileSetValue) createSCAPElementInt(child, ProfileSetValue.class);
			} else if (child.getName().equals("refine-value")) {
				profileChild = (ProfileRefineValue) createSCAPElementInt(child, ProfileRefineValue.class);
			} else if (child.getName().equals("refine-rule")) {
				profileChild = (ProfileRefineRule) createSCAPElementInt(child, ProfileRefineRule.class);
			}
			if (profileChild != null) {
				profileChildren.add(profileChild);
			}
		}
		return profileChildren;
	}
	
	/**
	 * Set the children of this profile.
	 * 
	 * @param childList A list of children to set
	 */
	public void setChildren(List<ProfileChild> childList) {
		element.removeChildren("select", element.getNamespace());
		element.removeChildren("set-value", element.getNamespace());
		element.removeChildren("refine-value", element.getNamespace());
		element.removeChildren("refine-rule", element.getNamespace());
		for (int i=0; i<childList.size(); i++) {
			ProfileChild child = childList.get(i);
			insertChild(child, getOrderMap(), -1);
		}
	}

	/**
	 * Add a profile select to this profile.
	 * 
	 * @param child A profile select.
	 */
	public void addProfileSelect(ProfileSelect child) {
		insertChild(child, getOrderMap(), -1);
	}

	/**
	 * Add a profile set value to this profile.
	 * 
	 * @param child A profile set value.
	 */
	public void addProfileSetValue(ProfileSetValue child) {
		insertChild(child, getOrderMap(), -1);
	}

	/**
	 * Add a profile refine value to this profile.
	 * 
	 * @param child A profile refine value.
	 */
	public void addProfileRefineValue(ProfileRefineValue child) {
		insertChild(child, getOrderMap(), -1);
	}

	/**
	 * Add a profile refine rule to this profile.
	 * 
	 * @param child A profile refine rule.
	 */
	public void addProfileRefineRule(ProfileRefineRule child) {
		insertChild(child, getOrderMap(), -1);
	}
	
	/**
	 * Add a profile child to this profile.
	 * 
	 * @param child A profile child
	 */
	public void addProfileChild(ProfileChild child) {
		insertChild(child, getOrderMap(), -1);
	}

	/**
	 * Add a profile child to this profile, in the requested order.
	 *
	 * @param child A profile child
	 */
	public void addProfileChild(ProfileChild child, int order) {
		insertChild(child, getOrderMap(), order);
	}
	
	/**
	 * Return a profile select for a given rule id.
	 * 
	 * @param ruleId Rule id we are looking for.
	 * 
	 * @return ProfileSelect
	 */
	public ProfileSelect findProfileSelect(String ruleId) {
		List<ProfileSelect> selectList = getSelectList();
		ProfileSelect select = null;
		for (int i=0; i<selectList.size(); i++) {
			if (selectList.get(i).getIdRef().equals(ruleId)) {
				select = selectList.get(i);
				break;
			}
		}
		return select;
	}
    
	/**
	 * Tell whether this profile is abstract.
	 * 
	 * @return boolean
	 */
    public boolean isAbstract() {
        boolean result = false;
        String abstractString = this.element.getAttributeValue("abstract");
        if (abstractString != null) {
            result = abstractString.equalsIgnoreCase("true") || abstractString.equals("1");
        }
        return result;
    }
    
    /**
     * Set whether this profile is abstract.
     * 
     * @param isAbstract Abstract or not
     */
    public void setAbstract(boolean isAbstract)
    {
        this.element.setAttribute("abstract", Boolean.toString(isAbstract));
    }
    
    /**
     * Get the profile this profile extends.
     * 
     * @return String
     */
    public String getExtends() {
        return this.element.getAttributeValue("extends");
    }
    
    /**
     * Set the id of the profile this profile extends.
     * 
     * @param extendsString Id of a profile we want to extend.
     */
    public void setExtends(String extendsString) {
        this.element.setAttribute("extends", extendsString);
    }

    /**
     * Override the toString method.
     * 
     * @return String
     */
    @Override
    public String toString() {
        return ((isAbstract() ? "ABSTRACT " : ""))+ getId();
    }
    
    /**
     * Get the order map used by insertChild.
     * 
     * @return HashMap<String, Integer>
     */
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return PROFILE_ORDER;
	}

	/**
	 * Get the title list for this profile.
	 * 
	 * @return List<Title>
	 */
	public List<Title> getTitleList() {
		return getSCAPElementIntList("title", Title.class);
	}

	/**
	 * Set the list of titles for this profile.
	 * 
	 * @param list A List<Title> of titles to set
	 */
	public void setTitleList(List<Title> list) {
		replaceList(list, getOrderMap(), "title");
	}

	/**
	 * Get value of the note-tag attribute for this Profile
	 *
	 * @return String which must be an "NCName" (suitable for use as an XCCDF id)
	 */
	public String getNoteTag() {
		return element.getAttributeValue("note-tag");
	}

		/**
	 * Set value of the note-tag attribute for this Profile
	 *
	 * @param noteTag String which must be an "NCName" (suitable for use as an XCCDF id)
	 */
	public void setNoteTag(String noteTag) {
		element.setAttribute("note-tag", noteTag);
	}

	/**
	 * Tell whether changes to this profile are prohibited
	 *
	 * @return boolean
	 */
	public boolean isProhibitChanges() {
		boolean result = false;
		String prohibitChangesString = element.getAttributeValue("prohibitChanges");
		if (prohibitChangesString != null) {
			result = (prohibitChangesString.equalsIgnoreCase("true") || prohibitChangesString.equals("1"));
		}
		return result;
	}

    /**
     * Set whether changes to this profile are prohibited
     *
     * @param isProhibitChanges Are changes to this profile allowed
     */
    public void setProhibitChanges(boolean isProhibitChanges)
    {
        this.element.setAttribute("prohibitChanges", Boolean.toString(isProhibitChanges));
    }

	public ProfileSelect createProfileSelect() {
		return (ProfileSelect) createSCAPElementInt("select", ProfileSelect.class);	
	}
	public ProfileRefineRule createProfileRefineRule() {
		return (ProfileRefineRule) createSCAPElementInt("refine-rule", ProfileRefineRule.class);
	}
	public ProfileRefineValue createProfileRefineValue() {
		return (ProfileRefineValue) createSCAPElementInt("refine-value", ProfileRefineValue.class);
	}
	public ProfileSetValue createProfileSetValue() {
		return (ProfileSetValueImpl) createSCAPElementInt("set-value", ProfileSetValue.class);
	}
	
    public List<DcStatus> getDcStatusList() {
    	return null;
    }
    public void setDcStatusList(List<DcStatus> dcStatusList) {
    	// default implementation does nothing; introduced in 1.2
    }
    
    public List<Metadata> getMetadataList() {
    	return null;
    }
    public void setMetadataList(List<Metadata> metadataList) {
    	// default implementation does nothing; introduced in 1.2
    }
}
