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

package ie.cmrc.smtx.skos.jena;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import ie.cmrc.smtx.skos.model.SKOSCollection;
import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSConceptScheme;
import ie.cmrc.smtx.skos.model.SKOSResource;
import ie.cmrc.smtx.jena.selector.FilteredSelector;
import ie.cmrc.smtx.skos.jena.util.JenaResourceIterator;
import ie.cmrc.smtx.skos.jena.util.SKOSResourceIterFactory;
import ie.cmrc.smtx.jena.selector.filter.StmtFilter;
import ie.cmrc.smtx.jena.selector.filter.StmtSubjectHasPropertyFilter;
import ie.cmrc.smtx.skos.model.SKOSElementProperty;
import ie.cmrc.smtx.skos.model.SKOSType;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link ie.cmrc.skos.core.SKOSConceptScheme} implementation based on Jena resources
 * @author Yassine Lassoued
 */
public class JenaSKOSConceptScheme extends JenaSKOSResource implements SKOSConceptScheme {

    /**
     * Wraps the provided Jena {@code com.hp.hpl.jena.rdf.model.Resource} as a {@link ie.cmrc.skos.core.SKOSConceptScheme}
     * @param res Jena {@code com.hp.hpl.jena.rdf.model.Resource} to wrap as a {@link ie.cmrc.skos.core.SKOSConceptScheme}
     */
    public JenaSKOSConceptScheme(Resource res) {
        super(res);
    }

    /**
     * {@inheritDoc}
     * @return {@code SKOSType.ConceptScheme}
     */
    @Override
    public SKOSType getSkosType() {
        return SKOSType.ConceptScheme;
    }
    
//    /**
//     * {@inheritDoc}
//     * @param skosResourceURI {@inheritDoc}
//     * @return {@inheritDoc}
//     * @throws IllegalArgumentException {@inheritDoc}
//     */
//    @Override
//    public SKOSConceptScheme add(String skosResourceURI) throws IllegalArgumentException {
//        if (skosResourceURI!=null && !skosResourceURI.isEmpty()) {
//            SKOSResource skosResource = this.getSKOS().getSKOSResource(skosResourceURI);
//            if (skosResource != null) {
//                if (skosResource.isSKOSConcept() || skosResource.isSKOSCollection()) {
//                    skosResource.addRelation(SKOSElementProperty.inScheme, this);
//                }
//                else throw new IllegalArgumentException("\""+skosResourceURI+"\" is not the URI of a SKOS Concept or SKOS Collection");
//            }
//        }
//        return this;
//    }

    /**
     * {@inheritDoc}
     * @param skosResource {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConceptScheme add(SKOSResource skosResource) {
        if (skosResource != null) {
            SKOSResource res = this.getSKOS().getSKOSResource(skosResource);
            if (res != null) {
                skosResource.addRelation(SKOSElementProperty.inScheme, this);
            }
        }
        return this;
    }

//    /**
//     * {@inheritDoc}
//     * @param skosResourceURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public boolean has(String skosResourceURI) {
//        return this.isRelation(skosResourceURI, SKOSElementProperty.inScheme);
//    }

    /**
     * {@inheritDoc}
     * @param skosResource {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean has(SKOSResource skosResource) {
        return this.isRelation(skosResource, SKOSElementProperty.inScheme);
    }

//    /**
//     * {@inheritDoc}
//     * @param skosResourceURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public SKOSConceptScheme remove(String skosResourceURI) {
//        if (skosResourceURI!=null && !skosResourceURI.isEmpty()) {
//            SKOSResource skosResource = this.getSKOS().getSKOSResource(skosResourceURI);
//            if (skosResource != null) {
//                this.removeAsRelation(skosResource, SKOSElementProperty.inScheme);
//            }
//        }
//        return this;
//    }

    /**
     * {@inheritDoc}
     * @param skosResource {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConceptScheme remove(SKOSResource skosResource) {
        if (skosResource != null) {
            this.removeAsRelation(skosResource, SKOSElementProperty.inScheme);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SKOSConcept createConcept(String conceptURI) throws IllegalArgumentException {
        SKOSConcept concept = this.getSKOS().createConcept(conceptURI);
        if (concept!=null) {
            concept.addToConceptScheme(this);
            this.makeRelation(concept, SKOSElementProperty.inScheme);
        }
        return concept;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getConcepts() {
        List<SKOSConcept> concepts = new ArrayList<SKOSConcept>();
        JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.inScheme);
        while (iter.hasNext()) {
            SKOSResource skosResource = fromJenaResource(iter.next());
            if (skosResource!=null && skosResource.isSKOSConcept()) concepts.add(skosResource.asSKOSConcept());
        }
        iter.close();
        return concepts;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listConcepts() {
        Property property = PropertyFactory.inScheme;
        Property filterProperty = com.hp.hpl.jena.vocabulary.RDF.type;
        Resource filterObject = TypeResourceFactory.CONCEPT;
        StmtFilter filter = new StmtSubjectHasPropertyFilter(filterProperty, filterObject);
        
        Selector filteredSelector = new FilteredSelector((Resource)null, property, this.resource, filter);
        
        StmtIterator iter = this.getModel().listStatements(filteredSelector);
        return SKOSResourceIterFactory.makeSKOSConceptIterOverSubjects(iter);
    }
    
//    /**
//     * {@inheritDoc}
//     * @param collectionURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSConcept> getConcepts(String collectionURI) {
//        List<SKOSConcept> concepts = new ArrayList<SKOSConcept>();
//        if (collectionURI!=null && !collectionURI.isEmpty()) {
//            Property memberProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.member);
//            SKOSCollection collection = this.getSKOS().getCollection(collectionURI);
//            if (memberProp!=null && collection!=null) {
//                JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.inScheme);
//                while (iter.hasNext()) {
//                    Resource res = iter.next();
//                    SKOSConcept concept = JenaSKOSResource.skosConceptFromJenaResource(res);
//                    if (concept!=null && concept.isMemberOfCollection(collection)) concepts.add(concept);
//                }
//                iter.close();
//            }
//        }
//        return concepts;
//    }

//    /**
//     * {@inheritDoc}
//     * @param collection {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSConcept> getConcepts(SKOSCollection collection) {
//        List<SKOSConcept> concepts = new ArrayList<SKOSConcept>();
//        Property memberProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.member);
//        if (memberProp!=null && collection!=null) {
//            JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.inScheme);
//            while (iter.hasNext()) {
//                Resource res = iter.next();
//                SKOSConcept concept = JenaSKOSResource.skosConceptFromJenaResource(res);
//                if (concept!=null && concept.isMemberOfCollection(collection)) concepts.add(concept);
//            }
//            iter.close();
//        }
//        return concepts;
//    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConceptScheme addTopConcept(SKOSConcept concept) {
        if (concept != null) {
            this.addRelation(SKOSElementProperty.hasTopConcept, concept);
            //concept.addRelation(SKOSObjectProperty.TOP_CONCEPT_OF, this);
        }
        return this;
    }

//    /**
//     * {@inheritDoc}
//     * @param conceptURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public boolean hasTopConcept(String conceptURI) {
//        if (conceptURI!=null && !conceptURI.isEmpty()) {
//            return this.hasRelation(SKOSElementProperty.hasTopConcept, conceptURI);
//        }
//        else return false;
//    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasTopConcept(SKOSConcept concept) {
        if (concept!=null) {
            return this.hasRelation(SKOSElementProperty.hasTopConcept, concept);
        }
        else return false;
    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConceptScheme removeTopConcept(SKOSConcept concept) {
        if (concept!=null) {
            this.removeRelation(SKOSElementProperty.hasTopConcept, concept);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConceptScheme removeTopConcepts() {
        return (this.removeRelations(SKOSElementProperty.hasTopConcept)).asSKOSConceptScheme();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getTopConcepts() {
        List<SKOSConcept> topConcepts = new ArrayList<SKOSConcept>();
        
        JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.topConceptOf);
        
        while (iter.hasNext()) {
            SKOSConcept concept = JenaSKOSResource.skosConceptFromJenaResource(iter.next());
            if (concept!=null) topConcepts.add(concept);
        }
        
        return topConcepts;
    }
    
//    /**
//     * {@inheritDoc}
//     * @param collectionURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSConcept> getTopConcepts(String collectionURI) {
//        List<SKOSConcept> topConcepts = new ArrayList<SKOSConcept>();
//        if (collectionURI!=null && !collectionURI.isEmpty()) {
//            Property memberProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.member);
//            SKOSCollection collection = this.getSKOS().getCollection(collectionURI);
//
//            if (collection!=null && memberProp != null) {
//                JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.topConceptOf);
//                while (iter.hasNext()) {
//                    Resource res = iter.next();
//                    SKOSConcept concept = JenaSKOSResource.skosConceptFromJenaResource(res);
//                    if (concept!=null && concept.isMemberOfCollection(collection)) topConcepts.add(concept);
//                }
//                iter.close();
//            }
//        }
//        return topConcepts;
//    }

//    /**
//     * {@inheritDoc}
//     * @param collection {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSConcept> getTopConcepts(SKOSCollection collection) {
//        List<SKOSConcept> topConcepts = new ArrayList<SKOSConcept>();
//        Property memberProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.member);
//        if (collection!=null && memberProp!=null) {
//            JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.topConceptOf);
//            while (iter.hasNext()) {
//                Resource res = iter.next();
//                SKOSConcept concept = JenaSKOSResource.skosConceptFromJenaResource(res);
//                if (concept!=null && concept.isMemberOfCollection(collection)) topConcepts.add(concept);
//            }
//            iter.close();
//        }
//        return topConcepts;
//    }

    /*
    @Override
    public boolean hasBroadestConcept(String conceptURI) {
        if (conceptURI!=null && !conceptURI.isEmpty()) {
            JenaSKOSConcept concept = (JenaSKOSConcept)(this.getSKOSThesaurus().getConcept(conceptURI));
            if (concept != null && this.hasSKOSResource(concept)) {
                Property broaderTransitive = ((JenaSKOSThesaurus)this.getSKOSThesaurus()).getProperty(SKOSObjectProperty.BROADER_TRANSITIVE);
                if (broaderTransitive != null) {
                    StmtIterator iter = concept.listProperties(broaderTransitive);
                    while (iter.hasNext()) {
                        Statement stmt = iter.next();
                        RDFNode object = stmt.getObject();
                        
                        if (object.isResource()) {
                            Resource objRes = object.asResource();
                            if (JenaSKOSResource.jenaResourceIsSKOSConcept(objRes)) {
                                SKOSConcept broaderConcept = JenaSKOSResource.skosConceptFromJenaResource(objRes);
                                if (broaderConcept.isInScheme(this) || this.hasSKOSResource(broaderConcept)) return false;
                            }
                        }
                    }
                    iter.close();
                }
                return true;
            }
            else return false;
        }
        else return false;
    }
    
    @Override
    public boolean hasBroadestConcept(SKOSConcept concept) {
        if (concept!=null) {
            return this.hasBroadestConcept(concept.getURI());
        }
        else return false;
    }
    
    @Override
    public List<SKOSConcept> getBroadestConcepts() {
        List<SKOSConcept> broadestConcepts = new ArrayList<SKOSConcept>();
        
        Property inShemeProp = ((JenaSKOSThesaurus)this.getSKOSThesaurus()).getProperty(SKOSObjectProperty.IN_SCHEME);
        Property broaderTransitiveProp = ((JenaSKOSThesaurus)this.getSKOSThesaurus()).getProperty(SKOSObjectProperty.BROADER_TRANSITIVE);
        if (inShemeProp!=null && broaderTransitiveProp!=null) {
            StmtIterator iter = this.resource.listProperties(inShemeProp);

            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                RDFNode node = stmt.getObject();
                // Only consider those statements with a resource as an object
                if (node.isResource()) {
                    Resource objResource = (Resource)node;
                    JenaSKOSConcept concept = (JenaSKOSConcept)JenaSKOSResource.skosConceptFromJenaResource(objResource);
                    if (concept!=null) {
                        
                        boolean isBroadest = true;
                        
                        StmtIterator broaderIter = concept.listProperties(broaderTransitiveProp);
                        while (broaderIter.hasNext() && isBroadest) {
                            Statement broaderStmt = broaderIter.next();
                            RDFNode object = broaderStmt.getObject();

                            if (object.isResource()) {
                                Resource objRes = object.asResource();
                                if (JenaSKOSResource.jenaResourceIsSKOSConcept(objRes)) {
                                    SKOSConcept broaderConcept = JenaSKOSResource.skosConceptFromJenaResource(objRes);
                                    if (broaderConcept.isInScheme(this) || this.hasSKOSResource(broaderConcept)) isBroadest = false;
                                }
                            }
                        }
                        broaderIter.close();
                        
                        if (isBroadest) broadestConcepts.add(concept);
                    }
                }
            }
            iter.close();
        }
        
        return broadestConcepts;
    }

    @Override
    public List<SKOSConcept> getBroadestConcepts(String collectionURI) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SKOSConcept> getBroadestConcepts(SKOSCollection collection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    */
    
    /**
     * {@inheritDoc}
     * @param collectionURI {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SKOSCollection createCollection(String collectionURI) throws IllegalArgumentException {
        SKOSCollection collection = this.getSKOS().createCollection(collectionURI);
        if (collection!=null) {
            this.makeRelation(collection, SKOSElementProperty.inScheme);
        }
        return collection;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollections() {
        StmtIterator iter = null;
        
        Property property = PropertyFactory.inScheme;
        Property filterProperty = com.hp.hpl.jena.vocabulary.RDF.type;
        Resource filterObject = TypeResourceFactory.COLLECTION;
        StmtFilter filter = new StmtSubjectHasPropertyFilter(filterProperty, filterObject);
        Selector filteredSelector = new FilteredSelector((Resource)null, property, this, filter);
        
        iter = this.getModel().listStatements(filteredSelector);
        return SKOSResourceIterFactory.makeSKOSCollectionIterOverSubjects(iter);
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSCollection> getCollections() {
        List<SKOSCollection> collections = new ArrayList<SKOSCollection>();
        JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.inScheme);
        while (iter.hasNext()) {
            SKOSCollection skosCollection = JenaSKOSResource.skosCollectionFromJenaResource(iter.next());
            if (skosCollection!=null) collections.add(skosCollection);
        }
        iter.close();
        return collections;
    }

//    /**
//     * {@inheritDoc}
//     * @param collectionURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSCollection> getCollections(String collectionURI) {
//        List<SKOSCollection> collections = new ArrayList<SKOSCollection>();
//        if (collectionURI!=null && !collectionURI.isEmpty()) {
//            Property memberProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.member);
//            SKOSCollection collection = this.getSKOS().getCollection(collectionURI);
//            if (memberProp!=null && collection!=null) {
//                JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.inScheme);
//                while (iter.hasNext()) {
//                    Resource res = iter.next();
//                    SKOSCollection coll = JenaSKOSResource.skosCollectionFromJenaResource(res);
//                    if (coll!=null && coll.isMemberOfCollection(collection)) collections.add(coll);
//                }
//                iter.close();
//            }
//        }
//        return collections;
//    }

//    /**
//     * {@inheritDoc}
//     * @param collection {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSCollection> getCollections(SKOSCollection collection) {
//        List<SKOSCollection> collections = new ArrayList<SKOSCollection>();
//        Property memberProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.member);
//        if (memberProp!=null && collection!=null) {
//            JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.inScheme);
//            while (iter.hasNext()) {
//                Resource res = iter.next();
//                SKOSCollection coll = JenaSKOSResource.skosCollectionFromJenaResource(res);
//                if (coll!=null && coll.isMemberOfCollection(collection)) collections.add(coll);
//            }
//            iter.close();
//        }
//        return collections;
//    }
}
