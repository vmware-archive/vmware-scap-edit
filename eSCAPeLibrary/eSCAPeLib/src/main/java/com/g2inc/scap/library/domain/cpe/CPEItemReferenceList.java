package com.g2inc.scap.library.domain.cpe;
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
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import com.g2inc.scap.library.domain.SCAPElementImpl;

/**
 * Represents a references element in a cpe-item.
 * 
 * @see CPEItem
 */
public abstract class CPEItemReferenceList extends SCAPElementImpl
{
	public final static HashMap<String, Integer> CPE_ORDER = new HashMap<String, Integer>();	
	static
	{
		CPE_ORDER.put("reference", 0);
	}
	
	public CPEItemReferenceList()
	{
		super();
	}
	/**
	 * Create and return a CPEItemReference suitable for adding to this ReferenceList.
	 * 
	 * @return CPEItemReference
	 */
	public CPEItemReference createReference()
	{
		CPEItemReference ref = getReferenceWrapper();
		
		Element e = new Element("reference", getElement().getNamespace());
		
		ref.setElement(e);
		ref.setRoot(getRoot());
		ref.setDoc(getDoc());
		ref.setSCAPDocument(getSCAPDocument());
		
		return ref;
	}
	
	/**
	 * Add a created reference to this references element.
	 * 
	 * @param ref
	 */
	public void addReference(CPEItemReference ref)
	{
		if(getElement() != null)
		{
			insertChild(ref, CPE_ORDER, -1);
		}
	}
	
    /**
     * Remove an existing reference from this references element.
     * 
     * @param ref
     */
    public void removeReference(CPEItemReference ref)
    {
        if(getElement() != null)
        {
            if(ref.getElement() != null)
            {
                getElement().removeContent(ref.getElement());
            }
        }
    }
    
	/**
	 * Get a version specific wrapper for a reference element.
	 * 
	 * @return CPEItemReference
	 */
	public abstract CPEItemReference getReferenceWrapper();
	
	/**
	 * Get a list of reference elements in this references element.
	 * 
	 * @return List<CPEItemReference>
	 */
	public List<CPEItemReference> getReferences()
	{
		List<CPEItemReference> refs = null;
		
		if(getElement() != null)
		{
			List children = getElement().getChildren("reference", getElement().getNamespace());
			
			if(children != null && children.size() > 0)
			{
				for(int x = 0; x < children.size(); x++)
				{
					Object child = children.get(x);
					
					if(!(child instanceof Element))
					{
						continue;
					}
					
					if(refs == null)
					{
						refs = new ArrayList<CPEItemReference>();
						
					}
					Element childE = (Element) child;
			
					CPEItemReference ref = getReferenceWrapper();
					
					ref.setElement(childE);
					ref.setRoot(getRoot());
					ref.setDoc(getDoc());
					ref.setSCAPDocument(getSCAPDocument());
					
					refs.add(ref);
				}
			}
		}
		
		return refs;
	}
}
