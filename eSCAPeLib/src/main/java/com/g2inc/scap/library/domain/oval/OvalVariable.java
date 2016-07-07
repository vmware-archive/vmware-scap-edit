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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Represents any of the *_variable elements in an oval definitions document.
 */
public abstract class OvalVariable extends VersionedOvalElementImpl implements OvalVariableContent
{
	private static Logger log = Logger.getLogger(OvalVariable.class);
	
	public OvalVariable(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
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
	 * Get the datatype.
	 * 
	 * @return String
	 */
	public String getDatatype()
	{
		String version = getElement().getAttributeValue("datatype");
		return version;
	}

	/**
	 * Create an object component suitable for adding to this variable as a child.
	 * An object component is a reference to an oval object.
	 * 
	 * @return OvalVariableComponentObject
	 */
    public OvalVariableComponentObject createObjectComponent() {
        Element e = new Element("object_component", getElement().getNamespace());
        OvalVariableComponentObject ret = getParentDocument().getObjectVariableComponentWrapper();
        ret.setElement(e);
        ret.setRoot(getRoot());
        return ret;
    }

    /**
     * Create a variable component suitable for adding to this variable as a child.
     * A variable component is a reference to another oval variable.
     * 
     * @return OvalVariableComponentVariable
     */
    public OvalVariableComponentVariable createVariableComponent() {
        Element e = new Element("variable_component", getElement().getNamespace());
        OvalVariableComponentVariable ret = getParentDocument().getVariableComponentWrapper();
        ret.setElement(e);
        ret.setRoot(getRoot());
        return ret;
    }

    
    /**
     * Create a literal component suitable for adding to this variable as a child.
     * A variable component is a static value assigned in the component itself and not
     * a reference to another object or variable.
     * 
     * @return OvalVariableComponentVariable
     */
    public OvalVariableComponentLiteral createLiteralComponent() {
        Element e = new Element("literal_component", getElement().getNamespace());
        OvalVariableComponentLiteral ret = getParentDocument().getLiteralVariableComponentWrapper();
        ret.setElement(e);
        ret.setRoot(getRoot());
        return ret;
    }

    /**
     * Create a possible restriction suitable for adding to this variable.
     * 
     * @return VariablePossibleRestriction
     */
    public VariablePossibleRestriction createPossibleRestriction() {
        Element e = new Element("possible_restriction", getElement().getNamespace());
        VariablePossibleRestriction ret = new VariablePossibleRestriction(getParentDocument());
        ret.setElement(e);
        ret.setRoot(root);
        return ret;
    }

    /**
     * Create a possible value suitable for adding to this variable.
     * 
     * @return VariablePossibleValue
     */
    public VariablePossibleValue createPossibleValue() {
        Element e = new Element("possible_value", getElement().getNamespace());
        VariablePossibleValue ret = new VariablePossibleValue(getParentDocument());
        ret.setElement(e);
        ret.setRoot(root);
        return ret;
    }

    /**
     * Create a variable restriction suitable for adding to this variable.
     * 
     * @return VariableRestriction
     */
    public VariableRestriction createVariableRestriction() {
        Element e = new Element("restriction", getElement().getNamespace());
        VariableRestriction ret = new VariableRestriction(getParentDocument());
        ret.setElement(e);
        ret.setRoot(root);
        return ret;
    }

    /**
     * Create constant value suitable for adding to this variable.
     * 
     * @return ConstantVariableValue
     */
   public ConstantVariableValue createConstantVariableValue() {
        Element e = new Element("value", getElement().getNamespace());
        ConstantVariableValue ret = new ConstantVariableValue(getParentDocument());
        ret.setElement(e);
        ret.setRoot(root);
        return ret;
    }

   /**
    * Create an oval function suitable for adding to this variable as a child.
    * 
    * @param functionType
    * 
    * @return OvalFunction
    */
    public OvalFunction createOvalFunction(OvalFunctionEnum functionType) {
        OvalFunction result = null;
        switch(functionType) {
            case arithmetic:
                result = new OvalFunctionArithmetic(getParentDocument());
                break;
            case begin:
                result = new OvalFunctionBegin(getParentDocument());
                break;
            case concat:
                result = new OvalFunctionConcat(getParentDocument());
                break;
            case end:
                result = new OvalFunctionEnd(getParentDocument());
                break;
            case escape_regex:
                result = new OvalFunctionEscapeRegex(getParentDocument());
                break;
            case regex_capture:
                result = new OvalFunctionRegexCapture(getParentDocument());
                break;
            case split:
                result = new OvalFunctionSplit(getParentDocument());
                break;
            case substring:
                result = new OvalFunctionSubstring(getParentDocument());
                break;
            case time_difference:
                result = new OvalFunctionTimeDifference(getParentDocument());
                break;
            case unique:
                result = new OvalFunctionUnique(getParentDocument());
                break;
            case count:
                result = new OvalFunctionCount(getParentDocument());
                break;
            default:
                throw new IllegalArgumentException("Function type not yet supported:" + functionType);
        }
        Element e = new Element(functionType.name(), getElement().getNamespace());
        result.setElement(e);
        result.setRoot(getRoot());
        return result;
    }
    
 
    /**
     * Add a class implementing OvalVariableChild to this one as a child.
     * 
     * @param child
     */
    public void addChild(OvalVariableChild child) {
        this.element.addContent(child.getElement());
    }

    /**
     * Get the child elements of this oval variable in the form of a tree.
     * 
     * @return DefaultMutableTreeNode
     */
	public DefaultMutableTreeNode getChildren()
	{
		DefaultMutableTreeNode ret = null;

		ret = new DefaultMutableTreeNode();

		OvalVariable ov = getParentDocument().getVariableWrapper(element.getName());
		ov.setElement(getElement());
		ov.setRoot(getRoot());

		ret.setUserObject(ov);

		buildVarParmTree(ret, getElement());

		return ret;
	}

	private void buildVarParmTree(DefaultMutableTreeNode currNode, Element e)
	{
		List childElements = e.getChildren();

		int size = childElements.size();

		if(size > 0)
		{
			for(int x = 0; x < childElements.size();x++)
			{
				Element childElement = (Element) childElements.get(x);

				String elementName = childElement.getName();

				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
				currNode.add(childNode);
				childNode.setParent(currNode);

				if(elementName.toLowerCase().indexOf("object_component") > -1)
				{
					OvalVariableComponentObject oc = getParentDocument().getObjectVariableComponentWrapper();
					oc.setElement(childElement);
					oc.setRoot(getRoot());
					childNode.setUserObject(oc);
				}
				else if(elementName.toLowerCase().indexOf("literal_component") > -1)
				{
					OvalVariableComponentLiteral lc = getParentDocument().getLiteralVariableComponentWrapper();
					lc.setElement(childElement);
					lc.setRoot(getRoot());
					childNode.setUserObject(lc);
				}
				else if(elementName.toLowerCase().indexOf("variable_component") > -1)
				{
					OvalVariableComponentVariable vc = getParentDocument().getVariableComponentWrapper();
					vc.setElement(childElement);
					vc.setRoot(getRoot());
					childNode.setUserObject(vc);
				}
                else if(elementName.toLowerCase().indexOf("possible_restriction") > -1)
				{
					VariablePossibleRestriction obj = new VariablePossibleRestriction(getParentDocument());
					obj.setElement(childElement);
					obj.setRoot(getRoot());
					childNode.setUserObject(obj);
                    buildVarParmTree(childNode, childElement);
				}
                else if (elementName.toLowerCase().indexOf("restriction") > -1) {
                    VariableRestriction obj = new VariableRestriction(getParentDocument());
                    obj.setElement(childElement);
                    obj.setRoot(getRoot());
                    childNode.setUserObject(obj);
                }
                else if(elementName.toLowerCase().indexOf("possible_value") > -1)
				{
					VariablePossibleValue obj = new VariablePossibleValue(getParentDocument());
					obj.setElement(childElement);
					obj.setRoot(getRoot());
					childNode.setUserObject(obj);
				}
                else if(elementName.toLowerCase().indexOf("value") > -1)
				{
					ConstantVariableValue obj = new ConstantVariableValue(getParentDocument());
					obj.setElement(childElement);
					obj.setRoot(getRoot());
					childNode.setUserObject(obj);
                    buildVarParmTree(childNode, childElement);
				}
				else if(childElement.getChildren().size() > 0)
				{
//					OvalVariableFunctionGroup fg = getParentDocument().getVariableFunctionGroupWrapper();
                    String functionType = childElement.getName();
                    OvalFunction fg = createOvalFunction(OvalFunctionEnum.valueOf(functionType));
					fg.setElement(childElement);
					fg.setRoot(getRoot());

					childNode.setUserObject(fg);

					buildVarParmTree(childNode, childElement);
				}
				else
				{
					childNode.setUserObject(elementName);
				}
			}
		}
		else
		{

		}
	}

	/**
	 * Get a list of oval variables referenced by this variable.
	 * 
	 * @return List<OvalVariable>
	 */
	public List<OvalVariable> getReferencedVariables()
	{
		Set<String> varIdsReferenced = new HashSet<String>();
		List<OvalVariable> variables = new ArrayList<OvalVariable>();

		refVariableRecurser(getElement(), varIdsReferenced);

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
					//	log.debug("OvalVar: getReferencedVariables: variableId is NULL");
					}
				}
			}
		}
		else
		{
	//		log.debug("OvalObj: getReferencedVariables: variablesElement is NULL");
		}

		return variables;
	}

	/**
	 * Get a list of oval objects referenced by this oval variable.
	 * 
	 * @return List<OvalObject>
	 */
	public List<OvalObject> getReferencedObjects()
	{
		Set<String> objIdsReferenced = new HashSet<String>();
		List<OvalObject> objects = new ArrayList<OvalObject>();

		refObjectRecurser(getElement(), objIdsReferenced);
		OvalDefinitionsDocument ovalDoc = getParentDocument();
		for (String id : objIdsReferenced) {
			OvalObject obj = ovalDoc.getOvalObject(id);
			objects.add(obj);
		}

		return objects;
	}

	private void refObjectRecurser(Element element, Set<String> referencedIds)
	{
		String id = null;

		if(element.getName().toLowerCase().endsWith("object_component"))
		{
			id = element.getAttributeValue("object_ref");
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

				refObjectRecurser(child, referencedIds);
			}
		}
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
        String comment = getComment();

        ret.append(getElementName());
        ret.append("(" + getId() + ")");

        if(comment != null && comment.length() > 0)
        {
            ret.append(" " + comment);
        }

        return ret.toString();
    }

    /**
     * Set the comment.
     * 
     * @param comment
     */
	public void setComment(String comment)
	{
		if (comment != null)
		{
			getElement().setAttribute("comment", comment);
		}
	}

	/**
	 * Set the datatype.
	 * 
	 * @param datatype
	 */
	public void setDatatype(String datatype)
	{
		if (datatype != null)
		{
			getElement().setAttribute("datatype", datatype);
		}
	}

    private void matchFinder(DefaultMutableTreeNode node, MatchTracker tracker, String findString)
    {
        int cc = node.getChildCount();

        Object userObj = node.getUserObject();

        if(userObj != null)
        {
            if(userObj instanceof OvalVariableComponentLiteral)
            {
                OvalVariableComponentLiteral comp = (OvalVariableComponentLiteral) userObj;
                String value = comp.getValue();

                if(value != null)
                {
                    if(value.toLowerCase().indexOf(findString) > -1)
                    {
                        tracker.found = true;
                    }
                }
            }
        }

        if(tracker.found)
        {
            return;
        }

        if(cc > 0)
        {
            Enumeration children = node.children();

            while(children.hasMoreElements())
            {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();

                matchFinder(child, tracker, findString);

                if(tracker.found)
                {
                    break;
                }
            }
        }
    }

    /**
     * Check to see if any text fields in this oval variable match the given search string.
	 *
     * @param findString
     * @return boolean.
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
        MatchTracker mt = new MatchTracker();

        matchFinder(getChildren(), mt, lcFindString);

        return mt.found;
    }

    /**
     * See if this oval variable looks like a template variable that was passed in.
     * 
     * @param template
     * @return boolean
     */
    public boolean matches(OvalVariable template)
    {
    	boolean ret = false;
    	
    	if(template == null)
    	{
    		return ret;
    	}
    	
    	if(!getElementName().matches(template.getElementName()))
    	{
    		return ret;
    	}
    	
    	DefaultMutableTreeNode tparms = template.getChildren();
    	
    	if(tparms == null || tparms.getChildCount() == 0)
    	{
    		return ret;
    	}
    	
    	DefaultMutableTreeNode parms = getChildren();
    	
    	if(parms == null || parms.getChildCount() == 0)
    	{
    		return ret;
    	}
    	
    	Enumeration tEnum = tparms.breadthFirstEnumeration();
    	Enumeration pEnum = parms.breadthFirstEnumeration();
    	
    	boolean equal = true;
    	
    	while(tEnum.hasMoreElements() && pEnum.hasMoreElements())
    	{
    		DefaultMutableTreeNode tNode = (DefaultMutableTreeNode) tEnum.nextElement();
    		DefaultMutableTreeNode pNode = (DefaultMutableTreeNode) pEnum.nextElement();
    		
    		Object tUo = tNode.getUserObject();
    		Object pUo = pNode.getUserObject();
    		
    		if(!tNode.isRoot())
    		{
    			if(!tUo.equals(pUo))
    			{
    				equal = false; 
    				break;
    			}
    		}
    	}
    	
    	if(equal)
    	{
    		ret = true;
    	}
    	
    	return ret;
    }

    /**
     * Get the type of id this variable has.
     * 
     * @return String
     */
    @Override
    public String getOvalIdType() {
        return "var";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof OvalVariable) {
            OvalVariable otherVar = (OvalVariable) other;
            if (this.getId() != null && otherVar.getId() != null) {
                return this.getId().equals(otherVar.getId());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (getId() == null ? 0 : getId().hashCode());
    }
    
    /**
     * Return a list of ids of entities this variable references but
     * do not exist in the document.
     * 
     * @return Set<String>
     */
    public Set<String> getBrokenRefs()
    {
    	Set<String> brokenRefs = null;
    	
    	DefaultMutableTreeNode childrenRoot = getChildren();
    	
    	if(childrenRoot != null)
    	{
    		brokenRefs = new HashSet<String>();
    		
    		brokenRefRecurser(childrenRoot, brokenRefs);
    	}
    	
    	return brokenRefs;
    }
    
    private void brokenRefRecurser(DefaultMutableTreeNode node, Set<String> brokenRefs)
    {
    	Object obj = node.getUserObject();
    	
    	OvalDefinitionsDocument odd = getParentDocument();
    	
    	if(obj != null)
    	{
    		if(obj instanceof OvalVariableComponentVariable)
    		{
    			OvalVariableComponentVariable varComp = (OvalVariableComponentVariable) obj;
    			String referencedVarId = varComp.getVariableId();
    			
    			if(referencedVarId == null)
    			{
    				// this variable component was added without the id of the variable it points to.
    				// this is not a valid condition either.
    				log.error("Variable " + getId() + " has a variable component with a null ID! ");
    				brokenRefs.add(getId());
    			}
    			else
    			{
    				if(!odd.containsVariable(referencedVarId))
    				{
    					log.error("Variable " + getId() + " references variable with id " + referencedVarId
    							+ " but " + referencedVarId + " does not exist in the document.");
    					
    					brokenRefs.add(referencedVarId);
    				}
    			}
    		}
    		else if(obj instanceof OvalVariableComponentObject)
    		{
    			OvalVariableComponentObject objComp = (OvalVariableComponentObject)obj;
    			
    			String referencedObjId = objComp.getObjectId();
    			
    			if(referencedObjId == null)
    			{
    				// this object component was added without the id of the object
    				// it's supposed to point to.  This is not a valid condition
    				log.error("Variable " + getId() + " has an object component with a null ID! ");
    				brokenRefs.add(getId());
    			}
    			else
    			{
    				if(!odd.containsObject(referencedObjId))
    				{
    					log.error("Variable " + getId() + " references object with id " + referencedObjId
    							+ " but " + referencedObjId + " does not exist in the document.");
    					brokenRefs.add(referencedObjId);
    				}
    			}
    		}	
    	}
    	
		int childCount = node.getChildCount();

		if (childCount > 0)
		{
			for (int x = 0; x < childCount; x++)
			{
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node
						.getChildAt(x);

				brokenRefRecurser(child, brokenRefs);
			}
		}
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
		if (!(other instanceof OvalVariable))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalVariable other2 = (OvalVariable) other;
		
		// If the other object's comment attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getComment(), this.getComment())))
    	{
    		return false;
    	}
    	
		// If the other object's datatype attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getDatatype(), this.getDatatype())))
    	{
    		return false;
    	}
    	
		// Compare all of the child elements against each other.
		List<OvalVariableChild> myChildren = getVarChildren();
		List<OvalVariableChild> otherChildren = other2.getVarChildren();
		if (myChildren.size() != otherChildren.size())
		{
			return false;
		}
		for (int i = 0; i < myChildren.size(); i++)
		{
			OvalVariableChild myChild = myChildren.get(i);
			OvalVariableChild otherChild = otherChildren.get(i);
			if (!(myChild.isDuplicateOf(otherChild)))
			{
				return false;
			}
		}
		
		// Return true if we get to this point.
		return true;
	}
}
