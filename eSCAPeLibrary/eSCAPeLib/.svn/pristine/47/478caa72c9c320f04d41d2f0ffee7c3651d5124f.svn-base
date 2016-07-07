package com.g2inc.scap.library.domain.oval.oval54;

import java.util.List;

import org.jdom.Document;

import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.AffectedItemFamilyEnum;
import com.g2inc.scap.library.domain.oval.OvalFunctionEnum;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.impl.DefinitionsDocumentBase;
import com.g2inc.scap.library.domain.oval.impl.OvalTestSingleStateImpl;
import com.g2inc.scap.library.schema.NameDoc;

/**
 * Implementation of an OVALDefinitionsDocument for OVAL 5.4
 *
 */
public class DefinitionsDocumentImpl extends DefinitionsDocumentBase
{
    public static final OvalFunctionEnum[] validFunctions = {OvalFunctionEnum.begin, OvalFunctionEnum.concat,
        OvalFunctionEnum.end, OvalFunctionEnum.escape_regex, OvalFunctionEnum.split, OvalFunctionEnum.substring};
 

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
        setDocumentType(SCAPDocumentTypeEnum.OVAL_54);
	}
	
	@Override
	public OvalTest getTestWrapper() {
		return new OvalTestSingleStateImpl(this);
	}

	@Override
	public OvalFunctionEnum[] getValidFunctions() {
		return validFunctions;
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
