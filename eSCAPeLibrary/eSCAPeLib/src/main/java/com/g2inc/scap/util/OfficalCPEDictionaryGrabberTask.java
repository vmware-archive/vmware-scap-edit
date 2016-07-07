package com.g2inc.scap.util;
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
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class OfficalCPEDictionaryGrabberTask
{
	private static final String CONTENT_DIR="data_feeds";
	private static final String CONTENT_URL="http://static.nvd.nist.gov/feeds/xml/cpe/dictionary/official-cpe-dictionary_v2.2.xml";
	/**
	 * Attempt to grab the latest offical CPE dictionary file and store it locally
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		OfficalCPEDictionaryGrabberTask task = new OfficalCPEDictionaryGrabberTask();
		
		task.work(args);
	}
	
	private void work(String [] args) throws Exception
	{		
		String dictLocation = CONTENT_URL;
				
		String cpeContentDirName = CONTENT_DIR;
		
		String fn = dictLocation.substring(dictLocation.lastIndexOf("/") + 1);
		File destFile = new File(cpeContentDirName + File.separator + fn);
		
		URL url = new URL(dictLocation);
		
		URLConnection conn = url.openConnection();
		
		conn.connect();
		
		long lmodifiedRemote = conn.getLastModified();
		
		boolean needToDownload = false;
		
		if(destFile.exists())
		{
			System.out.println(destFile.getAbsolutePath() + " exists, check modification time");
			long lmodifiedLocal = destFile.lastModified();
			
			if(lmodifiedRemote > lmodifiedLocal)
			{
				System.out.println("Server file is newer, need to download");
				needToDownload = true;
			}
			else
			{
				System.out.println("Local version is newer, no need to download");
			}
		}
		else
		{
			System.out.println("Local version doesn't exist, need to download");
			needToDownload = true;
		}
		
		if(needToDownload)
		{
			// the file on the server is newer, download the update file
			InputStream is = conn.getInputStream();
			FileOutputStream fos = new FileOutputStream(destFile);
			
			byte[] buff = new byte[8192];
			int read = 0;
			
			while((read = is.read(buff)) > 0)
			{
				fos.write(buff, 0, read);
			}
			
			fos.flush();
			fos.close();
			
			is.close();
		}
		
	}
}
