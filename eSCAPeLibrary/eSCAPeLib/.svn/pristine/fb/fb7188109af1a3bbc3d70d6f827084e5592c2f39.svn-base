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

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;

//import com.g2inc.scap.impl.ocil.OcilImpl;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.schema.SchemaLocator;

/**
 * This class contains methods for constructing new SCAPDocuments.
 * 
 * @author ssill2
 */
public class SCAPDocumentFactory
{
	private static final Logger log = Logger.getLogger(SCAPDocumentFactory.class);

	/**
	 * Returns a SCAPDocument that wraps the supplied JDOMDocument with the
	 * appropriate Implementing class based on the type.
	 * 
	 * @param type
	 *            The type of document we are dealing with
	 * @param d
	 *            The JDOM Document object to be wrapped.
	 * @return SCAPDocument The implementation specific SCAPDocument
	 */
	public static SCAPDocument getDocument(SCAPDocumentTypeEnum type, Document d)
	{
		return getDocument(type, d, null);
	}
	
	/**
	 * Returns a SCAPDocument that wraps the supplied JDOMDocument with the
	 * appropriate Implementing class based on the type.
	 * 
	 * @param type
	 *            The type of document we are dealing with
	 * @param d
	 *            The JDOM Document object to be wrapped.
	 * @return SCAPDocument The implementation specific SCAPDocument
	 */
	private static SCAPDocument getDocument(SCAPDocumentTypeEnum type, Document d, InputStream is)
	{
		SCAPDocument sd = null;

		switch (type)
		{
		case OVAL_53:
			sd = new com.g2inc.scap.library.domain.oval.oval53.DefinitionsDocumentImpl(d);
			break;
		case OVAL_54:
			sd = new com.g2inc.scap.library.domain.oval.oval54.DefinitionsDocumentImpl(d);
			break;
		case OVAL_55:
			sd = new com.g2inc.scap.library.domain.oval.oval55.DefinitionsDocumentImpl(d);
			break;
		case OVAL_56:
			sd = new com.g2inc.scap.library.domain.oval.oval56.DefinitionsDocumentImpl(d);
			break;
		case OVAL_57:
			sd = new com.g2inc.scap.library.domain.oval.oval57.DefinitionsDocumentImpl(d);
			break;
		case OVAL_58:
			sd = new com.g2inc.scap.library.domain.oval.oval58.DefinitionsDocumentImpl(d);
			break;			
		case OVAL_59:
			sd = new com.g2inc.scap.library.domain.oval.oval59.DefinitionsDocumentImpl(d);
			break;	
		case OVAL_510:
			sd = new com.g2inc.scap.library.domain.oval.oval510.DefinitionsDocumentImpl(d);
			break;
		case XCCDF_114:
			sd = new com.g2inc.scap.library.domain.xccdf.impl.XCCDF114.XCCDFBenchmark(d);
			break;
		case XCCDF_12:
			sd = new com.g2inc.scap.library.domain.xccdf.impl.XCCDF12.XCCDFBenchmark(d);
			break;
		case CPE_20:
			sd = new com.g2inc.scap.library.domain.cpe.cpe20.CPEDictionaryDocumentImpl(d);
			break;
		case CPE_21:
			sd = new com.g2inc.scap.library.domain.cpe.cpe21.CPEDictionaryDocumentImpl(d);
			break;
		case CPE_22:
			sd = new com.g2inc.scap.library.domain.cpe.cpe22.CPEDictionaryDocumentImpl(d);
			break;
		case CPE_23:
			sd = new com.g2inc.scap.library.domain.cpe.cpe23.CPEDictionaryDocumentImpl(d);
			break;			
		case OCIL_2:
			sd = new com.g2inc.scap.library.domain.ocil.impl.OCIL2.OcilDocument(d);
			if (is == null) {
				throw new IllegalStateException("Can't instantiate Ocil 2 Document with no input stream");
			}
//			sd = new OcilImpl(is);
			break;
		}

		return sd;
	}	
	
	/**
	 * Returns a blank SCAPDocument based on the type.
	 * 
	 * @param type
	 *            The type of document we are dealing with
	 * @return SCAPDocument The implementation specific SCAPDocument
	 */
	public static SCAPDocument createNewDocument(SCAPDocumentTypeEnum type)
	{
		SCAPDocument sd = null;

		sd = buildNewDocument(type);

		return sd;
	}

    private static SCAPDocument buildNewDocument(SCAPDocumentTypeEnum type) {
        String templateResource = null;
        switch (type) {
            case OVAL_53:
                templateResource = "itemplate/oval/OVAL_53";
                break;
            case OVAL_54:
                templateResource = "itemplate/oval/OVAL_54";
                break;
            case OVAL_55:
                templateResource = "itemplate/oval/OVAL_55";
                break;
            case OVAL_56:
                templateResource = "itemplate/oval/OVAL_56";
                break;
            case OVAL_57:
                templateResource = "itemplate/oval/OVAL_57";
                break;
            case OVAL_58:
                templateResource = "itemplate/oval/OVAL_58";
                break;
            case OVAL_59:
                templateResource = "itemplate/oval/OVAL_59";
                break;
            case OVAL_510:
                templateResource = "itemplate/oval/OVAL_510";
                break;
            case XCCDF_113:
                templateResource = "itemplate/xccdf/XCCDF_113";
                break;
            case XCCDF_114:
                templateResource = "itemplate/xccdf/XCCDF_114";
                break;
            case XCCDF_12:
                templateResource = "itemplate/xccdf/XCCDF_12";
                break;
            case CPE_20:
                templateResource = "itemplate/cpe/CPE_20";
                break;
            case CPE_21:
                templateResource = "itemplate/cpe/CPE_21";
                break;
            case CPE_22:
                templateResource = "itemplate/cpe/CPE_22";
                break;
            case CPE_23:
                templateResource = "itemplate/cpe/CPE_23";
                break;
            case OCIL_2:
                templateResource = "itemplate/ocil/OCIL_2";
                break;
            default:
                throw new RuntimeException(
                        "Unable to find template for type " + type);
        }
        templateResource += "/template.xml";
        log.debug("templateResource = " + templateResource);
        SchemaLocator slocator = SchemaLocator.getInstance();
        log.debug("SchemaLocator instance obtained");
        InputStream inStream = slocator.getInputStream(templateResource);
        if (inStream == null) {
            throw new RuntimeException("Unable access template resource "
                    + templateResource);
        }

        SCAPDocument ret = null;
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;

        try {
            doc = builder.build(inStream);
            inStream.close();
            inStream = slocator.getInputStream(templateResource);
            ret = getDocument(type, doc, inStream);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return ret;
    }
	
    /**
     * This is where xml documents are actually parsed into in-memory JDOM documents
     *
     * @param file A File object representing the xml document to be read
     * @return Document
     */
    public static Document parse(File file) throws IOException, JDOMException, CharacterSetEncodingException
    {
        return parse(file, Charset.forName("UTF-8"));
    }

    /**
     * This is where xml documents are actually parsed into in-memory JDOM documents
     *
     * @param file A File object representing the xml document to be read
     * @param documentEncoding The character set of the document to be read
     * @return Document
     */
    private static Document parse(File file, Charset documentEncoding) throws IOException, JDOMException, CharacterSetEncodingException
    {
        Document d = null;
        log.info("SCAPDocumentFactory parse loading file " + file.getAbsolutePath() + ", size=" + file.length() );
        FileInputStream fis = null;
        SAXBuilder builder = null;
        
        try
        {
        	fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, documentEncoding);

        	builder = new SAXBuilder();
        	log.info("SCAPDocumentFactory parse building document");
        	d = builder.build(isr);
        	log.info("SCAPDocumentFactory parse finished building document");
        }
        catch(IOException e)
        {
            String message = e.getMessage();
            String userMessage = null;

            if(message.indexOf("Invalid byte 1 of 1-byte UTF-8 sequence") > -1)
            {
                userMessage = "File " + file.getName() + "'s encoding attribute says it's UTF-8 but characters were found "
                        + "that were encoded in some other character encoding.  Perhaps they were encoding using your system's "
                        + "default character encoding.";
                throw new CharacterSetEncodingException(userMessage, e);
            }
            else
            {
            	log.error("I/O Error parsing JDOM document", e);
                throw e;
            }
        }
        catch(JDOMException e)
        {
        	log.error("Error parsing JDOM document", e);
            throw e;
        }
        finally
        {
            try
            {
                if(fis != null)
                {
                	log.debug("documentfactory closing input stream");
                    fis.close();
                    log.debug("documentfactory closed input stream");
                    fis = null;
                }

                if(builder != null)
                {
                    builder = null;
                }
            }
            catch(Exception e)
            {
            	log.error("Error closing input stream", e);
            }
        }
        log.info("documentfactory finished loading file " + file.getAbsolutePath() + ", returning " + d.getDocType());
        return d;
    }

    /**
     * This is where xml documents are actually parsed into in-memory JDOM documents
     *
     * @param file A File object representing the xml document to be read
     * @return Document
     */
    private static Document parse(InputStream is) throws IOException, JDOMException
    {
        Document d = null;

        SAXBuilder builder = new SAXBuilder();
        d = builder.build(is);
        
        return d;
    }
    
    /**
     * Returns a SCAPDocumentTypeEnum that represents the type of document
     * the supplied JDOM document represents.
     * 
     * @param doc JDOM document for which to find the document type
     * @return SCAPDocumentTypeEnum
     */
    public static SCAPDocumentTypeEnum findDocumentType(Document doc)
    {
        SCAPDocumentTypeEnum type = SCAPDocumentTypeEnum.UNKNOWN;
        Element rootElement = doc.getRootElement();

        if (rootElement.getName().indexOf("oval_definitions") > -1)
        {
            Element generator = null;
            List children = rootElement.getChildren();

            for (int x = 0; x < children.size(); x++)
            {
                Element child = (Element) children.get(x);

                if (child.getName().toLowerCase().equals("generator"))
                {
                    generator = child;
                    break;
                }
            }

            if (generator != null)
            {
                String version = null;

                Element verElement = null;

                List generatorChildren = generator.getChildren();

                for (int x = 0; x < generatorChildren.size(); x++)
                {
                    Element child = (Element) generatorChildren.get(x);

                    if (child.getName().toLowerCase().equals("schema_version"))
                    {
                        verElement = child;
                        break;
                    }
                }

                if (verElement != null)
                {
                    version = verElement.getValue();

                    if (version.equals("6.0"))
                    {
                        type = SCAPDocumentTypeEnum.OVAL_60;
                    }
                    else if (version.equals("5.10") || version.equals("5.10.1"))
                    {
                        type = SCAPDocumentTypeEnum.OVAL_510;
                    }  
                    else if (version.equals("5.9"))
                    {
                        type = SCAPDocumentTypeEnum.OVAL_59;
                    }    
                    else if (version.equals("5.8"))
                    {
                        type = SCAPDocumentTypeEnum.OVAL_58;
                    }                    
                    else if (version.equals("5.7"))
                    {
                        type = SCAPDocumentTypeEnum.OVAL_57;
                    }
                    else if (version.equals("5.6"))
                    {
                        type = SCAPDocumentTypeEnum.OVAL_56;
                    }
                    else if (version.equals("5.5"))
                    {
                        type = SCAPDocumentTypeEnum.OVAL_55;
                    }
                    else if (version.equals("5.4"))
                    {
                        type = SCAPDocumentTypeEnum.OVAL_54;
                    }
                    else if (version.equals("5.3"))
                    {
                        type = SCAPDocumentTypeEnum.OVAL_53;
                    }
                } else
                {
                    log.debug("verElement is NULL");
                }
            }
        }
        else if (rootElement.getName().toLowerCase().indexOf("benchmark") > -1)
        {
        	String rootNamespaceUri = rootElement.getNamespaceURI();
        	String style = rootElement.getAttributeValue("style");  // style is optional
        	if (rootNamespaceUri.equals(SCAPElement.XCCDF11_NAMESPACE.getURI())) {
        		type = SCAPDocumentTypeEnum.XCCDF_114;
        	} else if (rootNamespaceUri.equals(SCAPElement.XCCDF12_NAMESPACE.getURI())) {
        		type = SCAPDocumentTypeEnum.XCCDF_12;
        	} else {
        		throw new IllegalStateException("XCCDF document of unknown type encountered; root element namespace=" + rootNamespaceUri +", style attribute=" + (style == null ? "null" : style));
        	}
        }
        else if (rootElement.getName().indexOf("cpe-list") > -1)
        {
            Namespace ns = doc.getRootElement().getNamespace();
            
            // check if this is a cpe 2.x document
            if(ns.getURI().equals("http://cpe.mitre.org/dictionary/2.0"))
            {
            	List attributes = doc.getRootElement().getAttributes();
            	
                if(attributes != null && attributes.size() > 0)
                {
                    for(int x = 0; x < attributes.size() ; x++)
                    {
                        Attribute a = (Attribute) attributes.get(x);

                        String aName = a.getName();
                        String aValue = a.getValue();

                        if(aName.equals("schemaLocation"))
                        {
                            String searchString = "cpe-dictionary_";

                            if(aValue != null && aValue.indexOf(searchString) > -1)
                            {
                                int foundLoc = aValue.indexOf(searchString);
                                int startLoc = foundLoc + searchString.length();

                                // look for a string like "2.1" immediately following our
                                // search string
                                String substr = aValue.substring(startLoc, startLoc + 3);
                                if(substr.equals("2.3")) {
                                    type = SCAPDocumentTypeEnum.CPE_23;
                                } else if(substr.equals("2.2")) {
                                    type = SCAPDocumentTypeEnum.CPE_22;
                                } else if(substr.equals("2.1")) {
                                    type = SCAPDocumentTypeEnum.CPE_21;
                                } else if(substr.equals("2.0")) {
                                    type = SCAPDocumentTypeEnum.CPE_20;
                                } else {
                                    log.info("Defaulting type to " + SCAPDocumentTypeEnum.CPE_23);
                                    type = SCAPDocumentTypeEnum.CPE_23;
                                }
                            }
                            else
                            {
                                log.info("Defaulting type to " + SCAPDocumentTypeEnum.CPE_23);
                                type = SCAPDocumentTypeEnum.CPE_23;
                            }
                        }
                    }

                    if(type == null)
                    {
                        log.info("Defaulting type to " + SCAPDocumentTypeEnum.CPE_20);
                        type = SCAPDocumentTypeEnum.CPE_20;
                    }
                }
            }
            else
            {
                log.info("Defaulting type to " + SCAPDocumentTypeEnum.CPE_20);
                type = SCAPDocumentTypeEnum.CPE_20;
            }
        } 
        else if (rootElement.getName().equals("ocil")) {
        	type = SCAPDocumentTypeEnum.OCIL_2;
        }

        return type;
    }

    /**
     * Load an SCAP document from the disk.
     * 
     * @param file File to load.
     * @param documentEncoding Character set of the document
     * @return SCAPDocument
     * @throws JDOMException
     * @throws IOException
     */
    public static SCAPDocument loadDocument(File file, Charset documentEncoding) throws JDOMException,
    	IOException, CharacterSetEncodingException, UnsupportedDocumentTypeException
    {
    	SCAPDocument ret = null;

    	Document d = null;

    	try
    	{
    		d = parse(file, documentEncoding);
    	}
    	catch(JDOMException je)
    	{
    		log.error("JDOMException occurred trying to parse file " + file.getAbsolutePath(), je);
    		throw je;
    	}
    	catch(IOException ioe)
    	{
    		log.error("IOException occurred trying to parse file " + file.getAbsolutePath(), ioe);
    		throw ioe;
    	}

    	SCAPDocumentTypeEnum  dType = findDocumentType(d);

    	if(dType != SCAPDocumentTypeEnum.UNKNOWN)
    	{
    		ret = getDocument(dType, d, new FileInputStream(file));

    		if (ret != null)
    		{
    			ret.setFilename(file.getAbsolutePath());
    			if (ret instanceof OvalDefinitionsDocument)
    			{
    				// This code is now in OvalDefinitionsDocument constructor:
//    				OvalDefinitionsDocument odd = (OvalDefinitionsDocument) ret;
//    				odd.refreshDefinitionMap(); // to force set of def ids to be populated
//    				odd.refreshTestMap(); // to force set of test ids to be populated
//    				odd.refreshObjectMap(); // to force set of object ids to be populated
//    				odd.refreshStateMap(); // to force set of state ids to be populated
//    				odd.refreshVariableMap(); // to force set of var ids to be populated
    			}
    			else if(ret instanceof CPEDictionaryDocument)
    			{
    				CPEDictionaryDocument cdd = (CPEDictionaryDocument) ret;
    				log.debug("calling refreshItemsMap()");
    				cdd.refreshItemsMap(); // force map to be loaded
    				log.debug("returned from refreshItemsMap()");
    			}
    		}
    	}
    	else
    	{
    		throw new UnsupportedDocumentTypeException("Document type not currently supported");
    	}

    	return ret;
    }

    /**
     * Load an SCAP document from the disk.
     * 
     * @param file File to load.
     * 
     * @return SCAPDocument
     * @throws JDOMException
     * @throws IOException
     */
    public static SCAPDocument loadDocument(File file) throws JDOMException, IOException,
    	CharacterSetEncodingException, UnsupportedDocumentTypeException
    {
        return loadDocument(file, Charset.forName("UTF-8"));
    }

    /**
     * Load an SCAP document from a stream.
     * 
     * @param is Stream to read document from.
     * 
     * @return SCAPDocument
     * @throws JDOMException
     * @throws IOException
     */
    public static SCAPDocument loadDocument(InputStream is) throws JDOMException, IOException,
    	UnsupportedDocumentTypeException
    {
    	SCAPDocument ret = null;
    	
    	Document d = parse(is);
    	SCAPDocumentTypeEnum  dType = findDocumentType(d);
    	
    	if(dType != SCAPDocumentTypeEnum.UNKNOWN)
    	{
    		ret = getDocument(dType, d, is);
    	}
        else
        {
            throw new UnsupportedDocumentTypeException("Document type " + dType + " not currently supported");
        }

    	return ret;
    }
    
    /**
     * Load an SCAP document from a org.w3c.Document.
     * 
     * @param w3cDoc w3c Document to be converted to JDOM Document, used in new SCAPDocument
     * 
     * @return SCAPDocument
     * @throws JDOMException
     * @throws IOException
     */
    public static SCAPDocument loadDocument(org.w3c.dom.Document w3cDoc) throws JDOMException, IOException,
    	UnsupportedDocumentTypeException
    {
    	SCAPDocument ret = null;  	
    	DOMBuilder domBuilder = new DOMBuilder();  	
    	Document d = domBuilder.build(w3cDoc);
    	
    	SCAPDocumentTypeEnum  dType = findDocumentType(d);
    	
    	if(dType != SCAPDocumentTypeEnum.UNKNOWN) {
    		ret = getDocument(dType, d);
    	} else {
            throw new UnsupportedDocumentTypeException("Document type " + dType + " not currently supported");
        }

    	return ret;
    }
}
