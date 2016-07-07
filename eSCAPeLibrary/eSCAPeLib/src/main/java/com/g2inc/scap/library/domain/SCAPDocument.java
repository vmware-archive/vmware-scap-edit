package com.g2inc.scap.library.domain;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.EnumMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.g2inc.scap.library.content.style.ContentStyle;
import com.g2inc.scap.library.domain.bundle.SCAPDocumentBundle;
import com.g2inc.scap.library.domain.scap12.Scap12DataStreamCollection;
import com.g2inc.scap.library.parsers.ParserAbstract;
import com.g2inc.scap.library.schema.SchemaLocator;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import javax.xml.bind.JAXBException;
import org.jdom.JDOMException;

/**
 * This class represents any document that is loadable into the
 * SCAPContentManager.
 *
 * @author ssill2
 * @see com.g2inc.scap.library.domain.SCAPContentManager
 */
public abstract class SCAPDocument extends SCAPElementImpl implements SCAPDocumentType {

    private static final Logger LOG = Logger.getLogger(SCAPDocument.class);
    private static final String SCHEMA_LANG = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    protected SCAPDocumentTypeEnum documentType = null;
    protected String filename = null;
    protected boolean dirty = false;
    protected ContentStyle contentStyle = null;

    // enums below are for monitoring CPU consumption using one of two search strategies. 
    public enum SearchStrategy {

        OLD, NEW
    };
    protected SearchStrategy strategy;

    protected enum CollType {

        DEFINITIONS, TESTS, OBJECTS, STATES, VARIABLES, CPE_ITEMS
    };
    
    private EnumMap<CollType, CollStat> collMap = new EnumMap<CollType, CollStat>(CollType.class);
    protected Scap12DataStreamCollection dataStreamCollection = null;

    public Scap12DataStreamCollection getDataStreamCollection() {
        return dataStreamCollection;
    }

    public void setDataStreamCollection(Scap12DataStreamCollection dataStreamCollection) {
        this.dataStreamCollection = dataStreamCollection;
    }

    public ContentStyle getContentStyle() {
        return contentStyle;
    }

    public void setContentStyle(ContentStyle contentStyle) {
        this.contentStyle = contentStyle;
    }
    protected SCAPDocumentClassEnum documentClass = null;
    // this will be non-null if we are part of a bundle
    protected SCAPDocumentBundle bundle = null;

    /**
     * Constructor that takes a JDOM Document object.
     *
     * @param doc JDOM Document object
     */
    public SCAPDocument(Document doc) {
        super();
        String stategyString = System.getProperty("search.strategy", "OLD");
        try {
            strategy = SearchStrategy.valueOf(stategyString);
        } catch (Throwable t) {
            strategy = SearchStrategy.OLD;
        }
        setRoot(doc.getRootElement());
        setElement(getRoot());
        setDoc(doc);
        setSCAPDocument(this);
    }

    /**
     * Default no args constructor.
     */
    public SCAPDocument() {
        super();
    }

    /**
     * Returns the type of this document.
     *
     * @return SCAPDocumentTypeEnum
     */
    public SCAPDocumentTypeEnum getDocumentType() {
        return documentType;
    }

    /**
     * Sets the type of this document.
     *
     * @param documentType The type of this document
     */
    public void setDocumentType(SCAPDocumentTypeEnum documentType) {
        this.documentType = documentType;
    }

    /**
     * Get the path of this document on disk.
     *
     * @return String
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Return the implementation class for a given interface; used to produce
     * SCAPElements or Lists of SCAPElements.
     *
     * @param intClass Class of interface, eg, Group.class
     * @return Class of corresponding implementation, eg, GroupImpl.class
     */
    @Override
    public Class<? extends SCAPElementImpl> getImplClass(Class<? extends SCAPElement> intClass) {
        throw new IllegalStateException("getImplClass not implemented");
    }

    /**
     * Sets the path of this document on disk.
     *
     * @param filename Absolute path of this document on disk.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Saves this document to a temporary location on disk and tries to do a
     * validating parse on it. This will return any validation errors it finds.
     *
     * @return String
     */
    public String validate() {
        String errorMessage = null;

        // save the document to a temporary file, so we can do a validating parse
        File tempFile = null;

//    final File dir;

        try {
            tempFile = File.createTempFile("Validating", ".xml", null);

            FileOutputStream fos = new FileOutputStream(tempFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
            BufferedWriter bw = new BufferedWriter(osw);

            XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
            xmlo.output(getDoc(), bw);

            bw.flush();
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            errorMessage = "Validate could not populate temporary file";
            LOG.error(errorMessage, e);
            return errorMessage;
        }

        try {
            SAXParserFactory saxfactory = SAXParserFactory.newInstance();
            saxfactory.setValidating(true);
            saxfactory.setFeature("http://xml.org/sax/features/validation", true);
            saxfactory.setFeature("http://apache.org/xml/features/validation/schema", true);
            saxfactory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);

            saxfactory.setNamespaceAware(true);

            SAXParser saxparser = saxfactory.newSAXParser();

            saxparser.setProperty(SCHEMA_LANG, XML_SCHEMA);

            final SchemaLocator slocator = SchemaLocator.getInstance();
            String externalLocations = slocator.buildSchemaLocationAttributeForExport(this);
            saxparser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", externalLocations);

            XMLReader xmlreader = saxparser.getXMLReader();
            EntityResolver er = new ValidationEntityResolver();
            xmlreader.setEntityResolver(er);

            SCAPErrorHandler handler = new SCAPErrorHandler(tempFile.getAbsolutePath());
            xmlreader.setContentHandler(new DummyContentHandler());
            xmlreader.setErrorHandler(handler);
            xmlreader.parse(new InputSource(new FileInputStream(tempFile)));
            errorMessage = handler.getErrorMessage();
        } catch (Exception e) {
            errorMessage = "Validating parse exception:" + e.getMessage();
            LOG.error(errorMessage, e);
        }

        if (errorMessage == null) {
            tempFile.deleteOnExit();
        }

        return errorMessage;
    }

    /**
     * This method when implemented by subclasses should do a more document type
     * specific check than a normal xml schema validation will do. Checking
     * things like making sure references to other parts of the same document or
     * references to other documents can be done when an implementation of this
     * method.
     *
     * @return String
     * @throws Exception
     */
    public abstract String validateSymantically() throws Exception;

    /**
     * Save the document to the disk using the current filename.
     *
     * @throws IOException
     */
    public void save() throws IOException {
        
        if (dataStreamCollection == null && filename == null) {
            // This is part of a data stream collection, saving is essentially different
            throw new IllegalStateException("SCAPDocument.save() called on new file - use saveAs(filename) instead");
        }
        saveAs(filename);
    }

    /**
     * Save this document as another name on disk.
     *
     * @param filename
     * @throws IOException
     */
    public void saveAs(String filename) throws IOException {
        
        File file;
        
        if(dataStreamCollection == null) {
            file = new File(filename);
        } else {
            file = new File(dataStreamCollection.getFileName());
        }
        
        
        saveAs(file);
        
    }

    /**
     * Save this document as another name on disk.
     *
     * @param file A File object representing the file we want to save to
     *
     * @throws IOException
     */
    public void saveAs(File file) throws IOException {
        
        LOG.debug("saveAs called for SCAPDocument type " + getDocumentType());
        String updatedSchemaLocation = SchemaLocator.getInstance().buildSchemaLocationAttributeForExport(this);

        Namespace xsiSchema = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        getElement().setAttribute("schemaLocation", updatedSchemaLocation, xsiSchema);

        
        
        if (dataStreamCollection != null) {
            
            // This is part of a data stream collection, saving is essentially different,
            // the stream must be saved, not the document itself.
            
            try {
                dataStreamCollection.saveAs(file);
            } catch (JDOMException ex) {
                LOG.error("JDOMException saving data stream.", ex);
            } catch (FileNotFoundException ex) {
                LOG.error("FileNotFoundException saving data stream.", ex);
            } catch (JAXBException ex) {
                LOG.error("JAXBException saving data stream.", ex);
            }
            
        } else {
            // This is a normal document, we can save as a actual file

            // don't use FileWriter here, because there is no way to override encoding 
            // with FileWriter:
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
            xmlo.output(getDoc(), osw);
            osw.flush();
            osw.close();
            
        }
        
    }

    /**
     * Private class to be used as a ContentHandler for a sax parse. It really
     * is does nothing with the elements, it's needed to make sure that document
     * validates correctly.
     *
     */
    class DummyContentHandler extends ParserAbstract {
    }

    /**
     * Private class to be used as an ErrorHandler for a sax parse. It
     * accumulates error messages in a StringBuilder. The contents of the
     * StringBuilder can be gotten later for return by the validate() method.
     *
     *
     */
    class SCAPErrorHandler implements ErrorHandler {

        StringBuilder sb = new StringBuilder();
        String filename;

        SCAPErrorHandler(String filename) {
            this.filename = filename;
        }

        @Override
        public void error(SAXParseException arg0) throws SAXException {
            processException(arg0, "ERROR");
        }

        @Override
        public void fatalError(SAXParseException arg0) throws SAXException {
            processException(arg0, "FATAL");
        }

        @Override
        public void warning(SAXParseException arg0) throws SAXException {
            processException(arg0, "WARNING");
        }

        public String getErrorMessage() {
            return (sb.length() == 0 ? null : sb.toString());
        }

        private void processException(SAXParseException e, String level) {
            sb.append(level);
            sb.append(" validating XML file ");
            sb.append(filename);
            sb.append("\nException:" + e.getMessage());
            sb.append("\nLine=" + e.getLineNumber());
            sb.append(", column=" + e.getColumnNumber());
            sb.append(", public id=" + e.getPublicId());
            sb.append(", system id=" + e.getSystemId());
            sb.append("\n");
        }
    }

    /**
     * Return the class of document this is.
     *
     */
    public SCAPDocumentClassEnum getDocumentClass() {
        return documentClass;
    }

    /**
     * Set the class of document this is.
     *
     * @param docClass
     */
    public void setDocumentClass(SCAPDocumentClassEnum docClass) {
        documentClass = docClass;
    }

    /**
     * Can be overriden by subclasses to do some clean up before the document is
     * removed from memory.
     */
    public void tearDown() {
    }

    /**
     * Get the bundle this document is part of. Will be null if it has been
     * opened as a stand-alone document.
     *
     * @return SCAPDocumentBundle
     */
    public SCAPDocumentBundle getBundle() {
        return bundle;
    }

    /**
     * This will be called by the SCAPBundle parse code to let this document
     * know that it belongs to that bundle.
     *
     * @param bundle A reference to the bundle to which this document belongs
     */
    public void setBundle(SCAPDocumentBundle bundle) {
        this.bundle = bundle;
    }

    /**
     * Get the dirty flag for this document.
     *
     * @return boolean
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Set the dirty flag for this document.
     *
     * @param b Whether or not this document is dirty.
     */
    public void setDirty(boolean b) {
        dirty = b;

        if (b) {
            if (bundle != null) {
                bundle.setDirty(true);
            }
        }
    }

    /**
     * Method that should be called to clear out any collections or references
     * that might prevent garbage collection.
     *
     */
    public abstract void close();

    private class ValidationEntityResolver implements EntityResolver {

        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            SchemaLocator slocator = SchemaLocator.getInstance();
            String sep = System.getProperty("file.separator");
            LOG.debug("ValidationEntityResolver called publicid=" + publicId + ", systemid=" + systemId);
            URI uri = null;
            File f = null;
            String nameComponent = null;

//          if (systemId.startsWith("file:"))
//          {
//              try
//              {
//                  uri = new URI(systemId);
//                  f = new File(uri);
//                  if (f.exists()) {
//                      InputStream inputStream = new FileInputStream(f);
//                      return new InputSource(inputStream);
//                  }
//              }
//              catch (Exception e)
//              {
//                  throw new IllegalStateException("There was a problem processing the system id " + systemId, e);
//              }
//          }

            nameComponent = systemId.substring(systemId.lastIndexOf("/") + 1);
            LOG.debug("ValidationEntityResolver namecomponent=" + nameComponent);
            InputStream inputStream = slocator.getInputStreamForNamespace(nameComponent, documentType);
            InputSource inputSource = null;
            if (inputStream != null) {
                LOG.debug("ValidationEntityResolver inputstream not null");
                inputSource = new InputSource(inputStream);
            }
            return inputSource;
        }
    }

    protected void startTimer(CollType type) {
        CollStat collStat = collMap.get(type);
        if (collStat == null) {
            collStat = new CollStat();
            collMap.put(type, collStat);
        }
        collStat.start = System.nanoTime();
    }

    protected void stopTimer(CollType type) {
        long stop = System.nanoTime();
        CollStat collStat = collMap.get(type);
        long elapsed = stop - collStat.start;
        collStat.totalCount++;
        collStat.totalTime += elapsed;
    }

    public void showTiming() {
        LOG.debug("Timing using " + strategy.toString() + " strategy  for getOvalDefinitions(), getOvalTests(), etc");
        LOG.debug("   Type        Count     Total Elapsed      Avg Time ");

        double allTypesElapsed = 0.0;
        int allTypesCount = 0;
        for (CollType type : CollType.values()) {
            CollStat collStat = collMap.get(type);
            String line;
            if (collStat == null) {
                line = type.toString();
            } else {
                double elapsedSeconds = (double) collStat.totalTime / 1000000000.0;
                double avgTime = elapsedSeconds / collStat.totalCount;

                line = String.format("%-12s    %5d     %10.3f     %10.3f",
                        type.toString(),
                        collStat.totalCount,
                        elapsedSeconds,
                        avgTime);
                allTypesElapsed += collStat.totalTime;
                allTypesCount += collStat.totalCount;
            }
            LOG.debug(line);
        }
        if (allTypesCount > 0) {
            double elapsedSeconds = allTypesElapsed / 1000000000.0;
            double avgTime = elapsedSeconds / allTypesCount;
            String line = String.format("%-12s    %5d     %10.3f     %10.3f",
                    "All",
                    allTypesCount,
                    elapsedSeconds,
                    avgTime);
            LOG.debug(line);
        }
    }

    protected class CollStat {

        long start;
        double totalTime;
        int totalCount;
    }

    public SearchStrategy getStrategy() {
        return strategy;
    }
	
	   /**
     * Get the raw date string from the generator element. Applies to oval and ocil only.
     *
     * @return String
     */
    public String getGeneratorRawDate()
    {
        String retRawDate = null;

        Element generatorElement = getRoot().getChild("generator", getRoot().getNamespace());

        if (generatorElement != null)
        {
            Element timestampElement = generatorElement.getChild("timestamp", getRoot().getNamespace("oval"));

            if (timestampElement != null)
            {
                retRawDate = timestampElement.getValue();
            } else
            {
                LOG.debug("timestampElement is NULL");
            }
        } else
        {
            LOG.debug("generatorElement is NULL");
        }
        return retRawDate;
    }

}
