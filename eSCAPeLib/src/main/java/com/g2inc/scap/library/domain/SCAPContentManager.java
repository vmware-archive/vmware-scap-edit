package com.g2inc.scap.library.domain;
/* Copyright (c) 2016 - 2016. VMware, Inc. All rights reserved.
* 
* This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License version 3.0 
* as published by the FreeSoftware Foundation This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License version 3.0 
* for more details. You should have received a copy of the GNU General Public License version 3.0 along with this program; if not, write to
* the Free Software Foundation, Inc., 675 Mass Avenue, Cambridge, MA 02139, USA.
*/
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.g2inc.scap.library.domain.bundle.SCAPBundleType;
import com.g2inc.scap.library.domain.bundle.SCAPDocumentBundle;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.cpe.CPEItem;
import com.g2inc.scap.library.domain.cpe.CPEItemTitle;
import com.g2inc.scap.library.domain.oval.AvailableObjectBehavior;
import com.g2inc.scap.library.domain.oval.DefinitionClassEnum;
import com.g2inc.scap.library.domain.oval.OvalDatatype;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalEntity;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalReference;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVariable;
import com.g2inc.scap.library.parsers.ParserAbstract;
import com.g2inc.scap.library.schema.NameDoc;
import com.g2inc.scap.library.schema.PlatformNameKey;
import com.g2inc.scap.library.schema.SchemaLocator;

/**
 * This class is the primary way to load, access, and save content.
 * It is defined as a singleton so that access to the content can be synchronized.
 *
 * @author ssill2
 */
public class SCAPContentManager
{
    private static final Logger LOG = Logger.getLogger(SCAPContentManager.class.getName());
    private static SCAPContentManager instance = null;
 
    private SCAPDocumentBundle officalCPEBundle = null;

    private List<String> officalCPEProductNames = null;
    private List<String> officalCPEProductTitles = null;
    private List<String> officalCPEPlatformNames = null;
    private List<String> officalCPEPlatformTitles = null;

    private final String cpeHold = "CPEHOLD";
  
    private static final Map<SCAPDocumentTypeEnum, List<String>> platformTypesForOvalVersion = new HashMap<SCAPDocumentTypeEnum, List<String>>();
    private final Map<SCAPDocumentTypeEnum, Map<String, List<NameDoc>>> testTypesForOvalVersionAndPlatformType =
            new HashMap<SCAPDocumentTypeEnum, Map<String, List<NameDoc>>>();
    private final Map<SCAPDocumentTypeEnum, Map<String, List<NameDoc>>> objectTypesForOvalVersionAndPlatformType =
            new HashMap<SCAPDocumentTypeEnum, Map<String, List<NameDoc>>>();
    private final Map<SCAPDocumentTypeEnum, Map<String, List<NameDoc>>> stateTypesForOvalVersionAndPlatformType =
            new HashMap<SCAPDocumentTypeEnum, Map<String, List<NameDoc>>>();
    private final Map<SCAPDocumentTypeEnum, Map<String, Map<String, List<OvalEntity>>>> objectEntityTypesForOvalVersionAndObjectType =
            new HashMap<SCAPDocumentTypeEnum, Map<String, Map<String, List<OvalEntity>>>>();
    private final Map<SCAPDocumentTypeEnum, Map<String, Map<String, List<OvalEntity>>>> stateEntityTypesForOvalVersionAndStateType =
            new HashMap<SCAPDocumentTypeEnum, Map<String, Map<String, List<OvalEntity>>>>();
    private final Map<SCAPDocumentTypeEnum, Map<String, NameDoc>> enumTypesForOvalVersion =
            new HashMap<SCAPDocumentTypeEnum, Map<String, NameDoc>>();
    private final Map<SCAPDocumentTypeEnum, Map<String, List<NameDoc>>> enumValuesForOvalVersion =
            new HashMap<SCAPDocumentTypeEnum, Map<String, List<NameDoc>>>();
    private final Map<SCAPDocumentTypeEnum, Map<PlatformNameKey, List<AvailableObjectBehavior>>> behaviorValues =
            new HashMap<SCAPDocumentTypeEnum, Map<PlatformNameKey, List<AvailableObjectBehavior>>>();
    private Map<String, SCAPDocument> content = new HashMap<String, SCAPDocument>();


    private void parseDocs(File fileOrDir) throws IOException, FileNotFoundException, Exception
    {
        if (fileOrDir.exists() && fileOrDir.canRead())
        {
            if (fileOrDir.isDirectory())
            {
                FilenameFilter fnf = new FilenameFilter()
                {
                	@Override
                    public boolean accept(File dir, String name)
                    {
                        if (name.endsWith(".xml") || name.endsWith(".XML"))
                        {
                            return true;
                        }
                        return false;
                    }
                };

                File[] dircontents = fileOrDir.listFiles(fnf);
                int len = dircontents.length;
                for (int x = 0; x < len; x++)
                {
                    parseDocs(dircontents[x]);
                }
            }
            else if (fileOrDir.isFile())
            {
                Document doc = null;

                try
                {
                	if(LOG.isDebugEnabled())
                	{
                		LOG.trace("Beginning parse of " + fileOrDir.getAbsolutePath());
                	}

                    SCAPDocument sdoc = SCAPDocumentFactory.loadDocument(fileOrDir);
                    content.put(fileOrDir.getAbsolutePath(), sdoc);

                }
                catch (JDOMException je)
                {
                    throw new IllegalArgumentException("JDOMException occured trying to parse file " + fileOrDir, je);
                }
                catch (IOException e)
                {
                    throw e;
                }
                catch (Exception e)
                {
                	throw e;
                }

            }
        }
        else
        {
            throw new FileNotFoundException(fileOrDir.getAbsolutePath() + " doesn't exist or is not readable!");
        }
    }

    /**
     * This getInstance method allows you to return the singleton instance of this
     * class for a given file or directory
     *
     * @param fileOrDir A File object representing a file or directory where content is stored
     * @return SCAPContentManager
    */
    public synchronized static SCAPContentManager getInstance(File fileOrDir) throws IOException, Exception
    {
        return getInstance(fileOrDir, null);
    }

    /**
     * This function should be called to create a new oval definitions document in the given filename.
     * The type(aka version) of oval document is also specified.
     *
     * @param fn Path of new oval file you would like created.
     * @param docType The type of document you want created
    */
    public OvalDefinitionsDocument newDefinitionsDocument(String fn, SCAPDocumentTypeEnum docType)
    {
        String newFileName = fn;
        LOG.trace("Creating new Oval file:" + newFileName);
        File newFile = new File(newFileName);
        SCAPDocument sdoc = null;
        
        try 
        {
            sdoc = SCAPDocumentFactory.createNewDocument(docType);
            sdoc.setFilename(newFile.getAbsolutePath());
        }
        catch (Exception e)
        {
            LOG.error("IO Error creating new OVAL XML file",e);
            return null;
        }
        
        OvalDefinitionsDocument doc = (OvalDefinitionsDocument) sdoc;
        doc.setGeneratorDate(new Date());
        try
        {
            doc.save();
        }
        catch (Exception e)
        {
            LOG.error("Error writing new OVAL XML file", e);
            return null;
        }

        return doc;
    }

    /**
     * This getInstance method allows you to return the singleton instance of this
     * class for a given file or directory, along with specifying the path
     * where the schemas can be located.
     *
     * @param fileOrDir A File object representing a file or directory where content is stored
     * @param schemaroot Path to schemas
    */
    public synchronized static SCAPContentManager getInstance(File fileOrDir, String schemaroot) throws IOException, Exception
    {
        if (instance == null)
        {
            instance = new SCAPContentManager();
            instance.populateOVALSchemaInformation();
        }

        if (fileOrDir == null)
        {
            return instance;
        }

        if (fileOrDir.isFile())
        {
            // when called with a file, see if that one file is already parsed.
            String absPath = fileOrDir.getAbsolutePath();
            if (instance.content.containsKey(absPath))
            {
                return instance;
            }

            long start = System.nanoTime();
            instance.parseDocs(fileOrDir);
            long finish = System.nanoTime();
            long timetakenNS = finish - start;
            float timeTakenSec = (float) timetakenNS / (float) 1000000000;
            if(LOG.isDebugEnabled())
            {
                LOG.trace("parseDocs time taken(sec): " + timeTakenSec);
            }
        } else if (fileOrDir.isDirectory())
        {
            List<File> files = new ArrayList<File>();
            gatherFiles(fileOrDir, files);

            Iterator<File> fi = files.iterator();

            while (fi.hasNext())
            {
                File f = fi.next();

                String absPath = f.getAbsolutePath();
                if (instance.content.containsKey(absPath))
                {
                    continue;
                }

                long start = System.nanoTime();
                instance.parseDocs(fileOrDir);
                long finish = System.nanoTime();
                long timetakenNS = finish - start;
                float timeTakenSec = (float) timetakenNS / (float) 1000000000;
                
                if(LOG.isDebugEnabled())
                {
                	LOG.trace("parseDocs time taken(sec): " + timeTakenSec);
                }
            }
        }

        return instance;
    }

    private static void gatherFiles(File fileOrDir, List<File> files)
    {
        if (fileOrDir.isFile())
        {
            if (fileOrDir.getName().toLowerCase().endsWith(".xml"))
            {
                files.add(fileOrDir);
            }
        } else if (fileOrDir.isDirectory())
        {
            File[] children = fileOrDir.listFiles();

            if (children == null)
            {
                return;
            }

            for (int x = 0; x < children.length; x++)
            {
                gatherFiles(children[x], files);
            }
        }
    }

    /**
     * This getInstance method allows you to return the singleton instance of this
     * class.  If the instance hasn't been initialized, it will be initialized without
     * any documents.
     * 
     * @return SCAPContentManager
    */
    public synchronized static SCAPContentManager getInstance()
    {

        if (instance == null)
        {
            try
            {
                return getInstance(null, null);
            } catch (Exception e)
            {
                throw new IllegalStateException("Got really unexpected exception", e);
            }
        }

        return instance;
    }

    /**
     * Get an already loaded document by name.
     *
     * @param absFilePath String path of a loaded document.
     * @return SCAPDocument
    */
    public synchronized SCAPDocument getDocument(String absFilePath)
    {
        return content.get(absFilePath);
    }

    /**
     * Get the definitions from an already loaded document.
     *
     * @param absFilePath String path of a loaded document.
     * @return List<OvalDefinition>
    */
    public synchronized List<OvalDefinition> getDefinitions(String absFilePath)
    {
        List<OvalDefinition> defs = null;

        SCAPDocument d = content.get(absFilePath);

        if (d == null)
        {
            return null;
        }

        if (!(d instanceof OvalDefinitionsDocument))
        {
            return null;
        }

        OvalDefinitionsDocument odd = (OvalDefinitionsDocument) d;

        defs = odd.getOvalDefinitions();

        return defs;
    }
    
    public synchronized List<SCAPDocument> getLoadedSCAPDocuments() {
    	List<SCAPDocument> list = new ArrayList<SCAPDocument>();
    	Iterator<String> contentIter = content.keySet().iterator();
    	while (contentIter.hasNext()) {
    		String filename = contentIter.next();
    		SCAPDocument sd = content.get(filename);
    		list.add(sd);
    	}
    	return list;
    }
    
    public synchronized List<SCAPDocument> getLoadedSCAPDocuments(SCAPDocumentTypeEnum type) {
    	List<SCAPDocument> list = new ArrayList<SCAPDocument>();
    	Iterator<String> contentIter = content.keySet().iterator();
    	while (contentIter.hasNext()) {
    		String filename = contentIter.next();
    		SCAPDocument sd = content.get(filename);
    		if (type == null)
    			list.add(sd);
    		else if (sd.getDocumentType() == type) {
    			list.add(sd);
    		}
    	}
    	return list;
    }    

    /**
     * Get any loaded oval definitions documents
     *
     * 
    */
    public synchronized List<OvalDefinitionsDocument> getOvalDefinitionsDocuments()
    {
        return getOvalDefinitionsDocuments(null);
    }

    /**
     * Get any loaded oval definitions documents in a certain directory
     *
     * 
    */
    public synchronized List<OvalDefinitionsDocument> getOvalDefinitionsDocuments(String absParent)
    {
        List<OvalDefinitionsDocument> defDocs = new ArrayList<OvalDefinitionsDocument>();

        Iterator<String> itr = content.keySet().iterator();

        while (itr.hasNext())
        {
            String filename = itr.next();

            if (filename == null || filename.startsWith(absParent))
            {
                SCAPDocument sd = content.get(filename);

                if (sd instanceof OvalDefinitionsDocument)
                {
                    defDocs.add((OvalDefinitionsDocument) sd);
                }
            }
        }

        return defDocs;
    }

    /**
     * Returns true if any of the loaded OvalDefinitionsDocuments contain
     * a definition with the given id
     *
     * @param defId The id of an oval definition.
     * 
    */
    public synchronized boolean haveDefinition(String defId)
    {
        boolean ret = false;

        Iterator<String> itr = content.keySet().iterator();

        while (itr.hasNext())
        {
            String filename = itr.next();

            SCAPDocument sd = content.get(filename);

            if (sd instanceof OvalDefinitionsDocument)
            {
                OvalDefinitionsDocument odd = (OvalDefinitionsDocument) sd;

                // ask about defId
                ret = odd.containsDefinition(defId);

                if (ret)
                {
                    break;
                }
            }
        }

        return ret;
    }

    private void addDependentDefs(OvalDefinitionsDocument existingDoc,
            OvalDefinitionsDocument newDoc,
            List<OvalDefinition> referencedDefs)
    {
        if (referencedDefs != null)
        {
            for (OvalDefinition od : referencedDefs)
            {
                if (!newDoc.containsDefinition(od.getId()))
                {
                    newDoc.add(od);

                    List<OvalDefinition> rdefs = od.getReferencedDefinitions();
                    if (rdefs != null && rdefs.size() > 0)
                    {
                        addDependentDefs(existingDoc, newDoc, rdefs);
                    }

                    List<OvalTest> rtests = od.getReferencedTests();
                    if (rtests != null && rtests.size() > 0)
                    {
                        addDependentTests(existingDoc, newDoc, rtests);
                    }
                }
            }
        }
    }

    private void addDependentTests(OvalDefinitionsDocument existingDoc,
            OvalDefinitionsDocument newDoc,
            List<OvalTest> referencedTests)
    {
        if (referencedTests != null)
        {
            for (OvalTest ot : referencedTests)
            {
                if (!newDoc.containsTest(ot.getId()))
                {
//					newDocTestsElement.addContent((Element) ot.getElement().clone());
                    newDoc.add(ot);

                    OvalObject oo = ot.getObject();
                    if (oo != null)
                    {
                        List<OvalObject> robjs = new ArrayList<OvalObject>();
                        robjs.add(oo);
                        addDependentObjects(existingDoc, newDoc, robjs);
                    }

                    OvalState os = ot.getState();
                    if (os != null)
                    {
                        List<OvalState> rstates = new ArrayList<OvalState>();
                        rstates.add(os);
                        addDependentStates(existingDoc, newDoc, rstates);
                    }
                }
            }
        }
    }

    private void addDependentObjects(OvalDefinitionsDocument existingDoc,
            OvalDefinitionsDocument newDoc,
            List<OvalObject> referencedObjs)
    {
        if (referencedObjs != null)
        {
            for (OvalObject oo : referencedObjs)
            {
                if (!newDoc.containsObject(oo.getId()))
                {
//					newDocObjectsElement.addContent((Element) oo.getElement().clone());
                    newDoc.add(oo);

                    List<OvalObject> robjs = oo.getReferencedObjects();
                    if (robjs != null && robjs.size() > 0)
                    {
                        addDependentObjects(existingDoc, newDoc, robjs);
                    }

                    List<OvalState> rstates = oo.getReferencedStates();
                    if (rstates != null && rstates.size() > 0)
                    {
                        addDependentStates(existingDoc, newDoc, rstates);
                    }

                    List<OvalVariable> rvars = oo.getReferencedVariables();
                    if (rvars != null && rvars.size() > 0)
                    {
                        addDependentVariables(existingDoc, newDoc, rvars);
                    }
                }
            }
        }
    }

    private void addDependentStates(OvalDefinitionsDocument existingDoc,
            OvalDefinitionsDocument newDoc,
            List<OvalState> referencedStates)
    {
        if (referencedStates != null)
        {
            for (OvalState os : referencedStates)
            {
                if (!newDoc.containsState(os.getId()))
                {
//					newDocStatesElement.addContent((Element) os.getElement().clone());
                    newDoc.add(os);

                    List<OvalVariable> rvars = os.getReferencedVariables();

                    if (rvars != null && rvars.size() > 0)
                    {
                        addDependentVariables(existingDoc, newDoc, rvars);
                    }
                }
            }
        }
    }

    private void addDependentVariables(OvalDefinitionsDocument existingDoc,
            OvalDefinitionsDocument newDoc,
            List<OvalVariable> referencedVars)
    {
        if (referencedVars != null)
        {
            for (OvalVariable ov : referencedVars)
            {
                if (!newDoc.containsVariable(ov.getId()))
                {
                    newDoc.add(ov);

                    List<OvalVariable> rvars = ov.getReferencedVariables();
                    if (rvars != null && rvars.size() > 0)
                    {
                        addDependentVariables(existingDoc, newDoc, rvars);
                    }

                    List<OvalObject> robjs = ov.getReferencedObjects();
                    if (robjs != null && robjs.size() > 0)
                    {
                        addDependentObjects(existingDoc, newDoc, robjs);
                    }
                }
            }
        }
    }

    /**
     * Returns a new OvalDefinitionsDocument with definitions that references the given set of reference ids. Dependent
     * tests, objects, states, and variables are also added.
     *
     * @param refsToFind A set of id strings we are looking for.
     * @param type Produce a document of this type and stick to this type when looking at documents for definitions.
     * 
    */
    public synchronized OvalDefinitionsDocument newDefinitionsDocumentForRefs(Set<String> refsToFind, SCAPDocumentTypeEnum type)
    {
        OvalDefinitionsDocument ret = null;

        ret = (OvalDefinitionsDocument) SCAPDocumentFactory.createNewDocument(type);

        Iterator<String> keyItr = content.keySet().iterator();

        while (keyItr.hasNext())
        {
            String documentFilename = keyItr.next();

            SCAPDocument sd = content.get(documentFilename);

            if (sd instanceof OvalDefinitionsDocument)
            {
                OvalDefinitionsDocument odd = (OvalDefinitionsDocument) sd;

                if (odd.getDocumentType().equals(type))
                {
                    for (String refId : refsToFind)
                    {
                        List<OvalDefinition> defs = odd.getDefsForReferenceId(refId);

                        if (defs != null && defs.size() > 0)
                        {
                            addDependentDefs(odd, ret, defs);
                        }
                    }
                }
            }
        }

        return ret;
    }

    /**
     * Returns a new OvalDefinitionsDocument with definitions that references the given set of reference ids. Dependent
     * tests, objects, states, and variables are also added.
     *
     * @param refsToFind A set of id strings we are looking for.
     * 
    */
    
   
    public synchronized OvalDefinitionsDocument newDefinitionsDocumentForRefs(Set<String> refsToFind)
    {
        return newDefinitionsDocumentForRefs(refsToFind, SCAPDocumentTypeEnum.OVAL_511);

    }

    
    /**
     * Returns a new OvalDefinitionsDocument with definitions that references the given set of reference ids. Dependent
     * tests, objects, states, and variables are also added.  Also adds a definition to the resulting document that has
     * an extend definition reference to the definitions that were added because they matched the reference ids supplied.
     *
     * @param refsToFind A set of id strings we are looking for.
     * @param type Produce a document of this type and stick to this type when looking at documents for definitions.
     * @param di numeric portion of resulting defintion id
     * @param dn namespace portion of resulting definition id
     * 
    */
    public synchronized OvalDefinitionsDocument newDefinitionsDocumentForRefs(Set<String> refsToFind, SCAPDocumentTypeEnum type,
            String dn, String di)
    {
        OvalDefinitionsDocument odd = newDefinitionsDocumentForRefs(refsToFind, type);

        odd.setBaseId(dn);

        // add a definition to the document that includes the other definitions in the document as extendDefinition entries
        OvalDefinition newDef = odd.createDefinition(DefinitionClassEnum.VULNERABILITY);
        newDef.setId("oval:" + dn + ":def:" + di);

        odd.addDefinition(newDef, 0);

        Element criteriaElement = newDef.getElement().getChild("criteria", newDef.getElement().getNamespace());

        if (criteriaElement == null)
        {
            criteriaElement = new Element("criteria", newDef.getElement().getNamespace());
            newDef.getElement().addContent(criteriaElement);
        }

        List<OvalDefinition> defList = odd.getOvalDefinitions();

        for (OvalDefinition od : defList)
        {
            List<OvalReference> refs = od.getReferences();

            if (criteriaElement != null && !od.getId().equals(newDef.getId()))
            {
                boolean foundRef = false;

                for (Iterator<String> i = refsToFind.iterator(); i.hasNext();)
                {
                    if (od.getMetadata().containsReference(i.next()))
                    {
                        foundRef = true;
                        break;
                    }
                }
                if (foundRef)
                {
                    newDef.addReferences(refs);

                    Element edrElement = new Element("extend_definition", criteriaElement.getNamespace());
                    edrElement.setAttribute("comment", "generated comment");
                    edrElement.setAttribute("definition_ref", od.getId());
                    criteriaElement.addContent(edrElement);
                }
            }
        }

        return odd;
    }

    /**
     * Returns a new OvalDefinitionsDocument with definitions that references the given set of reference ids. Dependent
     * tests, objects, states, and variables are also added.  Also adds a definition to the resulting document that has
     * an extend definition reference to the definitions that were added because they matched the reference ids supplied.
     *
     * @param refsToFind A set of id strings we are looking for.
     * @param di numeric portion of resulting defintion id
     * @param dn namespace portion of resulting definition id
     * 
    */


    
    public synchronized OvalDefinitionsDocument newDefinitionsDocumentForRefs(Set<String> refsToFind, String dn, String di)
    {
        return newDefinitionsDocumentForRefs(refsToFind, SCAPDocumentTypeEnum.OVAL_511, dn, di);
    }

    
    /**
     * Returns list of valid test types on a given set of platforms for a specific version of oval
     *
     * @param ovalVersion Enum of oval version we want
     * @param platforms String array of platforms we are interested in
     * 
    */
    public List<NameDoc> getValidTestTypes(SCAPDocumentTypeEnum ovalVersion, String[] platforms)
    {
        List<NameDoc> allTestTypes = new ArrayList<NameDoc>();
        for (int i = 0; i < platforms.length; i++)
        {
            Map<String, List<NameDoc>> testTypesByPlatform = testTypesForOvalVersionAndPlatformType.get(ovalVersion);
            List<NameDoc> testTypes = testTypesByPlatform.get(platforms[i]);
            allTestTypes.addAll(testTypes);

        }
        Collections.sort(allTestTypes);
        return allTestTypes;
    }

    /**
     * Returns list of valid object types on a given set of platforms for a specific version of oval
     *
     * @param ovalVersion Enum of oval version we want
     * @param platforms String array of platforms we are interested in
     * 
    */
    public List<NameDoc> getValidObjectTypes(SCAPDocumentTypeEnum ovalVersion, String[] platforms)
    {
        List<NameDoc> allObjectTypes = new ArrayList<NameDoc>();
        for (int i = 0; i < platforms.length; i++)
        {
            Map<String, List<NameDoc>> objectTypesByPlatform = objectTypesForOvalVersionAndPlatformType.get(ovalVersion);
            List<NameDoc> objectTypes = objectTypesByPlatform.get(platforms[i]);
            allObjectTypes.addAll(objectTypes);
        }
        Collections.sort(allObjectTypes);
        return allObjectTypes;
    }

    /**
     * Returns list of valid state types on a given set of platforms for a specific version of oval
     *
     * @param ovalVersion Enum of oval version we want
     * @param platforms String array of platforms we are interested in
     * 
    */
    public List<NameDoc> getValidStateTypes(SCAPDocumentTypeEnum ovalVersion, String[] platforms)
    {
        List<NameDoc> allStateTypes = new ArrayList<NameDoc>();
        for (int i = 0; i < platforms.length; i++)
        {
            Map<String, List<NameDoc>> stateTypesByPlatform = stateTypesForOvalVersionAndPlatformType.get(ovalVersion);
            List<NameDoc> stateTypes = stateTypesByPlatform.get(platforms[i]);
            allStateTypes.addAll(stateTypes);
        }
        Collections.sort(allStateTypes);
        return allStateTypes;
    }

    /**
     * Returns list of valid object entity types on a given set of platforms for a specific version of oval and
     * a specific type of oval object
     *
     * @param ovalVersion Enum of oval version we want
     * @param platform The platform we are interested in
     * @param objectType String name of an oval object type
     * 
    */
    public List<OvalEntity> getValidObjectEntityTypes(SCAPDocumentTypeEnum ovalVersion, String platform, String objectType)
    {
//		LOG.debug("ovalVersion=" + ovalVersion + ", platform=" + platform + ", objectType=" + objectType);
        Map<String, Map<String, List<OvalEntity>>> platformEntityMap = objectEntityTypesForOvalVersionAndObjectType.get(ovalVersion);
        Map<String, List<OvalEntity>> objectEntityMap = platformEntityMap.get(platform);
        List<OvalEntity> objectEntityTypes = null;
        
        if(objectEntityMap != null)
        {
            objectEntityTypes = objectEntityMap.get(objectType);
        }

        return objectEntityTypes;
    }

    /**
     * Returns list of valid state entity types on a given set of platforms for a specific version of oval and
     * a specific type of oval state
     *
     * @param ovalVersion Enum of oval version we want
     * @param platform The platform we are interested in
     * @param stateType String name of an oval state type
     * 
    */
    public List<OvalEntity> getValidStateEntityTypes(SCAPDocumentTypeEnum ovalVersion, String platform, String stateType)
    {
        Map<String, Map<String, List<OvalEntity>>> platformEntityMap = stateEntityTypesForOvalVersionAndStateType.get(ovalVersion);
        Map<String, List<OvalEntity>> stateEntityMap = platformEntityMap.get(platform);
        List<OvalEntity> stateEntityTypes = stateEntityMap.get(stateType);
        return stateEntityTypes;
    }

    /**
     * Returns list of valid platforms for a specific version of oval.
     *
     * @param ovalVersion Enum of oval version we want
     * 
    */
    public List<String> getValidPlatforms(SCAPDocumentTypeEnum ovalVersion)
    {
        return platformTypesForOvalVersion.get(ovalVersion);
    }

    /**
     * Returns list of valid enum types for a specific version of oval.
     *
     * @param ovalVersion Enum of oval version we want
     * 
    */
    public Map<String, NameDoc> getEnumTypesForOvalVersion(SCAPDocumentTypeEnum ovalVersion)
    {
        return enumTypesForOvalVersion.get(ovalVersion);
    }

    /**
     * Returns list of valid enum values for a specific version of oval.
     *
     * @param ovalVersion Enum of oval version we want
     * 
    */
    public Map<String, List<NameDoc>> getEnumValuesForOvalVersion(SCAPDocumentTypeEnum ovalVersion)
    {
        return enumValuesForOvalVersion.get(ovalVersion);
    }

    /**
     * Returns list of valid object behavior values for a specific version of oval.
     *
     * @param ovalVersion Enum of oval version we want
     * 
    */
    public Map<PlatformNameKey, List<AvailableObjectBehavior>> getBehaviorValues(SCAPDocumentTypeEnum ovalVersion)
    {
        return behaviorValues.get(ovalVersion);
    }

    private void populateOVALSchemaInformation()
    {
        SchemaLocator slocator = SchemaLocator.getInstance();

        LOG.trace("Populating schema information");
        // Read xsd files from all OVAL schemas, one xsd
        // for each supported platform (eg, "aix", "linux", "solaris", "windows", etc)
        // For each schema/platform combination, record the valid OVAL test types,
        // object types, and state types. Then for each valid object or state type, record
        // the valid "entity" types, for example the registry_object can have "hive",
        // "key", and "name" as valid entity types.
        for (SCAPDocumentTypeEnum docType : SCAPDocumentTypeEnum.values())
        {
        //	LOG.debug("Processing docType " + docType);
            if (docType.name().startsWith("OVAL"))
            {
                Map<String, List<NameDoc>> testMap = new HashMap<String, List<NameDoc>>();
                Map<String, List<NameDoc>> objectMap = new HashMap<String, List<NameDoc>>();
                Map<String, List<NameDoc>> stateMap = new HashMap<String, List<NameDoc>>();
                Map<String, Map<String, List<OvalEntity>>> objectEntityMap =
                        new HashMap<String, Map<String, List<OvalEntity>>>();
                Map<String, Map<String, List<OvalEntity>>> stateEntityMap =
                        new HashMap<String, Map<String, List<OvalEntity>>>();

                List<String> ovalSchemaResourceNames = slocator.getOvalSchemaResourceFileNames(docType);
                
                List<String> platformNames = new ArrayList<String>();

                if (ovalSchemaResourceNames != null && ovalSchemaResourceNames.size() > 0)
                {
                    for (int i = 0; i < ovalSchemaResourceNames.size(); i++)
                    {
                        String schemaResourceName = ovalSchemaResourceNames.get(i);
                        String schemaFileName = schemaResourceName.substring(schemaResourceName.lastIndexOf("/")+1);
                      //  LOG.debug("Processing docType " + docType + ", schema file name " + schemaFileName);
                        String platform = null;
                        if (schemaFileName.startsWith("oval"))
                        {
                       // 	LOG.debug("Found oval xsd file name " + schemaFileName);
                            // handle both "oval-common" and "oval-definition"; all we want here are Enumerations
                            SchemaDOMParser parser = new SchemaDOMParser();
                            InputStream inputStream = slocator.getInputStream(ovalSchemaResourceNames.get(i));
                            parser.parseSchema(inputStream);
                            updateEnumData(parser, docType, "oval");
                        }
                        else if (schemaFileName.endsWith(".xsd"))
                        {
                            LOG.trace("Found xsd file name " + schemaFileName);
                            // xsd file names look like:
                            //    "solaris-definitions-schema.xsd"
                            // we want to find corresponding schema namespace, eg:
                            //    "http://oval.mitre.org/XMLSchema/oval-definitions-5#solaris"
                            int dashIndex = schemaFileName.indexOf("-definitions-schema");
                            if (dashIndex == -1)
                            {
                                continue;
                            }
                            platform = schemaFileName.substring(0, dashIndex);
                            LOG.trace("Found platform " + platform);
                            platformNames.add(platform);

                            SchemaDOMParser parser = new SchemaDOMParser();
                            parser.setPlatform(platform);
                            InputStream inputStream = slocator.getInputStream(ovalSchemaResourceNames.get(i));
                            parser.parseSchema(inputStream);

                            updateEnumData(parser, docType, platform);

                            List<NameDoc> list = parser.getValidTestsForPlatform();
                            testMap.put(platform, list);

                            list = parser.getValidObjectsForPlatform();
                            objectMap.put(platform, list);

                            list = parser.getValidStatesForPlatform();
                            stateMap.put(platform, list);

                            Map<String, List<OvalEntity>> validObjectEntities = parser.getValidObjectEntitiesByObject();
                            objectEntityMap.put(platform, validObjectEntities);

                            Map<String, List<OvalEntity>> validStateEntities = parser.getValidStateEntitiesByState();
                            stateEntityMap.put(platform, validStateEntities);

                            Map<PlatformNameKey, List<AvailableObjectBehavior>> behaviorsMap = behaviorValues.get(docType);
                            if (behaviorsMap == null)
                            {
                                behaviorsMap = new HashMap<PlatformNameKey, List<AvailableObjectBehavior>>();
                                behaviorValues.put(docType, behaviorsMap);
                            }
                            behaviorsMap.putAll(parser.getBehaviorsValuesMap());
                        }
                    }
                    LOG.trace("Found " + platformNames.size() + " platforms for version " + docType);
                    platformTypesForOvalVersion.put(docType, platformNames);
                    testTypesForOvalVersionAndPlatformType.put(docType, testMap);
                    objectTypesForOvalVersionAndPlatformType.put(docType, objectMap);
                    stateTypesForOvalVersionAndPlatformType.put(docType, stateMap);
                    objectEntityTypesForOvalVersionAndObjectType.put(docType, objectEntityMap);
                    fixEntityDatatypes(docType, objectEntityMap);
                    stateEntityTypesForOvalVersionAndStateType.put(docType, stateEntityMap);
                    fixEntityDatatypes(docType, stateEntityMap);
                }
            }
        }
    }

    private void updateEnumData(SchemaDOMParser parser, SCAPDocumentTypeEnum docType, String platform)
    {
        // first update List of NameDocs for the Enumeration names themselves
        Map<String, NameDoc> parsedEnumNameMap = parser.getEnumerationNameMap();
        Map<String, NameDoc> enumNamesForDocType = enumTypesForOvalVersion.get(docType);
        if (enumNamesForDocType == null)
        {
            enumNamesForDocType = new HashMap<String, NameDoc>();
            enumTypesForOvalVersion.put(docType, enumNamesForDocType);
        }
        enumNamesForDocType.putAll(parsedEnumNameMap);
        Map<String, List<NameDoc>> parsedEnumValueMap = parser.getEnumerationValueMap();
        Map<String, List<NameDoc>> enumValuesForDocType = enumValuesForOvalVersion.get(docType);
        if (enumValuesForDocType == null)
        {
            enumValuesForDocType = new HashMap<String, List<NameDoc>>();
            enumValuesForOvalVersion.put(docType, enumValuesForDocType);
        }
        enumValuesForDocType.putAll(parsedEnumValueMap);
    }

    private void fixEntityDatatypes(SCAPDocumentTypeEnum docType, Map<String, Map<String, List<OvalEntity>>> platformEntityMap)
    {
        Set<String> platformKeys = platformEntityMap.keySet();
        Iterator<String> platformKeyIter = platformKeys.iterator();
        while (platformKeyIter.hasNext())
        {
            String key = platformKeyIter.next();
            Map<String, List<OvalEntity>> entityValueMap = platformEntityMap.get(key);
            Set<String> entityKeys = entityValueMap.keySet();
            Iterator<String> entityKeyIter = entityKeys.iterator();
            while (entityKeyIter.hasNext())
            {
                String entityKey = entityKeyIter.next();
                List<OvalEntity> entityList = entityValueMap.get(entityKey);
                for (int iEntity = 0; iEntity < entityList.size(); iEntity++)
                {
                    OvalEntity entity = entityList.get(iEntity);
                    String schemaType = entity.getDatatypeString();
                    int colonOffset = schemaType.indexOf(":");
                    if (colonOffset != -1)
                    {
                        schemaType = schemaType.substring(colonOffset + 1);
                    }
                    Map<String, List<NameDoc>> map = getEnumValuesForOvalVersion(docType);
                    OvalDatatype dataType = new OvalDatatype(schemaType, map);
                    entity.setDatatype(dataType);
                }
            }
        }
    }

    /**
     * A main method only used for testing
     *
     * @param args Command-line arguments
     * 
    */
    public static void main(String[] args) throws Exception
    {
        BasicConfigurator.configure();
        LOG.trace("SchemaParser starting, schemaVersion =" + args[0] + ", xsd File=" + args[1]);
        File xsdFile = new File(args[1]);
        if (!xsdFile.exists())
        {
            LOG.error("Usage: java SchemaParser <name of xsd file>");
        }
        SCAPDocumentTypeEnum version = SCAPDocumentTypeEnum.valueOf(args[0]);
        SCAPContentManager mgr = SCAPContentManager.getInstance(new File(args[1]));

        // schema-related data should now be populated
        List<String> platformTypes = mgr.getValidPlatforms(version);
        LOG.trace("STARTING PLATFORMS");
        for (int i = 0; i < platformTypes.size(); i++)
        {
            LOG.trace(platformTypes.get(i));
        }

        String[] platforms =
        {
            "windows", "independent"
        };

        List<NameDoc> list = null;
        Iterator<NameDoc> iter = null;
        LOG.trace("STARTING TESTS");
        list = mgr.getValidTestTypes(version, platforms);
        iter = list.iterator();
        while (iter.hasNext())
        {
            NameDoc testName = iter.next();
            LOG.trace("\t" + testName);
        }
        LOG.trace("STARTING OBJECTS");
        list = mgr.getValidObjectTypes(version, platforms);
        iter = list.iterator();
        while (iter.hasNext())
        {
            NameDoc objName = iter.next();
            LOG.trace("\t" + objName);
            LOG.trace("STARTING OBJECT ENTITIES FOR " + objName.getName());
            List<OvalEntity> entityList = mgr.getValidObjectEntityTypes(version, "windows", objName.getName());
            if (entityList != null && entityList.size() > 0)
            {
                for (int i = 0; i < entityList.size(); i++)
                {
                    LOG.trace("\t\t" + entityList.get(i).toString());
                }
            }
        }

        LOG.trace("STARTING STATES");
        list = mgr.getValidStateTypes(version, platforms);
        iter = list.iterator();
        while (iter.hasNext())
        {
            NameDoc stateName = iter.next();
            LOG.trace("\t" + stateName);
            LOG.trace("STARTING STATE ENTITIES FOR " + stateName.getName());
            List<OvalEntity> entityList = mgr.getValidStateEntityTypes(version, "windows", stateName.getName());
            if (entityList != null && entityList.size() > 0)
            {
                for (int i = 0; i < entityList.size(); i++)
                {
                    LOG.trace("\t\t" + entityList.get(i).toString());
                }
            }
        }
    }

    /**
     * Removes a loaded document from the in-memory map.
     * File could still exist on disk.
     *
     * @param absFilename Path of file
     * 
    */
    public synchronized void removeDocument(String absFilename)
    {
    	SCAPDocument doc = content.get(absFilename);
    	if(doc != null)
    	{
    		doc.tearDown();
    		
    		content.remove(absFilename);
    	}
    }

    /**
     * Removes all loaded documents from the in-memory map.
     */
    public synchronized void removeAllDocuments()
    {
    	if(content == null || content.size() == 0)
    	{
    		return;
    	}
    	
    	Set<String> filenames = new HashSet<String>();
    	filenames.addAll(content.keySet());
    	
    	Iterator<String> keyItr = filenames.iterator();
    	
    	while(keyItr.hasNext())
    	{
    		String filename = keyItr.next();
    		removeDocument(filename);
    	}
    }
    
    /**
     * Adds document to the in-memory map.
     *
     * @param absFilename Path of file
     * @param doc The loaded document object
     * 
    */
    public synchronized void addDocument(String absFilename, SCAPDocument doc)
    {
        content.put(absFilename, doc);
    }
 
    /**
     * Use to copy the file on disk for a given scap document.  The new file can
     * be in a different directory all together.  This is mainly intended to be used
     * by the SCAPDocumentBundle code.  Any new directories specified in the new file's 
     * path will be created. 
     * @param doc
     * @param newFilename
     */
    public void copy(SCAPDocument doc, File newFilename) throws IOException
    {
    	String oldFilename = doc.getFilename();
    	
    	File newParent = newFilename.getParentFile();
    	
    	if(!newParent.exists())
    	{
    		try
    		{
    			newParent.mkdirs();
    		}
    		catch(Exception e)
    		{
    			throw new IllegalStateException("Unable to rename SCAP document "
    					+ oldFilename + " to " + newFilename.getAbsolutePath()
    					+ ": Unable to create distination parent directory or directories", e);
    		}
    	}
    	
    	// remove the document from the content map
    	removeDocument(oldFilename);
    	
    	// set the new name in the document
    	doc.setFilename(newFilename.getAbsolutePath());
    	
    	// add the document to the content map under the new name;
    	addDocument(doc.getFilename(), doc);
    	
    	// save the document in the new location
    	doc.save();    	
    }

	public List<File> getOvalFiles(File dir) {
		ArrayList<File> ovalFileList = new ArrayList<File>();
		// get list of all xml files in dir
		File[] xmlFiles = dir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".xml");
			}
		});
		if (xmlFiles != null && xmlFiles.length > 0)
		{
			try
            {
				SAXParserFactory saxfactory = SAXParserFactory.newInstance();
				saxfactory.setValidating(false);
                saxfactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                saxfactory.setFeature("http://xml.org/sax/features/validation", false);
                
				for (int i=0; i<xmlFiles.length; i++)
				{
					SAXParser saxparser = saxfactory.newSAXParser();
					XMLReader xmlReader = saxparser.getXMLReader();
					OvalParser ovalParser = new OvalParser();
					xmlReader.setContentHandler(ovalParser);
					FileInputStream inStream = new FileInputStream(xmlFiles[i]);
					xmlReader.parse(new InputSource(inStream));
					if (ovalParser.isOval())
					{
						ovalFileList.add(xmlFiles[i]);
					}
				}
            }
            catch(Exception e)
            {
                throw new IllegalStateException("Caught an error trying list oval files in directory " + dir.getAbsolutePath(), e);
            }
		}
		return ovalFileList;
	}

	private class OvalParser extends ParserAbstract {
		boolean oval = false;
		@Override
		public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException
		{
			if (name.equals("oval_definitions"))
			{
				oval = true;
			}
		}
		public boolean isOval()
		{
			return oval;
		}
	}

	/**
	 * Set instance to null and clear any maps.  Be careful where you use this!
	 */
	public void reset()
	{
		content.clear();
		content = null;
		
		instance = null;
	}

    /**
     * Go find and load the offical CPE dictionary from the filesystem.  This also loads the oval file
     * that contains the checks for the dictionary entries.
     *
     * @return SCAPDocumentBundle
     */
	
	

    public SCAPDocumentBundle getOffcialCPEDictionary()
    {
        File f = new File("logs/official-cpe-dictionary_v2.3.xml");
        f.deleteOnExit();
    	synchronized(cpeHold)
        {
            if(officalCPEBundle != null)
            {
                return officalCPEBundle;
            }

            CPEDictionaryDocument cpedoc = null;
            OvalDefinitionsDocument ovaldoc = null;

            String dictFilename = LibraryConfiguration.getInstance().getOfficialCPEDictionaryPath();
           // String ovalFilename = LibraryConfiguration.getInstance().getOfficialCPEOvalPath();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("official-cpe-dictionary_v2.3.xml");
            File dictFile = new File("logs/official-cpe-dictionary_v2.3.xml");
        	OutputStream outputStream;
			try {
				outputStream = new FileOutputStream(dictFile);
				IOUtils.copy(is, outputStream);
	        	outputStream.close();
	        	is.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        

           // File ovalFile = new File(ovalFilename);
          //  System.out.println(ovalFile.exists());         
           // System.out.println(ovalFile.isFile());

            try
            {
                cpedoc = (CPEDictionaryDocument) SCAPDocumentFactory.loadDocument(dictFile);
           //     ovaldoc = (OvalDefinitionsDocument) SCAPDocumentFactory.loadDocument(ovalFile);
                
                officalCPEBundle = new SCAPDocumentBundle();
                officalCPEBundle.setBundleType(SCAPBundleType.STREAM);
                officalCPEBundle.setReadOnly(true);

                officalCPEBundle.setFilename(dictFile.getAbsolutePath());

                officalCPEBundle.addDocument(cpedoc);
             //   officalCPEBundle.addDocument(ovaldoc);
                dictFile.delete();
            }
            catch(Exception e)
            {
                LOG.error("Error occurred opening offical CPE dictionary SCAP Data Stream", e);
                dictFile.delete();

            }
          dictFile.delete();

        }
        return officalCPEBundle;
    }
    
    public List<String> getOfficalCPEPlatformNames() {
        synchronized(cpeHold) {
            if (officalCPEPlatformNames == null) {
                initOfficialCPENamesAndTitles();
            }
            return officalCPEPlatformNames;
        }
    }

    public List<String> getOfficalCPEPlatformTitles() {
        synchronized(cpeHold) {
            if (officalCPEPlatformTitles == null) {
                initOfficialCPENamesAndTitles();
            }
            return officalCPEPlatformTitles;
        }
    }

    public List<String> getOfficalCPEProductNames() {
        synchronized(cpeHold) {
            if (officalCPEProductNames == null) {
                initOfficialCPENamesAndTitles();
            }
            return officalCPEProductNames;
        }
    }

    public List<String> getOfficalCPEProductTitles() {
        synchronized(cpeHold) {
            if (officalCPEProductTitles == null) {
                initOfficialCPENamesAndTitles();
            }
            return officalCPEProductTitles;
        }
    }

    private void initOfficialCPENamesAndTitles() {
        getOffcialCPEDictionary();
        officalCPEPlatformNames = new ArrayList<String>();
        officalCPEPlatformTitles = new ArrayList<String>();
        officalCPEProductNames = new ArrayList<String>();
        officalCPEProductTitles = new ArrayList<String>();
        List<CPEDictionaryDocument> docList = officalCPEBundle.getCPEDictionaryDocs();
        CPEDictionaryDocument doc = docList.get(0);
        List<CPEItem> allItems = doc.getItems();
        for (CPEItem item : allItems) {
            String itemName = item.getName(); // this is "cpe:something"
            List<CPEItemTitle> itemTitleList = item.getTitles();
            String itemTitle = getTitle(itemTitleList);
            if (itemName.startsWith("cpe:/a")) {
                officalCPEProductNames.add(itemName);
                officalCPEProductTitles.add(itemTitle);
            } else if (itemName.startsWith("cpe:/o")) {
                officalCPEPlatformNames.add(itemName);
                officalCPEPlatformTitles.add(itemTitle);
            }
        }
    }

    private String getTitle(List<CPEItemTitle> itemList) {
        String title = null;
        for (CPEItemTitle itemTitle : itemList) {
            if (title == null) {
                title = itemTitle.getText();
            }
            String lang = itemTitle.getLang();
            if (lang.equalsIgnoreCase("en-US")) {
                // if more than one title, they should disagree only in lang; prefer the en-us version
                title = itemTitle.getText();
            }
        }
        return title;
    }
}
