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

import org.jdom.Element;

/**
 * Base class for object that can be a child of an oval variable.
 * 
 * @author ssill2
 * @see OvalVariable
 */
public abstract class OvalVariableChild extends OvalElement implements OvalVariableContent
{
	public OvalVariableChild(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}

	/**
	 * Add a child element to this one.
	 * 
	 * @param child The child we want to add to this one
	 */
    public void addChild(OvalVariableChild child) {
        this.element.addContent(child.getElement());
    }
    
    public List<OvalVariableChild> getVarChildren() {
    	OvalDefinitionsDocument odd = getParentDocument();
    	List<OvalVariableChild> list = new ArrayList<OvalVariableChild>();
    	List<Element> childElements = getElement().getChildren();
    	for (Element e : childElements) 
    	{
			OvalVariableChild child = odd.getOvalVariableChild(e);
    		list.add(child);
    	} 
    	return list;
    }  
    
    protected boolean areChildrenDuplicates(OvalVariableChild other) {
		List<OvalVariableChild> myChildren = getVarChildren();
		List<OvalVariableChild> otherChildren = other.getVarChildren();
		if (myChildren.size() != otherChildren.size()) {
			return false;
		}
		for (int i=0; i<myChildren.size(); i++) {
			OvalVariableChild myChild = myChildren.get(i);
			OvalVariableChild otherChild = otherChildren.get(i);
			if (!(myChild.isDuplicateOf(otherChild))) {
				return false;
			}
		}
		return true;
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
		if (!(other instanceof OvalVariableChild))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalVariableChild other2 = (OvalVariableChild) other;
		
		// Compare all of the child elements against each other.
		return areChildrenDuplicates(other2);
	}
}
