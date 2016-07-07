package com.g2inc.scap.utility;

import com.g2inc.scap.library.domain.SCAPContentManager;
import com.g2inc.scap.library.domain.SCAPDocument;
import com.g2inc.scap.library.domain.SCAPDocumentFactory;
import com.g2inc.scap.library.domain.SCAPDocumentTypeEnum;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.g2inc.scap.library.domain.xccdf.Group;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.ProfileSelect;
import com.g2inc.scap.library.domain.xccdf.Rule;
import com.g2inc.scap.library.domain.xccdf.SelectableItem;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;
import com.g2inc.scap.library.util.CommonUtil;
/**
 * XCCDFMerge is a command-line utility to merge all XCCDF documents in a directory into a single XCCDF document.
 * The output XCCDF Benchmark document will have:
 *   one Profile, containing the combined <select> elements from all source documents
 *   one Group, containing the combined <Rule> elements from all source documents.
 *   
 * The merged Profile will only contain one select for any particular idref. Any select element in a source
 * document that has selected="false" will not be copied to the merged Profile.
 * 
 * The merged Group will only contain one Rule for any particular Rule Id. If the source documents contain 
 * multiple definitions of a given Rule id, only the first one encountered will be copied. 
 *    
 * Command line format:
 * java XCCDFMerge -indir inputDirectory -outfile outputFileName -benchmarkid id -groupid id -profileid id    
 *    
 * @author glenn.strickland
 *
 */
public class XCCDFMerge
{
	private File inputDir = null;
	private File outputFile = null;
	private String benchmarkId = "Test_New_XCCDF_Document";
	private String groupId = null;
	private String profileId = null;
	
	private void processCommandLine(String[] args)
	{
		if(args.length == 0) {
			usage("No arguments specified");
		}
		
		for(int x = 0; x < args.length; x++) {
			if(args[x].equals("-indir")) {
				inputDir = new File(args[++x]);
				if (!inputDir.exists() || !inputDir.isDirectory()) {
					throw new IllegalArgumentException("indir does not exist or is not a directory:" + args[x+1]);
				}
			} else if(args[x].equals("-outfile")) {
				outputFile = new File(args[++x]);
			} else if (args[x].equals("-benchmarkid")) {
				benchmarkId = args[++x];
			} else if (args[x].equals("-groupid")) {
				groupId = args[++x];
			} else if (args[x].equals("-profileid")) {
				profileId = args[++x];
			}
		}
		
		// validate all required options were supplied
		if(inputDir == null) {
			usage("Source directory (-indir) wasn't specified");
			return;
		}
		if(outputFile == null) {
			usage("Output XCCDF file name (-outfile) wasn't specified");
		}
		if (groupId == null) {
			groupId = benchmarkId + "_Group";
		}
		if (profileId == null) {
			profileId = benchmarkId + "_Profile";
		}
	}
	
	private void usage(String message) {
		System.out.println("Usage: " + this.getClass().getName() + " -indir inputDirectory -outfile outputFileName -benchmarkid id -groupid id -profileid id");
		System.out.println(message);
		return;
	}
	
	private void merge() throws Exception
	{	
		SCAPContentManager scm = SCAPContentManager.getInstance(inputDir);
		List<SCAPDocument> docList = scm.getLoadedSCAPDocuments(SCAPDocumentTypeEnum.XCCDF_114);
		if (docList.size() == 0) {
			System.out.println("No XCCDF files were loaded from directory:" + inputDir.getAbsolutePath());
			return;
		}
		XCCDFBenchmark mergedBenchmark = (XCCDFBenchmark) SCAPDocumentFactory.createNewDocument(SCAPDocumentTypeEnum.XCCDF_114);
		mergedBenchmark.setId(benchmarkId);
		
		Profile mergedProfile = mergedBenchmark.createProfile();
		mergedProfile.setTitle(profileId);
		profileId = CommonUtil.sanitize(profileId); //get rid of spaces for id
		mergedProfile.setId(profileId);
		mergedBenchmark.addProfile(mergedProfile);
		
		Group mergedGroup = mergedBenchmark.createGroup();
		groupId = CommonUtil.sanitize(groupId);  //get rid of spaces for id
		mergedGroup.setId(groupId);
		mergedBenchmark.addGroup(mergedGroup);
		
		HashSet<String> selectNames = new HashSet<String>();  // for duplicate idref detection
		HashSet<String> ruleNames = new HashSet<String>();    // for duplicate rule id detection
		
		ArrayList<ProfileSelect> mergedSelectList = new ArrayList<ProfileSelect>();
		ArrayList<SelectableItem> mergedRuleList = new ArrayList<SelectableItem>();
		
		for (SCAPDocument scapDoc : docList) {
			XCCDFBenchmark benchmark = (XCCDFBenchmark) scapDoc;
			Iterator<Profile> profileIter = benchmark.getProfileList().iterator();
			while (profileIter.hasNext()) {
				Profile profile = profileIter.next();
				Iterator<ProfileSelect> selectIter = profile.getSelectList().iterator();
				while (selectIter.hasNext()) {
					ProfileSelect select = selectIter.next();
					if (select.isSelected()) {  //don't include in merged Profile unless selected="true"
						String selectId = select.getIdRef();
						if (selectNames.add(selectId)) {    // don't add duplicate select idrefs
							select.getElement().detach();
							mergedSelectList.add(select);
						} else {
							System.out.println("Eliminated duplicate select idref:" + selectId);
						}
					}
				}
				
			}
			Iterator<SelectableItem> groupIter = benchmark.getSelectableItemList().iterator();
			while (groupIter.hasNext()) {
				SelectableItem item = groupIter.next();
				if (item instanceof Group) {
					addRulesFromGroup((Group) item, mergedRuleList, ruleNames);
				}
			}
		}
		// sort merged Rule list by Rule id, then set it in merged Group
		Collections.sort(mergedRuleList, new Comparator<SelectableItem>() {
			public int compare(SelectableItem o1, SelectableItem o2) {
				return (o1.getId().compareTo(o2.getId()));
			}			
		});
		mergedGroup.setChildren(mergedRuleList);
		
		// sort merged select list by select idref, then set it in merged Profile
		Collections.sort(mergedSelectList, new Comparator<ProfileSelect>() {
			public int compare(ProfileSelect o1, ProfileSelect o2) {
				return (o1.getIdRef().compareTo(o2.getIdRef()));
			}			
		});
		mergedProfile.setSelectList(mergedSelectList);
		
		mergedBenchmark.saveAs(outputFile);
	}
	
	private void addRulesFromGroup(Group group, List<SelectableItem> mergedRuleList, Set<String> ruleNames) {
		Iterator<SelectableItem> items = group.getChildren().iterator();
		while (items.hasNext()) {
			SelectableItem item = items.next();
			if (item instanceof Rule) {
				Rule rule = (Rule) item;
				if (ruleNames.add(rule.getId())) {
					item.getElement().detach();
					mergedRuleList.add(item);
				} else {
					System.out.println("Eliminated duplicate Rule id:" + rule.getId());
				}
			} else if (item instanceof Group) {
				addRulesFromGroup((Group) item, mergedRuleList, ruleNames);
			}
		}
	}
	
	/**
	 * The entry point into the program.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception
	{
		CommonUtil.establishProxySettings();		
		XCCDFMerge instance = new XCCDFMerge();
		instance.processCommandLine(args);		
		instance.merge();

	}
}

