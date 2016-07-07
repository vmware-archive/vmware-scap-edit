package com.g2inc.scap.library.schema;
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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.g2inc.scap.library.parsers.ParserAbstract;

/**
 * This class does a minimal sax parse of a given schema(.xsd) file
 * and stores what targetNamespace it claims it is for.  All other elements
 * are ignored.
 */
public class SchemaLocatorParser extends ParserAbstract
{
    private boolean seenElement = false;
    private String  fileNSURI = null;

    /**
     * Get the targetNamespace we found.
     * 
     * @return String
     */
    public String getFileNSURI()
    {
        return fileNSURI;
    }

    /**
     * Set the uri that we think this file represents.
     * 
     * @param fileNSURI
     */
    public void setFileNSURI(String fileNSURI)
    {
        this.fileNSURI = fileNSURI;
    }

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException
    {
        if(!seenElement)
        {
            // this is the root element

            // get the targetNamespace attribute
            String targetNSURI = atts.getValue("targetNamespace");

            fileNSURI = targetNSURI;
            seenElement = true;
        }
        else
        {
            // we don't care about the rest of the document
        }
	}


}