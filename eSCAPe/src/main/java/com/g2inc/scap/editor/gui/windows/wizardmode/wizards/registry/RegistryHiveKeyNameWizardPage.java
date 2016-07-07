package com.g2inc.scap.editor.gui.windows.wizardmode.wizards.registry;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.patternmatch.RegexDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.DocumentListenerAdaptor;
import com.g2inc.scap.editor.gui.wizards.WizardPage;
import com.g2inc.scap.library.domain.oval.OperEnum;
import com.g2inc.scap.library.domain.oval.OvalDatatype;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.TypeEnum;
import com.g2inc.scap.library.schema.NameDoc;

public class RegistryHiveKeyNameWizardPage extends WizardPage implements ChangeListener
{
    private static final long serialVersionUID = 1L;
    private static final String[] WINDOWS_ANY_DATATYPES = { "string", "int", "float", "binary", "boolean", "version"  };
    private String hive = null;
    private String key = null;
    private String registryName = null;
    private String value = null;
    private boolean isKeyRegex = false;
    private boolean isNameRegex = false;
    private String title;
    private WhatToTest whatToTest = null;
    private String operation = "equals";
    private String datatype = "string";
    private DocumentListenerAdaptor documentListener = null;
    private OvalDefinitionsDocument ovalDoc = null;

    private void initComboBoxes()
    {
        hiveCombo.removeAllItems();
        List<NameDoc> hiveNameDocs = ovalDoc.getEnumerationValues("EntityObjectRegistryHiveType");
        for (NameDoc nameDoc : hiveNameDocs)
        {
        	String name = nameDoc.getName();
        	
        	if(name != null && name.equals(""))
        	{
        		continue;
        	}
        	
        	hiveCombo.addItem(nameDoc.getName());
        }
        
        hiveCombo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                hive = (String) hiveCombo.getSelectedItem();
                setSatisfied(isPageComplete());
            }
        });

        datatypeCombo.removeAllItems();
        for (String type : WINDOWS_ANY_DATATYPES )
        {
            datatypeCombo.addItem(type);
        }
		
        enumCombo.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                value = (String) enumCombo.getSelectedItem();
            }
        });

        operationCombo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                operation = (String) operationCombo.getSelectedItem();
				valueEditButton.setEnabled(operation != null && operation.equals("pattern match"));
            }
        });

        datatypeCombo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                datatype = (String) datatypeCombo.getSelectedItem();
                TypeEnum typeEnum = TypeEnum.valueOf(datatype.toUpperCase());
                OvalDatatype ovalDatatype = new OvalDatatype(typeEnum);
                OperEnum[] validOperations = ovalDoc.getOperationsForDatatype(ovalDatatype);
                operationCombo.removeAllItems();
                for (OperEnum operEnum : validOperations)
                {
                    operationCombo.addItem(operEnum.toString());
                }
                valueField.setValue("");
                valueField.setForeground(valueField.getNormalTextColor());
                setValuePattern(ovalDatatype);
                if (parentWizard != null)
                {
                    setSatisfied(isPageComplete());
                }
            }
        });

        hiveCombo.setSelectedItem("HKEY_LOCAL_MACHINE");
        datatypeCombo.setSelectedItem("string");
    }

    private void setValuePattern(OvalDatatype ovalDatatype)
    {
	if (ovalDatatype.getType() == TypeEnum.BOOLEAN)
        {
            valueField.setVisible(false);
            enumCombo.setVisible(true);
            enumCombo.removeAllItems();
            enumCombo.addItem("true");
            enumCombo.addItem("false");
		} else {
			valueField.setVisible(true);
			enumCombo.setVisible(false);
			switch (ovalDatatype.getType()) {
				case INT:
					valueField.setPattern(Pattern.compile("^\\d+$"));
					break;
				case FLOAT:
					valueField.setPattern(Pattern.compile("^[0-9]+\\.[0-9]+$"));
					break;
				case BINARY:
					valueField.setPattern(Pattern.compile("^([A-Fa-f0-9]{2})+$"));
					break;
				case VERSION:
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

						valueField.setPatterns(patterns);
					}
					catch(Exception e)
					{
						// shouldn't fail, this is a known good regex
						throw new IllegalArgumentException("Exception", e);
					}
					break;
				case STRING:
				default:
					valueField.resetPattern();
					break;   
			}
			valueField.validate();
		}
	}

    private void initCheckBoxes()
    {
        keyPatternCheckBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e) {
                isKeyRegex =(e.getStateChange() == ItemEvent.SELECTED);
				keyEditButton.setEnabled(isKeyRegex);
            }
        });
        namePatternCheckBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                isNameRegex =(e.getStateChange() == ItemEvent.SELECTED);
				nameEditButton.setEnabled(isNameRegex);
            }
        });
    }

    private void initRadioButtons()
    {
        whatToTest = WhatToTest.VALUE;  // default
        ActionListener radioButtonListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Object button = e.getSource();
                if (button != null )
                {
                    JRadioButton radio = (JRadioButton) button;
                    String name = radio.getName();

                    if(name != null)
                    {
                        if(name.equals(keyExistButton.getName()))
                        {
                            whatToTest = WhatToTest.HIVE_KEY_EXISTS;
							setControls(namePanel, false);
							setControls(valuePanel, false);
                        }
                        else if(name.equals(keyNotExistButton.getName()))
                        {
                            whatToTest = WhatToTest.HIVE_KEY_NOT_EXIST;
							setControls(namePanel, false);
							setControls(valuePanel, false);
                        }
                        else if(name.equals(keyNameExistButton.getName()))
                        {
                            whatToTest = WhatToTest.HIVE_KEY_NAME_EXISTS;
							setControls(namePanel, true);
							setControls(valuePanel, false);
                        }
                        else if(name.equals(keyNameNotExistButton.getName()))
                        {
                            whatToTest = WhatToTest.HIVE_KEY_NAME_NOT_EXIST;
							setControls(namePanel, true);
							setControls(valuePanel, false);
                        } else if(name.equals(valueTestButton.getName()))
                        {
                            whatToTest = WhatToTest.VALUE;
							setControls(namePanel, true);
							setControls(valuePanel, true);
                            valuePanel.validate();  // for some reason, when I don't validate, the combo boxes don't reappear
                        } else
                        {
                            throw new IllegalStateException("ActionListener could not determine selected button");
                        }
                    }
                }
                setSatisfied(isPageComplete());
            }
        };
        keyExistButton.addActionListener(radioButtonListener);
        keyNotExistButton.addActionListener(radioButtonListener);
        keyNameExistButton.addActionListener(radioButtonListener);
        keyNameNotExistButton.addActionListener(radioButtonListener);
        valueTestButton.addActionListener(radioButtonListener);
    }

	private void initButtons()
	{

        keyEditButton.addActionListener(new ActionListener()
        {
            final JFrame parentRef = EditorMainWindow.getInstance();
            @Override
            public void actionPerformed(ActionEvent e)
            {
                EditorDialog editor = new EditorDialog(parentRef, true);
                editor.setEditorPage(new RegexDatatypeEditor());

                String existing =  keyTextField.getText();
                editor.setData(existing);

                editor.pack();
                editor.setLocationRelativeTo(parentRef);
                editor.validate();
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    String updated = (String) editor.getData();
                    if(!existing.equals(updated))
                    {
						keyTextField.setText(updated);
                    }
                }
            }
        });
        nameEditButton.addActionListener(new ActionListener()
        {
            final JFrame parentRef = EditorMainWindow.getInstance();
            @Override
            public void actionPerformed(ActionEvent e)
            {
                EditorDialog editor = new EditorDialog(parentRef, true);
                editor.setEditorPage(new RegexDatatypeEditor());

                String existing =  nameTextField.getText();
                editor.setData(existing);

                editor.pack();
                editor.setLocationRelativeTo(parentRef);
                editor.validate();
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    String updated = (String) editor.getData();
                    if(!existing.equals(updated))
                    {
						nameTextField.setText(updated);
                    }
                }
            }
        });
        valueEditButton.addActionListener(new ActionListener()
        {
            final JFrame parentRef = EditorMainWindow.getInstance();
            @Override
            public void actionPerformed(ActionEvent e)
            {
                EditorDialog editor = new EditorDialog(parentRef, true);
                editor.setEditorPage(new RegexDatatypeEditor());

                String existing =  valueField.getValue();
                editor.setData(existing);

                editor.pack();
                editor.setLocationRelativeTo(parentRef);
                editor.validate();
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    String updated = (String) editor.getData();
                    if(!existing.equals(updated))
                    {
						valueField.setValue(updated);
                    }
                }
            }
        });
	}

	private void setControls(JPanel panel, boolean enabled)
	{
		Color disabledColor = nameTextField.getDisabledTextColor();
		Color enabledColor = nameTextField.getForeground();
		keyEditButton.setEnabled(isKeyRegex);
		if (panel == namePanel)
		{
			namePanel.setEnabled(enabled);
			TitledBorder nameBorder = (TitledBorder) namePanel.getBorder();
			nameBorder.setTitleColor(enabled ? enabledColor : disabledColor);
			nameTextField.setEnabled(enabled);
			namePatternCheckBox.setEnabled(enabled);
			nameEditButton.setEnabled(enabled && isNameRegex);
		}
		else if (panel == valuePanel)
		{
			valuePanel.setEnabled(enabled);
			TitledBorder valueBorder = (TitledBorder) valuePanel.getBorder();
			valueBorder.setTitleColor(enabled ? enabledColor : disabledColor);
			valueField.setEnabled(enabled);
			enumCombo.setEnabled(enabled);
			valueEditButton.setEnabled(enabled && 
					(operation != null && operation.equals("pattern match") ) );
			operationCaption.setEnabled(enabled);
			operationCombo.setEnabled(enabled);
			datatypeCombo.setEnabled(enabled);
			datatypeCaption.setEnabled(enabled);
			valueFieldPanel.setEnabled(enabled);
			valueField.stringValueTextField.setEnabled(enabled);
		}
	}

    private boolean isPageComplete()
    {
        boolean complete = false;
        if (whatToTest != null) {
                switch (whatToTest)
                {
                        case HIVE_KEY_EXISTS:
                        case HIVE_KEY_NOT_EXIST:
                            complete = (
                            		(hive != null && !hive.equals("")) && 
                            		(key != null && !key.equals(""))); 
                                break;
                        case HIVE_KEY_NAME_EXISTS:
                        case HIVE_KEY_NAME_NOT_EXIST:
                            complete = (
                            		(hive != null && !hive.equals("")) && 
                            		(key != null && !key.equals("")) &&
                            		(registryName != null && !registryName.equals("")));
                                break;
                        case VALUE:
                        	if (valueField.isVisible())
                        	{
                                complete = (
                                		(hive != null && !hive.equals("")) &&
                                		(key != null && !key.equals("")) &&
                                		(registryName != null && !registryName.equals("")) &&
                                		(value != null && !value.equals("") && valueField.isValidInput()));
                        	}
                        	else
                        	{
                                complete = (
                                		(hive != null && !hive.equals("")) &&
                                		(key != null && !key.equals("")) &&
                                		(registryName != null && !registryName.equals("")) &&
                                		(value != null && !value.equals("")));
                        	}
                                break;
                }
        }
        if (complete)
        {
                parentWizard.enableNextButton();
        }
        else
        {
                parentWizard.disableNextButton();
        }
        return complete;
    }

    private void initTextFields()
    {
		if (documentListener == null) {
			documentListener = new TextFieldListener();
		}
		nameTextField.getDocument().addDocumentListener(documentListener);
        keyTextField.getDocument().addDocumentListener(documentListener);
		valueField.addChangeListener(this);
		titleTextField.getDocument().addDocumentListener(documentListener);
    }

    private void initComponents2()
    {
        initComboBoxes();
        initTextFields();
        initCheckBoxes();
        initRadioButtons();
		initButtons();
		setControls(namePanel, true);
		setControls(valuePanel, true);
    }

    /** Creates new form ChoseVariableTypeWizardPage */
    public RegistryHiveKeyNameWizardPage(OvalDefinitionsDocument doc, RegistryWizard wizard)
    {
        ovalDoc = doc;
        parentWizard = wizard;
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

        existenceButtonGroup = new javax.swing.ButtonGroup();
        titleCaption = new javax.swing.JLabel();
        titleTextField = new javax.swing.JTextField();
        whatToTestPanel = new javax.swing.JPanel();
        keyExistButton = new javax.swing.JRadioButton();
        keyNotExistButton = new javax.swing.JRadioButton();
        keyNameExistButton = new javax.swing.JRadioButton();
        keyNameNotExistButton = new javax.swing.JRadioButton();
        valueTestButton = new javax.swing.JRadioButton();
        hivePanel = new javax.swing.JPanel();
        hiveCombo = new javax.swing.JComboBox();
        keyPanel = new javax.swing.JPanel();
        keyTextField = new javax.swing.JTextField();
        keyPatternCheckBox = new javax.swing.JCheckBox();
        keyEditButton = new javax.swing.JButton();
        namePanel = new javax.swing.JPanel();
        nameTextField = new javax.swing.JTextField();
        namePatternCheckBox = new javax.swing.JCheckBox();
        nameEditButton = new javax.swing.JButton();
        valuePanel = new javax.swing.JPanel();
        valueFieldPlusButtonPanel = new javax.swing.JPanel();
        valueFieldPanel = new javax.swing.JPanel();
        valueField = new com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField();
        enumCombo = new javax.swing.JComboBox();
        valueEditButton = new javax.swing.JButton();
        operationCaption = new javax.swing.JLabel();
        operationCombo = new javax.swing.JComboBox();
        datatypeCaption = new javax.swing.JLabel();
        datatypeCombo = new javax.swing.JComboBox();

        setForeground(null);
        setLayout(new java.awt.GridBagLayout());

        titleCaption.setText("Title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(titleCaption, gridBagConstraints);

        titleTextField.setName("TitleTextField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 3, 5);
        add(titleTextField, gridBagConstraints);

        whatToTestPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "What is to be tested", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.SystemColor.windowText));
        whatToTestPanel.setLayout(new java.awt.GridBagLayout());

        existenceButtonGroup.add(keyExistButton);
        keyExistButton.setText("Hive\\Key exists - ignore Name and Value");
        keyExistButton.setName("key_exists_radio"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        whatToTestPanel.add(keyExistButton, gridBagConstraints);

        existenceButtonGroup.add(keyNotExistButton);
        keyNotExistButton.setText("Hive\\Key does NOT exist - ignore Name and Value");
        keyNotExistButton.setName("key_not_exists_radio"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        whatToTestPanel.add(keyNotExistButton, gridBagConstraints);

        existenceButtonGroup.add(keyNameExistButton);
        keyNameExistButton.setText("Hive\\Key\\Name exists  - ignore Value");
        keyNameExistButton.setName("hive_key_name_exists_radio"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        whatToTestPanel.add(keyNameExistButton, gridBagConstraints);

        existenceButtonGroup.add(keyNameNotExistButton);
        keyNameNotExistButton.setText("Hive\\Key\\Name does NOT exist - ignore Value");
        keyNameNotExistButton.setName("hive_key_name_not_exists_radio"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        whatToTestPanel.add(keyNameNotExistButton, gridBagConstraints);

        existenceButtonGroup.add(valueTestButton);
        valueTestButton.setSelected(true);
        valueTestButton.setText("Value of Hive\\Key\\Name ");
        valueTestButton.setName("value_matches_radio"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        whatToTestPanel.add(valueTestButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        add(whatToTestPanel, gridBagConstraints);

        hivePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registry Hive", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.SystemColor.windowText));
        hivePanel.setForeground(null);
        hivePanel.setLayout(new java.awt.GridBagLayout());

        hiveCombo.setName("HiveComboBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 12);
        hivePanel.add(hiveCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.01;
        add(hivePanel, gridBagConstraints);

        keyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registry Key", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.SystemColor.windowText));
        keyPanel.setForeground(null);
        keyPanel.setLayout(new java.awt.GridBagLayout());

        keyTextField.setName("KeyTextField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        keyPanel.add(keyTextField, gridBagConstraints);

        keyPatternCheckBox.setText("Regex");
        keyPatternCheckBox.setName("KeyPatternMatchCheckBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.05;
        keyPanel.add(keyPatternCheckBox, gridBagConstraints);

        keyEditButton.setText("Edit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        keyPanel.add(keyEditButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        add(keyPanel, gridBagConstraints);

        namePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registry Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.SystemColor.windowText));
        namePanel.setForeground(null);
        namePanel.setLayout(new java.awt.GridBagLayout());

        nameTextField.setName("NameTextField"); // NOI18N
        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        namePanel.add(nameTextField, gridBagConstraints);

        namePatternCheckBox.setText("Regex");
        namePatternCheckBox.setName("NamePatternMatchCheckBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.05;
        namePanel.add(namePatternCheckBox, gridBagConstraints);

        nameEditButton.setText("Edit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        namePanel.add(nameEditButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        add(namePanel, gridBagConstraints);

        valuePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registry Value", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.SystemColor.windowText));
        valuePanel.setForeground(null);
        valuePanel.setLayout(new java.awt.GridBagLayout());

        valueFieldPlusButtonPanel.setLayout(new java.awt.GridBagLayout());

        valueFieldPanel.setLayout(new java.awt.GridBagLayout());

        valueField.setName("ValuePatternedStringField"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        valueFieldPanel.add(valueField, gridBagConstraints);

        enumCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enumComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        valueFieldPanel.add(enumCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.95;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 8, 0);
        valueFieldPlusButtonPanel.add(valueFieldPanel, gridBagConstraints);

        valueEditButton.setText("Edit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, 0);
        valueFieldPlusButtonPanel.add(valueEditButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        valuePanel.add(valueFieldPlusButtonPanel, gridBagConstraints);

        operationCaption.setText("Operation:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 0);
        valuePanel.add(operationCaption, gridBagConstraints);

        operationCombo.setName("OperationComboBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.45;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        valuePanel.add(operationCombo, gridBagConstraints);

        datatypeCaption.setText("Datatype:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 0);
        valuePanel.add(datatypeCaption, gridBagConstraints);

        datatypeCombo.setName("DatatypeComboBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.45;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 3);
        valuePanel.add(datatypeCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        add(valuePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

	private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_nameTextFieldActionPerformed

	private void enumComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enumComboActionPerformed
		// TODO add your handling code here:
}//GEN-LAST:event_enumComboActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel datatypeCaption;
    private javax.swing.JComboBox datatypeCombo;
    private javax.swing.JComboBox enumCombo;
    private javax.swing.ButtonGroup existenceButtonGroup;
    private javax.swing.JComboBox hiveCombo;
    private javax.swing.JPanel hivePanel;
    private javax.swing.JButton keyEditButton;
    private javax.swing.JRadioButton keyExistButton;
    private javax.swing.JRadioButton keyNameExistButton;
    private javax.swing.JRadioButton keyNameNotExistButton;
    private javax.swing.JRadioButton keyNotExistButton;
    private javax.swing.JPanel keyPanel;
    private javax.swing.JCheckBox keyPatternCheckBox;
    private javax.swing.JTextField keyTextField;
    private javax.swing.JButton nameEditButton;
    private javax.swing.JPanel namePanel;
    private javax.swing.JCheckBox namePatternCheckBox;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel operationCaption;
    private javax.swing.JComboBox operationCombo;
    private javax.swing.JLabel titleCaption;
    private javax.swing.JTextField titleTextField;
    private javax.swing.JButton valueEditButton;
    public com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField valueField;
    private javax.swing.JPanel valueFieldPanel;
    private javax.swing.JPanel valueFieldPlusButtonPanel;
    private javax.swing.JPanel valuePanel;
    private javax.swing.JRadioButton valueTestButton;
    private javax.swing.JPanel whatToTestPanel;
    // End of variables declaration//GEN-END:variables

    
    @Override
    public Object getData()
    {
        return null;
    }

    
    @Override
    public void setData(Object data)
    {
//        parentDoc = (OvalDefinitionsDocument) data;
    }

//    
//    public void setWizard(Wizard wizard)
//    {
//        parentWiz = (RegistryWizard) wizard;
//    }

    
    @Override
    public String getPageTitle()
    {
        return "Registry Hive/Key/Name";
    }

    @Override
    public void performFinish()
    {
    }

    public String getHive() {
            return hive;
    }

    public String getRegistryName() {
            return registryName;
    }

    public boolean isKeyRegex() {
            return isKeyRegex;
    }

    public boolean isNameRegex() {
            return isNameRegex;
    }

    public String getKey() {
            return key;
    }

    public String getTitle() {
            return title;
    }

    public String getValue() {
            return value;
    }

    public WhatToTest getWhatToTest() {
            return whatToTest;
    }

    public String getOperation() {
            return operation;
    }

    public String getDatatype() {
            return datatype;
    }
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        Object src = e.getSource();

        if(src == valueField)
        {
			value = valueField.getValue();
			setSatisfied(isPageComplete());
            if(valueField.isValidInput())
            {
                valueField.setForeground(valueField.getNormalTextColor());
            }
            else
            {
                valueField.setForeground(valueField.getErrorTextColor());
            }
        }
    }

    private class TextFieldListener extends DocumentListenerAdaptor
    {
        @Override
        public void changed(DocumentEvent de)
        {
            if (de.getDocument() == keyTextField.getDocument())
            {
                    key = keyTextField.getText();
            }
            else if (de.getDocument() == nameTextField.getDocument())
            {
                    registryName = nameTextField.getText();
            }
            else if (de.getDocument() == titleTextField.getDocument())
            {
                    title = titleTextField.getText();
            }
            setSatisfied(isPageComplete());
        }
    }

    public javax.swing.JTextField getTitleTextField()
    {
            return titleTextField;
    }

    public javax.swing.JComboBox getHiveCombo()
    {
            return hiveCombo;
    }

    public javax.swing.JTextField getKeyTextField()
    {
            return keyTextField;
    }

    public JRadioButton getKeyExistButton()
    {
        return keyExistButton;
    }

    public JRadioButton getKeyNameExistButton()
    {
        return keyNameExistButton;
    }

    public JRadioButton getKeyNameNotExistButton()
    {
        return keyNameNotExistButton;
    }

    public JRadioButton getKeyNotExistButton()
    {
        return keyNotExistButton;
    }

    public JRadioButton getValueTestButton()
    {
        return valueTestButton;
    }

    public JTextField getNameTextField()
    {
        return nameTextField;
    }

    public PatternedStringField getValueField()
    {
        return valueField;
    }

    public JComboBox getDatatypeCombo()
    {
        return datatypeCombo;
    }

    public JComboBox getOperationCombo()
    {
        return operationCombo;
    }

    public JCheckBox getKeyPatternCheckBox()
    {
        return keyPatternCheckBox;
    }

    public JCheckBox getNamePatternCheckBox()
    {
        return namePatternCheckBox;
    }


//    public ReferencesPanel getReferencesComponent()
//    {
//        return refPanel;
//    }
}
