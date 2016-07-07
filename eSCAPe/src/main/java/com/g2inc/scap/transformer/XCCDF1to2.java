package com.g2inc.scap.transformer;
/* Copyright (c) 2016 - 2016. VMware, Inc. All rights reserved.
* 
* This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License version 3.0 
* as published by the FreeSoftware Foundation This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License version 3.0 
* for more details. You should have received a copy of the GNU General Public License version 3.0 along with this program; if not, write to
* the Free Software Foundation, Inc., 675 Mass Avenue, Cambridge, MA 02139, USA.
*/

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XCCDF1to2 {
	public static void xccdf12(File out, String reverse, File xsltfile, File xmlfile) throws IOException, URISyntaxException, TransformerException {
	//public static void main(String[] args) throws IOException, URISyntaxException, TransformerException {

        //TransformerFactory factory = TransformerFactory.newInstance();
       // File xmlfile = new File("temp.xml"); 
		//File xsltfile = new File("xccdf_1.1_to_1.2.xsl");
		/*System.out.println(xsltfile.getAbsolutePath());
		System.out.println(xsltfile.isFile());
		System.out.println(xsltfile.exists());
		System.out.println(xmlfile.getAbsolutePath());
		System.out.println(xmlfile.isFile());
		System.out.println(xmlfile.exists());*/
        Source xmlSource =new StreamSource(xmlfile);
        Source xsltSource = new StreamSource(xsltfile);
        File outfile = new File(out.getAbsolutePath()+".xml");
        //System.out.println(outfile.getAbsolutePath());
        Result result = new StreamResult(new FileOutputStream(outfile));
       // TransformerFactory transFact = new net.sf.saxon.TransformerFactoryImpl();
       
        javax.xml.transform.TransformerFactory transFact =  new org.apache.xalan.processor.TransformerFactoryImpl();
        //TransformerFactory transFact = TransformerFactory.newInstance(  );
       // transFact.setAttribute("indent-number", 4);
       Transformer trans = transFact.newTransformer(xsltSource);
       trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
       trans.setOutputProperty(OutputKeys.INDENT, "yes");
       // javax.xml.transform.Transformer trans = transFact.newTransformer(xsltSource);
        trans.setParameter("reverse_DNS",reverse);
        trans.transform(xmlSource, result);
        xmlSource=null;
       // System.out.println(xmlfile.delete());
	}
}
