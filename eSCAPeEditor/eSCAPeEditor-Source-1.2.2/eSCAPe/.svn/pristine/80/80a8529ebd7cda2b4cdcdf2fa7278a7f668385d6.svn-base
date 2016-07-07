package com.g2inc.scap.editor.gui.windows.xccdf;

/* ESCAPE Software   Copyright 2010 G2, Inc. - All rights reserved.
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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.windows.common.ChangeNotifierPanel;
import com.g2inc.scap.library.domain.xccdf.Value;
import com.g2inc.scap.library.domain.xccdf.ValueOperatorEnum;
import com.g2inc.scap.library.domain.xccdf.InterfaceHintEnum;
import com.g2inc.scap.library.domain.xccdf.ValueTypeEnum;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ValueTypeOperTab extends ChangeNotifierPanel implements IEditorPage, ChangeListener, ActionListener, ItemListener
{
    private EditorDialog parentEditor = null;
    private Value value = null;

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();
        if(src == typeCombo)
        {
            ValueTypeEnum type = (ValueTypeEnum) typeCombo.getSelectedItem();
            if (type != null && value != null)
            {
                value.setType(type);
                ValueOperatorEnum[] validOperators = type.getValidOperators();
                reinitOperatorComboBasedOnType(validOperators);

                operCombo.setSelectedItem(ValueOperatorEnum.EQUALS);
                value.setOperator(ValueOperatorEnum.EQUALS);
                
                notifyRegisteredListeners();
            }
        }
        else if (src == operCombo)
        {
            ValueOperatorEnum oper = (ValueOperatorEnum) operCombo.getSelectedItem();
            if (oper != null & value != null)
            {
                value.setOperator(oper);
                notifyRegisteredListeners();
            }
        }
        else if (src == interfaceHintCombo)
        {
            InterfaceHintEnum interfaceHint = (InterfaceHintEnum) interfaceHintCombo.getSelectedItem();
            if (interfaceHint != null & value != null)
            {
                value.setInterfaceHint(interfaceHint);
                notifyRegisteredListeners();
            }
        }
        else if (src == interactiveCheckBox)
        {
            InterfaceHintEnum interfaceHint = (InterfaceHintEnum) interfaceHintCombo.getSelectedItem();
            if (interfaceHint != null & value != null)
            {
                value.setInterfaceHint(interfaceHint);
                notifyRegisteredListeners();
            }
        }
    }
    
    public void itemStateChanged(ItemEvent ie){
        Object src = ie.getSource();
        if (src == interactiveCheckBox) {
            value.setInteractive(ie.getStateChange() == ItemEvent.SELECTED);
        }
    }

    private void initComboBoxes()
    {
        typeCombo.removeAllItems();
        ValueTypeEnum[] types = ValueTypeEnum.values();
        for (ValueTypeEnum type : types)
        {
            typeCombo.addItem(type);
        }
        interfaceHintCombo.removeAllItems();
        InterfaceHintEnum[] interfaceHints = InterfaceHintEnum.values();
        for (InterfaceHintEnum interfaceHint : interfaceHints) {
            interfaceHintCombo.addItem(interfaceHint);
        }
    }

    private void reinitOperatorComboBasedOnType(ValueOperatorEnum[] validEnums)
    {
        ValueOperatorEnum defaultOperator = ValueOperatorEnum.EQUALS;
        operCombo.removeActionListener(this);
        operCombo.removeAllItems();
        for (int i = 0; i < validEnums.length; i++)
        {
            operCombo.addItem(validEnums[i]);
        }

        operCombo.addActionListener(this);
    
        operCombo.setSelectedItem(defaultOperator);
    }

    private void initComponents2()
    {
        initComboBoxes();
        simpleOrComplexValueListPanel1.addChangeListener(this);
        simpleOrComplexValueListPanel1.setSimpleElementName("value");
        simpleOrComplexValueListPanel1.setComplexElementName("complex-value");
        
        simpleOrComplexDefaultListPanel1.addChangeListener(this);
        simpleOrComplexDefaultListPanel1.setSimpleElementName("default");
        simpleOrComplexDefaultListPanel1.setComplexElementName("complex-default");
    }

    public ValueTypeOperTab()
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

        valueIdCaption = new javax.swing.JLabel();
        valueIdLabel = new javax.swing.JLabel();
        typeCaption = new javax.swing.JLabel();
        typeCombo = new javax.swing.JComboBox();
        operatorCaption = new javax.swing.JLabel();
        operCombo = new javax.swing.JComboBox();
        interactiveCheckBox = new javax.swing.JCheckBox();
        interfaceHintCaption = new javax.swing.JLabel();
        interfaceHintCombo = new javax.swing.JComboBox();
        simpleOrComplexValueListPanel1 = new com.g2inc.scap.editor.gui.windows.xccdf.SimpleOrComplexValueListPanel();
        simpleOrComplexDefaultListPanel1 = new com.g2inc.scap.editor.gui.windows.xccdf.SimpleOrComplexValueListPanel();

        valueIdCaption.setText("Value Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 3, 2);
        add(valueIdCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 3, 5);
        add(valueIdLabel, gridBagConstraints);

        typeCaption.setText("Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 2);
        add(typeCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 5);
        add(typeCombo, gridBagConstraints);

        operatorCaption.setText("Operator:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(operatorCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 2, 5);
        add(operCombo, gridBagConstraints);

        interactiveCheckBox.setText("Interactive");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        add(interactiveCheckBox, gridBagConstraints);

        interfaceHintCaption.setText("Interface Hint:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(interfaceHintCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 2, 5);
        add(interfaceHintCombo, gridBagConstraints);

        simpleOrComplexValueListPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("value Editor"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.25;
        add(simpleOrComplexValueListPanel1, gridBagConstraints);

        simpleOrComplexDefaultListPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("suggested default Editor"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.25;
        add(simpleOrComplexDefaultListPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox interactiveCheckBox;
    private javax.swing.JLabel interfaceHintCaption;
    private javax.swing.JComboBox interfaceHintCombo;
    private javax.swing.JComboBox operCombo;
    private javax.swing.JLabel operatorCaption;
    private com.g2inc.scap.editor.gui.windows.xccdf.SimpleOrComplexValueListPanel simpleOrComplexDefaultListPanel1;
    private com.g2inc.scap.editor.gui.windows.xccdf.SimpleOrComplexValueListPanel simpleOrComplexValueListPanel1;
    private javax.swing.JLabel typeCaption;
    private javax.swing.JComboBox typeCombo;
    private javax.swing.JLabel valueIdCaption;
    private javax.swing.JLabel valueIdLabel;
    // End of variables declaration//GEN-END:variables

    
    @Override
    public Value getData()
    {
	return value;
    }

    
    @Override
    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
        parentEditor.setTitle("Value Type/Operation Editor");
    }

    
    @Override
    public void setData(Object data)
    {
        value = (Value) data;
        if (value != null)
        {
            ValueTypeEnum defaultType = value.getType();
            ValueOperatorEnum defaultOperator = ValueOperatorEnum.EQUALS;

            ValueTypeEnum type = value.getType();
            if (type != null)
            {
                typeCombo.setSelectedItem(type);
            }

            ValueOperatorEnum[] validOperators = defaultType.getValidOperators();
            operCombo.removeAllItems();
            for (int i = 0; i < validOperators.length; i++)
            {
                operCombo.addItem(validOperators[i]);
            }

            ValueOperatorEnum oper = value.getOperator();
            if (oper != null)
            {
                operCombo.setSelectedItem(oper);
            }

            typeCombo.addActionListener(this);
            operCombo.addActionListener(this);
            interfaceHintCombo.addActionListener(this);
            interactiveCheckBox.addItemListener(this);
            
            valueIdLabel.setText(value.getId());
            simpleOrComplexValueListPanel1.setDoc(value.getValueElementList());
            simpleOrComplexDefaultListPanel1.setDoc(value.getDefaultElementList());
        }
    }
	
    @Override
    public void stateChanged(ChangeEvent e) {
        Object eventSource = e.getSource();
        if (eventSource == simpleOrComplexValueListPanel1) {
            value.setValueElementList(simpleOrComplexValueListPanel1.getDoc());
            notifyRegisteredListeners();
        } else if (eventSource == simpleOrComplexDefaultListPanel1) {
            value.setDefaultElementList(simpleOrComplexDefaultListPanel1.getDoc());
            notifyRegisteredListeners();
        }
    }
}
