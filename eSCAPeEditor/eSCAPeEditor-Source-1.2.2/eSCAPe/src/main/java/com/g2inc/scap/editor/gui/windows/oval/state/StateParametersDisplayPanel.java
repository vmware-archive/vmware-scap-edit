package com.g2inc.scap.editor.gui.windows.oval.state;
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
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.state.parameter.StateParameterEditor;
import com.g2inc.scap.editor.gui.model.table.state.parameters.StateParameterTableModel;
import com.g2inc.scap.editor.gui.windows.EditorForm;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.NavigationButton;
import com.g2inc.scap.editor.gui.windows.oval.OvalEditorForm;
import com.g2inc.scap.library.domain.oval.OvalEntity;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalStateParameter;
import com.g2inc.scap.library.domain.oval.OvalVariable;

public class StateParametersDisplayPanel extends javax.swing.JPanel
{
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(StateParametersDisplayPanel.class);

    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    private boolean modalParent = false;
    private EditorForm parentEditor = null;
    private OvalState ovalState = null;
    private boolean changed = false;
    private DocumentListener commentDocListener = null;

    private List<OvalStateParameter> addedParmsList = new ArrayList<OvalStateParameter>();

    public void setModalParent(boolean b)
    {
        modalParent = b;
    }

    public void addChangeListener(ChangeListener cl)
    {
        if(!changeListeners.contains(cl))
        {
            changeListeners.add(cl);
        }
    }

    public void removeChangeListener(ChangeListener cl)
    {
        if(changeListeners.contains(cl))
        {
            changeListeners.remove(cl);
        }
    }

    public void setParentEditor(EditorForm parentEditor)
    {
        this.parentEditor = parentEditor;
    }

    private void initCombo()
    {        
        possibleParmsCombo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                OvalEntity selectedParm = (OvalEntity) possibleParmsCombo.getSelectedItem();

                if(selectedParm == null)
                {
                    return;
                }
                
                String documentation = selectedParm.getDocumentation();

                parmDocTextArea.setText(documentation);
                //if (!addedParmsMap.keySet().contains(selectedParm.toString()))
                if (getStateParameter(selectedParm.getName(),addedParmsList) == null)
                {
                    addParmButton.setEnabled(true);
                }
                else
                {
                    addParmButton.setEnabled(false);
                }
            }
        });
    }

    private void initButtons()
    {
        addParmButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                OvalEntity selectedEntity = (OvalEntity) possibleParmsCombo.getSelectedItem();

                if(selectedEntity == null)
                {
                    return;
                }
                OvalStateParameter osp = getStateParameter(selectedEntity.toString(), addedParmsList);
                if (osp == null)
                {
                    osp = ovalState.createOvalStateParameter(selectedEntity);

                    if(osp != null)
                    {
                        insertStateParameter(osp, ovalState.getValidParameters());
                        
                        // tell the table to refresh
                        StateParameterTableModel model = new StateParameterTableModel(addedParmsList);
                        addedParmsTable.setModel(model);

                        changed = true;
                        notifyRegisteredListeners();
                    }
                    else
                    {
                        log.debug("osp is null!");
                    }
                }
            }
        });

        modifyParmButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                StateParameterTableModel model = (StateParameterTableModel) addedParmsTable.getModel();
                OvalStateParameter osp = (OvalStateParameter) model.getValueAt(addedParmsTable.getSelectedRow(), -1);

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);

                StateParameterEditor editorPage = new StateParameterEditor(ovalState, osp);
                editor.setEditorPage(editorPage);

                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    editor.getData();

                    // tell the table to refresh
                    model = new StateParameterTableModel(addedParmsList);
                    addedParmsTable.setModel(model);

                    changed = true;
                    notifyRegisteredListeners();
                }
            }
        });

        removeParmButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                StateParameterTableModel model = (StateParameterTableModel) addedParmsTable.getModel();


                int[] selectedRows = addedParmsTable.getSelectedRows();

                for(int x = 0; x < selectedRows.length ; x++)
                {
                    OvalStateParameter osp  = (OvalStateParameter) model.getValueAt(selectedRows[x], -1);

                    addedParmsList.remove(osp);
                }
                addedParmsTable.setModel(new StateParameterTableModel(addedParmsList));

                changed = true;
                notifyRegisteredListeners();
            }
        });
    }

    private void initTable()
    {
        StateParameterTableModel model = new StateParameterTableModel(addedParmsList);
        addedParmsTable.setModel(model);

        addedParmsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent lse)
            {
                if(addedParmsTable.getSelectedRowCount() == 0)
                {
                    modifyParmButton.setEnabled(false);
                    removeParmButton.setEnabled(false);
                    return;
                }
                else
                {
                    if(addedParmsTable.getSelectedRowCount() == 1)
                    {
                        modifyParmButton.setEnabled(true);
                    }
                    else
                    {
                        modifyParmButton.setEnabled(false);
                    }

                    removeParmButton.setEnabled(true);
                }
            }
        });

        addedParmsTable.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                boolean isDoubleClick = arg0.getClickCount() > 1;

                if(isDoubleClick)
                {
                    if(modalParent || parentEditor == null)
                    {
                        return;
                    }
                    
                    int selectedRow = addedParmsTable.getSelectedRow();

                    if(selectedRow == -1)
                    {
                        return;
                    }

                    StateParameterTableModel model = (StateParameterTableModel) addedParmsTable.getModel();

                    OvalStateParameter osp = (OvalStateParameter) model.getValueAt(selectedRow, -1);

                    if(osp == null)
                    {
                        return;
                    }

                    if(osp.getVarRef() != null)
                    {
                        OvalVariable ov = osp.getVariableReference();

                        if (ov != null)
                        {
                            NavigationButton nb = new NavigationButton();
                            nb.setText("OvalState");
                            nb.setToolTipText(ovalState.getId());
                            nb.setSelectedElement(parentEditor.getSelectedPath());
                            nb.setEditorForm(parentEditor);

                            EditorMainWindow.getInstance().getNavPanel().addNavigationButton(nb);

                            // now set selection to the item the user intended
                            ((OvalEditorForm) parentEditor).selectVariable(ov);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {
            }

            @Override
            public void mouseReleased(MouseEvent arg0)
            {
            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {
            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {
            }
        });
    }

    private void initTextFields()
    {
        commentDocListener = new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                // text has changed, notify everyone
                // who's listening for changes
                changed = true;
                notifyRegisteredListeners();
            }

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
        };

        commentTextField.getDocument().addDocumentListener(commentDocListener);
    }

    private void initComponents2()
    {
        initTable();
        initButtons();
        initCombo();
        initTextFields();
    }

    public StateParametersDisplayPanel()
    {
        initComponents();
        initComponents2();
    }

    public List<OvalStateParameter> getAddedParmsList()
    {
        return addedParmsList;
    }

     public void clearAddedParmsList()
    {
        addedParmsList.clear();
        StateParameterTableModel model = new StateParameterTableModel(addedParmsList);
        addedParmsTable.setModel(model);

    }

    public boolean allParmsOk()
    {
        boolean ret = true;
        
        if(addedParmsList.size() > 0)
        {
            // they've added parameters, make sure they all have values.
            Collection<OvalStateParameter> parms = addedParmsList;

            Iterator<OvalStateParameter> itr = parms.iterator();

            int totalParms = addedParmsList.size();
            int missingValues = 0;

            while(itr.hasNext())
            {
                OvalStateParameter osp = itr.next();

                String varref = osp.getVarRef();
                String value = osp.getValue();

                if(varref == null || varref.length() == 0)
                {
                    if(value == null || value.length() == 0)
                    {
                        missingValues++;
                        ret = false;
                    }
                }
                else if(value == null || value.length() == 0)
                {
                    if(varref == null || varref.length() == 0)
                    {
                        missingValues++;
                        ret = false;
                    }
                }
            }

            if(!ret)
            {
                log.debug(missingValues + "/" + totalParms + ": missing parms");
            }
            else
            {
                
            }
        }

        if(commentTextField.getText() == null || commentTextField.getText().length() == 0)
        {
            ret = false;
            return ret;
        }
        else
        {
            ovalState.setComment(commentTextField.getText());
        }

        return ret;
    }

    private void notifyRegisteredListeners()
    {
        for(int x = 0; x < changeListeners.size(); x++)
        {
            ChangeListener cl = changeListeners.get(x);

            ChangeEvent ce = new ChangeEvent(this);

            cl.stateChanged(ce);
        }
    }

    public void setOvalState(OvalState os)
    {
        ovalState = os;

        possibleParmsCombo.removeAllItems();

        List<OvalEntity> options = os.getValidParameters();

        for (int x = 0; x < options.size(); x++)
        {
            OvalEntity oe = options.get(x);
            possibleParmsCombo.addItem(oe);
        }

        possibleParmsCombo.setSelectedIndex(0);

        addedParmsList.clear();

        List<OvalStateParameter> existingParms = ovalState.getParameters();

        if(existingParms != null && existingParms.size() > 0)
        {
            for(int x = 0; x < existingParms.size(); x++)
            {
                OvalStateParameter osp = existingParms.get(x);
                insertStateParameter(osp, options);
                //addedParmsMap.put(osp.getElementName(), osp);
            }

            StateParameterTableModel model = new StateParameterTableModel(addedParmsList);
            addedParmsTable.setModel(model);
        }

        commentTextField.getDocument().removeDocumentListener(commentDocListener);
        commentTextField.setText(ovalState.getComment());
        commentTextField.getDocument().addDocumentListener(commentDocListener);
    }

    public boolean hasChanged()
    {
        return changed;
    }

    public void setChanged(boolean b)
    {
        changed = b;
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

        commentCaption = new javax.swing.JLabel();
        commentTextField = new javax.swing.JTextField();
        possibleParmsPanel = new javax.swing.JPanel();
        possibleParmsCaption = new javax.swing.JLabel();
        possibleParmsCombo = new javax.swing.JComboBox();
        addParmButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        parmDocTextArea = new javax.swing.JTextArea();
        addedParmsPanel = new javax.swing.JPanel();
        addedParmsScroller = new javax.swing.JScrollPane();
        addedParmsTable = new javax.swing.JTable();
        buttonsPanel = new javax.swing.JPanel();
        modifyParmButton = new javax.swing.JButton();
        removeParmButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        commentCaption.setText("Comment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(commentCaption, gridBagConstraints);

        commentTextField.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(commentTextField, gridBagConstraints);

        possibleParmsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Possible parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        possibleParmsPanel.setLayout(new java.awt.GridBagLayout());

        possibleParmsCaption.setText("Parameter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
        possibleParmsPanel.add(possibleParmsCaption, gridBagConstraints);

        possibleParmsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.92;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 0, 0);
        possibleParmsPanel.add(possibleParmsCombo, gridBagConstraints);

        addParmButton.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        possibleParmsPanel.add(addParmButton, gridBagConstraints);

        parmDocTextArea.setBackground(new java.awt.Color(240, 240, 240));
        parmDocTextArea.setColumns(20);
        parmDocTextArea.setEditable(false);
        parmDocTextArea.setFont(new java.awt.Font("Monospaced", 0, 12));
        parmDocTextArea.setLineWrap(true);
        parmDocTextArea.setRows(5);
        parmDocTextArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(parmDocTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 1, 1);
        possibleParmsPanel.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.4;
        add(possibleParmsPanel, gridBagConstraints);

        addedParmsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Added parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        addedParmsPanel.setLayout(new java.awt.GridBagLayout());

        addedParmsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        addedParmsTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addedParmsScroller.setViewportView(addedParmsTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 5, 6);
        addedParmsPanel.add(addedParmsScroller, gridBagConstraints);

        buttonsPanel.setLayout(new java.awt.GridBagLayout());

        modifyParmButton.setText("Edit");
        modifyParmButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        buttonsPanel.add(modifyParmButton, gridBagConstraints);

        removeParmButton.setText("Remove");
        removeParmButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.9;
        buttonsPanel.add(removeParmButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        addedParmsPanel.add(buttonsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.7;
        add(addedParmsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addParmButton;
    private javax.swing.JPanel addedParmsPanel;
    private javax.swing.JScrollPane addedParmsScroller;
    private javax.swing.JTable addedParmsTable;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JLabel commentCaption;
    private javax.swing.JTextField commentTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton modifyParmButton;
    private javax.swing.JTextArea parmDocTextArea;
    private javax.swing.JLabel possibleParmsCaption;
    private javax.swing.JComboBox possibleParmsCombo;
    private javax.swing.JPanel possibleParmsPanel;
    private javax.swing.JButton removeParmButton;
    // End of variables declaration//GEN-END:variables

    private OvalStateParameter getStateParameter(String name, List<OvalStateParameter> list) {
        OvalStateParameter result = null;
        for (int i=0; i<list.size(); i++) {
            OvalStateParameter entry = list.get(i);
            if (entry.getElementName().equals(name)) {
                result = entry;
                break;
            }
        }
        return result;
    }

    private void removeStateParameter(String name, List<OvalStateParameter> list) {
        for (int i=0; i<list.size(); i++) {
            OvalStateParameter entry = list.get(i);
            if (entry.getElementName().equals(name)) {
                list.remove(i);
                break;
            }
        }
    }

    private void insertStateParameter(OvalStateParameter stateParameter, List<OvalEntity> validParms) {
        HashMap<String, Integer> parmOrder = new HashMap<String, Integer>();
        for (int i=0; i<validParms.size(); i++) {
            parmOrder.put(validParms.get(i).getName(), i);
        }
        String newParmName = stateParameter.getElementName();
        
        // This is a fix to handle the notes element when used with states.
        if (newParmName.toLowerCase().equals("notes"))
        {
        	return;
        }
        
        int newParmOrder = parmOrder.get(newParmName);
        int i=0;
        for (; i<addedParmsList.size(); i++) {
            OvalStateParameter existingNode = addedParmsList.get(i);
            String existingParmName = existingNode.getElementName();
            int existingParmOrder = parmOrder.get(existingParmName);
            if (newParmOrder < existingParmOrder) {               
                break;
            }
        }
        addedParmsList.add(i, stateParameter);
    }
}
