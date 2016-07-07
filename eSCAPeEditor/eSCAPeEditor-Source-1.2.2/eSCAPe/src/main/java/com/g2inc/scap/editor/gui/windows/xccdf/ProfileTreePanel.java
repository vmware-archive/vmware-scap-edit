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

import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.ProfileRefineRuleEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.ProfileRefineValueEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.ProfileSelectEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.ProfileSetValueEditor;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.ChangeNotifierPanel;
import com.g2inc.scap.editor.gui.windows.common.MouseClickListener;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.ProfileChild;
import com.g2inc.scap.library.domain.xccdf.ProfileRefineRule;
import com.g2inc.scap.library.domain.xccdf.ProfileRefineValue;
import com.g2inc.scap.library.domain.xccdf.ProfileSelect;
import com.g2inc.scap.library.domain.xccdf.ProfileSetValue;


public class ProfileTreePanel extends ChangeNotifierPanel implements ChangeListener
{
    private Profile profile = null;
    private JFrame parentWin = null;
    private static Logger log = Logger.getLogger(ProfileTreePanel.class);
    DefaultMutableTreeNode root = null;
    DefaultTreeModel model = null;
    ProfileTreeHandler treeHandler = new ProfileTreeHandler();

    /** Creates new form DefinitionDetailTab */
    public ProfileTreePanel()
    {
        initComponents();
        initTreeListeners();
    }

    private void initTreeListeners()
    {
        profileTree.addMouseListener(new MouseClickListener()
        {

            @Override
            public void rightClicked(MouseEvent me)
            {
                DefaultMutableTreeNode selectedNode =
                                       (DefaultMutableTreeNode) profileTree.getLastSelectedPathComponent();
                if(selectedNode != null)
                {
                    JPopupMenu popup = treeHandler.buildPopupMenu(selectedNode);
                    popup.show(me.getComponent(), me.getX(), me.getY());
                }
            }
            
            @Override
            public void doubleClicked(MouseEvent me)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) profileTree.getLastSelectedPathComponent();
                if (selectedNode == null)
                {
                    return;
                }

                ProfileChild profileChild = (ProfileChild) selectedNode.getUserObject();
                IEditorPage editorPage = null;
                if (profileChild instanceof ProfileSelect)
                {
                    editorPage = new ProfileSelectEditor();
                }
                else if (profileChild instanceof ProfileRefineRule)
                {
                    editorPage = new ProfileRefineRuleEditor();
                }
                else if (profileChild instanceof ProfileRefineValue)
                {
                    editorPage = new ProfileRefineValueEditor();
                }
                else if (profileChild instanceof ProfileSetValue)
                {
                    editorPage = new ProfileSetValueEditor();
                }
                editorPage.setData(profileChild);
                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);

                if (!editor.wasCancelled())
                {
                    DefaultTreeModel model = (DefaultTreeModel) profileTree.getModel();
                    model.reload(selectedNode);
                    notifyRegisteredListeners();
                }
            }
             
        });
        
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

        profileTreeScrollPane = new javax.swing.JScrollPane();
        profileTree = new javax.swing.JTree();

        setLayout(new java.awt.GridBagLayout());

        profileTreeScrollPane.setViewportView(profileTree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(profileTreeScrollPane, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree profileTree;
    private javax.swing.JScrollPane profileTreeScrollPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public JFrame getParentWin()
    {
        return parentWin;
    }

    @Override
    public void setParentWin(JFrame parentWin)
    {
        this.parentWin = parentWin;
    }

    public void setDoc(Profile profile)
    {
        this.profile = profile;
        treeHandler.setProfile(profile);
        treeHandler.setTree(profileTree);
        treeHandler.addChangeListener(this);
        rebuildTree(profile);
    }

    private void rebuildTree(Profile profile)
    {
        root = new DefaultMutableTreeNode(profile);
        model = new DefaultTreeModel(root);
        profileTree.setModel(model);
        List<ProfileChild> children = profile.getChildren();
        for (int i = 0; i < children.size(); i++)
        {
            ProfileChild profileChild = children.get(i);
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(profileChild);
            model.insertNodeInto(childNode, root, i);
        }
        TreePath path = new TreePath(model.getRoot());
        profileTree.expandPath(path);
    }

    @Override
    public void stateChanged(ChangeEvent ce)
    {
        Object src = ce.getSource();
        if(src == treeHandler)
        {
            notifyRegisteredListeners();
        }
    }
}
