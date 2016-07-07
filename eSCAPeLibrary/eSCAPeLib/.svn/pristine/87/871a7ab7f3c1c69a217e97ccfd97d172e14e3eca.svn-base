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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;


import org.apache.log4j.Logger;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ContentFilter;
import org.jdom.filter.Filter;

import com.g2inc.scap.library.content.style.ContentStyle;
import com.g2inc.scap.library.domain.NamespaceReferenceCounter;
import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentClassEnum;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.schema.NameDoc;
import com.g2inc.scap.library.schema.PlatformNameKey;
import com.g2inc.scap.library.schema.SchemaLocator;
import com.g2inc.scap.library.util.DateUtility;

/**
 * Represents an xml document containing oval definitions.
 * 
 * @author ssill
 */
public abstract class OvalDefinitionsDocument extends SCAPDocument
{

    private static final Logger log = Logger.getLogger(OvalDefinitionsDocument.class);
    private final static String OVAL_ID_BASE_TOKEN = "generated.oval.base.identifier=";
    public static final String DEFAULT_COMMENT = "Default comment, please change";
    /**
     * This map is used by insertChild to determine the correct order, according to the schema,
     * to insert elements.
     */
    public final static HashMap<String, Integer> DOC_ORDER = new HashMap<String, Integer>();
//    private List<AvailableObjectBehavior> behaviorsList = null;
    private Map<PlatformNameKey, List<AvailableObjectBehavior>> behaviorsMap = null;
    static
    {
        DOC_ORDER.put("generator", 0);
        DOC_ORDER.put("definitions", 1);
        DOC_ORDER.put("tests", 2);
        DOC_ORDER.put("objects", 3);
        DOC_ORDER.put("states", 4);
        DOC_ORDER.put("variables", 5);
    }
    private String baseId = null;
    private int startingNumber = 1;

	private Map<String, OvalElement> ovalDefinitionMap = new LinkedHashMap<String, OvalElement>();
    private Map<String, OvalElement> ovalTestMap = new HashMap<String, OvalElement>();
    private Map<String, OvalElement> ovalObjectMap = new HashMap<String, OvalElement>();
    private Map<String, OvalElement> ovalStateMap = new HashMap<String, OvalElement>();
    private Map<String, OvalElement> ovalVariableMap = new HashMap<String, OvalElement>();

    private Map<String, OvalElement> getOvalDefinitionMap()
    {
        return ovalDefinitionMap;
    }

    private Map<String, OvalElement> getOvalTestMap()
    {
        return ovalTestMap;
    }

    private Map<String, OvalElement> getOvalObjectMap()
    {
        return ovalObjectMap;
    }

    private Map<String, OvalElement> getOvalStateMap()
    {
        return ovalStateMap;
    }

    private Map<String, OvalElement> getOvalVariableMap()
    {
        return ovalVariableMap;
    }
    /**
     * A regex string that can be used to validate that a supplied definition id is valid.
     */
    public static final String DEFINITION_ID_REGEX = "oval:[A-Za-z0-9_\\-\\.]+:def:[1-9][0-9]*";
    /**
     * A compiled regex pattern that can be used to validate that a supplied definition id is valid.
     */
    public static Pattern DEFINITION_ID_PATTERN;

    {
        try
        {
            DEFINITION_ID_PATTERN = Pattern.compile(DEFINITION_ID_REGEX);
        } catch (Exception e)
        {
            throw new IllegalStateException("Error compiling pattern " + DEFINITION_ID_REGEX, e);
        }
    }
    /**
     * A regex string that can be used to validate that a supplied test id is valid.
     */
    public static final String TEST_ID_REGEX = "oval:[A-Za-z0-9_\\-\\.]+:tst:[1-9][0-9]*";
    /**
     * A compiled regex pattern that can be used to validate that a supplied test id is valid.
     */
    public static Pattern TEST_ID_PATTERN;

    {
        try
        {
            TEST_ID_PATTERN = Pattern.compile(TEST_ID_REGEX);
        } catch (Exception e)
        {
            throw new IllegalStateException("Error compiling pattern " + TEST_ID_REGEX, e);
        }
    }

    /**
     * A regex string that can be used to validate that a supplied object id is valid.
     */
    public static final String OBJECT_ID_REGEX = "oval:[A-Za-z0-9_\\-\\.]+:obj:[1-9][0-9]*";
    /**
     * A compiled regex pattern that can be used to validate that a supplied object id is valid.
     */
    public static Pattern OBJECT_ID_PATTERN;

    {
        try
        {
            OBJECT_ID_PATTERN = Pattern.compile(OBJECT_ID_REGEX);
        } catch (Exception e)
        {
            throw new IllegalStateException("Error compiling pattern " + OBJECT_ID_REGEX, e);
        }
    }

    /**
     * A regex string that can be used to validate that a supplied state id is valid.
     */
    public static final String STATE_ID_REGEX = "oval:[A-Za-z0-9_\\-\\.]+:ste:[1-9][0-9]*";
    /**
     * A compiled regex pattern that can be used to validate that a supplied state id is valid.
     */
    public static Pattern STATE_ID_PATTERN;

    {
        try
        {
            STATE_ID_PATTERN = Pattern.compile(STATE_ID_REGEX);
        } catch (Exception e)
        {
            throw new IllegalStateException("Error compiling pattern " + STATE_ID_REGEX, e);
        }
    }

    /**
     * A regex string that can be used to validate that a supplied variable id is valid.
     */
    public static final String VARIABLE_ID_REGEX = "oval:[A-Za-z0-9_\\-\\.]+:var:[1-9][0-9]*";
    /**
     * A compiled regex pattern that can be used to validate that a supplied variable id is valid.
     */
    public static Pattern VARIABLE_ID_PATTERN;

    {
        try
        {
            VARIABLE_ID_PATTERN = Pattern.compile(VARIABLE_ID_REGEX);
        } catch (Exception e)
        {
            throw new IllegalStateException("Error compiling pattern " + VARIABLE_ID_REGEX, e);
        }
    }

    /**
     * Constructor taking a JDOM Document object.
     *
     * @param doc A JDOM Document object
     */
    public OvalDefinitionsDocument(Document doc)
    {
    	super(doc);
    	ovalDefinitionMap = new LinkedHashMap<String, OvalElement>();
    	ovalTestMap = new LinkedHashMap<String, OvalElement>();
    	ovalObjectMap = new LinkedHashMap<String, OvalElement>();
    	ovalStateMap = new LinkedHashMap<String, OvalElement>();
    	ovalVariableMap = new LinkedHashMap<String, OvalElement>();

    	refreshDefinitionMap(); // to force set of def ids to be populated
    	refreshTestMap(); // to force set of test ids to be populated
    	refreshObjectMap(); // to force set of object ids to be populated
    	refreshStateMap(); // to force set of state ids to be populated
    	refreshVariableMap(); // to force set of var ids to be populated
    	
    	setDocumentClass(SCAPDocumentClassEnum.OVAL);
    }

    /**
     * Set the name of the product that generated this oval document.
     *
     * @param productName
     */
    public void setGeneratorProduct(String productName)
    {
        Element generatorElement = getRoot().getChild("generator", getRoot().getNamespace());

        if (generatorElement != null)
        {
            Element productNameElement = generatorElement.getChild("product_name", getRoot().getNamespace("oval"));

            if (productNameElement != null)
            {
                productNameElement.setText(productName);
            } else
            {
                log.debug("productNameElement is NULL");
            }
        } else
        {
            log.debug("generatorElement is NULL");
        }
    }

    /**
     * Set the version of the product that generated this oval document.
     *
     * @param productVersion
     */
    public void setGeneratorProductVersion(String productVersion)
    {
        Element generatorElement = getRoot().getChild("generator", getRoot().getNamespace());

        if (generatorElement != null)
        {
            Namespace ovalNS = getRoot().getNamespace("oval");

            Element productVersionElement = generatorElement.getChild("product_version", ovalNS);

            if (productVersionElement != null)
            {
                productVersionElement.setText(productVersion);
            } else
            {
                productVersionElement = new Element("product_version", ovalNS);
                productVersionElement.setText(productVersion);

                Element productNameElement = generatorElement.getChild("product_name", ovalNS);

                if (productNameElement != null)
                {
                    int index = generatorElement.indexOf(productNameElement);

                    if (index > -1)
                    {
                        generatorElement.addContent(index + 1, productVersionElement);
                    }
                } else
                {
                    generatorElement.addContent(productVersionElement);
                }
            }
        } else
        {
            log.debug("generatorElement is NULL");
        }
    }

    /**
     * Set the date the document was generated.
     * 
     * @param date The date the document was generated
     */
    public void setGeneratorDate(Date date)
    {
        setGeneratorRawDate(DateUtility.getOVALGeneratorDateTimeFromDate(date));
    }

    private void setGeneratorRawDate(String rawdate)
    {
        Element generatorElement = getRoot().getChild("generator", getRoot().getNamespace());

        if (generatorElement != null)
        {
            Element timestampElement = generatorElement.getChild("timestamp", getRoot().getNamespace("oval"));

            if (timestampElement != null)
            {
                timestampElement.setText(rawdate);
            } else
            {
                log.debug("timestampElement is NULL");
            }
        } else
        {
            log.debug("generatorElement is NULL");
        }
    }

    /**
     * Set the documents schema version in the generator element.
     *
     * @param version A version, such as 5.3
     */
    public void setSchemaVersion(String version)
    {
        Element generatorElement = getRoot().getChild("generator", getRoot().getNamespace());

        if (generatorElement != null)
        {
            Element schemaVersionElement = generatorElement.getChild("schema_version", getRoot().getNamespace("oval"));

            if (schemaVersionElement != null)
            {
                schemaVersionElement.setText(version);
            } else
            {
                log.debug("schemaVersionElement is NULL");
            }
        } else
        {
            log.debug("generatorElement is NULL");
        }
    }

    /**
     * Get the schema version from the generator element.
     *
     * @return String
     */
    public String getSchemaVersion()
    {
        String retVersion = null;

        Element generatorElement = getRoot().getChild("generator", getRoot().getNamespace());

        if (generatorElement != null)
        {
            Element schemaVersionElement = generatorElement.getChild("schema_version", getRoot().getNamespace("oval"));

            if (schemaVersionElement != null)
            {
                retVersion = schemaVersionElement.getValue();
            } else
            {
                log.debug("schemaVersionElement is NULL");
            }
        } else
        {
            log.debug("generatorElement is NULL");
        }
        return retVersion;
    }

    /**
     * Get the name of the product that generated this document.
     *
     * @return String
     */
    public String getGeneratorProduct()
    {
        String retProduct = null;

        Element generatorElement = getRoot().getChild("generator", getRoot().getNamespace());

        if (generatorElement != null)
        {
            Element productNameElement = generatorElement.getChild("product_name", getRoot().getNamespace("oval"));

            if (productNameElement != null)
            {
                retProduct = productNameElement.getValue();
            } else
            {
                log.debug("productNameElement is NULL");
            }
        } else
        {
            log.debug("generatorElement is NULL");
        }
        return retProduct;
    }

    /**
     * Get the version of the product that generated this document.
     *
     * @return String
     */
    public String getGeneratorProductVersion()
    {
        String retProduct = null;

        Element generatorElement = getRoot().getChild("generator", getRoot().getNamespace());

        if (generatorElement != null)
        {
            Element productVersionElement = generatorElement.getChild("product_version", getRoot().getNamespace("oval"));

            if (productVersionElement != null)
            {
                retProduct = productVersionElement.getValue();
            } else
            {
                log.debug("productNameElement is NULL");
            }
        } else
        {
            log.debug("generatorElement is NULL");
        }
        return retProduct;
    }


    /**
     * Return a set of the unique reference ids in this document.
     *
     * @return Set<String>
     */
    public abstract Set<String> getReferenceIds();

    /**
     * Returns the version specific wrapper for an existing definition element.
     *
     * @return OvalDefinition
     */
    public abstract OvalDefinition getDefinitionWrapper();

    /**
     * Returns the version specific wrapper for an existing test element.
     *
     * @return OvalTest
     */
    public abstract OvalTest getTestWrapper();

    /**
     * Returns the version specific wrapper for an existing object element.
     *
     * @return OvalObject
     */
    public abstract OvalObject getObjectWrapper();

    /**
     * Returns the version specific wrapper for an existing state element.
     *
     * @return OvalState
     */
    public abstract OvalState getStateWrapper();

    /**
     * Returns the version specific wrapper for an existing variable element.
     *
     * @return OvalVariable
     */
//	public abstract OvalVariable getVariableWrapper();
    /**
     * Returns the version specific wrapper for an existing element that
     * is a parameter of a state.
     *
     * @return OvalStateParameter
     */
    public abstract OvalStateParameter getStateParameterWrapper();

    /**
     * Returns the version specific wrapper for an existing element that
     * is a parameter of an object.
     *
     * @return OvalObjectParameter
     */
    public abstract OvalObjectParameter getObjectParameterWrapper();

    /**
     * Returns the version specific wrapper for an existing reference element
     * in a definition's metadata element.
     *
     * @return OvalReference
     */
    public abstract OvalReference getReferenceWrapper();

    /**
     * Returns the version specific wrapper for an existing note element.
     *
     * @return OvalNote
     */
    public abstract OvalNote getOvalNoteWrapper();
    
    /**
     * Returns the version specific wrapper for an existing notes element.
     *
     * @return OvalNotes
     */
    public abstract OvalNotes getOvalNotesWrapper();
    
    /**
     * Returns the version specific wrapper for existing affected elements
     * in a definition's metadata element.
     *
     * @return AffectedItemContainer
     */
    public abstract AffectedItemContainer getAffectedItemContainerWrapper();

    /**
     * Returns the version specific wrapper for an existing affected platform
     * element in a definition's metadata element.
     *
     * @return AffectedPlatform
     */
    public abstract AffectedPlatform getAffectedPlatformWrapper();

    /**
     * Returns the version specific wrapper for an existing affected product
     * element in a definition's metadata element.
     *
     * @return AffectedProduct
     */
    public abstract AffectedProduct getAffectedProductWrapper();

    /**
     * Returns the version specific wrapper for an existing definition's metadata element.
     *
     * @return Metadata
     */
    public abstract Metadata getMetadataWrapper();

    /**
     * Returns the version specific wrapper for an existing definition's criteria elements.
     *
     * @return Criteria
     */
    public abstract Criteria getCriteriaWrapper();

    /**
     * Returns the version specific wrapper for an existing definition's criterion elements.
     *
     * @return Criterion
     */
    public abstract Criterion getCriterionWrapper();

    /**
     * Returns the version specific wrapper for an existing definition's extend_definition elements.
     *
     * @return ExtendDefinition
     */
    public abstract ExtendDefinition getExtendDefinitionWrapper();

    /**
     * Returns the version specific wrapper for an existing object's object_reference elements.
     *
     * @return OvalObjectReference
     */
    public abstract OvalObjectReference getObjectReferenceWrapper();

    /**
     * Returns the version specific wrapper for an existing object's filter elements.
     *
     * @return OvalObjectFilter
     */
    public abstract OvalObjectFilter getObjectFilterWrapper();

    /**
     * Returns the version specific wrapper for an existing object's set elements.
     *
     * @return OvalObjectSet
     */
    public abstract OvalObjectSet getObjectSetWrapper();

    /**
     * Returns the version specific wrapper for an existing variable's literal_component elements.
     *
     * @return OvalVariableComponentLiteral
     */
    public abstract OvalVariableComponentLiteral getLiteralVariableComponentWrapper();

    /**
     * Returns the version specific wrapper for an existing variable's object_component elements.
     *
     * @return OvalVariableComponentObject
     */
    public abstract OvalVariableComponentObject getObjectVariableComponentWrapper();

    /**
     * Returns the version specific wrapper for an existing variable's variable_component elements.
     *
     * @return OvalVariableComponentVariable
     */
    public abstract OvalVariableComponentVariable getVariableComponentWrapper();

    /**
     * Create a new OvalDefinition object suitable for adding to this document.
     *
     * @param type The definition class, e.g vulnerability
     *
     * @return OvalDefinition
     */
    public abstract OvalDefinition createDefinition(DefinitionClassEnum type);

    /**
     * Get an array of operations supported for the given data type.
     *
     * @param type The datatype
     *
     * @return OperEnum[]
     */
    public abstract OperEnum[] getOperationsForDatatype(TypeEnum type);

    /**
     * Get an array of families supported by an affected element.
     *
     * @return AffectedItemFamilyEnum[]
     */
    public abstract AffectedItemFamilyEnum[] getSupportedFamilies();

    /**
     * Get an array of operations supported for the given data type.
     *
     * @param type The datatype
     *
     * @return OperEnum[]
     */
    public abstract OperEnum[] getOperationsForDatatype(OvalDatatype type);
    
    public abstract List<NameDoc> getDataTypeEnumerations();

    /**
     * Add a new oval definition.
     *
     * @param ovalDef
     */
    public void addDefinition(OvalDefinition ovalDef)
    {
    	addDefinition(ovalDef, -1);
    }
    
    /**
     * Add a new oval definition to the document in the specified position in the list of
     * existing definitions.  An index less than 0 will cause the definition element to be added after
     * any others.
     *
     * @param ovalDef
     * @param index
     */
    public void addDefinition(OvalDefinition ovalDef, int index)
    {
        while (ovalDefinitionMap.containsKey(ovalDef.getId()))
        {
            String newId = getNextId(ovalDef, ovalDefinitionMap);
            log.info("oval def id " + ovalDef.getId() + " was in use, changing it to " + newId);
            ovalDef.getElement().setAttribute("id", newId);
        }
        ovalDefinitionMap.put(ovalDef.getId(), ovalDef);
        Element definitionsElement = getElement().getChild("definitions", getElement().getNamespace());
        if (definitionsElement == null)
        {
            definitionsElement = new Element("definitions", getElement().getNamespace());
            insertChild(definitionsElement, DOC_ORDER);
        }

        addNamespaceToDocIfNecessary(ovalDef.getElement().getNamespace());

        List<Namespace> addlNamespaces = ovalDef.getElement().getAdditionalNamespaces();
        if(addlNamespaces != null && addlNamespaces.size() > 0)
        {
            for(int x = 0; x < addlNamespaces.size(); x++)
            {
                Namespace ns = (Namespace) addlNamespaces.get(x);
                addNamespaceToDocIfNecessary(ns);
            }
        }
        //addlNamespaces.clear();
        
        if (ovalDef.getElement().getParent() == null)
        {
            if (index < 0)
            {
                definitionsElement.addContent(ovalDef.getElement());
            } else
            {
                definitionsElement.addContent(index, ovalDef.getElement());
            }
        } else
        {
            if (index < 0)
            {
                definitionsElement.addContent((Element) ovalDef.getElement().clone());
            } else
            {
                definitionsElement.addContent(index, (Element) ovalDef.getElement().clone());
            }
        }
    }

    /**
     * Add a new oval state to the document.
     * 
     * @param state
     */
    public void addState(OvalState state)
    {
    	addState(state, -1);
    }
    
    /**
     * Add a new oval state to the document.
     * 
     * @param state
     * @param index Where to insert under states.  -1 means add at the end
     */
    public void addState(OvalState state, int index)
    {
        if (ovalStateMap.containsKey(state.getId()))
        {
            String newId = getNextId(state, ovalStateMap);
            log.info("oval state id " + state.getId() + " was in use, changing it to " + newId);
            state.getElement().setAttribute("id", newId);
        }
        ovalStateMap.put(state.getId(), state);
        Element statesElement = root.getChild("states", this.element.getNamespace());
        if (statesElement == null)
        {
            statesElement = new Element("states", getElement().getNamespace());
            insertChild(statesElement, DOC_ORDER);
        }

        addNamespaceToDocIfNecessary(state.getElement().getNamespace());
        List<Namespace> addlNamespaces = state.getElement().getAdditionalNamespaces();
        if(addlNamespaces != null && addlNamespaces.size() > 0)
        {
            for(int x = 0; x < addlNamespaces.size(); x++)
            {
                Namespace ns = (Namespace) addlNamespaces.get(x);
                addNamespaceToDocIfNecessary(ns);
            }
        }
        //addlNamespaces.clear();

        if (state.getElement().getParent() == null)
        {
            if(index == -1)
            {
                // minus 1 is special, it means add at the end
                statesElement.addContent(state.getElement());
            }
            else
            {
                statesElement.addContent(index, state.getElement());
            }
        }
        else
        {
            if(index == -1)
            {
                statesElement.addContent((Element) state.getElement().clone());
            }
            else
            {
                statesElement.addContent(index, (Element) state.getElement().clone());
            }
        }
    }

    /**
     * Add a new oval object to the document.
     * 
     * @param obj
     */
    public void addObject(OvalObject obj)
    {
    	addObject(obj, -1);
    }
    
    /**
     * Add a new oval object to the document.
     *
     * @param obj
     * @param index Where to insert under objects.  -1 means add at the end.
     */
    public void addObject(OvalObject obj, int index)
    {
        if (ovalObjectMap.containsKey(obj.getId()))
        {
            String newId = getNextId(obj, ovalObjectMap);
            log.info("oval object id " + obj.getId() + " was in use, changing it to " + newId);
            obj.getElement().setAttribute("id", newId);
        }

        ovalObjectMap.put(obj.getId(), obj);
        Element objectsElement = root.getChild("objects", this.element.getNamespace());
        if (objectsElement == null)
        {
            objectsElement = new Element("objects", getElement().getNamespace());
            insertChild(objectsElement, DOC_ORDER);
        }

        addNamespaceToDocIfNecessary(obj.getElement().getNamespace());

        List<Namespace> addlNamespaces = obj.getElement().getAdditionalNamespaces();
        if(addlNamespaces != null && addlNamespaces.size() > 0)
        {
            for(int x = 0; x < addlNamespaces.size(); x++)
            {
                Namespace ns = (Namespace) addlNamespaces.get(x);
                addNamespaceToDocIfNecessary(ns);
            }
        }
        //addlNamespaces.clear();
        
        if (obj.getElement().getParent() == null)
        {
            if(index == -1)
            {
                // minus 1 is special.  Add at the end of existing content
                objectsElement.addContent(obj.getElement());
            }
            else
            {
                objectsElement.addContent(index, obj.getElement());
            }
        }
        else
        {
            if(index == -1)
            {
                // minus 1 is special.  Add at the end of existing content
                objectsElement.addContent((Element) obj.getElement().clone());
            }
            else
            {
                objectsElement.addContent(index, (Element) obj.getElement().clone());
            }
        }
    }

    private void addNamespaceToDocIfNecessary(Namespace newNS)
    {
        List documentNamespaces = getRoot().getAdditionalNamespaces();

        if (documentNamespaces != null)
        {
            boolean found = false;

            for (int x = 0; x < documentNamespaces.size(); x++)
            {
                Namespace ns = (Namespace) documentNamespaces.get(x);

                if (ns.getURI().equals(newNS.getURI()))
                {
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                String uri = newNS.getURI();

                int poundLoc = uri.indexOf("#");

                if (poundLoc > -1)
                {
                    String prefix = uri.substring(poundLoc + 1) + "-def";

                    getRoot().addNamespaceDeclaration(Namespace.getNamespace(prefix, uri));
                    log.info("new namespace added to document: " + newNS.getURI());
                }
            }
        }
    }

    /**
     * Add a new oval test to the document.
     *
     * @param test
     */
    public void addTest(OvalTest test)
    {
    	addTest(test, -1);
    }
    
    /**
     * Add a new oval test to the document.
     *
     * @param test
     * @param index Where to insert under tests.  -1 means add at the end
     */

    public void addTest(OvalTest test, int index)
    {
        if (ovalTestMap.containsKey(test.getId()))
        {
            String newId = getNextId(test, ovalTestMap);
            log.info("oval test id " + test.getId() + " was in use, changing it to " + newId);
            test.getElement().setAttribute("id", newId);
        }

        ovalTestMap.put(test.getId(), test);

        Element testsElement = root.getChild("tests", this.element.getNamespace());

        if (testsElement == null)
        {
            testsElement = new Element("tests", getElement().getNamespace());
            insertChild(testsElement, DOC_ORDER);
        }

        addNamespaceToDocIfNecessary(test.getElement().getNamespace());
        List<Namespace> addlNamespaces = test.getElement().getAdditionalNamespaces();
        if(addlNamespaces != null && addlNamespaces.size() > 0)
        {
            for(int x = 0; x < addlNamespaces.size(); x++)
            {
                Namespace ns = (Namespace) addlNamespaces.get(x);
                addNamespaceToDocIfNecessary(ns);
            }
        }
        //addlNamespaces.clear();

        if (test.getElement().getParent() == null)
        {
            if(index == -1)
            {
                // minus 1 is special, it means add at the end
                testsElement.addContent(test.getElement());
            }
            else
            {
                testsElement.addContent(index, test.getElement());
            }
        }
        else
        {
            if(index == -1)
            {
                // minus 1 is special, it means add at the end
                testsElement.addContent((Element) test.getElement().clone());
            }
            else
            {
                testsElement.addContent(index, (Element) test.getElement().clone());
            }
        }
    }

    /**
     * Add a new variable to the document.
     *
     * @param var
     */
    public void addVariable(OvalVariable var)
    {
    	addVariable(var, -1);
    }
    
    /**
     * Add a new variable to the document.
     *
     * @param var
     * @param index Where to insert under variables.  -1 means add at the end
     */
    public void addVariable(OvalVariable var, int index)
    {
        if (ovalVariableMap.containsKey(var.getId()))
        {
            String newId = getNextId(var, ovalVariableMap);
            log.info("oval variable id " + var.getId() + " was in use, changing it to " + newId);
            var.getElement().setAttribute("id", newId);
        }
        ovalVariableMap.put(var.getId(), var);
        Element variablesElement = root.getChild("variables", this.element.getNamespace());
        if (variablesElement == null)
        {
            variablesElement = new Element("variables", getElement().getNamespace());
            insertChild(variablesElement, DOC_ORDER);
        }

        addNamespaceToDocIfNecessary(var.getElement().getNamespace());
        List<Namespace> addlNamespaces = var.getElement().getAdditionalNamespaces();
        if(addlNamespaces != null && addlNamespaces.size() > 0)
        {
            for(int x = 0; x < addlNamespaces.size(); x++)
            {
                Namespace ns = (Namespace) addlNamespaces.get(x);
                addNamespaceToDocIfNecessary(ns);
            }
        }
        //addlNamespaces.clear();

        if (var.getElement().getParent() == null)
        {
            if(index == -1)
            {
                // minus 1 is special, it means add at the end
                variablesElement.addContent(var.getElement());
            }
            else
            {
                variablesElement.addContent(index, var.getElement());
            }
        } else
        {
            if(index == -1)
            {
                variablesElement.addContent((Element) var.getElement().clone());
            }
            else
            {
                variablesElement.addContent(index, (Element) var.getElement().clone());
            }
        }
    }

    /**
     * Remove an oval element from the document.  This could be a
     *  definition, test, object, state, or variable.
     *
     * @param oe
     */
    public void remove(OvalElement oe)
    {
        if (oe instanceof OvalDefinition)
        {
            removeDefinition((OvalDefinition) oe);
        } else if (oe instanceof OvalTest)
        {
            removeTest((OvalTest) oe);
        } else if (oe instanceof OvalObject)
        {
            removeObject((OvalObject) oe);
        } else if (oe instanceof OvalState)
        {
            removeState((OvalState) oe);
        } else if (oe instanceof OvalVariable)
        {
            removeVariable((OvalVariable) oe);
        }
    }

    private void removeDefinition(OvalDefinition od)
    {
        ovalDefinitionMap.remove(od.getId());
        od.getElement().detach();
    }

    private void removeTest(OvalTest ot)
    {
        ovalTestMap.remove(ot.getId());
        ot.getElement().detach();
    }

    private void removeObject(OvalObject oo)
    {
        ovalObjectMap.remove(oo.getId());
        oo.getElement().detach();
    }

    private void removeState(OvalState os)
    {
        ovalStateMap.remove(os.getId());
        os.getElement().detach();
    }

    private void removeVariable(OvalVariable ov)
    {
        ovalVariableMap.remove(ov.getId());
        ov.getElement().detach();
    }

    /**
     * Get a set of definition ids contained in the document.
     * 
     * @return Set<String>
     */
    public Set<String> getDocDefinitionIds()
    {
        return ovalDefinitionMap.keySet();
    }

    /**
     * Get a set of test ids contained in the document.
     * 
     * @return Set<String>
     */
    public Set<String> getDocTestIds()
    {
        return ovalTestMap.keySet();
    }

    /**
     * Get a set of object ids contained in the document.
     * 
     * @return Set<String>
     */
    public Set<String> getDocObjectIds()
    {
        return ovalObjectMap.keySet();
    }

    /**
     * Get a set of state ids contained in the document.
     * 
     * @return Set<String>
     */
    public Set<String> getDocStateIds()
    {
        return ovalStateMap.keySet();
    }

    /**
     * Get a set of variable ids contained in the document.
     * 
     * @return Set<String>
     */
    public Set<String> getDocVariableIds()
    {
        return ovalVariableMap.keySet();
    }

    /**
     * Get the base id of the document.  This is a value stored in a comment
     * within the document that can be used to construct autonumbered ids as
     * new definitions, tests, objects, states, and variables are added to the document.
     * The base id is portion of the id is "gov.nist.fdcc.xp" in the id "oval:gov.nist.fdcc.xp:def:23".

     * @return String
     */
    public String getBaseId()
    {
        if (baseId == null)
        {
            String base = null;
            Comment comment = getIdBaseComment();
            if (comment != null)
            {
                String text = comment.getText();
                if (text.startsWith(OVAL_ID_BASE_TOKEN))
                {
                    base = text.substring(OVAL_ID_BASE_TOKEN.length());
                    if (!base.matches("[A-Za-z0-9_\\-\\.]+"))
                    {
                        throw new IllegalStateException("Default oval id base(" + base + ") contains illegal characters - only letters, numbers, underscores, dashes, and periods are allowed");
                    }
                    baseId = base;
                }
            }
        }
        return baseId;
    }

    protected String getNextDefId()
    {
        String ret = null;
        String theBaseId = getBaseId();

        theBaseId = "oval:" + theBaseId + ":def:";
        int num = startingNumber;

        boolean unique = false;

        while (!unique)
        {
            if (!ovalDefinitionMap.containsKey(theBaseId + num))
            {
                ret = theBaseId + num;
                unique = true;
            }
            num++;
        }


        return ret;
    }

    /**
     * Set the base id of the document.
     * 
     * @param base The base id
     */
    public void setBaseId(String base)
    {
        if (!base.matches("[A-Za-z0-9_\\-\\.]+"))
        {
            throw new IllegalStateException("Default oval id base(" + base + ") contains illegal characters - only letters, numbers, underscores, dashes, and periods are allowed");
        }
        baseId = base;
        String text = OVAL_ID_BASE_TOKEN + base;
        Comment comment = getIdBaseComment();
        if (comment == null)
        {
            comment = new Comment(text);
            root.addContent(comment);
        } else
        {
            comment.setText(text);
        }
    }

    private Comment getIdBaseComment()
    {
        Comment idBaseComment = null;
        Filter commentOnlyFilter = new ContentFilter(ContentFilter.COMMENT);
        List commentNodes = root.getContent(commentOnlyFilter);
        for (int i = 0; i < commentNodes.size(); i++)
        {
            Comment comment = (Comment) commentNodes.get(i);
            String text = comment.getText();
            if (text.startsWith(OVAL_ID_BASE_TOKEN))
            {
                idBaseComment = comment;
                break;
            }
        }
        return idBaseComment;
    }

    /**
     * Get an array of the platform names used in this document.
     * 
     * @return String[]
     */
    public String[] getPlatformsUsed()
    {
        ArrayList<String> usedNameList = new ArrayList<String>();
        List<Namespace> namespaces = root.getAdditionalNamespaces();
        for (int i = 0; i < namespaces.size(); i++)
        {
            String uri = namespaces.get(i).getURI();
            if (uri.startsWith("http://oval.mitre.org/XMLSchema/oval-definitions-5#"))
            {
                String platform = uri.substring("http://oval.mitre.org/XMLSchema/oval-definitions-5#".length());
                usedNameList.add(platform);
            }
        }
        return usedNameList.toArray(new String[0]);
    }

    /**
     * Get a list of valid platform names for the version of oval this document represents.
     *
     * @return List<String>
     */
    public List<String> getValidPlatforms()
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        return manager.getValidPlatforms(getDocumentType());
    }

    /**
     * Return whether a given platform supports a given test
     *
     * @param platform Name of a platform support by this oval document
     * @param testType String name of a test, e.g. file_test
     *
     * @return boolean
     */
    public boolean platformSupportsTest(String platform, String testType)
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();

        String[] platforms = new String[]
        {
            platform, "independent"
        };
        List<NameDoc> platSuppTests = manager.getValidTestTypes(getDocumentType(), platforms);

        if (platSuppTests != null && platSuppTests.size() > 0)
        {
            for (int x = 0; x < platSuppTests.size(); x++)
            {
                NameDoc entry = platSuppTests.get(x);
                if (entry.getName() != null && entry.getName().equals(testType))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Get a list of valid test names for the version of oval this document represents.
     *
     * @return List<NameDoc>
     */
    public List<NameDoc> getValidTestTypes()
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        String[] platformsUsed = getPlatformsUsed();
        return manager.getValidTestTypes(getDocumentType(), platformsUsed);
    }

    /**
     * Get a list of valid test names for the supplied platform.
     *
     * @param platform
     * @return List<NameDoc>
     */
    public List<NameDoc> getValidTestTypes(String platform)
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        String[] platforms =
        {
            platform
        };
        return manager.getValidTestTypes(getDocumentType(), platforms);
    }

    /**
     * Get a list of valid object types for the version of oval this document represents.
     *
     * @return List<NameDoc>
     */
    public List<NameDoc> getValidObjectTypes()
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        String[] platformsUsed = getPlatformsUsed();
        return manager.getValidObjectTypes(getDocumentType(), platformsUsed);
    }

    /**
     * Get a list of valid object types for the supplied platform.
     *
     * @param platform
     * @return List<NameDoc>
     */
    public List<NameDoc> getValidObjectTypes(String platform)
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        String[] platforms =
        {
            platform
        };
        return manager.getValidObjectTypes(getDocumentType(), platforms);
    }

    /**
     * Get a list of valid state types for the version of oval this document represents.
     *
     * @return List<NameDoc>
     */
    public List<NameDoc> getValidStateTypes()
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        String[] platformsUsed = getPlatformsUsed();
        return manager.getValidStateTypes(getDocumentType(), platformsUsed);
    }

    /**
     * Get a list of valid state types for the supplied platform.
     *
     * @param platform
     * @return List<NameDoc>
     */
    public List<NameDoc> getValidStateTypes(String platform)
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        String[] platforms =
        {
            platform
        };
        return manager.getValidStateTypes(getDocumentType(), platforms);
    }

    /**
     * Get a list of valid object entities for the supplied platform and object type.
     *
     * @param platform
     * @param objectType
     * @return List<OvalEntity>
     */
    public List<OvalEntity> getValidObjectEntityTypes(String platform,
            String objectType)
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        return manager.getValidObjectEntityTypes(getDocumentType(), platform, objectType);
    }

    /**
     * Get a list of valid state entities for the supplied platform and state type.
     *
     * @param platform
     * @param stateType
     * @return List<OvalEntity>
     */
    public List<OvalEntity> getValidStateEntityTypes(String platform,
            String stateType)
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        return manager.getValidStateEntityTypes(getDocumentType(), platform, stateType);
    }

    /**
     * Get a list of values for the given enumeration name.
     *
     * @param enumerationName
     * @return List<NameDoc>
     */
    public List<NameDoc> getEnumerationValues(String enumerationName)
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        Map<String, List<NameDoc>> map = manager.getEnumValuesForOvalVersion(getDocumentType());
        List<NameDoc> ret = map.get(enumerationName);
        return ret;
    }

    /**
     * Get a list of possible enumeration types.
     *
     * @return List<NameDoc>
     */
    public List<NameDoc> getEnumerationTypes()
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        List<NameDoc> result = new ArrayList<NameDoc>(manager.getEnumTypesForOvalVersion(getDocumentType()).values());
        Collections.sort(result);
        return result;
    }

    /**
     * Get the NameDoc for a given enumeration name.
     *
     * @param enumerationName
     * @return NameDoc
     */
    public NameDoc getEnumerationNameDoc(String enumerationName)
    {
        SCAPContentManager manager = SCAPContentManager.getInstance();
        return manager.getEnumTypesForOvalVersion(getDocumentType()).get(enumerationName);
    }

    /**
     * Get a list of available behaviors for a given platform key.
     *
     * @param key
     * @return List<AvailableObjectBehavior>
     */
    public List<AvailableObjectBehavior> getBehaviors(PlatformNameKey key)
    {		
    	if (behaviorsMap == null) {
    		initBehaviorsMap();
    	}
    	return behaviorsMap.get(key);
    }
    
    /**
     * Initialize the document-specific version of the behaviors map by cloning
     * the schema-based map. This is needed because we might have multiple 
     * OvalDefinitionsDocuments open, each with a different 'style'. Since the 
     * style can override some default behaviors values, we need one copy per
     * document. 
     */
    protected void initBehaviorsMap() {
    	if (behaviorsMap == null) {
    		behaviorsMap = new HashMap<PlatformNameKey, List<AvailableObjectBehavior>>();
    	} else {
    		behaviorsMap.clear();  
    	}
        SCAPContentManager manager = SCAPContentManager.getInstance();        
        Map<PlatformNameKey, List<AvailableObjectBehavior>> schemaMap = manager.getBehaviorValues(getDocumentType());
        for (PlatformNameKey key : schemaMap.keySet()) {
        	List<AvailableObjectBehavior> schemaList = schemaMap.get(key);
        	List<AvailableObjectBehavior> behaviorsList = new ArrayList<AvailableObjectBehavior>();
        	for (AvailableObjectBehavior schemaAOB : schemaList) {
        		AvailableObjectBehavior aob = new AvailableObjectBehavior(schemaAOB);
        		behaviorsList.add(aob);
        	}
        	behaviorsMap.put(key, behaviorsList);
        }
    }
    
    /**
     * For this particular document, the style might override 'behaviors' default specified in 
     * the schema. When the style is changed, we need to reset the default behavoirs value.
     * 
     * @param style the new ContentStyle 
     */
    @Override
    public void setContentStyle(ContentStyle style) {
    	super.setContentStyle(style);
    	initBehaviorsMap();  // reset to schema defaults
    	if (style == null) {
    		return;
    	}
    	String recurseDirectionDefault = this.contentStyle.getProperty(OvalObjectParameter.RECURSE_DIRECTION_DEFAULT);
    	System.out.println("setContentStyle called " + style.getStyleName() 
    			+ " genProperty(" + OvalObjectParameter.RECURSE_DIRECTION_DEFAULT + ") and got " 
    			+ recurseDirectionDefault);
		if (recurseDirectionDefault != null) {
	        for (PlatformNameKey key : behaviorsMap.keySet()) {
	        	List<AvailableObjectBehavior> behaviorList = behaviorsMap.get(key);
	        	for (AvailableObjectBehavior aob : behaviorList) {
	        		aob.setDefaultValue(recurseDirectionDefault);
	        	}
	        }			
		}
    }
    
    /**
     * Get the xml namespace for a supplied platform.
     *
     * @param platform
     * @return Namespace
     */
    public Namespace getNamespaceFromPlatform(String platform)
    {
        Namespace namespace = null;
        Namespace xsiNS = null;
        String platformURI = "http://oval.mitre.org/XMLSchema/oval-definitions-5#" + platform;

        List<Namespace> namespaces = root.getAdditionalNamespaces();

        for (int i = 0; i < namespaces.size(); i++)
        {
            String uri = namespaces.get(i).getURI();

            if (uri.equals(platformURI))
            {
                namespace = namespaces.get(i);
            } else if (uri.indexOf("XMLSchema-instance") != -1)
            {
                xsiNS = namespaces.get(i);
            }
        }

        if (namespace == null)
        {
            SchemaLocator slocator = SchemaLocator.getInstance();

            String prefix = platform + "-def";
            namespace = Namespace.getNamespace(prefix, platformURI);
            root.addNamespaceDeclaration(namespace);
//	        String schemaLocation = root.getAttributeValue("schemaLocation", xsiNS);
//	        StringBuilder sb = new StringBuilder(schemaLocation);
//            String schemaLoc = slocator.getOvalSchemaUri(getDocumentType(), platformURI);
//            sb.append(" " + platformURI + " " + schemaLoc);
//            log.info("Adding namespace ref " + platformURI + " with schemaLoc ")
//            root.setAttribute("schemaLocation", sb.toString(), xsiNS);
        }
        return namespace;
    }

    /**
     * Create an oval object for later insertion in the document.
     *
     * @param platform
     * @param objType
     * @return OvalObject
     */
    public OvalObject createObject(String platform, String objType)
    {
        OvalObject obj = getObjectWrapper();
        String newId = getNextId(obj, ovalObjectMap);
        Namespace ns = getNamespaceFromPlatform(platform);
        Element objElement = new Element(objType, ns.getURI());
        obj.setElement(objElement);
        obj.setRoot(this.getRoot());
        obj.setAttribute("id", newId);
        obj.setAttribute("version", "1");
        obj.setComment(DEFAULT_COMMENT);
        return obj;
    }

    /**
     * Create an oval state for later insertion in the document.
     * @param platform
     * @param stateType
     * @return OvalState
     */
    public OvalState createState(String platform, String stateType)
    {
        OvalState state = getStateWrapper();
        String newId = getNextId(state, ovalStateMap);
        Namespace ns = getNamespaceFromPlatform(platform);
        Element stateElement = new Element(stateType, ns.getURI());

        state.setElement(stateElement);
        state.setRoot(this.getRoot());
        state.setAttribute("id", newId);
        state.setAttribute("version", "1");
        state.setComment(DEFAULT_COMMENT);
        return state;
    }

    /**
     * Create a oval reference for later insertion in the metadata of a definition.
     * 
     * @return OvalReference
     */
    public OvalReference createReference()
    {
        Namespace ns = getRoot().getNamespace();
        Element refElement = new Element("reference");
        refElement.setNamespace(ns);

        OvalReference oref = getReferenceWrapper();
        oref.setElement(refElement);
        oref.setRoot(getRoot());

        return oref;
    }

    /**
     * Create an oval test for later insertion in the document.
     * @param platform
     * @param testType
     * @return OvalTest
     */
    public OvalTest createTest(String platform, String testType)
    {
        OvalTest test = getTestWrapper();
        String newId = getNextId(test, ovalTestMap);
        Namespace ns = getNamespaceFromPlatform(platform);
        Element testElement = new Element(testType, ns.getURI());

        test.setElement(testElement);
        test.setRoot(this.getRoot());

        test.setAttribute("id", newId);
        test.setAttribute("version", "1");
        test.setCheck(CheckEnumeration.ALL);

        test.setComment(DEFAULT_COMMENT);
        return test;
    }

    /**
     * Create an oval variable of the supplied type for later insertion in 
     * the document.
     * 
     * @param varType
     * @return OvalVariable
     */
    public OvalVariable createVariable(String varType)
    {
        OvalVariable var = getVariableWrapper(varType);
        String newId = getNextId(var, ovalVariableMap);

        Namespace ns = root.getNamespace();
        Element varElement = new Element(varType, ns.getURI());

        var.setElement(varElement);
        var.setRoot(this.getRoot());
        var.setAttribute("id", newId);
        var.setAttribute("version", "1");
        var.setComment(DEFAULT_COMMENT);

        return var;
    }

    /**
     * Instantiate the appropriate implementation of OvalVariable to match this document
     * and return it.  This will be used to wrap an existing element.
     * 
     * @param varType
     * @return OvalVariable
     */
    public OvalVariable getVariableWrapper(String varType)
    {
        OvalVariable var = null;
        if (varType.equals("local_variable"))
        {
            var = new OvalLocalVariable(this);
        } else if (varType.equals("external_variable"))
        {
            var = new OvalExternalVariable(this);
        } else if (varType.equals("constant_variable"))
        {
            var = new OvalConstantVariable(this);
        } else
        {
            throw new IllegalArgumentException("Can't instantiate OvalVariable of type:" + varType);
        }
        return var;
    }

    private String getNextId(OvalElement ovalElement, Map<String, ? extends OvalElement> map)
    {
        String newId = "oval:" + getBaseId() + ":" + ovalElement.getOvalIdType() + ":";
        int numericComp = startingNumber;
        String tryId = null;
        do
        {
            tryId = newId + numericComp++;
        } while (map.containsKey(tryId));
        return tryId;
    }

    private String getNextId(OvalElement ovalElement, Map<String, OvalElement> map1, Map<String, ? extends OvalElement> map2)
    {
        String newId = "oval:mace:" + ovalElement.getOvalIdType() + ":";
        int numericComp = startingNumber;
        String tryId = null;
        do
        {
            tryId = newId + numericComp++;
        } while (map1.containsKey(tryId) || map2.containsKey(tryId));
        return tryId;
    }

    /**
     * Check if this document contains a particular definition id.
     * 
     * @param defId
     * @return boolean
     */
    public boolean containsDefinition(String defId)
    {
        boolean ret = false;

        ret = ovalDefinitionMap.containsKey(defId);

        return ret;
    }

    /**
     * Check if this document contains a particular object id.
     * @param objectId
     * @return boolean
     */
    public boolean containsObject(String objectId)
    {
        boolean ret = false;

        ret = ovalObjectMap.containsKey(objectId);

        return ret;
    }

    /**
     * Check if this document contains a particular state id.
     *
     * @param stateId The state id we are looking for
     * @return boolean
     */
    public boolean containsState(String stateId)
    {
        boolean ret = false;

        ret = ovalStateMap.containsKey(stateId);

        return ret;
    }

    /**
     * Check if this document contains a particular test id.
     *
     * @param testId
     * @return boolean
     */
    public boolean containsTest(String testId)
    {
        boolean ret = false;

        ret = ovalTestMap.containsKey(testId);

        return ret;
    }

    /**
     * Check if this document contains a particular variable id.
     *
     * @param variableId
     * @return boolean
     */
    public boolean containsVariable(String variableId)
    {
        boolean ret = false;

        ret = ovalVariableMap.containsKey(variableId);

        return ret;
    }

    /**
     * Get the xml namespace of this document.
     *
     * @return Namespace
     */
    public Namespace getNamespace()
    {
        return getElement().getNamespace();
    }

    /**
     * This method should be overridden by version specific subclasses of this one.
     * It returns an array of supported variable functions, such as concat for the
     * version of oval represented by this document.
     *
     * @return OvalFunctionEnum[]
     */
    public abstract OvalFunctionEnum[] getValidFunctions();

    /**
     * This method should be overriden by version specific subclasses of this one.
     * Returns an array of supported check enumeration values for that version of oval.
     * 
     * @return CheckEnumeration[]
     */
    public abstract CheckEnumeration[] getSupportedCheckEnumerations();

    /**
     * This method should be overriden by version specific subclasses of this one.
     * Returns an array of supported existence enumeration values for that version of oval.
     * 
     * @return ExistenceEnumeration[]
     */
    public abstract ExistenceEnumeration[] getSupportedExistenceEnumerations();

    /**
     * Return a list of tests, objects, states, and variables that are not referenced
     * anywhere within this document.
     * 
     * @return List<OvalElement>
     */
    public List<OvalElement> findOrphans()
    {
        List<OvalElement> orphans = null;

        HashMap<String, OvalElement> ovalElements = new HashMap<String, OvalElement>();

        // not loading definitions here, this would be part of a bundle level
        // orphan checking routine

        List<OvalTest> tests = getOvalTests();
        if (tests != null && tests.size() > 0)
        {
            for (int x = 0; x < tests.size(); x++)
            {
                OvalTest ot = tests.get(x);
                ovalElements.put(ot.getId(), ot);
            }
        }

        List<OvalObject> objects = getOvalObjects();
        if (objects != null && objects.size() > 0)
        {
            for (int x = 0; x < objects.size(); x++)
            {
                OvalObject oo = objects.get(x);
                ovalElements.put(oo.getId(), oo);
            }
        }

        List<OvalState> states = getOvalStates();
        if (states != null && states.size() > 0)
        {
            for (int x = 0; x < states.size(); x++)
            {
                OvalState os = states.get(x);
                ovalElements.put(os.getId(), os);
            }
        }

        List<OvalVariable> variables = getOvalVariables();
        if (variables != null && variables.size() > 0)
        {
            for (int x = 0; x < variables.size(); x++)
            {
                OvalVariable ov = variables.get(x);
                ovalElements.put(ov.getId(), ov);
            }
        }

        //
        // now find references and remove referenced entities from the ovalElements map
        //
        
        // variables
        if (variables != null && variables.size() > 0)
        {
            for (int x = 0; x < variables.size(); x++)
            {
                OvalVariable ov = variables.get(x);
                
                List<OvalVariableChild> varChildren = ov.getVarChildren();
                for (OvalVariableChild varChild : varChildren) {
                	
                	processVariableChild(varChild, ovalElements, "\t");
                }

//                DefaultMutableTreeNode children = ov.getChildren();
//
//                if (children != null)
//                {
//                    Enumeration<DefaultMutableTreeNode> chEnum = children.depthFirstEnumeration();
//
//                    while (chEnum.hasMoreElements())
//                    {
//                        DefaultMutableTreeNode child = chEnum.nextElement();
//                        Object userObj = child.getUserObject();
//
//                        if (userObj instanceof OvalVariableComponentObject)
//                        {
//                            OvalVariableComponentObject objComponent = (OvalVariableComponentObject) userObj;
//
//                            String objId = objComponent.getObjectId();
//
//                            if (objId != null)
//                            {
//                                if (ovalElements.containsKey(objId))
//                                {
//                                    ovalElements.remove(objId);
//                                }
//                            }
//                        } else if (userObj instanceof OvalVariableComponentVariable)
//                        {
//                            OvalVariableComponentVariable varComponent = (OvalVariableComponentVariable) userObj;
//
//                            String varId = varComponent.getVariableId();
//
//                            if (varId != null)
//                            {
//                                if (ovalElements.containsKey(varId))
//                                {
//                                    ovalElements.remove(varId);
//                                }
//                            }
//                        }
//                    }
//                }
            }
        }

        // states

        for (OvalState os : states)
        {
            List<OvalStateParameter> parms = os.getParameters();
            for (OvalStateParameter osp : parms)
            {
                String varRef = osp.getVarRef();
                if (varRef != null)
                {
                    if (ovalElements.containsKey(varRef))
                    {
                        ovalElements.remove(varRef);
                    }
                }
                
                if (osp.getElementName().equals("result")) {
                	List<OvalStateParameter> fieldParms = osp.getFieldParameters();
                	for (OvalStateParameter fieldOsp : fieldParms) {
                		varRef = fieldOsp.getVarRef();
                		if (varRef != null) {
                			ovalElements.remove(varRef);
                		}
                	}
                }
            }

        }

        // objects
        if (objects != null && objects.size() > 0)
        {
            for (int x = 0; x < objects.size(); x++)
            {
                OvalObject oo = objects.get(x);
                List<OvalObjectChild> children = oo.getChildren();
                for (OvalObjectChild child : children)
                {
                    processObjectChildren(child, ovalElements);
                }
//    			DefaultMutableTreeNode children = oo.getChildTreeNodes();
//    			if(children != null)
//    			{
//    				
//    				Enumeration<DefaultMutableTreeNode> chEnum = children.depthFirstEnumeration();
//    				
//    				while(chEnum.hasMoreElements())
//    				{
//    					DefaultMutableTreeNode child = chEnum.nextElement();
//    					
//    					Object userObj = child.getUserObject();
//    					
//    					if(userObj instanceof OvalObjectParameter)
//    					{
//    						OvalObjectParameter oop = (OvalObjectParameter) userObj;
//    						
//    						String varRef = oop.getVarRef();
//    						
//    						if(varRef != null)
//    						{
//    							if(ovalElements.containsKey(varRef))
//    							{
//    								ovalElements.remove(varRef);
//    							}
//    						}
//    					}
//    					else if(userObj instanceof OvalObjectReference)
//    					{
//    						OvalObjectReference oor = (OvalObjectReference) userObj;
//    						
//    						String objId = oor.getObjectId();
//    						
//    						if(objId != null)
//    						{
//    							if(ovalElements.containsKey(objId))
//    							{
//    								ovalElements.remove(objId);
//    							}
//    						}
//    					}
//    					else if(userObj instanceof OvalObjectFilter)
//    					{
//    						OvalObjectFilter oof = (OvalObjectFilter) userObj;
//    						
//    						String stateId = oof.getStateId();
//    						
//    						if(stateId != null)
//    						{
//    							if(ovalElements.containsKey(stateId))
//    							{
//    								ovalElements.remove(stateId);
//    							}
//    						}
//    					}
//    				}
//    			}    			
            }
        }

        // tests
        if (tests != null && tests.size() > 0)
        {
            for (int x = 0; x < tests.size(); x++)
            {
                OvalTest ot = tests.get(x);

                String objId = ot.getObjectId();

                if (objId != null)
                {
                    if (ovalElements.containsKey(objId))
                    {
                        ovalElements.remove(objId);
                    }
                }

                String stateId = ot.getStateId();

                if (stateId != null)
                {
                    if (ovalElements.containsKey(stateId))
                    {
                        ovalElements.remove(stateId);
                    }
                }
            }
        }

        List<OvalDefinition> defs = getOvalDefinitions();
        if (defs != null && defs.size() > 0)
        {
            for (int x = 0; x < defs.size(); x++)
            {
                OvalDefinition od = defs.get(x);
                Criteria criteria = od.getCriteria();
                removeFoundElements(criteria, ovalElements);
            }
        }

        // now we should have a map with only ovalElements. that are not referenced by any definitions.

        if (ovalElements.size() > 0)
        {
            orphans = new ArrayList<OvalElement>();
            orphans.addAll(ovalElements.values());
        }

        return orphans;
    }
    
    private void logStats(Map<String, OvalElement> map) {
    	int tests = 0;
    	int objects = 0; 
    	int states = 0;
    	int vars = 0;
    	Set<String> keys = map.keySet();
    	for (String key : keys) {
    		if (key.indexOf(":tst:") != -1) {
    			tests++;
    		} else if (key.indexOf(":obj:") != -1) {
    			objects++;
    		} else if (key.indexOf(":ste:") != -1) {
    			states++;
    		} else if (key.indexOf(":var:") != -1) {
    			vars++;
    		} 
    	}
    	log.debug("Unused tests: " + tests);
    	log.debug("Unused objects: " + objects);
    	log.debug("Unused states: " + states);
    	log.debug("Unused vars: " + vars);
    }
    
    private void processVariableChild(OvalVariableChild variable, HashMap<String, OvalElement> ovalElements, String indent) {    
   		if (variable instanceof OvalVariableContent) {
			OvalVariableContent varContent = (OvalVariableContent) variable;
			String id = null;
			if (varContent instanceof OvalLocalVariable) {
				id = ((OvalLocalVariable) varContent).getId();	
				
			} else if (varContent instanceof OvalConstantVariable) {
				id = ((OvalConstantVariable) varContent).getId();
				
			} else if (varContent instanceof OvalExternalVariable) {
				id = ((OvalExternalVariable) varContent).getId();
				
			} else if (varContent instanceof OvalVariableComponentObject) {
				id = ((OvalVariableComponentObject) varContent).getObjectId();
				
			} else if (varContent instanceof OvalVariableComponentVariable) {
				id = ((OvalVariableComponentVariable) varContent).getVariableId();
				
			} else if (varContent instanceof OvalFunction 
					|| varContent instanceof OvalVariableComponentLiteral
					|| varContent instanceof ConstantVariableValue) {
				
			} else {
				// should not occur
				throw new IllegalStateException("Invalid variable content: " + variable.getElementName());
			}
			if (id != null) {
				ovalElements.remove(id);
			}
	    	List<OvalVariableChild> varChildren = variable.getVarChildren();
	    	for (OvalVariableChild varChild : varChildren) {
	 
	    			processVariableChild(varChild, ovalElements, indent + "\t"); // OvalVariableComponentVariable
	    	}
    	}
    }

    private void processObjectChildren(OvalObjectChild objectChild, HashMap<String, OvalElement> ovalElements)
    {
        if (objectChild instanceof OvalObjectParameter)
        {
            OvalObjectParameter oop = (OvalObjectParameter) objectChild;
            String varRef = oop.getVarRef();
            if (varRef == null) {
            	// no var_ref attribute, but the whole element could be a var_ref:
                if (oop.getElementName().equals("var_ref")) {
                	// this var_ref is a child of variable_object
                	varRef = oop.getValue();
                }
            }
            if (varRef != null)
            {
                if (ovalElements.containsKey(varRef))
                {
                    ovalElements.remove(varRef);
                }
            }

        } else if (objectChild instanceof OvalObjectReference)
        {
            OvalObjectReference oor = (OvalObjectReference) objectChild;
            String objId = oor.getObjectId();
            if (objId != null)
            {
                if (ovalElements.containsKey(objId))
                {
                    ovalElements.remove(objId);
                }
            }
        } else if (objectChild instanceof OvalObjectFilter)
        {
            OvalObjectFilter oof = (OvalObjectFilter) objectChild;
            String stateId = oof.getStateId();
            if (stateId != null)
            {
                if (ovalElements.containsKey(stateId))
                {
                    ovalElements.remove(stateId);
                }
            }
        }
        List<OvalObjectChild> grandChildren = objectChild.getChildren();
        for (OvalObjectChild grandChild : grandChildren)
        {
            processObjectChildren(grandChild, ovalElements);
        }
    }

    private void removeFoundElements(CriteriaChild criteriaChild, HashMap<String, OvalElement> ovalElements)
    {
        if (criteriaChild != null)
        {
            List<CriteriaChild> children = criteriaChild.getChildren();
            for (CriteriaChild child : children)
            {
                if (child instanceof Criterion)
                {
                    Criterion criterion = (Criterion) child;
                    OvalTest test = criterion.getTest();
                    if (test != null)
                    {
                        String testId = test.getId();
                        if (testId != null)
                        {
                            ovalElements.remove(testId);
                        }
                    }
                } else if (child instanceof ExtendDefinition)
                {
                    ExtendDefinition extDef = (ExtendDefinition) child;
                    OvalDefinition ovalDef = extDef.getOvalDefinition();
                    if (ovalDef != null)
                    {
                        ovalElements.remove(ovalDef.getId());
                    }
                } else if (child instanceof Criteria)
                {
                    removeFoundElements(child, ovalElements);
                }
            }
        }
    }

    /**
     * Called to recursively find the entities a given oval element is dependent on. This is useful if you want
     * to copy and entity from one document to another as this method will return all all objects that you need
     * to copy to the new document.
     * 
     * @param idsAdded
     * @param dependents
     * @param oe
     */
    private void findDependenciesRecurser(Set<String> idsAdded, List<OvalElement> dependents, OvalElement oe)
    {
        if (oe instanceof OvalDefinition)
        {
            OvalDefinition suppliedDef = (OvalDefinition) oe;

            // add this definition to dependents list
            if (!idsAdded.contains(suppliedDef.getId()))
            {
                idsAdded.add(suppliedDef.getId());
                dependents.add(suppliedDef);
            }

            // definitions can reference other definitions.
            List<OvalDefinition> referencedDefs = suppliedDef.getReferencedDefinitions();

            if (referencedDefs != null && referencedDefs.size() > 0)
            {
                for (int y = 0; y < referencedDefs.size(); y++)
                {
                    OvalDefinition rd = referencedDefs.get(y);

                    findDependenciesRecurser(idsAdded, dependents, rd);
                }
            }

            // definitions can reference tests
            List<OvalTest> referencedTests = suppliedDef.getReferencedTests();
            if (referencedTests != null && referencedTests.size() > 0)
            {
                for (int y = 0; y < referencedTests.size(); y++)
                {
                    OvalTest rt = referencedTests.get(y);

                    findDependenciesRecurser(idsAdded, dependents, rt);
                }
            }
        } else if (oe instanceof OvalTest)
        {
            OvalTest suppliedTest = (OvalTest) oe;

            // add this test to dependents list
            if (!idsAdded.contains(suppliedTest.getId()))
            {
                idsAdded.add(suppliedTest.getId());
                dependents.add(suppliedTest);
            }

            // tests can reference objects
            List<OvalObject> referencedObjects = suppliedTest.getReferencedObjects();

            if (referencedObjects != null && referencedObjects.size() > 0)
            {
                for (int y = 0; y < referencedObjects.size(); y++)
                {
                    OvalObject ro = referencedObjects.get(y);

                    findDependenciesRecurser(idsAdded, dependents, ro);
                }
            }

            // tests can reference states
            List<OvalState> referencedStates = suppliedTest.getReferencedStates();

            if (referencedStates != null && referencedStates.size() > 0)
            {
                for (int y = 0; y < referencedStates.size(); y++)
                {
                    OvalState rs = referencedStates.get(y);

                    findDependenciesRecurser(idsAdded, dependents, rs);
                }
            }
        } else if (oe instanceof OvalObject)
        {
            OvalObject suppliedObject = (OvalObject) oe;

            // add this object to dependents list
            if (!idsAdded.contains(suppliedObject.getId()))
            {
                idsAdded.add(suppliedObject.getId());
                dependents.add(suppliedObject);
            }

            // objects can reference objects
            List<OvalObject> referencedObjects = suppliedObject.getReferencedObjects();

            if (referencedObjects != null && referencedObjects.size() > 0)
            {
                for (int y = 0; y < referencedObjects.size(); y++)
                {
                    OvalObject ro = referencedObjects.get(y);

                    findDependenciesRecurser(idsAdded, dependents, ro);
                }
            }

            // objects can reference states
            List<OvalState> referencedStates = suppliedObject.getReferencedStates();

            if (referencedStates != null && referencedStates.size() > 0)
            {
                for (int y = 0; y < referencedStates.size(); y++)
                {
                    OvalState rs = referencedStates.get(y);

                    findDependenciesRecurser(idsAdded, dependents, rs);
                }
            }

            // objects can reference variables
            List<OvalVariable> referencedVariables = suppliedObject.getReferencedVariables();

            if (referencedVariables != null && referencedVariables.size() > 0)
            {
                for (int y = 0; y < referencedVariables.size(); y++)
                {
                    OvalVariable rv = referencedVariables.get(y);

                    findDependenciesRecurser(idsAdded, dependents, rv);
                }
            }
        } else if (oe instanceof OvalState)
        {
            OvalState suppliedState = (OvalState) oe;

            // add this state to dependents list
            if (!idsAdded.contains(suppliedState.getId()))
            {
                idsAdded.add(suppliedState.getId());
                dependents.add(suppliedState);
            }

            // states can reference variables
            List<OvalVariable> referencedVariables = suppliedState.getReferencedVariables();

            if (referencedVariables != null && referencedVariables.size() > 0)
            {
                for (int y = 0; y < referencedVariables.size(); y++)
                {
                    OvalVariable rv = referencedVariables.get(y);

                    findDependenciesRecurser(idsAdded, dependents, rv);
                }
            }

        } else if (oe instanceof OvalVariable)
        {
            OvalVariable suppliedVariable = (OvalVariable) oe;

            // add this variable to dependents list
            if (!idsAdded.contains(suppliedVariable.getId()))
            {
                idsAdded.add(suppliedVariable.getId());
                dependents.add(suppliedVariable);
            }

            // variables can reference objects
            List<OvalObject> referencedObjects = suppliedVariable.getReferencedObjects();

            if (referencedObjects != null && referencedObjects.size() > 0)
            {
                for (int y = 0; y < referencedObjects.size(); y++)
                {
                    OvalObject ro = referencedObjects.get(y);

                    findDependenciesRecurser(idsAdded, dependents, ro);
                }
            }

            // variables can reference variables
            List<OvalVariable> referencedVariables = suppliedVariable.getReferencedVariables();

            if (referencedVariables != null && referencedVariables.size() > 0)
            {
                for (int y = 0; y < referencedVariables.size(); y++)
                {
                    OvalVariable rv = referencedVariables.get(y);

                    findDependenciesRecurser(idsAdded, dependents, rv);
                }
            }
        }
    }

    /**
     * Called to recursively find the entities the given oval element is dependent on. This is useful if you want
     * to copy and entity from one document to another as this method will return all all objects that you need
     * to copy to the new document.
     *
     * An oval element can be a definition, test, object, state, or variable.
     * 
     * @param oe
     * @return List<OvalElement>
     */
    public List<OvalElement> findDependenciesFor(OvalElement oe)
    {
        List<OvalElement> dependencies = new ArrayList<OvalElement>();
        Set<String> ids = new HashSet<String>();

        findDependenciesRecurser(ids, dependencies, oe);

        if (oe instanceof OvalTest)
        {
            OvalTest suppliedTest = (OvalTest) oe;

            // tests can be referenced by
            // definitions, so we'll only loop through definitions
            // looking for references to the test that was supplied
            List<OvalDefinition> allDefs = getOvalDefinitions();
            if (allDefs != null && allDefs.size() > 0)
            {
                for (int x = 0; x < allDefs.size(); x++)
                {
                    OvalDefinition od = allDefs.get(x);

                    List<OvalTest> referencedTests = od.getReferencedTests();

                    if (referencedTests != null && referencedTests.size() > 0)
                    {
                        for (int y = 0; y < referencedTests.size(); y++)
                        {
                            OvalTest rt = referencedTests.get(y);

                            if (rt.getId().equals(suppliedTest.getId()))
                            {
                                // suppliedTest is referenced by od
                                dependencies.add(od);
                                break;
                            }
                        }
                    }
                }
            }

        } else if (oe instanceof OvalObject)
        {
            OvalObject suppliedObject = (OvalObject) oe;

            // first, objects can be referenced by tests
            List<OvalTest> allTests = getOvalTests();
            if (allTests != null && allTests.size() > 0)
            {
                for (int x = 0; x < allTests.size(); x++)
                {
                    OvalTest ot = allTests.get(x);

                    String objectId = ot.getObjectId();

                    if (suppliedObject.getId().equals(objectId))
                    {
                        // this test references the suppliedObject
                        dependencies.add(ot);
                    }
                }
            }

            // objects can also be referenced by other objects
            List<OvalObject> allObjects = getOvalObjects();
            if (allObjects != null && allObjects.size() > 0)
            {
                for (int x = 0; x < allObjects.size(); x++)
                {
                    OvalObject oo = allObjects.get(x);
                    if (oo.getId().equals(suppliedObject.getId()))
                    {
                        // same object as the one supplied, skip it
                        continue;
                    }

                    List<OvalObject> referencedObjects = oo.getReferencedObjects();

                    if (referencedObjects != null && referencedObjects.size() > 0)
                    {
                        for (int y = 0; y < referencedObjects.size(); y++)
                        {
                            OvalObject ro = referencedObjects.get(y);

                            if (ro.getId().equals(suppliedObject.getId()))
                            {
                                // oo references suppliedObject
                                dependencies.add(oo);
                                break;
                            }
                        }
                    }
                }
            }

            // objects can be referenced by variables
            List<OvalVariable> allVariables = getOvalVariables();
            if (allVariables != null && allVariables.size() > 0)
            {
                for (int x = 0; x < allVariables.size(); x++)
                {
                    OvalVariable ov = allVariables.get(x);

                    List<OvalObject> referencedObjects = ov.getReferencedObjects();
                    if (referencedObjects != null && referencedObjects.size() > 0)
                    {
                        for (int y = 0; y < referencedObjects.size(); y++)
                        {
                            OvalObject ro = referencedObjects.get(y);

                            if (ro.getId().equals(suppliedObject.getId()))
                            {
                                // ov references suppliedObject
                                dependencies.add(ov);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (oe instanceof OvalState)
        {
            OvalState suppliedState = (OvalState) oe;

            // first, states can be referenced by tests
            List<OvalTest> allTests = getOvalTests();
            if (allTests != null && allTests.size() > 0)
            {
                for (int x = 0; x < allTests.size(); x++)
                {
                    OvalTest ot = allTests.get(x);

                    String stateId = ot.getStateId();

                    if (stateId == null || stateId.length() == 0)
                    {
                        continue;
                    }

                    if (suppliedState.getId().equals(stateId))
                    {
                        // this test references the suppliedObject
                        dependencies.add(ot);
                    }
                }
            }

            // states can also be referenced by objects as filters
            List<OvalObject> allObjects = getOvalObjects();
            if (allObjects != null && allObjects.size() > 0)
            {
                for (int x = 0; x < allObjects.size(); x++)
                {
                    OvalObject oo = allObjects.get(x);

                    List<OvalState> referencedStates = oo.getReferencedStates();

                    if (referencedStates != null && referencedStates.size() > 0)
                    {
                        for (int y = 0; y < referencedStates.size(); y++)
                        {
                            OvalState rs = referencedStates.get(y);

                            if (rs.getId().equals(suppliedState.getId()))
                            {
                                // oo references suppliedState
                                dependencies.add(oo);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (oe instanceof OvalVariable)
        {
            OvalVariable suppliedVariable = (OvalVariable) oe;

            // variables can be referenced by objects
            List<OvalObject> allObjects = getOvalObjects();
            if (allObjects != null && allObjects.size() > 0)
            {
                for (int x = 0; x < allObjects.size(); x++)
                {
                    OvalObject oo = allObjects.get(x);

                    List<OvalVariable> referencedVariables = oo.getReferencedVariables();

                    if (referencedVariables != null && referencedVariables.size() > 0)
                    {
                        for (int y = 0; y < referencedVariables.size(); y++)
                        {
                            OvalVariable rv = referencedVariables.get(y);

                            if (rv.getId().equals(suppliedVariable.getId()))
                            {
                                // oo references suppliedVariable
                                dependencies.add(oo);
                                break;
                            }
                        }
                    }
                }
            }

            // variables can be referenced by states
            List<OvalState> allStates = getOvalStates();
            if (allStates != null && allStates.size() > 0)
            {
                for (int x = 0; x < allStates.size(); x++)
                {
                    OvalState os = allStates.get(x);

                    List<OvalVariable> referencedVariables = os.getReferencedVariables();

                    if (referencedVariables != null && referencedVariables.size() > 0)
                    {
                        for (int y = 0; y < referencedVariables.size(); y++)
                        {
                            OvalVariable rv = referencedVariables.get(y);

                            if (rv.getId().equals(suppliedVariable.getId()))
                            {
                                // os references suppliedVariable
                                dependencies.add(os);
                                break;
                            }
                        }
                    }
                }
            }

            // variables can be referenced by other variables
            List<OvalVariable> allVariables = getOvalVariables();
            if (allVariables != null && allVariables.size() > 0)
            {
                for (int x = 0; x < allVariables.size(); x++)
                {
                    OvalVariable ov = allVariables.get(x);

                    if (ov.getId().equals(suppliedVariable.getId()))
                    {
                        // same as supplied variable
                        continue;
                    }

                    List<OvalVariable> referencedVariables = ov.getReferencedVariables();

                    if (referencedVariables != null && referencedVariables.size() > 0)
                    {
                        for (int y = 0; y < referencedVariables.size(); y++)
                        {
                            OvalVariable rv = referencedVariables.get(y);

                            if (rv.getId().equals(suppliedVariable.getId()))
                            {
                                // ov references suppliedVariable
                                dependencies.add(ov);
                                break;
                            }
                        }
                    }
                }
            }
        }

        return dependencies;
    }

    /**
     * Return a list of oval elements dependent on the one supplied.
     * 
     * An oval element can be a definition, test, object, state, or variable.
     * 
     * @param oe
     * @return List<OvalElement>
     */
    public List<OvalElement> findEntitiesDependentOn(OvalElement oe)
    {
        List<OvalElement> dependencies = new ArrayList<OvalElement>();

        if (oe instanceof OvalDefinition)
        {
            OvalDefinition suppliedDef = (OvalDefinition) oe;

            // definitions can only be directly referenced by
            // other definitions, so we'll only loop through definitions
            // looking for references to the one supplied
            List<OvalDefinition> allDefs = getOvalDefinitions();
            if (allDefs != null && allDefs.size() > 0)
            {
                for (int x = 0; x < allDefs.size(); x++)
                {
                    OvalDefinition od = allDefs.get(x);

                    if (od.getId().equals(suppliedDef.getId()))
                    {
                        // this is the same one, skip it
                        continue;
                    }

                    List<OvalDefinition> referencedDefs = od.getReferencedDefinitions();

                    if (referencedDefs != null && referencedDefs.size() > 0)
                    {
                        for (int y = 0; y < referencedDefs.size(); y++)
                        {
                            OvalDefinition rd = referencedDefs.get(y);

                            if (rd.getId().equals(suppliedDef.getId()))
                            {
                                // suppliedDef is referenced by od
                                dependencies.add(od);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (oe instanceof OvalTest)
        {
            OvalTest suppliedTest = (OvalTest) oe;

            // tests can be referenced by
            // definitions, so we'll only loop through definitions
            // looking for references to the test that was supplied
            List<OvalDefinition> allDefs = getOvalDefinitions();
            if (allDefs != null && allDefs.size() > 0)
            {
                for (int x = 0; x < allDefs.size(); x++)
                {
                    OvalDefinition od = allDefs.get(x);

                    List<OvalTest> referencedTests = od.getReferencedTests();

                    if (referencedTests != null && referencedTests.size() > 0)
                    {
                        for (int y = 0; y < referencedTests.size(); y++)
                        {
                            OvalTest rt = referencedTests.get(y);

                            if (rt.getId().equals(suppliedTest.getId()))
                            {
                                // suppliedTest is referenced by od
                                dependencies.add(od);
                                break;
                            }
                        }
                    }
                }
            }

        } else if (oe instanceof OvalObject)
        {
            OvalObject suppliedObject = (OvalObject) oe;

            // first, objects can be referenced by tests
            List<OvalTest> allTests = getOvalTests();
            if (allTests != null && allTests.size() > 0)
            {
                for (int x = 0; x < allTests.size(); x++)
                {
                    OvalTest ot = allTests.get(x);

                    String objectId = ot.getObjectId();

                    if (suppliedObject.getId().equals(objectId))
                    {
                        // this test references the suppliedObject
                        dependencies.add(ot);
                    }
                }
            }

            // objects can also be referenced by other objects
            List<OvalObject> allObjects = getOvalObjects();
            if (allObjects != null && allObjects.size() > 0)
            {
                for (int x = 0; x < allObjects.size(); x++)
                {
                    OvalObject oo = allObjects.get(x);
                    if (oo.getId().equals(suppliedObject.getId()))
                    {
                        // same object as the one supplied, skip it
                        continue;
                    }

                    List<OvalObject> referencedObjects = oo.getReferencedObjects();

                    if (referencedObjects != null && referencedObjects.size() > 0)
                    {
                        for (int y = 0; y < referencedObjects.size(); y++)
                        {
                            OvalObject ro = referencedObjects.get(y);

                            if (ro.getId().equals(suppliedObject.getId()))
                            {
                                // oo references suppliedObject
                                dependencies.add(oo);
                                break;
                            }
                        }
                    }
                }
            }

            // objects can be referenced by variables
            List<OvalVariable> allVariables = getOvalVariables();
            if (allVariables != null && allVariables.size() > 0)
            {
                for (int x = 0; x < allVariables.size(); x++)
                {
                    OvalVariable ov = allVariables.get(x);

                    List<OvalObject> referencedObjects = ov.getReferencedObjects();
                    if (referencedObjects != null && referencedObjects.size() > 0)
                    {
                        for (int y = 0; y < referencedObjects.size(); y++)
                        {
                            OvalObject ro = referencedObjects.get(y);

                            if (ro.getId().equals(suppliedObject.getId()))
                            {
                                // ov references suppliedObject
                                dependencies.add(ov);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (oe instanceof OvalState)
        {
            OvalState suppliedState = (OvalState) oe;

            // first, states can be referenced by tests
            List<OvalTest> allTests = getOvalTests();
            if (allTests != null && allTests.size() > 0)
            {
                for (int x = 0; x < allTests.size(); x++)
                {
                    OvalTest ot = allTests.get(x);

                    List stateElements = ot.getElement().getChildren("state", ot.getElement().getNamespace());
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

                                if(refAttrib != null && refAttrib.equals(suppliedState.getId()))
                                {
                                    // this test references the suppliedObject
                                    dependencies.add(ot);
                                    break;
                                }
                            }
                        } // end of state reference for loop
                    }
                }
            }

            // states can also be referenced by objects as filters
            List<OvalObject> allObjects = getOvalObjects();
            if (allObjects != null && allObjects.size() > 0)
            {
                for (int x = 0; x < allObjects.size(); x++)
                {
                    OvalObject oo = allObjects.get(x);

                    List<OvalState> referencedStates = oo.getReferencedStates();

                    if (referencedStates != null && referencedStates.size() > 0)
                    {
                        for (int y = 0; y < referencedStates.size(); y++)
                        {
                            OvalState rs = referencedStates.get(y);

                            if (rs.getId().equals(suppliedState.getId()))
                            {
                                // oo references suppliedState
                                dependencies.add(oo);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (oe instanceof OvalVariable)
        {
            OvalVariable suppliedVariable = (OvalVariable) oe;

            // variables can be referenced by objects
            List<OvalObject> allObjects = getOvalObjects();
            if (allObjects != null && allObjects.size() > 0)
            {
                for (int x = 0; x < allObjects.size(); x++)
                {
                    OvalObject oo = allObjects.get(x);

                    List<OvalVariable> referencedVariables = oo.getReferencedVariables();

                    if (referencedVariables != null && referencedVariables.size() > 0)
                    {
                        for (int y = 0; y < referencedVariables.size(); y++)
                        {
                            OvalVariable rv = referencedVariables.get(y);

                            if (rv.getId().equals(suppliedVariable.getId()))
                            {
                                // oo references suppliedVariable
                                dependencies.add(oo);
                                break;
                            }
                        }
                    }
                }
            }

            // variables can be referenced by states
            List<OvalState> allStates = getOvalStates();
            if (allStates != null && allStates.size() > 0)
            {
                for (int x = 0; x < allStates.size(); x++)
                {
                    OvalState os = allStates.get(x);

                    List<OvalVariable> referencedVariables = os.getReferencedVariables();

                    if (referencedVariables != null && referencedVariables.size() > 0)
                    {
                        for (int y = 0; y < referencedVariables.size(); y++)
                        {
                            OvalVariable rv = referencedVariables.get(y);

                            if (rv.getId().equals(suppliedVariable.getId()))
                            {
                                // os references suppliedVariable
                                dependencies.add(os);
                                break;
                            }
                        }
                    }
                }
            }

            // variables can be referenced by other variables
            List<OvalVariable> allVariables = getOvalVariables();
            if (allVariables != null && allVariables.size() > 0)
            {
                for (int x = 0; x < allVariables.size(); x++)
                {
                    OvalVariable ov = allVariables.get(x);

                    if (ov.getId().equals(suppliedVariable.getId()))
                    {
                        // same as supplied variable
                        continue;
                    }

                    List<OvalVariable> referencedVariables = ov.getReferencedVariables();

                    if (referencedVariables != null && referencedVariables.size() > 0)
                    {
                        for (int y = 0; y < referencedVariables.size(); y++)
                        {
                            OvalVariable rv = referencedVariables.get(y);

                            if (rv.getId().equals(suppliedVariable.getId()))
                            {
                                // ov references suppliedVariable
                                dependencies.add(ov);
                                break;
                            }
                        }
                    }
                }
            }
        }

        return dependencies;
    }
    
    /**
     * Return a list of all oval definitions contained in this document.
     *
     * @return List<OvalDefinition>
     */
    public List<OvalDefinition> getOvalDefinitions() {
    	return getMatchingOvalElements(ovalDefinitionMap, null);
    }
    
    /**
     * Return a list of all oval tests contained in this document.
     *
     * @return List<OvalTest>
     */
    public List<OvalTest> getOvalTests() {
    	return getMatchingOvalElements(ovalTestMap, null);
    }
    
    /**
     * Return a list of all oval objects contained in this document.
     *
     * @return List<OvalObject>
     */
    public List<OvalObject> getOvalObjects() {
    	return getMatchingOvalElements(ovalObjectMap, null);
    }
    
    /**
     * Return a list of all oval states contained in this document.
     *
     * @return List<OvalState>
     */
    public List<OvalState> getOvalStates() {
    	return getMatchingOvalElements(ovalStateMap, null);
    }
    
    /**
     * Return a list of all oval variables contained in this document.
     *
     * @return List<OvalVariable>
     */
    public List<OvalVariable> getOvalVariables() {
    	return getMatchingOvalElements(ovalVariableMap, null);
    }
    
    /**
     * Return a list of all oval definitions matching a search string.
     *
     * @return List<OvalDefinition>
     */
    public List<OvalDefinition> getMatchingOvalDefinitions(String matchString) {
    	return getMatchingOvalElements(ovalDefinitionMap, matchString);
    }
    
    /**
     * Return a list of all oval tests matching a search string.
     *
     * @return List<OvalTest>
     */
    public List<OvalTest> getMatchingOvalTests(String matchString) {
    	return getMatchingOvalElements(ovalTestMap, matchString);
    }
    
    /**
     * Return a list of all oval opbjects matching a search string.
     *
     * @return List<OvalObject>
     */
    public List<OvalObject> getMatchingOvalObjects(String matchString) {
    	return getMatchingOvalElements(ovalObjectMap, matchString);
    }
    
    /**
     * Return a list of all oval states matching a search string.
     *
     * @return List<OvalState>
     */
    public List<OvalState> getMatchingOvalStates(String matchString) {
    	return getMatchingOvalElements(ovalStateMap, matchString);
    }
    
    /**
     * Return a list of all oval variables matching a search string.
     *
     * @return List<OvalVariable>
     */
    public List<OvalVariable> getMatchingOvalVariables(String matchString) {
    	return getMatchingOvalElements(ovalVariableMap, matchString);
    }
    
	private <T> List<T> getMatchingOvalElements(Map<String, OvalElement> map, String matchString) {
    	if (matchString != null) {
    		matchString = matchString.trim();
    		if (matchString.length() == 0) {
    			matchString = null;
    		}
    	} 
    	List<T> list = new ArrayList<T>();

    	Iterator<OvalElement> iter = map.values().iterator();
    	while (iter.hasNext()) {
    		VersionedOvalElement versionedElement =  (VersionedOvalElement) iter.next();
    		if (matchString == null || versionedElement.matches(matchString)) {
    			@SuppressWarnings("unchecked")
				T matchingElement = (T) versionedElement;
    			list.add(matchingElement);
    		}
    	}
    	return list;
    }


    /**
     * This logic was originally in getOvalTests().  It is moved to a separate method 
     * to avoid recreating OvalTests from JDOM objects when getOvalTests is called.
     */
    public void refreshTestMap() {
    	Namespace ns = getElement().getNamespace();
    	Element testsElement = getElement().getChild("tests", ns);
    	ovalTestMap.clear();
    	if (testsElement != null) {
    		@SuppressWarnings("unchecked")
			List<Element> children = testsElement.getChildren();
			for (Element elem : children) {			
				OvalTest ot = this.getTestWrapper();
				ot.setElement(elem);
				ovalTestMap.put(ot.getId(), ot);
			}
    	}
    }
    public void refreshDefinitionMap() {
    	Namespace ns = getElement().getNamespace();
    	Element collectionElement = getElement().getChild("definitions", ns);
    	ovalDefinitionMap.clear();
    	if (collectionElement != null) {
    		@SuppressWarnings("unchecked")
			List<Element> children = collectionElement.getChildren();
			for (Element elem : children) {			
				OvalDefinition ovalElem = this.getDefinitionWrapper();
				ovalElem.setElement(elem);
				ovalDefinitionMap.put(ovalElem.getId(), ovalElem);
			}
    	}
    }
    public void refreshObjectMap() {
    	Namespace ns = getElement().getNamespace();
    	Element collectionElement = getElement().getChild("objects", ns);
    	ovalObjectMap.clear();
    	if (collectionElement != null) {
    		@SuppressWarnings("unchecked")
			List<Element> children = collectionElement.getChildren();
			for (Element elem : children) {			
				OvalObject ovalElem = this.getObjectWrapper();
				ovalElem.setElement(elem);
				ovalObjectMap.put(ovalElem.getId(), ovalElem);
			}
    	}
    }
    public void refreshStateMap() {
    	Namespace ns = getElement().getNamespace();
    	Element collectionElement = getElement().getChild("states", ns);
    	ovalStateMap.clear();
    	if (collectionElement != null) {
    		@SuppressWarnings("unchecked")
			List<Element> children = collectionElement.getChildren();
			for (Element elem : children) {			
				OvalState ovalElem = this.getStateWrapper();
				ovalElem.setElement(elem);
				ovalStateMap.put(ovalElem.getId(), ovalElem);
			}
    	}
    }
    public void refreshVariableMap() {
    	Namespace ns = getElement().getNamespace();
    	Element collectionElement = getElement().getChild("variables", ns);
    	ovalVariableMap.clear();
    	if (collectionElement != null) {
    		@SuppressWarnings("unchecked")
			List<Element> children = collectionElement.getChildren();
			for (Element elem : children) {			
				OvalVariable ovalElem = this.getVariableWrapper(elem.getName());
				ovalElem.setElement(elem);
				ovalVariableMap.put(ovalElem.getId(), ovalElem);
			}
    	}
    }
    

    /**
     * Get a specific oval definition by id.
     *
     * @param id
     * @return OvalDefinition
     */
    public OvalDefinition getOvalDefinition(String id)
    {
        return (OvalDefinition) ovalDefinitionMap.get(id);
    }

    /**
     * Get a specific oval test by id.
     *
     * @param id
     * @return OvalTest
     */
    public OvalTest getOvalTest(String id)
    {
        return (OvalTest) ovalTestMap.get(id);
    }

    /**
     * Get a specific oval object by id.
     *
     * @param id
     * @return OvalObject
     */
    public OvalObject getOvalObject(String id)
    {
        return (OvalObject) ovalObjectMap.get(id);
    }

    /**
     * Get a specific oval state by id.
     *
     * @param id
     * @return OvalState
     */
    public OvalState getOvalState(String id)
    {
        return (OvalState) ovalStateMap.get(id);
    }

    /**
     * Get a specific oval variable by id.
     *
     * @param id
     * @return OvalVariable
     */
    public OvalVariable getOvalVariable(String id)
    {
        return (OvalVariable) ovalVariableMap.get(id);
    }

    /**
     * A convenience method to add some type of oval element to this document.
     *
     * @param ovalElement
     */
    public void add(OvalElement ovalElement)
    {
        add(ovalElement, 0);
    }

    /**
     * A convenience method to add some type of oval element to this document.
     *
     * @param ovalElement
     * @param index The index under the corresponding section(definitions, tests, etc)
     *              to add the new ovalElement
     */
    public void add(OvalElement ovalElement, int index)
    {
        if (ovalElement == null)
        {
            throw new IllegalStateException("A null oval element was passed in!");
        }

        if (ovalElement instanceof OvalDefinition)
        {
            if (!containsDefinition(ovalElement.getId()))
            {
                addDefinition((OvalDefinition) ovalElement, index);
            }
        } else if (ovalElement instanceof OvalTest)
        {
            if (!containsTest(ovalElement.getId()))
            {
                addTest((OvalTest) ovalElement, index);
            }
        } else if (ovalElement instanceof OvalObject)
        {
            if (!containsObject(ovalElement.getId()))
            {
                addObject((OvalObject) ovalElement, index);
            }
        } else if (ovalElement instanceof OvalState)
        {
            if (!containsState(ovalElement.getId()))
            {
                addState((OvalState) ovalElement, index);
            }
        } else if (ovalElement instanceof OvalVariable)
        {
            if (!containsVariable(ovalElement.getId()))
            {
                addVariable((OvalVariable) ovalElement, index);
            }
        } else
        {
            throw new IllegalStateException("The oval element passed in was not one that we know about!");
        }
    }

    /***
     * Make OVAL IDs in the OTHER document unique relative to THIS document.
     * Make sure the IDs in the OTHER document don't conflict with IDs in THIS
     * document. This is done in preparation for merging the OTHER documents 
     * contents into THIS document.
     *
     * @param otherDoc The other OvalDefinitionsDocument, whose names are to be made unique.
     */
    private void makeIDsUnique(OvalDefinitionsDocument otherDoc, MergeStats stats)
    {
        // Loop through all of the elements in the other document and
    	// determine if an ID change is required for each. An ID change 
        // is required if the element's ID matches an ID in this document 
        // and the element does not exactly match any element in this 
        // document. If the element is an exact match of an element in this
        // document, then it will be skipped during the actual merge process.
        Boolean idChanged = false;
		do
		{
	    	// Notice that we loop until idChanged = false. This is because if you 
	    	// change a state ID, that updates a test and then the test needs
	    	// to be rechecked.
			idChanged = false;
			
	        // Update definition IDs in the other document if required.
	        List<OvalDefinition> otherDefinitions = otherDoc.getOvalDefinitions();
	        for (OvalDefinition otherDefinition : otherDefinitions)
	        {
	        	Boolean idChangeRequired = false;
	            if (containsDefinition(otherDefinition.getId()))
    {
	            	Boolean duplicateElementFound = false;
	                List<OvalDefinition> myDefinitions = getOvalDefinitions();
	                for (OvalDefinition myDefinition : myDefinitions)
        {
	                	if (myDefinition.isDuplicateOf(otherDefinition))
            {
	                		duplicateElementFound = true;
	                		break;
            }
        }
	                if (duplicateElementFound == false)
        {
	                	idChangeRequired = true;
	                }
	            }
	            if (idChangeRequired == true)
            {
	            	idChanged = true;
		            String newId = getNextId(otherDefinition, this.getOvalDefinitionMap(), otherDoc.getOvalDefinitionMap());
		            File file = new File(otherDoc.getFilename());
		            String warning = file.getName() + " - ID " + otherDefinition.getId() + " changed to " + newId + ". The source documents have multiple elements with ID " + otherDefinition.getId() + " that are not identical.";
		            stats.getWarnings().add(warning);
		            log.info(warning);
		            otherDefinition.setId(newId);
            }
        }
	        
	        // Update test IDs in the other document if required.
	        List<OvalTest> otherTests = otherDoc.getOvalTests();
	        for (OvalTest otherTest : otherTests)
        {
	        	Boolean idChangeRequired = false;
	            if (containsTest(otherTest.getId()))
            {
	            	Boolean duplicateElementFound = false;
	                List<OvalTest> myTests = getOvalTests();
	                for (OvalTest myTest : myTests)
	                {
	                	if (myTest.isDuplicateOf(otherTest))
	                	{
	                		duplicateElementFound = true;
	                		break;
            }
        }
	                if (duplicateElementFound == false)
        {
	                	idChangeRequired = true;
	                }
	            }
	            if (idChangeRequired == true)
            {
	            	idChanged = true;
		            String newId = getNextId(otherTest, this.getOvalTestMap(), otherDoc.getOvalTestMap());
		            File file = new File(otherDoc.getFilename());
		            String warning = file.getName() + " - ID " + otherTest.getId() + " changed to " + newId + ". The source documents have multiple elements with ID " + otherTest.getId() + " that are not identical.";
		            stats.getWarnings().add(warning);
		            log.info(warning);
		            otherTest.setId(newId);
            }
        }
	        
	        // Update object IDs in the other document if required.
	        List<OvalObject> otherObjects = otherDoc.getOvalObjects();
	        for (OvalObject otherObject : otherObjects)
	        {
	        	Boolean idChangeRequired = false;
	            if (containsObject(otherObject.getId()))
	            {
	            	Boolean duplicateElementFound = false;
	                List<OvalObject> myObjects = getOvalObjects();
	                for (OvalObject myObject : myObjects)
        {
	                	if (myObject.isDuplicateOf(otherObject))
	                	{
	                		duplicateElementFound = true;
	                		break;
	                    }
	                }
	                if (duplicateElementFound == false)
            {
	                	idChangeRequired = true;
            }
        }
	            if (idChangeRequired == true)
	            {
	            	idChanged = true;
		            String newId = getNextId(otherObject, this.getOvalObjectMap(), otherDoc.getOvalObjectMap());
		            File file = new File(otherDoc.getFilename());
		            String warning = file.getName() + " - ID " + otherObject.getId() + " changed to " + newId + ". The source documents have multiple elements with ID " + otherObject.getId() + " that are not identical.";
		            stats.getWarnings().add(warning);
		            log.info(warning);	            
		            otherObject.setId(newId);
	            }
    }

	        // Update state IDs in the other document if required.
	        List<OvalState> otherStates = otherDoc.getOvalStates();
	        for (OvalState otherState : otherStates)
    {
	        	Boolean idChangeRequired = false;
	            if (containsState(otherState.getId()))
	            {
	            	Boolean duplicateElementFound = false;
	                List<OvalState> myStates = getOvalStates();
	                for (OvalState myState : myStates)
	                {
	                	if (myState.isDuplicateOf(otherState))
	                	{
	                		duplicateElementFound = true;
	                		break;
	                    }
	                }
	                if (duplicateElementFound == false)
	                {
	                	idChangeRequired = true;
	                }
	            }
	            if (idChangeRequired == true)
	            {
	            	idChanged = true;
		            String newId = getNextId(otherState, this.getOvalStateMap(), otherDoc.getOvalStateMap());
		            File file = new File(otherDoc.getFilename());
		            String warning = file.getName() + " - ID " + otherState.getId() + " changed to " + newId + ". The source documents have multiple elements with ID " + otherState.getId() + " that are not identical.";
		            stats.getWarnings().add(warning);
		            log.info(warning);	  
		            otherState.setId(newId);
	            }
	        }
	        
	        // Update variable IDs in the other document if required.
	        List<OvalVariable> otherVariables = otherDoc.getOvalVariables();
	        for (OvalVariable otherVariable : otherVariables)
	        {
	        	Boolean idChangeRequired = false;
	            if (containsVariable(otherVariable.getId()))
	            {
	            	Boolean duplicateElementFound = false;
	                List<OvalVariable> myVariables = getOvalVariables();
	                for (OvalVariable myVariable : myVariables)
	                {
	                	if (myVariable.isDuplicateOf(otherVariable))
	                	{
	                		duplicateElementFound = true;
	                		break;
	                    }
	                }
	                if (duplicateElementFound == false)
	                {
	                	idChangeRequired = true;
	                }
	            }
	            if (idChangeRequired == true)
        {
	            	idChanged = true;
		            String newId = getNextId(otherVariable, this.getOvalVariableMap(), otherDoc.getOvalVariableMap());
		            File file = new File(otherDoc.getFilename());
		            String warning = file.getName() + " - ID " + otherVariable.getId() + " changed to " + newId + ". The source documents have multiple elements with ID " + otherVariable.getId() + " that are not identical.";
		            stats.getWarnings().add(warning);
		            log.info(warning);	  
		            otherVariable.setId(newId);
	            }
	        }
		} while (idChanged == true);
    }

    /**
     * Merge another OvalDefinitionsDocument with this one.
     *
     * @param otherDoc, stats
     */
    public void merge(OvalDefinitionsDocument otherDoc, MergeStats stats)
    {
        // Make the IDs in the other document unique relative to this 
        // document.
        //
        // After this is method is called, the other document will contain:
        // - OVAL elements with IDs that do not exist in this document.
        // - OVAL elements that are exact duplicates of OVAL elements 
        //   in this document.
        // Because of this, the only OVAL elements that need to be merged will
        // be those that have IDs that aren't already present in this document.
		this.makeIDsUnique(otherDoc, stats);

        // Merge definitions.
		List<OvalDefinition> otherDefinitions = otherDoc.getOvalDefinitions();
        for (OvalDefinition otherDefinition : otherDefinitions)
        {
        	if (!(containsDefinition(otherDefinition.getId())))
            {
                addDefinition(otherDefinition, -1);
                stats.incrementDefsMerged();
            }
        }

        // Merge tests.
        List<OvalTest> otherTests = otherDoc.getOvalTests();
        for (OvalTest otherTest : otherTests)
        {
        	if (!(containsTest(otherTest.getId())))
            {
                addTest(otherTest, -1);
                stats.incrementTestsMerged();
            }
        }

        // Merge objects.
        List<OvalObject> otherObjects = otherDoc.getOvalObjects();
        for (OvalObject otherObject : otherObjects)
        {
        	if (!(containsObject(otherObject.getId())))
            {
                addObject(otherObject, -1);
                stats.incrementObjectsMerged();
            }
        }

        // Merge states.
        List<OvalState> otherStates = otherDoc.getOvalStates();
        for (OvalState otherState : otherStates)
        {
        	if (!(containsState(otherState.getId())))
            {
                addState(otherState, -1);
                stats.incrementStatesMerged();
            }
        }

        // Merge variables.
        List<OvalVariable> otherVariables = otherDoc.getOvalVariables();
        for (OvalVariable otherVariable : otherVariables)
        {
        	if (!(containsVariable(otherVariable.getId())))
            {
                addVariable(otherVariable, -1);
                stats.incrementVariablesMerged();
        }
        }
    }

    /**
     * Return a list of OvalDefinition objects whose metadata contains
     * reference elements referring to the supplied reference id.
     *
     * @param refId The reference id we want to get definitions for
     *
     * @return List<OvalDefinition>
     */
    public List<OvalDefinition> getDefsForReferenceId(String refId)
    {
        List<OvalDefinition> defs = new ArrayList<OvalDefinition>();
        Namespace ns = getElement().getNamespace();
        Element definitionsElement = getElement().getChild("definitions", ns);

        if (definitionsElement == null)
        {
            return defs;
        }

        List children = definitionsElement.getChildren();

        if (children != null)
        {
            for (int x = 0; x < children.size(); x++)
            {
                Element elem = (Element) children.get(x);

                OvalDefinition od = this.getDefinitionWrapper();
                od.setElement(elem);
                od.setRoot(this.getRoot());
                od.setSCAPDocument(this.getSCAPDocument());

                if (!containsDefinition(od.getId()))
                {
                    ovalDefinitionMap.put(od.getId(), od);
                }

                List<OvalReference> refs = od.getReferences();

                boolean addDef = false;

                if (refs != null && refs.size() > 0)
                {
                    for (int y = 0; y < refs.size(); y++)
                    {
                        OvalReference ref = refs.get(y);

                        if (ref.getRefId().equals(refId))
                        {
                            addDef = true;
                            break;
                        }
                    }
                }

                if (addDef)
                {
                    defs.add(od);
                }
            }
        }

        return defs;
    }

    @Override
    public String toString()
    {
        return "OVAL " + getSchemaVersion() + " Definitions Document(" + getFilename() + ")";
    }

    /**
     * Show variables, states, objects, tests, and definitions who have references to non-existent
     * entities.
     */
    public void showMissingEntityIds()
    {
        // loop through variables first
        List<OvalVariable> vars = getOvalVariables();
        if (vars != null && vars.size() > 0)
        {
            for (int x = 0; x < vars.size(); x++)
            {
                OvalVariable ov = vars.get(x);

                Set<String> brokenRefs = ov.getBrokenRefs();

                if (brokenRefs != null && brokenRefs.size() > 0)
                {
                    // we need to remove this variable
//    				log.error("Variable " + ov.getId() + " has broken references:");
//    				for(Iterator<String> it = brokenRefs.iterator(); it.hasNext();)
//    				{
//    					log.error("\t" + it.next());
//    				}
                }
            }
            vars.clear();
            vars = null;
        }

        // loop through states
        List<OvalState> states = getOvalStates();
        if (states != null && states.size() > 0)
        {
            for (int x = 0; x < states.size(); x++)
            {
                OvalState os = states.get(x);

                Set<String> brokenRefs = os.getBrokenRefs();

                if (brokenRefs != null && brokenRefs.size() > 0)
                {
                    // remove this state
//    				log.error("State " + os.getId() + " has broken references:");
//    				for(Iterator<String> it = brokenRefs.iterator(); it.hasNext();)
//    				{
//    					log.error("\t" + it.next());
//    				}
                }
            }
            states.clear();
            states = null;
        }

        // loop through objects
        List<OvalObject> objects = getOvalObjects();
        if (objects != null && objects.size() > 0)
        {
            for (int x = 0; x < objects.size(); x++)
            {
                OvalObject oo = objects.get(x);

                Set<String> brokenRefs = oo.getBrokenRefs();

                if (brokenRefs != null && brokenRefs.size() > 0)
                {
                    // remove this object
//    				log.error("Object " + oo.getId() + " has broken references:");
//    				for(Iterator<String> it = brokenRefs.iterator(); it.hasNext();)
//    				{
//    					log.error("\t" + it.next());
//    				}
                }
            }
            objects.clear();
            objects = null;
        }

        // loop through tests
        List<OvalTest> tests = getOvalTests();
        if (tests != null && tests.size() > 0)
        {
            for (int x = 0; x < tests.size(); x++)
            {
                OvalTest ot = tests.get(x);

                Set<String> brokenRefs = ot.getBrokenRefs();

                if (brokenRefs != null && brokenRefs.size() > 0)
                {
                    // remove this test
//    				log.error("Test " + ot.getId() + " has broken references:");
//    				for(Iterator<String> it = brokenRefs.iterator(); it.hasNext();)
//    				{
//    					log.error("\t" + it.next());
//    				}
                }
            }
            tests.clear();
            tests = null;
        }

        // loop through definitions
        List<OvalDefinition> defs = getOvalDefinitions();
        if (defs != null && defs.size() > 0)
        {
            for (int x = 0; x < defs.size(); x++)
            {
                OvalDefinition od = defs.get(x);
                Set<String> brokenRefs = od.getBrokenRefs();

                if (brokenRefs != null && brokenRefs.size() > 0)
                {
//    				log.error("Definition " + od.getId() + " has broken references:");
//    				for(Iterator<String> it = brokenRefs.iterator(); it.hasNext();)
//    				{
//    					log.error("\t" + it.next());
//    				}
                }
            }
            defs.clear();
            defs = null;
        }
    }

    /**
     * Validate things about definitions in this document.
     * 
     * @return String
     */
    public String validateDefinitions()
    {
        String ret = null;
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        if (getDocDefinitionIds() != null && getDocDefinitionIds().size() > 0)
        {
            // there are definitions in this document
            List<OvalDefinition> defs = getOvalDefinitions();

            for (int x = 0; x < defs.size(); x++)
            {
                OvalDefinition od = defs.get(x);
                Criteria criteria = od.getCriteria();
                if (criteria != null)
                {
                    validateCriteria(criteria, sb, od.getId());
                }

            }
        }

        if (sb.length() > 0)
        {
            ret = sb.toString();
        }
        return ret;
    }

    private void validateCriteria(CriteriaChild criteriaChild, StringBuilder sb, String defId)
    {
        String newLine = System.getProperty("line.separator");
        if (criteriaChild instanceof Criterion)
        {
            Criterion criterion = (Criterion) criteriaChild;
            String testId = criterion.getTestId();
            if (testId != null)
            {
                if (!containsTest(testId))
                {
                    sb.append("Criterion in definition " + defId + " points to a test that doesn't exist: " + testId + newLine);
                }
            } else
            {
                sb.append("Criterion in definition " + defId + " doesn't point to a test!" + newLine);
            }
        } else if (criteriaChild instanceof ExtendDefinition)
        {
            ExtendDefinition extDef = (ExtendDefinition) criteriaChild;
            String extDefId = extDef.getDefinitionId();
            if (extDefId != null)
            {
                if (!containsDefinition(extDefId))
                {
                    sb.append("ExtendDefinition in definition " + defId + " points to a definition that doesn't exist: " + extDefId + newLine);
                }
            } else
            {
                sb.append("ExtendDefinition in definition " + defId + " doesn't point to a definition!" + newLine);
            }
        } else if (criteriaChild instanceof Criteria)
        {
            List<CriteriaChild> children = criteriaChild.getChildren();
            for (CriteriaChild child : children)
            {
                validateCriteria(child, sb, defId);
            }
        }
    }

    /**
     * Validate things about tests in this document.
     * 
     * @return String
     */
    public String validateTests()
    {
        String ret = null;
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        if (getDocTestIds() != null && getDocTestIds().size() > 0)
        {
            // there are tests in this document
            List<OvalTest> tests = getOvalTests();

            for (int x = 0; x < tests.size(); x++)
            {
                OvalTest ot = tests.get(x);

                String objId = ot.getObjectId();
                String stateId = ot.getStateId();

                if (objId != null)
                {
                    if (!containsObject(objId))
                    {
                        sb.append("OvalTest " + ot.getId() + " points to an object that doesn't exist: " + objId + newLine);
                    }
                } else
                {
                    sb.append("OvalTest " + ot.getId() + " doesn't point to an object!" + newLine);
                }

                if (stateId != null)
                {
                    if (!containsState(stateId))
                    {
                        sb.append("OvalTest " + ot.getId() + " points to a state that doesn't exist: " + stateId + newLine);
                    }
                } else
                {
                    // a state is not required, so null is ok.
                }


            }
        }

        if (sb.length() > 0)
        {
            ret = sb.toString();
        }
        return ret;
    }

    /**
     * Validate things about objects in this document.
     * 
     * @return String
     */
    public String validateObjects()
    {
        String ret = null;
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        if (getDocObjectIds() != null && getDocObjectIds().size() > 0)
        {
            // there are objects in this document
            List<OvalObject> objects = getOvalObjects();

            for (int x = 0; x < objects.size(); x++)
            {
                OvalObject oo = objects.get(x);
                List<OvalObjectChild> children = oo.getChildren();
                for (OvalObjectChild child : children)
                {
                    validateObject(child, sb, oo.getId());
                }
            }
        }

        if (sb.length() > 0)
        {
            ret = sb.toString();
        }

        return ret;
    }

    /**
     * Validate things about an individual object and its children
     * 
     * @param child OvalObjectChild being validated
     * @param sb StringBuilder where error messages are being accumulated
     * @param objectId Id of the object being validated
     */
    public void validateObject(OvalObjectChild child, StringBuilder sb, String objectId)
    {
        if (child instanceof OvalObjectParameter)
        {
            String varRef = ((OvalObjectParameter) child).getVarRef();
            if (varRef != null && !containsVariable(varRef))
            {
                sb.append("OvalObject " + objectId + " has a parameter that"
                        + " points to a variable that doesn't exist: " + varRef + System.getProperty("line.separator"));
            }
        } else if (child instanceof OvalObjectReference)
        {
            String objId = ((OvalObjectReference) child).getObjectId();
            if (objId != null)
            {
                if (!containsObject(objId))
                {
                    sb.append("OvalObject " + objectId + " has an object reference that"
                            + " points to an object that doesn't exist: " + objId + System.getProperty("line.separator"));
                }
            } else
            {
                sb.append("OvalObject " + objectId + " has an object reference "
                        + " that doesn't point to an object!" + System.getProperty("line.separator"));
            }
        } else if (child instanceof OvalObjectFilter)
        {
            String stateId = ((OvalObjectFilter) child).getStateId();
            if (stateId != null)
            {
                if (!containsState(stateId))
                {
                    sb.append("OvalObject " + objectId + " has an object filter that"
                            + " points to a state that doesn't exist: " + stateId + System.getProperty("line.separator"));
                }
            } else
            {
                sb.append("OvalObject " + objectId + " has an object filter "
                        + " that doesn't point to state!" + System.getProperty("line.separator"));
            }
        }
        List<OvalObjectChild> children = child.getChildren();
        for (OvalObjectChild grandChild : children)
        {
            validateObject(grandChild, sb, objectId);
        }
    }

    /**
     * Validate things about states in this document.
     * 
     * @return String
     */
    public String validateStates()
    {
        String ret = null;
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        if (getDocStateIds() != null && getDocStateIds().size() > 0)
        {
            // there are states in this document
            List<OvalState> states = getOvalStates();

            for (int x = 0; x < states.size(); x++)
            {
                OvalState os = states.get(x);

                List<OvalStateParameter> parms = os.getParameters();

                if (parms != null && parms.size() > 0)
                {
                    for (int y = 0; y < parms.size(); y++)
                    {
                        OvalStateParameter osp = parms.get(y);

                        String varRef = osp.getVarRef();

                        if (varRef != null)
                        {
                            if (!containsVariable(varRef))
                            {
                                sb.append("OvalState " + os.getId() + " has a parameter that"
                                        + " points to a variable that doesn't exist: " + varRef + newLine);
                            }
                        }
                        if (osp.getElementName().equals("result")) {
                        	List<OvalStateParameter> fieldParms = osp.getFieldParameters();
                        	for (OvalStateParameter fieldOsp : fieldParms) {
                        		varRef = fieldOsp.getVarRef();
                        		if (varRef != null && !containsVariable(varRef)) {
                        			sb.append("OvalState " + os.getId() + " has a parameter that"
                                            + " points to a variable that doesn't exist: " + varRef + newLine);
                        		}
                        	}
                        }
                    }
                }
            }
        }

        if (sb.length() > 0)
        {
            ret = sb.toString();
        }

        return ret;
    }

    /**
     * Validate things about variables in this document.
     * 
     * @return String
     */
    public String validateVariables()
    {
        String ret = null;
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        if (getDocVariableIds() != null && getDocVariableIds().size() > 0)
        {
            // there are variables in this document
            List<OvalVariable> variables = getOvalVariables();

            for (int x = 0; x < variables.size(); x++)
            {
                OvalVariable ov = variables.get(x);

                DefaultMutableTreeNode children = ov.getChildren();

                if (children != null)
                {
                    Enumeration<DefaultMutableTreeNode> chEnum = children.depthFirstEnumeration();

                    while (chEnum.hasMoreElements())
                    {
                        DefaultMutableTreeNode child = chEnum.nextElement();
                        Object userObj = child.getUserObject();

                        if (userObj instanceof OvalVariableComponentObject)
                        {
                            OvalVariableComponentObject objComponent = (OvalVariableComponentObject) userObj;

                            String objId = objComponent.getObjectId();

                            if (objId != null)
                            {
                                if (!containsObject(objId))
                                {
                                    sb.append("OvalVariable " + ov.getId() + " has an object component that"
                                            + " points to an object that doesn't exist: " + objId + newLine);
                                } else
                                {
                                    // TODO: check that the field also exists
                                }
                            } else
                            {
                                sb.append("OvalVariable " + ov.getId() + " has an object component that"
                                        + " does not point to an object" + newLine);
                            }
                        } else if (userObj instanceof OvalVariableComponentVariable)
                        {
                            OvalVariableComponentVariable varComponent = (OvalVariableComponentVariable) userObj;

                            String varId = varComponent.getVariableId();

                            if (varId != null)
                            {
                                if (!containsVariable(varId))
                                {
                                    sb.append("OvalVariable " + ov.getId() + " has a variable component that"
                                            + " points to a variable that doesn't exist: " + varId + newLine);
                                }
                            } else
                            {
                                sb.append("OvalVariable " + ov.getId() + " has a variable component that"
                                        + " does not point to a variable" + newLine);
                            }

                        }
                    }
                }
            }
        }

        if (sb.length() > 0)
        {
            ret = sb.toString();
        }

        return ret;
    }

    @Override
    public String validateSymantically() throws Exception
    {
        String ret = null;
        StringBuilder sb = new StringBuilder();

        // definitions
        String defResults = validateDefinitions();
        if (defResults != null)
        {
            sb.append(defResults);
        }

        // tests
        String testResults = validateTests();
        if (testResults != null)
        {
            sb.append(testResults);
        }

        // objects
        String objectResults = validateObjects();
        if (objectResults != null)
        {
            sb.append(objectResults);
        }

        // states
        String stateResults = validateStates();
        if (stateResults != null)
        {
            sb.append(stateResults);
        }

        // variables
        String variableResults = validateVariables();
        if (variableResults != null)
        {
            sb.append(variableResults);
        }

        if (sb.length() > 0)
        {
            ret = sb.toString();
        }

        return ret;
    }

    /**
     * Check to see if the supplied id exists as a definition, test, object, state, or variable.
     * 
     * @param id The id of the item we want to return.
     * @return VersionedOvalElement
     */
    public VersionedOvalElement getById(String id)
    {
        VersionedOvalElement ret = null;

        if (containsDefinition(id))
        {
            ret = getOvalDefinition(id);
        } else if (containsTest(id))
        {
            ret = getOvalTest(id);
        } else if (containsObject(id))
        {
            ret = getOvalObject(id);
        } else if (containsState(id))
        {
            ret = getOvalState(id);
        } else if (containsVariable(id))
        {
            ret = getOvalVariable(id);
        }

        return ret;
    }

    /**
     * This method should be called after a definition has been renamed to update the map containing
     * this document's definition ids.
     * 
     * @param oldId
     * @param newId
     */
    public void updateRenamedDefinition(String oldId, String newId)
    {
        if (ovalDefinitionMap.containsKey(oldId))
        {
            OvalDefinition od = (OvalDefinition) ovalDefinitionMap.remove(oldId);

            // put it back in the map with the new id.
            ovalDefinitionMap.put(newId, od);
        }
    }

    /**
     * This method should be called after a test has been renamed to update the map containing
     * this document's test ids.
     * 
     * @param oldId
     * @param newId
     */
    public void updateRenamedTest(String oldId, String newId)
    {
        if (ovalTestMap.containsKey(oldId))
        {
            OvalTest ot = (OvalTest) ovalTestMap.remove(oldId);

            // put it back in the map with the new id.
            ovalTestMap.put(newId, ot);
        }
    }

    /**
     * This method should be called after an object has been renamed to update the map containing
     * this document's object ids.
     * 
     * @param oldId
     * @param newId
     */
    public void updateRenamedObject(String oldId, String newId)
    {
        if (ovalObjectMap.containsKey(oldId))
        {
            OvalObject oo = (OvalObject) ovalObjectMap.remove(oldId);

            // put it back in the map with the new id.
            ovalObjectMap.put(newId, oo);
        }
//    	else
//    	{
//    		throw new IllegalArgumentException("updateRenamedObject(String oldId, String newId): No existing object has the id " + oldId);
//    	}
    }

    /**
     * This method should be called after a state has been renamed to update the map containing
     * this document's state ids.
     * 
     * @param oldId
     * @param newId
     */
    public void updateRenamedState(String oldId, String newId)
    {
        if (ovalStateMap.containsKey(oldId))
        {
            OvalState os = (OvalState) ovalStateMap.remove(oldId);

            // put it back in the map with the new id.
            ovalStateMap.put(newId, os);
        }
    }

    /**
     * This method should be called after a variable has been renamed to update the map containing
     * this document's variable ids.
     * 
     * @param oldId
     * @param newId
     */
    public void updateRenamedVariable(String oldId, String newId)
    {
        if (ovalVariableMap.containsKey(oldId))
        {
            OvalVariable ov = (OvalVariable) ovalVariableMap.remove(oldId);

            // put it back in the map with the new id.
            ovalVariableMap.put(newId, ov);
        }
    }

    public OvalVariableChild getOvalVariableChild(Element e)
    {
        String elementName = e.getName();
        OvalVariableChild varChild = null;
        if (elementName.toLowerCase().equals("object_component"))
        {
            varChild = getObjectVariableComponentWrapper();
        } else if (elementName.toLowerCase().equals("variable_component"))
        {
            varChild = getVariableComponentWrapper();
        } else if (elementName.toLowerCase().equals("literal_component"))
        {
            varChild = getLiteralVariableComponentWrapper();
        } else if (elementName.toLowerCase().equals("restriction"))
        {
            varChild = new VariableRestriction(this);
        } else if (elementName.toLowerCase().equals("possible_restriction"))
        {
            varChild = new VariablePossibleRestriction(this);
        } else if (elementName.toLowerCase().equals("possible_value"))
        {
            varChild = new VariablePossibleValue(this);
        } else if (elementName.toLowerCase().equals("value"))
        {
            varChild = new ConstantVariableValue(this);
        } else
        {
            OvalFunctionEnum functionType = OvalFunctionEnum.valueOf(elementName);
            varChild = createOvalFunctionObject(functionType);
        }
        varChild.setElement(e);
        return varChild;
    }

    /**
     * Create an oval function suitable for adding an variable as a child.
     * 
     * @param functionType
     * 
     * @return OvalFunction
     */
    public OvalFunction createOvalFunctionObject(OvalFunctionEnum functionType)
    {
        OvalFunction result = null;
        switch (functionType)
        {
            case arithmetic:
                result = new OvalFunctionArithmetic(this);
                break;
            case begin:
                result = new OvalFunctionBegin(this);
                break;
            case concat:
                result = new OvalFunctionConcat(this);
                break;
            case end:
                result = new OvalFunctionEnd(this);
                break;
            case escape_regex:
                result = new OvalFunctionEscapeRegex(this);
                break;
            case regex_capture:
                result = new OvalFunctionRegexCapture(this);
                break;
            case split:
                result = new OvalFunctionSplit(this);
                break;
            case substring:
                result = new OvalFunctionSubstring(this);
                break;
            case time_difference:
                result = new OvalFunctionTimeDifference(this);
                break;
            case unique:
            	result = new OvalFunctionUnique(this);
            	break;
            case count:
            	result = new OvalFunctionCount(this);
            	break;
            default:
                throw new IllegalArgumentException("Function type not yet supported:" + functionType);
        }
        return result;
    }

    public OvalObjectChild getOvalObjectChild(Element e, List<OvalEntity> validParms)
    {
        OvalObjectChild child = null;
        String elementName = e.getName();
        if (elementName.toLowerCase().equals("object_reference"))
        {
            OvalObjectReference oor = getObjectReferenceWrapper();
            oor.setElement(e);
            child = oor;
        } else if (elementName.toLowerCase().equals("set"))
        {
            OvalObjectSet oos = getObjectSetWrapper();
            oos.setElement(e);
            child = oos;
            //		buildObjectParmTree(childNode, childElement, validParms);
        } else if (elementName.toLowerCase().equals("filter"))
        {
            OvalObjectFilter oof = getObjectFilterWrapper();
            oof.setElement(e);
            child = oof;
        } else
        {
            OvalObjectParameter oop = getObjectParameterWrapper();
            oop.setElement(e);
            if (validParms != null)
            {
                int index = validParms.indexOf(new NameDoc(elementName, null));
                if (index > -1)
                {
                    oop.setEntity(validParms.get(index));
                }
            } else
            {
                log.error("validParms is null for elementName " + elementName);
            }
            child = oop;
        }
        return child;
    }

    /**
     * Attempt to consolidate entities(tests, objects, states, variables) in this document by
     * removing duplicates. Duplicates are entities that may have a different id but are
     * precisely the same otherwise.
     *
     * @return OvalFunction
     */
    public boolean eliminateDuplicates()
    {
        boolean changed = false;
        IdComparator idComparator = new IdComparator();

        // First eliminate any duplicate variables
        List<OvalVariable> variables = getOvalVariables();
        Collections.sort(variables, idComparator);
        for (int iOuter = 0; iOuter < variables.size(); iOuter++)
        {
            OvalVariable variable1 = variables.get(iOuter);
            for (int iInner = iOuter + 1; iInner < variables.size(); iInner++)
            {
                OvalVariable variable2 = variables.get(iInner);
                if (variable1.isDuplicateOf(variable2))
                {
                    log.debug("Found duplicate var, replacing " + variable2.getId() + " with " + variable1.getId());
                    variable2.setId(variable1.getId(), true);  //force change of all references to obj2 to refer to obj1
                    variable2.getElement().detach();
                    changed = true;
                }
            }
        }

        // next eliminate any duplicate states
        List<OvalState> states = getOvalStates();
        Collections.sort(states, idComparator);
        for (int iOuter = 0; iOuter < states.size(); iOuter++)
        {
            OvalState state1 = states.get(iOuter);
            for (int iInner = iOuter + 1; iInner < states.size(); iInner++)
            {
                OvalState state2 = states.get(iInner);
                if (state1.isDuplicateOf(state2))
                {
                    String state2Id = state2.getId();
                    String state1Id = state1.getId();

                    log.debug("Found duplicate state, replacing " + state2Id + " with " + state1Id);
                    state2.setId(state1Id, true);  //force change of all references to obj2 to refer to obj1
                    changed = true;
                }
            }
        }

        // next eliminate any duplicate objects
        List<OvalObject> objects = getOvalObjects();
        Collections.sort(objects, idComparator);
        for (int iOuter = 0; iOuter < objects.size(); iOuter++)
        {
            OvalObject object1 = objects.get(iOuter);
            for (int iInner = iOuter + 1; iInner < objects.size(); iInner++)
            {
                OvalObject object2 = objects.get(iInner);
                if (object1.isDuplicateOf(object2))
                {
                    log.debug("Found duplicate object, replacing " + object2.getId() + " with " + object1.getId());
                    object2.setId(object1.getId(), true);  //force change of all references to obj2 to refer to obj1
                    object2.getElement().detach();
                    changed = true;
                }
            }
        }

        // next eliminate any duplicate tests
        List<OvalTest> tests = getOvalTests();
        Collections.sort(tests, idComparator);
        for (int iOuter = 0; iOuter < tests.size(); iOuter++)
        {
            OvalTest test1 = tests.get(iOuter);
            for (int iInner = iOuter + 1; iInner < tests.size(); iInner++)
            {
                OvalTest test2 = tests.get(iInner);
                if (test1.isDuplicateOf(test2))
                {
                    log.debug("Found duplicate test, replacing " + test2.getId() + " with " + test1.getId());
                    test2.setId(test1.getId(), true);  //force change of all references to test2 to refer to test1
                    test2.getElement().detach();
                    changed = true;
                }
            }
        }

        log.debug("eliminateDuplicates returning " + changed);
        return changed;
    }

    private class IdComparator implements Comparator<OvalElement>
    {

        @Override
        public int compare(OvalElement o1, OvalElement o2)
        {
            return o1.getId().compareTo(o2.getId());
        }
    }

    /**
     * Get a unique set of products/platforms referenced by any definitions
     * in this document.   This allows you to look up CPEs for each one.
     *
     * @return Set<String>
     */
    public Set<String> getReferencedPlatformsAndProducts()
    {
        Set<String> ret = new HashSet<String>();

        List<OvalDefinition> allDefs = getOvalDefinitions();

        if (allDefs != null && allDefs.size() > 0)
        {
            for (int x = 0; x < allDefs.size(); x++)
            {
                OvalDefinition def = allDefs.get(x);

                Metadata meta = def.getMetadata();

                List<AffectedItemContainer> affectedContainers = meta.getAffected();

                if (affectedContainers != null && affectedContainers.size() > 0)
                {
                    for (int y = 0; y < affectedContainers.size(); y++)
                    {
                        AffectedItemContainer container = affectedContainers.get(y);

                        List<AffectedItem> items = container.getAffectedItems();

                        if (items != null && items.size() > 0)
                        {
                            for (int z = 0; z < items.size(); z++)
                            {
                                AffectedItem item = items.get(z);

                                String value = item.getValue();

                                if (!ret.contains(value))
                                {
                                    ret.add(value);
                                }
                            }
                        }
                    }
                }
            }
        }

        return ret;
    }

    @Override
    protected void finalize()
    {
        close();

        ovalDefinitionMap = null;
    	ovalObjectMap = null;
    	ovalStateMap = null;
    	ovalVariableMap = null;
    	ovalTestMap = null;

    	setElement(null);
    	setDoc(null);
    }

    @Override
    public void close()
    {
    	ovalDefinitionMap.clear();
    	ovalObjectMap.clear();
    	ovalStateMap.clear();
    	ovalVariableMap.clear();
    	ovalTestMap.clear();    	
    }
    
    private void gatherReferencedNamespaces(Element parent, NamespaceReferenceCounter refCounter)
    {
        Namespace parentNS = parent.getNamespace();
        
        refCounter.addNamespace(parentNS);        
    
        List addlNamespaces = parent.getAdditionalNamespaces();
        if(addlNamespaces != null)
        {
            for(Object o : addlNamespaces)
            {
                Namespace ans = (Namespace) o;
                
                refCounter.addNamespace(ans);
            }
        }
        addlNamespaces = null;
        
        List children = parent.getChildren();
        if(children != null)
        {
            for(Object o : children)
            {
                if(o instanceof Element)
                {
                    Element child = (Element) o;
                    
                    gatherReferencedNamespaces(child, refCounter);
                }
            }
        }
        
        children = null;
    }
    
    private void removeUnusedNamespaces()
    {
        NamespaceReferenceCounter refCounter = new NamespaceReferenceCounter();

        gatherReferencedNamespaces(getElement(), refCounter);
        
        Map<Namespace, Integer> namespaceMap = refCounter.getNamespaceMap();
        
        if(namespaceMap != null)
        {
            Iterator<Namespace> nsItr = namespaceMap.keySet().iterator();
            
            while(nsItr.hasNext())
            {
                Namespace ns = nsItr.next();
                
                Integer refCount = namespaceMap.get(ns);
                
                if(refCount.intValue() < 2)
                {
                    // less than 2 means that the only reference to the
                    // namespace is the document's reference in the root
                    // element.
                    
                    if(ns.getURI().indexOf("#") > -1)
                    {
                        // only remove if this is a platform namespace
                        getElement().removeNamespaceDeclaration(ns);
                    }
                }
            }
        }        
    }
    
    /**
     * Save the document to the disk using the current filename.
     * 
     * @throws IOException
     */
    @Override
    public void save() throws IOException
    {
        removeUnusedNamespaces();
        super.save();
    }

    /**
     * Save this document as another name on disk.
     * 
     * @param file A File object representing the file we want to save to
     * 
     * @throws IOException
     */
    @Override
    public void saveAs(File file) throws IOException
    {
        removeUnusedNamespaces();
        super.saveAs(file);
    }
    
    @Override
    public void setDocumentType(SCAPDocumentTypeEnum documentType)
    {
      this.documentType = documentType;
    }    
    
    public int getStartingNumber() {
		return startingNumber;
	}

	public void setStartingNumber(int startingNumber) {
		this.startingNumber = startingNumber;
	}

}
