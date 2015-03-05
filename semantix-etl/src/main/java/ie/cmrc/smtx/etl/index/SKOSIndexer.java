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

package ie.cmrc.smtx.etl.index;

import ie.cmrc.smtx.skos.model.SKOS;
import java.io.IOException;
import java.nio.file.NotDirectoryException;

/**
 * A generic interface for SKOS thesaurus indexers. This interface provides one method
 * {@linkplain #indexSKOSThesaurus(ie.cmrc.skos.core.SKOS, java.lang.String)},
 * which indexes a {@link ie.cmrc.skos.core.SKOS} in the provided output index directory.
 * 
 * @author Yassine Lassoued
 */
public interface SKOSIndexer {
    
    /**
     * Indexes the provided SKOS thesaurus and stores the output index in the
     * provided file system directory. If the directory does not exist then this
     * method will create it. If the folder exists and is not empty, then its
     * content will be deleted.
     * @param thesaurus {@link ie.cmrc.skos.core.SKOS} to index
     * @param indexDir Path of the output file system directory index
     * @return {@code true} if indexing was successful, {@code false} otherwise.
     * @throws NotDirectoryException if the provided {@code indexDir} path is that of
     * an existing file that is not a directory
     * @throws IOException If an IO error occurs while connecting to the index directory
     * or writing
     */
    boolean indexSKOSThesaurus(SKOS thesaurus, String indexDir) throws NotDirectoryException, IOException;
    
}
