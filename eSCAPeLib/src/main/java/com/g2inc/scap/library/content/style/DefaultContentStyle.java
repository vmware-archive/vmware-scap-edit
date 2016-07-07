package com.g2inc.scap.library.content.style;

/* ESCAPE Software   Copyright 2010 G2, Inc. - All rights reserved.
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
import java.util.List;
import java.util.regex.Pattern;

import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

/**
 * This is the default style.  No specific stylistic elements will be checked.
 *
 * @author ssill2
 */
public class DefaultContentStyle extends ContentStyleAbstract
{
    public static final String NAME = "Default";
    public static final String OVAL_FILENAME_PATTERN_STRING = "^(?!-)(?!(.+[\\\\/]-)).+-(?:oval|patches|cpe-oval)\\.xml$";
    public static ContentStylePattern OVAL_FILENAME_PATTERN = new ContentStylePattern(Pattern.compile(OVAL_FILENAME_PATTERN_STRING)
        		, "Example valid filenames: mycontent-oval.xml, mycontent-cpe-oval.xml, or mycontent-patches.xml");
    

    public static final String XCCDF_FILENAME_PATTERN_STRING = "^(?i)(?!-)(?!(.+[\\\\/]-)).+-xccdf\\.xml$";
    public static ContentStylePattern XCCDF_FILENAME_PATTERN = new ContentStylePattern(Pattern.compile(XCCDF_FILENAME_PATTERN_STRING)
    			,"Example valid filename: mycontent-xccdf.xml");

    public static final String CPE_DICTIONARY_FILENAME_PATTERN_STRING = "^(?i)(?!-)(?!(.+[\\\\/]-)).+-cpe-dictionary\\.xml$";
    public static ContentStylePattern CPE_DICTIONARY_FILENAME_PATTERN = new ContentStylePattern(Pattern.compile(CPE_DICTIONARY_FILENAME_PATTERN_STRING)
    			,"Example valid filename: mycontent-cpe-dictionary.xml");

    public DefaultContentStyle()
    {
        setStyleName(NAME);
    }

    @Override
    protected List<ContentStyleViolationElement> handleOval(OvalDefinitionsDocument doc)
    {
        List<ContentStyleViolationElement> ret = new ArrayList<ContentStyleViolationElement>();

        //
        // Check for things about the filename
        handleDocFilename(doc, ret, OVAL_FILENAME_PATTERN);

        //
        // check things about the base id of the document
        ContentStyleViolationElement noBaseIdWarning = hasEmptyBaseId(doc);
        if(noBaseIdWarning != null)
        {
            ret.add(noBaseIdWarning);
        }

        return ret;
    }

    @Override
    protected List<ContentStyleViolationElement> handleXCCDF(XCCDFBenchmark doc)
    {
        List<ContentStyleViolationElement> ret = null;


        return ret;
    }

    @Override
    protected List<ContentStyleViolationElement> handleCPE(CPEDictionaryDocument doc)
    {
        List<ContentStyleViolationElement> ret = null;


        return ret;
    }
}
