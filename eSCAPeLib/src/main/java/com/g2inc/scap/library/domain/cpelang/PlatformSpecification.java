package com.g2inc.scap.library.domain.cpelang;
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

import java.util.HashMap;
import java.util.List;

import org.jdom.Document;

import com.g2inc.scap.library.domain.SCAPDocument;

/**
 * Defines what an CPE Language Platform Specification looks like as a SCAPDocument. Note that the
 * <platform-specification> element can occur in other contexts than the root element of an xml
 * document; it also can occur as a child of the XCCDF <Benchmark> element. 
 * 
 * @author gstrickland
 * @see com.g2inc.scap.library.domain.SCAPDocument
 */
public class PlatformSpecification extends SCAPDocument
{
	
	public final static HashMap<String, Integer> CPE_PLATFORM_ORDER = new HashMap<String, Integer>();	
	static
	{
		CPE_PLATFORM_ORDER.put("platform", 0);
	}
	
	public PlatformSpecification(Document doc)
	{
		super(doc);
	}
	
	public PlatformSpecification()
	{
	}
	
	public List<Platform> getPlatformList() {
		return getSCAPElementList("platform", Platform.class);
	}
	public void setPlatformList(List<Platform> list) {
		replaceList(list, CPE_PLATFORM_ORDER, "platform");
	}
	public void addPlatform(Platform platform) {
		insertChild(platform, CPE_PLATFORM_ORDER, -1);
	}

	public Platform createPlatform() {
		return (Platform) createSCAPElement("platform", Platform.class);
	}
	
    public Title createTitle() {
    	return (Title) createSCAPElement("title", Title.class);
    }
    
    public Remark createRemark() {
    	return (Remark) createSCAPElement("remark", Remark.class);
    }
    
    public LogicalTest createLogicalTest() {
    	return (LogicalTest) createSCAPElement("logical-test", LogicalTest.class);
    }
    
    public FactRef createFactRef() {
    	return (FactRef) createSCAPElement("fact-ref", FactRef.class);
    }
    
    @Override
    public String toString()
    {
        return "Platform-Specification " + getId();
    }
    
    @Override
    public String validateSymantically() throws Exception
    {
    	return null;
    }
    
    @Override
    public void close()
    {
    	setElement(null);
    	setDoc(null);
    }
}
