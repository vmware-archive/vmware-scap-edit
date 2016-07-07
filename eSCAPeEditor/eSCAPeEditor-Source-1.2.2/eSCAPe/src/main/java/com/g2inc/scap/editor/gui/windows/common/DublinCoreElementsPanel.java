package com.g2inc.scap.editor.gui.windows.common;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.DublinCoreElementEditor;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.xccdf.DublinCoreElement;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.domain.xccdf.impl.ReferenceChildComparator;
import com.g2inc.scap.library.schema.SchemaLocator;

/**
 * A reuseable component for managing a list of DublinCoreElement
 *
 * @author ssill2
 */
public class DublinCoreElementsPanel extends ChangeNotifierPanel implements ActionListener, ListSelectionListener
{
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();

        if(src == addElementButton)
        {
            String selectedItem = (String) availableDCElementsCombo.getSelectedItem();
            
            XCCDFBenchmark benchmark =  (XCCDFBenchmark) EditorMainWindow.getInstance().getActiveEditorForm().getDocument();
            DublinCoreElement dce = benchmark.createDublinCoreElement(selectedItem);

            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            DublinCoreElementEditor editorPage = new DublinCoreElementEditor();

            editor.setEditorPage(editorPage);
            editor.setData(dce);
            editor.pack();
            editor.setLocationRelativeTo(EditorMainWindow.getInstance().getActiveEditorForm());
            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                DCEListModel model = (DCEListModel) dceList.getModel();

                model.addElement(dce);

                notifyRegisteredListeners();
            }
        }
        else if(src == editButton)
        {
            DublinCoreElement dce = (DublinCoreElement) dceList.getSelectedValue();
            int currentLoc = dceList.getSelectedIndex();
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);

            DublinCoreElementEditor editorPage = new DublinCoreElementEditor();

            editor.setEditorPage(editorPage);
            editor.setData(dce);
            editor.pack();
            editor.setLocationRelativeTo(EditorMainWindow.getInstance().getActiveEditorForm());
            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                DCEListModel model = (DCEListModel) dceList.getModel();
                model.set(currentLoc, dce);
                notifyRegisteredListeners();
            }
        }
        else if(src == removeButton)
        {
            DCEListModel model = (DCEListModel) dceList.getModel();

            Object[] selectedValues =  dceList.getSelectedValues();

            for(int x = 0; x < selectedValues.length; x++)
            {
                model.removeElement(selectedValues[x]);
            }

            notifyRegisteredListeners();
        }
        else if(src == availableDCElementsCombo)
        {
            String item = (String) availableDCElementsCombo.getSelectedItem();

            DCEListModel model = (DCEListModel) dceList.getModel();

            if(model.alreadyContainsElement(item))
            {
                addElementButton.setEnabled(false);
            }
            else
            {
                addElementButton.setEnabled(true);
            }

        }
    }

    private void initButtons()
    {
        addElementButton.addActionListener(this);
        editButton.addActionListener(this);
        removeButton.addActionListener(this);
    }

    private void initComboBox()
    {
        List<String> comboOptions = SchemaLocator.getInstance().getSupportedDubinCoreElements();

        for(String option : comboOptions)
        {
            availableDCElementsCombo.addItem(option);
        }

        availableDCElementsCombo.validate();
        availableDCElementsCombo.addActionListener(this);
    }

    private void initList()
    {
        dceList.setModel(new DCEListModel());
        dceList.addListSelectionListener(this);
    }

    private void initComponents2()
    {
        initButtons();
        initList();
    }

    /** Creates new form DublinCoreElementsPanel */
    public DublinCoreElementsPanel()
    {
        initComponents();
        initComponents2();
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        Object src = e.getSource();
        if(src == dceList)
        {
            int [] selectedIndices = dceList.getSelectedIndices();

            if(selectedIndices == null || selectedIndices.length == 0)
            {
                editButton.setEnabled(false);
                removeButton.setEnabled(false);
            }
            else
            {
                if(selectedIndices.length > 1)
                {
                    editButton.setEnabled(false);
                    removeButton.setEnabled(true);
                }
                else
                {
                    editButton.setEnabled(true);
                    removeButton.setEnabled(true);
                }
            }
        }
    }

    public List<DublinCoreElement> getDublinCoreElements()
    {
        DCEListModel model = (DCEListModel) dceList.getModel();

        return model.getElements();
    }

    public void setDublinCoreElements(List<DublinCoreElement> elements)
    {
        initComboBox();

        DCEListModel model = (DCEListModel) dceList.getModel();
        model.setListData(elements);
    }

    private class DCEListModel extends DefaultListModel
    {
        public boolean alreadyContainsElement(String name)
        {
            boolean ret = false;

            Enumeration elements = elements();
            while(elements.hasMoreElements())
            {
                DublinCoreElement element = (DublinCoreElement) elements.nextElement();

                if(element.getName().equals(name))
                {
                    ret = true;
                    break;
                }
            }
            return ret;
        }

        @Override
        public void addElement(Object obj)
        {
            super.addElement(obj);

            List<DublinCoreElement> sortedList = new ArrayList<DublinCoreElement>(getSize());

            Object[] currentElements = toArray();

            if(currentElements != null && currentElements.length > 1)
            {
                for(Object o : currentElements)
                {
                    sortedList.add((DublinCoreElement) o);
                }

                Collections.sort(sortedList, new ReferenceChildComparator<DublinCoreElement>());

                int index = 0;

                for(DublinCoreElement dce : sortedList)
                {
                    setElementAt(dce, index);
                    fireContentsChanged(dceList, index, index);
                    index++;
                }

            }
        }

        public List<DublinCoreElement> getElements()
        {
            List<DublinCoreElement> ret = new ArrayList<DublinCoreElement>(getSize());

            Enumeration elements = elements();
            while(elements.hasMoreElements())
            {
                DublinCoreElement element = (DublinCoreElement) elements.nextElement();
                ret.add(element);
            }
            return ret;
        }

        public void setListData(List<DublinCoreElement> elements)
        {
            clear();

            if(elements != null)
            {
                for(DublinCoreElement dce : elements)
                {
                    addElement(dce);
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

        availableDCElementsCaption = new javax.swing.JLabel();
        availableDCElementsCombo = new javax.swing.JComboBox();
        addElementButton = new javax.swing.JButton();
        dcePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dceList = new javax.swing.JList();
        listButtonPanel = new javax.swing.JPanel();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        availableDCElementsCaption.setText("Available Elements");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 0);
        add(availableDCElementsCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        add(availableDCElementsCombo, gridBagConstraints);

        addElementButton.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 5);
        add(addElementButton, gridBagConstraints);

        dcePanel.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setViewportView(dceList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        dcePanel.add(jScrollPane1, gridBagConstraints);

        listButtonPanel.setLayout(new java.awt.GridBagLayout());

        editButton.setText("Edit");
        editButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        listButtonPanel.add(editButton, gridBagConstraints);

        removeButton.setText("Remove");
        removeButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        listButtonPanel.add(removeButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        dcePanel.add(listButtonPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 7, 4, 5);
        add(dcePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addElementButton;
    private javax.swing.JLabel availableDCElementsCaption;
    private javax.swing.JComboBox availableDCElementsCombo;
    private javax.swing.JList dceList;
    private javax.swing.JPanel dcePanel;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel listButtonPanel;
    private javax.swing.JButton removeButton;
    // End of variables declaration//GEN-END:variables

}
