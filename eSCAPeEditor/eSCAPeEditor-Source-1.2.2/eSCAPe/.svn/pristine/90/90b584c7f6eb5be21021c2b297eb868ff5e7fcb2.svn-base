package com.g2inc.scap.editor.gui.windows.wizardmode.wizards;
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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.Reader;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.oval.DefinitionClassEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalReference;

public class ReferencesCopyPasteTransferHandler extends TransferHandler
{
    private static Logger LOG = Logger.getLogger(ReferencesCopyPasteTransferHandler.class);

    private JList guiListComp = null;
    private OvalDefinitionsDocument ovalDocument = null;

    public ReferencesCopyPasteTransferHandler(JList list, OvalDefinitionsDocument odoc)
    {
        super();

        ovalDocument = odoc;
        guiListComp = list;
    }
    
    @Override
    public boolean canImport(TransferHandler.TransferSupport support)
    {
        DataFlavor[] flavors = support.getDataFlavors();

        if(flavors != null && flavors.length > 0)
        {
            DataFlavor bestTextFlavor = DataFlavor.selectBestTextFlavor(flavors);

            if(bestTextFlavor != null)
            {
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support)
    {
        DataFlavor[] flavors = support.getDataFlavors();

        if(flavors != null && flavors.length > 0)
        {
            DataFlavor bestTextFlavor = DataFlavor.selectBestTextFlavor(flavors);

            if(bestTextFlavor != null)
            {
                Transferable transferable = support.getTransferable();

                if(transferable != null)
                {
                    try
                    {
                        BufferedReader br = null;
                        Reader r = bestTextFlavor.getReaderForText(transferable);
                        br = new BufferedReader(r);

                        DefaultListModel model = (DefaultListModel) guiListComp.getModel();
                        OvalDefinition blankDef = ovalDocument.createDefinition(DefinitionClassEnum.PATCH);

                        String line = br.readLine();

                        while (line != null)
                        {
                            String[] parts = line.split(",");

                            if(parts != null)
                            {
                                if(parts.length > 0)
                                {
                                    OvalReference or = blankDef.getMetadata().createReference();

                                    or.setSource(parts[0].trim());

                                    if(parts.length > 1)
                                    {
                                        or.setRefId(parts[1]);
                                    }
                                    else
                                    {
                                        // not enough inf, skip this one.
                                        continue;
                                    }

                                    if(parts.length > 2)
                                    {
                                        or.setRefUrl(parts[2]);
                                    }

                                    if(!model.contains(or))
                                    {
                                        model.addElement(or);
                                    }
                                }
                            }

                            line = br.readLine();
                        }
                    }
                    catch(Exception e)
                    {
                        LOG.debug(bestTextFlavor.toString() + " is not a supported flavor", e);
                    }
                }
            }
        }

        return false;
    }

    @Override
    public int getSourceActions(JComponent c)
    {
        return COPY;
    }

    @Override
    public Transferable createTransferable(JComponent c)
    {
        StringSelection ss = null;

        DefaultListModel dlm = (DefaultListModel) guiListComp.getModel();

        StringBuilder sb = new StringBuilder();

        String eol = System.getProperty("line.separator");

        for(int x = 0; x < dlm.getSize(); x++)
        {
            OvalReference or = (OvalReference) dlm.get(x);
            sb.append(or.getSource() + ",");
            sb.append(or.getRefId());

            String href = or.getRefUrl();

            if(href != null && href.length() > 0)
            {
                sb.append("," + href + eol);
            }
            else
            {
                sb.append("," + eol);

            }
        }

        ss = new StringSelection(sb.toString());
        return ss;
    }
}
