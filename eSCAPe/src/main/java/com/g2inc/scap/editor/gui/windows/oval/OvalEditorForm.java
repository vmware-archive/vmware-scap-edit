package com.g2inc.scap.editor.gui.windows.oval;

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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.cellrenderers.tree.oval.OvalDocumentTreeCellRenderer;
import com.g2inc.scap.editor.gui.dialogs.dependency.DependencyNotificationDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.baseid.BaseIdEditor;
import com.g2inc.scap.editor.gui.model.tree.oval.DocumentTreeModel;
import com.g2inc.scap.editor.gui.model.tree.oval.SCAPParentTreeNode;
import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.util.Adder;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorForm;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.GenericSourceDetailTab;
import com.g2inc.scap.editor.gui.windows.common.WeakTreeModelListener;
import com.g2inc.scap.editor.gui.windows.oval.definition.DefinitionCriteriaDetailTab;
import com.g2inc.scap.editor.gui.windows.oval.definition.DefinitionGeneralDetailTab;
import com.g2inc.scap.editor.gui.windows.oval.definition.DefinitionMetadataDetailTab;
import com.g2inc.scap.editor.gui.windows.oval.definition.DefinitionSourceDetailTab;
import com.g2inc.scap.editor.gui.windows.oval.object.ObjectDetailTab;
import com.g2inc.scap.editor.gui.windows.oval.state.StateDetailTab;
import com.g2inc.scap.editor.gui.windows.oval.test.TestDetailTab;
import com.g2inc.scap.editor.gui.windows.oval.variable.VariableDetailTab;
import com.g2inc.scap.editor.gui.wizards.oval.definition.NewDefinitionWizard;
import com.g2inc.scap.editor.gui.wizards.oval.object.create.NewObjectWizard;
import com.g2inc.scap.editor.gui.wizards.oval.state.create.NewStateWizard;
import com.g2inc.scap.editor.gui.wizards.oval.test.NewTestWizard;
import com.g2inc.scap.editor.gui.wizards.oval.variable.NewVariableWizard;
import com.g2inc.scap.library.content.style.ContentStyle;
import com.g2inc.scap.library.content.style.ContentStyleRegistry;
import com.g2inc.scap.library.content.style.ContentStyleViolationElement;
import com.g2inc.scap.library.domain.oval.OvalComparator;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalElement;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVariable;

public class OvalEditorForm extends EditorForm
    implements TreeModelListener, ActionListener, ChangeListener, TreeSelectionListener, TreeWillExpandListener
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(OvalEditorForm.class);

    private WeakReference<OvalDefinitionsDocument>  document = null;

    public static final String WINDOW_TITLE_BASE = EditorMessages.OVAL + " Document - ";
    public static final String TAB_TITLE_DEFINITIONS_DOC = "Definitions Doc";
    public static final String TAB_TITLE_DEFINITION_GENERAL = "General";
    public static final String TAB_TITLE_DEFINITION_METADATA = "Metadata";
    public static final String TAB_TITLE_DEFINITION_CRITERIA = "Criteria";
    public static final String TAB_TITLE_TEST = "Test";
    public static final String TAB_TITLE_OBJECT = "Object";
    public static final String TAB_TITLE_STATE = "State";
    public static final String TAB_TITLE_VARIABLE = "Variable";
    public static final String TAB_TITLE_XML_PROPERTIES = "XML Properties";
    public static final String TAB_TITLE_SOURCE = "Source";
    private WeakReference<EditorMainWindow> parentWin = null;
    private ContentStyle currentStyle = null;
    private String searchText = null;

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

    private JMenuItem createAddDefMenuItem()
    {
        JMenuItem ret = null;

        ret = new JMenuItem("Add a definition");

        ret.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                NewDefinitionWizard wiz = new NewDefinitionWizard(getParentWin(), true, getDocument());

                wiz.pack();
                wiz.setVisible(true);

                if (!wiz.wasCancelled())
                {
                    OvalDefinition od = wiz.getDefinition();

                    getDocument().add(od);
                    insertItemInTreeIfNecessary(od);
                    
                    selectDefinition(od);
                    setDirty(true);
                }
            }
        });

        return ret;
    }
    
   
    
    private JMenuItem createDeleteMenuItem(final Class<? extends OvalElement> elementClass) {
    //	log.debug("createDeleteMenuItem called with class " + elementClass.toString());
        JMenuItem ret = new JMenuItem("Remove");
        ret.addActionListener(new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
      //      	log.debug("actionPerformed entetered");
                Object selected = structTree.getLastSelectedPathComponent();

                if (!(selected instanceof DefaultMutableTreeNode)) {
                    return;
                }

                DefaultMutableTreeNode selectedOvalElementNode = (DefaultMutableTreeNode) selected;
                DefaultMutableTreeNode selectedCategoryNode = (DefaultMutableTreeNode) selectedOvalElementNode.getParent();
                DefaultMutableTreeNode typeNode = (DefaultMutableTreeNode) selectedCategoryNode.getParent();

                Object userObj = selectedOvalElementNode.getUserObject();
                if (userObj == null || !elementClass.isInstance(userObj)) {
        //        	log.debug("delete called for element of class " + elementClass.getName() + " but actual class is " + userObj.getClass().getName());
                    return;
                }

                OvalElement ovalElement = (OvalElement) userObj;
                List<OvalElement> dependencies = getDocument().findEntitiesDependentOn(ovalElement);

                if(dependencies != null && dependencies.size() > 0)
                {
                    // we can't delete, there are items that reference this one.  show the user
                    // what they are.
                    DependencyNotificationDialog dnd = new DependencyNotificationDialog(EditorMainWindow.getInstance(), true);
                    dnd.setDependencies(dependencies);
                    dnd.pack();
                    dnd.setLocationRelativeTo(EditorMainWindow.getInstance());
                    dnd.setVisible(true);
                    return;
                }

                DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

//                dtm.removeNodeFromParent(selectedDefinitionNode);

          //      log.debug("removing from document: " + ovalElement.getId());
                getDocument().remove(ovalElement);
            //    log.debug("updating parent counts");
                updateCountsAfterAddOrDelete(selectedOvalElementNode);
              //  log.debug("refreshing parent " + selectedCategoryNode.getUserObject().toString());
                dtm.reload(selectedCategoryNode);
                //log.debug("refreshing grandparent " + typeNode.getUserObject().toString());
                dtm.reload(typeNode);
                EditorUtil.markActiveWindowDirty(getParentWin(), true);
            }
        });
        //log.debug("returning " + ret.toString());
        return ret;
    }
    
  
    private JMenuItem createAddTestMenuItem()
    {
        JMenuItem ret = null;

        ret = new JMenuItem("Add a test");
        ret.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
          //      log.debug("createAddTestMenuItem Opening NewTestWizard");
                NewTestWizard wiz = new NewTestWizard(getParentWin(), true, getDocument());

                wiz.pack();
                wiz.setLocationRelativeTo(EditorMainWindow.getInstance());

                wiz.setVisible(true);
            //    log.debug("createAddTestMenuItem returned from NewTestWizard");
                if (!wiz.wasCancelled())
                {
                   OvalTest ovalTest = wiz.getOvalTest();
                   getDocument().add(ovalTest);
                   EditorUtil.markActiveWindowDirty(getParentWin(), true);
                   insertItemInTreeIfNecessary(ovalTest);
                   selectTest(ovalTest);
                }
              //  log.debug("createAddTestMenuItem - leaving actionPerformed");
            }
        });
        return ret;
    }
    
   
    private JMenuItem createAddObjectMenuItem()
    {
        JMenuItem ret = null;

        ret = new JMenuItem("Add an object");
        ret.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                NewObjectWizard wiz = new NewObjectWizard(EditorMainWindow.getInstance(), true, getDocument());

                wiz.pack();
                wiz.setVisible(true);

                if (!wiz.wasCancelled())
                {
                    OvalObject oo = wiz.getOvalObject();
                    getDocument().add(oo);

                    insertItemInTreeIfNecessary(oo);
                    
                    EditorUtil.markActiveWindowDirty(getParentWin(), true);
                    selectObject(oo);
                }
            }
        });
        return ret;
    }

       
    private JMenuItem createAddStateMenuItem()
    {
        JMenuItem ret = null;

        ret = new JMenuItem("Add a state");
        ret.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                NewStateWizard wiz = new NewStateWizard(getParentWin(), true, getDocument());

                wiz.pack();
                wiz.setVisible(true);

                if (!wiz.wasCancelled())
                {
                    OvalState os = wiz.getOvalState();

                    getDocument().add(os);
                    EditorUtil.markActiveWindowDirty(getParentWin(), true);
                    insertItemInTreeIfNecessary(os);
    
                    selectState(os);
                }
            }
        });

        return ret;
    }
    
       
    private JMenuItem createAddVariableMenuItem()
    {
        JMenuItem ret = null;

        ret = new JMenuItem("Add a variable");
        ret.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                NewVariableWizard wiz = new NewVariableWizard(getParentWin(), true, getDocument());

                wiz.pack();
                wiz.setVisible(true);

                if (!wiz.wasCancelled())
                {
                    OvalVariable ovalVar = wiz.getOvalVariable();
                    getDocument().add(ovalVar);
                    EditorUtil.markActiveWindowDirty(getParentWin(), true);
                    insertItemInTreeIfNecessary(ovalVar);
                    selectVariable(ovalVar);
                }
            }
        });
        return ret;
    }

       
    private JMenuItem createEditBaseIdMenuItem()
    {
        JMenuItem ret = new JMenuItem("Edit " + EditorMessages.OVAL + " Id Base");
        ret.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                EditorDialog editor = new EditorDialog(getParentWin(), true);
                BaseIdEditor editorPage = new BaseIdEditor();
                editorPage.setOvalDoc(getDocument());
                String idBase = getDocument().getBaseId();
           //     log.debug("Baseid before opening editor:" + idBase);
                editorPage.setBaseId(idBase);
                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);
                if (!editor.wasCancelled())
                {
                    String newIdBase = editorPage.getBaseId();
             //       log.debug("New Baseid after opening editor:" + newIdBase);
                    if (newIdBase != null && (idBase == null || !idBase.equals(newIdBase)))
                    {
                        getDocument().setBaseId(newIdBase);
                        EditorUtil.markActiveWindowDirty(getParentWin(), true);
                    }
                }
            }
        });
        return ret;
    }

    private JPopupMenu buildRootPopupMenu()
    {
        JPopupMenu ret = null;
        ret = new JPopupMenu();
        JMenuItem addDefMenuItem = createAddDefMenuItem();
        JMenuItem addTestMenuItem = createAddTestMenuItem();
        JMenuItem addObjectMenuItem = createAddObjectMenuItem();
        JMenuItem addStateMenuItem = createAddStateMenuItem();
        JMenuItem addVariableMenuItem = createAddVariableMenuItem();
        JMenuItem editBaseIdMenuItem = createEditBaseIdMenuItem();

        ret.add(addDefMenuItem);
        ret.add(addTestMenuItem);
        ret.add(addObjectMenuItem);
        ret.add(addStateMenuItem);
        ret.add(addVariableMenuItem);
        ret.add(editBaseIdMenuItem);

        return ret;
    }

    private JPopupMenu buildDefsPopupMenu()
    {
        JPopupMenu ret = null;
        ret = new JPopupMenu();
        JMenuItem addDefMenuItem = createAddDefMenuItem();
        ret.add(addDefMenuItem);

        return ret;
    }

    private JPopupMenu buildTestsPopupMenu()
    {
        JPopupMenu ret = null;
        ret = new JPopupMenu();
        JMenuItem addTestMenuItem = createAddTestMenuItem();
        ret.add(addTestMenuItem);
        return ret;
    }

    private JPopupMenu buildObjectsPopupMenu()
    {
        JPopupMenu ret = null;
        ret = new JPopupMenu();
        JMenuItem addObjectMenuItem = createAddObjectMenuItem();
        ret.add(addObjectMenuItem);
        return ret;
    }

    private JPopupMenu buildStatesPopupMenu()
    {
        JPopupMenu ret = null;
        ret = new JPopupMenu();
        JMenuItem addStateMenuItem = createAddStateMenuItem();
        ret.add(addStateMenuItem);
        return ret;
    }

    private JPopupMenu buildVariablesPopupMenu()
    {
        JPopupMenu ret = null;
        ret = new JPopupMenu();
        JMenuItem addVariableMenuItem = createAddVariableMenuItem();
        ret.add(addVariableMenuItem);
        return ret;
    }
    
    private JPopupMenu buildDeletePopupMenu(Class<? extends OvalElement> elementClass) {
   // 	log.debug("buildDeletePopupMenu called with class " + elementClass.toString());
    	JPopupMenu menu = new JPopupMenu();
    	JMenuItem deleteMenuItem = createDeleteMenuItem(elementClass);
    	menu.add(deleteMenuItem);
    //	log.debug("returning " + menu.toString());
    	return menu;
    }

//    private JPopupMenu buildDefinitionPopupMenu()
//    {
//        JPopupMenu ret = null;
//        ret = new JPopupMenu();
//        JMenuItem deleteMenuItem = createDeleteMenuItem(OvalDefinition.class);
//        ret.add(deleteMenuItem);
//
//        return ret;
//    }
//
//    private JPopupMenu buildTestPopupMenu()
//    {
//        JPopupMenu ret = null;
//        ret = new JPopupMenu();
//        JMenuItem deleteMenuItem = createDeleteTestMenuItem();
//        ret.add(deleteMenuItem);
//
//        return ret;
//    }
//
//    private JPopupMenu buildObjectPopupMenu()
//    {
//        JPopupMenu ret = null;
//        ret = new JPopupMenu();
//        JMenuItem deleteMenuItem = createDeleteObjectMenuItem();
//        ret.add(deleteMenuItem);
//
//        return ret;
//    }
//
//    private JPopupMenu buildStatePopupMenu()
//    {
//        JPopupMenu ret = null;
//        ret = new JPopupMenu();
//        JMenuItem deleteMenuItem = createDeleteStateMenuItem();
//        ret.add(deleteMenuItem);
//
//        return ret;
//    }
//
//    private JPopupMenu buildVariablePopupMenu()
//    {
//        JPopupMenu ret = null;
//        ret = new JPopupMenu();
//        JMenuItem deleteMenuItem = createDeleteVariableMenuItem();
//        ret.add(deleteMenuItem);
//
//        return ret;
//    }

    private void initTextFields()
    {
        final OvalEditorForm thisRef = this;

        searchField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent e)
            {
            	searchText = searchField.getText();
            	
                String val = searchField.getText();
      //          log.debug("searchText changed to " + (val == null ? "null" : "'" + val + "'"));

                DocumentTreeModel dtm = new DocumentTreeModel(getDocument(), val);
                dtm.addTreeModelListener(thisRef);

                structTree.setModel(dtm);

                EditorUtil.expandTree(structTree);
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
                if (userObj instanceof OvalDefinitionsDocument)
                {
                    OvalDefinitionsDocument dd = (OvalDefinitionsDocument) userObj;

                    // they selected the root node
                    DefinitionsDocumentDetailTab tab = new DefinitionsDocumentDetailTab();

                    tab.setDoc(dd);
                    detailTabPane.addTab(TAB_TITLE_DEFINITIONS_DOC, tab);
                }
                else if (userObj instanceof OvalDefinition)
                {
                    // they selected an oval defintion
                    OvalDefinition od = (OvalDefinition) userObj;

                    // define here so we can set a reference to it in other tabs.
                    // will be added below
                    DefinitionSourceDetailTab sourceTab = new DefinitionSourceDetailTab();

                    DefinitionGeneralDetailTab tab = new DefinitionGeneralDetailTab();
                    tab.setDoc(od, getDocument().getDocumentType());
                    tab.setParentEditorForm(this);
                    tab.setSourceTab(sourceTab);
                    tab.addChangeListener(this);
                    detailTabPane.addTab(TAB_TITLE_DEFINITION_GENERAL, tab);

                    DefinitionMetadataDetailTab metadataTab = new DefinitionMetadataDetailTab();
                    metadataTab.setSourceTab(sourceTab);
                    metadataTab.setDoc(od, getDocument().getDocumentType());
                    metadataTab.setParentEditorForm(this);
                    metadataTab.addChangeListener(this);
                    detailTabPane.addTab(TAB_TITLE_DEFINITION_METADATA, metadataTab);

                    DefinitionCriteriaDetailTab criteriaTab = new DefinitionCriteriaDetailTab();
                    criteriaTab.setEditorForm(this);
                    criteriaTab.setSourceTab(sourceTab);
                    criteriaTab.setParentEditorForm(this);
                    criteriaTab.addChangeListener(this);
                    criteriaTab.setDoc(od, getDocument().getDocumentType());
                    detailTabPane.addTab(TAB_TITLE_DEFINITION_CRITERIA, criteriaTab);

                    // add the source tab last
                    detailTabPane.addTab(TAB_TITLE_SOURCE, sourceTab);

                    // set a reference to the source tab so calls can be made to update it.
                    metadataTab.setSourceTab(sourceTab);
                    criteriaTab.setSourceTab(sourceTab);

                    sourceTab.setXMLSource(elementToString(od.getElement()));
                }
                else if (userObj instanceof OvalTest)
                {
                    OvalTest ot = (OvalTest) userObj;

                    // define here so we can set a reference to it in other tabs.
                    // will be added below
                    GenericSourceDetailTab sourceTab = new GenericSourceDetailTab();

                    TestDetailTab tab = new TestDetailTab();
                    tab.setParentEditorForm(this);
                    tab.addChangeListener(this);
                    tab.setSourceTab(sourceTab);
                    tab.setDoc(ot);
                    tab.addChangeListener(this);

                    detailTabPane.addTab(TAB_TITLE_TEST, tab);

                    // add the source tab last
                    detailTabPane.addTab(TAB_TITLE_SOURCE, sourceTab);

                    sourceTab.setXMLSource(elementToString(ot.getElement()));
                }
                else if (userObj instanceof OvalObject)
                {
                    OvalObject oo = (OvalObject) userObj;

                    // define here so we can set a reference to it in other tabs.
                    // will be added below
                    GenericSourceDetailTab sourceTab = new GenericSourceDetailTab();

                    ObjectDetailTab tab = new ObjectDetailTab();
                    tab.setSourceTab(sourceTab);
                    tab.setParentEditorForm(this);
                    tab.setDoc(oo);
                    tab.addChangeListener(this);

                    detailTabPane.addTab(TAB_TITLE_OBJECT, tab);

                    // add source tab last
                    detailTabPane.addTab(TAB_TITLE_SOURCE, sourceTab);

                    sourceTab.setXMLSource(elementToString(oo.getElement()));
                }
                else if (userObj instanceof OvalState)
                {
                    OvalState os = (OvalState) userObj;

                    // define here so we can set a reference to it in other tabs.
                    // will be added below
                    GenericSourceDetailTab sourceTab = new GenericSourceDetailTab();

                    StateDetailTab tab = new StateDetailTab();
                    tab.setSourceTab(sourceTab);
                    tab.setParentEditorForm(this);
                    tab.setDoc(os);
                    tab.addChangeListener(this);

                    detailTabPane.addTab(TAB_TITLE_STATE, tab);

                    // add source tab last
                    detailTabPane.addTab(TAB_TITLE_SOURCE, sourceTab);

                    sourceTab.setXMLSource(elementToString(os.getElement()));
                }
                else if (userObj instanceof OvalVariable)
                {
                    OvalVariable ov = (OvalVariable) userObj;
                    GenericSourceDetailTab sourceTab = new GenericSourceDetailTab();
                    VariableDetailTab tab = new VariableDetailTab();
                    tab.setSourceTab(sourceTab);
                    tab.setParentEditorForm(this);
                    tab.setDoc(ov);
                    tab.addChangeListener(this);
                    detailTabPane.addTab(TAB_TITLE_VARIABLE, tab);

                    // add source tab last
                    detailTabPane.addTab(TAB_TITLE_SOURCE, sourceTab);

                    sourceTab.setXMLSource(elementToString(ov.getElement()));
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

    private void initTree()
    {
        ToolTipManager.sharedInstance().registerComponent(structTree);
        structTree.setScrollsOnExpand(true);
        structTree.addTreeSelectionListener(this);
        structTree.addTreeWillExpandListener(this);
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

        //        log.debug("button = " + me.getButton());
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

                JPopupMenu rootPopupMenu = buildRootPopupMenu();
                JPopupMenu defsPopupMenu = buildDefsPopupMenu();
                JPopupMenu testsPopupMenu = buildTestsPopupMenu();
                JPopupMenu objectsPopupMenu = buildObjectsPopupMenu();
                JPopupMenu statesPopupMenu = buildStatesPopupMenu();
                JPopupMenu variablesPopupMenu = buildVariablesPopupMenu();

                if (node.isRoot())
                {
                    if (!(userObj instanceof OvalDefinitionsDocument))
                    {
                        return;
                    }

                    if (isRightClick)
                    {
                        rootPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                    }
                } else
                {
                    if (userObj instanceof String)
                    {
                        String selectedNodeStringValue = (String) userObj;
                        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();

                        if (parentNode.isRoot())
                        {
                            if (selectedNodeStringValue.startsWith(DocumentTreeModel.NODE_DEFINITIONS))
                            {
                                if (isRightClick)
                                {
                                    defsPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                                }
                            } else if (selectedNodeStringValue.startsWith(DocumentTreeModel.NODE_TESTS))
                            {
                                if (isRightClick)
                                {
                                    testsPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                                }
                            } else if (selectedNodeStringValue.startsWith(DocumentTreeModel.NODE_OBJECTS))
                            {
                                if (isRightClick)
                                {
                                    objectsPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                                }
                            } else if (selectedNodeStringValue.startsWith(DocumentTreeModel.NODE_STATES))
                            {
                                if (isRightClick)
                                {
                                    statesPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                                }
                            } else if (selectedNodeStringValue.startsWith(DocumentTreeModel.NODE_VARIABLES))
                            {
                                if (isRightClick)
                                {
                                    variablesPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                                }
                            }
                        } else
                        {
                            Object parentNodeUserObj = parentNode.getUserObject();
                            if (parentNodeUserObj instanceof String)
                            {
                                String parentNodeName = (String) parentNodeUserObj;

                                if (parentNodeName.startsWith(DocumentTreeModel.NODE_DEFINITIONS))
                                {
                                    if (isRightClick)
                                    {
                                        defsPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                                    }
                                } else if (parentNodeName.startsWith(DocumentTreeModel.NODE_TESTS))
                                {
                                    if (isRightClick)
                                    {
                                        testsPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                                    }
                                } else if (parentNodeName.startsWith(DocumentTreeModel.NODE_OBJECTS))
                                {
                                    if (isRightClick)
                                    {
                                        objectsPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                                    }
                                } else if (parentNodeName.startsWith(DocumentTreeModel.NODE_STATES))
                                {
                                    if (isRightClick)
                                    {
                                        statesPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                                    }
                                } else if (parentNodeName.startsWith(DocumentTreeModel.NODE_VARIABLES))
                                {
                                    if (isRightClick)
                                    {
                                        variablesPopupMenu.show(me.getComponent(), me.getX(), me.getY());
                                    }
                                }
                            }
                        }
                    } else if (userObj instanceof OvalElement) {
                    	if (isRightClick) {
                    		OvalElement ovalElement = (OvalElement) userObj;
                            JPopupMenu defPopup = buildDeletePopupMenu(ovalElement.getClass());
          //                  log.debug("built delete popup " + defPopup.toString());
                            defPopup.show(me.getComponent(), me.getX(), me.getY());
            //                log.debug("called show");
                    	}
                    }
//                    else if (userObj instanceof OvalDefinition)
//                    {
//                        if (isRightClick)
//                        {
//                            JPopupMenu defPopup = buildDefinitionPopupMenu();
//
//                            defPopup.show(me.getComponent(), me.getX(), me.getY());
//                        }
//                    }
//                    else if (userObj instanceof OvalTest)
//                    {
//                        if (isRightClick)
//                        {
//                            JPopupMenu testPopup = buildTestPopupMenu();
//
//                            testPopup.show(me.getComponent(), me.getX(), me.getY());
//                        }
//                    }
//                    else if (userObj instanceof OvalObject)
//                    {
//                        if (isRightClick)
//                        {
//                            JPopupMenu objPopup = buildObjectPopupMenu();
//
//                            objPopup.show(me.getComponent(), me.getX(), me.getY());
//                        }
//                    }
//                    else if (userObj instanceof OvalState)
//                    {
//                        if (isRightClick)
//                        {
//                            JPopupMenu statePopup = buildStatePopupMenu();
//
//                            statePopup.show(me.getComponent(), me.getX(), me.getY());
//                        }
//                    }
//                    else if (userObj instanceof OvalVariable)
//                    {
//                        if (isRightClick)
//                        {
//                            JPopupMenu variablePopup = buildVariablePopupMenu();
//
//                            variablePopup.show(me.getComponent(), me.getX(), me.getY());
//                        }
//                    }
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

    /** Creates new form OvalEditorForm */
    public OvalEditorForm()
    {
        initComponents();
        parentWin = new WeakReference(EditorMainWindow.getInstance());
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
        treePanel = new javax.swing.JPanel();
        structTreeScrollPane = new javax.swing.JScrollPane();
        structTree = new javax.swing.JTree();
        expandAllButton = new javax.swing.JButton();
        collapseAllButton = new javax.swing.JButton();
        tabPanel = new javax.swing.JPanel();
        detailTabPane = new javax.swing.JTabbedPane();
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jSeparator1, gridBagConstraints);

        docStyleSplitPane.setDividerLocation(400);
        docStyleSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        docStyleSplitPane.setLastDividerLocation(400);
        docStyleSplitPane.setMaximumSize(null);

        topPanel.setLayout(new java.awt.GridBagLayout());

        searchCaption.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 4, 0);
        topPanel.add(searchCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 0);
        topPanel.add(searchField, gridBagConstraints);

        stylePickerPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        stylePickerPanel.add(stylePicker, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        topPanel.add(stylePickerPanel, gridBagConstraints);

        treeTabsSplitPane.setDividerLocation(240);

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

        tabPanel.setLayout(new java.awt.GridBagLayout());

        detailTabPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        detailTabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        detailTabPane.setMaximumSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        tabPanel.add(detailTabPane, gridBagConstraints);

        treeTabsSplitPane.setRightComponent(tabPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        topPanel.add(treeTabsSplitPane, gridBagConstraints);

        docStyleSplitPane.setLeftComponent(topPanel);

        bottomPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
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
    public OvalDefinitionsDocument getDocument()
    {
        return (document == null ? null : document.get());
    }

    public void setDocument(OvalDefinitionsDocument document)
    {
    //	log.debug("setDocument entered for oval document");
        this.document = new WeakReference<OvalDefinitionsDocument>(document);
        
      //  log.debug("setDocument constructing DocumentTreeModel");
        DocumentTreeModel tm = new DocumentTreeModel(document, null);
       // log.debug("setDocument adding Listener to DocumentTreeModel");
        tm.addTreeModelListener(new WeakTreeModelListener(this));
        
       /// log.debug("setDocument setting CellRenderer");
        structTree.setCellRenderer(new OvalDocumentTreeCellRenderer());
      //  log.debug("setDocument setting TreeModel");
        structTree.setModel(tm);
    
       // log.debug("setDocument setting selection path");
        structTree.setSelectionPath(new TreePath(tm.getRoot()));

       // log.debug("setDocument calling refershStyleMessages");
        refreshStyleMessages();
       // log.debug("setDocument returning");
    }

    @Override
    public void treeNodesChanged(TreeModelEvent arg0)
    {
    }

    @Override
    public void treeNodesInserted(TreeModelEvent tme)
    {
//        treeNodeCountUpdate(tme.getTreePath());
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent tme)
    {
//        treeNodeCountUpdate(tme.getTreePath());
    }

    @Override
    public void treeStructureChanged(TreeModelEvent tme)
    {
//        treeNodeCountUpdate(tme.getTreePath());
    }

    public EditorMainWindow getParentWin()
    {
        return parentWin.get();
    }

    public void setParentWin(EditorMainWindow parentWin)
    {
        this.parentWin = new WeakReference(parentWin);
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
    
    private void refreshStyleMessages()
    {
        ContentStyle style = (ContentStyle) stylePicker.getChosenStyle();
        if (style != null) 
        {
	        currentStyle = style;	
	        OvalDefinitionsDocument ovalDoc = getDocument();
	        if (ovalDoc != null)
	        {
	        	ovalDoc.setContentStyle(currentStyle);
		        List<ContentStyleViolationElement> violations = currentStyle.checkDocument(ovalDoc);
		        contentStylePanel.setData(violations);
	        }
        }
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
    
    public void selectDefinition(OvalDefinition ovaldef) {
    	selectOvalElement(ovaldef);
    }
    
    public void selectTest(OvalTest ovaltest) {
    	selectOvalElement(ovaltest);
    }
    
    public void selectObject(OvalObject ovalobject) {
    	selectOvalElement(ovalobject);
    }
    
    public void selectState(OvalState ovalstate) {
    	selectOvalElement(ovalstate);
    }
    
    public void selectVariable(OvalVariable ovalvar) {
    	selectOvalElement(ovalvar);
    }
    
    private void selectOvalElement(OvalElement ovalElement) {
        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();

        if(searchField.getText().length() > 0) {
            // need to remove filtering to be sure we can select the oval tree element
            searchField.setText("");
            dtm = (DocumentTreeModel) structTree.getModel();
        }

        DefaultMutableTreeNode collectionNode = null;
        if (ovalElement instanceof OvalDefinition) {
        	collectionNode = dtm.getDefsNode();
        } else if (ovalElement instanceof OvalTest) {
        	collectionNode = dtm.getTestsNode();
        } else if (ovalElement instanceof OvalObject) {
        	collectionNode = dtm.getObjectsNode();
        } else if (ovalElement instanceof OvalState) {
        	collectionNode = dtm.getStatesNode();
        } else if (ovalElement instanceof OvalVariable) {
        	collectionNode = dtm.getVariablesNode();
        }
        
        Enumeration<?> children = collectionNode.depthFirstEnumeration();

        TreePath path = null;

        while (children.hasMoreElements()) {
            Object child = children.nextElement();

            if (child instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) child;

                if (cNode.getUserObject() != null) {
                    OvalElement currentElement = (OvalElement) cNode.getUserObject();
                    if (currentElement.getId().equals(ovalElement.getId())) {
                        path = EditorUtil.getTreePathToChild(cNode);
                        break;
                    }
                }
            }
        }

        if (path != null) {
            structTree.setSelectionPath(path);
            structTree.scrollPathToVisible(path);
        }
    }

//    public void selectDefinition(OvalDefinition ovaldef)
//    {
//        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();
//
//        if(searchField.getText().length() > 0)
//        {
//            // need to remove filtering to be sure we can select the defintion
//            searchField.setText("");
//
//            dtm = (DocumentTreeModel) structTree.getModel();
//        }
//
//        DefaultMutableTreeNode defsNode = dtm.getDefsNode();
//        Enumeration children = defsNode.depthFirstEnumeration();
//
//        TreePath path = null;
//
//        while (children.hasMoreElements())
//        {
//            Object child = children.nextElement();
//
//            if (child instanceof DefaultMutableTreeNode)
//            {
//                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) child;
//
//                if (cNode.getUserObject() != null)
//                {
//                    Object userObj = cNode.getUserObject();
//
//                    if (userObj instanceof OvalDefinition)
//                    {
//                        OvalDefinition od = (OvalDefinition) userObj;
//
//                        if (od.getId().equals(ovaldef.getId()))
//                        {
//                            path = EditorUtil.getTreePathToChild(cNode);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//        if (path != null)
//        {
//            structTree.setSelectionPath(path);
//            structTree.scrollPathToVisible(path);
//        }
//    }
//
//    public void selectTest(OvalTest ovalTest)
//    {
//        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();
//
//        if(searchField.getText().length() > 0)
//        {
//            // need to remove filtering to be sure we can select the defintion
//            searchField.setText("");
//
//            dtm = (DocumentTreeModel) structTree.getModel();
//        }
//
//        DefaultMutableTreeNode testsNode = dtm.getTestsNode();
//        Enumeration children = testsNode.depthFirstEnumeration();
//
//        TreePath path = null;
//
//        while (children.hasMoreElements())
//        {
//            Object child = children.nextElement();
//
//            if (child instanceof DefaultMutableTreeNode)
//            {
//                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) child;
//
//                if (cNode.getUserObject() != null)
//                {
//                    Object userObj = cNode.getUserObject();
//
//                    if (userObj instanceof OvalTest)
//                    {
//                        OvalTest ot = (OvalTest) userObj;
//
//                        if (ot.getId().equals(ovalTest.getId()))
//                        {
//                            path = EditorUtil.getTreePathToChild(cNode);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//        if (path != null)
//        {
//            structTree.setSelectionPath(path);
//            structTree.scrollPathToVisible(path);
//        }
//    }
//
//    public void selectObject(OvalObject ovalObject)
//    {
//        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();
//
//        if(searchField.getText().length() > 0)
//        {
//            // need to remove filtering to be sure we can select the defintion
//            searchField.setText("");
//
//            dtm = (DocumentTreeModel) structTree.getModel();
//        }
//
//        DefaultMutableTreeNode objectsNode = dtm.getObjectsNode();
//        Enumeration children = objectsNode.depthFirstEnumeration();
//
//        TreePath path = null;
//
//        while (children.hasMoreElements())
//        {
//            Object child = children.nextElement();
//
//            if (child instanceof DefaultMutableTreeNode)
//            {
//                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) child;
//
//                if (cNode.getUserObject() != null)
//                {
//                    Object userObj = cNode.getUserObject();
//
//                    if (userObj instanceof OvalObject)
//                    {
//                        OvalObject oo = (OvalObject) userObj;
//
//                        if (oo.getId().equals(ovalObject.getId()))
//                        {
//                            path = EditorUtil.getTreePathToChild(cNode);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//        if (path != null)
//        {
//            structTree.setSelectionPath(path);
//            structTree.scrollPathToVisible(path);
//        }
//    }
//
//    public void selectState(OvalState ovalState)
//    {
//        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();
//
//        if(searchField.getText().length() > 0)
//        {
//            // need to remove filtering to be sure we can select the defintion
//            searchField.setText("");
//
//            dtm = (DocumentTreeModel) structTree.getModel();
//        }
//
//        DefaultMutableTreeNode statesNode = dtm.getStatesNode();
//        Enumeration children = statesNode.depthFirstEnumeration();
//
//        TreePath path = null;
//
//        while (children.hasMoreElements())
//        {
//            Object child = children.nextElement();
//
//            if (child instanceof DefaultMutableTreeNode)
//            {
//                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) child;
//
//                if (cNode.getUserObject() != null)
//                {
//                    Object userObj = cNode.getUserObject();
//
//                    if (userObj instanceof OvalState)
//                    {
//                        OvalState os = (OvalState) userObj;
//
//                        if (os.getId().equals(ovalState.getId()))
//                        {
//                            path = EditorUtil.getTreePathToChild(cNode);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//        if (path != null)
//        {
//            structTree.setSelectionPath(path);
//            structTree.scrollPathToVisible(path);
//        }
//    }
//
//    public void selectVariable(OvalVariable ovalVariable)
//    {
//        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();
//
//        if(searchField.getText().length() > 0)
//        {
//            // need to remove filtering to be sure we can select the defintion
//            searchField.setText("");
//
//            dtm = (DocumentTreeModel) structTree.getModel();
////            dtm = new DocumentTreeModel(document, "");
////            structTree.setModel(dtm);
//        }
//
//        DefaultMutableTreeNode variablesNode = dtm.getVariablesNode();
//        Enumeration children = variablesNode.depthFirstEnumeration();
//
//        TreePath path = null;
//
//        while (children.hasMoreElements())
//        {
//            Object child = children.nextElement();
//
//            if (child instanceof DefaultMutableTreeNode)
//            {
//                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) child;
//
//                if (cNode.getUserObject() != null)
//                {
//                    Object userObj = cNode.getUserObject();
//
//                    if (userObj instanceof OvalVariable)
//                    {
//                        OvalVariable ov = (OvalVariable) userObj;
//
//                        if (ov.getId().equals(ovalVariable.getId()))
//                        {
//                            path = EditorUtil.getTreePathToChild(cNode);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//        if (path != null)
//        {
//            structTree.setSelectionPath(path);
//            structTree.scrollPathToVisible(path);
//        }
//    }

    // add a new OvalState/Test/etc to the JTree
    // this should be when a new one of these items
    // is added to the underlying document via something like a chooser
    public void insertItemInTreeIfNecessary(OvalElement oe)
    {
        if (oe == null) {
            return;
        }
      //  log.debug("insertItemInTreeIfNecessary with oval element " + oe.getId());
        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();
        DefaultMutableTreeNode newNode = null;
        DefaultMutableTreeNode parentNode = null;
        String subType = null;
        
        if (oe instanceof OvalDefinition) {
        	OvalDefinition od = (OvalDefinition) oe;
        	subType = od.getDefinitionClass().toString().toLowerCase();
        	parentNode = dtm.getDefsNode();
        } else {
        	subType = oe.getElementName().substring(0, oe.getElementName().indexOf("_"));
        	if (oe instanceof OvalTest) {
        		parentNode = dtm.getTestsNode();
        	} else if (oe instanceof OvalObject) {
        		parentNode = dtm.getObjectsNode();
        	} else if (oe instanceof OvalState) {
        		parentNode = dtm.getStatesNode();
        	} else if (oe instanceof OvalVariable) {
        		parentNode = dtm.getVariablesNode();
        	}
        }
        DefaultMutableTreeNode newNodeParent = EditorUtil.createSubTreeIfNecessary(dtm, parentNode, subType);
        newNode = new DefaultMutableTreeNode(oe);
        dtm.insertNodeInto(newNode, newNodeParent, 0);
       // log.debug("insertItemInTreeIfNecessary updating parent counts " + oe.getId());
        updateCountsAfterAddOrDelete(newNode);
        dtm.reload(parentNode);
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

    public void cleanUp()
    {
        OvalDefinitionsDocument ovalDoc = getDocument();
        if(ovalDoc != null)
        {
            ovalDoc.close();
        }

        treePanel.remove(structTree);
        changeListeners.clear();
        tabPanel.removeAll();
        treePanel.removeAll();
        ToolTipManager.sharedInstance().unregisterComponent(structTree);
        
        if(structTree.getModel() instanceof DocumentTreeModel)
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

    public void rebuildTree()
    {
        DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();
        dtm.cleanUp();

        DocumentTreeModel tm = new DocumentTreeModel(document.get(), null);
        tm.addTreeModelListener(new WeakTreeModelListener(this));

        structTree.setModel(tm);

        structTree.setSelectionPath(new TreePath(tm.getRoot()));
    }
    
    private void addChildLevel1DefTypeNodes(List<OvalDefinition> list, DefaultMutableTreeNode parent) {
    	String parentName = parent.getUserObject().toString();
    //	log.debug("addChildLevel1DefTypeNodes called for parent " + parentName);
    	// remove existing tree nodes from parent, we will replace them all
    	parent.removeAllChildren();
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	for (OvalDefinition ovalDef : list) {
    		String category = ovalDef.getDefinitionClass().toString().toLowerCase();
    		Integer count = map.get(category);
    		if (count == null) {
    			count = new Integer(0);
    		}
    		count++;
    		map.put(category, count);
    	}
		List<String> categoryList = new LinkedList<String>(map.keySet());
		Collections.sort(categoryList);
		for (String category : categoryList) {
			String categoryName = category + "(" + map.get(category).toString() + ")";
			DefaultMutableTreeNode categoryNode = new SCAPParentTreeNode(categoryName, true);
			parent.add(categoryNode);
		}
    }
  
    
    private void addChildLevel1Nodes(List<? extends OvalElement> list, String suffix, DefaultMutableTreeNode parent) {
    	String parentName = parent.getUserObject().toString();
    //	log.debug("addChildLevel1Nodes called with suffix " + suffix + ", parent " + parentName);
    	// remove existing tree nodes from parent, we will replace them all
    	parent.removeAllChildren();
    	
    	// find number of OvalElements in each category
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	for (OvalElement ovalElement : list) {
    		String elementName = ovalElement.getElementName();
    		String category = elementName.substring(0, elementName.indexOf(suffix));
    		Integer count = map.get(category);
    		if (count == null) {
    			count = new Integer(0);
    		}
    		count++;
    		map.put(category, count);
    	}
		List<String> categoryList = new LinkedList<String>(map.keySet());
		Collections.sort(categoryList);
		int totalCount = 0;
		for (String category : categoryList) {
			totalCount += map.get(category).intValue();
			String categoryName = category + "(" + map.get(category).toString() + ")";
			DefaultMutableTreeNode categoryNode = new SCAPParentTreeNode(categoryName, true);
			parent.add(categoryNode);
		}
		
		String categoryName = parentName.substring(0, parentName.lastIndexOf("("));
		String categoryWithCount = categoryName + "(" + totalCount + ")";
	//	log.debug("New parent userObject: " + categoryWithCount);
		parent.setUserObject(categoryWithCount);
		DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();
		dtm.reload(parent);
		
    }
    
//    private DefaultMutableTreeNode getCategoryTreeNode(String category, DefaultMutableTreeNode parent) {
//    	DefaultMutableTreeNode categoryNode = null;
//    	String categoryWithParen = category + "(";
//    	int childCount = parent.getChildCount();
//    	for (int i=0; i<childCount; i++) {
//    		DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
//    		Object childObject = child.getUserObject();
//    		if (childObject instanceof String) {
//    			String childString = (String) childObject;
//    			if (childString.startsWith(categoryWithParen)) {
//    				categoryNode = child;
//    				break;
//    			}
//    		}
//    	}
//    	return categoryNode;
//    }
    
    private void addChildLevel2DefTypeNodes(List<OvalDefinition> list, String category, DefaultMutableTreeNode parent) {
    //	log.debug("addChildLevel2DefTypeNodes called with category " + category + ", parent " + parent.getUserObject().toString());
    	List<OvalDefinition> categoryMatchList = new LinkedList<OvalDefinition>();
    	parent.removeAllChildren();
    	for (OvalDefinition ovalDef : list) {
    		String defCategory = ovalDef.getDefinitionClass().toString().toLowerCase();
    		if (!defCategory.equals(category)) {
    			continue;
    		}
    		categoryMatchList.add(ovalDef);
    	}
    	Collections.sort(categoryMatchList, new OvalComparator<OvalDefinition>());
		for (OvalElement ovalElem : categoryMatchList) {
			DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(ovalElem);
			parent.add(categoryNode);
		}
    }
    
    private void addChildLevel2Nodes(List<? extends OvalElement> list, String category, DefaultMutableTreeNode parent) {
    //	log.debug("addChildLevel1Nodes called with category " + category + ", parent " + parent.getUserObject().toString());
    	List<OvalElement> categoryMatchList = new LinkedList<OvalElement>();
    	parent.removeAllChildren();
    	for (OvalElement ovalObject : list) {
    		String elementName = ovalObject.getElementName();
    		if (!elementName.equals(category)) {
    			continue;
    		}
    		categoryMatchList.add(ovalObject);
    	}
    	Collections.sort(categoryMatchList, new OvalComparator<OvalElement>());
		for (OvalElement ovalElem : categoryMatchList) {
			DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(ovalElem);
			parent.add(categoryNode);
		}
		String categoryName = category.substring(0, category.lastIndexOf("_"));
		String categoryWithCount = categoryName + "(" + categoryMatchList.size() + ")";
		parent.setUserObject(categoryWithCount);
		DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();
		dtm.reload(parent);
    }
    
    private void updateCountsAfterAddOrDelete(DefaultMutableTreeNode elementNode) {
    	Object userObj = elementNode.getUserObject();
    	if (!(userObj instanceof OvalElement)) {
    		throw new IllegalArgumentException("updateCountsAfterAddOrDelete called for unexpected object type: " + userObj.getClass());
    	}

    	DefaultMutableTreeNode level2Node = (DefaultMutableTreeNode) elementNode.getParent();
    	DefaultMutableTreeNode level1Node = (DefaultMutableTreeNode) level2Node.getParent();
    	
    	String level1Type = (String) level1Node.getUserObject();
		level1Type = level1Type.substring(0, level1Type.indexOf("(")); // "Definitions", "Tests", etc
		
    	
    	String level2Type = (String) level2Node.getUserObject();
		level2Type = level2Type.substring(0, level2Type.indexOf("(")); // "file", "registry", "compliance", etc
    	
		int level1Count = 0;
		int level2Count = 0;
		OvalDefinitionsDocument ovalDoc = document.get();
		List<? extends OvalElement> list = null;
		if (level1Type.startsWith("Definitions")) {
			List<OvalDefinition> ovalDefs = ovalDoc.getMatchingOvalDefinitions(searchText);
			list = ovalDoc.getMatchingOvalDefinitions(searchText);
			level1Count = list.size();
			for (OvalDefinition ovalDef : ovalDefs) {
				if (level2Type.equals(ovalDef.getDefinitionClass().toString().toLowerCase())) {
					level2Count++;
				}
			}
		} else {
			String typeString = level2Type + "_";
			if (level1Type.startsWith("Tests")) {
				list = ovalDoc.getMatchingOvalTests(searchText);
			} else if (level1Type.startsWith("Objects")) {
				list = ovalDoc.getMatchingOvalObjects(searchText);
			} else if (level1Type.startsWith("States")) {
				list = ovalDoc.getMatchingOvalStates(searchText);
			} else if (level1Type.startsWith("Variables")) {
				list = ovalDoc.getMatchingOvalVariables(searchText);
			}
			level1Count = list.size();
			for (OvalElement currOvalElement : list) {
				if (currOvalElement.getElementName().startsWith(typeString)) {
					level2Count++;
				}
			}
		}
		
		level1Node.setUserObject(level1Type + "(" + level1Count + ")");
		level2Node.setUserObject(level2Type + "(" + level2Count + ")");
    	
    }
    
    
    /**
     * Listen for tree expansion events to build tree nodes just in time.  Before this change,
     * every possible node in the tree was built when the tree is first displayed, causing a 
     * long delay when opening a very large oval document.
     */
	@Override
	public void treeWillExpand(TreeExpansionEvent event)
			throws ExpandVetoException {
		TreePath path = event.getPath();
	//	log.debug("treeWillExpand entry with path " + path.toString());
		if (path.getPathCount() < 2 || path.getPathCount() > 3) {
			return;
		}
		DocumentTreeModel dtm = (DocumentTreeModel) structTree.getModel();
		OvalDefinitionsDocument ovalDoc = document.get();
		Object[] pathComponents = path.getPath();
		DefaultMutableTreeNode level1Node = (DefaultMutableTreeNode) pathComponents[1];
		String level1Type = (String) level1Node.getUserObject();
		level1Type = level1Type.substring(0, level1Type.indexOf("("));
		if (path.getPathCount() == 2) {
			// selected node is "Definitions", "Tests", "Objects", "States", or "Variables"	
			if (level1Type.startsWith("Definitions")) {
				addChildLevel1DefTypeNodes(ovalDoc.getMatchingOvalDefinitions(searchText), level1Node);
			} else if (level1Type.startsWith("Tests")) {
				addChildLevel1Nodes(ovalDoc.getMatchingOvalTests(searchText), "_test", level1Node);
			} else if (level1Type.startsWith("Objects")) {
				addChildLevel1Nodes(ovalDoc.getMatchingOvalObjects(searchText), "_object", level1Node);
			} else if (level1Type.startsWith("States")) {
				addChildLevel1Nodes(ovalDoc.getMatchingOvalStates(searchText), "_state", level1Node);
			} else if (level1Type.startsWith("Variables")) {
				addChildLevel1Nodes(ovalDoc.getMatchingOvalVariables(searchText), "_variable", level1Node);
			}
		} else if (path.getPathCount() == 3) {
			DefaultMutableTreeNode level2Node = (DefaultMutableTreeNode) pathComponents[2];
			String level2Type = (String) level2Node.getUserObject();
			level2Type = level2Type.substring(0, level2Type.indexOf("("));
			// selected node is child of "Definitions", "Tests", "Objects", "States", or "Variables".
			// such as "inventory" or "compliance" or "fileeffectiverights"
			if (level1Type.startsWith("Definitions")) {
				addChildLevel2DefTypeNodes(ovalDoc.getMatchingOvalDefinitions(searchText), level2Type, level2Node);
			} else if (level1Type.startsWith("Tests")) {
				addChildLevel2Nodes(ovalDoc.getMatchingOvalTests(searchText), level2Type + "_test", level2Node);
			} else if (level1Type.startsWith("Objects")) {
				addChildLevel2Nodes(ovalDoc.getMatchingOvalObjects(searchText), level2Type  + "_object", level2Node);
			} else if (level1Type.startsWith("States")) {
				addChildLevel2Nodes(ovalDoc.getMatchingOvalStates(searchText), level2Type  + "_state", level2Node);
			} else if (level1Type.startsWith("Variables")) {
				addChildLevel2Nodes(ovalDoc.getMatchingOvalVariables(searchText), level2Type + "_variable", level2Node);
			}
		}
//		ovalDoc.showTiming();
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event)
			throws ExpandVetoException {	
	}
	
	
}
