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

package ie.cmrc.smtx.sws.client;

import ie.cmrc.smtx.base.SemanticEntity;
import ie.cmrc.smtx.skos.model.SKOSCollection;
import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSConceptScheme;
import ie.cmrc.smtx.skos.model.SKOSSemanticProperty;
import ie.cmrc.smtx.skos.model.hierarchy.HierarchyMethod;
import ie.cmrc.smtx.skos.model.hierarchy.SKOSConceptNode;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import ie.cmrc.smtx.skos.index.IndexField;
import ie.cmrc.smtx.thesaurus.SWSThesaurus;
import ie.cmrc.util.Term;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class SWSClient implements SWSThesaurus {

    @Override
    public CloseableIterator<SKOSConceptScheme> listConceptSchemes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SKOSConceptScheme getConceptScheme(String conceptSchemeURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSCollection> listCollections() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSCollection> listCollections(String conceptSchemeURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSCollection> listCollections(String conceptSchemeURI, String skosCollectionURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SKOSCollection getCollection(String skosCollectionURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSConcept> listTopConcepts() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSConcept> listTopConcepts(String conceptSchemeURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSConcept> listTopConcepts(String conceptSchemeURI, String skosCollectionURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> getBroadestConcepts() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> getBroadestConcepts(String conceptSchemeURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> getBroadestConcepts(String conceptSchemeURI, String skosCollectionURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSConcept> listConcepts() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSConcept> listConcepts(String conceptSchemeURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSConcept> listConcepts(String conceptSchemeURI, String skosCollectionURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConceptNode> getConceptHierarchy(HierarchyMethod hierarchyMethod) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConceptNode> getConceptHierarchy(String conceptSchemeURI, HierarchyMethod hierarchyMethod) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConceptNode> getConceptHierarchy(String conceptSchemeURI, String skosCollectionURI, HierarchyMethod hierarchyMethod) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SKOSConceptNode getConceptTree(String rootConceptURI, HierarchyMethod hierarchyMethod) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SKOSConceptNode getConceptTree(String rootConceptURI, String conceptSchemeURI, HierarchyMethod hierarchyMethod) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SKOSConceptNode getConceptTree(String rootConceptURI, String conceptSchemeURI, String skosCollectionURI, HierarchyMethod hierarchyMethod) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, String conceptSchemeURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, String conceptSchemeURI, String skosCollectionURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, Collection<String> conceptSchemeURIs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloseableIterator<SKOSConcept> listRelatedConcepts(String conceptURI, SKOSSemanticProperty relationshipType, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> getDirectNarrowerConcepts(String conceptURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> getDirectNarrowerConcepts(String conceptURI, String conceptSchemeURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> getDirectNarrowerConcepts(String conceptURI, String conceptSchemeURI, String skosCollectionURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> getDirectBroaderConcepts(String conceptURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> getDirectBroaderConcepts(String conceptURI, String conceptSchemeURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> getDirectBroaderConcepts(String conceptURI, String conceptSchemeURI, String skosCollectionURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SKOSConcept getConcept(String conceptURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, String skosCollectionURI, int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SemanticEntity> search(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SemanticEntity> search(String queryString, int offset, int limit) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SemanticEntity> search(String queryString, String conceptSchemeURI, int offset, int limit) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SemanticEntity> search(String queryString, String conceptSchemeURI, String skosCollectionURI, int offset, int limit) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SemanticEntity> search(String queryString, Collection<String> conceptSchemeURIs, int offset, int limit) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SemanticEntity> search(String queryString, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, String conceptSchemeURI, String skosCollectionURI, int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> searchConcepts(Term keyword, IndexField.Searchable searchField, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> searchConcepts(String queryString, int offset, int limit) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> searchConcepts(String queryString, String conceptSchemeURI, int offset, int limit) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> searchConcepts(String queryString, String conceptSchemeURI, String skosCollectionURI, int offset, int limit) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> searchConcepts(String queryString, Collection<String> conceptSchemeURIs, int offset, int limit) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> searchConcepts(String queryString, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs, int offset, int limit) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> interpretKeyword(Term keyword) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> interpretKeyword(Term keyword, String conceptSchemeURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> interpretKeyword(Term keyword, String conceptSchemeURI, String skosCollectionURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> interpretKeyword(Term keyword, Collection<String> conceptSchemeURIs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> interpretKeyword(Term keyword, Collection<String> conceptSchemeURIs, Collection<String> skosCollectionURIs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
