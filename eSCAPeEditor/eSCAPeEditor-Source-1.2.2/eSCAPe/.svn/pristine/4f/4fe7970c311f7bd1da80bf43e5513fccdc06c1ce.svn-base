package com.g2inc.scap.editor.gui.model.tree.xccdf;
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

import java.lang.ref.WeakReference;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import javax.swing.tree.MutableTreeNode;

import com.g2inc.scap.library.domain.xccdf.Group;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.ProfileSelect;
import com.g2inc.scap.library.domain.xccdf.Rule;
import com.g2inc.scap.library.domain.xccdf.Value;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

public class DocumentTreeModel extends DefaultTreeModel
{

    public static final String NODE_PROFILES = "Profiles";
    public static final String NODE_GROUPS = "Groups";
    public static final String NODE_VALUES = "Values";
    public static final String NODE_RULES = "Rules";
    DefaultMutableTreeNode profilesNode = null;
    DefaultMutableTreeNode groupsNode = null;
    DefaultMutableTreeNode valuesNode = null;
    DefaultMutableTreeNode rulesNode = null;
    WeakReference<XCCDFBenchmark> doc = null;

    public DocumentTreeModel(XCCDFBenchmark document)
    {
        super(new DefaultMutableTreeNode("No document loaded"));

        if(document != null)
        {
            this.doc = new WeakReference<XCCDFBenchmark>(document);

            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) root;

            rootNode.setUserObject(doc.get());
            nodeChanged(rootNode);

            List<Profile> profiles = doc.get().getProfileList();
            profilesNode = new DefaultMutableTreeNode("Profiles(" + profiles.size() + ")");

            insertNodeInto(profilesNode, rootNode, 0);

            for (int iProfile = 0; iProfile < profiles.size(); iProfile++)
            {
                Profile profile = profiles.get(iProfile);
                DefaultMutableTreeNode profileNode = new DefaultMutableTreeNode(profile);

                insertNodeInto(profileNode, profilesNode, iProfile);

                List<ProfileSelect> selects = profile.getSelectList();
                for (int iSelect = 0; iSelect < selects.size(); iSelect++)
                {
                    ProfileSelect select = selects.get(iSelect);
                    DefaultMutableTreeNode selectNode = new DefaultMutableTreeNode(select);
                    insertNodeInto(selectNode, profileNode, iSelect);
                }
            }

            profiles.clear();
            profiles = null;

            List<Value> valueList = doc.get().getValueList();
            if (valueList.size() > 0)
            {
                valuesNode = new DefaultMutableTreeNode("Values(0)");
                insertNodeInto(valuesNode, rootNode, rootNode.getChildCount());
                showValueList(valueList, valuesNode);
            }

            valueList.clear();
            valueList = null;

            groupsNode = new DefaultMutableTreeNode("Groups(0)");
            List<Group> groups = doc.get().getGroupList();
            insertNodeInto(groupsNode, rootNode, 1);
            showGroupList(groups, groupsNode);

            List<Rule> ruleList = doc.get().getRuleList();
            if (ruleList.size() > 0)
            {
                rulesNode = new DefaultMutableTreeNode("Rules(0)");
                insertNodeInto(rulesNode, rootNode, rootNode.getChildCount());
                showRuleList(ruleList, rulesNode);
            }

            ruleList.clear();
            ruleList = null;
        }
    }

    private void showRuleList(List<Rule> list, DefaultMutableTreeNode parent)
    {
        for (int i = 0; i < list.size(); i++)
        {
            Rule rule = (Rule) list.get(i);
            DefaultMutableTreeNode ruleNode = new DefaultMutableTreeNode(rule);
            insertNodeInto(ruleNode, parent, i);
        }
        updateParentCount(parent, list.size());
    }

    private void showValueList(List<Value> list, DefaultMutableTreeNode parent)
    {
        for (int i = 0; i < list.size(); i++)
        {
            Value value = (Value) list.get(i);
            DefaultMutableTreeNode ruleNode = new DefaultMutableTreeNode(value);
            insertNodeInto(ruleNode, parent, i);
        }
        updateParentCount(parent, list.size());
    }

    private void showGroupList(List<Group> list, DefaultMutableTreeNode parent)
    {
        for (int i = 0; i < list.size(); i++)
        {
            Group group = (Group) list.get(i);
            DefaultMutableTreeNode groupNode = buildGroupNode(group);
            insertNodeInto(groupNode, parent, i);
        }
        updateParentCount(parent, list.size());
    }

    private void updateParentCount(DefaultMutableTreeNode parent, int count)
    {
        Object parUserObj = parent.getUserObject();
        if (parUserObj != null)
        {
            if (parUserObj instanceof String)
            {
                String fullString = (String) parUserObj;
                int loc = fullString.indexOf("(");
                if (loc != -1)
                {
                    String partialString = fullString.substring(0, loc);
                    parent.setUserObject(partialString + "(" + count + ")");
                    nodeChanged(parent);
                }
            }
        }
    }

    private DefaultMutableTreeNode buildGroupNode(Group group)
    {
        DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(group);
        List<Value> valueList = group.getValueList();
        if (valueList.size() > 0)
        {
            DefaultMutableTreeNode valsNode = new DefaultMutableTreeNode("Values(0)");
            insertNodeInto(valsNode, groupNode, groupNode.getChildCount());
            showValueList(valueList, valsNode);
        }
        List<Rule> ruleList = group.getRuleList();
        if (ruleList.size() > 0)
        {
            rulesNode = new DefaultMutableTreeNode("Rules(0)");
            insertNodeInto(rulesNode, groupNode, groupNode.getChildCount());
            showRuleList(ruleList, rulesNode);
        }
        List<Group> groupList = group.getGroupList();
        if (groupList.size() > 0)
        {
            showGroupList(groupList, groupNode);
        }
        return groupNode;
    }

    @Override
    public Object getChild(Object parent, int index)
    {
        if (parent != null && parent instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode tn = (DefaultMutableTreeNode) parent;

            return tn.getChildAt(index);
        }

        return null;
    }

    @Override
    public int getChildCount(Object parent)
    {
        if (parent != null && parent instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode tn = (DefaultMutableTreeNode) parent;

            return tn.getChildCount();
        }

        return 0;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child)
    {
        if (parent instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode pNode = (DefaultMutableTreeNode) parent;

            if (child instanceof TreeNode)
            {
                return pNode.getIndex((TreeNode) child);
            }
        }

        return 0;
    }

    @Override
    public Object getRoot()
    {
        return root;
    }

    @Override
    public boolean isLeaf(Object node)
    {
        if (node instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) node;
            return treenode.isLeaf();
        }

        return false;
    }

    public void cleanUp()
    {
        if(root != null)
        {
            ((DefaultMutableTreeNode)root).removeAllChildren();

            root = null;
        }

        doc = null;
    }

    @Override
    public void nodeStructureChanged(TreeNode node)
    {
        DefaultMutableTreeNode mutableNode = (DefaultMutableTreeNode) node;
        Object userObj = mutableNode.getUserObject();

        if(userObj != null)
        {
            if(userObj instanceof Profile)
            {
                Profile profile = (Profile) userObj;

                // remove any children so they can be built again from the profile
                int childCount = mutableNode.getChildCount();
                if(childCount > 0)
                {
                    while(childCount > 0)
                    {
                        TreeNode childNode = mutableNode.getChildAt(0);

                        removeNodeFromParent((MutableTreeNode)childNode);
                        childCount--;
                    }
                }

                // now build children from the profile
                List<ProfileSelect> selects = profile.getSelectList();
                for (int iSelect = 0; iSelect < selects.size(); iSelect++)
                {
                    ProfileSelect select = selects.get(iSelect);
                    DefaultMutableTreeNode selectNode = new DefaultMutableTreeNode(select);
                    insertNodeInto(selectNode, mutableNode, iSelect);
                }
            }
        }
        super.nodeStructureChanged(node);
    }
}
