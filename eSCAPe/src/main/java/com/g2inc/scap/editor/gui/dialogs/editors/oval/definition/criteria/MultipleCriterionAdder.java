package com.g2inc.scap.editor.gui.dialogs.editors.oval.definition.criteria;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.g2inc.scap.library.domain.oval.OvalComparator;
import com.g2inc.scap.library.domain.oval.OvalTest;

public class MultipleCriterionAdder extends javax.swing.JDialog
{
    private boolean cancelled = true;

    private void initLists()
    {
        availableTestsList.setModel(new DefaultListModel());
        availableTestsList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                int[] selectedIndices = availableTestsList.getSelectedIndices();

                if (selectedIndices != null && selectedIndices.length > 0)
                {
                    addSelectedTestsButton.setEnabled(true);
                }
                else
                {
                    addSelectedTestsButton.setEnabled(false);
                }
            }
        });

        chosenTestsList.setModel(new DefaultListModel());
        chosenTestsList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                int[] selectedIndices = chosenTestsList.getSelectedIndices();

                if(selectedIndices != null && selectedIndices.length > 0)
                {
                    removeSelectedButton.setEnabled(true);
                }
                else
                {
                    removeSelectedButton.setEnabled(false);
                }
            }
        });
    }

    private void initButtons()
    {
        final MultipleCriterionAdder thisRef = this;

        addSelectedTestsButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Object[] selectedValues = availableTestsList.getSelectedValues();

                if(selectedValues != null && selectedValues.length > 0)
                {
                    DefaultListModel availModel = (DefaultListModel) availableTestsList.getModel();
                    DefaultListModel chosenModel = (DefaultListModel) chosenTestsList.getModel();

                    // remove them from the available tests list and into the
                    // chosen tests list
                    for(int x = 0; x < selectedValues.length; x++)
                    {
                        Object o = selectedValues[x];

                        availModel.removeElement(o);
                        chosenModel.addElement(o);
                    }

                    if(chosenModel.getSize() == 0)
                    {
                        okButton.setEnabled(false);
                    }
                    else
                    {
                        okButton.setEnabled(true);
                    }
                }
            }
        });

        removeSelectedButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Object[] selectedValues = chosenTestsList.getSelectedValues();

                if(selectedValues != null && selectedValues.length > 0)
                {
                    DefaultListModel availModel = (DefaultListModel) availableTestsList.getModel();
                    DefaultListModel chosenModel = (DefaultListModel) chosenTestsList.getModel();

                    // remove them from the chosen tests list and into the
                    // available tests list
                    for(int x = 0; x < selectedValues.length; x++)
                    {
                        Object o = selectedValues[x];

                        chosenModel.removeElement(o);
                        availModel.addElement(o);
                    }
                
                    if(chosenModel.getSize() == 0)
                    {
                        okButton.setEnabled(false);
                    }
                    else
                    {
                        okButton.setEnabled(true);
                    }
                }
            }
        });

        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cancelled = false;
                thisRef.setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                thisRef.setVisible(false);
            }
        });
    }

    private void initComponents2()
    {
        initButtons();
        initLists();

        setMaximumSize(new Dimension(1024, 768));
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 600));
    }

    /**
     * Our own constructor that also takes the list of available tests.
     *
     * @param parent The parent frame
     * @param modal Whether we are to be modal or not
     * @param allTests A list of tests from the parent oval document
     */
    public MultipleCriterionAdder(java.awt.Frame parent, boolean modal, List<OvalTest> allTests)
    {
        super(parent, modal);
        initComponents();
        initComponents2();

        Collections.sort(allTests, new OvalComparator<OvalTest>());

        DefaultListModel availModel = (DefaultListModel) availableTestsList.getModel();
        for(int x = 0; x < allTests.size(); x++)
        {
            OvalTest ot = allTests.get(x);
            availModel.addElement(ot);
        }
    }

    /**
     * Required constructor needed because we extend JDialog.
     *
     * @param parent The parent frame
     * @param modal Whether we are to be modal or not
     */
    public MultipleCriterionAdder(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        initComponents2();
    }

    /**
     * Tells whether this dialog was cancelled or not.
     *
     * @return boolean
     */
    public boolean wasCancelled()
    {
        return cancelled;
    }

    /**
     * Return the tests the user chose to have criterion elements created for.
     * 
     * @return List<OvalTest>
     */
    public List<OvalTest> getChosenTests()
    {
        ArrayList<OvalTest> ret = new ArrayList<OvalTest>();

        DefaultListModel chosenModel = (DefaultListModel) chosenTestsList.getModel();

        for(int x = 0 ; x < chosenModel.size(); x++)
        {
            OvalTest ot = (OvalTest) chosenModel.get(x);
            ret.add(ot);
        }

        return ret;
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

        availableTestsCaption = new javax.swing.JLabel();
        availableTestsScrollPane = new javax.swing.JScrollPane();
        availableTestsList = new javax.swing.JList();
        addSelectedTestsButton = new javax.swing.JButton();
        chosenTestsCaption = new javax.swing.JLabel();
        chosenTestsScrollPane = new javax.swing.JScrollPane();
        chosenTestsList = new javax.swing.JList();
        removeSelectedButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle("Add multiple tests as criterion elements");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setModal(true);
        setName("MultipleCriterionAdder"); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        availableTestsCaption.setText("Available tests");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        getContentPane().add(availableTestsCaption, gridBagConstraints);

        availableTestsScrollPane.setViewportView(availableTestsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        getContentPane().add(availableTestsScrollPane, gridBagConstraints);

        addSelectedTestsButton.setText("Choose selected");
        addSelectedTestsButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        getContentPane().add(addSelectedTestsButton, gridBagConstraints);

        chosenTestsCaption.setText("Tests to add criterion elements for");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 6, 0, 0);
        getContentPane().add(chosenTestsCaption, gridBagConstraints);

        chosenTestsScrollPane.setViewportView(chosenTestsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        getContentPane().add(chosenTestsScrollPane, gridBagConstraints);

        removeSelectedButton.setText("Remove selected");
        removeSelectedButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        getContentPane().add(removeSelectedButton, gridBagConstraints);

        okButton.setText("Ok");
        okButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        getContentPane().add(okButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        getContentPane().add(cancelButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSelectedTestsButton;
    private javax.swing.JLabel availableTestsCaption;
    private javax.swing.JList availableTestsList;
    private javax.swing.JScrollPane availableTestsScrollPane;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel chosenTestsCaption;
    private javax.swing.JList chosenTestsList;
    private javax.swing.JScrollPane chosenTestsScrollPane;
    private javax.swing.JButton okButton;
    private javax.swing.JButton removeSelectedButton;
    // End of variables declaration//GEN-END:variables

}
