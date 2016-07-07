package com.g2inc.scap.editor.gui.windows.xccdf;

/* ESCAPE Software   Copyright 2010 G2, Inc. - All rights reserved.
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

import com.g2inc.scap.editor.gui.windows.common.ChangeNotifierPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.xccdf.FixCommon;
import com.g2inc.scap.library.domain.xccdf.StrategyEnum;
import com.g2inc.scap.library.domain.xccdf.RatingEnum;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class FixCommonPanel extends ChangeNotifierPanel implements ActionListener, ItemListener
{

    private static Logger log = Logger.getLogger(FixCommonPanel.class);
    private FixCommon fixCommon = null;
    private static final String UNSPECIFIED = "unspecified";

    /** Creates new form DefinitionDetailTab */
    public FixCommonPanel()
    {
        initComponents();
        initComponents2();
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();
        log.debug("actionPerformed called, eventSource is " + src.getClass().getName());
        if(src == strategyComboBox) {
            String selectedString = (String) strategyComboBox.getSelectedItem();
            if (selectedString != null && fixCommon != null) {
                if (!selectedString.equals(UNSPECIFIED)) {
                    fixCommon.setStrategy(StrategyEnum.valueOf(selectedString));
                } else {
                    fixCommon.setStrategy(null);
                }
            }
        } else if(src == disruptionComboBox) {
            String selectedString = (String) disruptionComboBox.getSelectedItem();
            if (selectedString != null && fixCommon != null) {
                if (!selectedString.equals(UNSPECIFIED)) {
                    fixCommon.setDisruption(RatingEnum.valueOf(selectedString));
                } else {
                    fixCommon.setDisruption(null);
                }
            }
        } else if(src == complexityComboBox) {
            String selectedString = (String) complexityComboBox.getSelectedItem();
            if (selectedString != null && fixCommon != null) {
                if (!selectedString.equals(UNSPECIFIED)) {
                    fixCommon.setComplexity(RatingEnum.valueOf(selectedString));
                } else {
                    fixCommon.setComplexity(null);
                }
            }
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent ie) {
        Object src = ie.getSource();
        if(src == rebootCheckBox) {
            fixCommon.setReboot(ie.getStateChange() == ItemEvent.SELECTED);;
        }
    }

    private void initComboBoxes()
    {
        strategyComboBox.addActionListener(this);
        disruptionComboBox.addActionListener(this);
        complexityComboBox.addActionListener(this);

        StrategyEnum[] strategyValues = StrategyEnum.values();
        strategyComboBox.removeAllItems();    
        strategyComboBox.addItem(UNSPECIFIED);
        for (int i = 0; i < strategyValues.length; i++) {
            strategyComboBox.addItem(strategyValues[i].toString());
        }
        
        RatingEnum[] ratingValues = RatingEnum.values();
        disruptionComboBox.addItem(UNSPECIFIED);    
        for (int i = 0; i < ratingValues.length; i++) {
            disruptionComboBox.addItem(ratingValues[i].toString());
        }
        complexityComboBox.addItem(UNSPECIFIED);    
        for (int i = 0; i < ratingValues.length; i++) {
            complexityComboBox.addItem(ratingValues[i].toString());
        }
    }

    public final void initComponents2()
    {
        initComboBoxes();
        rebootCheckBox.addItemListener(this);
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

        strategyCaption = new javax.swing.JLabel();
        strategyComboBox = new javax.swing.JComboBox();
        disruptionCaption = new javax.swing.JLabel();
        disruptionComboBox = new javax.swing.JComboBox();
        complexityCaption = new javax.swing.JLabel();
        complexityComboBox = new javax.swing.JComboBox();
        rebootCheckBox = new javax.swing.JCheckBox();

        strategyCaption.setText("Strategy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(strategyCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 5);
        add(strategyComboBox, gridBagConstraints);

        disruptionCaption.setText("Disruption");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(disruptionCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 5);
        add(disruptionComboBox, gridBagConstraints);

        complexityCaption.setText("Complexity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(complexityCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 5);
        add(complexityComboBox, gridBagConstraints);

        rebootCheckBox.setText("reboot?");
        rebootCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(rebootCheckBox, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel complexityCaption;
    private javax.swing.JComboBox complexityComboBox;
    private javax.swing.JLabel disruptionCaption;
    private javax.swing.JComboBox disruptionComboBox;
    private javax.swing.JCheckBox rebootCheckBox;
    private javax.swing.JLabel strategyCaption;
    private javax.swing.JComboBox strategyComboBox;
    // End of variables declaration//GEN-END:variables

    public FixCommon getData()
    {
        return fixCommon;
    }

    public void setData(FixCommon fixCommon)
    {
        this.fixCommon = fixCommon;

        StrategyEnum strategy = fixCommon.getStrategy();
        if (strategy != null) {
            strategyComboBox.setSelectedItem(strategy.toString());
        } else {
            strategyComboBox.setSelectedItem(UNSPECIFIED);
        }
        
        RatingEnum complexity = fixCommon.getComplexity();
        if (complexity != null) {
            complexityComboBox.setSelectedItem(complexity.toString());
        } else {
            complexityComboBox.setSelectedItem(UNSPECIFIED);
        }
        
        RatingEnum disruption = fixCommon.getDisruption();
        if (disruption != null) {
            disruptionComboBox.setSelectedItem(disruption.toString());
        } else {
            disruptionComboBox.setSelectedItem(UNSPECIFIED);
        }
        
        rebootCheckBox.setSelected(fixCommon.getReboot());
    }
}
