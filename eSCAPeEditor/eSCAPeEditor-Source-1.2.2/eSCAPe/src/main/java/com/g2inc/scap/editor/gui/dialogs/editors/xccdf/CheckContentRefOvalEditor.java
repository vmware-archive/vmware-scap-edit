package com.g2inc.scap.editor.gui.dialogs.editors.xccdf;
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
import java.io.File;
import java.util.List;

import javax.swing.event.DocumentEvent;

import com.g2inc.scap.editor.gui.choosers.definition.DefinitionChooser;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.DocumentListenerAdaptor;
import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.xccdf.CheckContentRef;

public class CheckContentRefOvalEditor extends javax.swing.JPanel implements IEditorPage
{
	CheckContentRef contentRef = null;
    private EditorDialog parentEditor = null;
	File dir = null;
	List<File> ovalFiles = null;
	File ovalFile = null;
	OvalDefinitionsDocument ovalDoc = null;

	private void initButtons()
    {
        chooseDefinitionButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
				nameTextField.setText(null);
				contentRef.setName("");
                DefinitionChooser chooser = new DefinitionChooser(true);
                chooser.setSource(ovalDoc, null, null);
                chooser.setVisible(true);

                if(!chooser.wasCancelled())
                {
                    OvalDefinition ovalDef = (OvalDefinition) chooser.getChosen();
                    nameTextField.setText(ovalDef.getId());
					contentRef.setName(nameTextField.getText());
                }
            }
        });
    }

	private void initComboListener() {
		chooseDefinitionButton.setEnabled(false);
		hrefCombo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String fileName = (String) hrefCombo.getSelectedItem();
				if (fileName != null)
				{
					ovalFile = new File(dir, fileName);
					if (ovalFile.exists())
					{
						ovalDoc = getOvalDoc(ovalFile);
						if (ovalDoc != null) {
							contentRef.setHref(fileName);
							chooseDefinitionButton.setEnabled(true);
						}
					}
				}
			}

		});
	}
	
	private OvalDefinitionsDocument getOvalDoc(File file) {
		SCAPContentManager scm = SCAPContentManager.getInstance();
		OvalDefinitionsDocument ovalDocument = (OvalDefinitionsDocument) scm.getDocument(file.getAbsolutePath());
		if (ovalDocument == null)
		{
			try
			{
				scm = SCAPContentManager.getInstance(ovalFile);
				ovalDocument = (OvalDefinitionsDocument) scm.getDocument(file.getAbsolutePath());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return ovalDocument;
	}

	private void initOvalFiles() 
	{
		SCAPDocument benchmark =  EditorMainWindow.getInstance().getActiveEditorForm().getDocument();
//		SCAPDocument benchmark = contentRef.getSCAPDocument();
		File xccdfFile = new File(benchmark.getFilename());
		dir = xccdfFile.getParentFile();
		SCAPContentManager scm = SCAPContentManager.getInstance();
		ovalFiles = scm.getOvalFiles(dir);

		hrefCombo.removeAllItems();
		for (int i=0; i<ovalFiles.size(); i++) {
			File file = ovalFiles.get(i);
			hrefCombo.addItem(file.getName());
		}
	}

    private void initFields() {
		nameTextField.getDocument().addDocumentListener(new DocumentListenerAdaptor() {
			@Override
			public void changed(DocumentEvent de)
			{
				contentRef.setName(nameTextField.getText());
			}
		});
    }

    private void initComponents2()
    {
        initFields();
		initButtons();
		initComboListener();
    }

    /** Creates new form RegexDatatypeEditor */
    public CheckContentRefOvalEditor()
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        NameCaption = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        chooseDefinitionButton = new javax.swing.JButton();
        hrefCaption = new javax.swing.JLabel();
        hrefCombo = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        NameCaption.setText("Name (OVAL Def Id):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 5);
        add(NameCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 5, 3);
        add(nameTextField, gridBagConstraints);

        chooseDefinitionButton.setText("Choose OVAL Def");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 5, 5);
        add(chooseDefinitionButton, gridBagConstraints);

        hrefCaption.setText("Href (OVAL file):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 5);
        add(hrefCaption, gridBagConstraints);

        hrefCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hrefComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 7);
        add(hrefCombo, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

	private void hrefComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hrefComboActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_hrefComboActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel NameCaption;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton chooseDefinitionButton;
    private javax.swing.JLabel hrefCaption;
    private javax.swing.JComboBox hrefCombo;
    private javax.swing.JTextField nameTextField;
    // End of variables declaration//GEN-END:variables

    public Object getData()
    {
        return contentRef;
    }

    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
        parentEditor.setTitle("CheckExport Editor");
    }

    public void setData(Object data)
    {
		contentRef = (CheckContentRef) data;
        if (contentRef != null) 
		{
			initOvalFiles();
			String href = contentRef.getHref();
			for (int i=0; i<hrefCombo.getItemCount(); i++) 
			{
				String fileName = (String) hrefCombo.getItemAt(i);
				if (fileName.equals(href)) 
				{
					hrefCombo.setSelectedIndex(i);
				}
			}
			nameTextField.setText(contentRef.getName());
        }
    }

}
