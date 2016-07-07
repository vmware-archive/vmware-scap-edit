package junit.ocil;

import java.io.File;
import java.util.List;

import junit.TestCaseAbstract;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.ocil.ChoiceGroupRefImpl;
import com.g2inc.scap.library.domain.ocil.ChoiceRefImpl;
import com.g2inc.scap.model.ocil.Artifact;
import com.g2inc.scap.model.ocil.ArtifactRef;
import com.g2inc.scap.model.ocil.ArtifactRefs;
import com.g2inc.scap.model.ocil.Artifacts;
import com.g2inc.scap.model.ocil.BooleanQuestion;
import com.g2inc.scap.model.ocil.BooleanQuestionTestAction;
import com.g2inc.scap.model.ocil.Choice;
import com.g2inc.scap.model.ocil.ChoiceAbstract;
import com.g2inc.scap.model.ocil.ChoiceGroup;
import com.g2inc.scap.model.ocil.ChoiceGroupRef;
import com.g2inc.scap.model.ocil.ChoiceQuestion;
import com.g2inc.scap.model.ocil.ChoiceQuestionTestAction;
import com.g2inc.scap.model.ocil.ChoiceRef;
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

public class OcilCopyTest extends TestCaseAbstract {
	
	private static Logger LOG = Logger.getLogger(OcilCopyTest.class);
	
	private SCAPContentManager scm = null;
	private String inFileName = null;
	private File inFile = null;
	private String outFileName = null;
	private File outFile = null;
	private File schemaFile = null;
	private OcilDocument inDoc = null;
	private OcilDocument outDoc = null;

	public OcilCopyTest(String name) {
		super(name);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		assertNotNull(props);
		assertFalse(props.size() == 0);

		inFileName = props.getProperty("ocil.file");
		assertNotNull(inFileName);	
		inFile = new File("src/test/resources/" + inFileName);		
		assertTrue(inFile.exists());
		
		outFileName = props.getProperty("ocil.copy.output.file");
		assertNotNull(inFileName);	
		outFile = new File("src/test/resources/" + outFileName);		
		
		String schemaFileName = props.getProperty("ocil.schema.file");
		assertNotNull(schemaFileName);	
		schemaFile = new File(schemaFileName);		
		assertTrue(schemaFile.exists());
		
		scm = SCAPContentManager.getInstance(inFile);
	}
	
	@Override
	public void tearDown() throws Exception {
		scm.removeAllDocuments();
		scm = null;
		inFileName = null;
	}
	
	public void testCopy() throws Exception {
		SCAPDocument sdIn = scm.getDocument(inFile.getAbsolutePath());
		assertNotNull(sdIn);
		
		inDoc  = (OcilDocument) sdIn;
		
		SCAPDocument sdOut = SCAPDocumentFactory.createNewDocument(SCAPDocumentTypeEnum.OCIL_2);
		assertNotNull(sdOut);
		outDoc = (OcilDocument) sdOut;
		
		// Copy all generator subfields
		Generator inGen = inDoc.getGenerator();
		Generator outGen = outDoc.createGenerator();
		outDoc.setGenerator(outGen);
		outGen.setProductName(inGen.getProductName());
		outGen.setProductVersion(inGen.getProductVersion());
		outGen.setSchemaVersion(inGen.getSchemaVersion());
		outGen.setTimeStamp(inGen.getTimeStamp());
		for (User inAuthor : inGen.getAuthorList()) {
			User outAuthor = outDoc.createAuthor();
			copyNamedSubFields(inAuthor, outAuthor);
			outAuthor.setEmailList(inAuthor.getEmailList());
			outAuthor.setOrganizationList(inAuthor.getOrganizationList());
			outGen.addAuthor(outAuthor);
		}
		
		Document inDocDocument = inDoc.getDocument();
		if (inDocDocument != null) {
			outDoc.setDocument(copyDocument(inDocDocument));
		}
		
		Questionnaires inQuestionnaires = inDoc.getQuestionnaires();
		outDoc.setQuestionnaires(copyQuestionnaires(inQuestionnaires));
		
		Questions inQuestions = inDoc.getQuestions();
		Questions outQuestions = outDoc.createQuestions();
		outDoc.setQuestions(outQuestions);
		
		copyQuestions(inQuestions, outQuestions);
		
		TestActions inTestActions = inDoc.getTestActions();
		TestActions outTestActions = outDoc.createTestActions();
		copyTestActions(inTestActions, outTestActions);
		outDoc.setTestActions(outTestActions);
		
		sdOut.saveAs(outFile.getAbsolutePath());
		
		// The input has been copied, now make sure the input and output 
		// files are semantically identical.
		
		XmlFileCompare comparer = new XmlFileCompare();		
		LOG.debug("diff starting");
		List<String> errorList = comparer.compareFiles(inFile, outFile, schemaFile);
		
		int errorCount = errorList.size();
		LOG.debug("diff complete, error count = " + errorCount);
		LOG.debug((errorCount > 0 ? "Error List:" : "No Errors"));
		for (String errorString : errorList) {
			LOG.debug(errorString);
		}
		// The sample ocil file from Mitre has an error in it; two elements in the
		// generator element (product_name and product_version) are out of place 
		// according to the sequence in GeneratorType in the ocil schema. When we 
		// generate the copy, these elements are re-arranged into the correct
		// sequence. This causes 13 error messages to be generated when we
		// compare the input and output files semantically.
//		assertTrue(errorCount == 0 || errorCount == 13);
	}
	
	private Document copyDocument(Document in) {
		Document out = outDoc.createDocument();
		out.setTitle(in.getTitle());
		out.setDescriptionList(in.getDescriptionList());
		out.setNoticeList(in.getNoticeList());
		return out;
		
	}
	
	private Questionnaires copyQuestionnaires(Questionnaires in) {
		Questionnaires out = outDoc.createQuestionnaires();
		List<Questionnaire> questionnaireList = in.getQuestionnaireList();
		for (Questionnaire questionnaire : questionnaireList) {
			out.addQuestionnaire(copyQuestionnaire(questionnaire));
		}
		return out;
	}
	
	private Questionnaire copyQuestionnaire(Questionnaire in) {
		Questionnaire out = outDoc.createQuestionnaire();
		out.setId(in.getId());
		copyCompoundTestAction(in, out);
		out.setPriority(in.getPriority());
		out.setChildOnly(in.isChildOnly());
		out.setScope(in.getScope());
		return out;
	}
	
	private void copyCompoundTestAction(CompoundTestAction in, CompoundTestAction out) {
		TextType inTitle = in.getTitle();
		if (inTitle != null) {
			TextType outTitle = outDoc.createTitle();
			copyTextTypeFields(inTitle, outTitle);
			out.setTitle(outTitle);
		}
		TextType inDesc = in.getDescription();
		if (inDesc != null) {
			TextType outDesc = outDoc.createDescription();
			copyTextTypeFields(inDesc, outDesc);
			out.setDescription(outDesc);
		}
		References inRefs = in.getReferences();
		if (inRefs != null) {
			References outRefs = outDoc.createReferences();
			for (Reference inRef : inRefs.getReferenceList()) {
				Reference outRef = outDoc.createReference();
				copyReference(inRef,outRef);
				outRefs.addReference(outRef);
			}
		}
		Operation action = in.getActions();
		if (action != null) {
			out.setActions(copyOperation(action));
		}
		Artifacts inArtifacts = in.getArtifacts();
		if (inArtifacts != null) {
			Artifacts outArtifacts = copyArtifacts(inArtifacts);
			out.setArtifacts(outArtifacts);
		}
	}
	
	private Operation copyOperation(Operation in) {
		Operation out = outDoc.createOperation();
		List<TestActionRef> refList = in.getTestActionRefList();
		for (TestActionRef inRef : refList) {
			out.addTestActionRef(copyTestActionRef(inRef));
		}
		out.setNegate(in.isNegate());
		out.setPriority(in.getPriority());
		out.setNegate(in.isNegate());
		out.setOperation(in.getOperation());
		return out;
	}
	
	private TestActionRef copyTestActionRef(TestActionRef in) {
		TestActionRef out = outDoc.createTestActionRef();
		out.setTestActionRefId(in.getTestActionRefId());
		out.setPriority(in.getPriority());
		out.setNegate(in.isNegate());
		return out;
	}
	
	private void copyQuestionTestAction(QuestionTestAction in, QuestionTestAction out) {
		out.setId(in.getId());
		copyItemSubFields(in, out);
		out.setQuestion(in.getQuestion());
		TextType inTitle = in.getTitle();
		if (inTitle != null) {
			TextType outTitle = outDoc.createTitle();
			copyTextTypeFields(inTitle, outTitle);
			out.setTitle(outTitle);
		}
		if (in.getWhenUnknown() != null) {
			ResultChoice inRC = in.getWhenUnknown();
			ResultChoice outRC = copyResultChoice(inRC, "when_unknown");
			out.setWhenUnknown(outRC);
		} else if (in.getWhenError() != null) {
			ResultChoice inRC = in.getWhenError();
			ResultChoice outRC = copyResultChoice(inRC, "when_error");
			out.setWhenError(outRC);
		} else if (in.getWhenNotApplicable() != null) {
			ResultChoice inRC = in.getWhenNotApplicable();
			ResultChoice outRC = copyResultChoice(inRC, "when_not_applicable");
			out.setWhenNotApplicable(outRC);
		} else if (in.getWhenNotTested() != null) {
			ResultChoice inRC = in.getWhenNotTested();
			ResultChoice outRC = copyResultChoice(inRC, "when_not_tested");
			out.setWhenNotTested(outRC);
		} 
	}
	
	private ResultChoice copyResultChoice(ResultChoice inResultChoice, String resultChoiceType) {
		ResultChoice outResultChoice = outDoc.createResultChoice(resultChoiceType);
		copyResultChoiceFields(inResultChoice, outResultChoice);
		return outResultChoice;
	}

	private void copyResultChoiceFields(ResultChoice in, ResultChoice out) {
		if (in.getResult() != null)
			out.setResult(in.getResult());
		if (in.getTestActionRef() != null)
			out.setTestActionRef(in.getTestActionRef());
		ArtifactRefs inArtifacts = in.getArtifactRefs();
		if (inArtifacts != null) {
			ArtifactRefs outArtifacts = copyArtifactRefs(inArtifacts);
			out.setArtifactRefs(outArtifacts);
		}
	}
	
	private ArtifactRefs copyArtifactRefs(ArtifactRefs in) {
		ArtifactRefs out = outDoc.createArtifactRefs();
		List<ArtifactRef> list = in.getArtifactRefList();
		for (ArtifactRef inRef : list) {
			ArtifactRef outRef = copyArtifactRef(inRef);
			out.addArtifactRef(outRef);
		}
		return out;
	}
	
	private ArtifactRef copyArtifactRef(ArtifactRef in) {
		ArtifactRef out = outDoc.createArtifactRef();
		out.setRefId(in.getRefId());
		out.setRequired(in.isRequired());
		return out;
	}
	
	private Artifacts copyArtifacts(Artifacts in) {
		Artifacts out = outDoc.createArtifacts();
		List<Artifact> list = in.getArtifactList();
		for (Artifact inArt : list) {
			Artifact outArt = copyArtifact(inArt);
			out.addArtifact(outArt);
		}
		return out;
	}
	
	private Artifact copyArtifact(Artifact in) {
		Artifact out = outDoc.createArtifact();
		out.setId(in.getId());
		TextType inTitle = in.getTitle();
		if (inTitle != null) {
			TextType outTitle = outDoc.createTitle();
			copyTextTypeFields(inTitle, outTitle);
			out.setTitle(outTitle);
		}
		TextType inDesc = in.getDescription();
		if (inDesc != null) {
			TextType outDesc = outDoc.createDescription();
			copyTextTypeFields(inDesc, outDesc);
			out.setDescription(outDesc);
		}
		out.setPersistent(in.isPersistent());
		out.setRequired(in.isRequired());
		out.setDatatype(in.getDatatype());
		return out;
	}
		
	
	private void copyTestActions(TestActions in, TestActions out) {
		List<TestAction> inTAList = in.getTestActionList();
		for (TestAction inTA : inTAList) {
			LOG.debug("Processing TestAction " + inTA.getId());
			if (inTA instanceof BooleanQuestionTestAction) {
				BooleanQuestionTestAction inBQTA = (BooleanQuestionTestAction) inTA;
				BooleanQuestionTestAction outBQTA = outDoc.createBooleanQuestionTestAction();
				copyQuestionTestAction(inBQTA, outBQTA);
				out.addTestAction(outBQTA);
				if (inBQTA.getWhenTrue() != null) {
					ResultChoice inRC = inBQTA.getWhenTrue();
					ResultChoice outRC = copyResultChoice(inRC, "when_true");
					outBQTA.setWhenTrue(outRC);
				}
				if (inBQTA.getWhenFalse() != null) {
					ResultChoice inRC = inBQTA.getWhenFalse();
					ResultChoice outRC = copyResultChoice(inRC, "when_false");
					outBQTA.setWhenFalse(outRC);
				}
			} else if (inTA instanceof ChoiceQuestionTestAction) {
				ChoiceQuestionTestAction inCQTA = (ChoiceQuestionTestAction) inTA;
				ChoiceQuestionTestAction outCQTA = outDoc.createChoiceQuestionTestAction();
				copyQuestionTestAction(inCQTA, outCQTA);
				out.addTestAction(outCQTA);
				List<WhenChoice> whenChoiceList = inCQTA.getWhenChoiceList();
				for (WhenChoice inWhen : whenChoiceList) {
					WhenChoice outWhen = outDoc.createWhenChoice();
					copyResultChoiceFields(inWhen, outWhen);
					List<ChoiceRef> choices = inWhen.getChoiceRefList();
					outWhen.setChoiceRefList(choices);
//					for (Choice choice : choices) {
//						outWhen.addChoiceRef(choice.getId());
//					}
					outCQTA.addWhenChoice(outWhen);
				}				
			} else if (inTA instanceof NumericQuestionTestAction) {
				NumericQuestionTestAction inNQTA = (NumericQuestionTestAction) inTA;
				NumericQuestionTestAction outNQTA = outDoc.createNumericQuestionTestAction();
				copyQuestionTestAction(inNQTA, outNQTA);
				out.addTestAction(outNQTA);
				List<WhenRange> whenRangeList = inNQTA.getWhenRangeList();
				for (WhenRange inWhen : whenRangeList) {
					WhenRange outWhen = outDoc.createWhenRange();
					copyResultChoiceFields(inWhen, outWhen);
					List<Range> ranges = inWhen.getRangeList();
					for (Range range : ranges) {
						Range outRange = copyRange(range);
						outWhen.addRange(outRange);
					}
					outNQTA.addWhenRange(outWhen);
				}				
			} else if (inTA instanceof StringQuestionTestAction) {
				StringQuestionTestAction inSQTA = (StringQuestionTestAction) inTA;
				StringQuestionTestAction outSQTA = outDoc.createStringQuestionTestAction();
				copyQuestionTestAction(inSQTA, outSQTA);
				out.addTestAction(outSQTA);
				List<WhenPattern> whenPatternList = inSQTA.getWhenPatternList();
				for (WhenPattern inWhen : whenPatternList) {
					WhenPattern outWhen = outDoc.createWhenPattern();
					copyResultChoiceFields(inWhen, outWhen);
					List<Pattern> patterns = inWhen.getPatternList();
					for (Pattern pattern : patterns) {
						Pattern outPattern = copyPattern(pattern);
						outWhen.addPattern(outPattern);
					}
					outSQTA.addWhenPattern(outWhen);
				}
			}
		}
	}
	
	private void copyQuestions(Questions inQuestions, Questions outQuestions) {
		// must do choice_groups first, to populate outDoc.choiceGroupMap; 
		// map will be needed to resolve choice_group_refs in questions
		List<ChoiceGroup> inChoiceGroupList = inQuestions.getChoiceGroupList();
		for (ChoiceGroup inChoiceGroup : inChoiceGroupList) {
			ChoiceGroup outChoiceGroup = outDoc.createChoiceGroup();
			copyChoiceGroup(inChoiceGroup, outChoiceGroup);
			outQuestions.addChoiceGroup(outChoiceGroup);
		}
		List<Question> questionList = inQuestions.getQuestionList();
		for (Question inQuestion : questionList) {
			LOG.debug("Read question " + inQuestion.getId());
			if (inQuestion instanceof BooleanQuestion) {
				BooleanQuestion inBool = (BooleanQuestion) inQuestion;
				BooleanQuestion outBool = outDoc.createBooleanQuestion();
				copyQuestionFields(inQuestion, outBool);
				outBool.setBooleanQuestionModel(inBool.getBooleanQuestionModel());
				outBool.setDefaultAnswer(inBool.getDefaultAnswer());
				outQuestions.addQuestion(outBool);
			} else if (inQuestion instanceof ChoiceQuestion) {
				ChoiceQuestion inChoiceQ = (ChoiceQuestion) inQuestion;
				ChoiceQuestion outChoiceQ = outDoc.createChoiceQuestion();
				copyQuestionFields(inQuestion, outChoiceQ);
				outChoiceQ.setDefaultAnswer(inChoiceQ.getDefaultAnswer());
				for (ChoiceAbstract inChoiceAbstract : inChoiceQ.getChoiceAndChoiceGroupList()) {
					if (inChoiceAbstract instanceof Choice) {
						Choice outChoice = outDoc.createChoice();
						copyChoice(((Choice) inChoiceAbstract), outChoice); 
						outChoiceQ.addChoice(outChoice);
					} else if (inChoiceAbstract instanceof ChoiceGroupRef) {
						ChoiceGroupRef inChoiceGroupRef = (ChoiceGroupRef) inChoiceAbstract;
						ChoiceGroupRef outChoiceGroupRef = outDoc.createApiElement("choice_group_ref", ChoiceGroupRefImpl.class);
//						copyChoiceGroup(((ChoiceGroup) inChoiceAbstract), outChoiceGroup);
						copyChoiceGroupRef(inChoiceGroupRef, outChoiceGroupRef);
						outChoiceQ.addChoiceGroupRef(outChoiceGroupRef);
					}
				}
				outQuestions.addQuestion(outChoiceQ);
			} else if (inQuestion instanceof NumericQuestion) {
				NumericQuestion inNum = (NumericQuestion) inQuestion;
				NumericQuestion outNum = outDoc.createNumericQuestion();
				copyQuestionFields(inQuestion, outNum);
				outNum.setDefaultAnswer(inNum.getDefaultAnswer());
				outQuestions.addQuestion(outNum);
			} else if (inQuestion instanceof StringQuestion) {
				StringQuestion inStr = (StringQuestion) inQuestion;
				StringQuestion outStr = outDoc.createStringQuestion();
				copyQuestionFields(inQuestion, outStr);
				outStr.setDefaultAnswer(inStr.getDefaultAnswer());	
				outQuestions.addQuestion(outStr);
			}
		}
		Variables inVariables = inDoc.getVariables();
		if (inVariables != null) {
			Variables outVariables = outDoc.createVariables();
			copyVariables(inVariables, outVariables);
			outDoc.setVariables(outVariables);		
		}
	}
	
	private void copyChoiceGroupRef(ChoiceGroupRef in, ChoiceGroupRef out) {
		out.setRefId(in.getRefId());
	}
	
	private void copyChoiceGroup(ChoiceGroup in, ChoiceGroup out) {
		out.setId(in.getId());
		List<Choice> inChoiceList = in.getChoices();
		for (Choice inChoice : inChoiceList) {
			Choice outChoice = outDoc.createChoice();
			copyChoice(inChoice, outChoice);
			out.addChoice(outChoice);
		}
	}
	
	private void copyChoice(Choice in, Choice out) {
		out.setId(in.getId());
		String inValue = in.getChoiceValue();
		if (inValue != null) {
			out.setChoiceValue(inValue);
		}
		out.setVariable(in.getVariable());
	}
	
	private void copyQuestionFields(Question in, Question out) {
		out.setId(in.getId());
		copyItemSubFields(in, out);
		out.setQuestionTextList(in.getQuestionTextList());
		Instructions inInst = in.getInstructions();
		if (inInst != null) {
			Instructions outInst = outDoc.createInstructions();
			copyInstructionsFields(inInst, outInst);
			out.setInstructions(outInst);
		}
	}
	
	private void copyInstructionsFields(Instructions in, Instructions out) {
//		if (in == null) {
//			return;
//		}
		TextType inTitle = in.getTitle();
		if (inTitle != null) {
			TextType outTitle = outDoc.createTitle();
			copyTextTypeFields(inTitle, outTitle);
			out.setTitle(outTitle);
		}
		List<Step> inStepList = in.getStepList();
		for (Step inStep : inStepList) {
			Step outStep = outDoc.createStep();
			copyStep(inStep, outStep);
			out.addStep(outStep);
		}
	}
	
	private void copyStep(Step in, Step out) {
		TextType inDesc = in.getDescription();
		if (inDesc != null) {
			TextType outDesc = outDoc.createDescription();
			copyTextTypeFields(inDesc, outDesc);
			out.setDescription(outDesc);
		}
		out.setDone(in.isDone());
		out.setRequired(in.isRequired());
		for (Step inSubStep : in.getStepList()) {
			Step outSubStep = outDoc.createStep();
			copyStep(inSubStep, outSubStep);
			out.addStep(outSubStep);
		}
		for (Reference inRef : in.getReferenceList()) {
			Reference outRef = outDoc.createReference();
			copyReference(inRef,outRef);
			out.addReference(outRef);
		}
	}
	
	private void copyReference(Reference in, Reference out) {
		out.setHref(in.getHref());
		copyTextTypeFields(in, out);
	}
	
	private void copyTextTypeFields(TextType in, TextType out) {
		out.setLang(in.getLang());
		out.setValue(in.getValue());
	}
	
	private void copyItemSubFields(ItemBase in, ItemBase out) {
		out.setNotesList(in.getNotesList());
	}
	
	private void copyNamedSubFields(NamedItemBase in, NamedItemBase out) {
		out.setNotesList(in.getNotesList());
		out.setName(in.getName());
	}
	
	private void copyVariableFields(Variable in, Variable out) {
		out.setId(in.getId());
		out.setDatatype(in.getDatatype());
		TextType inDesc = in.getDescription();
		if (inDesc != null) {
			TextType outDesc = outDoc.createDescription();
			copyTextTypeFields(inDesc, outDesc);
			out.setDescription(outDesc);
		}		
	}
	
	private void copyVariableSet(VariableSet in, VariableSet out) {
		List<VarSetWhenCondition> conds = in.getWhenConditions();
		for (VarSetWhenCondition cond : conds) {
			if (cond instanceof VarSetWhenPattern) {
				VarSetWhenChoiceRef inWhenChoice = (VarSetWhenChoiceRef) cond;
				VarSetWhenChoiceRef outWhenChoice = outDoc.createVarSetWhenChoiceRef();
				String inChoiceId = inWhenChoice.getChoice().getId();
				outWhenChoice.setChoiceRefId(inChoiceId);				
			} else if (cond instanceof VarSetWhenPattern) {
				VarSetWhenPattern inWhenPat = (VarSetWhenPattern) cond;
				VarSetWhenPattern outWhenPat = outDoc.createVarSetWhenPattern();
				outWhenPat.setPattern(inWhenPat.getPattern());				
			}  else if (cond instanceof VarSetWhenRange) {
				VarSetWhenRange inWhenRange = (VarSetWhenRange) cond;
				VarSetWhenRange outWhenRange = outDoc.createVarSetWhenRange();
				outWhenRange.setMin(inWhenRange.getMin());
				outWhenRange.setMax(inWhenRange.getMax());
			}
			out.addWhenCondition(cond);
		}
		out.setValue(in.getValue());
	}
	
	private void copyVariables(Variables in, Variables out) {
		List<Variable> inVarList = in.getVariableList();
		for (Variable inVar : inVarList) {
			if (inVar instanceof ConstantVariable) {
				ConstantVariable inConstVar = (ConstantVariable) inVar;
				ConstantVariable outConstVar = outDoc.createConstantVariable();
				copyVariableFields(inConstVar, outConstVar);
				outConstVar.setValue(inConstVar.getValue());
				out.addVariable(outConstVar);
			} else if (inVar instanceof ExternalVariable) {
				ExternalVariable inExtVar = (ExternalVariable) inVar;
				ExternalVariable outExtVar = outDoc.createExternalVariable();
				copyVariableFields(inExtVar, outExtVar);
				outExtVar.setDefaultValue(inExtVar.getDefaultValue());
				out.addVariable(outExtVar);
			} else if (inVar instanceof LocalVariable) {
				LocalVariable inLocalVar = (LocalVariable) inVar;
				LocalVariable outLocalVar = outDoc.createLocalVariable();
				copyVariableFields(inLocalVar, outLocalVar);
				List<VariableSet> inSetList = inLocalVar.getSetList();
				outLocalVar.setQuestion(inLocalVar.getQuestion());
				for (VariableSet inVarSet : inSetList) {
					VariableSet outVarSet = outDoc.createVariableSet();
					copyVariableSet(inVarSet, outVarSet);
					outLocalVar.addSet(outVarSet);
				}
				out.addVariable(outLocalVar);
			}
		}
	}
	
	private Range copyRange(Range in) {
		Range out = outDoc.createRange();
		if (in.getMin() != null) {
			out.setMin(copyRangeValue(in.getMin()));
		}
		if (in.getMax() != null) {
			out.setMax(copyRangeValue(in.getMax()));
		}
		return out;
	}
	
	private Pattern copyPattern(Pattern in) {
		Pattern out = outDoc.createPattern();
		out.setValue(in.getValue());
		out.setVariableRef(in.getVariableRef());
		return out;
	}
	
	private RangeValue copyRangeValue(RangeValue in) {
		RangeValue out = outDoc.createRangeValue(in.getElementName());
		out.setVariableRefId(in.getVariableRefId());
		out.setInclusive(in.isInclusive());
		out.setValue(in.getValue());
		return out;
	}

}
