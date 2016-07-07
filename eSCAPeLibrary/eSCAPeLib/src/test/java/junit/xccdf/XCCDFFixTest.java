package junit.xccdf;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.TestCaseAbstract;
import junit.framework.Assert;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.xccdf.Choice;
import com.g2inc.scap.library.domain.xccdf.ChoiceAbstract;
import com.g2inc.scap.library.domain.xccdf.ChoicesElement;
import com.g2inc.scap.library.domain.xccdf.ComplexChoice;
import com.g2inc.scap.library.domain.xccdf.Fix;
import com.g2inc.scap.library.domain.xccdf.FixText;
import com.g2inc.scap.library.domain.xccdf.RatingEnum;
import com.g2inc.scap.library.domain.xccdf.Rule;
import com.g2inc.scap.library.domain.xccdf.SelComplexValueElement;
import com.g2inc.scap.library.domain.xccdf.SelValueElement;
import com.g2inc.scap.library.domain.xccdf.StrategyEnum;
import com.g2inc.scap.library.domain.xccdf.Value;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

/**
 * Testcases related to the benchmark document itself.
 */
public class XCCDFFixTest extends TestCaseAbstract
{
	private static Logger LOG = Logger.getLogger(XCCDFFixTest.class);
	
	
	public XCCDFFixTest(String name)
	{
		super(name);
	}
	
	/**
	 * Read in the document and compare profile counts.
	 * 
	 * @throws Exception
	 */
	public void testFixAndFixText() throws Exception
	{	
		String inputFileName = "/short_fixtext-xccdf.xml";
		InputStream is = this.getClass().getResourceAsStream(inputFileName);
		LOG.debug("Input xccdf file name: " + inputFileName);
		
		XCCDFBenchmark benchmark = (XCCDFBenchmark) SCAPDocumentFactory.loadDocument(is);
		
		String errors = benchmark.validate();
		LOG.debug("Errors: " + (errors == null ? "null" : errors));

		Assert.assertNotNull(benchmark);
		
		String benchmarkId = benchmark.getId();
		LOG.debug("benchmark id: " + benchmarkId);
		
		Rule rule = benchmark.findRule("SV-25257r2_rule");
		Assert.assertNotNull(rule);
		
		List<Fix> fixList = rule.getFixList();
		Assert.assertEquals(2, fixList.size());
		
		Fix fix0 = fixList.get(0);
		Fix fix1 = fixList.get(1);
		
		Assert.assertEquals("F-30098r1_fix", fix0.getId());
		Assert.assertNull(fix0.getSystem());
		
		Assert.assertEquals("cre_mil.disa.fso_25257-7_fix", fix1.getId());
		Assert.assertEquals("http://cre.mite.org/cre.xsd", fix1.getSystem());
		Assert.assertEquals("cre:mil.disa.fso:25257-7", fix1.getText());
		
		List<FixText> fixTextList = rule.getFixTextList();
		FixText fixText0 = fixTextList.get(0);
		System.out.println("fixTextO: " + fixText0.toString());
		FixText fixText1 = fixTextList.get(1);
		System.out.println("fixText1: " + fixText1.toString());
		
		//<fixtext fixref="cre_mil.disa.fso_25257-7_fix">Install the latest service pack.</fixtext>
		Assert.assertEquals("Install the latest service pack.", fixText1.getText());
		Assert.assertEquals("cre_mil.disa.fso_25257-7_fix", fixText1.getFixRef());
		
		Fix fix2 = benchmark.createFix();
		fix2.setId("cre_mil.disa.fso_99999-9_fix");
		fix2.setComplexity(RatingEnum.medium);
		fix2.setDisruption(RatingEnum.high);
		fix2.setStrategy(StrategyEnum.disable);
		fix2.setReboot(true);
		fix2.setText("Install a non-existent service pack");
		fix2.setPlatform("MyPlatform");
		fix2.setSystem("Atari Game Console");
		fixList.add(fix2);
		rule.setFixList(fixList);
		
		FixText fixText2 = benchmark.createFixText();
		fixText2.setFixRef(fix2.getId());
		fixText2.setText("Use an abacus instead of a computer");
		fixText2.setComplexity(RatingEnum.medium);
		fixText2.setDisruption(RatingEnum.high);
		fixText2.setStrategy(StrategyEnum.disable);
		fixText2.setReboot(true);
		fixTextList.add(fixText2);
		rule.setFixTextList(fixTextList);
		
		
		File outDir = new File("ovalTestOutput");
		File outFile = new File(outDir, "modified_short_fixtext-xccdf.xmll");
		
		benchmark.saveAs(outFile);
		
		benchmark = (XCCDFBenchmark) SCAPDocumentFactory.loadDocument(outFile);
		
		rule = benchmark.findRule("SV-25257r2_rule");
		Assert.assertNotNull(rule);
		
		fixList = rule.getFixList();
		Assert.assertEquals(3, fixList.size());
		
		fixList = rule.getFixList();
		Assert.assertEquals(3, fixList.size());
		
//		
//		value = benchmark.findValue("xccdf_gov.nist_value_Specify_the_System_Hibernate_Timeout_Plugged_in_var");
//		Assert.assertNotNull(value);
//		
//		valueElements = value.getValueElementList();
//		Assert.assertEquals(4, valueElements.size());
//		value0 = valueElements.get(0);
//		Assert.assertEquals("3600", (String) value0.getValue());
//		Assert.assertNull(value0.getSelector());
//		
//		value1 = valueElements.get(1);
//		Assert.assertEquals("3600", (String) value1.getValue());
//		Assert.assertEquals("3600_Seconds", value1.getSelector());
//		
//		value2 = (SelComplexValueElement) valueElements.get(2);
//		List<String> list = value2.getItemList();
//		Assert.assertEquals(2, list.size());
//		Assert.assertEquals("100", list.get(0));
//		Assert.assertEquals("200", list.get(1));
//		
//		value3 = valueElements.get(3);
//		Assert.assertEquals("1800", (String) value3.getValue());
//		Assert.assertEquals("1800_Seconds", value3.getSelector());
//		
//		choiceList = value.getChoiceList();
//		Assert.assertEquals(1, choiceList.size());
//		choices = choiceList.get(0);
//		Assert.assertTrue(choices.isMustMatch());
//		
//		choiceAbstractList = choices.getChoiceList();
//		Assert.assertEquals(3, choiceAbstractList.size());
//		choice1 = (Choice) choiceAbstractList.get(0);
//		Assert.assertEquals("MyFirstChoice", choice1.getChoiceString());
//		choice2 = (Choice) choiceAbstractList.get(1);
//		Assert.assertEquals("MySecondChoice", choice2.getChoiceString());
//		choice3 = (ComplexChoice) choiceAbstractList.get(2);
//		choiceItems = choice3.getItemList();
//		Assert.assertEquals(3, choiceItems.size());
//		Assert.assertEquals("MyThirdChoice-1", choiceItems.get(0));
//		Assert.assertEquals("MyThirdChoice-2", choiceItems.get(1));
//		Assert.assertEquals("MyThirdChoice-3", choiceItems.get(2));
		
		
	}
	
}
