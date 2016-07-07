package com.g2inc.scap.editor.gui.wizards.oval.definition;
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.editor.gui.wizards.WizardPage;
import com.g2inc.scap.library.domain.oval.DefinitionClassEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;

public class NewDefGeneralInfoWizardPage extends WizardPage
{
    private NewDefinitionWizard parentWiz = null;
    private OvalDefinitionsDocument parentDoc = null;

    private void initCombo()
    {
        defClassCombo.removeAllItems();

        DefinitionClassEnum[] items = DefinitionClassEnum.values();

        DefinitionClassEnum defaultClass = null;

        for(int x = 0; x < items.length; x++)
        {
            DefinitionClassEnum dc = items[x];

            if(dc.toString().toLowerCase().equals("vulnerability"))
            {
                defaultClass = dc;
            }

            defClassCombo.addItem(items[x]);
        }

        defClassCombo.setSelectedItem(defaultClass);

        defClassCombo.addActionListener(new ActionListener()
        {
            
            public void actionPerformed(ActionEvent ae)
            {
                JComboBox cb = (JComboBox) ae.getSource();

                parentWiz.getDefinition().setDefinitionClass((DefinitionClassEnum) cb.getSelectedItem());
            }
        });
    }

    private void initTextFields()
    {
        titleTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                String text = titleTextField.getText();

                if(text == null || text.length() == 0)
                {
                    parentWiz.disableNextButton();
                }
                else
                {
                    String defDesc = descTextArea.getText();

                    if( defDesc != null && defDesc.length() > 0)
                    {
                        parentWiz.enableNextButton();
                        parentWiz.getDefinition().getMetadata().setTitle(text);
                        parentWiz.getDefinition().getMetadata().setDescription(defDesc);
                        setSatisfied(true);
                    }
                    else
                    {
                        parentWiz.disableNextButton();
                        setSatisfied(false);
                    }
                }
            }

            
            public void insertUpdate(DocumentEvent de)
            {
                common(de);
            }

            
            public void removeUpdate(DocumentEvent de)
            {
                common(de);
            }

            
            public void changedUpdate(DocumentEvent de)
            {
                common(de);
            }
        });

        descTextArea.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                String text = descTextArea.getText();

                if(text == null || text.length() == 0)
                {
                    parentWiz.disableNextButton();
                }
                else
                {
                    String defTitle = titleTextField.getText();

                    if( defTitle != null && defTitle.length() > 0)
                    {
                        parentWiz.enableNextButton();
                        parentWiz.getDefinition().getMetadata().setDescription(text);
                        parentWiz.getDefinition().getMetadata().setTitle(defTitle);
                        setSatisfied(true);
                    }
                    else
                    {
                        parentWiz.disableNextButton();
                        setSatisfied(false);
                    }
                }
            }

            
            public void insertUpdate(DocumentEvent de)
            {
                common(de);
            }

            
            public void removeUpdate(DocumentEvent de)
            {
                common(de);
            }

            
            public void changedUpdate(DocumentEvent de)
            {
                common(de);
            }
        });
    }

    private void initComponents2()
    {
        initCombo();
        initTextFields();
    }

    /** Creates new form ChoseVariableTypeWizardPage */
    public NewDefGeneralInfoWizardPage()
    {
        initComponents();
        initComponents2();
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

        classPanel = new javax.swing.JPanel();
        defClassCombo = new javax.swing.JComboBox();
        titlePanel = new javax.swing.JPanel();
        titleTextField = new javax.swing.JTextField();
        descPanel = new javax.swing.JPanel();
        descScrollPane = new javax.swing.JScrollPane();
        descTextArea = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        classPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Class", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        classPanel.setLayout(new java.awt.GridBagLayout());

        defClassCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 12);
        classPanel.add(defClassCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.01;
        add(classPanel, gridBagConstraints);

        titlePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Title", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        titlePanel.setLayout(new java.awt.GridBagLayout());

        titleTextField.setName("title_text"); // NOI18N
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        add(titlePanel, gridBagConstraints);

        descPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        descPanel.setLayout(new java.awt.GridBagLayout());

        descTextArea.setColumns(20);
        descTextArea.setRows(5);
        descTextArea.setName("description_text"); // NOI18N
        descScrollPane.setViewportView(descTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        descPanel.add(descScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        add(descPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel classPanel;
    private javax.swing.JComboBox defClassCombo;
    private javax.swing.JPanel descPanel;
    private javax.swing.JScrollPane descScrollPane;
    private javax.swing.JTextArea descTextArea;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField titleTextField;
    // End of variables declaration//GEN-END:variables

    
    @Override
	public Object getData()
    {
        return null;
    }

    
    @Override
	public void setData(Object data)
    {
        parentDoc = (OvalDefinitionsDocument) data;


    }

    
    @Override
	public void setWizard(Wizard wizard)
    {
        parentWiz = (NewDefinitionWizard) wizard;
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
}
