package com.g2inc.scap.editor.gui.windows.oval.state;
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

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.GenericSourceDetailTab;
import com.g2inc.scap.editor.gui.windows.oval.OvalTab;
import com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalVersion;

public class StateDetailTab extends OvalTab implements ChangeListener
{
	private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(StateDetailTab.class);
    
	private OvalState doc = null;
    private GenericSourceDetailTab sourceTab = null;

    public void initPanel()
    {
        stateParmsPanel.addChangeListener(this);
        stateParmsPanel.setModalParent(false);
    }

    public void initComponents2()
    {
        initPanel();

        stateIdField.addChangeListener(this);
        stateIdField.setPattern(OvalDefinitionsDocument.STATE_ID_PATTERN);
    }

    /** Creates new form DefinitionDetailTab */
    public StateDetailTab()
    {
        initComponents();
        initComponents2();
    }

    public void setDoc(OvalState doc)
    {
        this.doc = doc;
        typeLabel.setText(doc.getElementName());
        stateIdField.setValue(doc.getId());
        stateParmsPanel.setOvalState(doc);
        stateParmsPanel.setModalParent(false);
        stateParmsPanel.setParentEditor(getParentEditorForm());
        versionEditCtrl.setVersion(doc.getVersion().getVersionString());
        versionEditCtrl.addChangeListener(this);
    }

    public GenericSourceDetailTab getSourceTab()
    {
        return sourceTab;
    }

    public void setSourceTab(GenericSourceDetailTab sourceTab)
    {
        this.sourceTab = sourceTab;
    }

    
    public void stateChanged(ChangeEvent ce)
    {
        Object src = ce.getSource();

        if(src == stateParmsPanel)
        {
            if(stateParmsPanel.hasChanged())
            {
                doc.setParameters(stateParmsPanel.getAddedParmsList());
                EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                updateSource();
            }

            if(!stateParmsPanel.allParmsOk())
            {
                log.error("All parms are NOT ok.");
                // TODO:  show some visual indications that
                //        parameters are in some way not valid.
            }
            else
            {
            }
        }
        else if(src instanceof OvalVersionControl)
        {
            OvalVersion ver = new OvalVersion();
            ver.setVersion(versionEditCtrl.getVersion().toString());
            doc.setVersion(ver);

            updateSource();
            EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
        }
        else if(src == stateIdField)
        {
            if(stateIdField.isValidInput())
            {
                // the id typed in is valid
                String newName = stateIdField.getValue();

                if(!newName.equals(doc.getId()))
                {
                    if(doc.getParentDocument().containsState(newName))
                    {
                        // the id they are trying to set already exists.
                        idCaption.setForeground(stateIdField.getErrorTextColor());
                    }
                    else
                    {
                        // the new name is available, use it.
                        idCaption.setForeground(stateIdField.getNormalTextColor());
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
                    idCaption.setForeground(stateIdField.getNormalTextColor());
                }
            }
            else
            {
                idCaption.setForeground(stateIdField.getErrorTextColor());
            }
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
        stateIdField = new com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField();
        typeCaption = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        versionEditCtrl = new com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl();
        parametersPanel = new javax.swing.JPanel();
        stateParmsPanel = new com.g2inc.scap.editor.gui.windows.oval.state.StateParametersDisplayPanel();
        fillerPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        generalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "General", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        generalPanel.setLayout(new java.awt.GridBagLayout());

        idCaption.setText("State Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        generalPanel.add(idCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 7);
        generalPanel.add(stateIdField, gridBagConstraints);

        typeCaption.setText("State Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        generalPanel.add(typeCaption, gridBagConstraints);

        typeLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        generalPanel.add(typeLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
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

        parametersPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        parametersPanel.add(stateParmsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.6;
        add(parametersPanel, gridBagConstraints);

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
    private javax.swing.JPanel parametersPanel;
    private com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField stateIdField;
    private com.g2inc.scap.editor.gui.windows.oval.state.StateParametersDisplayPanel stateParmsPanel;
    private javax.swing.JLabel typeCaption;
    private javax.swing.JLabel typeLabel;
    private com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl versionEditCtrl;
    // End of variables declaration//GEN-END:variables
}
