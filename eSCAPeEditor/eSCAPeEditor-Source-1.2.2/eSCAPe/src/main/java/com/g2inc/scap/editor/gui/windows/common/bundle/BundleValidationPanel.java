package com.g2inc.scap.editor.gui.windows.common.bundle;
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

//import gov.nist.scap.validation.core.UseCase;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.bundle.UseCase;

import com.g2inc.scap.editor.gui.dialogs.editors.DisplayDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.IDisplayPage;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.library.domain.SCAPValidationResult;
import com.g2inc.scap.library.domain.bundle.BundleValidator;
import com.g2inc.scap.library.domain.bundle.SCAPDocumentBundle;
import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.net.URL;
import javax.swing.JOptionPane;

public class BundleValidationPanel extends javax.swing.JPanel implements IDisplayPage, ActionListener
{
    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(BundleValidationPanel.class);
    private SCAPValidationResult valResult;
    private SCAPDocumentBundle bundle;
    private UseCase useCase;

    public static final String WINDOW_TITLE = "Validate SCAP Data Stream";

    public BundleValidationPanel()
    {
        initComponents();
        initComponents2();
    }

    public void setBundle(SCAPDocumentBundle bundle)
    {
        this.bundle = bundle;
    }

    private void initComponents2()
    {
        setButtonDefault(UseCase.VULNERABILITY_XCCDF_OVAL);
        configurationButton.addActionListener(this);
        systemInventoryButton.addActionListener(this);
        vulnOvalButton.addActionListener(this);
        vulnXccdfOvalButton.addActionListener(this);
        valProgressBar.setMinimum(0);
        valProgressBar.setMaximum(100);

        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                startButton.setEnabled(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                ValidationWorker worker = new ValidationWorker();
                valProgressBar.setIndeterminate(true);
                worker.execute();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String useCaseString = e.getActionCommand();
        useCase = UseCase.valueOf(useCaseString);
    }

    private void setButtonDefault(UseCase useCase)
    {
        configurationButton.setSelected(useCase == UseCase.CONFIGURATION);
        systemInventoryButton.setSelected(useCase == UseCase.SYSTEM_INVENTORY);
        vulnOvalButton.setSelected(useCase == UseCase.VULNERABILITY_OVAL);
        vulnXccdfOvalButton.setSelected(useCase == UseCase.VULNERABILITY_XCCDF_OVAL);
    }

    @Override
    public void setData(Object obj)
    {        
        useCase = (UseCase) obj;
        setButtonDefault(useCase);
    }

    public SCAPValidationResult getValidationResult()
    {
        return valResult;
    }

    /**
     * Class to execute long-running validation task in another Thread (not
     * the event dispatching thread). Right now there is not idea how much
     * progress has been made while the validation is running, so the
     * progress bar will just have an animation running to let the user
     * know something is going on. Later when the validation provides an
     * indication of how close to complete it is, a PropertyChangeListener
     * can be added to update the progress bar in a more meaningful way.
     */
    class ValidationWorker extends SwingWorker<SCAPValidationResult, Void>
    {
        /*
         * Executed in worker thread
         */
        @Override
        protected SCAPValidationResult doInBackground() throws Exception
        {
            BundleValidator validator = new BundleValidator(bundle, useCase);
            valResult = validator.validate(useCase);
            return valResult;
        }

        /*
         * Executed in event dispatching thread, so we can update UI components.
         */
        @Override
        public void done() {
            startButton.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            valProgressBar.setValue(valProgressBar.getMinimum());
            valProgressBar.setIndeterminate(false);

            // show results of validation
            File htmlFile = valResult.getHtmlFile();
            if (htmlFile == null || !htmlFile.exists() || htmlFile.length() == 0) {
                String errorString = valResult.getErrorMessage();
                if (errorString == null || errorString.equals("")) {
                    errorString = "Error occurred during validation, no more information available";
                }
                JOptionPane.showMessageDialog(EditorMainWindow.getInstance(),
                        errorString,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                Desktop desktop = null;
                if (Desktop.isDesktopSupported() && (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))) {
                    desktop = Desktop.getDesktop();
                    URI htmlURI = null;
                    try {
                        htmlURI = htmlFile.toURI();
                        desktop.browse(htmlURI);
                    } catch (Exception e) {
                        log.error("Error opening browser window to display URI: " + htmlURI.toString(), e);
                        JOptionPane.showMessageDialog(EditorMainWindow.getInstance(),
                                "Error displaying validation results in browser - " + e.getMessage() + ", debugging info in log",
                                "Validation Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // This SHOULD NOT OCCUR, especially on Windows. Some Unix systems may not support Desktop.
                    // Display html using swing. Doesn't look as good as in a real browser
                    DisplayDialog dialog = new DisplayDialog(EditorMainWindow.getInstance(), true);
                    BundleValidationResultsPanel displayPage = new BundleValidationResultsPanel();
                    dialog.setTitle(BundleValidationResultsPanel.WINDOW_TITLE);
                    dialog.setPage(displayPage);
                    displayPage.setData(valResult);
                    dialog.pack();
                    dialog.setLocationRelativeTo(EditorMainWindow.getInstance());
                    dialog.setVisible(true);
                }
            }
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

        useCaseButtonGroup = new javax.swing.ButtonGroup();
        UsecaseRadioButtonPanel = new javax.swing.JPanel();
        configurationButton = new javax.swing.JRadioButton();
        vulnXccdfOvalButton = new javax.swing.JRadioButton();
        vulnOvalButton = new javax.swing.JRadioButton();
        systemInventoryButton = new javax.swing.JRadioButton();
        startPanel = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        valProgressBar = new javax.swing.JProgressBar();

        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(363, 427));
        setPreferredSize(new java.awt.Dimension(363, 427));
        setLayout(new java.awt.GridBagLayout());

        UsecaseRadioButtonPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Choose Use Case", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        UsecaseRadioButtonPanel.setLayout(new java.awt.GridBagLayout());

        useCaseButtonGroup.add(configurationButton);
        configurationButton.setText("CONFIGURATION");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        UsecaseRadioButtonPanel.add(configurationButton, gridBagConstraints);

        useCaseButtonGroup.add(vulnXccdfOvalButton);
        vulnXccdfOvalButton.setText("VULNERABILITY_XCCDF_OVAL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        UsecaseRadioButtonPanel.add(vulnXccdfOvalButton, gridBagConstraints);

        useCaseButtonGroup.add(vulnOvalButton);
        vulnOvalButton.setText("VULNERABILITY_OVAL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        UsecaseRadioButtonPanel.add(vulnOvalButton, gridBagConstraints);

        useCaseButtonGroup.add(systemInventoryButton);
        systemInventoryButton.setText("SYSTEM_INVENTORY");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        UsecaseRadioButtonPanel.add(systemInventoryButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 5, 6);
        add(UsecaseRadioButtonPanel, gridBagConstraints);

        startPanel.setLayout(new java.awt.GridBagLayout());

        startButton.setText("Start");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        startPanel.add(startButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 1);
        startPanel.add(valProgressBar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 6, 6);
        add(startPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel UsecaseRadioButtonPanel;
    private javax.swing.JRadioButton configurationButton;
    private javax.swing.JButton startButton;
    private javax.swing.JPanel startPanel;
    private javax.swing.JRadioButton systemInventoryButton;
    private javax.swing.ButtonGroup useCaseButtonGroup;
    private javax.swing.JProgressBar valProgressBar;
    private javax.swing.JRadioButton vulnOvalButton;
    private javax.swing.JRadioButton vulnXccdfOvalButton;
    // End of variables declaration//GEN-END:variables
}
