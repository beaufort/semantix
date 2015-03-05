/* 
 * Copyright 2015 Coastal and Marine Research Centre (CMRC), Beaufort,
 * Environmental Research Institute (ERI), University College Cork (UCC).
 * Yassine Lassoued <y.lassoued@gmail.com, y.lassoued@ucc.ie>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ie.cmrc.smtx.etl.cl;

import ie.cmrc.smtx.skos.jena.SKOSFactory;
import ie.cmrc.smtx.skos.jena.TDBSKOS;
import ie.cmrc.smtx.etl.index.lucene.LuceneSKOSConceptIndexer;
import ie.cmrc.util.FileUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;

/**
 *
 * @author yassine
 */
public class TDBIndexer {
    
    
    
    /**
     * Indicates whether transitive collection membership should be indexed
     */
    private static boolean transitive = false;
    
    /**
     * Comma separated list of language codes
     */
    private static String langString = "en";
    
    /**
     * List of languages supported by the thesaurus
     */
    private static List<String> languages = null;
    
    /**
     * Path to the tdb directory
     */
    private static String tdbDir = null;
    
    /**
     * Path to the index directory
     */
    private static String indexDir = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("\n\033[1mtdbindexer\033[0m -- Version 2014.10\n");
        
        if (readArgs(args)) {
            initialise();
            
            boolean checkedOutput = clearIndexDir();
            if (checkedOutput) {
                try {
                    boolean success = index();

                    if (success) System.out.println("SUCCESS");
                    else System.out.println("FAILURE");

                } catch (CorruptIndexException ex) {
                    System.err.println("ERROR: Corrupt Index!");
                } catch (LockObtainFailedException ex) {
                    System.err.println("ERROR: Lock obtain failed!");
                } catch (IOException ex) {
                    System.err.println("ERROR: IO error!");
                }
            }
            else {
                System.out.println("FAILURE");
            }
        }
    }
    
    
    private static boolean readArgs(String[] args) {
        int i = 0;
        
        if (args.length==0) {
            printHelp();
            return false;
        }
        else {
            String arg = null;
            
            boolean option = true;
            
            // Read options
            while (i < args.length && option) {

                arg = args[i];

                if (arg.startsWith("-")) {

                    // This is an option
                    switch (arg) {
                        case "--transitive":
                        case "-t":
                            transitive = true;
                            i++;
                            break;
                        case "--languages":
                        case "-l":
                            i++;
                            if (i<args.length) {
                                langString = args[i].replaceAll("[\"']", "");
                                i++;
                            }
                            else {
                                System.err.println ("ERROR: The --languages (-l) option must be followed by a comma separated list of languages!\n"
                                        + "Please check swsetl command syntax below.");
                                printHelp();
                                return false;
                            }   break;
                        default:
                            System.err.println("ERROR: Invalid argument "+arg+"!\n"
                                    + "Please check swsetl command syntax below.");
                            printHelp();
                            return false;
                    }
                }
                else {
                    option = false;
                }
            }
            // This is the input folder
            
            tdbDir = arg;
            if (tdbDir != null) {
            
                System.out.println("Input TDB directory is "+tdbDir);
                i++;
                if (i<args.length) {
                    indexDir = args[i];
                    System.out.println("Output data will be writen to "+indexDir);
                }
                else {
                    System.out.println("ERROR: Missing output index directory!\n"
                                    + "Wille store outputs under 'index' in the current directory.");
                    
                    String userDir = System.getProperty("user.dir");
        
                    if (!userDir.endsWith("/")) userDir += "/";

                    indexDir = userDir + "index";
                    System.out.println("Output data will be writen to "+indexDir);
                }
                return true;
            }
            else {
                System.err.println("ERROR: Missing input TDB directory!\n"
                                + "Please check tdbindexer command syntax below.");
                            printHelp();
                return false;
            }
        }
    }
    
    private static void initialise() {
        
        if (langString.isEmpty()) langString = "en";
        String[] langs = langString.split(",");
        languages = new ArrayList<>(langs.length);
        languages.addAll(Arrays.asList(langs));
        
    }
    
    private static boolean clearIndexDir() {
        File outputDirFile = new File(indexDir);
        if (outputDirFile.exists()) {
            if (outputDirFile.isDirectory()) {
                System.out.println("Output directory \""+outputDirFile.getAbsolutePath()+"\" exists. Will clear its content...");
                for (File file: outputDirFile.listFiles()) {
                    if (!FileUtil.delete(file)) System.out.println("  - WARNING: Could not delete \""+file.getAbsolutePath()+"\"!");
                }
                System.out.println("  --> Done.");
            }
            else {
                System.err.println("ERROR: \""+outputDirFile.getAbsolutePath()+"\" exists but is not a directory!");
                return false;
            }
        }
        else {
            System.out.println("Output directory \""+outputDirFile.getAbsolutePath()+"\" does not exist. Will create it...");
            if (!outputDirFile.mkdir()) {
                System.err.println("ERROR: Could not create directory\""+outputDirFile.getAbsolutePath()+"\"!");
                return false;
            }
            System.out.println("  --> Done.");
        }
        return true;
    }
    
    
    private static boolean index() throws CorruptIndexException, LockObtainFailedException, IOException {
        try {
            
            System.out.println("Connecting to TDB and intialising thesaurus...");
            TDBSKOS thesaurus = SKOSFactory.createSKOSThesaurus(tdbDir);
            System.out.println("  --> Connected to TDB.");
                
            System.out.println("Indexing, pelase be patient as this may take a few minutes...");
            LuceneSKOSConceptIndexer indexer = new LuceneSKOSConceptIndexer(languages, true, transitive);
            
            boolean result = indexer.indexSKOSThesaurus(thesaurus, indexDir);

            System.out.println("  --> Done indexing.");

            System.out.println("Closing connection to TDB...");
            thesaurus.close();
            System.out.println("  --> Closed connection.");
            
            return result;
        } catch (FileNotFoundException | NullPointerException ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }
    
    
    private static void printHelp() {
        String man =
                      "\033[1mNAME\033[0m\n"
                    + "     \033[1mtdbindexer\033[0m -- creates a concept index for a TDB SKOS thesaurus\n\n"
                    + "\033[1mSYNOPSIS\033[0m\n"
                    + "     \033[1mswsetl\033[0m [options] <path/to/data/excel_file/or/ascii_directory.xlsx> [<path/to/output/directory>]\n\n"
                    + "\033[1mARGUMENTS\033[0m\n"
                    + "       \033[1mInput TDB directory\033[0m\n"
                    + "                 (Mandatory) this is the TDB directory\n\n"
                    + "       \033[1mOutput index directory\033[0m\n"
                    + "                 (Mandatory) Directory where the generated output index will be stored.\n\n"
                    + "\033[1mDESCRIPTION\033[0m\n"
                    + "     Extracts data from an input TDB directory and indexes it in the provided output\n"
                    + "     directory, if any, or the current user directory.\n\n"
                    + "\033[1mOPTIONS\033[0m\n"
                    + "       \033[1m--transitive\033[0m\n"
                    + "                 (Optional) If this option is used, then the indexer will index transitive\n"
                    + "                 collection memberships.\n\n"
                    + "       \033[1m-t\033[0m    Synonym of --transitive\n\n"
                    + "       \033[1m--languages\033[0m language code list\n"
                    + "                 (Optional) Comma separated list of two-letter language codes.\n"
                    + "                 If not specified English (en) will be the only language considered.\n"
                    + "                 Example: 'en,fr,es'\n\n"
                    + "       \033[1m-l\033[0m    Synonym of --languages\n";
        
        System.out.println(man);
    }
    
    
}
