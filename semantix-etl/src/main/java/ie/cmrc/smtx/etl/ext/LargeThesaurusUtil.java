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

import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;
import ie.cmrc.smtx.skos.model.SKOS;

/**
 * An attempt to compute the transitive closure of a large thesaurus
 * @author Yassine Lassoued
 * @deprecated This was useless
 */
public class LargeThesaurusUtil {
    
    /**
     * Computes the transitive closure of a {@code com.hp.hpl.jena.update.GraphStore}
     * and stores it in the same graph store
     * @param graphStore {@code com.hp.hpl.jena.update.GraphStore}
     */
    public static void computeTansitiveClosure(GraphStore graphStore) {
        String broaderTransQueryString = 
                "PREFIX skos: <"+SKOS.NAMESPACE+">\n"
                + "insert { ?x skos:broaderTransitive ?y . ?x skos:semanticRelation ?y }\n"
                + "where { ?x skos:broaderTransitive+ ?y . }";
        
        UpdateRequest broaderTransQuery = UpdateFactory.create(broaderTransQueryString);
        UpdateAction.execute(broaderTransQuery, graphStore);
        
        
        
        String inverseQueryString = 
                "PREFIX skos: <"+SKOS.NAMESPACE+">\n"
                + "insert { ?x skos:narrowerTransitive ?y . ?x skos:semanticRelation ?y }\n"
                + "where { ?y skos:broaderTransitive ?x . }";
        
        UpdateRequest inverseQuery = UpdateFactory.create(inverseQueryString);
        UpdateAction.execute(inverseQuery, graphStore);
    }
}
