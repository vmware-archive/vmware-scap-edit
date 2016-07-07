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

import com.g2inc.scap.library.schema.NameDoc;

/**
 * This class encapsulates some of the schema metadata specific object and state entities.
 */
public class OvalEntity extends NameDoc {
	
	private OvalDatatype datatype;
	private String datatypeString;
	private boolean nillable;
	private boolean required;

	public OvalEntity(String name, String documentation) {
		super(name, documentation);
	}

	/**
	 * Is this entity required.
	 * 
	 * @return boolean
	 */
	public boolean isRequired() 
	{
		return required;
	}

	/**
	 * Set whether this entity is required or not.
	 * 
	 * @param required
	 */
	public void setRequired(boolean required) 
	{
		this.required = required;
	}

	/**
	 * Is this entity nillable.
	 * @return boolean
	 */
	public boolean isNillable()
	{
		return nillable;
	}

	/**
	 * Set whether this entity is nillable or not.
	 * 
	 * @param nillable
	 */
	public void setNillable(boolean nillable) {
		this.nillable = nillable;
	}

	/**
	 * Set this entities datatype.
	 * 
	 * @param datatypeString
	 */
	public void setDatatypeString(String datatypeString) {
		this.datatypeString = datatypeString;
	}

	/**
	 * Get the entity's datatype as a string.
	 * 
	 * @return String
	 */
	public String getDatatypeString() {
		return datatypeString;
	}

	/**
	 * Get the entity datatype as an enum.
	 * 
	 * @return OvalDatatype
	 */
	public OvalDatatype getDatatype() {
		return datatype;
	}

	/**
	 * Set the entity datatype as an enum.
	 * 
	 * @param datatype
	 */
	public void setDatatype(OvalDatatype datatype) {
		this.datatype = datatype;
	}
}
