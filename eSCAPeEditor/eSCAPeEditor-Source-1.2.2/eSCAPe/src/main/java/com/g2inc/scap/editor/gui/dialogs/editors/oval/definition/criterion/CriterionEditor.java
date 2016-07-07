package com.g2inc.scap.editor.gui.dialogs.editors.oval.definition.criterion;
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

import com.g2inc.scap.editor.gui.choosers.test.TestChooser;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.library.domain.oval.Criterion;
import com.g2inc.scap.library.domain.oval.OvalTest;

public class CriterionEditor extends javax.swing.JPanel implements IEditorPage
{
    private EditorDialog parentEditor = null;
    private Criterion criterion = null;

    private static final String MSG_STATUS_OK = "OK";
    private static final String MSG_STATUS_ERROR_COMMENT_ZERO_LEN = "ERROR: Comment can't be zero-length";
    private static final String MSG_STATUS_ERROR_TEST = "ERROR: Test must be specified";

    private void initTextFields()
    {
        commentTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                String text = commentTextField.getText();

                if (text.length() > 0)
                {
                    if(testIdLabel.getText().length() > 0)
                    {
                        statusLabel.setText(MSG_STATUS_OK);
                        parentEditor.enableOkButton();
                    }
                    else
                    {
                        statusLabel.setText(MSG_STATUS_ERROR_TEST);
                        parentEditor.disableOkButton();
                    }
                }
                else
                {
                    statusLabel.setText(MSG_STATUS_ERROR_COMMENT_ZERO_LEN);
                    parentEditor.disableOkButton();
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

    private void initCombos()
    {
        negateCombo.removeAllItems();
        negateCombo.addItem("true");
        negateCombo.addItem("false");

        negateCombo.setSelectedItem("false");
    }

    private void initButtons()
    {
        chooseTestButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                TestChooser chooser = new TestChooser(true);
                chooser.setSource(criterion.getParentDocument(), null, null);

                chooser.setVisible(true);

                if(!chooser.wasCancelled())
                {
                    OvalTest test = (OvalTest) chooser.getChosen();

                    if(test != null)
                    {
                        testIdLabel.setText(test.getId());

                        if(commentTextField.getText().length() == 0)
                        {
                            commentTextField.setText(test.getComment());
                        }
                        parentEditor.enableOkButton();
                    }
                }
            }
        });
    }

    private void initComponents2()
    {
        initTextFields();
        initButtons();
        initCombos();
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

    private boolean valid()
    {
        boolean ret = true;

        if(commentTextField.getText().length() == 0)
        {
            statusLabel.setText(MSG_STATUS_ERROR_COMMENT_ZERO_LEN);
            return false;
        }

        if(testIdLabel.getText().length() == 0)
        {
            statusLabel.setText(MSG_STATUS_ERROR_TEST);
            return false;
        }

        statusLabel.setText(MSG_STATUS_OK);
        return ret;
    }

    /** Creates new form RegexDatatypeEditor */
    public CriterionEditor(EditorDialog parentEd, Criterion criterion)
    {
        initComponents();
        this.criterion = criterion;
        this.parentEditor = parentEd;

        initComponents2();

        String initialComment = criterion.getComment();

        if(initialComment != null)
        {
            commentTextField.setText(initialComment);
        }

        String initialTestId = criterion.getTestId();

        if(initialTestId != null)
        {
            testIdLabel.setText(initialTestId);
        }

        setNegated(criterion.isNegated());

        if(!valid())
        {
            parentEd.disableOkButton();
        }
        else
        {
            parentEd.enableOkButton();
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
        testIdCaption = new javax.swing.JLabel();
        testIdLabel = new javax.swing.JLabel();
        chooseTestButton = new javax.swing.JButton();
        statusCaption = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        negateCaption = new javax.swing.JLabel();
        negateCombo = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        commentCaption.setText("Comment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 5);
        add(commentCaption, gridBagConstraints);

        commentTextField.setPreferredSize(new java.awt.Dimension(255, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 5, 10);
        add(commentTextField, gridBagConstraints);

        testIdCaption.setText("Test id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 5);
        add(testIdCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 5);
        add(testIdLabel, gridBagConstraints);

        chooseTestButton.setText("Choose Test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.07;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 10);
        add(chooseTestButton, gridBagConstraints);

        statusCaption.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 6, 10);
        add(statusCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 6, 10);
        add(statusLabel, gridBagConstraints);

        negateCaption.setText("Negate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 6, 10);
        add(negateCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        add(negateCombo, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseTestButton;
    private javax.swing.JLabel commentCaption;
    private javax.swing.JTextField commentTextField;
    private javax.swing.JLabel negateCaption;
    private javax.swing.JComboBox negateCombo;
    private javax.swing.JLabel statusCaption;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel testIdCaption;
    private javax.swing.JLabel testIdLabel;
    // End of variables declaration//GEN-END:variables

    public Object getData()
    {
        criterion.setComment(commentTextField.getText());
        criterion.setTestId(testIdLabel.getText());
        criterion.setNegated(getNegated());
        
        return criterion;
    }

    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor.setTitle("Criterion Editor");
    }

    public void setData(Object data)
    {
        commentTextField.requestFocus();
    }

    public void setComment(String comment)
    {
        commentTextField.setText(comment);
    }

    public void setTestId(String testId)
    {
        testIdLabel.setText(testId);
    }
}
