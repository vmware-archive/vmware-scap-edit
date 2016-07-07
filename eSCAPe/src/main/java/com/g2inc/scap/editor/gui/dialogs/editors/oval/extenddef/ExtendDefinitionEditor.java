package com.g2inc.scap.editor.gui.dialogs.editors.oval.extenddef;
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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.g2inc.scap.editor.gui.choosers.definition.DefinitionChooser;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.library.domain.oval.ExtendDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinition;

public class ExtendDefinitionEditor extends javax.swing.JPanel implements IEditorPage
{
    private ExtendDefinition extendDef = null;
    private EditorDialog parentEditor = null;

    private void initCombos()
    {
        negateCombo.removeAllItems();

        negateCombo.addItem("true");
        negateCombo.addItem("false");

        negateCombo.setSelectedItem("false");
    }

    private void initTextFields()
    {
        commentTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                String text = commentTextField.getText();

                if (text.length() > 0)
                {
                    if(definitionLabel.getText().length() > 0)
                    {
                        parentEditor.enableOkButton();
                    }
                    else
                    {
                        parentEditor.disableOkButton();
                    }
                }
                else
                {
                    parentEditor.disableOkButton();
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
        });
    }

    private void initButtons()
    {
        chooseDefinitionButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                DefinitionChooser chooser = new DefinitionChooser(true);
                chooser.setSource(extendDef.getParentDocument(), null, null);
                
                chooser.setVisible(true);

                if(!chooser.wasCancelled())
                {
                    OvalDefinition chosen = (OvalDefinition) chooser.getChosen();
                    definitionLabel.setText(chosen.getId());
                
                    if(commentTextField.getText().length() == 0)
                    {
                        commentTextField.setText(chosen.getMetadata().getTitle());
                    }
                    parentEditor.enableOkButton();
                }
            }
        });
    }

    public void setNegated(boolean b)
    {
        negateCombo.setSelectedItem(b + "");
    }

    public boolean getNegated()
    {
        String negateChoice = (String) negateCombo.getSelectedItem();

        return Boolean.parseBoolean(negateChoice);
    }

    private void initComponents2()
    {
        initTextFields();
        initButtons();
        initCombos();
    }


    private boolean valid()
    {
        boolean ret = true;

        if(commentTextField.getText().length() == 0)
        {
            return false;
        }

        if(definitionLabel.getText().length() == 0)
        {
            return false;
        }
        return ret;
    }

    /** Creates new form RegexDatatypeEditor */
    public ExtendDefinitionEditor(EditorDialog parentEd, ExtendDefinition edef)
    {
        initComponents();
        extendDef = edef;
        parentEditor = parentEd;
        initComponents2();

        String initialComment = extendDef.getComment();

        if(initialComment != null)
        {
            commentTextField.setText(initialComment);
        }

        String initialDefId = extendDef.getDefinitionId();

        if(initialDefId != null)
        {
            definitionLabel.setText(initialDefId);
        }

        setNegated(edef.isNegated());
        
        if(!valid())
        {
            parentEditor.disableOkButton();
        }
        else
        {
            parentEditor.enableOkButton();
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

        commentCaption = new javax.swing.JLabel();
        commentTextField = new javax.swing.JTextField();
        definitionCaption = new javax.swing.JLabel();
        definitionLabel = new javax.swing.JLabel();
        chooseDefinitionButton = new javax.swing.JButton();
        negateCaption = new javax.swing.JLabel();
        negateCombo = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        commentCaption.setText("Comment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 5);
        add(commentCaption, gridBagConstraints);
        
        commentTextField.setPreferredSize(new java.awt.Dimension(255, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 5, 6);
        add(commentTextField, gridBagConstraints);

        definitionCaption.setText("Definition");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 5);
        add(definitionCaption, gridBagConstraints);

        definitionLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        definitionLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        definitionLabel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        definitionLabel.setMinimumSize(new java.awt.Dimension(128, 255));
        definitionLabel.setPreferredSize(new java.awt.Dimension(128, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 4, 0);
        add(definitionLabel, gridBagConstraints);

        chooseDefinitionButton.setText("Choose Definition");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(chooseDefinitionButton, gridBagConstraints);

        negateCaption.setText("Negate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 0, 5);
        add(negateCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(negateCombo, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseDefinitionButton;
    private javax.swing.JLabel commentCaption;
    private javax.swing.JTextField commentTextField;
    private javax.swing.JLabel definitionCaption;
    private javax.swing.JLabel definitionLabel;
    private javax.swing.JLabel negateCaption;
    private javax.swing.JComboBox negateCombo;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getData()
    {
        extendDef.setComment(commentTextField.getText());
        extendDef.setDefinitionId(definitionLabel.getText());
        extendDef.setNegated(getNegated());
        
        return null;
    }

    @Override
    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
        parentEditor.setTitle("Extend Definition Editor");
    }

    @Override
    public void setData(Object data)
    {
    }

    public void setComment(String comment)
    {
        commentTextField.setText(comment);
    }

    public void setDefinitionId(String defId)
    {
        definitionLabel.setText(defId);
    }
}
