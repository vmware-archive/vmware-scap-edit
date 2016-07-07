package com.g2inc.scap.editor.gui.windows.oval.test;
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

import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.string.PatternedStringField;
import com.g2inc.scap.editor.gui.util.EditorUtil;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.common.GenericSourceDetailTab;
import com.g2inc.scap.editor.gui.windows.common.WeakChangeListener;
import com.g2inc.scap.editor.gui.windows.oval.OvalTab;
import com.g2inc.scap.editor.gui.windows.oval.OvalVersionControl;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVersion;

public class TestDetailTab extends OvalTab implements ChangeListener
{
    private static final long serialVersionUID = 1L;
    private OvalTest ovalTest = null;
    private GenericSourceDetailTab sourceTab = null;

    private void initComponents2()
    {
    }

    /** Creates new form DefinitionDetailTab */
    public TestDetailTab()
    {
        initComponents();
        initComponents2();
    }

     public void setDoc(OvalTest doc)
    {
        this.ovalTest = doc;
        testDetailPanel.setModalParent(false);
        testDetailPanel.setOvalTest(doc);
        testDetailPanel.addChangeListener(new WeakChangeListener(this));
    }

    public GenericSourceDetailTab getSourceTab()
    {
        return sourceTab;
    }

    public void setSourceTab(GenericSourceDetailTab sourceTab)
    {
        this.sourceTab = sourceTab;
    }

    private void updateSource()
    {
        sourceTab.setXMLSource(elementToString(ovalTest.getElement()));
    }

    @Override
    public void stateChanged(ChangeEvent arg0)
    {
        Object src = arg0.getSource();

        if(src == testDetailPanel)
        {
            if(testDetailPanel.hasChanged())
            {
                ovalTest.setStates(testDetailPanel.getStates());
                updateSource();
                EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
            }
            
            if(!testDetailPanel.allDataOK())
            {
                // TODO:  show some indication that it's not valid
            }
        }
        else if(src instanceof OvalVersionControl)
        {
            OvalVersion ver = new OvalVersion();
            ver.setVersion(testDetailPanel.getVersion().toString());
            ovalTest.setVersion(ver);

            updateSource();
            EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
        }
        else if(src == testDetailPanel.getTestIdField())
        {
            PatternedStringField testIdField = testDetailPanel.getTestIdField();
            JLabel testIdCaption = testDetailPanel.getTestIdCaption();

            if(testIdField.isValidInput())
            {
                // the id typed in is valid
                String newName = testIdField.getValue();

                if(!newName.equals(ovalTest.getId()))
                {
                    if(ovalTest.getParentDocument().containsTest(newName))
                    {
                        // the id they are trying to set already exists.
                        testIdCaption.setForeground(testIdField.getErrorTextColor());
                    }
                    else
                    {
                        // the new name is available, use it.
                        testIdCaption.setForeground(testIdField.getNormalTextColor());
                        ovalTest.setId(newName);

                        updateSource();
                        parentEditorForm.updatedSelectedPath();
                        EditorUtil.markActiveWindowDirty(EditorMainWindow.getInstance(), true);
                    }
                }
                else
                {
                    // the id they typed in is the same as the original
                    // nothing to do
                    testIdCaption.setForeground(testIdField.getNormalTextColor());
                }
            }
            else
            {
                testIdCaption.setForeground(testIdField.getErrorTextColor());
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

        testDetailPanel = new com.g2inc.scap.editor.gui.windows.oval.test.TestDetailDisplayPanel();

        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(testDetailPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.g2inc.scap.editor.gui.windows.oval.test.TestDetailDisplayPanel testDetailPanel;
    // End of variables declaration//GEN-END:variables
}
