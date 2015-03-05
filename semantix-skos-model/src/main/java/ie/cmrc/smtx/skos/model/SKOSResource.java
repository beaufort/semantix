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

import ie.cmrc.smtx.base.SemanticEntity;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import ie.cmrc.util.Term;
import java.util.List;

/**
 * Defines a common interface for SKOS resources (concept schemes, collections,
 * and concepts). A SKOS resource may have <i><b>annotations</b></i> and
 * <i><b>relations</b></i>.
 * An <i><b>annotation</b></i> is a String value associated with a SKOS
 * <i><b>annotation property</b></i> ({@link SKOSAnnotationProperty}) and a
 * language code (possibly null). In this version, a <i><b>relation</b></i> is
 * SKOS resource that is associated with this resource through
 * a given <i><b>relationship type</b></i>. That is a <i><b>SKOS object property</b></i>
 * {@link SKOSObjectProperty}.<br/>
 * Please note that this interface is not specific about the reasoning capabilities
 * of the implementations or its logical consistency. Whether inferences or logical
 * consistency checks are to be supported depends on the implementation.
 * 
 * @author Yassine Lassoued
 */
public interface SKOSResource extends SemanticEntity {
    
//    /**
//     * Returns the local name of the resource
//     * @return Local name of the resource assuming that the URI of the resource
//     * is in one of the formats below, otherwise the full URI will be returned.
//     * <pre>
//     *  URL in the format {@code <namespace>#<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea"}
//     *  URL in the format {@code <namespace>#<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea/"}
//     *  URL in the format {@code <namespace>/<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea"}
//     *  URL in the format {@code <namespace>/<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea"}
//     *  URN in the format {@code urn:<namespaceIdentifier>:<localName>}, e.g., {@code "urn:ie:ucc:cmrc:places:IrishSea"}</pre>
//     */
//    String getLocalName();
//
//    /**
//     * Returns the namespace of the resource
//     * @return The namespace of the resource, returns null if the resource's URI
//     * is null, or not in one of the following formats
//     * <pre>
//     *  URL in the format {@code <namespace>#<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea"}
//     *  URL in the format {@code <namespace>#<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea/"}
//     *  URL in the format {@code <namespace>/<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea"}
//     *  URL in the format {@code <namespace>/<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea"}
//     *  URN in the format {@code urn:<namespaceIdentifier>:<localName>}, e.g., {@code "urn:ie:ucc:cmrc:places:IrishSea"}</pre>
//     */
//    String getNameSpace();
//
//    /**
//     * Returns the URI of the resource. While the URI may be any {@code String},
//     * it is commonly encoded in one of the following formats:
//     * <pre>
//     *  URL in the format {@code <namespace>#<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea"}
//     *  URL in the format {@code <namespace>#<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea/"}
//     *  URL in the format {@code <namespace>/<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea"}
//     *  URL in the format {@code <namespace>/<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea"}
//     *  URN in the format {@code urn:<namespaceIdentifier>:<localName>}, e.g., {@code "urn:ie:ucc:cmrc:places:IrishSea"}</pre>
//     * 
//     * @return URI of the resource
//     */
//    String getURI();
    
    /**
     * Returns the SKSO thesaurus {@link SKOS} this resource belongs to
     * @return {@link SKOS} defining this resource
     */
    SKOS getSKOS();
    
    /**
     * Type of this SKOS resource
     * @return {@link SKOSType} associated with his resource
     */
    SKOSType getSkosType();
    
    
    /**
     * Adds the provided {@code value} for the specified annotation property if
     * it does not exist already and is not null or empty. Note that as
     * no language is specified here, the value will not be associated to any language.
     * The annotation value will be trimmed and will only be added if it is not
     * null or empty. If the annotation's cardinality is not multiple
     * (i.e., {@code annotation.isMultiple()==false}, e.g., {@code SKOSAnnotation.PREF_LABEL}),
     * then the added value will replace the current value if it is different.
     * 
     * @param annotationProperty SKOS annotation property to associate the value with
     * @param value Value to add for the provided annotation
     * @return {@code true} if the value was added, {@code false} otherwise.
     * Please note that if {@code annotationProperty == null}, then the value will
     * not be added and the method returns {@code false}.
     */
    //boolean addAnnotation(SKOSAnnotationProperty annotationProperty, String value);
    
    /**
     * Adds the provided {@code value} for the specified annotation property
     * and language if it does not already exist and is not null or empty.
     * The annotation value will be trimmed and will only be added if it is not
     * null or empty. If the annotation property's cardinality is not multiple
     * (i.e., {@code annotationProperty.isMultiple() == false}, e.g.,
     * {@code SKOSAnnotation.PREF_LABEL}), then the added value will replace
     * the current value.<br/>
     * Use {@code null} for the {@code language} parameter if the
     * annotation value is not to be associated with any language.
     * Please note that if {@code annotationProperty == null}, then the value will
     * not be added.
     * 
     * @param annotationProperty SKOS annotation
     * @param value Value to add for the provided annotation property
     * @param language Language of the annotation value
     * @return This resource to allow cascading calls
     */
    SKOSResource addAnnotation(SKOSAnnotationProperty annotationProperty, String value, String language);
    
    /**
     * Get the first annotation value for the provided {@link SKOSAnnotationProperty}
     * regardless of the language
     * @param annotationProperty SKOS annotation property the value of which to be returned
     * @return First value found for {@code annotationProperty} regardless of the language.
     * If the resource does not have any value for {@code annotationProperty}, then {@code null}
     * is returned.
     * This return {@code null} if {@code annotationProperty == null}.
     */
    //String getAnnotation(SKOSAnnotationProperty annotationProperty);
    
    /**
     * Get the first annotation value for the provided {@link SKOSAnnotationProperty}
     * and language
     * @param annotationProperty SKOS annotation property the value of which to be returned
     * @param language Language of the annotation value
     * @return Value of {@code annotationProperty} for the specified {@code language}.
     * If {@code language==null}, then this returns the first value that has no language.
     * Please note that if the resource has no value for the provided annotation and language,
     * then null is returned.
     */
    String getAnnotation(SKOSAnnotationProperty annotationProperty, String language);
    
    /**
     * Returns all the values of the provided SKOS annotation property across all the languages.
     * @param annotationProperty SKOS annotation property the values of which to be returned
     * @return {@code List} of the values associated with {@code annotationProperty},
     * values are returned as {@link ie.cmrc.util.Term} instances.
     * If no values exist for this annotation or {@code annotationProperty == null},
     * then an empty list is returned.
     */
    List<Term> getAnnotations(SKOSAnnotationProperty annotationProperty);
    
    /**
     * Returns all the values of the provided SKOS annotation in the provided language.
     * @param annotationProperty SKOS annotation property the values of which to be returned
     * @param language Language of the annotation value. Use {@code null} to extract
     * annotations that do not have language tags.
     * @return {@code List<String>} of the values associated with {@code annotationProperty}
     * in the specified language. If no values exist for the provided annotation and language
     * in this resource or {@code annotationProperty == null}, then an empty list is returned.
     */
    List<String> getAnnotations(SKOSAnnotationProperty annotationProperty, String language);
    
    /**
     * Indicates whether this resource has at least one value for the provided
     * annotation property regardless of the language
     * @param annotationProperty SKOS annotation to check
     * @return {@code true} if the specified annotation has at least one value
     * (non-null and non-empty) for this resource, {@code false} otherwise.
     * Please note that if {@code annotationProperty == null}, then {@code false} is returned
     */
    boolean hasAnnotation(SKOSAnnotationProperty annotationProperty);
    
    /**
     * 
     * Indicates whether this resource has at least one value for the provided
     * annotation property in the provided language
     * @param annotationProperty SKOS annotation to check
     * @param language Language of the annotation value. Use {@code null} to check
     * annotations that do not have language tags.
     * @return {@code true} if the specified annotation has at least one
     * (non-null and non-empty) value in the specified language, {@code false} otherwise.
     * Please note that if {@code annotationProperty == null}, then {@code false} is returned
     */
    boolean hasAnnotation(SKOSAnnotationProperty annotationProperty, String language);
    
    /**
     * 
     * Indicates whether this resource has the provided value value as an
     * annotation for the provided annotation property in the provided language
     * @param annotationProperty SKOS annotation to check
     * @param value Annotation value to check
     * @param language Language of the annotation value. Use {@code null} to check
     * annotations that do not have language tags.
     * @return {@code true} if the specified annotation has at least one
     * (non-null and non-empty) value in the specified language, {@code false} otherwise.
     * Please note that if {@code annotationProperty == null}, then {@code false} is returned
     */
    boolean hasAnnotation(SKOSAnnotationProperty annotationProperty, String value, String language);
    
    /**
     * Clears all the values in all the languages for the provided annotation property
     * @param annotationProperty SKOS annotation whose values are to be deleted.
     * If {@code annotationProperty == null}, then no changes will be made
     * @return This resource to allow cascading calls
     */
    SKOSResource removeAnnotations(SKOSAnnotationProperty annotationProperty);
    
    /**
     * Clears all the values for the provided annotation property in the provided language
     * @param annotationProperty SKOS annotation whose values are to be deleted.
     * If {@code annotationProperty == null}, then no changes will be made.
     * @param language Specific language whose associated values are to be deleted.
     * @return This resource to allow cascading calls
     */
    SKOSResource removeAnnotations(SKOSAnnotationProperty annotationProperty, String language);
    
    /**
     * Removes the specified value for the specified annotation property that is
     * not associated with any language
     * @param annotationProperty SKOS annotation property the value of which is to be deleted
     * @param annotationValue Value to delete
     * @return {@code true} if the resource changed (i.e., the annotation value
     * was actually removed), {@code false} otherwise.
     * If {@code annotationProperty == null}, then no changes will be made and
     * {@code false} is returned.
     */
    //boolean removeAnnotation(SKOSAnnotationProperty annotationProperty, String annotationValue);
    
    /**
     * Removes the specified value for the specified annotation property and language
     * @param annotationProperty SKOS annotation property the value of which is to be deleted.
     * If {@code annotationProperty == null}, then no changes will be made.
     * @param annotationValue Value to delete
     * @param language Language of the annotation value
     * @return This resource to allow cascading calls
     */
    SKOSResource removeAnnotation(SKOSAnnotationProperty annotationProperty, String annotationValue, String language);
    
    /**
     * Returns the list of annotation properties that the resource has values for
     * @return List of annotation properties that have at least one value in any
     * language. If the resource has no annotations, then an <i>empty</i> {@code List}
     * is returned.
     */
    List<SKOSAnnotationProperty> getAnnotationProperties();
    
    /**
     * Returns the list of languages used by all the annotation values in this resource
     * @return {@code List<String>} containing all the languages used to annotate
     * the resource. If the resource has no annotations, then an <i>empty</i> {@code List}
     * is returned rather than an null value. 
     */
    List<String> getAnnotationLanguages();
    
    /**
     * Returns the list of languages of the annotation values for the provided {@code annotationProperty}
     * @param annotationProperty {@link SKOSAnnotationProperty} the languages of which to return
     * @return {@code List<String>} containing all the languages used to annotate
     * the resource using the {@link SKOSAnnotationProperty} {@code annotationProperty}.
     * If the resource has no values for {@code annotationProperty}, or {@code annotationProperty==null},
     * then an <i>empty</i> {@code List}
     * is returned rather than an null value.
     */
    List<String> getAnnotationLanguages(SKOSAnnotationProperty annotationProperty);
    
    /**
     * Adds a relationship between this resource and that specified by its URI
     * ({@code resourceURI}) if no such relationship exists already.
     * If {@code relationshipType == null}, or
     * {@code resourceURI} is {@code null} or empty, or the relationship exists already,
     * or no resource exists with the specified URI, then the relationship is not added.
     * @param relationshipType Type of the relationship between the resources
     * expressed as a {@link SKOSObjectProperty}
     * @param resourceURI URI of the related resource
     * @return This resource to allow cascading calls
     */
    SKOSResource addRelation(SKOSObjectProperty relationshipType, String resourceURI);
    
    /**
     * Adds a relationship between this resource and ({@code otherResource}) if
     * no such relationship exists already. If {@code relationshipType == null}, or
     * {@code resourceURI} is {@code null} or empty, or the relationship exists already,
     * then the relationship is not added.
     * @param relationshipType Type of the relationship between the resources
     * expressed as a {@link SKOSObjectProperty}
     * @param otherResource URI of the related resource
     * @return This resource to allow cascading calls
     */
    SKOSResource addRelation(SKOSObjectProperty relationshipType, SKOSResource otherResource);
    
    /**
     * Returns all the SKOS resources related to this one through all the SKOS
     * object properties
     * @return {@code ie.cmrc.util.Multimap<SKOSObjectProperty, SKOSResource>}
     * containing all the SKOS resources related to this one if any;
     * otherwise an <i>empty</i> {@code Multimap}.
     */
//    Multimap<SKOSObjectProperty,SKOSResource> getRelations();
    
    /**
     * Lists the SKOS resources related to this one through the provided
     * relationship type
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @return Iterator over the SKOS resources related to this one through
     * {@code relationshipType} if any; otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     */
    CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType);
    
//    /**
//     * Lists the SKOS resources related to this one through the provided
//     * relationship type and belonging to the provided concept scheme
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptScheme Target concept scheme
//     * @return Iterator over the SKOS resources related to this one through
//     * {@code relationshipType} and belonging to {@code conceptScheme} if any;
//     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code conceptScheme==null} then this is equivalent to
//     * {@linkplain #listRelations(ie.cmrc.skos.core.SKOSObjectProperty)}.
//     */
//    CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme);
//    
//    /**
//     * Lists the SKOS resources related to this one through the provided
//     * relationship type and belonging to the concept scheme identified by the
//     * provided URI
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURI URI of the target concept scheme
//     * @return Iterator over the SKOS resources related to this one through
//     * {@code relationshipType} and belonging to {@code conceptSchemeURI} if any;
//     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code conceptSchemeURI==null} then this is equivalent to
//     * {@linkplain #listRelations(ie.cmrc.skos.core.SKOSObjectProperty)}.
//     */
//    CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType, String conceptSchemeURI);
//    
//    /**
//     * Lists the SKOS resources related to this one through the provided
//     * relationship type and belonging to the provided concept scheme and collection
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptScheme Target concept scheme
//     * @param collection Target collection
//     * @return Iterator over the SKOS resources related to this one through
//     * {@code relationshipType} and belonging to {@code conceptScheme} and
//     * {@code collection} if any;
//     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code conceptScheme==null} then results are not filtered by concept scheme.
//     * If {@code collection==null} then results are not filtered by collection.
//     */
//    CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme, SKOSCollection collection);
//    
//    /**
//     * Lists the SKOS resources related to this one through the provided
//     * relationship type and belonging to the concept scheme and collection
//     * identified by the provided URIs
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURI URI of the target concept scheme
//     * @param collectionURI URI of the target collection
//     * @return Iterator over the SKOS resources related to this one through
//     * {@code relationshipType} and belonging to {@code conceptSchemeURI} and
//     * {@code collectionURI} if any;
//     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code conceptSchemeURI==null} then results are not filtered by concept scheme.
//     * If {@code collectionURI==null} then results are not filtered by collection.
//     */
//    CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType, String conceptSchemeURI, String collectionURI);
//    
//    /**
//     * Lists the SKOS resources related to this one through the provided
//     * relationship type and belonging to at least one of the concept schemes
//     * and at least one of the collections identified by the provided URIs
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURIs URIs of the target concept schemes
//     * @param collectionURIs URIs of the target collections
//     * @return Iterator over the SKOS resources related to this one through
//     * {@code relationshipType} and belonging to at least one of the concept schemes
//     * and at least one of the collections identified by the provided URIs if any;
//     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code conceptSchemeURIs} is {@code null} or empty then results are not
//     * filtered by concept scheme.
//     * If {@code collectionURIs} is {@code null} or empty then results are not filtered by collection.
//     */
//    CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType, Collection<String> conceptSchemeURIs, Collection<String> collectionURIs);
    
//    /**
//     * Returns the list of resources related to this one through the
//     * provided relationship type
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @return List of SKOS resources related to this one through
//     * {@code relationshipType} if any; otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     */
//    List<SKOSResource> getRelations(SKOSObjectProperty relationshipType);
//    
//    /**
//     * Returns the list of resources related to this one through the
//     * provided relationship type and which belong to the provided concept scheme
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptScheme Target concept scheme
//     * @return List of SKOS resources related to this one through
//     * {@code relationshipType} and belonging to {@code conceptScheme} if any;
//     * otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     * If {@code conceptScheme==null} then this is equivalent to
//     * {@linkplain #getRelations(ie.cmrc.skos.core.SKOSObjectProperty)}.
//     */
//    List<SKOSResource> getRelations(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme);
//    
//    /**
//     * Returns the list of resources related to this one through the
//     * provided relationship type and which belong to the concept scheme
//     * identified by the provided URI
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURI URI of the target concept scheme
//     * @return List of SKOS resources related to this one through
//     * {@code relationshipType} and belonging to {@code conceptSchemeURI} if any;
//     * otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     * If {@code conceptSchemeURI==null} then this is equivalent to
//     * {@linkplain #getRelations(ie.cmrc.skos.core.SKOSObjectProperty)}.
//     */
//    List<SKOSResource> getRelations(SKOSObjectProperty relationshipType, String conceptSchemeURI);
//    
//    /**
//     * Returns the list of resources related to this one through the
//     * provided relationship type and which belong to the provided concept scheme
//     * and collection
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptScheme Target concept scheme
//     * @param collection Target collection
//     * @return List of SKOS resources related to this one through
//     * {@code relationshipType} and belonging to {@code conceptScheme}
//     * and {@code collection} if any;
//     * otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     * If {@code conceptScheme==null} then the results are not filtered by
//     * concept scheme.
//     * If {@code collection==null} then the results are not filtered by
//     * collection.
//     */
//    List<SKOSResource> getRelations(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme, SKOSCollection collection);
//    
//    /**
//     * Returns the list of resources related to this one through the
//     * provided relationship type and which belong to the concept scheme
//     * identified by the provided URI
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURI URI of the target concept scheme
//     * @param collectionURI URI of the target collection
//     * @return List of SKOS resources related to this one through
//     * {@code relationshipType} and belonging to {@code conceptSchemeURI}
//     * and {@code collectionURI} if any;
//     * otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     * If {@code conceptSchemeURI==null} then the results are not filtered by
//     * concept scheme.
//     * If {@code collectionURI==null} then the results are not filtered by
//     * collection.
//     */
//    List<SKOSResource> getRelations(SKOSObjectProperty relationshipType, String conceptSchemeURI, String collectionURI);
//    
//    /**
//     * Returns the list of resources related to this one through the
//     * provided relationship type and which belong at least one the concept
//     * schemes and one of the collections identified by the provided URIs
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURIs URIs of the target concept scheme
//     * @param collectionURIs URIs of the target collection
//     * @return List of SKOS resources related to this one through
//     * {@code relationshipType} and belonging to at least one the concept
//     * schemes and one of the collections identified by the provided URIs if any;
//     * otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     * If {@code conceptSchemeURIs} is {@code null} or empty then the results are not filtered by
//     * concept scheme.
//     * If {@code collectionURIs} is {@code null} or empty then the results are not filtered by
//     * collection.
//     */
//    List<SKOSResource> getRelations(SKOSObjectProperty relationshipType, Collection<String> conceptSchemeURIs, Collection<String> collectionURIs);
    
    /**
     * Indicates whether this resource has resources related to it through the
     * provided relationship type
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @return {@code true} if this resource has at least one resource related to
     * it through {@code relationshipType}; {@code false} otherwise or if {@code relationshipType==null}.
     */
    boolean hasRelation(SKOSObjectProperty relationshipType);
    
    /**
     * Indicates whether the resource identified by the specified URI is related
     * to this resource through the provided relationship type
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @param resourceURI URI of the resource to check
     * @return {@code true} if the resource identified by {@code resourceURI} is related
     * to this resource through {@code relationshipType}; {@code false} otherwise
     * or if {@code relationshipType==null}.
     */
    boolean hasRelation(SKOSObjectProperty relationshipType, String resourceURI);
    
    /**
     * Indicates whether the provided SKOS resource is related
     * to this resource through the provided relationship type
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @param skosResource SKOS resource
     * @return {@code true} if the provide resource is related
     * to this resource through {@code relationshipType}; {@code false} otherwise
     * or if {@code relationshipType==null}.
     */
    boolean hasRelation(SKOSObjectProperty relationshipType, SKOSResource skosResource);
    
    /**
     * Removes all the relationships with the resource
     * @return This resource to allow cascading calls
     */
//    SKOSResource removeRelations();
    
    /**
     * Removes all the resource relationships for the specified relationship type
     * @param relationshipType Type of the relationship to delete expressed as a {@link SKOSObjectProperty}
     * @return This resource to allow cascading calls
     */
    SKOSResource removeRelations(SKOSObjectProperty relationshipType);
    
    /**
     * Deletes the link between this resource and that specified by the
     * provided URI and through the provided relationship type
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @param resourceURI URI of the target resource
     * @return This resource to allow cascading calls
     */
    SKOSResource removeRelation(SKOSObjectProperty relationshipType, String resourceURI);
    
    /**
     * Deletes the link between this resource and the provided one through the
     * provided relationship type
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @param skosResource A SKOS resource
     * @return This resource to allow cascading calls
     */
    SKOSResource removeRelation(SKOSObjectProperty relationshipType, SKOSResource skosResource);
    
    /**
     * Returns the list of relationship types that link this resource to
     * other resources
     * @return List of relationship types that link this resource to
     * other resources, expressed as SKOS object properties {@link SKOSObjectProperty}.
     * If no resources are related to this one, then an <i>empty</i> {@code List}
     * is returned.
     */
    //List<SKOSObjectProperty> getRelationships();
    
    
    /**
     * Adds this resource as a relation to the resource specified by its URI
     * ({@code resourceURI}) if no such relationship exists already.
     * If {@code relationshipType == null}, or
     * {@code resourceURI} is {@code null} or empty, or the relationship exists already,
     * or no resource exists with the specified URI, then the relationship is not added.
     * @param resourceURI URI of the SKOS resource that this will be related to
     * @param relationshipType Type of the relationship between the resources
     * expressed as a {@link SKOSObjectProperty}
     * @return This resource to allow cascading calls
     */
    SKOSResource makeRelation(String resourceURI, SKOSObjectProperty relationshipType);
    
    /**
     * Adds this relationship resource as a relation to ({@code otherResource}) if
     * no such relationship exists already. If {@code relationshipType == null}, or
     * {@code resourceURI} is {@code null} or empty, or the relationship exists already,
     * then the relationship is not added.
     * @param otherResource URI of the related resource
     * @param relationshipType Type of the relationship between the resources
     * expressed as a {@link SKOSObjectProperty}
     * @return This resource to allow cascading calls
     */
    SKOSResource makeRelation(SKOSResource otherResource, SKOSObjectProperty relationshipType);
    
    /**
     * Lists the resources that this one is related to through the
     * provided relationship type
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @return Iterator over the SKOS resources that this is related to through
     * {@code relationshipType} if any; otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
     */
    CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType);
    
//    /**
//     * Lists the resources that this one is related to through the
//     * provided relationship type and that belong to the provided concept scheme
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptScheme Target concept scheme
//     * @return Iterator over the SKOS resources that this is related to through
//     * {@code relationshipType} and that belong to {@code conceptScheme}
//     * if any; otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code conceptScheme==null}, then this is equivalent to
//     * {@linkplain #listRelationshipSources(ie.cmrc.skos.core.SKOSObjectProperty)}.
//     */
//    CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme);
//    
//    /**
//     * Lists the resources that this one is related to through the
//     * provided relationship type and that belong to the concept scheme
//     * identified by the provided URI
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURI URI of the target concept scheme
//     * @return Iterator over the SKOS resources that this is related to through
//     * {@code relationshipType} and that belong to {@code conceptSchemeURI} if any;
//     * otherwise an <i>empty</i> {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code conceptSchemeURI==null}, then this is equivalent to
//     * {@linkplain #listRelationshipSources(ie.cmrc.skos.core.SKOSObjectProperty)}.
//     */
//    CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType, String conceptSchemeURI);
//    
//    /**
//     * Lists the resources that this one is related to through the
//     * provided relationship type and that belong to the provided concept scheme
//     * and collection
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptScheme Target concept scheme
//     * @param collection Target collection
//     * @return Iterator over the SKOS resources that this is related to through
//     * {@code relationshipType} and that belong to {@code conceptScheme} and
//     * {@code collection} if any; otherwise an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code conceptScheme==null}, then results are not filtered by concept
//     * scheme.
//     * If {@code collection==null}, then results are not filtered by collection.
//     */
//    CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme, SKOSCollection collection);
//    
//    /**
//     * Lists the resources that this one is related to through the
//     * provided relationship type and that belong to the concept scheme
//     * and collection identified by the provided URIs
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURI URI of the target concept scheme
//     * @param collectionURI URI of the target collection
//     * @return Iterator over the SKOS resources that this is related to through
//     * {@code relationshipType} and that belong to {@code conceptSchemeURI} and
//     * {@code collectionURI} if any; otherwise an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code conceptSchemeURI==null}, then results are not filtered by concept
//     * scheme.
//     * If {@code collectionURI==null}, then results are not filtered by collection.
//     */
//    CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType, String conceptSchemeURI, String collectionURI);
//    
//    /**
//     * Lists the resources that this one is related to through the
//     * provided relationship type and that belong to at least one of the concept
//     * schemes and at least one of the collections identified by the provided URIs
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURIs URIs of the target concept schemes
//     * @param collectionURIs URIs of the target collections
//     * @return Iterator over the SKOS resources that this is related to through
//     * {@code relationshipType} and that belong to at least one of
//     * {@code conceptSchemeURIs} and on of
//     * {@code collectionURIs} if any; otherwise an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i>
//     * {@link ie.cmrc.skos.core.util.CloseableIterator}.
//     * If {@code conceptSchemeURIs} is {@code null} or empty, then results are
//     * not filtered by concept scheme.
//     * If {@code collectionURIs} is {@code null} or empty, then results are
//     * not filtered by collection.
//     */
//    CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType, Collection<String> conceptSchemeURIs, Collection<String> collectionURIs);
    
//    /**
//     * Returns the list of resources that this one is related to through the
//     * provided relationship type
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @return List of SKOS resources that this is related to through
//     * {@code relationshipType} if any; otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     */
//    List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType);
//    
//    /**
//     * Returns the list of resources that this one is related to through the
//     * provided relationship type and that belong to the provided concept
//     * scheme
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptScheme Target concept scheme
//     * @return List of SKOS resources that this is related to through
//     * {@code relationshipType} and that belong to {@code conceptScheme} if any;
//     * otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     * If {@code conceptScheme==null}, then this is equivalent to
//     * {@linkplain #listRelationshipSources(ie.cmrc.skos.core.SKOSObjectProperty)}.
//     */
//    List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme);
//    
//    /**
//     * Returns the list of resources that this one is related to through the
//     * provided relationship type and that belong to the concept
//     * scheme identified by the provided URI
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURI URI of the target concept scheme
//     * @return List of SKOS resources that this is related to through
//     * {@code relationshipType} and that belong to {@code conceptSchemeURI} if any;
//     * otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     * If {@code conceptSchemeURI==null}, then this is equivalent to
//     * {@linkplain #listRelationshipSources(ie.cmrc.skos.core.SKOSObjectProperty)}.
//     */
//    List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType, String conceptSchemeURI);
//    
//    /**
//     * Returns the list of resources that this one is related to through the
//     * provided relationship type and that belong to the provided concept
//     * scheme and collection
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptScheme Target concept scheme
//     * @param collection Target collection
//     * @return List of SKOS resources that this is related to through
//     * {@code relationshipType} and that belong to {@code conceptScheme}
//     * and {@code collection} if any;
//     * otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     * If {@code conceptScheme==null}, then results are not filtered by concept
//     * scheme.
//     * If {@code collection==null}, then results are not filtered by collection.
//     */
//    List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme, SKOSCollection collection);
//    
//    /**
//     * Returns the list of resources that this one is related to through the
//     * provided relationship type and that belong to the concept
//     * scheme and collection identified by the provided URIs
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURI URI of the target concept scheme
//     * @param collectionURI URI of the target collection
//     * @return List of SKOS resources that this is related to through
//     * {@code relationshipType} and that belong to {@code conceptSchemeURI}
//     * and {@code collectionURI} if any;
//     * otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     * If {@code conceptSchemeURI==null}, then results are not filtered by concept
//     * scheme.
//     * If {@code collectionURI==null}, then results are not filtered by collection.
//     */
//    List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType, String conceptSchemeURI, String collectionURI);
//    
//    /**
//     * Returns the list of resources that this one is related to through the
//     * provided relationship type and that belong to at least one of
//     * the concept schemes and one of the collections identified by the provided
//     * URIs
//     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
//     * @param conceptSchemeURIs URIs of the target concept schemes
//     * @param collectionURIs URIs of the target collections
//     * @return List of SKOS resources that this is related to through
//     * {@code relationshipType} and that belong to at least one of
//     * {@code conceptSchemeURIs} and at least one of {@code collectionURIs}
//     * if any; otherwise an <i>empty</i> {@code List}.
//     * If {@code relationshipType==null}, then this returns an <i>empty</i> {@code List}.
//     * If {@code conceptSchemeURIs} is {@code null} or empty, then results are not filtered by concept
//     * scheme.
//     * If {@code collectionURIs} is {@code null} or empty, then results are not filtered by collection.
//     */
//    List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType, Collection<String> conceptSchemeURIs, Collection<String> collectionURIs);
    
    /**
     * Indicates whether this resource is a relation of some SKOS resources through the
     * provided relationship type
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @return {@code true} if this resource is a relation of at least one SKOS resource
     * through {@code relationshipType}; {@code false} otherwise or if {@code relationshipType==null}.
     */
    boolean isRelation(SKOSObjectProperty relationshipType);
    
    /**
     * Indicates whether this resource is a relation of the resource identified
     * by the specified URI through the provided relationship type
     * @param resourceURI URI of the resource to check
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @return {@code true} if this resource is a relation of the resource
     * identified by {@code resourceURI} through {@code relationshipType};
     * {@code false} otherwise or if {@code relationshipType==null}.
     */
    boolean isRelation(String resourceURI, SKOSObjectProperty relationshipType);
    
    /**
     * Indicates whether this resource is a relation of the specified resource
     * through the provided relationship type
     * @param otherResource SKOS resource
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @return {@code true} if this resource is a relation of {@code otherResource}
     * through {@code relationshipType}; {@code false} otherwise or if {@code relationshipType==null}.
     */
    boolean isRelation(SKOSResource otherResource, SKOSObjectProperty relationshipType);
    
    /**
     * Removes this resource as a relation to all available SKOS resources
     * @return This resource to allow cascading calls
     */
//    SKOSResource removeAsRelation();
    
    /**
     * Removes this resource as a relation to all available SKOS resources
     * for the specified relationship type
     * @param relationshipType Type of the relationship to delete expressed as a {@link SKOSObjectProperty}
     * @return This resource to allow cascading calls
     */
    SKOSResource removeAsRelation(SKOSObjectProperty relationshipType);
    
    /**
     * Removes this resource as a relation to the SKOS resource identified by
     * the provided URI for the specified relationship type
     * @param resourceURI URI of the target resource
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @return This resource to allow cascading calls
     */
    SKOSResource removeAsRelation(String resourceURI, SKOSObjectProperty relationshipType);
    
    /**
     * Removes this resource as a relation to the provided SKOS resource for
     * the specified relationship type
     * @param skosResource SKOS resource that is the subject of the
     * relationship to remove
     * @param relationshipType Type of the relationship expressed as a {@link SKOSObjectProperty}
     * @return This resource to allow cascading calls
     */
    SKOSResource removeAsRelation(SKOSResource skosResource, SKOSObjectProperty relationshipType);
    
    
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Managing and accessing parent concept schemes
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * Adds this {@link SKOSResource} to the {@link SKOSConceptScheme} identified
     * by the provided URI
     * @param conceptSchemeURI URI of a {@link SKOSConceptScheme} to add this resource to
     * @return This {@link SKOSResource} to allow cascading calls
     * @throws IllegalArgumentException If the provided URI is not that of
     * a {@link SKOSConceptScheme}
     */
    SKOSResource addToConceptScheme(String conceptSchemeURI) throws IllegalArgumentException;

    /**
     * Adds this {@link SKOSResource} to the provided {@link SKOSConceptScheme}
     * @param conceptScheme {@link SKOSConceptScheme} to add this resource to
     * @return This {@link SKOSResource} to allow cascading calls
     */
    SKOSResource addToConceptScheme(SKOSConceptScheme conceptScheme);

    /**
     * Concept schemes that this {@link SKOSResource} belongs to
     * @return {@code List<SKOSConceptScheme>} containing all the concept schemes
     * that this {@link SKOSResource} belongs to
     */
    List<SKOSConceptScheme> getConceptSchemes();

    /**
     * Checks whether this {@link SKOSResource} belongs to the
     * {@link SKOSConceptScheme} identified by the provided URI
     * @param conceptSchemeURI URI of a {@link SKOSConceptScheme}
     * @return {@code true} if this {@link SKOSResource} belongs to the
     * {@link SKOSConceptScheme} whose URI is {@code conceptSchemeURI},
     * {@code false} otherwise. If no concept scheme exists with the provided URI
     * then {@code false} is returned.
     */
    boolean isInScheme(String conceptSchemeURI);

    /**
     * Checks whether this {@link SKOSResource} belongs to the specified
     * {@link SKOSConceptScheme}
     * @param conceptScheme Concept scheme to check whether this
     * {@link SKOSResource} belongs to
     * @return {@code true} if this {@link SKOSResource} belongs to {@code conceptScheme},
     * {@code false} otherwise, or if {@code conceptScheme==null}
     */
    boolean isInScheme(SKOSConceptScheme conceptScheme);

    /**
     * Removes this {@link SKOSResource}, as a member, from all concept schemes
     * @return This {@link SKOSResource} to allow cascading calls
     */
    SKOSResource removeFromAllConceptSchemes();

    /**
     * Removes this {@link SKOSResource}, as a member, from the concept scheme
     * identified by the provided URI
     * @param conceptSchemeURI URI of a {@link SKOSConceptScheme}
     * @return This {@link SKOSResource} to allow cascading calls
     */
    SKOSResource removeFromConceptScheme(String conceptSchemeURI);

    /**
     * Removes this {@link SKOSResource}, as a member, from the provided concept scheme
     * @param conceptScheme {@link SKOSConceptScheme} to remove this {@link SKOSResource} from
     * @return This {@link SKOSResource} to allow cascading calls
     */
    SKOSResource removeFromConceptScheme(SKOSConceptScheme conceptScheme);
    
    
    
//    /**
//     * Sets the language to be considered while comparing this resource to other resources.
//     * The problem we are trying to solve here is as follows. Sorting resources
//     * should ideally be based on their labels (preferred,
//     * alternative and hidden). As resources are multilingual and may be queried
//     * in different languages, they need to be sorted depending on the requestor's
//     * language of interest. To allow for this flexibility, we introduced this method,
//     * which will enable a requestor to set their language of interest before sorting
//     * or comparing resources.
//     * 
//     * @param lang Language to set for comparing the resource with other resources
//     */
//    void setComparisonLanguage(String lang);
//    
//    /**
//     * Language to use as the basis for comparing this resource to other resources.
//     * Please check {@linkplain #setComparisonLanguage(java.lang.String)} for more
//     * details on the utility of the comparison language.
//     * @return Comparison language for this resource. If the comparison language has
//     * not been set previously, then this returns {@code null}.
//     */
//    String getComparisonLanguage();

    /**
     * Checks whether this resource is a {@link SKOSConceptScheme}
     * @return {@code true} if this resources is a {@link SKOSConceptScheme} instance,
     * {@code false} otherwise
     * 
     * @see #asSKOSConceptScheme() 
     */
    Boolean isSKOSConceptScheme();
    
    /**
     * Checks whether this resource is a {@link SKOSCollection}
     * @return {@code true} if this resources is a {@link SKOSCollection} instance,
     * {@code false} otherwise
     * 
     * @see #asSKOSCollection() 
     */
    Boolean isSKOSCollection();
    
    /**
     * Checks whether this resource is a {@link SKOSConcept}
     * @return {@code true} if this resources is a {@link SKOSConcept} instance,
     * {@code false} otherwise
     * 
     * @see #asSKOSConcept()
     */
    Boolean isSKOSConcept();

    /**
     * Casts the resource to a {@link SKOSConceptScheme}.
     * <p>Usage:
     * <code><pre> if (myResource.isSKOSConceptScheme()) {
     *   SKOSConceptScheme myScheme = myResource.asSKOSConceptScheme();
     *   // Do something with myScheme...
     * }</pre></code>
     * 
     * @return {@link SKOSConceptScheme} casting of this {@link SKOSResource}
     * 
     * @see #isSKOSConceptScheme()
     */
    SKOSConceptScheme asSKOSConceptScheme();
    
    /**
     * Casts the resource to a {@link SKOSCollection}.
     * <p>Usage:
     * <code><pre> if (myResource.isSKOSCollection()) {
     *   SKOSCollection myCollection = myResource.asSKOSCollection();
     *   // Do something with myCollection...
     * }</pre></code>
     * 
     * @return {@link SKOSCollection} casting of this {@link SKOSResource}
     * 
     * @see #isSKOSCollection()
     */
    SKOSCollection asSKOSCollection();
    
    /**
     * Casts the resource to a {@link SKOSConcept}.
     * <p>Usage:
     * <code><pre>if (myResource.isSKOSConcept()) {
     *   SKOSConcept myConcept = myResource.asSKOSConcept();
     *   // Do something with myConcept...
     * }</pre></code>
     * 
     * @return {@link SKOSConcept} casting of this {@link SKOSResource}
     * 
     * @see #isSKOSConcept()
     */
    SKOSConcept asSKOSConcept();
}
