package com.g2inc.scap.editor.gui.wizards.filesaveas;

import java.util.regex.Pattern;

import com.g2inc.scap.editor.gui.windows.EditorMainWindow;
import com.g2inc.scap.editor.gui.wizards.Wizard;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;

public class FileSaveAsWizard extends Wizard
{
    private EditorMainWindow parentWin = null;

    private PickFilenamePage filenamePage = null;
    private SCAPDocumentTypeEnum docType = null;
    
    private void addPages()
    {
        filenamePage = new PickFilenamePage(this, docType);
        addWizardPage(filenamePage);
    }

    public FileSaveAsWizard(java.awt.Frame parent, boolean modal, SCAPDocumentTypeEnum docType)
    {
        super(parent, modal);
        setTitle("Pick Target Filename");
        parentWin = (EditorMainWindow) parent;
        this.docType = docType;
        
        addPages();
    }

    public EditorMainWindow getParentWin()
    {
        return parentWin;
    }

    public void setParentWin(EditorMainWindow parentWin)
    {
        this.parentWin = parentWin;
    }

    public void setFilenamePattern(Pattern pattern)
    {
        filenamePage.setFilenamePattern(pattern);
    }

    public void setFilenamePatterns(Pattern[] patterns)
    {
        filenamePage.setFilenamePatterns(patterns);
    }

    @Override
    public void performFinish()
    {
    }

    public String getFilename()
    {
        return filenamePage.getFilename();
    }
}
