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
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.ProfileRefineRuleEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.ProfileRefineValueEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.ProfileSelectEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.xccdf.ProfileSetValueEditor;
import com.g2inc.scap.editor.gui.model.tree.xccdf.TreeHandler;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.SCAPElement;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.ProfileChild;
import com.g2inc.scap.library.domain.xccdf.ProfileRefineRule;
import com.g2inc.scap.library.domain.xccdf.ProfileRefineValue;
import com.g2inc.scap.library.domain.xccdf.ProfileSelect;
import com.g2inc.scap.library.domain.xccdf.ProfileSetValue;


/**
 *
 * @author glenn.strickland
 */
public class ProfileTreeHandler extends TreeHandler
{
    protected static Logger log = Logger.getLogger(ProfileTreeHandler.class);
    private static final String MENU_ITEM_SELECT = "Add Select";
    private static final String MENU_ITEM_REFINE_RULE = "Add Refine Rule";
    private static final String MENU_ITEM_SET_VALUE = "Add Set Value";
    private static final String MENU_ITEM_REFINE_VALUE = "Add Refine Value";
    private Profile profile;
    protected JTree tree;
    private MenuActionListener listener = new MenuActionListener();

    @Override
    public JPopupMenu buildPopupMenu(DefaultMutableTreeNode selectedNode)
    {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem addSelectMenuItem = buildSelectAddMenuItem();
        JMenuItem addRefineRuleMenuItem = buildRefineRuleAddMenuItem();
        JMenuItem addRefineValueMenuItem = buildRefineValueAddMenuItem();
        JMenuItem addSetValueMenuItem = buildSetValueAddMenuItem();
        JMenuItem editMenuItem = buildEditMenuItem();
        JMenuItem removeMenuItem = buildRemoveMenuItem();
        menu.add(addSelectMenuItem);
        menu.add(addRefineRuleMenuItem);
        menu.add(addRefineValueMenuItem);
        menu.add(addSetValueMenuItem);
        menu.add(editMenuItem);
        menu.add(removeMenuItem);

        if (selectedNode.isRoot())
        {
            removeMenuItem.setEnabled(false);
            editMenuItem.setEnabled(false);
        }

        return menu;
    }

    private JMenuItem buildSelectAddMenuItem()
    {
        JMenuItem ret = new JMenuItem(MENU_ITEM_SELECT);
        ret.addActionListener(listener);
        return ret;
    }

    private JMenuItem buildRefineRuleAddMenuItem()
    {
        JMenuItem ret = new JMenuItem("Add Refine Rule");
        ret.addActionListener(listener);
        return ret;
    }

    private JMenuItem buildRefineValueAddMenuItem()
    {
        JMenuItem ret = new JMenuItem("Add Refine Value");
        ret.addActionListener(listener);
        return ret;
    }

    private JMenuItem buildSetValueAddMenuItem()
    {
        JMenuItem ret = new JMenuItem("Add Set Value");
        ret.addActionListener(listener);
        return ret;
    }

    private JMenuItem buildRemoveMenuItem()
    {
        JMenuItem ret = new JMenuItem("Remove");

        ret.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                DefaultMutableTreeNode pNode = (DefaultMutableTreeNode) selectedNode.getParent();
                ((SCAPElement) selectedNode.getUserObject()).getElement().detach();
                model.removeNodeFromParent(selectedNode);
                notifyRegisteredListeners();
            }
        });

        return ret;
    }

    private JMenuItem buildEditMenuItem()
    {
        JMenuItem ret = new JMenuItem("Edit");
        ret.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
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
                    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                    model.reload(selectedNode);
                    notifyRegisteredListeners();
                }
            }
        });
        return ret;
    }

    public void setProfile(Profile profile)
    {
        this.profile = profile;
    }

    public void setTree(JTree tree)
    {
        this.tree = tree;
    }

    private class MenuActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            ProfileChild profileChild = null;
            JMenuItem menuItem = (JMenuItem) arg0.getSource();
            if (menuItem == null)
            {
                return;  // should not occur
            }
            IEditorPage editorPage = null;
            if (menuItem.getText().equals(MENU_ITEM_SELECT))
            {
                profileChild = profile.createProfileSelect();
                editorPage = new ProfileSelectEditor();
            }
            else if (menuItem.getText().equals(MENU_ITEM_REFINE_RULE))
            {
                profileChild = profile.createProfileRefineRule();
                editorPage = new ProfileRefineRuleEditor();
            }
            else if (menuItem.getText().equals(MENU_ITEM_REFINE_VALUE))
            {
                profileChild = profile.createProfileRefineValue();
                editorPage = new ProfileRefineValueEditor();
            }
            else if (menuItem.getText().equals(MENU_ITEM_SET_VALUE))
            {
                profileChild = profile.createProfileSetValue();
                editorPage = new ProfileSetValueEditor();
            }
            else
            {
                return;
            }
            log.debug("New ProfileChild is " + profileChild.getClass().getName());
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            editorPage.setData(profileChild);
            editor.setEditorPage(editorPage);
            editor.pack();
            editor.setLocationRelativeTo(EditorMainWindow.getInstance());
            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                DefaultMutableTreeNode profileNode = (DefaultMutableTreeNode) model.getRoot();
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode == null)
                {
                    return;
                }
                int index = profileNode.getIndex(selectedNode) + 1;  // +1 to insert AFTER selected node; if profile
//                                                                   // is the selected node, index would have been -1,
//                                                                   // so adding +1 will insert new child as first in profile
                List<ProfileChild> profileChildren = profile.getChildren();
                profileChild = (ProfileChild) editorPage.getData();
                // create new DefaultMutableTreeNode to hold new profileChild
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(profileChild);

                // add new DefaultMutableTreeNode into profile TreeNode
                model.insertNodeInto(childNode, profileNode, index);
                // add new profileChild SCAPElement into SCAPElement child List
                profileChildren.add(index, profileChild);
                profile.setChildren(profileChildren);

                model.reload(profileNode);

                notifyRegisteredListeners();
            }
        }
    }
}
