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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.g2inc.scap.library.schema.SchemaLocator;

/**
 * A comparator that allows you to sort dublin core elements that are children of an xccdf reference element.
 *
 * @author ssill2
 */
public class ReferenceChildComparator<T> implements Comparator<T>
{
    private Map<String, Integer> elementOrder;

    public ReferenceChildComparator()
    {
        SchemaLocator sloc = SchemaLocator.getInstance();
        List<String> dcElements = sloc.getSupportedDubinCoreElements();

        elementOrder = new HashMap<String, Integer>(dcElements.size());

        int index = 0;

        for(String eName : dcElements)
        {
            elementOrder.put(eName, new Integer(index));
            index++;
        }
    }

    @Override
    public int compare(T o1, T o2)
    {
        if(o1 instanceof Element)
        {
            Element e1 = (Element) o1;
            Element e2 = (Element) o2;

            String e1Name = e1.getName();
            String e2Name = e2.getName();

            Integer e1Index = elementOrder.get(e1Name);
            Integer e2Index = elementOrder.get(e2Name);

            return e1Index.compareTo(e2Index);
        }
        else if(o1 instanceof DublinCoreElementImpl)
        {
            DublinCoreElementImpl e1 = (DublinCoreElementImpl) o1;
            DublinCoreElementImpl e2 = (DublinCoreElementImpl) o2;

            String e1Name = e1.getName();
            String e2Name = e2.getName();

            Integer e1Index = elementOrder.get(e1Name);
            Integer e2Index = elementOrder.get(e2Name);

            return e1Index.compareTo(e2Index);
        }
        else if(o1 instanceof String)
        {
            String e1Name = (String) o1;
            String e2Name = (String) o2;

            Integer e1Index = elementOrder.get(e1Name);
            Integer e2Index = elementOrder.get(e2Name);

            return e1Index.compareTo(e2Index);
        }

        return 0;
    }
}
