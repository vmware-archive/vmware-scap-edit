package com.g2inc.scap.library.domain.oval;

import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;

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

/**
 * This class defines the parameters to be used by XCCDFBuilder.
 */
public class XCCDFBuilderParameters
{

    private SCAPDocument scapDoc;
    private String xccdfFileName;
    private String benchmarkId;
    private String benchmarkDescription;
    private String profileId;
    private String groupId;

    private String profileTitle;
    private String groupTitle;
    private SCAPDocumentTypeEnum xccdfType;
    
    /**
     * Get the OvalDefinitionsDocument to be used.
     *
     * @return OvalDefinitionsDocument
     */
    public SCAPDocument getSourceDoc() {
        return scapDoc;
    }

    /**
     * Set the OvalDefinitionsDocument to be used.
     * 
     * @param ovalDoc
     */
    public void setSourceDoc(SCAPDocument scapDoc) {
        this.scapDoc = scapDoc;
    }

    /**
     * Get the name of the XCCDF benchmark file to be created.
     * 
     * @return String
     */
    public String getXccdfFileName()
    {
        return xccdfFileName;
    }

    /**
     * Set the name of the XCCDF benchmark file to be created.
     *
     * @param xccdfFileName
     */
    public void setXccdfFileName(String xccdfFileName)
    {
        this.xccdfFileName = xccdfFileName;
    }

    /**
     * Get the benchmark id to be used for the resulting XCCDF document.
     *
     * @return String
     */
    public String getBenchmarkId()
    {
        return benchmarkId;
    }

    /**
     * Set the benchmark id to be used for the resulting XCCDF document.
     *
     * @param benchmarkId
     */
    public void setBenchmarkId(String benchmarkId)
    {
        this.benchmarkId = benchmarkId;
    }

    /**
     * Get the profile id to be use for the resulting XCCDF document's main profile.
     *
     * @return String
     */
    public String getProfileId()
    {
        return profileId;
    }

    /**
     * Set the profile id to be use for the resulting XCCDF document's main profile.
     *
     * @param profileId
     */
    public void setProfileId(String profileId)
    {
        this.profileId = profileId;
    }

    /**
     * Get the group id to be use for the resulting XCCDF document's main group.
     *
     * @return String
     */
    public String getGroupId()
    {
        return groupId;
    }

    /**
     * Set the group id to be use for the resulting XCCDF document's main group.
     *
     * @param groupId
     */
    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    /**
     * Set the description of the resulting benchmark.
     *
     * @param benchmarkDescription
     */
    public void setBenchmarkDescription(String benchmarkDescription)
    {
        this.benchmarkDescription = benchmarkDescription;
    }

    /**
     * Get the description of the resulting benchmark.
     *
     * @return String
     */
    public String getBenchmarkDescription()
    {
        return benchmarkDescription;
    }

    /**
     * Get the group title to use in the benchmark being created.
     *
     * @return String
     */
    public String getGroupTitle() {
        return groupTitle;
    }

    /**
     * Set the group title to use in the benchmark being created.
     *
     * @param groupTitle
     */
    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    /**
     * Get the profile title to use in the benchmark being created.
     *
     * @return String
     */
    public String getProfileTitle() {
        return profileTitle;
    }

    /**
     * Set the profile title to use in the benchmark being created.
     *
     * @param profileTitle
     */
    public void setProfileTitle(String profileTitle) {
        this.profileTitle = profileTitle;
    }

	public SCAPDocumentTypeEnum getXccdfType() {
		return xccdfType;
	}

	public void setXccdfType(SCAPDocumentTypeEnum xccdfType) {
		this.xccdfType = xccdfType;
	}
}
