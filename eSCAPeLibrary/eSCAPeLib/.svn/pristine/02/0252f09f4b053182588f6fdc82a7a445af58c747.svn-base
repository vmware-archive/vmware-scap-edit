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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * Represents a child of an criteria element. It can be another criteria, a criterion, or an extend_definition.
 *
 * @see Criteria
 */
public abstract class CriteriaChild extends OvalElement
{
    private static Logger log = Logger.getLogger(CriteriaChild.class);
    
	public CriteriaChild(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}

	/**
	 * Return whether we are negated or not.
	 * 
	 * @return boolean
	 */
    public boolean isNegated()
	{
        boolean negated = false;
		String negatedString = getElement().getAttributeValue("negate");
        if(negatedString != null)
        {
            try
            {
                negated = Boolean.parseBoolean(negatedString);
            }
            catch(Exception e)
            {
                log.error("Unable to parse value as boolean: " + negatedString, e);
            }
        }
        return negated;
	}

    /**
     * Set whether we are negated or not.
     * 
     * @param b
     */
    public void setNegated(boolean b)
    {
		String negatedString = getElement().getAttributeValue("negate");
        if(negatedString != null)
        {
            // there is a negate value there already, remove it
            getElement().removeAttribute("negate");
        }
        getElement().setAttribute("negate", b + "");
    }
    
	/**
	 * Get the comment.
	 * 
	 * @return String
	 */
	public String getComment()
	{
		return getElement().getAttributeValue("comment");		
	}    
	
    /**
     * Set the comment.
     * 
     * @param comment
     */
	public void setComment(String comment)
	{
		if(comment == null)
		{
			getElement().removeAttribute("comment");
		} else 
		{
			getElement().setAttribute("comment", comment);
		}
	}	
	    
	/**
	 * Get all direct descendents of this criteria. Even though this method is defined
	 * in CriteriaChild, it will only find children if this CriteriaChild is a Criteria.
	 * If you need to get all descendents, regardless of depth, you will have to get this
	 * CriteriaChild's children, then if any of the children are criteria elements, 
	 * call getChildren on each criteria child.
	 * 
	 * @return List<CriteriaChild> list containing the direct descendents of this criteria
	 */
    public List<CriteriaChild> getChildren() {
    	ArrayList<CriteriaChild> children = new ArrayList<CriteriaChild>();
    	List<Element> elementChildren = element.getChildren();
		OvalDefinitionsDocument ovalDoc = (OvalDefinitionsDocument) getSCAPDocument();
    	for (Element childElement : elementChildren) {
    		String tagName = childElement.getName();
    		CriteriaChild child = null;
    		if (tagName.equals(Criteria.ELEMENT_NAME)) {
    			child = ovalDoc.getCriteriaWrapper();
    		} else if (tagName.equals(Criterion.ELEMENT_NAME)) {
    			child = ovalDoc.getCriterionWrapper();
    		} else if (tagName.equals(ExtendDefinition.ELEMENT_NAME)) {
    			child = ovalDoc.getExtendDefinitionWrapper();
    		} else {
    			throw new IllegalStateException("CriteriaChild.getChildren encountered unknown child type:" 
    					+ tagName);
    		}
			child.setElement(childElement);
    		children.add(child);
    	}
    	return children;
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
		if (!(other instanceof CriteriaChild))
		{
			return false;
		}
		
		// Cast the other object to this class.
		CriteriaChild other2 = (CriteriaChild) other;
		
		// If the other object's comment attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getComment(), this.getComment())))
    	{
    		return false;
    	}
    	
		// If the other object's negate attribute does not match,
		// then return false.
    	if (other2.isNegated() != this.isNegated())
    	{
    		return false;
    	}
		
		// Compare all of the child elements against each other.
		List<CriteriaChild> myChildren = getChildren();
		List<CriteriaChild> otherChildren = other2.getChildren();
		if (myChildren.size() != otherChildren.size())
		{
			return false;
		}
		for (int i=0; i < myChildren.size(); i++)
		{
			CriteriaChild myChild = myChildren.get(i);
			CriteriaChild otherChild = otherChildren.get(i);
			if (!(myChild.isDuplicateOf(otherChild)))
			{
				return false;
			}
		}
		
		// Return true if we get to this point.
		return true;
	}
}
