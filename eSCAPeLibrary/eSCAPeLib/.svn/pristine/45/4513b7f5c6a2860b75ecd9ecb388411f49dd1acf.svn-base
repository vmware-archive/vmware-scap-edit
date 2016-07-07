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
import com.g2inc.scap.library.domain.xccdf.Item;
import com.g2inc.scap.library.domain.xccdf.Metadata;
import com.g2inc.scap.library.domain.xccdf.Question;
import com.g2inc.scap.library.domain.xccdf.Reference;
import com.g2inc.scap.library.domain.xccdf.Status;
import com.g2inc.scap.library.domain.xccdf.Title;
import com.g2inc.scap.library.domain.xccdf.Version;
import com.g2inc.scap.library.domain.xccdf.Warning;

public abstract class ItemImpl extends SCAPElementImpl implements Item  {
	
	public final static HashMap<String, Integer> ITEM_ORDER = new HashMap<String, Integer>();
	static {
		ITEM_ORDER.put("status", 0);
		ITEM_ORDER.put("version", 1);
		ITEM_ORDER.put("title", 2);
		ITEM_ORDER.put("description", 3);
		ITEM_ORDER.put("warning", 4);
		ITEM_ORDER.put("question", 5);
		ITEM_ORDER.put("reference", 6);
	}

	public List<Status> getStatusList() {
		return getSCAPElementIntList("status", Status.class);
	}

	public void setStatusList(List<Status> statusList) {
		replaceList(statusList, getOrderMap(), "status");
	}
	
	public void setStatus(Status status) {
		ArrayList<Status> list = new ArrayList<Status>();
		list.add(status);
		setStatusList(list);
	}

	public Version getVersion() {
		Element versionElement = this.element.getChild("version", element.getNamespace());
		Version version = null;
		if (versionElement != null) {
			version = ((XCCDFBenchmark) getSCAPDocument()).createVersion();
			version.setRoot(this.root);
			version.setElement(versionElement);
		}
		return version;
	}

	public void setVersion(Version version) {
		element.removeChildren("version", element.getNamespace());
		insertChild(version, getOrderMap(), -1);
	}

	public List<Title> getTitleList() {
		return getSCAPElementIntList("title", Title.class);
	}

	public void setTitleList(List<Title> list) {
		replaceList(list, getOrderMap(), "title");
	}

	public String getTitle() {
		String title = null;
		Element titleElement = this.element.getChild("title", element.getNamespace());
        if (titleElement != null) {
            title = titleElement.getValue();
        }
		return title;
	}
	
	public String getTitleAsString() {
		StringBuilder sb = new StringBuilder();
		List<Title> titleList = getTitleList();
		for (Title title : titleList) {
			sb.append(title.getText());
		}
		return sb.toString();
	}

	public void setTitle(String title) {
		Element titleElement = this.element.getChild("title", element.getNamespace());
		if (titleElement == null) {
			titleElement = new Element("title", element.getNamespace());
			insertChild(titleElement, getOrderMap(), -1);
		} 
		titleElement.setText(title);
	}

	public List<Warning> getWarningList() {
		return getSCAPElementIntList("warning", Warning.class);
	}
	public void setWarningList(List<Warning> warningList) {
		replaceList(warningList, getOrderMap(), "warning");
	}

	public List<Question> getQuestionList() {
		return getSCAPElementIntList("question", Question.class);
	}
	public void setQuestionList(List<Question> questionList) {
		replaceList(questionList, getOrderMap(), "question");
	}

	public List<Reference> getReferenceList() {
		return getSCAPElementIntList("reference", Reference.class);
	}
	public void setReferenceList(List<Reference> referenceList) {
		replaceList(referenceList, getOrderMap(), "reference");
	}

	public List<Description> getDescriptionList() {
		return getSCAPElementIntList("description", Description.class);
	}

	public void setDescriptionList(List<Description> list) {
		replaceList(list, getOrderMap(), "description");
	}

	public String getDescriptionAsString() {
		StringBuilder sb = new StringBuilder();
		List<Description> list = getDescriptionList();
		for (int i=0; i<list.size(); i++) {
			Description desc = list.get(i);
			sb.append(desc.toStringWithHtml());
		}
		return sb.toString();
	}

	public void setDescriptionAsString(String desc) {
		element.removeChildren("description", element.getNamespace());
		XCCDFBenchmark xccdfDoc = (XCCDFBenchmark) this.SCAPDocument;
		Description description = xccdfDoc.createDescription();
		description.setElementFromStringWithHtml("description", desc);
		insertChild(description, getOrderMap(), -1);
	}

	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return ITEM_ORDER;
	}

	public boolean isAbstract() {
		boolean result = false;
		String abstractString = element.getAttributeValue("abstract");
		if (abstractString != null) {
			result = (abstractString.equalsIgnoreCase("true") || abstractString.equals("1"));
		}
		return result;
	}

	public void setAbstract(boolean isAbstract) {
		// Following line only changes the abstract attribute if
		// old value is not the same as the new value. This avoids
		// explicitly setting the default value if the original
		// document didn't specify the attribute, and the new value
		// is false.  
		// ^ is exclusive OR
		if (isAbstract ^ isAbstract()) {
			element.setAttribute("abstract", Boolean.toString(isAbstract));
		}
	}

	public boolean isHidden() {
		boolean result = false;
		String hiddenString = element.getAttributeValue("hidden");
		if (hiddenString != null) {
			result = (hiddenString.equalsIgnoreCase("true") || hiddenString.equals("1"));
		}
		return result;
	}

	public void setHidden(boolean isHidden) {
		// Following line only changes the hidden attribute if
		// old value is not the same as the new value. This avoids
		// explicitly setting the default value if the original
		// document didn't specify the attribute, and the new value
		// is false.
		// ^ is exclusive OR
		if (isHidden ^ isHidden()) {
			element.setAttribute("hidden", Boolean.toString(isHidden));
		}
	}

	public boolean isProhibitChanges() {
		boolean result = false;
		String prohibitChangesString = element.getAttributeValue("prohibitChanges");
		if (prohibitChangesString != null) {
			result = (prohibitChangesString.equalsIgnoreCase("true") || prohibitChangesString.equals("1"));
		}
		return result;
	}

	public void setProhibitChanges(boolean isProhibitChanges) {
		// Following line only changes the prohibitChanges attribute if
		// old value is not the same as the new value. This avoids
		// explicitly setting the default value if the original
		// document didn't specify the attribute, and the new value
		// is false.
		// ^ is exclusive OR
		if (isProhibitChanges ^ isProhibitChanges()) {
			element.setAttribute("prohibitChanges", Boolean.toString(isProhibitChanges));
		}
	}

	public String getClusterId() {
		return element.getAttributeValue("cluster-id");
	}

	public void setClusterId(String clusterId) {
		element.setAttribute("cluster-id", clusterId);
	}

	public String getExtends() {
		return element.getAttributeValue("extends");
	}

	public void setExtends(String val) {
		element.setAttribute("extends", val);
	}
    public boolean matches(String findString) {
        if(findString == null || findString.length() == 0) {
            return true;
        }
        String lcFindString = findString.toLowerCase();
        
        String title = getTitleAsString();
        if(title != null) {
            if(title.toLowerCase().indexOf(lcFindString) > -1) {
                // matches
                return true;
            }
        }
        
        String desc = getDescriptionAsString();      
        if(desc != null) {
            if(desc.toLowerCase().indexOf(lcFindString) > -1) {
                // matches
                return true;
            }
        }
        return false;
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
