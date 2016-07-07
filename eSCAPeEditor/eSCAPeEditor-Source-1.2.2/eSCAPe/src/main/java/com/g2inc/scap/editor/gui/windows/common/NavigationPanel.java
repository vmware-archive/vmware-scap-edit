package com.g2inc.scap.editor.gui.windows.common;
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
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.tree.TreePath;

import com.g2inc.scap.editor.gui.windows.EditorForm;

public class NavigationPanel extends javax.swing.JPanel
{
    private ArrayList<NavigationButton> buttonList = new ArrayList<NavigationButton>();
    private int currentIndex = -1;
    
    private void setCurrentIndex(int i)
    {
        currentIndex = i;

        if(currentIndex == -1)
        {
            navForwardButton.setEnabled(false);
            navBackButton.setEnabled(false);
        }
        else
        {
            if(currentIndex == 0)
            {
                navBackButton.setEnabled(false);
            }
            else
            {
                navBackButton.setEnabled(true);
            }

            if(currentIndex == (buttonList.size() - 1))
            {
                navForwardButton.setEnabled(false);
            }
            else
            {
                navForwardButton.setEnabled(true);
            }
        }
    }

    public void addNavigationButton(NavigationButton button)
    {
        if(buttonList == null)
        {
            buttonList = new ArrayList<NavigationButton>();
        }

        if(buttonList.size() > 0)
        {
            ((NavigationButton) buttonList.get(buttonList.size() -1)).setSelected(false);
        }
        
        button.setSize(button.getWidth(), dynamicButtonContainer.getHeight() - 10);
        button.setSelected(true);
        
        buttonList.add(button);
        setCurrentIndex(buttonList.size() - 1);

        dynamicButtonContainer.add(button);
        validate();
        
        button.addActionListener(new ActionListener()
        {
        	@Override
            public void actionPerformed(ActionEvent ae)
            {
                Object src = ae.getSource();

                if(src != null)
                {
                    if(src instanceof NavigationButton)
                    {
                        NavigationButton nb = (NavigationButton) src;

                        int index = buttonList.indexOf(nb);

                        while(index != buttonList.size())
                        {
                            int lastIndex = buttonList.size() - 1;
                            NavigationButton lastButton = buttonList.get(lastIndex);
                            
                            buttonList.remove(lastIndex);
                            dynamicButtonContainer.remove(lastButton);
                        }
                        validate();

                        if(buttonList.size() == 0)
                        {
                            setCurrentIndex(-1);
                        }
                        else
                        {
                            setCurrentIndex(buttonList.size() - 1);
                        }

                        EditorForm form = nb.getEditorForm();
                        TreePath path = nb.getSelectedElement();

                        form.setSelectedElement(path);
                    }
                }
            }
        });
    }

    private void initNavButtons()
    {
        navBackButton.addActionListener(new ActionListener()
        {
        	@Override
            public void actionPerformed(ActionEvent ae)
            {
                Object src = ae.getSource();
                if(src != null)
                {
                    if(src == navBackButton)
                    {
                       NavigationButton curNb = buttonList.get(currentIndex);
                       curNb.setSelected(false);

                       validate();
                       NavigationButton nextNb = buttonList.get(currentIndex - 1);
                       nextNb.setSelected(true);
                       setCurrentIndex(currentIndex - 1);
                       
                       EditorForm editorForm = nextNb.getEditorForm();
                       TreePath path = nextNb.getSelectedElement();
                       
                       editorForm.setSelectedElement(path);

                       try
                       {
                            editorForm.setSelected(true);
                       }
                       catch(PropertyVetoException pve)
                       {

                       }
                    }
                }
            }
        });

        navForwardButton.addActionListener(new ActionListener()
        {
        	@Override
            public void actionPerformed(ActionEvent ae)
            {
                Object src = ae.getSource();
                if(src != null)
                {
                    if(src == navForwardButton)
                    {
                       NavigationButton curNb = buttonList.get(currentIndex);
                       curNb.setSelected(false);

                       validate();
                       NavigationButton nextNb = buttonList.get(currentIndex + 1);
                       nextNb.setSelected(true);

                       setCurrentIndex(currentIndex + 1);
                       
                       EditorForm editorForm = nextNb.getEditorForm();
                       TreePath path = nextNb.getSelectedElement();
                       
                       editorForm.setSelectedElement(path);

                       try
                       {
                            editorForm.setSelected(true);
                       }
                       catch(PropertyVetoException pve)
                       {

                       }
                    }
                }
            }
        });
    }

    private void initComponents2()
    {
        initNavButtons();
    }

    /** Creates new form NavigationPanel */
    public NavigationPanel()
    {
        initComponents();
        initComponents2();
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

        forwardAndBackButtonContainer = new javax.swing.JPanel();
        navBackButton = new javax.swing.JButton();
        navForwardButton = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JSeparator();
        dynamicButtonContainer = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        forwardAndBackButtonContainer.setLayout(new java.awt.GridBagLayout());

        navBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/g2inc/scap/editor/gui/icons/navpanel-back.png"))); // NOI18N
        navBackButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        forwardAndBackButtonContainer.add(navBackButton, gridBagConstraints);

        navForwardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/g2inc/scap/editor/gui/icons/navpanel-forward.png"))); // NOI18N
        navForwardButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        forwardAndBackButtonContainer.add(navForwardButton, gridBagConstraints);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        forwardAndBackButtonContainer.add(jSeparator1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.02;
        add(forwardAndBackButtonContainer, gridBagConstraints);

        dynamicButtonContainer.setAutoscrolls(true);
        dynamicButtonContainer.setDoubleBuffered(false);
        dynamicButtonContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.98;
        add(dynamicButtonContainer, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dynamicButtonContainer;
    private javax.swing.JPanel forwardAndBackButtonContainer;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton navBackButton;
    private javax.swing.JToggleButton navForwardButton;
    // End of variables declaration//GEN-END:variables

    public void clearButtons()
    {
        Iterator<NavigationButton> itr = buttonList.iterator();

        while(itr.hasNext())
        {
            NavigationButton button = itr.next();
            dynamicButtonContainer.remove(button);
            button.setVisible(false);
            button.setEditorForm(null);
            button.setSelectedElement(null);

            itr.remove();
        }
        
        setCurrentIndex(-1);
    }
}
