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
 * Instances of this class can be used as keys in maps.  It's components are
 * the oval platform and the name of the element.
 * 
 * @author gstrickland
 */
public class PlatformNameKey implements Comparable {
	
    // the oval platform, e.g. aix
	private String platform;
	
	// the element name
	private String name;
	
	public PlatformNameKey(String platform, String name) {
		this.platform = platform;
		this.name = name;
	}

	/**
	 * Get the platform component of this key.
	 * 
	 * @return String
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * Set the platform component of this key.
	 * 
	 * @param platform
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * Get the element name component of this key.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the element name component of this key.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Overriding default equals method to be more meaningful in comparisons.
     * 
     * @param other An object we are comparing with.
     * 
     * @return boolean
     */
	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof PlatformNameKey) {
			PlatformNameKey otherPNK = (PlatformNameKey) other;
			result = (platform.equals(otherPNK.platform) &&
					name.equals(otherPNK.name));
		}
		return result;
	}
	
	/**
	 * Overriding default method to be more meaningful.
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		return name.hashCode() + platform.hashCode();		
	}
	
	/**
	 * Implementing this method as required by Comparable interface.
	 * 
	 * @param other An object we are comparing with.
	 * @return int
	 */
	public int compareTo(Object other) {
		int result = 0;
		if (other instanceof PlatformNameKey) {
			PlatformNameKey otherPNK = (PlatformNameKey) other;
			result = platform.compareTo(otherPNK.platform);
			if (result == 0) {
				result = name.compareTo(otherPNK.name);
			}
		}
		return result;
	}
}
