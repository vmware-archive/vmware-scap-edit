package com.g2inc.scap.editor.gui.cellrenderers.tree.oval.criteria;
/* ESCAPE Software   Copyright 2010 G2, Inc. - All rights reserved.
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

import com.g2inc.scap.library.domain.oval.Criteria;
import com.g2inc.scap.library.domain.oval.Criterion;
import com.g2inc.scap.library.domain.oval.ExtendDefinition;

/**
 * This class handles the custom rendering of tree nodes in the
 * DefinitionCriteriaDetailTab of an open definition.
 *
 * @author ssill2
 * @see scap.gui.windows.oval.definition.DefinitionCriteriaDetailTab
 */
public class CriteriaTreeCellRenderer extends DefaultTreeCellRenderer
{
    /**
     * the x size of an icon that is rendered for a node
     */
    public static final int ICON_SIZE_X = 16;
 
    /**
     * the y size of an icon that is rendered for a node
     */
     public static final int ICON_SIZE_Y = 16;

    public CriteriaTreeCellRenderer()
    {
        super();
    }

    /**
     *  Overrides the DefaultTreeCellRenderer for the tree containing the structure of Criteria elements
     *  in the DefinitionCriteriaDetailTab if ab open definition.
     *
     * @return java.awt.Component
     * @see scap.gui.windows.oval.definition.DefinitionCriteriaDetailTab
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        // TODO: worry about drag and drop

        Component cell = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        if (value != null)
        {
            if (!(value instanceof DefaultMutableTreeNode))
            {
                setText("BAR");
                return this;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObj = node.getUserObject();

            if (userObj instanceof Criteria)
            {
                Criteria c = (Criteria) userObj;

                String comment = c.getComment();
                if(comment == null || comment.length() == 0)
                {
                    comment = "";
                }
                else
                {
                    comment = "- " + comment;
                }

                setText("Criteria<" + c.getOperator().toString() + ">" + (c.isNegated() ? "(negated)":" ") + comment);
                setToolTipText(comment);
            }
            else if (userObj instanceof Criterion)
            {
                Criterion c = (Criterion) userObj;

                String comment = c.getComment();

                if(comment == null || comment.length() == 0)
                {
                    comment = "";
                }
                else
                {
                	comment = "- " + comment;
                }
                setText("Criterion<" + c.getTestId() + ">" + (c.isNegated() ? "(negated) ":" ") + comment);
                setToolTipText(comment);
            }
            else if (userObj instanceof ExtendDefinition)
            {
                ExtendDefinition edr = (ExtendDefinition) userObj;

                String comment = edr.getComment();

                if(comment == null || comment.length() == 0)
                {
                    comment = "";
                }
                else
                {
                	comment = "- " + comment;
                }
                setText("Definition<" + edr.getDefinitionId() + ">" + (edr.isNegated() ? "(negated) ":" ") + comment );
                setToolTipText(comment);
            }
            else
            {
                setText(userObj.toString());
            }
        }
        else
        {
            return cell;
        }

        return this;
    }
}