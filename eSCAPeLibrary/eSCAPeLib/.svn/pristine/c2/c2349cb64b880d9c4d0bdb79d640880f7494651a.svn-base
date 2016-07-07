1.2.1.4    05/01/2012

* Removing unused, conflicting XML schemas

* Added latest XCCDF 1.2.1, OVAL 5.10.1, Asset Identification and ARF XML schemas

* Removed requirement 211-1 as it was erroneous

* Removed requirement 171 and 275 based on errata E4

* Adding XCCDF 1.2 schematron

* Added warning if unrecognized namespaces are encountered in content

1.2.1.3    12/22/2011

* Minor bug fixes

* Added additional detailed error messages for select Schematron rules

1.2.1.2    12/06/2011

* Included several bug fixes

* Added functionality to provide more detailed error messages for some less specific Schematron errors

1.2.1.1    10/19/2011

*Added SCAP 1.2 source and results

1.1.2.9    4/28/2011

* Adding latest and final version of OCIL schema

1.1.2.7    3/29/2011

* Fixing Schematron assertion when patches file is remote

1.1.2.6    3/28/2011

* Set the Validation Program OVAL test type warning to INFO

* Fixed check on requirement 225

* Fixed issue where files were being downloaded even when the -online flag wasn't set

* Fixed issue when running OVAL 5.8 content that caused bad errors to be reported

1.1.2.5    3/08/2011

* Updated tool to remove naming convention for remote patches up to date OVAL files

* Added the -maxsize command line option

1.1.2.4    3/04/2011

* Fixed issues related to the tool path containing spaces

* Removed the 800-126 document

* Fixed the functionality of the version flag so that the tool reports the version and terminates

1.1.2.3    3/01/2011

* Updated tool to the final draft release of 800-126 rev 1

* Includes support for OVAL 5.3 - 5.8

* Added warning if OVAL test types are used in source content that are not
	currently validated in the SCAP Validation Program

* Fixed miscellaneous bugs in the tool

* Fixed the XSLT that generates the final result HTML

* Updating the error message when an XCCDF references an external patch file

1.1.2.2    12/22/2010

* Added support for SCAP 1.1

* Added support to validate SCAP result files

0.9   01/08/2010

Requires JRE 1.6 or higher.
If the JAVA_HOME variable is set then the JRE which is at that location will be
used.  Otherwise the JRE available on the path is used.

This bundle is a pre-release version of the content validation tool.  It is not
yet feature complete but will perform the majority of the validation steps that
the final release will perform.

This tool will take as input either a zip bundle or an SCAP data stream XML file
that conforms to the SCAP data stream XML Schema.  The data stream format is union
of the files contained within the zip bundle.  The expected common case is that 
a zip bundle will be supplied.  When providing the ZIP, the ZIP must contain 
the component XML files, correctly named, at the base of the ZIP file (i.e. not
inside any folders).  In addition, no other files should exist in the ZIP file.
The -dir option is similar to specifying a ZIP file, but with the files unzipped
into a directory.  The files should be at the root of the directory specified.

When a zip bundle is provided, the SCAP version is also needed.

When a zip bundle is provided, the SCAP use case is also needed.  The SCAP use
cases which are available for SCAL 1.0 are: CONFIGURATION, VULNERABILITY_XCCDF_OVAL,
VULNERABILITY_OVAL, SYSTEM_INVENTORY.  The SCAP use cases which are available 
for SCAL 1.1 are: CONFIGURATION, VULNERABILITY_XCCDF_OVAL, VULNERABILITY_OVAL, 
SYSTEM_INVENTORY, PATCH

An optional but important switch (-online) is also available to permit the tool 
to connect through the internet to download files which are referenced within 
the bundle, but were not provided.  (A typical example would be the oval patch 
definitions which have both a remote and a local reference.)

Once the tool has completed validation a results file is created (the default
name is scap-validation-result.xml), which contains a detailed listing of the
issues discovered.  The results file contains the name of the file in the
bundle and an XPATH expression where the error was found.

The -batch option allows the user to run multiple validations in sequence against
different source content.  When the -batch option is specified, -file should point
to a ZIP file, or -dir should point to a directory.  Within that ZIP file or
directory, at the root level, should be XML data stream files, SCAP content in
ZIP files, directories with SCAP content in the root of each directory, or any
combination of those three options.  The validation tool will then iterate
through each source content XML, ZIP or directory, and validate the content.

The -resultfile or -resultdir are used to validate result content.  When using
-resultfile, point to a ZIP file that contains the result files in the root
of the ZIP file.  When using -resultdir, point to a directory that contains the
result files in the root of the directory.  When specifying results, the source
content must also be specified.  In addition, if the results were generated
by specifying an XCCDF profile in the source content, provide that profile, which
is case-sensitive, using the -profile option.

The requirement IDs which are referenced are explained further in the 
"scap-val-requirements-1.X.html" document which has been
provided along with the zip file.  The section numbers in the matrix refer to
SP 800-126 v1.x, respectively, which is available at
http://csrc.nist.gov/publications/PubsSPs.html.

OVAL 5.3 AND 5.4:

1. To validate OVAL content, the tool uses an OVAL Definition Schematron
schemas that has been modified to use XPath 1.0 instead of XPath 2.0.  To run as 
XPath 1.0, the queryBinding="xslt2" attributes were removed from the root element
of the schematron schemas.  The modification was made to avoid an XPath exception 
in the floor() function when the current element being processed is not a node.  

2. To validate Windows content, the OVAL 5.4 schema and OVAL 5.3 schematron were 
modified to allow the following oval-def:platforms:

oval-def:platform='Microsoft Windows Server 2008' 
or  oval-def:platform='Microsoft Windows 7' 
or oval-def:platform='Microsoft Windows Server 2008 R2'

In OVAL 5.3, a pattern with ID = "fileobjfilename" has a single rule with an 
erroneous assertion. The test was corrected in OVAL 5.5, therefore the test 
from OVAL 5.5 is used in place of the OVAL 5.3 test.

In OVAL 5.3, a pattern with ID = "regstevalue" has a single rule with an erroneous 
assertion. The assertion was removed in OVAL 5.4 and later. In order to fix the 
assertion in the OVAL 5.3 Schematron, the test was updated for this tool.

The official OVAL schema and schematron can be found at
http://oval.mitre.org/language/download/schema/version5.3/ and  
http://oval.mitre.org/language/download/schema/version5.4/.

EXAMPLES:

To see available command line options:
scapval

To validate the SCAP content in the file scap-content.zip for the CONFIGURATION
use case:
scapval -file c:\scapfiles\scap-content.zip -usecase CONFIGURATION

To enable resolution of remote OVAL files (e.g., patches) using HTTP connection:
scapval -file c:\scapfiles\scap-content.zip -usecase CONFIGURATION -online

To use Schematron rules in a different directory:
scapval -file c:\scapfiles\scap-content.zip -usecase CONFIGURATION -ruledir c:\myruledirectory

To send output to a different file:
scapval -file c:\scapfiles\scap-content.zip -usecase CONFIGURATION -result myresult.xml

To validate a combined SCAP content file that contains XCCDF, OVAL, etc.:
scapval -file c:\scapfiles\scap-content.xml

To turn on verbose console messages:
scapval -file c:\scapfiles\scap-content.zip -usecase CONFIGURATION -debug

To hide all console messages:
scapval -file c:\scapfiles\scap-content.zip -usecase CONFIGURATION -quiet

RELEASE NOTES - 0.8:

* For SCAP schematron messages, provide an XPath location to the node in question that is
relative to the input file in the SCAP ZIP bundle. 

* Added level attribute to the message element in the validation results to indicate
whether an SCAP schematron message is a WARNING or ERROR.

* For traceability, added the names of the input files to the end of the validation results,
with SHA-256 hash values.  Also the SCAP version, the highest OVAL version, and
a timestamp to the validation results.

* Validate OVAL documents against both OVAL 5.3 and 5.4 schemas a) to warn when a document
marked as OVAL 5.4 is actually valid for 5.3 and b) to produce an error when document
marked as 5.3 fails to validate against 5.4.  Also now included with the tool is the complete
set of OVAL definition schemas.

* Moved the schematron rule files into the SCAP validation JAR file so that an external
rule directory is not required.  Rule developers can still use the -ruledir option to 
explicitly specify an external rule directory.

* Renamed the schema for the validation results from data-stream-0.1.xsd to 
scap-data-stream-0.1.xsd.

* The combined SCAP data stream .xml file that is generated by the tool is no longer left in
the working folder when the tool finishes.  Instead a temporary file is created and deleted.
In a future version of the tool, this will likely be a user option.

