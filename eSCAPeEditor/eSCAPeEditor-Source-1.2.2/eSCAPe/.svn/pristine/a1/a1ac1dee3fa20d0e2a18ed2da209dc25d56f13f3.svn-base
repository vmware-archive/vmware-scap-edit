package com.g2inc.scap.editor.gui.windows.xccdf.gen;

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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.GenericProgressDialog;
import com.g2inc.scap.editor.gui.windows.common.WeakRunnable;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.XCCDFBuilder;
import com.g2inc.scap.library.domain.oval.XCCDFBuilderParameters;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.util.CommonUtil;
import com.g2inc.scap.model.ocil.OcilDocument;

/**
 * This class allows the user to select multiple oval documents
 * and create an xccdf document per oval document.  This basically does in batch what
 * the editor's "new xccdf from oval" menu item does.
 */
public class XCCDFGen extends javax.swing.JDialog implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private static Logger LOG = Logger.getLogger(XCCDFGen.class);

    public static final String DEFAULT_GROUP_TITLE = "Group Title";
    public static final String DEFAULT_PROFILE_TITLE = "Profile Title";
    
    private SCAPDocument scapDoc;
    private XCCDFBenchmark benchmark = null;

    private boolean cancelled = true;    
    private Color normalColor = null;
    private Color errorColor = Color.RED;

    private boolean profOk = true;
    private boolean groupOk = true;

    /** Creates new form OvalMergerGui */
    public XCCDFGen(java.awt.Frame parent, boolean modal, SCAPDocument scapDoc) {
        super(parent, modal);
   
        this.scapDoc = scapDoc;

        initComponents();
        initComponents2();
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
    	Object src = ae.getSource();
        if(src == generateButton)
    	{
            cancelled = false;
            
            File scapFile = new File(scapDoc.getFilename());

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            generateButton.setEnabled(false);

            String suggestedXCCDFFilename = CommonUtil.getSuggestedXCCDFFilenameForOvalFilename(scapDoc.getFilename());
            File suggestedXCCDFFile = new File(suggestedXCCDFFilename).getAbsoluteFile();
            LOG.debug("actionPerformed called getSuggestedXCCDFFilenameForOvalFilename, got: " + suggestedXCCDFFilename);
            File chosenFile = null;

            while(chosenFile == null)
            {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(suggestedXCCDFFile.getParentFile());
                fc.setDialogType(JFileChooser.SAVE_DIALOG);
                fc.setSelectedFile(suggestedXCCDFFile);

                int rc = fc.showSaveDialog(this);
                if(rc == JFileChooser.APPROVE_OPTION)
                {
                    File tmpChosenFile = fc.getSelectedFile();

                    if(tmpChosenFile.exists())
                    {
                        String message = tmpChosenFile.getAbsolutePath() + " exists! Overwrite?";
                        message = EditorUtil.insertHtmlBreaks(message, 75);

                        int overwriteRc = JOptionPane.showConfirmDialog(
                            this,
                            "<HTML>" + message + "</HTML>",
                            "Overwrite existing file?",
                            JOptionPane.YES_NO_OPTION);

                        if(overwriteRc == JOptionPane.YES_OPTION)
                        {
                            chosenFile = tmpChosenFile;
                        }
                        else
                        {
                            continue;
                        }
                    }
                    else
                    {
                        chosenFile = tmpChosenFile;
                    }
                }
                else
                {
                    String message = "XCCDF to OVAL cancelled!";
                    EditorUtil.showMessageDialog(this,
                        message,
                        "Cancelled",
                        JOptionPane.INFORMATION_MESSAGE);
                    setCursor(null);
                    generateButton.setEnabled(true);
                    return;
                }
            }

            String chosenFilename = chosenFile.getAbsolutePath();

            if(!chosenFilename.toLowerCase().endsWith(".xml"))
            {
                chosenFilename += ".xml";
            }

            
            XCCDFBuilder builder = new XCCDFBuilder();
            XCCDFBuilderParameters buildParms = new XCCDFBuilderParameters();

            String profId =  CommonUtil.toNCName(profileTitleTextField.getText());
            String groupId = CommonUtil.toNCName(groupTitleTextField.getText());

            buildParms.setBenchmarkDescription("File content for " + getFileType() + " file " + scapFile.getName() + " built on " + scapDoc.getGeneratorRawDate());
            buildParms.setBenchmarkId(CommonUtil.toNCName(scapFile.getName()) + "_benchmark");
            buildParms.setGroupId(groupId);
            buildParms.setProfileId(profId);
            buildParms.setXccdfFileName(chosenFilename);
            buildParms.setGroupTitle(groupTitleTextField.getText());
            buildParms.setProfileTitle(profileTitleTextField.getText());
            buildParms.setSourceDoc(scapDoc);

            final XCCDFBuilder builderRef = builder;
            final XCCDFBuilderParameters builderParmsRef = buildParms;
            final XCCDFGen thisRef = this;
            
            Runnable r = new Runnable()
            {				
				@Override
				public void run()
				{
		            try
		            {
		                builderRef.setParms(builderParmsRef);
		                benchmark =  builderRef.generateXCCDF(builderParmsRef);
		            }
		            catch (Exception e)
		            {
		                LOG.error("IO Error Creating XCCDF from " + getFileType() + ": " + e.getMessage(), e);
		                String message = "Error creating XCCDF benchmark from " + getFileType() + " file " + builderParmsRef.getSourceDoc().getFilename() + e.getMessage();

		                EditorUtil.showMessageDialog(thisRef,
		                        message,
		                        "Error",
		                        JOptionPane.ERROR_MESSAGE);
		            }					
				}
			};

            GenericProgressDialog openCPEProgress = new GenericProgressDialog(EditorMainWindow.getInstance(), true);
            openCPEProgress.setRunnable(new WeakRunnable(r));
            openCPEProgress.setMessage("Creating " + EditorMessages.XCCDF + " benchmark from " + getFileType() + " document");
            openCPEProgress.pack();
            openCPEProgress.setLocationRelativeTo(this);
            openCPEProgress.setVisible(true);

            openCPEProgress.dispose();

            setCursor(Cursor.getDefaultCursor());

            generateButton.setEnabled(true);

            setVisible(false);
    	}
    }
    
    private void addButtonListeners()
    {
        generateButton.addActionListener(this);
    }

    private void addTextFieldDocumentListeners()
    {
        final String regex = "^[A-Za-z0-9\\-\\_ ]+$";
        final String regexFriendlyText = "Text may include A-Z, a-z, 0-9, -, _, and spaces only";

        if(normalColor == null)
        {
            normalColor = profileTitleTextField.getForeground();
        }

        profileTitleTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                String suppliedProfName = profileTitleTextField.getText();
                int docLen = suppliedProfName.length();

                if(docLen < 1)
                {
                    generateButton.setEnabled(false);
                    profileTitleStatusLabel.setText("A Profile title must be supplied!");
                    profOk = false;
                    generateButton.setEnabled(false);
                    return;
                }

                if(!suppliedProfName.matches(regex))
                {
                    profileTitleStatusLabel.setText(regexFriendlyText);
                    profileTitleTextField.setForeground(errorColor);
                    profOk = false;
                    generateButton.setEnabled(false);
                    return;
                }

                String suppliedGroupName = groupTitleTextField.getText();
                if(suppliedProfName.equals(suppliedGroupName))
                {
                    profileTitleStatusLabel.setText("Profile title must not be the same as the Group title.");
                    profileTitleTextField.setForeground(errorColor);
                    profOk = false;
                    generateButton.setEnabled(false);
                    return;
                }

                profileTitleStatusLabel.setText("OK");
                profileTitleTextField.setForeground(normalColor);
                profOk = true;

                if(!groupOk)
                {
                    generateButton.setEnabled(false);
                }
                else
                {
                    generateButton.setEnabled(true);
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                common(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                common(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                common(e);
            }
        });

        groupTitleTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent de)
            {
                Document d = de.getDocument();
                int docLen = d.getLength();

                if(docLen < 1)
                {
                    generateButton.setEnabled(false);
                    groupTitleStatusLabel.setText("A Group title must be supplied!");
                    groupOk = false;
                    generateButton.setEnabled(false);
                    return;
                }

                String suppliedGroupName = groupTitleTextField.getText();

                if(!suppliedGroupName.matches(regex))
                {
                    groupTitleStatusLabel.setText(regexFriendlyText);
                    groupTitleTextField.setForeground(errorColor);
                    groupOk = false;
                    generateButton.setEnabled(false);
                    return;
                }

                String suppliedProfName = profileTitleTextField.getText();
                if(suppliedProfName.equals(suppliedGroupName))
                {
                    groupTitleStatusLabel.setText("Group title must not be the same as the Profile title.");
                    groupTitleTextField.setForeground(errorColor);
                    groupOk = false;
                    generateButton.setEnabled(false);
                    return;
                }

                groupTitleStatusLabel.setText("OK");
                groupTitleTextField.setForeground(normalColor);
                groupOk = true;

                if(!profOk)
                {
                    generateButton.setEnabled(false);
                }
                else
                {
                    generateButton.setEnabled(true);
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                common(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                common(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                common(e);
            }
        });
    }

    private void initComponents2()
    {
        profileTitleTextField.setText(DEFAULT_PROFILE_TITLE);
        groupTitleTextField.setText(DEFAULT_GROUP_TITLE);
        
        if (scapDoc instanceof OvalDefinitionsDocument) {
            setTitle("XCCDF from OVAL");
        } else if (scapDoc instanceof OcilDocument) {
            setTitle("XCCDF from OCIL");
        }

        addButtonListeners();
        addTextFieldDocumentListeners();
    }

    public XCCDFBenchmark getBenchmark()
    {
        return benchmark;
    }

    public boolean wasCancelled() {
        return cancelled;
    }
    
    private String getFileType() {
    	String fileType = "";
    	if (scapDoc instanceof OvalDefinitionsDocument) {
    		fileType = EditorMessages.OVAL;
    	} else if (scapDoc instanceof OcilDocument) {
    		fileType = EditorMessages.OCIL;
    	}
    	return fileType;
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

        generateButton = new javax.swing.JButton();
        profileTitleCaption = new javax.swing.JLabel();
        profileTitleStatusLabel = new javax.swing.JLabel();
        profileTitleTextField = new javax.swing.JTextField();
        groupTitleCaption = new javax.swing.JLabel();
        groupTitleStatusLabel = new javax.swing.JLabel();
        groupTitleTextField = new javax.swing.JTextField();

        setTitle("XCCDF from OVAL");
        setMinimumSize(new java.awt.Dimension(400, 151));
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        generateButton.setText("Generate");
        generateButton.setToolTipText("Generate an XCCDF benchmark for each of the files above.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 6, 9);
        getContentPane().add(generateButton, gridBagConstraints);

        profileTitleCaption.setText("Profile title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 9, 0, 8);
        getContentPane().add(profileTitleCaption, gridBagConstraints);

        profileTitleStatusLabel.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        getContentPane().add(profileTitleStatusLabel, gridBagConstraints);

        profileTitleTextField.setText("Default");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 4, 8);
        getContentPane().add(profileTitleTextField, gridBagConstraints);

        groupTitleCaption.setText("Group title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 8);
        getContentPane().add(groupTitleCaption, gridBagConstraints);

        groupTitleStatusLabel.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        getContentPane().add(groupTitleStatusLabel, gridBagConstraints);

        groupTitleTextField.setText("Default");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 5, 8);
        getContentPane().add(groupTitleTextField, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton generateButton;
    private javax.swing.JLabel groupTitleCaption;
    private javax.swing.JLabel groupTitleStatusLabel;
    private javax.swing.JTextField groupTitleTextField;
    private javax.swing.JLabel profileTitleCaption;
    private javax.swing.JLabel profileTitleStatusLabel;
    private javax.swing.JTextField profileTitleTextField;
    // End of variables declaration//GEN-END:variables

}
