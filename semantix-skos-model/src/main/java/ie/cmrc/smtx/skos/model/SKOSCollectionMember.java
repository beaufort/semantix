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
 * A SKOS resource element is any {@link SKOSResource} that may belong to a another SKOS resource.
 * In practice a SKOSResourceElement may be a {@link SKOSConcept} or a {@link SKOSCollection}.
 * A SKOSResourceElement may belong to a {@link SKOSConceptScheme} or a {@link SKOSCollection}.
 * @author Yassine Lassoued
 */
public interface SKOSCollectionMember extends SKOSResource {

    
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Managing and accessing parent collections
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * Adds this {@link SKOSCollectionMember} to the {@link SKOSCollection} identified
     * by the provided URI
     * @param collectionUri URI of a {@link SKOSCollection} to add this resource to
     * @return This {@link SKOSCollectionMember} to allow cascading calls
     */
    SKOSCollectionMember addToCollection(String collectionUri);

    /**
     * Adds this {@link SKOSCollectionMember} to the provided {@link SKOSCollection}
     * @param collection {@link SKOSCollection} to add this resource to
     * @return This {@link SKOSCollectionMember} to allow cascading calls
     */
    SKOSCollectionMember addToCollection(SKOSCollection collection);

    /**
     * Collections that this {@link SKOSCollectionMember} is a direct member of
     * @return {@code CloseableIterator<SKOSCollection>} over the SKOS collections
     * that this {@link SKOSCollectionMember} belongs to
     */
    CloseableIterator<SKOSCollection> listCollections();

    /**
     * Collections that this {@link SKOSCollectionMember} belongs to directly
     * or indirectly
     * @return {@code CloseableIterator<SKOSCollection>} over the SKOS collections
     * that this {@link SKOSCollectionMember} belongs to directly or indirectly
     */
    CloseableIterator<SKOSCollection> listCollectionsTransitive();
    
    /**
     * Collections that this {@link SKOSCollectionMember} belongs to
     * @return {@code List<SKOSCollection>} containing all the SKOS collections
     * that this {@link SKOSCollectionMember} belongs to
     */
    List<SKOSCollection> getCollections();
    
    /**
     * Collections that this {@link SKOSCollectionMember} directly or
     * indirectly belongs to
     * @return {@code List<SKOSCollection>} containing all the SKOS collections
     * that this {@link SKOSCollectionMember} belongs to directly or indirectly
     */
    List<SKOSCollection> getCollectionsTransitive();

    /**
     * Checks whether this {@link SKOSCollectionMember} is a direct member of the
     * {@link SKOSCollection} identified by the provided URI
     * @param collectionURI URI of a {@link SKOSCollection}
     * @return {@code true} if this {@link SKOSCollectionMember} is a direct member of the
     * {@link SKOSCollection} whose URI is {@code collectionURI},
     * {@code false} otherwise. If no SKOS collection exists with the provided URI
     * then {@code false} is returned.
     */
    boolean isMemberOfCollection(String collectionURI);

    /**
     * Checks whether this {@link SKOSCollectionMember} is a direct or indirect
     * member of the {@link SKOSCollection} identified by the provided URI
     * @param collectionURI URI of a {@link SKOSCollection}
     * @return {@code true} if this {@link SKOSCollectionMember} is a direct
     * or indirect member of the {@link SKOSCollection} whose URI is {@code collectionURI},
     * {@code false} otherwise. If no SKOS collection exists with the provided URI
     * then {@code false} is returned.
     */
    boolean isTransitiveMemberOfCollection(String collectionURI);

    /**
     * Checks whether this {@link SKOSCollectionMember} belongs to the specified
     * {@link SKOSCollection}
     * @param collection SKOS collection to check whether this
     * {@link SKOSCollectionMember} belongs to
     * @return {@code true} if this {@link SKOSCollectionMember} belongs to {@code collection},
     * {@code false} otherwise, or if {@code collection==null}
     */
    boolean isMemberOfCollection(SKOSCollection collection);

    /**
     * Checks whether this {@link SKOSCollectionMember} is a direct or indirect
     * member of the specified {@link SKOSCollection}
     * @param collection SKOS collection to check whether this
     * {@link SKOSCollectionMember} directly or indirectly belongs to
     * @return {@code true} if this {@link SKOSCollectionMember} belongs to {@code collection},
     * {@code false} otherwise, or if {@code collection==null}
     */
    boolean isTransitiveMemberOfCollection(SKOSCollection collection);

    /**
     * Removes this {@link SKOSCollectionMember}, as a member, from all SKOS collections
     * @return This {@link SKOSCollectionMember} to allow cascading calls
     */
    SKOSCollectionMember removeFromAllCollections();

    /**
     * Removes this {@link SKOSCollectionMember}, as a member, from the collection
     * identified by the provided URI
     * @param collectionURI URI of a {@link SKOSCollection}
     * @return This {@link SKOSCollectionMember} to allow cascading calls
     */
    SKOSCollectionMember removeFromCollection(String collectionURI);

    /**
     * Removes this {@link SKOSCollectionMember}, as a member, from the provided
     * SKOS collection
     * @param collection {@link SKOSCollection} to remove this {@link SKOSCollectionMember} from
     * @return This {@link SKOSCollectionMember} to allow cascading calls
     */
    SKOSCollectionMember removeFromCollection(SKOSCollection collection);
    
}
