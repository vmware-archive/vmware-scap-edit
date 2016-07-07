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

//import gov.nist.scap.validation.core.ScapContentValidator;
//import gov.nist.scap.validation.core.ScapVersion;
//import gov.nist.scap.validation.core.UseCase;
//import gov.nist.scap.validation.core.ValidationInfo;
//import gov.nist.scap.validation.core.ValidationMessage;
//import gov.nist.scap.validation.core.ValidationMessageListener;
//import gov.nist.scap.validation.core.impl.ScapContentValidatorBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.g2inc.scap.library.domain.LibraryConfiguration;
import com.g2inc.scap.library.domain.SCAPValidationResult;
import com.g2inc.scap.library.util.CommonUtil;

public class BundleValidator implements Runnable
{
	
    @Override
    public void run()
    {
        validate(useCase);
    }
    private static final Logger LOG = Logger.getLogger(BundleValidator.class);
    private SCAPDocumentBundle bundle;
    private UseCase useCase;

    public BundleValidator(SCAPDocumentBundle bundle, UseCase useCase)
    {
        this.bundle = bundle;
        this.useCase = useCase;
    }

    public SCAPValidationResult validate(UseCase useCase)
    {
        LOG.debug("Bundle validation started - usecase " + useCase.toString());
        SCAPValidationResult result = new SCAPValidationResult();
        ArrayList<String> errorList = new ArrayList<String>();
        result.setValidationMessageList(errorList);
        File zipFile = null;
        String tempDirName = System.getProperty("java.io.tmpdir");
        File tempDir = new File(tempDirName);
        try
        {
            if (bundle.getBundleType() != SCAPBundleType.ZIP)
            {
                zipFile = new File(tempDir, "tempBundle.zip");
                bundle.saveAsZipFile(zipFile);
            }
            else
            {
                File srcZipFile = new File(bundle.getFilename());
                zipFile = new File(tempDir, srcZipFile.getName());
                
                CommonUtil.copyFile(srcZipFile, zipFile);
            }
        }
        catch (IOException e)
        {
            String errorMessage = "Validation could not create zip file " + zipFile.getAbsolutePath();
            LOG.error(errorMessage, e);
            addToCompletion(result,
                    errorMessage,
                    null);
            return result;
        }

        File scapvalDir = LibraryConfiguration.getInstance().getScapValDir();
    	if (!scapvalDir.exists() || !scapvalDir.isDirectory()) {
    		String errorMessage ="Could not find scapval directory " + scapvalDir.getAbsolutePath();
            LOG.error(errorMessage);
            addToCompletion(result,
                    errorMessage,
                    null);
            return result;
    	}
    	LOG.debug("Preparing to validate; scapval directory: " + scapvalDir.getAbsolutePath());
        String scapvalJar = getScapvalJar(scapvalDir);
        if (scapvalJar == null) {
    		String errorMessage ="Could not find scapval jar in directory " + scapvalDir.getAbsolutePath();
            LOG.error(errorMessage);
            addToCompletion(result,
                    errorMessage,
                    null);
            return result;
        }
        LOG.debug("Preparing to validate; scapval jar file: " + scapvalJar);
        File scapvalHtml = null;
        File scapvalStdout = null;
        try {
			scapvalHtml = File.createTempFile("scapvalResult", ".html");
			result.setHtmlFile(scapvalHtml);
			scapvalHtml.deleteOnExit();
			scapvalStdout = File.createTempFile("scapvalStdout", ".txt");
			scapvalStdout.deleteOnExit();
			result.setScapvalStdout(scapvalStdout);
		} catch (IOException e1) {
    		String errorMessage ="Could not create temp files for html results and/or scapval stdout " + scapvalDir.getAbsolutePath();
            LOG.error(errorMessage);
            addToCompletion(result,
                    errorMessage,
                    null);
            return result;
		}
        
        List<String> command = new ArrayList<String>();
        command.add("java.exe");
        command.add("-jar");
        command.add(scapvalJar);
        command.add("-file");
        command.add(zipFile.getAbsolutePath());
        command.add("-usecase");
        command.add(useCase.toString());
        if (LibraryConfiguration.getInstance().isOnline()) {
        	command.add("-online");
        }
        command.add("-result");
        command.add(scapvalHtml.getAbsolutePath());
        command.add("-scapversion");
        command.add("1.0");
        
        if (LOG.isDebugEnabled()) {
        	LOG.debug("scapval command:");
        	showCommand(command);
        }
                
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(scapvalDir);
        builder.redirectErrorStream(true);
        LOG.debug("Starting scapval process");
        Process process = null;
        try {
			process = builder.start();
			LOG.debug("Started scapval process");
		} catch (IOException e1) {
    		String errorMessage = "IO error starting scapval process";
            LOG.error(errorMessage, e1);
            addToCompletion(result,
                    errorMessage + e1.getMessage(),
                    null);
            return result;
		}
		LOG.debug("Instantiating and starting stdout/stderr capture thread");
        StreamGobbler gobbler = new StreamGobbler(process.getInputStream(), scapvalStdout);
        gobbler.start();
        
        LOG.debug("Waiting for scapval process to complete");
        int exitVal = -1;
        try {
			exitVal = process.waitFor();
		} catch (InterruptedException e1) {

		}
        LOG.debug("scapval process completed, return code: " + exitVal);        
        return result;
    }

    private void addToCompletion(SCAPValidationResult result, String message, Exception e)
    {
        StringBuilder sb = new StringBuilder();
        if (result.getValidationCompletion() != null)
        {
            sb.append(result.getValidationCompletion());
            sb.append("\n");
        }
        sb.append(message);
        result.setValidationCompletion(sb.toString());
        if (e != null)
        {
            LOG.error(message, e);
        } else
        {
            LOG.error(message);
        }
    }
    
    private String getScapvalJar(File scapvalDir) {
    	File scapvalLibDir = new File(scapvalDir, "lib");
    	if (!scapvalLibDir.exists() || !scapvalLibDir.isDirectory()) {
    		LOG.error("Could not find scapval lib directory " + scapvalLibDir.getAbsolutePath());
    		return null;
    	}
    	String[] scapvalLibJars = scapvalLibDir.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return (name.startsWith("scapval") && name.endsWith(".jar"));
			}
    		
    	});
    	if (scapvalLibJars == null || scapvalLibJars.length == 0) {
    		return null;
    	}
    	if (scapvalLibJars.length > 1) {
    		LOG.warn("Using first of " + scapvalLibJars.length 
    				+ " candidates for scapval jar from directory " 
    				+ scapvalLibDir.getAbsolutePath());
    	}
    	return "lib" + File.separator + scapvalLibJars[0];
    }
    
    private void showCommand(List<String> command) {
    	StringBuilder sb = new StringBuilder();
    	for (String str : command) {
    		sb.append(str);
    		sb.append(" ");
    	}
    	LOG.debug(sb.toString());
    }
    
    class StreamGobbler extends Thread {
    	InputStream is;
    	File outFile;
    	
    	StreamGobbler(InputStream is, File outFile) {
    		this.is = is;
    		this.outFile = outFile;
    	}
    	
    	public void run() {
    		try {
    			InputStreamReader isr = new InputStreamReader(is);
    			BufferedReader br = new BufferedReader(isr);
    			PrintWriter pw = new PrintWriter(outFile);
    			String line = null;
    			while ((line = br.readLine()) != null) {
    				pw.println(line);
    			}
    			pw.flush();
    			pw.close();
    			br.close();
    		} catch (IOException e) {
    			LOG.error("Error copying scapval output stream to file: " + outFile.getAbsolutePath(),e);
    		}
    	}
    }

//    class ScapMessageListener implements ValidationMessageListener
//    {
//
//        List<ValidationMessage> messageList;
//
//        public ScapMessageListener(List<ValidationMessage> messageList)
//        {
//            this.messageList = messageList;
//        }
//
//        @Override
//        /**
//         * add ValidationMessage to list IF it is a REAL validation message.
//         * As of now, all log4j messages pass through the onMessage method,
//         * not just those representing problems with the bundle. Checking
//         * for a non-null requirement id filters out the extraneous messages.
//         */
//        public void onMessage(ValidationMessage message)
//        {
//            if (message.getRequirementId() != null)
//            {
//                messageList.add(message);
//            }
//        }
//    }
}
