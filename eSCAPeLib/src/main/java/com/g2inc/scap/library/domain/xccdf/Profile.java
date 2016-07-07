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

import java.util.List;

/**
 * Represents a profile element in an XCCDFBenchmark.
 */
public interface Profile extends ItemBasicType, MetadataAndDcStatusContainer
{
	
	/**
	 * Set a single status for this profile.
	 * 
	 * @param status A status to set
	 */
	public void setStatus(Status status);


	/**
	 * Get the title for this profile.
	 * 
	 * @return String
	 */
	public String getTitle();

	/**
	 * Set the title of this profile.
	 * 
	 * @param title A title to set
	 */
	public void setTitle(String title);

	/**
	 * Get the description of this profile.
	 * 
	 * @return String
	 */
	public String getDescription();
	
	/**
	 * Get the list of references for this profile.
	 * 
	 * @return List<Reference>
	 */
	public List<Reference> getReferenceList();
	
	/**
	 * Set the list of references for this profile.
	 * 
	 * @param referenceList A list of references to set
	 */
	public void setReferenceList(List<Reference> referenceList);

	/**
	 * Get the platform list for this profile.
	 * 
	 * @return List<String>
	 */
	public List<String> getPlatformList();

	/**
	 * Set the platform list for this profile.
	 * 
	 * @param platformList A list of platforms to set
	 */
	public void setPlatformList(List<String> platformList);

	/**
	 * Get the select list for this profile.
	 * 
	 * @return List<ProfileSelect>
	 */
	public List<ProfileSelect> getSelectList();

	/**
	 * Set the select list for this profile.
	 * 
	 * @param selectList A list of ProfileSelect objects.
	 */
	public void setSelectList(List<ProfileSelect> selectList);
	
	/**
	 * Get the children of this profile.
	 * 
	 * @return List<ProfileChild>
	 */
	public List<ProfileChild> getChildren();
	
	/**
	 * Set the children of this profile.
	 * 
	 * @param childList A list of children to set
	 */
	public void setChildren(List<ProfileChild> childList);

	/**
	 * Add a profile select to this profile.
	 * 
	 * @param child A profile select.
	 */
	public void addProfileSelect(ProfileSelect child);

	/**
	 * Add a profile set value to this profile.
	 * 
	 * @param child A profile set value.
	 */
	public void addProfileSetValue(ProfileSetValue child);

	/**
	 * Add a profile refine value to this profile.
	 * 
	 * @param child A profile refine value.
	 */
	public void addProfileRefineValue(ProfileRefineValue child);
	/**
	 * Add a profile refine rule to this profile.
	 * 
	 * @param child A profile refine rule.
	 */
	public void addProfileRefineRule(ProfileRefineRule child);
	
	/**
	 * Add a profile child to this profile.
	 * 
	 * @param child A profile child
	 */
	public void addProfileChild(ProfileChild child);

	/**
	 * Add a profile child to this profile, in the requested order.
	 *
	 * @param child A profile child
	 */
	public void addProfileChild(ProfileChild child, int order);
	
	/**
	 * Return a profile select for a given rule id.
	 * 
	 * @param ruleId Rule id we are looking for.
	 * 
	 * @return ProfileSelect
	 */
	public ProfileSelect findProfileSelect(String ruleId);
    
	/**
	 * Tell whether this profile is abstract.
	 * 
	 * @return boolean
	 */
    public boolean isAbstract();
    
    /**
     * Set whether this profile is abstract.
     * 
     * @param isAbstract Abstract or not
     */
    public void setAbstract(boolean isAbstract);
    /**
     * Get the profile this profile extends.
     * 
     * @return String
     */
    public String getExtends();
    
    /**
     * Set the id of the profile this profile extends.
     * 
     * @param extendsString Id of a profile we want to extend.
     */
    public void setExtends(String extendsString);

	/**
	 * Get value of the note-tag attribute for this Profile
	 *
	 * @return String which must be an "NCName" (suitable for use as an XCCDF id)
	 */
	public String getNoteTag();

		/**
	 * Set value of the note-tag attribute for this Profile
	 *
	 * @param noteTag String which must be an "NCName" (suitable for use as an XCCDF id)
	 */
	public void setNoteTag(String noteTag);

	/**
	 * Tell whether changes to this profile are prohibited
	 *
	 * @return boolean
	 */
	public boolean isProhibitChanges();

    /**
     * Set whether changes to this profile are prohibited
     *
     * @param isProhibitChanges Are changes to this profile allowed
     */
    public void setProhibitChanges(boolean isProhibitChanges);

	public ProfileSelect createProfileSelect();
	public ProfileRefineRule createProfileRefineRule();
	public ProfileRefineValue createProfileRefineValue();
	public ProfileSetValue createProfileSetValue();
	
    public List<DcStatus> getDcStatusList();
    public void setDcStatusList(List<DcStatus> dcStatusList);
    
    public List<Metadata> getMetadataList();
    public void setMetadataList(List<Metadata> metadataList);
}
