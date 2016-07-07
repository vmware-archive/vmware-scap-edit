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

import java.io.File;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;

import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.bundle.SCAPDocumentBundle;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.cpe.CPEItem;
import com.g2inc.scap.library.domain.cpe.CPEItemCheck;
import com.g2inc.scap.library.domain.xccdf.Check;
import com.g2inc.scap.library.domain.xccdf.CheckContentRef;
import com.g2inc.scap.library.domain.xccdf.CheckExport;
import com.g2inc.scap.library.domain.xccdf.Rule;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

/**
 * Base class for any objects that will represent elements from
 * an oval document.
 * 
 */
public abstract class OvalElement extends SCAPElementImpl
{
    private static Logger log = Logger.getLogger(OvalElement.class);

	public static final String OVAL_XML_PLATFORM_NS_PREFIX = "http://oval.mitre.org/XMLSchema/oval-definitions-5#";
	public static final Namespace XSI_NS = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    
	public OvalDefinitionsDocument getParentDocument()
	{
		return (OvalDefinitionsDocument) getSCAPDocument();
	}

	public OvalElement(OvalDefinitionsDocument parentDocument)
	{
		setSCAPDocument(parentDocument);
		setRoot(parentDocument.getRoot());
		setDoc(parentDocument.getDoc());
	}

	/**
	 * Get the element's platform.  e.g. windows
	 * 
	 * @return String
	 */
    public String getPlatform()
    {
        String platform = null;

       	Namespace ns = getElement().getNamespace();
    	String nsUri = ns.getURI();
    	if (nsUri == null || !nsUri.startsWith(OVAL_XML_PLATFORM_NS_PREFIX))
        {
          //  log.debug("getPlatform(): No namespace uri for element " + getElementName());
            return platform;
    	}

    	platform = nsUri.substring(OVAL_XML_PLATFORM_NS_PREFIX.length());

        return platform;
    }

    /**
     * Return the type of oval id. This is only meaningful on elements
     * that have an id attribute.
     * 
     * @return String
     */
    public String getOvalIdType()
    {
        return null;
    }
    
    /**
     * Handle the refactoring associated with changing a definitions's id both within an
     * OvalDefinitionsDocument and in a SCAPDocumentBundle if applicable.
     * 
     * @param defBeingRenamed An OvalDefinition having it's id changed.
     * @param oldId The OvalDefinition's current id
     * @param id The id we are assigning this OvalDefinition
     * 
     * @see OvalDefinitionsDocument
     * @see SCAPDocumentBundle
     * @see OvalDefinition
     */
    private void handleDefinitionRename(OvalDefinition defBeingRenamed, String oldId, String id)
    {
		// this is an oval definition having it's id set
		
		// if the element of this definition has no parent, he hasn't been added to the document yet
		if(getElement().getParentElement() == null)
		{
			getElement().setAttribute("id", id);
			return;
		}

		// within a document only other definitions can reference a definition
		// we only need to look at definitions to see which ones reference the old id.
		List<OvalDefinition> allDocDefs = getParentDocument().getOvalDefinitions();
		
		if(allDocDefs == null || allDocDefs.size() == 0)
		{
			getElement().setAttribute("id", id);
			return;
		}
		
		// look at things that can reference a definition
		// first handle references within the document

		// loop through all definitions in the document
		for(int x = 0; x < allDocDefs.size(); x++)
		{
			OvalDefinition def = allDocDefs.get(x);
			
			String defId = def.getId();
			
			if(defId != null)
			{
				if(!defId.equals(oldId))
				{
					Criteria criteria = def.getCriteria();
					if (criteria != null) {
						renameExtDefs(criteria, oldId, id);
					}
				}
			}
			else
			{
				log.warn("allDocDefs contains a definition with a null id.", new Throwable());
			}
		} // end of definition for loop
		
		// second handle updating other documents in the bundle that might reference this
		// definition.
		if(getParentDocument().getBundle() != null)
		{
			File ovalDocFile = new File(getParentDocument().getFilename());
			SCAPDocumentBundle bundle = getParentDocument().getBundle();
						
			// changing a definition can impact cpe dictionaries and xccdf documents.
			// Let's look to see if the bundle has either of those.
			
			List<CPEDictionaryDocument> cpeDicts = bundle.getCPEDictionaryDocs();			
			if(cpeDicts != null && cpeDicts.size() > 0)
			{
				// there are some, let's check to see if they reference 
				for(int cpeDictIdx = 0; cpeDictIdx < cpeDicts.size(); cpeDictIdx++)
				{
					CPEDictionaryDocument dictionary = cpeDicts.get(cpeDictIdx);
					
					List<CPEItem> items = dictionary.getItems();
					
					if(items != null && items.size() > 0)
					{
						for(int itemIdx = 0; itemIdx < items.size(); itemIdx++)
						{
							CPEItem item = items.get(itemIdx);
							
							List<CPEItemCheck> checks = item.getChecks();
							
							if(checks != null && checks.size() > 0)
							{
								for(int checkIdx = 0; checkIdx < checks.size(); checkIdx++)
								{
									CPEItemCheck check = checks.get(checkIdx);
									
									String href = check.getHref();
									
									if(href != null)
									{
										if(!href.startsWith("http"))
										{
											String fnameOnly = ovalDocFile.getName();											
											if(href.equals(fnameOnly))
											{
												// same file
												// now check id
												
												String checkId = check.getCheckId();
												
												if(checkId != null)
												{
													if(checkId.equals(oldId))
													{
														// change it to the new one.
														log.info("Updating check in item " + item.getName()
																+ " from pointing to " + oldId + " to pointing "
																+ "to " + id);
														
														check.setCheckId(id);
														dictionary.setDirty(true);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			// now check if there are any xccdf documents that might reference this definition
			List<XCCDFBenchmark> benchmarks = bundle.getXCCDFBenchmarks();
			if(benchmarks != null && benchmarks.size() > 0)
			{
				for(int benchmarkIdx = 0; benchmarkIdx < benchmarks.size(); benchmarkIdx++)
				{
					XCCDFBenchmark benchmark = benchmarks.get(benchmarkIdx);
					
					// since rules can point to definitions, let's loop through and check them.
					List<Rule> allRules = benchmark.getAllRules();
					if(allRules != null && allRules.size() > 0)
					{
						for(int ruleIdx = 0; ruleIdx < allRules.size(); ruleIdx++)
						{
							Rule rule = allRules.get(ruleIdx);
							
							List<Check> checks = rule.getCheckList();
							if(checks != null && checks.size() > 0)
							{
								for(int checkIdx = 0; checkIdx < checks.size(); checkIdx++)
								{
									Check check = checks.get(checkIdx);
									
									List<CheckContentRef> contentRefs = check.getCheckContentRefList();
									if(contentRefs != null && contentRefs.size() > 0)
									{
										for(int ccrIdx = 0; ccrIdx < contentRefs.size(); ccrIdx++)
										{
											CheckContentRef contentRef = contentRefs.get(ccrIdx);
											
											String href = contentRef.getHref();
											
											if(href != null)
											{
												if(href.equals(ovalDocFile.getName()))
												{
													// refers to the oval doc that was modified
													String name = contentRef.getName();
													
													if(name != null)
													{
														if(name.equals(oldId))
														{
															// this matches the id of the definition we are renaming.
															// go ahead and change it.
															contentRef.setName(id);
															benchmark.setDirty(true);
														}
													}
												}
											}
										} 
									} 
								}
							}
						}
					}
				}
			}			
		}
		
		getElement().setAttribute("id", id);
		getParentDocument().setDirty(true);
		getParentDocument().updateRenamedDefinition(oldId, id);
    }

    /**
     * Handle the refactoring associated with changing a tests's id in an
     * OvalDefinitionsDocument.
     * 
     * @param testBeingRenamed An OvalTest having it's id changed.
     * @param oldId The OvalTest's current id
     * @param id The id we are assigning this OvalTest
     * @param allowDuplicateId The rename is being called on the test
     *                         to map all his dependents to another test
     *                         in order to remove this test.
     * 
     * @see OvalDefinitionsDocument
     * @see OvalTest
     */
    private void handleTestRename(OvalTest testBeingRenamed, String oldId, String id, boolean allowDuplicateId)
    {
        // if the element of this test has no parent, he hasn't been added to the document yet
        if(getElement().getParentElement() == null)
        {
                getElement().setAttribute("id", id);
                return;
        }

        // this is an oval test having it's id set, so
        // look at things that can reference a test

        // within a document only definitions can reference a test

        // we only need to look at definitions to see which ones reference the old id.
        List<OvalDefinition> allDocDefs = getParentDocument().getOvalDefinitions();

        boolean skipDefs = false;

        if(allDocDefs == null || allDocDefs.size() == 0)
        {
                // no defs to reference this test so skip ahead to rename
                skipDefs = true;
        }
        if (!skipDefs)
        {
            // loop through all definitions in the document
            for (int x = 0; x < allDocDefs.size(); x++)
            {
                OvalDefinition def = allDocDefs.get(x);

                String defId = def.getId();

                if (defId != null)
                {
                    Criteria criteria = def.getCriteria();
                    if (criteria != null)
                    {
                            renameCriterionElements(criteria, oldId, id);
                    }
                } else
                {
                        log.warn("allDocDefs contains a definition with a null id.",
                                        new Throwable());
                }
            } // end of definition for loop
        }

        getElement().setAttribute("id", id);
        if(allowDuplicateId)
        {
            // all the dependents of this test have been mapped to the new name
            // and this test is going to be deleted, so remove it's entry from the map.
            getParentDocument().remove(this);
        }
        else
        {
            // this is a normal rename, update the map accordingly
    		getParentDocument().updateRenamedTest(oldId, id);
        }
		getParentDocument().setDirty(true);
    }

    /**
     * Handle the refactoring associated with changing an objects's id in an
     * OvalDefinitionsDocument.
     * 
     * @param objectBeingRenamed An OvalObject having it's id changed.
     * @param oldId The OvalObject's current id
     * @param id The id we are assigning this OvalObject
     * @param allowDuplicateId The rename is being called on the object
     *                         to map all his dependents to another object
     *                         in order to remove this object.
     * 
     * @see OvalDefinitionsDocument
     * @see OvalObject
     */
    private void handleObjectRename(OvalObject objectBeingRenamed, String oldId, String id, boolean allowDuplicateId)
    {
		// if the element of this object has no parent, he hasn't been added to the document yet
		if(getElement().getParentElement() == null)
		{
			getElement().setAttribute("id", id);
			return;
		}

		// this is an oval object having it's id set, so
		// look at things that can reference an object
				
		// within a document tests, objects, and variables can reference an object
    	
		// first look at tests to see which ones reference the old id.
    	boolean skipTests = false;
		List<OvalTest> allDocTests = getParentDocument().getOvalTests();
		if(allDocTests == null || allDocTests.size() == 0)
		{
			skipTests = true;
		}
		
		if(!skipTests)
		{
			// loop through all tests in the document
			for (int x = 0; x < allDocTests.size(); x++)
			{
				OvalTest test = allDocTests.get(x);

				String objId = test.getObjectId();

				if (objId != null)
				{
					if(objId.equals(oldId))
					{
						// set him to the new id.
						test.setObjectId(id);
					}
				}
				else
				{
					if (!(test.getElementName() == "unknown_test"))
					{
					log.warn("allDocTests contains a test with a null object id: " + test.getId(),
							new Throwable());
				}
				}
			} // end of tests for loop
		} // end if not skipTests

		// now look at other objects to see which ones reference the old id.
    	boolean skipObjects = false;
		List<OvalObject> allDocObjects = getParentDocument().getOvalObjects();
		if(allDocObjects == null || allDocObjects.size() == 0)
		{
			skipObjects = true;
		}
		
		if(!skipObjects)
		{
			// loop through all objects in the document
			for (int x = 0; x < allDocObjects.size(); x++)
			{
				OvalObject oo = allDocObjects.get(x);
				List<OvalObjectChild> children = oo.getChildren();
				for (OvalObjectChild child : children) 
				{
					handleObjectRenameChild(child, oldId, id);
				}
				
				
			} // end of objects for loop
		} // end if not skipObjects

		// now look at other objects to see which ones reference the old id.
    	boolean skipVariables = false;
		List<OvalVariable> allDocVariables = getParentDocument().getOvalVariables();
		if(allDocVariables == null || allDocVariables.size() == 0)
		{
			skipVariables = true;
		}
		
		if(!skipVariables)
		{
			// loop through all variables in the document
			for (int x = 0; x < allDocVariables.size(); x++)
			{
				OvalVariable ov = allDocVariables.get(x);

				DefaultMutableTreeNode children = ov.getChildren();
				
				if(children != null)
				{
					Enumeration<DefaultMutableTreeNode> chEnum = children.depthFirstEnumeration();
					
					while(chEnum.hasMoreElements())
					{
						DefaultMutableTreeNode child = chEnum.nextElement();
						
						Object userObj = child.getUserObject();
						
						if(userObj != null)
						{
							if(userObj instanceof OvalVariableComponentObject)
							{
								OvalVariableComponentObject ovco = (OvalVariableComponentObject) userObj;
								String objectId = ovco.getObjectId();
								
								if(objectId != null)
								{
									if(objectId.equals(oldId))
									{
										// update it to new id
										ovco.setObjectId(id);
									}
								}
							}
						}
					}
				}
				
			} // end of variables for loop
		} // end if not skipVariables
		getElement().setAttribute("id", id);
        if(allowDuplicateId)
        {
            // all the dependents of this object have been mapped to the new name
            // and this object is going to be deleted, so remove it's entry from the map.
            getParentDocument().remove(this);
        }
        else
        {
            // this is a normal rename, update the map accordingly
    		getParentDocument().updateRenamedObject(oldId, id);
        }
		getParentDocument().setDirty(true);
    }
    
    /**
     * Handle updating children of an object
     * 
     * @param child OvalObjectChild being validated
     * @param oldId Previous id of the object
     * @param id The new id of the object 
     */
    public void handleObjectRenameChild(OvalObjectChild child, String oldId, String id) 
    {
		if(child instanceof OvalObjectParameter)
		{
			OvalObjectParameter oop = (OvalObjectParameter) child;
			String varRef = oop.getVarRef();		
			if(varRef != null)
			{
				if(varRef.equals(oldId))
				{
					oop.setVarRef(id);
				}
			}
			
			if(oop.getElementName().equals("var_ref"))
			{
				// this is a special case
				String oldVal = oop.getValue();
				if(oldVal != null && oldVal.equals(oldId))
				{
					// this references the old variable id, so change it
					oop.setValue(id);
				}
			}
			
		} else if (child instanceof OvalObjectReference)
    	{
    		OvalObjectReference oor = (OvalObjectReference) child;
			String objId = oor.getObjectId();			
			if(objId != null)
			{
					if(objId.equals(oldId))
					{
						// update it to new id
						oor.setObjectId(id);
					}
				}
    	} else if (child instanceof OvalObjectFilter)
    	{
			OvalObjectFilter filter = (OvalObjectFilter) child;
			String stateId = filter.getStateId();
			
			if(stateId != null)
			{
				if(stateId.equals(oldId))
				{
					// update it to new id
					filter.setStateId(id);
				}
			}
    	}
    	List<OvalObjectChild> children = child.getChildren();
    	for (OvalObjectChild grandChild : children)
    	{
    		handleObjectRenameChild(grandChild, oldId, id);
    	}
    }

    

    /**
     * Handle the refactoring associated with changing a states's id in an
     * OvalDefinitionsDocument.
     * 
     * @param stateBeingRenamed An OvalState having it's id changed.
     * @param oldId The OvalState's current id
     * @param id The id we are assigning this OvalState
     * @param allowDuplicateId The rename is being called on the state
     *                         to map all his dependents to another state
     *                         in order to remove this state.
     *
     * @see OvalDefinitionsDocument
     * @see OvalState
     */
    private void handleStateRename(OvalState stateBeingRenamed, String oldId, String id, boolean allowDuplicateId)
    {
    //	log.debug("handleStateRename called oldId=" + oldId + ", newId=" + id);
        // if the element of this state has no parent, he hasn't been added to the document yet
        if(getElement().getParentElement() == null)
        {
                getElement().setAttribute("id", id);
                return;
        }

        // this is an oval state having it's id set, so
        // look at things that can reference a state

        // within a document tests and objects can reference a state

        // first look at tests to see which ones reference the old id.
    	boolean skipTests = false;
        List<OvalTest> allDocTests = getParentDocument().getOvalTests();
      //  log.debug("handleStateRename got list of " + (allDocTests == null ? "null" : allDocTests.size()) + " tests");
        if(allDocTests == null || allDocTests.size() == 0)
        {
                skipTests = true;
        }

        if(!skipTests)
        {
            // loop through all tests in the document
            for (int x = 0; x < allDocTests.size(); x++)
            {
                OvalTest test = allDocTests.get(x);
                List stateElements = test.getElement().getChildren("state", test.getElement().getNamespace());

                if(stateElements != null && stateElements.size() > 0)
                {
                    for(int y = 0; y < stateElements.size(); y++)
                    {
                        Object child = stateElements.get(y);
                        if(child instanceof Element)
                        {
                            Element stateRefElement = (Element) child;

                            // get the state_ref attribute
                            String refAttrib = stateRefElement.getAttributeValue("state_ref");

                            if(refAttrib != null && refAttrib.equals(oldId))
                            {
                                // update it to point to new id
                                stateRefElement.setAttribute("state_ref", id);
                            }
                        }
                    } // end of state reference for loop
                }
            } // end of tests for loop
        } // end if not skipTests

        // now look at objects to see which ones reference the old id.
    	boolean skipObjects = false;
        List<OvalObject> allDocObjects = getParentDocument().getOvalObjects();
        if(allDocObjects == null || allDocObjects.size() == 0)
        {
                skipObjects = true;
        }

        if(!skipObjects)
        {
            // loop through all objects in the document
            for (int x = 0; x < allDocObjects.size(); x++)
            {
                OvalObject oo = allDocObjects.get(x);
                List<OvalObjectChild> children = oo.getChildren();
                for (OvalObjectChild child : children)
                {
                        handleObjectRenameChild(child, oldId, id);
                }

            } // end of objects for loop
        } // end if not skipObjects

        getElement().setAttribute("id", id);
        if(allowDuplicateId)
        {
            // all the dependents of this state have been mapped to the new name
            // and this state is going to be deleted, so remove it's entry from the map.
            getParentDocument().remove(this);
        }
        else
        {
            // this is a normal rename, update the map accordingly
            getParentDocument().updateRenamedState(oldId, id);
        }
        getParentDocument().setDirty(true);
    }

    /**
     * Handle the refactoring associated with changing a variables's id in an
     * OvalDefinitionsDocument.
     * 
     * @param variableBeingRenamed An OvalVariable having it's id changed.
     * @param oldId The OvalVariable's current id
     * @param id The id we are assigning this OvalVariable
     * @param allowDuplicateId The rename is being called on the variable
     *                         to map all his dependents to another variable
     *                         in order to remove this variable.
     * 
     * @see OvalDefinitionsDocument
     * @see OvalVariable
     */
    private void handleVariableRename(OvalVariable variableBeingRenamed, String oldId, String id, boolean allowDuplicateId)
    {
		// if the element of this variable has no parent, he hasn't been added to the document yet
		if(getElement().getParentElement() == null)
		{
			getElement().setAttribute("id", id);
			return;
		}
		
		// this is an oval variable having it's id set, so
		// look at things that can reference a variable
				
		// within a document objects, states, and variables can reference a variable

		// now look at objects to see which ones reference the old id.
    	boolean skipObjects = false;
		List<OvalObject> allDocObjects = getParentDocument().getOvalObjects();
		if(allDocObjects == null || allDocObjects.size() == 0)
		{
			skipObjects = true;
		}
		
		if(!skipObjects)
		{
			// loop through all objects in the document
			for (int x = 0; x < allDocObjects.size(); x++)
			{
				OvalObject oo = allDocObjects.get(x);
				List<OvalObjectChild> children = oo.getChildren();
				for (OvalObjectChild child : children) 
				{
					handleObjectRenameChild(child, oldId, id);
				}							
			} // end of objects for loop
		} // end if not skipObjects

		// now look at states to see which ones reference the old id.
    	List<OvalState> allDocStates = getParentDocument().getOvalStates();

    	// loop through all states in the document
    	for (OvalState os : allDocStates) {
    		List<OvalStateParameter> parameters = os.getParameters();
    		for(OvalStateParameter osp : parameters) {
    			String varRef = osp.getVarRef();
    			if(varRef != null && varRef.equals(oldId)) {
    				osp.setVarRef(id);
    			}
    			List<OvalStateParameter> fieldParms = osp.getFieldParameters();
    			for (OvalStateParameter fieldParm : fieldParms) {
    				varRef = fieldParm.getVarRef();
    				if (varRef != null && varRef.equals(oldId)) {
    					fieldParm.setVarRef(id);
    				}
    			}
    		}
    	} 


        // now look at variables to see which ones reference the old id.
    	boolean skipVariables = false;
		List<OvalVariable> allDocVariables = getParentDocument().getOvalVariables();
		if(allDocVariables == null || allDocVariables.size() == 0)
		{
			skipVariables = true;
		}
		
		if(!skipVariables)
		{
			// loop through all variables in the document
			for (int x = 0; x < allDocVariables.size(); x++)
			{
				OvalVariable ov = allDocVariables.get(x);

				DefaultMutableTreeNode children = ov.getChildren();
				
				if(children != null)
				{
					Enumeration<DefaultMutableTreeNode> chEnum = children.depthFirstEnumeration();
					
					while(chEnum.hasMoreElements())
					{
						DefaultMutableTreeNode child = chEnum.nextElement();
						
						Object userObj = child.getUserObject();
						
						if(userObj != null)
						{
							if(userObj instanceof OvalVariableComponentVariable)
							{
								OvalVariableComponentVariable ovcv = (OvalVariableComponentVariable)userObj;
								String varId = ovcv.getVariableId();
								if(varId != null)
								{
									if(varId.equals(oldId))
									{
										ovcv.setVariableId(id);
									}
								}
							}
						}
					}
				}
			} // end of variables for loop
		} // end if not skipVariables

		// if we are part of a bundle do some other checking
		if(getParentDocument().getBundle() != null)
		{
			File ovalDocFile = new File(getParentDocument().getFilename());
			SCAPDocumentBundle bundle = getParentDocument().getBundle();
			
			// check xccdf documents in the bundle to see if they reference
			// this variable.
			List<XCCDFBenchmark> benchmarks = bundle.getXCCDFBenchmarks();
			if(benchmarks != null && benchmarks.size() > 0)
			{
				for(int benchmarkIdx = 0; benchmarkIdx < benchmarks.size(); benchmarkIdx++)
				{
					XCCDFBenchmark benchmark = benchmarks.get(benchmarkIdx);
					
					// since rules can point to definitions, let's loop through and check them.
					List<com.g2inc.scap.library.domain.xccdf.Rule> allRules = benchmark.getAllRules();
					if(allRules != null && allRules.size() > 0)
					{
						for(int ruleIdx = 0; ruleIdx < allRules.size(); ruleIdx++)
						{
							Rule rule = allRules.get(ruleIdx);
							
							List<Check> checks = rule.getCheckList();
							if(checks != null && checks.size() > 0)
							{
								for(int checkIdx = 0; checkIdx < checks.size(); checkIdx++)
								{
									Check check = checks.get(checkIdx);

									if(check.getSystem() == null || !check.getSystem().equals(OvalDefinition.OVAL_NAMESPACE))
									{
										continue;
									}
									
									List<CheckExport> exports = check.getExportList();
									
									List<CheckContentRef> contentRefs = check.getCheckContentRefList();
									
									// WARNING: this is kludgy because the check exports
									// doesn't have an href to tell me which file the id
									// is to be found in.  I'm going to use the first 
									// content ref and the first export in the check.
									// TODO: keep an eye on the schema to see if this
									//       is ever better defined.
									if(exports != null && exports.size() > 0)
									{
										if(contentRefs != null && contentRefs.size() > 0)
										{
											CheckExport exp = exports.get(0);
											CheckContentRef contentRef = contentRefs.get(0);
											
											String href = contentRef.getHref();
											
											if(href != null)
											{
												if(href.equals(ovalDocFile.getName()))
												{
													if(exp.getExportName() != null && exp.getExportName().equals(oldId))
													{
														exp.setExportName(id);
														benchmark.setDirty(true);
													}
												}
											}											
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		getElement().setAttribute("id", id);
        if(allowDuplicateId)
        {
            // all the dependents of this variable have been mapped to the new name
            // and this variable is going to be deleted, so remove it's entry from the map.
            getParentDocument().remove(this);
        }
        else
        {
            // this is a normal rename, update the map accordingly
    		getParentDocument().updateRenamedVariable(oldId, id);
        }
		getParentDocument().setDirty(true);
    }

    @Override
	public void setId(String id)
	{
		setId(id, false);
	}

    /**
     * This method does the same thing as the setId in SCAPElement except that
     * we actually refactor any references to the old name, if it exists to point to the new one.
     *
     * @param id String representing id
     */
    public void setId(String id, boolean allowDuplicateId)
    {
    	String oldId = getElement().getAttributeValue("id");
    	
    	if(oldId == null)
    	{
            // there was no old element id so it couldn't have been referenced by anything
            // that would need to be updated to point at the new id.
            getElement().setAttribute("id", id);
            if(getElement().getParentElement() != null)
            {
                    getParentDocument().setDirty(true);
            }
            return;
    	}
    	
    	if(getParentDocument() == null || getRoot() == null)
    	{
            throw new IllegalStateException("The element having the id set has not been initialized "
                            + "properly.  The id being set is " + id);
    	}
    	if (!allowDuplicateId)
        {
            VersionedOvalElement voe = getParentDocument().getById(id);
            if(voe != null)
            {
                    throw new IllegalArgumentException("The id you are trying to set already exists in the document: " + id);
            }
        }
    	
    	// we got here, so there is an old id to refactor and the id we want to refactor to does not exist in the document
    	// first handle references within the document
    	
    	if(this instanceof OvalDefinition)
    	{
            OvalDefinition od = (OvalDefinition) this;
            handleDefinitionRename(od, oldId, id);
    	} 
    	else if(this instanceof OvalTest)
    	{
            OvalTest ot = (OvalTest) this;
            handleTestRename(ot, oldId, id, allowDuplicateId);
    	} 
    	else if(this instanceof OvalObject)
    	{
            OvalObject oo = (OvalObject) this;
            handleObjectRename(oo, oldId, id, allowDuplicateId);
    	} 
    	else if(this instanceof OvalState)
    	{
            OvalState os = (OvalState) this;
            handleStateRename(os, oldId, id, allowDuplicateId);
    	} 
    	else if(this instanceof OvalVariable)
    	{
            OvalVariable ov = (OvalVariable) this;
            handleVariableRename(ov, oldId, id, allowDuplicateId);
    	} 
    	else
    	{
            getElement().setAttribute("id",id);
    	}
    }
    
    private void renameExtDefs(CriteriaChild criteriaChild, String oldDefId, String newDefId) 
    {
    	if (criteriaChild instanceof ExtendDefinition) 
    	{
    		ExtendDefinition extDef = (ExtendDefinition) criteriaChild;
			if(extDef.getDefinitionId() != null && extDef.getDefinitionId().equals(oldDefId))
			{
				// this references the definition having a new id set
				// go ahead and point him at the new id.
				extDef.setDefinitionId(newDefId);
			}    				
    	} else if (criteriaChild instanceof Criteria) 
    	{
    		List<CriteriaChild> children = criteriaChild.getChildren();
    		for (CriteriaChild child : children) 
    		{
    			renameExtDefs(child, oldDefId, newDefId);
    		}
    	}
    }  
    
    private void renameCriterionElements(CriteriaChild criteriaChild, String oldTestId, String newTestId) 
    {
    	if (criteriaChild instanceof Criterion) 
    	{
    		Criterion criterion = (Criterion) criteriaChild;
			if(criterion.getTestId() != null && criterion.getTestId().equals(oldTestId))
			{
				// this references the test having a new id set
				// go ahead and point him at the new id.
				criterion.setTestId(newTestId);
			}    				
    	} else if (criteriaChild instanceof Criteria) 
    	{
    		List<CriteriaChild> children = criteriaChild.getChildren();
    		for (CriteriaChild child : children) 
    		{
    			renameCriterionElements(child, oldTestId, newTestId);
    		}
    	}
    } 
    
    protected boolean areStringsEqualOrBothNull(String string1, String string2) {
    	boolean result = true;
    	if (string1 != null && string2 != null) {
    		result = string1.equals(string2);
    	} else if (string1 == null && string2 != null) {
    		result = false;
    	} else if (string1 != null && string2 == null) {
    		result = false;
    	}
    	return result;
    }    
    
    /**
     * This method determines if this object is a duplicate of another object.
     * 
     * @param other
     * @return boolean
     */
	public boolean isDuplicateOf(Object other)
	{
		// If the other object is null, then return false.
		if (other == null)
		{
			return false;
		}
		
		// If the other object is not an instance of this class,
		// then return false.
		if (!(other instanceof OvalElement))
		{
			return false;
		}
		
		// Cast the other object to this class.
		OvalElement other2 = (OvalElement) other;
		
		// If the other object's element name does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getElementName(), this.getElementName())))
    	{
    		return false;
    	}
		
		// If the other object's id attribute does not match,
		// then return false.
    	if (!(areStringsEqualOrBothNull(other2.getId(), this.getId())))
    	{
    		return false;
    	}
		
		// Return true if we get to this point.
		return true;
	}
}
