package junit.ocil;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.TestCaseAbstract;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.ocil.OcilDocumentImpl;
import com.g2inc.scap.library.domain.ocil.UserImpl;
import com.g2inc.scap.library.util.DateUtility;
import com.g2inc.scap.model.ocil.BooleanQuestion;
import com.g2inc.scap.model.ocil.BooleanQuestionModel;
import com.g2inc.scap.model.ocil.Choice;
import com.g2inc.scap.model.ocil.ChoiceGroup;
import com.g2inc.scap.model.ocil.ChoiceQuestion;
import com.g2inc.scap.model.ocil.Generator;
import com.g2inc.scap.model.ocil.Ocil;
import com.g2inc.scap.model.ocil.Question;
import com.g2inc.scap.model.ocil.Questions;
import com.g2inc.scap.model.ocil.User;

public class OcilTest extends TestCaseAbstract {
	
	private static Logger LOG = Logger.getLogger(OcilTest.class);
	
	public SCAPContentManager scm = null;
	public String filePath = null;
	public File xmlFile = null;

	public OcilTest(String name) {
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
	
	public void testQuestions() throws Exception {
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		assertNotNull(sd);
		
		Ocil ocilDoc = (Ocil) sd;
		Questions questions = ocilDoc.getQuestions();
		List<Question> questionList = questions.getQuestionList();
		List<ChoiceGroup> choiceGroupList = questions.getChoiceGroupList();
		
		int questionCount = Integer.parseInt(props.getProperty("ocil.questions.number"));
		assertEquals(questionCount, questionList.size());	
		
		String booleanId = props.getProperty("ocil.question.boolean1.id");
		String choiceId = props.getProperty("ocil.question.choice1.id");
		BooleanQuestion booleanQ1 = null;
		ChoiceQuestion choiceQ1 = null;
		for (Question question : questionList) {
			if (question.getId().equals(booleanId)) {
				booleanQ1 = (BooleanQuestion) question;
				assertTrue(booleanQ1.getBooleanQuestionModel()== BooleanQuestionModel.valueOf(props.getProperty("ocil.question.boolean1.model")));
				assertTrue(booleanQ1.getQuestionTextList().size() == 1);
				assertTrue(booleanQ1.getQuestionTextList().get(0).getText().startsWith(props.getProperty("ocil.question.boolean1.textstart")));
			} else if (question.getId().equals(choiceId)) {
				choiceQ1 = (ChoiceQuestion) question;
				List<Choice> choices = choiceQ1.getAllChoices();
			}
		}
	}
	
	public void testGenerator() throws Exception {		
		SCAPDocument sd = scm.getDocument(xmlFile.getAbsolutePath());
		assertNotNull(sd);
		
		OcilDocumentImpl ocilDoc = (OcilDocumentImpl) sd;
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
	
	public void testAddAutor() throws Exception {		
		String xmlFileName = xmlFile.getAbsolutePath();
		LOG.debug("Reading ocil file: " + xmlFileName);
		OcilDocumentImpl ocilDoc = (OcilDocumentImpl) scm.getDocument(xmlFileName);
		assertNotNull(ocilDoc);
		
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
		
		User newAuthor1 = ocilDoc.createAuthor();
		newAuthor1.setName("Glenn Strickland");
		List<String> emailList = new ArrayList<String>();
		emailList.add("somefakeemail@yahoo.com");
		emailList.add("someotheremail@yahoo.com");
		newAuthor1.setEmailList(emailList);
		
		List<String> orgList = new ArrayList<String>();
		orgList.add("OAS");
		newAuthor1.setOrganizationList(orgList);
		generator.addAuthor(newAuthor1);
		
		StringBuilder oldFileName = new StringBuilder(ocilDoc.getFilename());
		int logicalNameEnd = oldFileName.lastIndexOf("-oval.xml");
		if (logicalNameEnd == -1) {
			logicalNameEnd = oldFileName.lastIndexOf(".xml");
		}
		assertTrue(logicalNameEnd != -1);
		oldFileName.insert(logicalNameEnd, "1");
		String newFileName = oldFileName.insert(logicalNameEnd, "1").toString();
		
		LOG.debug("Saving as " + newFileName);
		ocilDoc.saveAs(newFileName);
		
		scm = SCAPContentManager.getInstance(new File(newFileName));
		
		ocilDoc = (OcilDocumentImpl) scm.getDocument(newFileName);
		generator = ocilDoc.getGenerator();
		authors = generator.getAuthorList();
		assertTrue(authors.size() == 2);
		
		newAuthor1 = authors.get(1);
		assertTrue(newAuthor1.getName().equals("Glenn Strickland"));
		generator.removeAuthor(newAuthor1);
		
		User newAuthor2 = ocilDoc.createAuthor();
		newAuthor2.setName("Noam Chomsky");
		newAuthor2.setEmailList(emailList);

		generator.addAuthor(newAuthor2);
		
		newFileName = oldFileName.insert(logicalNameEnd, "2").toString();
		ocilDoc.saveAs(newFileName);
	}

}
