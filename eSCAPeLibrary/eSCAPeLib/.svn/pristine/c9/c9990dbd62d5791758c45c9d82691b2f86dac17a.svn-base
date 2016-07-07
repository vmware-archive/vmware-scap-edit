package com.g2inc.scap.library.domain.oval.impl;
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

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalStateOperatorEnum;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.UnresolvedOvalState;

public class OvalTestMultiStateImpl extends OvalTest
{
    public OvalTestMultiStateImpl(OvalDefinitionsDocument parentDocument)
    {
    	super(parentDocument);
    }

    @Override
    public List<OvalState> getStates()
    {
        List<OvalState> ret = new ArrayList<OvalState>();

        List stateRefElements = getElement().getChildren("state", getElement().getNamespace());

        if(stateRefElements == null)
        {
                return null;
        }

        for(int x = 0 ; x < stateRefElements.size(); x++)
        {
            Object o = stateRefElements.get(x);
            
            if(!(o instanceof Element))
            {
                continue;
            }
            
            Element stateRefElement = (Element) stateRefElements.get(x);
            
            String stateIdToFind = stateRefElement.getAttributeValue("state_ref");
           
            if(stateIdToFind == null)
            {
                continue;
            }
            
            OvalState os = getParentDocument().getOvalState(stateIdToFind);
            
            if(os == null)
            {
                os = new UnresolvedOvalState(getParentDocument());
                os.setId(stateIdToFind);
            }

            ret.add(os);
        }

        return ret;
    }

    @Override
    public OvalState getState()
    {
        OvalState ret = null;

        List<OvalState> states = getStates();

        if(states != null && states.size() > 0)
        {
            ret = states.get(0);
        }

        return ret;
    }

   /**
     * Set the states this test points to.  Only valid for 5.6+ of oval where
     * multiple states are permitted.
     *
     * @param states
     */
    @Override
    public void setStates(List<OvalState> states)
    {
        Namespace ns = getElement().getNamespace();

        getElement().removeChildren("state", ns);

        for(int x = 0; x < states.size() ; x++)
        {
            OvalState os = states.get(x);

            Element stateElement = new Element("state", ns);
            stateElement.setAttribute("state_ref", os.getId());
            getElement().addContent(stateElement);
        }
    }

   /**
    * @{@inheritDoc}
    */
    @Override
    public OvalStateOperatorEnum getStateOperator()
    {
        OvalStateOperatorEnum ret = OvalStateOperatorEnum.AND;

        Attribute opAtt =  element.getAttribute("state_operator");

        if(opAtt != null)
        {
            ret = OvalStateOperatorEnum.valueOf(opAtt.getValue().toUpperCase());
        }
        else
        {
            ret =  OvalStateOperatorEnum.AND;
        }

        return ret;
    }

   /**
    * @{@inheritDoc}
    */
    @Override
    public void setStateOperator(OvalStateOperatorEnum operator)
    {
        if(getElement() != null)
        {
            if(operator == null)
            {
                getElement().removeAttribute("state_operator");
            }
            else
            {
                getElement().setAttribute("state_operator", operator.toString());
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
		if (!(other instanceof OvalTestMultiStateImpl))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalTestMultiStateImpl other2 = (OvalTestMultiStateImpl) other;
		
		// If the other object's check_existence attribute does not match,
		// then return false.
		if (!(this.getStateOperator().toString().equals(other2.getStateOperator().toString())))
		{
			return false;
		}
    	
		// Return true if we get to this point.
		return true;
	}
}
