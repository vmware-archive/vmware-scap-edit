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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.library.domain.xccdf.Reference;

public class ReferenceEditor extends javax.swing.JPanel implements IEditorPage, ChangeListener, DocumentListener, ItemListener
{
    private EditorDialog parentEditor = null;
    private Reference reference = null;
    private static final Logger LOG = Logger.getLogger(ReferenceEditor.class);

  private void commonDocListener(DocumentEvent de)
    {
        Document doc = de.getDocument();

        if(doc == hrefTextField.getDocument())
        {
            reference.setHref(hrefTextField.getText());
        }
        else if(doc == plainTextArea.getDocument())
        {
            reference.setReference(plainTextArea.getText());
        }
    }

    @Override
    public void insertUpdate(DocumentEvent de)
    {
        commonDocListener(de);
    }

    @Override
    public void removeUpdate(DocumentEvent de)
    {
        commonDocListener(de);
    }

    @Override
    public void changedUpdate(DocumentEvent de)
    {
        commonDocListener(de);
    }

    private void initTextFields()
    {
        hrefTextField.getDocument().addDocumentListener(this);
        plainTextArea.getDocument().addDocumentListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        Object src = e.getSource();
        if(src == overrideCheckBox)
        {
            reference.setOverride(e.getStateChange() == ItemEvent.SELECTED);
        }
    }

    private void initCheckBox()
    {
        overrideCheckBox.addItemListener(this);
    }

    private void initComponents2()
    {
        initTextFields();
        initCheckBox();
    }

    /** Creates new form RegexDatatypeEditor */
    public ReferenceEditor()
    {
        initComponents();
        initComponents2();
    }

    @Override
    public Object getData()
    {
        return reference;
    }

    @Override
    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
        parentEditor.setTitle("Reference Editor");
    }

    @Override
    public void setData(Object data)
    {
        reference = (Reference) data;
        String href = reference.getHref();
        hrefTextField.getDocument().removeDocumentListener(this);
        if (href != null)
        {

            hrefTextField.setText(href.trim().replaceAll("^\\s+", "").replaceAll("\\s+$", ""));
        }

        hrefTextField.getDocument().addDocumentListener(this);

        overrideCheckBox.removeItemListener(this);
        overrideCheckBox.setSelected(reference.isOverride());
        overrideCheckBox.addItemListener(this);
        String referenceString = reference.getReference();

        plainTextArea.getDocument().removeDocumentListener(this);
        plainTextArea.setText(referenceString.trim().replaceAll("^\\s+", "").replaceAll("\\s+$", ""));
        plainTextArea.getDocument().addDocumentListener(this);

        dublinCoreElementsPanel1.setDublinCoreElements(reference.getDublinCoreElements());
        dublinCoreElementsPanel1.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent ce)
    {
        Object src = ce.getSource();

        if(src == dublinCoreElementsPanel1)
        {
            reference.setDublinCoreElements(dublinCoreElementsPanel1.getDublinCoreElements());
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

        hrefLabel = new javax.swing.JLabel();
        hrefTextField = new javax.swing.JTextField();
        overrideCheckBox = new javax.swing.JCheckBox();
        stringValueCaption = new javax.swing.JLabel();
        plainTextAreaPane = new javax.swing.JScrollPane();
        plainTextArea = new javax.swing.JTextArea();
        dcPanel = new javax.swing.JPanel();
        dublinCoreElementsPanel1 = new com.g2inc.scap.editor.gui.windows.common.DublinCoreElementsPanel();

        setLayout(new java.awt.GridBagLayout());

        hrefLabel.setText("Href (optional):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        add(hrefLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 3);
        add(hrefTextField, gridBagConstraints);

        overrideCheckBox.setText("Override?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 5);
        add(overrideCheckBox, gridBagConstraints);

        stringValueCaption.setText("Reference Text (no Dublin Core elements, those should be added below):");
        stringValueCaption.setAlignmentY(0.0F);
        stringValueCaption.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 5, 5);
        add(stringValueCaption, gridBagConstraints);

        plainTextArea.setColumns(20);
        plainTextArea.setRows(5);
        plainTextAreaPane.setViewportView(plainTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 6, 6);
        add(plainTextAreaPane, gridBagConstraints);

        dcPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dublin Core"));
        dcPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        dcPanel.add(dublinCoreElementsPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(dcPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dcPanel;
    private com.g2inc.scap.editor.gui.windows.common.DublinCoreElementsPanel dublinCoreElementsPanel1;
    private javax.swing.JLabel hrefLabel;
    private javax.swing.JTextField hrefTextField;
    private javax.swing.JCheckBox overrideCheckBox;
    private javax.swing.JTextArea plainTextArea;
    private javax.swing.JScrollPane plainTextAreaPane;
    private javax.swing.JLabel stringValueCaption;
    // End of variables declaration//GEN-END:variables

}
