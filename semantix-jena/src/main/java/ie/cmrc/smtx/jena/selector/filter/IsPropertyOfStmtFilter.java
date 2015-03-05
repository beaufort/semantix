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
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public abstract class IsPropertyOfStmtFilter implements StmtFilter {
    /**
     * The predicate constraint of this subject filter
     */
    protected Property predicate;
    
    /**
     * The resource constraint of this subject filter
     */
    protected Resource  subject;
    
    /**
     * Creates an {@link IsPropertyOfStmtFilter}. Since no subject or predicate
     * constraints are specified, all statements will be accepted.
     */
   public  IsPropertyOfStmtFilter() {
        predicate = null;
        subject = null;
    }
    
    /**
     * Create an {@link IsPropertyOfStmtFilter} with the provided subject
     * and predicate. 
     * @param predicate Predicate constraint of the filter.
     * @param subject Subject constraint of the filter.
     */
    public IsPropertyOfStmtFilter(Resource subject, Property predicate) {
        this.subject = subject;
        this.predicate = predicate;
    }
    
    /** Return the predicate constraint of this object filter.
     * @return the predicate constraint
     */
    public Property getPredicate() { return predicate; }
    
    /** Return the subject constraint of this object filter.
     * @return the subject constraint
     */
    public RDFNode  getSubject() { return subject; }
}
