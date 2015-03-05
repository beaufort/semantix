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

package ie.cmrc.smtx.etl.ext;

import ie.cmrc.smtx.skos.model.SKOS;


/**
 * A generic interface for data extractors. This interface is independent from the
 * data format and structure.
 * @author Yassine Lassoued
 */
public interface Extractor {
    /**
     * Loads the content of the extractor into a {@link ie.cmrc.skos.core.SKOS}
     * @param thesaurus {@link ie.cmrc.skos.core.SKOS} to load data into
     * @param inferInverseProperties If {@code true} the extractor will
     * infer all inverse object properties
     * @param inferSuperProperties If {@code true} the extractor will
     * infer all super object properties
     * @param inferTransitiveClosure If {@code true} the extractor will infer the
     * transitive closure of transitive relationships
     * @param incrementalSync If {@code true} the extractor will synchronise
     * the thesaurus every so often. This is useful for persistent thesauri. Using
     * incremental synchronisation on in memory thesauri will have no effect.
     * @return {@code true} if the extraction was successful, {@code false} otherwise
     */
    public boolean extractDataToThesaurus(SKOS thesaurus, boolean inferInverseProperties, boolean inferSuperProperties, boolean inferTransitiveClosure, boolean incrementalSync);
}
