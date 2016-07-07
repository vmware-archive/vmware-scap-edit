package com.g2inc.scap.editor.gui.wizards.oval.definition;
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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.cellrenderers.tree.oval.wizard.definition.affected.AffectedTreeCellRenderer;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.StringDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.definition.affected.AffectedItemContainerEditor;
import com.g2inc.scap.editor.gui.model.tree.oval.wizard.definition.AffectedTreeModel;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.editor.gui.wizards.WizardPage;
import com.g2inc.scap.library.domain.oval.AffectedItem;
import com.g2inc.scap.library.domain.oval.AffectedItemContainer;
import com.g2inc.scap.library.domain.oval.AffectedItemFamilyEnum;
import com.g2inc.scap.library.domain.oval.AffectedItemType;
import com.g2inc.scap.library.domain.oval.AffectedPlatform;
import com.g2inc.scap.library.domain.oval.AffectedProduct;

public class NewDefAffectedWizardPage extends WizardPage implements ActionListener, ClipboardOwner
{
    private static final long serialVersionUID = 1L;

    private NewDefinitionWizard parentWiz = null;
    private static Logger log = Logger.getLogger(NewDefAffectedWizardPage.class);

    @Override
    public void lostOwnership(Clipboard arg0, Transferable arg1)
    {
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();

        if (src == copyToClipboardButton)
        {
            MyTransferHandler mth = (MyTransferHandler) tree.getTransferHandler();

            Transferable t = mth.createTransferable(tree);

            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

            c.setContents(t, this);
        }
        else if (src == pasteFromClipboardButon)
        {
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = c.getContents(this);
            boolean itemsAdded = false;

            if(contents != null)
            {
                if(contents.isDataFlavorSupported(DataFlavor.stringFlavor))
                {
                    Object o = null;

                    try
                    {
                        o = contents.getTransferData(DataFlavor.stringFlavor);
                    }
                    catch(Exception e)
                    {
                        log.error("Error getting clipboard contents!");
                        return;
                    }

                    if(o != null)
                    {
                        if(!(o instanceof String))
                        {
                            return;
                        }

                        String data = (String) o;

                        if (data == null || data.trim().length() == 0)
                        {
                            return;
                        }
                        data = data.trim();
                        String eol = System.getProperty("line.separator");
                        eol = eol.replaceAll("(\r|\r\n)", "\n");

                        String[] lines = data.split("\n");

                        Map<String, AffectedItemContainer> existingContainers
                                = new HashMap<String, AffectedItemContainer>();

                        AffectedTreeModel model = (AffectedTreeModel) tree.getModel();
                        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

                        int existingContainerCount = root.getChildCount();

                        if(existingContainerCount > 0)
                        {
                            for(int x = 0; x < existingContainerCount; x++)
                            {
                                DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(x);

                                Object userObj = child.getUserObject();

                                if(userObj instanceof AffectedItemContainer)
                                {
                                    AffectedItemContainer aic = (AffectedItemContainer) userObj;

                                    existingContainers.put(aic.getFamily().toString(), aic);
                                }
                            }
                        }

                        if(lines == null || lines.length == 0)
                        {
                            return;
                        }

                        for(int x = 0; x < lines.length; x++)
                        {
                            String line = lines[x];

                            int commaLoc = line.indexOf(",");

                            if (commaLoc == -1)
                            {
                                continue;
                            }

                            int commaLoc2 = line.indexOf(",", commaLoc + 1);

                            if(commaLoc2 == -1)
                            {
                                continue;
                            }

                            String family = line.substring(0,commaLoc);

                            if(family == null || family.trim().length() == 0)
                            {
                                continue;
                            }

                            family = family.trim().toLowerCase();

                            String itemType = line.substring(commaLoc + 1, commaLoc2);

                            if(itemType == null || itemType.trim().length() == 0)
                            {
                                continue;
                            }

                            itemType = itemType.trim();

                            String itemValue = line.substring(commaLoc2 + 1);

                            if(itemValue == null || itemValue.trim().length() == 0)
                            {
                                continue;
                            }

                            if(existingContainers.containsKey(family))
                            {
                                // already a container in the tree, add to it
                                AffectedItemContainer aic = existingContainers.get(family);

                                if(itemType.equals(AffectedItemType.PLATFORM.toString().toLowerCase()))
                                {
                                    AffectedPlatform ap = aic.createAffectedPlatform(parentWiz.getOvalDefDoc());
                                    ap.setValue(itemValue);
                                    aic.addAffectedItem(ap);
                                    itemsAdded = true;
                                }
                                else if(itemType.equals(AffectedItemType.PRODUCT.toString().toLowerCase()))
                                {
                                    AffectedProduct ap = aic.createAffectedProduct(parentWiz.getOvalDefDoc());
                                    ap.setValue(itemValue);
                                    aic.addAffectedItem(ap);
                                    itemsAdded = true;
                                }
                            }
                            else
                            {
                                // need to create a new container and add it
                                AffectedItemContainer aic = parentWiz.getDefinition().getMetadata().createAffected(parentWiz.getOvalDefDoc());
                                aic.setFamily(AffectedItemFamilyEnum.valueOf(family.toUpperCase()));

                                if(itemType.equals(AffectedItemType.PLATFORM.toString().toLowerCase()))
                                {
                                    AffectedPlatform ap = aic.createAffectedPlatform(parentWiz.getOvalDefDoc());
                                    ap.setValue(itemValue);
                                    aic.addAffectedItem(ap);
                                    itemsAdded = true;
                                }
                                else if(itemType.equals(AffectedItemType.PRODUCT.toString().toLowerCase()))
                                {
                                    AffectedProduct ap = aic.createAffectedProduct(parentWiz.getOvalDefDoc());
                                    ap.setValue(itemValue);
                                    aic.addAffectedItem(ap);
                                    itemsAdded = true;
                                }

                                parentWiz.getAffectedContainers().add(aic);
                                existingContainers.put(aic.getFamily().toString(), aic);
                            }
                        }

                        if(itemsAdded)
                        {
                            tree.setModel(new AffectedTreeModel(parentWiz.getAffectedContainers()));
                        }
                    }
                }
            }
        }
    }

    private boolean hasAtLeastOnePlatformOrProduct()
    {
        boolean ret = false;

        if(parentWiz.getAffectedContainers() == null || parentWiz.getAffectedContainers().size() == 0)
        {
            return ret;
        }

        for(int x = 0; x < parentWiz.getAffectedContainers().size(); x++)
        {
            AffectedItemContainer aic = parentWiz.getAffectedContainers().get(x);

            List<AffectedItem> items = aic.getAffectedItems();
            if(items != null && items.size() > 0)
            {
                ret = true;
                break;
            }
        }

        return ret;
    }

    private JPopupMenu buildRootMenu()
    {
        JPopupMenu ret = new JPopupMenu();
        JMenuItem addAffectedContainerMenuItem = new JMenuItem("Add affected element");

        addAffectedContainerMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                EditorDialog editor = new EditorDialog(parentWiz.getParentWin(), true);

                AffectedItemContainerEditor editorPage = new AffectedItemContainerEditor();

                AffectedItemFamilyEnum[] enums = AffectedItemFamilyEnum.values();

                editorPage.setOptions(enums);
                editorPage.selectDefault(AffectedItemFamilyEnum.windows);

                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());

                editor.setVisible(true);

                if (!editor.wasCancelled())
                {
                    AffectedItemContainer container
                    	= parentWiz.getDefinition().getMetadata().createAffected(parentWiz.getOvalDefDoc());

                    AffectedItemFamilyEnum editorChoice = (AffectedItemFamilyEnum) editor.getData();

                    container.setFamily(editorChoice);

                    parentWiz.getAffectedContainers().add(container);
                    parentWiz.getDefinition().getMetadata().addAffected(container);

                    AffectedTreeModel atm = new AffectedTreeModel(parentWiz.getAffectedContainers());
                    tree.setModel(atm);
                }
            }
        });

        ret.add(addAffectedContainerMenuItem);
        return ret;
    }

    private JPopupMenu buildContainerMenu()
    {
        JPopupMenu ret = new JPopupMenu();
        JMenuItem editMenuItem = new JMenuItem("Edit");
        editMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selected = tree.getLastSelectedPathComponent();

                if(selected == null)
                {
                    return;
                }

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selected;

                Object userObj = selectedNode.getUserObject();

                if(!(userObj instanceof AffectedItemContainer))
                {
                    return;
                }

                AffectedItemContainer aic = (AffectedItemContainer) userObj;
                EditorDialog editor = new EditorDialog(parentWiz.getParentWin(), true);

                AffectedItemContainerEditor editorPage = new AffectedItemContainerEditor();

                AffectedItemFamilyEnum[] enums = AffectedItemFamilyEnum.values();

                editorPage.setOptions(enums);
                editorPage.selectDefault(aic.getFamily());

                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);
                if (!editor.wasCancelled())
                {
                    AffectedItemFamilyEnum editorChoice = (AffectedItemFamilyEnum) editor.getData();

                    aic.setFamily(editorChoice);
                    AffectedTreeModel atm = new AffectedTreeModel(parentWiz.getAffectedContainers());
                    atm.nodeChanged(selectedNode);
                }
            }
        });
        ret.add(editMenuItem);

        if(parentWiz.getAffectedContainers().size() > 1)
        {
            JMenuItem removeMenuItem = new JMenuItem("Remove");
            removeMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent ae)
                {
                    Object selected = tree.getLastSelectedPathComponent();

                    if(selected == null)
                    {
                        return;
                    }

                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selected;
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();

                    Object userObj = selectedNode.getUserObject();

                    if(!(userObj instanceof AffectedItemContainer))
                    {
                        return;
                    }

                    AffectedItemContainer aic = (AffectedItemContainer) userObj;

                    aic.getElement().detach();
                    parentWiz.getAffectedContainers().remove(aic);

                    AffectedTreeModel atm = new AffectedTreeModel(parentWiz.getAffectedContainers());
                    tree.setModel(atm);

                    if(hasAtLeastOnePlatformOrProduct())
                    {
                        parentWiz.enableNextButton();
                        setSatisfied(true);
                    }
                    else
                    {
                        parentWiz.disableNextButton();
                        setSatisfied(false);
                    }
                }
            });
            ret.add(removeMenuItem);
        }

        JMenuItem addPlatMenuItem = new JMenuItem("Add Platform");
        addPlatMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selected = tree.getLastSelectedPathComponent();

                if(selected == null)
                {
                    return;
                }

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selected;

                Object userObj = selectedNode.getUserObject();

                if(!(userObj instanceof AffectedItemContainer))
                {
                    return;
                }

                AffectedItemContainer aic = (AffectedItemContainer) userObj;

                AffectedPlatform ap = aic.createAffectedPlatform(parentWiz.getDefinition().getParentDocument());

                EditorDialog editor = new EditorDialog(parentWiz.getParentWin(), true);
                StringDatatypeEditor editorPage = new StringDatatypeEditor();
                editor.setEditorPage(editorPage);

                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    String chosen = (String) editor.getData();
                    ap.setValue(chosen);

                    aic.addAffectedItem(ap);


                    List<AffectedItem> items = aic.getAffectedItems();

                    AffectedTreeModel atm = (AffectedTreeModel) tree.getModel();

                    int childCount = atm.getChildCount(selectedNode);

                    while(childCount > 0)
                    {
                        DefaultMutableTreeNode child = (DefaultMutableTreeNode) atm.getChild(selectedNode, 0);

                        atm.removeNodeFromParent(child);

                        childCount = atm.getChildCount(selectedNode);
                    }

                    for(int x = 0; x < items.size();x++)
                    {
                        selectedNode.add(new DefaultMutableTreeNode(items.get(x)));
                    }

                    atm.nodeStructureChanged(selectedNode);

                    if(hasAtLeastOnePlatformOrProduct())
                    {
                        parentWiz.enableNextButton();
                        setSatisfied(true);
                    }
                    else
                    {
                        parentWiz.disableNextButton();
                        setSatisfied(false);
                    }

                }
           }
        });
        ret.add(addPlatMenuItem);

        JMenuItem addProdMenuItem = new JMenuItem("Add Product");
        addProdMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selected = tree.getLastSelectedPathComponent();

                if(selected == null)
                {
                    return;
                }

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selected;

                Object userObj = selectedNode.getUserObject();

                if(!(userObj instanceof AffectedItemContainer))
                {
                    return;
                }

                AffectedItemContainer aic = (AffectedItemContainer) userObj;

                AffectedProduct ap = aic.createAffectedProduct(parentWiz.getDefinition().getParentDocument());

                EditorDialog editor = new EditorDialog(parentWiz.getParentWin(), true);
                StringDatatypeEditor editorPage = new StringDatatypeEditor();
                editor.setEditorPage(editorPage);

                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    String chosen = (String) editor.getData();
                    ap.setValue(chosen);

                    aic.addAffectedItem(ap);


                    List<AffectedItem> items = aic.getAffectedItems();

                    AffectedTreeModel atm = (AffectedTreeModel) tree.getModel();

                    int childCount = atm.getChildCount(selectedNode);

                    while(childCount > 0)
                    {
                        DefaultMutableTreeNode child = (DefaultMutableTreeNode) atm.getChild(selectedNode, 0);

                        atm.removeNodeFromParent(child);

                        childCount = atm.getChildCount(selectedNode);
                    }

                    for(int x = 0; x < items.size();x++)
                    {
                        selectedNode.add(new DefaultMutableTreeNode(items.get(x)));
                    }

                    atm.nodeStructureChanged(selectedNode);

                    if(hasAtLeastOnePlatformOrProduct())
                    {
                        parentWiz.enableNextButton();
                        setSatisfied(true);
                    }
                    else
                    {
                        parentWiz.disableNextButton();
                        setSatisfied(false);
                    }

                }
           }
        });

        ret.add(addProdMenuItem);

        return ret;
    }

    private JPopupMenu buildItemMenu()
    {
        JPopupMenu ret = new JPopupMenu();
        JMenuItem editMenuItem = new JMenuItem("Edit");
        editMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selected = tree.getLastSelectedPathComponent();

                if(selected == null)
                {
                    return;
                }

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selected;

                Object userObj = selectedNode.getUserObject();

                if(!(userObj instanceof AffectedItem))
                {
                    return;
                }

                AffectedItem ai = (AffectedItem) userObj;

                EditorDialog editor = new EditorDialog(parentWiz.getParentWin(), true);
                StringDatatypeEditor editorPage = new StringDatatypeEditor();
                editorPage.setData(ai.getValue());
                editor.setEditorPage(editorPage);

                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    String chosen = (String) editor.getData();
                    ai.setValue(chosen);

                    AffectedTreeModel atm = (AffectedTreeModel) tree.getModel();

                    atm.nodeChanged(selectedNode);
                }
           }
        });
        ret.add(editMenuItem);

        JMenuItem removeMenuItem = new JMenuItem("Remove");
        removeMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selected = tree.getLastSelectedPathComponent();

                if(selected == null)
                {
                    return;
                }

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selected;

                Object userObj = selectedNode.getUserObject();

                if(!(userObj instanceof AffectedItem))
                {
                    return;
                }

                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();

                AffectedItem ai = (AffectedItem) userObj;

                ai.getElement().detach();
                parent.remove(selectedNode);

                AffectedTreeModel atm = (AffectedTreeModel) tree.getModel();

                atm.reload(parent);

                if(hasAtLeastOnePlatformOrProduct())
                {
                    parentWiz.enableNextButton();
                    setSatisfied(true);
                }
                else
                {
                    parentWiz.disableNextButton();
                    setSatisfied(false);
                }
           }
        });
        ret.add(removeMenuItem);
        return ret;
    }

    private void initTree()
    {
         ToolTipManager.sharedInstance().registerComponent(tree);
         tree.setCellRenderer(new AffectedTreeCellRenderer());
         tree.setModel(new AffectedTreeModel(parentWiz.getAffectedContainers()));
         tree.setTransferHandler(new MyTransferHandler());

         tree.addMouseListener(new MouseListener()
         {
            private void common(MouseEvent me)
            {
                boolean isRightClick = false;
                boolean isDoubleClick = false;

                int clickCount = me.getClickCount();
                if(clickCount > 1)
                {
                    isDoubleClick = true;
                }

                if(me.getButton() == MouseEvent.BUTTON2 || me.getButton() == MouseEvent.BUTTON3)
                {
                    isRightClick = true;
                }

                TreePath path = tree.getPathForLocation(me.getX(),me.getY());

                if(path == null)
                {
                    return;
                }

                tree.setSelectionPath(path);

                Object selected = tree.getLastSelectedPathComponent();

                if(selected == null)
                {
                    return;
                }

                if(!(selected instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selected;

                if(selectedNode.isRoot())
                {
                    if(isRightClick)
                    {
                        JPopupMenu rootMenu = buildRootMenu();
                        rootMenu.show(me.getComponent(), me.getX(), me.getY());
                    }
                }
                else
                {
                    Object userObj = selectedNode.getUserObject();

                    if(userObj instanceof AffectedItemContainer)
                    {
                        if(isRightClick)
                        {
                            JPopupMenu aicPopup = buildContainerMenu();

                            aicPopup.show(me.getComponent(), me.getX(), me.getY());
                        }
                    }
                    else if(userObj instanceof AffectedItem)
                    {
                        if(isRightClick)
                        {
                            JPopupMenu aiPopup = buildItemMenu();

                            aiPopup.show(me.getComponent(), me.getX(), me.getY());
                        }
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent me)
            {
                common(me);
            }

            @Override
            public void mousePressed(MouseEvent me)
            {
                common(me);
            }

            @Override
            public void mouseReleased(MouseEvent me)
            {
                common(me);
            }

            @Override
            public void mouseEntered(MouseEvent me)
            {
                common(me);
            }

            @Override
            public void mouseExited(MouseEvent me)
            {
                common(me);
            }
        });
    }

    private void initButtons()
    {
        copyToClipboardButton.addActionListener(this);
        pasteFromClipboardButon.addActionListener(this);
    }

    private void initComponents2()
    {
        initTree();
        initButtons();
    }

    /** Creates new form ChoseVariableTypeWizardPage */
    public NewDefAffectedWizardPage(NewDefinitionWizard wiz)
    {
        initComponents();
        parentWiz = wiz;
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

        treePanel = new javax.swing.JPanel();
        treeScrollPane = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        buttonPanel = new javax.swing.JPanel();
        copyToClipboardButton = new javax.swing.JButton();
        pasteFromClipboardButon = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        treePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Affected groupings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        treePanel.setLayout(new java.awt.GridBagLayout());

        tree.setMaximumSize(null);
        tree.setMinimumSize(null);
        tree.setPreferredSize(null);
        treeScrollPane.setViewportView(tree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        treePanel.add(treeScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(treePanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        copyToClipboardButton.setText("To Clipboard");
        copyToClipboardButton.setToolTipText("Copy tree contents to clipboard in the form: family,platform/product,name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        buttonPanel.add(copyToClipboardButton, gridBagConstraints);

        pasteFromClipboardButon.setText("From Clipboard");
        pasteFromClipboardButon.setToolTipText("Paste clipboard contents to tree.  Data should be text in the form: family,platform/product,name  (e.g. windows,platform,Microsoft Windows XP)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        buttonPanel.add(pasteFromClipboardButon, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(19, 0, 0, 0);
        add(buttonPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton copyToClipboardButton;
    private javax.swing.JButton pasteFromClipboardButon;
    private javax.swing.JTree tree;
    private javax.swing.JPanel treePanel;
    private javax.swing.JScrollPane treeScrollPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public Object getData()
    {
        return null;
    }

    @Override
    public void setData(Object data)
    {
    }

    @Override
    public void setWizard(Wizard wizard)
    {
        parentWiz = (NewDefinitionWizard) wizard;
    }

    @Override
    public String getPageTitle()
    {
        return "Affected";
    }

    @Override
    public void performFinish()
    {
    }

    private class MyTransferHandler extends TransferHandler
    {
        @Override
        protected Transferable createTransferable(JComponent c)
        {
            if(c == null)
            {
                return null;
            }

            if(!(c instanceof JTree))
            {
                return null;
            }

            JTree t = (JTree) c;

            StringBuilder sb = new StringBuilder();

            AffectedTreeModel tm = (AffectedTreeModel) t.getModel();

            String eol = System.getProperty("line.separator");

            DefaultMutableTreeNode root = (DefaultMutableTreeNode) tm.getRoot();

            int childCount = root.getChildCount();

            if(childCount > 0)
            {
                for(int x = 0; x < childCount; x++)
                {
                    DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(x);

                    Object userObj = child.getUserObject();

                    if(userObj == null || !(userObj instanceof AffectedItemContainer))
                    {
                        continue;
                    }

                    AffectedItemContainer aic = (AffectedItemContainer) userObj;
                    String family = aic.getFamily().toString();

                    List<AffectedItem> items = aic.getAffectedItems();

                    if(items != null && items.size() > 0)
                    {
                        for(int y = 0; y < items.size(); y++)
                        {
                            AffectedItem ai = items.get(y);

                            sb.append(family + "," + ai.getElementName() +  "," + ai.getValue() + eol);
                        }
                    }
                }
            }
            return new StringSelection(sb.toString());
        }

        @Override
        protected void exportDone(JComponent arg0, Transferable arg1, int arg2)
        {
        }

        @Override
        public void exportToClipboard(JComponent arg0, Clipboard arg1, int arg2) throws IllegalStateException
        {
        }
    }
}
