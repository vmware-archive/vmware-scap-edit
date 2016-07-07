package com.g2inc.scap.editor.gui.model.tree.bundle;
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

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.g2inc.scap.editor.gui.resources.EditorMessages;
import com.g2inc.scap.library.domain.bundle.SCAPDocumentBundle;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

public class SCAPBundleTreeModel extends DefaultTreeModel
{

    private static final long serialVersionUID = 1L;
    public static final String NODE_BUNDLE = EditorMessages.SCAP + " Bundle";
    public static final String NODE_DOCUMENTS = "Documents";

    private static final Logger log = Logger.getLogger(SCAPBundleTreeModel.class);
    private SCAPDocumentBundle doc = null;
    private String filterString = null;

    public SCAPBundleTreeModel(SCAPDocumentBundle doc, String filterString)
    {
        super(new DefaultMutableTreeNode("No Document Loaded"));

        this.filterString = filterString;

        if (doc != null)
        {
            this.doc = doc;

            DefaultMutableTreeNode ourRoot = (DefaultMutableTreeNode) getRoot();
            ourRoot.setUserObject(doc);

            DefaultMutableTreeNode documentsNode = new DefaultMutableTreeNode(NODE_DOCUMENTS);

            ourRoot.add(documentsNode);

            // populate nodes for each document that is part of this bundle
            List<XCCDFBenchmark> benchmarks = doc.getXCCDFBenchmarks();

            if(benchmarks != null && benchmarks.size() > 0)
            {
                DefaultMutableTreeNode benchmarksNode = new DefaultMutableTreeNode(EditorMessages.XCCDF + " Benchmarks");
                documentsNode.add(benchmarksNode);

                for(int x = 0; x < benchmarks.size(); x++)
                {
                    XCCDFBenchmark bm = benchmarks.get(x);

                    DefaultMutableTreeNode bmNode = new DefaultMutableTreeNode(bm);
                    benchmarksNode.add(bmNode);
                }
            }

            // populate nodes for each document that is part of this bundle
            List<CPEDictionaryDocument> cpeDicts = doc.getCPEDictionaryDocs();

            if(cpeDicts != null && cpeDicts.size() > 0)
            {
                DefaultMutableTreeNode cpeDictNode = new DefaultMutableTreeNode(EditorMessages.CPE + " Dictionaries");
                documentsNode.add(cpeDictNode);

                for(int x = 0; x < cpeDicts.size(); x++)
                {
                    CPEDictionaryDocument dict = cpeDicts.get(x);

                    DefaultMutableTreeNode dictNode = new DefaultMutableTreeNode(dict);
                    cpeDictNode.add(dictNode);
                }
            }

            // populate nodes for each document that is part of this bundle
            List<OvalDefinitionsDocument> ovalDocs = doc.getOvalDocs();

            if(ovalDocs != null && ovalDocs.size() > 0)
            {
                DefaultMutableTreeNode ovalDocsNode = new DefaultMutableTreeNode(EditorMessages.OVAL + " Documents");
                documentsNode.add(ovalDocsNode);

                for(int x = 0; x < ovalDocs.size(); x++)
                {
                    OvalDefinitionsDocument dict = ovalDocs.get(x);

                    DefaultMutableTreeNode dictNode = new DefaultMutableTreeNode(dict);
                    ovalDocsNode.add(dictNode);

                }
            }
        }
    }

    @Override
    public Object getRoot()
    {
        return root;
    }

    public String getFilterString()
    {
        return filterString;
    }

    public void setFilterString(String filterString)
    {
        this.filterString = filterString;

    }
}
