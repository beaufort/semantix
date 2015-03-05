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

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.query.QueryHandler;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Alt;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelChangedListener;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.NsIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.RSIterator;
import com.hp.hpl.jena.rdf.model.ReifiedStatement;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceF;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.Seq;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.Command;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.ReificationStyle;
import com.hp.hpl.jena.vocabulary.RDF;
import ie.cmrc.smtx.jena.selector.FilteredSelector;
import ie.cmrc.smtx.jena.selector.filter.AndStmtFilter;
import ie.cmrc.smtx.jena.selector.filter.OrStmtFilter;
import ie.cmrc.smtx.jena.selector.filter.StmtFilter;
import ie.cmrc.smtx.jena.selector.filter.StmtObjectHasPropertyFilter;
import ie.cmrc.smtx.jena.selector.filter.StmtObjectIsPropertyOfFilter;
import ie.cmrc.smtx.jena.selector.filter.StmtSubjectHasPropertyFilter;
import ie.cmrc.smtx.jena.selector.filter.StmtSubjectIsPropertyOfFilter;
import ie.cmrc.smtx.skos.model.SKOS;
import ie.cmrc.smtx.skos.model.SKOSAnnotationProperty;
import ie.cmrc.smtx.skos.model.SKOSCollection;
import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSConceptScheme;
import ie.cmrc.smtx.skos.model.SKOSObjectProperty;
import ie.cmrc.smtx.skos.model.SKOSResource;
import ie.cmrc.smtx.skos.model.SKOSSemanticProperty;
import ie.cmrc.smtx.skos.model.SKOSType;
import ie.cmrc.smtx.skos.model.hierarchy.DefaultSKOSConceptNode;
import ie.cmrc.smtx.skos.model.hierarchy.HierarchyMethod;
import ie.cmrc.smtx.skos.model.hierarchy.SKOSConceptNode;
import ie.cmrc.smtx.skos.jena.util.SKOSResourceIterFactory;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import ie.cmrc.smtx.skos.model.util.EmptyCloseableIterator;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

/**
 * A {@code JenaSKOSThesaurus} is an Jena-based implementation of the
 * {@link ie.cmrc.skos.core.SKOS} interface. It wraps a Jena Model
 * ({@code com.hp.hpl.jena.rdf.model.Model}) as a {@link ie.cmrc.skos.core.SKOS}.
 * This {@code JenaSKOSThesaurus} only supports inferences if the underlying
 * Jena Model does so.<br/>
 * {@code JenaSKOSThesaurus} also implements the Jena Model interface
 * ({@code com.hp.hpl.jena.rdf.model.Model}).
 * 
 * 
 * @author Yassine Lassoued
 */
public class JenaSKOS implements SKOS, Model {
    
    /**
     * Underlying Jena Model ({@code com.hp.hpl.jena.rdf.model.Model}) for this
     * {@code JenaSKOSThesaurus}. If this supports inferences, then the
     * thesaurus will support inferences too.
     */
    protected final Model model;
    
    /**
     * Underlying Jena Dataset ({@code com.hp.hpl.jena.query.Dataset}) if any
     */
    protected final Dataset dataset;
    
    /**
     * Constructs a {@link JenaSKOS} that wraps the provided Jena Model
     * ({@code com.hp.hpl.jena.rdf.model.Model})
     * @param model Jena Model ({@code com.hp.hpl.jena.rdf.model.Model}) to wrap
     * as a {@link ie.cmrc.skos.core.SKOS}. If this Model supports inferences,
     * so will the SKOS thesaurus.
     * @throws IllegalArgumentException if {@code model==null}
     */
    public JenaSKOS(Model model) {
        this.model = model;
        if (this.model == null) throw new IllegalArgumentException("JenaSKOSThesaurus constructor: Model argument must not be null");
        this.dataset = null;
    }

    public JenaSKOS(Dataset dataset) {
        this.dataset = dataset;
        this.model = dataset.getDefaultModel();
        if (this.model == null) throw new IllegalArgumentException("JenaSKOSThesaurus constructor: Model argument must not be null");
    }
    
    /**
     * Underlying Jena model
     * @return 
     */
//    public Model getModel() {
//        return this.model;
//    }
    
    /**
     * Returns the underlying Jena Dataset if any
     * @return {@code com.hp.hpl.jena.query.Dataset}
     */
    public Dataset getDataset() {
        return this.dataset;
    }
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Implementing the SKOSThesaurus interface
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * {@inheritDoc}<br/>
     * This method is not supported yet. Please use {@linkplain #listConceptSchemes()},
     * {@linkplain #listCollections()}, and {@linkplain #listConcepts()} instead
     * for the time being.
     * 
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSResource> listSKOSResources() {
        // TODO Implement this
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSResource> getSKOSResources() {
        List<SKOSResource> skosResources = new ArrayList<>();
        
        if (this.model != null) {
            Property rdfTypeProp = RDF.type;
            
            StmtIterator iter = this.model.listStatements((Resource)null, rdfTypeProp, (RDFNode)null);
            
            while (iter.hasNext()) {
                Statement statement = iter.next();
                Resource resource = statement.getSubject();
                RDFNode object = statement.getObject();
                if (object.isResource()) {
                    String uri = ((Resource)object).getURI();
                    SKOSType skosType = SKOSType.fromString(uri);
                    SKOSResource skosResource;
                    switch (skosType) {
                        case Concept:
                            skosResource = new JenaSKOSConcept(resource);
                            break;
                        case Collection:
                            skosResource = new JenaSKOSCollection(resource);
                            break;
                        case ConceptScheme: 
                            skosResource = new JenaSKOSConceptScheme(resource);
                            break;
                        default: 
                            skosResource = null;
                            break;
                    }
                    if (skosResource!=null && !skosResources.contains(skosResource)) skosResources.add(skosResource);       
                }
            }
            iter.close();
        }
        
        return skosResources;
    }

    /**
     * {@inheritDoc}
     * @param resourceURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public JenaSKOSResource getSKOSResource(String resourceURI) {
        if (this.model!=null) {
            Resource resource = this.model.getResource(resourceURI);
            if (this.model.containsResource(resource) && this.getSKOSType(resource)!=null) return JenaSKOSResource.fromJenaResource(resource);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @param skosResource {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public JenaSKOSResource getSKOSResource(SKOSResource skosResource) {
        if (this.model!=null && skosResource!=null) {
            if (skosResource instanceof JenaSKOSResource && skosResource.getSKOS()==this) return (JenaSKOSResource)skosResource;
            else return this.getSKOSResource(skosResource.getURI());
        }
        return null;
    }
    
    
    
    
    /**
     * {@inheritDoc}
     * @param resourceURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSType getSKOSResourceType(String resourceURI) {
        if (this.model!=null) {
            Resource resource = this.model.getResource(resourceURI);
            if (this.model.containsResource(resource)) return this.getSKOSType(resource);
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     * @param resourceURI {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SKOS removeResource(String resourceURI) throws IllegalArgumentException {
        if (this.model!=null) {
            Resource resource = this.model.getResource(resourceURI);
            if (JenaSKOSResource.jenaResourceIsSKOSResource(resource)) {
                this.model.removeAll(resource, null, null);
                this.model.removeAll(null, null, resource);
            }
            else throw new IllegalArgumentException("Resource \""+resourceURI+"\" is not a SKOS resource");
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param resource {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOS removeResource(SKOSResource resource)  {
        if (resource != null) {
            try {
                return this.removeResource(resource.getURI());
            }
            catch (IllegalArgumentException e) {}
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConceptScheme> listConceptSchemes() {
        Property rdfTypeProp = RDF.type;
        Resource skosCSTypeRes = this.getSKOSTypeResource(SKOSType.ConceptScheme);
        final StmtIterator iter;

        if (skosCSTypeRes != null) {
            iter = this.model.listStatements((Resource)null, rdfTypeProp, skosCSTypeRes);
        }
        else iter = null;

        return SKOSResourceIterFactory.makeSKOSConceptSchemeIterOverSubjects(iter);
        
//        return (new CloseableIterator<SKOSConceptScheme>() {
//
//            boolean isOpen= true;
//
//            @Override
//            public boolean hasNext() {
//                return (isOpen && iter!=null && iter.hasNext());
//            }
//
//            @Override
//            public SKOSConceptScheme next() {
//                if (isOpen && iter!=null && iter.hasNext()) {
//
//                    Statement statement = iter.next();
//                    Resource resource = statement.getSubject();
//                    SKOSConceptScheme skosConceptScheme = new JenaSKOSConceptScheme(resource);
//
//                    if (!iter.hasNext()) {
//                        isOpen = false;
//                        iter.close();
//                    }
//                    return skosConceptScheme;
//
//                }
//                else return null;
//            }
//
//            @Override
//            public void remove() {
//                if (isOpen && iter!=null) iter.remove();
//            }
//
//            @Override
//            public void close() {
//                if (isOpen && iter!=null) iter.close();
//            }
//        });
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConceptScheme> getConceptSchemes() {
        List<SKOSConceptScheme> conceptSchemes = new ArrayList<>();
        
        if (this.model != null) {
            Property rdfTypeProp = RDF.type;
            Resource skosCSTypeRes = this.getSKOSTypeResource(SKOSType.ConceptScheme);
            if (skosCSTypeRes != null) {
                StmtIterator iter = this.model.listStatements((Resource)null, rdfTypeProp, skosCSTypeRes);

                while (iter.hasNext()) {
                    Statement statement = iter.next();
                    Resource resource = statement.getSubject();
                    SKOSConceptScheme skosConceptScheme = new JenaSKOSConceptScheme(resource);
                    if (!conceptSchemes.contains(skosConceptScheme)) conceptSchemes.add(skosConceptScheme);
                }
                iter.close();
            }
        }
        return conceptSchemes;
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConceptScheme getConceptScheme(String conceptSchemeURI) {
        if (this.model!=null) {
            Resource resource = this.model.getResource(conceptSchemeURI);
            return JenaSKOSResource.skosConcepSchemeFromJenaResource(resource);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SKOSConceptScheme createConceptScheme(String conceptSchemeURI) throws IllegalArgumentException {
        if (this.model != null) {
            return this.createSKOSResource(conceptSchemeURI, SKOSType.ConceptScheme).asSKOSConceptScheme();
        }
        else return null;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollections() {
        Property rdfTypeProp = RDF.type;
        Resource skosCSTypeRes = this.getSKOSTypeResource(SKOSType.Collection);
        final StmtIterator iter;

        if (skosCSTypeRes != null) {
            iter = this.model.listStatements((Resource)null, rdfTypeProp, skosCSTypeRes);
        }
        else iter = null;

        return SKOSResourceIterFactory.makeSKOSCollectionIterOverSubjects(iter);
        
//        return (new CloseableIterator<SKOSCollection>() {
//
//            boolean isOpen= true;
//
//            @Override
//            public boolean hasNext() {
//                return (isOpen && iter!=null && iter.hasNext());
//            }
//
//            @Override
//            public SKOSCollection next() {
//                if (isOpen && iter!=null && iter.hasNext()) {
//
//                    Statement statement = iter.next();
//                    Resource resource = statement.getSubject();
//                    SKOSCollection skosCollection = new JenaSKOSCollection(resource);
//
//                    if (!iter.hasNext()) {
//                        isOpen = false;
//                        iter.close();
//                    }
//                    return skosCollection;
//
//                }
//                else return null;
//            }
//
//            @Override
//            public void remove() {
//                if (isOpen && iter!=null) iter.remove();
//            }
//
//            @Override
//            public void close() {
//                if (isOpen && iter!=null) iter.close();
//            }
//        });
    }

    /**
     * {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollections(SKOSConceptScheme conceptScheme) {
        Resource csRes = this.getJenaResource(conceptScheme);
        if (conceptScheme==null || csRes!=null) {
            if (csRes == null) return this.listCollections();
            else {
                // Iterate through collections and filter by concept scheme
                StmtFilter filter = new StmtSubjectHasPropertyFilter(PropertyFactory.inScheme, csRes);

                Selector filteredSelector = new FilteredSelector((Resource)null, RDF.type, TypeResourceFactory.COLLECTION, filter);

                StmtIterator iter = this.model.listStatements(filteredSelector);
                return SKOSResourceIterFactory.makeSKOSCollectionIterOverSubjects(iter);
            }
        }
        else return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @param skosCollection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollections(SKOSCollection skosCollection) {
        Resource collRes = this.getJenaResource(skosCollection);
        if (skosCollection==null || collRes!=null) {
            if (collRes == null) return this.listCollections();
            else {
                // Iterate through collections and filter by parent collection
                StmtFilter filter = new StmtSubjectIsPropertyOfFilter(collRes, PropertyFactory.member);

                Selector filteredSelector = new FilteredSelector((Resource)null, RDF.type, TypeResourceFactory.COLLECTION, filter);

                StmtIterator iter = this.model.listStatements(filteredSelector);
                return SKOSResourceIterFactory.makeSKOSCollectionIterOverSubjects(iter);
            }
        }
        else return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @param skosCollection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSCollection> listCollections(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        Resource csRes = this.getSKOSResource(conceptScheme);
        if (conceptScheme==null || csRes!=null) {
            if (csRes != null) {
                Resource collRes = this.getSKOSResource(skosCollection);
                if (skosCollection==null || collRes!=null) {
                    if (collRes != null) {
                        StmtFilter csFilter = new StmtSubjectHasPropertyFilter(PropertyFactory.inScheme, csRes);
                        StmtFilter collFilter = new StmtSubjectIsPropertyOfFilter(collRes, PropertyFactory.member);
                        StmtFilter filter = new AndStmtFilter(collFilter);
                        ((AndStmtFilter)filter).add(csFilter);

                        Selector filteredSelector = new FilteredSelector((Resource)null, RDF.type, TypeResourceFactory.COLLECTION, filter);

                        StmtIterator iter = this.model.listStatements(filteredSelector);
                        return SKOSResourceIterFactory.makeSKOSCollectionIterOverSubjects(iter);
                    }
                    else return this.listCollections(conceptScheme);
                }
                else return new EmptyCloseableIterator<>();
            }
            else return this.listCollections(skosCollection);
        }
        else return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSCollection> getCollections() {
        List<SKOSCollection> collections = new ArrayList<>();
        
        if (this.model != null) {
            Property rdfTypeProp = RDF.type;
            Resource skosCollectionTypeRes = this.getSKOSTypeResource(SKOSType.Collection);
            if (skosCollectionTypeRes != null) {
                StmtIterator iter = this.model.listStatements((Resource)null, rdfTypeProp, skosCollectionTypeRes);

                while (iter.hasNext()) {
                    Statement statement = iter.next();
                    Resource resource = statement.getSubject();
                    SKOSCollection skosCollection = new JenaSKOSCollection(resource);
                    collections.add(skosCollection);
                }
                iter.close();
            }
        }
        return collections;
    }
    
    
    

    /**
     * {@inheritDoc}
     * @param collectionURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSCollection getCollection(String collectionURI) {
        if (this.model!=null) {
            Resource resource = this.model.getResource(collectionURI);
            SKOSCollection coll = JenaSKOSResource.skosCollectionFromJenaResource(resource);
            return JenaSKOSResource.skosCollectionFromJenaResource(resource);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @param collectionURI {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SKOSCollection createCollection(String collectionURI) throws IllegalArgumentException {
        if (this.model != null) {
            return this.createSKOSResource(collectionURI, SKOSType.Collection).asSKOSCollection();
        }
        else return null;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listConcepts() {
        Property rdfTypeProp = RDF.type;
        Resource skosCSTypeRes = this.getSKOSTypeResource(SKOSType.Concept);
        final StmtIterator iter;

        
        
        if (skosCSTypeRes != null) {
            iter = this.model.listStatements((Resource)null, rdfTypeProp, skosCSTypeRes);
        }
        else iter = null;

        return SKOSResourceIterFactory.makeSKOSConceptIterOverSubjects(iter);
        
//        return (new CloseableIterator<SKOSConcept>() {
//
//            boolean isOpen= true;
//
//            @Override
//            public boolean hasNext() {
//                return (isOpen && iter!=null && iter.hasNext());
//            }
//
//            @Override
//            public SKOSConcept next() {
//                if (isOpen && iter!=null && iter.hasNext()) {
//
//                    Statement statement = iter.next();
//                    Resource resource = statement.getSubject();
//                    SKOSConcept skosCollection = new JenaSKOSConcept(resource);
//
//                    if (!iter.hasNext()) {
//                        isOpen = false;
//                        iter.close();
//                    }
//                    return skosCollection;
//
//                }
//                else return null;
//            }
//
//            @Override
//            public void remove() {
//                if (isOpen && iter!=null) iter.remove();
//            }
//
//            @Override
//            public void close() {
//                if (isOpen && iter!=null) iter.close();
//            }
//        });
    }

    /**
     * {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listConcepts(SKOSConceptScheme conceptScheme) {
        
        Resource csRes = this.getJenaResource(conceptScheme);
        if (conceptScheme==null || csRes!=null) {
            if (csRes == null) return this.listConcepts();
            else {
                Property property = PropertyFactory.inScheme;
                Property filterProperty = RDF.type;
                Resource filterObject = TypeResourceFactory.CONCEPT;
                StmtFilter filter = new StmtSubjectHasPropertyFilter(filterProperty, filterObject);

                Selector filteredSelector = new FilteredSelector((Resource)null, property, csRes, filter);

                StmtIterator iter = this.model.listStatements(filteredSelector);
                return SKOSResourceIterFactory.makeSKOSConceptIterOverSubjects(iter);
            }
        }
        else return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @param skosCollection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listConcepts(SKOSCollection skosCollection) {
        Resource collRes = this.getJenaResource(skosCollection);
        if (skosCollection==null || collRes!=null) {
            if (collRes == null) return this.listConcepts();
            else {
                Property property = PropertyFactory.member;
                Property filterProperty = RDF.type;
                Resource filterObject = TypeResourceFactory.CONCEPT;
                StmtFilter filter = new StmtObjectHasPropertyFilter(filterProperty, filterObject);

                Selector filteredSelector = new FilteredSelector(collRes, property, (RDFNode)null, filter);

                StmtIterator iter = this.model.listStatements(filteredSelector);
                return SKOSResourceIterFactory.makeSKOSConceptIterOverObjects(iter);
            }
        }
        else return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @param skosCollection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listConcepts(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        Resource csRes = this.getSKOSResource(conceptScheme);
        if (conceptScheme==null || csRes!=null) {
            if (csRes != null) {
                Resource collRes = this.getSKOSResource(skosCollection);
                if (skosCollection==null || collRes!=null) {
                    if (collRes != null) {
                        StmtFilter typeFilter = new StmtSubjectHasPropertyFilter(RDF.type, TypeResourceFactory.CONCEPT);
                        StmtFilter collFilter = new StmtSubjectIsPropertyOfFilter(collRes, PropertyFactory.member);
                        StmtFilter filter = new AndStmtFilter(collFilter);
                        ((AndStmtFilter)filter).add(typeFilter);

                        Selector filteredSelector = new FilteredSelector((Resource)null, PropertyFactory.inScheme, csRes, filter);

                        StmtIterator iter = this.model.listStatements(filteredSelector);
                        
                        return SKOSResourceIterFactory.makeSKOSConceptIterOverSubjects(iter);
                    }
                    else return this.listConcepts(conceptScheme);
                }
                else return new EmptyCloseableIterator<>();
            }
            else return this.listConcepts(skosCollection);
        }
        else return new EmptyCloseableIterator<>();
    }

    /**
     * {@inheritDoc}
     * @param skosCollection {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listConcepts(SKOSCollection skosCollection, SKOSConceptScheme conceptScheme) {
        Resource collRes = this.getSKOSResource(skosCollection);
        if (skosCollection==null || collRes!=null) {
            if (collRes != null) {
                Resource csRes = this.getSKOSResource(conceptScheme);
                if (conceptScheme==null || csRes!=null) {
                    if (csRes != null) {
                        StmtFilter typeFilter = new StmtObjectHasPropertyFilter(RDF.type, TypeResourceFactory.CONCEPT);
                        StmtFilter csFilter = new StmtObjectHasPropertyFilter(PropertyFactory.inScheme, csRes);
                        StmtFilter filter = new AndStmtFilter(csFilter);
                        ((AndStmtFilter)filter).add(typeFilter);

                        Selector filteredSelector = new FilteredSelector(collRes, PropertyFactory.inScheme, (RDFNode)null, filter);

                        StmtIterator iter = this.model.listStatements(filteredSelector);
                        return SKOSResourceIterFactory.makeSKOSConceptIterOverObjects(iter);
                    }
                    else return this.listConcepts(skosCollection);
                }
                else return new EmptyCloseableIterator<>();
            }
            else return this.listConcepts(conceptScheme);
        }
        else return new EmptyCloseableIterator<>();
    }
    
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getConcepts() {
        List<SKOSConcept> concepts = new ArrayList<>();
        
        if (this.model != null) {
            Property rdfTypeProp = RDF.type;
            Resource skosConceptTypeRes = this.getSKOSTypeResource(SKOSType.Concept);
            if (skosConceptTypeRes != null) {
                StmtIterator iter = this.model.listStatements((Resource)null, rdfTypeProp, skosConceptTypeRes);

                while (iter.hasNext()) {
                    Statement statement = iter.next();
                    Resource resource = statement.getSubject();
                    SKOSConcept skosConcept = new JenaSKOSConcept(resource);
                    concepts.add(skosConcept);
                }
                iter.close();
            }
        }
        return concepts;
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConcept getConcept(String conceptURI) {
        if (this.model!=null) {
            Resource resource = this.model.getResource(conceptURI);
            return JenaSKOSResource.skosConceptFromJenaResource(resource);
        }
        else return null;
    }

    /**
     * {@inheritDoc}
     * @param conceptURI {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SKOSConcept createConcept(String conceptURI) throws IllegalArgumentException {
        if (this.model != null) {
            return this.createSKOSResource(conceptURI, SKOSType.Concept).asSKOSConcept();
        }
        else return null;
    }
    
    // -------- -------- -------- --------
    // Advanced querying
    
    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listSemanticRelations(SKOSConcept concept, SKOSSemanticProperty relationshipType) {
        StmtIterator iter;
        if (concept!=null && relationshipType!=null) {

            Property property = this.getProperty(relationshipType);
            
            if (property!=null) {
                Resource conceptRes = this.getJenaResource(concept);
                iter = conceptRes.listProperties(property);
            }
            else iter = null;
        }
        else iter = null;
        
        return SKOSResourceIterFactory.makeSKOSConceptIterOverObjects(iter);
    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listSemanticRelations(SKOSConcept concept, SKOSSemanticProperty relationshipType, SKOSConceptScheme conceptScheme) {
        return this.listSemanticRelations(concept, relationshipType, conceptScheme, null);
    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @param skosCollection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listSemanticRelations(SKOSConcept concept, SKOSSemanticProperty relationshipType, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        Resource conceptRes = this.getJenaResource(concept);
        StmtIterator stmtIter = this.getRelationIter(conceptRes, relationshipType, conceptScheme, skosCollection);
        return SKOSResourceIterFactory.makeSKOSConceptIterOverObjects(stmtIter);
    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @param skosCollections {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listSemanticRelations(SKOSConcept concept, SKOSSemanticProperty relationshipType, Collection<SKOSConceptScheme> conceptSchemes, Collection<SKOSCollection> skosCollections) {
        Resource conceptRes = this.getJenaResource(concept);
        StmtIterator stmtIter = this.getRelationIter(conceptRes, relationshipType, conceptSchemes, skosCollections);
        return SKOSResourceIterFactory.makeSKOSConceptIterOverObjects(stmtIter);
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param targetResource {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType, SKOSResource targetResource) {
        StmtIterator iter;
        if (targetResource!=null && relationshipType!=null) {

            Property property = this.getProperty(relationshipType);
            
            if (property!=null) {
                Resource conceptRes = this.getJenaResource(targetResource);
                iter = this.listStatements((Resource)null, property, conceptRes);
            }
            else iter = null;
        }
        else iter = null;
        
        return SKOSResourceIterFactory.makeSKOSConceptIterOverSubjects(iter);
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param targetConcept {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType, SKOSConcept targetConcept, SKOSConceptScheme conceptScheme) {
        return this.listSemanticRelationshipSources(relationshipType, targetConcept, conceptScheme, null);
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param targetConcept {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @param skosCollection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType, SKOSConcept targetConcept, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        Resource conceptRes = this.getJenaResource(targetConcept);
        StmtIterator stmtIter = this.getRelationshipSourceIter(conceptRes, relationshipType, conceptScheme, skosCollection);
        return SKOSResourceIterFactory.makeSKOSConceptIterOverSubjects(stmtIter);
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param targetConcept {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType, SKOSConcept targetConcept, Collection<SKOSConceptScheme> conceptSchemes) {
        return this.listSemanticRelationshipSources(relationshipType, targetConcept, conceptSchemes, null);
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param targetConcept {@inheritDoc}
     * @param conceptSchemes {@inheritDoc}
     * @param skosCollections {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType, SKOSConcept targetConcept, Collection<SKOSConceptScheme> conceptSchemes, Collection<SKOSCollection> skosCollections) {
        Resource conceptRes = this.getJenaResource(targetConcept);
        StmtIterator stmtIter = this.getRelationshipSourceIter(conceptRes, relationshipType, conceptSchemes, skosCollections);
        return SKOSResourceIterFactory.makeSKOSConceptIterOverSubjects(stmtIter);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listTopConcepts() {
        StmtIterator iter = this.listStatements((Resource)null, PropertyFactory.topConceptOf, (RDFNode)null);
        return SKOSResourceIterFactory.makeSKOSConceptIterOverSubjects(iter);
    }

    /**
     * {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listTopConcepts(SKOSConceptScheme conceptScheme) {
        Resource csRes = this.getJenaResource(conceptScheme);
        StmtIterator iter = this.listStatements((Resource)null, PropertyFactory.topConceptOf, csRes);
        return SKOSResourceIterFactory.makeSKOSConceptIterOverSubjects(iter);
    }

    /**
     * {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @param skosCollection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listTopConcepts(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        Resource csRes = this.getJenaResource(conceptScheme);
        Resource collRes = this.getJenaResource(skosCollection);
        
        StmtFilter filter = null;
        if (collRes != null) {
            filter = new StmtSubjectIsPropertyOfFilter(collRes, PropertyFactory.member);
        }
        Selector selector  = new SimpleSelector((Resource)null, PropertyFactory.topConceptOf, csRes);
        if (filter != null) selector = new FilteredSelector(selector, filter);
        
        StmtIterator iter = this.listStatements(selector);
        
        return SKOSResourceIterFactory.makeSKOSConceptIterOverSubjects(iter);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getBroadestConcepts() {
        List<SKOSConcept> broadest = new ArrayList<>();
        
        CloseableIterator<SKOSConcept> iter = this.listConcepts();
        while(iter.hasNext()) {
            SKOSConcept concept = iter.next();
            if (concept != null) {
                if (!this.conceptHasBroaderConcepts((JenaSKOSConcept)concept, null, null)) broadest.add(concept);
            }
        }
        iter.close();
        return broadest;
    }

    /**
     * {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getBroadestConcepts(SKOSConceptScheme conceptScheme) {
        List<SKOSConcept> broadest = new ArrayList<>();
        Resource jcs = this.getJenaResource(conceptScheme);
        if (conceptScheme==null || jcs!=null) {
            CloseableIterator<SKOSConcept> iter = this.listConcepts(conceptScheme);
            while(iter.hasNext()) {
                SKOSConcept concept = iter.next();
                if (concept != null) {
                    if (!this.conceptHasBroaderConcepts((JenaSKOSConcept)concept, jcs, null)) broadest.add(concept);
                }
            }
            iter.close();
        }
        return broadest;
    }

    /**
     * {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @param skosCollection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getBroadestConcepts(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        List<SKOSConcept> broadest = new ArrayList<>();
        Resource jcs = this.getJenaResource(conceptScheme);
        Resource jcoll = this.getJenaResource(skosCollection);
        if ((conceptScheme==null || jcs!=null) && (skosCollection==null || jcoll!=null)) {
            CloseableIterator<SKOSConcept> iter = this.listConcepts(conceptScheme, skosCollection);
            while(iter.hasNext()) {
                SKOSConcept concept = iter.next();
                if (concept != null) {
                    if (!this.conceptHasBroaderConcepts((JenaSKOSConcept)concept, jcs, jcoll)) broadest.add(concept);
                }
            }
            iter.close();
        }
        return broadest;
    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectNarrowerConcepts(SKOSConcept concept) {
        return this.getDirectNarrowerConcepts(concept, null, null);
    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectNarrowerConcepts(SKOSConcept concept, SKOSConceptScheme conceptScheme) {
        return this.getDirectNarrowerConcepts(concept, conceptScheme, null);
    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @param skosCollection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectNarrowerConcepts(SKOSConcept concept, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        Resource conceptRes = this.getJenaResource(concept);
        if (conceptRes!=null) {

            Resource csRes = this.getJenaResource(conceptScheme);

            Resource collectionRes = this.getJenaResource(skosCollection);

            return getDirectNarrowerConcepts(conceptRes, csRes, collectionRes);
        }
        else return new ArrayList<>(0);
    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectBroaderConcepts(SKOSConcept concept) {
        return this.getDirectBroaderConcepts(concept, null, null);
    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectBroaderConcepts(SKOSConcept concept, SKOSConceptScheme conceptScheme) {
        return this.getDirectBroaderConcepts(concept, conceptScheme, null);
    }

    /**
     * {@inheritDoc}
     * @param concept {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @param skosCollection {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConcept> getDirectBroaderConcepts(SKOSConcept concept, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        Resource conceptRes = this.getJenaResource(concept);
        if (conceptRes!=null) {

            Resource csRes = this.getJenaResource(conceptScheme);

            Resource collectionRes = this.getJenaResource(skosCollection);

            return getDirectBroaderConcepts(conceptRes, csRes, collectionRes);
        }
        else return new ArrayList<>(0);
    }

    @Override
    public List<SKOSConceptNode> getConceptHierarchy() {
        return this.getConceptHierarchy(null, null, HierarchyMethod.DEFAULT_METHOD);
    }

    @Override
    public List<SKOSConceptNode> getConceptHierarchy(HierarchyMethod hierarchyMethod) {
        return this.getConceptHierarchy(null, null, hierarchyMethod);
    }
    
    @Override
    public List<SKOSConceptNode> getConceptHierarchy(SKOSConceptScheme conceptScheme) {
        return this.getConceptHierarchy(conceptScheme, null, HierarchyMethod.DEFAULT_METHOD);
    }

    @Override
    public List<SKOSConceptNode> getConceptHierarchy(SKOSConceptScheme conceptScheme, HierarchyMethod hierarchyMethod) {
        return this.getConceptHierarchy(conceptScheme, null, hierarchyMethod);
    }

    @Override
    public List<SKOSConceptNode> getConceptHierarchy(SKOSCollection skosCollection) {
        return this.getConceptHierarchy(null, skosCollection, HierarchyMethod.DEFAULT_METHOD);
    }

    @Override
    public List<SKOSConceptNode> getConceptHierarchy(SKOSCollection skosCollection, HierarchyMethod hierarchyMethod) {
        return this.getConceptHierarchy(null, skosCollection, hierarchyMethod);
    }

    @Override
    public List<SKOSConceptNode> getConceptHierarchy(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        return this.getConceptHierarchy(conceptScheme, skosCollection, HierarchyMethod.DEFAULT_METHOD);
    }

    @Override
    public List<SKOSConceptNode> getConceptHierarchy(SKOSConceptScheme conceptScheme, SKOSCollection skosCollection, HierarchyMethod hierarchyMethod) {
        // Make sure that the hierarchy method is not null
        HierarchyMethod hm = hierarchyMethod;
        if (hm == null) hm = HierarchyMethod.DEFAULT_METHOD;
        
        JenaSKOSConceptScheme jConceptScheme = null;
        JenaSKOSCollection jCollection = null;
        
        JenaSKOSResource csRes = this.getSKOSResource(conceptScheme);
        if (csRes!=null && (csRes instanceof JenaSKOSConceptScheme)) jConceptScheme = (JenaSKOSConceptScheme)csRes;
        JenaSKOSResource collRes = this.getSKOSResource(skosCollection);
        if (collRes!=null && (collRes instanceof JenaSKOSCollection)) jCollection = (JenaSKOSCollection)collRes;
        
        List<SKOSConceptNode> conceptHierarchy = new ArrayList<>();
        
        //Make sure that the matched Jena SKOS resources are correct
        if ((conceptScheme==null || jConceptScheme!=null) && (skosCollection==null || jCollection!=null)) {
            
            TreeSet<JenaSKOSConcept> rootConcepts = new TreeSet<>();
            
            CloseableIterator<SKOSConcept> topConcepts;
            List<SKOSConcept> broadestConcepts;
            switch (hm.getRootType()) {
                case BOTH:
                    topConcepts = this.listTopConcepts(jConceptScheme, jCollection);
                    while (topConcepts.hasNext()) {
                        SKOSConcept topConcept = topConcepts.next();
                        if (topConcept != null) rootConcepts.add((JenaSKOSConcept) topConcept);
                    }
                case BROADEST_CONCEPTS:
                    broadestConcepts = this.getBroadestConcepts(jConceptScheme, jCollection);
                    for (SKOSConcept broadConcept: broadestConcepts) rootConcepts.add((JenaSKOSConcept) broadConcept);
                    break;
                default:
                    topConcepts = this.listTopConcepts(jConceptScheme, jCollection);
                    while (topConcepts.hasNext()) {
                        SKOSConcept topConcept = topConcepts.next();
                        if (topConcept != null) rootConcepts.add((JenaSKOSConcept) topConcept);
                    }
                    break;
            }
            
            for (JenaSKOSConcept concept:rootConcepts) {
                conceptHierarchy.add(this.getHierarchyOfConcept(concept, jConceptScheme, jCollection, hm.getRelationshipType(), hm.getSortLanguage()));
            }
        }
        
        return conceptHierarchy;
    }
    
    @Override
    public SKOSConceptNode getConceptTree(SKOSConcept rootConcept) {
        return this.getConceptTree(rootConcept, null, null, HierarchyMethod.DEFAULT_METHOD);
    }

    @Override
    public SKOSConceptNode getConceptTree(SKOSConcept rootConcept, HierarchyMethod hierarchyMethod) {
        return this.getConceptTree(rootConcept, null, null, hierarchyMethod);
    }

    @Override
    public SKOSConceptNode getConceptTree(SKOSConcept rootConcept, SKOSConceptScheme conceptScheme) {
        return this.getConceptTree(rootConcept, conceptScheme, null, HierarchyMethod.DEFAULT_METHOD);
    }

    @Override
    public SKOSConceptNode getConceptTree(SKOSConcept rootConcept, SKOSConceptScheme conceptScheme, HierarchyMethod hierarchyMethod) {
        return this.getConceptTree(rootConcept, conceptScheme, null, hierarchyMethod);
    }

    @Override
    public SKOSConceptNode getConceptTree(SKOSConcept rootConcept, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        return this.getConceptTree(rootConcept, conceptScheme, skosCollection, HierarchyMethod.DEFAULT_METHOD);
    }
    
    @Override
    public SKOSConceptNode getConceptTree(SKOSConcept rootConcept, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection, HierarchyMethod hierarchyMethod) {
        // Make sure that the hierarchy method is not null
        HierarchyMethod hm = hierarchyMethod;
        if (hm == null) hm = HierarchyMethod.DEFAULT_METHOD;
        
        JenaSKOSConcept jRootConcept = null;
        
        JenaSKOSResource conceptRes = this.getSKOSResource(rootConcept);
        if (conceptRes!=null && (conceptRes instanceof JenaSKOSConcept)) jRootConcept = (JenaSKOSConcept)conceptRes;
        
        
        SKOSConceptNode conceptTree = null;
        
        //Make sure that the matched Jena SKOS resources are correct
        if (jRootConcept!=null) {
            JenaSKOSConceptScheme jConceptScheme = null;
            JenaSKOSCollection jCollection = null;
            JenaSKOSResource csRes = this.getSKOSResource(conceptScheme);
            if (csRes!=null && (csRes instanceof JenaSKOSConceptScheme)) jConceptScheme = (JenaSKOSConceptScheme)csRes;
            JenaSKOSResource collRes = this.getSKOSResource(skosCollection);
            if (collRes!=null && (collRes instanceof JenaSKOSCollection)) jCollection = (JenaSKOSCollection)collRes;
            
            if ((conceptScheme==null || jConceptScheme!=null) && (skosCollection==null || jCollection!=null)) {
                conceptTree = this.getHierarchyOfConcept(jRootConcept, jConceptScheme, jCollection, hm.getRelationshipType(), hm.getSortLanguage());
            }
            else conceptTree = new DefaultSKOSConceptNode(jRootConcept);
        }
        
        return conceptTree;
    }
    
    
    // -------- -------- -------- --------
    // Managing connection
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOS sync() {
        return this;
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public JenaSKOS commit() {
        model.commit();
        return this;
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isClosed() {
        return model.isClosed();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        model.close();
        if (dataset != null) {
            dataset.close();
        }
    }
    
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Methods specific to JenaSKOSThesaurus
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    
    /**
     * Returns the SKOS type of the provided Jena resource
     * @param resource Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * @return ({@link ie.cmrc.skos.core.SKOSType}) of {@code resource} is any, otherwise
     * {@code null}
     */
    private SKOSType getSKOSType(Resource resource) {
        if (this.model != null) {
            Property rdfTypeProp = RDF.type;
            
            StmtIterator iter = this.model.listStatements(resource, rdfTypeProp, (RDFNode)null);
            
            while (iter.hasNext()) {
                Statement statement = iter.next();
                Resource typeResource = statement.getSubject();
                RDFNode object = statement.getObject();
                if (object.isResource()) {
                    String uri = ((Resource)object).getURI();
                    SKOSType skosType = SKOSType.fromString(uri);
                    if (skosType != null) {
                        iter.close();
                        return skosType;
                    }      
                }
            }
            iter.close();
        }
        return null;
    }
    
    /**
     * Creates a SKOSResource with the provided URI and {@link ie.cmrc.skos.core.SKOSType}
     * @param resourceURI URI of the SKOS resource
     * @param type Type of the SKSO resource to create
     * @return SKOSResource with the provided URI and {@link ie.cmrc.skos.core.SKOSType}
     * @throws IllegalArgumentException if the provide URI is not that of a resource of
     * the provided type
     */
    private SKOSResource createSKOSResource(String resourceURI, SKOSType type) throws IllegalArgumentException {
        if (this.model != null) {
            Resource resource = this.model.getResource(resourceURI);
            Resource createdResource = null;
            if (this.model.containsResource(resource)) {
                // A resource alredy exists with the same URI
                SKOSType resourceSKOSType = this.getSKOSType(resource);
                if (resourceSKOSType==null || resourceSKOSType==type) {
                    // OK, no conflict with existing resource type
                    Property rdfTypeProp = RDF.type;
                    Resource typeRes = this.model.getResource(type.uri());
                    if (typeRes==null) typeRes = this.model.createResource(type.uri());
                    
                    this.model.add(resource, rdfTypeProp, typeRes);
                    
                    createdResource = resource;
                }
                else {
                    // Conflict with existing resource SKOS type
                    throw new IllegalArgumentException("Resource \""+resourceURI+"\" exists already and has type "+resourceSKOSType.name());
                }
            }
            else {
                // OK, no resource exists already with the same URI
                Property rdfTypeProp = RDF.type;
                Resource typeRes = this.model.getResource(type.uri());
                if (typeRes==null) typeRes = this.model.createResource(type.uri());
                createdResource = this.model.createResource(resourceURI, typeRes);
            }
            if (createdResource != null) {
                switch (type) {
                    case Concept: return new JenaSKOSConcept(createdResource);
                    case Collection: return new JenaSKOSCollection(createdResource);
                    case ConceptScheme: return new JenaSKOSConceptScheme(createdResource);
                    default: break;
                }
            }
        }
        return null;
    }
    
    /**
     * Returns the Jena resource matching the provided SKOS resource
     * @param skosResource SKOS resource
     * @return Jena Resource matching {@code skosResource}
     */
    protected Resource getJenaResource(SKOSResource skosResource) {
        if (this.model!=null && skosResource!=null) {
            if (skosResource instanceof JenaSKOSResource && skosResource.getSKOS()==this) return (JenaSKOSResource)skosResource;
            else return this.model.getResource(skosResource.getURI());
        }
        return null;
    }
    
    /**
     * Returns a statement iterator for the provided relationship constraint and
     * concept and collection filters
     * @param resource Subject of the relationship constraint
     * @param skosObjectProperty constraint relationship 
     * @param conceptScheme SKOS concept scheme to filter result by
     * @param skosCollection SKOS collection to filter result by
     * @return StmtIterator for the provided constraints
     */
    protected StmtIterator getRelationIter(Resource resource, SKOSObjectProperty skosObjectProperty, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        if (resource != null) {
            if (skosObjectProperty != null) {
                Property property = PropertyFactory.create(skosObjectProperty);
                StmtFilter csFilter = null;
                if (conceptScheme!=null) {
                    Resource csRes = this.getJenaResource(conceptScheme);
                    if (csRes != null) {
                        Property inSchemeProp = PropertyFactory.inScheme;
                        csFilter = new StmtObjectHasPropertyFilter(inSchemeProp, csRes);
                    }
                    else return null;
                }
                StmtFilter collFilter = null;
                if (skosCollection!=null) {
                    Resource collRes = this.getJenaResource(skosCollection);
                    if (collRes != null) {
                        collFilter = new StmtObjectIsPropertyOfFilter(collRes, PropertyFactory.member);
                    }
                    else return null;
                }

                StmtFilter stmtFilter = null;

                if (csFilter != null) {
                    if (collFilter != null) {
                        stmtFilter = new AndStmtFilter(csFilter);
                        ((AndStmtFilter)stmtFilter).add(collFilter);
                    }
                    else {
                        stmtFilter = csFilter;
                    }
                }
                else {
                    if (collFilter != null) {
                        stmtFilter = collFilter;
                    }
                }

                Selector selector;
                if (stmtFilter != null) selector = new FilteredSelector(resource, property, (RDFNode)null, stmtFilter);
                else selector = new SimpleSelector(resource, property, (RDFNode)null);

                return this.model.listStatements(selector);
            }
            else throw new IllegalArgumentException("SKOSObjectProperty must not be null!");
        }
        else return null;
    }
    
    /**
     * Returns a statement iterator for the provided relationship constraint and
     * concept and collection filters
     * @param resource Subject of the relationship constraint
     * @param skosObjectProperty constraint relationship 
     * @param conceptSchemes SKOS concept schemes to filter result by
     * @param skosCollections SKOS collections to filter result by
     * @return StmtIterator for the provided constraints
     */
    protected StmtIterator getRelationIter(Resource resource, SKOSObjectProperty skosObjectProperty, Collection<SKOSConceptScheme> conceptSchemes, Collection<SKOSCollection> skosCollections) {
        if (resource != null) {
            if (skosObjectProperty != null) {
                Property property = PropertyFactory.create(skosObjectProperty);
                StmtFilter csFilter = null;
                if (conceptSchemes!=null && !conceptSchemes.isEmpty()) {
                    csFilter = new OrStmtFilter();
                    for (SKOSConceptScheme conceptScheme: conceptSchemes) {
                        Resource csRes = this.getJenaResource(conceptScheme);
                        if (csRes != null) {
                            Property inSchemeProp = PropertyFactory.inScheme;
                            ((OrStmtFilter)csFilter).add(new StmtObjectHasPropertyFilter(inSchemeProp, csRes));
                        }
                        else return null;
                    }
                }
                StmtFilter collFilter = null;
                if (skosCollections!=null && !skosCollections.isEmpty()) {
                    collFilter = new OrStmtFilter();
                    for (SKOSCollection collection: skosCollections) {
                        Resource collRes = this.getJenaResource(collection);
                        if (collRes != null) {
                            ((OrStmtFilter)collFilter).add(new StmtObjectIsPropertyOfFilter(collRes, PropertyFactory.member));
                        }
                        else return null;
                    }
                }

                StmtFilter stmtFilter = null;

                if (csFilter != null) {
                    if (collFilter != null) {
                        stmtFilter = new AndStmtFilter(csFilter);
                        ((AndStmtFilter)stmtFilter).add(collFilter);
                    }
                    else {
                        stmtFilter = csFilter;
                    }
                }
                else {
                    if (collFilter != null) {
                        stmtFilter = collFilter;
                    }
                }

                Selector selector;
                if (stmtFilter != null) selector = new FilteredSelector(resource, property, (RDFNode)null, stmtFilter);
                else selector = new SimpleSelector(resource, property, (RDFNode)null);

                return this.model.listStatements(selector);
            }
            else throw new IllegalArgumentException("SKOSObjectProperty must not be null!");
        }
        else return null;
    }
    
    /**
     * Returns a statement iterator for the provided relationship constraint and
     * concept and collection filters
     * @param resource Object of the relationship constraint
     * @param skosObjectProperty constraint relationship 
     * @param conceptScheme SKOS concept scheme to filter result by
     * @param skosCollection SKOS collection to filter result by
     * @return StmtIterator for the provided constraints
     */
    protected StmtIterator getRelationshipSourceIter(Resource resource, SKOSObjectProperty skosObjectProperty, SKOSConceptScheme conceptScheme, SKOSCollection skosCollection) {
        if (resource != null) {
            if (skosObjectProperty != null) {
                Property property = PropertyFactory.create(skosObjectProperty);
                StmtFilter csFilter = null;
                if (conceptScheme!=null) {
                    Resource csRes = this.getJenaResource(conceptScheme);
                    if (csRes != null) {
                        Property inSchemeProp = PropertyFactory.inScheme;
                        csFilter = new StmtSubjectHasPropertyFilter(inSchemeProp, csRes);
                    }
                    else return null;
                }
                StmtFilter collFilter = null;
                if (skosCollection!=null) {
                    Resource collRes = this.getJenaResource(skosCollection);
                    if (collRes != null) {
                        collFilter = new StmtSubjectIsPropertyOfFilter(collRes, PropertyFactory.member);
                    }
                    else return null;
                }

                StmtFilter stmtFilter = null;

                if (csFilter != null) {
                    if (collFilter != null) {
                        stmtFilter = new AndStmtFilter(csFilter);
                        ((AndStmtFilter)stmtFilter).add(collFilter);
                    }
                    else {
                        stmtFilter = csFilter;
                    }
                }
                else {
                    if (collFilter != null) {
                        stmtFilter = collFilter;
                    }
                }

                Selector selector;
                if (stmtFilter != null) selector = new FilteredSelector((Resource)null, property, resource, stmtFilter);
                else selector = new SimpleSelector(resource, property, (RDFNode)null);

                return this.model.listStatements(selector);
            }
            else throw new IllegalArgumentException("SKOSObjectProperty must not be null!");
        }
        else return null;
    }
    
    /**
     * Returns a statement iterator for the provided relationship constraint and
     * concept and collection filters
     * @param resource Object of the relationship constraint
     * @param skosObjectProperty constraint relationship 
     * @param conceptSchemes SKOS concept schemes to filter result by
     * @param skosCollections SKOS collections to filter result by
     * @return StmtIterator for the provided constraints
     */
    protected StmtIterator getRelationshipSourceIter(Resource resource, SKOSObjectProperty skosObjectProperty, Collection<SKOSConceptScheme> conceptSchemes, Collection<SKOSCollection> skosCollections) {
        if (resource != null) {
            if (skosObjectProperty != null) {
                Property property = PropertyFactory.create(skosObjectProperty);
                StmtFilter csFilter = null;
                if (conceptSchemes!=null && !conceptSchemes.isEmpty()) {
                    csFilter = new OrStmtFilter();
                    for (SKOSConceptScheme conceptScheme: conceptSchemes) {
                        Resource csRes = this.getJenaResource(conceptScheme);
                        if (csRes != null) {
                            Property inSchemeProp = PropertyFactory.inScheme;
                            ((OrStmtFilter)csFilter).add(new StmtSubjectHasPropertyFilter(inSchemeProp, csRes));
                        }
                        else return null;
                    }
                }
                StmtFilter collFilter = null;
                if (skosCollections!=null && !skosCollections.isEmpty()) {
                    collFilter = new OrStmtFilter();
                    for (SKOSCollection collection: skosCollections) {
                        Resource collRes = this.getJenaResource(collection);
                        if (collRes != null) {
                            ((OrStmtFilter)collFilter).add(new StmtSubjectIsPropertyOfFilter(collRes, PropertyFactory.member));
                        }
                        else return null;
                    }
                }

                StmtFilter stmtFilter = null;

                if (csFilter != null) {
                    if (collFilter != null) {
                        stmtFilter = new AndStmtFilter(csFilter);
                        ((AndStmtFilter)stmtFilter).add(collFilter);
                    }
                    else {
                        stmtFilter = csFilter;
                    }
                }
                else {
                    if (collFilter != null) {
                        stmtFilter = collFilter;
                    }
                }

                Selector selector;
                if (stmtFilter != null) selector = new FilteredSelector((Resource)null, property, resource, stmtFilter);
                else selector = new SimpleSelector(resource, property, (RDFNode)null);

                return this.model.listStatements(selector);
            }
            else throw new IllegalArgumentException("SKOSObjectProperty must not be null!");
        }
        else return null;
    }
    
    /**
     * Checks whether the provided concept has broader collections in the provided
 concept scheme and collection
     * @param conceptResource Concept resource
     * @param targetCSResource Target concept scheme resource
     * @param targetCollectionResource Target collection resource
     * @return {@code true} if {@code conceptResource} has broader collections
 belonging to both
 {@code targetCSResource} and {@code targetCollectionResource}
     */
    protected boolean conceptHasBroaderConcepts(Resource conceptResource, Resource targetCSResource, Resource targetCollectionResource) {
        Property relationProp = PropertyFactory.broaderTransitive;

        for (StmtIterator iter = this.model.listStatements( (Resource) conceptResource, relationProp, (RDFNode) null); iter.hasNext(); ) {
            Statement stmt = iter.nextStatement();
            Resource broaderConceptRes = (Resource) stmt.getObject();
            if (!broaderConceptRes.getURI().equals(conceptResource.getURI())) {
                if ((targetCSResource==null || this.resourceIsInScheme(broaderConceptRes, targetCSResource))&&(targetCollectionResource==null || this.resourceIsInCollection(broaderConceptRes, targetCollectionResource))) return true;
            }
        }
        
        return false;
    }

    /**
     * Checks whether the provided concept has narrower collections in the provided
 concept scheme and collection
     * @param conceptResource Concept resource
     * @param targetCSResource Target concept scheme resource
     * @param targetCollectionResource Target collection resource
     * @return {@code true} if {@code conceptResource} has narrower collections
 belonging to both
 {@code targetCSResource} and {@code targetCollectionResource}
     */
    protected boolean conceptHasNarrowerConcepts(Resource conceptResource, Resource targetCSResource, Resource targetCollectionResource) {
        Property relationProp = PropertyFactory.narrowerTransitive;

        for (StmtIterator iter = this.model.listStatements( (Resource) conceptResource, relationProp, (RDFNode) null); iter.hasNext(); ) {
            Statement stmt = iter.nextStatement();
            Resource narrowerConceptRes = (Resource) stmt.getObject();
            if (!narrowerConceptRes.getURI().equals(conceptResource.getURI())) {
                if ((targetCSResource==null || this.resourceIsInScheme(narrowerConceptRes, targetCSResource))&&(targetCollectionResource==null || this.resourceIsInCollection(narrowerConceptRes, targetCollectionResource))) return true;
            }
        }

        return false;
    }
    
    /**
     * Checks whether a resource belongs to a concept scheme
     * @param resource Resource to check
     * @param conceptSchemeResource Concept scheme resource
     * @return {@code true} if {@code resource} belongs to
     * {@code conceptSchemeResource}; false otherwise
     */
    protected boolean resourceIsInScheme(Resource resource, Resource conceptSchemeResource) {
        if (resource==null || conceptSchemeResource==null) return false;
        else {
            Property inScheme = PropertyFactory.inScheme;
            return resource.hasProperty(inScheme, conceptSchemeResource);
        }
    }
    
    /**
     * Checks whether a resource belongs to a collection
     * @param resource Resource to check
     * @param skosCollectionResource Concept scheme resource
     * @return {@code true} if {@code resource} belongs to
     * {@code skosCollectionResource}; false otherwise
     */
    protected boolean resourceIsInCollection(Resource resource, Resource skosCollectionResource) {
        if (resource==null || skosCollectionResource==null) return false;
        else {
            Property member = PropertyFactory.member;
            return skosCollectionResource.hasProperty(member, resource);
        }
    }
    
    
    protected List<SKOSConcept> getDirectNarrowerConcepts(Resource conceptRes, Resource targetConceptSchemeRes, Resource targetCollectionRes) {

            Property narrower = PropertyFactory.narrowerTransitive;

            List<SKOSConcept> directNarrower = new ArrayList<>();

            List<Resource> directNarrowerResources = new ArrayList<>();

            for (StmtIterator iter = this.model.listStatements( conceptRes, narrower, (RDFNode) null); iter.hasNext(); ) {
                Statement stmt = iter.nextStatement();
                RDFNode r = stmt.getObject();
                if (r.isResource()) {
                    Resource relatedConceptRes = r.asResource();
                    if (!relatedConceptRes.getURI().equals(conceptRes.getURI())) {
                        if (!this.model.contains(relatedConceptRes, narrower, conceptRes)) {
                            if ((targetConceptSchemeRes==null || this.resourceIsInScheme(relatedConceptRes, targetConceptSchemeRes))&&(targetCollectionRes==null || this.resourceIsInCollection(relatedConceptRes, targetCollectionRes))) {
                                directNarrowerResources.add(relatedConceptRes);
                            }
                        }
                    }
                }
            }

            List<Resource> backup = new ArrayList<>(directNarrowerResources);

            int i = 0;
            int l = directNarrowerResources.size();
            while (i<l) {
                int j = 0;
                while (j<l) {
                    if (this.model.contains(directNarrowerResources.get(i), narrower, directNarrowerResources.get(j)) && !directNarrowerResources.get(i).getURI().equals(directNarrowerResources.get(j).getURI())) {
                        directNarrowerResources.remove(j);
                        l--;
                        if (i>j) i--;
                        j--;
                    }
                    j++;
                }
                i++;
            }
            
            List<Resource> directNarrowerCopy = new ArrayList<>(directNarrowerResources);

            for (Resource bu:backup) {
                for (Resource dn:directNarrowerCopy) {
                    if (this.model.contains(bu, narrower, dn)) directNarrowerResources.add(bu);
                }
            }
            
            for (Resource directNarrowerRes:directNarrowerResources) {
                SKOSConcept relatedConcept = JenaSKOSResource.skosConceptFromJenaResource(directNarrowerRes);
                directNarrower.add(relatedConcept);
            }

            return directNarrower;
    }
    
    protected List<SKOSConcept> getDirectBroaderConcepts(Resource conceptRes, Resource targetConceptSchemeRes, Resource targetCollectionRes) {

            Property broader = PropertyFactory.broaderTransitive;

            List<SKOSConcept> directBroader = new ArrayList<>();

             List<Resource> directBroaderResources = new ArrayList<>();

            for (StmtIterator iter = this.model.listStatements( (Resource) conceptRes, broader, (RDFNode) null); iter.hasNext(); ) {
                Statement stmt = iter.nextStatement();

                RDFNode r = stmt.getObject();

                if (r.isResource()) {
                    Resource relatedConceptRes = r.asResource();
                    if (!relatedConceptRes.getURI().equals(conceptRes.getURI())) {
                        if ((targetConceptSchemeRes==null || this.resourceIsInScheme(relatedConceptRes, targetConceptSchemeRes))&&(targetCollectionRes==null || this.resourceIsInCollection(relatedConceptRes, targetCollectionRes))) {
                            directBroaderResources.add(relatedConceptRes);
                        }
                    }
                }
            }

            int i = 0;
            int l = directBroaderResources.size();
            while (i<l) {
                int j = 0;
                while (j<l) {
                    if (this.model.contains(directBroaderResources.get(i), broader, directBroaderResources.get(j))) {
                        directBroaderResources.remove(j);
                        l--;
                        if (i>j) i--;
                        j--;
                    }
                    j++;
                }
                i++;
            }

            for (Resource directBroaderRes: directBroaderResources) {
                SKOSConcept relatedConcept = JenaSKOSResource.skosConceptFromJenaResource(directBroaderRes);
                directBroader.add(relatedConcept);
            }
            
            return directBroader;
    }
    
    protected SKOSConceptNode getHierarchyOfConcept (JenaSKOSConcept concept, JenaSKOSConceptScheme taregtConceptScheme, JenaSKOSCollection targetCollection, HierarchyMethod.RelationshipType relType, String sortLanguage) {
        SKOSConceptNode cn = new DefaultSKOSConceptNode(concept);
        TreeSet<SKOSConcept> sortedChildConcepts = new TreeSet<>();
        CloseableIterator<SKOSConcept> iter;
        
        switch (relType) {
            case BOTH_NARROWER:
                iter = this.listSemanticRelations(concept, SKOSSemanticProperty.narrower, taregtConceptScheme, targetCollection);
                while (iter.hasNext()) {
                    SKOSConcept narrower = iter.next();
                    if (narrower!=null) {
                        narrower.setComparisonLanguage(sortLanguage);
                        sortedChildConcepts.add(narrower);
                    }
                }
            case DIRECT_NARROWER:
                List<SKOSConcept> childConcepts = this.getDirectNarrowerConcepts(concept, (Resource)taregtConceptScheme, (Resource)targetCollection);
                for (SKOSConcept childConcept: childConcepts) {
                    childConcept.setComparisonLanguage(sortLanguage);
                    sortedChildConcepts.add(childConcept);
                }
                break;
            default:
                iter = this.listSemanticRelations(concept, SKOSSemanticProperty.narrower, taregtConceptScheme, targetCollection);
                while (iter.hasNext()) {
                    SKOSConcept narrower = iter.next();
                    if (narrower!=null) {
                        narrower.setComparisonLanguage(sortLanguage);
                        sortedChildConcepts.add(narrower);
                    }
                }
                break;
        }
        
        if (!sortedChildConcepts.isEmpty()) {
            for (SKOSConcept childConcept: sortedChildConcepts) {
                cn.addChild(this.getHierarchyOfConcept((JenaSKOSConcept)childConcept, taregtConceptScheme, targetCollection, relType, sortLanguage));
            }
        }
        return cn;
    }
    
    
    /**
     * {@inheritDoc}<br/>
     * Checks if the argument object is equal to the Jena SKOS thesaurus.
     * Two Jena SKSO thesauri are equal if their underlying models are equal
     * @param obj {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if (obj==null) return false;
        if (!(obj instanceof JenaSKOS)) {
              return false;
        }
        JenaSKOS thesaurus = (JenaSKOS)obj;

        if (this.model!=null) {
           return (this.model.equals(thesaurus.model));
        }
        else {
           return (thesaurus.model==null);
        }
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.model);
        return hash;
    }

    /**
     * Returns the Jena Resource for the provided {@link ie.cmrc.skos.core.SKOSType}
     * @param type SKOS type
     * @param withCreation If {@code true}, the method will create the SKOS type
     * resource if it does not exist in this thesaurus model.
     * @return The Jena resource for the provided {@link ie.cmrc.skos.core.SKOSType} if
     * it exists or if {@code withCreation==true}, {@code null} otherwise
     */
    public Resource getSKOSTypeResource(SKOSType type, boolean withCreation) {
        if (this.model != null && type != null) {
            Resource resource = this.model.getResource(type.uri());
            if (resource == null && withCreation) {
                resource = this.model.createResource(type.uri());
            }
            return resource;
        } else {
            return null;
        }
    }

    /**
     * Returns the Jena Resource for the provided {@link ie.cmrc.skos.core.SKOSType}
     * @param type SKOS type
     * @return The Jena resource for the provided {@link ie.cmrc.skos.core.SKOSType} if
     * it exists, {@code null} otherwise
     */
    public Resource getSKOSTypeResource(SKOSType type) {
        return this.getSKOSTypeResource(type, false);
    }

    /**
     * Returns the Jena Resource for the provided {@link ie.cmrc.skos.core.SKOSObjectProperty}
     * @param objectProperty SKOS object property
     * @param withCreation If {@code true}, the method will create the SKOS object property
     * resource if it does not exist in this thesaurus model.
     * @return The Jena resource for the provided {@link ie.cmrc.skos.core.SKOSObjectProperty} if
     * it exists or if {@code withCreation==true}, {@code null} otherwise
     */
    public Property getProperty(SKOSObjectProperty objectProperty, boolean withCreation) {
        if (this.model != null && objectProperty != null) {
            Property property = this.model.getProperty(objectProperty.uri());
            if (property == null && withCreation) {
                property = this.model.createProperty(objectProperty.uri());
            }
            return property;
        } else {
            return null;
        }
    }

    /**
     * Returns the Jena Resource for the provided {@link ie.cmrc.skos.core.SKOSObjectProperty}
     * @param objectProperty SKOS object property
     * @return The Jena resource for the provided {@link ie.cmrc.skos.core.SKOSObjectProperty} if
     * it exists, {@code null} otherwise
     */
    public Property getProperty(SKOSObjectProperty objectProperty) {
        return getProperty(objectProperty, false);
    }

    /**
     * Returns the Jena Resource for the provided {@link ie.cmrc.skos.core.SKOSAnnotationProperty}
     * @param annotationProperty SKOS annotation property
     * @param withCreation If {@code true}, the method will create the SKOS annotation property
     * resource if it does not exist in this thesaurus model.
     * @return The Jena resource for the provided {@link ie.cmrc.skos.core.SKOSAnnotationProperty} if
     * it exists or if {@code withCreation==true}, {@code null} otherwise
     */
    public Property getProperty(SKOSAnnotationProperty annotationProperty, boolean withCreation) {
        if (this.model != null && annotationProperty != null) {
            Property property = this.model.getProperty(annotationProperty.uri());
            if (property == null && withCreation) {
                property = this.model.createProperty(annotationProperty.uri());
            }
            return property;
        } else {
            return null;
        }
    }

    /**
     * Returns the Jena Resource for the provided {@link ie.cmrc.skos.core.SKOSAnnotationProperty}
     * @param annotationProperty SKOS annotation property
     * @return The Jena resource for the provided {@link ie.cmrc.skos.core.SKOSAnnotationProperty} if
     * it exists, {@code null} otherwise
     */
    public Property getProperty(SKOSAnnotationProperty annotationProperty) {
        return getProperty(annotationProperty, false);
    }

    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Implementing the Jena Model interface (all the implemented methods
    // are delegated to the inner Model object)
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    
    @Override
    public long size() {
        return model.size();
    }

    @Override
    public boolean isEmpty() {
        return model.isEmpty();
    }

    @Override
    public ResIterator listSubjects() {
        return model.listSubjects();
    }

    @Override
    public NsIterator listNameSpaces() {
        return model.listNameSpaces();
    }

    @Override
    public Resource getResource(String uri) {
        return model.getResource(uri);
    }

    @Override
    public Property getProperty(String nameSpace, String localName) {
        return model.getProperty(nameSpace, localName);
    }

    @Override
    public Resource createResource() {
        return model.createResource();
    }

    @Override
    public Resource createResource(AnonId id) {
        return model.createResource(id);
    }

    @Override
    public Resource createResource(String uri) {
        return model.createResource(uri);
    }

    @Override
    public Property createProperty(String nameSpace, String localName) {
        return model.createProperty(nameSpace, localName);
    }

    @Override
    public Literal createLiteral(String v, String language) {
        return model.createLiteral(v, language);
    }

    @Override
    public Literal createLiteral(String v, boolean wellFormed) {
        return model.createLiteral(v, wellFormed);
    }

    @Override
    public Literal createTypedLiteral(String lex, RDFDatatype dtype) {
        return model.createTypedLiteral(lex, dtype);
    }

    @Override
    public Literal createTypedLiteral(Object value, RDFDatatype dtype) {
        return model.createTypedLiteral(value, dtype);
    }

    @Override
    public Literal createTypedLiteral(Object value) {
        return model.createTypedLiteral(value);
    }

    @Override
    public Statement createStatement(Resource s, Property p, RDFNode o) {
        return model.createStatement(s, p, o);
    }

    @Override
    public RDFList createList() {
        return model.createList();
    }

    @Override
    public RDFList createList(Iterator<? extends RDFNode> members) {
        return model.createList(members);
    }

    @Override
    public RDFList createList(RDFNode[] members) {
        return model.createList(members);
    }

    @Override
    public Model add(Statement s) {
        return model.add(s);
    }

    @Override
    public Model add(Statement[] statements) {
        return model.add(statements);
    }

    @Override
    public Model remove(Statement[] statements) {
        return model.remove(statements);
    }

    @Override
    public Model add(List<Statement> statements) {
        return model.add(statements);
    }

    @Override
    public Model remove(List<Statement> statements) {
        return model.remove(statements);
    }

    @Override
    public Model add(StmtIterator iter) {
        return model.add(iter);
    }

    @Override
    public Model add(Model m) {
        return model.add(m);
    }

    @Override
    public Model add(Model m, boolean suppressReifications) {
        return model.add(m, suppressReifications);
    }

    @Override
    public Model read(String url) {
        return model.read(url);
    }

    @Override
    public Model read(InputStream in, String base) {
        return model.read(in, base);
    }

    @Override
    public Model read(InputStream in, String base, String lang) {
        return model.read(in, base, lang);
    }

    @Override
    public Model read(Reader reader, String base) {
        return model.read(reader, base);
    }

    @Override
    public Model read(String url, String lang) {
        return model.read(url, lang);
    }

    @Override
    public Model read(Reader reader, String base, String lang) {
        return model.read(reader, base, lang);
    }

    @Override
    public Model read(String url, String base, String lang) {
        return model.read(url, base, lang);
    }

    @Override
    public Model write(Writer writer) {
        return model.write(writer);
    }

    @Override
    public Model write(Writer writer, String lang) {
        return model.write(writer, lang);
    }

    @Override
    public Model write(Writer writer, String lang, String base) {
        return model.write(writer, lang, base);
    }

    @Override
    public Model write(OutputStream out) {
        return model.write(out);
    }

    @Override
    public Model write(OutputStream out, String lang) {
        return model.write(out, lang);
    }

    @Override
    public Model write(OutputStream out, String lang, String base) {
        return model.write(out, lang, base);
    }

    @Override
    public Model remove(Statement s) {
        return model.remove(s);
    }

    @Override
    public Statement getRequiredProperty(Resource s, Property p) {
        return model.getRequiredProperty(s, p);
    }

    @Override
    public Statement getProperty(Resource s, Property p) {
        return model.getProperty(s, p);
    }

    @Override
    public ResIterator listSubjectsWithProperty(Property p) {
        return model.listSubjectsWithProperty(p);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p) {
        return model.listResourcesWithProperty(p);
    }

    @Override
    public ResIterator listSubjectsWithProperty(Property p, RDFNode o) {
        return model.listSubjectsWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, RDFNode o) {
        return model.listResourcesWithProperty(p, o);
    }

    @Override
    public NodeIterator listObjects() {
        return model.listObjects();
    }

    @Override
    public NodeIterator listObjectsOfProperty(Property p) {
        return model.listObjectsOfProperty(p);
    }

    @Override
    public NodeIterator listObjectsOfProperty(Resource s, Property p) {
        return model.listObjectsOfProperty(s, p);
    }

    @Override
    public boolean contains(Resource s, Property p) {
        return model.contains(s, p);
    }

    @Override
    public boolean containsResource(RDFNode r) {
        return model.containsResource(r);
    }

    @Override
    public boolean contains(Resource s, Property p, RDFNode o) {
        return model.contains(s, p, o);
    }

    @Override
    public boolean contains(Statement s) {
        return model.contains(s);
    }

    @Override
    public boolean containsAny(StmtIterator iter) {
        return model.containsAny(iter);
    }

    @Override
    public boolean containsAll(StmtIterator iter) {
        return model.containsAll(iter);
    }

    @Override
    public boolean containsAny(Model model) {
        return this.model.containsAny(model);
    }

    @Override
    public boolean containsAll(Model model) {
        return this.model.containsAll(model);
    }

    @Override
    public boolean isReified(Statement s) {
        return model.isReified(s);
    }

    @Override
    public Resource getAnyReifiedStatement(Statement s) {
        return model.getAnyReifiedStatement(s);
    }

    @Override
    public void removeAllReifications(Statement s) {
        model.removeAllReifications(s);
    }

    @Override
    public void removeReification(ReifiedStatement rs) {
        model.removeReification(rs);
    }

    @Override
    public StmtIterator listStatements() {
        return model.listStatements();
    }

    @Override
    public StmtIterator listStatements(Selector s) {
        return model.listStatements(s);
    }

    @Override
    public StmtIterator listStatements(Resource s, Property p, RDFNode o) {
        return model.listStatements(s, p, o);
    }

    @Override
    public ReifiedStatement createReifiedStatement(Statement s) {
        return model.createReifiedStatement(s);
    }

    @Override
    public ReifiedStatement createReifiedStatement(String uri, Statement s) {
        return model.createReifiedStatement(uri, s);
    }

    @Override
    public RSIterator listReifiedStatements() {
        return model.listReifiedStatements();
    }

    @Override
    public RSIterator listReifiedStatements(Statement st) {
        return model.listReifiedStatements(st);
    }

    @Override
    public ReificationStyle getReificationStyle() {
        return model.getReificationStyle();
    }

    @Override
    public Model query(Selector s) {
        return model.query(s);
    }

    @Override
    public Model union(Model model) {
        return this.model.union(model);
    }

    @Override
    public Model intersection(Model model) {
        return this.model.intersection(model);
    }

    @Override
    public Model difference(Model model) {
        return this.model.difference(model);
    }

    @Override
    public Model begin() {
        return model.begin();
    }

    @Override
    public Model abort() {
        return model.abort();
    }

    @Override
    public Object executeInTransaction(Command cmd) {
        return model.executeInTransaction(cmd);
    }

    @Override
    public boolean independent() {
        return model.independent();
    }

    @Override
    public boolean supportsTransactions() {
        return model.supportsTransactions();
    }

    @Override
    public boolean supportsSetOperations() {
        return model.supportsSetOperations();
    }

    @Override
    public boolean isIsomorphicWith(Model g) {
        return model.isIsomorphicWith(g);
    }

    @Override
    public Lock getLock() {
        return model.getLock();
    }

    @Override
    public Model register(ModelChangedListener listener) {
        return model.register(listener);
    }

    @Override
    public Model unregister(ModelChangedListener listener) {
        return model.unregister(listener);
    }

    @Override
    public Model notifyEvent(Object e) {
        return model.notifyEvent(e);
    }

    @Override
    public Model removeAll() {
        return model.removeAll();
    }

    @Override
    public Model removeAll(Resource s, Property p, RDFNode r) {
        return model.removeAll(s, p, r);
    }

    @Override
    public Resource getResource(String uri, ResourceF f) {
        return model.getResource(uri, f);
    }

    @Override
    public Property getProperty(String uri) {
        return model.getProperty(uri);
    }

    @Override
    public Bag getBag(String uri) {
        return model.getBag(uri);
    }

    @Override
    public Bag getBag(Resource r) {
        return model.getBag(r);
    }

    @Override
    public Alt getAlt(String uri) {
        return model.getAlt(uri);
    }

    @Override
    public Alt getAlt(Resource r) {
        return model.getAlt(r);
    }

    @Override
    public Seq getSeq(String uri) {
        return model.getSeq(uri);
    }

    @Override
    public Seq getSeq(Resource r) {
        return model.getSeq(r);
    }

    @Override
    public Resource createResource(Resource type) {
        return model.createResource(type);
    }

    @Override
    public RDFNode getRDFNode(Node n) {
        return model.getRDFNode(n);
    }

    @Override
    public Resource createResource(String uri, Resource type) {
        return model.createResource(uri, type);
    }

    @Override
    public Resource createResource(ResourceF f) {
        return model.createResource(f);
    }

    @Override
    public Resource createResource(String uri, ResourceF f) {
        return model.createResource(uri, f);
    }

    @Override
    public Property createProperty(String uri) {
        return model.createProperty(uri);
    }

    @Override
    public Literal createLiteral(String v) {
        return model.createLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(boolean v) {
        return model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(int v) {
        return model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(long v) {
        return model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(Calendar d) {
        return model.createTypedLiteral(d);
    }

    @Override
    public Literal createTypedLiteral(char v) {
        return model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(float v) {
        return model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(double v) {
        return model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(String v) {
        return model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(String lex, String typeURI) {
        return model.createTypedLiteral(lex, typeURI);
    }

    @Override
    public Literal createTypedLiteral(Object value, String typeURI) {
        return model.createTypedLiteral(value, typeURI);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, boolean o) {
        return model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, float o) {
        return model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, double o) {
        return model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, long o) {
        return model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, int o) {
        return model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, char o) {
        return model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, Object o) {
        return model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createStatement(Resource s, Property p, String o) {
        return model.createStatement(s, p, o);
    }

    @Override
    public Statement createStatement(Resource s, Property p, String o, String l) {
        return model.createStatement(s, p, o, l);
    }

    @Override
    public Statement createStatement(Resource s, Property p, String o, boolean wellFormed) {
        return model.createStatement(s, p, o, wellFormed);
    }

    @Override
    public Statement createStatement(Resource s, Property p, String o, String l, boolean wellFormed) {
        return model.createStatement(s, p, o, l, wellFormed);
    }

    @Override
    public Bag createBag() {
        return model.createBag();
    }

    @Override
    public Bag createBag(String uri) {
        return model.createBag(uri);
    }

    @Override
    public Alt createAlt() {
        return model.createAlt();
    }

    @Override
    public Alt createAlt(String uri) {
        return model.createAlt(uri);
    }

    @Override
    public Seq createSeq() {
        return model.createSeq();
    }

    @Override
    public Seq createSeq(String uri) {
        return model.createSeq(uri);
    }

    @Override
    public Model add(Resource s, Property p, RDFNode o) {
        return model.add(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, boolean o) {
        return model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, long o) {
        return model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, int o) {
        return model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, char o) {
        return model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, float o) {
        return model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, double o) {
        return model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, Object o) {
        return model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, Literal o) {
        return model.addLiteral(s, p, o);
    }

    @Override
    public Model add(Resource s, Property p, String o) {
        return model.add(s, p, o);
    }

    @Override
    public Model add(Resource s, Property p, String lex, RDFDatatype datatype) {
        return model.add(s, p, lex, datatype);
    }

    @Override
    public Model add(Resource s, Property p, String o, boolean wellFormed) {
        return model.add(s, p, o, wellFormed);
    }

    @Override
    public Model add(Resource s, Property p, String o, String l) {
        return model.add(s, p, o, l);
    }

    @Override
    public Model remove(Resource s, Property p, RDFNode o) {
        return model.remove(s, p, o);
    }

    @Override
    public Model remove(StmtIterator iter) {
        return model.remove(iter);
    }

    @Override
    public Model remove(Model m) {
        return model.remove(m);
    }

    @Override
    public Model remove(Model m, boolean suppressReifications) {
        return model.remove(m, suppressReifications);
    }

    @Override
    public StmtIterator listLiteralStatements(Resource subject, Property predicate, boolean object) {
        return model.listLiteralStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listLiteralStatements(Resource subject, Property predicate, char object) {
        return model.listLiteralStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listLiteralStatements(Resource subject, Property predicate, long object) {
        return model.listLiteralStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listLiteralStatements(Resource subject, Property predicate, float object) {
        return model.listLiteralStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listLiteralStatements(Resource subject, Property predicate, double object) {
        return model.listLiteralStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listStatements(Resource subject, Property predicate, String object) {
        return model.listStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listStatements(Resource subject, Property predicate, String object, String lang) {
        return model.listStatements(subject, predicate, object, lang);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, boolean o) {
        return model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, long o) {
        return model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, char o) {
        return model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, float o) {
        return model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, double o) {
        return model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, Object o) {
        return model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listSubjectsWithProperty(Property p, String o) {
        return model.listSubjectsWithProperty(p, o);
    }

    @Override
    public ResIterator listSubjectsWithProperty(Property p, String o, String l) {
        return model.listSubjectsWithProperty(p, o, l);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, boolean o) {
        return model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, long o) {
        return model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, int o) {
        return model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, char o) {
        return model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, float o) {
        return model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, double o) {
        return model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, Object o) {
        return model.containsLiteral(s, p, o);
    }

    @Override
    public boolean contains(Resource s, Property p, String o) {
        return model.contains(s, p, o);
    }

    @Override
    public boolean contains(Resource s, Property p, String o, String l) {
        return model.contains(s, p, o, l);
    }

    @Override
    public RDFWriter getWriter() {
        return model.getWriter();
    }

    @Override
    public RDFWriter getWriter(String lang) {
        return model.getWriter(lang);
    }

    @Override
    public String setWriterClassName(String lang, String className) {
        return model.setWriterClassName(lang, className);
    }

    @Override
    public Statement asStatement(Triple t) {
        return model.asStatement(t);
    }

    @Override
    public Graph getGraph() {
        return model.getGraph();
    }

    @Override
    public RDFNode asRDFNode(Node n) {
        return model.asRDFNode(n);
    }

    @Override
    public Resource wrapAsResource(Node arg0) {
        return model.wrapAsResource(arg0);
    }

    @Override
    public RDFReader getReader() {
        return model.getReader();
    }

    @Override
    public RDFReader getReader(String lang) {
        return model.getReader(lang);
    }

    @Override
    public String setReaderClassName(String lang, String className) {
        return model.setReaderClassName(lang, className);
    }

    @Override
    public PrefixMapping setNsPrefix(String prefix, String uri) {
        return model.setNsPrefix(prefix, uri);
    }

    @Override
    public PrefixMapping removeNsPrefix(String prefix) {
        return model.removeNsPrefix(prefix);
    }

    @Override
    public PrefixMapping setNsPrefixes(PrefixMapping other) {
        return model.setNsPrefixes(other);
    }

    @Override
    public PrefixMapping setNsPrefixes(Map<String, String> map) {
        return model.setNsPrefixes(map);
    }

    @Override
    public PrefixMapping withDefaultMappings(PrefixMapping map) {
        return model.withDefaultMappings(map);
    }

    @Override
    public String getNsPrefixURI(String prefix) {
        return model.getNsPrefixURI(prefix);
    }

    @Override
    public String getNsURIPrefix(String uri) {
        return model.getNsURIPrefix(uri);
    }

    @Override
    public Map<String, String> getNsPrefixMap() {
        return model.getNsPrefixMap();
    }

    @Override
    public String expandPrefix(String prefixed) {
        return model.expandPrefix(prefixed);
    }

    @Override
    public String shortForm(String uri) {
        return model.shortForm(uri);
    }

    @Override
    public String qnameFor(String uri) {
        return model.qnameFor(uri);
    }

    @Override
    public PrefixMapping lock() {
        return model.lock();
    }

    @Override
    public boolean samePrefixMappingAs(PrefixMapping other) {
        return model.samePrefixMappingAs(other);
    }

    @Override
    public void enterCriticalSection(boolean readLockRequested) {
        model.enterCriticalSection(readLockRequested);
    }

    @Override
    public void leaveCriticalSection() {
        model.leaveCriticalSection();
    }

    @Override
    public QueryHandler queryHandler() {
        return model.queryHandler();
    }
    
    
    
}
