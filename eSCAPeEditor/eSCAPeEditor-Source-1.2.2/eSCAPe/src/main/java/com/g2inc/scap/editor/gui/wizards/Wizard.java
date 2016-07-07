package com.g2inc.scap.editor.gui.wizards;
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.windows.EditorMainWindow;

public abstract class Wizard extends javax.swing.JDialog implements ActionListener, ChangeListener
{
    private static Logger log = Logger.getLogger(Wizard.class);

    protected List<WizardPage> pages = null;
    private boolean cancelled = true;
    protected int currentPageIndex = 0;
    protected String wizardDescription = null;
    protected String wizardShortDescription = null;
    protected String wizardName = null;
 
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();

        if(src == cancelButton)
        {
            setVisible(false);
        }
        else if(src == nextButton)
        {
            if(nextButton.getText().equals("Next"))
            {
                if(!wizardPageTabs.isEnabledAt(getCurrentPageIndex() + 1))
                {
                    wizardPageTabs.setEnabledAt(getCurrentPageIndex() + 1, true);
                }

                wizardPageTabs.setSelectedComponent(pages.get(getCurrentPageIndex() + 1));
            }
            else if(nextButton.getText().equals("Finish"))
            {
                nextButton.setEnabled(false); // prevent from clicking twice
                performFinish();
                cancelled = false;
                this.setVisible(false);
            }
        }
        else if(src == backButton)
        {
            wizardPageTabs.setSelectedComponent(pages.get(getCurrentPageIndex() - 1));

            if(getCurrentPageIndex() == 0)
            {
                disableBackButton();
            }

            nextButton.setEnabled(true);
        }

        validate();
    }

    private void initButtonActions()
    {
        nextButton.addActionListener(this);
        backButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    private void initTabComponent()
    {
        wizardPageTabs.addChangeListener(this);
    }

    private void initComponents2()
    {
        initTabComponent();
        initButtonActions();
    }

    public Wizard(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        initComponents2();
        setLocationRelativeTo(parent);
    }

    public void enableNextButton()
    {
        nextButton.setEnabled(true);
    }

    public void disableNextButton()
    {
        nextButton.setEnabled(false);
    }

    public void enableBackButton()
    {
        backButton.setEnabled(true);
    }

    public void disableBackButton()
    {
        backButton.setEnabled(false);
    }

    public boolean wasCancelled()
    {
        return cancelled;
    }

    public int getCurrentPageIndex()
    {
        if(pages == null)
        {
            throw new RuntimeException("pages was null!");
        }

        return currentPageIndex;
    }

    private int getPageCount()
    {
        if(pages == null)
        {
            return -1;
        }

        return pages.size();
    }

    private void setCurrentPageIndex(int cur)
    {
        currentPageIndex = cur;
        updateCurrentPageLabel();

        if(cur < 2)
        {
            disableBackButton();
        }
        else if(cur  == (pages.size() - 1 ))
        {
            disableNextButton();

        }
        else
        {
            enableBackButton();
            enableNextButton();
        }
    }

    private void updateCurrentPageLabel()
    {
        currentPageLabel.setText((getCurrentPageIndex() + 1) + "");
    }

    protected void updatePageCountLabel()
    {
        totalPagesLabel.setText(getPageCount() + "");
    }

    public void addWizardPage(WizardPage page)
    {
        if(pages == null)
        {
            pages = new ArrayList<WizardPage>();
        }

        page.setWizard(this);
        
        pages.add(page);

        updatePageCountLabel();

        wizardPageTabs.addTab(page.getPageTitle(), page);

        if(pages.size() == 1)
        {
            updateCurrentPageLabel();
            wizardPageTabs.setSelectedComponent(page);
        
            nextButton.setText("Finish");
        }
        else
        {
            wizardPageTabs.setEnabledAt(pages.size() - 1, false);
            nextButton.setText("Next");
        }
    }

    // implemented by each wizard that extends this one.   This method should
    // do anything necessary after the user clicks finish.
    public abstract void performFinish();

    public JButton getBackButton()
    {
        return backButton;
    }

    public JButton getNextButton()
    {
        return nextButton;
    }
    
    public void stateChanged(ChangeEvent ce)
    {
        int index = wizardPageTabs.getSelectedIndex();
        log.trace("stateChanged entered - current tab is " + index + ", pages.size = " + pages.size());

        setCurrentPageIndex(index);

        if(index == (pages.size() - 1))
        {
            log.trace("stateChanged - setting next to Finish");
            nextButton.setText("Finish");
        }
        else
        {
            log.trace("stateChanged - setting next to Next");
            nextButton.setText("Next");
        }

        if(index == 0)
        {
            log.trace("stateChanged - disabling Back button");
            disableBackButton();
        }
        //else if(index < (pages.size() - 1))
        else if(index <= (pages.size() - 1))
        {
            log.trace("stateChanged - enaabling Back button");
            enableBackButton();
        }

        if(pages.get(index).isSatisfied())
        {
            log.trace("stateChanged - enabling Next button");
            enableNextButton();
        }
        else
        {
            log.trace("stateChanged - disabling Next button");
            disableNextButton();
        }
    }

    public String getWizardDescription()
    {
        return wizardDescription;
    }

    public void setWizardDescription(String description)
    {
        this.wizardDescription = description;
    }

    public String getWizardName()
    {
        return wizardName;
    }

    public void setWizardName(String wizardName)
    {
        this.wizardName = wizardName;
    }

    public String getWizardShortDescription()
    {
        return wizardShortDescription;
    }

    public void setWizardShortDescription(String wizardShortDescription)
    {
        this.wizardShortDescription = wizardShortDescription;
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

        bodyPanel = new javax.swing.JPanel();
        wizardPageTabs = new javax.swing.JTabbedPane();
        footerPanel = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        backButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        currentPageLabel = new javax.swing.JLabel();
        totalPagesLabel = new javax.swing.JLabel();
        ofLabel = new javax.swing.JLabel();
        pageLabel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(400, 300));
        setModal(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        bodyPanel.setAutoscrolls(true);
        bodyPanel.setLayout(new java.awt.GridBagLayout());

        wizardPageTabs.setAutoscrolls(true);
        wizardPageTabs.setMaximumSize(null);
        wizardPageTabs.setMinimumSize(null);
        wizardPageTabs.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        bodyPanel.add(wizardPageTabs, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        getContentPane().add(bodyPanel, gridBagConstraints);

        footerPanel.setAutoscrolls(true);
        footerPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        footerPanel.add(jSeparator1, gridBagConstraints);

        backButton.setText("Back");
        backButton.setEnabled(false);
        backButton.setName("back_button"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 3, 4);
        footerPanel.add(backButton, gridBagConstraints);

        nextButton.setText("Next");
        nextButton.setEnabled(false);
        nextButton.setName("next_button"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 3, 4);
        footerPanel.add(nextButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.setName("cancel_button"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 3, 6);
        footerPanel.add(cancelButton, gridBagConstraints);

        currentPageLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 1, 3);
        footerPanel.add(currentPageLabel, gridBagConstraints);

        totalPagesLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 3, 0);
        footerPanel.add(totalPagesLabel, gridBagConstraints);

        ofLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ofLabel.setText("of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 1, 5);
        footerPanel.add(ofLabel, gridBagConstraints);

        pageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pageLabel.setText("Page");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 1, 5);
        footerPanel.add(pageLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 9, 0);
        getContentPane().add(footerPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel currentPageLabel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel ofLabel;
    private javax.swing.JLabel pageLabel;
    private javax.swing.JLabel totalPagesLabel;
    protected javax.swing.JTabbedPane wizardPageTabs;
    // End of variables declaration//GEN-END:variables
}
