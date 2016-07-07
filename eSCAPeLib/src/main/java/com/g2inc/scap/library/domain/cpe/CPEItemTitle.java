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

import org.jdom.Namespace;

import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.SupportedInputLanguages;

/**
 * Represents a title element in a cpe-item.
 * 
 * @see CPEItem
 */
public abstract class CPEItemTitle extends SCAPElementImpl
{

    protected CPEItem parentItem = null;

    public CPEItemTitle(CPEItem parent)
    {
        super();
        parentItem = parent;
    }

    /**
     * Get the CPEItem that is the parent of this title.
     * 
     * @return CPEItem
     */
    public CPEItem getParentItem()
    {
        return parentItem;
    }

    /**
     * Set the CPEItem that is a parent of this title.
     * 
     * @param parentItem
     *            The CPEItem that this title belongs to
     */
    public void setParentItem(CPEItem parentItem)
    {
        this.parentItem = parentItem;
    }

    /**
     * Get the text associated with this title element.
     * 
     * @return String
     */
    public String getText()
    {
        String ret = null;

        if (getElement() != null)
        {
            ret = getElement().getText();
        }

        return ret;
    }

    /**
     * Set the text associated with this title element.
     * 
     * @param text
     *            The text to be associated with this element.
     */
    public void setText(String text)
    {
        if (getElement() != null)
        {
            getElement().setText(text);
        }
    }

    /**
     * Get the value of the lang attribute.
     * 
     * @return String
     */
    public String getLang()
    {
        String ret = SupportedInputLanguages.getDefault();

        if (getElement() != null)
        {
            Namespace ns = Namespace.getNamespace("xml",
                    "http://www.w3.org/XML/1998/namespace");
            String xmlLang = getElement().getAttributeValue("lang", ns);

            if (xmlLang != null)
            {
                ret = xmlLang;
            }
        }

        return ret;
    }

    /**
     * Set the value of the xml:lang attribute.
     * 
     * @param lang
     *            The language code of the language this title is in.
     */
    public void setLang(String lang)
    {
        if (getElement() != null)
        {
            Namespace ns = Namespace.getNamespace("xml",
                    "http://www.w3.org/XML/1998/namespace");
            getElement().setAttribute("lang", lang, ns);
        }
    }

    @Override
    public String toString()
    {
        String ret = null;

        String lang = getLang();
        String langString = "unknown";

        if (lang != null)
        {
            langString = lang;
        }
        ret = "(" + langString + ") " + getText();

        return ret;
    }

    @Override
    public int hashCode()
    {
        int ret = 0;

        String text = getText();
        if (text != null)
        {
            ret += text.hashCode();
        }

        String lang = getLang();
        if (lang != null)
        {
            ret += lang.hashCode();
        }

        return ret;
    }

    @Override
    public boolean equals(Object o)
    {
        boolean ret = false;

        if (o == null)
        {
            return ret;
        }

        if (o instanceof CPEItemTitle)
        {
            CPEItemTitle other = (CPEItemTitle) o;

            String thisText = getText();
            if (thisText == null)
            {
                if (other.getText() != null)
                {
                    // this text is null and the other's isn't.
                    // these are not equal
                    return ret;
                }
                else
                {
                    // both texts are null see if the lang attribute is
                    // the same.

                    if (getLang().equals(other.getLang()))
                    {
                        // both have null texts but the lang is the same
                        // they are talking about the same title so they are
                        // equal.
                        ret = true;
                        return ret;
                    }
                    else
                    {
                        // languages are not the same
                        return ret;
                    }
                }
            }
            else
            {
                // this title's text is not null
                String lcThisText = thisText.toLowerCase();
                if (lcThisText.equalsIgnoreCase(other.getText()))
                {
                    // compare langs
                    if (getLang().equals(other.getLang()))
                    {
                        ret = true;
                        return ret;
                    }
                }
            }
        }

        return ret;
    }
}