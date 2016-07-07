package com.g2inc.scap.editor.gui.windows.bundle;

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
import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorForm;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.bundle.BundleValidationPanel;
import com.g2inc.scap.editor.gui.windows.cpe.CPEDictionaryEditorForm;
import com.g2inc.scap.editor.gui.windows.oval.OvalEditorForm;
import com.g2inc.scap.editor.gui.windows.xccdf.XCCDFEditorForm;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.bundle.SCAPDocumentBundle;
import com.g2inc.scap.library.domain.bundle.UseCase;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

public class ZipBundleEditorForm extends BundleEditorForm implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static Logger LOG = Logger.getLogger(ZipBundleEditorForm.class);

    /** Creates new form NewJInternalFrame */
    public ZipBundleEditorForm(SCAPDocumentBundle bundle)
    {
        super(bundle);
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

                    switch(sdocType)
                    {
                        case CPE_20:
                        case CPE_21:
                        case CPE_22:
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
                    }
                }
            }
        }
        else if(source == valButton)
        {
            DisplayDialog dialog = new DisplayDialog(EditorMainWindow.getInstance(), true);
            BundleValidationPanel displayPage = new BundleValidationPanel();
            dialog.setTitle(BundleValidationPanel.WINDOW_TITLE);
            dialog.setPage(displayPage);
            displayPage.setBundle(bundle);
            displayPage.setData(UseCase.VULNERABILITY_XCCDF_OVAL);
            dialog.pack();
            dialog.setLocationRelativeTo(EditorMainWindow.getInstance());
            dialog.setVisible(true);
        }
    }

    private void initButtons()
    {
       openButton.addActionListener(this);
       valButton.addActionListener(this);
    }

    private void initTree()
    {
        ToolTipManager.sharedInstance().registerComponent(tree);

        tree.setCellRenderer(new SCAPBundleTreeCellRenderer());
        tree.setModel(new SCAPBundleTreeModel(bundle, null));

        // expand out all the nodes
        SCAPBundleTreeModel tm = (SCAPBundleTreeModel) tree.getModel();

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

                    if(userObj instanceof SCAPDocument)
                    {
                        openButton.setEnabled(true);
                    }
                    else if(userObj instanceof SCAPDocumentBundle)
                    {
                        openButton.setEnabled(false);
                    }
                    else
                    {
                        openButton.setEnabled(false);
                    }
                }
            }
        });
    }

    private void initComponents2()
    {
        initTree();
        initButtons();
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        getContentPane().add(buttonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TreePath getSelectedPath()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSelectedElement(TreePath selectionPath)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void refreshRootNode()
    {
    }
}
