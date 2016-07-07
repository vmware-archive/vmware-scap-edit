package com.g2inc.scap.library.domain.ocil.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.g2inc.scap.model.ocil.*;

public class OcilOrphanFinder {
	
	private Ocil ocilDoc;
	Set<String> allIds  = null;
	
	public OcilOrphanFinder(Ocil ocil) {
		this.ocilDoc = ocil;
	}
	
	public Set<String> findOrphans() {
		allIds  = getAllIds();
		// processQuestionnaires and all the mutually recursive routines it calls, will 
		// remove all ids from allIds that are referenced.
		processQuestionnaires(ocilDoc.getQuestionnaires()); 
		return allIds;
	}
	
	private void processQuestionnaires(Questionnaires questionnaires) {
		for (Questionnaire qnre : questionnaires.getQuestionnaireList()) {
			processQuestionnaire(qnre);
		}
	}
	
	private void processQuestionnaire(Questionnaire qnre) {
		Operation operation = qnre.getActions();
		if (operation != null) {
			List<TestActionRef> taRefs = operation.getTestActionRefList();
			for (TestActionRef taRef : taRefs) {
				String taId = taRef.getTestActionRefId();
				TestAction testAction = ocilDoc.getTestAction(taId);
				processTestAction(testAction);
			}
		}
	}
	
	private void processTestAction(TestAction ta) {
		if (allIds.remove(ta.getId())) {
			if (ta instanceof QuestionTestAction) {
				QuestionTestAction qta = (QuestionTestAction) ta;
				String questionId = qta.getQuestionRef();
				Question question = ocilDoc.getQuestion(questionId);
				processQuestion(question);
				
				processResultChoice(((QuestionTestAction) ta).getWhenError());
				processResultChoice(((QuestionTestAction) ta).getWhenUnknown());
				processResultChoice(((QuestionTestAction) ta).getWhenNotApplicable());
				processResultChoice(((QuestionTestAction) ta).getWhenNotTested());
				
				if (qta instanceof BooleanQuestionTestAction) {
					BooleanQuestionTestAction bqta = (BooleanQuestionTestAction) qta;
					processResultChoice(bqta.getWhenTrue());
					processResultChoice(bqta.getWhenFalse());
					
				} else if (qta instanceof ChoiceQuestionTestAction) {
					ChoiceQuestionTestAction cqta = (ChoiceQuestionTestAction) qta;
					List<WhenChoice> choiceList = cqta.getWhenChoiceList();
					for (WhenChoice whenChoice : choiceList) {
						processResultChoice(whenChoice);
						List<ChoiceRef> choiceRefList = whenChoice.getChoiceRefList();
						for (ChoiceRef choiceRef : choiceRefList) {
							String choiceId = choiceRef.getRefId();
							allIds.remove(choiceId);	
						}
					}
					
				} else if (qta instanceof NumericQuestionTestAction) {
					NumericQuestionTestAction nqta = (NumericQuestionTestAction) qta;
					for (WhenEquals whenEquals : nqta.getWhenEqualsList()) {
						processResultChoice(whenEquals);
					}
					for (WhenRange whenRange : nqta.getWhenRangeList()) {
						processResultChoice(whenRange);
					}	
					
				} else if (qta instanceof StringQuestionTestAction) {
					StringQuestionTestAction sqta = (StringQuestionTestAction) qta;
					for (WhenPattern whenPattern : sqta.getWhenPatternList()) {
						processResultChoice(whenPattern);
					}
				}
			}
		}
	}
	
	private void processQuestion(Question question) {
		if (allIds.remove(question.getId())) {
			List<QuestionText> questionTextList = question.getQuestionTextList();
			for (QuestionText questionText : questionTextList) {
				Set<String> varIds = questionText.getSubVarIds();
				for (String varId : varIds) {
					allIds.remove(varId); 
					Variable var = ocilDoc.getVariable(varId);
					processVariable(var);
				}
			}
			if (question instanceof ChoiceQuestion) {
				ChoiceQuestion choiceQuestion = (ChoiceQuestion) question;
				List<ChoiceAbstract> choiceAbstracts = choiceQuestion.getChoiceAndChoiceGroupList();
				for (ChoiceAbstract choiceAbstract : choiceAbstracts) {
					if (choiceAbstract instanceof Choice) {
						Choice choice = (Choice) choiceAbstract;
						processChoice(choice);
					} else if (choiceAbstract instanceof ChoiceGroupRef) {
						ChoiceGroupRef choiceGroupRef = (ChoiceGroupRef) choiceAbstract;
						String choiceGroupId = choiceGroupRef.getRefId();
						ChoiceGroup choiceGroup = ocilDoc.getChoiceGroup(choiceGroupId);
						processChoiceGroup(choiceGroup);
					}
				}
			}
		}
	}
	
	private void processChoiceGroup(ChoiceGroup choiceGroup) {
		if (allIds.remove(choiceGroup.getId())) {
			List<Choice> choices = choiceGroup.getChoices();
			for (Choice choice : choices) {
				processChoice(choice);
			}
		}
	}
	
	private void processChoice(Choice choice) {
		if (allIds.remove(choice.getId())) {
			Variable var = choice.getVariable();
			if (var != null) {
				processVariable(var);
			}
		}
	}
	
	private void processVariable(Variable var) {
		if (allIds.remove(var.getId())) {
			if (var instanceof LocalVariable) {
				LocalVariable localVar = (LocalVariable) var;
				Question question = localVar.getQuestion();
				processQuestion(question);
				List<VariableSet> setList = localVar.getSetList();
				for (VariableSet set : setList) {
					List<VarSetWhenCondition> whenConds = set.getWhenConditions();
					for (VarSetWhenCondition whenCond : whenConds) {
						if (whenCond instanceof VarSetWhenChoiceRef) {
							VarSetWhenChoiceRef whenChoiceRef = (VarSetWhenChoiceRef) whenCond;
							Choice choice = whenChoiceRef.getChoice();
							processChoice(choice);
						}
					}
				}
			}
		}
	}
	
	private void processResultChoice(ResultChoice resultChoice) {
		if (resultChoice != null) {
			TestActionRef taRef = resultChoice.getTestActionRef();
			if (taRef != null) {
				String taId = taRef.getTestActionRefId();
				TestAction ta = ocilDoc.getTestAction(taId);
				processTestAction(ta);
			}
			
			ArtifactRefs artifactRefs = resultChoice.getArtifactRefs();
			if (artifactRefs != null) {
				for (ArtifactRef artifactRef : artifactRefs.getArtifactRefList()) {
					allIds.remove(artifactRef.getRefId());
				}
			}	
		}

	}
	
	private Set<String> getAllIds() {
		
		Set<String> allIds = new HashSet<String>();
		
		Questionnaires questionnaires = ocilDoc.getQuestionnaires();
		if (questionnaires != null) {
			List<Questionnaire> questionnaireList = ocilDoc.getQuestionnaires().getQuestionnaireList();
			for (Questionnaire qnre : questionnaireList) {
				// only "child-only" questionnaires can be referred to by other objects (test_actions);
				// any non-child-only questionnaire is a top-level object, so don't add its id to list
				if (qnre.isChildOnly()) {
					allIds.add(qnre.getId());
				}
			}
		}
		
		TestActions testActions = ocilDoc.getTestActions();
		if (testActions != null) {
			List<TestAction> testActionList = testActions.getTestActionList();
			for (TestAction testAction : testActionList) {
				allIds.add(testAction.getId());
			}
		}
		
		Questions questions = ocilDoc.getQuestions();
		if (questions != null) {
			List<Question> questionList = questions.getQuestionList();
			for (Question question : questionList) {
				allIds.add(question.getId());
			}
		}
		
		Artifacts artifacts = ocilDoc.getArtifacts();
		if (artifacts != null) {
			List<Artifact> artifactList = artifacts.getArtifactList();
			for (Artifact artifact : artifactList) {
				allIds.add(artifact.getId());
			}
		}
		
		Variables variables = ocilDoc.getVariables();
		if (variables != null) {
			List<Variable> variableList = variables.getVariableList();
			for (Variable variable : variableList) {
				allIds.add(variable.getId());
			}
		}
		
		return allIds;
	}

}
