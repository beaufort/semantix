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

package ie.cmrc.smtx.jena.selector.filter;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 *
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public abstract class HasPropertyStmtFilter implements StmtFilter {
    /**
     * The predicate constraint of this subject filter
     */
    protected Property predicate;
    
    /**
     * The object constraint of this subject filter
     */
    protected RDFNode  object;
    
    /**
     * Creates a {@link HasPropertyStmtFilter}. Since no predicate or object constraints are
     * specified all statements will be accepted.
     */
   public  HasPropertyStmtFilter() {
        predicate = null;
        object = null;
    }
    
    /**
     * Create a {@link HasPropertyStmtFilter} with the provided object and predicate. 
     * @param predicate Predicate of the filter
     * @param object Object of the filter
     */
    public HasPropertyStmtFilter(Property predicate, RDFNode object) {
        this.predicate = predicate;
        this.object = object;
    }
    
    /** Return the predicate constraint of this object filter.
     * @return the predicate constraint
     */
    public Property getPredicate() { return predicate; }
    
    /** Return the object constraint of this object filter.
     * @return the object constraint
     */
    public RDFNode  getObject() { return object; }
}
