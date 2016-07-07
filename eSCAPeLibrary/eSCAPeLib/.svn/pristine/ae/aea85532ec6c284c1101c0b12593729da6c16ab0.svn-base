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
 * Represents any sub element of an oval state.
 */
public abstract class OvalStateParameter extends OvalElement
{
	private static Logger log = Logger.getLogger(OvalStateParameter.class);
	
    private OvalEntity entity = null;

	public OvalStateParameter(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}
	
	@Override
	public boolean equals(Object o)
	{
		boolean ret = false;
	
		if(o == null)
		{
			return ret;
		}
		
		if(!(o instanceof OvalStateParameter))
		{
			return ret;
		}
		
		OvalStateParameter other = (OvalStateParameter) o;

		// check element name
		if(getElementName() != null)
		{
			if(!getElementName().equals(other.getElementName()))
			{
				return ret;
			}
		}
		
		// check operation
		if(getOperation() != null)
		{
			if(!getOperation().equals(other.getOperation()))
			{
				return ret;
			}
		}

		// check EntityCheck
		if(getEntityCheck() != null)
		{
			if(!getEntityCheck().equals(other.getEntityCheck()))
			{
				return ret;
			}
		}

		// check Value
		if(getValue() != null)
		{
			if(!getValue().equals(other.getValue()))
			{
				return ret;
			}
		}

		// check VarCheck
		if(getVarCheck() != null)
		{
			if(!getVarCheck().equals(other.getVarCheck()))
			{
				return ret;
			}
		}
		
		// check variable reference
		if(getVarRef() != null)
		{
			if(!getVarRef().equals(other.getVarRef()))
			{
				return ret;
			}
		}
		
		// check data type
		if(getDatatype() != null)
		{
			if(!getDatatype().equals(other.getDatatype()))
			{
				return ret;
			}
		}
		
		List<OvalStateParameter> thisFieldParms = getFieldParameters();
		List<OvalStateParameter> otherFieldParms = other.getFieldParameters();
		if (thisFieldParms.size() == otherFieldParms.size() && thisFieldParms.size() > 0) {
			for (int i=0; i<thisFieldParms.size(); i++) {
				OvalStateParameter thisFieldParm = thisFieldParms.get(i);
				OvalStateParameter otherFieldParm = otherFieldParms.get(i);
				if (!thisFieldParm.equals(otherFieldParm)) {
					return ret;
				}
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
		if (!(other instanceof OvalStateParameter))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalStateParameter other2 = (OvalStateParameter) other;
		
		// If the other object's operation attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getOperation(), this.getOperation())))
    	{
    		return false;
    	}
    	
		// If the other object's entity_check attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getEntityCheck(), this.getEntityCheck())))
    	{
    		return false;
    	}
    	
		// If the other object's var_check attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getVarCheck(), this.getVarCheck())))
    	{
    		return false;
    	}
    	
		// If the other object's var_ref attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getVarRef(), this.getVarRef())))
    	{
    		return false;
    	}
    	
		// If the other object's datatype attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getDatatype(), this.getDatatype())))
    	{
    		return false;
    	}
    	
		// If the other object's mask attribute does not match,
		// then return false.
    	if (other2.getMask() != this.getMask())
    	{
    		return false;
    	}
    	
		// If the other object's value does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getValue(), this.getValue())))
    	{
    		return false;
    	}
		List<OvalStateParameter> thisFieldParms = getFieldParameters();
		List<OvalStateParameter> otherFieldParms = other2.getFieldParameters();
		if (thisFieldParms.size() == otherFieldParms.size() && thisFieldParms.size() > 0) {
			for (int i=0; i<thisFieldParms.size(); i++) {
				OvalStateParameter thisFieldParm = thisFieldParms.get(i);
				OvalStateParameter otherFieldParm = otherFieldParms.get(i);
				if (!thisFieldParm.isDuplicateOf(otherFieldParm)) {
					return false;
				}
			}
		}
		
		// Return true if we get to this point.
		return true;
	}
    
	/**
	 * Return the value of the mask attribute.
	 * 
	 * @return boolean
	 */
    public boolean getMask()
	{
        boolean mask = false;
		String maskString = getElement().getAttributeValue("mask");
        if(maskString != null)
        {
            try
            {
            	mask = Boolean.parseBoolean(maskString);
            }
            catch(Exception e)
            {
                log.error("Unable to parse value as boolean: " + maskString, e);
            }
        }
        return mask;
	}

    /**
     * Sets the value of the mask attribute.
     * 
     * @param b
     */
    public void setMask(boolean b)
    {
        getElement().removeAttribute("mask");
        getElement().setAttribute("mask", b + "");
    }
	
	/**
	 * Get the datatype of this parameter.
	 * 
	 * @return String
	 */
	public String getDatatype()
	{
        String ret = "string";

		String datatype = getElement().getAttributeValue("datatype");

        if(datatype != null && datatype.length() > 0)
        {
            ret = datatype;
        }

		return ret;
	}

	/**
	 * Get the operation of this parameter.
	 * 
	 * @return String
	 */
	public String getOperation()
	{
        String ret = "equals";

		String operation = getElement().getAttributeValue("operation");

        if(operation != null && operation.length() > 0)
        {
            ret = operation;
        }

        return ret;
	}

	/**
	 * Get the value of this parameter.
	 * 
	 * @return String
	 */
	public String getValue()
	{
		String value = getElement().getValue();
		return value;
	}
	
	/**
	 * Set the datatype of this parameter.
	 * 
	 * @param datatype The data type to set
	 */
	public void setDatatype(String datatype)
	{
		if(datatype != null)
		{
			getElement().setAttribute("datatype", datatype);
		}
        else
        {
            getElement().removeAttribute("datatype");
        }
	}

	/**
	 * Set the operation of this parameter.
	 * 
	 * @param operation
	 */
	public void setOperation(String operation)
	{
		if(operation != null)
		{
			getElement().setAttribute("operation", operation);
		}
        else
        {
            getElement().removeAttribute("operation");
        }
	}

	/**
	 * Set the value of this parameter.
	 * 
	 * @param value The value to set
	 */
	public void setValue(String value)
	{
		if(value != null)
		{
			getElement().setText(value);
		}
        else
        {
            getElement().setText("");
        }
	}

	/**
	 * Get the value of the entity_check attribute.
	 * 
	 * @return String
	 */
    public String getEntityCheck()
    {
        // TODO: programatically ask schema for default
        String entityCheckDefault = "all";
        String entityCheck = getElement().getAttributeValue("entity_check");

        if(entityCheck == null)
        {
            entityCheck = entityCheckDefault;
        }

        return entityCheck;
    }

    /**
     * Set the value of the entity_check attribute.
     * 
     * @param ec
     */
    public void setEntityCheck(String ec)
    {
        if(ec == null)
        {
            // TODO: programatically ask schema for default
            ec = "all";
        }

        getElement().setAttribute("entity_check", ec);
    }

    /**
     * Get the value of the var_check attribute.
     * 
     * @return String
     */
    public String getVarCheck()
    {
        // TODO: programatically ask schema for default
        String varCheckDefault = "all";

        String varCheck =  getElement().getAttributeValue("var_check");

        if(varCheck == null)
        {
            varCheck = varCheckDefault;
        }

        return varCheck;
    }

    /**
     * Set the value of the var_check attribute.
     * 
     * @param vc
     */
    public void setVarCheck(String vc)
    {
        if(vc == null)
        {
            // TODO: programatically ask schema for default
            vc = "all";
        }

        getElement().setAttribute("var_check", vc);
    }

    /**
     * Get the value of the var_ref attribute.
     * 
     * @return String
     */
    public String getVarRef()
    {
        String varCheck =  getElement().getAttributeValue("var_ref");
        return varCheck;
    }

    /**
     * Set the value of the var_ref attribute.
     * 
     * @param vr
     */
    public void setVarRef(String vr)
    {
        if(vr == null)
        {
            getElement().removeAttribute("var_ref");
        }
        else
        {
            getElement().setAttribute("var_ref", vr);
        }
    }

    /**
     * Get the OvalEntity object for this parameter.
     * 
     * @return OvalEntity
     */
    public OvalEntity getEntity()
    {
        return entity;
    }

    /**
     * Set the OvalEntity object for this parameter.
     * 
     * @param entity
     */
    public void setEntity(OvalEntity entity)
    {
        this.entity = entity;
    }

    /**
     * Parameters can reference variables.  This method returns the variable to which
     * this parameter points.
     * 
     * @return OvalVariable
     */
    public OvalVariable getVariableReference()
	{
		OvalVariable ret = null;

		String varId = getVarRef();

		if(varId != null && varId.length() > 0)
		{
            Element variablesElement = getRoot().getChild("variables", getRoot().getNamespace());
            if(variablesElement != null)
            {
                List children = variablesElement.getChildren();

                if(children != null && children.size() > 0)
                {
                    for(int x = 0; x < children.size(); x++)
                    {
                        Element child = (Element) children.get(x);

                        String elemVarId = child.getAttributeValue("id");

                        if(elemVarId!= null && elemVarId.equals(varId))
                        {
                            OvalVariable ov = getParentDocument().getVariableWrapper(child.getName());
                            ov.setElement(child);
                            ov.setRoot(getRoot());

                            ret = ov;
                            break;
                        }
                    }
                }
            }
            else
            {
                log.debug(this.getClass().getName() + ": variablesElement is NULL!");
            }
		}

		return ret;
	}
    
    public OperEnum getOperationEnum() {
    	String operation = getOperation();
    	return OperEnum.valueOfXmlName(operation);
    }
    
    public void setOperationEnum(OperEnum operEnum) {
    	setOperation(operEnum.toString());
    }
    
	/**
	 * Returns a list of "field" child parameters for this "result" parameter.
	 * 
	 * @return List<OvalStateParameter>
	 */
	public List<OvalStateParameter> getFieldParameters() {
		List<OvalStateParameter> fieldParms = new ArrayList<OvalStateParameter>();
		if (getElementName().equals("result")) {
			List<Element> children = getElement().getChildren();
			for (Element child : children) {
				if (child.getName().equals("field")) {
					OvalStateParameter fieldOsp = getParentDocument().getStateParameterWrapper();
					fieldOsp.setElement(child);
					fieldOsp.setRoot(getRoot());
					fieldParms.add(fieldOsp);
				}
			}
		}
		return fieldParms;
	}
}
