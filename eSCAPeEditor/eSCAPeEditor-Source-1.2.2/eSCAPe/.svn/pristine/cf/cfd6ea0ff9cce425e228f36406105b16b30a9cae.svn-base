package com.g2inc.scap.editor.gui.choosers.xccdf;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.g2inc.scap.editor.gui.util.Adder;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.library.domain.xccdf.Value;

public class ValueChooserTreeModel extends DefaultTreeModel
{
    private String typeFilter = null;
    private String textFilter = null;

    public ValueChooserTreeModel()
    {
        super(new DefaultMutableTreeNode("Values"));
    }

    public ValueChooserTreeModel(String filter)
    {
        super(new DefaultMutableTreeNode("Values"));
        this.typeFilter = filter;
    }

    public ValueChooserTreeModel(String typeFilter, String textFilter)
    {
        super(new DefaultMutableTreeNode("Values"));
        this.typeFilter = typeFilter;
        this.textFilter = textFilter;
    }

    public void setValues(List<Value> values)
    {
        if(values == null) {
            return;
        }

        Collections.sort(values, new Comparator<Value>() {
			public int compare(Value o1, Value o2) {
				return (o1.getId().compareToIgnoreCase(o2.getId()));
			}
		});

        HashMap <String, DefaultMutableTreeNode> subNodes =
                new HashMap<String, DefaultMutableTreeNode>();
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) root;
        for(int x = 0; x < values.size() ; x++)
        {
            Value rule = values.get(x);
            if(textFilter != null)
            {
                if(!rule.matches(textFilter))
                {
                    continue;
                }
            }
            
			DefaultMutableTreeNode childObject = new DefaultMutableTreeNode(rule);
			rootNode.add(childObject);
        }
		Adder cCount = new Adder(0);
		EditorUtil.countChildren(rootNode, cCount);
		rootNode.setUserObject("Values(" + cCount.getValue() + ")");
        reload(root);
    }
}
