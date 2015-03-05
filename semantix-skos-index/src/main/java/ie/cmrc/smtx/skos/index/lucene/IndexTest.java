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

package ie.cmrc.smtx.skos.index.lucene;

import ie.cmrc.smtx.base.SemanticEntity;
import ie.cmrc.smtx.skos.index.SKOSIndex;
import ie.cmrc.smtx.skos.index.SKOSIndexFactory;
import ie.cmrc.smtx.skos.index.Scored;
import ie.cmrc.util.Term;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.index.CorruptIndexException;

/**
 *
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class IndexTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IllegalArgumentException, CorruptIndexException, IOException {
        try (SKOSIndex index = SKOSIndexFactory.createLuceneSKOSIndex("/Users/yassine/umls/index", Arrays.asList("en"))) {
            List<Scored<SemanticEntity>> results = index.search(new Term("cod", "en"), 0, 10);
            for (Scored<SemanticEntity> result : results) {
                SemanticEntity entity = result.getItem();
                System.out.println(entity.getURI());
                System.out.println("    memberOf:           "+entity.getSemanticTypes());
                System.out.println("    memberOfTransitive: "+entity.getSemanticTypesTransitive());
                
            }
        }
    }
    
}
