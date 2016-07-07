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

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.variable.ConstantVariableValueEditor;
import com.g2inc.scap.editor.gui.model.tree.oval.wizard.variable.VariableContentTreeModel;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.oval.ConstantVariableValue;
import com.g2inc.scap.library.domain.oval.OvalElement;
import com.g2inc.scap.library.domain.oval.OvalVariableChild;

public class ConstantVariableTreeHandler extends VariableTreeHandler {

    private JMenuItem buildValueAddMenuItem()
    {
        JMenuItem ret = new JMenuItem("Add Value");
        ret.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                ConstantVariableValue possibleValue = ovalVar.createConstantVariableValue();
                ConstantVariableValueEditor editorPage = new ConstantVariableValueEditor(possibleValue);
                showEditorPage(editorPage, possibleValue);
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
                if (varChild instanceof ConstantVariableValue)
                {
                    editorPage = new ConstantVariableValueEditor((ConstantVariableValue) varChild);
                }
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

    @Override
	public JPopupMenu buildPopupMenu(DefaultMutableTreeNode selectedNode)
    {
        JPopupMenu menu = new JPopupMenu();
        if (selectedNode.isRoot()) {
            menu.add(buildValueAddMenuItem());
        } else {
            menu.add(buildEditMenuItem());
            menu.add(buildRemoveMenuItem());
        }
        return menu;
    }


}
