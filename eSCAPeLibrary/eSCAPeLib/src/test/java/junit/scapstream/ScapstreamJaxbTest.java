package junit.scapstream;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import junit.TestCaseAbstract;
import junit.framework.Assert;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.datastream.DataStream;
import com.g2inc.scap.library.domain.datastream.DataStreamCollection;
import com.g2inc.scap.library.domain.scap12.Scap12DataStreamCollection;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.ProfileChild;
import com.g2inc.scap.library.domain.xccdf.ProfileSelect;
import com.g2inc.scap.library.domain.xccdf.Remark;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

/**
 * Testcases related to the scap data-stream-collection document.
 */
public class ScapstreamJaxbTest extends TestCaseAbstract
{
	private static Logger LOG = Logger.getLogger(ScapstreamJaxbTest.class);
	
	public static JAXBContext JAXB_CONTEXT;
	static {
		try {
			JAXB_CONTEXT = JAXBContext.newInstance("com.g2inc.scap.library.domain.datastream");
		} catch (JAXBException e) {
			LOG.error("Error creating jaxb context for scap datastream", e);
		}
	}
	public ScapstreamJaxbTest(String name)
	{
		super(name);
	}
	
	/**
	 * Read in the document and compare profile counts.
	 * 
	 * @throws Exception
	 */
	public void testScapDataStream() throws Exception
	{	
		// first import an scap 1.2 data-stream-collection, 
		// make sure we can access fields in it, and in the component documents
		String inputFileName = "/scap_gov.nist_USGCB-Windows-7-Energy.xml";
		InputStream is = this.getClass().getResourceAsStream(inputFileName);
		LOG.debug("Input datastreamcollection file name: " + inputFileName);
		
		Scap12DataStreamCollection collection = 
				new Scap12DataStreamCollection("scap_gov.nist_USGCB-Windows-7-Energy.xml", is);
		
		DataStreamCollection dsc = collection.getDataStreamCollection();
		Assert.assertEquals("scap_gov.nist_collection_USGCB-Windows-7-Energy-1.2.3.1.zip", dsc.getId());
		
		List<XCCDFBenchmark> benchmarkList = collection.getXccdfDocuments();
		Assert.assertEquals(1, benchmarkList.size());
		XCCDFBenchmark benchmark = benchmarkList.get(0);
		
		String title = benchmark.getTitle();
		Assert.assertEquals("USGCB: Guidance for Securing Microsoft Windows 7 energy settings", title.trim());
		
		List<Profile> profileList = benchmark.getProfileList();
		Assert.assertEquals(1, profileList.size());
		
		Profile profile = profileList.get(0);
		ProfileSelect toBeUnselected = null;
		List<ProfileChild> profileChildren = profile.getChildren();
		for (ProfileChild profileChild : profileChildren) {
			if (profileChild.getIdRef().equals("xccdf_gov.nist_rule_Turn_off_the_Display_Plugged_In")) {
				toBeUnselected = (ProfileSelect) profileChild;
			}
		}
		
		Assert.assertNotNull(toBeUnselected);
		toBeUnselected.setSelected(false);
		List<Remark> remarks = toBeUnselected.getRemarkList();
		Assert.assertEquals(0, remarks.size());
		
		Remark remark = benchmark.createRemark();
		remark.setText("This is a FASCINATING Remark!");
		
		List<Remark> updatedRemarkList = new ArrayList<Remark>();
		updatedRemarkList.addAll(remarks);
		updatedRemarkList.add(remark);
		toBeUnselected.setRemarkList(updatedRemarkList);
		
		benchmark.setDirty(true);
		
		List<DataStream> dsList = dsc.getDataStream();
		Assert.assertEquals(1, dsList.size());
		
		DataStream ds = dsList.get(0);
		
		LOG.debug("calling addOrUpdateComponent");
		collection.addOrUpdateComponent(ds, (SCAPDocument) benchmark);
		
//		Profile newProfile = benchmark.createProfile();
//		newProfile.setTitle("Glenns profile title");
//		Description desc = benchmark.createDescription();
//		desc.setText("Glenns profile description...");
//		List<Description> descList = new ArrayList<Description>();
//		newProfile.setDescriptionList(descList);
		
		File outDir = new File("ovalTestOutput");
		File outFile = new File(outDir, "scap_gov.nist_USGCB-Windows-7-Energy.xml");
		
		collection.saveAs(outFile);
		
		LOG.debug("returned from saveAs");
		
		collection = new Scap12DataStreamCollection(outFile);
		benchmarkList = collection.getXccdfDocuments();
		benchmark = benchmarkList.get(0);
		profileList = benchmark.getProfileList();
		profile = profileList.get(0);
		toBeUnselected = null;
		profileChildren = profile.getChildren();
		for (ProfileChild profileChild : profileChildren) {
			if (profileChild.getIdRef().equals("xccdf_gov.nist_rule_Turn_off_the_Display_Plugged_In")) {
				toBeUnselected = (ProfileSelect) profileChild;
			}
		}
		Assert.assertFalse(toBeUnselected.isSelected());
		remarks = toBeUnselected.getRemarkList();
		Assert.assertEquals(1, remarks.size());
		
		remark = remarks.get(0);
		Assert.assertEquals("This is a FASCINATING Remark!", remark.getText());
		
	
	}
		
}
