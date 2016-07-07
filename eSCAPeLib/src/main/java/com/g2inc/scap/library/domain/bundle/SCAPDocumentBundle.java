package com.g2inc.scap.library.domain.bundle;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
//import com.g2inc.scap.library.domain.ocil.OcilDocumentImpl;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.util.CommonUtil;
//import com.g2inc.scap.model.ocil.OcilDocument;

/*
 * Represents a set of SCAP documents.  The primary reason for a document bundle
 * is group together SCAP documents that reference each other.  For example,
 * a bundle might contain an XCCDF Benchmark, a CPE dictionary, a CPE OVAL document,
 * and an OVAL document containing the checks referenced by the XCCDF Benchmark.
 *
 * Having these documents grouped together allows us to perform reference checking,
 * schema validation, and any other syntactical and symantical checks we want.
 *
 * Renaming of items in one of the documents can be done safely since we can find all
 * places that reference that item and change them as well.
 */
public class SCAPDocumentBundle
{
    private static final Logger LOG = Logger.getLogger(SCAPDocumentBundle.class);
    private String filename = null; // only set if this was loaded as a zip file.
    private SCAPBundleType bundleType = null;
    private List<BundledDocumentHandle> bundleDocs = new ArrayList<BundledDocumentHandle>();
    private BundleParseResults parseResults = new BundleParseResults();
    private BundleValidator bundleValidator = null;
    private boolean readOnly = false;
    private boolean dirty = false;
    /**
     * Stores the locator prefix we found when loading this bundle of documents.
     */
    private String locatorPrefix = null;

    /**
     * Parse a bundle and return it.
     *
     * @param filename String path to a directory or zip file containing related SCAP documents.
     * @return SCAPDocumentBundle
     * @throws IOException
     */
    public static SCAPDocumentBundle parseBundle(String filename) throws IOException, Exception
    {
        if (filename == null || filename.trim().length() == 0)
        {
            throw new IllegalArgumentException("A zip file or directory must be specified!");
        }

        File fileOrDir = new File(filename);

        return parseBundle(fileOrDir);
    }

    /**
     * Parse a bundle and return it.
     *
     * @param fileOrDir A File object representing a directory or zip file containing related SCAP documents.
     * @return SCAPDocumentBundle
     * @throws IOException
     */
    public static SCAPDocumentBundle parseBundle(File fileOrDir) throws IOException, Exception
    {
        SCAPDocumentBundle bundle = null;

        if (fileOrDir.exists())
        {
            if (fileOrDir.isDirectory())
            {
                throw new IllegalArgumentException("Must be either a .zip file or an xccdf benchmark document");
            } else
            {
                String tmpFN = fileOrDir.getName();

                if (tmpFN != null && tmpFN.trim().length() > 0)
                {
                    tmpFN = tmpFN.toLowerCase().trim();

                    if (tmpFN.toLowerCase().endsWith(".zip"))
                    {
                        bundle = parseZipBundle(fileOrDir);
                    } else if (tmpFN.toLowerCase().endsWith("xccdf.xml"))
                    {
                        bundle = parseStreamBundle(fileOrDir);
                    } else
                    {
                        throw new IllegalArgumentException("The supplied file is not a .zip file or an xccdf document: " + fileOrDir.getAbsolutePath());
                    }
                } else
                {
                    throw new IllegalArgumentException("The supplied filename is empty!");
                }
            }
        } else
        {
            throw new IllegalArgumentException("The supplied file or directory doesn't exist: " + fileOrDir.getAbsolutePath());
        }

        return bundle;
    }

    /**
     * New bundle to be populated based on the contents of a zip file.  It's assumed that the zip file
     * contains the documents for only a single SCAP bundle of documents.  The locator prefix portion
     * of the first xccdf document in the zip will be used to load the rest of the related documents.
     * 
     * @param file A File object pointing to the zip file to be loaded.
     * @return SCAPDocumentBundle
     * @throws IOException
     * @throws Exception
     */
    private static SCAPDocumentBundle parseZipBundle(File file) throws IOException, Exception
    {
        int loaded = 0;

        if (!file.exists())
        {
            throw new IllegalArgumentException("zip file doesn't exist: " + file.getAbsolutePath());
        }

        SCAPDocumentBundle bundle = new SCAPDocumentBundle();
        bundle.setBundleType(SCAPBundleType.ZIP);
        bundle.setFilename(file.getAbsolutePath());

        if (!file.canWrite())
        {
            // the zip file is not writable and will need to be saved somewhere else when it's
            // saved.
            bundle.setReadOnly(true);
        }

        ZipFile zf = new ZipFile(file);

        String prefix = null;

        Enumeration zipEnum = zf.entries();
        while (zipEnum.hasMoreElements())
        {
            ZipEntry zipEntry = (ZipEntry) zipEnum.nextElement();

            String name = zipEntry.getName();
            String lcName = name.toLowerCase();

            if (lcName.endsWith("xccdf.xml"))
            {
                prefix = name.substring(0, lcName.indexOf("xccdf.xml"));
                break;
            }
        }

        if (prefix == null)
        {
            LOG.error("Zip file doesn't contain an XCCDF benchmark document: " + file.getAbsolutePath());
            return null;
        }

        // calling it this way because this is the parse of the bundle, not setting a new value of the prefix
        bundle.locatorPrefix = prefix;

        zipEnum = zf.entries();
        while (zipEnum.hasMoreElements())
        {
            ZipEntry zipEntry = (ZipEntry) zipEnum.nextElement();

            String name = zipEntry.getName();

            if (name.startsWith(prefix))
            {
                SCAPDocument sdoc = SCAPDocumentFactory.loadDocument(zf.getInputStream(zipEntry));

                if (sdoc != null)
                {
                    loaded++;
                    sdoc.setFilename(name);
                    bundle.addDocument(sdoc);
                }
            }
        }

        LOG.info("SCAP Data Stream: " + file.getAbsolutePath());
        LOG.info("SCAP Data Stream: documents loaded = " + loaded);

        return bundle;
    }

    /**
     * New bundle to be populated based on the xccdf file in that bundle.  The locator prefix portion 
     * of the xccdf filename will be used to load any other related documents.
     * 
     * @param xccdfFile The File object pointing to the xccdf document.
     * @return SCAPDocumentBundle
     * @throws IOException
     * @throws Exception
     */
    private static SCAPDocumentBundle parseStreamBundle(File xccdfFile) throws IOException, Exception
    {
        int loaded = 0;

        SCAPDocumentBundle bundle = new SCAPDocumentBundle();
        bundle.setBundleType(SCAPBundleType.STREAM);

        File parent = xccdfFile.getAbsoluteFile().getParentFile();
        bundle.setFilename(parent.getAbsolutePath());

        String tmpFN = xccdfFile.getName();

        int loc = tmpFN.toLowerCase().indexOf("xccdf.xml");

        if (loc == -1)
        {
            throw new IllegalArgumentException("File supplied is not an xccdf document.");
        }

        final String prefix = tmpFN.substring(0, loc);

        bundle.setLocatorPrefix(prefix);

        // look for all files in xccdfFile's directory that have the prefix

        FilenameFilter fnf = new FilenameFilter()
        {

            public boolean accept(File dir, String name)
            {
                if (name != null && name.startsWith(prefix))
                {
                    return true;
                } else
                {
                    return false;
                }
            }
        };

        File parentDir = xccdfFile.getAbsoluteFile().getParentFile();

        File[] files = parentDir.listFiles(fnf);

        if (files != null && files.length > 0)
        {
            for (int x = 0; x < files.length; x++)
            {
                File f = files[x];

                SCAPDocument sd = null;

                sd = SCAPDocumentFactory.loadDocument(f);

                if (sd != null)
                {
                    sd.setFilename(f.getAbsolutePath());
                    loaded++;
                    bundle.addDocument(sd);
                }
            }
        }

        LOG.info("SCAP Data Stream: " + xccdfFile.getAbsolutePath());
        LOG.info("SCAP Data Stream: documents loaded = " + loaded);

        return bundle;
    }

    private BundleComponentType determineSubtype(String filename)
    {
        BundleComponentType ret = null;

        String lcFilename = filename.toLowerCase();

        if (lcFilename.endsWith("cpe-oval.xml"))
        {
            // CPE Inventory
            ret = BundleComponentType.CPE_INVENTORY_OVAL;
        }
        else if (lcFilename.indexOf("cpe-dictionary") > -1)
        {
            ret = BundleComponentType.CPE_DICTIONARY;
        }
        else if (lcFilename.endsWith("oval.xml"))
        {
            // OVAL Compliance
            ret = BundleComponentType.COMPLIANCE_OVAL;
        }
        else if (lcFilename.endsWith("patches.xml"))
        {
            // Oval Patch
            ret = BundleComponentType.PATCHES_OVAL;
        }
        else if (lcFilename.endsWith("ocil.xml"))
        {
            // OCIL questions
            ret = BundleComponentType.OCIL_QUESTIONNAIRE;
        }
        else if (lcFilename.endsWith("ocil.xml"))
        {
            // OCIL questions
            ret = BundleComponentType.OCIL_QUESTIONNAIRE;
        }
        else if (lcFilename.endsWith("xccdf.xml"))
        {
            // XCCDF Benchmark
            ret = BundleComponentType.XCCDF_BENCHMARK;
        }
        else
        {
            LOG.error("Uknown bundle component, filename = " + filename);
            ret = BundleComponentType.UNKNOWN;
        }

        return ret;
    }

    /**
     * Get this bundle's oval documents, of any type.
     * 
     * @return List<OvalDefinitionsDocument>
     */
    public List<OvalDefinitionsDocument> getOvalDocs()
    {
        List<OvalDefinitionsDocument> docs = new ArrayList<OvalDefinitionsDocument>();

        for (int x = 0; x < bundleDocs.size(); x++)
        {
            BundledDocumentHandle handle = bundleDocs.get(x);

            if (handle instanceof BundledOvalDocumentHandle)
            {
                BundledOvalDocumentHandle oHandle = (BundledOvalDocumentHandle) handle;
                docs.add(oHandle.getDocument());
            }
        }

        return docs;
    }

    /**
     * Get this bundle's ocil documents
     * 
     * @return List<OcilDocument>
     */
   /* public List<OcilDocument> getOcilDocs()
    {
        List<OcilDocument> docs = new ArrayList<OcilDocument>();

        for (int x = 0; x < bundleDocs.size(); x++)
        {
            BundledDocumentHandle handle = bundleDocs.get(x);

            if (handle instanceof BundledOcilDocumentHandle)
            {
                BundledOcilDocumentHandle oHandle = (BundledOcilDocumentHandle) handle;
                docs.add(oHandle.getDocument());
            }
        }

        return docs;
    }*/
    
    /**
     * Get this bundle's oval documents, specifically those containing
     * definitions that are used to back entries in the CPE dictionary.
     *
     * @return List<OvalDefinitionsDocument>
     */
    public List<OvalDefinitionsDocument> getCPEOvalDocs()
    {
        List<OvalDefinitionsDocument> docs = new ArrayList<OvalDefinitionsDocument>();

        for (int x = 0; x < bundleDocs.size(); x++)
        {
            BundledDocumentHandle handle = bundleDocs.get(x);

            if (handle instanceof BundledOvalDocumentHandle)
            {
                BundledOvalDocumentHandle oHandle = (BundledOvalDocumentHandle) handle;

                if (oHandle.getSubType().equals(BundleComponentType.CPE_INVENTORY_OVAL))
                {
                    docs.add(oHandle.getDocument());
                }
            }
        }

        return docs;
    }

    /**
     * Get this bundle's CPE Dictionary documents
     *
     * @return List<CPEDictionaryDocument>
     */
    public List<CPEDictionaryDocument> getCPEDictionaryDocs()
    {
        List<CPEDictionaryDocument> docs = new ArrayList<CPEDictionaryDocument>();

        for (int x = 0; x < bundleDocs.size(); x++)
        {
            BundledDocumentHandle handle = bundleDocs.get(x);

            if (handle instanceof BundledCPEDocumentHandle)
            {
                BundledCPEDocumentHandle cpeDictHandle = (BundledCPEDocumentHandle) handle;

                docs.add(cpeDictHandle.getDocument());
            }
        }

        return docs;
    }

    /**
     * Get this bundle's XCCDFBenchmark documents
     *
     * @return List<XCCDFBenchmark>
     */
    public List<XCCDFBenchmark> getXCCDFBenchmarks()
    {
        List<XCCDFBenchmark> docs = new ArrayList<XCCDFBenchmark>();

        for (int x = 0; x < bundleDocs.size(); x++)
        {
            BundledDocumentHandle handle = bundleDocs.get(x);

            if (handle instanceof BundledXCCDFDocumentHandle)
            {
                BundledXCCDFDocumentHandle benchmarkHandle = (BundledXCCDFDocumentHandle) handle;

                docs.add((XCCDFBenchmark) benchmarkHandle.getDocument());
            }
        }

        return docs;
    }

    /**
     * Get this bundle's oval documents, specifically those containing
     * definitions that are used to check for patches.
     *
     * @return List<OvalDefinitionsDocument>
     */
    public List<OvalDefinitionsDocument> getPatchOvalDocs()
    {
        List<OvalDefinitionsDocument> docs = new ArrayList<OvalDefinitionsDocument>();

        for (int x = 0; x < bundleDocs.size(); x++)
        {
            BundledDocumentHandle handle = bundleDocs.get(x);

            if (handle instanceof BundledOvalDocumentHandle)
            {
                BundledOvalDocumentHandle oHandle = (BundledOvalDocumentHandle) handle;

                if (oHandle.getSubType().equals(BundleComponentType.PATCHES_OVAL))
                {
                    docs.add(oHandle.getDocument());
                }
            }
        }

        return docs;
    }

    /**
     * Get this bundle's oval documents, specifically those containing
     * definitions that are used to check compliance.
     *
     * @return List<OvalDefinitionsDocument>
     */
    public List<OvalDefinitionsDocument> getComplianceOvalDocs()
    {
        List<OvalDefinitionsDocument> docs = new ArrayList<OvalDefinitionsDocument>();

        for (int x = 0; x < bundleDocs.size(); x++)
        {
            BundledDocumentHandle handle = bundleDocs.get(x);

            if (handle instanceof BundledOvalDocumentHandle)
            {
                BundledOvalDocumentHandle oHandle = (BundledOvalDocumentHandle) handle;

                if (oHandle.getSubType().equals(BundleComponentType.COMPLIANCE_OVAL))
                {
                    docs.add(oHandle.getDocument());
                }
            }
        }

        return docs;
    }

    /**
     * Should be called when you are done with a bundle you have loaded.
     * This removes the document references from SCAPContentManager.
     * Also clears the parse results object.
     * Also clears the filename.
     */
    public void closeBundle()
    {
        if(bundleDocs != null)
        {
            for (int x = 0; x < bundleDocs.size(); x++)
            {
                BundledDocumentHandle handle = bundleDocs.get(x);

                handle.unloadDocument();
            }

            bundleDocs.clear();
            bundleDocs = null;
        }

        if(parseResults != null)
        {
            parseResults.clear();
        }
    }

    /**
     * This method can be called after the bundle is parsed in order to see if
     * all documents required were loaded.
     * 
     * If there were any problems, parse results will say which files had errors.
     * 
     * @return boolean
     */
    public boolean hasLoadErrors()
    {
        boolean ret = false;

        List<ParseResult> rList = parseResults.getResults();

        if (rList != null)
        {
            for (int x = 0; x < rList.size(); x++)
            {
                ParseResult pr = rList.get(x);

                if (pr.getResult() != ResultTypeEnum.SUCCESS)
                {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    public String validate() throws Exception
    {
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        // first loop through docs and run a schema validation
        if (bundleDocs != null && bundleDocs.size() > 0)
        {
            String filename = null;

            for (int x = 0; x < bundleDocs.size(); x++)
            {
                BundledDocumentHandle doc = bundleDocs.get(x);

                SCAPDocument sdoc = (SCAPDocument) doc.getDocument();
                filename = sdoc.getFilename();

                String result = null;

                try
                {
                    result = sdoc.validate();
                    if (result != null && result.length() > 0)
                    {
                        // failure
                        sb.append("File: " + sdoc.getFilename() + newLine + result);
                    }
                } catch (Exception e)
                {
                    sb.append("File: " + filename + newLine + CommonUtil.stringifyStacktrace(e));
                }
            }

            // Loop through and validate documents symantically, for instance
            // checking that xccdf rules referring to non-existent oval checks
            // are flagged.
            for (int docIdx = 0; docIdx < bundleDocs.size(); docIdx++)
            {
                boolean fileAppended = false;

                BundledDocumentHandle handle = bundleDocs.get(docIdx);

                if (handle instanceof BundledXCCDFDocumentHandle)
                {
                    BundledXCCDFDocumentHandle xccdfHandle = (BundledXCCDFDocumentHandle) handle;

                    XCCDFBenchmark benchmark = (XCCDFBenchmark) xccdfHandle.getDocument();
                    String result = benchmark.validateSymantically();

                    if (result != null && result.trim().length() > 0)
                    {
                        sb.append("File: " + benchmark.getFilename() + newLine);
                        sb.append(result + newLine);
                    }
                } // if the document handle is for an oval document
                else if (handle instanceof BundledOvalDocumentHandle)
                {
                    BundledOvalDocumentHandle ovalHandle = (BundledOvalDocumentHandle) handle;

                    OvalDefinitionsDocument odd = ovalHandle.getDocument();

                    String result = odd.validateSymantically();

                    if (result != null && result.trim().length() > 0)
                    {
                        sb.append("File: " + odd.getFilename() + newLine);
                        sb.append(result + newLine);
                    }
                } // if the document handle is for a cpe dictionary document
                else if (handle instanceof BundledCPEDocumentHandle)
                {
                    BundledCPEDocumentHandle cpeHandle = (BundledCPEDocumentHandle) handle;

                    CPEDictionaryDocument cdd = cpeHandle.getDocument();

                    String result = cdd.validateSymantically();

                    if (result != null && result.trim().length() > 0)
                    {
                        sb.append("File: " + cdd.getFilename() + newLine);
                        sb.append(result + newLine);
                    }
                }
            }
        }

        return sb.toString();
    }

    /**
     * Return an oval document from this bundle where the filename(not path) matches the
     * supplied one.
     * 
     * @param name
     * @return OvalDefinitionsDocument
     */
    public OvalDefinitionsDocument getOvalDocumentByName(String name)
    {
        OvalDefinitionsDocument ret = null;

        if (bundleDocs != null && bundleDocs.size() > 0)
        {
            for (int x = 0; x < bundleDocs.size(); x++)
            {
                BundledDocumentHandle doc = bundleDocs.get(x);

                if (!(doc instanceof BundledOvalDocumentHandle))
                {
                    continue;
                }

                SCAPDocument sdoc = (SCAPDocument) doc.getDocument();

                File f = new File(sdoc.getFilename());

                if (name.equals(f.getName()))
                {
                 //   LOG.debug("Comparing supplied name " + name + " with " + f.getName());
                    if (name.equals(f.getName()))
                    {
                        ret = (OvalDefinitionsDocument) sdoc;
                        break;
                    }
                }
            }
        }

        return ret;
    }

    /**
     * Check if a document is part of this bundle.
     * 
     * @param scapDocument An existing SCAPDocument
     * 
     * @return boolean
     */
    public boolean containsDocument(SCAPDocument scapDocument)
    {
        if (bundleDocs == null || bundleDocs.size() == 0)
        {
            return false;
        }

        for (int x = 0; x < bundleDocs.size(); x++)
        {
            BundledDocumentHandle docHandle = bundleDocs.get(x);

            SCAPDocument sd = (SCAPDocument) docHandle.getDocument();

            if (sd == scapDocument)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Remove the document from the bundle.  Also removes the file on disk.
     * 
     * @param sdoc
     */
    public void removeDocument(SCAPDocument sdoc) throws IOException
    {
        if (!containsDocument(sdoc))
        {
            return;
        } else
        {
            if (bundleDocs != null && bundleDocs.size() > 0)
            {
                for (int x = 0; x < bundleDocs.size(); x++)
                {
                    BundledDocumentHandle bdh = bundleDocs.get(x);

                    SCAPDocument bDoc = (SCAPDocument) bdh.getDocument();

                    if (bDoc == sdoc)
                    {
                        // this is the document we want to remove

                        // remove the document from the scap content manager
                        SCAPContentManager.getInstance().removeDocument(sdoc.getFilename());

                        // remove the handle from the collection of handles
                        bundleDocs.remove(bdh);

                        if (getBundleType() == SCAPBundleType.STREAM)
                        {
                            // try to remove the file on disk
                            File sdocFile = new File(sdoc.getFilename());

                            try
                            {
                                if (!sdocFile.delete())
                                {
                                    throw new IllegalStateException("Trying to remove file " + sdocFile.getAbsolutePath() + " returned false");
                                }
                            } catch (Exception e)
                            {
                                LOG.error("Exception occurred trying to remove file " + sdocFile.getAbsolutePath(), e);
                            }
                        }

                        save();
                    }
                }
            }
        }
    }

    /**
     * Add a document to this bundle.  The document being added will be copied to this
     * bundle's directory.
     * 
     * @param sdoc
     */
    public void addDocument(SCAPDocument sdoc) throws IOException
    {
        if (containsDocument(sdoc))
        {
            LOG.error("Bundle already contains SCAPDocument " + sdoc.getFilename());
            return;
        }

        BundledDocumentHandle docHandle = createDocumentHandle(sdoc);

        sdoc.setBundle(this);

        bundleDocs.add(docHandle);
    }

    private BundledDocumentHandle createDocumentHandle(SCAPDocument sdoc)
    {
        BundledDocumentHandle ret = null;
        String fn = sdoc.getFilename();
        BundleComponentType bType = determineSubtype(fn);

        if (bType == BundleComponentType.XCCDF_BENCHMARK)
        {
            ret = new BundledXCCDFDocumentHandle((XCCDFBenchmark) sdoc, this);
        }
        else if (bType == BundleComponentType.CPE_DICTIONARY)
        {
            ret = new BundledCPEDocumentHandle((CPEDictionaryDocument) sdoc, this);
        }
        else if (bType == BundleComponentType.CPE_INVENTORY_OVAL)
        {
            OvalDefinitionsDocument odd = (OvalDefinitionsDocument) sdoc;
            ret = new BundledOvalDocumentHandle(odd, this);
            ((BundledOvalDocumentHandle) ret).setSubType(bType);
        }
        else if (bType == BundleComponentType.COMPLIANCE_OVAL)
        {
            OvalDefinitionsDocument odd = (OvalDefinitionsDocument) sdoc;
            ret = new BundledOvalDocumentHandle(odd, this);
            ((BundledOvalDocumentHandle) ret).setSubType(bType);
        }
        else if (bType == BundleComponentType.PATCHES_OVAL)
        {
            OvalDefinitionsDocument odd = (OvalDefinitionsDocument) sdoc;
            ret = new BundledOvalDocumentHandle(odd, this);
            ((BundledOvalDocumentHandle) ret).setSubType(bType);
        }
        /*else if (bType == BundleComponentType.OCIL_QUESTIONNAIRE)
        {
        	OcilDocumentImpl ocilDoc = (OcilDocumentImpl) sdoc;
        	ret = new BundledOcilDocumentHandle(ocilDoc, this);
        }*/
        else
        {
            throw new IllegalStateException("Don't know how to create a handle for a document type " + sdoc.getDocumentType());
        }

        return ret;
    }

    /**
     * Save any child documents.
     * 
     * @throws IOException
     */
    public void save() throws IOException
    {
        // save with the existing filename
        if (filename != null)
        {
            saveAs((String) filename);
        } else
        {
            saveAs((String) null);
        }
    }

    /**
     * Save the document bundle and then any child documents.
     * 
     * @param fname Name of of file to save as
     * @throws IOException
     */
    public void saveAs(String fname) throws IOException
    {
        File file = null;

        if (fname != null)
        {
            file = new File(fname);
        }

        saveAs(file);
    }

	public void saveAsZipFile(File zipFile) throws IOException
	{
		saveZipChildren(zipFile);
	}

    /**
     * Save the document bundle xml as a particular name, and then save any child documents.
     * 
     * @param file
     * @throws IOException
     */
    public void saveAs(File file) throws IOException
    {
        if (bundleType == SCAPBundleType.ZIP)
        {
            if (file != null)
            {
                saveZipChildren(file);
            } else
            {
                // the type is zip but no filename is set
                throw new IllegalStateException("The type is zip but no filename has been set!");
            }
        } else
        {
            // we are saving documents out as individual files
            saveChildren();
        }
    }

    /**
     * Create the bundle zip file and create entries in it for each related document.
     * 
     * @param file A File object representing the zip we are about to create.
     * @throws IOException 
     */
    private void saveZipChildren(File file) throws IOException
    {
        if (isReadOnly())
        {
            throw new IllegalStateException("This bundle was loaded from a read-only zip file."
                    + " You must use saveAs to save it in a non read-only location");
        } else
        {
            FileOutputStream fout = new FileOutputStream(file);
            ZipOutputStream zout = new ZipOutputStream(fout);

            for (int x = 0; x < bundleDocs.size(); x++)
            {
                BundledDocumentHandle docHandle = bundleDocs.get(x);

                SCAPDocument sdoc = (SCAPDocument) docHandle.getDocument();
                Document dom = sdoc.getDoc();

                ZipEntry entry = new ZipEntry(sdoc.getFilename());

                zout.putNextEntry(entry);

                XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
                xmlo.output(dom, zout);
            }

            zout.close();
        }

    }

    /**
     * Save the documents that are part of this bundle as separate files.
     * 
     * @throws IOException
     */
    private void saveChildren() throws IOException
    {
        if (bundleDocs == null || bundleDocs.size() == 0)
        {
            return;
        }

        for (int x = 0; x < bundleDocs.size(); x++)
        {
            BundledDocumentHandle docHandle = bundleDocs.get(x);

            SCAPDocument sd = (SCAPDocument) docHandle.getDocument();
            sd.save();
        }
    }

    /**
     * Get the instance of the class who provides validation for this bundle.
     * 
     * @return BundleValidator
     */
    public BundleValidator getBundleValidator()
    {
        return bundleValidator;
    }

    /**
     * Get the type of bundle this is.
     *
     * @return SCAPBundleType
     */
    public SCAPBundleType getBundleType()
    {
        return bundleType;
    }

    /**
     * Set the type of bundle this is.
     *
     * @param bundleType
     */
    public void setBundleType(SCAPBundleType bundleType)
    {
        this.bundleType = bundleType;
    }

    /**
     * Returns whether the file this bundle was loaded from is read only.
     *
     * @return boolean
     */
    public boolean isReadOnly()
    {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    /**
     * Get the filename/directory this bundle was loaded from.
     * When loaded from a zip file, this will be a filename, else
     * it will be a directory.
     *
     * @return String
     */
    public String getFilename()
    {
        return filename;
    }

    /**
     * Set the filename/directory this bundle will use.
     * When the bundle type is zip, this will be a filename, else
     * it will be a directory.
     *
     * @param filename
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    /**
     * Get the prefix currently being used.
     *
     * @return String
     */
    public String getLocatorPrefix()
    {
        return locatorPrefix;
    }

    /**
     * Set the locator prefix of this bundle.  This will rename any documents already in the bundle
     * with the new prefix.
     *
     * @param prefix The new prefix to be set
     */
    public void setLocatorPrefix(String prefix) throws IOException
    {
        // rename component documents
        for (int x = 0; x < bundleDocs.size(); x++)
        {
            BundledDocumentHandle docHandle = bundleDocs.get(x);

            if (bundleType == SCAPBundleType.ZIP)
            {
                String docFN = docHandle.getDocument().getFilename();

                docHandle.rename(docFN.replaceAll(this.locatorPrefix, prefix));
            } else
            {
                File tmpF = new File(docHandle.getDocument().getFilename()).getAbsoluteFile();
                File parent = tmpF.getParentFile();
                String tmpFN = tmpF.getName().replaceAll(this.locatorPrefix, prefix);

                tmpF = new File(parent.getAbsolutePath() + File.separator + tmpFN);
                docHandle.rename(tmpF.getAbsolutePath());
            }
        }

        this.locatorPrefix = prefix;
    }

    public boolean isDirty()
    {
        return dirty;
    }

    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
    }

}
