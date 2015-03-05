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

package ie.cmrc.smtx.skos.model;

import ie.cmrc.smtx.skos.model.hierarchy.HierarchyMethod;
import ie.cmrc.smtx.skos.model.hierarchy.SKOSConceptNode;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import java.util.Collection;
import java.util.List;

/**
 * Defines the common operations to be supported by a SKOS thesaurus.<br/>
 * Please note that this interface is not specific about the reasoning capabilities
 * of the thesaurus or its logical consistency. Whether inferences or logical
 * consistency checks are to be supported depends on the implementation.
 * 
 * @author Yassine Lassoued
 */
public interface SKOS {
    
    /**
     * URI of the SKOS ontology
     */
    public static final String URI = "http://www.w3.org/2004/02/skos/core";
    
    /**
     * Namespace of the SKOS ontology
     */
    public static final String NAMESPACE = "http://www.w3.org/2004/02/skos/core#";
    
    
    /**
     * Returns a closeable iterator over the SKOS resources of this thesaurus
     * @return {@link SKOSResource} iterator over the SKOS resources of the thesaurus
     */
    CloseableIterator<SKOSResource> listSKOSResources();
    
    /**
     * Lists the SKOS resources within this thesaurus. This method is to be used
     * with caution as it is not recommended with very large thesauri.
     * Use {@linkplain #getSKOSResources()} instead if the SKOSThesaurus is very large.
     * @return List of the SKOS resources ({@link SKOSResource}) contained in
     * the thesaurus
     */
    List<SKOSResource> getSKOSResources();
    
    /**
     * Returns the SKOS resource with the provided URI in this thesaurus
     * @param resourceURI URI of a SKOS Resource 
     * @return {@link SKOSResource} with the provided URI. If the URI is null or empty, or
     * no {@link SKOSResource} exists in the thesaurus with this URI, or the URI
     * belongs to a semantic resource other than {@link SKOSResource}, then
     * {@code null} is returned.
     */
    SKOSResource getSKOSResource(String resourceURI);
    
    /**
     * Returns the SKOS resource matching the provided one in this SKOS
     * thesaurus. This is useful when the provided SKOS resource object
     * may come from a different thesaurus but has an identical
     * resource in this thesaurus.
     * @param skosResource {@link SKOSResource} to get an identical for
     * @return {@link SKOSResource} in this thesaurus that is identical to
     * {@code skosResource}
     */
    SKOSResource getSKOSResource(SKOSResource skosResource);
    
    /**
     * Returns the type ({@link SKOSType}) of the resource associated with the
     * provide URI
     * @param resourceURI URI of a SKSO resource
     * @return SKOS resource type ({@link SKOSType}) of the resource associated with the
     * provide URI. If the provided URI is {@code null} or empty, or is not that
     * of a SKOS resource, then {@code null} is returned.
     */
    SKOSType getSKOSResourceType(String resourceURI);
    
    /**
     * Removes the SKOS resource with the provided URI from the thesaurus, if any
     * @param resourceURI URI of the resource to delete
     * @return This SKOSThesaurus to allow cascading calls
     * @throws IllegalArgumentException if the provided URI is used by a
     * semantic resource that is not a SKOS resource (e.g., and annotation property, etc.).
     */
    SKOS removeResource(String resourceURI) throws IllegalArgumentException;
    
    /**
     * Removes the provided SKOS resource from the thesaurus if it exists. If the
     * resource is removed then all its annotations and relationships are removed too.
     * If the provided resource does not exist then no changes are made to the thesaurus.
     * @param resource SKOSResource to remove from the thesaurus
     * @return This SKOSThesaurus to allow cascading calls
     */
    SKOS removeResource(SKOSResource resource);
    
    
    // -------- -------- -------- --------
    // Managing and accessing concept schemes
    
    /**
     * Lists the SKOS concept schemes of this thesaurus
     * @return Closeable iterator {@link ie.cmrc.skos.core.util.CloseableIterator}
     * over the SKOS concept schemes of the thesaurus
     */
    CloseableIterator<SKOSConceptScheme> listConceptSchemes();
    
    /**
     * Lists the SKOS concept schemes within the thesaurus. 
     * @return {@code List} of the SKOS concept schemes ({@link SKOSConceptScheme})
     * defined in the thesaurus if any; otherwise an <i>empty</i> {@code List}
     */
    List<SKOSConceptScheme> getConceptSchemes();
    
    /**
     * Returns the SKOS concept scheme identified by the provided URI
     * @param conceptSchemeURI URI of a SKOS concept scheme to extract
     * @return {@link ie.cmrc.skos.core.SKOSConceptScheme} with the provided URI.
     * If the URI is {@code null} or empty, or not that of a
     * {@link ie.cmrc.skos.core.SKOSConceptScheme}, then {@code null} is returned.
     */
    SKOSConceptScheme getConceptScheme(String conceptSchemeURI);
    
    /**
     * Creates a {@link SKOSConceptScheme} with the provided URI, if it does not exist.
     * If a {@link SKOSConceptScheme} already exists with the same URI then no modification is made.
     * @param conceptSchemeURI URI of the SKOS concept scheme to add
     * @return Added concept scheme
     * @throws IllegalArgumentException if a SKOS resource already exists
     * with the same URI and is not a {@link SKOSConceptScheme}
     */
    SKOSConceptScheme createConceptScheme(String conceptSchemeURI) throws IllegalArgumentException;
    
    
    // -------- -------- -------- --------
    // Managing and accessing collections
    
    /**
     * Returns a closeable iterator over the SKOS collections of this thesaurus
     * @return {@link SKOSCollection} iterator over the SKOS collections of the
     * thesaurus
     */
    CloseableIterator<SKOSCollection> listCollections();
    
    /**
     * Returns a closeable iterator over the SKOS collections that belong to the
     * provided conceptScheme
     * @param conceptScheme SKOS concept scheme to which the returned concepts
     * must belong
     * @return {@link SKOSCollection} iterator over the SKOS collections that
     * belong to {@code conceptScheme}. If {@code conceptScheme==null}, then
     * this is equivalent to {@linkplain #listCollections()}.
     */
    CloseableIterator<SKOSCollection> listCollections(SKOSConceptScheme conceptScheme);
    
    /**
     * Returns a closeable iterator over the SKOS collections that belong to the
     * provided SKOS collection
     * @param skosCollection SKOS collection to which the returned collections
     * must belong
     * @return {@link SKOSCollection} iterator over the SKOS collections that
     * belong to {@code skosCollection}. If {@code skosCollection==null}, then
     * this is equivalent to {@linkplain #listCollections()}.
     */
    CloseableIterator<SKOSCollection> listCollections(SKOSCollection skosCollection);
    
    /**
     * Returns a closeable iterator over the SKOS collections that belong to the
     * provided SKOS concept scheme and collection.
     * @param conceptScheme SKOS concept scheme to which the returned
     * collections must belong
     * @param skosCollection SKOS collection to which the returned collections
     * must belong
     * @return {@link SKOSCollection} iterator over the SKOS collections that
     * belong to {@code conceptScheme} and {@code skosCollection}.
     * If {@code conceptScheme==null}, then results are not filtered by
     * concept scheme.
     * If {@code skosCollection==null}, then results are not filtered by
     * collection.
     */
    CloseableIterator<SKOSCollection> listCollections(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection);
    
    
    
    /**
     * Lists the SKOS collections within the thesaurus. 
     * @return {@code List} of the SKOS collections ({@link SKOSCollection})
     * defined in the thesaurus if any; otherwise an <i>empty</i> {@code List}
     */
    List<SKOSCollection> getCollections();
    
    /**
     * Returns the SKOS collection identified by the provided URI
     * @param skosCollectionURI URI of a SKOS collection to extract
     * @return {@link ie.cmrc.skos.core.SKOSCollection} with the provided URI. If
     * the URI is {@code null} or empty, or
     * not that of a {@link SKOSCollection}, then {@code null} is returned.
     */
    SKOSCollection getCollection(String skosCollectionURI);
    
    /**
     * Creates a {@link SKOSCollection} with the provided URI, if it does not exist.
     * If a {@link SKOSCollection} already exists with the same URI then no modification is made.
     * @param collectionURI URI of the SKOS collection to add
     * @return Added collection
     * @throws IllegalArgumentException if a SKOS resource already exists
     * with the same URI and is not a {@link SKOSCollection}
     */
    SKOSCollection createCollection(String collectionURI) throws IllegalArgumentException;
    
    
    // -------- -------- -------- --------
    // Managing and accessing concepts
    
    /**
     * Returns a closeable iterator over the SKOS concepts of this thesaurus
     * @return {@link SKOSConcept} iterator over the SKOS concepts of the thesaurus
     */
    CloseableIterator<SKOSConcept> listConcepts();
    
    /**
     * Lists the SKOS concepts that belong to the provided conceptScheme
     * @param conceptScheme SKOS concept scheme to which the returned concepts
     * must belong
     * @return {@link ie.cmrc.skos.core.util.CloseableIterator} over the SKOS
     * concepts that belong to {@code conceptScheme}.
     * If {@code conceptScheme==null}, then
     * this is equivalent to {@linkplain #listConcepts()}.
     */
    CloseableIterator<SKOSConcept> listConcepts(SKOSConceptScheme conceptScheme);
    
    /**
     * Returns a closeable iterator over the SKOS concepts that belong to the
     * provided SKOS collection
     * @param skosCollection SKOS collection to which the returned concepts
     * must belong
     * @return {@link SKOSConcept} iterator over the SKOS concepts that belong
     * to {@code skosCollection}. If {@code skosCollection==null}, then
     * this is equivalent to {@linkplain #listConcepts()}.
     */
    CloseableIterator<SKOSConcept> listConcepts(SKOSCollection skosCollection);
    
    /**
     * Returns a closeable iterator over the SKOS concepts that belong to the
     * provided SKOS concept scheme and collection. This methods iterates over
     * the concept scheme members then filters by collection. Use this if
     * the concept scheme filtering is more restrictive than the collection
     * filtering.
     * @param conceptScheme SKOS concept scheme to which the returned concepts
     * must belong
     * @param skosCollection SKOS collection to which the returned concepts
     * must belong
     * @return {@link SKOSConcept} iterator over the SKOS concepts that belong
     * to {@code conceptScheme} and {@code skosCollection}.
     * If {@code conceptScheme==null}, then results are not filtered by
     * concept scheme.
     * If {@code skosCollection==null}, then results are not filtered by
     * collection.
     * @see #listConcepts(ie.cmrc.skos.core.SKOSCollection, ie.cmrc.skos.core.SKOSConceptScheme)
     */
    CloseableIterator<SKOSConcept> listConcepts(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection);
    
    /**
     * Returns a closeable iterator over the SKOS concepts that belong to the
     * provided SKOS collection and concept scheme. This methods iterates over
     * the collection members then filters by concept scheme. Use this if
     * the collection filtering is more restrictive than the concept scheme
     * filtering.
     * @param skosCollection SKOS collection to which the returned concepts
     * must belong
     * @param conceptScheme SKOS concept scheme to which the returned concepts
     * must belong
     * @return {@link SKOSConcept} iterator over the SKOS concepts that belong
     * to {@code skosCollection} and {@code conceptScheme}.
     * If {@code skosCollection==null}, then results are not filtered by
     * collection.
     * If {@code conceptScheme==null}, then results are not filtered by
     * concept scheme.
     * @see #listConcepts(ie.cmrc.skos.core.SKOSConceptScheme, ie.cmrc.skos.core.SKOSCollection)
     */
    CloseableIterator<SKOSConcept> listConcepts(SKOSCollection skosCollection, SKOSConceptScheme conceptScheme);
    
    
    /**
     * Lists the SKOS concept within the thesaurus. 
     * @return {@code List} of the SKOS concepts ({@link SKOSConcept})
     * defined in the thesaurus if any; otherwise an <i>empty</i> {@code List}
     */
    List<SKOSConcept> getConcepts();
    
    /**
     * Returns the SKOS concepts with the provided URI
     * @param conceptURI URI of a SKOS concept to return
     * @return {@link SKOSConcept} with the provided URI. If the URI is null or empty, or
     * not that of a {@link SKOSConcept}, then {@code null} is returned.
     */
    SKOSConcept getConcept(String conceptURI);
    
    /**
     * Creates a {@link SKOSConcept} with the provided URI, if it does not exist.
     * If a {@link SKOSConcept} already exists with the same URI then no modification is made.
     * @param conceptURI URI of the SKOS concept to add
     * @return Added concept
     * @throws IllegalArgumentException if a SKOS resource already exists
     * with the same URI and is not a {@link SKOSConcept}
     */
    SKOSConcept createConcept(String conceptURI) throws IllegalArgumentException;
    
    
    // -------- -------- -------- --------
    // Advanced querying
    
    /**
     * Lists the concepts related to the specified concept through the provided
     * relationship type
     * @param concept Source {@link SKOSConcept}
     * @param relationshipType Type of the relationship expressed as a
     * {@link SKOSSemanticProperty}
     * @return Iterator over the SKOS concepts related to
     * {@code concept} through {@code relationshipType} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code concept==null}, then this returns an
     * <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     */
    CloseableIterator<SKOSConcept> listSemanticRelations(SKOSConcept concept, SKOSSemanticProperty relationshipType);
    
    /**
     * Lists the concepts related to the specified concept through the provided
     * relationship type and belonging to the provided concept scheme
     * @param concept Source {@link SKOSConcept}
     * @param relationshipType Type of the relationship expressed as a {@link SKOSSemanticProperty}
     * @param conceptScheme Target concept scheme
     * @return Iterator over the SKOS concepts related to {@code concept} through
     * {@code relationshipType} and belonging to {@code conceptScheme} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code concept==null},
     * then this returns an <i>empty</i>
     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code conceptScheme==null} then this is equivalent to
     * {@linkplain #listSemanticRelations(ie.cmrc.skos.core.SKOSConcept, ie.cmrc.skos.core.SKOSSemanticProperty)}.
     */
    CloseableIterator<SKOSConcept> listSemanticRelations(SKOSConcept concept, SKOSSemanticProperty relationshipType, SKOSConceptScheme conceptScheme);
    
    /**
     * Lists the SKOS concepts related to the specified concept through the provided
     * relationship type and belonging to the provided SKOS concept scheme and
     * collection
     * @param concept Source {@link SKOSConcept}
     * @param relationshipType Type of the relationship expressed as a {@link SKOSSemanticProperty}
     * @param conceptScheme Target concept scheme
     * @param skosCollection Target collection
     * @return Iterator over the SKOS concepts related to {@code concept} through
     * {@code relationshipType} and belonging to {@code conceptScheme} and
     * {@code skosCollection} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code concept==null}, then
     * this returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code conceptScheme==null} then results are not filtered by concept scheme.
     * If {@code skosCollection==null} then results are not filtered by collection.
     */
    CloseableIterator<SKOSConcept> listSemanticRelations(SKOSConcept concept, SKOSSemanticProperty relationshipType, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection);
    
    /**
     * Lists the SKOS resources related to the specified concept through the provided
     * relationship type and belonging to at least one of the provided concept schemes
     * and at least one of the provided collections
     * @param concept Source {@link SKOSConcept}
     * @param relationshipType Type of the relationship expressed as a {@link SKOSSemanticProperty}
     * @param conceptSchemes Target concept schemes
     * @param skosCollections Target collections
     * @return Iterator over the SKOS concepts related to {@code concept} through
     * {@code relationshipType} and belonging to at least one of {@code conceptSchemes}
     * and at least one of {@code skosCollections} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code concept==null},
     * then this returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code conceptSchemes} is {@code null} or empty then results are not
     * filtered by concept schemes.
     * If {@code skosCollections} is {@code null} or empty then results are not
     * filtered by collections.
     */
    CloseableIterator<SKOSConcept> listSemanticRelations(SKOSConcept concept, SKOSSemanticProperty relationshipType, Collection<SKOSConceptScheme> conceptSchemes, Collection<SKOSCollection> skosCollections);
    
    
    /**
     * Lists the SKOS concepts that the provided concept is related to through
     * the provided relationship type
     * @param relationshipType Type of the relationship expressed as a
     * {@link SKOSSemanticProperty}
     * @param targetResource Target SKOS concepts
     * @return Iterator over the SKOS resources that {@code targetResource}
     * is related to through {@code relationshipType} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code targetResource==null}, then this returns an
     * <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     */
    CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType, SKOSResource targetResource);
    
    /**
     * Lists the concepts that the given concept is related to through
     * the provided relationship type and that belong to the provided concept scheme
     * @param relationshipType Type of the relationship expressed as a {@link SKOSSemanticProperty}
     * @param targetConcept Target SKOS concept of the relationship
     * @param conceptScheme Target concept scheme
     * @return Iterator over the SKOS concepts that {@code targetConcept} is related to through
     * {@code relationshipType} and that belong to {@code conceptScheme} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code targetConcept==null},
     * then this returns an <i>empty</i>
     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code conceptScheme==null} then this is equivalent to
     * {@linkplain #listSemanticRelationshipSources(ie.cmrc.skos.core.SKOSSemanticProperty, ie.cmrc.skos.core.SKOSResource)}.
     */
    CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType, SKOSConcept targetConcept, SKOSConceptScheme conceptScheme);
    
    /**
     * Lists the SKOS concepts that the provided concept is related to through the provided
     * relationship type and that belong to the provided concept scheme and collection
     * @param relationshipType Type of the relationship expressed as a {@link SKOSSemanticProperty}
     * @param targetConcept Target SKOS concept of the relationship
     * @param conceptScheme Target concept scheme
     * @param skosCollection Target collection
     * @return Iterator over the SKOS concepts that {@code targetCopncept} is related to through
     * {@code relationshipType} and that belong to {@code conceptScheme} and
     * {@code skosCollection} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code targetConcept==null}, then
     * this returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code conceptScheme==null} then results are not filtered by concept scheme.
     * If {@code skosCollection==null} then results are not filtered by collection.
     */
    CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType, SKOSConcept targetConcept, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection);
    
    /**
     * Lists the SKOS concepts that the provided concept is related to through the provided
     * relationship type and that belong to at least one of the provided concept schemes
     * @param relationshipType Type of the relationship expressed as a {@link SKOSSemanticProperty}
     * @param targetConcept Target SKOS concept of the relationship
     * @param conceptSchemes Target concept schemes
     * @return Iterator over the SKOS resources that {@code targetConcept} is related to through
     * {@code relationshipType} and that belong to at least one of {@code conceptSchemes} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code targetConcept==null},
     * then this returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code conceptSchemes} is {@code null} or empty then results are not
     * filtered by concept scheme.
     */
    CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType, SKOSConcept targetConcept, Collection<SKOSConceptScheme> conceptSchemes);
    
    /**
     * Lists the SKOS concepts that the provided concept is related to through the provided
     * relationship type and that belong to at least one of the provided concept schemes
     * and at least one of the provided collections
     * @param relationshipType Type of the relationship expressed as a {@link SKOSSemanticProperty}
     * @param targetConcept Target SKOS concept of the relationship
     * @param conceptSchemes Target concept schemes
     * @param skosCollections Target collections
     * @return Iterator over the SKOS concepts that {@code targetConcept} is related to through
     * {@code relationshipType} and that belong to at least one of {@code conceptSchemes}
     * and at least one of {@code skosCollections} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null} or {@code targetConcept==null},
     * then this returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code conceptSchemes} is {@code null} or empty then results are not
     * filtered by concept scheme.
     * If {@code skosCollections} is {@code null} or empty then results are not
     * filtered by collection.
     */
    CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType, SKOSConcept targetConcept, Collection<SKOSConceptScheme> conceptSchemes, Collection<SKOSCollection> skosCollections);
    
    /**
     * Lists the top concepts of all concept schemes in this SKOS thesaurus
     * @return {@link ie.cmrc.skos.core.util.CloseableIterator} over the concepts
     * that are asserted to be top concepts of at least one concept scheme in
     * this SKOS thesaurus
     */
    CloseableIterator<SKOSConcept> listTopConcepts();
    
    /**
     * Lists the top concepts of the provided concept scheme
     * @param conceptScheme SKOS concept scheme
     * @return {@link ie.cmrc.skos.core.util.CloseableIterator} over the top concepts
     * of {@code conceptScheme}. If {@code conceptScheme==null}, then this is
     * equivalent to {@linkplain #listTopConcepts()}.
     */
    CloseableIterator<SKOSConcept> listTopConcepts(SKOSConceptScheme conceptScheme);
    
    /**
     * Lists the top concepts of the provided concept scheme that belong to the
     * provided SKOS collection
     * @param conceptScheme SKOS concept scheme
     * @param skosCollection SKOS collection
     * @return {@link ie.cmrc.skos.core.util.CloseableIterator} over the top concepts
     * of {@code conceptScheme} that belong to {@code skosCollection}.
     * If {@code skosCollection==null}, then this is
     * equivalent to {@linkplain #listTopConcepts(ie.cmrc.skos.core.SKOSConceptScheme)}.
     * If {@code conceptScheme==null}, then all concept schemes are considered.
     */
    CloseableIterator<SKOSConcept> listTopConcepts(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection);
    
    /**
     * Lists the concepts that have no (transitive) broader concepts (other
     * than themselves) in this SKOS thesaurus
     * @return List of concepts that have no broader concepts
     */
    List<SKOSConcept> getBroadestConcepts();
    
    /**
     * Lists the concepts belonging to the provided concept scheme and which
     * do not have any (transitive) broader concepts within the same concept
     * scheme
     * @param conceptScheme SKOS concept scheme
     * @return List of concepts belonging to {@code conceptScheme} and having
     * no (transitive) broader concepts within {@code conceptScheme}
     */
    List<SKOSConcept> getBroadestConcepts(SKOSConceptScheme conceptScheme);
    
    /**
     * Lists the concepts belonging to the provided concept scheme and
     * collection, and which do not have any (transitive) broader concepts
     * within those concept scheme and collection
     * @param conceptScheme SKOS concept scheme
     * @param skosCollection SKOS collection
     * @return List of concepts belonging to {@code conceptScheme} and
     * and {@code skosCollection}, and having
     * no (transitive) broader concepts within {@code conceptScheme} or
     * {@code skosCollection}
     */
    List<SKOSConcept> getBroadestConcepts(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection);
    
    /**
     * Concepts that are immediately narrower than the provided concept. For a
     * given concept {@code X}, a concept {@code Y} is <b><i>immediately narrower</i></b>
     * than {@code X} if {@code Y} is semantically narrower than {@code X} and
     * no other concept {@code Z} exists such that {@code Z} is narrower than
     * {@code X} and {@code Y} is narrower than {@code Z}.
     * @param concept SKOS concept
     * @return List of concepts that are immediately narrower than
     * {@code concept}
     */
    List<SKOSConcept> getDirectNarrowerConcepts(SKOSConcept concept);
    
    /**
     * Concepts that are immediately narrower than the provided concept in the
     * provided concept scheme. For a given concept {@code X} and a concept
     * scheme {@code S}, a concept {@code Y} is <b><i>immediately narrower</i></b>
     * than {@code X} in {@code S} if {@code Y} is in scheme {@code S}, and is
     * semantically narrower than {@code X}, and if no other concept {@code Z}
     * exists in {@code S} such that {@code Z} is narrower than {@code X} and
     * {@code Y} is narrower than {@code Z}.
     * @param concept SKOS concept
     * @param conceptScheme SKOS concept scheme
     * @return List of concepts that are immediately narrower than
     * {@code concept} in {@code conceptScheme}
     */
    List<SKOSConcept> getDirectNarrowerConcepts(SKOSConcept concept, SKOSConceptScheme conceptScheme);
    
    /**
     * Concepts that are immediately narrower than the provided concept in the
     * provided concept scheme and collection. For a given concept {@code X}, a concept
     * scheme {@code S} and a SKOS collection {@code C}, a concept {@code Y} is
     * <b><i>immediately narrower</i></b> than {@code X} in {@code S} and {@code C}
     * if {@code Y} belongs to both {@code S} and {@code C}, and is
     * semantically narrower than {@code X}, and if no other concept {@code Z}
     * exists in both {@code S} and {@code C} such that {@code Z} is narrower
     * than {@code X} and {@code Y} is narrower than {@code Z}.
     * @param concept SKOS concept
     * @param conceptScheme SKOS concept scheme
     * @param skosCollection SKOS collection
     * @return List of concepts that are immediately narrower than
     * {@code concept} in {@code conceptScheme} and {@code skosCollection}
     */
    List<SKOSConcept> getDirectNarrowerConcepts(SKOSConcept concept, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection);
    
    /**
     * Concepts that are immediately broader than the provided concept. For a
     * given concept {@code X}, a concept {@code Y} is <b><i>immediately broader</i></b>
     * than {@code X} if {@code Y} is semantically broader than {@code X} and
     * no other concept {@code Z} exists such that {@code Z} is broader than
     * {@code X} and {@code Y} is broader than {@code Z}.
     * @param concept SKOS concept
     * @return List of concepts that are immediately broader than
     * {@code concept}
     */
    List<SKOSConcept> getDirectBroaderConcepts(SKOSConcept concept);
    
    /**
     * Concepts that are immediately broader than the provided concept in the
     * provided concept scheme. For a given concept {@code X} and a concept
     * scheme {@code S}, a concept {@code Y} is <b><i>immediately broader</i></b>
     * than {@code X} in {@code S} if {@code Y} is in scheme {@code S}, and is
     * semantically broader than {@code X}, and if no other concept {@code Z}
     * exists in {@code S} such that {@code Z} is broader than {@code X} and
     * {@code Y} is broader than {@code Z}.
     * @param concept SKOS concept
     * @param conceptScheme SKOS concept scheme
     * @return List of concepts that are immediately broader than
     * {@code concept} in {@code conceptScheme}
     */
    List<SKOSConcept> getDirectBroaderConcepts(SKOSConcept concept, SKOSConceptScheme conceptScheme);
    
    /**
     * Concepts that are immediately broader than the provided concept in the
     * provided concept scheme and collection. For a given concept {@code X}, a concept
     * scheme {@code S} and a SKOS collection {@code C}, a concept {@code Y} is
     * <b><i>immediately broader</i></b> than {@code X} in {@code S} and {@code C}
     * if {@code Y} belongs to both {@code S} and {@code C}, and is
     * semantically broader than {@code X}, and if no other concept {@code Z}
     * exists in both {@code S} and {@code C} such that {@code Z} is broader
     * than {@code X} and {@code Y} is broader than {@code Z}.
     * @param concept SKOS concept
     * @param conceptScheme SKOS concept scheme
     * @param skosCollection SKOS collection
     * @return List of concepts that are immediately broader than
     * {@code concept} in {@code conceptScheme} and {@code skosCollection}
     */
    List<SKOSConcept> getDirectBroaderConcepts(SKOSConcept concept, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection);
    
    /**
     * Builds the hierarchy of the concepts of this thesaurus using the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}. Please note that this
     * method is <b>not suitable for large thesauri</b>.
     * @return List of root concept nodes
     */
    List<SKOSConceptNode> getConceptHierarchy();
    
    /**
     * Builds the hierarchy of the concepts of this thesaurus using the provided
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}. Please note that this
     * method is <b>not suitable for large thesauri</b>.
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * If {@code null} then the default {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * id used.
     * @return List of root concept nodes
     */
    List<SKOSConceptNode> getConceptHierarchy(HierarchyMethod hierarchyMethod);
    
    /**
     * Builds the hierarchy of the concepts of the provided concept scheme
     * using the default {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}.
     * Please note that this method is not suitable for large concept schemes.
     * method is <b>not suitable for large concept schemes</b>.
     * @param conceptScheme Target concept scheme
     * @return List of root concept nodes. If {@code conceptScheme==null},
     * then this is equivalent to {@linkplain #getConceptHierarchy()}.
     */
    List<SKOSConceptNode> getConceptHierarchy(SKOSConceptScheme conceptScheme);
    
    /**
     * Builds the hierarchy of the concepts of the provided concept scheme
     * using the provided {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * Please note that this method is <b>not suitable for large concept
     * schemes</b>.
     * @param conceptScheme Target concept scheme
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * If {@code null} then the default {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * id used.
     * @return List of root concept nodes. If {@code conceptScheme==null},
     * then this is equivalent to
     * {@linkplain #getConceptHierarchy(ie.cmrc.skos.core.hierarchy.HierarchyMethod)}.
     */
    List<SKOSConceptNode> getConceptHierarchy(SKOSConceptScheme conceptScheme, HierarchyMethod hierarchyMethod);
    
    /**
     * Builds the hierarchy of the concepts belonging to the provided SKOS
     * collection using the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}. Please note that this
     * method is <b>not suitable for large collections</b>.
     * @param skosCollection Target SKOS collection
     * @return List of root concept nodes. . If {@code skosCollection==null},
     * then this is equivalent to {@linkplain #getConceptHierarchy()}. 
     */
    List<SKOSConceptNode> getConceptHierarchy(SKOSCollection skosCollection);
    
    /**
     * Builds the hierarchy of the concepts belonging to the provided SKOS
     * collection using the provided
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}. Please note that this
     * method is <b>not suitable for large collections</b>.
     * @param skosCollection Target SKOS collection
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * If {@code null} then the default {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * id used.
     * @return List of root concept nodes If {@code skosCollection==null},
     * then this is equivalent to
     * {@linkplain #getConceptHierarchy(ie.cmrc.skos.core.hierarchy.HierarchyMethod)}.
     */
    List<SKOSConceptNode> getConceptHierarchy(SKOSCollection skosCollection, HierarchyMethod hierarchyMethod);
    
    /**
     * Builds the hierarchy of the concepts of the provided concept scheme
     * and collection using the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param conceptScheme Target concept scheme
     * @param skosCollection Target collection
     * @return List of root concept nodes. If {@code conceptScheme==null} then
     * all concept schemes are considered. If {@code skosCollection==null} then
     * all collections are considered.
     */
    List<SKOSConceptNode> getConceptHierarchy(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection);
    
    /**
     * Builds the hierarchy of the concepts of the provided concept scheme
     * and collection using the provided
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param conceptScheme Target concept scheme
     * @param skosCollection Target collection
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * If {@code null} then the default {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * id used.
     * @return List of root concept nodes. If {@code conceptScheme==null} then
     * all concept schemes are considered. If {@code skosCollection==null} then
     * all collections are considered.
     */
    List<SKOSConceptNode> getConceptHierarchy(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection, HierarchyMethod hierarchyMethod);
    
    /**
     * Builds the hierarchy of the provided concept using the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param rootConcept Root concept of the hierarchy
     * @return Root concept node. If {@code rootConcept==null} then this returns
     * {@code null}.
     */
    SKOSConceptNode getConceptTree(SKOSConcept rootConcept);
    
    /**
     * Builds the hierarchy of the provided concept using the provided
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param rootConcept Root concept of the hierarchy
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * Please note that the root type of the hierarchy method is not relevant
     * here. If {@code hierarchyMethod==null} then the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod} id used.
     * @return Root concept node. If {@code rootConcept==null} then this returns
     * {@code null}.
     */
    SKOSConceptNode getConceptTree(SKOSConcept rootConcept, HierarchyMethod hierarchyMethod);
    
    /**
     * Builds the hierarchy of the provided concept in the provided concept
     * scheme using the default {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param rootConcept Root concept of the hierarchy
     * @param conceptScheme Target concept scheme
     * @return Root concept node. All concepts of the descendent nodes belong
     * to {@code conceptScheme}. The concept of the root node may not belong
     * to {@code conceptScheme}. If {@code rootConcept==null} then this returns
     * {@code null}. If {@code conceptScheme==null} then this is equivalent to
     * {@linkplain #getConceptTree(ie.cmrc.skos.core.SKOSConcept)}.
     */
    SKOSConceptNode getConceptTree(SKOSConcept rootConcept, SKOSConceptScheme conceptScheme);
    
    /**
     * Builds the hierarchy of the provided concept in the provided concept
     * scheme using the provided {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param rootConcept Root concept of the hierarchy
     * @param conceptScheme Target concept scheme
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * Please note that the root type of the hierarchy method is not relevant
     * here. If {@code hierarchyMethod==null} then the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod} id used.
     * @return Root concept node. All concepts of the descendent nodes belong
     * to {@code conceptScheme}. The concept of the root node may not belong
     * to {@code conceptScheme}. If {@code rootConcept==null} then this returns
     * {@code null}. If {@code conceptScheme==null} then this is equivalent to
     * {@linkplain #getConceptTree(ie.cmrc.skos.core.SKOSConcept, ie.cmrc.skos.core.hierarchy.HierarchyMethod)}.
     */
    SKOSConceptNode getConceptTree(SKOSConcept rootConcept, SKOSConceptScheme conceptScheme, HierarchyMethod hierarchyMethod);
    
    /**
     * Builds the hierarchy of the provided concept in the provided concept
     * scheme and collection using the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param rootConcept Root concept of the hierarchy
     * @param conceptScheme Target concept scheme
     * @param skosCollection Target SKOS collection
     * @return Root concept node. All concepts of the descendent nodes belong
     * to {@code conceptScheme} and {@code skosCollection}. The concept of the
     * root node may not. If {@code rootConcept==null} then this returns
     * {@code null}. If {@code conceptScheme==null} then
     * all concept schemes are considered. If {@code skosCollection==null} then
     * all collections are considered.
     */
    SKOSConceptNode getConceptTree(SKOSConcept rootConcept, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection);
    
    /**
     * Builds the hierarchy of the provided concept in the provided concept
     * scheme and collection using the provided
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod}
     * @param rootConcept Root concept of the hierarchy
     * @param conceptScheme Target concept scheme
     * @param skosCollection Target SKOS collection
     * @param hierarchyMethod Method to use to build the concept hierarchy.
     * Please note that the root type of the hierarchy method is not relevant
     * here. If {@code hierarchyMethod==null} then the default
     * {@link ie.cmrc.skos.core.hierarchy.HierarchyMethod} id used.
     * @return Root concept node. All concepts of the descendent nodes belong
     * to {@code conceptScheme} and {@code skosCollection}. The concept of the
     * root node may not. If {@code rootConcept==null} then this returns
     * {@code null}. If {@code conceptScheme==null} then
     * all concept schemes are considered. If {@code skosCollection==null} then
     * all collections are considered.
     */
    SKOSConceptNode getConceptTree(SKOSConcept rootConcept, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection, HierarchyMethod hierarchyMethod);
    
    
    // -------- -------- -------- --------
    // Managing connection
    
    /**
     * For persistent thesauri, this method synchronises the {@link SKOS}
     * with its backend. For in memory thesauri, this method does not do anything.
     * @return This thesaurus to enable cascading calls
     */
    SKOS sync();
    
    /**
     * Commits the latest transactions made to the SKOS thesaurus. This is
     * particularly useful for persistent transactional thesauri.
     * 
     * @return This thesaurus to enable cascading calls
     */
    SKOS commit();
    
    /**
     * Closes the {@code SKOSThesaurus} and frees up resources held.
     * Not all implementations of {@code SKOSThesaurus} require this method to
     * be called. But some do, so in general its best to call it when done with
     * the object, rather than leave it to the finalizer.
     */
    void close();
    
    /**
     * Indicates whether this {@code SKOSThesaurus} has been closed
     * @return {@code true} if {@linkplain #close()} has been called on this {@code SKOSThesaurus},
     * {@code false} otherwise.
     */
    boolean isClosed();
    
}
