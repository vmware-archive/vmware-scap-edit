package com.g2inc.scap.library.domain.xccdf.impl;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.xccdf.Status;
import com.g2inc.scap.library.domain.xccdf.StatusEnum;

public class StatusImpl extends SCAPElementImpl implements Status {
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public String getDate() {
		return this.element.getAttributeValue("date");
	}

	public void setDate(String date) {
		this.element.setAttribute("date", date);
	}
	
	public void setDate(Date date) {
		setDate(DATE_FORMAT.format(date));
	}

	public StatusEnum getStatus() {
		StatusEnum statusEnum = null;
		String statusString = element.getText();
		if (statusString != null && statusString.length() > 0) {
			statusEnum = StatusEnum.valueOf(statusString);
		}
		return statusEnum;
	}

	public void setStatus(StatusEnum status) {
		if (status != null) {
			element.setText(status.toString());
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		StatusEnum statusEnum = getStatus();
		if (statusEnum != null) {
			sb.append(statusEnum.toString());
			String date = getDate();
			if (date != null) {
				sb.append(" - ");
				sb.append(date);
			}
		}
		return sb.toString();
	}

}
