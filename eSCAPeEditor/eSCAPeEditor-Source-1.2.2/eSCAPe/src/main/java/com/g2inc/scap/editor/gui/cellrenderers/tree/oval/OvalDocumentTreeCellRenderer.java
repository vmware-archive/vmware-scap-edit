package com.g2inc.scap.editor.gui.cellrenderers.tree.oval;
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

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVariable;

/**
 * This class handles the custom rendering of tree nodes in the OvalEditorForm's
 * tree.
 *
 * @author ssill2
 * @see scap.gui.windows.oval.OvalEditorForm
 */
public class OvalDocumentTreeCellRenderer extends DefaultTreeCellRenderer
{
    /**
     * the x size of an icon that is rendered for a node
     */
    public static final int ICON_SIZE_X = 16;

    /**
     * the y size of an icon that is rendered for a node
     */
    public static final int ICON_SIZE_Y = 16;

    /**
     * Icon used for an inventory definition. Once opened the first time,
     * this icon is cached for all subsquent uses.
     */
    private static ImageIcon inventoryIcon = null;

    /**
     * Icon used for a vulnerability definition. Once opened the first time,
     * this icon is cached for all subsquent uses.
     */
    private static ImageIcon vulnerabilityIcon = null;

    /**
     * Icon used for a compliance definition. Once opened the first time,
     * this icon is cached for all subsquent uses.
     */
    private static ImageIcon complianceIcon = null;

    /**
     * Icon used for a patch definition. Once opened the first time,
     * this icon is cached for all subsquent uses.
     */
    private static ImageIcon patchIcon = null;

    /**
     * Rendered to be used for non-leave nodes.
     */
    public DefaultTreeCellRenderer nonLeaf = new DefaultTreeCellRenderer();

    /**
     * The no args constructor.
     */
    public OvalDocumentTreeCellRenderer()
    {
        super();
    }

    /**
     *  Load and cache the icon to be used by inventory definitions.
     *
     * @return javax.swing.ImageIcon
     * @see javax.swing.ImageIcon
     */
    private synchronized ImageIcon getInventoryIcon()
    {
        ImageIcon ret = null;

        if(inventoryIcon == null)
        {
            inventoryIcon = EditorUtil.createImageIcon(this.getClass(), "/com/g2inc/scap/editor/gui/icons/ovaldef-inv.png", ICON_SIZE_X, ICON_SIZE_Y);
        }

        ret = inventoryIcon;

        return ret;
    }

    /**
     *  Load and cache the icon to be used by vulnerability definitions.
     *
     * @return javax.swing.ImageIcon
     * @see javax.swing.ImageIcon
     */
    private synchronized ImageIcon getVulnerabilityIcon()
    {
        ImageIcon ret = null;

        if(vulnerabilityIcon == null)
        {
            vulnerabilityIcon = EditorUtil.createImageIcon(this.getClass(), "/com/g2inc/scap/editor/gui/icons/ovaldef-vuln.png", ICON_SIZE_X, ICON_SIZE_Y);
        }

        ret = vulnerabilityIcon;

        return ret;
    }

    /**
     *  Load and cache the icon to be used by compliance definitions.
     *  
     * @return javax.swing.ImageIcon
     * @see javax.swing.ImageIcon
     */
    private synchronized ImageIcon getComplianceIcon()
    {
        ImageIcon ret = null;

        if(complianceIcon == null)
        {
            complianceIcon = EditorUtil.createImageIcon(this.getClass(), "/com/g2inc/scap/editor/gui/icons/ovaldef-comp.png", ICON_SIZE_X, ICON_SIZE_Y);
        }

        ret = complianceIcon;

        return ret;
    }

    /**
     *  Load and cache the icon to be used by patch definitions.
     *  
     * @return javax.swing.ImageIcon
     * @see javax.swing.ImageIcon
     */
    private synchronized ImageIcon getPatchIcon()
    {
        ImageIcon ret = null;

        if(patchIcon == null)
        {
            patchIcon = EditorUtil.createImageIcon(this.getClass(), "/com/g2inc/scap/editor/gui/icons/ovaldef-patch.png", ICON_SIZE_X, ICON_SIZE_Y);
        }

        ret = patchIcon;

        return ret;
    }

    /**
     *  Overrides the DefaultTreeCellRenderer for the tree containing the main structure of the oval document
     *  in the OvalEditorForm
     *
     * @return java.awt.Component
     * @see scap.gui.windows.oval.OvalEditorForm
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        // TODO: worry about drag and drop

        if(!leaf || value == null)
        {
            return nonLeaf.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        }

        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        if(value != null)
        {
            if(!(value instanceof DefaultMutableTreeNode))
            {
                setText("BAR");
                return this;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObj = node.getUserObject();
            
            if(userObj instanceof OvalDefinition)
            {
                OvalDefinition od = (OvalDefinition) userObj;

                String titleText = od.getMetadata().getTitle();

                if(titleText != null && titleText.length() > 0)
                {
                    if(titleText.length() > 127)
                    {
                        titleText = titleText.substring(0,126) + "...";
                    }
                }
                else
                {
                    titleText = "No Title";
                }

                setText(od.getId() + " - " + titleText);

                setToolTipText("<HTML>" + titleText + "</HTML>");

                ImageIcon icon = null;

                switch(od.getDefinitionClass())
                {
                    case INVENTORY:
                        icon = getInventoryIcon();
                        break;
                    case VULNERABILITY:
                        icon = getVulnerabilityIcon();
                        break;
                    case COMPLIANCE:
                        icon = getComplianceIcon();
                        break;
                    case PATCH:
                        icon = getPatchIcon();
                        break;
                }

                if(icon != null)
                {
                    setIcon(icon);
                }
            }
            else if(userObj instanceof OvalDefinitionsDocument)
            {
                OvalDefinitionsDocument dd = (OvalDefinitionsDocument) userObj;
                
                setText(dd.toString());
                setToolTipText(dd.toString());
            }
            else if(userObj instanceof OvalTest)
            {
                OvalTest ot = (OvalTest) userObj;
                String comment = ot.getComment();

                if(comment != null && comment.length() > 0)
                {
                    if(comment.length() > 127)
                    {
                        comment = comment.substring(0,126) + "...";
                    }
                }
                else
                {
                    comment = "No Comment Set";
                }

                setText(ot.getId() + " - " + comment);

                String ttText = EditorUtil.buildTestToolTipText(ot).toString();
                setToolTipText("<HTML>" + ttText + "</HTML>");
            }
            else if(userObj instanceof OvalObject)
            {
                OvalObject oo = (OvalObject)userObj;
                String comment = oo.getComment();

                if(comment != null && comment.length() > 0)
                {
                    if(comment.length() > 127)
                    {
                        comment = comment.substring(0,126) + "...";
                    }
                }
                else
                {
                    comment = "No Comment Set";
                }

                setText(oo.getId() + " - " + comment);

                String ttText = EditorUtil.buildObjectToolTipText(oo).toString();
                setToolTipText("<HTML>" + ttText + "</HTML>");
            }
            else if(userObj instanceof OvalState)
            {
                OvalState os = (OvalState)userObj;
                String comment = os.getComment();

                if(comment != null && comment.length() > 0)
                {
                    if(comment.length() > 127)
                    {
                        comment = comment.substring(0,126) + "...";
                    }
                }
                else
                {
                    comment = "No Comment Set";
                }

                setText(os.getId() + " - " + comment);

                String ttText = EditorUtil.buildStateToolTipText(os).toString();
                setToolTipText("<HTML>" + ttText + "</HTML>");
            }
            else if(userObj instanceof OvalVariable)
            {
                OvalVariable ov = (OvalVariable)userObj;
                String comment = ov.getComment();

                if(comment != null && comment.length() > 0)
                {
                    if(comment.length() > 127)
                    {
                        comment = comment.substring(0,126) + "...";
                    }
                }
                else
                {
                    comment = "No Comment Set";
                }

                setText(ov.getId() + " - " + comment);

                String ttText = EditorUtil.buildVariableToolTipText(ov).toString();
                setToolTipText("<HTML>" + ttText + "</HTML>");
            }
            else if(userObj instanceof String)
            {
                setIcon(openIcon);
                setText((String)userObj);
                setToolTipText((String)userObj);
            }
            else
            {
                setIcon(openIcon);
                setText(node.getClass().getName());
                setToolTipText(node.getClass().getName());
            }
        }

        return this;
    }

}
