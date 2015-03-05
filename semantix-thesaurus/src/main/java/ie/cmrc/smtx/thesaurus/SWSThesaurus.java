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
import ie.cmrc.smtx.skos.model.SKOSCollection;
import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSConceptScheme;
import ie.cmrc.smtx.skos.model.SKOSSemanticProperty;
import ie.cmrc.smtx.skos.model.hierarchy.HierarchyMethod;
import ie.cmrc.smtx.skos.model.hierarchy.SKOSConceptNode;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import ie.cmrc.smtx.skos.index.IndexField;
import ie.cmrc.util.Term;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public interface SWSThesaurus {
    
    /**
     * Lists the SKOS concept schemes of this thesaurus
     * @return Closeable iterator {@link ie.cmrc.skos.core.util.CloseableIterator}
     * over the SKOS concept schemes of the thesaurus
     */
    public CloseableIterator<SKOSConceptScheme> listConceptSchemes();

    /**
     * Returns the SKOS concept scheme identified by the provided URI
     * @param conceptSchemeURI URI of a SKOS concept scheme to extract
     * @return {@link ie.cmrc.skos.core.SKOSConceptScheme} with the provided URI.
     * If the URI is {@code null} or empty, or not that of a
     * {@link ie.cmrc.skos.core.SKOSConceptScheme}, then {@code null} is returned.
     */
    public SKOSConceptScheme getConceptScheme(String conceptSchemeURI);
    
    /**
     * Lists the SKOS collections of this thesaurus
     * @return Closeable iterator {@link ie.cmrc.skos.core.util.CloseableIterator}
     * over the SKOS collections defined in the thesaurus
     */
    public CloseableIterator<SKOSCollection> listCollections();
    
    /**
     * Lists the SKOS collections within this thesaurus that belong to the
     * SKOS concept scheme identified by the provided URI
     * @param conceptSchemeURI URI of a SKOS concept scheme to which the
     * returned collections must belong
     * @return Closeable iterator {@link ie.cmrc.skos.core.util.CloseableIterator}
     * over the {@link ie.cmrc.skos.core.SKOSCollection}s that
     * belong to {@code conceptSchemeURI}.
     * If {@code conceptScheme==null}, then results are not filtered by
     * concept scheme.
     * If no concept scheme exists with the specified URI, then
     * an empty iterator is returned.
     */
    public CloseableIterator<SKOSCollection> listCollections(String conceptSchemeURI);
    
    /**
     * Lists the SKOS collections within this thesaurus that belong to the
     * provided SKOS concept scheme and collection.
     * @param conceptSchemeURI URI of a SKOS concept scheme to which the
     * returned collections must belong
     * @param skosCollectionURI SKOS collection to which the returned
     * collections must belong
     * @return Closeable iterator {@link ie.cmrc.skos.core.util.CloseableIterator}
     * over the {@link ie.cmrc.skos.core.SKOSCollection}s that
     * belong to {@code conceptSchemeURI} and {@code skosCollectionURI}.
     * If {@code conceptScheme==null}, then results are not filtered by
     * concept scheme.
     * If {@code skosCollection==null}, then results are not filtered by
     * collection.
     * If no concept scheme or collection exists with the specified URIs, then
     * an empty iterator is returned.
     */
    public CloseableIterator<SKOSCollection> listCollections(String conceptSchemeURI, String skosCollectionURI);

    /**
     * Returns the SKOS collection identified by the provided URI
     * @param skosCollectionURI URI of a SKOS collection to extract
     * @return {@link ie.cmrc.skos.core.SKOSCollection} with the provided URI. If
     * the URI is {@code null} or empty, or
     * not that of a {@link SKOSCollection}, then {@code null} is returned.
     */
    public SKOSCollection getCollection(String skosCollectionURI);

    /**
     * Lists the top concepts of all concept schemes in this SKOS thesaurus
     * @return {@link ie.cmrc.skos.core.util.CloseableIterator} over the concepts
     * that are asserted to be top concepts of at least one concept scheme in
     * this thesaurus
     */
    public CloseableIterator<SKOSConcept> listTopConcepts();
    
    /**
     * Lists the top concepts of the concept scheme identified by the
     * provided URI
     * @param conceptSchemeURI URI of a SKOS concept scheme the top concepts
     * of which are to be listed
     * @return {@link ie.cmrc.skos.core.util.CloseableIterator} over the top concepts
     * of the concept scheme identified by {@code conceptSchemeURI}.
     * If {@code conceptSchemeURI==null}, then this is
     * equivalent to {@link #listTopConcepts()}. <b>However</b>,
     * if no concept scheme exists with the specified URI, then
     * an empty iterator is returned.
     */
    public CloseableIterator<SKOSConcept> listTopConcepts(String conceptSchemeURI);
    
    /**
     * Lists the top concepts of the concept scheme that belong to the
     * provided SKOS collection
     * @param conceptSchemeURI URI of the a SKOS concept scheme
     * @param skosCollectionURI URI of a SKOS collection
     * @return {@link ie.cmrc.skos.core.util.CloseableIterator} over the top concepts
     * of the concept scheme identified by the URI {@code conceptSchemeURI}
     * that belong to the collection identified by the URI
     * {@code skosCollectionURI}.
     * If {@code skosCollectionURI==null}, then this is
     * equivalent to {@link #listTopConcepts(java.lang.String)}.
     * If {@code conceptScheme==null}, then all concept schemes are considered.
     * If not concept scheme or collection exist with the provided URIs then
     * an empty iterator is returned.
     */
    public CloseableIterator<SKOSConcept> listTopConcepts(String conceptSchemeURI, String skosCollectionURI);

    /**
     * Lists the concepts that have no (transitive) broader concepts (other
     * than themselves or equivalent concepts) in this SKOS thesaurus
     * @return List of concepts that have no broader concepts
     */
    public List<SKOSConcept> getBroadestConcepts();
    
    /**
     * Lists the concepts belonging to the concept scheme identified by the
     * provided URI and which do not have any (transitive) broader concepts
     * within the same concept scheme (other than themselves or equivalent
     * concepts)
     * @param conceptSchemeURI URI of the target SKOS concept scheme
     * @return List of the broadest concepts of the concept scheme identified
     * by the URI {@code conceptSchemeURI}
     */
    public List<SKOSConcept> getBroadestConcepts(String conceptSchemeURI);
    
    /**
     * Lists the concepts belonging to the concept scheme and collection
     * identified by the provided URIs and which do not have any (transitive)
     * broader concepts within the same concept scheme and collection (other
     * than themselves or equivalent concepts)
     * @param conceptSchemeURI URI of the target SKOS concept scheme
     * @param skosCollectionURI URI of the target SKOS collection
     * @return List of the broadest concepts of the concept scheme and
     * collection identified by the URI {@code conceptSchemeURI} and
     * {@code skosCollectionURI}
     */
    public List<SKOSConcept> getBroadestConcepts(String conceptSchemeURI, String skosCollectionURI);

    /**
     * Lists the SKOS concepts of this thesaurus
     * @return {@link ie.cmrc.skos.core.util.CloseableIterator} over the SKOS
     * concepts of the thesaurus
     */
    public CloseableIterator<SKOSConcept> listConcepts();
    
    /**
     * Lists the SKOS concepts that belong to the concept scheme identified by
     * the provided URI
     * @param conceptSchemeURI URI of the SKOS concept scheme to which the
     * returned concepts must belong
     * @return {@link ie.cmrc.skos.core.util.CloseableIterator} over the SKOS
     * concepts that belong to {@code conceptSchemeURI}.
     * If {@code conceptSchemeURI==null}, then
     * this is equivalent to {@link #listConcepts()}. <b>However</b>, if
     * no concept scheme exists with the provided URI, then an empty iterator
     * is returned.
     */
    public CloseableIterator<SKOSConcept> listConcepts(String conceptSchemeURI);
    
    /**
     * Lists the SKOS concepts that belong to the concept scheme and collection
     * identified by the provided URIs
     * @param conceptSchemeURI URI of the SKOS concept scheme to which the
     * returned concepts must belong
     * @param skosCollectionURI URI of the SKOS collection to which the
     * returned concepts must belong
     * @return {@link ie.cmrc.skos.core.util.CloseableIterator} over the SKOS
     * concepts that belong to {@code conceptSchemeURI} and
     * {@code skosCollectionURI}.
     * If {@code skosCollectionURI==null}, then
     * this is equivalent to {@link #listConcepts(java.lang.String)}.
     * If {@code conceptSchemeURI==null}, then results are not filtered by
     * concept scheme.
     * <b>However</b>, if no concept scheme or no collection exists with the
     * provided URIs, then an empty iterator is returned.
     */
    public CloseableIterator<SKOSConcept> listConcepts(String conceptSchemeURI, String skosCollectionURI);
    
    /**
     * Builds the hierarchy of the concepts of this thesaurus using the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}. Please note that this
     * method is <b>not suitable for large thesauri</b>.
     * @return List of root concept nodes
     */
    public List<SKOSConceptNode> getConceptHierarchy(HierarchyMethod hierarchyMethod);
    
    /**
     * Builds the hierarchy of the concepts of the provided concept scheme
     * using the provided {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * Please note that this method is <b>not suitable for large concept
     * schemes</b>.
     * @param conceptSchemeURI URI of the target concept scheme
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * If {@code null} then the default {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * id used.
     * @return List of root concept nodes, If {@code conceptSchemeURI==null},
     * then this is equivalent to
     * {@linkplain #getConceptHierarchy(ie.cmrc.skos.core.hierarchy.HierarchyMethod)}.
     * If no concept scheme exists with the provided URI then an empty list is
     * returned.
     */
    public List<SKOSConceptNode> getConceptHierarchy(String conceptSchemeURI, HierarchyMethod hierarchyMethod);
    
    /**
     * Builds the hierarchy of the concepts of the provided concept scheme
     * and collection using the provided {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param conceptSchemeURI URI of the target concept scheme
     * @param skosCollectionURI URI of the target SKOS collection
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * If {@code null} then the default {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * id used.
     * @return List of root concept nodes, If {@code conceptSchemeURI==null},
     * then results are not filtered by concept scheme. Similarly, if
     * {@code skosCollectionURI==null} then no filtering by collection is
     * performed.
     * If no concept scheme or collection exists with the provided URIs then an
     * empty list is returned.
     */
    public List<SKOSConceptNode> getConceptHierarchy(String conceptSchemeURI, String skosCollectionURI, HierarchyMethod hierarchyMethod);
    
    /**
     * Builds the hierarchy of the provided concept using the provided
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param rootConceptURI URI of the root concept of the hierarchy
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * Please note that the root type of the hierarchy method is not relevant
     * here. If {@code hierarchyMethod==null} then the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod} id used.
     * @return Root concept node. If {@code rootConceptURI==null} or no
     * concept exists with the provided URI, then this returns {@code null}.
     */
    public SKOSConceptNode getConceptTree(String rootConceptURI, HierarchyMethod hierarchyMethod);
    
    /**
     * Builds the hierarchy of the specified concept in the specified concept
     * scheme using the provided {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param rootConceptURI URI of the root concept of the hierarchy
     * @param conceptSchemeURI URI of the target concept scheme
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * Please note that the root type of the hierarchy method is not relevant
     * here. If {@code hierarchyMethod==null} then the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod} id used.
     * @return Root concept node. If {@code rootConceptURI==null} or no
     * concept exists with the provided URI, then this returns {@code null}.
     * If {@code conceptSchemeURI==null}, the this is equivalent to
     * {@linkplain #getConceptTree(java.lang.String, ie.cmrc.skos.core.hierarchy.HierarchyMethod)}.
     * If no concept scheme exists with the provided concepts scheme URI then
     * this returns a leaf node containing the root concept.
     */
    public SKOSConceptNode getConceptTree(String rootConceptURI, String conceptSchemeURI, HierarchyMethod hierarchyMethod);
    
    /**
     * Builds the hierarchy of the specified concept in the specified concept
     * scheme and collection using the provided {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param rootConceptURI URI of the root concept of the hierarchy
     * @param conceptSchemeURI URI of the target concept scheme
     * @param skosCollectionURI URI of the target SKOS collection
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * Please note that the root type of the hierarchy method is not relevant
     * here. If {@code hierarchyMethod==null} then the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod} id used.
     * @return Root concept node. If {@code rootConceptURI==null} or no
     * concept exists with the provided URI, then this returns {@code null}.
     * If {@code conceptSchemeURI==null}, then results are not filtered by
     * concept scheme. Similarly if {@code skosCollectionURI==null}, then
     * results are not filtered by collection. If
     * no concept scheme or collection exists with the provided URIs then this
     * returns a leaf node containing the root concept.
     */
    public SKOSConceptNode getConceptTree(String rootConceptURI, String conceptSchemeURI, String skosCollectionURI, HierarchyMethod hierarchyMethod);
    
    /**
     * Lists the concepts related to the specified concept through the provided
     * relationship type
     * @param conceptURI URI of the source concept
     * @param relationshipType Type of the relationship expressed as a
     * {@link ie.cmrc.skos.core.SKOSSemanticProperty}
     * @return Iterator over the SKOS concepts related to the concept identified
     * by the URI {@code conceptURI} through {@code relationshipType} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code conceptURI==null}, then this
     * returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     */
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType);
    
    /**
     * Lists the concepts related to the specified concept through the provided
     * relationship type and belonging to the specified concept scheme
     * @param conceptURI URI of the source concept
     * @param relationshipType Type of the relationship expressed as a
     * {@link ie.cmrc.skos.core.SKOSSemanticProperty}
     * @param conceptSchemeURI URI of the target concept scheme
     * @return Iterator over the SKOS concepts related to the concept identified
     * by the URI {@code conceptURI} through {@code relationshipType} and
     * belonging to {@code conceptSchemeURI} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code conceptURI==null}, or
     * no resource exists with one of the provided URIs, then this
     * returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code conceptSchemeURI==null} then this is equivalent to
     * {@link #listRelatedConcepts(java.lang.String, ie.cmrc.skos.core.SKOSSemanticProperty)}.
     */
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, String conceptSchemeURI);
    
    /**
     * Lists the concepts related to the specified concept through the provided
     * relationship type and belonging to the specified concept scheme and
     * collection
     * @param conceptURI URI of the source concept
     * @param relationshipType Type of the relationship expressed as a
     * {@link ie.cmrc.skos.core.SKOSSemanticProperty}
     * @param conceptSchemeURI URI of the target concept scheme
     * @param skosCollectionURI URI of the target SKOS collection
     * @return Iterator over the SKOS concepts related to the concept identified
     * by the URI {@code conceptURI} through {@code relationshipType} and
     * belonging to {@code conceptSchemeURI} and {@code skosCollectionURI} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code conceptURI==null}, or
     * no resource exists with one of the provided URIs, then this
     * returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code conceptSchemeURI==null}, then results are not filtered by
     * concept scheme. Similarly if {@code skosCollectionURI==null} then results
     * are not filtered by collection.
     */
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, String conceptSchemeURI, String skosCollectionURI);
    
    /**
     * Lists the concepts related to the specified concept through the provided
     * relationship type and belonging to at least on of the specified concept
     * schemes
     * @param conceptURI URI of the source concept
     * @param relationshipType Type of the relationship expressed as a
     * {@link ie.cmrc.skos.core.SKOSSemanticProperty}
     * @param conceptSchemeURIs URIs of the target concept schemes
     * @return Iterator over the SKOS concepts related to the concept identified
     * by the URI {@code conceptURI} through {@code relationshipType} and
     * belonging to at least one of the concept schemes listed in
     * {@code conceptSchemeURIs} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code conceptURI==null}, or
     * no resource exists with one of the provided URIs, then this
     * returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code conceptSchemeURIs} is {@code null} or empty, then this is
     * equivalent to {@linkplain #listRelatedConcepts(java.lang.String, ie.cmrc.skos.core.SKOSSemanticProperty)}.
     */
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, Collection<String> conceptSchemeURIs);
    
    /**
     * Lists the concepts related to the specified concept through the provided
     * relationship type and belonging to at least on of the specified concept
     * schemes and at least one of the specified collections
     * @param conceptURI URI of the source concept
     * @param relationshipType Type of the relationship expressed as a
     * {@link ie.cmrc.skos.core.SKOSSemanticProperty}
     * @param conceptSchemeURIs URIs of the target concept schemes
     * @param skosCollectionURIs URIs of the target SKOS collections
     * @return Iterator over the SKOS concepts related to the concept identified
     * by the URI {@code conceptURI} through {@code relationshipType} and
     * belonging to at least one of the concept schemes listed in
     * {@code conceptSchemeURIs} and and at least one of the collections listed
     * in {@code skosCollectionURIs} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code conceptURI==null}, or
     * no resource exists with one of the provided URIs, then this
     * returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code conceptSchemeURIs} is {@code null} or empty, then results are
     * not filtered by concept scheme. If {@code skosCollectionURIs} is
     * {@code null} or empty, then results are not filtered by collection.
     */
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs);

    /**
     * Concepts that are immediately narrower than the specified concept. For a
     * given concept {@code X}, a concept {@code Y} is <b><i>immediately narrower</i></b>
     * than {@code X} if {@code Y} is semantically narrower than {@code X} and
     * no other concept {@code Z} exists such that {@code Z} is narrower than
     * {@code X} and {@code Y} is narrower than {@code Z}.
     * @param conceptURI URI of a SKOS concept
     * @return List of concepts that are immediately narrower than the SKOS
     * concept identified by {@code concept}. If {@code conceptURI==null}
     * of not concept exists with such URI, then this returns an empty list.
     */
    public List<SKOSConcept> getDirectNarrowerConcepts(String conceptURI);
    
    /**
     * Concepts that are immediately narrower than the specified concept in the
     * specified concept scheme. For a given concept {@code X} and a concept
     * scheme {@code S}, a concept {@code Y} is <b><i>immediately narrower</i></b>
     * than {@code X} in {@code S} if {@code Y} is in scheme {@code S}, and is
     * semantically narrower than {@code X}, and if no other concept {@code Z}
     * exists in {@code S} such that {@code Z} is narrower than {@code X} and
     * {@code Y} is narrower than {@code Z}.
     * @param conceptURI URI of a SKOS concept
     * @param conceptSchemeURI URI of the target SKOS concept scheme
     * @return List of concepts that are immediately narrower than
     * {@code conceptURI} in {@code conceptScheme}. If {@code conceptURI==null}
     * or no concept or concept scheme exists with the provided URIs, then an
     * empty list is returned.
     */
    public List<SKOSConcept> getDirectNarrowerConcepts(String conceptURI, String conceptSchemeURI);
    
    /**
     * Concepts that are immediately narrower than the specified concept in the
     * specified concept scheme and collection. For a given concept {@code X}, a concept
     * scheme {@code S} and a SKOS collection {@code C}, a concept {@code Y} is
     * <b><i>immediately narrower</i></b> than {@code X} in {@code S} and {@code C}
     * if {@code Y} belongs to both {@code S} and {@code C}, and is
     * semantically narrower than {@code X}, and if no other concept {@code Z}
     * exists in both {@code S} and {@code C} such that {@code Z} is narrower
     * than {@code X} and {@code Y} is narrower than {@code Z}.
     * @param conceptURI URI of a SKOS concept
     * @param conceptSchemeURI URI of the target SKOS concept scheme
     * @param skosCollectionURI URI of the target SKOS collection
     * @return List of concepts that are immediately narrower than
     * {@code conceptURI} in {@code conceptSchemeURI} and {@code skosCollectionURI}.
     * If {@code conceptURI==null} or no resource exists with one of the
     * provided URIs, then an empty list is returned.
     */
    public List<SKOSConcept> getDirectNarrowerConcepts(String conceptURI, String conceptSchemeURI, String skosCollectionURI);

    /**
     * Concepts that are immediately broader than the specified concept. For a
     * given concept {@code X}, a concept {@code Y} is <b><i>immediately broader</i></b>
     * than {@code X} if {@code Y} is semantically broader than {@code X} and
     * no other concept {@code Z} exists such that {@code Z} is broader than
     * {@code X} and {@code Y} is broader than {@code Z}.
     * @param conceptURI URI of a SKOS concept
     * @return List of concepts that are immediately broader than
     * {@code concept}. If {@code conceptURI==null} or no concept exists with
     * the provided URI then an empty list is returned.
     */
    public List<SKOSConcept> getDirectBroaderConcepts(String conceptURI);
    
    /**
     * Concepts that are immediately broader than the specified concept in the
     * specified concept scheme. For a given concept {@code X} and a concept
     * scheme {@code S}, a concept {@code Y} is <b><i>immediately broader</i></b>
     * than {@code X} in {@code S} if {@code Y} is in scheme {@code S}, and is
     * semantically broader than {@code X}, and if no other concept {@code Z}
     * exists in {@code S} such that {@code Z} is broader than {@code X} and
     * {@code Y} is broader than {@code Z}.
     * @param conceptURI URI of a SKOS concept
     * @param conceptSchemeURI URI of the target SKOS concept scheme
     * @return List of concepts that are immediately broader than
     * {@code conceptURI} in {@code conceptSchemeURI}. If {@code conceptURI==null}
     * of no resource exists with one of the provided URIs, then an empty list
     * is returned. However, if {@code conceptSchemeURI==null}, then this is
     * equivalent to {@linkplain #getDirectBroaderConcepts(java.lang.String)}.
     */
    public List<SKOSConcept> getDirectBroaderConcepts(String conceptURI, String conceptSchemeURI);
    
    /**
     * Concepts that are immediately broader than the specified concept in the
     * specified concept scheme and collection. For a given concept {@code X}, a concept
     * scheme {@code S} and a SKOS collection {@code C}, a concept {@code Y} is
     * <b><i>immediately broader</i></b> than {@code X} in {@code S} and {@code C}
     * if {@code Y} belongs to both {@code S} and {@code C}, and is
     * semantically broader than {@code X}, and if no other concept {@code Z}
     * exists in both {@code S} and {@code C} such that {@code Z} is broader
     * than {@code X} and {@code Y} is broader than {@code Z}.
     * @param conceptURI URI of a SKOS concept
     * @param conceptSchemeURI URI of the target SKOS concept scheme
     * @param skosCollectionURI URI of the target SKOS collection
     * @return List of concepts that are immediately broader than
     * {@code conceptURI} in {@code conceptSchemeURI} and
     * {@code skosCollectionURI}. If {@code conceptURI==null}
     * of no resource exists with one of the provided URIs, then an empty list
     * is returned. If {@code conceptSchemeURI==null}, then results are not
     * filtered by concept scheme. If {@code skosCollectionURI==null}, then
     * results are not filtered by SKOS collection.
     */
    public List<SKOSConcept> getDirectBroaderConcepts(String conceptURI, String conceptSchemeURI, String skosCollectionURI);

    /**
     * Get the concept with the provide URI
     * @param conceptURI URI of the requested concept
     * @return {@link ie.cmrc.skos.core.SKOSConcept} whose URI is {@code conceptURI}
     * if any; otherwise {@code null}
     */
    public SKOSConcept getConcept(String conceptURI);

    /**
     * Lists the semantic entities (concepts) whose values for the provided field
     * match the provided keyword
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param searchField Field to search the keyword in
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of semantic entities matching {@code keyword}
     */
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, int offset, int limit);

    /**
     * Lists the semantic entities (concepts) matching the provided keyword and belonging to
     * the concept scheme identified by the provided URI
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param searchField Specifies the search field. If {@code null} then all
     * search fields a considered.
     * @param conceptSchemeURI URI of the target concept scheme to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of semantic entities matching {@code keyword}
     */
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, int offset, int limit);

    /**
     * Lists the semantic entities (concepts) matching the provided keyword and belonging to
     * the concept scheme and collection identified by the provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param searchField Specifies the search field. If {@code null} then all
     * search fields a considered.
     * @param conceptSchemeURI URI of the target concept scheme to restrict
     * search to
     * @param skosCollectionURI URI of the SKOS collection to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of semantic entities matching {@code keyword}
     */
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, String skosCollectionURI, int offset, int limit);

    /**
     * Lists the semantic entities (concepts) matching the provided keyword and belonging to
     * the concept schemes identified by the provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param searchField Specifies the search field. If {@code null} then all
     * search fields a considered.
     * @param conceptSchemeURIs List of URIs of concept schemes to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of semantic entities matching {@code keyword}
     */
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, int offset, int limit);

    /**
     * Lists the semantic entities (concepts) matching the provided keyword and
     * belonging to at least one of the concept schemes and at least one of the
     * collections identified by the provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param searchField Specifies the search field. If {@code null} then all
     * search fields a considered.
     * @param conceptSchemeURIs List of URIs of concept schemes to restrict
     * search to
     * @param skosCollectionURIs List of URIs of SKOS collections to restrict
     * search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of semantic entities matching {@code keyword}
     */
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit);
    
    /**
     * Lists the semantic entities (concepts) matching the provided query. This method
     * accepts a Lucene query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link ie.cmrc.skos.index.IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of semantic entities matching {@code queryString}
     * @throws java.text.ParseException If a parsing error is encountered
     */
    public List<SemanticEntity> search(String queryString, int offset, int limit) throws ParseException;
    
    /**
     * Lists the semantic entities (concepts) matching the provided query and belonging to
     * the concept scheme identified by the provided URI. This method
     * accepts a Lucene query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link ie.cmrc.skos.index.IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param conceptSchemeURI URI of the target concept scheme to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of semantic entities matching {@code queryString} and belonging to
     * the concept scheme identified by the URI {@code conceptSchemeURI}
     * @throws java.text.ParseException If a parsing error is encountered
     */
    public List<SemanticEntity> search(String queryString, String conceptSchemeURI, int offset, int limit) throws ParseException;
    
    /**
     * Lists the semantic entities (concepts) matching the provided query and belonging to
     * the concept scheme and collection identified by the provided URIs. This method
     * accepts a Lucene query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link ie.cmrc.skos.index.IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param conceptSchemeURI URI of the target concept scheme to restrict search to
     * @param skosCollectionURI URI of the SKOS collection to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of semantic entities matching {@code queryString} and belonging to
     * the specified concept scheme and collection
     * @throws java.text.ParseException If a parsing error is encountered
     */
    public List<SemanticEntity> search(String queryString, String conceptSchemeURI, String skosCollectionURI, int offset, int limit) throws ParseException;
    
    /**
     * Lists the semantic entities (concepts) matching the provided query and belonging
     * to at least one of the concept schemes identified by the provided URIs.
     * This method accepts a Lucene query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link ie.cmrc.skos.index.IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param conceptSchemeURIs List of URIs of concept schemes to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of semantic entities matching {@code queryString} and belonging to
     * at least one of the concept schemes identified by the URIs in
     * {@code conceptSchemes}
     * @throws java.text.ParseException If a parsing error is encountered
     */
    public List<SemanticEntity> search(String queryString, Collection<String> conceptSchemeURIs, int offset, int limit) throws ParseException;
    
    /**
     * Lists the semantic entities (concepts) matching the provided query and belonging
     * to at least one of the concept schemes and at least one of the
     * collections identified by the provided URIs. This method accepts a Lucene
     * query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link ie.cmrc.skos.index.IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param conceptSchemeURIs List of URIs of concept schemes to restrict search to
     * @param skosCollectionURIs List of URIs of SKOS collections to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of semantic entities matching
     * {@code queryString} and belonging to at least one of the specified
     * concept schemes and at least one of the specified target collections
     * @throws java.text.ParseException If a parsing error is encountered
     */
    public List<SemanticEntity> search(String queryString, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit) throws ParseException;
    
    /**
     * Lists the concepts whose values for the provided field
     * match the provided keyword
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param searchField Field to search the keyword in
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of concepts matching {@code keyword}
     */
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, int offset, int limit);

    /**
     * Lists the concepts matching the provided keyword and belonging to
     * the concept scheme identified by the provided URI
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param searchField Specifies the search field. If {@code null} then all
     * search fields a considered.
     * @param conceptSchemeURI URI of the target concept scheme to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of concepts matching {@code keyword}
     */
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, int offset, int limit);

    /**
     * Lists the concepts matching the provided keyword and belonging to
     * the concept scheme and collection identified by the provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param searchField Specifies the search field. If {@code null} then all
     * search fields a considered.
     * @param conceptSchemeURI URI of the target concept scheme to restrict
     * search to
     * @param skosCollectionURI URI of the SKOS collection to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of concepts matching {@code keyword}
     */
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, String skosCollectionURI, int offset, int limit);

    /**
     * Lists the URIs of the concepts matching the provided keyword and belonging to
     * the concept schemes identified by the provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param searchField Specifies the search field. If {@code null} then all
     * search fields a considered.
     * @param conceptSchemeURIs List of URIs of concept schemes to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of concepts matching {@code keyword}
     */
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, int offset, int limit);

    /**
     * Lists the URIs of the concepts matching the provided keyword and
     * belonging to at least one of the concept schemes and at least one of the
     * collections identified by the provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param searchField Specifies the search field. If {@code null} then all
     * search fields a considered.
     * @param conceptSchemeURIs List of URIs of concept schemes to restrict
     * search to
     * @param skosCollectionURIs List of URIs of SKOS collections to restrict
     * search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of concepts matching {@code keyword}
     */
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit);
    
    /**
     * Lists the concepts matching the provided query. This method
     * accepts a Lucene query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link ie.cmrc.skos.index.IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of concepts matching {@code queryString}
     * @throws java.text.ParseException If a parsing error is encountered
     */
    public List<SKOSConcept> searchConcepts(String queryString, int offset, int limit) throws ParseException;
    
    /**
     * Lists the concepts matching the provided query and belonging to
     * the concept scheme identified by the provided URI. This method
     * accepts a Lucene query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link ie.cmrc.skos.index.IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param conceptSchemeURI URI of the target concept scheme to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of concepts matching {@code queryString} and belonging to
     * the concept scheme identified by the URI {@code conceptSchemeURI}
     * @throws java.text.ParseException If a parsing error is encountered
     */
    public List<SKOSConcept> searchConcepts(String queryString, String conceptSchemeURI, int offset, int limit) throws ParseException;
    
    /**
     * Lists the concepts matching the provided query and belonging to
     * the concept scheme and collection identified by the provided URIs. This method
     * accepts a Lucene query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link ie.cmrc.skos.index.IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param conceptSchemeURI URI of the target concept scheme to restrict search to
     * @param skosCollectionURI URI of the SKOS collection to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of concepts matching {@code queryString} and belonging to
     * the specified concept scheme and collection
     * @throws java.text.ParseException If a parsing error is encountered
     */
    public List<SKOSConcept> searchConcepts(String queryString, String conceptSchemeURI, String skosCollectionURI, int offset, int limit) throws ParseException;
    
    /**
     * Lists the concepts matching the provided query and belonging
     * to at least one of the concept schemes identified by the provided URIs.
     * This method accepts a Lucene query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link ie.cmrc.skos.index.IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param conceptSchemeURIs List of URIs of concept schemes to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of concepts matching {@code queryString} and belonging to
     * at least one of the concept schemes identified by the URIs in
     * {@code conceptSchemes}
     * @throws java.text.ParseException If a parsing error is encountered
     */
    public List<SKOSConcept> searchConcepts(String queryString, Collection<String> conceptSchemeURIs, int offset, int limit) throws ParseException;
    
    /**
     * Lists the concepts matching the provided query and belonging
     * to at least one of the concept schemes and at least one of the
     * collections identified by the provided URIs. This method accepts a Lucene
     * query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link ie.cmrc.skos.index.IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param conceptSchemeURIs List of URIs of concept schemes to restrict search to
     * @param skosCollectionURIs List of URIs of SKOS collections to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return List of concepts matching
     * {@code queryString} and belonging to at least one of the specified
     * concept schemes and at least one of the specified target collections
     * @throws java.text.ParseException If a parsing error is encountered
     */
    public List<SKOSConcept> searchConcepts(String queryString, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit) throws ParseException;

    
    /**
     * Interprets the provided keyword and returns the concepts that match its
     * meaning
     * @param keyword Keyword to interpret
     * @return List of concepts matching the meaning of the provided keyword
     */
    public List<SKOSConcept> interpretKeyword(Term keyword);
    
    /**
     * Interprets the provided keyword in the provided concept scheme
     * @param keyword Keyword to interpret
     * @param conceptSchemeURI URI of the target concept scheme
     * @return List of concepts matching the meaning of the provided keyword
     * in the specified concept scheme. If {@code conceptSchemeURI==null}
     * then this is equivalent to {@linkplain #interpretKeyword(ie.cmrc.util.Term)}.
     * If no concept scheme exists with the specified URI, then an empty list is
     * returned.
     */
    public List<SKOSConcept> interpretKeyword(Term keyword, String conceptSchemeURI);
    
    /**
     * Interprets the provided keyword in the provided concept scheme and
     * SKOS collection
     * @param keyword Keyword to interpret
     * @param conceptSchemeURI URI of the target concept scheme
     * @param skosCollectionURI URI of the target SKOS collection
     * @return List of concepts matching the meaning of the provided keyword
     * in the specified concept scheme and collection.
     * If {@code conceptSchemeURI==null} then results are not filtered by
     * concept scheme. Similarly if {@code skosCollectionURI==null} then
     * results are not filtered by SKOS collection.
     * If no concepts scheme or collection exist for the provided URIs, then an
     * empty list is returned.
     */
    public List<SKOSConcept> interpretKeyword(Term keyword, String conceptSchemeURI, String skosCollectionURI);
    
    /**
     * Interprets the provided keyword in the provided concept schemes
     * @param keyword Keyword to interpret
     * @param conceptSchemeURIs URIs of the target concept schemes
     * @return List of concepts matching the meaning of the provided keyword
     * and belonging to at least one of the target concept schemes.
     * If {@code conceptSchemeURIs} id {@code null} or empty, then this is
     * equivalent to {@linkplain #interpretKeyword(ie.cmrc.util.Term)}.
     * However, if no concept scheme exists with the provided URI then an
     * empty list is returned.
     */
    public List<SKOSConcept> interpretKeyword(Term keyword, Collection<String> conceptSchemeURIs);
    
    /**
     * Interprets the provided keyword in the provided concept schemes and
     * SKOS collections
     * @param keyword Keyword to interpret
     * @param conceptSchemeURIs URIs of the target concept schemes
     * @param skosCollectionURIs URIs of the target SKOS collections
     * @return List of concepts matching the meaning of the provided keyword
     * and belonging to at least one of the specified concept schemes and one
     * of the specified collections.
     * If {@code conceptSchemeURI} is {@code null} or empty then results are
     * not filtered by concept scheme. Similarly if {@code skosCollectionURI}
     * is {@code null} or empty then results are not filtered by SKOS collection.
     * If no concept scheme or collection exist for any of the provided URIs,
     * then an empty list is returned.
     */
    public List<SKOSConcept> interpretKeyword(Term keyword, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs);
    
    /**
     * Closes the {@link SWSThesaurus} and frees up resources held.
     * Not all implementations of {@link SWSThesaurus} require this method to
     * be called. But some do, so in general its best to call it when done with
     * the object, rather than leave it to the finalizer.
     * @throws java.io.IOException If an IO error is encountered
     */
    public void close() throws IOException;
}
