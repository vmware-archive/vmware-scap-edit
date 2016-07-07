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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.NCNameDatatypeEditor;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.ChangeNotifierPanel;
import com.g2inc.scap.library.domain.xccdf.Item;

public class ItemAttributesDetailTab extends ChangeNotifierPanel implements ActionListener, ItemListener
{
    private Item item = null;
    private JFrame parentWin = null;
	private static Logger log = Logger.getLogger(ItemAttributesDetailTab.class);

    /** Creates new form DefinitionDetailTab */
    public ItemAttributesDetailTab()
    {
        initComponents();
		initButtons();
		initCheckBoxes();
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();
        if(src == editClusterIdButton)
        {
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            NCNameDatatypeEditor editorPage = new NCNameDatatypeEditor();
            editor.setEditorPage(editorPage);
            editor.setData(item.getClusterId());
            editor.pack();
            editor.setLocationRelativeTo(null);
            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                String clusterId = (String) editor.getData();
                item.setClusterId(clusterId);
                clusterIdTextField.setText(clusterId);
                notifyRegisteredListeners();
            }
        }
        else if(src == editExtendsButton)
        {
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            NCNameDatatypeEditor editorPage = new NCNameDatatypeEditor();
            editor.setEditorPage(editorPage);
            editor.setData(item.getExtends());
            editor.pack();
            editor.setLocationRelativeTo(null);
            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                String value = (String) editor.getData();
                item.setExtends(value);
                extendsTextField.setText(value);
            }
        }
    }

    private void initButtons()
    {
	editClusterIdButton.addActionListener(this);
	editExtendsButton.addActionListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent ie)
    {
        Object src = ie.getSource();
        if(src == abstractCheckBox)
        {
            item.setAbstract(ie.getStateChange() == ItemEvent.SELECTED);
            notifyRegisteredListeners();
        }
        else if(src == hiddenCheckBox)
        {
            item.setHidden(ie.getStateChange() == ItemEvent.SELECTED);
            notifyRegisteredListeners();
        }
        else if(src == prohibitChangesCheckBox)
        {
            item.setProhibitChanges(ie.getStateChange() == ItemEvent.SELECTED);
            notifyRegisteredListeners();
        }

    }
    private void initCheckBoxes()
    {
        abstractCheckBox.addItemListener(this);
        hiddenCheckBox.addItemListener(this);
        prohibitChangesCheckBox.addItemListener(this);
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

        idCaption = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        cluserIdCaption = new javax.swing.JLabel();
        clusterIdTextField = new javax.swing.JTextField();
        editClusterIdButton = new javax.swing.JButton();
        extendsCaption = new javax.swing.JLabel();
        extendsTextField = new javax.swing.JTextField();
        editExtendsButton = new javax.swing.JButton();
        abstractCheckBox = new javax.swing.JCheckBox();
        hiddenCheckBox = new javax.swing.JCheckBox();
        prohibitChangesCheckBox = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        idCaption.setText("Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 7, 0, 0);
        add(idCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(idLabel, gridBagConstraints);

        cluserIdCaption.setText("cluster-id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 0);
        add(cluserIdCaption, gridBagConstraints);

        clusterIdTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(clusterIdTextField, gridBagConstraints);

        editClusterIdButton.setText("Edit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 5);
        add(editClusterIdButton, gridBagConstraints);

        extendsCaption.setText("extends:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 0);
        add(extendsCaption, gridBagConstraints);

        extendsTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        add(extendsTextField, gridBagConstraints);

        editExtendsButton.setText("Edit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 5);
        add(editExtendsButton, gridBagConstraints);

        abstractCheckBox.setText("Abstract");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        add(abstractCheckBox, gridBagConstraints);

        hiddenCheckBox.setText("Hidden");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(hiddenCheckBox, gridBagConstraints);

        prohibitChangesCheckBox.setText("Prohibit Changes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(prohibitChangesCheckBox, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox abstractCheckBox;
    private javax.swing.JLabel cluserIdCaption;
    private javax.swing.JTextField clusterIdTextField;
    private javax.swing.JButton editClusterIdButton;
    private javax.swing.JButton editExtendsButton;
    private javax.swing.JLabel extendsCaption;
    private javax.swing.JTextField extendsTextField;
    private javax.swing.JCheckBox hiddenCheckBox;
    private javax.swing.JLabel idCaption;
    private javax.swing.JLabel idLabel;
    private javax.swing.JCheckBox prohibitChangesCheckBox;
    // End of variables declaration//GEN-END:variables

    @Override
    public JFrame getParentWin()
    {
        return parentWin;
    }

    @Override
    public void setParentWin(JFrame parentWin)
    {
        this.parentWin = parentWin;
    }

    public void setDoc(Item item)
    {
        this.item = item;
        idLabel.setText(item.getId());
        abstractCheckBox.setSelected(item.isAbstract());
        hiddenCheckBox.setSelected(item.isHidden());
        prohibitChangesCheckBox.setSelected(item.isProhibitChanges());
        if (item.getExtends() != null)
        {
            extendsTextField.setText(item.getExtends());
        }
        if (item.getClusterId() != null)
        {
            clusterIdTextField.setText(item.getClusterId());
        }
    }
}
