package com.g2inc.scap.editor.gui.windows.help;
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
import java.net.MalformedURLException;

import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.help.JHelp;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.resources.EditorMessages;

/**
 * This class has some static methods that allow us to be flexible in finding our helpsets.
 */
public class HelpSetUtil
{
    private static Logger log = Logger.getLogger(HelpSetUtil.class);

    public static final String HELPSET_FILENAME = "jhelpset.hs";
    public static final String WORKING_DIR_PROPERTY = "user.dir";

    /**
     * Get the full path to the helpset file(jhelpset.hs) for the getting started guide.
     *
     * @return File
     */
    public static File findGettingStartedHelpset()
    {
        File ret = null;

        String currentDir = System.getProperty(WORKING_DIR_PROPERTY);

        log.debug("Current working directory is " + currentDir);
        
        File f = new File(currentDir);

        if(f.getName().equals(EditorMessages.PRODUCT_SHORTNAME))
        {
            // we are running from an actual distribution of the editor, not from eclipse
            String helpsetPath = "doc" + File.separator + "javahelp" + File.separator + "gs" + File.separator + HELPSET_FILENAME;
            
            File hsPath = new File(helpsetPath);
            
            if(hsPath.exists())
            {
               ret = hsPath;
            }
        }
        else
        {
            // we are probably running from eclipse, or netbeans, or some other ide
            String helpsetPath = ".." + File.separator + "SCAPEditorDoc"
                    + File.separator + "output" + File.separator + "gs" + File.separator + HELPSET_FILENAME;

            File hsPath = new File(helpsetPath);

            if(hsPath.exists())
            {
               ret = hsPath;
            }
        }

        return ret;
    }

    /**
     * Get the full path to the helpset file(jhelpset.hs) for the user guide.
     *
     * @return File
     */
    public static File findUserGuideHelpset()
    {
        File ret = null;

        String currentDir = System.getProperty(WORKING_DIR_PROPERTY);

        File f = new File(currentDir);

        if(f.getName().equals(EditorMessages.PRODUCT_SHORTNAME))
        {
            // we are running from an actual distribution of the editor, not from eclipse
            String helpsetPath = "doc" + File.separator + "javahelp" + File.separator + "ug" + File.separator + HELPSET_FILENAME;

            File hsPath = new File(helpsetPath);

            if(hsPath.exists())
            {
               ret = hsPath;
            }
            else
            {
                log.error("Missing user guide helpset.  " + EditorMessages.PRODUCT_SHORTNAME + " installation is probably missing files");
            }
        }
        else
        {
            // we are probably running from eclipse, or netbeans, or some other ide
            String helpsetPath = ".." + File.separator + "SCAPEditorDoc"
                    + File.separator + "output" + File.separator + "ug" + File.separator + HELPSET_FILENAME;

            File hsPath = new File(helpsetPath);

            if(hsPath.exists())
            {
               ret = hsPath;
            }
            else
            {
                log.error(hsPath.getAbsolutePath() + " can't be found.  Have you run the SCAPEditorDoc ant build?");
            }
        }

        return ret;
    }

    /**
     * Get a helpviewer window for the given helpset.
     * 
     * @param hsPath Path to .hs file
     * @return HelpViewerWindow
     */
    public static HelpViewerWindow getHelpViewer(File hsPath)
    {
        HelpViewerWindow ret = null;

        ClassLoader cl = hsPath.getClass().getClassLoader();
        JHelp jhelp = null;

        try
        {
            jhelp = new JHelp(new HelpSet(cl, hsPath.toURI().toURL()));
            ret = new HelpViewerWindow();
            ret.setHelp(jhelp);
        }
        catch(MalformedURLException murle)
        {
            log.error("Exception", murle);
        }
        catch(HelpSetException hse)
        {
            log.error("Exception", hse);
        }

        return ret;
    }
}
