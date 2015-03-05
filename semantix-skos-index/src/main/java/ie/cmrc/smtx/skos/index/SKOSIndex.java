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

package ie.cmrc.smtx.skos.index;

import ie.cmrc.smtx.base.SemanticEntity;
import ie.cmrc.util.Term;
import java.io.Closeable;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

/**
 * {@code SKOSIndex} is a generic interface for searching SKOS resources by
 * label, name, URI or definition
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public interface SKOSIndex extends Closeable {
    
    public static final int DEFAULT_MIN_KEYWORD_LENGTH = 2;
    
    /**
     * Retrieve the concept identified by the provided URI
     * @param conceptURI URI of a concept to retrieve
     * @return {@link ie.cmrc.semantix.SemanticEntity} whose URI is
     * {@code conceptURI} if any, otherwise {@code null}
     */
    SemanticEntity getConceptByURI(String conceptURI);
    
    /**
     * Retrieves concepts by name
     * @param conceptName Name of a concept
     * @return List of {@link ie.cmrc.semantix.SemanticEntity}s whose local
     * name is {@code conceptName} if any, otherwise and empty list
     */
    List<SemanticEntity> getConceptsByName(String conceptName);
    
    /**
     * Lists the semantic entities matching the provided query. This method
     * accepts a Lucene query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities matching
     * {@code queryString}. If no matches are found, the en empty {@code List} is returned.
     * @throws java.text.ParseException If a parsing error is encountered
     */
    List<Scored<SemanticEntity>> query(String queryString, int offset, int limit) throws ParseException;
    
    /**
     * Lists the semantic entities matching the provided query and belonging
     * to at least one of the concept schemes identified by the provided URIs.
     * This method accepts a Lucene query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param conceptSchemes List of URIs of concept schemes to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities matching
     * {@code queryString} and belonging to at least one of the concept schemes
     * identified by the URIs in {@code conceptSchemes}.
     * If no matches are found, the en empty {@code List} is returned.
     * @throws java.text.ParseException If a parsing error is encountered
     */
    List<Scored<SemanticEntity>> query(String queryString, Collection<String> conceptSchemes, int offset, int limit) throws ParseException;
    
    /**
     * Lists the semantic entities matching the provided query and belonging
     * to at least one of the concept schemes and at least one of the
     * collections identified by the provided URIs. This method accepts a Lucene
     * query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param conceptSchemes List of URIs of concept schemes to restrict search to
     * @param collections List of URIs of SKOS collections to restrict search to
     * (only direct members of these collections are considered)
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities
     * matching {@code queryString} and belonging to at least one of the specified
     * concept schemes and at least one of the specified target collections.
     * If no matches are found, the en empty {@code List} is returned.
     * @throws java.text.ParseException If a parsing error is encountered
     */
    List<Scored<SemanticEntity>> query(String queryString, Collection<String> conceptSchemes, Collection<String> collections, int offset, int limit) throws ParseException;
    
    /**
     * Lists the semantic entities matching the provided query and belonging
     * to at least one of the concept schemes and at least one of the
     * collections identified by the provided URIs. This method accepts a Lucene
     * query as per
     * http://lucene.apache.org/core/2_9_4/queryparsersyntax.html and
     * http://www.lucenetutorial.com/lucene-query-syntax.html.
     * For the exact field names, please check {@link IndexField.Searchable}.
     * @param queryString Lucene query String
     * @param conceptSchemes List of URIs of concept schemes to restrict search to
     * @param collections List of URIs of SKOS collections to restrict search to
     * @param transitiveMember Indicates whether indirect (transitive) members
     * of the provided collections should be considered
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities
     * matching {@code queryString} and belonging to at least one of the specified
     * concept schemes and at least one of the specified target collections.
     * If no matches are found, the en empty {@code List} is returned.
     * @throws java.text.ParseException If a parsing error is encountered
     */
    List<Scored<SemanticEntity>> query(String queryString, Collection<String> conceptSchemes, Collection<String> collections, boolean transitiveMember, int offset, int limit) throws ParseException;
    
    /**
     * Lists the semantic entities matching the provided keyword
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities matching
     * {@code keyword}.
     * If no matches are found, the en empty {@code List} is returned.
     */
    List<Scored<SemanticEntity>> search(Term keyword, int offset, int limit);
    
    /**
     * Lists the semantic entities matching the provided keyword and belonging to
     * at least one of the concept schemes identified by the provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param conceptSchemes List of URIs of concept schemes to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities matching
     * {@code keyword}.
     * If no matches are found, the en empty {@code List} is returned.
     */
    List<Scored<SemanticEntity>> search(Term keyword, Collection<String> conceptSchemes, int offset, int limit);
    
    /**
     * Lists the semantic entities matching the provided keyword and
     * belonging to at least one of the concept schemes and at least one of the
     * collections identified by the provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param conceptSchemes List of URIs of concept schemes to restrict search to
     * @param collections List of URIs of SKOS collections to restrict search to
     * (only direct members of these collections are considered)
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities matching
     * {@code keyword}.
     * If no matches are found, the en empty {@code List} is returned.
     */
    List<Scored<SemanticEntity>> search(Term keyword, Collection<String> conceptSchemes, Collection<String> collections, int offset, int limit);
    
    /**
     * Lists the semantic entities matching the provided keyword and
     * belonging to at least one of the concept schemes and at least one of the
     * collections identified by the provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param conceptSchemes List of URIs of concept schemes to restrict search to
     * @param collections List of URIs of SKOS collections to restrict search to
     * (only direct members of these collections are considered)
     * @param transitiveMember Indicates whether indirect (transitive) members
     * of the provided collections should be considered
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities matching
     * {@code keyword}.
     * If no matches are found, the en empty {@code List} is returned.
     */
    List<Scored<SemanticEntity>> search(Term keyword, Collection<String> conceptSchemes, Collection<String> collections, boolean transitiveMember, int offset, int limit);
    
    /**
     * Lists the semantic entities whose values for the provided field
     * match the provided keyword
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param field Field to search the keyword in
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities matching
     * {@code keyword}.
     * If no matches are found, the en empty {@code List} is returned.
     */
    List<Scored<SemanticEntity>> search(Term keyword, IndexField.Searchable field, int offset, int limit);
    
    /**
     * Lists the semantic entities whose values for the provided field
     * match the provided keyword and which belong to at least one of the
     * concept schemes identified by the provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param field Field to search the keyword in
     * @param conceptSchemes List of URIs of concept schemes to restrict search to
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities matching
     * {@code keyword}.
     * If no matches are found, the en empty {@code List} is returned.
     */
    List<Scored<SemanticEntity>> search(Term keyword, IndexField.Searchable field, Collection<String> conceptSchemes, int offset, int limit);
    
    /**
     * Lists the semantic entities whose values for the provided field
     * match the provided keyword and which belong to at least one of the
     * concept schemes and at least one of the collections identified by the
     * provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param field Field to search the keyword in
     * @param conceptSchemes List of URIs of concept schemes to restrict search to
     * @param collections List of URIs of SKOS collections to restrict search to
     * (only direct members of these collections are considered)
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities matching
     * {@code keyword}.
     * If no matches are found, the en empty {@code List} is returned.
     */
    List<Scored<SemanticEntity>> search(Term keyword, IndexField.Searchable field, Collection<String> conceptSchemes, Collection<String> collections, int offset, int limit);
    
    /**
     * Lists the semantic entities whose values for the provided field
     * match the provided keyword and which belong to at least one of the
     * concept schemes and at least one of the collections identified by the
     * provided URIs
     * @param keyword A {@link ie.cmrc.util.Term} consisting of a string keyword
     * to search and a language code. If the language code is {@code null} then
     * the search is performed across all the available languages
     * @param field Field to search the keyword in
     * @param conceptSchemes List of URIs of concept schemes to restrict search to
     * @param collections List of URIs of SKOS collections to restrict search to
     * @param transitiveMember Indicates whether indirect (transitive) members
     * of the provided collections should be considered
     * @param offset Number of results to skip before returning any results. Results
     * that are skipped due to {@code offset} do not count against {@code limit}.
     * @param limit Maximum number of results the query will return
     * @return {@code List<Scored<SemanticEntity>>} containing the semantic entities matching
     * {@code keyword}.
     * If no matches are found, the en empty {@code List} is returned.
     */
    List<Scored<SemanticEntity>> search(Term keyword, IndexField.Searchable field, Collection<String> conceptSchemes, Collection<String> collections, boolean transitiveMember, int offset, int limit);
    
    /**
     * Lists the languages supported by the index
     * @return String collection containing the codes of the languages supported
     * by the SKOS index
     */
    Collection<String> getLanguages();
    
    /**
     * Minimum length of keyword required for search requests. This defaults to
     * {@linkplain #DEFAULT_MIN_KEYWORD_LENGTH}.
     * @return Minimum length of keyword required for search requests
     */
    public int getMinKeywordLength();

    /**
     * Sets the minimum length of keyword required for search requests
     * @param minKeywordLength Minimum length of keyword required for search requests
     */
    public void setMinKeywordLength(int minKeywordLength);
}
