package junit.xccdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import junit.TestCaseAbstract;
import junit.framework.Assert;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.xccdf.*;

/**
 * Testcases related to the benchmark document itself.
 */
public class XCCDF12BenchmarkTest extends TestCaseAbstract
{
	private static Logger LOG = Logger.getLogger(XCCDF12BenchmarkTest.class);
	
	
	public XCCDF12BenchmarkTest(String name)
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
		String inputFileName = "/win7_Energy_Benchmark-xccdf.xml";
		InputStream is = this.getClass().getResourceAsStream(inputFileName);
		LOG.debug("Input xccdf 1.2 file name: " + inputFileName);
		
		XCCDFBenchmark benchmark = (XCCDFBenchmark) SCAPDocumentFactory.loadDocument(is);
		
		String errors = benchmark.validate();
		LOG.debug("Errors: " + (errors == null ? "null" : errors));

		Assert.assertNotNull(benchmark);
		
		String benchmarkId = benchmark.getId();
		LOG.debug("benchmark id: " + benchmarkId);
		
		SCAPDocumentTypeEnum benchmarkType = benchmark.getDocumentType();
		Assert.assertEquals(SCAPDocumentTypeEnum.XCCDF_12, benchmarkType);
		
		List<Profile> profs = benchmark.getProfileList();
		Assert.assertNotNull(profs);
		Assert.assertEquals(1, profs.size());
		
		Profile profile = profs.get(0);
		List<DcStatus> profileDcStatusList = profile.getDcStatusList();
		Assert.assertNotNull(profileDcStatusList);
		LOG.debug("DcStatus list size: " + profileDcStatusList.size());
		Assert.assertEquals(1, profileDcStatusList.size());
		for (DcStatus dcStatus : profileDcStatusList) {
			List<DublinCoreElement> dcElementList = dcStatus.getDublinCoreElements();
			LOG.debug("DublinCoreElement list size: " + dcElementList.size());
			Assert.assertEquals(3, dcElementList.size());
			boolean creatorFound = false;
			for (DublinCoreElement dcElement : dcElementList) {
				String name = dcElement.getName();
				String text = dcElement.getText();
				if (name.equals("creator")) {
					creatorFound = true;
					Assert.assertEquals("G2", text);
				}
				LOG.debug("name: " + name + ", text: " + text);
			}
			Assert.assertTrue(creatorFound);
		}
		
		// then import an xccdf 1.1.4 document, make sure we new fields accessors return null
		inputFileName = "/apache-22-site-xccdf.xml";
		is = this.getClass().getResourceAsStream(inputFileName);
		LOG.debug("Input xccdf 1.1.4 file name: " + inputFileName);
		benchmark = (XCCDFBenchmark) SCAPDocumentFactory.loadDocument(is);
		
		benchmarkType = benchmark.getDocumentType();
		Assert.assertEquals(SCAPDocumentTypeEnum.XCCDF_114, benchmarkType);
		
		profs = benchmark.getProfileList();
		Assert.assertNotNull(profs);
		Assert.assertEquals(1, profs.size());
		
		profile = profs.get(0);
		profileDcStatusList = profile.getDcStatusList();
		Assert.assertNull(profileDcStatusList);

	}
	
}
