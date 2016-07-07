package com.g2inc.scap.library.domain.oval;
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

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.schema.NameDoc;

/**
 * Represents the different possible data types in oval.
 */
public class OvalDatatype
{
	private static final Logger log = Logger.getLogger(OvalDatatype.class);
	private TypeEnum type;
	private List<NameDoc> enumValues;

	/**
	 * Constructor.
	 * 
	 * @param type
	 */
    public OvalDatatype(TypeEnum type)
    {
        this.type = type;
    }

    /**
     * Constructor.
     * 
     * @param schemaType
     * @param valueMap
     */
	public OvalDatatype(String schemaType, Map<String, List<NameDoc>> valueMap)
	{
		int colonOffset = schemaType.indexOf(":");
		if (colonOffset != -1) {
			schemaType = schemaType.substring(0, colonOffset);
		}
		if (schemaType.equals("EntityObjectStringType") || schemaType.equals("EntityStateStringType")) {
			type = TypeEnum.STRING;
		} else if (schemaType.equals("EntityObjectBinaryType") || schemaType.equals("EntityStateBinaryType")) {
			type = TypeEnum.BINARY;
		} else if (schemaType.equals("EntityObjectBoolType") || schemaType.equals("EntityStateBoolType")) {
			type = TypeEnum.BOOLEAN;
		} else if (schemaType.equals("EntityObjectFloatType") || schemaType.equals("EntityStateFloatType")) {
			type = TypeEnum.FLOAT;
		} else if (schemaType.equals("EntityObjectIntType") || schemaType.equals("EntityStateIntType")) {
			type = TypeEnum.INT;
		} else if (schemaType.equals("EntityObjectAnyType") || schemaType.equals("EntityStateAnyType") ||
            schemaType.equals("EntityObjectAnySimpleType") || schemaType.equals("EntityStateAnySimpleType")) {
			type = TypeEnum.ANY;
		} else if (schemaType.equals("version")) {
			type = TypeEnum.VERSION;
		} else {
			enumValues = valueMap.get(schemaType);
			if (enumValues == null) {
				type = TypeEnum.STRING;
				log.info("Can't find type for entity " + schemaType);
			} else {
				type = TypeEnum.ENUMERATED;
			}
		}
	}

	/**
	 * Get the type.
	 * 
	 * @return TypeEnum
	 */
	public TypeEnum getType() {
		return type;
	}

	/**
	 * Get the list of XML datatypes associated with this enum.
	 * 
	 * @return List<NameDoc>
	 */
	public List<NameDoc> getEnumValues()
	{
		return enumValues;
	}
}
