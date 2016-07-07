package com.g2inc.scap.library.domain.ocil;
/* ESCAPE Software Copyright 2010 G2, Inc. - All rights reserved.
*
* ESCAPE is open source software distributed under GNU General Public License Version 3.  ESCAPE is not in the public domain 
* and G2, Inc. holds its copyright.  Redistribution and use in source and binary forms, with or without modification, are
* permitted provided that the following conditions are met:

* 1. Redistributions of ESCAPE source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
* 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the ESCAPE Software distribution. 
* 3. Neither the name of G2, Inc. nor the names of any contributors may be used to endorse or promote products derived from this software without specific prior written permission. 

* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES,
* INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
* IN NO EVENT SHALL G2, INC., THE AUTHORS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
* OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
* OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.

* You should have received a copy of the GNU General Public License Version 3 along with this program. 
* If not, see http://www.gnu.org/licenses/ for a copy.
*/

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentClassEnum;
import com.g2inc.scap.library.domain.ocil.util.OcilOrphanFinder;
import com.g2inc.scap.model.ocil.Artifact;
import com.g2inc.scap.model.ocil.ArtifactRef;
import com.g2inc.scap.model.ocil.ArtifactRefs;
import com.g2inc.scap.model.ocil.ArtifactResults;
import com.g2inc.scap.model.ocil.Artifacts;
import com.g2inc.scap.model.ocil.BooleanQuestion;
import com.g2inc.scap.model.ocil.BooleanQuestionTestAction;
import com.g2inc.scap.model.ocil.Choice;
import com.g2inc.scap.model.ocil.ChoiceGroup;
import com.g2inc.scap.model.ocil.ChoiceQuestion;
import com.g2inc.scap.model.ocil.ChoiceQuestionTestAction;
import com.g2inc.scap.model.ocil.ConstantVariable;
import com.g2inc.scap.model.ocil.Document;
import com.g2inc.scap.model.ocil.ExternalVariable;
import com.g2inc.scap.model.ocil.Generator;
import com.g2inc.scap.model.ocil.Instructions;
import com.g2inc.scap.model.ocil.LocalVariable;
import com.g2inc.scap.model.ocil.ModelBase;
import com.g2inc.scap.model.ocil.NumericQuestion;
import com.g2inc.scap.model.ocil.NumericQuestionTestAction;
import com.g2inc.scap.model.ocil.OcilDocument;
import com.g2inc.scap.model.ocil.Operation;
import com.g2inc.scap.model.ocil.Pattern;
import com.g2inc.scap.model.ocil.Question;
import com.g2inc.scap.model.ocil.QuestionTestAction;
import com.g2inc.scap.model.ocil.Questionnaire;
import com.g2inc.scap.model.ocil.Questionnaires;
import com.g2inc.scap.model.ocil.Questions;
import com.g2inc.scap.model.ocil.Range;
import com.g2inc.scap.model.ocil.RangeValue;
import com.g2inc.scap.model.ocil.Reference;
import com.g2inc.scap.model.ocil.References;
import com.g2inc.scap.model.ocil.ResultChoice;
import com.g2inc.scap.model.ocil.Results;
import com.g2inc.scap.model.ocil.Step;
import com.g2inc.scap.model.ocil.StringQuestion;
import com.g2inc.scap.model.ocil.StringQuestionTestAction;
import com.g2inc.scap.model.ocil.TestAction;
import com.g2inc.scap.model.ocil.TestActionRef;
import com.g2inc.scap.model.ocil.TestActions;
import com.g2inc.scap.model.ocil.TextType;
import com.g2inc.scap.model.ocil.User;
import com.g2inc.scap.model.ocil.VarSetWhenChoiceRef;
import com.g2inc.scap.model.ocil.VarSetWhenPattern;
import com.g2inc.scap.model.ocil.VarSetWhenRange;
import com.g2inc.scap.model.ocil.Variable;
import com.g2inc.scap.model.ocil.VariableSet;
import com.g2inc.scap.model.ocil.Variables;
import com.g2inc.scap.model.ocil.WhenChoice;
import com.g2inc.scap.model.ocil.WhenEquals;
import com.g2inc.scap.model.ocil.WhenPattern;
import com.g2inc.scap.model.ocil.WhenRange;

public class OcilDocumentImpl extends SCAPDocument implements OcilDocument, ModelBase {

	private static final Logger LOG = Logger.getLogger(OcilDocumentImpl.class);
	public static final HashMap<String, Integer> OCIL_ORDER = new HashMap<String, Integer>();
	
    static {
        OCIL_ORDER.put("generator", 0);
        OCIL_ORDER.put("document", 1);
        OCIL_ORDER.put("questionnaires", 2);
        OCIL_ORDER.put("test_actions", 3);
        OCIL_ORDER.put("questions", 4);
        OCIL_ORDER.put("artifacts", 5);
        OCIL_ORDER.put("variables", 6);
        OCIL_ORDER.put("results", 7);
    }
    
    private Map<String, Variable> variableMap = null;
    private Map<String, Question> questionMap = null;
    private Map<String, Choice> choiceMap = null;
    private Map<String, ChoiceGroup> choiceGroupMap = null;
    private Map<String, TestAction> testActionMap = null;
    private Map<String, Questionnaire> questionnaireMap = null;
    private Map<String, Artifact> artifactMap = null;
    
    private ModelBaseImpl modelBase;  
    private boolean changed = true;
    
    public OcilDocumentImpl(org.jdom.Document document) {
    	super(document);
    	modelBase = new ModelBaseImpl();
    	modelBase.setOcilDocument(this);
    	modelBase.setElement(this.getElement());
    	modelBase.setRoot(this.getRoot());
    	modelBase.setSCAPDocument(this.getSCAPDocument());
    	modelBase.setDoc(this.getDoc());
    	setDocumentClass(SCAPDocumentClassEnum.OCIL);
    	initMaps();
    	changed = false;
    }
    
    private void initMaps() {
    	variableMap = new HashMap<String, Variable>();
    	questionMap = new HashMap<String, Question>();
    	choiceMap = new HashMap<String, Choice>();
    	choiceGroupMap = new HashMap<String, ChoiceGroup>();
    	testActionMap = new HashMap<String, TestAction>();
    	questionnaireMap = new HashMap<String, Questionnaire>();
    	artifactMap = new HashMap<String, Artifact>();
    	
    	Variables variables = getVariables();
    	if (variables != null) {
    		List<Variable> variableList = variables.getVariableList();
    		if (variableList != null ) {
    			for (Variable variable : variableList) {
    				variableMap.put(variable.getId(), variable);
    			}
    		}
    	}
    	
    	Questions questions = getQuestions();
    	if (questions != null) {
	    	List<ChoiceGroup> choiceGroupList =  questions.getChoiceGroupList();
	    	if (choiceGroupList != null) {
		    	for (ChoiceGroup choiceGroup : choiceGroupList) {
//		    		LOG.debug("Mapping ChoiceGroup " + choiceGroup.getId());
		    		choiceGroupMap.put(choiceGroup.getId(), choiceGroup);
		    		for (Choice choice : choiceGroup.getChoices()) {
		    			choiceMap.put(choice.getId(), choice);
		    		}
		    	}
	    	}
	    	List<Question> questionList = questions.getQuestionList();
	    	if (questionList != null) {
		    	for (Question question : questionList) {
		    		questionMap.put(question.getId(), question);
		    		if (question instanceof ChoiceQuestion) {
		    			ChoiceQuestion choiceQuestion = (ChoiceQuestion) question;
		    			List<Choice> choices = choiceQuestion.getAllChoices();
		    			if (choices != null) {
			    			for (Choice choice : choices) {
			    				choiceMap.put(choice.getId(), choice);
			    			}
		    			}
		    		}
		    	}
	    	}
    	}
    	Artifacts artifacts = getArtifacts();
    	if (artifacts != null) {
	    	for (Artifact artifact : artifacts.getArtifactList()) {
	    		artifactMap.put(artifact.getId(), artifact);
	    	}
    	}
    	TestActions testActions = getTestActions();
    	if (testActions != null) {
	    	List<TestAction> testActionList = testActions.getTestActionList();
	    	if (testActionList != null) {
		    	for (TestAction testAction : testActionList) {
		    		testActionMap.put(testAction.getId(), testAction);
		    		if (testAction instanceof QuestionTestAction) {
		    			QuestionTestAction qta = (QuestionTestAction) testAction;
		    			addArtifactIds(qta.getWhenError());
		    			addArtifactIds(qta.getWhenNotApplicable());
		    			addArtifactIds(qta.getWhenNotTested());
		    			addArtifactIds(qta.getWhenUnknown());
		    			if (testAction instanceof BooleanQuestionTestAction) {
		    				BooleanQuestionTestAction bqta = (BooleanQuestionTestAction) testAction;
		    				addArtifactIds(bqta.getWhenTrue());
		    				addArtifactIds(bqta.getWhenFalse());
		    			} else if (testAction instanceof ChoiceQuestionTestAction) {
		    				ChoiceQuestionTestAction cqta = (ChoiceQuestionTestAction) testAction;
		    				addWhenChoiceArtifactIds(cqta.getWhenChoiceList());
		    			} else if (testAction instanceof NumericQuestionTestAction) {
		    				NumericQuestionTestAction nqta = (NumericQuestionTestAction) testAction;
		    				addWhenRangeArtifactIds(nqta.getWhenRangeList());
		    			} else if (testAction instanceof StringQuestionTestAction) {
		    				StringQuestionTestAction sqta = (StringQuestionTestAction) testAction;
		    				addWhenPatternArtifactIds(sqta.getWhenPatternList());
		    			}
		    		}
		    	}
	    	}
    	}
    	Questionnaires questionnaires = getQuestionnaires();
    	if (questionnaires != null) {
    		List<Questionnaire> questionnaireList = questionnaires.getQuestionnaireList();
    		if (questionnaireList != null) {
		    	for (Questionnaire questionnaire : questionnaireList) {
		    		questionnaireMap.put(questionnaire.getId(), questionnaire);
		    		testActionMap.put(questionnaire.getId(), questionnaire);
		    	}
    		}
    	}
    }
    
	private void addWhenPatternArtifactIds(List<WhenPattern> list) {
    	for (ResultChoice resultChoice : list) {
    		addArtifactIds(resultChoice);
    	}
	}
    
	private void addWhenRangeArtifactIds(List<WhenRange> list) {
    	for (ResultChoice resultChoice : list) {
    		addArtifactIds(resultChoice);
    	}
	}

	private void addWhenChoiceArtifactIds(List<WhenChoice> list) {
    	for (ResultChoice resultChoice : list) {
    		addArtifactIds(resultChoice);
    	}
	}

	private void addArtifactIds(ResultChoice resultChoice) {
		if (resultChoice != null && resultChoice.getArtifactRefs() != null) {
	    	List<ArtifactRef> artifactRefList = resultChoice.getArtifactRefs().getArtifactRefList();
	    	for (ArtifactRef artifactRef : artifactRefList) {
	    		LOG.debug("addArtifactIds called to add ref for " + artifactRef.getRefId());
//	    		artifactMap.put(artifact.getId(), artifact);
	    	}
		}
    }
    
    public Generator getGenerator() {
    	return modelBase.getApiElement("generator", GeneratorImpl.class);
    }
    
    public void setGenerator(Generator generator) {
    	modelBase.setApiElement(generator, getOrderMap(), "generator");
    }
    
    public Questions getQuestions() {
    	return modelBase.getApiElement("questions", QuestionsImpl.class);
    }
    
    public void setQuestions(Questions questions) {
    	modelBase.setApiElement(questions, getOrderMap(), "questions");
    }
    
    public Document getDocument() {
    	return getSCAPElement("document", DocumentImpl.class);
    }
    
    public void setDocument(Document document) {
    	modelBase.setApiElement(document, getOrderMap(), "document");
    }
    
    public Variable getVariable(String id) {
    	checkMaps();
    	return variableMap.get(id);
    }
    
    public boolean isChanged() {
		return changed;
	}

	protected void setChanged(boolean changed) {
		this.changed = changed;
	}

	public Question getQuestion(String id) {
    	checkMaps();
    	return questionMap.get(id);
    }
    
    public Choice getChoice(String id) {
    	checkMaps();
    	return choiceMap.get(id);
    }
    
    public ChoiceGroup getChoiceGroup(String id) {
    	checkMaps();
    	return choiceGroupMap.get(id);
    }    
    
	public String getSchemaVersion() {
		return null;
	}
	
	@Override
	public String validateSymantically() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// nothing to do here...
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return OCIL_ORDER;
	}

	@Override
	public OcilDocument getOcilDocument() {
		return this;
	}

	@Override
	public void setOcilDocument(OcilDocument ocilDoc) {
		throw new IllegalStateException("Can't call setOcilDocument on OcilDocument itself");
	}

	@Override
	public <T extends ModelBase> T createApiElement(String tag, Class<?> clazz) {
		@SuppressWarnings("unchecked")
		T modelBase = (T) createSCAPElement(tag, clazz);
		modelBase.setOcilDocument(this);
		return modelBase;
	}	  	
	
	@Override
	public User createAuthor() {
		return createApiElement("author", UserImpl.class);
	}
	
	public Generator createGenerator() {
		return createApiElement("generator", GeneratorImpl.class);
	}

	@Override
	public Questionnaires getQuestionnaires() {
		return modelBase.getApiElement("questionnaires", QuestionnairesImpl.class);
	}

	@Override
	public void setQuestionnaires(Questionnaires questionnaires) {
		modelBase.setApiElement(questionnaires, getOrderMap(), "questionnaires");
	}

	@Override
	public TestActions getTestActions() {
		return modelBase.getApiElement("test_actions", TestActionsImpl.class);
	}

	@Override
	public void setTestActions(TestActions testActions) {
		modelBase.setApiElement(testActions, getOrderMap(), "test_actions");
	}

	@Override
	public Artifacts getArtifacts() {
		return modelBase.getApiElement("artifacts", ArtifactsImpl.class);
	}

	@Override
	public void setArtifacts(Artifacts artifacts) {
		modelBase.setApiElement(artifacts, getOrderMap(), "artifacts");
	}

	@Override
	public Variables getVariables() {
		return modelBase.getApiElement("variables", VariablesImpl.class);
	}

	@Override
	public void setVariables(Variables variables) {
		modelBase.setApiElement(variables, getOrderMap(), "variables");
	}

	@Override
	public Results getResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResults(Results results) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Artifact getArtifact(String id) {
		checkMaps();
		return artifactMap.get(id);
	}

	@Override
	public TestAction getTestAction(String id) {
		checkMaps();
		return testActionMap.get(id);
	}

	@Override
	public Questionnaire getQuestionnaire(String id) {
		checkMaps();
		return questionnaireMap.get(id);
	}
	
	private void checkMaps() {
		if (changed) {
			changed = false;
			initMaps();
		}
	}
	
	@Override
	public void initNewDocument() {
		Generator generator = createGenerator();
		String schemaVersionString = getSchemaVersion();
		BigDecimal schemaVersionNumber = null;
		try {
			schemaVersionNumber = new BigDecimal(schemaVersionString);
		} catch (NumberFormatException e) {
			LOG.error("Invalid Schema version number: " + schemaVersionString, e);
			schemaVersionNumber = new BigDecimal("2.0");
		}
		generator.setSchemaVersion(schemaVersionNumber);
		generator.setTimeStamp();
		setGenerator(generator);
		
		setQuestionnaires(createQuestionnaires());
		
		setTestActions(createTestActions());
		
		setQuestions(createQuestions());
	}

	@Override
	public Questionnaire createQuestionnaire() {
		return createApiElement("questionnaire", QuestionnaireImpl.class);
	}
	
	@Override
	public Questions createQuestions() {
		return createApiElement("questions", QuestionsImpl.class);
	}

	@Override
	public BooleanQuestionTestAction createBooleanQuestionTestAction() {
		return createApiElement("boolean_question_test_action", BooleanQuestionTestActionImpl.class);
	}

	@Override
	public ChoiceQuestionTestAction createChoiceQuestionTestAction() {
		return createApiElement("choice_question_test_action", ChoiceQuestionTestActionImpl.class);
	}

	@Override
	public NumericQuestionTestAction createNumericQuestionTestAction() {
		return createApiElement("numeric_question_test_action", NumericQuestionTestActionImpl.class);
	}

	@Override
	public StringQuestionTestAction createStringQuestionTestAction() {
		return createApiElement("string_question_test_action", StringQuestionTestActionImpl.class);
	}

	@Override
	public BooleanQuestion createBooleanQuestion() {
		return createApiElement("boolean_question", BooleanQuestionImpl.class);
	}

	@Override
	public ChoiceQuestion createChoiceQuestion() {
		return createApiElement("choice_question", ChoiceQuestionImpl.class);
	}

	@Override
	public NumericQuestion createNumericQuestion() {
		return createApiElement("numeric_question", NumericQuestionImpl.class);
	}

	@Override
	public StringQuestion createStringQuestion() {
		return createApiElement("string_question", StringQuestionImpl.class);
	}

	@Override
	public Instructions createInstructions() {
		return createApiElement("instructions", InstructionsImpl.class);
	}

	@Override
	public Step createStep() {
		return createApiElement("step", StepImpl.class);
	}
	
	@Override
	public TextType createTitle() {
		return createApiElement("title", TextTypeImpl.class);
	}
	
	@Override
	public TextType createDescription() {
		return createApiElement("description", TextTypeImpl.class);
	}	
	
	@Override
	public Reference createReference() {
		return createApiElement("reference", ReferenceImpl.class);
	}

	@Override
	public Choice createChoice() {
		return createApiElement("choice", ChoiceImpl.class);
	}

	@Override
	public ChoiceGroup createChoiceGroup() {
		return createApiElement("choice_group", ChoiceGroupImpl.class);
	}	
	
	@Override
	public ConstantVariable createConstantVariable() {
		return createApiElement("constant_variable", ConstantVariableImpl.class);
	}	
	
	@Override
	public ExternalVariable createExternalVariable() {
		return createApiElement("external_variable", ExternalVariableImpl.class);
	}

	@Override
	public LocalVariable createLocalVariable() {
		return createApiElement("local_variable", LocalVariableImpl.class);
	}

	@Override
	public VarSetWhenChoiceRef createVarSetWhenChoiceRef() {
		return createApiElement("when_choice_ref", VarSetWhenChoiceRefImpl.class);
	}

	@Override
	public VarSetWhenPattern createVarSetWhenPattern() {
		return createApiElement("when_pattern", VarSetWhenPatternImpl.class);
	}

	@Override
	public VarSetWhenRange createVarSetWhenRange() {
		return createApiElement("when_range", VarSetWhenRangeImpl.class);
	}
	
	@Override
	public VariableSet createVariableSet() {
		return createApiElement("set", VariableSetImpl.class);
	}
	
	@Override
	public Variables createVariables() {
		return createApiElement("variables", VariablesImpl.class);
	}

	@Override
	public ResultChoice createResultChoice(String tagName) {
		return createApiElement(tagName, ResultChoiceImpl.class);
	}

	@Override
	public Artifacts createArtifacts() {
		return createApiElement("artifacts", ArtifactsImpl.class);
	}
	
	@Override
	public Artifact createArtifact() {
		return createApiElement("artifact", ArtifactImpl.class);
	}
	
	@Override
	public ArtifactRef createArtifactRef() {
		return createApiElement("artifact_ref", ArtifactRefImpl.class);
	}
	
	@Override
	public ArtifactRefs createArtifactRefs() {
		return createApiElement("artifact_refs", ArtifactRefsImpl.class);
	}
	
	@Override
	public WhenChoice createWhenChoice() {
		return createApiElement("when_choice", WhenChoiceImpl.class);
	}	
	
	@Override
	public WhenRange createWhenRange() {
		return createApiElement("when_range", WhenRangeImpl.class);
	}	
	
	@Override
	public WhenEquals createWhenEquals() {
		return createApiElement("when_equals", WhenEqualsImpl.class);
	}	
	
	@Override
	public Range createRange() {
		return createApiElement("range", RangeImpl.class);
	}
	
	@Override
	public RangeValue createRangeValue(String tagName) {
		return createApiElement(tagName, RangeValueImpl.class);
	}

	@Override
	public WhenPattern createWhenPattern() {
		return createApiElement("when_pattern", WhenPatternImpl.class);
	}
	
	@Override
	public Pattern createPattern() {
		return createApiElement("pattern", PatternImpl.class);
	}
	
	@Override
	public TestActions createTestActions() {
		return createApiElement("test_actions", TestActionsImpl.class);
	}

	@Override
	public Document createDocument() {
		return createApiElement("document", DocumentImpl.class);
	}
	
	@Override
	public Questionnaires createQuestionnaires() {
		return createApiElement("questionnaires", QuestionnairesImpl.class);
	}
	
	@Override
	public References createReferences() {
		return createApiElement("references", ReferencesImpl.class);
	}
	
	@Override
	public Operation createOperation() {
		return createApiElement("actions", OperationImpl.class);
	}
	
	@Override
	public TestActionRef createTestActionRef() {
		return createApiElement("test_action_ref", TestActionRefImpl.class);
	}

	@Override
	public void addQuestionnaire(Questionnaire questionnaire) {
		Questionnaires questionnaires = getQuestionnaires();
		if (questionnaires == null) {
			questionnaires = createQuestionnaires();
			setQuestionnaires(questionnaires);
		}
		questionnaires.addQuestionnaire(questionnaire);
	}

	@Override
	public void addTestAction(TestAction testAction) {
		TestActions testActions = getTestActions();
		if (testActions == null) {
			testActions = createTestActions();
			setTestActions(testActions);
		}
		testActions.addTestAction(testAction);
	}

	@Override
	public void addQuestion(Question question) {
		Questions questions = getQuestions();
		if (questions == null) {
			questions = createQuestions();
			setQuestions(questions);
		}
		questions.addQuestion(question);
	}

	@Override
	public String getIdNamespace() {
		String namespace = null;
		Generator generator = getGenerator();
		if (generator != null) {
			namespace = generator.getIdNamespace();
		}
		return namespace;
	}

	@Override
	public void setIdNamespace(String value) {
		Generator generator = getGenerator();
		if (generator == null) {
			generator = createGenerator();
			setGenerator(generator);
		}
		generator.setIdNamespace(value);
	}

	@Override
	public ResultChoice createWhenTrue() {
		return createResultChoice("when_true");
	}

	@Override
	public ResultChoice createWhenFalse() {
		return createResultChoice("when_false");
	}
	
	@Override
	public ArtifactResults createArtifactResults() {
		return createApiElement("artifact_results", ArtifactResultsImpl.class);
	}
	
	@Override
	public Set<String> findOrphans() {
		OcilOrphanFinder finder = new OcilOrphanFinder(this);
		return finder.findOrphans();
	}
	
}
