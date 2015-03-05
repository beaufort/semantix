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

package ie.cmrc.smtx.skos.jena.util;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import ie.cmrc.smtx.skos.model.SKOSCollection;
import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSConceptScheme;
import ie.cmrc.smtx.skos.model.SKOSResource;
import ie.cmrc.smtx.skos.model.SKOSCollectionMember;
import ie.cmrc.smtx.skos.jena.JenaSKOSCollection;
import ie.cmrc.smtx.skos.jena.JenaSKOSConcept;
import ie.cmrc.smtx.skos.jena.JenaSKOSConceptScheme;
import ie.cmrc.smtx.skos.jena.JenaSKOSResource;
import ie.cmrc.smtx.skos.jena.JenaSKOSCollectionMember;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;

/**
 * A factory class for creating {@link ie.cmrc.skos.core.SKOSResource} iterators
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class SKOSResourceIterFactory {
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSResource} iterator ({@code CloseableIterator<SKOSResource>})
     * that wraps the subjects of the elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.StmtIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSResource} iterator
     * @return ({@code CloseableIterator<SKOSResource>}) over the subjects of the
     * elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     */
    public static CloseableIterator<SKOSResource> makeSKOSResourceIterOverSubjects(final StmtIterator iter) {
        return (new CloseableIterator<SKOSResource>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSResource next() {
                if (isOpen && iter!=null && iter.hasNext()) {

                    Statement statement = iter.next();
                    Resource resource = statement.getSubject();
                    SKOSResource skosResource = JenaSKOSResource.fromJenaResource(resource);

                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosResource;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSConceptScheme} iterator ({@code CloseableIterator<SKOSConceptScheme>})
     * that wraps the subjects of the elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.StmtIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSConceptScheme} iterator
     * @return ({@code CloseableIterator<SKOSConceptScheme>}) over the subjects of the
     * elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     */
    public static CloseableIterator<SKOSConceptScheme> makeSKOSConceptSchemeIterOverSubjects(final StmtIterator iter) {
        return (new CloseableIterator<SKOSConceptScheme>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSConceptScheme next() {
                if (isOpen && iter!=null && iter.hasNext()) {

                    Statement statement = iter.next();
                    Resource resource = statement.getSubject();
                    SKOSConceptScheme skosConceptScheme = new JenaSKOSConceptScheme(resource);

                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosConceptScheme;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSCollectionMember} iterator ({@code CloseableIterator<SKOSResourceElement>})
     * that wraps the subjects of the elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.StmtIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSCollectionMember} iterator
     * @return ({@code CloseableIterator<SKOSResourceElement>}) over the subjects of the
     * elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     */
    public static CloseableIterator<SKOSCollectionMember> makeSKOSCollectionMemberIterOverSubjects(final StmtIterator iter) {
        return (new CloseableIterator<SKOSCollectionMember>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSCollectionMember next() {
                if (isOpen && iter!=null && iter.hasNext()) {

                    Statement statement = iter.next();
                    Resource resource = statement.getSubject();
                    SKOSCollectionMember skosResourceElt = JenaSKOSCollectionMember.skosResourceElementFomJenaResource(resource);
                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosResourceElt;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSConcept} iterator ({@code CloseableIterator<SKOSConcept>})
     * that wraps the subjects of the elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.StmtIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSConcept} iterator
     * @return ({@code CloseableIterator<SKOSConcept>}) over the subjects of the
     * elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     */
    public static CloseableIterator<SKOSConcept> makeSKOSConceptIterOverSubjects(final StmtIterator iter) {
        return (new CloseableIterator<SKOSConcept>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSConcept next() {
                if (isOpen && iter!=null && iter.hasNext()) {

                    Statement statement = iter.next();
                    Resource resource = statement.getSubject();
                    SKOSConcept skosConcept = new JenaSKOSConcept(resource);

                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosConcept;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSCollection} iterator ({@code CloseableIterator<SKOSCollection>})
     * that wraps the subjects of the elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.StmtIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSCollection} iterator
     * @return ({@code CloseableIterator<SKOSCollection>}) over the subjects of the
     * elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     */
    public static CloseableIterator<SKOSCollection> makeSKOSCollectionIterOverSubjects(final StmtIterator iter) {
        return (new CloseableIterator<SKOSCollection>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSCollection next() {
                if (isOpen && iter!=null && iter.hasNext()) {

                    Statement statement = iter.next();
                    Resource resource = statement.getSubject();
                    SKOSCollection skosCollection = new JenaSKOSCollection(resource);

                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosCollection;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSResource} iterator ({@code CloseableIterator<SKOSResource>})
     * that wraps the objects of the elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.StmtIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSResource} iterator
     * @return ({@code CloseableIterator<SKOSResource>}) over the objects of the
     * elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     */
    public static CloseableIterator<SKOSResource> makeSKOSResourceIterOverObjects(final StmtIterator iter) {
        return (new CloseableIterator<SKOSResource>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSResource next() {
                if (isOpen && iter!=null && iter.hasNext()) {
                    SKOSResource skosResource = null;
                    Statement statement = iter.next();
                    RDFNode object = statement.getObject();
                    if (object.isResource()) {
                        Resource resource = object.asResource();
                        skosResource = JenaSKOSResource.fromJenaResource(resource);
                    }
                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosResource;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSConceptScheme} iterator ({@code CloseableIterator<SKOSConceptScheme>})
     * that wraps the objects of the elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.StmtIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSConceptScheme} iterator
     * @return ({@code CloseableIterator<SKOSConceptScheme>}) over the objects of the
     * elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     */
    public static CloseableIterator<SKOSConceptScheme> makeSKOSConceptSchemeIterOverObjects(final StmtIterator iter) {
        return (new CloseableIterator<SKOSConceptScheme>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSConceptScheme next() {
                if (isOpen && iter!=null && iter.hasNext()) {
                    SKOSConceptScheme skosConceptScheme = null;
                    Statement statement = iter.next();
                    RDFNode object = statement.getObject();
                    if (object.isResource()) {
                        Resource resource = object.asResource();
                        skosConceptScheme = new JenaSKOSConceptScheme(resource);
                    }
                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosConceptScheme;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSCollectionMember} iterator ({@code CloseableIterator<SKOSResourceElement>})
     * that wraps the objects of the elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.StmtIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSCollectionMember} iterator
     * @return ({@code CloseableIterator<SKOSResourceElement>}) over the objects of the
     * elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     */
    public static CloseableIterator<SKOSCollectionMember> makeSKOSCollectionMemberIterOverObjects(final StmtIterator iter) {
        return (new CloseableIterator<SKOSCollectionMember>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSCollectionMember next() {
                if (isOpen && iter!=null && iter.hasNext()) {
                    SKOSCollectionMember skosResourceElement = null;
                    Statement statement = iter.next();
                    RDFNode object = statement.getObject();
                    if (object.isResource()) {
                        Resource resource = object.asResource();
                        skosResourceElement = JenaSKOSCollectionMember.skosResourceElementFomJenaResource(resource);
                    }
                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosResourceElement;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSConcept} iterator ({@code CloseableIterator<SKOSConcept>})
     * that wraps the objects of the elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.StmtIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSConcept} iterator
     * @return ({@code CloseableIterator<SKOSConcept>}) over the objects of the
     * elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     */
    public static CloseableIterator<SKOSConcept> makeSKOSConceptIterOverObjects(final StmtIterator iter) {
        return (new CloseableIterator<SKOSConcept>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSConcept next() {
                if (isOpen && iter!=null && iter.hasNext()) {
                    SKOSConcept skosConcept = null;
                    Statement statement = iter.next();
                    RDFNode object = statement.getObject();
                    if (object.isResource()) {
                        Resource resource = object.asResource();
                        skosConcept = new JenaSKOSConcept(resource);
                    }
                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosConcept;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSCollection} iterator ({@code CloseableIterator<SKOSCollection>})
     * that wraps the objects of the elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.StmtIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSCollection} iterator
     * @return ({@code CloseableIterator<SKOSCollection>}) over the objects of the
     * elements of the provided {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     */
    public static CloseableIterator<SKOSCollection> makeSKOSCollectionIterOverObjects(final StmtIterator iter) {
        return (new CloseableIterator<SKOSCollection>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSCollection next() {
                if (isOpen && iter!=null && iter.hasNext()) {
                    SKOSCollection skosCollection = null;
                    Statement statement = iter.next();
                    RDFNode object = statement.getObject();
                    if (object.isResource()) {
                        Resource resource = object.asResource();
                        skosCollection = new JenaSKOSCollection(resource);
                    }
                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosCollection;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    
    
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSResource} iterator ({@code CloseableIterator<SKOSResource>})
     * that wraps the provided {@link JenaResourceIterator}
     * @param iter {@code JenaResourceIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSResource} iterator
     * @return ({@code CloseableIterator<SKOSResource>}) over the
     * elements of the provided {@link JenaResourceIterator}
     */
    public static CloseableIterator<SKOSResource> makeSKOSResourceIter(final JenaResourceIterator iter) {
        return (new CloseableIterator<SKOSResource>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSResource next() {
                if (isOpen && iter!=null && iter.hasNext()) {

                    Resource resource = iter.next();
                    SKOSResource skosResource = JenaSKOSResource.fromJenaResource(resource);

                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosResource;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSConceptScheme} iterator ({@code CloseableIterator<SKOSConceptScheme>})
     * that wraps the provided {@link JenaResourceIterator}
     * @param iter {@link JenaResourceIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSConceptScheme} iterator
     * @return ({@code CloseableIterator<SKOSConceptScheme>}) over the
     * elements of the provided {@link JenaResourceIterator}
     */
    public static CloseableIterator<SKOSConceptScheme> makeSKOSConceptSchemeIter(final JenaResourceIterator iter) {
        return (new CloseableIterator<SKOSConceptScheme>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSConceptScheme next() {
                if (isOpen && iter!=null && iter.hasNext()) {

                    Resource resource = iter.next();
                    SKOSConceptScheme skosConceptScheme = new JenaSKOSConceptScheme(resource);

                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosConceptScheme;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSCollectionMember} iterator ({@code CloseableIterator<SKOSResourceElement>})
     * that wraps the provided {@link JenaResourceIterator}
     * @param iter {@link JenaResourceIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSCollectionMember} iterator
     * @return ({@code CloseableIterator<SKOSResourceElement>}) over the
     * elements of the provided {@link JenaResourceIterator}
     */
    public static CloseableIterator<SKOSCollectionMember> makeSKOSCollectionMemberIter(final JenaResourceIterator iter) {
        return (new CloseableIterator<SKOSCollectionMember>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSCollectionMember next() {
                if (isOpen && iter!=null && iter.hasNext()) {

                    Resource resource = iter.next();
                    SKOSCollectionMember skosResourceElt = JenaSKOSCollectionMember.skosResourceElementFomJenaResource(resource);
                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosResourceElt;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSConcept} iterator ({@code CloseableIterator<SKOSConcept>})
     * that wraps the provided {@link JenaResourceIterator}
     * @param iter {@link JenaResourceIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSConcept} iterator
     * @return ({@code CloseableIterator<SKOSConcept>}) over the subjects of the
     * elements of the provided {@link JenaResourceIterator}
     */
    public static CloseableIterator<SKOSConcept> makeSKOSConceptIter(final JenaResourceIterator iter) {
        return (new CloseableIterator<SKOSConcept>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSConcept next() {
                if (isOpen && iter!=null && iter.hasNext()) {

                    Resource resource = iter.next();
                    SKOSConcept skosConcept = new JenaSKOSConcept(resource);

                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosConcept;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.SKOSCollection} iterator ({@code CloseableIterator<SKOSConcept>})
     * that wraps the provided {@link JenaResourceIterator}
     * @param iter {@link JenaResourceIterator} to wrap as a {@link ie.cmrc.skos.core.SKOSCollection} iterator
     * @return ({@code CloseableIterator<SKOSCollection>}) over the subjects of the
     * elements of the provided {@link JenaResourceIterator}
     */
    public static CloseableIterator<SKOSCollection> makeSKOSCollectionIter(final JenaResourceIterator iter) {
        return (new CloseableIterator<SKOSCollection>() {

            boolean isOpen= true;

            @Override
            public boolean hasNext() {
                return (isOpen && iter!=null && iter.hasNext());
            }

            @Override
            public SKOSCollection next() {
                if (isOpen && iter!=null && iter.hasNext()) {

                    Resource resource = iter.next();
                    SKOSCollection skosCollection = new JenaSKOSCollection(resource);

                    if (!iter.hasNext()) {
                        isOpen = false;
                        iter.close();
                    }
                    return skosCollection;

                }
                else return null;
            }

            @Override
            public void remove() {
                if (isOpen && iter!=null) iter.remove();
            }

            @Override
            public void close() {
                if (isOpen && iter!=null) iter.close();
            }
        });
    }
    
    
}
