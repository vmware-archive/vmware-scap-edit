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
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This class is a singleton containing runtime configuration information needing to
 * be accessed by other parts of the library.  The values here should be read-only(i.e. no public setters)
 * and only loaded once at library load time.
 */
public class LibraryConfiguration 
{
	private static Logger log = Logger.getLogger(LibraryConfiguration.class);
	
	private static LibraryConfiguration instance;
	
	private File baseResourceDir = null;
	private File templateDir = null;
	private File contentDir = null;
	private File libraryHome = null;
	private File scapValDir = null;
	
	private File logsDir = null;
	private File logFile = null;
	private String logLevel = null;
	
	private File cpeDictFile = null;
	private File cpeOvalFile = null;
	private boolean online = true;
	
	private File styleJarDir = null;
	
	private String libraryVersion = "0.0.0-SNAPSHOT";

	/**
	 * Property name of where to store the logfile.
	 */
	public static final String PROPERTY_LOG_DIR = "escape.base.resource.logs.dir";

	/**
	 * Property name of the logfile.
	 */
	public static final String PROPERTY_LOG_FILE = "escape.base.resource.logs.logfile";
	
	/**
	 * Default logfile name if none is supplied.
	 */
	public static final String DEFAULT_LOG_FILE = "eSCAPe.log";
	
	/**
	 * Property name for name of the offical cpe dictionary file.
	 */
	public static final String PROPERTY_OFFICIAL_CPE_DICTIONARY_FILE = "escape.base.resource.cpe.dictionary.file";

	/**
	 * Default offical cpe dictionary file to be used if none is supplied.
	 */
	public static final String DEFAULT_OFFICIAL_CPE_DICTIONARY_FILE = "official-cpe-dictionary_v2.3.xml";
	
	/**
	 * Property name for name of the offical cpe oval file.
	 */
	public static final String PROPERTY_OFFICIAL_CPE_OVAL_FILE       = "escape.base.resource.cpe.oval.file";

	/**
	 * Default offical cpe oval file to be used if none is supplied.
	 */
	public static final String DEFAULT_OFFICIAL_CPE_OVAL_FILE 		 = "official-cpe-oval.xml";
	
	/**
	 * Property defining where the library will search for requested resources like templates
	 */
	public static final String PROPERTY_BASE_RESOURCE_DIR = "escape.base.resource.dir";
	
	/**
	 * Property defining where the library will search for templates used to create new documents.
	 */
	public static final String PROPERTY_TEMPLATE_DIR = "escape.base.resource.template.dir";
	// default relative to basedir
	public static final String DEFAULT_TEMPLATE_DIR = "template";
	
	/**
	 * Property defining where the library will search for templates used to create new documents.
	 */
	public static final String PROPERTY_CONTENT_DIR = "escape.base.resource.content.dir";	
	// default relative to basedir
	public static final String DEFAULT_CONTENT_DIR = "data_feeds";

	/**
	 * Property name for log level.
	 */
	public static final String PROPERTY_LOG_LEVEL = "escape.log.level";
	
	/**
	 * Default log level if none is supplied.
	 */
	public static final String DEFAULT_LOG_LEVEL = "INFO";
	/**
	 * boolean property specifying whether we can download files (cpe dictionary, eg)
	 */
	private static final String PROPERTY_ONLINE = "escape.online";
	
	public static final String PROPERTY_ESCAPE_LIBRARY_HOME = "escape.library.home";
	
	public static final String PROPERTY_STYLE_JAR_DIR = "style.jar.dir";
	// default styles directory relative to basedir
	public static final String DEFAULT_STYLE_DIR = "styles";
	

	static
	{
		getInstance();
	}
	
	private void initLogging()
	{
        Properties log4jProps = new Properties();

        String commonPatternString="%d [%c] %p %m%n";

        // set some properties
        log4jProps.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        log4jProps.setProperty("log4j.appender.stdout.Target", "System.out");
        log4jProps.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        log4jProps.setProperty("log4j.appender.stdout.layout.ConversionPattern", commonPatternString);
        log4jProps.setProperty("log4j.appender.R", "org.apache.log4j.RollingFileAppender");
        
        String logDirProp = System.getProperty(PROPERTY_LOG_DIR, null);
        if(logDirProp == null)
        {
        	logDirProp = getUserHomeDirectory() + File.separator + ".escape"  + File.separator + "logs";
        }
        logsDir = new File(logDirProp);
        
        String logFileProp = System.getProperty(PROPERTY_LOG_FILE, null);
        if(logFileProp == null)
        {
        	logFileProp = logsDir.getAbsolutePath() + File.separator + DEFAULT_LOG_FILE;
        }
        logFile = new File(logFileProp);
        
        String loglevel = System.getProperty(PROPERTY_LOG_LEVEL, null);
        if(loglevel == null)
        {
        	loglevel = DEFAULT_LOG_LEVEL;
        }
        else
        {
        	String validLevelPatternString = "^(?:TRACE|INFO|ERROR|DEBUG|OFF)$";
        	if(!loglevel.matches(validLevelPatternString))
        	{
        		System.err.println("Supplied loglevel \"" + loglevel +"\" not valid, using default: " + DEFAULT_LOG_LEVEL);
        		loglevel = DEFAULT_LOG_LEVEL;
        		logLevel = loglevel;
        	}
        }
        
        log4jProps.setProperty("log4j.appender.R.File", logFile.getAbsolutePath());
        log4jProps.setProperty("log4j.appender.R.MaxFileSize", "1MB");
        log4jProps.setProperty("log4j.appender.R.MaxBackupIndex", "5");
        log4jProps.setProperty("log4j.appender.R.layout", "org.apache.log4j.PatternLayout");
        log4jProps.setProperty("log4j.appender.R.layout.ConversionPattern", commonPatternString);

        log4jProps.setProperty("log4j.rootLogger", loglevel + ",stdout,R");
        log4jProps.setProperty("log4j.category.org.apache", "ERROR");

        PropertyConfigurator.configure(log4jProps);

        log.info("Logging configured: Logging to " + logFile.getAbsolutePath());
	}
	
	private LibraryConfiguration()
	{
		initLogging();
		
		// Set the library version
		ClassLoader cl = this.getClass().getClassLoader();
		
		try
		{
			InputStream is = cl.getResourceAsStream("com/g2inc/scap/library/domain/VERSION.props");
			Properties versionProps = new Properties();
			
			versionProps.load(is);
			
			is.close();
			
			String tmpLibraryVersion = versionProps.getProperty("version");
		
			if(tmpLibraryVersion != null && tmpLibraryVersion.trim().length() > 0)
			{
				libraryVersion = tmpLibraryVersion;
			}
			else
			{
				log.error("version property in VERSION.props is either null or zero-length!");
			}
			versionProps.clear();
			versionProps = null;
		}
		catch(Exception e)
		{
			log.error("Error reading library version", e);
		}
		
		log.info("libraryVersion: " + libraryVersion);
		
		// Configure base dir
		String baseDirProp = System.getProperty(PROPERTY_BASE_RESOURCE_DIR, null);	
		if(baseDirProp == null)
		{
			baseDirProp = System.getProperty("user.dir");
		}	
		log.info("basedir: " + baseDirProp);
		baseResourceDir = new File(baseDirProp);
		
		// Configure template dir
		String templateDirProp = System.getProperty(PROPERTY_TEMPLATE_DIR, null);
		if(templateDirProp == null)
		{
			templateDirProp = baseResourceDir.getAbsolutePath() + File.separator + DEFAULT_TEMPLATE_DIR;
		}
		log.info("templatedir: " + templateDirProp);
		templateDir = new File(templateDirProp);
		
		// Configure content dir
		String contentDirProp = System.getProperty(PROPERTY_CONTENT_DIR, null);		
		if(contentDirProp == null)
		{
			contentDirProp = baseResourceDir.getAbsolutePath() + File.separator + DEFAULT_CONTENT_DIR;
		}
		log.info("contentdir: " + contentDirProp);
		contentDir = new File(contentDirProp);		
		
		// Configure official cpe dictionary file
		String cpeDictProp = System.getProperty(PROPERTY_OFFICIAL_CPE_DICTIONARY_FILE, null);
		if(cpeDictProp == null)
		{
			cpeDictProp = getContentDir() + File.separator + DEFAULT_OFFICIAL_CPE_DICTIONARY_FILE;
		}
		log.info("cpeDictFile: " + cpeDictProp);
		cpeDictFile = new File(cpeDictProp);
		
		//Configure official cpe oval file
		String cpeOvalProp = System.getProperty(PROPERTY_OFFICIAL_CPE_OVAL_FILE, null);
		if(cpeOvalProp == null)
		{
			cpeOvalProp = getContentDir() + File.separator + DEFAULT_OFFICIAL_CPE_OVAL_FILE;
		}
		log.info("cpeOvalFile: " + cpeOvalProp);
		cpeOvalFile = new File(cpeOvalProp);
		
		// Configure online property (true means we can download, false we can't)
		String onlineString = System.getProperty(PROPERTY_ONLINE, "true");
		online = (onlineString.equals("true") || onlineString.equals("1")); 
		
		String libraryHomeProperty = System.getProperty(PROPERTY_ESCAPE_LIBRARY_HOME, ".");
		libraryHome = new File(libraryHomeProperty);
		scapValDir = new File(libraryHome, "scapval12");
		
	//	resetStyleJarDir();
	
	}
	
	public File getLibraryHome() {
		return libraryHome;
	}

	public File getScapValDir() {
		return scapValDir;
	}

	public static synchronized LibraryConfiguration getInstance()
	{
		if(instance == null)
		{
			instance = new LibraryConfiguration();
		}
		
		return instance;
	}
	
	/**
	 * Return the configured base resource directory
	 * 
	 * @return String The absolute path of the of the base resource directory
	 */
	public String getBaseResourceDir()
	{
		// don't return the actual reference
		return baseResourceDir.getAbsolutePath();
	}
	
	/**
	 * Return template directory path
	 * 
	 * @return String The absolute path to the template dir
	 */
	public String getTemplateDir()
	{
		return templateDir.getAbsolutePath();
	}

	/**
	 * Return content directory path
	 * 
	 * @return String The absolute path to the content dir
	 */
	public String getContentDir()
	{
		return contentDir.getAbsolutePath();
	}
	
	/**
	 * Return path to official cpe dictionary file
	 * 
	 * @return String The absolute path to the official cpe dictionary file.
	 */
	public String getOfficialCPEDictionaryPath()
	{
		return cpeDictFile.getAbsolutePath();
	}
	
	/**
	 * Return path to official cpe oval file
	 * 
	 * @return String The absolute path to the official cpe oval file.
	 */
	public String getOfficialCPEOvalPath()
	{
		return cpeOvalFile.getAbsolutePath();
	}
	
	/**
	 * Return path to oval templates dir
	 *
	 * @return String
	 */
	public String getOvalTemplateDir()
	{
		return templateDir.getAbsolutePath() + File.separator + "oval";
	}
	
	/**
	 * Return path to XCCDF templates dir
	 *
	 * @return String
	 */
	public String getXCCDFTemplateDir()
	{
		return templateDir.getAbsolutePath() + File.separator + "xccdf";
	}	

	/**
	 * Return path to CPE templates dir
	 *
	 * @return String
	 */
	public String getCPETemplateDir()
	{
		return templateDir.getAbsolutePath() + File.separator + "cpe";
	}
	
	/**
	 * Convenience method for getting path to user's home dir.
	 *
	 * @return String
	 */
	public String getUserHomeDirectory()
	{
		return System.getProperty("user.home");
	}
	
	/**
	 * Return the path to to the log file
	 *
	 * @return String
	 */
	public String getLogfilePath()
	{
		return logFile.getAbsolutePath();
	}
	
	/**
	 * Return the version of the library.
	 *
	 * @return String
	 */
	public String getLibraryVersion()
	{
		return libraryVersion;
	}
	
	public boolean isOnline() {
		return online;
	}
	
	public File getStyleJarDir() {
		return styleJarDir;
	}
	
	public void resetStyleJarDir() {
		String styleJarDirProperty = System.getProperty(PROPERTY_STYLE_JAR_DIR, "styles");
		if (styleJarDirProperty != null) {
			styleJarDir = new File(styleJarDirProperty);
			if (!styleJarDir.exists() || !styleJarDir.isDirectory()) {
				log.error("System property " + PROPERTY_STYLE_JAR_DIR + " must point to a directory that exists - value: " + styleJarDir.getAbsolutePath());
			}
		} else {
			log.info("System property " + PROPERTY_STYLE_JAR_DIR + " does not exist, no ContentStyles will be used");
		}
	}
}