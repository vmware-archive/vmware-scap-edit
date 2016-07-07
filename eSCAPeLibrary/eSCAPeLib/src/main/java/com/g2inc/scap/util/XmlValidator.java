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
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlValidator implements ErrorHandler {
	
	private static final Logger LOG = Logger.getLogger(XmlValidator.class);	
	
	private File currentFile;
	private Schema schema;
	
	private List<String> errorList = new ArrayList<String>();
	
	public void setSchemaFile(File schemaFile) throws Exception {
		LOG.debug("Using schema file: " + schemaFile.getAbsolutePath());
		SchemaFactory schemaFactory = 
		    SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		
		Source source = new StreamSource(schemaFile);
		schema = schemaFactory.newSchema(source);
	}
	
	public Document validateFile(File file) throws Exception {
		currentFile = file;
		
		LOG.debug("validating file: " + file.getAbsolutePath());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setValidating(false);  // don't need this set when using external schema
		factory.setNamespaceAware(true);
		factory.setSchema(schema);
		
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		docBuilder.setErrorHandler(this);
		Document doc = docBuilder.parse(file);
		
		currentFile = null;
		return doc;
	}
	
	public Document validateFile(File file, File schemaFile) throws Exception {
		setSchemaFile(schemaFile);
		return validateFile(file);
	}
	
	@Override
	public void error(SAXParseException e) throws SAXException {
		String errorMessage = "Error parsing file: " 
			+ (currentFile == null ? "null" : currentFile.getAbsoluteFile())
			+ " - " + e.getMessage() + "\nline=" + e.getLineNumber() + ", column=" + e.getColumnNumber();
		
		LOG.error(errorMessage, e);
		errorList.add(errorMessage);
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		String errorMessage = "Fatal Error parsing file: " 
			+ (currentFile == null ? "null" : currentFile.getAbsoluteFile())
			+ " - " + e.getMessage() + "\nline=" + e.getLineNumber() + ", column=" + e.getColumnNumber();
		LOG.error(errorMessage, e);
		errorList.add(errorMessage);
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		String errorMessage = "Warning parsing file: " 
			+ (currentFile == null ? "null" : currentFile.getAbsoluteFile())
			+ " - " + e.getMessage() + "\nline=" + e.getLineNumber() + ", column=" + e.getColumnNumber();
		LOG.error(errorMessage, e);
		errorList.add(errorMessage);
	}


}
