package com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string;
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

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.windows.common.ChangeNotifierPanel;

public class PatternedStringField extends ChangeNotifierPanel
{
    private static final Logger log = Logger.getLogger(PatternedStringField.class);
    private Pattern[] patterns = null;
    public static final String DEFAULT_PATTERN_STRING = "^.*$";

    public static final String MSG_OK = "Ok";
    public static final String MSG_ZERO_LENGTH = "Error: value can't be zero-length";
    public static final String MSG_REGEX = "Error: value entered must match one of the following patterns: ";

    private String status = MSG_OK;

    private Color normalTextColor = null;
    private Color errorTextColor = Color.RED;

    private DocumentListener textFieldDocumentListener = null;

    public String getStatus()
    {
        return status;
    }

    public String getPatternString()
    {
        return patterns[0].pattern();
    }

    /**
     * This is the method that will be called by any change listeners
     * of this class.  This will tell the caller class if the user has entered
     * valid input or not.  The calling class can then decided for example
     * if it should enable the ok button or not.
     *
     * @return boolean
     */
    public boolean isValidInput()
    {
        if(stringValueTextField.getText().length() == 0)
        {
            status = MSG_ZERO_LENGTH;
            stringValueTextField.setToolTipText(status);
        }

        if(MSG_OK.equals(status))
        {
            return true;
        }

        return false;
    }

    private void initTextFieldListeners()
    {
        textFieldDocumentListener = new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                String val = stringValueTextField.getText();

                if(val == null || val.length() == 0)
                {
                    status = MSG_ZERO_LENGTH;
                    stringValueTextField.setForeground(errorTextColor);
                    stringValueTextField.setToolTipText(status);
                }
                else
                {
                    boolean foundMatch = false;

                    for(int x = 0; x < patterns.length ; x++)
                    {
                        Pattern pattern = patterns[x];

                        Matcher m = pattern.matcher(val);

                        if (m.matches())
                        {
                            status = MSG_OK;
                            stringValueTextField.setForeground(normalTextColor);
                            stringValueTextField.setToolTipText(null);
                            foundMatch = true;
                            break;
                        }
                    }

                    if(!foundMatch)
                    {
                        StringBuilder sb = new StringBuilder();

                        // append a list of patterns that need to match
                        for(int x = 0; x < patterns.length; x++)
                        {
                            Pattern p = patterns[x];

                            if( x == patterns.length - 1)
                            {
                                sb.append(p.pattern());
                            }
                            else
                            {
                                sb.append(p.pattern() + ", ");
                            }
                        }

                        status = MSG_REGEX + sb.toString();
                        stringValueTextField.setForeground(errorTextColor);
                        stringValueTextField.setToolTipText(status);
                    }
                }

                notifyRegisteredListeners();
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

        stringValueTextField.getDocument().addDocumentListener(textFieldDocumentListener);
    }

    private void initComponents2()
    {
        normalTextColor = stringValueTextField.getForeground();

        initTextFieldListeners();
    }

    /**
     * Constructor taking the pattern to use
     * to validate the string entered by the user.
     *
     * @param pattern The pattern to be used
     */
    public PatternedStringField(Pattern pattern)
    {
        this.patterns = new Pattern[] { pattern };

        initComponents();
        initComponents2();

        stringValueTextField.requestFocus();
    }

	public void resetPattern() {
		patterns = new Pattern[] {Pattern.compile(DEFAULT_PATTERN_STRING) };
	}

    /** Creates new form RegexDatatypeEditor */
    public PatternedStringField()
    {
        try
        {
            Pattern pattern = Pattern.compile(DEFAULT_PATTERN_STRING);
            patterns = new Pattern[] { pattern };
        }
        catch(Exception e)
        {
            // This should not happen as we are putting a known working pattern
            log.error("This shouldn't happen, but it did!", e);
        }

        initComponents();
        initComponents2();
    }

    /**
     * Get the string the user entered.
     * 
     * @return String
     */
    public String getValue()
    {
        return stringValueTextField.getText();
    }

    /**
     * Set the initial value in the textfield contained in this control.
     *
     * @param existingValue A string
     */
    public void setValue(String existingValue)
    {
        if (existingValue == null)
        {
            stringValueTextField.setText("");
        } else
        {
            stringValueTextField.setText((String) existingValue);
        }
//        stringValueTextField.requestFocus();
        notifyRegisteredListeners();
    }

    @Override
    public void requestFocus()
    {
        stringValueTextField.requestFocus();
    }

    public void setPattern(Pattern pattern)
    {
        this.patterns = new Pattern[] {pattern};
        notifyRegisteredListeners();
    }

    public void setPatterns(Pattern[] patterns)
    {
        this.patterns = patterns;
        notifyRegisteredListeners();
    }

    public Color getNormalTextColor()
    {
        return normalTextColor;
    }

    public Color getErrorTextColor()
    {
        return errorTextColor;
    }

    public void setStringValueTextFieldColor(Color color)
    {
        stringValueTextField.setForeground(color);
    }
    
    public Color getStringValueTextFieldColor()
    {
        return stringValueTextField.getForeground();
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

        stringValueTextField = new javax.swing.JTextField();

        stringValueTextField.setPreferredSize(new java.awt.Dimension(255, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(stringValueTextField, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField stringValueTextField;
    // End of variables declaration//GEN-END:variables
}