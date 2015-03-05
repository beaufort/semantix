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

import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import java.util.List;

/**
 * A generic interface for representing SKOS concept schemes
 * @author Yassine Lassoued
 */
public interface SKOSConceptScheme extends SKOSResource {

//    /**
//     * Adds the {@link SKOSConcept} or {@link SKOSCollection} identified by the
//     * provided URI in this concept scheme
//     * @param skosConceptOrCollectionURI URI of a {@link SKOSConcept} or {@link SKOSCollection}
//     * @return This {@link SKOSConceptScheme} to allow cascading calls
//     * @throws IllegalArgumentException If the provided URI is not that of
//     * a {@link SKOSCollectionMember} (i.e., {@link SKOSConcept} or {@link SKOSCollection})
//     */
//    SKOSConceptScheme add(String skosConceptOrCollectionURI) throws IllegalArgumentException;
    
    /**
     * Adds the provided {@link SKOSResource} to this concept scheme
     * @param skosResource {@link SKOSResource} to add to to the concept
     * scheme
     * @return This {@link SKOSConceptScheme} to allow cascading calls
     */
    SKOSConceptScheme add(SKOSResource skosResource);
    
//    /**
//     * Checks whether the concept scheme includes the {@link SKOSResource}
//     * identified by the provided URI
//     * @param skosResourceURI URI of a {@link SKOSResource}
//     * @return {@code true} if the conceptScheme includes the {@link SKOSResource}
//     * whose URI is {@code skosResourceURI}
//     */
//    boolean has(String skosResourceURI);
    
    /**
     * Checks whether the concept scheme includes the provided {@link SKOSResource}
     * @param skosResource {@link SKOSResource} to check
     * @return {@code true} if the provided {@link SKOSResource} is non {@code null}
     * and belongs to this concept scheme; {@code false} otherwise
     */
    boolean has(SKOSResource skosResource);
    
//    /**
//     * Removes the {@link SKOSResource} identified by the
//     * provided URI from this concept scheme
//     * @param skosResourceURI URI of the {@link SKOSResource} to remove
//     * @return This {@link SKOSConceptScheme} to allow cascading calls
//     */
//    SKOSConceptScheme remove(String skosResourceURI);
    
    /**
     * Removes the provided {@link SKOSResource} from this concept scheme if possible
     * @param skosResource {@link SKOSResource} to remove from this concept scheme
     * @return This {@link SKOSConceptScheme} to allow cascading calls
     */
    SKOSConceptScheme remove(SKOSResource skosResource);
    
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Managing and accessing concepts
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * Creates a concept with the provided URI and adds it to this concept scheme
     * @param conceptURI URI of the concept to create. If a concept already exists with
     * this URI then it will be added to this concept scheme
     * @return The added {@link SKOSConcept}
     * @throws IllegalArgumentException If the provided URI is that of an existing {@link SKOSResource}
     * that is not a {@link SKOSConcept}
     */
    SKOSConcept createConcept(String conceptURI) throws IllegalArgumentException;
    
    /**
     * Returns a closeable iterator over the concepts of this concept scheme
     * @return {@code CloseableIterator<SKOSConcept>} over the concepts of this concept
     * scheme. If the concept scheme contains no concepts, then an empty iterator
     * is returned.
     */
    CloseableIterator<SKOSConcept> listConcepts();
    
    /**
     * Returns the list of concepts within this concept scheme
     * @return {@code List<SKOSConcept>} containing the concepts belonging to this
     * concept scheme. If the concept scheme contains no concepts, then an empty list
     * is returned.
     */
    List<SKOSConcept> getConcepts();
    
//    /**
//     * Returns the list of concepts belonging to this concept scheme and to the
//     * collection identified by the provided URI
//     * @param collectionURI URI of a {@link SKOSCollection}
//     * @return {@code List<SKOSConcept>} containing the concepts belonging to
//     * this concept scheme and to the collection whose URI is {@code collectionURI}.
//     * If the provided URI is null or not that of a SKOS collection or if no
//     * concepts of this concept scheme belong to the provided collection, then
//     * an <i>empty</i> list is returned rather than a {@code null} value.
//     */
//    List<SKOSConcept> getConcepts(String collectionURI);
    
//    /**
//     * Returns the list of concepts belonging to this concept scheme and to the
//     * provided {@link SKOSCollection}
//     * @param collection {@link SKOSCollection} to filter the results by
//     * @return {@code List<SKOSConcept>} containing the concepts belonging to this
//     * concept scheme and to the provided collection ({@code collection}).
//     * If {@code collection} is null or has no
//     * concepts in this concept scheme, then
//     * an <i>empty</i> list is returned rather than a {@code null} value.
//     */
//    List<SKOSConcept> getConcepts(SKOSCollection collection);
    
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Managing and accessing top concepts
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * Adds the provided {@link SKOSConcept} as a top concept for this
     * concept scheme
     * @param concept {@link SKOSConcept} to add as top concept
     * @return This {@link SKOSConceptScheme} to allow cascading calls
     */
    SKOSConceptScheme addTopConcept(SKOSConcept concept);

//    /**
//     * Checks whether the concept identified by the provided URI is a top concept
//     * of this concept scheme
//     * @param conceptURI URI of a {@link SKOSConcept} to check
//     * @return {@code true} if the concept whose URI is conceptURI is a top concept
//     * of this concept scheme, {@code false} otherwise
//     */
//    boolean hasTopConcept(String conceptURI);
    
    /**
     * Checks whether the provide concept is a top concept of this concept scheme
     * @param concept {@link SKOSConcept} to check
     * @return {@code true} if {@code concept} is a top concept
     * of this concept scheme, {@code false} otherwise
     */
    boolean hasTopConcept(SKOSConcept concept);

    /**
     * Removes the provided concept from the list of top concepts of
     * this concept scheme. Please note that the concept will remain in the
     * concept scheme after this operation.
     * @param concept {@link SKOSConcept} to remove as top concept
     * @return This {@link SKOSConceptScheme} to allow cascading calls
     */
    SKOSConceptScheme removeTopConcept(SKOSConcept concept);

    /**
     * Removes all the top concepts of this concept scheme, if any, from the list of
     * top concepts
     * @return This {@link SKOSConceptScheme} to allow cascading calls
     */
    SKOSConceptScheme removeTopConcepts();
    
    /**
     * Returns the list of top concepts of this concept scheme
     * @return {@code List<SKOSConcept>} containing all the top concepts of this
     * concept scheme, if any, otherwise an <i>empty</i> list
     */
    List<SKOSConcept> getTopConcepts();
    
//    /**
//     * Returns the list of top concepts of this concept scheme that belong to
//     * the {@link SKOSCollection} identified by the provided URI
//     * @param collectionURI URI of a {@link SKOSCollection}
//     * @return {@code List<SKOSConcept>} containing all the top concepts of this
//     * concept scheme belonging to the {@link SKOSCollection} whose URI is {@code collectionURI},
//     * if any, otherwise an <i>empty</i> list. If {@code collectionURI} is
//     * {@code null} is not a URI of an existing
//     * SKOS collection, then an <i>empty</i> list is returned.
//     */
//    List<SKOSConcept> getTopConcepts(String collectionURI);
//    
//    /**
//     * Returns the list of top concepts of this concept scheme that belong to
//     * the provided {@link SKOSCollection}
//     * @param collection {@link SKOSCollection} to filter the results by
//     * @return scheme belonging {@code collection},
//     * if any, otherwise an <i>empty</i> list. If {@code collection} is
//     * {@code null} or empty, then an <i>empty</i> list is returned.
//     */
//    List<SKOSConcept> getTopConcepts(SKOSCollection collection);
    
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Accessing broadest concepts
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    //boolean hasBroadestConcept(String conceptURI);
    
    //boolean hasBroadestConcept(SKOSConcept concept);
    
    //List<SKOSConcept> getBroadestConcepts();
    
    //List<SKOSConcept> getBroadestConcepts(String collectionURI);
    
    //List<SKOSConcept> getBroadestConcepts(SKOSCollection collection);
    
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Managing and accessing collections
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * Creates a {@link SKOSCollection} with the provided URI and adds it to this concept scheme
     * @param collectionURI URI of the collection to create. If a collection already exists with
     * this URI then it will be added to this concept scheme
     * @return The added {@link SKOSCollection}
     * @throws IllegalArgumentException If the provided URI is that of an existing {@link SKOSResource}
     * that is not a {@link SKOSCollection}
     */
    SKOSCollection createCollection(String collectionURI) throws IllegalArgumentException;
    
    /**
     * Returns a closeable iterator over the collections of this concept scheme
     * @return {@code CloseableIterator<SKOSCollection>} over the collections belonging to this concept
     * scheme. If the concept scheme contains no collections, then an empty iterator
     * is returned.
     */
    CloseableIterator<SKOSCollection> listCollections();
    
    /**
     * Returns the list of collections within this concept scheme
     * @return {@code List<SKOSCollection>} containing the collections belonging to this
     * concept scheme. If the concept scheme contains no collections, then an empty list
     * is returned.
     */
    List<SKOSCollection> getCollections();
    
//    /**
//     * Returns the list of SKOS collections belonging to this concept scheme and to the
//     * collection identified by the provided URI
//     * @param collectionURI URI of a {@link SKOSCollection}
//     * @return {@code List<SKOSCollection>} containing the collections belonging to
//     * this concept scheme and to the collection whose URI is {@code collectionURI}.
//     * If the provided URI is null or not that of a SKOS collection or if no
//     * collections of this concept scheme belong to the provided collection, then
//     * an <i>empty</i> list is returned rather than a {@code null} value.
//     */
//    List<SKOSCollection> getCollections(String collectionURI);
//    
//    /**
//     * Returns the list of SKOS collections belonging to this concept scheme and to the
//     * provided {@link SKOSCollection}
//     * @param collection {@link SKOSCollection} to filter the results by
//     * @return {@code List<SKOSCollection>} containing the SKOS collections belonging to this
//     * concept scheme and to the provided collection ({@code collection}).
//     * If {@code collection} is null or has no
//     * sub collections in this concept scheme, then
//     * an <i>empty</i> list is returned rather than a {@code null} value.
//     */
//    List<SKOSCollection> getCollections(SKOSCollection collection);
    
}
