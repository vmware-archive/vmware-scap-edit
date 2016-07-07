package com.g2inc.scap.editor.gui.wizards.oval.variable;
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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField;
import com.g2inc.scap.editor.gui.windows.common.GenericSourceDetailTab;
import com.g2inc.scap.editor.gui.windows.oval.variable.VariableDisplayPanel;
import com.g2inc.scap.editor.gui.windows.oval.variable.VariableGeneralPanel;
import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.editor.gui.wizards.WizardPage;
import com.g2inc.scap.library.domain.oval.OvalVariable;

public class NewVariableWizardPage extends WizardPage implements ChangeListener
{
    private static final Logger log = Logger.getLogger(NewVariableWizardPage.class);
    String varType = null;
    private NewVariableWizard parentWiz = null;
    private GenericSourceDetailTab sourceTab = null;

    public NewVariableWizardPage(NewVariableWizard wiz) {
        initComponents();
        parentWiz = wiz;
        initComponents2();
    }

    public void setChosenType(String varType) {
        this.varType = varType;
        log.debug("setChosenType called, type=" + varType);
        if (!varType.equals("local_variable") &&
            !varType.equals("external_variable") &&
            !varType.equals("constant_variable")) {
            throw new IllegalStateException("Unexpected variable Type:" + varType);
        } 
        OvalVariable ovalVar = parentWiz.getOvalDefDoc().createVariable(varType);
        parentWiz.setOvalVariable(ovalVar);
        variableDisplayPanel1.setOvalVariable(ovalVar);
        variableDisplayPanel1.getTreeHandler().addChangeListener(this);
    }

    private void initComponents2()
    {
        variableDisplayPanel1.setModalParent(true);
        variableDisplayPanel1.addChangeListener(this);
    }
    
    @Override
    public void stateChanged(ChangeEvent ce)
    {
        Object src = ce.getSource();

        if(src instanceof VariableDisplayPanel)
        {
            // the variableContentsDisplayPanel1 panel is telling us something changed, let's ask him if he's still valid.
            log.debug("Receiving ChangeEvent from variableDisplayPanel1.");
            if(!variableDisplayPanel1.allDataOk())
            {
                parentWiz.disableNextButton();
            }
            else
            {
                parentWiz.enableNextButton();
            }
        }
        else if(src instanceof PatternedStringField)
        {
            PatternedStringField field = (PatternedStringField) src;
            String componentName = field.getName();

            if(componentName != null)
            {
                if(componentName.equals(VariableGeneralPanel.VARIABLE_ID_FIELD_NAME))
                {
                    if(field.isValidInput())
                    {
                        parentWiz.enableNextButton();
                        parentWiz.getOvalVariable().setId(field.getValue());
                    }
                    else
                    {
                        parentWiz.disableNextButton();
                    }
                }
            }
        }
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

        variableDisplayPanel1 = new com.g2inc.scap.editor.gui.windows.oval.variable.VariableDisplayPanel();

        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(variableDisplayPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.g2inc.scap.editor.gui.windows.oval.variable.VariableDisplayPanel variableDisplayPanel1;
    // End of variables declaration//GEN-END:variables

    public GenericSourceDetailTab getSourceTab()
    {
        return sourceTab;
    }

    public void setSourceTab(GenericSourceDetailTab sourceTab)
    {
        this.sourceTab = sourceTab;
    }

    
    @Override
	public void setVisible(boolean visible)
    {
        log.debug("setVisible(" + visible + ") entered");
        super.setVisible(visible);
        if (visible)
        {
            setChosenType(parentWiz.getChosenType());
            setSatisfied(true);
        }
    }
    
    @Override
	public void performFinish() {

    }
    
    @Override
	public String getPageTitle() {
        return "Variable Details";
    }

    @Override
	public void setWizard(Wizard wizard) {
        parentWiz = (NewVariableWizard) wizard;
    }
    
    @Override
	public void setData(Object data) {
    }
    
    @Override
	public Object getData() {
        return null;
    }
}
