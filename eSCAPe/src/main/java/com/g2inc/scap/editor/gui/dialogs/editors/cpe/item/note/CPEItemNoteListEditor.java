package com.g2inc.scap.editor.gui.dialogs.editors.cpe.item.note;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.SupportedInputLanguages;
import com.g2inc.scap.library.domain.cpe.CPEItemNote;
import com.g2inc.scap.library.domain.cpe.CPEItemNoteList;

public class CPEItemNoteListEditor extends javax.swing.JPanel implements IEditorPage
{

    private EditorDialog parentEditor = null;
    private CPEItemNoteList noteList = null;

    private ActionListener comboListener = null;

    private void initLangCombo()
    {
        comboListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                noteList.setLang((String) langCombo.getSelectedItem());
            }
        };

        String[] langs = SupportedInputLanguages.getLangs();

        for(int x = 0; x < langs.length; x++)
        {
            langCombo.addItem(langs[x]);
        }

        langCombo.setSelectedItem(SupportedInputLanguages.getDefault());

        langCombo.setToolTipText("Note: Languages listed are only those supported by this machine.");
    }

    private void initListModels()
    {
        DefaultListModel model = new DefaultListModel();
        list.setModel(model);
    }

    private void addListenSelectionListeners()
    {
        list.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                int[] indices = list.getSelectedIndices();

                if(indices == null || indices.length == 0)
                {
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                    return;
                }
                else if(indices != null && indices.length == 1)
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
        
        list.addMouseListener(new MouseListener()
        {
            
            @Override
            public void mouseReleased(MouseEvent e){}
            
            @Override
            public void mousePressed(MouseEvent e){}
            
            @Override
            public void mouseExited(MouseEvent e){}
            
            @Override
            public void mouseEntered(MouseEvent e){}
            
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    openCPEItemNoteEditor();
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
                CPEItemNote note = noteList.createNote();

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                CPEItemNoteEditor editorPage = new CPEItemNoteEditor();
                editor.setEditorPage(editorPage);

                editor.setData(note);

                editor.pack();
                editor.setLocationRelativeTo(null);
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    noteList.addNote(note);
                    DefaultListModel model = (DefaultListModel) list.getModel();
                    model.addElement(note);

                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                }
            }
        });

        editButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openCPEItemNoteEditor();
            }
        });

        removeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Object[] selectedValues = list.getSelectedValues();

                if(selectedValues != null)
                {
                    DefaultListModel model = (DefaultListModel) list.getModel();

                    for(int x = 0 ; x < selectedValues.length ; x++)
                    {
                        CPEItemNote note = (CPEItemNote) selectedValues[x];

                        noteList.removeNote(note);

                        model.removeElement(note);
                    }

                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                }
            }
        });
    }

    private void initComponents2()
    {
        initListModels();
        addListenSelectionListeners();
        addButtonListeners();
        initLangCombo();
    }

    public CPEItemNoteListEditor()
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

        langLabel = new javax.swing.JLabel();
        langCombo = new javax.swing.JComboBox();
        mainPanel = new javax.swing.JPanel();
        listPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        buttonPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        langLabel.setText("Language");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 7, 0);
        add(langLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(langCombo, gridBagConstraints);

        mainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Notes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        mainPanel.setLayout(new java.awt.GridBagLayout());

        listPanel.setLayout(new java.awt.GridBagLayout());

        scrollPane.setViewportView(list);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        listPanel.add(scrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.95;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
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
        gridBagConstraints.weighty = 0.01;
        buttonPanel.add(removeButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 5, 0);
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
    private javax.swing.JComboBox langCombo;
    private javax.swing.JLabel langLabel;
    private javax.swing.JList list;
    private javax.swing.JPanel listPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton removeButton;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables

    public CPEItemNoteList getData()
    {
        return noteList;
    }

    public void setEditorDialog(EditorDialog editorDialog)
    {
        parentEditor = editorDialog;
        parentEditor.setTitle("Note Container Editor");
    }

    public void setData(Object data)
    {
        if (data == null)
        {
            throw new IllegalStateException("setData(Object data) must be called with a non-null item note list");
        }

        noteList = (CPEItemNoteList) data;

        // populate the list
        List<CPEItemNote> notes = this.noteList.getNotes();

        if(notes != null)
        {
            if(notes.size() > 0)
            {
                DefaultListModel dlm = (DefaultListModel) list.getModel();

                for(int x = 0; x < notes.size(); x++)
                {
                    CPEItemNote note = notes.get(x);

                    dlm.addElement(note);
                }
            }
        }

        langCombo.setSelectedItem(noteList.getLang());
        langCombo.addActionListener(comboListener);
    }

    private void openCPEItemNoteEditor()
    {
        CPEItemNote note = (CPEItemNote) list.getSelectedValue();

        EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
        CPEItemNoteEditor editorPage = new CPEItemNoteEditor();
        editor.setEditorPage(editorPage);

        editor.setData(note);

        editor.pack();
        editor.setLocationRelativeTo(null);
        editor.setVisible(true);

        if(!editor.wasCancelled())
        {
            DefaultListModel model = (DefaultListModel) list.getModel();
            int currentLoc = model.indexOf(note);

            model.set(currentLoc, note);

            EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
        }
    }
}
