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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.cellrenderers.tree.oval.object.ObjectTreeCellRenderer;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField;
import com.g2inc.scap.editor.gui.model.tree.oval.wizard.variable.VariableContentTreeModel;
import com.g2inc.scap.editor.gui.windows.EditorForm;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.NavigationButton;
import com.g2inc.scap.editor.gui.windows.common.TabAbstract;
import com.g2inc.scap.editor.gui.windows.oval.OvalEditorForm;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalVariable;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentObject;

public class VariableDisplayPanel extends TabAbstract
{
    private static Logger log = Logger.getLogger(VariableDisplayPanel.class);
    private VariableTreeHandler treeHandler = null;
    private boolean modalParent = false;
    private EditorForm parentEditor = null;
    protected OvalVariable ovalVar = null;
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    public VariableDisplayPanel() {
        initComponents();
        initTree();
    }

    public void addChangeListener(ChangeListener cl)
    {
        if(!changeListeners.contains(cl))
        {
            changeListeners.add(cl);
            generalPanel.addChangeListener(cl);
        }
    }

    public void removeChangeListener(ChangeListener cl)
    {
        if(changeListeners.contains(cl))
        {
            changeListeners.remove(cl);
            generalPanel.removeChangeListener(cl);
        }
    }

    public JTree getVarTree() {
        return varTree;
    }
    
    public OvalVariable getOvalVariable() {
        return ovalVar;
    }

    public VariableTreeHandler getTreeHandler() {
        return treeHandler;
    }

    public VariableContentTreeModel getTreeModel()
    {
        return (VariableContentTreeModel) varTree.getModel();
    }
    
    public void setOvalVariable(OvalVariable ovalVar)
    {
        this.ovalVar = ovalVar;
        VariableContentTreeModel model = new VariableContentTreeModel(ovalVar.getChildren());
        varTree.setModel(model);
        generalPanel.setOvalVariable(ovalVar);
        String varType = ovalVar.getElementName();
        if (varType.equals("local_variable")) {
            treeHandler = new LocalVariableTreeHandler();
        } else if (varType.equals("external_variable")) {
            treeHandler = new ExternalVariableTreeHandler();
        } else if (varType.equals("constant_variable")) {
            treeHandler = new ConstantVariableTreeHandler();
        } else {
            throw new IllegalStateException("Unexpected variable type " + varType);
        }
        treeHandler.setVarTree(varTree);
        treeHandler.setOvalVariable(ovalVar);
    }

    public void setParentEditor(EditorForm parentEditor)
    {
        this.parentEditor = parentEditor;
    }

    public void setModalParent(boolean b)
    {
        modalParent = b;
    }

    public Object getVersion()
    {
        return generalPanel.getVersion();
    }

    public JLabel getIDCaption()
    {
        return generalPanel.getIDCaption();
    }

    public PatternedStringField getVariableIDField()
    {
        return generalPanel.getVariableIDField();
    }

    public boolean allDataOk()
    {
        boolean ret = true;

        // TODO:  add some validity checking

        return ret;
    }

    public void initTree()
    {
        ToolTipManager.sharedInstance().registerComponent(varTree);
        varTree.setCellRenderer(new ObjectTreeCellRenderer());
        initTreeListeners();
    }

    private void initTreeListeners()
    {
        varTree.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent me)
            {
                boolean isDoubleClick = false;
                boolean isRightClick = false;

                if(me.getClickCount() > 1)
                {
                    isDoubleClick = true;
                }

                if(me.getButton() != 1)
                {
                    isRightClick = true;
                }

                TreePath path = varTree.getPathForLocation(me.getX(), me.getY());

                if(path == null)
                {
                    return;
                }

                varTree.setSelectionPath(path);

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) varTree.getLastSelectedPathComponent();
                Object userObj = selectedNode.getUserObject();
                if(isRightClick)
                {
                    JPopupMenu popup = treeHandler.buildPopupMenu(selectedNode);
                    popup.show(me.getComponent(), me.getX(), me.getY());
                }
                else if (isDoubleClick)
                {
                    if(parentEditor == null || modalParent)
                    {
                        return;
                    }
                    if (userObj instanceof OvalVariableComponentObject)
                    {
                        OvalVariableComponentObject objComp = (OvalVariableComponentObject) userObj;
                        OvalObject ovalObject = objComp.getObject();
                        if(ovalObject != null)
                        {
                            NavigationButton nb = new NavigationButton();
                            nb.setText("OvalVariable");
                            nb.setToolTipText(ovalVar.getId());
                            nb.setSelectedElement(parentEditor.getSelectedPath());
                            nb.setEditorForm(parentEditor);
                    //        EditorMainWindow.getInstance().getNavPanel().addNavigationButton(nb);
                            // now set selection to the item the user intended
                            ((OvalEditorForm)parentEditor).selectObject(ovalObject);
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        generalPanel = new com.g2inc.scap.editor.gui.windows.oval.variable.VariableGeneralPanel();
        treeScroller = new javax.swing.JScrollPane();
        varTree = new javax.swing.JTree();

        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(generalPanel, gridBagConstraints);

        varTree.setMaximumSize(null);
        varTree.setMinimumSize(null);
        varTree.setPreferredSize(null);
        varTree.setRequestFocusEnabled(false);
        treeScroller.setViewportView(varTree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        add(treeScroller, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.g2inc.scap.editor.gui.windows.oval.variable.VariableGeneralPanel generalPanel;
    private javax.swing.JScrollPane treeScroller;
    private javax.swing.JTree varTree;
    // End of variables declaration//GEN-END:variables

}
