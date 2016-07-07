package com.g2inc.scap.library.domain.xccdf.impl.XCCDF12;
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
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.xccdf.DcStatus;
import com.g2inc.scap.library.domain.xccdf.DublinCoreElement;
import com.g2inc.scap.library.domain.xccdf.impl.DublinCoreElementImpl;
import com.g2inc.scap.library.domain.xccdf.impl.ReferenceChildComparator;

public class DcStatusImpl extends SCAPElementImpl implements DcStatus
{
    private static final Logger LOG = Logger.getLogger(DcStatusImpl.class);

    /**
     * Get a list of the dublin core elements in this dc-status;
     *
     * @return List<DublinCoreElement>
     */
    @Override
    public List<DublinCoreElement> getDublinCoreElements() {
        List<DublinCoreElement> ret = new ArrayList<DublinCoreElement>();
        List<Element> refContent = getElement().getChildren();

        for (Element dcElement : refContent) {
            /*if (DublinCoreElementImpl.NS.equals(dcElement.getNamespace())) {
                DublinCoreElement dce = new DublinCoreElementImpl(dcElement);
                ret.add(dce);
            }*/
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
      //  LOG.debug("DcStatusImpl setDublinCoreElements with list of " + (elements == null ? "null" : elements.size()) + " dublincore elements");
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

                   /* if(DublinCoreElementImpl.NS.equals(childE.getNamespace()))
                    {
                        toRemove.add(childE);
                    }*/
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
         //   LOG.debug("DcStatusImpl setDublinCoreElements adding element " + dce.getElementName());
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
        List<DublinCoreElement> dcElements = getDublinCoreElements();
        String conjunction = "";
        for (DublinCoreElement dcElement : dcElements) {
            result += conjunction;
            result += dcElement.toString();
            conjunction = ", ";
        }
        if (result.equals("")) {
            result = "Empty";
        }
        return result;
    }


}
