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

public class OvalTestSingleStateImpl extends OvalTest
{
	public OvalTestSingleStateImpl(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}

    @Override
    /**
     * Get references to the states referred to by this test.
     * Oval 5.6 introduced the ability to tie an oval test to multiple states
     *
     * Versions of oval prior to 5.6 will return 0 or 1 element list.
     *
     * @return List<OvalState>
     */
    public List<OvalState> getStates()
    {
        List<OvalState> ret = new ArrayList<OvalState>();

        Element stateRefElement = getElement().getChild("state", getElement().getNamespace());

        if(stateRefElement != null)
        {
            String stateIdToFind = stateRefElement.getAttributeValue("state_ref");

            if(stateIdToFind != null)
            {
                OvalState os = getParentDocument().getOvalState(stateIdToFind);

                if(os == null)
                {
                    os = new UnresolvedOvalState(getParentDocument());
                    os.setId(stateIdToFind);
                }
                
                ret.add(os);
            }
        }
        return ret;
    }

    @Override
    public OvalState getState()
    {
        OvalState ret = null;

        List<OvalState> states = getStates();

        if(states.size() > 0)
        {
            ret = states.get(0);
        }

        return ret;
    }

    @Override
    /**
     * Set the states this test points to.  Only valid for 5.6+ of oval where
     * multiple states are permitted.  For lower versions of oval we'll throw an exception if more than one state is supplied
     *
     * @param states
     */
    public void setStates(List<OvalState> states)
    {
        Namespace ns = getElement().getNamespace();

        getElement().removeChildren("state", ns);

        if(states != null)
        {
            if(states.size() > 1)
            {
                throw new IllegalStateException("This version of oval doesn't support setting more than one state per test.  Please use setStateId() instead.");
            }

            for(int x = 0; x < states.size() ; x++)
            {
                OvalState os = states.get(x);

                Element stateElement = new Element("state", ns);
                stateElement.setAttribute("state_ref", os.getId());
                getElement().addContent(stateElement);
            }
        }
    }

    /**
     * Get the operator.  e.g. AND, OR, etc.
     *
     * @return OvalStateOperatorEnum
     */
    public OvalStateOperatorEnum getStateOperator()
    {
        throw new UnsupportedOperationException("This version of an OVAL test does not support the state_operator attribute");
    }

   /**
     * Set the operator.  e.g. AND, OR, etc.
     *
     * @param operator OvalStateOperatorEnum
     */
    public void setStateOperator(OvalStateOperatorEnum operator)
    {
        throw new UnsupportedOperationException("This version of an OVAL test does not support the state_operator attribute");
    }
}
