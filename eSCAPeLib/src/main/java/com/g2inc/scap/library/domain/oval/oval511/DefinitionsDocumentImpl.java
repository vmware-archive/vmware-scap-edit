package com.g2inc.scap.library.domain.oval.oval511;
/* Copyright (c) 2016 - 2016. VMware, Inc. All rights reserved.
* 
* This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License version 3.0 
* as published by the FreeSoftware Foundation This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License version 3.0 
* for more details. You should have received a copy of the GNU General Public License version 3.0 along with this program; if not, write to
* the Free Software Foundation, Inc., 675 Mass Avenue, Cambridge, MA 02139, USA.
*/

import java.util.List;

import org.jdom.Document;

import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.OvalFunctionEnum;
import com.g2inc.scap.library.domain.oval.impl.DefinitionsDocumentBase;
import com.g2inc.scap.library.schema.NameDoc;

/**
 * Implementation of an OVALDefinitionsDocument for OVAL 5.11
 *
 */
public class DefinitionsDocumentImpl extends DefinitionsDocumentBase
{
    public static final OvalFunctionEnum[] validFunctions = {OvalFunctionEnum.arithmetic, OvalFunctionEnum.begin, OvalFunctionEnum.concat,
        OvalFunctionEnum.end, OvalFunctionEnum.escape_regex, OvalFunctionEnum.split, OvalFunctionEnum.substring,
        OvalFunctionEnum.time_difference, OvalFunctionEnum.regex_capture, 
        OvalFunctionEnum.unique, OvalFunctionEnum.count };

	public DefinitionsDocumentImpl(Document doc) {
        super(doc);
        setDocumentType(SCAPDocumentTypeEnum.OVAL_511);
	}
	
    @Override
    public OvalFunctionEnum[] getValidFunctions() {
        return validFunctions;
    }
    
    @Override
    public List<NameDoc> getDataTypeEnumerations() {
    	return getEnumerationValues("SimpleDatatypeEnumeration");
    }
	
}
