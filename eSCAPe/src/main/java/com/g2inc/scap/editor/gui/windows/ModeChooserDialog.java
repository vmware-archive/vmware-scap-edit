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

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.editor.gui.windows.common.WeakInternalFrameListener;
import com.g2inc.scap.editor.gui.windows.wizardmode.WizardModeWindow;

public class ModeChooserDialog extends javax.swing.JDialog {

    private JFrame chosenWindow = null;

    /**
     * Creates new form ModeChooserDialog
     */
    public ModeChooserDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initComponents2();
        
        setTitle("Welcome to " + EditorMessages.PRODUCT_NAME);
    }
    
    public JFrame getChosenWindow() {
        return chosenWindow;
    }
    
    private void initButtons() {
        final ModeChooserDialog thisRef = this;
        
        standardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                thisRef.setVisible(false);
                thisRef.dispose();
            }
        });
        /*
        wizardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                wizardButton.setEnabled(false);
                            }
                        });
                        
                        WizardModeWindow wizModeWin = new WizardModeWindow();
                        
                        EditorMainWindow emw = EditorMainWindow.getInstance();
                        JDesktopPane emwDesktopPane = emw.getDesktopPane();
                        
                        wizModeWin.addInternalFrameListener(new WeakInternalFrameListener(emw));
                        
                        wizModeWin.setTitle("Wizard Mode");
                        wizModeWin.pack();
                        Dimension dpDim = emwDesktopPane.getSize();
                        int x = (dpDim.width - wizModeWin.getWidth()) / 2;
                        int y = (dpDim.height - wizModeWin.getHeight()) / 2;
                        
                        wizModeWin.setLocation(x, y);
                        emwDesktopPane.add(wizModeWin);
                        wizModeWin.setVisible(true);
                        emw.setWizMode(true);
                        
                        thisRef.setVisible(false);
                        thisRef.dispose();
                    }
                });
            }
        });*/
    }
    
    private void initComponents2() {
        initButtons();
        this.setName("mode_chooser");
    }
    
    public JButton getStandardButton() {
        return standardButton;
    }
    
    public JButton getWizardButton() {
        return wizardButton;
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

        captionLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
      //  wizardCaption = new javax.swing.JLabel();
      //  wizardButton = new javax.swing.JButton();
        standardCaption = new javax.swing.JLabel();
        standardButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setForeground(java.awt.SystemColor.windowText);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        captionLabel.setText("Please choose which mode you would like to start in:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 9, 0, 0);
        getContentPane().add(captionLabel, gridBagConstraints);

        buttonPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Modes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.SystemColor.windowText));
        buttonPanel.setToolTipText("");
        buttonPanel.setLayout(new java.awt.GridBagLayout());

     /*   wizardCaption.setText("<HTML>Starts the editor in wizard mode.  This mode asks you<BR>\nsome questions and produces content based on your answers.<BR>\n No need to know OVAL or XCCDF technology.</HTML>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        buttonPanel.add(wizardCaption, gridBagConstraints);*/

       /* wizardButton.setForeground(getForeground());
        wizardButton.setText("Wizard-Driven");
        wizardButton.setName("wizard_button"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        buttonPanel.add(wizardButton, gridBagConstraints);*/

        standardCaption.setText("<HTML>Starts the editor in standard mode.  This mode allows<BR>\nyou to create OVAL and XCCDF content directly.<BR>\nRequires you to know OVAL or XCCDF technology.</HTML>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        buttonPanel.add(standardCaption, gridBagConstraints);

        standardButton.setForeground(getForeground());
        standardButton.setText("Standard");
        standardButton.setName("standard_button"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 0, 0);
        buttonPanel.add(standardButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        getContentPane().add(buttonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel captionLabel;
    private javax.swing.JButton standardButton;
    private javax.swing.JLabel standardCaption;
    private javax.swing.JButton wizardButton;
    private javax.swing.JLabel wizardCaption;
    // End of variables declaration//GEN-END:variables
}
