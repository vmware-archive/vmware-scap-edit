package junit;

import java.util.List;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.content.style.ContentStyle;
import com.g2inc.scap.library.content.style.ContentStyleRegistry;
import com.g2inc.scap.library.domain.LibraryConfiguration;

public class ContentRegistryTest extends TestCaseAbstract {
	
	private static Logger LOG = Logger.getLogger(ContentRegistryTest.class);

	public ContentRegistryTest(String name) {
		super(name);
	}

	public void testContentRegistry() {
		String styleDirName = props.getProperty(LibraryConfiguration.PROPERTY_STYLE_JAR_DIR);
		if (styleDirName == null) {
			LOG.debug("No style directory name, allow test to pass");
			return;
		}
		System.setProperty(LibraryConfiguration.PROPERTY_STYLE_JAR_DIR, styleDirName);
		
		ContentStyleRegistry registry = ContentStyleRegistry.getInstance();
		List<ContentStyle> styles = registry.getAvailableStyles();
		assertNotNull(styles);
		if (styles.size() == 0 || styles.size() == 1) {
			LOG.warn("Assume no styles, or only default style is available; assume environment " +
					"is not set for styles. The 'Mace' product uses styles, but eSCAPe does not");
			return;
		}
		showStyles(styles);
		String expectedNumberString = props.getProperty("content.style.number");
		assertNotNull(expectedNumberString);
		int expectedNumber = Integer.parseInt(expectedNumberString);
		assertEquals(expectedNumber, styles.size());
		
	}
	
	private void showStyles(List<ContentStyle> styles) {
		LOG.debug("Number of styles: " + styles.size());
		for (ContentStyle style : styles) {
			LOG.debug(style.getStyleName());
		}
	}
}
