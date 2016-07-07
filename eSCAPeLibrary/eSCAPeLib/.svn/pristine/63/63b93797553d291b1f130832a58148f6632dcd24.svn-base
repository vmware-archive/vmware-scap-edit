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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Represents an oval definition in an oval definitions document.
 * This class is abstract and meant to be extended for specific versions
 * of OVAL.
 * 
 */
public abstract class OvalDefinition extends VersionedOvalElementImpl
{
	private static Logger log = Logger.getLogger(OvalDefinition.class);
	public static final String OVAL_NAMESPACE = "http://oval.mitre.org/XMLSchema/oval-definitions-5";
	public final static HashMap<String, Integer> DEFINITION_ORDER = new HashMap<String, Integer>();
	static 
	{
		DEFINITION_ORDER.put("Signature", 0);
		DEFINITION_ORDER.put("metadata", 1);
		DEFINITION_ORDER.put("notes", 2);
		DEFINITION_ORDER.put("criteria", 3);
	}	

	public OvalDefinition(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}

	/**
	 * get the type(definition class) of definition this is.
	 * 
	 * @return DefinitionClassEnum
	 */
	public DefinitionClassEnum getDefinitionClass()
	{
		String dc = element.getAttributeValue("class");
		
		DefinitionClassEnum ret = DefinitionClassEnum.valueOf(dc.toUpperCase());
		return ret;
	}
	
	/**
	 * Get a Metadata object for this definition.
	 * 
	 * @return Metadata
	 */
	public Metadata getMetadata()
	{
		Metadata ret = null;
		
		Namespace ovaldefNS = getElement().getNamespace();
		
		Element mde = getElement().getChild("metadata", ovaldefNS);
		
		if(mde != null)
		{
			ret = getParentDocument().getMetadataWrapper();
			ret.setElement(mde);
			ret.setRoot(getRoot());
		}
		
		return ret;
	}

	/**
	 * Create a criteria object suitable for adding to this oval definition.
	 * 
	 * @return Criteria
	 */
	public Criteria createCriteria()
	{
		Element e = new Element(Criteria.ELEMENT_NAME, getElement().getNamespace());
		Criteria c = getParentDocument().getCriteriaWrapper();
		c.setElement(e);
		c.setRoot(getRoot());
		c.setSCAPDocument(getSCAPDocument());
		c.setOperator(OvalCriteriaOperatorEnum.AND);
		
		return c;
	}
	
	/**
	 * Create an Extend Definition object suitable for adding to any criteria in this oval definition.
	 * 
	 * @return ExtendDefinition
	 */
	public ExtendDefinition createExtendDefinition()
	{
		Element e = new Element(ExtendDefinition.ELEMENT_NAME, getElement().getNamespace());
		ExtendDefinition extDef = getParentDocument().getExtendDefinitionWrapper();
		extDef.setElement(e);
		extDef.setRoot(getRoot());
		extDef.setSCAPDocument(getSCAPDocument());
		
		return extDef;
	}
	
	
	/**
	 * Create a Criterion object suitable for adding to any criteria in this oval definition.
	 * 
	 * @return ExtendDefinition
	 */
	public Criterion createCriterion()
	{
		Element e = new Element(Criterion.ELEMENT_NAME, getElement().getNamespace());
		Criterion criterion = getParentDocument().getCriterionWrapper();
		criterion.setElement(e);
		criterion.setRoot(getRoot());
		criterion.setSCAPDocument(getSCAPDocument());
		
		return criterion;
	}	
	
	public Metadata createMetadata()
	{
		Element e = new Element(Metadata.ELEMENT_NAME, getElement().getNamespace());
		Metadata metdata = getParentDocument().getMetadataWrapper();
		metdata.setElement(e);
		metdata.setRoot(getRoot());
		metdata.setSCAPDocument(getSCAPDocument());
		
		return metdata;
	}
	
	public AffectedItemContainer createAffectedItemContainer()
	{
		Element e = new Element(AffectedItemContainer.ELEMENT_NAME, getElement().getNamespace());
		AffectedItemContainer affected = getParentDocument().getAffectedItemContainerWrapper();
		affected.setElement(e);
		affected.setRoot(getRoot());
		affected.setSCAPDocument(getSCAPDocument());
		
		return affected;
	}
	
	/**
	 * Return a list of tests that are referenced by this definition's
	 * criterion elements.
	 * 
	 * @return List<OvalTest>
	 */
	public List<OvalTest> getReferencedTests()
	{
		Set<String> testIdsReferenced = new HashSet<String>(); 
		List<OvalTest> tests = new ArrayList<OvalTest>();
		
		Element rootCriteria = getElement().getChild("criteria", getElement().getNamespace());
		
		refTestRecurser(rootCriteria, testIdsReferenced);
		OvalDefinitionsDocument ovalDoc = getParentDocument();
		for (String testId : testIdsReferenced) {
			OvalTest test = ovalDoc.getOvalTest(testId);
			tests.add(test);
		}
		return tests;
	}

	private void refDefRecurser(Element element, Set<String> referencedIds)
	{
		String id = element.getAttributeValue("definition_ref");
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
				
				refDefRecurser(child, referencedIds);
			}
		}
	}
	
	private void refTestRecurser(Element element, Set<String> referencedIds)
	{
		String id = element.getAttributeValue("test_ref");
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
				
				refTestRecurser(child, referencedIds);
			}
		}
	}
	
	/**
	 * Get a list of oval definitions referenced by extend definition
	 * elements in this definitions criteria.
	 * 
	 * @return List<OvalDefinition>
	 */
	public List<OvalDefinition> getReferencedDefinitions()
	{
		Set<String> defIdsReferenced = new HashSet<String>(); 
		List<OvalDefinition> definitions = new ArrayList<OvalDefinition>();

		Element rootCriteria = getElement().getChild("criteria", getElement().getNamespace());

        if(rootCriteria == null || rootCriteria.getChildren() == null || rootCriteria.getChildren().size() == 0)
        {
            return definitions;
        }
        
		refDefRecurser(rootCriteria, defIdsReferenced);
				
		Element definitionsElement = getRoot().getChild("definitions", getRoot().getNamespace());
		
		if(definitionsElement != null)
		{
			List children = definitionsElement.getChildren();
			
			if(children != null && children.size() > 0)
			{
				for(int index = 0; index < children.size(); index++)
				{
					Element child = (Element) children.get(index);
					
					String defId = child.getAttributeValue("id");
					
					if(defId != null)
					{
						if(defIdsReferenced.contains(defId))
						{
							OvalDefinition od = getParentDocument().getDefinitionWrapper();
							od.setElement(child);
							od.setRoot(getRoot());
							
							definitions.add(od);
						}
					}
					else
					{
						log.debug("OvalDef: getReferencedDefinitions: defId is NULL");						
					}
				}
			}
		}
		else
		{
			log.debug("OvalDef: getReferencedDefinitions: definitionsElement is NULL");
		}

		return definitions;
	}

	/**
	 * Add a list of references to this oval definition's metadata.
	 * 
	 * @param newRefs
	 */
	public void addReferences(List<OvalReference> newRefs)
	{
		Metadata md = getMetadata();
		md.addReferences(newRefs);
	}

	/**
	 * Get a list of references from this definition's metadata.
	 * 
	 * @return List<OvalReference>
	 */
	public List<OvalReference> getReferences()
	{
		Metadata md = getMetadata();

        if(md == null)
        {
            throw new RuntimeException("Metadata is NULL");
        }
		return md.getReferences();	
	}

    @Override
    public String toString()
    {
        StringBuilder ret = new StringBuilder();
        ret.append(getId());
        ret.append( " - " + getMetadata().getTitle());
        return ret.toString();
    }

    /**
     * Set the definition class of this definition.
     * 
     * @param defClass
     */
	public void setDefinitionClass(DefinitionClassEnum defClass)
	{
		if(defClass != null)
		{
			getElement().setAttribute("class", defClass.toString().toLowerCase());
		}		
	}
	
	private void matchFinder(CriteriaChild criteriaChild, MatchTracker tracker, String findString)
	{
		if (criteriaChild == null) {
			return;
		}
		String comment = criteriaChild.getComment();
		if (comment != null && comment.toLowerCase().indexOf(findString) > -1) 
		{
			tracker.found = true;
			return;
		}
		List<CriteriaChild> criteriaChildren = criteriaChild.getChildren();
		for (CriteriaChild child : criteriaChildren) 
		{
			matchFinder(child, tracker, findString);
		}
	}


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

        String title = getMetadata().getTitle();
        if(title != null)
        {
            if(title.toLowerCase().indexOf(lcFindString) > -1)
            {
                // matches
                return true;
            }
        }
        
        String desc = getMetadata().getDescription();
        
        if(desc != null)
        {
            if(desc.toLowerCase().indexOf(lcFindString) > -1)
            {
                // matches
                return true;
            }
        }

        // look through references
        List<OvalReference> references = getMetadata().getReferences();

        if(references != null && references.size() > 0)
        {
            for(int x = 0 ; x < references.size(); x++)
            {
                OvalReference or = references.get(x);

                String refId = or.getRefId();
                String refurl = or.getRefUrl();

                if(refId != null)
                {
                    if(refId.toLowerCase().contains(lcFindString))
                    {
                        return true;
                    }
                }

                if(refurl != null)
                {
                    if(refurl.toLowerCase().contains(lcFindString))
                    {
                        return true;
                    }
                }

            }
        }
        // check under criteria
        MatchTracker mt = new MatchTracker();
        matchFinder(getCriteria(), mt, lcFindString);

        return mt.found;
    }

    /**
     * Get the type of oval id.
     * 
     * @return String
     */
    @Override
    public String getOvalIdType()
    {
        return "def";
    }

    /**
     * Returns a Set of all variables referenced by this definition.
     * 
     * @return Set<OvalVariable>
     */
    public Set<OvalVariable> getReferencedVariables()
    {
    	HashSet<OvalVariable> varSet = new HashSet<OvalVariable>();
    	addExtVarRefsFromDefinition(this.element, varSet);
    	return varSet;
    }
    
    /**
     * Returns a Set of external variables referenced by this definition.
     * 
     * @return Set<OvalVariable>
     */
    public Set<OvalVariable> getReferencedExternalVariables()
    {
    	HashSet<OvalVariable> varSet = (HashSet<OvalVariable>) getReferencedVariables();
    	Iterator<OvalVariable> varSetIter = varSet.iterator();
    	while (varSetIter.hasNext())
    	{
    		OvalVariable var = varSetIter.next();
    		if (!var.getElementName().equals("external_variable"))
    		{
    			varSetIter.remove();
    		}
    	}
    	return varSet;
    }
       
    private void addExtVarRefsFromDefinition(Element definitionElement, Set<OvalVariable> extVarSet)
    {
    	Element criteriaElement = definitionElement.getChild("criteria", definitionElement.getNamespace());
		if (criteriaElement != null) {
			addExtVarRefsFromCriteria(criteriaElement, extVarSet);
		}
    }
    
    private void addExtVarRefsFromCriteria(Element criteriaElement, Set<OvalVariable> extVarSet)
    {
    	List<Element> criteriaChildren = criteriaElement.getChildren();
    	for (int i=0; i<criteriaChildren.size(); i++) {
    		Element child = criteriaChildren.get(i);
    		if (child.getName().equals("extend_definition")) {
    			String defId = child.getAttributeValue("definition_ref");
    			OvalDefinition ovalDef = getParentDocument().getOvalDefinition(defId);
    			Element ovalDefElement = ovalDef.getElement();
    			addExtVarRefsFromDefinition(ovalDefElement, extVarSet);
    		} else if (child.getName().equals("criteria")) {
    			addExtVarRefsFromCriteria(child, extVarSet);   			
    		} else if (child.getName().equals("criterion")) {
    			String testId = child.getAttributeValue("test_ref");
    			OvalTest ovalTest = getParentDocument().getOvalTest(testId);
    			if (ovalTest != null) {
    				addExtVarRefsFromTest(ovalTest, extVarSet);
    			}
    		}
    	}
    }
    
    private void addExtVarRefsFromTest(OvalTest ovalTest, Set<OvalVariable> extVarSet) {
    	OvalObject ovalObject = ovalTest.getObject();
    	if (ovalObject != null) {
    		extVarSet.addAll(ovalObject.getReferencedVariables());
    	}
    	OvalState ovalState = ovalTest.getState();
    	if (ovalState != null) {
    		extVarSet.addAll(ovalState.getReferencedVariables());
    	}
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
    	// tests
    	
    	// find out what tests this def references
		Set<String> testIdsReferenced = new HashSet<String>();
		refTestRecurser(getElement(), testIdsReferenced);
		
		// loop through and make sure all those tests exist.
		if(testIdsReferenced != null && testIdsReferenced.size() > 0)
		{
			for(Iterator<String> i = testIdsReferenced.iterator(); i.hasNext(); )
			{
				String testId = i.next();
				if(!odd.containsTest(testId))
				{
    				log.error("OvalDefinition " + getId() + " points to OvalTest " + testId + " which doesn't exist");
    				brokenRefs.add(testId);
				}
			}
		}
		
		//
		// Definitions
		
		// find out what other definitions this definition references
		Set<String> defIdsReferenced = new HashSet<String>(); 
		Element rootCriteria = getElement().getChild("criteria", getElement().getNamespace());

        if(rootCriteria != null && rootCriteria.getChildren() != null && rootCriteria.getChildren().size() > 0)
        {
    		refDefRecurser(rootCriteria, defIdsReferenced);
    		
    		// loop through and make sure all those definitions exist.
    		if(defIdsReferenced != null && defIdsReferenced.size() > 0)
    		{
    			for(Iterator<String> i = defIdsReferenced.iterator(); i.hasNext(); )
    			{
    				String defId = i.next();
    				if(!odd.containsDefinition(defId))
    				{
        				log.error("OvalDefinition " + getId() + " points to OvalDefinition " + defId + " which doesn't exist");
        				brokenRefs.add(defId);
    				}
    			}
    		}    		
        }
        
    	return brokenRefs;
    }
    
    /**
     * Tells whether this definition has empty criteria.
     * 
     * @return boolean
     */
    public boolean hasEmptyCriteria()
    {
    	boolean ret = false;
    	
    	Criteria criteriaRoot = getCriteria();
    	
    	if (criteriaRoot == null || criteriaRoot.getElement().getChildren().size() == 0)
    	{
    		ret = true;
    	}
    	
    	return ret;
    }
    
    /**
     * Get this definition's criteria.
     * 
     * @return Criteria
     */
	public Criteria getCriteria()
	{
		Criteria criteria = null;
		Namespace ovaldefNS = getElement().getNamespace();
		Element odc = getElement().getChild("criteria", ovaldefNS);
		
		if(odc != null)
		{			
			criteria = getParentDocument().getCriteriaWrapper();
			criteria.setElement(odc);
		}
		return criteria;		
	}    
	
	/**
	 * Set this definition's criteria
	 * @param criteria
	 */
	public void setCriteria(Criteria criteria) 
	{
		element.removeChildren("criteria", element.getNamespace());
		if (criteria != null) {
			insertChild(criteria, DEFINITION_ORDER, -1);
		}
	}
	
	public void setMetadata(Metadata metadata) {
		element.removeChildren("metadata", element.getNamespace());
		if (metadata != null) {
			insertChild(metadata, DEFINITION_ORDER, -1);
		}
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
		if (!(other instanceof OvalDefinition))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalDefinition other2 = (OvalDefinition) other;
		
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
    	
		// If the other object's class attribute does not match,
		// then return false.
		if (!(this.getDefinitionClass().toString().equals(other2.getDefinitionClass().toString())))
		{
			return false;
		}
		
    	// If the other object's metadata element does not match,
    	// then return false.
		Metadata myMetadata = this.getMetadata();
		Metadata otherMetadata = other2.getMetadata();
    	if (myMetadata != null && otherMetadata != null)
    	{
    		if (!(myMetadata.isDuplicateOf(otherMetadata)))
    		{
    			return false;
    		}
    	}
    	else if (myMetadata == null && otherMetadata != null)
    	{
    		return false;
    	}
    	else if (myMetadata != null && otherMetadata == null)
    	{
    		return false;
    	}
		
    	// If the other object's criteria element does not match,
    	// then return false.
    	Criteria myCriteria = this.getCriteria();
    	Criteria otherCriteria = other2.getCriteria();
    	if (myCriteria != null && otherCriteria != null)
    	{
    		if (!(myCriteria.isDuplicateOf(otherCriteria)))
    		{
    			return false;
    		}
    	}
    	else if (myCriteria == null && otherCriteria != null)
    	{
    		return false;
    	}
    	else if (myCriteria != null && otherCriteria == null)
    	{
    		return false;
    	}
		
		// Return true if we get to this point.
		return true;
	}
}
