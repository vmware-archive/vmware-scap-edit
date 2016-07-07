package junit.ocil;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.TestCaseAbstract;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.ocil.OcilDocumentImpl;
import com.g2inc.scap.model.ocil.Generator;
import com.g2inc.scap.model.ocil.Ocil;
import com.g2inc.scap.model.ocil.User;

public class OcilApiTest extends TestCaseAbstract {
	
	private static Logger LOG = Logger.getLogger(OcilApiTest.class);
	
	public SCAPContentManager scm = null;
	public String filePath = null;
	public File xmlFile = null;

	public OcilApiTest(String name) {
		super(name);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		assertNotNull(props);
		assertFalse(props.size() == 0);

		filePath = props.getProperty("ocil.file");
		assertNotNull(filePath);
	
		xmlFile = new File("src/test/resources/" + filePath);		
		assertTrue(xmlFile.exists());
		
		scm = SCAPContentManager.getInstance(xmlFile);
	}
	
	@Override
	public void tearDown() throws Exception {
		props = null;
		scm.removeAllDocuments();
		scm = null;
		filePath = null;
		xmlFile = null;
	}
	
	public void testGenerator() throws Exception {		
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		assertNotNull(sd);
		
		Ocil ocilDoc = ((OcilDocumentImpl) sd);
//		Ocil ocil = ocilDoc.getOcil();
//		GeneratorType generator = ocil.getGenerator();
		Generator generator = ocilDoc.getGenerator();
		assertNotNull(generator);
		String prodName = generator.getProductName();
		assertNotNull(prodName);		
		assertEquals(prodName, props.getProperty("ocil.generator.product.name"));
		
		String prodVersion = generator.getProductVersion();
		assertNotNull(prodVersion);		
		assertEquals(prodVersion, props.getProperty("ocil.generator.product.version"));
		
		BigDecimal schemaVersion = generator.getSchemaVersion();
		assertNotNull(schemaVersion);	
		String expectedSchemaString = props.getProperty("ocil.generator.schema.version");
		BigDecimal expectedSchemaVersion = new BigDecimal(expectedSchemaString);
		assertTrue(expectedSchemaVersion.compareTo(schemaVersion) == 0);
		
		List<User> authors = generator.getAuthorList();
		int authorCount = Integer.parseInt(props.getProperty("ocil.generator.author.number"));
		assertEquals(authors.size(), authorCount);
		
		User author = authors.get(0);
		assertNotNull(author);
		String authorName = author.getName();
		assertEquals(authorName, props.getProperty("ocil.generator.author.name"));
		
		List<String> orgs = author.getOrganizationList();
		int orgCount = Integer.parseInt(props.getProperty("ocil.generator.author.organization.number"));
		assertEquals(orgs.size(), orgCount);
		
		assertEquals(orgs.get(0), props.getProperty("ocil.generator.author.organization1"));
		assertEquals(orgs.get(1), props.getProperty("ocil.generator.author.organization2"));
		
		String email = author.getEmailList().get(0);
		assertEquals(email, props.getProperty("ocil.generator.author.email"));
		
		XMLGregorianCalendar actualDate = generator.getTimeStamp();
		LOG.debug("timestamp from generator: " + actualDate.toString());
		String expectedDateString = props.getProperty("ocil.generator.schema.timestamp");
		DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
		XMLGregorianCalendar expectedDate = datatypeFactory.newXMLGregorianCalendar(expectedDateString);
		LOG.debug("timestamp from properties: " + expectedDate.toString());
		assertEquals(expectedDate, actualDate);
		
		sd = null;
	}

}
