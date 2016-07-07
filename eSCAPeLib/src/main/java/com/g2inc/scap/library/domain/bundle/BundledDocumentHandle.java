package com.g2inc.scap.library.domain.bundle;
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

import java.io.File;
import java.io.IOException;

import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

/**
 * Holds a reference to the loaded document as well as any metadata about the document
 */
public abstract class BundledDocumentHandle
{
	protected SCAPDocument document = null;
    protected SCAPDocumentBundle bundle = null;
    
    public abstract SCAPDocument getDocument();
    
    public BundledDocumentHandle(SCAPDocument sd, SCAPDocumentBundle bundle)
    {
        document = sd;
        this.bundle = bundle;
    }
    
    /**
     * Unload the documents that are part of this bundle to free up resources.
     */
    public void unloadDocument()
    {
        document = null;
    }
    
    /**
     * Rename the SCAPDocument.
     * 
     * @param filename Destination filename
     * @throws IOException
     */
    public void rename(String filename) throws IOException
    {
		if(bundle.getBundleType() == SCAPBundleType.ZIP)
		{
			// if this document was loaded via a zip file
			document.setFilename(filename);
			
			// no files to rename on disk
			// the entry in the zip file will be named with the name we just set
		}
		else
		{
			// the file exists on disk and will need to be renamed
			File oldFile = new File(document.getFilename()).getAbsoluteFile();
			File newFile = new File(filename).getAbsoluteFile();
			
			oldFile.renameTo(newFile);
			
			document.setFilename(newFile.getAbsolutePath());
		}
    }
}
