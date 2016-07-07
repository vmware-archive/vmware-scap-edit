package com.g2inc.scap.editor.gui.model.table.merger.oval;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class OvalMergerTableModel extends AbstractTableModel
{
    private List<String> data = null;

    public static final String[] COLUMN_HEADINGS = {
        "Message Text"
    };

    public static final int COLUMN_INDEX_MESSAGE = 0;

    public OvalMergerTableModel(List data)
    {
        this.data = data;
    }

    @Override
    public int getRowCount()
    {
        return data.size();
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
    public Class<?> getColumnClass(int column)
    {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        Object obj = null;
        if (row > data.size() || row < 0)
        {
            throw new IllegalStateException("getValueAt called with invalid row " + row + ", list size is " + data.size());
        }
        String message = data.get(row);
        switch(column)
        {
            case COLUMN_INDEX_MESSAGE:
                obj =  message;
                break;
            default:
                throw new IllegalStateException("getValueAt called with invalid col number " + row);
        }
        return obj;
    }
}