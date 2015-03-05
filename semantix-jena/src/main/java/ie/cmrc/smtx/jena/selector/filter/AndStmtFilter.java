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
 * A {@link BooleanOpStmtFilter} that consists of a conjunction of statement
 * filters (members). An AndStmtFilter accepts a statement if it is accepted by all
 * its members.
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class AndStmtFilter extends BooleanOpStmtFilter {

    /**
     * Default constructor. This creates an empty filter that accepts any statement.
     */
    public AndStmtFilter() {
        super();
    }
    
    /**
     * Constructs an {@link AndStmtFilter} and adds the provided filter
     * into it.
     * @param filter Statement filter to add to the {@link AndStmtFilter}
     */
    public AndStmtFilter(StmtFilter filter) {
        super(filter);
    }
    
    /**
     * Constructs a {@link AndStmtFilter} and adds all the provided filters
     * into it
     * @param filters Statement filters to wrap as an {@link AndStmtFilter}
     * @throws NullPointerException if the {@code filters == null}
     */
    public AndStmtFilter(Collection filters) {
        super(filters);
    }
    
    /**
     * Test whether the provided statement satisfies all the member filters
     * @param statement {@inheritDoc}
     * @return {@code true} if {@code statement} satisfies all the member filters,
     * {@code false} otherwise
     */
    @Override
    public boolean test(Statement statement) {
        for (StmtFilter filter: this.filters) {
            if (!filter.test(statement)) return false;
        }
        return true;
        
    }
    
}
