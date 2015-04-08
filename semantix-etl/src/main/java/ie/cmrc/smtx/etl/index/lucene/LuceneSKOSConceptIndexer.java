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

package ie.cmrc.smtx.etl.index.lucene;

import ie.cmrc.smtx.lucene.analysis.LanguageBasedAnalyzer;
import ie.cmrc.smtx.skos.model.SKOS;
import ie.cmrc.smtx.skos.model.SKOSAnnotationProperty;
import ie.cmrc.smtx.skos.model.SKOSCollection;
import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSConceptScheme;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import ie.cmrc.smtx.skos.index.IndexField;
import ie.cmrc.smtx.skos.index.lucene.analysis.SKOSAnalyzerFactory;
import ie.cmrc.smtx.etl.index.SKOSIndexer;
import ie.cmrc.smtx.skos.model.SKOSCollectionMember;
import ie.cmrc.util.Term;
import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 * A {@link ie.cmrc.smtx.etl.index.SKOSIndexer} implementation that indexes
 * concepts only
 * @author Yassine Lassoued
 */
public final class LuceneSKOSConceptIndexer implements SKOSIndexer {

    public static final boolean DEFAULT_VERBOSE = true;
    
    public static final boolean DEFAULT_INDEX_TRANSITIVE_COLLECTIONS = true;
    
    /**
     * If set to {@code true} the indexer will print processing information to the standard output
     */
    private boolean verbose;
    
    /**
     * If set to {@code true} the indexer will index transitive collection
     * membership
     */
    private final boolean indexTransitiveCollections;
    
    /**
     * List of languages for which the indexer will index the concept labels and definitions 
     */
    private List<String> languages;

    /**
     * Maps searchable index fields to SKOS annotation properties
     */
    private static final Map<SKOSAnnotationProperty, IndexField.Searchable[]> annotationFields;
    
    /**
     * Specifies whether annotations should be stored for each SKOSAnnotationProperty
     */
    private static final Map<SKOSAnnotationProperty, Field.Store[]> annotationStoring;
    
    static {
        
        annotationFields = new HashMap<>(4);
        annotationFields.put(SKOSAnnotationProperty.prefLabel, new IndexField.Searchable[]{IndexField.Searchable.PREF_LABEL, IndexField.Searchable.LABEL, IndexField.Searchable.IX_PREF_LABEL, IndexField.Searchable.IX_LABEL});
        annotationFields.put(SKOSAnnotationProperty.altLabel, new IndexField.Searchable[]{IndexField.Searchable.ALT_LABEL, IndexField.Searchable.LABEL, IndexField.Searchable.IX_ALT_LABEL, IndexField.Searchable.IX_LABEL});
        annotationFields.put(SKOSAnnotationProperty.hiddenLabel, new IndexField.Searchable[]{IndexField.Searchable.HIDDEN_LABEL, IndexField.Searchable.LABEL, IndexField.Searchable.IX_HIDDEN_LABEL, IndexField.Searchable.IX_LABEL});
        annotationFields.put(SKOSAnnotationProperty.definition, new IndexField.Searchable[]{IndexField.Searchable.DEFINITION});
        
        annotationStoring = new HashMap<>(4);
        annotationStoring.put(SKOSAnnotationProperty.prefLabel, new Field.Store[]{Field.Store.YES, Field.Store.NO, Field.Store.NO, Field.Store.NO});
        annotationStoring.put(SKOSAnnotationProperty.altLabel, new Field.Store[]{Field.Store.YES, Field.Store.NO, Field.Store.NO, Field.Store.NO});
        annotationStoring.put(SKOSAnnotationProperty.hiddenLabel, new Field.Store[]{Field.Store.YES, Field.Store.NO, Field.Store.NO, Field.Store.NO});
        annotationStoring.put(SKOSAnnotationProperty.definition, new Field.Store[]{Field.Store.NO});
        
    }
    
    /**
     * Constructs a {@code MultilingualSKOSIndexer} that supports the provided
     * languages
     * @param languageCodes List of two-letter codes of languages to support
     * by the indexer. If this is {@code null} or empty, then all annotations
     * in all languages are indexed.
     */
    public LuceneSKOSConceptIndexer(List<String> languageCodes) {
        this.setLanguages(languageCodes);
        this.verbose = DEFAULT_VERBOSE;
        this.indexTransitiveCollections = DEFAULT_INDEX_TRANSITIVE_COLLECTIONS;
    }
    
    /**
     * Constructs a {@code MultilingualSKOSIndexer} that supports the provided
     * languages
     * @param languageCodes List of two-letter codes of languages to support
     * by the indexer. If this is {@code null} or empty, then all annotations
     * in all languages are indexed.
     * @param verbose If set to {@code true} the indexer will write progress
     * information to the standard output
     */
    public LuceneSKOSConceptIndexer(List<String> languageCodes, boolean verbose) {
        this.setLanguages(languageCodes);
        this.verbose = verbose;
        this.indexTransitiveCollections = DEFAULT_INDEX_TRANSITIVE_COLLECTIONS;
    }
    
    /**
     * Constructs a {@code MultilingualSKOSIndexer} that supports the provided
     * languages
     * @param languageCodes List of two-letter codes of languages to support
     * by the indexer. If this is {@code null} or empty, then all annotations
     * in all languages are indexed.
     * @param verbose If set to {@code true} the indexer will write progress
     * information to the standard output
     * @param indexTransitiveCollections If set to {@code true} the indexer
     * will index transitive collection membership
     */
    public LuceneSKOSConceptIndexer(List<String> languageCodes, boolean verbose, boolean indexTransitiveCollections) {
        this.verbose = verbose;
        this.setLanguages(languageCodes);
        this.indexTransitiveCollections = indexTransitiveCollections;
    }
    
    /**
     * Indicates whether the indexer is in the verbose mode
     * @return {@code true} if the indexer is in the verbose mode; {@code false}
     * otherwise
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Indicates whether transitive collection membership is indexed
     * @return {@code true} if the transitive collection memberships are indexed
     */
    public boolean indexTransitiveCollections() {
        return indexTransitiveCollections;
    }
    
    /**
     * Returns the list of languages supported by the indexer
     * @return List of language codes supported by the indexer.
     */
    public List<String> getLanguages() {
        return languages;
    }

    /**
     * Sets the verbose mode for the indexer
     * @param verbose if set to {@code true} then the indexer will print progress
     * information to the standard output
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    /**
     * Sets the index languages
     * @param languageCodes List of languages to use for indexing thesauri. Use {@code null}
     * or an empty list to support all languages in the thesaurus.
     */
    public void setLanguages(List<String> languageCodes) {
        if (languageCodes!=null && !languageCodes.isEmpty()) this.languages = languageCodes;
        else this.languages = new ArrayList<>(LanguageBasedAnalyzer.SUPPORTED_LANGUAGES);
    }
    
    /**
     * {@inheritDoc}
     * @param thesaurus {@inheritDoc}
     * @param indexDirFile Output index directory
     * @return {@inheritDoc}
     * @throws NotDirectoryException {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public boolean indexSKOSThesaurus(SKOS thesaurus, File indexDirFile) throws NotDirectoryException, IOException {
        boolean success = false;
        
        if (thesaurus != null) {
            if (indexDirFile!=null) {
                
                // Check index directory
                if (!indexDirFile.exists()) {
                    // Index directory  does not exist, create it
                    if (verbose) System.out.println("  - Directory \""+indexDirFile.getAbsolutePath()+"\" does not exist. Will create it...");
                    boolean created = indexDirFile.mkdirs();
                    if (created) {
                        if (verbose) System.out.println("      --> Created directory \""+indexDirFile.getAbsolutePath()+"\".");
                    }
                    else {
                        if (verbose) System.out.println("    * ERROR: Could not create directory \""+indexDirFile.getAbsolutePath()+"\"!");
                        return false;
                    }
                }
                else {
                    if (indexDirFile.isDirectory()) {
                        // Index directory exists and is actually a directory
                        // Clear content
                        for (File f:indexDirFile.listFiles()) {
                            if (!f.delete() && verbose) System.out.println("    * WARNING: Could not delete file \""+f.getAbsolutePath()+"\"!");
                        }
                    }
                    else {
                        // A file exists with the same name but is not a directory
                        if (verbose) System.out.println("    * ERROR: File \""+indexDirFile.getAbsolutePath()+"\" exists but is not a directory!");
                        throw new NotDirectoryException(indexDirFile.getAbsolutePath());
                    }
                }
                
                // Initialise analyser
                if (verbose) System.out.println("  - Initialising index analysers...");
                
                Analyzer analyser = SKOSAnalyzerFactory.createSmartSKOSMultilingualAnalyser(this.languages);
                if (verbose) System.out.println("      --> Analysers intialised.");
                
                // Configure index writer and connect to index directory
                if (verbose) System.out.println("  - Configuring index writer and connecting to index directory...");
                IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, analyser);
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                IndexWriter indexWriter = new IndexWriter(new SimpleFSDirectory(indexDirFile), iwc);
                if (verbose) System.out.println("      --> Done configuring and connecting index writer.");
                
                if (verbose) System.out.println("  - Indexing concepts. Please be patient as this may take a few minutes...");
                int counter = 0;
                int errors = 0;
                CloseableIterator<SKOSConcept> iter = thesaurus.listConcepts();
                while (iter.hasNext()) {
                    counter++;
                    SKOSConcept concept = iter.next();
                    Document doc = this.indexConcept(concept);
                    if (doc != null) {
                        try {
                            indexWriter.addDocument(doc);
                        }
                        catch (IOException ex) {
                            errors++;
                            if (verbose) System.out.println("    * ERROR: Could not index concept \""+concept.getURI()+"\"!");
                        }
                    }
                }
                iter.close();
                if (verbose) System.out.println("      --> Indexed "+counter+" concepts(s) with "+errors+" error(s).");
                
                if (verbose) System.out.println("  - Closing connection to index directory...");
                indexWriter.close();
                if (verbose) System.out.println("      --> Closed connection.");
                
                success = true;
            }
            else {
                throw new IllegalArgumentException("Index directory is null");
            }
        }
        else {
            throw new IllegalArgumentException("Thesaurus parameter is null");
        }
        
        return success;
    }
    
    /**
     * Extracts the relevant information and annotations about the provided
     * {@link ie.cmrc.skos.SKOSConcept} and stores them in an index document
     * {@code org.apache.lucene.document.Document} ready to be added to an
     * index writer
     * @param concept Concept to index
     * @return {@code org.apache.lucene.document.Document} containing the
     * relevant fields and values to index.
     */
    private Document indexConcept(SKOSConcept concept) {
        Document doc = new Document();
        
        String uri = concept.getURI();
        doc.add(new TextField(IndexField.Searchable.URI.fieldName(), uri, Field.Store.YES));
        
        String name = concept.getLocalName();
        doc.add(new TextField(IndexField.Searchable.NAME.fieldName(), name, Field.Store.YES));
        
        List<SKOSConceptScheme> css = concept.getConceptSchemes();
        for (SKOSConceptScheme cs: css) {
            doc.add(new StringField(IndexField.Filterable.CS.fieldName(), cs.getURI(), Field.Store.YES));
        }
        
        SKOSCollectionMember member = (SKOSCollectionMember)concept;
        List<SKOSCollection> collections = member.getCollections();
        for (SKOSCollection collection: collections) {
            doc.add(new StringField(IndexField.Filterable.COLLECTION.fieldName(), collection.getURI(), Field.Store.YES));
        }

        if (this.indexTransitiveCollections) {
            List<SKOSCollection> transCollections = member.getCollectionsTransitive();
            for (SKOSCollection collection: transCollections) {
                doc.add(new StringField(IndexField.Filterable.COLLECTION_TRANSITIVE.fieldName(), collection.getURI(), Field.Store.YES));
            }
        }
        
        
        
        Set<SKOSAnnotationProperty> annotationProps = annotationFields.keySet();
        for (SKOSAnnotationProperty annotationProp: annotationProps) {
            List<Term> annotations = concept.getAnnotations(annotationProp);
            for (Term annotation: annotations) {
                String lang = annotation.getLanguage();
                
                IndexField.Searchable[] fields = annotationFields.get(annotationProp);
                Field.Store[] stores = annotationStoring.get(annotationProp);
                
                for (int i=0; i<fields.length; i++) {
                    IndexField.Searchable searchableField = fields[i];
                    Field.Store store = stores[i];
                    doc.add(new TextField(searchableField.field(lang).getQualifiedString(), annotation.getString(), store));
                }
            }
        }
        
        return doc;
    }
    
    
}
