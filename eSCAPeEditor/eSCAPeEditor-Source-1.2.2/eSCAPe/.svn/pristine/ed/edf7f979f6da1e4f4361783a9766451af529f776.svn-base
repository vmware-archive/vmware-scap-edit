package com.g2inc.scap.editor.gui.windows.wizardmode.wizards.file;
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
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.date.UnixDateDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.date.WindowsDateDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.patternmatch.RegexDatatypeEditor;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.oval.OperEnum;
import com.g2inc.scap.library.domain.oval.TypeEnum;
import com.g2inc.scap.library.schema.NameDoc;

public class DataEditorPanel extends javax.swing.JPanel implements IEditorPage, ChangeListener
{
    private Logger LOG = Logger.getLogger(DataEditorPanel.class);

    private EditorDialog parentEditor = null;
    private ListItem item;

    private void initComboBoxes()
    {
        DefaultComboBoxModel operModel = new DefaultComboBoxModel();
        operationCombo.setModel(operModel);

        DefaultComboBoxModel enumModel = new DefaultComboBoxModel();
        enumCombo.setModel(enumModel);

        operationCombo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                OperEnum chosenOp = (OperEnum) operationCombo.getSelectedItem();

                if(item != null)
                {
                    item.operation = chosenOp;
                }

                if(chosenOp.equals(OperEnum.PATTERN_MATCH))
                {
                    regexButton.setEnabled(true);
                }
                else
                {
                    regexButton.setEnabled(false);
                }
            }
        });

        enumCombo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String chosen = (String) enumCombo.getSelectedItem();

                if(item != null)
                {
                    item.value = chosen;
                }
            }
        });
    }

    private void initTextFields()
    {
        dataField.addChangeListener(this);
    }

    private void initButtons()
    {
        regexButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                editor.setEditorPage(new RegexDatatypeEditor());
                editor.setData(dataField.getValue());
                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.validate();
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    dataField.setValue((String) editor.getData());
                }
            }
        });
    }

    private void initDatePicker()
    {
        dateEditor.setDateFormatString("MM/dd/yyyy HH:mm:ss a");
        dateEditor.setDate(new Date(System.currentTimeMillis()));
        dateEditor.addChangeListener(this);
    }

    private void initComponents2()
    {
        initButtons();
        initComboBoxes();
        initTextFields();
        initDatePicker();
    }

    /** Creates new form DataEditorPanel */
    public DataEditorPanel()
    {
        initComponents();
        initComponents2();
    }

    
    public Object getData()
    {
        String ret = null;

        if(dataField.isVisible())
        {
            return dataField.getValue();
        }
        else if(enumCombo.isVisible())
        {
            return enumCombo.getSelectedItem();
        }
        else if(dateEditor.isVisible())
        {
            return dateEditor.getDate();
        }
        return ret;
    }

    
    public void setData(Object data)
    {
        item  = (ListItem) data;

        datatypeLabel.setText(item.datatype.getType().toString());

        if(item.datatype.getType().equals(TypeEnum.ENUMERATED))
        {
            dataField.setVisible(false);
            enumCombo.setVisible(true);
            dateEditor.setVisible(false);

            List<NameDoc> allowedValues = item.ovalEntity.getDatatype().getEnumValues();

            if(allowedValues != null && allowedValues.size() > 0)
            {
                for(int x = 0; x < allowedValues.size(); x++)
                {
                    NameDoc entry = allowedValues.get(x);
                    enumCombo.addItem(entry.getName());
                }
            }
            else
            {
                throw new IllegalStateException("No enum values for enumerated type " + item.ovalEntity.getName());
            }

            if(item.value == null || item.value.equals(""))
            {
                enumCombo.setSelectedIndex(0);
            }
            else
            {
                enumCombo.setSelectedItem(item.value);
            }
        }
        else if(item.datatype.getType().equals(TypeEnum.BOOLEAN))
        {
            dataField.setVisible(false);
            enumCombo.setVisible(true);
            dateEditor.setVisible(false);

            enumCombo.addItem("true");
            enumCombo.addItem("false");

            if(item.value == null || item.value.equals(""))
            {
                enumCombo.setSelectedItem("true");
            }
            else
            {
                enumCombo.setSelectedItem(item.value);
            }
        }
        else if(item.datatype.getType().equals(TypeEnum.INT))
        {
            String documentation = item.ovalEntity.getDocumentation();

            if(documentation != null && documentation.indexOf("in seconds since the last epoch") > -1)
            {
                dataField.setVisible(false);
                enumCombo.setVisible(false);
                dateEditor.setVisible(true);

                dateEditor.setDate(new Date(UnixDateDatatypeEditor.unixEpochToMillies(item.value)));
            }
            else if(documentation != null && documentation.indexOf("The string should represent the FILETIME structure") > -1)
            {
                dataField.setVisible(false);
                enumCombo.setVisible(false);
                dateEditor.setVisible(true);

                dateEditor.setDate(new Date(WindowsDateDatatypeEditor.fileTimeToMillies(item.value)));
            }
            else
            {
                dataField.setVisible(true);
                enumCombo.setVisible(false);
                dateEditor.setVisible(false);
                Pattern p = null;
                String pString = "^\\d+$";
                try
                {
                    p = Pattern.compile(pString);
                    dataField.setPattern(p);
                }
                catch(Exception e)
                {
                    // shouldn't fail, this is a known good regex
                    throw new IllegalArgumentException("Exception", e);
                }
                dataField.setValue(item.value);
            }
        }
        else if(item.datatype.getType().equals(TypeEnum.BINARY))
        {
            dataField.setVisible(true);
            enumCombo.setVisible(false);
            dateEditor.setVisible(false);

            Pattern p = null;
            String pString = "^([A-Fa-f0-9]{2})+$";
            try
            {
                p = Pattern.compile(pString);
                dataField.setPattern(p);
            }
            catch(Exception e)
            {
                // shouldn't fail, this is a known good regex
                throw new IllegalArgumentException("Exception", e);
            }
            dataField.setValue(item.value);
        }
        else if(item.datatype.getType().equals(TypeEnum.FLOAT))
        {
            dataField.setVisible(true);
            enumCombo.setVisible(false);
            dateEditor.setVisible(false);

            Pattern p = null;
            String pString = "^[0-9]+\\.[0-9]+$";
            try
            {
                p = Pattern.compile(pString);
                dataField.setPattern(p);
            }
            catch(Exception e)
            {
                // shouldn't fail, this is a known good regex
                throw new IllegalArgumentException("Exception", e);
            }
            dataField.setValue(item.value);
        }
        else if(item.datatype.getType().equals(TypeEnum.EVR_STRING))
        {
            dataField.setVisible(true);
            enumCombo.setVisible(false);
            dateEditor.setVisible(false);

            Pattern p = null;
            String pString = "^\\w+:[\\w\\.]+-\\w+$";
            try
            {
                p = Pattern.compile(pString);
                dataField.setPattern(p);
            }
            catch(Exception e)
            {
                // shouldn't fail, this is a known good regex
                throw new IllegalArgumentException("Exception", e);
            }
            dataField.setValue(item.value);
        }
        else if(item.datatype.getType().equals(TypeEnum.VERSION))
        {
            dataField.setVisible(true);
            enumCombo.setVisible(false);
            dateEditor.setVisible(false);

            String [] patternStrings = new String []
            {
                "^\\d+[^\\d]\\d+[^\\d]\\d+[^\\d]\\d+$",
                "^\\d+[^\\d]\\d+[^\\d]\\d+$",
                "^\\d+[^\\d]\\d+$",
                "^\\d+$"
            };


            Pattern[] patterns = new Pattern[patternStrings.length];
            try
            {
                for(int x = 0; x < patternStrings.length; x++)
                {
                    String pString = patternStrings[x];
                    patterns[x] = Pattern.compile(pString);
                }

                dataField.setPatterns(patterns);
            }
            catch(Exception e)
            {
                // shouldn't fail, this is a known good regex
                throw new IllegalArgumentException("Exception", e);
            }
            dataField.setValue(item.value);
        }
        else
        {
            String documentation = item.ovalEntity.getDocumentation();

            if(documentation != null && documentation.indexOf("in seconds since the last epoch") > -1)
            {
                dataField.setVisible(false);
                enumCombo.setVisible(false);
                dateEditor.setVisible(true);

                dateEditor.setDate(new Date(UnixDateDatatypeEditor.unixEpochToMillies(item.value)));
            }
            else if(documentation != null && documentation.indexOf("The string should represent the FILETIME structure") > -1)
            {
                dataField.setVisible(false);
                enumCombo.setVisible(false);
                dateEditor.setVisible(true);

                dateEditor.setDate(new Date(WindowsDateDatatypeEditor.fileTimeToMillies(item.value)));
            }
            else if(documentation != null && 
            		(documentation.indexOf("md5 hash") > -1 ||  documentation.indexOf("MD5 hash") > -1 ) )
            {
                dataField.setVisible(true);
                enumCombo.setVisible(false);
                dateEditor.setVisible(false);

                Pattern p = null;
                String pString = "^([A-Fa-f0-9]{2}){16}$";
                try
                {
                    p = Pattern.compile(pString);
                    dataField.setPattern(p);
                    dataField.setValue(item.value);
                }
                catch(Exception e)
                {
                    // shouldn't fail, this is a known good regex
                    throw new IllegalArgumentException("Exception", e);
                }
            }
            else if(documentation != null && 
            		(documentation.indexOf("sha1 hash") > -1 ||  documentation.indexOf("SHA-1 hash") > -1) )
            {
                dataField.setVisible(true);
                enumCombo.setVisible(false);
                dateEditor.setVisible(false);

                Pattern p = null;
                String pString = "^([A-Fa-f0-9]{8}\\s*){5}$";
                try
                {
                    p = Pattern.compile(pString);
                    dataField.setPattern(p);
                    dataField.setValue(item.value);
                }
                catch(Exception e)
                {
                    // shouldn't fail, this is a known good regex
                    throw new IllegalArgumentException("Exception", e);
                }
            }
            else
            {
                dataField.setValue(item.value);
                dataField.setVisible(true);
                enumCombo.setVisible(false);
                dateEditor.setVisible(false);
            }
        }


        // we are assuming setValidOperations was called first
        if(item.operation != null)
        {
            operationCombo.setSelectedItem(item.operation);
        }
        else
        {
            // pick default
            operationCombo.setSelectedItem(OperEnum.EQUALS);
        }
    }

    
    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
    }

    
    public void stateChanged(ChangeEvent e)
    {
        Object src = e.getSource();

        if(src == dataField)
        {
            if(dataField.isValidInput())
            {
                dataCaption.setForeground(dataField.getNormalTextColor());
                if(item != null)
                {
                    item.value = dataField.getValue();
                }

                parentEditor.enableOkButton();
            }
            else
            {
                dataCaption.setForeground(dataField.getErrorTextColor());
                parentEditor.disableOkButton();
            }
       }
        else if(src == dateEditor)
        {
            String documentation = item.ovalEntity.getDocumentation();

            if(documentation != null && documentation.indexOf("in seconds since the last epoch") > -1)
            {
                item.value = UnixDateDatatypeEditor.millisToUnixEpoch(dateEditor.getDate().getTime()) + "";
            }
            else if(documentation != null && documentation.indexOf("The string should represent the FILETIME structure") > -1)
            {
                item.value = WindowsDateDatatypeEditor.millisToFileTime(dateEditor.getDate().getTime()) + "";
            }
        }
    }

    public void setValidOperations(OperEnum[] validOps)
    {
        DefaultComboBoxModel model = (DefaultComboBoxModel) operationCombo.getModel();

        model.removeAllElements();

        boolean foundPatternMatch = false;

        for(int x = 0; x < validOps.length; x++)
        {
            OperEnum op = validOps[x];

            if(op == OperEnum.PATTERN_MATCH)
            {
                foundPatternMatch = true;
            }

            model.addElement(op);
        }

        if(foundPatternMatch)
        {
            regexButton.setVisible(true);
        }
        else
        {
            regexButton.setVisible(false);
        }
    }

    public EditorDialog getParentEditor()
    {
    	return parentEditor;
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

        datatypeCaption = new javax.swing.JLabel();
        datatypeLabel = new javax.swing.JLabel();
        dataCaption = new javax.swing.JLabel();
        dataField = new com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField();
        operationCaption = new javax.swing.JLabel();
        operationCombo = new javax.swing.JComboBox();
        regexButton = new javax.swing.JButton();
        enumCombo = new javax.swing.JComboBox();
        dateEditor = new com.toedter.calendar.JSpinnerDateEditor();

        setLayout(new java.awt.GridBagLayout());

        datatypeCaption.setText("Datatype");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 0);
        add(datatypeCaption, gridBagConstraints);

        datatypeLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 7, 0, 6);
        add(datatypeLabel, gridBagConstraints);

        dataCaption.setText("Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 6);
        add(dataCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 5, 0);
        add(dataField, gridBagConstraints);

        operationCaption.setText("Operation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 6);
        add(operationCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        add(operationCombo, gridBagConstraints);

        regexButton.setText("Regex");
        regexButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(regexButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        add(enumCombo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 5, 0);
        add(dateEditor, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dataCaption;
    private com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField dataField;
    private javax.swing.JLabel datatypeCaption;
    private javax.swing.JLabel datatypeLabel;
    private com.toedter.calendar.JSpinnerDateEditor dateEditor;
    private javax.swing.JComboBox enumCombo;
    private javax.swing.JLabel operationCaption;
    private javax.swing.JComboBox operationCombo;
    private javax.swing.JButton regexButton;
    // End of variables declaration//GEN-END:variables

}
