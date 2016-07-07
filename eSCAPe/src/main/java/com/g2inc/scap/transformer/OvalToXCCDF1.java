package com.g2inc.scap.transformer;

/* Copyright (c) 2016 - 2016. VMware, Inc. All rights reserved.
* 
* This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License version 3.0 
* as published by the FreeSoftware Foundation This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License version 3.0 
* for more details. You should have received a copy of the GNU General Public License version 3.0 along with this program; if not, write to
* the Free Software Foundation, Inc., 675 Mass Avenue, Cambridge, MA 02139, USA.
*/

import javax.swing.JOptionPane;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;


public class OvalToXCCDF1 {
	//public static void main(String[] args) throws IOException, URISyntaxException, TransformerException {
	public static void ovalToXccdf(File xmlfile, File xsltfile) throws IOException, URISyntaxException, TransformerException {
        //TransformerFactory factory = TransformerFactory.newInstance();
        //File xmlfile = new File("a-oval.xml"); 
    	//JOptionPane.showMessageDialog(null, "in ovaltoxccdf", "alert", JOptionPane.ERROR_MESSAGE);

	//	File xsltfile = new File("oval-to-xccdf.xsl");
    	//JOptionPane.showMessageDialog(null, xsltfile.exists() + xsltfile.getAbsolutePath() + xsltfile.isFile(), "alert", JOptionPane.ERROR_MESSAGE);

		/*System.out.println(xsltfile.getAbsolutePath());
		System.out.println(xsltfile.isFile());
		System.out.println(xsltfile.exists());
		System.out.println(xmlfile.getAbsolutePath());
		System.out.println(xmlfile.isFile());
		System.out.println(xmlfile.exists());*/
        Source xmlSource =new StreamSource(xmlfile);
        Source xsltSource = new StreamSource(xsltfile);
        File outfile = new File("temp.xml");
        FileOutputStream os = new FileOutputStream(outfile); 
        Result result = new StreamResult(os);
        TransformerFactory transFact = TransformerFactory.newInstance(  );
        Transformer trans = transFact.newTransformer(xsltSource);
        trans.setParameter("ovalfile", xmlfile.getName());
        trans.transform(xmlSource, result);
        os.close();
        result=null;
        outfile=null;
    }

}
