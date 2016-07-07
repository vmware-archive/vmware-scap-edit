package com.g2inc.scap.editor.gui.model.tree.oval;

import javax.swing.tree.DefaultMutableTreeNode;

public class SCAPParentTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;
	
	public SCAPParentTreeNode() {
		super();
	}
	
	public SCAPParentTreeNode(Object userObject) {
		super(userObject);
	}
	
	public SCAPParentTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}
	
	/**
	 * Never say this is a leaf node, so it will always have expansion control, 
	 * and treeWillExpand method can add children.
	 */
	public boolean isLeaf() {
		return false;
	}

}
