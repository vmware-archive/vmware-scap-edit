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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.StringDatatypeEditor;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.ChangeNotifierPanel;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

public class BenchmarkAttributesDetailTab extends ChangeNotifierPanel implements ActionListener, ItemListener
{

    private XCCDFBenchmark benchmark = null;
    private JFrame parentWin = null;
    private static Logger log = Logger.getLogger(BenchmarkAttributesDetailTab.class);

    /** Creates new form DefinitionDetailTab */
    public BenchmarkAttributesDetailTab()
    {
        initComponents();
        initButtons();
        initCheckBoxes();
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();

        if(src == editStyleButton)
        {
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            StringDatatypeEditor editorPage = new StringDatatypeEditor();
            editor.setEditorPage(editorPage);
            editor.setData(benchmark.getStyle());
            editor.pack();
            editor.setLocationRelativeTo(null);
            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                String style = (String) editor.getData();
                benchmark.setStyle(style);
                styleTextField.setText(style);
                notifyRegisteredListeners();
            }
        }
        else if (src == editStyleHrefButton)
        {
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            StringDatatypeEditor editorPage = new StringDatatypeEditor();
            editor.setEditorPage(editorPage);
            editor.setData(benchmark.getStyleHref());
            editor.pack();
            editor.setLocationRelativeTo(null);
            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                String value = (String) editor.getData();
                benchmark.setStyleHref(value);
                styleHrefTextField.setText(value);
                notifyRegisteredListeners();
            }
        }
    }
    private void initButtons()
    {
        editStyleButton.addActionListener(this);
        editStyleHrefButton.addActionListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        Object src = e.getSource();

        if(src == resolvedCheckBox)
        {
            benchmark.setResolved(e.getStateChange() == ItemEvent.SELECTED);
            notifyRegisteredListeners();
        }
    }

    private void initCheckBoxes()
    {
        resolvedCheckBox.addItemListener(this);
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
        styleCaption = new javax.swing.JLabel();
        styleTextField = new javax.swing.JTextField();
        editStyleButton = new javax.swing.JButton();
        styleHrefCaption = new javax.swing.JLabel();
        styleHrefTextField = new javax.swing.JTextField();
        editStyleHrefButton = new javax.swing.JButton();
        resolvedCheckBox = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        idCaption.setText("Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 7, 3, 0);
        add(idCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(idLabel, gridBagConstraints);

        styleCaption.setText("style:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 0);
        add(styleCaption, gridBagConstraints);

        styleTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(styleTextField, gridBagConstraints);

        editStyleButton.setText("Edit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 5);
        add(editStyleButton, gridBagConstraints);

        styleHrefCaption.setText("style-href:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 0);
        add(styleHrefCaption, gridBagConstraints);

        styleHrefTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 0, 0);
        add(styleHrefTextField, gridBagConstraints);

        editStyleHrefButton.setText("Edit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 5);
        add(editStyleHrefButton, gridBagConstraints);

        resolvedCheckBox.setText("Resolved");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 0);
        add(resolvedCheckBox, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton editStyleButton;
    private javax.swing.JButton editStyleHrefButton;
    private javax.swing.JLabel idCaption;
    private javax.swing.JLabel idLabel;
    private javax.swing.JCheckBox resolvedCheckBox;
    private javax.swing.JLabel styleCaption;
    private javax.swing.JLabel styleHrefCaption;
    private javax.swing.JTextField styleHrefTextField;
    private javax.swing.JTextField styleTextField;
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

    public void setDoc(XCCDFBenchmark benchmark)
    {
        this.benchmark = benchmark;
        idLabel.setText(benchmark.getId());
        resolvedCheckBox.setSelected(benchmark.isResolved());
        styleHrefTextField.setText(benchmark.getStyleHref());
        styleTextField.setText(benchmark.getStyle());
    }
}
