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
import java.util.Collection;

/**
 * A {@link BooleanOpStmtFilter} that consists of a disjunction of statement
 * filters (members). An OrStmtFilter accepts a statement if it is accepted by at
 * least one of its members.
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class OrStmtFilter extends BooleanOpStmtFilter {

    /**
     * Default constructor. This creates an empty filter that accepts any statement.
     */
    public OrStmtFilter() {
        super();
    }
    
    /**
     * Constructs an {@link OrStmtFilter} and adds the provided filter
     * into it.
     * @param filter Statement filter to add to the {@link OrStmtFilter}
     */
    public OrStmtFilter(StmtFilter filter) {
        super(filter);
    }
    
    /**
     * Constructs a {@link OrStmtFilter} and adds all the provided filters
     * into it
     * @param filters Statement filters to wrap as an {@link OrStmtFilter}
     * @throws NullPointerException if the {@code filters == null}
     */
    public OrStmtFilter(Collection filters) {
        super(filters);
    }
    
    /**
     * Test whether the provided statement satisfies at least one member filter
     * @param statement {@inheritDoc}
     * @return {@code true} if {@code statement} satisfies at least one member filter,
     * {@code false} otherwise
     */
    @Override
    public boolean test(Statement statement) {
        for (StmtFilter filter: this.filters) {
            if (filter.test(statement)) return true;
        }
        return false;
    }
    
}
