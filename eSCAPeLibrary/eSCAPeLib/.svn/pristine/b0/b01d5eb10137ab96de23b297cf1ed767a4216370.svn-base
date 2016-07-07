package com.g2inc.scap.library.domain.oval;

import org.apache.log4j.Logger;
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
 * Base class for any elements of an oval definitions document that have a version attribute.
 */
public abstract class VersionedOvalElementImpl extends OvalElement implements VersionedOvalElement
{
	private static Logger log = Logger.getLogger(VersionedOvalElementImpl.class);

	public VersionedOvalElementImpl(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}

	/**
	 * Get the version.
	 * 
	 * @return OvalVersion
	 */
	public OvalVersion getVersion()
	{
		String verString = getElement().getAttributeValue("version");
		OvalVersion version = new OvalVersion();
		version.setVersion(verString);
		
		return version;
	}

	/**
	 * Set the version.
	 * 
	 * @param ver
	 */
	public void setVersion(OvalVersion ver)
	{
		if(ver != null)
		{
			getElement().setAttribute("version", ver.getVersionString());
		}
		else
		{
			getElement().removeAttribute("version");
		}
	}

    protected class MatchTracker
    {
        public boolean found = false;
    }

    /**
     * Classes extending VersionedOvalElement will need to implement this method.
     * This method allows the user to search for VersionedOvalElements with specific
     * text in them.  The which fields get searched is left up to the implementor.
     * 
     * @param findString
     * @return boolean
     */
    public abstract boolean matches(String findString);
    
	/**
	 * Return the value of the deprecated attribute.
	 * 
	 * @return boolean
	 */
    public boolean getDeprecated()
	{
        boolean deprecated = false;
		String deprecatedString = getElement().getAttributeValue("deprecated");
        if(deprecatedString != null)
        {
            try
            {
            	deprecated = Boolean.parseBoolean(deprecatedString);
            }
            catch(Exception e)
            {
                log.error("Unable to parse value as boolean: " + deprecatedString, e);
            }
        }
        return deprecated;
	}

    /**
     * Sets the value of the deprecated attribute.
     * 
     * @param b
     */
    public void setDeprecated(boolean b)
    {
        getElement().removeAttribute("deprecated");
        getElement().setAttribute("deprecated", b + "");
    }
    
    /**
     * This method determines if this object is a duplicate of another object.
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
		if (!(other instanceof VersionedOvalElementImpl))
		{
			return false;
		}
		
		// Cast the other object to this class.
		VersionedOvalElementImpl other2 = (VersionedOvalElementImpl) other;
		
		// If the other object's version attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getVersion().toString(), this.getVersion().toString())))
    	{
    		return false;
    	}
    	
		// If the other object's deprecated attribute does not match,
		// then return false.
    	if (other2.getDeprecated() != this.getDeprecated())
    	{
    		return false;
    	}
		
		// Return true if we get to this point.
		return true;
	}
}
