package com.g2inc.scap.library.domain.oval.oval53;

import java.util.List;

import org.jdom.Document;

import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.AffectedItemFamilyEnum;
import com.g2inc.scap.library.domain.oval.CheckEnumeration;
import com.g2inc.scap.library.domain.oval.OperEnum;
import com.g2inc.scap.library.domain.oval.OvalFunctionEnum;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.impl.DefinitionsDocumentBase;
import com.g2inc.scap.library.domain.oval.impl.OvalTestSingleStateImpl;
import com.g2inc.scap.library.schema.NameDoc;

/**
 * Implementation of an OVALDefinitionsDocument for OVAL 5.3
 *
 */
public class DefinitionsDocumentImpl extends DefinitionsDocumentBase
{
	public static final OperEnum[] validEnumsForString    =  {OperEnum.EQUALS, OperEnum.NOT_EQUAL, OperEnum.PATTERN_MATCH };

	private static final OvalFunctionEnum[] validFunctions = {OvalFunctionEnum.begin, OvalFunctionEnum.concat,
		OvalFunctionEnum.end, OvalFunctionEnum.escape_regex, OvalFunctionEnum.split, OvalFunctionEnum.substring};
	
	public static final CheckEnumeration[] supportedCheckEnums = new CheckEnumeration[]
	{
		CheckEnumeration.ALL,
		CheckEnumeration.AT_LEAST_ONE,
		CheckEnumeration.NONE_EXIST,
		CheckEnumeration.NONE_SATISFY,
		CheckEnumeration.ONLY_ONE
	};

    public static final AffectedItemFamilyEnum [] supportedAffectedFamilies = new AffectedItemFamilyEnum[]
    {
        AffectedItemFamilyEnum.ios,
        AffectedItemFamilyEnum.macos,
        AffectedItemFamilyEnum.unix,
        AffectedItemFamilyEnum.windows
    };

	public DefinitionsDocumentImpl(Document doc)
	{
		super(doc);
        setDocumentType(SCAPDocumentTypeEnum.OVAL_53);
	}
	
	@Override
	public OvalTest getTestWrapper() {
		return new OvalTestSingleStateImpl(this);
	}
	
	@Override
	public OperEnum[] getValidEnumsForString() {
		return validEnumsForString;
	}

    @Override
    public AffectedItemFamilyEnum[] getSupportedFamilies()
    {
        return supportedAffectedFamilies;
    }
    
    @Override
    public List<NameDoc> getDataTypeEnumerations() {
    	return getEnumerationValues("DatatypeEnumeration");
    }
}
