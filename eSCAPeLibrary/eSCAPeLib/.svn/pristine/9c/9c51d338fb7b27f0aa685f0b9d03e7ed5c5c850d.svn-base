package com.g2inc.scap.library.domain.oval.oval56;

import java.util.List;

import org.jdom.Document;

import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.AffectedItemFamilyEnum;
import com.g2inc.scap.library.domain.oval.OvalFunctionEnum;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.impl.DefinitionsDocumentBase;
import com.g2inc.scap.library.domain.oval.impl.OvalTestMultiStateImpl;
import com.g2inc.scap.library.schema.NameDoc;

/**
 * Implementation of an OVALDefinitionsDocument for OVAL 5.6
 *
 */
public class DefinitionsDocumentImpl extends DefinitionsDocumentBase
{
    public static final OvalFunctionEnum[] validFunctions = {OvalFunctionEnum.arithmetic, OvalFunctionEnum.begin, OvalFunctionEnum.concat,
        OvalFunctionEnum.end, OvalFunctionEnum.escape_regex, OvalFunctionEnum.split, OvalFunctionEnum.substring,
        OvalFunctionEnum.time_difference, OvalFunctionEnum.regex_capture};
    
    public static final AffectedItemFamilyEnum [] supportedAffectedFamilies = new AffectedItemFamilyEnum[] {
        AffectedItemFamilyEnum.catos,
        AffectedItemFamilyEnum.ios,
        AffectedItemFamilyEnum.macos,
        AffectedItemFamilyEnum.pixos,
        AffectedItemFamilyEnum.undefined,
        AffectedItemFamilyEnum.unix,
        AffectedItemFamilyEnum.windows
    };

	public DefinitionsDocumentImpl(Document doc)
	{
            super(doc);
            setDocumentType(SCAPDocumentTypeEnum.OVAL_56);
	}
	
	@Override
	public OvalTest getTestWrapper() {
		return new OvalTestMultiStateImpl(this);
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
