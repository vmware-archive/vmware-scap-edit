package com.g2inc.scap.library.domain.ocil;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.g2inc.scap.model.ocil.Generator;
import com.g2inc.scap.model.ocil.User;

public class GeneratorImpl extends ModelBaseImpl implements Generator {
	
	private static Logger LOG = Logger.getLogger(GeneratorImpl.class);
	public final static HashMap<String, Integer> GEN_ORDER = new HashMap<String, Integer>();
	static {
		GEN_ORDER.put("product_name", 0);
		GEN_ORDER.put("product_version", 1);
		GEN_ORDER.put("author", 2);
		GEN_ORDER.put("schema_version", 3);
		GEN_ORDER.put("timestamp", 4);
		GEN_ORDER.put("id-namespace", 5);
	}
	
	@Override
	public String getProductName() {		
		return this.getChildStringValue("product_name", this.element.getNamespace());
	}
	
	@Override
	public void setProductName(String value) {
            if (value != null) {
		this.setStringValueChild("product_name", value, getOrderMap());
            }
	}
	
	@Override
	public String getProductVersion() {		
		return this.getChildStringValue("product_version");
	}
	
	@Override
	public List<User> getAuthorList() {
		return getApiElementList(new ArrayList<User>(), "author", UserImpl.class);
	}
	
	@Override
	public void setAuthorList(List<User> list) {
		replaceApiList(list, getOrderMap(), "author");
	}
	
	@Override
	public void addAuthor(User user) {
		addApiElement(user, getOrderMap());
	}
	
	@Override
	public void removeAuthor(User user) {
		removeApiElement(user);		
	}
	
	@Override
	public void removeAuthor(List<User> list, User user) {
		removeApiElementFromList(list, user);
	}	
	
	@Override
	public void setProductVersion(String value) {
            if (value != null) {
		this.setStringValueChild("product_version", value, getOrderMap());
            }
	}
	
	@Override
	public BigDecimal getSchemaVersion() {
		BigDecimal decimal = null;
		String decimalString = this.getChildStringValue("schema_version");
		if (decimalString != null && decimalString.length() > 0) {
			try {
				decimal = new BigDecimal(decimalString);
			} catch (NumberFormatException e) {
				LOG.error("Schema version not in valid decimal format: " + decimalString, e);
			}
		}
		return decimal;
	}
	
	@Override
	public void setSchemaVersion(BigDecimal version) {
		String versionString = "";
		if (version != null) {
			versionString = version.toString();
		}
		this.setStringValueChild("schema_version", versionString, getOrderMap());
	}
	
	@Override
	public String getSchemaVersionString() {
		return this.getChildStringValue("schema_version");
	}
	
	@Override
	public void setSchemaVersionString(String versionString) {
		this.setStringValueChild("schema_version", versionString, getOrderMap());
	}
	
	@Override
	public String getTimeStampString() {
		return this.getChildStringValue("timestamp");
	}
	
	@Override
	public XMLGregorianCalendar getTimeStamp() {
		XMLGregorianCalendar timestamp = null;
		String timestampString = getTimeStampString();
		if (timestampString != null) {
			try {
				DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
				timestamp = datatypeFactory.newXMLGregorianCalendar(timestampString);
			} catch (DatatypeConfigurationException e) {
				throw new IllegalStateException("Could not interpret timestamp string: " + timestampString, e);
			}			
		}
		return timestamp;
	}
	
	@Override
	public void setTimeStamp(XMLGregorianCalendar date) {
		date.setTimezone(DatatypeConstants.FIELD_UNDEFINED); 
		date.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
		String dateString = date.toString();
		setTimeStampString(dateString);
	}
	
	@Override
	public void setTimeStamp() {
		try {
			XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
			setTimeStamp(date);
		} catch (DatatypeConfigurationException e) {
			throw new IllegalStateException("Can't configure DataType to set timestamp", e);
		}
	}
	
	@Override
	public void setTimeStampString(String dateString) {
		setStringValueChild("timestamp", dateString, getOrderMap());
	}
	
	@Override
	public HashMap<String, Integer> getOrderMap() {
	  return GEN_ORDER;
	}

	@Override
	public String getIdNamespace() {
		return this.getChildStringValue("id-namespace", this.element.getNamespace());
	}

	@Override
	public void setIdNamespace(String value) {
		setStringValueChild("id-namespace", value, getOrderMap());
	}

}
