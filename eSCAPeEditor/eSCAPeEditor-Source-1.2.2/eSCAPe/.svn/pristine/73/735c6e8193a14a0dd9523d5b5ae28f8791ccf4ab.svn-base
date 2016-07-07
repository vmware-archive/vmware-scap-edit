package com.g2inc.scap.editor.gui.windows.oval.object;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.cellrenderers.tree.oval.object.ObjectTreeCellRenderer;
import com.g2inc.scap.editor.gui.choosers.Chooser;
import com.g2inc.scap.editor.gui.choosers.object.ObjectChooser;
import com.g2inc.scap.editor.gui.choosers.state.StateChooser;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.allowedvalues.AllowedValuesEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.bool.BooleanDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.numeric.IntDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.StringDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.object.parameter.ObjectParameterEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.object.selectparms.PickObjectParmsToAddPage;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.object.set.ObjectSetEditor;
import com.g2inc.scap.editor.gui.model.tree.oval.wizard.object.ObjectParametersTreeModel;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorForm;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.ChangeNotifierPanel;
import com.g2inc.scap.editor.gui.windows.common.NavigationButton;
import com.g2inc.scap.editor.gui.windows.oval.OvalEditorForm;
import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.oval.AvailableObjectBehavior;
import com.g2inc.scap.library.domain.oval.OvalEntity;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalObjectChild;
import com.g2inc.scap.library.domain.oval.OvalObjectChildComparator;
import com.g2inc.scap.library.domain.oval.OvalObjectFilter;
import com.g2inc.scap.library.domain.oval.OvalObjectParameter;
import com.g2inc.scap.library.domain.oval.OvalObjectReference;
import com.g2inc.scap.library.domain.oval.OvalObjectSet;
import com.g2inc.scap.library.domain.oval.OvalObjectSetOperatorEnum;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalVariable;
import com.g2inc.scap.library.domain.oval.TypeEnum;
import com.g2inc.scap.library.schema.PlatformNameKey;

public class ObjectParametersDisplayPanel extends ChangeNotifierPanel
{

    private static Logger log = Logger.getLogger(ObjectParametersDisplayPanel.class);
    private static final String NO_PARMS_ROOT_TEXT = "No parameters, right-click to add parameters.";
    private static final String PARMS_ROOT_TEXT = "Object parameters";
    private boolean modalParent = false;
    private EditorForm parentEditor = null;
    private DocumentListener commentDocListener = null;
    private OvalObject ovalObject = null;
    private Map<String, String> chosenBehaviors = new HashMap<String, String>();

    public void setModalParent(boolean b)
    {
        modalParent = b;
    }

    public void setParentEditor(EditorForm parentEditor)
    {
        this.parentEditor = parentEditor;
    }

    public boolean hasModalParent()
    {
        return modalParent;
    }

    private JMenuItem buildSetAddMenuItem()
    {
        JMenuItem ret = new JMenuItem("Add a set");

        ret.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                OvalObjectSet ooset = ovalObject.createSet();

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                ObjectSetEditor editorPage = new ObjectSetEditor();

                editorPage.selectDefault(ooset.getSetOperator());
                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setVisible(true);

                if (!editor.wasCancelled())
                {
                    ooset.setSetOperator((OvalObjectSetOperatorEnum) editorPage.getData());

                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objParmTree.getLastSelectedPathComponent();

                    if (selectedNode == null)
                    {
                        return;
                    }

                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(ooset);

                    ObjectParametersTreeModel model = (ObjectParametersTreeModel) objParmTree.getModel();

                    selectedNode.add(childNode);

                    model.reload(selectedNode);
                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                    if (root.getChildCount() > 0)
                    {
                        root.setUserObject(PARMS_ROOT_TEXT);
                        model.nodeChanged(root);
                    }
                    else
                    {
                        root.setUserObject(NO_PARMS_ROOT_TEXT);
                        model.nodeChanged(root);
                    }
                    notifyRegisteredListeners();
                }
            }
        });

        return ret;
    }

    private JMenuItem buildSetAddObjectReferenceMenuItem()
    {
        JMenuItem ret = new JMenuItem("Add object reference");

        ret.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                // launch an object chooser
                Chooser c = new ObjectChooser(true);
                c.setSource(ovalObject.getParentDocument(), null, null);

                c.setVisible(true);

                if (!c.wasCancelled())
                {
                    OvalObject chosen = (OvalObject) c.getChosen();

                    OvalObjectReference ref = chosen.createObjectReference();

                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objParmTree.getLastSelectedPathComponent();

                    if (selectedNode == null)
                    {
                        return;
                    }

                    DefaultMutableTreeNode refChild = new DefaultMutableTreeNode(ref);
                    ObjectParametersTreeModel model = (ObjectParametersTreeModel) objParmTree.getModel();

                    selectedNode.add(refChild);
                    sortChildren(selectedNode);
                    model.reload(selectedNode);

                    notifyRegisteredListeners();
                }
            }
        });
        return ret;
    }

    private JMenuItem buildAddParameterMenuItem()
    {
        JMenuItem ret = new JMenuItem("Add parameters");
        ret.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objParmTree.getLastSelectedPathComponent();

                if (selectedNode == null)
                {
                    return;
                }

                List<OvalEntity> validParms = ovalObject.getValidParameters();

                if (validParms == null || validParms.size() == 0)
                {
                    log.error(ovalObject.getId() + " reports no valid parameters! validParms" + (validParms == null ? " is " : " is not ") + "null");
                    return;
                }

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                PickObjectParmsToAddPage editorPage = new PickObjectParmsToAddPage();
                editor.setEditorPage(editorPage);

                Set<String> alreadyAddedParmNames = EditorUtil.getExistingObjectParameters(selectedNode);

                editorPage.setExisting(alreadyAddedParmNames);
                editorPage.setData(validParms);
                editor.pack();
                editor.setVisible(true);

                if (!editor.wasCancelled())
                {
                    Object[] chosenParms = (Object[]) editorPage.getData();

                    if (chosenParms != null && chosenParms.length > 0)
                    {
                        for (int x = 0; x < chosenParms.length; x++)
                        {
                            OvalEntity oe = (OvalEntity) chosenParms[x];

                            OvalObjectParameter oop = ovalObject.createObjectParameter(oe.getName());
                            DefaultMutableTreeNode child = new DefaultMutableTreeNode(oop);
                            insertObjectParameter(selectedNode, child, validParms);
                        }

                        DefaultTreeModel model = (DefaultTreeModel) objParmTree.getModel();
                        model.reload(selectedNode);

                        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                        if(root.getChildCount() > 0)
                        {
                            root.setUserObject(PARMS_ROOT_TEXT);
                            model.nodeChanged(root);
                        }
                        else
                        {
                            root.setUserObject(NO_PARMS_ROOT_TEXT);
                            model.nodeChanged(root);
                        }
                        notifyRegisteredListeners();
                    }
                }
            }
        });

        return ret;
    }

    private void insertObjectParameter(DefaultMutableTreeNode ovalObject, DefaultMutableTreeNode objectParameter, List<OvalEntity> validParms)
    {
        HashMap<String, Integer> parmOrder = new HashMap<String, Integer>();
        for (int i = 0; i < validParms.size(); i++)
        {
            parmOrder.put(validParms.get(i).getName(), i);
        }
        String newParmName = ((SCAPElementImpl) objectParameter.getUserObject()).getElementName();
        int newParmOrder = parmOrder.get(newParmName);
        int i = 0;
        for (; i < ovalObject.getChildCount(); i++)
        {
            DefaultMutableTreeNode existingNode = (DefaultMutableTreeNode) ovalObject.getChildAt(i);
            String existingParmName = ((SCAPElementImpl) existingNode.getUserObject()).getElementName();
            int existingParmOrder = parmOrder.get(existingParmName);
            if (newParmOrder < existingParmOrder)
            {
                break;
            }
        }
        ovalObject.insert(objectParameter, i);
    }

    private JMenuItem buildRemoveMenuItem()
    {
        JMenuItem ret = new JMenuItem("Remove");

        ret.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objParmTree.getLastSelectedPathComponent();

                ObjectParametersTreeModel model = (ObjectParametersTreeModel) objParmTree.getModel();

                DefaultMutableTreeNode pNode = (DefaultMutableTreeNode) selectedNode.getParent();

                pNode.remove(selectedNode);
                if(selectedNode.getUserObject() != null && selectedNode.getUserObject() instanceof OvalObjectChild)
                {
                    OvalObjectChild ooc = (OvalObjectChild) selectedNode.getUserObject();

                    ooc.getElement().detach();
                }

                model.reload(pNode);
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                if (root.getChildCount() > 0)
                {
                    root.setUserObject(PARMS_ROOT_TEXT);
                    model.nodeChanged(root);
                }
                else
                {
                    root.setUserObject(NO_PARMS_ROOT_TEXT);
                    model.nodeChanged(root);
                }
                
                notifyRegisteredListeners();
            }
        });

        return ret;
    }

    private JMenuItem buildSetAddFilterMenuItem()
    {
        JMenuItem ret = new JMenuItem("Add filter");

        ret.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                // launch a state chooser
                Chooser c = new StateChooser(true);
                c.setSource(ovalObject.getParentDocument(), null, null);

                c.setVisible(true);

                if (!c.wasCancelled())
                {
                    OvalState chosen = (OvalState) c.getChosen();

                    OvalObjectFilter filter = chosen.createObjectFilter();

                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objParmTree.getLastSelectedPathComponent();

                    if (selectedNode == null)
                    {
                        return;
                    }

                    DefaultMutableTreeNode filterChild = new DefaultMutableTreeNode(filter);
                    ObjectParametersTreeModel model = (ObjectParametersTreeModel) objParmTree.getModel();

                    selectedNode.add(filterChild);
                    sortChildren(selectedNode);

                    model.reload(selectedNode);
                    notifyRegisteredListeners();
                }
            }
        });
        return ret;
    }

    private void sortChildren(DefaultMutableTreeNode selectedNode)
    {
        int childCount = selectedNode.getChildCount();

        List<DefaultMutableTreeNode> nodeChildren = new ArrayList<DefaultMutableTreeNode>(childCount);

        for(int x = 0; x < childCount; x++)
        {
            nodeChildren.add((DefaultMutableTreeNode) selectedNode.getChildAt(x));
        }

        OvalObjectChildComparator oocc = new OvalObjectChildComparator(ovalObject);
        Collections.sort(nodeChildren, oocc);

        selectedNode.removeAllChildren();

        for(DefaultMutableTreeNode childNode: nodeChildren)
        {
            selectedNode.add(childNode);
        }
    }

    private JMenuItem buildSetEditMenuItem()
    {
        JMenuItem ret = new JMenuItem("Edit");

        ret.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objParmTree.getLastSelectedPathComponent();

                if (selectedNode == null)
                {
                    return;
                }

                OvalObjectSet ooset = (OvalObjectSet) selectedNode.getUserObject();

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                ObjectSetEditor editorPage = new ObjectSetEditor();

                editorPage.selectDefault(ooset.getSetOperator());
                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setVisible(true);

                if (!editor.wasCancelled())
                {
                    ooset.setSetOperator((OvalObjectSetOperatorEnum) editorPage.getData());


                    if (selectedNode.isRoot())
                    {
                        selectedNode.setUserObject(PARMS_ROOT_TEXT);
                    }
                    ObjectParametersTreeModel model = (ObjectParametersTreeModel) objParmTree.getModel();

                    model.reload(selectedNode);
                    notifyRegisteredListeners();
                }
            }
        });
        return ret;
    }

    // build the menu that pops up when you right click on a parameter
    private JPopupMenu buildParameterPopupMenu()
    {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem editMenuItem = new JMenuItem("Edit");
        editMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objParmTree.getLastSelectedPathComponent();
                OvalObjectParameter oop = (OvalObjectParameter) selectedNode.getUserObject();

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                ObjectParameterEditor editorPage = new ObjectParameterEditor(ovalObject, oop);
                editor.setEditorPage(editorPage);

                editor.pack();
                editor.setVisible(true);

                if (!editor.wasCancelled())
                {
                    editorPage.getData();

                    DefaultTreeModel model = (DefaultTreeModel) objParmTree.getModel();
                    model.reload(selectedNode);
                    notifyRegisteredListeners();
                }
            }
        });

        JMenuItem removeMenuItem = buildRemoveMenuItem();

        menu.add(editMenuItem);
        menu.add(removeMenuItem);

        return menu;
    }

    // build the menu that pops up when you right click on an object reference
    private JPopupMenu buildObjectReferencePopupMenu()
    {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem editMenuItem = new JMenuItem("Edit");
        editMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objParmTree.getLastSelectedPathComponent();

                OvalObjectReference oor = (OvalObjectReference) selectedNode.getUserObject();

                Chooser c = new ObjectChooser(true);
                c.setSource(ovalObject.getParentDocument(), null, null);

                c.setVisible(true);

                if (!c.wasCancelled())
                {
                    OvalObject oo = (OvalObject) c.getChosen();
                    oor.setObject(oo);

                    DefaultTreeModel model = (DefaultTreeModel) objParmTree.getModel();
                    model.reload(selectedNode);
                    notifyRegisteredListeners();
                }

            }
        });

        JMenuItem removeMenuItem = buildRemoveMenuItem();

        menu.add(editMenuItem);
        menu.add(removeMenuItem);

        return menu;
    }

    // build the menu that pops up when you right click on an object filter
    private JPopupMenu buildObjectFilterPopupMenu()
    {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem editMenuItem = new JMenuItem("Edit");
        editMenuItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objParmTree.getLastSelectedPathComponent();

                OvalObjectFilter oof = (OvalObjectFilter) selectedNode.getUserObject();

                Chooser c = new StateChooser(true);
                c.setSource(ovalObject.getParentDocument(), null, null);

                c.setVisible(true);

                if (!c.wasCancelled())
                {
                    OvalState os = (OvalState) c.getChosen();
                    oof.setState(os);

                    DefaultTreeModel model = (DefaultTreeModel) objParmTree.getModel();
                    model.reload(selectedNode);
                    notifyRegisteredListeners();
                }
            }
        });

        JMenuItem removeMenuItem = buildRemoveMenuItem();

        menu.add(editMenuItem);
        menu.add(removeMenuItem);

        return menu;
    }

    // build the menu that pops up when you right click on a set
    private JPopupMenu buildSetPopupMenu()
    {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem editMenuItem = buildSetEditMenuItem();
        JMenuItem removeMenuItem = buildRemoveMenuItem();

        JMenuItem addSetMenuItem = buildSetAddMenuItem();
        JMenuItem addObjRefMenuItem = buildSetAddObjectReferenceMenuItem();
        JMenuItem addFilterMenuItem = buildSetAddFilterMenuItem();
        menu.add(editMenuItem);
        menu.add(removeMenuItem);

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objParmTree.getLastSelectedPathComponent();
        int cc = selectedNode.getChildCount();

        if (cc == 0)
        {
            menu.add(addSetMenuItem);
            menu.add(addObjRefMenuItem);
        }
        else
        {
            int cSetCount = EditorUtil.getChildSetCount(selectedNode);
            int cObjCount = EditorUtil.getChildObjRefCount(selectedNode);

            if (cSetCount > 0)
            {
                if (cSetCount < 2)
                {
                    menu.add(addSetMenuItem);
                }
            }
            else if (cObjCount > 0)
            {
                if (cObjCount < 2)
                {
                    menu.add(addObjRefMenuItem);
                }

                menu.add(addFilterMenuItem);
            }
        }

        return menu;
    }

    // build the menu the pops up when you right click on the root of the tree
    private JPopupMenu buildRootPopupMenu(DefaultMutableTreeNode rootNode)
    {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem addSetMenuItem = buildSetAddMenuItem();
        JMenuItem addParameterMenuItem = buildAddParameterMenuItem();

        int cc = rootNode.getChildCount();

        if (cc == 0)
        {
            // they have the choice to add either a set or parameters
            menu.add(addSetMenuItem);
            menu.add(addParameterMenuItem);
        }
        else
        {
            int nonSetcount = EditorUtil.getChildNonSetCount(rootNode);

            if (nonSetcount > 0)
            {
                // since the non-set children exist,
                // we know that there aren't going to be any sets
                menu.add(addParameterMenuItem);
            }
            else
            {
                menu = null;
            }
        }

        return menu;
    }

    private void initTreeListeners()
    {
        objParmTree.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseClicked(MouseEvent me)
            {
                boolean isDoubleClick = false;
                boolean isRightClick = false;

                if (me.getClickCount() > 1)
                {
                    isDoubleClick = true;
                }

                if (me.getButton() != 1)
                {
                    isRightClick = true;
                }

                if (!objParmTree.isEnabled())
                {
                    return;
                }

                TreePath path = objParmTree.getPathForLocation(me.getX(), me.getY());

                if (path == null)
                {
                    return;
                }

                objParmTree.setSelectionPath(path);

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objParmTree.getLastSelectedPathComponent();
                Object userObj = selectedNode.getUserObject();

                if (selectedNode.isRoot())
                {
                    if (isRightClick)
                    {
                        // show root popup menu
                        JPopupMenu rootMenu = buildRootPopupMenu(selectedNode);

                        if(rootMenu != null)
                        {
                            rootMenu.show(me.getComponent(), me.getX(), me.getY());
                        }
                    }
                }
                else
                {
                    // show context sensitive menus
                    if (userObj instanceof OvalObjectSet)
                    {
                        if (isRightClick)
                        {
                            JPopupMenu menu = buildSetPopupMenu();
                            menu.show(me.getComponent(), me.getX(), me.getY());
                        }
                    }
                    else if (userObj instanceof OvalObjectReference)
                    {
                        if (isRightClick)
                        {
                            JPopupMenu menu = buildObjectReferencePopupMenu();
                            menu.show(me.getComponent(), me.getX(), me.getY());
                        }
                        else if (isDoubleClick)
                        {
                            if (parentEditor == null || modalParent)
                            {
                                return;
                            }

                            OvalObjectReference oor = (OvalObjectReference) userObj;
                            OvalObject oo = oor.getObject();

                            if (oo != null)
                            {
                                NavigationButton nb = new NavigationButton();
                                nb.setText("OvalObject");
                                nb.setToolTipText(ovalObject.getId());
                                nb.setSelectedElement(parentEditor.getSelectedPath());
                                nb.setEditorForm(parentEditor);

                                EditorMainWindow.getInstance().getNavPanel().addNavigationButton(nb);

                                // now set selection to the item the user intended
                                ((OvalEditorForm) parentEditor).selectObject(oo);
                            }
                        }
                    }
                    else if (userObj instanceof OvalObjectParameter)
                    {
                        if (isRightClick)
                        {
                            JPopupMenu menu = buildParameterPopupMenu();
                            menu.show(me.getComponent(), me.getX(), me.getY());
                        }
                        else if (isDoubleClick)
                        {
                            if (parentEditor == null || modalParent)
                            {
                                return;
                            }

                            OvalObjectParameter oop = (OvalObjectParameter) userObj;

                            OvalVariable ov = oop.getVariableReference();

                            if (ov != null)
                            {
                                NavigationButton nb = new NavigationButton();
                                nb.setText("OvalObject");
                                nb.setToolTipText(ovalObject.getId());
                                nb.setSelectedElement(parentEditor.getSelectedPath());
                                nb.setEditorForm(parentEditor);

                                EditorMainWindow.getInstance().getNavPanel().addNavigationButton(nb);

                                // now set selection to the item the user intended
                                ((OvalEditorForm) parentEditor).selectVariable(ov);
                            }
                        }
                    }
                    else if (userObj instanceof OvalObjectFilter)
                    {
                        if (isRightClick)
                        {
                            JPopupMenu menu = buildObjectFilterPopupMenu();
                            menu.show(me.getComponent(), me.getX(), me.getY());
                        }
                        else if (isDoubleClick)
                        {
                            if (parentEditor == null || modalParent)
                            {
                                return;
                            }
                            OvalObjectFilter oof = (OvalObjectFilter) userObj;
                            OvalState os = oof.getState();

                            if (os != null)
                            {
                                NavigationButton nb = new NavigationButton();
                                nb.setText("OvalObject");
                                nb.setToolTipText(ovalObject.getId());
                                nb.setSelectedElement(parentEditor.getSelectedPath());
                                nb.setEditorForm(parentEditor);

                                EditorMainWindow.getInstance().getNavPanel().addNavigationButton(nb);

                                // now set selection to the item the user intended
                                ((OvalEditorForm) parentEditor).selectState(os);
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
        ToolTipManager.sharedInstance().registerComponent(objParmTree);
        ObjectParametersTreeModel model = new ObjectParametersTreeModel(new DefaultMutableTreeNode(NO_PARMS_ROOT_TEXT));
        objParmTree.setModel(model);
        objParmTree.setCellRenderer(new ObjectTreeCellRenderer());
        initTreeListeners();
    }

    private void initTextFields()
    {
        commentDocListener = new DocumentListener()
        {

            private void common(DocumentEvent de)
            {
                // text has changed, notify everyone
                // who's listening for changes
                
                notifyRegisteredListeners();
            }

            @Override
            public void insertUpdate(DocumentEvent de)
            {
                common(de);
            }

            @Override
            public void removeUpdate(DocumentEvent de)
            {
                common(de);
            }

            @Override
            public void changedUpdate(DocumentEvent de)
            {
                common(de);
            }
        };

        commentTextField.getDocument().addDocumentListener(commentDocListener);
    }

    private void initComboBoxes()
    {
        behaviorCombo.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                if (chosenBehaviors != null && chosenBehaviors.size() > 0)
                {
                    AvailableObjectBehavior aob = (AvailableObjectBehavior) behaviorCombo.getSelectedItem();

                    String name = aob.getName();

                    if (!chosenBehaviors.keySet().contains(name))
                    {
                        addBehaviorButton.setEnabled(true);
                    }
                    else
                    {
                        addBehaviorButton.setEnabled(false);
                    }
                }

            }
        });
    }

    private void initButtons()
    {
        addBehaviorButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                AvailableObjectBehavior aob = (AvailableObjectBehavior) behaviorCombo.getSelectedItem();

                String name = aob.getName();

                chosenBehaviors.put(name, aob.getDefaultValue());
                // the code below is commented out, because the ability of a style 
                // to override the recurse_direction behavior default has been added,
                // so this hard-coded value is no longer used. Any overrides made by a
                // style are applied when setContentStyle was called for this document.
//                if (name.equals("recurse_direction"))
//                {
//                    // this is contrary to schema default
//                    chosenBehaviors.put(name, "down");
//                }

                behaviorCheckbox.setSelected(true);

                addBehaviorButton.setEnabled(false);

                DefaultListModel listModel = (DefaultListModel) behaviorListBox.getModel();
                listModel.clear();
                if (chosenBehaviors != null && chosenBehaviors.size() > 0)
                {
//                        behaviorCheckbox.setEnabled(true);

                    Iterator<String> cbKeysItr = chosenBehaviors.keySet().iterator();

                    while (cbKeysItr.hasNext())
                    {
                        String key = cbKeysItr.next();

                        String val = chosenBehaviors.get(key);

                        BehaviorPair bp = new BehaviorPair(key, val);

                        listModel.addElement(bp);
                    }
                }

                editBehaviorButton.setEnabled(false);
                removeBehaviorButton.setEnabled(false);

                ovalObject.setBehaviors(chosenBehaviors);

                
                notifyRegisteredListeners();
            }
        });

        editBehaviorButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                BehaviorPair bp = (BehaviorPair) behaviorListBox.getSelectedValue();
                int index = ((DefaultListModel) behaviorListBox.getModel()).indexOf(bp);

                String elementName = ovalObject.getElementName();
                String platform = ovalObject.getPlatform();

                PlatformNameKey pkey = new PlatformNameKey(platform, elementName);

                List<AvailableObjectBehavior> behaviors = ovalObject.getParentDocument().getBehaviors(pkey);

                boolean foundEditor = false;
                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);

                for (int x = 0; x < behaviors.size(); x++)
                {
                    AvailableObjectBehavior aob = behaviors.get(x);

                    if (aob.getName().equals(bp.name))
                    {
                        TypeEnum te = aob.getDatatype();

                        switch (te)
                        {
                            case BOOLEAN:
                                foundEditor = true;
                                BooleanDatatypeEditor bEditorPage = new BooleanDatatypeEditor();
                                bEditorPage.setData(bp.value);
                                editor.setEditorPage(bEditorPage);
                                break;
                            case STRING:
                                foundEditor = true;
                                StringDatatypeEditor sEditorPage = new StringDatatypeEditor();
                                AllowedValuesEditor avEditorPage = new AllowedValuesEditor();

                                if (aob.getValuesAllowed().size() > 0)
                                {
                                    avEditorPage.setAllowedValues(aob.getValuesAllowed());
                                    avEditorPage.setSelectedValue(bp.value);
                                    editor.setEditorPage(avEditorPage);
                                }
                                else
                                {
                                    sEditorPage.setData(bp.value);
                                    editor.setEditorPage(sEditorPage);
                                }
                                break;
                            case INT:
                                foundEditor = true;
                                IntDatatypeEditor iEditorPage = new IntDatatypeEditor();
                                editor.setEditorPage(iEditorPage);
                                iEditorPage.setData(bp.value);
                                break;
                        }

                        break;
                    }
                }

                if (foundEditor)
                {
                    editor.pack();
                    editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                    editor.setVisible(true);

                    if (!editor.wasCancelled())
                    {
                        Object data = editor.getData();
                        if (data instanceof Boolean)
                        {
                            bp.value = ((Boolean) editor.getData()).toString();
                        }
                        else
                        {
                            bp.value = (String) editor.getData();
                        }
                        ((DefaultListModel) behaviorListBox.getModel()).remove(index);
                        ((DefaultListModel) behaviorListBox.getModel()).addElement(bp);

                        chosenBehaviors.put(bp.name, bp.value);

                        ovalObject.setBehaviors(chosenBehaviors);

                        
                        notifyRegisteredListeners();
                    }
                }
            }
        });

        removeBehaviorButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                Object[] selectedValues = behaviorListBox.getSelectedValues();

                if (selectedValues == null || selectedValues.length == 0)
                {
                    return;
                }

                for (int x = 0; x < selectedValues.length; x++)
                {
                    BehaviorPair bp = (BehaviorPair) selectedValues[x];

                    chosenBehaviors.remove(bp.name);
                    ((DefaultListModel) behaviorListBox.getModel()).removeElement(bp);
                }

                editBehaviorButton.setEnabled(false);
                removeBehaviorButton.setEnabled(false);

                
                notifyRegisteredListeners();
            }
        });
    }

    private void initListboxes()
    {
        DefaultListModel listModel = new DefaultListModel();
        behaviorListBox.setModel(listModel);

        behaviorListBox.addListSelectionListener(new ListSelectionListener()
        {

            @Override
            public void valueChanged(ListSelectionEvent lse)
            {
                int[] selectedIndices = behaviorListBox.getSelectedIndices();

                if (selectedIndices == null || selectedIndices.length == 0)
                {
                    editBehaviorButton.setEnabled(false);
                    removeBehaviorButton.setEnabled(false);
                }
                else if (selectedIndices.length == 1)
                {
                    editBehaviorButton.setEnabled(true);
                    removeBehaviorButton.setEnabled(true);
                }
                else
                {
                    editBehaviorButton.setEnabled(false);
                    removeBehaviorButton.setEnabled(true);
                }
            }
        });
    }

    private void setBehaviorsSectionEnabled(boolean b)
    {
        behaviorCaption.setEnabled(b);
        behaviorCombo.setEnabled(b);
        addBehaviorButton.setEnabled(b);
        behaviorListBox.setEnabled(b);
        removeBehaviorButton.setEnabled(false);
        editBehaviorButton.setEnabled(false);
        behaviorPanel.setEnabled(b);
    }

    private void initCheckBoxes()
    {
        behaviorCheckbox.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (behaviorCheckbox.isSelected())
                {
                    setBehaviorsSectionEnabled(true);
                }
                else
                {
                    setBehaviorsSectionEnabled(false);
                }

                chosenBehaviors.clear();
                ((DefaultListModel) behaviorListBox.getModel()).clear();

                
                notifyRegisteredListeners();
            }
        });
    }

    private void initComponents2()
    {
//        initTree();
        initTextFields();
        initComboBoxes();
        initButtons();
        initCheckBoxes();
        initListboxes();
    }

    /** Creates new form ObjectParametersDisplayPanel */
    public ObjectParametersDisplayPanel()
    {
        initComponents();
        initComponents2();
    }

    public JTree getObjParmTree()
    {
        return objParmTree;
    }

    public void setObjParmTree(JTree objParmTree)
    {
        this.objParmTree = objParmTree;
    }

    public OvalObject getOvalObject()
    {
        return ovalObject;
    }

    public void setOvalObject(OvalObject ovalObject)
    {
        this.ovalObject = ovalObject;
        initTree();
        if (ovalObject.getValidParameters() == null || ovalObject.getValidParameters().size() > 0)
        {
            objParmTree.setEnabled(true);
            ObjectParametersTreeModel tree = new ObjectParametersTreeModel(ovalObject.getChildTreeNodes());
            objParmTree.setModel(tree);
        }
        else
        {
            objParmTree.setEnabled(false);
        }

        commentTextField.getDocument().removeDocumentListener(commentDocListener);
        commentTextField.setText(this.ovalObject.getComment());
        commentTextField.getDocument().addDocumentListener(commentDocListener);

        String elementName = ovalObject.getElementName();
        String platform = ovalObject.getPlatform();

        chosenBehaviors = ovalObject.getBehaviors();

        PlatformNameKey pkey = new PlatformNameKey(platform, elementName);

        List<AvailableObjectBehavior> behaviors = ovalObject.getParentDocument().getBehaviors(pkey);

        behaviorCombo.removeAllItems();

        if (behaviors == null || behaviors.size() == 0)
        {
            behaviorCheckbox.setSelected(false);
            behaviorCheckbox.setEnabled(false);
            setBehaviorsSectionEnabled(false);
        }
        else
        {
            behaviorCheckbox.setEnabled(true);
            setBehaviorsSectionEnabled(true);

            for (int x = 0; x < behaviors.size(); x++)
            {
                AvailableObjectBehavior aob = behaviors.get(x);

                behaviorCombo.addItem(aob);
            }

            if (chosenBehaviors == null || chosenBehaviors.size() == 0)
            {
                behaviorCheckbox.setSelected(false);
            }
            else
            {
                DefaultListModel listModel = (DefaultListModel) behaviorListBox.getModel();
                listModel.clear();

                behaviorCheckbox.setSelected(true);

                Iterator<String> cbKeysItr = chosenBehaviors.keySet().iterator();

                while (cbKeysItr.hasNext())
                {
                    String key = cbKeysItr.next();

                    String val = chosenBehaviors.get(key);

                    BehaviorPair bp = new BehaviorPair(key, val);

                    listModel.addElement(bp);
                }
            }
        }
    }

    public boolean allParmsOk()
    {
        ObjectParametersTreeModel model = (ObjectParametersTreeModel) objParmTree.getModel();
        ovalObject.setParameters((DefaultMutableTreeNode) model.getRoot());
        ovalObject.setBehaviors(chosenBehaviors);

        boolean ret = true;

        if (commentTextField.getText() == null || commentTextField.getText().length() == 0)
        {
            ret = false;
            return ret;
        }
        else
        {
            ovalObject.setComment(commentTextField.getText());
        }

        return ret;
    }

    private class BehaviorPair
    {

        String name = null;
        String value = null;

        public BehaviorPair(String n, String v)
        {
            name = n;
            value = v;
        }

        @Override
        public String toString()
        {
            return name + " = " + value;
        }
    }

    public Map<String, String> getChosenBehaviors()
    {
        return chosenBehaviors;
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

        commentCaption = new javax.swing.JLabel();
        commentTextField = new javax.swing.JTextField();
        behaviorCheckbox = new javax.swing.JCheckBox();
        behaviorPanel = new javax.swing.JPanel();
        behaviorCaption = new javax.swing.JLabel();
        behaviorCombo = new javax.swing.JComboBox();
        addBehaviorButton = new javax.swing.JButton();
        behaviorScroller = new javax.swing.JScrollPane();
        behaviorListBox = new javax.swing.JList();
        listControlPanel = new javax.swing.JPanel();
        editBehaviorButton = new javax.swing.JButton();
        removeBehaviorButton = new javax.swing.JButton();
        treeScroller = new javax.swing.JScrollPane();
        objParmTree = new javax.swing.JTree();

        setLayout(new java.awt.GridBagLayout());

        commentCaption.setText("Comment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 5, 5);
        add(commentCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(commentTextField, gridBagConstraints);

        behaviorCheckbox.setText("Use Behaviors");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(behaviorCheckbox, gridBagConstraints);

        behaviorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Behaviors", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        behaviorPanel.setLayout(new java.awt.GridBagLayout());

        behaviorCaption.setText("Behavior");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 5, 5);
        behaviorPanel.add(behaviorCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        behaviorPanel.add(behaviorCombo, gridBagConstraints);

        addBehaviorButton.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 0, 0);
        behaviorPanel.add(addBehaviorButton, gridBagConstraints);

        behaviorListBox.setMaximumSize(null);
        behaviorListBox.setMinimumSize(null);
        behaviorListBox.setPreferredSize(null);
        behaviorListBox.setVisibleRowCount(4);
        behaviorScroller.setViewportView(behaviorListBox);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        behaviorPanel.add(behaviorScroller, gridBagConstraints);

        listControlPanel.setLayout(new java.awt.GridBagLayout());

        editBehaviorButton.setText("Edit");
        editBehaviorButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        listControlPanel.add(editBehaviorButton, gridBagConstraints);

        removeBehaviorButton.setText("Remove");
        removeBehaviorButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        listControlPanel.add(removeBehaviorButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        behaviorPanel.add(listControlPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(behaviorPanel, gridBagConstraints);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        objParmTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        objParmTree.setMinimumSize(null);
        objParmTree.setName("objParmTree"); // NOI18N
        treeScroller.setViewportView(objParmTree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(treeScroller, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBehaviorButton;
    private javax.swing.JLabel behaviorCaption;
    private javax.swing.JCheckBox behaviorCheckbox;
    private javax.swing.JComboBox behaviorCombo;
    private javax.swing.JList behaviorListBox;
    private javax.swing.JPanel behaviorPanel;
    private javax.swing.JScrollPane behaviorScroller;
    private javax.swing.JLabel commentCaption;
    private javax.swing.JTextField commentTextField;
    private javax.swing.JButton editBehaviorButton;
    private javax.swing.JPanel listControlPanel;
    private javax.swing.JTree objParmTree;
    private javax.swing.JButton removeBehaviorButton;
    private javax.swing.JScrollPane treeScroller;
    // End of variables declaration//GEN-END:variables
}
