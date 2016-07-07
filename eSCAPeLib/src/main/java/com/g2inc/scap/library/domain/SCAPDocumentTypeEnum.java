package com.g2inc.scap.library.domain;
/* Copyright (c) 2016 - 2016. VMware, Inc. All rights reserved.
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
 * Enum representing the possible types of documents
 */


public enum SCAPDocumentTypeEnum {

    SCAP_12_DATA_STREAM("http://scap.nist.gov/schema/scap/1.2/", "scap-source-data-stream_1.2.xsd", "1.2"),
    OVAL_511("http://oval.mitre.org/language/version5.11/ovaldefinition/complete/", "oval-common-schema.xsd", "5.11"),
    OVAL_60("Not implemented", "Not implemented", "6.0"),
    UNKNOWN("UNKNOWN", "UKNOWN", "UNKNOWN"),
    CPE_20("http://cpe.mitre.org/files/", "cpe-dictionary_2.0.xsd", "2.0"),
    CPE_21("http://cpe.mitre.org/files/", "cpe-dictionary_2.1.xsd", "2.1"),
    CPE_22("http://cpe.mitre.org/files/", "cpe-dictionary_2.2.xsd", "2.2"),
    CPE_23("http://cpe.mitre.org/files/", "cpe-dictionary_2.3.xsd", "2.3"),
    OCIL_2("http://nvd.nist.gov/schema/", "ocil.xsd", "2.0");
	
    /**
     * The base url of where to get this schema over the internet.
     */
	
    private String baseUrl;
    /**
     * The filename that is added to the baseUrl, e.g. cpe-dictionary_2.2.xsd
     */
    private String primarySchemaFile;
    /**
     * Private constructor that takes the deprecated schema path.
     *
     * @param baseUrl The base url of where to get this schema over the
     * internet.
     * @param schemaFile The filename that is added to the baseUrl, e.g.
     * cpe-dictionary_2.2.xsd.
     *
     */
    private String displayName;

    private SCAPDocumentTypeEnum(String baseUrl, String schemaFile, String displayName) {
        this.baseUrl = baseUrl;
        this.primarySchemaFile = schemaFile;
        this.displayName = displayName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getPrimarySchemaFile() {
        return primarySchemaFile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SCAPDocumentTypeEnum valueOfDisplayName(String name) {
        name = name.trim();
        for (SCAPDocumentTypeEnum docEnum : SCAPDocumentTypeEnum.values()) {
            if (docEnum.getDisplayName().equals(name)) {
                return docEnum;
            }
        }
        return null;
    }
}
