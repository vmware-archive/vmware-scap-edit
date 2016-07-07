package com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.patternmatch;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.Element;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;

/**
 * This class is intended to be the way that data of type "pattern_match" is edited.
 * This provides validation that what the user entered is syntactically correct
 * before allowing the data to be saved into the document.
 *
 * @author ssill2
 */
public class RegexDatatypeEditor extends javax.swing.JPanel implements IEditorPage
{
    private static Logger log = Logger.getLogger(RegexDatatypeEditor.class);

    private EditorDialog parentEditor = null;
    private DocumentListener patternTextDocumentListener = null;

    private String convertPosixToJavaRegex(String posixPattern)
    {
        String ret = posixPattern;

        ret = ret.replaceAll("\\[:alpha:\\]", "\\\\p{Alpha}");
        ret = ret.replaceAll("\\[:word:\\]", "\\\\w");
        ret = ret.replaceAll("\\[:blank:\\]", "\\\\p{Blank}");
        ret = ret.replaceAll("\\[:alnum:\\]", "\\\\p{Alnum}");
        ret = ret.replaceAll("\\[:digit:\\]", "\\\\p{Digit}");
        ret = ret.replaceAll("\\[:punct:\\]", "\\\\p{Punct}");
        ret = ret.replaceAll("\\[:graph:\\]", "\\\\p{Graph}");
        ret = ret.replaceAll("\\[:print:\\]", "\\\\p{Print}");
        ret = ret.replaceAll("\\[:cntrl:\\]", "\\\\p{Cntrl}");
        ret = ret.replaceAll("\\[:xdigit:\\]", "\\\\p{Xdigit}");
        ret = ret.replaceAll("\\[:space:\\]", "\\\\p{Space}");
        ret = ret.replaceAll("\\[:lower:\\]", "\\\\p{Lower}");
        ret = ret.replaceAll("\\[:upper:\\]", "\\\\p{Upper}");

        log.debug("ret is " + ret);
        return ret;
    }

    private void initTextFields()
    {
        patternTextDocumentListener = new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                String text = patternTextField.getText();

                if (text.length() > 0)
                {
                    Pattern p = null;
                    text = convertPosixToJavaRegex(text);
                    try
                    {
                        p = Pattern.compile(text);
                        parentEditor.enableOkButton();
                        statusLabel.setText("Pattern OK");

                        userDefinedTextField.setEnabled(true);

                        if(userDefinedTextField.getText().length() > 0)
                        {
                            matchButton.setEnabled(true);
                        }
                        else
                        {
                            matchButton.setEnabled(false);
                        }
                    }
                    catch (Exception e)
                    {
                        statusLabel.setText("Pattern ERROR: " + e.getMessage());
                        parentEditor.disableOkButton();
                        userDefinedTextField.setEnabled(false);
                        matchButton.setEnabled(false);
                    }
                }
                else
                {
                    statusLabel.setText("Pattern ERROR: pattern can't be zero-length");
                    parentEditor.disableOkButton();
                    userDefinedTextField.setEnabled(false);
                    matchButton.setEnabled(false);
                }
            }

            public void insertUpdate(DocumentEvent de)
            {
                common(de);
            }

            public void removeUpdate(DocumentEvent de)
            {
                common(de);
            }

            public void changedUpdate(DocumentEvent de)
            {
                common(de);
            }
        };

        patternTextField.getDocument().addDocumentListener(patternTextDocumentListener);

        userDefinedTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                String text = patternTextField.getText();

                if (text.length() > 0)
                {
                    matchButton.setEnabled(true);
                }
                else
                {
                    matchButton.setEnabled(false);
                }
                validate();
            }

            public void insertUpdate(DocumentEvent de)
            {
                common(de);
            }

            public void removeUpdate(DocumentEvent de)
            {
                common(de);
            }

            public void changedUpdate(DocumentEvent de)
            {
                common(de);
            }
        });
    }

    private void initMatchButton()
    {
        matchButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                String lineEnding = System.getProperty("line.separator");

                String patternText = convertPosixToJavaRegex(patternTextField.getText());
                Pattern pattern = null;

                String matchText = userDefinedTextField.getText();

                try
                {
                    pattern = Pattern.compile(patternText);
                }
                catch(Exception e)
                {
                    testMatchStatusPane.setText("Pattern ERROR");
                    parentEditor.disableOkButton();
                    testAreaPanel.validate();
                    parentEditor.validate();
                    return;
                }

                Matcher m = pattern.matcher(matchText);

                if(m.lookingAt())
                {
                    StringBuilder statusText = new StringBuilder();
                    int grpCount = m.groupCount();
                    statusText.append("Number of groups matched: " + grpCount + lineEnding);
                    statusText.append("Matched text: " + m.group() + lineEnding);

                    if(grpCount > 1)
                    {
                        statusText.append("text match groups:" + lineEnding);

                        for(int x = 0; x <= grpCount; x++)
                        {
                            statusText.append("  groups[" + x + "]: " + m.group(x) + lineEnding);
                        }
                    }
                    testMatchStatusPane.setText(statusText.toString());
                    parentEditor.validate();
                }
                else
                {
                    testMatchStatusPane.setText("NO MATCH");
                    parentEditor.validate();
                }
            }
        });
    }
    
    private void initComponents2()
    {
        initTextFields();
        initMatchButton();
    }

    /** Creates new form RegexDatatypeEditor */
    public RegexDatatypeEditor()
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

        patternPanel = new javax.swing.JPanel();
        patternCaption = new javax.swing.JLabel();
        patternTextField = new javax.swing.JTextField();
        statusCaption = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        testAreaPanel = new javax.swing.JPanel();
        testBlurbLabel = new javax.swing.JLabel();
        userDefinedTextCaption = new javax.swing.JLabel();
        userDefinedTextField = new javax.swing.JTextField();
        matchButton = new javax.swing.JButton();
        testMatchStatusScroller = new javax.swing.JScrollPane();
        testMatchStatusPane = new javax.swing.JTextPane();

        setLayout(new java.awt.GridBagLayout());

        patternPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Regex Pattern", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        patternPanel.setLayout(new java.awt.GridBagLayout());

        patternCaption.setText("Pattern");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 0, 5);
        patternPanel.add(patternCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        patternPanel.add(patternTextField, gridBagConstraints);

        statusCaption.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 0, 5);
        patternPanel.add(statusCaption, gridBagConstraints);

        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        patternPanel.add(statusLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        add(patternPanel, gridBagConstraints);

        testAreaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Test Area", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        testAreaPanel.setLayout(new java.awt.GridBagLayout());

        testBlurbLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        testBlurbLabel.setText("You can test that your pattern defined above will match text you supply.");
        testBlurbLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        testAreaPanel.add(testBlurbLabel, gridBagConstraints);

        userDefinedTextCaption.setText("Text to match");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 4);
        testAreaPanel.add(userDefinedTextCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        testAreaPanel.add(userDefinedTextField, gridBagConstraints);

        matchButton.setText("Match");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        testAreaPanel.add(matchButton, gridBagConstraints);

        testMatchStatusScroller.setAutoscrolls(true);

        testMatchStatusPane.setEditable(false);
        testMatchStatusPane.setPreferredSize(new java.awt.Dimension(6, 255));
        testMatchStatusScroller.setViewportView(testMatchStatusPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        testAreaPanel.add(testMatchStatusScroller, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        add(testAreaPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton matchButton;
    private javax.swing.JLabel patternCaption;
    private javax.swing.JPanel patternPanel;
    private javax.swing.JTextField patternTextField;
    private javax.swing.JLabel statusCaption;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel testAreaPanel;
    private javax.swing.JLabel testBlurbLabel;
    private javax.swing.JTextPane testMatchStatusPane;
    private javax.swing.JScrollPane testMatchStatusScroller;
    private javax.swing.JLabel userDefinedTextCaption;
    private javax.swing.JTextField userDefinedTextField;
    // End of variables declaration//GEN-END:variables

    /**
     *  This function is used by the caller to get the data the user typed in.
     *  This should always return valid data as the OK button won't be enabled
     *  until the validation routines pass.
     *
     * @return String
     */
    public String getData()
    {
        return patternTextField.getText();
    }

    /**
     * Mainly this routine is called by the EditorDialog this editor is being added to.
     *
     * @param editorDialog
     */
    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
        parentEditor.setTitle("Regex pattern editor");
    }

    /**
     * Mainly this routine is called by the EditorDialog this editor is being added to.
     *
     * @param editorDialog
     */
    public void setData(Object data)
    {
        if(data == null)
        {
            patternTextField.setText("");
        }
        else
        {
            patternTextField.setText((String)data);
        }

        patternTextField.requestFocus();

        patternTextDocumentListener.changedUpdate(new DocumentEvent()
        {
            public int getOffset()
            {
                return 0;
            }

            public int getLength()
            {
                return 0;
            }

            public Document getDocument()
            {
                return patternTextField.getDocument();
            }

            public EventType getType()
            {
                return null;
            }

            public ElementChange getChange(Element arg0)
            {
                return null;
            }
        });
    }
}
