package com.g2inc.scap.editor.gui.model.tree.oval.criteria;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.oval.Criteria;
import com.g2inc.scap.library.domain.oval.CriteriaChild;
import com.g2inc.scap.library.domain.oval.OvalDefinition;

public class CriteriaTreeModel  extends DefaultTreeModel
{
	private static Logger log = Logger.getLogger(CriteriaTreeModel.class);

	public CriteriaTreeModel(OvalDefinition doc)
	{
        super(buildCriteriaRootNode(doc));
    }

	/**
	 * Create a tree (of DefaultMutableTreeNodes) to represent the structure of
	 * a definitions criteria
	 *
	 * @return DefaultMutableTreeNode
	 */
    public static DefaultMutableTreeNode buildCriteriaRootNode(OvalDefinition def)
    {
		log.debug("buildCriteriaRootNode called for definition " + def.getId());
        DefaultMutableTreeNode cloned = null;
        Criteria criteria = def.getCriteria();
        if(criteria != null && criteria.getChildren().size() > 0)
        {
            cloned = new DefaultMutableTreeNode(criteria);
            buildCriteriaNode(cloned);
        }
        else
        {
            // there are no criteria defined
            cloned = new DefaultMutableTreeNode("No criteria defined");
        }
        return cloned;
    }

    private static void buildCriteriaNode(DefaultMutableTreeNode node)
    {
    	CriteriaChild criteriaParent = (CriteriaChild) node.getUserObject();
    	List<CriteriaChild> children = criteriaParent.getChildren();
    	for (CriteriaChild child : children)
    	{
    		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
    		node.add(childNode);
    		buildCriteriaNode(childNode);
    	}
    }
}
