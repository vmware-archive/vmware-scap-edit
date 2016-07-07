package com.g2inc.scap.editor.gui.dialogs.editors.cpe.item.check;
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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.library.domain.cpe.CPEItemCheck;
import com.g2inc.scap.library.domain.cpe.CPEItemCheckSystemType;

public class CPEItemCheckEditor extends javax.swing.JPanel implements IEditorPage
{
    private EditorDialog parentEditor = null;
    private CPEItemCheck check = null;
    private DocumentListener hrefDocumentlistener = new DocumentListener()
    {
        private void common(DocumentEvent e)
        {
             String t = hrefTextField.getText();

            if (t != null && t.length() > 0)
            {
                check.setHref(t);

                String idTxt = idTextField.getText();

                if (idTxt != null && idTxt.length() > 0)
                {
                    parentEditor.enableOkButton();
                } else
                {
                    parentEditor.disableOkButton();
                }
            } else
            {
                parentEditor.disableOkButton();
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e)
        {
            common(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e)
        {
            common(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e)
        {
            common(e);
        }
    };

    private DocumentListener idDocumentListener = new DocumentListener()
    {
        private void common(DocumentEvent e)
        {
            String t = idTextField.getText();

            if (t != null && t.length() > 0)
            {
                check.setCheckId(t);

                String hrefTxt = hrefTextField.getText();

                if (hrefTxt != null && hrefTxt.length() > 0)
                {
                    parentEditor.enableOkButton();
                } else
                {
                    parentEditor.disableOkButton();
                }
            } else
            {
                parentEditor.disableOkButton();
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e)
        {
            common(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e)
        {
            common(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e)
        {
            common(e);
        }
    };

    private ActionListener comboActionListener = new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            CPEItemCheckSystemType selected = (CPEItemCheckSystemType) systemCombo.getSelectedItem();
            check.setSystem(selected);
        }
    };


    private void initComponents2()
    {
    }

    /** Creates new form RegexDatatypeEditor */
    public CPEItemCheckEditor()
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

        systemCaption = new javax.swing.JLabel();
        systemCombo = new javax.swing.JComboBox();
        hrefCaption = new javax.swing.JLabel();
        hrefTextField = new javax.swing.JTextField();
        idCaption = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        systemCaption.setText("System");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 0);
        add(systemCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 0);
        add(systemCombo, gridBagConstraints);

        hrefCaption.setText("Href");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 9, 3, 5);
        add(hrefCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
        add(hrefTextField, gridBagConstraints);

        idCaption.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 0);
        add(idCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
        add(idTextField, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel hrefCaption;
    private javax.swing.JTextField hrefTextField;
    private javax.swing.JLabel idCaption;
    private javax.swing.JTextField idTextField;
    private javax.swing.JLabel systemCaption;
    private javax.swing.JComboBox systemCombo;
    // End of variables declaration//GEN-END:variables

    @Override
    public CPEItemCheck getData()
    {
        return check;
    }

    @Override
    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
        parentEditor.setTitle("Check Editor");
    }

    @Override
    public void setData(Object data)
    {
        if (data == null)
        {
            throw new IllegalStateException("setData(Object data) must be called with a non-null item check");
        }

        check = (CPEItemCheck) data;

        systemCombo.removeAllItems();

        CPEItemCheckSystemType[] systemOptions = CPEItemCheckSystemType.values();

        // fill in possible options
        for(int x = 0; x < systemOptions.length; x++)
        {
            systemCombo.addItem(systemOptions[x]);
        }

        systemCombo.setSelectedItem(check.getSystem());

        // now add listener so we know if user changes it or not
        systemCombo.addActionListener(comboActionListener);

        String hrefText = check.getHref();
        if(hrefText != null)
        {
            hrefTextField.setText(hrefText);
        }
        hrefTextField.getDocument().addDocumentListener(hrefDocumentlistener);

        String idText = check.getCheckId();
        if(idText != null)
        {
            idTextField.setText(idText);
        }
        idTextField.getDocument().addDocumentListener(idDocumentListener);

        hrefTextField.requestFocus();
    }
}
