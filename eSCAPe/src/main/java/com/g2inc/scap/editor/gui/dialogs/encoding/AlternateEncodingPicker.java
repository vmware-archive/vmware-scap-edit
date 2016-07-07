package com.g2inc.scap.editor.gui.dialogs.encoding;

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
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.SortedMap;

/**
 * Dialog to be displayed when an exception occurred because some characters in a document to be loaded
 * don't agree with the declared encoding of the document.  This can happen when an xml document is handed
 * edited and text is pasted in from a program like Microsoft Word whose default encoding is CP1252.
 *
 * The user will be given a choice on which encoding to use to interpret the document.  This would allow those
 * offending characters to be seamlessly converted by java into unicode which is the encoding used to save documents.
 *
 * @author ssill2
 */
public class AlternateEncodingPicker extends javax.swing.JDialog implements ActionListener
{
    private boolean cancelled = true;

    public static final String EXPLANATION = "<HTML>The file you are trying to open contains characters in an" +
        " encoding other than UTF-8.  This can results from editing the document in a text editor that uses" +
        " the system's default encoding. Reading the document in that encoding should allow these characters" +
        " to be read properly and upon save they will be converted to UTF-8</HTML>";

    private void initExplanation()
    {
        explanationLabel.setText(EXPLANATION);
    }

    private void initCombo()
    {
        // list available charsets
        SortedMap<String, Charset> charsetMap = Charset.availableCharsets();
        Charset defaultCharset = null;

        Iterator<String> keyItr = charsetMap.keySet().iterator();

        while(keyItr.hasNext())
        {
            String key = keyItr.next();

            Charset value = charsetMap.get(key);

            if(value.canEncode() && value.isRegistered())
            {
                encodingCombo.addItem(value);

                if(value.displayName().equals("windows-1252"))
                {
                    // we are going to default to 1252
                    defaultCharset = value;
                }
            }
        }

        encodingCombo.setSelectedItem(defaultCharset);
        encodingCombo.validate();
    }

    private void initButtons()
    {
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();
        if(src == okButton)
        {
            cancelled = false;
            setVisible(false);
        }
        else if(src == cancelButton)
        {
            setVisible(false);
        }
    }

    private void initComponents2()
    {
        initExplanation();
        initCombo();
        initButtons();
    }

    /** Creates new form AlternateEncodingPicker */
    public AlternateEncodingPicker(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        initComponents2();
    }

    /**
     * Return value of cancelled.
     *
     * @return boolean
     */
    public boolean wasCancelled()
    {
        return cancelled;
    }

    /**
     * Return the encoding the user chose(or the default).
     * 
     * @return Charset
     */
    public Charset getSelectedEncoding()
    {
        return (Charset) encodingCombo.getSelectedItem();
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

        jPanel1 = new javax.swing.JPanel();
        explanationLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        encodingCaption = new javax.swing.JLabel();
        encodingCombo = new javax.swing.JComboBox();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Alternate Encoding?");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        explanationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        explanationLabel.setText("Replace");
        explanationLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(explanationLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 5);
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        encodingCaption.setText("Pick Encoding");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanel2.add(encodingCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(encodingCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 5);
        getContentPane().add(jPanel2, gridBagConstraints);

        okButton.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        getContentPane().add(okButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        getContentPane().add(cancelButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel encodingCaption;
    private javax.swing.JComboBox encodingCombo;
    private javax.swing.JLabel explanationLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

}
