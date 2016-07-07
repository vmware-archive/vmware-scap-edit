package com.g2inc.scap.editor.gui.windows.wizardmode.wizards.registry;
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
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.wizardmode.WizardModeWizard;
import com.g2inc.scap.editor.gui.windows.wizardmode.wizards.FileSaveWizardPage;
import com.g2inc.scap.library.domain.oval.AffectedItemContainer;
import com.g2inc.scap.library.domain.oval.AffectedItemFamilyEnum;
import com.g2inc.scap.library.domain.oval.AffectedPlatform;
import com.g2inc.scap.library.domain.oval.Criteria;
import com.g2inc.scap.library.domain.oval.Criterion;
import com.g2inc.scap.library.domain.oval.DefinitionClassEnum;
import com.g2inc.scap.library.domain.oval.ExistenceEnumeration;
import com.g2inc.scap.library.domain.oval.Metadata;
import com.g2inc.scap.library.domain.oval.OperEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalEntity;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalObjectParameter;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalStateParameter;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVariable;

public class RegistryWizard extends WizardModeWizard
{
    private static final long serialVersionUID = 1L;
    private static Logger LOG = Logger.getLogger(RegistryWizard.class);
    public  static final String NAME = "Registry";
    public  static final String SHORT_DESC = "Creates windows registry related content.";
    private static final String DESC = "<HTML>"
            + "Creates content that checks for existence/absence of registry keys or<BR>the value of a registry key."
            + "</HTML>";

    public static final String HKLM_HIVE = "HKEY_LOCAL_MACHINE";
    public static final String ENV_VAR_KEY = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion";
    public static final String ENV_VAR_KEY2 = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion";

    private RegistryHiveKeyNameWizardPage hiveKeyNamePage = null;
    private FileSaveWizardPage fileSavePage = null;

    private List<AffectedItemContainer> affectedContainers = null;

    public void addPages()
    {
        hiveKeyNamePage = new RegistryHiveKeyNameWizardPage(ovalDocument, this);
        fileSavePage = new FileSaveWizardPage(this);
        addWizardPage(hiveKeyNamePage);
        addWizardPage(fileSavePage);
    }

    public RegistryWizard(OvalDefinitionsDocument doc, boolean modal)
    {
        super(EditorMainWindow.getInstance(), modal);
        ovalDocument = doc;
        setTitle("Create new Registry Test");
        setWizardName(NAME);
        setWizardDescription(DESC);
        setWizardShortDescription(SHORT_DESC);
    }

    public List<AffectedItemContainer> getAffectedContainers()
    {
        return affectedContainers;
    }

    @Override
    public void performFinish()
    {
        String title = hiveKeyNamePage.getTitle();
        if (title == null)
        {
            title = "GENERATED TITLE";
        }
        // Build an Oval definition, add criteria and criterion to invoke one Oval test
        OvalDefinition def = ovalDocument.createDefinition(DefinitionClassEnum.VULNERABILITY);

        Metadata metadata = def.getMetadata();
        metadata.setTitle(title);
        ovalDocument.add(def);

        AffectedItemContainer affectedPlatforms
        	= def.getMetadata().createAffected(ovalDocument);

        AffectedPlatform win2kPlatform = affectedPlatforms.createAffectedPlatform(ovalDocument);
        win2kPlatform.setValue("Microsoft Windows 2000");
        affectedPlatforms.addAffectedItem(win2kPlatform);

        AffectedPlatform winXPPlatform = affectedPlatforms.createAffectedPlatform(ovalDocument);
        winXPPlatform.setValue("Microsoft Windows XP");
        affectedPlatforms.addAffectedItem(winXPPlatform);

        AffectedPlatform win2k3ServerPlatform = affectedPlatforms.createAffectedPlatform(ovalDocument);
        win2k3ServerPlatform.setValue("Microsoft Windows Server 2003");
        affectedPlatforms.addAffectedItem(win2k3ServerPlatform);

        AffectedPlatform win2k8ServerPlatform = affectedPlatforms.createAffectedPlatform(ovalDocument);
        win2k8ServerPlatform.setValue("Microsoft Windows Server 2008");
        affectedPlatforms.addAffectedItem(win2k8ServerPlatform);

        AffectedPlatform winVistaPlatform = affectedPlatforms.createAffectedPlatform(ovalDocument);
        winVistaPlatform.setValue("Microsoft Windows Vista");
        affectedPlatforms.addAffectedItem(winVistaPlatform);

        AffectedPlatform win7Platform = affectedPlatforms.createAffectedPlatform(ovalDocument);
        win7Platform.setValue("Microsoft Windows 7");
        affectedPlatforms.addAffectedItem(win7Platform);

        affectedPlatforms.setFamily(AffectedItemFamilyEnum.windows);
        def.getMetadata().addAffected(affectedPlatforms);

        Criteria criteria = def.createCriteria();
        Criterion criterion = criteria.createCriterion();
        criteria.addChild(criterion);
        def.setCriteria(criteria);

        // Now build a registry_test
        OvalTest test = ovalDocument.createTest("windows", "registry_test");
        ovalDocument.add(test);
        criterion.setTest(test);

        WhatToTest whatToTest = hiveKeyNamePage.getWhatToTest();

        // Now build a registry_object
        OvalObject ovalObject = ovalDocument.createObject("windows", "registry_object");
        ovalDocument.add(ovalObject);
        test.setObjectId(ovalObject.getId());

        OvalObjectParameter hiveParm = ovalObject.createObjectParameter("hive");
        hiveParm.setValue(hiveKeyNamePage.getHive());
        ovalObject.addChild(hiveParm);

        OvalObjectParameter keyParm = ovalObject.createObjectParameter("key");
        keyParm.setValue(hiveKeyNamePage.getKey());
        ovalObject.addChild(keyParm);
        if (hiveKeyNamePage.isKeyRegex())
        {
                keyParm.setOperation(OperEnum.PATTERN_MATCH.toString());
        }

        OvalObjectParameter nameParm = ovalObject.createObjectParameter("name");
        String regName = hiveKeyNamePage.getRegistryName();

        if (whatToTest == WhatToTest.HIVE_KEY_EXISTS) {
                test.setCheckExistence(ExistenceEnumeration.AT_LEAST_ONE_EXISTS);
                regName = null;
        } else if (whatToTest == WhatToTest.HIVE_KEY_NOT_EXIST) {
                test.setCheckExistence(ExistenceEnumeration.NONE_EXIST);
                regName = null;
        }

        if (regName != null && !regName.equals(""))
        {
            // A registry name was specified; put it in the registry_object,
            // and set pattern_match if requested. If only existance of the
            // name is to be checked, set check_existence in the registry_test
            // appropriately.
            nameParm.setValue(regName);
            if (hiveKeyNamePage.isNameRegex())
            {
                    nameParm.setOperation(OperEnum.PATTERN_MATCH.toString());
            }
            if (whatToTest == WhatToTest.HIVE_KEY_NAME_EXISTS)
            {
                    test.setCheckExistence(ExistenceEnumeration.AT_LEAST_ONE_EXISTS);
            }
            else if (whatToTest == WhatToTest.HIVE_KEY_NAME_NOT_EXIST)
            {
                    test.setCheckExistence(ExistenceEnumeration.NONE_EXIST);
            }
        }
        else
        {
            // If no name was specified, this is an existence check for the
            // hive\key, so set the name to nil
            nameParm.setNil(true);
        }
        ovalObject.addChild(nameParm);

        // Now build registry_state, if required
        String value = hiveKeyNamePage.getValue();

        if (whatToTest == WhatToTest.VALUE && value != null)
        {
            OvalState ovalState = ovalDocument.createState("windows", "registry_state");
            ovalDocument.add(ovalState);
            DefaultMutableTreeNode ovalStateNode = new DefaultMutableTreeNode(ovalState);
            test.setStateId(ovalState.getId());

            OvalEntity valueEntity = getOvalEntity(ovalState, "value");
            OvalStateParameter valueParm = ovalState.createOvalStateParameter(valueEntity);

            OvalVariable pathVar = handleWindowsEnvironmentVariableCreation(value);

            if (pathVar != null)
            {
                    valueParm.setVarRef(pathVar.getId());
            }
            else
            {
                    valueParm.setValue(value);
            }

            valueParm.setOperation(hiveKeyNamePage.getOperation());
            valueParm.setDatatype(hiveKeyNamePage.getDatatype());
            DefaultMutableTreeNode valueParmNode = new DefaultMutableTreeNode(valueParm);
            ovalStateNode.add(valueParmNode);
            ovalState.addParameter(valueParm);
        }

        // Finally, we are ready to save the oval file
        File file = fileSavePage.getChosenFile();
        if (file == null)
        {
                LOG.error("No file name was chosen for new " + EditorMessages.OVAL + " file");
        }
        else
        {
                saveDocuments(file, "windows");
        }
    }

    private OvalEntity getOvalEntity(OvalState ovalState, String name)
    {
        OvalEntity result = null;
        List<OvalEntity> list = ovalState.getValidParameters();
        for (OvalEntity entity : list)
        {
            if (entity.getName().equals(name))
            {
                result = entity;
                break;
            }
        }
        return result;
    }

    public RegistryHiveKeyNameWizardPage getHiveKeyNamePage()
    {
            return hiveKeyNamePage;
    }

    public void setHiveKeyNamePage(RegistryHiveKeyNameWizardPage hiveKeyNamePage)
    {
            this.hiveKeyNamePage = hiveKeyNamePage;
    }

    public FileSaveWizardPage getFileSavePage()
    {
            return fileSavePage;
    }

    public void setFileSavePage(FileSaveWizardPage fileSavePage)
    {
            this.fileSavePage = fileSavePage;
    }
}
