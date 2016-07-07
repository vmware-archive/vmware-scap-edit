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
 * Represents an oval object filter.  Filters contain a state by which to filter the objects.
 */
public class OvalObjectFilter extends OvalObjectChild
{
	public OvalObjectFilter(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Get the id of the state this filter points to.
	 * 
	 * @return String
	 */
	public String getStateId()
	{
		String ret = null;
		
		ret = getElement().getValue();
		
		return ret;
	}
	
	/**
	 * The value of an oval object filter is a state.  So this method returns the state.
	 * 
	 * @return OvalState
	 */
	public OvalState getState()
	{
		OvalState ret = null;
		
		String stateId = getStateId();
		
		ret = getParentDocument().getOvalState(stateId);
			
		return ret;
	}

	/**
	 * Set the state to be used by this filter.
	 * 
	 * @param os An oval state
	 */
	public void setState(OvalState os)
	{
		if(os != null)
		{
			getElement().setText(os.getId());
		}
	}
	
	/**
	 * Set the state id to be used by this filter.
	 * 
	 * @param stateId The id of the state
	 */
	public void setStateId(String stateId)
	{
		if(stateId != null)
		{
			getElement().setText(stateId);
		}
	}
	
    @Override
    public boolean equals(Object o)
    {
    	boolean ret = false;
    	
    	if(o == null)
    	{
    		return ret;
    	}
    	
    	if(!(o instanceof OvalObjectFilter))
    	{
    		return ret;
    	}
    	
    	OvalObjectFilter other = (OvalObjectFilter)o;
    	
    	if(getState() != null)
    	{
    		if(!getState().equals(other.getState()))
    		{
    			return ret;
    		}
    	}
    	
    	ret = true;
    	return ret;	
    }

    /**
     * Get the action attribute. The action attribute value should be "exclude" or "include".
     * 
     * @return String
     */
	public String getAction()
	{
        String ret = "exclude";
		String action = getElement().getAttributeValue("action");
        if(action != null && action.length() > 0)
        {
            ret = action;
        }
        return ret;
	}
	
	/**
	 * Set the action attribute. The action attribute value should be "exclude" or "include".
	 * 
	 * @param action
	 */
	public void setAction(String action)
	{
		if(action != null)
		{
			getElement().setAttribute("action", action);
		}
        else
        {
            getElement().removeAttribute("action");
        }
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
		if (!(other instanceof OvalObjectFilter))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalObjectFilter other2 = (OvalObjectFilter) other;
		
		// If the other object's comment attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getStateId(), this.getStateId())))
    	{
    		return false;
		}
    	
		// If the other object's action attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getAction(), this.getAction())))
    	{
    		return false;
    	}
		
		// Return true if we get to this point.
		return true;
	}	
}
