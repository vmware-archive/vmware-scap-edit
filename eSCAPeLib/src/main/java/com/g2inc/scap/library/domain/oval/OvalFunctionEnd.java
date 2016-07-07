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
 * Represents the end function that can be used in oval variables.
 *
 */
public class OvalFunctionEnd extends OvalFunction
{
    
	public OvalFunctionEnd(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}

	/**
	 * Get the character to end on.
	 * 
	 * @return String
	 */
    public String getCharacter() {
        return element.getAttributeValue("character");
    }

    /**
     * Set the character to end on.
     * 
     * @param character
     */
    public void setCharacter(String character) {
        element.setAttribute("character", character);
    }

    /**
     * Get the type of function this is.
     * 
     * @return OvalFunctionEnum
     */
    @Override
    public OvalFunctionEnum getType() {
        return OvalFunctionEnum.end;
    }

    /**
     * Get the minimum components group required for this function.
     * 
     * @return int
     */
    @Override
    public int getMinComponentGroups() {
        return 1;
    }

    /**
     * Get the maximum components group required for this function.
     * 
     * @return int
     */
    @Override
    public int getMaxComponentGroups() {
        return 1;
    }

    /**
     * Get the name of the enumeration type.
     * 
     * @return String
     */
    @Override
    public String getEnumerationTypeName() {
        return "EndFunctionType";
    }

    @Override
    public String getAttributeString() {
        String character = getCharacter();
        return (character == null ? "" : "character=" + character);
    }
    
    @Override
    public boolean equals(Object o)
    {
    	boolean ret = false;
    	
    	if(o == null)
    	{
    		return ret;
    	}
    	
    	if(!(o instanceof OvalFunctionEnd))
    	{
    		return ret;
    	}
    	
    	OvalFunctionEnd other = (OvalFunctionEnd)o;

    	if(getCharacter() != null)
    	{
    		if(!getCharacter().equals(other.getCharacter()))
    		{
    			return ret;
    		}
    	}
    	
    	ret = true;
    	return ret;
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
		if (!(other instanceof OvalFunctionEnd))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalFunctionEnd other2 = (OvalFunctionEnd) other;
		
		// If the other object's character attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getCharacter(), this.getCharacter())))
    	{
    		return false;
		}

		// Return true if we get to this point.
		return true;
	} 
}
