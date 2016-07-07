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

import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.editor.gui.wizards.WizardPage;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalStateParameter;

public class PickStateParametersWizardPage extends WizardPage implements ChangeListener
{
    private static Logger log = Logger.getLogger(PickStateParametersWizardPage.class);

    private NewStateWizard parentWiz = null;

    private void initPanel()
    {
        stateParmsPanel.setModalParent(true);
        stateParmsPanel.addChangeListener(this);
    }

    private void initComponents2()
    {
        initPanel();
    }

    public PickStateParametersWizardPage(NewStateWizard wiz)
    {
        initComponents();
        parentWiz = wiz;
        initComponents2();

        setChosenType(parentWiz.getChosenType());

        // for now do not require any parms to be added
        setSatisfied(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        stateParmsPanel = new com.g2inc.scap.editor.gui.windows.oval.state.StateParametersDisplayPanel();

        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(stateParmsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    
    @Override
	public Object getData()
    {
        return null;
    }

    
    @Override
	public void setData(Object data)
    {
    }

    
    @Override
	public void setWizard(Wizard wizard)
    {
        parentWiz = (NewStateWizard) wizard;
        parentWiz.enableNextButton();
    }

    
    @Override
	public String getPageTitle()
    {
        return "Parameters";
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.g2inc.scap.editor.gui.windows.oval.state.StateParametersDisplayPanel stateParmsPanel;
    // End of variables declaration//GEN-END:variables

    public void setChosenType(String type)
    {
        OvalState os = parentWiz.getOvalState();

        if(os != null)
        {
            stateParmsPanel.clearAddedParmsList();
        }

        os = parentWiz.getOvalDefDoc().createState(parentWiz.getChosenPlatform(), type);
        stateParmsPanel.setOvalState(os);
        parentWiz.setOvalState(os);

    }

    public List<OvalStateParameter> getAddedParmsList()
    {
        return stateParmsPanel.getAddedParmsList();
    }

    
    @Override
	public void performFinish()
    {
        parentWiz.getOvalState().setParameters(stateParmsPanel.getAddedParmsList());
    }

    
    public void stateChanged(ChangeEvent ce)
    {
        Object src = ce.getSource();
        if(src == stateParmsPanel)
        {
            if(!stateParmsPanel.allParmsOk())
            {
                parentWiz.disableNextButton();
            }
            else
            {
                parentWiz.enableNextButton();
            }
        }
    }
}