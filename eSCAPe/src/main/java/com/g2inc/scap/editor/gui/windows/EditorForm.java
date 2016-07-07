package com.g2inc.scap.editor.gui.windows;
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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

public abstract class EditorForm extends JInternalFrame implements InternalFrameListener
{
    private static Logger log = Logger.getLogger(EditorForm.class);
    private boolean dirty = false;

    public abstract void setSelectedElement(TreePath selectionPath);
    public abstract TreePath getSelectedPath();
    public abstract SCAPDocument getDocument();

    protected List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    protected EditorForm()
    {
        addInternalFrameListener(this);
    }
    
    public static final String MODIFIED_STRING = "(modified)";
    
    public void setDirty(boolean b)
    {
        dirty = b;

        if(b)
        {
            if(getTitle().indexOf(MODIFIED_STRING) == -1)
            {
                setTitle(getTitle() + MODIFIED_STRING);
            }

            if(getDocument().getBundle() != null)
            {
                getDocument().getBundle().setDirty(true);
            }
        }
        else
        {
            if(getTitle().indexOf(MODIFIED_STRING) > -1)
            {
                setTitle(getTitle().substring(0, getTitle().indexOf(MODIFIED_STRING)));
            }
        }
    }

    public boolean isDirty()
    {
        return dirty;
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e)
    {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e)
    {
    	System.gc();
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e)
    {
        changeListeners.clear();
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e)
    {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e)
    {
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e)
    {
    }

    private void handleBundledDoc(EditorMainWindow mw, SCAPDocument sdoc)
    {
        notifyRegisteredListeners();
        return;
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent ife)
    {
        EditorMainWindow mw = EditorMainWindow.getInstance();
        SCAPDocument sdoc = (SCAPDocument) getDocument();

        if(sdoc == null) {
        	log.warn("EditorForm internalFrameClosing found NULL SCAPDocument!");
            return;
        }
        
        if (sdoc.getDataStreamCollection() != null) {
        	log.info("EditorForm internalFrameClosing called for data-stream-collection component " + sdoc.getFilename());
        	// This SCAPDocument is a component in a data-stream-collection. Don't ask about saving it
        	// here, save will be done when data-stream-collection is closed.
        	return;
        }
        
        // handle bundled documents differently
        if(sdoc.getBundle() != null)
        {
            handleBundledDoc(mw, sdoc);
            return;
        }

        if(!dirty)
        {        
            return;
        }

        String filename = getDocument().getFilename();

        if(filename == null)
        {
            return;
        }

        Object[] options = { "Save", "Discard" };

        String message = filename + " has unsaved changes.  Do you want to save or discard changes?";
        String dTitle = "Unsaved changes";

        int n = JOptionPane.showOptionDialog(this, message,
                dTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if(n == JOptionPane.DEFAULT_OPTION || n == JOptionPane.YES_OPTION)
        {
            try
            {
            	getDocument().save();
            	setDirty(false);
            }
            catch(Exception e)
            {
                log.error("Error saving file " + filename, e);
                String errMessage = filename + " couldn't be saved.  An error occured: " + e.getMessage();
                dTitle = "File save error";

                JOptionPane.showMessageDialog(this, errMessage, dTitle, JOptionPane.ERROR_MESSAGE);
            }
        }

        sdoc.close();
        sdoc = null;        
    }

    protected String elementToString(Element e)
    {
        StringWriter sw = new StringWriter();
        XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());

        try
        {
            xmlo.output(e, sw);
            sw.close();
        }
        catch (IOException ioe)
        {
            log.error("exception occurred", ioe);
        }

        return sw.toString();
    }

    public void addChangeListener(ChangeListener cl)
    {
        changeListeners.add(cl);
    }

    public void removeChangeListener(ChangeListener cl)
    {
        changeListeners.remove(cl);
    }

    protected void notifyRegisteredListeners()
    {
        for(int x = 0; x < changeListeners.size(); x++)
        {
            ChangeListener cl = changeListeners.get(x);

            ChangeEvent ce = new ChangeEvent(this);

            cl.stateChanged(ce);
        }        
    }
    
    public abstract void refreshRootNode();
}
