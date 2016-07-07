package com.g2inc.scap.library.domain.cpe;
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

import com.g2inc.scap.library.domain.SCAPElementImpl;

/**
 * Represents the check element under a cpe-item element in an cpe dictionary doc.
 */
public abstract class CPEItemCheck extends SCAPElementImpl
{
	/**
	 * Get the system attribute for this check.
	 * 
	 * @return CPEItemCheckSystemType
	 */
	public CPEItemCheckSystemType getSystem()
	{
		CPEItemCheckSystemType system = null;
		
		if(getElement() != null)
		{
			String systemString = getElement().getAttributeValue("system");

            if(systemString != null)
            {
                system = CPEItemCheckSystemType.getEnum(systemString);
            }
            else
            {
                system = CPEItemCheckSystemType.OVAL5;
            }
        }
		
		return system;
	}
	
	/**
	 * Set the system attribute for this check.
	 * 
	 * @param system
	 */
	public void setSystem(CPEItemCheckSystemType system)
	{
		if(getElement() != null)
		{
			getElement().setAttribute("system", system.getSystemURI());
		}
	}
	
	/**
	 * Get the href of the file containing the check.
	 * 
	 * @return String
	 */
	public String getHref()
	{
		String href = null;
		
		if(getElement() != null)
		{
			href = getElement().getAttributeValue("href");
		}
		
		return href;
	}
	
	/**
	 * Set the href of the file containing the check.
	 *
	 * @param href
	 */
	public void setHref(String href)
	{
        if(href != null)
        {
            getElement().setAttribute("href", href);
        }
        else
        {
            getElement().removeAttribute("href");
        }
	}
	
	/**
	 * Get the id of the check in the file referred to by href.
	 *
	 * @return String
	 */
	public String getCheckId()
	{
		String id = null;
		
		if(getElement() != null)
		{
			id = getElement().getValue();
		}
		
		return id;
	}
	
	/**
	 * Set the id of the check in the file referred to by href.
	 *
	 * @param id
	 */
	public void setCheckId(String id)
	{
		if(getElement() != null)
		{
			getElement().setText(id);
		}
	}

    @Override
    public String toString()
    {
        return "(" + getSystem().toString() + ") " + getHref() + " - " + getCheckId();
    }
}
