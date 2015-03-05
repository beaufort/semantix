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

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import ie.cmrc.smtx.skos.model.SKOSCollection;
import ie.cmrc.smtx.skos.model.SKOSElementProperty;
import ie.cmrc.smtx.skos.model.SKOSResource;
import ie.cmrc.smtx.skos.model.SKOSCollectionMember;
import ie.cmrc.smtx.skos.jena.util.SKOSResourceIterFactory;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * A Jena-based implementation of {@link ie.cmrc.skos.core.SKOSCollectionMember}
 * @author Yassine Lassoued
 */
public abstract class JenaSKOSCollectionMember extends JenaSKOSResource implements SKOSCollectionMember {

    /**
     * Wraps the provided Jena {@code com.hp.hpl.jena.rdf.model.Resource} as a {@link ie.cmrc.skos.core.SKOSCollectionMember}
     * @param resource Jena {@code com.hp.hpl.jena.rdf.model.Resource} to wrap as a {@link ie.cmrc.skos.core.SKOSCollectionMember}
     */
    public JenaSKOSCollectionMember(Resource resource) {
        super(resource);
    }

    

    /**
     * {@inheritDoc}
     * @param collectionUri {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSCollectionMember addToCollection(String collectionUri) {
        if (collectionUri != null && !collectionUri.isEmpty()) {
            SKOSResource skosCollectionResource = this.getSKOS().getSKOSResource(collectionUri);
            if (skosCollectionResource != null) {
                skosCollectionResource.addRelation(SKOSElementProperty.member, this);
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param collection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSCollectionMember addToCollection(SKOSCollection collection) {
        if (collection != null) {
            collection.addRelation(SKOSElementProperty.member, this);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param collectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isMemberOfCollection(String collectionURI) {
        return this.isRelation(collectionURI, SKOSElementProperty.member);
    }

    /**
     * {@inheritDoc}
     * @param collectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isTransitiveMemberOfCollection(String collectionURI) {
        return this.isRelation(collectionURI, SKOSElementProperty.memberTransitive);
    }

    /**
     * {@inheritDoc}
     * @param collection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isMemberOfCollection(SKOSCollection collection) {
        return this.isRelation(collection, SKOSElementProperty.member);
    }

    /**
     * {@inheritDoc}
     * @param collection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isTransitiveMemberOfCollection(SKOSCollection collection) {
        return this.isRelation(collection, SKOSElementProperty.memberTransitive);
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSCollectionMember removeFromAllCollections() {
        return (SKOSCollectionMember)(this.removeAsRelation(SKOSElementProperty.member));
    }

    /**
     * {@inheritDoc}
     * @param collectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSCollectionMember removeFromCollection(String collectionURI) {
        if (collectionURI!=null && !collectionURI.isEmpty()) {
            SKOSResource skosCollectionResource = this.getSKOS().getSKOSResource(collectionURI);
            if (skosCollectionResource != null) {
                skosCollectionResource.removeRelation(SKOSElementProperty.member, this);
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param collection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSCollectionMember removeFromCollection(SKOSCollection collection) {
        return (SKOSCollectionMember)(this.removeAsRelation(collection, SKOSElementProperty.member));
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollections() {
        StmtIterator iter = this.resource.getModel().listStatements((Resource)null, PropertyFactory.member, this);
        return SKOSResourceIterFactory.makeSKOSCollectionIterOverSubjects(iter);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollectionsTransitive() {
        StmtIterator iter = this.resource.getModel().listStatements((Resource)null, PropertyFactory.memberTransitive, this);
        return SKOSResourceIterFactory.makeSKOSCollectionIterOverSubjects(iter);
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSCollection> getCollections() {
        List<SKOSCollection> collections = new ArrayList<>();

        CloseableIterator<SKOSCollection> iter = this.listCollections();
        while (iter.hasNext()) {
            SKOSCollection collection = iter.next();
            if (collection != null) collections.add(collection);
        }
        
        return collections;
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSCollection> getCollectionsTransitive() {
        List<SKOSCollection> collections = new ArrayList<>();

        CloseableIterator<SKOSCollection> iter = this.listCollectionsTransitive();
        while (iter.hasNext()) {
            SKOSCollection collection = iter.next();
            if (collection != null) collections.add(collection);
        }
        
        return collections;
    }
}
