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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An abstract class for n-airy Boolean operator filters. A {@code BooleanOpStmtFilter}
 * applies a Boolean operator to a list of statement filters.
 * @see AndStmtFilter
 * @see OrStmtFilter
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public abstract class BooleanOpStmtFilter implements StmtFilter {
    
    /**
     * List of filters to apply the Boolean operator to
     */
    protected final List<StmtFilter> filters;
    
    /**
     * Default constructor. This creates an empty filter that accepts any statement.
     */
    public BooleanOpStmtFilter() {
        this.filters = new ArrayList<StmtFilter>();
    }
    
    /**
     * Constructs a {@link BooleanOpStmtFilter} and adds the provided filter
     * into it.
     * @param filter Statement filter to add to the {@link BooleanOpStmtFilter}
     */
    public BooleanOpStmtFilter(StmtFilter filter) {
        this.filters =new ArrayList<StmtFilter>();
        this.filters.add(filter);
    }
    
    /**
     * Constructs a {@link BooleanOpStmtFilter} and adds all the provided filters
     * into it
     * @param filters Statement filters to wrap as a {@link BooleanOpStmtFilter}
     * @throws NullPointerException if the {@code filters == null}
     */
    public BooleanOpStmtFilter(Collection filters) {
        this.filters = new ArrayList<StmtFilter>(filters);
    }

    /**
     * Checks whether the {@link BooleanOpStmtFilter} contains the provided
     * statement filter
     * @param stmtFilter {@link StmtFilter} to check
     * @return {@code true} if the {@link BooleanOpStmtFilter} contains {@code stmtFilter},
     * false otherwise
     */
    public boolean contains(StmtFilter stmtFilter) {
        return filters.contains(stmtFilter);
    }

    /**
     * Adds the provided statement filter to this {@link BooleanOpStmtFilter}
     * @param stmtFilter {@link StmtFilter} to add
     * @return {@code true} if the statement filter was actually added, false otherwise
     */
    public boolean add(StmtFilter stmtFilter) {
        return filters.add(stmtFilter);
    }

    /**
     * Removes the provided statement filter from this {@link BooleanOpStmtFilter}
     * @param stmtFilter {@link StmtFilter} to remove
     * @return {@code true} if the statement filter was actually removed, false otherwise
     */
    public boolean remove(StmtFilter stmtFilter) {
        return filters.remove(stmtFilter);
    }

    /**
     * Adds all the provided statement filters to this {@link BooleanOpStmtFilter}
     * @param stmtFilters Collection of statement filters to add
     * @return {@code true} if the statement filters were actually added, false otherwise
     */
    public boolean addAll(Collection<? extends StmtFilter> stmtFilters) {
        return filters.addAll(stmtFilters);
    }

    /**
     * Removes all the provided statement filters from this {@link BooleanOpStmtFilter}
     * @param stmtFilters Collection of statement filters to remove
     * @return {@code true} if the statement filters were actually removed, false otherwise
     */
    public boolean removeAll(Collection<? extends StmtFilter> stmtFilters) {
        return filters.removeAll(stmtFilters);
    }

    /**
     * Clears the content of this {@link BooleanOpStmtFilter}. After calling this
     * method, the {@link BooleanOpStmtFilter} accepts all statement.
     */
    public void clear() {
        filters.clear();
    }

    /**
     * Returns the list of member filters of this {@link BooleanOpStmtFilter}
     * @return {@code List<StmtFilter>} containing all the member statement filters
     */
    public List<StmtFilter> getFilters() {
        return filters;
    }
    
}
