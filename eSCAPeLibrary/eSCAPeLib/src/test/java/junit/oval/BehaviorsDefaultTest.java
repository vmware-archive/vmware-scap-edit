package junit.oval;

import java.util.List;

import junit.TestCaseAbstract;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.content.style.ContentStyle;
import com.g2inc.scap.library.content.style.ContentStyleRegistry;
import com.g2inc.scap.library.domain.LibraryConfiguration;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.AvailableObjectBehavior;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.schema.PlatformNameKey;

public class BehaviorsDefaultTest extends TestCaseAbstract {
	
	private static Logger LOG = Logger.getLogger(BehaviorsDefaultTest.class);

	public BehaviorsDefaultTest(String name) {
		super(name);
	}

	public void testOvalBehaviors() {
		String styleDirName = props.getProperty(LibraryConfiguration.PROPERTY_STYLE_JAR_DIR);
		if (styleDirName == null) {
			LOG.debug("No style directory name, allow test to pass");
			return;
		}
		// first use default value of style.jar.dir which should have escapeStyle jar inside
		System.setProperty(LibraryConfiguration.PROPERTY_STYLE_JAR_DIR, styleDirName);
		LibraryConfiguration.getInstance().resetStyleJarDir();
		
		ContentStyleRegistry registry = ContentStyleRegistry.getInstance();
		List<ContentStyle> styles = registry.getAvailableStyles();
		assertNotNull(styles);
		
		showStyles(styles);
		String expectedNumberString = props.getProperty("content.style.number");
		assertNotNull(expectedNumberString);
		int expectedNumber = Integer.parseInt(expectedNumberString);
		assertEquals(expectedNumber, styles.size());
		OvalDefinitionsDocument ovalDocWithStyle = buildOvalDocumentWithObject(styles.get(2));
		String recurseDef = getRecurseDirectionDefault(ovalDocWithStyle);
		assertNotNull(recurseDef);
		assertEquals("down", recurseDef);
		
//		OvalObject fileObject = ovalDocWithStyle.createObject("windows", "file_object");
//		fileObject.setBehaviors(behaviorMap)
		
		// then use value of style.jar.dir which should have NO escapeStyle jar inside
		String noJarsDir = props.getProperty("style.jar.dir.without.jar");
		System.setProperty(LibraryConfiguration.PROPERTY_STYLE_JAR_DIR, noJarsDir);
		LibraryConfiguration.getInstance().resetStyleJarDir();		
		registry.registerAvailableStyles();
		
		styles = registry.getAvailableStyles();
		assertNotNull(styles);
		showStyles(styles);
		assertTrue(styles.size() == 0 || styles.size() == 1);	
		
		OvalDefinitionsDocument ovalDocWithoutStyle = buildOvalDocumentWithObject(null);
		recurseDef = getRecurseDirectionDefault(ovalDocWithoutStyle);
		assertNotNull(recurseDef);
		assertEquals("none", recurseDef);
	}
	
	private OvalDefinitionsDocument buildOvalDocumentWithObject(ContentStyle style) {
		OvalDefinitionsDocument ovalDoc = 
			(OvalDefinitionsDocument) SCAPDocumentFactory.createNewDocument(SCAPDocumentTypeEnum.OVAL_53);
		assertNotNull(ovalDoc);
		ovalDoc.setBaseId("com.g2inc");
		ovalDoc.setContentStyle(style);

		return ovalDoc;
	}
	
	private String getRecurseDirectionDefault(OvalDefinitionsDocument ovalDoc) {
		String recurseDef = null;
        PlatformNameKey pkey = new PlatformNameKey("windows", "file_object");
        List<AvailableObjectBehavior> behaviors = ovalDoc.getBehaviors(pkey);
        for (AvailableObjectBehavior behavior : behaviors) {
        	if (behavior.getName().equals("recurse_direction")) {
        		recurseDef = behavior.getDefaultValue();
        	}
        }
        return recurseDef;
	}
	
	private void showStyles(List<ContentStyle> styles) {
		LOG.debug("Number of styles: " + styles.size());
		for (ContentStyle style : styles) {
			LOG.debug(style.getStyleName());
		}
	}
}
