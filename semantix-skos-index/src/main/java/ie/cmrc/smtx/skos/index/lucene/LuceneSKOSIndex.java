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

package ie.cmrc.smtx.skos.index.lucene;

import ie.cmrc.smtx.base.SemanticEntity;
import ie.cmrc.smtx.skos.index.AbstractSKOSIndex;
import ie.cmrc.smtx.skos.index.IndexField;
import ie.cmrc.smtx.skos.index.SKOSIndex;
import ie.cmrc.smtx.skos.index.Scored;
import ie.cmrc.smtx.skos.index.lucene.analysis.SKOSAnalyzerFactory;
import ie.cmrc.util.Term;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 * A persistent {@link ie.cmrc.skos.index.SKOSIndex} implementation based on the
 * Lucene indexer. This implementation stores its content in a directory on the
 * file system. For performance reasons, if your index is unchanging, you should
 * share a single LuceneSKOSIndex instance across multiple searches instead of
 * creating a new one per-search.<br/>
 * Currently this SKOSIndex uses a predefined set of Lucene analysers.
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class LuceneSKOSIndex extends AbstractSKOSIndex implements SKOSIndex {
    
    /**
     * Path to the index data directory
     */
    private final String indexDir;
    
    /**
     * Index directory object
     */
    private Directory dir = null;
    
    /**
     * Index reader
     */
    private IndexReader indexReader = null;
    
    /**
     * Index searcher
     */
    private IndexSearcher indexSearcher = null;
    
    /**
     * Lucene analyser to use for analysing queries
     */
    private final Analyzer analyser;
    
    /**
     * Indicates whether the index has been successfully initialised
     */
    private boolean initialised = false;
    

    /**
     * Creates a {@link LuceneSKOSIndex} with the provided index data directory
     * and supporting the provided languages
     * @param indexDir Index data directory
     * @param languages Languages supported by the index
     * @throws IllegalArgumentException If the path to the index directory is
     * {@code null} or empty
     * @throws FileNotFoundException If the provided index directory does not exist
     * @throws NotDirectoryException If the provided path is not that of a directory
     * @throws CorruptIndexException If the index is corrupt
     * @throws IOException If an IO error is encountered
     */
    public LuceneSKOSIndex(String indexDir, Collection<String> languages) throws IllegalArgumentException, FileNotFoundException, NotDirectoryException, CorruptIndexException, IOException {
        super(languages);
        this.indexDir = indexDir;
        
        if (this.indexDir!=null && !this.indexDir.isEmpty()) {
            /*if (languages!=null && languages.size()==1) {
                this.analyser = AnalyzerFactory.createLanguageBasedAnalyser(this.languages.iterator().next());
            }
            else {
                this.analyser = AnalyzerFactory.createPerFieldMultilingualAnalyser(this.languages);
            }*/
            this.analyser = SKOSAnalyzerFactory.createSmartSKOSMultilingualAnalyser(this.languages);
            
            File dirFile = new File(indexDir);
            
            if (dirFile.exists()) {
                if (dirFile.isDirectory()) {
                    try {
                        dir = new SimpleFSDirectory(dirFile);

                        try {
                            this.indexReader = DirectoryReader.open(dir);
                            this.indexSearcher = new org.apache.lucene.search.IndexSearcher(indexReader);
                            this.initialised = true;
                        }
                        catch(CorruptIndexException ex) {
                            dir.close();
                            throw ex;
                        }
                        catch(IOException ex) {
                            dir.close();
                            throw ex;
                        }

                    } catch (IOException ex) {
                        throw ex;
                    }
                }
                else {
                    // File is not a directory
                    throw new NotDirectoryException("\""+dirFile.getAbsolutePath()+"\" exists but is not a directory.");
                }
            }
            else {
                // File does not exist
                throw new FileNotFoundException("\""+dirFile.getAbsolutePath()+"\": No such file or directory.");
            }
            
        }
        else {
            // Illegal argument
            throw new IllegalArgumentException("Index direcotry path is null or empty");
        }
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SemanticEntity getConceptByURI(String conceptURI) {
        List<Scored<SemanticEntity>> concepts = this.search(new Term(conceptURI, null), IndexField.Searchable.URI, 0, 1);
        if (concepts!=null && !concepts.isEmpty()) return concepts.get(0).getItem();
        else return null;
    }

    /**
     * {@inheritDoc}
     * @param conceptName {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SemanticEntity> getConceptsByName(String conceptName) {
        List<Scored<SemanticEntity>> scoredEntities =  this.search(new Term(conceptName, null), IndexField.Searchable.NAME, -1, -1);
        List<SemanticEntity> entities = new ArrayList<>(scoredEntities.size());
        for (Scored<SemanticEntity> scoredEntity : scoredEntities) entities.add(scoredEntity.getItem());
        return entities;
    }
    
    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @param collections {@inheritDoc}
     * @param transitiveMember {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws java.text.ParseException {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> query(String queryString, Collection<String> conceptSchemes, Collection<String> collections, boolean transitiveMember, int offset, int limit) throws java.text.ParseException {
        if (queryString != null) {
            QueryParser queryParser = new QueryParser(IndexField.Searchable.NAME.fieldName(), analyser);

            Filter filter = this.getFilterForSchemesAndCollections(conceptSchemes, collections, transitiveMember);

            try {
                //System.out.println("Your query is: "+queryString);
                Query query = queryParser.parse(queryString);
                return this.processQuery(query, filter, offset, limit);

            }
            catch (ParseException ex) {
                throw new java.text.ParseException("Could not parse \""+queryString+"\":"+ex.getMessage(), -1);
            }
        }
        return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param field {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @param collections {@inheritDoc}
     * @param transitiveMember {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> search(Term keyword, IndexField.Searchable field, Collection<String> conceptSchemes, Collection<String> collections, boolean transitiveMember, int offset, int limit) {
        String queryString = this.getQueryString(keyword, field);
        if (queryString != null) {
            QueryParser queryParser = new QueryParser(IndexField.Searchable.NAME.fieldName(), analyser);

            Filter filter = this.getFilterForSchemesAndCollections(conceptSchemes, collections, transitiveMember);

            try {
                //System.out.println("Your query is: "+queryString);
                Query query = queryParser.parse(queryString);
                return this.processQuery(query, filter, offset, limit);
                

            }
            catch (ParseException ex) {
                // This should never happen, but just in case...
                throw new RuntimeException("Could not parse \""+queryString+"\":"+ex.getMessage(), ex);
            }
        }
        return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (this.indexReader != null) {
            try {
                this.indexReader.close();
            } catch (IOException ex) {
                throw new IOException("Could not close index reader for directory \""+this.indexDir+"\"", ex);
            }
        }
        if (this.dir != null) {
            try {
                dir.close();
                System.out.println("Index directory closed successfully");
            } catch (IOException ex) {
                throw new IOException("Could not close directory \""+this.indexDir+"\".", ex);
            }
        }
        this.analyser.close();
    }
    
    /**
     * Constructs a query string for the provided keyword and search field
     * @param keyword A (string keyword, language code) pair
     * @param field Search field
     * @return Lucene query representation of the provided keyword and search field
     */
    private String getQueryString(Term keyword, IndexField.Searchable field) {
        if (keyword != null) {
            String kw = keyword.getString();
            if (kw!=null && (kw=kw.trim()).length()>=this.minKeywordLength) {
                
                String queryString;
                
                kw = QueryParserBase.escape(kw);
                String language = keyword.getLanguage();

                if (language == null) {
                    /*queryString = "("+IndexField.Property.NAME.fieldName()+":\""+kw+"\") OR ("
                            +IndexField.Property.URI.fieldName()+":\""+kw+"\")";*/
                    
                    // Search in all languages
                    queryString = "";
                    int i = 0;
                    for (String lang: this.languages) {
                        if (i>0) queryString += " OR ";
                        queryString +=  this.getQueryString(kw, lang, field);
                        i++;
                    }
                }
                else {
                    queryString = this.getQueryString(kw, language, field);
                }
                
                return queryString;
            }
        }
        return null;
    }
    
    /**
     * Constructs a query string for the provided keyword and search field
     * @param keyword String keyword to fetch
     * @param language Language of the keyword
     * @param field Search field
     * @return Lucene query representation of the provided keyword and search field
     */
    private String getQueryString(String keyword, String language /*non null*/, IndexField.Searchable field) {
        //System.out.println("+++ Received: field = "+field.fieldName());
        String queryString;
        if (field == null) {
            queryString = "("+IndexField.Searchable.PREF_LABEL.field(language).getQualifiedString()+":\""+keyword+"\")^0.5 OR ("
                    +IndexField.Searchable.LABEL.field(language).getQualifiedString()+":\""+keyword+"\")^0.35 OR ("
                    + IndexField.Searchable.URI.field(language).getQualifiedString()+":\""+keyword+"\")^0.5 OR ("
                    + IndexField.Searchable.NAME.field(language).getQualifiedString()+":\""+keyword+"\")^0.5 OR ("
                    + IndexField.Searchable.IX_LABEL.field(language).getQualifiedString()+":\""+keyword+"\")^0.04 OR ("
                    + IndexField.Searchable.DEFINITION.field(language).getQualifiedString()+":\""+keyword+"\")^0.01";
        }
        else {
            if (field.isMultilingual()) queryString = "("+field.field(language).getQualifiedString()+":\""+keyword+"\")";
            else queryString = "("+field.fieldName()+":\""+keyword+"\")";
        }
        //System.out.println("===== Query = "+queryString);
        return queryString;
    }
    
    /**
     * Constructs a Lucene filter for the provided lists of concept schemes and collections
     * @param conceptSchemes List of concept scheme URIs
     * @param collections List of SKOS collection URIs
     * @return Lucene filter for the provided lists of concept schemes and collections
     */
    private Filter getFilterForSchemesAndCollections(Collection<String> conceptSchemes, Collection<String> collections, boolean transitive) {
        Filter filter = null;

        Query filterQuery = null;

        Query csQuery = null;
        Query collectionQuery = null;


        if (conceptSchemes!=null && !conceptSchemes.isEmpty()) {
            if (conceptSchemes.size() == 1) {
                String cs = conceptSchemes.iterator().next();
                org.apache.lucene.index.Term qTerm = new org.apache.lucene.index.Term(IndexField.Filterable.CS.fieldName(), cs);
                csQuery = new TermQuery(qTerm);
                //csQuery = TermRangeQuery.newStringRange(IndexField.Property.CS.fieldName(), cs, cs, true, true);
            }
            else {
                csQuery = new BooleanQuery();
                for (String cs: conceptSchemes) {
                    
                    org.apache.lucene.index.Term qTerm = new org.apache.lucene.index.Term(IndexField.Filterable.CS.fieldName(), cs);
                    TermQuery oneCSQuery = new TermQuery(qTerm);
                    //Query oneCSQuery = TermRangeQuery.newStringRange(IndexField.Property.CS.fieldName(), cs, cs, true, true);
                    ((BooleanQuery)csQuery).add(oneCSQuery, BooleanClause.Occur.SHOULD);
                }
            }
        }

        IndexField.Filterable collectionField;
        if (transitive) collectionField = IndexField.Filterable.COLLECTION_TRANSITIVE;
        else collectionField = IndexField.Filterable.COLLECTION;
        
        if (collections != null && !collections.isEmpty()) {

            if (collections.size() == 1) {
                String collection = collections.iterator().next();
                org.apache.lucene.index.Term qTerm = new org.apache.lucene.index.Term(collectionField.fieldName(), collection);
                collectionQuery = new TermQuery(qTerm);
                //collectionQuery = TermRangeQuery.newStringRange(IndexField.Property.COLLECTION.fieldName(), collection, collection, true, true);
            }
            else {
                /*objectCategoryQuery = new SpanOrQuery();*/
                collectionQuery = new BooleanQuery();
                for (String collection: collections) {
                    org.apache.lucene.index.Term qTerm = new org.apache.lucene.index.Term(collectionField.fieldName(), collection);
                    Query oneCollQuery = new TermQuery(qTerm);
                    //Query oneCollQuery = TermRangeQuery.newStringRange(IndexField.Property.COLLECTION.fieldName(), collection, collection, true, true);
                    ((BooleanQuery)collectionQuery).add(oneCollQuery, BooleanClause.Occur.SHOULD);
                }
            }
        }

        if (csQuery!=null) {
            if (collectionQuery!=null) {
                filterQuery = new BooleanQuery();
                ((BooleanQuery)filterQuery).add(csQuery, BooleanClause.Occur.MUST);
                ((BooleanQuery)filterQuery).add(collectionQuery, BooleanClause.Occur.MUST);
            }
            else {
                filterQuery = csQuery;
            }
        }
        else {
            if (collectionQuery!=null) {
                filterQuery = collectionQuery;
            }
        }

        if (filterQuery != null) filter = new QueryWrapperFilter(filterQuery);
        
        return filter;
    }
    
    /**
     * Processes a Lucene query
     * @param query Lucene query to process
     * @param filter Additional filter for the query
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of semantic entities matching the provided query
     */
    private List<Scored<SemanticEntity>> processQuery(Query query, Filter filter, int offset, int limit) {
        if (this.initialised) {
        
            int qOffset = offset;
            int qLimit = limit;

            if (qOffset<0) qOffset = 0;
            if (qLimit<=0) qLimit = 200;

            List<Scored<SemanticEntity>> scoredConcepts = new ArrayList<>(qLimit);

            TopScoreDocCollector collector = TopScoreDocCollector.create(qOffset+qLimit, true);
            
            try {

                if (filter!=null) {
                    this.indexSearcher.search(query, filter, collector);
                }
                else {
                    this.indexSearcher.search(query, collector);
                }

                ScoreDoc[] hits = collector.topDocs(qOffset).scoreDocs;
                if (hits!=null) {
                    for (ScoreDoc hit : hits) {
                        int docId = hit.doc;
                        Document doc =this.indexSearcher.doc(docId);
                        double score = hit.score;
                        scoredConcepts.add(new Scored<SemanticEntity>(new DocSemanticEntityWrapper(doc), score));
                    }

                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            
            return scoredConcepts;
        }
        else {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Convenience method for getting the concept URI of a provided Lucene document
     * @param doc Lucene document to get the concept URI from
     * @return URI of the concept represented by {@code document}
     */
    private String getConceptURI(Document doc) {
        System.out.println("      - "+doc.get(IndexField.Searchable.URI.fieldName()));
        
        /*String[] values = doc.getValues(IndexField.Searchable.LABEL.field("en").getQualifiedString());
        
        for (String value: values) System.out.println("             = "+value);*/
        return doc.get(IndexField.Searchable.URI.fieldName());
    }

    /**
     * Returns the path to the index data directory
     * @return Path to the index data directory
     */
    public String getIndexDir() {
        return indexDir;
    }

    /**
     * Indicates whether the index has been successfully initialised. It is good
     * practice to check this method before querying the index for the first time.
     * @return {@code true} if the index was properly initialised; {@code false} otherwise
     */
    public boolean isInitialised() {
        return initialised;
    }

    
    
    
}
