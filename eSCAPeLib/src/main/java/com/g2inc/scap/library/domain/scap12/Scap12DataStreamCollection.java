package com.g2inc.scap.library.domain.scap12;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.jdom.output.DOMOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.g2inc.oasis.xml.catalog.Catalog;
import com.g2inc.oasis.xml.catalog.Uri;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentClassEnum;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.UnsupportedDocumentTypeException;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.datastream.Component;
import com.g2inc.scap.library.domain.datastream.ComponentRef;
import com.g2inc.scap.library.domain.datastream.DataStream;
import com.g2inc.scap.library.domain.datastream.DataStreamCollection;
import com.g2inc.scap.library.domain.datastream.RefListType;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
//import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.schema.SchemaLocator;
//import com.g2inc.scap.model.ocil.OcilDocument;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.jdom.input.SAXBuilder;

public class Scap12DataStreamCollection {
	
	private static Logger LOG = Logger.getLogger(Scap12DataStreamCollection.class);
	
	private String fileName;
	
	private Map<String, SCAPDocument> componentSCAPDocMap = new HashMap<String, SCAPDocument>();
	
	public static JAXBContext JAXB_CONTEXT;
	static {
		try {
			JAXB_CONTEXT = JAXBContext.newInstance("com.g2inc.scap.library.domain.datastream");
		} catch (JAXBException e) {
			LOG.error("Error creating jaxb context for scap datastream", e);
		}
	}
	
	DatatypeFactory datatypeFactory = null;
	
	DataStreamCollection dataStreamCollection;
	
	List<SCAPDocument> scapDocuments = new ArrayList<SCAPDocument>();
	
//	List<XCCDFBenchmark> xccdfDocuments = new ArrayList<XCCDFBenchmark>();
//	List<OvalDefinitionsDocument> ovalDocuments = new ArrayList<OvalDefinitionsDocument>();
//	List<CPEDictionaryDocument> cpeDocuments = new ArrayList<CPEDictionaryDocument>();
//	List<OcilDocument> ocilDocuments = new ArrayList<OcilDocument>();
	
        public Scap12DataStreamCollection(String filePath) 
                throws DatatypeConfigurationException, IOException, IllegalStateException, JAXBException, JDOMException, UnsupportedDocumentTypeException {
            // This is a brand new data stream
            this(filePath, null);
        } 
        
	public Scap12DataStreamCollection(File file) 
                throws DatatypeConfigurationException, IOException, IllegalStateException, JAXBException, JDOMException, UnsupportedDocumentTypeException {
            // This is an existing data stream
            this(file.getAbsolutePath(), new FileInputStream(file));
	}
	
	public Scap12DataStreamCollection(String fileName, InputStream inputStream) 
                throws DatatypeConfigurationException, IOException, IllegalStateException, JAXBException, JDOMException, UnsupportedDocumentTypeException {
		datatypeFactory = DatatypeFactory.newInstance();
                this.fileName = fileName;
                
                if(inputStream == null) {
                    // This is a new stream, we'll need to create a file first
                    File scapFile = new File(fileName);
                    scapFile.createNewFile();
                    inputStream = new FileInputStream(scapFile);
                    
                    String templateResource = "itemplate/scap/SCAP_12/template.xml";
                //    LOG.debug("templateResource = " + templateResource);
                    SchemaLocator slocator = SchemaLocator.getInstance();
                  //  LOG.debug("SchemaLocator instance obtained");
                    inputStream = slocator.getInputStream(templateResource);
                    if (inputStream == null) {
                        throw new RuntimeException("Unable access template resource "
                                + templateResource);
                    }
                }
                
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
	//	LOG.debug("Constructor calling unmarshaller");
		dataStreamCollection = (DataStreamCollection) unmarshaller
				.unmarshal(reader);
		List<Component> componentList = dataStreamCollection.getComponent();
		for (Component component : componentList) {
			Object compObject = component.getAny();
			if (compObject instanceof Element) {
				Element element = (Element) compObject;
				String elementName = element.getNodeName();
				String namespace = element.getNamespaceURI();
		//		LOG.debug("element name: " + elementName + ", namespace: " + namespace);
				String[] idParts = getReverseDnsAndNameFromScapGuid(component.getId(), "comp");
				String name = idParts[1];
				Node parent = element.getParentNode();
				if (parent == null) {
					throw new IllegalStateException("Parent (Document) Node is null for element: " + elementName + ", namespace: " + namespace);
				} 
				if (parent.getNodeType() != Node.DOCUMENT_NODE ) {
					throw new IllegalStateException("Parent Node type for element: " + elementName + ", namespace: " + namespace + " is not Document, but " + parent.getNodeType());
				} 

				Document parentDoc = (Document) parent;
				SCAPDocument scapDoc = SCAPDocumentFactory.loadDocument(parentDoc);
				scapDoc.setFilename(name); 
                                scapDoc.setDataStreamCollection(this);
				scapDocuments.add(scapDoc);
				componentSCAPDocMap.put(component.getId(), scapDoc);
//				if (scapDoc instanceof XCCDFBenchmark) {
//					xccdfDocuments.add((XCCDFBenchmark) scapDoc);
//				} else if (scapDoc instanceof OvalDefinitionsDocument) {
//					ovalDocuments.add((OvalDefinitionsDocument) scapDoc);
//				} else if (scapDoc instanceof CPEDictionaryDocument) {
//					cpeDocuments.add((CPEDictionaryDocument) scapDoc);
//				} else if (scapDoc instanceof OcilDocument) {
//					ocilDocuments.add((OcilDocument) scapDoc);
//				} else {
//					throw new IllegalStateException("Unexpected SCAPDocument type: " + scapDoc.getClass().getName());
//				}
			}
		}
	}
        
        public void save() throws JDOMException, FileNotFoundException, JAXBException {
		
		updateScapDocuments();
		
		FileOutputStream outputStream = new FileOutputStream(fileName);
		Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(this.dataStreamCollection, outputStream);
            
        }
	
	public void saveAs(File file) throws JDOMException, FileNotFoundException, JAXBException {
		
		updateScapDocuments();
		
		FileOutputStream outputStream = new FileOutputStream(file);
		Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(this.dataStreamCollection, outputStream);
	}
	
	private void updateScapDocuments() throws JDOMException {
		List<DataStream> dataStreamList = dataStreamCollection.getDataStream();
		for (DataStream dataStream : dataStreamList) {
                    
                    // Getting all the reference lists
                    RefListType cpeDictRefList = dataStream.getDictionaries();
                    RefListType checkListRefList = dataStream.getChecklists();
                    RefListType checkRefList = dataStream.getChecks();
                    RefListType extCompRefList = dataStream.getExtendedComponents();
                    
                    // Adding all the component references to a list, it will go through them all and update them
                    List<ComponentRef> compRefList = new ArrayList<ComponentRef>();
                    if(cpeDictRefList != null) {
                        compRefList.addAll(cpeDictRefList.getComponentRef());
                    }
                    if(checkListRefList != null) {
                        compRefList.addAll(checkListRefList.getComponentRef());
                    }
                    if(checkRefList != null) {
                        compRefList.addAll(checkRefList.getComponentRef());
                    }
                    if(extCompRefList != null) {
                        compRefList.addAll(extCompRefList.getComponentRef());
                    }
                    
                    // Iterating all component refs
                    for (ComponentRef compRef : compRefList) {
                            String href = compRef.getHref();
                            if (!href.startsWith("#")) {
                                    throw new IllegalStateException("Component-ref contains non-local (not starting with '#') href: " + href);
                            }
                            String compId = href.substring(1);  // skip over '#', rest is component id
                            Component component = getComponentById(compId);
                            if (component == null) {
                                    throw new IllegalStateException("Component-ref: " + compRef.getId() + " points to non existent component: " + compId);
                            }
                            updateComponent(component, dataStream);
                    }
		}
	}
	
	public void updateComponent(Component component, DataStream dataStream) throws JDOMException {
		SCAPDocument scapDoc = componentSCAPDocMap.get(component.getId());
		component.setTimestamp(datatypeFactory.newXMLGregorianCalendar(new GregorianCalendar()));
                
                DOMOutputter outputter = new DOMOutputter();
                org.w3c.dom.Document w3cDoc = outputter.output(scapDoc.getDoc());
                component.setAny(w3cDoc.getDocumentElement());
                scapDoc.setDirty(false);
                addComponentRefs(component, dataStream, scapDoc);	
		
	}
	
	public void addOrUpdateComponent(DataStream dataStream, SCAPDocument scapDoc) throws JDOMException {
		String fileName = scapDoc.getFilename();
		SCAPDocumentTypeEnum scapDocType = scapDoc.getDocumentType();
		//LOG.debug("addOrUpdateComponent called for SCAPDocument type: "
			//	+ (scapDocType == null ? "null" : scapDocType.toString())
			//	+ ", file name " + (fileName == null ? "null" : fileName)
			//	+ "\n\tto data-stream " + dataStream.getId());
		if (fileName == null) {
			throw new IllegalStateException(
					"addOrUpdateComponent called for component with no filename; filename is required to build component name; component type is "
							+ (scapDocType == null ? "null" : scapDocType.toString()));
		}
		String reverseDns = getReverseDns(dataStream);
		String componentId = getComponentId(reverseDns, fileName);
		Component component = getComponentById(componentId);
		if (component == null) {
			component = new Component();
			component.setId(componentId);
			dataStreamCollection.getComponent().add(component);
			componentSCAPDocMap.put(componentId, scapDoc);
			scapDoc.setDirty(true);  // force update of jaxb 'any' DOM element
		}	
		component.setTimestamp(datatypeFactory.newXMLGregorianCalendar(new GregorianCalendar()));
		
		if (scapDoc.isDirty()) {
			DOMOutputter outputter = new DOMOutputter();
			org.w3c.dom.Document w3cDoc = outputter.output(scapDoc.getDoc());
			component.setAny(w3cDoc.getDocumentElement());
			addComponentRefs(component, dataStream, scapDoc);	
		}	
          //      LOG.debug("addOrUpdateComponent call finished");
	}
	
	private void addComponentRefs(Component component, DataStream dataStream, SCAPDocument scapDoc) {
		String[] reverseDnsAndName = getReverseDnsAndNameFromScapGuid(component.getId(), "comp");
		String reverseDns = reverseDnsAndName[0];
		String name = reverseDnsAndName[1];
		String crefId = getComponentRefId(reverseDns, name);
		ComponentRef compRef = new ComponentRef();
		compRef.setId(crefId);
		String componentHref = "#" + component.getId();
		compRef.setHref(componentHref);
		
		SCAPDocumentClassEnum docClass = scapDoc.getDocumentClass();
		List<ComponentRef> refs = null;
		ComponentRef existingCompRef = null;
		Set<String> hrefSet = null;
		switch (docClass) {
		case OVAL:
		case OCIL:
			RefListType checks = dataStream.getChecks();
			refs = checks.getComponentRef();
			existingCompRef = getComponentRef(crefId, refs);
			if (existingCompRef == null) {
				refs.add(compRef);
			}
			break;
		/*case XCCDF:
			RefListType checklists = dataStream.getChecklists();
			if (checklists == null) {
				checklists = new RefListType();
				dataStream.setChecklists(checklists);
			}
			refs = checklists.getComponentRef();
			existingCompRef = getComponentRef(crefId, refs);
			if (existingCompRef == null) {
				refs.add(compRef);
			}
			XCCDFBenchmark benchmark = (XCCDFBenchmark) scapDoc;
			hrefSet = benchmark.getAllCheckContentHrefs();

			buildCatalog(hrefSet, reverseDns, compRef);
			break; */
		case CPE_DICTIONARY:
			RefListType dictionaries = dataStream.getDictionaries();
			if (dictionaries == null) {
				dictionaries = new RefListType();
				dataStream.setDictionaries(dictionaries);
			}
			refs = dictionaries.getComponentRef();
			existingCompRef = getComponentRef(crefId, refs);
			if (existingCompRef == null) {
				refs.add(compRef);
			}
			CPEDictionaryDocument cpeDict = (CPEDictionaryDocument) scapDoc;
			hrefSet = cpeDict.getAllCheckContentHrefs();
			
			buildCatalog(hrefSet, reverseDns, compRef);
			break;
		}
	}
	
	/**
	 * Inside each component-ref for an xccdf or cpe-dictionary document, there is often a 
	 * catalog entry containing one or more uris for hrefs.  In the case of an xccdf doc, the 
	 * uris are for the oval or ocil documents referenced by Rules in the xccdf. In the case of 
	 * a cpe-dictionary document the uris are the cpe-oval documents referenced by cpe-items.
	 * 
	 * This method builds a catalog entry and as many uri as are needed.
	 * 
	 * @param hrefSet Set<String> containing hrefs for uris
	 * @param reverseDns reverseDns used to build ids
	 * @param compRef ComponentRef which will contain generated Catalog element
	 */
	private void buildCatalog(Set<String> hrefSet, String reverseDns, ComponentRef compRef) {
		Catalog catalog = compRef.getCatalog();
		if (catalog == null) {
			catalog = new Catalog();
			compRef.setCatalog(catalog);
		} 
		if (hrefSet.size() == 0) {
			// no hrefs to OVAL or OCIL files, remove catalog from component-ref if it was there
			compRef.setCatalog(null);
		} else {
			// 
			List<Object> uriList = catalog.getPublicOrSystemOrUri();
			uriList.clear();  // remove any old uris which may have been in catalog, all will be rebuilt
			for (String href : hrefSet) {
				Uri uri = getUri(href, uriList);
				if (uri == null) {
					uri = new Uri();
					uri.setName(href);
					String uriCrefId = getComponentRefId(reverseDns, href);
					uri.setUri(uriCrefId);
					uriList.add(uri);
				}
			}
		}
	}
	
	private Uri getUri(String href, List<Object> uriList) {
		for (Object obj : uriList) {
			if (obj instanceof Uri) {
				Uri uri = (Uri) obj;
				if (uri.getName().equals(href)) {
					return uri;
				}
			}
		}
		return null;
	}
	
	private ComponentRef getComponentRef(String crefId, List<ComponentRef> refList) {
		for (ComponentRef compRef : refList) {
			if (compRef.getId().equals(crefId)) {
				return compRef;
			}
		}
		return null;
	}
	
	private Component getComponentById(String componentId) {
		List<Component> componentList = dataStreamCollection.getComponent();
		for (Component component : componentList) {
			if (component.getId().equals(componentId)) {
				return component;
			}
		}
		return null;
	}
	
	private String getComponentRefId(String reverseDns, String name) {
		return "scap_" + reverseDns + "_cref_" + name;
	}
	
	private String getComponentId(String reverseDns, String name) {
		return "scap_" + reverseDns + "_comp_" + name;
	}
	
	private String getReverseDns(DataStream ds) {
		String[] reverseDnsAndName = getReverseDnsAndNameFromScapGuid(ds.getId(), "datastream");
		return reverseDnsAndName[0];   // first String in array is reverse-dns, should apply to all components
	}
	
	/**
	 * Decompose an SCAP 1.2 id into its 4 parts:
	 * 
	 * "scap_"
	 * reverseDns
	 * separator
	 * name
	 * 
	 * The separator will be '_collection_', '_datastream_', '_cref_', '_comp_', or '_ecomp_' 
	 * 
	 * This function returns an array containing the reverseDns and the name
	 * 
	 * @param scapId  id like "scap_gov.nist_comp_USGCB-Windows-7-Energy-xccdf.xml"
	 * @param separator  string like "comp" for the example above
	 * @return  String[] containing 
	 * 		"gov.nist" and 
	 * 		"USGCB-Windows-7-Energy-xccdf.xml" for the example above
	 */
	private String[] getReverseDnsAndNameFromScapGuid(String scapId, String separatorWord) {
		String[] result = new String[2];
		String separator = "_" + separatorWord + "_";
		if (!scapId.startsWith("scap_")) {
			throw new IllegalStateException("Invalid scap id: " + scapId + ", must start with 'scap_'");
		}
		int separatorOffset = scapId.lastIndexOf(separator);
		if (separatorOffset == -1) {
			throw new IllegalStateException("Invalid scap id: " + scapId + ", must contain '" + separator);
		}
		result[0] = scapId.substring("scap_".length(), separatorOffset);
		result[1] = scapId.substring(separatorOffset + separator.length());
		return result;
	}

	public DataStreamCollection getDataStreamCollection() {
		return dataStreamCollection;
	}

	public void setDataStreamCollection(DataStreamCollection dataStreamCollection) {
		this.dataStreamCollection = dataStreamCollection;
	}

	private <T> List<T> getSCAPDocuments(SCAPDocumentClassEnum docClass) {
		List<T> list = new ArrayList<T>();
		for (SCAPDocument scapDoc : scapDocuments) {
			if (docClass == scapDoc.getDocumentClass()) {
				list.add( (T) scapDoc);
			}
		}
		return list;
	}

	private List<SCAPDocument> getSCAPDocuments(DataStream stream, RefListType refList) {
            
            List<SCAPDocument> documentList = new ArrayList<SCAPDocument>();

            // Adding all the component references to a list, it will go through them all and update them
            List<ComponentRef> compRefList = new ArrayList<ComponentRef>();
            if(refList != null) {
                compRefList.addAll(refList.getComponentRef());
            }

            // Iterating all component refs
            for (ComponentRef compRef : compRefList) {
                String href = compRef.getHref();
                if (!href.startsWith("#")) {
                        throw new IllegalStateException("Component-ref contains non-local (not starting with '#') href: " + href);
                }
                String compId = href.substring(1);  // skip over '#', rest is component id
                Component component = getComponentById(compId);
                if (component == null) {
                        throw new IllegalStateException("Component-ref: " + compRef.getId() + " points to non existent component: " + compId);
                }

                documentList.add(componentSCAPDocMap.get(component.getId()));
                    
            }
            
            return documentList;
	}

	/*public List<XCCDFBenchmark> getXccdfDocuments() {
		return getSCAPDocuments(SCAPDocumentClassEnum.XCCDF);
	}*/

	public List<OvalDefinitionsDocument> getOvalDocuments() {
		return getSCAPDocuments(SCAPDocumentClassEnum.OVAL);
	}

	public List<CPEDictionaryDocument> getCpeDocuments() {
		return getSCAPDocuments(SCAPDocumentClassEnum.CPE_DICTIONARY);
	}

	/*public List<OcilDocument> getOcilDocuments() {
		return getSCAPDocuments(SCAPDocumentClassEnum.OCIL);
	}
*/
	public List<SCAPDocument> getDictionaries(DataStream stream) {
            RefListType cpeDictRefList = stream.getDictionaries();
            return getSCAPDocuments(stream, cpeDictRefList);
	}

	public List<SCAPDocument> getChecklists(DataStream stream) {
            RefListType checkListRefList = stream.getChecklists();
            return getSCAPDocuments(stream, checkListRefList);
	}

	public List<SCAPDocument> getChecks(DataStream stream) {
            RefListType checkRefList = stream.getChecks();
            return getSCAPDocuments(stream, checkRefList);
	}

	public List<SCAPDocument> getExtendedComponents(DataStream stream) {
            RefListType extCompRefList = stream.getExtendedComponents();
            return getSCAPDocuments(stream, extCompRefList);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<SCAPDocument> getScapDocuments() {
		return scapDocuments;
	}

}
