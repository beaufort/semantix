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

package ie.cmrc.smtx.skos.index;

import ie.cmrc.smtx.skos.index.lucene.LuceneSKOSIndex;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.Collection;
import org.apache.lucene.index.CorruptIndexException;

/**
 * A Factory class for SKOS indexes {@link SKOSIndex}
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class SKOSIndexFactory {
    
    /**
     * Creates a Lucene-based SKOS index object with the provided index
     * directory and resource languages
     * @param indexDir Path of the backend directory (indexed data directory)
     * @param languages Languages supported by the index
     * @return {@link ie.cmrc.smtx.skos.index.lucene.LuceneSKOSIndex} object
     * @throws IllegalArgumentException
     * @throws NotDirectoryException
     * @throws CorruptIndexException
     * @throws IOException 
     */
    public static LuceneSKOSIndex createLuceneSKOSIndex(String indexDir, Collection<String> languages) throws IllegalArgumentException, NotDirectoryException, CorruptIndexException, IOException {
        return new LuceneSKOSIndex(indexDir, languages);
    }
}
