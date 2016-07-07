package com.g2inc.scap.editor.gui.dialogs.dependency;
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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalElement;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVariable;

/**
 * This dialog will be shown when an attempt is made to delete an item from
 * the definitions document.  The items which depend on the item being deleted
 * will be shown here.   The user will be able to copy the list to the system
 * clipboard for review.
 *
 * @author ssill
 */
public class DependencyNotificationDialog extends javax.swing.JDialog implements ActionListener
{
    private static Logger log = Logger.getLogger(DependencyNotificationDialog.class);

    private void initButtons()
    {
        okButton.addActionListener(this);
        copyButton.addActionListener(this);
    }

    private void initTable()
    {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ActionMap amap = table.getActionMap();

        amap.put(MyTransferHandler.getCopyAction().getValue(Action.NAME), MyTransferHandler.getCopyAction());

        MyTransferHandler mth = new MyTransferHandler(table);
        table.setTransferHandler(mth);
    }

    private void initComponents2()
    {
        initTable();
        initButtons();
    }

    /** Creates new form DependencyNotificationDialog */
    public DependencyNotificationDialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        initComponents2();
    }

    public String getMessage()
    {
        return messageLabel.getText();
    }

    public void setMessage(String message)
    {
        this.messageLabel.setText(message);
    }

    public void setDependencies(List<OvalElement> depends)
    {
        MyTableModel model = new MyTableModel(depends);
        table.setModel(model);
    }

    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();

        if(src == okButton)
        {
            setVisible(false);
        }
        else if(src == copyButton)
        {
            MyTransferHandler mth = (MyTransferHandler) table.getTransferHandler();
            
            Transferable t = mth.createTransferable(table);

            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

            c.setContents(t, new MyClipboardOwner());
        }
    }

    private class MyClipboardOwner implements ClipboardOwner
    {
        public void lostOwnership(Clipboard arg0, Transferable arg1)
        {
        }
    }

    private class MyTableModel extends DefaultTableModel
    {
        List<OvalElement> data = null;
        
        public final String [] COLUMN_HEADINGS =
            new String [] {
                            "Type",
                            "Id"
                          };
        
        public final int COLUMN_INDEX_TYPE = 0;
        public final int COLUMN_INDEX_ID = 1;

        private MyTableModel(List<OvalElement> data)
        {
            super();
            this.data = data;
        }

        @Override
        public int getRowCount()
        {
            if(data == null)
            {
                return 0;
            }

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
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }

        @Override
        public Object getValueAt(int row, int column)
        {
            Object obj = null;

            OvalElement oe = data.get(row);

            switch(column)
            {
                case COLUMN_INDEX_TYPE:
                    if(oe instanceof OvalDefinition)
                    {
                        return "Definition";
                    }
                    else if(oe instanceof OvalTest)
                    {
                        obj = "Test";
                    }
                    else if(oe instanceof OvalObject)
                    {
                        obj = "Object";
                    }
                    else if(oe instanceof OvalState)
                    {
                        obj = "State";
                    }
                    else if(oe instanceof OvalVariable)
                    {
                        obj = "Variable";
                    }
                    else
                    {
                        obj = "Uknown";
                    }
                    break;
                case COLUMN_INDEX_ID:
                    obj =  oe.getId();
                    break;
            }

            return obj;
        }
    }

    private class MyTransferHandler extends TransferHandler
    {
        public MyTransferHandler(JTable table)
        {
            super();
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support)
        {
            return false;
        }
        
        @Override
        public int getSourceActions(JComponent c)
        {
            return COPY;
        }
        
        @Override
        public Transferable createTransferable(JComponent c)
        {
            if(c == null)
            {
                return null;
            }

            if(!(c instanceof JTable))
            {
                return null;
            }

            JTable t = (JTable) c;

            StringBuilder sb = new StringBuilder();

            MyTableModel tm = (MyTableModel) t.getModel();

            String eol = System.getProperty("line.separator");

            if(tm.data != null)
            {
                for(int x = 0; x < tm.data.size(); x++)
                {
                    String type = (String) tm.getValueAt(x, tm.COLUMN_INDEX_TYPE);
                    String id = (String) tm.getValueAt(x, tm.COLUMN_INDEX_ID);

                    sb.append(type + "," + id + eol);
                }
            }
            return new StringSelection(sb.toString());
        }        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        messageLabel = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        okButton = new javax.swing.JButton();
        copyButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Item Dependencies Found");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        messageLabel.setText("<HTML>The item chosen for deletion is depended on by the following items. Please remove the other items first and try deleting this item again.</HTML>");
        messageLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(messageLabel, gridBagConstraints);

        scrollPane.setAutoscrolls(true);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.setMaximumSize(null);
        table.setMinimumSize(null);
        table.setPreferredSize(null);
        scrollPane.setViewportView(table);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(scrollPane, gridBagConstraints);

        okButton.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        getContentPane().add(okButton, gridBagConstraints);

        copyButton.setText("Copy this list to clipboard");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        getContentPane().add(copyButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton copyButton;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

}
