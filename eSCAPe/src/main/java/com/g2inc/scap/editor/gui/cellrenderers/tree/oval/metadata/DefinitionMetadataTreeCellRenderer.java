package com.g2inc.scap.editor.gui.cellrenderers.tree.oval.metadata;
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

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdom.Element;

import com.g2inc.scap.library.domain.oval.Metadata;
import com.g2inc.scap.library.util.CommonUtil;

/**
 * This class handles the custom rendering of tree nodes in the metadata detail
 * of an oval definition
 * 
 * @author ssill2
 * @see scap.gui.windows.oval.definition.DefinitionMetadataDetailTab
 */
public class DefinitionMetadataTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * the x size of an icon that is rendered for a node
	 */
	public static final int ICON_SIZE_X = 16;

	/**
	 * the y size of an icon that is rendered for a node
	 */
	public static final int ICON_SIZE_Y = 16;

	public DefinitionMetadataTreeCellRenderer() {
		super();
	}

	/**
	 * Overrides the DefaultTreeCellRenderer for the tree in the metadata detail
	 * for an oval definition
	 * 
	 * @return java.awt.Component
	 * @see scap.gui.windows.oval.definition.DefinitionMetadataDetailTab
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		Component cell = super.getTreeCellRendererComponent(tree, value,
				selected, expanded, leaf, row, hasFocus);

		if (value != null) {
			if (!(value instanceof DefaultMutableTreeNode)) {
				setText("BAR");
				return this;
			}

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			Object userObj = node.getUserObject();

			if (userObj instanceof Element) {
				Element e = (Element) userObj;

				StringBuilder pathbuff = new StringBuilder();
				CommonUtil.getElementPath(e, pathbuff);

				String ePath = pathbuff.toString();

				if (ePath.equals(Metadata.ELEMENT_PATH_TITLE)) {
					String text = e.getValue();
					String shortText = text;

					if (text != null && text.length() > 0) {
						if (text.length() > 127) {
							shortText = text.substring(0, 126) + "...";
						}
					} else {
						text = "No Title";
						shortText = "No Title";
					}

					setToolTipText(shortText);
					setText("Title: " + shortText);
				} else if (ePath.equals(Metadata.ELEMENT_PATH_DESCRIPTION)) {
					String text = e.getValue();
					String shortText = text;

					if (text != null && text.length() > 0) {
						if (text.length() > 127) {
							shortText = text.substring(0, 126) + "...";
						}
					} else {
						text = "No Description";
						shortText = text;
					}

					setToolTipText(shortText);
					setText("Description: " + shortText);
				} else if (ePath.equals(Metadata.ELEMENT_PATH_AFFECTED)) {
					String text = "Affected Platforms/Products for "
							+ e.getAttributeValue("family");
					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_AFFECTED_PLATFORM)) {
					String text = "Platform: " + e.getValue();
					setToolTipText(text);
					setText(text);
				} else if (ePath.equals(Metadata.ELEMENT_PATH_AFFECTED_PRODUCT)) {
					String text = "Product: " + e.getValue();
					setToolTipText(text);
					setText(text);
				} else if (ePath.equals(Metadata.ELEMENT_PATH_REFERENCE)) {
					String refurl = e.getAttributeValue("ref_url");
					if (refurl == null) {
						refurl = "";
					}

					String source = e.getAttributeValue("source");
					String refid = e.getAttributeValue("ref_id");

					String text = "Source = " + source + " Id = " + refid
							+ " ref_url = " + refurl;

					setToolTipText(text);
					setText("Reference: " + refid);
				} else if (ePath.equals(Metadata.ELEMENT_PATH_OVAL_REPOSITORY)) {
					String text = "Oval Repository Information";

					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_OVAL_REPOSITORY_STATUS)) {
					String text = "Current Repository status = " + e.getValue();

					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_OVAL_REPOSITORY_DATES)) {
					String text = "Dates";

					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_OVAL_REPOSITORY_DATES_SUBMITTED)) {
					String text = "Submitted " + e.getAttributeValue("date");

					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_OVAL_REPOSITORY_DATES_SUBMITTED_CONTRIBUTOR)) {
					String text = "By " + e.getValue() + " with "
							+ e.getAttributeValue("organization");

					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_OVAL_REPOSITORY_DATES_STATUS_CHANGE)) {
					String text = e.getAttributeValue("date")
							+ ": Changed to status " + e.getValue();

					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_OVAL_REPOSITORY_DATES_MODIFIED)) {
					String text = "Modified " + e.getAttributeValue("date");

					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_OVAL_REPOSITORY_DATES_MODIFIED_CONTRIBUTOR)) {
					String text = "By " + e.getValue() + " with "
							+ e.getAttributeValue("organization");

					setToolTipText(text);
					setText(text);
				} else if (ePath.equals(Metadata.ELEMENT_PATH_ADVISORY)) {
					String text = "Advisory from "
							+ e.getAttributeValue("from");

					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_ADVISORY_AFFECTED_CPE)) {
					String text = e.getValue();

					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_ADVISORY_AFFECTED_CPE_LIST)) {
					String text = e.getName();

					setToolTipText(text);
					setText(text);
				} else if (ePath.equals(Metadata.ELEMENT_PATH_ADVISORY_ISSUED)) {
					String text = "Issued " + e.getAttributeValue("date");

					setToolTipText(text);
					setText(text);
				} else if (ePath.equals(Metadata.ELEMENT_PATH_ADVISORY_UPDATED)) {
					String text = "Updated " + e.getAttributeValue("date");

					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_ADVISORY_SEVERITY)) {
					String text = "Severity: " + e.getValue();

					setToolTipText(text);
					setText(text);
				} else if (ePath.equals(Metadata.ELEMENT_PATH_ADVISORY_RIGHTS)) {
					String text = "Rights: " + e.getValue();

					setToolTipText(text);
					setText(text);
				} else if (ePath.equals(Metadata.ELEMENT_PATH_ADVISORY_CVE)) {
					String text = e.getValue() + " "
							+ e.getAttributeValue("href");

					setToolTipText(text);
					setText(text);
				} else if (ePath
						.equals(Metadata.ELEMENT_PATH_ADVISORY_BUGZILLA)) {
					String text = e.getValue() + " id "
							+ e.getAttributeValue("id") + " "
							+ e.getAttributeValue("href");

					setToolTipText(text);
					setText(text);
				} else {
					setText(e.getName());
				}

			} else {
				setText(userObj.toString());
			}
		} else {
			return cell;
		}

		return this;
	}
}