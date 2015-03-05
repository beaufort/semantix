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
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import ie.cmrc.smtx.skos.model.AbstractSKOSResource;
import ie.cmrc.smtx.skos.model.SKOSAnnotationProperty;
import ie.cmrc.smtx.skos.model.SKOSConceptScheme;
import ie.cmrc.smtx.skos.model.SKOSObjectProperty;
import ie.cmrc.smtx.skos.model.SKOSResource;
import ie.cmrc.smtx.skos.model.SKOSCollectionMember;
import ie.cmrc.smtx.skos.model.SKOSElementProperty;
import ie.cmrc.smtx.skos.jena.util.JenaResourceIterator;
import ie.cmrc.smtx.skos.jena.util.JenaResourceIteratorFactory;
import ie.cmrc.smtx.skos.jena.util.SKOSResourceIterFactory;
import ie.cmrc.util.Term;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract implementation of the {@link ie.cmrc.skos.core.SKOSResource} interface
 * for Jena-based thesauri. This implementation wraps a {@code com.hp.hpl.jena.rdf.model.Resource}
 * instance as {@link ie.cmrc.skos.core.SKOSResource}<br/>
 * {@code JenaSKOSResource} also implements the Jena Resource interface
 * ({@code com.hp.hpl.jena.rdf.model.Resource}).
 * 
 * @author Yassine Lassoued
 */
public abstract class JenaSKOSResource extends AbstractSKOSResource implements Resource {

    /**
     * Underlying Jena Resource ({@code com.hp.hpl.jena.rdf.model.Resource}) for this
     * {@code JenaSKOSResource}
     */
    protected final Resource resource;
    
    /**
     * Constructs a {@link JenaSKOSResource} that wraps the provided Jena Resource
     * ({@code com.hp.hpl.jena.rdf.model.Resource})
     * @param resource Jena Resource ({@code com.hp.hpl.jena.rdf.model.Resource}) to wrap
     * as a {@link ie.cmrc.skos.core.SKOSResource}.
     * @throws IllegalArgumentException if {@code resource==null}
     */
    public JenaSKOSResource(Resource resource) {
        super(resource!=null?resource.getURI():null);
        this.resource = resource;
        if (this.resource==null) throw new IllegalArgumentException("JenaSKOSResource constructor: Resource argument must not be null");
        //this.thesaurus = thesaurus;
    }

    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Implementing the SKOSResource interface
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public JenaSKOS getSKOS() {
        if (this.resource != null) return new JenaSKOS(this.resource.getModel());
        else return null;
    }

    /**
     * {@inheritDoc}
     * @param annotationProperty {@inheritDoc}
     * @param value {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource addAnnotation(SKOSAnnotationProperty annotationProperty, String value, String language) {
        if (this.resource != null && annotationProperty!=null) {
            String trimmedValue;
            if (value!=null && !(trimmedValue=value).isEmpty()) {

                Property property = ((JenaSKOS)(this.getSKOS())).getProperty(annotationProperty, true);

                if (!this.resource.hasProperty(property, trimmedValue, language)) {
                    if (!annotationProperty.isMultiple()) {
                        // Clear values for property and language
                        List<RDFNode> toDelete = new ArrayList<>();
                        StmtIterator iter = this.resource.listProperties(property);
                        while (iter.hasNext()) {
                            Statement stmt = iter.next();
                            RDFNode node = stmt.getObject();
                            if (node.isLiteral()) {
                                Literal literal = (Literal)node;
                                String lang = literal.getLanguage();
                                if ((language==null && lang==null) || (language!=null && language.equals(lang))) {
                                    toDelete.add(node);
                                }
                            }
                        }
                        iter.close();
                        for (RDFNode node: toDelete) this.resource.getModel().remove(this.resource, property, node);
                    }
                    this.resource.addProperty(property, value, language);
                }
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param annotationProperty {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getAnnotation(SKOSAnnotationProperty annotationProperty, String language) {
        if (this.resource!=null && annotationProperty!=null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(annotationProperty);

            StmtIterator iter = this.resource.listProperties(property);

            while (iter.hasNext()) {
                Statement stmt = iter.next();
                RDFNode node = stmt.getObject();
                if (node.isLiteral()) {
                    Literal literal = (Literal)node;
                    String lang = literal.getLanguage();

                    if ((language==null && lang==null) || (language!=null && language.equals(lang))) {
                        String value = literal.getString();
                        return value;
                    }
                }
            }
            
            iter.close();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @param annotationProperty {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<Term> getAnnotations(SKOSAnnotationProperty annotationProperty) {
        List<Term> terms = new ArrayList<>();
        
        if (this.resource!=null && annotationProperty!=null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(annotationProperty);

            StmtIterator iter = this.resource.listProperties(property);

            while (iter.hasNext()) {
                Statement stmt = iter.next();
                RDFNode node = stmt.getObject();
                if (node.isLiteral()) {
                    Literal literal = (Literal)node;
                    String lang = literal.getLanguage();
                    String value = literal.getString();

                    terms.add(new Term(value, lang));
                }
            }
            
            iter.close();
        }
        return terms;
    }

    /**
     * {@inheritDoc}
     * @param annotationProperty {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<String> getAnnotations(SKOSAnnotationProperty annotationProperty, String language) {
        List<String> values = new ArrayList<>();
        if (this.resource!=null && annotationProperty!=null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(annotationProperty);

            StmtIterator iter = this.resource.listProperties(property);

            while (iter.hasNext()) {
                Statement stmt = iter.next();
                RDFNode node = stmt.getObject();
                if (node.isLiteral()) {
                    Literal literal = (Literal)node;
                    String lang = literal.getLanguage();

                    if ((language==null && lang==null) || (language!=null && language.equals(lang))) {
                        String value = literal.getString();
                        values.add(value);
                    }
                }
            }
            
            iter.close();
        }
        return values;
    }

    /**
     * {@inheritDoc}
     * @param annotationProperty {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasAnnotation(SKOSAnnotationProperty annotationProperty) {
        if (this.resource!=null && annotationProperty!=null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(annotationProperty);
            if (property != null) {
                return this.resource.hasProperty(property);
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * @param annotationProperty {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasAnnotation(SKOSAnnotationProperty annotationProperty, String language) {
        if (this.resource!=null && annotationProperty!=null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(annotationProperty);
            
            StmtIterator iter = this.resource.listProperties(property);
            
            while (iter.hasNext()) {
                Statement stmt = iter.next();
                RDFNode node = stmt.getObject();
                if (node.isLiteral()) {
                    Literal literal = (Literal)node;
                    String lang = literal.getLanguage();

                    if ((language==null && lang==null) || (language!=null && language.equals(lang))) {
                        String value = literal.getString();
                        if (value!=null) {
                            iter.close();
                            return true;
                        }
                    }
                }
            }
            
            iter.close();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * @param annotationProperty {@inheritDoc}
     * @param value {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasAnnotation(SKOSAnnotationProperty annotationProperty, String value, String language) {
        if (value!=null && this.resource!=null && annotationProperty!=null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(annotationProperty);
            
            StmtIterator iter = this.resource.listProperties(property);
            
            while (iter.hasNext()) {
                Statement stmt = iter.next();
                RDFNode node = stmt.getObject();
                if (node.isLiteral()) {
                    Literal literal = (Literal)node;
                    String lang = literal.getLanguage();

                    if ((language==null && lang==null) || (language!=null && language.equals(lang))) {
                        String annValue = literal.getString();
                        if (annValue!=null && annValue.equals(value)) {
                            iter.close();
                            return true;
                        }
                    }
                }
            }
            
            iter.close();
        }
        return false;
    }
    
    
    
    /**
     * {@inheritDoc}
     * @param annotationProperty {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeAnnotations(SKOSAnnotationProperty annotationProperty) {
        if (this.resource!=null && annotationProperty!=null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(annotationProperty);
            this.resource.removeAll(property);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param annotationProperty {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeAnnotations(SKOSAnnotationProperty annotationProperty, String language) {
        if (this.resource!=null && annotationProperty!=null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(annotationProperty);
            
            StmtIterator iter = this.resource.listProperties(property);
            
            List<RDFNode> nodesToDelete = new ArrayList<>();
            
            while (iter.hasNext()) {
                Statement stmt = iter.next();
                RDFNode node = stmt.getObject();
                if (node.isLiteral()) {
                    Literal literal = (Literal)node;
                    String lang = literal.getLanguage();

                    if ((language==null && lang==null) || (language!=null && language.equals(lang))) {
                        nodesToDelete.add(node);
                    }
                }
            }
            iter.close();
            for (RDFNode node: nodesToDelete) this.resource.getModel().remove(resource, property, node);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param annotationProperty {@inheritDoc}
     * @param annotationValue {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeAnnotation(SKOSAnnotationProperty annotationProperty, String annotationValue, String language) {
        if (this.resource!=null && annotationProperty!=null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(annotationProperty);
            StmtIterator iter = this.resource.listProperties(property);
            
            List<RDFNode> nodesToDelete = new ArrayList<>();
            
            while (iter.hasNext()) {
                Statement stmt = iter.next();
                RDFNode node = stmt.getObject();
                if (node.isLiteral()) {
                    Literal literal = (Literal)node;
                    String lang = literal.getLanguage();

                    if ((language==null && lang==null) || (language!=null && language.equals(lang))) {
                        nodesToDelete.add(node);
                    }
                }
            }
            iter.close();
            for (RDFNode node: nodesToDelete) this.resource.getModel().remove(resource, property, node);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSAnnotationProperty> getAnnotationProperties() {
        List<SKOSAnnotationProperty> annotationProperties = new ArrayList<>();
        
        if (this.resource!=null) {
            StmtIterator iter = this.resource.listProperties();
            
            while (iter.hasNext()) {
                Statement stmt = iter.next();
                Property property = stmt.getPredicate();
                RDFNode node = stmt.getObject();
                // Only consider those statements with a literal as an object
                if (node.isLiteral()) {
                    String propLocalName = property.getURI();
                    SKOSAnnotationProperty annotationProperty = SKOSAnnotationProperty.fromString(propLocalName);
                    if (annotationProperty!=null && !annotationProperties.contains(annotationProperty)) annotationProperties.add(annotationProperty);
                }
            }
            iter.close();
        }
        
        return annotationProperties;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<String> getAnnotationLanguages() {
        List<String> languages = new ArrayList<>();
        
        if (this.resource!=null) {
            StmtIterator iter = this.resource.listProperties();
            
            while (iter.hasNext()) {
                Statement stmt = iter.next();
                Property property = stmt.getPredicate();
                RDFNode node = stmt.getObject();
                // Only consider those statements with a literal as an object
                if (node.isLiteral()) {
                    Literal literal = (Literal)node;
                    String lang = literal.getLanguage();
                    String propLocalName = property.getLocalName();
                    SKOSAnnotationProperty annotationProperty = SKOSAnnotationProperty.fromString(propLocalName);
                    if (annotationProperty!=null && !languages.contains(lang)) languages.add(lang);
                }
            }
            iter.close();
        }
        
        return languages;
    }

    /**
     * {@inheritDoc}
     * @param annotationProperty {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<String> getAnnotationLanguages(SKOSAnnotationProperty annotationProperty) {
        List<String> languages = new ArrayList<>();
        
        if (this.resource!=null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(annotationProperty);
            if (property!=null) {
                StmtIterator iter = this.resource.listProperties(property);

                while (iter.hasNext()) {
                    Statement stmt = iter.next();
                    RDFNode node = stmt.getObject();
                    // Only consider those statements with a literal as an object
                    if (node.isLiteral()) {
                        Literal literal = (Literal)node;
                        String lang = literal.getLanguage();
                        if (!languages.contains(lang)) languages.add(lang);
                    }
                }
                iter.close();
            }
        }
        
        return languages;
    }
    
    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param someResourceURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource addRelation(SKOSObjectProperty relationshipType, String someResourceURI) {
        if (this.resource!=null && relationshipType!=null) {
            if (someResourceURI!=null && !someResourceURI.isEmpty()) {

                Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType, true);
                
                Resource otherResource = this.resource.getModel().getResource(someResourceURI);
                
                if (otherResource != null) {
                    if (!this.resource.hasProperty(property, otherResource)) this.resource.addProperty(property, otherResource);
                }
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param otherResource {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource addRelation(SKOSObjectProperty relationshipType, SKOSResource otherResource) {
        if (otherResource!=null) return this.addRelation(relationshipType, otherResource.getURI());
        else return this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
//    @Override
//    public Multimap<SKOSObjectProperty, SKOSResource> getRelations() {
//        Multimap<SKOSObjectProperty, SKOSResource> relations = new Multimap<SKOSObjectProperty, SKOSResource>();
//        
//        if (this.resource!=null) {
//            StmtIterator iter = this.resource.listProperties();
//
//            while (iter.hasNext()) {
//                Statement stmt = iter.next();
//                Property property = stmt.getPredicate();
//                SKOSObjectProperty skosProperty = SKOSObjectProperty.fromString(property.getURI());
//                if (skosProperty != null) {
//                    RDFNode node = stmt.getObject();
//                    // Only consider those statements with a resource as an object
//                    if (node.isResource()) {
//                        Resource objResource = (Resource)node;
//                        SKOSResource skosResource = fromJenaResource(objResource);
//                        if (skosResource!=null) relations.put(skosProperty, skosResource);
//                    }
//                }
//            }
//            iter.close();
//        }
//        
//        return relations;
//    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ie.cmrc.smtx.skos.model.util.CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType) {
        StmtIterator iter;
        if (this.resource!=null && relationshipType!=null) {

            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);
            
            if (property!=null) {
                iter = this.resource.listProperties(property);
            }
            else iter = null;
        }
        else iter = null;
        
        return SKOSResourceIterFactory.makeSKOSResourceIterOverObjects(iter);
    }
    
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptScheme {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme) {
//        String csURI = null;
//        if (conceptScheme != null) csURI = conceptScheme.getURI();
//        return this.listRelations(relationshipType, csURI);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType, String conceptSchemeURI) {
//        return this.listRelations(relationshipType, conceptSchemeURI, null);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptScheme {@inheritDoc}
//     * @param collection {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme, SKOSCollection collection) {
//        String csURI = null;
//        if (conceptScheme != null) csURI = conceptScheme.getURI();
//        String collectionURI = null;
//        if (collection != null) collectionURI = collection.getURI();
//        return this.listRelations(relationshipType, collectionURI, collectionURI);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURI {@inheritDoc}
//     * @param collectionURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType, String conceptSchemeURI, String collectionURI) {
//        StmtIterator stmtIter = this.getRelationIter(relationshipType, conceptSchemeURI, collectionURI);
//        return SKOSResourceIterFactory.makeSKOSResourceIterOverObjects(stmtIter);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURIs {@inheritDoc}
//     * @param collectionURIs {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public CloseableIterator<SKOSResource> listRelations(SKOSObjectProperty relationshipType, Collection<String> conceptSchemeURIs, Collection<String> collectionURIs) {
//        StmtIterator stmtIter = this.getRelationIter(relationshipType, conceptSchemeURIs, collectionURIs);
//        return SKOSResourceIterFactory.makeSKOSResourceIterOverObjects(stmtIter);
//    }
    
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelations(SKOSObjectProperty relationshipType) {
//        List<SKOSResource> relations = new ArrayList<SKOSResource>();
//        
//        if (this.resource!=null && relationshipType!=null) {
//
//            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);
//
//            if (property!=null) {
//                StmtIterator iter = this.resource.listProperties(property);
//
//                while (iter.hasNext()) {
//                    Statement stmt = iter.next();
//                    RDFNode node = stmt.getObject();
//                    // Only consider those statements with a resource as an object
//                    if (node.isResource()) {
//                        Resource objResource = (Resource)node;
//                        SKOSResource skosResource = fromJenaResource(objResource);
//                        if (skosResource!=null) relations.add(skosResource);
//                    }
//                }
//                iter.close();
//            }
//        }
//        
//        return relations;
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptScheme {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelations(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme) {
//        String csURI = null;
//        if (conceptScheme != null) csURI = conceptScheme.getURI();
//        return this.getRelations(relationshipType, csURI);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelations(SKOSObjectProperty relationshipType, String conceptSchemeURI) {
//        return this.getRelations(relationshipType, conceptSchemeURI, null);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptScheme {@inheritDoc}
//     * @param collection {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelations(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme, SKOSCollection collection) {
//        String csURI = null;
//        if (conceptScheme != null) csURI = conceptScheme.getURI();
//        String collectionURI = null;
//        if (collection != null) collectionURI = collection.getURI();
//        return this.getRelations(relationshipType, collectionURI, collectionURI);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURI {@inheritDoc}
//     * @param collectionURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelations(SKOSObjectProperty relationshipType, String conceptSchemeURI, String collectionURI) {
//        List<SKOSResource> elts = new ArrayList<SKOSResource>();
//        
//        StmtIterator stmtIter = this.getRelationIter(relationshipType, conceptSchemeURI, collectionURI);
//        CloseableIterator<SKOSResource> iter = SKOSResourceIterFactory.makeSKOSResourceIterOverObjects(stmtIter);
//        while (iter.hasNext()) {
//            SKOSResource elt = iter.next();
//            if (elt != null) elts.add(elt);
//        }
//        iter.close();
//        return elts;
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURIs {@inheritDoc}
//     * @param collectionURIs {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelations(SKOSObjectProperty relationshipType, Collection<String> conceptSchemeURIs, Collection<String> collectionURIs) {
//        List<SKOSResource> elts = new ArrayList<SKOSResource>();
//        
//        StmtIterator stmtIter = this.getRelationIter(relationshipType, conceptSchemeURIs, collectionURIs);
//        CloseableIterator<SKOSResource> iter = SKOSResourceIterFactory.makeSKOSResourceIterOverObjects(stmtIter);
//        while (iter.hasNext()) {
//            SKOSResource elt = iter.next();
//            if (elt != null) elts.add(elt);
//        }
//        iter.close();
//        return elts;
//    }

    
    
    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasRelation(SKOSObjectProperty relationshipType) {
        if (this.resource!=null && relationshipType!=null) {

            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);

            if (property!=null) {
                StmtIterator iter = this.resource.listProperties(property);

                while (iter.hasNext()) {
                    Statement stmt = iter.next();
                    RDFNode node = stmt.getObject();
                    // Only consider those statements with a resource as an object
                    if (node.isResource()) {
                        Resource objResource = (Resource)node;
                        if (jenaResourceIsSKOSResource(objResource)) {
                            iter.close();
                            return true;
                        }
                    }
                }
                iter.close();
            }
        }
        
        return false;
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param resourceURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasRelation(SKOSObjectProperty relationshipType, String resourceURI) {
        if (this.resource!=null && relationshipType!=null && resourceURI!=null && !resourceURI.isEmpty()) {

            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);

            if (property!=null) {
                Resource otherResource = this.resource.getModel().getResource(resourceURI);
                if (otherResource != null) {
                    return this.resource.hasProperty(property, otherResource);
                }
            }
        }
        
        return false;
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param skosResource {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasRelation(SKOSObjectProperty relationshipType, SKOSResource skosResource) {
        if (skosResource != null) {
            return this.hasRelation(relationshipType, skosResource.getURI());
        }
        else return false;
    }
    
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
//    @Override
//    public SKOSResource removeRelations() {
//        if (this.resource!=null) {
//            SKOSObjectProperty[] skosObjectProperties = SKOSObjectProperty.values();
//            for (SKOSObjectProperty skosObjectProperty : skosObjectProperties) {
//                Property property = ((JenaSKOS)(this.getSKOS())).getProperty(skosObjectProperty);
//                this.resource.removeAll(property);
//            }
//        }
//        return this;
//    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeRelations(SKOSObjectProperty relationshipType) {
        if (this.resource!=null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);

            if (property!=null) {
                this.resource.removeAll(property);
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param resourceURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeRelation(SKOSObjectProperty relationshipType, String resourceURI) {
        if (this.resource!=null && relationshipType!=null && resourceURI!=null && !resourceURI.isEmpty()) {

            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);

            if (property!=null) {
                Resource otherResource = this.resource.getModel().getResource(resourceURI);
                if (otherResource != null) {
                    this.resource.getModel().remove(resource, property, otherResource);
                }
            }
        }
        return this;
        
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @param skosResource {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeRelation(SKOSObjectProperty relationshipType, SKOSResource skosResource) {
        if (skosResource != null) {
            return this.removeRelation(relationshipType, skosResource.getURI());
        }
        else return this;
    }
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    /*@Override
    public List<SKOSObjectProperty> getRelationships() {
    List<SKOSObjectProperty> objectProperties = new ArrayList<SKOSObjectProperty>();
    if (this.resource != null) {
    StmtIterator iter = this.resource.listProperties();
    while (iter.hasNext()) {
    Statement stmt = iter.next();
    Property property = stmt.getPredicate();
    RDFNode node = stmt.getObject();
    // Only consider those statements with a resource as an object
    if (node.isResource()) {
    String propertyURI = property.getURI();
    SKOSObjectProperty objectProperty = SKOSObjectProperty.fromString(propertyURI);
    Resource objResource = (Resource)node;
    if (objectProperty!=null && !objectProperties.contains(objectProperty) && jenaResourceIsSKOSResource(objResource)) {
    objectProperties.add(objectProperty);
    }
    }
    }
    iter.close();
    }
    return objectProperties;
    }*/
    
    /**
     * {@inheritDoc}
     * @param resourceURI {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource makeRelation(String resourceURI, SKOSObjectProperty relationshipType) {
        SKOSResource sourceResource = this.getSKOS().getSKOSResource(resourceURI);
        if (sourceResource != null) sourceResource.addRelation(relationshipType, this);
        return this;
    }

    /**
     * {@inheritDoc}
     * @param otherResource {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource makeRelation(SKOSResource otherResource, SKOSObjectProperty relationshipType) {
        if (otherResource != null) otherResource.addRelation(relationshipType, this);
        return this;
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public ie.cmrc.smtx.skos.model.util.CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType) {
        StmtIterator iter;
        if (this.resource!=null && relationshipType!=null) {

            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);
            
            if (property!=null) {
                iter = this.getModel().listStatements((Resource)null, property, this.resource);
            }
            else iter = null;
        }
        else iter = null;
        
        return SKOSResourceIterFactory.makeSKOSResourceIterOverSubjects(iter);
    }

//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptScheme {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme) {
//        String csURI = null;
//        if (conceptScheme != null) csURI = conceptScheme.getURI();
//        return this.listRelationshipSources(relationshipType, csURI);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType, String conceptSchemeURI) {
//        return this.listRelationshipSources(relationshipType, conceptSchemeURI, null);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptScheme {@inheritDoc}
//     * @param collection {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme, SKOSCollection collection) {
//        String csURI = null;
//        if (conceptScheme != null) csURI = conceptScheme.getURI();
//        String collectionURI = null;
//        if (collection != null) collectionURI = collection.getURI();
//        return this.listRelationshipSources(relationshipType, collectionURI, collectionURI);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURI {@inheritDoc}
//     * @param collectionURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType, String conceptSchemeURI, String collectionURI) {
//        StmtIterator stmtIter = this.getRelationshipSourceIter(relationshipType, conceptSchemeURI, collectionURI);
//        return SKOSResourceIterFactory.makeSKOSResourceIterOverObjects(stmtIter);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURIs {@inheritDoc}
//     * @param collectionURIs {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public CloseableIterator<SKOSResource> listRelationshipSources(SKOSObjectProperty relationshipType, Collection<String> conceptSchemeURIs, Collection<String> collectionURIs) {
//        StmtIterator stmtIter = this.getRelationshipSourceIter(relationshipType, conceptSchemeURIs, collectionURIs);
//        return SKOSResourceIterFactory.makeSKOSResourceIterOverObjects(stmtIter);
//    }
    
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType) {
//        List<SKOSResource> relationshipSources = new ArrayList<SKOSResource>();
//        
//        if (this.resource!=null && relationshipType!=null) {
//
//            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);
//
//            if (property!=null) {
//                StmtIterator iter = this.getModel().listStatements((Resource)null, property, this.resource);
//
//                while (iter.hasNext()) {
//                    Statement stmt = iter.next();
//                    Resource subject = stmt.getSubject();
//                    SKOSResource skosResource = fromJenaResource(subject);
//                    if (skosResource!=null) relationshipSources.add(skosResource);
//                }
//                iter.close();
//            }
//        }
//        
//        return relationshipSources;
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptScheme {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme) {
//        String csURI = null;
//        if (conceptScheme != null) csURI = conceptScheme.getURI();
//        return this.getRelationshipSources(relationshipType, csURI);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType, String conceptSchemeURI) {
//        return this.getRelationshipSources(relationshipType, conceptSchemeURI, null);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptScheme {@inheritDoc}
//     * @param collection {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType, SKOSConceptScheme conceptScheme, SKOSCollection collection) {
//        String csURI = null;
//        if (conceptScheme != null) csURI = conceptScheme.getURI();
//        String collectionURI = null;
//        if (collection != null) collectionURI = collection.getURI();
//        return this.getRelationshipSources(relationshipType, collectionURI, collectionURI);
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURI {@inheritDoc}
//     * @param collectionURI {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType, String conceptSchemeURI, String collectionURI) {
//        List<SKOSResource> elts = new ArrayList<SKOSResource>();
//        
//        StmtIterator stmtIter = this.getRelationshipSourceIter(relationshipType, conceptSchemeURI, collectionURI);
//        CloseableIterator<SKOSResource> iter = SKOSResourceIterFactory.makeSKOSResourceIterOverObjects(stmtIter);
//        while (iter.hasNext()) {
//            SKOSResource elt = iter.next();
//            if (elt != null) elts.add(elt);
//        }
//        iter.close();
//        
//        return elts;
//    }
//
//    /**
//     * {@inheritDoc}
//     * @param relationshipType {@inheritDoc}
//     * @param conceptSchemeURIs {@inheritDoc}
//     * @param collectionURIs {@inheritDoc}
//     * @return {@inheritDoc}
//     */
//    @Override
//    public List<SKOSResource> getRelationshipSources(SKOSObjectProperty relationshipType, Collection<String> conceptSchemeURIs, Collection<String> collectionURIs) {
//        List<SKOSResource> elts = new ArrayList<SKOSResource>();
//        
//        StmtIterator stmtIter = this.getRelationshipSourceIter(relationshipType, conceptSchemeURIs, collectionURIs);
//        CloseableIterator<SKOSResource> iter = SKOSResourceIterFactory.makeSKOSResourceIterOverObjects(stmtIter);
//        while (iter.hasNext()) {
//            SKOSResource elt = iter.next();
//            if (elt != null) elts.add(elt);
//        }
//        iter.close();
//        
//        return elts;
//    }
    
    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isRelation(SKOSObjectProperty relationshipType) {
        if (this.resource!=null && relationshipType!=null) {

            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);

            if (property!=null) {
                StmtIterator iter = this.getModel().listStatements((Resource)null, property, this.resource);

                while (iter.hasNext()) {
                    Statement stmt = iter.next();
                    Resource subject = stmt.getSubject();
                    if (jenaResourceIsSKOSResource(subject)) return true;
                }
                iter.close();
            }
        }
        
        return false;
    }

    /**
     * {@inheritDoc}
     * @param resourceURI {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isRelation(String resourceURI, SKOSObjectProperty relationshipType) {
        if (this.resource!=null && resourceURI!=null && !resourceURI.isEmpty() && relationshipType!=null) {
            
            Resource subject = this.getModel().getResource(resourceURI);
            
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);
            
            if (subject!=null && property!=null) {
                return this.getModel().contains(subject, property, this.resource);
            }
        }
        
        return false;
    }

    /**
     * {@inheritDoc}
     * @param otherResource {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isRelation(SKOSResource otherResource, SKOSObjectProperty relationshipType) {
        if (otherResource != null) {
            return this.isRelation(otherResource.getURI(), relationshipType);
        }
        else return false;
    }
    
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
//    @Override
//    public SKOSResource removeAsRelation() {
//        if (this.resource!=null) {
//            SKOSObjectProperty[] skosObjectProperties = SKOSObjectProperty.values();
//            for (SKOSObjectProperty skosObjectProperty : skosObjectProperties) {
//                this.removeAsRelation(skosObjectProperty);
//            }
//        }
//        return this;
//    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeAsRelation(SKOSObjectProperty relationshipType) {
        if (this.resource!=null && relationshipType!=null) {

            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);

            if (property!=null) {
                this.getModel().removeAll((Resource)null, property, this.resource);
            }
        }
        
        return this;
    }

    /**
     * {@inheritDoc}
     * @param resourceURI {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeAsRelation(String resourceURI, SKOSObjectProperty relationshipType) {
        if (this.resource!=null && resourceURI!=null && !resourceURI.isEmpty() && relationshipType!=null) {
            
            Resource subject = this.getModel().getResource(resourceURI);
            
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);
            
            if (subject!=null && property!=null) {
                this.getModel().remove(subject, property, this.resource);
            }
        }
        
        return this;
    }
    
    /**
     * {@inheritDoc}
     * @param skosResource {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeAsRelation(SKOSResource skosResource, SKOSObjectProperty relationshipType) {
        if (skosResource != null) {
            return this.removeAsRelation(skosResource.getURI(), relationshipType);
        }
        else return this;
    }
    
    
    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SKOSResource addToConceptScheme(String conceptSchemeURI) throws IllegalArgumentException {
        
        if (conceptSchemeURI != null && !conceptSchemeURI.isEmpty()) {
            
                    
            SKOSResource skosResource = this.getSKOS().getSKOSResource(conceptSchemeURI);
            if (skosResource != null) {
                if (skosResource.isSKOSConceptScheme()) {
                    this.addRelation(SKOSElementProperty.inScheme, skosResource);
                } else {
                    throw new IllegalArgumentException("\"" + conceptSchemeURI + "\" is not the URI of a SKOS Concept Scheme");
                }
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource addToConceptScheme(SKOSConceptScheme conceptScheme) {
        
        if (conceptScheme != null) {
            this.addRelation(SKOSElementProperty.inScheme, conceptScheme);
        }
        
        return this;
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isInScheme(String conceptSchemeURI) {
        return this.hasRelation(SKOSElementProperty.inScheme, conceptSchemeURI);
    }

    /**
     * {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isInScheme(SKOSConceptScheme conceptScheme) {
        return this.hasRelation(SKOSElementProperty.inScheme, conceptScheme);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeFromAllConceptSchemes() {
        return ((SKOSCollectionMember)this.removeRelations(SKOSElementProperty.inScheme));
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeFromConceptScheme(String conceptSchemeURI) {
        if (conceptSchemeURI != null && !conceptSchemeURI.isEmpty()) {
            SKOSResource skosResource = this.getSKOS().getSKOSResource(conceptSchemeURI);
            if (skosResource != null) {
                this.removeRelation(SKOSElementProperty.inScheme, skosResource);
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param conceptScheme {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeFromConceptScheme(SKOSConceptScheme conceptScheme) {
        return this.removeRelation(SKOSElementProperty.inScheme, conceptScheme);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<SKOSConceptScheme> getConceptSchemes() {
        List<SKOSConceptScheme> conceptSchemes = new ArrayList<>();
        Property property = ((JenaSKOS) (this.getSKOS())).getProperty(SKOSElementProperty.inScheme);
        if (property != null) {
            StmtIterator iter = this.resource.listProperties(property);
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                RDFNode node = stmt.getObject();
                // Only consider those statements with a resource as an object
                if (node.isResource()) {
                    Resource objResource = (Resource) node;
                    SKOSConceptScheme cs = JenaSKOSResource.skosConcepSchemeFromJenaResource(objResource);
                    if (cs != null) {
                        conceptSchemes.add(cs);
                    }
                }
            }
            iter.close();
        }
        return conceptSchemes;
    }
    
    
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Methods specific to JenaSKOSResource
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * Return an iterator over the Jena resources that this SKOS resource is
     * related to through the provided SKOS relationship type
     * @param relationshipType Type of the relationship between the resources
     * expressed as a {@link ie.cmrc.skos.core.SKOSObjectProperty}
     * @return A {@link ie.cmrc.skos.core.jena.util.JenaResourceIterator} iterating over
     * the Jena resources related to this SKOS resource
     * through the provided SKOS relationship type ({@code relationshipType})
     * 
     */
    public JenaResourceIterator getResourceIterOverRelationshipSources(SKOSObjectProperty relationshipType) {
        StmtIterator iter = null;
        if (relationshipType != null) {
            Property property = ((JenaSKOS)(this.getSKOS())).getProperty(relationshipType);
            if (property!=null) {
                iter = this.getModel().listStatements((Resource)null, property, this);
            }
        }
        return JenaResourceIteratorFactory.create(iter);
    }
    
    
    
    /**
     * Checks whether the Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * is a {@link ie.cmrc.skos.core.SKOSResource})
     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * instance
     * @return {@code true} if {@code someResource} is not {@code null} and is asserted
     * to have as a type one of the supported SKOS types (Concept, Collection,
     * or ConceptScheme); {@code false} otherwise
     */
    public static boolean jenaResourceIsSKOSResource(Resource someResource) {
        return (jenaResourceIsSKOSConcept(someResource)
                || jenaResourceIsSKOSCollection(someResource)
                || jenaResourceIsSKOSConceptScheme(someResource));
    }
    
    /**
     * Checks whether the Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * is a {@link ie.cmrc.skos.core.SKOSCollectionMember})
     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * instance
     * @return {@code true} if {@code someResource} is not {@code null} and is asserted
     * to be a SKOS Concept orCollection; {@code false} otherwise
     */
    public static boolean jenaResourceIsSKOSResourceElement(Resource someResource) {
        return (jenaResourceIsSKOSConcept(someResource)
                || jenaResourceIsSKOSCollection(someResource));
    }
    
    /**
     * Checks whether the Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * is a {@link ie.cmrc.skos.core.SKOSConcept})
     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * instance
     * @return {@code true} if {@code someResource} is not {@code null} and is asserted
     * to have as a type skos:Concept; {@code false} otherwise
     */
    public static boolean jenaResourceIsSKOSConcept(Resource someResource) {
        if (someResource!=null) {
            return someResource.hasProperty(RDF.type, TypeResourceFactory.CONCEPT);
        }
        return false;
    }
    
    /**
     * Checks whether the Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * is a {@link ie.cmrc.skos.core.SKOSConcept})
     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * instance
     * @return {@code true} if {@code someResource} is not {@code null} and is asserted
     * to have as a type skos:Concept; {@code false} otherwise
     */
    public static boolean jenaResourceIsSKOSConceptScheme(Resource someResource) {
        if (someResource!=null) {
            return someResource.hasProperty(RDF.type, TypeResourceFactory.CONCEPT_SCHEME);
        }
        return false;
    }
    
    /**
     * Checks whether the Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * can be converted to a {@link ie.cmrc.skos.core.SKOSCollection})
     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * instance
     * @return {@code true} if {@code someResource} is not {@code null} and is asserted
     * to have as a type skos:Collection; {@code false} otherwise
     */
    public static boolean jenaResourceIsSKOSCollection(Resource someResource) {
        if (someResource!=null) {
            return someResource.hasProperty(RDF.type, TypeResourceFactory.COLLECTION);
        }
        return false;
    }
    
//    /**
//     * Checks whether the Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
//     * can be converted to a {@link ie.cmrc.skos.core.SKOSCollectionMember})
//     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
//     * instance
//     * @return {@code true} if {@code someResource} is not {@code null} and is not asserted
//     * to be a skos:ConceptScheme; {@code false} otherwise
//     */
//    public static boolean jenaResourceCanAsSKOSResourceElement(Resource someResource) {
//        if (someResource!=null) {
//            return !someResource.hasProperty(RDF.type, TypeResourceFactory.CONCEPT_SCHEME);
//        }
//        return false;
//    }
//    
//    /**
//     * Checks whether the Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
//     * can be converted to a {@link ie.cmrc.skos.core.SKOSConcept})
//     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
//     * instance
//     * @return {@code true} if {@code someResource} is not {@code null} and is not asserted
//     * to have as a type skos:ConceptScheme or skos:Collection; {@code false} otherwise
//     */
//    public static boolean jenaResourceCanAsSKOSConcept(Resource someResource) {
//        if (someResource!=null) {
//            return (!someResource.hasProperty(RDF.type, TypeResourceFactory.COLLECTION)
//                    && !someResource.hasProperty(RDF.type, TypeResourceFactory.CONCEPT_SCHEME));
//        }
//        return false;
//    }
//    
//    /**
//     * Checks whether the Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
//     * can be converted to a {@link ie.cmrc.skos.core.SKOSConcept})
//     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
//     * instance
//     * @return {@code true} if {@code someResource} is not {@code null} and is not asserted
//     * to have as a type skos:Concept or skos:Collection; {@code false} otherwise
//     */
//    public static boolean jenaResourceCanAsSKOSConceptScheme(Resource someResource) {
//        if (someResource!=null) {
//            return (!someResource.hasProperty(RDF.type, TypeResourceFactory.COLLECTION)
//                    && !someResource.hasProperty(RDF.type, TypeResourceFactory.CONCEPT));
//        }
//        return false;
//    }
//    
//    /**
//     * Checks whether the Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
//     * can be converted to a {@link ie.cmrc.skos.core.SKOSCollection})
//     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
//     * instance
//     * @return {@code true} if {@code someResource} is not {@code null} and is not asserted
//     * to have as a type skos:ConceptScheme or skos:Concept; {@code false} otherwise
//     */
//    public static boolean jenaResourceCanAsSKOSCollection(Resource someResource) {
//        if (someResource!=null) {
//            return (!someResource.hasProperty(RDF.type, TypeResourceFactory.CONCEPT)
//                    && !someResource.hasProperty(RDF.type, TypeResourceFactory.CONCEPT_SCHEME));
//        }
//        return false;
//    }
    
    /**
     * Constructs a SKOSConcept using the provided Jena Resource if possible 
     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * instance
     * @return {@link ie.cmrc.skos.core.SKOSConcept} matching the provided Jena resource if
     * possible (i.e., the provided resource is not null and is of type skos:Concept),
     * otherwise {@code null}
     */
    public static JenaSKOSConcept skosConceptFromJenaResource(Resource someResource) {
        if (jenaResourceIsSKOSConcept(someResource)) {
            return new JenaSKOSConcept(someResource);
        }
        else return null;
    }
    
    /**
     * Constructs a SKOSCollection using the provided Jena Resource if possible 
     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * instance
     * @return {@link ie.cmrc.skos.core.SKOSCollection} matching the provided Jena resource if
     * possible (i.e., the provided resource is not null and is of type skos:Collection),
     * otherwise {@code null}
     */
    public static JenaSKOSCollection skosCollectionFromJenaResource(Resource someResource) {
        if (jenaResourceIsSKOSCollection(someResource)) {
            return new JenaSKOSCollection(someResource);
        }
        else return null;
    }
    
    /**
     * Constructs a SKOSConceptScheme using the provided Jena Resource if possible 
     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * instance
     * @return {@link ie.cmrc.skos.core.SKOSConceptScheme} matching the provided Jena resource if
     * possible (i.e., the provided resource is not null and is of type skos:ConceptScheme),
     * otherwise {@code null}
     */
    public static JenaSKOSConceptScheme skosConcepSchemeFromJenaResource(Resource someResource) {
        if (jenaResourceIsSKOSConceptScheme(someResource)) {
            return new JenaSKOSConceptScheme(someResource);
        }
        else return null;
    }
    
    /**
     * Constructs a SKOSResourceElement using the provided Jena Resource if possible 
     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * instance
     * @return {@link ie.cmrc.skos.core.SKOSCollectionMember} matching the provided Jena resource if
     * possible (i.e., the provided resource is not null and is of type skos:Concept,
     * or skos:Collection), otherwise {@code null}
     */
    public static JenaSKOSCollectionMember skosResourceElementFomJenaResource(Resource someResource) {
        if (jenaResourceIsSKOSConcept(someResource)) return new JenaSKOSConcept(someResource);
        else if (jenaResourceIsSKOSCollection(someResource)) return new JenaSKOSCollection(someResource);
        else return null;
    }
    
    /**
     * Constructs a SKOSResource using the provided Jena Resource if possible 
     * @param someResource A Jena resource ({@code com.hp.hpl.jena.rdf.model.Resource})
     * instance
     * @return {@link ie.cmrc.skos.core.SKOSResource} matching the provided Jena resource if
     * possible (i.e., the provided resource is not null and is of type skos:Concept,
     * skos:ConceptScheme or skos:Collection), otherwise {@code null}
     */
    public static JenaSKOSResource fromJenaResource(Resource someResource) {
        if (jenaResourceIsSKOSConcept(someResource)) return new JenaSKOSConcept(someResource);
        else if (jenaResourceIsSKOSCollection(someResource)) return new JenaSKOSCollection(someResource);
        else if (jenaResourceIsSKOSConceptScheme(someResource)) return new JenaSKOSConceptScheme(someResource);
        else return null;
    }
    
//    protected StmtIterator getRelationIter(SKOSObjectProperty skosObjectProperty, String conceptSchemeURI, String collectionURI) {
//        if (skosObjectProperty != null) {
//            Property property = PropertyFactory.create(skosObjectProperty);
//            StmtFilter csFilter = null;
//            if (conceptSchemeURI!=null && !conceptSchemeURI.isEmpty()) {
//                Resource csRes = this.getModel().getResource(conceptSchemeURI);
//                if (csRes != null) {
//                    Property inSchemeProp = PropertyFactory.inScheme;
//                    csFilter = new StmtObjectFilter(inSchemeProp, csRes);
//                }
//                else return null;
//            }
//            StmtFilter collFilter = null;
//            if (collectionURI!=null && !collectionURI.isEmpty()) {
//                Resource collRes = this.getModel().getResource(collectionURI);
//                if (collRes != null) {
//                    collFilter = new StmtObjectIsPropertyOfFilter(collRes, PropertyFactory.member);
//                }
//                else return null;
//            }
//            
//            StmtFilter stmtFilter = null;
//            
//            if (csFilter != null) {
//                if (collFilter != null) {
//                    stmtFilter = new AndStmtFilter(csFilter);
//                    ((AndStmtFilter)stmtFilter).add(collFilter);
//                }
//                else {
//                    stmtFilter = csFilter;
//                }
//            }
//            else {
//                if (collFilter != null) {
//                    stmtFilter = collFilter;
//                }
//            }
//            
//            Selector selector;
//            if (stmtFilter != null) selector = new FilteredSelector(this.resource, property, (RDFNode)null, stmtFilter);
//            else selector = new SimpleSelector(this.resource, property, (RDFNode)null);
//            
//            return this.getModel().listStatements(selector);
//        }
//        else throw new IllegalArgumentException("SKOSObjectProperty must not be null!");
//    }
//    
//    protected StmtIterator getRelationIter(SKOSObjectProperty skosObjectProperty, Collection<String> conceptSchemeURIs, Collection<String> collectionURIs) {
//        if (skosObjectProperty != null) {
//            Property property = PropertyFactory.create(skosObjectProperty);
//            StmtFilter csFilter = null;
//            if (conceptSchemeURIs!=null && !conceptSchemeURIs.isEmpty()) {
//                csFilter = new OrStmtFilter();
//                for (String conceptSchemeURI: conceptSchemeURIs) {
//                    Resource csRes = this.getModel().getResource(conceptSchemeURI);
//                    if (csRes != null) {
//                        Property inSchemeProp = PropertyFactory.inScheme;
//                        ((OrStmtFilter)csFilter).add(new StmtObjectFilter(inSchemeProp, csRes));
//                    }
//                    else return null;
//                }
//            }
//            StmtFilter collFilter = null;
//            if (collectionURIs!=null && !collectionURIs.isEmpty()) {
//                collFilter = new OrStmtFilter();
//                for (String collectionURI: collectionURIs) {
//                    Resource collRes = this.getModel().getResource(collectionURI);
//                    if (collRes != null) {
//                        ((OrStmtFilter)collFilter).add(new StmtObjectIsPropertyOfFilter(collRes, PropertyFactory.member));
//                    }
//                    else return null;
//                }
//            }
//            
//            StmtFilter stmtFilter = null;
//            
//            if (csFilter != null) {
//                if (collFilter != null) {
//                    stmtFilter = new AndStmtFilter(csFilter);
//                    ((AndStmtFilter)stmtFilter).add(collFilter);
//                }
//                else {
//                    stmtFilter = csFilter;
//                }
//            }
//            else {
//                if (collFilter != null) {
//                    stmtFilter = collFilter;
//                }
//            }
//            
//            Selector selector;
//            if (stmtFilter != null) selector = new FilteredSelector(this.resource, property, (RDFNode)null, stmtFilter);
//            else selector = new SimpleSelector(this.resource, property, (RDFNode)null);
//            
//            return this.getModel().listStatements(selector);
//        }
//        else throw new IllegalArgumentException("SKOSObjectProperty must not be null!");
//    }
//    
//    
//    protected StmtIterator getRelationshipSourceIter(SKOSObjectProperty skosObjectProperty, String conceptSchemeURI, String collectionURI) {
//        if (skosObjectProperty != null) {
//            Property property = PropertyFactory.create(skosObjectProperty);
//            StmtFilter csFilter = null;
//            if (conceptSchemeURI!=null && !conceptSchemeURI.isEmpty()) {
//                Resource csRes = this.getModel().getResource(conceptSchemeURI);
//                if (csRes != null) {
//                    Property inSchemeProp = PropertyFactory.inScheme;
//                    csFilter = new StmtSubjectFilter(inSchemeProp, csRes);
//                }
//                else return null;
//            }
//            StmtFilter collFilter = null;
//            if (collectionURI!=null && !collectionURI.isEmpty()) {
//                Resource collRes = this.getModel().getResource(collectionURI);
//                if (collRes != null) {
//                    collFilter = new StmtSubjectIsPropertyOfFilter(collRes, PropertyFactory.member);
//                }
//                else return null;
//            }
//            
//            StmtFilter stmtFilter = null;
//            
//            if (csFilter != null) {
//                if (collFilter != null) {
//                    stmtFilter = new AndStmtFilter(csFilter);
//                    ((AndStmtFilter)stmtFilter).add(collFilter);
//                }
//                else {
//                    stmtFilter = csFilter;
//                }
//            }
//            else {
//                if (collFilter != null) {
//                    stmtFilter = collFilter;
//                }
//            }
//            
//            Selector selector;
//            if (stmtFilter != null) selector = new FilteredSelector((Resource)null, property, this.resource, stmtFilter);
//            else selector = new SimpleSelector(this.resource, property, (RDFNode)null);
//            
//            return this.getModel().listStatements(selector);
//        }
//        else throw new IllegalArgumentException("SKOSObjectProperty must not be null!");
//    }
//    
//    protected StmtIterator getRelationshipSourceIter(SKOSObjectProperty skosObjectProperty, Collection<String> conceptSchemeURIs, Collection<String> collectionURIs) {
//        if (skosObjectProperty != null) {
//            Property property = PropertyFactory.create(skosObjectProperty);
//            StmtFilter csFilter = null;
//            if (conceptSchemeURIs!=null && !conceptSchemeURIs.isEmpty()) {
//                csFilter = new OrStmtFilter();
//                for (String conceptSchemeURI: conceptSchemeURIs) {
//                    Resource csRes = this.getModel().getResource(conceptSchemeURI);
//                    if (csRes != null) {
//                        Property inSchemeProp = PropertyFactory.inScheme;
//                        ((OrStmtFilter)csFilter).add(new StmtSubjectFilter(inSchemeProp, csRes));
//                    }
//                    else return null;
//                }
//            }
//            StmtFilter collFilter = null;
//            if (collectionURIs!=null && !collectionURIs.isEmpty()) {
//                collFilter = new OrStmtFilter();
//                for (String collectionURI: collectionURIs) {
//                    Resource collRes = this.getModel().getResource(collectionURI);
//                    if (collRes != null) {
//                        ((OrStmtFilter)collFilter).add(new StmtSubjectIsPropertyOfFilter(collRes, PropertyFactory.member));
//                    }
//                    else return null;
//                }
//            }
//            
//            StmtFilter stmtFilter = null;
//            
//            if (csFilter != null) {
//                if (collFilter != null) {
//                    stmtFilter = new AndStmtFilter(csFilter);
//                    ((AndStmtFilter)stmtFilter).add(collFilter);
//                }
//                else {
//                    stmtFilter = csFilter;
//                }
//            }
//            else {
//                if (collFilter != null) {
//                    stmtFilter = collFilter;
//                }
//            }
//            
//            Selector selector;
//            if (stmtFilter != null) selector = new FilteredSelector((Resource)null, property, this.resource, stmtFilter);
//            else selector = new SimpleSelector(this.resource, property, (RDFNode)null);
//            
//            return this.getModel().listStatements(selector);
//        }
//        else throw new IllegalArgumentException("SKOSObjectProperty must not be null!");
//    }
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // Implementing the Jena Resource interface (all the implemented methods
    // are delegated to the inner Resource object)
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    
    @Override
    public AnonId getId() {
        return resource.getId();
    }

    @Override
    public Resource inModel(Model m) {
        return resource.inModel(m);
    }

    @Override
    public boolean hasURI(String uri) {
        return resource.hasURI(uri);
    }

    @Override
    public String toString() {
        return resource.toString();
    }

    @Override
    public Statement getRequiredProperty(Property p) {
        return resource.getRequiredProperty(p);
    }

    @Override
    public Statement getProperty(Property p) {
        return resource.getProperty(p);
    }

    @Override
    public StmtIterator listProperties(Property p) {
        return resource.listProperties(p);
    }

    @Override
    public StmtIterator listProperties() {
        return resource.listProperties();
    }

    @Override
    public Resource addLiteral(Property p, boolean o) {
        return resource.addLiteral(p, o);
    }

    @Override
    public Resource addLiteral(Property p, long o) {
        return resource.addLiteral(p, o);
    }

    @Override
    public Resource addLiteral(Property p, char o) {
        return resource.addLiteral(p, o);
    }

    @Override
    public Resource addLiteral(Property value, double d) {
        return resource.addLiteral(value, d);
    }

    @Override
    public Resource addLiteral(Property value, float d) {
        return resource.addLiteral(value, d);
    }

    @Override
    public Resource addLiteral(Property p, Object o) {
        return resource.addLiteral(p, o);
    }

    @Override
    public Resource addLiteral(Property p, Literal o) {
        return resource.addLiteral(p, o);
    }

    @Override
    public Resource addProperty(Property p, String o) {
        return resource.addProperty(p, o);
    }

    @Override
    public Resource addProperty(Property p, String o, String l) {
        return resource.addProperty(p, o, l);
    }

    @Override
    public Resource addProperty(Property p, String lexicalForm, RDFDatatype datatype) {
        return resource.addProperty(p, lexicalForm, datatype);
    }

    @Override
    public Resource addProperty(Property p, RDFNode o) {
        return resource.addProperty(p, o);
    }

    @Override
    public boolean hasProperty(Property p) {
        return resource.hasProperty(p);
    }

    @Override
    public boolean hasLiteral(Property p, boolean o) {
        return resource.hasLiteral(p, o);
    }

    @Override
    public boolean hasLiteral(Property p, long o) {
        return resource.hasLiteral(p, o);
    }

    @Override
    public boolean hasLiteral(Property p, char o) {
        return resource.hasLiteral(p, o);
    }

    @Override
    public boolean hasLiteral(Property p, double o) {
        return resource.hasLiteral(p, o);
    }

    @Override
    public boolean hasLiteral(Property p, float o) {
        return resource.hasLiteral(p, o);
    }

    @Override
    public boolean hasLiteral(Property p, Object o) {
        return resource.hasLiteral(p, o);
    }

    @Override
    public boolean hasProperty(Property p, String o) {
        return resource.hasProperty(p, o);
    }

    @Override
    public boolean hasProperty(Property p, String o, String l) {
        return resource.hasProperty(p, o, l);
    }

    @Override
    public boolean hasProperty(Property p, RDFNode o) {
        return resource.hasProperty(p, o);
    }

    @Override
    public Resource removeProperties() {
        return resource.removeProperties();
    }

    @Override
    public Resource removeAll(Property p) {
        return resource.removeAll(p);
    }

    @Override
    public Resource begin() {
        return resource.begin();
    }

    @Override
    public Resource abort() {
        return resource.abort();
    }

    @Override
    public Resource commit() {
        return resource.commit();
    }

    @Override
    public Resource getPropertyResourceValue(Property p) {
        return resource.getPropertyResourceValue(p);
    }

    @Override
    public boolean isAnon() {
        return resource.isAnon();
    }

    @Override
    public boolean isLiteral() {
        return resource.isLiteral();
    }

    @Override
    public boolean isURIResource() {
        return resource.isURIResource();
    }

    @Override
    public boolean isResource() {
        return resource.isResource();
    }

    @Override
    public <T extends RDFNode> T as(Class<T> view) {
        return resource.as(view);
    }

    @Override
    public <T extends RDFNode> boolean canAs(Class<T> view) {
        return resource.canAs(view);
    }

    @Override
    public Model getModel() {
        return resource.getModel();
    }

    @Override
    public Object visitWith(RDFVisitor rv) {
        return resource.visitWith(rv);
    }

    @Override
    public Resource asResource() {
        return resource.asResource();
    }

    @Override
    public Literal asLiteral() {
        return resource.asLiteral();
    }

    @Override
    public Node asNode() {
        return resource.asNode();
    }
    
    
}
