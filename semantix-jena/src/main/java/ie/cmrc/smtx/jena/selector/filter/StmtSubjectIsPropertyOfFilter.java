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
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * A statement filter that checks whether the subject ({@code stmtSubj}) of
 * a provided statement satisfies a condition, which is expressed as a
 * {@code object predicate stmtSubj} selector.
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class StmtSubjectIsPropertyOfFilter extends IsPropertyOfStmtFilter {

    /**
     * Creates a {@link StmtSubjectIsPropertyOfFilter}. Since no predicate or object
     * constraints are specified all statements will be accepted.
     */
    public StmtSubjectIsPropertyOfFilter() {
        super();
    }

    /**
     * Create a {@link StmtSubjectIsPropertyOfFilter} with the provided object and predicate.
     * @param subject subject of the filter
     * @param predicate if not null, the predicate of the tested statement
     * must equal this argument.
     */
    public StmtSubjectIsPropertyOfFilter(Resource subject, Property predicate) {
        super(subject, predicate);
    }
    
    /**
     * {@inheritDoc}
     * @param statement {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean test(Statement statement) {
        return statement.getModel().contains(this.subject, predicate, statement.getSubject());
        //return this.subject.hasProperty(predicate, statement.getSubject());
        
    }
    
}
