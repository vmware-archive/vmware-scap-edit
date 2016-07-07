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
 * This class represents an extend_definition tag in the criteria element
 * of an oval definition.
 * 
 * @author ssill2
 * @see OvalDefinition
 */
public class ExtendDefinition extends CriteriaChild
{
    private static Logger log = Logger.getLogger(ExtendDefinition.class);
    public static final String ELEMENT_NAME = "extend_definition";
    
	public ExtendDefinition(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}

	/**
	 * Get the definition id
	 * 
	 * @return String
	 */
	public String getDefinitionId()
	{
		String defId = getElement().getAttributeValue("definition_ref");		
		return defId;
	}

    @Override
    public String toString()
    {
        String comment = getComment();
        return "ExtendDefintion(defId=" + getDefinitionId() + ", comment = " + (comment == null ? "" : comment) + ")";
    }

    /**
     * Get a reference to the oval definition we refer to.
     * 
     * @return OvalDefinition
     */
    public OvalDefinition getOvalDefinition()
    {
		String defIdToFind = getDefinitionId();

		if(defIdToFind == null)
		{
			return null;
		}

		Element definitionsElement = getRoot().getChild("definitions", getRoot().getNamespace());

		if(definitionsElement != null)
		{
			List children = definitionsElement.getChildren();

			if(children != null && children.size() > 0)
			{
				for(int index = 0; index < children.size(); index++)
				{
					Element child = (Element) children.get(index);

					String defId = child.getAttributeValue("id");

					if(defId != null)
					{
						if(defId.equals(defIdToFind))
						{
							OvalDefinition od = getParentDocument().getDefinitionWrapper();
							od.setElement(child);
							od.setRoot(getRoot());

							return od;
						}
					}
					else
					{
				//		log.debug("ExtendDefinition: getDefinition: defId is NULL");
					}
				}
			}
		}
		else
		{
		//	log.debug("ExtendDefinition: getDefinition: definitionsElement is NULL");
		}

		return null;
    }

	/**
	 * Set the definition id.
	 * 
	 * @param defId
	 */
	public void setDefinitionId(String defId)
	{
		if(defId != null)
		{
			getElement().setAttribute("definition_ref", defId);
			getParentDocument().setDirty(true);
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
		if (!(other instanceof ExtendDefinition))
		{
			return false;
		}
		
		// Cast the other object to this class.
		ExtendDefinition other2 = (ExtendDefinition) other;
		
		// If the other object's test_ref attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getDefinitionId(), this.getDefinitionId())))
    	{
			return false;
		}
		
		// Return true if we get to this point.
		return true;
	}
}
