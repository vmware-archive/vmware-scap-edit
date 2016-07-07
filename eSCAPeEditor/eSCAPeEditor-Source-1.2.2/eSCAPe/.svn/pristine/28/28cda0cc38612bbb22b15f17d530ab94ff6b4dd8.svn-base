package com.g2inc.scap.editor.gui.model.table.test.states;

/* ESCAPE Software   Copyright 2010 G2, Inc. - All rights reserved.
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

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalTest;

public class OvalTestStatesTableModel extends DefaultTableModel
{    

    private List<OvalState> states = null;

    public static final String [] COLUMN_HEADINGS =
            new String [] {
                            "State"
                          };

    public static final int COLUMN_INDEX_STATE = 0;

    public OvalTestStatesTableModel(OvalTest ovalTest)
    {
        super();
        
        if(ovalTest == null)
        {
            throw new RuntimeException("OvalTest is NULL!");
        }

        states = ovalTest.getStates();
    }

    @Override
    public int getRowCount()
    {
        if(states == null)
        {
            return 0;
        }
        
        return states.size();
    }
    
    @Override
    public int getColumnCount()
    {
        return COLUMN_HEADINGS.length;
    }

    @Override
    public String getColumnName(int column)
    {
        return COLUMN_HEADINGS[column];
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }

    @Override
    public Class getColumnClass(int col)
    {
        Class clazz = null;

        switch(col)
        {
            case COLUMN_INDEX_STATE:
                clazz = OvalState.class;
                break;
        }

        return clazz;
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        Object obj = null;

        OvalState os = (OvalState) states.get(row);

        switch(column)
        {
            case COLUMN_INDEX_STATE:
                obj =  os;
                break;
        }

        return obj;
    }

    public List<OvalState> getStates()
    {
        return states;
    }

    @Override
    public void removeRow(int row)
    {
        states.remove(row);
        fireTableRowsDeleted(row, row);
    }
    @Override
    public void insertRow(int row, Object[] rowData)
    {
       OvalState os = (OvalState) rowData[0];
       states.add(row, os);
       fireTableRowsInserted(row, row);
    }

    @Override
    public void insertRow(int row, Vector rowData)
    {
        insertRow(row, rowData.toArray());
    }

    @Override
    public void addRow(Object[] rowData)
    {
       OvalState os = (OvalState) rowData[0];
       states.add(os);
       fireTableDataChanged();
    }
}
