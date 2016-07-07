package com.g2inc.scap.editor.gui.wizards.oval.definition;
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
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.ovalref.OvalReferenceEditor;
import com.g2inc.scap.editor.gui.model.table.references.OvalReferencesTableModel;
import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.editor.gui.wizards.WizardPage;
import com.g2inc.scap.library.domain.oval.OvalReference;

public class NewDefReferencesWizardPage extends WizardPage implements ActionListener, ClipboardOwner
{
    private static Logger log = Logger.getLogger(NewDefReferencesWizardPage.class);
    
    private NewDefinitionWizard parentWiz = null;

    
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();

        if(src == addRefButton)
        {
            OvalReference newRef = parentWiz.getDefinition().getMetadata().createReference();

            EditorDialog dtEditor = new EditorDialog(parentWiz.getParentWin(), true);

            OvalReferenceEditor editorPage = new OvalReferenceEditor();
            editorPage.setData(newRef);

            dtEditor.setEditorPage(editorPage);
            dtEditor.pack();
            dtEditor.setVisible(true);

            if (!dtEditor.wasCancelled())
            {
                dtEditor.getData(); // force the fields in the object we passed in to be populated

                String source = newRef.getSource();
                String refid = newRef.getRefId();
                String refurl = newRef.getRefUrl();

                boolean sourceGood = false;
                boolean refIdGood = false;

                if (source != null && source.length() > 0)
                {
                    sourceGood = true;
                }

                if (refid != null && refid.length() > 0)
                {
                    refIdGood = true;
                }

                if (sourceGood && refIdGood)
                {
                    parentWiz.getDefinition().getMetadata().addReference(newRef);

                    ((DefaultTableModel)refTable.getModel()).fireTableDataChanged();
                }
            }
        }
        else if(src == delRefButton)
        {
            int [] rowsSelected = refTable.getSelectedRows();

            List<String> refIds = new ArrayList<String>();

            for(int x = 0; x < rowsSelected.length ; x++)
            {
                String refId = (String) refTable.getModel().getValueAt(x, OvalReferencesTableModel.COLUMN_INDEX_ID);

                refIds.add(refId);
            }

            parentWiz.getDefinition().getMetadata().delReferences(refIds);
            ((DefaultTableModel)refTable.getModel()).fireTableDataChanged();
        }
        else if(src == modRefButton)
        {
            int [] rowsSelected = refTable.getSelectedRows();

            if(rowsSelected == null || rowsSelected.length == 0)
            {
                // this shouldn't happen because the selection listener
                // disables this button in case of no or multiple selections
                return;
            }

            String selectedRefId = null;

            for(int x = 0; x < rowsSelected.length ; x++)
            {
                selectedRefId = (String) refTable.getModel().getValueAt(x, OvalReferencesTableModel.COLUMN_INDEX_ID);
                break;
            }

            List<OvalReference> refs = parentWiz.getDefinition().getMetadata().getReferences();

            OvalReference refToEdit = null;

            for(int x = 0; x < refs.size(); x++)
            {
                OvalReference or = refs.get(x);

                if(or.getRefId().equals(selectedRefId))
                {
                    refToEdit = or;
                }
            }

            if(refToEdit == null)
            {
                return;
            }

            EditorDialog dtEditor = new EditorDialog(parentWiz.getParentWin(), true);

            dtEditor.setData(refToEdit);
            dtEditor.setEditorPage(new OvalReferenceEditor());
            dtEditor.setData(refToEdit);

            dtEditor.pack();

            // record values before edit
            dtEditor.setVisible(true);

            if (!dtEditor.wasCancelled())
            {
                dtEditor.getData(); // force the fields in the object we passed in to be populated

                String source = refToEdit.getSource();
                String refid = refToEdit.getRefId();
                String refurl = refToEdit.getRefUrl();

                boolean sourceGood = false;
                boolean refIdGood = false;

                if (source != null && source.length() > 0)
                {
                    sourceGood = true;
                }

                if (refid != null && refid.length() > 0)
                {
                    refIdGood = true;
                }

                if (sourceGood && refIdGood)
                {
                    ((DefaultTableModel)refTable.getModel()).fireTableDataChanged();
                }
            }
        }
        else if(src == pasteFromClipboardButton)
        {
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = c.getContents(this);

            if(contents != null)
            {
                if(contents.isDataFlavorSupported(DataFlavor.stringFlavor))
                {
                    Object o = null;

                    try
                    {
                        o = contents.getTransferData(DataFlavor.stringFlavor);
                    }
                    catch(Exception e)
                    {
                        log.error("Error getting clipboard contents!");
                        return;
                    }

                    if(o != null)
                    {
                   //     log.debug("o class is " + o.getClass().getName());
                        if(o instanceof String)
                        {
                            String data = (String)o;

                            if(data == null || data.trim().length() == 0)
                            {
                                return;
                            }

                            data = data.trim();
                            String eol = System.getProperty("line.separator");
                            eol = eol.replaceAll("(\r|\r\n)", "\n");

                     //       log.debug("eol is " + eol);

                            String[] lines = data.split("\n");
                            
                            if(lines == null || lines.length == 0)
                            {
                                return;
                            }
                            
                            for(int x = 0; x < lines.length; x++)
                            {
                                String line = lines[x];
                                
                                int commaLoc = line.indexOf(",");
                                
                                if(commaLoc == -1)
                                {
                                    continue;
                                }
                                
                                String id = line.substring(0, commaLoc).trim();
                                
                                int nextCommaLoc = line.indexOf(",", commaLoc + 1);
                                
                                if(nextCommaLoc == -1)
                                {
                                    // missing a second comma, skip
                                    continue;
                                }
                                
                                String source = line.substring(commaLoc + 1, nextCommaLoc).trim();                                
                                String url = line.substring(nextCommaLoc + 1).trim();
                                
                                if(id.length() == 0 || source.length() == 0)
                                {
                                    // the two required items were not supplied,
                                    // skip it
                                    continue;                                    
                                }                                
                               
                                OvalReference newRef = parentWiz.getDefinition().getMetadata().createReference();

                                newRef.setRefId(id);
                                newRef.setSource(source);
                                
                                if(url.length() > 0)
                                {
                                    newRef.setRefUrl(url);
                                }
                                
                                parentWiz.getDefinition().getMetadata().addReference(newRef);

                                ((DefaultTableModel) refTable.getModel()).fireTableDataChanged();
                            }
                        }
                    }
                }
            }
        }
        else if(src == copyToClipboardButton)
        {
            MyTransferHandler mth = (MyTransferHandler) refTable.getTransferHandler();

            Transferable t = mth.createTransferable(refTable);

            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

            c.setContents(t, this);
        }
    }

    
    public void lostOwnership(Clipboard arg0, Transferable arg1)
    {
    }

    private void initButtons()
    {
        addRefButton.addActionListener(this);
        delRefButton.addActionListener(this);
        modRefButton.addActionListener(this);
        pasteFromClipboardButton.addActionListener(this);
        copyToClipboardButton.addActionListener(this);
    }

    private void initTable()
    {
        OvalReferencesTableModel ortm = new OvalReferencesTableModel(parentWiz.getDefinition());
        refTable.setModel(ortm);

        MyTransferHandler transferHandler = new MyTransferHandler();

        refTable.setTransferHandler(transferHandler);

        refTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            
            public void valueChanged(ListSelectionEvent lse)
            {
                int [] selectedRows = refTable.getSelectedRows();

                if(selectedRows != null && selectedRows.length > 0)
                {
                    delRefButton.setEnabled(true);

                    if(selectedRows.length == 1)
                    {
                        // only enable modify for a single selection
                        modRefButton.setEnabled(true);
                    }
                }
                else
                {
                    delRefButton.setEnabled(false);
                    modRefButton.setEnabled(false);
                }
            }
        });
    }
    private void initComponents2()
    {
        initTable();
        initButtons();
    }

    /** Creates new form ChoseVariableTypeWizardPage */
    public NewDefReferencesWizardPage(NewDefinitionWizard wiz)
    {
        initComponents();
        parentWiz = wiz;
        initComponents2();

        setSatisfied(true);
    }

    
    @Override
	public Object getData()
    {
        return null;
    }

    
    @Override
	public void setData(Object data)
    {
    }

    
    @Override
	public void setWizard(Wizard wizard)
    {
        parentWiz = (NewDefinitionWizard) wizard;
    }

    
    @Override
	public String getPageTitle()
    {
        return "References";
    }

    
    @Override
	public void performFinish()
    {
    }

    private class MyTransferHandler extends TransferHandler
    {

        
        @Override
		protected Transferable createTransferable(JComponent c)
        {
            if(c == null)
            {
                return null;
            }

            if(!(c instanceof JTable))
            {
                return null;
            }

            JTable t = (JTable) c;

            StringBuilder sb = new StringBuilder();

            OvalReferencesTableModel tm = (OvalReferencesTableModel) t.getModel();

            String eol = System.getProperty("line.separator");

            int rowcount = tm.getRowCount();

            for(int x = 0; x < rowcount; x++)
            {
                OvalReference or = (OvalReference) tm.getValueAt(x, -1);

                String refId = or.getRefId();
                String refSource = or.getSource();
                String refUrl = or.getRefUrl();

                sb.append(refId + "," + refSource + "," + (refUrl == null ? "" : refUrl) + eol);
            }
            return new StringSelection(sb.toString());
        }

        
        @Override
		protected void exportDone(JComponent arg0, Transferable arg1, int arg2)
        {
        }

        
        @Override
		public void exportToClipboard(JComponent arg0, Clipboard arg1, int arg2) throws IllegalStateException
        {
            super.exportToClipboard(arg0, arg1, arg2);
        }

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

        tablePanel = new javax.swing.JPanel();
        tableScrollPane = new javax.swing.JScrollPane();
        refTable = new javax.swing.JTable();
        tableButtonPanel = new javax.swing.JPanel();
        addRefButton = new javax.swing.JButton();
        delRefButton = new javax.swing.JButton();
        modRefButton = new javax.swing.JButton();
        copyToClipboardButton = new javax.swing.JButton();
        pasteFromClipboardButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        tablePanel.setLayout(new java.awt.GridBagLayout());

        refTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableScrollPane.setViewportView(refTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 10, 0);
        tablePanel.add(tableScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 1.0;
        add(tablePanel, gridBagConstraints);

        tableButtonPanel.setLayout(new java.awt.GridBagLayout());

        addRefButton.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 5);
        tableButtonPanel.add(addRefButton, gridBagConstraints);

        delRefButton.setText("Remove");
        delRefButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        tableButtonPanel.add(delRefButton, gridBagConstraints);

        modRefButton.setText("Edit");
        modRefButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        tableButtonPanel.add(modRefButton, gridBagConstraints);

        copyToClipboardButton.setText("To Clipboard");
        copyToClipboardButton.setToolTipText("Export reference data to clipboard.  Data will be in the form:  id,source,url");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 5);
        tableButtonPanel.add(copyToClipboardButton, gridBagConstraints);

        pasteFromClipboardButton.setText("From Clipboard");
        pasteFromClipboardButton.setToolTipText("Import data from clipboard.  Data should be in the form:  id,source,url   The id and source fields are required.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        tableButtonPanel.add(pasteFromClipboardButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 1.0;
        add(tableButtonPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addRefButton;
    private javax.swing.JButton copyToClipboardButton;
    private javax.swing.JButton delRefButton;
    private javax.swing.JButton modRefButton;
    private javax.swing.JButton pasteFromClipboardButton;
    private javax.swing.JTable refTable;
    private javax.swing.JPanel tableButtonPanel;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration//GEN-END:variables
}
