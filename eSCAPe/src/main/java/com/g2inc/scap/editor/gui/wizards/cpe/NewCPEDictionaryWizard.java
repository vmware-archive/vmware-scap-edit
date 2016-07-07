package com.g2inc.scap.editor.gui.wizards.cpe;
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
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.windows.EditorConfiguration;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.cpe.CPEItem;
import com.g2inc.scap.library.domain.cpe.CPEListGenerator;
import com.g2inc.scap.library.domain.cpe.CPESchemaVersion;

public class NewCPEDictionaryWizard extends Wizard
{
    private EditorMainWindow parentWin = null;
    private CPEDictionaryDocument dictDoc = null;
    private CPEItem item = null;

    private NewCPEDictionaryWizardPage page = null;

    private void addPages()
    {
        page = new NewCPEDictionaryWizardPage();        
        addWizardPage(page);
    }

    public NewCPEDictionaryWizard(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        setTitle("Create new " + EditorMessages.CPE + " Dictionary");
        parentWin = (EditorMainWindow) parent;
        
        addPages();
    }

    /**
     * Get the cpe dictionary document created by this wizard.
     * It will be null if there was an error.
     *
     * @return CPEDictionaryDocument
     */
    public CPEDictionaryDocument getCPEDictDoc()
    {
        return dictDoc;
    }

    public CPEItem getItem()
    {
        return item;
    }

    public EditorMainWindow getParentWin()
    {
        return parentWin;
    }

    public void setParentWin(EditorMainWindow parentWin)
    {
        this.parentWin = parentWin;
    }

    /**
     * This method gets called when the user clicks the finish button.
     */
    
    @Override
	public void performFinish()
    {
        SCAPDocument sd = null;
        Exception thrownException = null;

        try
        {
            sd = SCAPDocumentFactory.createNewDocument(page.getVersion());
        }
        catch(Exception e)
        {
            // unable to create a new document of that type, template missing???
            thrownException = e;
        }
        if(sd != null)
        {
        	String pageFN = page.getFilename();
        	
            sd.setFilename((new File(pageFN)).getAbsolutePath());

            try
            {
                dictDoc = (CPEDictionaryDocument) sd;

                CPEListGenerator generator = dictDoc.getGenerator();

                if(generator == null)
                {
                    generator = dictDoc.createGenerator();
                    dictDoc.setGenerator(generator);
                }

                generator.setProductName(EditorMessages.PRODUCT_NAME);
                generator.setProductVersion(EditorConfiguration.getInstance().getEditorVersion());
                
                Calendar c = Calendar.getInstance();
                c.setTime(new Date(System.currentTimeMillis()));

                generator.setTimestamp(c);

                // TODO: do this better
                if(dictDoc.getDocumentType().equals(SCAPDocumentTypeEnum.CPE_20))
                {
                    generator.setSchemaVersion(new CPESchemaVersion("2.0"));
                }
                else if(dictDoc.getDocumentType().equals(SCAPDocumentTypeEnum.CPE_21))
                {
                    generator.setSchemaVersion(new CPESchemaVersion("2.1"));
                }
                else if(dictDoc.getDocumentType().equals(SCAPDocumentTypeEnum.CPE_22))
                {
                    generator.setSchemaVersion(new CPESchemaVersion("2.2"));
                }
                else if(dictDoc.getDocumentType().equals(SCAPDocumentTypeEnum.CPE_23))
                {
                    generator.setSchemaVersion(new CPESchemaVersion("2.3"));
                }
                
                dictDoc.save();
            }
            catch(Exception e)
            {
                // unable to write the document out.  Permissions issue??
                thrownException = e;
            }
        }

        if(thrownException != null)
        {
            JOptionPane.showMessageDialog(EditorMainWindow.getInstance(),
                    "Unable to create new " + EditorMessages.CPE + " Dictionary document " + page.getFilename()
                        + ", an Exception occurred:\n" + thrownException.getMessage(),
                    "Unable to save file",
                    JOptionPane.ERROR_MESSAGE);
            sd = null;
        }
        else
        {
            // no exceptions have been thrown, go ahead and set the
        }
    }
}
