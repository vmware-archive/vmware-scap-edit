package com.g2inc.scap.editor.gui.cellrenderers.tree.oval.state;
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

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.library.domain.oval.OvalState;

/**
 * This class handles the custom rendering of tree nodes in the
 * state chooser
 *
 * @author ssill2
 * @see scap.gui.choosers.state.StateChooser
 */
public class StateChooserTreeCellRenderer extends DefaultTreeCellRenderer
{
	private static Logger log = Logger.getLogger(StateChooserTreeCellRenderer.class);
	
    public StateChooserTreeCellRenderer()
    {
        super();
    }

    /**
     *  Overrides the DefaultTreeCellRenderer for the tree in the state chooser
     *
     * @return java.awt.Component
     * @see scap.gui.choosers.state.StateChooser
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        if(value != null)
        {
            if(value instanceof DefaultMutableTreeNode)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

                Object userObj = node.getUserObject();

                if(userObj != null)
                {
                    if(userObj instanceof OvalState)
                    {
                        OvalState os = (OvalState) userObj;

                        String comment = os.getComment();

                        if(comment == null || comment.length() == 0)
                        {
                            comment = "No comment set";
                        }

                        setText(os.getId() + " - " + comment);
                        setToolTipText(EditorUtil.buildStateToolTipText(os).toString());
                    }
                    else
                    {
                        setText(userObj.toString());
                        setToolTipText(userObj.toString());
                    }

                }
                else
                {
                    setText("Null UserObject");
                }
            }
            else
            {
           //     log.debug(this.getClass().getName() + ": value is NOT a treeNode!");
                setText(value.getClass().getName());
            }
        }
        else
        {
        //    log.debug(this.getClass().getName() + ": value is null!");
        }

        return this;
    }
}
