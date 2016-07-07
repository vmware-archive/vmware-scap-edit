package junit;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.XCCDFBuilder;
import com.g2inc.scap.library.domain.oval.XCCDFBuilderParameters;
import com.g2inc.scap.library.domain.xccdf.Check;
import com.g2inc.scap.library.domain.xccdf.CheckContentRef;
import com.g2inc.scap.library.domain.xccdf.Group;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.Rule;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.util.CommonUtil;
import com.g2inc.scap.model.ocil.OcilDocument;

/**
 * Testcases related to the scap data-stream-collection document.
 */
public class BuildXccdfTest extends TestCaseAbstract
{
	private static Logger LOG = Logger.getLogger(BuildXccdfTest.class);
	
	public BuildXccdfTest(String name) {
		super(name);
	}
	
	/**
	 * Read in the document and compare profile counts.
	 * 
	 * @throws Exception
	 */
	public void testFromOval() throws Exception
	{	
		// first import an scap 1.2 data-stream-collection, 
		// make sure we can access fields in it, and in the component documents
		String inputFileName = "/check-dups-oval.xml";
		InputStream is = this.getClass().getResourceAsStream(inputFileName);
		LOG.debug("Input oval file name: " + inputFileName);
		
		OvalDefinitionsDocument ovalDoc = (OvalDefinitionsDocument) SCAPDocumentFactory.loadDocument(is);
		
		inputFileName = inputFileName.substring(1);
		ovalDoc.setFilename(inputFileName);
		
		XCCDFBuilder builder = new XCCDFBuilder();
        XCCDFBuilderParameters buildParms = new XCCDFBuilderParameters();
        
        File dir = new File("ovalTestOutput");
        File xccdfFile = new File(dir, "scap_MAC-1_Public-xccdf.xml");

        String profId =  CommonUtil.toNCName("xccdf_com.company_profile_MAC-1_Public");
        String groupId = CommonUtil.toNCName("xccdf_com.company_group_MAC-1_Public");

        buildParms.setBenchmarkDescription("File content for oval file " + inputFileName );
        buildParms.setBenchmarkId("xccdf_com.company_benchmark_MAC-1_Public");
        buildParms.setGroupId(groupId);
        buildParms.setProfileId(profId);
        buildParms.setXccdfFileName(xccdfFile.getAbsolutePath());
        buildParms.setGroupTitle("Title for " + groupId);
        buildParms.setProfileTitle("Title for " + profId);
        buildParms.setSourceDoc(ovalDoc);
        
        XCCDFBenchmark benchmark = builder.generateXCCDF(buildParms);
		Assert.assertEquals("xccdf_com.company_benchmark_MAC-1_Public", benchmark.getId());
		
		List<Profile> profileList = benchmark.getProfileList();
		Assert.assertEquals(1, profileList.size());
		
		List<Group> groupList = benchmark.getGroupList();
		Assert.assertEquals(1, groupList.size());
		
		Group group = groupList.get(0);
		Assert.assertEquals(groupId, group.getId());
		
		List<Rule> ruleList = group.getRuleList();
		Assert.assertEquals(1, ruleList.size());
		Rule rule = ruleList.get(0);
		
		List<Check> checkList = rule.getCheckList();
		Assert.assertEquals(1, checkList.size());
		
		Check check = checkList.get(0);
		Assert.assertEquals(OvalDefinition.OVAL_NAMESPACE, check.getSystem());
		
		List<CheckContentRef> checkContentRefList = check.getCheckContentRefList();
		CheckContentRef checkContentRef = checkContentRefList.get(0);
		Assert.assertEquals(1, checkContentRefList.size());
		
		Assert.assertEquals(inputFileName, checkContentRef.getHref());
		Assert.assertEquals("oval:mil.disa.fso.apache:def:165", checkContentRef.getName());
	
	}
	
	public void testFromOcil() throws Exception
	{	
		// first import an scap 1.2 data-stream-collection, 
		// make sure we can access fields in it, and in the component documents
		String inputFileName = "/pizza-ocil.xml";
		InputStream is = this.getClass().getResourceAsStream(inputFileName);
		LOG.debug("Input ocil file name: " + inputFileName);
		
		SCAPDocument ocilDoc = (SCAPDocument) SCAPDocumentFactory.loadDocument(is);
		
		inputFileName = inputFileName.substring(1);
		ocilDoc.setFilename(inputFileName);
		
		XCCDFBuilder builder = new XCCDFBuilder();
        XCCDFBuilderParameters buildParms = new XCCDFBuilderParameters();
        
        File dir = new File("ovalTestOutput");
        File xccdfFile = new File(dir, "pizza-xccdf.xml");

        String profId =  CommonUtil.toNCName("xccdf_com.g2_profile_pizza");
        String groupId = CommonUtil.toNCName("xccdf_com.g2_group_pizza");

        buildParms.setBenchmarkDescription("File content for oval file " + inputFileName );
        buildParms.setBenchmarkId("xccdf_com.g2_benchmark_pizza");
        buildParms.setGroupId(groupId);
        buildParms.setProfileId(profId);
        buildParms.setXccdfFileName(xccdfFile.getAbsolutePath());
        buildParms.setGroupTitle("Title for " + groupId);
        buildParms.setProfileTitle("Title for " + profId);
        buildParms.setSourceDoc(ocilDoc);
        
        XCCDFBenchmark benchmark = builder.generateXCCDF(buildParms);
		Assert.assertEquals("xccdf_com.g2_benchmark_pizza", benchmark.getId());
		
		List<Profile> profileList = benchmark.getProfileList();
		Assert.assertEquals(1, profileList.size());
		
		List<Group> groupList = benchmark.getGroupList();
		Assert.assertEquals(1, groupList.size());
		
		Group group = groupList.get(0);
		Assert.assertEquals(groupId, group.getId());
		
		List<Rule> ruleList = group.getRuleList();
		Assert.assertEquals(5, ruleList.size());
		Rule rule = ruleList.get(0);
		
		List<Check> checkList = rule.getCheckList();
		Assert.assertEquals(1, checkList.size());
		
		Check check = checkList.get(0);
		Assert.assertEquals(OcilDocument.OCIL_NAMESPACE, check.getSystem());
		
		List<CheckContentRef> checkContentRefList = check.getCheckContentRefList();
		CheckContentRef checkContentRef = checkContentRefList.get(0);
		Assert.assertEquals(1, checkContentRefList.size());
		
		Assert.assertEquals(inputFileName, checkContentRef.getHref());
		Assert.assertEquals("ocil:com.g2-inc:questionnaire:1001", checkContentRef.getName());
	
	}
		
}
