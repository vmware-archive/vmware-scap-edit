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

import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * Represents an object_component element in an oval variable.
 */
public abstract class OvalVariableComponentObject extends OvalVariableChild
{
	private static Logger log = Logger.getLogger(OvalVariableComponentObject.class);
	
	public OvalVariableComponentObject(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}
	

	/**
	 * Get field name we are looking at in the object.
	 * 
	 * @return String
	 */
	public String getItemField()
	{
		String itemField = getElement().getAttributeValue("item_field");
		return itemField;
	}

	/**
	 * Get the object id of the object we are looking at.
	 * 
	 * @return String
	 */
	public String getObjectId()
	{
		String objId = getElement().getAttributeValue("object_ref");
		return objId;
	}

	/**
	 * Get a reference to the object this references points to.
	 * 
	 * @return OvalObject
	 */
    public OvalObject getObject()
    {
        OvalObject ret = null;

        String objIdToFind = getObjectId();

        if(objIdToFind != null)
        {
           ret = getParentDocument().getOvalObject(objIdToFind);
        }

		return ret;
    }

    /**
     * Set the field name we want to look at in the referenced object.
     * 
     * @param fieldName
     */
	public void setItemField(String fieldName)
	{
		if(fieldName != null)
		{
			getElement().setAttribute("item_field", fieldName);
		}
	}

	/**
	 * Set the id of the object we want to reference.
	 * 
	 * @param objId
	 */
	public void setObjectId(String objId)
	{
		if(objId != null)
		{
			getElement().setAttribute("object_ref", objId);
		}
	}

    @Override
	public String toString() {
        StringBuilder sb = new StringBuilder("object_component(");
        sb.append(getObjectId());
        sb.append(")");
        return sb.toString();
    }
    
	/**
	 * Returns the value of the record_field attribute.
	 * 
	 * @return String
	 */
	public String getRecordField()
	{
		String recordField = getElement().getAttributeValue("record_field");
		return recordField;
	}
	
    /**
     * Sets the value of the record_field attribute.
     * 
     * @param fieldName
     */
	public void setRecordField(String fieldName)
	{
		if(fieldName != null)
		{
			getElement().setAttribute("record_field", fieldName);
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
    	
    	if(!(o instanceof OvalVariableComponentObject))
    	{
    		return ret;
    	}
    	
    	OvalVariableComponentObject other = (OvalVariableComponentObject)o;

    	if(getItemField() != null)
    	{
    		if(!getItemField().equals(other.getItemField()))
    		{
    			return ret;
    		}
    	}
    	
    	if(getObjectId() != null)
    	{
    		if(!getObjectId().equals(other.getObjectId()))
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
		if (!(other instanceof OvalVariableComponentObject))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalVariableComponentObject other2 = (OvalVariableComponentObject) other;
    	
		// If the other object's object_ref attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getObjectId(), this.getObjectId())))
    	{
    		return false;
			}
    	
		// If the other object's item_field attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getItemField(), this.getItemField())))
    	{
    		return false;
			}			
    	
		// If the other object's record_field attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getRecordField(), this.getRecordField())))
    	{
    		return false;
		}

		// Return true if we get to this point.
		return true;
	}
}
