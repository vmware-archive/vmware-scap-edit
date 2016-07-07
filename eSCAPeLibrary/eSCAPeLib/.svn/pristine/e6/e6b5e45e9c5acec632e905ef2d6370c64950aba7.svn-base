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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Element;
import org.jdom.Namespace;

import com.g2inc.scap.library.schema.NameDoc;

/**
 * Represents any of the *_object elements in an oval definitions document.
 */
public abstract class OvalObject extends OvalObjectChild implements VersionedOvalElement
{

    private static Logger log = Logger.getLogger(OvalObject.class);

    public OvalObject(OvalDefinitionsDocument parentDocument)
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
     * Objects can reference variables.  This method returns all variables directly referenced
     * by this object.
     *
     * @return List<OvalVariable>
     */
    public List<OvalVariable> getReferencedVariables()
    {
        Set<String> varIdsReferenced = new HashSet<String>();
        List<OvalVariable> variables = new ArrayList<OvalVariable>();

        refVariableRecurser(getElement(), varIdsReferenced);

        Element variablesElement = getRoot().getChild("variables", getRoot().getNamespace());

        if (variablesElement != null)
        {
            List children = variablesElement.getChildren();

            if (children != null && children.size() > 0)
            {
                for (int index = 0; index < children.size(); index++)
                {
                    Element child = (Element) children.get(index);

                    String variableId = child.getAttributeValue("id");

                    if (variableId != null)
                    {
                        if (varIdsReferenced.contains(variableId))
                        {
                            OvalVariable ov = getParentDocument().getVariableWrapper(child.getName());
                            ov.setElement(child);
                            ov.setRoot(getRoot());

                            variables.add(ov);
                        }
                    }
                    else
                    {
                        log.debug("OvalObj: getReferencedVariables: variableId is NULL:  child element name is " + child.getName());
                    }
                }
            }
        }
        else
        {
            log.debug("OvalObj: getReferencedVariables: variablesElement is NULL");
        }

        return variables;
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
     * @param comment
     */
    public void setComment(String comment)
    {
        if (comment != null)
        {
            getElement().setAttribute("comment", comment);
        }
        else
        {
            getElement().removeAttribute("comment");
        }
    }

    /**
     * Return a tree representing all of the child elements in this object.
     * 
     * @return DefaultMutableTreeNode
     */
    public DefaultMutableTreeNode getChildTreeNodes()
    {
        DefaultMutableTreeNode ret = null;

        ret = new DefaultMutableTreeNode();

        OvalObject oo = getParentDocument().getObjectWrapper();
        oo.setElement(getElement());
        oo.setRoot(getRoot());

        ret.setUserObject(oo);

        String objType = getElementName();
        String platform = getPlatform();

        List<OvalEntity> validParms = getParentDocument().getValidObjectEntityTypes(platform, objType);

        buildObjectParmTree(ret, getElement(), validParms);

        return ret;
    }

    private void buildObjectParmTree(DefaultMutableTreeNode currNode, Element e, List<OvalEntity> validParms)
    {
        List childElements = e.getChildren();

        int size = childElements.size();

        if (size > 0)
        {
            for (int x = 0; x < childElements.size(); x++)
            {
                Element childElement = (Element) childElements.get(x);

                String elementName = childElement.getName();

                if (elementName.toLowerCase().equals("behaviors"))
                {
                    continue;
                }

                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
                currNode.add(childNode);
                childNode.setParent(currNode);

                if (elementName.toLowerCase().equals("object_reference"))
                {
                    OvalObjectReference oor = getParentDocument().getObjectReferenceWrapper();
                    oor.setElement(childElement);
                    oor.setRoot(getRoot());
                    childNode.setUserObject(oor);
                }
                else if (elementName.toLowerCase().equals("set"))
                {
                    OvalObjectSet oos = getParentDocument().getObjectSetWrapper();
                    oos.setElement(childElement);
                    oos.setRoot(getRoot());
                    childNode.setUserObject(oos);

                    buildObjectParmTree(childNode, childElement, validParms);
                }
                else if (elementName.toLowerCase().equals("filter"))
                {
                    OvalObjectFilter oof = getParentDocument().getObjectFilterWrapper();
                    oof.setElement(childElement);
                    oof.setRoot(getRoot());
                    childNode.setUserObject(oof);
                }
                else if (childElement.getChildren().size() > 0)
                {
                    childNode.setUserObject(elementName);

                    buildObjectParmTree(childNode, childElement, validParms);
                }
                else
                {
                    OvalObjectParameter oop = getParentDocument().getObjectParameterWrapper();
                    oop.setElement(childElement);
                    oop.setRoot(getRoot());
                    int index = validParms.indexOf(new NameDoc(childElement.getName(), null));

                    if (index > -1)
                    {
                        oop.setEntity(validParms.get(index));
                    }
                    childNode.setUserObject(oop);
                }
            }
        }
        else
        {
        }
    }

    /**
     * Objects can reference other objects.  This method returns a list of
     *  all objects directly referenced by this one.
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

    /**
     * Objects can reference states by using them as filters.  This method returns
     * all oval states directly referenced by this object.
     *
     * @return List<OvalState>
     */
    public List<OvalState> getReferencedStates()
    {
        Set<String> stateIdsReferenced = new HashSet<String>();
        List<OvalState> states = new ArrayList<OvalState>();

        refStatesRecurser(getElement(), stateIdsReferenced);
		OvalDefinitionsDocument ovalDoc = getParentDocument();
		for (String stateId : stateIdsReferenced) {
			OvalState state = ovalDoc.getOvalState(stateId);
			states.add(state);
		}
		
        return states;
    }

    private void refObjectRecurser(Element element, Set<String> referencedIds)
    {
        String id = null;

        if (element.getName().toLowerCase().endsWith("object_reference"))
        {
            id = element.getValue();
        }

        if (id != null && id.length() > 0)
        {
            referencedIds.add(id);
        }

        List children = element.getChildren();

        if (children != null && children.size() > 0)
        {
            for (int x = 0; x < children.size(); x++)
            {
                Element child = (Element) children.get(x);

                refObjectRecurser(child, referencedIds);
            }
        }
    }

    private void refVariableRecurser(Element element, Set<String> referencedIds)
    {
        String id = null;

        if (element.getName().toLowerCase().endsWith("var_ref"))
        {
            id = element.getValue();
        }
        else
        {
            id = element.getAttributeValue("var_ref");
        }

        if (id != null && id.length() > 0)
        {
            referencedIds.add(id);
        }

        List children = element.getChildren();

        if (children != null && children.size() > 0)
        {
            for (int x = 0; x < children.size(); x++)
            {
                Element child = (Element) children.get(x);

                refVariableRecurser(child, referencedIds);
            }
        }
    }

    private void refStatesRecurser(Element element, Set<String> referencedIds)
    {
        String id = null;

        if (element.getName().toLowerCase().endsWith("filter"))
        {
            id = element.getValue();
        }


        if (id != null && id.length() > 0)
        {
            referencedIds.add(id);
        }

        List children = element.getChildren();

        if (children != null && children.size() > 0)
        {
            for (int x = 0; x < children.size(); x++)
            {
                Element child = (Element) children.get(x);

                refStatesRecurser(child, referencedIds);
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
     * Returns a list of OvalEntity objects that represent valid parameters to this object.
     * 
     * @return List<OvalEntity>
     */
    public List<OvalEntity> getValidParameters()
    {
        OvalDefinitionsDocument defDoc = getParentDocument();
        Namespace objectNS = this.element.getNamespace();
        String objectNSUri = objectNS.getURI();
        if (objectNSUri == null || !objectNSUri.startsWith(OVAL_XML_PLATFORM_NS_PREFIX))
        {
            throw new IllegalStateException("Unexpected xmlns on Oval Object:" + objectNSUri);
        }
        String platform = objectNSUri.substring(OVAL_XML_PLATFORM_NS_PREFIX.length());
        String objectType = this.element.getName();
        return defDoc.getValidObjectEntityTypes(platform, objectType);
    }

    /**
     * Creates and returns an oval set that can be added to this object as a child.
     * 
     * @return OvalObjectSet
     */
    public OvalObjectSet createSet()
    {
        Element e = new Element("set", getRoot().getNamespace());

        e.setAttribute("set_operator", OvalObjectSetOperatorEnum.UNION.toString());

        OvalObjectSet ret = getParentDocument().getObjectSetWrapper();
        ret.setElement(e);
        ret.setRoot(getRoot());
        return ret;
    }

    /**
     *  Creates and returns an oval object reference that can be added to this object
     *  as a child.
     *  
     * @return OvalObjectReference
     */
    public OvalObjectReference createObjectReference()
    {
        Element e = new Element("object_reference", getRoot().getNamespace());

        e.setText(getId());

        OvalObjectReference ret = getParentDocument().getObjectReferenceWrapper();
        ret.setElement(e);
        ret.setRoot(getRoot());

        return ret;
    }

    /**
     * Creates and returns an oval object filter that can be added to this object
     * as a child.
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
     * Creates and returns an oval object parameter that can be add to this
     * object as a child.
     * 
     * @param parmName
     * @return OvalObjectParameter
     */
    public OvalObjectParameter createObjectParameter(String parmName)
    {
        Element e = new Element(parmName, getElement().getNamespace());

        String objType = getElementName();
        String platform = getPlatform();

        List<OvalEntity> validParms = getParentDocument().getValidObjectEntityTypes(platform, objType);

        OvalObjectParameter ret = getParentDocument().getObjectParameterWrapper();
        ret.setElement(e);
        ret.setRoot(getRoot());

        int index = validParms.indexOf(new NameDoc(e.getName(), null));

        if (index > -1)
        {
            OvalEntity oe = validParms.get(index);
            ret.setEntity(oe);
        }

        return ret;
    }

    /**
     * This method is called by setParameters(DefaultMutableTreeNode) to reconstruct the
     * JDom Element tree based on the structure of the DefaultMutableTreeNode tree.
     *
     * @param parent  parent element, to which DefaultMutableTreeNode node's children will be added
     * @param node    node containing children, whose Elements will be added to the Element tree
     */
private void addElementsFromTreeNode(Element parent, DefaultMutableTreeNode node)
    {
        if(parent == null)
        {
            parent = this.getElement();
            int childCount = node.getChildCount();

            if (childCount > 0)
            {
                List<DefaultMutableTreeNode> ooChildren = new ArrayList<DefaultMutableTreeNode>(childCount);

                for (int x = 0; x < childCount; x++)
                {
                    DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(x);
                    ooChildren.add(childNode);
                }

                // sort the children
                OvalObjectChildComparator oocc = new OvalObjectChildComparator(this);
                Collections.sort(ooChildren, oocc);

                for(DefaultMutableTreeNode childNode : ooChildren)
                {
                    Object userObj = childNode.getUserObject();

                    if (userObj instanceof OvalObjectChild)
                    {
                        addElementsFromTreeNode(parent, childNode);
                    }
                }
            }
        }
        else
        {
            Object nodeUserObj = node.getUserObject();
            if(nodeUserObj instanceof OvalObjectChild)
            {
                OvalObjectChild nodeChildObj = (OvalObjectChild) nodeUserObj;
                parent.addContent(nodeChildObj.getElement().detach());

                int childCount = node.getChildCount();

                if (childCount > 0)
                {
                    List<DefaultMutableTreeNode> ooChildren = new ArrayList<DefaultMutableTreeNode>(childCount);

                    for (int x = 0; x < childCount; x++)
                    {
                        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(x);
                        ooChildren.add(childNode);
                    }

                    // sort the children
                    OvalObjectChildComparator oocc = new OvalObjectChildComparator(this);
                    Collections.sort(ooChildren, oocc);

                    for(DefaultMutableTreeNode childNode : ooChildren)
                    {
                        Object userObj = childNode.getUserObject();

                        if (userObj instanceof OvalObjectChild)
                        {
                            addElementsFromTreeNode(nodeChildObj.getElement(), childNode);
                        }
                    }
                }
            }
        }
    }

    private void removeNonBehaviorChildren()
    {
        List<Content> contentToRemove = new ArrayList<Content>();

        List children = getElement().getChildren();

        if(children != null && children.size() > 0)
        {
            for(int x = 0 ; x < children.size(); x++)
            {
                Object child = children.get(x);

                if(child instanceof Element)
                {
                    Element childE = (Element) child;

                    if(childE.getName().equals("behaviors"))
                    {
                        continue;
                    }

                    // mark this element for removal
                    contentToRemove.add(childE);
                }
            }
        }

        if(contentToRemove.size() > 0)
        {
            for(Content c : contentToRemove)
            {
                c.detach();
            }
        }

    }

    /**
     * This method sets this objects parameters from the parameters passed in
     * as a tree.
     * 
     * @param rootNode
     */
    public void setParameters(DefaultMutableTreeNode rootNode)
    {
        // remove any existing children
        removeNonBehaviorChildren();

        if (rootNode == null || rootNode.getChildCount() == 0)
        {
            return;
        }

        addElementsFromTreeNode(null, rootNode);
    }

    /**
     * Overridden version of the clone() method.  This implements a deep copy of this 
     * object.
     */
    @Override
    public OvalObject clone()
    {
        OvalObject ret = null;

        ret = getParentDocument().createObject(getPlatform(), getElementName());

        if (ret == null)
        {
            log.error("clone(): Object created by parent was null!");
            return null;
        }

        String newId = ret.getId();

        ret.setElement((Element) getElement().clone());

        ret.setAttribute("id", newId);

        return ret;
    }

    private void matchFinder(OvalObjectChild node, MatchTracker tracker, String findString)
    {
        List<OvalObjectChild> children = node.getChildren();
        if (node instanceof OvalObjectParameter)
        {
            OvalObjectParameter oop = (OvalObjectParameter) node;
            String value = oop.getValue();

            if (value != null)
            {
                if (value.toLowerCase().indexOf(findString) > -1)
                {
                    tracker.found = true;
                }
            }
        }

        for (OvalObjectChild child : children)
        {
            matchFinder(child, tracker, findString);
            if (tracker.found)
            {
                break;
            }
        }

    }

    private void matchFinder(DefaultMutableTreeNode node, MatchTracker tracker, String findString)
    {
        int cc = node.getChildCount();

        Object userObj = node.getUserObject();

        if (userObj != null)
        {
            if (userObj instanceof OvalObjectParameter)
            {
                OvalObjectParameter oop = (OvalObjectParameter) userObj;
                String value = oop.getValue();

                if (value != null)
                {
                    if (value.toLowerCase().indexOf(findString) > -1)
                    {
                        tracker.found = true;
                    }
                }
            }
        }

        if (tracker.found)
        {
            return;
        }

        if (cc > 0)
        {
            Enumeration children = node.children();

            while (children.hasMoreElements())
            {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();

                matchFinder(child, tracker, findString);

                if (tracker.found)
                {
                    break;
                }
            }
        }
    }

    /**
     * Overridden implementation of a matches method that checks all text fields
     * in this object for the supplied string and returns true if it finds it.
     * 
     * @return boolean
     */
    @Override
    public boolean matches(String findString)
    {
        if (findString == null || findString.length() == 0)
        {
            return true;
        }

        String lcFindString = findString.toLowerCase();

        String id = getId();
        if (id != null)
        {
            if (id.toLowerCase().indexOf(lcFindString) > -1)
            {
                return true;
            }
        }

        String comment = getComment();
        if (comment != null)
        {
            if (comment.toLowerCase().indexOf(lcFindString) > -1)
            {
                // matches
                return true;
            }
        }

        // check parameters
        MatchTracker mt = new MatchTracker();

        List<OvalObjectChild> children = getChildren();
        for (OvalObjectChild child : children)
        {
            matchFinder(child, mt, lcFindString);
        }
//        matchFinder(getChildTreeNodes(), mt, lcFindString);

        return mt.found;
    }

    /**
     * See if this oval object looks like a template object that was passed in.
     * 
     * @param template
     * @return boolean
     */
    public boolean matches(OvalObject template)
    {
        boolean ret = false;

        if (template == null)
        {
            return ret;
        }

        if (!getElementName().equals(template.getElementName()))
        {
            return ret;
        }

        List<OvalObjectChild> templateChildren = template.getChildren();
        List<OvalObjectChild> myChildren = getChildren();
        return matches(templateChildren, myChildren);

//    	DefaultMutableTreeNode tparms = template.getChildTreeNodes();
//    	
//    	if(tparms == null || tparms.getChildCount() == 0)
//    	{
//    		DefaultMutableTreeNode myParms = getChildTreeNodes();
//    		
//    		if(myParms == null || myParms.getChildCount() == 0)
//    		{
//    			ret = true;
//    		}
//    		return ret;
//    	}
//    	
//    	DefaultMutableTreeNode parms = getChildTreeNodes();
//    	
//    	if(parms == null || parms.getChildCount() == 0)
//    	{
//    		return ret;
//    	}
//    	
//    	Enumeration tEnum = tparms.breadthFirstEnumeration();
//    	Enumeration pEnum = parms.breadthFirstEnumeration();
//    	
//    	boolean equal = true;
//    	
//    	while(tEnum.hasMoreElements() && pEnum.hasMoreElements())
//    	{
//    		DefaultMutableTreeNode tNode = (DefaultMutableTreeNode) tEnum.nextElement();
//    		DefaultMutableTreeNode pNode = (DefaultMutableTreeNode) pEnum.nextElement();
//    		
//    		Object tUo = tNode.getUserObject();
//    		Object pUo = pNode.getUserObject();
//    		
//    		if(!tNode.isRoot())
//    		{
//    			if(!tUo.equals(pUo))
//    			{
//    				equal = false; 
//    				break;
//    			}
//    		}
//    	}
//    	
//    	if(equal)
//    	{
//    		ret = true;
//    	}
//    	
//    	return ret;
    }

    private boolean matches(List<OvalObjectChild> list1, List<OvalObjectChild> list2)
    {
        boolean equal = true;
        if (list1.size() != list2.size())
        {
            equal = false;
        }
        else
        {
            for (int i = 0; i < list1.size(); i++)
            {
                OvalObjectChild c1Child = list1.get(i);
                OvalObjectChild c2Child = list2.get(i);
                if (!c1Child.equals(c2Child))
                {
                    equal = false;
                    break;
                }
                else
                {
                    equal = matches(c1Child.getChildren(), c2Child.getChildren());
                }
            }
        }
        return equal;
    }

    /**
     * Gets the behaviors for this object.
     * 
     * @return Map<String,String>
     */
    public Map<String, String> getBehaviors()
    {
        HashMap<String, String> behaviorMap = new HashMap<String, String>();
        Element behaviorsElement = getElement().getChild("behaviors", getElement().getNamespace());

        if (behaviorsElement == null)
        {
            return behaviorMap;
        }

        List<Attribute> behaviorAtts = behaviorsElement.getAttributes();
        for (int i = 0; i < behaviorAtts.size(); i++)
        {
            Attribute att = behaviorAtts.get(i);
            behaviorMap.put(att.getName(), att.getValue());
        }
        return behaviorMap;
    }

    /**
     * Sets the behaviors for this object.
     * 
     * @param behaviorMap
     */
    public void setBehaviors(Map<String, String> behaviorMap)
    {
        Element behaviorsElement = getElement().getChild("behaviors", getElement().getNamespace());
        if (behaviorsElement == null)
        {
            if (behaviorMap == null || behaviorMap.size() == 0)
            {
                log.debug("setBehaviors(): Empty map passed in and no existing behaviors element");
                return;
            }
            behaviorsElement = new Element("behaviors", getElement().getNamespace());
            getElement().addContent(0, behaviorsElement);
        }
        else
        {
            if (behaviorMap == null || behaviorMap.size() == 0)
            {
                log.debug("setBehaviors(): removing existing behaviors element, empty map passed in");

                getElement().removeChild("behaviors", getElement().getNamespace());
                return;
            }
        }
        behaviorsElement.setAttributes(null);  // clear any existing attributes (behaviors)();
        Iterator<String> newAttKeys = behaviorMap.keySet().iterator();
        while (newAttKeys.hasNext())
        {
            String key = newAttKeys.next();
            String value = behaviorMap.get(key);
            behaviorsElement.setAttribute(key, value);
        }
    }

    @Override
    public String getOvalIdType()
    {
        return "obj";
    }

    /**
     * Return a list of ids of entities this object references but
     * do not exist in the document.
     * 
     * @return Set<String>
     */
    public Set<String> getBrokenRefs()
    {
        Set<String> brokenRefs = new HashSet<String>();

        OvalDefinitionsDocument odd = getParentDocument();

        //
        // Objects

        // find out what other objects this one references
        Set<String> objIdsReferenced = new HashSet<String>();
        refObjectRecurser(getElement(), objIdsReferenced);

        // loop through and make sure all those objects exist.
        if (objIdsReferenced != null && objIdsReferenced.size() > 0)
        {
            for (Iterator<String> i = objIdsReferenced.iterator(); i.hasNext();)
            {
                String objId = i.next();
                if (!odd.containsObject(objId))
                {
                    log.error("OvalObject " + getId() + " points to OvalObject " + objId + " which doesn't exist");
                    brokenRefs.add(objId);
                }
            }
        }

        //
        // States

        // find out what states this object references
        Set<String> stateIdsReferenced = new HashSet<String>();
        refStatesRecurser(getElement(), stateIdsReferenced);

        // loop through and make sure all states exist
        if (stateIdsReferenced != null && stateIdsReferenced.size() > 0)
        {
            for (Iterator<String> i = stateIdsReferenced.iterator(); i.hasNext();)
            {
                String steId = i.next();
                if (!odd.containsState(steId))
                {
                    log.error("OvalObject " + getId() + " points to OvalState " + steId + " which doesn't exist");
                    brokenRefs.add(steId);
                }
            }
        }

        //
        // Variables

        // find out what variables this object references
        Set<String> varIdsReferenced = new HashSet<String>();
        refVariableRecurser(getElement(), varIdsReferenced);

        // loop through and make sure all variables exist
        if (varIdsReferenced != null && varIdsReferenced.size() > 0)
        {
            for (Iterator<String> i = varIdsReferenced.iterator(); i.hasNext();)
            {
                String varId = i.next();
                if (!odd.containsVariable(varId))
                {
                    log.error("OvalObject " + getId() + " points to OvalVariable " + varId + " which doesn't exist");
                    brokenRefs.add(varId);
                }
            }
        }

        return brokenRefs;
    }

    /**
     * Get the version.
     *
     * @return OvalVersion
     */
    @Override
    public OvalVersion getVersion()
    {
        String verString = getElement().getAttributeValue("version");
        OvalVersion version = new OvalVersion();
        version.setVersion(verString);

        return version;
    }

    /**
     * Set the version.
     *
     * @param ver
     */
    @Override
    public void setVersion(OvalVersion ver)
    {
        if (ver != null)
        {
            getElement().setAttribute("version", ver.getVersionString());
        }
        else
        {
            getElement().removeAttribute("version");
        }
    }

    protected class MatchTracker
    {

        public boolean found = false;
    }

	/**
	 * Return the value of the deprecated attribute.
	 * 
	 * @return boolean
	 */
    public boolean getDeprecated()
	{
        boolean deprecated = false;
		String deprecatedString = getElement().getAttributeValue("deprecated");
        if(deprecatedString != null)
        {
            try
            {
            	deprecated = Boolean.parseBoolean(deprecatedString);
            }
            catch(Exception e)
            {
                log.error("Unable to parse value as boolean: " + deprecatedString, e);
            }
        }
        return deprecated;
	}

    /**
     * Sets the value of the deprecated attribute.
     * 
     * @param b
     */
    public void setDeprecated(boolean b)
    {
        getElement().removeAttribute("deprecated");
        getElement().setAttribute("deprecated", b + "");
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
		if (!(other instanceof OvalObject))
        {
			return false;
		}
		
		// Cast the other object to this class.
		OvalObject other2 = (OvalObject) other;
		
		// If the other object's comment attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getComment(), this.getComment())))
            {
    		return false;
            }
    	
		// If the other object's version attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getVersion().toString(), this.getVersion().toString())))
    	{
    		return false;
    	}
    	
		// If the other object's deprecated attribute does not match,
		// then return false.
    	if (other2.getDeprecated() != this.getDeprecated())
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
    	
    	// Compare behaviors.
    	Map<String, String> myBehaviors = this.getBehaviors();
    	Map<String, String> otherBehaviors = other2.getBehaviors();
    	if (myBehaviors != null && otherBehaviors != null)
    	{
    		if (!(myBehaviors.equals(otherBehaviors)))
    		{
    			return false;
                }
            }
    	else if (myBehaviors == null && otherBehaviors != null)
    	{
    		return false;
        }
    	else if (myBehaviors != null && otherBehaviors == null)
    	{
    		return false;
    	}
		
		// Return true if we get to this point.
		return true;
    }
}
