package com.g2inc.scap.editor.gui.choosers.test;
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

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.g2inc.scap.editor.gui.cellrenderers.tree.oval.test.TestChooserTreeCellRenderer;
import com.g2inc.scap.editor.gui.choosers.Chooser;
import com.g2inc.scap.editor.gui.model.tree.oval.ChooserTreeModel;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.oval.OvalEditorForm;
import com.g2inc.scap.editor.gui.wizards.oval.test.NewTestWizard;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalTest;

/**
 * A chooser, given a source document and optionally a filter, can show you
 * a list of tests from the source document.
 *
 * @author ssill
 */
public class TestChooser extends Chooser implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private OvalDefinitionsDocument odd = null;
    private String typeFilter = null;
    private String platformFilter = null;
    
    /** Creates new form DefinitionChooser */
    public TestChooser(boolean modal)
    {
        super(EditorMainWindow.getInstance(), modal);
        initComponents();
        initComponents2();
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();

        if(src == cancelButton)
        {
            setVisible(false);
        }
        else if(src == okButton)
        {
            cancelled = false;
            setVisible(false);
        }
        else if(src == newButton)
        {
            @SuppressWarnings("unchecked")
			ChooserTreeModel<OvalTest> tm = (ChooserTreeModel<OvalTest>) tree.getModel();
            DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) tm.getRoot();

            NewTestWizard wiz = null;

            if(platformFilter != null)
            {
               wiz = new NewTestWizard(EditorMainWindow.getInstance(), true, odd, platformFilter, typeFilter);
            }
            else
            {
               wiz = new NewTestWizard(EditorMainWindow.getInstance(), true, odd);
            }

            wiz.pack();
            wiz.setVisible(true);

            if(!wiz.wasCancelled())
            {
                OvalTest ot = wiz.getOvalTest();
                odd.add(ot);
                EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);

                String type = ot.getElementName().substring(0,ot.getElementName().indexOf("_"));

                DefaultMutableTreeNode newTestParent
                        = EditorUtil.createSubTreeIfNecessary(tm, treeRoot, type);

                DefaultMutableTreeNode newTestNode = new DefaultMutableTreeNode(ot);

                tm.insertNodeInto(newTestNode, newTestParent, 0);
                tm.reload(treeRoot);

                TreePath pathToNewTest = EditorUtil.getTreePathToChild(newTestNode);

                tree.setSelectionPath(pathToNewTest);
                tree.scrollPathToVisible(pathToNewTest);

                okButton.setEnabled(true);
                chosen = ot;

                newItemAdded = true;

                OvalEditorForm ovalEditor = (OvalEditorForm) EditorMainWindow.getInstance().getActiveEditorForm();

                if(ovalEditor != null)
                {
                    ovalEditor.insertItemInTreeIfNecessary(ot);
                }
            }
        }
    }

    private void initButtons()
    {
        cancelButton.addActionListener(this);
        okButton.addActionListener(this);
        newButton.addActionListener(this);
    }

    private void initTreeListeners()
    {
        tree.addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                TreePath path = tree.getSelectionPath();

                if(path == null)
                {
                    okButton.setEnabled(false);
                    chosen = null;
                }

                Object selected = tree.getLastSelectedPathComponent();

                if(selected == null)
                {
                    return;
                }

                if(!(selected instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode)selected;

                Object userObj = node.getUserObject();
                if(userObj != null && (userObj instanceof OvalTest))
                {
                    okButton.setEnabled(true);
                    chosen = userObj;
                }
                else
                {
                    okButton.setEnabled(false);
                    chosen = null;
                }
            }
        });
    }

    private void initTree()
    {
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new TestChooserTreeCellRenderer());
        ToolTipManager.sharedInstance().registerComponent(tree);
        initTreeListeners();
    }

    private void initTextFields()
    {
        filterTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                List<OvalTest> tests = odd.getMatchingOvalTests(filterTextField.getText());
                ChooserTreeModel<OvalTest> model = new ChooserTreeModel<OvalTest>(typeFilter);
                model.setOvalElementList(tests);
                tree.setModel(model);

                // since no items will now be selected, disable the clone and ok buttons
                okButton.setEnabled(false);
            }

            @Override
            public void insertUpdate(DocumentEvent arg0)
            {
                common(arg0);
            }

            @Override
            public void removeUpdate(DocumentEvent arg0)
            {
                common(arg0);
            }

            @Override
            public void changedUpdate(DocumentEvent arg0)
            {
                common(arg0);
            }
        });
    }

    private void initComponents2()
    {
        initTree();
        initButtons();
        initTextFields();
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

        treePanel = new javax.swing.JPanel();
        treeScrollPane = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        filterCaption = new javax.swing.JLabel();
        filterTextField = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        newButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle("Choose Test");
        setMinimumSize(new java.awt.Dimension(250, 350));
        setModal(true);
        setName("State chooser"); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        treePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tests"));
        treePanel.setLayout(new java.awt.GridBagLayout());

        treeScrollPane.setAutoscrolls(true);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        tree.setAutoscrolls(true);
        tree.setMinimumSize(null);
        treeScrollPane.setViewportView(tree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        treePanel.add(treeScrollPane, gridBagConstraints);

        filterCaption.setText("Filter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 2, 0);
        treePanel.add(filterCaption, gridBagConstraints);

        filterTextField.setMaximumSize(null);
        filterTextField.setMinimumSize(null);
        filterTextField.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 1);
        treePanel.add(filterTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        getContentPane().add(treePanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        newButton.setText("New");
        newButton.setToolTipText("Launch new object wizard");
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        buttonPanel.add(newButton, gridBagConstraints);

        okButton.setText("OK");
        okButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        buttonPanel.add(okButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 10);
        buttonPanel.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        getContentPane().add(buttonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel filterCaption;
    private javax.swing.JTextField filterTextField;
    private javax.swing.JButton newButton;
    private javax.swing.JButton okButton;
    private javax.swing.JTree tree;
    private javax.swing.JPanel treePanel;
    private javax.swing.JScrollPane treeScrollPane;
    // End of variables declaration//GEN-END:variables

    /**
     * Overriding the Chooser baseclass to take the source document and
     * get a list of existing tests to display to the user.
     *
     * @param scapDoc
     * @param typeFilter
     * @param platformFilter
     *
     * @return void
     *
     * @see scap.gui.choosers.Chooser
     */
    @Override
    public void setSource(SCAPDocument scapDoc, String type, String platform )
    {
        odd = (OvalDefinitionsDocument) scapDoc;
        this.typeFilter = type;
        this.platformFilter = platform;

        List<OvalTest> tests = odd.getOvalTests();

        ChooserTreeModel<OvalTest> model = new ChooserTreeModel<OvalTest>(typeFilter);
        model.setOvalElementList(tests);
        model.addTreeModelListener(this);
        tree.setModel(model);
    }

    @Override
    protected JTree getTreeComp()
    {
        return tree;
    }
}
