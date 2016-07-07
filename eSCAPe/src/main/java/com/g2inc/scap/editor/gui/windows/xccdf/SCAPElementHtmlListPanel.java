package com.g2inc.scap.editor.gui.windows.xccdf;

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
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IEditorPage;
import com.g2inc.scap.editor.gui.resources.HTMLEditorScrubber;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.ChangeNotifierPanel;
import com.g2inc.scap.library.domain.SCAPElement;
import com.g2inc.scap.library.domain.SCAPElementImpl;
import com.g2inc.scap.library.domain.xccdf.HtmlText;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.util.CommonUtil;

public class SCAPElementHtmlListPanel extends ChangeNotifierPanel implements
        ActionListener, DocumentListener {

    private static final long serialVersionUID = 1L;
    protected List elementList = null;
    private static Logger log = Logger
            .getLogger(SCAPElementHtmlListPanel.class);
    private Class<? extends HtmlText> elementClass = null;
    private String elementName = null;
    private Class editorPageClass = null;
    private JList listBox;
    private JEditorPane htmlTextEditorPane;
    private JScrollPane scrollPane;
    private HtmlCellRenderer renderer = new HtmlCellRenderer();

    private void addListSelectionListeners() {
        listBox.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] indices = listBox.getSelectedIndices();

                if (indices == null || indices.length == 0) {
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                    return;
                } else if (indices != null && indices.length == 1) {
                    // more than one item was selected, only one
                    // item can be edited at a time.
                    editButton.setEnabled(true);
                    removeButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    removeButton.setEnabled(true);
                }
            }
        });

        listBox.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    SCAPElementImpl currentElement = null;
                    int currentLoc = 0;
                    if (listBox != null) {
                        currentElement = (SCAPElementImpl) listBox
                                .getSelectedValue();
                        DefaultListModel model = (DefaultListModel) listBox
                                .getModel();
                        currentLoc = model.indexOf(currentElement);
                    } else {
                        if (elementList.size() > 0) {
                            currentElement = (SCAPElementImpl) elementList.get(0);
                        } else {
                            return; // should not occur
                        }
                    }
                    EditorDialog editor = new EditorDialog(EditorMainWindow
                            .getInstance(), true);
                    IEditorPage editorPage = getEditorPage();
                    editor.setEditorPage(editorPage);
                    editor.setData(currentElement);
                    editor.pack();
                    editor.setLocationRelativeTo(null);
                    editor.setVisible(true);

                    if (!editor.wasCancelled()) {
                        SCAPElementImpl newElement = (SCAPElementImpl) editor.getData();
                        elementList.set(currentLoc, newElement);
                        addCorrectComponentToPanel();
                        validate();
                        setChanged(true);
                        notifyRegisteredListeners();
                    }
                }

            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();

        if (src == addButton) {
            EditorDialog editor = new EditorDialog(
                    EditorMainWindow.getInstance(), true);
            IEditorPage editorPage = getEditorPage();
            editor.setEditorPage(editorPage);
            XCCDFBenchmark benchmark = (XCCDFBenchmark) EditorMainWindow
                    .getInstance().getActiveEditorForm().getDocument();
            SCAPElement newElement = benchmark.createSCAPElement(elementName,
                    benchmark.getImplClass(elementClass));
            editor.setData(newElement);
            editor.pack();
            editor.setLocationRelativeTo(null);
            editor.setVisible(true);

            if (!editor.wasCancelled()) {
                newElement = (SCAPElementImpl) editor.getData();
                elementList.add(newElement);
                addCorrectComponentToPanel();
                validate();
                setChanged(true);
                notifyRegisteredListeners();
            }
        } else if (src == editButton) {
            SCAPElementImpl currentElement = null;
            int currentLoc = 0;
            if (listBox != null) {
                currentElement = (SCAPElementImpl) listBox.getSelectedValue();
                DefaultListModel model = (DefaultListModel) listBox.getModel();
                currentLoc = model.indexOf(currentElement);
            } else {
                if (elementList.size() > 0) {
                    currentElement = (SCAPElementImpl) elementList.get(0);
                } else {
                    return; // should not occur
                }
            }
            EditorDialog editor = new EditorDialog(
                    EditorMainWindow.getInstance(), true);
            IEditorPage editorPage = getEditorPage();
            editor.setEditorPage(editorPage);
            editor.setData(currentElement);
            editor.pack();
            editor.setLocationRelativeTo(null);
            editor.setVisible(true);

            if (!editor.wasCancelled()) {
                SCAPElementImpl newElement = (SCAPElementImpl) editor.getData();
                elementList.set(currentLoc, newElement);
                addCorrectComponentToPanel();
                validate();
                setChanged(true);
                notifyRegisteredListeners();
            }
        } else if (src == removeButton) {
            Object[] selectedObjects = new Object[1];
            if (listBox != null) {
                selectedObjects = listBox.getSelectedValues();
            } else {
                selectedObjects[0] = elementList.get(0);
            }
            for (int i = 0; i < selectedObjects.length; i++) {
                elementList.remove(selectedObjects[i]);
            }
            addCorrectComponentToPanel();
            validate();
            if (elementList.size() == 0) {
                removeButton.setEnabled(false);
            }
            setChanged(true);
            notifyRegisteredListeners();
        }
    }

    private void addButtonListeners() {
        addButton.addActionListener(this);
        editButton.addActionListener(this);
        removeButton.addActionListener(this);
    }

    private void initComponents2() {
        addButtonListeners();
    }

    /**
     * Creates new form DefinitionDetailTab
     */
    public SCAPElementHtmlListPanel() {
        initComponents();
        initComponents2();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed"
    // desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        listPanel = new javax.swing.JPanel();
        buttonsPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        listPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.95;
        gridBagConstraints.weighty = 1.0;
        add(listPanel, gridBagConstraints);

        buttonsPanel.setLayout(new java.awt.GridBagLayout());

        addButton.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        buttonsPanel.add(addButton, gridBagConstraints);

        editButton.setText("Edit");
        editButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        buttonsPanel.add(editButton, gridBagConstraints);

        removeButton.setText("Remove");
        removeButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        buttonsPanel.add(removeButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(buttonsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
     // Variables declaration - do not modify//GEN-BEGIN:variables

    private javax.swing.JButton addButton;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton editButton;
    private javax.swing.JPanel listPanel;
    private javax.swing.JButton removeButton;

    // End of variables declaration//GEN-END:variables
    // End of variables declaration
    public List getDoc() {
        return elementList;
    }

    public void setDoc(List doc) {
        this.elementList = doc;
        addCorrectComponentToPanel();
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public void setElementClass(Class<? extends HtmlText> elementClass) {
        this.elementClass = elementClass;
    }

    public void setEditorPageClass(Class editorPageClass) {
        this.editorPageClass = editorPageClass;
    }

    private IEditorPage getEditorPage() {
        IEditorPage editorPage = null;
        try {
            editorPage = (IEditorPage) editorPageClass.newInstance();
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new IllegalStateException("Cant instantiate editor page for "
                    + editorPageClass.getName());
        }
        return editorPage;
    }

    private void addCorrectComponentToPanel() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        scrollPane = new JScrollPane();
        HTMLEditorKit htmlKit = new HTMLEditorKit();
        if (elementList.size() > 1) {
            // JPanel should contain a JList because we have more than one
            // HtmlText
            listPanel.removeAll();
            htmlTextEditorPane = null;
            if (listBox == null) {
                listBox = new JList();
                listBox.setModel(new DefaultListModel());
                listBox.setCellRenderer(renderer);
                addListSelectionListeners();
            }
            scrollPane.setViewportView(listBox);
            DefaultListModel rListModel = (DefaultListModel) listBox.getModel();
            rListModel.removeAllElements();
            for (int i = 0; i < elementList.size(); i++) {
                rListModel.addElement(elementList.get(i));
            }
            listBox.setSelectedIndex(0);
        } else {
            // JPanel should just contain a JTextPane, because there is only one
            // Description
            listPanel.removeAll();
            scrollPane = new JScrollPane();
            listBox = null;
            htmlTextEditorPane = new JEditorPane();
            htmlTextEditorPane.setEditable(false);
            scrollPane.setViewportView(htmlTextEditorPane);
            if (elementList.size() != 0) {
                HtmlText htmlText = (HtmlText) elementList.get(0);
                String text = htmlText.toStringWithHtml();
                htmlTextEditorPane.setEditorKit(htmlKit);
                Document htmlTextDoc = htmlKit.createDefaultDocument();
                htmlTextEditorPane.setDocument(htmlTextDoc);
                htmlTextEditorPane.setText(CommonUtil.removeXhtmlPrefixes(text));
                HTMLEditorScrubber.scrubJEditorPane(htmlTextEditorPane);
                editButton.setEnabled(true);
            } else {
                editButton.setEnabled(false);
            }
        }
        // htmlTextEditorPane.getDocument().addDocumentListener(this);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        listPanel.add(scrollPane, gridBagConstraints);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        notifyRegisteredListeners();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        notifyRegisteredListeners();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        notifyRegisteredListeners();
    }

    private class HtmlCellRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            HtmlText htmlElement = (HtmlText) value;
            String text = htmlElement.toStringWithHtml();
            JEditorPane editorPane = new JEditorPane();

            HTMLEditorKit htmlKit = new HTMLEditorKit();
            editorPane.setEditorKit(htmlKit);
            Document htmlTextDoc = htmlKit.createDefaultDocument();
            editorPane.setDocument(htmlTextDoc);
            editorPane.setEditable(false);
            editorPane.setText(CommonUtil.removeXhtmlPrefixes(text));
            HTMLEditorScrubber.scrubJEditorPane(editorPane);
            if (isSelected) {
                editorPane.setPreferredSize(list.getMaximumSize());
                editorPane
                        .setBackground((Color) UIManager.get("textHighlight"));
            } else {
                editorPane.setBackground(Color.WHITE);
            }
            return editorPane;
        }
    }
}
