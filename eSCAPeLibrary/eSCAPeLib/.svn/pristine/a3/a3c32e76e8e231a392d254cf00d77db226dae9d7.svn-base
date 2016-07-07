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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Represents any of the *_test elements in an oval definitions document.
 */
public abstract class OvalTest extends VersionedOvalElementImpl
{

    private static Logger log = Logger.getLogger(OvalTest.class);

    public OvalTest(OvalDefinitionsDocument parentDocument)
    {
        super(parentDocument);
    }

    /**
     * Get the check attribute.
     *
     * @return String
     */
    public String getCheck()
    {
        // TODO: make this get a schema default value.

        String check = getElement().getAttributeValue("check");
        return check;
    }

    /**
     * Set the check attribute.
     *
     * @param check
     */
    public void setCheck(String check)
    {
        if (check == null)
        {
            element.removeAttribute("check");
        } else
        {
            element.setAttribute("check", check);
        }
    }

    /**
     * Set the check attribute.
     *
     * @param check
     */
    public void setCheck(CheckEnumeration check)
    {
        if (check == null)
        {
            element.removeAttribute("check");
        } else
        {
            element.setAttribute("check", check.toString());
        }
    }

    /**
     * Get check_existence attribute.
     * 
     * @return String
     */
    public String getCheckExistence()
    {
        // TODO: make this get the schema default value.

        String defaultVal = "at_least_one_exists";

        String check_existence = getElement().getAttributeValue("check_existence");

        if (check_existence == null || check_existence.length() == 0)
        {
            check_existence = defaultVal;
        }

        return check_existence;
    }

    /**
     * Set check_existence attribute.
     *
     * @param ceVal
     */
    public void setCheckExistence(String ceVal)
    {
        if (ceVal == null)
        {
            getElement().removeAttribute("check_existence");
        } else
        {
            getElement().setAttribute("check_existence", ceVal);
        }
    }

    /**
     * Set check_existence attribute.
     *
     * @param ceVal
     */
    public void setCheckExistence(ExistenceEnumeration ceVal)
    {
        if (ceVal == null)
        {
            getElement().removeAttribute("check_existence");
        } else
        {
            getElement().setAttribute("check_existence", ceVal.toString());
        }
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
        } else
        {
            getElement().removeAttribute("comment");
        }
    }

    /**
     * Get the id of the object this test refers to.
     * 
     * @return String
     */
    public String getObjectId()
    {
        String objectId = null;

        List children = getElement().getChildren();
        for (int x = 0; x < children.size(); x++)
        {
            Element child = (Element) children.get(x);

            if (child.getName().toLowerCase().indexOf("object") > -1)
            {
                objectId = child.getAttributeValue("object_ref");
                break;
            }
        }

        return objectId;
    }

    /**
     * Set the id of the object this test refers to.
     *
     * @param objectId
     */
    public void setObjectId(String objectId)
    {
        Element objectElement = null;
        List children = getElement().getChildren();
        for (int x = 0; x < children.size(); x++)
        {
            Element child = (Element) children.get(x);

            if (child.getName().toLowerCase().indexOf("object") > -1)
            {
                objectElement = child;
                break;
            }
        }

        if (objectElement == null)
        {
            objectElement = new Element("object", element.getNamespace());
            this.element.addContent(0, objectElement);
        }
        objectElement.setAttribute("object_ref", objectId);
    }

    /**
     * Get the id of the state this test refers to.
     * 
     * @return String
     */
    public String getStateId()
    {
        String stateId = null;

        List children = getElement().getChildren();
        for (int x = 0; x < children.size(); x++)
        {
            Element child = (Element) children.get(x);

            if (child.getName().toLowerCase().indexOf("state") > -1)
            {
                stateId = child.getAttributeValue("state_ref");
                break;
            }
        }

        return stateId;
    }

    /**
     * Set the id of the state this test refers to.
     *
     * @param stateId
     */
    public void setStateId(String stateId)
    {
        if(stateId == null)
        {
            List<OvalState> emptyStates = new ArrayList<OvalState>(0);
            setStates(emptyStates);
        }
        else
        {
            OvalState os = getParentDocument().getOvalState(stateId);

            if(os == null)
            {
                os = new UnresolvedOvalState(getParentDocument());
                os.setId(stateId);
            }

            List<OvalState> singleList = new ArrayList<OvalState>(1);

            singleList.add(os);
            setStates(singleList);
        }
    }

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
     * Get a reference to the object referred to by this test.
     * 
     * @return OvalObject
     */
    public OvalObject getObject()
    {
        Element objRefElement = getElement().getChild("object", getElement().getNamespace());

        if (objRefElement == null)
        {
            return null;
        }

        String objIdToFind = objRefElement.getAttributeValue("object_ref");

        if (objIdToFind == null)
        {
            return null;
        }

        OvalObject oo = getParentDocument().getOvalObject(objIdToFind);

        if(oo == null)
        {
            oo = new UnresolvedOvalObject(getParentDocument());
            oo.setId(objIdToFind);
        }

        return oo;
    }

    /**
     * Get a reference to the state referred to by this test.
     * 
     * @return OvalState
     */
    public OvalState getState()
    {
        Element stateRefElement = getElement().getChild("state", getElement().getNamespace());

        if (stateRefElement == null)
        {
            return null;
        }

        String stateIdToFind = stateRefElement.getAttributeValue("state_ref");

        if (stateIdToFind == null)
        {
            return null;
        }

        OvalState os = getParentDocument().getOvalState(stateIdToFind);

        if(os == null)
        {
            os = new UnresolvedOvalState(getParentDocument());
            os.setId(stateIdToFind);
        }
        
        return os;
    }

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
    public String toString()
    {
        StringBuilder ret = new StringBuilder();

        String comment = getComment();
        ret.append(getElementName());
        ret.append("(" + getId() + ")");

        if (comment != null && comment.length() > 0)
        {
            ret.append(" " + comment);
        }

        return ret.toString();
    }

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

        return false;
    }

    @Override
    public String getOvalIdType()
    {
        return "tst";
    }

    /**
     * Return a list of ids of entities this test references but
     * do not exist in the document.
     * 
     * @return Set<String>
     */
    public Set<String> getBrokenRefs()
    {
        Set<String> brokenRefs = new HashSet<String>();

        OvalDefinitionsDocument odd = getParentDocument();

        String objectId = getObjectId();

        if (objectId != null)
        {
            if (!odd.containsObject(objectId))
            {
                log.error("OvalTest " + getId() + " points to OvalObject " + objectId + " which doesn't exist");
                brokenRefs.add(objectId);
            }
        }


        List<OvalState> states = getStates();
        if (states != null)
        {
            for(int x = 0; x < states.size(); x++)
            {
                OvalState os = states.get(x);

                if(os instanceof UnresolvedOvalState)
                {
                    brokenRefs.add(os.getId());
                }
            }
        }

        return brokenRefs;
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
		if (!(other instanceof OvalTest))
        {
            return false;
        }
		
		// Cast the other object to this class.
		OvalTest other2 = (OvalTest) other;
		
		// If the other object's check attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getCheck(), this.getCheck())))
        {
            return false;
        }
		
		// If the other object's check_existence attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getCheckExistence(), this.getCheckExistence())))
        {
            return false;
        }

		// If the other object's comment attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getComment(), this.getComment())))
    	{
    		return false;
    	}
		
		// If the other object's object ID does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getObjectId(), this.getObjectId())))
    	{
    		return false;
    	}
    	
        // Compare states.
        List<OvalState> myStates = this.getStates();
        List<OvalState> otherStates = other2.getStates();
        if(!areListsEqualOrBothEmpty(myStates, otherStates))
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

		// Return true if we get to this point.
        return true;
    }

    private boolean areListsEqualOrBothEmpty(List<OvalState> list1, List<OvalState> list2)
    {
        boolean ret = true;

        if(list1.size() != list2.size())
        {
            ret = false;
            return ret;
        }

        // we know both lists are same size,
        // loop through them
        for(int x = 0; x < list1.size(); x++)
        {
            OvalState a = list1.get(x);
            OvalState b = list2.get(x);

            // that have to match in order as well
            // as size, the first one that doesn't match should
            // break us out of the loop
            if(!a.getId().equals(b.getId()))
            {
                ret = false;
                break;
            }
        }

        return ret;
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
     * Return a list of objects this test references.
     * 
     * @return List<OvalObject>
     */
    public List<OvalObject> getReferencedObjects()
    {
        List<OvalObject> referenced = new ArrayList<OvalObject>();

        List children = getElement().getChildren();
        for (int x = 0; x < children.size(); x++)
        {
            Element child = (Element) children.get(x);

            if (child.getName().toLowerCase().indexOf("object") > -1)
            {
                String objId = child.getAttributeValue("object_ref");

                OvalObject ovlObj = getParentDocument().getOvalObject(objId);

                if (ovlObj != null)
                {
                    referenced.add(ovlObj);
                }
            }
        }

        return referenced;
    }

    /**
     * Return a list of states this test references.
     * 
     * @return List<OvalState>
     */
    public List<OvalState> getReferencedStates()
    {
        List<OvalState> referenced = new ArrayList<OvalState>();

        List children = getElement().getChildren();
        for (int x = 0; x < children.size(); x++)
        {
            Element child = (Element) children.get(x);

            if (child.getName().toLowerCase().indexOf("state") > -1)
            {
                String steId = child.getAttributeValue("state_ref");

                OvalState ovlSte = getParentDocument().getOvalState(steId);

                if (ovlSte != null)
                {
                    referenced.add(ovlSte);
                }
            }
        }

        return referenced;
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
