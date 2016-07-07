package com.g2inc.scap.editor.gui.model.tree.cpe;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.util.Adder;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.library.domain.cpe.CPEComparator;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.cpe.CPEItem;

public class DocumentTreeModel extends DefaultTreeModel
{

    private static final long serialVersionUID = 1L;
    public static final String NODE_ITEMS = "Items";
    private static final Logger log = Logger.getLogger(DocumentTreeModel.class);
    private WeakReference<CPEDictionaryDocument> doc = null;
    private String filterString = null;
    private DefaultMutableTreeNode itemsNode = null;

    public DocumentTreeModel(CPEDictionaryDocument document, String filterString)
    {
        super(new DefaultMutableTreeNode("No Document Loaded"));

        // ns at start
        long modelStart = System.nanoTime();

        this.filterString = filterString;

        if (document != null)
        {
            this.doc = new WeakReference<CPEDictionaryDocument>(document);

            DefaultMutableTreeNode ourRoot = (DefaultMutableTreeNode) getRoot();
            ourRoot.setUserObject(doc.get());



            // Items
            long getItemsStart = System.nanoTime();
            List<CPEItem> itemsFromDoc = doc.get().getItems();
            long getItemsStop = System.nanoTime();
            showTime("getItems finished", getItemsStart, getItemsStop);
            
            HashMap<String, DefaultMutableTreeNode> subNodes =
                    new HashMap<String, DefaultMutableTreeNode>();
            List<CPEItem> iList = new ArrayList<CPEItem>(itemsFromDoc.size());
            iList.addAll(itemsFromDoc);

            itemsFromDoc.clear();
            itemsFromDoc = null;

            long itemsNodeStop = System.nanoTime();
            showTime("iList copying finished", getItemsStop, itemsNodeStop);
            
            if (iList.size() == 0)
            {
                if (itemsNode == null)
                {
                    itemsNode = new DefaultMutableTreeNode(NODE_ITEMS + "(0)");
                    insertNodeInto(itemsNode, (DefaultMutableTreeNode) root, 0);
                }
            }

            Collections.sort(iList, new CPEComparator());
            long sortStop = System.nanoTime();
            showTime("iList sort finished", itemsNodeStop, sortStop);
            
            long phase1Total = 0;
            long phase2Total = 0;
            long phase1Start = 0;
            long phase2Start = 0;
            phase1Start = System.nanoTime();
            for (int x = 0; x < iList.size(); x++)
            {	
                CPEItem item = iList.get(x);

                if (itemsNode == null)
                {
                    itemsNode = new DefaultMutableTreeNode(NODE_ITEMS + "(0)");

                    insertNodeInto(itemsNode, ourRoot, 0);
                }

                if (filterString != null && filterString.length() > 0)
                {
                    if (!item.matches(filterString))
                    {
                        continue;
                    }
                }
                
                

                DefaultMutableTreeNode child = new DefaultMutableTreeNode(item);
                itemsNode.add(child);
                
                phase2Start = System.nanoTime();
                phase1Total += (phase2Start - phase1Start);

//                Adder totalItemsCount = new Adder(0);
//                EditorUtil.countChildren(itemsNode, totalItemsCount);
//                itemsNode.setUserObject(NODE_ITEMS + "(" + totalItemsCount.getValue() + ")");
                
                phase1Start = System.nanoTime();
                phase2Total += (phase1Start - phase2Start);
            }
            showTime("Total phase 1 ", phase1Total);
            showTime("Total phase 2 ", phase2Total);
            itemsNode.setUserObject(NODE_ITEMS + "(" + itemsNode.getChildCount() + ")");
            long insertStop = System.nanoTime();
            showTime("insertion of DMTNs finished", sortStop, insertStop);
            subNodes.clear();
        }

        // ns since we started
        long modelFinish = System.nanoTime();

        // get difference in ns
        long diff = modelFinish - modelStart;

        // get difference in ms
        long diffMS = diff / 1000000;

      //  log.debug(EditorMessages.CPE + " Dictionary Document tree model, time taken(ms): " + diffMS);
    }
    
    private void showTime(String msg, long start, long end) {
    	showTime(msg, (end - start));
    }
    
    private void showTime(String msg, long time) {
    	double elapsedSecs = ((double) time) / 1000000000.0;
    //	log.debug(String.format("%s duration: %10.3f seconds", msg,	elapsedSecs));
    }

    @Override
    public Object getRoot()
    {
        return root;
    }

    public DefaultMutableTreeNode getItemsNode()
    {
        return itemsNode;
    }

    public String getFilterString()
    {
        return filterString;
    }

    public void setFilterString(String filterString)
    {
        this.filterString = filterString;

    }

    public void cleanUp()
    {
        if (root != null)
        {
            ((DefaultMutableTreeNode) root).removeAllChildren();

            root = null;
        }

        filterString = null;

        doc = null;
    }
}
