package junit.ocil;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.model.ocil.OcilDocument;

public class OciFindOrphanTest extends TestCase {
	
	private static Logger LOG = Logger.getLogger(OciFindOrphanTest.class);


	public OciFindOrphanTest(String name) {
		super(name);
	}
	
	
	public void testFindOrphan() throws Exception {
		
		String inputFileName = "/gOCIL_Favorite_With_Unused-ocil.xml";
		InputStream is = this.getClass().getResourceAsStream(inputFileName);
		LOG.debug("Input file name: " + inputFileName);
		
		File inputFile = File.createTempFile("testOcil", "-ocil.xml");
		FileUtils.copyInputStreamToFile(is, inputFile);
		inputFile.deleteOnExit();
		
		SCAPContentManager scm = SCAPContentManager.getInstance(inputFile);
		SCAPDocument scapDoc = scm.getDocument(inputFile.getAbsolutePath());
		
		String validationResult = scapDoc.validate();
		if (validationResult != null) {
			throw new IllegalArgumentException("INPUT FILE " + inputFile.getAbsolutePath() + " IS INVALID:\n" + validationResult);
		}
		LOG.debug("Incoming file passes validation: " + inputFile.getAbsolutePath());
		
		OcilDocument ocilDoc = (OcilDocument) scapDoc;
		Set<String> orphanIds = ocilDoc.findOrphans();
		
		Assert.assertEquals(2, orphanIds.size());
		
		LOG.debug("Orphan Id set contains " + orphanIds.size() + " ids");
		for (String orphanId : orphanIds) {
			LOG.debug("\t" + orphanId);
			Assert.assertTrue(orphanId.endsWith("399"));
		}
		
	}
	

}
