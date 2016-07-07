package com.g2inc.scap.editor.gui.wizards.cpe.item.create;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.editor.gui.wizards.WizardPage;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;

public class NewCPEItemWizardPage extends WizardPage implements ChangeListener
{
    private static final Logger log = Logger.getLogger(NewCPEItemWizardPage.class);
    private NewCPEItemWizard parentWiz = null;
    private CPEDictionaryDocument parentDoc = null;

    private DocumentListener titleListener = null;

    public static final String MSG_OK = "Ok";
    public static final String MSG_TITLE_ZERO_LEN = "Title must not be zero-length!";
    public static final String MSG_DUPLICATE_NAME = "The dictionary document already contains that name!";

    private void setStatus(String statusText)
    {
        statusLabel.setText(statusText);
    }

    private void initDocumentListeners()
    {
        titleListener = new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                String titleText = titleTextField.getText();

                if (titleText == null || titleText.length() == 0)
                {
                    titleTextField.setForeground(nameField.getErrorTextColor());
                    setStatus(MSG_TITLE_ZERO_LEN);
                    parentWiz.disableNextButton();
                }
                else
                {
                    titleTextField.setForeground(nameField.getNormalTextColor());

                    if (nameField.isValidInput())
                    {
                        
                        setStatus(MSG_OK);
                        parentWiz.enableNextButton();
                        setSatisfied(true);
                    }
                    else
                    {
                        setStatus(nameField.getStatus());

                        parentWiz.disableNextButton();
                        setSatisfied(false);
                    }
                }
            }

            @Override
            public void insertUpdate(DocumentEvent de)
            {
                common(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de)
            {
                common(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de)
            {
                common(de);
            }
        };
    }

    private void initTextFields()
    {
        titleTextField.getDocument().addDocumentListener(titleListener);
        nameField.addChangeListener(this);
        nameField.setPattern(CPEDictionaryDocument.CPE_ITEM_NAME_PATTERN);
    }

    private void initComponents2()
    {
        initDocumentListeners();
        initTextFields();
    }

    /** Creates new form ChoseVariableTypeWizardPage */
    public NewCPEItemWizardPage(NewCPEItemWizard wiz)
    {
        parentWiz = wiz;

        initComponents();
        initComponents2();

        titleTextField.requestFocus();
    }

    /** Creates new form ChoseVariableTypeWizardPage */
    public NewCPEItemWizardPage()
    {
        initComponents();
        initComponents2();
    
        titleTextField.requestFocus();
    }

    @Override
    public Object getData()
    {
        return null;
    }

    @Override
    public void setData(Object data)
    {
        parentDoc = (CPEDictionaryDocument) data;
    }

    @Override
    public void setWizard(Wizard wizard)
    {
        parentWiz = (NewCPEItemWizard) wizard;
    }

    @Override
    public String getPageTitle()
    {
        return "General";
    }

    @Override
    public void performFinish()
    {

    }

    public String getItemName()
    {
        return nameField.getValue();
    }

    public String getItemTitle()
    {
        return titleTextField.getText();
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {        
        Object src = e.getSource();

        if(src == nameField)
        {
            if(!nameField.isValidInput())
            {
                parentWiz.disableNextButton();
                setStatus(nameField.getStatus());
            }
            else
            {
                if(parentDoc.containsItem(nameField.getValue()))
                {
                    parentWiz.disableNextButton();
                    setStatus(MSG_DUPLICATE_NAME);
                }
                else if(titleTextField.getText().length() == 0)
                {
                    // title is zero length
                    parentWiz.disableNextButton();
                    setStatus(MSG_TITLE_ZERO_LEN);
                }
                else
                {
                    parentWiz.enableNextButton();
                    setStatus(MSG_OK);
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

        namePanel = new javax.swing.JPanel();
        nameField = new com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField();
        titlePanel = new javax.swing.JPanel();
        titleTextField = new javax.swing.JTextField();
        statusLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        namePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        namePanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 5);
        namePanel.add(nameField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(namePanel, gridBagConstraints);

        titlePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Title", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        titlePanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        titlePanel.add(titleTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(titlePanel, gridBagConstraints);

        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusLabel.setToolTipText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(statusLabel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField nameField;
    private javax.swing.JPanel namePanel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField titleTextField;
    // End of variables declaration//GEN-END:variables
}
