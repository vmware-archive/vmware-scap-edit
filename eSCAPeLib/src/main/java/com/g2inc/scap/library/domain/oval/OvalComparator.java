package com.g2inc.scap.library.domain.oval;
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

import java.util.Comparator;

/**
 * This class contains the logic necessary to sort various oval constructs
 * in ways that make sense to those constructs.
 * 
 * @author ssill2
 *
 * @param <T>
 */
public class OvalComparator<T> implements Comparator<T>
{

    public int compare(T arg0, T arg1)
    {
        if (arg0 instanceof OvalDefinition && arg1 instanceof OvalDefinition)
        {
            OvalDefinition od0 = (OvalDefinition) arg0;
            OvalDefinition od1 = (OvalDefinition) arg1;

            String strId0 = od0.getId().substring(od0.getId().lastIndexOf(":") + 1);
            String strId1 = od1.getId().substring(od1.getId().lastIndexOf(":") + 1);

            Long lngId0 = new Long(Long.parseLong(strId0));
            Long lngId1 = new Long(Long.parseLong(strId1));

            return lngId0.compareTo(lngId1);
        } else if (arg0 instanceof OvalTest && arg1 instanceof OvalTest)
        {
            OvalTest ot0 = (OvalTest) arg0;
            OvalTest ot1 = (OvalTest) arg1;

            String strId0 = ot0.getId().substring(ot0.getId().lastIndexOf(":") + 1);
            String strId1 = ot1.getId().substring(ot1.getId().lastIndexOf(":") + 1);

            Long lngId0 = new Long(Long.parseLong(strId0));
            Long lngId1 = new Long(Long.parseLong(strId1));

            return lngId0.compareTo(lngId1);
        } else if (arg0 instanceof OvalObject && arg1 instanceof OvalObject)
        {
            OvalObject oo0 = (OvalObject) arg0;
            OvalObject oo1 = (OvalObject) arg1;

            String strId0 = oo0.getId().substring(oo0.getId().lastIndexOf(":") + 1);
            String strId1 = oo1.getId().substring(oo1.getId().lastIndexOf(":") + 1);

            Long lngId0 = new Long(Long.parseLong(strId0));
            Long lngId1 = new Long(Long.parseLong(strId1));

            return lngId0.compareTo(lngId1);
        } else if (arg0 instanceof OvalState && arg1 instanceof OvalState)
        {
            OvalState os0 = (OvalState) arg0;
            OvalState os1 = (OvalState) arg1;

            String strId0 = os0.getId().substring(os0.getId().lastIndexOf(":") + 1);
            String strId1 = os1.getId().substring(os1.getId().lastIndexOf(":") + 1);

            Long lngId0 = new Long(Long.parseLong(strId0));
            Long lngId1 = new Long(Long.parseLong(strId1));

            return lngId0.compareTo(lngId1);
        } else if (arg0 instanceof OvalVariable && arg1 instanceof OvalVariable)
        {
            OvalVariable ov0 = (OvalVariable) arg0;
            OvalVariable ov1 = (OvalVariable) arg1;

            String strId0 = ov0.getId().substring(ov0.getId().lastIndexOf(":") + 1);
            String strId1 = ov1.getId().substring(ov1.getId().lastIndexOf(":") + 1);

            Long lngId0 = new Long(Long.parseLong(strId0));
            Long lngId1 = new Long(Long.parseLong(strId1));

            return lngId0.compareTo(lngId1);
        } else if (arg0 instanceof AffectedItem && arg1 instanceof AffectedItem)
        {
            AffectedItem ai0 = (AffectedItem) arg0;
            AffectedItem ai1 = (AffectedItem) arg1;

            if (ai0.getType() == AffectedItemType.PLATFORM && ai1.getType() == AffectedItemType.PRODUCT)
            {
                // platforms are less than products
                return -1;
            } else if (ai0.getType() == AffectedItemType.PRODUCT && ai1.getType() == AffectedItemType.PLATFORM)
            {
                // platforms are less than products
                return 1;
            } else
            {
                return ai0.getValue().compareTo(ai1.getValue());
            }
        }

        return 0;
    }
}
