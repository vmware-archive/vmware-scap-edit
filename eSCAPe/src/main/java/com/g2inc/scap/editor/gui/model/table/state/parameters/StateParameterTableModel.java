package com.g2inc.scap.editor.gui.model.table.state.parameters;
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

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.g2inc.scap.library.domain.oval.OvalStateParameter;

public class StateParameterTableModel implements TableModel
{
    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();

    private List data = null;

    public static final String [] COLUMN_HEADINGS =
            new String [] {
                            "Name",
                            "Operation",
                            "Datatype",
                            "Value"
                          };

    public static final int COLUMN_INDEX_NAME = 0;
    public static final int COLUMN_INDEX_OPERATION = 1;
    public static final int COLUMN_INDEX_DATATYPE = 2;
    public static final int COLUMN_INDEX_VALUE = 3;

    public StateParameterTableModel(List data)
    {
        this.data = data;
    }

    public int getRowCount()
    {
        return data.size();
    }

    public int getColumnCount()
    {
        return COLUMN_HEADINGS.length;
    }

    public String getColumnName(int column)
    {
        return COLUMN_HEADINGS[column];
    }

    public Class<?> getColumnClass(int column)
    {
        Class clazz;

        switch(column)
        {
            case COLUMN_INDEX_NAME:
                clazz =  "".getClass();
                break;
            case COLUMN_INDEX_OPERATION:
                clazz =  "".getClass();
                break;
            case COLUMN_INDEX_DATATYPE:
                clazz =  "".getClass();
                break;
            case COLUMN_INDEX_VALUE:
                clazz =  "".getClass();
                break;
            default:
                clazz = OvalStateParameter.class;
        }

        return clazz;
    }

    public boolean isCellEditable(int row, int column)
    {
        return false;
    }

    public Object getValueAt(int row, int column)
    {
        Object obj = null;

        OvalStateParameter osp = (OvalStateParameter) data.get(row);

        switch(column)
        {
            case COLUMN_INDEX_NAME:
                obj =  osp.getElementName();
                break;
            case COLUMN_INDEX_OPERATION:
                obj =  osp.getOperation();
                break;
            case COLUMN_INDEX_DATATYPE:
                obj =  osp.getDatatype();
                break;
            case COLUMN_INDEX_VALUE:
                if(osp.getVarRef() != null && osp.getVarRef().length() > 0)
                {
                    obj = "VarRef -> " + osp.getVarRef();
                }
                else
                {
                    obj =  osp.getValue();
                }
                break;
            default:
                obj = osp;
        }

        return obj;
    }

    public void setValueAt(Object value, int row, int column)
    {
        // TODO: implement this
//        OvalReference or = (ovalReference) data.get(row);
//        String prefix = null;
//        String uri = null;
//        switch(column)
//        {
//            case COLUMN_INDEX_ID:
//                prefix = (String)value;
//                uri = ns.getURI();
//                data.set(row, Namespace.getNamespace(prefix, uri));
//                break;
//            case COLUMN_INDEX_SOURCE:
//                prefix = ns.getPrefix();
//                uri = (String)value;
//                data.set(row, Namespace.getNamespace(prefix, uri));
//                break;
//        }
    }

    public void addTableModelListener(TableModelListener arg0)
    {
        listeners.add(arg0);
    }

    public void removeTableModelListener(TableModelListener arg0) 
    {
        listeners.remove(arg0);
    }

}
