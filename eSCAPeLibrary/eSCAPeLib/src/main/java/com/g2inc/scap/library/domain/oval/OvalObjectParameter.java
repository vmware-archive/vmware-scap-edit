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

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * This represents what the oval schema refers to as an entity for an object.
 * An example would be a hive for a registry object.  The hive is the parameter
 * or entity.
 */
public abstract class OvalObjectParameter extends OvalObjectChild
{
	private static Logger log = Logger.getLogger(OvalObjectParameter.class);
	public static final String RECURSE_DIRECTION_DEFAULT = "oval.object.parameter.recurse.direction.default";
    private OvalEntity entity = null;
    
	public OvalObjectParameter(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}

	/**
	 * Get the value.
	 * 
	 * @return String
	 */
	public String getValue()
	{
		String value = getElement().getValue();		
		return value;
	}	

	/**
	 * An object parameter can reference a variable.  This method returns that variable
	 * if it's set.
	 * 
	 * @return OvalVariable
	 */
	public OvalVariable getVariableReference()
	{
		OvalVariable ret = null;
		
		String varId = getElement().getAttributeValue("var_ref");

        if(varId == null || varId.length() > 0)
        {
            // this could be a var_ref element so we'll get the varId differently
            if(getElement().getName().equals("var_ref"))
            {
                varId = getElement().getValue();
            }
        }

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

	/**
	 * Set the value.
	 * 
	 * @param value The value to set
	 */
	public void setValue(String value)
	{
		if(value != null)
		{
			getElement().setText(value);
		}
	}

	/**
	 * Set the variable this parameter will reference.
	 * 
	 * @param ov An oval variable
	 */
	public void setVariableReference(OvalVariable ov)
	{
        if(ov != null)
        {
            if(getElement().getName().toLowerCase().equals("var_ref"))
            {
                getElement().setText(ov.getId());
            }
            else
            {
                getElement().setAttribute("var_ref", ov.getId());
            }
        }
        else
        {
            getElement().removeAttribute("var_ref");
        }
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
     * Get the operation to be performed for this parameter.
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
	 * Set the datatype for this parameter.
	 * 
	 * @param datatype The data type to set
	 */
	public void setDatatype(String datatype)
	{
		if(datatype != null)
		{
            if(datatype.equals("enumerated"))
            {
                datatype = "string";
            }
			getElement().setAttribute("datatype", datatype);
		}
        else
        {
            getElement().removeAttribute("datatype");
        }
	}

	/**
	 * Set the operation for this paramter.
	 * 
	 * @param operation The operation to set
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
     * @param vc The value to set
     */
    public void setVarCheck(String vc)
    {
        if(vc == null)
        {
            // TODO: programatically ask schema for default
            getElement().removeAttribute("var_check");
        }
        else
        {
            getElement().setAttribute("var_check", vc);
        }
    }

//    /**
//     * Set the value of the var_check attribute.
//     * 
//     * @param vc The value to set
//     */
//    public void setVarCheck(CheckEnumeration vc)
//    {
//        if(vc == null)
//        {
//            // TODO: programatically ask schema for default
//            getElement().removeAttribute("var_check");
//        }
//        else
//        {
//            getElement().setAttribute("var_check", vc.toString());
//        }
//    }

    /**
     * Get the value of var_ref attribute.
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
     * @param vr The value to set
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
     * @param entity An OvalEntity
     */
    public void setEntity(OvalEntity entity)
    {
        this.entity = entity;
        setDatatype(this.entity.getDatatype().getType().toString().toLowerCase());
    }

    /**
     * See if this parameter is set to Nil.
     * 
     * @return boolean
     */
    public boolean isNil()
    {
        String nilValString = getElement().getAttributeValue("nil",XSI_NS);

        if(nilValString != null)
        {
            return Boolean.parseBoolean(nilValString);
        }
        else
        {
            // must not be defined, default value is false
            return false;
        }
    }

    /**
     * Set this parameter to Nil.
     * 
     * @param nil Whether or not this parameter is nil
     */
    public void setNil(boolean nil)
    {
        if(nil)
        {
            getElement().setAttribute("nil", nil + "", XSI_NS);
        }
        else
        {
            getElement().removeAttribute("nil", XSI_NS);
        }
    }

    @Override
    public boolean equals(Object o)
    {
    	boolean ret = false;
    	
    	if(o == null)
    	{
    		return ret;
    	}
    	
    	if(!(o instanceof OvalObjectParameter))
    	{
    		return ret;
    	}
    	
    	OvalObjectParameter other = (OvalObjectParameter)o;
    	
    	if(getDatatype() != null)
    	{
    		if(!getDatatype().equals(other.getDatatype()))
    		{
    			return ret;
    		}
    	}

    	if(getOperation() != null)
    	{
    		if(!getOperation().equals(other.getOperation()))
    		{
    			return ret;
    		}
    	}

    	if(getValue() != null)
    	{
    		if(!getValue().equals(other.getValue()))
    		{
    			return ret;
    		}
    	}

    	if(getVarCheck() != null)
    	{
    		if(!getVarCheck().equals(other.getVarCheck()))
    		{
    			return ret;
    		}
    	}

    	if(getVarRef() != null)
    	{
    		if(!getVarRef().equals(other.getVarRef()))
    		{
    			return ret;
    		}
    	}
    	
    	if(isNil() != other.isNil())
    	{
    		return ret;
    	}
    	
    	ret = true;
    	return ret;	
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
		if (!(other instanceof OvalObjectParameter))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalObjectParameter other2 = (OvalObjectParameter) other;
		
		// If the other object's datatype attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getDatatype(), this.getDatatype())))
    	{
    		return false;
    	}
    	
		// If the other object's operation attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getOperation(), this.getOperation())))
    	{
    		return false;
    	}
    	
		// If the other object's value does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getValue(), this.getValue())))
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
    	
		// If the other object's mask attribute does not match,
		// then return false.
    	if (other2.getMask() != this.getMask())
    	{
    		return false;
    	}
    	
		// Return true if we get to this point.
		return true;
    }
}
