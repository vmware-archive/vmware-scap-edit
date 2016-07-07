package com.g2inc.scap.editor.gui.windows.wizardmode;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField;
import com.g2inc.scap.editor.gui.windows.EditorConfiguration;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.wizardmode.wizards.file.FileWizard;
import com.g2inc.scap.editor.gui.windows.wizardmode.wizards.registry.RegistryWizard;
import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;

public class WizardModeWindow extends javax.swing.JInternalFrame implements ChangeListener
{
    private static Logger LOG = Logger.getLogger(WizardModeWindow.class);
    private OvalDefinitionsDocument ovalDocument = null;
    private ListSelectionListener wizListListener = null;
    private static final String DEFAULT_NAMESPACE = "default.namespace";
    private final EditorConfiguration guiProps = EditorConfiguration.getInstance();
    
    @Override
    public void dispose()
    {
        super.dispose();
    }

    private void initWizList()
    {
        wizList.setCellRenderer(new WizardListCellRenderer());
        DefaultListModel model = new DefaultListModel();
        wizList.setModel(model);
        wizListListener = new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                int selectedIndex = wizList.getSelectedIndex();

                if(selectedIndex > -1)
                {
                    if(ovalVerCombo.getSelectedIndex() > -1)
                    {
                        if(namespaceField.isValidInput())
                        {
                            goButton.setEnabled(true);
                        }
                        else
                        {
                            namespaceCaption.setForeground(namespaceField.getErrorTextColor());
                            goButton.setEnabled(false);
                        }
                    }
                    else
                    {
                        ovalVerCaption.setForeground(namespaceField.getErrorTextColor());
                        goButton.setEnabled(false);
                    }
                }
                else
                {
                    goButton.setEnabled(false);
                }
            }
        };

        wizList.addListSelectionListener(wizListListener);
    }

    private void populateAvailableWizards()
    {
        LOG.debug("Entering populateAvailableWizards()");

        DefaultListModel model = (DefaultListModel) wizList.getModel();
        if(model == null)
        {
            LOG.debug("Model is null, returning");
            return;
        }

        model.clear();

        // TODO: Fix this to dynamically populate the list

        /** WARNING - KLUDGE in progress **/

        // add one file wizard per platform
        List<String> platforms = new ArrayList<String>();
        
        platforms.addAll(ovalDocument.getValidPlatforms());

        Collections.sort(platforms);
        Collections.reverse(platforms);

        for(int x = 0; x < platforms.size(); x++)
        {
            String platform = platforms.get(x);

            if(ovalDocument.platformSupportsTest(platform, "file_test"))
            {
                FileWizard fw = new FileWizard(ovalDocument, true, platform);
                WizardListEntry fileEntry = new WizardListEntry(fw);
                model.addElement(fileEntry);
            }
        }

        platforms.clear();

        RegistryWizard rw = new RegistryWizard(ovalDocument, true);
        WizardListEntry registryEntry = new WizardListEntry(rw);
        model.addElement(registryEntry);

        /** END KLUDGE **/

        LOG.debug("Leaving populateAvailableWizards()");
    }

    private void initButtons()
    {
        goButton.addActionListener(new ActionListener()
        {
        	@Override
            public void actionPerformed(ActionEvent e)
            {
                ovalDocument.setBaseId(namespaceField.getValue());

                guiProps.setPreviousNamespaceUsed(namespaceField.getValue());
                guiProps.save();
                
                WizardListEntry entry = (WizardListEntry) wizList.getSelectedValue();

                Wizard w = entry.getWizard();

                if(w instanceof FileWizard)
                {
                    FileWizard fw = (FileWizard) w;

                    fw.initPages();
                    fw.setMinimumSize(new Dimension(800,700));
                    fw.setSize(800, 700);

                    fw.setLocationRelativeTo(EditorMainWindow.getInstance());

                    fw.setVisible(true);
                    fw.dispose();
                }
		else if(w instanceof RegistryWizard)
                {
                    RegistryWizard rw = (RegistryWizard) w;

                    rw.addPages();
                    rw.setMinimumSize(new Dimension(800,600));
                    rw.setSize(800, 600);
                    rw.validate();

                    rw.setLocationRelativeTo(EditorMainWindow.getInstance());

                    rw.setVisible(true);
                    rw.dispose();
                }

                SCAPDocumentTypeEnum currentlySelectedDocType = (SCAPDocumentTypeEnum) ovalVerCombo.getSelectedItem();
                ovalVerCombo.setSelectedItem(currentlySelectedDocType);
            }
        });
    }

    private void initOvalVersionCombo()
    {
        LOG.debug("Entering initOvalVersionCombo()");
        ovalVerCombo.addItem(SCAPDocumentTypeEnum.OVAL_53);
        ovalVerCombo.addItem(SCAPDocumentTypeEnum.OVAL_54);
        ovalVerCombo.addItem(SCAPDocumentTypeEnum.OVAL_55);
        ovalVerCombo.addItem(SCAPDocumentTypeEnum.OVAL_56);
        ovalVerCombo.addItem(SCAPDocumentTypeEnum.OVAL_57);
        ovalVerCombo.addItem(SCAPDocumentTypeEnum.OVAL_58);
        ovalVerCombo.addItem(SCAPDocumentTypeEnum.OVAL_59);
        ovalVerCombo.addItem(SCAPDocumentTypeEnum.OVAL_510);

        ovalVerCombo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                LOG.debug("Entering ovalVerCombo.actionPerformed(ActionEvent ae)");
                SCAPDocumentTypeEnum type = (SCAPDocumentTypeEnum)ovalVerCombo.getSelectedItem();

                ovalDocument = null;

                SCAPDocument sdoc = SCAPDocumentFactory.createNewDocument(type);
                ovalDocument = (OvalDefinitionsDocument) sdoc;

                populateAvailableWizards();
                LOG.debug("Leaving ovalVerCombo.actionPerformed(ActionEvent ae)");
            }
        });
        
        LOG.debug("Leaving initOvalVersionCombo()");
    }

    private void initOvalNamespaceIDField()
    {
        namespaceField.addChangeListener(this);

        Pattern p = null;
        String patternString = "^.*$";
        try
        {
            p = Pattern.compile(patternString);
        }
        catch(Exception e)
        {
            LOG.error("Error compiling pattern " + patternString + ", using default", e);
        }

        if(p != null)
        {
            namespaceField.setPattern(p);
        }

        String previousValue = guiProps.getPreviousNamespaceUsed();

        if(previousValue != null)
        {
            namespaceField.setValue(previousValue);
        }
        else
        {
            namespaceField.setValue(DEFAULT_NAMESPACE);
        }
    }

    public JButton getGoButton()
    {
        return goButton;
    }

    public PatternedStringField getNamespaceField()
    {
        return namespaceField;
    }

    public JList getWizList()
    {
        return wizList;
    }

    private void initComponents2()
    {
        initWizList();
        initOvalNamespaceIDField();
        initOvalVersionCombo();
        initButtons();

        // set the default
        try
        {
            ovalVerCombo.setSelectedItem(SCAPDocumentTypeEnum.OVAL_53);
        }
        catch(Exception ex)
        {
            LOG.error("Exception", ex);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        try
        {
            Object src = e.getSource();

            if(src == namespaceField)
            {
                if(namespaceField.isValidInput())
                {
                    namespaceCaption.setForeground(namespaceField.getNormalTextColor());
                    if(wizList.getSelectedIndex() > -1)
                    {
                        if(ovalVerCombo.getSelectedIndex() > -1)
                        {
                            // everything seems valid, enable the go button
                            ovalVerCaption.setForeground(namespaceField.getNormalTextColor());
                            namespaceCaption.setForeground(namespaceField.getNormalTextColor());
                            goButton.setEnabled(true);

                            ovalDocument.setBaseId(namespaceField.getValue());
                        }
                        else
                        {
                            ovalVerCaption.setForeground(namespaceField.getErrorTextColor());
                            goButton.setEnabled(false);
                        }
                    }
                    else
                    {
                        goButton.setEnabled(false);
                    }
                }
                else
                {
                    namespaceCaption.setForeground(namespaceField.getErrorTextColor());
                    goButton.setEnabled(false);
                }
            }
        }
        catch(Exception ex)
        {
            LOG.error("Exception", ex);
        }
    }
    public OvalDefinitionsDocument getOvalDocument()
    {
        return ovalDocument;
    }

    public void setOvalDocument(OvalDefinitionsDocument ovalDocument)
    {
        this.ovalDocument = ovalDocument;
    }

    /** Creates new form WizardModeWindow */
    public WizardModeWindow()
    {
        initComponents();
        initComponents2();
    }

    public Properties getGuiProperties() 
    {
		return guiProps;
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

        mainCaption = new javax.swing.JLabel();
        wizScrollPane = new javax.swing.JScrollPane();
        wizList = new javax.swing.JList();
        goButton = new javax.swing.JButton();
        ovalVerCaption = new javax.swing.JLabel();
        ovalVerCombo = new javax.swing.JComboBox();
        namespaceCaption = new javax.swing.JLabel();
        namespaceField = new com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField();

        setClosable(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        mainCaption.setText("What would you like to create?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(6, 9, 5, 0);
        getContentPane().add(mainCaption, gridBagConstraints);

        wizList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        wizList.setName("avail_wizard_list"); // NOI18N
        wizScrollPane.setViewportView(wizList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 9, 0);
        getContentPane().add(wizScrollPane, gridBagConstraints);

        goButton.setText("Go!");
        goButton.setEnabled(false);
        goButton.setName("go_button"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 5);
        getContentPane().add(goButton, gridBagConstraints);

        ovalVerCaption.setText("Target OVAL version");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        getContentPane().add(ovalVerCaption, gridBagConstraints);

        ovalVerCombo.setName("oval_version_combo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        getContentPane().add(ovalVerCombo, gridBagConstraints);

        namespaceCaption.setText("OVAL Namespace Identifier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 0, 0);
        getContentPane().add(namespaceCaption, gridBagConstraints);

        namespaceField.setName("oval_namespace_id"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 10, 0);
        getContentPane().add(namespaceField, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton goButton;
    private javax.swing.JLabel mainCaption;
    private javax.swing.JLabel namespaceCaption;
    private com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField namespaceField;
    private javax.swing.JLabel ovalVerCaption;
    private javax.swing.JComboBox ovalVerCombo;
    private javax.swing.JList wizList;
    private javax.swing.JScrollPane wizScrollPane;
    // End of variables declaration//GEN-END:variables
}
