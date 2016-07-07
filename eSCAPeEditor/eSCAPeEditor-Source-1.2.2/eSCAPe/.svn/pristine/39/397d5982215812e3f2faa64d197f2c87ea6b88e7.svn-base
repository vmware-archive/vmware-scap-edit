package com.g2inc.scap.editor.gui.windows;
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

import com.g2inc.scap.editor.gui.dialogs.editors.DisplayDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.patternmatch.RegexDatatypeEditor;
import com.g2inc.scap.editor.gui.dialogs.encoding.AlternateEncodingPicker;
import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.bundle.ZipBundleEditorForm;
import com.g2inc.scap.editor.gui.windows.common.EditorAboutBox;
import com.g2inc.scap.editor.gui.windows.common.ErrorSummaryWindow;
import com.g2inc.scap.editor.gui.windows.common.GenericProgressDialog;
import com.g2inc.scap.editor.gui.windows.common.NavigationPanel;
import com.g2inc.scap.editor.gui.windows.common.WeakInternalFrameListener;
import com.g2inc.scap.editor.gui.windows.common.WeakRunnable;
import com.g2inc.scap.editor.gui.windows.common.mrufiles.MRUDocumentsMenu;
import com.g2inc.scap.editor.gui.windows.common.mrufiles.MRUFileSelectedEvent;
import com.g2inc.scap.editor.gui.windows.cpe.CPEDictionaryEditorForm;
import com.g2inc.scap.editor.gui.windows.datastream.DataStreamBundleEditorForm;
import com.g2inc.scap.editor.gui.windows.merger.oval.OvalMergerGui;
import com.g2inc.scap.editor.gui.windows.merger.oval.OvalMergerMessagePanel;
import com.g2inc.scap.editor.gui.windows.oval.OvalEditorForm;
import com.g2inc.scap.editor.gui.windows.wizardmode.WizardModeWindow;
import com.g2inc.scap.editor.gui.windows.xccdf.XCCDFEditorForm;
import com.g2inc.scap.editor.gui.windows.xccdf.gen.XCCDFGen;
import com.g2inc.scap.editor.gui.wizards.cpe.NewCPEDictionaryWizard;
import com.g2inc.scap.editor.gui.wizards.filesaveas.FileSaveAsWizard;
import com.g2inc.scap.editor.gui.wizards.oval.document.CreateOvalWizard;
import com.g2inc.scap.editor.gui.wizards.xccdf.CreateNewXCCDFParametersPage;
import com.g2inc.scap.editor.gui.wizards.xccdf.CreateXCCDFWizard;
import com.g2inc.scap.library.domain.CharacterSetEncodingException;
import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentClassEnum;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.UnsupportedDocumentTypeException;
import com.g2inc.scap.library.domain.bundle.SCAPDocumentBundle;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.scap12.Scap12DataStreamCollection;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.util.CommonUtil;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;

public class EditorMainWindow extends javax.swing.JFrame implements InternalFrameListener, WindowListener, ChangeListener {

    private static EditorMainWindow instance = null;
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(EditorMainWindow.class);
    public static final int MIN_WIN_X = 800;
    public static final int MIN_WIN_Y = 600;
    private int openDocuments = 0;
    private boolean wizardMode = false;
    private List<File> filesFromCommandLine = null;
    private final EditorConfiguration guiProps = EditorConfiguration.getInstance();

    public static synchronized EditorMainWindow getInstance() {
        if (instance == null) {
            LOG.debug("Instantiating EditorMainWindow");
            try {
                instance = new EditorMainWindow();
            } catch (Throwable t) {
                LOG.error("ERROR instantiating EditorMainWindow", t);
            }
            LOG.debug("Finished instantiating EditorMainWindow");
        }

        return instance;
    }

    private synchronized void updateMenus() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // save and save as menu items
                if (wizardMode || openDocuments == 0) {
                    saveAsMenuItem.setEnabled(false);
                    saveMenuItem.setEnabled(false);
                } else {
                    saveAsMenuItem.setEnabled(true);
                    saveMenuItem.setEnabled(true);
                }

                // the other menu items
                if (wizardMode) {
                    newCPEDictionaryMenuItem.setEnabled(false);
                    newOvalMenuItem.setEnabled(false);
                    newXCCDFMenuItem.setEnabled(false);
                    newXCCDFFromOvalMenuItem.setEnabled(false);
                    newXCCDFFromOcilMenuItem.setEnabled(false);
                    openCPEDictionaryMenuItem.setEnabled(false);
                    openOvalMenuItem.setEnabled(false);
                    openXCCDFMenuItem.setEnabled(false);
                    validateDocumentMenuItem.setEnabled(false);

                    wizModeMenuItem.setEnabled(false);
                    mostRecentOpenDocsMenu.setEnabled(false);
                } else {
                    newCPEDictionaryMenuItem.setEnabled(true);
                    newOvalMenuItem.setEnabled(true);
                    newXCCDFMenuItem.setEnabled(true);
                    newXCCDFFromOvalMenuItem.setEnabled(true);
                    newXCCDFFromOcilMenuItem.setEnabled(true);
                    openCPEDictionaryMenuItem.setEnabled(true);
                    openOvalMenuItem.setEnabled(true);
                    openXCCDFMenuItem.setEnabled(true);

                    validateDocumentMenuItem.setEnabled(true);

                    if (openDocuments == 0) {
                        wizModeMenuItem.setEnabled(true);
                        validateDocumentMenuItem.setEnabled(false);
                    } else {
                        wizModeMenuItem.setEnabled(false);
                        validateDocumentMenuItem.setEnabled(true);
                    }
                    mostRecentOpenDocsMenu.setEnabled(true);
                }
            }
        });
    }

    private void populateMRUFiles() {
        List<String> mruFileKeys = new ArrayList<String>();

        Iterator<Object> propKeys = guiProps.keySet().iterator();

        while (propKeys.hasNext()) {
            String key = (String) propKeys.next();

            if (key.startsWith(MRUDocumentsMenu.PROPERTY_MRU_FILE_PREFIX)) {
                mruFileKeys.add(key);
            }
        }

        if (mruFileKeys.size() > 0) {
            boolean foundNonExistentFiles = false;

            mostRecentOpenDocsMenu.setEnabled(true);
            Collections.sort(mruFileKeys);
            Collections.reverse(mruFileKeys);
            for (int x = 0; x < mruFileKeys.size(); x++) {
                String mruFileKey = mruFileKeys.get(x);
                String pathString = guiProps.getProperty(mruFileKey);


                if (pathString != null) {
                    File f = new File(pathString);

                    if (!f.exists()) {
                        foundNonExistentFiles = true;
                        guiProps.remove(mruFileKey);
                        continue;
                    }

                    mostRecentOpenDocsMenu.addItem(f, this);

                } else {
                    // no value, remove this key
                    guiProps.remove(mruFileKey);
                    foundNonExistentFiles = true;
                    x--;
                }
            }

            if (foundNonExistentFiles) {
                guiProps.save();
            }
        } else {
            mostRecentOpenDocsMenu.setEnabled(false);
        }
    }

    private void saveMRUFiles() {
        // remove any existing mru entries from
        // props

        Iterator keyItr = guiProps.keySet().iterator();

        while (keyItr.hasNext()) {
            String key = (String) keyItr.next();

            if (key.startsWith(MRUDocumentsMenu.PROPERTY_MRU_FILE_PREFIX)) {
                keyItr.remove();
            }
        }

        List<File> files = mostRecentOpenDocsMenu.getRecentFiles();

        if (files != null && files.size() > 0) {
            for (int x = 0; x < files.size(); x++) {
                File f = files.get(x);

                String propName = MRUDocumentsMenu.PROPERTY_MRU_FILE_PREFIX;

                String indexNumber = String.format("%04d", x);

                propName = propName + indexNumber;

                guiProps.put(propName, f.getAbsolutePath());
            }
        }

        guiProps.save();
    }

    /**
     * Creates new form EditorMainWindow
     */
    public EditorMainWindow() {
        LOG.debug("EditorMainWindow constructor entered");
        this.setMinimumSize(new Dimension(MIN_WIN_X, MIN_WIN_Y));
        LOG.debug("EditorMainWindow calling initComponents");
        initComponents();
        LOG.debug("EditorMainWindow calling initComponents2");
        initComponents2();

        LOG.debug("EditorMainWindow calling populateMRUFiles");
        populateMRUFiles();
        LOG.debug("EditorMainWindow constructor returning");
    }

    private void initHelpMenu() {
        final EditorMainWindow parentRef = this;

        /*gsMenuItem.addActionListener(new ActionListener()
         {
         @Override
         public void actionPerformed(ActionEvent e)
         {
         File hsPath = HelpSetUtil.findGettingStartedHelpset();
                
         if(hsPath != null)
         {
         HelpViewerWindow helpWindow = null;
                    
         helpWindow = HelpSetUtil.getHelpViewer(hsPath);
                    
         if(helpWindow != null)
         {
         helpWindow.pack();
         helpWindow.setLocationRelativeTo(parentRef);
         helpWindow.setVisible(true);
         }
         else
         {
         String message = "HelpSet file " + hsPath.getAbsolutePath() + "found but\n" +
         "there was an error loading the HelpSet.";                    	
         EditorUtil.showMessageDialog(parentRef,
         message,
         "Error",
         JOptionPane.ERROR_MESSAGE);
         return;                        
         }
         }
         else
         {
         String message = "Unable to find HelpSet for getting started guide.  Check your installation of " + EditorMessages.PRODUCT_SHORTNAME;
         EditorUtil.showMessageDialog(parentRef,
         message,
         "Error",
         JOptionPane.ERROR_MESSAGE);
         return;
         }
         }
         });*/

        /*ugMenuItem.addActionListener(new ActionListener()
         {
         @Override
         public void actionPerformed(ActionEvent e)
         {
         File hsPath = HelpSetUtil.findUserGuideHelpset();

         if(hsPath != null)
         {
         HelpViewerWindow helpWindow = null;

         helpWindow = HelpSetUtil.getHelpViewer(hsPath);

         if(helpWindow != null)
         {
         helpWindow.pack();
         helpWindow.setLocationRelativeTo(parentRef);
         helpWindow.setVisible(true);
         }
         else
         {
         String message = "HelpSet file " + hsPath.getAbsolutePath() + "found but\n" +
         "there was an error loading the HelpSet.";
                    	                    	
         EditorUtil.showMessageDialog(parentRef,
         message,
         "Error",
         JOptionPane.ERROR_MESSAGE);
         return;
         }
         }
         else
         {
         String message = "Unable to find HelpSet for user guide.  Check your installation of " + EditorMessages.PRODUCT_SHORTNAME;

         EditorUtil.showMessageDialog(parentRef,
         message,
         "Error",
         JOptionPane.ERROR_MESSAGE);
         return;
         }
         }
         });*/

        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditorAboutBox aboutBox = new EditorAboutBox(parentRef, true);
                aboutBox.pack();
                aboutBox.validate();
                aboutBox.setLocationRelativeTo(parentRef);
                aboutBox.setVisible(true);
            }
        });
    }

    private void initToolsMenu() {
        final EditorMainWindow parentRef = this;

        regexValidatorMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                EditorDialog editor = new EditorDialog(parentRef, true);
                editor.setEditorPage(new RegexDatatypeEditor());

                editor.pack();
                editor.setLocationRelativeTo(EditorMainWindow.getInstance());
                editor.validate();
                editor.setVisible(true);
            }
        });

        validateDocumentMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // get the currently open window
                JInternalFrame selectedWin = desktopPane.getSelectedFrame();

                if (selectedWin != null) {
                    SCAPDocument scapDoc = null;

                    if (selectedWin instanceof OvalEditorForm) {
                        OvalEditorForm oef = (OvalEditorForm) selectedWin;
                        scapDoc = oef.getDocument();
                    } else if (selectedWin instanceof XCCDFEditorForm) {
                        XCCDFEditorForm xef = (XCCDFEditorForm) selectedWin;
                        scapDoc = (SCAPDocument) xef.getDocument();
                    } else if (selectedWin instanceof CPEDictionaryEditorForm) {
                        CPEDictionaryEditorForm cdf = (CPEDictionaryEditorForm) selectedWin;
                        scapDoc = cdf.getDocument();
                    } else {
                        return;
                    }

                    if (scapDoc != null) {
                        String result = scapDoc.validate();
                        boolean success = false;

                        if (result == null) {
                            // this means success
                            success = true;
                            result = "Validation successful";
                        }

                        if (success) {
                            EditorUtil.showMessageDialog(parentRef,
                                    result,
                                    "Validation successful",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            ErrorSummaryWindow esw = new ErrorSummaryWindow(parentRef, false);

                            esw.setErrorSummary(scapDoc.getFilename(), result);
                            esw.pack();
                            esw.setLocationRelativeTo(EditorMainWindow.getInstance());
                            esw.setVisible(true);
                        }
                    }
                }
            }
        });

        ovalMergeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OvalMergerGui mergerGui = new OvalMergerGui(parentRef, false);
                mergerGui.setModal(true);
                mergerGui.setLocationRelativeTo(parentRef);

                mergerGui.setVisible(true);

                if (!mergerGui.wasCancelled()) {
                    // Display results.
                    DisplayDialog dialog = new DisplayDialog(EditorMainWindow.getInstance(), true);
                    OvalMergerMessagePanel displayPage = new OvalMergerMessagePanel();
                    dialog.setTitle(OvalMergerMessagePanel.WINDOW_TITLE);
                    dialog.setPage(displayPage);
                    if (mergerGui.getMergeStats().getWarnings().size() == 0) {
                        mergerGui.getMergeStats().getWarnings().add("No issues were detected during the merge process.");
                    }
                    displayPage.setData(mergerGui.getMergeStats());
                    dialog.pack();
                    dialog.setLocationRelativeTo(EditorMainWindow.getInstance());
                    dialog.setVisible(true);

                    // Open the OVAL file.
                    File f = mergerGui.getTargetFile();
                    openFile(f, SCAPDocumentClassEnum.OVAL);
                }

                mergerGui.dispose();
            }
        });
    }

    private void initFilemenu() {
        final EditorMainWindow parentWinRef = this;

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentWinRef.dispose();
            }
        });

        openXCCDFMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                final JFileChooser fc = new JFileChooser();
                fc.setDialogType(JFileChooser.OPEN_DIALOG);

                File lastOpenedFrom = guiProps.getLastOpenedFromFile();

                // Set current directory
                fc.setCurrentDirectory(lastOpenedFrom);

                FileFilter ff = new XCCDFFilesFilter();
                fc.setFileFilter(ff);
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int ret = fc.showOpenDialog((Component) EditorMainWindow.getInstance());

                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();

                    File parent = f.getAbsoluteFile().getParentFile();
                    guiProps.setLastOpenedFrom(parent.getAbsolutePath());
                    guiProps.save();

                    openFile(f, SCAPDocumentClassEnum.XCCDF);
                }
            }
        });

        openOvalMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                final JFileChooser fc = new JFileChooser();
                fc.setDialogType(JFileChooser.OPEN_DIALOG);

                File lastOpenedFrom = guiProps.getLastOpenedFromFile();

                // Set current directory
                fc.setCurrentDirectory(lastOpenedFrom);

                FileFilter ff = new OcilOrOvalFilesFilter("OVAL");
                fc.setFileFilter(ff);
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int ret = fc.showOpenDialog(EditorMainWindow.getInstance());


                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    File parent = f.getAbsoluteFile().getParentFile();
                    guiProps.setLastOpenedFrom(parent.getAbsolutePath());
                    guiProps.save();

                    openFile(f, SCAPDocumentClassEnum.OVAL);
                }
            }
        });

        openCPEDictionaryMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                final JFileChooser fc = new JFileChooser();
                fc.setDialogType(JFileChooser.OPEN_DIALOG);

                File lastOpenedFrom = guiProps.getLastOpenedFromFile();

                // Set current directory
                fc.setCurrentDirectory(lastOpenedFrom);

                FileFilter ff = new CPEDictionaryFilesFilter();
                fc.setFileFilter(ff);
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int ret = fc.showOpenDialog(EditorMainWindow.getInstance());

                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    File parent = f.getAbsoluteFile().getParentFile();
                    guiProps.setLastOpenedFrom(parent.getAbsolutePath());
                    guiProps.save();

                    openFile(f, SCAPDocumentClassEnum.CPE_DICTIONARY);
                }
            }
        });

        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // get the currently open window
                JInternalFrame selectedWin = desktopPane.getSelectedFrame();

                if (selectedWin != null) {
                    SCAPDocument scapDoc = null;

                    Document dom = null;
                    String filename = null;

                    if (selectedWin instanceof OvalEditorForm) {
                        OvalEditorForm oef = (OvalEditorForm) selectedWin;
                        scapDoc = oef.getDocument();
                        dom = scapDoc.getDoc();
                        filename = scapDoc.getFilename();
                    } else if (selectedWin instanceof XCCDFEditorForm) {
                        XCCDFEditorForm xef = (XCCDFEditorForm) selectedWin;
                        scapDoc = (SCAPDocument) xef.getDocument();
                        dom = scapDoc.getDoc();
                        filename = scapDoc.getFilename();
                    } else if (selectedWin instanceof CPEDictionaryEditorForm) {
                        CPEDictionaryEditorForm cef = (CPEDictionaryEditorForm) selectedWin;
                        scapDoc = cef.getDocument();
                        dom = scapDoc.getDoc();
                        filename = scapDoc.getFilename();
                    } else if (selectedWin instanceof DataStreamBundleEditorForm) {
                        DataStreamBundleEditorForm cef = (DataStreamBundleEditorForm) selectedWin;
                        Scap12DataStreamCollection col = cef.getCollection();
                        filename = col.getFileName();
                        try {
                            col.save();
                            ((EditorForm) selectedWin).setDirty(false);
                        } catch (Exception e) {
                            String message = "An error occured trying to save to file " + filename + ": " + e.getMessage();
                            EditorUtil.showMessageDialog(parentWinRef,
                                    message,
                                    EditorMessages.SAVE_ERROR_DIALOG_TITLE,
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    if (dom != null) {
                        // since this is a save operation, not save as, we won't
                        // prompt the user for where to store the file

                        try {
                            scapDoc.save();
                            ((EditorForm) selectedWin).setDirty(false);
                        } catch (Exception e) {
                            String message = "An error occured trying to save to file " + filename + ": " + e.getMessage();
                            EditorUtil.showMessageDialog(parentWinRef,
                                    message,
                                    EditorMessages.SAVE_ERROR_DIALOG_TITLE,
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        saveAsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // get the currently open window
                JInternalFrame selectedWin = desktopPane.getSelectedFrame();

                if (selectedWin != null) {
                    SCAPDocument scapDoc = null;
                    Document dom = null;
                    String filename = null;
                    String windowTitle = null;

                    if (selectedWin instanceof OvalEditorForm) {
                        windowTitle = OvalEditorForm.WINDOW_TITLE_BASE;
                        OvalEditorForm oef = (OvalEditorForm) selectedWin;
                        scapDoc = oef.getDocument();
                        dom = scapDoc.getDoc();
                        filename = scapDoc.getFilename();
                    } else if (selectedWin instanceof XCCDFEditorForm) {
                        windowTitle = XCCDFEditorForm.WINDOW_TITLE_BASE;
                        XCCDFEditorForm xef = (XCCDFEditorForm) selectedWin;
                        scapDoc = (SCAPDocument) xef.getDocument();
                        dom = scapDoc.getDoc();
                        filename = scapDoc.getFilename();
                    } else if (selectedWin instanceof CPEDictionaryEditorForm) {
                        windowTitle = CPEDictionaryEditorForm.WINDOW_TITLE_BASE;
                        CPEDictionaryEditorForm cef = (CPEDictionaryEditorForm) selectedWin;
                        scapDoc = cef.getDocument();
                        dom = scapDoc.getDoc();
                        filename = scapDoc.getFilename();
                    } else if (selectedWin instanceof DataStreamBundleEditorForm) {
                        windowTitle = DataStreamBundleEditorForm.WINDOW_TITLE_BASE;
                        DataStreamBundleEditorForm cef = (DataStreamBundleEditorForm) selectedWin;
                        Scap12DataStreamCollection col = cef.getCollection();
                        filename = col.getFileName();
                        String newFilename = null;
                        SCAPDocumentTypeEnum docType = SCAPDocumentTypeEnum.SCAP_12_DATA_STREAM;

                        FileSaveAsWizard saveAsWiz = new FileSaveAsWizard(EditorMainWindow.getInstance(), true, docType);

                        //saveAsWiz.pack();
                        saveAsWiz.setLocationRelativeTo(EditorMainWindow.getInstance());
                        saveAsWiz.setVisible(true);

                        if (saveAsWiz.wasCancelled()) {
                            return;
                        }

                        newFilename = saveAsWiz.getFilename();

                        try {
                            col.setFileName(newFilename);
                            col.saveAs(new File(newFilename));

                            EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), false);
                            ((EditorForm) selectedWin).refreshRootNode();

                        } catch (Exception e) {
                            LOG.error(e.getMessage(), e);

                            EditorUtil.showMessageDialog(parentWinRef,
                                    "An error occured trying to save to file " + newFilename + ": " + e.getMessage(),
                                    "Save Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        mostRecentOpenDocsMenu.addItem((new File(newFilename)).getAbsoluteFile(), EditorMainWindow.getInstance());
                        saveMRUFiles();
                    } else {
                        return;
                    }

                    if (dom != null) {
                        String newFilename = null;
                        SCAPDocumentTypeEnum docType = scapDoc.getDocumentType();

                        FileSaveAsWizard saveAsWiz = new FileSaveAsWizard(EditorMainWindow.getInstance(), true, docType);

                        //saveAsWiz.pack();
                        saveAsWiz.setLocationRelativeTo(EditorMainWindow.getInstance());
                        saveAsWiz.setVisible(true);

                        if (saveAsWiz.wasCancelled()) {
                            return;
                        }

                        newFilename = saveAsWiz.getFilename();

                        try {
                            scapDoc.setFilename(newFilename);
                            scapDoc.saveAs(newFilename);

                            EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), false);
                            ((EditorForm) selectedWin).refreshRootNode();

                        } catch (Exception e) {
                            LOG.error(e.getMessage(), e);

                            EditorUtil.showMessageDialog(parentWinRef,
                                    "An error occured trying to save to file " + newFilename + ": " + e.getMessage(),
                                    "Save Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        SCAPContentManager scm = SCAPContentManager.getInstance();

                        if (scm != null) {
                            scm.removeDocument(filename);
                            scm.addDocument(newFilename, scapDoc);
                            selectedWin.setTitle(windowTitle + (new File(newFilename)).getAbsolutePath());
                        } else {
                            LOG.error("SCM instance is null here!");
                        }

                        mostRecentOpenDocsMenu.addItem((new File(newFilename)).getAbsoluteFile(), EditorMainWindow.getInstance());
                        saveMRUFiles();
                    }
                }
            }
        });

        newXCCDFMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                CreateXCCDFWizard wiz = new CreateXCCDFWizard(true);
                wiz.setName("create_xccdf_wizard");
                wiz.pack();
                wiz.setVisible(true);

                if (!wiz.wasCancelled()) {
                    String createdFilename = createNewXCCDFDocument(wiz);

                    if (createdFilename == null) {
                        LOG.error("newXCCDFMenuItem.actionlistener: Created filename was null!");
                        return;
                    }

                    File f = new File(createdFilename);

                    guiProps.setLastOpenedFromFile(f.getParentFile());
                    guiProps.save();

                    SCAPContentManager scm = SCAPContentManager.getInstance();

                    if (scm != null) {
                        XCCDFBenchmark benchmark = (XCCDFBenchmark) scm.getDocument(f.getAbsolutePath());

                        openFile((SCAPDocument) benchmark);
                    }
                }
                wiz.setVisible(false);
                wiz.dispose();
            }
        });

        newXCCDFFromOvalMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	generateXccdfFromOvalOrOcil("OVAL");
            }
 
        });
        
        newXCCDFFromOcilMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	generateXccdfFromOvalOrOcil("OCIL");
            }
 
        });
        newScapDataStreamMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                final JFileChooser fc = new JFileChooser();
                fc.setDialogType(JFileChooser.SAVE_DIALOG);

                File lastOpenedFrom = guiProps.getLastOpenedFromFile();

                // Set current directory
                fc.setCurrentDirectory(lastOpenedFrom);

                FileFilter ff = new StreamBundleFilesFilter();
                fc.setFileFilter(ff);
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int ret = fc.showOpenDialog(EditorMainWindow.getInstance());

                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();

                    File parent = f.getAbsoluteFile().getParentFile();
                    guiProps.setLastOpenedFrom(parent.getAbsolutePath());
                    guiProps.save();

                    if (f.getName().toLowerCase().endsWith(".xml")) {
                        try {
                            // first import an xccdf 1.2 document, make sure we can access new fields
                            Scap12DataStreamCollection collection =
                                    new Scap12DataStreamCollection(f.getAbsolutePath());
                            LOG.debug("Input datastreamcollection file name: " + f.getName());

                            EditorMainWindow emw = EditorMainWindow.getInstance();
                            JDesktopPane emwDesktopPane = emw.getDesktopPane();

                            DataStreamBundleEditorForm sbef = new DataStreamBundleEditorForm(collection);
                            sbef.setSource(f.getAbsolutePath());

                            //zbef.pack();
                            Dimension dpDim = emwDesktopPane.getSize();
                            int x = (dpDim.width - sbef.getWidth()) / 2;
                            int y = (dpDim.height - sbef.getHeight()) / 2;

                            sbef.setLocation(x, y);
                            sbef.setVisible(true);
                            emwDesktopPane.add(sbef);
                        } catch (UnsupportedDocumentTypeException e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath() + ", UnsupportedDocumentTypeException", e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: This SCAP Document Type is currently unsupported",
                                    "Error opening SCAP Data Stream - Unsupported Document Type", JOptionPane.ERROR_MESSAGE);
                        } catch (IOException e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath() + ", IOException", e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: An IO error occured, please make sure the file is available and not corrupted.",
                                    "Error opening SCAP Data Stream - IO Exception", JOptionPane.ERROR_MESSAGE);
                        } catch (IllegalStateException e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath() + ", IllegalStateException", e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: Error occured parsing the XML, IllegalStateException",
                                    "Error opening SCAP Data Stream - IllegalStateException", JOptionPane.ERROR_MESSAGE);
                        } catch (JAXBException e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath() + ", JAXBException", e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: Error occured parsing the XML, check to make sure this document is an SCAP Data Stream.",
                                    "Error opening SCAP Data Stream - JAXBException", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath(), e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: Exception",
                                    "Error opening SCAP Data Stream", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                
            }
        });

        newOvalMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                CreateOvalWizard wiz = new CreateOvalWizard(true);
                wiz.setName("create_oval_wizard");
                wiz.pack();
                wiz.setVisible(true);

                if (!wiz.wasCancelled()) {
                    // User has been through the wizard to select
                    // 1. an Oval schema version (eg, OVAL55)
                    // 2. one or more platforms (eg, "windows", "solaris", etc)
                    // 3. a file name for the new Oval file
                    // Now we are ready to actually create the file
                    String createdFilename = createNewOvalDocument(wiz);

                    if (createdFilename == null) {
                        LOG.error("newOvalMenuItem.actionlistener: Created filename was null!");
                        return;
                    }

                    File f = new File(createdFilename);

                    guiProps.setLastOpenedFromFile(f.getParentFile());
                    guiProps.save();

                    SCAPContentManager scm = SCAPContentManager.getInstance();

                    if (scm != null) {
                        OvalDefinitionsDocument dd = (OvalDefinitionsDocument) scm.getDocument(f.getAbsolutePath());

                        openFile(dd);
                    }
                }
                wiz.setVisible(false);
                wiz.dispose();
            }
        });

        newCPEDictionaryMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                NewCPEDictionaryWizard wiz = new NewCPEDictionaryWizard(EditorMainWindow.getInstance(), true);
                wiz.pack();
                wiz.setLocationRelativeTo(EditorMainWindow.getInstance());

                wiz.setVisible(true);

                if (!wiz.wasCancelled()) {
                    CPEDictionaryDocument cdd = wiz.getCPEDictDoc();

                    if (cdd == null) {
                        return;
                    }

                    File f = new File(cdd.getFilename());

                    guiProps.setLastOpenedFromFile(f.getParentFile());
                    guiProps.save();

                    SCAPContentManager scm = null;
                    try {
                        scm = SCAPContentManager.getInstance();
                    } catch (Exception eee) {
                        eee.printStackTrace();
                        return;
                    }

                    scm.addDocument(f.getAbsolutePath(), cdd);

                    if (scm != null) {
                        openFile(cdd);
                    }
                }
                wiz.setVisible(false);
                wiz.dispose();
            }
        });

        newCPEDictionaryFromXCCDFMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                /* If the user has an XCCDF document open, we'll go ahead and use that, otherwise we'll prompt them
                 * for an XCCDF file to use
                 */
                XCCDFBenchmark benchmark = null;

                // get the currently open window
                JInternalFrame selectedWin = desktopPane.getSelectedFrame();
                if (selectedWin != null) {
                    if (selectedWin instanceof XCCDFEditorForm) {
                        XCCDFEditorForm xef = (XCCDFEditorForm) selectedWin;
                        benchmark = (XCCDFBenchmark) xef.getDocument();
                    }
                } else {
                    // open a filechooser so the user can select the XCCDF file
                    final JFileChooser fc = new JFileChooser();
                    fc.setDialogType(JFileChooser.OPEN_DIALOG);

                    File lastOpenedFrom = guiProps.getLastOpenedFromFile();

                    // Set current directory
                    fc.setCurrentDirectory(lastOpenedFrom);

                    FileFilter ff = new XCCDFFilesFilter();
                    fc.setFileFilter(ff);
                    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                    int ret = fc.showOpenDialog(EditorMainWindow.getInstance());

                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File f = fc.getSelectedFile();
                        File parent = f.getAbsoluteFile().getParentFile();
                        guiProps.setLastOpenedFrom(parent.getAbsolutePath());
                        guiProps.save();

                        try {
                            benchmark = (XCCDFBenchmark) SCAPDocumentFactory.loadDocument(f);
                        } catch (Exception ex) {
                            LOG.error(ex.getMessage(), ex);

                            EditorUtil.showMessageDialog(parentWinRef,
                                    "An error occured trying to open " + EditorMessages.XCCDF + " file " + f.getAbsolutePath() + ": " + ex.getMessage(),
                                    "Open Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                if (benchmark == null) {
                    LOG.error("benchmark is null here, must return");
                    return;
                }
                SCAPDocumentBundle cpeDocBundle = null;
                try {
                    // check that there are platforms in the xccdf before we
                    // we proceed
                    Set<String> referencedCPEs = benchmark.getAllReferencedCPES();

                    if (referencedCPEs == null || referencedCPEs.size() == 0) {
                        // get out now
                        String message = "Unable to create a " + EditorMessages.CPE + " Dictionary for "
                                + EditorMessages.XCCDF + " file " + benchmark.getFilename() + ". "
                                + EditorMessages.XCCDF + " file must have at least one platform element to proceed.";

                        EditorUtil.showMessageDialog(parentWinRef,
                                message,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    cpeDocBundle = CommonUtil.buildCPEDictionaryAndOval(benchmark);
                } catch (Exception ex) {
                    LOG.error(ex.getMessage(), ex);

                    EditorUtil.showMessageDialog(parentWinRef,
                            "An error occured trying to generate a " + EditorMessages.CPE + " Dictionary for " + EditorMessages.XCCDF + " file " + benchmark.getFilename() + ": " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (cpeDocBundle != null) {
                    CPEDictionaryDocument cpeDict = cpeDocBundle.getCPEDictionaryDocs().get(0);
                    OvalDefinitionsDocument cpeOval = cpeDocBundle.getCPEOvalDocs().get(0);

                    openFile(cpeDict);
                    openFile(cpeOval);
                }
            }
        });
        

        wizModeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WizardModeWindow wizModeWin = new WizardModeWindow();

                EditorMainWindow emw = EditorMainWindow.getInstance();
                JDesktopPane emwDesktopPane = emw.getDesktopPane();

                wizModeWin.setTitle(EditorMessages.PRODUCT_NAME + " (Wizard Mode)");
                wizModeWin.pack();

                wizModeWin.addInternalFrameListener(new WeakInternalFrameListener(EditorMainWindow.getInstance()));

                Dimension dpDim = emwDesktopPane.getSize();
                int x = (dpDim.width - wizModeWin.getWidth()) / 2;
                int y = (dpDim.height - wizModeWin.getHeight()) / 2;

                wizModeWin.setLocation(x, y);
                emwDesktopPane.add(wizModeWin);
                wizModeWin.setVisible(true);

                setWizMode(true);
            }
        });
        
        

        openDocumentStreamMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                final JFileChooser fc = new JFileChooser();
                fc.setDialogType(JFileChooser.OPEN_DIALOG);

                File lastOpenedFrom = guiProps.getLastOpenedFromFile();

                // Set current directory
                fc.setCurrentDirectory(lastOpenedFrom);

                FileFilter ff = new StreamBundleFilesFilter();
                fc.setFileFilter(ff);
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int ret = fc.showOpenDialog(EditorMainWindow.getInstance());

                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();

                    File parent = f.getAbsoluteFile().getParentFile();
                    guiProps.setLastOpenedFrom(parent.getAbsolutePath());
                    guiProps.save();

                    if (f.getName().toLowerCase().endsWith(".xml")) {
                        try {
                            // first import an xccdf 1.2 document, make sure we can access new fields
                            Scap12DataStreamCollection collection =
                                    new Scap12DataStreamCollection(f);
                            LOG.debug("Input datastreamcollection file name: " + f.getName());

                            EditorMainWindow emw = EditorMainWindow.getInstance();
                            JDesktopPane emwDesktopPane = emw.getDesktopPane();

                            DataStreamBundleEditorForm sbef = new DataStreamBundleEditorForm(collection);
                            sbef.setSource(f.getAbsolutePath());

                            //zbef.pack();
                            Dimension dpDim = emwDesktopPane.getSize();
                            int x = (dpDim.width - sbef.getWidth()) / 2;
                            int y = (dpDim.height - sbef.getHeight()) / 2;

                            sbef.setLocation(x, y);
                            sbef.setVisible(true);
                            emwDesktopPane.add(sbef);
                            
                            incrementOpenDocuments();
                            
                        } catch (UnsupportedDocumentTypeException e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath() + ", UnsupportedDocumentTypeException", e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: This SCAP Document Type is currently unsupported",
                                    "Error opening SCAP Data Stream - Unsupported Document Type", JOptionPane.ERROR_MESSAGE);
                        } catch (IOException e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath() + ", IOException", e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: An IO error occured, please make sure the file is available and not corrupted.",
                                    "Error opening SCAP Data Stream - IO Exception", JOptionPane.ERROR_MESSAGE);
                        } catch (IllegalStateException e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath() + ", IllegalStateException", e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: Error occured parsing the XML, IllegalStateException",
                                    "Error opening SCAP Data Stream - IllegalStateException", JOptionPane.ERROR_MESSAGE);
                        } catch (JAXBException e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath() + ", JAXBException", e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: Error occured parsing the XML, check to make sure this document is an SCAP Data Stream.",
                                    "Error opening SCAP Data Stream - JAXBException", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath(), e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: Exception",
                                    "Error opening SCAP Data Stream", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

            }
        });

         ugMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    InputStream resource = this.getClass().getResourceAsStream("/User_Guide.pdf");
                    try {
                        File userGuideFile = File.createTempFile("UserGuide", ".pdf");
                        userGuideFile.deleteOnExit();
                        OutputStream out = new FileOutputStream(userGuideFile);
                        try {
                            // copy contents from resource to out
                            IOUtils.copy(resource, out);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Couldn't copy between streams.");
                        } finally {
                            out.close();
                        }
                        desktop.open(userGuideFile);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Could not call Open on desktop object.");
                    } finally {
                        try {
                        	if (resource != null) {
                        		resource.close();
                        	}
                        } catch (IOException ex) {
                        	LOG.error("Error displaying user guide", ex);
                            JOptionPane.showMessageDialog(null, "Desktop not supported. Cannot open user guide.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Desktop not supported. Cannot open user guide.");
                }
            }
        });
        
        
        openDocumentBundleMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                final JFileChooser fc = new JFileChooser();
                fc.setDialogType(JFileChooser.OPEN_DIALOG);

                File lastOpenedFrom = guiProps.getLastOpenedFromFile();

                // Set current directory
                fc.setCurrentDirectory(lastOpenedFrom);

                FileFilter ff = new BundleFilesFilter();
                fc.setFileFilter(ff);
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int ret = fc.showOpenDialog(EditorMainWindow.getInstance());

                if (ret == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();

                    File parent = f.getAbsoluteFile().getParentFile();
                    guiProps.setLastOpenedFrom(parent.getAbsolutePath());
                    guiProps.save();

                    if (f.getName().toLowerCase().endsWith(".zip")) {
                        try {
                            SCAPDocumentBundle bundle = SCAPDocumentBundle.parseBundle(f);
                            EditorMainWindow emw = EditorMainWindow.getInstance();
                            JDesktopPane emwDesktopPane = emw.getDesktopPane();

                            ZipBundleEditorForm zbef = new ZipBundleEditorForm(bundle);
                            zbef.setSource(f.getAbsolutePath());

                            //zbef.pack();
                            Dimension dpDim = emwDesktopPane.getSize();
                            int x = (dpDim.width - zbef.getWidth()) / 2;
                            int y = (dpDim.height - zbef.getHeight()) / 2;

                            zbef.setLocation(x, y);
                            zbef.setVisible(true);
                            emwDesktopPane.add(zbef);
                        } catch (IOException e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath() + ", IOException", e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: An IO error occured, please make sure the file is available and not corrupted.",
                                    "Error opening SCAP Data Bundle - IO Exception", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception e) {
                            LOG.error("Error opening document: " + f.getAbsolutePath(), e);
                            JOptionPane.showMessageDialog(parentWinRef,
                                    "Error opening document: " + f.getAbsolutePath() + "\nMessage: Exception",
                                    "Error opening SCAP Data Bundle", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (f.getName().toLowerCase().endsWith("xccdf.xml")) {
                        try {
                            SCAPDocumentBundle bundle = SCAPDocumentBundle.parseBundle(f);
                            EditorMainWindow emw = EditorMainWindow.getInstance();
                            JDesktopPane emwDesktopPane = emw.getDesktopPane();

                            ZipBundleEditorForm zbef = new ZipBundleEditorForm(bundle);
                            zbef.setSource(f.getAbsolutePath());

                            //zbef.pack();
                            Dimension dpDim = emwDesktopPane.getSize();
                            int x = (dpDim.width - zbef.getWidth()) / 2;
                            int y = (dpDim.height - zbef.getHeight()) / 2;

                            zbef.setLocation(x, y);
                            zbef.setVisible(true);
                            emwDesktopPane.add(zbef);
                        } catch (Exception e) {
                            LOG.error("Error opening document bundle: " + f.getAbsolutePath(), e);
                        }
                    }
                }
            }
        });
    }
    
    private void generateXccdfFromOvalOrOcil(String fileType) {
        
         SCAPDocument scapDoc = null;

            // get the currently open window
         if (fileType.equals("OVAL")) {
            JInternalFrame selectedWin = desktopPane.getSelectedFrame();
            if (selectedWin != null) {
                if (selectedWin instanceof OvalEditorForm) {
                    OvalEditorForm oef = (OvalEditorForm) selectedWin;
                    scapDoc = oef.getDocument();
                }
            }
         }
         
         if (scapDoc == null) {
            // open a filechooser so the user can select the oval file
            final JFileChooser fc = new JFileChooser();
            fc.setDialogType(JFileChooser.OPEN_DIALOG);

            File lastOpenedFrom = guiProps.getLastOpenedFromFile();

            // Set current directory
            fc.setCurrentDirectory(lastOpenedFrom);

            FileFilter ff = new OcilOrOvalFilesFilter(fileType);
            fc.setFileFilter(ff);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int ret = fc.showOpenDialog(EditorMainWindow.getInstance());

            if (ret == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                File parent = f.getAbsoluteFile().getParentFile();
                guiProps.setLastOpenedFrom(parent.getAbsolutePath());
                guiProps.save();

                try {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    scapDoc = SCAPDocumentFactory.loadDocument(f);
                } catch (Exception e) {
                    setCursor(null);
                    LOG.error(e.getMessage(), e);
                    String message = "An error occured trying to open " + fileType + " file " + f.getAbsolutePath() + ": " + e.getMessage();
                    EditorUtil.showMessageDialog(this,
                            message,
                            "Open Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        setCursor(null);

        if (scapDoc == null) {
            LOG.error("ovalDefinitionsDoc is null here, must return");
            return;
        }

        XCCDFGen xg = new XCCDFGen(EditorMainWindow.getInstance(), false, scapDoc);

        xg.pack();
        xg.setModal(true);
        xg.setLocationRelativeTo(EditorMainWindow.getInstance());
        try {
            xg.setVisible(true);

            if (!xg.wasCancelled()) {
                if (xg.getBenchmark() != null) {
                    openFile((SCAPDocument) xg.getBenchmark());
                }
            }
        } catch (Exception ex) {
            setCursor(null);
            LOG.error(ex.getMessage(), ex);
            String message = "An error occured trying to generate an "
                    + EditorMessages.XCCDF + " file for " + fileType + " file "
                    + scapDoc.getFilename() + ": " + ex.getMessage();
            EditorUtil.showMessageDialog(this,
                    message,
                    "Open Error",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }
    }


    private void initComponents2() {
        addWindowListener(this);

        initFilemenu();
        initToolsMenu();
        initHelpMenu();
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

        navPanel = new com.g2inc.scap.editor.gui.windows.common.NavigationPanel();
        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newSubMenu = new javax.swing.JMenu();
        newCPEDictionaryMenu = new javax.swing.JMenu();
        newCPEDictionaryMenuItem = new javax.swing.JMenuItem();
        newCPEDictionaryFromXCCDFMenuItem = new javax.swing.JMenuItem();
        newOvalMenuItem = new javax.swing.JMenuItem();
        newXCCDFSubMenu = new javax.swing.JMenu();
        newXCCDFMenuItem = new javax.swing.JMenuItem();
        newXCCDFFromOvalMenuItem = new javax.swing.JMenuItem();
        newXCCDFFromOcilMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        newBundleSubMenu = new javax.swing.JMenu();
        newBundleFromXCCDFMenuItem = new javax.swing.JMenuItem();
        newScapDataStreamMenuItem = new javax.swing.JMenuItem();
        openSubMenu = new javax.swing.JMenu();
        openCPEDictionaryMenuItem = new javax.swing.JMenuItem();
        openOvalMenuItem = new javax.swing.JMenuItem();
        openXCCDFMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        openDocumentBundleMenuItem = new javax.swing.JMenuItem();
        openDocumentStreamMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        mostRecentOpenDocsMenu = new com.g2inc.scap.editor.gui.windows.common.mrufiles.MRUDocumentsMenu();
        jSeparator4 = new javax.swing.JSeparator();
        wizModeMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        regexValidatorMenuItem = new javax.swing.JMenuItem();
        validateDocumentMenuItem = new javax.swing.JMenuItem();
        ovalMergeMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        ugMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setName("mainWin"); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(navPanel, gridBagConstraints);

        desktopPane.setBackground(new java.awt.Color(153, 153, 153));
        desktopPane.setMinimumSize(new java.awt.Dimension(800, 600));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.97;
        getContentPane().add(desktopPane, gridBagConstraints);

        fileMenu.setBorder(null);
        fileMenu.setText("File");

        newSubMenu.setText("New");

        newCPEDictionaryMenu.setText("CPE Dictionary");

        newCPEDictionaryMenuItem.setText("Blank");
        newCPEDictionaryMenuItem.setToolTipText("Create a new empty CPE dictionary");
        newCPEDictionaryMenu.add(newCPEDictionaryMenuItem);

        newCPEDictionaryFromXCCDFMenuItem.setText("From XCCDF");
        newCPEDictionaryFromXCCDFMenuItem.setToolTipText("Create a new CPE Dictionary containing CPE items derived from platform entries in an existing XCCDF benchmark.");
        newCPEDictionaryMenu.add(newCPEDictionaryFromXCCDFMenuItem);

        newSubMenu.add(newCPEDictionaryMenu);

        newOvalMenuItem.setText("OVAL");
        newOvalMenuItem.setToolTipText("Create a new empty OVAL definitions document");
        newSubMenu.add(newOvalMenuItem);

        newXCCDFSubMenu.setText("XCCDF");

        newXCCDFMenuItem.setText("Blank");
        newXCCDFMenuItem.setToolTipText("Create a new empty XCCDF benchmark");
        newXCCDFSubMenu.add(newXCCDFMenuItem);

        newXCCDFFromOvalMenuItem.setText("From OVAL");
        newXCCDFSubMenu.add(newXCCDFFromOvalMenuItem);

        newXCCDFFromOcilMenuItem.setText("From OCIL");
        newXCCDFSubMenu.add(newXCCDFFromOcilMenuItem);

        newSubMenu.add(newXCCDFSubMenu);
        newSubMenu.add(jSeparator5);

        newBundleSubMenu.setText("SCAP Data Bundle");
        newBundleSubMenu.setToolTipText("Create an SCAP Data Stream from an existing document");

        newBundleFromXCCDFMenuItem.setText("From XCCDF");
        newBundleSubMenu.add(newBundleFromXCCDFMenuItem);

        newSubMenu.add(newBundleSubMenu);

        newScapDataStreamMenuItem.setText("SCAP Data Stream");
        newSubMenu.add(newScapDataStreamMenuItem);

        fileMenu.add(newSubMenu);

        openSubMenu.setText("Open");

        openCPEDictionaryMenuItem.setText("CPE Dictionary");
        openSubMenu.add(openCPEDictionaryMenuItem);

        openOvalMenuItem.setText("OVAL");
        openSubMenu.add(openOvalMenuItem);

        openXCCDFMenuItem.setText("XCCDF");
        openSubMenu.add(openXCCDFMenuItem);
        openSubMenu.add(jSeparator1);

        openDocumentBundleMenuItem.setText("SCAP Data Bundle");
        openDocumentBundleMenuItem.setToolTipText("Open an existing bundle of SCAP documents.");
        openSubMenu.add(openDocumentBundleMenuItem);

        openDocumentStreamMenuItem.setText("SCAP Data Stream");
        openSubMenu.add(openDocumentStreamMenuItem);

        fileMenu.add(openSubMenu);
        fileMenu.add(jSeparator2);

        saveMenuItem.setText("Save");
        saveMenuItem.setEnabled(false);
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save As");
        saveAsMenuItem.setEnabled(false);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(jSeparator3);

        mostRecentOpenDocsMenu.setEnabled(false);
        fileMenu.add(mostRecentOpenDocsMenu);
        fileMenu.add(jSeparator4);

        wizModeMenuItem.setText("Wizard Mode");
        fileMenu.add(wizModeMenuItem);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        toolsMenu.setText("Tools");

        regexValidatorMenuItem.setText("Regex Validator");
        toolsMenu.add(regexValidatorMenuItem);

        validateDocumentMenuItem.setText("Validate");
        validateDocumentMenuItem.setEnabled(false);
        toolsMenu.add(validateDocumentMenuItem);

        ovalMergeMenuItem.setText("Merge OVAL Documents");
        toolsMenu.add(ovalMergeMenuItem);

        menuBar.add(toolsMenu);

        helpMenu.setText("Help");

        ugMenuItem.setText("User Guide (.pdf)");
        helpMenu.add(ugMenuItem);

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_exitMenuItemActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JMenuBar menuBar;
    private com.g2inc.scap.editor.gui.windows.common.mrufiles.MRUDocumentsMenu mostRecentOpenDocsMenu;
    private com.g2inc.scap.editor.gui.windows.common.NavigationPanel navPanel;
    private javax.swing.JMenuItem newBundleFromXCCDFMenuItem;
    private javax.swing.JMenu newBundleSubMenu;
    private javax.swing.JMenuItem newCPEDictionaryFromXCCDFMenuItem;
    private javax.swing.JMenu newCPEDictionaryMenu;
    private javax.swing.JMenuItem newCPEDictionaryMenuItem;
    private javax.swing.JMenuItem newOvalMenuItem;
    private javax.swing.JMenuItem newScapDataStreamMenuItem;
    private javax.swing.JMenu newSubMenu;
    private javax.swing.JMenuItem newXCCDFFromOcilMenuItem;
    private javax.swing.JMenuItem newXCCDFFromOvalMenuItem;
    private javax.swing.JMenuItem newXCCDFMenuItem;
    private javax.swing.JMenu newXCCDFSubMenu;
    private javax.swing.JMenuItem openCPEDictionaryMenuItem;
    private javax.swing.JMenuItem openDocumentBundleMenuItem;
    private javax.swing.JMenuItem openDocumentStreamMenuItem;
    private javax.swing.JMenuItem openOvalMenuItem;
    private javax.swing.JMenu openSubMenu;
    private javax.swing.JMenuItem openXCCDFMenuItem;
    private javax.swing.JMenuItem ovalMergeMenuItem;
    private javax.swing.JMenuItem regexValidatorMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JMenuItem ugMenuItem;
    private javax.swing.JMenuItem validateDocumentMenuItem;
    private javax.swing.JMenuItem wizModeMenuItem;
    // End of variables declaration//GEN-END:variables

        public NavigationPanel getNavPanel() {
        return navPanel;
    }

    public JMenuItem getValidateDocumentMenuItem() {
        return validateDocumentMenuItem;
    }

    public JMenuItem getExitMenuItem() {
        return exitMenuItem;
    }

    public EditorForm[] getOpenEditors() {
        EditorForm[] forms = null;
        ArrayList<EditorForm> formList = new ArrayList<EditorForm>();
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] instanceof EditorForm) {
                formList.add((EditorForm) frames[i]);
            }
        }
        forms = formList.toArray(new EditorForm[0]);
        return forms;
    }

    public void addOvalForm(OvalEditorForm form) {
        form.setClosable(true);
        form.setParentWin(this);
        form.setVisible(true);
        desktopPane.add(form);

        try {
            form.setSelected(true);
            form.setMaximizable(true);
            form.setResizable(true);
            int childMinX = getMinimumSize().width - 50;
            int childMinY = getMinimumSize().height - 50;
            form.setMinimumSize(new Dimension(childMinX, childMinY));
            form.setMaximum(true);
        } catch (PropertyVetoException pve) {
        }
    }

    private String createNewOvalDocument(CreateOvalWizard wiz) {
        File newFile = new File(wiz.getFilename());
        String newFileName = newFile.getAbsolutePath();
        LOG.debug("Creating new " + EditorMessages.OVAL + " file:" + newFileName);

        SCAPContentManager manager = null;
        try {
            SCAPDocument sdoc = SCAPDocumentFactory.createNewDocument(wiz.getSchemaType());
            sdoc.setFilename(newFile.getAbsolutePath());
            manager = SCAPContentManager.getInstance();

            try {
                OvalDefinitionsDocument odd = (OvalDefinitionsDocument) sdoc;
                odd.setGeneratorProduct(EditorMessages.PRODUCT_NAME);
                odd.setGeneratorProductVersion(EditorConfiguration.getInstance().getEditorVersion());
                odd.setGeneratorDate(new Date(System.currentTimeMillis()));

                sdoc.save();
            } catch (Exception e) {
                LOG.error("Exception creating new " + EditorMessages.OVAL + " document of type " + wiz.getSchemaType().toString(), e);

                EditorUtil.showMessageDialog(EditorMainWindow.getInstance(),
                        "An error occured trying to save to file " + newFileName + ": " + e.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            manager.addDocument(newFileName, sdoc);
        } catch (Exception e) {
            LOG.error("IO Error importing new file into SCAPContentManager", e);
            return null;
        }

        return newFileName;
    }

    private String createNewScapDataStream(String filePath) {
        
        File newFile = new File(filePath);
        String newFileName = newFile.getAbsolutePath();
        LOG.debug("Creating new " + EditorMessages.SCAP_DATA_STREAM + ": " + newFileName);

        SCAPContentManager manager = null;
        try {
            
            Scap12DataStreamCollection col = new Scap12DataStreamCollection(filePath);
            col.save();
            
            //SCAPDocument sdoc = SCAPDocumentFactory.createNewDocument(SCAPDocumentTypeEnum.SCAP_12_DATA_STREAM);
            //sdoc.setFilename(newFile.getAbsolutePath());
            manager = SCAPContentManager.getInstance();
            
            // TODO - Figure out adding a collection to the manager
            //manager.addDocument(newFileName, col);
        } catch (Exception e) {
            LOG.error("IO Error importing new file into SCAPContentManager", e);
            return null;
        }

        return newFileName;
    }

    private String createNewXCCDFDocument(CreateXCCDFWizard wiz) {
        File newFile = new File(wiz.getFilename());
        String newFileName = newFile.getAbsolutePath();
        LOG.debug("Creating new " + EditorMessages.OVAL + " file:" + newFileName);

        SCAPContentManager manager = null;
        try {
            SCAPDocument sdoc = SCAPDocumentFactory.createNewDocument(wiz.getSchemaType());
            sdoc.setFilename(newFile.getAbsolutePath());
            manager = SCAPContentManager.getInstance();

            try {
                XCCDFBenchmark benchmark = (XCCDFBenchmark) sdoc;

                CreateNewXCCDFParametersPage parmPage = wiz.getParmChoicePage();

                benchmark.setId(parmPage.getChosenBenchmarkId());
                benchmark.setTitle(parmPage.getChosenBenchmarkDescription());
                benchmark.save();
            } catch (Exception e) {
                LOG.error("Exception creating new " + EditorMessages.XCCDF + " document of type " + wiz.getSchemaType().toString(), e);

                EditorUtil.showMessageDialog(EditorMainWindow.getInstance(),
                        "An error occured trying to save to file " + newFileName + ": " + e.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            manager.addDocument(newFileName, sdoc);
        } catch (Exception e) {
            LOG.error("IO Error importing new file into SCAPContentManager", e);
            return null;
        }

        return newFileName;
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
        // try to restore window location and bounds
        String strWidth = guiProps.getProperty(EditorConfiguration.PROPERTY_MAIN_WINDOW_SIZE_X);
        int intWidth = getMinimumSize().width;

        String strHeight = guiProps.getProperty(EditorConfiguration.PROPERTY_MAIN_WINDOW_SIZE_Y);
        int intHeight = getMinimumSize().height;

        if (strWidth != null && !strWidth.trim().equals("")) {
            intWidth = Integer.parseInt(strWidth);
        }

        if (strHeight != null && !strHeight.trim().equals("")) {
            intHeight = Integer.parseInt(strHeight);
        }

        setSize(intWidth, intHeight);

        Point myLocation = null;
        String strLocX = guiProps.getProperty(EditorConfiguration.PROPERTY_MAIN_WINDOW_LOCATION_X);
        int intLocX = 0;

        String strLocY = guiProps.getProperty(EditorConfiguration.PROPERTY_MAIN_WINDOW_LOCATION_Y);
        int intLocY = 0;

        if (strLocX != null && !strLocX.trim().equals("")) {
            intLocX = Integer.parseInt(strLocX.trim());
        }
        if (strLocY != null && !strLocY.trim().equals("")) {
            intLocY = Integer.parseInt(strLocY.trim());
        }

        if (intLocX == 0 && intLocY == 0) {
            // center it
            setLocationRelativeTo(null);
        } else {
            myLocation = new Point(intLocX, intLocY);
            setLocation(myLocation);
        }

        if (filesFromCommandLine != null && filesFromCommandLine.size() > 0) {
            for (int x = 0; x < filesFromCommandLine.size(); x++) {
                File f = filesFromCommandLine.get(x);

                openMRUFile(f);
            }

            filesFromCommandLine.clear();
        } else {
            ModeChooserDialog mcd = new ModeChooserDialog(this, true);

            mcd.setLocationRelativeTo(EditorMainWindow.getInstance());
            mcd.setVisible(true);
        }
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        // save window location and bounds
        Point myLocation = getLocation();
        Dimension mySize = getSize();

        guiProps.setProperty(EditorConfiguration.PROPERTY_MAIN_WINDOW_LOCATION_X, myLocation.x + "");
        guiProps.setProperty(EditorConfiguration.PROPERTY_MAIN_WINDOW_LOCATION_Y, myLocation.y + "");

        guiProps.setProperty(EditorConfiguration.PROPERTY_MAIN_WINDOW_SIZE_X, mySize.width + "");
        guiProps.setProperty(EditorConfiguration.PROPERTY_MAIN_WINDOW_SIZE_Y, mySize.height + "");

        guiProps.save();

        // cycle through open internal frames(documents)
        // and see if any of them need to be saved
        JInternalFrame[] openFrames = desktopPane.getAllFrames();

        if (openFrames != null && openFrames.length > 0) {
            for (int x = 0; x < openFrames.length; x++) {
                JInternalFrame internalWin = openFrames[x];
                if (internalWin instanceof EditorForm) {
                    EditorForm ef = (EditorForm) internalWin;
                    if (ef.getDocument() == null) {
                        continue;
                    }

                    String filename = ef.getDocument().getFilename();

                    if (filename == null) {
                        continue;
                    }

                    if (ef.isDirty()) {
                        Object[] options = {EditorMessages.FILE_CHOOSER_BUTTON_TEXT_SAVE, "Discard"};

                        String message = filename + " has unsaved changes.  Do you want to save or discard changes?";
                        String dTitle = "Unsaved changes";

                        int n = JOptionPane.showOptionDialog(this, message,
                                dTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                        if (n == JOptionPane.DEFAULT_OPTION || n == JOptionPane.YES_OPTION) {
                            final GenericProgressDialog progress = new GenericProgressDialog(EditorMainWindow.getInstance(), true);
                            progress.setMessage("Saving changes to " + ef.getDocument().getFilename());
                            try {
                                final SCAPDocument sdoc = (SCAPDocument) ef.getDocument();

                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            sdoc.save();
                                        } catch (Exception e) {
                                            progress.setException(e);
                                        }
                                    }
                                };

                                progress.setRunnable(r);

                                progress.pack();
                                progress.setLocationRelativeTo(this);
                                progress.setVisible(true);

                                if (progress.getException() != null) {
                                    throw (progress.getException());
                                }

                                ef.setDirty(false);
                            } catch (Exception e) {
                                LOG.error("Error saving file " + filename, e);
                                String errMessage = filename + " couldn't be saved.  An error occured: " + e.getMessage();
                                dTitle = "File save error";

                                EditorUtil.showMessageDialog(this, errMessage, dTitle, JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        System.gc();
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

    public void markActiveWindowDirty(boolean b) {
        JInternalFrame selectedFrame = desktopPane.getSelectedFrame();

        if (selectedFrame == null) {
            return;
        }

        if (selectedFrame instanceof EditorForm) {
            ((EditorForm) selectedFrame).setDirty(b);
        }
    }

    public EditorForm getActiveEditorForm() {
        JInternalFrame selectedFrame = desktopPane.getSelectedFrame();

        if (selectedFrame == null) {
            return null;
        }

        if (selectedFrame instanceof EditorForm) {
            return (EditorForm) selectedFrame;
        } else {
            return null;
        }
    }

    /**
     * Implemented because this class implements ChangeListener.
     *
     * @param e The ChangeEvent we are processing
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e instanceof MRUFileSelectedEvent) {
            MRUFileSelectedEvent mruEvent = (MRUFileSelectedEvent) e;

            File f = mruEvent.getFile();
            mostRecentOpenDocsMenu.addItem(f, this);
            openMRUFile(f);
        }
    }

    private void openMRUFile(File f) {
        SCAPDocument sdoc = null;
        Charset alternateEncoding = Charset.forName("UTF-8");

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            sdoc = SCAPDocumentFactory.loadDocument(f, alternateEncoding);
            if (sdoc == null) {
                // if the document is null here it's probably an unsupported version
                throw new UnsupportedDocumentTypeException("The document type is not supported at this time");
            }
        } catch (UnsupportedDocumentTypeException udte) {
            setCursor(null);
            String message = "The document contained in file " + f.getAbsolutePath() + " could not be loaded: "
                    + udte.getMessage();

            EditorUtil.showMessageDialog(this,
                    message,
                    EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
            return;

        } catch (CharacterSetEncodingException e) {
            setCursor(null);
            AlternateEncodingPicker encPicker = new AlternateEncodingPicker(this, true);

            encPicker.pack();
            encPicker.setLocationRelativeTo(this);
            encPicker.setVisible(true);

            if (!encPicker.wasCancelled()) {
                alternateEncoding = encPicker.getSelectedEncoding();
            } else {
                setCursor(null);
                String message = "Opening of file " + f.getName() + " Cancelled: "
                        + e.getMessage();

                EditorUtil.showMessageDialog(this,
                        message,
                        EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (IOException e) {
            setCursor(null);
            LOG.error("EXCEPTION", e);
            String message = "File " + f.getName() + " couldn't be opened because of an I/O issue: "
                    + e.getMessage();

            EditorUtil.showMessageDialog(this,
                    message,
                    EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception eee) {
            setCursor(null);
            LOG.error("Exception", eee);
            String message = "An exception occurred trying to open file " + f.getAbsolutePath() + ", see log file for details";
            EditorUtil.showMessageDialog(this,
                    message,
                    EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        setCursor(null);

        if (sdoc != null) {
            openFile(sdoc);
        } else {
            String message = "There was a problem trying to open file " + f.getAbsolutePath() + ": document returned was NULL";
            EditorUtil.showMessageDialog(this,
                    message,
                    EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void openFile(File f, SCAPDocumentClassEnum expectedClass) {
        LOG.debug("openFile entered for file " + (f == null ? "null" : f.getAbsolutePath()));
        SCAPDocument sdoc = null;
        Charset alternateEncoding = Charset.forName("UTF-8");

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            sdoc = SCAPDocumentFactory.loadDocument(f, alternateEncoding);
            if (sdoc == null) {
                // if the document is null here it's probably an unsupported version
                throw new UnsupportedDocumentTypeException("The document type is not supported at this time");
            }
        } catch (UnsupportedDocumentTypeException udte) {
            setCursor(null);
            String message = "The document contained in file " + f.getAbsolutePath() + " could not be loaded: "
                    + udte.getMessage();

            EditorUtil.showMessageDialog(this,
                    message,
                    EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
            return;

        } catch (CharacterSetEncodingException e) {
            setCursor(null);
            AlternateEncodingPicker encPicker = new AlternateEncodingPicker(this, true);

            encPicker.pack();
            encPicker.setLocationRelativeTo(this);
            encPicker.setVisible(true);

            if (!encPicker.wasCancelled()) {
                alternateEncoding = encPicker.getSelectedEncoding();
            } else {
                setCursor(null);
                String message = "Opening of file " + f.getName() + " Cancelled: "
                        + e.getMessage();

                EditorUtil.showMessageDialog(this,
                        message,
                        EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (IOException e) {
            setCursor(null);
            LOG.error("EXCEPTION", e);
            String message = "File " + f.getName() + " couldn't be opened because of an I/O issue: "
                    + e.getMessage();

            EditorUtil.showMessageDialog(this,
                    message,
                    EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception eee) {
            setCursor(null);
            LOG.error("Exception", eee);
            String message = "An exception occurred trying to open file " + f.getAbsolutePath() + ", see log file for details";

            EditorUtil.showMessageDialog(this,
                    message,
                    EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        setCursor(null);

        if (sdoc != null) {
            SCAPDocumentClassEnum actualClass = sdoc.getDocumentClass();
            LOG.debug("openFile: document class is " + actualClass + ", expected is " + expectedClass);
            if (!expectedClass.equals(actualClass)) {
                setCursor(null);
                String message = "File " + f.getName() + " type mismatch: expected="
                        + expectedClass.toString() + " actual=" + actualClass.toString();

                EditorUtil.showMessageDialog(this,
                        message,
                        EditorMessages.DOCUMENT_MISMATCH_ERROR_TITLE,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            mostRecentOpenDocsMenu.addItem(f, this);
            saveMRUFiles();
            openFile(sdoc);
        } else {
            String message = "There was a problem trying to open file " + f.getAbsolutePath() + ": document returned was NULL";

            EditorUtil.showMessageDialog(this,
                    message,
                    EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        LOG.debug("openFile returning for file " + (f == null ? "null" : f.getAbsolutePath()));
    }

    public void openFile(SCAPDocument sdoc) {
        File f = new File(sdoc.getFilename()).getAbsoluteFile();
        LOG.debug("openFile(SCAPDoc) entered for file " + (f == null ? "null" : f.getAbsolutePath()));
        mostRecentOpenDocsMenu.addItem(f, this);
        saveMRUFiles();

        EditorForm editorForm = null;

        switch (sdoc.getDocumentType()) {
            case CPE_20:
            case CPE_21:
            case CPE_22:
            case CPE_23:
                editorForm = new CPEDictionaryEditorForm();
                final CPEDictionaryEditorForm cpeEfRef = (CPEDictionaryEditorForm) editorForm;
                final CPEDictionaryDocument cpeDocRef = (CPEDictionaryDocument) sdoc;

                Runnable openCPERunnable = new Runnable() {
                    @Override
                    public void run() {
                        cpeEfRef.setParentWin(EditorMainWindow.getInstance());
                        cpeEfRef.setDocument(cpeDocRef);
                    }
                };

                GenericProgressDialog openCPEProgress = new GenericProgressDialog(this, true);
                openCPEProgress.setRunnable(new WeakRunnable(openCPERunnable));
                openCPEProgress.setMessage("Preparing to display " + EditorMessages.CPE + " Dictionary document");
                openCPEProgress.pack();
                openCPEProgress.setLocationRelativeTo(this);
                openCPEProgress.setVisible(true);

                openCPEProgress.dispose();

                editorForm.setTitle(EditorMessages.CPE_EDITOR_FORM_BASE_TITLE + f.getAbsolutePath());
                break;
            case OVAL_53:
            case OVAL_54:
            case OVAL_55:
            case OVAL_56:
            case OVAL_57:
            case OVAL_58:
            case OVAL_59:
            case OVAL_510:
                LOG.debug("Constructing OvalEditorForm");
                editorForm = new OvalEditorForm();
                final OvalEditorForm ovalEfRef = (OvalEditorForm) editorForm;
                final OvalDefinitionsDocument ovalDocRef = (OvalDefinitionsDocument) sdoc;

                LOG.debug("Constructing Runnable");
                Runnable openOvalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        LOG.debug("Run method entered");
                        ovalEfRef.setParentWin(EditorMainWindow.getInstance());
                        LOG.debug("Run method calling setDocument");
                        ovalEfRef.setDocument(ovalDocRef);
                        LOG.debug("Run method returning");
                    }
                };

                LOG.debug("Building Progress Dialog");
                GenericProgressDialog openOvalProgress = new GenericProgressDialog(this, true);
                openOvalProgress.setRunnable(new WeakRunnable(openOvalRunnable));
                openOvalProgress.setMessage("Preparing to display " + EditorMessages.OVAL + " document");
                openOvalProgress.pack();
                openOvalProgress.setLocationRelativeTo(this);
                openOvalProgress.setVisible(true);
                LOG.debug("Progress dialog is visible");

                openOvalProgress.dispose();

                LOG.debug("Progress dialog disposed");
                editorForm.setTitle(EditorMessages.OVAL_EDITOR_FORM_BASE_TITLE + f.getAbsolutePath());
                LOG.debug("Title is set");
                break;
            case XCCDF_114:
            case XCCDF_12:
                LOG.debug("Constructing XCCDFEditorForm");
                editorForm = new XCCDFEditorForm();
                final XCCDFEditorForm xEfRef = (XCCDFEditorForm) editorForm;
                final XCCDFBenchmark xDocRef = (XCCDFBenchmark) sdoc;

                LOG.debug("Constructing Runnable");
                Runnable openXCCDFRunnable = new Runnable() {
                    @Override
                    public void run() {
                        LOG.debug("Run method entered");
                        xEfRef.setParentWin(EditorMainWindow.getInstance());
                        LOG.debug("Run method calling setDocument");
                        xEfRef.setDocument(xDocRef);
                        LOG.debug("Run method returning");
                    }
                };
                LOG.debug("Building Progress Dialog");
                GenericProgressDialog openXCCDFProgress = new GenericProgressDialog(this, true);
                openXCCDFProgress.setRunnable(new WeakRunnable(openXCCDFRunnable));
                openXCCDFProgress.setMessage("Preparing to display " + EditorMessages.XCCDF + " document");
                openXCCDFProgress.pack();
                openXCCDFProgress.setLocationRelativeTo(this);
                openXCCDFProgress.setVisible(true);
                LOG.debug("Progress dialog is visible");

                openXCCDFProgress.dispose();

                LOG.debug("Progress dialog disposed");
                editorForm.setTitle(EditorMessages.XCCDF_EDITOR_FORM_BASE_TITLE + f.getAbsolutePath());
                LOG.debug("Title is set");
                break;
            default:
                JOptionPane.showMessageDialog(this,
                        "The file being opened is not one that we know how to process: " + f.getAbsolutePath(),
                        EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                        JOptionPane.ERROR_MESSAGE);
                return;
        }

        LOG.debug("Adding frame listener to editor form");
        editorForm.addInternalFrameListener(new WeakInternalFrameListener(EditorMainWindow.getInstance()));

        editorForm.setClosable(true);
        editorForm.pack();
        LOG.debug("Setting editor form visible");
        editorForm.setVisible(true);
        LOG.debug("Adding editor form to desktopPane");
        desktopPane.add(editorForm);

        try {
            editorForm.setSelected(true);
            editorForm.setMaximizable(true);
            editorForm.setResizable(true);
            int childMinX = getMinimumSize().width - 50;
            int childMinY = getMinimumSize().height - 50;
            editorForm.setMinimumSize(new Dimension(childMinX, childMinY));
            editorForm.setMaximum(false);

            LOG.debug("Moving editor form to fronto of desktopPane");
            desktopPane.moveToFront(editorForm);
            incrementOpenDocuments();
        } catch (Throwable t) {
            throw new IllegalStateException("Error caught trying to set editor form", t);
        }

        LOG.debug("OpenFile returning");
        //cascade(desktopPane);
    }

    public JMenuItem getNewOvalMenuItem() {
        return newOvalMenuItem;
    }

    @Override
    public void dispose() {
        super.dispose();

        reset();
    }

    public static void reset() {
        instance = null;
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    public synchronized void setWizMode(boolean b) {
        wizardMode = b;

        updateMenus();
    }

    public synchronized void incrementOpenDocuments() {
        openDocuments++;
        updateMenus();
    }

    public synchronized void decrementOpenDocuments() {
        if (openDocuments > 0) {
            openDocuments--;
            updateMenus();
        }
    }

    // borrowed example code from http://www.javalobby.org/forums/thread.jspa?threadID=15690&tstart=0
    public void cascade(JDesktopPane desktopPane, int layer) {
        JInternalFrame[] frames = desktopPane.getAllFramesInLayer(layer);
        if (frames.length == 0) {
            return;
        }

        cascade(frames, desktopPane.getBounds(), 24);
    }

    // borrowed example code from http://www.javalobby.org/forums/thread.jspa?threadID=15690&tstart=0
    public void cascade(JDesktopPane desktopPane) {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        if (frames.length == 0) {
            return;
        }

        cascade(frames, desktopPane.getBounds(), 24);
    }

    // borrowed example code from http://www.javalobby.org/forums/thread.jspa?threadID=15690&tstart=0
    private void cascade(JInternalFrame[] frames, Rectangle dBounds, int separation) {
        int margin = frames.length * separation + separation;
        int width = dBounds.width - margin;
        int height = dBounds.height - margin;
        for (int i = 0; i < frames.length; i++) {
            frames[i].setBounds(separation + dBounds.x + i * separation,
                    separation + dBounds.y + i * separation,
                    width, height);
        }
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        decrementOpenDocuments();

        JInternalFrame internalFrame = (JInternalFrame) e.getInternalFrame();

        if (internalFrame instanceof WizardModeWindow) {
            setWizMode(false);
        }

        InternalFrameListener[] frameListeners = internalFrame.getInternalFrameListeners();

        if (frameListeners != null) {
            for (int x = 0; x < frameListeners.length; x++) {
                internalFrame.removeInternalFrameListener(frameListeners[x]);
            }

            frameListeners = null;
        }

        decrementOpenDocuments();

        desktopPane.remove(internalFrame);

        if (internalFrame instanceof OvalEditorForm) {
            ((OvalEditorForm) internalFrame).cleanUp();
        } else if (internalFrame instanceof XCCDFEditorForm) {
            ((XCCDFEditorForm) internalFrame).cleanUp();

        } else if (internalFrame instanceof CPEDictionaryEditorForm) {
            ((CPEDictionaryEditorForm) internalFrame).cleanUp();
        }

        internalFrame.dispose();
        navPanel.clearButtons();
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
        System.gc();
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
        incrementOpenDocuments();
    }

    public void setFilesFromCommandLine(List<File> filesFromCommandLine) {
        this.filesFromCommandLine = filesFromCommandLine;
    }
    
}
