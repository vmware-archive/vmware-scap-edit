package com.g2inc.scap.library.domain.oval;
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
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.jdom.Element;
import org.jdom.Namespace;

import com.g2inc.scap.library.util.CommonUtil;

/**
 * This class represents the data in a metadata tag in an oval definition.
 * 
 * @author ssill2
 * @see OvalDefinition
 */
public abstract class Metadata extends OvalElement
{
	public static final String ELEMENT_NAME = "metadata";
	
	public Metadata(OvalDefinitionsDocument parentDocument)
	{
		super(parentDocument);
	}
	
	public final static HashMap<String, Integer> METADATA_ORDER = new HashMap<String, Integer>();
	static 
	{
		METADATA_ORDER.put("title", 0);
		METADATA_ORDER.put("affected", 1);
		METADATA_ORDER.put("reference", 2);
		METADATA_ORDER.put("description", 3);
	}

    public static final String ELEMENT_PATH_METADATA =
            ".metadata.definition.definitions";
    public static final String ELEMENT_PATH_TITLE
            = ".title.metadata.definition.definitions";
    public static final String ELEMENT_PATH_AFFECTED
            = ".affected.metadata.definition.definitions";
    public static final String ELEMENT_PATH_AFFECTED_PLATFORM =
            ".platform.affected.metadata.definition.definitions";
    public static final String ELEMENT_PATH_AFFECTED_PRODUCT =
            ".product.affected.metadata.definition.definitions";
    public static final String ELEMENT_PATH_REFERENCE =
            ".reference.metadata.definition.definitions";
    public static final String ELEMENT_PATH_DESCRIPTION =
            ".description.metadata.definition.definitions";
    public static final String ELEMENT_PATH_OVAL_REPOSITORY =
            ".oval_repository.metadata.definition.definitions";
    public static final String ELEMENT_PATH_OVAL_REPOSITORY_DATES
            = ".dates.oval_repository.metadata.definition.definitions";
    public static final String ELEMENT_PATH_OVAL_REPOSITORY_DATES_SUBMITTED
            = ".submitted.dates.oval_repository.metadata.definition.definitions";
    public static final String ELEMENT_PATH_OVAL_REPOSITORY_DATES_SUBMITTED_CONTRIBUTOR
            = ".contributor.submitted.dates.oval_repository.metadata.definition.definitions";
    public static final String ELEMENT_PATH_OVAL_REPOSITORY_DATES_STATUS_CHANGE
            = ".status_change.dates.oval_repository.metadata.definition.definitions";
    public static final String ELEMENT_PATH_OVAL_REPOSITORY_DATES_MODIFIED
            = ".modified.dates.oval_repository.metadata.definition.definitions";
    public static final String ELEMENT_PATH_OVAL_REPOSITORY_DATES_MODIFIED_CONTRIBUTOR
            = ".contributor.modified.dates.oval_repository.metadata.definition.definitions";
    public static final String ELEMENT_PATH_OVAL_REPOSITORY_STATUS
            = ".status.oval_repository.metadata.definition.definitions";

    // these are redhat specific elements
    public static final String ELEMENT_PATH_ADVISORY
            = ".advisory.metadata.definition.definitions";
    public static final String ELEMENT_PATH_ADVISORY_SEVERITY
            = ".severity.advisory.metadata.definition.definitions";
    public static final String ELEMENT_PATH_ADVISORY_RIGHTS
            = ".rights.advisory.metadata.definition.definitions";
    public static final String ELEMENT_PATH_ADVISORY_ISSUED
            = ".issued.advisory.metadata.definition.definitions";
    public static final String ELEMENT_PATH_ADVISORY_UPDATED
            = ".updated.advisory.metadata.definition.definitions";
    public static final String ELEMENT_PATH_ADVISORY_CVE
            = ".cve.advisory.metadata.definition.definitions";
    public static final String ELEMENT_PATH_ADVISORY_BUGZILLA
            = ".bugzilla.advisory.metadata.definition.definitions";
    public static final String ELEMENT_PATH_ADVISORY_AFFECTED_CPE_LIST
            = ".affected_cpe_list.advisory.metadata.definition.definitions";
    public static final String ELEMENT_PATH_ADVISORY_AFFECTED_CPE
            = ".cpe.affected_cpe_list.advisory.metadata.definition.definitions";

    /**
     * Get the description.
     * 
     * @return String
     */
	public String getDescription()
	{
		String description = null;
		
		Namespace ns = getElement().getNamespace();
		Element descElement = getElement().getChild("description",ns);
		
		description = descElement.getValue();
		
		return description;
	}

	/**
	 * Return a list of references.
	 * 
	 * @return List<OvalReference>
	 */
	public List<OvalReference> getReferences()
	{
		List<OvalReference> refs = new ArrayList<OvalReference>();		
		
		List children = getElement().getChildren();
		if(children != null)
		{
			for(int x = 0; x < children.size();x++)
			{
				Element elem = (Element) children.get(x);

				if(elem.getName().toLowerCase().indexOf("reference") > -1)
				{
					OvalReference r = getParentDocument().getReferenceWrapper();
					r.setElement(elem);
					r.setRoot(getRoot());
					refs.add(r);
				}
			}
		}
		
		return refs;
	}

	/**
	 * Get the title.
	 * 
	 * @return String
	 */
	public String getTitle()
	{
		String title = null;
		
		Namespace ns = getElement().getNamespace();
		Element titleElement = getElement().getChild("title",ns);
		
		title = titleElement.getValue();
		
		return title;
	}

    /**
     * Get a list of affected item containers(affected tags).
     *
     * @return List<AffectedItemContainer>
     */
    public List<AffectedItemContainer> getAffected()
    {
            List<AffectedItemContainer> items = new ArrayList<AffectedItemContainer>();
            List children = getElement().getChildren();
            if(children != null)
            {
                    for(int x = 0; x < children.size();x++)
                    {
                            Element elem = (Element) children.get(x);

                            if(elem.getName().toLowerCase().indexOf("affected") > -1)
                            {
                                    AffectedItemContainer container = getParentDocument().getAffectedItemContainerWrapper();
                                    container.setElement(elem);
                                    container.setRoot(getRoot());

                                    items.add(container);
                            }
                    }
            }
            else
            {
                    return null;
            }

            return items;
    }

    /**
     * Add a new reference to this metadata.
     * @param ref
     * @return int
     */
    public int addReference(OvalReference ref)
    {
    	insertChild(ref, METADATA_ORDER, -1);

        return getElement().indexOf(ref.getElement());
    }

    /**
     * Add a list of references to this metadata.
     * 
     * @param newRefs
     * @return int
     */
    public int addReferences(List<OvalReference> newRefs)
    {
        int whereToInsert = -1;

        if(newRefs != null && newRefs.size() > 0)
        {
            for(OvalReference or : newRefs)
            {
                whereToInsert = addReference(or);
            }
        }

        return whereToInsert;
    }
	
	/**
	 * Add a new affected item container to this metadata.
	 * 
	 * @param container
	 * @return int
	 */
    public int addAffected(AffectedItemContainer container)
    {
    	insertChild(container, METADATA_ORDER, -1);
        return getElement().indexOf(container.getElement());
    }
    
    /**
     * Replace any existing affected elements with this affected element.
     * 
     * @param container
     */
    public void setAffected(AffectedItemContainer container) 
    {
    	this.element.removeChildren("affected", this.element.getNamespace());
    	addAffected(container);
    }

    /**
     * Add a list of affected item containers.
     * 
     * @param newContainers
     * @return int
     */
	public int addAffected(List<AffectedItemContainer> newContainers)
	{
		int whereToInsert = -1;

		if(newContainers != null && newContainers.size() > 0)
		{
			for(AffectedItemContainer aic : newContainers)
			{
				if(!containsAffected(aic.hashCode()))
				{
					if(whereToInsert == -1)
					{
						List children = getElement().getChildren();
						if(children != null)
						{
							for(int x = 0; x < children.size();x++)
							{
								Element elem = (Element) children.get(x);

								if(elem.getName().toLowerCase().indexOf("affected") > -1)
								{
									whereToInsert = x + 1;
									break;
								}
							}
						}
					}

                    if(aic.getElement().getParent() == null)
                    {
                        getElement().addContent(whereToInsert, (Element) aic.getElement());
                    }
                    else
                    {
                        getElement().addContent(whereToInsert, (Element) aic.getElement().clone());
                    }
				}
			}
		}

		return whereToInsert + 1;
	}

	/**
	 * Remove references with the supplied ids.
	 * 
	 * @param refIds
	 */
  	public void delReferences(List<String> refIds)
	{
		if(refIds != null && refIds.size() > 0)
		{
			for(String refId : refIds)
			{
                List children = getElement().getChildren();
                if (children != null)
                {
                    for (int x = 0; x < children.size(); x++)
                    {
                        Element elem = (Element) children.get(x);

                        if (elem.getName().toLowerCase().indexOf("reference") > -1)
                        {
                            if(refId.equals(elem.getAttributeValue("ref_id")))
                            {
                                elem.detach();
                                break;
                            }
                        }
                    }
                }
            }
		}
	}

  	/**
  	 * Tell us whether the supplied reference id exists in the metadata.
  	 * 
  	 * @param refId
  	 * @return boolean
  	 */
	public boolean containsReference(String refId)
	{
		boolean ret = false;
		
		List<OvalReference> refs = getReferences();
		
		if(refs != null && refs.size() > 0)
		{
			for(OvalReference or : refs)
			{
				if(or.getRefId().equals(refId))
				{
					ret = true;
					break;
				}
			}
		}
		
		return ret;
	}

	/**
	 * Tells whether an affected item exists and the supplied index.
	 * 
	 * @param affectedElementHashcode
	 * @return boolean
	 */
    public boolean containsAffected(int affectedElementHashcode)
	{
		boolean ret = false;

		List<AffectedItemContainer> aicList = getAffected();

		if(aicList != null && aicList.size() > 0)
		{
			for(AffectedItemContainer aic : aicList)
			{
				if(aic.getElement().hashCode() == affectedElementHashcode)
                {
                    ret = true;
                    break;
                }
			}
		}

		return ret;
	}

    /**
     * Return the contents of this metadata as a tree, for easy display in a gui.
     * 
     * @return DefaultMutableTreeNode
     */
    public DefaultMutableTreeNode asTree()
    {
        DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode(this.getElement());

        buildMetadataTree(getElement(), treeRoot);

        return treeRoot;
    }

    private void buildMetadataTree(Element currElement, DefaultMutableTreeNode node)
    {
        List children = currElement.getChildren();

        if(children != null && children.size() > 0)
        {
            for(int x = 0; x < children.size();x++)
            {
                Element child = (Element) children.get(x);

                StringBuilder elementPath = new StringBuilder();
                CommonUtil.getElementPath(child, elementPath);

                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
                node.add(childNode);
                childNode.setParent(node);

                List childChildren = child.getChildren();

                if(childChildren != null && childChildren.size() > 0)
                {
                    buildMetadataTree(child, childNode);
                }
            }
        }
    }

    @Override
    public String toString()
    {
        return "Metadata";
    }

    /**
     * Set the metadata's description.
     * 
     * @param description
     */
	public void setDescription(String description)
	{
		if(description != null)
		{
			Namespace ns = getElement().getNamespace();
			Element descElement = getElement().getChild("description",ns);
			if (descElement == null) {
				descElement = new Element("description", getElement().getNamespace());
				insertChild(descElement, METADATA_ORDER);
			}
			descElement.setText(description);	
		}
	}

	/**
	 * Set the metadata's title.
	 * 
	 * @param title
	 */
	public void setTitle(String title)
	{
		if(title != null)
		{
			Namespace ns = getElement().getNamespace();
			Element titleElement = getElement().getChild("title",ns);
			if (titleElement == null) {
				titleElement = new Element("title", ns);
				insertChild(titleElement, METADATA_ORDER);
			}
			titleElement.setText(title);
		}
	}
	
	/**
	 * Creates an AffectedItemContainer that can later be added.
	 * 
	 * @param parentDoc
	 * @return AffectedItemContainer
	 */
	public AffectedItemContainer createAffected(OvalDefinitionsDocument parentDoc)
	{
		AffectedItemContainer aic = parentDoc.getAffectedItemContainerWrapper();
		Element e = new Element("affected", getElement().getNamespace());
		
		aic.setElement(e);
		aic.setRoot(parentDoc.getElement());
		
		return aic;
	}

    /**
     * Creates an OvalReference that can later be added.
     *
     * @return OvalReference
     */
    public OvalReference createReference()
    {
       Element newElement = new Element(OvalReference.DEFAULT_ELEMENT_NAME_REFERENCE, getElement().getNamespace());

       OvalReference newRef = getParentDocument().getReferenceWrapper();
       newRef.setElement(newElement);
       newRef.setRoot(getRoot());

       return newRef;
    }

    /**
     * Remove an affected element that's child of this definition metadata.
     *
     * @param container An AffectedItemContainer
     */
    public void removeAffected(AffectedItemContainer container)
    {
        if(container != null)
        {
            getElement().removeContent(container.getElement());
        }
    }
}
