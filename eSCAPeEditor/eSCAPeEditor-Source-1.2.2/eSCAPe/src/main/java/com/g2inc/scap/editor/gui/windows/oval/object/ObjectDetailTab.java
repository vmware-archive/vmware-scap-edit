package com.g2inc.scap.editor.gui.windows.oval.object;
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
import javax.swing.tree.DefaultMutableTreeNode;

import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.GenericSourceDetailTab;
import com.g2inc.scap.editor.gui.windows.oval.OvalTab;
import com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalVersion;

public class ObjectDetailTab extends OvalTab implements ChangeListener
{

    private static final long serialVersionUID = 1L;
    private OvalObject doc = null;
    private GenericSourceDetailTab sourceTab = null;

    private void initComponents2()
    {
        objParmDispPanel.addChangeListener(this);
        versionEditCtrl.addChangeListener(this);
        objIdField.addChangeListener(this);
        objIdField.setPattern(OvalDefinitionsDocument.OBJECT_ID_PATTERN);
    }

    /** Creates new form DefinitionDetailTab */
    public ObjectDetailTab()
    {
        initComponents();
        initComponents2();
    }

    public void setDoc(OvalObject doc)
    {
        this.doc = doc;
        typeLabel.setText(doc.getElementName());
        objIdField.setValue(doc.getId());
        versionEditCtrl.setVersion(doc.getVersion().getVersionString());
        objParmDispPanel.setParentEditor(getParentEditorForm());
        objParmDispPanel.setOvalObject(doc);
        objParmDispPanel.setModalParent(false);
    }

    public GenericSourceDetailTab getSourceTab()
    {
        return sourceTab;
    }

    public void setSourceTab(GenericSourceDetailTab sourceTab)
    {
        this.sourceTab = sourceTab;
    }

    @Override
    public void stateChanged(ChangeEvent arg0)
    {
        Object src = arg0.getSource();

        if (src instanceof OvalVersionControl)
        {
            OvalVersion ver = new OvalVersion();
            ver.setVersion(versionEditCtrl.getVersion().toString());
            doc.setVersion(ver);

            updateSource();
            EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
        }
        else if (src == objIdField)
        {
            if (objIdField.isValidInput())
            {
                // the id typed in is valid
                String newName = objIdField.getValue();

                if (!newName.equals(doc.getId()))
                {
                    if (doc.getParentDocument().containsObject(newName))
                    {
                        // the id they are trying to set already exists.
                        idCaption.setForeground(objIdField.getErrorTextColor());
                    }
                    else
                    {
                        // the new name is available, use it.
                        idCaption.setForeground(objIdField.getNormalTextColor());
                        doc.setId(newName);

                        updateSource();
                        parentEditorForm.updatedSelectedPath();
                        EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                    }
                }
                else
                {
                    // the id they typed in is the same as the original
                    // nothing to do
                    idCaption.setForeground(objIdField.getNormalTextColor());
                }
            }
            else
            {
                idCaption.setForeground(objIdField.getErrorTextColor());
            }
        }
        else
        {
            if (!objParmDispPanel.allParmsOk())
            {
                // TODO: show some indication that parms are not ok
            }

            doc.setParameters((DefaultMutableTreeNode) objParmDispPanel.getObjParmTree().getModel().getRoot());

            updateSource();
            EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);


        }
    }

    private void updateSource()
    {
        sourceTab.setXMLSource(elementToString(doc.getElement()));
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
        objIdField = new com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField();
        typeCaption = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        versionEditCtrl = new com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl();
        structureDetailPanel = new javax.swing.JPanel();
        objParmDispPanel = new com.g2inc.scap.editor.gui.windows.oval.object.ObjectParametersDisplayPanel();
        fillerPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        generalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "General", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        generalPanel.setLayout(new java.awt.GridBagLayout());

        idCaption.setText("Object Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 11, 0, 0);
        generalPanel.add(idCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 11);
        generalPanel.add(objIdField, gridBagConstraints);

        typeCaption.setText("Object Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 11, 0, 10);
        generalPanel.add(typeCaption, gridBagConstraints);

        typeLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 11);
        generalPanel.add(typeLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 0);
        generalPanel.add(versionEditCtrl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0050;
        add(generalPanel, gridBagConstraints);

        structureDetailPanel.setLayout(new java.awt.GridBagLayout());

        objParmDispPanel.setMaximumSize(null);
        objParmDispPanel.setMinimumSize(null);
        objParmDispPanel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        structureDetailPanel.add(objParmDispPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.6;
        add(structureDetailPanel, gridBagConstraints);

        fillerPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        add(fillerPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel fillerPanel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel idCaption;
    private com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField objIdField;
    private com.g2inc.scap.editor.gui.windows.oval.object.ObjectParametersDisplayPanel objParmDispPanel;
    private javax.swing.JPanel structureDetailPanel;
    private javax.swing.JLabel typeCaption;
    private javax.swing.JLabel typeLabel;
    private com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl versionEditCtrl;
    // End of variables declaration//GEN-END:variables
    // End of variables declaration
}
