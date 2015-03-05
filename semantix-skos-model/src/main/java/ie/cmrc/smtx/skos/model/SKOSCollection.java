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
 * A generic interface for representing SKOS collections
 * @author Yassine Lassoued
 */
public interface SKOSCollection extends SKOSCollectionMember {
    
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Managing and accessing members
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * Adds the {@link SKOSConcept} or {@link SKOSCollection} identified by the
     * provided URI to this collection
     * @param conceptOrCollectionURI URI of the {@link SKOSConcept} or {@link SKOSCollection}
     * to add to this collection
     * @return This collection to allow cascading calls
     * @throws IllegalArgumentException If the provided URI is not that of
     * a {@link SKOSCollectionMember} (i.e., {@link SKOSConcept} or {@link SKOSCollection})
     */
    SKOSCollection addMember(String conceptOrCollectionURI) throws IllegalArgumentException;

    /**
     * Adds the provided SKOSResource to this collection
     * @param skosCollectionMember {@link SKOSCollectionMember} to add to to the collection
     * @return This collection to allow cascading calls
     */
    SKOSCollection addMember(SKOSCollectionMember skosCollectionMember);
    
//    /**
//     * Checks whether the {@link SKOSResource} identified by the provided
//     * URI is a member of this collection
//     * @param resourceURI URI of the SKOSResource to check
//     * @return {@code true} if the resource identified by the URI {@code resourceURI}
//     * is a member of this collection; {@code false} otherwise
//     */
//    boolean hasMember(String resourceURI);
    
    /**
     * Checks whether the provided {@link SKOSResource} (typically a {@link SKOSConcept}
     * or {@link SKOSCollection}) is a member of this collection
     * @param skosCollectionMember {@link SKOSCollectionMember} to check
     * @return {@code true} if the {@code skosCollectionMember} is a member of this collection,
     * {@code false} otherwise
     */
    boolean hasMember(SKOSCollectionMember skosCollectionMember);
    
    /**
     * Checks whether the provided {@link SKOSResource} (typically a {@link SKOSConcept}
     * or {@link SKOSCollection}) is a direct or indirect member of this collection
     * @param skosCollectionMember {@link SKOSCollectionMember} to check
     * @return {@code true} if the {@code skosCollectionMember} is a
     * direct or indirect member of this collection, {@code false} otherwise
     */
    boolean hasMemberTransitive(SKOSCollectionMember skosCollectionMember);
    
//    /**
//     * Removes the {@link SKOSCollectionMember} identified by the provided URI from this
//     * collection
//     * @param skosResourceEltURI URI of the {@link SKOSCollectionMember} to remove from
//     * this collection
//     * @return This collection to allow cascading calls
//     * @throws IllegalArgumentException If the provided URI is not that of
//     * a {@link SKOSCollectionMember} (i.e., {@link SKOSConcept} or {@link SKOSCollection})
//     */
//    SKOSCollection removeMember(String skosResourceEltURI) throws IllegalArgumentException;
    
    /**
     * Removes the provided {@link SKOSCollectionMember} from this collection if possible
     * @param skosCollectionMember {@link SKOSCollectionMember} to remove from this collection
     * @return This collection to allow cascading calls
     */
    SKOSCollection removeMember(SKOSCollectionMember skosCollectionMember);
    
    /**
     * Returns a {@link ie.cmrc.skos.core.util.CloseableIterator} over the
     * members of this collection
     * @return {@code CloseableIterator<SKOSResourceElement>} over the members of this
     * collection. If the collection is empty, then an <i>empty</i> iterator is returned
     */
    CloseableIterator<SKOSCollectionMember> listMembers();
    
    /**
     * Returns a {@link ie.cmrc.skos.core.util.CloseableIterator} over the
     * direct and indirect members of this collection
     * @return {@code CloseableIterator<SKOSResourceElement>} over the direct
     * and indirect members of this collection. If the collection is empty,
     * then an <i>empty</i> iterator is returned
     */
    CloseableIterator<SKOSCollectionMember> listMembersTransitive();
    
    /**
     * Returns the list of members of this collection
     * @return {@code List<SKOSCollectionMember>} containing all the members of this
     * collection. Is the collection is empty, then an <i>empty</i> list is returned
     */
    List<SKOSCollectionMember> getMembers();
    
    /**
     * Returns the list of direct and indirect members of this collection
     * @return {@code List<SKOSCollectionMember>} containing all the direct
     * and indirect members of this collection. If the collection is empty,
     * then an <i>empty</i> list is returned
     */
    List<SKOSCollectionMember> getMembersTransitive();
    
//    /**
//     * Returns the list of SKOS resource elements that belong to this collection
//     * and are in the concept scheme identified by the provided URI
//     * @param conceptSchemeURI URI of a concept scheme
//     * @return {@code List<SKOSResourceElement>} containing the members of this
//     * collection that belong to the concept scheme whose URI is {@code conceptSchemeURI}.
//     * If the provided URI is null or not that of a concept scheme or if no
//     * members of this collection belong to the provided concept scheme, then
//     * an <i>empty</i> list is returned rather than a {@code null} value.
//     */
//    List<SKOSCollectionMember> getMembers(String conceptSchemeURI);
    
//    /**
//     * Returns the list of SKOS resource elements that belong to this collection
//     * and are in the provided concept scheme
//     * @param conceptScheme {@link SKOSConceptScheme} to filter the results by
//     * @return {@code List<SKOSResourceElement>} containing the members of this
//     * collection that belong to {@code conceptScheme}.
//     * If the provided conceptScheme is null or if no
//     * members of this collection belong to the provided concept scheme, then
//     * an <i>empty</i> list is returned rather than a {@code null} value.
//     */
//    List<SKOSCollectionMember> getMembers(SKOSConceptScheme conceptScheme);
    
    /**
     * Returns a {@link ie.cmrc.skos.core.util.CloseableIterator} over the
     * member concepts of this collection
     * @return {@code CloseableIterator<SKOSConcepts>} over the member concepts of this
     * collection. If the collection is empty or contains no concepts, then an
     * <i>empty</i> iterator is returned
     */
    CloseableIterator<SKOSConcept> listConceptMembers();
    
    /**
     * Returns a {@link ie.cmrc.skos.core.util.CloseableIterator} over the
     * direct and indirect member concepts of this collection
     * @return {@code CloseableIterator<SKOSConcepts>} over the direct
     * and idirect member concepts of this
     * collection. If the collection is empty or contains no concepts, then an
     * <i>empty</i> iterator is returned
     */
    CloseableIterator<SKOSConcept> listConceptMembersTransitive();
    
    /**
     * Returns all the member concepts of this collection
     * @return {@code List<SKOSConcept>} containing all the concepts belonging
     * to this collection. If the collection contains no concepts, then an
     * <i>empty</i> list is returned.
     */
    List<SKOSConcept> getConceptMembers();
    
    /**
     * Returns all the direct and indirect member concepts of this collection
     * @return {@code List<SKOSConcept>} containing all the concepts that
     * directly or indirectly belong
     * to this collection. If the collection contains no concepts, then an
     * <i>empty</i> list is returned.
     */
    List<SKOSConcept> getConceptMembersTransitive();
    
//    /**
//     * Returns all the member concepts of this collection that are in the concept
//     * scheme identified by the provided URI
//     * @param conceptSchemeURI URI of a concept scheme
//     * @return {@code List<SKOSConcept>} containing the concepts belonging to this
//     * collection and to the concept scheme whose URI is {@code conceptSchemeURI}.
//     * If the provided URI is null or not that of a concept scheme or if no
//     * concepts of this collection belong to the provided concept scheme, then
//     * an <i>empty</i> list is returned rather than a {@code null} value.
//     */
//    List<SKOSConcept> getConceptMembers(String conceptSchemeURI);
    
//    /**
//     * Returns the list of SKOS concepts that belong to this collection
//     * and are in the provided concept scheme
//     * @param conceptScheme {@link SKOSConceptScheme} to filter the results by
//     * @return {@code List<SKOSConcept>} containing the concepts belonging to this
//     * collection and to the provided concept scheme ({@code conceptScheme}).
//     * If the provided concept scheme is null or has no
//     * concepts in this collection, then
//     * an <i>empty</i> list is returned rather than a {@code null} value.
//     */
//    List<SKOSConcept> getConceptMembers(SKOSConceptScheme conceptScheme);
    
    /**
     * Returns a {@link ie.cmrc.skos.core.util.CloseableIterator} over the
     * member collections of this collection
     * @return {@code CloseableIterator<SKOSCollection>} over the member collections of this
     * collection. Is the collection is empty or contains no collections, then an
     * <i>empty</i> iterator is returned
     */
    CloseableIterator<SKOSCollection> listCollectionMembers();
    
    /**
     * Returns a {@link ie.cmrc.skos.core.util.CloseableIterator} over the
     * direct and indirect member collections of this collection
     * @return {@code CloseableIterator<SKOSCollection>} over the direct
     * and indirect member collections of this
     * collection. Is the collection is empty or contains no collections, then an
     * <i>empty</i> iterator is returned
     */
    CloseableIterator<SKOSCollection> listCollectionMembersTransitive();
    
    /**
     * Returns all the collections belonging to this collection
     * @return {@code List<SKOSCollection} containing all the collections belonging
     * to this one. If the collection contains no collection, then an
     * <i>empty</i> list is returned.
     */
    List<SKOSCollection> getCollectionMembers();
    
    /**
     * Returns all the collections that directly or indirectly belong to this one
     * @return {@code List<SKOSCollection} containing all the collections
     * that belong to this one directly or indirectly. If the collection
     * contains no collection, then an <i>empty</i> list is returned.
     */
    List<SKOSCollection> getCollectionMembersTransitive();
    
//    /**
//     * Returns all the collections belonging to this collection and that are in the concept
//     * scheme identified by the provided URI
//     * @param conceptSchemeURI URI of a concept scheme
//     * @return {@code List<SKOSCollection>} containing the collections belonging to this
//     * collection and to the concept scheme whose URI is {@code conceptSchemeURI}.
//     * If the provided URI is null or not that of a concept scheme or if no
//     * member collections belong to the provided concept scheme, then
//     * an <i>empty</i> list is returned rather than a {@code null} value.
//     */
//    List<SKOSCollection> getCollectionMembers(String conceptSchemeURI);
    
//    /**
//     * Returns the list of SKOS collections that belong to this collection
//     * and are in the provided concept scheme
//     * @param conceptScheme {@link SKOSConceptScheme} to filter the results by
//     * @return {@code List<SKOSCollection>} containing the collections belonging to this
//     * collection and to the provided concept scheme ({@code conceptScheme}).
//     * If the provided concept scheme is null has no
//     * collections within this collection, then
//     * an <i>empty</i> list is returned rather than a {@code null} value.
//     */
//    List<SKOSCollection> getCollectionMembers(SKOSConceptScheme conceptScheme);
    
}
