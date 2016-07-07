package com.g2inc.scap.editor.gui.windows.oval.definition;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.jdom.Element;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.MultilineStringDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.definition.affected.AffectedItemContainerEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.definition.affected.platform.AffectedEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.ovalref.OvalReferenceEditor;
import com.g2inc.scap.editor.gui.model.tree.oval.metadata.MetadataTreeModel;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.oval.AffectedItem;
import com.g2inc.scap.library.domain.oval.AffectedItemContainer;
import com.g2inc.scap.library.domain.oval.AffectedItemFamilyEnum;
import com.g2inc.scap.library.domain.oval.AffectedPlatform;
import com.g2inc.scap.library.domain.oval.AffectedProduct;
import com.g2inc.scap.library.domain.oval.Metadata;
import com.g2inc.scap.library.domain.oval.OvalReference;
import com.g2inc.scap.library.util.CommonUtil;

/**
 * A common metadata editing component that can be used anywhere someone needs
 * to be able to edit the metadata of an oval definition.
 * 
 * @author ssill
 */
public class MetaDataEditingPanel extends javax.swing.JPanel
{

    private static final long serialVersionUID = 1L;
    private Metadata defMetadata;
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    public void addChangeListener(ChangeListener cl)
    {
        changeListeners.add(cl);
    }

    public void removeChangeListener(ChangeListener cl)
    {
        changeListeners.remove(cl);
    }

    private void notifyChangeListeners()
    {
        for (int x = 0; x < changeListeners.size(); x++)
        {
            ChangeListener cl = changeListeners.get(x);

            ChangeEvent ce = new ChangeEvent(this);
            cl.stateChanged(ce);
        }
    }

    public MetaDataEditingPanel()
    {
        initComponents();
        tree.addMouseListener(new MouseListener()
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

                if (me.getButton() == MouseEvent.BUTTON2
                    || me.getButton() == MouseEvent.BUTTON3)
                {
                    isRightClick = true;
                }

                TreePath path = tree.getPathForLocation(me.getX(), me.getY());

                if (path == null)
                {
                    return;
                }

                tree.setSelectionPath(path);

                Object selected = tree.getLastSelectedPathComponent();

                if (selected == null)
                {
                    return;
                }

                final MetadataTreeModel treeModel = (MetadataTreeModel) tree.getModel();

                if (selected instanceof DefaultMutableTreeNode)
                {
                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) selected;

                    Object userObj = node.getUserObject();

                    if (userObj != null)
                    {
                        if (userObj instanceof Element)
                        {
                            Element elem = (Element) userObj;
                            StringBuilder ePath = new StringBuilder();
                            CommonUtil.getElementPath(elem, ePath);

                            if (ePath.toString().equals(
                                Metadata.ELEMENT_PATH_METADATA))
                            {
                                if (isRightClick)
                                {
                                    // show a popup menu
                                    JPopupMenu mainMetadataPopup = buildMetadataPopupMenu(node);
                                    mainMetadataPopup.show(me.getComponent(),
                                                           me.getX(), me.getY());
                                }
                                else if (isDoubleClick)
                                {
                                    // open string editor for title
                                }
                            }
                            else if (ePath.toString().equals(
                                Metadata.ELEMENT_PATH_AFFECTED))
                            {
                                AffectedItemContainer affected = defMetadata.getParentDocument().getAffectedItemContainerWrapper();
                                affected.setElement(elem);

                                if (isRightClick)
                                {
                                    // show a popup menu
                                    JPopupMenu affectedPopup = buildAffectedPopupMenu(
                                        affected, node);
                                    affectedPopup.show(me.getComponent(), me.getX(), me.getY());
                                }
                                else if (isDoubleClick)
                                {
                                }
                            }
                            else if (ePath.toString().equals(
                                Metadata.ELEMENT_PATH_AFFECTED_PRODUCT))
                            {
                                AffectedProduct affected = defMetadata.getParentDocument().getAffectedProductWrapper();
                                affected.setElement(elem);

                                if (isRightClick)
                                {
                                    // show a popup menu
                                    JPopupMenu affectedProductPopup = buildAffectedItemPopupMenu(
                                        affected, node);
                                    affectedProductPopup.show(
                                        me.getComponent(), me.getX(), me.getY());
                                }
                                else if (isDoubleClick)
                                {
                                    System.out.println("The item of interest was double clicked");
                                    final MetadataTreeModel model = (MetadataTreeModel) tree.getModel();
                                    editString(model, affected.getElement(), node);                                    
                                }
                            }
                            else if (ePath.toString().equals(
                                Metadata.ELEMENT_PATH_AFFECTED_PLATFORM))
                            {
                                AffectedPlatform affected = defMetadata.getParentDocument().getAffectedPlatformWrapper();
                                affected.setElement(elem);

                                if (isRightClick)
                                {
                                    // show a popup menu
                                    JPopupMenu affectedPlatformPopup = buildAffectedItemPopupMenu(
                                        affected, node);
                                    affectedPlatformPopup.show(me.getComponent(), me.getX(), me.getY());
                                }
                                else if (isDoubleClick)
                                {
                                }
                            }
                            else if (ePath.toString().equals(
                                Metadata.ELEMENT_PATH_TITLE))
                            {
                                final Element elemRef = elem;
                                if (isRightClick)
                                {
                                    // show a popup menu
                                    JPopupMenu titlePopup = new JPopupMenu();
                                    JMenuItem editTitleMenuItem = new JMenuItem(
                                        "Edit title");
                                    editTitleMenuItem.setArmed(true);

                                    editTitleMenuItem.addActionListener(new ActionListener()
                                    {

                                        @Override
                                        public void actionPerformed(
                                            ActionEvent ae)
                                        {
                                            editString(treeModel,
                                                       elemRef, node);
                                        }
                                    });

                                    titlePopup.add(editTitleMenuItem);
                                    titlePopup.show(me.getComponent(), me.getX(), me.getY());
                                }
                                else if (isDoubleClick)
                                {
                                    editString(treeModel, elemRef, node);
                                }
                            }
                            else if (ePath.toString().equals(
                                Metadata.ELEMENT_PATH_DESCRIPTION))
                            {
                                final Element elemRef = elem;
                                if (isRightClick)
                                {
                                    // show a popup menu
                                    JPopupMenu descPopup = new JPopupMenu();
                                    JMenuItem editDescMenuItem = new JMenuItem(
                                        "Edit Description");
                                    editDescMenuItem.setArmed(true);

                                    editDescMenuItem.addActionListener(new ActionListener()
                                    {

                                        @Override
                                        public void actionPerformed(
                                            ActionEvent ae)
                                        {
                                            editString(treeModel,
                                                       elemRef, node);
                                        }
                                    });

                                    descPopup.add(editDescMenuItem);
                                    descPopup.show(me.getComponent(),
                                                   me.getX(), me.getY());
                                }
                                else if (isDoubleClick)
                                {
                                    editString(treeModel, elemRef, node);
                                }
                            }
                            else if (ePath.toString().equals(
                                Metadata.ELEMENT_PATH_REFERENCE))
                            {
                                final OvalReference oref = defMetadata.getParentDocument().getReferenceWrapper();
                                oref.setElement(elem);
                                oref.setRoot(defMetadata.getRoot());

                                if (isRightClick)
                                {
                                    // show a popup menu
                                    JPopupMenu refPopup = new JPopupMenu();
                                    JMenuItem editRefMenuItem = new JMenuItem(
                                        "Edit");
                                    editRefMenuItem.setArmed(true);

                                    editRefMenuItem.addActionListener(new ActionListener()
                                    {

                                        @Override
                                        public void actionPerformed(ActionEvent ae)
                                        {
                                            // open editor for oval ref
                                            EditorDialog dtEditor = new EditorDialog(
                                                EditorMainWindow.getInstance(),
                                                true);


                                            dtEditor.setEditorPage(new OvalReferenceEditor());
                                            dtEditor.setData(oref);

                                            dtEditor.pack();
                                            dtEditor.setVisible(true);

                                            if (!dtEditor.wasCancelled())
                                            {
                                                dtEditor.getData(); // force user
                                                // supplied data to
                                                // be added to
                                                // oref
                                                treeModel.nodeChanged(node);
                                                notifyChangeListeners();
                                            }
                                        }
                                    });

                                    refPopup.add(editRefMenuItem);

                                    JMenuItem removeRefMenuItem = new JMenuItem(
                                        "Remove");
                                    removeRefMenuItem.addActionListener(new ActionListener()
                                    {
                                        @Override
                                        public void actionPerformed(ActionEvent ae)
                                        {
                                            List<String> references = new ArrayList<String>();
                                            references.add(oref.getRefId());

                                            defMetadata.delReferences(references);
                                            treeModel.removeNodeFromParent(node);
                                            notifyChangeListeners();
                                        }
                                    });

                                    refPopup.add(removeRefMenuItem);

                                    refPopup.show(me.getComponent(), me.getX(),
                                                  me.getY());
                                }
                                else if (isDoubleClick)
                                {
                                    // open editor for oval ref
                                    EditorDialog dtEditor = new EditorDialog(
                                        EditorMainWindow.getInstance(),
                                        true);

                                    dtEditor.setEditorPage(new OvalReferenceEditor());
                                    dtEditor.setData(oref);

                                    dtEditor.pack();
                                    dtEditor.setVisible(true);

                                    if (!dtEditor.wasCancelled())
                                    {
                                        dtEditor.getData(); // force user
                                        // supplied data to
                                        // be added to
                                        // oref
                                        treeModel.nodeChanged(node);
                                        notifyChangeListeners();
                                    }
                                }
                            }
                        }
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

        initComponents2();
    }

    public void setMetadata(Metadata metadata)
    {
        defMetadata = metadata;
    }

    private void editString(DefaultTreeModel treeModel, Element elemRef,
                            DefaultMutableTreeNode node)
    {
        EditorDialog dtEditor = new EditorDialog(
            EditorMainWindow.getInstance(), true);
        MultilineStringDatatypeEditor editorPage = new MultilineStringDatatypeEditor();
        String elemText = elemRef.getText();
        editorPage.setData(elemText);

        dtEditor.setEditorPage(editorPage);

        dtEditor.pack();
        dtEditor.setLocationRelativeTo(EditorMainWindow.getInstance());

        dtEditor.setVisible(true);

        String data = (String) dtEditor.getData();

        if (!dtEditor.wasCancelled() && data != null)
        {
            if (!data.equals(elemText))
            {
                elemRef.setText(data);
                treeModel.nodeChanged(node);
                notifyChangeListeners();
            }
        }
    }

    private JPopupMenu buildMetadataPopupMenu(final DefaultMutableTreeNode node)
    {
        final MetadataTreeModel model = (MetadataTreeModel) tree.getModel();

        JPopupMenu menu = new JPopupMenu();

        JMenuItem addRefMenuItem = new JMenuItem("Add Reference...");
        addRefMenuItem.setArmed(true);
        addRefMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                EditorDialog dtEditor = new EditorDialog(EditorMainWindow.getInstance(), true);

                OvalReference newRef = defMetadata.getParentDocument().createReference();

                dtEditor.setEditorPage(new OvalReferenceEditor());
                dtEditor.setData(newRef);

                dtEditor.pack();
                dtEditor.setVisible(true);

                if (!dtEditor.wasCancelled())
                {
                    dtEditor.getData(); // force the fields in the object we
                    // passed in to be populated

                    String source = newRef.getSource();
                    String refid = newRef.getRefId();

                    boolean sourceGood = false;
                    boolean refIdGood = false;

                    if (source != null && source.length() > 0)
                    {
                        sourceGood = true;
                    }

                    if (refid != null && refid.length() > 0)
                    {
                        refIdGood = true;
                    }

                    if (sourceGood && refIdGood)
                    {
                        // TODO: addReference is going to always return 0, as
                        // it's implemented now,
                        // should we do something different here??
                        defMetadata.addReference(newRef);
                        notifyChangeListeners();

                        int whereToInsert = model.whereToInsertReference(newRef);
                        if (whereToInsert != -1)
                        {
                            DefaultMutableTreeNode childRefNode = new DefaultMutableTreeNode(
                                newRef.getElement());
                            model.insertNodeInto(childRefNode, node,
                                                 whereToInsert);
                        }
                    }
                }
            }
        });
        menu.add(addRefMenuItem);

        JMenuItem addAffectedMenuItem = new JMenuItem("Add Affected...");
        addAffectedMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                AffectedItemContainer affected = defMetadata.createAffected(defMetadata.getParentDocument());
                EditorDialog dtEditor = new EditorDialog(EditorMainWindow.getInstance(), true);
                AffectedItemContainerEditor editorPage = new AffectedItemContainerEditor();

                editorPage.setOptions(defMetadata.getParentDocument().getSupportedFamilies());

                dtEditor.setEditorPage(editorPage);
                dtEditor.setData(affected.getFamily());

                dtEditor.pack();
                dtEditor.setVisible(true);

                if (!dtEditor.wasCancelled())
                {
                    affected.setFamily((AffectedItemFamilyEnum) editorPage.getData());
                    defMetadata.addAffected(affected);
                    notifyChangeListeners();

                    int whereToInsert = model.whereToInsertAffected(affected);
                    if (whereToInsert != -1)
                    {
                        DefaultMutableTreeNode childAffNode = new DefaultMutableTreeNode(
                            affected.getElement());
                        model.insertNodeInto(childAffNode, node, whereToInsert);
                    }
                }
            }
        });
        menu.add(addAffectedMenuItem);
        return menu;
    }

    private JPopupMenu buildAffectedPopupMenu(
        final AffectedItemContainer affected,
        final DefaultMutableTreeNode node)
    {
        final MetadataTreeModel model = (MetadataTreeModel) tree.getModel();

        JPopupMenu menu = new JPopupMenu();
        JMenuItem setFamilyMenuItem = new JMenuItem("Set family");
        setFamilyMenuItem.setArmed(true);
        setFamilyMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                EditorDialog dtEditor = new EditorDialog(EditorMainWindow.getInstance(), true);
                AffectedItemContainerEditor editorPage = new AffectedItemContainerEditor();

                editorPage.setOptions(defMetadata.getParentDocument().getSupportedFamilies());

                dtEditor.setEditorPage(editorPage);
                dtEditor.setData(affected.getFamily());

                dtEditor.pack();
                dtEditor.setVisible(true);

                if (!dtEditor.wasCancelled())
                {
                    affected.setFamily((AffectedItemFamilyEnum) editorPage.getData());
                    notifyChangeListeners();
                    model.nodeChanged(node);
                }
            }
        });
        menu.add(setFamilyMenuItem);

        JMenuItem removeAffectedItemContainerMenuItem = new JMenuItem("Remove");
        removeAffectedItemContainerMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                defMetadata.removeAffected(affected);
                model.removeNodeFromParent(node);
                notifyChangeListeners();
            }
        });
        menu.add(removeAffectedItemContainerMenuItem);

        JMenuItem addPlatformMenuItem = new JMenuItem("Add platform");
        addPlatformMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                EditorDialog dtEditor = new EditorDialog(EditorMainWindow.getInstance(), true);
//                String family = (affected.getFamily() == null ? null : affected.getFamily().name());
                SCAPContentManager scm = SCAPContentManager.getInstance();
                List<String> platformNames = scm.getOfficalCPEPlatformNames();
                List<String> platformTitles = scm.getOfficalCPEPlatformTitles();
                AffectedEditor editorPage = new AffectedEditor("Platform", platformNames, platformTitles);
//                AffectedPlatformEditor editorPage = new AffectedPlatformEditor(family);
//
//                editorPage.setOptions();

                dtEditor.setEditorPage(editorPage);
                dtEditor.setData(affected.getFamily());

                dtEditor.pack();
                dtEditor.setVisible(true);

                if (!dtEditor.wasCancelled())
                {
                    AffectedPlatform platform = affected.createAffectedPlatform(affected.getParentDocument());

                    platform.setValue((String) dtEditor.getData());
                    affected.addAffectedItem(platform);
                    notifyChangeListeners();

                    int whereToInsert = model.whereToInsertAffectedPlatform(
                        node, platform);
                    DefaultMutableTreeNode newChildPlatNode = new DefaultMutableTreeNode(
                        platform.getElement());

                    model.insertNodeInto(newChildPlatNode, node, whereToInsert);
                    model.nodeChanged(newChildPlatNode);
                }
            }
        });
        menu.add(addPlatformMenuItem);

        JMenuItem addProductMenuItem = new JMenuItem("Add product");
        addProductMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                EditorDialog dtEditor = new EditorDialog(EditorMainWindow.getInstance(), true);
                SCAPContentManager scm = SCAPContentManager.getInstance();
                List<String> productNames = scm.getOfficalCPEProductNames();
                List<String> productTitles = scm.getOfficalCPEProductTitles();
                AffectedEditor editorPage = new AffectedEditor("Product", productNames, productTitles);                

                dtEditor.setEditorPage(editorPage);
                dtEditor.setData(affected.getFamily());

                dtEditor.pack();
                dtEditor.setVisible(true);

                if (!dtEditor.wasCancelled())
                {
                    AffectedProduct product = affected.createAffectedProduct(affected.getParentDocument());

                    product.setValue((String) dtEditor.getData());
                    affected.addAffectedItem(product);
                    notifyChangeListeners();

                    int whereToInsert = model.whereToInsertAffectedProduct(
                        node, product);
                    DefaultMutableTreeNode newChildProdNode = new DefaultMutableTreeNode(
                        product.getElement());

                    model.insertNodeInto(newChildProdNode, node, whereToInsert);
                    model.nodeChanged(newChildProdNode);
                }
            }
        });
        menu.add(addProductMenuItem);

        return menu;
    }

    private JPopupMenu buildAffectedItemPopupMenu(final AffectedItem affected,
                                                  final DefaultMutableTreeNode node)
    {
        final MetadataTreeModel model = (MetadataTreeModel) tree.getModel();

        JPopupMenu menu = new JPopupMenu();
        JMenuItem editPlatformMenuItem = new JMenuItem("Edit");
        editPlatformMenuItem.setArmed(true);
        editPlatformMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                editString(model, affected.getElement(), node);
            }
        });
        menu.add(editPlatformMenuItem);

        JMenuItem removeAffectedItemContainerMenuItem = new JMenuItem("Remove");
        removeAffectedItemContainerMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                affected.removeFromParent();
                model.removeNodeFromParent(node);
                notifyChangeListeners();
            }
        });
        menu.add(removeAffectedItemContainerMenuItem);

        return menu;
    }

    private void initTree()
    {
        ToolTipManager.sharedInstance().registerComponent(tree);
    }

    private void initComponents2()
    {
        initTree();
    }

    public void setTreeModel(MetadataTreeModel mtm)
    {
        tree.setModel(mtm);
    }

    public void setTreeCellRenderer(TreeCellRenderer cellRenderer)
    {
        tree.setCellRenderer(cellRenderer);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		treePanel = new javax.swing.JPanel();
		scrollPane = new javax.swing.JScrollPane();
		tree = new javax.swing.JTree();

		setLayout(new java.awt.GridBagLayout());

		treePanel.setLayout(new java.awt.GridBagLayout());

		javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode(
				"root");
		tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
		scrollPane.setViewportView(tree);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.ipadx = 51;
		gridBagConstraints.ipady = 252;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		treePanel.add(scrollPane, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
		add(treePanel, gridBagConstraints);
	}// </editor-fold>//GEN-END:initComponents
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JScrollPane scrollPane;
	private javax.swing.JTree tree;
	private javax.swing.JPanel treePanel;
	// End of variables declaration//GEN-END:variables
}
