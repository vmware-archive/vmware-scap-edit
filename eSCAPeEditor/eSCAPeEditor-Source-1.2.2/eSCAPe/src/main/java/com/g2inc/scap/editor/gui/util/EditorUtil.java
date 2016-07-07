package com.g2inc.scap.editor.gui.util;
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
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.baseid.BaseIdEditor;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.oval.OvalComparator;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalElement;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalObjectChild;
import com.g2inc.scap.library.domain.oval.OvalObjectFilter;
import com.g2inc.scap.library.domain.oval.OvalObjectParameter;
import com.g2inc.scap.library.domain.oval.OvalObjectReference;
import com.g2inc.scap.library.domain.oval.OvalObjectSet;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalStateParameter;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVariable;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentLiteral;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentObject;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentVariable;

public class EditorUtil
{

    private static final Logger log = Logger.getLogger(EditorUtil.class);
    public static final String eol = "<BR>" + System.getProperty("line.separator");

    public static void writeProperties(Properties p, String filename)
    {
        if (p != null)
        {
            FileOutputStream fos = null;

            try
            {
                fos = new FileOutputStream(filename);
                p.store(fos, "Updated " + new Timestamp(System.currentTimeMillis()).toString());
                fos.flush();
                fos.close();
            } catch (Exception e)
            {
                log.error("Error writing properties to " + filename, e);
            }
        }
    }

    public static void loadProperties(Properties p, String filename)
    {
        if (p != null)
        {
            FileInputStream fis = null;

            try
            {
                File f = new File(filename);
                if (!f.exists())
                {
                    f.createNewFile();
                }

                fis = new FileInputStream(f);
                p.load(fis);
                fis.close();
            } catch (Exception e)
            {
                log.error("Error reading properties from " + filename, e);
            }
        }
    }

    public static String getUserDir()
    {
        return System.getProperty("user.home", ".");
    }

    public static ImageIcon createImageIcon(Class clazz, String pathToImage)
    {
        ImageIcon ret = null;
        log.debug("pathToImage=" + (pathToImage == null ? "null" : pathToImage));
        if (pathToImage != null)
        {
            try
            {
                URL url = clazz.getResource(pathToImage);
                log.debug("URL=" + (url == null ? "null" : url.toString()));
                ret = new ImageIcon(url);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public static ImageIcon createImageIcon(Class clazz, String pathToImage, int sizeX, int sizeY)
    {
        ImageIcon ret = null;
        log.debug("pathToImage=" + (pathToImage == null ? "null" : pathToImage));
        if (pathToImage != null)
        {
            try
            {
                InputStream is = clazz.getResourceAsStream(pathToImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = is.read(buffer)) != -1)
                {
                    baos.write(buffer, 0, bytesRead);
                }
                ret = new ImageIcon(baos.toByteArray());
//                URL url = clazz.getResource(pathToImage);
//                System.out.println("URL=" + (url == null ? "null" : url.toString()));
//                ret = new ImageIcon(url);

                Image i = ret.getImage();

                Image ni = i.getScaledInstance(sizeX, sizeY, java.awt.Image.SCALE_SMOOTH);

                ret = new ImageIcon(ni);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public static TreePath getTreePathToChild(DefaultMutableTreeNode child)
    {
        TreePath path = null;

        ArrayList<Object> al = new ArrayList<Object>();

        TreeNode node = child;

        while (node != null)
        {
            al.add(0, node);

            node = node.getParent();
        }

        if (al.size() > 0)
        {
            Object[] objs = al.toArray();
            path = new TreePath(objs);
        }
        return path;
    }

    public static void countChildren(DefaultMutableTreeNode node, Adder totalChildCount)
    {
        Object userObj = node.getUserObject();

        if (userObj != null)
        {
            if (!(userObj instanceof String))
            {
                totalChildCount.plusplus();
            }
        }

        int count = node.getChildCount();
        if (count > 0)
        {
            for (int x = 0; x < count; x++)
            {
                DefaultMutableTreeNode kid = (DefaultMutableTreeNode) node.getChildAt(x);

                countChildren(kid, totalChildCount);
            }
        }
    }

    public static void markActiveWindowDirty(JFrame parentWin, boolean b)
    {
        if (parentWin == null)
        {
            return;
        }

        if (parentWin instanceof EditorMainWindow)
        {
            EditorMainWindow mainWin = (EditorMainWindow) parentWin;
            mainWin.markActiveWindowDirty(b);
        }
    }

    public static DefaultMutableTreeNode createSubTreeIfNecessary(TreeModel model, DefaultMutableTreeNode parent, String childName)
    {
        DefaultMutableTreeNode ret = null;

        ret = getSubtree(parent, childName);

        if (ret == null)
        {
            ret = new DefaultMutableTreeNode(childName + "(0)");

            DefaultTreeModel dtm = (DefaultTreeModel) model;
            dtm.insertNodeInto(ret, parent, 0);
        }

        return ret;
    }

    private static DefaultMutableTreeNode getSubtree(DefaultMutableTreeNode parent, String childName)
    {
        DefaultMutableTreeNode ret = null;
        Enumeration children = parent.children();

        while (children.hasMoreElements())
        {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();

            Object userObj = childNode.getUserObject();

            if (userObj instanceof String)
            {
                String name = (String) userObj;

                String tmpName = name;

                int loc = tmpName.indexOf("(");
                if (loc > -1)
                {
                    tmpName = tmpName.substring(0, loc);

                    name = tmpName;
                }
                if (name.equals(childName))
                {
                    ret = childNode;
                    break;
                }
            }
        }

        return ret;
    }

    public static String editBaseIdIfNecessary(OvalDefinitionsDocument ovalDefDoc)
    {
        String baseId = ovalDefDoc.getBaseId();

        while (baseId == null)
        {
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            BaseIdEditor editorPage = new BaseIdEditor();
            editorPage.setOvalDoc(ovalDefDoc);
            editorPage.setBaseId(baseId);
            editor.setEditorPage(editorPage);
            editor.pack();
            editor.setLocationRelativeTo(EditorMainWindow.getInstance());

            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                String newBaseId = editorPage.getBaseId();
                if (newBaseId != null && (baseId == null || !baseId.equals(newBaseId)))
                {
                    ovalDefDoc.setBaseId(newBaseId);
                    EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                }
                baseId = newBaseId;
            } else
            {
                baseId = "changeme";
                break;
            }
        }

        return baseId;
    }

    // return number of direct child set nodes the given node contains
    public static int getChildSetCount(DefaultMutableTreeNode node)
    {
        int ret = 0;

        int cc = node.getChildCount();

        if (cc > 0)
        {
            for (int x = 0; x < cc; x++)
            {
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) node.getChildAt(x);

                Object userObj = cNode.getUserObject();

                if (userObj instanceof OvalObjectSet)
                {
                    ret++;
                }
            }
        }

        return ret;
    }

    // return number of direct non-set child nodes the given node contains
    public static int getChildNonSetCount(DefaultMutableTreeNode node)
    {
        int ret = 0;

        int cc = node.getChildCount();

        if (cc > 0)
        {
            for (int x = 0; x < cc; x++)
            {
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) node.getChildAt(x);

                Object userObj = cNode.getUserObject();

                if (!(userObj instanceof OvalObjectSet))
                {
                    ret++;
                }
            }
        }

        return ret;
    }

    // return number of direct child object reference nodes the given node contains
    public static int getChildObjRefCount(DefaultMutableTreeNode node)
    {
        int ret = 0;

        int cc = node.getChildCount();

        if (cc > 0)
        {
            for (int x = 0; x < cc; x++)
            {
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) node.getChildAt(x);

                Object userObj = cNode.getUserObject();

                if (userObj instanceof OvalObjectReference)
                {
                    ret++;
                }
            }
        }

        return ret;
    }

    // return parameters already added
    public static Set<String> getExistingObjectParameters(DefaultMutableTreeNode node)
    {
        Set<String> ret = new HashSet<String>();

        int cc = node.getChildCount();

        if (cc > 0)
        {
            for (int x = 0; x < cc; x++)
            {
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) node.getChildAt(x);

                Object userObj = cNode.getUserObject();

                if (userObj instanceof OvalObjectParameter)
                {
                    OvalObjectParameter oop = (OvalObjectParameter) userObj;

                    ret.add(oop.getElementName());
                }
            }
        }

        return ret;
    }

    public static int whereToInsertTypeNode(DefaultMutableTreeNode parent, String type)
    {
        int index = 0;

        int childCount = parent.getChildCount();

        if (childCount == 0)
        {
            return index;
        }

        ArrayList<String> existingNames = new ArrayList<String>();
        existingNames.add(type);

        for (int x = 0; x < childCount; x++)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(x);

            Object userObj = child.getUserObject();

            if (userObj instanceof String)
            {
                String st = (String) userObj;
                existingNames.add(st);
            }
        }

        Collections.sort(existingNames);

        return existingNames.indexOf(type);
    }

    public static int whereToInsertOvalElement(DefaultMutableTreeNode parent, OvalElement supplied_oe)
    {
        int index = 0;

        int childCount = parent.getChildCount();

        if (childCount == 0)
        {
            return index;
        }

        ArrayList<OvalElement> existingElements = new ArrayList<OvalElement>();
        existingElements.add(supplied_oe);

        for (int x = 0; x < childCount; x++)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(x);

            Object userObj = child.getUserObject();

            if (userObj instanceof OvalElement)
            {
                OvalElement oe = (OvalElement) userObj;
                existingElements.add(oe);
            }
        }

        Collections.sort(existingElements, new OvalComparator<OvalElement>());

        return existingElements.indexOf(supplied_oe);
    }

    private static void recurseObjectForToolTip(OvalObjectChild node, String indent, StringBuilder sb)
    {
        if (node instanceof OvalObjectParameter)
        {
            OvalObjectParameter oop = (OvalObjectParameter) node;

            if (oop.getVarRef() == null)
            {
                sb.append(indent + "Parameter: " + oop.getElementName() + " = " + oop.getValue() + eol);
            }
            else
            {
                sb.append(indent + "Parameter: " + oop.getElementName() + " = varRef->" + oop.getVarRef() + eol);
            }
        }
        else if (node instanceof OvalObjectSet)
        {
            OvalObjectSet oos = (OvalObjectSet) node;

            sb.append(indent + "Set<" + oos.getSetOperator().toString() + ">" + eol);
        }
        else if (node instanceof OvalObjectReference)
        {
            OvalObjectReference oor = (OvalObjectReference) node;
            if (oor.getObjectId() != null)
            {
            	sb.append(indent + "objRef->" + oor.getObjectId() + eol);
            }
        }
        else if (node instanceof OvalObjectFilter)
        {
            OvalObjectFilter oof = (OvalObjectFilter) node;
            if (oof.getStateId() != null)
            {
            	sb.append(indent + "filter->" + oof.getStateId() + eol);
            }
        }
        
        for (OvalObjectChild child : node.getChildren())
        {
            recurseObjectForToolTip(child, indent + "&nbsp;&nbsp;", sb);
        }
    }

    public static StringBuilder buildObjectToolTipText(OvalObject oo)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML>");

        String comment = oo.getComment();

        if (comment != null && comment.length() > 0)
        {
            sb.append("Comment: " + comment + eol);
        }

        for (OvalObjectChild child : oo.getChildren())
        {
            sb.append("Parameters:" + eol);
            recurseObjectForToolTip(child, "", sb);
        }

//        DefaultMutableTreeNode node = oo.getChildTreeNodes();
//
//        if(node != null && node.getChildCount() > 0)
//        {
//            sb.append("Parameters:" + eol);
//            recurseObjectForToolTip(node, "", sb);
//        }

        sb.append("</HTML>");
        return sb;
    }

    public static StringBuilder buildStateToolTipText(OvalState os)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML>");

        String indent = "&nbsp;&nbsp;";

        String comment = os.getComment();

        if (comment != null && comment.length() > 0)
        {
            sb.append("Comment: " + comment + eol);
        }

        List<OvalStateParameter> parms = os.getParameters();

        if (parms != null && parms.size() > 0)
        {
            sb.append("Parameters:" + eol);
            for (int x = 0; x < parms.size(); x++)
            {
                OvalStateParameter osp = parms.get(x);
                if (osp.getVarRef() == null)
                {
                    sb.append(indent + "Parameter: " + osp.getElementName() + " = " + osp.getValue() + eol);
                } else
                {
                    sb.append(indent + "Parameter: " + osp.getElementName() + " = varRef->" + osp.getVarRef() + eol);
                }
            }
        }

        sb.append("</HTML>");
        return sb;
    }

    private static void recurseVariableForToolTip(DefaultMutableTreeNode node, String indent, StringBuilder sb)
    {
        int cc = node.getChildCount();

        Object userObj = node.getUserObject();

        if (userObj != null)
        {
            if (userObj instanceof OvalVariableComponentLiteral)
            {
                OvalVariableComponentLiteral lit = (OvalVariableComponentLiteral) userObj;

                if (lit.getValue() != null)
                {
                    sb.append(indent + "Literal: " + lit.getValue() + eol);
                }
            } else if (userObj instanceof OvalVariableComponentObject)
            {
                OvalVariableComponentObject obj = (OvalVariableComponentObject) userObj;

                if (obj.getObjectId() != null)
                {
                    sb.append(indent + "Object: id=" + obj.getObjectId() + " field=" + obj.getItemField() + eol);
                }
            } else if (userObj instanceof OvalVariableComponentVariable)
            {
                OvalVariableComponentVariable var = (OvalVariableComponentVariable) userObj;

                if (var.getVariableId() != null)
                {
                    sb.append(indent + "Variable: id=" + var.getVariableId() + eol);
                }
            }
        }

        if (cc > 0)
        {
            for (int x = 0; x < cc; x++)
            {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(x);
                recurseVariableForToolTip(child, indent + "&nbsp;&nbsp;", sb);
            }
        }
    }

    public static StringBuilder buildVariableToolTipText(OvalVariable ov)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<HTML>");
        String comment = ov.getComment();

        if (comment != null && comment.length() > 0)
        {
            sb.append("Comment: " + comment + eol);
        }

        DefaultMutableTreeNode node = ov.getChildren();
        if (node != null && node.getChildCount() > 0)
        {
            sb.append("Parameters:" + eol);
            recurseVariableForToolTip(node, "", sb);
        }

        sb.append("</HTML>");
        return sb;
    }

    public static StringBuilder buildTestToolTipText(OvalTest ot)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<HTML>");
        String comment = ot.getComment();

        if (comment != null && comment.length() > 0)
        {
            sb.append("Comment: " + comment + eol);
        }

        String objId = ot.getObjectId();
        if (objId != null && objId.length() > 0)
        {
            sb.append("Object: id=" + objId + eol);
        }

        String steId = ot.getObjectId();
        if (steId != null && steId.length() > 0)
        {
            sb.append("State: id=" + steId + eol);
        }

        sb.append("</HTML>");
        return sb;
    }
    
    public static void expandTree(JTree tree)
    {
        TreeModel tm = tree.getModel();

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tm.getRoot();
        expandTreeRecurser(tree, rootNode);
    }

    private static void expandTreeRecurser(JTree tree, DefaultMutableTreeNode node)
    {
        TreePath path = getTreePathToChild(node);
        tree.expandPath(path);

        int childCount = node.getChildCount();

        if(childCount != 0)
        {
            for(int x = 0; x < childCount ; x++)
            {
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(x);
                expandTreeRecurser(tree, childNode);
            }
        }
    }

    /**
     * Take the given string and insert html breaks at the given column widths
     * but attempt to do it on whitespace and not in the middle of a word.
     * 
     * @param startingString
     * @param columnWidth
     * @return
     */
    public static String insertHtmlBreaks(String startingString, int columnWidth)
    {
    	if(startingString == null || startingString.length() < columnWidth)
    	{
    		return startingString;
    	}
    	
    	StringBuilder sb = new StringBuilder(startingString);
    	String breakString = "<br>";
    	
    	int index = 0;
    	int col = 1;
    	
    	while(index < sb.length())
    	{
    		if(col >= columnWidth && (sb.charAt(index) == ' ' || sb.charAt(index) == '\t'))
    		{
    			sb.insert(index, breakString);
    			index += breakString.length();
    			col = 1;
    		}
    		else
    		{
    			col++;
        		index++;
    		}
    	}
    	
    	return sb.toString();
    }

    /**
     * Show a message dialog with given settings but have the message text wrapped at 75 characters.
     * 
     * @param component
     * @param message
     * @param title
     * @param optPaneType
     */
    public static void showMessageDialog(Component component, String message, String title, int optPaneType)
    {
    	showMessageDialog(component, message, title, optPaneType, 75);
    }

    /**
     * Show a message dialog with the given settings.
     * 
     * @param component
     * @param message
     * @param title
     * @param optPaneType
     * @param numberColumns
     */
    public static void showMessageDialog(Component component, String message, String title, int optPaneType, int numberColumns)
    {
    	String htmlMessage = "<HTML>" + insertHtmlBreaks(message, numberColumns) + "</HTML>"; 
        JOptionPane.showMessageDialog(component,
                htmlMessage,
                title,
                optPaneType);    	
    }
}