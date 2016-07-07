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

import com.g2inc.scap.library.domain.xccdf.ProfileChild;
import com.g2inc.scap.library.domain.xccdf.Remark;
import com.g2inc.scap.library.domain.xccdf.RoleEnum;
import com.g2inc.scap.library.domain.xccdf.RoleSeverity;
import com.g2inc.scap.library.domain.xccdf.SeverityEnum;

public class ProfileRefineRuleImpl extends ProfileChildImpl implements ProfileChild, RoleSeverity
{

    public final static HashMap<String, Integer> PROFILEREFINERULE_ORDER = new HashMap<String, Integer>();

    static
    {
        PROFILEREFINERULE_ORDER.put("remark", 0);
    }

    public String getSelector()
    {
        return element.getAttributeValue("selector");
    }

    public void setSelector(String selector)
    {
        element.setAttribute("selector", selector);
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
        if(role != null)
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

    @Override
    public SeverityEnum getSeverity()
    {
        String severity = element.getAttributeValue("severity");
        return (severity == null ? getDefaultSeverity() : SeverityEnum.valueOf(severity));
    }

    @Override
    public void setSeverity(SeverityEnum severity)
    {
        if(severity != null)
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

    public Double getWeight()
    {
        Double weight = null;
        String weightString = this.element.getAttributeValue("weight");
        if (weightString != null)
        {
            weight = Double.parseDouble(weightString);
        }
        return weight;
    }

    public void setWeight(Double weight)
    {
        this.element.setAttribute("weight", weight.toString());
    }

    public List<Remark> getRemarkList()
    {
        return getSCAPElementIntList("remark", Remark.class);
    }

    public void setRemarkList(List<Remark> list)
    {
        replaceList(list, PROFILEREFINERULE_ORDER, "remark");
    }

    @Override
    public String toString()
    {
        return "refine-rule id-ref=" + getIdRef();
    }
}
