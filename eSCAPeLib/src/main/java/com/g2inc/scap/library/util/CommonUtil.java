package com.g2inc.scap.library.util;
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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.bundle.SCAPBundleType;
import com.g2inc.scap.library.domain.bundle.SCAPDocumentBundle;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.cpe.CPEItem;
import com.g2inc.scap.library.domain.cpe.CPEItemCheck;
import com.g2inc.scap.library.domain.cpe.CPEItemCheckSystemType;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalElement;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

/**
 * This class contains frequently used static utility methods.
 */
public class CommonUtil
{

    private static final Logger LOG = Logger.getLogger(CommonUtil.class);
    public static final String DOC_WRAPPER_START =
                               "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                               + "<DocWrapper xmlns=\"http://checklists.nist.gov/xccdf/1.1\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:dc=\"http://purl.org/dc/elements/1.1\" >\n";
    public static final String DOC_WRAPPER_END =
                               "</DocWrapper>";

    /**
     * Set up the VM to use the system proxy settings if they are present.
     */
    public static void establishProxySettings()
    {
        System.setProperty("java.net.useSystemProxies", "true");
        List<Proxy> pList = null;

        try
        {
            pList = ProxySelector.getDefault().select(new URI("http://www.yahoo.com"));
        }
        catch (URISyntaxException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (pList != null && pList.size() > 0)
        {
            Proxy p = pList.get(0);
            InetSocketAddress addr = (InetSocketAddress) p.address();
            if (addr != null)
            {
                System.setProperty("proxySet", "true");
                System.setProperty("http.proxyHost", addr.getHostName());
                System.setProperty("http.proxyPort", addr.getPort() + "");
            }
        }
    }

    /**
     * Return a dot separated list of element names representing the path to the given element in
     * the hierarchy.
     *
     * @param currentElement The element for which we want to produce the path
     * @param path This StringBuilder will be updated with the path to the current element
     */
    public static void getElementPath(Element currentElement, StringBuilder path)
    {
        if (!currentElement.isRootElement())
        {
            path.append("." + currentElement.getName());

            getElementPath(currentElement.getParentElement(), path);

        }
    }

    /**
     * Replaces all whitespace with underscores.
     *
     * @param in The string to be sanitized.
     *
     * @return String
     */
    public static String sanitize(String in)
    {
        return in.replaceAll("\\W", "_");
    }

    /**
     * Return the supplied string so that it's valid as an NCname if possible.
     *
     * @param dirtyString The dirty string
     * @return String
     */
    public static String toNCName(String dirtyString)
    {
    	//TODO: allow non-english letters or digits, not just A-Z, a-z, and 0-9
    	// idea: use isLetter and isDigit instead of regexes
        String cleanString = dirtyString;

        cleanString = cleanString.replaceAll("\\:", "");
        cleanString = cleanString.replaceAll("\\W+", "_");
        cleanString = cleanString.replaceAll("[^a-zA-Z0-9_\\-]", "");
        cleanString = cleanString.replaceAll("\\.", "");
        char firstChar = cleanString.charAt(0);
        if (Character.isDigit(firstChar)) {
        	cleanString = "X" + cleanString;
        }
      //  LOG.debug("toNCName returning " + cleanString);
        return cleanString;
    }

    /**
     * TODO: fill in this comment.
     *
     * @param element The element
     *
     * @return String
     */
    public static String getXmlString(Element element)
    {
        String result;
        if (element.getChildren().size() == 0)
        {
            result = element.getText();
        }
        else
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter osw = null;
            BufferedWriter bw = null;
            try
            {
                osw = new OutputStreamWriter(baos, Charset.forName("UTF-8"));
                bw = new BufferedWriter(osw);

                XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
                xmlo.outputElementContent(element, bw);

                bw.flush();
                bw.close();
                osw.close();
                baos.close();
                result = baos.toString("UTF-8");
            }
            catch (Exception e)
            {
                throw new IllegalStateException("Error creating Element content string for element " + element.getName(), e);
            }
        }
        return result;
    }

    /**
     * TODO: fill in this comment.
     *
     * @param tagName The name of the tag
     * @param namespace The namespace
     * @param string The string
     *
     * @return String
     */
    public static Element getElementFromXmlString(String tagName, Namespace namespace, String string)
    {
        // string (last parameter) may contain embedded xml
        StringBuilder sb = new StringBuilder();
        sb.append(DOC_WRAPPER_START);
        sb.append("<" + tagName + ">");
        sb.append(string);
        sb.append("</" + tagName + ">\n");
        sb.append(DOC_WRAPPER_END);
        SAXBuilder builder = new SAXBuilder();
        Document doc;
        ByteArrayInputStream bais;
        try
        {
            bais = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
            doc = builder.build(bais);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Can't parse tagName " + tagName + ", string:\n" + string, e);
        }
        Element wrapperRoot = doc.getRootElement();
        Element element = (Element) wrapperRoot.getChild(tagName, namespace).detach();
        return element;
    }

    /**
     * Trim string and convert all multi whitespace regions to a single space.
     *
     * @param dirtyString String potentially containing lots of excess whitespace.
     *
     * @return String
     */
    public static String normalizeWhitespace(String dirtyString)
    {
        if (dirtyString == null)
        {
            return null;
        }

        if (dirtyString.length() < 1)
        {
            return dirtyString;
        }

        StringBuffer buff = new StringBuffer();
        int dsl = dirtyString.length();
        buff.ensureCapacity(dsl);

        boolean lastWasWhitespace = false;
        for (int x = 0; x < dsl; x++)
        {
            char currChar = dirtyString.charAt(x);

            switch (currChar)
            {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    if (!lastWasWhitespace)
                    {
                        lastWasWhitespace = true;
                        buff.append(' ');
                    }
                    break;
                default:
                    lastWasWhitespace = false;
                    buff.append(currChar);
            }
        } // end of for loop

        return buff.toString().trim();
    }

    /**
     * Return a string representing an exception and all of it's nested exceptions.
     *
     * @param t The exception or throwable we want to produce a string for
     *
     * @return String
     */
    public static String stringifyStacktrace(Throwable t)
    {
        return stringifyStacktrace(t, "");
    }

    private static String stringifyStacktrace(Throwable t, String indent)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(indent + t.getMessage() + "\n");
        StackTraceElement[] stackTrace = t.getStackTrace();

        for (int x = 0; x < stackTrace.length; x++)
        {
            sb.append(indent + "   " + stackTrace[x].toString() + "\n");
        }

        if (t.getCause() != null)
        {
            sb.append(indent + "Caused by:\n");
            sb.append(stringifyStacktrace(t.getCause(), indent + "   "));
        }

        return sb.toString();
    }

    /**
     * Sanitize a string for use in XML.
     *
     * @param aText The text we want to sanitize
     *
     * @return String
     */
    public static String escapeXML(String aText)
    {
        final StringBuilder result = new StringBuilder();
        final StringCharacterIterator iterator = new StringCharacterIterator(aText);
        char character = iterator.current();
        while (character != CharacterIterator.DONE)
        {
            switch (character)
            {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '\"':
                    result.append("&quot;");
                    break;
                case '\'':
                    result.append("&#039;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                default:
                    result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }

    /**
     * Copy a file.
     *
     * @param src The file we want to copy
     * @param dst The file we want src copied as
     *
     * @throws IOException
     */
    public static void copyFile(File src, File dst) throws IOException
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;

        fis = new FileInputStream(src);
        fos = new FileOutputStream(dst);

        byte[] buffer = new byte[16384];
        int read = 0;

        while ((read = fis.read(buffer)) != -1)
        {
            fos.write(buffer, 0, read);
        }

        fis.close();
        fos.flush();
        fos.close();
    }

    /**
     * Recursively delete a directory.
     *
     * @param fileOrDir The file or directory to be deleted
     *
     * @throws IOException
     */
    public static void recursiveDelete(File fileOrDir) throws IOException
    {
        if (fileOrDir.isDirectory())
        {
            File[] contents = fileOrDir.listFiles();

            if (contents != null && contents.length > 0)
            {
                for (int x = 0; x < contents.length; x++)
                {
                    File entry = contents[x];

                    recursiveDelete(entry);
                }
            }
        }

        if (!fileOrDir.delete())
        {
            throw new IllegalStateException(fileOrDir.getAbsolutePath() + " delete returned false!");
        }
    }

    /**
     * This method will build a cpe dictionary and accompanying oval that contains only the cpes referenced by the given
     * benchmark.
     *
     * @param benchmark The benchmark for which you want to generate a cpe dictionary
     * @return SCAPDocumentBundle
     */
    public static SCAPDocumentBundle buildCPEDictionaryAndOval(XCCDFBenchmark benchmark) throws IOException
    {
        SCAPDocumentBundle destBundle = null;
        CPEDictionaryDocument destDict = null;
        OvalDefinitionsDocument destOval = null;

        String bmFilename = benchmark.getFilename();

        if (bmFilename == null || bmFilename.length() == 0)
        {
            throw new IllegalStateException("Benchmark supplied has a null or empty filename!");
        }
        String ext = "xccdf.xml";

        File bmFile = new File(bmFilename).getAbsoluteFile();
        String bmFilenameOnly = bmFile.getName();
        String lcBmFilenameOnly = bmFilenameOnly.toLowerCase();

        if (!lcBmFilenameOnly.endsWith(ext))
        {
            throw new IllegalStateException("Benchmark filename doesn't end with xccdf.xml, it must to be 800-126 compliant");
        }

        int extLoc = lcBmFilenameOnly.indexOf(ext);

        String prefix = bmFilenameOnly.substring(0, extLoc);

        File cpeDictFile = new File(bmFile.getParentFile().getAbsolutePath() + File.separator + prefix + "cpe-dictionary.xml");
        File cpeOvalFile = new File(bmFile.getParentFile().getAbsolutePath() + File.separator + prefix + "cpe-oval.xml");

        // load the official cpeDictionary
        SCAPDocumentBundle offCPEBundle = SCAPContentManager.getInstance().getOffcialCPEDictionary();

        if (offCPEBundle == null)
        {
            throw new IllegalStateException("Offical CPE dictionary could not be loaded!");
        }

        // we assume only one of each type of document
        CPEDictionaryDocument offCPEDict = offCPEBundle.getCPEDictionaryDocs().get(0);
        OvalDefinitionsDocument offCPEOval = offCPEBundle.getCPEOvalDocs().get(0);

        destDict = (CPEDictionaryDocument) SCAPDocumentFactory.createNewDocument(offCPEDict.getDocumentType());
        destOval = (OvalDefinitionsDocument) SCAPDocumentFactory.createNewDocument(offCPEOval.getDocumentType());
        destOval.setBaseId("prefix");

        destBundle = new SCAPDocumentBundle();
        destBundle.setBundleType(SCAPBundleType.STREAM);

        // we are storing the files out on the filesystem, so use the full path
        destDict.setFilename(cpeDictFile.getAbsolutePath());
        destOval.setFilename(cpeOvalFile.getAbsolutePath());
        destBundle.setFilename(bmFilename);

        destBundle.addDocument(destDict);
        destBundle.addDocument(destOval);

        Set<String> referencedCPEs = benchmark.getAllReferencedCPES();

        if (referencedCPEs != null && referencedCPEs.size() > 0)
        {
            Iterator<String> rcItr = referencedCPEs.iterator();
            while (rcItr.hasNext())
            {
                String cpeId = rcItr.next();

                long startTime = System.nanoTime();

                List<CPEItem> existingOfficialItems = offCPEDict.getItem(cpeId);

                long endTime = System.nanoTime();

                if (LOG.isDebugEnabled())
                {
                    long timeTakenNanos = endTime - startTime;
                    double timeTakenSeconds = timeTakenNanos / 1000000000L;
                 //   LOG.debug("getItem for " + cpeId + " took " + timeTakenSeconds + " Sec");
                }

                if (existingOfficialItems != null && existingOfficialItems.size() > 0)
                {
                    // take the first one
                    CPEItem officialItem = existingOfficialItems.get(0);


                    boolean hasCheckcontent = false;


                    // find out if there an oval that needs to come over as well
                    List<CPEItemCheck> officialChecks = officialItem.getChecks();

                    if (officialChecks != null && officialChecks.size() > 0)
                    {
                        for (int x = 0; x < officialChecks.size(); x++)
                        {
                            CPEItemCheck officialCheck = officialChecks.get(x);

                            if (officialCheck.getSystem() != null && officialCheck.getSystem().equals(CPEItemCheckSystemType.OVAL5))
                            {
                                // there is an oval check that needs to come over
                                String defId = officialCheck.getCheckId();

                                hasCheckcontent = true;

                                OvalDefinition officialDef = offCPEOval.getOvalDefinition(defId);
                                List<OvalElement> deps = offCPEOval.findDependenciesFor(officialDef);

                                // add any dependencies(including officialDef) to the destination oval
                                if (deps != null && deps.size() > 0)
                                {
                                    for (int i = 0; i < deps.size(); i++)
                                    {
                                        destOval.add(deps.get(i));
                                    }
                                }
                            }
                        }
                    }

                    if (hasCheckcontent)
                    {
                        // add the entry from the offical dictionary to the new dictionary
                        // for this xccdf
                        destDict.addItem(officialItem);

                        // get the cpe item we added from the destination dictionary
                        List<CPEItem> itemsForName = destDict.getItem(officialItem.getName());
                        CPEItem destItem = null;

                        if(itemsForName.size() > 1)
                        {
                            for(CPEItem litem : itemsForName)
                            {
                                if(!litem.getDeprecated())
                                {
                                    destItem = litem;
                                    break;
                                }
                            }
                        }
                        else
                        {
                            destItem = itemsForName.get(0);
                        }
                        
                        // find out if there an oval that needs to come over as well
                        List<CPEItemCheck> destItemChecks = destItem.getChecks();

                        if (destItemChecks != null && destItemChecks.size() > 0)
                        {
                            for (int x = 0; x < destItemChecks.size(); x++)
                            {
                                CPEItemCheck destCheck = destItemChecks.get(x);

                                if (destCheck.getSystem() != null && destCheck.getSystem().equals(CPEItemCheckSystemType.OVAL5))
                                {
                                    // there is an oval check that needs to come over
                                    String defId = destCheck.getCheckId();

                                    destCheck.setHref(cpeOvalFile.getName());
//                                    // update the href in the destination item to be the new
//                                    // cpe oval file, not the official one
//                                    destItem.updateCheckHref(destCheck.getHref(), cpeOvalFile.getName());
                                }
                            }
                        }
                    }
                }
            }
        }

        destDict.save();
        destOval.save();

        return destBundle;
    }

    /**
     * Return the namespace portion of an oval id,
     *
     * For a definition id of oval:dibtf.tar-u:def:4
     *
     * This function would return dibtf.tar-u
     *
     * @param ovalId
     *
     * @return String
     */
    public static String getNamespaceFromOvalId(String ovalId)
    {
        String ret = null;

        String prefix = "oval:";
        int loc = ovalId.indexOf(prefix);

        if (loc > -1)
        {
            String tmpString = ovalId.substring(0 + prefix.length());

            // get the location of the next colon
            loc = tmpString.indexOf(":");

            if (loc > -1)
            {
                ret = tmpString.substring(0, loc);
            }
        }

        return ret;
    }

    /**
     * Return a recommended xccdf filename based on an oval filename.
     * For an oval file named mycontent-oval.xml
     * The name suggested would be mycontent-xccdf.xml
     *
     * @param ovalFilename
     *
     * @return String
     */
    public static String getSuggestedXCCDFFilenameForOvalFilename(String ovalFilename)
    {
        String ret = null;

        if(ovalFilename == null)
        {
            return ret;
        }
    	File ovalFile = new File(ovalFilename);
    	String filename = ovalFile.getName();
    	File dir = ovalFile.getParentFile();

        String suffix = ".xml";

        String lcFilename = filename.toLowerCase();
        if(lcFilename.endsWith("patches.xml"))
        {
            suffix = "patches.xml";
        }
        else if(lcFilename.endsWith("cpe-oval.xml"))
        {
            suffix = "cpe-oval.xml";
        }
        else if(lcFilename.endsWith("oval.xml"))
        {
            suffix = "oval.xml";
        }
        
        int loc = filename.toLowerCase().lastIndexOf(suffix);

        if (loc > -1)
        {
            // an XCCDF Benchmark id must be an NCName; if the oval file
            // name begins with a digit it is not a valid NCName. So we
            // will pre-pend an alphabetic character in this case, so 
            // the file name will match the benchmark id.
            String prefix = "";
            char firstChar = filename.charAt(0);
            if (Character.isDigit(firstChar)) {
            	prefix = "X";
            }
            String xccdfFilename = prefix + filename.substring(0, loc) + "xccdf.xml";
            ret = new File(dir, xccdfFilename).getAbsolutePath();
        }
        else
        {
            return null;
        }

        return ret;
    }


    /**
     * Return a recommended cpe dictionary filename based on an oval filename.
     * 
     * For an oval file named mycontent-oval.xml
     *
     * The name suggested would be mycontent-cpe-dictionary.xml
     *
     * @param ovalFilename
     *
     * @return String
     */
    public static String getSuggestedCPEDictionaryFilenameForOvalFilename(String ovalFilename)
    {
        String ret = null;
        String suffix = "oval.xml";

        int loc = ovalFilename.toLowerCase().lastIndexOf(suffix);

        if (loc > -1)
        {
            String tmpString = ovalFilename.substring(0, loc);

            ret = tmpString + "cpe-dictionary.xml";
        }
        else
        {
            return null;
        }

        return ret;
    }

    /**
     * Return a recommended cpe oval filename based on an oval filename.
     * 
     * For an oval file named mycontent-oval.xml
     *
     * The name suggested would be mycontent-cpe-oval.xml
     *
     * @param ovalFilename
     *
     * @return String
     */
    public static String getSuggestedCPEOvalFilenameForOvalFilename(String ovalFilename)
    {
        String ret = null;
        String suffix = "oval.xml";

        int loc = ovalFilename.toLowerCase().lastIndexOf(suffix);

        if (loc > -1)
        {
            String tmpString = ovalFilename.substring(0, loc);

            ret = tmpString + "cpe-oval.xml";
        }
        else
        {
            return null;
        }

        return ret;
    }

    /**
     * This method was useful during debugging to see the current state of the DefaultMutableTreeNode
     * tree, and compare it with the state of the Element tree. It is not currently
     * used, but I might need it again sometime.
     *
     * @param node     current DefaultMutableTreeNode
     * @param indent   current indentation level
     */
    public static void showNode(DefaultMutableTreeNode node, String indent)
    {
        LOG.info(indent + node.getUserObject().toString());
        int childCount = node.getChildCount();
        indent += "    ";
        for (int i = 0; i < childCount; i++)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            showNode(child, indent);
        }
    }

    /**
     * This method was useful during debugging to see the current state of the Element tree,
     * and compare it with the state of the DefaultMutableTreeNode tree. It is not currently
     * used, but I might need it again sometime.
     *
     * @param element  current JDom Element
     * @param indent   current indentation level
     */
    public static void showElement(Element element, String indent)
    {
      //  LOG.debug(indent + element.getName());
        List<Element> children = element.getChildren();
        indent += "    ";
        for (Element child : children)
        {
            showElement(child, indent);
        }
    }
    
    public static String removeXhtmlPrefixes(String withPrefixes)
    {
        return withPrefixes.replaceAll("xhtml:", "");
    }
    
    public static String truncateWithEllipsis(String str, int maxLength) {
    	StringBuilder sb = new StringBuilder();
    	if (str != null) {
    		sb.append(str);	
    		if (str.length() > maxLength) {
    			sb.setLength(maxLength);
    			if (maxLength > 3) {
    				sb.replace(maxLength-3, maxLength, "...");
    			}
    		}
    	}
    	return sb.toString();
    }
}
