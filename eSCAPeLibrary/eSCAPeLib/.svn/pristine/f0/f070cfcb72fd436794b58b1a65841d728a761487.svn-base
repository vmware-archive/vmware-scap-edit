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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * Class represents affected element in oval
 * definition metadata.
 *
 * @author ssill2
 * @see com.g2inc.scap.library.domain.oval.Metadata
 */
public abstract class AffectedItemContainer extends OvalElement
{
	public static final String ELEMENT_NAME = "affected";
	
    private static Logger log = Logger.getLogger(AffectedItemContainer.class);
	public final static HashMap<String, Integer> ITEM_ORDER = new HashMap<String, Integer>();
	static
	{
		ITEM_ORDER.put("platform", 0);
		ITEM_ORDER.put("product", 0);
	}

	public AffectedItemContainer(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}

    /**
     * Get the value of the family attribute of this affected element.
     *
     * @return AffectedItemFamilyEnum
     */
	public AffectedItemFamilyEnum getFamily()
	{
		AffectedItemFamilyEnum ret = null;
		
		String familyString = getElement().getAttributeValue("family");
		if(familyString == null)
        {
            familyString = "windows";
            getElement().setAttribute("family",familyString);
        }
		ret = AffectedItemFamilyEnum.valueOf(familyString.toLowerCase());
		
		return ret;
	}

        /**
         * Returns a list of platforms or products in this affected item container.
         *
         * @return List<AffectedItem>
         */
	public List<AffectedItem> getAffectedItems()
	{
		List<AffectedItem> items = new ArrayList<AffectedItem>();
		List children = getElement().getChildren();
		for(int x = 0; x < children.size();x++)
		{
			Element child = (Element) children.get(x);

			AffectedItem ai = null;

			if(child.getName().toLowerCase().indexOf("platform") > -1)
			{
				ai = getParentDocument().getAffectedPlatformWrapper();
			}
			else if(child.getName().toLowerCase().indexOf("product") > -1)
			{
				ai = getParentDocument().getAffectedProductWrapper();
			}

			if(ai != null)
			{
				ai.setElement(child);
				ai.setRoot(getRoot());
				ai.setValue(child.getText());

				items.add(ai);
			}
		}

        Collections.sort(items, new OvalComparator<AffectedItem>());
        
		return items;
	}
	
	public void setAffectedItems(List<AffectedItem> items)
	{
		if(items != null && items.size() > 0)
		{
            Collections.sort(items, new OvalComparator<AffectedItem>());

            // remove existing child elements
            getElement().removeContent();

            for(int x = 0; x < items.size();x++)
			{
				AffectedItem item = items.get(x);

                if(item.getElement().getParentElement() != null)
                {
                    item.getElement().detach();
                }
                getElement().addContent(item.getElement());
			}
		}
	}

    public void addAffectedItem(AffectedItem newItem)
	{
        List<AffectedItem> items = new ArrayList<AffectedItem>();
        items.add(newItem);

        addAffectedItems(items);
	}

    public void addAffectedItems(List<AffectedItem> newItems)
	{
		if(newItems != null && newItems.size() > 0)
		{
			List<AffectedItem> existing = getAffectedItems();

            boolean added = false;

			for(int x = 0; x < newItems.size();x++)
			{
				AffectedItem newItem = newItems.get(x);

                if(!containsItem(newItem.hashCode()))
                {
                    added = true;
                    existing.add(newItem);
                }
			}

            if(added)
                setAffectedItems(existing);
		}
	}

	public void setFamily(AffectedItemFamilyEnum fam)
	{
		getElement().setAttribute("family", fam.toString());
	}

    private boolean containsItem(int itemElementHashCode)
    {
        boolean ret = false;

        List<AffectedItem> existing = getAffectedItems();

        for(AffectedItem ai : existing)
        {
            if(ai.getElement().hashCode() == itemElementHashCode)
            {
                ret = true;
                break;
            }
        }

        return ret;
    }
    
	public AffectedPlatform createAffectedPlatform(OvalDefinitionsDocument parentDoc)
	{
		AffectedPlatform ap = parentDoc.getAffectedPlatformWrapper();
		Element e = new Element("platform", getElement().getNamespace());
		
		ap.setElement(e);
		ap.setRoot(parentDoc.getElement());
		
		return ap;
	}

	public AffectedProduct createAffectedProduct(OvalDefinitionsDocument parentDoc)
	{
		AffectedProduct ap = parentDoc.getAffectedProductWrapper();
		Element e = new Element("product", getElement().getNamespace());
		
		ap.setElement(e);
		ap.setRoot(parentDoc.getElement());
		
		return ap;
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
		if (!(other instanceof AffectedItemContainer))
		{
			return false;
		}
		
		// Cast the other object to this class.
		AffectedItemContainer other2 = (AffectedItemContainer) other;
		
		// If the other object's family attribute does not match,
		// then return false.
		if (!(this.getFamily().toString().equals(other2.getFamily().toString())))
		{
			return false;
		}

    	// Compare affected item elements.
		List<AffectedItem> myChildren = getAffectedItems();
		List<AffectedItem> otherChildren = other2.getAffectedItems();
		if (myChildren.size() != otherChildren.size())
		{
			return false;
		}
		for (int i=0; i < myChildren.size(); i++)
		{
			AffectedItem myChild = myChildren.get(i);
			AffectedItem otherChild = otherChildren.get(i);
			if (!(myChild.isDuplicateOf(otherChild)))
			{
				return false;
			}
		}
		
		// Return true if we get to this point.
		return true;
	}
}