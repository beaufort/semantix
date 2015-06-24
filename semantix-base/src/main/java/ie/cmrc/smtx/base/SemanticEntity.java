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

package ie.cmrc.smtx.base;

import ie.cmrc.smtx.base.serialisation.json.JSONisable;
import ie.cmrc.smtx.base.serialisation.rdfxml.RDFXMLisable;
import java.util.List;

/**
 * A generic interface for semantic entities. A semantic entity has a unique
 * resource identifier (URI), preferred labels (one per language) and
 * alternative labels (zero or many per language).
 * 
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public interface SemanticEntity extends Comparable<SemanticEntity>, RDFXMLisable, JSONisable {

    /**
     * Returns the URI of the resource. While the URI may be any {@code String},
     * it is commonly encoded in one of the following formats:
     * <pre>
     *  URL in the format {@code <namespace>#<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea"}
     *  URL in the format {@code <namespace>#<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea/"}
     *  URL in the format {@code <namespace>/<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea"}
     *  URL in the format {@code <namespace>/<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea/"}
     *  URN in the format {@code urn:<namespaceIdentifier>:<localName>}, e.g., {@code "urn:ie:ucc:cmrc:places:IrishSea"}</pre>
     * 
     * @return URI of the resource
     */
    String getURI();
    
    /**
     * Returns the local name of the resource
     * @return Local name of the resource assuming that the URI of the resource
     * is in one of the formats below, otherwise the full URI will be returned.
     * <pre>
     *  URL in the format {@code <namespace>#<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea"}
     *  URL in the format {@code <namespace>#<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea/"}
     *  URL in the format {@code <namespace>/<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea"}
     *  URL in the format {@code <namespace>/<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea/"}
     *  URN in the format {@code urn:<namespaceIdentifier>:<localName>}, e.g., {@code "urn:ie:ucc:cmrc:places:IrishSea"}</pre>
     */
    String getLocalName();

    /**
     * Returns the namespace of the resource
     * @return The namespace of the resource, returns null if the resource's URI
     * is null, or not in one of the following formats
     * <pre>
     *  URL in the format {@code <namespace>#<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea"}
     *  URL in the format {@code <namespace>#<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/201403/places.owl#IrishSea/"}
     *  URL in the format {@code <namespace>/<localName>}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea"}
     *  URL in the format {@code <namespace>/<localName>/}, e.g., {@code "http://cmrc.ucc.ie/ont/concept/current/IrishSea/"}
     *  URN in the format {@code urn:<namespaceIdentifier>:<localName>}, e.g., {@code "urn:ie:ucc:cmrc:places:IrishSea"}</pre>
     */
    String getNameSpace();
    
    /**
     * Preferred label of the semantic entity in the specified language
     * @param language Language code
     * @return Preferred label of the semantic entity in the specified language
     * if any, otherwise {@code null}.
     * 
     */
    String getPrefLabel(String language);
    
    /**
     * List the alternative labels of the semantic entity in the specified
     * language
     * @param language Language code
     * @return List of alternative labels of this semantic entity in the
     * specified language. If the semantic entity has no alternative labels
     * in the specified language then an <b><i>empty list</i></b> is returned.
     */
    List<String> getAltLabels(String language);
    
    /**
     * Sets the entity's preferred label in the specified language.
     * This method is optional. If not implemented (e.g., read only
     * semantic entity implementation, see {@linkplain #isReadOnly()})
     * this should throw an {@code UnsupportedOperationException}.
     * @param prefLabel String to set as the preferred label.
     * @param language Language of the preferred label
     * @return This semantic entity to enable cascading calls
     */
    SemanticEntity setPrefLabel(String prefLabel, String language);
    
    /**
     * Adds an alternative label in the specified language.
     * This method is optional. If not implemented (e.g., read only
     * semantic entity implementation, see {@linkplain #isReadOnly()})
     * this should throw an {@code UnsupportedOperationException}.
     * @param altLabel Label to add as an alternative label
     * @param language Language of the label
     * @return This semantic entity to enable cascading calls
     */
    SemanticEntity addAltLabel(String altLabel, String language);
    
    /**
     * Removes the preferred label of this semantic entity in the specified
     * language if any.
     * This method is optional. If not implemented (e.g., read only
     * semantic entity implementation, see {@linkplain #isReadOnly()})
     * this should throw an {@code UnsupportedOperationException}.
     * @param language Language of the preferred label to remove
     * @return This semantic entity to enable cascading calls
     */
    SemanticEntity removePrefLabel(String language);
    
    /**
     * Removes the specified alternative label for the specified language if
     * any.
     * This method is optional. If not implemented (e.g., read only
     * semantic entity implementation, see {@linkplain #isReadOnly()})
     * this should throw an {@code UnsupportedOperationException}.
     * @param altLabel Alternative label to remove
     * @param language Language of the label
     * @return This semantic entity to enable cascading calls
     */
    SemanticEntity removeAltLabel(String altLabel, String language);
    
    /**
     * Removes the alternative labels for the specified language if
     * any.
     * This method is optional. If not implemented (e.g., read only
     * semantic entity implementation, see {@linkplain #isReadOnly()})
     * this should throw an {@code UnsupportedOperationException}.
     * @param language Language of the alternative labels to remove
     * @return This semantic entity to enable cascading calls
     */
    SemanticEntity removeAltLabels(String language);
    
    /**
     * Checks whether the semantic entity has a preferred label in the
     * specified language
     * @param language Language code
     * @return {@code true} if the semantic entity has a preferred
     * label in the specified language; {@code false} otherwise
     */
    boolean hasPrefLabel(String language);
    
    /**
     * Checks whether the semantic entity has the provided value as an
     * alternative label in the specified language
     * @param value Label to check
     * @param language Language code of the label
     * @return {@code true} if the semantic entity has {@code value} as an
     * alternative label in the specified language; {@code false} otherwise
     */
    boolean hasAltLabel(String value, String language);
    
    /**
     * Checks whether the semantic entity has an alternative label in the
     * specified language
     * @param language Language code
     * @return {@code true} if the semantic entity has an
     * alternative label in the specified language; {@code false} otherwise
     */
    boolean hasAltLabel(String language);
    
    /**
     * Returns the list of languages of all the preferred labels of this
     * semantic entity
     * @return {@code List<String>} containing all the languages of the
     * preferred labels of this semantic entity if any; otherwise an
     * <b><i>empty list</i></b>
     */
    List<String> getPrefLabelLanguages();
    
    /**
     * Returns the list of languages of all the alternative labels of this
     * semantic entity
     * @return {@code List<String>} containing all the languages of the
     * alternative labels of this semantic entity if any; otherwise an
     * <b><i>empty list</i></b>
     */
    List<String> getAltLabelLanguages();
    
    /**
     * Returns the semantic type of this entity. If more than one semantic types
     * are available, then the "first" is returned.
     * @return String identifier of the semantic type of this entity. If no
     * semantic type is found then {@code null} is returned.
     */
    String getSemanticType();
    
    /**
     * Semantic types of this entity
     * @return List of semantic types of this entity. If no semantic type exists
     * for this entity then an empty list is returned.
     */
    List<String> getSemanticTypes();
    
    /**
     * Adds the provided semantic type to this entity
     * @param semanticType String identifier of a semantic type
     * @return This semantic entity to enable cascading calls
     */
    SemanticEntity addSemanticType(String semanticType);
    
    /**
     * Removes the association between this entity and the provided semantic type 
     * @param semanticType Semantic type to remove
     * @return This semantic entity to enable cascading calls
     */
    SemanticEntity removeSemanticType(String semanticType);
    
    /**
     * Removes all the semantic types associated with this entity
     * @return This semantic entity to enable cascading calls
     */
    SemanticEntity removeSemanticTypes();
    
    /**
     * Tells whether the provided semantic type is associated with this entity
     * @param semanticType String identifier of a semantic type
     * @return {@code true} if this entity is associated with {@code semanticType},
     * {@code false} otherwise
     */
    boolean hasSemanticType(String semanticType);
    
    /**
     * Semantic types of this entity
     * @return List of direct and indirect (transitive) semantic types of this
     * entity. If no such semantic type exists
     * for this entity then an empty list is returned.
     */
    List<String> getSemanticTypesTransitive();
    
    /**
     * Tells whether the provided semantic type is directly or indirectly
     * associated with this entity
     * @param semanticType String identifier of a semantic type
     * @return {@code true} if this entity is directly or indirectly
     * associated with {@code semanticType}, {@code false} otherwise
     */
    boolean hasSemanticTypeTransitive(String semanticType);
    
    /**
     * Specifies whether this instance (or implementation) is read only, in
     * which case all write operations are not supported
     * @return {@code true} if the object is read only, {@code false} otherwise
     */
    boolean isReadOnly();
    
    /**
     * Sets the language to be considered while comparing this semantic entity
     * to other semantic entities.
     * The problem we are trying to solve here is as follows. Sorting entities
     * should ideally be based on their labels (preferred,
     * and alternative). As resources are multilingual and may be queried
     * in different languages, they need to be sorted depending on the requestor's
     * language of interest. To allow for this flexibility, we introduced this method,
     * which will enable a requestor to set their language of interest before sorting
     * or comparing resources.
     * 
     * @param lang Language to set for comparing the resource with other resources
     */
    void setComparisonLanguage(String lang);
    
    /**
     * Language to use as the basis for comparing this resource to other resources.
     * Please check {@linkplain #setComparisonLanguage(java.lang.String)} for more
     * details on the need for the comparison language.
     * @return Comparison language for this resource. If the comparison language has
     * not been set previously, then this returns {@code null}.
     */
    String getComparisonLanguage();
    
}
