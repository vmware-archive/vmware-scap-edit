package com.g2inc.scap.editor.gui.windows.oval.variable;
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
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdom.Element;

import com.g2inc.scap.editor.gui.choosers.Chooser;
import com.g2inc.scap.editor.gui.choosers.variable.VariableChooser;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.StringDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.FunctionEditorArithmetic;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.FunctionEditorBegin;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.FunctionEditorConcat;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.FunctionEditorCount;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.FunctionEditorEnd;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.FunctionEditorEscapeRegex;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.FunctionEditorRegexCapture;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.FunctionEditorSplit;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.FunctionEditorSubstring;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.FunctionEditorTimeDifference;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.FunctionEditorUnique;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.LiteralComponentEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.ObjectComponentEditor;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.VariableComponentEditor;
import com.g2inc.scap.editor.gui.model.tree.oval.wizard.variable.VariableContentTreeModel;
import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalElement;
import com.g2inc.scap.library.domain.oval.OvalFunction;
import com.g2inc.scap.library.domain.oval.OvalFunctionEnum;
import com.g2inc.scap.library.domain.oval.OvalVariable;
import com.g2inc.scap.library.domain.oval.OvalVariableChild;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentLiteral;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentObject;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentVariable;
import com.g2inc.scap.library.domain.oval.OvalVariableContent;

public class LocalVariableTreeHandler extends VariableTreeHandler {

    // build the menu the pops up when you right click on the root of the tree
    
    @Override
	public JPopupMenu buildPopupMenu(DefaultMutableTreeNode selectedNode)
    {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem addObjRefMenuItem = buildObjRefAddMenuItem();
        JMenuItem addVarRefMenuItem = buildVarRefAddMenuItem();
        JMenuItem addLiteralMenuItem = buildLiteralAddMenuItem();
        JMenu     addFunctionMenuItem = buildAddFunctionSubmenu();
        JMenuItem editMenuItem = buildEditMenuItem();
        JMenuItem removeMenuItem = buildRemoveMenuItem();

        menu.add(addObjRefMenuItem);
        menu.add(addVarRefMenuItem);
        menu.add(addLiteralMenuItem);
        menu.add(addFunctionMenuItem);
        menu.add(editMenuItem);
        menu.add(removeMenuItem);

        if (selectedNode.isRoot()) {
            removeMenuItem.setEnabled(false);
            editMenuItem.setEnabled(false);
        } else {
            OvalElement selectedElement = (OvalElement) selectedNode.getUserObject();
            if (selectedElement instanceof OvalVariableComponentObject
                    || selectedElement instanceof OvalVariableComponentVariable
                    || selectedElement instanceof OvalVariableComponentLiteral) {
                // if selected is a leaf node (can't have any children), disable add menu items
                addObjRefMenuItem.setEnabled(false);
                addVarRefMenuItem.setEnabled(false);
                addLiteralMenuItem.setEnabled(false);
                addFunctionMenuItem.setEnabled(false);
            } else if (selectedElement instanceof OvalFunction) {
                // if selected is a function node it can have children, up to max component groups
                OvalFunction ovalFunction = (OvalFunction) selectedElement;
                List<Element> children = selectedElement.getElement().getChildren();
                if (children.size() >= ovalFunction.getMaxComponentGroups()) {
                    // if function already has as many children as it can, disable add menu items
                    addObjRefMenuItem.setEnabled(false);
                    addVarRefMenuItem.setEnabled(false);
                    addLiteralMenuItem.setEnabled(false);
                    addFunctionMenuItem.setEnabled(false);
                }
            }
        }
        return menu;
    }

    private JMenuItem buildObjRefAddMenuItem()
    {
        JMenuItem ret = new JMenuItem("Add Object Ref");

        ret.addActionListener(new ActionListener()
        {
            
            public void actionPerformed(ActionEvent ae)
            {
                OvalVariableComponentObject objComp = ovalVar.createObjectComponent();

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                ObjectComponentEditor editorPage = new ObjectComponentEditor();
                editorPage.setData(objComp);
                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) varTree.getLastSelectedPathComponent();
                    if(selectedNode == null)
                    {
                        return;
                    }
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(objComp);
                    VariableContentTreeModel model = (VariableContentTreeModel) varTree.getModel();
                    selectedNode.add(childNode);
                    OvalVariableContent selected = (OvalVariableContent) selectedNode.getUserObject();
                    selected.addChild(objComp);
//                    Element selectedElement = ((OvalElement) selectedNode.getUserObject()).getElement();
//                    selectedElement.addContent(objComp.getElement());
                    model.reload(selectedNode);
                    notifyRegisteredListeners();
                }
            }
        });

        return ret;
    }

    private JMenuItem buildVarRefAddMenuItem()
    {
        JMenuItem ret = new JMenuItem("Add Variable Ref");

        ret.addActionListener(new ActionListener()
        {
            
            public void actionPerformed(ActionEvent ae)
            {
                OvalVariableComponentVariable varComp = ovalVar.createVariableComponent();
                // launch a variable chooser
                Chooser c = new VariableChooser(true);
                c.setSource(ovalVar.getParentDocument(), null, null);
                c.setLocationRelativeTo(EditorMainWindow.getInstance());
                c.setVisible(true);

                if(!c.wasCancelled())
                {
                    OvalVariable chosen = (OvalVariable) c.getChosen();
                    varComp.setVariableId(chosen.getId());

                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) varTree.getLastSelectedPathComponent();
                    if(selectedNode == null)
                    {
                        return;
                    }
                    DefaultMutableTreeNode refChild = new DefaultMutableTreeNode(varComp);
                    VariableContentTreeModel model = (VariableContentTreeModel) varTree.getModel();

                    // add in the proper location
                    selectedNode.add(refChild);
                    OvalVariableContent selected = (OvalVariableContent) selectedNode.getUserObject();
                    selected.addChild(varComp);
//                    Element selectedElement = ((OvalElement) selectedNode.getUserObject()).getElement();
//                    selectedElement.addContent(varComp.getElement());
                    model.reload(selectedNode);

                    notifyRegisteredListeners();
                }
            }
        });
        return ret;
    }

    private JMenuItem buildLiteralAddMenuItem()
    {
        JMenuItem ret = new JMenuItem("Add Literal");
        ret.addActionListener(new ActionListener()
        {
            
            public void actionPerformed(ActionEvent arg0)
            {
                OvalVariableComponentLiteral literalComp = ovalVar.createLiteralComponent();
                String literal = null;

                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                StringDatatypeEditor editorPage = new StringDatatypeEditor();
                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    literal = editorPage.getData();

	                literalComp.setValue(literal);
	                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) varTree.getLastSelectedPathComponent();
	                if(selectedNode == null)
	                {
	                    return;
	                }
	                DefaultMutableTreeNode refChild = new DefaultMutableTreeNode(literalComp);
	                VariableContentTreeModel model = (VariableContentTreeModel) varTree.getModel();
	
	                // add in the proper location
	                selectedNode.add(refChild);
	                OvalVariableContent selected = (OvalVariableContent) selectedNode.getUserObject();
	                selected.addChild(literalComp);
	//                Element selectedElement = ((OvalElement) selectedNode.getUserObject()).getElement();
	//                selectedElement.addContent(literalComp.getElement());
	                model.reload(selectedNode);
	
	                notifyRegisteredListeners();
                }
            }
        });

        return ret;
    }

    private JMenuItem buildRemoveMenuItem()
    {
        JMenuItem ret = new JMenuItem("Remove");

        ret.addActionListener(new ActionListener()
        {
            
            public void actionPerformed(ActionEvent ae)
            {
                DefaultMutableTreeNode selectedNode
                        = (DefaultMutableTreeNode) varTree.getLastSelectedPathComponent();

                VariableContentTreeModel model  = (VariableContentTreeModel) varTree.getModel();

                DefaultMutableTreeNode pNode = (DefaultMutableTreeNode) selectedNode.getParent();
                ((OvalElement) selectedNode.getUserObject()).getElement().detach();
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
            
            public void actionPerformed(ActionEvent arg0)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) varTree.getLastSelectedPathComponent();
                if(selectedNode == null)
                {
                   return;
                }

                OvalVariableChild varChild = (OvalVariableChild) selectedNode.getUserObject();
                IEditorPage editorPage = null;
                if (varChild instanceof OvalVariableComponentVariable)
                {
                    editorPage = new VariableComponentEditor();
                }
                else if (varChild instanceof OvalVariableComponentObject)
                {
                    editorPage = new ObjectComponentEditor();
                }
                else if (varChild instanceof OvalVariableComponentLiteral)
                {
                    editorPage = new LiteralComponentEditor();
                }
                else if (varChild instanceof OvalFunction)
                {
                    OvalFunction function = (OvalFunction) varChild;
                    editorPage = getFunctionEditor(function.getType());
                }
                editorPage.setData(varChild);
                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                editor.setEditorPage(editorPage);
                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.setVisible(true);

                if(!editor.wasCancelled())
                {
                    VariableContentTreeModel model = (VariableContentTreeModel) varTree.getModel();
                    model.reload(selectedNode);
                    notifyRegisteredListeners();
                }
            }
        });
        return ret;
    }

    private JMenu buildAddFunctionSubmenu() {
        JMenu menu = new JMenu("Add Function");
        log.debug("Add Function Submenu");
        menu.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent arg0)
            {
                log.debug("Add Function action listener called");
                log.debug("In actionListener - ActionEvent source is of type " + arg0.getSource().getClass().getName());
                JMenu menu = (JMenu) arg0.getSource();
                log.debug("In actionListener -isPopupMenuVisible=" + menu.isPopupMenuVisible());
            }
        });
        OvalDefinitionsDocument ovalDoc = ovalVar.getParentDocument();
        OvalFunctionEnum[] validFunctions = ovalDoc.getValidFunctions();
        for (int i=0; i<validFunctions.length; i++) {
            String functionName = validFunctions[i].toString();
            JMenuItem functionItem = new JMenuItem(functionName);
            functionItem.addActionListener(new ActionListener() {
                
                public void actionPerformed(ActionEvent arg0) {
                    JMenuItem item = (JMenuItem) arg0.getSource();
                    String functionType = item.getText();
                    OvalFunctionEnum functionEnum = OvalFunctionEnum.valueOf(functionType);
                    OvalFunction ovalFunction = ovalVar.createOvalFunction(functionEnum);
                    EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                    IEditorPage functionEditor = getFunctionEditor(functionEnum);
                    functionEditor.setData(ovalFunction);
                    editor.setEditorPage(functionEditor);
                    editor.pack();
                    editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                    editor.setVisible(true);

                    if(!editor.wasCancelled())
                    {
                        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) varTree.getLastSelectedPathComponent();
                        if(selectedNode == null)
                        {
                            return;
                        }
                        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(ovalFunction);
                        VariableContentTreeModel model = (VariableContentTreeModel) varTree.getModel();
                        selectedNode.add(childNode);
                        OvalVariableContent selected = (OvalVariableContent) selectedNode.getUserObject();
                        selected.addChild(ovalFunction);
//                        Element selectedElement = ((OvalElement) selectedNode.getUserObject()).getElement();
//                        selectedElement.addContent(ovalFunction.getElement());
                        model.reload(selectedNode);

                        notifyRegisteredListeners();
                    }
                }

            });
            menu.add(functionItem);
        }
        return menu;
    }

    private IEditorPage getFunctionEditor(OvalFunctionEnum functionEnum) {
        IEditorPage functionEditor = null;
//        OvalFunctionEnum functionEnum = ovalFunction.getType();
        switch (functionEnum) {
        case arithmetic:
            functionEditor = new FunctionEditorArithmetic();
            break;
        case begin:
            functionEditor = new FunctionEditorBegin();
            break;
        case concat:
            functionEditor = new FunctionEditorConcat();
            break;
        case end:
            functionEditor = new FunctionEditorEnd();
            break;
        case escape_regex:
            functionEditor = new FunctionEditorEscapeRegex();
            break;
        case regex_capture:
            functionEditor = new FunctionEditorRegexCapture();
            break;
        case split:
            functionEditor = new FunctionEditorSplit();
            break;
        case substring:
            functionEditor = new FunctionEditorSubstring();
            break;
        case time_difference:
            functionEditor = new FunctionEditorTimeDifference();
            break;
        case unique:
            functionEditor = new FunctionEditorUnique();
            break;
        case count:
            functionEditor = new FunctionEditorCount();
            break;
        default:
            throw new IllegalStateException("Unsupported " + EditorMessages.OVAL + " Function type:" + functionEnum);
        }
        return functionEditor;
    }



}
