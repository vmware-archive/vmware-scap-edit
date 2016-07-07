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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import com.g2inc.scap.library.schema.NameDoc;

/**
 * Represents any *_state element in an oval definitions document.
 */
public abstract class OvalState extends VersionedOvalElementImpl
{
    private static Logger log = Logger.getLogger(OvalState.class);

	public OvalState(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}
	
	/**
	 * Get an OVAL notes object. Returns null if the notes element does not exist.
	 * 
	 * @return OvalNotes
	 */
	public OvalNotes getOvalNotes()
	{
		OvalNotes ret = null;
		Namespace ovaldefNS = getElement().getNamespace();
		Element notes = getElement().getChild("notes", ovaldefNS);
		if(notes != null)
		{
			ret = getParentDocument().getOvalNotesWrapper();
			ret.setElement(notes);
			ret.setRoot(getRoot());
		}
		return ret;
	}
	
	/**
	 * Returns a list of parameters for this state.
	 * 
	 * @return List<OvalStateParameter>
	 */
	public List<OvalStateParameter> getParameters()
	{
        List<OvalEntity> validParms  = getValidParameters();

		List<OvalStateParameter> parameters = new ArrayList<OvalStateParameter>();
		List children = getElement().getChildren();
		for(int x = 0; x < children.size();x++)
		{
			Element child = (Element) children.get(x);

			OvalStateParameter osp = getParentDocument().getStateParameterWrapper();

            int index = validParms.indexOf(new NameDoc(child.getName(),null));
            if(index > -1)
            {
                OvalEntity oe = validParms.get(index);
                osp.setEntity(oe);
            }

			osp.setElement(child);
			osp.setRoot(getRoot());

			parameters.add(osp);
		}
		
		return parameters;
	}
	

	/**
	 * Sets this state's parameters using a collection.
	 * 
	 * @param parms A collection of parameters to set
	 */
    public void setParameters(Collection<OvalStateParameter> parms)
    {
        if(parms == null || parms.size() == 0)
        {
            getElement().removeContent();
            return;
        }

        getElement().removeContent();

        // TODO: make sure parms are sorted according to sequence in schema
        Iterator<OvalStateParameter> itr = parms.iterator();

        while(itr.hasNext())
        {
            OvalStateParameter osp = itr.next();

            getElement().addContent(osp.getElement());
        }
    }

    /**
     * States can reference variables.  This method returns a list of
     * variables directly referenced by this state.
     * 
     * @return List<OvalVariable>
     */
	public List<OvalVariable> getReferencedVariables()
	{
		Set<String> varIdsReferenced = new HashSet<String>(); 
		List<OvalVariable> variables = new ArrayList<OvalVariable>();
		
		refVariableRecurser(getElement(), varIdsReferenced);
		if (varIdsReferenced.size() > 0) {
		//	log.debug("getReferencedVariables found " + varIdsReferenced.size() + " variable references in state " + getId());
			for (String varId : varIdsReferenced) {
			//	log.debug("getReferencedVariables found variable " + varId);
			}
		} 
		
		Element variablesElement = getRoot().getChild("variables", getRoot().getNamespace());
		
		if(variablesElement != null)
		{
			List children = variablesElement.getChildren();
			
			if(children != null && children.size() > 0)
			{
				for(int index = 0; index < children.size(); index++)
				{
					Element child = (Element) children.get(index);
					
					String variableId = child.getAttributeValue("id");
					
					if(variableId != null)
					{
						if(varIdsReferenced.contains(variableId))
						{
							OvalVariable ov = getParentDocument().getVariableWrapper(child.getName());
							ov.setElement(child);
							ov.setRoot(getRoot());
							
							variables.add(ov);
						}
					}
					else
					{
				//		log.debug("OvalState: getReferencedVariables: variableId is NULL");						
					}
				}
			}
		}
		else
		{
//			log.debug("OvalState: getReferencedVariables: variablesElement is NULL");
		}
		
		return variables;
	}

	private void refVariableRecurser(Element element, Set<String> referencedIds)
	{
		String id = null;
		
		if(element.getName().toLowerCase().endsWith("var_ref"))
		{
			id = element.getValue();
		}
		else
		{
			id = element.getAttributeValue("var_ref");
		}

		if(id != null && id.length() > 0)
		{
			referencedIds.add(id);
		}
		
		List children = element.getChildren();
		
		if(children != null && children.size() > 0)
		{
			for(int x = 0; x < children.size();x++)
			{
				Element child = (Element) children.get(x);
				
				refVariableRecurser(child, referencedIds);
			}
		}
	}

    @Override
    public String toString()
    {
        StringBuilder ret = new StringBuilder();
     
        ret.append(getElementName());
        ret.append("(" + getId() + ")");

        return ret.toString();
    }
    
    /**
     * Get the comment.
     * 
     * @return String
     */
    public String getComment()
	{
		String comment = getElement().getAttributeValue("comment");
		return comment;
	}

    /**
     * Set the comment.
     * 
     * @param comment The comment to set
     */
    public void setComment(String comment)
	{
        if(comment == null)
        {
            getElement().removeAttribute("comment");
        }
        else
        {
            getElement().setAttribute("comment", comment);
        }
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
     * Get a list of the possible valid parameters for this state.
     * 
     * @return List<OvalEntity>
     */
    public List<OvalEntity> getValidParameters()
    {
    	OvalDefinitionsDocument odd = getParentDocument();
    	String platform = getPlatform();
    	String stateType = getElementName();
    	return odd.getValidStateEntityTypes(platform, stateType);
    }
    
    public HashMap<String, Integer> getParameterOrderMap() {
        HashMap<String, Integer> parmOrder = new HashMap<String, Integer>();
        List<OvalEntity> validParms = getValidParameters();
        for (int i=0; i<validParms.size(); i++) {
            parmOrder.put(validParms.get(i).getName(), i);
        }
        return parmOrder;
        
    }
    
    /**
     * Creates and returns a new oval state parameter that can be added to 
     * this state.
     * 
     * @param oe An OvalEntity
     * 
     * @return OvalStateParameter
     */
    public OvalStateParameter createOvalStateParameter(String parmName)
    {
    	OvalEntity ovalEntity = null;
    	for (OvalEntity oe : getValidParameters()) {
    		if (parmName.equals(oe.toString()) ) {
    			ovalEntity = oe;
    			break;
    		}
    	}
    	if (ovalEntity == null) {
    		throw new IllegalArgumentException("Parameter name " + parmName + " is not valid for state " + getElement().getName());
    	}
    	
    	return createOvalStateParameter(ovalEntity);
    }

    /**
     * Creates and returns a new oval state parameter that can be added to 
     * this state.
     * 
     * @param oe An OvalEntity
     * 
     * @return OvalStateParameter
     */
    public OvalStateParameter createOvalStateParameter(OvalEntity oe)
    {
        Element ospElement = new Element(oe.toString());
        ospElement.setNamespace(getElement().getNamespace());

        OvalStateParameter osp = getParentDocument().getStateParameterWrapper();

        osp.setElement(ospElement);
        osp.setRoot(getRoot());

        TypeEnum typeEnum = oe.getDatatype().getType();
        String datatype = (typeEnum==TypeEnum.ENUMERATED ? "string" : typeEnum.name().toLowerCase());
        osp.setDatatype(datatype);
        osp.setOperation("equals");
        osp.setEntity(oe);
        return osp;
    }

    /**
     * Add a new parameter to this state.
     * 
     * @param osp An oval state parameter
     */
    public void addParameter(OvalStateParameter osp)
    {
    	String parmType = osp.getElementName();
    	// if this parameter is already a child of this OvalState, remove it first
    	List<Element> childElements = getElement().getChildren();
    	for (Element childElement : childElements ) {
    		if (childElement.getName().equals(parmType)) {
    			getElement().removeContent(childElement);
    		}
    	}
    	
    	// build temporary order map. could make this static, but not used very often...
    	HashMap<String, Integer> orderMap = getParameterOrderMap();
    	insertChild(osp.getElement(), orderMap);
    }

    /**
     * create an object filter pointing to this state, suitable for use in a set
     * defined in an object.
     * 
     * @return OvalObjectFilter
     */
    public OvalObjectFilter createObjectFilter()
    {
        Element e = new Element("filter", getRoot().getNamespace());

        e.setText(getId());

        OvalObjectFilter ret = getParentDocument().getObjectFilterWrapper();
        ret.setElement(e);
        ret.setRoot(getRoot());

        return ret;
    }

    /**
     * Overridden version of the clone() method.  Makes a deep copy of this state.
     * 
     * @return OvalState
     */
    @Override
    public OvalState clone()
    {
        OvalState ret = null;

        ret = getParentDocument().createState(getPlatform(), getElementName());

        if(ret == null)
        {
            log.error("clone(): State created by parent was null!");
            return null;
        }

        String newId = ret.getId();

        ret.setElement((Element) getElement().clone());

        ret.setAttribute("id", newId);

        return ret;
    }

    /**
     * See if any of the text fields in this state match the find string.
     * 
     * @param findString The string to look for
     * 
     * @return boolean
     */
    @Override
    public boolean matches(String findString)
    {
        if(findString == null || findString.length() == 0)
        {
            return true;
        }

        String lcFindString = findString.toLowerCase();

        String id = getId();
        if(id != null)
        {
        	if(id.toLowerCase().indexOf(lcFindString) > -1)
        	{
        		return true;
        	}
        }

        String comment = getComment();
        if(comment != null)
        {
            if(comment.toLowerCase().indexOf(lcFindString) > -1)
            {
                // matches
                return true;
            }
        }

        // check parameters

        List<OvalStateParameter> parms = getParameters();

        if(parms != null && parms.size() > 0)
        {
            for(int x = 0; x < parms.size(); x++)
            {
                OvalStateParameter osp = parms.get(x);

                String value = osp.getValue();

                if(value != null)
                {
                    if(value.toLowerCase().indexOf(lcFindString) > -1)
                    {
                        return true;
                    }
                }
                List<OvalStateParameter> fieldParms = osp.getFieldParameters();
                for (OvalStateParameter fieldParm : fieldParms) {
                	value = fieldParm.getValue();
                    if(value.toLowerCase().indexOf(lcFindString) > -1) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * See if this state's parameters match the ones passed in to us
     * in the template.
     * 
     * @param template Another oval state to compare ourselves with
     * 
     * @return boolean
     */
    public boolean matches(OvalState template)
    {
    	boolean ret = false;
    	
    	List<OvalStateParameter> templateParms = template.getParameters();
		List<OvalStateParameter> parms = getParameters();
		
		if(!getElementName().equals(template.getElementName()))
		{
			return ret;
		}
		
		if(parms == null || parms.size() == 0)
		{
			return ret;
		}
		
		int foundParms = 0;
		
		for(int x = 0; x < templateParms.size(); x++)
		{
			OvalStateParameter tparm = templateParms.get(x);

			for(int y = 0; y < parms.size(); y++)
			{
				OvalStateParameter parm = parms.get(y);
				
				if(tparm.equals(parm))  // equals method of OvalStateParameter checks for field (child) parameters, no need to check here
				{
					foundParms++;
				}
			}
		}
				
		if(foundParms == templateParms.size())
		{
			ret = true;
		}
		
		return ret;
    }

    @Override
    public String getOvalIdType() {
        return "ste";
    }
    
    /**
     * Return a list of ids of entities this state references but
     * do not exist in the document.
     * 
     * @return Set<String>
     */
    public Set<String> getBrokenRefs()
    {
    	Set<String> brokenRefs = new HashSet<String>();
    	OvalDefinitionsDocument odd = getParentDocument();
    	
		Set<String> varIdsReferenced = new HashSet<String>(); 		
		refVariableRecurser(getElement(), varIdsReferenced);

    	if(varIdsReferenced != null && varIdsReferenced.size() > 0)
    	{
    		Iterator<String> idItr = varIdsReferenced.iterator();
    		
    		while(idItr.hasNext())
    		{
    			String varId = idItr.next();
    			if(!odd.containsVariable(varId))
    			{
    				log.error("OvalState " + getId() + " points to variable " + varId + " which doesn't exist");
    				brokenRefs.add(varId);
    			}
    		}
    	}
    	
    	return brokenRefs;
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
		if (!(other instanceof OvalState))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalState other2 = (OvalState) other;
		
		// If the other object's comment attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getComment(), this.getComment())))
    	{
    		return false;
    	}
    	
		// If the other object's operator attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getOperator().toString(), this.getOperator().toString())))
    	{
    		return false;
    	}
    	
    	// If the other object's notes element does not match,
    	// then return false.
    	OvalNotes myNotes = this.getOvalNotes();
    	OvalNotes otherNotes = other2.getOvalNotes();
    	if (myNotes != null && otherNotes != null)
    	{
    		if (!(myNotes.isDuplicateOf(otherNotes)))
    		{
    			return false;
    		}
    	}
    	else if (myNotes == null && otherNotes != null)
    	{
    		return false;
    	}
    	else if (myNotes != null && otherNotes == null)
    	{
    		return false;
    	}
    	
    	// If the other object's child parameters do not match,
    	// then return false.
    	List<OvalStateParameter> myParms = this.getParameters();
    	List<OvalStateParameter> otherParms = other2.getParameters();
    	if (myParms.size() != otherParms.size())
    	{
    		return false;
    	}
    	for (int i = 0; i < myParms.size(); i++)
    	{
    		OvalStateParameter myParm = myParms.get(i);
    		OvalStateParameter otherParm = otherParms.get(i);
    		if (!(myParm.isDuplicateOf(otherParm)))
    		{
    			return false;
    		}
    	}
		
		// Return true if we get to this point.
    	return true;
    }

    public OvalEntity getOvalEntityByName(String name)
    {
        OvalEntity result = null;
        List<OvalEntity> list = getValidParameters();
        for (OvalEntity entity : list)
        {
            if (entity.getName().equals(name))
            {
                result = entity;
                break;
            }
        }
        return result;
    }
}