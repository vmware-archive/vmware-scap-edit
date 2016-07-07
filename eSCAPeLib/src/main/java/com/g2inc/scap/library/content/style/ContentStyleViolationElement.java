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

/**
 * Represents a single violation of style in a document.
 *
 * @author ssill2
 */
public class ContentStyleViolationElement
{
    private String message = null;
    private ContentStyleViolationLevel level = ContentStyleViolationLevel.WARN;

    public ContentStyleViolationElement(ContentStyleViolationLevel lvl, String msg)
    {
        setLevel(lvl);
        setMessage(msg);
    }

    /**
     * Overriding default toString() method to something more meaningful.
     * @return String
     */
    @Override
    public String toString()
    {
        return level.toString() + " - " + message;
    }

    /**
     * Get the warning level of this violation element.
     * 
     * @return ContentStyleViolationLevel
     */
    public ContentStyleViolationLevel getLevel()
    {
        return level;
    }

    /**
     * Set the warning level of this violation element.
     * @param lvl
     */
    public void setLevel(ContentStyleViolationLevel lvl)
    {
        level = lvl;
    }

    /**
     * Get the warning message of this violation element.
     * @return
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Set the warning message of this violation element.
     * @param msg
     */
    public void setMessage(String msg)
    {
        message = msg;
    }
}