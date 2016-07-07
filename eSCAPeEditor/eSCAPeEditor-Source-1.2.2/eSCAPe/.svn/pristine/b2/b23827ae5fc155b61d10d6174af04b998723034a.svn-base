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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.ComplexValueElementEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.SelComplexValueElementEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.SelValueElementEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.ValueElementEditor;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.ChangeNotifierPanel;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPElement;
import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.xccdf.ComplexValueElement;
import com.g2inc.scap.library.domain.xccdf.SelComplexValueElement;
import com.g2inc.scap.library.domain.xccdf.SelValueElement;
import com.g2inc.scap.library.domain.xccdf.ValueElement;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

public class SimpleOrComplexValueListPanel extends ChangeNotifierPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    protected List elementList = null;
    private static Logger LOG = Logger.getLogger(SimpleOrComplexValueListPanel.class);
    private String simpleElementName = null;
    private String complexElementName = null;
    private boolean selectorAttribute = true;

    private void initListModels()
    {
        listBox.setModel(new DefaultListModel());
    }

    private void addListSelectionListeners()
    {
        listBox.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                int[] indices = listBox.getSelectedIndices();

                if (indices == null || indices.length == 0)
                {
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                    return;
                }
                else if (indices != null && indices.length == 1)
                {
                    // more than one item was selected, only one
                    // item can be edited at a time.
                    editButton.setEnabled(true);
                }
                else
                {
                    editButton.setEnabled(false);
                }

                removeButton.setEnabled(true);
            }
        });
        
        listBox.addMouseListener(new MouseListener()
        {
            
            @Override
            public void mouseReleased(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void mousePressed(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void mouseExited(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void mouseEntered(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                if (arg0.getClickCount() == 2)
                {
                    openEditorDialog();
                }
                
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();

        IEditorPage editorPage = null;
        SCAPElement newElement = null;
        XCCDFBenchmark benchmark = (XCCDFBenchmark) EditorMainWindow.getInstance().getActiveEditorForm().getDocument();
        if(src == addSimpleButton)
        {
            if (selectorAttribute) {
                editorPage = new SelValueElementEditor();      
                newElement = benchmark.createSCAPElementInt(simpleElementName, SelValueElement.class);
            } else {
                editorPage = new ValueElementEditor();      
                newElement = benchmark.createSCAPElementInt(simpleElementName, ValueElement.class);
            }
        } 
        else if(src == addComplexButton)
        {
            if (selectorAttribute) {
                editorPage = new SelComplexValueElementEditor();
                newElement = benchmark.createSCAPElementInt(complexElementName, SelComplexValueElement.class);
            } else {
                editorPage = new ComplexValueElementEditor();
                newElement = benchmark.createSCAPElementInt(complexElementName, ComplexValueElement.class);
            }
        } 
        if (editorPage != null) {
            // either "Add Simple" or "Add Complex" button was pressed
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            editor.setEditorPage(editorPage);
                       editor.setData(newElement);
            editor.pack();
            editor.setLocationRelativeTo(null);
            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                newElement = (SCAPElementImpl) editor.getData();
                elementList.add(newElement);
                DefaultListModel model = (DefaultListModel) listBox.getModel();
                // add to the list
                model.addElement(newElement);
                setChanged(true);
                notifyRegisteredListeners();
            }
        }
        else {
            // button pressed was either Edit or Remove
            if(src == editButton)
            {
                openEditorDialog();
            }
            else if(src == removeButton)
            {
                Object[] selectedObjects = listBox.getSelectedValues();
                DefaultListModel model = (DefaultListModel) listBox.getModel();
                for (int i = 0; i < selectedObjects.length; i++)
                {
                    model.removeElement(selectedObjects[i]);
                    elementList.remove(selectedObjects[i]);
                }
                if (model.getSize() == 0)
                {
                    removeButton.setEnabled(false);
                }
                setChanged(true);
                notifyRegisteredListeners();
            }
        }
    }

    private void openEditorDialog()
    {
        SCAPElementImpl currentValueElement = (SCAPElementImpl) listBox.getSelectedValue();
        EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
        IEditorPage editorPage = null;
        if (currentValueElement instanceof ComplexValueElement) {
            if (selectorAttribute) {
                editorPage = new SelComplexValueElementEditor();
            } else {
                editorPage = new ComplexValueElementEditor();
            }
        } else {
            if (selectorAttribute) {
                editorPage = new SelValueElementEditor(); 
            } else {
                editorPage = new ValueElementEditor(); 
            }
        }
        editor.setEditorPage(editorPage);
        editor.setData(currentValueElement);
        editor.pack();
        editor.setLocationRelativeTo(null);
        editor.setVisible(true);

        if (!editor.wasCancelled())
        {
            SCAPElementImpl newElement = (SCAPElementImpl) editor.getData();
            DefaultListModel model = (DefaultListModel) listBox.getModel();
            int currentLoc = model.indexOf(currentValueElement);
            elementList.set(currentLoc, newElement);
            model.set(currentLoc, newElement);
            setChanged(true);
            notifyRegisteredListeners();
        }
    }

    private void addButtonListeners()
    {
        addSimpleButton.addActionListener(this);
        addComplexButton.addActionListener(this);
        editButton.addActionListener(this);
        removeButton.addActionListener(this);
    }

    private void initComponents2()
    {
        SCAPDocument scapDoc =  EditorMainWindow.getInstance().getActiveEditorForm().getDocument();
        if (scapDoc.getDataStreamCollection() != null) {
            addComplexButton.setVisible(false);
            addSimpleButton.setText("Add");
        }
        
        initListModels();
        addListSelectionListeners();
        addButtonListeners();
    }

    /** Creates new form DefinitionDetailTab */
    public SimpleOrComplexValueListPanel()
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

        listPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        listBox = new javax.swing.JList();
        buttonsPanel = new javax.swing.JPanel();
        addSimpleButton = new javax.swing.JButton();
        addComplexButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        listPanel.setLayout(new java.awt.GridBagLayout());

        scrollPane.setViewportView(listBox);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        listPanel.add(scrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.95;
        gridBagConstraints.weighty = 1.0;
        add(listPanel, gridBagConstraints);

        buttonsPanel.setLayout(new java.awt.GridBagLayout());

        addSimpleButton.setText("Add Simple");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        buttonsPanel.add(addSimpleButton, gridBagConstraints);

        addComplexButton.setText("Add Complex");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        buttonsPanel.add(addComplexButton, gridBagConstraints);

        editButton.setText("Edit");
        editButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        buttonsPanel.add(editButton, gridBagConstraints);

        removeButton.setText("Remove");
        removeButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        buttonsPanel.add(removeButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(buttonsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addComplexButton;
    private javax.swing.JButton addSimpleButton;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton editButton;
    private javax.swing.JList listBox;
    private javax.swing.JPanel listPanel;
    private javax.swing.JButton removeButton;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables

    // End of variables declaration
    public List getDoc()
    {
        return elementList;
    }

    public void setDoc(List doc)
    {
        this.elementList = doc;
        DefaultListModel rListModel = (DefaultListModel) listBox.getModel();
        rListModel.removeAllElements();
        if (elementList != null)
        {
            for (int i = 0; i < elementList.size(); i++)
            {
                SCAPElementImpl scapElement = (SCAPElementImpl) elementList.get(i);
                rListModel.addElement(scapElement);
            }
        }
    }
    
    public String getSimpleElementName() {
        return simpleElementName;
    }

    public void setSimpleElementName(String simpleElementName) {
        this.simpleElementName = simpleElementName;
    }

    public String getComplexElementName() {
        return complexElementName;
    }

    public void setComplexElementName(String complexElementName) {
        this.complexElementName = complexElementName;
    }
    
    public boolean isSelectorAttribute() {
        return selectorAttribute;
    }

    public void setSelectorAttribute(boolean selectorAttribute) {
        this.selectorAttribute = selectorAttribute;
    }    
}
