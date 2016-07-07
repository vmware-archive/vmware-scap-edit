package com.g2inc.scap.editor.gui.windows.oval.variable;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalVariable;
import com.g2inc.scap.library.schema.NameDoc;

public class VariableGeneralPanel extends javax.swing.JPanel
{
    private static Logger log = Logger.getLogger(VariableGeneralPanel.class);
    private OvalVariable ovalVar = null;
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    public static final String VARIABLE_ID_FIELD_NAME = "variableIdField";

    public void addChangeListener(ChangeListener cl)
    {
        if (!changeListeners.contains(cl))
        {
            changeListeners.add(cl);
            versionEditCtrl.addChangeListener(cl);
            variableIdField.addChangeListener(cl);
        }
    }

    public void removeChangeListener(ChangeListener cl)
    {
        if (changeListeners.contains(cl))
        {
            changeListeners.remove(cl);
            versionEditCtrl.removeChangeListener(cl);
            variableIdField.removeChangeListener(cl);
        }
    }

    public Object getVersion()
    {
        return versionEditCtrl.getVersion();
    }

    private void initDataTypeCombo()
    {
        String datatype = ovalVar.getDatatype();
        if (datatype == null)
        {
            datatype = "string";
        }
        datatypeComboBox.removeAllItems();
        List<NameDoc> datatypes = ovalVar.getParentDocument().getDataTypeEnumerations();
        for (int i = 0; i < datatypes.size(); i++)
        {
            String currentDatatype = datatypes.get(i).getName();
            datatypeComboBox.addItem(currentDatatype);
            if (datatype.equals(currentDatatype))
            {
                datatypeComboBox.setSelectedIndex(i);
            }
        }
    }

    private void initDatatypeComboListener()
    {
        datatypeComboBox.removeAllItems();
        datatypeComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                ovalVar.setDatatype((String) datatypeComboBox.getSelectedItem());
            }
        });
    }

    private void initComponents2()
    {
        initTextFields();
        initDatatypeComboListener();
    }

    private void initTextFields()
    {
        commentTextField.getDocument().addDocumentListener(new DocumentListener()
        {
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

            private void common(DocumentEvent de)
            {
                ovalVar.setComment(commentTextField.getText());
            }
        });

        variableIdField.setPattern(OvalDefinitionsDocument.VARIABLE_ID_PATTERN);
    }

    /** Creates new form ObjectParametersDisplayPanel */
    public VariableGeneralPanel()
    {
        initComponents();
        initComponents2();
        
        variableIdField.setName(VARIABLE_ID_FIELD_NAME);
    }

    public JLabel getIDCaption()
    {
        return idCaption;
    }

    public PatternedStringField getVariableIDField()
    {
        return variableIdField;
    }

    public OvalVariable getOvalVariable()
    {
        return ovalVar;
    }

    public void setOvalVariable(OvalVariable ovalVar)
    {
        this.ovalVar = ovalVar;
        variableIdField.setValue(ovalVar.getId());
        typeLabel.setText(ovalVar.getElement().getName());
        commentTextField.setText(ovalVar.getComment());
        versionEditCtrl.setVersion(ovalVar.getVersion().getVersionString());

        initDataTypeCombo();
    }

    public boolean allDataOk()
    {
        boolean ret = true;

        // TODO:  add some validity checking

        return ret;
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

        generalPanel = new javax.swing.JPanel();
        idCaption = new javax.swing.JLabel();
        variableIdField = new com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField();
        typeCaption = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        commentCaption = new javax.swing.JLabel();
        commentTextField = new javax.swing.JTextField();
        datatypeCaption = new javax.swing.JLabel();
        datatypeComboBox = new javax.swing.JComboBox();
        versionEditCtrl = new com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl();

        setLayout(new java.awt.GridBagLayout());

        generalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "General", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        generalPanel.setLayout(new java.awt.GridBagLayout());

        idCaption.setText("Variable Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 9, 0, 0);
        generalPanel.add(idCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        generalPanel.add(variableIdField, gridBagConstraints);

        typeCaption.setText("Variable type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 9, 0, 0);
        generalPanel.add(typeCaption, gridBagConstraints);

        typeLabel.setText("Type goes here");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        generalPanel.add(typeLabel, gridBagConstraints);

        commentCaption.setText("Comment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 9, 0, 0);
        generalPanel.add(commentCaption, gridBagConstraints);

        commentTextField.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
        generalPanel.add(commentTextField, gridBagConstraints);

        datatypeCaption.setText("Datatype");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 9, 0, 0);
        generalPanel.add(datatypeCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        generalPanel.add(datatypeComboBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        generalPanel.add(versionEditCtrl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        add(generalPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel commentCaption;
    private javax.swing.JTextField commentTextField;
    private javax.swing.JLabel datatypeCaption;
    private javax.swing.JComboBox datatypeComboBox;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel idCaption;
    private javax.swing.JLabel typeCaption;
    private javax.swing.JLabel typeLabel;
    private com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField variableIdField;
    private com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl versionEditCtrl;
    // End of variables declaration//GEN-END:variables
}
