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

import org.jdom.Element;
import org.jdom.Namespace;

import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.SupportedInputLanguages;

/**
 * Represents a notes element in a cpe-item.
 * 
 * @see CPEItem
 */
public abstract class CPEItemNoteList extends SCAPElementImpl
{
	public final static HashMap<String, Integer> CPE_ORDER = new HashMap<String, Integer>();	
	static
	{
		CPE_ORDER.put("note", 0);
	}

	public CPEItemNoteList()
	{
		super();
	}
	
	/**
	 * Create and return a CPEItemNote suitable for adding to this NoteList.
	 * 
	 * @return CPEItemNote
	 */
	public CPEItemNote createNote()
	{
		CPEItemNote note = getNoteWrapper();
		
		Element e = new Element("note", getElement().getNamespace());
		
		note.setElement(e);
		note.setRoot(getRoot());
		note.setDoc(getDoc());
		note.setSCAPDocument(getSCAPDocument());
		
		return note;
	}
	
	/**
	 * Add a created note to this notes element.
	 * 
	 * @param note
	 */
	public void addNote(CPEItemNote note)
	{
		if(getElement() != null)
		{
			insertChild(note, CPE_ORDER, -1);
		}
	}

    public void removeNote(CPEItemNote note)
    {
        if(getElement() != null)
        {
            if(note.getElement() != null)
            {
                getElement().removeContent(note.getElement());
            }
        }
    }

	/**
	 * Get a version specific wrapper for a note element.
	 * 
	 * @return CPEItemNote
	 */
	public abstract CPEItemNote getNoteWrapper();
	
	/**
	 * Get a list of note elements in this notes element.
	 * 
	 * @return List<CPEItemNote>
	 */
	public List<CPEItemNote> getNotes()
	{
		List<CPEItemNote> notes = null;
		
		if(getElement() != null)
		{
			List children = getElement().getChildren("note", getElement().getNamespace());
			
			if(children != null && children.size() > 0)
			{
				for(int x = 0; x < children.size(); x++)
				{
					Object child = children.get(x);
					
					if(!(child instanceof Element))
					{
						continue;
					}
					
					if(notes == null)
					{
						notes = new ArrayList<CPEItemNote>();
						
					}
					Element childE = (Element) child;
			
					CPEItemNote note = getNoteWrapper();
					
					note.setElement(childE);
					note.setRoot(getRoot());
					note.setDoc(getDoc());
					note.setSCAPDocument(getSCAPDocument());
					
					notes.add(note);
				}
			}
		}
		
		return notes;
	}

	/**
	 * Get the value of the lang attribute.
	 *
	 * @return String
	 */
	public String getLang()
	{
		String ret = SupportedInputLanguages.getDefault();

		if(getElement() != null)
		{
			Namespace ns = Namespace.getNamespace("xml", "http://www.w3.org/XML/1998/namespace");
			String xmlLang = getElement().getAttributeValue("lang", ns);

			if(xmlLang != null)
			{
				ret = xmlLang;
			}
		}

		return ret;
	}

	/**
	 * Set the value of the lang attribute.
	 *
	 * @param lang
	 */
	public void setLang(String lang)
	{
		if(getElement() != null)
		{
			Namespace ns = Namespace.getNamespace("xml", "http://www.w3.org/XML/1998/namespace");
			getElement().setAttribute("lang", lang, ns);
		}
	}

    @Override
    public String toString()
    {
        String ret = "(" + getLang() + ")";

        List<CPEItemNote> notes = getNotes();

        if(notes != null && notes.size() > 0)
        {
            String firstNoteText = notes.get(0).getText();

            if(firstNoteText != null)
            {
                if(firstNoteText.length() >= 47)
                {
                    firstNoteText = firstNoteText.substring(0,46) + "...";
                }
            }
            else
            {
                firstNoteText = "Not Set";
            }

            ret = ret + " " + firstNoteText;
        }
        
        return ret;
    }
}
