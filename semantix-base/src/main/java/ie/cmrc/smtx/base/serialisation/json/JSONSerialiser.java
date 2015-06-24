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

package ie.cmrc.smtx.base.serialisation.json;

import ie.cmrc.smtx.base.serialisation.ElementSetName;
import ie.cmrc.smtx.base.serialisation.Namespaces;
import java.util.Collection;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * A JSON-DL serialiser for SKOS resources
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class JSONSerialiser {
    
    /**
     * Creates an RDF JSON document containing the JSON representation of the
     * provided resource
     * @param resource A {@code JSONisable} instance
     * @param elementSet Specifies the level of information to serialise
     * (see {@link ie.cmrc.semantix.serialisation.ElementSetName} for more details)
     * @param language Language code. Only annotations in this language will
     * be included in the XML.
     * @return JSON object following the JSON-LD specification
     */
    public static JSONObject makeRDFJSONDocument(JSONisable resource, ElementSetName elementSet, String language) {
        JSONObject doc = new JSONObject();
        
        JSONObject context = new JSONObject();
        context.put("@vocab", Namespaces.SKOS.getURI());
        
        doc.put("@context", context);
        
        JSONArray rdfResources = new JSONArray();
        if (resource != null) {
            JSONObject jsonObject = resource.toJSONObject(elementSet, language);
            if (jsonObject != null) rdfResources.add(jsonObject);
        }
        doc.put("@graph", rdfResources);

        return doc;
    }
    
    /**
     * Creates an RDF JSON document containing the JSON representation of the
     * provided resources
     * @param resources A collection of {@code JSONisable} instances
     * @param elementSet Specifies the level of information to serialise
     * (see {@link ie.cmrc.semantix.serialisation.ElementSetName} for more details)
     * @param language Language code. Only annotations in this language will
     * be included in the XML.
     * @return JSON object following the JSON-LD specification
     */
    public static JSONObject makeRDFJSONDocument(Collection<? extends JSONisable> resources, ElementSetName elementSet, String language) {
        JSONObject doc = new JSONObject();
        
        JSONObject context = new JSONObject();
        context.put("@vocab", Namespaces.SKOS.getURI());
        
        doc.put("@context", context);
        
        JSONArray rdfResources = new JSONArray();
        for (JSONisable resource: resources) {
            if (resource != null) {
                JSONObject jsonObject = resource.toJSONObject(elementSet, language);
                if (jsonObject != null) rdfResources.add(jsonObject);
            }
        }
        doc.put("@graph", rdfResources);

        return doc;
    }
    
    /**
     * Creates an RDF JSON document containing the JSON representation of the
     * provided resources
     * @param resources An iterator over {@code JSONisable} instances
     * @param elementSet Specifies the level of information to serialise
     * (see {@link ie.cmrc.semantix.serialisation.ElementSetName} for more details)
     * @param language Language code. Only annotations in this language will
     * be included in the XML.
     * @return JSON object following the JSON-LD specification
     */
    public static JSONObject makeRDFJSONDocument(Iterator<? extends JSONisable> resources, ElementSetName elementSet, String language) {
        JSONObject doc = new JSONObject();
        
        JSONObject context = new JSONObject();
        context.put("@vocab", Namespaces.SKOS.getURI());
        
        doc.put("@context", context);
               
        JSONArray rdfResources = new JSONArray();
        while (resources.hasNext()) {
            
            JSONisable resource = resources.next();
            if (resource != null) {
                
                
                JSONObject jsonObject = resource.toJSONObject(elementSet, language);
                if (jsonObject != null) rdfResources.add(jsonObject);
            }
        }
        doc.put("@graph", rdfResources);

        return doc;
    }

    
}
