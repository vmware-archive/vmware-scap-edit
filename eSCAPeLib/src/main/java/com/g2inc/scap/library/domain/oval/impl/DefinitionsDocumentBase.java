package com.g2inc.scap.library.domain.oval.impl;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import com.g2inc.scap.library.domain.oval.AffectedItemContainer;
import com.g2inc.scap.library.domain.oval.AffectedItemFamilyEnum;
import com.g2inc.scap.library.domain.oval.AffectedPlatform;
import com.g2inc.scap.library.domain.oval.AffectedProduct;
import com.g2inc.scap.library.domain.oval.CheckEnumeration;
import com.g2inc.scap.library.domain.oval.Criteria;
import com.g2inc.scap.library.domain.oval.Criterion;
import com.g2inc.scap.library.domain.oval.DefinitionClassEnum;
import com.g2inc.scap.library.domain.oval.ExistenceEnumeration;
import com.g2inc.scap.library.domain.oval.ExtendDefinition;
import com.g2inc.scap.library.domain.oval.Metadata;
import com.g2inc.scap.library.domain.oval.OperEnum;
import com.g2inc.scap.library.domain.oval.OvalDatatype;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalFunctionEnum;
import com.g2inc.scap.library.domain.oval.OvalNote;
import com.g2inc.scap.library.domain.oval.OvalNotes;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalObjectFilter;
import com.g2inc.scap.library.domain.oval.OvalObjectParameter;
import com.g2inc.scap.library.domain.oval.OvalObjectReference;
import com.g2inc.scap.library.domain.oval.OvalObjectSet;
import com.g2inc.scap.library.domain.oval.OvalReference;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalStateParameter;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentLiteral;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentObject;
import com.g2inc.scap.library.domain.oval.OvalVariableComponentVariable;
import com.g2inc.scap.library.domain.oval.TypeEnum;
import com.g2inc.scap.library.schema.NameDoc;

/**
 * Implementation of an OVALDefinitionsDocument for OVAL 5.7
 *
 */
public abstract class DefinitionsDocumentBase extends OvalDefinitionsDocument
{
	public static final OperEnum[] validEnumsForString    =  {OperEnum.EQUALS, OperEnum.NOT_EQUAL, OperEnum.PATTERN_MATCH,
		OperEnum.CASE_INSENSITIVE_EQUALS, OperEnum.CASE_INSENSITIVE_NOT_EQUAL };
	public static final OperEnum[] validEnumsForBinary    =  {OperEnum.EQUALS, OperEnum.NOT_EQUAL };
	public static final OperEnum[] validEnumsForBoolean   =  {OperEnum.EQUALS, OperEnum.NOT_EQUAL };
	public static final OperEnum[] validEnumsForNumeric   =  {OperEnum.EQUALS, OperEnum.NOT_EQUAL,
		OperEnum.GREATER_THAN, OperEnum.GREATER_THAN_OR_EQUAL,
		OperEnum.LESS_THAN, OperEnum.LESS_THAN_OR_EQUAL};
    public static final OvalFunctionEnum[] validFunctions = {OvalFunctionEnum.arithmetic, OvalFunctionEnum.begin, OvalFunctionEnum.concat,
        OvalFunctionEnum.end, OvalFunctionEnum.escape_regex, OvalFunctionEnum.split, OvalFunctionEnum.substring,
        OvalFunctionEnum.time_difference, OvalFunctionEnum.regex_capture};

	public static final CheckEnumeration[] supportedCheckEnums = 
	{
		CheckEnumeration.ALL,
		CheckEnumeration.AT_LEAST_ONE,
		CheckEnumeration.NONE_EXIST,
		CheckEnumeration.NONE_SATISFY,
		CheckEnumeration.ONLY_ONE
	};
    
    public static final ExistenceEnumeration[] supportedExistenceEnums = new ExistenceEnumeration[]
    {
		ExistenceEnumeration.ALL_EXIST,
		ExistenceEnumeration.ANY_EXIST,
		ExistenceEnumeration.AT_LEAST_ONE_EXISTS,
		ExistenceEnumeration.NONE_EXIST,
		ExistenceEnumeration.ONLY_ONE_EXISTS
    };
    
    public static final AffectedItemFamilyEnum [] supportedAffectedFamilies = new AffectedItemFamilyEnum[]
    {
        AffectedItemFamilyEnum.unix,
        AffectedItemFamilyEnum.windows
    };

	public DefinitionsDocumentBase(Document doc)
	{
        super(doc);
	}
	
	public OperEnum[] getValidEnumsForString() {
		return validEnumsForString;
	}

	public OperEnum[] getValidEnumsForBinary() {
		return validEnumsForBinary;
	}

	public OperEnum[] getValidEnumsForBoolean() {
		return validEnumsForBoolean;
	}
	public OperEnum[] getValidEnumsForNumeric() {
		return validEnumsForNumeric;
	}
	
	@Override
	public OvalDefinition createDefinition(DefinitionClassEnum type)
	{		
		Element ovalDefElement = new Element("definition",getElement().getNamespace());

		ovalDefElement.setAttribute(new Attribute("id", getNextDefId()));
		ovalDefElement.setAttribute("version", "1");
		ovalDefElement.setAttribute("class", type.toString().toLowerCase());

		
		Element metadataElement = new Element("metadata", getElement().getNamespace());
		Element mdTitleElement = new Element("title", getElement().getNamespace());
		mdTitleElement.setText("Generated title");
		Element mdDescriptionElement = new Element("description", getElement().getNamespace());
		mdDescriptionElement.setText("Generated description");
				
		metadataElement.addContent((Element) mdTitleElement.clone());
		metadataElement.addContent((Element) mdDescriptionElement.clone());
		
		ovalDefElement.addContent((Element) metadataElement.clone());
				
		OvalDefinition def = new OvalDefinitionImpl(this);
		def.setElement(ovalDefElement);
		def.setRoot(getRoot());
		
		return def;
	}
    
	@Override
	public Set<String> getReferenceIds() {
		Set<String> docReferences = new HashSet<String>();
		List<OvalDefinition> defs = getOvalDefinitions();
		for(OvalDefinition od :defs) {
			List<OvalReference> refs = od.getReferences();
			for(OvalReference or : refs) {
				docReferences.add(or.getRefId());
			}
		}
		return docReferences;
	}

	@Override
	public OvalDefinition getDefinitionWrapper()
	{		
		return new OvalDefinitionImpl(this);
	}

	@Override
	public OvalObject getObjectWrapper()
	{
		return new OvalObjectImpl(this);
	}

	@Override
	public OvalState getStateWrapper()
	{
		return new OvalStateImpl(this);
	}

	@Override
	public OvalTest getTestWrapper()
	{
		return new OvalTestImpl(this);
	}

//	@Override
//	public OvalVariable getVariableWrapper()
//	{
//		return new OvalVariableImpl(this);
//	}

	@Override
	public OvalObjectParameter getObjectParameterWrapper()
	{
		return new OvalObjectParameterImpl(this);
	}

	@Override
	public OvalStateParameter getStateParameterWrapper()
	{
		return new OvalStateParameterImpl(this);
	}

	@Override
	public OvalReference getReferenceWrapper()
	{
		return new ReferenceImpl(this);
	}
	
	@Override
    public OvalNote getOvalNoteWrapper()
	{
		return new OvalNoteImpl(this);
	}
	
	@Override
    public OvalNotes getOvalNotesWrapper()
	{
		return new OvalNotesImpl(this);
	}	

	@Override
	public AffectedItemContainer getAffectedItemContainerWrapper()
	{
		return new AffectedItemContainerImpl(this);
	}

	@Override
	public AffectedPlatform getAffectedPlatformWrapper()
	{
		return new AffectedPlatformImpl(this);
	}

	@Override
	public AffectedProduct getAffectedProductWrapper()
	{
		return new AffectedProductImpl(this);
	}

	@Override
	public Metadata getMetadataWrapper()
	{
		return new MetadataImpl(this);
	}

	@Override
	public Criteria getCriteriaWrapper()
	{
		return new CriteriaImpl(this);
	}

	@Override
	public Criterion getCriterionWrapper()
	{
		return new CriterionImpl(this);
	}

	@Override
	public ExtendDefinition getExtendDefinitionWrapper()
	{
		return new ExtendDefinitionImpl(this);
	}

	@Override
	public OvalObjectReference getObjectReferenceWrapper()
	{
		return new OvalObjectReferenceImpl(this);
	}

	@Override
	public OvalObjectFilter getObjectFilterWrapper()
	{
		return new OvalObjectFilterImpl(this);
	}

	@Override
	public OvalObjectSet getObjectSetWrapper()
	{
		return new OvalObjectSetImpl(this);
	}

	@Override
	public OvalVariableComponentLiteral getLiteralVariableComponentWrapper()
	{
		return new OvalVariableComponentLiteralImpl(this);
	}

	@Override
	public OvalVariableComponentObject getObjectVariableComponentWrapper()
	{
		return new OvalVariableComponentObjectImpl(this);
	}

	@Override
	public OvalVariableComponentVariable getVariableComponentWrapper()
	{
		return new OvalVariableComponentVariableImpl(this);
	}

	@Override
	public OperEnum[] getOperationsForDatatype(OvalDatatype type) {
        return getOperationsForDatatype(type.getType());
	}

    @Override
    public OperEnum[] getOperationsForDatatype(TypeEnum type) {
		OperEnum[] validEnums = null;
		switch (type) {
		case BINARY:
			validEnums = validEnumsForBinary;
			break;
		case BOOLEAN:
			validEnums = validEnumsForBoolean;
			break;
		case EVR_STRING:
			validEnums = validEnumsForNumeric;
			break;
		case FILESET_REVISION:
			validEnums = validEnumsForNumeric;
			break;
		case FLOAT:
			validEnums = validEnumsForNumeric;
			break;
		case IOS_VERSION:
			validEnums = validEnumsForNumeric;
			break;
		case INT:
			validEnums = validEnumsForNumeric;
			break;
		case STRING:
			validEnums = validEnumsForString;
			break;
		case VERSION:
			validEnums = validEnumsForNumeric;
			break;
		case ENUMERATED:
			validEnums = validEnumsForString;
			break;
		default:
			throw new IllegalArgumentException("Unsupported datatype: " + type);
		}
		return validEnums;
	}
    
    @Override
    public OvalFunctionEnum[] getValidFunctions() {
        return validFunctions;
    }
    
    @Override
    public CheckEnumeration[] getSupportedCheckEnumerations()
    {
        return supportedCheckEnums;
    }

    @Override
    public ExistenceEnumeration[] getSupportedExistenceEnumerations()
    {
        return supportedExistenceEnums;
    }

    @Override
    public AffectedItemFamilyEnum[] getSupportedFamilies()
    {
        return supportedAffectedFamilies;
    }
    
    @Override
    public List<NameDoc> getDataTypeEnumerations() {
        List<NameDoc> result = getEnumerationValues("DatatypeEnumeration");
        if (result == null || result.size() == 0) {
            result = getEnumerationValues("SimpleDatatypeEnumeration");
        }
    	return result;
    }

}
