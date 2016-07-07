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
import org.jdom.Element;

/**
 * Class represents either an affected product or affected platform in oval
 * definition metadata.
 *
 * @author ssill2
 * 
 * @see com.g2inc.scap.library.domain.oval.AffectedPlatform
 * @see com.g2inc.scap.library.domain.oval.AffectedProduct
 * @see com.g2inc.scap.library.domain.oval.Metadata
 * @see com.g2inc.scap.library.domain.oval.AffectedItemContainer
 */
public abstract class AffectedItem extends OvalElement {

    public AffectedItem(OvalDefinitionsDocument parentDocument) {
        super(parentDocument);
    }

    /**
     * Get the type of affected item this is.  Can either be a platform or a product.
     *
     * @return AffectedItemType
     */
    public AffectedItemType getType() {
        String elementName = getElementName();

        if (elementName.toUpperCase().equals(AffectedItemType.PLATFORM.toString())) {
            return AffectedItemType.PLATFORM;
        } else {
            return AffectedItemType.PRODUCT;
        }
    }

    /**
     * Set the type of affected item this is. Can either be a platform or a product.
     *
     * @param type
     */
    public void setType(AffectedItemType type) {
        switch (type) {
            case PLATFORM:
                if (getElement() != null) {
                    getElement().setName("platform");
                }
                break;
            default:
                if (getElement() != null) {
                    getElement().setName("product");
                }
                break;
        }
    }

    /**
     * Get the value of this affected item.
     *
     * @return String
     */
    public String getValue() {
        String name = getElement().getValue();
        return name;
    }

    /**
     * Set the value of this affected item.
     *
     * @param value The value of either the product name or the platform name.
     */
    public void setValue(String value) {
        getElement().setText(value);
    }


    /**
     * Remove this platform or product from it's parent affected container.
     * 
     */
    public void removeFromParent()
    {
        if(getElement() != null)
        {
            Element parent = getElement().getParentElement();

            if(parent != null)
            {
                parent.removeContent(getElement());
            }
        }
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
		if (!(other instanceof AffectedItem))
		{
			return false;
		}
		
		// Cast the other object to this class.
		AffectedItem other2 = (AffectedItem) other;
		
		// If the other object's value does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getValue(), this.getValue())))
    	{
    		return false;
    	}

		// If the other object's type does not match,
		// then return false.
		if (!(this.getType().toString().equals(other2.getType().toString())))
		{
			return false;
		}
		
		// Return true if we get to this point.
		return true;
	}
}
