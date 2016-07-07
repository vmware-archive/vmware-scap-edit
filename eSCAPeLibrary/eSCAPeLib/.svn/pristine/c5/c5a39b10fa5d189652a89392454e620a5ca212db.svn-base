package junit.oval;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;

public class OvalLargeImportTest extends TestCase {
	
	private static Logger LOG = Logger.getLogger(OvalLargeImportTest.class);


	public OvalLargeImportTest(String name) {
		super(name);
	}
	
	
	public void testLargeImport() throws Exception {
		
		String inputFileName = "/mitre-oval.xml";
		InputStream is = this.getClass().getResourceAsStream(inputFileName);
		LOG.debug("Input file name: " + inputFileName);
		
		long startTime = System.currentTimeMillis();
		LOG.debug("Starting Mitre content load");
		OvalDefinitionsDocument ovalDoc = (OvalDefinitionsDocument) SCAPDocumentFactory.loadDocument(is);
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.SSS");
		dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
		LOG.debug("Finished Mitre content load, duration: " + dateFormat.format(new Date(System.currentTimeMillis() - startTime)));
		
		List<String> defIdList = new LinkedList<String>();
		LOG.debug("Starting iteration over definitions");
		startTime = System.currentTimeMillis();
		List<OvalDefinition> defList = ovalDoc.getOvalDefinitions();
		
		LOG.debug("Found " + defList.size() + " definitions");		
		Assert.assertEquals(13915, defList.size());
		//TODO: add content check to make sure at least some content is right.
	}
	

}
