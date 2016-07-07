package com.g2inc.scap.util;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import com.g2inc.scap.library.domain.SCAPElementImpl;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.g2inc.scap.library.domain.oval.MergeStats;
import com.g2inc.scap.library.domain.oval.OvalDefinition;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.domain.xccdf.Check;
import com.g2inc.scap.library.domain.xccdf.CheckContentRef;
import com.g2inc.scap.library.domain.xccdf.Group;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.ProfileSelect;
import com.g2inc.scap.library.domain.xccdf.Rule;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.util.CommonUtil;

/**
 * This class generates oval defintions documents and optionally an xccdf document that contains 
 * only definitions(and dependencies) that contain references supplied by the user.   It reads in a master
 * oval file, like the one released by NIST.
 */
public class CVEMiner
{
	private File documentFileOrDir = null;
	private File outputFile = null;
	private Set<String> refsToLookFor = null;
	private String suppliedDefNS = null;
	private String suppliedDefId = null;
	private File xccdfFile = null;
	private String benchmarkId = null;
	private ArrayList<String> profileIds = new ArrayList<String>();
	private String groupId = null;
	private String ruleId = null;

	/**
	 * Print the usage statement.
	 */
	public void usage()
	{
		System.out.println("Usage: java " + this.getClass().getName() + " commandline-arguments:");
		System.out.println("    -s             oval file or directory of files to be read");
		System.out.println("    -o             oval output file to be created"); 
		System.out.println("    -dn            namespace of new oval definition");
		System.out.println("    -di            oval id of new definition" );
		System.out.println("    -r             referenceId (more than one -r line is allowed to collect multiple references");
		System.out.println("    -xccdffile     name of xccdf file to be created or updated");
		System.out.println("    -benchmarkid   id of benchmark to be created in xccf file");
		System.out.println("    -profileid     id of profile to be created or updated in xccf file. More than one profileId line is allowed");
		System.out.println("    -groupid       id of group to be created or updated in xccf file");
		System.out.println("    -ruleid        id of rule to be created in xccf file; must not already exist in group");
		System.out.println("");
		System.out.println("Example of -di and -dn: If di=oval:mil.nsa.foo and dn=9999, new oval definition oval:mil.nsa.foo:def:9999 will be created");
	}
	
	private void processCommandLine(String[] args)
	{
		if(args.length == 0)
		{
			usage();
			System.exit(0);
		}
		
		for(int x = 0; x < args.length; x++)
		{
			if (args[x].equals("-h") || args[x].equals("-help") || args[x].equals("?") || args[x].equals("-?"))
			{
				usage();
				System.exit(0);
			}
			if(args[x].equals("-o"))
			{
				try 
				{
					outputFile = new File( args[x+1] );
				}
				catch(ArrayIndexOutOfBoundsException aioobe)
				{
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing filename for option \"-o\"");
				}
			}
			else if(args[x].equals("-dn"))
			{
				try
				{
					suppliedDefNS = args[x+1];
				}
				catch(ArrayIndexOutOfBoundsException aioobe)
				{
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing definition namespace for option \"-dn\"");
				}
			}
			else if(args[x].equals("-di"))
			{
				try
				{
					suppliedDefId = args[x+1];
				}
				catch(ArrayIndexOutOfBoundsException aioobe)
				{
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing definition id for option \"-di\"");
				}
			}
			else if(args[x].equals("-s"))
			{
				try
				{
					documentFileOrDir = new File(args[x+1]);
				}
				catch(ArrayIndexOutOfBoundsException aioobe)
				{
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing filename for option \"-s\"");
				}
			}
			else if(args[x].equals("-r"))
			{
				try
				{
					String ref = args[x+1];
					
					if(refsToLookFor == null)
					{
						refsToLookFor = new HashSet<String>();
					}
					refsToLookFor.add(ref);
				}
				catch(ArrayIndexOutOfBoundsException aioobe)
				{
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing references for option \"-r\"");
				}
			}
			else if(args[x].equals("-xccdffile"))
			{
				try 
				{
					xccdfFile = new File(args[x+1] );
				}
				catch(ArrayIndexOutOfBoundsException aioobe)
				{
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing filename for option \"-xccdffile\"");
				}
			} 
			else if(args[x].equals("-benchmarkid"))
			{
				try 
				{
					benchmarkId = args[x+1];
				}
				catch(ArrayIndexOutOfBoundsException aioobe)
				{
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing id for option \"-benchmarkid\"");
				}
			} 
			else if(args[x].equals("-profileid"))
			{
				try 
				{
					profileIds.add(args[x+1]);
				}
				catch(ArrayIndexOutOfBoundsException aioobe)
				{
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing id for option \"-profileid\"");
				}
			} 
			else if(args[x].equals("-groupid"))
			{
				try 
				{
					groupId = args[x+1];
				}
				catch(ArrayIndexOutOfBoundsException aioobe)
				{
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing id for option \"-groupid\"");
				}
			} 
			else if(args[x].equals("-ruleid"))
			{
				try 
				{
					ruleId = args[x+1];
				}
				catch(ArrayIndexOutOfBoundsException aioobe)
				{
					usage();
					aioobe.printStackTrace();
					throw new IllegalArgumentException("Missing id for option \"-ruleid\"");
				}
			} 
		}
		
		// validate all options were supplied
		if(documentFileOrDir == null)
		{
			throw new IllegalArgumentException("Source file or directory wasn't specified");
		}
		
		if((suppliedDefNS != null && suppliedDefId == null) || (suppliedDefNS == null && suppliedDefId != null))
		{
			throw new IllegalArgumentException("Options \"-dn\" and \"-di\" require each other");
		}
		validateCommandLineArgs();
	}
	
	private void dowork() throws Exception
	{	
		SCAPContentManager scm = SCAPContentManager.getInstance(documentFileOrDir);

		SCAPDocument sd = scm.getDocument(documentFileOrDir.getAbsolutePath());
		OvalDefinitionsDocument nodd = null;
		OvalDefinition addedDef = null;
		if(sd != null)
		{
			if(suppliedDefNS != null)
			{
				nodd = scm.newDefinitionsDocumentForRefs(refsToLookFor, sd.getDocumentType(), suppliedDefNS, suppliedDefId);
				addedDef = findAddedOvalDefinition(nodd, suppliedDefNS, suppliedDefId);
			}
			else
			{
				nodd = scm.newDefinitionsDocumentForRefs(refsToLookFor, sd.getDocumentType());
			}			

			System.out.println("Document: " + documentFileOrDir.getAbsolutePath());
			System.out.println("Summary:");
			System.out.println("  Number of definitions: " + nodd.getOvalDefinitions().size());
			System.out.println("  Number of tests:       " + nodd.getOvalTests().size());
			System.out.println("  Number of objects:     " + nodd.getOvalObjects().size());
			System.out.println("  Number of states:      " + nodd.getOvalStates().size());
			System.out.println("  Number of variables:   " + nodd.getOvalVariables().size());

			Set<String> docReferences = nodd.getReferenceIds();

			Iterator<String> refStats = refsToLookFor.iterator();

			System.out.println();
			System.out.println("Reference summary");
			while(refStats.hasNext())
			{
				String ref = refStats.next();
				boolean refFound = false;

				if(docReferences.contains(ref))
				{
					refFound = true;
				}

				System.out.println(ref + " " + (refFound ? "" : "NOT") + " found");
			}
			System.out.println();

			MergeStats mergeStats = null;
			if(outputFile != null)
			{
				if(outputFile.exists())
				{
					System.out.println("Oval output File " + outputFile.getAbsolutePath() + " exists.. Merging");

					scm = SCAPContentManager.getInstance(outputFile);

					SCAPDocument existingDoc = scm.getDocument(outputFile.getAbsolutePath());

					if(existingDoc.getClass() == nodd.getClass())
					{
						mergeStats = new MergeStats();
						((OvalDefinitionsDocument)existingDoc).merge(nodd, mergeStats);
						nodd = (OvalDefinitionsDocument) existingDoc;
						
						if(mergeStats != null)
						{
							System.out.println("Merged Defs:      " + mergeStats.getDefsMerged());
							System.out.println("Merged Tests:     " + mergeStats.getTestsMerged());
							System.out.println("Merged Objects:   " + mergeStats.getObjectsMerged());
							System.out.println("Merged States:    " + mergeStats.getStatesMerged());
							System.out.println("Merged Variables: " + mergeStats.getVariablesMerged());
						}
					}
					else
					{
						System.err.println("Destination file is not an oval definitions document or is not of the same version: " + outputFile.getAbsolutePath());
					}
				}
				else
				{
					mergeStats = new MergeStats();
				}

				if(mergeStats != null)
				{
					System.out.println("Document changed, writing");
					FileWriter fw = null;

					fw = new FileWriter(outputFile);

					XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
					xmlo.output(nodd.getDoc(), fw);

					fw.flush();
					fw.close();
				}
				else
				{
					System.out.println("Document unchanged, not writing");
				}
			}
			else
			{
				System.out.println("Begin document");
				XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
				xmlo.output(nodd.getDoc(), System.out);
			}
			if (xccdfFile != null && addedDef != null && mergeStats != null) 
			{
				// Make sure oval file and xccdf file are in the same directory
				if (!outputFile.getAbsoluteFile().getParentFile().equals(xccdfFile.getAbsoluteFile().getParentFile())) {
					System.out.println("OVAL and XCCDF files must be in the same directory");
					throw new IllegalArgumentException("OVAL and XCCDF files not in same directories: OVAL file dir=" + outputFile.getParent() + ", XCCDF file dir=" + xccdfFile.getParent());
				}
				// see if we should create or update an XCCDF file to include a Rule for the new
				// Oval definition.
				XCCDFBenchmark benchmark = null;
				if (xccdfFile.exists()) 
				{
					scm = SCAPContentManager.getInstance(xccdfFile);
					benchmark = (XCCDFBenchmark) scm.getDocument(xccdfFile.getAbsolutePath());
					if (benchmarkId != null) {
						String id = benchmark.getId().trim();
						if (!id.equals(benchmarkId.trim())) {
							System.out.println("Warning: XCCDF file exists, and actual benchmark id does not match requested benchmark id. Using actual id");
							benchmarkId = id;
						}
					}
					// Make sure new Rule does not already exist
					Rule rule = benchmark.findRule(ruleId);
					if (rule != null) {
						throw new IllegalStateException("Rule already exists, can't insert - rule id=" + ruleId);
					}
				}
				else 
				{
					if (benchmarkId == null) 
					{
						throw new IllegalStateException("Benchmarkid parameter not set, but does not exist, file=" + xccdfFile.getAbsolutePath());
					}
				//	benchmark = (XCCDFBenchmark) SCAPDocumentFactory.createNewDocument(SCAPDocumentTypeEnum.XCCDF_114);
				}
				addRuleToBenchmark(addedDef, benchmark);
				System.out.println("Writing XCCDF File");
				FileWriter fw = new FileWriter(xccdfFile);
				XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
				xmlo.output(benchmark.getDoc(), fw);
				fw.flush();
				fw.close();
			}
		}
	}
	
	private void addRuleToBenchmark(OvalDefinition ovalDef, XCCDFBenchmark benchmark) {		
		// First, find or create Group to contain new Rule
		Group group = benchmark.findGroup(groupId);
		if (group == null) {
			Element groupElement = new Element("Group", benchmark.getNamespace());
			group = benchmark.createGroup();
			group.setElement(groupElement);
			group.setId(groupId);
			benchmark.addGroup(group);
		}
		
		// Second, create a new Rule and add it to the selected Group
		Rule rule = benchmark.createRule();
		Element ruleElement = new Element("Rule", benchmark.getNamespace());
		rule.setElement(ruleElement);
		rule.setRoot(benchmark.getRoot());
		rule.setId(ruleId);
		group.addRule(rule);
		
		// Third, create a new Check and add it to the new Rule
		Check check = benchmark.createCheck();
		Element checkElement = new Element("check", benchmark.getNamespace());
		check.setElement(checkElement);
		check.setRoot(benchmark.getRoot());
		check.setSystem(OvalDefinition.OVAL_NAMESPACE);
		rule.addCheck(check);
		
		// Fourth, create a new CheckContentRef and add it to the new Check
		CheckContentRef checkContentRef = benchmark.createCheckContentRef();
		Element checkContentRefElement = new Element("check-content-ref", benchmark.getNamespace());
		checkContentRef.setElement(checkContentRefElement);
		checkContentRef.setRoot(benchmark.getRoot());
		// href field is just the name of the Oval xml file
		checkContentRef.setHref(outputFile.getName());
		checkContentRef.setName(ovalDef.getId());
		check.addCheckContentRef(checkContentRef);
		
		// Fifth, create or find each Profile whose id is in profileIds, and add a select element to
		// each of these Profiles for the new Rule id;
		for (int i=0; i<profileIds.size(); i++) {
			String profileId = profileIds.get(i);
			Profile profile = benchmark.findProfile(profileId);
			ProfileSelect select = null;
			if (profile != null) {
				select = profile.findProfileSelect(ruleId);
				if (select != null) {
					if (!select.isSelected()) {
						select.setSelected(true);
					}
				}
			} else {
				profile = benchmark.createProfile();
				Element profileElement = new Element("Profile", benchmark.getNamespace());
				profile.setElement(profileElement);
				profile.setRoot(benchmark.getRoot());
				profile.setId(profileId);
				profile.setTitle("Title");
				benchmark.addProfile(profile);
			}
			// Find or create a 'select' for the new Rule to be added to this Profile
			if (select == null) {
				select = benchmark.createProfileSelect();
				Element selectElement = new Element("select", benchmark.getNamespace());
				select.setElement(selectElement);
				select.setRoot(benchmark.getRoot());
				select.setIdRef(ruleId);
				select.setSelected(true);
				profile.addProfileSelect(select);
			}
		}		
	}
	
	private void validateCommandLineArgs() {
		if (outputFile == null && xccdfFile != null) {
			throw new IllegalArgumentException("-xccdfFile was specified, but NOT -o (for Oval output file). Can't write to an xccdf file unless there is also an Oval output file");
		} else if (xccdfFile == null && 
				(benchmarkId != null || profileIds.size() > 0 || groupId != null || ruleId != null)) {
			throw new IllegalArgumentException("-xccdfFile NOT specified, but one or more other xccdf-related parameters are specified: -benchmarkid, -profileid, -groupid, -ruleid");
		} else if (xccdfFile != null) {
			if (profileIds.size() == 0) {
				throw new IllegalArgumentException("-xccdfFile specified, but no -profileid; To create/update an xccdf document, there must be at least one profile");
			} else if (groupId == null) {
				throw new IllegalArgumentException("-xccdfFile specified, but no -groupid; To create/update an xccdf document, there must be exactly one group id");
			} else if (ruleId == null) {
				throw new IllegalArgumentException("-xccdfFile specified, but no -ruleid; To create/update an xccdf document, there must be exactly one rule id");
			} 
		}		
	}
	
	private OvalDefinition findAddedOvalDefinition(OvalDefinitionsDocument defDoc, String namespace, String defIdNumber)
	{
		List<OvalDefinition> defList = defDoc.getOvalDefinitions();
		
		OvalDefinition addedDef = defList.get(0);
		
		if (addedDef == null)
		{
			throw new IllegalStateException("Can't find added definition, oval definitions doc is empty");
		}
		// validate the definition found against expected namespace and id number
		String addedDefId = addedDef.getId();
		if (!addedDefId.startsWith("oval:" + namespace)) {
			throw new IllegalStateException("Can't find added definition with expected namespace:" + namespace);
		}
		int defIdNumberAsInt = Integer.parseInt(defIdNumber);
		int idNumStart = addedDefId.lastIndexOf(":");
		if (idNumStart == -1) {
			throw new IllegalStateException("Last definition in new document has invalid format:" + addedDefId);
		}
		String addedDefIdNumber = addedDefId.substring(idNumStart+1);
		int addedDefIdNumberAsInt = Integer.parseInt(addedDefIdNumber);
		if (addedDefIdNumberAsInt < defIdNumberAsInt) {
			throw new IllegalStateException("Last definition in file should have id number >= expected number " + defIdNumber 
					+ " actual number found is " + addedDefIdNumber);
		}
		return addedDef; 
	}
	
	/**
	 * The entry point into this program.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception
	{
   		Properties log4jProps = new Properties();
		CVEMiner instance = new CVEMiner();
		
		try
		{
			// set some properties
			log4jProps.setProperty("log4j.rootLogger", "INFO, A1");
			log4jProps.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
			log4jProps.setProperty("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
			log4jProps.setProperty("log4j.appender.A1.layout.ConversionPattern", "%m%n");
			// configure log4j with the properties
        	PropertyConfigurator.configure(log4jProps);
        	
			CommonUtil.establishProxySettings();
			

			instance.processCommandLine(args);
		}
		catch(IllegalArgumentException iae)
		{
			iae.printStackTrace();
			System.exit(1);
		}
		
		instance.dowork();

	}
}

