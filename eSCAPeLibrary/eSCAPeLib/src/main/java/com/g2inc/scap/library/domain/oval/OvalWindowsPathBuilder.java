package com.g2inc.scap.library.domain.oval;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class OvalWindowsPathBuilder {
    protected Map<String, OvalObject> winVarsFound = new HashMap<String,OvalObject>();
    protected Map<String,Map<String,OvalVariable>> winVarLiteralMap = new HashMap<String, Map<String,OvalVariable>>();
    
    protected OvalDefinitionsDocument ovalDocument = null;
    
    public OvalWindowsPathBuilder(OvalDefinitionsDocument ovalDoc) {
    	this.ovalDocument = ovalDoc;
    }
    
    /**
     * A statically initiallized map containing all the special windows environment variables we know about
     * so we can create registry objects to look them up when we need to.
     * 
     */
    public static Map<String, SpecialCaseWindowsVariable> supportedVariables = new LinkedHashMap<String, SpecialCaseWindowsVariable>();
    {
        String varName = "";
        String hive = "";
        
        varName = "%systemroot%";
        hive = "HKEY_LOCAL_MACHINE";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", "SystemRoot", "System Root"));

        varName = "%commonprogramfiles(x86)%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "CommonFilesDir(x86)", "Common Program Files(x86)"));

        varName = "%commonprogramfiles%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "CommonFilesDir", "Common Program Files"));

        varName = "%programfiles(x86)%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "ProgramFilesDir(x86)", "Common Program Files(x86)"));

        varName = "%programfiles%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "ProgramFilesDir", "Common Program Files"));

        varName = "%programdata%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders", "Common AppData", "Common Program Data"));
        
        varName = "\\program files (x86)\\common files";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "CommonFilesDir(x86)", "Common Program Files(x86)"));

        varName = "\\program files\\common files";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "CommonFilesDir", "Common Program Files"));

        varName = "\\program files (x86)";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "ProgramFilesDir(x86)", "Common Program Files(x86)"));

        varName = "\\program files";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
        		"SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "ProgramFilesDir", "Common Program Files"));        

        hive = "HKEY_CURRENT_USER";
        varName = "%appdata%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "Volatile Environment", "APPDATA", "Current user's roaming app data directory"));
        varName = "%homepath%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "Volatile Environment", "HOMEPATH", "Current user's home directory path minus drive"));
        varName = "%homedrive%";
        supportedVariables.put(varName, new SpecialCaseWindowsVariable(hive, varName,
                "Volatile Environment", "HOMEDRIVE", "Current user's home directory drive"));     

    }
    
    public OvalVariable handleWindowsEnvironmentVariableCreation(String path)
    {
        OvalVariable pathVar = null;
        OvalObject regObj = null;

        String lcPath = path.toLowerCase();

        for(Iterator<String> knownVarNames = supportedVariables.keySet().iterator();knownVarNames.hasNext();)
        {
            String lcSysVarName = knownVarNames.next();
            int index = lcPath.indexOf(lcSysVarName);
            if(index > -1)
            {
                if(winVarsFound.containsKey(lcSysVarName))
                {
                    // dont' create another, use exising one
                    regObj = winVarsFound.get(lcSysVarName);
                }
                else
                {
                    // create object that gets the value of the registry
                    regObj = createWinEnvRegObj(lcSysVarName);
                    winVarsFound.put(lcSysVarName, regObj);
                }

                String literal = null;

                if(path.length() == lcSysVarName.length())
                {
                    literal = "";
                }
                else
                {
                    literal = path.substring(index +lcSysVarName.length());
                }

                if(winVarLiteralMap.containsKey(lcSysVarName))
                {
                    Map<String,OvalVariable> lit2VarMap = winVarLiteralMap.get(lcSysVarName);

                    if(lit2VarMap.containsKey(literal))
                    {
                        pathVar = lit2VarMap.get(literal);
                    }
                }

                // didn't find it in our map, must add it
                if(pathVar == null)
                {
                    // we haven't added this one before
                    pathVar = createVarFromObjectAndLiteral(regObj, literal);
                    Map<String,OvalVariable> lit2VarMap = new HashMap<String,OvalVariable>();

                    lit2VarMap.put(literal, pathVar);

                    // save for later
                    winVarLiteralMap.put(lcSysVarName, lit2VarMap);
                }

                break;
            }
        }

        return pathVar;
    }
    private OvalVariable createVarFromObjectAndLiteral(OvalObject obj, String literal)
    {
        OvalVariable var = ovalDocument.createVariable("local_variable");
        var.setDatatype(TypeEnum.STRING.toString().toLowerCase());
        
        
        OvalVariableComponentObject objComp = var.createObjectComponent();
        objComp.setItemField("value");
        objComp.setObjectId(obj.getId());
        
        if (literal == null || literal.equals("")) 
        {
        	var.addChild(objComp);
        	var.setComment("Variable that references " + obj.getComment());
        } 
        else 
        {
            OvalFunction concat = var.createOvalFunction(OvalFunctionEnum.concat);
            var.addChild(concat);
            concat.addChild(objComp);

            OvalVariableComponentLiteral litComp = var.createLiteralComponent();
            litComp.setValue(literal);
            concat.addChild(litComp);
            var.setComment("Variable that concatenates " + obj.getComment() + " with the literal " + literal);
        }
        ovalDocument.add(var);
        return var;
    }

    private OvalObject createWinEnvRegObj(String var)
    {
        // create registry object
        OvalObject regObject = ovalDocument.createObject("windows", "registry_object");
        regObject.setComment("registry object that checks " + var);
        OvalObjectParameter hiveOop = regObject.createObjectParameter("hive");
        OvalObjectParameter keyOop = regObject.createObjectParameter("key");
        OvalObjectParameter nameOop = regObject.createObjectParameter("name");

        SpecialCaseWindowsVariable winVar = supportedVariables.get(var);
        
        hiveOop.setValue(winVar.getHive());

        keyOop.setValue(winVar.getKeyName());

        nameOop.setValue(winVar.getValueName());

        regObject.addChild(hiveOop);
        regObject.addChild(keyOop);
        regObject.addChild(nameOop);

        ovalDocument.add(regObject);

        return regObject;
    }
	public OvalDefinitionsDocument getOvalDocument() {
		return ovalDocument;
	}
	public void setOvalDocument(OvalDefinitionsDocument ovalDocument) {
		this.ovalDocument = ovalDocument;
	}    
}
