package junit.xccdf;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import junit.TestCaseAbstract;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.xccdf.*;
import com.g2inc.scap.library.domain.xccdf.impl.XCCDFBenchmark;

/**
 * Testcases related to the benchmark document itself.
 */
public class XCCDFBenchmarkTests extends TestCaseAbstract
{
	public Properties props = null;
	public SCAPContentManager scm = null;
	public String xccdfFilePath = null;
	public File xmlFile = null;
	
	@Override
	public void setUp() throws Exception
	{
		props = loadProperties(this.getClass().getResourceAsStream("expected.properties"));
		assertNotNull(props);
		assertFalse(props.size() == 0);

		xccdfFilePath = props.getProperty("xccdf.file");
		assertNotNull(xccdfFilePath);

		URL xmlFileUrl = this.getClass().getResource(xccdfFilePath);
		
		xmlFile = new File(xmlFileUrl.toURI());
		
		assertTrue(xmlFile.exists());
		
		scm = SCAPContentManager.getInstance(xmlFile);
	}
	
	@Override
	public void tearDown() throws Exception
	{
		props = null;
		scm.removeAllDocuments();
		scm = null;
		xccdfFilePath = null;
		xmlFile = null;
	}
	
	public XCCDFBenchmarkTests(String name)
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
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		XCCDFBenchmark xd = null;
		
		assertNotNull(sd);
		
		xd = (XCCDFBenchmark) sd;

		List<Profile> profs = xd.getProfileList();

		assertNotNull(profs);
		
		int expectedCount = getPropertyAsInt(props, "profile.total.count");
		
		assertEquals(expectedCount, profs.size());
		
		xd = null;
	}
	
	/**
	 * Read in the document and compare titles.  I'm doing this while Glenn makes changes
	 * necessary to do other, more interesting tests.
	 * 
	 * @throws Exception
	 */
	public void testTitle() throws Exception
	{		
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		XCCDFBenchmark xd = null;
		
		assertNotNull(sd);
		
		xd = (XCCDFBenchmark) sd;

		String title = xd.getTitle();
		//System.out.println("title is:'"+title+"'");

		assertNotNull(title);
		
		String expectedTitle = props.getProperty("xccdf.title");
		
		assertEquals(title, expectedTitle);
		
		xd = null;
	}
	/**
	 * Read in the document and compare the number of selectable items (rules and groups)
	 * to a predetermined value.
	 * 
	 * @throws Exception
	 */
	public void testSelectableItemCount() throws Exception
	{
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		XCCDFBenchmark xd = null;
		
		assertNotNull(sd);
		
		xd = (XCCDFBenchmark) sd;

		List<SelectableItem> si_list = xd.getSelectableItemList();
		//System.out.println("number of selectable items:"+se_list.size());

		assertNotNull(si_list);
		
		int expectedCount = getPropertyAsInt(props, "benchmark.selectableItemsCount");
		
		assertEquals(si_list.size(), expectedCount);
		
		xd = null;
	}
	/**
	 * Read in the document and compare the number of groups to a predetermined value.
	 * 
	 * @throws Exception
	 */
	public void testTotalGroupCount() throws Exception
	{
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		XCCDFBenchmark xd = null;
		
		assertNotNull(sd);
		
		xd = (XCCDFBenchmark) sd;

		List<Group> group_list = xd.getAllGroups();
		//System.out.println("number of groups:"+group_list.size());

		assertNotNull(group_list);
		
		int expectedCount = getPropertyAsInt(props, "benchmark.totalGroupCount");
		
		assertEquals(group_list.size(), expectedCount);
		
		xd = null;
	}
	/**
	 * Read in the document and compare the number of rules to a predetermined value.
	 * 
	 * @throws Exception
	 */
	public void testTotalRuleCount() throws Exception
	{
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		XCCDFBenchmark xd = null;
		
		assertNotNull(sd);
		
		xd = (XCCDFBenchmark) sd;

		List<Rule> rule_list = xd.getAllRules();
		//System.out.println("number of rules:"+rule_list.size());

		assertNotNull(rule_list);
		
		int expectedCount = getPropertyAsInt(props, "benchmark.totalRuleCount");
		
		assertEquals(rule_list.size(), expectedCount);
		
		xd = null;
	}
	public void testAddGroup() throws Exception
	{
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		XCCDFBenchmark xd = null;
		
		assertNotNull(sd);
		
		xd = (XCCDFBenchmark) sd;
		CheckContentRef ccr=xd.createCheckContentRef();
		ccr.setHref("fdcc-winvista-oval.xml");
		ccr.setName("oval:gov.nist.fdcc.vista:def:6999");
		CheckExport ce=xd.createCheckExport();
		ce.setValueId("account_lockout_duration_var");
		ce.setExportName("oval:gov.nist.fdcc.vista:var:60071");
		Check new_check = xd.createCheck();
		new_check.setId("my_new_check");
		new_check.setSystem("http://oval.mitre.org/XMLSchema/oval-definitions-5");
		new_check.addCheckContentRef(ccr);
		new_check.addCheckExport(ce);
		Group new_group = xd.createGroup();
		new_group.setId("my_new_group");
		new_group.setDescriptionAsString("this is my shiny new group");
		new_group.setSelected(false);
		Rule new_rule=xd.createRule();
		new_rule.setId("my_new_rule0");
		new_rule.setSelected(false);
		new_rule.setWeight(10.0);
		new_rule.setTitle("my new rule");
		new_rule.addCheck(new_check);
		new_group.addRule(new_rule);
		xd.addGroup(new_group);
		List<Rule> rule_list = xd.getAllRules();
		assertNotNull(rule_list);
		int expectedCount = getPropertyAsInt(props, "benchmark.totalRuleCount");
		System.out.println("looking for:"+(expectedCount+1)+" found:"+rule_list.size());
		assertEquals(rule_list.size(), expectedCount+1);
		xd.validate();
		List<SelectableItem> si_list=xd.getSelectableItemList();
		assert(si_list.remove(new_group)==true);
		xd.setSelectablItemList(si_list);
		xd.validate();
	}
	public void testAddProfile()
	{
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		XCCDFBenchmark xd = null;
		
		assertNotNull(sd);
		
		xd = (XCCDFBenchmark) sd;
		ProfileSelect my_select=xd.createProfileSelect();
		my_select.setIdRef("a_rule");
		my_select.setSelected(true);
		ProfileRefineRule my_refine_rule=xd.createProfileRefineRule();
		my_refine_rule.setIdRef("some_rule_or_other");
		my_refine_rule.setWeight(10.0);
		my_refine_rule.setSeverity(SeverityEnum.high);
		ProfileRefineValue my_refine_value=xd.createProfileRefineValue();
		my_refine_value.setIdRef("a_value_reference");
		my_refine_value.setOperator(ValueOperatorEnum.GREATER_THAN_OR_EQUAL);
		ProfileSetValue my_set_value=xd.createProfileSetValue();
		my_set_value.setIdRef("some value");
		my_set_value.setValue("bogus");
		Profile my_profile=xd.createProfile();
		my_profile.addProfileSelect(my_select);
		my_profile.addProfileRefineRule(my_refine_rule);
		my_profile.addProfileRefineValue(my_refine_value);
		my_profile.addProfileSetValue(my_set_value);
		xd.addProfile(my_profile);
		List<Profile> prof_list=xd.getProfileList();
		int expectedCount = getPropertyAsInt(props, "profile.total.count");
		
		assertEquals((expectedCount+1), prof_list.size());
		String error_message=xd.validate();
		assert(error_message==null||error_message.length()<1);
	}
	
	public void testAddProfileSelect() throws IOException
	{
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		XCCDFBenchmark xd = null;
		
		assertNotNull(sd);
		
		xd = (XCCDFBenchmark) sd;
		List<Profile> profileList = xd.getProfileList();
		assertTrue(profileList.size() == 5);
		Profile profile = profileList.get(0);
		assertNotNull(profile);
		assertTrue(profile.getId().equals("low_800_53"));
		ProfileSelect profileSelect = xd.createProfileSelect();
		profileSelect.setIdRef("Always-Use-Classic-Logon");
		profileSelect.setSelected(true);
		List<Remark> remarkList = new ArrayList<Remark>();
		Remark remark = xd.createRemark();
		remark.setText("Wow, I created a Remark!!");
		remarkList.add(remark);
		profileSelect.setRemarkList(remarkList);
		profile.addProfileSelect(profileSelect);
		
		StringBuilder sb = new StringBuilder(xmlFile.getAbsolutePath());
		int nameEnd = sb.lastIndexOf(".xml");
		sb.insert(nameEnd, "1");
		xd.saveAs(sb.toString());
		
		xd = (XCCDFBenchmark) scm.getDocument(xmlFile.getAbsolutePath());
		profile = profileList.get(0);
		assertNotNull(profile);
		assertTrue(profile.getId().equals("low_800_53"));
		ProfileSelect profileSelect2 = xd.createProfileSelect();
		profileSelect2.setIdRef("audit-use-backup-restore-privilege");
		profileSelect2.setSelected(true);
		List<Remark> remarkList2 = new ArrayList<Remark>();
		Remark remark2 = xd.createRemark();
		remark2.setText("Wow, I created another Remark!!");
		remarkList2.add(remark2);
		profileSelect2.setRemarkList(remarkList2);
		List<ProfileChild> children = profile.getChildren();
		children.add(profileSelect2);
		profile.setChildren(children);
		
		sb = new StringBuilder(xmlFile.getAbsolutePath());
		nameEnd = sb.lastIndexOf(".xml");
		sb.insert(nameEnd, "2");
		xd.saveAs(sb.toString());
		
	}	
	
	/**
	 * Read in the document and compare profile counts.
	 * 
	 * @throws Exception
	 */
	public void testAllCpeReferences() throws Exception
	{		
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		assertNotNull(sd);
		
		XCCDFBenchmark benchmark = (XCCDFBenchmark) sd;

		Set<String> allCpeSet = benchmark.getAllReferencedCPES();
		List<String> expectedCpeList = Arrays.asList( 
				"cpe:/o:microsoft:windows_xp", 
				"cpe:/a:adobe:reader",
				"cpe:/o:sun:solaris:5.8", 
				"cpe:/o:sun:solaris:5.9", 
				"cpe:/o:sun:solaris:5.10",
				"cpe:/a:microsoft:office:2003",
				"cpe:/a:microsoft:office:2007"
				);
//		System.out.println("Expected CPES:");
//		showCpes(expectedCpeList);
//		System.out.println("Found CPES:");
//		showCpes(allCpeSet);		
		for (String expectedCpe : expectedCpeList) {
			assertTrue("Expected CPE not found: ", allCpeSet.contains(expectedCpe) );
		}
		for (String foundCpe : allCpeSet) {
			assertTrue("Unexpected CPE found: " + foundCpe , expectedCpeList.contains(foundCpe));
		}
	}
	
//	private void showCpes(Collection<String> cpeCollection) {
//		for (String cpe : cpeCollection) {
//			System.out.println("\t" + cpe);
//		}
//	}
}
