package junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.g2inc.scap.library.domain.ocil.OcilDocumentImpl;

import junit.framework.TestCase;

/**
 * Our own base class for junit tests.  This allows us to add frequently used
 * code to methods in this class to be available to any junit tests we write.
 */
public abstract class TestCaseAbstract extends TestCase {	
	private static final Logger LOG = Logger.getLogger(TestCaseAbstract.class);
	
	protected Properties props = null;
	
	public TestCaseAbstract(String name) {
		super(name);
		File configFile = new File("src/test/resources/log4j.xml");
		if (configFile.exists()) {
			DOMConfigurator.configure(configFile.getAbsolutePath());
			LOG.debug("Log4j configured using " + configFile.getAbsolutePath());
		} else {
			System.out.println("Log4j config file not found: " + configFile.getAbsolutePath());
		}
		loadProperties("src/test/resources/expected.properties");
	}
	
	public Properties loadProperties(String fileName) {
		props = new Properties();
		File propertiesFile = new File(fileName);
		if (!(propertiesFile.exists())) {
			LOG.error("Properties file does not exist: " + propertiesFile.getAbsolutePath()); 
		} else {
			try {
				props.load(new FileInputStream(propertiesFile));
			} catch(IOException e) {
				LOG.error("Could not load properties file: " + propertiesFile.getAbsolutePath());
			}	
		}
		return props;
	}

	/**
	 * Simple method to load properties.
	 * 
	 * @param is
	 * @return Properties
	 * @throws Exception
	 */
	public Properties loadProperties(InputStream is) throws Exception {
		props = new Properties();		
		props.load(is);		
		return props;
	}
	
	/**
	 * Get the named property and cast it to an int.
	 * 
	 * @param props
	 * @param propertyName
	 * @return int
	 */
	public int getPropertyAsInt(Properties props, String propertyName)
	{
		int ret = -1;
		
		String str = props.getProperty(propertyName);
		if(str == null)
		{
			return ret;
		}
		
		try
		{
			ret = Integer.parseInt(str);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}
}
