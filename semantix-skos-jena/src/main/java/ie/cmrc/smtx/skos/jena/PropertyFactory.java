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
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import ie.cmrc.smtx.skos.model.SKOSElementProperty;
import ie.cmrc.smtx.skos.model.SKOSObjectProperty;
import ie.cmrc.smtx.skos.model.SKOSSemanticProperty;

/**
 * Utility class that provides useful static SKOS object properties
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class PropertyFactory {
    
    /**
     * Creates a Jena {@code com.hp.hpl.jena.rdf.model.Property} for the provided {@link ie.cmrc.skos.core.SKOSObjectProperty}
     * @param skosObjectProperty {@link ie.cmrc.skos.core.SKOSObjectProperty} instance
     * @return Jena {@code com.hp.hpl.jena.rdf.model.Property} representing the provided {@link ie.cmrc.skos.core.SKOSObjectProperty}
     */
    public static final Property create(SKOSObjectProperty skosObjectProperty) {
        return ResourceFactory.createProperty(skosObjectProperty.uri());
    }
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // SKOS Properties
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * SKOS broader property
     */
    public static final Property broader = create(SKOSSemanticProperty.broader);
    
    /**
     * SKOS broaderTransitive property
     */
    public static final Property broaderTransitive = create(SKOSSemanticProperty.broaderTransitive);
    
    /**
     * SKOS broadMatch property
     */
    public static final Property broadMatch = create(SKOSSemanticProperty.broadMatch);
    
    /**
     * SKOS closeMatch property
     */
    public static final Property closeMatch = create(SKOSSemanticProperty.closeMatch);
    
    /**
     * SKOS exactMatch property
     */
    public static final Property exactMatch = create(SKOSSemanticProperty.exactMatch);
    
    /**
     * SKOS hasTopConcept property
     */
    public static final Property hasTopConcept = create(SKOSElementProperty.hasTopConcept);
    
    /**
     * SKOS inScheme property
     */
    public static final Property inScheme = create(SKOSElementProperty.inScheme);
    
    /**
     * SKOS mappingRelation property
     */
    public static final Property mappingRelation = create(SKOSSemanticProperty.mappingRelation);
    
    /**
     * SKOS member property
     */
    public static final Property member = create(SKOSElementProperty.member);
    
    /**
     * SKOSX memberOf property
     */
    public static final Property memberOf = create(SKOSElementProperty.memberOf);
    
    /**
     * SKOSX memberTransitive property
     */
    public static final Property memberTransitive = create(SKOSElementProperty.memberTransitive);
    
    /**
     * SKOSX memberOfTransitive property
     */
    public static final Property memberOfTransitive = create(SKOSElementProperty.memberOfTransitive);
    
    /**
     * SKOS narrower property
     */
    public static final Property narrower = create(SKOSSemanticProperty.narrower);
    
    /**
     * SKOS narrowerTransitive property
     */
    public static final Property narrowerTransitive = create(SKOSSemanticProperty.narrowerTransitive);
    
    /**
     * SKOS narrowMatch property
     */
    public static final Property narrowMatch = create(SKOSSemanticProperty.narrowMatch);
    
    /**
     * SKOS related property
     */
    public static final Property related = create(SKOSSemanticProperty.related);
    
    /**
     * SKOS relatedMatch property
     */
    public static final Property relatedMatch = create(SKOSSemanticProperty.relatedMatch);
    
    /**
     * SKOS semanticRelation property
     */
    public static final Property semanticRelation = create(SKOSSemanticProperty.semanticRelation);
    
    /**
     * SKOS topConceptOf property
     */
    public static final Property topConceptOf = create(SKOSElementProperty.topConceptOf);
}
