package com.g2inc.scap.editor.gui.dialogs.editors.oval.object.parameter;
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
import java.util.List;
import java.util.regex.Pattern;

import com.g2inc.scap.editor.gui.choosers.Chooser;
import com.g2inc.scap.editor.gui.choosers.variable.VariableChooser;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.allowedvalues.AllowedValuesEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.binary.BinaryDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.bool.BooleanDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.date.UnixDateDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.date.WindowsDateDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.evrstring.EVRStringDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.numeric.FloatDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.numeric.IntDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.patternmatch.RegexDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.StringDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.version.VersionDatatypeEditor;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.oval.OperEnum;
import com.g2inc.scap.library.domain.oval.OvalEntity;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalObjectParameter;
import com.g2inc.scap.library.domain.oval.OvalVariable;
import com.g2inc.scap.library.domain.oval.TypeEnum;
import com.g2inc.scap.library.schema.NameDoc;

public class ObjectParameterEditor extends javax.swing.JPanel implements IEditorPage
{

    private EditorDialog parentEditor = null;
    private OvalObject obj = null;
    private OvalObjectParameter parm = null;
    private OvalEntity entity = null;

    private void initComboBoxData()
    {
        OperEnum[] operations = obj.getParentDocument().getOperationsForDatatype(entity.getDatatype());

        for (int x = 0; x < operations.length; x++)
        {
            operationCombo.addItem(operations[x]);
        }

        List<NameDoc> checkOptions = obj.getParentDocument().getEnumerationValues("CheckEnumeration");

        for(int x = 0; x < checkOptions.size(); x++)
        {
            NameDoc item = checkOptions.get(x);
            varCheckCombo.addItem(item);
        }
    }

    private void initComboBoxListeners()
    {
    }

    private void enableHereSection(boolean enabled)
    {
        herePanel.setEnabled(enabled);
        hereDataCaption.setEnabled(enabled);
        hereDataTextField.setEnabled(enabled);
        editHereDataButton.setEnabled(enabled);
    }

    private void enableVariableSection(boolean enabled)
    {
        variablePanel.setEnabled(enabled);
        varCheckCaption.setEnabled(enabled);
        varCheckCombo.setEnabled(enabled);
        variableIdCaption.setEnabled(enabled);
        variableIdLabel.setEnabled(enabled);
        chooseVariableButton.setEnabled(enabled);
    }

    private void initButtonListeners()
    {
        dsHereButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                enableHereSection(true);
                enableVariableSection(false);
            }
        });

        dsVariableButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                enableHereSection(false);
                enableVariableSection(true);
            }
        });

        editHereDataButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);

                TypeEnum datatype = entity.getDatatype().getType();
                switch (datatype)
                {
                    case BINARY:
                        BinaryDatatypeEditor binPage = new BinaryDatatypeEditor();
                        editor.setEditorPage(binPage);
                        binPage.setData(parm.getValue());
                        editor.pack();
                        editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                        editor.setVisible(true);

                        if (!editor.wasCancelled())
                        {
                            Object binData = binPage.getData();
                            hereDataTextField.setText(binData.toString());
                        }

                        break;
                    case BOOLEAN:
                        BooleanDatatypeEditor boolPage = new BooleanDatatypeEditor();
                        editor.setEditorPage(boolPage);
                        boolPage.setData(parm.getValue());
                        editor.pack();
                        editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                        editor.setVisible(true);

                        if (!editor.wasCancelled())
                        {
                            Object boolData = boolPage.getData();
                            hereDataTextField.setText(boolData.toString());
                        }

                        break;
                    case ENUMERATED:
                        AllowedValuesEditor avePage = new AllowedValuesEditor();
                        editor.setEditorPage(avePage);
                        avePage.setAllowedValues(entity.getDatatype().getEnumValues());

                        if (parm.getValue() != null)
                        {
                            avePage.setSelectedValue(parm.getValue());
                        }
                        editor.pack();
                        editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                        editor.setVisible(true);

                        if (!editor.wasCancelled())
                        {
                            Object aveData = avePage.getSelectedValue();
                            hereDataTextField.setText(aveData.toString());
                        }
                        break;
                    case EVR_STRING:
                        IEditorPage evrPage = null;

                        if(((OperEnum) operationCombo.getSelectedItem()).equals(OperEnum.PATTERN_MATCH))
                        {
                            evrPage = new RegexDatatypeEditor();
                            editor.setEditorPage(evrPage);
                            evrPage.setData(parm.getValue());
                        }
                        else
                        {
                            evrPage = new EVRStringDatatypeEditor();
                            evrPage.setData(parm.getValue());
                        }

                        editor.setEditorPage(evrPage);
                        editor.pack();
                        editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                        editor.setVisible(true);

                        if (!editor.wasCancelled())
                        {
                            Object evrData = evrPage.getData();
                            hereDataTextField.setText(evrData.toString());
                        }

                        break;
                    case FILESET_REVISION:
                        break;
                    case FLOAT:
                        FloatDatatypeEditor floatPage = new FloatDatatypeEditor();
                        editor.setEditorPage(floatPage);
                        floatPage.setData(parm.getValue());
                        editor.pack();
                        editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                        editor.setVisible(true);

                        if (!editor.wasCancelled())
                        {
                            Object floatData = floatPage.getData();
                            hereDataTextField.setText(floatData.toString());
                        }

                        break;
                    case INT:

                        String intDocumentation = parm.getEntity().getDocumentation();
                        IEditorPage page = null;

                        if(intDocumentation != null && intDocumentation.indexOf("in seconds since the last epoch") > -1)
                        {
                            page = new UnixDateDatatypeEditor();
                        }
                        else if(intDocumentation != null && intDocumentation.indexOf("The string should represent the FILETIME structure") > -1)
                        {
                            page = new WindowsDateDatatypeEditor();
                        }
                        else
                        {
                            page = new IntDatatypeEditor();
                        }

                        editor.setEditorPage(page);
                        page.setData(parm.getValue());
                        editor.pack();
                        editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                        editor.setVisible(true);

                        if (!editor.wasCancelled())
                        {
                            Object intData = page.getData();
                            hereDataTextField.setText(intData.toString());
                        }

                        break;
                    case IOS_VERSION:
                        break;
                    case STRING:
                        IEditorPage stringPage = null;
                        if(((OperEnum) operationCombo.getSelectedItem()).equals(OperEnum.PATTERN_MATCH))
                        {
                            stringPage = new RegexDatatypeEditor();
                        }
                        else
                        {
                            String strDocumentation = parm.getEntity().getDocumentation();
                            if(strDocumentation != null && strDocumentation.indexOf("in seconds since the last epoch") > -1)
                            {
                                stringPage = new UnixDateDatatypeEditor();
                            }
                            else if(strDocumentation != null && strDocumentation.indexOf("The string should represent the FILETIME structure") > -1)
                            {
                                stringPage = new WindowsDateDatatypeEditor();
                            }
                            else if(strDocumentation != null && 
                            		(strDocumentation.indexOf("md5 hash") > -1 || strDocumentation.indexOf("MD5 hash") > -1) )
                            {
                                Pattern p = null;
                                String pString = "^([A-Fa-f0-9]{2}){16}$";
                                try
                                {
                                    p = Pattern.compile(pString);
                                    stringPage = new PatternedStringEditor(editor, p);
                                }
                                catch(Exception e)
                                {
                                    // shouldn't fail, this is a known good regex
                                    throw new IllegalArgumentException("Exception", e);
                                }
                            }
                            else if(strDocumentation != null && 
                            		(strDocumentation.indexOf("sha1 hash") > -1 || strDocumentation.indexOf("SHA-1 hash") > -1) )
                            {
                                Pattern p = null;
                                String pString = "^([A-Fa-f0-9]{8}\\s*){5}$";
                                try
                                {
                                    p = Pattern.compile(pString);
                                    stringPage = new PatternedStringEditor(editor, p);
                                }
                                catch(Exception e)
                                {
                                    // shouldn't fail, this is a known good regex
                                    throw new IllegalArgumentException("Exception", e);
                                }
                            }
                            else
                            {
                                stringPage = new StringDatatypeEditor();
                            }
                        }

                        editor.setEditorPage(stringPage);
                        stringPage.setData(parm.getValue());

                        editor.pack();
                        editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                        editor.setVisible(true);

                        if (!editor.wasCancelled())
                        {
                            Object data = stringPage.getData();
                            hereDataTextField.setText(data.toString());
                        }
                        break;
                    case VERSION:
                        VersionDatatypeEditor versionPage = new VersionDatatypeEditor();
                        editor.setEditorPage(versionPage);
                        versionPage.setData(parm.getValue());
                        editor.pack();
                        editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                        editor.setVisible(true);

                        if (!editor.wasCancelled())
                        {
                            Object versionData = versionPage.getData();
                            hereDataTextField.setText(versionData.toString());
                        }
                        break;
                    default:
                        throw new RuntimeException("Unknown datatype " + datatype);
                }
            }
        });
        dsHereButton.setSelected(true);

        chooseVariableButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                Chooser chooser = new VariableChooser(true);
                chooser.setSource(obj.getParentDocument(), null, null);
                
                chooser.setVisible(true);
                
                if(!chooser.wasCancelled())
                {
                    OvalVariable ov = (OvalVariable) chooser.getChosen();
                    
                    variableIdLabel.setText(ov.getId());
                    variableIdLabel.setToolTipText(ov.getId());
                }
            }
        });
    }

    private void initCheckBoxes()
    {
        nilCheckBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                if(nilCheckBox.isSelected())
                {
                    parm.setNil(true);
                    operationCombo.setEnabled(false);
                    enableHereSection(false);
                    enableVariableSection(false);
                    dsHereButton.setEnabled(false);
                    dsVariableButton.setEnabled(false);
                }
                else
                {
                    parm.setNil(false);
                    parm.setOperation(((OperEnum) operationCombo.getSelectedItem()).toString());
                    parm.setDatatype(dataTypeLabel.getText());
                    
                    operationCombo.setEnabled(true);

                    dsHereButton.setEnabled(true);
                    dsVariableButton.setEnabled(true);

                    if(dsHereButton.isSelected())
                    {
                        enableHereSection(true);
                        enableVariableSection(false);
                    }
                    else
                    {
                        enableHereSection(false);
                        enableVariableSection(true);
                    }
                }
            }
        });
    }

    private void initComponents2()
    {
        parmNameLabel.setText(obj.getElementName());
        dataTypeLabel.setText(entity.getDatatype().getType().name().toLowerCase());

        initButtonListeners();
        initComboBoxData();
        initComboBoxListeners();
        initCheckBoxes();
    }

    /** Creates new form RegexDatatypeEditor */
    public ObjectParameterEditor(OvalObject oo, OvalObjectParameter oop)
    {
        initComponents();

        obj = oo;
        parm = oop;
        entity = oop.getEntity();

        initComponents2();

        String varRef = oop.getVarRef();

        if(varRef != null)
        {
            dsVariableButton.setSelected(true);
            enableHereSection(false);
            enableVariableSection(true);
            variableIdLabel.setText(varRef);
            variableIdLabel.setToolTipText(varRef);
        }
        else
        {
            hereDataTextField.setText(oop.getValue());
        }

        if(oop.getEntity().isNillable())
        {
            nilCheckBox.setEnabled(true);

            if(oop.isNil())
            {
                nilCheckBox.setSelected(true);
                operationCombo.setEnabled(false);
                dsHereButton.setEnabled(false);
                dsVariableButton.setEnabled(false);
                enableVariableSection(false);
                enableHereSection(false);
            }
            else
            {
                nilCheckBox.setSelected(false);
                operationCombo.setEnabled(true);
                dsHereButton.setEnabled(false);
                dsVariableButton.setEnabled(false);

                if(dsHereButton.isSelected())
                {
                    enableHereSection(true);
                    enableVariableSection(false);
                }
                else
                {
                    enableVariableSection(true);
                    enableHereSection(false);
                }
            }

        }
        else
        {
            nilCheckBox.setEnabled(false);
        }
        
        String existingOp = oop.getOperation();

        if(existingOp != null)
        {
            OperEnum[] operations = OperEnum.values();

            for(int x = 0; x < operations.length; x++)
            {
                OperEnum op = operations[x];

                if(op.toString().equals(existingOp))
                {
                    operationCombo.setSelectedItem(op);
                    break;
                }
            }
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

        dataSourceButtonGroup = new javax.swing.ButtonGroup();
        parmCaption = new javax.swing.JLabel();
        parmNameLabel = new javax.swing.JLabel();
        dataPanel = new javax.swing.JPanel();
        datatypeCaption = new javax.swing.JLabel();
        dataTypeLabel = new javax.swing.JLabel();
        operationCaption = new javax.swing.JLabel();
        operationCombo = new javax.swing.JComboBox();
        nilCheckBox = new javax.swing.JCheckBox();
        datasourcePanel = new javax.swing.JPanel();
        dataSourceCaption = new javax.swing.JLabel();
        dsVariableButton = new javax.swing.JRadioButton();
        dsHereButton = new javax.swing.JRadioButton();
        herePanel = new javax.swing.JPanel();
        hereDataCaption = new javax.swing.JLabel();
        hereDataTextField = new javax.swing.JTextField();
        editHereDataButton = new javax.swing.JButton();
        variablePanel = new javax.swing.JPanel();
        varCheckCaption = new javax.swing.JLabel();
        varCheckCombo = new javax.swing.JComboBox();
        variableIdCaption = new javax.swing.JLabel();
        variableIdLabel = new javax.swing.JLabel();
        chooseVariableButton = new javax.swing.JButton();

        setName("State Parameter"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        parmCaption.setText("Parameter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
        add(parmCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(parmNameLabel, gridBagConstraints);

        dataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        dataPanel.setLayout(new java.awt.GridBagLayout());

        datatypeCaption.setText("Data type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        dataPanel.add(datatypeCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        dataPanel.add(dataTypeLabel, gridBagConstraints);

        operationCaption.setText("Operation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 0, 0);
        dataPanel.add(operationCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 10);
        dataPanel.add(operationCombo, gridBagConstraints);

        nilCheckBox.setText("Nil");
        nilCheckBox.setToolTipText("If allowed by the schema, define if this parameter is nil.");
        nilCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        dataPanel.add(nilCheckBox, gridBagConstraints);

        datasourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datasource", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        datasourcePanel.setLayout(new java.awt.GridBagLayout());

        dataSourceCaption.setText("Where is the data for this parameter?");
        dataSourceCaption.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dataSourceCaption.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        dataSourceCaption.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        datasourcePanel.add(dataSourceCaption, gridBagConstraints);

        dataSourceButtonGroup.add(dsVariableButton);
        dsVariableButton.setText("A Variable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        datasourcePanel.add(dsVariableButton, gridBagConstraints);

        dataSourceButtonGroup.add(dsHereButton);
        dsHereButton.setSelected(true);
        dsHereButton.setText("Defined Here");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        datasourcePanel.add(dsHereButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        dataPanel.add(datasourcePanel, gridBagConstraints);

        herePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data defined here", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        herePanel.setLayout(new java.awt.GridBagLayout());

        hereDataCaption.setText("Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        herePanel.add(hereDataCaption, gridBagConstraints);

        hereDataTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 5);
        herePanel.add(hereDataTextField, gridBagConstraints);

        editHereDataButton.setText("Edit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 5);
        herePanel.add(editHereDataButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 7);
        dataPanel.add(herePanel, gridBagConstraints);

        variablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data defined in a variable", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        variablePanel.setEnabled(false);
        variablePanel.setLayout(new java.awt.GridBagLayout());

        varCheckCaption.setText("Variable check");
        varCheckCaption.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 10);
        variablePanel.add(varCheckCaption, gridBagConstraints);

        varCheckCombo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        variablePanel.add(varCheckCombo, gridBagConstraints);

        variableIdCaption.setText("Variable Id");
        variableIdCaption.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 10);
        variablePanel.add(variableIdCaption, gridBagConstraints);

        variableIdLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        variablePanel.add(variableIdLabel, gridBagConstraints);

        chooseVariableButton.setText("Choose Variable");
        chooseVariableButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 5, 5);
        variablePanel.add(chooseVariableButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 7);
        dataPanel.add(variablePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 3);
        add(dataPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseVariableButton;
    private javax.swing.JPanel dataPanel;
    private javax.swing.ButtonGroup dataSourceButtonGroup;
    private javax.swing.JLabel dataSourceCaption;
    private javax.swing.JLabel dataTypeLabel;
    private javax.swing.JPanel datasourcePanel;
    private javax.swing.JLabel datatypeCaption;
    private javax.swing.JRadioButton dsHereButton;
    private javax.swing.JRadioButton dsVariableButton;
    private javax.swing.JButton editHereDataButton;
    private javax.swing.JLabel hereDataCaption;
    private javax.swing.JTextField hereDataTextField;
    private javax.swing.JPanel herePanel;
    private javax.swing.JCheckBox nilCheckBox;
    private javax.swing.JLabel operationCaption;
    private javax.swing.JComboBox operationCombo;
    private javax.swing.JLabel parmCaption;
    private javax.swing.JLabel parmNameLabel;
    private javax.swing.JLabel varCheckCaption;
    private javax.swing.JComboBox varCheckCombo;
    private javax.swing.JLabel variableIdCaption;
    private javax.swing.JLabel variableIdLabel;
    private javax.swing.JPanel variablePanel;
    // End of variables declaration//GEN-END:variables

    public Object getData()
    {
        parm.setOperation(((OperEnum) operationCombo.getSelectedItem()).toString());

        if(parm.isNil())
        {
            parm.setValue("");
            parm.setVarRef(null);
            parm.setVariableReference(null);
            parm.setOperation(null);
            parm.setDatatype(null);
        }
        else
        {
            if(dsHereButton.isSelected())
            {
                parm.setValue(hereDataTextField.getText());
                parm.setVarRef(null);
                parm.setVarCheck(null);
            }
            else
            {
                parm.setValue(null);
                parm.setVarRef(variableIdLabel.getText());
                parm.setVarCheck(varCheckCombo.getSelectedItem().toString());
            }
        }

        return null;
    }

    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
        parentEditor.setTitle("Object parameter - " + parm.getElementName());
    }

    public void setData(Object data)
    {
    }
}
