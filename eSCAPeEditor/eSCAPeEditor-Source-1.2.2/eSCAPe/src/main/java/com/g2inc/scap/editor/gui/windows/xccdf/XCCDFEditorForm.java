package com.g2inc.scap.editor.gui.windows.xccdf;

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
import java.lang.ref.WeakReference;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.content.style.ContentStyle;
import com.g2inc.scap.library.content.style.ContentStyleViolationElement;
import com.g2inc.scap.editor.gui.model.tree.xccdf.DocumentTreeModel;
import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorForm;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.GenericSourceDetailTab;
import com.g2inc.scap.editor.gui.windows.common.WeakTreeModelListener;
import com.g2inc.scap.editor.gui.wizards.xccdf.group.create.CreateXCCDFGroupWizard;
import com.g2inc.scap.editor.gui.wizards.xccdf.profile.create.CreateXCCDFProfileWizard;
import com.g2inc.scap.editor.gui.wizards.xccdf.rule.create.CreateXCCDFRuleWizard;
import com.g2inc.scap.editor.gui.wizards.xccdf.value.create.CreateXCCDFValueWizard;
import com.g2inc.scap.library.content.style.ContentStyleRegistry;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.xccdf.Group;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.Rule;
import com.g2inc.scap.library.domain.xccdf.Value;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

/**
 *
 * @author gstrickland
 */
public class XCCDFEditorForm extends EditorForm implements TreeModelListener, ActionListener, ChangeListener

{
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(XCCDFEditorForm.class);

    private WeakReference<EditorMainWindow> parentWin = null;
    private WeakReference<XCCDFBenchmark> document = null;

    private ContentStyle currentStyle = null;

    public static final String WINDOW_TITLE_BASE = EditorMessages.XCCDF + " Document - ";

    @Override
    public void stateChanged(ChangeEvent ce)
    {
        Object src = ce.getSource();

        if(src == stylePicker)
        {
            refreshStyleMessages();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();

        if(src == expandAllButton)
        {
            Object root = structTree.getModel().getRoot();

            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) root;

            Enumeration<DefaultMutableTreeNode> nodeEnum = rootNode.depthFirstEnumeration();
            while (nodeEnum.hasMoreElements())
            {
                TreePath tp = EditorUtil.getTreePathToChild(nodeEnum.nextElement());

                structTree.expandPath(tp);
            }
        }
        else if(src == collapseAllButton)
        {
            Object root = structTree.getModel().getRoot();

            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) root;

            Enumeration<DefaultMutableTreeNode> nodeEnum = rootNode.depthFirstEnumeration();
            while (nodeEnum.hasMoreElements())
            {
                TreePath tp = EditorUtil.getTreePathToChild(nodeEnum.nextElement());

                structTree.collapsePath(tp);
            }
        }
    }

    private void initStylePanel()
    {
        contentStylePanel.addChangeListener(this);
    }

    private void initStylePicker()
    {
        stylePicker.addChangeListener(this);
        currentStyle = stylePicker.getChosenStyle();
    }

    private void initButtons()
    {
        expandAllButton.addActionListener(this);
        collapseAllButton.addActionListener(this);
    }

    private void initComponents2()
    {
        initTree();
        initButtons();
        List<ContentStyle> styles = ContentStyleRegistry.getInstance().getAvailableStyles();
        if (styles.isEmpty() || styles.size() == 1) {
            // if no styles, or only default styles are present, don't show style picker
            // or style messages panel.
            bottomPanel.remove(contentStylePanel);
            docStyleSplitPane.remove(bottomPanel);
            stylePickerPanel.remove(stylePicker);
            topPanel.remove(stylePickerPanel);
        } else {
            initStylePanel();
            initStylePicker();
        }
    }

    /** Creates new form OvalEditorForm */
    public XCCDFEditorForm()
    {
        initComponents();
        initComponents2();
    }

    private void initTree()
    {
        ToolTipManager.sharedInstance().registerComponent(structTree);
        structTree.setScrollsOnExpand(true);

        structTree.addTreeSelectionListener(new XCCDFTreeSelectionListener());
        structTree.addMouseListener(new XCCDFTreeMouseListener());
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

        jSeparator1 = new javax.swing.JSeparator();
        docStyleSplitPane = new javax.swing.JSplitPane();
        topPanel = new javax.swing.JPanel();
        stylePickerPanel = new javax.swing.JPanel();
        stylePicker = new com.g2inc.scap.editor.gui.windows.common.content.ContentStylePicker();
        treeTabsSplitPane = new javax.swing.JSplitPane();
        treePanel = new javax.swing.JPanel();
        treeScrollPane = new javax.swing.JScrollPane();
        structTree = new javax.swing.JTree();
        collapseAllButton = new javax.swing.JButton();
        expandAllButton = new javax.swing.JButton();
        tabPanel = new javax.swing.JPanel();
        detailTabPane = new javax.swing.JTabbedPane();
        bottomPanel = new javax.swing.JPanel();
        contentStylePanel = new com.g2inc.scap.editor.gui.windows.common.content.ContentStyleMessagesPanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(800, 700));
        setPreferredSize(new java.awt.Dimension(1024, 768));
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jSeparator1, gridBagConstraints);

        docStyleSplitPane.setDividerLocation(400);
        docStyleSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        topPanel.setLayout(new java.awt.GridBagLayout());

        stylePickerPanel.setLayout(new java.awt.GridBagLayout());
        stylePickerPanel.add(stylePicker, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 4);
        topPanel.add(stylePickerPanel, gridBagConstraints);

        treeTabsSplitPane.setDividerLocation(240);

        treePanel.setLayout(new java.awt.GridBagLayout());

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        structTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        structTree.setMinimumSize(null);
        treeScrollPane.setViewportView(structTree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        treePanel.add(treeScrollPane, gridBagConstraints);

        collapseAllButton.setText("Collapse All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        treePanel.add(collapseAllButton, gridBagConstraints);

        expandAllButton.setText("Expand All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        treePanel.add(expandAllButton, gridBagConstraints);

        treeTabsSplitPane.setLeftComponent(treePanel);

        tabPanel.setLayout(new java.awt.GridBagLayout());

        detailTabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        tabPanel.add(detailTabPane, gridBagConstraints);

        treeTabsSplitPane.setRightComponent(tabPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        topPanel.add(treeTabsSplitPane, gridBagConstraints);

        docStyleSplitPane.setLeftComponent(topPanel);

        bottomPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        bottomPanel.add(contentStylePanel, gridBagConstraints);

        docStyleSplitPane.setRightComponent(bottomPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(docStyleSplitPane, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton collapseAllButton;
    private com.g2inc.scap.editor.gui.windows.common.content.ContentStyleMessagesPanel contentStylePanel;
    private javax.swing.JTabbedPane detailTabPane;
    private javax.swing.JSplitPane docStyleSplitPane;
    private javax.swing.JButton expandAllButton;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTree structTree;
    private com.g2inc.scap.editor.gui.windows.common.content.ContentStylePicker stylePicker;
    private javax.swing.JPanel stylePickerPanel;
    private javax.swing.JPanel tabPanel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel treePanel;
    private javax.swing.JScrollPane treeScrollPane;
    private javax.swing.JSplitPane treeTabsSplitPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public SCAPDocument getDocument()
    {
        return (SCAPDocument) document.get();
    }

    public void setDocument(XCCDFBenchmark doc)
    {
        this.document = new WeakReference<XCCDFBenchmark>(doc);
        DocumentTreeModel tm = new DocumentTreeModel(doc);
        tm.addTreeModelListener(new WeakTreeModelListener(this));
        structTree.setModel(tm);
        structTree.setSelectionPath(new TreePath(tm.getRoot()));
        refreshStyleMessages();
    }

    public JFrame getParentWin()
    {
        return parentWin.get();
    }

    public void setParentWin(EditorMainWindow parentWin)
    {
        this.parentWin = new WeakReference<EditorMainWindow>(parentWin);
    }

    @Override
    public void treeNodesChanged(TreeModelEvent tme)
    {
        log.trace("Entering treeNodesChanged(TreeModelEvent tme)");
        treeNodeCountUpdate(new TreePath(structTree.getModel().getRoot()));
    }

    @Override
    public void treeNodesInserted(TreeModelEvent tme)
    {
        log.trace("Entering treeNodesInserted(TreeModelEvent tme)");
        treeNodeCountUpdate(tme.getTreePath());
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent tme)
    {
        log.trace("Entering treeNodesRemoved(TreeModelEvent tme)");
        treeNodeCountUpdate(tme.getTreePath());
    }

    @Override
    public void treeStructureChanged(TreeModelEvent tme)
    {
        log.trace("Entering treeStructureChanged(TreeModelEvent tme)");
        treeNodeCountUpdate(tme.getTreePath());
    }

    private void treeNodeCountUpdate(TreePath path)
    {
        Object last = path.getLastPathComponent();

        if (last instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) last;

            Object userObj = node.getUserObject();

            if (userObj instanceof String)
            {
                String name = (String) userObj;

                int loc = name.indexOf("(");

                if (loc > -1)
                {
                    String basename = name.substring(0, loc);

                    DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

                    node.setUserObject(basename + "(" + node.getChildCount() + ")");
                    dtm.nodeChanged(node);

                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

                    if (parent != null)
                    {
                        treeNodeCountUpdate(new TreePath(parent.getPath()));
                    }
                }
            }
        }
    }

    public void removeDetailTabs()
    {
        while (detailTabPane.getTabCount() > 0)
        {
            detailTabPane.removeTabAt(0);
        }
        detailTabPane.repaint();
    }

    @Override
    public void setSelectedElement(TreePath selectionPath)
    {
        if (selectionPath != null)
        {
            structTree.setSelectionPath(selectionPath);
            structTree.scrollPathToVisible(selectionPath);
        }
    }

    public void refreshProfileNode(Profile prof)
    {
        DefaultMutableTreeNode profilesNode = getBenchmarkProfilesNode();
        DocumentTreeModel model = (DocumentTreeModel) structTree.getModel();
        if(profilesNode != null)
        {
            int childCount = profilesNode.getChildCount();

            if(childCount > 0)
            {
                for(int x = 0 ; x < childCount ; x++)
                {
                    DefaultMutableTreeNode child = (DefaultMutableTreeNode) profilesNode.getChildAt(x);

                    Object userObj = child.getUserObject();

                    if(prof == userObj)
                    {
                        // this is the node
                        model.nodeStructureChanged(child);
                        model.reload(child);
                    }
                }
            }
        }
    }

    @Override
    public TreePath getSelectedPath()
    {
        return structTree.getSelectionPath();
    }

    public JTabbedPane getDetailTabPane()
    {
        return detailTabPane;
    }

    private DefaultMutableTreeNode getBenchmarkGroupsNode()
    {
        DefaultMutableTreeNode groupsNode = null;

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) structTree.getModel().getRoot();

        int childCount = rootNode.getChildCount();

        for (int x = 0; x < childCount; x++)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) rootNode.getChildAt(x);

            Object userObj = child.getUserObject();

            if (userObj == null)
            {
                continue;
            }

            if (userObj instanceof String)
            {
                String val = (String) userObj;

                if (val.startsWith("Groups"))
                {
                    groupsNode = child;
                    break;
                }
            }
        }

        return groupsNode;
    }

    private DefaultMutableTreeNode getBenchmarkProfilesNode()
    {
        DefaultMutableTreeNode profilesNode = null;

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) structTree.getModel().getRoot();

        int childCount = rootNode.getChildCount();

        for (int x = 0; x < childCount; x++)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) rootNode.getChildAt(x);

            Object userObj = child.getUserObject();

            if (userObj == null)
            {
                continue;
            }

            if (userObj instanceof String)
            {
                String val = (String) userObj;

                if (val.startsWith("Profiles"))
                {
                    profilesNode = child;
                    break;
                }
            }
        }

        return profilesNode;
    }

    private DefaultMutableTreeNode getBenchmarkRulesNode()
    {
        DefaultMutableTreeNode profilesNode = null;

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) structTree.getModel().getRoot();

        int childCount = rootNode.getChildCount();

        for (int x = 0; x < childCount; x++)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) rootNode.getChildAt(x);

            Object userObj = child.getUserObject();

            if (userObj == null)
            {
                continue;
            }

            if (userObj instanceof String)
            {
                String val = (String) userObj;

                if (val.startsWith("Rules"))
                {
                    profilesNode = child;
                    break;
                }
            }
        }

        return profilesNode;
    }

    private JMenuItem buildAddGroupMenuItem()
    {
        JMenuItem item = new JMenuItem("Add Group");

        item.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) structTree.getLastSelectedPathComponent();
                if (selectedNode == null)
                {
                    return;
                }

                CreateXCCDFGroupWizard wiz = new CreateXCCDFGroupWizard(true, document.get());
                wiz.pack();
                wiz.validate();
                wiz.setLocationRelativeTo(EditorMainWindow.getInstance());

                wiz.setVisible(true);

                if (!wiz.wasCancelled())
                {
                    // figure out where we are adding this group based on the selected node in the tree
                    Object selUserObj = selectedNode.getUserObject();
                    DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

                    DefaultMutableTreeNode newGroupNode = new DefaultMutableTreeNode(wiz.getGroup());

                    if (selUserObj instanceof String)
                    {
                        // selected Node has something like "Groups" as it's user object.
                        // we need it's parent to add this new group to.
                        String val = (String) selUserObj;

                        if (val == null || !val.startsWith("Groups"))
                        {
                            return;
                        }

                        // the way the gui is implemented, parent can only be a benchmark
                        document.get().addGroup(wiz.getGroup());

                        selectedNode.add(newGroupNode);
                        dtm.nodeStructureChanged(selectedNode);
                        TreePath path = EditorUtil.getTreePathToChild(newGroupNode);
                        structTree.setSelectionPath(path);
                    } else if (selUserObj instanceof XCCDFBenchmark)
                    {
                        document.get().addGroup(wiz.getGroup());

                        DefaultMutableTreeNode groupsNode = getBenchmarkGroupsNode();

                        if (groupsNode == null)
                        {
                            // we need to create one
                            groupsNode = new DefaultMutableTreeNode("Groups(0)");

                            selectedNode.add(groupsNode);
                            dtm.nodeStructureChanged(selectedNode);
                        }

                        groupsNode.add(newGroupNode);
                        dtm.nodeStructureChanged(groupsNode);

                        TreePath path = EditorUtil.getTreePathToChild(newGroupNode);
                        structTree.setSelectionPath(path);
                    } else if (selUserObj instanceof Group)
                    {
                        Group existing = (Group) selUserObj;
                        existing.addGroup(wiz.getGroup());

                        selectedNode.add(newGroupNode);
                        dtm.nodeStructureChanged(selectedNode);
                        TreePath path = EditorUtil.getTreePathToChild(newGroupNode);
                        structTree.setSelectionPath(path);
                    }

                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                }
            }
        });
        return item;
    }

    private JMenuItem buildAddRuleMenuItem()
    {
        JMenuItem item = new JMenuItem("Add Rule");

        item.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) structTree.getLastSelectedPathComponent();
                if (selectedNode == null)
                {
                    return;
                }

                CreateXCCDFRuleWizard wiz = new CreateXCCDFRuleWizard(true, document.get());
                wiz.pack();
                wiz.validate();
                wiz.setLocationRelativeTo(EditorMainWindow.getInstance());

                wiz.setVisible(true);

                if (!wiz.wasCancelled())
                {
                    // figure out where we are adding this group based on the selected node in the tree
                    Object selUserObj = selectedNode.getUserObject();
                    DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

                    DefaultMutableTreeNode newRuleNode = new DefaultMutableTreeNode(wiz.getRule());

                    if (selUserObj instanceof String)
                    {
                        // selected Node has something like "Groups" as it's user object.
                        // we need it's parent to add this new group to.
                        String val = (String) selUserObj;

                        if (val == null || !val.startsWith("Rules"))
                        {
                            return;
                        }

                        // the way the gui is implemented, parent can only be a benchmark
                        document.get().addRule(wiz.getRule());

                        selectedNode.add(newRuleNode);
                        dtm.nodeStructureChanged(selectedNode);
                        TreePath path = EditorUtil.getTreePathToChild(newRuleNode);
                        structTree.setSelectionPath(path);
                    } else if (selUserObj instanceof XCCDFBenchmark)
                    {
                        document.get().addRule(wiz.getRule());

                        DefaultMutableTreeNode rulesNode = getBenchmarkRulesNode();

                        if (rulesNode == null)
                        {
                            // we need to create one
                            rulesNode = new DefaultMutableTreeNode("Rules(0)");

                            selectedNode.add(rulesNode);
                            dtm.nodeStructureChanged(selectedNode);
                        }

                        rulesNode.add(newRuleNode);
                        dtm.nodeStructureChanged(rulesNode);

                        TreePath path = EditorUtil.getTreePathToChild(newRuleNode);
                        structTree.setSelectionPath(path);
                    } else if (selUserObj instanceof Group)
                    {
                        Group existing = (Group) selUserObj;
                        existing.addRule(wiz.getRule());

                        DefaultMutableTreeNode rulesNode = null;
                        for (int x = 0; x < selectedNode.getChildCount(); x++)
                        {
                            DefaultMutableTreeNode child = (DefaultMutableTreeNode) selectedNode.getChildAt(x);

                            Object chuo = child.getUserObject();

                            if (chuo != null)
                            {
                                if (chuo instanceof String)
                                {
                                    String chuoVal = (String) chuo;

                                    if (chuoVal.startsWith("Rules("))
                                    {
                                        rulesNode = child;
                                        break;
                                    }
                                }
                            }
                        }

                        if (rulesNode == null)
                        {
                            rulesNode = new DefaultMutableTreeNode("Rules(0)");
                            selectedNode.add(rulesNode);
                            dtm.nodeStructureChanged(selectedNode);
                        }

                        rulesNode.add(newRuleNode);
                        dtm.nodeStructureChanged(rulesNode);
                        TreePath path = EditorUtil.getTreePathToChild(newRuleNode);
                        structTree.setSelectionPath(path);
                    }

                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                }
            }
        });
        return item;
    }

    private JMenuItem buildAddValueMenuItem()
    {
        JMenuItem item = new JMenuItem("Add Value");

        item.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) structTree.getLastSelectedPathComponent();
                if (selectedNode == null)
                {
                    return;
                }

                CreateXCCDFValueWizard wiz = new CreateXCCDFValueWizard(true, document.get());
                wiz.pack();
                wiz.validate();
                wiz.setLocationRelativeTo(EditorMainWindow.getInstance());

                wiz.setVisible(true);

                if (!wiz.wasCancelled())
                {
                    // figure out where we are adding this group based on the selected node in the tree
                    Object selUserObj = selectedNode.getUserObject();
                    DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

                    DefaultMutableTreeNode newValueNode = new DefaultMutableTreeNode(wiz.getValue());

                    if (selUserObj instanceof String)
                    {
                        // selected Node has something like "Groups" as it's user object.
                        // we need it's parent to add this new group to.
                        String val = (String) selUserObj;

                        if (val == null || !val.startsWith("Values"))
                        {
                            return;
                        }

                        // the way the gui is implemented, parent can only be a benchmark
                        document.get().addValue(wiz.getValue());

                        selectedNode.add(newValueNode);
                        dtm.nodeStructureChanged(selectedNode);
                        TreePath path = EditorUtil.getTreePathToChild(newValueNode);
                        structTree.setSelectionPath(path);
                    } else if (selUserObj instanceof XCCDFBenchmark)
                    {
                        document.get().addValue(wiz.getValue());

                        DefaultMutableTreeNode rulesNode = getBenchmarkRulesNode();

                        if (rulesNode == null)
                        {
                            // we need to create one
                            rulesNode = new DefaultMutableTreeNode("Values(0)");

                            selectedNode.add(rulesNode);
                            dtm.nodeStructureChanged(selectedNode);
                        }

                        rulesNode.add(newValueNode);
                        dtm.nodeStructureChanged(rulesNode);

                        TreePath path = EditorUtil.getTreePathToChild(newValueNode);
                        structTree.setSelectionPath(path);
                    } else if (selUserObj instanceof Group)
                    {
                        Group existing = (Group) selUserObj;
                        existing.addValue(wiz.getValue());

                        DefaultMutableTreeNode valuesNode = null;
                        for (int x = 0; x < selectedNode.getChildCount(); x++)
                        {
                            DefaultMutableTreeNode child = (DefaultMutableTreeNode) selectedNode.getChildAt(x);

                            Object chuo = child.getUserObject();

                            if (chuo != null)
                            {
                                if (chuo instanceof String)
                                {
                                    String chuoVal = (String) chuo;

                                    if (chuoVal.startsWith("Values("))
                                    {
                                        valuesNode = child;
                                        break;
                                    }
                                }
                            }
                        }

                        if (valuesNode == null)
                        {
                            valuesNode = new DefaultMutableTreeNode("Values(0)");
                            selectedNode.add(valuesNode);
                            dtm.nodeStructureChanged(selectedNode);
                        }

                        valuesNode.add(newValueNode);
                        dtm.nodeStructureChanged(valuesNode);
                        TreePath path = EditorUtil.getTreePathToChild(newValueNode);
                        structTree.setSelectionPath(path);
                    }

                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                }
            }
        });
        return item;
    }

    private JMenuItem buildAddProfileMenuItem()
    {
        JMenuItem item = new JMenuItem("Add Profile");

        item.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) structTree.getLastSelectedPathComponent();
                if (selectedNode == null)
                {
                    return;
                }

                CreateXCCDFProfileWizard wiz = new CreateXCCDFProfileWizard(true, document.get());
                wiz.pack();
                wiz.validate();
                wiz.setLocationRelativeTo(EditorMainWindow.getInstance());

                wiz.setVisible(true);

                if (!wiz.wasCancelled())
                {
                    boolean modified = false;

                    // figure out where we are adding this group based on the selected node in the tree
                    Object selUserObj = selectedNode.getUserObject();
                    DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

                    DefaultMutableTreeNode newProfileNode = new DefaultMutableTreeNode(wiz.getProfile());

                    if (selUserObj instanceof String)
                    {
                        // selected Node has something like "Groups" as it's user object.
                        // we need it's parent to add this new group to.
                        String val = (String) selUserObj;

                        if (val == null || !val.startsWith("Profiles"))
                        {
                            return;
                        }

                        // the way the gui is implemented, parent can only be a benchmark
                        document.get().addProfile(wiz.getProfile());
                        modified = true;

                        selectedNode.add(newProfileNode);
                        dtm.nodeStructureChanged(selectedNode);
                        TreePath path = EditorUtil.getTreePathToChild(newProfileNode);
                        structTree.setSelectionPath(path);
                    } else if (selUserObj instanceof XCCDFBenchmark)
                    {
                        document.get().addProfile(wiz.getProfile());
                        modified = true;

                        DefaultMutableTreeNode profilesNode = getBenchmarkProfilesNode();

                        if (profilesNode == null)
                        {
                            // we need to create one
                            profilesNode = new DefaultMutableTreeNode("Profiles(0)");

                            selectedNode.add(profilesNode);
                        }

                        profilesNode.add(newProfileNode);
                        dtm.nodeStructureChanged(profilesNode);

                        TreePath path = EditorUtil.getTreePathToChild(newProfileNode);
                        structTree.expandPath(path);
                        structTree.setSelectionPath(path);
                    }

                    if (modified)
                    {
                        EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                    }
                }
            }
        });
        return item;
    }

    /**
     * To be called when the user right-clicks on the Groups tree item.  Groups
     * is really talking about groups defined directly under the benchmark, not
     * groups nested below that.
     * 
     * @return JPopupMenu
     */
    private JPopupMenu buildGroupsPopup()
    {
        JPopupMenu popup = new JPopupMenu();

        // for adding a group to the benchmark
        JMenuItem item = buildAddGroupMenuItem();
        popup.add(item);

        return popup;
    }

    /**
     * To be called when the user right-clicks on the Profiles tree item.
     *
     * @return JPopupMenu
     */
    private JPopupMenu buildProfilesPopup()
    {
        JPopupMenu popup = new JPopupMenu();

        // for adding a group to the benchmark
        JMenuItem item = buildAddProfileMenuItem();
        popup.add(item);

        return popup;
    }

    /**
     * To be called when the user right-clicks on a group tree item
     *
     * @return JPopupMenu
     */
    private JPopupMenu buildGroupPopup()
    {
        JPopupMenu popup = new JPopupMenu();

//        JMenuItem removeGroupMenuItem = new JMenuItem("Remove (Not Implemented)");
//        removeGroupMenuItem.setEnabled(false);
//        popup.add(removeGroupMenuItem);
//        removeGroupMenuItem.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(ActionEvent e)
//            {
//            }
//        });

        JMenuItem addGroupMenuItem = buildAddGroupMenuItem();
        popup.add(addGroupMenuItem);

        JMenuItem addRuleMenuItem = buildAddRuleMenuItem();
        popup.add(addRuleMenuItem);

        JMenuItem addValueMenuItem = buildAddValueMenuItem();
        popup.add(addValueMenuItem);

        return popup;
    }

    /**
     * To be called when the user right-clicks on a benchmark tree item
     *
     * @return JPopupMenu
     */
    private JPopupMenu buildBenchmarkPopup()
    {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem addGroupMenuItem = buildAddGroupMenuItem();
        popup.add(addGroupMenuItem);

        JMenuItem addProfileMenuItem = buildAddProfileMenuItem();
        popup.add(addProfileMenuItem);

        JMenuItem addRuleMenuItem = buildAddRuleMenuItem();
        popup.add(addRuleMenuItem);

        JMenuItem addValueMenuItem = buildAddValueMenuItem();
        popup.add(addValueMenuItem);

        return popup;
    }

    /**
     * To be called when the user right-clicks on a profile tree item
     *
     * @return JPopupMenu
     */
    private JPopupMenu buildProfilePopup()
    {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem item = new JMenuItem("Remove (Not Implemented)");
        item.setEnabled(false);
        popup.add(item);
//        item.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(ActionEvent e)
//            {
//            }
//        });

        return popup;
    }

    /**
     * To be called when the user right-clicks on a value
     *
     * @return JPopupMenu
     */
    private JPopupMenu buildValuePopup()
    {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem item = new JMenuItem("Remove (Not Implemented)");
        item.setEnabled(false);
        popup.add(item);
//        item.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(ActionEvent e)
//            {
//            }
//        });

        return popup;
    }

    /**
     * To be called when the user right-clicks on a Rule
     *
     * @return JPopupMenu
     */
    private JPopupMenu buildRulePopup()
    {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem item = new JMenuItem("Remove (Not Implemented)");
        item.setEnabled(false);
        popup.add(item);
//        item.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(ActionEvent e)
//            {
//            }
//        });

        return popup;
    }

    /**
     * To be called when the user right-clicks on Values node under a group or benchmark
     *
     * @return JPopupMenu
     */
    private JPopupMenu buildValuesPopup()
    {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem addValueMenuItem = buildAddValueMenuItem();
        popup.add(addValueMenuItem);

        return popup;
    }

    /**
     * To be called when the user right-clicks on Rules node under a group or benchmark
     *
     * @return JPopupMenu
     */
    private JPopupMenu buildRulesPopup()
    {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem addRuleMenuItem = buildAddRuleMenuItem();
        popup.add(addRuleMenuItem);

        return popup;
    }

    class XCCDFTreeMouseListener implements MouseInputListener
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            TreePath selPath = structTree.getPathForLocation(e.getX(), e.getY());

            if (selPath == null)
            {
                return;
            }

            structTree.setSelectionPath(selPath);

            Object selected = structTree.getLastSelectedPathComponent();
            if (selected != null && selected instanceof DefaultMutableTreeNode)
            {
                int button = e.getButton();
                boolean isRightClick = (button == MouseEvent.BUTTON2 || button == MouseEvent.BUTTON3);

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selected;
                Object userObj = node.getUserObject();

                if (userObj != null)
                {
                    if (userObj instanceof String)
                    {
                        String nodeText = (String) userObj;

                        if (nodeText.startsWith("Rules"))
                        {
                            if (isRightClick)
                            {
                                JPopupMenu rulesMenu = buildRulesPopup();
                                rulesMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        } else if (nodeText.startsWith("Values"))
                        {

                            if (isRightClick)
                            {
                                JPopupMenu valuesMenu = buildValuesPopup();
                                valuesMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        } else if (nodeText.startsWith("Profiles"))
                        {

                            if (isRightClick)
                            {
                                JPopupMenu profilesMenu = buildProfilesPopup();
                                profilesMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        } else if (nodeText.startsWith("Groups"))
                        {

                            if (isRightClick)
                            {
                                JPopupMenu groupsMenu = buildGroupsPopup();
                                groupsMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
//                    } else if (userObj instanceof Rule)
//                    {
//                        if (isRightClick)
//                        {
//                            JPopupMenu ruleMenu = buildRulePopup();
//                            ruleMenu.show(e.getComponent(), e.getX(), e.getY());
//                        }
                    } else if (userObj instanceof Group)
                    {
                        if (isRightClick)
                        {
                            JPopupMenu groupMenu = buildGroupPopup();
                            groupMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
//                    } else if (userObj instanceof Profile)
//                    {
//                        if (isRightClick)
//                        {
//                            JPopupMenu profileMenu = buildProfilePopup();
//                            profileMenu.show(e.getComponent(), e.getX(), e.getY());
//                        }
//                    } else if (userObj instanceof Value)
//                    {
//                        if (isRightClick)
//                        {
//                            JPopupMenu valueMenu = buildValuePopup();
//                            valueMenu.show(e.getComponent(), e.getX(), e.getY());
//                        }
                    } else if (userObj instanceof XCCDFBenchmark)
                    {
                        if (isRightClick)
                        {
                            JPopupMenu benchmarkMenu = buildBenchmarkPopup();
                            benchmarkMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
        }

        @Override
        public void mouseMoved(MouseEvent e)
        {
        }
    }

    class XCCDFTreeSelectionListener implements TreeSelectionListener
    {
        @Override
        public void valueChanged(TreeSelectionEvent tse)
        {
            Object selected = structTree.getLastSelectedPathComponent();
            if (selected != null && selected instanceof DefaultMutableTreeNode)
            {
                removeDetailTabs();
                Object userObj = ((DefaultMutableTreeNode) selected).getUserObject();
                if (userObj != null)
                {
                    if (userObj instanceof XCCDFBenchmark)
                    {
                        XCCDFBenchmark benchmark = (XCCDFBenchmark) userObj;
                        BenchmarkDetailTab tab = new BenchmarkDetailTab();
                        tab.setDoc(benchmark);
                        tab.setParentWin(getParentWin());
                        tab.setTabs(detailTabPane);
//                        detailTabPane.addTab("General", tab);
                    } else if (userObj instanceof Group)
                    {
                        Group group = (Group) userObj;
                        GenericSourceDetailTab sourceTab = new GenericSourceDetailTab();
                        GroupDetailTab tab = new GroupDetailTab();
                        tab.setSourceTab(sourceTab);
                        tab.setDoc(group);
                        tab.setParentWin(getParentWin());
                        tab.setTabs(detailTabPane);
                        userObj = tab.getDoc();
                        detailTabPane.addTab("Source", sourceTab);
                        sourceTab.setXMLSource(elementToString(group.getElement()));
                    } else if (userObj instanceof Rule)
                    {
                        Rule rule = (Rule) userObj;
                        GenericSourceDetailTab sourceTab = new GenericSourceDetailTab();
                        RuleDetailTab tab = new RuleDetailTab();
                        tab.setSourceTab(sourceTab);
                        tab.setDoc(rule);
                        tab.setParentWin(getParentWin());
                        tab.setTabs(detailTabPane);

                        detailTabPane.addTab("Source", sourceTab);
                        sourceTab.setXMLSource(elementToString(rule.getElement()));
                    } else if (userObj instanceof Value)
                    {
                        Value value = (Value) userObj;
                        GenericSourceDetailTab sourceTab = new GenericSourceDetailTab();
                        ValueDetailTab tab = new ValueDetailTab();
                        tab.setSourceTab(sourceTab);
                        tab.setDoc(value);
                        tab.setParentWin(getParentWin());
                        tab.setTabs(detailTabPane);

                        detailTabPane.addTab("Source", sourceTab);
                        sourceTab.setXMLSource(elementToString(value.getElement()));
                    } else if (userObj instanceof Profile)
                    {
                        Profile profile = (Profile) userObj;
                        GenericSourceDetailTab sourceTab = new GenericSourceDetailTab();
                        ProfileDetailTab tab = new ProfileDetailTab();
                        tab.setSourceTab(sourceTab);
                        tab.setDoc(profile);
                        tab.setParentWin(getParentWin());
                        tab.setTabs(detailTabPane);
                        userObj = tab.getDoc();
                        detailTabPane.addTab("Source", sourceTab);
                        sourceTab.setXMLSource(elementToString(profile.getElement()));
                    } else
                    {
                        removeDetailTabs();
                    }
                } else
                {
                    removeDetailTabs();
                }
            } else
            {
                removeDetailTabs();
            }
        }
    }

    public void cleanUp()
    {
        XCCDFBenchmark benchmark = document.get();
        if(benchmark != null)
        {
            benchmark.close();
        }

        treePanel.remove(structTree);
        changeListeners.clear();

        tabPanel.removeAll();
        treePanel.removeAll();
        ToolTipManager.sharedInstance().unregisterComponent(structTree);

        TreeModel defTreeModel = structTree.getModel();

        if(defTreeModel instanceof DocumentTreeModel)
        {
            DocumentTreeModel tm = (DocumentTreeModel) structTree.getModel();
            tm.cleanUp();
        }
        
        TreeSelectionListener[] treeSelectionListeners = structTree.getTreeSelectionListeners();

        if(treeSelectionListeners != null)
        {
            for(int x = 0 ; x < treeSelectionListeners.length; x++)
            {
                structTree.removeTreeSelectionListener(treeSelectionListeners[x]);
            }
        }

        structTree = null;
    	parentWin = null;
    	document = null;
    }

    private void selectRootNode()
    {
        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) dtm.getRoot();
        TreePath path = EditorUtil.getTreePathToChild(rootNode);

        if (path != null)
        {
            structTree.clearSelection();
            structTree.setSelectionPath(path);
        }


    }

    @Override
    public void refreshRootNode()
    {
        selectRootNode();
    }

    private void refreshStyleMessages()
    {
        ContentStyle style = (ContentStyle) stylePicker.getChosenStyle();

        currentStyle = style;

        if (currentStyle != null) {
	        List<ContentStyleViolationElement> violations = currentStyle.checkDocument((SCAPDocument) getDocument());
	        contentStylePanel.setData(violations);
        }
    }

    @Override
    public void setDirty(boolean b)
    {
        super.setDirty(b);
        if(currentStyle != null)
        {
            refreshStyleMessages();
        }
    }

}
