package junit;

import java.io.File;
import java.util.List;

import junit.TestCaseAbstract;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.model.ocil.Artifact;
import com.g2inc.scap.model.ocil.Artifacts;
import com.g2inc.scap.model.ocil.BooleanQuestion;
import com.g2inc.scap.model.ocil.BooleanQuestionTestAction;
import com.g2inc.scap.model.ocil.Choice;
import com.g2inc.scap.model.ocil.ChoiceAbstract;
import com.g2inc.scap.model.ocil.ChoiceGroup;
import com.g2inc.scap.model.ocil.ChoiceQuestion;
import com.g2inc.scap.model.ocil.ChoiceQuestionTestAction;
import com.g2inc.scap.model.ocil.CompoundTestAction;
import com.g2inc.scap.model.ocil.ConstantVariable;
import com.g2inc.scap.model.ocil.Document;
import com.g2inc.scap.model.ocil.ExternalVariable;
import com.g2inc.scap.model.ocil.Generator;
import com.g2inc.scap.model.ocil.Instructions;
import com.g2inc.scap.model.ocil.ItemBase;
import com.g2inc.scap.model.ocil.LocalVariable;
import com.g2inc.scap.model.ocil.NamedItemBase;
import com.g2inc.scap.model.ocil.NumericQuestion;
import com.g2inc.scap.model.ocil.NumericQuestionTestAction;
import com.g2inc.scap.model.ocil.OcilDocument;
import com.g2inc.scap.model.ocil.Operation;
import com.g2inc.scap.model.ocil.Pattern;
import com.g2inc.scap.model.ocil.Question;
import com.g2inc.scap.model.ocil.Questionnaire;
import com.g2inc.scap.model.ocil.Questionnaires;
import com.g2inc.scap.model.ocil.QuestionTestAction;
import com.g2inc.scap.model.ocil.Questions;
import com.g2inc.scap.model.ocil.Range;
import com.g2inc.scap.model.ocil.RangeValue;
import com.g2inc.scap.model.ocil.Reference;
import com.g2inc.scap.model.ocil.References;
import com.g2inc.scap.model.ocil.ResultChoice;
import com.g2inc.scap.model.ocil.Step;
import com.g2inc.scap.model.ocil.StringQuestion;
import com.g2inc.scap.model.ocil.StringQuestionTestAction;
import com.g2inc.scap.model.ocil.TestAction;
import com.g2inc.scap.model.ocil.TestActionRef;
import com.g2inc.scap.model.ocil.TestActions;
import com.g2inc.scap.model.ocil.TextType;
import com.g2inc.scap.model.ocil.User;
import com.g2inc.scap.model.ocil.VarSetWhenChoiceRef;
import com.g2inc.scap.model.ocil.VarSetWhenCondition;
import com.g2inc.scap.model.ocil.VarSetWhenPattern;
import com.g2inc.scap.model.ocil.VarSetWhenRange;
import com.g2inc.scap.model.ocil.Variable;
import com.g2inc.scap.model.ocil.VariableSet;
import com.g2inc.scap.model.ocil.Variables;
import com.g2inc.scap.model.ocil.WhenChoice;
import com.g2inc.scap.model.ocil.WhenPattern;
import com.g2inc.scap.model.ocil.WhenRange;
import com.g2inc.scap.util.XmlFileCompare;
import com.g2inc.scap.util.XmlValidator;

public class XmlValidatorTest extends TestCaseAbstract {
	
	private static Logger LOG = Logger.getLogger(XmlValidatorTest.class);
	private File inFile;
	private File schemaFile;

	public XmlValidatorTest(String name) {
		super(name);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		assertNotNull(props);
		assertFalse(props.size() == 0);

		String inFileName = props.getProperty("validator.test.file");
		assertNotNull(inFileName);	
		inFile = new File(inFileName);		
		assertTrue(inFile.exists());
		
		String schemaFileName = props.getProperty("validator.schema.file");
		assertNotNull(schemaFileName);	
		schemaFile = new File(schemaFileName);		
		assertTrue(schemaFile.exists());
	}
	
	@Override
	public void tearDown() throws Exception {
	}
	
	public void testValidate() throws Exception {
		XmlValidator validator = new XmlValidator();
		validator.setSchemaFile(schemaFile);
		validator.validateFile(inFile);
	}
	

}
