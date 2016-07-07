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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.cellrenderers.tree.oval.criteria.CriteriaTreeCellRenderer;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.definition.criteria.CriteriaEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.definition.criteria.MultipleCriterionAdder;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.definition.criterion.CriterionEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.extenddef.ExtendDefinitionEditor;
import com.g2inc.scap.editor.gui.model.tree.oval.criteria.CriteriaEditorTreeModel;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorForm;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.oval.OvalTab;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.Criteria;
import com.g2inc.scap.library.domain.oval.CriteriaChild;
import com.g2inc.scap.library.domain.oval.Criterion;
import com.g2inc.scap.library.domain.oval.ExtendDefinition;
import com.g2inc.scap.library.domain.oval.OvalCriteriaOperatorEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalTest;

public class DefinitionCriteriaDetailTab extends OvalTab
{
    private static Logger log = Logger.getLogger(DefinitionCriteriaDetailTab.class);
    private OvalDefinition ovalDef = null;
    private DefinitionSourceDetailTab sourceTab = null;
    private DefaultMutableTreeNode defCriteria = null;
    private static final String NO_CRITERIA_DEFINED = "No Criteria defined";
    private boolean tempBool = false;
    private EditorForm editorForm;

    private void allCriteriaHaveChildren(DefaultMutableTreeNode node)
    {
        Object userObj = node.getUserObject();
        int childCount = node.getChildCount();
        if(userObj instanceof Criteria)
        {
            if(childCount == 0)
            {
                tempBool = false;
                return;
            }
        }

        if(childCount > 0)
        {
            Enumeration children = node.children();
            while (children.hasMoreElements())
            {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
                allCriteriaHaveChildren(child);
            }
        }
    }

    private boolean allItemsAreValid()
    {
        boolean ret = true;
		if (defCriteria == null)
		{
			return ret;
		}
        tempBool = true;
        allCriteriaHaveChildren(defCriteria);
        if(!tempBool)
        {
            String msg = "All criteria must have at least one child!";
            log.error(msg);
            statusLabel.setText(msg);
            return ret;
        }

        if(ret)
        {
            statusLabel.setText("OK");
        }
        return ret;
    }
    private void initTreeListeners()
    {
        criteriaTree.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent me)
            {
                javax.swing.tree.TreePath path = criteriaTree.getPathForLocation(me.getX(), me.getY());

                if(path == null)
                {
                    return;
                }

                criteriaTree.setSelectionPath(path);

                Object selection = criteriaTree.getLastSelectedPathComponent();

                if(selection == null)
                {
                    return;
                }

                if(!(selection instanceof javax.swing.tree.DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode)selection;


                    Object userObj = node.getUserObject();
                    if(userObj != null)
                    {
                        boolean isRightClick = false;
                        boolean isDoubleClick = false;

                        if(me.getClickCount() == 2)
                        {
                            isDoubleClick = true;
                        }

                        if(me.getButton() == MouseEvent.BUTTON2 || me.getButton() == MouseEvent.BUTTON3)
                        {
                            isRightClick = true;
                        }

						if (userObj instanceof String)
						{
                            if(isRightClick)
                            {
                                JPopupMenu popup = buildRootMenu();
                                popup.show(me.getComponent(), me.getX(), me.getY());
                            }
						}

						else if(userObj instanceof ExtendDefinition)
                        {
                            ExtendDefinition edef = (ExtendDefinition)userObj;

                            if(isRightClick)
                            {
                                JPopupMenu edPopup = buildExtendDefinitionPopupMenu();

                                edPopup.show(me.getComponent(), me.getX(), me.getY());
                            }
                            else if(isDoubleClick)
                            {
                                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                                ExtendDefinitionEditor editorPage = new ExtendDefinitionEditor(editor, edef);
                                editor.setEditorPage(editorPage);

                                editor.pack();
                                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                                editor.setVisible(true);

                                if (!editor.wasCancelled())
                                {
                                    editorPage.getData();

                                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();
                                    model.reload(node);
				    updateSource();
                                }
                            }
                        }
                        else if(userObj instanceof Criteria)
                        {
                            Criteria criteria = (Criteria)userObj;

                            if(isRightClick)
                            {
                                JPopupMenu criteriaPopup = buildCriteriaPopupMenu(node.isRoot());

                                criteriaPopup.show(me.getComponent(), me.getX(), me.getY());
                            }
                            else if(isDoubleClick)
                            {
                                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);

                                CriteriaEditor editorPage = new CriteriaEditor();
                                editor.setEditorPage(editorPage);

                                editorPage.setOptions(OvalCriteriaOperatorEnum.values());
                                editorPage.setData(criteria);

                                editor.pack();
                                editor.setLocationRelativeTo(EditorMainWindow.getInstance());

                                editor.setVisible(true);

                                if (!editor.wasCancelled())
                                {
                                    // force user entered data to be populated.
                                    editorPage.getData();

                                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();
                                    model.reload(node);
                                    updateSource();
                                }
                            }
                        }
                        else if(userObj instanceof Criterion)
                        {
                            Criterion criterion = (Criterion)userObj;

                            if(isRightClick)
                            {
                                JPopupMenu criterionPopup = buildCriterionPopupMenu();
                                criterionPopup.show(me.getComponent(), me.getX(), me.getY());
                            }
                            else if(isDoubleClick)
                            {
                                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                                editor.setData(criterion);

                                CriterionEditor criterionEditorPage = new CriterionEditor(editor, criterion);
                                editor.setEditorPage(criterionEditorPage);

                                editor.pack();
                                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                                editor.setVisible(true);

                                if (!editor.wasCancelled())
                                {
                                    editor.getData(); // called to populate criteria we
                                    // passed in with the changes the user made

                                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();
                                    model.reload(node);
									updateSource();
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
    }

    private void initTree()
    {
       ToolTipManager.sharedInstance().registerComponent(criteriaTree);
       criteriaTree.setCellRenderer(new CriteriaTreeCellRenderer());
       CriteriaEditorTreeModel treeModel = new CriteriaEditorTreeModel(defCriteria);
       criteriaTree.setModel(treeModel);

       initTreeListeners();
    }

	/**
	 * Create a copy of this definition's criteria.
	 *
	 * @return DefaultMutableTreeNode
	 */
    public DefaultMutableTreeNode buildCriteriaNode(OvalDefinition def)
    {
        defCriteria = null;
        Criteria criteria = def.getCriteria();
        if(criteria != null && criteria.getChildren().size() > 0)
        {
            defCriteria = new DefaultMutableTreeNode(criteria);
            buildCriteriaNode(defCriteria);
        }
        else
        {
            // there are no criteria defined
//			criteria = def.createCriteria();
            defCriteria = new DefaultMutableTreeNode(NO_CRITERIA_DEFINED);
//            def.setCriteria(criteria);
        }
        return defCriteria;
    }

    private void buildCriteriaNode(DefaultMutableTreeNode node)
    {
    	CriteriaChild criteriaParent = (CriteriaChild) node.getUserObject();
    	List<CriteriaChild> children = criteriaParent.getChildren();
    	for (CriteriaChild child : children)
    	{
    		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
    		node.add(childNode);
    		buildCriteriaNode(childNode);
    	}
    }


	private void showCriteriaChild(CriteriaChild criteriaChild, String indent) {
		if (criteriaChild instanceof Criterion)  {
			Criterion criterion = (Criterion) criteriaChild;
			log.debug(indent + "Criterion " + criterion.getTestId());
		} else if (criteriaChild instanceof ExtendDefinition)  {
			ExtendDefinition extDef = (ExtendDefinition) criteriaChild;
			log.debug(indent + "Extend Def " + extDef.getDefinitionId());
		} else if (criteriaChild instanceof Criteria)  {
			Criteria criteria = (Criteria) criteriaChild;
			log.debug(indent + "Criteria " + criteria.getOperator());
			List<CriteriaChild> subCriteria = criteria.getChildren();
			indent += "--";
			for (CriteriaChild innerChild : subCriteria) {
				showCriteriaChild(innerChild, indent);
			}
		}

	}


    private void initComponents2()
    {
		initTree();
    }

    /** Creates new form DefinitionDetailTab */
    public DefinitionCriteriaDetailTab()
    {
        initComponents();
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

        treeScroller = new javax.swing.JScrollPane();
        criteriaTree = new javax.swing.JTree();
        statusPanel = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        treeScroller.setMinimumSize(new java.awt.Dimension(12, 12));

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        criteriaTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        criteriaTree.setAutoscrolls(true);
        criteriaTree.setMinimumSize(null);
        treeScroller.setViewportView(criteriaTree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        add(treeScroller, gridBagConstraints);

        statusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Status"));
        statusPanel.setMinimumSize(new java.awt.Dimension(12, 12));
        statusPanel.setLayout(new java.awt.GridBagLayout());

        statusLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 0.01;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 5, 5);
        statusPanel.add(statusLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        add(statusPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree criteriaTree;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JScrollPane treeScroller;
    // End of variables declaration//GEN-END:variables

    // End of variables declaration

    public OvalDefinition getDoc()
    {
        return ovalDef;
    }

    public void setDoc(OvalDefinition doc, SCAPDocumentTypeEnum docType)
    {
        this.ovalDef = doc;
		buildCriteriaNode(ovalDef);
        criteriaTree.setCellRenderer(new CriteriaTreeCellRenderer());
        CriteriaEditorTreeModel ctm = new CriteriaEditorTreeModel(defCriteria);
        criteriaTree.setModel(ctm);

        Enumeration<DefaultMutableTreeNode> childEnum = defCriteria.depthFirstEnumeration();

        while (childEnum.hasMoreElements())
        {
            DefaultMutableTreeNode child = childEnum.nextElement();

            criteriaTree.expandPath(EditorUtil.getTreePathToChild(child));
        }
    }

    public void setEditorForm(EditorForm form)
    {
        this.editorForm = form;
    }

    public DefinitionSourceDetailTab getSourceTab()
    {
        return sourceTab;
    }

    public void setSourceTab(DefinitionSourceDetailTab sourceTab)
    {
        this.sourceTab = sourceTab;
    }

    private void updateSource()
    {
        sourceTab.setXMLSource(elementToString(ovalDef.getElement()));
    }

    private JPopupMenu buildExtendDefinitionPopupMenu()
    {
        JPopupMenu ret = new JPopupMenu();

        JMenuItem chooseDefMenuItem = new JMenuItem("Edit");
        chooseDefMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selectedTreeObj = criteriaTree.getLastSelectedPathComponent();

                if(selectedTreeObj == null)
                {
                    return;
                }

                if(!(selectedTreeObj instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedTreeObj;

                Object userObj = node.getUserObject();

                if(!(userObj instanceof ExtendDefinition))
                {
                    return;
                }

                ExtendDefinition extendDef = (ExtendDefinition) userObj;

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                ExtendDefinitionEditor editorPage = new ExtendDefinitionEditor(editor, extendDef);
                editor.setEditorPage(editorPage);

                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    editorPage.getData(); // force supplied data to be written to the actual extend def

                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();
                    updateSource();
                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                    model.reload(node);
                }
            }
        });

        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selectedTreeObj = criteriaTree.getLastSelectedPathComponent();

                if(selectedTreeObj == null)
                {
                    return;
                }

                if(!(selectedTreeObj instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedTreeObj;

                Object userObj = node.getUserObject();

                if(!(userObj instanceof ExtendDefinition))
                {
                    return;
                }

                ExtendDefinition extendDef = (ExtendDefinition) userObj;

                Object[] options =
                {
                    "Ok",
                    "Cancel"
                };

                int response = JOptionPane.showOptionDialog(EditorMainWindow.getInstance(),
                        "Are you sure you want to remove the selected definition reference?"
                        + " Press Ok to continue.",
                        "Confirm delete",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);

                if (response == JOptionPane.OK_OPTION)
                {
                    extendDef.getElement().detach();
                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();
                    model.removeNodeFromParent(node);
                    updateSource();
                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);

                    allItemsAreValid();
                }
            }
        });

        ret.add(chooseDefMenuItem);
        ret.add(deleteMenuItem);

        return ret;
    }

    private JMenuItem buildDeleteCriteriaMenuItem()
    {
         JMenuItem deleteMenuItem = null;

        deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selectedTreeObj = criteriaTree.getLastSelectedPathComponent();

                if (selectedTreeObj == null)
                {
                    return;
                }

                if (!(selectedTreeObj instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedTreeObj;

                Object userObj = node.getUserObject();

                if (!(userObj instanceof Criteria))
                {
                    return;
                }

                Criteria criteria = (Criteria) userObj;

                Object[] options =
                {
                    "Ok",
                    "Cancel"
                };
                int response = JOptionPane.showOptionDialog(EditorMainWindow.getInstance(),
                        "Are you sure you want to remove the selected criteria and it's children?" + " Press Ok to continue.",
                        "Confirm delete",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);

                if (response == JOptionPane.OK_OPTION)
                {
                    criteria.getElement().detach();

                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();
                    if (!node.isRoot())
                    {
                            model.removeNodeFromParent(node);
                    } else
                    {
                            defCriteria.setUserObject(NO_CRITERIA_DEFINED);
                            ovalDef.setCriteria(null);
                            model.reload();
                    }
                    updateSource();
                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                    allItemsAreValid();
                }
            }
        });

         return deleteMenuItem;
    }

    private JMenuItem buildAddCriteriaMenuItem()
    {
        JMenuItem addMenuItem = null;

        addMenuItem = new JMenuItem("Add criteria");
        addMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selectedTreeObj = criteriaTree.getLastSelectedPathComponent();

                if (selectedTreeObj == null)
                {
					log.error("Add Criteria - no selected Node, ignoring");
                    return;
                }

                if (!(selectedTreeObj instanceof DefaultMutableTreeNode))
                {
					log.error("Add Criteria - selected tree node not DefaultMutableTreeNode, ignoring");
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedTreeObj;
				Criteria parentCriteria = null;
                Object userObj = node.getUserObject();
                if (userObj instanceof String)
                {
                        parentCriteria = null;  // there is no criteria in the definition yet
                }
                else if (userObj instanceof Criteria)
                {
                    parentCriteria = (Criteria) userObj;
                }
                else
                {
                    log.error("Add Criteria - selected tree node not Criteria node, but " + userObj.getClass().getName() + ", ignoring");
                    return;
                }
				
                Criteria newCriteria = ovalDef.createCriteria();

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                CriteriaEditor editorPage = new CriteriaEditor();

                editorPage.setOptions(OvalCriteriaOperatorEnum.values());
                editorPage.setData(newCriteria);

                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    // force user entered data to be populated
                    editorPage.getData();

                    if (parentCriteria != null)
                    {
                            DefaultMutableTreeNode childCriteriaNode = new DefaultMutableTreeNode(newCriteria);
                            node.add(childCriteriaNode);
                            parentCriteria.addChild(newCriteria);
                    }
                    else
                    {
                            ovalDef.setCriteria(newCriteria);
                            defCriteria.setUserObject(newCriteria);
                            node = defCriteria;
                    }

                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel)criteriaTree.getModel();
                    model.nodeStructureChanged(node);
                    model.reload(node);
                    updateSource();
                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);

                    allItemsAreValid();
                }
            }
        });

         return addMenuItem;
    }

    private JPopupMenu buildRootMenu()
    {
        JPopupMenu ret = new JPopupMenu();
        ret.add(buildAddCriteriaMenuItem());
        return ret;
    }

    private JPopupMenu buildCriteriaPopupMenu(boolean isRoot)
    {
        JPopupMenu ret = new JPopupMenu();
        JMenuItem editMenuItem = new JMenuItem("Edit");
        editMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selectedTreeObj = criteriaTree.getLastSelectedPathComponent();

                if(selectedTreeObj == null)
                {
                    return;
                }

                if(!(selectedTreeObj instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedTreeObj;

                Object userObj = node.getUserObject();

                if(!(userObj instanceof Criteria))
                {
                    return;
                }

                Criteria criteria = (Criteria) userObj;

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);

                CriteriaEditor editorPage = new CriteriaEditor();
                editor.setEditorPage(editorPage);

                editorPage.setOptions(OvalCriteriaOperatorEnum.values());
                editorPage.setData(criteria);

                editor.pack();
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    // for user supplied data to be populated
                    editorPage.getData();

                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();
					updateSource();
                    model.reload(node);
                    updateSource();
                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                }
            }
        });
        ret.add(editMenuItem);

        JMenuItem deleteMenuItem = null;
        deleteMenuItem = buildDeleteCriteriaMenuItem();
        ret.add(deleteMenuItem);

        JMenuItem addCriteriaMenuItem = buildAddCriteriaMenuItem();
        ret.add(addCriteriaMenuItem);

        JMenuItem addCriterionMenuItem = new JMenuItem("Add criterion");
        addCriterionMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selectedTreeObj = criteriaTree.getLastSelectedPathComponent();

                if(selectedTreeObj == null)
                {
                    log.error("Add Criterion - no selected tree node, ignoring");
                    return;
                }

                if(!(selectedTreeObj instanceof DefaultMutableTreeNode))
                {
                    log.error("Add Criterion - selected tree node not DefaultMutableTreeNode, ignoring");
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedTreeObj;

                Object userObj = node.getUserObject();

                if(!(userObj instanceof Criteria))
                {
                    log.error("Add Criterion - selected (parent) node not Criteria, ignoring");
                    return;
                }
                Criteria criteria = (Criteria) userObj;

                Criterion criterion = criteria.createCriterion();

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                CriterionEditor editorPage = new CriterionEditor(editor, criterion);
                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    editor.getData();  // force criterion to have it's fields populated
                                       // from user input
                   node.add(new DefaultMutableTreeNode(criterion));
				   criteria.addChild((CriteriaChild)criterion);
				   CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();
                   model.nodeStructureChanged(node);
                   model.reload(node);
                   updateSource();
                   EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);

                   allItemsAreValid();
                }
            }
        });
        ret.add(addCriterionMenuItem);

        JMenuItem addMultipleCriterionFromTestsMenuItem = new JMenuItem("Add criterion elements via tests");
        addMultipleCriterionFromTestsMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selectedTreeObj = criteriaTree.getLastSelectedPathComponent();

                if(selectedTreeObj == null)
                {
                    return;
                }

                if(!(selectedTreeObj instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedTreeObj;

                Object userObj = node.getUserObject();

                if(!(userObj instanceof Criteria))
                {
                    return;
                }
                Criteria criteria = (Criteria) userObj;

                OvalDefinitionsDocument odd = ovalDef.getParentDocument();

                List<OvalTest> allTests = odd.getOvalTests();

                if(allTests == null || allTests.size() == 0)
                {
                    JOptionPane.showMessageDialog(EditorMainWindow.getInstance(), "No tests are defined. Please define some tests before "
                            + "using this option.", "No tests defined", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // we got here, so there are some tests already defined.
                MultipleCriterionAdder criterionAdder = new MultipleCriterionAdder(EditorMainWindow.getInstance(), true, allTests);

                criterionAdder.pack();
                criterionAdder.setLocationRelativeTo(EditorMainWindow.getInstance());

                criterionAdder.setVisible(true);

                if (!criterionAdder.wasCancelled())
                {
                    List<OvalTest> chosenTests = criterionAdder.getChosenTests();

                    criterionAdder.dispose();

                    HashSet<String> existingCriterionTestIds = new HashSet<String>();

                    // find out which tests are already referenced here
                    int nodeChildCount = node.getChildCount();

                    if(nodeChildCount > 0)
                    {
                        for(int x = 0; x < nodeChildCount ; x++)
                        {
                            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)node.getChildAt(x);

                            Object childUO = childNode.getUserObject();

                            if(childUO != null)
                            {
                                if(childUO instanceof Criterion)
                                {
                                    Criterion childCriterion = (Criterion) childUO;

                                    String testId = childCriterion.getTestId();
                                    if(testId != null)
                                    {
                                        existingCriterionTestIds.add(testId);
                                    }
                                }
                            }
                        }
                    }

                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();

                    for(int x = 0; x < chosenTests.size(); x++)
                    {
                        OvalTest ot = chosenTests.get(x);

                        if(existingCriterionTestIds.contains(ot.getId()))
                        {
                            continue;
                        }
                        Criterion criterion = criteria.createCriterion();
                        criterion.setComment(ot.getComment());
                        criterion.setTestId(ot.getId());

                        criteria.getElement().addContent(criterion.getElement());
                        node.add(new DefaultMutableTreeNode(criterion));
                        model.nodeStructureChanged(node);
                    }
                    updateSource();
                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);

                    allItemsAreValid();
                }
            }
        });
        ret.add(addMultipleCriterionFromTestsMenuItem);

        JMenuItem addExtendDefRefMenuItem = new JMenuItem("Add definition reference");
        addExtendDefRefMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selectedTreeObj = criteriaTree.getLastSelectedPathComponent();

                if(selectedTreeObj == null)
                {
                    return;
                }

                if(!(selectedTreeObj instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedTreeObj;

                Object userObj = node.getUserObject();

                if(!(userObj instanceof Criteria))
                {
                    return;
                }
                Criteria criteria = (Criteria) userObj;

                // Create a new definition reference(read extend definition) in this criteria
                ExtendDefinition extendDef = criteria.createExtendDefinition();

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                ExtendDefinitionEditor editorPage = new ExtendDefinitionEditor(editor, extendDef);
                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    editor.getData();  // force extendDef to have it's fields populated
                                       // from user input

                    Criteria parentCriteria = (Criteria) userObj;
                    parentCriteria.getElement().addContent(extendDef.getElement());

                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();

                    node.add(new DefaultMutableTreeNode(extendDef));
                    model.nodeStructureChanged(node);
                    model.reload(node);
		    updateSource();
                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                }
            }
        });

        ret.add(addExtendDefRefMenuItem);

        return ret;
    }

    private JPopupMenu buildCriterionPopupMenu()
    {
        JPopupMenu ret = new JPopupMenu();
        JMenuItem editMenuItem = new JMenuItem("Edit");
        editMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selectedTreeObj = criteriaTree.getLastSelectedPathComponent();

                if(selectedTreeObj == null)
                {
                    return;
                }

                if(!(selectedTreeObj instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedTreeObj;

                Object userObj = node.getUserObject();

                if(!(userObj instanceof Criterion))
                {
                    return;
                }

                Criterion criterion = (Criterion) userObj;

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                editor.setData(criterion);

                CriterionEditor criterionEditorPage = new CriterionEditor(editor, criterion);
                editor.setEditorPage(criterionEditorPage);

                editor.pack();
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    editor.getData(); // called to populate criteria we
                                      // passed in with the changes the user made

                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();
                    model.reload(node);
                    updateSource();
                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                }
            }
        });

        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Object selectedTreeObj = criteriaTree.getLastSelectedPathComponent();

                if(selectedTreeObj == null)
                {
                    return;
                }

                if(!(selectedTreeObj instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedTreeObj;

                Object userObj = node.getUserObject();

                if(!(userObj instanceof Criterion))
                {
                    return;
                }

                Criterion criterion = (Criterion) userObj;

                Object[] options =
                {
                    "Ok",
                    "Cancel"
                };
                int response = JOptionPane.showOptionDialog(EditorMainWindow.getInstance(),
                        "Are you sure you want to remove the selected criterion?"
                        + " Press Ok to continue.",
                        "Confirm delete",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);

                if (response == JOptionPane.OK_OPTION)
                {
                    criterion.getElement().detach();

                    CriteriaEditorTreeModel model = (CriteriaEditorTreeModel) criteriaTree.getModel();
                    model.removeNodeFromParent(node);
                    updateSource();
                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);

                    allItemsAreValid();
                }
            }
        });

        ret.add(editMenuItem);
        ret.add(deleteMenuItem);

        return ret;
    }
}
