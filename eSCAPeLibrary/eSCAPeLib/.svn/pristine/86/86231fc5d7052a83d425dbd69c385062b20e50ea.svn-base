package junit.oval;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.TestCaseAbstract;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.UnsupportedDocumentTypeException;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalStateParameter;
import com.g2inc.scap.library.schema.NameDoc;
import junit.framework.Assert;

public class ParameterOrderTest extends TestCaseAbstract {
	
	private static Logger LOG = Logger.getLogger(ParameterOrderTest.class);

	public ParameterOrderTest(String name) {
		super(name);
	}

	public void testParmeters() throws Exception, IOException, UnsupportedDocumentTypeException {

		String inputFileName = "/fileAndRegTests-oval.xml";
		InputStream is = this.getClass().getResourceAsStream(inputFileName);
		LOG.debug("Input file name: " + inputFileName);

		OvalDefinitionsDocument ovalDoc = (OvalDefinitionsDocument) SCAPDocumentFactory.loadDocument(is);
		OvalState regState = ovalDoc.getOvalState("oval:g2:ste:2");
		Assert.assertNotNull(regState);
		List<OvalStateParameter> regParms = regState.getParameters();
		Assert.assertEquals(1, regParms.size());
		OvalStateParameter valueParm = regParms.get(0);
		Assert.assertEquals("value", valueParm.getElementName());
		String valueValue = valueParm.getValue();
		Assert.assertEquals("myregvalue", valueValue);
		
		OvalStateParameter typeParm = regState.createOvalStateParameter("type");
		typeParm.setValue("reg_dword");
		
		regState.addParameter(typeParm);
		
		File outDir = new File("ovalTestOutput");
		File outFile = new File(outDir, inputFileName.substring(1));
		ovalDoc.saveAs(outFile);
		
		ovalDoc = (OvalDefinitionsDocument) SCAPDocumentFactory.loadDocument(outFile);
		regState = ovalDoc.getOvalState("oval:g2:ste:2");
		regParms = regState.getParameters();
		Assert.assertEquals(2, regParms.size());
		
		// this will pass if ordering bug is there:
//		valueParm = regParms.get(0);
//		Assert.assertEquals("value", valueParm.getElementName());
//		typeParm = regParms.get(1);
//		Assert.assertEquals("type", typeParm.getElementName());
		
		// this will pass if ordering bug is FIXED:
		typeParm = regParms.get(0);
		Assert.assertEquals("type", typeParm.getElementName());
		valueParm = regParms.get(1);
		Assert.assertEquals("value", valueParm.getElementName());

		
		
		
	}
	
	
	
}
