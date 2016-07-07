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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.g2inc.scap.library.domain.xccdf.HtmlText;
import java.util.Iterator;
import java.util.List;
import org.jdom.Attribute;

public abstract class HtmlTextImpl extends OverrideableSCAPElementImpl implements HtmlText
{

//    public static final String DOC_WRAPPER_START =
//                               "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
//                               + "<DocWrapper xmlns=\"http://checklists.nist.gov/xccdf/1.1\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" >\n";
//    

    public final String DOC_WRAPPER_END =
                               "</DocWrapper>";

    @Override
    public String getLang()
    {
        return this.element.getAttributeValue("lang", Namespace.XML_NAMESPACE);
    }

    @Override
    public void setLang(String lang)
    {
        if (lang != null && lang.trim().length() > 0)
        {
            element.setAttribute("lang", lang, Namespace.XML_NAMESPACE);
        }
        else
        {
            element.removeAttribute("lang", Namespace.XML_NAMESPACE);
        }
    }

    public void setElementFromStringWithHtml(String tagName, String string)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getDocWrapperStart());
        sb.append("<" + tagName + ">");
        sb.append(string);
        sb.append("</" + tagName + ">\n");
        sb.append(DOC_WRAPPER_END);
        SAXBuilder builder = new SAXBuilder();
        Document xmlDoc;
        ByteArrayInputStream bais;
        try
        {
            bais = new ByteArrayInputStream(sb.toString().getBytes("UTF8"));
            xmlDoc = builder.build(bais);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Can't parse tagName " + tagName + ", string:\n" + string, e);
        }
        Element wrapperRoot = xmlDoc.getRootElement();
        Element realElement = (Element) wrapperRoot.getChild(tagName, element.getNamespace()).detach();
        String lang = getLang();
        boolean override = isOverride();
        copyAttributes(this.element, realElement);
        this.element = realElement;
        if (lang != null)
        {
            setLang(lang);
        }
        if (override)
        {
            setOverride(override);
        }
    }
    
    /**
     * This function is necessary to fix a bug, in which all attributes disappear 
     * when the setElementFromStringWithHtml function is called. The FixText 
     * can have html, so it subclasses HtmlText, but when the set function mentioned 
     * above is called, the other subclass data (all in attributes) disappeared.
     * 
     * @param from
     * @param to 
     */
    private void copyAttributes(Element from, Element to) {
        List<Attribute> fromAttributes = from.getAttributes();
        Attribute[] fromAttributeArray = fromAttributes.toArray(new Attribute[0]);
        for (Attribute attribute : fromAttributeArray) {
            attribute.detach();
            to.setAttribute(attribute);
        }
    }
    
    private String getDocWrapperStart() {
    	return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<DocWrapper xmlns=\""
                + this.doc.getRootElement().getNamespaceURI() 
                + "\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" >\n";
    }

//    public String removeXhtmlPrefixes(String withPrefixes)
//    {
//        return withPrefixes.replaceAll("xhtml:", "");
//    }

    public String toStringWithHtml()
    {
        // TODO: if we ever find an example of use of <sub>, need to process sub
        // elements instead of using the approach below;  Code below will print out
        // all element content, including html tags and <sub> tags.
        String result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try
        {
            osw = new OutputStreamWriter(baos, Charset.forName("UTF-8"));
            bw = new BufferedWriter(osw);

            XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
            xmlo.outputElementContent(this.element, bw);

            bw.flush();
            bw.close();
            osw.close();
            baos.close();
            result = baos.toString("UTF-8");
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Error creating Element content string", e);
        }
        return result;
    }
}
