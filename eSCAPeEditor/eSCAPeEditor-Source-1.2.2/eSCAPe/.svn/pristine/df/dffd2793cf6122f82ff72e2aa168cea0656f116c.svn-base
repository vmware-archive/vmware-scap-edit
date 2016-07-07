package com.g2inc.scap.editor.gui.windows.merger.oval;

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
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.jdom.Document;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.windows.EditorConfiguration;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.OcilOrOvalFilesFilter;
import com.g2inc.scap.editor.gui.windows.merger.common.MergerSourceFile;
import com.g2inc.scap.editor.gui.wizards.oval.document.CreateOvalFilenameChoicePage;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.Criteria;
import com.g2inc.scap.library.domain.oval.DefinitionClassEnum;
import com.g2inc.scap.library.domain.oval.ExtendDefinition;
import com.g2inc.scap.library.domain.oval.MergeStats;
import com.g2inc.scap.library.domain.oval.OvalCriteriaOperatorEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVariable;

/**
 * This class allows the user to select multiple oval definitions documents and
 * merge them into a single new oval definitions document.
 */
public class OvalMergerGui extends javax.swing.JDialog implements WindowListener, ActionListener,
        PropertyChangeListener,
        ChangeListener {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(OvalMergerGui.class);
    private File destFile = null;
    private boolean cancelled = true;
    private MergeStats mergeStats = new MergeStats();
    private String progressBarMinProperty = "PROGRESS_MIN";
    private String progressBarMaxProperty = "PROGRESS_MAX";

    public MergeStats getMergeStats() {
        return mergeStats;
    }

    private void initFilePicker() {
        Pattern p = null;
        String pString = CreateOvalFilenameChoicePage.PATTERN_STRING;
        try {
            p = Pattern.compile(pString);
            filenamePicker.setFilenamePattern(p);
        } catch (Exception e) {
            LOG.error("Error compiling pattern " + pString, e);
        }

        filenamePicker.addDefaultExtension("-oval.xml");
        filenamePicker.addDefaultExtension("-cpe-oval.xml");
        filenamePicker.addDefaultExtension("-patches.xml");

        filenamePicker.setApproveButtonText("Save");
        filenamePicker.setApproveButtonTooltipText("Save");

        filenamePicker.setBrowseButtonText("Browse");
        filenamePicker.setBrowseButtonTooltipText("Browse for file");

        filenamePicker.setFilenameCaption(CreateOvalFilenameChoicePage.FILENAME_CAPTION_STRING);

        filenamePicker.setShowOverviewText(false);
        filenamePicker.validate();
        filenamePicker.addChangeListener(this);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    private class Task extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() throws Exception {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            mergeButton.setEnabled(false);

            destFile = new File(filenamePicker.getFilenameText());
            DefaultListModel dlm = (DefaultListModel) sourceFilesList.getModel();

            int maximum = dlm.getSize();

            firePropertyChange(progressBarMaxProperty, new Integer(0), new Integer(maximum));

            //setProgress(0);
            operationProgressBar.setValue(0);

            try {
                // this will be created below when we see what the type of the first
                // document to be merged is
                OvalDefinitionsDocument targetOdd = null;

                if (dlm != null && dlm.size() > 0) {
                    // Initialize mergeStats.
                    mergeStats.initialize();

                    for (int x = 0; x < dlm.size(); x++) {
                        File source = (File) dlm.get(x);
                        Document doc = SCAPDocumentFactory.parse(source);

                        SCAPDocumentTypeEnum sdtype = SCAPDocumentFactory.findDocumentType(doc);

                        if (targetOdd == null) {
                            // target document will be of the same type as the first document in the list
                            targetOdd =
                                    (OvalDefinitionsDocument) SCAPDocumentFactory.createNewDocument(sdtype);
                            targetOdd.setGeneratorDate(new Date(System.currentTimeMillis()));
                            targetOdd.setFilename(destFile.getAbsolutePath());
                        }

                        SCAPDocument sd = SCAPDocumentFactory.getDocument(sdtype, doc);
                        sd.setFilename(source.getAbsolutePath());

                        if (sd.getDocumentType().equals(targetOdd.getSCAPDocument().getDocumentType())) {
                            // the documents are both the same type
                            OvalDefinitionsDocument sourceOdd = (OvalDefinitionsDocument) sd;
                            List<OvalDefinition> defList = sourceOdd.getOvalDefinitions();
                            List<OvalTest> testList = sourceOdd.getOvalTests();
                            List<OvalObject> objList = sourceOdd.getOvalObjects();
                            List<OvalState> stateList = sourceOdd.getOvalStates();
                            List<OvalVariable> varList = sourceOdd.getOvalVariables();

                            targetOdd.merge(sourceOdd, mergeStats);

                            defList.clear();
                            testList.clear();
                            objList.clear();
                            stateList.clear();
                            varList.clear();

                            sourceOdd = null;
                        }

//                       setProgress(x + 1); // The value passed to setProgress cannot be greater than 100.
                        operationProgressBar.setValue(x + 1);
                    }

                    if (mergeWithWrapperStyleRadio.isSelected()) {
                        // generate a definition that extends all of the definitions in the target document.
                        List<OvalDefinition> targetDefs = targetOdd.getOvalDefinitions();
                        if (targetDefs.size() != 0) {
                            String docBaseId = defNamespaceField.getValue();
                            targetOdd.setBaseId(docBaseId);

                            OvalDefinition wrapperDef = targetOdd.createDefinition((DefinitionClassEnum) defClassCombo.getSelectedItem());
                            wrapperDef.getMetadata().setTitle(defTitleTextArea.getText());
                            wrapperDef.getMetadata().setDescription("This definition extends all of the definitions that were merged from multiple documents");

                            Criteria wrapperCriteria = wrapperDef.createCriteria();
                            wrapperCriteria.setOperator(OvalCriteriaOperatorEnum.AND);

                            for (int x = 0; x < targetDefs.size(); x++) {
                                OvalDefinition def = targetDefs.get(x);

                                ExtendDefinition extendDef = wrapperCriteria.createExtendDefinition();

                                extendDef.setDefinitionId(def.getId());
                                extendDef.setComment(def.getMetadata().getTitle());

                                wrapperCriteria.addChild(extendDef);
                            }

                            wrapperDef.setCriteria(wrapperCriteria);

                            targetOdd.setBaseId(docBaseId);

                            // add the new definition
                            targetOdd.add(wrapperDef, 0);
                        }
                    }

                    // save document
                    targetOdd.save();
                    targetOdd = null;
                }
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
                //setProgress(0);
                operationProgressBar.setValue(0);
            }

            cancelled = false;
            setVisible(false);

            return null;
        }

        @Override
        public void done() {
            setCursor(null);
        }
    }

    /**
     * Creates new form OvalMergerGui
     */
    public OvalMergerGui(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        initComponents();
        initComponents2();
    }

    public boolean wasCancelled() {
        return cancelled;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();

        if (src == addFilesButton) {
            // launch a fileChooser
            final JFileChooser fc = new JFileChooser();
            fc.setDialogType(JFileChooser.OPEN_DIALOG);

            EditorConfiguration guiProps = EditorConfiguration.getInstance();
            File lastOpenedFrom = guiProps.getLastOpenedFromFile();

            // Set current directory
            fc.setCurrentDirectory(lastOpenedFrom);

            FileFilter ff = new OcilOrOvalFilesFilter("OVAL");
            fc.setFileFilter(ff);

            fc.setMultiSelectionEnabled(true);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int ret = fc.showOpenDialog(this);

            if (ret == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fc.getSelectedFiles();

                if (selectedFiles != null && selectedFiles.length > 0) {
                    boolean setLastOpened = false;

                    for (int x = 0; x < selectedFiles.length; x++) {
                        File f = selectedFiles[x];

                        if (!setLastOpened) {
                            File parent = f.getAbsoluteFile().getParentFile();
                            guiProps.setLastOpenedFrom(parent.getAbsolutePath());
                            guiProps.save();
                            setLastOpened = true;
                        }

                        ((DefaultListModel) sourceFilesList.getModel()).addElement(new MergerSourceFile(f.getAbsolutePath()));
                    }
                }

                if (sourceFilesList.getModel().getSize() < 2) {
                    mergeButton.setEnabled(false);
                } else {
                    if (filenamePicker.isValidFilename()) {
                        mergeButton.setEnabled(true);
                    } else {
                        mergeButton.setEnabled(false);
                    }
                }
            } else {
                return;
            }
        } else if (src == removeFilesButton) {
            List itemsToRemove = new ArrayList();

            int[] indicesToRemove = sourceFilesList.getSelectedIndices();

            if (indicesToRemove != null && indicesToRemove.length > 0) {
                DefaultListModel dlm = (DefaultListModel) sourceFilesList.getModel();
                for (int x = 0; x < indicesToRemove.length; x++) {
                    int index = indicesToRemove[x];

                    File f = (File) dlm.getElementAt(index);

                    itemsToRemove.add(f);
                }

                if (itemsToRemove.size() > 0) {
                    Iterator i = itemsToRemove.iterator();
                    while (i.hasNext()) {
                        Object listItem = i.next();

                        dlm.removeElement(listItem);

                        i.remove();
                    }
                }
            }

            if (sourceFilesList.getModel().getSize() < 2) {
                mergeButton.setEnabled(false);
            } else {
                if (filenamePicker.isValidFilename()) {
                    mergeButton.setEnabled(true);
                } else {
                    mergeButton.setEnabled(false);
                }
            }
        } else if (src == cancelButton) {
            setVisible(false);
        } else if (src == mergeButton) {
            DefaultListModel dlm = (DefaultListModel) sourceFilesList.getModel();
            SCAPDocumentTypeEnum targetSdType = null;
            // do preflight check of document versions
            if (dlm != null && dlm.size() > 0) {
                for (int x = 0; x < dlm.size(); x++) {
                    File source = (File) dlm.get(x);
                    SCAPDocumentTypeEnum sdtype = null;

                    try {
                        Document doc = SCAPDocumentFactory.parse(source);
                        sdtype = SCAPDocumentFactory.findDocumentType(doc);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(EditorMainWindow.getInstance(),
                                "<HTML>An error occurred try to open " + source.getAbsolutePath() + ": " + ex.getMessage()
                                + "</HTML>",
                                "File Open Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (sdtype != null) {
                        if (targetSdType == null) {
                            // the first file defines the target type
                            targetSdType = sdtype;
                        } else {
                            // compare the current document's type with the target
                            if (!targetSdType.equals(sdtype)) {
                                JOptionPane.showMessageDialog(EditorMainWindow.getInstance(),
                                        "<HTML>The document <br/>" + source.getAbsolutePath() + " type(" + sdtype.toString() + ") "
                                        + "<br/>does not match the target type(" + targetSdType.toString() + "). "
                                        + "<br/>All documents must be of the same version of " + EditorMessages.OVAL
                                        + " to be merged.</HTML>",
                                        EditorMessages.OVAL + " Version Mismatch",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }
                }
            }

            // handle what gets done for merge button
            Task task = new Task();
            task.addPropertyChangeListener(this);
            task.execute();
        } else if (src == simpleMergeStyleRadio) {
            setMergeDefintionOptionsEnabled(false);
        } else if (src == mergeWithWrapperStyleRadio) {
            setMergeDefintionOptionsEnabled(true);
        }
    }

    private void setMergeDefintionOptionsEnabled(boolean enabled) {
        mergeWithDefinitionWrapperOptionsPanel.setEnabled(enabled);
        defNamespaceCaption.setEnabled(enabled);
        defNamespaceField.setEnabled(enabled);
        defClassCaption.setEnabled(enabled);
        defClassCombo.setEnabled(enabled);
        defTitleCaption.setEnabled(enabled);
        defTitleTextArea.setEnabled(enabled);

        defNamespaceField.setValue("");
        defTitleTextArea.setText("");
    }

    private void initButtons() {
        addFilesButton.addActionListener(this);
        removeFilesButton.addActionListener(this);
        cancelButton.addActionListener(this);
        mergeButton.addActionListener(this);
    }

    private void initList() {
        DefaultListModel dlm = new DefaultListModel();
        sourceFilesList.setModel(dlm);

        sourceFilesList.setTransferHandler(new FileListDnDTransferHandler(this, sourceFilesList));
    }

    private void addListSelectionListeners() {
        sourceFilesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] selectedIndices = sourceFilesList.getSelectedIndices();

                if (selectedIndices == null || selectedIndices.length == 0) {
                    removeFilesButton.setEnabled(false);
                } else {
                    removeFilesButton.setEnabled(true);
                }
            }
        });
    }

    private void initProgressBar() {
        operationProgressBar.setMinimum(0);
        operationProgressBar.setStringPainted(true);
    }

    private void initCombos() {
        DefinitionClassEnum[] defClasses = DefinitionClassEnum.values();

        for (int x = 0; x < defClasses.length; x++) {
            defClassCombo.addItem(defClasses[x]);
        }

        defClassCombo.setSelectedItem(DefinitionClassEnum.VULNERABILITY);
    }

    private void initRadioButtons() {
        simpleMergeStyleRadio.addActionListener(this);
        mergeWithWrapperStyleRadio.addActionListener(this);
    }

    private void initTextFields() {
        String namespacePattern = "^[A-Za-z0-9_\\-\\.]+$";
        Pattern p = null;
        try {
            p = Pattern.compile(namespacePattern);
        } catch (Exception e) {
        }
        defNamespaceField.setPattern(p);
        defNamespaceField.addChangeListener(this);
    }

    private void initComponents2() {
        initProgressBar();
        initButtons();
        initRadioButtons();
        initList();
        initFilePicker();
        initCombos();
        initTextFields();
        addListSelectionListeners();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        styleRadioGroup = new javax.swing.ButtonGroup();
        sourceFilesPanel = new javax.swing.JPanel();
        sourcesCaption = new javax.swing.JLabel();
        sourceFilesScrollPane = new javax.swing.JScrollPane();
        sourceFilesList = new javax.swing.JList();
        listButtonPanel = new javax.swing.JPanel();
        addFilesButton = new javax.swing.JButton();
        removeFilesButton = new javax.swing.JButton();
        mergeStylePanel = new javax.swing.JPanel();
        mergeRadioPanel = new javax.swing.JPanel();
        simpleMergeStyleRadio = new javax.swing.JRadioButton();
        mergeWithWrapperStyleRadio = new javax.swing.JRadioButton();
        mergeWithDefinitionWrapperOptionsPanel = new javax.swing.JPanel();
        defNamespaceCaption = new javax.swing.JLabel();
        defNamespaceField = new com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField();
        defClassCaption = new javax.swing.JLabel();
        defClassCombo = new javax.swing.JComboBox();
        defTitleCaption = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        defTitleTextArea = new javax.swing.JTextArea();
        targetFilePanel = new javax.swing.JPanel();
        filenamePicker = new com.g2inc.scap.editor.gui.windows.common.FilenamePicker();
        operationsButtonPanel = new javax.swing.JPanel();
        mergeButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        operationProgressBar = new javax.swing.JProgressBar();

        setTitle("OVAL Merger");
        setMinimumSize(new java.awt.Dimension(462, 511));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        sourceFilesPanel.setLayout(new java.awt.GridBagLayout());

        sourcesCaption.setText("Select files to merge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        sourceFilesPanel.add(sourcesCaption, gridBagConstraints);

        sourceFilesList.setDropMode(javax.swing.DropMode.INSERT);
        sourceFilesScrollPane.setViewportView(sourceFilesList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        sourceFilesPanel.add(sourceFilesScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 3, 0);
        getContentPane().add(sourceFilesPanel, gridBagConstraints);

        listButtonPanel.setLayout(new java.awt.GridBagLayout());

        addFilesButton.setText("Add...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        listButtonPanel.add(addFilesButton, gridBagConstraints);

        removeFilesButton.setText("Remove");
        removeFilesButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        listButtonPanel.add(removeFilesButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 0, 0, 5);
        getContentPane().add(listButtonPanel, gridBagConstraints);

        mergeStylePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Merge Style"));
        mergeStylePanel.setLayout(new java.awt.GridBagLayout());

        mergeRadioPanel.setLayout(new java.awt.GridBagLayout());

        styleRadioGroup.add(simpleMergeStyleRadio);
        simpleMergeStyleRadio.setSelected(true);
        simpleMergeStyleRadio.setText("Simple");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        mergeRadioPanel.add(simpleMergeStyleRadio, gridBagConstraints);

        styleRadioGroup.add(mergeWithWrapperStyleRadio);
        mergeWithWrapperStyleRadio.setText("With Wrapper Definition");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        mergeRadioPanel.add(mergeWithWrapperStyleRadio, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        mergeStylePanel.add(mergeRadioPanel, gridBagConstraints);

        mergeWithDefinitionWrapperOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Wrapper Definition Options"));
        mergeWithDefinitionWrapperOptionsPanel.setEnabled(false);
        mergeWithDefinitionWrapperOptionsPanel.setLayout(new java.awt.GridBagLayout());

        defNamespaceCaption.setText("Definition Namespace");
        defNamespaceCaption.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        mergeWithDefinitionWrapperOptionsPanel.add(defNamespaceCaption, gridBagConstraints);

        defNamespaceField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        mergeWithDefinitionWrapperOptionsPanel.add(defNamespaceField, gridBagConstraints);

        defClassCaption.setText("Definition Class");
        defClassCaption.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        mergeWithDefinitionWrapperOptionsPanel.add(defClassCaption, gridBagConstraints);

        defClassCombo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        mergeWithDefinitionWrapperOptionsPanel.add(defClassCombo, gridBagConstraints);

        defTitleCaption.setText("Definition Title");
        defTitleCaption.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        mergeWithDefinitionWrapperOptionsPanel.add(defTitleCaption, gridBagConstraints);

        defTitleTextArea.setColumns(20);
        defTitleTextArea.setRows(5);
        defTitleTextArea.setEnabled(false);
        jScrollPane1.setViewportView(defTitleTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        mergeWithDefinitionWrapperOptionsPanel.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.7;
        mergeStylePanel.add(mergeWithDefinitionWrapperOptionsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(mergeStylePanel, gridBagConstraints);

        targetFilePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Target Filename"));
        targetFilePanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        targetFilePanel.add(filenamePicker, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(targetFilePanel, gridBagConstraints);

        operationsButtonPanel.setLayout(new java.awt.GridBagLayout());

        mergeButton.setText("Merge");
        mergeButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
        operationsButtonPanel.add(mergeButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        operationsButtonPanel.add(cancelButton, gridBagConstraints);

        operationProgressBar.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 3, 0);
        operationsButtonPanel.add(operationProgressBar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        getContentPane().add(operationsButtonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void setMergeButtonEnabled(boolean b) {
        mergeButton.setEnabled(b);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addFilesButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel defClassCaption;
    private javax.swing.JComboBox defClassCombo;
    private javax.swing.JLabel defNamespaceCaption;
    private com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField defNamespaceField;
    private javax.swing.JLabel defTitleCaption;
    private javax.swing.JTextArea defTitleTextArea;
    private com.g2inc.scap.editor.gui.windows.common.FilenamePicker filenamePicker;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel listButtonPanel;
    private javax.swing.JButton mergeButton;
    private javax.swing.JPanel mergeRadioPanel;
    private javax.swing.JPanel mergeStylePanel;
    private javax.swing.JPanel mergeWithDefinitionWrapperOptionsPanel;
    private javax.swing.JRadioButton mergeWithWrapperStyleRadio;
    private javax.swing.JProgressBar operationProgressBar;
    private javax.swing.JPanel operationsButtonPanel;
    private javax.swing.JButton removeFilesButton;
    private javax.swing.JRadioButton simpleMergeStyleRadio;
    private javax.swing.JList sourceFilesList;
    private javax.swing.JPanel sourceFilesPanel;
    private javax.swing.JScrollPane sourceFilesScrollPane;
    private javax.swing.JLabel sourcesCaption;
    private javax.swing.ButtonGroup styleRadioGroup;
    private javax.swing.JPanel targetFilePanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("progress")) {
            int progress = (Integer) evt.getNewValue();

            operationProgressBar.setValue(progress);
            operationProgressBar.setString(operationProgressBar.getValue() + "/" + operationProgressBar.getMaximum());
        } else if (propertyName.equals(progressBarMinProperty)) {
            operationProgressBar.setMinimum((Integer) evt.getNewValue());
        } else if (propertyName.equals(progressBarMaxProperty)) {
            operationProgressBar.setMaximum((Integer) evt.getNewValue());
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object src = e.getSource();

        if (src == filenamePicker) {
            if (filenamePicker.isValidFilename()) {
                if (sourceFilesList.getModel().getSize() < 2) {
                    mergeButton.setEnabled(false);
                } else {
                    if (simpleMergeStyleRadio.isSelected()) {
                        mergeButton.setEnabled(true);
                    } else {
                        // look at the required wrapper mode options
                        if (!defNamespaceField.isValidInput()) {
                            mergeButton.setEnabled(false);
                        } else {
                            mergeButton.setEnabled(true);
                        }
                    }
                }
            } else {
                mergeButton.setEnabled(false);
            }
        } else if (src == defNamespaceField) {
            if (defNamespaceField.isValidInput()) {
                defNamespaceCaption.setForeground(defNamespaceField.getNormalTextColor());

                // check filename has been supplied
                // since we know we need that regardless of the mode we are in
                if (filenamePicker.isValidFilename()) {
                    // this really has to be true since the namespace field is
                    // in the "with wrapper" section
                    if (mergeWithWrapperStyleRadio.isSelected()) {
                        if (sourceFilesList.getModel().getSize() < 2) {
                            mergeButton.setEnabled(false);
                        } else {
                            mergeButton.setEnabled(true);
                        }
                    }
                } else {
                    // namespace was supplied but a valid filename was not
                    mergeButton.setEnabled(false);
                }
            } else {
                if (defNamespaceField.isEnabled()) {
                    // namespace was supplied but a valid filename was not
                    defNamespaceCaption.setForeground(defNamespaceField.getErrorTextColor());
                    mergeButton.setEnabled(false);
                } else {
                    // namespace they typed was not valid
                    defNamespaceCaption.setForeground(defNamespaceField.getNormalTextColor());
                    mergeButton.setEnabled(true);
                }
            }
        }
    }

    public File getTargetFile() {
        return new File(filenamePicker.getFilenameText());
    }
}
