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

package ie.cmrc.smtx.etl.ext.tabular;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Resource;
import ie.cmrc.smtx.skos.model.SKOS;
import ie.cmrc.smtx.skos.model.SKOSAnnotationProperty;
import ie.cmrc.smtx.skos.model.SKOSCollection;
import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSConceptScheme;
import ie.cmrc.smtx.skos.model.SKOSElementProperty;
import ie.cmrc.smtx.skos.jena.SKOSFactory;
import ie.cmrc.smtx.skos.model.SKOSObjectProperty;
import ie.cmrc.smtx.skos.model.SKOSResource;
import ie.cmrc.smtx.skos.model.SKOSSemanticProperty;
import ie.cmrc.smtx.skos.model.SKOSType;
import ie.cmrc.smtx.skos.jena.JenaSKOS;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import ie.cmrc.smtx.etl.ext.Extractor;
import ie.cmrc.tabular.Header;
import ie.cmrc.tabular.Table;
import ie.cmrc.tabular.TableCell;
import ie.cmrc.tabular.TableRow;
import ie.cmrc.tabular.TabularDataset;
import ie.cmrc.util.Term;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Use this class to extract a SKOS thesaurus from an Excel spreadsheet.
 * @author Yassine Lassoued
 */
public final class TabularExtractor implements Extractor {

    /**
     * <code>Dataset</code> to extract model from
     */
    private final TabularDataset dataset;
    
    /**
     * If set to {@code true} the extractor will print processing information on the standard output
     */
    private final boolean verbose;
    
    /**
     * Default name space for concepts
     */
    private String conceptNS = "";
    
    /**
     * Default namespace for collection schemes
     */
    private String schemeNS = "";
    
    /**
     * Default namespace for collections
     */
    private String collectionNS = "";
    
    /**
     * Maximum number of objects to keep in memory before committing
     */
    private final int maxBufferSize;
    
    /**
     * Default maximum buffer size
     */
    private static final int DEFAULT_MAX_BUFFER = 300000;
    
    // TODO instead of default "," separator for multivalue cells, add a customisable separator attribute
    
    
    /**
     * Builds an Excel SKOS thesaurus extractor using an Excel file path
     * @param dataset Tabular dataset to extract data from
     * @param verbose If set to {@code true} then the {@code TabularExtractor} will
     * print processing information on the standard output
     */
    public TabularExtractor(TabularDataset dataset, boolean verbose) {
        this(dataset, verbose, DEFAULT_MAX_BUFFER);
    }
    
    /**
     * Builds an Excel SKOS thesaurus extractor using an Excel file path
     * @param dataset Tabular dataset to extract data from
     * @param verbose If set to {@code true} then the {@code TabularExtractor} will
     * print processing information on the standard output
     * @param maxBufferSize Maximum number of objects to keep in memory before committing
     */
    public TabularExtractor(TabularDataset dataset, boolean verbose, int maxBufferSize) {
        this.dataset = dataset;
        this.verbose = verbose;
        this.maxBufferSize = maxBufferSize;
    }
    
    /**
     * {@inheritDoc}
     * @param thesaurus {@inheritDoc}
     * @param inferInverseProperties {@inheritDoc}
     * @param inferSuperProperties {@inheritDoc}
     * @param inferTransitiveClosure {@inheritDoc}
     * @param incrementalSync {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean extractDataToThesaurus(SKOS thesaurus, boolean inferInverseProperties, boolean inferSuperProperties, boolean inferTransitiveClosure, boolean incrementalSync) {
        boolean success = false;
        if (this.dataset != null) {
            
            // Initialise namespaces
            this.initNamespaces();
            
            // Build default JenaSKOSThesaurus
            //JenaSKOSThesaurus thesaurus = SKOSThesaurusFactory.buildDefaultSKOSThesaurus();
            
            // Load collection schemes into thesaurus and get returned metadata
            if (verbose) System.out.println("Adding concept schemes...");
            List<CSMetadata> csMetadata = this.addConceptSchemes(thesaurus);
            if (verbose) System.out.println("  --> "+csMetadata.size()+" concept scheme(s) added.");
            if (incrementalSync) thesaurus.sync();
            if (verbose) System.out.println("  --> Committed.");
            
            // For each collection scheme, load concepts into thesaurus
            if (!csMetadata.isEmpty()) {
                if (verbose) System.out.println("Adding concepts for concept schemes...");
                for (CSMetadata csm: csMetadata) {
                    if (verbose) System.out.println("  - Adding concepts for concept scheme "+"\""+csm.getURI()+"\"...");
                    int addedConcepts = this.addConcepts(csm, thesaurus, inferInverseProperties, incrementalSync);
                    if (verbose) System.out.println("      --> "+addedConcepts+" concept(s) added.");
                    if (incrementalSync) thesaurus.sync();
                    if (verbose) System.out.println("      --> Committed.");
                }
                if (verbose) System.out.println("Concepts added");
            }
            else {
                if (verbose) System.out.println("No concepts to add.");
            }
            // Load collections into thesaurus and get returned metadata
            if (verbose) System.out.println("Adding collections...");
            List<CollectionMetadata> collMetadata = this.addCollections(thesaurus);
            if (verbose) System.out.println("  --> "+collMetadata.size()+" collection(s) added.");
            
            if (incrementalSync) thesaurus.sync();
            if (verbose) System.out.println("  --> Committed.");
            
            // Add collection members
            if (!collMetadata.isEmpty()) {
                if (verbose) System.out.println("Adding collection members...");
                
                // Add collection members from the collection-specific tables
                for (CollectionMetadata cm: collMetadata) {
                    String conceptsTableName = cm.getConceptsTableName();
                    if (conceptsTableName!=null && !conceptsTableName.isEmpty()) {
                        if (verbose) System.out.println("  - Adding member resources for collection \""+cm.getURI()+"\"...");
                        int numMembers = this.addCollectionMembers(cm, thesaurus, inferInverseProperties, inferSuperProperties, incrementalSync);
                        if (verbose) System.out.println("      --> "+numMembers+" member(s) added.");
                        if (incrementalSync) thesaurus.sync();
                        if (verbose) System.out.println("      --> Committed.");
                    }
                    else if (verbose) {
                        if (verbose) System.out.println("  - No member table specified for collection \""+cm.getURI()+"\".");
                    }
                }
                
                // Add collection members from the skos-member table
                if (verbose) System.out.println("  - Adding concept members for collections from table \""+CTN.SKOS_MEMBER.tableName()+"\"...");
                this.addCollectionMembers(thesaurus, inferInverseProperties, inferSuperProperties, incrementalSync);
                if (verbose) System.out.println("  --> Done.");
                
                // Add collection members from the skos-member-Collection table
                if (verbose) System.out.println("  - Adding collection members for collections from table \""+CTN.SKOS_MEMBER_COLLECTION.tableName()+"\"...");
                this.addCollectionCollectionMembers(thesaurus, inferInverseProperties, inferSuperProperties, incrementalSync);
                if (verbose) System.out.println("  --> Done.");
            }
            else if (this.verbose) {
                System.out.println("No collection members to add.");
            }
            
            
            // Get relationship table names
            List<String> relTableNames = this.getRelationshipTables();
            
            //Add reltionships
            if (verbose) System.out.println("Adding relationships...");
            for (String relTableName: relTableNames) {
                if (verbose) System.out.println("  - Adding relationships from table "+"\""+relTableName+"\"...");
                int numRelationships = this.addRelationships(relTableName, thesaurus, inferInverseProperties, inferSuperProperties, incrementalSync);
                if (verbose) System.out.println("      --> "+numRelationships+" relationship(s) added.");
                if (incrementalSync) thesaurus.sync();
                if (verbose) System.out.println("      --> Committed.");
            }
            
            // If inferencing is requested, compute transitive inferences
            if (inferTransitiveClosure) {
                if (verbose) System.out.println("Computing inferences for semantic relationships...");
                this.inferTransitiveClosureForSemanticRelationships(thesaurus, incrementalSync);
                if (incrementalSync) thesaurus.sync();
                if (verbose) System.out.println("      --> Committed.");
                
                
                if (verbose) System.out.println("Computing inferences for collection membership relationships...");
                this.inferTransitiveClosureForCollectionMembership(thesaurus, incrementalSync);
                if (incrementalSync) thesaurus.sync();
                if (verbose) System.out.println("      --> Committed.");
            }
            
            
            
            success = true;
            if (verbose) System.out.println("Done.");
        }
        return success;
    }
    
    /**
     * Initialises the default namespaces for collection schemes, concepts, and collections.
     * These are specified in the owl-Ontology table.
     */
    private void initNamespaces() {
        
        Table ontologyTable = this.dataset.getTable(CTN.OWL_ONTOLOGY.tableName());


        if (ontologyTable != null) {
            TableRow row = ontologyTable.getNextRow();

            if (row != null) {
                String baseNS = row.getFieldStringValue(CFN.SWS_NAMESPACE.field());
                
                if (baseNS!=null) {
                    baseNS = baseNS.trim();
                    this.conceptNS = baseNS;
                    this.schemeNS = baseNS;
                    this.collectionNS = baseNS;
                }
                
                String schemePostfix = row.getFieldStringValue(CFN.SWS_NS_SCHEME_PREFIX.field());
                String collPostfix = row.getFieldStringValue(CFN.SWS_NS_COLLECTION_PREFIX.field());
                String conceptPostfix = row.getFieldStringValue(CFN.SWS_NS_CONCEPT_PREFIX.field());
                
                if (schemePostfix!=null) this.schemeNS += schemePostfix.trim();
                if (collPostfix!=null) this.collectionNS += collPostfix.trim();
                if (conceptPostfix!=null) this.conceptNS += conceptPostfix.trim();
            }
            try {
                ontologyTable.close();
            } catch (IOException ex) {}
        }
    }
    
    private List<CSMetadata> addConceptSchemes(SKOS thesaurus) {
        List<CSMetadata> metadata = new ArrayList<>();
        
        Table csTable = this.dataset.getTable(CTN.SKOS_CONCEPT_SCHEME.tableName());
        
        if (csTable != null) {
            
            Header header = csTable.getHeader();
            HashMap<Term,SKOSAnnotationProperty> annoFields= this.getAnnotationFields(header);
            
            if (!annoFields.isEmpty()) {
                TableRow tr;
                while ((tr = csTable.getNextRow()) != null) {
                    
                    
                    
                    CSMetadata csm = this.getCSMetadata(tr);
                    if (csm != null) {
                        try {
                            
                            SKOSConceptScheme cs = thesaurus.createConceptScheme(csm.getURI());
                            
                            Set<Term> fields = annoFields.keySet();
                            // Add annotations
                            for (Term field: fields) {
                                SKOSAnnotationProperty property = annoFields.get(field);
                                List<TableCell> annotationCells = tr.getCells(field);
                                
                                for (TableCell cell: annotationCells) {
                                    String annotation = cell.getStringValue();
                                    if (annotation!=null && !(annotation=annotation.trim()).isEmpty()) {
                                        cs.addAnnotation(property, annotation, field.getLanguage());
                                    }
                                }
                            }
                            metadata.add(csm);
                            
                        } catch (IllegalArgumentException ex) {
                            //Logger.getLogger(TabularExtractor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                }
            }
            try {
                // Close csTable
                csTable.close();
            } catch (IOException ex) {}
        }
        return metadata;
    }
    
    
    private HashMap<Term,SKOSAnnotationProperty> getAnnotationFields(Header header) {
        HashMap<Term,SKOSAnnotationProperty> fieldAnnotationMap = new HashMap<>();
        List<Term> fields = header.getFields();
        if (fields!=null) {
            for (Term field: fields) {
                SKOSAnnotationProperty property = SKOSAnnotationProperty.fromString(field.getString());
                if (property != null) {
                    fieldAnnotationMap.put(field, property);
                }
            }
        }
        return fieldAnnotationMap;
    }
    
    /**
     * A convenience method that builds a CSMetadata objects and initialises
     * it using the provided {@link ie.cmrc.tabular.TableRow} object.
     * Field names must follow the naming convention defined in {@link CFN}:<br/>
     * <code>
 URI or name of the collection scheme: FNC.SKOS_CONCEPT_SCHEME<br/>
     * Tabular format: CFN.SWS_TABULAR, supported values (case
     * insensitive): true, t, yes, y, 1, false, f, no, n, 0<br/>
     * Concepts table name: CFN.SWS_CONCEPTS<br/>
 List of collection defitions table name: CFN.SWS_CONCEPT_DEFINITIONS (comma-separated list of table names)<br/>
     * List of supported languages: CFN.SWS_LANG (comma-separated list of language codes)
     * </code>
     * @param tr {@link ie.cmrc.tabular.TableRow} object to parse metadata from
     * @return {@link ie.cmrc.sws.etl.ext.tabular.util.CSMetadata} object containing
 the collection scheme metadata loaded from {@code TableRow} {@code tr}. If
 the URI of the collection scheme is empty or {@code null}, then {@code null} is returned.
     */
    private CSMetadata getCSMetadata(TableRow tr) {
        if (tr!=null) {
            String uri = ResourceParser.parseResourceURI(tr.getFieldStringValue(CFN.SKOS_CONCEPT_SCHEME.field()), this.schemeNS);
            
            if (uri!=null && !uri.isEmpty()) {
                
                
                
                CSMetadata csMetadata = new CSMetadata();
            
                csMetadata.setURI(uri);
                
                Boolean tabular = tr.getFieldBooleanValue(CFN.SWS_TABULAR.field());
                if (tabular!=null) csMetadata.setTabular(tabular);

                csMetadata.setConceptListTableName(tr.getFieldStringValue(CFN.SWS_CONCEPT_LIST.field()));
                
                List<String> defTableNames = this.extractCSVFromRow(tr, CFN.SWS_CONCEPT_DEFINITIONS.field());
                if (!defTableNames.isEmpty()) csMetadata.setConceptDefinitionTableNames(defTableNames);
                
                List<String> languages = this.extractCSVFromRow(tr, CFN.SWS_LANG.field());
                if (!languages.isEmpty()) csMetadata.setLanguages(languages);
                
                Boolean sorted = tr.getFieldBooleanValue(CFN.SWS_SORTED.field());
                if (sorted == null) sorted = false;
                csMetadata.setSorted(sorted);
                
                return csMetadata;
            }
            
            
        }
        return null;
    }
    
    
    private List<String> extractCSVFromRow(TableRow tr, Term field) {
        List<String> allValues = new ArrayList<>();
        List<TableCell> cells = tr.getCells(field);
        for (TableCell cell: cells) {
            if (cell!=null && !cell.isEmpty()) {
                String csvs = cell.getStringValue();
                String[] values = csvs.split(",");
                for (String value : values) {
                    value = value.trim();
                    if (!value.isEmpty() && !allValues.contains(value)) allValues.add(value);
                }
            }
        }
        return allValues;
    }
    
    private int addConcepts(CSMetadata csm, SKOS thesaurus, boolean inferInverseProperties, boolean incrementalSync) {
        if (csm.isTabular()) {
            // The concepts tables are in a tabular format, which means that
            // declarations, labels, and definitions are all in one single table
            
            return this.addTabularConcepts(csm, thesaurus, inferInverseProperties, incrementalSync);
        }
        else {
            // The concepts tables are in a (subject, predicate, , which means that
            // declarations, labels, and definitions are split into many tables
            
            return this.addTripleConcepts(csm, thesaurus, inferInverseProperties, incrementalSync);
        }
        
        
    }
    
    
    private int addTabularConcepts(CSMetadata csm, SKOS thesaurus, boolean inferInverseProperties, boolean incrementalSync) {
        // In this case conepts are defined in a tabular format, they me spread over one or more tables
        int counter = 0;
        
        // Load collection definitions
        List<String> defTableNames = csm.getConceptDefinitionTableNames();
        if (defTableNames!=null) {
            
            SKOSConceptScheme conceptScheme = thesaurus.getConceptScheme(csm.getURI());
            Term conceptURIField = CFN.SKOS_CONCEPT.field();
            for (String tableName: defTableNames) {
                // Get definitions table
                Table defTable = this.dataset.getTable(tableName);
                if (defTable!=null) {
                    if (defTable.getHeader()!=null) {
                        if (defTable.getHeader().containsField(conceptURIField)) {
                            Header header = defTable.getHeader();
                            HashMap<Term,SKOSAnnotationProperty> annoFields= this.getAnnotationFields(header);
                            Term topConceptField = header.getField(CFN.SKOS_TOP_CONCEPT.fieldName());

                            if (!annoFields.isEmpty()) {
                                TableRow tr;
                                int bufferSize = 0;
                                while ((tr = defTable.getNextRow()) != null) {
                                    String uri = ResourceParser.parseResourceURI(tr.getFieldStringValue(conceptURIField), this.conceptNS);
                                    if (uri!=null && !(uri=uri.trim()).isEmpty()) {
                                        try{
                                            SKOSConcept concept = conceptScheme.createConcept(uri);
                                            counter++;
                                            bufferSize++;

                                            Boolean isTopConcept;

                                            if (concept!=null && topConceptField != null && (isTopConcept = tr.getFieldBooleanValue(topConceptField))!=null && isTopConcept==true) {
                                                concept.addRelation(SKOSElementProperty.topConceptOf, conceptScheme);

                                                if(inferInverseProperties) {
                                                    conceptScheme.addRelation(SKOSElementProperty.hasTopConcept, concept);
                                                }
                                            }
                                            Set<Term> fields = annoFields.keySet();
                                            // Add annotations
                                            for (Term field: fields) {
                                                SKOSAnnotationProperty property = annoFields.get(field);
                                                List<TableCell> annotationCells = tr.getCells(field);
                                                for (TableCell cell: annotationCells) {
                                                    String annotation = cell.getStringValue();
                                                    if (annotation!=null && !(annotation=annotation.trim()).isEmpty()) {
                                                        if (concept != null) concept.addAnnotation(property, annotation, field.getLanguage());
                                                    }
                                                }
                                            }
                                            if (incrementalSync && bufferSize>=this.maxBufferSize) {
                                                thesaurus.sync();
                                                bufferSize = 0;
                                            }
                                        } catch (IllegalArgumentException ex) {
                                            //Logger.getLogger(TabularExtractor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                                        }
                                    }
                                }
                            }

                            try {
                                // Close definitions table
                                defTable.close();
                            } catch (IOException ex) {} // OK, no need to do anything
                        }
                        else {
                            Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "\"{0}\": does not contain a field named\"{1}\". It will be skipped!", new Object[]{tableName, CFN.SKOS_CONCEPT.fieldName()});
                        }
                    }
                    else {
                        Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "Table \"{0}\": has no header. It will be skipped!", tableName);
                    }
                }
                else {
                    Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "\"{0}\": No such table!", tableName);
                }
            }
        }
        return counter;
    }
    
    private int addTripleConcepts(CSMetadata csm, SKOS thesaurus, boolean inferInverseProperties, boolean incrementalSync) {
        // In this case, collection information is split over 2 types of tables:
        // 1. Concept declaration table: simply lists all the concepts of a conept scheme (this is optional)
        // 3. Concept definition tables: one or more tables that contain the collection definitions (labels, annotations, definitions, etc.) expressed as RDF triples
        
        int counter = 0;
        
        SKOSConceptScheme conceptScheme = thesaurus.getConceptScheme(csm.getURI());
        
        String conceptsListTableName = csm.getConceptListTableName();
        
        // Get collection declarations table
        Table conceptsTable = this.dataset.getTable(conceptsListTableName);
        
        // This indicates whether the collection URIs are all declared in
        // a collection declaration table
        boolean conceptsAlreadyDeclared;

        // Load the collection URIs if a collection declaration table is provided
        if (conceptsTable!=null) {
            if (conceptsTable.getHeader()!=null) {
                if (conceptsTable.getHeader().containsField(CFN.SKOS_CONCEPT.fieldName())) {
                    conceptsAlreadyDeclared = true;
                    Term conceptURIField = CFN.SKOS_CONCEPT.field();
                    Term topConceptField = conceptsTable.getHeader().getField(CFN.SKOS_TOP_CONCEPT.fieldName());
                    TableRow tr;
                    int bufferSize = 0;
                    while ((tr = conceptsTable.getNextRow()) != null) {
                        String uri = ResourceParser.parseResourceURI(tr.getFieldStringValue(conceptURIField), this.conceptNS);
                        if (uri!=null && !uri.isEmpty()) {
                            try {
                                SKOSConcept concept = conceptScheme.createConcept(uri);
                                counter++;
                                bufferSize++;
                                if (concept!=null && topConceptField != null && tr.getFieldBooleanValue(topConceptField)==true) {
                                    concept.addRelation(SKOSElementProperty.topConceptOf, conceptScheme);

                                    if(inferInverseProperties) {
                                        conceptScheme.addRelation(SKOSElementProperty.hasTopConcept, concept);
                                    }
                                }
                                if (incrementalSync && bufferSize>=this.maxBufferSize) {
                                    thesaurus.sync();
                                    bufferSize = 0;
                                }
                            }
                            catch (IllegalArgumentException ex) {
                                if (this.verbose) System.out.println("WARNING: Could not create concept \""+uri+"\"!");
                            }
                        }

                    }
                    try {
                        // Close concepts table
                        conceptsTable.close();
                    } catch (IOException ex) {}
                }
                else {
                    Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "\"{0}\": does not contain a field named\"{1}\". It will be skipped!", new Object[]{conceptsListTableName, CFN.SKOS_CONCEPT.fieldName()});
                    conceptsAlreadyDeclared = false;
                }
            }
            else {
                Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "Table \"{0}\": has no header. It will be skipped!", conceptsListTableName);
                conceptsAlreadyDeclared = false;
            }
        }
        else {
            conceptsAlreadyDeclared = false;
        }
        
        // Load collection definitions
        List<String> defTableNames = csm.getConceptDefinitionTableNames();
        if (defTableNames!=null) {
            for (String tableName: defTableNames) {
                // Get definitions table
                Table defTable = this.dataset.getTable(tableName);
                
                if (defTable!=null) {
                    if (defTable.getHeader()!=null) {
                        if (defTable.getHeader().containsField(CFN.RDF_SUBJECT.fieldName())
                            && defTable.getHeader().containsField(CFN.RDF_PREDICATE.fieldName())
                            && defTable.getHeader().containsField(CFN.RDF_OBJECT.fieldName())) {
                            // Add definitions
                            int addedFromannotationTables = this.addConceptAnnotationTriples(defTable, thesaurus, conceptScheme, conceptsAlreadyDeclared);
                            counter += addedFromannotationTables;
                            try {
                                // Close definitions table
                                defTable.close();
                            } catch (IOException ex) {} // OK, no need to do anything
                        }
                        else {
                            Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "Table \"{0}\" does not have the required fields rdf:subject, rdf:predicate, and rdf:object. It will be skipped!", tableName);
                        }
                    }
                    else {
                        Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "Table \"{0}\": has no header. It will be skipped!", tableName);
                    }
                }
                else {
                    Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "\"{0}\": No such table!", tableName);
                }
            }
        }
        return counter;
    }
    
    private int addConceptAnnotationTriples(Table table, SKOS thesaurus, SKOSConceptScheme conceptScheme, boolean conceptsAlreadyDeclared) {
        int counter = 0;
        Term subjectField = CFN.RDF_SUBJECT.field();
        Term predicateField = CFN.RDF_PREDICATE.field();
        Term objectField = CFN.RDF_OBJECT.field();
        String currentConceptURI = null;
        SKOSConcept currentConcept = null;

        TableRow tr;
        while ((tr = table.getNextRow()) != null) {
            String uri = ResourceParser.parseResourceURI(tr.getFieldStringValue(subjectField), this.conceptNS);
            String annotationPropStr = tr.getFieldStringValue(predicateField);
            String annotation = tr.getFieldStringValue(objectField);

            // Skip null or empty values
            if (uri!=null && !uri.isEmpty()
                    && annotationPropStr!=null && !(annotationPropStr=annotationPropStr.trim()).isEmpty()
                    && annotation!=null && !(annotation=annotation.trim()).isEmpty()) {
                
                Term annotationPropTerm = new Term(annotationPropStr);
                SKOSAnnotationProperty annotationProp = SKOSAnnotationProperty.fromString(annotationPropTerm.getString());
                if (annotationProp != null) {
                
                    if (!uri.equals(currentConceptURI)) {
                        // This is a new collection
                        currentConceptURI = uri;

                        currentConcept = thesaurus.getConcept(currentConceptURI);
                        if (currentConcept==null && !conceptsAlreadyDeclared) {
                            try {
                                currentConcept = conceptScheme.createConcept(currentConceptURI);
                                counter++;
                            } catch (IllegalArgumentException ex) {
                                //Logger.getLogger(TabularExtractor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                            }
                        }
                    }

                    if (currentConcept!=null) {
                        currentConcept.addAnnotation(annotationProp, annotation, annotationPropTerm.getLanguage());
                    }
                }
            }
        }
        return counter;
    }
    
    
    private List<CollectionMetadata> addCollections(SKOS thesaurus) {
        List<CollectionMetadata> metadata = new ArrayList<>();
        
        Table collTable = this.dataset.getTable(CTN.SKOS_COLLECTION.tableName());
        
        if (collTable != null) {
            Header header = collTable.getHeader();
            HashMap<Term,SKOSAnnotationProperty> annoFields= this.getAnnotationFields(header);
            
            if (!annoFields.isEmpty()) {
                TableRow tr;
                while ((tr = collTable.getNextRow()) != null) {
                    CollectionMetadata cm = this.getCollectionMetadata(tr);
                    if (cm != null) {
                        try {
                            SKOSCollection coll = thesaurus.createCollection(cm.getURI());
                            
                            Set<Term> fields = annoFields.keySet();
                            // Add annotations
                            for (Term field: fields) {
                                SKOSAnnotationProperty property = annoFields.get(field);
                                List<TableCell> annotationCells = tr.getCells(field);
                                for (TableCell cell: annotationCells) {
                                    String annotation = cell.getStringValue();
                                    if (annotation!=null && !(annotation=annotation.trim()).isEmpty()) {
                                        coll.addAnnotation(property, annotation, field.getLanguage());
                                    }
                                }
                            }
                            metadata.add(cm);
                            
                            // Add collection to collection schemes
                            List<String> csNames = this.extractCSVFromRow(tr, CFN.SKOS_CONCEPT_SCHEME.field());
                            for (String csName: csNames) {
                                String uri = ResourceParser.parseResourceURI(csName, this.schemeNS);
                                if (uri!=null && !uri.isEmpty()) {
                                    SKOSConceptScheme conceptScheme = thesaurus.getConceptScheme(uri);
                                    if (conceptScheme==null) {
                                        conceptScheme = thesaurus.createConceptScheme(uri);
                                    }
                                    coll.addToConceptScheme(conceptScheme);
                                }
                            }
                            
                        } catch (IllegalArgumentException ex) {
                            //Logger.getLogger(TabularExtractor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                }
            }
            try {
                collTable.close();
            } catch (IOException ex) {
                Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "Could not close collections table \""+collTable.getName()+"\"!", ex);
            }
        }
        
        return metadata;
    }
    
    /**
     * A convenience method that builds a CollectionMetadata object and initialises
     * it using the provided {@link ie.cmrc.tabular.TableRow} object.
     * Field names must follow the naming convention defined in {@link CFN}:<br/>
     * <code>
 URI or name of the collection scheme: FNC.SKOS_COLLECTION<br/>
     * Concepts table name: FNC.SWS_CONCEPTS<br/>
     * </code>
     * @param tr {@link ie.cmrc.tabular.TableRow} object to parse metadata from
     * @return {@link ie.cmrc.sws.etl.ext.tabular.util.CollectionMetadata} object containing
     * the collection metadata loaded from {@code TableRow} {@code tr}. If
     * the URI of the collection is empty or {@code null}, then {@code null} is returned.
     */
    private CollectionMetadata getCollectionMetadata(TableRow tr) {
        if (tr!=null) {
            String uri = ResourceParser.parseResourceURI(tr.getFieldStringValue(CFN.SKOS_COLLECTION.field()), this.collectionNS);
            if (uri != null) uri = uri.trim();

            if (uri!=null && !uri.isEmpty()) {
                CollectionMetadata collMetadata = new CollectionMetadata();
            
                collMetadata.setURI(uri);

                collMetadata.setConceptsTableName(tr.getFieldStringValue(CFN.SWS_CONCEPT_LIST.field()));
                
                return collMetadata;
            }
        }
        return null;
    }
    
    
    private int addCollectionMembers(CollectionMetadata cm, SKOS thesaurus, boolean inferInverseProperties, boolean inferSuperProperties, boolean incrementalSync) {
        int counter = 0;
        SKOSCollection collection = thesaurus.getCollection(cm.getURI());
        
        // Get collection declarations table
        String tableName = cm.getConceptsTableName();
        Table conceptsTable = this.dataset.getTable(tableName);
        
        // Load the collection URIs
        if (conceptsTable!=null) {
            if (conceptsTable.getHeader()!=null) {
                if (conceptsTable.getHeader().containsField(CFN.SKOS_MEMBER.fieldName())) {
                    Term memberURIField = CFN.SKOS_MEMBER.field();
                    TableRow tr;
                    int bufferSize = 0;
                    while ((tr = conceptsTable.getNextRow()) != null) {
                        String uri = ResourceParser.parseResourceURI(tr.getFieldStringValue(memberURIField), this.conceptNS);
                        
                        if (uri!=null && !uri.isEmpty()) {
                            try {
                                collection.addMember(uri);
                                if (inferInverseProperties) collection.makeRelation(uri, SKOSElementProperty.memberOf);
                                if (inferSuperProperties) {
                                    collection.addRelation(SKOSElementProperty.memberTransitive, uri);
                                    if (inferInverseProperties) collection.makeRelation(uri, SKOSElementProperty.memberOfTransitive);
                                }
                                
                                counter++;
                                bufferSize++;
                            } catch (IllegalArgumentException ex) {}
                        }
                        if (incrementalSync && bufferSize>=this.maxBufferSize) {
                            thesaurus.sync();
                            bufferSize = 0;
                        }
                    }
                    try {
                        // Close concepts table
                        conceptsTable.close();
                    } catch (IOException ex) {}
                }
                else {
                    Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "\"{0}\": does not contain a field named\"{1}\". It will be skipped!", new Object[]{tableName, CFN.SKOS_MEMBER.fieldName()});
                }
            }
            else {
                Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "Table \"{0}\": has no header. It will be skipped!", tableName);
            }
        }
        else {
            Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "\"{0}\": No such table!", tableName);
        }
        return counter;
    }
    
    
    private int addCollectionMembers(SKOS thesaurus, boolean inferInverseProperties, boolean inferSuperProperties, boolean incrementalSync) {
        int counter = 0;
        
        // Get collection declarations table
        String tableName = CTN.SKOS_MEMBER.tableName();
        Table membershipTable = this.dataset.getTable(tableName);
        
        // Load the collection URIs
        if (membershipTable!=null) {
            if (membershipTable.getHeader()!=null) {
                if (membershipTable.getHeader().containsField(CFN.SKOS_COLLECTION.fieldName())) {
                    if (membershipTable.getHeader().containsField(CFN.SKOS_MEMBER.fieldName())) {
                        
                        String currentCollectionUri = null;
                        SKOSCollection collection = null;
                        
                        Term collectionURIField = CFN.SKOS_COLLECTION.field();
                        Term memberURIField = CFN.SKOS_MEMBER.field();
                        TableRow tr;
                        int bufferSize = 0;
                        while ((tr = membershipTable.getNextRow()) != null) {
                            String collectionUri = ResourceParser.parseResourceURI(tr.getFieldStringValue(collectionURIField), this.collectionNS);
                            if (collectionUri!=null) {
                                if (!collectionUri.equals(currentCollectionUri)) {
                                    currentCollectionUri = collectionUri;
                                    collection = thesaurus.getCollection(currentCollectionUri);
                                }
                                if (collection != null) {
                                    String uri = ResourceParser.parseResourceURI(tr.getFieldStringValue(memberURIField), this.conceptNS);
                                    if (uri!=null && !uri.isEmpty()) {
                                        try {
                                            collection.addMember(uri);
                                            if (inferInverseProperties) collection.makeRelation(uri, SKOSElementProperty.memberOf);
                                            if (inferSuperProperties) {
                                                collection.addRelation(SKOSElementProperty.memberTransitive, uri);
                                                if (inferInverseProperties) collection.makeRelation(uri, SKOSElementProperty.memberOfTransitive);
                                            }
                                            counter++;
                                            bufferSize++;
                                        } catch (IllegalArgumentException ex) {}
                                    }
                                    if (incrementalSync && bufferSize>=this.maxBufferSize) {
                                        thesaurus.sync();
                                        bufferSize = 0;
                                    }
                                }
                            }
                        }
                        try {
                            // Close concepts table
                            membershipTable.close();
                        } catch (IOException ex) {}
                    }
                    else {
                        Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "\"{0}\": does not contain a field named\"{1}\". It will be skipped!", new Object[]{tableName, CFN.SKOS_MEMBER.fieldName()});
                    }
                }
                else {
                    Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "\"{0}\": does not contain a field named\"{1}\". It will be skipped!", new Object[]{tableName, CFN.SKOS_COLLECTION.fieldName()});
                }
            }
            else {
                Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "Table \"{0}\": has no header. It will be skipped!", tableName);
            }
        }
        else {
            Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "No \"{0}\": table found!", tableName);
        }
        return counter;
    }
    
    private int addCollectionCollectionMembers(SKOS thesaurus, boolean inferInverseProperties, boolean inferSuperProperties, boolean incrementalSync) {
        int counter = 0;
        
        // Get collection declarations table
        String tableName = CTN.SKOS_MEMBER_COLLECTION.tableName();
        Table collectionMembershipTable = this.dataset.getTable(tableName);
        
        // Load the collection URIs
        if (collectionMembershipTable!=null) {
            if (collectionMembershipTable.getHeader()!=null) {
                if (collectionMembershipTable.getHeader().containsField(CFN.SKOS_COLLECTION.fieldName())) {
                    if (collectionMembershipTable.getHeader().containsField(CFN.SKOS_MEMBER.fieldName())) {
                        
                        String currentCollectionUri = null;
                        SKOSCollection collection = null;
                        
                        Term collectionURIField = CFN.SKOS_COLLECTION.field();
                        Term memberURIField = CFN.SKOS_MEMBER.field();
                        TableRow tr;
                        int bufferSize = 0;
                        while ((tr = collectionMembershipTable.getNextRow()) != null) {
                            String collectionUri = ResourceParser.parseResourceURI(tr.getFieldStringValue(collectionURIField), this.collectionNS);
                            if (collectionUri!=null) {
                                if (!collectionUri.equals(currentCollectionUri)) {
                                    currentCollectionUri = collectionUri;
                                    collection = thesaurus.getCollection(currentCollectionUri);
                                }
                                if (collection != null) {
                                    String uri = ResourceParser.parseResourceURI(tr.getFieldStringValue(memberURIField), this.collectionNS);
                                    if (uri!=null && !uri.isEmpty()) {
                                        try {
                                            collection.addMember(uri);
                                            if (inferInverseProperties) collection.makeRelation(uri, SKOSElementProperty.memberOf);
                                            if (inferSuperProperties) {
                                                collection.addRelation(SKOSElementProperty.memberTransitive, uri);
                                                if (inferInverseProperties) collection.makeRelation(uri, SKOSElementProperty.memberOfTransitive);
                                            }
                                            counter++;
                                            bufferSize++;
                                        } catch (IllegalArgumentException ex) {}
                                    }
                                    if (incrementalSync && bufferSize>=this.maxBufferSize) {
                                        thesaurus.sync();
                                        bufferSize = 0;
                                    }
                                }
                            }
                        }
                        try {
                            // Close concepts table
                            collectionMembershipTable.close();
                        } catch (IOException ex) {}
                    }
                    else {
                        Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "\"{0}\": does not contain a field named\"{1}\". It will be skipped!", new Object[]{tableName, CFN.SKOS_MEMBER.fieldName()});
                    }
                }
                else {
                    Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "\"{0}\": does not contain a field named\"{1}\". It will be skipped!", new Object[]{tableName, CFN.SKOS_COLLECTION.fieldName()});
                }
            }
            else {
                Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "Table \"{0}\": has no header. It will be skipped!", tableName);
            }
        }
        else {
            Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "No \"{0}\": table found!", tableName);
        }
        return counter;
    }
    
    private List<String> getRelationshipTables() {
        List<String> relationshipTables = new ArrayList<>();
        Table relTable = this.dataset.getTable(CTN.SKOS_SEMANTIC_RELATION.tableName());
        
        if (relTable != null) {
            Header header = relTable.getHeader();
            Term field = CFN.SWS_TABLE.field();
            
            if (header!=null && header.containsField(field)) {
                TableRow tr;
                while ((tr = relTable.getNextRow()) != null) {
                    String tableName = tr.getFieldStringValue(field);
                    if (tableName!=null && !(tableName=tableName.trim()).isEmpty()) {
                        if (!relationshipTables.contains(tableName)) relationshipTables.add(tableName);
                    }
                }
            }
            try {
                relTable.close();
            } catch (IOException ex) {}
        }
        
        
        return relationshipTables;
    }
 
    private int addRelationships(String relTableName, SKOS thesaurus, boolean inferInverseProperties, boolean inferSuperProperties, boolean incrementalSync) {
        int counter = 0;
        Table table = this.dataset.getTable(relTableName);
        
        if (table!=null) {
            Term subjectField = CFN.RDF_SUBJECT.field();
            Term predicateField = CFN.RDF_PREDICATE.field();
            Term objectField = CFN.RDF_OBJECT.field();
            Header header = table.getHeader();
            
            if (header!=null && header.containsField(subjectField) && header.containsField(predicateField) && header.containsField(objectField)) {
                                
                String currentResourceURI = null;
                SKOSResource currentResource = null;

                TableRow tr;
                int bufferSize = 0;
                while ((tr = table.getNextRow()) != null) {
                    String subjectURI = ResourceParser.parseResourceURI(tr.getFieldStringValue(subjectField), this.conceptNS);
                    String objectPropStr = tr.getFieldStringValue(predicateField);
                    String objectURI = ResourceParser.parseResourceURI(tr.getFieldStringValue(objectField), this.conceptNS);
                    
                    // Skip null or empty values
                    if (subjectURI!=null && !subjectURI.isEmpty()
                            && objectPropStr!=null && !(objectPropStr=objectPropStr.trim()).isEmpty()
                            && objectURI!=null && !objectURI.isEmpty()) {
                        
                        
                        SKOSSemanticProperty objectProperty = SKOSSemanticProperty.fromString(objectPropStr);

                        if (objectProperty != null) {
                                
                            if (!subjectURI.equals(currentResourceURI)) {
                                // This is a new collection
                                currentResourceURI = subjectURI;
                                currentResource = thesaurus.getConcept(currentResourceURI);
                            }

                            if (currentResource!=null) {
                                currentResource.addRelation(objectProperty, objectURI);
                                counter++;
                                bufferSize++;
                                // If inferencing enabled infer inverse and super properties
                                if (inferInverseProperties) {
                                    SKOSObjectProperty inverse = objectProperty.getInverseProperty();
                                    currentResource.makeRelation(objectURI, inverse);
                                    counter++;
                                    bufferSize++;
                                }
                                if (inferSuperProperties) {
                                    for (SKOSObjectProperty superProp: objectProperty.getSuperProperties()) {
                                        currentResource.addRelation(superProp, objectURI);
                                        counter++;
                                        bufferSize++;
                                    }
                                    if (inferInverseProperties) {
                                        SKOSObjectProperty inverse = objectProperty.getInverseProperty();
                                        for (SKOSObjectProperty inverseSuperProp: inverse.getSuperProperties()) {
                                            currentResource.makeRelation(objectURI, inverseSuperProp);
                                            counter++;
                                            bufferSize++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    if (incrementalSync && bufferSize>=this.maxBufferSize) {
                        thesaurus.sync();
                        bufferSize = 0;
                    }
                    
                }
            }
            else {
                Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "Table \"{0}\" does not have the required fields rdf:subject, rdf:predicate, and rdf:object. It will be skipped!", relTableName);
            }
            
            // Close table
            try {
                table.close();
            } catch (IOException ex) {}
            
        }
        else {
            Logger.getLogger(TabularExtractor.class.getName()).log(Level.WARNING, "\"{0}\": No such table!", relTableName);
        }
        return counter;
    }
    
    
    private int inferTransitiveClosureForSemanticRelationships(SKOS thesaurus, boolean incrementalSync) {
        int before = 0;
        int after = 0;
        
        JenaSKOS subthesaurus = SKOSFactory.createDefaultSKOSThesaurus();
        //JenaSKOSThesaurus infThesaurus = SKOSThesaurusFactory.buildInferencingSKOSThesaurus(OntModelSpec.OWL_MEM_MINI_RULE_INF);
        
        
        Resource skosConceptTypeRes = subthesaurus.getResource(SKOSType.Concept.uri());
        
        CloseableIterator<SKOSConcept> coneptIter = thesaurus.listConcepts();

        
        if (this.verbose) System.out.println("  - Extracting required statements...");
        
        int numConcepts = 0;
        while (coneptIter.hasNext()) {
            SKOSConcept concept = coneptIter.next();
            subthesaurus.createResource(concept.getURI(), skosConceptTypeRes);
            numConcepts++;
        }
        
        coneptIter.close();
        CloseableIterator<SKOSConcept> iter = thesaurus.listConcepts();
        while (iter.hasNext()) {
            SKOSConcept concept = iter.next();
            
            if (concept != null) {
                SKOSConcept infConcept = subthesaurus.getConcept(concept.getURI());
                if (infConcept != null) {
                    CloseableIterator<SKOSResource> narrowerIter = concept.listRelations(SKOSSemanticProperty.narrowerTransitive);
                    while (narrowerIter.hasNext()) {
                        SKOSResource relatedResource = narrowerIter.next();
                        if (relatedResource != null) {
                            infConcept.addRelation(SKOSSemanticProperty.narrowerTransitive, relatedResource.getURI());
                            before++;
                        }
                        else if (this.verbose) System.out.println("WARNING: Found a null related concept for \""+concept.getURI()+"\"!");
                    }
                    narrowerIter.close();
                }
            }
            else {
                if (this.verbose) System.out.println("WARNING: Found null concept!");
            }
        }
        iter.close();
        if (verbose) System.out.println("      --> Done extracting: Copied "+numConcepts+" concept(s) and "+before+" relationships(s).");
        
        if (this.verbose) System.out.println("  - Computing inferences for transitive relationships...");
        JenaSKOS infThesaurus = SKOSFactory.createInferencingSKOSThesaurus(subthesaurus, OntModelSpec.OWL_MEM_MINI_RULE_INF);
        
        if (verbose) System.out.println("      --> Inferences computed.");
        if (verbose) System.out.println("  - Adding Inferred relationships to thesaurus...");
        
        Iterator<SKOSConcept> infIter = infThesaurus.listConcepts();
        while (infIter.hasNext()) {
            SKOSConcept infConcept = infIter.next();
            SKOSConcept concept = thesaurus.getConcept(infConcept.getURI());
            if (concept != null) {
                CloseableIterator<SKOSResource> relIter = infConcept.listRelations(SKOSSemanticProperty.narrowerTransitive);
                while (relIter.hasNext()) {
                    SKOSResource relatedResource = relIter.next();
                    if (!concept.hasRelation(SKOSSemanticProperty.narrowerTransitive, relatedResource.getURI())) {
                        concept.addRelation(SKOSSemanticProperty.narrowerTransitive, relatedResource.getURI());
                        concept.makeRelation(relatedResource.getURI(), SKOSSemanticProperty.broaderTransitive);
                        concept.addRelation(SKOSSemanticProperty.semanticRelation, relatedResource.getURI());
                        concept.makeRelation(relatedResource.getURI(), SKOSSemanticProperty.semanticRelation);
                        after+=4;
                    }
                }
                relIter.close();
                //if (incrementalSync) thesaurus.sync();
            }
        }
        
        subthesaurus.close();
        infThesaurus.close();
        if (verbose) System.out.println("      --> Added "+(after-before)+" inferred relationships.");
        
        return after-before;
    }
    
    private int inferTransitiveClosureForCollectionMembership(SKOS thesaurus, boolean incrementalSync) {
        int before = 0;
        int after = 0;
        
        JenaSKOS subthesaurus = SKOSFactory.createDefaultSKOSThesaurus();
        //JenaSKOSThesaurus infThesaurus = SKOSThesaurusFactory.buildInferencingSKOSThesaurus(OntModelSpec.OWL_MEM_MINI_RULE_INF);
        
        
        Resource skosCollectionTypeRes = subthesaurus.getResource(SKOSType.Collection.uri());
        
        CloseableIterator<SKOSCollection> collectionIter = thesaurus.listCollections();

        
        if (this.verbose) System.out.println("  - Extracting required statements...");
        
        int numCollections = 0;
        while (collectionIter.hasNext()) {
            SKOSCollection collection = collectionIter.next();
            subthesaurus.createResource(collection.getURI(), skosCollectionTypeRes);
            numCollections++;
        }
        
        collectionIter.close();
        
        CloseableIterator<SKOSCollection> iter = thesaurus.listCollections();
        while (iter.hasNext()) {
            SKOSCollection collection = iter.next();
            
            if (collection != null) {
                SKOSCollection infCollection = subthesaurus.getCollection(collection.getURI());
                if (infCollection != null) {
                    CloseableIterator<SKOSResource> memberIter = collection.listRelations(SKOSElementProperty.member);
                    while (memberIter.hasNext()) {
                        SKOSResource memberResource = memberIter.next();
                        if (memberResource != null) {
                            if (memberResource.isSKOSCollection()) subthesaurus.createCollection(memberResource.getURI());
                            else if (memberResource.isSKOSConcept()) subthesaurus.createConcept(memberResource.getURI());
                            
                            infCollection.addRelation(SKOSElementProperty.memberTransitive, memberResource.getURI());
                            before++;
                        }
                        else if (this.verbose) System.out.println("WARNING: Found a null member resource for \""+collection.getURI()+"\"!");
                    }
                    memberIter.close();
                }
            }
            else {
                if (this.verbose) System.out.println("WARNING: Found null concept!");
            }
        }
        iter.close();
        if (verbose) System.out.println("      --> Done extracting: Copied "+numCollections+" collections(s) and "+before+" member(s).");
        
        if (this.verbose) System.out.println("  - Computing inferences for transitive member relationships...");
        JenaSKOS infThesaurus = SKOSFactory.createInferencingSKOSThesaurus(subthesaurus, OntModelSpec.OWL_MEM_MINI_RULE_INF);
        
        if (verbose) System.out.println("      --> Inferences computed.");
        if (verbose) System.out.println("  - Adding Inferred relationships to thesaurus...");
        
        CloseableIterator<SKOSCollection> infIter = infThesaurus.listCollections();
        while (infIter.hasNext()) {
            SKOSCollection infCollection = infIter.next();
            SKOSCollection collection = thesaurus.getCollection(infCollection.getURI());
            if (collection != null) {
                CloseableIterator<SKOSResource> relIter = infCollection.listRelations(SKOSElementProperty.memberTransitive);
                while (relIter.hasNext()) {
                    SKOSResource relatedResource = relIter.next();
                    if (!collection.hasRelation(SKOSElementProperty.memberTransitive, relatedResource.getURI())) {
                        collection.addRelation(SKOSElementProperty.memberTransitive, relatedResource.getURI());
                        collection.makeRelation(relatedResource.getURI(), SKOSElementProperty.memberOfTransitive);
                        after+=2;
                    }
                }
                relIter.close();
                //if (incrementalSync) thesaurus.sync();
            }
        }
        infIter.close();
        subthesaurus.close();
        infThesaurus.close();
        if (verbose) System.out.println("      --> Added "+(after-before)+" inferred membership relationships.");
        
        return after-before;
    }
    
}
