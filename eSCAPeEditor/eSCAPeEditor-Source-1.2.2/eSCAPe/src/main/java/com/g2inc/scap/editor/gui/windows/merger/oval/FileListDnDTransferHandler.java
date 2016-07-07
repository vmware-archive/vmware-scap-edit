package com.g2inc.scap.editor.gui.windows.merger.oval;
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
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.windows.merger.common.MergerSourceFile;

public class FileListDnDTransferHandler extends TransferHandler
{
    private static Logger log = Logger.getLogger(FileListDnDTransferHandler.class);
    private static final String listMimeType = "application/x-java-file-list";

    private OvalMergerGui parentApp = null;

    private JList guiListComp = null;

    public FileListDnDTransferHandler(OvalMergerGui pa, JList list)
    {
        super();

        parentApp = pa;
        guiListComp = list;
    }
    
    @Override
    public boolean canImport(TransferHandler.TransferSupport support)
    {
        DataFlavor[] flavors = support.getDataFlavors();

        DataFlavor listFlavor = null;

        if(flavors != null & flavors.length > 0)
        {
            for(int x = 0; x < flavors.length; x++)
            {
                DataFlavor df = flavors[x];

                if(df.isMimeTypeEqual(listMimeType))
                {
                    // this is the one we want
                    listFlavor = df;
                    break;
                }
            }

            if(listFlavor == null)
            {
                log.error("no list data flavor found, unable to import");
                return false;
            }

            Transferable trans = support.getTransferable();

            if(trans == null)
            {
                log.error("transferable is null, unable to import");
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            log.error("flavors array is null or empty!");
            return false;
        }        
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support)
    {
        DataFlavor[] flavors = support.getDataFlavors();

        DataFlavor listFlavor = null;

        if(flavors != null & flavors.length > 0)
        {
            for(int x = 0; x < flavors.length; x++)
            {
                DataFlavor df = flavors[x];

                if(df.isMimeTypeEqual(listMimeType))
                {
                    // this is the one we want
                    listFlavor = df;
                    break;
                }
            }

            if(listFlavor == null)
            {
                log.error("no list data flavor found, unable to import");
                return false;
            }

            Transferable trans = support.getTransferable();

            if(trans == null)
            {
                log.error("transferable is null, unable to import");
                return false;
            }

            Object o = null;

            try
            {
                o = trans.getTransferData(listFlavor);
            }
            catch(Exception e)
            {
                log.error("error getting transfer data", e);
                return false;
            }

            if(o == null)
            {
                log.error("Transfer data is null!");
                return false;
            }

            List<File> files = null;

            try
            {
                files = (List<File>) o;
            }
            catch(Exception e)
            {
                log.error("Unable to cast transfer data into list of files", e);
                return false;
            }

            DefaultListModel listModel = (DefaultListModel) guiListComp.getModel();


            if(files != null && files.size() > 0)
            {
                boolean changed = false;
                for(int x = 0; x < files.size(); x++)
                {
                    File f = files.get(x);

                    MergerSourceFile vsf = new MergerSourceFile(f.getAbsolutePath());

                    if(!listModel.contains(vsf))
                    {
                        changed = true;
                        listModel.addElement(vsf);
                    }
                }

                if(changed)
                {
                    if(listModel.size() > 1)
                    {
                        // sort the list
                        Object[] tmpElements = listModel.toArray();
                        List<File> tmpList = new ArrayList<File>();

                        for(int x = 0; x < tmpElements.length; x++)
                        {
                            tmpList.add((File) tmpElements[x]);
                        }

                        Collections.sort(tmpList);

                        listModel.clear();

                        for(int x = 0; x < tmpList.size(); x++)
                        {
                            listModel.addElement(tmpList.get(x));
                        }

                        tmpList.clear();
                        tmpElements = null;
                    }

                    parentApp.setMergeButtonEnabled(true);
                }
            }
        }
        else
        {
            log.error("flavors array is null or empty!");
            return false;
        }

        return true;
    }
}
