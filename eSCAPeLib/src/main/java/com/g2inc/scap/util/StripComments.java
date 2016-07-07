package com.g2inc.scap.util;

import java.io.*;
import java.util.regex.Pattern;

public class StripComments {
  
    File inSourceDir = null;
    File outSourceDir = null;
    static final String START_COMMENT = "/*";
    static final String END_COMMENT = "*/";
  
    public StripComments(String inSource, String outSource) {
        init(inSource, outSource);
    }
  
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("usage: java StripComments inSourceDir, outSourceDir");
            return;
        }
        StripComments  stripper = new StripComments(args[0], args[1]);
        stripper.stripComments();
    }
  
    private void stripComments() throws IOException {
        stripComments(inSourceDir, outSourceDir);
    }
  
    private void stripComments(File inDir, File outDir) throws IOException {
        if (inDir.isDirectory() && outDir.isDirectory()) {
            File[] inChildren = inDir.listFiles();
            for (File inChild: inChildren) {
                if (inChild.isDirectory()) {
                    String dirName = inChild.getName();
                    File outChild = new File(outDir, dirName);
                    outChild.mkdir();
                    stripComments(inChild, outChild);
                } else {
                    String childName = inChild.getName();
                    if (childName.endsWith(".java")) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        stripOneFile(new FileInputStream(inChild), baos);
                        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                        File outChild = new File(outDir, childName);
                        FileOutputStream outStream = new FileOutputStream(outChild);
                        removeBlankLines(bais, outStream);

                    }
                }
            }
        }
    }
    
    private void removeBlankLines(InputStream inStream, OutputStream outStream) throws IOException {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
    	Pattern pattern = Pattern.compile("^[\\s]*$");
    	String line;
    	while ((line = reader.readLine()) != null) {
    		if (!pattern.matcher(line).matches()) {
    			writer.write(line);
    			writer.newLine();
    		}
    	}
        writer.flush();
        writer.close();
    }
   
    private void stripOneFile(InputStream inStream, OutputStream outStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
        boolean inBlockComment = false;
        boolean inSlashSlashComment = false;
        int char1 = reader.read();
        if (char1 != -1) {
            int char2;
            while (char1 != -1) {
            	if ((char2 = reader.read()) == -1) {
            		writer.write(char1);
            		break;
            	}
                if (char1 == '/' && char2 == '*') {
                    inBlockComment = true;
                    char1 = reader.read();
                    continue;
                } else if (char1 == '*' && char2 == '/') {
                    inBlockComment = false;
                    char1 = reader.read();
                    continue;
                } else if (char1 == '/' && char2 == '/' && !inBlockComment) {
                    inSlashSlashComment = true;
                    char1 = reader.read();
                    continue;
                }
                if (inBlockComment) {
                    char1 = char2;
                    continue;
                }
                if (inSlashSlashComment) {
                    if (char2 == '\n') {
                        inSlashSlashComment = false;
                        writer.write(char2);
                        char1 = reader.read();
                        continue;
                    } else if (char1 == '\n') {
                        inSlashSlashComment = false;
                        writer.write(char1);
                        char1 = char2;
                        continue;
                    } else {
                        char1 = reader.read();
                        continue;
                    }
                }
                writer.write(char1);
                char1 = char2;
            }
            writer.flush();
            writer.close();
        }
    }   
   
  
    private void stripOneFilex(File inFile, File outFile) throws IOException {
        StreamTokenizer reader = new StreamTokenizer(new FileReader(inFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
        reader.slashSlashComments(false);
        reader.slashStarComments(false);
//        reader.ordinaryChar('"');
//        reader.ordinaryChar('\'');
//        reader.ordinaryChar(' ');
//        reader.ordinaryChar('-');
//        reader.ordinaryChar('.');
//        reader.ordinaryChar('+');
//        reader.ordinaryChar('\t');
//        reader.ordinaryChars('0', '9');
//        reader.whitespaceChars(0,1);
        reader.eolIsSignificant(true);
//        reader.resetSyntax();
        int token;
        while ((token = reader.nextToken()) != StreamTokenizer.TT_EOF) {
            switch(token) {
            case StreamTokenizer.TT_NUMBER:
                throw new IllegalStateException("didn't expect TT_NUMBER: " + reader.nval);              
            case StreamTokenizer.TT_WORD:
                System.out.print(reader.sval);
                writer.write("WORD:" + reader.sval, 0, reader.sval.length());
            default:
                char outChar = (char) reader.ttype;
                System.out.print(outChar);
                writer.write(outChar);
            }
        }
    }
  
    private void init(String inSource, String outSource) {
        inSourceDir = new File(inSource);
        if (!inSourceDir.exists() || !inSourceDir.isDirectory()) {
            System.out.println("inSourceDir does not exist or is not directory: " + inSource);
            return;
        }
        outSourceDir = new File(outSource);
        if (!outSourceDir.exists()) {
            outSourceDir.mkdir();
        } else {
            if (!outSourceDir.isDirectory()) {
                System.out.println("outSourceDir exists but is not directory: " + outSource);
                return;  
            }
            removeContents(outSourceDir);
        }
    }
  
    private static void removeContents(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file: files) {
                if (file.isDirectory()) {
                    removeContents(file);
                } else {
                    file.delete();
                }
            }
        }
    }

}