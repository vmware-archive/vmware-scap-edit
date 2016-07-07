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

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.StringDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.VersionEditor;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.ChangeNotifierPanel;
import com.g2inc.scap.editor.gui.windows.common.GenericSourceDetailTab;
import com.g2inc.scap.library.domain.xccdf.ItemBasicType;
import com.g2inc.scap.library.domain.xccdf.Version;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

public class ItemDetailPanel extends ChangeNotifierPanel implements ChangeListener, ActionListener
{
    private ItemBasicType item = null;
    private JFrame parentWin = null;
    private GenericSourceDetailTab sourceTab = null;
	private static final Logger log = Logger.getLogger(ItemDetailPanel.class);

    /** Creates new form DefinitionDetailTab */
    public ItemDetailPanel()
    {
        initComponents();
	initComponents2();
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();
        if(src == editIdButton)
        {
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            StringDatatypeEditor stringEditor = new StringDatatypeEditor();
            editor.setEditorPage(stringEditor);
            stringEditor.setData(item.getId());
            editor.pack();
            editor.setLocationRelativeTo(EditorMainWindow.getInstance());
            editor.setVisible(true);
            if (!editor.wasCancelled())
            {
                String updated = (String) editor.getData();
                item.setId(updated);
                idLabel.setText(updated);
                notifyRegisteredListeners();
            }
        }
        else if(src == versionEditButton)
        {
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            VersionEditor versionEditor = new VersionEditor();
            editor.setEditorPage(versionEditor);
            Version version = item.getVersion();
            if (version == null)
            {
                XCCDFBenchmark benchmark = (XCCDFBenchmark) item.getSCAPDocument();
                version = benchmark.createVersion();
            }
            versionEditor.setData(version);
            editor.pack();
            editor.setLocationRelativeTo(EditorMainWindow.getInstance());
            editor.setVisible(true);
            if (!editor.wasCancelled())
            {
                version = (Version) editor.getData();
                item.setVersion(version);
                versionLabel.setText(version.toString());
                notifyRegisteredListeners();
            }
        }
    }

    public void initComponents2()
    {
        editIdButton.addActionListener(this);
        versionEditButton.addActionListener(this);
        descriptionListPanel1.addChangeListener(this);
        titleListPanel1.addChangeListener(this);
        statusListPanel1.addChangeListener(this);
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
        editIdButton = new javax.swing.JButton();
        versionCaption = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        versionEditButton = new javax.swing.JButton();
        descriptionListPanel1 = new com.g2inc.scap.editor.gui.windows.xccdf.DescriptionListPanel();
        titleListPanel1 = new com.g2inc.scap.editor.gui.windows.xccdf.TitleListPanel();
        statusListPanel1 = new com.g2inc.scap.editor.gui.windows.xccdf.StatusListPanel();

        setLayout(new java.awt.GridBagLayout());

        idCaption.setText("Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 4, 0);
        add(idCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 4, 0);
        add(idLabel, gridBagConstraints);

        editIdButton.setText("Edit Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        add(editIdButton, gridBagConstraints);

        versionCaption.setText("Version:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        add(versionCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(versionLabel, gridBagConstraints);

        versionEditButton.setText("Edit Version");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        add(versionEditButton, gridBagConstraints);

        descriptionListPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Descriptions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(descriptionListPanel1, gridBagConstraints);

        titleListPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Titles", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.2;
        add(titleListPanel1, gridBagConstraints);

        statusListPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Status", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        add(statusListPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.g2inc.scap.editor.gui.windows.xccdf.DescriptionListPanel descriptionListPanel1;
    private javax.swing.JButton editIdButton;
    private javax.swing.JLabel idCaption;
    private javax.swing.JLabel idLabel;
    private com.g2inc.scap.editor.gui.windows.xccdf.StatusListPanel statusListPanel1;
    private com.g2inc.scap.editor.gui.windows.xccdf.TitleListPanel titleListPanel1;
    private javax.swing.JLabel versionCaption;
    private javax.swing.JButton versionEditButton;
    private javax.swing.JLabel versionLabel;
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

    public ItemBasicType getDoc()
    {
	log.trace("getDoc called for Item " + item.toString());
        return item;
    }

    public void setDoc(ItemBasicType item)
    {
        this.item = item;
        idLabel.setText(item.getId());
        Version version = item.getVersion();
        if (version != null)
        {
            versionLabel.setText(version.toString());
        }
        descriptionListPanel1.setDoc(item.getDescriptionList());
        titleListPanel1.setDoc(item.getTitleList());
        statusListPanel1.setDoc(item.getStatusList());
    }

    public void setTabs(JTabbedPane detailTabPane)
    {
        detailTabPane.addTab("General", this);
        SelectableItemDetailTab selItemTab = new SelectableItemDetailTab();
        detailTabPane.addTab("References", selItemTab);
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
    public void stateChanged(ChangeEvent ce)
    {
        Object eventSource = ce.getSource();
        if (eventSource == titleListPanel1)
        {
            item.setTitleList(titleListPanel1.getDoc());
            notifyRegisteredListeners();
        }
        else if (eventSource == descriptionListPanel1)
        {
            item.setDescriptionList(descriptionListPanel1.getDoc());
            notifyRegisteredListeners();
        }
        else if (eventSource == statusListPanel1)
        {
            item.setStatusList(statusListPanel1.getDoc());
            notifyRegisteredListeners();
        }
    }
}
