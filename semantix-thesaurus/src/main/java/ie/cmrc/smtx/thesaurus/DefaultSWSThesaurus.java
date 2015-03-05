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

package ie.cmrc.smtx.thesaurus;

import ie.cmrc.smtx.base.SemanticEntity;
import ie.cmrc.smtx.skos.model.SKOS;
import ie.cmrc.smtx.skos.model.SKOSCollection;
import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSConceptScheme;
import ie.cmrc.smtx.skos.model.SKOSSemanticProperty;
import ie.cmrc.smtx.skos.model.hierarchy.HierarchyMethod;
import ie.cmrc.smtx.skos.model.hierarchy.SKOSConceptNode;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import ie.cmrc.smtx.skos.model.util.EmptyCloseableIterator;
import ie.cmrc.smtx.skos.index.IndexField;
import ie.cmrc.smtx.skos.index.SKOSIndex;
import ie.cmrc.smtx.skos.index.Scored;
import ie.cmrc.util.Term;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The {@code DefaultSWSThesaurus} interface is a high-level interface that
 * combines the capabilities of SKOS thesauri ({@link ie.cmrc.skos.core.SKOS})
 * and SKOS indexes ({@link ie.cmrc.skos.index.SKOSIndex}). It supports most
 * of the {@link ie.cmrc.skos.core.SKOS} operations but instead of using
 * SKOS resource objects as method parameters, it uses URI references. This
 * is suitable for web based thesauri.
 * 
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class DefaultSWSThesaurus implements SWSThesaurus {

    /**
     * SKOS thesaurus
     */
    protected final SKOS skos;
    
    /**
     * SKOS index used for searches and interpretation
     */
    protected final SKOSIndex index;

    /**
     * Default maximum number of concept matches for keyword interpretation
     */
    public static final int DEFAULT_MAX_MATCHES = 3;
    
    /**
     * Maximum number of concept matches for keyword interpretation
     */
    private int interpretationMaxMatches;
    
    /**
     * Indicates whether related concepts are included in keyword interpretation
     */
    private boolean interpretWithRelatedConcepts;
    
    /**
     * Constructs a {@link DefaultSWSThesaurus} with the provided SKOS thesaurus
     * and SKOS index
     * @param skos SKOS thesaurus
     * @param index SKOS index to use for searching and interpreting keywords
     * and queries
     */
    public DefaultSWSThesaurus(SKOS skos, SKOSIndex index) {
        this.skos = skos;
        this.index = index;
        this.interpretationMaxMatches = DEFAULT_MAX_MATCHES;
        this.interpretWithRelatedConcepts = false;
    }

    /**
     * Maximum number of concept matches for keyword interpretation
     * @return Maximum number of concept matches for keyword interpretation
     */
    public int getInterpretationMaxMatches() {
        return interpretationMaxMatches;
    }

    /**
     * Indicates whether related concepts are included in keyword interpretation
     * @return {@code true} if related concepts are included in keyword
     * interpretation
     */
    public boolean isInterpretWithRelatedConcepts() {
        return interpretWithRelatedConcepts;
    }

    /**
     * Sets the maximum number of concept matches for keyword interpretation
     * @param interpretationMaxMatches Maximum number of concept matches for
     * keyword interpretation
     */
    public void setInterpretationMaxMatches(int interpretationMaxMatches) {
        this.interpretationMaxMatches = interpretationMaxMatches;
    }

    /**
     * Specifies whether related concepts should be included in keyword
     * interpretation
     * @param interpretWithRelatedConcepts Use {@code true} if you require
     * related concepts to be included in keyword interpretation
     */
    public void setInterpretWithRelatedConcepts(boolean interpretWithRelatedConcepts) {
        this.interpretWithRelatedConcepts = interpretWithRelatedConcepts;
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConceptScheme> listConceptSchemes() {
        return this.skos.listConceptSchemes();
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConceptScheme getConceptScheme(String conceptSchemeURI) {
        return this.skos.getConceptScheme(conceptSchemeURI);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollections() {
        return this.skos.listCollections();
    }
    
    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollections(String conceptSchemeURI) {
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if (conceptSchemeURI==null || conceptScheme!=null) {
            return this.skos.listCollections(conceptScheme);
        }
        return new EmptyCloseableIterator<>();
    }
    
    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollections(String conceptSchemeURI, String skosCollectionURI) {
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if (conceptSchemeURI==null || conceptScheme!=null) {
            SKOSCollection skosCollection = this.skos.getCollection(skosCollectionURI);
            if (skosCollectionURI==null || skosCollection!=null) {
                return this.skos.listCollections(conceptScheme, skosCollection);
            }
        }
        return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSCollection getCollection(String skosCollectionURI) {
        return this.skos.getCollection(skosCollectionURI);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listTopConcepts() {
        return this.skos.listTopConcepts();
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listTopConcepts(String conceptSchemeURI) {
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if (conceptSchemeURI==null || conceptScheme!=null) {
            return this.skos.listTopConcepts(conceptScheme);
        }
        return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listTopConcepts(String conceptSchemeURI, String skosCollectionURI) {
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if (conceptSchemeURI==null || conceptScheme!=null) {
            SKOSCollection skosCollection = this.skos.getCollection(skosCollectionURI);
            if (skosCollectionURI==null || skosCollection!=null) {
                return this.skos.listTopConcepts(conceptScheme, skosCollection);
            }
        }
        return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getBroadestConcepts() {
        return this.skos.getBroadestConcepts();
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getBroadestConcepts(String conceptSchemeURI) {
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if (conceptSchemeURI==null || conceptScheme!=null) {
            return this.skos.getBroadestConcepts(conceptScheme);
        }
        return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getBroadestConcepts(String conceptSchemeURI, String skosCollectionURI) {
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if (conceptSchemeURI==null || conceptScheme!=null) {
            SKOSCollection skosCollection = this.skos.getCollection(skosCollectionURI);
            if (skosCollectionURI==null || skosCollection!=null) {
                return this.skos.getBroadestConcepts(conceptScheme, skosCollection);
            }
        }
        return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listConcepts() {
        return this.skos.listConcepts();
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listConcepts(String conceptSchemeURI) {
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if (conceptSchemeURI==null || conceptScheme!=null) {
            return this.skos.listConcepts(conceptScheme);
        }
        return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listConcepts(String conceptSchemeURI, String skosCollectionURI) {
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if (conceptSchemeURI==null || conceptScheme!=null) {
            
            
            SKOSCollection skosCollection = this.skos.getCollection(skosCollectionURI);
            if (skosCollectionURI==null || skosCollection!=null) {
                if (skosCollection!=null) {
                    System.out.println("Collection URI is: "+skosCollection.getURI());
                }
                return this.skos.listConcepts(conceptScheme, skosCollection);
            }
        }
        return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @param hierarchyMethod {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConceptNode> getConceptHierarchy(HierarchyMethod hierarchyMethod) {
        return this.skos.getConceptHierarchy(hierarchyMethod);
    }
    
    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param hierarchyMethod {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConceptNode> getConceptHierarchy(String conceptSchemeURI, HierarchyMethod hierarchyMethod) {
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if (conceptSchemeURI==null || conceptScheme!=null) {
            return this.skos.getConceptHierarchy(conceptScheme, hierarchyMethod);
        }
        else return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @param hierarchyMethod {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConceptNode> getConceptHierarchy(String conceptSchemeURI, String skosCollectionURI, HierarchyMethod hierarchyMethod) {
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        SKOSCollection skosCollection  = this.skos.getCollection(skosCollectionURI);
        if ((conceptSchemeURI==null || conceptScheme!=null) && (skosCollectionURI==null || skosCollection!=null)) {
            return this.skos.getConceptHierarchy(conceptScheme, skosCollection, hierarchyMethod);
        }
        else return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @param rootConceptURI {@inheritDoc}
     * @param hierarchyMethod {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConceptNode getConceptTree(String rootConceptURI, HierarchyMethod hierarchyMethod) {
        SKOSConcept rootConcept = this.skos.getConcept(rootConceptURI);
        if (rootConcept != null) {
            return this.skos.getConceptTree(rootConcept, hierarchyMethod);
        }
        else return null;
    }

    /**
     * {@inheritDoc}
     * @param rootConceptURI {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param hierarchyMethod {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConceptNode getConceptTree(String rootConceptURI, String conceptSchemeURI, HierarchyMethod hierarchyMethod) {
        SKOSConcept rootConcept = this.skos.getConcept(rootConceptURI);
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if ((rootConcept!=null) && (conceptSchemeURI==null || conceptScheme!=null)) {
            return this.skos.getConceptTree(rootConcept, conceptScheme, hierarchyMethod);
        }
        else return null;
    }

    /**
     * {@inheritDoc}
     * @param rootConceptURI {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @param hierarchyMethod {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConceptNode getConceptTree(String rootConceptURI, String conceptSchemeURI, String skosCollectionURI, HierarchyMethod hierarchyMethod) {
        SKOSConcept rootConcept = this.skos.getConcept(rootConceptURI);
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        SKOSCollection skosCollection  = this.skos.getCollection(skosCollectionURI);
        if ((rootConcept!=null) && (conceptSchemeURI==null || conceptScheme!=null) && (skosCollectionURI==null || skosCollection!=null)) {
            return this.skos.getConceptTree(rootConcept, conceptScheme, skosCollection, hierarchyMethod);
        }
        else return null;
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType) {
        SKOSConcept concept = this.skos.getConcept(conceptURI);
        if ((conceptURI==null || concept!=null)) {
            return this.skos.listSemanticRelations(concept, relationshipType);
        }
        else return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, String conceptSchemeURI) {
        SKOSConcept concept = this.skos.getConcept(conceptURI);
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if ((concept!=null) && (conceptSchemeURI==null || conceptScheme!=null)) {
            return this.skos.listSemanticRelations(concept, relationshipType, conceptScheme);
        }
        else return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, String conceptSchemeURI, String skosCollectionURI) {
        SKOSConcept concept = this.skos.getConcept(conceptURI);
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        SKOSCollection skosCollection  = this.skos.getCollection(skosCollectionURI);
        if ((concept!=null) && (conceptSchemeURI==null || conceptScheme!=null) && (skosCollectionURI==null || skosCollection!=null)) {
            return this.skos.listSemanticRelations(concept, relationshipType, conceptScheme, skosCollection);
        }
        else return new EmptyCloseableIterator<>();
    }

    @Override
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, Collection<String> conceptSchemeURIs) {
        return this.listRelatedConcepts(conceptURI, relationshipType, conceptSchemeURIs, null);
    }

    @Override
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs) {
        SKOSConcept concept = this.skos.getConcept(conceptURI);
        if (concept != null) {
            List<SKOSConceptScheme> conceptSchemes = new ArrayList<>(0);
            if (conceptSchemeURIs!=null) {
                conceptSchemes = new ArrayList<>(conceptSchemeURIs.size());
                for (String conceptSchemeURI: conceptSchemeURIs) {
                    SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
                    if (conceptScheme != null) conceptSchemes.add(conceptScheme);
                }
            }

            if (conceptSchemeURIs==null || conceptSchemeURIs.isEmpty() || !conceptSchemes.isEmpty()) {

                List<SKOSCollection> skosCollections = new ArrayList<>(0);
                if (conceptSchemeURIs!=null) {
                    skosCollections = new ArrayList<>(skosCollectionURIs.size());
                    for (String skosCollectionURI: skosCollectionURIs) {
                        SKOSCollection skosCollection = this.skos.getCollection(skosCollectionURI);
                        if (skosCollection != null) skosCollections.add(skosCollection);
                    }
                }
                if (skosCollectionURIs==null || skosCollectionURIs.isEmpty() || !skosCollections.isEmpty()) {

                    return this.skos.listSemanticRelations(concept, relationshipType, conceptSchemes, skosCollections);

                }
            }
        }
        return new EmptyCloseableIterator<>();
    }

    
    
    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectNarrowerConcepts(String conceptURI) {
        SKOSConcept concept = this.skos.getConcept(conceptURI);
        if (concept!=null) {
            return this.skos.getDirectNarrowerConcepts(concept);
        }
        else return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectNarrowerConcepts(String conceptURI, String conceptSchemeURI) {
        SKOSConcept concept = this.skos.getConcept(conceptURI);
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if ((concept!=null) && (conceptSchemeURI==null || conceptScheme!=null)) {
            return this.skos.getDirectNarrowerConcepts(concept, conceptScheme);
        }
        else return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectNarrowerConcepts(String conceptURI, String conceptSchemeURI, String skosCollectionURI) {
        SKOSConcept concept = this.skos.getConcept(conceptURI);
        if (concept!=null) {
            return this.skos.getDirectNarrowerConcepts(concept);
        }
        else return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectBroaderConcepts(String conceptURI) {
        SKOSConcept concept = this.skos.getConcept(conceptURI);
        if (concept!=null) {
            return this.skos.getDirectBroaderConcepts(concept);
        }
        else return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectBroaderConcepts(String conceptURI, String conceptSchemeURI) {
        SKOSConcept concept = this.skos.getConcept(conceptURI);
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        if ((concept!=null) && (conceptSchemeURI==null || conceptScheme!=null)) {
            return this.skos.getDirectBroaderConcepts(concept, conceptScheme);
        }
        else return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectBroaderConcepts(String conceptURI, String conceptSchemeURI, String skosCollectionURI) {
        SKOSConcept concept = this.skos.getConcept(conceptURI);
        SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
        SKOSCollection skosCollection  = this.skos.getCollection(skosCollectionURI);
        if ((conceptURI==null || concept!=null) && (conceptSchemeURI==null || conceptScheme!=null) && (skosCollectionURI==null || skosCollection!=null)) {
            return this.skos.getDirectBroaderConcepts(concept, conceptScheme, skosCollection);
        }
        else return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConcept getConcept(String conceptURI) {
        return this.skos.getConcept(conceptURI);
    }
    
    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param searchField {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, int offset, int limit) {
        return this.search(keyword, searchField, (Collection<String>)null, (Collection<String>)null, offset, limit);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param searchField {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, int offset, int limit) {
        Collection<String> conceptSchemeURIs = null;
        if (conceptSchemeURI != null) {
            conceptSchemeURIs = new ArrayList<>(1);
            conceptSchemeURIs.add(conceptSchemeURI);
        }
        return this.search(keyword, searchField, conceptSchemeURIs, null, offset, limit);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param searchField {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, String skosCollectionURI, int offset, int limit) {
        Collection<String> conceptSchemeURIs = null;
        if (conceptSchemeURI != null) {
            conceptSchemeURIs = new ArrayList<>(1);
            conceptSchemeURIs.add(conceptSchemeURI);
        }
        Collection<String> skosCollectionURIs = null;
        if (skosCollectionURI != null) {
            skosCollectionURIs = new ArrayList<>(1);
            skosCollectionURIs.add(skosCollectionURI);
        }
        return this.search(keyword, searchField, conceptSchemeURIs, skosCollectionURIs, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param searchField {@inheritDoc}
     * @param conceptSchemeURIs {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, int offset, int limit) {
        return this.search(keyword, searchField, conceptSchemeURIs, null, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param searchField {@inheritDoc}
     * @param conceptSchemeURIs {@inheritDoc}
     * @param skosCollectionURIs {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit) {
        List<Scored<SemanticEntity>> results = this.index.search(keyword, searchField, conceptSchemeURIs, skosCollectionURIs, offset, limit);
        List<SemanticEntity> entities = new ArrayList<>(results.size());
        for (Scored<SemanticEntity> result : results) entities.add(result.getItem());
        return entities;
    }
    
    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<SemanticEntity> search(String queryString, int offset, int limit) throws ParseException {
        return this.search(queryString, (Collection<String>)null, (Collection<String>)null, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<SemanticEntity> search(String queryString, String conceptSchemeURI, int offset, int limit) throws ParseException {
        Collection<String> conceptSchemeURIs = null;
        if (conceptSchemeURI != null) {
            conceptSchemeURIs = new ArrayList<>(1);
            conceptSchemeURIs.add(conceptSchemeURI);
        }
        return this.search(queryString, conceptSchemeURIs, null, offset, limit);
    }

    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<SemanticEntity> search(String queryString, String conceptSchemeURI, String skosCollectionURI, int offset, int limit) throws ParseException {
        Collection<String> conceptSchemeURIs = null;
        if (conceptSchemeURI != null) {
            conceptSchemeURIs = new ArrayList<>(1);
            conceptSchemeURIs.add(conceptSchemeURI);
        }
        Collection<String> skosCollectionURIs = null;
        if (skosCollectionURI != null) {
            skosCollectionURIs = new ArrayList<>(1);
            skosCollectionURIs.add(skosCollectionURI);
        }
        return this.search(queryString, conceptSchemeURIs, skosCollectionURIs, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param conceptSchemeURIs {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<SemanticEntity> search(String queryString, Collection<String> conceptSchemeURIs, int offset, int limit) throws ParseException {
        return this.search(queryString, conceptSchemeURIs, null, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param conceptSchemeURIs {@inheritDoc}
     * @param skosCollectionURIs {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<SemanticEntity> search(String queryString, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit) throws ParseException {
        List<Scored<SemanticEntity>> results = this.index.query(queryString, conceptSchemeURIs, skosCollectionURIs, offset, limit);
        List<SemanticEntity> entities = new ArrayList<>(results.size());
        for (Scored<SemanticEntity> result : results) entities.add(result.getItem());
        return entities;
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param searchField {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, int offset, int limit) {
        return this.searchConcepts(keyword, searchField, (Collection<String>)null, (Collection<String>)null, offset, limit);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param searchField {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, int offset, int limit) {
        Collection<String> conceptSchemeURIs = null;
        if (conceptSchemeURI != null) {
            conceptSchemeURIs = new ArrayList<>(1);
            conceptSchemeURIs.add(conceptSchemeURI);
        }
        return this.searchConcepts(keyword, searchField, conceptSchemeURIs, null, offset, limit);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param searchField {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, String skosCollectionURI, int offset, int limit) {
        Collection<String> conceptSchemeURIs = null;
        if (conceptSchemeURI != null) {
            conceptSchemeURIs = new ArrayList<>(1);
            conceptSchemeURIs.add(conceptSchemeURI);
        }
        Collection<String> skosCollectionURIs = null;
        if (skosCollectionURI != null) {
            skosCollectionURIs = new ArrayList<>(1);
            skosCollectionURIs.add(skosCollectionURI);
        }
        return this.searchConcepts(keyword, searchField, conceptSchemeURIs, skosCollectionURIs, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param searchField {@inheritDoc}
     * @param conceptSchemeURIs {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, int offset, int limit) {
        return this.searchConcepts(keyword, searchField, conceptSchemeURIs, null, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param searchField {@inheritDoc}
     * @param conceptSchemeURIs {@inheritDoc}
     * @param skosCollectionURIs {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit) {
        List<SKOSConcept> concepts = new ArrayList<>(Math.max(0, limit));
        List<Scored<SemanticEntity>> results = this.index.search(keyword, searchField, conceptSchemeURIs, skosCollectionURIs, offset, limit);
        for (Scored<SemanticEntity> result: results) {
            String conceptURI = result.getItem().getURI();
            SKOSConcept concept = this.skos.getConcept(conceptURI);
            if (concept != null) concepts.add(concept);
        }
        return concepts;
    }
    
    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> searchConcepts(String queryString, int offset, int limit) throws ParseException {
        return this.searchConcepts(queryString, (Collection<String>)null, (Collection<String>)null, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> searchConcepts(String queryString, String conceptSchemeURI, int offset, int limit) throws ParseException {
        Collection<String> conceptSchemeURIs = null;
        if (conceptSchemeURI != null) {
            conceptSchemeURIs = new ArrayList<>(1);
            conceptSchemeURIs.add(conceptSchemeURI);
        }
        return this.searchConcepts(queryString, conceptSchemeURIs, null, offset, limit);
    }

    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> searchConcepts(String queryString, String conceptSchemeURI, String skosCollectionURI, int offset, int limit) throws ParseException {
        Collection<String> conceptSchemeURIs = null;
        if (conceptSchemeURI != null) {
            conceptSchemeURIs = new ArrayList<>(1);
            conceptSchemeURIs.add(conceptSchemeURI);
        }
        Collection<String> skosCollectionURIs = null;
        if (skosCollectionURI != null) {
            skosCollectionURIs = new ArrayList<>(1);
            skosCollectionURIs.add(skosCollectionURI);
        }
        return this.searchConcepts(queryString, conceptSchemeURIs, skosCollectionURIs, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param conceptSchemeURIs {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> searchConcepts(String queryString, Collection<String> conceptSchemeURIs, int offset, int limit) throws ParseException {
        return this.searchConcepts(queryString, conceptSchemeURIs, null, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param conceptSchemeURIs {@inheritDoc}
     * @param skosCollectionURIs {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> searchConcepts(String queryString, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit) throws ParseException {
        List<SKOSConcept> concepts = new ArrayList<>(Math.max(0, limit));
        List<Scored<SemanticEntity>> results = this.index.query(queryString, conceptSchemeURIs, skosCollectionURIs, offset, limit);
        for (Scored<SemanticEntity> result: results) {
            String conceptURI = result.getItem().getURI();
            SKOSConcept concept = this.skos.getConcept(conceptURI);
            if (concept != null) concepts.add(concept);
        }
        return concepts;
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> interpretKeyword(Term keyword) {
        return this.interpretKeyword(keyword, (Collection<String>)null, (Collection<String>)null);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> interpretKeyword(Term keyword, String conceptSchemeURI) {
        Collection<String> conceptSchemeURIs = null;
        if (conceptSchemeURI != null) {
            conceptSchemeURIs = new ArrayList<>(1);
            conceptSchemeURIs.add(conceptSchemeURI);
        }
        return this.interpretKeyword(keyword, conceptSchemeURIs, null);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @param skosCollectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> interpretKeyword(Term keyword, String conceptSchemeURI, String skosCollectionURI) {
        Collection<String> conceptSchemeURIs = null;
        if (conceptSchemeURI != null) {
            conceptSchemeURIs = new ArrayList<>(1);
            conceptSchemeURIs.add(conceptSchemeURI);
        }
        Collection<String> skosCollectionURIs = null;
        if (skosCollectionURI != null) {
            skosCollectionURIs = new ArrayList<>(1);
            skosCollectionURIs.add(skosCollectionURI);
        }
        return this.interpretKeyword(keyword, conceptSchemeURIs, skosCollectionURIs);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param conceptSchemeURIs {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> interpretKeyword(Term keyword, Collection<String> conceptSchemeURIs) {
        return this.interpretKeyword(keyword, conceptSchemeURIs, null);
    }
    
    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param conceptSchemeURIs {@inheritDoc}
     * @param skosCollectionURIs {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> interpretKeyword(Term keyword, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs) {
        List<SKOSConcept> matchingConcepts = new ArrayList<>();
        
        if (keyword!=null) {
            
            List<SKOSConceptScheme> conceptSchemes = new ArrayList<>(0);
            if (conceptSchemeURIs!=null) {
                conceptSchemes = new ArrayList<>(conceptSchemeURIs.size());
                for (String conceptSchemeURI: conceptSchemeURIs) {
                    SKOSConceptScheme conceptScheme = this.skos.getConceptScheme(conceptSchemeURI);
                    if (conceptScheme != null) conceptSchemes.add(conceptScheme);
                }
            }
            
            if (conceptSchemeURIs==null || conceptSchemeURIs.isEmpty() || !conceptSchemes.isEmpty()) {
            
                List<SKOSCollection> skosCollections = new ArrayList<>(0);
                if (conceptSchemeURIs!=null) {
                    skosCollections = new ArrayList<>(skosCollectionURIs.size());
                    for (String skosCollectionURI: skosCollectionURIs) {
                        SKOSCollection skosCollection = this.skos.getCollection(skosCollectionURI);
                        if (skosCollection != null) skosCollections.add(skosCollection);
                    }
                }
                if (skosCollectionURIs==null || skosCollectionURIs.isEmpty() || !skosCollections.isEmpty()) {
            
                    String stringKeyword = keyword.getString();

                    List<SKOSConcept> directMatches = new ArrayList<>();

                    List<SKOSConcept> firstLevelNarrowerConcepts = new ArrayList<>();

                    SKOSConcept concept = this.skos.getConcept(stringKeyword);
                    if (concept != null) {
                        directMatches.add(concept);
                    }
                    else {
                        directMatches = this.searchConcepts(keyword, IndexField.Searchable.LABEL, conceptSchemeURIs, skosCollectionURIs, 0, this.interpretationMaxMatches);

                        if (directMatches.isEmpty()) directMatches = this.searchConcepts(keyword, IndexField.Searchable.IX_LABEL, conceptSchemeURIs, skosCollectionURIs, 0, this.interpretationMaxMatches);

                        if (directMatches.isEmpty()) directMatches = this.searchConcepts(keyword, null, conceptSchemeURIs, skosCollectionURIs, 0, this.interpretationMaxMatches);
                    }

                    for (SKOSConcept matchingConcept:directMatches) {

                        firstLevelNarrowerConcepts.add(matchingConcept);

                        CloseableIterator<SKOSConcept> matchingConceptNarrowerConcepts = matchingConcept.listSemanticRelations(SKOSSemanticProperty.narrowerTransitive);

                        while (matchingConceptNarrowerConcepts.hasNext()) {
                            firstLevelNarrowerConcepts.add(matchingConceptNarrowerConcepts.next());
                        }
                    }

                    if (this.interpretWithRelatedConcepts && !firstLevelNarrowerConcepts.isEmpty()) {
                        List<SKOSConcept> flncRlatedConcepts = new ArrayList<>();
                        for (SKOSConcept narrowerConcept:firstLevelNarrowerConcepts) {

                            if (this.conceptIsInAtLeastOneOfSchemes(narrowerConcept, conceptSchemes) &&
                                this.conceptIsInAtLeastOneOfCollections(narrowerConcept, skosCollections)) {
                                if (!matchingConcepts.contains(narrowerConcept)) matchingConcepts.add(narrowerConcept);
                            }
                            
                            CloseableIterator<SKOSConcept> relatedConcepts = narrowerConcept.listSemanticRelations(SKOSSemanticProperty.related);
                            while (relatedConcepts.hasNext()) {
                                SKOSConcept relatedConcept = relatedConcepts.next();
                                if (relatedConcept!=null && !flncRlatedConcepts.contains(relatedConcept)) flncRlatedConcepts.add(relatedConcept);
                            }
                        }
                        
                        if (!flncRlatedConcepts.isEmpty()){
                            
                            List<SKOSConcept> secondLevelNarrowerConcepts = new ArrayList<>();
                            
                            for (SKOSConcept relatedConcept:flncRlatedConcepts) {
                                if (this.conceptIsInAtLeastOneOfSchemes(relatedConcept, conceptSchemes) &&
                                    this.conceptIsInAtLeastOneOfCollections(relatedConcept, skosCollections)) {
                                    
                                    if (!matchingConcepts.contains(relatedConcept)) matchingConcepts.add(relatedConcept);
                                }
                                
                                CloseableIterator<SKOSConcept> narrowerConceptsIter = this.skos.listSemanticRelations(relatedConcept, SKOSSemanticProperty.narrowerTransitive, conceptSchemes, skosCollections);
                                while (narrowerConceptsIter.hasNext()) {
                                    SKOSConcept narrowerConcept = narrowerConceptsIter.next();
                                    if (!matchingConcepts.contains(narrowerConcept)) matchingConcepts.add(narrowerConcept);
                                }
                            }
                        }

                    }
                }
            }
        }
        return matchingConcepts;
    }

    /**
     * {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        this.index.close();
        this.skos.close();
    }
    
    /**
     * Checks if the provided concept is in at least one of the provided concept
     * schemes
     * @param concept SKOS concept to check
     * @param conceptSchemes Collection of SKOS concept schemes
     * @return {@code true} if {@code conceptSchemes} is null or empty of if
     * {@code concept} is an at least one of the concept schemes listed in
     * {@code conceptSchemes}
     */
    private boolean conceptIsInAtLeastOneOfSchemes(SKOSConcept concept, List<SKOSConceptScheme> conceptSchemes) {
        if (conceptSchemes==null || conceptSchemes.isEmpty()) return true;
        else {
            for (SKOSConceptScheme conceptScheme: conceptSchemes) {
                if (concept.isInScheme(conceptScheme)) return true;
            }
            return false;
        }
    }
    
    /**
     * Checks if the provided concept is in at least one of the provided SKOS
     * collections
     * @param concept SKOS concept to check
     * @param skosCollections Collection of SKOS collections
     * @return {@code true} if {@code skosCollections} is null or empty of if
     * {@code concept} is an at least one of the collections listed in
     * {@code conceptSchemes}
     */
    private boolean conceptIsInAtLeastOneOfCollections(SKOSConcept concept, List<SKOSCollection> skosCollections) {
        if (skosCollections==null || skosCollections.isEmpty()) return true;
        else {
            for (SKOSCollection skosCollection: skosCollections) {
                if (concept.isMemberOfCollection(skosCollection)) return true;
            }
            return false;
        }
    }
    
}
