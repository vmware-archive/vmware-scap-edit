package com.g2inc.scap.editor.gui.windows.datastream;

//import gov.nist.scap.validation.core.UseCase;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Enumeration;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.cellrenderers.tree.bundle.SCAPBundleTreeCellRenderer;
import com.g2inc.scap.editor.gui.dialogs.editors.DisplayDialog;
import com.g2inc.scap.editor.gui.model.tree.bundle.SCAPBundleTreeModel;
import com.g2inc.scap.editor.gui.model.tree.oval.DocumentTreeModel;
import com.g2inc.scap.editor.gui.model.tree.stream.SCAPStreamTreeModel;
import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.CPEDictionaryFilesFilter;
import com.g2inc.scap.editor.gui.windows.EditorConfiguration;
import com.g2inc.scap.editor.gui.windows.EditorForm;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.OcilOrOvalFilesFilter;
import com.g2inc.scap.editor.gui.windows.XCCDFFilesFilter;
import com.g2inc.scap.editor.gui.windows.bundle.BundleEditorForm;
import com.g2inc.scap.editor.gui.windows.common.bundle.BundleValidationPanel;
import com.g2inc.scap.editor.gui.windows.common.datastream.DataStreamValidationPanel;
import com.g2inc.scap.editor.gui.windows.cpe.CPEDictionaryEditorForm;
import com.g2inc.scap.editor.gui.windows.oval.OvalEditorForm;
import com.g2inc.scap.editor.gui.windows.xccdf.XCCDFEditorForm;
import com.g2inc.scap.library.domain.CharacterSetEncodingException;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentClassEnum;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.UnsupportedDocumentTypeException;
import com.g2inc.scap.library.domain.bundle.SCAPDocumentBundle;
import com.g2inc.scap.library.domain.bundle.UseCase;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.datastream.DataStream;
import com.g2inc.scap.library.domain.datastream.DataStreamCollection;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.scap12.Scap12DataStreamCollection;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.jdom.JDOMException;

public class DataStreamBundleEditorForm extends DataStreamEditorForm implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static Logger LOG = Logger.getLogger(DataStreamBundleEditorForm.class);
        public static final String WINDOW_TITLE_BASE = EditorMessages.SCAP_DATA_STREAM + " Document - ";

    /** Creates new form NewJInternalFrame */
    public DataStreamBundleEditorForm(Scap12DataStreamCollection collection)
    {
        super(collection);
        initComponents();
        initComponents2();

        addInternalFrameListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();

        if(source == openButton)
        {
            Object selected = tree.getLastSelectedPathComponent();

            if(selected != null)
            {
                if(!(selected instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selected;

                Object userObj = selNode.getUserObject();

                if(userObj == null)
                {
                    return;
                }

                if(userObj instanceof SCAPDocument)
                {
                    EditorMainWindow emw = EditorMainWindow.getInstance();
                    JDesktopPane mwDesktop = emw.getDesktopPane();

                    SCAPDocument sdoc = (SCAPDocument) userObj;
                    SCAPDocumentTypeEnum sdocType = sdoc.getDocumentType();
                    EditorForm editorForm = null;
                    
                    LOG.info("actionPerformed on type: " + sdocType);

                    switch(sdocType)
                    {
                        case CPE_20:
                        case CPE_21:
                        case CPE_22:
                        case CPE_23:
                            editorForm = new CPEDictionaryEditorForm();
                            ((CPEDictionaryEditorForm) editorForm).setParentWin(emw);
                            ((CPEDictionaryEditorForm) editorForm).setDocument((CPEDictionaryDocument)sdoc);
                            editorForm.setTitle(EditorMessages.CPE_EDITOR_FORM_BASE_TITLE + sdoc.getFilename());
                            break;
                        case OVAL_53:
                        case OVAL_54:
                        case OVAL_55:
                        case OVAL_56:
                        case OVAL_57:
                        case OVAL_58:
                        case OVAL_59:
                        case OVAL_510:
                            editorForm = new OvalEditorForm();
                            ((OvalEditorForm) editorForm).setParentWin(emw);
                            ((OvalEditorForm) editorForm).setDocument((OvalDefinitionsDocument)sdoc);
                            editorForm.setTitle(EditorMessages.OVAL_EDITOR_FORM_BASE_TITLE + sdoc.getFilename());
                            break;
                        case XCCDF_114:
                        case XCCDF_12:
                            editorForm = new XCCDFEditorForm();
                            ((XCCDFEditorForm) editorForm).setParentWin(emw);
                            ((XCCDFEditorForm) editorForm).setDocument((XCCDFBenchmark)sdoc);
                            editorForm.setTitle(EditorMessages.XCCDF_EDITOR_FORM_BASE_TITLE + sdoc.getFilename());
                            break;
                        default:
                            JOptionPane.showMessageDialog(emw,
                            "The file being opened is not one that we know how to process: " + sdoc.getFilename(),
                            EditorMessages.LOAD_DOCUMENT_ERROR_TITLE,
                            JOptionPane.ERROR_MESSAGE);
                            return;
                    }

                    editorForm.addInternalFrameListener(this);

//                        setClosable(false);
                    openDocWindows.add(editorForm);

                    editorForm.setVisible(true);
                    mwDesktop.add(editorForm);

                    try
                    {
                        editorForm.setSelected(true);
                        editorForm.setMaximizable(true);
                        editorForm.setResizable(true);
                        int childMinX = getMinimumSize().width - 50;
                        int childMinY = getMinimumSize().height - 50;
                        editorForm.setMinimumSize(new Dimension(childMinX, childMinY));
                        editorForm.setMaximumSize(mwDesktop.getSize());
                        editorForm.setMaximum(true);
                        emw.incrementOpenDocuments();
                    }
                    catch(PropertyVetoException pve)
                    {
                        LOG.error("Error opening up document", pve);
                    }
                }
            }
        }
        else if(source == valButton)
        {
            // TODO THIS
            DisplayDialog dialog = new DisplayDialog(EditorMainWindow.getInstance(), true);
            DataStreamValidationPanel displayPage = new DataStreamValidationPanel();
            dialog.setTitle(BundleValidationPanel.WINDOW_TITLE);
            dialog.setPage(displayPage);
            displayPage.setCollection(collection);
            dialog.pack();
            dialog.setLocationRelativeTo(EditorMainWindow.getInstance());
            dialog.setVisible(true);
            
        }
        else if(source == addNewCPEDicButton
                || source == addNewOcilButton
                || source == addNewOvalButton
                || source == addNewXCCDFButton)
        {
            
            
            Object selected = tree.getLastSelectedPathComponent();

            if(selected != null)
            {
                if(!(selected instanceof DefaultMutableTreeNode))
                {
                    return;
                }

                DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selected;

                Object userObj = selNode.getUserObject();

                if(userObj == null)
                {
                    return;
                }

                if(userObj instanceof DataStream)
                {
                    DataStream stream = (DataStream) userObj;
                    
                    if(source == addNewCPEDicButton) {
                        addNewDocument(stream, SCAPDocumentClassEnum.CPE_DICTIONARY);
                    }
                    
                    if(source == addNewOcilButton) {
                        addNewDocument(stream, SCAPDocumentClassEnum.OCIL);
                    }
                    
                    if(source == addNewOvalButton) {
                        addNewDocument(stream, SCAPDocumentClassEnum.OVAL);
                    }
                    
                    if(source == addNewXCCDFButton) {
                        addNewDocument(stream, SCAPDocumentClassEnum.XCCDF);
                    }
                }
            }
            
        }
    }
    
    
    private void addNewDocument(DataStream stream, SCAPDocumentClassEnum docClass) {

        final JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.OPEN_DIALOG);

        File lastOpenedFrom = EditorConfiguration.getInstance().getLastOpenedFromFile();

        // Set current directory
        fc.setCurrentDirectory(lastOpenedFrom);

        FileFilter ff = null;
        
        switch(docClass) {
            case CPE_DICTIONARY:
                ff = new CPEDictionaryFilesFilter();
                break;
            case OCIL:
                ff = new OcilOrOvalFilesFilter("OCIL");
                break;
            case OVAL:
                ff = new OcilOrOvalFilesFilter("OVAL");
                break;
            case XCCDF:
                ff = new XCCDFFilesFilter();
                break;
        }
        
        if(ff != null) {
            fc.setFileFilter(ff);
        }
        
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int ret = fc.showOpenDialog((Component) EditorMainWindow.getInstance());

        if (ret == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();

            File parent = f.getAbsoluteFile().getParentFile();
            EditorConfiguration.getInstance().setLastOpenedFrom(parent.getAbsolutePath());
            EditorConfiguration.getInstance().save();

            SCAPDocument scapDoc;
            try {
                scapDoc = SCAPDocumentFactory.loadDocument(f);
                scapDoc.setDataStreamCollection(collection);

                this.collection.addOrUpdateComponent(stream, scapDoc);
            } catch (Exception ex) {
                LOG.error("Error opening SCAP Document", ex);
                return;
            }

            // if we got here, everything is OK so we'll reload the tree
            initTree();
            clearButtons();
        }
    }
    

    private void initButtons()
    {
       openButton.addActionListener(this);
       valButton.addActionListener(this);
       addNewCPEDicButton.addActionListener(this);
        addNewOcilButton.addActionListener(this);
        addNewOvalButton.addActionListener(this);
        addNewXCCDFButton.addActionListener(this);
    }

    private void initTree()
    {
        ToolTipManager.sharedInstance().registerComponent(tree);

        tree.setCellRenderer(new SCAPBundleTreeCellRenderer());
        tree.setModel(new SCAPStreamTreeModel(collection, null));

        // expand out all the nodes
        SCAPStreamTreeModel tm = (SCAPStreamTreeModel) tree.getModel();

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tm.getRoot();

        Enumeration nodeEnum = root.depthFirstEnumeration();

        while(nodeEnum.hasMoreElements())
        {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) nodeEnum.nextElement();

            TreePath tp = EditorUtil.getTreePathToChild(n);

            tree.expandPath(tp);
        }
        

        tree.addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                Object selected = tree.getLastSelectedPathComponent();

                if(selected != null)
                {
                    if(!(selected instanceof DefaultMutableTreeNode))
                    {
                        return;
                    }

                    DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selected;

                    Object userObj = selNode.getUserObject();
                    
                    if(userObj instanceof DataStream)
                    {
                        openButton.setEnabled(false);
                        addNewCPEDicButton.setEnabled(true);
                        addNewOcilButton.setEnabled(true);
                        addNewOvalButton.setEnabled(true);
                        addNewXCCDFButton.setEnabled(true);
                    } else if(userObj instanceof DataStreamCollection)
                    {
                        openButton.setEnabled(false);
                        addNewCPEDicButton.setEnabled(false);
                        addNewOcilButton.setEnabled(false);
                        addNewOvalButton.setEnabled(false);
                        addNewXCCDFButton.setEnabled(false);
                    }
                    else if(userObj instanceof SCAPDocument)
                    {
                        openButton.setEnabled(true);
                        addNewCPEDicButton.setEnabled(false);
                        addNewOcilButton.setEnabled(false);
                        addNewOvalButton.setEnabled(false);
                        addNewXCCDFButton.setEnabled(false);
                    }
                    else
                    {
                        clearButtons();
                    }
                }
            }
        });
    }

    private void initComponents2()
    {
        initTree();
        initButtons();
        clearButtons();
    }

    private void clearButtons() {
        openButton.setEnabled(false);
        addNewCPEDicButton.setEnabled(false);
        addNewOcilButton.setEnabled(false);
        addNewOvalButton.setEnabled(false);
        addNewXCCDFButton.setEnabled(false);
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

        treePanel = new javax.swing.JPanel();
        treeScrollPane = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        buttonPanel = new javax.swing.JPanel();
        openButton = new javax.swing.JButton();
        valButton = new javax.swing.JButton();
        addNewCPEDicButton = new javax.swing.JButton();
        addNewXCCDFButton = new javax.swing.JButton();
        addNewOvalButton = new javax.swing.JButton();
        addNewOcilButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("SCAP Data Stream -- ");
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(640, 480));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        treePanel.setLayout(new java.awt.GridBagLayout());

        treeScrollPane.setViewportView(tree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        treePanel.add(treeScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.75;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 0);
        getContentPane().add(treePanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        openButton.setText("Open file");
        openButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        buttonPanel.add(openButton, gridBagConstraints);

        valButton.setText("Validate SCAP Data Stream");
        valButton.setToolTipText("Validate this bundle according to 800-126");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        buttonPanel.add(valButton, gridBagConstraints);

        addNewCPEDicButton.setText("Add New CPE Dictionary");
        addNewCPEDicButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        buttonPanel.add(addNewCPEDicButton, gridBagConstraints);

        addNewXCCDFButton.setText("Add New XCCDF Document");
        addNewXCCDFButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        buttonPanel.add(addNewXCCDFButton, gridBagConstraints);

        addNewOvalButton.setText("Add New OVAL Document");
        addNewOvalButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        buttonPanel.add(addNewOvalButton, gridBagConstraints);

        addNewOcilButton.setText("Add New OCIL Document");
        addNewOcilButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        buttonPanel.add(addNewOcilButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        getContentPane().add(buttonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewCPEDicButton;
    private javax.swing.JButton addNewOcilButton;
    private javax.swing.JButton addNewOvalButton;
    private javax.swing.JButton addNewXCCDFButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton openButton;
    private javax.swing.JTree tree;
    private javax.swing.JPanel treePanel;
    private javax.swing.JScrollPane treeScrollPane;
    private javax.swing.JButton valButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public SCAPDocument getDocument()
    {
        return null;
    }

    @Override
    public TreePath getSelectedPath()
    {
        TreePath path = null;
        path = tree.getSelectionPath();
        return path;
    }

    @Override
    public void setSelectedElement(TreePath selectionPath)
    {
        if (selectionPath != null)
        {
            tree.setSelectionPath(selectionPath);
            tree.scrollPathToVisible(selectionPath);
        }
    }

    @Override
    public void refreshRootNode()
    {
        SCAPStreamTreeModel dtm = (SCAPStreamTreeModel) tree.getModel();

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) dtm.getRoot();
        TreePath path = EditorUtil.getTreePathToChild(rootNode);

        if (path != null)
        {
            tree.clearSelection();
            tree.setSelectionPath(path);
        }
    }
}
