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

import com.hp.hpl.jena.rdf.model.Statement;

/**
 * A {@code NotStmtFilter} may be used to negate a statement filter.
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class NotStmtFilter implements StmtFilter {

    /**
     * Statement filter to negate
     */
    final protected StmtFilter filterToNegate;
    
    /**
     * Constructs a {@link NotStmtFilter} that negates the provided statement filter
     * @param filter Statement filter to negate
     */
    public NotStmtFilter(StmtFilter filter) {
        this.filterToNegate = filter;
    }
    
    /**
     * Checks whether the provided statement satisfies the conditions of this
     * {@link NotStmtFilter}
     * @param statement {@inheritDoc}
     * @return {@code true} if {@code statement} does not satisfy the condition
     * of the negated filter, {@code false} otherwise
     */
    @Override
    public boolean test(Statement statement) {
        return !this.filterToNegate.test(statement);
    }
    
}
