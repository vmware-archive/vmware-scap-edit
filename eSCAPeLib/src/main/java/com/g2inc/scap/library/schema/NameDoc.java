package com.g2inc.scap.library.schema;
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

/**
 * Stores the name of of an element and it's associated schema documentation, if any.
 * 
 * @author gstrickland
 *
 */
public class NameDoc implements Comparable
{
    // name of the element or attribute
	private String name;
	
	// documentation from schema
	private String documentation;
	
	public NameDoc(String name, String documentation) {
		this.name = name;
		this.documentation = documentation;
	}
	
	/**
	 * Get the name of the element or attribute.
	 * 
	 * @return String
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Get the schema documentation for this element or attribute, if any.
	 * 
	 * @return String
	 */
	public String getDocumentation() {
		return documentation;
	}
    
	/**
	 * Overrides default method to return something meaningful.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Overrides the default equals method to properly compare this instance
	 * with another.
	 * 
	 * @param other An object to compare with.
	 * 
	 * @return boolean
	 */
    @Override
	public boolean equals(Object other)
    {
        if(other == null)
        {
            return false;
        }

		boolean result = false;
		
        if (this.name != null)
        {
            if (other instanceof String)
            {
				result = this.name.equals((String)other);
			}
            else if (other instanceof NameDoc)
            {
				String otherName = ((NameDoc) other).name;
				result = this.name.equals(otherName);
			}
		}
		return result;
	}

    /**
     * Overriding default method to return something meaningful.
     * 
     * @return int
     */
    @Override
	public int hashCode()
    {
		return (this.name == null ? super.hashCode() : this.name.hashCode());
	}
    
    /**
     * Implements method from Comparable interface.
     * 
     * @param other An object to compare with.
     * 
     * @return int
     */
    @Override
    public int compareTo(Object other)
    {
		int result = 0;

		if (this.name != null)
        {
			if (other instanceof String)
            {
				result = this.name.compareTo((String)other);
			}
            else if (other instanceof NameDoc)
            {
				NameDoc otherNameDoc = (NameDoc) other;
				if (otherNameDoc.name != null)
                {
					result = this.name.compareTo(otherNameDoc.name);
				}
			}
		}
		return result;
	}
}
