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

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVariable;

public class DocumentTreeModel extends DefaultTreeModel
{

	private static final long serialVersionUID = 1L;
	public static final String NODE_DEFINITIONS = "Definitions";
    public static final String NODE_DEFINITIONS_INVENTORY = "inventory";
    public static final String NODE_DEFINITIONS_VULNERABILITY = "vulnerablity";
    public static final String NODE_DEFINITIONS_COMPLIANCE = "compliance";
    public static final String NODE_DEFINITIONS_PATCH = "patch";
    public static final String NODE_TESTS = "Tests";
    public static final String NODE_OBJECTS = "Objects";
    public static final String NODE_STATES = "States";
    public static final String NODE_VARIABLES = "Variables";
    private DefaultMutableTreeNode defsNode = null;
    private DefaultMutableTreeNode defsInvNode = null;
    private DefaultMutableTreeNode defsVulnNode = null;
    private DefaultMutableTreeNode defsCompNode = null;
    private DefaultMutableTreeNode defsPatchNode = null;
    private DefaultMutableTreeNode testsNode = null;
    private DefaultMutableTreeNode objectsNode = null;
    private DefaultMutableTreeNode statesNode = null;
    private DefaultMutableTreeNode varsNode = null;
    private static final Logger log = Logger.getLogger(DocumentTreeModel.class);
    private String filterString = null;

    public DocumentTreeModel(OvalDefinitionsDocument doc, String filterString)
    {
        super(new DefaultMutableTreeNode("No Document Loaded"));

        // ns at start
        long modelStart = System.nanoTime();

        this.filterString = filterString;

        if (doc != null)
        {

            DefaultMutableTreeNode ourRoot = (DefaultMutableTreeNode) getRoot();
            ourRoot.setUserObject(doc);

            // Definitions           
            if (defsNode == null) {
            	List<OvalDefinition> defsFromDoc = doc.getMatchingOvalDefinitions(filterString);
            	int definitionCount = defsFromDoc.size();
            //	log.debug("building Definitions node, size = " + definitionCount);
                defsNode = new SCAPParentTreeNode(NODE_DEFINITIONS + "(" + definitionCount + ")");
                addContainerNode(defsNode);
                defsNode.setParent(ourRoot);
            }


            // Tests   
            if (testsNode == null) {
            	List<OvalTest> testsFromDoc = doc.getMatchingOvalTests(filterString);
            	int testCount = testsFromDoc.size();
            //	log.debug("building Tests node, size = " + testCount);
                testsNode = new SCAPParentTreeNode(NODE_TESTS + "(" + testCount + ")");
                addContainerNode(testsNode);
                testsNode.setParent(ourRoot);
            }
            
            // Objects
            if (objectsNode == null) {
                List<OvalObject> objectsFromDoc = doc.getMatchingOvalObjects(filterString);
                int objectCount = objectsFromDoc.size();
              //  log.debug("objectsFromDoc size = " + objectsFromDoc.size());
                objectsNode = new SCAPParentTreeNode(NODE_OBJECTS + "(" + objectCount +")");
                addContainerNode(objectsNode);
                objectsNode.setParent(ourRoot);
            }

            // States
            if (statesNode == null) {
            	List<OvalState> statesFromDoc = doc.getMatchingOvalStates(filterString);
            	int stateCount = statesFromDoc.size();
                statesNode = new SCAPParentTreeNode(NODE_STATES + "(" + stateCount + ")");
                addContainerNode(statesNode);
                statesNode.setParent(ourRoot);
            }

            // variables
            if (varsNode == null) {
            	List<OvalVariable> variablesFromDoc = doc.getMatchingOvalVariables(filterString);
            	int variableCount = variablesFromDoc.size();
                varsNode = new SCAPParentTreeNode(NODE_VARIABLES + "(" + variableCount + ")");
                addContainerNode(varsNode);
                varsNode.setParent(ourRoot);
            }
        }

        // ns since we started
        long modelFinish = System.nanoTime();

        // get difference in ns
        long diff = modelFinish - modelStart;

        // get difference in ms
        long diffMS = diff / 1000000;

        //log.debug(EditorMessages.OVAL + " Document tree model, time taken(ms): " + diffMS);

    }

    @Override
    public Object getRoot()
    {
        return root;
    }

    public DefaultMutableTreeNode getDefsNode()
    {
        if (defsNode == null)
        {
            defsNode = new DefaultMutableTreeNode("Definitions(0)");
            addContainerNode(defsNode);
            reload(root);
        }
        return defsNode;
    }

    public DefaultMutableTreeNode getTestsNode()
    {
        if (testsNode == null)
        {
            testsNode = new DefaultMutableTreeNode("Tests(0)");
            addContainerNode(testsNode);
            reload(root);
        }
        return testsNode;
    }

    public DefaultMutableTreeNode getObjectsNode()
    {
        if (objectsNode == null)
        {
            objectsNode = new DefaultMutableTreeNode("Objects(0)");
            addContainerNode(objectsNode);
            reload(root);
        }
        return objectsNode;
    }

    public DefaultMutableTreeNode getStatesNode()
    {
        if (statesNode == null)
        {
        //	log.debug("getStatesNode creating states node");
            statesNode = new DefaultMutableTreeNode("States(0)");
            addContainerNode(statesNode);
            reload(root);
        }
       // log.debug("getStatesNode returning node with user object " + statesNode.getUserObject().toString());
        return statesNode;
    }

    public DefaultMutableTreeNode getVariablesNode()
    {
        if (varsNode == null)
        {
            varsNode = new DefaultMutableTreeNode("Variables(0)");
            addContainerNode(varsNode);
            reload(root);
        }
        return varsNode;
    }

    public DefaultMutableTreeNode getDefsCompNode()
    {
        return defsCompNode;
    }

    public DefaultMutableTreeNode getDefsInvNode()
    {
        return defsInvNode;
    }

    public DefaultMutableTreeNode getDefsPatchNode()
    {
        return defsPatchNode;
    }

    public DefaultMutableTreeNode getDefsVulnNode()
    {
        return defsVulnNode;
    }

    public String getFilterString()
    {
        return filterString;
    }

    public void setFilterString(String filterString)
    {
        this.filterString = filterString;

    }

    private void addContainerNode(DefaultMutableTreeNode newNode)
    {
        String nodeType = (String) newNode.getUserObject();
       // log.debug("addContainerNode called for nodeType " + nodeType);
        nodeType = nodeType.substring(0, nodeType.indexOf("(")).toLowerCase();
        int newNodeOrder = OvalDefinitionsDocument.DOC_ORDER.get(nodeType);
        DefaultMutableTreeNode ourRoot = (DefaultMutableTreeNode) getRoot();
        int childCount = ourRoot.getChildCount();
       // log.debug("child count for nodeType " + nodeType + " is " + childCount);
        int i = 0;
        for (i = 0; i < childCount; i++)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) ourRoot.getChildAt(i);
            nodeType = (String) node.getUserObject();
            nodeType = nodeType.substring(0, nodeType.indexOf("(")).toLowerCase();
            int nodeOrder = OvalDefinitionsDocument.DOC_ORDER.get(nodeType);
            if (newNodeOrder < nodeOrder)
            {
                break;
            }
        }
        ourRoot.insert(newNode, i);
    }

    public void cleanUp()
    {
        if(root != null)
        {
            ((DefaultMutableTreeNode)root).removeAllChildren();

            root = null;
        }

        defsCompNode = null;
        defsInvNode = null;
        defsNode = null;
        defsPatchNode = null;
        defsVulnNode = null;

        objectsNode = null;
        statesNode = null;
        testsNode = null;
        varsNode = null;

        filterString = null;
    }
}
