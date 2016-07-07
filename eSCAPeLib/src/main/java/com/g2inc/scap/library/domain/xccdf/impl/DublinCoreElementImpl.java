package com.g2inc.scap.library.domain.xccdf.impl;

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

import org.jdom.Element;
import org.jdom.Namespace;

import com.g2inc.scap.library.domain.SupportedInputLanguages;
import com.g2inc.scap.library.domain.xccdf.DublinCoreElement;
import com.g2inc.scap.library.schema.SchemaLocator;

/**
 * Represents a specific element in the dublin core schema.
 *
 * @author ssill2
 */
public class DublinCoreElementImpl extends XmlLangImpl implements DublinCoreElement
{
   // public static final Namespace NS = Namespace.getNamespace("dc", SchemaLocator.DUBLIN_CORE_1_1_SCHEMA);
    
    public DublinCoreElementImpl() {
        super();
    }

    public DublinCoreElementImpl(String name)
    {	
        super();
        if (!isSupportedName(name))
        {
            throw new IllegalArgumentException("Supplied name is not a support element name for a Dublin Core element: " + name);
        }
     //   element = new Element(name, NS);
    }

    public DublinCoreElementImpl(Element e)
    {
        element = e;
    }

    /**
     * Get the element name.
     *
     * @return String
     */
    public String getName()
    {
        return element.getName();
    }

    /**
     * Set the name of this Dublin Core element.  The name must conform to one of the supported element
     * names in SchemaLocator.getSupportedDubinCoreElements();
     *
     * @param n
     */
    public void setName(String n)
    {
        if (!isSupportedName(n))
        {
            throw new IllegalArgumentException("Supplied name is not a support element name for a Dublin Core element: " + n);
        }

        element.setName(n);
    }

    /**
     * Check the supplied name against the list of supported element names;
     *
     * @param n A name to be checked.
     *
     * @return boolean
     */
    private boolean isSupportedName(String n)
    {
        boolean ret = false;

        ret = SchemaLocator.getInstance().getSupportedDubinCoreElements().contains(n);

        return ret;
    }

   /**
    * Set the xml:lang attribute.
    *
    * @param lang Language code.
    */
    @Override
    public void setLang(String lang)
    {
        if(lang != null && lang.trim().length() > 0)
        {
            element.setAttribute("lang", lang.trim());
        }
        else
        {
            element.removeAttribute("lang");
        }
    }

    /**
     * Return the value of the xml:lang attribute
     **/
    @Override
    public String getLang()
    {
        String lang = element.getAttributeValue("lang");

        if(lang == null)
        {
            lang = SupportedInputLanguages.getDefault();
        }
        return lang;
    }

    /**
     * Return the underlying JDOM element;
     * @return Element
     */
    public Element getElement()
    {
        return element;
    }

    @Override
    public String toString()
    {
        String elementTxt = element.getTextTrim();

        if(elementTxt == null || elementTxt.length() == 0)
        {
            elementTxt = "No Value Set";
        }
        
        return element.getName() + " - " + element.getText() ;
    }

    /**
     * Set the element text.
     *
     * @param text
     */
    public void setText(String text)
    {
        if(text != null && text.trim().length() > 0)
        {
            element.setText(text.trim());
        }
        else
        {
            element.setText(null);
        }
    }

    /**
     * Get the element text.
     * @return String
     */
    public String getText()
    {
        return getElement().getTextTrim();
    }
}
