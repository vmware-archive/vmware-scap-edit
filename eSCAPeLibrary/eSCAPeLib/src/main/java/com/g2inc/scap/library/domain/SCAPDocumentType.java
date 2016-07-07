package com.g2inc.scap.library.domain;
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

import com.g2inc.scap.library.content.style.ContentStyle;
import com.g2inc.scap.library.domain.bundle.SCAPDocumentBundle;

/**
 * This class represents any document that is loadable into the
 * SCAPContentManager.
 *
 * @author ssill2
 * @see com.g2inc.scap.library.domain.SCAPContentManager
 */
public interface SCAPDocumentType extends SCAPElement
{

  public ContentStyle getContentStyle();
  public void setContentStyle(ContentStyle contentStyle);


  /**
   * Returns the type of this document.
   *
   * @return SCAPDocumentTypeEnum
   */
  public SCAPDocumentTypeEnum getDocumentType();

  /**
   * Sets the type of this document.
   *
   * @param documentType The type of this document
   */
  public void setDocumentType(SCAPDocumentTypeEnum documentType);

  /**
   * Get the path of this document on disk.
   * 
   * @return String
   */
  public String getFilename();

  /**
   * Sets the path of this document on disk.
   *
   * @param filename Absolute path of this document on disk.
   */
  public void setFilename(String filename);


  /**
   * Saves this document to a temporary location on disk and tries to do a
   * validating parse on it.  This will return any validation errors it finds.
   *
   * @return String
   */
  public String validate();
  
  /**
   * This method when implemented by subclasses should do a more document type specific check than
   * a normal xml schema validation will do.  Checking things like making sure references to other parts of the
   * same document or references to other documents can be done when an implementation of this method.
   * 
   * @return String
   * @throws Exception
   */
  public abstract String validateSymantically() throws Exception;
  
  /**
   * Save the document to the disk using the current filename.
   * 
   * @throws IOException
   */
  public void save() throws IOException;

  /**
   * Save this document as another name on disk.
   * 
   * @param filename
   * @throws IOException
   */
  public void saveAs(String filename) throws IOException;

  /**
   * Save this document as another name on disk.
   * 
   * @param file A File object representing the file we want to save to
   * 
   * @throws IOException
   */
  public void saveAs(File file) throws IOException;
  /**
    * Private class to be used as a ContentHandler for a sax parse.  It really
    * is does nothing with the elements, it's needed to make sure that document
    * validates correctly. 
    *
  */
  ;
  
  /**
   * Return the class of document this is.
   * 
   */
  public SCAPDocumentClassEnum getDocumentClass();
  
  /**
   * Set the class of document this is.
   * 
   * @param docClass
   */
  public void setDocumentClass(SCAPDocumentClassEnum docClass);
  /**
   * Can be overriden by subclasses to do some clean up before the document is removed from memory.
   */
  public void tearDown();
  /**
   * Get the bundle this document is part of.  Will be null if it has been opened
   * as a stand-alone document.
   * 
   * @return SCAPDocumentBundle
   */
  public SCAPDocumentBundle getBundle();

  /**
   * This will be called by the SCAPBundle parse code to let this document know
   * that it belongs to that bundle.
   * 
   * @param bundle A reference to the bundle to which this document belongs
   */
  public void setBundle(SCAPDocumentBundle bundle);
  /**
   * Get the dirty flag for this document.
   * 
   * @return boolean
   */
  public boolean isDirty();
  
  /**
   * Set the dirty flag for this document.
   * 
   * @param b Whether or not this document is dirty.
   */
  public void setDirty(boolean b);
  
  /**
   * Method that should be called to clear out any collections or references
   *  that might prevent garbage collection.
   * 
   */
  public abstract void close();
  
  public Class<? extends SCAPElementImpl> getImplClass(Class<? extends SCAPElement> intClass);
  
}
