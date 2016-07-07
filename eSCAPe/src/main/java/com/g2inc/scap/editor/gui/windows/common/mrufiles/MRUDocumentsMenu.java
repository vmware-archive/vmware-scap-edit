package com.g2inc.scap.editor.gui.windows.common.mrufiles;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.event.ChangeListener;

public class MRUDocumentsMenu extends JMenu
{
    public static final String PROPERTY_MRU_FILE_PREFIX = "mru.file.";
    private List<MRURecentDocumentMenuItem> menuItems = new ArrayList<MRURecentDocumentMenuItem>();
    private int numDocsToKeep = 5;

    /** Creates new form RecentDocumentsMenu */
    public MRUDocumentsMenu()
    {
        super("Recent");
        initComponents();
    }

    public void clearItems()
    {
        while(menuItems.size() > 0)
        {
            MRURecentDocumentMenuItem menuItem = menuItems.get(0);
            menuItems.remove(menuItem);
            remove(menuItem);
        }

        setEnabled(false);
    }

    private int indexOfFile(File f)
    {
        int ret = -1;

        for(int x = 0; x < menuItems.size() ; x++)
        {
            MRURecentDocumentMenuItem menuItem = menuItems.get(x);

            if(menuItem.getFile() != null && menuItem.getFile().equals(f))
            {
                return x;
            }
        }

        return ret;
    }

    /**
     * Add a new item to this recently used file menu.
     *
     * @param recentFile The file to which the new menu item will point.
     * @param cl The change listener that will be notified when the item is clicked.
     */
    public void addItem(File recentFile, ChangeListener cl)
    {
        int index = indexOfFile(recentFile);

        if(index < 0)
        {
            MRURecentDocumentMenuItem menuItem = new MRURecentDocumentMenuItem(recentFile.getName());
            menuItem.addChangeListener(cl);
            menuItem.setFile(recentFile);

            menuItems.add(0,menuItem);
            add(menuItem,0);

            menuItem.setToolTipText(recentFile.getAbsolutePath());

            while(menuItems.size() > numDocsToKeep)
            {
                MRURecentDocumentMenuItem itemToRemove = menuItems.get(menuItems.size() -1);

                menuItems.remove(itemToRemove);
                remove(itemToRemove);
            }

            setEnabled(true);
        }
        else
        {
            if(index > 0)
            {
                // move it to the top of the list
                MRURecentDocumentMenuItem existing = menuItems.get(index);
                menuItems.remove(index);
                remove(existing);

                menuItems.add(0, existing);
                add(existing, 0);
            }
        }
    }

    public List<File> getRecentFiles()
    {
        ArrayList<File> files = new ArrayList<File>();

        for(int x = 0; x < menuItems.size(); x++)
        {
            MRURecentDocumentMenuItem item = menuItems.get(x);
            File f = item.getFile();
            files.add(f);
        }
        
        return files;
    }

    public int getNumDocsToKeep()
    {
        return numDocsToKeep;
    }

    public void setNumDocsToKeep(int numDocsToKeep)
    {
        this.numDocsToKeep = numDocsToKeep;

        while(menuItems.size() > numDocsToKeep)
        {
            MRURecentDocumentMenuItem itemToRemove = menuItems.get(menuItems.size() -1);

            menuItems.remove(itemToRemove);
            remove(itemToRemove);
        }

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
