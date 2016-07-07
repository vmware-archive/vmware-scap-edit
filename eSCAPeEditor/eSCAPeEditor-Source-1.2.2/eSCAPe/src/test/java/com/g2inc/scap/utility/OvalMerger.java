package com.g2inc.scap.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.oval.MergeStats;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.oval.OvalObject;
import com.g2inc.scap.library.domain.oval.OvalState;
import com.g2inc.scap.library.domain.oval.OvalTest;
import com.g2inc.scap.library.domain.oval.OvalVariable;
import com.g2inc.scap.library.util.CommonUtil;

/**
 * This class takes multiple oval documents and merges them into a single oval document. This is 
 * a command-line version of the same functionality we use in the GUI. 
 */
public class OvalMerger
{
	private List<File> sourceList = null;
	private File outputFile = null;
		
	private void dowork() throws Exception {	
		
		OvalDefinitionsDocument targetOdd = 
			(OvalDefinitionsDocument) SCAPDocumentFactory.createNewDocument(SCAPDocumentTypeEnum.OVAL_53);
		targetOdd.setGeneratorDate(new Date(System.currentTimeMillis()));
		targetOdd.setFilename(outputFile.getAbsolutePath());
		SCAPDocumentTypeEnum targetType = null;
		System.out.println("Output file name = " + targetOdd.getFilename());
		
		if(sourceList != null && sourceList.size() > 0) {
			
			for (File source : sourceList) {
				Document doc = SCAPDocumentFactory.parse(source);
				
				SCAPDocumentTypeEnum sdType = SCAPDocumentFactory.findDocumentType(doc);
				SCAPDocument sd = SCAPDocumentFactory.getDocument(sdType, doc);
				sd.setFilename(source.getAbsolutePath());
				if (targetType == null) {
					/*
					 * The target document type was created as Oval 5.3, but that 'default'  will be 
					 * overridden by the type of the first document to be merged.
					 */
					targetType = sdType;
					targetOdd.setDocumentType(targetType);
				}
				
				if(sdType == targetType) {
					// all documents must be the same type
					
					System.out.println("Merging input file " + sd.getFilename()); 
					
					// show some statistics about the document being merged
					OvalDefinitionsDocument sourceOdd = (OvalDefinitionsDocument) sd;
					List<OvalDefinition> defList = sourceOdd.getOvalDefinitions();
					List<OvalTest> testList = sourceOdd.getOvalTests();
					List<OvalObject> objList = sourceOdd.getOvalObjects();
					List<OvalState> stateList = sourceOdd.getOvalStates();
					List<OvalVariable> varList = sourceOdd.getOvalVariables();
					
					
					System.out.println("Source " + sourceOdd.getFilename()
							+ " has " + (defList != null ? defList.size() : "null") + " definitions");
					System.out.println("Source " + sourceOdd.getFilename()
							+ " has " + (testList != null ? testList.size() : "null") + " tests");
					System.out.println("Source " + sourceOdd.getFilename()
							+ " has " + (objList != null ? objList.size() : "null") + " objects");
					System.out.println("Source " + sourceOdd.getFilename()
							+ " has " + (stateList != null ? stateList.size() : "null") + " states");
					System.out.println("Source " + sourceOdd.getFilename()
							+ " has " + (varList != null ? varList.size() : "null") + " variables");
					
					// Finally, do the merge of the current source doc into the target
					MergeStats mergeStats = new MergeStats();
					targetOdd.merge(sourceOdd, mergeStats);			
				} else {
					System.out.println("Skipping input file " + sd.getFilename() 
							+ " because its type: " + sdType 
							+ " doesn't match merged document type: " + targetType );
				}
			}
			
			FileWriter fw = new FileWriter(outputFile);
			XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
			
			xmlo.output(targetOdd.getDoc(), fw);
			
			fw.flush();
			fw.close();
			
			List<OvalDefinition> defList = targetOdd.getOvalDefinitions();
			List<OvalTest> testList = targetOdd.getOvalTests();
			List<OvalObject> objList = targetOdd.getOvalObjects();
			List<OvalState> stateList = targetOdd.getOvalStates();
			List<OvalVariable> varList = targetOdd.getOvalVariables();
			
			// show some statistics about the final merged document
			System.out.println("Target " + targetOdd.getFilename()
					+ " has " + (defList != null ? defList.size() : "null") + " definitions");
			System.out.println("Target " + targetOdd.getFilename()
					+ " has " + (testList != null ? testList.size() : "null") + " tests");
			System.out.println("Target " + targetOdd.getFilename()
					+ " has " + (objList != null ? objList.size() : "null") + " objects");
			System.out.println("Target " + targetOdd.getFilename()
					+ " has " + (stateList != null ? stateList.size() : "null") + " states");
			System.out.println("Target " + targetOdd.getFilename()
					+ " has " + (varList != null ? varList.size() : "null") + " variables");
		}
	}

	
	/**
	 * The entry point into the program.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static void main(String[] args) throws Exception {
		CommonUtil.establishProxySettings();
		
		OvalMerger instance = new OvalMerger();
		
		try {
			instance.processCommandLine(args);
		}
		catch(IllegalArgumentException iae) {
			iae.printStackTrace();
			System.exit(1);
		}
		
		instance.dowork();
	}
	
	private void processCommandLine(String[] args)
	{
		if(args.length == 0) {
			usage();
			System.exit(0);
		}
		
		for(int i = 0; i < args.length; i++) {
			if (args[i].equals("-h") || args[i].equals("-help") || args[i].equals("?") || args[i].equals("-?")) {
				usage();
				System.exit(0);
			}
			else if(args[i].equals("-s")) {
				try {
					if(sourceList == null) {
						sourceList = new ArrayList<File>();
					}
					
					File f = new File(args[i+1]);
					
					if(f.isFile()) {
						sourceList.add(f);
					}
					else {
						System.out.println("Skipping source directory " + f.getAbsolutePath());
					}
				}
				catch(ArrayIndexOutOfBoundsException aioobe) {
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing filename for option \"-s\"");
				}
			}
			else if(args[i].equals("-o")) {
				try	{					
					 outputFile = new File(args[i+1]);
				}
				catch(ArrayIndexOutOfBoundsException aioobe) {
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing filename for option \"-o\"");
				}
			}

		}
		
		// validate all options were supplied
		if(sourceList == null) {
			throw new IllegalArgumentException("Source file or directory wasn't specified");
		}
		
		if(outputFile == null) {
			throw new IllegalArgumentException("No output file was specified");
		}
	}
	
	private void usage() {
		System.out.println("Usage: java " + this.getClass().getName() + " commandline-arguments:");
		System.out.println("    -s             oval file to be read.  Multiple -s options denote multiple sources");
		System.out.println("    -o             resulting merged oval document.");
		System.out.println("");
	}	
}
