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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

import com.g2inc.scap.library.domain.SCAPElementImpl;

/**
 * Represents a cpe-item element.
 */
public abstract class CPEItem extends SCAPElementImpl
{
    
	public final static HashMap<String, Integer> CPE_ORDER = new HashMap<String, Integer>();	
	static
	{
		CPE_ORDER.put("title", 0);
		CPE_ORDER.put("notes", 1);
		CPE_ORDER.put("references", 2);
		CPE_ORDER.put("check", 3);
	}
	
	/**
	 * Default no args constructor.
	 */
	public CPEItem()
	{
		super();
	}
	
	public CPEItem(CPEDictionaryDocument cpeDoc) {
		setSCAPDocument(cpeDoc);
		setRoot(cpeDoc.getRoot());		
	}
	
	/**
	 * Set the name.
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		if(getElement() != null)
		{
            String currentName = getElement().getAttributeValue("name");

            if(currentName != null)
            {
                getParentDocument().updatedRenamedItem(currentName, name);
                getElement().setAttribute("name", name);
            }
            else
            {
                getElement().setAttribute("name", name);
            }
		}
	}
	
	/**
	 * Get the name.
	 * 
	 * @return String
	 */
	public String getName()
	{
		String ret = null;
		
		if(getElement() != null)
		{
			Attribute att = getElement().getAttribute("name");
			if(att != null)
			{
				ret = att.getValue();
			}
		}
		
		return ret;
	}
	
    /**
     * Set whether or not this item is deprecated
     *
     * @param deprecate
     */
    public void setDeprecated(boolean deprecate)
    {
        if (getElement() != null)
        {

            if(deprecate)
            {
                getElement().setAttribute("deprecated", deprecate + "");
            }
            else
            {
                getElement().removeAttribute("deprecated");
            }
        }
    }

    /**
     * Get whether or not this item is deprecated
     *
     * @return boolean
     */
    public boolean getDeprecated()
    {
        boolean ret = false;

        if (getElement() != null)
        {
            Attribute att = getElement().getAttribute("deprecated");
            if (att != null)
            {
                String retStr = att.getValue();
                if(retStr != null)
                {
                    try
                    {
                        ret = Boolean.parseBoolean(retStr);
                    }
                    catch(Exception e)
                    {
                        // the value couldn't be cast into a boolean
                        // but since something was in there we'll assume it was deprecated
                        ret = true;
                    }
                }
                else
                {
                    // this really shouldn't happen
                }
            }
        }

        return ret;
    }

    /**
	 * Get a list of the item's title elements.
	 * 
	 * @return List<CPEItemTitle>
	 */
	public List<CPEItemTitle> getTitles()
	{
		List<CPEItemTitle> ret = null;
	
		if(getElement() != null)
		{
			List children = getElement().getChildren();
			
			if(children != null && children.size() > 0)
			{
				for(int x = 0; x < children.size(); x++)
				{
					Object child = children.get(x);
					
					if(!(child instanceof Element))
					{
						continue;
					}
					
					Element childE = (Element) child;
					
					if(childE.getName() != null && childE.getName().equals("title"))
					{
						if(ret == null)
						{
							ret = new ArrayList<CPEItemTitle>();
						}
						
						CPEItemTitle title = getTitleWrapper();
						title.setElement(childE);
						title.setRoot(getRoot());
						title.setDoc(getDoc());
						title.setSCAPDocument(getSCAPDocument());
					
						ret.add(title);
					}
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Set the item title.  Removing any other title elements.
	 * 
	 * @param titleString
	 */
	public void setTitle(String titleString)
	{
		if(getElement() != null)
		{
			
			Element titleElement = (Element) getElement().getChild("title", getElement().getNamespace());
			
			while(titleElement != null)
			{
				getElement().removeContent(titleElement);
				
				titleElement = (Element) getElement().getChild("title", getElement().getNamespace());
			}

			// now create a new title Element
			CPEItemTitle title = createTitle();
			title.setText(titleString);
			
			addTitle(title);
		}		
	}

	/**
	 * Add a title element to this CPEItem.
	 * 
	 * @param title
	 */
	public void addTitle(CPEItemTitle title)
	{
		if(getElement() != null)
		{
			insertChild(title, CPE_ORDER, -1);
		}
	}
	
	/**
	 * Set a single item note inside a single notes element.  Removing any other notes elements.
	 * 
	 * @param notesString
	 */
	public void setNote(String notesString)
	{
		if(getElement() != null)
		{
			List<CPEItemNoteList> notesList = getNotes();			
			
			if(notesList != null)
			{
				for(int x = 0; x < notesList.size(); x++)
				{
					CPEItemNoteList list = notesList.get(x);
					
					getElement().removeContent(list.getElement());
				}
			}
			
			// now create a new notes Element
			CPEItemNoteList notes = createNoteList();
			CPEItemNote note = notes.createNote();
			
			note.setText(notesString);
			
			notes.addNote(note);
			
			addNoteList(notes);
		}		
	}

	/**
	 * Set a single item reference inside a single references element.  Removing any other references elements.
	 * 
	 * @param referenceHref A url or uri to set
	 */
	public void setReference(String referenceHref)
	{
		if(getElement() != null)
		{
			CPEItemReferenceList refsList = getReferenceList();			

			if(refsList != null)
			{
				getElement().removeContent(refsList.getElement());
			}
			
			// now create a new references Element
			CPEItemReferenceList refs = createReferenceList();
			CPEItemReference ref = refs.createReference();
			ref.setHref(referenceHref);

			refs.addReference(ref);
			
			addReferenceList(refs);
		}		
	}

	/**
	 * Add a notes element to this CPEItem.
	 * 
	 * @param notesContainer
	 */
	public void addNoteList(CPEItemNoteList notesContainer)
	{
		if(getElement() != null)
		{
			insertChild(notesContainer, CPE_ORDER, -1);
		}
	}

    /**
     * Remove a notes element from this CPEItem.
     * 
     * @param notesContainer
     */
    public void removeNoteList(CPEItemNoteList notesContainer)
    {
        if(getElement() != null)
        {
            if(notesContainer.getElement() != null)
            {
                getElement().removeContent(notesContainer.getElement());
            }
        }
    }

	/**
	 * Add a references element to this CPEItem, replacing an existing one if necessary.
	 * 
	 * @param references
	 */
	public void addReferenceList(CPEItemReferenceList references)
	{
		if(getElement() != null)
		{
            if(references.getElement() != null)
            {
    			insertChild(references, CPE_ORDER, -1);
            }
		}
	}

    /**
     * Remove a references element from this CPEItem.
     * 
     * @param references
     */
    public void removeReferenceList(CPEItemReferenceList references)
    {
        if(getElement() != null)
        {
            if(references.getElement() != null)
            {
                getElement().removeContent(references.getElement());
            }
        }
    }
	
	/**
	 * Get a list of the item's notes elements.
	 * 
	 * @return List<CPEItemNoteList>
	 */
	public List<CPEItemNoteList> getNotes()
	{		
		List<CPEItemNoteList> ret = null;
	
		if(getElement() != null)
		{
			List children = getElement().getChildren();
			
			if(children != null && children.size() > 0)
			{
				for(int x = 0; x < children.size(); x++)
				{
					Object child = children.get(x);
					
					if(!(child instanceof Element))
					{
						continue;
					}
					
					Element childE = (Element) child;
					
					if(childE.getName() != null && childE.getName().equals("notes"))
					{
						if(ret == null)
						{
							ret = new ArrayList<CPEItemNoteList>();
						}
						
						CPEItemNoteList noteList = getNoteListWrapper();
						noteList.setElement(childE);
						noteList.setRoot(getRoot());
						noteList.setDoc(getDoc());
						noteList.setSCAPDocument(getSCAPDocument());

                                                ret.add(noteList);
					}
				}
			}
		}
		
		return ret;
	}

	/**
	 * Get the item's references element.
	 * 
	 * @return CPEItemReferenceList
	 */
	public CPEItemReferenceList getReferenceList()
	{
		CPEItemReferenceList ret = null;
	
		if(getElement() != null)
		{
			List children = getElement().getChildren();
			
			if(children != null && children.size() > 0)
			{
				for(int x = 0; x < children.size(); x++)
				{
					Object child = children.get(x);
					
					if(!(child instanceof Element))
					{
						continue;
					}
					
					Element childE = (Element) child;
					
					if(childE.getName() != null && childE.getName().equals("references"))
					{						
						CPEItemReferenceList referenceList = getReferenceListWrapper();
						referenceList.setElement(childE);
						referenceList.setRoot(getRoot());
						referenceList.setDoc(getDoc());
						referenceList.setSCAPDocument(getSCAPDocument());
						
						ret = referenceList;
						break;
					}
				}
			}
		}
		
		return ret;
	}

	/**
	 * Get the checks for this item.
	 * 
	 * @return List<CPEItemCheck>
	 */
	public List<CPEItemCheck> getChecks()
	{
		List<CPEItemCheck> checks = null;
			
		if(getElement() != null)
		{
			List children = getElement().getChildren();
			
			if(children != null && children.size() > 0)
			{
				checks = new ArrayList<CPEItemCheck>();
				
				for(int x = 0; x < children.size() ; x++)
				{
					Object child = children.get(x);
					
					if(!(child instanceof Element))
					{
						continue;
					}
					
					Element childE = (Element) child;
					
					if(childE.getName().equals("check"))
					{
						CPEItemCheck check = getCheckWrapper();
						
						check.setElement(childE);
						check.setRoot(getRoot());
						check.setDoc(getDoc());
						check.setSCAPDocument(getSCAPDocument());
					
						checks.add(check);
					}
				}
			}
		}
		
		return checks;
	}

	/**
	 * Update check elements who have an href attribute matching the old value with the new supplied href.
	 * 
	 * @param oldHref Only check elements with an href attribute matching this href will be updated.
	 * @param newHref The href value that you want to replace the old value with.
	 */
	public void updateCheckHref(String oldHref, String newHref)
	{
		if(getElement() != null)
		{
			List children = getElement().getChildren();
			
			if(children != null && children.size() > 0)
			{
				for(int x = 0; x < children.size() ; x++)
				{
					Object child = children.get(x);
					
					if(!(child instanceof Element))
					{
						continue;
					}
					
					Element childE = (Element) child;
					
					if(childE.getName().equals("check"))
					{
						CPEItemCheck check = getCheckWrapper();
						
						check.setElement(childE);
						check.setRoot(getRoot());
						check.setDoc(getDoc());
						check.setSCAPDocument(getSCAPDocument());

						if(check.getHref() != null && check.getHref().equals(oldHref))
						{
							check.setHref(newHref);
						}
					}
				}
			}
		}		
	}

	/**
	 * Create and return a CPEItemCheck that can be added to this CPEItem.
	 * 
	 * @return CPEItemCheck
	 */
	public CPEItemCheck createCheck()
	{
		CPEItemCheck check = getCheckWrapper();
		Element checkElem = new Element("check", getElement().getNamespace());
		check.setElement(checkElem);
		check.setRoot(getRoot());
		check.setSCAPDocument(getSCAPDocument());
		check.setDoc(getDoc());

                check.setSystem(CPEItemCheckSystemType.OVAL5);
                
		return check;
	}
	
	
	/**
	 * Create and return a CPEItemTitle that can be added to this CPEItem.
	 * 
	 * @return CPEItemTitle
	 */
	public CPEItemTitle createTitle()
	{
		CPEItemTitle title = getTitleWrapper();
		Element checkElem = new Element("title", getElement().getNamespace());
		title.setElement(checkElem);
		title.setRoot(getRoot());
		title.setSCAPDocument(getSCAPDocument());
		title.setDoc(getDoc());
		
		return title;
	}
	
	/**
	 * Create and return a CPEItemNoteList that can be added to this CPEItem.
	 * 
	 * @return CPEItemNoteList
	 */
	public CPEItemNoteList createNoteList()
	{
		CPEItemNoteList notes = getNoteListWrapper();
		Element noteListElem = new Element("notes", getElement().getNamespace());
		notes.setElement(noteListElem);
		notes.setRoot(getRoot());
		notes.setSCAPDocument(getSCAPDocument());
		notes.setDoc(getDoc());
		
		return notes;
	}
	
	/**
	 * Create and return a CPEItemReferenceList that can be added to this CPEItem.
	 * 
	 * @return CPEItemReferenceList
	 */
	public CPEItemReferenceList createReferenceList()
	{
		CPEItemReferenceList notes = getReferenceListWrapper();
		Element checkElem = new Element("references", getElement().getNamespace());
		notes.setElement(checkElem);
		notes.setRoot(getRoot());
		notes.setSCAPDocument(getSCAPDocument());
		notes.setDoc(getDoc());
		
		return notes;
	}
	
	/**
	 * Add a check to this item.
	 * 
	 * @param check
	 */
	public void addCheck(CPEItemCheck check)
	{
		if(getElement() != null)
		{
			insertChild(check, CPE_ORDER, -1);
		}
	}
	
	/**
	 * Remove check.
	 * 
	 * @param check
	 */
	public void removeCheck(CPEItemCheck check)
	{
		if(getElement() != null)
		{
			if(check.getElement() != null)
			{
				getElement().removeContent(check.getElement());
			}
		}
	}
	
    /**
     * Remove an existing title from this cpe item
     * @param title
     */
    public void removeTitle(CPEItemTitle title)
    {
        if(getElement() != null)
        {
            if(title.getElement() != null)
            {
                getElement().removeContent(title.getElement());
            }
        }
    }

    /**
     * Get the CPEDictionaryDocument this item belongs to.
     * 
     * @return CPEDictionaryDocument
     */
    public CPEDictionaryDocument getParentDocument()
    {
        return (CPEDictionaryDocument) SCAPDocument;
    }


	/**
	 * Create a version specific check item wrapper.
	 * 
	 * @return CPEItemCheck
	 */
	public abstract CPEItemCheck getCheckWrapper();

	/**
	 * Create a version specific title wrapper.
	 *
	 * @return CPEItemTitle
	 */
	public abstract CPEItemTitle getTitleWrapper();

	/**
	 * Create a version specific notes wrapper.
	 *
	 * @return CPEItemNoteList
	 */
	public abstract CPEItemNoteList getNoteListWrapper();

	/**
	 * Create a version specific references wrapper.
	 *
	 * @return CPEItemReferenceList
	 */
	public abstract CPEItemReferenceList getReferenceListWrapper();

    /**
     * Returns true if the given string exists anywhere in the text
     * fields of the item.
     *
     * @param findString A string to find.
     * @return boolean
     */
    public boolean matches(String findString)
    {
        boolean ret = false;

        if(findString == null || findString.length() == 0)
        {
            // no find String was supplied so they must want everything
            return true;
        }

        String lcFindString = findString.toLowerCase();

        String name = getName();

        if(name != null)
        {
            if(name.toLowerCase().contains(lcFindString))
            {
                return true;
            }
        }

        // loop through titles
        List<CPEItemTitle> titles = getTitles();
        if(titles != null && titles.size() > 0)
        {
            for(int x = 0; x < titles.size(); x++)
            {
                CPEItemTitle title = titles.get(x);

                String text = title.getText();
                if(text != null && text.toLowerCase().contains(lcFindString))
                {
                    return true;
                }
            }
        }

        // look through notes
        List<CPEItemNoteList> noteLists = getNotes();
        if(noteLists != null && noteLists.size() > 0)
        {
            for(int x = 0; x < noteLists.size();x++)
            {
                CPEItemNoteList noteList = noteLists.get(x);

                List<CPEItemNote> notes = noteList.getNotes();
                if(notes != null && notes.size() > 0)
                {
                    for(int noteIdx = 0; noteIdx < notes.size(); noteIdx++)
                    {
                        CPEItemNote note = notes.get(noteIdx);

                        String text = note.getText();
                        if(text != null && text.toLowerCase().contains(lcFindString))
                        {
                            return true;
                        }
                    }
                }
            }
        }

        // look through references
        CPEItemReferenceList refList = getReferenceList();
        if(refList != null)
        {
            List<CPEItemReference> refs = refList.getReferences();
            if(refs != null && refs.size() > 0)
            {
                for(int refIdx = 0; refIdx < refs.size(); refIdx++)
                {
                    CPEItemReference ref = refs.get(refIdx);

                    String href = ref.getHref();
                    if(href != null && href.toLowerCase().contains(lcFindString))
                    {
                        return true;
                    }
                }
            }
        }

        // loop through checks
        List<CPEItemCheck> checks = getChecks();
        if(checks != null && checks.size() > 0)
        {
            for(int checkIdx = 0; checkIdx < checks.size(); checkIdx++)
            {
                CPEItemCheck check = checks.get(checkIdx);

                String href = check.getHref();
                if(href != null && href.toLowerCase().contains(lcFindString))
                {
                    return true;
                }

                String checkId = check.getCheckId();
                if(checkId != null && checkId.toLowerCase().contains(lcFindString))
                {
                    return true;
                }
            }
        }

        return ret;
    }
    
    @Override
    public String toString()
    {
    	return getName(); 
    }
    
    public boolean hasOval5Content() {
    	boolean result = false;
		List<CPEItemCheck> officialChecks = getChecks();
		for (CPEItemCheck itemCheck : officialChecks) {
			if (itemCheck.getSystem() != null
                    && itemCheck.getSystem().equals(
                            CPEItemCheckSystemType.OVAL5)) {
				result = true;
				break;
			}
		}
		return result;
    }

}
