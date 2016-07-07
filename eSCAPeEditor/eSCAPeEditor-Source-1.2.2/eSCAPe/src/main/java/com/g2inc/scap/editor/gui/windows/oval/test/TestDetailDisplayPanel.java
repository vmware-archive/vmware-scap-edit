package com.g2inc.scap.editor.gui.windows.oval.test;
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
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.cellrenderers.table.oval.test.states.OvalTestStateTableCellRenderer;
import com.g2inc.scap.editor.gui.choosers.Chooser;
import com.g2inc.scap.editor.gui.choosers.object.ObjectChooser;
import com.g2inc.scap.editor.gui.choosers.state.StateChooser;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField;
import com.g2inc.scap.editor.gui.model.table.test.states.OvalTestStatesTableModel;
import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.windows.EditorForm;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.MouseClickListener;
import com.g2inc.scap.editor.gui.windows.common.NavigationButton;
import com.g2inc.scap.editor.gui.windows.common.TabAbstract;
import com.g2inc.scap.editor.gui.windows.oval.OvalEditorForm;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalStateOperatorEnum;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.UnresolvedOvalState;
import com.g2inc.scap.library.schema.NameDoc;

public class TestDetailDisplayPanel extends TabAbstract
{
    private static final Logger log = Logger.getLogger(TestDetailDisplayPanel.class);

    public static final String NO_VALUE_SET_TEXT = "No Value Set";

    private OvalTest ovalTest = null;
    private EditorForm parentEditor = null;
    private boolean modalParent = false;
    private boolean changed = false;

    private DocumentListener commentDocListener = null;
    private ActionListener checkComboActionListener = null;
    private ActionListener checkExistComboActionListener = null;
    private ActionListener stateOperatorActionListener = null;

    private boolean listenersAdded = false;

    private static final String MESSAGE_STATE_PRE_56 = "This version of " + EditorMessages.OVAL
        + " only allows one state to be set per test.";
    private static final String MESSAGE_STATE_56_PLUS = "This version of " + EditorMessages.OVAL
        + " allows multiple states to be set per test.";
    private static final String MESSAGE_STATE_OPERATOR_PRE_56 = "This version of " + EditorMessages.OVAL
        + " does not support this attribute.";

    public void setModalParent(boolean b)
    {
        modalParent = b;
    }
    public boolean isModalParent()
    {
        return modalParent;
    }

    @Override
    public void addChangeListener(ChangeListener cl)
    {
        super.addChangeListener(cl);
        versionEditCtrl.addChangeListener(cl);
        testIdField.addChangeListener(cl);
    }

    @Override
    public void removeChangeListener(ChangeListener cl)
    {
        super.removeChangeListener(cl);
        versionEditCtrl.removeChangeListener(cl);
        testIdField.removeChangeListener(cl);
    }

    public void setParentEditor(EditorForm parentEditor)
    {
        this.parentEditor = parentEditor;
    }
    public EditorForm getParentEditor()
    {
        return parentEditor;
    }

    private void initTextFields()
    {
        if(listenersAdded)
        {
            return;
        }

        commentDocListener = new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent de)
            {
                common(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de)
            {
                common(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de)
            {
                common(de);
            }

            private void common(DocumentEvent de)
            {
                if(ovalTest != null)
                {
                    String comment = testCommentTextField.getText();
                    ovalTest.setComment(comment);
                    changed = true;

                    notifyRegisteredListeners();
                }
            }
        };

        testCommentTextField.getDocument().addDocumentListener(commentDocListener);
        
        testIdField.setPattern(OvalDefinitionsDocument.TEST_ID_PATTERN);
    }

    private void initButtons()
    {
        if(listenersAdded)
        {
            return;
        }

        objectIdChooserButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                String elementName = ovalTest.getElementName();
                String typeFilter = elementName.substring(0, elementName.indexOf("_test"));

                Chooser chooser = new ObjectChooser(true);
                chooser.setSource(ovalTest.getParentDocument(), typeFilter, ovalTest.getPlatform());
                chooser.setLocationRelativeTo(EditorMainWindow.getInstance());
                chooser.setVisible(true);
                if (!chooser.wasCancelled())
                {
                    OvalObject oo = (OvalObject) chooser.getChosen();
                    log.debug("objectIdChooserButton - user chose " + oo.getId());
                    objectIdLabel.setText(oo.getId());
                    ovalTest.setObjectId(oo.getId());
                    changed = true;
                    notifyRegisteredListeners();
                }
            }
        });

        addStateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                String elementName = ovalTest.getElementName();
                log.debug("stateIdChooserButton pressed - ovalTest = " + elementName);
                String typeFilter = elementName.substring(0, elementName.indexOf("_test"));

                Chooser chooser = new StateChooser(true);
                chooser.setSource(ovalTest.getParentDocument(), typeFilter, ovalTest.getPlatform());
                chooser.setLocationRelativeTo(EditorMainWindow.getInstance());

                chooser.setVisible(true);

                if (!chooser.wasCancelled())
                {
                    OvalState os = (OvalState) chooser.getChosen();
                    log.debug("stateIdChooserButton - user chose " + os.getId());

                    OvalTestStatesTableModel tableModel = (OvalTestStatesTableModel) statesTable.getModel();

                    tableModel.addRow(new Object[]{os});
                    
                    ovalTest.setStates(tableModel.getStates());
                    changed = true;
                    notifyRegisteredListeners();

                    int rowCount = tableModel.getRowCount();

                    SCAPDocumentTypeEnum docType = ovalTest.getParentDocument().getDocumentType();

                    if(docType.equals(SCAPDocumentTypeEnum.OVAL_53)
                       || docType.equals(SCAPDocumentTypeEnum.OVAL_54)
                       || docType.equals(SCAPDocumentTypeEnum.OVAL_55))
                    {
                        if(rowCount == 1)
                        {
                            addStateButton.setEnabled(false);
                        }
                        else
                        {
                            addStateButton.setEnabled(true);
                        }

                        stateStatusLabel.setText(MESSAGE_STATE_PRE_56);
                    }
                    else
                    {
                        stateStatusLabel.setText(MESSAGE_STATE_56_PLUS);
                        addStateButton.setEnabled(true);
                    }
                }
            }
        });

        removeStateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                OvalTestStatesTableModel tableModel = (OvalTestStatesTableModel) statesTable.getModel();

                int index = statesTable.getSelectedRow();

                tableModel.removeRow(index);

                changed = true;
                notifyRegisteredListeners();

                int rowCount = tableModel.getRowCount();

                SCAPDocumentTypeEnum docType = ovalTest.getParentDocument().getDocumentType();

                if(docType.equals(SCAPDocumentTypeEnum.OVAL_53)
                   || docType.equals(SCAPDocumentTypeEnum.OVAL_54)
                   || docType.equals(SCAPDocumentTypeEnum.OVAL_55))
                {
                    if(rowCount == 1)
                    {
                        addStateButton.setEnabled(false);
                    }
                    else
                    {
                        addStateButton.setEnabled(true);
                    }

                    stateStatusLabel.setText(MESSAGE_STATE_PRE_56);
                }
                else
                {
                    stateStatusLabel.setText(MESSAGE_STATE_56_PLUS);
                    addStateButton.setEnabled(true);
                }
            }
        });

        stateUpButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                OvalTestStatesTableModel tableModel = (OvalTestStatesTableModel) statesTable.getModel();

                int index = statesTable.getSelectedRow();

                if(index > 0)
                {
                    OvalState os = tableModel.getStates().get(index);
                    tableModel.removeRow(index);

                    tableModel.insertRow(index - 1, new Object[]{os});

                    changed = true;
                    notifyRegisteredListeners();
                }
            }
        });

        stateDownButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                OvalTestStatesTableModel tableModel = (OvalTestStatesTableModel) statesTable.getModel();

                int index = statesTable.getSelectedRow();

                if(index < tableModel.getRowCount() - 1)
                {
                    OvalState os = tableModel.getStates().get(index);
                    tableModel.removeRow(index);

                    tableModel.insertRow(index + 1, new Object[]{os});

                    changed = true;
                    notifyRegisteredListeners();
                }
            }
        });
    }

    private void initLabels()
    {
        if(listenersAdded)
        {
            return;
        }

        objectIdLabel.addMouseListener(new MouseClickListener()
        {
            @Override
            public void mouseClicked(MouseEvent me)
            {
                init(me);
                if (isDoubleClick)
                {
                    if(parentEditor == null || modalParent)
                    {
                        return;
                    }
                    if(ovalTest == null)
                    {
                        return;
                    }
                    
                    OvalObject oo = ovalTest.getObject();

                    if (oo != null)
                    {
                        NavigationButton nb = new NavigationButton();
                        nb.setText("OvalTest");
                        nb.setToolTipText(ovalTest.getId());
                        nb.setSelectedElement(parentEditor.getSelectedPath());
                        nb.setEditorForm(parentEditor);

                        EditorMainWindow.getInstance().getNavPanel().addNavigationButton(nb);

                        // now set selection to the item the user intended
                        ((OvalEditorForm) parentEditor).selectObject(oo);
                    }
                }
            }
        });
    }

    private void initTables()
    {
        if(listenersAdded)
        {
            return;
        }

        statesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                int selectedRowCount = statesTable.getSelectedColumnCount();
                int selectedRow = statesTable.getSelectedRow();

                OvalTestStatesTableModel tableModel = (OvalTestStatesTableModel) statesTable.getModel();

                int totalRowCount = tableModel.getRowCount();

                if(selectedRowCount == 0)
                {
                    removeStateButton.setEnabled(false);
                    stateUpButton.setEnabled(false);
                    stateDownButton.setEnabled(false);
                }
                else
                {
                    removeStateButton.setEnabled(true);

                    if(selectedRowCount == 1)
                    {
                        if(selectedRow > 0)
                        {
                            stateUpButton.setEnabled(true);
                        }
                        else if(selectedRow < (totalRowCount - 1))
                        {
                            stateDownButton.setEnabled(true);
                        }
                        else
                        {
                            stateUpButton.setEnabled(false);
                            stateDownButton.setEnabled(false);
                        }
                    }
                    else
                    {
                        stateUpButton.setEnabled(false);
                        stateDownButton.setEnabled(false);
                    }
                }
            }
        });

        statesTable.addMouseListener(new MouseClickListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                init(e);
                int selectedIndex = statesTable.getSelectedRow();

                if(selectedIndex != -1)
                {
                    if(statesTable.contains(e.getX(), e.getY()))
                    {
                        if(isDoubleClick)
                        {
                            if(parentEditor == null || modalParent)
                            {
                                return;
                            }

                            if(ovalTest == null)
                            {
                                return;
                            }

                            OvalState os = (OvalState) statesTable.getModel().getValueAt(selectedIndex, 0);

                            if(os instanceof UnresolvedOvalState)
                            {
                                return;
                            }

                            if (os != null)
                            {
                                NavigationButton nb = new NavigationButton();
                                nb.setText("OvalTest");
                                nb.setToolTipText(ovalTest.getId());
                                nb.setSelectedElement(parentEditor.getSelectedPath());
                                nb.setEditorForm(parentEditor);

                                EditorMainWindow.getInstance().getNavPanel().addNavigationButton(nb);

                                // now set selection to the item the user intended
                                ((OvalEditorForm) parentEditor).selectState(os);
                            }
                        }
                    }
                }
            }
        });
    }

    private void initCombos()
    {
        if(listenersAdded)
        {
            return;
        }

        stateOperatorCombo.removeAllItems();
        OvalStateOperatorEnum[]  stateOperators = OvalStateOperatorEnum.values();
        for(int x = 0 ; x < stateOperators.length; x++)
        {
            stateOperatorCombo.addItem(stateOperators[x]);
        }
        stateOperatorCombo.validate();
        stateOperatorCombo.setSelectedItem(OvalStateOperatorEnum.AND);
        stateOperatorActionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ovalTest.setStateOperator((OvalStateOperatorEnum) stateOperatorCombo.getSelectedItem());
                changed = true;
                notifyRegisteredListeners();
            }
        };
        stateOperatorCombo.addActionListener(stateOperatorActionListener);

        checkComboBox.removeAllItems();
        OvalDefinitionsDocument ovalDoc = (OvalDefinitionsDocument) parentEditor.getDocument();
        List<NameDoc> checkEnumValues = ovalDoc.getEnumerationValues("CheckEnumeration");
        for (int i=0; i<checkEnumValues.size(); i++) {
            checkComboBox.addItem(checkEnumValues.get(i).getName());
        }
        checkComboBox.validate();

        checkComboActionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                ovalTest.setCheck((String) checkComboBox.getSelectedItem());
                changed = true;
                notifyRegisteredListeners();
            }
        };
        checkComboBox.addActionListener(checkComboActionListener);

        checkExistCombo.removeAllItems();
        List<NameDoc> checkExistEnumValues = ovalDoc.getEnumerationValues("ExistenceEnumeration");
        for (int i=0; i<checkEnumValues.size(); i++) {
            checkExistCombo.addItem(checkExistEnumValues.get(i).getName());
        }
        checkExistCombo.validate();
        
        checkExistComboActionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                ovalTest.setCheckExistence((String) checkExistCombo.getSelectedItem());
                changed = true;
                notifyRegisteredListeners();
            }
        };

        checkExistCombo.addActionListener(checkExistComboActionListener);
    }

    private void initComponents2()
    {
        initLabels();
        initTextFields();
        initButtons();
        initCombos();
        initTables();
        listenersAdded = true;
    }

    /** Creates new form DefinitionDetailTab */
    public TestDetailDisplayPanel()
    {
        initComponents();
    }

    public PatternedStringField getTestIdField()
    {
        return testIdField;
    }

    public JLabel getTestIdCaption()
    {
        return testIdCaption;
    }

    @Override
    public boolean hasChanged()
    {
        return changed;
    }

    @Override
    public void setChanged(boolean b)
    {
        changed = b;
    }

    public boolean allDataOK()
    {
        boolean result = false;
        if (ovalTest != null)
        {
            String comment = ovalTest.getComment();
            if (comment != null && comment.length() > 0)
            {
                result = true;
            }
        }
        return result;
    }

    public OvalTest getOvalTest()
    {
        return ovalTest;
    }

    public void setOvalTest(OvalTest ovalTest)
    {
        parentEditor = EditorMainWindow.getInstance().getActiveEditorForm();

        initComponents2();

        this.ovalTest = ovalTest;
        testIdField.setValue(ovalTest.getId());
        testTypeLabel.setText(ovalTest.getElementName());
        versionEditCtrl.setVersion(ovalTest.getVersion().getVersionString());
        
        testCommentTextField.getDocument().removeDocumentListener(commentDocListener);
        testCommentTextField.setText(ovalTest.getComment());
        testCommentTextField.getDocument().addDocumentListener(commentDocListener);
        
        if(ovalTest.getObjectId() != null)
        {
            objectIdLabel.setText(ovalTest.getObjectId());
            objectIdLabel.setToolTipText("Double-click to view " + objectIdLabel.getText());
        }

        OvalTestStatesTableModel tableModel = new OvalTestStatesTableModel(ovalTest);
        statesTable.setModel(tableModel);
        statesTable.setDefaultRenderer(OvalState.class, new OvalTestStateTableCellRenderer());
        
        int stateRowCount = tableModel.getRowCount();

        SCAPDocumentTypeEnum docType = ovalTest.getParentDocument().getDocumentType();
        if(docType.equals(SCAPDocumentTypeEnum.OVAL_53)
           || docType.equals(SCAPDocumentTypeEnum.OVAL_54)
           || docType.equals(SCAPDocumentTypeEnum.OVAL_55))
        {
            // disable state operator combo
            stateOperatorCombo.setEnabled(false);
            stateOperatorCaption.setEnabled(false);
            stateOperatorCombo.setToolTipText(MESSAGE_STATE_OPERATOR_PRE_56);
            stateOperatorCaption.setToolTipText(MESSAGE_STATE_OPERATOR_PRE_56);

            if(stateRowCount == 1)
            {
                addStateButton.setEnabled(false);
            }
            else
            {
                addStateButton.setEnabled(true);
            }
            stateStatusLabel.setText(MESSAGE_STATE_PRE_56);
        }
        else
        {
            // enable state operator combo and set the current test's value
            stateOperatorCombo.setEnabled(true);
            stateOperatorCaption.setEnabled(true);
            stateOperatorCombo.setToolTipText(null);
            stateOperatorCaption.setToolTipText(null);

            stateOperatorCombo.removeActionListener(stateOperatorActionListener);
            stateOperatorCombo.setSelectedItem(ovalTest.getStateOperator());
            stateOperatorCombo.addActionListener(stateOperatorActionListener);

            stateStatusLabel.setText(MESSAGE_STATE_56_PLUS);
            addStateButton.setEnabled(true);
        }
        
        String defaultCheckSetting = ovalTest.getCheck();
        checkComboBox.removeActionListener(checkComboActionListener);
        checkComboBox.setSelectedItem(defaultCheckSetting);
        checkComboBox.addActionListener(checkComboActionListener);
        ovalTest.setCheck(defaultCheckSetting);

        String defaultCheckExistenceSetting = ovalTest.getCheckExistence();
        checkExistCombo.removeActionListener(checkExistComboActionListener);
        checkExistCombo.setSelectedItem(defaultCheckExistenceSetting);
        checkExistCombo.addActionListener(checkExistComboActionListener);
        ovalTest.setCheckExistence(defaultCheckExistenceSetting);

        if(ovalTest.getElementName().equals("unknown_test"))
        {
            objectIdChooserButton.setEnabled(false);

            addStateButton.setEnabled(false);
        }
        else
        {
            objectIdChooserButton.setEnabled(true);
            addStateButton.setEnabled(true);
        }


        notifyRegisteredListeners();
    }

    public Object getVersion()
    {
        return versionEditCtrl.getVersion();
    }

    public List<OvalState> getStates()
    {
        OvalTestStatesTableModel tableModel = (OvalTestStatesTableModel) statesTable.getModel();

        return tableModel.getStates();
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

        generalPanel = new javax.swing.JPanel();
        testIdCaption = new javax.swing.JLabel();
        testIdField = new com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField();
        testTypeCaption = new javax.swing.JLabel();
        testTypeLabel = new javax.swing.JLabel();
        testCommentCaption = new javax.swing.JLabel();
        testCommentTextField = new javax.swing.JTextField();
        comboPanel = new javax.swing.JPanel();
        stateOperatorCombo = new javax.swing.JComboBox();
        stateOperatorCaption = new javax.swing.JLabel();
        checkCaption = new javax.swing.JLabel();
        checkComboBox = new javax.swing.JComboBox();
        checkExistCaption = new javax.swing.JLabel();
        checkExistCombo = new javax.swing.JComboBox();
        versionEditCtrl = new com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl();
        testDetailPanel = new javax.swing.JPanel();
        objectContainer = new javax.swing.JPanel();
        objectExplanationLabel = new javax.swing.JLabel();
        objectIdCaption = new javax.swing.JLabel();
        objectIdLabel = new javax.swing.JLabel();
        objectIdChooserButton = new javax.swing.JButton();
        stateContainer = new javax.swing.JPanel();
        stateStatusLabel = new javax.swing.JLabel();
        statesTablePanel = new javax.swing.JPanel();
        statesTableScrollPane = new javax.swing.JScrollPane();
        statesTable = new javax.swing.JTable();
        statesTableControls = new javax.swing.JPanel();
        addStateButton = new javax.swing.JButton();
        removeStateButton = new javax.swing.JButton();
        stateUpButton = new javax.swing.JButton();
        stateDownButton = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(584, 529));
        setPreferredSize(new java.awt.Dimension(584, 529));
        setLayout(new java.awt.GridBagLayout());

        generalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "General", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        generalPanel.setLayout(new java.awt.GridBagLayout());

        testIdCaption.setText("Test Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 11, 0, 0);
        generalPanel.add(testIdCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        generalPanel.add(testIdField, gridBagConstraints);

        testTypeCaption.setText("Test Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 0, 10);
        generalPanel.add(testTypeCaption, gridBagConstraints);

        testTypeLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        generalPanel.add(testTypeLabel, gridBagConstraints);

        testCommentCaption.setText("Comment");
        testCommentCaption.setToolTipText("Double-click to edit comment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 11, 0, 0);
        generalPanel.add(testCommentCaption, gridBagConstraints);

        testCommentTextField.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 7);
        generalPanel.add(testCommentTextField, gridBagConstraints);

        comboPanel.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 0);
        comboPanel.add(stateOperatorCombo, gridBagConstraints);

        stateOperatorCaption.setText("State Operator");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 11, 2, 0);
        comboPanel.add(stateOperatorCaption, gridBagConstraints);

        checkCaption.setText("Check");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 11, 0, 0);
        comboPanel.add(checkCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 7);
        comboPanel.add(checkComboBox, gridBagConstraints);

        checkExistCaption.setText("Check Existence");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 11, 0, 3);
        comboPanel.add(checkExistCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 7);
        comboPanel.add(checkExistCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        generalPanel.add(comboPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        generalPanel.add(versionEditCtrl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(generalPanel, gridBagConstraints);

        testDetailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Test Detail", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        testDetailPanel.setLayout(new java.awt.GridBagLayout());

        objectContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Object", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        objectContainer.setLayout(new java.awt.GridBagLayout());

        objectExplanationLabel.setText("A test must point to an object to be valid.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 0, 0);
        objectContainer.add(objectExplanationLabel, gridBagConstraints);

        objectIdCaption.setText("Object Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 3);
        objectContainer.add(objectIdCaption, gridBagConstraints);

        objectIdLabel.setText("No Value Set");
        objectIdLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        objectIdLabel.setMaximumSize(null);
        objectIdLabel.setMinimumSize(null);
        objectIdLabel.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        objectContainer.add(objectIdLabel, gridBagConstraints);

        objectIdChooserButton.setText("Choose Object");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 6);
        objectContainer.add(objectIdChooserButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        testDetailPanel.add(objectContainer, gridBagConstraints);

        stateContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "State(s)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        stateContainer.setLayout(new java.awt.GridBagLayout());

        stateStatusLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        stateContainer.add(stateStatusLabel, gridBagConstraints);

        statesTablePanel.setLayout(new java.awt.GridBagLayout());

        statesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "State"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        statesTable.setMaximumSize(null);
        statesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        statesTableScrollPane.setViewportView(statesTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        statesTablePanel.add(statesTableScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        stateContainer.add(statesTablePanel, gridBagConstraints);

        statesTableControls.setLayout(new java.awt.GridBagLayout());

        addStateButton.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        statesTableControls.add(addStateButton, gridBagConstraints);

        removeStateButton.setText("Remove");
        removeStateButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        statesTableControls.add(removeStateButton, gridBagConstraints);

        stateUpButton.setText("Move Up");
        stateUpButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        statesTableControls.add(stateUpButton, gridBagConstraints);

        stateDownButton.setText("Move Down");
        stateDownButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        statesTableControls.add(stateDownButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.1;
        stateContainer.add(statesTableControls, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        testDetailPanel.add(stateContainer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        add(testDetailPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addStateButton;
    private javax.swing.JLabel checkCaption;
    private javax.swing.JComboBox checkComboBox;
    private javax.swing.JLabel checkExistCaption;
    private javax.swing.JComboBox checkExistCombo;
    private javax.swing.JPanel comboPanel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JPanel objectContainer;
    private javax.swing.JLabel objectExplanationLabel;
    private javax.swing.JLabel objectIdCaption;
    private javax.swing.JButton objectIdChooserButton;
    private javax.swing.JLabel objectIdLabel;
    private javax.swing.JButton removeStateButton;
    private javax.swing.JPanel stateContainer;
    private javax.swing.JButton stateDownButton;
    private javax.swing.JLabel stateOperatorCaption;
    private javax.swing.JComboBox stateOperatorCombo;
    private javax.swing.JLabel stateStatusLabel;
    private javax.swing.JButton stateUpButton;
    private javax.swing.JTable statesTable;
    private javax.swing.JPanel statesTableControls;
    private javax.swing.JPanel statesTablePanel;
    private javax.swing.JScrollPane statesTableScrollPane;
    private javax.swing.JLabel testCommentCaption;
    private javax.swing.JTextField testCommentTextField;
    private javax.swing.JPanel testDetailPanel;
    private javax.swing.JLabel testIdCaption;
    private com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField testIdField;
    private javax.swing.JLabel testTypeCaption;
    private javax.swing.JLabel testTypeLabel;
    private com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl versionEditCtrl;
    // End of variables declaration//GEN-END:variables
}
