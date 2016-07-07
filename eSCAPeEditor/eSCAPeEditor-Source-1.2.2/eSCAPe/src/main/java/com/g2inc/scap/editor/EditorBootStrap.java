package com.g2inc.scap.editor;
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

import java.awt.EventQueue;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.windows.EditorConfiguration;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.LibraryConfiguration;
import com.g2inc.scap.library.domain.SCAPContentManager;
import javax.swing.ImageIcon;

/**
 * This is the main class for the SCAPEditor program.  When command-line arguments
 * are supported they should be validated and processed here.
 * 
 * @author ssill2
 */
public class EditorBootStrap
{
    private static Logger LOG = Logger.getLogger(EditorBootStrap.class);
    
    /**
     * Main entry point into the SCAPEditor program.
     *
     * @param args An array of strings that represent command-line arguments
     * @author ssill2
     */
    public static void main (String[] args)
    {
        try
        {
        	LibraryConfiguration.getInstance();  // initialize library config
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Error configuring logging!", e);
        }

        try
        {	
            String osname = System.getProperty("os.name");

            if(osname.indexOf("indows") > -1)
            {
            	LOG.info("OS is " + osname + ", using Windows look and feel");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            else
            {
            	LOG.info("OS is " + osname + ", using JAVA look and feel");
            }
        }
        catch (ClassNotFoundException e)
        {
                e.printStackTrace();
        } catch (InstantiationException e)
        {
                e.printStackTrace();
        } catch (IllegalAccessException e)
        {
                e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e)
        {
                e.printStackTrace();
        }

        final EditorMainWindow mainWin = EditorMainWindow.getInstance();
        // assume any arguments on the command-line are files to be opened
        if(args != null && args.length > 0)
        {
    		List<File> filesToOpen = new LinkedList<File>();

    		for(int x = 0; x < args.length; x++)
        	{
        		String argument = args[x];	
        		
        		// make sure this is not an argument of some other kind
        		// like -foo=bar
        		if(!argument.startsWith("-") && argument.indexOf("=") == -1)
        		{
        			// assume this is a file.
        			File f = new File(argument);
        			
        			if(!f.exists())
        			{
        				LOG.warn("A non-existent filename was passed to us on the command-line, skipping: "
        						+ argument);
        			}
        			else
        			{
        				filesToOpen.add(f);
        			}        			
        		}
        		else
        		{
        			// TODO: process some other kind of argument
        		}
        	}
        	
        	if(filesToOpen.size() > 0)
        	{
        		mainWin.setFilesFromCommandLine(filesToOpen);
        	}
        }

        
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                mainWin.setTitle(EditorMessages.PRODUCT_NAME + " " + EditorConfiguration.getInstance().getEditorVersion());
                mainWin.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("com/g2inc/scap/editor/gui/icons/icon_32 2.png")).getImage());
                mainWin.setLocationRelativeTo(null);
                mainWin.setVisible(true);
            }
        });

        Runnable cpeLoader = new Runnable()
        {
            @Override
            public void run()
            {
                SCAPContentManager scm = SCAPContentManager.getInstance();

                // force this to be loaded
                scm.getOffcialCPEDictionary();

                LOG.info("Official " + EditorMessages.CPE + " dictionary content loaded");
            }
        };

        Thread cpeLoaderThread = new Thread(cpeLoader);
        cpeLoaderThread.start();

        while(cpeLoaderThread.isAlive())
        {
            try
            {
                Thread.sleep(3000);
            }
            catch(InterruptedException ie)
            {

            }
        }

        try
        {
            cpeLoaderThread.join();
        }
        catch(InterruptedException ie)
        {
        }
    }
}
