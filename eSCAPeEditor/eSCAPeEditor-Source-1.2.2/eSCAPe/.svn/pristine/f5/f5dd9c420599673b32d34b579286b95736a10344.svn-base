package com.g2inc.scap.editor.gui.windows.oval.definition;
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
import java.lang.ref.WeakReference;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.WeakChangeListener;
import com.g2inc.scap.editor.gui.windows.oval.OvalTab;
import com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.DefinitionClassEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalVersion;

public class DefinitionGeneralDetailTab extends OvalTab implements ChangeListener, ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private DefinitionSourceDetailTab sourceTab = null;
    private WeakReference<OvalDefinition> doc = null;

    private void initCombo()
    {
        defClassCombo.removeAllItems();
        
        DefinitionClassEnum[] defClasses = DefinitionClassEnum.values();
        for(int x = 0; x < defClasses.length; x++)
        {
            defClassCombo.addItem(defClasses[x]);
        }
    }

    private void initComponents2()
    {
        defIdField.addChangeListener(new WeakChangeListener(this));
        defIdField.setPattern(OvalDefinitionsDocument.DEFINITION_ID_PATTERN);
        initCombo();
    }

    /** Creates new form DefinitionDetailTab */
    public DefinitionGeneralDetailTab()
    {
        initComponents();
        initComponents2();
    }

    public OvalDefinition getDoc()
    {
        return doc.get();
    }

    public void setDoc(OvalDefinition document, SCAPDocumentTypeEnum docType)
    {
        this.doc = new WeakReference<OvalDefinition>(document);

        defClassCombo.setSelectedItem(document.getDefinitionClass());
        defClassCombo.addActionListener(this);

        defIdField.setValue(doc.get().getId());
        titleTextArea.setText(doc.get().getMetadata().getTitle());
        versionEditCtrl.setVersion(doc.get().getVersion().getVersionString());
        referencesPanel.setDoc(doc.get());
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();

        if(src == defClassCombo)
        {
            this.doc.get().setDefinitionClass((DefinitionClassEnum)defClassCombo.getSelectedItem());
            updateSource();
            EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);

            // if they changed the class of this definition we need to update the document tree to reflect it.
            parentEditorForm.rebuildTree();

            parentEditorForm.selectDefinition(this.doc.get());
        }
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        Object src = e.getSource();

        if(src instanceof OvalVersionControl)
        {
            OvalVersion ver = new OvalVersion();
            ver.setVersion(versionEditCtrl.getVersion().toString());
            doc.get().setVersion(ver);

            updateSource();
            EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
        }
        else if(src == defIdField)
        {
            if(defIdField.isValidInput())
            {
                // the id typed in is valid
                String newName = defIdField.getValue();

                if(!newName.equals(doc.get().getId()))
                {
                    if(doc.get().getParentDocument().containsDefinition(newName))
                    {
                        // the id they are trying to set already exists.
                        defIdCaption.setForeground(defIdField.getErrorTextColor());
                    }
                    else
                    {
                        // the new name is available, use it.
                        defIdCaption.setForeground(defIdField.getNormalTextColor());
                        doc.get().setId(newName);

                        updateSource();
                        parentEditorForm.updatedSelectedPath();
                        EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                    }
                }
                else
                {
                    // the id they typed in is the same as the original
                    // nothing to do
                    defIdCaption.setForeground(defIdField.getNormalTextColor());
                }
            }
            else
            {
                defIdCaption.setForeground(defIdField.getErrorTextColor());
            }
        }
    }

    public DefinitionSourceDetailTab getSourceTab()
    {
        return sourceTab;
    }

    public void setSourceTab(DefinitionSourceDetailTab sourceTab)
    {
        this.sourceTab = sourceTab;
    }

    private void updateSource()
    {
        sourceTab.setXMLSource(elementToString(doc.get().getElement()));
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

        overviewPanel = new javax.swing.JPanel();
        defClassCaption = new javax.swing.JLabel();
        defClassCombo = new javax.swing.JComboBox();
        defIdCaption = new javax.swing.JLabel();
        defIdField = new com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField();
        defTitleCaption = new javax.swing.JLabel();
        versionEditCtrl = new com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl();
        titleScrollPane = new javax.swing.JScrollPane();
        titleTextArea = new javax.swing.JTextArea();
        referencesPanel = new com.g2inc.scap.editor.gui.windows.common.ReferencesSummaryPanel();

        setLayout(new java.awt.GridBagLayout());

        overviewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Overview", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        overviewPanel.setLayout(new java.awt.GridBagLayout());

        defClassCaption.setText("Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        overviewPanel.add(defClassCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        overviewPanel.add(defClassCombo, gridBagConstraints);

        defIdCaption.setText("Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        overviewPanel.add(defIdCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        overviewPanel.add(defIdField, gridBagConstraints);

        defTitleCaption.setText("Title:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        overviewPanel.add(defTitleCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        overviewPanel.add(versionEditCtrl, gridBagConstraints);

        titleTextArea.setColumns(20);
        titleTextArea.setEditable(false);
        titleTextArea.setLineWrap(true);
        titleTextArea.setRows(5);
        titleTextArea.setWrapStyleWord(true);
        titleTextArea.setAutoscrolls(false);
        titleTextArea.setEnabled(false);
        titleScrollPane.setViewportView(titleTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 8);
        overviewPanel.add(titleScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.4;
        add(overviewPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.3;
        add(referencesPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel defClassCaption;
    private javax.swing.JComboBox defClassCombo;
    private javax.swing.JLabel defIdCaption;
    private com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField defIdField;
    private javax.swing.JLabel defTitleCaption;
    private javax.swing.JPanel overviewPanel;
    private com.g2inc.scap.editor.gui.windows.common.ReferencesSummaryPanel referencesPanel;
    private javax.swing.JScrollPane titleScrollPane;
    private javax.swing.JTextArea titleTextArea;
    private com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl versionEditCtrl;
    // End of variables declaration//GEN-END:variables

    // End of variables declaration
}
