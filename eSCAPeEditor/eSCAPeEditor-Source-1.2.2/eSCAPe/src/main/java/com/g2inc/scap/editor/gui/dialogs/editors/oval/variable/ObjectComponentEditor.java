package com.g2inc.scap.editor.gui.dialogs.editors.oval.variable;
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
import java.util.List;

import com.g2inc.scap.editor.gui.choosers.object.ObjectChooser;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalEntity;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentObject;

public class ObjectComponentEditor extends javax.swing.JPanel implements IEditorPage
{
    private OvalVariableComponentObject objectComp = null;
    private EditorDialog parentEditor = null;
    private OvalDefinitionsDocument ovalDoc;
    private OvalObject ovalObject = null;
    private String itemField = null;

    private void initComboBox(OvalObject ovalObject) {
        itemFieldCombo.removeAllItems();
        if (ovalObject == null) {
            return;
        }
        ovalDoc = ovalObject.getParentDocument();
        String platform = ovalObject.getPlatform();
        String objectType = ovalObject.getElementName();
        int underScoreOffset = objectType.indexOf("_object");
        if (underScoreOffset == -1) {
            throw new IllegalStateException("Invalid object type:" + objectType);
        }
        String objectName = objectType.substring(0,underScoreOffset);
        String stateType = objectName + "_state";
        List<OvalEntity> stateEntityTypes = ovalDoc.getValidStateEntityTypes(platform, stateType);
        for (int i=0; i<stateEntityTypes.size(); i++) {
            OvalEntity entity = stateEntityTypes.get(i);
            itemFieldCombo.addItem(entity.getName());
        }
        itemFieldCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                itemField = (String) itemFieldCombo.getSelectedItem();
                objectComp.setItemField(itemField);
                if (parentEditor != null) {
                    parentEditor.enableOkButton(); // couldn't get here if object not already chosen
                }
            }
        });
    }

    private void initButtons()
    {
        chooseObjectButton.addActionListener(new ActionListener()
        {
        	@Override
            public void actionPerformed(ActionEvent ae)
            {
                ObjectChooser chooser = new ObjectChooser(true);
                chooser.setSource(objectComp.getParentDocument(), null, null);
                
                chooser.setVisible(true);

                if(!chooser.wasCancelled())
                {
                    ovalObject = (OvalObject) chooser.getChosen();
                    objectLabel.setText(ovalObject.getId());
                    objectComp.setObjectId(ovalObject.getId());
                    itemField = null;
                    initComboBox(ovalObject);
                    if(itemFieldCombo.getItemCount() > 0)
                    {
                        String selected = (String) itemFieldCombo.getSelectedItem();

                        itemFieldCombo.setSelectedItem(selected);
                    }
                }
            }
        });
    }

    private void initComponents2()
    {
        initButtons();
    }


    private boolean valid()
    {
        boolean ret = true;
        if(itemField == null || itemField.length() == 0) {
            return false;
        }
        if(ovalObject == null || ovalObject.getId() == null || ovalObject.getId().length() == 0) {
            return false;
        }
        return ret;
    }

    public ObjectComponentEditor()
    {
        initComponents();
//        setOvalVariableComponentObject(objComp);
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

        objectCaption = new javax.swing.JLabel();
        objectLabel = new javax.swing.JLabel();
        chooseObjectButton = new javax.swing.JButton();
        itemFieldCaption = new javax.swing.JLabel();
        itemFieldCombo = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        objectCaption.setText("Object");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 2);
        add(objectCaption, gridBagConstraints);

        objectLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        objectLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        objectLabel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        objectLabel.setMinimumSize(new java.awt.Dimension(128, 255));
        objectLabel.setPreferredSize(new java.awt.Dimension(128, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 4, 4);
        add(objectLabel, gridBagConstraints);

        chooseObjectButton.setText("Choose Object");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 5, 5);
        add(chooseObjectButton, gridBagConstraints);

        itemFieldCaption.setText("Item Field");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 9, 0, 0);
        add(itemFieldCaption, gridBagConstraints);

        itemFieldCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 8);
        add(itemFieldCombo, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseObjectButton;
    private javax.swing.JLabel itemFieldCaption;
    private javax.swing.JComboBox itemFieldCombo;
    private javax.swing.JLabel objectCaption;
    private javax.swing.JLabel objectLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getData()
    {   
        return null;
    }

    @Override
    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
        parentEditor.setTitle("Object Component Editor");
    }

    @Override
    public void setData(Object data)
    {
    	setOvalVariableComponentObject((OvalVariableComponentObject) data);
    }

    private void setOvalVariableComponentObject(OvalVariableComponentObject objectComp) {
        this.objectComp = objectComp;
        ovalObject = objectComp.getObject();
        itemField = objectComp.getItemField();
        if (ovalObject != null)
        {
            objectLabel.setText(ovalObject.getId());
        }
        initComboBox(ovalObject);
        if (itemField != null) {
            itemFieldCombo.setSelectedItem(itemField);
        }
    }
}
