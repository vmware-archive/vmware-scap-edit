package com.g2inc.scap.editor.gui.windows.cpe.item;

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
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.cpe.item.title.CPEItemTitleEditor;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.cpe.CPEItem;
import com.g2inc.scap.library.domain.cpe.CPEItemTitle;

public class ItemTitlesTab extends CPEItemTab
{
    private static final long serialVersionUID = 1L;

    private void initListModels()
    {
        DefaultListModel titleModel = new DefaultListModel();
        list.setModel(titleModel);
    }

    private void addListSelectionListeners()
    {
        list.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                int[] indices = list.getSelectedIndices();

                if (indices == null || indices.length == 0)
                {
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                    return;
                } else if (indices != null && indices.length == 1)
                {
                    // more than one item was selected, only one
                    // item can be edited at a time.
                    editButton.setEnabled(true);
                } else
                {
                    editButton.setEnabled(false);
                }

                removeButton.setEnabled(true);
            }
        });

        list.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseReleased(MouseEvent e)
            {
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    openCPEItemTitleEditor();
                }
            }
        });
    }

    private void addButtonListeners()
    {
        addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                CPEItemTitle title = doc.createTitle();

                EditorDialog editor = new EditorDialog(EditorMainWindow
                        .getInstance(), true);
                CPEItemTitleEditor editorPage = new CPEItemTitleEditor();
                editor.setEditorPage(editorPage);

                editor.setData(title);

                editor.pack();
                editor.setLocationRelativeTo(null);
                editor.setVisible(true);

                if (!editor.wasCancelled())
                {
                    doc.addTitle(title);
                    DefaultListModel model = (DefaultListModel) list.getModel();
                    model.addElement(title);

                    EditorUtil.markActiveWindowDirty(
                            EditorMainWindow.getInstance(), true);
                }
            }
        });

        editButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openCPEItemTitleEditor();
            }
        });

        removeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Object[] selectedValues = list.getSelectedValues();

                if (selectedValues != null)
                {
                    DefaultListModel model = (DefaultListModel) list.getModel();

                    for (int x = 0; x < selectedValues.length; x++)
                    {
                        CPEItemTitle title = (CPEItemTitle) selectedValues[x];

                        doc.removeTitle(title);

                        model.removeElement(title);
                    }

                    EditorUtil.markActiveWindowDirty(
                            EditorMainWindow.getInstance(), true);
                }
            }
        });
    }

    private void initComponents2()
    {
        initListModels();
        addListSelectionListeners();
        addButtonListeners();
    }

    /** Creates new form DefinitionDetailTab */
    public ItemTitlesTab()
    {
        initComponents();
        initComponents2();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed"
    // desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        mainPanel = new javax.swing.JPanel();
        listPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        buttonPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        mainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                "Titles",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 0, 11),
                java.awt.SystemColor.windowText)); // NOI18N
        mainPanel.setLayout(new java.awt.GridBagLayout());

        listPanel.setLayout(new java.awt.GridBagLayout());

        scrollPane.setViewportView(list);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        listPanel.add(scrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.95;
        gridBagConstraints.weighty = 1.0;
        mainPanel.add(listPanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        addButton.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        buttonPanel.add(addButton, gridBagConstraints);

        editButton.setText("Edit");
        editButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        buttonPanel.add(editButton, gridBagConstraints);

        removeButton.setText("Remove");
        removeButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        buttonPanel.add(removeButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 1.0;
        mainPanel.add(buttonPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(mainPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton editButton;
    private javax.swing.JList list;
    private javax.swing.JPanel listPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton removeButton;
    private javax.swing.JScrollPane scrollPane;

    // End of variables declaration//GEN-END:variables

    // End of variables declaration

    public CPEItem getDoc()
    {
        return doc;
    }

    public void setDoc(CPEItem doc)
    {
        this.doc = doc;

        // populate titles list
        List<CPEItemTitle> titles = doc.getTitles();

        if (titles != null)
        {
            DefaultListModel tListModel = (DefaultListModel) list.getModel();

            for (int x = 0; x < titles.size(); x++)
            {
                CPEItemTitle title = titles.get(x);

                tListModel.addElement(title);
            }
        }
    }

    private void openCPEItemTitleEditor()
    {
        CPEItemTitle title = (CPEItemTitle) list.getSelectedValue();

        EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(),
                true);
        CPEItemTitleEditor editorPage = new CPEItemTitleEditor();
        editor.setEditorPage(editorPage);

        editor.setData(title);

        editor.pack();
        editor.setLocationRelativeTo(null);
        editor.setVisible(true);

        if (!editor.wasCancelled())
        {
            DefaultListModel model = (DefaultListModel) list.getModel();
            int currentLoc = model.indexOf(title);

            model.set(currentLoc, title);

            EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(),
                    true);
        }
    }
}
