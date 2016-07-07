package com.g2inc.scap.library.domain.oval;

import java.io.File;
import java.io.FilenameFilter;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.LibraryConfiguration;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;

public class DupTest extends TestCase {
    
	private static Logger LOG = Logger.getLogger(DupTest.class);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LibraryConfiguration.getInstance(); 
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testDuplicates() throws Exception {
        LOG.debug("testDuplicates started");
        String xmlDirName = System.getProperty("xml.dir");
        assertNotNull(xmlDirName);
        String outFileName = System.getProperty("out.file");
        assertNotNull(outFileName);
        File xmlDir = new File(xmlDirName);
        File[] xmlFiles = xmlDir.listFiles(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("def") && name.endsWith(".xml");
            }
        });
        assertTrue(xmlFiles.length > 0);
        OvalDefinitionsDocument content = null;
        int suffix = 0;
        for (File xmlFile : xmlFiles) {
            LOG.debug("***********************************************************************************");
            LOG.debug("Processing xml file " + xmlFile.getName());
            OvalDefinitionsDocument odd = (OvalDefinitionsDocument) SCAPDocumentFactory.loadDocument(xmlFile);
            LOG.debug("***********************************************************************************");
            if(odd != null)
            {
                if(content == null)
                {
                    // first time through we need to create a target document of the same type as the
                    // content we are downloading
                    content = (OvalDefinitionsDocument) SCAPDocumentFactory.createNewDocument(odd.getDocumentType());
                    File ovalFile = new File(xmlDir, outFileName);           
                    content.setFilename(ovalFile.getAbsolutePath());
                }
                MergeStats mergeStats = new MergeStats();
                mergeStats.initialize();
                content.merge(odd, mergeStats);
                StringBuilder sb = new StringBuilder(outFileName);
                int dotIndex = sb.lastIndexOf(".xml");
                sb.insert(dotIndex, Integer.toString(suffix++));
                File ovalFile = new File(xmlDir, sb.toString());     
                content.saveAs(ovalFile);
                LOG.debug("***********************************************************************************");
                LOG.debug("Merged xml file " + xmlFile.getName() + ", validating");
                LOG.debug("***********************************************************************************");
                String valMessage = content.validate();
                if (valMessage != null) {
                    LOG.error("================================================================================");
                    LOG.error("VALIDATION FAILED AFTER MERGING " + xmlFile.getName());
                    LOG.error(valMessage);
                    LOG.error("================================================================================");
                    break;
                }
            } else {
                LOG.error("loadDocument RETURNED NULL from inputstream for vuln " + xmlFile.getName());
            }  
        }

        content.save();
        LOG.debug("testDuplicates ended");
    }

}
