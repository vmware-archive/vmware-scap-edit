package com.g2inc.scap.library.schema;
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
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;

/**
 * This class handles finding all local schema(.xsd) files for use
 * in off-line validation of documents.
 * 
 * This class is a singleton and as such is called once to initialize an a
 * single instance of the class which will hold a map of the discovered
 * schema files for later run-time use.
 * 
 */
public class SchemaLocator
{
    private static final Logger log = Logger.getLogger(SchemaLocator.class);

    // static variables
    private static SchemaLocator instance = null;

    public static final String XCCDF_NAMESPACE =  "http://checklists.nist.gov/xccdf/1.1";
    public static final String OVAL5_DEF_NAMESPACE = "http://oval.mitre.org/XMLSchema/oval-definitions-5";
    public static final String CPE20_NAMESPACE = "http://cpe.mitre.org/dictionary/2.0";
    public static final String XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml xhtml.xsd";
    public static final String XMLDSIG_NAMESPACE = "http://www.w3.org/2000/09/xmldsig#";
    public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String DUBLIN_CORE_1_1_SCHEMA = "http://purl.org/dc/elements/1.1/";

    // instance variables
    private static HashMap<String, ArrayList<String>> schemaNames = new HashMap<String, ArrayList<String>>();
    private static HashMap<String, ArrayList<String>> schemaNameByNamespace = new HashMap<String, ArrayList<String>>();
    private static final Map<String, String> resourcePathMap = new HashMap<String, String>();
    
    // a list of supported dublin core elements and the order in which they are required.
    private List<String> dublinCoreElements = new ArrayList<String>();

    static {
        /* "ischema" folder must be in the classpath */
        getInstance();
        try {
            instance.populateResourcePathMap("ischema/");
        	instance.populateDublinCoreSchemaInformation();
        } catch (IOException e) {
            log.error(e);
        } catch (URISyntaxException e) {
            log.error(e);
        }
    }    
    
    public InputStream getInputStreamForNamespace(String nameSpace, SCAPDocumentTypeEnum docType) {
        InputStream is = null;
        ArrayList<String> resourceNames = schemaNames.get(nameSpace);
        if (resourceNames == null || resourceNames.size() == 0) {
            throw new IllegalStateException("Can't find schema for unknown namespace:" + nameSpace);
        }
        String resourceName = null;
        if (resourceNames.size() == 1) {
            resourceName = resourceNames.get(0);
        } else {
            // more than one resource name is available; use document type to choose the right one
            for (String resource : resourceNames) {
                switch (docType) {
                case OVAL_53:
                    if (resource.indexOf("ischema/oval53") != -1) {
                        resourceName = resource;
                    }
                    break;
                case OVAL_54:
                    if (resource.indexOf("ischema/oval54") != -1) {
                        resourceName = resource;
                    }
                    break;
                case OVAL_55:
                    if (resource.indexOf("ischema/oval55") != -1) {
                        resourceName = resource;
                    }
                    break;
                case OVAL_56:
                    if (resource.indexOf("ischema/oval56") != -1) {
                        resourceName = resource;
                    }
                    break;
                case OVAL_57:
                    if (resource.indexOf("ischema/oval57") != -1) {
                        resourceName = resource;
                    }
                    break;
                case OVAL_58:
                    if (resource.indexOf("ischema/oval58") != -1) {
                        resourceName = resource;
                    }
                    break;     
                case OVAL_59:
                    if (resource.indexOf("ischema/oval59") != -1) {
                        resourceName = resource;
                    }
                    break;         
                case OVAL_510:
                    if (resource.indexOf("ischema/oval510") != -1) {
                        resourceName = resource;
                    }
                    break;   
                case OVAL_60:
                    if (resource.indexOf("ischema/oval60") != -1) {
                        resourceName = resource;
                    }
                    break;
                default:
                    resourceName = resourceNames.get(0);
                }
            }
        }
        if (resourceName != null) {
            is = getInputStream(resourceName);
        }
        return is;
    }

    public InputStream getSchemaInputStreamByTargetNamespace(String targetUri, SCAPDocumentTypeEnum docType) 
    {
//    	log.debug("getSchemaInputStreamByTargetNamespace called with targetUri " + targetUri + ", docType " + docType);
        InputStream is = null;
        ArrayList<String> xsdNames = schemaNameByNamespace.get(targetUri);
        if (xsdNames == null || xsdNames.size() == 0) {
            throw new IllegalStateException("Can't find schema for unknown namespace:" + targetUri);
        }
        String resourceName = null;
        if (xsdNames.size() == 1) {
            resourceName = xsdNames.get(0);
        } else {
            // more than one resource name is available; use document type to choose the right one
            for (String resource : xsdNames) {
                switch (docType) {
                case OVAL_53:
                    if (resource.indexOf("ischema/oval53") != -1) {
                        resourceName = resource;
                    }
                    break;
                case OVAL_54:
                    if (resource.indexOf("ischema/oval54") != -1) {
                        resourceName = resource;
                    }
                    break;
                case OVAL_55:
                    if (resource.indexOf("ischema/oval55") != -1) {
                        resourceName = resource;
                    }
                    break;
                case OVAL_56:
                    if (resource.indexOf("ischema/oval56") != -1) {
                        resourceName = resource;
                    }
                    break;
                case OVAL_57:
                    if (resource.indexOf("ischema/oval57") != -1) {
                        resourceName = resource;
                    }
                    break;
                case OVAL_58:
                    if (resource.indexOf("ischema/oval58") != -1) {
                        resourceName = resource;
                    }
                    break;   
                case OVAL_59:
                    if (resource.indexOf("ischema/oval59") != -1) {
                        resourceName = resource;
                    }
                    break;    
                case OVAL_510:
                    if (resource.indexOf("ischema/oval510") != -1) {
                        resourceName = resource;
                    }
                    break;                       
                case OVAL_60:
                    if (resource.indexOf("ischema/oval60") != -1) {
                        resourceName = resource;
                    }
                    break;
                default:
                    resourceName = xsdNames.get(0);
                }
            }
        }
        if (resourceName != null) {
            is = getInputStream(resourceName);
        }
        return is;
    }

    public InputStream getInputStream(String url) {
        InputStream is = null;
        String resPath = url;
        if (url.startsWith("classpath:")) {
            resPath = url.substring("classpath:/".length());
        }
        try {
            ClassLoader loader =  Thread.currentThread().getContextClassLoader();
            URL urlFound = loader.getResource(resPath);
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resPath);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    private SchemaLocator()
    {
    }
    
    private static class FileListing implements Enumeration<String> {
        private LinkedList<File> dirs;

        private File[] files;
        private int i = 0;

        FileListing(File basedir) {
            dirs = new LinkedList<File>();
            files = basedir.listFiles();
            nextFile();
        }

        @Override
        public boolean hasMoreElements() {
            return i < files.length;
        }

        @Override
        public String nextElement() {
            File result = files[i++];
            nextFile();
            return result.getPath();
        }

        private void nextFile() {
            while (true) {
                if (i >= files.length) {
                    if (dirs.size() > 0) {
                        files = dirs.pop().listFiles();
                        i = 0;
                    } else {
                        // no more directories
                        break;
                    }
                } else if (files[i].isDirectory()) {
                    dirs.add(files[i++]);
                } else {
                    break;
                }
            }
        }
    }

    private static class JarListing implements Enumeration<String> {
        private Enumeration<JarEntry> entries;
        private String next = null;
        private String baseDir;

        JarListing(String jarPath, String baseDir) throws IOException {
            JarFile jar = new JarFile(jarPath);
            // enumerate all entries in the jar
            entries = jar.entries();
            this.baseDir = baseDir;
            nextValidEntry();
        }

        @Override
        public boolean hasMoreElements() {
            return next != null;
        }

        @Override
        public String nextElement() {
            String result = next;
            nextValidEntry();
            return result;
        }

        private void nextValidEntry() {
            next = null;
            while (entries.hasMoreElements() && next == null) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                String name = entry.getName();
                // filter according to the path
                if (name.startsWith(baseDir)) {
                    next = name;
                }
            }
        }
    }
    
    private void processFile(String path, boolean classPath) {
        String key;
//        log.debug("processFile called with path " + path + ", classpath " + classPath);
        path = path.replaceAll("\\\\", "/");
        int ischemaStart = path.indexOf("ischema/");
        if (ischemaStart == -1) {
            throw new IllegalStateException("Unexpected path:" + path);
        }
        path = path.substring(ischemaStart);
        
        key = path.substring(path.lastIndexOf('/') + 1);

        // store the names
        if (resourcePathMap.containsKey(key)) {
            log.error("duplicate key: " + key);
        }
        if (classPath) {
            path = "classpath:/" + path;
        }
        ArrayList<String> existingUrlList = schemaNames.get(key);
        if (existingUrlList == null) {
            existingUrlList = new ArrayList<String>();
            schemaNames.put(key, existingUrlList);
        }
        existingUrlList.add(path);
        InputStream inputStream = getInputStream(path);
        if (path.endsWith(".xsd")) {
            parseInputStream(path, inputStream);
        }
    }    
    
    private void populateResourcePathMap(String name)
    throws IOException, URISyntaxException {
//    	log.debug("populateResourcePathMap called with " + name);
        Enumeration<String> listing;
        URL url = SchemaLocator.class.getClassLoader()
                .getResource(name);
        boolean classPath = false;
        if (url != null && url.getProtocol().equals("file")) {
            // A file path
            listing = new FileListing(new File(url.toURI()));
        } else if (url != null && url.getProtocol().equals("jar")) {
            // strip out only the JAR file
            String jarPath = url.getPath().substring(5,
                    url.getPath().indexOf("!"));
            listing = new JarListing(URLDecoder.decode(jarPath, "UTF-8"), name);
            classPath = true;
        } else {
            throw new UnsupportedOperationException(
                    "Cannot list files for path " + name);
        }
        while (listing.hasMoreElements()) {
            String element = listing.nextElement();
            if (element.endsWith(".xsd") || element.endsWith(".dtd") ){
                processFile(element, classPath);
            }
        }
    }    
    
    /**
     * Parse a schema file just to fill in all the namespace -> resource name mappings.
     * 
     * @param inputStream
     */
    private void parseInputStream(String name, InputStream inputStream) {
//    	log.debug("parseInputStream called with name " + name);
        SAXParserFactory saxfactory = SAXParserFactory.newInstance();
        saxfactory.setValidating(false);
        saxfactory.setNamespaceAware(true);

        SAXParser saxparser = null;

        try
        {
            saxfactory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            saxfactory.newSAXParser();
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Caught an error trying to create new SAXParser!", e);
        }

        if(saxfactory != null)
        {
            try
            {
                saxparser = saxfactory.newSAXParser();
                XMLReader xmlreader = saxparser.getXMLReader();
                SchemaLocatorParser handler = new SchemaLocatorParser();
                final String path = name;

                EntityResolver er = new EntityResolver()
                {
                    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
                    {
                        InputSource source = null;
                        InputStream inputStream = null;
                        String resourceName = null;
                        if(systemId.endsWith("XMLSchema.dtd"))
                        {
                            resourceName = "ischema/XMLSchema/XMLSchema.dtd";
                        }
                        else if(systemId.endsWith("xml.xsd"))
                        {
                            resourceName = "ischema/XMLSchema/xml.dtd";
                        }
                        else if(systemId.endsWith("xhtml.xsd"))
                        {
                            resourceName = "ischema/xhtml/xhtml.xsd";
                        }
                        else if(systemId.endsWith("datatypes.dtd"))
                        {
                            resourceName = "ischema/XMLSchema/datatypes.dtd";
                        }

                        if(resourceName != null)
                        {
                            inputStream = getInputStream(resourceName);
                            source = new InputSource(inputStream);
                        }
                        else
                        {
                            throw new IllegalStateException("Entity not resolved!\n"
                                    + "publicId = " + publicId + "\n"
                                    + "systemId = " + systemId);
                        }
                        return source;
                    }
                };

                if(name.endsWith("xml.xsd")
                        || name.endsWith("xhtml.xsd"))
                {
                    xmlreader.setEntityResolver(er);
                }
                
                xmlreader.setContentHandler(handler);
                xmlreader.setErrorHandler(handler);

                xmlreader.parse(new InputSource(inputStream));

                inputStream.close();

                String targetNamespace = handler.getFileNSURI();
//                log.debug("targetNamespace = " + targetNamespace);
                if(targetNamespace != null)
                {
                    ArrayList<String> schemaXSDs = null;

                    if(schemaNameByNamespace.containsKey(targetNamespace))
                    {
                        schemaXSDs = schemaNameByNamespace.get(targetNamespace);
                        if(schemaXSDs == null)
                        {
                            schemaXSDs = new ArrayList<String>();
                            schemaNameByNamespace.put(targetNamespace, schemaXSDs);
                        }
                        schemaXSDs.add(name);
//                        log.debug("added name " + name + " for targetNamespace = " + targetNamespace);
                    }
                    else
                    {
                        schemaXSDs = new ArrayList<String>();
                        schemaNameByNamespace.put(targetNamespace, schemaXSDs);

                        schemaXSDs.add(name);
//                        log.debug("added name " + name + " for new targetNamespace = " + targetNamespace);
                    }
                }
                else
                {
                    log.error("targetNamespace was null!");
                }
            }
            catch (Exception e)
            {
                throw new IllegalStateException("Caught an error trying to parse schema file " + schemaNameByNamespace, e);
            }
        }       
    }

    /**
     * No args getInstance method.  Not valid until the method is called that takes a parameter.
     *
     * @return SchemaLocator
     */
    public static synchronized SchemaLocator getInstance()
    {
        if(instance == null)
        {
            instance = new SchemaLocator();
        }

        return instance;
    }

    /**
     * Return a string suitable for use as a schemaLocation attribute value that
     * represents all of the targetNamespaces in our schemas map pointing to the latest
     * file for each. Path to the latest file will be relative, meaning no path.  This will
     * mean that the file will be looked for in the current directory.
     *
     * @param doc The scapDocument we are interested in producing a schemaLocation string for
     * @return String
     */
    public String buildSchemaLocationAttributeForExport(SCAPDocument doc)
    {
        String ret = null;
        StringBuilder sb = new StringBuilder();
        Document dom = doc.getDoc();

        // ask the dom what namespaces he references
        Element rootElement = dom.getRootElement();

        List namespaces = rootElement.getAdditionalNamespaces();

        // store the targetNamespaces actually found in the document
        ArrayList<String> docTargetNamespaces = new ArrayList<String>();
        
        if(namespaces != null)
        {
            ArrayList<Namespace> rwNamespaces = new ArrayList<Namespace>();
            rwNamespaces.addAll(namespaces);
            
            
            // add the primary namespace of the root element.  this doesn't come back as part
            // of additional namespaces.
            rwNamespaces.add(rootElement.getNamespace());

            for(int x = 0; x < rwNamespaces.size();x++)
            {
                Namespace ns = (Namespace)rwNamespaces.get(x);
                String uri = ns.getURI();
                if(uri.equals("http://www.w3.org/2001/XMLSchema-instance"))
                {
                    // this is a special case, continue
                    continue;
                }
                else if(uri.equals("http://www.w3.org/1999/xhtml"))
                {
                    // special case
                    continue;
                }
                
                docTargetNamespaces.add(uri);

                // check our schemas map to see if we've loaded a schema for this namespace.
                if(!schemaNameByNamespace.containsKey(uri))
                {
                    // we don't have this schema
                    throw new IllegalStateException("Unknown schema namespace " + uri);
                }
            }
        }

    	SchemaLocationAttribute schemaLocAtt = new SchemaLocationAttribute();

    	// loop through for each namespace referenced and add an entry
    	// to the schemaLocationAttribute
        for(int x = 0; x < docTargetNamespaces.size(); x++)
        {
            String nsURI = docTargetNamespaces.get(x);
            ArrayList<String> resourceNames = schemaNameByNamespace.get(nsURI);
            if (resourceNames.size() == 0) {
                // we don't have this schema
                throw new IllegalStateException("No local copy of the schema for namespace " + nsURI);
            }
            // Assumption here: all xsd file names are the same for any particular namespace,
            // regardless of the oval version; for example the namespace:
            // "http://oval.mitre.org/XMLSchema/oval-definitions-5#windows"
            // corresponds to filename windows-definitions-schema.xsd regardless of the 
            // oval version. Version-unique xsd files are in resources with version-unique 
            // resource names, eg, "ischema/oval53/windows-definitions-schema.xsd"
            //
            // Thus we can use the filename part of the resource name in the schema location,
            // assuming that the version-appropriate xsd file will be in the same directory as
            // the xml file to be validated, if the user wants to validate the exported file 
            // using another tool, such as Oxygen.
            String resourceName = resourceNames.get(0);
            // isolate the filename part of the resource name
            int lastSlashIndex = resourceName.lastIndexOf("/");
            if (lastSlashIndex == -1) {
                throw new IllegalStateException("Unexpected resource name " 
                    + resourceName + " for namespace " + nsURI);
            }
            String filenameOnly = resourceName.substring(lastSlashIndex + 1);
            schemaLocAtt.addNamespace(nsURI, filenameOnly);
        }        

        ret = schemaLocAtt.toString();
        return ret;
    }

    /**
     * Get all the schema files associated with the given version of oval.
     *
     * @param ovalVer The version of OVAL we are interested in
     *
     * @return List<File>
     */
    public List<String> getOvalSchemaResourceFileNames(SCAPDocumentTypeEnum ovalVer)
    {
        List<String> resourceNames = new ArrayList<String>();
        if(!ovalVer.toString().startsWith("OVAL"))
        {
            throw new IllegalStateException("This method supports only documents of type OVAL");
        }
        ArrayList<String> keys = new ArrayList<String>();
        keys.addAll(schemaNames.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            ArrayList<String> resources = schemaNames.get(key);
            for (String resource : resources) {
                if (resource.indexOf("ischema/oval") != -1) {
                    switch (ovalVer) {
                    case OVAL_53:
                        if (resource.indexOf("ischema/oval53") != -1) {
                            resourceNames.add(resource);
                        }
                        break;
                    case OVAL_54:
                        if (resource.indexOf("ischema/oval54") != -1) {
                            resourceNames.add(resource);
                        }
                        break;
                    case OVAL_55:
                        if (resource.indexOf("ischema/oval55") != -1) {
                            resourceNames.add(resource);
                        }
                        break;
                    case OVAL_56:
                        if (resource.indexOf("ischema/oval56") != -1) {
                            resourceNames.add(resource);
                        }
                        break;
                    case OVAL_57:
                        if (resource.indexOf("ischema/oval57") != -1) {
                            resourceNames.add(resource);
                        }
                        break;
                    case OVAL_58:
                        if (resource.indexOf("ischema/oval58") != -1) {
                            resourceNames.add(resource);
                        }
                        break;  
                    case OVAL_59:
                        if (resource.indexOf("ischema/oval59") != -1) {
                            resourceNames.add(resource);
                        }
                        break;  
                    case OVAL_510:
                        if (resource.indexOf("ischema/oval510") != -1) {
                            resourceNames.add(resource);
                        }
                        break;       
                    case OVAL_60:
                        if (resource.indexOf("ischema/oval60") != -1) {
                            resourceNames.add(resource);
                        }
                        break;
                    }
                }
            }
        }
        return resourceNames;
    }

    // load the list of supported dublin core elements
    private void populateDublinCoreSchemaInformation()
    {
        InputStream dcSchemaStream = getSchemaInputStreamByTargetNamespace(SchemaLocator.DUBLIN_CORE_1_1_SCHEMA, SCAPDocumentTypeEnum.UNKNOWN);

        Document dcDom = null;
        SAXBuilder builder = new SAXBuilder();

        try
        {
            dcDom = builder.build(dcSchemaStream);

            List contentList = dcDom.getRootElement().getChildren();

            if(contentList != null && contentList.size() > 0)
            {
                for(Object o : contentList)
                {
                    if(o instanceof Element)
                    {
                        Element ele = (Element) o;

                        String eleName = ele.getName();

                        if(eleName != null && eleName.equals("group"))
                        {
                            String nameAttVal = ele.getAttributeValue("name");

                            if(nameAttVal == null || !nameAttVal.equals("elementsGroup"))
                            {
                                continue;
                            }
                        
                            List groupChildren = ele.getChildren();
                            Element sequenceEle = null;
                            
                            if(groupChildren != null && groupChildren.size() > 0)
                            {
                                for(Object groupChild : groupChildren)
                                {
                                    if(groupChild instanceof Element)
                                    {
                                        sequenceEle = (Element) groupChild;
                                        break;
                                    }
                                }
                                groupChildren.clear();
                            }

                            if(sequenceEle == null)
                            {
                                continue;
                            }

                            List sequenceChildren = sequenceEle.getChildren();
                            Element choiceEle = null;

                            if(sequenceChildren != null && sequenceChildren.size() > 0)
                            {
                                for(Object sequenceChild : sequenceChildren)
                                {
                                    if(sequenceChild instanceof Element)
                                    {
                                        choiceEle = (Element) sequenceChild;
                                        break;
                                    }
                                }
                                sequenceChildren.clear();
                            }
                            
                            if(choiceEle == null)
                            {
                                continue;
                            }

                            List dcElements = choiceEle.getContent();

                            if(dcElements != null && dcElements.size() > 0)
                            {
                                for(Object dco : dcElements)
                                {
                                    if(dco instanceof Element)
                                    {
                                        Element dce = (Element) dco;

                                        dublinCoreElements.add(dce.getAttributeValue("ref"));
                                    }
                                }

                                break;
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            log.error("Unable to load the Dublin Core schema!", e);
            return;
        }
    }

    /**
     * Return a list of dublin core elements supported and the order in which they are required.
     * 
     * @return List<String>
     */
    public List<String> getSupportedDubinCoreElements()
    {
        List<String> ret = new ArrayList<String>(dublinCoreElements.size());
        ret.addAll(dublinCoreElements);

        return ret;
    }    
}
