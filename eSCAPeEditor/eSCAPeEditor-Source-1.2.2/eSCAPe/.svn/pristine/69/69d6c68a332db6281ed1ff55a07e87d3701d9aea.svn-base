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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.model.tree.oval.wizard.variable.VariableContentTreeModel;
import com.g2inc.scap.editor.gui.windows.EditorForm;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.oval.OvalVariable;
import com.g2inc.scap.library.domain.oval.OvalVariableChild;
import com.g2inc.scap.library.domain.oval.OvalVariableContent;

public abstract class VariableTreeHandler {
    protected static Logger log = Logger.getLogger(VariableTreeHandler.class);
    protected OvalVariable ovalVar = null;
    protected JTree varTree;
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    protected EditorForm parentEditor = null;
    
    public abstract JPopupMenu buildPopupMenu(DefaultMutableTreeNode selectedNode);

    public void setVarTree(JTree varTree)
    {
        this.varTree = varTree;
    }

    public void addChangeListener(ChangeListener cl)
    {
        if(!changeListeners.contains(cl))
        {
            changeListeners.add(cl);
        }
    }

    public void removeChangeListener(ChangeListener cl)
    {
        if(changeListeners.contains(cl))
        {
            changeListeners.remove(cl);
        }
    }

    protected void notifyRegisteredListeners()
    {
        for(int x = 0; x < changeListeners.size(); x++)
        {
            ChangeListener cl = changeListeners.get(x);
            ChangeEvent ce = new ChangeEvent(this);
            cl.stateChanged(ce);
        }
    }

    public void setOvalVariable(OvalVariable ovalVariable) {
        this.ovalVar = ovalVariable;
    }
    protected void showEditorPage(IEditorPage editorPage, OvalVariableChild varChild) {
        EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
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
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(varChild);
            VariableContentTreeModel model = (VariableContentTreeModel) varTree.getModel();
            selectedNode.add(childNode);
            OvalVariableContent selected = (OvalVariableContent) selectedNode.getUserObject();
            selected.addChild(varChild);
//            Element selectedElement = ((OvalElement) selectedNode.getUserObject()).getElement();
//            selectedElement.addContent(varChild.getElement());
            model.reload(selectedNode);
            notifyRegisteredListeners();
        }
    }
    
    public void setParentEditor(EditorForm parentEditor)
    {
        this.parentEditor = parentEditor;
    }

}
