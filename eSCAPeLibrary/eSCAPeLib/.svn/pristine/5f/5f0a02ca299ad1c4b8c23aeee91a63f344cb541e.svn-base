package junit.oval;

import java.util.List;

import junit.TestCaseAbstract;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.schema.NameDoc;
import junit.framework.Assert;

public class SchemaDOMParserTest extends TestCaseAbstract {
	
	private static Logger LOG = Logger.getLogger(SchemaDOMParserTest.class);

	public SchemaDOMParserTest(String name) {
		super(name);
	}

	public void testOvalBehaviors() {

		OvalDefinitionsDocument oval53 = buildOvalDocument(SCAPDocumentTypeEnum.OVAL_53);
        List<NameDoc> dataTypes53 = oval53.getDataTypeEnumerations();
        System.out.println("oval 53 data types:");
        showDataTypes(dataTypes53);
        Assert.assertEquals(8, dataTypes53.size());
        
        
        OvalDefinitionsDocument oval59 = buildOvalDocument(SCAPDocumentTypeEnum.OVAL_59);
        List<NameDoc> dataTypes59 = oval59.getDataTypeEnumerations();
        System.out.println("oval 59 data types:");
        Assert.assertEquals(11, dataTypes59.size());
        showDataTypes(dataTypes59);
	}
	
	private OvalDefinitionsDocument buildOvalDocument(SCAPDocumentTypeEnum type) {
		OvalDefinitionsDocument ovalDoc = 
			(OvalDefinitionsDocument) SCAPDocumentFactory.createNewDocument(type);
		assertNotNull(ovalDoc);
		ovalDoc.setBaseId("com.g2inc");
		return ovalDoc;
	}
	
    private void showDataTypes(List<NameDoc> list) {
        for (NameDoc nameDoc : list) {
            System.out.println("\t" + nameDoc.toString());
        }
    }
}
