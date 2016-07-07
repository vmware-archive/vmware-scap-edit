package com.g2inc.scap.editor.gui.resources;

/*
 * Copyright (c) 2016 - 2016. VMware, Inc. All rights reserved.
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License version 3.0 
 * as published by the FreeSoftware Foundation This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License version 3.0 
 * for more details. You should have received a copy of the GNU General Public License version 3.0 along with this program; if not, write to
 * the Free Software Foundation, Inc., 675 Mass Avenue, Cambridge, MA 02139, USA.
 */
  
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

/**
 * A central place to store messages presented to the user
 * @author ssill
 */
public class EditorMessages
{
    /*
     * General strings
     */
	
    public static final String PRODUCT_NAME = "VMware SCAP Edit";
    public static final String PRODUCT_SHORTNAME = "VMware SCAP Edit";
    
    
    // technology name strings
    public static final String OVAL = "OVAL";
    public static final String XCCDF = "XCCDF";
    public static final String CPE = "CPE";
    public static final String CVE = "CVE";
    public static final String CCE = "CCE";
    public static final String SCAP = "SCAP";
    public static final String SCAP_DATA_STREAM = "SCAP Data Stream";
    public static final String OCIL = "OCIL";

    /*
     * FileChooser strings
     */
    public static final String FILE_CHOOSER_BUTTON_TEXT_SAVE = "Save";
    public static final String FILE_CHOOSER_BUTTON_TOOLTIP_TEXT_SAVE = "Choose existing existing file or type a new filename";
    public static final String FILE_CHOOSER_FILTER_DESC_XML = "XML files";

    /*
     * Create oval wizard stuff
     */
    public static final String NEW_OVAL_WIZARD_FILENAME_CHOICE_NOTE_TEXT =
            "<HTML>Click the Choose File button to select a name for the new " + OVAL +" file." +
            " If you select an existing file, it will be overwritten." +
            " If you enter a new file name, it will be created.</HTML>";

    public static final String NEW_OVAL_WIZARD_FILENAME_CHOICE_PAGE_TITLE = "Choose name for new " + OVAL + " file";

    /*
     * Create xccdf wizard stuff
     */
    public static final String NEW_XCCDF_WIZARD_FILENAME_CHOICE_NOTE_TEXT =
            "<HTML>Click the Choose File button to select a name for the new " + XCCDF +" file." +
            " If you select an existing file, it will be overwritten." +
            " If you enter a new file name, it will be created.</HTML>";

    public static final String NEW_XCCDF_WIZARD_FILENAME_CHOICE_PAGE_TITLE = "Choose name for new " + XCCDF + " file";

    /*
     * Misc strings
     */
    public static final String CAPTION_FILENAME = "Filename";
    public static final String CHOOSE_FILE_BUTTON_TEXT = "Choose file";
    public static final String XCCDF_EDITOR_FORM_BASE_TITLE = XCCDF + " Benchmark - ";
    public static final String OVAL_EDITOR_FORM_BASE_TITLE = OVAL + " Document - ";
    public static final String CPE_EDITOR_FORM_BASE_TITLE = CPE + " Dictionary - ";

    /*
     * Error dialog related
     */

    public static final String SAVE_ERROR_DIALOG_TITLE = "Save Error";
    public static final String LOAD_DOCUMENT_ERROR_TITLE = "Load Error";
    public static final String LOAD_DOCUMENT_ENCODING_ERROR_TITLE = "File Encoding Error";
    public static final String DOCUMENT_MISMATCH_ERROR_TITLE = "Document Type Mismatch";
    public static final String XCCDF_FROM_OVAL_DIRECTORY_TEXT = "Directory in which " + XCCDF + " file will be created: (same as " + OVAL + " file)";
}
