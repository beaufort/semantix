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
import ie.cmrc.smtx.etl.ext.Extractor;
import ie.cmrc.smtx.etl.ext.tabular.TabularExtractor;
import ie.cmrc.smtx.etl.index.lucene.LuceneSKOSConceptIndexer;
import ie.cmrc.tabular.DatasetFactory;
import ie.cmrc.tabular.TabularDataset;
import ie.cmrc.tabular.exceptions.InvalidFormatException;
import ie.cmrc.util.FileUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;

/**
 *
 * @author yassine
 */
public class ETL {
    
    /**
     * Format of the input data; this may be "excel" or "ascii"
     */
    private static InputFormat format = null;
    
    /**
     * Inferencing options in String format
     */
    private static String infOptionsString = null;
    
    /**
     * Inferencing options
     */
    private static Map<String,Boolean> infOptions;
    
    /**
     * File extension for ASCII input data
     */
    private static String extension = "";
    
    /**
     * Column separator for ASCII input data
     */
    private static String separator = ",";
    
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
     * Path to the input data file or directory
     */
    private static String data = null;

    /**
     * Path to the output directory
     */
    private static String output = null;
    
    /**
     * Path to the index directory
     */
    private static String indexDir = null;
    
    /**
     * Path to the tdb directory
     */
    private static String tdbDir = null;
    
    private static final String owlOntologySheetName = "owl:Ontlogy";
    private static final String skosCSSheetName = "skos:ConceptScheme";
    private static final String skosCollectionSheetName = "skos:Collection";
    private static final String skosSemanticRelationSheetName = "skos:semanticRelation";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("\n\033[1mswsetl\033[0m -- Version 2014.04\n");
        
        if (readArgs(args)) {
            
            boolean initialised = initialise();
            
            if (initialised) {
                boolean checkedOutput = clearDirectories();
                if (checkedOutput) {
                    try {
                        boolean success = etl();

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

                    if (arg.equals("--format") || arg.equals("-f")) {
                        i++;
                        if (i<args.length) {
                            format = InputFormat.fromString(args[i].toLowerCase());
                            if (format==null) {
                                System.err.println ("ERROR: The provided input format is not supported!\n");
                                System.err.println ("Supported input formats are: "+Arrays.toString(InputFormat.values()));
                                return false;
                            }
                            i++;
                        }
                        else {
                            System.err.println ("ERROR: The --format (-f) option must be followed by the input data format!\n"
                                    + "Please check swsetl command syntax below.");
                            printHelp();
                            return false;
                        }
                    }
                    else if (arg.equals("--infer") || arg.equals("-i")) {
                        i++;
                        if (i<args.length) {
                            infOptionsString = args[i];
                            i++;
                        }
                        else {
                            System.err.println ("ERROR: The --infer (-i) option must be followed by the inferencing options!\n"
                                    + "Please check swsetl command syntax below.");
                            printHelp();
                            return false;
                        }
                    }
                    else if (arg.equals("--extension") || arg.equals("-e")) {
                        i++;
                        if (i<args.length) {
                            extension = args[i].replaceAll("[\"']", "");
                            i++;
                        }
                        else {
                            System.err.println ("ERROR: The --extension (-e) option must be followed by the file extension!\n"
                                    + "Please check swsetl command syntax below.");
                            printHelp();
                            return false;
                        }
                    }
                    else if (arg.equals("--separator") || arg.equals("-s")) {
                        i++;
                        if (i<args.length) {
                            separator = args[i].replaceAll("[\"']", "");
                            i++;
                        }
                        else {
                            System.err.println ("ERROR: The --separator (-s) option must be followed by a valid data separator!\n"
                                    + "Please check swsetl command syntax below.");
                            printHelp();
                            return false;
                        }
                    }
                    else if (arg.equals("--transitive") || arg.equals("-t")) {
                        transitive = true;
                        i++;
                    }
                    else if (arg.equals("--languages") || arg.equals("-l")) {
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
                        }
                    }
                    else {
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
            // This is the input file or folder
            data = arg;
            System.out.println("Input Data file is "+data);
            i++;
            if (i<args.length) {
                output = args[i];
                System.out.println("Output data will be writen to "+output);
            }

            

            if (data !=null) return true;
            else {
                System.err.println("ERROR: Missing input data file or directory!\n"
                                + "Please check swsetl command syntax below.");
                            printHelp();
                return false;
            }
        }
    }
    
    private static boolean initialise() {
        if (output==null) output = System.getProperty("user.dir");
        
        if (!output.endsWith("/")) output += "/";
        
        indexDir = output + "index";
        tdbDir = output + "tdb";
        
        if (format == null) format = InputFormat.excel;
        
        
        if (infOptionsString == null) {
            infOptionsString = "sit";
        }
        
        if (langString.isEmpty()) langString = "en";
        String[] langs = langString.split(",");
        languages = new ArrayList<>(langs.length);
        languages.addAll(Arrays.asList(langs));
        
        try {
            infOptions = parseInferencingOptions(infOptionsString);
        }
        catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
            
        return true;
        
    }
    
    private static boolean clearDirectories() {
        File outputDirFile = new File(output);
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
    
    
    private static boolean etl() throws CorruptIndexException, LockObtainFailedException, IOException {
        File dataFile = new File (data);
        
        TabularDataset dataset;
        try {
            System.out.println("Connecting to dataset..");
            if (format==InputFormat.excel) dataset = DatasetFactory.createFromExcel(dataFile);
            else dataset = DatasetFactory.createFromASCIIFileDir(dataFile, extension, separator);
            System.out.println("  --> Done.");
            
            System.out.println("Connecting to TDB and intialising thesaurus...");
            TDBSKOS thesaurus = SKOSFactory.createSKOSThesaurus(tdbDir, true, true);
            System.out.println("  --> Connected to TDB.");
            
            System.out.println("Initialising extractor...");
            Extractor extractor = new TabularExtractor(dataset, true);
            System.out.println("  --> Extractor initialised.");
            
            System.out.println("Extracting...");
            boolean result = extractor.extractDataToThesaurus(thesaurus, infOptions.get("i"), infOptions.get("s"), infOptions.get("t"), true);
            System.out.println("  --> Done");
            
            try {
                System.out.println("Closing input dataset...");
                dataset.close();
                System.out.println("  --> Input dataset closed");
            } catch (IOException ex) {
                System.out.println("WARNING: Could not close input dataset");
            }

            System.out.println("Synchronising TDB...");
            thesaurus.sync();
            System.out.println("  --> TDB synchronised.");
            
            if (result) {
                
                System.out.println("Indexing...");
                LuceneSKOSConceptIndexer indexer = new LuceneSKOSConceptIndexer(languages, true, transitive);
                
                File indexDirFile = new File(indexDir);
                result = indexer.indexSKOSThesaurus(thesaurus, indexDirFile);

                System.out.println("  --> Done indexing.");
                
                System.out.println("Closing connection to TDB...");
                thesaurus.close();
                System.out.println("  --> Closed connection.");
            }
            
            return result;
        } catch (InvalidFormatException | FileNotFoundException | NullPointerException ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }
    
    private static Map<String,Boolean> parseInferencingOptions(String inferOptions) throws IllegalArgumentException {
        if (inferOptions.matches("[sit]{1,3}")) {
            Map<String,Boolean> options = new HashMap<>(3);
            options.put("i", inferOptions.contains("i"));
            options.put("s", inferOptions.contains("s"));
            options.put("t", inferOptions.contains("t"));
            return options;
        }
        else throw new IllegalArgumentException(inferOptions+" is not a valid inferencing option."
                + "Possible values are combinations of 's' (super properties), 'i' (inverse properties), and 't' (transitive closure)."
                + "Examples: use \"-i sit\" for inferencing inverse, super and transitive properties.");
    }
    
    
    private static void printHelp() {
        String man =
                      "\033[1mNAME\033[0m\n"
                    + "     \033[1mswsetl\033[0m -- extract, transform, and load data to a semantic web service (SWS) backend\n\n"
                    + "\033[1mSYNOPSIS\033[0m\n"
                    + "     \033[1mswsetl\033[0m [options] <path/to/data/excel_file/or/ascii_directory.xlsx> [<path/to/output/directory>]\n\n"
                    + "\033[1mARGUMENTS\033[0m\n"
                    + "       \033[1mInput file or directory\033[0m\n"
                    + "                 (Mandatory) this may be either un Excel spreadsheet or a directory\n"
                    + "                 containing ASCII files (e.g., CSV files).\n\n"
                    + "       \033[1mOutput directory\033[0m\n"
                    + "                 (Optional) Directory where the generated outputs will be stored.\n"
                    + "                 If the ouput directory is not provided, then outputs will be saved in the\n"
                    + "                 current user directory.\n\n"
                    + "\033[1mDESCRIPTION\033[0m\n"
                    + "     Extracts data from an input file or directory and creates a TDB store and an index\n"
                    + "     in the provided output directory, if any, or the current user directory.\n\n"
                    + "\033[1mOPTIONS\033[0m\n"
                    + "       \033[1m--extension\033[0m extension\n"
                    + "                 (Optional) For ASCII input data, this specifies the file extension.\n"
                    + "                 Default value is '' which means that al files are considered.\n"
                    + "                 Example: 'txt', 'data', '.data', etc.\n\n"
                    + "       \033[1m-e\033[0m    Synonym of --extension\n\n"
                    + "       \033[1m--format\033[0m format\n"
                    + "                 (Optional) input data format. Supported values are 'excel' and 'ascii'\n"
                    + "                 Default value is 'excel'\n\n"
                    + "       \033[1m-f\033[0m    Synonym of --format\n\n"
                    + "       \033[1m--infer\033[0m [s][i][t]\n"
                    + "                 (Optional) inferencing options. If not specified inferences will not be computed.\n"
                    + "                 Values: any combination of 1 or 3 of the following inferencing options:\n"
                    + "                     s: infer super properties\n"
                    + "                     i: infer inverse properties\n"
                    + "                     t: infer transitive closure\n"
                    + "                 Example: 'sit' for super, inverse, and transtitive property inferencing\n\n"
                    + "       \033[1m-i\033[0m    Synonym of --infer\n\n"
                    + "       \033[1m--transitive\033[0m\n"
                    + "                 (Optional) If this option is used, then the indexer will index transitive\n"
                    + "                 collection memberships.\n\n"
                    + "       \033[1m-t\033[0m    Synonym of --transitive\n\n"
                    + "       \033[1m--languages\033[0m language code list\n"
                    + "                 (Optional) Comma separated list of two-letter language codes.\n"
                    + "                 If not specified English (en) will be the only language considered.\n"
                    + "                 Example: 'en,fr,es'\n\n"
                    + "       \033[1m-l\033[0m    Synonym of --languages\n\n"
                    + "       \033[1m--separator\033[0m separator\n"
                    + "                 (Optional) For ASCII input data, this specifies the column separator\n"
                    + "                 Default value is ','.\n"
                    + "                 Example: ';' or '|', etc.\n\n"
                    + "       \033[1m-s\033[0m    Synonym of --separator\n";
        
        System.out.println(man);
    }
    
    
}
