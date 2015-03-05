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
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import ie.cmrc.smtx.skos.model.SKOSType;

/**
 * Utility class that provides useful static SKOS type resources
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class TypeResourceFactory {
    
    /**
     * Creates a Jena {@code com.hp.hpl.jena.rdf.model.Resource} for the provided {@link ie.cmrc.skos.core.SKOSType}
     * @param skosType {@link ie.cmrc.skos.core.SKOSType} instance
     * @return Jena {@code com.hp.hpl.jena.rdf.model.Resource} representing the provided {@link ie.cmrc.skos.core.SKOSType}
     */
    public static final Resource create(SKOSType skosType) {
        return ResourceFactory.createResource(skosType.uri());
    }
    
    // ******** ******** ******** ******** ******** ******** ******** ********
    // SKOS Types
    // ******** ******** ******** ******** ******** ******** ******** ********
    
    /**
     * SKOS Concept type resource
     */
    public static final Resource CONCEPT = create(SKOSType.Concept);
    
    /**
     * SKOS ConceptScheme type resource
     */
    public static final Resource CONCEPT_SCHEME = create(SKOSType.ConceptScheme);
    
    /**
     * SKOS Collection type resource
     */
    public static final Resource COLLECTION = create(SKOSType.Collection);
}
