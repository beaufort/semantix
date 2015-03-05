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
import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSConceptScheme;
import ie.cmrc.smtx.skos.model.SKOSElementProperty;
import ie.cmrc.smtx.skos.model.SKOSResource;
import ie.cmrc.smtx.skos.model.SKOSSemanticProperty;
import ie.cmrc.smtx.skos.model.SKOSType;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;

/**
 *
 * @author Yassine Lassoued
 */
public class JenaSKOSConcept extends JenaSKOSCollectionMember implements SKOSConcept {

    /**
     * Constructs a {@code JenaSKOSConcept} using the provided Jena resource
     * {@code com.hp.hpl.jena.rdf.model.Resource}
     * @param res Jena {@code com.hp.hpl.jena.rdf.model.Resource} to expose as
     * a SKOSConcept
     */
    public JenaSKOSConcept(Resource res) {
        super(res);
    }

    /**
     * {@inheritDoc}
     * @return {@code SKOSType.Concept}
     */
    @Override
    public SKOSType getSkosType() {
        return SKOSType.Concept;
    }

    /**
     * {@inheritDoc}
     * @param conceptSchemeURI {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SKOSConcept makeTopConcept(String conceptSchemeURI) throws IllegalArgumentException {
        if (conceptSchemeURI!=null && !conceptSchemeURI.isEmpty()) {
            SKOSResource skosResource = this.getSKOS().getSKOSResource(conceptSchemeURI);
            if (skosResource != null) {
                if (skosResource.isSKOSConceptScheme()) {
                    this.addRelation(SKOSElementProperty.topConceptOf, skosResource);
                }
                else throw new IllegalArgumentException("\""+conceptSchemeURI+"\" is not the URI of a SKOS Concept Scheme");
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
    public SKOSConcept makeTopConcept(SKOSConceptScheme conceptScheme) {
        if (conceptScheme != null) {
            this.addRelation(SKOSElementProperty.topConceptOf, conceptScheme);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listSemanticRelations(SKOSSemanticProperty relationshipType) {
        return this.getSKOS().listSemanticRelations(this, relationshipType);
    }

    /**
     * {@inheritDoc}
     * @param relationshipType {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public CloseableIterator<SKOSConcept> listSemanticRelationshipSources(SKOSSemanticProperty relationshipType) {
        return this.getSKOS().listSemanticRelationshipSources(relationshipType, this);
    }
    
    
}
