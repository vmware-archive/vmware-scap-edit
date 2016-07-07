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

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Represents a criteria element of an oval definition.
 *
 * @see OvalDefinition
 */
public class Criteria extends CriteriaChild
{
    private static Logger log = Logger.getLogger(Criteria.class);
    public static final String ELEMENT_NAME = "criteria";
	public final static HashMap<String, Integer> CRITERIA_ORDER = new HashMap<String, Integer>();
	static
	{
		CRITERIA_ORDER.put("criteria", 0);
		CRITERIA_ORDER.put("criterion", 0);
		CRITERIA_ORDER.put("extend_definition", 0);
	}
    
	public Criteria(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}


    /**
     * Get the operator.  e.g. AND, OR, etc.
     * 
     * @return OvalCriteriaOperatorEnum
     */
	public OvalCriteriaOperatorEnum getOperator()
	{
		OvalCriteriaOperatorEnum ret = OvalCriteriaOperatorEnum.AND;
		
		Attribute opAtt =  element.getAttribute("operator");
		
		if(opAtt != null)
		{
			ret = OvalCriteriaOperatorEnum.valueOf(opAtt.getValue().toUpperCase());
		}
		return ret;
	}

    @Override
	public String toString()
    {
        return "Criteria(" + getOperator().toString() + ")";
    }

    /**
     * Set the operator.  e.g. AND, OR, etc.
     * 
     * @param operator
     */
	public void setOperator(OvalCriteriaOperatorEnum operator)
	{
		getElement().setAttribute("operator", operator.toString());
	}

	/**
	 * Create a Criterion object suitable for adding to this Criteria.
	 * 
	 * @return Criterion
	 */
	public Criterion createCriterion()
	{
		Element e = new Element(Criterion.ELEMENT_NAME, getElement().getNamespace());
		Criterion c = getParentDocument().getCriterionWrapper();
		c.setElement(e);
		c.setRoot(getRoot());
		c.setSCAPDocument(getSCAPDocument());
		
		return c;
	}

	/**
	 * Create an ExtendDefinition object suitable for adding to this Criteria.
	 * 
	 * @return ExtendDefinition
	 */
	public ExtendDefinition createExtendDefinition()
	{
		Element e = new Element(ExtendDefinition.ELEMENT_NAME, getElement().getNamespace());
		ExtendDefinition eDef = getParentDocument().getExtendDefinitionWrapper();
		eDef.setElement(e);
		eDef.setRoot(getRoot());
		eDef.setSCAPDocument(getSCAPDocument());
		
		return eDef;
	}

	/**
	 * Add a child criteria to this one.
	 * 
	 * @param criteriaChild the child element (criterion, extend_definition, or another criteria) to be added.
	 */
	public void addChild(CriteriaChild criteriaChild)
	{
		insertChild(criteriaChild, CRITERIA_ORDER, -1);
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
		if (!(other instanceof Criteria))
		{
			return false;
		}
		
		// Cast the other object to this class.
		Criteria other2 = (Criteria) other;
		
		// If the other object's operator attribute does not match,
		// then return false.
		if (!(this.getOperator().toString().equals(other2.getOperator().toString())))
		{
			return false;
	}

		// Return true if we get to this point.
		return true;
	}
}
