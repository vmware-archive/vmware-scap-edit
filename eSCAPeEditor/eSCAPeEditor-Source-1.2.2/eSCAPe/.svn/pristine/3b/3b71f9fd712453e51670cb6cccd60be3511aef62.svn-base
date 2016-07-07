package com.g2inc.scap.editor.gui.dialogs.editors.xccdf;
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

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;

import com.g2inc.scap.editor.gui.choosers.xccdf.ValueChooser;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.DocumentListenerAdaptor;
import com.g2inc.scap.library.domain.xccdf.ProfileRefineValue;
import com.g2inc.scap.library.domain.xccdf.Value;
import com.g2inc.scap.library.domain.xccdf.ValueOperatorEnum;

public class ProfileRefineValueEditor extends JPanel implements IEditorPage, ChangeListener
{

    private EditorDialog parentEditor = null;
    private ProfileRefineValue profileRefineValue = null;

    private void initButtons()
    {
        chooseValueButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                ValueChooser chooser = new ValueChooser(true);
                chooser.setSource(profileRefineValue.getSCAPDocument(), null, null);
                chooser.setVisible(true);

                if (!chooser.wasCancelled())
                {
                    Value value = (Value) chooser.getChosen();
                    selectedIdLabel.setText(value.getId());
                    profileRefineValue.setIdRef(value.getId());
                    parentEditor.enableOkButton();
                }
            }
        });
    }

    private void initComboBoxes()
    {
        operatorComboBox.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                ValueOperatorEnum operator = (ValueOperatorEnum) operatorComboBox.getSelectedItem();
                if (operator != null && profileRefineValue != null)
                {
                    profileRefineValue.setOperator(operator);
                }
            }
        });

        operatorComboBox.removeAllItems();
        ValueOperatorEnum[] opers = ValueOperatorEnum.values();
        for (int i = 0; i < opers.length; i++)
        {
            operatorComboBox.addItem(opers[i]);
        }
    }

    private void initTextFields()
    {
        selectorTextField.getDocument().addDocumentListener(new DocumentListenerAdaptor()
        {

            @Override
            public void changed(DocumentEvent de)
            {
                profileRefineValue.setSelector(selectorTextField.getText());
            }
        });
    }

    private void initComponents2()
    {
        initButtons();
        initTextFields();
        initComboBoxes();
    }

    public ProfileRefineValueEditor()
    {
        initComponents();
        initComponents2();
        remarkListPanel1.addChangeListener(this);
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

        selectedIdCaption1 = new javax.swing.JLabel();
        selectedIdLabel = new javax.swing.JLabel();
        chooseValueButton = new javax.swing.JButton();
        selectorIdCaption = new javax.swing.JLabel();
        selectorTextField = new javax.swing.JTextField();
        operatorCaption = new javax.swing.JLabel();
        remarkListPanel1 = new com.g2inc.scap.editor.gui.windows.xccdf.RemarkListPanel();
        operatorComboBox = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        selectedIdCaption1.setText("Selected Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 2);
        add(selectedIdCaption1, gridBagConstraints);

        selectedIdLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        selectedIdLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        selectedIdLabel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        selectedIdLabel.setMinimumSize(new java.awt.Dimension(128, 255));
        selectedIdLabel.setPreferredSize(new java.awt.Dimension(128, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 4, 4);
        add(selectedIdLabel, gridBagConstraints);

        chooseValueButton.setText("Choose Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 5, 5);
        add(chooseValueButton, gridBagConstraints);

        selectorIdCaption.setText("Selector:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 2);
        add(selectorIdCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 5);
        add(selectorTextField, gridBagConstraints);

        operatorCaption.setText("Operator:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(operatorCaption, gridBagConstraints);

        remarkListPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Remarks", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 5);
        add(remarkListPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 2, 0);
        add(operatorComboBox, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseValueButton;
    private javax.swing.JLabel operatorCaption;
    private javax.swing.JComboBox operatorComboBox;
    private com.g2inc.scap.editor.gui.windows.xccdf.RemarkListPanel remarkListPanel1;
    private javax.swing.JLabel selectedIdCaption1;
    private javax.swing.JLabel selectedIdLabel;
    private javax.swing.JLabel selectorIdCaption;
    private javax.swing.JTextField selectorTextField;
    // End of variables declaration//GEN-END:variables

    @Override
    public ProfileRefineValue getData()
    {
        return profileRefineValue;
    }

    @Override
    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
        parentEditor.setTitle("Profile Refine Value Editor");
    }

    @Override
    public void setData(Object data)
    {
        profileRefineValue = (ProfileRefineValue) data;
        if (profileRefineValue != null)
        {
            String selectedId = profileRefineValue.getIdRef();
            if (selectedId != null)
            {
                selectedIdLabel.setText(selectedId);
            }
            selectorTextField.setText(profileRefineValue.getSelector());
            ValueOperatorEnum oper = profileRefineValue.getOperator();
            
            operatorComboBox.setSelectedItem(oper);

            remarkListPanel1.setDoc(profileRefineValue.getRemarkList());
        }
    }

    @Override
    public void stateChanged(ChangeEvent ce)
    {
        Object eventSource = ce.getSource();
        if (eventSource == remarkListPanel1)
        {
            if (remarkListPanel1.hasChanged())
            {
                profileRefineValue.setRemarkList(remarkListPanel1.getDoc());
                EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
            }
        }
    }
}
