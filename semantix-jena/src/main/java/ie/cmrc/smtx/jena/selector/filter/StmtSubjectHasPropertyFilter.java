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
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * A statement filter that checks whether the subject ({@code stmtSubj}) of
 * a provided statement satisfies a condition, which is expressed as a
 * {@code stmtSubj predicate object} selector.
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class StmtSubjectHasPropertyFilter extends HasPropertyStmtFilter {

    /**
     * Creates a {@link StmtSubjectHasPropertyFilter}. Since no predicate or object
     * constraints are specified all statements will be accepted.
     */
    public StmtSubjectHasPropertyFilter() {
        super();
    }

    /**
     * Create a {@link StmtSubjectHasPropertyFilter} with the provided predicate
     * and object
     * @param predicate Predicate of the filter
     * @param object Object of the filter
     */
    public StmtSubjectHasPropertyFilter(Property predicate, RDFNode object) {
        super(predicate, object);
    }
    
    /**
     * {@inheritDoc}
     * @param statement {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean test(Statement statement) {
        return (statement.getSubject().hasProperty(predicate, object));
    }
    
}
