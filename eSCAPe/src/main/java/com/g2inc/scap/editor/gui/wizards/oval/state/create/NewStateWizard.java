package com.g2inc.scap.editor.gui.wizards.oval.state.create;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalStateParameter;

public class NewStateWizard extends Wizard
{
    private static Logger log = Logger.getLogger(NewStateWizard.class);

    private OvalDefinitionsDocument ovalDefDoc = null;

    private String chosenPlatform = null;
    private String chosenType = null;

    private PickStatePlatformWizardPage platPage = null;
    private PickStateTypeWizardPage typePage = null;
    private PickStateParametersWizardPage parmPage = null;

    List<OvalStateParameter> addedParmsMap = null;

    // this won't actually assigned until after the user completes the second page
    // of the wizard
    private OvalState ovalState = null;

    private void addPages()
    {
        platPage = new PickStatePlatformWizardPage(this, ovalDefDoc.getValidPlatforms());
        addWizardPage(platPage);

        typePage = new PickStateTypeWizardPage(this);
        addWizardPage(typePage);

        parmPage = new PickStateParametersWizardPage(this);
        addWizardPage(parmPage);
        
        addedParmsMap = parmPage.getAddedParmsList();
    }

    private void addParameterPage()
    {
        parmPage = new PickStateParametersWizardPage(this);
        addWizardPage(parmPage);
    }

    private void setWindowTitle()
    {
        setTitle("New " + EditorMessages.OVAL + " state");
    }

    public NewStateWizard(Frame parent, boolean modal, OvalDefinitionsDocument odd)
    {
        super(parent, modal);
        setWindowTitle();
        ovalDefDoc = odd;
        
        EditorUtil.editBaseIdIfNecessary(ovalDefDoc);

        addPages();
    }

    public NewStateWizard(Frame parent, boolean modal, OvalDefinitionsDocument odd, String platformFilter, String typeFilter)
    {
        super(parent, modal);
        setWindowTitle();

        ovalDefDoc = odd;
        chosenPlatform = platformFilter;
        chosenType = typeFilter + "_state";

        addParameterPage();
    }

    public String getChosenPlatform()
    {
        return chosenPlatform;
    }

    public void setChosenPlatform(String chosenPlatform)
    {
        this.chosenPlatform = chosenPlatform;
        
//        log.debug("chosenPlatform is " + this.chosenPlatform);

        if(typePage != null)
        {
            typePage.setChosenPlatForm(this.chosenPlatform);
        }
    }

    public String getChosenType()
    {
        return chosenType;
    }

    public void setChosenType(String chosenType)
    {
        this.chosenType = chosenType;
//        log.debug("chosenType is " + this.chosenType);

        if(parmPage != null)
        {
//            log.debug("parmPage is not null");
            parmPage.setChosenType(chosenType);
        }
        else
        {
//            log.debug("parmPage is null");
        }
    }

    public OvalState getOvalState()
    {
        return ovalState;
    }

    public void setOvalState(OvalState ovalste)
    {
        this.ovalState = ovalste;
    }

    public OvalDefinitionsDocument getOvalDefDoc()
    {
        return ovalDefDoc;
    }

    
    @Override
	public void performFinish()
    {
        Collection<OvalStateParameter> osps = parmPage.getAddedParmsList();

        Iterator<OvalStateParameter> i = osps.iterator();

        while(i.hasNext())
        {
            ovalState.addParameter(i.next());
        }
    }
}
