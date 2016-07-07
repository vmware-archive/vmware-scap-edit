package com.g2inc.scap.library.content.style;

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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Namespace;

import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.oval.Criteria;
import com.g2inc.scap.library.domain.oval.CriteriaChild;
import com.g2inc.scap.library.domain.oval.ExtendDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.schema.SchemaLocator;

/**
 * An an abstract class implementing the DocumentStyle interface.
 * Any methods or instance variables common to all styles should be implemented here.
 *
 * @author ssill2
 */
public abstract class ContentStyleAbstract implements ContentStyle
{
    private Map<String, String> styleSettings = new HashMap<String, String>();
    private String styleName = null;

    public static final String NON_EMPTY_PATTERN_STRING = "^(?i).+$";
    public static ContentStylePattern NON_EMPTY_PATTERN = null;
    {
        NON_EMPTY_PATTERN = new ContentStylePattern(Pattern.compile(NON_EMPTY_PATTERN_STRING),"Example valid input: Any non-empty string");
    }

    public static final String ANY_VALID_OVAL_DEF_ID_PATTERN_STRING = "^(?i)oval:[^:]+:def:\\d+$";
    public static ContentStylePattern ANY_VALID_OVAL_DEF_ID_PATTERN = null;
    {
    	ANY_VALID_OVAL_DEF_ID_PATTERN = new ContentStylePattern(Pattern.compile(ANY_VALID_OVAL_DEF_ID_PATTERN_STRING),
    			"Example valid definition id: oval:org.mitre:def:1234");
    }

    @Override
    public String getStyleName()
    {
        return styleName;
    }

    @Override
    public void setStyleName(String name)
    {
        this.styleName = name;
    }

    @Override
    public String getProperty(String key)
    {
        return styleSettings.get(key);
    }

    @Override
    public void setProperty(String key, String value)
    {
        styleSettings.put(key, value);
    }

    /**
     * Check that the oval document has a base id set and if not, return a violation.
     * 
     * @param ovalDoc
     * @return ContentStyleViolationElement
     */
    public ContentStyleViolationElement hasEmptyBaseId(OvalDefinitionsDocument ovalDoc)
    {
        String baseId = ovalDoc.getBaseId();

        if(baseId == null || baseId.trim().length() == 0)
        {
            return new ContentStyleViolationElement(ContentStyleViolationLevel.WARN, "No document default namespace has been set.");
        }

        return null;
    }

    /**
     * Given a supplied pattern and a string, check if it matches
     *
     * @param pattern The pattern to check against
     * @param text The string to check against
     *
     * @return boolean
     */
    public boolean matchesPattern(ContentStylePattern pattern, String text)
    {
        boolean ret = false;

        if(text == null)
        {
            text = "";
        }
        
        Matcher m = pattern.getPattern().matcher(text);

        ret = m.matches();

        return ret;
    }

    /**
     * Handle check for null of document filename and that it meets the required pattern.
     * 
     */
    public void handleDocFilename(SCAPDocument document, List<ContentStyleViolationElement> currentViolations, ContentStylePattern pattern)
    {
        String docFilename = document.getFilename();

        //
        // Check for things about the filename
        if(docFilename == null)
        {
        	currentViolations.add(new ContentStyleViolationElement(ContentStyleViolationLevel.WARN, "Document filename is null!"));
        }
        else
        {
            File f = new File(docFilename);

            if(!matchesPattern(pattern,f.getName()))
            {
            	currentViolations.add(new ContentStyleViolationElement(ContentStyleViolationLevel.WARN, "Filename isn't valid: " + pattern.getFriendlyExplanation()));
            }
        }
    }
    
    @Override
    public String toString()
    {
        return getStyleName();
    }
    
    protected abstract List<ContentStyleViolationElement> handleCPE(CPEDictionaryDocument doc);
    protected abstract List<ContentStyleViolationElement> handleXCCDF(XCCDFBenchmark doc);
    protected abstract List<ContentStyleViolationElement> handleOval(OvalDefinitionsDocument doc);

    @Override
    public List<ContentStyleViolationElement> checkDocument(SCAPDocument doc)
    {
        List<ContentStyleViolationElement> ret = null;

        SCAPDocumentTypeEnum docType = doc.getDocumentType();

        switch(docType)
        {
            case OVAL_53:
            case OVAL_54:
            case OVAL_55:
            case OVAL_56:
            case OVAL_57:
            case OVAL_58:
            case OVAL_59:
            case OVAL_510:
                ret = handleOval((OvalDefinitionsDocument) doc);
                break;
            case XCCDF_114:
            case XCCDF_12:
                ret = handleXCCDF((XCCDFBenchmark) doc);
                break;
            case CPE_20:
            case CPE_21:
            case CPE_22:
                ret = handleCPE((CPEDictionaryDocument) doc);
                break;
        }

        return ret;
    }
    
    
    /**
     * Warn if the benchmark has a style attribute defined.  Some older SCAP tools generate errors if the content has this attribute.  New SCAP standards require this attribute
     * so this method will eventually be deprecated.
     * 
     * @param currentViolations
     * @param benchmark
     */
    public void handleXCCDFStyleAttributeWarning(List<ContentStyleViolationElement> currentViolations, XCCDFBenchmark benchmark)
    {
        String style = benchmark.getStyle();
        if (style != null)
        {
            currentViolations.add(new ContentStyleViolationElement(ContentStyleViolationLevel.WARN, "Benchmark has a style attribute may prevent some tools from processing it.  Please set the attribute to an empty string to remove it."));
        }
    }

    
    /**
     * Warn if the schemaLocation attribute contains xsd entries for xhtml and xmldsig since many SCAP tools do not ship with these schemas and will throw
     * errors if they are explicitly referenced in the schemaLocation attribute.
     * 
     * @param currentViolations
     * @param benchmark
     */
    public void handleXHTMLAndXMLDSIGEntries(List<ContentStyleViolationElement> currentViolations, XCCDFBenchmark benchmark)
    {
        // check for xhtml and xmldsig entries in the schemalocation attribute.
        String schemaLocation = benchmark.getElement().getAttributeValue("schemaLocation", Namespace.getNamespace("xsi", SchemaLocator.XML_SCHEMA_NAMESPACE));

        if (schemaLocation != null)
        {
            // check for xhtml
            if (schemaLocation.indexOf(SchemaLocator.XHTML_NAMESPACE) > -1)
            {
                currentViolations.add(new ContentStyleViolationElement(ContentStyleViolationLevel.WARN, "The schemaLocation"
                                                                                                        + " attribute of the Benchmark contains an entry for " + SchemaLocator.XHTML_NAMESPACE
                                                                                                        + " which could prevent validation from occuring in some editors and tools. Save the document"
                                                                                                        + " to remove it."));
            }

            // check for xmldsig
            if (schemaLocation.indexOf(SchemaLocator.XMLDSIG_NAMESPACE) > -1)
            {
                currentViolations.add(new ContentStyleViolationElement(ContentStyleViolationLevel.WARN, "The schemaLocation"
                                                                                                        + " attribute of the Benchmark contains an entry for " + SchemaLocator.XMLDSIG_NAMESPACE
                                                                                                        + " which could prevent validation from occuring in some editors and tools. Save the document"
                                                                                                        + " to remove it."));
            }
        }
    }

    /**
     * Ensure that the comment on an extend definition element match the title of definition being referred to.
     * 
     * @param defId
     * @param currentViolations
     * @param defCriteriaChild
     */
    protected void checkExtendDefComments(String defId, List<ContentStyleViolationElement> currentViolations, CriteriaChild defCriteriaChild)
    {
        if (defCriteriaChild instanceof Criteria)
        {
            Criteria c = (Criteria) defCriteriaChild;

            List<CriteriaChild> children = c.getChildren();

            if (children != null && children.size() > 0)
            {
                for (CriteriaChild cc : children)
                {
                    checkExtendDefComments(defId, currentViolations, cc);
                }
            }
        }
        else if (defCriteriaChild instanceof ExtendDefinition)
        {
            ExtendDefinition ed = (ExtendDefinition) defCriteriaChild;
            String edDefId = ed.getDefinitionId();

            String edComment = ed.getComment();

            if (edComment != null && edComment.trim().length() > 0)
            {
                OvalDefinition referencedDef = ed.getParentDocument().getOvalDefinition(edDefId);

                if (referencedDef != null)
                {
                    String refDefTitle = referencedDef.getMetadata().getTitle();
                    if (!edComment.trim().equals((refDefTitle != null ? refDefTitle.trim() : "")))
                    {
                        currentViolations.add(new ContentStyleViolationElement(ContentStyleViolationLevel.WARN,
                                                                               "Vulnerability definition (" + defId + ") extend_definition comment does not equal"
                                                                               + " the title of the definition it references: " + referencedDef.getId()));
                    }
                }
                else
                {
                    currentViolations.add(new ContentStyleViolationElement(ContentStyleViolationLevel.WARN,
                                                                           "Vulnerability definition (" + defId + ") extend_definition references null definition: " + edDefId));
                }
            }
            else
            {
                currentViolations.add(new ContentStyleViolationElement(ContentStyleViolationLevel.WARN,
                                                                       "Vulnerability definition (" + defId + ") extend_definition(" + edDefId + ") has no comment set"));
            }
        }
    }
    
    @Override
    public boolean equals(Object other) {
    	boolean result = false;
    	if (other instanceof ContentStyle) {
    		ContentStyle otherStyle = (ContentStyle) other;
    		result = (styleName != null && styleName.equals(otherStyle.getStyleName()));
    	}
    	return result;
    }
    
    @Override
    public int hashCode() {
    	return 31 + (styleName == null ? 0 : styleName.hashCode());
    }
}
