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
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;

import com.g2inc.scap.library.domain.SCAPDocumentClassEnum;

/**
 * Stores the results of a merge of two oval definitions documents.
 *
 */
public class MergeStats
{
	private int defsMerged;
	private int testsMerged;
	private int objectsMerged;
	private int statesMerged;
	private int variablesMerged;
	private List<String> warnings = new ArrayList<String>();

    /**
     * Constructor.
     */
    public MergeStats()
	{
    	this.initialize();
	}
	
	/**
	 * Get all of the warnings for this merge.
	 * 
	 * @return List of String objects containing the warnings.
	 */
	public List<String> getWarnings()
	{
		return this.warnings;
	}

	/**
	 * Replace all of the current warnings with a new list of warnings.
	 * 
	 * @param theWarnings List of warnings to replace the current list.
	 */
	public void setWarnings(List<String> theWarnings)
	{
		this.clearWarnings();
		this.warnings.addAll(theWarnings);
	}
	
	/**
	 * Clear all of the current warnings.
	 * 
	 */
	public void clearWarnings()
	{
		this.warnings.clear();
	}
	
	/**
	 * Appends a list of warnings to the current list.
	 * 
	 * @param theWarnings List of warnings to append to the current list.
	 */
	public void appendWarnings(List<String> theWarnings)
	{
		this.warnings.addAll(theWarnings);
	}
	
	/**
	 * Initializes to defaults.
	 */
	public void initialize()
	{
		defsMerged = 0;
		testsMerged = 0;
		objectsMerged = 0;
		statesMerged = 0;
		variablesMerged = 0;
		this.clearWarnings();
	}
	
	/**
	 * Returns the total number of elements merged.
	 * 
	 * @return int
	 */
	public int getTotalElementsMerged()
	{
		return defsMerged + testsMerged + objectsMerged + statesMerged + variablesMerged;
	}
	
	/**
	 * Returns the number of definitions merged.
	 * 
	 * @return int
	 */
	public int getDefsMerged()
	{
		return defsMerged;
	}
	
	/**
	 * Returns the number of tests merged.
	 * 
	 * @return int
	 */
	public int getTestsMerged()
	{
		return testsMerged;
	}
	
	/**
	 * Returns the number of objects merged.
	 * 
	 * @return int
	 */
	public int getObjectsMerged()
	{
		return objectsMerged;
	}
	
	/**
	 * Returns the number of states merged.
	 * 
	 * @return int
	 */
	public int getStatesMerged()
	{
		return statesMerged;
	}
	
	/**
	 * Returns the number of variables merged.
	 * 
	 * @return int
	 */
	public int getVariablesMerged()
	{
		return variablesMerged;
	}
	
	/**
	 * Sets the number of definitions merged.
	 * 
	 * @param defsMerged The number of definitions merged.
	 */
	public void setDefsMerged(int defsMerged)
	{
		this.defsMerged = defsMerged;
	}
	
	/**
	 * Sets the number of tests merged.
	 * 
	 * @param testsMerged The number of tests merged.
	 */
	public void setTestsMerged(int testsMerged)
	{
		this.testsMerged = testsMerged;
	}
	
	/**
	 * Sets the number of objects merged.
	 * 
	 * @param objectsMerged The number of objects merged.
	 */
	public void setObjectsMerged(int objectsMerged)
	{
		this.objectsMerged = objectsMerged;
	}
	
	/**
	 * Sets the number of states merged.
	 * 
	 * @param statesMerged The number of states merged.
	 */
	public void setStatesMerged(int statesMerged)
	{
		this.statesMerged = statesMerged;
	}
	
	/**
	 * Sets the number of variables merged.
	 * 
	 * @param variablesMerged The number of variables merged.
	 */
	public void setVariablesMerged(int variablesMerged)
	{
		this.variablesMerged = variablesMerged;
	}

	/**
	 * Increments the number of definitions merged by one.
	 */
	public void incrementDefsMerged()
	{
		defsMerged++;
	}

	/**
	 * Increments the number of tests merged by one.
	 */
	public void incrementTestsMerged()
	{
		testsMerged++;
	}
	
	/**
	 * Increments the number of objects merged by one.
	 */
	public void incrementObjectsMerged()
	{
		objectsMerged++;
	}
	
	/**
	 * Increments the number of states merged by one.
	 */
	public void incrementStatesMerged()
	{
		statesMerged++;
	}
	
	/**
	 * Increments the number of variables merged by one.
	 */
	public void incrementVariablesMerged()
	{
		variablesMerged++;
	}	
}
