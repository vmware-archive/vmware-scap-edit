package com.g2inc.scap.model.ocil;

import java.util.Set;
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


public interface Ocil extends ModelBase {
    
    public Generator getGenerator();    
    public void setGenerator(Generator generator);
    public Generator createGenerator();
    
    public Questionnaires getQuestionnaires();
    public void setQuestionnaires(Questionnaires questionnaires);
    
    public TestActions getTestActions();
    public void setTestActions(TestActions testActions);
    public Questionnaire createQuestionnaire();
    public BooleanQuestionTestAction createBooleanQuestionTestAction();
    public ChoiceQuestionTestAction createChoiceQuestionTestAction();
    public NumericQuestionTestAction createNumericQuestionTestAction();
    public StringQuestionTestAction createStringQuestionTestAction();
    
    public Questions getQuestions();
    public void setQuestions(Questions questions);
    public Questions createQuestions();
    public BooleanQuestion createBooleanQuestion();
    public ChoiceQuestion createChoiceQuestion();
    public NumericQuestion createNumericQuestion();
    public StringQuestion createStringQuestion();
    
    public Instructions createInstructions();
    public Step createStep();
    public TextType createTitle();
    public Reference createReference();
    public Choice createChoice();
    public ChoiceGroup createChoiceGroup();
	public TextType createDescription();
	public User createAuthor();
	public ConstantVariable createConstantVariable();
	public ExternalVariable createExternalVariable();
	public LocalVariable createLocalVariable();
	public VarSetWhenChoiceRef createVarSetWhenChoiceRef();
	public VarSetWhenPattern createVarSetWhenPattern();
	public VarSetWhenRange createVarSetWhenRange();
	public VariableSet createVariableSet();
	public Variables createVariables();
	public ResultChoice createResultChoice(String tagName);
	public Artifacts createArtifacts();
	public Artifact createArtifact();
	public ArtifactRef createArtifactRef();
	public ArtifactRefs createArtifactRefs();
	public WhenChoice createWhenChoice();
	public WhenRange createWhenRange();
	public WhenEquals createWhenEquals();
	public Range createRange();
	public RangeValue createRangeValue(String tagName);
	public WhenPattern createWhenPattern();
	public Pattern createPattern();
	public TestActions createTestActions();
	public Document createDocument();
	public Questionnaires createQuestionnaires();
	public References createReferences();
	public Operation createOperation();
	public TestActionRef createTestActionRef();
	public ResultChoice createWhenTrue();
	public ResultChoice createWhenFalse();
	public ArtifactResults createArtifactResults();
	
    
    public Artifacts getArtifacts();
    public void setArtifacts(Artifacts artifacts);
    
    public Variables getVariables();
    public void setVariables(Variables variables);
    
    public Results getResults();
    public void setResults(Results results);
    
    public Document getDocument();
    public void setDocument(Document document);
    
    public Variable getVariable(String id); 
    public Question getQuestion(String id);    
    public Choice getChoice(String id);    
    public ChoiceGroup getChoiceGroup(String id);
    public Artifact getArtifact(String id);
    public TestAction getTestAction(String id);
    public Questionnaire getQuestionnaire(String id);
    
	public String getSchemaVersion();
	public void addQuestionnaire(Questionnaire questionnaire);
	public void addTestAction(TestAction testAction);
	public void addQuestion(Question question);
	public void initNewDocument();
	
	public Set<String> findOrphans();
}
