package com.g2inc.scap.editor.gui.model.tree.oval;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.g2inc.scap.library.domain.oval.OvalElement;

public class ChooserTreeModel<T> extends DefaultTreeModel
{
	private static final long serialVersionUID = 1L;
	private String typeFilter = null;

    public ChooserTreeModel() {
        super(new DefaultMutableTreeNode());
    }

    public ChooserTreeModel(String typeFilter) {
    	this();
        this.typeFilter = typeFilter;
    }

    public void setOvalElementList(List<T> objects) {
        HashMap <String, DefaultMutableTreeNode> subNodes =
                new HashMap<String, DefaultMutableTreeNode>();
        
        String suffix = null;  // suffix will be "test", "object", "state", or _variable"
        String rootTitle = null; // rootTitle will be "Tests", "Objects", "States", or "Variables"
        for (T listEntry : objects) {
        	OvalElement ovalElement = (OvalElement) listEntry;
        	String elementName = ovalElement.getElementName();
        	int underScore = elementName.indexOf("_");
        	String category = elementName.substring(0, underScore); // category will be "registry", "file", "external", etc
        	if (suffix == null) {
        		suffix = elementName.substring(underScore + 1);
        		StringBuilder sb = new StringBuilder();
        		sb.append(suffix.substring(0,1).toUpperCase());
        		sb.append(suffix.substring(1));
        		sb.append("s");
        		rootTitle = sb.toString(); 
        	}
        	
        	if (typeFilter != null && !(category.equals(typeFilter))) {
        		continue;
        	}
        	DefaultMutableTreeNode categoryNode = subNodes.get(category);
        	if (categoryNode == null) {
        		categoryNode = new DefaultMutableTreeNode(category);
                subNodes.put(category, categoryNode);
        	}
        	DefaultMutableTreeNode ovalNode = new DefaultMutableTreeNode(ovalElement);
        	categoryNode.add(ovalNode);
        }
        List<String> categoryList = new ArrayList<String>(subNodes.keySet());
        Collections.sort(categoryList);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
        int totalCount = 0;
        for (String category : categoryList) {
        	DefaultMutableTreeNode categoryNode = subNodes.get(category);
        	int childCount = categoryNode.getChildCount();
        	totalCount += childCount;
        	String titleWithCount = category + "(" + childCount + ")";
        	categoryNode.setUserObject(titleWithCount);
        	root.add(categoryNode);
        }
        root.setUserObject(rootTitle + "(" + totalCount + ")"); 
        reload(root);
    }
}
