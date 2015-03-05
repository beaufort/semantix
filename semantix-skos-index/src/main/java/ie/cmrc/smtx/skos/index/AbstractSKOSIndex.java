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
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

/**
 * An abstract {@link SKOSIndex} implementation which may be used as a base for implementing
 * concrete SKOS indexes
 * @author Yassine Lassoued
 */
public abstract class AbstractSKOSIndex implements SKOSIndex {

    /**
     * Languages supported by the index
     */
    protected final Collection<String> languages;
    
    /**
     * Minimum length of keyword required for search requests
     */
    protected int minKeywordLength = DEFAULT_MIN_KEYWORD_LENGTH;
    
    
    public AbstractSKOSIndex(Collection<String> languages) {
        this.languages = languages;
    }
    
    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> query(String keyword, int offset, int limit) throws ParseException {
        return this.query(keyword, null, null, offset, limit);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ParseException {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> query(String keyword, Collection<String> conceptSchemes, int offset, int limit) throws ParseException {
        return this.query(keyword, conceptSchemes, null, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param queryString {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @param collections {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     * @throws java.text.ParseException {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> query(String queryString, Collection<String> conceptSchemes, Collection<String> collections, int offset, int limit) throws java.text.ParseException {
        return this.query(queryString, conceptSchemes, collections, false, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> search(Term keyword, int offset, int limit) {
        return this.search(keyword, null, null, null, offset, limit);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> search(Term keyword, Collection<String> conceptSchemes, int offset, int limit) {
        return this.search(keyword, null, conceptSchemes, null, offset, limit);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @param collections {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> search(Term keyword, Collection<String> conceptSchemes, Collection<String> collections, int offset, int limit) {
        return this.search(keyword, null, conceptSchemes, collections, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @param collections {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> search(Term keyword, Collection<String> conceptSchemes, Collection<String> collections, boolean transitiveMember, int offset, int limit) {
        return this.search(keyword, null, conceptSchemes, collections, transitiveMember, offset, limit);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param field {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> search(Term keyword, IndexField.Searchable field, int offset, int limit) {
        return this.search(keyword, field, null, null, offset, limit);
    }

    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param field {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> search(Term keyword, IndexField.Searchable field, Collection<String> conceptSchemes, int offset, int limit) {
        return this.search(keyword, field, conceptSchemes, null, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @param keyword {@inheritDoc}
     * @param field {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @param collections {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param limit {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<Scored<SemanticEntity>> search(Term keyword, IndexField.Searchable field, Collection<String> conceptSchemes, Collection<String> collections, int offset, int limit) {
        return this.search(keyword, field, conceptSchemes, collections, false, offset, limit);
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Collection<String> getLanguages() {
        return languages;
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int getMinKeywordLength() {
        return minKeywordLength;
    }

    /**
     * {@inheritDoc}
     * @param minKeywordLength {@inheritDoc}
     */
    @Override
    public void setMinKeywordLength(int minKeywordLength) {
        this.minKeywordLength = minKeywordLength;
    }
    
}
