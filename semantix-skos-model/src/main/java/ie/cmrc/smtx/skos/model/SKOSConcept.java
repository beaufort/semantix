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

/**
 * A generic interface for representing SKOS concepts
 * @author Yassine Lassoued
 */
public interface SKOSConcept extends SKOSCollectionMember {
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Managing and accessing concept schemes
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * Makes this concept a top concept of the concept scheme identified by the
     * provided URI
     * @param conceptSchemeURI URI of a concept scheme
     * @return This SKOSConcept to allow cascading calls
     * @throws IllegalArgumentException if the provided URI is not that of a {@link SKOSConceptScheme}
     */
    SKOSConcept makeTopConcept(String conceptSchemeURI) throws IllegalArgumentException;
    
    /**
     * Makes this concept a top concept of the provided concept scheme
     * @param conceptScheme {@link SKOSConceptScheme} to make this concept top
     * concept of
     * @return This SKOSConcept to allow cascading calls
     */
    SKOSConcept makeTopConcept(SKOSConceptScheme conceptScheme);
    
    /**
     * Lists the concepts related to this concept through the provided
     * relationship type
     * @param relationshipType Type of the relationship expressed as a
     * {@link SKOSSemanticProperty}
     * @return Iterator over the SKOS concepts related to
     * this one through {@code relationshipType} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null}, then this returns an
     * <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     */
    CloseableIterator<SKOSConcept> listSemanticRelations(SKOSSemanticProperty relationshipType);
    
    /**
     * Lists the SKOS concepts that this concept is related to through
     * the provided relationship type
     * @param relationshipType Type of the relationship expressed as a
     * {@link SKOSSemanticProperty}
     * @return Iterator over the SKOS concepts that this concept
     * is related to through {@code relationshipType} if any;
     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null}, then this returns an
     * <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     */
    CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType);
    
}
