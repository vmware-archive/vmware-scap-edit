package junit.ocil;

import java.io.File;
import java.util.List;

import junit.TestCaseAbstract;

import org.apache.log4j.Logger;

import com.g2inc.scap.util.XmlFileCompare;

public class OcilFileCompareTest extends TestCaseAbstract {
	
	private static Logger LOG = Logger.getLogger(OcilFileCompareTest.class);

	public OcilFileCompareTest(String name) {
		super(name);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void testCompare() throws Exception {
		XmlFileCompare comparer = new XmlFileCompare();
		
		String inFile1Name = System.getProperty("input.file1","src/test/resources/input1-ocil.xml");
		File inFile1 = checkFile(inFile1Name);
		
		String inFile2Name = System.getProperty("input.file2","src/test/resources/input2-ocil.xml");
		File inFile2 = checkFile(inFile2Name);
		
		String schemaFileName = System.getProperty("schema.file","src/main/resources/ischema/ocil/ocil.xsd");
		File schemaFile = checkFile(schemaFileName);
		
		LOG.debug("diff starting");
		List<String> errorList = comparer.compareFiles(inFile1, inFile2, schemaFile);
		LOG.debug("diff complete");
		LOG.debug((errorList.size() > 0 ? "Error List:" : "No Errors"));
		for (String errorString : errorList) {
			LOG.debug(errorString);
		}
		assertEquals(0, errorList.size());
	}
	
	private static File checkFile(String filename) {
		File file = new File(filename);
		if (!(file.exists())) {
			throw new IllegalArgumentException("File does not exist: " + filename);
		}
		return file;
	}

}
