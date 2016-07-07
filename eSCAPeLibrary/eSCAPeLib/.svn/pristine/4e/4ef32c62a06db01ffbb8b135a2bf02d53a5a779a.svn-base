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
 * Base class for anything that can be a child of an oval object.
 */

public abstract class OvalObjectChild extends OvalElement 
{
	private static Logger log = Logger.getLogger(OvalObjectChild.class);
	private OvalObjectChild parent = null;

	public OvalObjectChild(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}
	/**
	 * Get all direct descendents of this OvalObjectChild
	 * 
	 * @return  direct descendents of the node
	 */
	public List<OvalObjectChild> getChildren() {
		List<OvalObjectChild> list = new ArrayList<OvalObjectChild>();
		List<OvalEntity> validParms = null;
		OvalDefinitionsDocument odd = getParentDocument();
		if (this instanceof OvalObject) 
		{
			String objType = getElementName();
			String platform = getPlatform();
			validParms = getParentDocument().getValidObjectEntityTypes(platform, objType);
		}
    	List<Element> childElements = getElement().getChildren();
    	for (Element e : childElements) 
    	{
			if ((e.getName().equals("behaviors")) || (e.getName().equals("notes")))
			{
				continue;
			}
			OvalObjectChild child = odd.getOvalObjectChild(e,validParms);
			child.setParent(this);
    		list.add(child);
    	}  	
		return list;
	}

	/**
	 * Replace all current children with the new List of children
	 * 
	 * @param childList  List of children to replace current children
	 */
	public void setChildren(List<OvalObjectChild> childList)
	{
		// remove any existing children that are not 'behaviors' or 'notes'.
		List<Element> existingChildren = element.getChildren();
		for (Element existingChild : existingChildren)
		{
			String existingChildName = existingChild.getName();
			if ((existingChildName.equals("behaviors")) || (existingChildName.equals("notes")))
			{	
			}
			else
			{
				existingChild.detach();
			}
		}
		
		// assume childList is in correct order for the schema
		for (OvalObjectChild child : childList)
		{
			element.addContent(child.getElement());
		}
	}
	
	/**
	 * Add a new OvalObjectChild at the end of this nodes child list
	 * 
	 * @param child - the new child node to be added
	 */
	public void addChild(OvalObjectChild child) {
		// assume new child belongs at end of child list
		element.addContent(child.getElement());
	}
	
	/**
	 * The parent is not really used now - it is here because it was
	 * useful when OvalObjectChild implemented MutableTreeNode. 
	 * 
	 * @param parent the node to be set as this nodes parent
	 */
	public void setParent(OvalObjectChild parent) {
		this.parent = parent;
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
		if (!(other instanceof OvalObjectChild))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalObjectChild other2 = (OvalObjectChild) other;
		
		// Compare children.
        List<OvalObjectChild> myChildren = getChildren();
        List<OvalObjectChild> otherChildren = other2.getChildren();
        if (myChildren.size() != otherChildren.size())
        {
            return false;
        }
        else
        {
            for (int i = 0; i < myChildren.size(); i++)
            {
                OvalObjectChild myChild = myChildren.get(i);
                OvalObjectChild otherChild = otherChildren.get(i);
                if (!(myChild.isDuplicateOf(otherChild)))
                {
                	return false;
                }
            }
        }
		
		// Return true if we get to this point.
		return true;
	}
}
