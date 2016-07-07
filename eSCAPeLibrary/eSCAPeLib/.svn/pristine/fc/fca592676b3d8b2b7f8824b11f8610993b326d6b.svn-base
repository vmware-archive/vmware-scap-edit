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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.xccdf.DublinCoreElement;
import com.g2inc.scap.library.domain.xccdf.Reference;
import com.g2inc.scap.library.schema.SchemaLocator;

public class ReferenceImpl extends SCAPElementImpl implements Reference
{
    private static final Logger LOG = Logger.getLogger(ReferenceImpl.class);

    public String getHref()
    {
        return this.element.getAttributeValue("href");
    }

    public void setHref(String href)
    {
        if(href != null && !href.trim().equals(""))
        {
            element.setAttribute("href", href);
        }
        else
        {
            element.removeAttribute("href");
        }
    }

    public boolean isOverride()
    {
        boolean override = false;
        String overrideString = this.element.getAttributeValue("override");
        if (overrideString == null)
        {
            override = false;
        }
        else
        {
            overrideString = overrideString.trim();
            override = overrideString.equalsIgnoreCase("true")
                       || overrideString.equals("1");
        }
        return override;
    }

    public void setOverride(boolean override)
    {
        element.setAttribute("override", Boolean.toString(override));
    }

    public String getReference()
    {
        String result = null;
        result = element.getText();
        return result;
    }

    /**
     * Set the text of this reference.
     *
     * @param referenceString
     */
    public void setReference(String referenceString)
    {
        if(referenceString != null && referenceString.length() > 0)
        {
            getElement().setText(referenceString);
        }
        else
        {
            getElement().setText(null);
        }
    }

    /**
     * Return a list of sources as defined in child <dc:source> elements.
     *
     * @return List<String>
     */
    @Deprecated
    public List<String> getSourceList()
    {
        List<String> ret = new ArrayList<String>();

        if (getElement() != null)
        {
            List childSources = getElement().getChildren(
                "source",
                Namespace.getNamespace("dc",
                                       SchemaLocator.DUBLIN_CORE_1_1_SCHEMA));
            if (childSources != null && childSources.size() > 0)
            {
                for (Object o : childSources)
                {
                    if (o instanceof Element)
                    {
                        Element e = (Element) o;

                        ret.add(e.getTextTrim());
                    }
                }
            }
        }

        return ret;
    }

    /**
     * Get a list of the dublin core elements in this reference;
     *
     * @return List<DublinCoreElement>
     */
    public List<DublinCoreElement> getDublinCoreElements()
    {
        List<DublinCoreElement> ret = null;

        List refContent = getElement().getChildren();

        if(refContent != null && refContent.size() > 0)
        {
            for(Object o : refContent)
            {
                if(o instanceof Element)
                {
                    Element dcElement = (Element) o;

                    if(DublinCoreElementImpl.NS.equals(dcElement.getNamespace()))
                    {
                        if(ret == null)
                        {
                            ret = new ArrayList<DublinCoreElement>();
                        }

                        DublinCoreElement dce = (DublinCoreElement) new DublinCoreElementImpl(dcElement);
                        ret.add(dce);
                    }
                }
            }
        }
        
        return ret;
    }

    /**
     * Set the list of dublin core elements for this reference.  Elements will be sorted
     * to ensure proper order according to the schema.
     *
     * @param elements
     */
    public void setDublinCoreElements(List<DublinCoreElement> elements)
    {
        List children = getElement().getContent();
        if(children != null && children.size() > 0)
        {
            List<Element> toRemove = new ArrayList<Element>(children.size());

            Iterator chItr = children.iterator();
            while(chItr.hasNext())
            {
                Object child = chItr.next();
                if(child instanceof Element)
                {
                    Element childE = (Element) child;

                    if(DublinCoreElementImpl.NS.equals(childE.getNamespace()))
                    {
                        toRemove.add(childE);
                    }
                }
            }

            if(toRemove.size() > 0)
            {
                for(Element c : toRemove)
                {
                    c.detach();
                }
            }
        }

        Collections.sort(elements, new ReferenceChildComparator<DublinCoreElement>());
        
        for(DublinCoreElement dce : elements)
        {
            getElement().addContent(dce.getElement());
        }
    }

    public void printCurrentDomState()
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
            xmlo.output(getElement(), baos);

            LOG.error("Reference element :\n" + baos.toString());
        }
        catch(Exception e)
        {
        }
    }

    @Override
    public String toString()
    {
        String result = "";
        String referenceString = getReference();
        if (referenceString != null && referenceString.trim().length() > 0)
        {
            result = truncate(referenceString);
        }
        else
        {
            List<DublinCoreElement> dcElements = getDublinCoreElements();

            if(dcElements != null && dcElements.size() > 0)
            {
                DublinCoreElement dce = dcElements.get(0);
                result = dce.toString();
            }
            else
            {
                result = "Empty";
            }
        }
        return result;
    }

    private String truncate(String in)
    {
        return (in.length() > 256 ? in.substring(0, 255) : in);
    }

}
