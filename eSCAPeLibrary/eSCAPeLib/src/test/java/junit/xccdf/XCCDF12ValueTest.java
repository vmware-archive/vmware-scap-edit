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
import com.g2inc.scap.library.domain.xccdf.SelComplexValueElement;
import com.g2inc.scap.library.domain.xccdf.SelValueElement;
import com.g2inc.scap.library.domain.xccdf.Value;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

/**
 * Testcases related to the benchmark document itself.
 */
public class XCCDF12ValueTest extends TestCaseAbstract
{
	private static Logger LOG = Logger.getLogger(XCCDF12ValueTest.class);
	
	
	public XCCDF12ValueTest(String name)
	{
		super(name);
	}
	
	/**
	 * Read in the document and compare profile counts.
	 * 
	 * @throws Exception
	 */
	public void testValues() throws Exception
	{	
		String inputFileName = "/win7_Energy_Benchmark-xccdf.xml";
		InputStream is = this.getClass().getResourceAsStream(inputFileName);
		LOG.debug("Input xccdf 1.2 file name: " + inputFileName);
		
		XCCDFBenchmark benchmark = (XCCDFBenchmark) SCAPDocumentFactory.loadDocument(is);
		
		String errors = benchmark.validate();
		LOG.debug("Errors: " + (errors == null ? "null" : errors));

		Assert.assertNotNull(benchmark);
		
		String benchmarkId = benchmark.getId();
		LOG.debug("benchmark id: " + benchmarkId);
		
		Value value = benchmark.findValue("xccdf_gov.nist_value_Specify_the_System_Hibernate_Timeout_Plugged_in_var");
		Assert.assertNotNull(value);
		
		List<SelValueElement> valueElements = value.getValueElementList();
		Assert.assertEquals(2, valueElements.size());
		SelValueElement value0 = valueElements.get(0);
		Assert.assertEquals("3600", (String) value0.getValue());
		Assert.assertNull(value0.getSelector());
		
		SelValueElement value1 = valueElements.get(1);
		Assert.assertEquals("3600", (String) value1.getValue());
		Assert.assertEquals("3600_Seconds", value1.getSelector());
		
		SelComplexValueElement value2 = benchmark.createSelComplexValueElement("complex-value");
		List<String> items = new ArrayList<String>();
		items.add("100");
		items.add("200");
		value2.setItemList(items);
		value2.setSelector("itemList");
		valueElements.add(value2);
		
		
		SelValueElement value3 = benchmark.createSelValueElement();
		value3.setValue("1800");
		value3.setSelector("1800_Seconds");
		valueElements.add(value3);
		
		value.setValueElementList(valueElements);
		
		List<ChoicesElement> choiceList = value.getChoiceList();
		Assert.assertEquals(0, choiceList.size());
		
		ChoicesElement choices = benchmark.createChoicesElement();
		choices.setMustMatch(true);
		choiceList.add(choices);
		
		List<ChoiceAbstract> choiceAbstractList = choices.getChoiceList();
		Assert.assertEquals(0, choiceAbstractList.size());
		
		Choice choice1 = benchmark.createChoice();
		choice1.setChoiceString("MyFirstChoice");
		choiceAbstractList.add(choice1);
		
		Choice choice2 = benchmark.createChoice();
		choice2.setChoiceString("MySecondChoice");
		choiceAbstractList.add(choice2);
		
		ComplexChoice choice3 = benchmark.createComplexChoice();
		List<String> choiceItems = choice3.getItemList();
		choiceItems.add("MyThirdChoice-1");
		choiceItems.add("MyThirdChoice-2");
		choiceItems.add("MyThirdChoice-3");
		choice3.setItemList(choiceItems);
		choiceAbstractList.add(choice3);
		
		choices.setChoiceList(choiceAbstractList);
		
		value.setChoiceList(choiceList);
		
		File outDir = new File("ovalTestOutput");
		File outFile = new File(outDir, "modified_win7_Energy_Benchmark-xccdf.xml");
		
		benchmark.saveAs(outFile);
		
		benchmark = (XCCDFBenchmark) SCAPDocumentFactory.loadDocument(outFile);
		
		value = benchmark.findValue("xccdf_gov.nist_value_Specify_the_System_Hibernate_Timeout_Plugged_in_var");
		Assert.assertNotNull(value);
		
		valueElements = value.getValueElementList();
		Assert.assertEquals(4, valueElements.size());
		value0 = valueElements.get(0);
		Assert.assertEquals("3600", (String) value0.getValue());
		Assert.assertNull(value0.getSelector());
		
		value1 = valueElements.get(1);
		Assert.assertEquals("3600", (String) value1.getValue());
		Assert.assertEquals("3600_Seconds", value1.getSelector());
		
		value2 = (SelComplexValueElement) valueElements.get(2);
		List<String> list = value2.getItemList();
		Assert.assertEquals(2, list.size());
		Assert.assertEquals("100", list.get(0));
		Assert.assertEquals("200", list.get(1));
		
		value3 = valueElements.get(3);
		Assert.assertEquals("1800", (String) value3.getValue());
		Assert.assertEquals("1800_Seconds", value3.getSelector());
		
		choiceList = value.getChoiceList();
		Assert.assertEquals(1, choiceList.size());
		choices = choiceList.get(0);
		Assert.assertTrue(choices.isMustMatch());
		
		choiceAbstractList = choices.getChoiceList();
		Assert.assertEquals(3, choiceAbstractList.size());
		choice1 = (Choice) choiceAbstractList.get(0);
		Assert.assertEquals("MyFirstChoice", choice1.getChoiceString());
		choice2 = (Choice) choiceAbstractList.get(1);
		Assert.assertEquals("MySecondChoice", choice2.getChoiceString());
		choice3 = (ComplexChoice) choiceAbstractList.get(2);
		choiceItems = choice3.getItemList();
		Assert.assertEquals(3, choiceItems.size());
		Assert.assertEquals("MyThirdChoice-1", choiceItems.get(0));
		Assert.assertEquals("MyThirdChoice-2", choiceItems.get(1));
		Assert.assertEquals("MyThirdChoice-3", choiceItems.get(2));
		
		
	}
	
}
