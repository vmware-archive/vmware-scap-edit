package com.g2inc.scap.library.domain;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

/**
 * This class handles dealing with supported languages in the model.  This will likely be
 * expanded quite a bit if there is more demand to internationalize.
 */
public class SupportedInputLanguages
{
     private static String[] langs;

     /**
      * Static initialization.
      */

     private static void initLangs()
     {
         ArrayList langsList = new ArrayList<String>();
         HashSet<String> tmpSet = new HashSet<String>();

         Locale[] locales = Locale.getAvailableLocales();

         for(int x = 0; x < locales.length; x++)
         {
             Locale l = locales[x];

             // country code
             String cCode = l.getCountry();

             // language code
             String lCode = l.getLanguage();

             if(lCode != null && !lCode.equals(""))
             {
                 if(cCode != null && !cCode.equals(""))
             {
                     tmpSet.add(lCode + "-" + cCode.toUpperCase());
                 }
                 else
                 {
                    tmpSet.add(lCode);
                 }
             }
         }

         langsList.addAll(tmpSet);
         Collections.sort(langsList);
         langs = new String[langsList.size()];
         langsList.toArray(langs);
     }

     /**
      * For now the default will be english("en-US").
      *
      * @return String
      */
     public static String getDefault()
     {
         return "en-US";
     }

     /**
      * Tell us if the language specified is a supported language
      * in any of the locals on this machine.
      *
      * @param langFromXML The xml:lang language code
      *
      * @return boolean
      */
     public boolean isSupportedLanguage(String langFromXML)
     {
         boolean ret = false;

         if(langFromXML != null)
         {
             int underscoreLoc = langFromXML.indexOf("_");
             int dashLoc = langFromXML.indexOf("-");

             String lcLang = null;

             if(underscoreLoc != -1)
             {
                 // has a region or country code
                 lcLang = langFromXML.substring(0,underscoreLoc).toLowerCase();
             }
             else if(dashLoc != -1)
             {
                 // has a region or country code
                 lcLang = langFromXML.substring(0,dashLoc).toLowerCase();
             }
             else
             {
                 // must be a two character lang code
                 lcLang = langFromXML.toLowerCase().trim();
             }

             for (int x = 0; x < langs.length; x++)
             {
                 if (lcLang.equals(langs[x]))
                 {
                     ret = true;
                     break;
                 }
             }
         }

         return ret;
     }

    /**
     * Return a list of supported input languages on this system.

     * @return String []
     */
    public static String[] getLangs()
    {
        if(langs == null)
        {
            initLangs();
        }
        
        return langs;
    }
}
