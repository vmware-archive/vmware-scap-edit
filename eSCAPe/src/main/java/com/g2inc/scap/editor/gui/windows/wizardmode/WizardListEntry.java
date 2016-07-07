package com.g2inc.scap.editor.gui.windows.wizardmode;
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

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.wizards.Wizard;

public class WizardListEntry implements Comparable
{
    private static Logger LOG = Logger.getLogger(WizardListEntry.class);
    
    private Wizard wiz = null;

    public WizardListEntry(Wizard nwiz)
    {
        setWizard(nwiz);
    }

    public Wizard getWizard()
    {
        return wiz;
    }

    public void setWizard(Wizard nwiz)
    {
        wiz = nwiz;
    }

    
    @Override
	public String toString()
    {
        return wiz.getWizardName() + " - " + wiz.getWizardShortDescription();
    }

    public String getToolTipText()
    {
        return wiz.getWizardDescription();
    }

    
    public int compareTo(Object o)
    {
        int ret = -1;

        try
        {

        if(o == null)
        {
           ret = 1;
        }
        else
        {
            if(!(o instanceof WizardListEntry))
            {
                ret = 1;
            }
            else
            {
                WizardListEntry other = (WizardListEntry) o;

                if(getWizard() == null && other.getWizard() == null)
                {
                    ret = 0;
                }
                else if(getWizard() != null && other.getWizard() == null)
                {
                    ret = 1;
                }
                else if(getWizard() == null && other.getWizard() != null)
                {
                    ret = -1;
                }
                else
                {
                    // both are non null must compare fields
                    if(getWizard().getName() == null && other.getWizard().getName() == null)
                    {
                        ret = 0;
                    }
                    else if(getWizard().getName() == null && getWizard().getName() != null)
                    {
                        ret = -1;
                    }
                    else
                    {
                        ret = getWizard().getName().compareTo(other.getWizard().getName());
                    }
                }
            }
        }
        }
        catch(Exception e)
        {
            LOG.error("Exception", e);
        }
        
        return ret;
    }
}
