

# vmware-scap-edit

## Overview
VMware SCAP Edit is an updated version of the [Enhanced SCAP Content Editor tool by G2, Inc.](http://www.g2-inc.com/escape). The modified tool builds on similar capabilities but few subtle differences required to build OVAL content based on latest OVAL schema.

## Try it out

### System Requirements

* A Java Runtime Environment (JRE) preferably 1.8 or later.
* 1 GB of memory.
* Supported OS – Microsoft Windows and Linux (with support for JRE).

### Build
This project is configured to be built using maven.

* Go to eSCAPeLib Directory and execute the following Maven command:
```shell
mvn clean install
```	
* Next go to eSCAPe directory and execute the following Maven command:
```shell
mvn clean install
```

### Run
* Run from distributable
 * Extract the VMware SCAP Edit 1.0.0.zip file.
 * Navigate to the extracted directory.
 * If you are running a Microsoft Windows system, execute startEditor.bat file. This will launch the tool. If you are running a 
Linux system, execute startEditor.sh file to launch the tool.

* Run From Build
 * Go to eSCAPe/target directory. 
 * Execute VMware SCAP Edit 1.0.0 jar to launch the tool.

## User Guide

[User Guide](/distributable/VMware SCAP Edit.pdf)

## Contributing

The vmware-scap-edit project team welcomes contributions from the community. If you wish to contribute code and you have not
signed our contributor license agreement (CLA), our bot will update the issue when you open a Pull Request. For any
questions about the CLA process, please refer to our [FAQ](https://cla.vmware.com/faq). For more detailed information,
refer to [CONTRIBUTING.md](CONTRIBUTING.md).
