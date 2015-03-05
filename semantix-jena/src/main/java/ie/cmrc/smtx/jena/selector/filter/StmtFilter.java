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
 * A generic interface that specifies the methods to be supported by filters
 * over Jena statements @code com.hp.hpl.jena.rdf.model.Statement}. A statement
 * filter has a {@linkplain #test(com.hp.hpl.jena.rdf.model.Statement)} method
 * that indicates whether a given statement should be accepted or rejected.
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public interface StmtFilter {
    /**
     * Tests if a statement should be accepted
     * @param statement Statement to test
     * @return {@code true} if the provided statement satisfies the condition of
     * this filter, {@code false} otherwise
     */
    boolean test(Statement statement);
}
