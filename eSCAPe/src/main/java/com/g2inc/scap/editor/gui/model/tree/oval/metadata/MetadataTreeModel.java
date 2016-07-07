package com.g2inc.scap.editor.gui.model.tree.oval.metadata;
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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jdom.Element;

import com.g2inc.scap.library.domain.oval.AffectedItemContainer;
import com.g2inc.scap.library.domain.oval.AffectedPlatform;
import com.g2inc.scap.library.domain.oval.AffectedProduct;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalReference;

public class MetadataTreeModel extends DefaultTreeModel {

    private static final long serialVersionUID = 1L;

    public MetadataTreeModel(OvalDefinition doc) {
        super(doc.getMetadata().asTree());
    }

    public int whereToInsertReference(OvalReference ref) {
        int loc = -1;
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) root;

        int titleLoc = -1;
        int affectedLoc = -1;
        int descLoc = -1;

        int childCount = rootNode.getChildCount();

        for (int x = 0; x < childCount; x++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) rootNode.getChildAt(x);

            Element nodeElement = (Element) child.getUserObject();

            if (nodeElement.getName().toLowerCase().equals("title")) {
                titleLoc = x;
            } else if (nodeElement.getName().toLowerCase().equals("affected")) {
                affectedLoc = x;
            } else if (nodeElement.getName().toLowerCase().equals("description")) {
                descLoc = x;
                break;
            }
        }

        if (descLoc != -1) {
            // should be inserted before description
            loc = descLoc;
        } else if (affectedLoc != -1) {
            // should be inserted after affected element
            loc = affectedLoc + 1;
        } else if (titleLoc != -1) {
            // insert after title
            loc = titleLoc + 1;
        }

        return loc;
    }

    public int whereToInsertAffected(AffectedItemContainer container) {
        int loc = -1;
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) root;

        int titleLoc = -1;
        int referenceLoc = -1;
        int descLoc = -1;
        int affectedLoc = -1;

        int childCount = rootNode.getChildCount();

        for (int x = 0; x < childCount; x++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) rootNode.getChildAt(x);

            Element nodeElement = (Element) child.getUserObject();

            if (nodeElement.getName().toLowerCase().equals("title")) {
                titleLoc = x;
            } else if (nodeElement.getName().toLowerCase().equals("affected")) {
                affectedLoc = x;
                break;
            } else if (nodeElement.getName().toLowerCase().equals("reference")) {
                referenceLoc = x;
                break;
            } else if (nodeElement.getName().toLowerCase().equals("description")) {
                descLoc = x;
                break;
            }
        }

        if (affectedLoc != -1) {
            // should be inserted before any other affected elements
            loc = affectedLoc;
        } else if (referenceLoc != -1) {
            // should be inserted before any references
            loc = referenceLoc;
        } else if (titleLoc != -1) {
            // insert after title
            loc = titleLoc + 1;
        } else if (descLoc != -1) {
            // insert before desc
            loc = descLoc;
        }

        return loc;
    }

    public int whereToInsertAffectedPlatform(DefaultMutableTreeNode affectedNode, AffectedPlatform platform)
    {
        int loc = 0;

        int platformLoc = -1;
        int productLoc = -1;

        int childCount = affectedNode.getChildCount();

        for (int x = 0; x < childCount; x++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) affectedNode.getChildAt(x);

            Element nodeElement = (Element) child.getUserObject();

            if (nodeElement.getName().toLowerCase().equals("platform")) {
                platformLoc = x;
            } else if (nodeElement.getName().toLowerCase().equals("product")) {
                productLoc = x;
                break;
            }
        }

        if (productLoc != -1) {
            // should be inserted before any product elements
            loc = productLoc;
        } else if (platformLoc != -1) {
            // should be inserted after any other platform references
            loc = platformLoc;
        }

        return loc;
    }

    public int whereToInsertAffectedProduct(DefaultMutableTreeNode affectedNode, AffectedProduct product)
    {
        int loc = 0;

        int platformLoc = -1;
        int productLoc = -1;

        int childCount = affectedNode.getChildCount();

        for (int x = 0; x < childCount; x++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) affectedNode.getChildAt(x);

            Element nodeElement = (Element) child.getUserObject();

            if (nodeElement.getName().toLowerCase().equals("platform")) {
                platformLoc = x;
            } else if (nodeElement.getName().toLowerCase().equals("product")) {
                productLoc = x;
            }
        }

        if (productLoc != -1) {
            // should be inserted before any other product elements
            loc = productLoc;
        } else if (platformLoc != -1) {
            // should be inserted after any platform references
            loc = platformLoc + 1;
        }

        return loc;
    }
}
