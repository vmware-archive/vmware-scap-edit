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

import java.util.HashMap;
import java.util.List;

import com.g2inc.scap.library.domain.xccdf.Check;
import com.g2inc.scap.library.domain.xccdf.ComplexCheck;
import com.g2inc.scap.library.domain.xccdf.Fix;
import com.g2inc.scap.library.domain.xccdf.FixText;
import com.g2inc.scap.library.domain.xccdf.Ident;
import com.g2inc.scap.library.domain.xccdf.RoleEnum;
import com.g2inc.scap.library.domain.xccdf.RoleSeverity;
import com.g2inc.scap.library.domain.xccdf.Rule;
import com.g2inc.scap.library.domain.xccdf.SeverityEnum;

public class RuleImpl extends SelectableItemImpl implements Rule, RoleSeverity
{

    public final static HashMap<String, Integer> RULE_ORDER = new HashMap<String, Integer>();

    static
    {
        RULE_ORDER.put("status", 0);
        RULE_ORDER.put("version", 1);
        RULE_ORDER.put("title", 2);
        RULE_ORDER.put("description", 3);
        RULE_ORDER.put("warning", 4);
        RULE_ORDER.put("question", 5);
        RULE_ORDER.put("reference", 6);
        // end of ITEM_ORDER fields
        RULE_ORDER.put("rationale", 7);
        RULE_ORDER.put("platform", 8);
        RULE_ORDER.put("requires", 9);
        RULE_ORDER.put("conflicts", 10);
        // end of SELECTABLEITEM_ORDER fields
        RULE_ORDER.put("ident", 11);
        RULE_ORDER.put("impact-metric", 12);
        RULE_ORDER.put("profile-note", 13);
        RULE_ORDER.put("fixtext", 14);
        RULE_ORDER.put("fix", 15);
		RULE_ORDER.put("check", 16);
		RULE_ORDER.put("complex-check", 17);
		RULE_ORDER.put("signature", 18);
	}

    public List<Ident> getIdentList()
    {
        return getSCAPElementIntList("ident", Ident.class);
    }

    public void setIdentList(List<Ident> identList)
    {
        replaceList(identList, RULE_ORDER, "ident");
    }

    public List<Check> getCheckList()
    {
        return getSCAPElementIntList("check", Check.class);
    }

    public void setCheckList(List<Check> checkList)
    {
        replaceList(checkList, RULE_ORDER, "check");
    }

    public void addCheck(Check check)
    {
        insertChild(check, RULE_ORDER, -1);
    }

    @Override
    public RoleEnum getRole()
    {
        String role = element.getAttributeValue("role");
        return (role == null ? getDefaultRole() : RoleEnum.valueOf(role));
    }

    @Override
    public void setRole(RoleEnum role)
    {
        if (role != null)
        {
            element.setAttribute("role", role.toString());
        }
        else
        {
            element.removeAttribute("role");
        }
    }

    @Override
    public RoleEnum getDefaultRole()
    {
        return RoleEnum.full;
    }

	// there can never be more than one complex-check in a Rule, so
	// no need to treat it as a list.
	public ComplexCheck getComplexCheck()
	{
	    return getSCAPElementInt("complex-check", ComplexCheck.class);
	}	
	public void setComplexCheck(ComplexCheck complexCheck)
	{
	    insertChild(complexCheck, RULE_ORDER, -1);
	}
	
    @Override
    public SeverityEnum getSeverity()
    {
        String severity = element.getAttributeValue("severity");
        return (severity == null ? getDefaultSeverity() : SeverityEnum.valueOf(severity));
    }

    @Override
    public void setSeverity(SeverityEnum severity)
    {
        if (severity != null)
        {
            element.setAttribute("severity", severity.toString());
        }
        else
        {
            element.removeAttribute("severity");
        }
    }

    @Override
    public SeverityEnum getDefaultSeverity()
    {
        return SeverityEnum.unknown;
    }

    public boolean isMultiple()
    {
        boolean result = false;
        String multiple = element.getAttributeValue("multiple");
        if (multiple != null && (multiple.equals("1") || multiple.equalsIgnoreCase("true")))
        {
            result = true;
        }
        return result;
    }

    public void setMultiple(boolean multiple)
    {
        element.setAttribute("multiple", Boolean.toString(multiple));
    }

    public List<FixText> getFixTextList()
    {
        return getSCAPElementIntList("fixtext", FixText.class);
    }

    public void setFixTextList(List<FixText> list)
    {
        replaceList(list, RULE_ORDER, "fixtext");
    }

    public List<Fix> getFixList()
    {
        return getSCAPElementIntList("fix", Fix.class);
    }

    public void setFixList(List<Fix> list)
    {
        replaceList(list, RULE_ORDER, "fix");
    }

    @Override
    public String toString()
    {
        return "Rule id=" + getId();
    }

    @Override
    public HashMap<String, Integer> getOrderMap()
    {
        return RULE_ORDER;
    }
}
