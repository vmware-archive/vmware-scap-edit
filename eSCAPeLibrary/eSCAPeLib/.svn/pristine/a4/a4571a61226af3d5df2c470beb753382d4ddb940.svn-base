package junit.scapstream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.TestCaseAbstract;
import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.datastream.Component;
import com.g2inc.scap.library.domain.datastream.DataStream;
import com.g2inc.scap.library.domain.datastream.DataStreamCollection;
import com.g2inc.scap.library.domain.xccdf.impl.XCCDFBenchmark;

/**
 * Testcases related to the scap data-stream-collection document.
 */
public class ScapstreamJaxbTest1 extends TestCaseAbstract
{
	private static Logger LOG = Logger.getLogger(ScapstreamJaxbTest1.class);
	
	public static JAXBContext JAXB_CONTEXT;
	static {
		try {
			JAXB_CONTEXT = JAXBContext.newInstance("com.g2inc.scap.library.domain.datastream");
		} catch (JAXBException e) {
			LOG.error("Error creating jaxb context for scap datastream", e);
		}
	}
	public ScapstreamJaxbTest1(String name)
	{
		super(name);
	}
	
	/**
	 * Read in the document and compare profile counts.
	 * 
	 * @throws Exception
	 */
	public void testProfileCounts() throws Exception
	{	
		// first import an xccdf 1.2 document, make sure we can access new fields
		String inputFileName = "/scap_gov.nist_USGCB-Windows-7-Energy.xml";
		InputStream is = this.getClass().getResourceAsStream(inputFileName);
		LOG.debug("Input datastreamcollection file name: " + inputFileName);
		
		DataStreamCollection result = null;
		Reader reader = new InputStreamReader(is, "UTF-8");
		Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
		result = (DataStreamCollection) unmarshaller
				.unmarshal(reader);
		
		Assert.assertNotNull(result);
		
		String collectionId = result.getId();
		Assert.assertEquals("scap_gov.nist_collection_USGCB-Windows-7-Energy-1.2.3.1.zip", collectionId);
		
		List<DataStream> dataStreamList = result.getDataStream();
		Assert.assertEquals(1, dataStreamList.size());
		
		DataStream dataStream = dataStreamList.get(0);
		Assert.assertEquals("scap_gov.nist_datastream_USGCB-Windows-7-Energy-1.2.3.1.zip", dataStream.getId());
		
		List<Component> componentList = result.getComponent();
		Assert.assertEquals(4, componentList.size());
		
		Component xccdfComp = findComponentById("scap_gov.nist_comp_USGCB-Windows-7-Energy-xccdf.xml", componentList);
		Assert.assertNotNull(xccdfComp);
		
		Object compObject = xccdfComp.getAny();
		System.out.println("comp object type: " + compObject.getClass().getName());
		if (compObject instanceof Element) {
			Element compElement = (Element) compObject;
			System.out.println("element name = " + compElement.getNodeName());
			String ns = compElement.getNamespaceURI();
			
			System.out.println("namespace: " + ns + ", prefix: " + compElement.getPrefix());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			printDocument(compElement, baos);
			
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			
			XCCDFBenchmark ourBenchmark = (XCCDFBenchmark) SCAPDocumentFactory.loadDocument(bais);
			String title = ourBenchmark.getTitle();
			System.out.println("title: " + title);
			Assert.assertEquals("USGCB: Guidance for Securing Microsoft Windows 7 energy settings", title.trim());
		}

	}
	
	public static void printDocument(Element element, OutputStream out) throws IOException, TransformerException {
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	    transformer.transform(new DOMSource(element), 
	         new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}
	
	public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	    transformer.transform(new DOMSource(doc), 
	         new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}
	
	private Component findComponentById(String id, List<Component> componentList) {
		for (Component comp : componentList) {
			if (comp.getId().equals(id)) {
				return comp;
			}
		}
		return null;
	}
		
}
