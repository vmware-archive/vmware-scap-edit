package com.g2inc.scap.editor.gui.windows.cpe;
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
import java.lang.ref.WeakReference;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.cellrenderers.tree.cpe.CPEDocumentTreeCellRenderer;
import com.g2inc.scap.editor.gui.model.tree.cpe.DocumentTreeModel;
import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.util.Adder;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorForm;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.GenericSourceDetailTab;
import com.g2inc.scap.editor.gui.windows.common.WeakTreeModelListener;
import com.g2inc.scap.editor.gui.windows.cpe.item.ItemChecksTab;
import com.g2inc.scap.editor.gui.windows.cpe.item.ItemGeneralTab;
import com.g2inc.scap.editor.gui.windows.cpe.item.ItemNotesTab;
import com.g2inc.scap.editor.gui.windows.cpe.item.ItemReferencesTab;
import com.g2inc.scap.editor.gui.windows.cpe.item.ItemTitlesTab;
import com.g2inc.scap.editor.gui.wizards.cpe.item.create.NewCPEItemWizard;
import com.g2inc.scap.library.content.style.ContentStyle;
import com.g2inc.scap.library.content.style.ContentStyleRegistry;
import com.g2inc.scap.library.content.style.ContentStyleViolationElement;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.cpe.CPEItem;

public class CPEDictionaryEditorForm extends EditorForm implements TreeModelListener, ActionListener, ChangeListener
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(CPEDictionaryEditorForm.class);
    private WeakReference<CPEDictionaryDocument> document = null;
    public static final String WINDOW_TITLE_BASE = EditorMessages.CPE + " Document - ";
    public static final String TAB_TITLE_DICTIONARY_DOC = EditorMessages.CPE + " Dictionary";
    public static final String TAB_TITLE_CPE_ITEM_OVERVIEW = "General";
    public static final String TAB_TITLE_CPE_ITEM_TITLES = "Titles";
    public static final String TAB_TITLE_CPE_ITEM_NOTES = "Notes";
    public static final String TAB_TITLE_CPE_ITEM_REFS = "References";
    public static final String TAB_TITLE_CPE_ITEM_CHECKS = "Checks";
    private WeakReference<EditorMainWindow> parentWin = null;
    private ContentStyle currentStyle = null;

    private JPopupMenu buildRootPopupMenu()
    {
        JPopupMenu ret = null;
        ret = new JPopupMenu();

        JMenuItem addCPEItemMenuItem = new JMenuItem("Add Item");
        addCPEItemMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                NewCPEItemWizard wiz = new NewCPEItemWizard(parentWin.get(), true, document.get());
                wiz.pack();
                wiz.setLocationRelativeTo(parentWin.get());
                wiz.setVisible(true);

                if(!wiz.wasCancelled())
                {
                    CPEItem item = wiz.getItem();

                    document.get().addItem(item);

                    DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

                    DefaultMutableTreeNode itemsNode = findItemsNode();

                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(item);

                    dtm.insertNodeInto(childNode, itemsNode, 0);

                    selectItem(item);
                    
                    EditorUtil.markActiveWindowDirty(parentWin.get(), true);
                }
            }
        });
        ret.add(addCPEItemMenuItem);
        
        return ret;
    }

    private DefaultMutableTreeNode findItemsNode()
    {
        DefaultMutableTreeNode ret = null;
        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) dtm.getRoot();

        int rootChildCount = root.getChildCount();
        boolean createNode = false;

        if(rootChildCount == 0)
        {
            createNode = true;
        }
        else
        {
            // in case we don't find the items node
            createNode = true;

            Enumeration children = root.children();

            while(children.hasMoreElements())
            {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();

                Object userObj = child.getUserObject();

                if(userObj != null && userObj instanceof String)
                {
                    String nodeName = (String) userObj;

                    if(nodeName.toLowerCase().startsWith("items"))
                    {
                        ret = child;
                        createNode = false;
                        break;
                    }
                }
            }
        }

        if(createNode)
        {
            // root has no children, need to create items node
            ret = new DefaultMutableTreeNode("Items(0)");
            dtm.insertNodeInto(ret, root, 0);
        }
        return ret;
    }

    private JPopupMenu buildItemPopupMenu(DefaultMutableTreeNode node, CPEItem item)
    {
        JPopupMenu ret = null;
        ret = new JPopupMenu("Item");

        final CPEItem itemRef = item;
        final DefaultMutableTreeNode nodeRef = node;
        final CPEDictionaryDocument dictRef = (CPEDictionaryDocument) item.getSCAPDocument();

        JMenuItem removeCPEItemMenuItem = new JMenuItem("Remove");
        removeCPEItemMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String promptMessage = "Are you sure you would want to remove the item " + itemRef.getName() + "?";

                int rc = JOptionPane.showConfirmDialog(EditorMainWindow.getInstance(), promptMessage, "Remove Item?", JOptionPane.YES_NO_OPTION);

                if(rc == JOptionPane.YES_OPTION)
                {
                    dictRef.remove(itemRef);
                    DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

                    TreeNode parent = nodeRef.getParent();
                    dtm.removeNodeFromParent(nodeRef);
                    dtm.reload(parent);

                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                }
            }
        });
        ret.add(removeCPEItemMenuItem);

        return ret;
    }

    private void initTree()
    {
        final CPEDictionaryEditorForm thisRef = this;

        ToolTipManager.sharedInstance().registerComponent(structTree);

        structTree.setScrollsOnExpand(true);

        structTree.addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override
            public void valueChanged(TreeSelectionEvent tse)
            {
                Object selected = structTree.getLastSelectedPathComponent();

                if (selected != null)
                {
                    removeDetailTabs();

                    if (!(selected instanceof DefaultMutableTreeNode))
                    {
                        return;
                    }
                    DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selected;

                    Object userObj = selNode.getUserObject();

                    if (userObj != null)
                    {
                        if (userObj instanceof CPEDictionaryDocument)
                        {
                            CPEDictionaryDocument dd = (CPEDictionaryDocument) userObj;

                            // they selected the root node
                            CPEDictionaryDetailTab tab = new CPEDictionaryDetailTab();

                            tab.setDoc(dd);
                            detailTabPane.addTab(TAB_TITLE_DICTIONARY_DOC, tab);
                            
                            if(tab.generatorCreated())
                            {
                            	setDirty(true);
                            }
                        }
                        else if (userObj instanceof CPEItem)
                        {
                            CPEItem item = (CPEItem) userObj;
                            GenericSourceDetailTab sourceTab = new GenericSourceDetailTab();

                            sourceTab.setXMLSource(elementToString(item.getElement()));

                            JPanel tab = new ItemGeneralTab();
                            ((ItemGeneralTab)tab).setDoc(item);
                            ((ItemGeneralTab)tab).setParentEditor(thisRef);
                            ((ItemGeneralTab)tab).setSourceTab(sourceTab);
                            detailTabPane.addTab(TAB_TITLE_CPE_ITEM_OVERVIEW, tab);

                            tab = new ItemTitlesTab();
                            ((ItemTitlesTab)tab).setDoc(item);
                            ((ItemTitlesTab)tab).setParentEditor(thisRef);
                            ((ItemTitlesTab)tab).setSourceTab(sourceTab);
                            detailTabPane.addTab(TAB_TITLE_CPE_ITEM_TITLES, tab);

                            tab = new ItemNotesTab();
                            ((ItemNotesTab)tab).setDoc(item);
                            ((ItemNotesTab)tab).setParentEditor(thisRef);
                            ((ItemNotesTab)tab).setSourceTab(sourceTab);
                            detailTabPane.addTab(TAB_TITLE_CPE_ITEM_NOTES, tab);

                            tab = new ItemReferencesTab();
                            ((ItemReferencesTab)tab).setDoc(item);
                            ((ItemReferencesTab)tab).setParentEditor(thisRef);
                            ((ItemReferencesTab)tab).setSourceTab(sourceTab);
                            detailTabPane.addTab(TAB_TITLE_CPE_ITEM_REFS, tab);

                            tab = new ItemChecksTab();
                            ((ItemChecksTab)tab).setDoc(item);
                            ((ItemChecksTab)tab).setParentEditor(thisRef);
                            ((ItemChecksTab)tab).setSourceTab(sourceTab);
                            detailTabPane.addTab(TAB_TITLE_CPE_ITEM_CHECKS, tab);

                            detailTabPane.add("Source", sourceTab);
                        }
                        else
                        {
                            removeDetailTabs();
                        }
                    } else
                    {
                        removeDetailTabs();
                    }
                } else
                {
                    // nothing in tree selected, remove active detail tab
                    removeDetailTabs();
                }
            }
        });

        structTree.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent me)
            {
                boolean isRightClick = false;
                boolean isDoubleClick = false;

                if (me.getClickCount() == 2)
                {
                    isDoubleClick = true;
                }

                if (me.getButton() == MouseEvent.BUTTON2 || me.getButton() == MouseEvent.BUTTON3)
                {
                    isRightClick = true;
                }

                TreePath selectedPath = structTree.getPathForLocation(me.getX(), me.getY());

                if (selectedPath == null)
                {
                    return;
                }

                structTree.setSelectionPath(selectedPath);

                Object selected = structTree.getLastSelectedPathComponent();

                if (selected == null)
                {
                    TreePath path = structTree.getPathForLocation(me.getX(), me.getY());

                    if (path != null)
                    {
                        structTree.setSelectionPath(path);

                        selected = structTree.getLastSelectedPathComponent();
                    }
                }


                if (selected == null)
                {
                    return;
                }

                if (!(selected instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selected;

                Object userObj = node.getUserObject();

                if (userObj == null)
                {
                    return;
                }

                if (node.isRoot())
                {
                    if (!(userObj instanceof CPEDictionaryDocument))
                    {
                        return;
                    }

                    if (isRightClick)
                    {
                        JPopupMenu rootPopupMenu = buildRootPopupMenu();
                        rootPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                    }
                }
                else
                {
                    if(userObj instanceof CPEItem)
                    {
                        CPEItem item = (CPEItem) userObj;

                        if(isRightClick)
                        {
                            JPopupMenu itemMenu = buildItemPopupMenu(node, item);

                            itemMenu.show(me.getComponent(), me.getX(), me.getY());
                        }
                    }
                    else if (userObj instanceof String)
                    {
                        String selectedNodeStringValue = (String) userObj;
                        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();

                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {
            }

            @Override
            public void mouseReleased(MouseEvent arg0)
            {
            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {
            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {
            }
        });
    }

    private void removeDetailTabs()
    {
        while (detailTabPane.getTabCount() > 0)
        {
            detailTabPane.removeTabAt(0);
        }

        detailTabPane.repaint();
    }

    private void initDetailTabPane()
    {
    }

    private void initStructureTab()
    {
        initTree();

        initDetailTabPane();
    }

    public void initDividerLocation()
    {
        int treeSlicePercent = 30;

        treeTabsSplitPane.setDividerLocation(treeSlicePercent / 100);
    }

    private void initTextFields()
    {
        final CPEDictionaryEditorForm thisRef = this;

        searchField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent e)
            {
                String val = searchField.getText();

                DocumentTreeModel dtm = new DocumentTreeModel(document.get(), val);
                dtm.addTreeModelListener(thisRef);

                structTree.setModel(dtm);
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                common(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                common(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                common(e);
            }
        });
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

    private void initComponents2()
    {
        initStructureTab();
        initButtons();
        initTextFields();
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

    private void initButtons()
    {
        expandAllButton.addActionListener(this);
        collapseAllButton.addActionListener(this);
    }

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
            while(nodeEnum.hasMoreElements())
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
            while(nodeEnum.hasMoreElements())
            {
                TreePath tp = EditorUtil.getTreePathToChild(nodeEnum.nextElement());

                structTree.collapsePath(tp);
            }
        }
    }

    /** Creates new form OvalEditorForm */
    public CPEDictionaryEditorForm()
    {
        initComponents();
        parentWin = new WeakReference<EditorMainWindow>(EditorMainWindow.getInstance());
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

        jSeparator1 = new javax.swing.JSeparator();
        docStyleSplitPane = new javax.swing.JSplitPane();
        topPanel = new javax.swing.JPanel();
        searchCaption = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        stylePickerPanel = new javax.swing.JPanel();
        stylePicker = new com.g2inc.scap.editor.gui.windows.common.content.ContentStylePicker();
        treeTabsSplitPane = new javax.swing.JSplitPane();
        tabPanel = new javax.swing.JPanel();
        detailTabPane = new javax.swing.JTabbedPane();
        treePanel = new javax.swing.JPanel();
        structTreeScrollPane = new javax.swing.JScrollPane();
        structTree = new javax.swing.JTree();
        expandAllButton = new javax.swing.JButton();
        collapseAllButton = new javax.swing.JButton();
        bottomPanel = new javax.swing.JPanel();
        contentStylePanel = new com.g2inc.scap.editor.gui.windows.common.content.ContentStyleMessagesPanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(800, 700));
        setPreferredSize(new java.awt.Dimension(1024, 768));
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        getContentPane().add(jSeparator1, gridBagConstraints);

        docStyleSplitPane.setDividerLocation(400);
        docStyleSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        docStyleSplitPane.setMinimumSize(new java.awt.Dimension(191, 133));
        docStyleSplitPane.setPreferredSize(new java.awt.Dimension(462, 813));

        topPanel.setLayout(new java.awt.GridBagLayout());

        searchCaption.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 3, 5, 0);
        topPanel.add(searchCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        topPanel.add(searchField, gridBagConstraints);

        stylePickerPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        stylePickerPanel.add(stylePicker, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 5);
        topPanel.add(stylePickerPanel, gridBagConstraints);

        treeTabsSplitPane.setDividerLocation(240);
        treeTabsSplitPane.setAutoscrolls(true);
        treeTabsSplitPane.setMaximumSize(null);

        tabPanel.setLayout(new java.awt.GridBagLayout());

        detailTabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        detailTabPane.setMaximumSize(null);
        detailTabPane.setMinimumSize(null);
        detailTabPane.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        tabPanel.add(detailTabPane, gridBagConstraints);

        treeTabsSplitPane.setRightComponent(tabPanel);

        treePanel.setLayout(new java.awt.GridBagLayout());

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        structTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        structTreeScrollPane.setViewportView(structTree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        treePanel.add(structTreeScrollPane, gridBagConstraints);

        expandAllButton.setText("Expand All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        treePanel.add(expandAllButton, gridBagConstraints);

        collapseAllButton.setText("Collapse All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        treePanel.add(collapseAllButton, gridBagConstraints);

        treeTabsSplitPane.setLeftComponent(treePanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
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
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
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
    private javax.swing.JLabel searchCaption;
    private javax.swing.JTextField searchField;
    private javax.swing.JTree structTree;
    private javax.swing.JScrollPane structTreeScrollPane;
    private com.g2inc.scap.editor.gui.windows.common.content.ContentStylePicker stylePicker;
    private javax.swing.JPanel stylePickerPanel;
    private javax.swing.JPanel tabPanel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel treePanel;
    private javax.swing.JSplitPane treeTabsSplitPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public CPEDictionaryDocument getDocument()
    {
        return document.get();
    }

    public void setDocument(CPEDictionaryDocument doc)
    {
        this.document = new WeakReference<CPEDictionaryDocument>(doc);
        DocumentTreeModel tm = new DocumentTreeModel(document.get(), null);
        tm.addTreeModelListener(new WeakTreeModelListener(this));

        structTree.setCellRenderer(new CPEDocumentTreeCellRenderer());
        structTree.setModel(tm);

        structTree.setSelectionPath(new TreePath(tm.getRoot()));
    
        refreshStyleMessages();
    }

    @Override
    public void treeNodesChanged(TreeModelEvent tme)
    {
    }

    @Override
    public void treeNodesInserted(TreeModelEvent tme)
    {
        treeNodeCountUpdate(tme.getTreePath());
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent tme)
    {
        treeNodeCountUpdate(tme.getTreePath());
    }

    @Override
    public void treeStructureChanged(TreeModelEvent tme)
    {
    }

    private void treeNodeCountUpdate(TreePath path)
    {
        Object last = path.getLastPathComponent();

        if(last instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) last;

            Object userObj = node.getUserObject();

            if(userObj instanceof String)
            {
                String name = (String) userObj;

                int loc = name.indexOf("(");

                if(loc > -1)
                {
                    String basename = name.substring(0, loc);

                    Adder childCount = new Adder(0);

                    EditorUtil.countChildren(node, childCount);

                    DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();
                    node.setUserObject(basename + "(" +  childCount.getValue() + ")");

                    dtm.nodeChanged(node);

                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

                    if(parent != null)
                    {
                        treeNodeCountUpdate(new TreePath(parent.getPath()));
                    }
                }
            }
        }
    }

    public EditorMainWindow getParentWin()
    {
        return parentWin.get();
    }

    public void setParentWin(EditorMainWindow parentWin)
    {
        this.parentWin = new WeakReference<EditorMainWindow>(parentWin);
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

    @Override
    public TreePath getSelectedPath()
    {
        TreePath path = null;
        path = structTree.getSelectionPath();
        return path;
    }

    /**
     * This method should be called when the selected item has changed and needs
     * this to be reflected in the main tree.
     */
    public void updatedSelectedPath()
    {
        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

        TreePath path = getSelectedPath();

        if(path != null)
        {
            dtm.nodeChanged((DefaultMutableTreeNode)path.getLastPathComponent());
        }
    }

    public void selectItem(CPEItem item)
    {
        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

        DefaultMutableTreeNode defsNode = dtm.getItemsNode();
        Enumeration children = defsNode.depthFirstEnumeration();

        TreePath path = null;

        while (children.hasMoreElements())
        {
            Object child = children.nextElement();

            if (child instanceof DefaultMutableTreeNode)
            {
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) child;

                if (cNode.getUserObject() != null)
                {
                    Object userObj = cNode.getUserObject();

                    if (userObj instanceof CPEItem)
                    {
                        CPEItem ci = (CPEItem) userObj;

                        if (ci.getName().equals(item.getName()))
                        {
                            path = EditorUtil.getTreePathToChild(cNode);
                            break;
                        }
                    }
                }
            }
        }

        if (path != null)
        {
            structTree.setSelectionPath(path);
            structTree.scrollPathToVisible(path);
        }
    }

    public void cleanUp()
    {
        CPEDictionaryDocument cpeDoc = document.get();
        if(cpeDoc != null)
        {
            cpeDoc.close();
        }

        treePanel.remove(structTree);
        changeListeners.clear();
        tabPanel.removeAll();
        treePanel.removeAll();
        ToolTipManager.sharedInstance().unregisterComponent(structTree);
        DocumentTreeModel tm = (DocumentTreeModel) structTree.getModel();
        tm.cleanUp();

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
    public void setDirty(boolean b)
    {
        super.setDirty(b);
        if(currentStyle != null)
        {
            refreshStyleMessages();
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
	        CPEDictionaryDocument cpeDoc = getDocument();
	        if (cpeDoc != null) {
		        List<ContentStyleViolationElement> violations = currentStyle.checkDocument(cpeDoc);
		        contentStylePanel.setData(violations);
	        }
        }
    }
}
