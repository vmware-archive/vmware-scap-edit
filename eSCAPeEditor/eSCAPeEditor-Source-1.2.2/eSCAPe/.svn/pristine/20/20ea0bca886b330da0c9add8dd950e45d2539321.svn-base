package com.g2inc.scap.editor.gui.dialogs.editors.oval.baseid;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;

/**
 * Editor page used to edit the base id of the oval definitions document.
 * The base id is used in autogeneration of id strings of definitions, objects,
 * tests, states, etc.
 * 
 * @author ssill
 */
public class BaseIdEditor extends javax.swing.JPanel implements IEditorPage
{
    private static final Logger log = Logger.getLogger(BaseIdEditor.class);
    private OvalDefinitionsDocument ovalDoc;
    private EditorDialog parentEditor = null;
    private String baseId;

    private void initFields() {
    	log.debug("initFields - baseId=" + baseId); 
        baseComboBox.removeAllItems();
        HashSet<String> existingBases = new HashSet<String>();
        if (baseId != null) {
            existingBases.add(baseId);
        }
        addToExistingBases(ovalDoc.getDocDefinitionIds(), existingBases);
        addToExistingBases(ovalDoc.getDocTestIds(), existingBases);
        addToExistingBases(ovalDoc.getDocObjectIds(), existingBases);
        addToExistingBases(ovalDoc.getDocStateIds(), existingBases);
        addToExistingBases(ovalDoc.getDocVariableIds(), existingBases);

        ArrayList<String> existingList = new ArrayList<String>(existingBases);
        Collections.sort(existingList);
        int selectedItemIndex = 0;
        if (baseId != null ) {
            if (existingList.contains(baseId)) {
                selectedItemIndex = existingList.indexOf(baseId);
            } else {
                existingList.add(0, baseId);
            }
        } else {
            existingList.add(0,"");
        }
        for (int i=0; i<existingList.size(); i++) {
            baseComboBox.addItem(existingList.get(i));
        }
        baseComboBox.setSelectedIndex(selectedItemIndex);
        baseComboBox.setEditable(true);
     }

    private void initComponents2() {
        baseComboBox.removeAllItems();
        baseComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
               baseId = (String) baseComboBox.getSelectedItem();
               log.debug("actionListener - baseId set to " + baseId); 
            }
        });
    }

    private void addToExistingBases(Set<String> idSet, Set<String> existingBases) {
        String id;
        Iterator<String> iter = idSet.iterator();
        while (iter.hasNext()) {
            id = iter.next();
            if (id.startsWith("oval:")) {
                id = id.substring("oval:".length());  // skip over "oval:"
                int colonOffset = id.indexOf(":");
                if (colonOffset != -1) {
                    String existingBase = id.substring(0,colonOffset);
                    existingBases.add(existingBase);
                }
            }
        }
    }

    /** Creates new form RegexDatatypeEditor */
    public BaseIdEditor() {
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

        panel = new javax.swing.JPanel();
        baseCaption = new javax.swing.JLabel();
        ovalColonLabel = new javax.swing.JLabel();
        baseComboBox = new javax.swing.JComboBox();
        explanationLabel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(234, 200));
        setPreferredSize(new java.awt.Dimension(525, 200));
        setLayout(new java.awt.GridBagLayout());

        panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "OVAL Base Identifier", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        panel.setLayout(new java.awt.GridBagLayout());

        baseCaption.setText("Base");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 0, 5);
        panel.add(baseCaption, gridBagConstraints);

        ovalColonLabel.setText("oval:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        panel.add(ovalColonLabel, gridBagConstraints);

        baseComboBox.setEditable(true);
        baseComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.75;
        panel.add(baseComboBox, gridBagConstraints);

        explanationLabel.setText("<html>The \"OVAL base identifier\" is used by this editor as the first part of the id field of any <b>NEW</b> OVAL definition, test, object, state, or variable that the editor creates. It is stored with the OVAL document. For example, in the OVAL definition id<p>\noval:<font color=\"red\"><b>gov.nist.fdcc.vista</b></font>:def:6007<p>\nthe part of the id shown in <font color=\"red\"><b>red</b></font> is the base id.</html>\n\n");
        explanationLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        explanationLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        explanationLabel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        explanationLabel.setMinimumSize(new java.awt.Dimension(114, 128));
        explanationLabel.setPreferredSize(new java.awt.Dimension(400, 128));
        explanationLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 0, 5);
        panel.add(explanationLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(panel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel baseCaption;
    private javax.swing.JComboBox baseComboBox;
    private javax.swing.JLabel explanationLabel;
    private javax.swing.JLabel ovalColonLabel;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getData() {
    	log.debug("getData returning null");
        return null;
    }

    @Override
    public void setEditorDialog(EditorDialog editorDialog) {
        parentEditor = editorDialog;
    }

    @Override
    public void setData(Object data) {
    }

    public String getBaseId() {
        if (baseId != null && baseId.equals("")) {
            baseId = null;
        }
    	log.debug("getBaseId returning baseId=" + baseId);
        return baseId;
    }

    public void setBaseId(String id) {
        baseId = id;
        log.debug("setBaseId setting baseId=" + baseId);
        initFields();
    }

    public void setOvalDoc(OvalDefinitionsDocument doc) {
        this.ovalDoc = doc;
    }
}
