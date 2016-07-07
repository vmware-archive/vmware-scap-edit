package com.g2inc.scap.editor.gui.wizards.oval.test;
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

import java.awt.Frame;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalTest;

public class NewTestWizard extends Wizard
{
    private static Logger log = Logger.getLogger(NewTestWizard.class);
    private OvalDefinitionsDocument ovalDefDoc = null;

    private String chosenPlatform = null;
    private String chosenType = null;
    private PickTestObjectAndStateWizardPage lastPage = null;

    // this won't actually assigned until after the user completes the second page
    // of the wizard
    private OvalTest ovalTest = null;

    private void addPages()
    {
        addWizardPage(new PickTestPlatformWizardPage(this, ovalDefDoc.getValidPlatforms()));
        addWizardPage(new PickTestTypeWizardPage(this));
        addObjectAndStatePage();
    }

    private void addObjectAndStatePage()
    {
        lastPage = new PickTestObjectAndStateWizardPage(this);
        addWizardPage(lastPage);
    }
    
    public NewTestWizard(Frame parent, boolean modal, OvalDefinitionsDocument odd)
    {
        super(parent, modal);
        setWindowTitle();
        ovalDefDoc = odd;
        
        EditorUtil.editBaseIdIfNecessary(ovalDefDoc);

        addPages();
    }

    public NewTestWizard(Frame parent, boolean modal, OvalDefinitionsDocument odd, String platformFilter, String typeFilter)
    {
        super(parent, modal);
        setWindowTitle();
        
        ovalDefDoc = odd;
        chosenPlatform = platformFilter;
        chosenType = typeFilter + "_test";

        addObjectAndStatePage();
    }

    public String getChosenPlatform()
    {
        return chosenPlatform;
    }

    public void setChosenPlatform(String chosenPlatform)
    {
        this.chosenPlatform = chosenPlatform;
        setOvalTest(null);
    }

    public String getChosenType()
    {
        return chosenType;
    }

    private void setWindowTitle()
    {
        setTitle("New " + EditorMessages.OVAL + " test");
    }

    public void setChosenType(String chosenType)
    {
        this.chosenType = chosenType;
    }

    public OvalTest getOvalTest()
    {
        if(ovalTest == null)
        {
            initOvalTest();
        }
        return ovalTest;
    }

    public void setOvalTest(OvalTest ovaltest)
    {
        this.ovalTest = ovaltest;
    }

    public OvalDefinitionsDocument getOvalDefDoc()
    {
        return ovalDefDoc;
    }

    // called by third wizard page to make sure we
    // initialize an instance of our new OvalTest before we
    // start adding parameters
    private void initOvalTest()
    {
       // log.debug("initOvalTest called");
    }

    @Override
    public void performFinish()
    {
      //  log.debug("performFinish called");
        lastPage.performFinish();
    }
}
