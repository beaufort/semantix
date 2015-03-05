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
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import ie.cmrc.smtx.jena.selector.FilteredSelector;
import ie.cmrc.smtx.jena.selector.filter.StmtFilter;
import ie.cmrc.smtx.jena.selector.filter.StmtObjectHasPropertyFilter;
import ie.cmrc.smtx.skos.model.SKOSCollection;
import ie.cmrc.smtx.skos.model.SKOSCollectionMember;
import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSElementProperty;
import ie.cmrc.smtx.skos.model.SKOSResource;
import ie.cmrc.smtx.skos.model.SKOSType;
import ie.cmrc.smtx.skos.jena.util.SKOSResourceIterFactory;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yassine Lassoued
 */
public class JenaSKOSCollection extends JenaSKOSCollectionMember implements SKOSCollection {

    /**
     * Constructs a {@link JenaSKOSCollection} using the provided Jena {@code com.hp.hpl.jena.rdf.model.Resource}
     * @param res Jena Resource
     */
    public JenaSKOSCollection(Resource res) {
        super(res);
    }

    /**
     * {@inheritDoc}
     * @return {@code SKOSType.Collection}
     */
    @Override
    public SKOSType getSkosType() {
        return SKOSType.Collection;
    }
    
    /**
     * {@inheritDoc}
     * @param conceptOrCollectionURI {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SKOSCollection addMember(String conceptOrCollectionURI) throws IllegalArgumentException {
        if (conceptOrCollectionURI!=null && !conceptOrCollectionURI.isEmpty()) {
            SKOSResource skosResource = this.getSKOS().getSKOSResource(conceptOrCollectionURI);
            if (skosResource != null) {
                if (skosResource.isSKOSConcept() || skosResource.isSKOSCollection()) {
                    this.addRelation(SKOSElementProperty.member, skosResource);
                }
                else throw new IllegalArgumentException("\""+conceptOrCollectionURI+"\" is not the URI of a SKOS Concept or SKOS Collection");
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param skosCollectionMember {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSCollection addMember(SKOSCollectionMember skosCollectionMember) {
        if (skosCollectionMember != null) {
            this.addRelation(SKOSElementProperty.member, skosCollectionMember);
        }
        return this;
    }

//    /**
//     * {@inheritDoc}
//     * @param resourceURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public boolean hasMember(String resourceURI) {
//        return this.isRelation(resourceURI, SKOSElementProperty.member);
//    }

    /**
     * {@inheritDoc}
     * @param skosCollectionMember {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasMember(SKOSCollectionMember skosCollectionMember) {
        if (skosCollectionMember != null) {
            return this.hasRelation(SKOSElementProperty.member, skosCollectionMember);
        }
        else return false;
    }

    /**
     * {@inheritDoc}
     * @param skosCollectionMember {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasMemberTransitive(SKOSCollectionMember skosCollectionMember) {
        if (skosCollectionMember != null) {
            return this.hasRelation(SKOSElementProperty.memberTransitive, skosCollectionMember);
        }
        else return false;
    }

//    /**
//     * {@inheritDoc}
//     * @param skosResourceEltURI {@inheritDoc}
//     * @return {@inheritDoc}
//     * @throws IllegalArgumentException {@inheritDoc}
//     */
//    @Override
//    public SKOSCollection removeMember(String skosResourceEltURI) throws IllegalArgumentException {
//        if (skosResourceEltURI!=null && !skosResourceEltURI.isEmpty()) {
//            SKOSResource skosResource = this.getSKOS().getSKOSResource(skosResourceEltURI);
//            if (skosResource != null) {
//                if (skosResource.isSKOSConcept() || skosResource.isSKOSCollection()) {
//                    this.removeAsRelation(skosResource, SKOSElementProperty.member);
//                }
//            }
//        }
//        return this;
//    }

    /**
     * {@inheritDoc}
     * @param skosCollectionMember {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSCollection removeMember(SKOSCollectionMember skosCollectionMember) {
        if (skosCollectionMember != null) {
            this.removeRelation(SKOSElementProperty.member, skosCollectionMember);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollectionMember> listMembers() {
        Property property = PropertyFactory.member;
        
        StmtIterator iter = this.getModel().listStatements(this, property, (RDFNode)null);
        return SKOSResourceIterFactory.makeSKOSCollectionMemberIterOverObjects(iter);
    }

    @Override
    public CloseableIterator<SKOSCollectionMember> listMembersTransitive() {
        Property property = PropertyFactory.memberTransitive;
        
        StmtIterator iter = this.getModel().listStatements(this, property, (RDFNode)null);
        return SKOSResourceIterFactory.makeSKOSCollectionMemberIterOverObjects(iter);
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSCollectionMember> getMembers() {
        List<SKOSCollectionMember> members = new ArrayList<SKOSCollectionMember>();
        CloseableIterator<SKOSCollectionMember> iter = this.listMembers();
        while (iter.hasNext()) {
            SKOSCollectionMember member = iter.next();
            if (member!=null) members.add(member);
        }
        iter.close();
        return members;
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSCollectionMember> getMembersTransitive() {
        List<SKOSCollectionMember> members = new ArrayList<SKOSCollectionMember>();
        CloseableIterator<SKOSCollectionMember> iter = this.listMembersTransitive();
        while (iter.hasNext()) {
            SKOSCollectionMember member = iter.next();
            if (member!=null) members.add(member);
        }
        iter.close();
        return members;
    }

//    /**
//     * {@inheritDoc}
//     * @param conceptSchemeURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSCollectionMember> getMembers(String conceptSchemeURI) {
//        List<SKOSCollectionMember> members = new ArrayList<SKOSCollectionMember>();
//        if (conceptSchemeURI!=null && !conceptSchemeURI.isEmpty()) {
//            Property inSchemeProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.inScheme);
//            SKOSConceptScheme cs = this.getSKOS().getConceptScheme(conceptSchemeURI);
//            if (inSchemeProp!=null && cs!=null) {
//                JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.member);
//                while (iter.hasNext()) {
//                    Resource res = iter.next();
//                    SKOSCollectionMember skosRes = JenaSKOSResource.skosResourceElementFomJenaResource(res);
//                    if (skosRes!=null) members.add(skosRes);
//                }
//                iter.close();
//            }
//        }
//        return members;
//    }
//    /**
//     * {@inheritDoc}
//     * @param conceptScheme {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSCollectionMember> getMembers(SKOSConceptScheme conceptScheme) {
//        List<SKOSCollectionMember> members = new ArrayList<SKOSCollectionMember>();
//            Property inSchemeProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.inScheme);
//        if (conceptScheme!=null && inSchemeProp!=null) {
//            JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.member);
//            while (iter.hasNext()) {
//                Resource res = iter.next();
//                SKOSCollectionMember skosRes = JenaSKOSResource.skosResourceElementFomJenaResource(res);
//                if (skosRes!=null) members.add(skosRes);
//            }
//            iter.close();
//        }
//        return members;
//    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listConceptMembers() {
        Property property = PropertyFactory.member;
        Property filterProperty = com.hp.hpl.jena.vocabulary.RDF.type;
        Resource filterObject = TypeResourceFactory.CONCEPT;
        StmtFilter filter = new StmtObjectHasPropertyFilter(filterProperty, filterObject);
        
        Selector filteredSelector = new FilteredSelector(this.resource, property, (RDFNode)null, filter);
        
        StmtIterator iter = this.getModel().listStatements(filteredSelector);
        return SKOSResourceIterFactory.makeSKOSConceptIterOverObjects(iter);
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listConceptMembersTransitive() {
        Property property = PropertyFactory.memberTransitive;
        Property filterProperty = com.hp.hpl.jena.vocabulary.RDF.type;
        Resource filterObject = TypeResourceFactory.CONCEPT;
        StmtFilter filter = new StmtObjectHasPropertyFilter(filterProperty, filterObject);
        
        Selector filteredSelector = new FilteredSelector(this.resource, property, (RDFNode)null, filter);
        
        StmtIterator iter = this.getModel().listStatements(filteredSelector);
        return SKOSResourceIterFactory.makeSKOSConceptIterOverObjects(iter);
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getConceptMembers() {
        List<SKOSConcept> members = new ArrayList<>();
        CloseableIterator<SKOSConcept> iter = this.listConceptMembers();
        while (iter.hasNext()) {
            SKOSConcept concept = iter.next();
            if (concept!=null) members.add(concept);
        }
        iter.close();
        return members;
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getConceptMembersTransitive() {
        List<SKOSConcept> members = new ArrayList<>();
        CloseableIterator<SKOSConcept> iter = this.listConceptMembersTransitive();
        while (iter.hasNext()) {
            SKOSConcept concept = iter.next();
            if (concept!=null) members.add(concept);
        }
        iter.close();
        return members;
    }

//    /**
//     * {@inheritDoc}
//     * @param conceptSchemeURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSConcept> getConceptMembers(String conceptSchemeURI) {
//        List<SKOSConcept> concepts = new ArrayList<SKOSConcept>();
//        if (conceptSchemeURI!=null && !conceptSchemeURI.isEmpty()) {
//            Property inSchemeProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.inScheme);
//            SKOSConceptScheme cs = this.getSKOS().getConceptScheme(conceptSchemeURI);
//            if (inSchemeProp!=null && cs!=null) {
//                JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.member);
//                while (iter.hasNext()) {
//                    Resource res = iter.next();
//                    SKOSConcept concept = JenaSKOSResource.skosConceptFromJenaResource(res);
//                    if (concept!=null && concept.isInScheme(cs)) concepts.add(concept);
//                }
//                iter.close();
//            }
//        }
//        return concepts;
//    }
//    /**
//     * {@inheritDoc}
//     * @param conceptScheme {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSConcept> getConceptMembers(SKOSConceptScheme conceptScheme) {
//        List<SKOSConcept> concepts = new ArrayList<SKOSConcept>();
//        Property inSchemeProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.inScheme);
//        if (inSchemeProp!=null && conceptScheme!=null) {
//            JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.member);
//            while (iter.hasNext()) {
//                Resource res = iter.next();
//                SKOSConcept concept = JenaSKOSResource.skosConceptFromJenaResource(res);
//                if (concept!=null && concept.isInScheme(conceptScheme)) concepts.add(concept);
//            }
//            iter.close();
//        }
//        return concepts;
//    }
    
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollectionMembers() {
        Property property = PropertyFactory.member;
        Property filterProperty = com.hp.hpl.jena.vocabulary.RDF.type;
        Resource filterObject = TypeResourceFactory.COLLECTION;
        StmtFilter filter = new StmtObjectHasPropertyFilter(filterProperty, filterObject);
        
        Selector filteredSelector = new FilteredSelector(this.resource, property, (RDFNode)null, filter);
        
        StmtIterator iter = this.getModel().listStatements(filteredSelector);
        return SKOSResourceIterFactory.makeSKOSCollectionIterOverObjects(iter);
    }
    
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollectionMembersTransitive() {
        Property property = PropertyFactory.memberTransitive;
        Property filterProperty = com.hp.hpl.jena.vocabulary.RDF.type;
        Resource filterObject = TypeResourceFactory.COLLECTION;
        StmtFilter filter = new StmtObjectHasPropertyFilter(filterProperty, filterObject);
        
        Selector filteredSelector = new FilteredSelector(this.resource, property, (RDFNode)null, filter);
        
        StmtIterator iter = this.getModel().listStatements(filteredSelector);
        return SKOSResourceIterFactory.makeSKOSCollectionIterOverObjects(iter);
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSCollection> getCollectionMembers() {
        List<SKOSCollection> members = new ArrayList<>();
        CloseableIterator<SKOSCollection> iter = this.listCollectionMembers();
        while (iter.hasNext()) {
            SKOSCollection collection = iter.next();
            if (collection!=null) members.add(collection);
        }
        iter.close();
        return members;
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSCollection> getCollectionMembersTransitive() {
        List<SKOSCollection> members = new ArrayList<>();
        CloseableIterator<SKOSCollection> iter = this.listCollectionMembersTransitive();
        while (iter.hasNext()) {
            SKOSCollection collection = iter.next();
            if (collection!=null) members.add(collection);
        }
        iter.close();
        return members;
    }

//    /**
//     * {@inheritDoc}
//     * @param conceptSchemeURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSCollection> getCollectionMembers(String conceptSchemeURI) {
//        List<SKOSCollection> collections = new ArrayList<SKOSCollection>();
//        if (conceptSchemeURI!=null && !conceptSchemeURI.isEmpty()) {
//            Property inSchemeProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.inScheme);
//            SKOSConceptScheme cs = this.getSKOS().getConceptScheme(conceptSchemeURI);
//            if (inSchemeProp!=null && cs!=null) {
//                JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.member);
//                while (iter.hasNext()) {
//                    Resource res = iter.next();
//                    SKOSCollection coll = JenaSKOSResource.skosCollectionFromJenaResource(res);
//                    if (coll!=null && coll.isInScheme(cs)) collections.add(coll);
//                }
//                iter.close();
//            }
//        }
//        return collections;
//    }

//    /**
//     * {@inheritDoc}
//     * @param conceptScheme {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSCollection> getCollectionMembers(SKOSConceptScheme conceptScheme) {
//        List<SKOSCollection> collections = new ArrayList<SKOSCollection>();
//        Property inSchemeProp = ((JenaSKOS)(this.getSKOS())).getProperty(SKOSElementProperty.inScheme);
//        if (inSchemeProp!=null && conceptScheme!=null) {
//            JenaResourceIterator iter = this.getResourceIterOverRelationshipSources(SKOSElementProperty.member);
//            while (iter.hasNext()) {
//                Resource res = iter.next();
//                SKOSCollection coll = JenaSKOSResource.skosCollectionFromJenaResource(res);
//                if (coll!=null && coll.isInScheme(conceptScheme)) collections.add(coll);
//            }
//            iter.close();
//        }
//        return collections;
//    }
    
    
}
