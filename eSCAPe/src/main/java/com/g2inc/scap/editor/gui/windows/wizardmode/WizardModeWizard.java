package com.g2inc.scap.editor.gui.windows.wizardmode;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.windows.EditorConfiguration;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalFunction;
import com.g2inc.scap.library.domain.oval.OvalFunctionEnum;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalObjectParameter;
import com.g2inc.scap.library.domain.oval.OvalVariable;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentLiteral;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentObject;
import com.g2inc.scap.library.domain.oval.TypeEnum;
import com.g2inc.scap.library.domain.oval.XCCDFBuilder;
import com.g2inc.scap.library.domain.oval.XCCDFBuilderParameters;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

public abstract class WizardModeWizard extends Wizard {

    protected Map<String, OvalObject> winVarsFound = new HashMap<String, OvalObject>();
    protected Map<String, Map<String, OvalVariable>> winVarLiteralMap = new HashMap<String, Map<String, OvalVariable>>();
    /**
     * A statically initiallized map containing all the special windows
     * environment variables we know about so we can create registry objects to
     * look them up when we need to.
     *
     */
    public static Map<String, SpecialCaseWindowsVariable> supportedVariables = new HashMap<String, SpecialCaseWindowsVariable>();

    {
        String varName = "";
        String hive = "";

        varName = "%systemroot%";
        hive = "HKEY_LOCAL_MACHINE";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", "SystemRoot", "System Root"));

        varName = "%commonprogramfiles(x86)%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "CommonFilesDir(x86)", "Common Program Files(x86)"));

        varName = "%commonprogramfiles%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "CommonFilesDir", "Common Program Files"));

        varName = "%programfiles(x86)%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "ProgramFilesDir(x86)", "Common Program Files(x86)"));

        varName = "%programfiles%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "ProgramFilesDir", "Common Program Files"));

        varName = "%programdata%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders", "Common AppData", "Common Program Data"));

        hive = "HKEY_CURRENT_USER";
        varName = "%appdata%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "Volatile Environment", "APPDATA", "Current user's roaming app data directory"));
        varName = "%homepath%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "Volatile Environment", "HOMEPATH", "Current user's home directory path minus drive"));
        varName = "%homedrive%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "Volatile Environment", "HOMEDRIVE", "Current user's home directory drive"));
    }
    protected OvalDefinitionsDocument ovalDocument = null;

    public WizardModeWizard(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    public WizardModeWizard(boolean modal) {
        super(EditorMainWindow.getInstance(), modal);
    }

    public void saveDocuments(File destFile, String platform) {
        ovalDocument.setGeneratorDate(new Date(System.currentTimeMillis()));
        ovalDocument.setGeneratorProduct(EditorMessages.PRODUCT_SHORTNAME);
        ovalDocument.setGeneratorProductVersion(EditorConfiguration.getInstance().getEditorVersion());

        ovalDocument.setFilename(destFile.getAbsolutePath());


        try {
            ovalDocument.save();
            // create xccdf wrapper document
            XCCDFBuilderParameters builderParms = new XCCDFBuilderParameters();
            builderParms.setSourceDoc(ovalDocument);
            builderParms.setBenchmarkDescription("File content for " + platform + " built on " + ovalDocument.getGeneratorRawDate());
            builderParms.setBenchmarkId(platform + "_content");
            builderParms.setGroupId(platform + "_content_group");
            builderParms.setGroupTitle(platform + " group");
            builderParms.setProfileTitle(platform + " profile");
            builderParms.setProfileId(platform + "_content_profile");

            XCCDFBenchmark benchmark = null;
            XCCDFBuilder builder = new XCCDFBuilder();
            builder.setParms(builderParms);

            benchmark = builder.generateXCCDF(builderParms);

            ovalDocument.close();
            benchmark.close();


            EditorMainWindow.getInstance().openFile(ovalDocument);


        } catch (IOException ex) {
            throw new IllegalStateException("Exception while saving", ex);
        }
    }

    public OvalVariable handleWindowsEnvironmentVariableCreation(String path) {
        OvalVariable pathVar = null;
        OvalObject regObj = null;

        String lcPath = path.toLowerCase();

        for (Iterator<String> knownVarNames = supportedVariables.keySet().iterator(); knownVarNames.hasNext();) {
            String lcSysVarName = knownVarNames.next();
            int index = lcPath.indexOf(lcSysVarName);
            if (index > -1) {
                if (winVarsFound.containsKey(lcSysVarName)) {
                    // dont' create another, use exising one
                    regObj = winVarsFound.get(lcSysVarName);
                } else {
                    // create object that gets the value of the registry
                    regObj = createWinEnvRegObj(lcSysVarName);
                    winVarsFound.put(lcSysVarName, regObj);
                }

                String literal = null;

                if (path.length() == lcSysVarName.length()) {
                    literal = "\\";
                } else {
                    literal = path.substring(index + lcSysVarName.length());
                }

                if (winVarLiteralMap.containsKey(lcSysVarName)) {
                    Map<String, OvalVariable> lit2VarMap = winVarLiteralMap.get(lcSysVarName);

                    if (lit2VarMap.containsKey(literal)) {
                        pathVar = lit2VarMap.get(literal);
                    }
                }

                // didn't find it in our map, must add it
                if (pathVar == null) {
                    // we haven't added this one before
                    pathVar = createVarFromObjectAndLiteral(regObj, literal);
                    Map<String, OvalVariable> lit2VarMap = new HashMap<String, OvalVariable>();

                    lit2VarMap.put(literal, pathVar);

                    // save for later
                    winVarLiteralMap.put(lcSysVarName, lit2VarMap);
                }

                break;
            }
        }

        return pathVar;
    }

    private OvalVariable createVarFromObjectAndLiteral(OvalObject obj, String literal) {
        OvalVariable var = ovalDocument.createVariable("local_variable");
        var.setDatatype(TypeEnum.STRING.toString().toLowerCase());
        var.setComment("Variable that concatenates object " + obj.getId() + " with the literal " + literal);

        OvalFunction concat = var.createOvalFunction(OvalFunctionEnum.concat);

        var.addChild(concat);

        OvalVariableComponentObject objComp = var.createObjectComponent();
        objComp.setItemField("value");
        objComp.setObjectId(obj.getId());

        concat.addChild(objComp);

        OvalVariableComponentLiteral litComp = var.createLiteralComponent();
        litComp.setValue(literal);
        concat.addChild(litComp);

        ovalDocument.add(var);
        return var;
    }

    private OvalObject createWinEnvRegObj(String var) {
        // create registry object
        OvalObject regObject = ovalDocument.createObject("windows", "registry_object");
        regObject.setComment("Registry object that checks " + var);
        OvalObjectParameter hiveOop = regObject.createObjectParameter("hive");
        OvalObjectParameter keyOop = regObject.createObjectParameter("key");
        OvalObjectParameter nameOop = regObject.createObjectParameter("name");

        SpecialCaseWindowsVariable winVar = supportedVariables.get(var);

        hiveOop.setValue(winVar.getHive());

        keyOop.setValue(winVar.getKeyName());

        nameOop.setValue(winVar.getValueName());

        regObject.addChild(hiveOop);
        regObject.addChild(keyOop);
        regObject.addChild(nameOop);

        ovalDocument.add(regObject);

        return regObject;
    }

    @Override
    public void dispose() {
        if (winVarsFound != null) {
            winVarsFound.clear();
            winVarsFound = null;
        }
        if (winVarLiteralMap != null) {
            winVarLiteralMap.clear();
            winVarLiteralMap = null;
        }
    }
}
