package com.g2inc.scap.editor.gui.windows.wizardmode.wizards;
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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.ovalref.OvalReferenceEditor;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.oval.DefinitionClassEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalReference;

public class ReferencesPanel extends javax.swing.JPanel
{
    private OvalDefinitionsDocument ovalDocument;

    public void setOvalDocument(OvalDefinitionsDocument doc)
    {
       ovalDocument = doc;
       referencesList.setTransferHandler(new ReferencesCopyPasteTransferHandler(referencesList, ovalDocument));
    }

    private void initList()
    {
        referencesList.setModel(new ReferencesListModel());
        referencesList.setCellRenderer(new ReferenceListCellRenderer());
        
        ActionMap map = referencesList.getActionMap();
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());

        referencesList.addListSelectionListener(new ListSelectionListener()
        {
            
            public void valueChanged(ListSelectionEvent e)
            {
                int selectedIndex = referencesList.getSelectedIndex();

                if(selectedIndex > -1)
                {
                    editButton.setEnabled(true);
                    removeButton.setEnabled(true);
                }
                else
                {
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                }
            }
        });
    }

    private void initButtons()
    {
        addButton.addActionListener(new ActionListener()
        {
            
            public void actionPerformed(ActionEvent e)
            {
                OvalDefinition blankDef = ovalDocument.createDefinition(DefinitionClassEnum.PATCH);
                OvalReference or = blankDef.getMetadata().createReference();

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                OvalReferenceEditor page = new OvalReferenceEditor();
                editor.setEditorPage(page);
                page.setData(or);

                editor.validate();
                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    editor.getData(); // force data from gui fields to be written to OvalReference;
                    DefaultListModel model = (DefaultListModel) referencesList.getModel();

                    model.addElement(or);
                }
            }
        });

        editButton.addActionListener(new ActionListener()
        {
            
            public void actionPerformed(ActionEvent e)
            {
                int index = referencesList.getSelectedIndex();
                DefaultListModel model = (DefaultListModel) referencesList.getModel();

                Object selectedObj = model.get(index);
                if(selectedObj != null)
                {
                    OvalReference or = (OvalReference) selectedObj;

                    EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                    OvalReferenceEditor page = new OvalReferenceEditor();
                    editor.setEditorPage(page);
                    page.setData(or);

                    editor.validate();
                    editor.pack();
                    editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                    editor.setVisible(true);

                    if(!editor.wasCancelled())
                    {
                        editor.getData();
                        model.remove(index);
                        model.addElement(selectedObj);
                    }
                }
            }
        });

        removeButton.addActionListener(new ActionListener()
        {
            
            public void actionPerformed(ActionEvent e)
            {
                Object[] selectedObjs = referencesList.getSelectedValues();
                if(selectedObjs != null || selectedObjs.length > 0)
                {
                    DefaultListModel model = (DefaultListModel) referencesList.getModel();

                    for(int x = 0; x < selectedObjs.length; x++)
                    {
                        model.removeElement(selectedObjs[x]);                    
                    }
                }
            }
        });

        toClipboardButton.addActionListener(new ActionListener()
        {
            
            public void actionPerformed(ActionEvent e)
            {
                DefaultListModel dlm = (DefaultListModel) referencesList.getModel();

                if(dlm.size() == 0)
                {
                    return;
                }

                ReferencesCopyPasteTransferHandler thandler = (ReferencesCopyPasteTransferHandler) referencesList.getTransferHandler();
                Transferable xferable = thandler.createTransferable(referencesList);

                Clipboard sysClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                sysClipboard.setContents(xferable, null);
            }
        });

        final ReferencesPanel thisRef = this;

        fromClipboardButton.addActionListener(new ActionListener()
        {
            
            public void actionPerformed(ActionEvent e)
            {
                ReferencesCopyPasteTransferHandler thandler = (ReferencesCopyPasteTransferHandler) referencesList.getTransferHandler();

                Clipboard sysClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                Transferable t = sysClipboard.getContents(thisRef);
                
                TransferHandler.TransferSupport support = new TransferHandler.TransferSupport(thisRef, t);
                
                thandler.importData(support);
            }
        });
    }

    private void initComponents2()
    {
        initList();
        initButtons();
    }

    /** Creates new form ReferencesPanel */
    public ReferencesPanel()
    {
        initComponents();
        initComponents2();
    }

    public List<OvalReference> getAddedReferences()
    {
        DefaultListModel model = (DefaultListModel) referencesList.getModel();
        List<OvalReference> ret = new ArrayList<OvalReference>();

        if(model.getSize() > 0)
        {
            for(int x = 0; x < model.getSize(); x++)
            {
                OvalReference or = (OvalReference) model.get(x);
                ret.add(or);

            }
        }

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

        referencesCaption = new javax.swing.JLabel();
        referencesScrollPane = new javax.swing.JScrollPane();
        referencesList = new javax.swing.JList();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        toClipboardButton = new javax.swing.JButton();
        fromClipboardButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        referencesCaption.setText("References(e.g. CVE-2004-1234)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(referencesCaption, gridBagConstraints);

        referencesList.setDragEnabled(true);
        referencesList.setDropMode(javax.swing.DropMode.INSERT);
        referencesScrollPane.setViewportView(referencesList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 0);
        add(referencesScrollPane, gridBagConstraints);

        addButton.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 4);
        add(addButton, gridBagConstraints);

        editButton.setText("Edit");
        editButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 4);
        add(editButton, gridBagConstraints);

        removeButton.setText("Remove");
        removeButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 4);
        add(removeButton, gridBagConstraints);

        toClipboardButton.setText("To clipboard");
        toClipboardButton.setToolTipText("Copy this list to the clipboard.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 4);
        add(toClipboardButton, gridBagConstraints);

        fromClipboardButton.setText("From clipboard");
        fromClipboardButton.setToolTipText("Get data from clipboard.  Data should be in the form \"Source,RefId,RefUrl\" with one entry per line.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 3, 4);
        add(fromClipboardButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton fromClipboardButton;
    private javax.swing.JLabel referencesCaption;
    private javax.swing.JList referencesList;
    private javax.swing.JScrollPane referencesScrollPane;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton toClipboardButton;
    // End of variables declaration//GEN-END:variables

}
