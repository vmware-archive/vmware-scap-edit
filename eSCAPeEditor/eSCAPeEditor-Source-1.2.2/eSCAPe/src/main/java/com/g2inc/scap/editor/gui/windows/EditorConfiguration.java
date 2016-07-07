/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

package com.g2inc.scap.editor.gui.windows;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.util.EditorUtil;

/**
 *
 * @author glenn.strickland
 */
public class EditorConfiguration extends Properties {

	private static EditorConfiguration instance = null;
	File propertiesFile = null;
	private static final Logger LOG = Logger.getLogger(EditorConfiguration.class);
	private static final String PROPERTY_LAST_OPENED_FROM = "last.opened.from";
	private static final String PROPERTY_PREVIOUS_NAMESPACE_USED = "wizmode.previous.namespace";

    public static final String PROPERTY_MAIN_WINDOW_LOCATION_X = "main.window.location.x";
    public static final String PROPERTY_MAIN_WINDOW_LOCATION_Y = "main.window.location.y";
    public static final String PROPERTY_MAIN_WINDOW_SIZE_X = "main.window.size.x";
    public static final String PROPERTY_MAIN_WINDOW_SIZE_Y = "main.window.size.y";

    private static String editorVersion = "0.0.0-SNAPSHOT";

    public static EditorConfiguration getInstance()
    {
		if (instance == null) 
		{
			instance = new EditorConfiguration();
		}
		return instance;
	}

	private EditorConfiguration() {
		File userDir = new File(System.getProperty("user.home", "."));
		propertiesFile = new File(userDir, "SCAPEditor.props");
		EditorUtil.loadProperties(this, propertiesFile.getAbsolutePath());

		// load editor version
		ClassLoader cl = this.getClass().getClassLoader();
		
		try
		{
			InputStream is = cl.getResourceAsStream("com/g2inc/scap/editor/VERSION.props");
			Properties versionProps = new Properties();
			
			versionProps.load(is);
			
			is.close();
			
			String tmpEditorVersion = versionProps.getProperty("version");
		
			if(tmpEditorVersion != null && tmpEditorVersion.trim().length() > 0)
			{
				editorVersion = tmpEditorVersion;
			}
			else
			{
				LOG.error("version property in VERSION.props is either null or zero-length!");
			}
			versionProps.clear();
			versionProps = null;
		}
		catch(Exception e)
		{
			LOG.error("Error reading editor version", e);
		}
	}

	public void save() {
		EditorUtil.writeProperties(this, propertiesFile.getAbsolutePath());
	}

	public String getLastOpenedFrom() {
		return getProperty(PROPERTY_LAST_OPENED_FROM, ".");
	}

	public File getLastOpenedFromFile() {
		String dirName = getProperty(PROPERTY_LAST_OPENED_FROM, ".");
		return new File(dirName);
	}

	public void setLastOpenedFrom(String lastOpenedFrom)
        {
            setProperty(PROPERTY_LAST_OPENED_FROM, lastOpenedFrom);
	}

	public void setLastOpenedFromFile(File lastOpenedFrom)
        {
            setProperty(PROPERTY_LAST_OPENED_FROM, lastOpenedFrom.getAbsolutePath());
	}

	public String getPreviousNamespaceUsed() {
		return getProperty(PROPERTY_PREVIOUS_NAMESPACE_USED);
	}

	public void setPreviousNamespaceUsed(String namespace) {
		setProperty(PROPERTY_PREVIOUS_NAMESPACE_USED, namespace);
	}

	public String getEditorVersion()
	{
		return editorVersion;
	}
}
