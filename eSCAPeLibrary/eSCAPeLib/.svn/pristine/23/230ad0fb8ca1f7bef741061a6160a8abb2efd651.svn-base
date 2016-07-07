package com.g2inc.scap.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlFileCompare implements ErrorHandler {
	
	private static final Logger LOG = Logger.getLogger(XmlFileCompare.class);	
//	public static final String SCHEMA_NAME = "src/main/resources/ocil.xsd";
//	public static final File SCHEMA_FILE = new File(SCHEMA_NAME);
	
	private File currentFile;
	private Document doc1;
	private Document doc2;
	
	private List<String> errorList = new ArrayList<String>();
	
	public XmlFileCompare() {
	}
	
	private void initializeLog4j() {
		
		File configFile = new File("src/test/resources/log4j.xml");
		if (configFile.exists()) {
			DOMConfigurator.configure(configFile.getAbsolutePath());
			LOG.debug("Log4j configured using " + configFile.getAbsolutePath());
		} else {
			System.out.println("Log4j config file not found: " + configFile.getAbsolutePath());
		}
//		System.setProperty("javax.xml.parsers.DocumentBuilderFactory", 
//				"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		
	}
	
	private static File checkFile(String filename) {
		File file = new File(filename);
		if (!(file.exists())) {
			throw new IllegalArgumentException("File does not exist: " + filename);
		}
		return file;
	}
	
	private Document validateFile(File file, File schemaFile) throws Exception {
		currentFile = file;
		LOG.debug("validating file: " + file.getAbsolutePath());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		factory.setAttribute("http://apache.org/xml/properties/dom/document-class-name",
//				"org.apache.xerces.dom.PSVIDocumentImpl");
		factory.setValidating(false);  // don't need this set when using external schema
		factory.setNamespaceAware(true);
		
		SchemaFactory schemaFactory = 
		    SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		
		Source source = new StreamSource(schemaFile);
		Schema schema = schemaFactory.newSchema(source);
		factory.setSchema(schema);

		
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		docBuilder.setErrorHandler(this);
		Document doc = docBuilder.parse(file);
		
		currentFile = null;
		return doc;
	}
	
	public List<String> compareFiles(File inFile1, File inFile2, File schemaFile) {
		errorList = new ArrayList<String>();
		try {
			doc1 = validateFile(inFile1, schemaFile);
			LOG.debug("Finished validating inFile1");
		} catch (Exception e) {
			LOG.error("Error processing inFile1",e);
			return errorList;
		}
		try {
			doc2 = validateFile(inFile2, schemaFile);
			LOG.debug("Finished validating inFile1");
		} catch (Exception e) {
			LOG.error("Error processing inFile2");
			return errorList;
		}
		// now we know both files are valid according to the schema, compare each element
		Element root1 = doc1.getDocumentElement();
		Element root2 = doc2.getDocumentElement();
		boolean areEqual = compareElements(root1, root2);
		LOG.debug("Documents are " + (areEqual ? "" : "NOT") + " equal");
		return errorList;
	}
	
	private boolean compareElements(Element elem1, Element elem2) {
		boolean areEqual = true;
		String errorMessage = null;
		String elem1Name = elem1.getNodeName();
		String elem2Name = elem2.getNodeName();
		String elem1NS = elem1.getNamespaceURI();
		String elem2NS = elem2.getNamespaceURI();
		String path = getXPath(elem1);
		if (!(elem1Name.equals(elem2Name))) {
			errorMessage = "Unequal node names '" + elem1Name + "' and '" + elem2Name + " PATH:" + path;
			LOG.error(errorMessage);
			errorList.add(errorMessage);
			areEqual = false;
		} else if (!(elem1NS.equals(elem2NS))) {
			errorMessage = "Unequal namespaces for node '" + elem1Name + "' Namespace 1:" + elem1NS + " Namespace 2:" + elem2NS + " PATH:" + path;
			LOG.error(errorMessage);
			errorList.add(errorMessage);
			areEqual = false;
		} 
		LOG.debug("PATH: " + path);
		
		boolean areAttributesEqual = compareAttributes(elem1, elem2, path);
		LOG.debug("Attributes are " + (areAttributesEqual ? "" : "NOT") + " equal");
		
		NodeList children1 = elem1.getChildNodes();
		List<Integer> indexList1 = getElementIndeces(children1);
		
		NodeList children2 = elem2.getChildNodes();
		List<Integer> indexList2 = getElementIndeces(children2);	
		
		if (indexList1.size() != indexList2.size()) {
			errorMessage = "Unequal number of child element nodes for '" + elem1Name + "' left: " + indexList1.size() + " right: " + indexList2.size()  + " PATH:" + path;
			LOG.error(errorMessage);
			errorList.add(errorMessage);
			areEqual = false;
		}
		if (areEqual) {

			for (int iIndex=0; iIndex<indexList1.size(); iIndex++) {
				Element child1 = (Element) children1.item(indexList1.get(iIndex));
				Element child2 = (Element) children2.item(indexList2.get(iIndex));
				if (compareElements(child1, child2) ) {
					LOG.debug("Child nodes are equal left: " + child1.getLocalName() + ", right: " + child2.getLocalName());
				} else {
					errorMessage = "Unequal child element nodes left: " + child1.getLocalName() + ", right: " + child2.getLocalName() + " PATH:" + getXPath(child1);
					LOG.error(errorMessage);
					errorList.add(errorMessage);
					areEqual = false;
				}			
			}
		}
		
		return areEqual;
	}
	
	private List<Integer> getElementIndeces(NodeList nodeList) {
		List<Integer> indexList = new ArrayList<Integer>();
		for (int i=0; i<nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			if (childNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			indexList.add(Integer.valueOf(i));
		}
		return indexList;
	}
	
	private boolean compareAttributes(Element elem1, Element elem2, String path) {
		boolean areEqual = true;
		String errorMessage = null;
		NamedNodeMap attrMap1 = elem1.getAttributes();
		NamedNodeMap attrMap2 = elem2.getAttributes();
		for (int i1=0; i1<attrMap1.getLength(); i1++) {
			Attr attr1 = (Attr) attrMap1.item(i1);
			String attr1Name = attr1.getName();
			Attr attr2 = (Attr) attrMap2.getNamedItem(attr1Name);
			if (attr2 == null) {
				errorMessage = "Missing attribute " + attr1.getNodeName() + " PATH: " + path ;
				LOG.error(errorMessage);
				errorList.add(errorMessage);
				areEqual = false;
			} else {
				String value1 = attr1.getValue();
				String value2 = attr2.getValue();
				if (value1 == null && value2 == null) {
					continue;
				} else if (value1 == null && value2 != null) {
					errorMessage = "Attribute value (left) missing, value (right) = " + value2 + " PATH: " + path ;
					LOG.error(errorMessage);
					errorList.add(errorMessage);
					areEqual = false;
				} else if (value1 != null && value2 == null) {
					errorMessage = "Attribute value (right) missing, value (left) = " + value1 + " PATH: " + path ;
					LOG.error(errorMessage);
					errorList.add(errorMessage);
					areEqual = false;
				} else if (!(value1.equals(value2))) {
					errorMessage = "Attribute value (right) missing, value (left) = " + value1 + " PATH: " + path ;
					LOG.error(errorMessage);
					errorList.add(errorMessage);
					areEqual = false;
				}
			}
		}
		return areEqual;
	}
	
	private String getXPath(Element element) {
		StringBuilder sb = new StringBuilder();
		getXPath(element, sb);
		return sb.toString();
	}
	
	private void getXPath(Element elem, StringBuilder sb) {
		String elementString = "/" + elem.getLocalName();
		Node parentNode = elem.getParentNode();
		if (parentNode == null || parentNode.getNodeType() != Node.ELEMENT_NODE) {
			sb.insert(0, elementString);
			return; 
		}
		Element parent = (Element) elem.getParentNode();
		NodeList children = parent.getChildNodes();
		int index = 0;
		for (int i=0; i<children.getLength(); i++) {
			Node childNode = children.item(i);
			if (childNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element child = (Element) childNode;
			if (child.getLocalName().equals(elem.getLocalName())) {
				if (child.isSameNode(elem)) {
					break;
				}
				index++;
			}
		}
		elementString += "[" + Integer.toString(index) + "]";
		sb.insert(0, elementString);
		getXPath(parent, sb);
	}
	
	public static void main(String[] args) {
		XmlFileCompare comparer = new XmlFileCompare();
		comparer.initializeLog4j();
		
		String inFile1Name = System.getProperty("input.file1","src/test/resources/input1-ocil.xml");
		File inFile1 = checkFile(inFile1Name);
		
		String inFile2Name = System.getProperty("input.file2","src/test/resources/input2-ocil.xml");
		File inFile2 = checkFile(inFile2Name);
		
		String schemaFileName = System.getProperty("schema.file","src/main/resources/ocil.xsd");
		File schemaFile = checkFile(schemaFileName);
		
		LOG.debug("diff starting");
		comparer.compareFiles(inFile1, inFile2, schemaFile);
		LOG.debug("diff complete");
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		String errorMessage = "Error parsing file: " 
			+ (currentFile == null ? "null" : currentFile.getAbsoluteFile())
			+ " - " + e.getMessage();
		LOG.error(errorMessage, e);
		errorList.add(errorMessage);
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		String errorMessage = "Fatal Error parsing file: " 
			+ (currentFile == null ? "null" : currentFile.getAbsoluteFile())
			+ " - " + e.getMessage();
		LOG.error(errorMessage, e);
		errorList.add(errorMessage);
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		String errorMessage = "Warning parsing file: " 
			+ (currentFile == null ? "null" : currentFile.getAbsoluteFile())
			+ " - " + e.getMessage();
		LOG.error(errorMessage, e);
		errorList.add(errorMessage);
	}


}
