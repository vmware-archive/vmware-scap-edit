package com.g2inc.scap.library.domain.oval;
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
 * Represents a reference element in the metadata element of an oval definition.
 */
public abstract class OvalReference extends OvalElement
{
    public static final String DEFAULT_ELEMENT_NAME_REFERENCE = "reference";

	public OvalReference(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}
	
	/**
	 * Get the reference id.
	 * 
	 * @return String
	 */
	public String getRefId()
	{
		String refid = null;
		refid = getElement().getAttributeValue("ref_id");
		return refid;
	}

	/**
	 * Get the reference URL
	 * 
	 * @return String
	 */
	public String getRefUrl()
	{
		String refurl = null;
		refurl = getElement().getAttributeValue("ref_url");
	
		return refurl;
	}

	/**
	 * Get the source.
	 * 
	 * @return String
	 */
	public String getSource()
	{
		String refsource = null;	
		refsource = getElement().getAttributeValue("source");
		return refsource;
	}

	/**
	 * Set the reference id.
	 * 
	 * @param refId
	 */
	public void setRefId(String refId)
	{
		if(refId != null && refId.length() > 0)
		{
            getElement().setAttribute("ref_id", refId);
		}
        else
        {
            getElement().removeAttribute("ref_id");
        }
	}

	/**
	 * Set the reference URL.
	 * 
	 * @param refUrl
	 */
	public void setRefUrl(String refUrl)
	{
		if(refUrl != null && refUrl.length() > 0)
		{
            getElement().setAttribute("ref_url", refUrl);
		}
        else
        {
            getElement().removeAttribute("ref_url");
        }
	}

	/**
	 * Set the source.
	 * 
	 * @param source
	 */
	public void setSource(String source)
	{
		if(source != null && source.length() > 0)
		{
            getElement().setAttribute("source", source);
		}
        else
        {
            getElement().removeAttribute("source");
        }
	}
	
    /**
     * This method determines if this object is a duplicate of another.
     * 
     * @param other
     * @return boolean
     */
    @Override
	public boolean isDuplicateOf(Object other)
	{
    	// Call the ancestor isDuplicateOf method.
    	if (!(super.isDuplicateOf(other)))
    	{
    		return false;
    	}
		
		// If the other object is not an instance of this class,
		// then return false.
		if (!(other instanceof OvalReference))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalReference other2 = (OvalReference) other;
		
		// If the other object's source attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getSource(), this.getSource())))
    	{
    		return false;
    	}

		// If the other object's ref_id attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getRefId(), this.getRefId())))
    	{
    		return false;
    	}
    	
		// If the other object's ref_url attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getRefUrl(), this.getRefUrl())))
    	{
    		return false;
    	}
		
		// Return true if we get to this point.
		return true;
	}
}
