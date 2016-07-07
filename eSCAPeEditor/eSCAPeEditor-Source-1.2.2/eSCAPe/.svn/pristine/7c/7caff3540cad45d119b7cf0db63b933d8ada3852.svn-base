package com.g2inc.scap.editor.gui.windows.wizardmode.wizards.file;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.dialogs.editors.EditorDialog;
import com.g2inc.scap.editor.gui.dialogs.editors.oval.datatype.patternmatch.RegexDatatypeEditor;
import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.windows.wizardmode.WizardModeWizard;
import com.g2inc.scap.editor.gui.wizards.WizardPage;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.AffectedItemContainer;
import com.g2inc.scap.library.domain.oval.AffectedItemFamilyEnum;
import com.g2inc.scap.library.domain.oval.AffectedPlatform;
import com.g2inc.scap.library.domain.oval.CheckEnumeration;
import com.g2inc.scap.library.domain.oval.Criteria;
import com.g2inc.scap.library.domain.oval.Criterion;
import com.g2inc.scap.library.domain.oval.DefinitionClassEnum;
import com.g2inc.scap.library.domain.oval.ExistenceEnumeration;
import com.g2inc.scap.library.domain.oval.HashTypeEnum;
import com.g2inc.scap.library.domain.oval.OperEnum;
import com.g2inc.scap.library.domain.oval.OvalCriteriaOperatorEnum;
import com.g2inc.scap.library.domain.oval.OvalDatatype;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalEntity;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalObjectParameter;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalStateParameter;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVariable;
import com.g2inc.scap.library.domain.oval.RecurseDirectionBehavior;
import com.g2inc.scap.library.domain.oval.TypeEnum;
import com.g2inc.scap.library.domain.oval.VersionedOvalElement;

public class FileWizardPage extends WizardPage implements ActionListener
{

    private static Logger LOG = Logger.getLogger(FileWizardPage.class);
    public static final String FILE_STATE = "file_state";
    public static final String FILEHASH_STATE = "filehash_state";

    public static final String UNLIMITED_DEPTH_OPTION = "Unlimited";
    public static final String [] DEPTH_OPTIONS
        = {
            "Unlimited",
            "1",
            "2",
            "3",
            "4",
            "5",
            "10",
            "20",
            "100"
          };


    private String platform = null;
    private OvalDefinitionsDocument ovalDocument = null;
    private List<OvalEntity> possibleFilehashStateParms = null;
    private List<OvalEntity> possibleFileStateParms = null;
    private DataEditorPanel currentEditorPanel = null;

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object src = ae.getSource();

        if(src == pathRegexCheckbox)
        {
            if (pathRegexCheckbox.isSelected())
            {
                editPathRegexButton.setEnabled(true);
            } else
            {
                editPathRegexButton.setEnabled(false);
            }
        }
        else if(src == filenameRegexCheckbox)
        {
            if (filenameRegexCheckbox.isSelected())
            {
                editFilenameRegexButton.setEnabled(true);
            } else
            {
                editFilenameRegexButton.setEnabled(false);
            }
        }
        else if(src == editPathRegexButton)
        {
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            editor.setEditorPage(new RegexDatatypeEditor());

            String existing = (String) pathCombo.getSelectedItem();

            editor.setData(existing);

            editor.pack();
            editor.setLocationRelativeTo(this);
            editor.validate();
            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                DefaultComboBoxModel comboModel = (DefaultComboBoxModel) pathCombo.getModel();

                String updated = (String) editor.getData();

                if (!existing.equals(updated))
                {
                    comboModel.removeElementAt(comboModel.getIndexOf(existing));
                    comboModel.addElement(updated);
                    comboModel.setSelectedItem(updated);
                }
            }
        }
        else if(src == editFilenameRegexButton)
        {
            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            editor.setEditorPage(new RegexDatatypeEditor());
            editor.setData(filenameField.getText());
            editor.pack();
            editor.setLocationRelativeTo(this);
            editor.validate();
            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                filenameField.setText((String) editor.getData());
            }
        }
        else if(src == addCriteriaButton)
        {
            ListItem selected = (ListItem) availableCriteriaList.getSelectedValue();

            if (selected != null)
            {
                EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
                editor.setName("editor_dialog");
                DataEditorPanel page = new DataEditorPanel();
                currentEditorPanel = page;

                editor.setEditorPage(page);

                OperEnum[] validOperations = ovalDocument.getOperationsForDatatype(selected.ovalEntity.getDatatype());
                page.setValidOperations(validOperations);
                editor.setData(selected);
                editor.validate();
                editor.pack();
                editor.setLocationRelativeTo(this);
                editor.setVisible(true);

                if (!editor.wasCancelled())
                {
                    DefaultListModel availModel = (DefaultListModel) availableCriteriaList.getModel();
                    DefaultListModel addedModel = (DefaultListModel) addedList.getModel();

                    availModel.removeElement(selected);
                    addedModel.addElement(selected);

                    checkSatisfied();
                }

                currentEditorPanel = null;
            }
        }
        else if (src == editButton)
        {
            ListItem li = (ListItem) addedList.getSelectedValue();

            EditorDialog editor = new EditorDialog(EditorMainWindow.getInstance(), true);
            DataEditorPanel page = new DataEditorPanel();

            currentEditorPanel = page;

            editor.setEditorPage(page);

            OperEnum[] validOperations = ovalDocument.getOperationsForDatatype(li.ovalEntity.getDatatype());
            page.setValidOperations(validOperations);
            editor.setData(li);
            editor.validate();
            editor.pack();
            editor.setLocationRelativeTo(this);
            editor.setVisible(true);

            if (!editor.wasCancelled())
            {
                DefaultListModel model = (DefaultListModel) addedList.getModel();

                int index = addedList.getSelectedIndex();
                model.set(index, li);
            }

            currentEditorPanel = null;

            checkSatisfied();
        }
        else if (src == removeButton)
        {
            ListItem selected = (ListItem) addedList.getSelectedValue();

            if (selected != null)
            {
                DefaultListModel availModel = (DefaultListModel) availableCriteriaList.getModel();
                DefaultListModel addedModel = (DefaultListModel) addedList.getModel();

                addedModel.removeElement(selected);
                availModel.addElement(selected);

                checkSatisfied();
            }
        }
        else if(src == existsRadio)
        {
            enableCriteriaSection(false);
            checkSatisfied();
        }
        else if(src == notExistsRadio)
        {
            enableCriteriaSection(false);
            checkSatisfied();
        }
        else if(src == criteriaSectionRadio)
        {
            enableCriteriaSection(true);
            checkSatisfied();
        }
        else if(src == pathCombo)
        {
            String comboVal = (String) pathCombo.getSelectedItem();

            if (comboVal != null)
            {
                if (comboVal.length() > 0)
                {
                    pathProvided(true);
                    checkSatisfied();
                } else
                {
                    pathProvided(false);
                    checkSatisfied();
                }
            } else
            {
                pathProvided(false);
                checkSatisfied();
            }
        }
        else if(src == recurseCheckbox)
        {
            enableRecursionOptions(recurseCheckbox.isSelected());
        }
    }

    private void initCheckBoxListeners()
    {
        pathRegexCheckbox.addActionListener(this);
        filenameRegexCheckbox.addActionListener(this);
        recurseCheckbox.addActionListener(this);
    }

    private void initButtonListeners()
    {
        editPathRegexButton.addActionListener(this);
        editFilenameRegexButton.addActionListener(this);
        addCriteriaButton.addActionListener(this);
        editButton.addActionListener(this);
        removeButton.addActionListener(this);
    }

    private void initRadioListeners()
    {
        existsRadio.addActionListener(this);
        notExistsRadio.addActionListener(this);

        criteriaSectionRadio.addActionListener(this);
    }

    private void initCombos()
    {
        pathCombo.addActionListener(this);

        RecurseDirectionBehavior [] values = RecurseDirectionBehavior.values();

        for(int x = 0 ; x < values.length; x++)
        {
            recurseDirectionCombo.addItem(values[x]);
        }

        recurseDirectionCombo.setSelectedItem(RecurseDirectionBehavior.down);

        for(int x = 0 ; x < DEPTH_OPTIONS.length; x++)
        {
            recurseDepthCombo.addItem(DEPTH_OPTIONS[x]);
        }

        recurseDepthCombo.setSelectedItem(UNLIMITED_DEPTH_OPTION);

    }

    private void initLists()
    {
        availableCriteriaList.setModel(new DefaultListModel());
        addedList.setModel(new DefaultListModel());

        availableCriteriaList.setCellRenderer(new AvailableListCellRenderer());
        availableCriteriaList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                ListItem selectedItem = (ListItem) availableCriteriaList.getSelectedValue();

                if (selectedItem != null)
                {
                    addCriteriaButton.setEnabled(true);
                } else
                {
                    addCriteriaButton.setEnabled(false);
                }
            }
        });

        addedList.setCellRenderer(new AddedListCellRenderer());
        addedList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                ListItem selectedItem = (ListItem) addedList.getSelectedValue();

                if (selectedItem != null)
                {
                    editButton.setEnabled(true);
                    removeButton.setEnabled(true);
                } else
                {
                    editButton.setEnabled(false);
                    removeButton.setEnabled(false);
                }
            }
        });
    }

    private void initTextFields()
    {
        defTitleField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent e)
            {
                checkSatisfied();
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

        filenameField.getDocument().addDocumentListener(new DocumentListener()
        {
            private void common(DocumentEvent e)
            {
                String val = filenameField.getText();

                if (val != null && val.length() > 0)
                {
                    if (possibleFilehashStateParms != null)
                    {
                        addHashItem(true);
                    }

                    criteriaSectionRadio.setEnabled(true);
                    filenameRegexCheckbox.setEnabled(true);
                }
                else
                {
                    filenameRegexCheckbox.setEnabled(false);
                    enableCriteriaSection(false);
                    if (criteriaSectionRadio.isSelected())
                    {
                        existsRadio.setSelected(true);
                        initLists();
                        initListValues();
                    }

                    criteriaSectionRadio.setEnabled(false);
                    if (possibleFilehashStateParms != null)
                    {
                        addHashItem(false);
                    }
                }

                checkSatisfied();
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
        initCheckBoxListeners();
        initButtonListeners();
        initRadioListeners();
        initTextFields();
        initLists();
        initCombos();
        enableRecursionOptions(false); // turn off recursion by default
    }

    private void enableCriteriaSection(boolean b)
    {
        availableCriteriaList.setSelectedIndex(-1);

        if (!b)
        {
            resetAddedCriteria();
        }

        addCriteriaButton.setEnabled(b);
        availableCriteriaList.setEnabled(b);

        addedList.setEnabled(b);
    }

    private void enableRecursionOptions(boolean b)
    {
        recurseDirectionCaption.setEnabled(b);
        recurseDirectionCombo.setEnabled(b);
        recurseDepthCaption.setEnabled(b);
        recurseDepthCombo.setEnabled(b);
    }
    private void pathProvided(boolean b)
    {
        existsRadio.setEnabled(b);
        notExistsRadio.setEnabled(b);
    }

    private void resetAddedCriteria()
    {
        DefaultListModel addedModel = (DefaultListModel) addedList.getModel();
        DefaultListModel availModel = (DefaultListModel) availableCriteriaList.getModel();

        if (addedModel.size() > 0)
        {
            while (addedModel.size() > 0)
            {
                ListItem li = (ListItem) addedModel.get(0);
                availModel.addElement(li);
                addedModel.removeElement(li);
            }
        }
    }

    /** Creates new form FileWizardPage */
    public FileWizardPage(String platform, WizardModeWizard parentWiz, OvalDefinitionsDocument odd)
    {
        initComponents();
        setWizard(parentWiz);
        initComponents2();
        ovalDocument = odd;
        setPlatform(platform);
    }

    @Override
    public Object getData()
    {
        return null;
    }

    @Override
    public void setData(Object data)
    {
    }

    @Override
    public String getPageTitle()
    {
        return "Basic File Info";
    }

    private void addBehaviors(OvalObject o)
    {
        String RECURSE_KEY = "recurse_direction";
        String MAX_DEPTH_KEY = "max_depth";

        Map<String,String> behaviors = new HashMap<String, String>();

        if(recurseCheckbox.isSelected())
        {
            RecurseDirectionBehavior selectedDirection = (RecurseDirectionBehavior) recurseDirectionCombo.getSelectedItem();
            behaviors.put(RECURSE_KEY, selectedDirection.name());

            String chosenDepth = (String) recurseDepthCombo.getSelectedItem();
            
            if(chosenDepth.equals(UNLIMITED_DEPTH_OPTION))
            {
                behaviors.put(MAX_DEPTH_KEY, "-1");                
            }
            else
            {
                behaviors.put(MAX_DEPTH_KEY, chosenDepth);                
            }
        }

        if(behaviors.size() > 0)
        {
            o.setBehaviors(behaviors);
        }
    }

    /**
     * Create the various tests/objects/states/variables that need to be created
     * to match what the user chose.
     *
     * @param def Definition which will reference the tests/objects/states/variables created. This will have already been added to the document.
     */
    @Override
    public void performFinish()
    {
        OvalDefinition def = ovalDocument.createDefinition(DefinitionClassEnum.VULNERABILITY);
        ovalDocument.add(def);

        def.getMetadata().setTitle(defTitleField.getText());
        String defDesc = "File content that checks " + (String) pathCombo.getSelectedItem();

        if (filenameField.getText() != null)
        {
            defDesc += " for file named " + filenameField.getText();
        }

        def.getMetadata().setDescription(defDesc);
        AffectedItemContainer affectedPlatforms = def.getMetadata().createAffected(ovalDocument);

        if (platform != null)
        {
            if (platform.equals("windows"))
            {
                AffectedPlatform win2kPlatform = affectedPlatforms.createAffectedPlatform(ovalDocument);
                win2kPlatform.setValue("Microsoft Windows 2000");
                affectedPlatforms.addAffectedItem(win2kPlatform);

                AffectedPlatform winXPPlatform = affectedPlatforms.createAffectedPlatform(ovalDocument);
                winXPPlatform.setValue("Microsoft Windows XP");
                affectedPlatforms.addAffectedItem(winXPPlatform);

                AffectedPlatform win2k3ServerPlatform = affectedPlatforms.createAffectedPlatform(ovalDocument);
                win2k3ServerPlatform.setValue("Microsoft Windows Server 2003");
                affectedPlatforms.addAffectedItem(win2k3ServerPlatform);

                AffectedPlatform win2k8ServerPlatform = affectedPlatforms.createAffectedPlatform(ovalDocument);
                win2k8ServerPlatform.setValue("Microsoft Windows Server 2008");
                affectedPlatforms.addAffectedItem(win2k8ServerPlatform);

                AffectedPlatform winVistaPlatform = affectedPlatforms.createAffectedPlatform(ovalDocument);
                winVistaPlatform.setValue("Microsoft Windows Vista");
                affectedPlatforms.addAffectedItem(winVistaPlatform);

                AffectedPlatform win7Platform = affectedPlatforms.createAffectedPlatform(ovalDocument);
                win7Platform.setValue("Microsoft Windows 7");
                affectedPlatforms.addAffectedItem(win7Platform);

                affectedPlatforms.setFamily(AffectedItemFamilyEnum.windows);
                def.getMetadata().addAffected(affectedPlatforms);
            }
        }

        DefaultListModel addedModel = (DefaultListModel) addedList.getModel();
        
        Criteria criteria = def.createCriteria();
        criteria.setOperator(OvalCriteriaOperatorEnum.AND);

        if (existsRadio.isSelected() || notExistsRadio.isSelected())
        {
            // only checking for existence
            OvalTest fileTest = ovalDocument.createTest(platform, "file_test");
            ovalDocument.add(fileTest);

            fileTest.setCheck(CheckEnumeration.ALL);

            if (existsRadio.isSelected())
            {
                fileTest.setCheckExistence(ExistenceEnumeration.AT_LEAST_ONE_EXISTS);
            } else
            {
                fileTest.setCheckExistence(ExistenceEnumeration.NONE_EXIST);
            }

            // add a file object
            OvalObject fileObj = ovalDocument.createObject(platform, "file_object");
            ovalDocument.add(fileObj);

            addBehaviors(fileObj);
            OvalObjectParameter pathOop = fileObj.createObjectParameter("path");
            fileObj.addChild(pathOop);

            OvalObjectParameter filenameOop = fileObj.createObjectParameter("filename");
            fileObj.addChild(filenameOop);

            buildFileObject(fileObj);

            // set the object id in the test
            fileTest.setObjectId(fileObj.getId());

            Criterion c = criteria.createCriterion();
            c.setTestId(fileTest.getId());

            criteria.addChild(c);
            c.setComment(fileTest.getComment());

        }
        else
        {
            // check details about file/directory, not just existence
            List<OvalTest> tests = generateFileHashTests();
            OvalTest fileTest = generateFileTest();
            tests.add(fileTest);
            
            for (OvalTest ovalTest : tests) {
                Criterion c = criteria.createCriterion();
                c.setComment(ovalTest.getComment());
                c.setTest(ovalTest);

                criteria.addChild(c);
            }

        }
        def.setCriteria(criteria);
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
        platformLabel.setText(platform);

        DefaultComboBoxModel comboModel = (DefaultComboBoxModel) pathCombo.getModel();
        comboModel.removeAllElements();

        if (platform.equals("windows"))
        {
            // add some predefined paths(Any of these will cause a variable to be created to
        	ArrayList<String> knownVarNames = new ArrayList<String>();
        	knownVarNames.addAll(WizardModeWizard.supportedVariables.keySet());
        	Collections.sort(knownVarNames);
        	
        	for(String item : knownVarNames)
        	{
                comboModel.addElement(item);        	
        	}

        }
        else if (!platform.equals("cisco") && !platform.equals("ios") && !platform.equals("sharepoint"))
        {
            comboModel.addElement("/");
            comboModel.addElement("/bin");
            comboModel.addElement("/etc");
            comboModel.addElement("/sbin");
            comboModel.addElement("/usr/bin");
            comboModel.addElement("/usr/sbin");
        }

        initListValues();

        // hold on to possible filehash state parms for later use
//        possibleFilehashStateParms = ovalDocument.getValidStateEntityTypes("independent", FILEHASH_STATE);
        possibleFilehashStateParms = new ArrayList<OvalEntity>();
        if (isOval58()) {
        	HashTypeEnum[] hashTypes = HashTypeEnum.values();
        	for (HashTypeEnum hashType : hashTypes) {
        		possibleFilehashStateParms.add(getFilehashEntity(hashType));
        	}
        } else {
        	possibleFilehashStateParms.add(getFilehashEntity(HashTypeEnum.MD5));
        	possibleFilehashStateParms.add(getFilehashEntity(HashTypeEnum.SHA1));
        }

        enableCriteriaSection(false);
        criteriaSectionRadio.setEnabled(false);
    }
    
    private OvalEntity getFilehashEntity(HashTypeEnum hashtype) {
    	OvalEntity ovalEntity = new OvalEntity(hashtype.getName(), hashtype.getName() + " hash");
    	OvalDatatype ovalDatatype = new OvalDatatype(TypeEnum.STRING);
    	ovalEntity.setDatatype(ovalDatatype);
    	ovalEntity.setDatatypeString("string");
    	return ovalEntity;
    }

    private void initListValues()
    {
        //
        // populate available criteria
        //
        DefaultListModel availModel = (DefaultListModel) availableCriteriaList.getModel();

        possibleFileStateParms = ovalDocument.getValidStateEntityTypes(platform, FILE_STATE);

        if (possibleFileStateParms != null && possibleFileStateParms.size() > 0)
        {
            for (int x = 0; x < possibleFileStateParms.size(); x++)
            {
                OvalEntity oe = possibleFileStateParms.get(x);

                String name = oe.getName();
                if (name.equals("path") || name.equals("filepath") || name.equals("filename")) {
                	continue;
                }

                ListItem li = new ListItem();
                li.elementName = FILE_STATE;
                li.ovalEntity = oe;
                li.field = name;
                li.datatype = oe.getDatatype();

                availModel.addElement(li);
            }
        }
    }

    // check if available list contains a hash item
    private boolean availableContainsHashItem()
    {
        DefaultListModel availModel = (DefaultListModel) availableCriteriaList.getModel();

        int size = availModel.getSize();

        if (size > 0)
        {
            for (int x = 0; x < size; x++)
            {
                ListItem li = (ListItem) availModel.get(x);

                if (li.elementName != null && li.elementName.equals(FILEHASH_STATE))
                {
                    return true;
                }
            }
        }

        return false;
    }

    // check if available list contains a hash item
    private boolean addedContainsHashItem()
    {
        DefaultListModel addedModel = (DefaultListModel) addedList.getModel();

        int size = addedModel.getSize();

        if (size > 0)
        {
            for (int x = 0; x < size; x++)
            {
                ListItem li = (ListItem) addedModel.get(x);

                if (li.elementName != null && li.elementName.equals(FILEHASH_STATE))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private void addHashItem(boolean b)
    {
        DefaultListModel availModel = (DefaultListModel) availableCriteriaList.getModel();
        DefaultListModel addedModel = (DefaultListModel) addedList.getModel();

        if (b)
        {
            String filename = filenameField.getText();

            if (filename != null && filename.length() > 0)
            {
                if (!addedContainsHashItem() && !availableContainsHashItem())
                {
                    for (int x = 0; x < possibleFilehashStateParms.size(); x++)
                    {
                        OvalEntity oe = possibleFilehashStateParms.get(x);

                        String name = oe.getName();

                        if (name.equals("path") || name.equals("filename"))
                        {
                            // skip because they overlap with file_object
                            continue;
                        }

                        ListItem hashItem = new ListItem();
                        hashItem.elementName = FILEHASH_STATE;
                        hashItem.field = oe.getName();
                        hashItem.datatype = oe.getDatatype();
                        hashItem.operation = OperEnum.EQUALS;
                        hashItem.ovalEntity = oe;
                        availModel.addElement(hashItem);
                    }
                }
            } else
            {
                // no filename supplied, remove hash option
            }
        } else
        {
            if (addedContainsHashItem())
            {
                int removedElements = 0;
                int x = 0;
                while (removedElements < possibleFilehashStateParms.size() - 2)
                {
                    ListItem li = (ListItem) addedModel.get(x);

                    if (li.elementName != null && li.elementName.equals(FILEHASH_STATE))
                    {
                        addedModel.remove(x);
                        removedElements++;

                        // not incrementing x here because we removed the element at x which
                        // should make the current value of x the valid index of the next
                        // element.
                    } else
                    {
                        x++;
                    }
                }
            }

            if (availableContainsHashItem())
            {
                int removedElements = 0;
                int x = 0;
                while (removedElements < possibleFilehashStateParms.size() - 2)
                {
                    ListItem li = (ListItem) availModel.get(x);

                    if (li.elementName != null && li.elementName.equals(FILEHASH_STATE))
                    {
                        availModel.remove(x);
                        removedElements++;

                        // not incrementing x here because we removed the element at x which
                        // should make the current value of x the valid index of the next
                        // element.
                    } else
                    {
                        x++;
                    }
                }
            }
        }
    }

    private boolean pathValid()
    {
        String pathname = (String) pathCombo.getSelectedItem();

        if (pathname == null || pathname.length() == 0)
        {
            return false;
        }



        return true;
    }

    private boolean filenameValid()
    {
        String filename = filenameField.getText();

        if (filename != null && filename.length() > 0)
        {
            // check something about the filename
        }

        // no filename is a valid condition
        return true;
    }

    /**
     * Make sure that all conditions are correct before we allow moving on to the next page.
     */
    public void checkSatisfied()
    {
        if (defTitleField.getText() == null || defTitleField.getText().length() == 0)
        {
            parentWizard.disableNextButton();
            satisfied = false;
            return;
        }

        if (pathValid())
        {
            if (filenameValid())
            {
                if (existsRadio.isSelected() || notExistsRadio.isSelected())
                {
                    parentWizard.enableNextButton();
                    satisfied = true;
                } else
                {
                    // check to make sure they supplied something to check the path
                    // or files by, like hash, or whatever
                    DefaultListModel model = (DefaultListModel) addedList.getModel();

                    if (model.size() > 0)
                    {

                        // check that all added criteria have values
                        for (int x = 0; x < model.size(); x++)
                        {
                            ListItem li = (ListItem) model.get(x);
                            if (li.value == null)
                            {
                                parentWizard.disableNextButton();
                                satisfied = false;

                                return;
                            }
                        }
                        parentWizard.enableNextButton();
                        satisfied = true;
                    } else
                    {
                        parentWizard.disableNextButton();
                        satisfied = false;
                    }
                }
            }
            else
            {
                criteriaSectionRadio.setEnabled(false);
                enableCriteriaSection(false);
                parentWizard.disableNextButton();
                satisfied = false;
            }
        } else
        {
            parentWizard.disableNextButton();
            satisfied = false;
        }
    }

    public JButton getAddCriteriaButton()
    {
        return addCriteriaButton;
    }

    public JList getAvailableCriteriaList()
    {
        return availableCriteriaList;
    }

    public JList getAddedCriteriaList()
    {
        return addedList;
    }

    public JRadioButton getCriteriaSectionRadio()
    {
        return criteriaSectionRadio;
    }

    public JTextField getDefTitleField()
    {
        return defTitleField;
    }

    public JRadioButton getExistsRadio()
    {
        return existsRadio;
    }

    public JTextField getFilenameField()
    {
        return filenameField;
    }

    public JRadioButton getNotExistsRadio()
    {
        return notExistsRadio;
    }

    public JComboBox getPathCombo()
    {
        return pathCombo;
    }

    public JCheckBox getPathRegexCheckbox()
    {
        return pathRegexCheckbox;
    }

    public DataEditorPanel getCurrentEditorPanel()
    {
        return currentEditorPanel;
    }

    public JCheckBox getFilenameRegexCheckbox()
    {
        return filenameRegexCheckbox;
    }
    
    private OvalTest generateFileTest() {
        // regardless of how many things about a file or directory
        // we are checking, we only need one file object to map to each
        // state using a test
        
        OvalTest fileTest = ovalDocument.createTest(platform, "file_test");
        ovalDocument.add(fileTest);
        
        OvalObject fileObj = ovalDocument.createObject(platform, "file_object");
        buildFileObject(fileObj);
        
        OvalState fileState = ovalDocument.createState(platform, "file_state");
        ovalDocument.add(fileState);
            
        fileTest.setComment("File test of " + fileObj.getComment());
        fileTest.setObjectId(fileObj.getId());
        fileTest.setStateId(fileState.getId());
       
        @SuppressWarnings("rawtypes")
		DefaultListModel addedModel = (DefaultListModel) addedList.getModel();
        String conjunction = "";
        String stateComment = "Check file for ";
        for (int i=0; i<addedModel.size(); i++) {
            ListItem listItem = (ListItem) addedModel.getElementAt(i);
            if (listItem.elementName.indexOf("file_") == -1) {
                continue;
            }
            
            OvalStateParameter osp = fileState.createOvalStateParameter(listItem.field);
            osp.setValue(listItem.value);
            osp.setOperation(listItem.operation.toString());
            fileState.addParameter(osp);
            stateComment += (conjunction + listItem.field + " " + listItem.operation.toString() + " " + listItem.value + " ");
            conjunction = " AND ";
        }
        fileState.setComment(stateComment);
        return fileTest;
    }
    
    private List<OvalTest> generateFileHashTests() {
        List<OvalTest> hashTests = new ArrayList<OvalTest>();
        OvalTest filehashTest = null;
        OvalObject filehashObj = null;
        OvalState filehashState = null;
        boolean use58 = isOval58();
        @SuppressWarnings("rawtypes")
		DefaultListModel addedModel = (DefaultListModel) addedList.getModel();
        for (int i=0; i<addedModel.size(); i++) {
            ListItem listItem = (ListItem) addedModel.getElementAt(i);
            if (listItem.elementName.indexOf("filehash_") == -1) {
                continue;
            }
            String hashType = listItem.field;
            OvalStateParameter hashValueParm = null;
            if (use58) {
                filehashTest = ovalDocument.createTest("independent", "filehash58_test");
                filehashObj = ovalDocument.createObject("independent", "filehash58_object");
                filehashState = ovalDocument.createState("independent", "filehash58_state");
                
                buildFileObject(filehashObj);
                String fileObjComment = filehashObj.getComment();
                fileObjComment += (", hash_type = " + hashType);
                filehashObj.setComment(fileObjComment);
                
            	OvalObjectParameter hashTypeParm =  filehashObj.createObjectParameter("hash_type");
            	hashTypeParm.setValue(hashType);
            	filehashObj.addChild(hashTypeParm);
            	
            	hashValueParm = filehashState.createOvalStateParameter("hash");  
            } else {
                filehashTest = ovalDocument.createTest("independent", "filehash_test");
                filehashObj = ovalDocument.createObject("independent", "filehash_object");
                filehashState = ovalDocument.createState("independent", "filehash_state");
                
                buildFileObject(filehashObj);
                
            	String entityName = null;
            	if (hashType.equals("MD5")) {
            		entityName = "md5";
            	} else if (hashType.equals("SHA-1")) {
            		entityName = "sha1";
            	}
            	hashValueParm = filehashState.createOvalStateParameter(entityName);
            }
            hashValueParm.setValue(listItem.value);
            hashValueParm.setOperation(listItem.operation.toString());
            filehashState.addParameter(hashValueParm);
            filehashState.setComment("Check files for " + hashType + " " + listItem.operation.toString() + " " + listItem.value + " ");
            
            ovalDocument.add(filehashTest);
            ovalDocument.add(filehashObj);
            ovalDocument.add(filehashState);
            
            filehashTest.setComment("File hash test of " + filehashObj.getComment());
            filehashTest.setCheck(CheckEnumeration.ALL);
            filehashTest.setCheckExistence(ExistenceEnumeration.AT_LEAST_ONE_EXISTS);

            // set the object id we already know
            filehashTest.setObjectId(filehashObj.getId());
            filehashTest.setStateId(filehashState.getId());

            hashTests.add(filehashTest);

        }
            

        return hashTests;
    }
    
    private boolean isOval58() {
        SCAPDocumentTypeEnum docType = ovalDocument.getDocumentType();
        return (docType == SCAPDocumentTypeEnum.OVAL_58 || 
                docType == SCAPDocumentTypeEnum.OVAL_59 || 
                docType == SCAPDocumentTypeEnum.OVAL_510);
    }
    
    private void buildFileObject(OvalObject fileObject) {
    	
        String path = (String) pathCombo.getSelectedItem();
        String filename = filenameField.getText();
        String objComment = "File path " + path;
        if (filename != null && filename.length() > 0) {
            objComment += ", file name " + filename;
        }
        
        ovalDocument.add(fileObject);
        addBehaviors(fileObject);

        fileObject.setComment(objComment);

        OvalObjectParameter pathOop = fileObject.createObjectParameter("path");
        fileObject.addChild(pathOop);

        OvalObjectParameter filenameOop = fileObject.createObjectParameter("filename");
        fileObject.addChild(filenameOop);

        OvalVariable pathVar = null;

        if (platform.equals("windows")) {
            // do environment variable checking magic
            pathVar = ((WizardModeWizard) parentWizard).handleWindowsEnvironmentVariableCreation(path);
        }

        if (pathVar != null) {
            pathOop.setVariableReference(pathVar);
            pathOop.setVarCheck("all");
        } else {
            pathOop.setValue(path);
            if (pathRegexCheckbox.isSelected()) {
                pathOop.setOperation(OperEnum.PATTERN_MATCH.toString());
            }
        }

        if (filename != null && filename.length() > 0) {
            filenameOop.setValue(filename);
            if (filenameRegexCheckbox.isSelected()) {
                filenameOop.setOperation(OperEnum.PATTERN_MATCH.toString());
            }
        } else {
            filenameOop.setNil(true);
        }
    }
    
    private OvalEntity getOvalEntityForName(VersionedOvalElement ovalStateOrObject, String name) {
    	List<OvalEntity> validParms = null;
    	if (ovalStateOrObject instanceof OvalState) {
    		validParms = ((OvalState) ovalStateOrObject).getValidParameters();
    	} else if (ovalStateOrObject instanceof OvalObject) {
    		validParms = ((OvalObject) ovalStateOrObject).getValidParameters();
    	} 
        for (OvalEntity ovalEntity : validParms) {
            if (ovalEntity.getName().equals(name)) {
                return ovalEntity;
            }
        }
        return null;
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

        existenceButtonGroup = new javax.swing.ButtonGroup();
        platformPanel = new javax.swing.JPanel();
        platformCaption = new javax.swing.JLabel();
        platformLabel = new javax.swing.JLabel();
        defTitleCaption = new javax.swing.JLabel();
        defTitleField = new javax.swing.JTextField();
        pathCaption = new javax.swing.JLabel();
        pathCombo = new javax.swing.JComboBox();
        pathRegexCheckbox = new javax.swing.JCheckBox();
        editPathRegexButton = new javax.swing.JButton();
        filenameCaption = new javax.swing.JLabel();
        filenameField = new javax.swing.JTextField();
        filenameRegexCheckbox = new javax.swing.JCheckBox();
        editFilenameRegexButton = new javax.swing.JButton();
        recurseCheckbox = new javax.swing.JCheckBox();
        recurseComboPanel = new javax.swing.JPanel();
        recurseDirectionCaption = new javax.swing.JLabel();
        recurseDirectionCombo = new javax.swing.JComboBox();
        recurseDepthCaption = new javax.swing.JLabel();
        recurseDepthCombo = new javax.swing.JComboBox();
        existencePanel = new javax.swing.JPanel();
        existsRadio = new javax.swing.JRadioButton();
        notExistsRadio = new javax.swing.JRadioButton();
        criteriaPanel = new javax.swing.JPanel();
        criteriaSectionRadio = new javax.swing.JRadioButton();
        availableCriteriaScrollPane = new javax.swing.JScrollPane();
        availableCriteriaList = new javax.swing.JList();
        addCriteriaButton = new javax.swing.JButton();
        addedCaption = new javax.swing.JLabel();
        addedScrollPane = new javax.swing.JScrollPane();
        addedList = new javax.swing.JList();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        setForeground(null);
        setMinimumSize(new java.awt.Dimension(480, 607));
        setName("file_wizard_page"); // NOI18N
        setPreferredSize(new java.awt.Dimension(480, 607));
        setLayout(new java.awt.GridBagLayout());

        platformCaption.setText("Platform");
        platformPanel.add(platformCaption);

        platformLabel.setText("something");
        platformPanel.add(platformLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(platformPanel, gridBagConstraints);

        defTitleCaption.setText("Title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 0, 0);
        add(defTitleCaption, gridBagConstraints);

        defTitleField.setName("TitleTextBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 7);
        add(defTitleField, gridBagConstraints);

        pathCaption.setText("Path");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 8, 0, 0);
        add(pathCaption, gridBagConstraints);

        pathCombo.setEditable(true);
        pathCombo.setMaximumSize(null);
        pathCombo.setMinimumSize(null);
        pathCombo.setName("PathComboBox"); // NOI18N
        pathCombo.setPreferredSize(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 0, 3);
        add(pathCombo, gridBagConstraints);

        pathRegexCheckbox.setText("Regex");
        pathRegexCheckbox.setName("PathRegexCheckBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        add(pathRegexCheckbox, gridBagConstraints);

        editPathRegexButton.setText("Edit");
        editPathRegexButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 5);
        add(editPathRegexButton, gridBagConstraints);

        filenameCaption.setText("Filename");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 8, 0, 0);
        add(filenameCaption, gridBagConstraints);

        filenameField.setName("FilenameTextBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.03;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 3, 0);
        add(filenameField, gridBagConstraints);

        filenameRegexCheckbox.setText("Regex");
        filenameRegexCheckbox.setEnabled(false);
        filenameRegexCheckbox.setName("FilenameRegexCheckBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(filenameRegexCheckbox, gridBagConstraints);

        editFilenameRegexButton.setText("Edit");
        editFilenameRegexButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(editFilenameRegexButton, gridBagConstraints);

        recurseCheckbox.setText("Recurse to find file(s)/directory(ies)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(recurseCheckbox, gridBagConstraints);

        recurseComboPanel.setLayout(new java.awt.GridBagLayout());

        recurseDirectionCaption.setText("Direction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
        recurseComboPanel.add(recurseDirectionCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        recurseComboPanel.add(recurseDirectionCombo, gridBagConstraints);

        recurseDepthCaption.setText("Depth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
        recurseComboPanel.add(recurseDepthCaption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        recurseComboPanel.add(recurseDepthCombo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 26, 0, 0);
        add(recurseComboPanel, gridBagConstraints);

        existencePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "File/Dir Existence", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        existencePanel.setForeground(null);
        existencePanel.setLayout(new java.awt.GridBagLayout());

        existenceButtonGroup.add(existsRadio);
        existsRadio.setSelected(true);
        existsRadio.setText("Exists");
        existsRadio.setEnabled(false);
        existsRadio.setName("existsRadio"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        existencePanel.add(existsRadio, gridBagConstraints);

        existenceButtonGroup.add(notExistsRadio);
        notExistsRadio.setText("Doesn't Exist");
        notExistsRadio.setEnabled(false);
        notExistsRadio.setName("notExistsRadio"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        existencePanel.add(notExistsRadio, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.01;
        add(existencePanel, gridBagConstraints);

        criteriaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "File detail", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), java.awt.SystemColor.windowText)); // NOI18N
        criteriaPanel.setForeground(null);
        criteriaPanel.setLayout(new java.awt.GridBagLayout());

        existenceButtonGroup.add(criteriaSectionRadio);
        criteriaSectionRadio.setText("Must exist and meet the following criteria");
        criteriaSectionRadio.setToolTipText("A file name must be supplied for this to be enabled.");
        criteriaSectionRadio.setEnabled(false);
        criteriaSectionRadio.setName("CriteriaSelectionRadio"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        criteriaPanel.add(criteriaSectionRadio, gridBagConstraints);

        availableCriteriaList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        availableCriteriaList.setEnabled(false);
        availableCriteriaList.setMaximumSize(null);
        availableCriteriaList.setMinimumSize(null);
        availableCriteriaList.setName("availList"); // NOI18N
        availableCriteriaList.setPreferredSize(null);
        availableCriteriaScrollPane.setViewportView(availableCriteriaList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        criteriaPanel.add(availableCriteriaScrollPane, gridBagConstraints);

        addCriteriaButton.setText("Add");
        addCriteriaButton.setEnabled(false);
        addCriteriaButton.setName("add_button"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        criteriaPanel.add(addCriteriaButton, gridBagConstraints);

        addedCaption.setText("Added");
        addedCaption.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 0);
        criteriaPanel.add(addedCaption, gridBagConstraints);

        addedList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        addedList.setEnabled(false);
        addedScrollPane.setViewportView(addedList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, 3);
        criteriaPanel.add(addedScrollPane, gridBagConstraints);

        editButton.setText("Edit");
        editButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        criteriaPanel.add(editButton, gridBagConstraints);

        removeButton.setText("Remove");
        removeButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        criteriaPanel.add(removeButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 4.0;
        add(criteriaPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCriteriaButton;
    private javax.swing.JLabel addedCaption;
    private javax.swing.JList addedList;
    private javax.swing.JScrollPane addedScrollPane;
    private javax.swing.JList availableCriteriaList;
    private javax.swing.JScrollPane availableCriteriaScrollPane;
    private javax.swing.JPanel criteriaPanel;
    private javax.swing.JRadioButton criteriaSectionRadio;
    private javax.swing.JLabel defTitleCaption;
    private javax.swing.JTextField defTitleField;
    private javax.swing.JButton editButton;
    private javax.swing.JButton editFilenameRegexButton;
    private javax.swing.JButton editPathRegexButton;
    private javax.swing.ButtonGroup existenceButtonGroup;
    private javax.swing.JPanel existencePanel;
    private javax.swing.JRadioButton existsRadio;
    private javax.swing.JLabel filenameCaption;
    private javax.swing.JTextField filenameField;
    private javax.swing.JCheckBox filenameRegexCheckbox;
    private javax.swing.JRadioButton notExistsRadio;
    private javax.swing.JLabel pathCaption;
    private javax.swing.JComboBox pathCombo;
    private javax.swing.JCheckBox pathRegexCheckbox;
    private javax.swing.JLabel platformCaption;
    private javax.swing.JLabel platformLabel;
    private javax.swing.JPanel platformPanel;
    private javax.swing.JCheckBox recurseCheckbox;
    private javax.swing.JPanel recurseComboPanel;
    private javax.swing.JLabel recurseDepthCaption;
    private javax.swing.JComboBox recurseDepthCombo;
    private javax.swing.JLabel recurseDirectionCaption;
    private javax.swing.JComboBox recurseDirectionCombo;
    private javax.swing.JButton removeButton;
    // End of variables declaration//GEN-END:variables
    
}
