package com.g2inc.scap.library.domain.oval;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.bundle.SCAPDocumentBundle;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.xccdf.Check;
import com.g2inc.scap.library.domain.xccdf.CheckContentRef;
import com.g2inc.scap.library.domain.xccdf.CheckExport;
import com.g2inc.scap.library.domain.xccdf.Group;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.ProfileSelect;
import com.g2inc.scap.library.domain.xccdf.Rule;
import com.g2inc.scap.library.domain.xccdf.SelValueElement;
import com.g2inc.scap.library.domain.xccdf.SelectableItem;
import com.g2inc.scap.library.domain.xccdf.SelElement;
import com.g2inc.scap.library.domain.xccdf.Value;
import com.g2inc.scap.library.domain.xccdf.ValueTypeEnum;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.util.CommonUtil;

/**
 * This class is used to build an XCCDFBenchmark from an
 * OvalDefinitionsDocument.
 * 
 * @see OvalDefinitionsDocument
 * @see com.g2inc.scap.library.domain.xccdf.impl.XCCDFBenchmark
 */
public class XCCDFBuilder
{
    private static final Logger LOG = Logger.getLogger(XCCDFBuilder.class);
    public static final String OVAL_CHECK_SYSTEM = "http://oval.mitre.org/XMLSchema/oval-definitions-5";
    private XCCDFBuilderParameters parms;
    private SCAPDocument scapDoc;

    /**
     * Using the passed in parameters, build an XCCDFBenchmark and return it.
     * 
     * @param parms
     *            Builder parameters
     * @return XCCDFBenchmark
     * @throws IOException
     */
    public XCCDFBenchmark generateXCCDF(XCCDFBuilderParameters parms)
            throws IOException
    {
        this.parms = parms;
        this.scapDoc = parms.getSourceDoc();

        String scapFileName = scapDoc.getFilename();
        String xccdfFilename = parms.getXccdfFileName();

        if (xccdfFilename == null || xccdfFilename.trim().length() == 0)
        {
            // pick one automatically
            xccdfFilename = CommonUtil.getSuggestedXCCDFFilenameForOvalFilename(scapFileName);
          //  LOG.debug("generateXCCDF got suggested file name: " + xccdfFilename);
        }
        File xccdfFile = new File(xccdfFilename);
        SCAPDocumentTypeEnum type = parms.getXccdfType();
        if (type == null) {
        	//type = SCAPDocumentTypeEnum.XCCDF_114;
        }

        XCCDFBenchmark benchmark = (XCCDFBenchmark) SCAPDocumentFactory.createNewDocument(type);
        benchmark.setFilename(xccdfFile.getAbsolutePath());
       // LOG.debug("benchmark file name: " + benchmark.getFilename());

        String benchmarkId = parms.getBenchmarkId();
        if (benchmarkId == null)
        {
            benchmarkId = "Benchmark_Generated_from_oval_file_"
                    + CommonUtil.sanitize(scapDoc.getFilename());
        }
        benchmark.setId(benchmarkId);
        benchmark.setDescriptionAsString(parms.getBenchmarkDescription());

        Profile profile = benchmark.createProfile();
        String profileId = parms.getProfileId();
        if (profileId == null)
        {
            profileId = "Profile_Generated_from_oval_file_"
                    + CommonUtil.sanitize(scapDoc.getFilename());
        }
        profile.setTitle(profileId);
        profileId = CommonUtil.sanitize(profileId); // get rid of spaces for id
        profile.setId(profileId);
        benchmark.addProfile(profile);
        Group group = benchmark.createGroup();
        String groupId = parms.getGroupId();
        if (groupId == null)
        {
            groupId = "Group_Generated_from_oval_file_"
                    + CommonUtil.sanitize(scapDoc.getFilename());
        }
        groupId = CommonUtil.sanitize(groupId); // get rid of spaces for id
        group.setId(groupId);
        benchmark.addGroup(group);
        
        if (scapDoc instanceof OvalDefinitionsDocument) {
        	buildXccdf(benchmark, (OvalDefinitionsDocument) scapDoc);
        } 
        
        benchmark.save();
        return benchmark;
    }
    
    private String getScapDocHref() {
        String scapDocHref = scapDoc.getFilename();
        int indexLastSeparator = scapDocHref.lastIndexOf(System.getProperty("file.separator"));
        if (indexLastSeparator != -1) {
            scapDocHref = scapDocHref.substring(indexLastSeparator + 1);
        }
        return scapDocHref;
    }
    
    private void buildXccdf(XCCDFBenchmark benchmark, OvalDefinitionsDocument ovalDoc) {
    	
        Set<String> ovalPlatforms = ovalDoc.getReferencedPlatformsAndProducts();
        if (ovalPlatforms.size() > 0) {
        	List<String> xccdfPlatforms = new ArrayList<String>();

            SCAPDocumentBundle officialCPEBundle = SCAPContentManager.getInstance().getOffcialCPEDictionary();
            if (officialCPEBundle == null) {
                throw new IllegalStateException(
                        "Could not load official CPE dictionary to resolve platforms!");
            }

            CPEDictionaryDocument officialCPEDict = officialCPEBundle.getCPEDictionaryDocs().get(0);
            Iterator<String> opItr = ovalPlatforms.iterator();
            while (opItr.hasNext()) {
                String op = opItr.next();
                Set<String> cpes = officialCPEDict.findItemsWithCheckContentByTitle(op);
                if (cpes != null && cpes.size() > 0) {
                    xccdfPlatforms.addAll(cpes);
                }
            }
            benchmark.setPlatformList(xccdfPlatforms);
        }

        List<OvalDefinition> defList = ovalDoc.getOvalDefinitions();
        List<SelectableItem> ruleList = new ArrayList<SelectableItem>(); // test
        List<Value> valueList = new ArrayList<Value>(); // test
        List<ProfileSelect> selectList = new ArrayList<ProfileSelect>();
        Group group = benchmark.getGroupList().get(0);
        Profile profile = benchmark.getProfileList().get(0);
        String ovalDocHref = getScapDocHref();
        for (OvalDefinition def : defList) {
            String ruleId = "xccdf_" + CommonUtil.sanitize(def.getId() + "_rule");
         //   LOG.debug("Creating Rule " + ruleId);
            Rule rule = benchmark.createRule();
            rule.setId(ruleId);
            rule.setTitle(def.getMetadata().getTitle());
            rule.setDescriptionAsString(def.getMetadata().getDescription());

            ruleList.add(rule); // test
            // group.addRule(rule);

            ProfileSelect profileSelect = benchmark.createProfileSelect();
            profileSelect.setIdRef(ruleId);
            profileSelect.setSelected(true);
            selectList.add(profileSelect);
            // profile.addProfileSelect(profileSelect);

            Check check = benchmark.createCheck();
            check.setSystem(OvalDefinition.OVAL_NAMESPACE);
            ArrayList<Check> checkList = new ArrayList<Check>(); // test
            checkList.add(check); // test
            rule.setCheckList(checkList);
            // rule.addCheck(check);

            CheckContentRef checkContentRef = benchmark.createCheckContentRef();
            // href field is just the name of the Oval xml file
            checkContentRef.setHref(ovalDocHref);
            checkContentRef.setName(def.getId());
            ArrayList<CheckContentRef> checkContentRefList = new ArrayList<CheckContentRef>(); // test
            checkContentRefList.add(checkContentRef); // test
            check.setCheckContentRefList(checkContentRefList); // test
            // check.addCheckContentRef(checkContentRef);

            // If this Oval definition refers to any external variables, we must
            // create a "Value" element in the Group for each one, and a
            // "check-export" element
            // inside the check.
            Set<OvalVariable> extVars = def.getReferencedExternalVariables();
            Iterator<OvalVariable> extVarIter = extVars.iterator();
            ArrayList<CheckExport> checkExportList = new ArrayList<CheckExport>(); // test
            while (extVarIter.hasNext()) {
                OvalVariable ovalVariable = extVarIter.next();
                // make sure its an external variable
                if (ovalVariable.getElement().getName().equals("external_variable")) {
                    String valueId = "Value_"
                            + CommonUtil.sanitize(ovalVariable.getId());
                    String varType = ovalVariable.getDatatype();
                    Value value = benchmark.createValue();
                    value.setId(valueId);
                    value.setTitle("This generated Value must be overridden");
                    // group.addValue(value);
                    valueList.add(value);
                    // we have to pick a value for the "Value" we are
                    // generating. If the Oval variable
                    // has any possible_values specified, pick the first one.
                    // Otherwise, pick a value
                    // based on the variables datatype ("" for string, 0 for
                    // int, false for boolean, etc).
                    List<VariablePossibleValue> possibleValues = ((OvalExternalVariable) ovalVariable).getPossibleValues();
                    String valueString = null;
                    ValueTypeEnum valueType = null;
                    String defaultValue = null;
                    if (possibleValues.size() > 0) {
                        valueString = possibleValues.get(0).getValue();
                    }
                    if (varType.equals("int") || varType.equals("float")
                            || varType.equals("binary")) {
                        valueType = ValueTypeEnum.NUMBER;
                        defaultValue = "0";
                    }
                    else if (varType.equals("boolean")) {
                        valueType = ValueTypeEnum.BOOLEAN;
                        defaultValue = "false";
                    }
                    else {
                        valueType = ValueTypeEnum.STRING;
                        defaultValue = "";
                    }
                    if (valueString == null) {
                        valueString = defaultValue;
                    }
                    SelValueElement selValue = benchmark.createSelValueElement();
                    selValue.setValue(valueString);
                    value.setType(valueType);
                    value.addValueSelector(selValue);

                    CheckExport checkExport = benchmark.createCheckExport();
                    checkExport.setValueId(valueId);
                    checkExport.setExportName(ovalVariable.getId());
                    checkExportList.add(checkExport); // test
                    // check.addCheckExport(checkExport);
                }
                check.setExportList(checkExportList); // test
            }
        }
        group.setChildren(ruleList);
        group.setValueList(valueList);
        profile.setSelectList(selectList);

    }
    
  /* private void buildXccdf(XCCDFBenchmark benchmark, OcilDocument ocilDoc) {
        List<SelectableItem> ruleList = new ArrayList<SelectableItem>(); // test
        List<Value> valueList = new ArrayList<Value>(); // test
        List<ProfileSelect> selectList = new ArrayList<ProfileSelect>();
        
        Group group = benchmark.getGroupList().get(0);
        Profile profile = benchmark.getProfileList().get(0);
        String ocilDocHref = getScapDocHref();
        
        Questionnaires questionnaires = ocilDoc.getQuestionnaires();
        if (questionnaires == null) {
        	throw new IllegalStateException("Error building XCCDF - incoming OCIL document " + scapDoc.getFilename() + " has no 'questionnaires' element");
        }
        List<Questionnaire> questionnaireList = questionnaires.getQuestionnaireList();
        for (Questionnaire questionnaire : questionnaireList) {
        	if (questionnaire.isChildOnly()) {
        		LOG.debug("skipping child-only questionnaire " + questionnaire.getId());
        		continue;
        	}
            String ruleId = "xccdf_" + CommonUtil.sanitize(questionnaire.getId() + "_rule");
            LOG.debug("Creating Rule " + ruleId);
            Rule rule = benchmark.createRule();
            rule.setId(ruleId);
            TextType textType = questionnaire.getTitle();
            if (textType != null) {
            	rule.setTitle(((String)textType.getValue()));
            }
            textType = questionnaire.getDescription();
            if (textType != null) {
            	rule.setDescriptionAsString(((String)textType.getValue()));
            }

            ruleList.add(rule); // test

            ProfileSelect profileSelect = benchmark.createProfileSelect();
            profileSelect.setIdRef(ruleId);
            profileSelect.setSelected(true);
            selectList.add(profileSelect);

            Check check = benchmark.createCheck();
            check.setSystem(OcilDocument.OCIL_NAMESPACE);
            ArrayList<Check> checkList = new ArrayList<Check>(); // test
            checkList.add(check); // test
            rule.setCheckList(checkList);

            CheckContentRef checkContentRef = benchmark.createCheckContentRef();
            // href field is just the name of the OCIL xml file
            checkContentRef.setHref(ocilDocHref);
            checkContentRef.setName(questionnaire.getId());
            ArrayList<CheckContentRef> checkContentRefList = new ArrayList<CheckContentRef>(); // test
            checkContentRefList.add(checkContentRef); // test
            check.setCheckContentRefList(checkContentRefList); // test
            // check.addCheckContentRef(checkContentRef);
        }
        
        group.setChildren(ruleList);
        group.setValueList(valueList);
        profile.setSelectList(selectList);
    }*/
    	

    /**
     * Get the XCCDFBuilderParameters object.
     * 
     * @return XCCDFBuilderParameters
     */
    public XCCDFBuilderParameters getParms()
    {
        return parms;
    }

    /**
     * Set the XCCDFBuilderParameters object.
     * 
     * @param parms
     *            Builder parameters
     */
    public void setParms(XCCDFBuilderParameters parms)
    {
        this.parms = parms;
    }

    /**
     * Get the OvalDefinitionsDocument being used.
     * 
     * @return OvalDefinitionsDocument
     */
    public SCAPDocument getSCAPDoc()
    {
        return scapDoc;
    }

    /**
     * Set the OvalDefinitionsDocument to be used.
     * 
     * @param ovalDoc
     *            Oval document to produce the XCCDF for.
     */
    public void setSCAPDoc(SCAPDocument scapDoc)
    {
        this.scapDoc = scapDoc;
    }
}
