package com.g2inc.scap.editor.gui.cellrenderers.tree.oval.object;
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

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalObjectFilter;
import com.g2inc.scap.library.domain.oval.OvalObjectParameter;
import com.g2inc.scap.library.domain.oval.OvalObjectReference;
import com.g2inc.scap.library.domain.oval.OvalObjectSet;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalVariable;

/**
 * This class handles the custom rendering of tree nodes in the
 * ObjectParametersDisplayPanel
 *
 * @author ssill2
 * @see scap.gui.windows.oval.object.ObjectParametersDisplayPanel
 */
public class ObjectTreeCellRenderer extends DefaultTreeCellRenderer
{
	private static Logger log = Logger.getLogger(ObjectTreeCellRenderer.class);
	
    public ObjectTreeCellRenderer()
    {
        super();
    }

    /**
     *  Overrides the DefaultTreeCellRenderer for the tree in the
     *  ObjectParametersDisplayPanel
     *
     * @return java.awt.Component
     * @see scap.gui.windows.oval.object.ObjectParametersDisplayPanel
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
                    if(userObj instanceof OvalObject)
                    {
                        OvalObject oo = (OvalObject) userObj;

                        setText("Structure for " + EditorMessages.OVAL + " Object " + oo.getId());
                        setToolTipText(null);
                    }
                    else if(userObj instanceof OvalObjectSet)
                    {
                        OvalObjectSet oos = (OvalObjectSet) userObj;

                        String text = "Set<" + oos.getSetOperator().toString() + ">";
                        setText(text);
                        setToolTipText(text);

                    }
                    else if(userObj instanceof OvalObjectFilter)
                    {
                        OvalObjectFilter oof = (OvalObjectFilter) userObj;
                        OvalState os = oof.getState();

                        String text = "ObjectFilter: " + os.getElementName() + " with id " + os.getId();
                        setText(text);
                        setToolTipText(text);
                    }
                    else if(userObj instanceof OvalObjectReference)
                    {
                        OvalObjectReference oor = (OvalObjectReference) userObj;
                        OvalObject oo = oor.getObject();

                        String text = "ObjectReference: " + oo.getElementName() + " with id " + oo.getId();
                        setText(text);
                        setToolTipText(text);
                    }
                    else if(userObj instanceof OvalObjectParameter)
                    {
                        OvalObjectParameter oop = (OvalObjectParameter) userObj;

                        OvalVariable varRef = null;

                        varRef = oop.getVariableReference();

                        String text = null;

                        if(oop.isNil())
                        {
                            text = "Parameter(NIL) " + oop.getElementName();
                        }
                        else
                        {
                            if(varRef == null)
                            {
                                text = "Parameter(" + oop.getDatatype() + " | " + oop.getOperation() + ") " + oop.getElementName() + " = " + oop.getValue();
                            }
                            else
                            {
                                text = "Parameter:" + oop.getElementName() + " = variable reference to " + varRef.getId();
                            }
                        }
                        setText(text);
                        setToolTipText(text);
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
                log.debug(this.getClass().getName() + ": value is NOT a treeNode!");
                setText(value.getClass().getName());
            }
        }
        else
        {
            log.debug(this.getClass().getName() + ": value is null!");
        }

        return this;
    }
}
