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

package ie.cmrc.smtx.skos.jena.util;

import ie.cmrc.smtx.skos.jena.util.impl.ResIteratorWrapper;
import ie.cmrc.smtx.skos.jena.util.impl.StmtSubjectIterator;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * A Factory class for creating {@link JenaResourceIterator} instances
 * @author Yassine Lassoued
 */
public class JenaResourceIteratorFactory {
    
    /**
     * Creates a {@link JenaResourceIterator} over the subjects of a {@code com.hp.hpl.jena.rdf.model.StmtIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.StmtIterator} to wrap as a {@link JenaResourceIterator}
     * @return {@link JenaResourceIterator} that wraps the subjects of the provided iterator
     */
    public static JenaResourceIterator create(StmtIterator iter) {
        return new StmtSubjectIterator(iter);
    }
    
    /**
     * Creates a {@link JenaResourceIterator} that wraps the provided {@code com.hp.hpl.jena.rdf.model.ResIterator}
     * @param iter {@code com.hp.hpl.jena.rdf.model.ResIterator} to wrap as a {@link JenaResourceIterator}
     * @return {@link JenaResourceIterator} that wraps the provided {@code com.hp.hpl.jena.rdf.model.ResIterator}
     */
    public static JenaResourceIterator create(ResIterator iter) {
        return new ResIteratorWrapper(iter);
    }
}
