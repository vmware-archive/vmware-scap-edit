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

import java.util.Calendar;
import java.util.HashMap;

import org.jdom.Element;

import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.util.DateUtility;

/**
 * Represents a generator element under a cpe-list element.
 */
public abstract class CPEListGenerator extends SCAPElementImpl {
	public final static HashMap<String, Integer> CPE_ORDER = new HashMap<String, Integer>();
	static {
		CPE_ORDER.put("product_name", 0);
		CPE_ORDER.put("product_version", 1);
		CPE_ORDER.put("schema_version", 2);
		CPE_ORDER.put("timestamp", 3);
	}

	public CPEListGenerator() {
		super();
	}

	/**
	 * Get the product name.
	 * 
	 * @return String
	 */
	public String getProductName() {
		String ret = null;

		if (getElement() != null) {
			Element e = getElement().getChild("product_name",
					getElement().getNamespace());

			if (e != null) {
				ret = e.getText();
			}
		}

		return ret;
	}

	/**
	 * Set the product name.
	 * 
	 * @param prodName
	 */
	public void setProductName(String prodName) {
		if (getElement() != null) {
			Element e = getElement().getChild("product_name",
					getElement().getNamespace());

			boolean insertElement = false;

			if (e == null) {
				e = new Element("product_name", getElement().getNamespace());
				insertElement = true;
			}

			e.setText(prodName);

			if (insertElement) {
				insertChild(e, CPE_ORDER);
			}
		}
	}

	/**
	 * Get the product version.
	 * 
	 * @return String
	 */
	public String getProductVersion() {
		String ret = null;

		if (getElement() != null) {
			Element e = getElement().getChild("product_version",
					getElement().getNamespace());

			if (e != null) {
				ret = e.getText();
			}
		}

		return ret;
	}

	/**
	 * Set the product version.
	 * 
	 * @param ver
	 */
	public void setProductVersion(String ver) {
		if (getElement() != null) {
			Element e = getElement().getChild("product_version",
					getElement().getNamespace());

			boolean insertElement = false;

			if (e == null) {
				e = new Element("product_version", getElement()
						.getNamespace());
				insertElement = true;
			}

			e.setText(ver);

			if (insertElement) {
				insertChild(e, CPE_ORDER);
			}
		}
	}

	/**
	 * Get the schema version.
	 * 
	 * @return CPESchemaVersion
	 */
	public CPESchemaVersion getSchemaVersion() {
		CPESchemaVersion ret = null;

		if (getElement() != null) {
			Element e = getElement().getChild("schema_version",
					getElement().getNamespace());

			if (e != null) {
				ret = new CPESchemaVersion(e.getText());
			}
		}

		return ret;
	}

	/**
	 * Set the schema version.
	 * 
	 * @param ver
	 */
	public void setSchemaVersion(CPESchemaVersion ver) {
		if (getElement() != null) {
			Element e = getElement().getChild("schema_version",
					getElement().getNamespace());

			boolean insertElement = false;

			if (e == null) {
				e = new Element("schema_version", getElement().getNamespace());
				insertElement = true;
			}

			e.setText(ver.getVersion());

			if (insertElement) {
				insertChild(e, CPE_ORDER);
			}
		}
	}

	/**
	 * Get the timestamp.
	 * 
	 * The format of the date in the xml according to the schema is
	 * yyyy-mm-ddThh:mm:ss
	 * 
	 * @return Calendar
	 */
	public Calendar getTimestamp() {
		Calendar ret = null;

		if (getElement() != null) {
			Element e = getElement().getChild("timestamp",
					getElement().getNamespace());

			if (e != null) {
				String cpeDateTime = e.getText();

				try {
					ret = DateUtility
							.getCalendarFromCPEGeneratorDateTime(cpeDateTime);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		}
		return ret;
	}

	/**
	 * Set the timestamp.
	 * 
	 * @param dateTime
	 */
	public void setTimestamp(Calendar dateTime) {
		if (getElement() != null) {
			Element e = getElement().getChild("timestamp",
					getElement().getNamespace());

			boolean insertElement = false;

			if (e == null) {
				e = new Element("timestamp", getElement().getNamespace());
				insertElement = true;
			}

			String xsdTime = null;

			try {
				xsdTime = DateUtility
						.getCPEGeneratorDateTimeFromCalendar(dateTime);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (xsdTime != null) {
				e.setText(xsdTime);

				if (insertElement) {
					insertChild(e, CPE_ORDER);
				}
			}
		}
	}
}
