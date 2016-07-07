package com.g2inc.scap.util;
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

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.cpe.CPEItem;
import com.g2inc.scap.library.domain.cpe.CPEItemCheck;
import com.g2inc.scap.library.domain.cpe.CPEItemCheckSystemType;
import com.g2inc.scap.library.domain.oval.MergeStats;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;

public class CreateOfficalCPEDictionaryOvalTask
{
	private static final String CONTENT_DIR = "data_feeds";
	private static final String OVAL_FILE = "official-cpe-oval.xml";
	private static final Logger LOG = Logger.getLogger(CreateOfficalCPEDictionaryOvalTask.class);
	
	/**
	 * Attempt to grab the latest offical CPE dictionary file and store it locally
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
//		File configFile = new File("src/test/resources/log4j.xml");
//		if (configFile.exists()) {
//			DOMConfigurator.configure(configFile.getAbsolutePath());
//			LOG.debug("Log4j configured using " + configFile.getAbsolutePath());
//		} else {
//			System.out.println("Log4j config file not found: " + configFile.getAbsolutePath());
//		}
		CreateOfficalCPEDictionaryOvalTask task = new CreateOfficalCPEDictionaryOvalTask();
		
		task.work(args);
	}
	
	private void work(String [] args) throws Exception
	{
		String contentDir = CONTENT_DIR;
		
		File contentDirHandle = new File(contentDir);
		
		FilenameFilter ffilter = new FilenameFilter()
		{	
			@Override
			public boolean accept(File dir, String name)
			{
				String lcFn = name.toLowerCase();
				
				if(lcFn.startsWith("official-cpe-dictionary") && lcFn.endsWith(".xml"))
				{
					return true;
				}						
				
				return false;
			}
		};

		File[] dictFiles = contentDirHandle.listFiles(ffilter);
		
		if(dictFiles == null || dictFiles.length == 0)
		{
			throw new RuntimeException("official cpe dictionary file not found!");
		}
		
		
		String dictFilename = dictFiles[0].getAbsolutePath();
		String ovalFilename = dictFiles[0].getAbsoluteFile().getParentFile().getAbsolutePath() +
			File.separator + OVAL_FILE;
				
		File dictFile = new File(dictFilename);
		File ovalFile = new File(ovalFilename);
		
		// we won't save the cpe file at the end unless it's been updated
		boolean dictChanged = false;
		
		// cpe dictionary we are reading from
		CPEDictionaryDocument cpeDict = (CPEDictionaryDocument) SCAPDocumentFactory.loadDocument(dictFile);
		if(cpeDict == null)
		{
			throw new IllegalStateException("CPEDictionary could not be loaded!");		
		}
//		cpeDict.setFilename(dictFile.getAbsolutePath());
		
		// this is the oval file we'll be putting definitions etc into.
	
		OvalDefinitionsDocument cpeOval = (OvalDefinitionsDocument) SCAPDocumentFactory.createNewDocument(SCAPDocumentTypeEnum.OVAL_511);

		cpeOval.setFilename(ovalFile.getAbsolutePath());

		int itemsProcessed = 0;
		int itemsWithChecks = 0;		
		
		List<CPEItem> items = cpeDict.getItems();
		
		// Create the MergeStats object.
		MergeStats stats = new MergeStats();
		
		for(int x = 0; x < items.size(); x++)
		{
			CPEItem item = items.get(x);
			
			// this has now been processed
			itemsProcessed++;
			
			List<CPEItemCheck> checks = item.getChecks();
			
			if(checks == null)
			{
				continue;
			}
			
			for(int y = 0; y < checks.size(); y++)
			{
				CPEItemCheck check = checks.get(y);
				
				if(check.getSystem() != null && check.getSystem().equals(CPEItemCheckSystemType.OVAL5))
				{
					// we care about this one
					String href = check.getHref();
					
					if(href.indexOf(":") == -1)
					{
						// this href has already been processed, skip it.
						continue;
					}

					System.out.println("Downloading content for cpe item " + item.getName() + "(" + itemsProcessed + ")");
					
					dictChanged = true;
					
					// he has a check we care about so count him
					itemsWithChecks++;
					
					URL url = new URL(href);
					
					URLConnection conn = url.openConnection();
					
					conn.connect();
					
					InputStream is = conn.getInputStream();
					
				//	LOG.debug("loading oval definitions document from URL " + href);
					OvalDefinitionsDocument odd = (OvalDefinitionsDocument) SCAPDocumentFactory.loadDocument(is);
			//		LOG.debug("oval definitions document loaded");
					
					if(odd != null)
					{
						cpeOval.merge(odd, stats);
					}
					
					odd = null;
					
					check.setHref(ovalFile.getName());
				}
			}
		}
		
		if(dictChanged)
		{
			System.out.println("Items processed = " + itemsProcessed);
			System.out.println("Items with checks = " + itemsWithChecks);

			cpeDict.save();
			cpeOval.save();
		}
		else
		{
			System.out.println("Files have already been processed, no changes needed.");
		}
		
	}
}
