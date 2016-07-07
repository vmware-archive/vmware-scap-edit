package com.g2inc.scap.editor.gui.choosers.definition;
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

import com.g2inc.scap.editor.gui.cellrenderers.tree.oval.definition.DefinitionChooserTreeCellRenderer;
import com.g2inc.scap.editor.gui.choosers.Chooser;
import com.g2inc.scap.editor.gui.model.tree.oval.DefinitionChooserTreeModel;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;

/**
 * A chooser, given a source document and optionally a filter, can show you
 * a list of definitions from the source document.
 *
 * @author ssill
 */
public class DefinitionChooser extends Chooser implements ActionListener
{
    private com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument odd = null;

    /** Creates new form DefinitionChooser */
    public DefinitionChooser(boolean modal)
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
                    okButton.setEnabled(true);
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
                if(userObj != null && (userObj instanceof OvalDefinition))
                {
                    okButton.setEnabled(true);
                    chosen = userObj;
                }
                else
                {
                    okButton.setEnabled(false);
                    chosen = false;
                }
            }
        });
    }

    private void initTree()
    {
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new DefinitionChooserTreeCellRenderer());
        ToolTipManager.sharedInstance().registerComponent(tree);
        initTreeListeners();
    }

    private void initTextFields()
    {
        filterTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                List<OvalDefinition> definitions = odd.getMatchingOvalDefinitions(filterTextField.getText());
                DefinitionChooserTreeModel model = new DefinitionChooserTreeModel();
                model.setDefinitions(definitions);
                tree.setModel(model);

                // since no items will now be selected, disable the clone and ok buttons
                //cloneButton.setEnabled(false);
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

        setTitle("Choose Definition");
        setMinimumSize(new java.awt.Dimension(512, 384));
        setModal(true);
        setName("Definition chooser"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        treePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Definitions"));
        treePanel.setLayout(new java.awt.GridBagLayout());

        treeScrollPane.setAutoscrolls(true);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        tree.setAutoscrolls(true);
        tree.setLargeModel(true);
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
     * get a list of existing definitions to display to the user.
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
    public void setSource(SCAPDocument scapDoc, String typeFilter, String platformFilter)
    {
        odd = (OvalDefinitionsDocument) scapDoc;

        List<OvalDefinition> definitions = odd.getOvalDefinitions();

        DefinitionChooserTreeModel model = new DefinitionChooserTreeModel(typeFilter);
        model.setDefinitions(definitions);
        model.addTreeModelListener(this);
        tree.setModel(model);
    }

    @Override
    protected JTree getTreeComp()
    {
        return tree;
    }
}
