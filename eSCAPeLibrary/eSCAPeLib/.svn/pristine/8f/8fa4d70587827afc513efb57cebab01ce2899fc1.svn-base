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

import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.xccdf.Fix;
import com.g2inc.scap.library.domain.xccdf.RatingEnum;
import com.g2inc.scap.library.domain.xccdf.StrategyEnum;
import com.g2inc.scap.library.util.CommonUtil;

public class FixImpl extends TextWithSubImpl implements Fix {
	
	public String getText() {
		return element.getText();
	}
	
	public void setText(String text) {
		element.setText(text);
	}
	
	public boolean getReboot() {
		boolean result = false;
		String reboot = element.getAttributeValue("reboot");
		if (reboot != null && (reboot.equals("1") || reboot.equalsIgnoreCase("true"))) {
			result = true;
		}
		return result;
	}
	
	public void setReboot(boolean reboot) {
		element.setAttribute("reboot", Boolean.toString(reboot));
	}
	
	public StrategyEnum getStrategy() {
		String strategy = element.getAttributeValue("strategy");
		return (strategy == null ? StrategyEnum.unknown : StrategyEnum.valueOf(strategy));
	}
	
	public void setStrategy(StrategyEnum strategy) {
		element.setAttribute("strategy", strategy.toString());
	}
	
	public RatingEnum getDisruption() {
		String disruption = element.getAttributeValue("disruption");
		return (disruption == null ? RatingEnum.unknown : RatingEnum.valueOf(disruption));
	}
	
	public void setDisruption(RatingEnum disruption) {
		element.setAttribute("disruption", disruption.toString());
	}
	
	public RatingEnum getComplexity() {
		String complexity = element.getAttributeValue("complexity");
		return (complexity == null ? RatingEnum.unknown : RatingEnum.valueOf(complexity));
	}
	
	public void setComplexity(RatingEnum complexity) {
		element.setAttribute("complexity", complexity.toString());
	}
	
	public String getSystem() {
		return element.getAttributeValue("system");
	}
	
	public void setSystem(String system) {
		element.setAttribute("system", system);
	}
	
	public String getPlatform() {
		return element.getAttributeValue("platform");
	}
	
	public void setPlatform(String platform) {
		element.setAttribute("platform", platform);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: " + (getId() == null ? "null" : getId()));
		String text = CommonUtil.escapeXML(getText());
		text = CommonUtil.truncateWithEllipsis(text, 100);
		sb.append(";").append(text);
		return sb.toString();
	}
}
