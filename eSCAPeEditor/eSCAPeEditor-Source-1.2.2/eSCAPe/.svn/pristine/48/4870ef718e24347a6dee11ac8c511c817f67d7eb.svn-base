package com.g2inc.scap.editor.gui.dialogs.editors.xccdf;

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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.resources.HTMLEditorScrubber;
import com.g2inc.scap.editor.gui.windows.common.ChangeNotifierPanel;
import com.g2inc.scap.library.domain.xccdf.FixText;
import com.g2inc.scap.library.domain.xccdf.FixCommon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FixTextEditor extends ChangeNotifierPanel implements IEditorPage, ChangeListener
{

    private FixText fixText;
    private EditorDialog parentEditor = null;
    private HTMLEditorKit htmlKit = null;
    private static final Logger LOG = Logger.getLogger(FixTextEditor.class);

    private void initTextFields()
    {
        htmlKit = new HTMLEditorKit();
        htmlTextEditorPane.setEditorKit(htmlKit);
        Document htmlTextDoc = htmlKit.createDefaultDocument();
        htmlTextEditorPane.setDocument(htmlTextDoc);
        htmlTextEditorPane.setEditable(false);
        plainTextArea.setDocument(new PlainDocument());
        plainTextArea.getDocument().addDocumentListener(new DocumentListener()
        {

            public void common()
            {
                String plainText = plainTextArea.getText();
                htmlTextEditorPane.setText(plainText.replaceAll("xhtml:", ""));
                HTMLEditorScrubber.scrubJEditorPane(htmlTextEditorPane);
            }

            @Override
            public void insertUpdate(DocumentEvent arg0)
            {
                common();
            }

            @Override
            public void removeUpdate(DocumentEvent arg0)
            {
                common();
            }

            @Override
            public void changedUpdate(DocumentEvent arg0)
            {
                common();
            }
        });
    }
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        Object src = e.getSource();
        if (src == idStringField)
        {
            if (idStringField.getValue() == null || idStringField.getValue().trim().equals("")) {
                // id field is optional, its not an error if its missing
                fixText.setFixRef("");
            }
            else if (idStringField.isValidInput())
            {
                fixText.setFixRef(idStringField.getValue());
                idCaption.setForeground(idStringField.getNormalTextColor());
            }
            else
            {
                idCaption.setForeground(idStringField.getErrorTextColor());
            }
            notifyRegisteredListeners();
        } else if (src == fixCommonPanel1) {
            FixCommon fixCommon = fixCommonPanel1.getData();
            fixText.setComplexity(fixCommon.getComplexity());
            fixText.setDisruption(fixCommon.getDisruption());
            fixText.setStrategy(fixCommon.getStrategy());
            fixText.setReboot(fixCommon.getReboot());
            notifyRegisteredListeners();
        }
        
    }

    private void initComponents2()
    {
        initTextFields();
//        fixCommonPanel1.addChangeListener(this);
        langOverridePanel1.addChangeListener(this);
        idStringField.addChangeListener(this);
    }

    /** Creates new form RegexDatatypeEditor */
    public FixTextEditor()
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

        idCaption = new javax.swing.JLabel();
        idStringField = new com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField();
        stringValueCaption = new javax.swing.JLabel();
        htmlCaption = new javax.swing.JLabel();
        plainTextAreaPane = new javax.swing.JScrollPane();
        plainTextArea = new javax.swing.JTextArea();
        htmlTextEditorScrollPane = new javax.swing.JScrollPane();
        htmlTextEditorPane = new javax.swing.JEditorPane();
        langOverridePanel1 = new com.g2inc.scap.editor.gui.windows.xccdf.LangOverridePanel();
        fixCommonPanel1 = new com.g2inc.scap.editor.gui.windows.xccdf.FixCommonPanel();

        idCaption.setText("fix id (optional):");
        idCaption.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        add(idCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        add(idStringField, gridBagConstraints);

        stringValueCaption.setText("String Text:");
        stringValueCaption.setAlignmentY(0.0F);
        stringValueCaption.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        add(stringValueCaption, gridBagConstraints);

        htmlCaption.setLabelFor(htmlTextEditorScrollPane);
        htmlCaption.setText("HTML Text:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(htmlCaption, gridBagConstraints);

        plainTextArea.setColumns(20);
        plainTextArea.setRows(5);
        plainTextAreaPane.setViewportView(plainTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 0, 5);
        add(plainTextAreaPane, gridBagConstraints);

        htmlTextEditorScrollPane.setViewportView(htmlTextEditorPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 5, 5);
        add(htmlTextEditorScrollPane, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 5, 5);
        add(langOverridePanel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 5, 5);
        add(fixCommonPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.g2inc.scap.editor.gui.windows.xccdf.FixCommonPanel fixCommonPanel1;
    private javax.swing.JLabel htmlCaption;
    private javax.swing.JEditorPane htmlTextEditorPane;
    private javax.swing.JScrollPane htmlTextEditorScrollPane;
    private javax.swing.JLabel idCaption;
    private com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField idStringField;
    private com.g2inc.scap.editor.gui.windows.xccdf.LangOverridePanel langOverridePanel1;
    private javax.swing.JTextArea plainTextArea;
    private javax.swing.JScrollPane plainTextAreaPane;
    private javax.swing.JLabel stringValueCaption;
    // End of variables declaration//GEN-END:variables

    @Override
    public Object getData()
    {
        FixCommon fixCommon = fixCommonPanel1.getData();
        String tagName = fixText.getElement().getName();
        fixText.setElementFromStringWithHtml(tagName, plainTextArea.getText());
        fixText.setLang(langOverridePanel1.getData().getLang());

        fixText.setComplexity(fixCommon.getComplexity());
        fixText.setDisruption(fixCommon.getDisruption());
        fixText.setStrategy(fixCommon.getStrategy());
        fixText.setReboot(fixCommon.getReboot());
        
        return fixText;
    }

    @Override
    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
    }

    @Override
    public void setData(Object data)
    {
        fixText = (FixText) data;
        langOverridePanel1.setData(fixText);
        fixCommonPanel1.setData(fixText);
        idStringField.setValue(fixText.getFixRef());
        String string = fixText.toStringWithHtml();
        if (data == null) {
            plainTextArea.setText("");
        } else {
            plainTextArea.setText(string);
        }
        plainTextArea.requestFocus();
    }
}
