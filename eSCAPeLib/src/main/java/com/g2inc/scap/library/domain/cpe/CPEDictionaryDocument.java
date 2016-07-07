package com.g2inc.scap.library.domain.cpe;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;

import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentClassEnum;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;

/**
 * Defines what an CPE Document looks like as a SCAPDocument.
 * 
 * @author ssill2
 * @see com.g2inc.scap.library.domain.SCAPDocument
 */
public abstract class CPEDictionaryDocument extends SCAPDocument
{
    private static Logger log = Logger.getLogger(CPEDictionaryDocument.class);
    public final static HashMap<String, Integer> CPE_ORDER = new HashMap<String, Integer>();

    static
    {
        CPE_ORDER.put("generator", 0);
        CPE_ORDER.put("cpe-item", 1);
    }

    private Map<String, CPEItem> itemsMap = null;
    private HashMap<CPEItemTitle, List<WeakReference<CPEItem>>> titlesMap = new HashMap<CPEItemTitle, List<WeakReference<CPEItem>>>();

    public static final String CPE_ITEM_REGEX = "[c][pP][eE]:/[AHOaho]?(:[A-Za-z0-9\\._\\-~%]*){0,6}";
    public static Pattern CPE_ITEM_NAME_PATTERN;

    {
        try
        {
            CPE_ITEM_NAME_PATTERN = Pattern.compile(CPE_ITEM_REGEX);
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Error compiling pattern "
                    + CPE_ITEM_REGEX, e);
        }
    }

    public CPEDictionaryDocument(Document doc)
    {
        super(doc);
        setDocumentClass(SCAPDocumentClassEnum.CPE_DICTIONARY);
        itemsMap = new LinkedHashMap<String, CPEItem>();
    }
    
    public void refreshItemsMap() {
    	itemsMap.clear();
    	@SuppressWarnings("unchecked")
    	List<Element> children = this.element.getChildren();
    	for (Element elem : children) {		
    		if (elem.getName().equals("cpe-item")) {
	    		CPEItem cpeItem = this.getItemWrapper();
	    		cpeItem.setElement(elem);
	    		itemsMap.put(cpeItem.getName(), cpeItem);
    		}
    	}
    }

    /**
     * Return a List of CPEItem objects for this document.
     * 
     * @return List<CPEItem>
     */
    public List<CPEItem> getItems()
    {
    	//    	startTimer(CollType.CPE_ITEMS);
    	List<CPEItem> items = new ArrayList<CPEItem>();
    	Iterator<CPEItem> iter = itemsMap.values().iterator();
    	while (iter.hasNext()) {
    		items.add(iter.next());
    	}
    	//       stopTimer(CollType.CPE_ITEMS);
    	return items;
    }

    /**
     * Return whether or not this document contains a CPE item with the given
     * name attribute.
     */
    public boolean containsItem(String itemName)
    {
        return itemsMap.containsKey(itemName);
    }

    /**
     * Create an instance of the correct wrapper class for the underlying
     * cpe-item element. This must be implemented in the implemenations of this
     * class
     */
    public abstract CPEItem getItemWrapper();

    /**
     * Create a new CPEItem suitable for adding to the document later.
     * 
     */
    public CPEItem createItem()
    {
        CPEItem item = getItemWrapper();
        Element newElem = new Element("cpe-item", getElement().getNamespace());

        item.setElement(newElem);
        item.setRoot(getRoot());
        item.setDoc(getDoc());
        item.setSCAPDocument(getSCAPDocument());

        return item;
    }

    /**
     * Add an item to this document.
     * 
     * @param item
     */
    public void addItem(CPEItem item)
    {
        if (!containsItem(item.getName()))
        {
            Element newItemElement = null;
            CPEItem itemToAdd = null;

            if (item.getElement().getParent() != null)
            {
                newItemElement = (Element) item.getElement().clone();
                itemToAdd = getItemWrapper();
                itemToAdd.setElement(newItemElement);

                getElement().addContent((Element) newItemElement);
            }
            else
            {
                itemToAdd = item;
                newItemElement = item.getElement();
                getElement().addContent(newItemElement);
            }
            itemsMap.put(item.getName(), itemToAdd);
        }
        else
        {
            if (log.isDebugEnabled())
            {
             //   log.debug("CPE dictionary " + getFilename()
               //         + " already contains the cpe item " + item.getName());
            }
        }
    }
    
    
//    public Set<String> findItemsWithCheckContentByTitle(String title)
//    {
//        Set<String> found = new HashSet<String>();
//
//        CPEItemTitle key = createItem().createTitle();
//        key.setText(title);
//
//        List<WeakReference<CPEItem>> itemsForTitle = titlesMap.get(key);
//
//        if (itemsForTitle != null && itemsForTitle.size() > 0)
//        {
//            for (int x = 0; x < itemsForTitle.size(); x++)
//            {
//                WeakReference<CPEItem> itemRef = itemsForTitle.get(x);
//
//                if (itemRef.get() == null)
//                {
//                    log.info("itemRef.get() returned null");
//                    continue;
//                }
//
//                CPEItem item = itemRef.get();
//
//                if (item.getDeprecated())
//                {
//                    continue;
//                }
//
//                String itemName = item.getName();
//
//                // check that the item has check content
//
//                List<CPEItemCheck> officialChecks = item.getChecks();
//
//                if (officialChecks != null && officialChecks.size() > 0)
//                {
//                    for (int chkIndex = 0; chkIndex < officialChecks.size(); x++)
//                    {
//                        CPEItemCheck officialCheck = officialChecks.get(chkIndex);
//
//                        if (officialCheck.getSystem() != null
//                                && officialCheck.getSystem().equals(
//                                        CPEItemCheckSystemType.OVAL5))
//                        {
//                            // this item has check content, add it
//                            found.add(itemName);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//        return found;
//    }

    /**
     * Return a list of item names(CPEs) by checking if the title matches the
     * supplied one. Only cpe names for items with check content will be
     * returned
     * 
     * @param title
     *            The title to match
     * 
     *            Note: for now we only support en-US titles
     * 
     * @return Set<String>
     */
    public Set<String> findItemsWithCheckContentByTitle(String title)
    {
        Set<String> found = new HashSet<String>();

        CPEItemTitle key = createItem().createTitle();
        key.setText(title);
        
        List<CPEItem> allItems = getItems();
        for (CPEItem item : allItems) {
        	if (!item.getDeprecated()) {
        		List<CPEItemTitle> itemTitles = item.getTitles();
	        	for (CPEItemTitle itemTitle : itemTitles) {
	        		String itemTitleText = itemTitle.getText();
	        		if (itemTitleText != null && itemTitleText.equals(title)) {
	        			// this is a candidate, make sure it has an oval check-content
	        			if (item.hasOval5Content()) {
	        				found.add(item.getName());
	        			}
	        		}
	        	}
        	}
        }
        return found;
    }
    
    public Set<String> getAllCheckContentHrefs() {
    	Set<String> found = new HashSet<String>();
        List<CPEItem> allItems = getItems();
        for (CPEItem item : allItems) {
        	if (!item.getDeprecated()) {
        		List<CPEItemCheck> checks = item.getChecks();
        		for (CPEItemCheck check : checks) {
        			String href = check.getHref();
        			if (href != null) {
        				found.add(href);
        			}
        		}
        	}
        }
    	return found;
    }

    /**
     * Get an existing item by name. Returns a list just in case there are
     * duplicate entries. A list of more than one means there is a duplicate.
     * 
     * @param name
     * @return List<CPEItem>
     */
    public List<CPEItem> getItem(String name)
    {
        List<CPEItem> items = new ArrayList<CPEItem>();
        if (containsItem(name))
        {
            items.add(itemsMap.get(name));
        }
        return items;
    }

    /**
     * Remove a cpe item by name.
     * 
     * @param name
     */
    public void remove(String name)
    {
        if (itemsMap.containsKey(name))
        {
            // get the item.
            List<CPEItem> itemList = getItem(name);

            if (itemList != null && itemList.size() > 0)
            {
                for (int x = 0; x < itemList.size(); x++)
                {
                    CPEItem item = itemList.get(x);

                    Element parent = item.getElement().getParentElement();

                    if (parent != null)
                    {
                        parent.removeContent(item.getElement());
                    }

                    item.setElement(null);
                    item.setRoot(null);
                    item.setDoc(null);
                    item.setSCAPDocument(null);

                    // remove entry in set
                    itemsMap.remove(name);
                }

                itemList.clear();
            }
            else
            {
                // item doesn't actually exist in the document
                // remove entry in set
                itemsMap.remove(name);
            }

        }
    }

    /**
     * Remove a cpe item.
     * 
     * @param item
     */
    public void remove(CPEItem item)
    {
        if (getElement() != null)
        {
            if (item.getElement() != null)
            {
                itemsMap.remove(item.getName());

                getElement().removeContent(item.getElement());
            }
        }
    }

    /**
     * Create an instance of the correct wrapper class for the underlying
     * generator element. This must be implemented in the implemenations of this
     * class
     */
    public abstract CPEListGenerator getListGeneratorWrapper();

    /**
     * Get the generator of this document.
     * 
     * @return CPEListGenerator
     */
    public CPEListGenerator getGenerator()
    {
        CPEListGenerator generator = null;

        if (getElement() != null)
        {
            Element e = getElement().getChild("generator",
                    getElement().getNamespace());

            if (e != null)
            {
                generator = getListGeneratorWrapper();
                generator.setElement(e);
                generator.setRoot(getRoot());
                generator.setDoc(getDoc());
                generator.setSCAPDocument(getSCAPDocument());
            }
        }

        return generator;
    }

    /**
     * Remove the generator element.
     */
    public void removeGenerator()
    {
        if (getElement() != null)
        {
            Element e = getElement().getChild("generator");

            if (e != null)
            {
                getElement().removeContent(e);
            }
        }
    }

    /**
     * Set the generator.
     * 
     * @param generator
     */
    public void setGenerator(CPEListGenerator generator)
    {
        removeGenerator();

        if (getElement() != null)
        {
            insertChild(generator, CPE_ORDER, -1);
        }
    }

    /**
     * Create a generator suitable for adding to this document.
     * 
     * @return CPEListGenerator
     */
    public CPEListGenerator createGenerator()
    {
        CPEListGenerator generator = getListGeneratorWrapper();

        Element e = new Element("generator", getElement().getNamespace());

        generator.setElement(e);
        generator.setRoot(getRoot());
        generator.setDoc(getDoc());
        generator.setSCAPDocument(getSCAPDocument());

        return generator;
    }

    @Override
    public String toString()
    {
        CPEListGenerator generator = getGenerator();

        String version = null;

        if (generator != null)
        {
            version = generator.getSchemaVersion().getVersion();
        }
        else
        {
            SCAPDocumentTypeEnum type = getDocumentType();

            String str = type.getPrimarySchemaFile();

            version = str.substring(str.lastIndexOf("\\") + 1);
        }

        return "CPE " + version + " Dictionary Document(" + getFilename() + ")";
    }

    @Override
    public String validateSymantically() throws Exception
    {
        String ret = null;
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        List<CPEItem> items = getItems();

        if (items != null && items.size() > 0)
        {
            for (int x = 0; x < items.size(); x++)
            {
                CPEItem item = items.get(x);

                List<CPEItemCheck> checks = item.getChecks();

                if (checks != null && checks.size() > 0)
                {
                    for (int y = 0; y < checks.size(); y++)
                    {
                        CPEItemCheck check = checks.get(y);

                        if (check.getSystem().equals(
                                CPEItemCheckSystemType.OVAL5))
                        {
                            String ovalFilename = check.getHref();

                            if (ovalFilename != null)
                            {
                                if (getBundle() != null)
                                {
                                    OvalDefinitionsDocument referencedDocument = getBundle().getOvalDocumentByName(
                                            ovalFilename);

                                    if (referencedDocument != null)
                                    {
                                        String defId = check.getCheckId();

                                        if (defId != null)
                                        {
                                            if (!referencedDocument.containsDefinition(defId))
                                            {
                                                sb.append("CPEItem "
                                                        + item.getName()
                                                        + " has a check element who's href points to"
                                                        + " a valid document "
                                                        + ovalFilename
                                                        + " but the definition id specified doesn't exist: "
                                                        + defId + newLine);
                                            }
                                        }
                                        else
                                        {
                                            sb.append("CPEItem "
                                                    + item.getName()
                                                    + " has a check element who's href points to"
                                                    + " a valid document "
                                                    + ovalFilename
                                                    + " but has no id specified for a definition within that file."
                                                    + newLine);
                                        }
                                    }
                                    else
                                    {
                                        sb.append("CPEItem "
                                                + item.getName()
                                                + " has a check element who's href points to"
                                                + " a document that is non-existent, or not part of this bundle: "
                                                + ovalFilename + newLine);
                                    }
                                }
                                else
                                {
                                    log.warn("Unable to verify href for item "
                                            + item.getName()
                                            + " because this"
                                            + " document was not loaded as part of a bundle.");
                                }
                            }
                            else
                            {
                                sb.append("CPEItem "
                                        + item.getName()
                                        + " has a check element who's href is null!"
                                        + newLine);
                            }
                        }
                    }
                }
            }
        }

        if (sb.length() > 0)
        {
            ret = sb.toString();
        }
        return ret;
    }

    /**
     * This method should be called after you rename a cpe item so existence
     * checks will be able to find the item under it's new name.
     * 
     * @param oldName
     * @param newName
     */
    public void updatedRenamedItem(String oldName, String newName)
    {
        if (itemsMap.containsKey(oldName))
        {
            CPEItem item = itemsMap.remove(oldName);
            item.setName(newName);
            itemsMap.put(newName, item);
        }
    }

    @Override
    protected void finalize()
    {
        close();
        itemsMap = null;
        setElement(null);
        setDoc(null);
    }

    @Override
    public void close()
    {
        itemsMap.clear();
    }
}
