package com.g2inc.scap.editor.gui.windows.cpe;
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

import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.GenericSourceDetailTab;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.cpe.CPEListGenerator;
import com.g2inc.scap.library.domain.cpe.CPESchemaVersion;

public class CPEDictionaryDetailTab extends javax.swing.JPanel
{
    /** Creates new form DefinitionsDocumentDetailTab */
    private CPEDictionaryDocument doc = null;
    private DocumentListener prodNameListener = null;
    private DocumentListener prodVerListener = null;
    private GenericSourceDetailTab sourceTab = null;
    private boolean generatorCreated = false;
    
    private void addTextFieldListeners()
    {
        prodNameListener = new DocumentListener()
        {
            private void common(DocumentEvent e)
            {
                if(doc != null)
                {
                    doc.getGenerator().setProductName(generatorProdNameText.getText());

                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
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
        generatorProdNameText.getDocument().addDocumentListener(prodNameListener);

        prodVerListener = new DocumentListener()
        {
            private void common(DocumentEvent e)
            {
                if(doc != null)
                {
                    doc.getGenerator().setProductVersion(generatorProdVerText.getText());

                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
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
        generatorProdVerText.getDocument().addDocumentListener(prodVerListener);
    }

    private void initComponents2()
    {
        addTextFieldListeners();
    }

    public CPEDictionaryDetailTab() {
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

        genDetailsPanel = new javax.swing.JPanel();
        filenameCaption = new javax.swing.JLabel();
        filenameLabel = new javax.swing.JLabel();
        ovalDetailsPanel = new javax.swing.JPanel();
        generatorProdNameCaption = new javax.swing.JLabel();
        generatorProdNameText = new javax.swing.JTextField();
        generatorProdVerCaption = new javax.swing.JLabel();
        generatorProdVerText = new javax.swing.JTextField();
        generatorSchemaVersionCaption = new javax.swing.JLabel();
        generatorSchemaVersionLabel = new javax.swing.JLabel();
        generatorTimestampCaption = new javax.swing.JLabel();
        generatorTimestampLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        genDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "General Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        genDetailsPanel.setLayout(new java.awt.GridBagLayout());

        filenameCaption.setText("Filename:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        genDetailsPanel.add(filenameCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        genDetailsPanel.add(filenameLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(genDetailsPanel, gridBagConstraints);

        ovalDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CPE Generator Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        ovalDetailsPanel.setLayout(new java.awt.GridBagLayout());

        generatorProdNameCaption.setText("Product Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        ovalDetailsPanel.add(generatorProdNameCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
        ovalDetailsPanel.add(generatorProdNameText, gridBagConstraints);

        generatorProdVerCaption.setText("Product Version");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        ovalDetailsPanel.add(generatorProdVerCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
        ovalDetailsPanel.add(generatorProdVerText, gridBagConstraints);

        generatorSchemaVersionCaption.setText("CPE Schema Version");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 3, 19);
        ovalDetailsPanel.add(generatorSchemaVersionCaption, gridBagConstraints);

        generatorSchemaVersionLabel.setText("version");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        ovalDetailsPanel.add(generatorSchemaVersionLabel, gridBagConstraints);

        generatorTimestampCaption.setText("Timestamp");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.99;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        ovalDetailsPanel.add(generatorTimestampCaption, gridBagConstraints);

        generatorTimestampLabel.setText("timestamp");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        ovalDetailsPanel.add(generatorTimestampLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        add(ovalDetailsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel filenameCaption;
    private javax.swing.JLabel filenameLabel;
    private javax.swing.JPanel genDetailsPanel;
    private javax.swing.JLabel generatorProdNameCaption;
    private javax.swing.JTextField generatorProdNameText;
    private javax.swing.JLabel generatorProdVerCaption;
    private javax.swing.JTextField generatorProdVerText;
    private javax.swing.JLabel generatorSchemaVersionCaption;
    private javax.swing.JLabel generatorSchemaVersionLabel;
    private javax.swing.JLabel generatorTimestampCaption;
    private javax.swing.JLabel generatorTimestampLabel;
    private javax.swing.JPanel ovalDetailsPanel;
    // End of variables declaration//GEN-END:variables

    public boolean generatorCreated()
    {
    	return generatorCreated;
    }
    
    public CPEDictionaryDocument getDoc()
    {
        return doc;
    }

    public void setDoc(CPEDictionaryDocument doc)
    {
        this.doc = doc;
        filenameLabel.setText(doc.getFilename());

        CPEListGenerator generator = doc.getGenerator();

        if(generator == null)
        {
            generator = doc.createGenerator();

            generator.setProductName("Auto-generated product name");
            generator.setProductVersion("Auto-generated product version");

            String ver = "0.0";
            SCAPDocumentTypeEnum type = getDoc().getDocumentType();
            
            if(type.equals(SCAPDocumentTypeEnum.CPE_20))
            {
                ver = "2.0";
            }
            else if(type.equals(SCAPDocumentTypeEnum.CPE_21))
            {
                ver = "2.1";
            }
            else if(type.equals(SCAPDocumentTypeEnum.CPE_22))
            {
                ver = "2.2";
            }

            generator.setSchemaVersion(new CPESchemaVersion(ver));

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            generator.setTimestamp(c);

            JOptionPane.showMessageDialog(EditorMainWindow.getInstance(), "This dictionary document didn't have a generator element.\n"
                    + "One has been added, but you will need to click \"Save\" or \"Save As\" on the File menu\n"
                    + "for these changes to be reflected in the on-disk version of this document.",
                    "Generator element added", JOptionPane.INFORMATION_MESSAGE);

            doc.setGenerator(generator);

            generatorCreated = true;
        }

        generatorProdNameText.getDocument().removeDocumentListener(prodNameListener);
        generatorProdNameText.setText(generator.getProductName());
        generatorProdNameText.getDocument().addDocumentListener(prodNameListener);

        generatorProdVerText.getDocument().removeDocumentListener(prodVerListener);
        generatorProdVerText.setText(generator.getProductVersion());
        generatorProdVerText.getDocument().addDocumentListener(prodVerListener);

        generatorSchemaVersionLabel.setText(generator.getSchemaVersion().getVersion());
        generatorTimestampLabel.setText(generator.getTimestamp().getTime().toString());
    }

    public GenericSourceDetailTab getSourceTab()
    {
        return sourceTab;
    }

    public void setSourceTab(GenericSourceDetailTab sourceTab)
    {
        this.sourceTab = sourceTab;
    }
}
