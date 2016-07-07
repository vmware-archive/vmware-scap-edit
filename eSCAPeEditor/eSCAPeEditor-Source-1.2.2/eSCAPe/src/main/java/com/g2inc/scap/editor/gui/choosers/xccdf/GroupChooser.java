package com.g2inc.scap.editor.gui.choosers.xccdf;
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

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.g2inc.scap.editor.gui.cellrenderers.tree.oval.test.TestChooserTreeCellRenderer;
import com.g2inc.scap.editor.gui.choosers.Chooser;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.xccdf.Group;

/**
 * A chooser, given a source document and optionally a filter, can show you
 * a list of tests from the source document.
 *
 * @author ssill
 */
public class GroupChooser extends Chooser implements ActionListener
{
    private JTree tree = null;
    
    /** Creates new form DefinitionChooser */
    public GroupChooser(boolean modal)
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
    }

    private void initButtons()
    {
        cancelButton.addActionListener(this);
        okButton.addActionListener(this);
    }

    private void initTreeListeners()
    {
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                TreePath path = tree.getSelectionPath();

                if(path == null)
                {
                    okButton.setEnabled(false);
                    chosen = null;
                    return;
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
                if(userObj != null && (userObj instanceof Group))
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
        ruleListScrollPane = new javax.swing.JScrollPane();
        ruleListBox = new javax.swing.JList();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle("Choose Test");
        setMinimumSize(new java.awt.Dimension(250, 350));
        setModal(true);
        setName("State chooser"); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        treePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Rules"));
        treePanel.setLayout(new java.awt.GridBagLayout());

        ruleListScrollPane.setViewportView(ruleListBox);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        treePanel.add(ruleListScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        getContentPane().add(treePanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton okButton;
    private javax.swing.JList ruleListBox;
    private javax.swing.JScrollPane ruleListScrollPane;
    private javax.swing.JPanel treePanel;
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
//        odd = (OvalDefinitionsDocument) scapDoc;
//        this.typeFilter = type;
//        this.platformFilter = platform;
//
//        List<OvalTest> tests = odd.getOvalTests();
//
//        TestChooserTreeModel model = new TestChooserTreeModel(typeFilter);
//        model.setTests(tests);
//        model.addTreeModelListener(this);
//        tree.setModel(model);
    }

    @Override
    protected JTree getTreeComp()
    {
        return tree;
    }
}
